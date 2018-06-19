package hr.vestigo.modules.collateral.jcics.co25;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO250 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co25/LocalCO250.java,v 1.2 2006/08/17 10:38:47 hrasia Exp $";

	public LocalCO250() {
	}

	public void executeProgram() throws Exception {
		DeclCO25 decl = new DeclCO25();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("LoanStockQbeMapp")) {
			CO252 co252 = new CO252(decl);
			co252.execute(tc);
		} else if(tc.getMapping().equals("CashDepQbeMapp")) {
			CO253 co253 = new CO253(decl);
			co253.execute(tc);
		} else { 
			// basic mapping
			CO251 co251 = new CO251(decl);
			co251.execute(tc);
		}
	}

}