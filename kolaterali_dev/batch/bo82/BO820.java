package hr.vestigo.modules.collateral.batch.bo82;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo82.BO821.CcfGroupIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.CollSubtypeIdsIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.CollateralCategoryIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.CollateralIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.CollateralSubtypeIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.CollateralTypeIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.CoverageIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.FrameAgreementIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.GuaranteeIssuerIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.MortgageIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.PlacementIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.PonderCoIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.PonderDefaultIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.RatingDefinitionIterator;
import hr.vestigo.modules.collateral.batch.bo82.BO821.MappingSchIterator;
import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;
import hr.vestigo.modules.collateral.common.yoyM.YOYM0;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.financial.common.yfzD.YFZD0;
import hr.vestigo.modules.rba.common.yrxX.ExchangeRateData;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.math.BigDecimal;
import java.math.RoundingMode;



/**
 * Obrada za izraèun pokrivenosti
 * @author hrakis
 */
public class BO820 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo82/BO820.java,v 1.30 2018/05/09 11:38:26 hraskd Exp $";

    private BatchContext bc;
    private BO821 bo821;

    private String proc_type;
    private AllocationTypeData allocationType;
    private boolean isTestMode = false;
    
    private Date value_date;
    private BigDecimal hfx;
    private BigDecimal hfx_gar;
    
    private YRXX0 yrxx0;
    private BigDecimal domestic_cur_id;
    private String domestic_code_char;
    
    private final BigDecimal zero = new BigDecimal("0.00");
    private final BigDecimal one = new BigDecimal("1.00");
    private final BigDecimal hundred = new BigDecimal("100.00");
    private final DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    
    private final BigDecimal defaultCcfValue = new BigDecimal("100.00");
    private final BigDecimal defaultCcfValueForModuleOkv = new BigDecimal("75.00");
    private final BigDecimal defaultCcfValueForModuleLoc = new BigDecimal("50.00");
    private final BigDecimal defaultCcfValueForModuleGar = new BigDecimal("100.00");
    private final BigDecimal defaultCcfValueForModulesPkrKkrPpzTrcKrd = new BigDecimal("100.00");
    
    private final BigDecimal evfx = new BigDecimal("1.4");
    
    private HashMap<BigDecimal, ExchangeRateData> exchangeRate = new HashMap<BigDecimal, ExchangeRateData>();
    private HashMap<BigDecimal, CollateralData> collaterals = new HashMap<BigDecimal, CollateralData>();
    private HashMap<BigDecimal, FrameAgreementData> frameAgreements = new HashMap<BigDecimal, FrameAgreementData>();
    private HashMap<BigDecimal, MortgageData> mortgages = new HashMap<BigDecimal, MortgageData>();
    private HashMap<BigDecimal, PlacementData> placements = new HashMap<BigDecimal, PlacementData>();
    private HashMap<String, PlacementData> frames = new HashMap<String, PlacementData>();
    private HashMap<BigDecimal, CustomerData> customers = new HashMap<BigDecimal, CustomerData>();
    private ArrayList<CoverageData> coverages = new ArrayList<CoverageData>();
    private ArrayList<CoverageData> eligibleCoverages = new ArrayList<CoverageData>();
    private HashMap<BigDecimal, CollateralCategoryData> collateralCategories = new HashMap<BigDecimal, CollateralCategoryData>();
    private HashMap<BigDecimal, CollateralTypeData> collateralTypes = new HashMap<BigDecimal, CollateralTypeData>();
    private HashMap<BigDecimal, CollateralSubtypeData> collateralSubtypes = new HashMap<BigDecimal, CollateralSubtypeData>();
    private HashMap<String, CcfGroupData> ccfGroups = new HashMap<String, CcfGroupData>();
    private HashMap<String, EirArPocParamData> eirParams = new HashMap<String, EirArPocParamData>();
    private HashMap<String, EirArPocParamData> arPocParams = new HashMap<String, EirArPocParamData>();
    private HashMap<String, B2GroupData> b2Groups = new HashMap<String, B2GroupData>();
    private ArrayList<PonderData> defaultPonders = new ArrayList<PonderData>();
    private TreeMap<String, HashMap<BigDecimal, RatingData>> ratingDefinitions = new TreeMap<String, HashMap<BigDecimal, RatingData>>();
    private ArrayList<ArrayList<CoverageData>> allocationGroups = new ArrayList<ArrayList<CoverageData>>();

    private String retCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo821 = new BO821(bc);
        this.yrxx0 = new YRXX0(bc);
        
        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat datuma za koji se vrši izraèun pokrivenosti (datuma valute)
        value_date = bo821.selectValueDate(allocationType);
        if (value_date == null)
        {
           error("Nije moguce utvrditi datum valute!", null);
           return RemoteConstants.RET_CODE_ERROR;
        }
        info("Datum za koji se vrsi izracun pokrivenosti (datum valute) = " + dateFormat.format(value_date));
        
        // provjera da li je za datum valute i vrstu izraèuna veæ izvršena obrada
        if (!isTestMode && !bo821.checkPreconditions(value_date, proc_type))
        {
            error("Obrada za datum " + dateFormat.format(value_date) + " je vec pustena i uredno zavrsila!", null);
            return RemoteConstants.RET_CODE_ERROR;
        }
        
        // dohvat haircut parametra
        hfx = bo821.selectHaircutValue();
        if (hfx == null)
        {
            error("Haircut parametar nije definiran u korisnickoj parametrizaciji! (sifra = clt_hfx)", null);
            return RemoteConstants.RET_CODE_ERROR;
        }
        info("Haircut (hfx) = " + hfx + "%");
        
        // dohvat haircut parametra za garancije
        hfx_gar = bo821.selectHaircutGaranteeValue();
        if (hfx_gar == null)
        {
            error("Haircut parametar za garancije nije definiran u korisnickoj parametrizaciji! (sifra = clt_hfx_gar)", null);
            return RemoteConstants.RET_CODE_ERROR;
        }
        info("Haircut (hfx_gar) = " + hfx_gar + "%");
        
        // dohvat domaæe valute
        YFZD0 yfzd0 = new YFZD0(bc);
        domestic_cur_id = yfzd0.getDomCurId();
        domestic_code_char = yfzd0.getDomCurCodeChar();
        
        // dohvat teèajne liste za datum valute
        exchangeRate = yrxx0.getExchangeRate(value_date);
        bc.debug(yrxx0.exchangeRateToString(value_date));
        
        // uèitavanje svih kategorija kolaterala
        loadCollateralCategories();
        
        // uèitavanje svih vrsta kolaterala
        loadCollateralTypes();
        
        // uèitavanje svih podvrsta kolaterala
        loadCollateralSubtypes();
        
        // uèitavanje svih defaultnih pondera
        loadCollateralDefaultPonders();
        
        // uèitavanje kolaterala
        loadCollaterals();
        bc.debug("Ucitano " + collaterals.size() + " kolaterala.");
        
        // popunjavanje podvrste za kolaterale
        fillCollateralsSubtypeData();
        bc.debug("Ucitane podvrste za kolaterale.");
        
        // uèitavanje pondera za kolaterale
        if (allocationType.ponderType == PonderType.Ponder)
        {
            loadCollateralPonders();
            bc.debug("Ucitani ponderi za kolaterale.");
        }
        
        // uèitavanje podataka o izdavateljima garancija
        loadGuaranteeIssuers();
        bc.debug("Ucitani podaci o izdavateljima garancija.");
        
        // mapiranje kolaterala na pripadajuæe GCM tipove
        mapCollaterals();
        bc.debug("Kolaterali mapirani.");
        
        // uèitavanje CCF grupa
        loadCcfGroups();
        
        // uèitavanje B2 grupa
        loadB2Groups();
        
        // uèitavanje EIR parametara
        loadEirParams(); 
       
     // uèitavanje ARPOC parametara
        loadArpocParams();
        
        // uèitavanje okvirnih sporazuma
        loadFrameAgreements();
        bc.debug("Ucitano " + frameAgreements.size() + " okvirnih sporazuma.");
        
        // uèitavanje hipoteka
        loadMortgages();
        bc.debug("Ucitano " + mortgages.size() + " hipoteka.");
        
        // uèitavanje plasmana
        loadPlacements();
        bc.debug("Ucitano " + placements.size() + " plasmana (od toga " + frames.size() + " framea).");
        
        // uèitavanje ratinga i pripadajuæih PD vrijednosti
        loadRatingDefinitions();
        
        // uèitavanje ratinga za sve uèitane komitente
        bc.debug("Ucitano " + customers.size() + " komitenata.");
        if (!loadCustomerRatings()) return RemoteConstants.RET_CODE_ERROR;

        // uèitavanje veza kolateral-plasman
        loadCoverages();
        bc.debug("Ucitano " + coverages.size() + " veza kolateral-plasman.");
        
        // izdvajanje prihvatljivih veza kolateral-plasman
        filterCoverages();
        bc.debug(eligibleCoverages.size() + " veza kolateral-plasman su prihvatljive za izracun pokrivenosti.");
        
        // kreiranje virtualnih veza za hipoteke
        createVirtualCoverages();

        // identificiranje veza 1:1
        identifyOneOnOneRelationships();

        // uèitavanje CCF vrijednosti za plasmane
        determineCcf();
        bc.debug("Ucitane CCF vrijednosti za plasmane.");

        // postavljanje inicijalnih vrijednosti za izraèun
        setInitialValues();
        bc.debug("Postavljene inicijalne vrijednosti za izracun.");

        // ispis uèitanih podataka u programerski log
        printStructureToDebug();

        // utvrðivanje redoslijeda rasporeðivanja kolaterala na plasmane
        sortCoverages();
        bc.debug("Utvrdjen redoslijed rasporedjivanja kolaterala na plasmane.");

        // grupiranje pokrivanja plasmana kolateralom u grupe za alikvotni izraèun
        groupCoverages();

        // izraèun pokrivenosti
        calculateAllocation();
        
        // izraèun bez rezanja na iznos izloženosti
        if (allocationType.additionalType == AdditionalType.Unlimited)
        {
            distributeUnallocatedValues();
        }
        
        // izraèun Execution Valuea
        calculateExecutionValue();
        
        // izraèun realnog pondera
        if (allocationType.ponderType == PonderType.Ponder)
        {
            calculateRealPonder();
        }
        
        // zapiši izraèunate vrijednosti u bazu podataka
        writeAllocation();

        return retCode;
    }

    
    /**
     * Metoda vrši izraèun vrijednosti kojom kolaterali pokrivaju plasmane.
     * Pretpostavlja se da je prije poziva metode veæ obavljeno utvrðivanje redoslijeda izraèuna i grupiranje za alikvotni izraèun.
     * Metoda kao rezultat daje popunjene iznose pokrivanja za svaku vezu kolateral-plasman. 
     */
    private void calculateAllocation()
    {
        bc.startStopWatch("BO820.calculateAllocation");
        
        int groupNo = 1;
        
        for (ArrayList<CoverageData> allocationList : allocationGroups)
        {
            if (allocationList.size() <= 0) continue;
            
            // odredi kolateral, hipoteku i okvirni sporazum koji sudjeluju u izraèunu
            CoverageData firstAllocation = allocationList.get(0);
            CollateralData collateral = firstAllocation.collateral;
            MortgageData mortgage = firstAllocation.mortgage;
            FrameAgreementData frameAgreement = null;
            if (mortgage != null && mortgage.frameAgreement != null) frameAgreement = mortgage.frameAgreement;
            
            // dohvati sumu iznosa hipoteka višeg reda drugih banaka
            BigDecimal mortgageSum = (mortgage != null && mortgage.otherMortgageAmount != null) ? mortgage.otherMortgageAmount : zero;
            
            // hipoteka koja je dio okvirnog sporazuma se može tumaèiti kao hipoteka druge banke (zakljuèak sastanka 2.1.2014.) u iznosu iskorištenog dijela okvirnog sporazuma (zakljuèak sastanka 29.1.2014.)
            // BigDecimal mortgageSum2 = getSumOfHigherOrderFrameAgreementMortgagesAllocation(mortgage);  // izbaèeno 9.3.2016. prilikom promjene algoritma za vrijednost WCOVH

            // WCOVH = preostali dio vrijednosti kolaterala - suma iznosa hipoteka višeg reda drugih banaka
            BigDecimal wcovh = collateral.remaining_value.subtract(mortgageSum).max(zero);
            if (mortgage != null && mortgage.wcovh == null) mortgage.wcovh = wcovh;
            if (firstAllocation.isVirtual) continue;
            
            // WCCV = min(WCOWH, neiskorišteni dio okvirnog sporazuma)
            BigDecimal wccv = wcovh;
            if (frameAgreement != null) wccv = wcovh.min(frameAgreement.remaining_amount);
            
            // izraèunaj sumu izloženosti svih plasmana iz grupe
            BigDecimal groupPlacementRemainingExposureSum = zero;
            for (CoverageData allocation : allocationList)
            {
                groupPlacementRemainingExposureSum = groupPlacementRemainingExposureSum.add(allocation.placement.remaining_exposure);
            }
            
            // složi u string parametre s kojima se ulazi u izraèun
            StringBuffer buffer = new StringBuffer();
            buffer.append("GRUPA ").append(groupNo).append(":").append("\n");
            buffer.append("KOLATERAL ").append(collateral.col_num).append("\n");
            buffer.append("     WCOV = ").append(decimalFormat.format(collateral.wcov)).append("\n");
            buffer.append("        R = ").append(decimalFormat.format(collateral.remaining_value)).append("\n");
            buffer.append("        H = ").append(decimalFormat.format(mortgageSum)).append("\n");
            buffer.append("    WCOVH = ").append(decimalFormat.format(wcovh)).append("\n");
            if (frameAgreement != null) buffer.append("       F' = ").append(decimalFormat.format(frameAgreement.remaining_amount)).append("\n");
            buffer.append("     WCCV = ").append(decimalFormat.format(wccv)).append("\n");
            buffer.append("  suma P' = ").append(decimalFormat.format(groupPlacementRemainingExposureSum)).append("\n");
            
            // za svako pokrivanje iz grupe izraèunaj iznos pokrivenosti
            for (CoverageData allocation : allocationList)
            {
                allocation.priority = groupNo;
                PlacementData placement = allocation.placement;
                
                // odredi Hfx parametar koji se primjenjuje u izraèunu
                BigDecimal hfx = zero;
                if (!allocation.isCurrencyMatched && allocationType.ponderType == PonderType.Ponder) 
                {
                  if (collateral.category.col_cat_id.longValue()== 615223)
                  {
                     hfx = this.hfx_gar;  
                     bc.debug("HFXTEST1 kolateral: " + collateral.col_num + " kategorija " +collateral.category.col_cat_id + " hfx: " +hfx);
                  }
                  else
                  {
                    hfx = this.hfx;  
                  }
                }
           
                // izraèun je drugaèiji za sindicirane plasmane (RTC 14313)
                if (!allocation.isSindicated)
                {
                    // WCV = min(WCCV * (preostali dio plasmana / suma svih preostalih dijelova plasmana iz grupe) * (100% - Hfx), preostali dio plasmana)
                    if (groupPlacementRemainingExposureSum.compareTo(zero) > 0)
                    {
                        allocation.amount = wccv.multiply(placement.remaining_exposure).divide(groupPlacementRemainingExposureSum, 20, RoundingMode.HALF_UP).multiply(hundred.subtract(hfx).divide(hundred)).min(placement.remaining_exposure).setScale(2, RoundingMode.HALF_UP);
                    }
                    else
                    {
                        allocation.amount = zero;                        
                    }
                    
                    // izraèunaj iznos kolaterala potrošen za pokrivanje - razlikuje se od iznosa pokrivanja ako je Hfx > 0%
                    allocation.collateralAmountUsed = allocation.amount.divide(hundred.subtract(hfx).divide(hundred), 2, RoundingMode.HALF_UP).min(wccv);
                }
                else
                {
                    // WCV = min(WCCV * udio RBA * (100% - Hfx), preostali dio plasmana)
                    // pretpostavka je da se na hipoteku za sindicirani plasman može vezati samo jedan plasman - zato se ne koristi groupPlacementRemainingExposureSum
                    allocation.amount = wccv.multiply(mortgage.rba_sindic_part.divide(hundred)).multiply(hundred.subtract(hfx).divide(hundred)).min(placement.remaining_exposure).setScale(2, RoundingMode.HALF_UP);

                    // WCVs = min(WCCV * udio drugih banaka, preostali dio iznosa hipoteke drugih banaka)
                    allocation.amount_other_sindic = wccv.multiply(mortgage.other_sindic_part.divide(hundred)).min(mortgage.remaining_other_sindic_amount).setScale(2, RoundingMode.HALF_UP);

                    // izraèunaj iznos kolaterala potrošen za pokrivanje - razlikuje se od iznosa pokrivanja ako je Hfx > 0%
                    allocation.collateralAmountUsed = allocation.amount.divide(hundred.subtract(hfx).divide(hundred), 2, RoundingMode.HALF_UP).add(allocation.amount_other_sindic).min(wccv);
                }

                // raspodijeli iznos na bilancu i vanbilancu
                allocation.amount_balance = placement.remaining_exposure_balance.min(allocation.amount);
                allocation.amount_off_balance = placement.remaining_exposure_off_balance.min(allocation.amount.subtract(allocation.amount_balance));
                placement.remaining_exposure_balance = placement.remaining_exposure_balance.subtract(allocation.amount_balance);
                placement.remaining_exposure_off_balance = placement.remaining_exposure_off_balance.subtract(allocation.amount_off_balance);
                
                // pribroji iznos u pripadni iznos iskorištenosti po valutnoj usklaðenosti
                if (allocation.isCurrencyMatched) collateral.amountUsedForCurrencyMatchedPlacements = collateral.amountUsedForCurrencyMatchedPlacements.add(allocation.amount);
                else collateral.amountUsedForCurrencyMismatchedPlacements = collateral.amountUsedForCurrencyMismatchedPlacements.add(allocation.amount);
                
                // složi tijek izraèuna u string
                buffer.append("-> PLASMAN ").append(placement.cus_acc_no).append("   ").append(allocation).append("\n");
                buffer.append("       P' = ").append(decimalFormat.format(placement.remaining_exposure)).append("\n");
                if (allocation.isSindicated) buffer.append("       S' = ").append(decimalFormat.format(allocation.mortgage.remaining_other_sindic_amount)).append("\n");
                buffer.append("      Hfx = ").append(hfx).append("%").append("\n");
                buffer.append("      WCV = ").append(decimalFormat.format(allocation.amount)).append(" (").append("bilanca = ").append(decimalFormat.format(allocation.amount_balance)).append(" + vanbilanca = ").append(decimalFormat.format(allocation.amount_off_balance)).append(")").append("\n");
                if (allocation.isSindicated) buffer.append("     WCVs = ").append(decimalFormat.format(allocation.amount_other_sindic)).append("\n");
                
                // umanji preostali dio kolaterala, plasmana i okvirnog sporazuma za izraèunatu pokrivenost 
                collateral.remaining_value = collateral.remaining_value.subtract(allocation.collateralAmountUsed);
                placement.remaining_exposure = placement.remaining_exposure.subtract(allocation.amount);
                if (frameAgreement != null) frameAgreement.remaining_amount = frameAgreement.remaining_amount.subtract(allocation.collateralAmountUsed).max(zero);
                if (allocation.isSindicated)
                {
                    for (CoverageData coverage : placement.eligibleCoverages)  // sve sindicirane hipoteke povezane na plasman se moraju promatrati kao jedna sindicirana hipoteka - pretpostavka je da se na sindiciranu hipoteku može vezati samo jedan plasman
                    {
                        MortgageData sindicatedMortgage = coverage.mortgage;
                        if (sindicatedMortgage == null || !sindicatedMortgage.isSindicated) continue;
                        sindicatedMortgage.remaining_other_sindic_amount = sindicatedMortgage.remaining_other_sindic_amount.subtract(allocation.amount_other_sindic);
                    }
                }
                
                // složi vrijednosti poslije izraèuna u string
                buffer.append("    poslije izracuna:").append("\n");
                buffer.append("        R = ").append(decimalFormat.format(collateral.remaining_value)).append("\n");
                buffer.append("       P' = ").append(decimalFormat.format(placement.remaining_exposure)).append("\n");
                if (allocation.isSindicated) buffer.append("       S' = ").append(decimalFormat.format(allocation.mortgage.remaining_other_sindic_amount)).append("\n");
                if (frameAgreement != null) buffer.append("       F' = ").append(decimalFormat.format(frameAgreement.remaining_amount)).append("\n");
            }
            
            // ispiši u programerski log string koji opisuje izraèun
            bc.debug(buffer.toString());
            groupNo++;
        }
        
        bc.stopStopWatch("BO820.calculateAllocation");
    }
    
   
    /**
     * Metoda vrši izraèun bez rezanja na iznos izloženosti.
     */
    private void distributeUnallocatedValues()
    {
        bc.startStopWatch("BO820.distributeUnallocatedValues");
        bc.debug("Dodatna raspodjela:");

        for (CollateralData collateral : collaterals.values())
        {
            if (collateral.eligibleCoverages.size() == 0) continue;

            BigDecimal remainingValue = collateral.remaining_value;
            if (collateral.lastRbaMortgage != null && collateral.lastRbaMortgage.otherMortgageAmount != null) remainingValue = remainingValue.subtract(collateral.lastRbaMortgage.otherMortgageAmount);
            if (remainingValue.compareTo(zero) <= 0) continue;

            BigDecimal totalAllocatedValue = zero;
            BigDecimal totalExposure = zero;
            for (CoverageData allocation : collateral.eligibleCoverages)
            {
                totalAllocatedValue = totalAllocatedValue.add(allocation.collateralAmountUsed);
                totalExposure = totalExposure.add(allocation.placement.exposure);
            }
            if (totalAllocatedValue.compareTo(zero) <= 0 && totalExposure.compareTo(zero) <= 0) continue;

            StringBuffer buffer = new StringBuffer();
            buffer.append("KOLATERAL ").append(collateral.col_num).append(", R = ").append(decimalFormat.format(collateral.remaining_value)).append(", suma WCV = ").append(decimalFormat.format(totalAllocatedValue));

            for (CoverageData allocation : collateral.eligibleCoverages)
            {
                if (allocation.isVirtual) continue;

                MortgageData mortgage = allocation.mortgage;
                FrameAgreementData frameAgreement = null;
                if (mortgage != null && mortgage.frameAgreement != null) frameAgreement = mortgage.frameAgreement;
                PlacementData placement = allocation.placement;

                buffer.append("\n");
                if (mortgage != null) buffer.append(" -> HIPOTEKA ").append(mortgage.priority);
                if (frameAgreement != null) buffer.append(" (DS ").append(frameAgreement.agreement_no).append(" F' = ").append(decimalFormat.format(frameAgreement.remaining_amount)).append(")");
                buffer.append(" -> PLASMAN ").append(placement.cus_acc_no);
                buffer.append(" -> WCV = ").append(decimalFormat.format(allocation.collateralAmountUsed));

                BigDecimal ratio;
                if (totalAllocatedValue.compareTo(zero) > 0) ratio = allocation.collateralAmountUsed.divide(totalAllocatedValue, 20, RoundingMode.HALF_UP);
                else ratio = allocation.placement.exposure.divide(totalExposure, 20, RoundingMode.HALF_UP);
                
                BigDecimal additionalValue = remainingValue.multiply(ratio).setScale(2, RoundingMode.HALF_UP).min(collateral.remaining_value);
          //    35239 potrebno je promijeniti izraèun pokrivenosti bez rezanja na iznos izloženosti da iznos pokrivenosti nije ogranièen iznosom DS-a. 
          //    if (frameAgreement != null && additionalValue.compareTo(frameAgreement.remaining_amount) > 0) additionalValue = frameAgreement.remaining_amount;
                buffer.append(" -> WCV+ = ").append(decimalFormat.format(additionalValue));
                if (additionalValue.compareTo(zero) <= 0) continue;

                BigDecimal hfx = zero;
                if (!allocation.isCurrencyMatched && allocationType.ponderType == PonderType.Ponder) 
                {
                if (collateral.category.col_cat_id.longValue()== 615223)
                  {
                     hfx = this.hfx_gar;  
                     bc.debug("HFXTEST2 kolateral: " + collateral.col_num + " kategorija " +collateral.category.col_cat_id + " hfx: " +hfx);
                  }
                  else
                  {
                    hfx = this.hfx;  
                  }
                }
                
                BigDecimal additionalHaircutValue = additionalValue.multiply(hundred.subtract(hfx).divide(hundred)).setScale(2, RoundingMode.HALF_UP);

                allocation.collateralAmountUsed = allocation.collateralAmountUsed.add(additionalValue);
                allocation.amount = allocation.amount.add(additionalHaircutValue);

                if (allocation.isCurrencyMatched) collateral.amountUsedForCurrencyMatchedPlacements = collateral.amountUsedForCurrencyMatchedPlacements.add(additionalHaircutValue);
                else collateral.amountUsedForCurrencyMismatchedPlacements = collateral.amountUsedForCurrencyMismatchedPlacements.add(additionalHaircutValue);

                BigDecimal total_balance = allocation.amount_balance.add(allocation.amount_off_balance);
                if (total_balance.compareTo(zero) > 0)
                {
                    BigDecimal additional_amount_balance = allocation.amount_balance.divide(total_balance, 20, RoundingMode.HALF_UP).multiply(additionalHaircutValue).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal additional_amount_off_balance = allocation.amount_off_balance.divide(total_balance, 20, RoundingMode.HALF_UP).multiply(additionalHaircutValue).setScale(2, RoundingMode.HALF_UP);
                    allocation.amount_balance = allocation.amount_balance.add(additional_amount_balance);
                    allocation.amount_off_balance = allocation.amount_off_balance.add(additional_amount_off_balance);
                    placement.remaining_exposure_balance = placement.remaining_exposure_balance.subtract(additional_amount_balance);
                    placement.remaining_exposure_off_balance = placement.remaining_exposure_off_balance.subtract(additional_amount_off_balance);
                }

                collateral.remaining_value = collateral.remaining_value.subtract(additionalValue);
                placement.remaining_exposure = placement.remaining_exposure.subtract(additionalHaircutValue);
                if (frameAgreement != null) frameAgreement.remaining_amount = frameAgreement.remaining_amount.subtract(additionalValue).max(zero);
                // izraèun Undiscounted WCV 
                
                BigDecimal eir = zero;
                BigDecimal arpoc = zero;
                String b2Group = getB2Group(placement.owner.basel_cus_type.trim() );
                eir = getEirParam (b2Group);
                arpoc = getArpocParam (b2Group);
                Double eird=eir.divide(hundred).doubleValue();
                Double arpocd=arpoc.doubleValue();
                  //              WCV Adjustment=min[1/ponder; (1 + EIR)ARPOC]
                BigDecimal wcv_adjustment = new BigDecimal(Math.pow(1d + eird, arpocd));
        
                BigDecimal wcv_adjustment_ncv = null;
                bc.debug("PONDER wcv_adjustment_ncv: " + collateral.ponder);
                if (collateral.ponder == null || collateral.ponder.compareTo(zero) == 0)
                {  
                    wcv_adjustment_ncv =  wcv_adjustment; 
                }   
                 else
                 {
                     wcv_adjustment_ncv =  (one.divide(collateral.ponder.divide(hundred, 2, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP)).min(wcv_adjustment); 
                 }
                allocation.undiscounted_wcv = allocation.amount.multiply(wcv_adjustment_ncv).setScale(2, RoundingMode.HALF_UP);
                buffer.append("  wcv_adjustment' = ").append(decimalFormat.format(wcv_adjustment)).append("\n");
                buffer.append("  wcv_adjustment_ncv' = ").append(decimalFormat.format(wcv_adjustment_ncv)).append("\n");
                buffer.append("  undiscounted_wcv' = ").append(decimalFormat.format(allocation.undiscounted_wcv)).append("\n");
             
            }

            bc.debug(buffer.append("\n").toString());
        }

        bc.stopStopWatch("BO820.distributeUnallocatedValues");
    }
    
    
    /**
     * Metoda raèuna Execution Value.
     */
    private void calculateExecutionValue() throws Exception
    {
        bc.startStopWatch("BO820.calculateExecutionValue");

        int iterationNumber = 0;
        boolean isStuck;
        boolean isAllCalculated;

        do
        {
            iterationNumber++;
            isStuck = iterationNumber > 20;
            
            if (isStuck) warn("Izracun EV zapeo...");
            bc.debug("\n\nIzracun EV iteracija #" + iterationNumber + "\n\n");

            isAllCalculated = true;
            
            for (CollateralData collateral : collaterals.values())
            {
                if (collateral.mortgages.size() == 0) continue;
                
                boolean isCalculationAttempted = false;
                
                StringBuffer buffer = new StringBuffer();
                buffer.append("\n").append("   KOLATERAL ").append(collateral.col_num).append(collateral.isEligible ? " (prihvatljiv)" : " (neprihvatljiv)");
                
                if (collateral.isEligible)  // prihvatljivi kolaterali
                {
                    for (MortgageData mortgage : collateral.mortgages.values())
                    {
                        if (!mortgage.isRBA || mortgage.execution_value != null) continue;  // preskoèi ako nije RBA hipoteka ili ako je EV veæ izraèunat
                        
                        isCalculationAttempted = true;
                        
                        buffer.append("\n   |   HIPOTEKA ").append(mortgage.priority);
                        if (mortgage.frameAgreement != null) buffer.append(" (DS ").append(mortgage.frameAgreement.agreement_no).append(")");
                        buffer.append(": ");
                        
                        BigDecimal ev1 = null, ev2 = null, ev3 = null;
                        
                        // NCV - prethodno iskorišteno
                        BigDecimal ncvLeftover = ncvLeftover(mortgage);
                        if (ncvLeftover == null)
                        {
                            buffer.append("\n   /   Privremeno staje izracun EV za kolateral ").append(collateral.col_num).append(" na hipoteci #").append(mortgage.priority);
                            isAllCalculated = false;
                            break;
                        }
                        ev1 = ncvLeftover;
                        buffer.append("EV1 = ").append(decimalFormat.format(ev1)).append(" ").append(domestic_code_char);

                        // hipoteka dio DS
                        if (mortgage.frameAgreement != null)
                        {
                            BigDecimal ratio;  // udio hipoteke u DS

                            if (!isStuck)
                            {
                                BigDecimal leftoverSum = zero;  // suma NCV - prethodno iskorišteno svih hipoteka iz DS
                                for (MortgageData faMortgage : mortgage.frameAgreement.mortgages)
                                {
                                    BigDecimal leftover = ncvLeftover(faMortgage);
                                    if (leftover == null)
                                    {
                                        buffer.append("\n   /   Privremeno staje izracun EV za kolateral ").append(collateral.col_num).append(" na hipoteci #").append(mortgage.priority).append(" iz DS ").append(mortgage.frameAgreement.agreement_no).append(" -> hipoteka #").append(faMortgage.priority).append(" kolaterala ").append(faMortgage.collateral.col_num).append(" nema izracunat EV");
                                        leftoverSum = null;
                                        break;
                                    }
                                    leftoverSum = leftoverSum.add(leftover);
                                }
                                if (leftoverSum == null)
                                {
                                    isAllCalculated = false;
                                    break;
                                }
                                
                                if (leftoverSum.compareTo(zero) != 0) ratio = ncvLeftover.divide(leftoverSum, 20, RoundingMode.HALF_UP);
                                else ratio = zero;
                            }
                            else  // izraèun zapeo, rasporedi alikvotno
                            {
                                ratio = one.divide(new BigDecimal(mortgage.frameAgreement.collaterals.size()), 20, RoundingMode.HALF_UP);
                            }

                            ev2 = mortgage.frameAgreement.amount.multiply(evfx).multiply(ratio).setScale(2, RoundingMode.HALF_UP);
                            buffer.append(", EV2 = ").append(decimalFormat.format(ev2)).append(" ").append(domestic_code_char);
                        }
                        
                        // zadnja RBA hipoteka
                        if (!mortgage.isLastRBA)
                        {
                            BigDecimal usedValue = zero;  // iskorišteni iznos kolaterala ili DS
                            
                            if (mortgage.frameAgreement != null)  // DS
                            {
                                for (CoverageData coverage : collateral.eligibleCoverages)
                                {
                                    if (coverage.mortgage != null && coverage.mortgage.frameAgreement == mortgage.frameAgreement)
                                    {
                                        usedValue = usedValue.add(coverage.amount);
                                    }
                                }
                            }
                            else  // kolateral
                            {
                                for (CoverageData coverage : collateral.eligibleCoverages)
                                {
                                    if (coverage.mortgage == mortgage)
                                    {
                                        usedValue = usedValue.add(coverage.amount);
                                    }
                                }
                            }
    
                            ev3 = usedValue.multiply(evfx);
                            buffer.append(", EV3 = ").append(decimalFormat.format(ev3)).append(" ").append(domestic_code_char);
                        }

                        BigDecimal ev = ev1;
                        if (ev2 != null) ev = ev.min(ev2);
                        if (ev3 != null) ev = ev.min(ev3);
                        mortgage.execution_value = ev;
                        
                        buffer.append(" -> EV = ").append(decimalFormat.format(mortgage.execution_value)).append(" ").append(domestic_code_char);
                    }
                }
                else  // neprihvatljivi kolaterali
                {
                    boolean isEvSet = false;
                    
                    for (MortgageData mortgage : collateral.mortgages.values())
                    {
                        if (!mortgage.isRBA || mortgage.execution_value != null) continue;  // preskoèi ako nije RBA hipoteka ili ako je EV veæ izraèunat
                        
                        isCalculationAttempted = true;
                        
                        buffer.append("\n   |   HIPOTEKA ").append(mortgage.priority);
                        if (mortgage.frameAgreement != null) buffer.append(" (DS ").append(mortgage.frameAgreement.agreement_no).append(")");
                        buffer.append(": ");

                        // da li hipoteka ima povezan aktivni plasman
                        boolean mortgageHasActivePlacement = false;
                        for (CoverageData coverage : mortgage.collateral.coverages)
                        {
                            if (coverage.mortgage == mortgage)
                            {
                                mortgageHasActivePlacement = true;
                                break;
                            }
                        }

                        if (mortgageHasActivePlacement && !isEvSet)
                        {
                            buffer.append("> ");
                            mortgage.execution_value = collateral.ncv.subtract(mortgage.otherMortgageAmount).max(zero);
                            isEvSet = true;
                        }
                        else
                        {
                            mortgage.execution_value = zero;
                        }
                        
                        buffer.append("EV = ").append(decimalFormat.format(mortgage.execution_value)).append(" ").append(domestic_code_char);
                    }
                }
                
                if (isCalculationAttempted) bc.debug(buffer.append("\n").toString());
            }
        }
        while (!isAllCalculated);

        bc.info("Broj iteracija EV: " + iterationNumber);
        bc.stopStopWatch("BO820.calculateExecutionValue");
    }
    
    
    /**
     * Metoda raèuna iznos NCV - suma iznosa EV hipoteka višeg reda od zadane hipoteke. 
     * @param mortgage hipoteka za koju se raèuna iznos
     */
    private BigDecimal ncvLeftover(MortgageData mortgage)
    {
        BigDecimal sum_previous_ev = zero;
        
        for (MortgageData otherMortgage : mortgage.collateral.mortgages.values())
        {
            if (otherMortgage == mortgage || otherMortgage.priority >= mortgage.priority) break;
            
            if (!otherMortgage.isRBA) continue;
            
            if (otherMortgage.execution_value == null) return null;
            
            sum_previous_ev = sum_previous_ev.add(otherMortgage.execution_value);
        }
        
        BigDecimal previous_used = mortgage.otherMortgageAmount.add(sum_previous_ev);
        
        return mortgage.collateral.ncv.subtract(previous_used).max(zero);
    }
 
    
    
    /** 
     * Metoda raèuna realan ponder za sve kolaterale. 
     */
    private void calculateRealPonder()
    {
        bc.startStopWatch("BO820.calculateRealPonder");
        
        for (CollateralData collateral : collaterals.values())
        {
            if (collateral.amountUsedForCurrencyMismatchedPlacements.compareTo(zero) <= 0)
            {
                collateral.real_ponder = collateral.ponder;
            }
            else
            {
                BigDecimal hfx = zero;
                if (collateral.category.col_cat_id.longValue()== 615223)
                  {
                     hfx = this.hfx_gar;  
                     bc.debug("HFXTESTPONDER kolateral: " + collateral.col_num + " kategorija " +collateral.category.col_cat_id + " hfx: " +hfx);
                  }
                  else
                  {
                    hfx = this.hfx;  
                  }
              
                
                // realan ponder = ponder * (PKVU + PKVN) / [PKVU + PKVN / (1 - HFX)]
                BigDecimal brojnik = collateral.amountUsedForCurrencyMatchedPlacements.add(collateral.amountUsedForCurrencyMismatchedPlacements);
                BigDecimal nazivnik = collateral.amountUsedForCurrencyMatchedPlacements.add(collateral.amountUsedForCurrencyMismatchedPlacements.divide(hundred.subtract(hfx).divide(hundred), 20, RoundingMode.HALF_UP));
                collateral.real_ponder = collateral.ponder.multiply(brojnik).divide(nazivnik, 2, RoundingMode.HALF_UP);
                if (collateral.amountUsedForCurrencyMismatchedPlacements.compareTo(zero) > 0)
                {
                    if (collateral.category.col_cat_id.longValue()== 615223)
                    {
                       bc.debug("HFXTESTPONDER_NAKON kolateral: " + collateral.col_num + " kategorija " +collateral.category.col_cat_id + " hfx: " +hfx + " REL PONDER: " +collateral.real_ponder);  
                    }
                }
            }
        }
        
        bc.stopStopWatch("BO820.calculateRealPonder");
    }


    /**
     * Metoda zapisuje izraèunate vrijednosti pokrivenosti u bazu podataka. 
     */
    private void writeAllocation() throws Exception
    {
        BigDecimal col_pro_id = null;
        
        if (!isTestMode)
        {
            bc.beginTransaction();
            col_pro_id = bo821.insertIntoColProc(value_date, proc_type);
        }
        
        int writtenEligibleCoveragesCount = 0;
        for (CoverageData coverage : eligibleCoverages)
        {
            if (coverage.isVirtual) continue;
            if (!isTestMode) bo821.insertIntoCusaccExpColl(coverage, coverage.collateral, coverage.mortgage, coverage.placement, col_pro_id, value_date, yrxx0, domestic_cur_id, allocationType);
            coverage.isWrittenToDatabase = true;
            coverage.collateral.isWrittenToDatabase = true;
            coverage.placement.isWrittenToDatabase = true;
            if (coverage.mortgage != null) coverage.mortgage.isWrittenToDatabase = true;
            writtenEligibleCoveragesCount++;
        }
        bc.debug("Broj zapisa za prihvatljive pokrivenosti: " + writtenEligibleCoveragesCount);
        
        int writtenOtherCoveragesCount = 0;
        for (CoverageData coverage : coverages)
        {
            if (coverage.isWrittenToDatabase) continue;
            if (!isTestMode) bo821.insertIntoCusaccExpColl(coverage, coverage.collateral, coverage.mortgage, coverage.placement, col_pro_id, value_date, yrxx0, domestic_cur_id, allocationType);
            coverage.isWrittenToDatabase = true;
            coverage.collateral.isWrittenToDatabase = true;
            coverage.placement.isWrittenToDatabase = true;
            if (coverage.mortgage != null) coverage.mortgage.isWrittenToDatabase = true;
            writtenOtherCoveragesCount++;
        }
        bc.debug("Broj zapisa za ostale pokrivenosti: " + writtenOtherCoveragesCount);
        
        int writtenPlacementsCount = 0;
        for (PlacementData placement : placements.values())
        {
            if (placement.isWrittenToDatabase) continue;
            if (!isTestMode) bo821.insertIntoCusaccExpColl(null, null, null, placement, col_pro_id, value_date, yrxx0, domestic_cur_id, allocationType);
            placement.isWrittenToDatabase = true;
            writtenPlacementsCount++;
        }
        bc.debug("Broj zapisa za plasmane bez pokrivenosti: " + writtenPlacementsCount);
        
        int writtenMortgagesCount = 0;
        for (CollateralData collateral : collaterals.values())
        {
            for (MortgageData mortgage : collateral.mortgages.values())
            {
                if (mortgage.isWrittenToDatabase) continue;
                if (!isTestMode) bo821.insertIntoCusaccExpColl(null, collateral, mortgage, null, col_pro_id, value_date, yrxx0, domestic_cur_id, allocationType);
                mortgage.isWrittenToDatabase = true;
                collateral.isWrittenToDatabase = true;
                writtenMortgagesCount++;
            }
        }
        bc.debug("Broj zapisa za hipoteke bez pokrivenosti: " + writtenMortgagesCount);
        
        int writtenCollateralsCount = 0;
        for (CollateralData collateral : collaterals.values())
        {
            if (collateral.isWrittenToDatabase) continue;
            if (!isTestMode) bo821.insertIntoCusaccExpColl(null, collateral, null, null, col_pro_id, value_date, yrxx0, domestic_cur_id, allocationType);
            collateral.isWrittenToDatabase = true;
            writtenCollateralsCount++;
        }
        bc.debug("Broj zapisa za kolaterale bez pokrivenosti: " + writtenCollateralsCount);
        
        int totalCount = writtenEligibleCoveragesCount + writtenOtherCoveragesCount + writtenPlacementsCount + writtenMortgagesCount + writtenCollateralsCount;
        bc.debug("Ukupan broj zapisa: " + totalCount);
        
        if (!isTestMode)
        {
            bo821.updateColProc(col_pro_id, totalCount);
            bc.commitTransaction();
            info("U bazu podataka zapisano " + totalCount + " slogova.");
        }
        
        bc.setCounter(totalCount);
    }


    /**
     * Metoda izdvaja pokrivanja plasmana kolateralom koja su prihvatljiva za izraèun pokrivenosti.
     */
    private void filterCoverages() throws Exception
    {
        bc.startStopWatch("BO820.filterCoverages");
        
        eligibleCoverages.clear();
        
        for (CoverageData coverage : coverages)
        {
            if (coverage.collateral.isEligibleFor(allocationType) && coverage.placement.isEligibleFor(allocationType))
            {
                eligibleCoverages.add(coverage);
                coverage.collateral.eligibleCoverages.add(coverage);
                coverage.placement.eligibleCoverages.add(coverage);
            }
        }
        
        bc.stopStopWatch("BO820.filterCoverages");
    }
    
    
    /**
     * Metoda kreira virtualne veze za hipoteke prihvatljivih kolaterala koje nemaju povezan nijedan plasman.
     */
    private void createVirtualCoverages() throws Exception
    {
        bc.startStopWatch("BO820.createVirtualCoverages");
        
        int totalCount = 0;
        
        for (MortgageData mortgage : mortgages.values())
        {
            if (!mortgage.collateral.isEligibleFor(allocationType)) continue;
            
            int count = 0;
            for (CoverageData coverage : mortgage.collateral.eligibleCoverages)
            {
                if (coverage.mortgage == mortgage) count++;
            }
            
            if (count == 0)
            {
                CoverageData coverage = new CoverageData();
                coverage.isVirtual = true;
                coverage.isOneOnOneRelationship = false;
                coverage.collateral = mortgage.collateral;
                coverage.mortgage = mortgage;
                coverage.placement = null;
                eligibleCoverages.add(coverage);
                totalCount++;
            }
        }
        
        bc.debug("Dodano " + totalCount + " virtualnih veza.");
        
        bc.stopStopWatch("BO820.createVirtualCoverages");
    }
    
    
    /**
     * Metoda grupira pokrivanja plasmana kolateralom u grupe za alikvotni izraèun.
     * Ako isti kolateral pokriva više jednako vrijednih plasmana, tj. jednakih po svim kriterijima, kolateral se rasporeðuje alikvotno.
     */
    private void groupCoverages()
    {
        bc.startStopWatch("BO820.groupCoverages");
        
        ArrayList<CoverageData> allocationList = null;
        CoverageData oldCoverage = null;
        
        for (CoverageData coverage : eligibleCoverages)
        {
            if (coverage.compareTo(oldCoverage) != 0)
            {
                allocationList = new ArrayList<CoverageData>();
                this.allocationGroups.add(allocationList);
            }
            
            allocationList.add(coverage);
            oldCoverage = coverage;
        }
        
        bc.debug("Broj grupa za izracun: " + allocationGroups.size());
        bc.stopStopWatch("BO820.groupCoverages");
    }
    
    
    /**
     * Metoda dohvaæa sumu iznosa hipoteka drugih banaka višeg reda od reda zadane hipoteke. 
     * @param mortgage referentna hipoteka 
     * @return suma iznosa hipoteka
     */
    private BigDecimal getSumOfHigherOrderForeignMortgages(MortgageData mortgage)
    {
        if (mortgage == null || !mortgage.isRBA) return zero;
        
        BigDecimal sum = zero;
        for (MortgageData otherMortgage : mortgage.collateral.mortgages.values())
        {
            if (otherMortgage == mortgage || otherMortgage.priority >= mortgage.priority) break;
            
            if (!otherMortgage.isRBA)
            {
                sum = sum.add(otherMortgage.amount);
            }
        }
        return sum;
    }
    
    
    /**
     * Metoda dohvaæa sumu iskorištenih dijelova okvirnih sporazuma hipoteka višeg reda od zadane hipoteke. 
     * @param mortgage referentna hipoteka
     * @return suma iznosa
     */
    private BigDecimal getSumOfHigherOrderFrameAgreementMortgagesAllocation(MortgageData mortgage)
    {
        if (mortgage == null) return zero;
        
        BigDecimal sum = zero;
        for (MortgageData otherMortgage : mortgage.collateral.mortgages.values())
        {
            if (otherMortgage == mortgage || otherMortgage.priority >= mortgage.priority) break;
            
            if (mortgage.isRBA && otherMortgage.frameAgreement != null)
            {
                sum = sum.add(otherMortgage.frameAgreement.amount.subtract(otherMortgage.frameAgreement.remaining_amount));
                for (CoverageData coverage : otherMortgage.collateral.eligibleCoverages)  // potrebno je oduzeti iznos koji je veæ iskorišten za pokrivanje jer je on veæ uzet u obzir prilikom raèunanja neiskorištenog dijela kolaterala 
                {
                    if (coverage.mortgage == otherMortgage && coverage.collateralAmountUsed != null) sum = sum.subtract(coverage.collateralAmountUsed);
                }
            }
        }
        return sum.max(zero);
    }
    
    
    
    /**
     * Metoda postavlja inicijalne vrijednosti potrebne za izraèun za sve kolaterale i plasmane.
     */
    private void setInitialValues() throws Exception
    {
        bc.startStopWatch("BO820.setInitialValues");
        
        for (CollateralData collateral : collaterals.values())
        {
            collateral.isEligible = collateral.isEligibleFor(allocationType);
            
            if (allocationType.ponderType == PonderType.Ponder)
            {
                collateral.wcov = collateral.ncv.multiply(collateral.ponder).divide(hundred, 2, RoundingMode.HALF_UP);
            }
            else
            {
                collateral.wcov = collateral.ncv;
            }
            
            if (collateral.isEligible)
            {
                collateral.wcovh = collateral.wcov;
                if (collateral.firstRbaMortgage != null && collateral.firstRbaMortgage.otherMortgageAmount != null)
                {
                    collateral.wcovh = collateral.wcov.subtract(collateral.firstRbaMortgage.otherMortgageAmount).max(zero);
                }
            }
            else
            {
                collateral.wcovh = zero;
            }
            
            collateral.eligibility = collateral.isEligible ? "D" : "N";
            collateral.remaining_value = collateral.wcov;
            collateral.amountUsedForCurrencyMatchedPlacements = zero;
            collateral.amountUsedForCurrencyMismatchedPlacements = zero;
            collateral.real_ponder = null;
            collateral.isWrittenToDatabase = false;
        }
        
        for (MortgageData mortgage : mortgages.values())
        {
            mortgage.wcovh = null;
            if (mortgage.isRBA && !mortgage.collateral.isEligible) mortgage.wcovh = zero;
            mortgage.execution_value = null;
            if (mortgage.isSindicated) mortgage.remaining_other_sindic_amount = mortgage.other_sindic_amount;
            mortgage.isWrittenToDatabase = false;
        }
        
        for (PlacementData placement : placements.values())
        {
            placement.remaining_exposure = placement.exposure;
            placement.remaining_exposure_balance = placement.exposure.subtract(placement.exp_off_bal_lcy);
            placement.remaining_exposure_off_balance = placement.exp_off_bal_lcy;
            placement.isWrittenToDatabase = false;
        }
        
        for (FrameAgreementData frameAgreement : frameAgreements.values())
        {
            frameAgreement.remaining_amount = frameAgreement.amount;
        }
        
        for (CoverageData coverage : coverages)
        {
            coverage.amount = zero;
            coverage.amount_balance = zero;
            coverage.amount_off_balance = zero;
            coverage.amount_other_sindic = zero;
            coverage.collateralAmountUsed = zero;
            coverage.isWrittenToDatabase = false;
        }
        
        bc.stopStopWatch("BO820.setInitialValues");
    }
    
    
    /**
     * Metoda utvrðuje redoslijed rasporeðivanja kolaterala na plasmane.
     */
    private void sortCoverages()
    {
        bc.startStopWatch("BO820.sortCoverages");
        Collections.sort(eligibleCoverages);
        bc.stopStopWatch("BO820.sortCoverages");
    }
    

    /**
     * Metoda identificira sve veze 1:1, tj. sluèajeve kada je kolateral jednom hipotekom
     * (ili bez hipoteke u sluèaju kolaterala koji nemaju hipoteke) povezan na samo jedan plasman.
     */
    private void identifyOneOnOneRelationships() throws Exception
    {
        bc.startStopWatch("BO820.identifyOneOnOneRelationships");
        
        int oneOnOneCount = 0;
        for (CoverageData coverage : eligibleCoverages)
        {
            if (coverage.collateral.eligibleCoverages.size() == 1)
            {
                coverage.isOneOnOneRelationship = true;
                oneOnOneCount++;
            }
            else
            {
                coverage.isOneOnOneRelationship = false;
            }
        }
        
        bc.debug("Broj veza 1:1 = " + oneOnOneCount);
        bc.stopStopWatch("BO820.identifyOneOnOneRelationships");
    }



    /**
     * Metoda ispisuje uèitane podatke u programerski log.
     */
    private void printStructureToDebug()
    {
        bc.startStopWatch("BO820.printStructureToDebug");
        
        for (PlacementData placement : placements.values())
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("\n");
            buffer.append("   PLASMAN ").append(placement.cus_acc_no);
            if (placement.frame != null) buffer.append(" (iz framea ").append(placement.frame.cus_acc_no).append(")");
            buffer.append(": izlozenost=").append(decimalFormat.format(placement.exposure_balance)).append(" ").append(exchangeRate.get(placement.exposure_cur_id).code_char);
            if (placement.exposure_cur_id.compareTo(domestic_cur_id) != 0) buffer.append("=").append(decimalFormat.format(placement.exposure)).append(" ").append(domestic_code_char);
            if (placement.isCurrencyClause) buffer.append(" (valutna klauzula u ").append(exchangeRate.get(placement.contract_cur_id).code_char).append(")");
            buffer.append(" od toga bilanca=").append(decimalFormat.format(placement.remaining_exposure_balance)).append(" ").append(exchangeRate.get(domestic_cur_id).code_char);
            buffer.append(" i vanbilanca=").append(decimalFormat.format(placement.remaining_exposure_off_balance)).append(" ").append(exchangeRate.get(domestic_cur_id).code_char);
            buffer.append(", vlasnik=").append(placement.owner.register_no);
            buffer.append(", PD=").append(placement.owner.rating.pd);
            buffer.append(", modul=").append(placement.module_code);
            buffer.append(", CCF=").append(placement.ccf).append("%");
            
            HashSet<CollateralData> collaterals = new HashSet<CollateralData>();
            for (CoverageData coverage : placement.eligibleCoverages)
            {
                if (!collaterals.contains(coverage.collateral) && coverage.isDirectlyRelatedToPlacement) collaterals.add(coverage.collateral);
            }
            
            for (CollateralData collateral : collaterals)
            {
                buffer.append("\n   |");
                if (isPlacementCoveredViaCollateralDirectly(placement, collateral)) buffer.append("-> "); else buffer.append("   ");
                buffer.append("KOLATERAL ").append(collateral.col_num);
                buffer.append(": NCV=").append(decimalFormat.format(collateral.real_est_nomi_valu)).append(" ").append(exchangeRate.get(collateral.real_est_nm_cur_id).code_char);
                if (collateral.real_est_nm_cur_id.compareTo(domestic_cur_id) != 0) buffer.append("=").append(decimalFormat.format(collateral.ncv)).append(" ").append(domestic_code_char);
                buffer.append(", ponder=").append(collateral.ponder).append("%");
                buffer.append(", WCOV=").append(decimalFormat.format(collateral.wcov)).append(" ").append(domestic_code_char);
                buffer.append(", tip=");
                if (collateral.gcmType != null) buffer.append(collateral.gcmType.name_add).append(" ").append(collateral.gcmType.name);
                buffer.append(", prioritet=").append(collateral.priority);
                if (collateral.issuer != null) buffer.append(", izdavatelj=").append(collateral.issuer.register_no).append(", PD=").append(collateral.issuer.rating.pd);
                
                for (MortgageData mortgage : collateral.mortgages.values())
                {
                    buffer.append("\n   |   |");
                    if (isPlacementCoveredViaMortgage(placement, mortgage)) buffer.append("-> "); else buffer.append("   ");
                    buffer.append("HIPOTEKA ").append(mortgage.priority);
                    if (mortgage.isRBA) buffer.append(" RBA"); else buffer.append(" xxx");
                    buffer.append(": iznos=").append(decimalFormat.format(mortgage.amount_ref)).append(" ").append(exchangeRate.get(mortgage.cur_id_ref).code_char);
                    if (mortgage.cur_id_ref.compareTo(domestic_cur_id) != 0) buffer.append("=").append(decimalFormat.format(mortgage.amount)).append(" ").append(domestic_code_char);
                    if (mortgage.isFirstRBA) buffer.append(" (1.RBA)");
                    
                    if (mortgage.isSindicated)
                    {
                        buffer.append(" [SINDICIRANA:");
                        buffer.append("udio RBA=").append(mortgage.rba_sindic_part).append("%").append(",udio drugih banaka=").append(mortgage.other_sindic_part).append("%");
                        buffer.append(",iznos drugih banaka=").append(decimalFormat.format(mortgage.other_sindic_amount_ref)).append(" ").append(exchangeRate.get(mortgage.cur_id_ref).code_char);
                        if (mortgage.cur_id_ref.compareTo(domestic_cur_id) != 0) buffer.append("=").append(decimalFormat.format(mortgage.other_sindic_amount)).append(" ").append(domestic_code_char);
                        buffer.append("]");
                    }
                    
                    if (mortgage.frameAgreement != null)
                    {
                        buffer.append(" - OKVIRNI SPORAZUM ").append(mortgage.frameAgreement.agreement_no);
                        buffer.append(": ").append(decimalFormat.format(mortgage.frameAgreement.amount_ref)).append(" ").append(exchangeRate.get(mortgage.frameAgreement.cur_id_ref).code_char);
                        if (mortgage.frameAgreement.cur_id_ref.compareTo(domestic_cur_id) != 0) buffer.append("=").append(decimalFormat.format(mortgage.frameAgreement.amount)).append(" ").append(domestic_code_char);
                    }
                }
            }
            
            buffer.append("\n");
            
            bc.debug(buffer.toString());
        }
        
        bc.stopStopWatch("BO820.printStructureToDebug");
    }
    
    private boolean isPlacementCoveredViaMortgage(PlacementData placement, MortgageData mortgage)
    {
        for (CoverageData coverage : placement.eligibleCoverages) if (coverage.mortgage == mortgage) return true;
        return false;
    }
    
    private boolean isPlacementCoveredViaCollateralDirectly(PlacementData placement, CollateralData collateral)
    {
        for (CoverageData coverage : placement.eligibleCoverages) if (coverage.collateral == collateral && coverage.mortgage == null) return true;
        return false;
    }
    
    
    
    
    
    /**
     * Metoda uèitava potrebne podatke o kolateralima koji ulaze u izraèun pokrivenosti.
     */
    private void loadCollaterals() throws Exception
    {
        bc.startStopWatch("BO820.loadCollaterals");
        
        CollateralIterator iter = bo821.selectCollaterals(value_date);
        while (iter.next())
        {
            CollateralData collateral = new CollateralData();
            collateral.col_hea_id = iter.col_hea_id();
            collateral.col_num = iter.col_num().trim();
            
            collateral.real_est_nomi_valu = iter.real_est_nomi_valu();
            collateral.real_est_nm_cur_id = iter.real_est_nm_cur_id();
            if (collateral.real_est_nomi_valu == null || collateral.real_est_nm_cur_id == null)
            {
                collateral.ncv = zero;
            }
            else
            {
                collateral.ncv = yrxx0.exchange(collateral.real_est_nomi_valu, collateral.real_est_nm_cur_id, domestic_cur_id, value_date);
            }
            
            collateral.category = collateralCategories.get(iter.col_cat_id());
            collateral.type = collateralTypes.get(iter.col_typ_id());
            
            collateral.collateral_status = iter.collateral_status();
            
            collateral.inspol_ind = iter.inspol_ind();
            if (!"D".equalsIgnoreCase(collateral.inspol_ind)) collateral.inspol_ind = "N";
            
            collateral.rba_eligibility = "D".equalsIgnoreCase(iter.rba_eligibility());
            collateral.reservation_eligibility = "D".equalsIgnoreCase(iter.reservation_eligibility());
            collateral.b2_hnb_stand_eligibility = (iter.b2_hnb_stand_eligibility() != null && "D".equalsIgnoreCase(iter.b2_hnb_stand_eligibility().trim()));
            collateral.b2_irb_eligibility = "D".equalsIgnoreCase(iter.b2_irb_eligibility());
            collateral.nd_eligibility = "D".equalsIgnoreCase(iter.nd_eligibility());
           
            collaterals.put(collateral.col_hea_id, collateral);
        }
        iter.close();
        
        bc.stopStopWatch("BO820.loadCollaterals");
    }
    
    
    /**
     * Metoda uèitava podatke o svim kategorijama kolaterala.
     */
    private void loadCollateralCategories() throws Exception
    {
        bc.startStopWatch("BO820.loadCollateralCategories");
        bc.debug("Kategorije kolaterala:");
        
        CollateralCategoryIterator iter = bo821.selectCollateralCategories();
        while (iter.next())
        {
            CollateralCategoryData category = new CollateralCategoryData();
            category.col_cat_id = iter.col_cat_id();
            category.code = iter.code().trim();
            category.name = iter.name();
            collateralCategories.put(category.col_cat_id, category);
            bc.debug("   " + category.col_cat_id + " " + category.code + " " + category.name);
        }
        iter.close();
        
        bc.stopStopWatch("BO820.loadCollateralCategories");
    }
    
    /**
     * Metoda uèitava podatke o svim vrstama kolaterala.
     */
    private void loadCollateralTypes() throws Exception
    {
        bc.startStopWatch("BO820.loadCollateralTypes");
        bc.debug("Vrste kolaterala:");
        
        CollateralTypeIterator iter = bo821.selectCollateralTypes();
        while (iter.next())
        {
            CollateralTypeData type = new CollateralTypeData();
            type.col_typ_id = iter.col_typ_id();
            type.code = iter.code().trim();
            type.name = iter.name();
            collateralTypes.put(type.col_typ_id, type);
            bc.debug("   " + type.col_typ_id + " " + type.code + " " + type.name);
        }
        iter.close();
        
        bc.stopStopWatch("BO820.loadCollateralTypes");
    }
    
    /**
     * Metoda uèitava podatke o svim podvrstama kolaterala.
     */
    private void loadCollateralSubtypes() throws Exception
    {
        bc.startStopWatch("BO820.loadCollateralSubtypes");
        bc.debug("Podvrste kolaterala:");
        
        // dohvati sve moguæe podvrste kolaterala
        CollateralSubtypeIterator iter = bo821.selectCollateralSubtypes();
        while (iter.next())
        {
            CollateralSubtypeData subtype = new CollateralSubtypeData();
            subtype.col_sub_id = iter.col_sub_id();
            subtype.code = iter.code().trim();
            subtype.name = iter.name();
            collateralSubtypes.put(subtype.col_sub_id, subtype);
            bc.debug("   " + subtype.col_sub_id + " " + subtype.code + " " + subtype.name);
        }
        iter.close();
        
        bc.stopStopWatch("BO820.loadCollateralSubtypes");
    }
    
    
    /**
     * Metoda popunjava podvrstu za sve uèitane kolaterale.
     */
    private void fillCollateralsSubtypeData() throws Exception
    {
        bc.startStopWatch("BO820.fillCollateralsSubtypeData");
        
        // dohvati kolaterale i njihove ID-eve podvrste
        CollSubtypeIdsIterator iter = bo821.selectCollSubtypeIds();
        while (iter.next())
        {
            CollateralData collateral = collaterals.get(iter.col_hea_id());
            if (collateral != null) collateral.subtype = collateralSubtypes.get(iter.col_sub_id());
        }
        iter.close();
        
        bc.stopStopWatch("BO820.fillCollateralsSubtypeData");
    }
    
    
    /** 
     * Metoda uèitava sve defaultne pondere.
     */
    private void loadCollateralDefaultPonders() throws Exception
    {
        bc.startStopWatch("BO820.loadCollateralDefaultPonders");
        
        bc.debug("Defaultni ponderi:");
        PonderDefaultIterator iter = bo821.selectPondersDefault(value_date);
        while (iter.next())
        {
            PonderData ponder = new PonderData();
            ponder.category = collateralCategories.get(iter.col_cat_id());
            ponder.type = collateralTypes.get(iter.col_typ_id());
            ponder.subtype = collateralSubtypes.get(iter.col_sub_id());
            ponder.addRequest = iter.add_request();
            ponder.defaultValue = iter.dfl_value();
            defaultPonders.add(ponder);
            bc.debug("   " + ponder.category + ", " + ponder.type + ", " + ponder.subtype + ", " + ponder.addRequest + " -> " + ponder.defaultValue + "%");
        }
        iter.close();
        
        bc.stopStopWatch("BO820.loadCollateralDefaultPonders");
    }
    

    /**
     * Metoda vraæa defaultni ponder za zadani kolateral prema njegovoj kategoriji, vrsti, podvrsti te dodatnom uvjetu.
     * @param collateral objekt s podacima o kolateralu
     * @param addRequest dodatni uvjet (D ili N)
     * @return defaultni ponder ili null ako nije definiran
     */
    private BigDecimal getDefaultPonder(CollateralData collateral, String addRequest)
    {
        for (PonderData ponder : defaultPonders)
        {
            if (ponder.category == collateral.category && 
                ponder.type == collateral.type && 
                (ponder.subtype == null || ponder.subtype == collateral.subtype) &&
                ponder.addRequest.equalsIgnoreCase(addRequest))
            {
                return ponder.defaultValue; 
            }
        }
        return null;
    }
    
    
    /**
     * Metoda dohvaæa pondere za sve kolaterale koji ulaze u izraèun pokrivenosti.
     */
    private void loadCollateralPonders() throws Exception
    {
        bc.startStopWatch("BO820.loadCollateralPonders");
        
        // dohvati sve pondere koje definiraju referenti kolaterala
        PonderCoIterator iter = bo821.selectPondersCo(value_date);
        while (iter.next())
        {
            CollateralData collateral = collaterals.get(iter.col_hea_id());
            if (collateral != null) collateral.ponder = iter.ponder_value();
        }
        iter.close();
        
        // za sve kolaterale koji nemaju definiran ponder referenta kolaterala vrijedi defaultni ponder
        for (CollateralData collateral : collaterals.values())
        {
            if (collateral.ponder == null) collateral.ponder = getDefaultPonder(collateral, collateral.inspol_ind);
            if (collateral.ponder == null && "D".equalsIgnoreCase(collateral.inspol_ind)) collateral.ponder = getDefaultPonder(collateral, "N");
            if (collateral.ponder == null) collateral.ponder = hundred;
        }
        
        bc.stopStopWatch("BO820.loadCollateralPonders");
    }
    
    
    /**
     * Metoda uèitava podatke o svim CCF grupama.
     */
    private void loadCcfGroups() throws Exception
    {
        bc.startStopWatch("BO820.loadCcfGroups");
        bc.debug("CCF grupe:");
        
        CcfGroupIterator iter = bo821.selectCcfGroups(value_date);
        while (iter.next())
        {
            CcfGroupData ccfGroup = new CcfGroupData();
            ccfGroup.ccf_gro_id = iter.ccf_gro_id();
            ccfGroup.code = iter.code();
            ccfGroup.name = iter.name();
            ccfGroup.ccf_value = iter.ccf_value();
            ccfGroups.put(ccfGroup.code, ccfGroup);
            bc.debug("   " + ccfGroup.code + " " + ccfGroup.name + " -> " + ccfGroup.ccf_value + "%");
        }
        iter.close();
        
        bc.stopStopWatch("BO820.loadCcfGroups");
    }
    
    
    /**
     * Metoda dohvaæa pripadajuæu CCF grupu za zadanu RBHR šifru. 
     * @param rbhr_code RBHR šifra
     * @return pripadajuæa CCF grupa, null ako nije pronaðena
     */
    private CcfGroupData getCcfGroupForRbhrCode(String rbhr_code)
    {
        for (CcfGroupData ccfGroup : ccfGroups.values())
        {
            if (ccfGroup.name != null && ccfGroup.name.trim().equalsIgnoreCase(rbhr_code))
            {
                return ccfGroup;
            }
        }
        return null;
    }
    
    
    
    /**
     * Metoda dohvaæa potrebne podatke o izdavateljima garancija koje ulaze u izraèun pokrivenosti.
     */
    private void loadGuaranteeIssuers() throws Exception
    {
        bc.startStopWatch("BO820.loadGuaranteeIssuers");
        
        GuaranteeIssuerIterator iter = bo821.selectGuaranteeIssuers();
        while (iter.next())
        {
            CollateralData collateral = collaterals.get(iter.col_hea_id());
            if (collateral == null) continue;
            
            CustomerData customer = customers.get(iter.cus_id());
            if (customer == null)
            {
                customer = new CustomerData();
                customer.cus_id = iter.cus_id();
                customer.register_no = iter.register_no().trim();
                customer.name = iter.name().trim();
                customer.basel_cus_type = iter.basel_cus_type().trim();
                customers.put(customer.cus_id, customer);
            }
            collateral.issuer = customer;
        }
        iter.close();
        
        bc.stopStopWatch("BO820.loadGuaranteeIssuers");
    }
    
    
    
    /**
     * Metoda mapira sve kolaterale u pripadajuæe tipove definirane za izraèun pokrivenosti i pridjeljuje im prioritet ovisno o tipu.  
     */
    private void mapCollaterals() throws Exception
    {
        bc.startStopWatch("BO820.mapCollaterals");
        
        YOYM0 yoym0 = new YOYM0(bc, "allocation", value_date);
        
        for (CollateralData collateral : collaterals.values())
        {
            GcmTypeData type = yoym0.resolve(collateral.category.col_cat_id, collateral.type.col_typ_id, collateral.subtype != null ? collateral.subtype.col_sub_id : null);
            if (type != null)  // ako je mapiranje pronaðeno, kolateralu pridijeli prioritet pripadajuæeg tipa
            {
                collateral.gcmType = type;
                collateral.priority = type.ord_no;
            }
            else  // ako mapiranje nije pronaðeno, kolateralu pridijeli najniži prioritet
            {
                warn("Nije moguce mapirati kolateral " + collateral.col_num + ". Kolateral ce dobiti najnizi prioritet prilikom raspodjele. (kategorija = " + collateral.category + ", vrsta = " + collateral.type + ", podvrsta = " + collateral.subtype + ")");
                collateral.priority = 999999;
            }
        }
        
        bc.stopStopWatch("BO820.mapCollaterals");
    }
    
    
    
    /**
     * Metoda uèitava potrebne podatke o okvirnim sporazumima koji ulaze u izraèun pokrivenosti
     */
    private void loadFrameAgreements() throws Exception
    {
        bc.startStopWatch("BO820.loadFrameAgreements");
        
        FrameAgreementIterator iter = bo821.selectFrameAgreements(value_date);
        while (iter.next())
        {
            FrameAgreementData frameAgreement = new FrameAgreementData();
            
            frameAgreement.fra_agr_id = iter.fra_agr_id();
            frameAgreement.agreement_no = iter.agreement_no();
            frameAgreement.amount_ref = iter.amount_ref();
            frameAgreement.cur_id_ref = iter.cur_id_ref();
            frameAgreement.amount = yrxx0.exchange(frameAgreement.amount_ref, frameAgreement.cur_id_ref, domestic_cur_id, value_date);
           
            frameAgreements.put(frameAgreement.fra_agr_id, frameAgreement);
        }
        iter.close();
        
        bc.stopStopWatch("BO820.loadFrameAgreements");
    }
    
    
    /**
     * Metoda uèitava potrebne podatke o hipotekama koje ulaze u izraèun pokrivenosti.
     */
    private void loadMortgages() throws Exception
    {
        bc.startStopWatch("BO820.loadMortgages");
        
        MortgageIterator iter = bo821.selectMortgages(value_date);
        while (iter.next())
        {
            CollateralData collateral = collaterals.get(iter.col_hea_id());
            if (collateral == null) continue;
            
            MortgageData mortgage = new MortgageData();
            mortgage.coll_hf_prior_id = iter.coll_hf_prior_id();
            mortgage.collateral = collateral;
            mortgage.priority = iter.hf_priority();
            mortgage.amount_ref = iter.amount_ref();
            mortgage.cur_id_ref = iter.cur_id_ref();
            mortgage.amount = yrxx0.exchange(mortgage.amount_ref, mortgage.cur_id_ref, domestic_cur_id, value_date);
            mortgage.isRBA = MortgageData.rba_cus_id.compareTo(iter.hf_own_cus_id()) == 0;
            mortgage.isFirstRBA = false;
            mortgage.isLastRBA = false;
            mortgage.frameAgreement = frameAgreements.get(iter.fra_agr_id());
            if (mortgage.frameAgreement != null)
            {
                if (!mortgage.frameAgreement.mortgages.contains(mortgage)) mortgage.frameAgreement.mortgages.add(mortgage);
                if (!mortgage.frameAgreement.collaterals.contains(mortgage.collateral)) mortgage.frameAgreement.collaterals.add(mortgage.collateral);
            }

            mortgage.isSindicated = "D".equalsIgnoreCase(iter.sindic_ind());
            if (mortgage.isSindicated)
            {
                mortgage.rba_sindic_part = iter.rba_sindic_part();
                mortgage.other_sindic_part = iter.other_sindic_part();
                mortgage.other_sindic_amount_ref = iter.other_sindic_amount();
                mortgage.other_sindic_amount = yrxx0.exchange(mortgage.other_sindic_amount_ref, mortgage.cur_id_ref, domestic_cur_id, value_date);
                mortgage.isRBA = true;
            }

            collateral.mortgages.put(mortgage.priority, mortgage);

            mortgages.put(mortgage.coll_hf_prior_id, mortgage);
        }
        iter.close();
        
        // za svaku hipoteku izraèunaj sumu iznosa hipoteka višeg reda drugih banaka
        for (MortgageData mortgage : mortgages.values())
        {
            mortgage.otherMortgageAmount = getSumOfHigherOrderForeignMortgages(mortgage);
        }
        
        // oznaèi prvu i zadnju RBA hipoteku
        for (CollateralData collateral : collaterals.values())
        {
            MortgageData lastMortgage = null;
            for (MortgageData mortgage : collateral.mortgages.values())
            {
                if (mortgage.isRBA)
                {
                    if (lastMortgage == null)
                    {
                        mortgage.isFirstRBA = true;
                        collateral.firstRbaMortgage = mortgage;                        
                    }
                    lastMortgage = mortgage;
                }
            }
            if (lastMortgage != null)
            {
                lastMortgage.isLastRBA = true;
                collateral.lastRbaMortgage = lastMortgage;
            }
        }
        
        bc.stopStopWatch("BO820.loadMortgages");
    }
    
    
    /**
     * Metoda uèitava potrebne podatke o plasmanima koji ulaze u izraèun pokrivenosti.
     */
    private void loadPlacements() throws Exception
    {
        bc.startStopWatch("BO820.loadPlacements");

        PlacementIterator iter = bo821.selectPlacements(value_date, allocationType);
        while (iter.next())
        {
            PlacementData placement = new PlacementData();
            placement.cus_acc_id = iter.cus_acc_id();
            placement.cus_acc_no = iter.cus_acc_no();
            placement.module_code = iter.module_code();
            placement.prod_code = iter.prod_code();
            placement.exposure_balance = iter.exposure_balance();
            placement.exposure_cur_id = iter.exposure_cur_id();
            placement.exposure = yrxx0.exchange(placement.exposure_balance, placement.exposure_cur_id, domestic_cur_id, value_date);
            placement.exposure_bal_lcy = iter.exposure_bal_lcy();
            placement.exp_off_bal_lcy = iter.exp_off_bal_lcy().min(placement.exposure);
            placement.ccf = iter.ccf();
            placement.contract_cur_id = iter.contract_cur_id();
            placement.isCurrencyClause = "3".equals(iter.currency_type());
            
            CustomerData customer = customers.get(iter.cus_id());
            if (customer == null)
            {
                customer = new CustomerData();
                customer.cus_id = iter.cus_id();
                customer.register_no = iter.register_no().trim();
                customer.name = iter.name().trim();
                customer.basel_cus_type = iter.basel_cus_type().trim();
                customers.put(customer.cus_id, customer);
            }
            placement.owner = customer;
            
            // ako je partija iz okvira, zapamti cus_acc_no okvira
            if (iter.frame_cus_acc_no() != null && !"".equals(iter.frame_cus_acc_no().trim()) && !"0".equals(iter.frame_cus_acc_no().trim()))
            {
                placement.frame = new PlacementData();
                placement.frame.cus_acc_no = iter.frame_cus_acc_no();
            }
            
            // ako je plasman okvir, stavi okvir na listu okvira
            if ("OKV".equalsIgnoreCase(placement.module_code) && !frames.containsKey(placement.cus_acc_no))
            {
                placement.isFrame = true;
                placement.exposure = placement.exp_off_bal_lcy;  // prema RTC 6092, 20.12.2013. - za izloženost okvira treba uzeti iznos vanbilance dostavljen iz DWH
                placement.framePlacements = new ArrayList<PlacementData>();
                frames.put(placement.cus_acc_no, placement);
            }
            
            placements.put(placement.cus_acc_id, placement);
        }
        iter.close();
        
        // poveži partije iz okvira s okvirom
        for (PlacementData placement : placements.values())
        {
            if (placement.frame != null)
            {
                String placement_frame_cus_acc_no = placement.frame.cus_acc_no;
                placement.frame = frames.get(placement_frame_cus_acc_no);
                if (placement.frame == null)
                {
                    warn("Ne postoje podaci za okvir " + placement_frame_cus_acc_no + " iz kojeg je plasman " + placement.cus_acc_no + "!");
                    continue;
                }
                placement.frame.framePlacements.add(placement);
            }
        }
        
        bc.stopStopWatch("BO820.loadPlacements"); 
    }
    
    
    /**
     * Metoda uèitava potrebne podatke o vezama kolateral-plasman koje ulaze u izraèun pokrivenosti.
     */
    private void loadCoverages() throws Exception
    {
        bc.startStopWatch("BO820.loadCoverages");
        
        // uèitaj direktne veze
        CoverageIterator iter = bo821.selectCoverages(value_date, allocationType);
        while (iter.next())
        {
            CoverageData coverage = new CoverageData();
            coverage.isVirtual = false;
            
            PlacementData placement = placements.get(iter.cus_acc_id());
            if (placement == null) continue;
            coverage.placement = placement;
            
            if (iter.coll_hf_prior_id() != null)
            {
               MortgageData mortgage = mortgages.get(iter.coll_hf_prior_id());
               if (mortgage == null) continue;
               coverage.mortgage = mortgage;
               coverage.collateral = mortgage.collateral;
               coverage.isCurrencyMatched = placement.isCurrencyMatchedWithCollateral(coverage.mortgage.collateral);
               coverage.isDirectlyRelatedToPlacement = Boolean.TRUE;
               coverage.isSindicated = mortgage.isSindicated;
               mortgage.collateral.coverages.add(coverage);
            }
            else
            {
                CollateralData collateral = collaterals.get(iter.col_hea_id());
                if (collateral == null) continue;
                coverage.collateral = collateral;
                coverage.isCurrencyMatched = placement.isCurrencyMatchedWithCollateral(coverage.collateral);
                coverage.isDirectlyRelatedToPlacement = Boolean.TRUE;
                coverage.isSindicated = Boolean.FALSE;
                collateral.coverages.add(coverage);
            }
            
            placement.coverages.add(coverage);
            coverages.add(coverage);
        }
        iter.close();
        
        // uèitaj indirektne veze, tj. veze plasmana iz okvira na kolateral preko okvira
        for (PlacementData placement : placements.values())
        {
            if (!placement.isFrame) continue;
            
            for (CoverageData coverage : placement.coverages)
            {
                for (PlacementData framePlacement : placement.framePlacements)
                {
                    CoverageData coverageCopy = new CoverageData();
                    coverageCopy.isVirtual = false;
                    coverageCopy.placement = framePlacement; 
                    coverageCopy.mortgage = coverage.mortgage;
                    coverageCopy.collateral = coverage.collateral;
                    coverageCopy.isDirectlyRelatedToPlacement = Boolean.FALSE;
                    
                    if (coverageCopy.mortgage != null)
                    {
                        coverageCopy.isCurrencyMatched = framePlacement.isCurrencyMatchedWithCollateral(coverageCopy.mortgage.collateral);
                        coverageCopy.isSindicated = coverageCopy.mortgage.isSindicated;
                        coverageCopy.mortgage.collateral.coverages.add(coverageCopy);
                    }
                    else
                    {
                        coverageCopy.isCurrencyMatched = framePlacement.isCurrencyMatchedWithCollateral(coverageCopy.collateral);
                        coverageCopy.isSindicated = Boolean.FALSE;
                        coverageCopy.collateral.coverages.add(coverageCopy);
                    }
                    
                    framePlacement.coverages.add(coverageCopy);
                    coverages.add(coverageCopy);
                }
            }
        }
        
        bc.stopStopWatch("BO820.loadCoverages");
    }



    /**
     * Metoda uèitava sve definicije vrsta ratinga, ocjena i pripadajuæih PD vrijednosti.   
     */
    private void loadRatingDefinitions() throws Exception
    {
        bc.startStopWatch("BO820.loadRatingDefinitions");
        
        RatingDefinitionIterator iter = bo821.selectRatingDefinitions(value_date);
        while (iter.next())
        {
            String basel_cus_type = iter.basel_cus_type().trim();
            HashMap<BigDecimal, RatingData> customerTypeRatings = ratingDefinitions.get(basel_cus_type);
            if (customerTypeRatings == null)
            {
                customerTypeRatings = new HashMap<BigDecimal, RatingData>();
                ratingDefinitions.put(basel_cus_type, customerTypeRatings);
            }
            
            RatingData ratingDefinition = new RatingData();
            ratingDefinition.rat_typ_id = iter.rat_typ_id();
            ratingDefinition.rat_typ_code = iter.rat_typ_code();
            ratingDefinition.rat_sco_id = iter.rat_sco_id();
            ratingDefinition.score = iter.score();
            ratingDefinition.pd = iter.pd();
            customerTypeRatings.put(ratingDefinition.rat_sco_id, ratingDefinition);
        }
        iter.close();
        
        // ispis u debug
        bc.debug("Rating definicije:");
        for (String basel_cus_type : ratingDefinitions.keySet())
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("   B2AC ").append(basel_cus_type).append(": ");
            HashMap<BigDecimal, RatingData> customerTypeRatings = ratingDefinitions.get(basel_cus_type);
            
            boolean first = true;
            for (RatingData ratingDefinition : customerTypeRatings.values())
            {
                if (first)
                {
                    buffer.append(ratingDefinition.rat_typ_code).append(": ");
                    first = false;
                }
                buffer.append(ratingDefinition.score).append("->").append(ratingDefinition.pd).append("%, ");
            }
            bc.debug(buffer.substring(0, buffer.length() - 2));
        }
        
        bc.stopStopWatch("BO820.loadRatingDefinitions");
    }
    
    
    /**
     * Metoda za sve komitente koji ulaze u izraèun pokrivenosti uèitava rating i pripadajuæu PD vrijednost.
     * @return da li je metoda uspješno izvršena
     */
    private boolean loadCustomerRatings() throws Exception
    {
        bc.debug("Komitenti:");
        
        for (CustomerData customer : customers.values())
        {
            // dohvati sve definicije ratinga za B2 Asset klasu komitenta
            HashMap<BigDecimal, RatingData> customerTypeRatings = ratingDefinitions.get(customer.basel_cus_type);
            
            // ako ne postoji nijedna definicija za B2 Asset klasu komitenta, ispiši grešku i prekini daljnje izvoðenje
            if (customerTypeRatings == null)
            {
                error("Komitent " + customer.register_no + " " + customer.name + " pripada B2 Asset klasi " + customer.basel_cus_type + " za koju u parametrizaciji nije definirana vrsta ratinga!", null);
                return false;
            }
            
            // uzmi prvu vrstu ratinga
            RatingData ratingDefinition = customerTypeRatings.values().iterator().next();
            
            // dohvati ocjenu komitenta za vrstu ratinga
            RatingData ratingScore;
            if (ratingDefinition.rat_typ_id != null)   // ako je vrsta ratinga definirana, dohvati važeæu ocjenu za komitenta na datum valute prema vrsti ratinga
            {
                ratingScore = bo821.selectCustomerRating(customer, ratingDefinition, value_date);
            }
            else  // ako vrsta ratinga nije definirana, PD vrijednost je veæ definirana u prvom zapisu
            {
                ratingScore = ratingDefinition;
            }
            
            // ako komitent ima definiranu ocjenu, naði pripadajuæi PD za tu ocjenu
            if (ratingScore != null)  
            {
                customer.rating = customerTypeRatings.get(ratingScore.rat_sco_id);
            }
            
            // ako PD nije naðen, naði pripadajuæi PD za nedefiniranu ocjenu (unrated)
            if (customer.rating == null)
            {
                customer.rating = customerTypeRatings.get(null);
            }

            // ako PD nije naðen ni za ocjenu unrated, ispiši grešku i prekini daljnje izvoðenje
            if (customer.rating == null)
            {
                error("Komitent " + customer.register_no + " " + customer.name + " iz B2 Asset klase " + customer.basel_cus_type + " za datum " + dateFormat.format(value_date) + " i vrstu ratinga " + ratingDefinition.rat_typ_code + " ima ocjenu " + "UNRATED" + " koja u parametrizaciji nema definiranu pripadajucu PD vrijednost!", null);
                return false;
            }
            
            bc.debug("   " + customer.register_no + " " + customer.name + ", B2AC " + customer.basel_cus_type + " -> rating " + ratingDefinition.rat_typ_code + " -> ocjena " + customer.rating.score + " -> PD = " + customer.rating.pd);
        }
        
        return true;
    }
    
    
    
    /**
     * Metoda koja za sve uèitane plasmane odreðuje i postavlja CCF vrijednost.
     */
    private void determineCcf() throws Exception
    {
        for (PlacementData placement : placements.values())
        {
            // ako je modul plasmana PKR, KKR, PPZ, SDR, TRC ili KRD, uzima se CCF vrijednost 100%
            if ("PKR".equalsIgnoreCase(placement.module_code) ||
                "KKR".equalsIgnoreCase(placement.module_code) ||
                "PPZ".equalsIgnoreCase(placement.module_code) ||
                "SDR".equalsIgnoreCase(placement.module_code) ||
                "TRC".equalsIgnoreCase(placement.module_code) ||
                "KRD".equalsIgnoreCase(placement.module_code))
            {
                placement.ccf = defaultCcfValueForModulesPkrKkrPpzTrcKrd;
                continue;
            }
            
            // ako je iz DWH dostavljena CCF vrijednost za plasman, uzima se dostavljena vrijednost
            if (placement.ccf != null) continue;
            
            // ako iz DWH nije dostavljena CCF vrijednost za plasman, CCF vrijednost se odreðuje na temelju oznake modula plasmana
            if ("OKV".equalsIgnoreCase(placement.module_code))  // OKVIRI
            {
                determineCcfForFrame(placement);
            }
            else if ("LOC".equalsIgnoreCase(placement.module_code))  // AKREDITIVI
            {
                determineCcfForLetterOfCredit(placement);
            }
            else if ("GAR".equalsIgnoreCase(placement.module_code))  // GARANCIJE
            {
                determineCcfForGuarantee(placement);
            }
            else  // NEPOZNAT MODUL
            {
                placement.ccf = defaultCcfValue;  // uzmi defaultnu CCF vrijednost
                info("Plasman " + placement.cus_acc_no + " ima nepoznatu oznaku modula " + placement.module_code + ". Za CCF vrijednost ce se uzeti defaultna vrijednost (" + placement.ccf + "%).");
            }
        }
    }
    
    
    /**
     * Metoda odreðuje i postavlja CCF vrijednost za zadani okvir.
     * @param placement objekt s podacima o okviru
     */
    private void determineCcfForFrame(PlacementData placement) throws Exception
    {
        // dohvati šifru CCF grupe za okvir
        String ccf_group_code = bo821.selectPlacementCcfGroup(value_date, placement);
        
        if (ccf_group_code != null)  // ako je za okvir definirana šifra CCF grupe, dohvati CCF vrijednost za tu grupu
        {
            CcfGroupData ccfGroup = ccfGroups.get(ccf_group_code);  // naði definiciju CCF grupe
            if (ccfGroup != null)  // ako je CCF grupa pronaðena, postavi okviru CCF vrijednost definiranu za tu grupu
            {
                placement.ccf = ccfGroup.ccf_value;
            }
            else  // ako CCF grupa nije pronaðena, postavi okviru defaultnu CCF vrijednost za okvire 
            {
                placement.ccf = defaultCcfValueForModuleOkv;
                info("Za okvir " + placement.cus_acc_no + " je definirana CCF grupa " + ccf_group_code + " koja ne postoji u sifrarniku CCF grupa. Za CCF vrijednost ce se uzeti defaultna CCF vrijednost za okvire (" + placement.ccf + "%).");
            }
        }
        else  // ako za okvir nije definirana šifra CCF grupe, postavi okviru defaultnu CCF vrijednost za okvire
        {
            placement.ccf = defaultCcfValueForModuleOkv;
            info("Za okvir " + placement.cus_acc_no + " nije definirana CCF grupa. Za CCF vrijednost ce se uzeti defaultna CCF vrijednost za okvire (" + placement.ccf + "%).");
        }
    }
    
    /**
     * Metoda odreðuje i postavlja CCF vrijednost za zadani akreditiv.
     * @param placement objekt s podacima o akreditivu
     */
    private void determineCcfForLetterOfCredit(PlacementData placement) throws Exception
    {
        // dohvati neopozivost akreditiva
        Boolean isIrrevocable = bo821.selectIsPlacementIrrevocable(value_date, placement);
        
        if (Boolean.TRUE.equals(isIrrevocable))  // akreditiv je neopoziv
        {
            // dohvati maksimalni datum dospijeæa akreditiva
            Date maturityDate = bo821.selectPlacementMaxMaturityDate(value_date, placement);
            
            // dohvati oèekivani datum dospijeæa akreditiva
            Date expectedMaturityDate = bo821.selectPlacementExpectedMaturityDate(value_date, placement);
            
            // datum dospijeæa koji se uzima za odreðivanje roènosti
            Date dueDate;
            if (maturityDate != null) dueDate = maturityDate;  // ako je popunjen datum dospijeæa, uzima se datum dospijeæa
            else dueDate = expectedMaturityDate;    // ako nije popunjen datum dospijeæa, uzima se oèekivani datum dospijeæa

            // ako je datum dospijeæa manji od datuma valute, a postoji unesen oèekivani datum dospijeæa veæi od datuma valute, uzima se oèekivani datum dospijeæa
            if (maturityDate != null && maturityDate.before(value_date) && expectedMaturityDate != null && expectedMaturityDate.after(value_date)) dueDate = expectedMaturityDate;
            
            // dohvati datum aktiviranja ugovora o akreditivu
            Date activationDate = bo821.selectContractActivationDate(value_date, placement);
            
            if (dueDate == null || activationDate == null)  // ako neki od potrebnih podataka za odreðivanje roènosti nije dostupan, uzima se defaultna CCF vrijednost za akreditive
            {
                placement.ccf = defaultCcfValueForModuleLoc;
                info("Za neopozivi akreditiv " + placement.cus_acc_no + " nije moguce odrediti rocnost (datum dospijeca = " + maturityDate + ", ocekivani datum dospijeca = " + expectedMaturityDate + ", datum aktiviranja ugovora = " + activationDate + "). Za CCF vrijednost ce se uzeti defaultna CCF vrijednost za akreditive (" + placement.ccf + "%).");
            }
            else
            {
                // roènost = (datum dospijeæa ili oèekivani datum dospijeæa) - datum aktiviranja ugovora o akreditivu 
                long maturity = TimeUnit.MILLISECONDS.toDays(dueDate.getTime() - activationDate.getTime());
                
                // ako je roènost <= 3 mjeseca, uzima se CCF grupa 70, inaèe CCF grupa 80
                String ccf_group_code;
                if (maturity <= 90) ccf_group_code = "70"; else ccf_group_code = "80";
                
                // dohvati CCF vrijednost za grupu
                CcfGroupData ccfGroup = ccfGroups.get(ccf_group_code);  // naði definiciju CCF grupe
                if (ccfGroup != null)  // ako je CCF grupa pronaðena, postavi akreditivu CCF vrijednost definiranu za tu grupu 
                {
                    placement.ccf = ccfGroup.ccf_value;
                }
                else  // ako CCF grupa nije pronaðena, postavi akreditivu defaultnu CCF vrijednost za akeditive
                {
                    placement.ccf = defaultCcfValueForModuleLoc;
                    info("Za neopozivi akreditiv " + placement.cus_acc_no + " je definirana CCF grupa " + ccf_group_code + " koja ne postoji u sifrarniku CCF grupa. Za CCF vrijednost ce se uzeti defaultna CCF vrijednost za akreditive (" + placement.ccf + "%).");
                }
            }
        }
        else if (Boolean.FALSE.equals(isIrrevocable))  // akreditiv je opoziv
        {
            // dohvati CCF vrijednost za grupu 60
            String ccf_group_code = "60";
            CcfGroupData ccfGroup = ccfGroups.get(ccf_group_code);
            if (ccfGroup != null)  // ako je CCF grupa pronaðena, postavi akreditivu CCF vrijednost definiranu za tu grupu
            {
                placement.ccf = ccfGroup.ccf_value;
            }
            else  // ako CCF grupa nije pronaðena, postavi akreditivu defaultnu CCF vrijednost za akeditive
            {
                placement.ccf = defaultCcfValueForModuleLoc;
                info("Za opozivi akreditiv " + placement.cus_acc_no + " je definirana CCF grupa " + ccf_group_code + " koja ne postoji u sifrarniku CCF grupa. Za CCF vrijednost ce se uzeti defaultna CCF vrijednost za akreditive (" + placement.ccf + "%).");
            }
        }
        else  // nema podatka o neopozivosti
        {
            placement.ccf = defaultCcfValueForModuleLoc;
            info("Za akreditiv " + placement.cus_acc_no + " ne postoji podatak o nepozivosti. Za CCF vrijednost ce se uzeti defaultna CCF vrijednost za akreditive (" + placement.ccf + "%).");
        }
    }


    /**
     * Metoda odreðuje i postavlja CCF vrijednost za zadanu garanciju.
     * @param placement objekt s podacima o garanciji
     */
    private void determineCcfForGuarantee(PlacementData placement) throws Exception
    {
        // preko RBHR koda dohvati CCF grupu
        CcfGroupData ccfGroup = getCcfGroupForRbhrCode(placement.prod_code);

        if (ccfGroup != null)  // ako je CCF grupa pronaðena, postavi garanciji CCF vrijednost definiranu za tu grupu
        {
            placement.ccf = ccfGroup.ccf_value;
        }
        else  // ako CCF grupa nije pronaðena, postavi garanciji defaultnu CCF vrijednost za garancije
        {
            placement.ccf = defaultCcfValueForModuleGar;
            info("Za garanciju " + placement.cus_acc_no + " je definiran RBHR kod " + placement.prod_code + " koji nije definiran u sifrarniku CCF grupa. Za CCF vrijednost ce se uzeti defaultna vrijednost za garancije (" + placement.ccf + "%).");
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
            bc.startStopWatch("BO820.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6461430704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Obrada za izracun pokrivenosti");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO820.insertIntoEvent");
            return eve_id;
        }
        catch(Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade!", ex);
            throw ex;
        }
    }
    
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * Parametri se predaju u formatu: <code>bank_sign proc_type</code>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>proc_type</dt><dd>Oznaka vrste obrade.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = {"Oznaka banke", "Oznaka vrste obrade"};

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

            // uèitaj oznaku vrste obrade
            proc_type = bc.getArg(1);
            if (proc_type.endsWith("#TEST"))
            {
               isTestMode = true;
               proc_type = proc_type.replaceAll("#TEST", "");
               info("**************** TESTNI MOD ****************");
            }
            this.allocationType = AllocationTypeData.getAllocationType(proc_type);
            info("Vrsta izracuna pokrivenosti: " + allocationType);
            
            return true;
        }
        catch(Exception ex)
        {
            error("Neispravno zadani parametri!", ex);
            return false;
        }
    }
    
    
    
    /**
    * Metoda uèitava podatke o svim B2 grupama.
    */
   private void loadB2Groups() throws Exception
   {
       bc.startStopWatch("BO820.loadB2Groups");
       bc.debug("B2 grupe:");
       
       MappingSchIterator iter = bo821.selectMappingScheme("b2_group");
       while (iter.next())
       {
           B2GroupData b2Group = new B2GroupData();
           b2Group.b2_code = normalizeString(iter.map_in_cod1());
           b2Group.b2_group_code = normalizeString(iter.map_out_code());
           b2Groups.put(b2Group.b2_code, b2Group);
           bc.debug("   " + b2Group.b2_code  + " -> " + b2Group.b2_group_code + "%");
       }
       iter.close();
       
       bc.stopStopWatch("BO820.loadB2Groups");
   }

   
   /**
    * Metoda dohvaæa pripadajuæu B2 grupu za zadani basel_cus_type. 
    * @param basel_cus_type
    * @return pripadajuæa B2 grupa, 'Other' ako nije pronaðena
    */
   private String getB2Group(String basel_cus_type)
   {
       for (B2GroupData b2Group : b2Groups.values())
       {
           if (b2Group.b2_code != null && b2Group.b2_code.trim().equalsIgnoreCase(basel_cus_type))
           {
               return b2Group.b2_group_code;
           }
       }
       return "Other";
   }
   
   /**
    * Metoda uèitava podatke vrijednosti eir parametara.
    */
   private void loadEirParams() throws Exception
   {
       bc.startStopWatch("BO820.loadEirParams");
       bc.debug("EIR:");
       
       MappingSchIterator iter = bo821.selectMappingScheme("eir_param");
       while (iter.next())
       {
           EirArPocParamData eirParam = new EirArPocParamData();
           eirParam.b2_group_code = normalizeString(iter.map_in_cod1());
           eirParam.value         = new BigDecimal(iter.map_out_code());
           eirParams.put(eirParam.b2_group_code, eirParam);
           bc.debug("   " + eirParam.b2_group_code  + " -> " + eirParam.value + "%");
       }
       iter.close();
       
       bc.stopStopWatch("BO820.loadEirParams");
   }
   
    /**
    * Metoda dohvaæa pripadajuæu vrijednost eir parametra za zadanu b2 grupu. 
    * @param b2_group
    * @return pripadajuæi eir, null ako nije pronaðena
    */
   private BigDecimal getEirParam(String b2_group)
   {
       for (EirArPocParamData eirParam : eirParams.values())
       {
           if (eirParam.b2_group_code != null && eirParam.b2_group_code.trim().equalsIgnoreCase(b2_group))
           {
               return eirParam.value;
           }
       }
       return null;
   }

    
   /**
    * Metoda uèitava podatke vrijednosti arpoc parametara.
    */
   private void loadArpocParams() throws Exception
   {
       bc.startStopWatch("BO820.loadArpocParams");
       bc.debug("EIR:");
       
       MappingSchIterator iter = bo821.selectMappingScheme("arpoc_param");
       while (iter.next())
       {
           EirArPocParamData arPocParam = new EirArPocParamData();
           arPocParam.b2_group_code = normalizeString(iter.map_in_cod1());
           arPocParam.value         = new BigDecimal(iter.map_out_code());
           arPocParams.put(arPocParam.b2_group_code, arPocParam);
           bc.debug("   " + arPocParam.b2_group_code  + " -> " + arPocParam.value + "%");
       }
       iter.close();
       
       bc.stopStopWatch("BO820.loadArpocParams");
   }
   
    /**
    * Metoda dohvaæa pripadajuæu vrijednost arpoc parametra za zadanu b2 grupu. 
    * @param b2_group
    * @return pripadajuæi arpoc, null ako nije pronaðena
    */
   private BigDecimal getArpocParam(String b2_group)
   {
       for (EirArPocParamData arPocParam : arPocParams.values())
       {
           if (arPocParam.b2_group_code != null && arPocParam.b2_group_code.trim().equalsIgnoreCase(b2_group))
           {
               return arPocParam.value;
           }
       }
       return null;
   }

     
     
   private String normalizeString (String str)
   {
       if (str == null) return str;
       str = str.trim();
       if (str.length() == 0) str = null;
       return str;
   }
     
    
    
    private void error(String message, Exception ex) throws Exception
    {
        if (ex != null) bc.error(message, ex); else bc.error(message, new String[]{});
        bc.userLog("GRESKA: " + message);
    }
    
    private void warn(String message) throws Exception
    {
        bc.warning(message);
        bc.userLog("UPOZORENJE: " + message);
    }
    
    private void info(String message) throws Exception
    {
        bc.info(message);
        bc.userLog(message);
    }


    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("6461429704"));
        bp.setArgs(args);
        new BO820().run(bp);
    }
}