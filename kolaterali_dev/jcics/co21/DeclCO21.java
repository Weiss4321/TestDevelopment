package hr.vestigo.modules.collateral.jcics.co21;

import java.sql.Timestamp;
import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import hr.vestigo.framework.common.celltable.CellTable;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO21 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co21/DeclCO21.java,v 1.21 2016/12/05 09:54:29 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCO21() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public String ScreenEntryParam = null;
	public BigDecimal use_id = null;
	public BigDecimal org_uni_id = null;
	public String ColWorkList_txtScreenContext = null;
	public java.util.HashMap listSelected = null;

	// out args
	public CellTable tblColWorkListCellTable = null;
	public TableData tblColWorkList = new TableData();

	// inner classes
	public COLLWORKLISTSTOP collworkliststop = null;
	public COLLWORKLISTSNDVER collworklistsndver = null;
	public COLLVERIFICATION collverification = null;
	public COLLAUTHORIZATION collauthorization = null;
	public COLLSNDARHIVA collsndarhiva = null;
	public COLLSNDBACK collsndback = null;
	public COLLSNDBACKALL collsndbackall = null;
	public KOLATERALQBEMAP kolateralqbemap = null;
	public COLLCOCHGHISTMAPPING collcochghistmapping = null;
	public REESTCOCHGHISTM reestcochghistm = null;
	public KOLATERALRETURNDEACT kolateralreturndeact = null;
	public COLLOWNERBANKMAPPING collownerbankmapping = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		ScreenEntryParam = (String) request.get("ScreenEntryParam");
		use_id = (BigDecimal) request.get("use_id");
		org_uni_id = (BigDecimal) request.get("org_uni_id");
		ColWorkList_txtScreenContext = (String) request.get("ColWorkList_txtScreenContext");
		listSelected = (java.util.HashMap) request.get("listSelected");
		tblColWorkListCellTable = (CellTable) request.get("CellTableDefinition");
	}

	public Map getResponse() {
		response.put("tblColWorkListCellTable", tblColWorkListCellTable);
		response.put("tblColWorkList", tblColWorkList);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollWorkListStop")) {
			collworkliststop = new COLLWORKLISTSTOP(request);
			return collworkliststop;
		} else if(mapping.equals("CollWorkListSndVer")) {
			collworklistsndver = new COLLWORKLISTSNDVER(request);
			return collworklistsndver;
		} else if(mapping.equals("CollVerification")) {
			collverification = new COLLVERIFICATION(request);
			return collverification;
		} else if(mapping.equals("CollAuthorization")) {
			collauthorization = new COLLAUTHORIZATION(request);
			return collauthorization;
		} else if(mapping.equals("CollSndArhiva")) {
			collsndarhiva = new COLLSNDARHIVA(request);
			return collsndarhiva;
		} else if(mapping.equals("CollSndBack")) {
			collsndback = new COLLSNDBACK(request);
			return collsndback;
		} else if(mapping.equals("CollSndBackAll")) {
			collsndbackall = new COLLSNDBACKALL(request);
			return collsndbackall;
		} else if(mapping.equals("KolateralQBEmap")) {
			kolateralqbemap = new KOLATERALQBEMAP(request);
			return kolateralqbemap;
		} else if(mapping.equals("CollCoChgHistMapping")) {
			collcochghistmapping = new COLLCOCHGHISTMAPPING(request);
			return collcochghistmapping;
		} else if(mapping.equals("ReEstCoChgHistM")) {
			reestcochghistm = new REESTCOCHGHISTM(request);
			return reestcochghistm;
		} else if(mapping.equals("KolateralReturnDeact")) {
			kolateralreturndeact = new KOLATERALRETURNDEACT(request);
			return kolateralreturndeact;
		} else if(mapping.equals("CollOwnerBankMapping")) {
			collownerbankmapping = new COLLOWNERBANKMAPPING(request);
			return collownerbankmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLWORKLISTSTOP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String CollListQ_txaCmnt = null;
		public String source_list = null;
		public String target_list = null;
		public String proc_status = null;
		public String action_type = null;

		// out args
		public String poruka = null;

		public COLLWORKLISTSTOP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			CollListQ_txaCmnt = (String) request.get("CollListQ_txaCmnt");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			proc_status = (String) request.get("proc_status");
			action_type = (String) request.get("action_type");
		}

		public Map getResponse() {
			response.put("poruka", poruka);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLWORKLISTSNDVER implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String source_list = null;
		public String target_list = null;
		public String proc_status = null;
		public String action_type = null;

		// out args
		public String poruka = null;

		public COLLWORKLISTSNDVER(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			proc_status = (String) request.get("proc_status");
			action_type = (String) request.get("action_type");
		}

		public Map getResponse() {
			response.put("poruka", poruka);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLVERIFICATION implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String source_list = null;
		public String target_list = null;
		public String proc_status = null;
		public String action_type = null;

		// out args
		public String poruka = null;

		public COLLVERIFICATION(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			proc_status = (String) request.get("proc_status");
			action_type = (String) request.get("action_type");
		}

		public Map getResponse() {
			response.put("poruka", poruka);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLAUTHORIZATION implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String source_list = null;
		public String target_list = null;
		public String proc_status = null;
		public String action_type = null;

		// out args
		public String poruka = null;

		public COLLAUTHORIZATION(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			proc_status = (String) request.get("proc_status");
			action_type = (String) request.get("action_type");
		}

		public Map getResponse() {
			response.put("poruka", poruka);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLSNDARHIVA implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String source_list = null;
		public String target_list = null;
		public String proc_status = null;
		public String action_type = null;

		// out args
		public String poruka = null;

		public COLLSNDARHIVA(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			proc_status = (String) request.get("proc_status");
			action_type = (String) request.get("action_type");
		}

		public Map getResponse() {
			response.put("poruka", poruka);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLSNDBACK implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String CollListQ_txaCmnt = null;
		public String source_list = null;
		public String target_list = null;
		public String proc_status = null;
		public String action_type = null;
		public String workflow_indic = null;
		public String source_status = null;
		public BigDecimal org_uni_id_asg = null;
		public BigDecimal use_id_asg = null;

		// out args
		public String poruka = null;

		public COLLSNDBACK(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			CollListQ_txaCmnt = (String) request.get("CollListQ_txaCmnt");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			proc_status = (String) request.get("proc_status");
			action_type = (String) request.get("action_type");
			workflow_indic = (String) request.get("workflow_indic");
			source_status = (String) request.get("source_status");
			org_uni_id_asg = (BigDecimal) request.get("org_uni_id_asg");
			use_id_asg = (BigDecimal) request.get("use_id_asg");
		}

		public Map getResponse() {
			response.put("poruka", poruka);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLSNDBACKALL implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String CollListQ_txaCmnt = null;
		public String source_list = null;
		public String target_list = null;
		public String proc_status = null;
		public String action_type = null;
		public String workflow_indic = null;
		public String source_status = null;
		public BigDecimal org_uni_id_asg = null;
		public BigDecimal use_id_asg = null;
		public TableData tblColWorkList = new TableData();
		public java.util.HashMap listSelected = null;

		// out args
		public String poruka = null;

		public COLLSNDBACKALL(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			CollListQ_txaCmnt = (String) request.get("CollListQ_txaCmnt");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			proc_status = (String) request.get("proc_status");
			action_type = (String) request.get("action_type");
			workflow_indic = (String) request.get("workflow_indic");
			source_status = (String) request.get("source_status");
			org_uni_id_asg = (BigDecimal) request.get("org_uni_id_asg");
			use_id_asg = (BigDecimal) request.get("use_id_asg");
			tblColWorkList = (TableData) request.get("tblColWorkList");
			listSelected = (java.util.HashMap) request.get("listSelected");
		}

		public Map getResponse() {
			response.put("poruka", poruka);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class KOLATERALQBEMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal org_uni_id_qbe = null;
		public BigDecimal use_id_qbe = null;
		public BigDecimal cus_id_qbe = null;
		public String Kolateral_txtColNumQBE = null;
		public String Kolateral_txtAccNoQBE = null;
		public String Kolateral_txtNoRqQBE = null;
		public Date Kolateral_txtDateFromQBE = null;
		public Date Kolateral_txtDateUntilQBE = null;
		public String proc_status_QBE = null;
		public BigDecimal RealEstate_OwnerCUS_ID = null;
		public BigDecimal RealEstate_QBE_CADA_MUNC = null;
		public String RealEstate_txtCoown = null;
		public String RealEstate_txtQbeLandSub = null;
		public String RealEstate_txtQbeRealEstLandRegn = null;
		public String Vehi_txtVINQBE = null;
		public String Coll_txtDepAccQBE = null;
		public String Kolateral_txtBrojPoliceQBE = null;
		public BigDecimal col_cat_id_qbe = null;
		public String KolQBE_txtClientType = null;
		public BigDecimal issuer_cus_id_qbe = null;
		public String ISIN_txtQBE = null;
		public String ContractNo_txtQBE = null;
		public String GuarIzdavatelj_txtQBE = null;
		public String GuarIzdavateljName_txtQBE = null;
		public BigDecimal guar_issuer_cus_id_qbe = null;
		public java.util.HashMap listSelected = null;

		// out args
		public TableData tblColWorkList = new TableData();
		public CellTable tblColWorkListCellTable = null;

		public KOLATERALQBEMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			org_uni_id_qbe = (BigDecimal) request.get("org_uni_id_qbe");
			use_id_qbe = (BigDecimal) request.get("use_id_qbe");
			cus_id_qbe = (BigDecimal) request.get("cus_id_qbe");
			Kolateral_txtColNumQBE = (String) request.get("Kolateral_txtColNumQBE");
			Kolateral_txtAccNoQBE = (String) request.get("Kolateral_txtAccNoQBE");
			Kolateral_txtNoRqQBE = (String) request.get("Kolateral_txtNoRqQBE");
			Kolateral_txtDateFromQBE = (Date) request.get("Kolateral_txtDateFromQBE");
			Kolateral_txtDateUntilQBE = (Date) request.get("Kolateral_txtDateUntilQBE");
			proc_status_QBE = (String) request.get("proc_status_QBE");
			RealEstate_OwnerCUS_ID = (BigDecimal) request.get("RealEstate_OwnerCUS_ID");
			RealEstate_QBE_CADA_MUNC = (BigDecimal) request.get("RealEstate_QBE_CADA_MUNC");
			RealEstate_txtCoown = (String) request.get("RealEstate_txtCoown");
			RealEstate_txtQbeLandSub = (String) request.get("RealEstate_txtQbeLandSub");
			RealEstate_txtQbeRealEstLandRegn = (String) request.get("RealEstate_txtQbeRealEstLandRegn");
			Vehi_txtVINQBE = (String) request.get("Vehi_txtVINQBE");
			Coll_txtDepAccQBE = (String) request.get("Coll_txtDepAccQBE");
			Kolateral_txtBrojPoliceQBE = (String) request.get("Kolateral_txtBrojPoliceQBE");
			col_cat_id_qbe = (BigDecimal) request.get("col_cat_id_qbe");
			KolQBE_txtClientType = (String) request.get("KolQBE_txtClientType");
			issuer_cus_id_qbe = (BigDecimal) request.get("issuer_cus_id_qbe");
			ISIN_txtQBE = (String) request.get("ISIN_txtQBE");
			ContractNo_txtQBE = (String) request.get("ContractNo_txtQBE");
			GuarIzdavatelj_txtQBE = (String) request.get("GuarIzdavatelj_txtQBE");
			GuarIzdavateljName_txtQBE = (String) request.get("GuarIzdavateljName_txtQBE");
			guar_issuer_cus_id_qbe = (BigDecimal) request.get("guar_issuer_cus_id_qbe");
			listSelected = (java.util.HashMap) request.get("listSelected");
			tblColWorkListCellTable = (CellTable) request.get("CellTableDefinition");
		}

		public Map getResponse() {
			response.put("tblColWorkList", tblColWorkList);
			response.put("tblColWorkListCellTable", tblColWorkListCellTable);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCOCHGHISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal co_chg_his_id = null;
		public String co_ind = null;
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal co_chg_use_id = null;

		// out args
		public String Kol_txtCoConfirm = null;
		public String Kol_txtCoConfirmUserId = null;
		public String Kol_txtCoConfirmUserName = null;
		public Timestamp Kol_txtCoConfirmTime = null;

		public COLLCOCHGHISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			co_chg_his_id = (BigDecimal) request.get("co_chg_his_id");
			co_ind = (String) request.get("co_ind");
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			co_chg_use_id = (BigDecimal) request.get("co_chg_use_id");
		}

		public Map getResponse() {
			response.put("Kol_txtCoConfirm", Kol_txtCoConfirm);
			response.put("Kol_txtCoConfirmUserId", Kol_txtCoConfirmUserId);
			response.put("Kol_txtCoConfirmUserName", Kol_txtCoConfirmUserName);
			response.put("Kol_txtCoConfirmTime", Kol_txtCoConfirmTime);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class REESTCOCHGHISTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal co_chg_his_id = null;
		public String co_ind = null;
		public BigDecimal col_hea_id = null;
		public BigDecimal co_chg_use_id = null;
		public BigDecimal col_cat_id = null;
		public BigDecimal use_id = null;
		public BigDecimal RealEstate_REAL_EST_EUSE_ID = null;
		public BigDecimal RealEstate_RCEstimateId = null;
		public BigDecimal RealEstate_txtEstnValu = null;
		public BigDecimal RealEstate_REAL_EST_NM_CUR_ID = null;
		public BigDecimal RealEstate_txtNewBuildVal = null;
		public String RealEstate_txtEstnMarkDesc = null;
		public Date RealEstate_txtEstnDate = null;

		// out args
		public String Kol_txtCoConfirm = null;
		public String Kol_txtCoConfirmUserId = null;
		public String Kol_txtCoConfirmUserName = null;
		public Timestamp Kol_txtCoConfirmTime = null;

		public REESTCOCHGHISTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			co_chg_his_id = (BigDecimal) request.get("co_chg_his_id");
			co_ind = (String) request.get("co_ind");
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			co_chg_use_id = (BigDecimal) request.get("co_chg_use_id");
			col_cat_id = (BigDecimal) request.get("col_cat_id");
			use_id = (BigDecimal) request.get("use_id");
			RealEstate_REAL_EST_EUSE_ID = (BigDecimal) request.get("RealEstate_REAL_EST_EUSE_ID");
			RealEstate_RCEstimateId = (BigDecimal) request.get("RealEstate_RCEstimateId");
			RealEstate_txtEstnValu = (BigDecimal) request.get("RealEstate_txtEstnValu");
			RealEstate_REAL_EST_NM_CUR_ID = (BigDecimal) request.get("RealEstate_REAL_EST_NM_CUR_ID");
			RealEstate_txtNewBuildVal = (BigDecimal) request.get("RealEstate_txtNewBuildVal");
			RealEstate_txtEstnMarkDesc = (String) request.get("RealEstate_txtEstnMarkDesc");
			RealEstate_txtEstnDate = (Date) request.get("RealEstate_txtEstnDate");
		}

		public Map getResponse() {
			response.put("Kol_txtCoConfirm", Kol_txtCoConfirm);
			response.put("Kol_txtCoConfirmUserId", Kol_txtCoConfirmUserId);
			response.put("Kol_txtCoConfirmUserName", Kol_txtCoConfirmUserName);
			response.put("Kol_txtCoConfirmTime", Kol_txtCoConfirmTime);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class KOLATERALRETURNDEACT implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String CollListQ_txaCmnt = null;

		// out args

		public KOLATERALRETURNDEACT(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			col_hea_id = (BigDecimal) request.get("col_hea_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			CollListQ_txaCmnt = (String) request.get("CollListQ_txaCmnt");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLOWNERBANKMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal col_hea_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;

		// out args

		public COLLOWNERBANKMAPPING(Map request) {
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

}