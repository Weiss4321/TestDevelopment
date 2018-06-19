/*
 * Created on 2006.05.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.javatran;

import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;

import hr.vestigo.framework.common.VConstants;
import hr.vestigo.framework.util.db.VestigoResultSet;
import java.util.List;

/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InsuCompanyLookupTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/InsuCompanyLookupTran.java,v 1.7 2008/06/05 10:26:35 hramkr Exp $";
	public InsuCompanyLookupTran(ResourceAccessor ra) {
		super(ra);
	} 
	
	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType) throws Exception {
	
		this.debug("U look up-u sam prije upita");
	
		
		String SQLQuery = ""; 
		SQLQuery = 
			"SELECT icom.ic_id as ic_id, cust.register_no as ic_register_no, cust.name as ic_name, icom.ic_code as ic_code  " +
			" FROM insu_company icom, customer cust " +
			" WHERE  rtrim(cust.register_no) LIKE ?  AND cust.name LIKE ?  AND icom.ic_code LIKE ? " +
			" AND icom.ic_cus_id = cust.cus_id AND ( current date BETWEEN icom.ic_from AND icom.ic_until ) AND icom.ic_status = 'A' " +
			" ORDER BY cust.name ";			
				
			
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
	}	

	
	
}
