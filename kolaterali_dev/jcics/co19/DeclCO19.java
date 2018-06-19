package hr.vestigo.modules.collateral.jcics.co19;

import java.sql.Timestamp;
import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO19 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co19/DeclCO19.java,v 1.4 2015/01/13 08:26:43 hrajkl Exp $";
	private Map response = new HashMap();

	public DeclCO19() {
	}

	// in args
	public java.lang.Integer ActionListLevel = null;

	// out args
	public TableData tblInsuPolicyType = new TableData();

	// inner classes
	public INSUPOLICYTYPEINSERTM insupolicytypeinsertm = null;
	public INSUPOLICYTYPEDELETEM insupolicytypedeletem = null;
	public INSUPOLICYTYPEUPDATEM insupolicytypeupdatem = null;
	public INSUPOLICYTYPEQUERRYM insupolicytypequerrym = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblInsuPolicyType", tblInsuPolicyType);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("InsuPolicyTypeInsertM")) {
			insupolicytypeinsertm = new INSUPOLICYTYPEINSERTM(request);
			return insupolicytypeinsertm;
		} else if(mapping.equals("InsuPolicyTypeDeleteM")) {
			insupolicytypedeletem = new INSUPOLICYTYPEDELETEM(request);
			return insupolicytypedeletem;
		} else if(mapping.equals("InsuPolicyTypeUpdateM")) {
			insupolicytypeupdatem = new INSUPOLICYTYPEUPDATEM(request);
			return insupolicytypeupdatem;
		} else if(mapping.equals("InsuPolicyTypeQuerryM")) {
			insupolicytypequerrym = new INSUPOLICYTYPEQUERRYM(request);
			return insupolicytypequerrym;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class INSUPOLICYTYPEINSERTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal use_id = null;
		public String STATUS = null;
		public String InsuPolicyTypeDialog_txtIntPolTypeCode = null;
		public String InsuPolicyTypeDialog_txtIntPolTypeName = null;
		public Date InsuPolicyTypeDialog_txtDateFrom = null;
		public Date InsuPolicyTypeDialog_txtDateUntil = null;
		public BigDecimal INT_POL_COMPANY_ID = null;
		public String InsuPolicyTypeDialog_txtICCode = null;
		public String INT_GROUP1 = null;
		public String INT_POL_SPEC_STAT = null;
		public String INT_GROUP2 = null;
		public String INT_GROUP3 = null;
		public String INT_GROUP4 = null;

		// out args

		public INSUPOLICYTYPEINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			STATUS = (String) request.get("STATUS");
			InsuPolicyTypeDialog_txtIntPolTypeCode = (String) request.get("InsuPolicyTypeDialog_txtIntPolTypeCode");
			InsuPolicyTypeDialog_txtIntPolTypeName = (String) request.get("InsuPolicyTypeDialog_txtIntPolTypeName");
			InsuPolicyTypeDialog_txtDateFrom = (Date) request.get("InsuPolicyTypeDialog_txtDateFrom");
			InsuPolicyTypeDialog_txtDateUntil = (Date) request.get("InsuPolicyTypeDialog_txtDateUntil");
			INT_POL_COMPANY_ID = (BigDecimal) request.get("INT_POL_COMPANY_ID");
			InsuPolicyTypeDialog_txtICCode = (String) request.get("InsuPolicyTypeDialog_txtICCode");
			INT_GROUP1 = (String) request.get("INT_GROUP1");
			INT_POL_SPEC_STAT = (String) request.get("INT_POL_SPEC_STAT");
			INT_GROUP2 = (String) request.get("INT_GROUP2");
			INT_GROUP3 = (String) request.get("INT_GROUP3");
			INT_GROUP4 = (String) request.get("INT_GROUP4");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSUPOLICYTYPEDELETEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal INT_POL_TYPE_ID = null;
		public Timestamp USER_LOCK = null;

		// out args

		public INSUPOLICYTYPEDELETEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			INT_POL_TYPE_ID = (BigDecimal) request.get("INT_POL_TYPE_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSUPOLICYTYPEUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal INT_POL_TYPE_ID = null;
		public BigDecimal INT_POL_COMPANY_ID = null;
		public Timestamp USER_LOCK = null;
		public Date InsuPolicyTypeDialog_txtDateUntil = null;
		public Date InsuPolicyTypeDialog_txtDateFrom = null;
		public String InsuPolicyTypeDialog_txtIntPolTypeName = null;
		public String InsuPolicyTypeDialog_txtIntPolTypeCode = null;
		public BigDecimal use_id = null;
		public String INT_GROUP1 = null;
		public String INT_GROUP2 = null;
		public String INT_GROUP3 = null;
		public String INT_GROUP4 = null;

		// out args

		public INSUPOLICYTYPEUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			INT_POL_TYPE_ID = (BigDecimal) request.get("INT_POL_TYPE_ID");
			INT_POL_COMPANY_ID = (BigDecimal) request.get("INT_POL_COMPANY_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
			InsuPolicyTypeDialog_txtDateUntil = (Date) request.get("InsuPolicyTypeDialog_txtDateUntil");
			InsuPolicyTypeDialog_txtDateFrom = (Date) request.get("InsuPolicyTypeDialog_txtDateFrom");
			InsuPolicyTypeDialog_txtIntPolTypeName = (String) request.get("InsuPolicyTypeDialog_txtIntPolTypeName");
			InsuPolicyTypeDialog_txtIntPolTypeCode = (String) request.get("InsuPolicyTypeDialog_txtIntPolTypeCode");
			use_id = (BigDecimal) request.get("use_id");
			INT_GROUP1 = (String) request.get("INT_GROUP1");
			INT_GROUP2 = (String) request.get("INT_GROUP2");
			INT_GROUP3 = (String) request.get("INT_GROUP3");
			INT_GROUP4 = (String) request.get("INT_GROUP4");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class INSUPOLICYTYPEQUERRYM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public java.lang.Integer ActionListLevel = null;
		public Date InsuPolicyTypeQuerry_txtValueDate = null;
		public String InsuPolicyTypeQuerry_txtIntPolTypeName = null;
		public String InsuPolicyTypeQuerry_txtIntPolTypeCode = null;

		// out args
		public TableData tblInsuPolicyType = new TableData();

		public INSUPOLICYTYPEQUERRYM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			InsuPolicyTypeQuerry_txtValueDate = (Date) request.get("InsuPolicyTypeQuerry_txtValueDate");
			InsuPolicyTypeQuerry_txtIntPolTypeName = (String) request.get("InsuPolicyTypeQuerry_txtIntPolTypeName");
			InsuPolicyTypeQuerry_txtIntPolTypeCode = (String) request.get("InsuPolicyTypeQuerry_txtIntPolTypeCode");
		}

		public Map getResponse() {
			response.put("tblInsuPolicyType", tblInsuPolicyType);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}