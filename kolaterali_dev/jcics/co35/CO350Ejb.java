package hr.vestigo.modules.collateral.jcics.co35;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO350Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co35/CO350Ejb.java,v 1.3 2007/06/21 12:34:05 hratar Exp $";

	public CO350Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO35 decl = new DeclCO35();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollRevalWorkupsMapping".equals(mapping)) {
			CO351 co351 = new CO351(decl);
			co351.execute(tc);
		} else if("CollRevalWorkupsDetailsMapping".equals(mapping)) {
			CO352 co352 = new CO352(decl);
			co352.execute(tc);
		} else if("CollWorkupsMapping".equals(mapping)) {
			CO353 co353 = new CO353(decl);
			co353.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
