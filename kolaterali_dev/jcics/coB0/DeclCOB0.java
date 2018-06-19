package hr.vestigo.modules.collateral.jcics.coB0;

import java.sql.Timestamp;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOB0 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB0/DeclCOB0.java,v 1.2 2017/11/29 09:46:18 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCOB0() {
	}

	// in args
	public BigDecimal EST_CUS_ID = null;
	public BigDecimal EST_COMP_CUS_ID = null;
	public String CollEstimatorDialog_txtEstimatorTypeCode = null;
	public BigDecimal use_id = null;
	public String CollEstimatorDialog_txtEstimatorRegNo = null;
	public String CollEstimatorDialog_txtEstCompRegNo = null;
	public Date CollEstimatorDialog_txtDateFrom = null;
	public Date CollEstimatorDialog_txtDateUntil = null;
	public Date ProcessingDate = null;
	public String CollEstimatorDialog_txtEstiminatorInternal = null;

	// out args

	// inner classes
	public COLLESTIMATORDIALOGDETAILSMAPPING collestimatordialogdetailsmapping = null;

	public void setRequest(Map request) {
		EST_CUS_ID = (BigDecimal) request.get("EST_CUS_ID");
		EST_COMP_CUS_ID = (BigDecimal) request.get("EST_COMP_CUS_ID");
		CollEstimatorDialog_txtEstimatorTypeCode = (String) request.get("CollEstimatorDialog_txtEstimatorTypeCode");
		use_id = (BigDecimal) request.get("use_id");
		CollEstimatorDialog_txtEstimatorRegNo = (String) request.get("CollEstimatorDialog_txtEstimatorRegNo");
		CollEstimatorDialog_txtEstCompRegNo = (String) request.get("CollEstimatorDialog_txtEstCompRegNo");
		CollEstimatorDialog_txtDateFrom = (Date) request.get("CollEstimatorDialog_txtDateFrom");
		CollEstimatorDialog_txtDateUntil = (Date) request.get("CollEstimatorDialog_txtDateUntil");
		ProcessingDate = (Date) request.get("ProcessingDate");
		CollEstimatorDialog_txtEstiminatorInternal = (String) request.get("CollEstimatorDialog_txtEstiminatorInternal");
	}

	public Map getResponse() {

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollEstimatorDialogDetailsMapping")) {
			collestimatordialogdetailsmapping = new COLLESTIMATORDIALOGDETAILSMAPPING(request);
			return collestimatordialogdetailsmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLESTIMATORDIALOGDETAILSMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal EST_ID = null;

		// out args
		public BigDecimal EST_CUS_ID = null;
		public BigDecimal EST_COMP_CUS_ID = null;
		public String CollEstimatorDialog_txtEstimatorTypeCode = null;
		public Date CollEstimatorDialog_txtDateFrom = null;
		public Date CollEstimatorDialog_txtDateUntil = null;
		public String CollEstimatorDialog_txtEstimatorRegNo = null;
		public String CollEstimatorDialog_txtEstimatorName = null;
		public String CollEstimatorDialog_txtEstCompRegNo = null;
		public String CollEstimatorDialog_txtEstCompName = null;
		public String CollEstimatorDialog_txtEstimatorTypeName = null;
		public String CollEstimatorDialog_txtUseRegNo = null;
		public String CollEstimatorDialog_txtUseName = null;
		public String CollEstimatorDialog_txtChgUseRegNo = null;
		public String CollEstimatorDialog_txtChgUseName = null;
		public Timestamp CollEstimatorDialog_txtOpeningTS = null;
		public Timestamp CollEstimatorDialog_txtUserLock = null;
		public String CollEstimatorDialog_txtEstiminatorInternal = null;

		public COLLESTIMATORDIALOGDETAILSMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			EST_ID = (BigDecimal) request.get("EST_ID");
		}

		public Map getResponse() {
			response.put("EST_CUS_ID", EST_CUS_ID);
			response.put("EST_COMP_CUS_ID", EST_COMP_CUS_ID);
			response.put("CollEstimatorDialog_txtEstimatorTypeCode", CollEstimatorDialog_txtEstimatorTypeCode);
			response.put("CollEstimatorDialog_txtDateFrom", CollEstimatorDialog_txtDateFrom);
			response.put("CollEstimatorDialog_txtDateUntil", CollEstimatorDialog_txtDateUntil);
			response.put("CollEstimatorDialog_txtEstimatorRegNo", CollEstimatorDialog_txtEstimatorRegNo);
			response.put("CollEstimatorDialog_txtEstimatorName", CollEstimatorDialog_txtEstimatorName);
			response.put("CollEstimatorDialog_txtEstCompRegNo", CollEstimatorDialog_txtEstCompRegNo);
			response.put("CollEstimatorDialog_txtEstCompName", CollEstimatorDialog_txtEstCompName);
			response.put("CollEstimatorDialog_txtEstimatorTypeName", CollEstimatorDialog_txtEstimatorTypeName);
			response.put("CollEstimatorDialog_txtUseRegNo", CollEstimatorDialog_txtUseRegNo);
			response.put("CollEstimatorDialog_txtUseName", CollEstimatorDialog_txtUseName);
			response.put("CollEstimatorDialog_txtChgUseRegNo", CollEstimatorDialog_txtChgUseRegNo);
			response.put("CollEstimatorDialog_txtChgUseName", CollEstimatorDialog_txtChgUseName);
			response.put("CollEstimatorDialog_txtOpeningTS", CollEstimatorDialog_txtOpeningTS);
			response.put("CollEstimatorDialog_txtUserLock", CollEstimatorDialog_txtUserLock);
			response.put("CollEstimatorDialog_txtEstiminatorInternal", CollEstimatorDialog_txtEstiminatorInternal);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}