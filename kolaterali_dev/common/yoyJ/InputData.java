//created 2017.02.13
package hr.vestigo.modules.collateral.common.yoyJ;

import java.math.BigDecimal;

import hr.vestigo.modules.coreapp.share.DataObject;

/**
 *
 * @author hrazst
 */
public class InputData extends DataObject<Object> {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyJ/InputData.java,v 1.1 2017/02/13 09:20:52 hrazst Exp $";
    
    public String coll_owner_register_no=null;
    public String aps_number=null;
    public String coll_category=null;
    public String coll_type=null;
    public String register_no=null;
    public BigDecimal amount=null;
    public String cur_code_num=null;
    public String cusacc_no=null;
    public String deposit_account_no=null;  
   
}

