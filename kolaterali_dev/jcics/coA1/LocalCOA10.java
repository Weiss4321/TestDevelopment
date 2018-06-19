package hr.vestigo.modules.collateral.jcics.coA1;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCOA10 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA1/LocalCOA10.java,v 1.1 2008/03/28 09:42:30 hramkr Exp $";

	public LocalCOA10() {
	}

	public void executeProgram() throws Exception {
		DeclCOA1 decl = new DeclCOA1();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		COA11 coa11 = new COA11(decl);
		coa11.execute(tc);
	}

}