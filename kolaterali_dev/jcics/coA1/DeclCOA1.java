package hr.vestigo.modules.collateral.jcics.coA1;

import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA1 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA1/DeclCOA1.java,v 1.7 2012/03/30 09:23:18 hramkr Exp $";
	private Map response = new HashMap();

	public DeclCOA1() {
	}

	// in args
	public Integer LookUpLevel = null;
	public BigDecimal cus_id = null;
	public String register_no = null;
	public String name = null;
	public String cocunat = null;
	public String rating = null;
	public BigDecimal residency_cou_id = null;
	public String residency_cou_num = null;
	public String residency_cou_name = null;
	public String issuer_status = null;
	public String tax_number = null;
	public String rating_out = null;
	public String retail_cust_flag = null;
	public String mlt_rating = null;

	// out args
	public TableData LookUpTableData = new TableData();

	// inner classes
	public INSUCOMPANYSELECTMAPP insucompanyselectmapp = null;

	public void setRequest(Map request) {
		LookUpLevel = (Integer) request.get("LookUpLevel");
		cus_id = (BigDecimal) request.get("cus_id");
		register_no = (String) request.get("register_no");
		name = (String) request.get("name");
		cocunat = (String) request.get("cocunat");
		rating = (String) request.get("rating");
		residency_cou_id = (BigDecimal) request.get("residency_cou_id");
		residency_cou_num = (String) request.get("residency_cou_num");
		residency_cou_name = (String) request.get("residency_cou_name");
		issuer_status = (String) request.get("issuer_status");
		tax_number = (String) request.get("tax_number");
		rating_out = (String) request.get("rating_out");
		retail_cust_flag = (String) request.get("retail_cust_flag");
		mlt_rating = (String) request.get("mlt_rating");
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
		if(mapping.equals("InsuCompanySelectMapp")) {
			insucompanyselectmapp = new INSUCOMPANYSELECTMAPP(request);
			return insucompanyselectmapp;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class INSUCOMPANYSELECTMAPP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public Integer LookUpLevel = null;
		public String insu_company_register_no = null;
		public String insu_company_name = null;

		// out args
		public TableData LookUpTableData = new TableData();

		public INSUCOMPANYSELECTMAPP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LookUpLevel = (Integer) request.get("LookUpLevel");
			insu_company_register_no = (String) request.get("insu_company_register_no");
			insu_company_name = (String) request.get("insu_company_name");
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