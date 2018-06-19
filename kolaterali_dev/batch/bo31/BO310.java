package hr.vestigo.modules.collateral.batch.bo31;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo31.BO311.CollateralIterator;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 *
 * @author hrakis
 * Izvješæe po vrstama kolaterala
 * 
 */
public class BO310 extends Batch
{ 
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo31/BO310.java,v 1.39 2017/10/17 12:51:42 hrakis Exp $";

    private Date report_date;
    private String customer_type;
    private BigDecimal col_cat_id;
    private String col_list;
    private String ind_archive;
    
    private BO311 bo311;
    private BatchContext bc;
    
    private ExcelStyleData styles;
    private Sheet[] sheet;
    private int[] rowIndex;
    private int[] columnIndex;
    private boolean autosized;

    
    public String executeBatch (BatchContext bc) throws Exception
    {
        this.bc = bc;
        this.bo311 = new BO311(bc);
        
        this.styles = null;
        this.sheet = new Sheet[2];
        this.rowIndex = new int[2];
        this.columnIndex = new int[2];
        this.autosized = false;
        
        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat trenutnog datuma
        Date current_date = bo311.selectCurrentDate();
        
        // dohvat zadnjeg izraèuna pokrivenosti
        HashMap colProcMap = bo311.selectColProId(report_date);
        if (colProcMap == null) return RemoteConstants.RET_CODE_ERROR;
        BigDecimal col_pro_id = (BigDecimal)colProcMap.get(OutputData.COL_PRO_ID);
        Date allocation_date = (Date)colProcMap.get(OutputData.ALLOCATION_DATE);
        bc.info("COL_PRO_ID = " + col_pro_id);
        bc.info("ALLOCATION_DATE = " + allocation_date);
        
        // common za konverziju valuta
        YRXX0 yrxx0 = new YRXX0(bc);
        
        // sve klase za generiranje izvještaja
        HashMap<Class, List<BigDecimal>> reportHandlers = new HashMap<Class, List<BigDecimal>>();
        reportHandlers.put(BO312.class, Arrays.asList(new BigDecimal("618223")));  // nekretnine
        reportHandlers.put(BO313.class, Arrays.asList(new BigDecimal("615223")));  // garancije
        reportHandlers.put(BO314.class, Arrays.asList(new BigDecimal("626223")));  // zalihe
        reportHandlers.put(BO315.class, Arrays.asList(new BigDecimal("616223")));  // police osiguranja
        reportHandlers.put(BO316.class, Arrays.asList(new BigDecimal("620223")));  // plovila
        reportHandlers.put(BO317.class, Arrays.asList(new BigDecimal("612223")));  // gotovinski depoziti
        reportHandlers.put(BO318.class, Arrays.asList(new BigDecimal("613223"), new BigDecimal("619223"), new BigDecimal("622223"), new BigDecimal("627223"), new BigDecimal("629223")));  // VRP
        reportHandlers.put(BO319.class, Arrays.asList(new BigDecimal("621223")));  // pokretnine
        reportHandlers.put(BO31A.class, Arrays.asList(new BigDecimal("624223")));  // vozila
        reportHandlers.put(BO31B.class, Arrays.asList(new BigDecimal("617223")));  // mjenice
        reportHandlers.put(BO31C.class, Arrays.asList(new BigDecimal("625223")));  // zadužnice
        reportHandlers.put(BO31D.class, Arrays.asList(new BigDecimal("614223")));  // cesije
        
        // dohvat klase za zadanu vrstu kolaterala
        Class reportClass = null;
        List<BigDecimal> categories = null;
        for (Entry<Class, List<BigDecimal>> entry : reportHandlers.entrySet())
        {
            if (entry.getValue().contains(col_cat_id))
            {
                reportClass = entry.getKey();
                categories = entry.getValue();
                break;
            }
        }
        
        // ako nije pronaðena nijedna klasa, prekini obradu
        if (reportClass == null || categories == null)
        {
            error("Nije moguce zadati izvjestaj za ID kategorije kolaterala = " + col_cat_id + "!", null);
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        // instanciranje klase
        bc.debug("Instanciram " + reportClass.getName() + "...");
        ReportData report = (ReportData)reportClass.newInstance();
        report.setBatchContext(bc);
        info(report.getFileName());
        info("Broj sheetova u izvjestaju = " + report.getNumberOfSheets());
        
        // generiranje imena datoteke
        String dateString = new SimpleDateFormat("yyyyMMdd").format(report_date);
        String fileName = bc.getOutDir() + "/" + report.getFileName() + dateString + ".xlsx";
        
        Workbook workbook = null;
        
        try
        {
            // stvori workbook
            workbook = new SXSSFWorkbook(100);
            this.styles = ExcelStyleData.createStyles(workbook);
            
            // stvori sheetove i zaglavlje u njima
            for (int sheetIdx = 0; sheetIdx < report.getNumberOfSheets(); sheetIdx++)
            {
                bc.debug("IZVJESTAJ " + (sheetIdx+1) + ":");
                sheet[sheetIdx] = workbook.createSheet();
                rowIndex[sheetIdx] = 0;
                columnIndex[sheetIdx] = 0;
                Row headerRow = sheet[sheetIdx].createRow(rowIndex[sheetIdx]++);
                ArrayList<String> columns = report.columns.get(sheetIdx);
                for (int j = 0; j < columns.size(); j++)
                {
                    String column = columns.get(j);
                    bc.debug("   " + (j+1) + ". " + column);
                    ExcelUtils.createCell(headerRow, columnIndex[sheetIdx]++, report.convertToUnicode(column), styles.headingStyle);
                }
                sheet[sheetIdx].createFreezePane(1, rowIndex[sheetIdx]);
            }
            
            for (BigDecimal col_cat_id : categories)
            {
                bc.info("COL_CAT_ID = " + col_cat_id);
                
                // dohvat kolaterala iz kategorije
                CollateralIterator iter = bo311.selectCollaterals(col_cat_id, report.hasMortgage(), customer_type, report_date, current_date, col_list, ind_archive);
                
                while (iter.next())
                {
                    bc.debug("COL_HEA_ID=" + iter.COL_HEA_ID());
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    
                    // dohvat dodatnih podataka o kolateralu
                    report.fillDataFromIterator(iter, data, OutputData.class);
                    report.selectAdditionalData(data, report_date, yrxx0);
                    bo311.selectAllocationData(data, col_pro_id, allocation_date, yrxx0);
                    bo311.selectB2AllocatedCollateralValue(data);
                    bo311.selectCollateralOwner(data);
                    bo311.selectCoborrower(data);
                    bo311.selectCoKvacica(data);
                    bo311.selectEstimator(data);
                    if (data.get(OutputData.premija_placena_do) == null) bo311.selectIpValiUntil(data);
                    bo311.selectPlacementOwner(data);
                    bo311.selectPonderRestAmount(data);
                    bo311.selectPonderCo(data, report_date, "MVP");
                    bo311.selectPonderCo(data, report_date, "CES");
                    bo311.selectPonderDfl(data, report_date);
                    data.put(OutputData.obrada, null);
                    
                    // zapiši podatke o kolateralu u izvještaj
                    bc.startStopWatch("BO310.writeToReport");
                    for (int sheetIdx = 0; sheetIdx < report.getNumberOfSheets(); sheetIdx++)
                    {
                        columnIndex[sheetIdx] = 0;
                        Row row = sheet[sheetIdx].createRow(rowIndex[sheetIdx]++);
                        ArrayList<String> columns = report.columns.get(sheetIdx);
                        
                        for (String column : columns)
                        {
                            writeData(sheetIdx, data, row, column);
                        }
                    }
                    bc.stopStopWatch("BO310.writeToReport");
                    
                    if (!autosized && rowIndex[0] > 150) autosize(workbook, report);
                }
                iter.close();
            }
            
            if (!autosized) autosize(workbook, report);
            
            // spremi izvještaj
            bc.startStopWatch("BO310.flush");
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            info("Broj redova zapisanih u izvjestaj = " + rowIndex[0]);
            new File(fileName + ".marker").createNewFile();
            bc.stopStopWatch("BO310.flush");
            
            // slanje maila
            bc.startStopWatch("BO310.sendMail");
            YXY70.send(bc, "csv169", bc.getLogin(), fileName);
            bc.stopStopWatch("BO310.sendMail");
            bc.debug("Mail poslan.");
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
        
        // stvaranje marker datoteka
        new File(fileName + ".marker").createNewFile();
        bc.debug("Stvoren marker.");

        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda za upis podatka u æeliju izvještaja.
     */
    private void writeData(int sheetIdx, HashMap<String, Object> data, Row row, String column) throws Exception
    {
        if (!data.containsKey(column))
        {
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)"???", styles.errorStyle);
            return;
        }

        Object obj = data.get(column);
        
        if (obj == null) {
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)null, styles.normalStyle);
        }
        else if (obj instanceof String){
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, ((String)obj).trim(), styles.normalStyle);
        }
        else if (obj instanceof BigDecimal){
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (BigDecimal)obj, styles.normalNumericStyle);
        }
        else if (obj instanceof Date){
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (Date)obj, styles.normalDateStyle);
        }
        else if (obj instanceof Integer){
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (Integer)obj, styles.normalGeneralNumericStyle);
        }
        else {
            ExcelUtils.createCell(row, columnIndex[sheetIdx]++, (String)"unknown type?", styles.errorStyle);
            warn(column + ": TYPE NOT FOUND FOR VALUE = " + obj);
            if (obj != null) bc.warning("object type = " + obj.getClass().getName());
        }
    }
    
    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose (Workbook workbook)
    {
        bc.startStopWatch("BO310.dispose");
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
        bc.stopStopWatch("BO310.dispose");
    }
    
    
    /**
     * Metoda postavlja širinu kolona u izvještaju.
     */
    private void autosize (Workbook workbook, ReportData report)
    {
        bc.startStopWatch("BO310.autosize");
        final int minimumWidth = 2750;
        final int maximumWidth = 12000;
        int numberOfSheets = report.getNumberOfSheets();
        
        for (int sheetIdx = 0; sheetIdx < numberOfSheets; sheetIdx++)
        {
            Sheet sheet = workbook.getSheetAt(sheetIdx);
            int lastColumnIndex = report.columns.get(sheetIdx).size();
            for (int colIdx = 0; colIdx < lastColumnIndex; colIdx++)
            {
                sheet.autoSizeColumn(colIdx);
                if (sheet.getColumnWidth(colIdx) < minimumWidth) sheet.setColumnWidth(colIdx, minimumWidth);
                else if (sheet.getColumnWidth(colIdx) > maximumWidth) sheet.setColumnWidth(colIdx, maximumWidth);
            }
        }
        this.autosized = true;
        bc.stopStopWatch("BO310.autosize");
    }

    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent () throws Exception
    {
        try
        {
            bc.startStopWatch("BO310.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("2537853003"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Izvjesce po vrstama kolaterala");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO310.insertIntoEvent");
            return eve_id;
        }
        catch (Exception ex)
        {
            bc.error("Dogodila se nepredvidjena greska pri evidentiranju pocetka izvodjenja obrade!", ex);
            throw ex;
        }
    }
    
    
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * Parametri se predaju u formatu: <code>bank_sign report_date customer_type col_cat_id col_list ind_archive</code>.
     * Svi parametri su obvezni.
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati <b>RB</b>.</dd>
     *    <dt>report_date</dt><dd>Datum za koji se radi izvještaj. Format mora biti yyyy-MM-dd.</dd>
     *    <dt>customer_type</dt><dd>Vrsta klijenta:
     *          <br/><b>P</b> - pravne osobe,
     *          <br/><b>F</b> - fizièke osobe.</dd>
     *    <dt>col_cat_id</dt><dd>ID kategorije kolaterala.</dd>
     *    <dt>col_list</dt><dd>Indikator liste na kojoj se kolateral nalazi: 
     *          <br/><b>A</b> - aktivna/referentska/verifikacijska lista,
     *          <br/><b>N</b> - lista neaktivnih,
     *          <br/><b>O</b> - lista oslobodjenih,
     *          <br/><b>S</b> - lista slobodnih.</dd>
     *    <dt>ind_archive</dt><dd>Indikator arhive:
     *          <br/><b>Y</b> - podaci se uzimaju iz arhive,
     *          <br/><b>N</b> - podaci se ne uzimaju iz arhive.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = {"Oznaka banke", "Datum izvjestaja", "Vrsta klijenta", "ID kategorije kolaterala", "Indikator liste", "Indikator arhive"};

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

            // datum izvještaja
            report_date = Date.valueOf(bc.getArg(1).trim());
            
            // vrsta klijenta
            customer_type = bc.getArg(2).trim().toUpperCase();
            if (!customer_type.equals("P") && !customer_type.equals("F")) throw new Exception("Dozvoljene vrijednosti za vrstu klijenta su P i F!");
            
            // ID kategorije kolaterala
            col_cat_id = new BigDecimal(bc.getArg(3).trim());
            
            // indikator liste
            col_list = bc.getArg(4).trim().toUpperCase();
            if (!col_list.equals("A") && !col_list.equals("N") && !col_list.equals("O") && !col_list.equals("S")) throw new Exception("Dozvoljene vrijednosti za indikator liste su A, N, O i S!");
            
            if (col_list.equalsIgnoreCase("O")) col_list = "4";
            else if (col_list.equalsIgnoreCase("S")) col_list = "F";
            else if (col_list.equalsIgnoreCase("N")) col_list = "N";
            
            // indikator arhive
            ind_archive = bc.getArg(5).trim().toUpperCase();
            if (!ind_archive.equals("Y") && !ind_archive.equals("N")) throw new Exception("Dozvoljene vrijednosti za indikator arhive su Y i N!");
            
            return true;
        }
        catch (Exception ex)
        {
            error("Neispravno zadani parametri!", ex);
            return false;
        }
    }
 
    
    private void error (String message, Exception ex) throws Exception
    {
        if (ex != null) bc.error(message, ex); else bc.error(message, new String[]{});
        bc.userLog("GRESKA: " + message);
    }
    
    private void warn (String message) throws Exception
    {
        bc.warning(message);
        bc.userLog("UPOZORENJE: " + message);
    }
    
    private void info (String message) throws Exception
    {
        bc.info(message);
        bc.userLog(message);
    }
    

    public static void main (String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2537850003"));
        batchParameters.setArgs(args);
        new BO310().run(batchParameters);
    }
}