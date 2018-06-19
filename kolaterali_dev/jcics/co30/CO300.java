package hr.vestigo.modules.collateral.jcics.co30;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO300 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co30/CO300.java,v 1.1 2006/10/06 07:11:00 hrasia Exp $";

	public CO300() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO30 decl = new DeclCO30();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO301 co301 = new CO301(decl);
		co301.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new CO300().run(ca);
	}

}