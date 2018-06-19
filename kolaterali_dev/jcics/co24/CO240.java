package hr.vestigo.modules.collateral.jcics.co24;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO240 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co24/CO240.java,v 1.4 2006/07/12 09:02:24 hraaks Exp $";

	public CO240() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
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

	public static void main(CommAreaHolder ca) {
		new CO240().run(ca);
	}

}