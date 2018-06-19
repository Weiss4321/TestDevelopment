package hr.vestigo.modules.collateral.jcics.co24;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO240 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co24/LocalCO240.java,v 1.4 2006/07/12 09:02:23 hraaks Exp $";

	public LocalCO240() {
	}

	public void executeProgram() throws Exception {
		DeclCO24 decl = new DeclCO24();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollCourtQuerryM")) {
			CO242 co242 = new CO242(decl);
			co242.execute(tc);
		} else if(tc.getMapping().equals("CollCourtInsertM")) {
			CO243 co243 = new CO243(decl);
			co243.execute(tc);
		} else if(tc.getMapping().equals("CollCourtUpdateM")) {
			CO244 co244 = new CO244(decl);
			co244.execute(tc);
		} else if(tc.getMapping().equals("CollCourtDeleteM")) {
			CO245 co245 = new CO245(decl);
			co245.execute(tc);
		} else { 
			// basic mapping
			CO241 co241 = new CO241(decl);
			co241.execute(tc);
		}
	}

}