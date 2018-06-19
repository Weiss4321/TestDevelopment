package hr.vestigo.modules.collateral.jcics.co29;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO290 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co29/LocalCO290.java,v 1.2 2006/10/07 15:34:21 hramkr Exp $";

	public LocalCO290() {
	}

	public void executeProgram() throws Exception {
		DeclCO29 decl = new DeclCO29();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO291 co291 = new CO291(decl);
		co291.execute(tc);
	}

}