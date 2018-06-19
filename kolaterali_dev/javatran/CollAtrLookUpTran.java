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
public class CollAtrLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/CollAtrLookUpTran.java,v 1.2 2008/03/27 13:38:39 hramkr Exp $";
	public CollAtrLookUpTran(ResourceAccessor ra) {
		super(ra);
	} 
 
	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";		
		this.debug("U look up-u sam prije upita");
		String tempCollCategory = null;
//		BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("CollateralTypeLookUpLDB","col_cat_id_atr");		


		String CollCategory = (String) searchableParams.get(0);
		
		tempCollCategory = (String) ra.getAttribute("CollateralTypeLookUpLDB","CollateralSubCategory");
		
		if(tempCollCategory == null ){
			tempCollCategory = "%";
		}
		
		if(tempCollCategory.compareTo("SVE")== 0){
			//System.out.println ("BLABLA");
			tempCollCategory = "%";
		}  
		
		if (CollCategory.indexOf("%")<0){
  
			SQLQuery =    
				"SELECT a.coll_type_id, b.coll_type_code, b.coll_type_name, a.val_per_min, " +
	            " a.mvp_dfl, a.mvp_min, a.mvp_max, " +
				" a.hnb_dfl, a.hnb_min, a.hnb_max, " +
				" a.rzb_dfl, a.rzb_min, a.rzb_max, " +  
	            " a.col_prior, a.mortgage_flag, " +  
	            " a.posting_flag, a.date_from, a.date_until " + 
				" FROM coll_atr a, " + 
				" collateral_type b " + 
				" WHERE a.col_cat_id = b.col_cat_id " +
				" AND a.coll_type_id = b.coll_type_id " +
				" AND rtrim(b.coll_type_code) = ? AND b.coll_type_name LIKE ? AND b.sub_category LIKE ? " + 
				" AND a.date_until > a.date_from AND a.status = 'A' AND a.bank_sign = 'RB' AND b.coll_status = 'A' AND b.coll_spec_status = 'A'";
		} else {
			SQLQuery = 
				"SELECT a.coll_type_id, b.coll_type_code, b.coll_type_name, a.val_per_min, " +
	            " a.mvp_dfl, a.mvp_min, a.mvp_max, " +
				" a.hnb_dfl, a.hnb_min, a.hnb_max, " +
				" a.rzb_dfl, a.rzb_min, a.rzb_max, " +
	            " a.col_prior, a.mortgage_flag, " +  
	            " a.posting_flag, a.date_from, a.date_until " + 
				" FROM coll_atr a, " + 
				" collateral_type b " + 
				" WHERE a.col_cat_id = b.col_cat_id " +
				" AND a.coll_type_id = b.coll_type_id " +
				" AND rtrim(b.coll_type_code) LIKE ? AND b.coll_type_name LIKE ? AND b.sub_category LIKE ? " + 
				" AND a.date_until > a.date_from AND a.status = 'A' AND a.bank_sign = 'RB' AND b.coll_status = 'A' AND b.coll_spec_status = 'A'";
		}  
		   
		   
		this.debug("SQLQuery je " + SQLQuery);
		searchableParams.add(2, tempCollCategory);
		tempCollCategory = null;	
//		searchableParams.add(2, col_cat_id);	
//		col_cat_id = null;	

		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
		
	}	  
}


