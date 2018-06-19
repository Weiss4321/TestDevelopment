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

public class ArtTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/ArtTypeLookUpTran.java,v 1.1 2006/01/28 15:26:30 hramkr Exp $";
	public ArtTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		this.debug("U look up-u sam prije upita");
		
		String SQLQuery = "";
		if (lookUpType == VConstants.PLAIN_LOOKUP) {

			SQLQuery = 
				"SELECT wot_typ_id, wot_type_code, wot_type_desc FROM work_of_art_type WHERE "
				+ " wot_type_code LIKE ? AND wot_type_desc LIKE ? AND wot_date_unti > wot_date_from AND wot_act_noact = 'A' AND bank_sign = 'RB'";			
			 
		}
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}