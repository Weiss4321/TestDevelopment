/*
 * Created on 2006.03.06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.javatran;

import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.util.db.VestigoResultSet;
import java.util.List;
/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EngineTypeLookUpTran extends LookUpTransaction {

	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/EngineTypeLookUpTran.java,v 1.2 2006/03/06 10:45:33 hrasia Exp $";
	
	public EngineTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	}


	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";
		SQLQuery =
			"SELECT eng_type_id, eng_make, eng_type, eng_fuel_type, eng_date_from, eng_date_unti " +
			" FROM engine_type WHERE  eng_make LIKE ? AND eng_type LIKE ? AND eng_fuel_type LIKE ? AND " +
				 " (eng_date_unti > eng_date_from)  AND  bank_sign = 'RB' AND eng_status = 'A'";
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
	}
	
	
	
}
