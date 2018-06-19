package hr.vestigo.modules.collateral.jcics.co07;

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

public class DeclCO07 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co07/DeclCO07.java,v 1.5 2006/02/22 14:35:29 hrasia Exp $";
	private Map response = new HashMap();

	public DeclCO07() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal CUS_ID = null;
	public String Collateral_txtColaCusIdUseRegisterNo = null;
	public String Collateral_txtColaCusIdUseOwnerName = null;
	public BigDecimal COL_TYPE_ID = null;
	public String Collateral_txtCollTypeCode = null;
	public String Collateral_txtCollTypeName = null;
	public BigDecimal COL_HEA_ID = null;
	public String Collateral_txtColNum = null;
	public BigDecimal LOAN_TYPE = null;
	public String Collateral_txtLoanTypeCode = null;
	public String Collateral_txtLoanTypeName = null;
	public BigDecimal LA_ACC_ID = null;
	public String Collateral_txtAccNo = null;
	public BigDecimal MODULE_LOAN = null;
	public String Collateral_txtModuleLoanCode = null;
	public String Collateral_txtModuleLoanName = null;

	//	out args
	public TableData tblLoanRelColl = new TableData();

	// inner classes
	public COLLATERALDIALOGINSERTMAPPING collateraldialoginsertmapping = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		CUS_ID = (BigDecimal) request.get("CUS_ID");
		Collateral_txtColaCusIdUseRegisterNo = (String) request.get("Collateral_txtColaCusIdUseRegisterNo");
		Collateral_txtColaCusIdUseOwnerName = (String) request.get("Collateral_txtColaCusIdUseOwnerName");
		COL_TYPE_ID = (BigDecimal) request.get("COL_TYPE_ID");
		Collateral_txtCollTypeCode = (String) request.get("Collateral_txtCollTypeCode");
		Collateral_txtCollTypeName = (String) request.get("Collateral_txtCollTypeName");
		COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
		Collateral_txtColNum = (String) request.get("Collateral_txtColNum");
		LOAN_TYPE = (BigDecimal) request.get("LOAN_TYPE");
		Collateral_txtLoanTypeCode = (String) request.get("Collateral_txtLoanTypeCode");
		Collateral_txtLoanTypeName = (String) request.get("Collateral_txtLoanTypeName");
		LA_ACC_ID = (BigDecimal) request.get("LA_ACC_ID");
		Collateral_txtAccNo = (String) request.get("Collateral_txtAccNo");
		MODULE_LOAN = (BigDecimal) request.get("MODULE_LOAN");
		Collateral_txtModuleLoanCode = (String) request.get("Collateral_txtModuleLoanCode");
		Collateral_txtModuleLoanName = (String) request.get("Collateral_txtModuleLoanName");
	}

	public Map getResponse() {
		response.put("tblLoanRelColl", tblLoanRelColl);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollateralDialogInsertMapping")) {
			collateraldialoginsertmapping = new COLLATERALDIALOGINSERTMAPPING(request);
			return collateraldialoginsertmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLATERALDIALOGINSERTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public TableData tblCollateralDialogLoanComp = new TableData();
		public TableData tblCollateralDialogCollComp = new TableData();
		public BigDecimal CUS_ID = null;
		public BigDecimal use_id = null;

		//	out args

		public COLLATERALDIALOGINSERTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			tblCollateralDialogLoanComp = (TableData) request.get("tblCollateralDialogLoanComp");
			tblCollateralDialogCollComp = (TableData) request.get("tblCollateralDialogCollComp");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}