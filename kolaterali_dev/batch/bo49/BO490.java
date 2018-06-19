package hr.vestigo.modules.collateral.batch.bo49;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo49.BO491.CustDataIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
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
 * Izvješæe o dugoroènim sporazumima. 
 * @author hrakis
 */
public class BO490 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo49/BO490.java,v 1.4 2016/11/25 12:43:30 hrakis Exp $";
    
    private BatchContext bc;
    private BO491 bo491;
    private ExcelStyleData styles;
    private Sheet[] sheets;
    private int[] rowIndex;
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo491 = new BO491(bc);
        Workbook workbook = null;
        sheets = new Sheet[4];
        rowIndex = new int[4];
        
        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        try
        {
            // kreiraj workbook i sheet
            workbook = new SXSSFWorkbook(100);
            styles = ExcelStyleData.createStyles(workbook);
            sheets[0] = workbook.createSheet("Aktivni DS");
            sheets[1] = workbook.createSheet("Neaktivni DS");
            sheets[2] = workbook.createSheet("Mjenice za DS");
            sheets[3] = workbook.createSheet("Zadu\u017Enice za DS");
            
            // formiranje zaglavlja u sheetovima
            for (int i = 0; i < sheets.length; i++) writeHeader(i);
            
            // dohvat podataka o okvirnim sporazumima i zapisivanje u sheetove                  
            CustDataIterator iter = bo491.selectFrameAgreements();
            while (iter.next())
            {
                if ("A".equalsIgnoreCase(iter.frame_status()))  // u prvi sheet stavljaju se aktivni DS 
                { 
                    writeRow(0, iter);
                }
                else  // u drugi sheet stavljaju se neaktivni DS
                {  
                    writeRow(1, iter);
                }
            }
            iter.close();
            info("Upisano " + rowIndex[0] + " redova u sheet za aktivne DS.");
            info("Upisano " + rowIndex[1] + " redova u sheet za neaktivne DS.");
            
            // dohvat podataka o mjenicama za DS i zapisivanje u sheet
            iter = bo491.selectBillsOfExchange();
            while (iter.next())
            {
                writeRow(2, iter);
            }
            iter.close();
            info("Upisano " + rowIndex[2] + " redova u sheet za mjenice.");
            
            // dohvat podataka o zadužnicama za DS i zapisivanje u sheet
            iter = bo491.selectLoanStocks();
            while (iter.next())
            {
                writeRow(3, iter);
            }
            iter.close();
            info("Upisano " + rowIndex[3] + " redova u sheet za zaduznice.");
            
            // spremi izvještaj
            String dateString = new SimpleDateFormat("yyyyMMdd").format(bc.getExecStartTime());
            String fileName = bc.getOutDir() + "/" + "DS_Hipoteke_Mjen_Zad_" + dateString + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            new File(fileName + ".marker").createNewFile();

            // pošalji izvještaj na mail
            YXY70.send(bc, "csv221", bc.getLogin(), fileName);
            info("Izvjestaj je kreiran i poslan na mail korisniku.");            
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
    
    
    /**
     * Metoda zapisuje zaglavlje u zadani sheet.
     * @param sheetIdx broj sheeta
     */
    private void writeHeader(int sheetIdx)
    {
        int columnIndex = 0;
        
        Sheet sheet = sheets[sheetIdx];
        
        // širine kolona
        ArrayList<Integer> widths = new ArrayList<Integer>();
        widths.add(100);        // Broj sporazuma
        widths.add(65);         // Interni MB komitenta
        widths.add(200);        // Komitent
        widths.add(100);        // Iznos sporazuma
        widths.add(75);         // Valuta sporazuma
        widths.add(80);         // Datum dospijeæa
        widths.add(75);         // Status sporazuma
        widths.add(150);        // Šifra kolaterala
        widths.add(70);         // Status kolaterala
        widths.add(120);        // Partija plasmana
        widths.add(150);        // Broj zahtjeva
        widths.add(65);         // DWH status plasmana
        widths.add(65);         // Status plasmana u modulu
        if (sheetIdx == 2 || sheetIdx == 3)
        {
            widths.add(65);     // Broj mjenica / zadužnica
        }
        widths.add(120);        // Zadnja raspoloživa izloženost plasmana
        widths.add(75);         // Valuta izloženosti
        widths.add(80);         // Datum izloženosti
        widths.add(65);         // ID vlasnika plasmana
        widths.add(200);        // Naziv vlasnika plasmana
        if (sheetIdx == 0 || sheetIdx == 1)
        {
            widths.add(60);     // Status hipoteke
            widths.add(60);     // Prioritet hipoteke
        }
        ExcelUtils.setColumnWidths(sheet, widths);
        
        // zaglavlje
        Row row = sheet.createRow(rowIndex[sheetIdx]);
        ExcelUtils.createCell(row, columnIndex++, "Broj sporazuma", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Interni MB komitenta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Komitent", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Iznos sporazuma", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta sporazuma", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum dospije\u0107a", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status sporazuma", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Broj zahtjeva", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "DWH status plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status plasmana u modulu", styles.headingStyle);
        if (sheetIdx == 2)
        {
            ExcelUtils.createCell(row, columnIndex++, "Broj mjenica", styles.headingStyle);
        }
        else if (sheetIdx == 3)
        {
            ExcelUtils.createCell(row, columnIndex++, "Broj zadu\u017Enica", styles.headingStyle);
        }
        ExcelUtils.createCell(row, columnIndex++, "Zadnja raspolo\u017Eiva izlo\u017Eenost plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta izlo\u017Eenosti", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum izlo\u017Eenosti", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "ID vlasnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv vlasnika plasmana", styles.headingStyle);
        if (sheetIdx == 0 || sheetIdx == 1)
        {
            ExcelUtils.createCell(row, columnIndex++, "Status hipoteke", styles.headingStyle);
            ExcelUtils.createCell(row, columnIndex++, "Prioritet hipoteke", styles.headingStyle);
        }
        
        rowIndex[sheetIdx]++;
        sheet.createFreezePane(0, rowIndex[sheetIdx]);
    }
    
    
    /**
     * Metoda zapisuje podatke o kolateralu u zadani sheet.
     * @param sheetIdx broj sheeta
     * @param iter iterator s podacima
     */
    private void writeRow(int sheetIdx, CustDataIterator iter) throws Exception
    {
        int columnIndex = 0;
        
        Sheet sheet = sheets[sheetIdx];
        
        Row row = sheet.createRow(rowIndex[sheetIdx]);
        ExcelUtils.createCell(row, columnIndex++, iter.agreement_no(), styles.normalStyle);                 // Broj sporazuma 
        ExcelUtils.createCell(row, columnIndex++, iter.register_no(), styles.normalStyle);                  // Interni MB komitenta
        ExcelUtils.createCell(row, columnIndex++, iter.name(), styles.normalStyle);                         // Komitent
        ExcelUtils.createCell(row, columnIndex++, iter.frame_amount(), styles.normalNumericStyle);          // Iznos sporazuma
        ExcelUtils.createCell(row, columnIndex++, iter.frame_currency(), styles.normalStyle);               // Valuta sporazuma
        ExcelUtils.createCell(row, columnIndex++, iter.frame_until(), styles.normalDateStyle);              // Datum dospijeæa
        if (sheetIdx == 0 || sheetIdx == 1)
        {
            ExcelUtils.createCell(row, columnIndex++, iter.frame_status(), styles.normalStyle);             // Status sporazuma
        }
        else if (sheetIdx == 2 || sheetIdx == 3)
        {
            String frame_status = "0".equals(iter.collateral_status()) || "1".equals(iter.collateral_status()) || "2".equals(iter.collateral_status()) || "3".equals(iter.collateral_status()) ? "A" : "N";
            ExcelUtils.createCell(row, columnIndex++, frame_status, styles.normalStyle);                    // Status sporazuma
        }
        ExcelUtils.createCell(row, columnIndex++, iter.col_num(), styles.normalStyle);                      // Šifra kolaterala
        ExcelUtils.createCell(row, columnIndex++, iter.collateral_status(), styles.normalStyle);            // Status kolaterala
        ExcelUtils.createCell(row, columnIndex++, iter.acc_no(), styles.normalStyle);                       // Partija plasmana
        ExcelUtils.createCell(row, columnIndex++, iter.request_no(), styles.normalStyle);                   // Broj zahtjeva
        ExcelUtils.createCell(row, columnIndex++, iter.dwh_status(), styles.normalStyle);                   // DWH status plasmana
        ExcelUtils.createCell(row, columnIndex++, iter.orig_status(), styles.normalStyle);                  // Status plasmana u modulu
        if (sheetIdx == 2 || sheetIdx == 3)
        {
            ExcelUtils.createCell(row, columnIndex++, iter.number(), styles.normalGeneralNumericStyle);     // Broj mjenica / zadužnica
        }
        ExcelUtils.createCell(row, columnIndex++, iter.exposure_balance(), styles.normalNumericStyle);      // Zadnja raspoloživa izloženost plasmana 
        ExcelUtils.createCell(row, columnIndex++, iter.exposure_currency(), styles.normalStyle);            // Valuta izloženosti
        ExcelUtils.createCell(row, columnIndex++, iter.exposure_date(), styles.normalDateStyle);            // Datum izloženosti
        ExcelUtils.createCell(row, columnIndex++, iter.placement_owner_register_no(), styles.normalStyle);  // ID vlasnika plasmana
        ExcelUtils.createCell(row, columnIndex++, iter.placement_owner_name(), styles.normalStyle);         // Naziv vlasnika plasmana
        if (sheetIdx == 0 || sheetIdx == 1)
        {
            ExcelUtils.createCell(row, columnIndex++, iter.hf_status(), styles.normalStyle);                // Status hipoteke
            ExcelUtils.createCell(row, columnIndex++, iter.hf_priority(), styles.normalStyle);              // Prioritet hipoteke
        }
        
        rowIndex[sheetIdx]++;
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
            bc.startStopWatch("BO990.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("2675611514"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "bo49:Izvjesce o dugorocnim sporazumima");
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
        BatchParameters bp = new BatchParameters(new BigDecimal("2675608654"));
        bp.setArgs(args);
        new BO490().run(bp);       
    }
}