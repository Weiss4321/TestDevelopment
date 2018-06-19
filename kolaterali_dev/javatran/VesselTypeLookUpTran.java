/*
 * Created on 2006.02.01
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

public class VesselTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/VesselTypeLookUpTran.java,v 1.3 2008/03/18 14:29:00 hramkr Exp $";
	public VesselTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		this.debug("U look up-u sam prije upita");
		
		String SQLQuery = "";
		if (lookUpType == VConstants.PLAIN_LOOKUP) {

			SQLQuery = 
				"SELECT a.ves_typ_id, a.ves_code, a.ves_dsc, " +
				" b.val_per_min,  b.mvp_dfl, b.mvp_min, b.mvp_max, " +
				" b.hnb_dfl, b.hnb_min, b.hnb_max, " +
				" b.rzb_dfl, b.rzb_min, b.rzb_max " +				
				" FROM vessel_type a, coll_atr b WHERE " +
				" a.ves_code LIKE ? AND a.ves_dsc LIKE ? AND a.ves_date_unti > a.ves_date_from AND " +
				" a.ves_act_noact = 'A' AND a.bank_sign = 'RB'AND " +		
				" a.ves_typ_id = b.col_sub_id AND " +
				" b.col_cat_id = 620223 AND b.coll_type_id = 15777 ";
		
			
//			"SELECT ves_typ_id, ves_type, ves_make FROM vessel_type WHERE "
//			+ " ves_type LIKE ? AND ves_make LIKE ? AND ves_date_unti > ves_date_from AND ves_act_noact = 'A' AND bank_sign = 'RB'";				
			 
		}
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}

				