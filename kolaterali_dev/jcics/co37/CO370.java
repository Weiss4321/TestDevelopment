package hr.vestigo.modules.collateral.jcics.co37;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO370 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co37/CO370.java,v 1.1 2007/09/06 14:50:25 hraamh Exp $";

	public CO370() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO37 decl = new DeclCO37();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		CO371 co371 = new CO371(decl);
		co371.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new CO370().run(ca);
	}

}