package hr.vestigo.modules.collateral.jcics.co14;

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

public class DeclCO14 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co14/DeclCO14.java,v 1.3 2006/05/16 12:32:06 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCO14() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblRevRegCoefRe = new TableData();

	// inner classes
	public REVREGCOEFREQUERRYM revregcoefrequerrym = null;
	public REVREGCOEFRELOOKUPMAPPING revregcoefrelookupmapping = null;
	public REVREGCOEFREDIALOGINSERTM revregcoefredialoginsertm = null;
	public REVREGCOEFREDIALOGDELETEM revregcoefredialogdeletem = null;
	public REVREGCOEFREDIALOGUPDATEM revregcoefredialogupdatem = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblRevRegCoefRe", tblRevRegCoefRe);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("RevRegCoefReQuerryM")) {
			revregcoefrequerrym = new REVREGCOEFREQUERRYM(request);
			return revregcoefrequerrym;
		} else if(mapping.equals("RevRegCoefReLookUpMapping")) {
			revregcoefrelookupmapping = new REVREGCOEFRELOOKUPMAPPING(request);
			return revregcoefrelookupmapping;
		} else if(mapping.equals("RevRegCoefReDialogInsertM")) {
			revregcoefredialoginsertm = new REVREGCOEFREDIALOGINSERTM(request);
			return revregcoefredialoginsertm;
		} else if(mapping.equals("RevRegCoefReDialogDeleteM")) {
			revregcoefredialogdeletem = new REVREGCOEFREDIALOGDELETEM(request);
			return revregcoefredialogdeletem;
		} else if(mapping.equals("RevRegCoefReDialogUpdateM")) {
			revregcoefredialogupdatem = new REVREGCOEFREDIALOGUPDATEM(request);
			return revregcoefredialogupdatem;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class REVREGCOEFREQUERRYM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal REV_RE_PLACE = null;
		public BigDecimal REV_RE_COUNTY = null;
		public String RevRegCoefReDialogS_txtCode = null;
		public String RevRegCoefReDialogS_txtName = null;
		public java.lang.Integer ActionListLevel = null;

		//	out args
		public TableData tblRevRegCoefRe = new TableData();

		public REVREGCOEFREQUERRYM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			REV_RE_PLACE = (BigDecimal) request.get("REV_RE_PLACE");
			REV_RE_COUNTY = (BigDecimal) request.get("REV_RE_COUNTY");
			RevRegCoefReDialogS_txtCode = (String) request.get("RevRegCoefReDialogS_txtCode");
			RevRegCoefReDialogS_txtName = (String) request.get("RevRegCoefReDialogS_txtName");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		}

		public Map getResponse() {
			response.put("tblRevRegCoefRe", tblRevRegCoefRe);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class REVREGCOEFRELOOKUPMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public String rev_re_code = null;
		public String rev_re_name = null;
		public Integer LookUpLevel = null;
		public String county_name = null;
		public String place_name = null;

		//	out args
		public TableData LookUpTableData = new TableData();

		public REVREGCOEFRELOOKUPMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			rev_re_code = (String) request.get("rev_re_code");
			rev_re_name = (String) request.get("rev_re_name");
			LookUpLevel = (Integer) request.get("LookUpLevel");
			county_name = (String) request.get("county_name");
			place_name = (String) request.get("place_name");
		}

		public Map getResponse() {
			response.put("LookUpTableData", LookUpTableData);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class REVREGCOEFREDIALOGINSERTM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal REV_RE_COUNTY = null;
		public BigDecimal REV_RE_DISTRICT = null;
		public BigDecimal REV_RE_PLACE = null;
		public BigDecimal REV_RE_RESI_QUAR = null;
		public BigDecimal RevRegCoefReDialog_txtCoef = null;
		public Date RevRegCoefReDialog_txtDateFrom = null;
		public Date RevRegCoefReDialog_txtDateUntil = null;
		public String RevRegCoefReDialog_txtAct = null;
		public String RevRegCoefReDialog_txtSpecStat = null;
		public String RevRegCoefReDialog_txtCode = null;
		public String RevRegCoefReDialog_txtName = null;
		public BigDecimal USE_OPEN_ID = null;

		//	out args

		public REVREGCOEFREDIALOGINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			REV_RE_COUNTY = (BigDecimal) request.get("REV_RE_COUNTY");
			REV_RE_DISTRICT = (BigDecimal) request.get("REV_RE_DISTRICT");
			REV_RE_PLACE = (BigDecimal) request.get("REV_RE_PLACE");
			REV_RE_RESI_QUAR = (BigDecimal) request.get("REV_RE_RESI_QUAR");
			RevRegCoefReDialog_txtCoef = (BigDecimal) request.get("RevRegCoefReDialog_txtCoef");
			RevRegCoefReDialog_txtDateFrom = (Date) request.get("RevRegCoefReDialog_txtDateFrom");
			RevRegCoefReDialog_txtDateUntil = (Date) request.get("RevRegCoefReDialog_txtDateUntil");
			RevRegCoefReDialog_txtAct = (String) request.get("RevRegCoefReDialog_txtAct");
			RevRegCoefReDialog_txtSpecStat = (String) request.get("RevRegCoefReDialog_txtSpecStat");
			RevRegCoefReDialog_txtCode = (String) request.get("RevRegCoefReDialog_txtCode");
			RevRegCoefReDialog_txtName = (String) request.get("RevRegCoefReDialog_txtName");
			USE_OPEN_ID = (BigDecimal) request.get("USE_OPEN_ID");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class REVREGCOEFREDIALOGDELETEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal REV_RE_ID = null;
		public Date RevRegCoefReDialog_txtDateUntil = null;
		public String RevRegCoefReDialog_txtAct = null;
		public Timestamp RevRegCoefReDialog_UserLockNF = null;
		public BigDecimal use_id = null;
		public BigDecimal REV_RE_COUNTY_B = null;
		public BigDecimal REV_RE_DISTRICT_B = null;
		public BigDecimal REV_RE_PLACE_B = null;
		public BigDecimal REV_RE_RESI_QUAR_B = null;
		public BigDecimal RevRegCoefReDialog_txtCoefB = null;
		public Date RevRegCoefReDialog_txtDateFromB = null;
		public Date RevRegCoefReDialog_txtDateUntilB = null;
		public String RevRegCoefReDialog_txtActB = null;
		public String RevRegCoefReDialog_txtSpecStatB = null;
		public String RevRegCoefReDialog_txtCodeB = null;
		public String RevRegCoefReDialog_txtNameB = null;
		public BigDecimal USE_ID_B = null;
		public Timestamp RevRegCoefReDialog_txtOpeningTsNF = null;
		public BigDecimal EVE_ID_B = null;
		public BigDecimal USE_OPEN_ID_B = null;

		//	out args

		public REVREGCOEFREDIALOGDELETEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			REV_RE_ID = (BigDecimal) request.get("REV_RE_ID");
			RevRegCoefReDialog_txtDateUntil = (Date) request.get("RevRegCoefReDialog_txtDateUntil");
			RevRegCoefReDialog_txtAct = (String) request.get("RevRegCoefReDialog_txtAct");
			RevRegCoefReDialog_UserLockNF = (Timestamp) request.get("RevRegCoefReDialog_UserLockNF");
			use_id = (BigDecimal) request.get("use_id");
			REV_RE_COUNTY_B = (BigDecimal) request.get("REV_RE_COUNTY_B");
			REV_RE_DISTRICT_B = (BigDecimal) request.get("REV_RE_DISTRICT_B");
			REV_RE_PLACE_B = (BigDecimal) request.get("REV_RE_PLACE_B");
			REV_RE_RESI_QUAR_B = (BigDecimal) request.get("REV_RE_RESI_QUAR_B");
			RevRegCoefReDialog_txtCoefB = (BigDecimal) request.get("RevRegCoefReDialog_txtCoefB");
			RevRegCoefReDialog_txtDateFromB = (Date) request.get("RevRegCoefReDialog_txtDateFromB");
			RevRegCoefReDialog_txtDateUntilB = (Date) request.get("RevRegCoefReDialog_txtDateUntilB");
			RevRegCoefReDialog_txtActB = (String) request.get("RevRegCoefReDialog_txtActB");
			RevRegCoefReDialog_txtSpecStatB = (String) request.get("RevRegCoefReDialog_txtSpecStatB");
			RevRegCoefReDialog_txtCodeB = (String) request.get("RevRegCoefReDialog_txtCodeB");
			RevRegCoefReDialog_txtNameB = (String) request.get("RevRegCoefReDialog_txtNameB");
			USE_ID_B = (BigDecimal) request.get("USE_ID_B");
			RevRegCoefReDialog_txtOpeningTsNF = (Timestamp) request.get("RevRegCoefReDialog_txtOpeningTsNF");
			EVE_ID_B = (BigDecimal) request.get("EVE_ID_B");
			USE_OPEN_ID_B = (BigDecimal) request.get("USE_OPEN_ID_B");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class REVREGCOEFREDIALOGUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal REV_RE_COUNTY = null;
		public BigDecimal REV_RE_DISTRICT = null;
		public BigDecimal REV_RE_PLACE = null;
		public BigDecimal REV_RE_RESI_QUAR = null;
		public BigDecimal RevRegCoefReDialog_txtCoef = null;
		public Date RevRegCoefReDialog_txtDateFrom = null;
		public Date RevRegCoefReDialog_txtDateUntil = null;
		public String RevRegCoefReDialog_txtAct = null;
		public String RevRegCoefReDialog_txtSpecStat = null;
		public String RevRegCoefReDialog_txtCode = null;
		public String RevRegCoefReDialog_txtName = null;
		public BigDecimal use_id = null;
		public Timestamp RevRegCoefReDialog_txtUserLock = null;
		public BigDecimal REV_RE_ID = null;
		public Timestamp RevRegCoefReDialog_UserLockNF = null;
		public BigDecimal REV_RE_COUNTY_B = null;
		public BigDecimal REV_RE_DISTRICT_B = null;
		public BigDecimal REV_RE_PLACE_B = null;
		public BigDecimal REV_RE_RESI_QUAR_B = null;
		public BigDecimal RevRegCoefReDialog_txtCoefB = null;
		public Date RevRegCoefReDialog_txtDateFromB = null;
		public Date RevRegCoefReDialog_txtDateUntilB = null;
		public String RevRegCoefReDialog_txtActB = null;
		public String RevRegCoefReDialog_txtSpecStatB = null;
		public String RevRegCoefReDialog_txtCodeB = null;
		public String RevRegCoefReDialog_txtNameB = null;
		public BigDecimal USE_ID_B = null;
		public Timestamp RevRegCoefReDialog_txtOpeningTsNF = null;
		public BigDecimal EVE_ID_B = null;
		public BigDecimal USE_OPEN_ID_B = null;

		//	out args

		public REVREGCOEFREDIALOGUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			REV_RE_COUNTY = (BigDecimal) request.get("REV_RE_COUNTY");
			REV_RE_DISTRICT = (BigDecimal) request.get("REV_RE_DISTRICT");
			REV_RE_PLACE = (BigDecimal) request.get("REV_RE_PLACE");
			REV_RE_RESI_QUAR = (BigDecimal) request.get("REV_RE_RESI_QUAR");
			RevRegCoefReDialog_txtCoef = (BigDecimal) request.get("RevRegCoefReDialog_txtCoef");
			RevRegCoefReDialog_txtDateFrom = (Date) request.get("RevRegCoefReDialog_txtDateFrom");
			RevRegCoefReDialog_txtDateUntil = (Date) request.get("RevRegCoefReDialog_txtDateUntil");
			RevRegCoefReDialog_txtAct = (String) request.get("RevRegCoefReDialog_txtAct");
			RevRegCoefReDialog_txtSpecStat = (String) request.get("RevRegCoefReDialog_txtSpecStat");
			RevRegCoefReDialog_txtCode = (String) request.get("RevRegCoefReDialog_txtCode");
			RevRegCoefReDialog_txtName = (String) request.get("RevRegCoefReDialog_txtName");
			use_id = (BigDecimal) request.get("use_id");
			RevRegCoefReDialog_txtUserLock = (Timestamp) request.get("RevRegCoefReDialog_txtUserLock");
			REV_RE_ID = (BigDecimal) request.get("REV_RE_ID");
			RevRegCoefReDialog_UserLockNF = (Timestamp) request.get("RevRegCoefReDialog_UserLockNF");
			REV_RE_COUNTY_B = (BigDecimal) request.get("REV_RE_COUNTY_B");
			REV_RE_DISTRICT_B = (BigDecimal) request.get("REV_RE_DISTRICT_B");
			REV_RE_PLACE_B = (BigDecimal) request.get("REV_RE_PLACE_B");
			REV_RE_RESI_QUAR_B = (BigDecimal) request.get("REV_RE_RESI_QUAR_B");
			RevRegCoefReDialog_txtCoefB = (BigDecimal) request.get("RevRegCoefReDialog_txtCoefB");
			RevRegCoefReDialog_txtDateFromB = (Date) request.get("RevRegCoefReDialog_txtDateFromB");
			RevRegCoefReDialog_txtDateUntilB = (Date) request.get("RevRegCoefReDialog_txtDateUntilB");
			RevRegCoefReDialog_txtActB = (String) request.get("RevRegCoefReDialog_txtActB");
			RevRegCoefReDialog_txtSpecStatB = (String) request.get("RevRegCoefReDialog_txtSpecStatB");
			RevRegCoefReDialog_txtCodeB = (String) request.get("RevRegCoefReDialog_txtCodeB");
			RevRegCoefReDialog_txtNameB = (String) request.get("RevRegCoefReDialog_txtNameB");
			USE_ID_B = (BigDecimal) request.get("USE_ID_B");
			RevRegCoefReDialog_txtOpeningTsNF = (Timestamp) request.get("RevRegCoefReDialog_txtOpeningTsNF");
			EVE_ID_B = (BigDecimal) request.get("EVE_ID_B");
			USE_OPEN_ID_B = (BigDecimal) request.get("USE_OPEN_ID_B");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}