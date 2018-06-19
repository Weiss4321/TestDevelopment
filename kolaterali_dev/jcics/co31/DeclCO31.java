package hr.vestigo.modules.collateral.jcics.co31;

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

public class DeclCO31 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co31/DeclCO31.java,v 1.3 2006/11/08 12:37:31 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCO31() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblCadastreMap = new TableData();

	// inner classes
	public CADASTREMAPQBEM cadastremapqbem = null;
	public CADASTREMAPDIALOGUPDATEM cadastremapdialogupdatem = null;
	public CADASTREMAPDIALOGINSERTM cadastremapdialoginsertm = null;
	public CADASTREMAPDIALOGDELETEM cadastremapdialogdeletem = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblCadastreMap", tblCadastreMap);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CadastreMapQBEM")) {
			cadastremapqbem = new CADASTREMAPQBEM(request);
			return cadastremapqbem;
		} else if(mapping.equals("CadastreMapDialogUpdateM")) {
			cadastremapdialogupdatem = new CADASTREMAPDIALOGUPDATEM(request);
			return cadastremapdialogupdatem;
		} else if(mapping.equals("CadastreMapDialogInsertM")) {
			cadastremapdialoginsertm = new CADASTREMAPDIALOGINSERTM(request);
			return cadastremapdialoginsertm;
		} else if(mapping.equals("CadastreMapDialogDeleteM")) {
			cadastremapdialogdeletem = new CADASTREMAPDIALOGDELETEM(request);
			return cadastremapdialogdeletem;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class CADASTREMAPQBEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public String CadastreMapQBE_txtCode = null;
		public String CadastreMapQBE_txtName = null;
		public BigDecimal POL_MAP_ID = null;
		public BigDecimal CO_ID = null;

		//	out args
		public TableData tblCadastreMap = new TableData();

		public CADASTREMAPQBEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CadastreMapQBE_txtCode = (String) request.get("CadastreMapQBE_txtCode");
			CadastreMapQBE_txtName = (String) request.get("CadastreMapQBE_txtName");
			POL_MAP_ID = (BigDecimal) request.get("POL_MAP_ID");
			CO_ID = (BigDecimal) request.get("CO_ID");
		}

		public Map getResponse() {
			response.put("tblCadastreMap", tblCadastreMap);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CADASTREMAPDIALOGUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public String CadastreMapDialog_txtCode = null;
		public String CadastreMapDialog_txtName = null;
		public String CODE_CAD_REG = null;
		public BigDecimal PARENT_CAD_MAP_ID = null;
		public BigDecimal CAD_MAP_TYP_ID = null;
		public BigDecimal POL_MAP_ID = null;
		public BigDecimal CO_ID = null;
		public BigDecimal COU_ID = null;
		public Timestamp USER_LOCK = null;
		public BigDecimal CAD_MAP_ID = null;
		public BigDecimal use_id = null;

		//	out args

		public CADASTREMAPDIALOGUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CadastreMapDialog_txtCode = (String) request.get("CadastreMapDialog_txtCode");
			CadastreMapDialog_txtName = (String) request.get("CadastreMapDialog_txtName");
			CODE_CAD_REG = (String) request.get("CODE_CAD_REG");
			PARENT_CAD_MAP_ID = (BigDecimal) request.get("PARENT_CAD_MAP_ID");
			CAD_MAP_TYP_ID = (BigDecimal) request.get("CAD_MAP_TYP_ID");
			POL_MAP_ID = (BigDecimal) request.get("POL_MAP_ID");
			CO_ID = (BigDecimal) request.get("CO_ID");
			COU_ID = (BigDecimal) request.get("COU_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
			CAD_MAP_ID = (BigDecimal) request.get("CAD_MAP_ID");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CADASTREMAPDIALOGINSERTM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public String CadastreMapDialog_txtCode = null;
		public String CadastreMapDialog_txtName = null;
		public String CODE_CAD_REG = null;
		public BigDecimal PARENT_CAD_MAP_ID = null;
		public BigDecimal CAD_MAP_TYP_ID = null;
		public BigDecimal POL_MAP_ID = null;
		public BigDecimal CO_ID = null;
		public BigDecimal COU_ID = null;
		public BigDecimal use_id = null;

		//	out args

		public CADASTREMAPDIALOGINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CadastreMapDialog_txtCode = (String) request.get("CadastreMapDialog_txtCode");
			CadastreMapDialog_txtName = (String) request.get("CadastreMapDialog_txtName");
			CODE_CAD_REG = (String) request.get("CODE_CAD_REG");
			PARENT_CAD_MAP_ID = (BigDecimal) request.get("PARENT_CAD_MAP_ID");
			CAD_MAP_TYP_ID = (BigDecimal) request.get("CAD_MAP_TYP_ID");
			POL_MAP_ID = (BigDecimal) request.get("POL_MAP_ID");
			CO_ID = (BigDecimal) request.get("CO_ID");
			COU_ID = (BigDecimal) request.get("COU_ID");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CADASTREMAPDIALOGDELETEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public Timestamp USER_LOCK = null;
		public BigDecimal use_id = null;
		public BigDecimal CAD_MAP_ID = null;

		//	out args

		public CADASTREMAPDIALOGDELETEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
			use_id = (BigDecimal) request.get("use_id");
			CAD_MAP_ID = (BigDecimal) request.get("CAD_MAP_ID");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}