package hr.vestigo.modules.collateral.jcics.co04;

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

public class DeclCO04 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co04/DeclCO04.java,v 1.11 2006/05/23 07:49:45 hramkr Exp $";
	private Map response = new HashMap();

	public DeclCO04() {
	}

	//	in args
	public java.lang.Integer ActionListLevel = null;
	public BigDecimal CUS_ID = null;
	public String CollateralSubCategory = null;

	//	out args
	public TableData tblCollSecPaper = new TableData();

	// inner classes
	public COLLMOVABLELISTMAPPING collmovablelistmapping = null;
	public COLLSUPPLYLISTMAPPING collsupplylistmapping = null;
	public COLLARTLISTMAPPING collartlistmapping = null;
	public COLLPRECLISTMAPPING collpreclistmapping = null;
	public VESSELLISTMAPPING vessellistmapping = null;
	public CASHDEPLISTMAPPING cashdeplistmapping = null;
	public COLLINSPOLLISTMAPPING collinspollistmapping = null;
	public COLLGUARANTLISTMAPPING collguarantlistmapping = null;
	public COLLLOANSTOCKLISTMAP collloanstocklistmap = null;

	public void setRequest(Map request) {
		ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
		CUS_ID = (BigDecimal) request.get("CUS_ID");
		CollateralSubCategory = (String) request.get("CollateralSubCategory");
	}

	public Map getResponse() {
		response.put("tblCollSecPaper", tblCollSecPaper);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstracat superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollMovableListMapping")) {
			collmovablelistmapping = new COLLMOVABLELISTMAPPING(request);
			return collmovablelistmapping;
		} else if(mapping.equals("CollSupplyListMapping")) {
			collsupplylistmapping = new COLLSUPPLYLISTMAPPING(request);
			return collsupplylistmapping;
		} else if(mapping.equals("CollArtListMapping")) {
			collartlistmapping = new COLLARTLISTMAPPING(request);
			return collartlistmapping;
		} else if(mapping.equals("CollPrecListMapping")) {
			collpreclistmapping = new COLLPRECLISTMAPPING(request);
			return collpreclistmapping;
		} else if(mapping.equals("VesselListMapping")) {
			vessellistmapping = new VESSELLISTMAPPING(request);
			return vessellistmapping;
		} else if(mapping.equals("CashDepListMapping")) {
			cashdeplistmapping = new CASHDEPLISTMAPPING(request);
			return cashdeplistmapping;
		} else if(mapping.equals("CollInsPolListMapping")) {
			collinspollistmapping = new COLLINSPOLLISTMAPPING(request);
			return collinspollistmapping;
		} else if(mapping.equals("CollGuarantListMapping")) {
			collguarantlistmapping = new COLLGUARANTLISTMAPPING(request);
			return collguarantlistmapping;
		} else if(mapping.equals("CollLoanStockListMap")) {
			collloanstocklistmap = new COLLLOANSTOCKLISTMAP(request);
			return collloanstocklistmap;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLMOVABLELISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblCollMovable = new TableData();

		public COLLMOVABLELISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblCollMovable", tblCollMovable);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLSUPPLYLISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblCollSupply = new TableData();

		public COLLSUPPLYLISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblCollSupply", tblCollSupply);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLARTLISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblCollArt = new TableData();

		public COLLARTLISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblCollArt", tblCollArt);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLPRECLISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblCollPrec = new TableData();

		public COLLPRECLISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblCollPrec", tblCollPrec);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class VESSELLISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblVessel = new TableData();

		public VESSELLISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblVessel", tblVessel);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class CASHDEPLISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblCashDep = new TableData();

		public CASHDEPLISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblCashDep", tblCashDep);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLINSPOLLISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblCollInsPol = new TableData();

		public COLLINSPOLLISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblCollInsPol", tblCollInsPol);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLGUARANTLISTMAPPING implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblCollGuarant = new TableData();

		public COLLGUARANTLISTMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblCollGuarant", tblCollGuarant);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

	public class COLLLOANSTOCKLISTMAP implements MappingDecl {
		public Map response = new HashMap();

		//	in args
		public java.lang.Integer ActionListLevel = null;
		public BigDecimal CUS_ID = null;
		public String CollateralSubCategory = null;

		//	out args
		public TableData tblCollLoanStock = new TableData();

		public COLLLOANSTOCKLISTMAP(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
			ActionListLevel = (java.lang.Integer) request.get("ActionListLevel");
			CUS_ID = (BigDecimal) request.get("CUS_ID");
			CollateralSubCategory = (String) request.get("CollateralSubCategory");
		}

		public Map getResponse() {
			response.put("tblCollLoanStock", tblCollLoanStock);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}