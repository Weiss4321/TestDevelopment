package hr.vestigo.modules.collateral.batch.boA1;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.boA1.BOA11.Iter1;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Kreiranje izvješæa s listom kandidata za pozivanje
 * @author hrakis
 */
public class BOA10 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/boA1/BOA10.java,v 1.1 2017/02/27 12:24:26 hrakis Exp $";
    
    private BatchContext bc;
    private BOA11 boA11;
    
    private String policy_type;
    private String cust_type;
    private Date max_date;
    
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheet;
    private int rowIndex;
    private int columnIndex;
    private String fileName;
    
    private final String policy_type_coll = "K";
    private final String policy_type_inspol = "P";
    private final String rpt_code = "csvboA1";
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        this.boA11 = new BOA11(bc);
        this.bc = bc;
        
        // evidentiranje eventa
        boA11.insertIntoEvent();

        // dohvat i provjera parametara obrade
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        try
        {
            // otvaranje izvještaja
            openReport();
            
            // dohvat podataka
            Iter1 iter = boA11.selectData(policy_type, cust_type, max_date);
            
            // zapisivanje podataka u izvještaj
            while (iter.next())
            {
                String owner = boA11.selectRetailPlacementOwner(iter.col_hea_id());
                if (owner == null) continue;
                
                writeDetailsRow(iter);
            }
            iter.close();
            
            // spremanje izvještaja
            saveReport();
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
        sheet = workbook.createSheet("Lista kandidata za pozivanje");
        ExcelUtils.setColumnWidths(sheet, new int[] { 40, 80, 200, 120, 120, 130, 80, 170, 90, 70, 80, 125, 125 });
        writeHeaderRow();
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u izvještaj.
     */
    private void writeHeaderRow()
    {
        rowIndex = 0;
        columnIndex = 0;
        
        Row row = sheet.createRow(rowIndex);
        ExcelUtils.createCell(row, columnIndex++, "OJ", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "ID korisnika", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Korisnik", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra police", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Premija pla\u0107ena do", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Osiguravatelj", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Va\u017Ee\u0107i datum dospije\u0107a kredita", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status plasmana u modulu", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status vlasnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Kontakt broj fiksnog telefona vlasnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Kontakt broj mobilnog telefona vlasnika plasmana", styles.headingStyle);
        
        rowIndex++;
        sheet.createFreezePane(0, rowIndex);
    }
    
    
    /**
     * Metoda zapisuje podatke o kandidatu u izvještaj.
     * @param iter iterator s podacima
     */
    private void writeDetailsRow(Iter1 iter) throws Exception
    {
        columnIndex = 0;
        
        HashMap map = boA11.selectCustomerData(iter.cus_id());
        
        Row row = sheet.createRow(rowIndex);
        ExcelUtils.createCell(row, columnIndex++, iter.org_uni_code(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.register_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.name(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.cus_acc_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.col_num().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.ip_code().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.ip_paid_until(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.ic_name(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.due_date(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.cus_acc_orig_st(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.customer_status(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, (String)map.get("phone"), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, (String)map.get("mobile"), styles.normalStyle);

        rowIndex++;
    }
    
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku i kreira pripadajuæu marker datoteku.  
     */
    private void saveReport() throws Exception
    {
        String dir = bc.getOutDir() + "/";
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        fileName = dir + "ListaKandidataZaPozivanje" + dateString + ".xlsx";
        
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        new File(fileName + ".marker").createNewFile();
        
        info("Broj redova zapisanih u izvjestaj: " + rowIndex);
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
        bc.startStopWatch("BOA10.sendMail");
        
        Vector attachments = new Vector();
        attachments.add(fileName);
        YXY70.send(bc, rpt_code, bc.getLogin(), attachments);
        
        bc.stopStopWatch("BOA10.sendMail");
    }
    
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>policy_type</dt><dd>Vrsta police. P za police osiguranje imovine, K za police osiguranja života.</dd>
     *    <dt>cust_type</dt><dd>Vrsta komitenta. P za pravne osobe, F za fizièke osobe.</dd>
     *    <dt>max_date</dt><dd>Maksimalni datum Premija plaæena do</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Vrsta police", "Vrsta komitenta", "Datum" };

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
            
            // vrsta police
            policy_type = bc.getArg(1).trim().toUpperCase();
            if (!policy_type.equals(policy_type_inspol))
            {
                error("Vrsta police moze biti samo " + policy_type_inspol + "!", null);
                return false;
            }
            
            // vrsta komitenta
            cust_type = bc.getArg(2).trim().toUpperCase();
            if (!cust_type.equals("F"))
            {
                error("Vrsta komitenta moze biti samo F!", null);
                return false;
            }
            
            // datum
            max_date = DateUtils.parseDate(bc.getArg(3).trim());
            
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("1215309327"));
        batchParameters.setArgs(args);
        new BOA10().run(batchParameters);
    }
}