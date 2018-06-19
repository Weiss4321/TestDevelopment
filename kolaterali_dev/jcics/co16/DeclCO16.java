package hr.vestigo.modules.collateral.jcics.co16;

import java.sql.Timestamp;
import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO16 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co16/DeclCO16.java,v 1.25 2014/12/15 08:39:25 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCO16() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal LBen_CUS_ID = null;
	public String LBen_txtAccNo = null;
	public BigDecimal LBen_COLL_HF_PRIOR_ID = null;
	public BigDecimal LBen_COLL_HEA_ID = null;
	public String LBen_txtRequestNo = null;
	public BigDecimal LBen_fra_agr_id = null;

	// out args
	public TableData tblLoanBeneficiary = new TableData();

	// inner classes
	public LOANBENEFICIARYINSERTMAPPING loanbeneficiaryinsertmapping = null;
	public LOANBENEFICIARYDETAILMAPPING loanbeneficiarydetailmapping = null;
	public LOANBENEFICIARYUPDMAPPING loanbeneficiaryupdmapping = null;
	public LOANBENEFICIARYDELM loanbeneficiarydelm = null;
	public LOANBENEFICIARYNEWLISTMAPP loanbeneficiarynewlistmapp = null;
	public LBENKREDADMINMAPPING lbenkredadminmapping = null;
	public LBENFRAMEACCEXCEPTIONCHECKM lbenframeaccexceptioncheckm = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		LBen_CUS_ID = (BigDecimal) request.get("LBen_CUS_ID");
		LBen_txtAccNo = (String) request.get("LBen_txtAccNo");
		LBen_COLL_HF_PRIOR_ID = (BigDecimal) request.get("LBen_COLL_HF_PRIOR_ID");
		LBen_COLL_HEA_ID = (BigDecimal) request.get("LBen_COLL_HEA_ID");
		LBen_txtRequestNo = (String) request.get("LBen_txtRequestNo");
		LBen_fra_agr_id = (BigDecimal) request.get("LBen_fra_agr_id");
	}

	public Map getResponse() {
		response.put("tblLoanBeneficiary", tblLoanBeneficiary);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("LoanBeneficiaryInsertMapping")) {
			loanbeneficiaryinsertmapping = new LOANBENEFICIARYINSERTMAPPING(request);
			return loanbeneficiaryinsertmapping;
		} else if(mapping.equals("LoanBeneficiaryDetailMapping")) {
			loanbeneficiarydetailmapping = new LOANBENEFICIARYDETAILMAPPING(request);
			return loanbeneficiarydetailmapping;
		} else if(mapping.equals("LoanBeneficiaryUpdMapping")) {
			loanbeneficiaryupdmapping = new LOANBENEFICIARYUPDMAPPING(request);
			return loanbeneficiaryupdmapping;
		} else if(mapping.equals("LoanBeneficiaryDelM")) {
			loanbeneficiarydelm = new LOANBENEFICIARYDELM(request);
			return loanbeneficiarydelm;
		} else if(mapping.equals("LoanBeneficiaryNewListMapp")) {
			loanbeneficiarynewlistmapp = new LOANBENEFICIARYNEWLISTMAPP(request);
			return loanbeneficiarynewlistmapp;
		} else if(mapping.equals("LBenKredAdminMapping")) {
			lbenkredadminmapping = new LBENKREDADMINMAPPING(request);
			return lbenkredadminmapping;
		} else if(mapping.equals("LBenFrameAccExceptionCheckM")) {
			lbenframeaccexceptioncheckm = new LBENFRAMEACCEXCEPTIONCHECKM(request);
			return lbenframeaccexceptioncheckm;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class LOANBENEFICIARYINSERTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal LBenDialog_LOAN_BEN_ID = null;
		public String LBenDialog_txtRegNo = null;
		public BigDecimal LBenDialog_CUS_ID = null;
		public String LBenDialog_txtAccNo = null;
		public BigDecimal LBenDialog_LA_ACC_ID = null;
		public BigDecimal LBenDialog_COLL_HF_PRIOR_ID = null;
		public BigDecimal LBenDialog_COLL_HEA_ID = null;
		public Integer LBenDialog_txtSerNum = null;
		public Date LBenDialog_txtDateFrom = null;
		public Date LBenDialog_txtDateUntil = null;
		public BigDecimal LBenDialog_USE_OPEN_ID = null;
		public BigDecimal LBenDialog_USE_ID = null;
		public Timestamp LBenDialog_txtOpeningTS = null;
		public Timestamp LBenDialog_txtUserLock = null;
		public String LBenDialog_txtName = null;
		public String LBenDialog_txtCode = null;
		public String LBenDialog_txtFName = null;
		public String LBenDialog_txtSurName = null;
		public String LBenDialog_txtUserLogin = null;
		public String LBenDialog_txtUserName = null;
		public String LBenDialog_txtUserOpenLogin = null;
		public String LBenDialog_txtUserOpenName = null;
		public String LBenDialog_txtRequestNo = null;
		public String LBenDialog_txtPriorityNo = null;
		public BigDecimal LBenDialog_fra_agr_id = null;
		public String LBenDialog_txtApsRqstNo = null;
		public BigDecimal LBen_col_hea_id_check = null;
		public String LBenDialog_Insu = null;
		public BigDecimal LBenDialog_IP_CUS_ID = null;
		public BigDecimal RealEstate_COL_HEA_ID = null;
		public BigDecimal RealEstate_REAL_EST_NM_CUR_ID = null;
		public BigDecimal RealEstate_txtNomiValu = null;
		public BigDecimal RealEstate_txtCollMvpPonder = null;
		public String LBenDialog_txtContractNo = null;

		// out args
		public BigDecimal RealEstate_txtWeighValue = null;
		public Date RealEstate_txtWeighDate = null;
		public BigDecimal RealEstate_txtAvailValue = null;
		public Date RealEstate_txtAvailDate = null;
		public BigDecimal RealEstate_txtSumPartVal = null;
		public Date RealEstate_txtSumPartDat = null;
		public String Kol_ND_lb = null;
		public String Kol_ND_lb_dsc = null;
		public String Kol_B2_lb = null;
		public String Kol_B2_lb_dsc = null;
		public String Kol_HNB_lb = null;
		public String Kol_HNB_lb_dsc = null;
		public String Kol_B2IRB_lb = null;
		public String Kol_B2IRB_lb_dsc = null;

		public LOANBENEFICIARYINSERTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LBenDialog_LOAN_BEN_ID = (BigDecimal) request.get("LBenDialog_LOAN_BEN_ID");
			LBenDialog_txtRegNo = (String) request.get("LBenDialog_txtRegNo");
			LBenDialog_CUS_ID = (BigDecimal) request.get("LBenDialog_CUS_ID");
			LBenDialog_txtAccNo = (String) request.get("LBenDialog_txtAccNo");
			LBenDialog_LA_ACC_ID = (BigDecimal) request.get("LBenDialog_LA_ACC_ID");
			LBenDialog_COLL_HF_PRIOR_ID = (BigDecimal) request.get("LBenDialog_COLL_HF_PRIOR_ID");
			LBenDialog_COLL_HEA_ID = (BigDecimal) request.get("LBenDialog_COLL_HEA_ID");
			LBenDialog_txtSerNum = (Integer) request.get("LBenDialog_txtSerNum");
			LBenDialog_txtDateFrom = (Date) request.get("LBenDialog_txtDateFrom");
			LBenDialog_txtDateUntil = (Date) request.get("LBenDialog_txtDateUntil");
			LBenDialog_USE_OPEN_ID = (BigDecimal) request.get("LBenDialog_USE_OPEN_ID");
			LBenDialog_USE_ID = (BigDecimal) request.get("LBenDialog_USE_ID");
			LBenDialog_txtOpeningTS = (Timestamp) request.get("LBenDialog_txtOpeningTS");
			LBenDialog_txtUserLock = (Timestamp) request.get("LBenDialog_txtUserLock");
			LBenDialog_txtName = (String) request.get("LBenDialog_txtName");
			LBenDialog_txtCode = (String) request.get("LBenDialog_txtCode");
			LBenDialog_txtFName = (String) request.get("LBenDialog_txtFName");
			LBenDialog_txtSurName = (String) request.get("LBenDialog_txtSurName");
			LBenDialog_txtUserLogin = (String) request.get("LBenDialog_txtUserLogin");
			LBenDialog_txtUserName = (String) request.get("LBenDialog_txtUserName");
			LBenDialog_txtUserOpenLogin = (String) request.get("LBenDialog_txtUserOpenLogin");
			LBenDialog_txtUserOpenName = (String) request.get("LBenDialog_txtUserOpenName");
			LBenDialog_txtRequestNo = (String) request.get("LBenDialog_txtRequestNo");
			LBenDialog_txtPriorityNo = (String) request.get("LBenDialog_txtPriorityNo");
			LBenDialog_fra_agr_id = (BigDecimal) request.get("LBenDialog_fra_agr_id");
			LBenDialog_txtApsRqstNo = (String) request.get("LBenDialog_txtApsRqstNo");
			LBen_col_hea_id_check = (BigDecimal) request.get("LBen_col_hea_id_check");
			LBenDialog_Insu = (String) request.get("LBenDialog_Insu");
			LBenDialog_IP_CUS_ID = (BigDecimal) request.get("LBenDialog_IP_CUS_ID");
			RealEstate_COL_HEA_ID = (BigDecimal) request.get("RealEstate_COL_HEA_ID");
			RealEstate_REAL_EST_NM_CUR_ID = (BigDecimal) request.get("RealEstate_REAL_EST_NM_CUR_ID");
			RealEstate_txtNomiValu = (BigDecimal) request.get("RealEstate_txtNomiValu");
			RealEstate_txtCollMvpPonder = (BigDecimal) request.get("RealEstate_txtCollMvpPonder");
			LBenDialog_txtContractNo = (String) request.get("LBenDialog_txtContractNo");
		}

		public Map getResponse() {
			response.put("RealEstate_txtWeighValue", RealEstate_txtWeighValue);
			response.put("RealEstate_txtWeighDate", RealEstate_txtWeighDate);
			response.put("RealEstate_txtAvailValue", RealEstate_txtAvailValue);
			response.put("RealEstate_txtAvailDate", RealEstate_txtAvailDate);
			response.put("RealEstate_txtSumPartVal", RealEstate_txtSumPartVal);
			response.put("RealEstate_txtSumPartDat", RealEstate_txtSumPartDat);
			response.put("Kol_ND_lb", Kol_ND_lb);
			response.put("Kol_ND_lb_dsc", Kol_ND_lb_dsc);
			response.put("Kol_B2_lb", Kol_B2_lb);
			response.put("Kol_B2_lb_dsc", Kol_B2_lb_dsc);
			response.put("Kol_HNB_lb", Kol_HNB_lb);
			response.put("Kol_HNB_lb_dsc", Kol_HNB_lb_dsc);
			response.put("Kol_B2IRB_lb", Kol_B2IRB_lb);
			response.put("Kol_B2IRB_lb_dsc", Kol_B2IRB_lb_dsc);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class LOANBENEFICIARYDETAILMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal LBenDialog_LOAN_BEN_ID = null;
		public BigDecimal laAccId = null;
		public BigDecimal l_fra_agr_id = null;
		public String laAccNo = null;
		public String laRequestNo = null;
		public String laContractNo = null;

		// out args
		public String LBenDialog_txtRegNo = null;
		public BigDecimal LBenDialog_CUS_ID = null;
		public String LBenDialog_txtAccNo = null;
		public BigDecimal LBenDialog_LA_ACC_ID = null;
		public BigDecimal LBenDialog_COLL_HF_PRIOR_ID = null;
		public BigDecimal LBenDialog_COLL_HEA_ID = null;
		public Integer LBenDialog_txtSerNum = null;
		public Date LBenDialog_txtDateFrom = null;
		public Date LBenDialog_txtDateUntil = null;
		public BigDecimal LBenDialog_USE_OPEN_ID = null;
		public BigDecimal LBenDialog_USE_ID = null;
		public Timestamp LBenDialog_txtOpeningTS = null;
		public Timestamp LBenDialog_txtUserLock = null;
		public String LBenDialog_txtName = null;
		public String LBenDialog_txtCode = null;
		public String LBenDialog_txtFName = null;
		public String LBenDialog_txtSurName = null;
		public String LBenDialog_txtUserLogin = null;
		public String LBenDialog_txtUserName = null;
		public String LBenDialog_txtUserOpenLogin = null;
		public String LBenDialog_txtUserOpenName = null;
		public String LBenDialog_txtRegNo_O = null;
		public BigDecimal LBenDialog_CUS_ID_O = null;
		public String LBenDialog_txtAccNo_O = null;
		public BigDecimal LBenDialog_LA_ACC_ID_O = null;
		public BigDecimal LBenDialog_COLL_HF_PRIOR_ID_O = null;
		public BigDecimal LBenDialog_COLL_HEA_ID_O = null;
		public Integer LBenDialog_txtSerNum_O = null;
		public Date LBenDialog_txtDateFrom_O = null;
		public Date LBenDialog_txtDateUntil_O = null;
		public BigDecimal LBenDialog_USE_OPEN_ID_O = null;
		public BigDecimal LBenDialog_USE_ID_O = null;
		public Timestamp LBenDialog_txtOpeningTS_O = null;
		public Timestamp LBenDialog_txtUserLock_O = null;
		public String LBenDialog_txtName_O = null;
		public String LBenDialog_txtCode_O = null;
		public String LBenDialog_txtFName_O = null;
		public String LBenDialog_txtSurName_O = null;
		public String LBenDialog_txtUserLogin_O = null;
		public String LBenDialog_txtUserName_O = null;
		public String LBenDialog_txtUserOpenLogin_O = null;
		public String LBenDialog_txtUserOpenName_O = null;
		public String LBenDialog_txtStatus = null;
		public String LBenDialog_txtSpecStatus = null;
		public String LBenDialog_txtStatus_O = null;
		public String LBenDialog_txtSpecStatus_O = null;
		public String LBenDialog_txtRequestNo = null;
		public String LBenDialog_txtPriorityNo = null;
		public String LBenDialog_txtRequestNo_O = null;
		public String LBenDialog_txtPriorityNo_O = null;
		public BigDecimal LBenDialog_fra_agr_id = null;
		public BigDecimal LBenDialog_fra_agr_id_O = null;
		public String LBenDialog_txtApsRqstNo = null;
		public String LBenDialog_txtApsRqstNo_O = null;
		public String LBenDialog_Insu = null;
		public BigDecimal LBenDialog_IP_CUS_ID = null;
		public String LBenDialog_txtInsPolRegNo = null;
		public String LBenDialog_txtInsPolName = null;
		public String LBenDialog_Insu_O = null;
		public BigDecimal LBenDialog_IP_CUS_ID_O = null;
		public String LBenDialog_txtKredAdmin = null;
		public BigDecimal LBenDialog_KredAdminUSE_ID = null;
		public Timestamp LBenDialog_txtKredAdminTime = null;
		public String LBenDialog_txtKredAdminUserId = null;
		public String LBenDialog_txtKredAdminUserName = null;
		public String LBenDialog_txtKredAdmin_O = null;
		public BigDecimal LBenDialog_KredAdminUSE_ID_O = null;
		public Timestamp LBenDialog_txtKredAdminTime_O = null;
		public String LBenDialog_txtContractNo = null;
		public String LBenDialog_txtContractNo_O = null;
		public String LBenDialog_txtOIB = null;
		public String LBenDialog_txtInsPolOIB = null;
		public BigDecimal LBenDialog_txtExposure = null;
		public BigDecimal LBenDialog_txtExposureExpOffBalLcy = null;
		public String LBenDialog_txtDefault = null;
		public Date LBenDialog_txtDafaultDate = null;
		public Date LBenDialog_txtDueDate = null;
		public Date LBenDialog_txtUseDate = null;
		public Date LBenDialog_txtExposureDate = null;

		public LOANBENEFICIARYDETAILMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LBenDialog_LOAN_BEN_ID = (BigDecimal) request.get("LBenDialog_LOAN_BEN_ID");
			laAccId = (BigDecimal) request.get("laAccId");
			l_fra_agr_id = (BigDecimal) request.get("l_fra_agr_id");
			laAccNo = (String) request.get("laAccNo");
			laRequestNo = (String) request.get("laRequestNo");
			laContractNo = (String) request.get("laContractNo");
		}

		public Map getResponse() {
			response.put("LBenDialog_txtRegNo", LBenDialog_txtRegNo);
			response.put("LBenDialog_CUS_ID", LBenDialog_CUS_ID);
			response.put("LBenDialog_txtAccNo", LBenDialog_txtAccNo);
			response.put("LBenDialog_LA_ACC_ID", LBenDialog_LA_ACC_ID);
			response.put("LBenDialog_COLL_HF_PRIOR_ID", LBenDialog_COLL_HF_PRIOR_ID);
			response.put("LBenDialog_COLL_HEA_ID", LBenDialog_COLL_HEA_ID);
			response.put("LBenDialog_txtSerNum", LBenDialog_txtSerNum);
			response.put("LBenDialog_txtDateFrom", LBenDialog_txtDateFrom);
			response.put("LBenDialog_txtDateUntil", LBenDialog_txtDateUntil);
			response.put("LBenDialog_USE_OPEN_ID", LBenDialog_USE_OPEN_ID);
			response.put("LBenDialog_USE_ID", LBenDialog_USE_ID);
			response.put("LBenDialog_txtOpeningTS", LBenDialog_txtOpeningTS);
			response.put("LBenDialog_txtUserLock", LBenDialog_txtUserLock);
			response.put("LBenDialog_txtName", LBenDialog_txtName);
			response.put("LBenDialog_txtCode", LBenDialog_txtCode);
			response.put("LBenDialog_txtFName", LBenDialog_txtFName);
			response.put("LBenDialog_txtSurName", LBenDialog_txtSurName);
			response.put("LBenDialog_txtUserLogin", LBenDialog_txtUserLogin);
			response.put("LBenDialog_txtUserName", LBenDialog_txtUserName);
			response.put("LBenDialog_txtUserOpenLogin", LBenDialog_txtUserOpenLogin);
			response.put("LBenDialog_txtUserOpenName", LBenDialog_txtUserOpenName);
			response.put("LBenDialog_txtRegNo_O", LBenDialog_txtRegNo_O);
			response.put("LBenDialog_CUS_ID_O", LBenDialog_CUS_ID_O);
			response.put("LBenDialog_txtAccNo_O", LBenDialog_txtAccNo_O);
			response.put("LBenDialog_LA_ACC_ID_O", LBenDialog_LA_ACC_ID_O);
			response.put("LBenDialog_COLL_HF_PRIOR_ID_O", LBenDialog_COLL_HF_PRIOR_ID_O);
			response.put("LBenDialog_COLL_HEA_ID_O", LBenDialog_COLL_HEA_ID_O);
			response.put("LBenDialog_txtSerNum_O", LBenDialog_txtSerNum_O);
			response.put("LBenDialog_txtDateFrom_O", LBenDialog_txtDateFrom_O);
			response.put("LBenDialog_txtDateUntil_O", LBenDialog_txtDateUntil_O);
			response.put("LBenDialog_USE_OPEN_ID_O", LBenDialog_USE_OPEN_ID_O);
			response.put("LBenDialog_USE_ID_O", LBenDialog_USE_ID_O);
			response.put("LBenDialog_txtOpeningTS_O", LBenDialog_txtOpeningTS_O);
			response.put("LBenDialog_txtUserLock_O", LBenDialog_txtUserLock_O);
			response.put("LBenDialog_txtName_O", LBenDialog_txtName_O);
			response.put("LBenDialog_txtCode_O", LBenDialog_txtCode_O);
			response.put("LBenDialog_txtFName_O", LBenDialog_txtFName_O);
			response.put("LBenDialog_txtSurName_O", LBenDialog_txtSurName_O);
			response.put("LBenDialog_txtUserLogin_O", LBenDialog_txtUserLogin_O);
			response.put("LBenDialog_txtUserName_O", LBenDialog_txtUserName_O);
			response.put("LBenDialog_txtUserOpenLogin_O", LBenDialog_txtUserOpenLogin_O);
			response.put("LBenDialog_txtUserOpenName_O", LBenDialog_txtUserOpenName_O);
			response.put("LBenDialog_txtStatus", LBenDialog_txtStatus);
			response.put("LBenDialog_txtSpecStatus", LBenDialog_txtSpecStatus);
			response.put("LBenDialog_txtStatus_O", LBenDialog_txtStatus_O);
			response.put("LBenDialog_txtSpecStatus_O", LBenDialog_txtSpecStatus_O);
			response.put("LBenDialog_txtRequestNo", LBenDialog_txtRequestNo);
			response.put("LBenDialog_txtPriorityNo", LBenDialog_txtPriorityNo);
			response.put("LBenDialog_txtRequestNo_O", LBenDialog_txtRequestNo_O);
			response.put("LBenDialog_txtPriorityNo_O", LBenDialog_txtPriorityNo_O);
			response.put("LBenDialog_fra_agr_id", LBenDialog_fra_agr_id);
			response.put("LBenDialog_fra_agr_id_O", LBenDialog_fra_agr_id_O);
			response.put("LBenDialog_txtApsRqstNo", LBenDialog_txtApsRqstNo);
			response.put("LBenDialog_txtApsRqstNo_O", LBenDialog_txtApsRqstNo_O);
			response.put("LBenDialog_Insu", LBenDialog_Insu);
			response.put("LBenDialog_IP_CUS_ID", LBenDialog_IP_CUS_ID);
			response.put("LBenDialog_txtInsPolRegNo", LBenDialog_txtInsPolRegNo);
			response.put("LBenDialog_txtInsPolName", LBenDialog_txtInsPolName);
			response.put("LBenDialog_Insu_O", LBenDialog_Insu_O);
			response.put("LBenDialog_IP_CUS_ID_O", LBenDialog_IP_CUS_ID_O);
			response.put("LBenDialog_txtKredAdmin", LBenDialog_txtKredAdmin);
			response.put("LBenDialog_KredAdminUSE_ID", LBenDialog_KredAdminUSE_ID);
			response.put("LBenDialog_txtKredAdminTime", LBenDialog_txtKredAdminTime);
			response.put("LBenDialog_txtKredAdminUserId", LBenDialog_txtKredAdminUserId);
			response.put("LBenDialog_txtKredAdminUserName", LBenDialog_txtKredAdminUserName);
			response.put("LBenDialog_txtKredAdmin_O", LBenDialog_txtKredAdmin_O);
			response.put("LBenDialog_KredAdminUSE_ID_O", LBenDialog_KredAdminUSE_ID_O);
			response.put("LBenDialog_txtKredAdminTime_O", LBenDialog_txtKredAdminTime_O);
			response.put("LBenDialog_txtContractNo", LBenDialog_txtContractNo);
			response.put("LBenDialog_txtContractNo_O", LBenDialog_txtContractNo_O);
			response.put("LBenDialog_txtOIB", LBenDialog_txtOIB);
			response.put("LBenDialog_txtInsPolOIB", LBenDialog_txtInsPolOIB);
			response.put("LBenDialog_txtExposure", LBenDialog_txtExposure);
			response.put("LBenDialog_txtExposureExpOffBalLcy", LBenDialog_txtExposureExpOffBalLcy);
			response.put("LBenDialog_txtDefault", LBenDialog_txtDefault);
			response.put("LBenDialog_txtDafaultDate", LBenDialog_txtDafaultDate);
			response.put("LBenDialog_txtDueDate", LBenDialog_txtDueDate);
			response.put("LBenDialog_txtUseDate", LBenDialog_txtUseDate);
			response.put("LBenDialog_txtExposureDate", LBenDialog_txtExposureDate);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class LOANBENEFICIARYUPDMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal LBenDialog_LOAN_BEN_ID = null;
		public String LBenDialog_txtRegNo = null;
		public BigDecimal LBenDialog_CUS_ID = null;
		public String LBenDialog_txtAccNo = null;
		public BigDecimal LBenDialog_LA_ACC_ID = null;
		public BigDecimal LBenDialog_COLL_HF_PRIOR_ID = null;
		public BigDecimal LBenDialog_COLL_HEA_ID = null;
		public Integer LBenDialog_txtSerNum = null;
		public Date LBenDialog_txtDateFrom = null;
		public Date LBenDialog_txtDateUntil = null;
		public BigDecimal LBenDialog_USE_OPEN_ID = null;
		public BigDecimal LBenDialog_USE_ID = null;
		public Timestamp LBenDialog_txtOpeningTS = null;
		public Timestamp LBenDialog_txtUserLock = null;
		public String LBenDialog_txtName = null;
		public String LBenDialog_txtCode = null;
		public String LBenDialog_txtFName = null;
		public String LBenDialog_txtSurName = null;
		public String LBenDialog_txtUserLogin = null;
		public String LBenDialog_txtUserName = null;
		public String LBenDialog_txtUserOpenLogin = null;
		public String LBenDialog_txtUserOpenName = null;
		public String LBenDialog_txtRegNo_O = null;
		public BigDecimal LBenDialog_CUS_ID_O = null;
		public String LBenDialog_txtAccNo_O = null;
		public BigDecimal LBenDialog_LA_ACC_ID_O = null;
		public BigDecimal LBenDialog_COLL_HF_PRIOR_ID_O = null;
		public BigDecimal LBenDialog_COLL_HEA_ID_O = null;
		public Integer LBenDialog_txtSerNum_O = null;
		public Date LBenDialog_txtDateFrom_O = null;
		public Date LBenDialog_txtDateUntil_O = null;
		public BigDecimal LBenDialog_USE_OPEN_ID_O = null;
		public BigDecimal LBenDialog_USE_ID_O = null;
		public Timestamp LBenDialog_txtOpeningTS_O = null;
		public Timestamp LBenDialog_txtUserLock_O = null;
		public String LBenDialog_txtName_O = null;
		public String LBenDialog_txtCode_O = null;
		public String LBenDialog_txtFName_O = null;
		public String LBenDialog_txtSurName_O = null;
		public String LBenDialog_txtUserLogin_O = null;
		public String LBenDialog_txtUserName_O = null;
		public String LBenDialog_txtUserOpenLogin_O = null;
		public String LBenDialog_txtUserOpenName_O = null;
		public String LBenDialog_txtStatus = null;
		public String LBenDialog_txtSpecStatus = null;
		public String LBenDialog_txtStatus_O = null;
		public String LBenDialog_txtSpecStatus_O = null;
		public String LBenDialog_txtRequestNo = null;
		public String LBenDialog_txtPriorityNo = null;
		public String LBenDialog_txtRequestNo_O = null;
		public String LBenDialog_txtPriorityNo_O = null;
		public BigDecimal LBenDialog_fra_agr_id = null;
		public BigDecimal LBenDialog_fra_agr_id_O = null;
		public BigDecimal laAccId = null;
		public BigDecimal l_fra_agr_id = null;
		public String LBenDialog_txtApsRqstNo = null;
		public String LBenDialog_txtApsRqstNo_O = null;
		public BigDecimal LBen_col_hea_id_check = null;
		public String LBenDialog_Insu = null;
		public BigDecimal LBenDialog_IP_CUS_ID = null;
		public String LBenDialog_Insu_O = null;
		public BigDecimal LBenDialog_IP_CUS_ID_O = null;
		public BigDecimal RealEstate_COL_HEA_ID = null;
		public BigDecimal RealEstate_REAL_EST_NM_CUR_ID = null;
		public BigDecimal RealEstate_txtNomiValu = null;
		public BigDecimal RealEstate_txtCollMvpPonder = null;
		public String LBenDialog_txtContractNo = null;
		public String LBenDialog_txtContractNo_O = null;

		// out args
		public BigDecimal RealEstate_txtWeighValue = null;
		public Date RealEstate_txtWeighDate = null;
		public BigDecimal RealEstate_txtAvailValue = null;
		public Date RealEstate_txtAvailDate = null;
		public BigDecimal RealEstate_txtSumPartVal = null;
		public Date RealEstate_txtSumPartDat = null;
		public String Kol_ND_lb = null;
		public String Kol_ND_lb_dsc = null;
		public String Kol_B2_lb = null;
		public String Kol_B2_lb_dsc = null;
		public String Kol_HNB_lb = null;
		public String Kol_HNB_lb_dsc = null;
		public String Kol_B2IRB_lb = null;
		public String Kol_B2IRB_lb_dsc = null;

		public LOANBENEFICIARYUPDMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LBenDialog_LOAN_BEN_ID = (BigDecimal) request.get("LBenDialog_LOAN_BEN_ID");
			LBenDialog_txtRegNo = (String) request.get("LBenDialog_txtRegNo");
			LBenDialog_CUS_ID = (BigDecimal) request.get("LBenDialog_CUS_ID");
			LBenDialog_txtAccNo = (String) request.get("LBenDialog_txtAccNo");
			LBenDialog_LA_ACC_ID = (BigDecimal) request.get("LBenDialog_LA_ACC_ID");
			LBenDialog_COLL_HF_PRIOR_ID = (BigDecimal) request.get("LBenDialog_COLL_HF_PRIOR_ID");
			LBenDialog_COLL_HEA_ID = (BigDecimal) request.get("LBenDialog_COLL_HEA_ID");
			LBenDialog_txtSerNum = (Integer) request.get("LBenDialog_txtSerNum");
			LBenDialog_txtDateFrom = (Date) request.get("LBenDialog_txtDateFrom");
			LBenDialog_txtDateUntil = (Date) request.get("LBenDialog_txtDateUntil");
			LBenDialog_USE_OPEN_ID = (BigDecimal) request.get("LBenDialog_USE_OPEN_ID");
			LBenDialog_USE_ID = (BigDecimal) request.get("LBenDialog_USE_ID");
			LBenDialog_txtOpeningTS = (Timestamp) request.get("LBenDialog_txtOpeningTS");
			LBenDialog_txtUserLock = (Timestamp) request.get("LBenDialog_txtUserLock");
			LBenDialog_txtName = (String) request.get("LBenDialog_txtName");
			LBenDialog_txtCode = (String) request.get("LBenDialog_txtCode");
			LBenDialog_txtFName = (String) request.get("LBenDialog_txtFName");
			LBenDialog_txtSurName = (String) request.get("LBenDialog_txtSurName");
			LBenDialog_txtUserLogin = (String) request.get("LBenDialog_txtUserLogin");
			LBenDialog_txtUserName = (String) request.get("LBenDialog_txtUserName");
			LBenDialog_txtUserOpenLogin = (String) request.get("LBenDialog_txtUserOpenLogin");
			LBenDialog_txtUserOpenName = (String) request.get("LBenDialog_txtUserOpenName");
			LBenDialog_txtRegNo_O = (String) request.get("LBenDialog_txtRegNo_O");
			LBenDialog_CUS_ID_O = (BigDecimal) request.get("LBenDialog_CUS_ID_O");
			LBenDialog_txtAccNo_O = (String) request.get("LBenDialog_txtAccNo_O");
			LBenDialog_LA_ACC_ID_O = (BigDecimal) request.get("LBenDialog_LA_ACC_ID_O");
			LBenDialog_COLL_HF_PRIOR_ID_O = (BigDecimal) request.get("LBenDialog_COLL_HF_PRIOR_ID_O");
			LBenDialog_COLL_HEA_ID_O = (BigDecimal) request.get("LBenDialog_COLL_HEA_ID_O");
			LBenDialog_txtSerNum_O = (Integer) request.get("LBenDialog_txtSerNum_O");
			LBenDialog_txtDateFrom_O = (Date) request.get("LBenDialog_txtDateFrom_O");
			LBenDialog_txtDateUntil_O = (Date) request.get("LBenDialog_txtDateUntil_O");
			LBenDialog_USE_OPEN_ID_O = (BigDecimal) request.get("LBenDialog_USE_OPEN_ID_O");
			LBenDialog_USE_ID_O = (BigDecimal) request.get("LBenDialog_USE_ID_O");
			LBenDialog_txtOpeningTS_O = (Timestamp) request.get("LBenDialog_txtOpeningTS_O");
			LBenDialog_txtUserLock_O = (Timestamp) request.get("LBenDialog_txtUserLock_O");
			LBenDialog_txtName_O = (String) request.get("LBenDialog_txtName_O");
			LBenDialog_txtCode_O = (String) request.get("LBenDialog_txtCode_O");
			LBenDialog_txtFName_O = (String) request.get("LBenDialog_txtFName_O");
			LBenDialog_txtSurName_O = (String) request.get("LBenDialog_txtSurName_O");
			LBenDialog_txtUserLogin_O = (String) request.get("LBenDialog_txtUserLogin_O");
			LBenDialog_txtUserName_O = (String) request.get("LBenDialog_txtUserName_O");
			LBenDialog_txtUserOpenLogin_O = (String) request.get("LBenDialog_txtUserOpenLogin_O");
			LBenDialog_txtUserOpenName_O = (String) request.get("LBenDialog_txtUserOpenName_O");
			LBenDialog_txtStatus = (String) request.get("LBenDialog_txtStatus");
			LBenDialog_txtSpecStatus = (String) request.get("LBenDialog_txtSpecStatus");
			LBenDialog_txtStatus_O = (String) request.get("LBenDialog_txtStatus_O");
			LBenDialog_txtSpecStatus_O = (String) request.get("LBenDialog_txtSpecStatus_O");
			LBenDialog_txtRequestNo = (String) request.get("LBenDialog_txtRequestNo");
			LBenDialog_txtPriorityNo = (String) request.get("LBenDialog_txtPriorityNo");
			LBenDialog_txtRequestNo_O = (String) request.get("LBenDialog_txtRequestNo_O");
			LBenDialog_txtPriorityNo_O = (String) request.get("LBenDialog_txtPriorityNo_O");
			LBenDialog_fra_agr_id = (BigDecimal) request.get("LBenDialog_fra_agr_id");
			LBenDialog_fra_agr_id_O = (BigDecimal) request.get("LBenDialog_fra_agr_id_O");
			laAccId = (BigDecimal) request.get("laAccId");
			l_fra_agr_id = (BigDecimal) request.get("l_fra_agr_id");
			LBenDialog_txtApsRqstNo = (String) request.get("LBenDialog_txtApsRqstNo");
			LBenDialog_txtApsRqstNo_O = (String) request.get("LBenDialog_txtApsRqstNo_O");
			LBen_col_hea_id_check = (BigDecimal) request.get("LBen_col_hea_id_check");
			LBenDialog_Insu = (String) request.get("LBenDialog_Insu");
			LBenDialog_IP_CUS_ID = (BigDecimal) request.get("LBenDialog_IP_CUS_ID");
			LBenDialog_Insu_O = (String) request.get("LBenDialog_Insu_O");
			LBenDialog_IP_CUS_ID_O = (BigDecimal) request.get("LBenDialog_IP_CUS_ID_O");
			RealEstate_COL_HEA_ID = (BigDecimal) request.get("RealEstate_COL_HEA_ID");
			RealEstate_REAL_EST_NM_CUR_ID = (BigDecimal) request.get("RealEstate_REAL_EST_NM_CUR_ID");
			RealEstate_txtNomiValu = (BigDecimal) request.get("RealEstate_txtNomiValu");
			RealEstate_txtCollMvpPonder = (BigDecimal) request.get("RealEstate_txtCollMvpPonder");
			LBenDialog_txtContractNo = (String) request.get("LBenDialog_txtContractNo");
			LBenDialog_txtContractNo_O = (String) request.get("LBenDialog_txtContractNo_O");
		}

		public Map getResponse() {
			response.put("RealEstate_txtWeighValue", RealEstate_txtWeighValue);
			response.put("RealEstate_txtWeighDate", RealEstate_txtWeighDate);
			response.put("RealEstate_txtAvailValue", RealEstate_txtAvailValue);
			response.put("RealEstate_txtAvailDate", RealEstate_txtAvailDate);
			response.put("RealEstate_txtSumPartVal", RealEstate_txtSumPartVal);
			response.put("RealEstate_txtSumPartDat", RealEstate_txtSumPartDat);
			response.put("Kol_ND_lb", Kol_ND_lb);
			response.put("Kol_ND_lb_dsc", Kol_ND_lb_dsc);
			response.put("Kol_B2_lb", Kol_B2_lb);
			response.put("Kol_B2_lb_dsc", Kol_B2_lb_dsc);
			response.put("Kol_HNB_lb", Kol_HNB_lb);
			response.put("Kol_HNB_lb_dsc", Kol_HNB_lb_dsc);
			response.put("Kol_B2IRB_lb", Kol_B2IRB_lb);
			response.put("Kol_B2IRB_lb_dsc", Kol_B2IRB_lb_dsc);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class LOANBENEFICIARYDELM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal LBenDialog_LOAN_BEN_ID = null;
		public String LBenDialog_txtRegNo = null;
		public BigDecimal LBenDialog_CUS_ID = null;
		public String LBenDialog_txtAccNo = null;
		public BigDecimal LBenDialog_LA_ACC_ID = null;
		public BigDecimal LBenDialog_COLL_HF_PRIOR_ID = null;
		public BigDecimal LBenDialog_COLL_HEA_ID = null;
		public Integer LBenDialog_txtSerNum = null;
		public Date LBenDialog_txtDateFrom = null;
		public Date LBenDialog_txtDateUntil = null;
		public BigDecimal LBenDialog_USE_OPEN_ID = null;
		public BigDecimal LBenDialog_USE_ID = null;
		public Timestamp LBenDialog_txtOpeningTS = null;
		public Timestamp LBenDialog_txtUserLock = null;
		public String LBenDialog_txtName = null;
		public String LBenDialog_txtCode = null;
		public String LBenDialog_txtFName = null;
		public String LBenDialog_txtSurName = null;
		public String LBenDialog_txtUserLogin = null;
		public String LBenDialog_txtUserName = null;
		public String LBenDialog_txtUserOpenLogin = null;
		public String LBenDialog_txtUserOpenName = null;
		public String LBenDialog_txtRegNo_O = null;
		public BigDecimal LBenDialog_CUS_ID_O = null;
		public String LBenDialog_txtAccNo_O = null;
		public BigDecimal LBenDialog_LA_ACC_ID_O = null;
		public BigDecimal LBenDialog_COLL_HF_PRIOR_ID_O = null;
		public BigDecimal LBenDialog_COLL_HEA_ID_O = null;
		public Integer LBenDialog_txtSerNum_O = null;
		public Date LBenDialog_txtDateFrom_O = null;
		public Date LBenDialog_txtDateUntil_O = null;
		public BigDecimal LBenDialog_USE_OPEN_ID_O = null;
		public BigDecimal LBenDialog_USE_ID_O = null;
		public Timestamp LBenDialog_txtOpeningTS_O = null;
		public Timestamp LBenDialog_txtUserLock_O = null;
		public String LBenDialog_txtName_O = null;
		public String LBenDialog_txtCode_O = null;
		public String LBenDialog_txtFName_O = null;
		public String LBenDialog_txtSurName_O = null;
		public String LBenDialog_txtUserLogin_O = null;
		public String LBenDialog_txtUserName_O = null;
		public String LBenDialog_txtUserOpenLogin_O = null;
		public String LBenDialog_txtUserOpenName_O = null;
		public String LBenDialog_txtStatus = null;
		public String LBenDialog_txtSpecStatus = null;
		public String LBenDialog_txtStatus_O = null;
		public String LBenDialog_txtSpecStatus_O = null;
		public String LBenDialog_txtRequestNo = null;
		public String LBenDialog_txtPriorityNo = null;
		public String LBenDialog_txtRequestNo_O = null;
		public String LBenDialog_txtPriorityNo_O = null;
		public BigDecimal laAccId = null;
		public BigDecimal l_fra_agr_id = null;
		public String LBenDialog_txtApsRqstNo = null;
		public String LBenDialog_txtApsRqstNo_O = null;
		public BigDecimal RealEstate_COL_HEA_ID = null;
		public BigDecimal RealEstate_REAL_EST_NM_CUR_ID = null;
		public BigDecimal RealEstate_txtNomiValu = null;
		public BigDecimal RealEstate_txtCollMvpPonder = null;

		// out args
		public BigDecimal LBenDialog_fra_agr_id = null;
		public BigDecimal LBenDialog_fra_agr_id_O = null;
		public BigDecimal RealEstate_txtWeighValue = null;
		public Date RealEstate_txtWeighDate = null;
		public BigDecimal RealEstate_txtAvailValue = null;
		public Date RealEstate_txtAvailDate = null;
		public BigDecimal RealEstate_txtSumPartVal = null;
		public Date RealEstate_txtSumPartDat = null;
		public String Kol_ND_lb = null;
		public String Kol_ND_lb_dsc = null;
		public String Kol_B2_lb = null;
		public String Kol_B2_lb_dsc = null;
		public String Kol_HNB_lb = null;
		public String Kol_HNB_lb_dsc = null;
		public String Kol_B2IRB_lb = null;
		public String Kol_B2IRB_lb_dsc = null;

		public LOANBENEFICIARYDELM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LBenDialog_LOAN_BEN_ID = (BigDecimal) request.get("LBenDialog_LOAN_BEN_ID");
			LBenDialog_txtRegNo = (String) request.get("LBenDialog_txtRegNo");
			LBenDialog_CUS_ID = (BigDecimal) request.get("LBenDialog_CUS_ID");
			LBenDialog_txtAccNo = (String) request.get("LBenDialog_txtAccNo");
			LBenDialog_LA_ACC_ID = (BigDecimal) request.get("LBenDialog_LA_ACC_ID");
			LBenDialog_COLL_HF_PRIOR_ID = (BigDecimal) request.get("LBenDialog_COLL_HF_PRIOR_ID");
			LBenDialog_COLL_HEA_ID = (BigDecimal) request.get("LBenDialog_COLL_HEA_ID");
			LBenDialog_txtSerNum = (Integer) request.get("LBenDialog_txtSerNum");
			LBenDialog_txtDateFrom = (Date) request.get("LBenDialog_txtDateFrom");
			LBenDialog_txtDateUntil = (Date) request.get("LBenDialog_txtDateUntil");
			LBenDialog_USE_OPEN_ID = (BigDecimal) request.get("LBenDialog_USE_OPEN_ID");
			LBenDialog_USE_ID = (BigDecimal) request.get("LBenDialog_USE_ID");
			LBenDialog_txtOpeningTS = (Timestamp) request.get("LBenDialog_txtOpeningTS");
			LBenDialog_txtUserLock = (Timestamp) request.get("LBenDialog_txtUserLock");
			LBenDialog_txtName = (String) request.get("LBenDialog_txtName");
			LBenDialog_txtCode = (String) request.get("LBenDialog_txtCode");
			LBenDialog_txtFName = (String) request.get("LBenDialog_txtFName");
			LBenDialog_txtSurName = (String) request.get("LBenDialog_txtSurName");
			LBenDialog_txtUserLogin = (String) request.get("LBenDialog_txtUserLogin");
			LBenDialog_txtUserName = (String) request.get("LBenDialog_txtUserName");
			LBenDialog_txtUserOpenLogin = (String) request.get("LBenDialog_txtUserOpenLogin");
			LBenDialog_txtUserOpenName = (String) request.get("LBenDialog_txtUserOpenName");
			LBenDialog_txtRegNo_O = (String) request.get("LBenDialog_txtRegNo_O");
			LBenDialog_CUS_ID_O = (BigDecimal) request.get("LBenDialog_CUS_ID_O");
			LBenDialog_txtAccNo_O = (String) request.get("LBenDialog_txtAccNo_O");
			LBenDialog_LA_ACC_ID_O = (BigDecimal) request.get("LBenDialog_LA_ACC_ID_O");
			LBenDialog_COLL_HF_PRIOR_ID_O = (BigDecimal) request.get("LBenDialog_COLL_HF_PRIOR_ID_O");
			LBenDialog_COLL_HEA_ID_O = (BigDecimal) request.get("LBenDialog_COLL_HEA_ID_O");
			LBenDialog_txtSerNum_O = (Integer) request.get("LBenDialog_txtSerNum_O");
			LBenDialog_txtDateFrom_O = (Date) request.get("LBenDialog_txtDateFrom_O");
			LBenDialog_txtDateUntil_O = (Date) request.get("LBenDialog_txtDateUntil_O");
			LBenDialog_USE_OPEN_ID_O = (BigDecimal) request.get("LBenDialog_USE_OPEN_ID_O");
			LBenDialog_USE_ID_O = (BigDecimal) request.get("LBenDialog_USE_ID_O");
			LBenDialog_txtOpeningTS_O = (Timestamp) request.get("LBenDialog_txtOpeningTS_O");
			LBenDialog_txtUserLock_O = (Timestamp) request.get("LBenDialog_txtUserLock_O");
			LBenDialog_txtName_O = (String) request.get("LBenDialog_txtName_O");
			LBenDialog_txtCode_O = (String) request.get("LBenDialog_txtCode_O");
			LBenDialog_txtFName_O = (String) request.get("LBenDialog_txtFName_O");
			LBenDialog_txtSurName_O = (String) request.get("LBenDialog_txtSurName_O");
			LBenDialog_txtUserLogin_O = (String) request.get("LBenDialog_txtUserLogin_O");
			LBenDialog_txtUserName_O = (String) request.get("LBenDialog_txtUserName_O");
			LBenDialog_txtUserOpenLogin_O = (String) request.get("LBenDialog_txtUserOpenLogin_O");
			LBenDialog_txtUserOpenName_O = (String) request.get("LBenDialog_txtUserOpenName_O");
			LBenDialog_txtStatus = (String) request.get("LBenDialog_txtStatus");
			LBenDialog_txtSpecStatus = (String) request.get("LBenDialog_txtSpecStatus");
			LBenDialog_txtStatus_O = (String) request.get("LBenDialog_txtStatus_O");
			LBenDialog_txtSpecStatus_O = (String) request.get("LBenDialog_txtSpecStatus_O");
			LBenDialog_txtRequestNo = (String) request.get("LBenDialog_txtRequestNo");
			LBenDialog_txtPriorityNo = (String) request.get("LBenDialog_txtPriorityNo");
			LBenDialog_txtRequestNo_O = (String) request.get("LBenDialog_txtRequestNo_O");
			LBenDialog_txtPriorityNo_O = (String) request.get("LBenDialog_txtPriorityNo_O");
			laAccId = (BigDecimal) request.get("laAccId");
			l_fra_agr_id = (BigDecimal) request.get("l_fra_agr_id");
			LBenDialog_txtApsRqstNo = (String) request.get("LBenDialog_txtApsRqstNo");
			LBenDialog_txtApsRqstNo_O = (String) request.get("LBenDialog_txtApsRqstNo_O");
			RealEstate_COL_HEA_ID = (BigDecimal) request.get("RealEstate_COL_HEA_ID");
			RealEstate_REAL_EST_NM_CUR_ID = (BigDecimal) request.get("RealEstate_REAL_EST_NM_CUR_ID");
			RealEstate_txtNomiValu = (BigDecimal) request.get("RealEstate_txtNomiValu");
			RealEstate_txtCollMvpPonder = (BigDecimal) request.get("RealEstate_txtCollMvpPonder");
		}

		public Map getResponse() {
			response.put("LBenDialog_fra_agr_id", LBenDialog_fra_agr_id);
			response.put("LBenDialog_fra_agr_id_O", LBenDialog_fra_agr_id_O);
			response.put("RealEstate_txtWeighValue", RealEstate_txtWeighValue);
			response.put("RealEstate_txtWeighDate", RealEstate_txtWeighDate);
			response.put("RealEstate_txtAvailValue", RealEstate_txtAvailValue);
			response.put("RealEstate_txtAvailDate", RealEstate_txtAvailDate);
			response.put("RealEstate_txtSumPartVal", RealEstate_txtSumPartVal);
			response.put("RealEstate_txtSumPartDat", RealEstate_txtSumPartDat);
			response.put("Kol_ND_lb", Kol_ND_lb);
			response.put("Kol_ND_lb_dsc", Kol_ND_lb_dsc);
			response.put("Kol_B2_lb", Kol_B2_lb);
			response.put("Kol_B2_lb_dsc", Kol_B2_lb_dsc);
			response.put("Kol_HNB_lb", Kol_HNB_lb);
			response.put("Kol_HNB_lb_dsc", Kol_HNB_lb_dsc);
			response.put("Kol_B2IRB_lb", Kol_B2IRB_lb);
			response.put("Kol_B2IRB_lb_dsc", Kol_B2IRB_lb_dsc);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class LOANBENEFICIARYNEWLISTMAPP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal LBen_COLL_HF_PRIOR_ID = null;
		public BigDecimal LBen_COLL_HEA_ID = null;
		public BigDecimal LBen_fra_agr_id = null;
		public String LBen_AgreementFlag = null;

		// out args
		public TableData tblLoanBeneficiary = new TableData();

		public LOANBENEFICIARYNEWLISTMAPP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			LBen_COLL_HF_PRIOR_ID = (BigDecimal) request.get("LBen_COLL_HF_PRIOR_ID");
			LBen_COLL_HEA_ID = (BigDecimal) request.get("LBen_COLL_HEA_ID");
			LBen_fra_agr_id = (BigDecimal) request.get("LBen_fra_agr_id");
			LBen_AgreementFlag = (String) request.get("LBen_AgreementFlag");
		}

		public Map getResponse() {
			response.put("tblLoanBeneficiary", tblLoanBeneficiary);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class LBENKREDADMINMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal LBenDialog_LOAN_BEN_ID = null;
		public Timestamp LBenDialog_txtUserLock_O = null;
		public String LBenDialog_txtKredAdmin = null;
		public BigDecimal LBenDialog_KredAdminUSE_ID = null;
		public String LBenDialog_txtRegNo_O = null;
		public BigDecimal LBenDialog_CUS_ID_O = null;
		public String LBenDialog_txtAccNo_O = null;
		public BigDecimal LBenDialog_LA_ACC_ID_O = null;
		public BigDecimal LBenDialog_COLL_HF_PRIOR_ID_O = null;
		public BigDecimal LBenDialog_COLL_HEA_ID_O = null;
		public Integer LBenDialog_txtSerNum_O = null;
		public Date LBenDialog_txtDateFrom_O = null;
		public Date LBenDialog_txtDateUntil_O = null;
		public BigDecimal LBenDialog_USE_OPEN_ID_O = null;
		public BigDecimal LBenDialog_USE_ID_O = null;
		public Timestamp LBenDialog_txtOpeningTS_O = null;
		public String LBenDialog_txtName_O = null;
		public String LBenDialog_txtCode_O = null;
		public String LBenDialog_txtFName_O = null;
		public String LBenDialog_txtSurName_O = null;
		public String LBenDialog_txtUserLogin_O = null;
		public String LBenDialog_txtUserName_O = null;
		public String LBenDialog_txtUserOpenLogin_O = null;
		public String LBenDialog_txtUserOpenName_O = null;
		public String LBenDialog_txtStatus_O = null;
		public String LBenDialog_txtSpecStatus_O = null;
		public String LBenDialog_txtRequestNo_O = null;
		public String LBenDialog_txtPriorityNo_O = null;
		public BigDecimal LBenDialog_fra_agr_id_O = null;
		public String LBenDialog_txtApsRqstNo_O = null;
		public String LBenDialog_Insu_O = null;
		public BigDecimal LBenDialog_IP_CUS_ID_O = null;
		public String LBenDialog_txtKredAdmin_O = null;
		public BigDecimal LBenDialog_KredAdminUSE_ID_O = null;
		public Timestamp LBenDialog_txtKredAdminTime_O = null;

		// out args
		public Timestamp LBenDialog_txtKredAdminTime = null;

		public LBENKREDADMINMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LBenDialog_LOAN_BEN_ID = (BigDecimal) request.get("LBenDialog_LOAN_BEN_ID");
			LBenDialog_txtUserLock_O = (Timestamp) request.get("LBenDialog_txtUserLock_O");
			LBenDialog_txtKredAdmin = (String) request.get("LBenDialog_txtKredAdmin");
			LBenDialog_KredAdminUSE_ID = (BigDecimal) request.get("LBenDialog_KredAdminUSE_ID");
			LBenDialog_txtRegNo_O = (String) request.get("LBenDialog_txtRegNo_O");
			LBenDialog_CUS_ID_O = (BigDecimal) request.get("LBenDialog_CUS_ID_O");
			LBenDialog_txtAccNo_O = (String) request.get("LBenDialog_txtAccNo_O");
			LBenDialog_LA_ACC_ID_O = (BigDecimal) request.get("LBenDialog_LA_ACC_ID_O");
			LBenDialog_COLL_HF_PRIOR_ID_O = (BigDecimal) request.get("LBenDialog_COLL_HF_PRIOR_ID_O");
			LBenDialog_COLL_HEA_ID_O = (BigDecimal) request.get("LBenDialog_COLL_HEA_ID_O");
			LBenDialog_txtSerNum_O = (Integer) request.get("LBenDialog_txtSerNum_O");
			LBenDialog_txtDateFrom_O = (Date) request.get("LBenDialog_txtDateFrom_O");
			LBenDialog_txtDateUntil_O = (Date) request.get("LBenDialog_txtDateUntil_O");
			LBenDialog_USE_OPEN_ID_O = (BigDecimal) request.get("LBenDialog_USE_OPEN_ID_O");
			LBenDialog_USE_ID_O = (BigDecimal) request.get("LBenDialog_USE_ID_O");
			LBenDialog_txtOpeningTS_O = (Timestamp) request.get("LBenDialog_txtOpeningTS_O");
			LBenDialog_txtName_O = (String) request.get("LBenDialog_txtName_O");
			LBenDialog_txtCode_O = (String) request.get("LBenDialog_txtCode_O");
			LBenDialog_txtFName_O = (String) request.get("LBenDialog_txtFName_O");
			LBenDialog_txtSurName_O = (String) request.get("LBenDialog_txtSurName_O");
			LBenDialog_txtUserLogin_O = (String) request.get("LBenDialog_txtUserLogin_O");
			LBenDialog_txtUserName_O = (String) request.get("LBenDialog_txtUserName_O");
			LBenDialog_txtUserOpenLogin_O = (String) request.get("LBenDialog_txtUserOpenLogin_O");
			LBenDialog_txtUserOpenName_O = (String) request.get("LBenDialog_txtUserOpenName_O");
			LBenDialog_txtStatus_O = (String) request.get("LBenDialog_txtStatus_O");
			LBenDialog_txtSpecStatus_O = (String) request.get("LBenDialog_txtSpecStatus_O");
			LBenDialog_txtRequestNo_O = (String) request.get("LBenDialog_txtRequestNo_O");
			LBenDialog_txtPriorityNo_O = (String) request.get("LBenDialog_txtPriorityNo_O");
			LBenDialog_fra_agr_id_O = (BigDecimal) request.get("LBenDialog_fra_agr_id_O");
			LBenDialog_txtApsRqstNo_O = (String) request.get("LBenDialog_txtApsRqstNo_O");
			LBenDialog_Insu_O = (String) request.get("LBenDialog_Insu_O");
			LBenDialog_IP_CUS_ID_O = (BigDecimal) request.get("LBenDialog_IP_CUS_ID_O");
			LBenDialog_txtKredAdmin_O = (String) request.get("LBenDialog_txtKredAdmin_O");
			LBenDialog_KredAdminUSE_ID_O = (BigDecimal) request.get("LBenDialog_KredAdminUSE_ID_O");
			LBenDialog_txtKredAdminTime_O = (Timestamp) request.get("LBenDialog_txtKredAdminTime_O");
		}

		public Map getResponse() {
			response.put("LBenDialog_txtKredAdminTime", LBenDialog_txtKredAdminTime);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class LBENFRAMEACCEXCEPTIONCHECKM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal cus_acc_id = null;

		// out args
		public String LBenDialog_frame_acc_exception_flag = null;

		public LBENFRAMEACCEXCEPTIONCHECKM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			cus_acc_id = (BigDecimal) request.get("cus_acc_id");
		}

		public Map getResponse() {
			response.put("LBenDialog_frame_acc_exception_flag", LBenDialog_frame_acc_exception_flag);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}