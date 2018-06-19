package hr.vestigo.modules.collateral.jcics.co23;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO230 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co23/CO230.java,v 1.2 2014/12/16 12:34:46 hrakis Exp $";

	public CO230() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO23 decl = new DeclCO23();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollHistorySelectMapping")) {
			CO232 co232 = new CO232(decl);
			co232.execute(tc);
		} else { 
			// basic mapping
			CO231 co231 = new CO231(decl);
			co231.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO230().run(ca);
	}

}