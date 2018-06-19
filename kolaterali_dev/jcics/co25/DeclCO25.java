package hr.vestigo.modules.collateral.jcics.co25;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.math.*;

import hr.vestigo.framework.common.*;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO25 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co25/DeclCO25.java,v 1.2 2006/09/21 10:20:52 hrasia Exp $";
	private Map response = new HashMap();

	public DeclCO25() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;
	public String ScreenEntryParam = null;
	public BigDecimal use_id = null;
	public BigDecimal org_uni_id = null;
	public String RealEstate_txtOwnerQbeRegNo2 = null;
	public String RealEstate_txtCarrierQbeRegNo2 = null;
	public String RealEstate_txtQbeAccNo2 = null;
	public String RealEstate_txtQbeCode2 = null;
	public BigDecimal RealEstate_Qbe_ORG_UNI_ID2 = null;
	public BigDecimal RealEstate_QbeUSE_OPEN_ID2 = null;
	public Date RealEstate_txtQbeDateFrom2 = null;
	public Date RealEstate_txtQbeDateUntil2 = null;
	public String RealEstate_txtQbeRequestNo2 = null;

	//	out args
	public TableData tblColWorkList = new TableData();
	public Timestamp Coll_USER_LOCK = null;

	// inner classes
	public LOANSTOCKQBEMAPP loanstockqbemapp = null;
	public CASHDEPQBEMAPP cashdepqbemapp = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		ScreenEntryParam = (String) request.get("ScreenEntryParam");
		use_id = (BigDecimal) request.get("use_id");
		org_uni_id = (BigDecimal) request.get("org_uni_id");
		RealEstate_txtOwnerQbeRegNo2 = (String) request.get("RealEstate_txtOwnerQbeRegNo2");
		RealEstate_txtCarrierQbeRegNo2 = (String) request.get("RealEstate_txtCarrierQbeRegNo2");
		RealEstate_txtQbeAccNo2 = (String) request.get("RealEstate_txtQbeAccNo2");
		RealEstate_txtQbeCode2 = (String) request.get("RealEstate_txtQbeCode2");
		RealEstate_Qbe_ORG_UNI_ID2 = (BigDecimal) request.get("RealEstate_Qbe_ORG_UNI_ID2");
		RealEstate_QbeUSE_OPEN_ID2 = (BigDecimal) request.get("RealEstate_QbeUSE_OPEN_ID2");
		RealEstate_txtQbeDateFrom2 = (Date) request.get("RealEstate_txtQbeDateFrom2");
		RealEstate_txtQbeDateUntil2 = (Date) request.get("RealEstate_txtQbeDateUntil2");
		RealEstate_txtQbeRequestNo2 = (String) request.get("RealEstate_txtQbeRequestNo2");
	}

	public Map getResponse() {
		response.put("tblColWorkList", tblColWorkList);
		response.put("Coll_USER_LOCK", Coll_USER_LOCK);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("LoanStockQbeMapp")) {
			loanstockqbemapp = new LOANSTOCKQBEMAPP(request);
			return loanstockqbemapp;
		} else if(mapping.equals("CashDepQbeMapp")) {
			cashdepqbemapp = new CASHDEPQBEMAPP(request);
			return cashdepqbemapp;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class LOANSTOCKQBEMAPP implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public String ScreenEntryParam = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String RealEstate_txtOwnerQbeRegNo2 = null;
		public String RealEstate_txtCarrierQbeRegNo2 = null;
		public String RealEstate_txtQbeAccNo2 = null;
		public String RealEstate_txtQbeCode2 = null;
		public BigDecimal RealEstate_Qbe_ORG_UNI_ID2 = null;
		public BigDecimal RealEstate_QbeUSE_OPEN_ID2 = null;
		public Date RealEstate_txtQbeDateFrom2 = null;
		public Date RealEstate_txtQbeDateUntil2 = null;
		public String RealEstate_txtQbeRequestNo2 = null;

		//	out args
		public TableData tblColWorkList = new TableData();
		public Timestamp Coll_USER_LOCK = null;

		public LOANSTOCKQBEMAPP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			ScreenEntryParam = (String) request.get("ScreenEntryParam");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			RealEstate_txtOwnerQbeRegNo2 = (String) request.get("RealEstate_txtOwnerQbeRegNo2");
			RealEstate_txtCarrierQbeRegNo2 = (String) request.get("RealEstate_txtCarrierQbeRegNo2");
			RealEstate_txtQbeAccNo2 = (String) request.get("RealEstate_txtQbeAccNo2");
			RealEstate_txtQbeCode2 = (String) request.get("RealEstate_txtQbeCode2");
			RealEstate_Qbe_ORG_UNI_ID2 = (BigDecimal) request.get("RealEstate_Qbe_ORG_UNI_ID2");
			RealEstate_QbeUSE_OPEN_ID2 = (BigDecimal) request.get("RealEstate_QbeUSE_OPEN_ID2");
			RealEstate_txtQbeDateFrom2 = (Date) request.get("RealEstate_txtQbeDateFrom2");
			RealEstate_txtQbeDateUntil2 = (Date) request.get("RealEstate_txtQbeDateUntil2");
			RealEstate_txtQbeRequestNo2 = (String) request.get("RealEstate_txtQbeRequestNo2");
		}

		public Map getResponse() {
			response.put("tblColWorkList", tblColWorkList);
			response.put("Coll_USER_LOCK", Coll_USER_LOCK);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CASHDEPQBEMAPP implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public String ScreenEntryParam = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String RealEstate_txtOwnerQbeRegNo2 = null;
		public String RealEstate_txtCarrierQbeRegNo2 = null;
		public String RealEstate_txtQbeAccNo2 = null;
		public String RealEstate_txtQbeCode2 = null;
		public BigDecimal RealEstate_Qbe_ORG_UNI_ID2 = null;
		public BigDecimal RealEstate_QbeUSE_OPEN_ID2 = null;
		public Date RealEstate_txtQbeDateFrom2 = null;
		public Date RealEstate_txtQbeDateUntil2 = null;
		public String RealEstate_txtQbeRequestNo2 = null;

		//	out args
		public TableData tblColWorkList = new TableData();
		public Timestamp Coll_USER_LOCK = null;

		public CASHDEPQBEMAPP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			ScreenEntryParam = (String) request.get("ScreenEntryParam");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			RealEstate_txtOwnerQbeRegNo2 = (String) request.get("RealEstate_txtOwnerQbeRegNo2");
			RealEstate_txtCarrierQbeRegNo2 = (String) request.get("RealEstate_txtCarrierQbeRegNo2");
			RealEstate_txtQbeAccNo2 = (String) request.get("RealEstate_txtQbeAccNo2");
			RealEstate_txtQbeCode2 = (String) request.get("RealEstate_txtQbeCode2");
			RealEstate_Qbe_ORG_UNI_ID2 = (BigDecimal) request.get("RealEstate_Qbe_ORG_UNI_ID2");
			RealEstate_QbeUSE_OPEN_ID2 = (BigDecimal) request.get("RealEstate_QbeUSE_OPEN_ID2");
			RealEstate_txtQbeDateFrom2 = (Date) request.get("RealEstate_txtQbeDateFrom2");
			RealEstate_txtQbeDateUntil2 = (Date) request.get("RealEstate_txtQbeDateUntil2");
			RealEstate_txtQbeRequestNo2 = (String) request.get("RealEstate_txtQbeRequestNo2");
		}

		public Map getResponse() {
			response.put("tblColWorkList", tblColWorkList);
			response.put("Coll_USER_LOCK", Coll_USER_LOCK);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}