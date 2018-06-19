package hr.vestigo.modules.collateral.jcics.coA0;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCOA00 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA0/LocalCOA00.java,v 1.1 2008/03/27 13:52:14 hramkr Exp $";

	public LocalCOA00() {
	}

	public void executeProgram() throws Exception {
		DeclCOA0 decl = new DeclCOA0();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("COPonderSelectMap")) {
			COA02 coa02 = new COA02(decl);
			coa02.execute(tc);
		} else if(tc.getMapping().equals("COPonderListMap")) {
			COA03 coa03 = new COA03(decl);
			coa03.execute(tc);
		} else if(tc.getMapping().equals("COPonderUpdateMap")) {
			COA04 coa04 = new COA04(decl);
			coa04.execute(tc);
		} else { 
			// basic mapping
			COA01 coa01 = new COA01(decl);
			coa01.execute(tc);
		}
	}

}