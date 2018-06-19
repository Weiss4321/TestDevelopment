package hr.vestigo.modules.collateral.jcics.co22;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO220 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co22/LocalCO220.java,v 1.1 2006/06/10 15:07:40 hramkr Exp $";

	public LocalCO220() {
	}

	public void executeProgram() throws Exception {
		DeclCO22 decl = new DeclCO22();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO221 co221 = new CO221(decl);
		co221.execute(tc);
	}

}