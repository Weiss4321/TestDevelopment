package hr.vestigo.modules.collateral.jcics.coA6;

import java.sql.Timestamp;
import java.sql.Date;
import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA6 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA6/DeclCOA6.java,v 1.3 2012/08/29 06:30:21 hraaks Exp $";
	private Map response = new HashMap();

	public DeclCOA6() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;

	// out args
	public TableData tblCollCashDepException = new TableData();

	// inner classes
	public COLLCASHDEPEXCEPTIONINSERTM collcashdepexceptioninsertm = null;
	public COLLCASHDEPEXCEPTIONUPDATEM collcashdepexceptionupdatem = null;
	public COLLCASHDEPEXCEPTIONDETAILM collcashdepexceptiondetailm = null;
	public COLLCASHDEPEXCEPTIONQUERYM collcashdepexceptionquerym = null;
	public COLLCASHDEPEXCEPTIONOWNERM collcashdepexceptionownerm = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblCollCashDepException", tblCollCashDepException);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollCashDepExceptionInsertM")) {
			collcashdepexceptioninsertm = new COLLCASHDEPEXCEPTIONINSERTM(request);
			return collcashdepexceptioninsertm;
		} else if(mapping.equals("CollCashDepExceptionUpdateM")) {
			collcashdepexceptionupdatem = new COLLCASHDEPEXCEPTIONUPDATEM(request);
			return collcashdepexceptionupdatem;
		} else if(mapping.equals("CollCashDepExceptionDetailM")) {
			collcashdepexceptiondetailm = new COLLCASHDEPEXCEPTIONDETAILM(request);
			return collcashdepexceptiondetailm;
		} else if(mapping.equals("CollCashDepExceptionQueryM")) {
			collcashdepexceptionquerym = new COLLCASHDEPEXCEPTIONQUERYM(request);
			return collcashdepexceptionquerym;
		} else if(mapping.equals("CollCashDepExceptionOwnerM")) {
			collcashdepexceptionownerm = new COLLCASHDEPEXCEPTIONOWNERM(request);
			return collcashdepexceptionownerm;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLCASHDEPEXCEPTIONINSERTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String CollCashDepExceptionDialog_txtCus_Acc_No = null;
		public Date CollCashDepExceptionDialog_txtDateFrom = null;
		public String CollCashDepExceptionDialog_txtStatus = null;
		public Date CollCashDepExceptionDialog_txtDateUntil = null;
		public String CollCashDepExceptionDialog_txtUseId = null;

		// out args
		public Timestamp CollCashDepExceptionDialog_txtUserLock = null;

		public COLLCASHDEPEXCEPTIONINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollCashDepExceptionDialog_txtCus_Acc_No = (String) request.get("CollCashDepExceptionDialog_txtCus_Acc_No");
			CollCashDepExceptionDialog_txtDateFrom = (Date) request.get("CollCashDepExceptionDialog_txtDateFrom");
			CollCashDepExceptionDialog_txtStatus = (String) request.get("CollCashDepExceptionDialog_txtStatus");
			CollCashDepExceptionDialog_txtDateUntil = (Date) request.get("CollCashDepExceptionDialog_txtDateUntil");
			CollCashDepExceptionDialog_txtUseId = (String) request.get("CollCashDepExceptionDialog_txtUseId");
		}

		public Map getResponse() {
			response.put("CollCashDepExceptionDialog_txtUserLock", CollCashDepExceptionDialog_txtUserLock);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCASHDEPEXCEPTIONUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public Date CollCashDepExceptionDialog_txtDateUntil = null;
		public BigDecimal use_id = null;
		public BigDecimal cas_exc_id = null;
		public Date CollCashDepExceptionDialog_txtDateFrom = null;
		public String CollCashDepExceptionDialog_txtStatus = null;

		// out args
		public Timestamp CollCashDepExceptionDialog_txtUserLock = null;
		public String CollCashDepExceptionDialog_txtUserLogin = null;
		public String CollCashDepExceptionDialog_txtUserName = null;
		public String CollCashDepExceptionDialog_txtUseId = null;

		public COLLCASHDEPEXCEPTIONUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollCashDepExceptionDialog_txtDateUntil = (Date) request.get("CollCashDepExceptionDialog_txtDateUntil");
			use_id = (BigDecimal) request.get("use_id");
			cas_exc_id = (BigDecimal) request.get("cas_exc_id");
			CollCashDepExceptionDialog_txtDateFrom = (Date) request.get("CollCashDepExceptionDialog_txtDateFrom");
			CollCashDepExceptionDialog_txtStatus = (String) request.get("CollCashDepExceptionDialog_txtStatus");
		}

		public Map getResponse() {
			response.put("CollCashDepExceptionDialog_txtUserLock", CollCashDepExceptionDialog_txtUserLock);
			response.put("CollCashDepExceptionDialog_txtUserLogin", CollCashDepExceptionDialog_txtUserLogin);
			response.put("CollCashDepExceptionDialog_txtUserName", CollCashDepExceptionDialog_txtUserName);
			response.put("CollCashDepExceptionDialog_txtUseId", CollCashDepExceptionDialog_txtUseId);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCASHDEPEXCEPTIONDETAILM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal cas_exc_id = null;

		// out args
		public String CollCashDepExceptionDialog_txtCus_Acc_No = null;
		public Date CollCashDepExceptionDialog_txtDateFrom = null;
		public Date CollCashDepExceptionDialog_txtDateUntil = null;
		public String CollCashDepExceptionDialog_txtStatus = null;
		public String CollCashDepExceptionDialog_txtUserLogin = null;
		public String CollCashDepExceptionDialog_txtUserName = null;
		public Timestamp CollCashDepExceptionDialog_txtUserLock = null;
		public String CollCashDepExceptionDialog_txtUseId = null;
		public BigDecimal CollCashDepExceptionDialog_txtCas_Exc_Id = null;
		public String CollCashDepExceptionDialog_txtRegisterNo = null;
		public String CollCashDepExceptionDialog_txtName = null;

		public COLLCASHDEPEXCEPTIONDETAILM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			cas_exc_id = (BigDecimal) request.get("cas_exc_id");
		}

		public Map getResponse() {
			response.put("CollCashDepExceptionDialog_txtCus_Acc_No", CollCashDepExceptionDialog_txtCus_Acc_No);
			response.put("CollCashDepExceptionDialog_txtDateFrom", CollCashDepExceptionDialog_txtDateFrom);
			response.put("CollCashDepExceptionDialog_txtDateUntil", CollCashDepExceptionDialog_txtDateUntil);
			response.put("CollCashDepExceptionDialog_txtStatus", CollCashDepExceptionDialog_txtStatus);
			response.put("CollCashDepExceptionDialog_txtUserLogin", CollCashDepExceptionDialog_txtUserLogin);
			response.put("CollCashDepExceptionDialog_txtUserName", CollCashDepExceptionDialog_txtUserName);
			response.put("CollCashDepExceptionDialog_txtUserLock", CollCashDepExceptionDialog_txtUserLock);
			response.put("CollCashDepExceptionDialog_txtUseId", CollCashDepExceptionDialog_txtUseId);
			response.put("CollCashDepExceptionDialog_txtCas_Exc_Id", CollCashDepExceptionDialog_txtCas_Exc_Id);
			response.put("CollCashDepExceptionDialog_txtRegisterNo", CollCashDepExceptionDialog_txtRegisterNo);
			response.put("CollCashDepExceptionDialog_txtName", CollCashDepExceptionDialog_txtName);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCASHDEPEXCEPTIONQUERYM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public java.lang.Integer ActionListLevel = null;
		public String CollCashDepExceptionQBE_txtCus_Acc_No = null;
		public Date CollCashDepExceptionQBE_txtDateFrom = null;
		public Date CollCashDepExceptionQBE_txtDateUntil = null;
		public String CollCashDepExceptionQBE_register_no = null;
		public String CollCashDepExceptionQBE_owner_name = null;
		public BigDecimal cus_id_qbe = null;

		// out args
		public TableData tblCollCashDepException = new TableData();

		public COLLCASHDEPEXCEPTIONQUERYM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CollCashDepExceptionQBE_txtCus_Acc_No = (String) request.get("CollCashDepExceptionQBE_txtCus_Acc_No");
			CollCashDepExceptionQBE_txtDateFrom = (Date) request.get("CollCashDepExceptionQBE_txtDateFrom");
			CollCashDepExceptionQBE_txtDateUntil = (Date) request.get("CollCashDepExceptionQBE_txtDateUntil");
			CollCashDepExceptionQBE_register_no = (String) request.get("CollCashDepExceptionQBE_register_no");
			CollCashDepExceptionQBE_owner_name = (String) request.get("CollCashDepExceptionQBE_owner_name");
			cus_id_qbe = (BigDecimal) request.get("cus_id_qbe");
		}

		public Map getResponse() {
			response.put("tblCollCashDepException", tblCollCashDepException);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLCASHDEPEXCEPTIONOWNERM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String CollCashDepExceptionDialog_txtCus_Acc_No = null;

		// out args
		public String CollCashDepExceptionDialog_txtRegisterNo = null;
		public String CollCashDepExceptionDialog_txtName = null;

		public COLLCASHDEPEXCEPTIONOWNERM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollCashDepExceptionDialog_txtCus_Acc_No = (String) request.get("CollCashDepExceptionDialog_txtCus_Acc_No");
		}

		public Map getResponse() {
			response.put("CollCashDepExceptionDialog_txtRegisterNo", CollCashDepExceptionDialog_txtRegisterNo);
			response.put("CollCashDepExceptionDialog_txtName", CollCashDepExceptionDialog_txtName);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}