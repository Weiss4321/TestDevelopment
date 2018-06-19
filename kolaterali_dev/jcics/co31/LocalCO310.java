package hr.vestigo.modules.collateral.jcics.co31;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO310 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co31/LocalCO310.java,v 1.3 2006/11/08 12:37:31 hrazst Exp $";

	public LocalCO310() {
	}

	public void executeProgram() throws Exception {
		DeclCO31 decl = new DeclCO31();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CadastreMapDialogInsertM")) {
			CO312 co312 = new CO312(decl);
			co312.execute(tc);
		} else if(tc.getMapping().equals("CadastreMapDialogUpdateM")) {
			CO313 co313 = new CO313(decl);
			co313.execute(tc);
		} else if(tc.getMapping().equals("CadastreMapDialogDeleteM")) {
			CO314 co314 = new CO314(decl);
			co314.execute(tc);
		} else if(tc.getMapping().equals("CadastreMapQBEM")) {
			CO315 co315 = new CO315(decl);
			co315.execute(tc);
		} else { 
			// basic mapping
			CO311 co311 = new CO311(decl);
			co311.execute(tc);
		}
	}

}