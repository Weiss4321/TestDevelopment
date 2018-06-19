package hr.vestigo.modules.collateral.jcics.coA9;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA90 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA9/COA90.java,v 1.3 2012/05/14 07:38:12 hradnp Exp $";

	public COA90() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA9 decl = new DeclCOA9();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CoBorrowerInsertM")) {
			COA92 coa92 = new COA92(decl);
			coa92.execute(tc);
		} else if(tc.getMapping().equals("CoBorrowerUpdateM")) {
			COA93 coa93 = new COA93(decl);
			coa93.execute(tc);
		} else { 
			// basic mapping
			COA91 coa91 = new COA91(decl);
			coa91.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COA90().run(ca);
	}

}