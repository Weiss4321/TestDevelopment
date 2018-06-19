package hr.vestigo.modules.collateral.jcics.coA8;

import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA8 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA8/DeclCOA8.java,v 1.4 2012/09/26 10:20:29 hrakis Exp $";
	private Map response = new HashMap();

	public DeclCOA8() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public String InsPolWarningNotesList_txtPolicyType = null;
	public String InsPolWarningNotesList_txtCustType = null;
	public BigDecimal ORG_UNI_ID = null;
	public BigDecimal IC_ID = null;
	public String InsPolWarningNotesList_txtWrnStatus = null;
	public Date InsPolWarningNotesList_txtDateFrom = null;
	public Date InsPolWarningNotesList_txtDateUntil = null;
	public BigDecimal CUS_ID = null;
	public BigDecimal CUS_ACC_ID = null;
	public String InsPolWarningNotesList_txtCandidateStatus = null;

	// out args
	public TableData tblInsPolWarningNotes = new TableData();

	// inner classes
	public INSPOLWARNINGNOTESLISTSTOPSENDM inspolwarningnotesliststopsendm = null;
	public INSPOLWARNINGNOTESLISTWARNTONOTEM inspolwarningnoteslistwarntonotem = null;
	public INSPOLSTATCHGHISTORYLISTSELECTM inspolstatchghistorylistselectm = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		InsPolWarningNotesList_txtPolicyType = (String) request.get("InsPolWarningNotesList_txtPolicyType");
		InsPolWarningNotesList_txtCustType = (String) request.get("InsPolWarningNotesList_txtCustType");
		ORG_UNI_ID = (BigDecimal) request.get("ORG_UNI_ID");
		IC_ID = (BigDecimal) request.get("IC_ID");
		InsPolWarningNotesList_txtWrnStatus = (String) request.get("InsPolWarningNotesList_txtWrnStatus");
		InsPolWarningNotesList_txtDateFrom = (Date) request.get("InsPolWarningNotesList_txtDateFrom");
		InsPolWarningNotesList_txtDateUntil = (Date) request.get("InsPolWarningNotesList_txtDateUntil");
		CUS_ID = (BigDecimal) request.get("CUS_ID");
		CUS_ACC_ID = (BigDecimal) request.get("CUS_ACC_ID");
		InsPolWarningNotesList_txtCandidateStatus = (String) request.get("InsPolWarningNotesList_txtCandidateStatus");
	}

	public Map getResponse() {
		response.put("tblInsPolWarningNotes", tblInsPolWarningNotes);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("InsPolWarningNotesListStopSendM")) {
			inspolwarningnotesliststopsendm = new INSPOLWARNINGNOTESLISTSTOPSENDM(request);
			return inspolwarningnotesliststopsendm;
		} else if(mapping.equals("InsPolWarningNotesListWarnToNoteM")) {
			inspolwarningnoteslistwarntonotem = new INSPOLWARNINGNOTESLISTWARNTONOTEM(request);
			return inspolwarningnoteslistwarntonotem;
		} else if(mapping.equals("InsPolStatChgHistoryListSelectM")) {
			inspolstatchghistorylistselectm = new INSPOLSTATCHGHISTORYLISTSELECTM(request);
			return inspolstatchghistorylistselectm;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class INSPOLWARNINGNOTESLISTSTOPSENDM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String InsPolWarningNotesList_txtPolicyType = null;
		public String InsPolWarningNotesList_txtCustType = null;
		public BigDecimal ORG_UNI_ID = null;
		public BigDecimal IC_ID = null;
		public String InsPolWarningNotesList_txtWrnStatus = null;
		public Date InsPolWarningNotesList_txtDateFrom = null;
		public Date InsPolWarningNotesList_txtDateUntil = null;
		public BigDecimal INS_WAR_NOT_ID = null;
		public String CANDIDATE_STATUS = null;
		public BigDecimal use_id = null;

		// out args

		public INSPOLWARNINGNOTESLISTSTOPSENDM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			InsPolWarningNotesList_txtPolicyType = (String) request.get("InsPolWarningNotesList_txtPolicyType");
			InsPolWarningNotesList_txtCustType = (String) request.get("InsPolWarningNotesList_txtCustType");
			ORG_UNI_ID = (BigDecimal) request.get("ORG_UNI_ID");
			IC_ID = (BigDecimal) request.get("IC_ID");
			InsPolWarningNotesList_txtWrnStatus = (String) request.get("InsPolWarningNotesList_txtWrnStatus");
			InsPolWarningNotesList_txtDateFrom = (Date) request.get("InsPolWarningNotesList_txtDateFrom");
			InsPolWarningNotesList_txtDateUntil = (Date) request.get("InsPolWarningNotesList_txtDateUntil");
			INS_WAR_NOT_ID = (BigDecimal) request.get("INS_WAR_NOT_ID");
			CANDIDATE_STATUS = (String) request.get("CANDIDATE_STATUS");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSPOLWARNINGNOTESLISTWARNTONOTEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal INS_WAR_NOT_ID = null;
		public BigDecimal use_id = null;

		// out args

		public INSPOLWARNINGNOTESLISTWARNTONOTEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			INS_WAR_NOT_ID = (BigDecimal) request.get("INS_WAR_NOT_ID");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSPOLSTATCHGHISTORYLISTSELECTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal IP_ID = null;
		public BigDecimal COL_INS_ID = null;

		// out args
		public String InsPolStatChgHistory_txtColNum = null;
		public String InsPolStatChgHistory_txtIpCode = null;
		public TableData tblInsPolStatChgHistory = new TableData();

		public INSPOLSTATCHGHISTORYLISTSELECTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			IP_ID = (BigDecimal) request.get("IP_ID");
			COL_INS_ID = (BigDecimal) request.get("COL_INS_ID");
		}

		public Map getResponse() {
			response.put("InsPolStatChgHistory_txtColNum", InsPolStatChgHistory_txtColNum);
			response.put("InsPolStatChgHistory_txtIpCode", InsPolStatChgHistory_txtIpCode);
			response.put("tblInsPolStatChgHistory", tblInsPolStatChgHistory);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}