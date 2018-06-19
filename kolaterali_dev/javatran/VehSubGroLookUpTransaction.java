/*
 * Created on 2006.11.24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.javatran;

/**
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.math.BigDecimal;
import java.util.List;


public class VehSubGroLookUpTransaction extends LookUpTransaction {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/VehSubGroLookUpTransaction.java,v 1.1 2006/11/26 14:36:13 hramkr Exp $";

	public VehSubGroLookUpTransaction(ResourceAccessor ra) {
		super(ra);
	}
  
	public VestigoResultSet getResultSet(List searchableParams, String dbName, int lookUpType) throws Exception {
		String SQL = null;
		BigDecimal gro_id = (BigDecimal) ra.getAttribute("VehSubGroLookUpLDB", "gro_id");
		
		if(gro_id != null){
			searchableParams.add(ra.getAttribute("VehSubGroLookUpLDB", "gro_id"));
			searchableParams.add((String) ra.getAttribute("GDB", "bank_sign"));
		

				SQL =
				"SELECT veh_sub_id"
					+ ", veh_sub_code"
					+ ", veh_sub_desc"
					+ " FROM vehicle_subgroup"
					+ " WHERE RTRIM(veh_sub_code) like ?"
					+ " AND veh_gro_id = ?"
					+ " AND bank_sign = ?" ;
 
		}else{
			
			searchableParams.add((String) ra.getAttribute("GDB", "bank_sign"));
			
 
				SQL =
					"SELECT veh_sub_id"
					+ ", veh_sub_code"
					+ ", veh_sub_desc"
					+ " FROM vehicle_subgroup"
					+ " WHERE RTRIM(veh_sub_code) like ?"
					+ " AND bank_sign = ?" ;

		}

		return ra.getMetaModelDB().selectPrepare(SQL, searchableParams, dbName);
	}

}