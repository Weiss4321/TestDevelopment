package hr.vestigo.modules.collateral.jcics.coA3;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA30 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA3/COA30.java,v 1.1 2009/09/25 11:33:00 hrakis Exp $";

	public COA30() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
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

	public static void main(CommAreaHolder ca) {
		new COA30().run(ca);
	}

}