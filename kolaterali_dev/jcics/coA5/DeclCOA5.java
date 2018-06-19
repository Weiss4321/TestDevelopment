package hr.vestigo.modules.collateral.jcics.coA5;

import hr.vestigo.framework.common.TableData;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOA5 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA5/DeclCOA5.java,v 1.1 2010/08/16 12:11:20 hrakis Exp $";
	private Map response = new HashMap();

	public DeclCOA5() {
	}

	// in args
	public String CollEligDetails_txtColNum = null;

	// out args
	public TableData tblHNB = new TableData();
	public String CollEligDetails_txtHNB = null;
	public TableData tblB2Stand = new TableData();
	public String CollEligDetails_txtB2Stand = null;
	public TableData tblB2IRB = new TableData();
	public String CollEligDetails_txtB2IRB = null;
	public TableData tblND = new TableData();
	public String CollEligDetails_txtND = null;
	public String CollEligDetails_txtHNB_desc = null;
	public String CollEligDetails_txtB2Stand_desc = null;
	public String CollEligDetails_txtB2IRB_desc = null;
	public String CollEligDetails_txtND_desc = null;

	// inner classes

	public void setRequest(Map request) {
		CollEligDetails_txtColNum = (String) request.get("CollEligDetails_txtColNum");
	}

	public Map getResponse() {
		response.put("tblHNB", tblHNB);
		response.put("CollEligDetails_txtHNB", CollEligDetails_txtHNB);
		response.put("tblB2Stand", tblB2Stand);
		response.put("CollEligDetails_txtB2Stand", CollEligDetails_txtB2Stand);
		response.put("tblB2IRB", tblB2IRB);
		response.put("CollEligDetails_txtB2IRB", CollEligDetails_txtB2IRB);
		response.put("tblND", tblND);
		response.put("CollEligDetails_txtND", CollEligDetails_txtND);
		response.put("CollEligDetails_txtHNB_desc", CollEligDetails_txtHNB_desc);
		response.put("CollEligDetails_txtB2Stand_desc", CollEligDetails_txtB2Stand_desc);
		response.put("CollEligDetails_txtB2IRB_desc", CollEligDetails_txtB2IRB_desc);
		response.put("CollEligDetails_txtND_desc", CollEligDetails_txtND_desc);

		return response;
	}

	public Map getRefToResponseMap() {
		return response;
	}

	// Implementation of abstract superclass methods
	public MappingDecl createDecl(String mapping, Map request) throws Exception {
		setRequest(request);
		return this;
	}

	// INNER CLASSES
}