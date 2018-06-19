/*
 * Created on 2007.03.27
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
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollCategoryLookUpTran extends LookUpTransaction {
		public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/CollCategoryLookUpTran.java,v 1.2 2012/02/14 08:50:32 hramkr Exp $";

		public CollCategoryLookUpTran(ResourceAccessor ra) {
			super(ra);
		}
	  
		public VestigoResultSet getResultSet(List searchableParams, String dbName, int lookUpType) throws Exception {
			String SQL = null;
			
	
			String bank_sign = (String)ra.getAttribute("GDB", "bank_sign");

			
			if (lookUpType == VConstants.PLAIN_LOOKUP) {
				SQL = 
					"SELECT col_cat_id, code, name FROM coll_category WHERE "
					+ " code LIKE ? AND "
					+ " screen_name <> 'X' AND "
					+ " bank_sign = '"+bank_sign+"'"
					+ " ORDER BY order_num";			
			
			}
			
			this.debug("SQLQuery je " + SQL);
			return ra.getMetaModelDB().selectPrepare(SQL, searchableParams, dbName);
		}

}
