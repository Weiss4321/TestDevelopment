package hr.vestigo.modules.collateral.jcics.coB2;

import java.sql.Timestamp;
import hr.vestigo.framework.common.TableData;
import java.sql.Date;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOB2 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB2/DeclCOB2.java,v 1.2 2015/12/03 08:51:11 hraziv Exp $";
	private Map response = new HashMap();

	public DeclCOB2() {
	}

	// in args
	public String ScreenEntryParam = null;
	public java.lang.Integer ActionListLevel = null;

	// out args
	public TableData tblCollPolRegionMapList = new TableData();

	// inner classes
	public COLLPOLMAPREGIONDIALOGDETAILSMAPPING collpolmapregiondialogdetailsmapping = null;
	public COLLPOLREGIONMAPCLOSEMAPPING collpolregionmapclosemapping = null;
	public COLLPOLREGIONMAPDIALOGINSERTMAPPING collpolregionmapdialoginsertmapping = null;
	public COLLPOLREGIONMAPQBEMAPPING collpolregionmapqbemapping = null;

	public void setRequest(Map request) {
		ScreenEntryParam = (String) request.get("ScreenEntryParam");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblCollPolRegionMapList", tblCollPolRegionMapList);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollPolMapRegionDialogDetailsMapping")) {
			collpolmapregiondialogdetailsmapping = new COLLPOLMAPREGIONDIALOGDETAILSMAPPING(request);
			return collpolmapregiondialogdetailsmapping;
		} else if(mapping.equals("CollPolRegionMapCloseMapping")) {
			collpolregionmapclosemapping = new COLLPOLREGIONMAPCLOSEMAPPING(request);
			return collpolregionmapclosemapping;
		} else if(mapping.equals("CollPolRegionMapDialogInsertMapping")) {
			collpolregionmapdialoginsertmapping = new COLLPOLREGIONMAPDIALOGINSERTMAPPING(request);
			return collpolregionmapdialoginsertmapping;
		} else if(mapping.equals("CollPolRegionMapQBEMapping")) {
			collpolregionmapqbemapping = new COLLPOLREGIONMAPQBEMAPPING(request);
			return collpolregionmapqbemapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLPOLMAPREGIONDIALOGDETAILSMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal POL_REGION_MAP_ID = null;

		// out args
		public String CollPolRegionMapDialog_txtPolMapCode = null;
		public String CollPolRegionMapDialog_txtPolMapName = null;
		public String CollPolMapRegionDialog_txtRegionCode = null;
		public String CollPolMapRegionDialog_txtRegionName = null;
		public Date CollPolMapRegionDialog_txtDateFrom = null;
		public Date CollPolMapRegionDialog_txtDateUntil = null;
		public BigDecimal POL_MAP_ID = null;
		public BigDecimal SysCodValueId = null;
		public String CollPolRegionMapDialog_txtUseName = null;
		public String CollPolRegionMapDialog_txtUseLogin = null;
		public Timestamp CollPolRegionMapDialog_txtUserLock = null;

		public COLLPOLMAPREGIONDIALOGDETAILSMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			POL_REGION_MAP_ID = (BigDecimal) request.get("POL_REGION_MAP_ID");
		}

		public Map getResponse() {
			response.put("CollPolRegionMapDialog_txtPolMapCode", CollPolRegionMapDialog_txtPolMapCode);
			response.put("CollPolRegionMapDialog_txtPolMapName", CollPolRegionMapDialog_txtPolMapName);
			response.put("CollPolMapRegionDialog_txtRegionCode", CollPolMapRegionDialog_txtRegionCode);
			response.put("CollPolMapRegionDialog_txtRegionName", CollPolMapRegionDialog_txtRegionName);
			response.put("CollPolMapRegionDialog_txtDateFrom", CollPolMapRegionDialog_txtDateFrom);
			response.put("CollPolMapRegionDialog_txtDateUntil", CollPolMapRegionDialog_txtDateUntil);
			response.put("POL_MAP_ID", POL_MAP_ID);
			response.put("SysCodValueId", SysCodValueId);
			response.put("CollPolRegionMapDialog_txtUseName", CollPolRegionMapDialog_txtUseName);
			response.put("CollPolRegionMapDialog_txtUseLogin", CollPolRegionMapDialog_txtUseLogin);
			response.put("CollPolRegionMapDialog_txtUserLock", CollPolRegionMapDialog_txtUserLock);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLPOLREGIONMAPCLOSEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal POL_REGION_MAP_ID = null;

		// out args
		public TableData tblCollPolRegionMapList = new TableData();

		public COLLPOLREGIONMAPCLOSEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			POL_REGION_MAP_ID = (BigDecimal) request.get("POL_REGION_MAP_ID");
		}

		public Map getResponse() {
			response.put("tblCollPolRegionMapList", tblCollPolRegionMapList);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLPOLREGIONMAPDIALOGINSERTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal POL_REGION_MAP_ID = null;
		public BigDecimal POL_MAP_ID = null;
		public BigDecimal REGION_ID = null;
		public Date CollPolMapRegionDialog_txtDateFrom = null;
		public Date CollPolMapRegionDialog_txtDateUntil = null;
		public BigDecimal use_id = null;
		public Date ProcessingDate = null;

		// out args

		public COLLPOLREGIONMAPDIALOGINSERTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			POL_REGION_MAP_ID = (BigDecimal) request.get("POL_REGION_MAP_ID");
			POL_MAP_ID = (BigDecimal) request.get("POL_MAP_ID");
			REGION_ID = (BigDecimal) request.get("REGION_ID");
			CollPolMapRegionDialog_txtDateFrom = (Date) request.get("CollPolMapRegionDialog_txtDateFrom");
			CollPolMapRegionDialog_txtDateUntil = (Date) request.get("CollPolMapRegionDialog_txtDateUntil");
			use_id = (BigDecimal) request.get("use_id");
			ProcessingDate = (Date) request.get("ProcessingDate");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLPOLREGIONMAPQBEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args
		public BigDecimal use_id = null;
		public String ScreenEntryParam = null;
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal POL_MAP_ID = null;
		public BigDecimal REGION_ID = null;
		public Date CollPolRegionMapQBE_txtDateFrom = null;
		public Date CollPolRegionMapQBE_txtDateUntil = null;

		// out args
		public TableData tblCollPolRegionMapList = new TableData();

		public COLLPOLREGIONMAPQBEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			ScreenEntryParam = (String) request.get("ScreenEntryParam");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			POL_MAP_ID = (BigDecimal) request.get("POL_MAP_ID");
			REGION_ID = (BigDecimal) request.get("REGION_ID");
			CollPolRegionMapQBE_txtDateFrom = (Date) request.get("CollPolRegionMapQBE_txtDateFrom");
			CollPolRegionMapQBE_txtDateUntil = (Date) request.get("CollPolRegionMapQBE_txtDateUntil");
		}

		public Map getResponse() {
			response.put("tblCollPolRegionMapList", tblCollPolRegionMapList);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}