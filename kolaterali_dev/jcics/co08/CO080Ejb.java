package hr.vestigo.modules.collateral.jcics.co08;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO080Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co08/CO080Ejb.java,v 1.3 2006/02/03 14:01:38 hrasia Exp $";

	public CO080Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO08 decl = new DeclCO08();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("LoanAccountListMapping".equals(mapping)) {
			CO081 co081 = new CO081(decl);
			co081.execute(tc);
		} else if("LoanAccountDialogMapping".equals(mapping)) {
			CO082 co082 = new CO082(decl);
			co082.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
