//created 2009.09.18
package hr.vestigo.modules.collateral.common.yoyE;
import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author hramkr
 */
public class YOYE0Data {
  
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyE/YOYE0Data.java,v 1.5 2017/12/21 14:04:06 hrazst Exp $";

    /**
    * Constructor for class YOYE0Data
    **/
    public YOYE0Data () {
    }
    /**
     * Collateral id
     */ 
    public BigDecimal colHeaId=null;

    /**
     * MVP dfl ponder kolaterala
     */ 
    public BigDecimal ponder=null;
      
    /** 
     * Nominalna vrijednost collaterala kuju je unio collateral officer
     */ 
    public BigDecimal nominalAmount=null;
    
    /** 
     * Ponderirana vrijednost collaterala 
     */ 
    public BigDecimal ponderAmount=null;    

    /**
     * Id valute vrijednosti collaterala
     */ 
    public BigDecimal colCurId=null;
    
    /** 
     * Raspolozivi neponderirani iznos kolaterala = neponderirana vrijednost - iskoristenost neponderirano<br/>
     * Raspoloživa vrijednost (RBA nepond)
     */ 
    public BigDecimal restAmount=null;
    
    /** 
     * Datum izlozenosti 
     */ 
    public Date exposureDate=null;
    
    /** 
     * Ukupna izlozenost svih plasmana vezanih na kolateral u valuti kolaterala
     */
    public BigDecimal exposureAmount=null;  
    
    /** 
     * ID kategorije kolaterala
     */
    public BigDecimal colCatId=null;  
    
    /** 
     * ID vrste kolaterala
     */
    public BigDecimal colTypId=null;
    
    /** 
     * ID podvrste kolaterala
     */
    public BigDecimal colSubTypId=null;
    
    /**
     * MVP dfl ponder kolaterala
     */ 
    public BigDecimal dflPonder=null;
    
    /**
     * MVP min ponder kolaterala
     */ 
    public BigDecimal minPonder=null;
    
    /**
     * MVP max ponder kolaterala
     */ 
    public BigDecimal maxPonder=null;
       
    /**
     * indikator dodatnog uvjeta 
     */ 
    public String addRequest=null;
    
    /** 
     * Raspolozivi ponderirani iznos kolaterala = ponderirana vrijednost - iskoristeno ponderirano<br/>
     * Raspoloživa vrijednost (ND pond)
     */ 
    public BigDecimal restPonAmount=null;  
    
    /** 
     * Iskoristeno neponderirano 
     */ 
    public BigDecimal covAmount=null;  
    
    /** 
     * Iskoristeno ponderirano 
     */ 
    public BigDecimal covPonAmount=null;  

    /** 
     * Datum ponderirane ND vrijednosti
     */ 
    public Date NDCoverDate=null;    
    
    /** 
     * Datum RBA neponderirane vrijednosti
     */ 
    public Date coverDate=null;    
    
    /** 
     * Iznos tudjih hipoteka u valuti kolaterala 
     */ 
    public BigDecimal otherMortgAmount=null;      
    
    /** 
     * Iznos ponderiranih izracuna pokrivenosti 
     */ 
    public BigDecimal exposurePonAmount=null; 
    
    /** 
     * Iznos neponderiranih izracuna pokrivenosti 
     */ 
    public BigDecimal exposureNoPonAmount=null; 
    
    /** 
     * Iznos RBA hipoteka u valuti kolaterala 
     */ 
    public BigDecimal rbaMortgAmount=null;     
}
