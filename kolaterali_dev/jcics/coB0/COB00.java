package hr.vestigo.modules.collateral.jcics.coB0;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COB00 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB0/COB00.java,v 1.2 2017/11/29 09:46:18 hrazst Exp $";

	public COB00() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOB0 decl = new DeclCOB0();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollEstimatorDialogDetailsMapping")) {
			COB02 cob02 = new COB02(decl);
			cob02.execute(tc);
		} else { 
			// basic mapping
			COB01 cob01 = new COB01(decl);
			cob01.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COB00().run(ca);
	}

}