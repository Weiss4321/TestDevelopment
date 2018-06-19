package hr.vestigo.modules.collateral.jcics.co37;

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

public class DeclCO37 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co37/DeclCO37.java,v 1.1 2007/09/06 14:50:24 hraamh Exp $";
	private Map response = new HashMap();

	public DeclCO37() {
	}

	//	in args
	public String RCLRcrdID_in = null;

	//	out args
	public String RCLRcrdID_out = null;
	public TableData tblAPSCollateralData = new TableData();

	// inner classes

	public void setRequest(Map request) {
		RCLRcrdID_in = (String) request.get("RCLRcrdID_in");
	}

	public Map getResponse() {
		response.put("RCLRcrdID_out", RCLRcrdID_out);
		response.put("tblAPSCollateralData", tblAPSCollateralData);

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