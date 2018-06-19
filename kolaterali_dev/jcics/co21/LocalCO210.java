package hr.vestigo.modules.collateral.jcics.co21;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO210 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co21/LocalCO210.java,v 1.7 2008/05/12 14:19:59 hramkr Exp $";

	public LocalCO210() {
	}

	public void executeProgram() throws Exception {
		DeclCO21 decl = new DeclCO21();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollWorkListStop")) {
			CO212 co212 = new CO212(decl);
			co212.execute(tc);
		} else if(tc.getMapping().equals("CollWorkListSndVer")) {
			CO213 co213 = new CO213(decl);
			co213.execute(tc);
		} else if(tc.getMapping().equals("CollVerification")) {
			CO214 co214 = new CO214(decl);
			co214.execute(tc);
		} else if(tc.getMapping().equals("CollAuthorization")) {
			CO215 co215 = new CO215(decl);
			co215.execute(tc);
		} else if(tc.getMapping().equals("CollSndArhiva")) {
			CO216 co216 = new CO216(decl);
			co216.execute(tc);
		} else if(tc.getMapping().equals("CollSndBack")) {
			CO217 co217 = new CO217(decl);
			co217.execute(tc);
		} else if(tc.getMapping().equals("CollSndBackAll")) {
			CO218 co218 = new CO218(decl);
			co218.execute(tc);
		} else if(tc.getMapping().equals("KolateralQBEmap")) {
			CO219 co219 = new CO219(decl);
			co219.execute(tc);
		} else { 
			// basic mapping
			CO211 co211 = new CO211(decl);
			co211.execute(tc);
		}
	}

}