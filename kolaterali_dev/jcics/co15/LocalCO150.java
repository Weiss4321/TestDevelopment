package hr.vestigo.modules.collateral.jcics.co15;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO150 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co15/LocalCO150.java,v 1.8 2006/08/16 14:18:52 hrasia Exp $";

	public LocalCO150() {
	}

	public void executeProgram() throws Exception {
		DeclCO15 decl = new DeclCO15();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollBoeInsertMapping")) {
			CO152 co152 = new CO152(decl);
			co152.execute(tc);
		} else if(tc.getMapping().equals("CollBoeDetailMapping")) {
			CO153 co153 = new CO153(decl);
			co153.execute(tc);
		} else if(tc.getMapping().equals("CollBoeUpdateMapping")) {
			CO154 co154 = new CO154(decl);
			co154.execute(tc);
		} else if(tc.getMapping().equals("CollBoeQbeMapp")) {
			CO155 co155 = new CO155(decl);
			co155.execute(tc);
		} else { 
			// basic mapping
			CO151 co151 = new CO151(decl);
			co151.execute(tc);
		}
	}

}