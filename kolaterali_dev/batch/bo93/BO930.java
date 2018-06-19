package hr.vestigo.modules.collateral.batch.bo93;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo93.BO931.RealEstateIterator;
import hr.vestigo.modules.collateral.batch.bo93.BO931.VehicleIterator;
import hr.vestigo.modules.collateral.common.factory.CollateralCommonFactory;
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.collateral.common.yoyH.YOYH0;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Revalorizacija kolaterala - knjiženje i ažuriranje tržišne vrijednosti kolaterala.
 * @author hrakis
 */
public class BO930 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo93/BO930.java,v 1.2 2014/12/16 12:35:48 hrakis Exp $";

    private BatchContext bc;
    private BO931 bo931;
    private YOYH0 yoyH0;

    private Date currentDate;

    private ArrayList<CollateralData> collaterals = new ArrayList<CollateralData>();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
    private final BigDecimal maxCoef = new BigDecimal("9.99");


    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo931 = new BO931(bc);
        this.yoyH0 = new YOYH0(bc);

        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;

        // dohvat trenutnog datuma
        this.currentDate = new Date(bc.getExecStartTime().getTime());
        info("Datum revalorizacije = " + dateFormat.format(currentDate));

        // instanciranje commona za knjiženje
        CollateralPosting collPosting = CollateralCommonFactory.getCollateralPosting(bc);

        // uèitavanje vozila
        VehicleIterator iterVehicle = bo931.selectVehicles();
        while (iterVehicle.next())
        {
            VehicleData vehicle = getVehicleDataFromIterator(iterVehicle);
            vehicle.customer_type = bo931.selectNumberOfCorporatePlacements(vehicle.col_hea_id) == 0 ? "RET" : "CO";
            collaterals.add(vehicle);
        }
        iterVehicle.close();

        // uèitavanje nekretnina
        RealEstateIterator iterRealEstate = bo931.selectRealEstates();
        while (iterRealEstate.next())
        {
            RealEstateData realEstate = getRealEstateDataFromIterator(iterRealEstate);
            realEstate.customer_type = bo931.selectNumberOfCorporatePlacements(realEstate.col_hea_id) == 0 ? "RET" : "CO";
            collaterals.add(realEstate);
        }
        iterRealEstate.close();

        try
        {
            bc.beginTransaction();

            // evidentiranje poèetka obrade za kolaterale
            BigDecimal col_pro_id = bo931.insertIntoColProc();
            bc.debug("COL_PRO_ID = " + col_pro_id);

            for (CollateralData collateral : collaterals)
            {
                info(collateral.col_num + ": " + decimalFormat.format(collateral.nomi_value) + " " + collateral.nomi_value_cur_code + " -> " + decimalFormat.format(collateral.new_nomi_value) + " " + collateral.new_nomi_value_cur_code);
                
                // provjera valute
                if (!collateral.nomi_value_cur_id.equals(collateral.new_nomi_value_cur_id))
                {
                    error("Valuta trzisne vrijednosti kolaterala i valuta revaloriziranog iznosa nisu isti za kolateral " + collateral.col_num + "!", null);
                    bc.rollbackTransaction();
                    return RemoteConstants.RET_CODE_ERROR;
                }

                // ogranièenje koeficijenta na 9.99 (zbog COLL_HEAD)
                collateral.koef_rev = collateral.koef_rev.min(maxCoef);

                // ažuriranje vrijednosti kolaterala
                bo931.updateCollHead(collateral);

                // poziv knjiženja
                collPosting.CollPosting(collateral.col_hea_id, false);

                // oznaèavanje izraèuna finaliziranim
                bo931.finalizeRevaluation(collateral.col_rev_id);

                // evidentiranje revalorizacije
                bo931.insertIntoColTurnover(collateral, col_pro_id);
                
                // historizacija kolaterala
                yoyH0.historize(collateral.col_hea_id);
            }

            // deaktiviranje koeficijenata promjene
            bo931.deactivateCoefficients();

            // deaktiviranje liste kolaterala izbaèenih iz revalorizacije
            bo931.deactivateExceptions();

            // poništavanje datuma uèitavanja koeficijenata i datuma izraèuna revalorizacije
            bo931.updateLastDate("bo91", "0");
            bo931.updateLastDate("bo92", "0");

            // postavljanje datuma izvršenja revalorizacije
            bo931.updateLastDate("bo93", "1");

            // evidentiranje završetka obrade i broja obraðenih zapisa
            bo931.updateColProc(col_pro_id, collaterals.size());

            bc.commitTransaction();
            info("Revalorizirano je " + collaterals.size() + " kolaterala. Vrijednosti su zapisane u bazu podataka.");
        }
        catch (Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri provedbi revalorizacije. Nista nije spremljeno u bazu podataka.", ex);
            bc.rollbackTransaction();
            throw ex;
        }
        
        makeReportAndSendToMail();

        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda kreira i vraæa objekt s podacima o vozilu na temelju zadanog iteratora.
     * @param iter iterator
     * @return objekt s podacima o vozilu
     */
    private VehicleData getVehicleDataFromIterator(VehicleIterator iter) throws Exception
    {
        VehicleData vehicle = new VehicleData();
        
        vehicle.col_hea_id = iter.col_hea_id();
        vehicle.col_num = iter.col_num().trim();
        vehicle.collateral_status = iter.collateral_status(); 
        vehicle.col_cat_id = iter.col_cat_id();
        vehicle.col_cat_name = iter.col_cat_name();
        vehicle.col_typ_id = iter.col_typ_id();
        vehicle.col_typ_name = iter.col_typ_name();
        vehicle.col_sub_id = iter.col_sub_id();
        vehicle.col_sub_name = iter.col_sub_name();
        vehicle.nomi_value = iter.nomi_value();
        vehicle.nomi_value_cur_id = iter.nomi_value_cur_id();
        vehicle.nomi_value_cur_code = iter.nomi_value_cur_code();
        vehicle.nomi_value_date = iter.nomi_value_date();
        vehicle.est_date = iter.est_date();
        vehicle.veh_made_year = iter.veh_made_year();
        vehicle.reva_date_am = iter.reva_date_am();
        vehicle.col_rev_id = iter.col_rev_id();
        vehicle.koef_rev = iter.koef_rev();
        vehicle.new_nomi_value = iter.new_value();
        vehicle.new_nomi_value_cur_id = iter.new_value_cur_id();
        vehicle.new_nomi_value_cur_code = iter.new_value_cur_code();
        
        return vehicle;
    }
    
    
    /**
     * Metoda kreira i vraæa objekt s podacima o nekretnini na temelju zadanog iteratora.
     * @param iter iterator
     * @return objekt s podacima o nekretnini
     */
    private RealEstateData getRealEstateDataFromIterator(RealEstateIterator iter) throws Exception
    {
        RealEstateData realEstate = new RealEstateData();
        
        realEstate.col_hea_id = iter.col_hea_id();
        realEstate.col_num = iter.col_num().trim();
        realEstate.collateral_status = iter.collateral_status(); 
        realEstate.col_cat_id = iter.col_cat_id();
        realEstate.col_cat_name = iter.col_cat_name();
        realEstate.col_typ_id = iter.col_typ_id();
        realEstate.col_typ_name = iter.col_typ_name();
        realEstate.col_sub_id = iter.col_sub_id();
        realEstate.col_sub_name = iter.col_sub_name();
        realEstate.nomi_value = iter.nomi_value();
        realEstate.nomi_value_cur_id = iter.nomi_value_cur_id();
        realEstate.nomi_value_cur_code = iter.nomi_value_cur_code();
        realEstate.nomi_value_date = iter.nomi_value_date();
        realEstate.est_date = iter.est_date();
        realEstate.cadastral = new LocationData(iter.cada_id(), iter.cada_code(), iter.cada_name(), "");
        realEstate.city = new LocationData(iter.city_id(), iter.city_code(), iter.city_name(), "");
        realEstate.county = new LocationData(iter.county_id(), iter.county_code(), iter.county_name(), "");
        realEstate.reva_date_am = iter.reva_date_am();
        realEstate.col_rev_id = iter.col_rev_id();
        realEstate.koef_rev = iter.koef_rev();
        realEstate.new_nomi_value = iter.new_value();
        realEstate.new_nomi_value_cur_id = iter.new_value_cur_id();
        realEstate.new_nomi_value_cur_code = iter.new_value_cur_code();
        
        return realEstate;
    }


   
    /**
     * Metoda èita podatke iz baze podataka, zapisuje ih u Excel datoteku i šalje na mail korisniku.
     * @return da li je izvještaj uspješno generiran i poslan korisniku
     */
    private boolean makeReportAndSendToMail() throws Exception
    {
        Workbook workbook = null;
        try
        {
            String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
            String fileName = bc.getOutDir() + "/" + "IzvjestajOZavrsenojRevalorizaciji" + dateString + ".xlsx";
            
            // kreiraj workbook
            workbook = new SXSSFWorkbook(100);
            ExcelStyleData styles = ExcelStyleData.createStyles(workbook);
            
            // kreiraj sheetove
            Sheet sheetRealEstates = workbook.createSheet("Nekretnine");
            Sheet sheetVehicles = workbook.createSheet("Vozila");
            
            // podesi širinu kolona
            ExcelUtils.setColumnWidths(sheetRealEstates, new int[] { 50, 35, 120, 75, 95, 45, 75, 210, 230, 190, 175, 145, 95 });
            ExcelUtils.setColumnWidths(sheetVehicles, new int[] { 50, 35, 120, 75, 95, 45, 75, 210, 260, 65, 95 });
            
            // zapiši podatke u sheetove
            writeRealEstateReport(sheetRealEstates, styles);
            writeVehicleReport(sheetVehicles, styles);
    
            // spremi izvještaj
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            dispose(workbook);
            new File(fileName + ".marker").createNewFile();
    
            // pošalji izvještaj na mail
            YXY70.send(bc, "csvbo93", bc.getLogin(), fileName);
            
            info("Izvjestaj je kreiran i poslan na mail korisniku.");
            return true;
        }
        catch (Exception ex)
        {
            warn("Izvjestaj nije poslan na mail.", ex);
            return false;
        }
        finally
        {
            dispose(workbook);
        }
    }
    
    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose(Workbook workbook)
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
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
        ExcelUtils.createCell(row, columnIndex++, "Lista", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kol.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "NOVA TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum procj.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tip", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrsta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u017Dupanija", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Mjesto", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Katastarska op\u0107ina (\u010Cetvrt)", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Stara TV", styles.headingStyle);
        rowIndex++;
        
        sheet.createFreezePane(0, rowIndex);
        
        // zapiši podatke
        for (CollateralData collateral : collaterals)
        {
            if (!(collateral instanceof RealEstateData)) continue;
            RealEstateData realEstate = (RealEstateData)collateral;
            
            columnIndex = 0;
            row = sheet.createRow(rowIndex);
            ExcelUtils.createCell(row, columnIndex++, realEstate.customer_type, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.collateral_status, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.col_num, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.nomi_value_date, styles.normalDateStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.new_nomi_value, styles.normalNumericStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.nomi_value_cur_code, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.est_date, styles.normalDateStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.col_typ_name, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.col_sub_name, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.county.name, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.city.name, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.cadastral.name, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, realEstate.nomi_value, styles.normalNumericStyle);
            rowIndex++;
        }
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
        ExcelUtils.createCell(row, columnIndex++, "Lista", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kol.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "NOVA TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum procj.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tip", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrsta", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "God. proizv.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Stara TV", styles.headingStyle);
        rowIndex++;

        sheet.createFreezePane(0, rowIndex);

        // zapiši podatke
        for (CollateralData collateral : collaterals)
        {
            if (!(collateral instanceof VehicleData)) continue;
            VehicleData vehicle = (VehicleData)collateral;

            columnIndex = 0;
            row = sheet.createRow(rowIndex);
            ExcelUtils.createCell(row, columnIndex++, vehicle.customer_type, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.collateral_status, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.col_num, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.nomi_value_date, styles.normalDateStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.new_nomi_value, styles.normalNumericStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.nomi_value_cur_code, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.est_date, styles.normalDateStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.col_typ_name, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.col_sub_name, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.veh_made_year, styles.normalStyle);
            ExcelUtils.createCell(row, columnIndex++, vehicle.nomi_value, styles.normalNumericStyle);
            rowIndex++;
        }
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
            event.put("eve_typ_id", new BigDecimal("6546886704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Revalorizacija kolaterala");
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
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB#COL.</dd>
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
            if (!bank_sign.equals("RB") && !bank_sign.equals("RB#COL"))
            {
                error("Oznaka banke mora biti RB ili RB#COL!", null);
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
    
    private void warn(String message, Exception ex) throws Exception
    {
        if (ex != null) bc.error(message, ex); else bc.warning(message);
        bc.userLog("UPOZORENJE: " + message);
    }

    private void info(String message) throws Exception
    {
        bc.info(message);
        bc.userLog(message);
    }


    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("6546833704"));
        bp.setArgs(args);
        new BO930().run(bp);
    }
}