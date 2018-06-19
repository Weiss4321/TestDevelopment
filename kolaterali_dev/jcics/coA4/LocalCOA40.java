package hr.vestigo.modules.collateral.jcics.coA4;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCOA40 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA4/LocalCOA40.java,v 1.1 2009/11/04 13:09:01 hrakis Exp $";

	public LocalCOA40() {
	}

	public void executeProgram() throws Exception {
		DeclCOA4 decl = new DeclCOA4();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		COA41 coa41 = new COA41(decl);
		coa41.execute(tc);
	}

}