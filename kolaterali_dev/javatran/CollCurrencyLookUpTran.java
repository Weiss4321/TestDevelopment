package hr.vestigo.modules.collateral.javatran;

import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;
import java.util.List;

/**
 * @author rba0100
 */
public class CollCurrencyLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/CollCurrencyLookUpTran.java,v 1.1 2009/05/19 10:51:08 hramkr Exp $";
	public CollCurrencyLookUpTran(ResourceAccessor ra) {
		super(ra);
	}

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";
		SQLQuery =
			"SELECT a.cur_id, a.code_num, a.code_char, a.name, a.ord_no FROM currency a, exchange_rate b WHERE a.code_num LIKE ? "
				+ " AND a.code_char LIKE ? AND a.name_sc LIKE ? AND a.closing_date > current date AND a.cur_id = b.cur_id "
				+ " AND b.date_until = '9999-12-31' ORDER BY a.ord_no WITH UR";  
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
	}
}
  