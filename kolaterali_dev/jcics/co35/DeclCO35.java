package hr.vestigo.modules.collateral.jcics.co35;

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

public class DeclCO35 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co35/DeclCO35.java,v 1.4 2007/06/21 12:34:05 hratar Exp $";
	private Map response = new HashMap();

	public DeclCO35() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal COL_PRO_ID = null;

	//	out args
	public TableData tblCollRevalWorkups = new TableData();

	// inner classes
	public COLLREVALWORKUPSDETAILSMAPPING collrevalworkupsdetailsmapping = null;
	public COLLWORKUPSMAPPING collworkupsmapping = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		COL_PRO_ID = (BigDecimal) request.get("COL_PRO_ID");
	}

	public Map getResponse() {
		response.put("tblCollRevalWorkups", tblCollRevalWorkups);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollRevalWorkupsDetailsMapping")) {
			collrevalworkupsdetailsmapping = new COLLREVALWORKUPSDETAILSMAPPING(request);
			return collrevalworkupsdetailsmapping;
		} else if(mapping.equals("CollWorkupsMapping")) {
			collworkupsmapping = new COLLWORKUPSMAPPING(request);
			return collworkupsmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLREVALWORKUPSDETAILSMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal COL_PRO_ID = null;

		//	out args
		public TableData tblCollRevalWorkupsDetails = new TableData();

		public COLLREVALWORKUPSDETAILSMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			COL_PRO_ID = (BigDecimal) request.get("COL_PRO_ID");
		}

		public Map getResponse() {
			response.put("tblCollRevalWorkupsDetails", tblCollRevalWorkupsDetails);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLWORKUPSMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal col_hea_id = null;

		//	out args
		public TableData tblCollWorkups = new TableData();
		public BigDecimal CollWorkups_dynLblCollateral = null;

		public COLLWORKUPSMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			col_hea_id = (BigDecimal) request.get("col_hea_id");
		}

		public Map getResponse() {
			response.put("tblCollWorkups", tblCollWorkups);
			response.put("CollWorkups_dynLblCollateral", CollWorkups_dynLblCollateral);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}