package hr.vestigo.modules.collateral.jcics.co15;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO150 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co15/CO150.java,v 1.12 2015/01/20 12:32:28 hrazst Exp $";

	public CO150() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO15 decl = new DeclCO15();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollBoeInsertMapping")) {
			CO152 co152 = new CO152(decl);
			co152.execute(tc);
		} else if(tc.getMapping().equals("CollBoeDetailMapping")) {
			CO153 co153 = new CO153(decl);
			co153.execute(tc);
		} else if(tc.getMapping().equals("CollBoeUpdateMapping")) {
			CO154 co154 = new CO154(decl);
			co154.execute(tc);
		} else if(tc.getMapping().equals("CollBoeQbeMapp")) {
			CO155 co155 = new CO155(decl);
			co155.execute(tc);
		} else { 
			// basic mapping
			CO151 co151 = new CO151(decl);
			co151.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO150().run(ca);
	}

}