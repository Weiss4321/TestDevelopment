package hr.vestigo.modules.collateral.jcics.coB3;

import hr.vestigo.framework.common.TableData;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOB3 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB3/DeclCOB3.java,v 1.2 2017/04/14 10:13:07 hraziv Exp $";
	private Map response = new HashMap();

	public DeclCOB3() {
	}

	// in args
	public String ScreenEntryParam = null;
	public java.lang.Integer ActionListLevel = null;

	// out args
	public TableData tblCollMappingList = new TableData();

	// inner classes
	public COLLMAPPINGDIALOGDETAILSMAPPING collmappingdialogdetailsmapping = null;

	public void setRequest(Map request) {
		ScreenEntryParam = (String) request.get("ScreenEntryParam");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblCollMappingList", tblCollMappingList);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollMappingDialogDetailsMapping")) {
			collmappingdialogdetailsmapping = new COLLMAPPINGDIALOGDETAILSMAPPING(request);
			return collmappingdialogdetailsmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLMAPPINGDIALOGDETAILSMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal COL_GCM_TYP_ID = null;
		public BigDecimal COL_GCM_TYP_MAP_ID = null;

		// out args
		public String CollMappingDialog_txtCollateralCategoryCode = null;
		public String CollMappingDialog_txtCollateralCategoryName = null;
		public String CollMappingDialog_ColSubCode = null;
		public String CollMappingDialog_ColSubName = null;
		public String CollMappingDialog_txtColGroCode = null;
		public String CollMappingDialog_txtColGroName = null;
		public BigDecimal COL_CAT_ID = null;
		public BigDecimal COL_TYP_ID = null;
		public BigDecimal COL_SUB_ID = null;
		public String CollMappingDialog_txtName = null;
		public String CollMappingDialog_txtCode = null;
		public String CollMappingDialog_txtCollateralTypeCode = null;
		public String CollMappingDialog_txtCollateralTypeName = null;
		public BigDecimal COL_GRO_ID = null;
		public String CollMappingDialog_txtMapCode = null;

		public COLLMAPPINGDIALOGDETAILSMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COL_GCM_TYP_ID = (BigDecimal) request.get("COL_GCM_TYP_ID");
			COL_GCM_TYP_MAP_ID = (BigDecimal) request.get("COL_GCM_TYP_MAP_ID");
		}

		public Map getResponse() {
			response.put("CollMappingDialog_txtCollateralCategoryCode", CollMappingDialog_txtCollateralCategoryCode);
			response.put("CollMappingDialog_txtCollateralCategoryName", CollMappingDialog_txtCollateralCategoryName);
			response.put("CollMappingDialog_ColSubCode", CollMappingDialog_ColSubCode);
			response.put("CollMappingDialog_ColSubName", CollMappingDialog_ColSubName);
			response.put("CollMappingDialog_txtColGroCode", CollMappingDialog_txtColGroCode);
			response.put("CollMappingDialog_txtColGroName", CollMappingDialog_txtColGroName);
			response.put("COL_CAT_ID", COL_CAT_ID);
			response.put("COL_TYP_ID", COL_TYP_ID);
			response.put("COL_SUB_ID", COL_SUB_ID);
			response.put("CollMappingDialog_txtName", CollMappingDialog_txtName);
			response.put("CollMappingDialog_txtCode", CollMappingDialog_txtCode);
			response.put("CollMappingDialog_txtCollateralTypeCode", CollMappingDialog_txtCollateralTypeCode);
			response.put("CollMappingDialog_txtCollateralTypeName", CollMappingDialog_txtCollateralTypeName);
			response.put("COL_GRO_ID", COL_GRO_ID);
			response.put("CollMappingDialog_txtMapCode", CollMappingDialog_txtMapCode);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}