package hr.vestigo.modules.collateral.jcics.co20;

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

public class DeclCO20 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co20/DeclCO20.java,v 1.5 2006/06/19 10:02:13 hraamh Exp $";
	private Map response = new HashMap();

	public DeclCO20() {
	}

	//	in args
	public BigDecimal RE_TYPE_ID = null;
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblReSubType = new TableData();

	// inner classes
	public RESUBTYPEINSERTM resubtypeinsertm = null;
	public RESUBTYPEUPDATEM resubtypeupdatem = null;
	public RESUBTYPEDELETEM resubtypedeletem = null;
	public RESUBTYPEQUERRYM resubtypequerrym = null;

	public void setRequest(Map request) {
		RE_TYPE_ID = (BigDecimal) request.get("RE_TYPE_ID");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblReSubType", tblReSubType);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("ReSubTypeInsertM")) {
			resubtypeinsertm = new RESUBTYPEINSERTM(request);
			return resubtypeinsertm;
		} else if(mapping.equals("ReSubTypeUpdateM")) {
			resubtypeupdatem = new RESUBTYPEUPDATEM(request);
			return resubtypeupdatem;
		} else if(mapping.equals("ReSubTypeDeleteM")) {
			resubtypedeletem = new RESUBTYPEDELETEM(request);
			return resubtypedeletem;
		} else if(mapping.equals("ReSubTypeQuerryM")) {
			resubtypequerrym = new RESUBTYPEQUERRYM(request);
			return resubtypequerrym;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class RESUBTYPEINSERTM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal RE_TYPE_ID = null;
		public BigDecimal use_id = null;
		public String STATUS = null;
		public String ReSubTypeD_txtCode = null;
		public String ReSubTypeD_txtName = null;

		//	out args

		public RESUBTYPEINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			RE_TYPE_ID = (BigDecimal) request.get("RE_TYPE_ID");
			use_id = (BigDecimal) request.get("use_id");
			STATUS = (String) request.get("STATUS");
			ReSubTypeD_txtCode = (String) request.get("ReSubTypeD_txtCode");
			ReSubTypeD_txtName = (String) request.get("ReSubTypeD_txtName");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class RESUBTYPEUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal use_id = null;
		public BigDecimal RE_SUB_TYPE_ID = null;
		public Timestamp USER_LOCK = null;
		public String ReSubTypeD_txtName = null;
		public String ReSubTypeD_txtCode = null;
		public String STATUS = null;
		public String RE_SUB_SPEC_STAT_a = null;
		public BigDecimal USE_OPEN_ID_a = null;
		public BigDecimal USE_ID_a = null;
		public Timestamp OPENING_TS_a = null;
		public Timestamp USER_LOCK_a = null;
		public BigDecimal RE_SUB_TYPE_ID_a = null;
		public String RE_SUB_TYPE_CODE_a = null;
		public String RE_SUB_TYPE_DESC_a = null;
		public BigDecimal RE_TYPE_ID_a = null;
		public String RE_SUB_STATUS_a = null;

		//	out args

		public RESUBTYPEUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			RE_SUB_TYPE_ID = (BigDecimal) request.get("RE_SUB_TYPE_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
			ReSubTypeD_txtName = (String) request.get("ReSubTypeD_txtName");
			ReSubTypeD_txtCode = (String) request.get("ReSubTypeD_txtCode");
			STATUS = (String) request.get("STATUS");
			RE_SUB_SPEC_STAT_a = (String) request.get("RE_SUB_SPEC_STAT_a");
			USE_OPEN_ID_a = (BigDecimal) request.get("USE_OPEN_ID_a");
			USE_ID_a = (BigDecimal) request.get("USE_ID_a");
			OPENING_TS_a = (Timestamp) request.get("OPENING_TS_a");
			USER_LOCK_a = (Timestamp) request.get("USER_LOCK_a");
			RE_SUB_TYPE_ID_a = (BigDecimal) request.get("RE_SUB_TYPE_ID_a");
			RE_SUB_TYPE_CODE_a = (String) request.get("RE_SUB_TYPE_CODE_a");
			RE_SUB_TYPE_DESC_a = (String) request.get("RE_SUB_TYPE_DESC_a");
			RE_TYPE_ID_a = (BigDecimal) request.get("RE_TYPE_ID_a");
			RE_SUB_STATUS_a = (String) request.get("RE_SUB_STATUS_a");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class RESUBTYPEDELETEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal use_id = null;
		public BigDecimal RE_SUB_TYPE_ID = null;
		public Timestamp USER_LOCK = null;
		public String ReSubTypeD_txtName = null;
		public String ReSubTypeD_txtCode = null;
		public String STATUS = null;
		public String RE_SUB_SPEC_STAT_a = null;
		public BigDecimal USE_OPEN_ID_a = null;
		public BigDecimal USE_ID_a = null;
		public Timestamp OPENING_TS_a = null;
		public Timestamp USER_LOCK_a = null;
		public BigDecimal RE_SUB_TYPE_ID_a = null;
		public String RE_SUB_TYPE_CODE_a = null;
		public String RE_SUB_TYPE_DESC_a = null;
		public BigDecimal RE_TYPE_ID_a = null;
		public String RE_SUB_STATUS_a = null;

		//	out args

		public RESUBTYPEDELETEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			RE_SUB_TYPE_ID = (BigDecimal) request.get("RE_SUB_TYPE_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
			ReSubTypeD_txtName = (String) request.get("ReSubTypeD_txtName");
			ReSubTypeD_txtCode = (String) request.get("ReSubTypeD_txtCode");
			STATUS = (String) request.get("STATUS");
			RE_SUB_SPEC_STAT_a = (String) request.get("RE_SUB_SPEC_STAT_a");
			USE_OPEN_ID_a = (BigDecimal) request.get("USE_OPEN_ID_a");
			USE_ID_a = (BigDecimal) request.get("USE_ID_a");
			OPENING_TS_a = (Timestamp) request.get("OPENING_TS_a");
			USER_LOCK_a = (Timestamp) request.get("USER_LOCK_a");
			RE_SUB_TYPE_ID_a = (BigDecimal) request.get("RE_SUB_TYPE_ID_a");
			RE_SUB_TYPE_CODE_a = (String) request.get("RE_SUB_TYPE_CODE_a");
			RE_SUB_TYPE_DESC_a = (String) request.get("RE_SUB_TYPE_DESC_a");
			RE_TYPE_ID_a = (BigDecimal) request.get("RE_TYPE_ID_a");
			RE_SUB_STATUS_a = (String) request.get("RE_SUB_STATUS_a");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class RESUBTYPEQUERRYM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public String ReSubTypeQuerry_txtCode = null;
		public String ReSubTypeQuerry_txtName = null;
		public BigDecimal RE_TYPE_ID = null;

		//	out args
		public TableData tblReSubType = new TableData();

		public RESUBTYPEQUERRYM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			ReSubTypeQuerry_txtCode = (String) request.get("ReSubTypeQuerry_txtCode");
			ReSubTypeQuerry_txtName = (String) request.get("ReSubTypeQuerry_txtName");
			RE_TYPE_ID = (BigDecimal) request.get("RE_TYPE_ID");
		}

		public Map getResponse() {
			response.put("tblReSubType", tblReSubType);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}