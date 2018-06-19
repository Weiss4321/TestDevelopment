package hr.vestigo.modules.collateral.jcics.co12;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO120Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co12/CO120Ejb.java,v 1.6 2006/04/13 09:49:02 hrarmv Exp $";

	public CO120Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO12 decl = new DeclCO12();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("PolMapDistrictMapping".equals(mapping)) {
			CO121 co121 = new CO121(decl);
			co121.execute(tc);
		} else if("PolMapDistrictInsertMapping".equals(mapping)) {
			CO122 co122 = new CO122(decl);
			co122.execute(tc);
		} else if("PolMapDistrictDeleteMapping".equals(mapping)) {
			CO123 co123 = new CO123(decl);
			co123.execute(tc);
		} else if("PolMapDistrictUpdateMapping".equals(mapping)) {
			CO124 co124 = new CO124(decl);
			co124.execute(tc);
		} else if("PolMapResiQuarListMapping".equals(mapping)) {
			CO125 co125 = new CO125(decl);
			co125.execute(tc);
		} else if("PolMapSelectTypeMapping".equals(mapping)) {
			CO126 co126 = new CO126(decl);
			co126.execute(tc);
		} else if("PolMapResiQuarInsertMapping".equals(mapping)) {
			CO127 co127 = new CO127(decl);
			co127.execute(tc);
		} else if("PolMapResiQuarDeleteMapping".equals(mapping)) {
			CO128 co128 = new CO128(decl);
			co128.execute(tc);
		} else if("PolMapResiQuarUpdateMapping".equals(mapping)) {
			CO129 co129 = new CO129(decl);
			co129.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
