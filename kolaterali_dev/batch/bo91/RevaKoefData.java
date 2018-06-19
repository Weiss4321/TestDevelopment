package hr.vestigo.modules.collateral.batch.bo91;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * Objekt koji definira koeficijent promjene i uvjete koje kolateral mora ispuniti da bi koeficijent bio primijenjen.
 * @author hrakis
 */
public class RevaKoefData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo91/RevaKoefData.java,v 1.1 2014/08/01 10:36:05 hrakis Exp $";

    
    /** Vrste kolaterala prema mapiranju za revalorizaciju. */
    public TypeData collateralType;
    
    /** Županija nekretnine. */
    public LocationData county;
    
    /** Grad nekretnine. */
    public LocationData city;
    
    /** Katastarske opæina nekretnine. */
    public LocationData cadastralMunicipality;
    
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
    
    
    public String toString()
    {
        return String.format("{ collateralType = %1$s, county = %2$s, city = %3$s, cadastralMunicipality = %4$s, est_year = %5$s, value_from = %6$s, value_to = %7$s, age_from = %8$s, age_to = %9$s, koef_rev = %10$s }", 
                collateralType, county, city, cadastralMunicipality, est_year, value_from, value_to, age_from, age_to, koef_rev); 
    }
}


/**
 * Objekt koji predstavlja lokaciju kolaterala.
 * Može biti primijenjen za katastarsku opæinu, mjesto, županiju.
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
 * Objekt koji predstavlja tip kolaterala.
 */
class TypeData
{
    public TypeData(BigDecimal id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    /** ID tipa. */
    public BigDecimal id;
    
    /** Naziv tipa. */
    public String name;
    
    
    public String toString()
    {
        return String.format("{ id = %1$s, name = '%2$s' }", id, name);
    }
}