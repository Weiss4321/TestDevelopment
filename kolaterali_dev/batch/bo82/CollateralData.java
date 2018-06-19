package hr.vestigo.modules.collateral.batch.bo82;

import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;


/**
 * Klasa koja predstavlja kolateral.
 * @author hrakis
 */
public class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo82/CollateralData.java,v 1.21 2018/05/09 07:32:37 hraskd Exp $";

    public static final BigDecimal zero = new BigDecimal("0.00");
    
    public CollateralData()
    {
        this.mortgages = new TreeMap<Integer, MortgageData>();
        this.coverages = new ArrayList<CoverageData>();
        this.eligibleCoverages = new ArrayList<CoverageData>();
    }
    

    /** ID kolaterala. */
    public BigDecimal col_hea_id; 

    /** �ifra kolaterala. */
    public String col_num;


    /** Nominalna vrijednost kolaterala izra�ena u originalnoj valuti. */
    public BigDecimal real_est_nomi_valu;

    /** ID valute nominalne vrijednosti kolaterala. */
    public BigDecimal real_est_nm_cur_id;

    /** Nominal Collateral Value - Nominalna vrijednost kolaterala izra�ena u doma�oj valuti. */
    public BigDecimal ncv;


    /** Kategorija kolaterala. */
    public CollateralCategoryData category;

    /** Vrsta kolaterala. */
    public CollateralTypeData type;

    /** Podvrsta kolaterala. */
    public CollateralSubtypeData subtype;
    
    /** Tip kolaterala prema mapiranju. */
    public GcmTypeData gcmType;
    
    /** Prioritet kolaterala. Manji broj ozna�ava ve�i prioritet. */
    public Integer priority;
    
    /** Status kolaterala. */
    public String collateral_status;
    
    /** Indikator da li je kolateral osiguran. */
    public String inspol_ind;

    /** Ponder koji se primjenjuje na kolateral izra�en u postotcima. */
    public BigDecimal ponder;

    /** Weighted Collateral Object Value - Nominalna vrijednost kolaterala umanjena za ponder izra�ena u doma�oj valuti. */
    public BigDecimal wcov;
    
    /** Nominalna vrijednost kolaterala * ponder - suma tu�ih hipoteka do prve RBA hipoteke */
    public BigDecimal wcovh;


    /** Oznaka da li je kolateral RBA prihvatljiv. */
    public boolean rba_eligibility;

    /** Oznaka da li je kolateral prihvatljiv po prihvatljivosti za rezervacije (ex B1 prihvatljivost). */
    public boolean reservation_eligibility;

    /** Oznaka da li je kolateral B2 HNB STAND prihvatljiv. */
    public boolean b2_hnb_stand_eligibility;

    /** Oznaka da li je kolateral B2 IRB prihvatljiv. */
    public boolean b2_irb_eligibility;

    /** Oznaka da li je kolateral ND prihvatljiv. */
    public boolean nd_eligibility;
    
    /** Oznaka da li je kolateral prihvatljiv za vrstu prihvatljivosti za koju se radi izra�un. */
    public boolean isEligible;
    
    /** Oznaka ("D" ili "N") da li je kolateral prihvatljiv za vrstu prihvatljivosti za koju se radi izra�un. */
    public String eligibility;
    

    /** Izdavatelj kolaterala (garancije). */
    public CustomerData issuer;


    /** Neiskori�teni (preostali) dio vrijednosti kolaterala. */
    public BigDecimal remaining_value;
    
    
    /** Iznos kolaterala iskori�ten za pokrivanje valutno uskla�enih plasmana. */
    public BigDecimal amountUsedForCurrencyMatchedPlacements;
    
    /** Iznos kolaterala iskori�ten za pokrivanje valutno neuskla�enih plasmana. */
    public BigDecimal amountUsedForCurrencyMismatchedPlacements;
    
    /** Realan ponder kolaterala - prosje�an ponder kori�ten prilikom pokrivanja plasmana. */
    public BigDecimal real_ponder;


    /** Kolekcija hipoteka kolaterala. */
    public TreeMap<Integer, MortgageData> mortgages;
    
    /** Prva RBA hipoteka. */
    public MortgageData firstRbaMortgage;
    
    /** Zadnja RBA hipoteka. */
    public MortgageData lastRbaMortgage;


    /** Kolekcija pokrivenosti kolateralom. */
    public ArrayList<CoverageData> coverages;
    
    /** Kolekcija pokrivenosti kolateralom koje su prihvatljive za izra�un. */
    public ArrayList<CoverageData> eligibleCoverages;


    /** Oznaka da li se kolateral nalazi u podacima o izra�unu pokrivenosti zapisanima u bazu. */
    public boolean isWrittenToDatabase;



    /** Metoda vra�a da li kolateral pripada kategoriji gotovinskih depozita. */
    public boolean isCashDeposit()
    {
        return category != null && CollateralCategoryData.cash_dep_col_cat_id.equals(category.col_cat_id);
    }
    
    /** Metoda vra�a da li kolateral pripada kategoriji garancija. */
    public boolean isGuarantee()
    {
        return category != null && CollateralCategoryData.guarantee_col_cat_id.equals(category.col_cat_id);
    }
    
    /** Metoda vra�a da li kolateral pripada kategoriji nekretnina. */
    public boolean isRealEstate()
    {
        return category != null && CollateralCategoryData.real_est_col_cat_id.equals(category.col_cat_id);
    }
    
    /** Metoda vra�a da li kolateral pripada tipu depozita za netiranje. */
    public boolean isNettingCashDeposit()
    {
        return type != null && ( CollateralTypeData.cash_dep_netting_hrk_col_typ_id.equals(type.col_typ_id) || CollateralTypeData.cash_dep_netting_eur_col_typ_id.equals(type.col_typ_id) );
    }


    /**
     * Metoda vra�a da li kolateral ulazi u zadanu vrstu izra�una pokrivenosti.
     * U izra�un ulaze aktivni kolaterali kojima je nominalna vrijednost na datum valute bila ve�a od 0.
     * Depoziti za netiranje u kunama i depoziti za netiranje u valuti ne ulaze u izra�un pokrivenosti.
     * @param allocationType Vrsta izra�una pokrivenosti
     * @return true ako kolateral ulazi u izra�un, false ako ne ulazi
     */
    public boolean isEligibleFor(AllocationTypeData allocationType) throws Exception
    {
        // provjeri status kolaterala
        if (!collateral_status.equalsIgnoreCase("3")) return false;
        
        // vrijednost kolaterala mora biti ve�a od nule
        if (ncv == null || ncv.compareTo(zero) <= 0) return false;
        
        // u izra�un ne ulaze depoziti za netiranje
        if (isNettingCashDeposit()) return false;
        
        // provjeri da li je kolateral prihvatljiv za zadanu vrstu izra�una
        if (allocationType.eligibilityType == EligibilityType.RBA)
        {
            if (allocationType.additionalType == AdditionalType.Micro)  // izra�un za Micro klijente u obzir uzima samo nekretnine i gotovinske depozite
            {
                return rba_eligibility && (isCashDeposit() || isRealEstate());
            }
            else
            {
                return rba_eligibility;
            }
        }
        else if (allocationType.eligibilityType == EligibilityType.Reservation)
        {
            return reservation_eligibility;
        }
        else if (allocationType.eligibilityType == EligibilityType.B2HNBStand)
        {
            return b2_hnb_stand_eligibility;
        }
        else if (allocationType.eligibilityType == EligibilityType.B2IRB)
        {
            return b2_irb_eligibility;
        }
        else if (allocationType.eligibilityType == EligibilityType.ND)
        {
            return nd_eligibility;
        }
        else
        {
            throw new Exception("Nepoznata vrsta prihvatljivosti " + allocationType.eligibilityType + "!");
        }
    }
}



