/*
 * Created on 2006.02.06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.javatran;
import hr.vestigo.framework.common.VConstants;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.math.BigDecimal;
import java.util.List;
/**
 * @author HRAMKR
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class HarbourSecLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/HarbourSecLookUpTran.java,v 1.1 2006/02/09 09:15:31 hramkr Exp $";
	public HarbourSecLookUpTran(ResourceAccessor ra) {
		super(ra); 
	} 

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		this.debug("U look up-u sam prije upita");
		
		BigDecimal har_off_id = null;
		har_off_id = (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB", "HAR_OFF_ID");	
		
		String SQLQuery = ""; 
		if (lookUpType == VConstants.PLAIN_LOOKUP) {

			SQLQuery =  
				"SELECT a.har_sec_id as har_sec_id, a.has_office as has_office from harbour_off_sec a, harbour_office b WHERE"
				+ " a.har_off_id = " + har_off_id.toString()
				+ " AND a.has_office LIKE ? AND a.has_date_unti > a.has_date_from AND a.has_status = 'A'"
				+ " AND a.bank_sign = 'RB' AND a.har_off_id = b.har_off_id";			
			
		} 
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}