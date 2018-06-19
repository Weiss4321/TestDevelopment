package hr.vestigo.modules.collateral.jcics.co32;

import java.sql.Timestamp;
import java.sql.Date;
import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO32 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co32/DeclCO32.java,v 1.6 2017/02/16 15:27:22 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCO32() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public String ScreenEntryParam = null;
	public BigDecimal use_id = null;
	public BigDecimal org_uni_id = null;

	// out args
	public TableData tblAgreementList = new TableData();

	// inner classes
	public AGRLISTQBE agrlistqbe = null;
	public AGRINSERT agrinsert = null;
	public AGRUPDATE agrupdate = null;
	public AGRSELECT agrselect = null;
	public AGRSELMAP agrselmap = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		ScreenEntryParam = (String) request.get("ScreenEntryParam");
		use_id = (BigDecimal) request.get("use_id");
		org_uni_id = (BigDecimal) request.get("org_uni_id");
	}

	public Map getResponse() {
		response.put("tblAgreementList", tblAgreementList);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("AgrListQBE")) {
			agrlistqbe = new AGRLISTQBE(request);
			return agrlistqbe;
		} else if(mapping.equals("AgrInsert")) {
			agrinsert = new AGRINSERT(request);
			return agrinsert;
		} else if(mapping.equals("AgrUpdate")) {
			agrupdate = new AGRUPDATE(request);
			return agrupdate;
		} else if(mapping.equals("AgrSelect")) {
			agrselect = new AGRSELECT(request);
			return agrselect;
		} else if(mapping.equals("AgrSelMap")) {
			agrselmap = new AGRSELMAP(request);
			return agrselmap;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class AGRLISTQBE implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal cus_id_qbe = null;
		public String Agr_txtAgrNoQBE = null;
		public String ScreenEntryParam = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;

		// out args
		public TableData tblAgreementList = new TableData();

		public AGRLISTQBE(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			cus_id_qbe = (BigDecimal) request.get("cus_id_qbe");
			Agr_txtAgrNoQBE = (String) request.get("Agr_txtAgrNoQBE");
			ScreenEntryParam = (String) request.get("ScreenEntryParam");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
		}

		public Map getResponse() {
			response.put("tblAgreementList", tblAgreementList);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class AGRINSERT implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal agr_cus_id = null;
		public String Agr_txtRegNo = null;
		public String Agr_txtAgrNo = null;
		public BigDecimal Agr_txtAmount = null;
		public BigDecimal agr_cur_id = null;
		public Date Agr_txtDateUntil = null;
		public String Agr_txtNumOfBill = null;
		public String Agr_txtAddData = null;
		public BigDecimal use_open_id = null;
		public BigDecimal org_uni_open_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String txtSporazumMZ = null;

		// out args
		public BigDecimal fra_agr_id = null;
		public String status = null;
		public String proc_status = null;
		public String Coll_txtMortgageSt = null;
		public String Coll_txtCoverIndic = null;
		public String workflow_indic = null;
		public String Coll_txtFinFlag = null;
		public Timestamp Coll_txtOpeningTs = null;
		public Timestamp Coll_txtUserLock = null;
		public Timestamp user_lock = null;

		public AGRINSERT(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			agr_cus_id = (BigDecimal) request.get("agr_cus_id");
			Agr_txtRegNo = (String) request.get("Agr_txtRegNo");
			Agr_txtAgrNo = (String) request.get("Agr_txtAgrNo");
			Agr_txtAmount = (BigDecimal) request.get("Agr_txtAmount");
			agr_cur_id = (BigDecimal) request.get("agr_cur_id");
			Agr_txtDateUntil = (Date) request.get("Agr_txtDateUntil");
			Agr_txtNumOfBill = (String) request.get("Agr_txtNumOfBill");
			Agr_txtAddData = (String) request.get("Agr_txtAddData");
			use_open_id = (BigDecimal) request.get("use_open_id");
			org_uni_open_id = (BigDecimal) request.get("org_uni_open_id");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			txtSporazumMZ = (String) request.get("txtSporazumMZ");
		}

		public Map getResponse() {
			response.put("fra_agr_id", fra_agr_id);
			response.put("status", status);
			response.put("proc_status", proc_status);
			response.put("Coll_txtMortgageSt", Coll_txtMortgageSt);
			response.put("Coll_txtCoverIndic", Coll_txtCoverIndic);
			response.put("workflow_indic", workflow_indic);
			response.put("Coll_txtFinFlag", Coll_txtFinFlag);
			response.put("Coll_txtOpeningTs", Coll_txtOpeningTs);
			response.put("Coll_txtUserLock", Coll_txtUserLock);
			response.put("user_lock", user_lock);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class AGRUPDATE implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal fra_agr_id = null;
		public BigDecimal agr_cus_id = null;
		public String Agr_txtRegNo = null;
		public String Agr_txtAgrNo = null;
		public BigDecimal Agr_txtAmount = null;
		public BigDecimal agr_cur_id = null;
		public Date Agr_txtDateUntil = null;
		public String Agr_txtNumOfBill = null;
		public String Agr_txtAddData = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public Timestamp user_lock_in = null;
		public String update_flag = null;
		public String txtSporazumMZ = null;

		// out args
		public Timestamp user_lock = null;
		public Timestamp Coll_txtUserLock = null;

		public AGRUPDATE(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			fra_agr_id = (BigDecimal) request.get("fra_agr_id");
			agr_cus_id = (BigDecimal) request.get("agr_cus_id");
			Agr_txtRegNo = (String) request.get("Agr_txtRegNo");
			Agr_txtAgrNo = (String) request.get("Agr_txtAgrNo");
			Agr_txtAmount = (BigDecimal) request.get("Agr_txtAmount");
			agr_cur_id = (BigDecimal) request.get("agr_cur_id");
			Agr_txtDateUntil = (Date) request.get("Agr_txtDateUntil");
			Agr_txtNumOfBill = (String) request.get("Agr_txtNumOfBill");
			Agr_txtAddData = (String) request.get("Agr_txtAddData");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			user_lock_in = (Timestamp) request.get("user_lock_in");
			update_flag = (String) request.get("update_flag");
			txtSporazumMZ = (String) request.get("txtSporazumMZ");
		}

		public Map getResponse() {
			response.put("user_lock", user_lock);
			response.put("Coll_txtUserLock", Coll_txtUserLock);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class AGRSELECT implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal fra_agr_id = null;

		// out args
		public BigDecimal agr_cus_id = null;
		public String Agr_txtRegNo = null;
		public String Agr_txtAgrNo = null;
		public BigDecimal Agr_txtAmount = null;
		public BigDecimal agr_cur_id = null;
		public Date Agr_txtDateUntil = null;
		public String Agr_txtNumOfBill = null;
		public String Agr_txtAddData = null;
		public String status = null;
		public String proc_status = null;
		public String Coll_txtMortgageSt = null;
		public String Coll_txtCoverIndic = null;
		public String workflow_indic = null;
		public String Coll_txtFinFlag = null;
		public BigDecimal use_open_id = null;
		public BigDecimal org_uni_open_id = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public Timestamp Coll_txtOpeningTs = null;
		public Timestamp Coll_txtUserLock = null;
		public Timestamp user_lock = null;
		public String Agr_txtCode = null;
		public String Agr_txtCur = null;
		public BigDecimal Agr_txtAmountPost = null;
		public BigDecimal Agr_txtAmountRest = null;
		public BigDecimal use_id_ver = null;
		public String Coll_txtUseOpenIdLogin = null;
		public String Coll_txtUseOpenIdName = null;
		public String Coll_txtUseIdLogin = null;
		public String Coll_txtUseIdName = null;
		public BigDecimal use_id_ver_snd = null;
		public String C_txtUseIdSndVerLogin = null;
		public String C_txtUseIdSndVerName = null;
		public Timestamp VER_SEND_TS = null;
		public String C_txtUseIdVerLogin = null;
		public String C_txtUseIdVerName = null;
		public Timestamp VERIFICATION_TS = null;
		public String Agr_txtName = null;
		public String Agr_txtOIB = null;
		public String txtSporazumMZ = null;

		public AGRSELECT(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			fra_agr_id = (BigDecimal) request.get("fra_agr_id");
		}

		public Map getResponse() {
			response.put("agr_cus_id", agr_cus_id);
			response.put("Agr_txtRegNo", Agr_txtRegNo);
			response.put("Agr_txtAgrNo", Agr_txtAgrNo);
			response.put("Agr_txtAmount", Agr_txtAmount);
			response.put("agr_cur_id", agr_cur_id);
			response.put("Agr_txtDateUntil", Agr_txtDateUntil);
			response.put("Agr_txtNumOfBill", Agr_txtNumOfBill);
			response.put("Agr_txtAddData", Agr_txtAddData);
			response.put("status", status);
			response.put("proc_status", proc_status);
			response.put("Coll_txtMortgageSt", Coll_txtMortgageSt);
			response.put("Coll_txtCoverIndic", Coll_txtCoverIndic);
			response.put("workflow_indic", workflow_indic);
			response.put("Coll_txtFinFlag", Coll_txtFinFlag);
			response.put("use_open_id", use_open_id);
			response.put("org_uni_open_id", org_uni_open_id);
			response.put("use_id", use_id);
			response.put("org_uni_id", org_uni_id);
			response.put("Coll_txtOpeningTs", Coll_txtOpeningTs);
			response.put("Coll_txtUserLock", Coll_txtUserLock);
			response.put("user_lock", user_lock);
			response.put("Agr_txtCode", Agr_txtCode);
			response.put("Agr_txtCur", Agr_txtCur);
			response.put("Agr_txtAmountPost", Agr_txtAmountPost);
			response.put("Agr_txtAmountRest", Agr_txtAmountRest);
			response.put("use_id_ver", use_id_ver);
			response.put("Coll_txtUseOpenIdLogin", Coll_txtUseOpenIdLogin);
			response.put("Coll_txtUseOpenIdName", Coll_txtUseOpenIdName);
			response.put("Coll_txtUseIdLogin", Coll_txtUseIdLogin);
			response.put("Coll_txtUseIdName", Coll_txtUseIdName);
			response.put("use_id_ver_snd", use_id_ver_snd);
			response.put("C_txtUseIdSndVerLogin", C_txtUseIdSndVerLogin);
			response.put("C_txtUseIdSndVerName", C_txtUseIdSndVerName);
			response.put("VER_SEND_TS", VER_SEND_TS);
			response.put("C_txtUseIdVerLogin", C_txtUseIdVerLogin);
			response.put("C_txtUseIdVerName", C_txtUseIdVerName);
			response.put("VERIFICATION_TS", VERIFICATION_TS);
			response.put("Agr_txtName", Agr_txtName);
			response.put("Agr_txtOIB", Agr_txtOIB);
			response.put("txtSporazumMZ", txtSporazumMZ);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class AGRSELMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public Integer LookUpLevel = null;
		public String frame_num = null;
		public String l_register_no = null;

		// out args
		public TableData LookUpTableData = new TableData();

		public AGRSELMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LookUpLevel = (Integer) request.get("LookUpLevel");
			frame_num = (String) request.get("frame_num");
			l_register_no = (String) request.get("l_register_no");
		}

		public Map getResponse() {
			response.put("LookUpTableData", LookUpTableData);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}