package hr.vestigo.modules.collateral.batch.bo72;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo72.BO721.CollateralIterator;
import hr.vestigo.modules.collateral.batch.bo72.BO721.CollateralPlacementIterator;
import hr.vestigo.modules.collateral.batch.bo72.BO721.FrameAgreementIterator;
import hr.vestigo.modules.collateral.batch.bo72.BO721.MortgageIterator;
import hr.vestigo.modules.collateral.batch.bo72.BO721.PlacementIterator;
import hr.vestigo.modules.collateral.batch.bo72.BO721.ExchangeRateIterator;
import hr.vestigo.modules.financial.common.yfzD.YFZD0;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;


/**
 * Obrada za izraèun pokrivenosti do visine kolaterala
 * @author hrakis
 */
public class BO720 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo72/BO720.java,v 1.1 2012/04/27 08:19:25 hrakis Exp $";

    private BatchContext bc;
    private BO721 bo721;
    private String returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    
    private String proc_type;

    private BigDecimal col_pro_id;
    private BigDecimal zero = new BigDecimal("0.00");
    private Date value_date;
    private BigDecimal rba_cus_id = new BigDecimal("8218251");
    private BigDecimal domestic_cur_id;
    private String domestic_code_char;

   
    /** Teèajna lista. */
    private HashMap<BigDecimal, ExchangeRateData> exchangeRates;
    
    /** Skup kolaterala. */
    private HashMap<BigDecimal, CollateralData> collaterals;
    
    /** Skup okvirnih sporazuma. */
    private HashMap<BigDecimal, FrameAgreementData> frameAgreements;
    
    /** Skup hipoteka. */
    private HashMap<BigDecimal, MortgageData> mortgages;
    
    /** Skup plasmana. */
    private HashMap<BigDecimal, PlacementData> placements;
    



    public String executeBatch(BatchContext bc) throws Exception
    {
        bc.debug("BO720.executeBatch pokrenut.");
        
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo721 = new BO721(bc);
        
        // evidentiranje eventa
        bo721.insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if(!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat datuma za koji se vrši izraèun pokrivenosti
        value_date = bo721.selectValueDate();
        
        // dohvat ID obrade iz tablice COL_PROC
        col_pro_id = bo721.selectColProId(value_date, proc_type);
        if(col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat teèajne liste
        loadExchangeRates(value_date); 
        
        // odreðivanje domaæe valute
        domestic_cur_id = new YFZD0(bc).getDomCurId();
        domestic_code_char = exchangeRates.get(domestic_cur_id).code_char;
        
        // uèitavanje kolaterala
        loadCollaterals();
        
        // uèitavanje okvirnih sporazuma
        loadFrameAgreements();
        
        // uèitavanje hipoteka
        loadMortgages();
        
        // uèitavanje plasmana
        loadPlacements();
        
        // uèitavanje veze kolateral-plasman
        loadCollateralPlacements();

        // izraèun
        for(Iterator<CollateralData> iterCollateral = collaterals.values().iterator(); iterCollateral.hasNext();)
        {
            CollateralData collateral = iterCollateral.next();
            calculate(collateral);
        }
        
        
        bc.beginTransaction();
        
        // zapisivanje izraèunatih pokrivenosti u bazu
        int count = 0;
        for(Iterator<CollateralData> iterCollateral = collaterals.values().iterator(); iterCollateral.hasNext();)
        {
            CollateralData collateral = iterCollateral.next();
            if(collateral.coverages == null) continue;
            for(Iterator<CoverageData> iterCoverage = collateral.coverages.iterator(); iterCoverage.hasNext();)
            {
                CoverageData coverage = iterCoverage.next();
                insertCoverage(coverage.placement, collateral, coverage);
                count++;
            }
        }
        
        // zapiši "praznu" pokrivenost za plasmane koji nisu pokriveni nijednim kolateralom
        for(Iterator<PlacementData> iterPlacement = placements.values().iterator(); iterPlacement.hasNext();)
        {
            PlacementData placement = iterPlacement.next();
            if(!placement.isCovered)
            {
                insertCoverage(placement, null, null);
                count++;
            }
        }
        
        bc.info("Ukupan broj zapisanih pokrivenosti: " + count);
        bo721.updateColProc(col_pro_id, count);
        
        bc.commitTransaction();
        
        bc.debug("Obrada zavrsena.");
        return returnCode;
    }
    


    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * Parametri se predaju u formatu: <code>bank_sign proc_type</code>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>proc_type</dt><dd>Vrsta obrade. Ovo je obvezan parametar.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters()
    {
        try
        {
            if(bc.getArgs().length != 2) throw new Exception("Neispravan broj parametara!");

            bc.info("Parametri predani obradi:");
            bc.info("Oznaka banke = " + bc.getArg(0));
            bc.info("Vrsta obrade = " + bc.getArg(1));
            
            bc.userLog("Parametri predani obradi:");
            bc.userLog("Oznaka banke = " + bc.getArg(0));
            bc.userLog("Vrsta obrade = " + bc.getArg(1));

            if(!bc.getArg(0).equals("RB")) throw new Exception("Bank sign mora biti 'RB'!");
            bc.setBankSign(bc.getArg(0));

            proc_type = bc.getArg(1).trim();
            if("".equals(proc_type)) throw new Exception("Obvezno predati vrstu obrade!");
        }
        catch(Exception ex)
        {
            bc.error("Neispravno zadani parametri! Parametri se predaju u formatu 'bank_sign proc_type'!", ex);
            return false;
        }
        return true;
    }
    
    
    /**
     * Metoda koja dohvaæa važeæu teèajnu listu za zadani datum.
     * @param date Datum teèajne liste
     * @return da li je dohvat uspješno zavšio
     */
    private boolean loadExchangeRates(Date date) throws Exception
    {
        bc.startStopWatch("loadExchangeRates");
        exchangeRates = new HashMap<BigDecimal, ExchangeRateData>();
        
        ExchangeRateIterator iter = bo721.selectExchangeRates(date);
        while(iter.next())
        {
            ExchangeRateData data = new ExchangeRateData();
            data.cur_id = iter.cur_id();
            data.code_num = iter.code_num();
            data.code_char = iter.code_char();
            data.midd_rate = iter.midd_rate();
            exchangeRates.put(data.cur_id, data);
            bc.debug("CUR_ID = " + data.cur_id + ", CODE_NUM = " + data.code_num + ", CODE_CHAR = " + data.code_char + ", MIDD_RATE = " + data.midd_rate);
        }
        iter.close();
        iter = null;
        bc.stopStopWatch("loadExchangeRates");

        // provjera postoji li teèajna lista
        if(exchangeRates.isEmpty())
        {
            bc.error("Ne postoji tecajna lista za datum " + date + "!", new String[]{});
            return false;
        }
        
        return true;
    }
    
    
    /**
     * Metoda koja preraèunava zadani iznos iz jedne u drugu valutu.
     * @param amount Iznos za pretvaranje
     * @param from_cur_id Valuta u kojoj je izražen iznos
     * @param to_cur_id Valuta u koju se želi pretvoriti iznos
     * @return pretvoreni iznos
     */
    private BigDecimal exchange(BigDecimal amount, BigDecimal from_cur_id, BigDecimal to_cur_id)
    {
        if(from_cur_id.equals(to_cur_id)) return amount;

        ExchangeRateData exchRateFrom = exchangeRates.get(from_cur_id);
        if(exchRateFrom == null)
        {
            bc.warning("...Nema tecaja za CUR_ID = " + from_cur_id + "!");
            return null;
        }

        ExchangeRateData exchRateTo = exchangeRates.get(to_cur_id);
        if(exchRateTo == null)
        {
            bc.warning("...Nema tecaja za CUR_ID = " + to_cur_id + "!");
            return null;
        }

        if(from_cur_id.equals(domestic_cur_id))
        {
            return amount.divide(exchRateTo.midd_rate, 2, RoundingMode.HALF_UP);
        }
        else if(to_cur_id.equals(domestic_cur_id))
        {
            return amount.multiply(exchRateFrom.midd_rate).setScale(2, RoundingMode.HALF_UP);
        }
        else
        {
            return amount.multiply(exchRateFrom.midd_rate).divide(exchRateTo.midd_rate, 2, RoundingMode.HALF_UP);
        }
    }
    
    
    /**
     * Metoda koja dohvaæa kolaterale koji ulaze u izraèun pokrivenosti.
     */
    private void loadCollaterals() throws Exception
    {
        bc.startStopWatch("loadCollaterals");
        this.collaterals = new HashMap<BigDecimal, CollateralData>();
        
        CollateralIterator iter = bo721.selectCollaterals(value_date);
        while(iter.next())
        {
            CollateralData collateral = new CollateralData();
            collateral.col_hea_id = iter.col_hea_id();
            collateral.col_num = iter.col_num().trim();
            collateral.value = iter.real_est_nomi_valu();
            collateral.value_cur_id = iter.real_est_nm_cur_id();
            collateral.value_hrk = exchange(collateral.value, collateral.value_cur_id, domestic_cur_id);
            collaterals.put(collateral.col_hea_id, collateral);
        }
        iter.close();
        iter = null;
        
        bc.debug("Broj dohvacenih kolaterala: " + collaterals.size());
        bc.stopStopWatch("loadCollaterals");
    }
    
    /**
     * Metoda koja dohvaæa okvirne sporazume.
     */
    private void loadFrameAgreements() throws Exception
    {
        bc.startStopWatch("loadFrameAgreements");
        this.frameAgreements = new HashMap<BigDecimal, FrameAgreementData>();
        
        FrameAgreementIterator iter = bo721.selectFrameAgreements(value_date);
        while(iter.next())
        {
            FrameAgreementData frameAgreement = new FrameAgreementData();
            frameAgreement.fra_agr_id = iter.fra_agr_id();
            frameAgreement.agreement_no = iter.agreement_no();
            frameAgreement.amount = iter.amount();
            frameAgreement.amount_cur_id = iter.cur_id();
            frameAgreement.amount_hrk = exchange(frameAgreement.amount, frameAgreement.amount_cur_id, domestic_cur_id);
            frameAgreements.put(frameAgreement.fra_agr_id, frameAgreement);
        }
        iter.close();
        iter = null;
        
        bc.debug("Broj dohvacenih okvirnih sporazuma: " + frameAgreements.size());
        bc.stopStopWatch("loadFrameAgreements");
    }
    
    /**
     * Metoda koja dohvaæa hipoteke.
     */
    private void loadMortgages() throws Exception
    {
        bc.startStopWatch("loadMortgages");
        this.mortgages = new HashMap<BigDecimal, MortgageData>();
        
        MortgageIterator iter = bo721.selectMortgages(value_date);
        while(iter.next())
        {
            MortgageData mortgage = new MortgageData();
            mortgage.coll_hf_prior_id = iter.coll_hf_prior_id();
            mortgage.priority = Integer.parseInt(iter.hf_priority());
            mortgage.amount = iter.amount_ref();
            mortgage.amount_cur_id = iter.cur_id_ref();
            mortgage.amount_hrk = exchange(mortgage.amount, mortgage.amount_cur_id, domestic_cur_id);
            mortgage.isRBA = rba_cus_id.equals(iter.hf_own_cus_id());
            if(iter.fra_agr_id() != null) mortgage.frame_agreement = frameAgreements.get(iter.fra_agr_id());
            
            CollateralData collateral = collaterals.get(iter.hf_coll_head_id());
            if(collateral != null)
            {
                if(collateral.mortgages == null) collateral.mortgages = new TreeMap<Integer, MortgageData>();
                collateral.mortgages.put(mortgage.priority, mortgage);
                mortgages.put(iter.coll_hf_prior_id(), mortgage);
            }
        }
        iter.close();
        iter = null;
        
        bc.debug("Broj dohvacenih hipoteka: " + mortgages.size());
        bc.stopStopWatch("loadMortgages");
    }
    
    /**
     * Metoda koja dohvaæa plasmane.
     */
    private void loadPlacements() throws Exception
    {
        bc.startStopWatch("loadPlacements");
        this.placements = new HashMap<BigDecimal, PlacementData>();
        
        PlacementIterator iter = bo721.selectPlacements(value_date);
        while(iter.next())
        {
            PlacementData placement = new PlacementData();
            placement.cus_acc_id = iter.cus_acc_id();
            placement.cus_acc_no = iter.cus_acc_no();
            placement.exposure = iter.exposure_balance();
            placement.exposure_cur_id = iter.exposure_cur_id();
            placement.exposure_hrk = exchange(placement.exposure, placement.exposure_cur_id, domestic_cur_id);
            placement.exposure_for_coverage_hrk = placement.exposure_hrk;
            placement.cus_id = iter.cus_id();
            placement.exposure_bal_lcy = iter.exposure_bal_lcy();
            placement.exp_off_bal_lcy = iter.exp_off_bal_lcy();
            placements.put(placement.cus_acc_id, placement);
        }
        iter.close();
        iter = null;
        
        bc.debug("Broj dohvacenih plasmana: " + placements.size());
        bc.stopStopWatch("loadPlacements");
    }
    
    /**
     * Metoda koja dohvaæa veze kolateral-plasman.
     */
    private void loadCollateralPlacements() throws Exception
    {
        bc.startStopWatch("loadCollateralPlacements");
        int count = 0, count1 = 0, count2 = 0;
        
        CollateralPlacementIterator iter = bo721.selectCollateralPlacements(value_date);
        while(iter.next())
        {
            count++;
            if(iter.col_hea_id() != null)  // veza direktno na kolateral
            {
                CollateralData collateral = collaterals.get(iter.col_hea_id());
                PlacementData placement = placements.get(iter.cus_acc_id());
                if(collateral == null || placement == null) continue;
                if(collateral.placements == null) collateral.placements = new ArrayList<PlacementData>();
                collateral.placements.add(placement);
                count1++;
            }
            if(iter.coll_hf_prior_id() != null)  // veza preko hipoteke
            {
                MortgageData mortgage = mortgages.get(iter.coll_hf_prior_id());
                PlacementData placement = placements.get(iter.cus_acc_id());
                if(mortgage == null || placement == null) continue;
                if(mortgage.placements == null) mortgage.placements = new ArrayList<PlacementData>();
                mortgage.placements.add(placement);
                count2++;
            }
        }
        iter.close();
        iter = null;
        
        bc.debug("Broj dohvacenih veza kolateral-plasman: " + count);
        bc.debug("Preko kolaterala: " + count1);
        bc.debug("Preko hipoteke: " + count2);
        bc.stopStopWatch("loadCollateralPlacements");
    }
    
    
    /** 
     * Metoda formira string koji sadrži sve potrebne podatke o izraèunu pokrivenosti za zadani kolateral.
     * @param collateral objekt s podacima o kolateralu
     * @return formirani string
     */
    public String collateralToString(CollateralData collateral)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\nKOLATERAL ").append(collateral.col_num);
        buffer.append(" - ");
        buffer.append(collateral.value).append(" ").append(exchangeRates.get(collateral.value_cur_id).code_char);
        buffer.append(" = ");
        buffer.append(collateral.value_hrk).append(" ").append(domestic_code_char);
        if (collateral.value_hrk.compareTo(collateral.value_for_coverage_hrk) != 0)
        {
           buffer.append(" -> iznos za pokrivanje = ");
           buffer.append(collateral.value_for_coverage_hrk).append(" ").append(domestic_code_char);
        }

        if (collateral.placements != null)
        {
           for (int i = 0; i < collateral.placements.size(); i++)
           {
               PlacementData placement = collateral.placements.get(i);
               buffer.append("\n|  PLASMAN").append(" ");
               buffer.append("(dir) ");
               buffer.append(placement.cus_acc_no);
               buffer.append(" - ");
               buffer.append(placement.exposure).append(" ").append(exchangeRates.get(placement.exposure_cur_id).code_char);
               buffer.append(" = ");
               buffer.append(placement.exposure_hrk).append(" ").append(domestic_code_char);
           }
        }

        if (collateral.mortgages != null)
        {
           for (Iterator<MortgageData> iterMort = collateral.mortgages.values().iterator(); iterMort.hasNext(); )
           {
               MortgageData mortgage = iterMort.next();
               buffer.append("\n|  HIPOTEKA").append(mortgage.priority).append(" ");
               
               if (mortgage.isRBA)
               {
                   buffer.append("RBA");
               }
               else
               {
                   buffer.append("strana");
                   buffer.append(" - ");
                   buffer.append(mortgage.amount).append(" ").append(exchangeRates.get(mortgage.amount_cur_id).code_char);
                   buffer.append(" = ");
                   buffer.append(mortgage.amount_hrk).append(" ").append(domestic_code_char);
               }
               
               if (mortgage.frame_agreement != null)
               {
                  buffer.append(" - DS (");
                  buffer.append(mortgage.frame_agreement.amount).append(" ").append(exchangeRates.get(mortgage.frame_agreement.amount_cur_id).code_char);
                  buffer.append(" = ");
                  buffer.append(mortgage.frame_agreement.amount_hrk).append(" ").append(domestic_code_char);
                  buffer.append(")");
               }
               
               if (mortgage.placements != null)
               {
                   for (int i = 0; i < mortgage.placements.size(); i++)
                   {
                       PlacementData placement = mortgage.placements.get(i);
                       buffer.append("\n|  |  PLASMAN");
                       buffer.append(" ");
                       buffer.append(placement.cus_acc_no);
                       buffer.append(" - ");
                       buffer.append(placement.exposure).append(" ").append(exchangeRates.get(placement.exposure_cur_id).code_char);
                       buffer.append(" = ");
                       buffer.append(placement.exposure_hrk).append(" ").append(domestic_code_char);
                       if (placement.exposure_hrk.compareTo(placement.exposure_for_coverage_hrk) != 0)
                       {
                          buffer.append(" -> prilagodjeno = ");
                          buffer.append(placement.exposure_for_coverage_hrk).append(" ").append(domestic_code_char);
                       }
                   }                   
               }
           }
        }
        
        buffer.append("\nPOKRIVENOST: ");
        if (collateral.coverages == null || collateral.coverages.size() == 0)
        {
            buffer.append("-");
        }
        else
        {
            for (int i = 0; i < collateral.coverages.size(); i++)
            {
                CoverageData coverage = collateral.coverages.get(i);
                buffer.append("\n   PLASMAN");
                buffer.append(" ");
                buffer.append(coverage.placement.cus_acc_no);
                buffer.append(" pokriven s ");
                buffer.append(coverage.amount_hrk).append(" ").append(domestic_code_char);
            }
        }
        
        buffer.append("\n");
        return buffer.toString();
    }
    
    
    /**
     * Metoda koja za zadani kolateral raèuna pokrivenost vezanih plasmana.
     * @param collateral Objekt s podacima o kolateralu.
     */
    public void calculate(CollateralData collateral)
    {
        bc.startStopWatch("calculate");
        
        // odreðivanje visine kolaterala koja æe se koristiti za pokrivanje plasmana
        // vrijednost se dobije kao tržišna vrijednost kolaterala umanjena za hipoteke drugih banaka do prve RBA hipoteke
        collateral.value_for_coverage_hrk = collateral.value_hrk;
        if (collateral.mortgages != null)
        {
            for (Iterator<MortgageData> iterMortgage = collateral.mortgages.values().iterator(); iterMortgage.hasNext();)
            {
                MortgageData mortgage = iterMortgage.next();
                if (mortgage.isRBA) break;
                collateral.value_for_coverage_hrk = collateral.value_for_coverage_hrk.subtract(mortgage.amount_hrk);
            }
            if (collateral.value_for_coverage_hrk.compareTo(zero) < 0) collateral.value_for_coverage_hrk = zero;
        }
        
        // stvaranje liste plasmana vezanih na kolateral
        HashMap<BigDecimal, PlacementData> collateralPlacements = new HashMap<BigDecimal, PlacementData>();
        if (collateral.placements != null)  // ubaci na listu plasmane vezane direktno
        {
            for (Iterator<PlacementData> iterPlacement = collateral.placements.iterator(); iterPlacement.hasNext();)
            {
                PlacementData placement = iterPlacement.next();
                if (!collateralPlacements.containsKey(placement.cus_acc_id)) collateralPlacements.put(placement.cus_acc_id, placement);
            }
        }
        if (collateral.mortgages != null)  // ubaci na listu plasmane vezane preko hipoteka
        {
            for (Iterator<MortgageData> iterMortgage = collateral.mortgages.values().iterator(); iterMortgage.hasNext();)
            {
                MortgageData mortgage = iterMortgage.next();
                if (!mortgage.isRBA || mortgage.placements == null) continue;  // samo RBA hipoteke dolaze u obzir
                BigDecimal totalPlacementExposure = getTotalPlacementExposure(mortgage.placements.iterator());  // suma izloženosti svih plasmana vezanih preko trenutne hipoteke
                
                for (Iterator<PlacementData> iterPlacement = mortgage.placements.iterator(); iterPlacement.hasNext();)
                {
                    PlacementData placement = iterPlacement.next();
                    if (!collateralPlacements.containsKey(placement.cus_acc_id)) collateralPlacements.put(placement.cus_acc_id, placement);
                    
                    // ako je hipoteka dio okvirnog sporazuma, potrebno je alikvotno ogranièiti izloženost plasmana tako da suma ne prelazi vrijednost okvirnog sporazuma
                    if (mortgage.frame_agreement != null && totalPlacementExposure.compareTo(mortgage.frame_agreement.amount_hrk) > 0 && totalPlacementExposure.compareTo(zero) > 0)
                    {
                        BigDecimal ratio = placement.exposure_hrk.divide(totalPlacementExposure, 20, RoundingMode.HALF_UP);  // udio izloženosti trenutnog plasmana u ukupnoj izloženosti svih plasmana hipoteke
                        BigDecimal exposure_for_coverage_hrk = mortgage.frame_agreement.amount_hrk.multiply(ratio).setScale(2, RoundingMode.HALF_UP);  // ogranièeni iznos izloženosti
                        if (exposure_for_coverage_hrk.compareTo(placement.exposure_for_coverage_hrk) < 0) placement.exposure_for_coverage_hrk = exposure_for_coverage_hrk;  // postavi izraèunati iznos kao izloženost plasmana za izraèun (samo ako je iznos manji od postojeæeg)  
                    }
                }
            }
        }
        
        // izraèun pokrivenosti
        BigDecimal totalPlacementExposureForCoverage = getTotalPlacementExposureForCoverage(collateralPlacements.values().iterator());  // suma izloženosti svih plasmana vezanih na kolateral
        if(totalPlacementExposureForCoverage.compareTo(zero) > 0)
        {
            for(Iterator<PlacementData> iterPlacement = collateralPlacements.values().iterator(); iterPlacement.hasNext();)
            {
                PlacementData placement = iterPlacement.next();
                BigDecimal ratio = placement.exposure_for_coverage_hrk.divide(totalPlacementExposureForCoverage, 20, RoundingMode.HALF_UP);  // udio izloženosti trenutnog plasmana u sumi izloženosti svih plasmana vezanih na kolateral
                BigDecimal coverageAmount = collateral.value_for_coverage_hrk.multiply(ratio).setScale(2, RoundingMode.HALF_UP);  // iznos kolaterala za pokrivanje trenutnog plasmana
                
                // zabilježi pokrivenost 
                CoverageData coverage = new CoverageData();
                coverage.placement = placement;
                coverage.amount_hrk = coverageAmount;
                if(collateral.coverages == null) collateral.coverages = new ArrayList<CoverageData>();
                collateral.coverages.add(coverage);
                
                placement.isCovered = true; 
            }
        }

        bc.debug(collateralToString(collateral));
        
        // vrati na staro iznos izloženosti plasmana za izraèun
        for(Iterator<PlacementData> iterPlacement = collateralPlacements.values().iterator(); iterPlacement.hasNext();)
        {
            PlacementData placement = iterPlacement.next();
            placement.exposure_for_coverage_hrk = placement.exposure_hrk;
        }
        
        bc.stopStopWatch("calculate");
    }
    
    /**
     * Metoda koja vraæa sumu izloženosti svih zadanih plasmana.
     * @param iterPlacement Iterator s plasmanima
     * @return suma izloženosti
     */
    private BigDecimal getTotalPlacementExposure(Iterator<PlacementData> iterPlacement)
    {
        BigDecimal sum = zero;
        for(;iterPlacement.hasNext();) sum = sum.add(iterPlacement.next().exposure_hrk);
        return sum;
    }
    
    /**
     * Metoda koja vraæa sumu modificiranih iznosa izloženosti svih zadanih plasmana. 
     * @param iterPlacement Iterator s plasmanima
     * @return suma modificiranih iznosa izloženosti
     */
    private BigDecimal getTotalPlacementExposureForCoverage(Iterator<PlacementData> iterPlacement)
    {
        BigDecimal sum = zero;
        for(;iterPlacement.hasNext();) sum = sum.add(iterPlacement.next().exposure_for_coverage_hrk);
        return sum;
    }
    
    /**
     * Metoda koja zapisuje u bazu izraèunatu pokrivenost.
     * @param placement objekt s podacima o plasmanu
     * @param collateral objekt s podacima o kolateralu. Ako se preda null, u bazu se zapisuje prazna pokrivenost
     * @param coverage objekt s podacima o pokrivenosti. Ako se preda null, u bazu se zapisuje prazna pokrivenost
     */
    private void insertCoverage(PlacementData placement, CollateralData collateral, CoverageData coverage) throws Exception
    {
        BigDecimal cus_acc_exp_col_id = bo721.generateNewId();
        BigDecimal cus_id = placement.cus_id;
        BigDecimal cus_acc_id = placement.cus_acc_id;
        BigDecimal col_hea_id = collateral == null ? null : collateral.col_hea_id;
        BigDecimal exp_coll_amount = coverage == null ? zero : coverage.amount_hrk;
        BigDecimal coll_amount = collateral == null ? zero : collateral.value;
        BigDecimal exp_coll_cur_id = collateral == null ? null : collateral.value_cur_id;
        BigDecimal exp_fc_amount = coverage == null ? zero : exchange(coverage.amount_hrk, domestic_cur_id, placement.exposure_cur_id);
        BigDecimal coll_fc_amount = coverage == null ? zero : exchange(coverage.amount_hrk, domestic_cur_id, collateral.value_cur_id);
        Date cover_date = value_date;
        BigDecimal coll_hf_prior_id = null;
        BigDecimal exp_percent = placement == null || coverage == null || placement.exposure_hrk.compareTo(zero) <= 0 ? zero : coverage.amount_hrk.multiply(new BigDecimal("100.00")).divide(placement.exposure_hrk, 2, RoundingMode.HALF_UP);
        BigDecimal fra_agr_id = null;
        BigDecimal exp_cur_id = placement.exposure_cur_id;
        BigDecimal exp_balance = placement.exposure;
        BigDecimal exp_balance_hrk = placement.exposure_hrk;
        BigDecimal mvp_dfl = zero;
        BigDecimal exposure_bal_lcy = placement.exposure_bal_lcy;
        BigDecimal exp_off_bal_lcy = placement.exp_off_bal_lcy;
        String rba_eligibility = null;
        String eligibility = "0";
        String b1_eligibility = null;
        String b2_irb_elig = null;
        
        bo721.insertCoverage(cus_acc_exp_col_id, cus_id, cus_acc_id, col_hea_id, col_pro_id,
                exp_coll_amount, coll_amount, exp_coll_cur_id, exp_fc_amount, coll_fc_amount,
                cover_date, coll_hf_prior_id, exp_percent, fra_agr_id,
                exp_cur_id, exp_balance, exp_balance_hrk,
                mvp_dfl, exposure_bal_lcy, exp_off_bal_lcy,
                rba_eligibility, eligibility, b1_eligibility, b2_irb_elig);
    }
    
    
    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("5166369704"));
        bp.setArgs(args);
        new BO720().run(bp);
    }
}