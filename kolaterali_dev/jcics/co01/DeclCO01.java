package hr.vestigo.modules.collateral.jcics.co01;

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

public class DeclCO01 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co01/DeclCO01.java,v 1.4 2005/12/30 10:25:42 hrasia Exp $";
	private Map response = new HashMap();

	public DeclCO01() {
	}

	//	in args
	public String rev_re_code = null;
	public String rev_re_name = null;
	public Integer LookUpLevel = null;
	public String county_name = null;
	public String place_name = null;

	//	out args
	public TableData LookUpTableData = new TableData();

	// inner classes

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

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		setRequest(request);
		return this;
	}

	// INNER CLASSES
}