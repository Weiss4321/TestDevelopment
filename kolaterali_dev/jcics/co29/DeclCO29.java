package hr.vestigo.modules.collateral.jcics.co29;

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

public class DeclCO29 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co29/DeclCO29.java,v 1.2 2006/10/07 15:34:21 hramkr Exp $";
	private Map response = new HashMap();

	public DeclCO29() {
	}

	//	in args
	public Integer LookUpLevel = null;
	public BigDecimal cus_id = null;
	public String col_num = null;
	public String acc_no = null;

	//	out args
	public TableData LookUpTableData = new TableData();

	// inner classes

	public void setRequest(Map request) {
		LookUpLevel = (Integer) request.get("LookUpLevel");
		cus_id = (BigDecimal) request.get("cus_id");
		col_num = (String) request.get("col_num");
		acc_no = (String) request.get("acc_no");
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