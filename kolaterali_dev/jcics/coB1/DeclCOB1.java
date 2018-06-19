package hr.vestigo.modules.collateral.jcics.coB1;

import java.sql.Date;
import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOB1 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB1/DeclCOB1.java,v 1.2 2017/11/29 09:46:19 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCOB1() {
	}

	// in args
	public String ScreenEntryParam = null;
	public java.lang.Integer ActionListLevel = null;

	// out args
	public TableData tblCollEstimatorList = new TableData();

	// inner classes
	public COLLESTIMATORQBEMAPPING collestimatorqbemapping = null;
	public COLLESTIMATORCLOSEMAPPING collestimatorclosemapping = null;

	public void setRequest(Map request) {
		ScreenEntryParam = (String) request.get("ScreenEntryParam");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblCollEstimatorList", tblCollEstimatorList);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollEstimatorQBEMapping")) {
			collestimatorqbemapping = new COLLESTIMATORQBEMAPPING(request);
			return collestimatorqbemapping;
		} else if(mapping.equals("CollEstimatorCloseMapping")) {
			collestimatorclosemapping = new COLLESTIMATORCLOSEMAPPING(request);
			return collestimatorclosemapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLESTIMATORQBEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal use_id = null;
		public String ScreenEntryParam = null;
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal EST_CUS_ID = null;
		public Date CollEstimatorQBE_txtEstimatorDateFrom = null;
		public Date CollEstimatorQBE_txtEstimatorDateUntil = null;

		// out args
		public TableData tblCollEstimatorList = new TableData();

		public COLLESTIMATORQBEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			ScreenEntryParam = (String) request.get("ScreenEntryParam");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			EST_CUS_ID = (BigDecimal) request.get("EST_CUS_ID");
			CollEstimatorQBE_txtEstimatorDateFrom = (Date) request.get("CollEstimatorQBE_txtEstimatorDateFrom");
			CollEstimatorQBE_txtEstimatorDateUntil = (Date) request.get("CollEstimatorQBE_txtEstimatorDateUntil");
		}

		public Map getResponse() {
			response.put("tblCollEstimatorList", tblCollEstimatorList);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLESTIMATORCLOSEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal EST_ID = null;

		// out args
		public TableData tblCollEstimatorList = new TableData();

		public COLLESTIMATORCLOSEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			EST_ID = (BigDecimal) request.get("EST_ID");
		}

		public Map getResponse() {
			response.put("tblCollEstimatorList", tblCollEstimatorList);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}