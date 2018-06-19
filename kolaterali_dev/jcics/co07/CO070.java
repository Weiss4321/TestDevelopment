package hr.vestigo.modules.collateral.jcics.co07;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO070 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co07/CO070.java,v 1.2 2006/02/21 07:45:07 hrasia Exp $";

	public CO070() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO07 decl = new DeclCO07();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollateralDialogInsertMapping")) {
			CO072 co072 = new CO072(decl);
			co072.execute(tc);
		} else { 
			// basic mapping
			CO071 co071 = new CO071(decl);
			co071.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO070().run(ca);
	}

}