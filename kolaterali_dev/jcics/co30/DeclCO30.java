package hr.vestigo.modules.collateral.jcics.co30;

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

public class DeclCO30 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co30/DeclCO30.java,v 1.1 2006/10/06 07:11:12 hrasia Exp $";
	private Map response = new HashMap();

	public DeclCO30() {
	}

	//	in args
	public BigDecimal COL_HEA_ID = null;

	//	out args

	// inner classes

	public void setRequest(Map request) {
		COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
	}

	public Map getResponse() {

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