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
public class VehicleTypeLookUpTran extends LookUpTransaction {

	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/VehicleTypeLookUpTran.java,v 1.5 2006/06/06 06:47:16 hrasia Exp $";
	
	public VehicleTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	}

	

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";
		SQLQuery =
			"SELECT vet_type_id, vet_make, vet_type, vet_size_type, vet_date_from, vet_date_unti " +
			" FROM vehicle_type WHERE  vet_make LIKE ? AND vet_type LIKE ? AND vet_size_type LIKE ? AND " +
				 " (vet_date_unti > vet_date_from) AND  bank_sign = 'RB' AND vet_status = 'A'";
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
	}
	
	
	
}
