package hr.vestigo.modules.collateral.jcics.co12;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.remote.transaction.Decl;
import hr.vestigo.framework.remote.transaction.MappingDecl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DeclCO12 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co12/DeclCO12.java,v 1.8 2006/04/14 10:07:13 hrarmv Exp $";
	private Map response = new HashMap();

	public DeclCO12() {
	}

	//	in args
	public BigDecimal POL_MAP_ID_PLACE = null;
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblPolMapDistrict = new TableData();

	// inner classes
	public POLMAPDISTRICTINSERTMAPPING polmapdistrictinsertmapping = null;
	public POLMAPDISTRICTDELETEMAPPING polmapdistrictdeletemapping = null;
	public POLMAPDISTRICTUPDATEMAPPING polmapdistrictupdatemapping = null;
	public POLMAPRESIQUARLISTMAPPING polmapresiquarlistmapping = null;
	public POLMAPSELECTTYPEMAPPING polmapselecttypemapping = null;
	public POLMAPRESIQUARDELETEMAPPING polmapresiquardeletemapping = null;
	public POLMAPRESIQUARINSERTMAPPING polmapresiquarinsertmapping = null;
	public POLMAPRESIQUARUPDATEMAPPING polmapresiquarupdatemapping = null;

	public void setRequest(Map request) {
		POL_MAP_ID_PLACE = (BigDecimal) request.get("POL_MAP_ID_PLACE");
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblPolMapDistrict", tblPolMapDistrict);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("PolMapDistrictInsertMapping")) {
			polmapdistrictinsertmapping = new POLMAPDISTRICTINSERTMAPPING(request);
			return polmapdistrictinsertmapping;
		} else if(mapping.equals("PolMapDistrictDeleteMapping")) {
			polmapdistrictdeletemapping = new POLMAPDISTRICTDELETEMAPPING(request);
			return polmapdistrictdeletemapping;
		} else if(mapping.equals("PolMapDistrictUpdateMapping")) {
			polmapdistrictupdatemapping = new POLMAPDISTRICTUPDATEMAPPING(request);
			return polmapdistrictupdatemapping;
		} else if(mapping.equals("PolMapResiQuarListMapping")) {
			polmapresiquarlistmapping = new POLMAPRESIQUARLISTMAPPING(request);
			return polmapresiquarlistmapping;
		} else if(mapping.equals("PolMapSelectTypeMapping")) {
			polmapselecttypemapping = new POLMAPSELECTTYPEMAPPING(request);
			return polmapselecttypemapping;
		} else if(mapping.equals("PolMapResiQuarDeleteMapping")) {
			polmapresiquardeletemapping = new POLMAPRESIQUARDELETEMAPPING(request);
			return polmapresiquardeletemapping;
		} else if(mapping.equals("PolMapResiQuarInsertMapping")) {
			polmapresiquarinsertmapping = new POLMAPRESIQUARINSERTMAPPING(request);
			return polmapresiquarinsertmapping;
		} else if(mapping.equals("PolMapResiQuarUpdateMapping")) {
			polmapresiquarupdatemapping = new POLMAPRESIQUARUPDATEMAPPING(request);
			return polmapresiquarupdatemapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class POLMAPDISTRICTINSERTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal POL_MAP_IDD_PLACE = null;
		public String txtDistrictName = null;
		public String txtDistrictCode = null;

		//	out args

		public POLMAPDISTRICTINSERTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			POL_MAP_IDD_PLACE = (BigDecimal) request.get("POL_MAP_IDD_PLACE");
			txtDistrictName = (String) request.get("txtDistrictName");
			txtDistrictCode = (String) request.get("txtDistrictCode");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class POLMAPDISTRICTDELETEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal DISTRICT_ID = null;
		public Timestamp USER_LOCK = null;

		//	out args

		public POLMAPDISTRICTDELETEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			DISTRICT_ID = (BigDecimal) request.get("DISTRICT_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class POLMAPDISTRICTUPDATEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal DISTRICT_ID = null;
		public String txtDistrictName = null;
		public String txtDistrictCode = null;
		public Timestamp USER_LOCK = null;

		//	out args

		public POLMAPDISTRICTUPDATEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			DISTRICT_ID = (BigDecimal) request.get("DISTRICT_ID");
			txtDistrictName = (String) request.get("txtDistrictName");
			txtDistrictCode = (String) request.get("txtDistrictCode");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class POLMAPRESIQUARLISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal POL_MAP_ID_PLACE = null;
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal POL_MAP_ID_DISTRICT = null;

		//	out args
		public TableData tblPolMapResiQuar = new TableData();

		public POLMAPRESIQUARLISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			POL_MAP_ID_PLACE = (BigDecimal) request.get("POL_MAP_ID_PLACE");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			POL_MAP_ID_DISTRICT = (BigDecimal) request.get("POL_MAP_ID_DISTRICT");
		}

		public Map getResponse() {
			response.put("tblPolMapResiQuar", tblPolMapResiQuar);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class POLMAPSELECTTYPEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal POL_MAP_IDD_DISTRICT = null;
		public BigDecimal POL_MAP_IDD_RESIQ = null;
		public BigDecimal POL_MAP_TYP_ID_ResiQuar = null;
		public BigDecimal POL_MAP_TYP_ID_District = null;

		//	out args
		public String PolMapDistrictDialog_txtDistrictCode = null;
		public String PolMapDistrictDialog_txtDistrictName = null;
		public Timestamp District_USER_LOCK = null;
		public String PolMapResiQuarDialog_txtResiQuarCode = null;
		public String PolMapResiQuarDialog_txtResiQuarName = null;
		public Timestamp ResiQuar_USER_LOCK = null;

		public POLMAPSELECTTYPEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			POL_MAP_IDD_DISTRICT = (BigDecimal) request.get("POL_MAP_IDD_DISTRICT");
			POL_MAP_IDD_RESIQ = (BigDecimal) request.get("POL_MAP_IDD_RESIQ");
			POL_MAP_TYP_ID_ResiQuar = (BigDecimal) request.get("POL_MAP_TYP_ID_ResiQuar");
			POL_MAP_TYP_ID_District = (BigDecimal) request.get("POL_MAP_TYP_ID_District");
		}

		public Map getResponse() {
			response.put("PolMapDistrictDialog_txtDistrictCode", PolMapDistrictDialog_txtDistrictCode);
			response.put("PolMapDistrictDialog_txtDistrictName", PolMapDistrictDialog_txtDistrictName);
			response.put("District_USER_LOCK", District_USER_LOCK);
			response.put("PolMapResiQuarDialog_txtResiQuarCode", PolMapResiQuarDialog_txtResiQuarCode);
			response.put("PolMapResiQuarDialog_txtResiQuarName", PolMapResiQuarDialog_txtResiQuarName);
			response.put("ResiQuar_USER_LOCK", ResiQuar_USER_LOCK);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class POLMAPRESIQUARDELETEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal RESIQ_ID = null;
		public Timestamp USER_LOCK = null;

		//	out args

		public POLMAPRESIQUARDELETEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			RESIQ_ID = (BigDecimal) request.get("RESIQ_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class POLMAPRESIQUARINSERTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal POL_MAP_IDD_DISTRICT = null;
		public String txtResiQuarCode = null;
		public String txtResiQuarName = null;

		//	out args

		public POLMAPRESIQUARINSERTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			POL_MAP_IDD_DISTRICT = (BigDecimal) request.get("POL_MAP_IDD_DISTRICT");
			txtResiQuarCode = (String) request.get("txtResiQuarCode");
			txtResiQuarName = (String) request.get("txtResiQuarName");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class POLMAPRESIQUARUPDATEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal RESIQ_ID = null;
		public String txtResiQuarName = null;
		public String txtResiQuarCode = null;
		public Timestamp USER_LOCK = null;

		//	out args

		public POLMAPRESIQUARUPDATEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			RESIQ_ID = (BigDecimal) request.get("RESIQ_ID");
			txtResiQuarName = (String) request.get("txtResiQuarName");
			txtResiQuarCode = (String) request.get("txtResiQuarCode");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}