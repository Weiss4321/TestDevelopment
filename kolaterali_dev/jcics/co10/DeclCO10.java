package hr.vestigo.modules.collateral.jcics.co10;

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

public class DeclCO10 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co10/DeclCO10.java,v 1.5 2006/03/28 08:27:04 hrasia Exp $";
	private Map response = new HashMap();

	public DeclCO10() {
	}

	//	in args
	public BigDecimal LoanComponentItem_laAccId = null;

	//	out args
	public BigDecimal curSumTotalFromLoanComponent = null;

	// inner classes
	public COLLCOMPONENTITEMCHECKMAPPING collcomponentitemcheckmapping = null;
	public LCIGETFBMAPPING lcigetfbmapping = null;

	public void setRequest(Map request) {
		LoanComponentItem_laAccId = (BigDecimal) request.get("LoanComponentItem_laAccId");
	}

	public Map getResponse() {
		response.put("curSumTotalFromLoanComponent", curSumTotalFromLoanComponent);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollComponentItemCheckMapping")) {
			collcomponentitemcheckmapping = new COLLCOMPONENTITEMCHECKMAPPING(request);
			return collcomponentitemcheckmapping;
		} else if(mapping.equals("LCIGetFBMapping")) {
			lcigetfbmapping = new LCIGETFBMAPPING(request);
			return lcigetfbmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLCOMPONENTITEMCHECKMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal CollComponentItem_COMP_HF_PRIOR_ID = null;

		//	out args
		public BigDecimal curSumTotalFromCollComponent = null;

		public COLLCOMPONENTITEMCHECKMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollComponentItem_COMP_HF_PRIOR_ID = (BigDecimal) request.get("CollComponentItem_COMP_HF_PRIOR_ID");
		}

		public Map getResponse() {
			response.put("curSumTotalFromCollComponent", curSumTotalFromCollComponent);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class LCIGETFBMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal LoanComponentItem_laAccId = null;

		//	out args
		public BigDecimal LoanComponentItem_txtBalance = null;
		public BigDecimal LoanComponentItem_CUR_ID = null;
		public String LoanComponentItem_txtBalanceCurIdLoanCodeC = null;
		public Date LoanComponentItem_txtBalanceDate = null;
		public BigDecimal LoanComponentItem_txtBalanceRef = null;
		public BigDecimal LoanComponentItem_CUR_ID_REF = null;
		public String LoanComponentItem_txtBalanceCurIdRefLoanCodeC = null;
		public BigDecimal LoanComponentItem_txtExcRatRef = null;
		public Date LoanComponentItem_txtExcRatRefDate = null;
		public Date LoanComponentItem_laDateFrom = null;
		public Date LoanComponentItem_laDateUntil = null;

		public LCIGETFBMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LoanComponentItem_laAccId = (BigDecimal) request.get("LoanComponentItem_laAccId");
		}

		public Map getResponse() {
			response.put("LoanComponentItem_txtBalance", LoanComponentItem_txtBalance);
			response.put("LoanComponentItem_CUR_ID", LoanComponentItem_CUR_ID);
			response.put("LoanComponentItem_txtBalanceCurIdLoanCodeC", LoanComponentItem_txtBalanceCurIdLoanCodeC);
			response.put("LoanComponentItem_txtBalanceDate", LoanComponentItem_txtBalanceDate);
			response.put("LoanComponentItem_txtBalanceRef", LoanComponentItem_txtBalanceRef);
			response.put("LoanComponentItem_CUR_ID_REF", LoanComponentItem_CUR_ID_REF);
			response.put("LoanComponentItem_txtBalanceCurIdRefLoanCodeC", LoanComponentItem_txtBalanceCurIdRefLoanCodeC);
			response.put("LoanComponentItem_txtExcRatRef", LoanComponentItem_txtExcRatRef);
			response.put("LoanComponentItem_txtExcRatRefDate", LoanComponentItem_txtExcRatRefDate);
			response.put("LoanComponentItem_laDateFrom", LoanComponentItem_laDateFrom);
			response.put("LoanComponentItem_laDateUntil", LoanComponentItem_laDateUntil);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}