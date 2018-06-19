package hr.vestigo.modules.collateral.jcics.coA5;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA50 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA5/COA50.java,v 1.1 2010/08/16 12:11:19 hrakis Exp $";

	public COA50() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA5 decl = new DeclCOA5();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		COA51 coa51 = new COA51(decl);
		coa51.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new COA50().run(ca);
	}

}