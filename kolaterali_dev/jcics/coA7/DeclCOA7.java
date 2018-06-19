package hr.vestigo.modules.collateral.jcics.coA7;

import java.sql.Timestamp;
import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA7 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA7/DeclCOA7.java,v 1.2 2011/04/27 12:47:57 hrakis Exp $";
	private Map response = new HashMap();

	public DeclCOA7() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public String EXCEPTION_TYPE = null;

	// out args
	public TableData tblFrameAccException = new TableData();

	// inner classes
	public FRAMEACCEXCEPTIONDIALOGSELECTM frameaccexceptiondialogselectm = null;
	public FRAMEACCEXCEPTIONDIALOGUPDATEM frameaccexceptiondialogupdatem = null;
	public FRAMEACCEXCEPTIONDIALOGINSERTM frameaccexceptiondialoginsertm = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		EXCEPTION_TYPE = (String) request.get("EXCEPTION_TYPE");
	}

	public Map getResponse() {
		response.put("tblFrameAccException", tblFrameAccException);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("FrameAccExceptionDialogSelectM")) {
			frameaccexceptiondialogselectm = new FRAMEACCEXCEPTIONDIALOGSELECTM(request);
			return frameaccexceptiondialogselectm;
		} else if(mapping.equals("FrameAccExceptionDialogUpdateM")) {
			frameaccexceptiondialogupdatem = new FRAMEACCEXCEPTIONDIALOGUPDATEM(request);
			return frameaccexceptiondialogupdatem;
		} else if(mapping.equals("FrameAccExceptionDialogInsertM")) {
			frameaccexceptiondialoginsertm = new FRAMEACCEXCEPTIONDIALOGINSERTM(request);
			return frameaccexceptiondialoginsertm;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class FRAMEACCEXCEPTIONDIALOGSELECTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal FRA_ACC_EXC_ID = null;

		// out args
		public String FrameAccExceptionDialog_txtRegisterNo = null;
		public String FrameAccExceptionDialog_txtName = null;
		public String FrameAccExceptionDialog_txtCusAccNo = null;
		public String FrameAccExceptionDialog_txtContractNo = null;
		public String FrameAccExceptionDialog_txtComment = null;
		public String FrameAccExceptionDialog_txtStatus = null;
		public String FrameAccExceptionDialog_txtUserLogin = null;
		public String FrameAccExceptionDialog_txtUserName = null;
		public Timestamp FrameAccExceptionDialog_txtUserLock = null;
		public BigDecimal CUS_ID = null;
		public BigDecimal CUS_ACC_ID = null;
		public BigDecimal USE_ID = null;

		public FRAMEACCEXCEPTIONDIALOGSELECTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			FRA_ACC_EXC_ID = (BigDecimal) request.get("FRA_ACC_EXC_ID");
		}

		public Map getResponse() {
			response.put("FrameAccExceptionDialog_txtRegisterNo", FrameAccExceptionDialog_txtRegisterNo);
			response.put("FrameAccExceptionDialog_txtName", FrameAccExceptionDialog_txtName);
			response.put("FrameAccExceptionDialog_txtCusAccNo", FrameAccExceptionDialog_txtCusAccNo);
			response.put("FrameAccExceptionDialog_txtContractNo", FrameAccExceptionDialog_txtContractNo);
			response.put("FrameAccExceptionDialog_txtComment", FrameAccExceptionDialog_txtComment);
			response.put("FrameAccExceptionDialog_txtStatus", FrameAccExceptionDialog_txtStatus);
			response.put("FrameAccExceptionDialog_txtUserLogin", FrameAccExceptionDialog_txtUserLogin);
			response.put("FrameAccExceptionDialog_txtUserName", FrameAccExceptionDialog_txtUserName);
			response.put("FrameAccExceptionDialog_txtUserLock", FrameAccExceptionDialog_txtUserLock);
			response.put("CUS_ID", CUS_ID);
			response.put("CUS_ACC_ID", CUS_ACC_ID);
			response.put("USE_ID", USE_ID);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class FRAMEACCEXCEPTIONDIALOGUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal FRA_ACC_EXC_ID = null;
		public String FrameAccExceptionDialog_txtStatus = null;
		public String FrameAccExceptionDialog_txtComment = null;
		public BigDecimal use_id = null;

		// out args

		public FRAMEACCEXCEPTIONDIALOGUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			FRA_ACC_EXC_ID = (BigDecimal) request.get("FRA_ACC_EXC_ID");
			FrameAccExceptionDialog_txtStatus = (String) request.get("FrameAccExceptionDialog_txtStatus");
			FrameAccExceptionDialog_txtComment = (String) request.get("FrameAccExceptionDialog_txtComment");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class FRAMEACCEXCEPTIONDIALOGINSERTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal CUS_ACC_ID = null;
		public String FrameAccExceptionDialog_txtStatus = null;
		public String FrameAccExceptionDialog_txtComment = null;
		public BigDecimal use_id = null;
		public String EXCEPTION_TYPE = null;

		// out args

		public FRAMEACCEXCEPTIONDIALOGINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CUS_ACC_ID = (BigDecimal) request.get("CUS_ACC_ID");
			FrameAccExceptionDialog_txtStatus = (String) request.get("FrameAccExceptionDialog_txtStatus");
			FrameAccExceptionDialog_txtComment = (String) request.get("FrameAccExceptionDialog_txtComment");
			use_id = (BigDecimal) request.get("use_id");
			EXCEPTION_TYPE = (String) request.get("EXCEPTION_TYPE");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}