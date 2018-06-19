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
public class VehTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/VehTypeLookUpTran.java,v 1.2 2008/03/18 14:29:00 hramkr Exp $";
	public VehTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		
		this.debug("U look up-u sam prije upita");
		
		String SQLQuery = "";
		if (lookUpType == VConstants.PLAIN_LOOKUP) {

			SQLQuery = 
				"SELECT a.veh_gro_id, a.veh_gro_code, a.veh_gro_desc," +
				" b.val_per_min,  b.mvp_dfl, b.mvp_min, b.mvp_max, " +
				" b.hnb_dfl, b.hnb_min, b.hnb_max, " +
				" b.rzb_dfl, b.rzb_min, b.rzb_max " +
				" FROM vehicle_group a, coll_atr b WHERE " +
				" a.veh_gro_code LIKE ? AND a.veh_gro_desc LIKE ? AND a.date_until > a.date_from AND " +
				" a.status = 'A' AND a.bank_sign = 'RB' AND " +		
				" a.coll_type_id = b.coll_type_id AND (b.col_sub_id is null OR a.veh_gro_id = b.col_sub_id) ";						 
		}
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}  
			  