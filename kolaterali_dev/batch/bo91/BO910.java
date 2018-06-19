package hr.vestigo.modules.collateral.batch.bo91;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo91.BO911.CollateralTypeIterator;
import hr.vestigo.modules.collateral.batch.bo91.BO911.LocationIterator;
import hr.vestigo.modules.collateral.batch.bo91.BO911.RevaKoefIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;


/**
 * Uèitavanje koeficijenata promjene za revalorizaciju.
 * @author hrakis
 */
public class BO910 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo91/BO910.java,v 1.1 2014/08/01 10:36:05 hrakis Exp $";

    private BatchContext bc;
    private BO911 bo911;

    /** Putanja do ulaznog direktorija - parametar koji se predaje obradi. */
    private String inputDirectoryName;

    /** Kolekcija svih mapiranih tipova kolaterala za revalorizaciju. Kljuè je naziv tipa kolaterala, a vrijednost objekt s podacima o tipu. */
    private HashMap<String, TypeData> collateralTypes = new HashMap<String, TypeData>();

    /** Kolekcija svih katastarskih opæina. Kljuè kolekcije je šifra katastarske opæine, a vrijednost objekt s podacima o katastarskoj opæini. */
    private HashMap<String, LocationData> cadastralMunicipalities = new HashMap<String, LocationData>();

    /** Kolekcija svih gradova. Kljuè kolekcije je šifra grada, a vrijednost objekt s podacima o gradu. */
    private HashMap<String, LocationData> cities = new HashMap<String, LocationData>();

    /** Kolekcija svih županija. Kljuè kolekcije je šifra županije, a vrijednost objekt s podacima o gradu. */
    private HashMap<String, LocationData> counties = new HashMap<String, LocationData>();

    /** Oznaka da li je bilo grešaka prilikom parsiranja ulazne datoteke. */
    private boolean hasError = false;

    private Sheet sheetVehicles, sheetCadastralMunicipalities, sheetCities, sheetCounties;

    /** Sheet koji se trenutno parsira. */
    private Sheet currentSheet;

    /** Æelija koja se trenutno parsira. */
    private CellReference currentCellReference;

    /** Kolekcija svih uèitanih koeficijenata. */
    private ArrayList<RevaKoefData> coefficients = new ArrayList<RevaKoefData>();

    private static final BigDecimal zero = new BigDecimal("0.00");
    private static final BigDecimal maxValue = new BigDecimal("999999999999.99");


    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo911 = new BO911(bc);

        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;

        // dohvat ulazne datoteke
        String inputShortFileName = getInputFileName(this.inputDirectoryName);
        String inputFileName = inputDirectoryName + "/" + inputShortFileName;
        info("Ulazna datoteka je " + inputShortFileName);
        bc.debug("Otvaram " + inputFileName);

        // otvaranje workbooka
        Workbook workbook = getWorkbook(inputFileName);
        bc.debug("Otvorena je ulazna datoteka.");

        // provjera da li workbook ima dovoljan broj sheetova
        int numberOfSheets = workbook.getNumberOfSheets();
        if (numberOfSheets < 4)
        {
            error("Excel datoteka s koeficijentima ima premali broj sheetova (" + numberOfSheets + "), a trebala bi imati 4!", null);
            return RemoteConstants.RET_CODE_ERROR;
        }

        // dohvat sheetova i odreðivanje raspona æelija u kojima se nalaze koeficijenti
        sheetVehicles = workbook.getSheetAt(0);
        info("Sheet za vozila ima naziv '" + sheetVehicles.getSheetName() + "'.");
        CellRangeAddress rangeVehicles = getCoefficientCellRange(sheetVehicles, 2, 4);
        if (rangeVehicles != null) info("Raspon celija u kojima su koeficijenti za vozila je " + rangeVehicles.formatAsString()); 
        else info("Sheet za vozila ne sadrzi koeficijente.");
        
        sheetCadastralMunicipalities = workbook.getSheetAt(1);
        info("Sheet za katastarske opcine ima naziv '" + sheetCadastralMunicipalities.getSheetName() + "'.");
        CellRangeAddress rangeCadastralMunicipalities = getCoefficientCellRange(sheetCadastralMunicipalities, 2, 3);
        if (rangeCadastralMunicipalities != null) info("Raspon celija u kojima su koeficijenti za katastarske opcine je " + rangeCadastralMunicipalities.formatAsString());
        else info("Sheet za katastarske opcine ne sadrzi koeficijente.");
        
        sheetCities = workbook.getSheetAt(2);
        info("Sheet za gradove ima naziv '" + sheetCities.getSheetName() + "'.");
        CellRangeAddress rangeCities = getCoefficientCellRange(sheetCities, 2, 3);
        if (rangeCities != null) info("Raspon celija u kojima su koeficijenti za gradove je " + rangeCities.formatAsString());
        else info("Sheet za gradove ne sadrzi koeficijente.");
        
        sheetCounties = workbook.getSheetAt(3);
        info("Sheet za zupanije ima naziv '" + sheetCounties.getSheetName() + "'.");
        CellRangeAddress rangeCounties = getCoefficientCellRange(sheetCounties, 2, 3);
        if (rangeCounties != null) info("Raspon celija u kojima su koeficijenti za zupanije je " + rangeCounties.formatAsString());
        else info("Sheet za zupanije ne sadrzi koeficijente.");

        // uèitavanje mapiranih tipova kolaterala
        loadCollateralTypes();

        // uèitavanje katastarskih opæina
        loadCadastralMunicipalities();

        // uèitavanje mjesta
        loadCities();

        // uèitavanje županija
        loadCounties();

        try
        {
            bc.beginTransaction();

            // evidentiranje obrade za kolaterale
            BigDecimal col_pro_id = bo911.insertIntoColProc();
            bc.debug("COL_PRO_ID = " + col_pro_id);

            // parsiranje sheetova
            parseVehicleSheet(sheetVehicles, rangeVehicles);
            parseRealEstateSheet(sheetCadastralMunicipalities, rangeCadastralMunicipalities);
            parseRealEstateSheet(sheetCities, rangeCities);
            parseRealEstateSheet(sheetCounties, rangeCounties);
            
            if (hasError)
            {
                info("Zbog nadjenih gresaka u ulaznoj datoteci nijedan koeficijent nece biti ucitan. Postojeci koeficijenti (ako ih ima) u bazi podataka ostaju vazeci.");
                bc.rollbackTransaction();
                return RemoteConstants.RET_CODE_ERROR;
            }
            else
            {
                info("Iz ulazne datoteke je procitano " + coefficients.size() + " koeficijenata promjene.");
                
                // deaktiviranje svih aktivnih koeficijenata
                bo911.deactivateCoefficients();

                // ubacivanje novih koeficijenata u bazu
                for (RevaKoefData data : coefficients)
                {
                    bo911.insertIntoRevaKoef(col_pro_id, data);
                }

                // evidentiranje završetka obrade i broja obraðenih zapisa
                bo911.updateColProc(col_pro_id, coefficients.size());
                
                // evidentiranje datuma uèitavanja koeficijenata
                bo911.updateLastDate();
                
                bc.commitTransaction();
                info("Ucitani koeficijenti su upisani u bazu podataka. Postojeci koeficijenti su proglaseni nevazecima.");
            }
        }
        catch (Exception ex)
        {
            error("Doslo je do nepredvidjene greske kod citanja ulazne datoteke! " + (currentCellReference == null ? "" : "Ucitavanje je stalo na '" + currentSheet.getSheetName() + "'!" + currentCellReference.formatAsString() + ". ") + "Nijedan koeficijent nece biti ucitan. Postojeci koeficijenti (ako ih ima) u bazi podataka ostaju vazeci.", ex);
            bc.rollbackTransaction();
            throw ex;
        }

        // prebacivanje ulazne datoteke i markera u izlazni direktorij
        new File(inputFileName).renameTo(new File(bc.getOutDir() + "/" + inputShortFileName));
        new File(inputFileName + ".marker").renameTo(new File(bc.getOutDir() + "/" + inputShortFileName + ".marker"));
        bc.debug("Premjestena ulazna datoteka i marker.");
        
        // èitanje svih aktivnih koeficijenata iz baze i ispis u korisnièki log
        boolean mailSent = makeReportAndSendToMail();
        if (!mailSent) return RemoteConstants.RET_CODE_WARNING;
        
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }


    /**
     * Metoda parsira sheet za vozila.
     * @param sheet sheet koji se parsira
     * @param range raspon æelija
     */
    private void parseVehicleSheet(Sheet sheet, CellRangeAddress range) throws Exception
    {
        if (sheet == null || range == null) return;

        bc.startStopWatch("BO910.parseVehicleSheet");
        
        currentSheet = sheet;
        bc.debug("Pocinje parsiranje sheeta '" + sheet.getSheetName() + "'...");

        // starosti vozila
        Row topRow = sheet.getRow(1);
        Integer[] ages = new Integer[range.getLastColumn() - range.getFirstColumn() + 2];
        ages[0] = 0;
        for (int columnIndex = range.getFirstColumn(); columnIndex <= range.getLastColumn(); columnIndex++)
        {
            currentCellReference = new CellReference(topRow.getRowNum(), columnIndex);
            Integer age = 0;
            Object age_obj = getCellValue(topRow.getCell(columnIndex));
            if (!(age_obj instanceof BigDecimal))
            {
                error("Greska kod dohvata proteklog vremena u mjesecima - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + age_obj + "' sto nije valjani broj!", null);
            }
            else
            {
                age = ((BigDecimal)age_obj).intValue();
            }
            bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + age_obj + " -> " + age);
            ages[columnIndex - range.getFirstColumn() + 1] = age;
        }
        bc.debug("Starosti vozila = " + Arrays.toString(ages));

        // koeficijenti
        for (int rowIndex = range.getFirstRow(); rowIndex <= range.getLastRow(); rowIndex++)
        {
            Row row = sheet.getRow(rowIndex);

            TypeData type = null;
            BigDecimal value_from = null;
            BigDecimal value_to = null;

            // tip kolaterala
            currentCellReference = new CellReference(row.getRowNum(), 0);
            Object col_gcm_typ_name_obj = getCellValue(row.getCell(0));
            if (!(col_gcm_typ_name_obj instanceof String))
            {
                error("Greska kod dohvata tipa kolaterala - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + col_gcm_typ_name_obj + "' sto nije valjana vrijednost! Celija mora sadrzavati naziv tipa kolaterala za revalorizaciju!", null);
            }
            else
            {
                String col_gcm_typ_name = (String)col_gcm_typ_name_obj;
                type = collateralTypes.get(col_gcm_typ_name);
                if (type == null)
                {
                    error("Greska kod dohvata tipa kolaterala - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + col_gcm_typ_name_obj + "' sto nije valjani tip kolaterala za revalorizaciju!", null);
                }
            }
            bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + col_gcm_typ_name_obj + " -> " + type);

            // nominalna vrijednost OD
            currentCellReference = new CellReference(row.getRowNum(), 1);
            Object value_from_obj = getCellValue(row.getCell(1));
            if (value_from_obj != null && !(value_from_obj instanceof BigDecimal))
            {
                error("Greska kod dohvata nominalne vrijednosti OD - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + value_from_obj + "' sto nije valjani broj!", null);
            }
            else
            {
                value_from = (BigDecimal)value_from_obj;
                if (value_from == null) value_from = zero; else value_from.setScale(2, RoundingMode.HALF_UP);
            }
            bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + value_from_obj + " -> " + value_from);

            // nominalna vrijednost DO
            currentCellReference = new CellReference(row.getRowNum(), 2);
            Object value_to_obj = getCellValue(row.getCell(2));
            if (value_to_obj != null && !(value_to_obj instanceof BigDecimal))
            {
                error("Greska kod dohvata nominalne vrijednosti DO - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + value_to_obj + "' sto nije valjani broj!", null);
            }
            else
            {
                value_to = (BigDecimal)value_to_obj;
                if (value_to == null) value_to = maxValue; else value_to.setScale(2, RoundingMode.HALF_UP);
            }
            bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + value_to_obj + " -> " + value_to);


            // koeficijenti promjene
            for (int columnIndex = range.getFirstColumn(); columnIndex <= range.getLastColumn(); columnIndex++)
            {
                BigDecimal koef_rev = null;

                currentCellReference = new CellReference(row.getRowNum(), columnIndex);
                Object koef_rev_obj = getCellValue(row.getCell(columnIndex));
                if (!(koef_rev_obj instanceof BigDecimal))
                {
                    error("Greska kod dohvata koeficijenta promjene - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + koef_rev_obj + "' sto nije valjani broj!", null);
                }
                else
                {
                    koef_rev = ((BigDecimal)koef_rev_obj).setScale(2, RoundingMode.HALF_UP);
                }
                bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + koef_rev_obj + " -> " + koef_rev);

                RevaKoefData data = new RevaKoefData();
                data.collateralType = type;
                data.age_from = ages[columnIndex - range.getFirstColumn()] + 1;
                data.age_to = ages[columnIndex - range.getFirstColumn() + 1];
                data.value_from = value_from;
                data.value_to = value_to;
                data.koef_rev = koef_rev;
                coefficients.add(data);
                bc.debug("KOEFICIJENT = " + data);
            }
        }
        
        currentSheet = null;
        currentCellReference = null;

        bc.stopStopWatch("BO910.parseVehicleSheet");
    }
    
    
    /**
     * Metoda parsira sheet za nekretnine.
     * @param sheet sheet koji se parsira
     * @param range raspon æelija
     */
    private void parseRealEstateSheet(Sheet sheet, CellRangeAddress range) throws Exception
    {
        if (sheet == null || range == null) return;

        bc.startStopWatch("BO910.parseRealEstateSheet");
        
        currentSheet = sheet;
        bc.debug("Pocinje parsiranje sheeta '" + sheet.getSheetName() + "'...");

        // godine procjene
        Row topRow = sheet.getRow(1);
        Integer[] est_years = new Integer[range.getLastColumn() - range.getFirstColumn() + 1];
        for (int columnIndex = range.getFirstColumn(); columnIndex <= range.getLastColumn(); columnIndex++)
        {
            currentCellReference = new CellReference(topRow.getRowNum(), columnIndex);
            Integer est_year = 0;
            Object est_year_obj = getCellValue(topRow.getCell(columnIndex));
            if (!(est_year_obj instanceof BigDecimal))
            {
                error("Greska kod dohvata godine procjene - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + est_year_obj + "' sto nije valjani broj!", null);
            }
            else
            {
                est_year = ((BigDecimal)est_year_obj).intValue();
            }
            bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + est_year_obj + " -> " + est_year);
            est_years[columnIndex - range.getFirstColumn()] = est_year;
        }
        bc.debug("Godine procjene = " + Arrays.toString(est_years));
        
        
        // koeficijenti
        for (int rowIndex = range.getFirstRow(); rowIndex <= range.getLastRow(); rowIndex++)
        {
            Row row = sheet.getRow(rowIndex);

            TypeData type = null;
            LocationData location = null;

            // tip kolaterala
            currentCellReference = new CellReference(row.getRowNum(), 0);
            Object col_gcm_typ_name_obj = getCellValue(row.getCell(0));
            if (!(col_gcm_typ_name_obj instanceof String))
            {
                error("Greska kod dohvata tipa kolaterala - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + col_gcm_typ_name_obj + "' sto nije valjana vrijednost! Celija mora sadrzavati naziv tipa kolaterala za revalorizaciju!", null);
            }
            else
            {
                String col_gcm_typ_name = (String)col_gcm_typ_name_obj;
                type = collateralTypes.get(col_gcm_typ_name);
                if (type == null)
                {
                    error("Greska kod dohvata tipa kolaterala - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + col_gcm_typ_name_obj + "' sto nije valjani tip kolaterala za revalorizaciju!", null);
                }
            }
            bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + col_gcm_typ_name_obj + " -> " + type);

            // lokacija (katastarska opæina, grad, županija)
            currentCellReference = new CellReference(row.getRowNum(), 1);
            Object location_code_obj = getCellValue(row.getCell(1));
            if (!(location_code_obj instanceof String) && !(location_code_obj instanceof BigDecimal))
            {
                error("Greska kod dohvata sifre lokacije - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + location_code_obj + "' sto nije valjana sifra!", null);
            }
            else
            {
                String location_code = null;
                if (location_code_obj instanceof BigDecimal) location_code = "" + ((BigDecimal)location_code_obj).intValue();
                else location_code = (String)location_code_obj;

                if (sheet == sheetCadastralMunicipalities)
                {
                   location = cadastralMunicipalities.get(location_code);
                   if (location == null) error("Greska kod dohvata katastarske opcine - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + location_code + "' sto nije valjana sifra katastarske opcine!", null);
                }
                else if (sheet == sheetCities)
                {
                   location = cities.get(location_code);
                   if (location == null) error("Greska kod dohvata grada - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + location_code + "' sto nije valjana sifra grada!", null);
                }
                else if (sheet == sheetCounties)
                {
                   location = counties.get(location_code);
                   if (location == null) error("Greska kod dohvata grada - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + location_code + "' sto nije valjana sifra zupanije!", null);
                }
            }
            bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + location_code_obj + " -> " + location);


            // koeficijenti promjene
            for (int columnIndex = range.getFirstColumn(); columnIndex <= range.getLastColumn(); columnIndex++)
            {
                BigDecimal koef_rev = null;

                currentCellReference = new CellReference(row.getRowNum(), columnIndex);
                Object koef_rev_obj = getCellValue(row.getCell(columnIndex));
                if (!(koef_rev_obj instanceof BigDecimal))
                {
                    error("Greska kod dohvata koeficijenta promjene - celija " + currentCellReference.formatAsString() + " u sheetu '" + sheet.getSheetName() + "' ima vrijednost '" + koef_rev_obj + "' sto nije valjani broj!", null);
                }
                else
                {
                    koef_rev = ((BigDecimal)koef_rev_obj).setScale(3, RoundingMode.HALF_UP);
                }
                bc.debug("'" + sheet.getSheetName() + "'!" + currentCellReference.formatAsString() + " = " + koef_rev_obj + " -> " + koef_rev);

                RevaKoefData data = new RevaKoefData();
                data.collateralType = type;
                data.est_year = est_years[columnIndex - range.getFirstColumn()];
                if (sheet == sheetCadastralMunicipalities) data.cadastralMunicipality = location;
                else if (sheet == sheetCities) data.city = location;
                else if (sheet == sheetCounties) data.county = location;
                data.koef_rev = koef_rev;
                coefficients.add(data);
                bc.debug("KOEFICIJENT = " + data);
            }
        }
        
        currentSheet = null;
        currentCellReference = null;

        bc.stopStopWatch("BO910.parseRealEstateSheet");
    }
    
    
    /**
     * Metoda vraæa raspon æelija u kojima se nalaze koeficijenti.
     * @param sheet sheet u kojem su koeficijenti
     * @param startingRowIndex broj reda u kojem poèinju koeficijenti
     * @param startingColumnIndex broj kolone u kojem poèinju koeficijenti
     * @return raspon æelija
     */
    private CellRangeAddress getCoefficientCellRange(Sheet sheet, int startingRowIndex, int startingColumnIndex) throws Exception
    {
        bc.startStopWatch("BO910.getCoefficientCellRange");

        // ide po prvom redu s koeficijentima dok ne doðe do prazne æelije
        int columnIndex = startingColumnIndex;
        for (;;)
        {
            Row row = sheet.getRow(startingRowIndex);
            if (row == null) break;
            Cell cell = row.getCell(columnIndex);
            if (cell == null) break;
            Object value = getCellValue(cell);
            if (value == null || value.equals("")) break;
            columnIndex++;
        }

        // ide po prvoj koloni s koeficijentima dok ne doðe do prazne æelije
        int rowIndex = startingRowIndex;
        for (;;)
        {
            Row row = sheet.getRow(rowIndex);
            if (row == null) break;
            Cell cell = row.getCell(startingColumnIndex);
            if (cell == null) break;
            Object value = getCellValue(cell);
            if (value == null || value.equals("")) break;
            rowIndex++;
        }

        bc.stopStopWatch("BO910.getCoefficientCellRange");

        if (rowIndex - 1 < startingRowIndex || columnIndex - 1 < startingColumnIndex) return null;
        else return new CellRangeAddress(startingRowIndex, rowIndex - 1, startingColumnIndex, columnIndex - 1);
    }
    
    
    /**
     * Metoda èita sve aktivne koeficijente promjene iz baze podataka, zapisuje ih u CSV i šalje na mail korisniku.
     */
    private boolean makeReportAndSendToMail() throws Exception
    {
        try
        {
            bc.startStopWatch("BO910.makeReportAndSendToMail");
            
            String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
            String reportFileName = bc.getOutDir() + "/" + "UcitaniKoeficijenti" + dateString + ".csv";
            OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(reportFileName)), "Cp1250");
            
            RevaKoefIterator iter = bo911.selectRevalorizationCoefficients();
            
            while (iter.next())
            {
                String line = "";
                
                if (iter.est_year() != null)
                {
                    String type = "", code = "", name = "";
                    if (iter.county_code() != null)
                    {
                        type = iter.county_type().trim().toUpperCase();
                        code = iter.county_code().trim().toUpperCase();
                        name = iter.county_name().trim().toUpperCase();
                    }
                    else if (iter.city_code() != null)
                    {
                        type = iter.city_type().trim().toUpperCase();
                        code = iter.city_code().trim().toUpperCase();
                        name = iter.city_name().trim().toUpperCase();
                    }
                    else if (iter.cada_code() != null)
                    {
                        type = iter.cada_type().trim().toUpperCase();
                        code = iter.cada_code().trim().toUpperCase();
                        name = iter.cada_name().trim().toUpperCase();
                    }
                    line = String.format("%1$s;%2$s;%3$s;%4$s;%5$s;%6$s\n", iter.col_gcm_type_name(), type, code, name, iter.est_year(), iter.koef_rev());
                }
                else
                {
                    line = String.format("%1$s;%2$s;%3$s;%4$s;%5$s;%6$s\n", iter.col_gcm_type_name(), iter.value_from(), iter.value_to(), iter.age_from(), iter.age_to(), iter.koef_rev());
                }
                
                streamWriter.write(line);
            }
    
            iter.close();
    
            streamWriter.flush();
            streamWriter.close();
    
            YXY70.send(bc, "csvbo91", bc.getLogin(), reportFileName);
            
            info("Izvjestaj s ucitanim koeficijentima promjene je kreiran i poslan na mail korisniku.");
            return true;
        }
        catch (Exception ex)
        {
            warn("Izvjestaj nije poslan na mail.", ex);
            return false;
        }
        finally
        {
            bc.stopStopWatch("BO910.makeReportAndSendToMail");
        }
    }


    /**
     * Metoda uèitava mapirane tipove kolaterala za revalorizaciju.
     */
    private void loadCollateralTypes() throws Exception
    {
        bc.startStopWatch("BO910.loadCollateralTypes");
        bc.debug("Mapirani tipovi kolaterala:");

        CollateralTypeIterator iter = bo911.selectCollateralTypes();
        while (iter.next())
        {
            String name = iter.name().trim().toUpperCase();
            BigDecimal id = iter.col_gcm_typ_id();
            TypeData type = new TypeData(id, name);
            collateralTypes.put(name, type);
            bc.debug("   " + type.name + " (" + type.id + ")");
        }
        iter.close();

        bc.stopStopWatch("BO910.loadCollateralTypes");
    }


    /**
     * Metoda uèitava sve katastarske opæine.
     */
    private void loadCadastralMunicipalities() throws Exception
    {
        bc.startStopWatch("BO910.loadCadastralMunicipalities");
        bc.debug("Katastarske opcine: ");

        LocationIterator iter = bo911.selectCadastralMunicipalities();
        while (iter.next())
        {
            BigDecimal id = iter.id();
            String code = iter.code().trim().toUpperCase();
            String name = iter.name().trim().toUpperCase();
            LocationData location = new LocationData(id, code, name, "Katastarska opcina");
            cadastralMunicipalities.put(location.code, location);
            bc.debug("   " + location.code + " " + location.name + " (" + location.id + ")");
        }
        iter.close();

        bc.stopStopWatch("BO910.loadCadastralMunicipalities");
    }


    /**
     * Metoda uèitava sva mjesta.
     */
    private void loadCities() throws Exception
    {
        bc.startStopWatch("BO910.loadCities");
        bc.debug("Mjesta: ");

        LocationIterator iter = bo911.selectCities();
        while (iter.next())
        {
            BigDecimal id = iter.id();
            String code = iter.code().trim().toUpperCase();
            String name = iter.name().trim().toUpperCase();
            LocationData location = new LocationData(id, code, name, "Grad");
            cities.put(location.code, location);
            bc.debug("   " + location.code + " " + location.name + " (" + location.id + ")");
        }
        iter.close();

        bc.stopStopWatch("BO910.loadCities");
    }


    /**
     * Metoda uèitava sva mjesta.
     */
    private void loadCounties() throws Exception
    {
        bc.startStopWatch("BO910.loadCounties");
        bc.debug("Zupanije: ");

        LocationIterator iter = bo911.selectCounties();
        while (iter.next())
        {
            BigDecimal id = iter.id();
            String code = iter.code().trim().toUpperCase();
            String name = iter.name().trim().toUpperCase();
            LocationData location = new LocationData(id, code, name, "Zupanija");
            counties.put(location.code, location);
            bc.debug("   " + location.code + " " + location.name + " (" + location.id + ")");
        }
        iter.close();

        bc.stopStopWatch("BO910.loadCounties");
    }



    /**
     * Metoda otvara i vraæa workbook iz zadane ulazne datoteke.
     * @param inputFileName Puna putanja do ulazne datoteke.
     * @return workbook
     */
    private Workbook getWorkbook(String inputFileName) throws Exception
    {
        bc.startStopWatch("BO910.getWorkbook");

        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(inputFileName);
            Workbook workbook = WorkbookFactory.create(inputStream);
            return workbook;
        }
        catch (Exception ex)
        {
            error("Doslo je do greske kod otvaranja ulazne datoteke " + inputFileName + "!", ex);
            throw ex;
        }
        finally
        {
            if (inputStream != null) inputStream.close();  // jednom kad je workbook uèitan, InputStream više nije potreban
            bc.stopStopWatch("BO910.getWorkbook");
        }
    }


    /**
     * Metoda dohvaæa naziv ulazne datoteke bez putanje. 
     * Ako postoji više datoteka u ulaznom direktoriju, uzet æe se prva na koju se naiðe.
     * Ulazna datoteka prepoznaje se po tome što u istom direktoriju ima pripadajuæu marker datoteku.
     * @param inputDirectoryName putanja do ulaznog direktorija
     * @return naziv ulazne datoteke bez putanje
     */
    private String getInputFileName(String inputDirectoryName) throws Exception
    {
        bc.startStopWatch("BO910.getInputFileName");
        
        try
        {
           File inputDirectoryFile = new File(inputDirectoryName);
           for (String fileName : inputDirectoryFile.list())
           {
               if (fileName.endsWith(".marker"))
               {
                   return fileName.replace(".marker", "");
               }
           }
           throw new Exception("U ulaznom direktoriju " + inputDirectoryName + " nema nijedne datoteke s markerom!");
        }
        catch (Exception ex)
        {
            error("Doslo je do greske kod dohvata ulazne datoteke!", ex);
            throw ex;
        }
        finally
        {
            bc.stopStopWatch("BO910.getInputFileName");
        }
    }


    /**
     * Metoda dohvaæa sadržaj zadane æelije.
     * @param cell æelija iz koje se èita vrijednost
     * @return objekt koji sadrži vrijednost æelije.
     */
    private Object getCellValue(Cell cell)
    {
        if (cell == null) return null;

        switch (cell.getCellType())
        {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString().trim().toUpperCase();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) return cell.getDateCellValue();
                else return BigDecimal.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return null;
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
            bc.startStopWatch("BO910.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6546852704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Ucitavanje koeficijenata promjene za revalorizaciju");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO910.insertIntoEvent");
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
     *    <dt>input_directory_name</dt><dd>Puna putanja do direktorija u kojem je smještena ulazna datoteka.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Ulazni direktorij" };

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
            bc.setBankSign(bank_sign);
            
            // dohvati putanju ulaznog direktorija
            this.inputDirectoryName = bc.getArg(1);
            
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
        hasError = true;
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
        BatchParameters bp = new BatchParameters(new BigDecimal("6546829704"));
        bp.setArgs(args);
        new BO910().run(bp);
    }
}