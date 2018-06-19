package hr.vestigo.modules.collateral.batch.bo85;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo85.BO851.AllocationIterator;
import hr.vestigo.modules.collateral.batch.bo85.BO851.CollSubtypeIdsIterator;
import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;
import hr.vestigo.modules.collateral.common.yoyM.YOYM0;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;



/**
 * Obrada za kreiranje CRM izvješæa
 * @author hrakis
 */
public class BO850 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo85/BO850.java,v 1.4 2017/05/05 09:32:42 hrakis Exp $";

    private BatchContext bc;
    private BO851 bo851;

    private String proc_type;
    private String register_no;
    private String client_type;
    private AllocationTypeData allocationType;
    private Date value_date;

    private YOYM0 yoym0;
    private ArrayList<GcmTypeData> gcmTypes;
    private HashMap<BigDecimal, BigDecimal> collateralSubtypes;
    
    private String fileName;
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheetAnalytic, sheetSyntetic;
    private int rowIndexAnalytic = 0, rowIndexSyntetic = 0;
    
    private final BigDecimal zero = new BigDecimal("0.00");
    


    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo851 = new BO851(bc);

        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat ID-a obrade izraèuna pokrivenosti
        BigDecimal col_pro_id = bo851.selectColProId(value_date, proc_type);
        if (col_pro_id == null)
        {
            error("Obrada nije izvrsena!", null);
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        // mapiranje kolaterala
        yoym0 = new YOYM0(bc, "crm_report", value_date);
        gcmTypes = yoym0.getGcmTypes();
        
        // uèitavanje podtipova kolaterala
        loadCollateralSubtypes();
        
        // dohvat podataka
        AllocationIterator iter = bo851.selectData(col_pro_id, register_no, client_type, allocationType);
        
        // zapisivanje podataka u izvještaj
        try
        {
            openReport();
            writeTitles();
            writeHeaders();
            writeRows(iter);
            saveReport();
        }
        finally
        {
            dispose();
            if (iter != null) iter.close();
        }
        
        // slanje maila
        sendMail();

        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }



    /**
     * Metoda uèitava ID-eve podtipova kolaterala za one kolaterale koji imaju podtip. 
     */
    private void loadCollateralSubtypes() throws Exception
    {
        bc.startStopWatch("BO850.loadCollateralSubtypes");
        
        collateralSubtypes = new HashMap<BigDecimal, BigDecimal>();
        
        CollSubtypeIdsIterator iter = bo851.selectCollSubtypeIds();
        while (iter.next())
        {
            collateralSubtypes.put(iter.col_hea_id(), iter.col_sub_id());
        }
        
        bc.stopStopWatch("BO850.loadCollateralSubtypes");
    }
    
    
    /**
     * Metoda otvara izvještaj i kreira sheetove u koje æe biti zapisani podaci. 
     */
    private void openReport()
    {
        // kreiraj workbook
        this.workbook = new SXSSFWorkbook(100);
        this.styles = ExcelStyleData.createStyles(workbook);
        
        // kreiraj sheetove
        this.sheetAnalytic = workbook.createSheet("Analitika");
        ExcelUtils.setColumnWidths(sheetAnalytic, new int[] { 100, 65, 400, 130, 110, 200, 70, 110, 110, 130, 100, 100 });
        
        this.sheetSyntetic = workbook.createSheet("Sintetika");
        int[] widthsFixed = new int[] { 100, 65, 400, 150, 100, 110 };
        int[] widths = new int[widthsFixed.length + gcmTypes.size() + 3];
        for (int i = 0; i < widthsFixed.length; i++) widths[i] = widthsFixed[i];
        for (int i = widthsFixed.length; i < widths.length; i++) widths[i] = 110;
        ExcelUtils.setColumnWidths(sheetSyntetic, widths);
    }

    
    /**
     * Metoda zapisuje naziv izvještaja u analitièki i sintetièki sheet.
     */
    private void writeTitles()
    {
        writeTitle(sheetAnalytic);
        writeTitle(sheetSyntetic);
    }
    
    /**
     * Metoda zapisuje naziv izvještaja u zadani sheet.
     * @param sheet sheet u koji se upisuje naziv izvještaja.
     */
    private void writeTitle(Sheet sheet)
    {
        Row row = sheet.createRow(0);
        ExcelUtils.createCell(row, 0, value_date, styles.titleDateStyle);
        
        row = sheet.createRow(1);
        String title = "Izvje\u0161\u0107e za CRM s " + (allocationType.ponderType == PonderType.Ponder ? "ponderiranim" : "neponderiranim") + " vrijednostima";
        ExcelUtils.createCell(row, 0, title, styles.titleStyle);
        
        row = sheet.createRow(2);
        title = allocationType.eligibilityType.toString();
        if (allocationType.additionalType == AdditionalType.Unlimited) title += " - Alocirani kolateral bez rezanja na izlo\u017Eenost";
        ExcelUtils.createCell(row, 0, title, styles.titleStyle);
        
        row = sheet.createRow(3);
        ExcelUtils.createCell(row, 0, "Iznosi su u HRK", styles.headingStyle);
    }

    
    /**
     * Metoda zapisuje zaglavlje u analitièki i sintetièki sheet.
     */
    private void writeHeaders()
    {
        writeAnalyticHeader();
        writeSynteticHeader();
    }
    
    /**
     * Metoda zapisuje zaglavlje u analitièki sheet.
     */
    private void writeAnalyticHeader()
    {
        Row row = sheetAnalytic.createRow(4);
        row.setHeightInPoints(74);
        
        int columnIndex = 0;
        ExcelUtils.createCell(row, columnIndex++, "Interni MB komitenta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "B2 asset class", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv komitenta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Izlo\u017Eenost", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrsta kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Ponder", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Osigurano", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Neosigurano", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija okvira", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Kolateral direktno povezan na plasman iz okvira", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "SSP plasmani", styles.headingStyle);
        
        sheetAnalytic.createFreezePane(0, 5);
    }
    
    /**
     * Metoda zapisuje zaglavlje u sintetièki sheet.
     */
    private void writeSynteticHeader()
    {
        Row row = sheetSyntetic.createRow(4);
        row.setHeightInPoints(74);
        
        int columnIndex = 0;
        ExcelUtils.createCell(row, columnIndex++, "Interni MB komitenta korisnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "B2 asset class korisnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv komitenta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija okvira", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Izlo\u017Eenost", styles.headingStyle);
        for (GcmTypeData gcmType : gcmTypes) ExcelUtils.createCell(row, columnIndex++, gcmType.name, styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Osigurano", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Neosigurano", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "SSP plasmani", styles.headingStyle);
        
        sheetSyntetic.createFreezePane(0, 5);
    }
    
    
    
    /**
     * Metoda zapisuje retke s podacima u analitièki i sintetièki sheet.
     * @param iter iterator s podacima
     */
    private void writeRows(AllocationIterator iter) throws Exception
    {
        rowIndexAnalytic = rowIndexSyntetic = 5;
        
        BigDecimal old_cus_acc_id = null;
        CRMData data = null;
        
        while (iter.next())
        {
            if (!iter.cus_acc_id().equals(old_cus_acc_id))
            {
                if (data != null) writeSynteticRow(data);
                data = new CRMData(gcmTypes);
                old_cus_acc_id = iter.cus_acc_id();
            }
            
            fillDataFromIterator(data, iter);
            
            data.col_sub_id = collateralSubtypes.get(data.col_hea_id);
            
            GcmTypeData gcmType = yoym0.resolve(data.col_cat_id, data.col_typ_id, data.col_sub_id);
            
            if (gcmType != null)
            {
                data.amounts.put(gcmType, data.amounts.get(gcmType).add(data.exp_coll_amount));
                writeAnalyticRow(data);
            }
            else
            {
                if(data.col_hea_id != null)
                {
                    bc.debug("Kolateral nema pripadajuce mapiranje. COL_HEA_ID=" + data.col_hea_id + ", COL_CAT_ID=" + data.col_cat_id + ", COL_TYP_ID=" + data.col_typ_id + ", COL_SUB_ID=" + data.col_sub_id);
                }
            }
        }
        
        if (data != null) writeSynteticRow(data);
        
        info("Zapisano " + rowIndexAnalytic + " redova u analiticki sheet i " + rowIndexSyntetic + " u sinteticki sheet.");
    }
    

    /**
     * Metoda zapisuje redak s podacima u analitièki sheet.
     * @param data objekt s podacima
     */
    private void writeAnalyticRow(CRMData data)
    {
        bc.startStopWatch("BO850.writeAnalyticRow");
        
        Row row = sheetAnalytic.createRow(rowIndexAnalytic);
        int columnIndex = 0;
        
        ExcelUtils.createCell(row, columnIndex++, data.register_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.b2_asset_class, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.cus_acc_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.exp_balance_hrk, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.col_type_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.ponder, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.exp_coll_amount, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.exp_balance_hrk.subtract(data.exp_coll_amount).max(zero), styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.frame_cus_acc_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.frame_cus_acc_no != null && "1".equals(data.dir_rel_indic) ? "*" : null, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.isSSP() ? "SSP" : null, styles.normalStyle);
        
        rowIndexAnalytic++;
        
        bc.stopStopWatch("BO850.writeAnalyticRow");
    }

    /**
     * Metoda zapisuje redak s podacima u sintetièki sheet.
     * @param data objekt s podacima
     */
    private void writeSynteticRow(CRMData data)
    {
        bc.startStopWatch("BO850.writeSynteticRow");
        
        Row row = sheetSyntetic.createRow(rowIndexSyntetic);
        int columnIndex = 0;
        
        ExcelUtils.createCell(row, columnIndex++, data.register_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.b2_asset_class, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.cus_acc_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.frame_cus_acc_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.exp_balance_hrk, styles.normalNumericStyle);
        for (GcmTypeData gcmType : gcmTypes) ExcelUtils.createCell(row, columnIndex++, data.amounts.get(gcmType), styles.normalNumericStyle);
        BigDecimal allocatedSum = data.getAllocatedSum();
        ExcelUtils.createCell(row, columnIndex++, allocatedSum, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.exp_balance_hrk.subtract(allocatedSum).max(zero), styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.isSSP() ? "SSP" : null, styles.normalStyle);
        
        rowIndexSyntetic++;
        
        bc.stopStopWatch("BO850.writeSynteticRow");
    }

    
    
    /**
     * Metoda puni data objekt s podacima iz iteratora.
     * @param data data objekt
     * @param iter iterator s podacima
     */
    private void fillDataFromIterator(CRMData data, AllocationIterator iter) throws Exception
    {
        data.cus_id = iter.cus_id();
        data.register_no = iter.register_no();
        data.name = iter.name();
        data.b2_asset_class = iter.b2_asset_class();
        data.cus_acc_no = iter.cus_acc_no();
        data.cus_acc_id = iter.cus_acc_id();
        data.exp_balance_hrk = iter.exp_balance_hrk();
        data.frame_cus_acc_no = iter.frame_cus_acc_no();
        data.module_code = iter.module_code();
        data.cus_acc_orig_st = iter.cus_acc_orig_st();
        data.dir_rel_indic = iter.dir_rel_ind();

        data.col_hea_id = iter.col_hea_id();
        data.exp_coll_amount = iter.exp_coll_amount();
        data.col_cat_id = iter.col_cat_id();
        data.col_typ_id = iter.col_typ_id();
        data.col_type_name = iter.col_type_name();
        data.ponder = iter.ponder();
    }

    
    /**
     * Metoda generira ime izlazne datoteke na temelju ulaznih parametara obrade.
     */
    private void generateFileName()
    {
        String dir = bc.getOutDir() + "/";
        String mod = "CUS";
        if ("P".equals(client_type)) mod = "CO"; else if ("F".equals(client_type)) mod = "RET";
        String dateString = new SimpleDateFormat("yyyyMMdd").format(value_date);
        String pond = allocationType.ponderType == PonderType.Ponder ? "POND" : "NEPOND";

        String fileNameCore = dir + "CRM_" + mod + "_" + dateString + "_" + allocationType.eligibilityType.name() + "_" + pond + "<suffix>.xlsx";
        
        if (allocationType.dataType == DataType.GeneralLedger) fileNameCore = fileNameCore.replaceFirst("<suffix>", "_FINAL");
        else if (allocationType.additionalType == AdditionalType.Micro) fileNameCore = fileNameCore.replaceFirst("<suffix>", "_MICRO");
        else if (allocationType.additionalType == AdditionalType.Unlimited) fileNameCore = fileNameCore.replaceFirst("<suffix>", "_UNLIMITED");
        else fileNameCore = fileNameCore.replaceFirst("<suffix>", "");
        
        this.fileName = fileNameCore;
    }
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku i kreira pripadajuæu marker datoteku.  
     */
    private void saveReport() throws Exception
    {
        bc.startStopWatch("BO850.saveReport");
        
        generateFileName();
        bc.info("Izlazna datoteka: " + fileName);
        
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        dispose();
        
        new File(fileName + ".marker").createNewFile();
        
        bc.stopStopWatch("BO850.saveReport");
    }
    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose()
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }

    /**
     * Metoda šalje mail s generiranim izvješæem naruèitelju obrade.
     */
    private void sendMail() throws Exception
    {
        bc.startStopWatch("BO850.sendMail");
        YXY70.send(bc, "csv257", bc.getLogin(), fileName);
        bc.stopStopWatch("BO850.sendMail");
    }


    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO850.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6502763704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Obrada za kreiranje CRM izvjesca");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO850.insertIntoEvent");
            return eve_id;
        }
        catch(Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade!", ex);
            throw ex;
        }
    }
    
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>value_date</dt><dd>Datum valute (datum za koji se radi izvještaj).</dd>
     *    <dt>register_no</dt><dd>Interni MB komitenta (ostavlja se prazno ako je popunjen parametar Vrsta komitenta).</dd>
     *    <dt>client_type</dt><dd>Vrsta komitenta (P za pravne osobe ili F za fizièke osobe; ostavlja se prazno ako je popunjen parametar Interni MB komitenta).</dd>
     *    <dt>proc_type</dt><dd>Oznaka vrste obrade</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Datum za koji se radi izvjestaj", "Interni MB komitenta", "Vrsta komitenta", "Oznaka vrste obrade" };

            // ispiši parametre predane obradi
            info("Parametri predani obradi:");
            for (int i = 0; i < bc.getArgs().length; i++)
            {
                String parameterName = (i < bc.getArgs().length) ? parameterNames[i] : "Nepoznati parametar";
                String parameterValue = bc.getArg(i);
                info("   " + parameterName + " = " + parameterValue);
            }

            // provjeri da li je broj parametara ispravan
            if (bc.getArgs().length != parameterNames.length)
            {
                error("Neispravan broj parametara - predani broj parametara je " + bc.getArgs().length + ", a obrada prima " + parameterNames.length + "!", null);  
                return false;
            }
            
            // provjeri oznaku banke
            String bank_sign = bc.getArg(0);
            if (!bank_sign.equals("RB"))
            {
                error("Oznaka banke mora biti RB!", null);
                return false;
            }
            bc.setBankSign(bank_sign);
            
            // datum valute
            value_date = DateUtils.parseDate(bc.getArg(1).trim());

            // interni MB komitenta
            if (bc.getArg(2).trim().equals("") || bc.getArg(2).trim().equalsIgnoreCase("X")) register_no = null; else register_no = bc.getArg(2).trim();

            // vrsta komitenta
            if (bc.getArg(3).trim().equals("") || bc.getArg(3).trim().equalsIgnoreCase("X")) client_type = null; else client_type = bc.getArg(3).trim();
            if (client_type != null && !client_type.equals("P") && !client_type.equals("F")) throw new Exception("Dozvoljene vrijednosti za vrstu komitenta su P, F ili blank!");
            
            if (register_no == null && client_type == null) throw new Exception("Jedan od parametara mora biti predan: interni MB komitenta ili vrsta komitenta!");

            // oznaka vrste obrade
            proc_type = bc.getArg(4);
            this.allocationType = AllocationTypeData.getAllocationType(proc_type);
            info("Vrsta izracuna pokrivenosti: " + allocationType);
            
            return true;
        }
        catch (Exception ex)
        {
            error("Neispravno zadani parametri!", ex);
            return false;
        }
    }
    
    
    private void error(String message, Exception ex) throws Exception
    {
        if (ex != null) bc.error(message, ex); else bc.error(message, new String[]{});
        bc.userLog("GRESKA: " + message);
    }
    
    private void info(String message) throws Exception
    {
        bc.info(message);
        bc.userLog(message);
    }


    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("6502762704"));
        bp.setArgs(args);
        new BO850().run(bp);
    }
}