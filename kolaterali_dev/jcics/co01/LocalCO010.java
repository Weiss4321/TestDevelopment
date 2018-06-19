package hr.vestigo.modules.collateral.jcics.co01;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO010 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co01/LocalCO010.java,v 1.2 2005/12/30 11:27:13 hrasia Exp $";

	public LocalCO010() {
	}

	public void executeProgram() throws Exception {
		DeclCO01 decl = new DeclCO01();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO011 co011 = new CO011(decl);
		co011.execute(tc);
	}

}