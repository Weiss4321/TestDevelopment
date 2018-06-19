package hr.vestigo.modules.collateral.jcics.co32;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO320 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co32/LocalCO320.java,v 1.2 2007/03/06 14:16:16 hramkr Exp $";

	public LocalCO320() {
	}

	public void executeProgram() throws Exception {
		DeclCO32 decl = new DeclCO32();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("AgrListQBE")) {
			CO322 co322 = new CO322(decl);
			co322.execute(tc);
		} else if(tc.getMapping().equals("AgrInsert")) {
			CO323 co323 = new CO323(decl);
			co323.execute(tc);
		} else if(tc.getMapping().equals("AgrUpdate")) {
			CO324 co324 = new CO324(decl);
			co324.execute(tc);
		} else if(tc.getMapping().equals("AgrSelect")) {
			CO325 co325 = new CO325(decl);
			co325.execute(tc);
		} else if(tc.getMapping().equals("AgrSelMap")) {
			CO326 co326 = new CO326(decl);
			co326.execute(tc);
		} else { 
			// basic mapping
			CO321 co321 = new CO321(decl);
			co321.execute(tc);
		}
	}

}