package hr.vestigo.modules.collateral.batch.bo76;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;

/**
 *
 * @author hrakis
 */
public class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo76/CollateralData.java,v 1.3 2014/06/11 08:45:40 hrakis Exp $";
    
    private final BigDecimal zero = new BigDecimal("0.00");
    private final DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
    
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** Šifra kolaterala. */
    public String col_num;
    
    /** ID kategorije kolaterala. */
    public BigDecimal col_cat_id;
    
    /** ID vrste kolaterala. */
    public BigDecimal col_typ_id;
    
    /** ID podvrste kolaterala. */
    public BigDecimal col_sub_id;
    
    /** Tržišna vrijednost kolaterala. */
    public BigDecimal real_est_nomi_valu;
    
    /** Valuta tržišne vrijednosti kolaterala. */
    public BigDecimal real_est_nm_cur_id;
    
    /** Nominal Collateral Value - tržišna vrijednost kolaterala u EUR. */
    public BigDecimal ncv;
    
    /** Indikator da li je kolateral osiguran. */
    public String inspol_ind;
    
    /** Ponder kolaterala izražen u postotku. */
    public BigDecimal ponder;
    
    /** Realan ponder kolaterala korišten u izraèunu pokrivenosti. */
    public BigDecimal real_ponder;
    
    /** Suma vrijednosti hipoteka do prve RBA hipoteke u EUR. */
    public BigDecimal mortgages_sum = zero;
    
    /** Weighted Collateral Object Value = (NCV * ponder) - suma vrijednosti hipoteka do prve RBA hipoteke. */
    public BigDecimal wcov;
    
    /** Weighted Collateral Value - alocirana vrijednost kolaterala u EUR. */
    public BigDecimal wcv;
    
    /** Najraniji datum do kada vrijedi neka hipoteka kolaterala. */
    public Date earliest_exp_date;
    
    /** Najdalji datum do kada vrijedi neka hipoteka kolaterala. */
    public Date latest_exp_date;
    
    /** ID plasmana s najdaljim datumom dospijeæa kojeg osigurava kolateral. */
    public BigDecimal cus_acc_id;
    
    /** Datum dospijeæa plasmana. */
    public Date placement_due_date;
    
    /** Suma izloženosti svih plasmana koji su osigurani kolateralom u EUR. */
    public BigDecimal placement_exposure_sum = zero;
    
    /** LTV - omjer sume izloženosti svih plasmana kolaterala i NCV kolaterala, izražen u postotku. */ 
    public BigDecimal ltv;
    
    /** Datum do kada vrijedi polica osiguranja. Ako je više polica, uzima se najkraæi datum. */
    public Date insurance_exp_date;
    
    /** Suma osiguranih svota svih aktivnih polica osiguranja u EUR. */
    public BigDecimal insurance_sum = zero;
    
    /** Datum do kada vrijedi kolateral. */
    public Date coll_exp_date;
    
    
    public String gct;
    public String gctc;
    
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");
        buffer.append(col_num).append("\n");
        buffer.append("   NCV = ").append(bigDecimalToString(ncv)).append("€\n");
        buffer.append("   ponder = ").append(ponder).append("%\n");
        buffer.append("   real ponder = ").append(real_ponder).append("%\n");
        buffer.append("   hipoteke = ").append(bigDecimalToString(mortgages_sum)).append("€\n");
        buffer.append("   earliest expiry date = ").append(earliest_exp_date).append("\n");
        buffer.append("   latest expiry date = ").append(latest_exp_date).append("\n");
        buffer.append("   WCOV = ").append(bigDecimalToString(wcov)).append("€\n");
        buffer.append("   WCV = ").append(bigDecimalToString(wcv)).append("€\n");
        buffer.append("   insurance expiry date = ").append(insurance_exp_date).append("\n");
        buffer.append("   insurance amount = ").append(bigDecimalToString(insurance_sum)).append("€\n");
        buffer.append("   placement due date = ").append(placement_due_date).append("\n");
        buffer.append("   placement sum = ").append(bigDecimalToString(placement_exposure_sum)).append("€\n");
        buffer.append("   LTV = ").append(bigDecimalToString(ltv)).append("%\n");
        buffer.append("   collateral expiry date = ").append(coll_exp_date).append("\n");
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