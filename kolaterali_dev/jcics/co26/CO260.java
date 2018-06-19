package hr.vestigo.modules.collateral.jcics.co26;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO260 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co26/CO260.java,v 1.1 2006/09/04 11:23:47 hrazst Exp $";

	public CO260() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO26 decl = new DeclCO26();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO261 co261 = new CO261(decl);
		co261.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new CO260().run(ca);
	}

}