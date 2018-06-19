package hr.vestigo.modules.collateral.jcics.coA2;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA20 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA2/COA20.java,v 1.4 2017/04/14 10:17:31 hraziv Exp $";

	public COA20() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA2 decl = new DeclCOA2();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollEstimatorLookUpMapping")) {
			COA22 coa22 = new COA22(decl);
			coa22.execute(tc);
		} else if(tc.getMapping().equals("EconomicLifeSelectMapping")) {
			COA23 coa23 = new COA23(decl);
			coa23.execute(tc);
		} else if(tc.getMapping().equals("CollGcmTypLookUpMapping")) {
			COA24 coa24 = new COA24(decl);
			coa24.execute(tc);
		} else { 
			// basic mapping
			COA21 coa21 = new COA21(decl);
			coa21.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COA20().run(ca);
	}

}