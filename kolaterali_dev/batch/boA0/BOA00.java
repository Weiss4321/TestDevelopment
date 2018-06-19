package hr.vestigo.modules.collateral.batch.boA0;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.boA0.BOA01.DataIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Izvješæe o koncentraciji kolaterala.
 * @author hrakis
 */
public class BOA00 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/boA0/BOA00.java,v 1.3 2016/07/21 10:29:43 hrakis Exp $";
    
    private BatchContext bc;
    private BOA01 boA01;
    
    private Date value_date;
    
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheetSyntetic, sheetAnalytic;
    private int rowIndexSyntetic, rowIndexAnalytic;
    private int columnIndexSyntetic, columnIndexAnalytic;
    
    private final BigDecimal zero = new BigDecimal("0.00");
    private final BigDecimal hrk_cur_id = new BigDecimal("63999");
    private final BigDecimal garancije_col_cat_id = new BigDecimal("615223");
    private final BigDecimal police_col_cat_id = new BigDecimal("616223");
    private final BigDecimal dionice_col_cat_id = new BigDecimal("613223");
    private final BigDecimal obveznice_col_cat_id = new BigDecimal("619223");
    private final BigDecimal udjeli_u_fondovima_col_cat_id = new BigDecimal("622223");
    private final BigDecimal zapisi_col_cat_id = new BigDecimal("627223");
    

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.boA01 = new BOA01(bc);
        YRXX0 yrxx0 = new YRXX0(bc);
        HashMap<BigDecimal, SynteticData> syntetic = new HashMap<BigDecimal, SynteticData>();

        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        info("DATUM VALUTE = " + value_date);
        
        // dohvati podatke za izvještaj
        DataIterator iter = boA01.selectData(value_date);

        try
        {
            // kreiraj workbook
            workbook = new SXSSFWorkbook(100);
            styles = ExcelStyleData.createStyles(workbook);
                
            // kreiraj sheetove i zapiši header u njih
            sheetAnalytic = workbook.createSheet("Pripremni podaci");
            sheetSyntetic = workbook.createSheet("Final koncentracija");
            ExcelUtils.setColumnWidths(sheetAnalytic, new int[] { 150, 80, 250, 100, 50, 100, 70, 250, 120, 90, 50, 70, 160 });
            ExcelUtils.setColumnWidths(sheetSyntetic, new int[] { 100, 300, 120 });
            writeHeaderAnalytic();
            writeHeaderSyntetic();

            // zapiši podatke u analitièki izvještaj i priredi podatke za sintetièki izvještaj
            while (iter.next())
            {
                AnalyticData data = createDataFromIterator(iter);
                
                // ako je VRP, vlasnik je izdavatelj VRP-a
                if (iter.col_cat_id().equals(dionice_col_cat_id) || iter.col_cat_id().equals(obveznice_col_cat_id) || iter.col_cat_id().equals(udjeli_u_fondovima_col_cat_id) || iter.col_cat_id().equals(zapisi_col_cat_id))
                {
                    boA01.selectVrpIssuer(data);
                }
                
                // ako je garancija, vlasnik je izdavatelj garancije
                if (iter.col_cat_id().equals(garancije_col_cat_id))
                {
                    boA01.selectGuaranteeIssuer(data);
                }
                
                // ako je polica osiguranja, vlasnik je osiguranik, a vrijednost kolaterala akumulirana ili otkupna vrijednost 
                if (iter.col_cat_id().equals(police_col_cat_id))
                {
                    boA01.selectInsuredCustomer(data);
                    data.real_est_nomi_valu = data.acum_buy_value;
                }
                
                if (data.real_est_nomi_valu == null || data.real_est_nm_cur_id == null) continue;
                data.value_hrk = yrxx0.exchange(data.real_est_nomi_valu, data.real_est_nm_cur_id, hrk_cur_id, value_date);
                
                if (data.collateral_owner_cus_id == null || data.collateral_owner_register_no == null) continue;
                data.collateral_owner_register_no = data.collateral_owner_register_no.trim();
                
                writeRowAnalytic(data);
                
                SynteticData dataSynt = syntetic.get(data.collateral_owner_cus_id);
                if (dataSynt == null)
                {
                    dataSynt = new SynteticData();
                    dataSynt.collateral_owner_cus_id = data.collateral_owner_cus_id;
                    dataSynt.collateral_owner_register_no = data.collateral_owner_register_no;
                    dataSynt.collateral_owner_name = data.collateral_owner_name;
                    dataSynt.value_hrk = zero;
                    syntetic.put(dataSynt.collateral_owner_cus_id, dataSynt);
                }
                if (!dataSynt.collaterals.contains(data.col_hea_id))
                {
                    dataSynt.value_hrk = dataSynt.value_hrk.add(data.value_hrk);
                    dataSynt.collaterals.add(data.col_hea_id);
                }
            }
            iter.close();
            
            // zapiši podatke u sintetièki izvještaj
            for (SynteticData dataSynt : syntetic.values())
            {
                writeRowSyntetic(dataSynt);
            }
            
            info("Ukupan broj redova u analitickom izvjestaju: " + rowIndexAnalytic);
            info("Ukupan broj redova u sintetickom izvjestaju: " + rowIndexSyntetic);

            // spremi izvještaj
            String dateString = new SimpleDateFormat("yyyy_MM_dd").format(value_date);
            String fileName = bc.getOutDir() + "/" + "Koncentracija_" + dateString + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            new File(fileName + ".marker").createNewFile();

            // pošalji izvještaj na mail
            YXY70.send(bc, "csvboA0", bc.getLogin(), fileName);
            info("Izvjestaj je kreiran i poslan na mail.");
        }
        catch (Exception ex)
        {
            error("Dogodila se nepredvidjena greska kod kreiranja izvjestaja!", ex);
            throw ex;
        }
        finally
        {
            dispose(workbook);
        }

        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    private AnalyticData createDataFromIterator(DataIterator iter) throws Exception
    {
        AnalyticData data = new AnalyticData();
        data.col_hea_id = iter.col_hea_id();
        data.col_num = iter.col_num().trim();
        data.col_cat_id = iter.col_hea_id();
        data.real_est_nomi_valu = iter.real_est_nomi_valu();
        data.real_est_nm_cur_id = iter.real_est_nm_cur_id();
        data.real_est_nm_cur_code = iter.real_est_nm_cur_code();
        data.acum_buy_value = iter.acum_buy_value();
        data.cus_acc_no = iter.cus_acc_no();
        data.request_no = iter.request_no();
        data.cus_acc_status = iter.cus_acc_status();
        data.cus_acc_orig_st = iter.cus_acc_orig_st();
        data.placement_owner_register_no = iter.placement_owner_register_no();
        data.placement_owner_name = iter.placement_owner_name();
        data.collateral_owner_cus_id = iter.collateral_owner_cus_id();
        data.collateral_owner_register_no = iter.collateral_owner_register_no();
        data.collateral_owner_name = iter.collateral_owner_name();
        return data;
    }
    
    
    /**
     * Metoda zapisuje podatke u analitièki izvještaj.
     * @param data objekt s podacima
     */
    private void writeRowAnalytic(AnalyticData data) throws Exception
    {
        columnIndexAnalytic = 0;
        
        Row row = sheetAnalytic.createRow(rowIndexAnalytic);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.col_num, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.collateral_owner_register_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.collateral_owner_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.real_est_nomi_valu, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.real_est_nm_cur_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.value_hrk, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.placement_owner_register_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.placement_owner_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.cus_acc_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.request_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.cus_acc_status, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, data.cus_acc_orig_st, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Lista aktivnih kolaterala", styles.normalStyle);
        
        rowIndexAnalytic++;
    }
    
    
    /**
     * Metoda zapisuje podatke u sintetièki izvještaj.
     * @param data objekt s podacima
     */
    private void writeRowSyntetic(SynteticData data) throws Exception
    {
        columnIndexSyntetic = 0;
        
        Row row = sheetSyntetic.createRow(rowIndexSyntetic);
        ExcelUtils.createCell(row, columnIndexSyntetic++, data.collateral_owner_register_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexSyntetic++, data.collateral_owner_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndexSyntetic++, data.value_hrk, styles.normalNumericStyle);
        
        rowIndexSyntetic++;
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u analitièki izvještaj.
     */
    private void writeHeaderAnalytic() throws Exception
    {
        rowIndexAnalytic = 0;
        columnIndexAnalytic = 0;
        
        Row row = sheetAnalytic.createRow(rowIndexAnalytic);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "ID vlasnika / izdavatelja", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Naziv vlasnika / izdavatelja", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Iznos kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Valuta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Tr\u017Ei\u0161na u HRK", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "ID korisnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Naziv korisnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Broj zahtjeva", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "DWH status", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "Originalni status plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexAnalytic++, "\u0160ifra liste", styles.headingStyle);
        
        rowIndexAnalytic++;
        
        sheetAnalytic.createFreezePane(1, rowIndexAnalytic);
    }

    
    /**
     * Metoda zapisuje zaglavlje u sintetièki izvještaj.
     */
    private void writeHeaderSyntetic() throws Exception
    {
        rowIndexSyntetic = 0;
        columnIndexSyntetic = 0;
        
        Row row = sheetSyntetic.createRow(rowIndexSyntetic);
        ExcelUtils.createCell(row, columnIndexSyntetic++, "ID vlasnika / izdavatelja kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexSyntetic++, "Vlasnik kolaterala / izdavatelj kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndexSyntetic++, "Tr\u017Ei\u0161na vrijednost kolaterala u kunama", styles.headingStyle);
        
        rowIndexSyntetic++;
        
        sheetSyntetic.createFreezePane(0, rowIndexSyntetic);
    }
    
    
    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose(Workbook workbook)
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }


    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO970.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("676234327"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Izvjesce o koncentraciji kolaterala");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO970.insertIntoEvent");
            return eve_id;
        }
        catch (Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri evidentiranju pocetka izvodjenja obrade!", ex);
            throw ex;
        }
    }


    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>value_date</dt><dd>Datum valute. Opcionalan parametar - ako se ne preda, uzima se zadnji dan u prethodnom mjesecu.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Datum valute" };

            // ispiši parametre predane obradi
            info("Parametri predani obradi:");
            for (int i = 0; i < bc.getArgs().length; i++)
            {
                String parameterName = (i < bc.getArgs().length) ? parameterNames[i] : "Nepoznati parametar";
                String parameterValue = bc.getArg(i);
                info("   " + parameterName + " = " + parameterValue);
            }

            // provjeri da li je broj parametara ispravan
            if (bc.getArgs().length < parameterNames.length - 1 || bc.getArgs().length > parameterNames.length)
            {
                error("Neispravan broj parametara - predani broj parametara je " + bc.getArgs().length + ", a obrada prima " + (parameterNames.length - 1) + " ili " + parameterNames.length + "!", null);  
                return false;
            }
            
            // provjeri oznaku banke
            String bank_sign = bc.getArg(0);
            if (!bank_sign.equals("RB"))
            {
                error("Oznaka banke mora biti RB!", null);
                return false;
            }
            bc.setBankSign("RB");
            
            // datum valute - ako je predan uzmi njega, inaèe uzmi zadnji dan u prethodnom mjesecu
            if (bc.getArgs().length == parameterNames.length)
            {
                value_date = DateUtils.parseDate(bc.getArg(1).trim());
            }
            else
            {
                value_date = new Date(bc.getExecStartTime().getTime());  // tekuæi datum
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(value_date);
                calendar.set(Calendar.DATE, 1);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                value_date = new Date(calendar.getTimeInMillis());
            }
            
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
        BatchParameters bp = new BatchParameters(new BigDecimal("676205327"));
        bp.setArgs(args);
        new BOA00().run(bp);
    }
}