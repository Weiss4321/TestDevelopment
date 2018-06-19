package hr.vestigo.modules.collateral.jcics.co10;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO100Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co10/CO100Ejb.java,v 1.3 2006/02/27 10:26:08 hrasia Exp $";

	public CO100Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO10 decl = new DeclCO10();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("LoanComponentItemCheckMapping".equals(mapping)) {
			CO101 co101 = new CO101(decl);
			co101.execute(tc);
		} else if("CollComponentItemCheckMapping".equals(mapping)) {
			CO102 co102 = new CO102(decl);
			co102.execute(tc);
		} else if("LoanComponentItemGetFullBalanceMapping".equals(mapping)) {
			CO103 co103 = new CO103(decl);
			co103.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
