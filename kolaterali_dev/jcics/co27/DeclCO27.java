package hr.vestigo.modules.collateral.jcics.co27;

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

public class DeclCO27 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co27/DeclCO27.java,v 1.7 2007/08/29 13:23:01 hramkr Exp $";
	private Map response = new HashMap();

	public DeclCO27() {
	}
  
	//	in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal COL_HEA_ID = null;

	//	out args
	public TableData tblCusaccExpColl = new TableData();
	public BigDecimal CusaccExpColl_txtCovAmount = null;
	public BigDecimal CusaccExpColl_txtExpAmount = null;
	public String CusaccExpColl_txtExpCodeNum = null;
	public String CusaccExpColl_txtExpCodeChar = null;
	public String CusaccExpColl_txtCovCodeNum = null;
	public String CusaccExpColl_txtCovCodeChar = null;

	// inner classes
	public CUSACCEXPCOLLDETAILSMAPPING cusaccexpcolldetailsmapping = null;
	public CUSACCEXPCOLLQBE2MAPPING cusaccexpcollqbe2mapping = null;
	public CUSACCEXPCOLLQBEMAPPING cusaccexpcollqbemapping = null;
	public COLWORKLISTCOVMAPPING colworklistcovmapping = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
	}

	public Map getResponse() {
		response.put("tblCusaccExpColl", tblCusaccExpColl);
		response.put("CusaccExpColl_txtCovAmount", CusaccExpColl_txtCovAmount);
		response.put("CusaccExpColl_txtExpAmount", CusaccExpColl_txtExpAmount);
		response.put("CusaccExpColl_txtExpCodeNum", CusaccExpColl_txtExpCodeNum);
		response.put("CusaccExpColl_txtExpCodeChar", CusaccExpColl_txtExpCodeChar);
		response.put("CusaccExpColl_txtCovCodeNum", CusaccExpColl_txtCovCodeNum);
		response.put("CusaccExpColl_txtCovCodeChar", CusaccExpColl_txtCovCodeChar);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CusaccExpCollDetailsMapping")) {
			cusaccexpcolldetailsmapping = new CUSACCEXPCOLLDETAILSMAPPING(request);
			return cusaccexpcolldetailsmapping;
		} else if(mapping.equals("CusaccExpCollQBE2Mapping")) {
			cusaccexpcollqbe2mapping = new CUSACCEXPCOLLQBE2MAPPING(request);
			return cusaccexpcollqbe2mapping;
		} else if(mapping.equals("CusaccExpCollQBEMapping")) {
			cusaccexpcollqbemapping = new CUSACCEXPCOLLQBEMAPPING(request);
			return cusaccexpcollqbemapping;
		} else if(mapping.equals("ColWorkListCovMapping")) {
			colworklistcovmapping = new COLWORKLISTCOVMAPPING(request);
			return colworklistcovmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class CUSACCEXPCOLLDETAILSMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal CUS_ACC_ID = null;
		public java.lang.Integer ActionListLevel = null;
		public Date VALUE_DATE = null;
		public BigDecimal COL_PRO_ID = null;

		//	out args
		public TableData tblCusaccExpCollFOI = new TableData();
		public String CusaccExpCollFOI_dynLblInternCustNum = null;
		public String CusaccExpCollFOI_dynLblInvestment = null;

		public CUSACCEXPCOLLDETAILSMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CUS_ACC_ID = (BigDecimal) request.get("CUS_ACC_ID");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			VALUE_DATE = (Date) request.get("VALUE_DATE");
			COL_PRO_ID = (BigDecimal) request.get("COL_PRO_ID");
		}

		public Map getResponse() {
			response.put("tblCusaccExpCollFOI", tblCusaccExpCollFOI);
			response.put("CusaccExpCollFOI_dynLblInternCustNum", CusaccExpCollFOI_dynLblInternCustNum);
			response.put("CusaccExpCollFOI_dynLblInvestment", CusaccExpCollFOI_dynLblInvestment);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CUSACCEXPCOLLQBE2MAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal CUS_ACC_ID = null;
		public BigDecimal CUS_ID = null;
		public java.lang.Integer ActionListLevel = null;

		//	out args
		public TableData tblCusaccExpColl = new TableData();
		public String CusaccExpColl_dynLblSearchCriteria = null;

		public CUSACCEXPCOLLQBE2MAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			CUS_ACC_ID = (BigDecimal) request.get("CUS_ACC_ID");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		}

		public Map getResponse() {
			response.put("tblCusaccExpColl", tblCusaccExpColl);
			response.put("CusaccExpColl_dynLblSearchCriteria", CusaccExpColl_dynLblSearchCriteria);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CUSACCEXPCOLLQBEMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal COL_HEA_ID = null;
		public BigDecimal CUS_ACC_ID = null;
		public BigDecimal CUS_ID = null;

		//	out args
		public TableData tblCusaccExpColl = new TableData();

		public CUSACCEXPCOLLQBEMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
			CUS_ACC_ID = (BigDecimal) request.get("CUS_ACC_ID");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
		}

		public Map getResponse() {
			response.put("tblCusaccExpColl", tblCusaccExpColl);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLWORKLISTCOVMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public BigDecimal COL_HEA_ID = null;
		public java.lang.Integer ActionListLevel = null;

		//	out args
		public TableData tblColWorkListCov = new TableData();
		public String ColWorkListCov_dynLblCollCurrency = null;
		public String ColWorkListCov_dynLblCollParty = null;
		public String ColWorkListCov_dynLblCollType = null;
		public String ColWorkListCov_dynLblCollValue = null;
		public BigDecimal ColWorkListCov_dynLblPonder = null;

		public COLWORKLISTCOVMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			COL_HEA_ID = (BigDecimal) request.get("COL_HEA_ID");
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		}

		public Map getResponse() {
			response.put("tblColWorkListCov", tblColWorkListCov);
			response.put("ColWorkListCov_dynLblCollCurrency", ColWorkListCov_dynLblCollCurrency);
			response.put("ColWorkListCov_dynLblCollParty", ColWorkListCov_dynLblCollParty);
			response.put("ColWorkListCov_dynLblCollType", ColWorkListCov_dynLblCollType);
			response.put("ColWorkListCov_dynLblCollValue", ColWorkListCov_dynLblCollValue);
			response.put("ColWorkListCov_dynLblPonder", ColWorkListCov_dynLblPonder);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}