
package hr.vestigo.modules.collateral.javatran;


import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;
import java.util.List;

/**
 * @author HRASIA
 */
 
 
public class RealEstateSubTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/RealEstateSubTypeLookUpTran.java,v 1.2 2006/08/19 12:39:50 hrasia Exp $";
	public RealEstateSubTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	}

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";
		java.math.BigDecimal reTypeId = null;
		reTypeId = (java.math.BigDecimal)ra.getAttribute("RealEstateSubTypeLookUpLDB","RE_TYPE_ID");
		
		if(reTypeId == null){
		
		SQLQuery =
			"SELECT re_sub_type_id, re_sub_type_code , re_sub_type_desc FROM re_sub_type WHERE rtrim(re_sub_type_code) LIKE ? "
				+ " AND rtrim(re_sub_type_desc) LIKE ? AND  bank_sign = 'RB' AND re_sub_status = 'A'";
		}else{
			SQLQuery =
				"SELECT re_sub_type_id, re_sub_type_code , re_sub_type_desc FROM re_sub_type WHERE rtrim(re_sub_type_code) LIKE ? "
					+ " AND rtrim(re_sub_type_desc) LIKE ? AND  bank_sign = 'RB' AND re_sub_status = 'A' AND re_type_id = ? ";
		
			searchableParams.add(2, reTypeId);
		}
		
		
		
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
	}
}