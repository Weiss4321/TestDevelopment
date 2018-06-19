package hr.vestigo.modules.collateral.batch.bo93;

import java.math.BigDecimal;
import java.sql.Date;


/**
 * Klasa koja predstavlja kolateral.
 * @author hrakis
 */
abstract class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo93/CollateralData.java,v 1.2 2014/10/22 11:09:58 hrakis Exp $";
    
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** Šifra kolaterala. */
    public String col_num;
    
    /** Status kolaterala. */
    public String collateral_status; 
    
    /** ID kategorije kolaterala. */
    public BigDecimal col_cat_id;
    
    /** Naziv kategorije kolaterala. */
    public String col_cat_name;

    /** ID vrste kolaterala. */
    public BigDecimal col_typ_id;
    
    /** Naziv vrste kolaterala. */
    public String col_typ_name;
    
    /** ID podvrste kolaterala. */
    public BigDecimal col_sub_id;
    
    /** Naziv podvrste kolaterala. */
    public String col_sub_name;
    
    /** Nominalna vrijednost kolaterala. */
    public BigDecimal nomi_value;
    
    /** ID valute nominalne vrijednosti kolaterala. */
    public BigDecimal nomi_value_cur_id;
    
    /** Oznaka valute nominalne vrijednosti kolaterala. */
    public String nomi_value_cur_code;
    
    /** Datum nominalne vrijednosti kolaterala. */
    public Date nomi_value_date;
    
    /** Datum procjene. */
    public Date est_date;
    
    /** Naèin kako je prošla revalorizacija uraðena. (A-automatski, R-ruèno) */
    public String reva_date_am;
    
    /** ID zapisa u tablici COLL_REVA koji sadrži podatke o revalorizaciji. */
    public BigDecimal col_rev_id;
    
    /** Koeficijent revalorizacije. */
    public BigDecimal koef_rev;
    
    /** Nova nominalna vrijednost nakon revalorizacije. */
    public BigDecimal new_nomi_value;
    
    /** ID valute nove nominalne vrijednosti nakon revalorizacije. */
    public BigDecimal new_nomi_value_cur_id;
    
    /** Oznaka valute nove nominalne vrijednosti nakon revalorizacije. */
    public String new_nomi_value_cur_code;
    
    /** Vrsta komitenta - CO (Corporate) ili RET (Retail). */
    public String customer_type;
}


/**
 * Klasa koja predstavlja vozilo.
 * @author hrakis
 */
class VehicleData extends CollateralData
{
    /** Godina proizvodnje vozila. */
    public String veh_made_year;
}


/**
 * Klasa koja predstavlja nekretninu.
 * @author hrakis
 */
class RealEstateData extends CollateralData
{
    /** Katastarska opæina u kojoj se nalazi nekretnina. */
    public LocationData cadastral;
    
    /** Grad u kojem se nalazi nekretnina. */
    public LocationData city;
    
    /** Županija u kojoj se nalazi nekretnina. */
    public LocationData county;
}



/**
 * Klasa koja predstavlja lokaciju kolaterala.
 * Može biti primijenjena za katastarsku opæinu, mjesto, županiju.
 * @author hrakis
 */
class LocationData
{
    public LocationData(BigDecimal id, String code, String name, String type)
    {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
    }
    
    /** ID lokacije. */
    public BigDecimal id;
    
    /** Šifra lokacije. */
    public String code;
    
    /** Naziv lokacije. */
    public String name;
    
    /** Tip lokacije. */
    public String type;
    
    
    public String toString()
    {
        return String.format("{ id = %1$s, code = '%2$s', name = '%3$s', type = '%4$s' }", id, code, name, type); 
    }
}