package hr.vestigo.modules.collateral.jcics.co32;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO320 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co32/CO320.java,v 1.3 2017/02/16 15:27:22 hrazst Exp $";

	public CO320() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO32 decl = new DeclCO32();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("AgrListQBE")) {
			CO322 co322 = new CO322(decl);
			co322.execute(tc);
		} else if(tc.getMapping().equals("AgrInsert")) {
			CO323 co323 = new CO323(decl);
			co323.execute(tc);
		} else if(tc.getMapping().equals("AgrUpdate")) {
			CO324 co324 = new CO324(decl);
			co324.execute(tc);
		} else if(tc.getMapping().equals("AgrSelect")) {
			CO325 co325 = new CO325(decl);
			co325.execute(tc);
		} else if(tc.getMapping().equals("AgrSelMap")) {
			CO326 co326 = new CO326(decl);
			co326.execute(tc);
		} else { 
			// basic mapping
			CO321 co321 = new CO321(decl);
			co321.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO320().run(ca);
	}

}