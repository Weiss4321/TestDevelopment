package hr.vestigo.modules.collateral.batch.bo92;

import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;

import java.math.BigDecimal;
import java.sql.Date;


/**
 * Klasa koja predstavlja revalorizacijski koeficijent. 
 * @author hrakis
 */
public class RevaKoefData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo92/RevaKoefData.java,v 1.2 2015/01/29 15:04:52 hrakis Exp $";
    
    /** ID revalorizacijskog koeficijenta. */
    public BigDecimal rev_koe_id;
    
    /** ID vrste kolaterala prema mapiranju za revalorizaciju. */
    public BigDecimal col_gcm_typ_id;
    
    /** Vrsta kolaterala prema mapiranju za revalorizaciju. */
    public String col_gcm_type_name;
    
    /** Županija u kojoj se nalazi nekretnina. */
    public LocationData county;
    
    /** Grad u kojem se nalazi nekretnina. */
    public LocationData city;
    
    /** Katastarska opæina u kojoj se nalazi nekretnina. */
    public LocationData cadastral;
    
    /** Godina procjene. */
    public Integer est_year;
    
    /** Vrijednost od. */
    public BigDecimal value_from;
    
    /** Vrijednost do. */
    public BigDecimal value_to;
    
    /** Starost od (mjeseci). */
    public Integer age_from;
    
    /** Starost do (mjeseci). */
    public Integer age_to;
    
    /** Koeficijent promjene. */
    public BigDecimal koef_rev;
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


/**
 * Klasa koja predstavlja kolateral.
 * @author hrakis
 */
abstract class CollateralData
{
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** Šifra kolaterala. */
    public String col_num;
    
    /** Status kolaterala. */
    public String collateral_status; 
    
    /** Status da li je kolateral knjižen. (0-nije knjižen, 1-knjižen) */
    public String financial_flag;

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
    
    /** Mapirana vrsta kolaterala. */
    public GcmTypeData gcmType;
    
    /** Nominalna vrijednost kolaterala. */
    public BigDecimal nomi_value;
    
    /** ID valute nominalne vrijednosti kolaterala. */
    public BigDecimal nomi_value_cur_id;
    
    /** Oznaka valute nominalne vrijednosti kolaterala. */
    public String nomi_value_cur_code;
    
    /** Datum nominalne vrijednosti kolaterala. */
    public Date nomi_value_date;
    
    /** Nominalna vrijednost kolaterala izražena u eurima. */
    public BigDecimal nomi_value_eur;
    
    /** Procijenjena vrijednost kolaterala. */
    public BigDecimal est_value;
    
    /** Procijenjena vrijednost kolaterala izražena u eurima. */
    public BigDecimal est_value_eur;
    
    /** Datum procjene. */
    public Date est_date;
    
    /** Godina procjene. */
    public int est_year;
    
    /** Proteklo vrijeme od datuma procjene do datuma revalorizacije u mjesecima. */
    public Integer age;
    
    
    /** Revalorizacijski koeficijent. */
    public RevaKoefData coefficient;
    
    /** Nova nominalna vrijednost nakon revalorizacije. */
    public BigDecimal new_nomi_value;
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