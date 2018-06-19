package hr.vestigo.modules.collateral.batch.bo80;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo80.BO801.CollSubtypeIdsIterator;
import hr.vestigo.modules.collateral.batch.bo80.BO801.CollateralSubtypeIterator;
import hr.vestigo.modules.collateral.batch.bo80.BO801.DataIterator;
import hr.vestigo.modules.collateral.batch.bo85.CRMData;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;
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
 * Izvješæe o dospijeæu kolaterala i plasmana
 * @author hrakis
 */
public class BO800 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo80/BO800.java,v 1.3 2017/05/04 10:32:00 hrakis Exp $";
    
    private BatchContext bc;
    private BO801 bo801;
    private YRXX0 yrxx0;
    
    private Date report_date;
    
    private BigDecimal col_pro_id;
    
    private HashMap<BigDecimal, String> subtypes = new HashMap<BigDecimal, String>();
    private HashMap<BigDecimal, BigDecimal> collateralSubtypes = new HashMap<BigDecimal, BigDecimal>();
    
    private String fileName;
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheet;
    private int rowIndex = 0;
    
    private final BigDecimal hrk_cur_id = new BigDecimal("63999");
    private final BigDecimal eur_cur_id = new BigDecimal("64999");
    
    private final BigDecimal corporate_cus_typ_id1 = new BigDecimal("2999");
    private final BigDecimal corporate_cus_typ_id2 = new BigDecimal("2998");
    private final BigDecimal corporate_cus_typ_id3 = new BigDecimal("999");
    
    private final BigDecimal mjen_ds_id = new BigDecimal("81777");
    private final BigDecimal zadu_ds_id = new BigDecimal("80777");

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo801 = new BO801(bc);
        this.yrxx0 = new YRXX0(bc);
        
        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();
 
        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvati ID obrade izraèuna pokrivenosti
        col_pro_id = bo801.selectColProId(report_date);
        if (col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.info("COL_PRO_ID = " + col_pro_id);
        
        // dohvati sve moguæe podvrste kolaterala
        bc.debug("Podvrste kolaterala:");
        CollateralSubtypeIterator iterSubtype = bo801.selectCollateralSubtypes();
        while (iterSubtype.next())
        {
            subtypes.put(iterSubtype.col_sub_id(), iterSubtype.name());
            bc.debug("   " + iterSubtype.col_sub_id() + " " + iterSubtype.name());
        }
        iterSubtype.close();
        
        // dohvati kolaterale i njihove podvrste
        CollSubtypeIdsIterator iterCollSubtypeIds = bo801.selectCollSubtypeIds();
        while (iterCollSubtypeIds.next())
        {
            collateralSubtypes.put(iterCollSubtypeIds.col_hea_id(), iterCollSubtypeIds.col_sub_id());
        }
        iterCollSubtypeIds.close();
        
        // dohvat podataka za izvještaj
        ArrayList<CollateralData> collateralItems = new ArrayList<CollateralData>();
        DataIterator iter = bo801.selectData(col_pro_id, report_date);
        
        try
        {
            // otvori izvještaj
            openReport();
            
            // procesiraj dohvaæene podatke
            BigDecimal col_hea_id = null;
            while (iter.next())
            {
                if (!iter.col_hea_id().equals(col_hea_id))
                {
                    processCollateral(collateralItems);
                    col_hea_id = iter.col_hea_id();
                    collateralItems = new ArrayList<CollateralData>();
                }
                bc.debug(iter.col_num().trim() + " <-> " + iter.hf_priority() + " <-> " + iter.cus_acc_no() + " (due date = " + iter.placement_due_date() + ", cus_typ_id = " + iter.cus_typ_id() +")");
                collateralItems.add(loadData(iter));
            }
            processCollateral(collateralItems);
            
            // spremi generirani izvještaj
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
     * Metoda obraðuje sve zapise jednog kolaterala.
     * @param collateralItems kolekcija zapisa o kolateralu
     */
    public void processCollateral(ArrayList<CollateralData> collateralItems) throws Exception
    {
        if (collateralItems == null || collateralItems.size() == 0) return;
        
        // kolateral je Corporate kolateral ako je barem jedan vlasnik povezanih plasmana Corporate komitent 
        boolean isCorporateCollateral = false;
        for (CollateralData item : collateralItems)
        {
            if (item.isCorporate)
            {
                isCorporateCollateral = true;
                break;
            }
        }
        bc.debug("Corporate kolateral = " + isCorporateCollateral);
        
        if (!isCorporateCollateral) return;
        
        CollateralData collateral = collateralItems.get(0);
        
        boolean isCollateralWithMortgage = collateral.coll_hf_prior_id != null;
        bc.debug("Kolateral ima hipoteku = " + isCollateralWithMortgage);
        
        boolean isMjenZadDs = mjen_ds_id.equals(collateral.col_typ_id) || zadu_ds_id.equals(collateral.col_typ_id);
        
        // odredi datum dospijeæa kolaterala
        Date collateral_due_date = null;
        if (isCollateralWithMortgage || isMjenZadDs)  // kolateral ima hipoteku - datum dospijeæa kolaterala je najveæi od datuma dospijeæa povezanih plasmana
        {
            for (CollateralData item : collateralItems)
            {
                if (collateral_due_date == null || item.placement_due_date.after(collateral_due_date))
                {
                    collateral_due_date = item.placement_due_date;
                }
            }
        }
        else  // kolateral nema hipoteku - dohvaæa se datum ovisno o vrsti kolaterala
        {
            collateral_due_date = bo801.selectCollateralDueDate(collateral);
        }
        bc.debug("Datum dospijeca kolaterala = " + collateral_due_date);
        
        // zapiši podatke u izvještaj
        bc.debug("Zapisujem u izvjestaj - broj redova = " + collateralItems.size());
        for (CollateralData item : collateralItems)
        {
            item.collateral_due_date = collateral_due_date;
            writeRow(item);
        }
    }
    
    
    /**
     * Metoda otvara izvještaj, kreira sheet i u njemu zapisuje zaglavlje.
     */
    private void openReport()
    {
        bc.startStopWatch("BO800.openReport");
        
        // kreiraj workbook
        this.workbook = new SXSSFWorkbook(100);
        this.styles = ExcelStyleData.createStyles(workbook);
        
        // kreiraj sheet
        this.sheet = workbook.createSheet();
        ExcelUtils.setColumnWidths(sheet, new int[] { 150, 90, 320, 230, 120, 100, 200, 120, 110, 100, 100, 100 });
        
        // zapiši zaglavlje
        writeHeader();
        
        bc.stopStopWatch("BO800.openReport");
    }
    
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku.
     */
    private void saveReport() throws Exception
    {
        bc.startStopWatch("BO800.saveReport");
        
        // složi ime izlazne datoteke
        String dir = bc.getOutDir() + "/";
        String dateString = new SimpleDateFormat("yyyyMMdd").format(report_date);
        fileName = dir + "DospijeceKolateralaIPlasmana" + dateString + ".xlsx";
        bc.info("Izlazna datoteka = " + fileName);
        
        // spremi izvješæe u izlaznu datoteku
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        info("Zapisano " + rowIndex + " redova u izvjesce.");
        
        // napravi marker datoteku
        new File(fileName + ".marker").createNewFile();
        
        bc.stopStopWatch("BO800.saveReport");
    }
    
    
    /**
     * Metoda šalje generirano izvješæe naruèitelju na mail. 
     */
    private void sendMail() throws Exception
    {
        bc.startStopWatch("BO800.sendMail");
        YXY70.send(bc, "csv261", bc.getLogin(), fileName);
        bc.stopStopWatch("BO800.sendMail");
    }
    
    
    
    /**
     * Metoda zapisuje redak s podacima u analitièki sheet.
     * @param data objekt s podacima
     */
    private void writeRow(CollateralData item)
    {
        bc.startStopWatch("BO800.writeRow");
        
        Row row = sheet.createRow(rowIndex);
        int columnIndex = 0;
        
        ExcelUtils.createCell(row, columnIndex++, item.col_num, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, item.hf_priority, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, item.col_type_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, item.subtype, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, item.collateral_value_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, item.register_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, item.name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, item.cus_acc_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, item.placement_exposure_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, item.placement_due_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, item.collateral_due_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, item.fra_agr_date_until, styles.normalDateStyle);
        
        rowIndex++;
        
        bc.stopStopWatch("BO800.writeRow");
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u sheet.
     */
    private void writeHeader()
    {
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(45);
        
        int columnIndex = 0;
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Red hipoteke", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrsta kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Podvrsta kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tr\u017Ei\u0161na vrijednost u EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Interni MB korisnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv korisnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Izlo\u017Eenost plasmana u EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum dospije\u0107a plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum dospije\u0107a kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum dospije\u0107a DS", styles.headingStyle);
        rowIndex++;
        
        sheet.createFreezePane(0, rowIndex);
    }

    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose()
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }


    /**
     * Metoda koja kreira data objekt na temelju podataka iz iteratora.
     * @param iter iterator s podacima
     * @return objekt s podacima
     */
    public CollateralData loadData(DataIterator iter) throws Exception
    {
        CollateralData data = new CollateralData();
        data.col_hea_id = iter.col_hea_id();
        data.col_num = iter.col_num().trim();
        data.collateral_value_eur = yrxx0.exchange(iter.real_est_nomi_value(), iter.real_est_nm_cur_id(), eur_cur_id, report_date);
        data.col_cat_id = iter.col_cat_id();
        data.col_typ_id = iter.col_typ_id();
        data.col_type_name = iter.col_typ_name();
        data.coll_hf_prior_id = iter.coll_hf_prior_id();
        data.hf_priority = iter.hf_priority();
        data.cus_acc_id = iter.cus_acc_id();
        data.cus_acc_no = iter.cus_acc_no().trim();
        data.placement_due_date = iter.placement_due_date();
        data.placement_exposure_eur = yrxx0.exchange(iter.exp_balance_hrk(), hrk_cur_id, eur_cur_id, report_date);
        data.register_no = iter.register_no().trim();
        data.name = iter.name().trim();
        data.isCorporate = iter.cus_typ_id().equals(corporate_cus_typ_id1) || iter.cus_typ_id().equals(corporate_cus_typ_id2) || iter.cus_typ_id().equals(corporate_cus_typ_id3);
        data.subtype = subtypes.get(collateralSubtypes.get(data.col_hea_id));
        data.fra_agr_date_until = iter.fra_agr_date_until();
        return data;
    }
    
    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    public BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6526069704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Izvjesce o dospijecu kolaterala i plasmana");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("insertIntoEvent");
            return eve_id;
        }
        catch (Exception ex)
        {
            bc.error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade u tablicu EVENT!", ex);
            throw ex;
        }
    }
        
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>report_date</dt><dd>Datum izvjestaja.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Datum izvjestaja" };

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
            
            // datum izvještaja
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            report_date = new Date(dateFormat.parse(bc.getArg(1).trim()).getTime());            
            
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
        BatchParameters bp = new BatchParameters(new BigDecimal("6526066704"));
        bp.setArgs(args);
        new BO800().run(bp);
    }
}