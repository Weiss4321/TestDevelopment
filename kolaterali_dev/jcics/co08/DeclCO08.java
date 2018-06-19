package hr.vestigo.modules.collateral.jcics.co08;

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

public class DeclCO08 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co08/DeclCO08.java,v 1.10 2006/02/07 10:55:52 hrasia Exp $";
	private Map response = new HashMap();

	public DeclCO08() {
	}

	//	in args
	public BigDecimal LoanAccountList_CUS_ID = null;
	public String LoanAccountList_txtColaCusIdUseRegisterNo = null;
	public String LoanAccountList_txtColaCusIdUseOwnerName = null;
	public BigDecimal LoanAccountList_LOAN_TYPE = null;
	public String LoanAccountList_txtLoanTypeCode = null;
	public String LoanAccountList_txtLoanTypeName = null;
	public BigDecimal LoanAccountList_MODULE_LOAN = null;
	public String LoanAccountList_txtModuleLoanCode = null;
	public String LoanAccountList_txtModuleLoanName = null;
	public BigDecimal LoanAccountList_LA_ACC_ID = null;
	public String LoanAccountList_txtAccNo = null;
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblLoanAccountList = new TableData();

	// inner classes
	public LOANACCOUNTDIALOGMAPPING loanaccountdialogmapping = null;

	public void setRequest(Map request) {
		LoanAccountList_CUS_ID = (BigDecimal) request.get("LoanAccountList_CUS_ID");
		LoanAccountList_txtColaCusIdUseRegisterNo = (String) request.get("LoanAccountList_txtColaCusIdUseRegisterNo");
		LoanAccountList_txtColaCusIdUseOwnerName = (String) request.get("LoanAccountList_txtColaCusIdUseOwnerName");
		LoanAccountList_LOAN_TYPE = (BigDecimal) request.get("LoanAccountList_LOAN_TYPE");
		LoanAccountList_txtLoanTypeCode = (String) request.get("LoanAccountList_txtLoanTypeCode");
		LoanAccountList_txtLoanTypeName = (String) request.get("LoanAccountList_txtLoanTypeName");
		LoanAccountList_MODULE_LOAN = (BigDecimal) request.get("LoanAccountList_MODULE_LOAN");
		LoanAccountList_txtModuleLoanCode = (String) request.get("LoanAccountList_txtModuleLoanCode");
		LoanAccountList_txtModuleLoanName = (String) request.get("LoanAccountList_txtModuleLoanName");
		LoanAccountList_LA_ACC_ID = (BigDecimal) request.get("LoanAccountList_LA_ACC_ID");
		LoanAccountList_txtAccNo = (String) request.get("LoanAccountList_txtAccNo");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblLoanAccountList", tblLoanAccountList);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("LoanAccountDialogMapping")) {
			loanaccountdialogmapping = new LOANACCOUNTDIALOGMAPPING(request);
			return loanaccountdialogmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class LOANACCOUNTDIALOGMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal LA_ACC_ID = null;
		public BigDecimal CUS_ID = null;
		public String LoanAccountDialog_txtColaCusIdUseRegisterNo = null;

		//	out args
		public String LoanAccountDialog_txtAccNo = null;
		public BigDecimal LOAN_TYPE = null;
		public String LoanAccountDialog_txtLoanTypeCode = null;
		public String LoanAccountDialog_txtLoanTypeName = null;
		public BigDecimal MODULE_LOAN = null;
		public String LoanAccountDialog_txtModuleLoanCode = null;
		public String LoanAccountDialog_txtModuleLoanName = null;
		public String LoanAccountDialog_txtColaCusIdUseOwnerName = null;
		public BigDecimal LoanAccountDialog_txtAmount = null;
		public BigDecimal CUR_ID = null;
		public String LoanAccountDialog_txtCurIdCodeC = null;
		public BigDecimal CUR_ID_REF = null;
		public String LoanAccountDialog_txtCurIdRefCodeC = null;
		public Date LoanAccountDialog_txtStartDate = null;
		public Date LoanAccountDialog_txtPayedDate = null;
		public String LoanAccountDialog_txtYearLoGu = null;
		public String LoanAccountDialog_txtMonthLoGu = null;
		public String LoanAccountDialog_txtDayLoGu = null;
		public BigDecimal LoanAccountDialog_txtBalance = null;
		public Date LoanAccountDialog_txtBalanceDate = null;
		public BigDecimal LoanAccountDialog_txtBalanceRef = null;
		public Date LoanAccountDialog_txtBalanceDate1 = null;
		public BigDecimal LoanAccountDialog_txtExcRatRef = null;
		public Date LoanAccountDialog_txtExcRatRefDate = null;
		public BigDecimal LoanAccountDialog_txtBalanceB = null;
		public Date LoanAccountDialog_txtBalanceBDate = null;
		public BigDecimal LoanAccountDialog_txtBalanceBRef = null;
		public Date LoanAccountDialog_txtBalanceBDate1 = null;
		public BigDecimal USE_OPEN_ID = null;
		public String LoanAccountDialog_txtUseOpenIdLogin = null;
		public String LoanAccountDialog_txtUseOpenIdName = null;
		public Timestamp LoanAccountDialog_txtOpeningTs = null;
		public BigDecimal USE_ID = null;
		public String LoanAccountDialog_txtUseIdLogin = null;
		public String LoanAccountDialog_txtUseIdName = null;
		public Timestamp LoanAccountDialog_txtUserLock = null;
		public Date LoanAccountDialog_txtLaDateFrom = null;
		public Date LoanAccountDialog_txtLaDateUntil = null;

		public LOANACCOUNTDIALOGMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LA_ACC_ID = (BigDecimal) request.get("LA_ACC_ID");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			LoanAccountDialog_txtColaCusIdUseRegisterNo = (String) request.get("LoanAccountDialog_txtColaCusIdUseRegisterNo");
		}

		public Map getResponse() {
			response.put("LoanAccountDialog_txtAccNo", LoanAccountDialog_txtAccNo);
			response.put("LOAN_TYPE", LOAN_TYPE);
			response.put("LoanAccountDialog_txtLoanTypeCode", LoanAccountDialog_txtLoanTypeCode);
			response.put("LoanAccountDialog_txtLoanTypeName", LoanAccountDialog_txtLoanTypeName);
			response.put("MODULE_LOAN", MODULE_LOAN);
			response.put("LoanAccountDialog_txtModuleLoanCode", LoanAccountDialog_txtModuleLoanCode);
			response.put("LoanAccountDialog_txtModuleLoanName", LoanAccountDialog_txtModuleLoanName);
			response.put("LoanAccountDialog_txtColaCusIdUseOwnerName", LoanAccountDialog_txtColaCusIdUseOwnerName);
			response.put("LoanAccountDialog_txtAmount", LoanAccountDialog_txtAmount);
			response.put("CUR_ID", CUR_ID);
			response.put("LoanAccountDialog_txtCurIdCodeC", LoanAccountDialog_txtCurIdCodeC);
			response.put("CUR_ID_REF", CUR_ID_REF);
			response.put("LoanAccountDialog_txtCurIdRefCodeC", LoanAccountDialog_txtCurIdRefCodeC);
			response.put("LoanAccountDialog_txtStartDate", LoanAccountDialog_txtStartDate);
			response.put("LoanAccountDialog_txtPayedDate", LoanAccountDialog_txtPayedDate);
			response.put("LoanAccountDialog_txtYearLoGu", LoanAccountDialog_txtYearLoGu);
			response.put("LoanAccountDialog_txtMonthLoGu", LoanAccountDialog_txtMonthLoGu);
			response.put("LoanAccountDialog_txtDayLoGu", LoanAccountDialog_txtDayLoGu);
			response.put("LoanAccountDialog_txtBalance", LoanAccountDialog_txtBalance);
			response.put("LoanAccountDialog_txtBalanceDate", LoanAccountDialog_txtBalanceDate);
			response.put("LoanAccountDialog_txtBalanceRef", LoanAccountDialog_txtBalanceRef);
			response.put("LoanAccountDialog_txtBalanceDate1", LoanAccountDialog_txtBalanceDate1);
			response.put("LoanAccountDialog_txtExcRatRef", LoanAccountDialog_txtExcRatRef);
			response.put("LoanAccountDialog_txtExcRatRefDate", LoanAccountDialog_txtExcRatRefDate);
			response.put("LoanAccountDialog_txtBalanceB", LoanAccountDialog_txtBalanceB);
			response.put("LoanAccountDialog_txtBalanceBDate", LoanAccountDialog_txtBalanceBDate);
			response.put("LoanAccountDialog_txtBalanceBRef", LoanAccountDialog_txtBalanceBRef);
			response.put("LoanAccountDialog_txtBalanceBDate1", LoanAccountDialog_txtBalanceBDate1);
			response.put("USE_OPEN_ID", USE_OPEN_ID);
			response.put("LoanAccountDialog_txtUseOpenIdLogin", LoanAccountDialog_txtUseOpenIdLogin);
			response.put("LoanAccountDialog_txtUseOpenIdName", LoanAccountDialog_txtUseOpenIdName);
			response.put("LoanAccountDialog_txtOpeningTs", LoanAccountDialog_txtOpeningTs);
			response.put("USE_ID", USE_ID);
			response.put("LoanAccountDialog_txtUseIdLogin", LoanAccountDialog_txtUseIdLogin);
			response.put("LoanAccountDialog_txtUseIdName", LoanAccountDialog_txtUseIdName);
			response.put("LoanAccountDialog_txtUserLock", LoanAccountDialog_txtUserLock);
			response.put("LoanAccountDialog_txtLaDateFrom", LoanAccountDialog_txtLaDateFrom);
			response.put("LoanAccountDialog_txtLaDateUntil", LoanAccountDialog_txtLaDateUntil);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}