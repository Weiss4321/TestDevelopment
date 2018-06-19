package hr.vestigo.modules.collateral.batch.bo99;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 */
public class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo99/CollateralData.java,v 1.5 2017/06/01 10:52:53 hrakis Exp $";
    
    public CollateralData (BigDecimal col_hea_id)
    {
        this.col_hea_id = col_hea_id;
    }
    
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** Datum ulaska u default. */
    public Date default_date;
    
    /** Šifra kolaterala. */
    public String col_num;
    
    /** ID kategorije kolaterala. */
    public BigDecimal col_cat_id;
    
    /** ID vrste kolaterala. */
    public BigDecimal col_typ_id;
    
    /** Naziv vrste kolaterala. */
    public String col_typ_name;
    
    /** ID podvrste kolaterala. */
    public BigDecimal col_sub_id;
    
    /** Šifra liste na kojoj se nalazi kolateral. */
    public String collateral_status;
    
    /** ID valute kolaterala. */
    public BigDecimal cur_id;
    
    /** Oznaka valute kolaterala. */
    public String cur_code;
    
    /** Zadnji datum tržišne vrijednosti kolaterala. */
    public Date last_ncv_date;
    
    /** Tržišna vrijednost kolaterala na last_ncv_date. */
    public BigDecimal last_ncv;
    
    /** Tržišna vrijednost kolaterala (u EUR) na last_ncv_date. */
    public BigDecimal last_ncv_eur;
    
    /** GCTC. */
    public String gctc;
    
    /** Property type. */
    public String property_type;
    
    /** Object type. */
    public String object_type;
    
    /** Real estate indicator. */
    public String real_estate_indicator;
    
    /** Iznos realizacije. */
    public BigDecimal realization_amount;
    
    /** ID valute realizacije. */
    public BigDecimal realization_cur_id;
    
    /** Oznaka valute realizacije. */
    public String realization_cur_code;
    
    /** Iznos realizacije u EUR. */
    public BigDecimal realization_amount_eur;
    
    /** Datum realizacije. */
    public Date realization_date;
    
    /** Vrsta realizacije. */
    public String kind_of_realization;
    
    /** Troškovi realizacije. */
    public BigDecimal realization_cost;
    
    /** Iznos iz kojeg Banka naplaæuje dugovanje. */
    public BigDecimal recovery_amount;
    
    /** ID valute Recovery amounta.  */
    public BigDecimal recovery_cur_id;
    
    /** Oznaka valute Recovery amounta. */
    public String recovery_cur_code;
    
    /** Iznos iz kojeg Banka naplaæuje dugovanje (EUR). */
    public BigDecimal recovery_amount_eur;
    
    /** Komentar naplate. */
    public String recovery_comment;
    
    /** Datum naplate. */
    public Date recovery_date;
    
    /** Vrsta naplate. */
    public String kind_of_recovery;
    
    /** Country of collateral. */
    public String country_of_collateral;
    
    /** Oznaka je li kolateral dio kompleksa. */
    public String part_of_complex;
    
    /** Kolateral na prodaju. */
    public String for_sale;
    
    /** Datum stavljanja kolaterala na prodaju. */
    public Date for_sale_date;
    
    
    /** NCV at default start date. */
    public BigDecimal ncv_default;
    
    /** NCV at default start date EUR. */
    public BigDecimal ncv_default_eur;
    
    /** WCV date. **/
    public Date wcv_date;
    
    /** WCV at default start date. */
    public BigDecimal wcv_default;
    
    /** WCV at default start date EUR.*/
    public BigDecimal wcv_default_eur;
    
    /** WCVOV at default start date. */
    public BigDecimal wcov_default;
    
    /** 1 - realni ponder. */
    public BigDecimal actually_applied_discount;
    
    /** 1 - defaultni ponder. */
    public BigDecimal minimum_discount;
    
    
    /** Datum prvog WCV. */
    public Date first_wcv_date;
    
    /** Iznos prvog WCV. */
    public BigDecimal first_wcv;

    /** Iznos prvog WCV u EUR. */
    public BigDecimal first_wcv_eur;
    
    
    /** Datum zadnjeg WCV. */
    public Date last_wcv_date;
    
    /** Iznos zadnjeg WCV. */
    public BigDecimal last_wcv;

    /** Iznos zadnjeg WCV u EUR. */
    public BigDecimal last_wcv_eur;
    
    
    /** Datum prvog NCV. */
    public Date first_ncv_date;
    
    /** Iznos prvog NCV. */
    public BigDecimal first_ncv;

    /** Iznos prvog NCV u EUR. */
    public BigDecimal first_ncv_eur;

    
    /** Metoda dohvaæa naziv liste na kojoj se kolateral nalazi. */
    public String getCollateralListName()
    {
        if ("0".equalsIgnoreCase(collateral_status)) return "Referentska lista";
        else if ("1".equalsIgnoreCase(collateral_status)) return "Verifikacijska lista";
        else if ("2".equalsIgnoreCase(collateral_status)) return "Autorizacijska lista";
        else if ("3".equalsIgnoreCase(collateral_status)) return "Lista aktivnih";
        else if ("4".equalsIgnoreCase(collateral_status)) return "Lista poni\u0161tenih";
        else if ("5".equalsIgnoreCase(collateral_status)) return "Lista odbijenih";
        else if ("N".equalsIgnoreCase(collateral_status)) return "Lista neaktivnih";
        else if ("F".equalsIgnoreCase(collateral_status)) return "Lista slobodnih";
        else if ("6".equalsIgnoreCase(collateral_status)) return "Lista verifikacije deaktivacije";
        else if ("7".equalsIgnoreCase(collateral_status)) return "Lista kolaterala u vlasni\u0161tvu banke";
        else return "";
    }
    
    
    /** Metoda vraæa da li kolateral ulazi u izvještaj. */
    public boolean isIncludedInReport()
    {
        return ("3".equalsIgnoreCase(collateral_status) || "N".equalsIgnoreCase(collateral_status) || "7".equalsIgnoreCase(collateral_status));
    }
}


