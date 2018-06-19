package hr.vestigo.modules.collateral.jcics.co01;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO010 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co01/CO010.java,v 1.2 2005/12/30 11:26:59 hrasia Exp $";

	public CO010() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO01 decl = new DeclCO01();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO011 co011 = new CO011(decl);
		co011.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new CO010().run(ca);
	}

}