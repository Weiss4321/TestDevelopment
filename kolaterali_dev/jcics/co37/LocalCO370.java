package hr.vestigo.modules.collateral.jcics.co37;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO370 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co37/LocalCO370.java,v 1.1 2007/09/06 14:50:24 hraamh Exp $";

	public LocalCO370() {
	}

	public void executeProgram() throws Exception {
		DeclCO37 decl = new DeclCO37();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO371 co371 = new CO371(decl);
		co371.execute(tc);
	}

}