class CustomerData
{
    public CustomerData (BigDecimal cus_id)
    {
        this.cus_id = cus_id;
    }
    
    /** ID komitenta vlasnika plasmana. */
    public BigDecimal cus_id;
    
    /** Šifra komitenta vlasnika plasmana. */
    public String register_no;
    
    /** Naziv komitenta vlasnika plasmana. */
    public String name;
    
    /** ID tipa komitenta. */
    public BigDecimal cus_typ_id;
    
    /** Identifikator komitenta u Tiger-u. */
    public String cocunat;
    
    /** Država prebivališta komitenta. */
    public String country_of_residence;
    
    /** B2 Asset Class komitenta. */
    public String b2_asset_class;
    
    /** Kolaterali komitenta. */
    public HashMap<BigDecimal, CollateralData> collaterals = new HashMap<BigDecimal, CollateralData>();
    

    public String getSegmentIndication()
    {
        if (this.cus_typ_id == null) return "";
        
        for (BigDecimal cus_typ_id : retail_cus_typ_id)
        {
            if (cus_typ_id.equals(this.cus_typ_id)) return "Retail";
        }
        
        for (BigDecimal cus_typ_id : corporate_cus_typ_id)
        {
            if (cus_typ_id.equals(this.cus_typ_id)) return "Non-Retail";
        }
        
        return "";
    }
    
    private static final BigDecimal[] retail_cus_typ_id = new BigDecimal[] { new BigDecimal("1999"), new BigDecimal("1998") };
    private static final BigDecimal[] corporate_cus_typ_id = new BigDecimal[] { new BigDecimal("2999"), new BigDecimal("2998"), new BigDecimal("999") };
}