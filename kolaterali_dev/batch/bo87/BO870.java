//created 2014.04.01
package hr.vestigo.modules.collateral.batch.bo87;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;



import hr.vestigo.modules.collateral.batch.bo87.BO871.AllocationIterator;

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
 * @author hraziv
 */
public class BO870 extends Batch
{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo87/BO870.java,v 1.2 2014/05/27 08:58:00 hraziv Exp $";
    
    private BatchContext bc;
    private BO871 bo871;

   
    private Date value_date;

    
    private String fileName;
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheetPonder;
    private int rowIndexPonder = 0;
    


    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo871 = new BO871(bc);

        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat podataka
        AllocationIterator iter = bo871.selectData();
        
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
     * Metoda otvara izvještaj i kreira sheetove u koje æe biti zapisani podaci. 
     */
    private void openReport()
    {
        // kreiraj workbook
        this.workbook = new SXSSFWorkbook(100);
        this.styles = ExcelStyleData.createStyles(workbook);
        
        // kreiraj sheet
        this.sheetPonder = workbook.createSheet("Ponderi");
        ExcelUtils.setColumnWidths(sheetPonder, new int[] { 100, 140, 131, 400, 110, 230, 50, 85, 160, 183, 160, 125, 100, 100, 100, 179, 126 });
        
    }

    
    /**
     * Metoda zapisuje naziv izvještaja u Ponder sheet.
     */
    private void writeTitles()
    {
        writeTitle(sheetPonder);
    }
    
    /**
     * Metoda zapisuje naziv izvještaja u zadani sheet.
     * @param sheet sheet u koji se upisuje naziv izvještaja.
     */
    private void writeTitle(Sheet sheet)
    {
        Row row = sheet.createRow(0);
        ExcelUtils.createCell(row, 0, value_date, styles.titleStyle);
        
        row = sheet.createRow(1);
        String title = "Izvje\u0161\u0107e une\u0161enih defaultnih pondera po kategorijama, tipovima i vrstama"; 
        ExcelUtils.createCell(row, 0, title, styles.titleStyle);
        
    }

    
    /**
     * Metoda zapisuje zaglavlje u ponder sheet.
     */
    private void writeHeaders()
    {
        writePonderHeader();
    }
    
    /**
     * Metoda zapisuje zaglavlje u ponder sheet.
     */
    private void writePonderHeader()
    {
        Row row = sheetPonder.createRow(2);
        row.setHeightInPoints(52);
        
        int columnIndex = 0;
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kategorije", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv kategorije", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra tipa kolaterala", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv tipa kolaterala", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra vrste", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv vrste", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Uvjet", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tip pondera", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Min ponder izra\u017Een u %", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Defaultni ponder izra\u017Een u %", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Max ponder izra\u017Een u %", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status zapisa:A-aktivan, trenutno va\u017Ee\u0107i,N-neaktivan", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Od kada vrijedi ponder", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Do kada vrijedi ponder", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "OJ unosa sloga", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Referent koji je unio zapis", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrijeme unosa", styles.headingWrapStyle);
        
        sheetPonder.createFreezePane(0, 3);
    }
    
    
    
    /**
     * Metoda zapisuje retke s podacima u ponder sheet.
     * @param iter iterator s podacima
     */
    private void writeRows(AllocationIterator iter) throws Exception
    {
        rowIndexPonder = 3;
        
        BigDecimal old_cus_acc_id = null;
        DefPonderData data = new DefPonderData();
        
        while (iter.next())
        {   
            fillDataFromIterator(data, iter);
            writePonderRow(data);
        }
        
        info("Zapisano " + rowIndexPonder + " redova u ponder sheet.");
    }
    

    /**
     * Metoda zapisuje redak s podacima u ponder sheet.
     * @param data objekt s podacima
     */
    private void writePonderRow(DefPonderData data)
    {
        bc.startStopWatch("BO870.writePonderRow");
        
        Row row = sheetPonder.createRow(rowIndexPonder);
        int columnIndex = 0;
        
        ExcelUtils.createCell(row, columnIndex++, data.category_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.category_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.type_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.type_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.sub_type_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.sub_type_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.uvjet, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.ponder_type, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.min_value, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.dfl_value, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.max_value, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.status, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.date_from, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.date_until, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.oj_unosa, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.referent_unosa, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.vrijeme_unosa, styles.normalTimestampStyle);
        
        rowIndexPonder++;
        
        bc.stopStopWatch("BO870.writePonderRow");
    }

    
    
    
    /**
     * Metoda puni data objekt s podacima iz iteratora.
     * @param data data objekt
     * @param iter iterator s podacima
     */
    private void fillDataFromIterator(DefPonderData data, AllocationIterator iter) throws Exception
    {
        bc.debug("category code" + iter.category_code());
        data.category_code = iter.category_code();
        data.category_name = iter.category_name();
        data.date_from = iter.date_from();
        data.date_until = iter.date_until();
        data.dfl_value = iter.dfl_value();
        data.max_value = iter.max_value();
        data.min_value = iter.min_value();
        data.oj_unosa = iter.oj_unosa();
        data.ponder_type = iter.ponder_type();
        data.referent_unosa = iter.referent_unosa();
        data.status = iter.status();
        data.sub_type_code = iter.sub_type_code();
        data.sub_type_name = iter.sub_type_name();
        data.type_code = iter.type_code();
        data.type_name = iter.type_name();
        data.uvjet = iter.uvjet();
        data.vrijeme_unosa = iter.vrijeme_unosa();
    }

    
    /**
     * Metoda generira ime izlazne datoteke 
     */
    private void generateFileName()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        
        String dir = bc.getOutDir() + "/";
        String mod = "DefaultPonderData";
        
        String dateString = sdf.format(new Date(System.currentTimeMillis()));
        String fileNameCore = dir + mod + "_" + dateString + ".xlsx";
        
        this.fileName = fileNameCore;
    }
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku i kreira pripadajuæu marker datoteku.  
     */
    private void saveReport() throws Exception
    {
        bc.startStopWatch("BO870.saveReport");
        
        generateFileName();
        bc.info("Izlazna datoteka: " + fileName);
        
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        dispose();
        
        new File(fileName + ".marker").createNewFile();
        
        bc.stopStopWatch("BO870.saveReport");
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
        bc.startStopWatch("BO870.sendMail");
        //String mail = bo871.selectMailAddresses();
        YXY70.send(bc, "csvb087", bc.getLogin(), fileName);
        bc.stopStopWatch("BO870.sendMail");
    }


    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO870.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6529807704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Obrada za kreiranje CRM izvjesca");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO870.insertIntoEvent");
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
        BatchParameters bp = new BatchParameters(new BigDecimal("6529804704"));
        bp.setArgs(args);
        new BO870().run(bp);
    }
}