/**
 * Klasa koja predstavlja kategoriju kolaterala.
 * @author hrakis
 */
class CollateralCategoryData
{
    /** ID kategorije kolaterala. */
    public BigDecimal col_cat_id;
    
    /** Oznaka kategorije kolaterala. */
    public String code;
    
    /** Naziv kategorije kolaterala. */
    public String name;
    
    /** ID kategorije gotovinskih depozita. */
    public static final BigDecimal cash_dep_col_cat_id = new BigDecimal("612223");
    
    /** ID kategorije garancija. */
    public static final BigDecimal guarantee_col_cat_id = new BigDecimal("615223");
    
    /** ID kategorije nekretnina. */
    public static final BigDecimal real_est_col_cat_id = new BigDecimal("618223");
    
    public String toString()
    {
        return name;
    }
}


/**
 * Klasa koja predstavlja vrstu kolaterala.
 * @author hrakis
 */
class CollateralTypeData
{
    /** ID vrste kolaterala. */
    public BigDecimal col_typ_id;
    
    /** Oznaka vrste kolaterala. */
    public String code;
    
    /** Naziv vrste kolaterala. */
    public String name;
    
    /** ID tipa depozita za netiranje u kunama. */
    public static final BigDecimal cash_dep_netting_hrk_col_typ_id = new BigDecimal("76777");
    
    /** ID tipa depozita za netiranje u eurima. */
    public static final BigDecimal cash_dep_netting_eur_col_typ_id = new BigDecimal("77777");
    
    public String toString()
    {
        return name;
    }
}


/**
 * Klasa koja predstavlja podvrstu kolaterala.
 * @author hrakis
 */
