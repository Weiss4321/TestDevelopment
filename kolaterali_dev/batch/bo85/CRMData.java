package hr.vestigo.modules.collateral.batch.bo85;

import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Klasa koja sadrži analitièke i sintetièke podatke o pokrivenosti plasmana kolateralima.
 * @author hrakis
 */
public class CRMData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo85/CRMData.java,v 1.5 2017/09/29 14:34:35 hrazst Exp $";

    /** ID komitenta vlasnika plasmana. */ 
    public BigDecimal cus_id;
    
    /** Interni MB vlasnika plasmana. */
    public String register_no;
    
    /** Naziv vlasnika plasmana. */
    public String name;
    
    /** B2 asset class vlasnika plasmana. */
    public String b2_asset_class;
    
    /** ID plasmana. */
    public BigDecimal cus_acc_id;
    
    /** Broj partije plasmana. */
    public String cus_acc_no;
    
    /** Izloženost plasmana. */
    public BigDecimal exp_balance_hrk;
    
    /** Broj partije okvira iz kojeg je plasman. */
    public String frame_cus_acc_no;
    
    /** Sustav iz kojeg su dobiveni podaci o plasmanu. */
    public String module_code;
    
    /** Status plasmana u sustavu iz kojeg su dobiveni podaci. */
    public String cus_acc_orig_st;
    
    /** Razrada pokrivenosti plasmana po vrstama kolaterala. */
    public LinkedHashMap<GcmTypeData, BigDecimal> amounts;
    

    // ANALITIÈKI PODACI
    
    /** ID komitenta. */
    public BigDecimal col_hea_id;
    
    /** Iznos kolaterala koji pokriva iznos izloženosti. */
    public BigDecimal exp_coll_amount;
    
    /** ID kategorije kolaterala. */
    public BigDecimal col_cat_id;
    
    /** ID vrste kolaterala. */
    public BigDecimal col_typ_id;
    
    /** Naziv vrste kolaterala. */
    public String col_type_name;
    
    /** ID podvrste kolaterala. */
    public BigDecimal col_sub_id;
    
    /** Ponder kolaterala primijenjen u izraèunu pokrivenosti. */
    public BigDecimal ponder;
    
    /** Oznaka da li je kolateral direktno povezan na plasman ili je veza ostvarena preko okvira. */
    public String dir_rel_indic;
   
   
    private static final BigDecimal zero = new BigDecimal("0.00");
    
    /** 
     * Konstruktor klase.
     * @param gcmTypes lista vrsta kolaterala po kojoj treba napraviti razradu.
     */
    public CRMData(ArrayList<GcmTypeData> gcmTypes)
    {
        amounts = new LinkedHashMap<GcmTypeData, BigDecimal>(gcmTypes.size());
        for (GcmTypeData gcmType : gcmTypes) amounts.put(gcmType, zero);
    }
    
    /** Ukupna pokrivenost plasmana (zbroj svih pokrivenosti po vrstama kolaterala). */
    public BigDecimal getAllocatedSum()
    {
        BigDecimal sum = zero;
        for (BigDecimal amount : amounts.values()) sum = sum.add(amount);
        return sum;
    }
    
    /** Metoda koja vraæa da li je plasman na SSP. */
    public boolean isSSP()
    {
        if (module_code == null || cus_acc_orig_st == null) return false;
        String module = module_code.trim();
        String status = cus_acc_orig_st.trim();
        return (module.equals("TRC") && status.equals("E")) ||
               (module.equals("PKR") && status.equals("T")) ||
               (module.equals("PPZ") && status.equals("SS")) ||
               (module.equals("SDR") && status.equals("SS")) ||
               (module.equals("KRD") && status.equals("SS")) ||
               (module.equals("GAR") && status.equals("SS")) ||
               (module.equals("KKR") && (status.equals("94") || status.equals("95"))) ||
               (module.equals("LOC") && status.equals("SS"));
    }
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
 * Klasa koja predstavlja vrstu podataka koji ulaze u izraèun.  
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
    
    /** Metoda vraæa slovèanu oznaku vrste podataka. */
    public String getExpTypeInd() throws Exception
    {
        if (this == DataType.Regular) return "DVA";              // redovni podaci po datumu valute analitike
        else if (this == DataType.GeneralLedger) return "DGK";   // podaci po datumu valute glavne knjige
        else throw new Exception("Nepoznata vrsta podataka " + this + "!");      
    }
}


/**
 * Klasa koja predstavlja vrstu pondera koji se primjenjuje u izraèunu. 
 * @author hrakis
 */
enum PonderType
{
    /** Ponderirani izraèun. */
    Ponder,
    
    /** Neponderirani izraèun. */
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
 * Klasa koja predstavlja dodatni uvjet koji se primjenjuje u izraèunu. 
 * @author hrakis
 */
enum AdditionalType
{
    /** Bez dodatnog uvjeta. */
    None,
    
    /** Izraèun samo za Micro klijente. */
    Micro,
    
    /** Alocirani kolateral bez rezanja na izloženost. */
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
 * Klasa koja predstavlja vrstu izraèuna pokrivenosti.
 * <table>
 *  <caption>Moguæe vrste izraèuna pokrivenosti</caption>
 *  <tr> <td>Oznaka vrste obrade</td> <td>Vrsta prihvatljivosti</td> <td>Ponderirani / neponderirani izraèun</td> <td>Datum valute analitike (DVA) / Datum valute glavne knjige (DGK)</td> </tr>
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
    
    /** Vrsta prihvatljivosti koja se gleda u izraèunu. */
    public EligibilityType eligibilityType;
    
    /** Vrsta pondera koji se primjenjuje u izraèunu. */
    public PonderType ponderType;
    
    /** Vrsta podataka koji ulaze u izraèun. */
    public DataType dataType;
    
    /** Dodatni uvjet koji se primjenjuje u izraèunu. */
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
     * Metoda za zadanu oznaku vrste obrade vraæa vrstu izraèuna pokrivenosti.
     * @param proc_type oznaka vrste obrade
     * @return vrsta izraèuna pokrivenosti
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