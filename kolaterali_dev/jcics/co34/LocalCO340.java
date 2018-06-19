package hr.vestigo.modules.collateral.jcics.co34;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO340 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co34/LocalCO340.java,v 1.2 2007/11/08 09:32:18 hramkr Exp $";

	public LocalCO340() {
	}

	public void executeProgram() throws Exception {
		DeclCO34 decl = new DeclCO34();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("KolMortgageSel")) {
			CO342 co342 = new CO342(decl);
			co342.execute(tc);
		} else if(tc.getMapping().equals("KolMortgageDeact")) {
			CO343 co343 = new CO343(decl);
			co343.execute(tc);
		} else if(tc.getMapping().equals("KolateralDeact")) {
			CO344 co344 = new CO344(decl);
			co344.execute(tc);
		} else { 
			// basic mapping
			CO341 co341 = new CO341(decl);
			co341.execute(tc);
		}
	}

}