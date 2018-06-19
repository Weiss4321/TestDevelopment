package hr.vestigo.modules.collateral.jcics.co03;

import java.sql.Timestamp;
import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO03 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co03/DeclCO03.java,v 1.41 2017/11/10 12:05:12 hrakis Exp $";
	private Map response = new HashMap();

	public DeclCO03() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal CollHfPrior_HF_COLL_HEAD_ID = null;
	public BigDecimal HF_FRA_AGR_ID = null;

	// out args
	public TableData tblCollHfPrior = new TableData();

	// inner classes
	public CHPDINSMAP chpdinsmap = null;
	public CHPDPLAUSEMAP chpdplausemap = null;
	public CHPDDETMAP chpddetmap = null;
	public CHPDUPDMAP chpdupdmap = null;
	public CHPDUSERLOCKM chpduserlockm = null;
	public CHPLOANMAP chploanmap = null;
	public MRTGDELMAP mrtgdelmap = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		CollHfPrior_HF_COLL_HEAD_ID = (BigDecimal) request.get("CollHfPrior_HF_COLL_HEAD_ID");
		HF_FRA_AGR_ID = (BigDecimal) request.get("HF_FRA_AGR_ID");
	}

	public Map getResponse() {
		response.put("tblCollHfPrior", tblCollHfPrior);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("ChpDInsMap")) {
			chpdinsmap = new CHPDINSMAP(request);
			return chpdinsmap;
		} else if(mapping.equals("ChpDPlaUseMap")) {
			chpdplausemap = new CHPDPLAUSEMAP(request);
			return chpdplausemap;
		} else if(mapping.equals("ChpDDetMap")) {
			chpddetmap = new CHPDDETMAP(request);
			return chpddetmap;
		} else if(mapping.equals("ChpDUpdMap")) {
			chpdupdmap = new CHPDUPDMAP(request);
			return chpdupdmap;
		} else if(mapping.equals("ChpDUserLockM")) {
			chpduserlockm = new CHPDUSERLOCKM(request);
			return chpduserlockm;
		} else if(mapping.equals("ChpLoanMap")) {
			chploanmap = new CHPLOANMAP(request);
			return chploanmap;
		} else if(mapping.equals("MrtgDelMap")) {
			mrtgdelmap = new MRTGDELMAP(request);
			return mrtgdelmap;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class CHPDINSMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
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
		public String CollHfPriorDialog_txtHfStatusDesc = null;
		public String CollHfPriorDialog_txtHfSpecStat = null;
		public String CollHfPriorDialog_txtHfSpecStatDesc = null;
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
		public BigDecimal CollHfPriorDialog_txtHfAvailAmoRef = null;
		public Date CollHfPriorDialog_txtHfAvailDatRef = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawAmoRef = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailBAmoRef = null;
		public Date CollHfPriorDialog_txtHfAvailBDatRef = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawBAmoRef = null;
		public String CollHfPriorDialog_txtLandRegn = null;
		public String CollHfPriorDialog_txtLandRegnNew = null;
		public BigDecimal CollHfPriorDialog_JUDGE_ID = null;
		public String CollHfPriorDialog_txtJudgeFname = null;
		public String CollHfPriorDialog_txtJudgeLname = null;
		public BigDecimal CollHfPriorDialog_COURT_ID = null;
		public String CollHfPriorDialog_txtJudgeRegisterNo = null;
		public String CollHfPriorDialog_txtCoCode = null;
		public String CollHfPriorDialog_txtCoName = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailBAmo = null;
		public Date CollHfPriorDialog_txtHfAvailBDat = null;
		public Timestamp USER_LOCKCollHead = null;
		public BigDecimal CollHfPrior_HOW_COWER = null;
		public String CollHfPrior_txtHowCowerSCV = null;
		public String CollHfPrior_txtHowCowerSCD = null;
		public String CollHfPrior_txtIsPartAgreem = null;
		public String CollHFP_txtRecLop = null;
		public Date CollHFP_txtDateToLop = null;
		public Date CollHFP_txtDateRecLop = null;
		public String CollHFP_txtVehConNum = null;
		public BigDecimal REG_INS = null;
		public String Kol_txtFrameAgr = null;
		public BigDecimal fra_agr_id = null;
		public Date HfPror_txtConcDate = null;
		public String HfPror_txtConcNum = null;
		public BigDecimal REG_COU_ID = null;
		public String HfPror_txtRegPlace = null;
		public String CollHfPriorDialog_txtSyndicate = null;
		public String CollHfPriorDialog_txtHBORCredit = null;
		public String CollHfPriorDialog_txtRBAArranger = null;
		public BigDecimal CollHfPriorDialog_txtAmountSyndicate = null;
		public BigDecimal CollHfPriorDialog_txtRBASyndicatePartValue = null;
		public BigDecimal CollHfPriorDialog_txtOtherSyndicatePartValue = null;
		public BigDecimal CollHfPriorDialog_txtRBASyndicateAmountValue = null;
		public BigDecimal CollHfPriorDialog_txtOtherSyndicateAmountValue = null;
		public String CollHfPriorDialog_txtSyndicatedComment = null;
		public BigDecimal OTHER_SYNDICATE_CUS_ID = null;

		// out args
		public BigDecimal COLL_HF_PRIOR_ID = null;
		public BigDecimal Coll_txtThirdRightInNom = null;
		public BigDecimal Coll_txtHfsValue = null;
		public String RealEstate_txtRecLop = null;
		public String Kol_ND = null;
		public String Kol_B2 = null;
		public String Kol_HNB = null;
		public String Kol_B2IRB = null;
		public String Kol_ND_dsc = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB_dsc = null;
		public BigDecimal Kol_txtLastRBAMortgageAmount = null;
		public Date Kol_txtLastRBAMortgageDate = null;

		public CHPDINSMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			HF_TABLE_ID = (BigDecimal) request.get("HF_TABLE_ID");
			CollHfPriorDialog_txtHfTableSysCodeValue = (String) request.get("CollHfPriorDialog_txtHfTableSysCodeValue");
			CollHfPriorDialog_txtHfTableSysCodeDesc = (String) request.get("CollHfPriorDialog_txtHfTableSysCodeDesc");
			HF_REF_ID = (BigDecimal) request.get("HF_REF_ID");
			CollHfPriorDialog_txtColNum = (String) request.get("CollHfPriorDialog_txtColNum");
			HF_REC_LOP_ID = (BigDecimal) request.get("HF_REC_LOP_ID");
			CollHfPriorDialog_txtHfRecLopSysCodeValue = (String) request.get("CollHfPriorDialog_txtHfRecLopSysCodeValue");
			CollHfPriorDialog_txtHfRecLopSysCodeDesc = (String) request.get("CollHfPriorDialog_txtHfRecLopSysCodeDesc");
			HF_OWN_CUS_ID = (BigDecimal) request.get("HF_OWN_CUS_ID");
			CollHfPriorDialog_txtHfRegisterNo = (String) request.get("CollHfPriorDialog_txtHfRegisterNo");
			CollHfPriorDialog_txtHfOwnCode = (String) request.get("CollHfPriorDialog_txtHfOwnCode");
			CollHfPriorDialog_txtHfOwnFname = (String) request.get("CollHfPriorDialog_txtHfOwnFname");
			CollHfPriorDialog_txtHfOwnLname = (String) request.get("CollHfPriorDialog_txtHfOwnLname");
			CollHfPriorDialog_txtHfAmount = (BigDecimal) request.get("CollHfPriorDialog_txtHfAmount");
			HF_CUR_ID = (BigDecimal) request.get("HF_CUR_ID");
			CollHfPriorDialog_txtHfCurIdCodeChar = (String) request.get("CollHfPriorDialog_txtHfCurIdCodeChar");
			HF_HFC_ID = (BigDecimal) request.get("HF_HFC_ID");
			CollHfPriorDialog_txtHfHfcSysCodeValue = (String) request.get("CollHfPriorDialog_txtHfHfcSysCodeValue");
			CollHfPriorDialog_txtHfHfcSysCodeDesc = (String) request.get("CollHfPriorDialog_txtHfHfcSysCodeDesc");
			CollHfPriorDialog_txtHfPriority = (String) request.get("CollHfPriorDialog_txtHfPriority");
			CollHfPriorDialog_txtHfDateHfcFrom = (Date) request.get("CollHfPriorDialog_txtHfDateHfcFrom");
			CollHfPriorDialog_txtHfDateHfcUntil = (Date) request.get("CollHfPriorDialog_txtHfDateHfcUntil");
			CollHfPriorDialog_txtHfDateReciv = (Date) request.get("CollHfPriorDialog_txtHfDateReciv");
			CollHfPriorDialog_txtHfCourtDecis = (String) request.get("CollHfPriorDialog_txtHfCourtDecis");
			CollHfPriorDialog_txtHfDateExtract = (Date) request.get("CollHfPriorDialog_txtHfDateExtract");
			HF_OFFI_LRD = (BigDecimal) request.get("HF_OFFI_LRD");
			CollHfPriorDialog_txtHfOffildRegisterNo = (String) request.get("CollHfPriorDialog_txtHfOffildRegisterNo");
			CollHfPriorDialog_txtHfOffildFname = (String) request.get("CollHfPriorDialog_txtHfOffildFname");
			CollHfPriorDialog_txtHfOffildLname = (String) request.get("CollHfPriorDialog_txtHfOffildLname");
			CollHfPriorDialog_txtHfNotaryAgr = (String) request.get("CollHfPriorDialog_txtHfNotaryAgr");
			HF_NOTARY_PLACE_ID = (BigDecimal) request.get("HF_NOTARY_PLACE_ID");
			CollHfPriorDialog_txtHfNotaryPlace = (String) request.get("CollHfPriorDialog_txtHfNotaryPlace");
			CollHfPriorDialog_txtHfNotaryDate = (Date) request.get("CollHfPriorDialog_txtHfNotaryDate");
			HF_NOTARY = (BigDecimal) request.get("HF_NOTARY");
			CollHfPriorDialog_txtHfNotaryRegisterNo = (String) request.get("CollHfPriorDialog_txtHfNotaryRegisterNo");
			CollHfPriorDialog_txtHfNotFname = (String) request.get("CollHfPriorDialog_txtHfNotFname");
			CollHfPriorDialog_txtHfNotLname = (String) request.get("CollHfPriorDialog_txtHfNotLname");
			CollHfPriorDialog_txtHfAvailAmo = (BigDecimal) request.get("CollHfPriorDialog_txtHfAvailAmo");
			CollHfPriorDialog_txtHfAvailDat = (Date) request.get("CollHfPriorDialog_txtHfAvailDat");
			CollHfPriorDialog_txtHfStatus = (String) request.get("CollHfPriorDialog_txtHfStatus");
			CollHfPriorDialog_txtHfStatusDesc = (String) request.get("CollHfPriorDialog_txtHfStatusDesc");
			CollHfPriorDialog_txtHfSpecStat = (String) request.get("CollHfPriorDialog_txtHfSpecStat");
			CollHfPriorDialog_txtHfSpecStatDesc = (String) request.get("CollHfPriorDialog_txtHfSpecStatDesc");
			CollHfPriorDialog_txtHfAddData = (String) request.get("CollHfPriorDialog_txtHfAddData");
			USE_OPEN_ID = (BigDecimal) request.get("USE_OPEN_ID");
			CollHfPriorDialog_txtUseOpenIdLogin = (String) request.get("CollHfPriorDialog_txtUseOpenIdLogin");
			CollHfPriorDialog_txtUseOpenIdName = (String) request.get("CollHfPriorDialog_txtUseOpenIdName");
			CollHfPriorDialog_txtOpeningTs = (Timestamp) request.get("CollHfPriorDialog_txtOpeningTs");
			USE_ID = (BigDecimal) request.get("USE_ID");
			CollHfPriorDialog_txtUseIdLogin = (String) request.get("CollHfPriorDialog_txtUseIdLogin");
			CollHfPriorDialog_txtUseIdName = (String) request.get("CollHfPriorDialog_txtUseIdName");
			CollHfPriorDialog_txtUserLock = (Timestamp) request.get("CollHfPriorDialog_txtUserLock");
			CollHfPriorDialog_txtHfDateFrom = (Date) request.get("CollHfPriorDialog_txtHfDateFrom");
			CollHfPriorDialog_txtHfDateUntil = (Date) request.get("CollHfPriorDialog_txtHfDateUntil");
			HF_COLL_HEAD_ID = (BigDecimal) request.get("HF_COLL_HEAD_ID");
			CollHfPriorDialog_txtHfDrawAmo = (BigDecimal) request.get("CollHfPriorDialog_txtHfDrawAmo");
			CollHfPriorDialog_txtAmountRef = (BigDecimal) request.get("CollHfPriorDialog_txtAmountRef");
			CUR_ID_REF = (BigDecimal) request.get("CUR_ID_REF");
			CollHfPriorDialog_txtHfCurIdRefCodeChar = (String) request.get("CollHfPriorDialog_txtHfCurIdRefCodeChar");
			CollHfPriorDialog_txtExcRatRef = (BigDecimal) request.get("CollHfPriorDialog_txtExcRatRef");
			CollHfPriorDialog_txtExcRatRefDate = (Date) request.get("CollHfPriorDialog_txtExcRatRefDate");
			CollHfPriorDialog_txtHfAvailAmoRef = (BigDecimal) request.get("CollHfPriorDialog_txtHfAvailAmoRef");
			CollHfPriorDialog_txtHfAvailDatRef = (Date) request.get("CollHfPriorDialog_txtHfAvailDatRef");
			CollHfPriorDialog_txtHfDrawAmoRef = (BigDecimal) request.get("CollHfPriorDialog_txtHfDrawAmoRef");
			CollHfPriorDialog_txtHfAvailBAmoRef = (BigDecimal) request.get("CollHfPriorDialog_txtHfAvailBAmoRef");
			CollHfPriorDialog_txtHfAvailBDatRef = (Date) request.get("CollHfPriorDialog_txtHfAvailBDatRef");
			CollHfPriorDialog_txtHfDrawBAmoRef = (BigDecimal) request.get("CollHfPriorDialog_txtHfDrawBAmoRef");
			CollHfPriorDialog_txtLandRegn = (String) request.get("CollHfPriorDialog_txtLandRegn");
			CollHfPriorDialog_txtLandRegnNew = (String) request.get("CollHfPriorDialog_txtLandRegnNew");
			CollHfPriorDialog_JUDGE_ID = (BigDecimal) request.get("CollHfPriorDialog_JUDGE_ID");
			CollHfPriorDialog_txtJudgeFname = (String) request.get("CollHfPriorDialog_txtJudgeFname");
			CollHfPriorDialog_txtJudgeLname = (String) request.get("CollHfPriorDialog_txtJudgeLname");
			CollHfPriorDialog_COURT_ID = (BigDecimal) request.get("CollHfPriorDialog_COURT_ID");
			CollHfPriorDialog_txtJudgeRegisterNo = (String) request.get("CollHfPriorDialog_txtJudgeRegisterNo");
			CollHfPriorDialog_txtCoCode = (String) request.get("CollHfPriorDialog_txtCoCode");
			CollHfPriorDialog_txtCoName = (String) request.get("CollHfPriorDialog_txtCoName");
			CollHfPriorDialog_txtHfAvailBAmo = (BigDecimal) request.get("CollHfPriorDialog_txtHfAvailBAmo");
			CollHfPriorDialog_txtHfAvailBDat = (Date) request.get("CollHfPriorDialog_txtHfAvailBDat");
			USER_LOCKCollHead = (Timestamp) request.get("USER_LOCKCollHead");
			CollHfPrior_HOW_COWER = (BigDecimal) request.get("CollHfPrior_HOW_COWER");
			CollHfPrior_txtHowCowerSCV = (String) request.get("CollHfPrior_txtHowCowerSCV");
			CollHfPrior_txtHowCowerSCD = (String) request.get("CollHfPrior_txtHowCowerSCD");
			CollHfPrior_txtIsPartAgreem = (String) request.get("CollHfPrior_txtIsPartAgreem");
			CollHFP_txtRecLop = (String) request.get("CollHFP_txtRecLop");
			CollHFP_txtDateToLop = (Date) request.get("CollHFP_txtDateToLop");
			CollHFP_txtDateRecLop = (Date) request.get("CollHFP_txtDateRecLop");
			CollHFP_txtVehConNum = (String) request.get("CollHFP_txtVehConNum");
			REG_INS = (BigDecimal) request.get("REG_INS");
			Kol_txtFrameAgr = (String) request.get("Kol_txtFrameAgr");
			fra_agr_id = (BigDecimal) request.get("fra_agr_id");
			HfPror_txtConcDate = (Date) request.get("HfPror_txtConcDate");
			HfPror_txtConcNum = (String) request.get("HfPror_txtConcNum");
			REG_COU_ID = (BigDecimal) request.get("REG_COU_ID");
			HfPror_txtRegPlace = (String) request.get("HfPror_txtRegPlace");
			CollHfPriorDialog_txtSyndicate = (String) request.get("CollHfPriorDialog_txtSyndicate");
			CollHfPriorDialog_txtHBORCredit = (String) request.get("CollHfPriorDialog_txtHBORCredit");
			CollHfPriorDialog_txtRBAArranger = (String) request.get("CollHfPriorDialog_txtRBAArranger");
			CollHfPriorDialog_txtAmountSyndicate = (BigDecimal) request.get("CollHfPriorDialog_txtAmountSyndicate");
			CollHfPriorDialog_txtRBASyndicatePartValue = (BigDecimal) request.get("CollHfPriorDialog_txtRBASyndicatePartValue");
			CollHfPriorDialog_txtOtherSyndicatePartValue = (BigDecimal) request.get("CollHfPriorDialog_txtOtherSyndicatePartValue");
			CollHfPriorDialog_txtRBASyndicateAmountValue = (BigDecimal) request.get("CollHfPriorDialog_txtRBASyndicateAmountValue");
			CollHfPriorDialog_txtOtherSyndicateAmountValue = (BigDecimal) request.get("CollHfPriorDialog_txtOtherSyndicateAmountValue");
			CollHfPriorDialog_txtSyndicatedComment = (String) request.get("CollHfPriorDialog_txtSyndicatedComment");
			OTHER_SYNDICATE_CUS_ID = (BigDecimal) request.get("OTHER_SYNDICATE_CUS_ID");
		}

		public Map getResponse() {
			response.put("COLL_HF_PRIOR_ID", COLL_HF_PRIOR_ID);
			response.put("Coll_txtThirdRightInNom", Coll_txtThirdRightInNom);
			response.put("Coll_txtHfsValue", Coll_txtHfsValue);
			response.put("RealEstate_txtRecLop", RealEstate_txtRecLop);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_ND_dsc", Kol_ND_dsc);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Kol_txtLastRBAMortgageAmount", Kol_txtLastRBAMortgageAmount);
			response.put("Kol_txtLastRBAMortgageDate", Kol_txtLastRBAMortgageDate);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CHPDPLAUSEMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal CUS_ID = null;

		// out args
		public String CollHfPriorDialog_txtColaCusIdUseRegisterNo = null;
		public String CollHfPriorDialog_txtColaCusIdUseOwnerName = null;

		public CHPDPLAUSEMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CUS_ID = (BigDecimal) request.get("CUS_ID");
		}

		public Map getResponse() {
			response.put("CollHfPriorDialog_txtColaCusIdUseRegisterNo", CollHfPriorDialog_txtColaCusIdUseRegisterNo);
			response.put("CollHfPriorDialog_txtColaCusIdUseOwnerName", CollHfPriorDialog_txtColaCusIdUseOwnerName);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CHPDDETMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COLL_HF_PRIOR_ID = null;
		public BigDecimal CUS_ID = null;

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
		public String CollHfPriorDialog_txtHfStatusDesc = null;
		public String CollHfPriorDialog_txtHfSpecStat = null;
		public String CollHfPriorDialog_txtHfSpecStatDesc = null;
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
		public BigDecimal CollHfPriorDialog_txtHfAvailAmoRef = null;
		public Date CollHfPriorDialog_txtHfAvailDatRef = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawAmoRef = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailBAmoRef = null;
		public Date CollHfPriorDialog_txtHfAvailBDatRef = null;
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
		public String PlRegisterNo = null;
		public String PlName = null;
		public BigDecimal CHPDB_COLL_HF_PRIOR_ID = null;
		public BigDecimal CHPDB_HF_TABLE_ID = null;
		public String CHPDB_txtHfTableSysCodeValue = null;
		public String CHPDB_txtHfTableSysCodeDesc = null;
		public BigDecimal CHPDB_HF_REF_ID = null;
		public String CHPDB_txtColNum = null;
		public BigDecimal CHPDB_HF_REC_LOP_ID = null;
		public String CHPDB_txtHfRecLopSysCodeValue = null;
		public String CHPDB_txtHfRecLopSysCodeDesc = null;
		public BigDecimal CHPDB_HF_OWN_CUS_ID = null;
		public String CHPDB_txtHfRegisterNo = null;
		public String CHPDB_txtHfOwnCode = null;
		public String CHPDB_txtHfOwnFname = null;
		public String CHPDB_txtHfOwnLname = null;
		public BigDecimal CHPDB_txtHfAmount = null;
		public BigDecimal CHPDB_HF_CUR_ID = null;
		public String CHPDB_txtHfCurIdCodeChar = null;
		public BigDecimal CHPDB_HF_HFC_ID = null;
		public String CHPDB_txtHfHfcSysCodeValue = null;
		public String CHPDB_txtHfHfcSysCodeDesc = null;
		public String CHPDB_txtHfPriority = null;
		public Date CHPDB_txtHfDateHfcFrom = null;
		public Date CHPDB_txtHfDateHfcUntil = null;
		public Date CHPDB_txtHfDateReciv = null;
		public String CHPDB_txtHfCourtDecis = null;
		public Date CHPDB_txtHfDateExtract = null;
		public BigDecimal CHPDB_HF_OFFI_LRD = null;
		public String CHPDB_txtHfOffildRegisterNo = null;
		public String CHPDB_txtHfOffildFname = null;
		public String CHPDB_txtHfOffildLname = null;
		public String CHPDB_txtHfNotaryAgr = null;
		public BigDecimal CHPDB_HF_NOTARY_PLACE_ID = null;
		public String CHPDB_txtHfNotaryPlace = null;
		public Date CHPDB_txtHfNotaryDate = null;
		public BigDecimal CHPDB_HF_NOTARY = null;
		public String CHPDB_txtHfNotaryRegisterNo = null;
		public String CHPDB_txtHfNotFname = null;
		public String CHPDB_txtHfNotLname = null;
		public BigDecimal CHPDB_txtHfAvailAmo = null;
		public Date CHPDB_txtHfAvailDat = null;
		public String CHPDB_txtHfStatus = null;
		public String CHPDB_txtHfStatusDesc = null;
		public String CHPDB_txtHfSpecStat = null;
		public String CHPDB_txtHfSpecStatDesc = null;
		public String CHPDB_txtHfAddData = null;
		public BigDecimal CHPDB_USE_OPEN_ID = null;
		public String CHPDB_txtUseOpenIdLogin = null;
		public String CHPDB_txtUseOpenIdName = null;
		public Timestamp CHPDB_txtOpeningTs = null;
		public BigDecimal CHPDB_USE_ID = null;
		public String CHPDB_txtUseIdLogin = null;
		public String CHPDB_txtUseIdName = null;
		public Timestamp CHPDB_txtUserLock = null;
		public Date CHPDB_txtHfDateFrom = null;
		public Date CHPDB_txtHfDateUntil = null;
		public BigDecimal CHPDB_HF_COLL_HEAD_ID = null;
		public BigDecimal CHPDB_txtHfDrawAmo = null;
		public BigDecimal CHPDB_txtAmountRef = null;
		public BigDecimal CHPDB_CUR_ID_REF = null;
		public String CHPDB_txtHfCurIdRefCodeChar = null;
		public BigDecimal CHPDB_txtExcRatRef = null;
		public Date CHPDB_txtExcRatRefDate = null;
		public BigDecimal CHPDB_txtHfAvailAmoRef = null;
		public Date CHPDB_txtHfAvailDatRef = null;
		public BigDecimal CHPDB_txtHfDrawAmoRef = null;
		public BigDecimal CHPDB_txtHfAvailBAmoRef = null;
		public Date CHPDB_txtHfAvailBDatRef = null;
		public BigDecimal CHPDB_txtHfDrawBAmoRef = null;
		public Date CHPDB_txtValDateTurn = null;
		public Date CHPDB_txtValDateProc = null;
		public BigDecimal CHPDB_txtHfDrawBAmo = null;
		public BigDecimal CHPDB_txtHfAvailBAmo = null;
		public Date CHPDB_txtHfAvailBDat = null;
		public BigDecimal CHPDB_CUS_ID = null;
		public String CHPDB_txtColaCusIdUseRegisterNo = null;
		public String CHPDB_txtColaCusIdUseOwnerName = null;
		public String CHPDB_txtLandRegn = null;
		public String CHPDB_txtLandRegnNew = null;
		public BigDecimal CHPDB_JUDGE_ID = null;
		public String CHPDB_txtJudgeRegisterNo = null;
		public String CHPDB_txtJudgeFname = null;
		public String CHPDB_txtJudgeLname = null;
		public BigDecimal CHPDB_COURT_ID = null;
		public String CHPDB_txtCoCode = null;
		public String CHPDB_txtCoName = null;
		public Timestamp CHPDB_USER_LOCKCollHead = null;
		public Timestamp CHPDB_USER_LOCKCollHfPrior = null;
		public BigDecimal CollHfPrior_HOW_COWER = null;
		public String CollHfPrior_txtHowCowerSCV = null;
		public String CollHfPrior_txtHowCowerSCD = null;
		public String CollHfPrior_txtIsPartAgreem = null;
		public BigDecimal CHPDB_HOW_COWER = null;
		public String CHPDB_txtHowCowerSCV = null;
		public String CHPDB_txtHowCowerSCD = null;
		public String CHPDB_txtIsPartAgreem = null;
		public String CollHFP_txtRecLop = null;
		public Date CollHFP_txtDateToLop = null;
		public Date CollHFP_txtDateRecLop = null;
		public String CHPDB_txtRecLop = null;
		public Date CHPDB_txtDateToLop = null;
		public Date CHPDB__txtDateRecLop = null;
		public String CollHFP_txtVehConNum = null;
		public String CHPDB_txtVehConNum = null;
		public String Hf_txtRegIns = null;
		public BigDecimal REG_INS = null;
		public String Kol_txtFrameAgr = null;
		public String Agr_txtAgrNo1 = null;
		public BigDecimal fra_agr_id = null;
		public Date HfPror_txtConcDate = null;
		public String HfPror_txtConcNum = null;
		public String HfPror_txtRegPlaceCou = null;
		public String HfPror_txtRegPlaceCouName = null;
		public BigDecimal REG_COU_ID = null;
		public String HfPror_txtRegPlace = null;
		public String HfPror_txtCourtCode = null;
		public String HfPror_txtCourtName = null;
		public String CollHfPriorDialog_txtSyndicate = null;
		public String CollHfPriorDialog_txtHBORCredit = null;
		public String CollHfPriorDialog_txtRBAArranger = null;
		public BigDecimal CollHfPriorDialog_txtAmountSyndicate = null;
		public BigDecimal CollHfPriorDialog_txtRBASyndicatePartValue = null;
		public BigDecimal CollHfPriorDialog_txtOtherSyndicatePartValue = null;
		public BigDecimal CollHfPriorDialog_txtRBASyndicateAmountValue = null;
		public BigDecimal CollHfPriorDialog_txtOtherSyndicateAmountValue = null;
		public String CollHfPriorDialog_txtSyndicatedComment = null;
		public BigDecimal OTHER_SYNDICATE_CUS_ID = null;
		public String CollHfPriorDialog_txtOtherSyndicateMB = null;
		public String CollHfPriorDialog_txtOtherSyndicateName = null;
		public BigDecimal CollHfPriorDialog_txtWcovh = null;
		public String CollHfPriorDialog_txtWcovhCur = null;
		public BigDecimal CollHfPriorDialog_txtEv = null;
		public String CollHfPriorDialog_txtEvCur = null;
		public Date CollHfPriorDialog_txtWcovhEvDate = null;

		public CHPDDETMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COLL_HF_PRIOR_ID = (BigDecimal) request.get("COLL_HF_PRIOR_ID");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
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
			response.put("CollHfPriorDialog_txtHfStatusDesc", CollHfPriorDialog_txtHfStatusDesc);
			response.put("CollHfPriorDialog_txtHfSpecStat", CollHfPriorDialog_txtHfSpecStat);
			response.put("CollHfPriorDialog_txtHfSpecStatDesc", CollHfPriorDialog_txtHfSpecStatDesc);
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
			response.put("CollHfPriorDialog_txtHfAvailAmoRef", CollHfPriorDialog_txtHfAvailAmoRef);
			response.put("CollHfPriorDialog_txtHfAvailDatRef", CollHfPriorDialog_txtHfAvailDatRef);
			response.put("CollHfPriorDialog_txtHfDrawAmoRef", CollHfPriorDialog_txtHfDrawAmoRef);
			response.put("CollHfPriorDialog_txtHfAvailBAmoRef", CollHfPriorDialog_txtHfAvailBAmoRef);
			response.put("CollHfPriorDialog_txtHfAvailBDatRef", CollHfPriorDialog_txtHfAvailBDatRef);
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
			response.put("PlRegisterNo", PlRegisterNo);
			response.put("PlName", PlName);
			response.put("CHPDB_COLL_HF_PRIOR_ID", CHPDB_COLL_HF_PRIOR_ID);
			response.put("CHPDB_HF_TABLE_ID", CHPDB_HF_TABLE_ID);
			response.put("CHPDB_txtHfTableSysCodeValue", CHPDB_txtHfTableSysCodeValue);
			response.put("CHPDB_txtHfTableSysCodeDesc", CHPDB_txtHfTableSysCodeDesc);
			response.put("CHPDB_HF_REF_ID", CHPDB_HF_REF_ID);
			response.put("CHPDB_txtColNum", CHPDB_txtColNum);
			response.put("CHPDB_HF_REC_LOP_ID", CHPDB_HF_REC_LOP_ID);
			response.put("CHPDB_txtHfRecLopSysCodeValue", CHPDB_txtHfRecLopSysCodeValue);
			response.put("CHPDB_txtHfRecLopSysCodeDesc", CHPDB_txtHfRecLopSysCodeDesc);
			response.put("CHPDB_HF_OWN_CUS_ID", CHPDB_HF_OWN_CUS_ID);
			response.put("CHPDB_txtHfRegisterNo", CHPDB_txtHfRegisterNo);
			response.put("CHPDB_txtHfOwnCode", CHPDB_txtHfOwnCode);
			response.put("CHPDB_txtHfOwnFname", CHPDB_txtHfOwnFname);
			response.put("CHPDB_txtHfOwnLname", CHPDB_txtHfOwnLname);
			response.put("CHPDB_txtHfAmount", CHPDB_txtHfAmount);
			response.put("CHPDB_HF_CUR_ID", CHPDB_HF_CUR_ID);
			response.put("CHPDB_txtHfCurIdCodeChar", CHPDB_txtHfCurIdCodeChar);
			response.put("CHPDB_HF_HFC_ID", CHPDB_HF_HFC_ID);
			response.put("CHPDB_txtHfHfcSysCodeValue", CHPDB_txtHfHfcSysCodeValue);
			response.put("CHPDB_txtHfHfcSysCodeDesc", CHPDB_txtHfHfcSysCodeDesc);
			response.put("CHPDB_txtHfPriority", CHPDB_txtHfPriority);
			response.put("CHPDB_txtHfDateHfcFrom", CHPDB_txtHfDateHfcFrom);
			response.put("CHPDB_txtHfDateHfcUntil", CHPDB_txtHfDateHfcUntil);
			response.put("CHPDB_txtHfDateReciv", CHPDB_txtHfDateReciv);
			response.put("CHPDB_txtHfCourtDecis", CHPDB_txtHfCourtDecis);
			response.put("CHPDB_txtHfDateExtract", CHPDB_txtHfDateExtract);
			response.put("CHPDB_HF_OFFI_LRD", CHPDB_HF_OFFI_LRD);
			response.put("CHPDB_txtHfOffildRegisterNo", CHPDB_txtHfOffildRegisterNo);
			response.put("CHPDB_txtHfOffildFname", CHPDB_txtHfOffildFname);
			response.put("CHPDB_txtHfOffildLname", CHPDB_txtHfOffildLname);
			response.put("CHPDB_txtHfNotaryAgr", CHPDB_txtHfNotaryAgr);
			response.put("CHPDB_HF_NOTARY_PLACE_ID", CHPDB_HF_NOTARY_PLACE_ID);
			response.put("CHPDB_txtHfNotaryPlace", CHPDB_txtHfNotaryPlace);
			response.put("CHPDB_txtHfNotaryDate", CHPDB_txtHfNotaryDate);
			response.put("CHPDB_HF_NOTARY", CHPDB_HF_NOTARY);
			response.put("CHPDB_txtHfNotaryRegisterNo", CHPDB_txtHfNotaryRegisterNo);
			response.put("CHPDB_txtHfNotFname", CHPDB_txtHfNotFname);
			response.put("CHPDB_txtHfNotLname", CHPDB_txtHfNotLname);
			response.put("CHPDB_txtHfAvailAmo", CHPDB_txtHfAvailAmo);
			response.put("CHPDB_txtHfAvailDat", CHPDB_txtHfAvailDat);
			response.put("CHPDB_txtHfStatus", CHPDB_txtHfStatus);
			response.put("CHPDB_txtHfStatusDesc", CHPDB_txtHfStatusDesc);
			response.put("CHPDB_txtHfSpecStat", CHPDB_txtHfSpecStat);
			response.put("CHPDB_txtHfSpecStatDesc", CHPDB_txtHfSpecStatDesc);
			response.put("CHPDB_txtHfAddData", CHPDB_txtHfAddData);
			response.put("CHPDB_USE_OPEN_ID", CHPDB_USE_OPEN_ID);
			response.put("CHPDB_txtUseOpenIdLogin", CHPDB_txtUseOpenIdLogin);
			response.put("CHPDB_txtUseOpenIdName", CHPDB_txtUseOpenIdName);
			response.put("CHPDB_txtOpeningTs", CHPDB_txtOpeningTs);
			response.put("CHPDB_USE_ID", CHPDB_USE_ID);
			response.put("CHPDB_txtUseIdLogin", CHPDB_txtUseIdLogin);
			response.put("CHPDB_txtUseIdName", CHPDB_txtUseIdName);
			response.put("CHPDB_txtUserLock", CHPDB_txtUserLock);
			response.put("CHPDB_txtHfDateFrom", CHPDB_txtHfDateFrom);
			response.put("CHPDB_txtHfDateUntil", CHPDB_txtHfDateUntil);
			response.put("CHPDB_HF_COLL_HEAD_ID", CHPDB_HF_COLL_HEAD_ID);
			response.put("CHPDB_txtHfDrawAmo", CHPDB_txtHfDrawAmo);
			response.put("CHPDB_txtAmountRef", CHPDB_txtAmountRef);
			response.put("CHPDB_CUR_ID_REF", CHPDB_CUR_ID_REF);
			response.put("CHPDB_txtHfCurIdRefCodeChar", CHPDB_txtHfCurIdRefCodeChar);
			response.put("CHPDB_txtExcRatRef", CHPDB_txtExcRatRef);
			response.put("CHPDB_txtExcRatRefDate", CHPDB_txtExcRatRefDate);
			response.put("CHPDB_txtHfAvailAmoRef", CHPDB_txtHfAvailAmoRef);
			response.put("CHPDB_txtHfAvailDatRef", CHPDB_txtHfAvailDatRef);
			response.put("CHPDB_txtHfDrawAmoRef", CHPDB_txtHfDrawAmoRef);
			response.put("CHPDB_txtHfAvailBAmoRef", CHPDB_txtHfAvailBAmoRef);
			response.put("CHPDB_txtHfAvailBDatRef", CHPDB_txtHfAvailBDatRef);
			response.put("CHPDB_txtHfDrawBAmoRef", CHPDB_txtHfDrawBAmoRef);
			response.put("CHPDB_txtValDateTurn", CHPDB_txtValDateTurn);
			response.put("CHPDB_txtValDateProc", CHPDB_txtValDateProc);
			response.put("CHPDB_txtHfDrawBAmo", CHPDB_txtHfDrawBAmo);
			response.put("CHPDB_txtHfAvailBAmo", CHPDB_txtHfAvailBAmo);
			response.put("CHPDB_txtHfAvailBDat", CHPDB_txtHfAvailBDat);
			response.put("CHPDB_CUS_ID", CHPDB_CUS_ID);
			response.put("CHPDB_txtColaCusIdUseRegisterNo", CHPDB_txtColaCusIdUseRegisterNo);
			response.put("CHPDB_txtColaCusIdUseOwnerName", CHPDB_txtColaCusIdUseOwnerName);
			response.put("CHPDB_txtLandRegn", CHPDB_txtLandRegn);
			response.put("CHPDB_txtLandRegnNew", CHPDB_txtLandRegnNew);
			response.put("CHPDB_JUDGE_ID", CHPDB_JUDGE_ID);
			response.put("CHPDB_txtJudgeRegisterNo", CHPDB_txtJudgeRegisterNo);
			response.put("CHPDB_txtJudgeFname", CHPDB_txtJudgeFname);
			response.put("CHPDB_txtJudgeLname", CHPDB_txtJudgeLname);
			response.put("CHPDB_COURT_ID", CHPDB_COURT_ID);
			response.put("CHPDB_txtCoCode", CHPDB_txtCoCode);
			response.put("CHPDB_txtCoName", CHPDB_txtCoName);
			response.put("CHPDB_USER_LOCKCollHead", CHPDB_USER_LOCKCollHead);
			response.put("CHPDB_USER_LOCKCollHfPrior", CHPDB_USER_LOCKCollHfPrior);
			response.put("CollHfPrior_HOW_COWER", CollHfPrior_HOW_COWER);
			response.put("CollHfPrior_txtHowCowerSCV", CollHfPrior_txtHowCowerSCV);
			response.put("CollHfPrior_txtHowCowerSCD", CollHfPrior_txtHowCowerSCD);
			response.put("CollHfPrior_txtIsPartAgreem", CollHfPrior_txtIsPartAgreem);
			response.put("CHPDB_HOW_COWER", CHPDB_HOW_COWER);
			response.put("CHPDB_txtHowCowerSCV", CHPDB_txtHowCowerSCV);
			response.put("CHPDB_txtHowCowerSCD", CHPDB_txtHowCowerSCD);
			response.put("CHPDB_txtIsPartAgreem", CHPDB_txtIsPartAgreem);
			response.put("CollHFP_txtRecLop", CollHFP_txtRecLop);
			response.put("CollHFP_txtDateToLop", CollHFP_txtDateToLop);
			response.put("CollHFP_txtDateRecLop", CollHFP_txtDateRecLop);
			response.put("CHPDB_txtRecLop", CHPDB_txtRecLop);
			response.put("CHPDB_txtDateToLop", CHPDB_txtDateToLop);
			response.put("CHPDB__txtDateRecLop", CHPDB__txtDateRecLop);
			response.put("CollHFP_txtVehConNum", CollHFP_txtVehConNum);
			response.put("CHPDB_txtVehConNum", CHPDB_txtVehConNum);
			response.put("Hf_txtRegIns", Hf_txtRegIns);
			response.put("REG_INS", REG_INS);
			response.put("Kol_txtFrameAgr", Kol_txtFrameAgr);
			response.put("Agr_txtAgrNo1", Agr_txtAgrNo1);
			response.put("fra_agr_id", fra_agr_id);
			response.put("HfPror_txtConcDate", HfPror_txtConcDate);
			response.put("HfPror_txtConcNum", HfPror_txtConcNum);
			response.put("HfPror_txtRegPlaceCou", HfPror_txtRegPlaceCou);
			response.put("HfPror_txtRegPlaceCouName", HfPror_txtRegPlaceCouName);
			response.put("REG_COU_ID", REG_COU_ID);
			response.put("HfPror_txtRegPlace", HfPror_txtRegPlace);
			response.put("HfPror_txtCourtCode", HfPror_txtCourtCode);
			response.put("HfPror_txtCourtName", HfPror_txtCourtName);
			response.put("CollHfPriorDialog_txtSyndicate", CollHfPriorDialog_txtSyndicate);
			response.put("CollHfPriorDialog_txtHBORCredit", CollHfPriorDialog_txtHBORCredit);
			response.put("CollHfPriorDialog_txtRBAArranger", CollHfPriorDialog_txtRBAArranger);
			response.put("CollHfPriorDialog_txtAmountSyndicate", CollHfPriorDialog_txtAmountSyndicate);
			response.put("CollHfPriorDialog_txtRBASyndicatePartValue", CollHfPriorDialog_txtRBASyndicatePartValue);
			response.put("CollHfPriorDialog_txtOtherSyndicatePartValue", CollHfPriorDialog_txtOtherSyndicatePartValue);
			response.put("CollHfPriorDialog_txtRBASyndicateAmountValue", CollHfPriorDialog_txtRBASyndicateAmountValue);
			response.put("CollHfPriorDialog_txtOtherSyndicateAmountValue", CollHfPriorDialog_txtOtherSyndicateAmountValue);
			response.put("CollHfPriorDialog_txtSyndicatedComment", CollHfPriorDialog_txtSyndicatedComment);
			response.put("OTHER_SYNDICATE_CUS_ID", OTHER_SYNDICATE_CUS_ID);
			response.put("CollHfPriorDialog_txtOtherSyndicateMB", CollHfPriorDialog_txtOtherSyndicateMB);
			response.put("CollHfPriorDialog_txtOtherSyndicateName", CollHfPriorDialog_txtOtherSyndicateName);
			response.put("CollHfPriorDialog_txtWcovh", CollHfPriorDialog_txtWcovh);
			response.put("CollHfPriorDialog_txtWcovhCur", CollHfPriorDialog_txtWcovhCur);
			response.put("CollHfPriorDialog_txtEv", CollHfPriorDialog_txtEv);
			response.put("CollHfPriorDialog_txtEvCur", CollHfPriorDialog_txtEvCur);
			response.put("CollHfPriorDialog_txtWcovhEvDate", CollHfPriorDialog_txtWcovhEvDate);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CHPDUPDMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COLL_HF_PRIOR_ID = null;
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
		public String CollHfPriorDialog_txtHfStatusDesc = null;
		public String CollHfPriorDialog_txtHfSpecStat = null;
		public String CollHfPriorDialog_txtHfSpecStatDesc = null;
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
		public BigDecimal CollHfPriorDialog_txtHfAvailAmoRef = null;
		public Date CollHfPriorDialog_txtHfAvailDatRef = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawAmoRef = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailBAmoRef = null;
		public Date CollHfPriorDialog_txtHfAvailBDatRef = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawBAmoRef = null;
		public String CollHfPriorDialog_txtLandRegn = null;
		public String CollHfPriorDialog_txtLandRegnNew = null;
		public BigDecimal CollHfPriorDialog_JUDGE_ID = null;
		public String CollHfPriorDialog_txtJudgeFname = null;
		public String CollHfPriorDialog_txtJudgeLname = null;
		public BigDecimal CollHfPriorDialog_COURT_ID = null;
		public String CollHfPriorDialog_txtJudgeRegisterNo = null;
		public String CollHfPriorDialog_txtCoCode = null;
		public String CollHfPriorDialog_txtCoName = null;
		public BigDecimal CollHfPriorDialog_txtHfAvailBAmo = null;
		public Date CollHfPriorDialog_txtHfAvailBDat = null;
		public Timestamp USER_LOCKCollHead = null;
		public Timestamp USER_LOCKCollHfPrior = null;
		public BigDecimal CollHfPriorDialog_txtHfDrawBAmo = null;
		public BigDecimal CollHfPrior_HOW_COWER = null;
		public String CollHfPrior_txtHowCowerSCV = null;
		public String CollHfPrior_txtHowCowerSCD = null;
		public String CollHfPrior_txtIsPartAgreem = null;
		public BigDecimal CHPDB_HOW_COWER = null;
		public String CHPDB_txtHowCowerSCV = null;
		public String CHPDB_txtHowCowerSCD = null;
		public String CHPDB_txtIsPartAgreem = null;
		public BigDecimal CHPDB_txtHfAmount = null;
		public String CollHFP_txtRecLop = null;
		public Date CollHFP_txtDateToLop = null;
		public Date CollHFP_txtDateRecLop = null;
		public String CollHFP_txtVehConNum = null;
		public BigDecimal REG_INS = null;
		public String Kol_txtFrameAgr = null;
		public BigDecimal fra_agr_id = null;
		public Date HfPror_txtConcDate = null;
		public String HfPror_txtConcNum = null;
		public BigDecimal REG_COU_ID = null;
		public String HfPror_txtRegPlace = null;
		public String CollHfPriorDialog_txtSyndicate = null;
		public String CollHfPriorDialog_txtHBORCredit = null;
		public String CollHfPriorDialog_txtRBAArranger = null;
		public BigDecimal CollHfPriorDialog_txtAmountSyndicate = null;
		public BigDecimal CollHfPriorDialog_txtRBASyndicatePartValue = null;
		public BigDecimal CollHfPriorDialog_txtOtherSyndicatePartValue = null;
		public BigDecimal CollHfPriorDialog_txtRBASyndicateAmountValue = null;
		public BigDecimal CollHfPriorDialog_txtOtherSyndicateAmountValue = null;
		public String CollHfPriorDialog_txtSyndicatedComment = null;
		public BigDecimal OTHER_SYNDICATE_CUS_ID = null;

		// out args
		public BigDecimal Coll_txtThirdRightInNom = null;
		public BigDecimal Coll_txtHfsValue = null;
		public String RealEstate_txtRecLop = null;
		public String Kol_ND = null;
		public String Kol_B2 = null;
		public String Kol_HNB = null;
		public String Kol_B2IRB = null;
		public String Kol_ND_dsc = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB_dsc = null;
		public BigDecimal Kol_txtLastRBAMortgageAmount = null;
		public Date Kol_txtLastRBAMortgageDate = null;

		public CHPDUPDMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COLL_HF_PRIOR_ID = (BigDecimal) request.get("COLL_HF_PRIOR_ID");
			HF_TABLE_ID = (BigDecimal) request.get("HF_TABLE_ID");
			CollHfPriorDialog_txtHfTableSysCodeValue = (String) request.get("CollHfPriorDialog_txtHfTableSysCodeValue");
			CollHfPriorDialog_txtHfTableSysCodeDesc = (String) request.get("CollHfPriorDialog_txtHfTableSysCodeDesc");
			HF_REF_ID = (BigDecimal) request.get("HF_REF_ID");
			CollHfPriorDialog_txtColNum = (String) request.get("CollHfPriorDialog_txtColNum");
			HF_REC_LOP_ID = (BigDecimal) request.get("HF_REC_LOP_ID");
			CollHfPriorDialog_txtHfRecLopSysCodeValue = (String) request.get("CollHfPriorDialog_txtHfRecLopSysCodeValue");
			CollHfPriorDialog_txtHfRecLopSysCodeDesc = (String) request.get("CollHfPriorDialog_txtHfRecLopSysCodeDesc");
			HF_OWN_CUS_ID = (BigDecimal) request.get("HF_OWN_CUS_ID");
			CollHfPriorDialog_txtHfRegisterNo = (String) request.get("CollHfPriorDialog_txtHfRegisterNo");
			CollHfPriorDialog_txtHfOwnCode = (String) request.get("CollHfPriorDialog_txtHfOwnCode");
			CollHfPriorDialog_txtHfOwnFname = (String) request.get("CollHfPriorDialog_txtHfOwnFname");
			CollHfPriorDialog_txtHfOwnLname = (String) request.get("CollHfPriorDialog_txtHfOwnLname");
			CollHfPriorDialog_txtHfAmount = (BigDecimal) request.get("CollHfPriorDialog_txtHfAmount");
			HF_CUR_ID = (BigDecimal) request.get("HF_CUR_ID");
			CollHfPriorDialog_txtHfCurIdCodeChar = (String) request.get("CollHfPriorDialog_txtHfCurIdCodeChar");
			HF_HFC_ID = (BigDecimal) request.get("HF_HFC_ID");
			CollHfPriorDialog_txtHfHfcSysCodeValue = (String) request.get("CollHfPriorDialog_txtHfHfcSysCodeValue");
			CollHfPriorDialog_txtHfHfcSysCodeDesc = (String) request.get("CollHfPriorDialog_txtHfHfcSysCodeDesc");
			CollHfPriorDialog_txtHfPriority = (String) request.get("CollHfPriorDialog_txtHfPriority");
			CollHfPriorDialog_txtHfDateHfcFrom = (Date) request.get("CollHfPriorDialog_txtHfDateHfcFrom");
			CollHfPriorDialog_txtHfDateHfcUntil = (Date) request.get("CollHfPriorDialog_txtHfDateHfcUntil");
			CollHfPriorDialog_txtHfDateReciv = (Date) request.get("CollHfPriorDialog_txtHfDateReciv");
			CollHfPriorDialog_txtHfCourtDecis = (String) request.get("CollHfPriorDialog_txtHfCourtDecis");
			CollHfPriorDialog_txtHfDateExtract = (Date) request.get("CollHfPriorDialog_txtHfDateExtract");
			HF_OFFI_LRD = (BigDecimal) request.get("HF_OFFI_LRD");
			CollHfPriorDialog_txtHfOffildRegisterNo = (String) request.get("CollHfPriorDialog_txtHfOffildRegisterNo");
			CollHfPriorDialog_txtHfOffildFname = (String) request.get("CollHfPriorDialog_txtHfOffildFname");
			CollHfPriorDialog_txtHfOffildLname = (String) request.get("CollHfPriorDialog_txtHfOffildLname");
			CollHfPriorDialog_txtHfNotaryAgr = (String) request.get("CollHfPriorDialog_txtHfNotaryAgr");
			HF_NOTARY_PLACE_ID = (BigDecimal) request.get("HF_NOTARY_PLACE_ID");
			CollHfPriorDialog_txtHfNotaryPlace = (String) request.get("CollHfPriorDialog_txtHfNotaryPlace");
			CollHfPriorDialog_txtHfNotaryDate = (Date) request.get("CollHfPriorDialog_txtHfNotaryDate");
			HF_NOTARY = (BigDecimal) request.get("HF_NOTARY");
			CollHfPriorDialog_txtHfNotaryRegisterNo = (String) request.get("CollHfPriorDialog_txtHfNotaryRegisterNo");
			CollHfPriorDialog_txtHfNotFname = (String) request.get("CollHfPriorDialog_txtHfNotFname");
			CollHfPriorDialog_txtHfNotLname = (String) request.get("CollHfPriorDialog_txtHfNotLname");
			CollHfPriorDialog_txtHfAvailAmo = (BigDecimal) request.get("CollHfPriorDialog_txtHfAvailAmo");
			CollHfPriorDialog_txtHfAvailDat = (Date) request.get("CollHfPriorDialog_txtHfAvailDat");
			CollHfPriorDialog_txtHfStatus = (String) request.get("CollHfPriorDialog_txtHfStatus");
			CollHfPriorDialog_txtHfStatusDesc = (String) request.get("CollHfPriorDialog_txtHfStatusDesc");
			CollHfPriorDialog_txtHfSpecStat = (String) request.get("CollHfPriorDialog_txtHfSpecStat");
			CollHfPriorDialog_txtHfSpecStatDesc = (String) request.get("CollHfPriorDialog_txtHfSpecStatDesc");
			CollHfPriorDialog_txtHfAddData = (String) request.get("CollHfPriorDialog_txtHfAddData");
			USE_OPEN_ID = (BigDecimal) request.get("USE_OPEN_ID");
			CollHfPriorDialog_txtUseOpenIdLogin = (String) request.get("CollHfPriorDialog_txtUseOpenIdLogin");
			CollHfPriorDialog_txtUseOpenIdName = (String) request.get("CollHfPriorDialog_txtUseOpenIdName");
			CollHfPriorDialog_txtOpeningTs = (Timestamp) request.get("CollHfPriorDialog_txtOpeningTs");
			USE_ID = (BigDecimal) request.get("USE_ID");
			CollHfPriorDialog_txtUseIdLogin = (String) request.get("CollHfPriorDialog_txtUseIdLogin");
			CollHfPriorDialog_txtUseIdName = (String) request.get("CollHfPriorDialog_txtUseIdName");
			CollHfPriorDialog_txtUserLock = (Timestamp) request.get("CollHfPriorDialog_txtUserLock");
			CollHfPriorDialog_txtHfDateFrom = (Date) request.get("CollHfPriorDialog_txtHfDateFrom");
			CollHfPriorDialog_txtHfDateUntil = (Date) request.get("CollHfPriorDialog_txtHfDateUntil");
			HF_COLL_HEAD_ID = (BigDecimal) request.get("HF_COLL_HEAD_ID");
			CollHfPriorDialog_txtHfDrawAmo = (BigDecimal) request.get("CollHfPriorDialog_txtHfDrawAmo");
			CollHfPriorDialog_txtAmountRef = (BigDecimal) request.get("CollHfPriorDialog_txtAmountRef");
			CUR_ID_REF = (BigDecimal) request.get("CUR_ID_REF");
			CollHfPriorDialog_txtHfCurIdRefCodeChar = (String) request.get("CollHfPriorDialog_txtHfCurIdRefCodeChar");
			CollHfPriorDialog_txtExcRatRef = (BigDecimal) request.get("CollHfPriorDialog_txtExcRatRef");
			CollHfPriorDialog_txtExcRatRefDate = (Date) request.get("CollHfPriorDialog_txtExcRatRefDate");
			CollHfPriorDialog_txtHfAvailAmoRef = (BigDecimal) request.get("CollHfPriorDialog_txtHfAvailAmoRef");
			CollHfPriorDialog_txtHfAvailDatRef = (Date) request.get("CollHfPriorDialog_txtHfAvailDatRef");
			CollHfPriorDialog_txtHfDrawAmoRef = (BigDecimal) request.get("CollHfPriorDialog_txtHfDrawAmoRef");
			CollHfPriorDialog_txtHfAvailBAmoRef = (BigDecimal) request.get("CollHfPriorDialog_txtHfAvailBAmoRef");
			CollHfPriorDialog_txtHfAvailBDatRef = (Date) request.get("CollHfPriorDialog_txtHfAvailBDatRef");
			CollHfPriorDialog_txtHfDrawBAmoRef = (BigDecimal) request.get("CollHfPriorDialog_txtHfDrawBAmoRef");
			CollHfPriorDialog_txtLandRegn = (String) request.get("CollHfPriorDialog_txtLandRegn");
			CollHfPriorDialog_txtLandRegnNew = (String) request.get("CollHfPriorDialog_txtLandRegnNew");
			CollHfPriorDialog_JUDGE_ID = (BigDecimal) request.get("CollHfPriorDialog_JUDGE_ID");
			CollHfPriorDialog_txtJudgeFname = (String) request.get("CollHfPriorDialog_txtJudgeFname");
			CollHfPriorDialog_txtJudgeLname = (String) request.get("CollHfPriorDialog_txtJudgeLname");
			CollHfPriorDialog_COURT_ID = (BigDecimal) request.get("CollHfPriorDialog_COURT_ID");
			CollHfPriorDialog_txtJudgeRegisterNo = (String) request.get("CollHfPriorDialog_txtJudgeRegisterNo");
			CollHfPriorDialog_txtCoCode = (String) request.get("CollHfPriorDialog_txtCoCode");
			CollHfPriorDialog_txtCoName = (String) request.get("CollHfPriorDialog_txtCoName");
			CollHfPriorDialog_txtHfAvailBAmo = (BigDecimal) request.get("CollHfPriorDialog_txtHfAvailBAmo");
			CollHfPriorDialog_txtHfAvailBDat = (Date) request.get("CollHfPriorDialog_txtHfAvailBDat");
			USER_LOCKCollHead = (Timestamp) request.get("USER_LOCKCollHead");
			USER_LOCKCollHfPrior = (Timestamp) request.get("USER_LOCKCollHfPrior");
			CollHfPriorDialog_txtHfDrawBAmo = (BigDecimal) request.get("CollHfPriorDialog_txtHfDrawBAmo");
			CollHfPrior_HOW_COWER = (BigDecimal) request.get("CollHfPrior_HOW_COWER");
			CollHfPrior_txtHowCowerSCV = (String) request.get("CollHfPrior_txtHowCowerSCV");
			CollHfPrior_txtHowCowerSCD = (String) request.get("CollHfPrior_txtHowCowerSCD");
			CollHfPrior_txtIsPartAgreem = (String) request.get("CollHfPrior_txtIsPartAgreem");
			CHPDB_HOW_COWER = (BigDecimal) request.get("CHPDB_HOW_COWER");
			CHPDB_txtHowCowerSCV = (String) request.get("CHPDB_txtHowCowerSCV");
			CHPDB_txtHowCowerSCD = (String) request.get("CHPDB_txtHowCowerSCD");
			CHPDB_txtIsPartAgreem = (String) request.get("CHPDB_txtIsPartAgreem");
			CHPDB_txtHfAmount = (BigDecimal) request.get("CHPDB_txtHfAmount");
			CollHFP_txtRecLop = (String) request.get("CollHFP_txtRecLop");
			CollHFP_txtDateToLop = (Date) request.get("CollHFP_txtDateToLop");
			CollHFP_txtDateRecLop = (Date) request.get("CollHFP_txtDateRecLop");
			CollHFP_txtVehConNum = (String) request.get("CollHFP_txtVehConNum");
			REG_INS = (BigDecimal) request.get("REG_INS");
			Kol_txtFrameAgr = (String) request.get("Kol_txtFrameAgr");
			fra_agr_id = (BigDecimal) request.get("fra_agr_id");
			HfPror_txtConcDate = (Date) request.get("HfPror_txtConcDate");
			HfPror_txtConcNum = (String) request.get("HfPror_txtConcNum");
			REG_COU_ID = (BigDecimal) request.get("REG_COU_ID");
			HfPror_txtRegPlace = (String) request.get("HfPror_txtRegPlace");
			CollHfPriorDialog_txtSyndicate = (String) request.get("CollHfPriorDialog_txtSyndicate");
			CollHfPriorDialog_txtHBORCredit = (String) request.get("CollHfPriorDialog_txtHBORCredit");
			CollHfPriorDialog_txtRBAArranger = (String) request.get("CollHfPriorDialog_txtRBAArranger");
			CollHfPriorDialog_txtAmountSyndicate = (BigDecimal) request.get("CollHfPriorDialog_txtAmountSyndicate");
			CollHfPriorDialog_txtRBASyndicatePartValue = (BigDecimal) request.get("CollHfPriorDialog_txtRBASyndicatePartValue");
			CollHfPriorDialog_txtOtherSyndicatePartValue = (BigDecimal) request.get("CollHfPriorDialog_txtOtherSyndicatePartValue");
			CollHfPriorDialog_txtRBASyndicateAmountValue = (BigDecimal) request.get("CollHfPriorDialog_txtRBASyndicateAmountValue");
			CollHfPriorDialog_txtOtherSyndicateAmountValue = (BigDecimal) request.get("CollHfPriorDialog_txtOtherSyndicateAmountValue");
			CollHfPriorDialog_txtSyndicatedComment = (String) request.get("CollHfPriorDialog_txtSyndicatedComment");
			OTHER_SYNDICATE_CUS_ID = (BigDecimal) request.get("OTHER_SYNDICATE_CUS_ID");
		}

		public Map getResponse() {
			response.put("Coll_txtThirdRightInNom", Coll_txtThirdRightInNom);
			response.put("Coll_txtHfsValue", Coll_txtHfsValue);
			response.put("RealEstate_txtRecLop", RealEstate_txtRecLop);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_ND_dsc", Kol_ND_dsc);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Kol_txtLastRBAMortgageAmount", Kol_txtLastRBAMortgageAmount);
			response.put("Kol_txtLastRBAMortgageDate", Kol_txtLastRBAMortgageDate);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CHPDUSERLOCKM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal HF_COLL_HEAD_ID = null;

		// out args
		public Timestamp USER_LOCKCollHead = null;

		public CHPDUSERLOCKM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			HF_COLL_HEAD_ID = (BigDecimal) request.get("HF_COLL_HEAD_ID");
		}

		public Map getResponse() {
			response.put("USER_LOCKCollHead", USER_LOCKCollHead);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CHPLOANMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COLL_HF_PRIOR_ID = null;
		public String Kol_txtFrameAgr = null;
		public BigDecimal fra_agr_id = null;

		// out args

		public CHPLOANMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COLL_HF_PRIOR_ID = (BigDecimal) request.get("COLL_HF_PRIOR_ID");
			Kol_txtFrameAgr = (String) request.get("Kol_txtFrameAgr");
			fra_agr_id = (BigDecimal) request.get("fra_agr_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class MRTGDELMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COLL_HF_PRIOR_ID = null;
		public String CollHfPriorDialog_txtHfRecLopSysCodeValue = null;
		public Timestamp USER_LOCKCollHfPrior = null;

		// out args
		public String RealEstate_txtRecLop = null;
		public String Kol_ND = null;
		public String Kol_B2 = null;
		public String Kol_HNB = null;
		public String Kol_B2IRB = null;
		public String Kol_ND_dsc = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB_dsc = null;
		public BigDecimal Coll_txtThirdRightInNom = null;
		public BigDecimal Coll_txtHfsValue = null;
		public Date CollHfPriorDialog_txtHfDateHfcUntil = null;
		public BigDecimal CollHfPriorDialog_txtHfAmount = null;

		public MRTGDELMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COLL_HF_PRIOR_ID = (BigDecimal) request.get("COLL_HF_PRIOR_ID");
			CollHfPriorDialog_txtHfRecLopSysCodeValue = (String) request.get("CollHfPriorDialog_txtHfRecLopSysCodeValue");
			USER_LOCKCollHfPrior = (Timestamp) request.get("USER_LOCKCollHfPrior");
		}

		public Map getResponse() {
			response.put("RealEstate_txtRecLop", RealEstate_txtRecLop);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_ND_dsc", Kol_ND_dsc);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Coll_txtThirdRightInNom", Coll_txtThirdRightInNom);
			response.put("Coll_txtHfsValue", Coll_txtHfsValue);
			response.put("CollHfPriorDialog_txtHfDateHfcUntil", CollHfPriorDialog_txtHfDateHfcUntil);
			response.put("CollHfPriorDialog_txtHfAmount", CollHfPriorDialog_txtHfAmount);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}