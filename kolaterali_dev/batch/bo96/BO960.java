//created 2015.02.09
package hr.vestigo.modules.collateral.batch.bo96;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;

import hr.vestigo.modules.collateral.batch.bo96.BO961.AllocationIterator;
import hr.vestigo.modules.collateral.batch.bo96.BO961.CollRecovery;
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
 *
 * @author Hraziv
 */
public class BO960 extends Batch{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo96/BO960.java,v 1.8 2018/01/23 12:13:45 hraskd Exp $";
    
    private BatchContext bc;
    private BO961 bo961;
   
    private Date value_date;
    
    private String fileName;
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheetColl;
    private int rowIndexColl = 0;
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        bc.debug("inicijalizacija potrebnih varijabli");
        this.bc = bc; 
        this.bo961 = new BO961(bc);

        // evidentiranje eventa
        bc.debug("evidentiranje eventa");
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        bc.debug("dohvat i provjera parametara predanih obradi");
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat podataka
        // zapisivanje podataka u izvještaj
        try
        {
            openReport();
            writeTitles();
            writeHeaders();
            writeRows();
            saveReport();
        }
        finally
        {
            dispose();
        }
        
        // slanje maila
        sendMail();

        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku i kreira pripadajuæu marker datoteku.  
     */
    private void saveReport() throws Exception
    {
        bc.startStopWatch("BO960.saveReport");
        
        generateFileName();
        bc.info("Izlazna datoteka: " + fileName);
        
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        dispose();
        
        new File(fileName + ".marker").createNewFile();
        
        bc.stopStopWatch("BO960.saveReport");
    }
    
    /**
     * Metoda generira ime izlazne datoteke 
     */
    private void generateFileName()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        
        String dir = bc.getOutDir() + "/";
        String mod = "RecoveryReport";
        
        String dateString = sdf.format(new Date(System.currentTimeMillis()));
        String fileNameCore = dir + mod + dateString + ".xlsx";
        
