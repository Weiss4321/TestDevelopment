package hr.vestigo.modules.collateral.jcics.co18;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO180 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co18/LocalCO180.java,v 1.3 2009/03/25 09:32:49 hramkr Exp $";

	public LocalCO180() {
	}

	public void executeProgram() throws Exception {
		DeclCO18 decl = new DeclCO18();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("InsuPolicyDialogInsertM")) {
			CO182 co182 = new CO182(decl);
			co182.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyDialogUpdateM")) {
			CO183 co183 = new CO183(decl);
			co183.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyDialogDeleteM")) {
			CO184 co184 = new CO184(decl);
			co184.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyStatusUpdateM")) {
			CO185 co185 = new CO185(decl);
			co185.execute(tc);
		} else { 
			// basic mapping
			CO181 co181 = new CO181(decl);
			co181.execute(tc);
		}
	}

}