class CollateralSubtypeData
{
    /** ID podvrste kolaterala. */
    public BigDecimal col_sub_id;
    
    /** Oznaka podvrste kolaterala. */
    public String code;
    
    /** Naziv podvrste kolaterala. */
    public String name;
    
    public String toString()
    {
        return name;
    }
}


/**
 * Klasa koja predstavlja CCF (credit conversion factor) grupu.
 * @author hrakis 
 */
class CcfGroupData
{
    /** ID CCF grupe. */
    public BigDecimal ccf_gro_id;
    
    /** Korisni�ka �ifra CCF grupe. */
    public String code;
    
    /** Naziv CCF grupe. */
    public String name;
    
    /** CCF vrijednost izra�ena u postotcima. */
    public BigDecimal ccf_value;
}



/**
 * Klasa koja predstavlja ponder za kategoriju/vrstu/podvrstu kolaterala.
 * @author hrakis
 */
class PonderData
{
    /** Kategorija kolaterala. */
    public CollateralCategoryData category;
    
    /** Vrsta kolaterala. */
    public CollateralTypeData type;
    
    /** Podvrsta kolaterala. */
    public CollateralSubtypeData subtype;
    
    /** Dodatni uvjet koji odre�uje ponder. */
    public String addRequest;
    
    /** Defaultni ponder izra�en u postotcima. */
    public BigDecimal defaultValue; 
}


/**
* Klasa koja predstavlja vezu basel_cus_type i b2 grupe.
* @author hraskd 
*/
class B2GroupData
{
/** Korisni�ka �ifra basel_cus_type */
public String b2_code;

/** �ifra b2 grupe. */
public String b2_group_code;
}


/**
* Klasa koja predstavlja vezu b2 grupe i eir/arpoc parametra.
* @author hraskd 
*/
class EirArPocParamData
{
/** Korisni�ka �ifra b2 grupe */
public String b2_group_code;

/** eir/arpoc postotak */
public BigDecimal value;
}


/**
 * Klasa koja predstavlja vrstu prihvatljivosti.
 * @author hrakis
 */
enum EligibilityType
{
    /** RBA prihvatljivost. */
    RBA,
    
    /** Prihvatljivost za rezervacije. (ex-B1 prihvatljivost) */
    Reservation,
    
    /** B2 HNB Standard prihvatljivost. */
    B2HNBStand,
    
    /** B2 IRB prihvatljivost. */
    B2IRB,
    
    /** ND prihvatljivost. */
    ND;
    
    public String toString()
    {
        switch(this)
        {
            case RBA: return "RBA prihvatljivost";
            case Reservation: return "Prihvatljivost za rezervacije";
            case B2HNBStand: return "B2 HNB Standard prihvatljivost";
            case B2IRB: return "B2 IRB prihvatljivost";
            case ND: return "ND prihvatljivost";
            default: return super.toString();
        }
    }
    
    public int toInt()
    {
        switch(this)
        {
            case RBA: return 0;
            case Reservation: return 1;
            case B2HNBStand: return 2;
            case B2IRB: return 3;
            case ND: return 4;
            default: return -1;
        }
    }
}


/**
 * Klasa koja predstavlja vrstu podataka koji ulaze u izra�un.  
 * @author hrakis
 */
enum DataType
{
    /** Redovni podaci po datumu valute analitike (DVA). */
    Regular,
    
    /** Podaci po datumu valute glavne knjige (DGK). */
    GeneralLedger;
    
    public String toString()
    {
        switch(this)
        {
            case Regular: return "Redovni podaci po datumu valute analitike";
            case GeneralLedger: return "Podaci po datumu valute glavne knjige";
            default: return super.toString();
        }
    }
    
    /** Metoda vra�a slov�anu oznaku vrste podataka. */
    public String getExpTypeInd() throws Exception
    {
        if (this == DataType.Regular) return "DVA";              // redovni podaci po datumu valute analitike
        else if (this == DataType.GeneralLedger) return "DGK";   // podaci po datumu valute glavne knjige
        else throw new Exception("Nepoznata vrsta podataka " + this + "!");      
    }
}


/**
 * Klasa koja predstavlja vrstu pondera koji se primjenjuje u izra�unu. 
 * @author hrakis
 */
enum PonderType
{
    /** Ponderirani izra�un. */
    Ponder,
    
    /** Neponderirani izra�un. */
    NoPonder;
    
    public String toString()
    {
        switch(this)
        {
            case Ponder: return "Ponderirani izracun";
            case NoPonder: return "Neponderirani izracun";
            default: return super.toString();
        }
    }
}


/** 
 * Klasa koja predstavlja dodatni uvjet koji se primjenjuje u izra�unu. 
 * @author hrakis
 */
enum AdditionalType
{
    /** Bez dodatnog uvjeta. */
    None,
    
