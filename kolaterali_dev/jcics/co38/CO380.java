package hr.vestigo.modules.collateral.jcics.co38;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO380 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co38/CO380.java,v 1.1 2007/10/12 08:35:15 hratar Exp $";

	public CO380() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO38 decl = new DeclCO38();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO381 co381 = new CO381(decl);
		co381.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new CO380().run(ca);
	}

}