/*
 * Created on 2005.11.29
 *
 */
package hr.vestigo.modules.collateral.javatran;

import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;
import java.util.List;
/**
 * @author HRASIA
 *
 */
public class CollateralTypeLookUpTran extends LookUpTransaction {

	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/CollateralTypeLookUpTran.java,v 1.13 2008/03/27 13:38:39 hramkr Exp $";
	
	public CollateralTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	}
//
	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		String tempCollCategory = null;
		//List searchableParams2 = new List();
		String CollCategory = (String) searchableParams.get(0);
//		if (CollCategory!= null) {
//			if (CollCategory.indexOf("*")<0){
//				
//			}
//			  CollCategory = CollCategory + searchableParams.get(0)+"%";
//			
//		}
//		searchableParams.remove(0);
//		searchableParams.add(0, CollCategory); 
		tempCollCategory = (String) ra.getAttribute("CollateralTypeLookUpLDB","CollateralSubCategory");
		
		if(tempCollCategory == null ){
			tempCollCategory = "%";
		}
		
		if(tempCollCategory.compareTo("SVE")== 0){
			//System.out.println ("BLABLA");
			tempCollCategory = "%";
		}  
		//System.out.println ("tempCollCategory " + tempCollCategory);
		
		String SQLQuery = "";
		  
		if (CollCategory.indexOf("%")<0){
		
		SQLQuery =
			"SELECT c.coll_type_id coll_type_id,c.coll_type_code coll_type_code ,c.coll_type_name coll_type_name, c.coll_period_rev coll_period_rev, " +
            " c.coll_mvp_ponder coll_mvp_ponder, c.coll_mvp_ponder_mn coll_mvp_ponder_mn , c.coll_mvp_ponder_mx coll_mvp_ponder_mx ,  " +
			" c.coll_hnb_ponder coll_hnb_ponder, c.coll_hnb_ponder_mn coll_hnb_ponder_mn, c.coll_hnb_ponder_mx coll_hnb_ponder_mx ,   " +
			" c.coll_rzb_ponder coll_rzb_ponder, c.coll_rzb_ponder_mn coll_rzb_ponder_mn, c.coll_rzb_ponder_mx coll_rzb_ponder_mx ,   " +
            " c.coll_category coll_category, scv1.sys_code_value coll_hypo_fidu , scv1.sys_code_desc hypo_fidu_name ,c.coll_anlitika coll_anlitika,  " +  
            " scv2.sys_code_value coll_accounting ,  scv2.sys_code_desc accounting_name, c.coll_date_from coll_date_from, c.coll_date_until coll_date_until " + 
			" FROM collateral_type c  " + 
			" LEFT OUTER JOIN  system_code_value scv1  on    c.coll_hypo_fidu_id = scv1.sys_cod_val_id  " + 
			" LEFT OUTER JOIN system_code_value scv2   on    c.coll_accounting_id = scv2.sys_cod_val_id  " +  
			" WHERE rtrim(c.coll_type_code) = ? AND c.coll_type_name LIKE ? AND c.sub_category LIKE ? " + 
			" AND c.coll_date_until > c.coll_date_from AND  c.bank_sign = 'RB' AND c.coll_status = 'A' AND c.coll_spec_status = 'A' "; 
		} else {
			SQLQuery =
				"SELECT c.coll_type_id coll_type_id,c.coll_type_code coll_type_code ,c.coll_type_name coll_type_name, c.coll_period_rev coll_period_rev, " +
	            " c.coll_mvp_ponder coll_mvp_ponder, c.coll_mvp_ponder_mn coll_mvp_ponder_mn , c.coll_mvp_ponder_mx coll_mvp_ponder_mx ,  " +
				" c.coll_hnb_ponder coll_hnb_ponder, c.coll_hnb_ponder_mn coll_hnb_ponder_mn, c.coll_hnb_ponder_mx coll_hnb_ponder_mx ,   " +
				" c.coll_rzb_ponder coll_rzb_ponder, c.coll_rzb_ponder_mn coll_rzb_ponder_mn, c.coll_rzb_ponder_mx coll_rzb_ponder_mx ,   " +
	            " c.coll_category coll_category, scv1.sys_code_value coll_hypo_fidu , scv1.sys_code_desc hypo_fidu_name ,c.coll_anlitika coll_anlitika,  " +  
	            " scv2.sys_code_value coll_accounting ,  scv2.sys_code_desc accounting_name, c.coll_date_from coll_date_from, c.coll_date_until coll_date_until " + 
				" FROM collateral_type c  " + 
				" LEFT OUTER JOIN  system_code_value scv1  on    c.coll_hypo_fidu_id = scv1.sys_cod_val_id  " + 
				" LEFT OUTER JOIN system_code_value scv2   on    c.coll_accounting_id = scv2.sys_cod_val_id  " +  
				" WHERE c.coll_type_code LIKE ? AND c.coll_type_name LIKE ? AND c.sub_category LIKE ? " + 
				" AND c.coll_date_until > c.coll_date_from AND  c.bank_sign = 'RB' AND c.coll_status = 'A' AND c.coll_spec_status = 'A'"; 			
		}
		 
		//searchableParams2.add(0,CollCategory);
		//searchableParams2.add(1, searchableParams.get(1));
		
		searchableParams.add(2, tempCollCategory);
		//System.out.println(searchableParams.get(0));
		//System.out.println(searchableParams.get(1));
		//System.out.println(searchableParams.get(2));
		tempCollCategory = null;	
System.out.println ("searchableParams " + searchableParams);
//System.out.println ("searchableParams2 " + searchableParams2);

		
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
		
	}
	
	
	
}
