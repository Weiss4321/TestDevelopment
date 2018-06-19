package hr.vestigo.modules.collateral.jcics.co09;

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

public class DeclCO09 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co09/DeclCO09.java,v 1.14 2006/02/08 08:28:27 hrasia Exp $";
	private Map response = new HashMap();

	public DeclCO09() {
	}

	//	in args
	public BigDecimal CollHfPriorListRba_CUS_ID = null;
	public String CollHfPriorListRba_txtColaCusIdUseRegisterNo = null;
	public String CollHfPriorListRba_txtColaCusIdUseOwnerName = null;
	public BigDecimal CollHfPriorListRba_COL_TYPE_ID = null;
	public String CollHfPriorListRba_txtCollTypeCode = null;
	public String CollHfPriorListRba_txtCollTypeName = null;
	public BigDecimal CollHfPriorListRba_COL_HEA_ID = null;
	public String CollHfPriorListRba_txtColNum = null;
	public java.lang.Integer ActionListLevel = null;

	
	//	out args
	public TableData tblCollHfPriorListRba = new TableData();

	// inner classes
	public COLLHFPRIORDIALOGRBAMAPPING collhfpriordialogrbamapping = null;

	public void setRequest(Map request) {
		CollHfPriorListRba_CUS_ID = (BigDecimal) request.get("CollHfPriorListRba_CUS_ID");
		CollHfPriorListRba_txtColaCusIdUseRegisterNo = (String) request.get("CollHfPriorListRba_txtColaCusIdUseRegisterNo");
		CollHfPriorListRba_txtColaCusIdUseOwnerName = (String) request.get("CollHfPriorListRba_txtColaCusIdUseOwnerName");
		CollHfPriorListRba_COL_TYPE_ID = (BigDecimal) request.get("CollHfPriorListRba_COL_TYPE_ID");
		CollHfPriorListRba_txtCollTypeCode = (String) request.get("CollHfPriorListRba_txtCollTypeCode");
		CollHfPriorListRba_txtCollTypeName = (String) request.get("CollHfPriorListRba_txtCollTypeName");
		CollHfPriorListRba_COL_HEA_ID = (BigDecimal) request.get("CollHfPriorListRba_COL_HEA_ID");
		CollHfPriorListRba_txtColNum = (String) request.get("CollHfPriorListRba_txtColNum");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblCollHfPriorListRba", tblCollHfPriorListRba);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollHfPriorDialogRbaMapping")) {
			collhfpriordialogrbamapping = new COLLHFPRIORDIALOGRBAMAPPING(request);
			return collhfpriordialogrbamapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLHFPRIORDIALOGRBAMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal CUS_ID = null;
		public String CollHfPriorDialog_txtColaCusIdUseRegisterNo = null;
		public String CollHfPriorDialog_txtColaCusIdUseOwnerName = null;
		public BigDecimal COLL_HF_PRIOR_ID = null;

		//	out args
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

		public COLLHFPRIORDIALOGRBAMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollHfPriorDialog_txtColaCusIdUseRegisterNo = (String) request.get("CollHfPriorDialog_txtColaCusIdUseRegisterNo");
			CollHfPriorDialog_txtColaCusIdUseOwnerName = (String) request.get("CollHfPriorDialog_txtColaCusIdUseOwnerName");
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

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}