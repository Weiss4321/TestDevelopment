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
public class CollCourtLookUpTran extends LookUpTransaction {

	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/CollCourtLookUpTran.java,v 1.4 2008/05/05 12:18:28 hramkr Exp $";
	
	public CollCourtLookUpTran(ResourceAccessor ra) {
		super(ra);
	}

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";
		SQLQuery =
			"SELECT co_id, co_code, co_name, co_pol_map_id_cnt ,co_date_from, co_date_until " +
			     " FROM coll_court WHERE rtrim(co_code)  LIKE ?  " +
				 " AND co_name LIKE ? AND co_date_until > co_date_from AND " +
				 " bank_sign = 'RB' AND co_status = 'A' AND court_type = 'O' ";
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
	}
	
}
