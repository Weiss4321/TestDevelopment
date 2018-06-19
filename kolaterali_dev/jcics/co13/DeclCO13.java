package hr.vestigo.modules.collateral.jcics.co13;

import java.sql.Timestamp;
import java.sql.Date;
import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO13 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co13/DeclCO13.java,v 1.16 2011/02/18 10:00:34 hramkr Exp $";
	private Map response = new HashMap();

	public DeclCO13() {
	}
 
	// in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal CollOwners_COL_HEA_ID = null;

	// out args
	public TableData tblCollOwners = new TableData();

	// inner classes
	public COLLOWNERSINSERTMAPPING collownersinsertmapping = null;
	public COLLOWNERSDELETEMAPPING collownersdeletemapping = null;
	public COLLOWNERSUPDATEMAPPING collownersupdatemapping = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		CollOwners_COL_HEA_ID = (BigDecimal) request.get("CollOwners_COL_HEA_ID");
	}

	public Map getResponse() {
		response.put("tblCollOwners", tblCollOwners);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollOwnersInsertMapping")) {
			collownersinsertmapping = new COLLOWNERSINSERTMAPPING(request);
			return collownersinsertmapping;
		} else if(mapping.equals("CollOwnersDeleteMapping")) {
			collownersdeletemapping = new COLLOWNERSDELETEMAPPING(request);
			return collownersdeletemapping;
		} else if(mapping.equals("CollOwnersUpdateMapping")) {
			collownersupdatemapping = new COLLOWNERSUPDATEMAPPING(request);
			return collownersupdatemapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLOWNERSINSERTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String CollOwnersDialog_txtCode = null;
		public BigDecimal CollOwnersDialog_CUS_ID = null;
		public String CollOwnersDialog_txtUseCodeValue = null;
		public String CollOwnersDialog_txtUseCodeDesc = null;
		public String CollOwnersDialog_txtSurname = null;
		public String CollOwnersDialog_txtCustomerName = null;
		public String CollOwnersDialog_txtRegisterNo = null;
		public String CollOwnersDialog_txtFirstName = null;
		public BigDecimal CollOwnersDialog_USE_ID = null;
		public BigDecimal CollOwnersDialog_USE_OPEN_ID = null;
		public Date CollOwnersDialog_DATE_FROM = null;
		public Date CollOwnersDialog_DATE_UNTIL = null;
		public BigDecimal CollOwners_COL_HEA_ID = null;
		public String CollOwnersDialog_NAME_LEG = null;
		public BigDecimal Kol_txtOwnNum = null;
		public String Kol_txtOwnNumOfEstate = null;
		public String Kol_txtMainOwner = null;
		public BigDecimal Kol_txtStatementUseId = null;

		// out args
		public String Kol_B2 = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB = null;
		public String Kol_B2IRB_dsc = null;
		public String Kol_ND = null;
		public String Kol_ND_dsc = null;
		public String Kol_CRMHnb = null;

		public COLLOWNERSINSERTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollOwnersDialog_txtCode = (String) request.get("CollOwnersDialog_txtCode");
			CollOwnersDialog_CUS_ID = (BigDecimal) request.get("CollOwnersDialog_CUS_ID");
			CollOwnersDialog_txtUseCodeValue = (String) request.get("CollOwnersDialog_txtUseCodeValue");
			CollOwnersDialog_txtUseCodeDesc = (String) request.get("CollOwnersDialog_txtUseCodeDesc");
			CollOwnersDialog_txtSurname = (String) request.get("CollOwnersDialog_txtSurname");
			CollOwnersDialog_txtCustomerName = (String) request.get("CollOwnersDialog_txtCustomerName");
			CollOwnersDialog_txtRegisterNo = (String) request.get("CollOwnersDialog_txtRegisterNo");
			CollOwnersDialog_txtFirstName = (String) request.get("CollOwnersDialog_txtFirstName");
			CollOwnersDialog_USE_ID = (BigDecimal) request.get("CollOwnersDialog_USE_ID");
			CollOwnersDialog_USE_OPEN_ID = (BigDecimal) request.get("CollOwnersDialog_USE_OPEN_ID");
			CollOwnersDialog_DATE_FROM = (Date) request.get("CollOwnersDialog_DATE_FROM");
			CollOwnersDialog_DATE_UNTIL = (Date) request.get("CollOwnersDialog_DATE_UNTIL");
			CollOwners_COL_HEA_ID = (BigDecimal) request.get("CollOwners_COL_HEA_ID");
			CollOwnersDialog_NAME_LEG = (String) request.get("CollOwnersDialog_NAME_LEG");
			Kol_txtOwnNum = (BigDecimal) request.get("Kol_txtOwnNum");
			Kol_txtOwnNumOfEstate = (String) request.get("Kol_txtOwnNumOfEstate");
			Kol_txtMainOwner = (String) request.get("Kol_txtMainOwner");
			Kol_txtStatementUseId = (BigDecimal) request.get("Kol_txtStatementUseId");
		}

		public Map getResponse() {
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_ND_dsc", Kol_ND_dsc);
			response.put("Kol_CRMHnb", Kol_CRMHnb);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLOWNERSDELETEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal CollOwnersDialog_USE_ID = null;
		public BigDecimal CollOwnersDialog_COLL_OWN_ID = null;
		public Timestamp CollOwnersDialog_USER_LOCK_NF = null;
		public BigDecimal CollOwners_COL_HEA_ID = null;

		// out args
		public String Kol_B2 = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB = null;
		public String Kol_B2IRB_dsc = null;
		public String Kol_ND = null;
		public String Kol_ND_dsc = null;
		public String Kol_CRMHnb = null;

		public COLLOWNERSDELETEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollOwnersDialog_USE_ID = (BigDecimal) request.get("CollOwnersDialog_USE_ID");
			CollOwnersDialog_COLL_OWN_ID = (BigDecimal) request.get("CollOwnersDialog_COLL_OWN_ID");
			CollOwnersDialog_USER_LOCK_NF = (Timestamp) request.get("CollOwnersDialog_USER_LOCK_NF");
			CollOwners_COL_HEA_ID = (BigDecimal) request.get("CollOwners_COL_HEA_ID");
		}

		public Map getResponse() {
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_ND_dsc", Kol_ND_dsc);
			response.put("Kol_CRMHnb", Kol_CRMHnb);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLOWNERSUPDATEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String CollOwnersDialog_txtCode = null;
		public BigDecimal CollOwnersDialog_CUS_ID = null;
		public String CollOwnersDialog_txtUseCodeValue = null;
		public String CollOwnersDialog_txtUseCodeDesc = null;
		public String CollOwnersDialog_txtSurname = null;
		public String CollOwnersDialog_txtCustomerName = null;
		public String CollOwnersDialog_txtRegisterNo = null;
		public String CollOwnersDialog_txtFirstName = null;
		public BigDecimal CollOwnersDialog_USE_ID = null;
		public Date CollOwnersDialog_DATE_UNTIL = null;
		public BigDecimal CollOwners_COL_HEA_ID = null;
		public String CollOwnersDialog_NAME_LEG = null;
		public Timestamp CollOwnersDialog_USER_LOCK_NF = null;
		public BigDecimal CollOwnersDialog_COLL_OWN_ID = null;
		public BigDecimal Kol_txtOwnNum = null;
		public String Kol_txtOwnNumOfEstate = null;
		public String Kol_txtMainOwner = null;
		public BigDecimal Kol_txtStatementUseId = null;

		// out args
		public String Kol_B2 = null;
		public String Kol_B2_dsc = null;
		public String Kol_HNB = null;
		public String Kol_HNB_dsc = null;
		public String Kol_B2IRB = null;
		public String Kol_B2IRB_dsc = null;
		public String Kol_ND = null;
		public String Kol_ND_dsc = null;
		public String Kol_CRMHnb = null;

		public COLLOWNERSUPDATEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CollOwnersDialog_txtCode = (String) request.get("CollOwnersDialog_txtCode");
			CollOwnersDialog_CUS_ID = (BigDecimal) request.get("CollOwnersDialog_CUS_ID");
			CollOwnersDialog_txtUseCodeValue = (String) request.get("CollOwnersDialog_txtUseCodeValue");
			CollOwnersDialog_txtUseCodeDesc = (String) request.get("CollOwnersDialog_txtUseCodeDesc");
			CollOwnersDialog_txtSurname = (String) request.get("CollOwnersDialog_txtSurname");
			CollOwnersDialog_txtCustomerName = (String) request.get("CollOwnersDialog_txtCustomerName");
			CollOwnersDialog_txtRegisterNo = (String) request.get("CollOwnersDialog_txtRegisterNo");
			CollOwnersDialog_txtFirstName = (String) request.get("CollOwnersDialog_txtFirstName");
			CollOwnersDialog_USE_ID = (BigDecimal) request.get("CollOwnersDialog_USE_ID");
			CollOwnersDialog_DATE_UNTIL = (Date) request.get("CollOwnersDialog_DATE_UNTIL");
			CollOwners_COL_HEA_ID = (BigDecimal) request.get("CollOwners_COL_HEA_ID");
			CollOwnersDialog_NAME_LEG = (String) request.get("CollOwnersDialog_NAME_LEG");
			CollOwnersDialog_USER_LOCK_NF = (Timestamp) request.get("CollOwnersDialog_USER_LOCK_NF");
			CollOwnersDialog_COLL_OWN_ID = (BigDecimal) request.get("CollOwnersDialog_COLL_OWN_ID");
			Kol_txtOwnNum = (BigDecimal) request.get("Kol_txtOwnNum");
			Kol_txtOwnNumOfEstate = (String) request.get("Kol_txtOwnNumOfEstate");
			Kol_txtMainOwner = (String) request.get("Kol_txtMainOwner");
			Kol_txtStatementUseId = (BigDecimal) request.get("Kol_txtStatementUseId");
		}

		public Map getResponse() {
			response.put("Kol_B2", Kol_B2);
			response.put("Kol_B2_dsc", Kol_B2_dsc);
			response.put("Kol_HNB", Kol_HNB);
			response.put("Kol_HNB_dsc", Kol_HNB_dsc);
			response.put("Kol_B2IRB", Kol_B2IRB);
			response.put("Kol_B2IRB_dsc", Kol_B2IRB_dsc);
			response.put("Kol_ND", Kol_ND);
			response.put("Kol_ND_dsc", Kol_ND_dsc);
			response.put("Kol_CRMHnb", Kol_CRMHnb);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}