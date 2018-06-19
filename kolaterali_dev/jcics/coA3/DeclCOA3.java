package hr.vestigo.modules.collateral.jcics.coA3;

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

public class DeclCOA3 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA3/DeclCOA3.java,v 1.1 2009/09/25 11:33:00 hrakis Exp $";
	private Map response = new HashMap();

	public DeclCOA3() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblCollPonder = new TableData();

	// inner classes
	public COLLPONDERINSERTM collponderinsertm = null;
	public COLLPONDERQUERYM collponderquerym = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblCollPonder", tblCollPonder);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollPonderInsertM")) {
			collponderinsertm = new COLLPONDERINSERTM(request);
			return collponderinsertm;
		} else if(mapping.equals("CollPonderQueryM")) {
			collponderquerym = new COLLPONDERQUERYM(request);
			return collponderquerym;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLPONDERINSERTM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal COL_CAT_ID = null;
		public BigDecimal COL_TYP_ID = null;
		public BigDecimal COL_SUB_ID = null;
		public BigDecimal org_uni_id = null;
		public BigDecimal use_id = null;
		public String CollPonderDialog_txtAddRequest = null;
		public String CollPonderDialog_txtPonderType = null;
		public BigDecimal CollPonderDialog_txtPonderMin = null;
		public BigDecimal CollPonderDialog_txtPonderDefault = null;
		public BigDecimal CollPonderDialog_txtPonderMax = null;

		//	out args

		public COLLPONDERINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COL_CAT_ID = (BigDecimal) request.get("COL_CAT_ID");
			COL_TYP_ID = (BigDecimal) request.get("COL_TYP_ID");
			COL_SUB_ID = (BigDecimal) request.get("COL_SUB_ID");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			use_id = (BigDecimal) request.get("use_id");
			CollPonderDialog_txtAddRequest = (String) request.get("CollPonderDialog_txtAddRequest");
			CollPonderDialog_txtPonderType = (String) request.get("CollPonderDialog_txtPonderType");
			CollPonderDialog_txtPonderMin = (BigDecimal) request.get("CollPonderDialog_txtPonderMin");
			CollPonderDialog_txtPonderDefault = (BigDecimal) request.get("CollPonderDialog_txtPonderDefault");
			CollPonderDialog_txtPonderMax = (BigDecimal) request.get("CollPonderDialog_txtPonderMax");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLPONDERQUERYM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal COL_CAT_ID = null;
		public BigDecimal COL_TYP_ID = null;
		public BigDecimal COL_SUB_ID = null;
		public String CollPonderQBE_txtPonderType = null;
		public String CollPonderQBE_txtStatus = null;
		public Date CollPonderQBE_txtDateFrom = null;
		public Date CollPonderQBE_txtDateUntil = null;

		//	out args
		public TableData tblCollPonder = new TableData();

		public COLLPONDERQUERYM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			COL_CAT_ID = (BigDecimal) request.get("COL_CAT_ID");
			COL_TYP_ID = (BigDecimal) request.get("COL_TYP_ID");
			COL_SUB_ID = (BigDecimal) request.get("COL_SUB_ID");
			CollPonderQBE_txtPonderType = (String) request.get("CollPonderQBE_txtPonderType");
			CollPonderQBE_txtStatus = (String) request.get("CollPonderQBE_txtStatus");
			CollPonderQBE_txtDateFrom = (Date) request.get("CollPonderQBE_txtDateFrom");
			CollPonderQBE_txtDateUntil = (Date) request.get("CollPonderQBE_txtDateUntil");
		}

		public Map getResponse() {
			response.put("tblCollPonder", tblCollPonder);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}