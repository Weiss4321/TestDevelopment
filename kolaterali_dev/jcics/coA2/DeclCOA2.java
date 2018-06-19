package hr.vestigo.modules.collateral.jcics.coA2;

import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA2 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA2/DeclCOA2.java,v 1.9 2017/04/14 10:17:31 hraziv Exp $";
	private Map response = new HashMap();

	public DeclCOA2() {
	}

	// in args
	public Integer LookUpLevel = null;
	public String isin = null;
	public BigDecimal col_cat_id = null;
	public BigDecimal col_typ_id = null;

	// out args
	public TableData LookUpTableData = new TableData();

	// inner classes
	public COLLESTIMATORLOOKUPMAPPING collestimatorlookupmapping = null;
	public ECONOMICLIFESELECTMAPPING economiclifeselectmapping = null;
	public COLLGCMTYPLOOKUPMAPPING collgcmtyplookupmapping = null;

	public void setRequest(Map request) {
		LookUpLevel = (Integer) request.get("LookUpLevel");
		isin = (String) request.get("isin");
		col_cat_id = (BigDecimal) request.get("col_cat_id");
		col_typ_id = (BigDecimal) request.get("col_typ_id");
	}

	public Map getResponse() {
		response.put("LookUpTableData", LookUpTableData);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollEstimatorLookUpMapping")) {
			collestimatorlookupmapping = new COLLESTIMATORLOOKUPMAPPING(request);
			return collestimatorlookupmapping;
		} else if(mapping.equals("EconomicLifeSelectMapping")) {
			economiclifeselectmapping = new ECONOMICLIFESELECTMAPPING(request);
			return economiclifeselectmapping;
		} else if(mapping.equals("CollGcmTypLookUpMapping")) {
			collgcmtyplookupmapping = new COLLGCMTYPLOOKUPMAPPING(request);
			return collgcmtyplookupmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLESTIMATORLOOKUPMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public Date estDate = null;
		public BigDecimal est_cus_id = null;
		public String est_register_no = null;
		public String est_name = null;
		public String est_tax_number = null;
		public BigDecimal est_comp_cus_id = null;
		public String est_comp_register_no = null;
		public String est_comp_name = null;
		public String est_comp_tax_number = null;
		public String est_type = null;
		public String est_type_code = null;
		public Integer LookUpLevel = null;

		// out args
		public TableData LookUpTableData = new TableData();

		public COLLESTIMATORLOOKUPMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			estDate = (Date) request.get("estDate");
			est_cus_id = (BigDecimal) request.get("est_cus_id");
			est_register_no = (String) request.get("est_register_no");
			est_name = (String) request.get("est_name");
			est_tax_number = (String) request.get("est_tax_number");
			est_comp_cus_id = (BigDecimal) request.get("est_comp_cus_id");
			est_comp_register_no = (String) request.get("est_comp_register_no");
			est_comp_name = (String) request.get("est_comp_name");
			est_comp_tax_number = (String) request.get("est_comp_tax_number");
			est_type = (String) request.get("est_type");
			est_type_code = (String) request.get("est_type_code");
			LookUpLevel = (Integer) request.get("LookUpLevel");
		}

		public Map getResponse() {
			response.put("LookUpTableData", LookUpTableData);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class ECONOMICLIFESELECTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_cat_id = null;
		public BigDecimal col_typ_id = null;
		public BigDecimal col_sub_id = null;

		// out args
		public Integer economic_life = null;

		public ECONOMICLIFESELECTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_cat_id = (BigDecimal) request.get("col_cat_id");
			col_typ_id = (BigDecimal) request.get("col_typ_id");
			col_sub_id = (BigDecimal) request.get("col_sub_id");
		}

		public Map getResponse() {
			response.put("economic_life", economic_life);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLGCMTYPLOOKUPMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_gcm_typ_id = null;
		public String map_code = null;
		public Integer ord_no = null;
		public String code = null;
		public String name = null;
		public String name_add = null;
		public String param_value = null;
		public String param_indic = null;
		public Integer LookUpLevel = null;
		public Date currentDate = null;

		// out args
		public TableData LookUpTableData = new TableData();

		public COLLGCMTYPLOOKUPMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_gcm_typ_id = (BigDecimal) request.get("col_gcm_typ_id");
			map_code = (String) request.get("map_code");
			ord_no = (Integer) request.get("ord_no");
			code = (String) request.get("code");
			name = (String) request.get("name");
			name_add = (String) request.get("name_add");
			param_value = (String) request.get("param_value");
			param_indic = (String) request.get("param_indic");
			LookUpLevel = (Integer) request.get("LookUpLevel");
			currentDate = (Date) request.get("currentDate");
		}

		public Map getResponse() {
			response.put("LookUpTableData", LookUpTableData);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}