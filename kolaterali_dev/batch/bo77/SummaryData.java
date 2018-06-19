package hr.vestigo.modules.collateral.batch.bo77;

import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashSet;


/**
 * Klasa koja sadrži sumarne podatke za neki tip kolaterala.
 */
public class SummaryData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo77/SummaryData.java,v 1.2 2013/06/03 14:26:10 hradnp Exp $";
    
    private final BigDecimal zero = new BigDecimal("0.00");
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");
    
    public SummaryData(GcmTypeData type)
    {
        this.type = type;
    }
    
    /** Tip kolaterala. */
    public GcmTypeData type;
    
    /** Suma svih NCV vrijednosti za tip kolaterala. */
    public BigDecimal ncv_sum = zero;
    
    /** Suma svih WCOV vrijednosti za tip kolaterala. */
    public BigDecimal wcov_sum = zero;
    
    /** Suma svih WCV vrijednosti za tip kolaterala. */
    public BigDecimal wcv_sum = zero;
    
    /** Suma svih postotaka za koje se umanjuje vrijednost kolaterala. */
    public BigDecimal actual_discount_sum = zero;
    
    /** Minimalni postotak umanjenja po odreðenom tipu kolaterala. */
    public BigDecimal min_discount = zero;
    
    /** Maksimalni postotak umanjenja po odreðenom tipu kolaterala. */
    public BigDecimal max_discount = zero;
    
    /** Broj kolaterala. */
    public int count = 0;
    
    /** CRE/RRE indikator (samo za nekretnine). */
    public String creReeIndic; 
    
    /** ID-evi plasmana koji su vezani za kolaterale odreðenog tipa. */
    public HashSet<BigDecimal> contractIds = new HashSet<BigDecimal>();
    
    /** Broj plasmana vezanih za kolaterale odreðenog tipa. */
    public int countContracts = 0;
    
    /** ID-evi komitenata èiji su plasmani vezani za kolaterale odreðenog tipa. */
    public HashSet<BigDecimal> customerIDs = new HashSet<BigDecimal>();
    
    /** Broj komitenata èiji su plasmani vezani za kolaterale odreðenog tipa. */
    public int countCustomers = 0;
    
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        String separator = "; ";
        buffer.append("ncv_sum = ").append(bigDecimalToString(ncv_sum)).append(separator);
        buffer.append("wcov_sum = ").append(bigDecimalToString(wcov_sum)).append(separator);
        buffer.append("wcv_sum = ").append(bigDecimalToString(wcv_sum)).append(separator);
        buffer.append("actual_discount_sum = ").append(bigDecimalToString(actual_discount_sum)).append(separator);
        buffer.append("min_discount = ").append(bigDecimalToString(min_discount)).append(separator);
        buffer.append("max_discount = ").append(bigDecimalToString(max_discount)).append(separator);
        buffer.append("count = ").append(count).append(separator);
        buffer.append("creReeIndic = ").append(creReeIndic).append(separator);
        buffer.append("countContracts = ").append(countContracts).append(separator);
        buffer.append("countCustomers = ").append(countCustomers).append(separator);
        
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
