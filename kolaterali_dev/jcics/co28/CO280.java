package hr.vestigo.modules.collateral.jcics.co28;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO280 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co28/CO280.java,v 1.3 2011/05/20 07:22:34 hramkr Exp $";

	public CO280() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO28 decl = new DeclCO28();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CusaccAccountROBILookUpMap")) {
			CO282 co282 = new CO282(decl);
			co282.execute(tc);
		} else if(tc.getMapping().equals("KolCusaccExpSelectM")) {
			CO283 co283 = new CO283(decl);
			co283.execute(tc);
		} else { 
			// basic mapping
			CO281 co281 = new CO281(decl);
			co281.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO280().run(ca);
	}

}