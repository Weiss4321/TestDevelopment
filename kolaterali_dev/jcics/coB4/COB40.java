package hr.vestigo.modules.collateral.jcics.coB4;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COB40 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB4/COB40.java,v 1.1 2017/02/23 08:17:15 hraaks Exp $";

	public COB40() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOB4 decl = new DeclCOB4();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		// basic mapping
		COB41 cob41 = new COB41(decl);
		cob41.execute(tc);
	}

	public static void main(CommAreaHolder ca) {
		new COB40().run(ca);
	}

}