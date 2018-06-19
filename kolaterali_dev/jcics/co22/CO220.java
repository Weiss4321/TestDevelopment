package hr.vestigo.modules.collateral.jcics.co22;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO220 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co22/CO220.java,v 1.1 2006/06/10 15:07:40 hramkr Exp $";

	public CO220() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO22 decl = new DeclCO22();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO221 co221 = new CO221(decl);
		co221.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new CO220().run(ca);
	}

}