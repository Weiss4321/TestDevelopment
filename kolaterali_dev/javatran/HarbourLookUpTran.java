/*
 * Created on 2006.02.03
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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

public class HarbourLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/HarbourLookUpTran.java,v 1.1 2006/02/09 09:15:23 hramkr Exp $";
	public HarbourLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		this.debug("U look up-u sam prije upita");
		
		String SQLQuery = "";
		if (lookUpType == VConstants.PLAIN_LOOKUP) {

			SQLQuery = 
				"SELECT har_off_id, hao_office from harbour_office WHERE "
				+ " hao_office LIKE ? AND hao_date_unti > hao_date_from AND hao_status = 'A' AND bank_sign = 'RB'";			
			 
		}
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}