package hr.vestigo.modules.collateral.jcics.co29;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO290 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co29/CO290.java,v 1.2 2006/10/07 15:34:21 hramkr Exp $";

	public CO290() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO29 decl = new DeclCO29();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO291 co291 = new CO291(decl);
		co291.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new CO290().run(ca);
	}

}