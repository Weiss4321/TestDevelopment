package hr.vestigo.modules.collateral.jcics.co26;

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

public class DeclCO26 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co26/DeclCO26.java,v 1.2 2006/09/21 13:33:51 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCO26() {
	}

	//	in args
	public String ColControlListing_txtAccNo = null;
	public Date ColControlListing_txtDateFrom = null;
	public Date ColControlListing_txtDateUntil = null;
	public String ColControlListing_txtNoRequest = null;
	public BigDecimal REF_ID = null;
	public BigDecimal OWNER_ID = null;
	public String COLL_STATUS = null;
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblColControlListing = new TableData();   

	// inner classes

	public void setRequest(Map request) {
		ColControlListing_txtAccNo = (String) request.get("ColControlListing_txtAccNo");
		ColControlListing_txtDateFrom = (Date) request.get("ColControlListing_txtDateFrom");
		ColControlListing_txtDateUntil = (Date) request.get("ColControlListing_txtDateUntil");
		ColControlListing_txtNoRequest = (String) request.get("ColControlListing_txtNoRequest");
		REF_ID = (BigDecimal) request.get("REF_ID");
		OWNER_ID = (BigDecimal) request.get("OWNER_ID");
		COLL_STATUS = (String) request.get("COLL_STATUS");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblColControlListing", tblColControlListing);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		setRequest(request);
		return this;
	}

	// INNER CLASSES
}