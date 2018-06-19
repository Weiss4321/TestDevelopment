package hr.vestigo.modules.collateral.jcics.coA6;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA60 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA6/COA60.java,v 1.3 2012/08/29 06:30:21 hraaks Exp $";

	public COA60() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA6 decl = new DeclCOA6();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollCashDepExceptionInsertM")) {
			COA62 coa62 = new COA62(decl);
			coa62.execute(tc);
		} else if(tc.getMapping().equals("CollCashDepExceptionUpdateM")) {
			COA63 coa63 = new COA63(decl);
			coa63.execute(tc);
		} else if(tc.getMapping().equals("CollCashDepExceptionDetailM")) {
			COA64 coa64 = new COA64(decl);
			coa64.execute(tc);
		} else if(tc.getMapping().equals("CollCashDepExceptionQueryM")) {
			COA65 coa65 = new COA65(decl);
			coa65.execute(tc);
		} else if(tc.getMapping().equals("CollCashDepExceptionOwnerM")) {
			COA66 coa66 = new COA66(decl);
			coa66.execute(tc);
		} else { 
			// basic mapping
			COA61 coa61 = new COA61(decl);
			coa61.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COA60().run(ca);
	}

}