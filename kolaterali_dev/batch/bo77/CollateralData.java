package hr.vestigo.modules.collateral.batch.bo77;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;


/**
 * Klasa koja sadrži podatke o kolateralu.
 */
public class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo77/CollateralData.java,v 1.5 2014/06/11 08:51:29 hrakis Exp $";
    
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");


    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** Šifra kolaterala. */
    public String col_num;
    
    /** ID kategorije kolaterala (COLL_CATEGORY). */
    public BigDecimal col_cat_id;
    
    /** Naziv kategorije kolaterala. */
    public String col_cat_name;
    
    /** ID vrste kolaterala (COLLATERAL_TYPE). */
    public BigDecimal col_typ_id;
    
    /** Naziv vrste kolaterala. */
    public String col_typ_name;
    
    /** ID podvrste kolaterala (REAL_ESTATE_TYPE, VECHICLE_TYPE, MOVABLE_TYPE... ). */
    public BigDecimal col_sub_id;
    
    /** Naziv podvrste kolaterala. */
    public String col_sub_name;
    
    /** NCV - Nominal Collateral Value */
    public BigDecimal ncv;
    
    /** WCOV - Weighted Collateral Object Value */
    public BigDecimal wcov;
    
    /** WCV - Weighted Collateral Value */
    public BigDecimal wcv;
    
    /** Ponder kolaterala izražen u postotku. */
    public BigDecimal ponder;
    
    /** Realan ponder kolaterala. */
    public BigDecimal real_ponder;
    
    /** 100% - ponder kolaterala */
    public BigDecimal actual_discont;
    
    /** ID grada u kojem se nalazi nekretnina. */
    public BigDecimal city_id;
    
    /** ID županije u kojem se nalazi nekretnina. */
    public BigDecimal district_id;
    
    /** ID vlasnika plasmana. */
    public BigDecimal placement_cus_id;
    
    /** Interni MB vlasnika plasmana. */
    public String placement_owner_register_no;
    
    /** Naziv vlasnika plasmana. */
    public String placement_owner_name;
    
    /** Ocjena vlasnika plasmana. */
    public String placement_owner_rating;
    
    /** Datum dospjeæa plasmana. */
    public Date placement_due_date;
    
    /** Ime referenta vlasnika plasmana. */
    public String relationship_manag_name;
        
    /** Oznaka tipa kolaterala prema Beèu. */
    public String colateral_type; 
    
    /** Datum zadnje revalorizacije. */
    public Date last_evaluation; 
    
    /** Collateral officer koji je zadnji napravio revalorizaciju. */
    public String latest_evaluator;
    
    /** B2IRB prihvatljivost kolaterala (Y/N). */
    public String b2_irb_eligibility;
    
    /** Datum isteka revalorizacije. */
    public Date due_revaluation_date; 
    
    /** Datum do kada vrijedi kolateral. */
    public Date coll_exp_date;
    
    /** ISIN */
    public String isin;
    
    /** Long rating kolaterala (zapisa,obveznice,garancije,...). */
    public String debtSecurityLongRating;
    
    /** Short rating kolaterala (zapisa,obveznice,garancije,...). */
    public String debtSecurityShortRating;
    
    /** Naziv izdavatelja kolaterala (zapisa,obveznice,garancije,...). */
    public String issuerName;
    
    /** Interni MB izdavatelja kolaterala (zapisa,obveznice,garancije,...). */
    public String issuerRegisterNo;
    
    /** Rating izdavatelja kolaterala (zapisa,obveznice,garancije,...). */
    public String issuerRating;
    
    /** Engleski naziv države izdavatelja kolaterala (zapisa,obveznice,garancije,...). */
    public String issuerCouName;
    
    /** ISO država izdavatelja kolaterala (zapisa,obveznice,garancije,...). */
    public String issuerCouISOCode;
    
    /** Partija plasmana. */
    public String contract_acc_no;
        
    /** Najraniji datum do kada vrijedi neka hipoteka kolaterala. */
    public Date earliest_exp_date;
    
    /** Najdalji datum do kada vrijedi neka hipoteka kolaterala. */
    public Date latest_exp_date;
    
    /** Omjer sume izloženosti svih plasmana koje osigurava kolateral i NCV kolaterala, izražen u postotku. */
    public BigDecimal ltv;
    
    /** Suma osiguranih svota svih aktivnih polica osiguranja kolaterala u EUR. */
    public BigDecimal insurance_sum;
    
    /** Datum do kada vrijedi polica osiguranja. Ako više polica osiguranja osigurava kolateral - najkraæi datum. */
    public Date insurance_exp_date;
    
    /** Podataka da li je polica plaæena (Y/N). */
    public String premium_paid;
    
    /** Podaci o kolateralu od prije 7 dana. */
    public CollateralData week_before_collateral;
    
    
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        String separator = "; ";
        buffer.append("col_hea_id = ").append(col_hea_id).append(separator);
        buffer.append("col_num = ").append(col_num).append(separator);
        buffer.append("col_cat_id = ").append(col_cat_id).append(separator);
        buffer.append("col_typ_id = ").append(col_typ_id).append(separator);
        buffer.append("col_sub_id = ").append(col_typ_id).append(separator);
        buffer.append("ncv = ").append(bigDecimalToString(ncv)).append(separator);
        buffer.append("wcov = ").append(bigDecimalToString(wcov)).append(separator);
        buffer.append("wcv = ").append(bigDecimalToString(wcv)).append(separator);
        buffer.append("ponder = ").append(bigDecimalToString(ponder)).append(separator);
        buffer.append("real ponder = ").append(bigDecimalToString(real_ponder)).append(separator);
        buffer.append("actual_discont = ").append(bigDecimalToString(actual_discont)).append(separator);
        buffer.append("city_id = ").append(bigDecimalToString(city_id)).append(separator);
        buffer.append("district_id = ").append(bigDecimalToString(district_id)).append(separator);  
        buffer.append("pl_own_register_no = ").append(placement_owner_register_no).append(separator);
        buffer.append("pl_own_name = ").append(placement_owner_name).append(separator);
        buffer.append("pl_own_rating = ").append(placement_owner_rating).append(separator);
        buffer.append("pl_due_date = ").append(placement_due_date).append(separator);
        buffer.append("relation_manag_name = ").append(relationship_manag_name).append(separator);
        buffer.append("coll_type = ").append(colateral_type).append(separator);
        buffer.append("last_evaluation = ").append(last_evaluation).append(separator);
        buffer.append("latest_evaluator = ").append(latest_evaluator).append(separator);
        buffer.append("b2_irb_elig = ").append(b2_irb_eligibility).append(separator);
        buffer.append("due_reval_date = ").append(due_revaluation_date).append(separator);
        buffer.append("coll_exp_date = ").append(coll_exp_date).append(separator);
        buffer.append("contract_acc_no = ").append(contract_acc_no).append(separator);
        buffer.append("earliest_exp_date = ").append(earliest_exp_date).append(separator);
        buffer.append("latest_exp_date = ").append(latest_exp_date).append(separator);
        buffer.append("ltv = ").append(bigDecimalToString(ltv)).append(separator);
        buffer.append("insurance_sum = ").append(bigDecimalToString(insurance_sum)).append(separator);
        buffer.append("insurance_exp_date = ").append(insurance_exp_date).append(separator);
        buffer.append("premium_paid = ").append(premium_paid).append(separator);
        if (week_before_collateral != null) buffer.append("week_before_collateral.wcov = ").append(bigDecimalToString(week_before_collateral.wcov)).append(separator);
        
        return buffer.toString();
    }
    
    
    /**
     * Metoda koja vraæa formatirani zapis zadanog broja.
     * @param number broj
     * @return formatirani zapis broja
     */
    private String bigDecimalToString(BigDecimal number)
    {
        if (number == null) return "";
        else return decimalFormat.format(number);
    }

}