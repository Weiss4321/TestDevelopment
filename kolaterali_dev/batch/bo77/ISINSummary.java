//created 2013.05.17
package hr.vestigo.modules.collateral.batch.bo77;

import java.math.BigDecimal;
import java.util.HashSet;

/**
 *
 * @author HRADNP
 */
public class ISINSummary {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo77/ISINSummary.java,v 1.1 2013/07/16 06:59:26 hradnp Exp $";
    
    private final BigDecimal zero = new BigDecimal("0.00");
    
    public ISINSummary(CollateralData data)
    {
        this.isin = data.isin;
        this.issuerRegisterNo = data.issuerRegisterNo;
        this.issuerName = data.issuerName;
        this.issuerRating = data.issuerRating;
        this.issuerCouName = data.issuerCouName;
        this.issuerCouISOCode = data.issuerCouISOCode;
    }
    
    /** ISIN */
    public String isin;
    
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
    
    /** Kategorija kolaterala po RBA podjeli. */
    public String rbaCategory; 
    
    /** Tip kolaterala po RBA podjeli. */
    public String rbaType;
    
    /** Tip kolaterala prema Beèkoj podjeli. */
    public String collateralType;
    
    /** Object/property type according to GCTC */
    public String gctc;
        
    /** Naziv izdanja. */
    public String issueName = "";
    
    /** Rating izdanja. */
    public String issueRating = "";
    
    /** Suma svih NCV vrijednosti izdanih garancija izdavatelja. */
    public BigDecimal ncv_sum = zero;

    /** Suma svih WCV vrijednosti izdanih garancija izdavatelja. */
    public BigDecimal wcv_sum = zero;

    /** ID-evi komitenata èiji su plasmani pokriveni garancijama izdavatelja. */
    public HashSet<BigDecimal> customerIds = new HashSet<BigDecimal>();

    /** Broj komitenata èiji su plasmani pokriveni vrijednosnim papirima jednog ISIN-a*/
    public int countCustomers = 0;
    
    /** Broj kolaterala jednog ISIN-a */
    public int countCollaterals = 0; 
}

