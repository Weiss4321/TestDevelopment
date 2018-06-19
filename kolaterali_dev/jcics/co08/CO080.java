package hr.vestigo.modules.collateral.jcics.co08;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO080 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co08/CO080.java,v 1.3 2006/02/03 14:01:33 hrasia Exp $";

	public CO080() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO08 decl = new DeclCO08();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("LoanAccountDialogMapping")) {
			CO082 co082 = new CO082(decl);
			co082.execute(tc);
		} else { 
			// basic mapping
			CO081 co081 = new CO081(decl);
			co081.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO080().run(ca);
	}

}