    /** Izra�un samo za Micro klijente. */
    Micro,
    
    /** Alocirani kolateral bez rezanja na izlo�enost. */
    Unlimited;
    
    public String toString()
    {
        switch(this)
        {
            case None: return "";
            case Micro: return "Micro klijenti";
            case Unlimited: return "Alocirani kolateral bez rezanja na izlozenost";
            default: return super.toString();
        }
    }
}


/**
 * Klasa koja predstavlja vrstu izra�una pokrivenosti.
 * <table>
 *  <caption>Mogu�e vrste izra�una pokrivenosti</caption>
 *  <tr> <td>Oznaka vrste obrade</td> <td>Vrsta prihvatljivosti</td> <td>Ponderirani / neponderirani izra�un</td> <td>Datum valute analitike (DVA) / Datum valute glavne knjige (DGK)</td> </tr>
 *  <tr> <td>N</td>  <td>RBA</td>                           <td>neponderirani</td> <td>DVA</td> </tr>
 *  <tr> <td>NG</td> <td>RBA</td>                           <td>neponderirani</td> <td>DGK</td> </tr>
 *  <tr> <td>P</td>  <td>RBA</td>                           <td>ponderirani</td>   <td>DVA</td> </tr>
 *  <tr> <td>PG</td> <td>RBA</td>                           <td>ponderirani</td>   <td>DGK</td> </tr>
 *  <tr> <td>C</td>  <td>Prihvatljivost za rezervacije</td> <td>neponderirani</td> <td>DVA</td> </tr>
 *  <tr> <td>CG</td> <td>Prihvatljivost za rezervacije</td> <td>neponderirani</td> <td>DGK</td> </tr>
 *  <tr> <td>E</td>  <td>Prihvatljivost za rezervacije</td> <td>ponderirani</td>   <td>DVA</td> </tr>
 *  <tr> <td>EG</td> <td>Prihvatljivost za rezervacije</td> <td>ponderirani</td>   <td>DGK</td> </tr>
 *  <tr> <td>H</td>  <td>B2 HNB Standard</td>               <td>neponderirani</td> <td>DVA</td> </tr>
 *  <tr> <td>HG</td> <td>B2 HNB Standard</td>               <td>neponderirani</td> <td>DGK</td> </tr>
 *  <tr> <td>I</td>  <td>B2 HNB Standard</td>               <td>ponderirani</td>   <td>DVA</td> </tr>
 *  <tr> <td>IG</td> <td>B2 HNB Standard</td>               <td>ponderirani</td>   <td>DGK</td> </tr>
 *  <tr> <td>J</td>  <td>B2 IRB</td>                        <td>neponderirani</td> <td>DVA</td> </tr>
 *  <tr> <td>JG</td> <td>B2 IRB</td>                        <td>neponderirani</td> <td>DGK</td> </tr>
 *  <tr> <td>K</td>  <td>B2 IRB</td>                        <td>ponderirani</td>   <td>DVA</td> </tr>
 *  <tr> <td>KG</td> <td>B2 IRB</td>                        <td>ponderirani</td>   <td>DGK</td> </tr>
 *  <tr> <td>L</td>  <td>ND</td>                            <td>neponderirani</td> <td>DVA</td> </tr>
 *  <tr> <td>M</td>  <td>ND</td>                            <td>ponderirani</td>   <td>DVA</td> </tr>
 *  <tr> <td>T</td>  <td>RBA MICRO</td>                     <td>ponderirani</td>   <td>DVA</td> </tr>
 * </table>
 * @author hrakis
 */
class AllocationTypeData
{
    private static HashMap<String, AllocationTypeData> types;
    
    /** Vrsta prihvatljivosti koja se gleda u izra�unu. */
    public EligibilityType eligibilityType;
    
    /** Vrsta pondera koji se primjenjuje u izra�unu. */
    public PonderType ponderType;
    
    /** Vrsta podataka koji ulaze u izra�un. */
    public DataType dataType;
    
