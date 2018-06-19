package hr.vestigo.modules.collateral.jcics.co36;

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

public class DeclCO36 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co36/DeclCO36.java,v 1.2 2007/06/12 14:17:29 hratar Exp $";
	private Map response = new HashMap();

	public DeclCO36() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal COL_PRO_ID = null;

	//	out args
	public TableData tblCollAmortWorkups = new TableData();

	// inner classes
	public COLLAMORTWORKUPSDETAILSMAPPING collamortworkupsdetailsmapping = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		COL_PRO_ID = (BigDecimal) request.get("COL_PRO_ID");
	}

	public Map getResponse() {
		response.put("tblCollAmortWorkups", tblCollAmortWorkups);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollAmortWorkupsDetailsMapping")) {
			collamortworkupsdetailsmapping = new COLLAMORTWORKUPSDETAILSMAPPING(request);
			return collamortworkupsdetailsmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLAMORTWORKUPSDETAILSMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal COL_PRO_ID = null;

		//	out args
		public TableData tblCollAmortWorkupsDetails = new TableData();

		public COLLAMORTWORKUPSDETAILSMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			COL_PRO_ID = (BigDecimal) request.get("COL_PRO_ID");
		}

		public Map getResponse() {
			response.put("tblCollAmortWorkupsDetails", tblCollAmortWorkupsDetails);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}