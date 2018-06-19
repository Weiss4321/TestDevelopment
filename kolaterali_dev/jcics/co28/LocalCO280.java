package hr.vestigo.modules.collateral.jcics.co28;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO280 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co28/LocalCO280.java,v 1.2 2006/10/07 15:32:57 hramkr Exp $";

	public LocalCO280() {
	}

	public void executeProgram() throws Exception {
		DeclCO28 decl = new DeclCO28();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CusaccAccountROBILookUpMap")) {
			CO282 co282 = new CO282(decl);
			co282.execute(tc);
		} else { 
			// basic mapping
			CO281 co281 = new CO281(decl);
			co281.execute(tc);
		}
	}

}