//created 2008.11.06
package hr.vestigo.modules.collateral.batch.bo31;

import hr.vestigo.modules.rba.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;


/**
 * NE KORISTI SE VIŠE
 * @author hrarmv
 */
public class InputParams {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo31/InputParams.java,v 1.4 2010/08/20 14:23:26 hrakis Exp $";
    
    /**
     * Datum za koji se radi izvjestaj
     */
    public Date reportDate;
    /**
     * Vrsta klijenta
     */
    public String custType;
    /**
     * Id kategorije kolaterala
     */
    public BigDecimal colCatId;
        
    public String bankSign;
    
    public InputParams(String [] parametri) throws Exception{
        try { 
            this.bankSign=parametri[0].trim();
            this.reportDate=Date.valueOf(parametri[1].trim());
            this.custType=(String)parametri[2].trim();
            this.colCatId= new BigDecimal (parametri[3].trim());
        } catch (Exception e) {
            throw new Exception("Greska pri kreiranju ulaznih parametara u class-i InParametars." , e);
        }        
    }
    
      
    public String toString(){
        String rez="";
        rez+="bankSign= "+this.bankSign+", ";
        rez+="reportDate= "+this.reportDate+", ";                                 
        rez+="custType= "+this.custType+", ";                       
        rez+="colCatId= "+this.colCatId+", ";                          
                            
        return rez;
    }
    

    
}

