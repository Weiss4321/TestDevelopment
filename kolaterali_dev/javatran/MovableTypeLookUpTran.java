/*
/*
 * Created on 2006.01.21
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
public class MovableTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/MovableTypeLookUpTran.java,v 1.3 2008/03/18 14:29:58 hramkr Exp $";
	public MovableTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 
 
	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";		
		this.debug("U look up-u sam prije upita");
		
		BigDecimal col_typ_id = (BigDecimal) ra.getAttribute("MovTypLDB","mov_col_typ_id");
		if(col_typ_id != null){
			searchableParams.add(col_typ_id);	
		}	
   

		if (col_typ_id != null) {

			SQLQuery = 
				"SELECT a.mov_typ_id, a.mov_typ_code, a.mov_typ_dsc, " +
				" b.val_per_min,  b.mvp_dfl, b.mvp_min, b.mvp_max, " +
				" b.hnb_dfl, b.hnb_min, b.hnb_max, " +
				" b.rzb_dfl, b.rzb_min, b.rzb_max " +
				" FROM movable_type a, coll_atr b WHERE " +
				" a.mov_typ_code LIKE ? AND a.mov_typ_dsc LIKE ? AND a.mov_date_unti > a.mov_date_from AND " +
				" a.bank_sign = 'RB' AND a.mov_act_noact = 'A' AND a.col_typ_id = ? AND " +
				" a.col_typ_id = b.coll_type_id AND (b.col_sub_id is null OR a.mov_typ_id = b.col_sub_id) ";			
		} else {
			SQLQuery = 
				"SELECT a.mov_typ_id, a.mov_typ_code, a.mov_typ_dsc, " +
				" b.val_per_min,  b.mvp_dfl, b.mvp_min, b.mvp_max, " +
				" b.hnb_dfl, b.hnb_min, b.hnb_max, " +
				" b.rzb_dfl, b.rzb_min, b.rzb_max " +
				" FROM movable_type a, coll_atr b WHERE " +
				" a.mov_typ_code LIKE ? AND a.mov_typ_dsc LIKE ? AND a.mov_date_unti > a.mov_date_from AND " +
				" a.bank_sign = 'RB' AND a.mov_act_noact = 'A' AND " +
				" a.col_typ_id = b.coll_type_id AND (b.col_sub_id is null OR a.mov_typ_id = b.col_sub_id)	";						
		}
		
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}


