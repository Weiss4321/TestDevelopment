
package hr.vestigo.modules.collateral.javatran;

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
public class VrpRatingLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/VrpRatingLookUpTran.java,v 1.1 2008/04/18 13:51:07 hramkr Exp $";
	public VrpRatingLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 
 
	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";		
		this.debug("U look up-u sam prije upita");
		
		BigDecimal rat_typ_id = (BigDecimal) ra.getAttribute("VrpIsinLDB","col_typ_id");
 		System.out.println("provjera rat_typ_id:"+rat_typ_id);	
		if(rat_typ_id != null){
			searchableParams.add(rat_typ_id);	
		}	
    

		if (rat_typ_id != null) {
	 		System.out.println("1. tu sam: "+rat_typ_id);	
			SQLQuery = 
				"SELECT a.rat_typ_id as rat_typ_id, a.rat_typ_desc as rat_typ_desc, " +
				" b.rat_sco_id as rat_sco_id, b.score as score , b.score_order as score_desc, " +
				" a.date_from as date_from, a.date_until as date_until " +
				" FROM rating_type a, rating_score b WHERE " +
				" a.rat_typ_id = ? AND a.rat_typ_id = b.rat_typ_id AND " +
				" a.date_from <= current date  AND a.date_until >= current date " +
				" order by 1, 5 asc "; 	
		} else {
	 		System.out.println("2. tu sam: "+rat_typ_id);	
			SQLQuery = 
				"SELECT a.rat_typ_id as rat_typ_id, a.rat_typ_desc as rat_typ_desc, " +
				" b.rat_sco_id as rat_sco_id, b.score as score , b.score_order as score_desc, " +
				" a.date_from as date_from, a.date_until as date_until " +
				" FROM rating_type a, rating_score b WHERE " +
				" a.rat_typ_id = b.rat_typ_id AND " +
				" a.date_from <= current date  AND a.date_until >= current date " +
				" order by 1, 5 asc "; 	 			
		}
		
		this.debug("SQLQuery je " + SQLQuery);
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	
}


