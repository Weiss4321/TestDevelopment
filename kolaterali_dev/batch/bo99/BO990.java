package hr.vestigo.modules.collateral.batch.bo99;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo99.BO991.CustomerCollateralIterator;
import hr.vestigo.modules.collateral.batch.bo99.BO991.RecoveryCollateralIterator;
import hr.vestigo.modules.collateral.common.yoyM.YOYM0;
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
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Kolaterali komitenata u Defaultu.
 * @author hrakis
 */
public class BO990 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo99/BO990.java,v 1.5 2017/06/01 10:52:53 hrakis Exp $";
    
    private BatchContext bc;
    private BO991 bo991;
    
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet[] sheet;
    private int[] rowIndex;
    private int[] columnIndex;
    private String fileName;
    
    private final String rpt_code = "csvbo99";
    private final BigDecimal real_estate_col_cat_id = new BigDecimal("618223");

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo991 = new BO991(bc);
        HashMap<BigDecimal, CustomerData> customers = new HashMap<BigDecimal, CustomerData>();

        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // inicijalizacija potrebnih commona
        YRXX0 exchange = new YRXX0(bc);
        YOYM0 rre_cre_mapper = new YOYM0(bc, "rre_cre_indic", new Date(bc.getExecStartTime().getTime()));
        
        // dohvati sve kolaterale komitenata u defaultu
        CustomerCollateralIterator iter1 = bo991.selectCustomerCollaterals();
        while (iter1.next())
        {
            CustomerData customer = customers.get(iter1.cus_id());
            if (customer == null)
            {
                customer = new CustomerData(iter1.cus_id());
                customers.put(customer.cus_id, customer);
            }
            
            BigDecimal col_hea_id = iter1.col_hea_id() != null ? iter1.col_hea_id() : iter1.hf_col_hea_id();
            CollateralData collateral = new CollateralData(col_hea_id);
            collateral.default_date = iter1.default_date();
            customer.collaterals.put(collateral.col_hea_id, collateral);
        }
        iter1.close();
        
        // dohvati kolaterale koji imaju popunjene recovery ili realization podatke
        RecoveryCollateralIterator iter2 = bo991.selectRecoveryCollaterals();
        while (iter2.next())
        {
            CustomerData customer = customers.get(iter2.cus_id());
            if (customer == null)
            {
                customer = new CustomerData(iter2.cus_id());
                customers.put(customer.cus_id, customer);
            }
            
            CollateralData collateral = customer.collaterals.get(iter2.col_hea_id());
            if (collateral == null)
            {
                collateral = new CollateralData(iter2.col_hea_id());
                bo991.selectDefaultDateForRecoveryCollateral(customer, collateral);
                customer.collaterals.put(collateral.col_hea_id, collateral);
            }
        }
        iter2.close();
        
        // dohvati podatke o komitentima
        for (CustomerData customer : customers.values())
        {
            bo991.selectCustomerData(customer);
            
            // dohvati podatke o svim kolateralima komitenta
            for (CollateralData collateral : customer.collaterals.values())
            {
                bo991.selectCollateralData(collateral, customer, exchange, rre_cre_mapper);
            }
        }

        try
        {
            // otvaranje izvještaja
            openReport();

            // zapisivanje podataka u sheetove
            for (CustomerData customer : customers.values())
            {
                for (CollateralData collateral : customer.collaterals.values())
                {
                    if (collateral.isIncludedInReport())
                    {
                        writeDetailsRow(customer, collateral);
                    }
                }
            }

            // spremanje izvještaja
            saveReport();
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
        
        // slanje izvještaja na mail
        sendMail();

        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda otvara izvještaj i kreira sheetove u koje æe biti zapisani podaci. 
     */
    private void openReport()
    {
        workbook = new SXSSFWorkbook(100);
        styles = ExcelStyleData.createStyles(workbook);
        sheet = new Sheet[2];
        sheet[0] = workbook.createSheet("Nekretnine");
        sheet[1] = workbook.createSheet("Ostalo");
        int[] columnWidths = new int[] { 80, 80, 85, 80, 180, 90, 75, 75, 150, 200, 90, 200, 150, 200, 75, 35, 100, 100, 100, 100, 100, 105, 105, 65, 75, 65, 100, 75, 100, 75, 115, 100, 100, 65, 100, 75, 145, 90, 100, 100, 75, 100, 75, 100, 100, 75, 100, 75, 130 };
        ExcelUtils.setColumnWidths(sheet[0], columnWidths);
        ExcelUtils.setColumnWidths(sheet[1], columnWidths);
        rowIndex = new int[2];
        columnIndex = new int[2];
        writeHeaderRow(0);
        writeHeaderRow(1);
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u zadani sheet.
     * @param sheetIdx broj sheeta
     */
    private void writeHeaderRow(int sheetIdx)
    {
        rowIndex[sheetIdx] = 0;
        columnIndex[sheetIdx] = 0;
        
        Row row = sheet[sheetIdx].createRow(rowIndex[sheetIdx]);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Segment Indication", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Asset class", styles.headingStyle);        
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Customer ID", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Cocunut", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Customer Name (borrower)", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Country of residence of borrower", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Default start date", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "WCV date", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Collateral ID", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Collateral type", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Country of Collateral", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "GCT", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Property type", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Object type", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Real Estate Indicator", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "CUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "NCV/NGV at default start date", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "NCV/NGV at default start date EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "WCV/WGV at default start date", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "WCV/WGV at default start date EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "WCOV at default start date", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "actually applied discount at default start date", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "min. discount according to your local set of discounts", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Kolateral na prodaju", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Datum stavljanja kolaterala na prodaju", styles.headingStyle);        
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Part of the complex", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Realization Amount", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Realization Amount Currency", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Realization Amount EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Realization Date", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Kind of Realization", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Costs for Collateral Realization", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Recovery Amount", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Recovery Amount Currency", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Recovery Amount EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Recovery Date", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Kind of Recovery", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Recovery comment", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "First NCV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "First NCV EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Date of First NCV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Last NCV (EUR)", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Date of Last NCV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "First WCV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "First WCV EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Date of First WCV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Last WCV (EUR)", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Date of Last WCV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Lista na kojoj se nalazi kolateral", styles.headingStyle);
        
        rowIndex[sheetIdx]++;
        sheet[sheetIdx].createFreezePane(0, rowIndex[sheetIdx]);
    }
    
    
    /**
     * Metoda zapisuje podatke o kolateralu u izvještaj.
     * @param customer objekt s podacima o komitentu
     * @param collateral objekt s podacima o kolateralu
     */
    private void writeDetailsRow(CustomerData customer, CollateralData collateral) throws Exception
    {
        int sheetIdx = real_estate_col_cat_id.equals(collateral.col_cat_id) ? 0 : 1;
       
        columnIndex[sheetIdx] = 0;
        
        Row row = sheet[sheetIdx].createRow(rowIndex[sheetIdx]);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, customer.getSegmentIndication(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, customer.b2_asset_class, styles.normalStyle);        
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, customer.register_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, customer.cocunat, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, customer.name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, customer.country_of_residence, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.default_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.wcv_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.col_num, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.col_typ_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.country_of_collateral, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.gctc, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.property_type, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.object_type, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.real_estate_indicator, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.cur_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.ncv_default, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.ncv_default_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.wcv_default, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.wcv_default_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.wcov_default, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.actually_applied_discount, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.minimum_discount, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.for_sale, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.for_sale_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.part_of_complex, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.realization_amount, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.realization_cur_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.realization_amount_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.realization_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.kind_of_realization, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.realization_cost, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.recovery_amount, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.recovery_cur_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.recovery_amount_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.recovery_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.kind_of_recovery, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.recovery_comment, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.first_ncv, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.first_ncv_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.first_ncv_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.last_ncv_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.last_ncv_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.first_wcv, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.first_wcv_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.first_wcv_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.last_wcv_eur, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.last_wcv_date, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, collateral.getCollateralListName(), styles.normalStyle);
        
        rowIndex[sheetIdx]++;
    }
    
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku i kreira pripadajuæu marker datoteku.  
     */
    private void saveReport() throws Exception
    {
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        fileName = bc.getOutDir() + "/" + "DefaultReport" + dateString + ".xlsx";
        
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        new File(fileName + ".marker").createNewFile();
        
        info("Broj redova zapisanih u 1.sheet: " + rowIndex[0]);
        info("Broj redova zapisanih u 2.sheet: " + rowIndex[1]);
    }
    
    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose(Workbook workbook)
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }
    
    
    /**
     * Metoda koja šalje mail s priloženim izvještajem.
     */
    private void sendMail() throws Exception
    {
        bc.startStopWatch("BO990.sendMail");
        
        YXY70.send(bc, rpt_code, bc.getLogin(), fileName);
        bc.debug("Izvjestaj je poslan na mail korisniku.");
        
        bc.stopStopWatch("BO990.sendMail");
    }


    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO990.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("74613327"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Kolaterali komitenata u Defaultu");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO990.insertIntoEvent");
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
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke" };

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
            bc.setBankSign("RB");
            
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
        BatchParameters bp = new BatchParameters(new BigDecimal("74610327"));
        bp.setArgs(args);
        new BO990().run(bp);
    }
}