    /** Dodatni uvjet koji se primjenjuje u izra�unu. */
    public AdditionalType additionalType;
    
    
    static
    {
       types = new HashMap<String, AllocationTypeData>();
       
       types.put("N",  new AllocationTypeData(EligibilityType.RBA, PonderType.NoPonder, DataType.Regular,       AdditionalType.None));
       types.put("NG", new AllocationTypeData(EligibilityType.RBA, PonderType.NoPonder, DataType.GeneralLedger, AdditionalType.None));
       types.put("NU", new AllocationTypeData(EligibilityType.RBA, PonderType.NoPonder, DataType.Regular,       AdditionalType.Unlimited));
       types.put("P",  new AllocationTypeData(EligibilityType.RBA, PonderType.Ponder,   DataType.Regular,       AdditionalType.None));
       types.put("PG", new AllocationTypeData(EligibilityType.RBA, PonderType.Ponder,   DataType.GeneralLedger, AdditionalType.None));
       types.put("PU", new AllocationTypeData(EligibilityType.RBA, PonderType.Ponder,   DataType.Regular,       AdditionalType.Unlimited));       
       types.put("T",  new AllocationTypeData(EligibilityType.RBA, PonderType.Ponder,   DataType.Regular,       AdditionalType.Micro));
       
       types.put("C",  new AllocationTypeData(EligibilityType.Reservation, PonderType.NoPonder, DataType.Regular,       AdditionalType.None));
       types.put("CG", new AllocationTypeData(EligibilityType.Reservation, PonderType.NoPonder, DataType.GeneralLedger, AdditionalType.None));
       types.put("CU", new AllocationTypeData(EligibilityType.Reservation, PonderType.NoPonder, DataType.Regular,       AdditionalType.Unlimited));
       types.put("E",  new AllocationTypeData(EligibilityType.Reservation, PonderType.Ponder,   DataType.Regular,       AdditionalType.None));
       types.put("EG", new AllocationTypeData(EligibilityType.Reservation, PonderType.Ponder,   DataType.GeneralLedger, AdditionalType.None));
       types.put("EU", new AllocationTypeData(EligibilityType.Reservation, PonderType.Ponder,   DataType.Regular,       AdditionalType.Unlimited));
       
       types.put("H",  new AllocationTypeData(EligibilityType.B2HNBStand, PonderType.NoPonder, DataType.Regular,        AdditionalType.None));
       types.put("HG", new AllocationTypeData(EligibilityType.B2HNBStand, PonderType.NoPonder, DataType.GeneralLedger,  AdditionalType.None));
       types.put("HU", new AllocationTypeData(EligibilityType.B2HNBStand, PonderType.NoPonder, DataType.Regular,        AdditionalType.Unlimited));
       types.put("I",  new AllocationTypeData(EligibilityType.B2HNBStand, PonderType.Ponder,   DataType.Regular,        AdditionalType.None));
       types.put("IG", new AllocationTypeData(EligibilityType.B2HNBStand, PonderType.Ponder,   DataType.GeneralLedger,  AdditionalType.None));
       types.put("IU", new AllocationTypeData(EligibilityType.B2HNBStand, PonderType.Ponder,   DataType.Regular,        AdditionalType.Unlimited));
       
       types.put("J",  new AllocationTypeData(EligibilityType.B2IRB, PonderType.NoPonder, DataType.Regular,         AdditionalType.None));
       types.put("JG", new AllocationTypeData(EligibilityType.B2IRB, PonderType.NoPonder, DataType.GeneralLedger,   AdditionalType.None));
       types.put("JU", new AllocationTypeData(EligibilityType.B2IRB, PonderType.NoPonder, DataType.Regular,         AdditionalType.Unlimited));
       types.put("K",  new AllocationTypeData(EligibilityType.B2IRB, PonderType.Ponder,   DataType.Regular,         AdditionalType.None));
       types.put("KG", new AllocationTypeData(EligibilityType.B2IRB, PonderType.Ponder,   DataType.GeneralLedger,   AdditionalType.None));
       types.put("KU", new AllocationTypeData(EligibilityType.B2IRB, PonderType.Ponder,   DataType.Regular,         AdditionalType.Unlimited));
       
       types.put("M",  new AllocationTypeData(EligibilityType.ND, PonderType.Ponder,    DataType.Regular, AdditionalType.None));
       types.put("MU", new AllocationTypeData(EligibilityType.ND, PonderType.Ponder,    DataType.Regular, AdditionalType.Unlimited));
       types.put("L",  new AllocationTypeData(EligibilityType.ND, PonderType.NoPonder,  DataType.Regular, AdditionalType.None));
       types.put("LU", new AllocationTypeData(EligibilityType.ND, PonderType.NoPonder,  DataType.Regular, AdditionalType.Unlimited));
    }
    
    public AllocationTypeData(EligibilityType eligibilityType, PonderType ponderType, DataType dataType, AdditionalType additionalType)
    {
        this.eligibilityType = eligibilityType;
        this.ponderType = ponderType;
        this.dataType = dataType;
        this.additionalType = additionalType;
    }
    
    /**
     * Metoda za zadanu oznaku vrste obrade vra�a vrstu izra�una pokrivenosti.
     * @param proc_type oznaka vrste obrade
     * @return vrsta izra�una pokrivenosti
     */
    public static AllocationTypeData getAllocationType(String proc_type) throws Exception
    {
        AllocationTypeData type = types.get(proc_type);
        if (type == null) throw new Exception("Nepoznata oznaka vrste obrade " + proc_type + "!");
        return type;
    }

