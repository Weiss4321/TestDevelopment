package hr.vestigo.modules.collateral.jcics.co30;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO300 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co30/LocalCO300.java,v 1.1 2006/10/06 07:11:18 hrasia Exp $";

	public LocalCO300() {
	}

	public void executeProgram() throws Exception {
		DeclCO30 decl = new DeclCO30();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO301 co301 = new CO301(decl);
		co301.execute(tc);
	}

}