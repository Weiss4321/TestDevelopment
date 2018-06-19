package hr.vestigo.modules.collateral.batch.bo90;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * Klasa koja predstavlja kolateral.
 * @author hrakis
 */
public class CollateralData implements Comparable<CollateralData>
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo90/CollateralData.java,v 1.3 2014/11/19 12:11:18 hrakis Exp $";
    
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** �ifra kolaterala. */
    public String col_num;

    
    /** Tr�i�na vrijednost kolaterala. */
    public BigDecimal real_est_nomi_valu;
    
    /** ID valute tr�i�ne vrijednosti kolaterala. */
    public BigDecimal real_est_nm_cur_id;
    
    /** Kod valute tr�i�ne vrijednosti kolaterala. */
    public String real_est_nm_cur_code;
    
    /** Tr�i�na vrijedost kolaterala izra�ena u EUR. */
    public BigDecimal value_eur;

    
    /** ID kategorije kolaterala. */
    public BigDecimal col_cat_id;
    
    /** ID vrste kolaterala. */
    public BigDecimal col_typ_id;
    
    /** Naziv vrste kolaterala. */
    public String col_typ_name;
    
    /** ID podvrste kolaterala. */
    public BigDecimal col_sub_id;
    
    /** Naziv podvrste kolaterala. */
    public String col_sub_name;
    
    
    /** Katastarska op�ina nekretnine. */
    public String cadastre_map_name;
    
    /** Broj zemlji�no-knji�nog ulo�ka. */
    public String real_est_land_regn;
    
    /** Broj zemlji�no-knji�nog podulo�ka. */
    public String real_est_land_sub;
    
    /** Mjesto gdje se nekretnina nalazi. */
    public String city;
    
    
    /** Kolekcija pokrivenosti kolateralom. */
    public ArrayList<CoverageData> coverages = new ArrayList<CoverageData>();
    

    /** Iznos hipoteka vi�eg reda. */
    public BigDecimal other_mortgage_amount;
    
    /** Iskori�tenost kolaterala izra�ena u EUR. */
    public BigDecimal utilization_amount_eur;
    
    /** Iskori�tenost kolaterala izra�ena u postotku. */
    public BigDecimal utilization_percentage;
    
    /** LTV vrijednost. */
    public BigDecimal ltv;
    
    
    /** Realan ponder kolaterala. */
    public BigDecimal real_ponder;
    
    
    /**
     * Metoda vra�a objekt s podacima o pokrivenosti zadanog plasmana.
     * @param cus_acc_id ID plasmana.
     * @return objekt s podacima o pokrivenosti zadanog plasmana ako podaci postoje, ina�e vra�a null.
     */
    public CoverageData getPlacementCoverage(BigDecimal cus_acc_id)
    {
        for (CoverageData coverage : coverages)
        {
            if ((cus_acc_id == null && coverage.cus_acc_id == null) || (cus_acc_id != null && cus_acc_id.equals(coverage.cus_acc_id))) return coverage;
        }
        return null;
    }
    
    private final BigDecimal guar_col_cat_id = new BigDecimal("615223");
    private final BigDecimal real_est_col_cat_id = new BigDecimal("618223");
    
    /** Metoda vra�a da li je kolateral garancija. */
    public boolean isGuarantee()
    {
        return guar_col_cat_id.equals(col_cat_id);
    }
    
    /** Metoda vra�a da li je kolateral nekretnina. */
    public boolean isRealEstate()
    {
        return real_est_col_cat_id.equals(col_cat_id);
    }
    
    /** Metoda za sortiranje - prvi kriterij je iskori�tenost kolaterala, drugi je LTV. */
    public int compareTo(CollateralData collateral)
    {
        int c = this.utilization_percentage.compareTo(collateral.utilization_percentage);
        if (c != 0) return -1 * c; 
        return -1 * this.ltv.compareTo(collateral.ltv);
    }
    
    public String toString()
    {
        return col_num + ", " + real_est_nomi_valu + " " + real_est_nm_cur_code + " = " + value_eur + " EUR, hipoteke = " + other_mortgage_amount + " EUR, iskoristenost = " + utilization_amount_eur + " EUR (" + utilization_percentage + "%), LTV = " + ltv + "%"; 
    }
}


/**
 * Klasa koja predstavlja pokrivenost plasmana kolateralom.
 */
