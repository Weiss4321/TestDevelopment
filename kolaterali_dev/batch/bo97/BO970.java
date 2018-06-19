package hr.vestigo.modules.collateral.batch.bo97;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo97.BO971.DataIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
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
 * Izvješæe o dospijeæu hipoteka i dugoroènih sporazuma.
 * @author hrakis
 */
public class BO970 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo97/BO970.java,v 1.1 2015/02/27 13:51:44 hrakis Exp $";
    
    private BatchContext bc;
    private BO971 bo971;
    
    private Workbook[] workbook;
    private ExcelStyleData[] styles;
    private Sheet[] sheet;
    private int[] rowIndex;
    private int[] columnIndex;
    
    private final BigDecimal[] retail_cus_typ_id = new BigDecimal[] { new BigDecimal("1999"), new BigDecimal("1998") };
    private final BigDecimal[] corporate_cus_typ_id = new BigDecimal[] { new BigDecimal("2999"), new BigDecimal("2998"), new BigDecimal("999") };

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo971 = new BO971(bc);
        this.workbook = new Workbook[2];
        this.styles = new ExcelStyleData[2];
        this.sheet = new Sheet[2];
        this.rowIndex = new int[2];
        this.columnIndex = new int[2];

        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvati podatke za izvještaj
        DataIterator iter = bo971.selectData();

        try
        {
            // kreiraj workbookove za Retail i Corporate i zapiši zaglavlje u njih
            for (int idx = 0; idx < workbook.length; idx++)
            {
                workbook[idx] = new SXSSFWorkbook(100);
                styles[idx] = ExcelStyleData.createStyles(workbook[idx]);
                sheet[idx] = workbook[idx].createSheet((idx == 0 ? "Retail" : "Corporate"));
                ExcelUtils.setColumnWidths(sheet[idx], new int[] { 150, 80, 300, 120, 100, 80, 80, 80, 65, 50, 50 });
                writeHeader(idx);
            }

            // zapiši podatke u izvještaje
            while (iter.next())
            {
                int reportIndex = getReportIndex(iter);
                if (reportIndex == -1) continue;
                writeRow(reportIndex, iter);
            }
            info("Ukupan broj zapisa za Retail: " + rowIndex[0]);
            info("Ukupan broj zapisa za Corporate: " + rowIndex[1]);
            iter.close();

            // spremi izvještaje
            String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
            String[] fileName = new String[2];
            for (int idx = 0; idx < workbook.length; idx++)
            {
                fileName[idx] = bc.getOutDir() + "/" + "F60_" + (idx == 0 ? "RET" : "CO") + "_" + dateString + ".xlsx";
                FileOutputStream fileOut = new FileOutputStream(fileName[idx]);
                workbook[idx].write(fileOut);
                fileOut.close();
                new File(fileName[idx] + ".marker").createNewFile();
            }

            // pošalji izvještaje na mail
            Vector attachments = new Vector();
            for (int i = 0; i < fileName.length; i++) attachments.add(fileName[i]);
            YXY70.send(bc, "csvbo97", bc.getLogin(), attachments);
            info("Izvjestaj je kreiran i poslan na mail.");
        }
        catch (Exception ex)
        {
            error("Dogodila se nepredvidjena greska kod kreiranja izvjestaja!", ex);
            throw ex;
        }
        finally
        {
            for (int idx = 0; idx < workbook.length; idx++) dispose(workbook[idx]);
        }

        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u zadani izvještaj.
     * @param reportIndex redni broj izvještaja
     */
    private void writeHeader(int reportIndex) throws Exception
    {
        rowIndex[reportIndex] = 0;
        columnIndex[reportIndex] = 0;
        ExcelStyleData styles = this.styles[reportIndex];
        
        Row row = sheet[reportIndex].createRow(rowIndex[reportIndex]);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "IM vlasnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "Naziv vlasnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "Broj dugoro\u010Dnog sporazuma", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "Dospije\u0107e plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "Dospije\u0107e DS", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "Datum hipoteke do", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "Red hipoteke", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "DWH status", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, "Orig. status", styles.headingStyle);
        rowIndex[reportIndex]++;
        
        sheet[reportIndex].createFreezePane(0, rowIndex[reportIndex]);
    }
    
    
    /**
     * Metoda zapisuje podatke u zadani izvještaj.
     * @param reportIndex redni broj izvještaja
     * @param iter iterator s podacima
     */
    private void writeRow(int reportIndex, DataIterator iter) throws Exception
    {
        columnIndex[reportIndex] = 0;
        ExcelStyleData styles = this.styles[reportIndex];
        
        Row row = sheet[reportIndex].createRow(rowIndex[reportIndex]);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.col_num().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.register_no().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.name(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.cus_acc_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.agreement_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.due_date(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.date_until(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.hf_date_hfc_until(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.hf_priority(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.cus_acc_status(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[reportIndex]++, iter.cus_acc_orig_st(), styles.normalStyle);
        rowIndex[reportIndex]++;
    }
    
    
    /**
     * Metoda vraæa kojem izvještaju pripada zadani skup podataka.
     * @param iter iterator s podacima
     * @return 0 za Retail, 1 za Corporate, -1 inaèe
     */
    private int getReportIndex(DataIterator iter) throws Exception
    {
        for (BigDecimal cus_typ_id : retail_cus_typ_id)
        {
            if (cus_typ_id.equals(iter.cus_typ_id())) return 0;
        }
        
        for (BigDecimal cus_typ_id : corporate_cus_typ_id)
        {
            if (cus_typ_id.equals(iter.cus_typ_id())) return 1;
        }
        
        return -1;
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
            event.put("eve_typ_id", new BigDecimal("7705617704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Izvjesce o dospijecu hipoteka i dugorocnih sporazuma");
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
        BatchParameters bp = new BatchParameters(new BigDecimal("7705610704"));
        bp.setArgs(args);
        new BO970().run(bp);
    }
}