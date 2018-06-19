package hr.vestigo.modules.collateral.jcics.co36;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO360Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co36/CO360Ejb.java,v 1.2 2007/06/12 14:17:29 hratar Exp $";

	public CO360Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO36 decl = new DeclCO36();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollAmortWorkupsMapping".equals(mapping)) {
			CO361 co361 = new CO361(decl);
			co361.execute(tc);
		} else if("CollAmortWorkupsDetailsMapping".equals(mapping)) {
			CO362 co362 = new CO362(decl);
			co362.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
