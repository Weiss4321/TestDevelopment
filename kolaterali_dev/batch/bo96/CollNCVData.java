//created 2015.02.11
package hr.vestigo.modules.collateral.batch.bo96;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Hraziv
 */
public class CollNCVData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo96/CollNCVData.java,v 1.2 2015/03/13 13:11:27 hrazst Exp $";
    
    public BigDecimal cus_id;
    public Date maxDate;
    public BigDecimal COLL_AMOUNT;
    public BigDecimal EXP_COLL_AMOUNT;
    public BigDecimal WCOV;
    
    /** 
     * Konstruktor klase.
     */
    public CollNCVData()
    {
        
    }
}



