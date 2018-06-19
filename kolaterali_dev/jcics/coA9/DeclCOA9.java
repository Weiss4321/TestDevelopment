package hr.vestigo.modules.collateral.jcics.coA9;

import java.sql.Date;
import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA9 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA9/DeclCOA9.java,v 1.3 2012/05/14 07:38:12 hradnp Exp $";
	private Map response = new HashMap();

	public DeclCOA9() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;
	public String CoBorrower_txtLBenAccNo = null;
	public String CoBorrower_txtLBenRegNo = null;
	public String CoBorrower_txtLBenRequestNo = null;
	public BigDecimal CUS_ACC_ID = null;
	public String CONTRACT_NO = null;

	// out args
	public TableData tblCoBorrowerList = new TableData();

	// inner classes
	public COBORROWERINSERTM coborrowerinsertm = null;
	public COBORROWERUPDATEM coborrowerupdatem = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		CoBorrower_txtLBenAccNo = (String) request.get("CoBorrower_txtLBenAccNo");
		CoBorrower_txtLBenRegNo = (String) request.get("CoBorrower_txtLBenRegNo");
		CoBorrower_txtLBenRequestNo = (String) request.get("CoBorrower_txtLBenRequestNo");
		CUS_ACC_ID = (BigDecimal) request.get("CUS_ACC_ID");
		CONTRACT_NO = (String) request.get("CONTRACT_NO");
	}

	public Map getResponse() {
		response.put("tblCoBorrowerList", tblCoBorrowerList);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CoBorrowerInsertM")) {
			coborrowerinsertm = new COBORROWERINSERTM(request);
			return coborrowerinsertm;
		} else if(mapping.equals("CoBorrowerUpdateM")) {
			coborrowerupdatem = new COBORROWERUPDATEM(request);
			return coborrowerupdatem;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COBORROWERINSERTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String CoBorrower_txtLBenAccNo = null;
		public String CoBorrower_txtLBenRequestNo = null;
		public String CoBorrower_txtRole = null;
		public String CONTRACT_NO = null;
		public BigDecimal CUS_ACC_ID = null;
		public BigDecimal CUS_ID = null;
		public BigDecimal use_id = null;

		// out args

		public COBORROWERINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CoBorrower_txtLBenAccNo = (String) request.get("CoBorrower_txtLBenAccNo");
			CoBorrower_txtLBenRequestNo = (String) request.get("CoBorrower_txtLBenRequestNo");
			CoBorrower_txtRole = (String) request.get("CoBorrower_txtRole");
			CONTRACT_NO = (String) request.get("CONTRACT_NO");
			CUS_ACC_ID = (BigDecimal) request.get("CUS_ACC_ID");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COBORROWERUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal LOA_COB_ID = null;
		public Date DATE_UNTIL = null;
		public BigDecimal use_id = null;

		// out args

		public COBORROWERUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LOA_COB_ID = (BigDecimal) request.get("LOA_COB_ID");
			DATE_UNTIL = (Date) request.get("DATE_UNTIL");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}