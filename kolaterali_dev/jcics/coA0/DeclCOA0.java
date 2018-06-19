package hr.vestigo.modules.collateral.jcics.coA0;

import java.sql.Date;
import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA0 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA0/DeclCOA0.java,v 1.4 2014/10/07 10:54:19 hrazst Exp $";
	private Map response = new HashMap();

	public DeclCOA0() {
	}

	// in args
	public BigDecimal Mvp_col_hea_id = null;
	public BigDecimal Mvp_txtNewValue = null;
	public Date Mvp_txtFrom = null;
	public Date Mvp_txtUntil = null;
	public BigDecimal Mvp_org_uni_id = null;
	public BigDecimal Mvp_use_id = null;
	public String PONDER_TYPE = null;

	// out args

	// inner classes
	public COPONDERSELECTMAP coponderselectmap = null;
	public COPONDERLISTMAP coponderlistmap = null;
	public COPONDERUPDATEMAP coponderupdatemap = null;
	public WCALISTMAP wcalistmap = null;

	public void setRequest(Map request) {
		Mvp_col_hea_id = (BigDecimal) request.get("Mvp_col_hea_id");
		Mvp_txtNewValue = (BigDecimal) request.get("Mvp_txtNewValue");
		Mvp_txtFrom = (Date) request.get("Mvp_txtFrom");
		Mvp_txtUntil = (Date) request.get("Mvp_txtUntil");
		Mvp_org_uni_id = (BigDecimal) request.get("Mvp_org_uni_id");
		Mvp_use_id = (BigDecimal) request.get("Mvp_use_id");
		PONDER_TYPE = (String) request.get("PONDER_TYPE");
	}

	public Map getResponse() {

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("COPonderSelectMap")) {
			coponderselectmap = new COPONDERSELECTMAP(request);
			return coponderselectmap;
		} else if(mapping.equals("COPonderListMap")) {
			coponderlistmap = new COPONDERLISTMAP(request);
			return coponderlistmap;
		} else if(mapping.equals("COPonderUpdateMap")) {
			coponderupdatemap = new COPONDERUPDATEMAP(request);
			return coponderupdatemap;
		} else if(mapping.equals("WCAListMap")) {
			wcalistmap = new WCALISTMAP(request);
			return wcalistmap;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COPONDERSELECTMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal Mvp_col_hea_id = null;
		public BigDecimal Mvp_col_cat_id = null;
		public BigDecimal Mvp_col_typ_id = null;
		public BigDecimal Mvp_sub_typ_id = null;
		public BigDecimal Mvp_col_pon_id = null;
		public String PONDER_TYPE = null;

		// out args
		public BigDecimal Mvp_txtOldValue = null;
		public BigDecimal Mvp_txtMinValue = null;
		public BigDecimal Mvp_txtMaxValue = null;
		public BigDecimal Mvp_txtNewValue = null;
		public Date Mvp_txtFrom = null;
		public Date Mvp_txtUntil = null;
		public BigDecimal Mvp_txtDflValue = null;

		public COPONDERSELECTMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			Mvp_col_hea_id = (BigDecimal) request.get("Mvp_col_hea_id");
			Mvp_col_cat_id = (BigDecimal) request.get("Mvp_col_cat_id");
			Mvp_col_typ_id = (BigDecimal) request.get("Mvp_col_typ_id");
			Mvp_sub_typ_id = (BigDecimal) request.get("Mvp_sub_typ_id");
			Mvp_col_pon_id = (BigDecimal) request.get("Mvp_col_pon_id");
			PONDER_TYPE = (String) request.get("PONDER_TYPE");
		}

		public Map getResponse() {
			response.put("Mvp_txtOldValue", Mvp_txtOldValue);
			response.put("Mvp_txtMinValue", Mvp_txtMinValue);
			response.put("Mvp_txtMaxValue", Mvp_txtMaxValue);
			response.put("Mvp_txtNewValue", Mvp_txtNewValue);
			response.put("Mvp_txtFrom", Mvp_txtFrom);
			response.put("Mvp_txtUntil", Mvp_txtUntil);
			response.put("Mvp_txtDflValue", Mvp_txtDflValue);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COPONDERLISTMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal Mvp_col_hea_id = null;
		public java.lang.Integer ActionListLevel = null;

		// out args
		public TableData tblMvpPonder = new TableData();
		public TableData tblCESPonder = new TableData();

		public COPONDERLISTMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			Mvp_col_hea_id = (BigDecimal) request.get("Mvp_col_hea_id");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		}

		public Map getResponse() {
			response.put("tblMvpPonder", tblMvpPonder);
			response.put("tblCESPonder", tblCESPonder);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COPONDERUPDATEMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal Mvp_col_pon_id = null;
		public BigDecimal Mvp_txtNewValue = null;
		public Date Mvp_txtUntil = null;

		// out args

		public COPONDERUPDATEMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			Mvp_col_pon_id = (BigDecimal) request.get("Mvp_col_pon_id");
			Mvp_txtNewValue = (BigDecimal) request.get("Mvp_txtNewValue");
			Mvp_txtUntil = (Date) request.get("Mvp_txtUntil");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class WCALISTMAP implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal Mvp_col_hea_id = null;
		public java.lang.Integer ActionListLevel = null;

		// out args
		public TableData tblWCAList = new TableData();

		public WCALISTMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			Mvp_col_hea_id = (BigDecimal) request.get("Mvp_col_hea_id");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		}

		public Map getResponse() {
			response.put("tblWCAList", tblWCAList);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}