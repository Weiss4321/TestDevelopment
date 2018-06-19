package hr.vestigo.modules.collateral.batch.bo92;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo92.BO921.RealEstateIterator;
import hr.vestigo.modules.collateral.batch.bo92.BO921.RealEstateReportDataIterator;
import hr.vestigo.modules.collateral.batch.bo92.BO921.RevaExceptionIterator;
import hr.vestigo.modules.collateral.batch.bo92.BO921.RevaKoefIterator;
import hr.vestigo.modules.collateral.batch.bo92.BO921.VehicleIterator;
import hr.vestigo.modules.collateral.batch.bo92.BO921.VehicleReportDataIterator;
import hr.vestigo.modules.collateral.common.yoyM.YOYM0;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Obrada za izraèun pomoæne tržišne vrijednosti za revalorizaciju
 * @author hrakis
 */
public class BO920 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo92/BO920.java,v 1.3 2015/01/29 15:04:52 hrakis Exp $";

    private BatchContext bc;
    private BO921 bo921;

    /** Ulazni parametar - Vrsta komitenta - može biti CO ili RET. */
    private String customer_type;
    
    /** Ulazni parametar - Datum tržišne vrijednosti OD. */
    private Date date_from;

    /** Ulazni parametar - Datum tržišne vrijednosti DO. */
    private Date date_until;

    /** Da li obrada samo generira izvještaj. */
    private boolean only_make_report = false;
    
    
    /** Svi važeæi koeficijenti za revalorizaciju. */
    private ArrayList<RevaKoefData> coefficients = new ArrayList<RevaKoefData>();
    
    /** Lista ID-eva kolaterala koji ne ulaze u revalorizaciju. */
    private ArrayList<BigDecimal> exceptions = new ArrayList<BigDecimal>();
    
    /** Lista kolaterala koji su revalorizirani. */
    private ArrayList<CollateralData> collaterals = new ArrayList<CollateralData>();
    
    private Date currentDate;
    private YRXX0 yrxx0;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
    private final BigDecimal eur_cur_id = new BigDecimal("64999");
    private final String padStr = "...";
    private final String falseStr = " -> kolateral ne ulazi u revalorizaciju.";


    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo921 = new BO921(bc);
        this.yrxx0 = new YRXX0(bc);

        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat trenutnog datuma
        this.currentDate = new Date(bc.getExecStartTime().getTime());
        info("Datum revalorizacije = " + dateFormat.format(currentDate));
        
        // uèitavanje koeficijenata promjene
        loadRevalorizationCoefficients();
        
        // uèitavanje popisa kolaterala koji su iskljuèeni iz revalorizacije
        loadRevalorizationExceptions();
        
        if (!only_make_report)
        {
            try
            {
                // uèitavanje vozila i izraèun revalorizacije
                processVehicles();

                // uèitavanje nekretnina i izraèun revalorizacije
                processRealEstates();

                bc.beginTransaction();

                // evidentiranje obrade za kolaterale
                BigDecimal col_pro_id = bo921.insertIntoColProc();
                bc.debug("COL_PRO_ID = " + col_pro_id);

                // deaktiviranje postojeæeg izraèuna
                bo921.deactivateCollReva();

                // zapisivanje izraèunatih vrijednosti
                for (CollateralData collateral : collaterals)
                {
                    bo921.insertIntoCollReva(collateral, col_pro_id);
                }

                // evidentiranje završetka obrade i broja obraðenih zapisa
                bo921.updateColProc(col_pro_id, collaterals.size());

                // evidentiranje datuma izraèuna revalorizacije
                bo921.updateLastDate();

                bc.commitTransaction();
                info("Napravljen je izracun revalorizacije za " + collaterals.size() + " kolaterala. Vrijednosti su spremljene u bazu podataka.");
            }
            catch (Exception ex)
            {
                error("Dogodila se nepredvidjena greska kod izracuna revalorizacije. Izracun nije spremljen u bazu podataka. Postojeci izracun (ako postoji) u bazi podataka ostaje vazeci.", ex);
                bc.rollbackTransaction();
                throw ex;
            }
        }

        makeReportAndSendToMail();

        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /** 
     * Metoda uèitava revalorizacijske koeficijente. 
     */
    private void loadRevalorizationCoefficients() throws Exception
    {
        RevaKoefIterator iter = bo921.selectRevalorizationCoefficients();
        while (iter.next())
        {
            RevaKoefData coefficient = new RevaKoefData();
            coefficient.rev_koe_id = iter.rev_koe_id();
            coefficient.col_gcm_typ_id = iter.col_gcm_typ_id();
            coefficient.col_gcm_type_name = iter.col_gcm_type_name();
            if (iter.county_id() != null) coefficient.county = new LocationData(iter.county_id(), iter.county_code(), iter.county_name(), iter.county_type());
            if (iter.city_id() != null) coefficient.city = new LocationData(iter.city_id(), iter.city_code(), iter.city_name(), iter.city_type());
            if (iter.cada_id() != null) coefficient.cadastral = new LocationData(iter.cada_id(), iter.cada_code(), iter.cada_name(), iter.cada_type());
            coefficient.est_year = iter.est_year();
            coefficient.value_from = iter.value_from();
            coefficient.value_to = iter.value_to();
            coefficient.age_from = iter.age_from();
            coefficient.age_to = iter.age_to();
            coefficient.koef_rev = iter.koef_rev();
            
            coefficients.add(coefficient);
        }
        iter.close();
        info("Ucitano je " + coefficients.size() + " koeficijenata.");
    }
    
    
    /**
     * Metoda uèitava popis kolaterala koji su na listi iskljuèenih iz revalorizacije.
     */
    private void loadRevalorizationExceptions() throws Exception
    {
        RevaExceptionIterator iter = bo921.selectRevalorizationExceptions();
        while(iter.next())
        {
            exceptions.add(iter.col_hea_id());
        }
        iter.close();
        
        info("Na listi kolaterala iskljucenih iz revalorizacije nalazi se " + exceptions.size() + " kolaterala.");
    }
    
    
    /**
     * Metoda uèitava sve potrebne podatke o vozilima koja ulaze u revalorizaciju i za njih vrši izraèun revalorizacije.
     */
    private void processVehicles() throws Exception
    {
        // inicijalizacija mapiranja za revalorizaciju
        YOYM0 mappingVehicles = new YOYM0(bc, "reva_typ_vehicle", currentDate);
        
        VehicleIterator iter = bo921.selectVehicles();
        while (iter.next())
        {
            info("Vozilo " + iter.col_num().trim());
            
            VehicleData vehicle = getVehicleDataFromIterator(iter);

            // mapiranje vozila
            vehicle.gcmType = mappingVehicles.resolve(vehicle.col_cat_id, vehicle.col_typ_id, vehicle.col_sub_id);
            if (vehicle.gcmType == null)
            {
                info(padStr + "nije moguce mapirati kolateral na nijedan tip za revalorizaciju (kategorija=" + vehicle.col_cat_name + ",vrsta=" + vehicle.col_typ_name + ",podvrsta=" + vehicle.col_sub_name + ")" + falseStr); 
                continue;
            }
            
            // provjera uvjeta
            if (!isCollateralAcceptable(vehicle)) continue;
            
            // naði odgovarajuæi koeficijent
            vehicle.coefficient = findVehicleCoefficient(vehicle);
            if (vehicle.coefficient == null) continue;
            
            // izraèunaj novu nominalnu vrijednost
            vehicle.new_nomi_value = vehicle.est_value.multiply(vehicle.coefficient.koef_rev).setScale(2, RoundingMode.HALF_UP);
            info("-> nova vrijednost = " + decimalFormat.format(vehicle.est_value) + " " + vehicle.nomi_value_cur_code + " x " + vehicle.coefficient.koef_rev + " = " + decimalFormat.format(vehicle.new_nomi_value) + " " + vehicle.nomi_value_cur_code);
            
            collaterals.add(vehicle);
        }
        iter.close();
    }
    
    
    /**
     * Metoda uèitava sve potrebne podatke o nekretninama koja ulaze u revalorizaciju i za njih vrši izraèun revalorizacije.
     */
    private void processRealEstates() throws Exception
    {
        // inicijalizacija mapiranja za revalorizaciju
        YOYM0 mappingRealEstates = new YOYM0(bc, "reva_typ_realest", currentDate);
        
        RealEstateIterator iter = bo921.selectRealEstates();
        while (iter.next())
        {
            info("Nekretnina " + iter.col_num().trim());

            RealEstateData realEstate = getRealEstateDataFromIterator(iter);

            // mapiranje nekretnine
            realEstate.gcmType = mappingRealEstates.resolve(realEstate.col_cat_id, realEstate.col_typ_id, realEstate.col_sub_id);
            if (realEstate.gcmType == null)
            {
                info(padStr + "nije moguce mapirati kolateral na nijedan tip za revalorizaciju (kategorija=" + realEstate.col_cat_name + ",vrsta=" + realEstate.col_typ_name + ",podvrsta=" + realEstate.col_sub_name + ")" + falseStr);
                continue;
            }
            
            // provjera uvjeta
            if (!isCollateralAcceptable(realEstate)) continue;
            
            // naði odgovarajuæi koeficijent
            realEstate.coefficient = findRealEstateCoefficient(realEstate);
            if (realEstate.coefficient == null) continue;
            
            // izraèunaj novu nominalnu vrijednost
            realEstate.new_nomi_value = realEstate.nomi_value.multiply(realEstate.coefficient.koef_rev).setScale(2, RoundingMode.HALF_UP);
            info("-> nova vrijednost = " + decimalFormat.format(realEstate.nomi_value) + " " + realEstate.nomi_value_cur_code + " x " + realEstate.coefficient.koef_rev + " = " + decimalFormat.format(realEstate.new_nomi_value) + " " + realEstate.nomi_value_cur_code);
            
            collaterals.add(realEstate);
        }
        iter.close();
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
        vehicle.financial_flag = iter.financial_flag();
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
        vehicle.nomi_value_eur = yrxx0.exchange(vehicle.nomi_value, vehicle.nomi_value_cur_id, eur_cur_id, currentDate);
        vehicle.est_value = iter.est_value();
        vehicle.est_value_eur = yrxx0.exchange(vehicle.est_value, vehicle.nomi_value_cur_id, eur_cur_id, currentDate);
        
        vehicle.est_date = iter.est_date();
        if (vehicle.est_date != null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(vehicle.est_date);
            vehicle.est_year = calendar.get(Calendar.YEAR);
            vehicle.age = numberOfMonthsBetween(currentDate, vehicle.est_date);
        }
        
        vehicle.veh_made_year = iter.veh_made_year();
        
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
        realEstate.financial_flag = iter.financial_flag();
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
        realEstate.nomi_value_eur = yrxx0.exchange(realEstate.nomi_value, realEstate.nomi_value_cur_id, eur_cur_id, currentDate);
        realEstate.est_date = iter.est_date();
        if (realEstate.est_date != null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(realEstate.est_date);
            realEstate.est_year = calendar.get(Calendar.YEAR);
            realEstate.age = numberOfMonthsBetween(currentDate, realEstate.est_date);
        }
        realEstate.cadastral = new LocationData(iter.cada_id(), iter.cada_code(), iter.cada_name(), "");
        realEstate.city = new LocationData(iter.city_id(), iter.city_code(), iter.city_name(), "");
        realEstate.county = new LocationData(iter.county_id(), iter.county_code(), iter.county_name(), "");
        return realEstate;
    }
    
    
    /**
     * Metoda provjerava da li zadani kolateral zadovoljava uvjete da bi ušao u izraèun revalorizacije.
     * @param collateral kolateral
     * @return true ako kolateral zadovoljava uvjete, false inaèe
     */
    private boolean isCollateralAcceptable(CollateralData collateral) throws Exception
    {
        // provjera statusa kolaterala
        if (!collateral.collateral_status.equals("3") &&
            !(collateral.collateral_status.equals("0") && collateral.financial_flag.equals("1")) &&
            !(collateral.collateral_status.equals("1") && collateral.financial_flag.equals("1")))
        {
            info(padStr + "kolateral je na listi " + collateral.collateral_status + " i " + ("1".equals(collateral.financial_flag) ? "knjizen je" : "NIJE KNJIZEN") + falseStr);
            return false;
        }
        
        // provjera da li je kolateral na listi kolaterala iskljuèenih iz revalorizacije
        if (exceptions.contains(collateral.col_hea_id))
        {
           info(padStr + "kolateral je na listi iskljucenih" + falseStr);
           return false;
        }
        
        // provjera datuma tržišne vrijednosti
        if (!(collateral.nomi_value_date.compareTo(date_from) >= 0 && collateral.nomi_value_date.compareTo(date_until) <= 0))
        {
            info(padStr + "datum trzisne vrijednosti kolaterala je " + dateFormat.format(collateral.nomi_value_date) + " sto je izvan raspona definiranog parametrima obrade (" + dateFormat.format(date_from) + " - " + dateFormat.format(date_until) + ")" + falseStr);
            return false;
        }
        
        // provjera datuma procjene
        if (collateral.est_date == null)
        {
            info(padStr + "datum procjene nije popunjen" + falseStr);
            return false;
        }
        
        // provjera CO / RET
        int numberOfCoPlacements = bo921.selectNumberOfCorporatePlacements(collateral.col_hea_id);
        if (numberOfCoPlacements > 0 && customer_type.equalsIgnoreCase("RET"))
        {
            info(padStr + "kolateral ima barem jedan povezan Corporate plasman (konkretno " + numberOfCoPlacements + "), a revalorizacija se radi za RET" + falseStr);
            return false;
        }
        if (numberOfCoPlacements <= 0 && customer_type.equalsIgnoreCase("CO"))
        {
            info(padStr + "kolateral nema povezanih Corporate plasmana, a revalorizacija se radi za CO" + falseStr);
            return false;
        }

        return true;
    }
   

    /**
     * Metoda traži odgovarajuæi revalorizacijski koeficijent za zadano vozilo.
     * @param collateral objekt s podacima o vozilu
     * @return objekt s podacima o revalorizacijskom koeficijentu ako odgovarajuæi koeficijent postoji, inaèe null
     */
    private RevaKoefData findVehicleCoefficient(VehicleData collateral) throws Exception
    {
        try
        {
            bc.startStopWatch("BO920.findVehicleCoefficient");
            
            // provjeri da li postoji barem jedan koeficijent za mapirani tip kolaterala
            boolean found = false;
            for (RevaKoefData coef : coefficients)
            {
                if (collateral.gcmType.col_gcm_typ_id.equals(coef.col_gcm_typ_id))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                info(padStr + "ne postoji nijedan koeficijent definiran za tip " + collateral.gcmType.name + falseStr); 
                return null;
            }
            
            // naði koeficijent za vozilo
            for (RevaKoefData coef : coefficients)
            {
                if (collateral.gcmType.col_gcm_typ_id.equals(coef.col_gcm_typ_id) && 
                    collateral.est_value_eur.compareTo(coef.value_from) >= 0 && collateral.est_value_eur.compareTo(coef.value_to) <= 0 && 
                    collateral.age.compareTo(coef.age_from) >= 0 && collateral.age.compareTo(coef.age_to) <= 0)
                {
                    info(padStr + "pronadjen je koeficijent = " + coef.koef_rev + " definiran za: tip " + collateral.gcmType.name + ", proc.vrijednost = " + collateral.est_value_eur + " EUR, proteklo vrijeme = " + collateral.age + " mj.");
                    return coef;
                }
            }
            
            info(padStr + "nije pronadjen koeficijent za: tip " + collateral.gcmType.name + ", proc.vrijednost = " + collateral.est_value_eur + " EUR, proteklo vrijeme = " + collateral.age + " mj." + falseStr);
            return null;
        }
        catch (Exception ex)
        {
            error("Doslo je do nepredvidjene greske kod trazenja odgovarajuceg koeficijenta za vozilo!", ex);
            throw ex;
        }
        finally
        {
            bc.stopStopWatch("BO920.findVehicleCoefficient");
        }
    }
    
    
    /**
     * Metoda traži odgovarajuæi revalorizacijski koeficijent za zadanu nekretninu.
     * @param collateral objekt s podacima o nekretnini
     * @return objekt s podacima o revalorizacijskom koeficijentu ako odgovarajuæi koeficijent postoji, inaèe null
     */
    private RevaKoefData findRealEstateCoefficient(RealEstateData collateral) throws Exception
    {
        try
        {
            bc.startStopWatch("BO920.findRealEstateCoefficient");

            // provjeri da li postoji barem jedan koeficijent za mapirani tip kolaterala
            boolean found = false;
            for (RevaKoefData coef : coefficients)
            {
                if (collateral.gcmType.col_gcm_typ_id.equals(coef.col_gcm_typ_id))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                info(padStr + "ne postoji nijedan koeficijent definiran za tip " + collateral.gcmType.name + falseStr);
                return null;
            }
            
            // provjeri da li postoji barem jedan koeficijent za godinu procjene
            found = false;
            for (RevaKoefData coef : coefficients)
            {
                if (coef.est_year != null && collateral.est_year == coef.est_year)
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                info(padStr + "ne postoji nijedan koeficijent definiran za godinu procjene " + collateral.est_year + falseStr);
                return null;
            }
            
            // naði koeficijent za katastarsku opæinu
            for (RevaKoefData coef : coefficients)
            {
                if (collateral.gcmType.col_gcm_typ_id.equals(coef.col_gcm_typ_id) && coef.cadastral != null && collateral.cadastral.id.equals(coef.cadastral.id) && collateral.est_year == coef.est_year)
                {
                    info(padStr + "pronadjen je koeficijent = " + coef.koef_rev + " definiran za: tip " + collateral.gcmType.name + ", katastarska opcina = " + collateral.cadastral.name + " (" + collateral.cadastral.code + "), godina procjene = " + collateral.est_year);
                    return coef;
                }
            }
            info(padStr + "nije pronadjen koeficijent za: tip " + collateral.gcmType.name + ", katastarska opcina = " + collateral.cadastral.name + " (" + collateral.cadastral.code + "), godina procjene = " + collateral.est_year);
    
            // naði koeficijent za grad
            for (RevaKoefData coef : coefficients)
            {
                if (collateral.gcmType.col_gcm_typ_id.equals(coef.col_gcm_typ_id) && coef.city != null && collateral.city.id.equals(coef.city.id) && collateral.est_year == coef.est_year)
                {
                    info(padStr + "pronadjen je koeficijent = " + coef.koef_rev + " definiran za: tip " + collateral.gcmType.name + ", mjesto = " + collateral.city.name + " (" + collateral.city.code + "), godina procjene = " + collateral.est_year);
                    return coef;
                }
            }
            info(padStr + "nije pronadjen koeficijent za: tip " + collateral.gcmType.name + ", mjesto = " + collateral.city.name + " (" + collateral.city.code + "), godina procjene = " + collateral.est_year);
    
            // naði koeficijent za županiju
            for (RevaKoefData coef : coefficients)
            {
                if (collateral.gcmType.col_gcm_typ_id.equals(coef.col_gcm_typ_id) && coef.county != null && collateral.county.id.equals(coef.county.id) && collateral.est_year == coef.est_year)
                {
                    info(padStr + "pronadjen je koeficijent = " + coef.koef_rev + " definiran za: tip " + collateral.gcmType.name + ", zupanija = " + collateral.county.name + " (" + collateral.county.code + "), godina procjene = " + collateral.est_year);
                    return coef;
                }
            }
            
            info(padStr + "nije pronadjen koeficijent za: tip " + collateral.gcmType.name + ", zupanija = " + collateral.county.name + " (" + collateral.county.code + "), godina procjene = " + collateral.est_year + falseStr);
            return null;
        }
        catch (Exception ex)
        {
            error("Doslo je do nepredvidjene greske kod trazenja odgovarajuceg koeficijenta za nekretninu!", ex);
            throw ex;
        }
        finally
        {
            bc.stopStopWatch("BO920.findRealEstateCoefficient");
        }
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
            String fileName = bc.getOutDir() + "/" + "PomocniIzracunRevalorizacije" + dateString + ".xlsx";
            
            // kreiraj workbook
            workbook = new SXSSFWorkbook(100);
            ExcelStyleData styles = ExcelStyleData.createStyles(workbook);
            
            // kreiraj sheetove
            Sheet sheetRealEstates = workbook.createSheet("Nekretnine");
            Sheet sheetVehicles = workbook.createSheet("Vozila");
            Sheet sheetCoefficients = workbook.createSheet("Koeficijenti");
            
            // podesi širinu kolona
            ExcelUtils.setColumnWidths(sheetRealEstates, new int[] { 50, 65, 35, 120, 75, 95, 45, 75, 210, 230, 190, 175, 145, 95 });
            ExcelUtils.setColumnWidths(sheetVehicles, new int[] { 50, 65, 35, 120, 75, 95, 95, 45, 75, 210, 260, 65, 95 });
            ExcelUtils.setColumnWidths(sheetCoefficients, new int[] { 110, 150, 120, 195, 45, 55 });
            
            // zapiši podatke u sheetove
            writeRealEstateReport(sheetRealEstates, styles);
            writeVehicleReport(sheetVehicles, styles);
            writeCoefficientReport(sheetCoefficients, styles);
    
            // spremi izvještaj
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            dispose(workbook);
            new File(fileName + ".marker").createNewFile();
    
            // pošalji izvještaj na mail
            YXY70.send(bc, "csvbo92", bc.getLogin(), fileName);
            
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
        ExcelUtils.createCell(row, columnIndex++, "Pomo\u0107na TV", styles.headingStyle);
        rowIndex++;
        
        sheet.createFreezePane(0, rowIndex);

        // dohvati i zapiši podatke
        RealEstateReportDataIterator iter = bo921.selectReportDataForRealEstates();
        while (iter.next())
        {
            columnIndex = 0;
            row = sheet.createRow(rowIndex);
            String customerType = bo921.selectNumberOfCorporatePlacements(iter.col_hea_id()) > 0 ? "CO" : "RET";
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
            ExcelUtils.createCell(row, columnIndex++, iter.new_value(), styles.normalNumericStyle);
            rowIndex++;
        }
        iter.close();
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
        ExcelUtils.createCell(row, columnIndex++, "Pomo\u0107na TV", styles.headingStyle);
        rowIndex++;
        
        sheet.createFreezePane(0, rowIndex);

        // dohvati i zapiši podatke
        VehicleReportDataIterator iter = bo921.selectReportDataForVehicles();
        while (iter.next())
        {
            columnIndex = 0;
            row = sheet.createRow(rowIndex);
            String customerType = bo921.selectNumberOfCorporatePlacements(iter.col_hea_id()) > 0 ? "CO" : "RET";
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
            ExcelUtils.createCell(row, columnIndex++, iter.new_value(), styles.normalNumericStyle);
            rowIndex++;
        }
        iter.close();
    }


    /**
     * Metoda zapisuje podatke za izvještaj u sheet za koeficijente.
     * @param sheet sheet za koeficijente
     * @param styles stilovi za izvještaj
     */
    private void writeCoefficientReport(Sheet sheet, ExcelStyleData styles) throws Exception
    {
        int rowIndex = 0;
        int columnIndex = 0;
        
        // zapiši podatke u sheet za koeficijente
        for (RevaKoefData coefficient : coefficients)
        {
            columnIndex = 0;
            Row row = sheet.createRow(rowIndex);
            
            if (coefficient.est_year != null)
            {
                LocationData location = null;
                if (coefficient.county != null) location = coefficient.county;
                else if (coefficient.city != null) location = coefficient.city;
                else if (coefficient.cadastral != null) location = coefficient.cadastral;
                
                ExcelUtils.createCell(row, columnIndex++, coefficient.col_gcm_type_name, styles.normalStyle);
                ExcelUtils.createCell(row, columnIndex++, location.type.trim().toUpperCase(), styles.normalStyle);
                ExcelUtils.createCell(row, columnIndex++, location.code.trim().toUpperCase(), styles.normalStyle);
                ExcelUtils.createCell(row, columnIndex++, location.name.trim().toUpperCase(), styles.normalStyle);
                ExcelUtils.createCell(row, columnIndex++, coefficient.est_year, styles.normalGeneralNumericStyle);
                ExcelUtils.createCell(row, columnIndex++, coefficient.koef_rev, styles.normalCoefficientNumericStyle);
            }
            else
            {
                ExcelUtils.createCell(row, columnIndex++, coefficient.col_gcm_type_name, styles.normalStyle);
                ExcelUtils.createCell(row, columnIndex++, coefficient.value_from, styles.normalNumericStyle);
                ExcelUtils.createCell(row, columnIndex++, coefficient.value_to, styles.normalNumericStyle);
                ExcelUtils.createCell(row, columnIndex++, coefficient.age_from, styles.normalGeneralNumericStyle);
                ExcelUtils.createCell(row, columnIndex++, coefficient.age_to, styles.normalGeneralNumericStyle);
                ExcelUtils.createCell(row, columnIndex++, coefficient.koef_rev, styles.normalCoefficientNumericStyle);
            }
            
            rowIndex++;
        }
    }
    


    /**
     * Metoda raèuna broj mjeseci proteklih izmeðu dva zadana datuma.
     * @param date1 Prvi datum
     * @param date2 Drugi datum
     * @return broj proteklih mjeseca zaokružen na više
     */
    private int numberOfMonthsBetween(Date date1, Date date2)
    {
        boolean isFirstDateAfterSecondDate = date1.compareTo(date2) > 0;
        
        Calendar laterDate = Calendar.getInstance();
        laterDate.setTime(isFirstDateAfterSecondDate ? date1 : date2);
        
        Calendar earlierDate = Calendar.getInstance();
        earlierDate.setTime(isFirstDateAfterSecondDate ? date2 : date1);
        
        int months  = (laterDate.get(Calendar.YEAR) - earlierDate.get(Calendar.YEAR)) * 12 +
                      (laterDate.get(Calendar.MONTH)- earlierDate.get(Calendar.MONTH)) + 
                      //(laterDate.get(Calendar.DAY_OF_MONTH) >= earlierDate.get(Calendar.DAY_OF_MONTH)? 0 : -1);
                      (laterDate.get(Calendar.DAY_OF_MONTH) > earlierDate.get(Calendar.DAY_OF_MONTH)? 1 : 0);
        
        return months;
    }
    
    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO920.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6546869704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Izracun pomocne trzisne vrijednosti za revalorizaciju");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO920.insertIntoEvent");
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
     *    <dt>client_type</dt><dd>Vrsta komitenta. Dozvoljene vrijednosti su RET ili CO.</dd>
     *    <dt>date_from</dt><dd>Datum od</dd>
     *    <dt>date_until</dt><dd>Datum do</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Vrsta komitenta", "Datum od", "Datum do" };

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
            
            // ako su svi parametri X, samo treba napraviti izvještaj
            if (bc.getArg(1).equalsIgnoreCase("X") && bc.getArg(2).equalsIgnoreCase("X") && bc.getArg(3).equalsIgnoreCase("X"))
            {
                only_make_report = true;
                info("Svi parametri su X - nece se raditi izracun vec samo izvjestaj.");
            }
            else
            {
                // dohvati vrstu komitenta
                this.customer_type = bc.getArg(1).trim().toUpperCase();
                if (!this.customer_type.equalsIgnoreCase("CO") && !this.customer_type.equalsIgnoreCase("RET")) throw new Exception("Vrsta komitenta moze biti samo CO ili RET!");
                
                // datum od i do
                date_from = new Date(dateFormat.parse(bc.getArg(2).trim()).getTime());
                date_until = new Date(dateFormat.parse(bc.getArg(3).trim()).getTime());
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
        BatchParameters bp = new BatchParameters(new BigDecimal("6546832704"));
        bp.setArgs(args);
        new BO920().run(bp);
    }
}