    public String toString()
    {
        return eligibilityType + " - " + ponderType + " - " + dataType + " - " + additionalType;
    }
}


/**
 * Klasa koja predstavlja hipoteku.
 * @author hrakis
 */
class MortgageData
{
    /** ID Raiffeisen banke u CUSTOMER tablici. */
    public static final BigDecimal rba_cus_id = new BigDecimal("8218251");

    /** ID hipoteke. */
    public BigDecimal coll_hf_prior_id;

    /** Kolateral hipoteke. */
    public CollateralData collateral;

    /** Prioritet hipoteke. Manji broj ozna�ava ve�i prioritet. */
    public Integer priority;


    /** Iznos hipoteke u originalnoj valuti. */
    public BigDecimal amount_ref;

    /** Valuta iznosa hipoteke. */ 
    public BigDecimal cur_id_ref;

    /** Iznos hipoteke izra�en u doma�oj valuti. */
    public BigDecimal amount;


    /** Oznaka da li je hipoteka RBA hipoteka. */
    public boolean isRBA;
    
    /** Oznaka da li je hipoteka prva RBA hipoteka. */
    public boolean isFirstRBA;
    
    /** Oznaka da li je hipoteka zadnja RBA hipoteka. */
    public boolean isLastRBA;
    
    /** Suma iznosa hipoteka drugih banaka vi�eg reda (od reda ove hipoteke) izra�ena u doma�oj valuti. */
    public BigDecimal otherMortgageAmount;


    /** Okvirni sporazum. */
    public FrameAgreementData frameAgreement;


    /** Oznaka da li je hipoteka za sindicirani plasman. */
    public boolean isSindicated;

    /** Udio RBA u sindiciranom plasmanu izra�en u postotku. */
    public BigDecimal rba_sindic_part;
    
    /** Udio drugih banaka u sindiciranom plasmanu izra�en u postoku. */
    public BigDecimal other_sindic_part;

    /** Iznos hipoteke drugih banaka u sindiciranom plasmanu izra�en u valuti hipoteke. */
    public BigDecimal other_sindic_amount_ref;
    
    /** Iznos hipoteke drugih banaka u sindiciranom plasmanu izra�en u doma�oj valuti. */
    public BigDecimal other_sindic_amount;
    
    /** Preostali (neiskori�teni) iznos hipoteke drugih banaka u sindiciranom plasmanu izra�en u doma�oj valuti. */
    public BigDecimal remaining_other_sindic_amount;

    
    /** WCOV na razini hipoteke. */
    public BigDecimal wcovh;
    
    /** Execution value. */
    public BigDecimal execution_value;

    
    /** Oznaka da li se hipoteka nalazi u podacima o izra�unu pokrivenosti zapisanima u bazu. */
    public boolean isWrittenToDatabase;
}



/**
 * Klasa koja predstavlja komitenta.
 * @author hrakis
 */
class CustomerData
{
    /** ID komitenta. */
    public BigDecimal cus_id;

    /** Interni MB komitenta. */
    public String register_no;

    /** Naziv komitenta. */
    public String name;

    /** Vrsta komitenta prema Basel2 specifikaciji. */
    public String basel_cus_type;


    /** Rating komitenta. */
    public RatingData rating;
}



/**
 * Klasa koja definira ocjenu i pripadaju�i probability of default.
 * @author hrakis
 */
class RatingData
{
    /** ID vrste ocjene. */
    BigDecimal rat_typ_id;
    
    /** Oznaka vrste ocjene. */
    String rat_typ_code;
    
    /** ID ocjene. */
    BigDecimal rat_sco_id;
    
    /** Vrijednost ocjene. */
    String score;
    
    /** Probability of default. */
    BigDecimal pd;
}




/**
 * Klasa koja predstavlja plasman.
 * @author hrakis
 */
class PlacementData
{
    public PlacementData()
    {
        this.coverages = new ArrayList<CoverageData>();
        this.eligibleCoverages = new ArrayList<CoverageData>();
    }
    
    /** ID plasmana. */
    public BigDecimal cus_acc_id;

    /** Partija plasmana. */
    public String cus_acc_no;

    /** Oznaka modula iz kojeg su dobiveni podaci. */
    public String module_code;
    
    /** �ifra proizvoda. */
    public String prod_code;


    /** Stanje izlo�enosti u valuti izlo�enosti. */
    public BigDecimal exposure_balance;

    /** Valuta u kojoj je izra�ena izlo�enost. */
    public BigDecimal exposure_cur_id;

    /** Stanje izlo�enosti izra�eno u doma�oj valuti. */
    public BigDecimal exposure;

    /** Nepokriveni (preostali) dio plasmana izra�en u doma�oj valuti. */
    public BigDecimal remaining_exposure;
    

