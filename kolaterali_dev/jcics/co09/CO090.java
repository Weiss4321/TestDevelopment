package hr.vestigo.modules.collateral.jcics.co09;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO090 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co09/CO090.java,v 1.7 2006/02/05 07:41:06 hrasia Exp $";

	public CO090() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO09 decl = new DeclCO09();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollHfPriorDialogRbaMapping")) {
			CO092 co092 = new CO092(decl);
			co092.execute(tc);
		} else { 
			// basic mapping
			CO091 co091 = new CO091(decl);
			co091.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO090().run(ca);
	}

}