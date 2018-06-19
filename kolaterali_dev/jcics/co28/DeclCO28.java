package hr.vestigo.modules.collateral.jcics.co28;

import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO28 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co28/DeclCO28.java,v 1.4 2012/05/24 09:20:49 hramkr Exp $";
	private Map response = new HashMap();

	public DeclCO28() {
	}
 
	// in args
	public Integer LookUpLevel = null;
	public BigDecimal cus_id = null;
	public String cus_acc_no = null;

	// out args
	public TableData LookUpTableData = new TableData();

	// inner classes
	public CUSACCACCOUNTROBILOOKUPMAP cusaccaccountrobilookupmap = null;
	public KOLCUSACCEXPSELECTM kolcusaccexpselectm = null;

	public void setRequest(Map request) {
		LookUpLevel = (Integer) request.get("LookUpLevel");
		cus_id = (BigDecimal) request.get("cus_id");
		cus_acc_no = (String) request.get("cus_acc_no");
	}

	public Map getResponse() {
		response.put("LookUpTableData", LookUpTableData);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CusaccAccountROBILookUpMap")) {
			cusaccaccountrobilookupmap = new CUSACCACCOUNTROBILOOKUPMAP(request);
			return cusaccaccountrobilookupmap;
		} else if(mapping.equals("KolCusaccExpSelectM")) {
			kolcusaccexpselectm = new KOLCUSACCEXPSELECTM(request);
			return kolcusaccexpselectm;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class CUSACCACCOUNTROBILOOKUPMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public Integer LookUpLevel = null;
		public String cus_acc_no = null;
		public String frame_cus_acc_no = null;

		// out args
		public TableData LookUpTableData = new TableData();

		public CUSACCACCOUNTROBILOOKUPMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			LookUpLevel = (Integer) request.get("LookUpLevel");
			cus_acc_no = (String) request.get("cus_acc_no");
			frame_cus_acc_no = (String) request.get("frame_cus_acc_no");
		}

		public Map getResponse() {
			response.put("LookUpTableData", LookUpTableData);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class KOLCUSACCEXPSELECTM implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public String CusAcc_txt = null;
		public BigDecimal Customer_Id = null;

		// out args
		public TableData CusAcc_tbl = new TableData();

		public KOLCUSACCEXPSELECTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CusAcc_txt = (String) request.get("CusAcc_txt");
			Customer_Id = (BigDecimal) request.get("Customer_Id");
		}

		public Map getResponse() {
			response.put("CusAcc_tbl", CusAcc_tbl);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}