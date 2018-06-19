//created 2013.04.30
package hr.vestigo.modules.collateral.batch.bo77;

import hr.vestigo.modules.collateral.common.yoyM.GcmTypeData;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * Podaci o izdavatelju (garancije)
 * @author HRADNP
 */
public class IssuerSummary 
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo77/IssuerSummary.java,v 1.1 2013/07/16 06:59:26 hradnp Exp $";
 
    private final BigDecimal zero = new BigDecimal("0.00");
        
    public IssuerSummary(CollateralData data)
    {
        this.issuerRegisterNo = data.issuerRegisterNo;
        this.issuerName = data.issuerName;
        this.issuerRating = data.issuerRating;
        this.issuerCouName = data.issuerCouName;
        this.issuerCouISOCode = data.issuerCouISOCode;
    }
        
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
    
    /**  */
    public String gcc; 
    
    /** */
    public String gct;
        
    /** Suma svih NCV vrijednosti izdanih garancija izdavatelja. */
    public BigDecimal ncv_sum = zero;

    /** Suma svih WCV vrijednosti izdanih garancija izdavatelja. */
    public BigDecimal wcv_sum = zero;

    /** ID-evi komitenata èiji su plasmani pokriveni garancijama izdavatelja. */
    public HashSet<BigDecimal> customerIds = new HashSet<BigDecimal>();
    
    /** Broj komitenata èiji su plasmani pokriveni garanvijama izdavatelja. */
    public int countCustomers = 0;
    
}

