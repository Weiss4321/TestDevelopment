package hr.vestigo.modules.collateral.jcics.co17;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.math.*;

import hr.vestigo.framework.common.*;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCO17 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co17/DeclCO17.java,v 1.8 2006/06/07 06:38:27 hrajkl Exp $";
	private Map response = new HashMap();

	public DeclCO17() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;

	//	out args
	public TableData tblRealEstateType = new TableData();

	// inner classes
	public REALESTATETYPEINSERTM realestatetypeinsertm = null;
	public REALESTATETYPEDELETEM realestatetypedeletem = null;
	public REALESTATETYPEUPDATEM realestatetypeupdatem = null;
	public REALESTATETYPEQUERRYM realestatetypequerrym = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
	}

	public Map getResponse() {
		response.put("tblRealEstateType", tblRealEstateType);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("RealEstateTypeInsertM")) {
			realestatetypeinsertm = new REALESTATETYPEINSERTM(request);
			return realestatetypeinsertm;
		} else if(mapping.equals("RealEstateTypeDeleteM")) {
			realestatetypedeletem = new REALESTATETYPEDELETEM(request);
			return realestatetypedeletem;
		} else if(mapping.equals("RealEstateTypeUpdateM")) {
			realestatetypeupdatem = new REALESTATETYPEUPDATEM(request);
			return realestatetypeupdatem;
		} else if(mapping.equals("RealEstateTypeQuerryM")) {
			realestatetypequerrym = new REALESTATETYPEQUERRYM(request);
			return realestatetypequerrym;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class REALESTATETYPEINSERTM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal use_id = null;
		public String RealEstateDailog_txtCode = null;
		public String RealEstateDailog_txtDesc = null;
		public String RealEstateDailog_txtRealEsActNoact = null;
		public BigDecimal COLL_TYPE_ID = null;

		//	out args

		public REALESTATETYPEINSERTM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			use_id = (BigDecimal) request.get("use_id");
			RealEstateDailog_txtCode = (String) request.get("RealEstateDailog_txtCode");
			RealEstateDailog_txtDesc = (String) request.get("RealEstateDailog_txtDesc");
			RealEstateDailog_txtRealEsActNoact = (String) request.get("RealEstateDailog_txtRealEsActNoact");
			COLL_TYPE_ID = (BigDecimal) request.get("COLL_TYPE_ID");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class REALESTATETYPEDELETEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal REAL_ES_TYPE_ID = null;
		public Timestamp USER_LOCK = null;
		public BigDecimal COLL_TYPE_ID_O = null;
		public String RealEstateDailog_txtRealEsActNoact_O = null;
		public String RealEstateDailog_txtDesc_O = null;
		public String RealEstateDailog_txtCode_O = null;
		public BigDecimal USE_OPEN_ID = null;
		public BigDecimal USE_ID = null;
		public Timestamp RealEstateDialog_txtUseLock = null;
		public Timestamp RealEstateDialog_txtOpeningTS = null;
		public BigDecimal use_id = null;

		//	out args

		public REALESTATETYPEDELETEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			REAL_ES_TYPE_ID = (BigDecimal) request.get("REAL_ES_TYPE_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
			COLL_TYPE_ID_O = (BigDecimal) request.get("COLL_TYPE_ID_O");
			RealEstateDailog_txtRealEsActNoact_O = (String) request.get("RealEstateDailog_txtRealEsActNoact_O");
			RealEstateDailog_txtDesc_O = (String) request.get("RealEstateDailog_txtDesc_O");
			RealEstateDailog_txtCode_O = (String) request.get("RealEstateDailog_txtCode_O");
			USE_OPEN_ID = (BigDecimal) request.get("USE_OPEN_ID");
			USE_ID = (BigDecimal) request.get("USE_ID");
			RealEstateDialog_txtUseLock = (Timestamp) request.get("RealEstateDialog_txtUseLock");
			RealEstateDialog_txtOpeningTS = (Timestamp) request.get("RealEstateDialog_txtOpeningTS");
			use_id = (BigDecimal) request.get("use_id");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class REALESTATETYPEUPDATEM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public String RealEstateDailog_txtCode = null;
		public String RealEstateDailog_txtDesc = null;
		public BigDecimal use_id = null;
		public BigDecimal REAL_ES_TYPE_ID = null;
		public Timestamp USER_LOCK = null;
		public BigDecimal COLL_TYPE_ID = null;
		public String RealEstateDailog_txtRealEsActNoact = null;
		public BigDecimal COLL_TYPE_ID_O = null;
		public String RealEstateDailog_txtRealEsActNoact_O = null;
		public String RealEstateDailog_txtDesc_O = null;
		public String RealEstateDailog_txtCode_O = null;
		public BigDecimal USE_OPEN_ID = null;
		public BigDecimal USE_ID = null;
		public Timestamp RealEstateDialog_txtUseLock = null;
		public Timestamp RealEstateDialog_txtOpeningTS = null;

		//	out args

		public REALESTATETYPEUPDATEM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			RealEstateDailog_txtCode = (String) request.get("RealEstateDailog_txtCode");
			RealEstateDailog_txtDesc = (String) request.get("RealEstateDailog_txtDesc");
			use_id = (BigDecimal) request.get("use_id");
			REAL_ES_TYPE_ID = (BigDecimal) request.get("REAL_ES_TYPE_ID");
			USER_LOCK = (Timestamp) request.get("USER_LOCK");
			COLL_TYPE_ID = (BigDecimal) request.get("COLL_TYPE_ID");
			RealEstateDailog_txtRealEsActNoact = (String) request.get("RealEstateDailog_txtRealEsActNoact");
			COLL_TYPE_ID_O = (BigDecimal) request.get("COLL_TYPE_ID_O");
			RealEstateDailog_txtRealEsActNoact_O = (String) request.get("RealEstateDailog_txtRealEsActNoact_O");
			RealEstateDailog_txtDesc_O = (String) request.get("RealEstateDailog_txtDesc_O");
			RealEstateDailog_txtCode_O = (String) request.get("RealEstateDailog_txtCode_O");
			USE_OPEN_ID = (BigDecimal) request.get("USE_OPEN_ID");
			USE_ID = (BigDecimal) request.get("USE_ID");
			RealEstateDialog_txtUseLock = (Timestamp) request.get("RealEstateDialog_txtUseLock");
			RealEstateDialog_txtOpeningTS = (Timestamp) request.get("RealEstateDialog_txtOpeningTS");
		}

		public Map getResponse() {

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class REALESTATETYPEQUERRYM implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public String RealEstateQuerry_txtCode = null;
		public String RealEstateQuerry_txtDesc = null;

		//	out args
		public TableData tblRealEstateType = new TableData();

		public REALESTATETYPEQUERRYM(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			RealEstateQuerry_txtCode = (String) request.get("RealEstateQuerry_txtCode");
			RealEstateQuerry_txtDesc = (String) request.get("RealEstateQuerry_txtDesc");
		}

		public Map getResponse() {
			response.put("tblRealEstateType", tblRealEstateType);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}