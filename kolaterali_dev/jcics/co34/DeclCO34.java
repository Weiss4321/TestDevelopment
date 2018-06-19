package hr.vestigo.modules.collateral.jcics.co34;

import java.sql.Timestamp;
import java.sql.Date;
import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO34 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co34/DeclCO34.java,v 1.7 2014/06/17 12:23:38 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCO34() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public String ScreenEntryParam = null;
	public BigDecimal use_id = null;
	public BigDecimal org_uni_id = null;
	public BigDecimal HF_COL_HEA_ID = null;

	// out args
	public TableData tblKolMortgage = new TableData();

	// inner classes
	public KOLMORTGAGESEL kolmortgagesel = null;
	public KOLMORTGAGEDEACT kolmortgagedeact = null;
	public KOLATERALDEACT kolateraldeact = null;
	public MORTAGEHISTDETAILSMAPPING mortagehistdetailsmapping = null;
	public MORTGAGEDEACTPRINTDEMO mortgagedeactprintdemo = null;
	public KOLATERALBEFOREDEACT kolateralbeforedeact = null;
	public KOLMORTAGEDEACTCOUNT kolmortagedeactcount = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		ScreenEntryParam = (String) request.get("ScreenEntryParam");
		use_id = (BigDecimal) request.get("use_id");
		org_uni_id = (BigDecimal) request.get("org_uni_id");
		HF_COL_HEA_ID = (BigDecimal) request.get("HF_COL_HEA_ID");
	}

	public Map getResponse() {
		response.put("tblKolMortgage", tblKolMortgage);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("KolMortgageSel")) {
			kolmortgagesel = new KOLMORTGAGESEL(request);
			return kolmortgagesel;
		} else if(mapping.equals("KolMortgageDeact")) {
			kolmortgagedeact = new KOLMORTGAGEDEACT(request);
			return kolmortgagedeact;
		} else if(mapping.equals("KolateralDeact")) {
			kolateraldeact = new KOLATERALDEACT(request);
			return kolateraldeact;
		} else if(mapping.equals("MortageHistDetailsMapping")) {
			mortagehistdetailsmapping = new MORTAGEHISTDETAILSMAPPING(request);
			return mortagehistdetailsmapping;
		} else if(mapping.equals("MortgageDeactPrintDemo")) {
			mortgagedeactprintdemo = new MORTGAGEDEACTPRINTDEMO(request);
			return mortgagedeactprintdemo;
		} else if(mapping.equals("KolateralBeforeDeact")) {
			kolateralbeforedeact = new KOLATERALBEFOREDEACT(request);
			return kolateralbeforedeact;
		} else if(mapping.equals("KolMortageDeactCount")) {
			kolmortagedeactcount = new KOLMORTAGEDEACTCOUNT(request);
			return kolmortagedeactcount;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class KOLMORTGAGESEL implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COLL_HF_PRIOR_ID = null;

		// out args
		public BigDecimal HF_TABLE_ID = null;
		public String CollHfPriorDialog_txtHfTableSysCodeValue = null;
		public String CollHfPriorDialog_txtHfTableSysCodeDesc = null;
		public BigDecimal HF_REF_ID = null;
		public String CollHfPriorDialog_txtColNum = null;
		public BigDecimal HF_REC_LOP_ID = null;
		public String CollHfPriorDialog_txtHfRecLopSysCodeValue = null;
		public String CollHfPriorDialog_txtHfRecLopSysCodeDesc = null;
		public BigDecimal HF_OWN_CUS_ID = null;
		public String CollHfPriorDialog_txtHfRegisterNo = null;
		public String CollHfPriorDialog_txtHfOwnCode = null;
		public String CollHfPriorDialog_txtHfOwnFname = null;
		public String CollHfPriorDialog_txtHfOwnLname = null;
		public BigDecimal CollHfPriorDialog_txtHfAmount = null;
		public BigDecimal HF_CUR_ID = null;
		public String CollHfPriorDialog_txtHfCurIdCodeChar = null;
		public BigDecimal HF_HFC_ID = null;
		public String CollHfPriorDialog_txtHfHfcSysCodeValue = null;
		public String CollHfPriorDialog_txtHfHfcSysCodeDesc = null;
		public String CollHfPriorDialog_txtHfPriority = null;
		public Date CollHfPriorDialog_txtHfDateHfcFrom = null;
		public Date CollHfPriorDialog_txtHfDateHfcUntil = null;
		public Date CollHfPriorDialog_txtHfDateReciv = null;
		public String CollHfPriorDialog_txtHfCourtDecis = null;
		public Date CollHfPriorDialog_txtHfDateExtract = null;
		public BigDecimal HF_OFFI_LRD = null;
		public String CollHfPriorDialog_txtHfOffildRegisterNo = null;
		public String CollHfPriorDialog_txtHfOffildFname = null;
		public String CollHfPriorDialog_txtHfOffildLname = null;
		public String CollHfPriorDialog_txtHfNotaryAgr = null;
		public BigDecimal HF_NOTARY_PLACE_ID = null;
		public String CollHfPriorDialog_txtHfNotaryPlace = null;
		public Date CollHfPriorDialog_txtHfNotaryDate = null;
		public BigDecimal HF_NOTARY = null;
		public String CollHfPriorDialog_txtHfNotaryRegisterNo = null;
		public String CollHfPriorDialog_txtHfNotFname = null;
		public String CollHfPriorDialog_txtHfNotLname = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailAmo = null;
		public Date CollHfPriorDialog_txtHfAvailDat = null;
		public String CollHfPriorDialog_txtHfStatus = null;
		public String CollHfPriorDialog_txtHfSpecStat = null;
		public String CollHfPriorDialog_txtHfAddData = null;
		public BigDecimal USE_OPEN_ID = null;
		public String CollHfPriorDialog_txtUseOpenIdLogin = null;
		public String CollHfPriorDialog_txtUseOpenIdName = null;
		public Timestamp CollHfPriorDialog_txtOpeningTs = null;
		public BigDecimal USE_ID = null;
		public String CollHfPriorDialog_txtUseIdLogin = null;
		public String CollHfPriorDialog_txtUseIdName = null;
		public Timestamp CollHfPriorDialog_txtUserLock = null;
		public Date CollHfPriorDialog_txtHfDateFrom = null;
		public Date CollHfPriorDialog_txtHfDateUntil = null;
		public BigDecimal HF_COLL_HEAD_ID = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawAmo = null;
		public BigDecimal CollHfPriorDialog_txtAmountRef = null;
		public BigDecimal CUR_ID_REF = null;
		public String CollHfPriorDialog_txtHfCurIdRefCodeChar = null;
		public BigDecimal CollHfPriorDialog_txtExcRatRef = null;
		public Date CollHfPriorDialog_txtExcRatRefDate = null;
		public Date CollHfPriorDialog_txtHfAvailDatRef = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawAmoRef = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailBAmoRef = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawBAmoRef = null;
		public Date CollHfPriorDialog_txtValDateTurn = null;
		public Date CollHfPriorDialog_txtValDateProc = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawBAmo = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailBAmo = null;
		public Date CollHfPriorDialog_txtHfAvailBDat = null;
		public String CollHfPriorDialog_txtLandRegn = null;
		public String CollHfPriorDialog_txtLandRegnNew = null;
		public BigDecimal CollHfPriorDialog_JUDGE_ID = null;
		public String CollHfPriorDialog_txtJudgeFname = null;
		public String CollHfPriorDialog_txtJudgeLname = null;
		public BigDecimal CollHfPriorDialog_COURT_ID = null;
		public String CollHfPriorDialog_txtJudgeRegisterNo = null;
		public String CollHfPriorDialog_txtCoCode = null;
		public String CollHfPriorDialog_txtCoName = null;
		public Timestamp USER_LOCKCollHead = null;
		public Timestamp USER_LOCKCollHfPrior = null;
		public BigDecimal CollHfPrior_HOW_COWER = null;
		public String CollHfPrior_txtHowCowerSCV = null;
		public String CollHfPrior_txtHowCowerSCD = null;
		public String CollHfPrior_txtIsPartAgreem = null;
		public String CollHFP_txtRecLop = null;
		public Date CollHFP_txtDateToLop = null;
		public Date CollHFP_txtDateRecLop = null;
		public String CollHFP_txtVehConNum = null;
		public String Hf_txtRegIns = null;
		public BigDecimal REG_INS = null;
		public String Kol_txtFrameAgr = null;
		public String Agr_txtAgrNo1 = null;
		public BigDecimal fra_agr_id = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailAmoRef = null;

		public KOLMORTGAGESEL(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COLL_HF_PRIOR_ID = (BigDecimal) request.get("COLL_HF_PRIOR_ID");
		}

		public Map getResponse() {
			response.put("HF_TABLE_ID", HF_TABLE_ID);
			response.put("CollHfPriorDialog_txtHfTableSysCodeValue", CollHfPriorDialog_txtHfTableSysCodeValue);
			response.put("CollHfPriorDialog_txtHfTableSysCodeDesc", CollHfPriorDialog_txtHfTableSysCodeDesc);
			response.put("HF_REF_ID", HF_REF_ID);
			response.put("CollHfPriorDialog_txtColNum", CollHfPriorDialog_txtColNum);
			response.put("HF_REC_LOP_ID", HF_REC_LOP_ID);
			response.put("CollHfPriorDialog_txtHfRecLopSysCodeValue", CollHfPriorDialog_txtHfRecLopSysCodeValue);
			response.put("CollHfPriorDialog_txtHfRecLopSysCodeDesc", CollHfPriorDialog_txtHfRecLopSysCodeDesc);
			response.put("HF_OWN_CUS_ID", HF_OWN_CUS_ID);
			response.put("CollHfPriorDialog_txtHfRegisterNo", CollHfPriorDialog_txtHfRegisterNo);
			response.put("CollHfPriorDialog_txtHfOwnCode", CollHfPriorDialog_txtHfOwnCode);
			response.put("CollHfPriorDialog_txtHfOwnFname", CollHfPriorDialog_txtHfOwnFname);
			response.put("CollHfPriorDialog_txtHfOwnLname", CollHfPriorDialog_txtHfOwnLname);
			response.put("CollHfPriorDialog_txtHfAmount", CollHfPriorDialog_txtHfAmount);
			response.put("HF_CUR_ID", HF_CUR_ID);
			response.put("CollHfPriorDialog_txtHfCurIdCodeChar", CollHfPriorDialog_txtHfCurIdCodeChar);
			response.put("HF_HFC_ID", HF_HFC_ID);
			response.put("CollHfPriorDialog_txtHfHfcSysCodeValue", CollHfPriorDialog_txtHfHfcSysCodeValue);
			response.put("CollHfPriorDialog_txtHfHfcSysCodeDesc", CollHfPriorDialog_txtHfHfcSysCodeDesc);
			response.put("CollHfPriorDialog_txtHfPriority", CollHfPriorDialog_txtHfPriority);
			response.put("CollHfPriorDialog_txtHfDateHfcFrom", CollHfPriorDialog_txtHfDateHfcFrom);
			response.put("CollHfPriorDialog_txtHfDateHfcUntil", CollHfPriorDialog_txtHfDateHfcUntil);
			response.put("CollHfPriorDialog_txtHfDateReciv", CollHfPriorDialog_txtHfDateReciv);
			response.put("CollHfPriorDialog_txtHfCourtDecis", CollHfPriorDialog_txtHfCourtDecis);
			response.put("CollHfPriorDialog_txtHfDateExtract", CollHfPriorDialog_txtHfDateExtract);
			response.put("HF_OFFI_LRD", HF_OFFI_LRD);
			response.put("CollHfPriorDialog_txtHfOffildRegisterNo", CollHfPriorDialog_txtHfOffildRegisterNo);
			response.put("CollHfPriorDialog_txtHfOffildFname", CollHfPriorDialog_txtHfOffildFname);
			response.put("CollHfPriorDialog_txtHfOffildLname", CollHfPriorDialog_txtHfOffildLname);
			response.put("CollHfPriorDialog_txtHfNotaryAgr", CollHfPriorDialog_txtHfNotaryAgr);
			response.put("HF_NOTARY_PLACE_ID", HF_NOTARY_PLACE_ID);
			response.put("CollHfPriorDialog_txtHfNotaryPlace", CollHfPriorDialog_txtHfNotaryPlace);
			response.put("CollHfPriorDialog_txtHfNotaryDate", CollHfPriorDialog_txtHfNotaryDate);
			response.put("HF_NOTARY", HF_NOTARY);
			response.put("CollHfPriorDialog_txtHfNotaryRegisterNo", CollHfPriorDialog_txtHfNotaryRegisterNo);
			response.put("CollHfPriorDialog_txtHfNotFname", CollHfPriorDialog_txtHfNotFname);
			response.put("CollHfPriorDialog_txtHfNotLname", CollHfPriorDialog_txtHfNotLname);
			response.put("CollHfPriorDialog_txtHfAvailAmo", CollHfPriorDialog_txtHfAvailAmo);
			response.put("CollHfPriorDialog_txtHfAvailDat", CollHfPriorDialog_txtHfAvailDat);
			response.put("CollHfPriorDialog_txtHfStatus", CollHfPriorDialog_txtHfStatus);
			response.put("CollHfPriorDialog_txtHfSpecStat", CollHfPriorDialog_txtHfSpecStat);
			response.put("CollHfPriorDialog_txtHfAddData", CollHfPriorDialog_txtHfAddData);
			response.put("USE_OPEN_ID", USE_OPEN_ID);
			response.put("CollHfPriorDialog_txtUseOpenIdLogin", CollHfPriorDialog_txtUseOpenIdLogin);
			response.put("CollHfPriorDialog_txtUseOpenIdName", CollHfPriorDialog_txtUseOpenIdName);
			response.put("CollHfPriorDialog_txtOpeningTs", CollHfPriorDialog_txtOpeningTs);
			response.put("USE_ID", USE_ID);
			response.put("CollHfPriorDialog_txtUseIdLogin", CollHfPriorDialog_txtUseIdLogin);
			response.put("CollHfPriorDialog_txtUseIdName", CollHfPriorDialog_txtUseIdName);
			response.put("CollHfPriorDialog_txtUserLock", CollHfPriorDialog_txtUserLock);
			response.put("CollHfPriorDialog_txtHfDateFrom", CollHfPriorDialog_txtHfDateFrom);
			response.put("CollHfPriorDialog_txtHfDateUntil", CollHfPriorDialog_txtHfDateUntil);
			response.put("HF_COLL_HEAD_ID", HF_COLL_HEAD_ID);
			response.put("CollHfPriorDialog_txtHfDrawAmo", CollHfPriorDialog_txtHfDrawAmo);
			response.put("CollHfPriorDialog_txtAmountRef", CollHfPriorDialog_txtAmountRef);
			response.put("CUR_ID_REF", CUR_ID_REF);
			response.put("CollHfPriorDialog_txtHfCurIdRefCodeChar", CollHfPriorDialog_txtHfCurIdRefCodeChar);
			response.put("CollHfPriorDialog_txtExcRatRef", CollHfPriorDialog_txtExcRatRef);
			response.put("CollHfPriorDialog_txtExcRatRefDate", CollHfPriorDialog_txtExcRatRefDate);
			response.put("CollHfPriorDialog_txtHfAvailDatRef", CollHfPriorDialog_txtHfAvailDatRef);
			response.put("CollHfPriorDialog_txtHfDrawAmoRef", CollHfPriorDialog_txtHfDrawAmoRef);
			response.put("CollHfPriorDialog_txtHfAvailBAmoRef", CollHfPriorDialog_txtHfAvailBAmoRef);
			response.put("CollHfPriorDialog_txtHfDrawBAmoRef", CollHfPriorDialog_txtHfDrawBAmoRef);
			response.put("CollHfPriorDialog_txtValDateTurn", CollHfPriorDialog_txtValDateTurn);
			response.put("CollHfPriorDialog_txtValDateProc", CollHfPriorDialog_txtValDateProc);
			response.put("CollHfPriorDialog_txtHfDrawBAmo", CollHfPriorDialog_txtHfDrawBAmo);
			response.put("CollHfPriorDialog_txtHfAvailBAmo", CollHfPriorDialog_txtHfAvailBAmo);
			response.put("CollHfPriorDialog_txtHfAvailBDat", CollHfPriorDialog_txtHfAvailBDat);
			response.put("CollHfPriorDialog_txtLandRegn", CollHfPriorDialog_txtLandRegn);
			response.put("CollHfPriorDialog_txtLandRegnNew", CollHfPriorDialog_txtLandRegnNew);
			response.put("CollHfPriorDialog_JUDGE_ID", CollHfPriorDialog_JUDGE_ID);
			response.put("CollHfPriorDialog_txtJudgeFname", CollHfPriorDialog_txtJudgeFname);
			response.put("CollHfPriorDialog_txtJudgeLname", CollHfPriorDialog_txtJudgeLname);
			response.put("CollHfPriorDialog_COURT_ID", CollHfPriorDialog_COURT_ID);
			response.put("CollHfPriorDialog_txtJudgeRegisterNo", CollHfPriorDialog_txtJudgeRegisterNo);
			response.put("CollHfPriorDialog_txtCoCode", CollHfPriorDialog_txtCoCode);
			response.put("CollHfPriorDialog_txtCoName", CollHfPriorDialog_txtCoName);
			response.put("USER_LOCKCollHead", USER_LOCKCollHead);
			response.put("USER_LOCKCollHfPrior", USER_LOCKCollHfPrior);
			response.put("CollHfPrior_HOW_COWER", CollHfPrior_HOW_COWER);
			response.put("CollHfPrior_txtHowCowerSCV", CollHfPrior_txtHowCowerSCV);
			response.put("CollHfPrior_txtHowCowerSCD", CollHfPrior_txtHowCowerSCD);
			response.put("CollHfPrior_txtIsPartAgreem", CollHfPrior_txtIsPartAgreem);
			response.put("CollHFP_txtRecLop", CollHFP_txtRecLop);
			response.put("CollHFP_txtDateToLop", CollHFP_txtDateToLop);
			response.put("CollHFP_txtDateRecLop", CollHFP_txtDateRecLop);
			response.put("CollHFP_txtVehConNum", CollHFP_txtVehConNum);
			response.put("Hf_txtRegIns", Hf_txtRegIns);
			response.put("REG_INS", REG_INS);
			response.put("Kol_txtFrameAgr", Kol_txtFrameAgr);
			response.put("Agr_txtAgrNo1", Agr_txtAgrNo1);
			response.put("fra_agr_id", fra_agr_id);
			response.put("CollHfPriorDialog_txtHfAvailAmoRef", CollHfPriorDialog_txtHfAvailAmoRef);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class KOLMORTGAGEDEACT implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hf_prior_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String KolMortgageDeact_txtCmnt = null;

		// out args

		public KOLMORTGAGEDEACT(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hf_prior_id = (BigDecimal) request.get("col_hf_prior_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			KolMortgageDeact_txtCmnt = (String) request.get("KolMortgageDeact_txtCmnt");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class KOLATERALDEACT implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;

		// out args

		public KOLATERALDEACT(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class MORTAGEHISTDETAILSMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal CollHfPrior_HF_COLL_HEAD_ID = null;
		public BigDecimal mrtg_id = null;

		// out args
		public TableData tblHistDetails = new TableData();

		public MORTAGEHISTDETAILSMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollHfPrior_HF_COLL_HEAD_ID = (BigDecimal) request.get("CollHfPrior_HF_COLL_HEAD_ID");
			mrtg_id = (BigDecimal) request.get("mrtg_id");
		}

		public Map getResponse() {
			response.put("tblHistDetails", tblHistDetails);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class MORTGAGEDEACTPRINTDEMO implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hf_prior_id = null;
		public BigDecimal Dom_cur_id = null;

		// out args
		public String court = null;
		public String bussinessNo = null;
		public Date requestDate = null;
		public String bank = null;
		public String ownerName = null;
		public String ownerAddress = null;
		public String ownerTaxNum = null;
		public String mortgageDesc = null;
		public String loanUserName = null;
		public String loanUserTaxNum = null;
		public BigDecimal loanAmount = null;
		public BigDecimal loanAmountFc = null;
		public String loanCusAccNo = null;
		public Date contractDate = null;
		public BigDecimal realEstateValue = null;
		public BigDecimal realEstateValueFc = null;
		public String ownerCity = null;
		public BigDecimal mortgageCurrency = null;
		public String mortgageCurrencyCode = null;
		public String isDomestic = null;

		public MORTGAGEDEACTPRINTDEMO(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hf_prior_id = (BigDecimal) request.get("col_hf_prior_id");
			Dom_cur_id = (BigDecimal) request.get("Dom_cur_id");
		}

		public Map getResponse() {
			response.put("court", court);
			response.put("bussinessNo", bussinessNo);
			response.put("requestDate", requestDate);
			response.put("bank", bank);
			response.put("ownerName", ownerName);
			response.put("ownerAddress", ownerAddress);
			response.put("ownerTaxNum", ownerTaxNum);
			response.put("mortgageDesc", mortgageDesc);
			response.put("loanUserName", loanUserName);
			response.put("loanUserTaxNum", loanUserTaxNum);
			response.put("loanAmount", loanAmount);
			response.put("loanAmountFc", loanAmountFc);
			response.put("loanCusAccNo", loanCusAccNo);
			response.put("contractDate", contractDate);
			response.put("realEstateValue", realEstateValue);
			response.put("realEstateValueFc", realEstateValueFc);
			response.put("ownerCity", ownerCity);
			response.put("mortgageCurrency", mortgageCurrency);
			response.put("mortgageCurrencyCode", mortgageCurrencyCode);
			response.put("isDomestic", isDomestic);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class KOLATERALBEFOREDEACT implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String KolDeactCmnt_txtReasoneCode = null;
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String KolDeactCmnt_txtComment = null;
		public String KolDeactCmnt_txtCommentHip = null;
		public BigDecimal col_hf_prior_id = null;

		// out args

		public KOLATERALBEFOREDEACT(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			KolDeactCmnt_txtReasoneCode = (String) request.get("KolDeactCmnt_txtReasoneCode");
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			KolDeactCmnt_txtComment = (String) request.get("KolDeactCmnt_txtComment");
			KolDeactCmnt_txtCommentHip = (String) request.get("KolDeactCmnt_txtCommentHip");
			col_hf_prior_id = (BigDecimal) request.get("col_hf_prior_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class KOLMORTAGEDEACTCOUNT implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hf_prior_id = null;

		// out args
		public Integer MORTAGE_NUMBER = null;

		public KOLMORTAGEDEACTCOUNT(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hf_prior_id = (BigDecimal) request.get("col_hf_prior_id");
		}

		public Map getResponse() {
			response.put("MORTAGE_NUMBER", MORTAGE_NUMBER);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}