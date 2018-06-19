package hr.vestigo.modules.collateral.javatran;

import hr.vestigo.framework.common.VConstants;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.util.List;

/** 
 * @author HRAMKR
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class VrpStockLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/VrpStockLookUpTran.java,v 1.1 2008/04/18 13:51:07 hramkr Exp $";
	public VrpStockLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		this.debug("U look up-u sam prije upita");
		
		String SQLQuery = "";
		if (lookUpType == VConstants.PLAIN_LOOKUP) {

			SQLQuery = 
				"SELECT sto_exc_id, code, name, short_name FROM stock_exchange WHERE "
				+ " code LIKE ? AND name LIKE ? AND short_name like ? order by 3";			
			 
		}
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}