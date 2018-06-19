/* Created on 2006.03.08 */
package hr.vestigo.modules.collateral.javatran;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.math.BigDecimal;
import java.util.List;


/** 
 * @author hramkr
 */
public class InsPolTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/InsPolTypeLookUpTran.java,v 1.7 2013/05/22 06:52:31 hrakis Exp $";
	public InsPolTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 
 
	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		this.debug("U look up-u sam prije upita");
		
		String SQLQuery = ""; 
		String group1 = null;
		String group2 = null;
		String group3 = null;
		String group4 = null;
		BigDecimal polCompanyId = null;
		group1 = (String) ra.getAttribute("group1");
		group2 = (String) ra.getAttribute("group2");
		group3 = (String) ra.getAttribute("group3");
		group4 = (String) ra.getAttribute("group4");
		
		
		polCompanyId = (BigDecimal)ra.getAttribute("polCompanyId");

		  
		
		if ( group1 != null){
			if(group1.trim().compareTo("")== 0){
				group1 = null;
			}else{
				group1 = group1.trim();
			}
		}
		if ( group2 != null){
			if(group2.trim().compareTo("")== 0){
				group2 = null;
			}else{
				group2 = group2.trim();
			}
		}
		if ( group3 != null){
			if(group3.trim().compareTo("")== 0){
				group3 = null;
			}else{
				group3 = group3.trim();
			}
		}
		if ( group4 != null){
			if(group4.trim().compareTo("")== 0){
				group4 = null;
			}else{
				group4 = group4.trim();
			}
		}
		
		if(polCompanyId == null){
		
		
			if((group3 == null) && (group4 == null)){
				SQLQuery = 
				"SELECT int_pol_type_id, int_pol_type_code, int_pol_type_name, int_pol_sh_sign FROM insu_policy_type WHERE "
				+ " rtrim(int_pol_type_code) LIKE ? AND int_pol_type_name LIKE ? AND int_pol_sh_sign LIKE ? "
				+ " AND int_pol_date_until > int_pol_date_from AND int_pol_act_noact = 'A' AND bank_sign = 'RB'";			
			}
			
			if((group3 != null) && (group4 == null)){
				SQLQuery = 
				"SELECT int_pol_type_id, int_pol_type_code, int_pol_type_name, int_pol_sh_sign FROM insu_policy_type WHERE "
				+ " rtrim(int_pol_type_code) LIKE ? AND int_pol_type_name LIKE ? AND int_pol_sh_sign LIKE ? "
				+ " AND int_pol_date_until > int_pol_date_from AND int_pol_act_noact = 'A' AND bank_sign = 'RB' AND (int_group2 = ? OR int_group3 = ?) ";			
			 
				searchableParams.add(3, group3);
				searchableParams.add(4, group3);
			}
			
			if((group3 == null) && (group4 != null)){
				SQLQuery = 
				"SELECT int_pol_type_id, int_pol_type_code, int_pol_type_name, int_pol_sh_sign FROM insu_policy_type WHERE "
				+ " rtrim(int_pol_type_code) LIKE ? AND int_pol_type_name LIKE ? AND int_pol_sh_sign LIKE ? "
				+ " AND int_pol_date_until > int_pol_date_from AND int_pol_act_noact = 'A' AND bank_sign = 'RB' AND int_group4 = ? ";			
				searchableParams.add(3, group4);
			}
			
			if((group3 != null) && (group4 != null)){
				SQLQuery = 
				"SELECT int_pol_type_id, int_pol_type_code, int_pol_type_name, int_pol_sh_sign FROM insu_policy_type WHERE "
				+ " rtrim(int_pol_type_code) LIKE ? AND int_pol_type_name LIKE ? AND int_pol_sh_sign LIKE ? "
				+ " AND int_pol_date_until > int_pol_date_from AND int_pol_act_noact = 'A' AND bank_sign = 'RB' AND (int_group2 = ? OR int_group3 = ?) AND int_group4 = ? ";			
				searchableParams.add(3, group3);
				searchableParams.add(4, group3);
				searchableParams.add(5, group4);
			}
			
		
		}else{
			
			//POSTAVLJEN OSIGURAVATELJ
			
			 
			if((group3 == null) && (group4 == null)){
				
				 
				SQLQuery = 
					"SELECT int_pol_type_id, int_pol_type_code, int_pol_type_name, int_pol_sh_sign FROM insu_policy_type WHERE "
					+ " rtrim(int_pol_type_code) LIKE ? AND rtrim(int_pol_type_name) LIKE ? AND int_pol_sh_sign LIKE ? "
					+ " AND int_pol_date_until > int_pol_date_from AND int_pol_act_noact = 'A' AND bank_sign = 'RB'  AND int_pol_company_id = ? ";			
				 
				System.out.println("SQLQuery " + SQLQuery);
				searchableParams.add(3, polCompanyId);
				
				
			}  
			if((group3 != null) && (group4 == null)){
				
				SQLQuery = 
					"SELECT int_pol_type_id, int_pol_type_code, int_pol_type_name, int_pol_sh_sign FROM insu_policy_type WHERE "
					+ " rtrim(int_pol_type_code) LIKE ? AND int_pol_type_name LIKE ? AND int_pol_sh_sign LIKE ? "
					+ " AND int_pol_date_until > int_pol_date_from AND int_pol_act_noact = 'A' AND bank_sign = 'RB' AND (int_group2 = ? OR int_group3 = ?) AND int_pol_company_id = ? ";			
				 
				searchableParams.add(3, group3);
				searchableParams.add(4, group3);
				searchableParams.add(5, polCompanyId);
			}
			if((group3 == null) && (group4 != null)){
				 
				SQLQuery = 
					"SELECT int_pol_type_id, int_pol_type_code, int_pol_type_name, int_pol_sh_sign FROM insu_policy_type WHERE "
					+ " rtrim(int_pol_type_code) LIKE ? AND int_pol_type_name LIKE ? AND int_pol_sh_sign LIKE ? "
					+ " AND int_pol_date_until > int_pol_date_from AND int_pol_act_noact = 'A' AND bank_sign = 'RB' AND int_group4 = ?  AND int_pol_company_id = ? ";			
				searchableParams.add(3, group4);
				searchableParams.add(4, polCompanyId);
			}

			
			if((group3 != null) && (group4 != null)){
				 
				SQLQuery = 
					"SELECT int_pol_type_id, int_pol_type_code, int_pol_type_name, int_pol_sh_sign FROM insu_policy_type WHERE "
					+ " rtrim(int_pol_type_code) LIKE ? AND int_pol_type_name LIKE ? AND int_pol_sh_sign LIKE ? "
					+ " AND int_pol_date_until > int_pol_date_from AND int_pol_act_noact = 'A' AND bank_sign = 'RB' AND (int_group2 = ? OR int_group3 = ?) AND int_group4 = ?   AND int_pol_company_id = ? ";			
				searchableParams.add(3, group3);
				searchableParams.add(4, group3);
				searchableParams.add(5, group4);
				searchableParams.add(6, polCompanyId);
			}	
		
		}  
		
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}  