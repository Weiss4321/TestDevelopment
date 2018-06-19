package hr.vestigo.modules.collateral.jcics.co14;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO140 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co14/LocalCO140.java,v 1.3 2006/05/16 12:32:05 hrazst Exp $";

	public LocalCO140() {
	}

	public void executeProgram() throws Exception {
		DeclCO14 decl = new DeclCO14();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("RevRegCoefReDialogInsertM")) {
			CO142 co142 = new CO142(decl);
			co142.execute(tc);
		} else if(tc.getMapping().equals("RevRegCoefReDialogUpdateM")) {
			CO143 co143 = new CO143(decl);
			co143.execute(tc);
		} else if(tc.getMapping().equals("RevRegCoefReDialogDeleteM")) {
			CO144 co144 = new CO144(decl);
			co144.execute(tc);
		} else if(tc.getMapping().equals("RevRegCoefReQuerryM")) {
			CO145 co145 = new CO145(decl);
			co145.execute(tc);
		} else { 
			// basic mapping
			CO141 co141 = new CO141(decl);
			co141.execute(tc);
		}
	}

}