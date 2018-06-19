//created 2008.11.07
package hr.vestigo.modules.collateral.batch.bo34;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author hraamh
 */
public class VRPNewData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo34/VRPNewData.java,v 1.6 2012/04/04 12:04:47 hramlo Exp $";
    
    BigDecimal one_nom_amo=null;
    BigDecimal nominal_amount=null;
    BigDecimal one_mar_amo=null;
    BigDecimal one_nom_amo_kn=null;
    BigDecimal nominal_amount_kn =null;
    BigDecimal one_mar_amo_kn =null;
    BigDecimal one_mar_amo_per =null;
    BigDecimal market_amount =null;
    BigDecimal market_amount_kn =null;
    Date price_date =null;
    BigDecimal margin_omjer=null;
    
    public void round(){
        one_nom_amo=DecimalUtils.scale(one_nom_amo, 4);
        one_mar_amo=DecimalUtils.scale(one_mar_amo, 4);
        one_nom_amo_kn=DecimalUtils.scale(one_nom_amo_kn, 4);
        nominal_amount_kn=DecimalUtils.scale(nominal_amount_kn, 2);
        one_mar_amo_kn=DecimalUtils.scale(one_mar_amo_kn, 4);
        one_mar_amo_per=DecimalUtils.scale(one_mar_amo_per, 4);
        market_amount=DecimalUtils.scale(market_amount, 2);
        market_amount_kn=DecimalUtils.scale(market_amount_kn, 2);
        nominal_amount=DecimalUtils.scale(nominal_amount, 2);
        margin_omjer=DecimalUtils.scale(margin_omjer, 4);
    }
    
    public String toString(){
        String result="";
        result+="\n one_nom_amo= "+one_nom_amo;
        result+="\n nominal_amount= "+nominal_amount;
        result+="\n one_mar_amo= "+one_mar_amo;
        result+="\n one_nom_amo_kn= "+one_nom_amo_kn;
        result+="\n nominal_amount_kn= "+nominal_amount_kn;
        result+="\n one_mar_amo_kn= "+one_mar_amo_kn;
        result+="\n one_mar_amo_per= "+one_mar_amo_per;
        result+="\n market_amount= "+market_amount;
        result+="\n market_amount_kn= "+market_amount_kn;
        result+="\n price_date= "+price_date;
        
        return result;
    }

}

