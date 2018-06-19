package hr.vestigo.modules.collateral.jcics.co05;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO050 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co05/LocalCO050.java,v 1.9 2006/11/26 14:37:05 hramkr Exp $";

	public LocalCO050() {
	}

	public void executeProgram() throws Exception {
		DeclCO05 decl = new DeclCO05();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollMovableInsertMapping")) {
			CO052 co052 = new CO052(decl);
			co052.execute(tc);
		} else if(tc.getMapping().equals("CollArtInsertMapping")) {
			CO053 co053 = new CO053(decl);
			co053.execute(tc);
		} else if(tc.getMapping().equals("CollPrecInsertMapping")) {
			CO054 co054 = new CO054(decl);
			co054.execute(tc);
		} else if(tc.getMapping().equals("VesselInsertMapping")) {
			CO055 co055 = new CO055(decl);
			co055.execute(tc);
		} else if(tc.getMapping().equals("CashDepInsertMapping")) {
			CO056 co056 = new CO056(decl);
			co056.execute(tc);
		} else if(tc.getMapping().equals("CollInsPolInsertMapping")) {
			CO057 co057 = new CO057(decl);
			co057.execute(tc);
		} else if(tc.getMapping().equals("CollGuarantInsertMapping")) {
			CO058 co058 = new CO058(decl);
			co058.execute(tc);
		} else if(tc.getMapping().equals("CollLoanStockInsertMap")) {
			CO059 co059 = new CO059(decl);
			co059.execute(tc);
		} else if(tc.getMapping().equals("VehInsertMapping")) {
			CO05A co05a = new CO05A(decl);
			co05a.execute(tc);
		} else { 
			// basic mapping
			CO051 co051 = new CO051(decl);
			co051.execute(tc);
		}
	}

}