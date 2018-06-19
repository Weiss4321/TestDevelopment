package hr.vestigo.modules.collateral.jcics.co19;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO190 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co19/CO190.java,v 1.4 2015/01/13 08:27:08 hrajkl Exp $";

	public CO190() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO19 decl = new DeclCO19();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("InsuPolicyTypeInsertM")) {
			CO192 co192 = new CO192(decl);
			co192.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyTypeDeleteM")) {
			CO193 co193 = new CO193(decl);
			co193.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyTypeUpdateM")) {
			CO194 co194 = new CO194(decl);
			co194.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyTypeQuerryM")) {
			CO195 co195 = new CO195(decl);
			co195.execute(tc);
		} else { 
			// basic mapping
			CO191 co191 = new CO191(decl);
			co191.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO190().run(ca);
	}

}