package hr.vestigo.modules.collateral.jcics.co38;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO380 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co38/LocalCO380.java,v 1.1 2007/10/12 08:35:15 hratar Exp $";

	public LocalCO380() {
	}

	public void executeProgram() throws Exception {
		DeclCO38 decl = new DeclCO38();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO381 co381 = new CO381(decl);
		co381.execute(tc);
	}

}