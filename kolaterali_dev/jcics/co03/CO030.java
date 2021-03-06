package hr.vestigo.modules.collateral.jcics.co03;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO030 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co03/CO030.java,v 1.13 2014/10/07 10:54:20 hrazst Exp $";

	public CO030() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO03 decl = new DeclCO03();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("ChpDInsMap")) {
			CO032 co032 = new CO032(decl);
			co032.execute(tc);
		} else if(tc.getMapping().equals("ChpDPlaUseMap")) {
			CO033 co033 = new CO033(decl);
			co033.execute(tc);
		} else if(tc.getMapping().equals("ChpDDetMap")) {
			CO034 co034 = new CO034(decl);
			co034.execute(tc);
		} else if(tc.getMapping().equals("ChpDUpdMap")) {
			CO035 co035 = new CO035(decl);
			co035.execute(tc);
		} else if(tc.getMapping().equals("ChpDUserLockM")) {
			CO036 co036 = new CO036(decl);
			co036.execute(tc);
		} else if(tc.getMapping().equals("ChpLoanMap")) {
			CO037 co037 = new CO037(decl);
			co037.execute(tc);
		} else if(tc.getMapping().equals("MrtgDelMap")) {
			CO038 co038 = new CO038(decl);
			co038.execute(tc);
		} else { 
			// basic mapping
			CO031 co031 = new CO031(decl);
			co031.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO030().run(ca);
	}

}