    /** Nepokriveni (preostali) dio bilan�nog iznosa izlo�enosti plasmana izra�en u doma�oj valuti. */
    public BigDecimal remaining_exposure_balance;
    
    /** Nepokriveni (preostali) dio vanbilan�nog iznosa izlo�enosti plasmana izra�en u doma�oj valuti. */
    public BigDecimal remaining_exposure_off_balance;
    
    
    /** Ugovorena valuta plasmana. */
    public BigDecimal contract_cur_id;
    
    /** Da li je plasman s valutnom klauzulom. */
    public boolean isCurrencyClause;


    /** Vlasnik plasmana. */
    public CustomerData owner;

    /** Okvir iz kojeg je plasman. */
    public PlacementData frame;
    
    /** Da li je plasman okvir. */
    public boolean isFrame;
    
    /** Kolekcija plasmana iz okvira. Popunjena samo ako je plasman okvir. */
    public ArrayList<PlacementData> framePlacements;

    /** Credit conversion factor. */
    public BigDecimal ccf;


    /** Stanje ukupne izlo�enosti po partiji u kunama - bilan�no. */
    public BigDecimal exposure_bal_lcy;

    /** Stanje ukupne izlo�enosti po partiji u kunama - vanbilan�no. */
    public BigDecimal exp_off_bal_lcy;


    /** Kolekcija pokrivenosti plasmana. */
    public ArrayList<CoverageData> coverages;
    
    /** Kolekcija pokrivenosti plasmana koje su prihvatljive za izra�un. */
    public ArrayList<CoverageData> eligibleCoverages;
    
    
    /** Oznaka da li se plasman nalazi u podacima o izra�unu pokrivenosti zapisanima u bazu. */
    public boolean isWrittenToDatabase;
    
    
    /**
     * Metoda vra�a da li plasman ulazi u zadanu vrstu izra�una pokrivenosti.  
     * @param allocationType Vrsta izra�una pokrivenosti
     * @return true ako plasman ulazi u izra�un, false ako ne ulazi
     */
    public boolean isEligibleFor(AllocationTypeData allocationType) throws Exception
    {
        if (allocationType.additionalType == AdditionalType.Micro)
        {
            return owner.basel_cus_type.trim().equalsIgnoreCase("20");
        }
        else
        {
            return true;
        }
    }
    
    /**
     * Metoda vra�a da li je plasman valutno uskla�en sa zadanim kolateralom. 
     * @param collateral objekt s podacima o kolateralu
     * @return da li je plasman valutno uskla�en s kolateralom
     */
    public boolean isCurrencyMatchedWithCollateral(CollateralData collateral)
    {
        if (collateral.real_est_nm_cur_id == null) return false;
        
        if (isCurrencyClause)
        {
            return contract_cur_id.compareTo(collateral.real_est_nm_cur_id) == 0;
        }
        else
        {
            return exposure_cur_id.compareTo(collateral.real_est_nm_cur_id) == 0;
        }
    }
}



/**
 * Klasa koja predstavlja okvirni sporazum.
 * @author hrakis
 */
class FrameAgreementData
{
    public FrameAgreementData()
    {
        this.mortgages = new HashSet<MortgageData>();
        this.collaterals = new HashSet<CollateralData>();
    }
    
    /** ID okvirnog sporazuma. */
    public BigDecimal fra_agr_id;

    /** Broj sporazuma. */
    public String agreement_no;


    /** Iznos sporazuma u originalnoj valuti. */
    public BigDecimal amount_ref;

    /** Valuta iznosa sporazuma. */
    public BigDecimal cur_id_ref;

    /** Iznos sporazuma izra�en u doma�oj valuti. */
    public BigDecimal amount;
    
    
    /** Kolekcija hipoteka iz okvirnog sporazuma. */
    public HashSet<MortgageData> mortgages;
    
    /** Kolekcija kolaterala iz okvirnog sporazuma. */
    public HashSet<CollateralData> collaterals; 


    /** Neiskori�teni iznos sporazuma izra�en u doma�oj valuti. */
    public BigDecimal remaining_amount;
}



/**
 * Klasa koja predstavlja vezu izme�u kolaterala i plasmana.
 * @author hrakis
 */
class CoverageData implements Comparable<CoverageData>
{
    /** Redni broj redoslijeda prilikom raspodjele. */
    public Integer priority;

    
    /** Kolateral koji pokriva plasman. */
    public CollateralData collateral;

    /** Hipoteka kolaterala koji pokriva plasman. */
    public MortgageData mortgage;

    /** Plasman koji je pokriven plasmanom. */
    public PlacementData placement;


    /** Oznaka da li je veza 1:1. */
    public Boolean isOneOnOneRelationship;

    /** Oznaka da li su kolateral i plasman valutno uskla�eni. */
    public Boolean isCurrencyMatched;
    
