package hr.vestigo.modules.collateral.jcics.co36;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO360 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co36/CO360.java,v 1.2 2007/06/12 14:15:37 hratar Exp $";

	public CO360() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO36 decl = new DeclCO36();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollAmortWorkupsDetailsMapping")) {
			CO362 co362 = new CO362(decl);
			co362.execute(tc);
		} else { 
			// basic mapping
			CO361 co361 = new CO361(decl);
			co361.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO360().run(ca);
	}

}