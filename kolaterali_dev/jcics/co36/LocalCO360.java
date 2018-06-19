package hr.vestigo.modules.collateral.jcics.co36;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO360 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co36/LocalCO360.java,v 1.2 2007/06/12 14:17:29 hratar Exp $";

	public LocalCO360() {
	}

	public void executeProgram() throws Exception {
		DeclCO36 decl = new DeclCO36();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollAmortWorkupsDetailsMapping")) {
			CO362 co362 = new CO362(decl);
			co362.execute(tc);
		} else { 
			// basic mapping
			CO361 co361 = new CO361(decl);
			co361.execute(tc);
		}
	}

}