    /** Oznaka da li je kolateral direktno povezan na plasman iz okvira. */
    public Boolean isDirectlyRelatedToPlacement;
    
    /** Oznaka da li je veza za sindicirani plasman. */
    public Boolean isSindicated;
    
    /** Oznaka da li je veza stvorena samo za potrebe izra�una pokrivenosti. */
    public boolean isVirtual;


    /** Iznos kojim kolateral pokriva plasman izra�en u doma�oj valuti. */
    public BigDecimal amount;

    /** Iznos kolaterala potro�en za pokrivanje. Mo�e se razlikovati od <code>amount</code> ako je Hfx > 0%. */
    public BigDecimal collateralAmountUsed;
    
    
    /** Iznos kojim kolateral pokriva bilan�ni iznos izlo�enosti plasmana izra�en u doma�oj valuti. */
    public BigDecimal amount_balance;
    
    /** Iznos kojim kolateral pokriva vanbilan�ni iznos izlo�enosti plasmana izra�en u doma�oj valuti. */
    public BigDecimal amount_off_balance;
    
    /** Iznos kojim kolateral pokriva plasman druge banke u sindiciranom plasmanu izra�en u doma�oj valuti. */
    public BigDecimal amount_other_sindic;
    
    /** Undiscounted WCV Iznos kojim kolateral pokriva plasman  */
    public BigDecimal undiscounted_wcv;
    
    /** Oznaka da li se veza nalazi u podacima o izra�unu pokrivenosti zapisanima u bazu. */
    public boolean isWrittenToDatabase;
    

    /** Metoda za uspore�ivanje dvaju veza. */
    public int compareTo(CoverageData coverage)
    {
        if (coverage == null) return 100;
        
        int c;

        // kriterij 1. veza 1:1 -> jedan na jedan ide prije
        if ((c = coverage.isOneOnOneRelationship.compareTo(this.isOneOnOneRelationship)) != 0) return c;
        
        // kriterij 2. vrsta kolaterala -> manji prioritet ide prije
        if ((c = this.collateral.priority.compareTo(coverage.collateral.priority)) != 0) return c;
        
        // kriterij 3. rating garantora -> ve�i pd ide prije
        if (this.collateral.issuer != null && coverage.collateral.issuer == null) return -1;
        if (this.collateral.issuer == null && coverage.collateral.issuer != null) return 1;
        if (this.collateral.issuer != null && coverage.collateral.issuer != null && (c = coverage.collateral.issuer.rating.pd.compareTo(this.collateral.issuer.rating.pd)) != 0) return c;
        
        // implicitni kriterij - �ifra kolaterala -> manja �ifra ide prije
        if ((c = this.collateral.col_num.compareTo(coverage.collateral.col_num)) != 0) return c;

        // kriterij 4. red hipoteke -> manji red hipoteke ide prije
        if (this.mortgage != null && coverage.mortgage == null) return -1;
        if (this.mortgage == null && coverage.mortgage != null) return 1;
        if (this.mortgage != null && coverage.mortgage != null && (c = this.mortgage.priority.compareTo(coverage.mortgage.priority)) != 0) return c;

        // kriterij 5. rating vlasnika plasmana -> ve�i pd ide prije
        if ((c = coverage.placement.owner.rating.pd.compareTo(this.placement.owner.rating.pd)) != 0) return c;

        // kriterij 6. valutna uskla�enost -> valutno uskla�eni idu prije
        if ((c = coverage.isCurrencyMatched.compareTo(this.isCurrencyMatched)) != 0) return c;

        // kriterij 7. CCF vrijednost plasmana -> ve�a CCF vrijednost ide prije
        return (c = coverage.placement.ccf.compareTo(this.placement.ccf));
    }
    
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("veza ");
        if (isOneOnOneRelationship) buffer.append("1:1"); else buffer.append("X:X");
        buffer.append(", ");
        
        buffer.append("prioritet kol=");
        buffer.append(collateral.priority);
        buffer.append(", ");
        
        buffer.append("PD garantora=");
        if (collateral.issuer != null) buffer.append(collateral.issuer.rating.pd); else buffer.append("nema");
        buffer.append(", ");
        
        buffer.append("red hipoteke=");
        if (mortgage != null) buffer.append(mortgage.priority); else buffer.append("nema");
        buffer.append(", ");
        
        buffer.append("PD vlasn pl=");
        buffer.append(placement.owner.rating.pd);
        buffer.append(", ");
        
        buffer.append("val uskl=");
        buffer.append(isCurrencyMatched);
        buffer.append(", ");
        
        buffer.append("CCF=");
        buffer.append(placement.ccf);

        if (isSindicated) buffer.append(" [SND]");
        
        return buffer.toString(); 
    }
}
