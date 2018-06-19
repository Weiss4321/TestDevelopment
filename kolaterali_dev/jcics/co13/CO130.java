package hr.vestigo.modules.collateral.jcics.co13;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO130 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co13/CO130.java,v 1.9 2010/07/16 13:37:06 hramlo Exp $";

	public CO130() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO13 decl = new DeclCO13();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollOwnersInsertMapping")) {
			CO132 co132 = new CO132(decl);
			co132.execute(tc);
		} else if(tc.getMapping().equals("CollOwnersDeleteMapping")) {
			CO133 co133 = new CO133(decl);
			co133.execute(tc);
		} else if(tc.getMapping().equals("CollOwnersUpdateMapping")) {
			CO134 co134 = new CO134(decl);
			co134.execute(tc);
		} else { 
			// basic mapping
			CO131 co131 = new CO131(decl);
			co131.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO130().run(ca);
	}

}