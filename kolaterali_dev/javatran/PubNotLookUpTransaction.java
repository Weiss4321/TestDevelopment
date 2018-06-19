/*
 * Created on 2006.11.15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.javatran;

/**
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.math.BigDecimal;
import java.util.List;

/**      
 * @author HRAMKR
 */
public class PubNotLookUpTransaction extends LookUpTransaction {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/PubNotLookUpTransaction.java,v 1.1 2006/11/16 13:22:58 hramkr Exp $";

	public PubNotLookUpTransaction(ResourceAccessor ra) {
		super(ra);
	}
  
	public VestigoResultSet getResultSet(List searchableParams, String dbName, int lookUpType) throws Exception {
		String SQL = null;
		BigDecimal p_not_cty_id = (BigDecimal) ra.getAttribute("PubNotLDB", "p_not_cty_id");
		System.out.println("1. mjesto:"+p_not_cty_id);		
/*		if (p_not_cty_id != null) {
			searchableParams.add(ra.getAttribute("PubNotLDB", "p_not_cty_id"));
			System.out.println("2. tu sam :"+searchableParams);		
		}	*/
		
		searchableParams.add((String) ra.getAttribute("GDB", "bank_sign"));
		
		System.out.println("3. tu sam :"+searchableParams);		

//		String city = (String) searchableParams.get(1);
		
		
// nije poznato mjesto
//		if (p_not_cty_id == null) {
			
			System.out.println("4. tu sam :"+searchableParams);		

				SQL =
					"SELECT pub_not_id"
						+ ", name"
						+ ", adress"  
						+ ", pol_map_id"
						+ ", city"
						+ " FROM public_notary"
						+ " WHERE name_sc like ?"
						+ " AND city like ?"
						+ " AND bank_sign = ?";
			
/*		} else {

// imam dva slucaja, zadano mjesto kao uvjet pretrazivanja u lookupu i nije zadano
			if (city != null && !city.equals("")) {
				System.out.println("5. tu sam :"+searchableParams);		
				SQL =
					"SELECT pub_not_id"
						+ ", name"
						+ ", adress"
						+ ", pol_map_id"
						+ ", city"
						+ " FROM public_notary"
						+ " WHERE name_sc like ?"
						+ " AND city like ?"
						+ " AND bank_sign = ?";				
			} else {
				searchableParams.remove(1);
				searchableParams.add(ra.getAttribute("PubNotLDB", "p_not_cty_id"));
				System.out.println("6. tu sam :"+searchableParams);					
				SQL =
					"SELECT pub_not_id"
							+ ", name"
							+ ", adress"
							+ ", pol_map_id"
							+ ", org_uni_id" 
							+ ", city"
							+ " FROM public_notary"
						+ " WHERE name_sc like ?"
						+ " AND bank_sign = ?"
						+ " AND pol_map_id = ?";
			}
		}*/
    
		return ra.getMetaModelDB().selectPrepare(SQL, searchableParams, dbName);
	}

}