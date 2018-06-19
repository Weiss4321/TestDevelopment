//created 2017.03.02
package hr.vestigo.modules.collateral.common.yoyJ;

import java.math.BigDecimal;
import java.sql.Date;

import hr.vestigo.modules.coreapp.share.DataObject;

/**
 *
 * @author hrazst
 */
public class CashDepData extends DataObject<Object>{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyJ/CashDepData.java,v 1.1 2017/03/02 12:45:58 hrazst Exp $";
    
    public BigDecimal cus_acc_id=null;    
    public BigDecimal ban_pro_typ_id=null;
    public BigDecimal cur_id=null;        
    public String cde_cur=null;           
    public BigDecimal cde_amount=null;    
    public String prolong_flag=null;      
    public String cde_account=null;       
    public Date cde_dep_from=null;        
    public Date cde_dep_unti=null;        
    public String cde_owner=null;         
    public String acc_num=null;           
    public String loan_owner=null;        
    public String cus_acc_status=null;  
}

