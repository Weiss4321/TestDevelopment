package hr.vestigo.modules.collateral.jcics.co41;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO410 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co41/CO410.java,v 1.3 2014/05/05 09:00:35 hraaks Exp $";

	public CO410() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO41 decl = new DeclCO41();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollInvestPartiesAllQuerryDate")) {
			CO412 co412 = new CO412(decl);
			co412.execute(tc);
		} else if(tc.getMapping().equals("CollCesReportQueryDate")) {
			CO413 co413 = new CO413(decl);
			co413.execute(tc);
		} else { 
			// basic mapping
			CO411 co411 = new CO411(decl);
			co411.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO410().run(ca);
	}

}