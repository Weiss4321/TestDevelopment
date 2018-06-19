package hr.vestigo.modules.collateral.jcics.co24;

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

public class DeclCO24 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co24/DeclCO24.java,v 1.5 2006/07/12 09:02:24 hraaks Exp $";
	private Map response = new HashMap();

	public DeclCO24() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblCollCourt = new TableData();

	// inner classes
	public COLLCOURTINSERTM collcourtinsertm = null;
	public COLLCOURTQUERRYM collcourtquerrym = null;
	public COLLCOURTUPDATEM collcourtupdatem = null;
	public COLLCOURTDELETEM collcourtdeletem = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblCollCourt", tblCollCourt);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollCourtInsertM")) {
			collcourtinsertm = new COLLCOURTINSERTM(request);
			return collcourtinsertm;
		} else if(mapping.equals("CollCourtQuerryM")) {
			collcourtquerrym = new COLLCOURTQUERRYM(request);
			return collcourtquerrym;
		} else if(mapping.equals("CollCourtUpdateM")) {
			collcourtupdatem = new COLLCOURTUPDATEM(request);
			return collcourtupdatem;
		} else if(mapping.equals("CollCourtDeleteM")) {
			collcourtdeletem = new COLLCOURTDELETEM(request);
			return collcourtdeletem;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLCOURTINSERTM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal CollCourtDialog_POL_MAP_ID = null;
		public String CollCourtDialog_txtCodeCnt = null;
		public String CollCourtDialog_txtNameCnt = null;
		public BigDecimal CollCourtDialog_CO_ID = null;
		public String CollCourtDialog_txtCoCode = null;
		public String CollCourtDialog_txtCoName = null;
		public String CollCourtDialog_txtCoStatus = null;
		public BigDecimal use_id = null;

		//	out args

		public COLLCOURTINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollCourtDialog_POL_MAP_ID = (BigDecimal) request.get("CollCourtDialog_POL_MAP_ID");
			CollCourtDialog_txtCodeCnt = (String) request.get("CollCourtDialog_txtCodeCnt");
			CollCourtDialog_txtNameCnt = (String) request.get("CollCourtDialog_txtNameCnt");
			CollCourtDialog_CO_ID = (BigDecimal) request.get("CollCourtDialog_CO_ID");
			CollCourtDialog_txtCoCode = (String) request.get("CollCourtDialog_txtCoCode");
			CollCourtDialog_txtCoName = (String) request.get("CollCourtDialog_txtCoName");
			CollCourtDialog_txtCoStatus = (String) request.get("CollCourtDialog_txtCoStatus");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCOURTQUERRYM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public String CollCourtDialog_txtCoCode = null;
		public String CollCourtDialog_txtCoName = null;
		public java.lang.Integer ActionListLevel = null;

		//	out args
		public TableData tblCollCourt = new TableData();

		public COLLCOURTQUERRYM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollCourtDialog_txtCoCode = (String) request.get("CollCourtDialog_txtCoCode");
			CollCourtDialog_txtCoName = (String) request.get("CollCourtDialog_txtCoName");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		}

		public Map getResponse() {
			response.put("tblCollCourt", tblCollCourt);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCOURTUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal CollCourtDialog_POL_MAP_ID = null;
		public String CollCourtDialog_txtCodeCnt = null;
		public String CollCourtDialog_txtNameCnt = null;
		public BigDecimal CollCourtDialog_CO_ID = null;
		public String CollCourtDialog_txtCoCode = null;
		public String CollCourtDialog_txtCoName = null;
		public BigDecimal use_id = null;
		public String CollCourtDialog_txtCoStatus = null;
		public BigDecimal CollCourtDialog_POL_MAP_ID_a = null;
		public BigDecimal CollCourtDialog_CO_ID_a = null;
		public String CollCourtDialog_txCoCode_a = null;
		public String CollCourtDialog_txCoName_a = null;
		public String CollCourtDialog_txtCoStatus_a = null;
		public BigDecimal CollCourtDialog_USE_OPEN_ID_a = null;
		public BigDecimal CollCourtDialog_USE_ID_a = null;
		public Timestamp CollCourtDialog_USER_LOCK_a = null;
		public Timestamp CollCourtDialog_OPENING_TS_a = null;
		public Timestamp CollCourtDialog_USER_LOCK = null;

		//	out args

		public COLLCOURTUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollCourtDialog_POL_MAP_ID = (BigDecimal) request.get("CollCourtDialog_POL_MAP_ID");
			CollCourtDialog_txtCodeCnt = (String) request.get("CollCourtDialog_txtCodeCnt");
			CollCourtDialog_txtNameCnt = (String) request.get("CollCourtDialog_txtNameCnt");
			CollCourtDialog_CO_ID = (BigDecimal) request.get("CollCourtDialog_CO_ID");
			CollCourtDialog_txtCoCode = (String) request.get("CollCourtDialog_txtCoCode");
			CollCourtDialog_txtCoName = (String) request.get("CollCourtDialog_txtCoName");
			use_id = (BigDecimal) request.get("use_id");
			CollCourtDialog_txtCoStatus = (String) request.get("CollCourtDialog_txtCoStatus");
			CollCourtDialog_POL_MAP_ID_a = (BigDecimal) request.get("CollCourtDialog_POL_MAP_ID_a");
			CollCourtDialog_CO_ID_a = (BigDecimal) request.get("CollCourtDialog_CO_ID_a");
			CollCourtDialog_txCoCode_a = (String) request.get("CollCourtDialog_txCoCode_a");
			CollCourtDialog_txCoName_a = (String) request.get("CollCourtDialog_txCoName_a");
			CollCourtDialog_txtCoStatus_a = (String) request.get("CollCourtDialog_txtCoStatus_a");
			CollCourtDialog_USE_OPEN_ID_a = (BigDecimal) request.get("CollCourtDialog_USE_OPEN_ID_a");
			CollCourtDialog_USE_ID_a = (BigDecimal) request.get("CollCourtDialog_USE_ID_a");
			CollCourtDialog_USER_LOCK_a = (Timestamp) request.get("CollCourtDialog_USER_LOCK_a");
			CollCourtDialog_OPENING_TS_a = (Timestamp) request.get("CollCourtDialog_OPENING_TS_a");
			CollCourtDialog_USER_LOCK = (Timestamp) request.get("CollCourtDialog_USER_LOCK");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCOURTDELETEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal CollCourtDialog_POL_MAP_ID = null;
		public String CollCourtDialog_txtCodeCnt = null;
		public String CollCourtDialog_txtNameCnt = null;
		public BigDecimal CollCourtDialog_CO_ID = null;
		public String CollCourtDialog_txtCoCode = null;
		public String CollCourtDialog_txtCoName = null;
		public BigDecimal use_id = null;
		public String CollCourtDialog_txtCoStatus = null;
		public BigDecimal CollCourtDialog_POL_MAP_ID_a = null;
		public BigDecimal CollCourtDialog_CO_ID_a = null;
		public String CollCourtDialog_txCoCode_a = null;
		public String CollCourtDialog_txCoName_a = null;
		public String CollCourtDialog_txtCoStatus_a = null;
		public BigDecimal CollCourtDialog_USE_OPEN_ID_a = null;
		public BigDecimal CollCourtDialog_USE_ID_a = null;
		public Timestamp CollCourtDialog_USER_LOCK_a = null;
		public Timestamp CollCourtDialog_OPENING_TS_a = null;
		public Timestamp CollCourtDialog_USER_LOCK = null;

		//	out args

		public COLLCOURTDELETEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollCourtDialog_POL_MAP_ID = (BigDecimal) request.get("CollCourtDialog_POL_MAP_ID");
			CollCourtDialog_txtCodeCnt = (String) request.get("CollCourtDialog_txtCodeCnt");
			CollCourtDialog_txtNameCnt = (String) request.get("CollCourtDialog_txtNameCnt");
			CollCourtDialog_CO_ID = (BigDecimal) request.get("CollCourtDialog_CO_ID");
			CollCourtDialog_txtCoCode = (String) request.get("CollCourtDialog_txtCoCode");
			CollCourtDialog_txtCoName = (String) request.get("CollCourtDialog_txtCoName");
			use_id = (BigDecimal) request.get("use_id");
			CollCourtDialog_txtCoStatus = (String) request.get("CollCourtDialog_txtCoStatus");
			CollCourtDialog_POL_MAP_ID_a = (BigDecimal) request.get("CollCourtDialog_POL_MAP_ID_a");
			CollCourtDialog_CO_ID_a = (BigDecimal) request.get("CollCourtDialog_CO_ID_a");
			CollCourtDialog_txCoCode_a = (String) request.get("CollCourtDialog_txCoCode_a");
			CollCourtDialog_txCoName_a = (String) request.get("CollCourtDialog_txCoName_a");
			CollCourtDialog_txtCoStatus_a = (String) request.get("CollCourtDialog_txtCoStatus_a");
			CollCourtDialog_USE_OPEN_ID_a = (BigDecimal) request.get("CollCourtDialog_USE_OPEN_ID_a");
			CollCourtDialog_USE_ID_a = (BigDecimal) request.get("CollCourtDialog_USE_ID_a");
			CollCourtDialog_USER_LOCK_a = (Timestamp) request.get("CollCourtDialog_USER_LOCK_a");
			CollCourtDialog_OPENING_TS_a = (Timestamp) request.get("CollCourtDialog_OPENING_TS_a");
			CollCourtDialog_USER_LOCK = (Timestamp) request.get("CollCourtDialog_USER_LOCK");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}