package hr.vestigo.modules.collateral.jcics.co33;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO330 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co33/CO330.java,v 1.2 2007/03/06 14:19:06 hramkr Exp $";

	public CO330() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO33 decl = new DeclCO33();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("AgrListStop")) {
			CO332 co332 = new CO332(decl);
			co332.execute(tc);
		} else if(tc.getMapping().equals("AgrListBack")) {
			CO333 co333 = new CO333(decl);
			co333.execute(tc);
		} else if(tc.getMapping().equals("AgrListVer")) {
			CO334 co334 = new CO334(decl);
			co334.execute(tc);
		} else if(tc.getMapping().equals("AgrListQSelect")) {
			CO335 co335 = new CO335(decl);
			co335.execute(tc);
		} else if(tc.getMapping().equals("AgrListClose")) {
			CO336 co336 = new CO336(decl);
			co336.execute(tc);
		} else { 
			// basic mapping
			CO331 co331 = new CO331(decl);
			co331.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO330().run(ca);
	}

}