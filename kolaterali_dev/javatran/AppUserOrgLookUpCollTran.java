/*
 * Created on 2006.09.22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.javatran;

/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.util.List;

public class AppUserOrgLookUpCollTran extends LookUpTransaction {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/AppUserOrgLookUpCollTran.java,v 1.1 2006/09/22 08:52:10 hrasia Exp $";

	public AppUserOrgLookUpCollTran(ResourceAccessor ra) {
		super(ra);
	}
  
	public VestigoResultSet getResultSet(List searchableParams, String dbName, int lookUpType) throws Exception {
		String SQL = null;
		
		java.math.BigDecimal orgUniId = (java.math.BigDecimal)ra.getAttribute("AppUserOrgLDB", "org_uni_id");
		
		
		
		if(orgUniId != null){
			searchableParams.add(ra.getAttribute("AppUserOrgLDB", "org_uni_id"));
			searchableParams.add((String) ra.getAttribute("GDB", "bank_sign"));
		
		if (((String) searchableParams.get(1)).equals("%") && !((String) searchableParams.get(0)).equals("%")) {
			SQL =
				"SELECT distinct app_user.use_id"
					+ ", login"
					+ ", user_name"
					+ ", abbreviation"
					+ ", org_uni_id"
					+ " FROM app_user, user_signing"
					+ " WHERE RTRIM(login) = ?"
					+ " AND user_name_sc like ?"
					+ " AND abbreviation like ?"
					+ " AND org_uni_id = ?"
					+ " AND app_user.bank_sign = ?" 
					+ " AND app_user.use_id = user_signing.use_id";
		} else {    
			SQL =
				"SELECT distinct app_user.use_id"
					+ ", login"
					+ ", user_name"
					+ ", abbreviation"
					+ ", org_uni_id"    
					+ " FROM app_user, user_signing"
					+ " WHERE RTRIM(login) like ?"
					+ " AND user_name_sc like ?"
					+ " AND RTRIM(abbreviation) like ?"
					+ " AND org_uni_id = ?"
					+ " AND app_user.bank_sign = ?"
					+ " AND app_user.use_id = user_signing.use_id";
		}
		}else{
			
			searchableParams.add((String) ra.getAttribute("GDB", "bank_sign"));
			
			if (((String) searchableParams.get(1)).equals("%") && !((String) searchableParams.get(0)).equals("%")) {
				SQL =
					"SELECT distinct app_user.use_id"
						+ ", login"
						+ ", user_name"
						+ ", abbreviation"
						+ ", org_uni_id"
						+ " FROM app_user, user_signing"
						+ " WHERE RTRIM(login)  = ?"
						+ " AND user_name_sc like ?"
						+ " AND abbreviation like ?"
						
						+ " AND app_user.bank_sign = ?" 
						+ " AND app_user.use_id = user_signing.use_id";
			} else {    
				SQL =
					"SELECT distinct app_user.use_id"
						+ ", login"
						+ ", user_name"
						+ ", abbreviation"
						+ ", org_uni_id"    
						+ " FROM app_user, user_signing"
						+ " WHERE RTRIM(login) like ?"
						+ " AND user_name_sc like ?"
						+ " AND RTRIM(abbreviation) like ?"
						
						+ " AND app_user.bank_sign = ?"
						+ " AND app_user.use_id = user_signing.use_id";
			}
		}

		return ra.getMetaModelDB().selectPrepare(SQL, searchableParams, dbName);
	}

}
