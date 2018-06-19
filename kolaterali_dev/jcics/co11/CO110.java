package hr.vestigo.modules.collateral.jcics.co11;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO110 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co11/CO110.java,v 1.14 2017/06/01 12:03:41 hrazst Exp $";

	public CO110() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO11 decl = new DeclCO11();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollMovableUpdateMapping")) {
			CO112 co112 = new CO112(decl);
			co112.execute(tc);
		} else if(tc.getMapping().equals("CollArtUpdateMapping")) {
			CO113 co113 = new CO113(decl);
			co113.execute(tc);
		} else if(tc.getMapping().equals("CollPrecUpdateMapping")) {
			CO114 co114 = new CO114(decl);
			co114.execute(tc);
		} else if(tc.getMapping().equals("VesselUpdateMapping")) {
			CO115 co115 = new CO115(decl);
			co115.execute(tc);
		} else if(tc.getMapping().equals("CashDepUpdateMapping")) {
			CO116 co116 = new CO116(decl);
			co116.execute(tc);
		} else if(tc.getMapping().equals("CollInsPolUpdateMapping")) {
			CO117 co117 = new CO117(decl);
			co117.execute(tc);
		} else if(tc.getMapping().equals("CollGuarantUpdateMapping")) {
			CO118 co118 = new CO118(decl);
			co118.execute(tc);
		} else if(tc.getMapping().equals("CollLoanStockUpdateMap")) {
			CO119 co119 = new CO119(decl);
			co119.execute(tc);
		} else if(tc.getMapping().equals("VehUpdateMapping")) {
			CO11A co11a = new CO11A(decl);
			co11a.execute(tc);
		} else if(tc.getMapping().equals("CollCesijaUpdateMapping")) {
			CO11B co11b = new CO11B(decl);
			co11b.execute(tc);
		} else { 
			// basic mapping
			CO111 co111 = new CO111(decl);
			co111.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO110().run(ca);
	}

}