package hr.vestigo.modules.collateral.jcics.co25;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO250 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co25/CO250.java,v 1.2 2006/08/17 10:38:29 hrasia Exp $";

	public CO250() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO25 decl = new DeclCO25();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("LoanStockQbeMapp")) {
			CO252 co252 = new CO252(decl);
			co252.execute(tc);
		} else if(tc.getMapping().equals("CashDepQbeMapp")) {
			CO253 co253 = new CO253(decl);
			co253.execute(tc);
		} else { 
			// basic mapping
			CO251 co251 = new CO251(decl);
			co251.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO250().run(ca);
	}

}