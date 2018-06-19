package hr.vestigo.modules.collateral.jcics.co17;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO170 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co17/LocalCO170.java,v 1.8 2006/06/07 06:38:27 hrajkl Exp $";

	public LocalCO170() {
	}

	public void executeProgram() throws Exception {
		DeclCO17 decl = new DeclCO17();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("RealEstateTypeInsertM")) {
			CO172 co172 = new CO172(decl);
			co172.execute(tc);
		} else if(tc.getMapping().equals("RealEstateTypeDeleteM")) {
			CO173 co173 = new CO173(decl);
			co173.execute(tc);
		} else if(tc.getMapping().equals("RealEstateTypeUpdateM")) {
			CO174 co174 = new CO174(decl);
			co174.execute(tc);
		} else if(tc.getMapping().equals("RealEstateTypeQuerryM")) {
			CO175 co175 = new CO175(decl);
			co175.execute(tc);
		} else { 
			// basic mapping
			CO171 co171 = new CO171(decl);
			co171.execute(tc);
		}
	}

}