package hr.vestigo.modules.collateral.jcics.co30;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO300Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co30/CO300Ejb.java,v 1.1 2006/10/06 07:11:06 hrasia Exp $";

	public CO300Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO30 decl = new DeclCO30();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollateralPostingMapp".equals(mapping)) {
			CO301 co301 = new CO301(decl);
			co301.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
