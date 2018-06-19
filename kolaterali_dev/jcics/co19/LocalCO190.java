package hr.vestigo.modules.collateral.jcics.co19;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO190 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co19/LocalCO190.java,v 1.3 2006/06/07 06:38:40 hrajkl Exp $";

	public LocalCO190() {
	}

	public void executeProgram() throws Exception {
		DeclCO19 decl = new DeclCO19();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("InsuPolicyTypeInsertM")) {
			CO192 co192 = new CO192(decl);
			co192.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyTypeDeleteM")) {
			CO193 co193 = new CO193(decl);
			co193.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyTypeUpdateM")) {
			CO194 co194 = new CO194(decl);
			co194.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyTypeQuerryM")) {
			CO195 co195 = new CO195(decl);
			co195.execute(tc);
		} else { 
			// basic mapping
			CO191 co191 = new CO191(decl);
			co191.execute(tc);
		}
	}

}