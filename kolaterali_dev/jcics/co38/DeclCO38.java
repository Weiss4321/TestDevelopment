package hr.vestigo.modules.collateral.jcics.co38;

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

public class DeclCO38 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co38/DeclCO38.java,v 1.1 2007/10/12 08:35:15 hratar Exp $";
	private Map response = new HashMap();

	public DeclCO38() {
	}

	//	in args
	public Integer LookUpLevel = null;

	//	out args
	public TableData LookUpTableData = new TableData();

	// inner classes

	public void setRequest(Map request) {
		LookUpLevel = (Integer) request.get("LookUpLevel");
	}

	public Map getResponse() {
		response.put("LookUpTableData", LookUpTableData);

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