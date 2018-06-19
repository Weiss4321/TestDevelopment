package hr.vestigo.modules.collateral.jcics.co05;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO050 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co05/CO050.java,v 1.21 2017/06/01 12:03:46 hrazst Exp $";

	public CO050() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO05 decl = new DeclCO05();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollMovableInsertMapping")) {
			CO052 co052 = new CO052(decl);
			co052.execute(tc);
		} else if(tc.getMapping().equals("CollArtInsertMapping")) {
			CO053 co053 = new CO053(decl);
			co053.execute(tc);
		} else if(tc.getMapping().equals("CollPrecInsertMapping")) {
			CO054 co054 = new CO054(decl);
			co054.execute(tc);
		} else if(tc.getMapping().equals("VesselInsertMapping")) {
			CO055 co055 = new CO055(decl);
			co055.execute(tc);
		} else if(tc.getMapping().equals("CashDepInsertMapping")) {
			CO056 co056 = new CO056(decl);
			co056.execute(tc);
		} else if(tc.getMapping().equals("CollInsPolInsertMapping")) {
			CO057 co057 = new CO057(decl);
			co057.execute(tc);
		} else if(tc.getMapping().equals("CollGuarantInsertMapping")) {
			CO058 co058 = new CO058(decl);
			co058.execute(tc);
		} else if(tc.getMapping().equals("CollLoanStockInsertMap")) {
			CO059 co059 = new CO059(decl);
			co059.execute(tc);
		} else if(tc.getMapping().equals("VehInsertMapping")) {
			CO05A co05a = new CO05A(decl);
			co05a.execute(tc);
		} else if(tc.getMapping().equals("CollCesijaInsertMapping")) {
			CO05B co05b = new CO05B(decl);
			co05b.execute(tc);
		} else { 
			// basic mapping
			CO051 co051 = new CO051(decl);
			co051.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO050().run(ca);
	}

}