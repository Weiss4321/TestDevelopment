package hr.vestigo.modules.collateral.batch.bo64;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo64.ExcelStyleData;
import hr.vestigo.modules.collateral.batch.bo64.BO641.Iter1;
import hr.vestigo.modules.collateral.common.yoyF.*;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Priprema datoteke za slanje obavijesti / ugovaranje grupne police
 * @author hrakis
 */
public class BO640 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo64/BO640.java,v 1.6 2017/02/20 12:17:37 hrakis Exp $";
    
    private BatchContext bc;
    private BO641 bo641;
    private BigDecimal eve_id;
    
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
    private final String rpt_code = "csv246";
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        this.bo641 = new BO641(bc);
        this.bc = bc;
        
        // evidentiranje eventa
        this.eve_id = bo641.insertIntoEvent();

        // dohvat i provjera parametara obrade
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // provjera da li su sve kandidati oznaèeni za slanje/odbacivanje
        if (!bo641.isAllCandidatesHandled(policy_type))
        {
            bc.error("Obrada se ne moze izvrsiti jer u evidenciji postoje kandidati koji nisu oznaceni da se salju ili ne salju!", new String[]{});
            sendMail("Obrada se ne moze izvr\u0161iti jer u evidenciji postoje kandidati koji nisu ozna\u010Deni da se \u0161alju ili ne \u0161alju!");
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        bc.beginTransaction();

        try
        {
            // otvaranje izvještaja
            openReport();
            
            // dohvat oznaèenih kandidata
            Iter1 iter = bo641.selectData(policy_type);
            
            // obraðivanje oznaèenih kandidata
            BigDecimal policy_id = null;
            while (iter.next())
            {
                bc.info("Procesiram kandidata - polica " + iter.ip_code().trim() + " - kolateral " + iter.col_num().trim());
                
                // ažuriranje evidencije obavijesti i formiranje sloga u izlaznoj datoteci
                updateWarningNote(iter);
                
                // ažuriranje statusa police osiguranja
                if (!iter.policy_id().equals(policy_id)) updatePolicyStatus(iter);
                
                policy_id = iter.policy_id();
            }
            iter.close();
            
            // spremanje izvještaja
            saveReport();
        }
        finally
        {
            dispose(workbook);
        }

        bc.commitTransaction();
        
        // slanje izvještaja na mail
        sendMail();
        
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }



    /**
     * Metoda koja ažurira evidenciju obavijesti, te zapisuje obavijest u izlaznu datoteku ako je kandidat oznaèen za slanje.
     * @param iter Iterator s podacima
     */
    private void updateWarningNote(Iter1 iter) throws Exception
    {
        boolean send = iter.candidate_status().equalsIgnoreCase("A");  // da li je kandidat oznaèen za slanje
        String candidate_status_desc;
        String new_candidate_status;  // novi status kandidata
        String new_candidate_status_desc;
        String new_war_not_sent_stat; // novi status slanja
        
        // odredi nove statuse za zapis u evidenciji obavijesti
        if (send)
        {
            candidate_status_desc = "SALJI";
            new_candidate_status = "O";  // O - poslano
            new_candidate_status_desc = "POSLANO";
            new_war_not_sent_stat = "2"; // 2 - sent
        }
        else
        {
            candidate_status_desc = "NE SALJI";
            new_candidate_status = "X";  // X - discarded
            new_candidate_status_desc = "ODBACENO";
            new_war_not_sent_stat = "X"; // X - discarded
        }
        
        bc.info("...status kandidata je " + iter.candidate_status() + "-" + candidate_status_desc);

        // ažuriraj evidenciju obavijesti 
        bc.info("...postavljam status kandidata na " + new_candidate_status + "-" + new_candidate_status_desc);
        bo641.updateWarningNote(iter.ins_war_not_id(), new_candidate_status, new_war_not_sent_stat);
        
        // ako je kandidat oznaèen za slanje, zapiši obavijest u izlaznu datoteku
        if (send) writeDetailsRow(iter);
    }
    
    /**
     * Metoda koja ažurira status slanja obavijesti za policu osiguranja.
     * @param iter Iterator s podacima
     */
    private void updatePolicyStatus(Iter1 iter) throws Exception
    {
        boolean send = iter.candidate_status().equalsIgnoreCase("A");  // da li je kandidat oznaèen za slanje
        if (!send) return;  // ako kandidat nije oznaèen za slanje, ne mijenja se status police osiguranja
        
        // iniciraj common za evidentiranje povijesti promjena
        YOYFData data = new YOYFData();
        if(policy_type.equals(policy_type_coll)) data.col_ins_id = iter.policy_id(); else data.ip_id = iter.policy_id();
        data.use_id = new BigDecimal(1);
        data.org_uni_id = new BigDecimal(53253);
        data.eve_id = eve_id;
        YOYF0 yoyF0 = new YOYF0(bc, data);
        yoyF0.selectOldState();  // dohvati staro stanje

        // ažuriraj status slanja obavijesti za policu osiguranja
        bc.info("...postavljam status slanja obavijesti na " + iter.wrn_status());
        bo641.updatePolicyStatus(policy_type, iter.policy_id(), iter.wrn_status());

        // dohvati novo stanje i zapiši promjene u povijest promjena
        yoyF0.selectNewState();
        yoyF0.insertIntoIpChgHistory(); 
    }
    
    
    /**
     * Metoda otvara izvještaj i kreira sheetove u koje æe biti zapisani podaci. 
     */
    private void openReport()
    {
        workbook = new SXSSFWorkbook(100);
        styles = ExcelStyleData.createStyles(workbook);
        sheet = new Sheet[2];
        sheet[0] = workbook.createSheet("Slanje obavijesti");
        sheet[1] = workbook.createSheet("Ugovaranje grupne police");
        ExcelUtils.setColumnWidths(sheet[0], new int[] { 50, 80, 200, 200, 60, 130, 120, 80, 120, 120, 80, 80 });
        ExcelUtils.setColumnWidths(sheet[1], new int[] { 50, 80, 200, 200, 60, 130, 120, 80, 120, 120, 80, 80 });
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
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "OJ", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Interni ID", styles.headingStyle);        
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Ime i prezime", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Adresa", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Po\u0161ta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Mjesto", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Datum dospije\u0107a plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Broj police", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Datum do", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Pla\u0107eno do", styles.headingStyle);
        
        rowIndex[sheetIdx]++;
        sheet[sheetIdx].createFreezePane(0, rowIndex[sheetIdx]);
    }
    
    /**
     * Metoda zapisuje podatke o polici osiguranja u izvještaj.
     * @param sheetIdx broj sheeta
     * @param iter iterator s podacima
     */
    private void writeDetailsRow(Iter1 iter) throws Exception
    {
        HashMap map = bo641.selectCustomerData(iter.cus_id());
        
        int sheetIdx;
        if ("1".equals(iter.wrn_status())) sheetIdx = 0;
        else if ("5".equals(iter.wrn_status())) sheetIdx = 1;
        else throw new Exception("Nepoznat status obavijesti " + iter.wrn_status() + "!");
        
        columnIndex[sheetIdx] = 0;
        
        Row row = sheet[sheetIdx].createRow(rowIndex[sheetIdx]);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.org_uni_code(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.register_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.name(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, map.get("street") + " " + map.get("house_no"), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("postoffice"), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)map.get("city_name"), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.cus_acc_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.due_date(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.col_num().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.ip_code().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.ip_valid_until(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.ip_paid_until(), styles.normalDateStyle);        
        
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
        fileName = dir + "ObavijestiUgovaranjeGPZaPoliceOsiguranja" + policy_type_suffix + dateString + ".xlsx";
        
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
        bc.startStopWatch("BO640.sendMail");
        
        Vector attachments = new Vector();
        attachments.add(fileName);
        YXY70.send(bc, rpt_code, bc.getLogin(), attachments);
        
        bc.stopStopWatch("BO640.sendMail");
    }
    
    /**
     * Metoda koja šalje mail sa zadanom porukom.
     */
    private void sendMail(String message) throws Exception
    {
        bc.startStopWatch("BO640.sendMail");
        
        YXY70.send(bc, rpt_code, bc.getLogin(), new Vector(), message);
        
        bc.stopStopWatch("BO640.sendMail");
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4673995704"));
        batchParameters.setArgs(args);
        new BO640().run(batchParameters);
    }
}