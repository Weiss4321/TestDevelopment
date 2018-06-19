package hr.vestigo.modules.collateral.jcics.co27;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO270 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co27/CO270.java,v 1.5 2007/06/28 07:26:51 hratar Exp $";

	public CO270() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO27 decl = new DeclCO27();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CusaccExpCollQBEMapping")) {
			CO272 co272 = new CO272(decl);
			co272.execute(tc);
		} else if(tc.getMapping().equals("CusaccExpCollQBE2Mapping")) {
			CO273 co273 = new CO273(decl);
			co273.execute(tc);
		} else if(tc.getMapping().equals("CusaccExpCollDetailsMapping")) {
			CO274 co274 = new CO274(decl);
			co274.execute(tc);
		} else if(tc.getMapping().equals("ColWorkListCovMapping")) {
			CO275 co275 = new CO275(decl);
			co275.execute(tc);
		} else { 
			// basic mapping
			CO271 co271 = new CO271(decl);
			co271.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO270().run(ca);
	}

}