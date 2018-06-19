package hr.vestigo.modules.collateral.jcics.coA3;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCOA30 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA3/LocalCOA30.java,v 1.1 2009/09/25 11:33:00 hrakis Exp $";

	public LocalCOA30() {
	}

	public void executeProgram() throws Exception {
		DeclCOA3 decl = new DeclCOA3();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollPonderInsertM")) {
			COA32 coa32 = new COA32(decl);
			coa32.execute(tc);
		} else if(tc.getMapping().equals("CollPonderQueryM")) {
			COA33 coa33 = new COA33(decl);
			coa33.execute(tc);
		} else { 
			// basic mapping
			COA31 coa31 = new COA31(decl);
			coa31.execute(tc);
		}
	}

}