class CoverageData implements Comparable<CoverageData>
{
    /** ID plasmana. */
    public BigDecimal cus_acc_id;
    
    /** Partija plasmana. */
    public String cus_acc_no;


    /** ID vlasnika plasmana. */
    public BigDecimal cus_id;
    
    /** Vlasnik plasmana. */
    public CustomerData placement_owner;

    
    /** Izlo�enost plasmana izra�ena u doma�oj valuti. */
    public BigDecimal exposure_balance_hrk;
    
    /** ID valute izlo�enosti plasmana. */
    public BigDecimal exposure_balance_cur_id;
    
    /** Kod valute izlo�enosti plasmana. */
    public String exposure_balance_cur_code;
    
    /** Izlo�enost plasmana izra�ena u EUR. */
    public BigDecimal exposure_balance_eur;

    
    /** Iznos pokrivenosti plasmana kolateralom izra�en u doma�oj valuti. */
    public BigDecimal coverage_amount_hrk;
    
    /** Iznos pokrivenosti plasmana kolateralom izra�en u EUR. */
    public BigDecimal coverage_amount_eur;

    
    /** Iznos hipoteke izra�en u EUR. */
    public BigDecimal mortgage_amount_eur;
    
    
    /** Metoda za sortiranje - kriterij je naziv vlasnika plasmana. */
    public int compareTo(CoverageData coverage)
    {
        if (placement_owner == null && coverage.placement_owner == null) return 0;
        else if (placement_owner == null && coverage.placement_owner != null) return 1;
        else if (placement_owner != null && coverage.placement_owner == null) return -1;
        else return placement_owner.name.compareTo(coverage.placement_owner.name);
    }
    
    public String toString()
    {
        return "plasman " + cus_acc_no + ", CUS_ID = " + cus_id + ", vlasnik plasmana = [" + placement_owner + "]" + ", izlozenost = " + exposure_balance_hrk + " HRK = " + exposure_balance_eur + " EUR, iskoristeni iznos = " + coverage_amount_hrk + " HRK = " + coverage_amount_eur + " EUR, iznos hipoteke = " + mortgage_amount_eur + " EUR";
    }
}


/**
 * Klasa koja predstavlja komitenta.
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
    
    public String toString()
    {
        return name + " (interni MB = " + register_no + ", CUS_ID = " + cus_id + ", B2 asset klasa = " + basel_cus_type + ")"; 
    }
}


/**
 * Klasa koja predstavlja grupaciju komitenata.
 */
class GroupData
{
    /** ID grupacije. */
    public BigDecimal cus_gro_id;
    
    /** �ifra grupacije. */
    public String code;
    
    /** Naziv grupacije. */
    public String name;
    
    /** Oznaka da li da se prikazuje stupac s vrijednosti LTV. */
    public boolean showLTV;
    
    /** Komitenti koji pripadaju grupaciji. */
    public ArrayList<CustomerData> customers = new ArrayList<CustomerData>();
    
    /** Kolaterali povezani na plasmane komitenata iz grupacije. */
    public ArrayList<CollateralData> collaterals = new ArrayList<CollateralData>();
    
    /** Referenti komitenata. */
    public ArrayList<String> relationshipManagers = new ArrayList<String>();

    
    /**
     * Metoda dohva�a zadanog komitenta iz grupacije.
     * @param cus_id ID komitenta
     * @return vra�a objekt s podacima o komitentu ako je komitent dio grupacije, ina�e vra�a null.
     */
    public CustomerData getCustomer(BigDecimal cus_id)
    {
        for (CustomerData customer : customers)
        {
            if (customer.cus_id.equals(cus_id)) return customer;
        }
        return null;
    }
    
    /**
     * Metoda dohva�a zadani kolateral iz grupacije.
     * @param col_hea_id ID kolaterala
     * @return vra�a objekt s podacima o kolateralu ako je kolateral vezan za grupaciju, ina�e vra�a null.
     */
    public CollateralData getCollateral(BigDecimal col_hea_id)
    {
        for (CollateralData collateral : collaterals)
        {
            if (collateral.col_hea_id.equals(col_hea_id)) return collateral;
        }
        return null;
    }
    
    public String toString()
    {
        return name + " (sifra = " + code + ", CUS_GRO_ID = " + cus_gro_id + ")";  
    }
}