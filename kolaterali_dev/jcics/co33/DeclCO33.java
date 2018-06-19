package hr.vestigo.modules.collateral.jcics.co33;

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

public class DeclCO33 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co33/DeclCO33.java,v 1.2 2007/03/06 14:19:06 hramkr Exp $";
	private Map response = new HashMap();

	public DeclCO33() {
	}

	//	in args
	public BigDecimal use_id = null;
	public BigDecimal org_uni_id = null;
	public String source_list = null;
	public String target_list = null;
	public String w_proc_status = null;
	public String action_type = null;
	public BigDecimal w_fra_agr_id = null;

	//	out args

	// inner classes
	public AGRLISTBACK agrlistback = null;
	public AGRLISTCLOSE agrlistclose = null;
	public AGRLISTSTOP agrliststop = null;
	public AGRLISTVER agrlistver = null;
	public AGRLISTQSELECT agrlistqselect = null;

	public void setRequest(Map request) {
		use_id = (BigDecimal) request.get("use_id");
		org_uni_id = (BigDecimal) request.get("org_uni_id");
		source_list = (String) request.get("source_list");
		target_list = (String) request.get("target_list");
		w_proc_status = (String) request.get("w_proc_status");
		action_type = (String) request.get("action_type");
		w_fra_agr_id = (BigDecimal) request.get("w_fra_agr_id");
	}

	public Map getResponse() {

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("AgrListBack")) {
			agrlistback = new AGRLISTBACK(request);
			return agrlistback;
		} else if(mapping.equals("AgrListClose")) {
			agrlistclose = new AGRLISTCLOSE(request);
			return agrlistclose;
		} else if(mapping.equals("AgrListStop")) {
			agrliststop = new AGRLISTSTOP(request);
			return agrliststop;
		} else if(mapping.equals("AgrListVer")) {
			agrlistver = new AGRLISTVER(request);
			return agrlistver;
		} else if(mapping.equals("AgrListQSelect")) {
			agrlistqselect = new AGRLISTQSELECT(request);
			return agrlistqselect;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class AGRLISTBACK implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String source_list = null;
		public String target_list = null;
		public String w_proc_status = null;
		public String action_type = null;
		public BigDecimal w_fra_agr_id = null;
		public String CollListQ_txaCmnt = null;
		public String w_workflow_indic = null;
		public String source_status = null;
		public BigDecimal org_uni_id_asg = null;
		public BigDecimal use_id_asg = null;

		//	out args

		public AGRLISTBACK(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			w_proc_status = (String) request.get("w_proc_status");
			action_type = (String) request.get("action_type");
			w_fra_agr_id = (BigDecimal) request.get("w_fra_agr_id");
			CollListQ_txaCmnt = (String) request.get("CollListQ_txaCmnt");
			w_workflow_indic = (String) request.get("w_workflow_indic");
			source_status = (String) request.get("source_status");
			org_uni_id_asg = (BigDecimal) request.get("org_uni_id_asg");
			use_id_asg = (BigDecimal) request.get("use_id_asg");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class AGRLISTCLOSE implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String source_list = null;
		public String target_list = null;
		public String w_proc_status = null;
		public String action_type = null;
		public BigDecimal w_fra_agr_id = null;

		//	out args

		public AGRLISTCLOSE(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			w_proc_status = (String) request.get("w_proc_status");
			action_type = (String) request.get("action_type");
			w_fra_agr_id = (BigDecimal) request.get("w_fra_agr_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class AGRLISTSTOP implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public String CollListQ_txaCmnt = null;
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String source_list = null;
		public String target_list = null;
		public String w_proc_status = null;
		public String action_type = null;
		public BigDecimal w_fra_agr_id = null;

		//	out args

		public AGRLISTSTOP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollListQ_txaCmnt = (String) request.get("CollListQ_txaCmnt");
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			w_proc_status = (String) request.get("w_proc_status");
			action_type = (String) request.get("action_type");
			w_fra_agr_id = (BigDecimal) request.get("w_fra_agr_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class AGRLISTVER implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal use_id = null;
		public BigDecimal org_uni_id = null;
		public String source_list = null;
		public String target_list = null;
		public String w_proc_status = null;
		public String action_type = null;
		public BigDecimal w_fra_agr_id = null;

		//	out args

		public AGRLISTVER(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			org_uni_id = (BigDecimal) request.get("org_uni_id");
			source_list = (String) request.get("source_list");
			target_list = (String) request.get("target_list");
			w_proc_status = (String) request.get("w_proc_status");
			action_type = (String) request.get("action_type");
			w_fra_agr_id = (BigDecimal) request.get("w_fra_agr_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class AGRLISTQSELECT implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal w_fra_agr_id = null;
		public java.lang.Integer ActionListLevel = null;

		//	out args
		public TableData tblAgrListQ = new TableData();

		public AGRLISTQSELECT(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			w_fra_agr_id = (BigDecimal) request.get("w_fra_agr_id");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		}

		public Map getResponse() {
			response.put("tblAgrListQ", tblAgrListQ);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}