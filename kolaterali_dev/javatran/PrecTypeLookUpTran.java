/*
 * Created on 2006.01.28
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

public class PrecTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/PrecTypeLookUpTran.java,v 1.1 2006/01/28 15:27:55 hramkr Exp $";
	public PrecTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		this.debug("U look up-u sam prije upita");
		
		String SQLQuery = ""; 
		if (lookUpType == VConstants.PLAIN_LOOKUP) {

			SQLQuery = 
				"SELECT prt_typ_id, prt_type_code, prt_type_desc FROM precious_type WHERE "
				+ " prt_type_code LIKE ? AND prt_type_desc LIKE ? AND prt_date_unti > prt_date_from AND prt_act_noact = 'A' AND bank_sign = 'RB'";			
			 
		}
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}


