package hr.vestigo.modules.collateral.jcics.coB1;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COB10 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB1/COB10.java,v 1.2 2017/11/29 09:46:19 hrazst Exp $";

	public COB10() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOB1 decl = new DeclCOB1();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollEstimatorQBEMapping")) {
			COB12 cob12 = new COB12(decl);
			cob12.execute(tc);
		} else if(tc.getMapping().equals("CollEstimatorCloseMapping")) {
			COB13 cob13 = new COB13(decl);
			cob13.execute(tc);
		} else { 
			// basic mapping
			COB11 cob11 = new COB11(decl);
			cob11.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COB10().run(ca);
	}

}