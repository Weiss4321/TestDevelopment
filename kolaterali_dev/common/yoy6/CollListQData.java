/*
 * Created on 2006.06.12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy6;
import java.math.BigDecimal;

import java.sql.Timestamp;
/**
 * @author hramkr 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollListQData {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy6/CollListQData.java,v 1.3 2014/06/17 12:23:38 hrazst Exp $";
	
	public CollListQData () {
	    
	}
	
	public BigDecimal col_lis_q_id=null;	
	public BigDecimal col_lis_typ_id=null;
	public BigDecimal col_hea_id=null;	
	public String status=null;	 
	public Timestamp income_time=null;
	public BigDecimal use_id=null;
	public Timestamp release_time=null;
	public String bank_sign=null; 
	public String cmnt=null;
	public String action_type=null;
	public BigDecimal org_uni_id=null;
	
	public String source_list = null;
	public String status_source_list=null;	  
	public String target_list = null;	
	public String status_target_list=null;	 
	
	public BigDecimal target_list_use_id=null;	
	public BigDecimal target_list_org_uni_id=null;	
	public String coll_deact_reason=null;
	
	public String toString(){
	    StringBuffer buffy = new StringBuffer();
	    
	    buffy.append("\ncol_lis_q_id=["+col_lis_q_id+"],");
	    buffy.append("\ncol_lis_typ_id=["+col_lis_typ_id+"],");
	    buffy.append("\ncol_hea_id=["+col_hea_id+"],");
	    buffy.append("\nstatus=["+status+"],");
	    buffy.append("\nincome_time=["+income_time+"],");
	    buffy.append("\nuse_id=["+use_id+"],");
	    buffy.append("\nrelease_time=["+release_time+"],");
	    buffy.append("\nbank_sign=["+bank_sign+"],");
	    buffy.append("\ncmnt=["+cmnt+"],");
	    buffy.append("\naction_type=["+action_type+"],");
	    buffy.append("\norg_uni_id=["+org_uni_id+"],");
	    buffy.append("\nsource_list=["+source_list+"],");
	    buffy.append("\nstatus_source_list=["+status_source_list+"],");
	    buffy.append("\ntarget_list=["+target_list+"],");
	    buffy.append("\nstatus_target_list=["+status_target_list+"],");
	    buffy.append("\ntarget_list_use_id=["+target_list_use_id+"],");
	    buffy.append("\ntarget_list_org_uni_id=["+target_list_org_uni_id+"],");
	    buffy.append("\ncoll_deact_reason=["+coll_deact_reason+"]");
	    
	    return buffy.toString();
	}
}





