package hr.vestigo.modules.collateral.jcics.co22;

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

public class DeclCO22 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co22/DeclCO22.java,v 1.1 2006/06/10 15:07:40 hramkr Exp $";
	private Map response = new HashMap();

	public DeclCO22() {
	}

	//	in args

	//	out args
	public TableData tblColTypeList = new TableData();

	// inner classes

	public void setRequest(Map request) {
	}

	public Map getResponse() {
		response.put("tblColTypeList", tblColTypeList);

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