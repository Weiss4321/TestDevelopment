package hr.vestigo.modules.collateral.jcics.co26;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO260 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co26/LocalCO260.java,v 1.1 2006/09/04 11:23:47 hrazst Exp $";

	public LocalCO260() {
	}

	public void executeProgram() throws Exception {
		DeclCO26 decl = new DeclCO26();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO261 co261 = new CO261(decl);
		co261.execute(tc);
	}

}