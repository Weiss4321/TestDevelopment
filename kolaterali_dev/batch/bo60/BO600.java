package hr.vestigo.modules.collateral.batch.bo60;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo60.BO601.*;
import hr.vestigo.modules.collateral.common.yoyF.*;
import hr.vestigo.modules.collateral.common.yoyI.YOYI0;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
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
 * Priprema kandidata za slanje obavijesti / ugovaranje grupne police.
 * @author hrakis
 */
public class BO600 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo60/BO600.java,v 1.6 2017/03/14 12:00:19 hrakis Exp $";
    
    private BatchContext bc;
    private BO601 bo601;
    private BigDecimal eve_id;
    private String retCode;
    
    private String policy_type;
    private String cust_type;
    
    private Date date;
    private HashMap<String, ParamWrnNotesData> params;

    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet[] sheet;
    private int[] rowIndex;
    private int[] columnIndex;
    private String fileName;
   
    private final String policy_type_coll = "K";
    private final String policy_type_inspol = "P";
    private final String rpt_code = "csv243";
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        this.bo601 = new BO601(bc);
        this.bc = bc;
        this.retCode = RemoteConstants.RET_CODE_SUCCESSFUL;
        this.date = new Date(bc.getExecStartTime().getTime());

        // evidentiranje eventa
        eve_id = bo601.insertIntoEvent();

        // dohvat i provjera parametara obrade
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;

        // provjera da li su svi postojeæi kandidati riješeni
        if (!bo601.isAllWarnNotesHandled())
        {
            String message = "Obrada za pripremu kandidata se ne mo\u017Ee izvr\u0161iti jer u evidenciji ve\u0107 postoje kandidati koji nisu potvr\u0111eni ili odba\u010Deni.";
            error(message, null);
            sendMail(message);
            return RemoteConstants.RET_CODE_ERROR;
        }

        // uèitavanje parametara za slanje obavijesti
        loadWarningNotesParameters();

        bc.beginTransaction();

        int counterResolved, counterCandidates;
        try
        {
            // otvaranje izvještaja
            openReport();
            
            // rješavanje plaæenih polica osiguranja
            counterResolved = resolvePolicies();
    
            // priprema kandidata
            counterCandidates = candidatePolicies();
            
            // spremanje izvještaja
            saveReport();
        }
        finally
        {
            dispose(workbook);
        }

        bc.commitTransaction();
        
        bc.info("****************************** REKAPITULACIJA ******************************");
        info("Broj rijesenih polica osiguranja: " + counterResolved);
        info("Broj pripremljenih kandidata: " + counterCandidates);
        bc.setCounter(counterCandidates);

        // slanje maila
        sendMail(counterResolved, counterCandidates);

        return retCode;
    }

    
    /**
     * Metoda rješava police osiguranja koje su plaæene.
     */
    private int resolvePolicies() throws Exception
    {
        bc.debug("******************** RJESAVANJE PLACENIH POLICA OSIGURANJA ********************");
        
        int counter = 0;
        IterPolicies iter = bo601.selectResolvedPolicies();
        while (iter.next())
        {
            bc.debug("kolateral " + iter.col_num().trim() + ", polica " + iter.ip_code().trim() + " (ID=" + iter.id() + ")"); 
            bc.debug("...polica placena do " + iter.ip_paid_until() + ", status slanja " + iter.wrn_status());
            
            String owner = bo601.selectRetailPlacementOwner(iter.col_hea_id());
            bc.debug("...vlasnik: " + owner);
            if (owner == null) continue;

            boolean isResolved = resolvePolicy(iter);
            if (isResolved)
            {
                counter++;
                writeDetailsRow(0, iter);
                bc.debug("...polica rijesena");
            }
        }
        iter.close();
        return counter;
    }
    
    /**
     * Metoda priprema kandidate za slanje obavijesti.
     * @return broj pripremljenih kandidata
     */
    private int candidatePolicies() throws Exception
    {
        bc.debug("***************************** PRIPREMA KANDIDATA ******************************");
        
        int counter = 0;
        String col_num_old = null;
        IterPolicies iter = bo601.selectCandidates();
        while (iter.next())
        {
            bc.debug("kolateral " + iter.col_num().trim() + ", polica " + iter.ip_code().trim() + " (ID=" + iter.id() + ")"); 
            bc.debug("...polica placena do " + iter.ip_paid_until() + ", status slanja " + iter.wrn_status());
            
            if (iter.col_num().equals(col_num_old))
            {
                bc.debug("...uzima se samo jedna polica po kolateralu");
                continue;
            }
            col_num_old = iter.col_num();
            
            String owner = bo601.selectRetailPlacementOwner(iter.col_hea_id());
            bc.debug("...vlasnik: " + owner);
            if (owner == null) continue;
            
            boolean isCandidated = candidatePolicy(iter);
            if (isCandidated)
            {
                counter++;
                int sheetIdx = "1".equals(iter.wrn_status()) ? 2 : 1;
                writeDetailsRow(sheetIdx, iter);
                bc.debug("...polica kandidirana");
            }
        }
        iter.close();
        return counter;
    }
    
    
    /**
     * Metoda koja zadanoj polici osiguranja postavlja status slanja obavijesti/opomene na '0', zapisuje to u povijest promjena, te stavlja obavijest/opomenu u 'R'-resolved.
     * @param iter iterator s podacima o polici osiguranja
     */
    private boolean resolvePolicy(IterPolicies iter) throws Exception
    {
        // iniciraj common za evidentiranje povijesti promjena
        YOYFData data = new YOYFData();
        if (policy_type.equals(policy_type_coll)) data.col_ins_id = iter.id(); else data.ip_id = iter.id();
        data.use_id = new BigDecimal(1);
        data.org_uni_id = new BigDecimal(53253);
        data.eve_id = eve_id;
        YOYF0 yoyF0 = new YOYF0(bc, data);
        yoyF0.selectOldState();  // dohvati staro stanje
        
        // postavi status slanja obavijesti/opomene za policu na '0'
        bo601.updatePolicyStatusToResolved(iter.id());
        
        // dohvati novo stanje i zapiši promjene u povijest promjena
        yoyF0.selectNewState();
        yoyF0.insertIntoIpChgHistory();
        
        // status slanja i tip obavijesti
        ParamWrnNotesData param = params.get(data.old_ip_wrn_status);
        if (param == null) return false;
        
        // zapiši u evidenciju slanja da je obavijest/opomena riješena
        bo601.insertResolvedIntoWarningNotes(iter.id(), iter.col_hea_id(), date, data.old_ip_wrn_status, param.cus_let_typ_id);
        
        return true;
    }
    
    /**
     * Metoda koja provjerava da li polica osiguranja ispunjava kriterije za slanje obavijesti/opomene, te ako 
     * su svi kriteriji zadovoljeni upisuje policu osiguranja kao kandidata za slanje obavijesti/opomene.
     * @param iter iterator s podacima o polici osiguranja
     */
    private boolean candidatePolicy(IterPolicies iter) throws Exception
    {
        // status slanja i tip obavijesti
        ParamWrnNotesData param = getNextWarning(iter.wrn_status());
        if (param == null)
        {
            bc.info("...nema parametrizacije za status " + iter.wrn_status());
            return false;
        }
        
        // zapiši policu kao kandidata za slanje obavijesti/opomene
        bo601.insertCandidateIntoWarningNotes(iter.id(), iter.col_hea_id(), iter.ip_paid_until(), date, param.wrn_status, param.cus_let_typ_id);
        
        return true;
    }
    
    /**
     * Metoda koja vraæa parametre obavijesti koja slijedi nakon zadanog statusa.
     * @param wrn_status Trenutni status slanja obavijesti/opomene po polici osiguranja
     * @return objekt s parameterima
     */
    private ParamWrnNotesData getNextWarning(String wrn_status)
    {
        for (ParamWrnNotesData param : params.values())
        {
            if (param.pre_wrn_status.equals(wrn_status)) return param;
        }
        return null;
    }
    
    
    /**
     * Metoda otvara izvještaj i kreira sheet u koji æe biti zapisani podaci. 
     */
    private void openReport()
    {
        workbook = new SXSSFWorkbook(100);
        styles = ExcelStyleData.createStyles(workbook);
        sheet = new Sheet[3];
        sheet[0] = workbook.createSheet("Rije\u0161ene police");
        sheet[1] = workbook.createSheet("Kandidati za slanje obavijesti");
        sheet[2] = workbook.createSheet("Kandidati za grupnu policu");
        ExcelUtils.setColumnWidths(sheet[0], new int[] { 150, 100, 50, 150 });
        ExcelUtils.setColumnWidths(sheet[1], new int[] { 150, 100, 50, 150 });
        ExcelUtils.setColumnWidths(sheet[2], new int[] { 150, 100, 50, 150 });
        rowIndex = new int[3];
        columnIndex = new int[3];
        writeHeaderRow(0);
        writeHeaderRow(1);
        writeHeaderRow(2);
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
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "\u0160ifra police", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Premija pla\u0107ena do", styles.headingStyle);        
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "Status police", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, "\u0160ifra kolaterala", styles.headingStyle);
        
        rowIndex[sheetIdx]++;
        sheet[sheetIdx].createFreezePane(0, rowIndex[sheetIdx]);
    }
    
    /**
     * Metoda zapisuje podatke o polici osiguranja u izvještaj.
     * @param sheetIdx broj sheeta
     * @param iter iterator s podacima
     */
    private void writeDetailsRow(int sheetIdx, IterPolicies iter) throws SQLException
    {
        columnIndex[sheetIdx] = 0;
        
        Row row = sheet[sheetIdx].createRow(rowIndex[sheetIdx]);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.ip_code().trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.ip_paid_until(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.wrn_status(), styles.normalStyle);        
        ExcelUtils.createCell(row, columnIndex[sheetIdx]++, iter.col_num().trim(), styles.normalStyle);
        
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
        fileName = dir + "ObradjenePoliceOsiguranja" + policy_type_suffix + dateString + ".xlsx";
        
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        new File(fileName + ".marker").createNewFile();
    }
    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose(Workbook workbook)
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }
    
    /**
     * Metoda koja šalje mail s informacijom o završenoj obradi i priloženom listom riješenih polica osiguranja.
     * @param counterResolved broj riješenih polica osiguranja
     * @param counterCandidates broj pripremljenih kandidata
     */
    private void sendMail(int counterResolved, int counterCandidates) throws Exception
    {
        bc.startStopWatch("BO600.sendMail");
        
        Vector attachments = new Vector();
        attachments.add(fileName);
        String message = "Obrada za pripremu kandidata za slanje obavijesti i ugovaranje grupne police je uspje\u0161no zavr\u0161ena.\n";
        message += "Broj rije\u0161enih polica osiguranja: " + counterResolved + "\n";
        message += "Broj pripremljenih kandidata: " + counterCandidates + "\n";
        message += "Lista obra\u0111enih polica osiguranja je u prilogu."; 
        YXY70.send(bc, rpt_code, bc.getLogin(), attachments, message);
        
        bc.stopStopWatch("BO600.sendMail");
    }
    
    
    private void sendMail(String message) throws Exception
    {
        bc.startStopWatch("BO600.sendMail");
        
        YXY70.send(bc, rpt_code, bc.getLogin(), new Vector(), message);
        
        bc.stopStopWatch("BO600.sendMail");
    }
    
    
    /**
     * Metoda uèitava parametre za slanje obavijesti.
     */
    private void loadWarningNotesParameters()
    {
        params = new HashMap<String, ParamWrnNotesData>();
        
        ParamWrnNotesData param;
        param = new ParamWrnNotesData();
        param.pre_wrn_status = "0";
        param.wrn_status = "1";
        param.cus_let_typ_id = new BigDecimal("189999");
        params.put(param.wrn_status, param);
        
        param = new ParamWrnNotesData();
        param.pre_wrn_status = "1";
        param.wrn_status = "5";
        param.cus_let_typ_id = new BigDecimal("229999");
        params.put(param.wrn_status, param);
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4490036704"));
        batchParameters.setArgs(args);
        new BO600().run(batchParameters);
    }
}