package hr.vestigo.modules.collateral.jcics.coB4;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import hr.vestigo.framework.remote.transaction.*;

public class DeclCOB4 implements MappingDecl, Decl {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB4/DeclCOB4.java,v 1.1 2017/02/23 08:17:15 hraaks Exp $";
	private Map response = new HashMap();

	public DeclCOB4() {
	}

	// in args
	public String coll_owner_register_no = null;
	public String aps_number = null;
	public String coll_category = null;
	public String coll_type = null;
	public String register_no = null;
	public BigDecimal amount = null;
	public String cur_code_num = null;
	public String cusacc_no = null;
	public String deposit_account_no = null;

	// out args
	public String col_num_out = null;
	public BigDecimal errorCode = null;
	public String errorDesc = null;

	// inner classes

	public void setRequest(Map request) {
		coll_owner_register_no = (String) request.get("coll_owner_register_no");
		aps_number = (String) request.get("aps_number");
		coll_category = (String) request.get("coll_category");
		coll_type = (String) request.get("coll_type");
		register_no = (String) request.get("register_no");
		amount = (BigDecimal) request.get("amount");
		cur_code_num = (String) request.get("cur_code_num");
		cusacc_no = (String) request.get("cusacc_no");
		deposit_account_no = (String) request.get("deposit_account_no");
	}

	public Map getResponse() {
		response.put("col_num_out", col_num_out);
		response.put("errorCode", errorCode);
		response.put("errorDesc", errorDesc);

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