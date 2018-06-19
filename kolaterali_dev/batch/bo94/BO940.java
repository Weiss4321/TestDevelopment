package hr.vestigo.modules.collateral.batch.bo94;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo94.BO941.RealEstateIterator;
import hr.vestigo.modules.collateral.batch.bo94.BO941.RevaExceptionIterator;
import hr.vestigo.modules.collateral.batch.bo94.BO941.VehicleIterator;
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
 * Izvještaj o tržišnoj vrijednosti nekretnina i vozila
 * @author hrakis
 */
public class BO940 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo94/BO940.java,v 1.2 2015/01/29 15:05:36 hrakis Exp $";
    
    private BatchContext bc;
    private BO941 bo941;
    
    private ArrayList<BigDecimal> exceptions = new ArrayList<BigDecimal>();

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo941 = new BO941(bc);
        Workbook workbook = null;

        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // uèitavanje liste kolaterala koji su iskljuèeni iz revalorizacije
        loadRevalorizationExceptions();

        try
        {
            // kreiraj workbook
            workbook = new SXSSFWorkbook(100);
            ExcelStyleData styles = ExcelStyleData.createStyles(workbook);

            // kreiraj sheetove
            Sheet sheetRealEstates = workbook.createSheet("Nekretnine");
            Sheet sheetVehicles = workbook.createSheet("Vozila");

            // podesi širinu kolona
            ExcelUtils.setColumnWidths(sheetRealEstates, new int[] { 50, 65, 35, 120, 75, 95, 45, 75, 210, 230, 190, 175, 145, 95 });
            ExcelUtils.setColumnWidths(sheetVehicles, new int[] { 50, 65, 35, 120, 75, 95, 95, 45, 75, 210, 260, 65, 95 });

            // zapiši podatke u sheetove
            writeRealEstateReport(sheetRealEstates, styles);
            writeVehicleReport(sheetVehicles, styles);

            // spremi izvještaj
            String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
            String fileName = bc.getOutDir() + "/" + "KontrolniIzvjestaj" + dateString + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            new File(fileName + ".marker").createNewFile();

            // pošalji izvještaj na mail
            YXY70.send(bc, "csvbo94", bc.getLogin(), fileName);
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
     * Metoda uèitava popis kolaterala koji su na listi iskljuèenih iz revalorizacije.
     */
    private void loadRevalorizationExceptions() throws Exception
    {
        RevaExceptionIterator iter = bo941.selectRevalorizationExceptions();
        while (iter.next())
        {
            exceptions.add(iter.col_hea_id());
        }
        iter.close();
    }
    
    
    /**
     * Metoda zapisuje podatke za izvještaj u sheet za nekretnine.
     * @param sheet sheet za nekretnine
     * @param styles stilovi za izvještaj
     */
    private void writeRealEstateReport(Sheet sheet, ExcelStyleData styles) throws Exception
    {
        int rowIndex = 0;
        int columnIndex = 0;

        // zapiši zagljavlje
        Row row = sheet.createRow(rowIndex);
        ExcelUtils.createCell(row, columnIndex++, "Co/Ret", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Isklju\u010Deni", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Lista", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kol.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum procj.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tip", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrsta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u017Dupanija", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Mjesto", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Katastarska op\u0107ina (\u010Cetvrt)", styles.headingStyle);
        rowIndex++;
        
        sheet.createFreezePane(0, rowIndex);

        // dohvati i zapiši podatke
        RealEstateIterator iter = bo941.selectRealEstates();
        while (iter.next())
        {
            columnIndex = 0;
            row = sheet.createRow(rowIndex);
            String customerType = bo941.selectNumberOfCorporatePlacements(iter.col_hea_id()) > 0 ? "CO" : "RET";
            ExcelUtils.createCell(row, columnIndex++, customerType, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, exceptions.contains(iter.col_hea_id()) ? "da" : "ne", styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.collateral_status(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.col_num().trim(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.nomi_value_date(), styles.normalDateStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.nomi_value(), styles.normalNumericStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.nomi_value_cur_code(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.est_date(), styles.normalDateStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.col_typ_name(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.col_sub_name(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.county_name(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.city_name(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.cada_name(), styles.normalStyle);
            rowIndex++;
        }
        iter.close();
        
        info("Ukupan broj zapisa u sheetu za nekretnine: " + rowIndex);
    }
    
    
    /**
     * Metoda zapisuje podatke za izvještaj u sheet za vozila.
     * @param sheet sheet za vozila
     * @param styles stilovi za izvještaj
     */
    private void writeVehicleReport(Sheet sheet, ExcelStyleData styles) throws Exception
    {
        int rowIndex = 0;
        int columnIndex = 0;

        // zapiši zagljavlje
        Row row = sheet.createRow(rowIndex);
        ExcelUtils.createCell(row, columnIndex++, "Co/Ret", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Isklju\u010Deni", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Lista", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kol.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Procijenjena vrijednost", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum procj.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tip", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrsta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "God. proizv.", styles.headingStyle);
        rowIndex++;
        
        sheet.createFreezePane(0, rowIndex);

        // dohvati i zapiši podatke
        VehicleIterator iter = bo941.selectVehicles();
        while (iter.next())
        {
            columnIndex = 0;
            row = sheet.createRow(rowIndex);
            String customerType = bo941.selectNumberOfCorporatePlacements(iter.col_hea_id()) > 0 ? "CO" : "RET";
            ExcelUtils.createCell(row, columnIndex++, customerType, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, exceptions.contains(iter.col_hea_id()) ? "da" : "ne", styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.collateral_status(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.col_num().trim(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.nomi_value_date(), styles.normalDateStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.nomi_value(), styles.normalNumericStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.est_value(), styles.normalNumericStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.nomi_value_cur_code(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.est_date(), styles.normalDateStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.col_typ_name(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.col_sub_name(), styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, iter.veh_made_year(), styles.normalStyle);
            rowIndex++;
        }
        iter.close();
        
        info("Ukupan broj zapisa u sheetu za vozila: " + rowIndex);
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
            bc.startStopWatch("BO930.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6563372704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Izvjestaj o trzisnoj vrijednosti nekretnina i vozila");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO930.insertIntoEvent");
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
        BatchParameters bp = new BatchParameters(new BigDecimal("6563371704"));
        bp.setArgs(args);
        new BO940().run(bp);
    }
}