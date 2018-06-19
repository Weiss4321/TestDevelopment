package hr.vestigo.modules.collateral.jcics.coA4;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA40 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA4/COA40.java,v 1.2 2014/09/18 10:37:41 hrakis Exp $";

	public COA40() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA4 decl = new DeclCOA4();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollRevaDatesMapping")) {
			COA42 coa42 = new COA42(decl);
			coa42.execute(tc);
		} else { 
			// basic mapping
			COA41 coa41 = new COA41(decl);
			coa41.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COA40().run(ca);
	}

}