package hr.vestigo.modules.collateral.jcics.coA2;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCOA20 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA2/LocalCOA20.java,v 1.1 2008/04/18 13:55:37 hramkr Exp $";

	public LocalCOA20() {
	}

	public void executeProgram() throws Exception {
		DeclCOA2 decl = new DeclCOA2();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		COA21 coa21 = new COA21(decl);
		coa21.execute(tc);
	}

}