        this.fileName = fileNameCore;
    }
    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose()
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }
    
    /**
     * Metoda zapisuje zaglavlje u sheet.
     */
    private void writeHeaders()
    {
        writeHeader();
    }
    
    /**
     * Metoda zapisuje zaglavlje u sheet.
     */
    private void writeHeader()
    {
        Row row = sheetColl.createRow(1);
        row.setHeightInPoints(52);
        
        int columnIndex = 0;
        ExcelUtils.createCell(row, columnIndex++, "Customer id", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Customer name", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Coconut", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Collateral Object ID", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Collateral code (GCTC)", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Endorsement type (GCTC)", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Property type (GCTC)", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Object type (GCTC)", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Iznos NCV EUR", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum NCV", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Iznos zadnje procjene EUR", styles.headingWrapStyle);//Iznos NGV
        ExcelUtils.createCell(row, columnIndex++, "Datum procjene", styles.headingWrapStyle);//Datum NGV
        ExcelUtils.createCell(row, columnIndex++, "Iznos WCV HRK", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum WCV", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Iznos WCOV HRK", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum WCOV", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Preuzeta od banke", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Recovery amount", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Recovery currency", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Kind of recovery", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Recovery date", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Costs for Collateral Realization", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Realization Amount", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Realization currency", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Realization date", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Recovery comment", styles.headingWrapStyle);                
        
        sheetColl.createFreezePane(0, 2);
    }
    
    /**
     * Metoda zapisuje retke s podacima u sheet.
     * @param iter iterator s podacima
     */
    private void writeRows() throws Exception
    {
        rowIndexColl = 2;
                
        CollRecovery collRecovery = bo961.selectRecoveryCollaterals();        
        while (collRecovery.next())
        {   
            bc.debug("dohvat podataka za col_hea_id=" + collRecovery.colHeaId());
            BigDecimal col_pro_id = bo961.getLastColProcID(collRecovery.colHeaId());            
            AllocationIterator iter = bo961.selectData(collRecovery.colHeaId(), col_pro_id);
            bc.debug("uspjesan dohvat podataka");
            while(iter.next()){
                ListCollData data = fillDataFromIterator(iter);   
                writeRow(data);
            }
        }
        
        info("Zapisano " + rowIndexColl + " redova u sheet.");
    }
    
    /**
     * Metoda puni data objekt s podacima iz iteratora.
     * @param data data objekt
     * @param iter iterator s podacima
     */
    private ListCollData fillDataFromIterator(AllocationIterator iter) throws Exception
    {
        ListCollData data=new ListCollData();
        
        bc.debug("registerNo=" + iter.registerno());
        data.colHeaId = iter.colHeaId();
        data.collCatId = iter.collCatId();
        data.registerNo = iter.registerno();
        data.customerName = iter.customer_name();
        data.cocunat = iter.cocunat();
        data.collateralObjectId = iter.collateralObjectId();
        data.collataeralCode = iter.collataeralCode();
        data.endorsementType = iter.endorsementType();
        data.propertyType = iter.propertyType();
        data.objectType = iter.objectType();
        data.iznosNCV = iter.real_est_nomi_valu();
        data.datumNCV =iter.real_est_nomi_date();
        if(data.collCatId.equals(new BigDecimal("618223"))){
            data.iznosNGV = iter.new_build_val();
            data.datumNGV = iter.real_est_estn_date();            
        }        
        data.iznosWCV = iter.wca();
        data.datumWCV = iter.cover_date();
        data.iznosWCOV = iter.wcov();
        data.datumWCOV = iter.cover_date();
        data.recoveryAmount = iter.recoveryAmount();
        data.recoveryCurrency = iter.recoveryCurrency();
        data.kindOfRecovery = iter.kindOfRecovery();
        data.recoveryDate = iter.recoveryDate();
        data.costForCollateralRealization = iter.costForCollateralRealization();
        data.realizationAmount = iter.realizationAmount();
        data.realizationCurrency = iter.realizationCurrency();
        data.realizationDate = iter.realizationDate();
        data.recovery_comment = iter.recovery_comment();    
        data.takeoverFromBank = iter.takeoverFromBank(); 
        
        return data;
    }
      
    /**
     * Metoda zapisuje redak s podacima u sheet.
     * @param data objekt s podacima
     */
    private void writeRow(ListCollData data)
    {
        bc.startStopWatch("BO960.writeCollRow");
        
        Row row = sheetColl.createRow(rowIndexColl);
        int columnIndex = 0;
        
        ExcelUtils.createCell(row, columnIndex++, data.registerNo, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.customerName, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.cocunat, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.collateralObjectId, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.collataeralCode, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.endorsementType, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.propertyType, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.objectType, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.iznosNCV, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.datumNCV, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.iznosNGV, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.datumNGV, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.iznosWCV, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.datumWCV, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.iznosWCOV, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.datumWCOV, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.takeoverFromBank, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.recoveryAmount, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.recoveryCurrency, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.kindOfRecovery, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.recoveryDate, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.costForCollateralRealization, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.realizationAmount, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.realizationCurrency, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.realizationDate, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.recovery_comment, styles.normalStyle);
        
        rowIndexColl++;
        
        bc.stopStopWatch("BO960.writeCollRow");
    }
    

        
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO960.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("7699041704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Obrada za kreiranje CRM izvjesca");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO960.insertIntoEvent");
            return eve_id;
        }
        catch(Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade!", ex);
            throw ex;
        }
    }
    
    /**
     * Metoda otvara izvještaj i kreira sheetove u koje æe biti zapisani podaci. 
     */
    private void openReport()
    {
        // kreiraj workbook
        bc.debug("openReport -> start");
        this.workbook = new SXSSFWorkbook(100);
        this.styles = ExcelStyleData.createStyles(workbook);
        
        // kreiraj sheet
        this.sheetColl = workbook.createSheet("RecoveryReport");
        ExcelUtils.setColumnWidths(sheetColl, new int[] { 85, 60, 135, 85, 90, 80, 80, 110, 90, 110, 90, 110, 90, 110, 90, 110, 65, 150, 90, 110, 110, 75, 90, 250});
        bc.debug("openReport -> end");
    }
    
    /**
     * Metoda zapisuje naziv izvještaja u sheet.
     */
    private void writeTitles()
    {
        writeTitle(sheetColl);
    }
    
    /**
     * Metoda zapisuje naziv izvještaja u zadani sheet.
     * @param sheet sheet u koji se upisuje naziv izvještaja.
     */
    private void writeTitle(Sheet sheet)
    {
        Row row = sheet.createRow(0);
        String title = "Recovery data"; 
        ExcelUtils.createCell(row, 0, title, styles.titleStyle);
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
        BatchParameters bp = new BatchParameters(new BigDecimal("7698996704"));
        bp.setArgs(args);
        new BO960().run(bp);
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
           bc.setBankSign(bank_sign);
           
           return true;
       }
       catch (Exception ex)
       {
           error("Neispravno zadani parametri!", ex);
           return false;
       }
   }
   
   /**
    * Metoda šalje mail s generiranim izvješæem naruèitelju obrade.
    */
   private void sendMail() throws Exception
   {
       bc.startStopWatch("BO960.sendMail");
       //String mail = bo871.selectMailAddresses();
       //bc.debug("LOGIN : " + bc.getLogin());
       if (bc.getLogin().equals("000010"))
       {
           bc.debug("LOGIN : " + bc.getLogin());
           YXY70.send(bc, "csvbo96", bc.getLogin(), fileName);
       }
       else
       {
           bc.debug("LOGIN_else : " + bc.getLogin());
           YXY70.send(bc, "csvbo96c", bc.getLogin(), fileName);
       }
       
       bc.stopStopWatch("BO960.sendMail");
   }
}

