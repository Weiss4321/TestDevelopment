package hr.vestigo.modules.collateral.batch.bo76;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo76.BO761.CollateralFromDailyBatchIterator;
import hr.vestigo.modules.collateral.batch.bo76.BO761.CollateralIterator;
import hr.vestigo.modules.collateral.batch.bo76.BO761.InsuranceIterator;
import hr.vestigo.modules.collateral.batch.bo76.BO761.MortgageIterator;
import hr.vestigo.modules.collateral.batch.bo76.BO761.PlacementIterator;
import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;
import hr.vestigo.modules.collateral.common.yoyM.YOYM0;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Punjenje podataka za Group Collateral Management Project
 * @author hrakis
 */
public class BO760 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo76/BO760.java,v 1.7 2015/02/19 13:31:54 hrakis Exp $";
    
    private BatchContext bc;
    private BO761 bo761;
    
    private String batch_type;
    private Date value_date;
    
    private String proc_type;
    private boolean isDailyBatch;
    
    private BigDecimal col_pro_id;
    private BigDecimal alloc_col_pro_id;
    private YRXX0 yrxx0;
    private YOYM0 mapping_coll_exp_date;
   
    private final BigDecimal hrk_cur_id = new BigDecimal("63999");
    private final BigDecimal euro_cur_id = new BigDecimal("64999");
    private final BigDecimal zero = new BigDecimal("0.00");
    private final BigDecimal hundred = new BigDecimal("100.00");
    private final BigDecimal rba_cus_id = new BigDecimal("8218251");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo761 = new BO761(bc);

        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat ID-a obrade iz tablice COL_PROC
        this.col_pro_id = bo761.selectColProId(value_date, proc_type);
        if (col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.info("Obrada je dobila COL_PRO_ID = " + col_pro_id + ".");

        // dohvat ID-a obrade izraèuna pokrivenosti
        this.alloc_col_pro_id = bo761.selectAllocColProId(value_date);
        if (alloc_col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.info("Uzimaju se podaci o ND ponderiranom izracunu pokrivenosti koje je generirala obrada COL_PRO_ID = " + alloc_col_pro_id + ".");
        
        // mapiranja
        mapping_coll_exp_date = new YOYM0(bc, "coll_exp_date", value_date);
        
        // inicijalizacija commona za konverziju iznosa i ispis teèajne liste važeæe na datum valute
        this.yrxx0 = new YRXX0(bc);
        bc.debug(yrxx0.exchangeRateToString(value_date));
        
        int count = 0;
        bc.beginTransaction();
        if (isDailyBatch)  // dnevna obrada
        {
            CollateralIterator iter = bo761.selectCollaterals(value_date);  // dohvati sve kolaterale aktivne na zadani datum
            while (iter.next())
            {
                CollateralData collateral = getCollateralDataFromIterator(iter);    // stvori objekt na temelju iteratora 
                prepareCollateral(collateral);                                      // dohvati sve potrebne vrijednosti
                if (collateral.cus_acc_id != null) bo761.insertIntoCollGcmData(col_pro_id, value_date, collateral);    // zapiši kolateral u bazu podataka
                count++;
            }
            iter.close();
        }
        else  // mjeseèna obrada
        {
            BigDecimal ref_daily_col_pro_id = bo761.selectRefDailyColProId(value_date);   // naði ID dnevne obrade na koju se mjeseèna obrada referencira
            if (ref_daily_col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
            bc.info("Obrada preuzima podatke koji nisu vezani za izracun pokrivenosti od dnevne obrade COL_PRO_ID = " + ref_daily_col_pro_id + ".");
            CollateralFromDailyBatchIterator iter = bo761.selectCollateralsFromDailyBatch(value_date, ref_daily_col_pro_id);  // dohvati kolaterale koje je priredila dnevna obrada
            while (iter.next())
            {
                CollateralData collateral = getCollateralDataFromIterator(iter);    // stvori objekt na temelju iteratora
                prepareCollateral(collateral);                                      // dohvati sve potrebne vrijednosti
                if (collateral.cus_acc_id != null) bo761.insertIntoCollGcmData(col_pro_id, value_date, collateral);    // zapiši kolateral u bazu podataka
                count++;
            }
            iter.close();
        }
       
        // zapisivanje kraja obrade
        bo761.updateColProc(col_pro_id, count);
        bc.commitTransaction();
        bc.info("Broj upisanih kolaterala: " + count);
        bc.userLog("Broj upisanih kolaterala: " + count);
       
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda koja na temelju osnovnih podataka o zadanom kolateralu dohvaæa sve podatke potrebne za punjenje.  
     * @param collateral objekt s podacima o kolateralu
     */
    private void prepareCollateral(CollateralData collateral) throws Exception
    {
        // dohvati podtip kolaterala
        fetchSubtypeData(collateral);
        
        // izraèunaj NCV
        if (isDailyBatch) calculateNCV(collateral);
        
        // dohvati ponder kolaterala
        if (isDailyBatch) fetchPonderData(collateral);
        
        // dohvati sumu iznosa hipoteka do prve RBA hipoteke, te najraniji i najdalji datum do kada vrijedi hipoteka
        if (isDailyBatch) fetchMortgagesData(collateral);
        
        // izraèunaj WCOV = (NCV * ponder) - hipoteke
        if (isDailyBatch) calculateWCOV(collateral);
        
        // izraèunaj WCV
        calculateWCV(collateral);
        
        // dohvati sumu izloženosti svih povezanih plasmana, te plasman s najdaljim datumom dospijeæa
        fetchPlacementsData(collateral);
        
        // izraèunaj LTV
        calculateLTV(collateral);
        
        // dohvati datum isteka police osiguranja i sumiraj osigurane svote za aktivne police
        if (isDailyBatch) fetchInsurancePoliciesData(collateral);
        
        // izraèunaj datum do kada vrijedi kolateral
        calculateExpiryData(collateral);
        
        // ispiši vrijednosti
        bc.debug(collateral.toString());
    }
    
    /**
     * Metoda koja zadanom kolateralu izraèunava Nominal Collateral Value (NCV), tj. tržišnu vrijednost kolaterala izraženu u EUR. 
     * @param collateral objekt s podacima o kolateralu
     */
    private void calculateNCV(CollateralData collateral) throws Exception
    {
        if (collateral.real_est_nomi_valu == null || collateral.real_est_nm_cur_id == null) collateral.ncv = zero;  // ako tržišna vrijednost ili valuta nisu popunjeni, postavi NCV na 0;
        else collateral.ncv = yrxx0.exchange(collateral.real_est_nomi_valu, collateral.real_est_nm_cur_id, euro_cur_id, value_date);  // pretvori tržišnu vrijednost u EUR
    }
    
    /**
     * Metoda dohvaæa podvrstu zadanog kolaterala.
     * @param collateral objekt s podacima o kolateralu
     */
    private void fetchSubtypeData(CollateralData collateral) throws Exception
    {
        collateral.col_sub_id = bo761.selectCollSubtypeID(collateral.col_hea_id, collateral.col_cat_id);
    }
    
    /**
     * Metoda dohvaæa ponder primijenjen na zadani kolateral.
     * @param collateral objekt s podacima o kolateralu
     */
    private void fetchPonderData(CollateralData collateral) throws Exception
    {
        collateral.ponder = bo761.selectPonderCo(value_date, collateral.col_hea_id);
        if (collateral.ponder == null) collateral.ponder = bo761.selectPonderDfl(value_date, collateral.col_cat_id, collateral.col_typ_id, collateral.col_sub_id, collateral.inspol_ind);
        if (collateral.ponder == null && "D".equalsIgnoreCase(collateral.inspol_ind)) collateral.ponder = bo761.selectPonderDfl(value_date, collateral.col_cat_id, collateral.col_typ_id, collateral.col_sub_id, "N");
        if (collateral.ponder == null) collateral.ponder = new BigDecimal("100.00");
        
        collateral.real_ponder = bo761.selectRealPonder(collateral.col_hea_id, alloc_col_pro_id);
    }
    
    /**
     * Metoda za zadani kolateral dohvaæa sumu iznosa hipoteka do prve RBA hipoteke, te najraniji i najdalji datum do kada vrijedi hipoteka.
     * @param collateral objekt s podacima o kolateralu
     */
    private void fetchMortgagesData(CollateralData collateral) throws Exception
    {
        boolean isRBAMortgageReached = false;  // oznaka da li je iterator došao do RBA hipoteke
        MortgageIterator iterMortgage = bo761.selectMortgages(value_date, collateral.col_hea_id);
        while (iterMortgage.next())
        {
            if (iterMortgage.hf_own_cus_id().equals(rba_cus_id))  // ako je RBA hipoteka
            {
                // datum do kada vrijedi hipoteka (ako je hipoteka dio okvirnog sporazuma, uzima se datum do kada vrijedi okvirni sporazum)
                Date date_until = iterMortgage.hf_date_hfc_until();
                if (iterMortgage.fra_agr_id() != null) date_until = bo761.selectFrameAgreementDateUntil(value_date, iterMortgage.fra_agr_id());
                
                // provjeri je li datum manji od minimalnog, te da li je veæi od maksimalnog
                if (collateral.earliest_exp_date == null || date_until.compareTo(collateral.earliest_exp_date) < 0) collateral.earliest_exp_date = date_until;
                if (collateral.latest_exp_date == null || date_until.compareTo(collateral.latest_exp_date) > 0) collateral.latest_exp_date = date_until;
                
                isRBAMortgageReached = true;
            }
            
            if (!isRBAMortgageReached)  // ako još nije dosegnuta RBA hipoteka, poveæaj sumu za vrijednost hipoteke
            {
                BigDecimal amount_eur = yrxx0.exchange(iterMortgage.amount_ref(), iterMortgage.cur_id_ref(), euro_cur_id, value_date);
                collateral.mortgages_sum = collateral.mortgages_sum.add(amount_eur);
            }
        }
        iterMortgage.close();
    }
    
    /**
     * Metoda koja zadanom kolateralu izraèunava Weighted Collateral Object Value (WCOV) = (NCV * ponder) - hipoteke
     * @param collateral objekt s podacima o kolateralu
     */
    private void calculateWCOV(CollateralData collateral) throws Exception
    {
        collateral.wcov = collateral.ncv.multiply(collateral.ponder).divide(hundred, 2, RoundingMode.HALF_UP).subtract(collateral.mortgages_sum);
        if (collateral.wcov.compareTo(zero) < 0) collateral.wcov = zero;
    }
    
    /**
     * Metoda koja zadanom kolateralu izraèunava Weighted Collateral Value - alociranu vrijednost kolaterala u EUR prema ND ponderiranom izraèunu pokrivenosti
     * @param collateral objekt s podacima o kolateralu
     */
    private void calculateWCV(CollateralData collateral) throws Exception
    {
        BigDecimal allocatedValueHRK = bo761.selectCollateralAllocatedValueHRK(collateral.col_hea_id, alloc_col_pro_id);
        collateral.wcv = yrxx0.exchange(allocatedValueHRK, hrk_cur_id, euro_cur_id, value_date);
    }
    
    /**
     * Metoda koja dohvaæa sumu izloženosti plasmana koji su osigurani zadanim kolateralom, te podatke o plasmanu s najdaljim datumom dospijeæa.
     * @param collateral objekt s podacima o kolateralu
     */
    private void fetchPlacementsData(CollateralData collateral) throws Exception
    {
        boolean isFirstPlacement = true;  // oznaka da li se radi o prvom plasmanu
        PlacementIterator iterPlacement = bo761.selectCollateralPlacements(value_date, collateral.col_hea_id);
        while (iterPlacement.next())
        {
            if (isFirstPlacement)  // dohvati plasman s najdaljim datumom dospijeæa
            {
                collateral.cus_acc_id = iterPlacement.cus_acc_id();
                collateral.placement_due_date = iterPlacement.due_date();
                isFirstPlacement = false;
            }
            BigDecimal exposure_balance_hrk = bo761.selectPlacementExposureBalanceHRK(iterPlacement.cus_acc_id(), alloc_col_pro_id);  // izloženost plasmana u HRK
            BigDecimal exposure_balance_eur = yrxx0.exchange(exposure_balance_hrk, hrk_cur_id, euro_cur_id, value_date);              // izloženost plasmana u EUR
            collateral.placement_exposure_sum = collateral.placement_exposure_sum.add(exposure_balance_eur);                          // poveæaj sumu izloženosti povezanih plasmana
        }
        iterPlacement.close();
    }
    
    /**
     * Metoda za zadani kolateral izraèunava LTV - omjer sume izloženosti svih plasmana koje osigurava kolateral i NCV kolaterala, izražen u postotku.
     * @param collateral objekt s podacima o kolateralu
     */
    private void calculateLTV(CollateralData collateral) throws Exception
    {
        if (collateral.ncv.compareTo(zero) > 0) collateral.ltv = collateral.placement_exposure_sum.divide(collateral.ncv, 10, RoundingMode.HALF_UP).multiply(hundred).setScale(2, RoundingMode.HALF_UP); 
    }
    
    
    /**
     * Metoda dohvaæa sumu osiguranih svota svih aktivnih polica osiguranja, te datum do kada vrijedi polica.
     * Datum do kada vrijedi polica se odreðuje prema sljedeæim primjerima: 
     *     STATUS    DATUM DO KADA VRIJEDI/VRIJEDILA JE POLICA
     *  1)   N                       01.01.2013.
     *       A                       01.10.2013.
     *       A                       01.11.2013. 
     *     U ovom sluèaju traženi datum je 01.10.2013.
     *
     *  2)   A                       01.10.2013.
     *       A                       01.11.2013.
     *     U ovom sluèaju traženi datum je 01.10.2013.
     *
     *  3)   N                       01.05.2013.
     *       N                       01.06.2013.
     *     U ovom sluèaju traženi datum je 01.06.2013.

     *  4)   N                       01.05.2013.
     *       N                       01.06.2013.
     *       A                       01.11.2013.
     *     U ovom sluèaju traženi datum je 01.11.2013. 
     * @param collateral objekt s podacima o kolateralu
     */
    private void fetchInsurancePoliciesData(CollateralData collateral) throws Exception
    {
        boolean isActivePolicyEncountered = false;  // oznaka da li se došlo do aktivne police
        
        InsuranceIterator iterPolicy = bo761.selectInsurancePolicies(collateral.col_hea_id);
        while (iterPolicy.next())
        {
            if ("A".equalsIgnoreCase(iterPolicy.ip_act_noact()))  // ako je polica aktivna
            {
                // pribroji osiguranu svotu
                BigDecimal amount_eur = yrxx0.exchange(iterPolicy.ip_secu_val(), iterPolicy.ip_cur_id(), euro_cur_id, value_date);
                collateral.insurance_sum = collateral.insurance_sum.add(amount_eur);
                
                // ako je ovo prva (najkraæa) aktivna polica, uzmi datum do kada vrijedi polica
                if (!isActivePolicyEncountered)
                {
                    isActivePolicyEncountered = true;
                    collateral.insurance_exp_date = iterPolicy.ip_vali_until();
                }
            }
            else  // ako je polica neaktivna
            {
                if (!isActivePolicyEncountered)  // ako još nije naišla aktivna polica, uzmi datum do kada vrijedi polica
                {
                    collateral.insurance_exp_date = iterPolicy.ip_vali_until();
                }
            }
        }
        iterPolicy.close();
    }
    
    /**
     * Metoda koja odreðuje datum do kada vrijedi kolateral.
     * @param collateral objekt s podacima o kolateralu
     */
    private void calculateExpiryData(CollateralData collateral) throws Exception
    {
        // mapiraj kolateral na algoritam odreðivanja datuma do kada vrijedi
        GcmTypeData gcmTypeData_coll_exp_date = mapping_coll_exp_date.resolve(collateral.col_cat_id, collateral.col_typ_id, collateral.col_sub_id);
        
        if (gcmTypeData_coll_exp_date != null && gcmTypeData_coll_exp_date.code != null)
        {
            if (gcmTypeData_coll_exp_date.code.equalsIgnoreCase("exp_dat_hip"))  // Do datuma hipoteke; kod više hipoteka - najdalji dat.
            {
                collateral.coll_exp_date = collateral.latest_exp_date;
            }
            else if (gcmTypeData_coll_exp_date.code.equalsIgnoreCase("exp_dat_pls2"))  // Datum dospijeæa plasmana; kod više plasmana - najdalji dat.
            {
                collateral.coll_exp_date = collateral.placement_due_date;
            }
            else if (gcmTypeData_coll_exp_date.code.equalsIgnoreCase("exp_dat_pls1") && collateral.placement_due_date != null)  // Datum dospijeæa plasmana + 10 godina
            {
                collateral.coll_exp_date = addYears(collateral.placement_due_date, 10);
            }
            else  // datum dospijeæa depozita, police osiguranja, obveznice, garancije
            {
                collateral.coll_exp_date = bo761.selectCollExpiryDate(collateral.col_hea_id, gcmTypeData_coll_exp_date.code);
            }
        }
    }
    
    
    /**
     * Metoda na temelju zadanog iteratora stvara objekt s podacima o kolateralu.
     * @param iter iterator s podacima o kolateralu
     * @return kreirani objekt
     */
    private CollateralData getCollateralDataFromIterator(CollateralIterator iter) throws Exception
    {
        CollateralData collateral = new CollateralData();
        collateral.col_hea_id = iter.col_hea_id();
        collateral.col_num = iter.col_num();
        collateral.col_cat_id = iter.col_cat_id();
        collateral.col_typ_id = iter.col_typ_id();
        collateral.real_est_nomi_valu = iter.real_est_nomi_valu();
        collateral.real_est_nm_cur_id = iter.real_est_nm_cur_id();
        collateral.inspol_ind = iter.inspol_ind();
        if (!"D".equalsIgnoreCase(collateral.inspol_ind)) collateral.inspol_ind = "N";
        return collateral;
    }
    
    /**
     * Metoda na temelju zadanog iteratora stvara objekt s podacima o kolateralu.
     * @param iter iterator s podacima o kolateralu prireðenima dnevnom obradom
     * @return kreirani objekt
     */
    private CollateralData getCollateralDataFromIterator(CollateralFromDailyBatchIterator iter) throws Exception
    {
        CollateralData collateral = new CollateralData();
        collateral.col_hea_id = iter.col_hea_id();
        collateral.col_num = iter.col_num();
        collateral.col_cat_id = iter.col_cat_id();
        collateral.col_typ_id = iter.col_typ_id();
        collateral.real_est_nomi_valu = iter.real_est_nomi_valu();
        collateral.real_est_nm_cur_id = iter.real_est_nm_cur_id();
        collateral.ncv = iter.ncv();
        collateral.ponder = iter.ponder();
        collateral.real_ponder = iter.real_ponder();
        collateral.wcov = iter.wcov();
        collateral.wcv = iter.wcv();
        collateral.earliest_exp_date = iter.earliest_exp_date();
        collateral.latest_exp_date = iter.latest_exp_date();
        collateral.insurance_sum = iter.insurance_sum();
        collateral.insurance_exp_date = iter.insurance_exp_date();
        return collateral;
    }
    
    /**
     * Metoda koja uveæava zadani datum za odreðeni broj godina.
     * @param date Datum
     * @param years Broj godina
     * @return uveæani datum
     */
    private Date addYears(Date date, int years)
    {
        if (date == null) return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        Date retDate = new Date(calendar.getTimeInMillis());
        Date maxDate = Date.valueOf("9999-12-31");
        if (retDate.compareTo(maxDate) >= 0) return maxDate;
        else return retDate;
    }
 
    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6390603704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Punjenje podataka za Group Collateral Management Project");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("insertIntoEvent");
            return eve_id;
        }
        catch(Exception ex)
        {
            bc.error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade u tablicu EVENT!", ex);
            throw ex;
        }
    }

        
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * Parametri se predaju u formatu: <code>bank_sign batch_type value_date</code>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>batch_type</dt><dd>Vrsta obrade. (D za dnevnu obradu ili M za mjeseènu obradu)</dd>
     *    <dt>value_date</dt><dd>Datum valute. Opcionalan parametar - ako se ne navede uzima se trenutni datum za dnevne obrade ili zadnji dan prethodnog mjeseca za mjeseène obrade.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters()
    {
        try
        {
            int numberOfParameters = bc.getArgs().length;
            if (numberOfParameters < 2 || numberOfParameters > 3) throw new Exception("Neispravan broj parametara!");

            bc.info("Parametri predani obradi:");
            bc.info("Oznaka banke = " + bc.getArg(0));
            bc.info("Vrsta obrade = " + bc.getArg(1));
            bc.info("Datum valute = " + bc.getArg(2));
            
            bc.userLog("Parametri predani obradi:");
            bc.userLog("Oznaka banke = " + bc.getArg(0));
            bc.userLog("Vrsta obrade = " + bc.getArg(1));
            bc.userLog("Datum valute = " + bc.getArg(2));

            // oznaka banke
            if (!bc.getArg(0).equals("RB")) throw new Exception("Bank sign mora biti 'RB'!");
            bc.setBankSign(bc.getArg(0));
            
            // vrsta obrade - D ili M
            batch_type = bc.getArg(1).trim().toUpperCase();
            if (batch_type.equals("D")) isDailyBatch = true;
            else if (batch_type.equals("M")) isDailyBatch = false;
            else throw new Exception("Vrsta obrade mora biti D ili M!");
            
            // oznaka vrste obrade iz tablice COL_PROC 
            proc_type = "GC" + batch_type;
            
            // odreðivanje datuma valute
            if (numberOfParameters == 3)  // ako je datum predan obradi, uzmi taj datum
            {
                value_date = new Date(dateFormat.parse(bc.getArg(2).trim()).getTime());
            }
            else  // ako datum nije predan obradi
            {
                value_date = new Date(bc.getExecStartTime().getTime());  // uzmi tekuæi datum
                
                if (!isDailyBatch)  // ako je mjeseèna obrada, uzmi zadnji dan u prethodnom mjesecu
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(value_date);
                    calendar.set(Calendar.DATE, 1);
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    value_date = new Date(calendar.getTimeInMillis());
                }
            }
            
            bc.info("POKRECE SE " + (isDailyBatch ? "DNEVNA" : "MJESECNA") + " OBRADA ZA DATUM " + value_date + "...");
            return true;
        }
        catch (Exception ex)
        {
            bc.error("Neispravno zadani parametri! Parametri se predaju u formatu 'bank_sign batch_type value_date'!", ex);
            return false;
        }
    }


    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("6390600704"));
        bp.setArgs(args);
        new BO760().run(bp);
    }
}