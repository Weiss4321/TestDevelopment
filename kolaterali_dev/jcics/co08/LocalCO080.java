package hr.vestigo.modules.collateral.jcics.co08;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO080 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co08/LocalCO080.java,v 1.3 2006/02/03 14:01:43 hrasia Exp $";

	public LocalCO080() {
	}

	public void executeProgram() throws Exception {
		DeclCO08 decl = new DeclCO08();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("LoanAccountDialogMapping")) {
			CO082 co082 = new CO082(decl);
			co082.execute(tc);
		} else { 
			// basic mapping
			CO081 co081 = new CO081(decl);
			co081.execute(tc);
		}
	}

}