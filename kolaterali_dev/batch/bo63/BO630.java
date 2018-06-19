package hr.vestigo.modules.collateral.batch.bo63;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo63.ExcelStyleData;
import hr.vestigo.modules.collateral.batch.bo63.BO631.Iter1;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
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
 * Kreiranje izvješæa s listom kandidata za slanje obavijesti / ugovaranje grupne police
 * @author hrakis
 */
public class BO630 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo63/BO630.java,v 1.4 2017/02/16 11:55:47 hrakis Exp $";
    
    private BatchContext bc;
    private BO631 bo631;
    
    private String policy_type;
    private String cust_type;
    
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet[] sheet;
    private int[] rowIndex;
    private int[] columnIndex;
    private String fileName;
    
    private final String policy_type_coll = "K";
    private final String policy_type_inspol = "P";
    private final String rpt_code = "csv245";
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        this.bo631 = new BO631(bc);
        this.bc = bc;
        
        // evidentiranje eventa
        bo631.insertIntoEvent();

        // dohvat i provjera parametara obrade
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        try
        {
            // otvaranje izvještaja
            openReport();
            
            // dohvat podataka
            Iter1 iter = bo631.selectData(policy_type, cust_type);
            
            // zapisivanje podataka u izvještaj
            while (iter.next())
            {
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
        sheet = new Sheet[2];
        sheet[0] = workbook.createSheet("Kandidati za obavijest");
        sheet[1] = workbook.createSheet("Kandidati za grupnu policu");
        ExcelUtils.setColumnWidths(sheet[0], new int[] { 80, 40, 80, 160, 120, 120, 130, 80, 150, 90, 70, 80, 80 });
        ExcelUtils.setColumnWidths(sheet[1], new int[] { 80, 40, 80, 160, 120, 120, 130, 80, 150, 90, 100, 100, 95, 130, 150, 80, 80, 160, 110, 130, 130, 150, 80, 70, 80, 80, 80 });
        rowIndex = new int[2];
        columnIndex = new int[2];
        writeHeaderRow(0);
        writeHeaderRow(1);
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u izvještaj.
     * @param sheetIdx broj sheeta
     */
    private void writeHeaderRow(int sheetIdx)
    {
        rowIndex[sheetIdx] = 0;
        columnIndex[sheetIdx] = 0;
        
        Row row = sheet[sheetIdx].createRow(rowIndex[sheetIdx]);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Status kandidata", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "OJ", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "ID korisnika", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Korisnik", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Partija", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "\u0160ifra police", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Premija pla\u0107ena do", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Osiguravatelj", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Va\u017Ee\u0107i datum dospije\u0107a kredita", styles.headingStyle);

        if (sheetIdx == 1)
        {
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Vrsta nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Nova gra\u0111evinska vrijednost", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Po\u0161tanski broj mjesta osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Mjesto osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Ulica osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Ku\u0107ni broj osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "ID vlasnika osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Vlasnik osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "OIB vlasnika osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Po\u0161tanski broj mjesta prebivali\u0161ta vlasnika osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Mjesto prebivali\u0161ta vlasnika osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Ulica vlasnika osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Ku\u0107ni broj vlasnika osigurane nekretnine", styles.headingStyle);
        }
        
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Status plasmana u modulu", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Status vlasnika plasmana", styles.headingStyle);
        
        if (sheetIdx == 1)
        {
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Status vlasnika osigurane nekretnine", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Datum kori\u0161tenja plasmana", styles.headingStyle);            
        }
        
        rowIndex[sheetIdx]++;
        sheet[sheetIdx].createFreezePane(0, rowIndex[sheetIdx]);
    }
    
    
    /**
     * Metoda zapisuje podatke o kandidatu u izvještaj.
     * @param iter iterator s podacima
     */
    private void writeDetailsRow(Iter1 iter) throws Exception
    {
        int sheetIdx;
        if ("1".equals(iter.wrn_status())) sheetIdx = 0;
        else if ("5".equals(iter.wrn_status())) sheetIdx = 1;
        else throw new Exception("Nepoznat status obavijesti " + iter.wrn_status() + "!");
                
        columnIndex[sheetIdx] = 0;
        
        Row row = sheet[sheetIdx].createRow(rowIndex[sheetIdx]);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.candidate_status(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.org_uni_code(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.register_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.name(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.cus_acc_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.col_num().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.ip_code().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.ip_paid_until(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.ic_name(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.due_date(), styles.normalDateStyle);

        HashMap map = null;
        if (sheetIdx == 1)
        {
            map = bo631.selectCollOwnerData(iter.col_hea_id());
            
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.real_es_type_desc(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.new_build_val(), styles.normalNumericStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.postal_code(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.city(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.street(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.housenr(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("register_no"), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("name"), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("tax_number"), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("postoffice"), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("city_name"), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("street"), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("house_no"), styles.normalStyle);
        }

        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.cus_acc_orig_st(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.customer_status(), styles.normalStyle);

        if (sheetIdx == 1)
        {
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("status"), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.usage_date(), styles.normalDateStyle);            
        }

        rowIndex[sheetIdx]++;
    }
    
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku i kreira pripadajuæu marker datoteku.  
     */
    private void saveReport() throws Exception
    {
        String dir = bc.getOutDir() + "/";
        String policy_type_suffix = policy_type.equals(policy_type_inspol) ? "Imovine" : "Zivota";
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        fileName = dir + "ListaKandidataPoliceOsiguranja" + policy_type_suffix + dateString + ".xlsx";
        
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
        bc.startStopWatch("BO630.sendMail");
        
        Vector attachments = new Vector();
        attachments.add(fileName);
        YXY70.send(bc, rpt_code, bc.getLogin(), attachments);
        
        bc.stopStopWatch("BO630.sendMail");
    }
    
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>policy_type</dt><dd>Vrsta police. P za police osiguranje imovine, K za police osiguranja života.</dd>
     *    <dt>cust_type</dt><dd>Vrsta komitenta. P za pravne osobe, F za fizièke osobe.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Vrsta police", "Vrsta komitenta" };

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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4668827704"));
        batchParameters.setArgs(args);
        new BO630().run(batchParameters);
    }
}