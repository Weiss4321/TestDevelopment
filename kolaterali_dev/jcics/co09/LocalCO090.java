package hr.vestigo.modules.collateral.jcics.co09;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO090 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co09/LocalCO090.java,v 1.7 2006/02/05 07:40:49 hrasia Exp $";

	public LocalCO090() {
	}

	public void executeProgram() throws Exception {
		DeclCO09 decl = new DeclCO09();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollHfPriorDialogRbaMapping")) {
			CO092 co092 = new CO092(decl);
			co092.execute(tc);
		} else { 
			// basic mapping
			CO091 co091 = new CO091(decl);
			co091.execute(tc);
		}
	}

}