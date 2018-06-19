//created 2008.11.07
package hr.vestigo.modules.collateral.batch.bo34;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author hraamh
 */
public class CollHeadNewData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo34/CollHeadNewData.java,v 1.3 2009/03/04 14:24:24 hramkr Exp $";
    
    BigDecimal one_mar_amo=null;
    BigDecimal market_amount=null;
    Date price_date=null;
    BigDecimal weigh_value=null;
    BigDecimal avail_value=null;
    
    public void round(){
        one_mar_amo =DecimalUtils.scale(one_mar_amo, 2);
        weigh_value=DecimalUtils.scale(weigh_value, 2);
        avail_value=DecimalUtils.scale(avail_value, 2);
        market_amount=DecimalUtils.scale(market_amount, 2);
    }
    
    public String toString(){
        String result="";
        result+="\n market_amount= "+market_amount;
        result+="\n price_date= "+price_date;
        result+="\n weigh_value= "+weigh_value;
        result+="\n avail_value= "+avail_value;
        return result;
    }
}

