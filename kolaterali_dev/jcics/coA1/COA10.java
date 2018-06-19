package hr.vestigo.modules.collateral.jcics.coA1;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA10 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA1/COA10.java,v 1.2 2012/03/30 09:23:18 hramkr Exp $";

	public COA10() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA1 decl = new DeclCOA1();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("InsuCompanySelectMapp")) {
			COA12 coa12 = new COA12(decl);
			coa12.execute(tc);
		} else { 
			// basic mapping
			COA11 coa11 = new COA11(decl);
			coa11.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COA10().run(ca);
	}

}