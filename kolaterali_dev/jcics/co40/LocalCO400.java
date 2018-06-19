package hr.vestigo.modules.collateral.jcics.co40;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO400 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co40/LocalCO400.java,v 1.4 2009/11/11 08:55:58 hramlo Exp $";

	public LocalCO400() {
	}

	public void executeProgram() throws Exception {
		DeclCO40 decl = new DeclCO40();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("VehDeactOwnerMapping")) {
			CO401 co401 = new CO401(decl);
			co401.execute(tc);
		} else if(tc.getMapping().equals("MovDeactOwnerMapping")) {
			CO403 co403 = new CO403(decl);
			co403.execute(tc);
		} else if(tc.getMapping().equals("VesDeactOwnerMapping")) {
			CO404 co404 = new CO404(decl);
			co404.execute(tc);
		} else if(tc.getMapping().equals("VrpDeactOwnerMapping")) {
			CO405 co405 = new CO405(decl);
			co405.execute(tc);
		} else if(tc.getMapping().equals("InsPolDeactOwnerMapping")) {
			CO406 co406 = new CO406(decl);
			co406.execute(tc);
		} else if(tc.getMapping().equals("CashDepDeactOwnerMapping")) {
			CO407 co407 = new CO407(decl);
			co407.execute(tc);
		} else { 
			// basic mapping
			CO402 co402 = new CO402(decl);
			co402.execute(tc);
		}
	}

}