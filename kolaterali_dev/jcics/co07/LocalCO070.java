package hr.vestigo.modules.collateral.jcics.co07;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO070 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co07/LocalCO070.java,v 1.2 2006/02/21 07:45:20 hrasia Exp $";

	public LocalCO070() {
	}

	public void executeProgram() throws Exception {
		DeclCO07 decl = new DeclCO07();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollateralDialogInsertMapping")) {
			CO072 co072 = new CO072(decl);
			co072.execute(tc);
		} else { 
			// basic mapping
			CO071 co071 = new CO071(decl);
			co071.execute(tc);
		}
	}

}