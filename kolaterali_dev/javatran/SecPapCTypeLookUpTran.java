/*
 * Created on 2006.01.11
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
public class SecPapCTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/SecPapCTypeLookUpTran.java,v 1.3 2006/03/07 09:33:45 hramkr Exp $";
	public SecPapCTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		String CollateralSubCategory = "";
		CollateralSubCategory = (String)ra.getAttribute("CollSecPaperLDB","CollateralSubCategory");
		
		this.debug("U look up-u sam prije upita");
		this.debug("PODVRSTA: " + CollateralSubCategory);
		
		
		String SQLQuery = "";
		if (lookUpType == VConstants.PLAIN_LOOKUP) {

//			SQLQuery = 
//				"SELECT sec_typ_id, sec_type_code, sec_type, sec_subtype, sec_type_name FROM security_type WHERE sec_type_code LIKE ? "
//				+ " AND sec_subtype LIKE ? AND sec_type_name LIKE ? AND sec_date_until > sec_date_from AND  bank_sign = 'RB' AND sec_act_noact = 'A'";
	
			SQLQuery = 
				"SELECT sec_typ_id, sec_type_code, sec_type, sec_subtype, sec_type_name FROM security_type WHERE "
				+ " sec_subtype LIKE ? AND sec_type_name LIKE ? AND sec_date_until > sec_date_from AND  bank_sign = 'RB' AND sec_act_noact = 'A'";			
			
//poznat tip vrijednosnice
			if (CollateralSubCategory!=null && !CollateralSubCategory.equals("")) {
				SQLQuery+= " AND sec_type_code = '" + CollateralSubCategory + "'";				
			}				
			 
		}
	 	
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}


