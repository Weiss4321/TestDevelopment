package hr.vestigo.modules.collateral.jcics.co10;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO100 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co10/CO100.java,v 1.3 2006/02/27 10:26:13 hrasia Exp $";

	public CO100() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO10 decl = new DeclCO10();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollComponentItemCheckMapping")) {
			CO102 co102 = new CO102(decl);
			co102.execute(tc);
		} else if(tc.getMapping().equals("LoanComponentItemGetFullBalanceMapping")) {
			CO103 co103 = new CO103(decl);
			co103.execute(tc);
		} else { 
			// basic mapping
			CO101 co101 = new CO101(decl);
			co101.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO100().run(ca);
	}

}