//created 2014.11.11
package hr.vestigo.modules.collateral.common.yoy6;

import java.math.BigDecimal;
import java.sql.Date;
/**
 *
 * @author hrazst
 */
public class CalculationData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy6/CalculationData.java,v 1.2 2014/12/29 13:58:26 hrazst Exp $";
    
    public BigDecimal wcov_amount = null;
    public Date wcov_date = null;
    public String wcov_cur_char = null;
    public BigDecimal wca_amount = null;
    
    public BigDecimal MVP_ponder = null;
    public BigDecimal real_ponder = null;
    public BigDecimal NGV_insurance_ration = null;
    public BigDecimal sum_to_first_RBA_hip = null;
    
    public String toString(){
        StringBuffer buffy=new StringBuffer();          
        buffy.append("\n*********************CollHeadUpdateData*********************");
        buffy.append("\nwca_amount=["+this.wca_amount+"]");
        buffy.append("\nwca_date=["+this.wcov_date+"]");
        buffy.append("\nwca_cur_char=["+this.wcov_cur_char+"]");
        buffy.append("\nMVP_ponder=["+this.MVP_ponder+"]");
        buffy.append("\nreal_ponder=["+this.real_ponder+"]");
        buffy.append("\nNGV_insurance_ration=["+this.NGV_insurance_ration+"]");
        buffy.append("\nsum_to_first_RBA_hip=["+this.sum_to_first_RBA_hip+"]");
        buffy.append("\n**********************************************************");
        return buffy.toString();
    }
}

