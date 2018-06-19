package hr.vestigo.modules.collateral.jcics.co23;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO230 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co23/LocalCO230.java,v 1.1 2006/06/19 14:37:50 hramkr Exp $";

	public LocalCO230() {
	}

	public void executeProgram() throws Exception {
		DeclCO23 decl = new DeclCO23();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO231 co231 = new CO231(decl);
		co231.execute(tc);
	}

}