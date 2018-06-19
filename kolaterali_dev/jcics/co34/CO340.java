package hr.vestigo.modules.collateral.jcics.co34;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO340 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co34/CO340.java,v 1.4 2014/06/17 12:23:38 hrazst Exp $";

	public CO340() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO34 decl = new DeclCO34();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("KolMortgageSel")) {
			CO342 co342 = new CO342(decl);
			co342.execute(tc);
		} else if(tc.getMapping().equals("KolMortgageDeact")) {
			CO343 co343 = new CO343(decl);
			co343.execute(tc);
		} else if(tc.getMapping().equals("KolateralDeact")) {
			CO344 co344 = new CO344(decl);
			co344.execute(tc);
		} else if(tc.getMapping().equals("MortageHistDetailsMapping")) {
			CO345 co345 = new CO345(decl);
			co345.execute(tc);
		} else if(tc.getMapping().equals("MortgageDeactPrintDemo")) {
			CO346 co346 = new CO346(decl);
			co346.execute(tc);
		} else if(tc.getMapping().equals("KolateralBeforeDeact")) {
			CO347 co347 = new CO347(decl);
			co347.execute(tc);
		} else if(tc.getMapping().equals("KolMortageDeactCount")) {
			CO348 co348 = new CO348(decl);
			co348.execute(tc);
		} else { 
			// basic mapping
			CO341 co341 = new CO341(decl);
			co341.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO340().run(ca);
	}

}