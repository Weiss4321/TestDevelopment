package hr.vestigo.modules.collateral.jcics.coA4;

import java.sql.Date;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA4 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA4/DeclCOA4.java,v 1.3 2014/09/18 10:37:41 hrakis Exp $";
	private Map response = new HashMap();

	public DeclCOA4() {
	}

	// in args
	public Date CollTypeReport_txtDate = null;

	// out args
	public Integer BrojZapisa = null;
	public String IndikatorArhive = null;

	// inner classes
	public COLLREVADATESMAPPING collrevadatesmapping = null;

	public void setRequest(Map request) {
		CollTypeReport_txtDate = (Date) request.get("CollTypeReport_txtDate");
	}

	public Map getResponse() {
		response.put("BrojZapisa", BrojZapisa);
		response.put("IndikatorArhive", IndikatorArhive);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		if(mapping.equals("CollRevaDatesMapping")) {
			collrevadatesmapping = new COLLREVADATESMAPPING(request);
			return collrevadatesmapping;
		} else {
			setRequest(request);
			return this;
		}
	}

	// INNER CLASSES
	public class COLLREVADATESMAPPING implements MappingDecl {
		public Map response = new HashMap();

		// in args

		// out args
		public Date CollReva_txtCoefDate = null;
		public Date CollReva_txtCalcDate = null;

		public COLLREVADATESMAPPING(Map request) {
			setRequest(request);
		}

		public void setRequest(Map request) {
		}

		public Map getResponse() {
			response.put("CollReva_txtCoefDate", CollReva_txtCoefDate);
			response.put("CollReva_txtCalcDate", CollReva_txtCalcDate);

			return response;
		}

		public Map getRefToResponseMap() {
			return response;
		}

	}

}