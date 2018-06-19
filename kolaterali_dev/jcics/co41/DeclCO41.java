package hr.vestigo.modules.collateral.jcics.co41;

import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO41 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co41/DeclCO41.java,v 1.3 2014/05/05 09:00:35 hraaks Exp $";
	private Map response = new HashMap();

	public DeclCO41() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal RealEstate_COL_HEA_ID = null;

	// out args
	public TableData tblCOHistoryViewList = new TableData();

	// inner classes
	public COLLINVESTPARTIESALLQUERRYDATE collinvestpartiesallquerrydate = null;
	public COLLCESREPORTQUERYDATE collcesreportquerydate = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		RealEstate_COL_HEA_ID = (BigDecimal) request.get("RealEstate_COL_HEA_ID");
	}

	public Map getResponse() {
		response.put("tblCOHistoryViewList", tblCOHistoryViewList);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollInvestPartiesAllQuerryDate")) {
			collinvestpartiesallquerrydate = new COLLINVESTPARTIESALLQUERRYDATE(request);
			return collinvestpartiesallquerrydate;
		} else if(mapping.equals("CollCesReportQueryDate")) {
			collcesreportquerydate = new COLLCESREPORTQUERYDATE(request);
			return collcesreportquerydate;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLINVESTPARTIESALLQUERRYDATE implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public Date CollInvestPartiesAll_ReportDate = null;

		// out args
		public String flag = null;

		public COLLINVESTPARTIESALLQUERRYDATE(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollInvestPartiesAll_ReportDate = (Date) request.get("CollInvestPartiesAll_ReportDate");
		}

		public Map getResponse() {
			response.put("flag", flag);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCESREPORTQUERYDATE implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public Date txtDate = null;

		// out args
		public String flag = null;

		public COLLCESREPORTQUERYDATE(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			txtDate = (Date) request.get("txtDate");
		}

		public Map getResponse() {
			response.put("flag", flag);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}