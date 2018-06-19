//created 2014.11.07
package hr.vestigo.modules.collateral.common.yoy6;

import java.math.BigDecimal;

/**
 *
 * @author hrazst
 */
public class GCTCData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy6/GCTCData.java,v 1.1 2014/11/12 14:13:47 hrazst Exp $";
    
    /**GCTC kolaterala*/
    public BigDecimal gctc_id = null;
    public String gctc_code = null;
    public String gctc_desc = null;
    /**Endorsement Type - GCTC*/
    public BigDecimal endorsement_type_id = null;
    public String endorsement_type_code = null;
    public String endorsement_type_desc = null;
    /**Object Type - GCTC*/
    public BigDecimal object_type_id = null;
    public String object_type_code = null;
    public String object_type_desc = null;
    /**Property Type - GCTC*/
    public BigDecimal property_type_id = null;
    public String property_type_code = null;
    public String property_type_desc = null;    
}

