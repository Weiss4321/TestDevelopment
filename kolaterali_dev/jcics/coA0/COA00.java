package hr.vestigo.modules.collateral.jcics.coA0;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA00 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA0/COA00.java,v 1.3 2014/10/07 10:54:19 hrazst Exp $";

	public COA00() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA0 decl = new DeclCOA0();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("COPonderSelectMap")) {
			COA02 coa02 = new COA02(decl);
			coa02.execute(tc);
		} else if(tc.getMapping().equals("COPonderListMap")) {
			COA03 coa03 = new COA03(decl);
			coa03.execute(tc);
		} else if(tc.getMapping().equals("COPonderUpdateMap")) {
			COA04 coa04 = new COA04(decl);
			coa04.execute(tc);
		} else if(tc.getMapping().equals("WCAListMap")) {
			COA05 coa05 = new COA05(decl);
			coa05.execute(tc);
		} else { 
			// basic mapping
			COA01 coa01 = new COA01(decl);
			coa01.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COA00().run(ca);
	}

}