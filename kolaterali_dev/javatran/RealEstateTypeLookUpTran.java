/*
 * Created on 2005.11.29
 *
 */
package hr.vestigo.modules.collateral.javatran;


import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;
import java.util.List;

/**
 * @author HRASIA
 */
 
 

public class RealEstateTypeLookUpTran extends LookUpTransaction {
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/RealEstateTypeLookUpTran.java,v 1.7 2008/03/18 14:28:23 hramkr Exp $";
	public RealEstateTypeLookUpTran(ResourceAccessor ra) {
		super(ra);
	}

	public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
		throws Exception {
		String SQLQuery = "";
		java.math.BigDecimal collateralTypeId = null;
		collateralTypeId = (java.math.BigDecimal)ra.getAttribute("RealEstateTypeLookUpLDB","collateralTypeId");
		
		if(collateralTypeId == null){
		 
		SQLQuery =
			"SELECT a.real_es_type_id, a.real_es_type_code, a.real_es_type_desc, " +
			" b.val_per_min as coll_period_rev,  b.mvp_dfl as coll_mvp_ponder, " +
			" b.mvp_min as coll_mvp_ponder_mn, b.mvp_max as coll_mvp_ponder_mx, " +
			" b.hnb_dfl as coll_hnb_ponder, b.hnb_min as coll_hnb_ponder_mn, b.hnb_max as coll_hnb_ponder_mx, " +
			" b.rzb_dfl as coll_rzb_ponder, b.rzb_min as coll_rzb_ponder_mn, b.rzb_max as coll_rzb_ponder_mx, " +
			" a.real_es_date_from, a.real_es_date_unti " +
			" FROM real_estate_type a, coll_atr b WHERE " +
			" rtrim(a.real_es_type_code) LIKE ? AND rtrim(a.real_es_type_desc) LIKE ? AND " +
			" a.real_es_date_unti > a.real_es_date_from AND a.bank_sign = 'RB' AND a.real_es_act_noact = 'A' AND " +
			" a.coll_type_id = b.coll_type_id AND (b.col_sub_id is null OR a.real_es_type_id = b.col_sub_id) ";	
		}else{
			SQLQuery =
				"SELECT a.real_es_type_id, a.real_es_type_code, a.real_es_type_desc, " +
				" b.val_per_min as coll_period_rev,  b.mvp_dfl as coll_mvp_ponder, " +
				" b.mvp_min as coll_mvp_ponder_mn, b.mvp_max as coll_mvp_ponder_mx, " +
				" b.hnb_dfl as coll_hnb_ponder, b.hnb_min as coll_hnb_ponder_mn, b.hnb_max as coll_hnb_ponder_mx, " +
				" b.rzb_dfl as coll_rzb_ponder, b.rzb_min as coll_rzb_ponder_mn, b.rzb_max as coll_rzb_ponder_mx, " +
				" a.real_es_date_from, a.real_es_date_unti " +
				" FROM real_estate_type a, coll_atr b WHERE " +
				" rtrim(a.real_es_type_code) LIKE ? AND rtrim(a.real_es_type_desc) LIKE ? AND " +
				" a.real_es_date_unti > a.real_es_date_from AND a.bank_sign = 'RB' AND a.real_es_act_noact = 'A' AND a.coll_type_id = ? AND " +
				" a.coll_type_id = b.coll_type_id AND (b.col_sub_id is null OR a.real_es_type_id = b.col_sub_id) ";					
				
			searchableParams.add(2, collateralTypeId);
		}
		
		
		
		return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
	}
}

