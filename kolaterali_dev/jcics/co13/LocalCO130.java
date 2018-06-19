package hr.vestigo.modules.collateral.jcics.co13;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO130 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co13/LocalCO130.java,v 1.6 2006/05/08 14:06:36 hrarmv Exp $";

	public LocalCO130() {
	}

	public void executeProgram() throws Exception {
		DeclCO13 decl = new DeclCO13();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollOwnersInsertMapping")) {
			CO132 co132 = new CO132(decl);
			co132.execute(tc);
		} else if(tc.getMapping().equals("CollOwnersDeleteMapping")) {
			CO133 co133 = new CO133(decl);
			co133.execute(tc);
		} else if(tc.getMapping().equals("CollOwnersUpdateMapping")) {
			CO134 co134 = new CO134(decl);
			co134.execute(tc);
		} else { 
			// basic mapping
			CO131 co131 = new CO131(decl);
			co131.execute(tc);
		}
	}

}