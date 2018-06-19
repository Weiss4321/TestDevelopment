package hr.vestigo.modules.collateral.jcics.co23;

import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO23 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co23/DeclCO23.java,v 1.2 2014/12/16 12:34:46 hrakis Exp $";
	private Map response = new HashMap();

	public DeclCO23() {
	}

	// in args
	public BigDecimal col_hea_id = null;
	public java.lang.Integer ActionListLevel = null;

	// out args
	public TableData tblColListQ = new TableData();

	// inner classes
	public COLLHISTORYSELECTMAPPING collhistoryselectmapping = null;

	public void setRequest(Map request) {
		col_hea_id = (BigDecimal) request.get("col_hea_id");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblColListQ", tblColListQ);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollHistorySelectMapping")) {
			collhistoryselectmapping = new COLLHISTORYSELECTMAPPING(request);
			return collhistoryselectmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLHISTORYSELECTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COL_HEA_ID = null;

		// out args
		public TableData tblCollHistory = new TableData();

		public COLLHISTORYSELECTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
		}

		public Map getResponse() {
			response.put("tblCollHistory", tblCollHistory);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}