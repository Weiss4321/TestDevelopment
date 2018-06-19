package hr.vestigo.modules.collateral.jcics.co40;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO40 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co40/DeclCO40.java,v 1.6 2012/03/06 12:39:05 hramlo Exp $";
	private Map response = new HashMap();

	public DeclCO40() {
	}

	// in args
	public BigDecimal col_hea_id = null;
	public BigDecimal use_id = null;
	public BigDecimal org_uni_id = null;
	public BigDecimal col_cat_id = null;

	// out args
	public String Kolateral_txtColNumQBE = null;

	// inner classes
	public VEHDEACTOWNERMAPPING vehdeactownermapping = null;
	public MOVDEACTOWNERMAPPING movdeactownermapping = null;
	public VESDEACTOWNERMAPPING vesdeactownermapping = null;
	public VRPDEACTOWNERMAPPING vrpdeactownermapping = null;
	public INSPOLDEACTOWNERMAPPING inspoldeactownermapping = null;
	public CASHDEPDEACTOWNERMAPPING cashdepdeactownermapping = null;
	public GUARANTDEACTOWNERMAPPING guarantdeactownermapping = null;

	public void setRequest(Map request) {
		col_hea_id = (BigDecimal) request.get("col_hea_id");
		use_id = (BigDecimal) request.get("use_id");
		org_uni_id = (BigDecimal) request.get("org_uni_id");
		col_cat_id = (BigDecimal) request.get("col_cat_id");
	}

	public Map getResponse() {
		response.put("Kolateral_txtColNumQBE", Kolateral_txtColNumQBE);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("VehDeactOwnerMapping")) {
			vehdeactownermapping = new VEHDEACTOWNERMAPPING(request);
			return vehdeactownermapping;
		} else if(mapping.equals("MovDeactOwnerMapping")) {
			movdeactownermapping = new MOVDEACTOWNERMAPPING(request);
			return movdeactownermapping;
		} else if(mapping.equals("VesDeactOwnerMapping")) {
			vesdeactownermapping = new VESDEACTOWNERMAPPING(request);
			return vesdeactownermapping;
		} else if(mapping.equals("VrpDeactOwnerMapping")) {
			vrpdeactownermapping = new VRPDEACTOWNERMAPPING(request);
			return vrpdeactownermapping;
		} else if(mapping.equals("InsPolDeactOwnerMapping")) {
			inspoldeactownermapping = new INSPOLDEACTOWNERMAPPING(request);
			return inspoldeactownermapping;
		} else if(mapping.equals("CashDepDeactOwnerMapping")) {
			cashdepdeactownermapping = new CASHDEPDEACTOWNERMAPPING(request);
			return cashdepdeactownermapping;
		} else if(mapping.equals("GuarantDeactOwnerMapping")) {
			guarantdeactownermapping = new GUARANTDEACTOWNERMAPPING(request);
			return guarantdeactownermapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class VEHDEACTOWNERMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public BigDecimal col_cat_id = null;

		// out args
		public String Kolateral_txtColNumQBE = null;

		public VEHDEACTOWNERMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			col_cat_id = (BigDecimal) request.get("col_cat_id");
		}

		public Map getResponse() {
			response.put("Kolateral_txtColNumQBE", Kolateral_txtColNumQBE);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class MOVDEACTOWNERMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public BigDecimal col_cat_id = null;

		// out args
		public String Kolateral_txtColNumQBE = null;

		public MOVDEACTOWNERMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			col_cat_id = (BigDecimal) request.get("col_cat_id");
		}

		public Map getResponse() {
			response.put("Kolateral_txtColNumQBE", Kolateral_txtColNumQBE);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class VESDEACTOWNERMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public BigDecimal col_cat_id = null;

		// out args
		public String Kolateral_txtColNumQBE = null;

		public VESDEACTOWNERMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			col_cat_id = (BigDecimal) request.get("col_cat_id");
		}

		public Map getResponse() {
			response.put("Kolateral_txtColNumQBE", Kolateral_txtColNumQBE);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class VRPDEACTOWNERMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public BigDecimal col_cat_id = null;

		// out args
		public String Kolateral_txtColNumQBE = null;

		public VRPDEACTOWNERMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			col_cat_id = (BigDecimal) request.get("col_cat_id");
		}

		public Map getResponse() {
			response.put("Kolateral_txtColNumQBE", Kolateral_txtColNumQBE);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSPOLDEACTOWNERMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public BigDecimal col_cat_id = null;
		public BigDecimal COL_TYPE_ID = null;

		// out args
		public String Kolateral_txtColNumQBE = null;

		public INSPOLDEACTOWNERMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			col_cat_id = (BigDecimal) request.get("col_cat_id");
			COL_TYPE_ID = (BigDecimal) request.get("COL_TYPE_ID");
		}

		public Map getResponse() {
			response.put("Kolateral_txtColNumQBE", Kolateral_txtColNumQBE);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CASHDEPDEACTOWNERMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public BigDecimal col_cat_id = null;
		public BigDecimal cde_cus_id = null;

		// out args
		public String Kolateral_txtColNumQBE = null;

		public CASHDEPDEACTOWNERMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			col_cat_id = (BigDecimal) request.get("col_cat_id");
			cde_cus_id = (BigDecimal) request.get("cde_cus_id");
		}

		public Map getResponse() {
			response.put("Kolateral_txtColNumQBE", Kolateral_txtColNumQBE);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class GUARANTDEACTOWNERMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal col_cat_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;

		// out args
		public String Kolateral_txtColNumQBE = null;

		public GUARANTDEACTOWNERMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			col_cat_id = (BigDecimal) request.get("col_cat_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
		}

		public Map getResponse() {
			response.put("Kolateral_txtColNumQBE", Kolateral_txtColNumQBE);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}