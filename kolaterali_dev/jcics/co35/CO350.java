package hr.vestigo.modules.collateral.jcics.co35;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO350 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co35/CO350.java,v 1.3 2007/06/21 12:34:05 hratar Exp $";

	public CO350() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO35 decl = new DeclCO35();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollRevalWorkupsDetailsMapping")) {
			CO352 co352 = new CO352(decl);
			co352.execute(tc);
		} else if(tc.getMapping().equals("CollWorkupsMapping")) {
			CO353 co353 = new CO353(decl);
			co353.execute(tc);
		} else { 
			// basic mapping
			CO351 co351 = new CO351(decl);
			co351.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO350().run(ca);
	}

}