package hr.vestigo.modules.collateral.jcics.co04;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO040 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co04/CO040.java,v 1.10 2006/05/23 07:49:45 hramkr Exp $";

	public CO040() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO04 decl = new DeclCO04();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollMovableListMapping")) {
			CO042 co042 = new CO042(decl);
			co042.execute(tc);
		} else if(tc.getMapping().equals("CollSupplyListMapping")) {
			CO043 co043 = new CO043(decl);
			co043.execute(tc);
		} else if(tc.getMapping().equals("CollArtListMapping")) {
			CO044 co044 = new CO044(decl);
			co044.execute(tc);
		} else if(tc.getMapping().equals("CollPrecListMapping")) {
			CO045 co045 = new CO045(decl);
			co045.execute(tc);
		} else if(tc.getMapping().equals("VesselListMapping")) {
			CO046 co046 = new CO046(decl);
			co046.execute(tc);
		} else if(tc.getMapping().equals("CashDepListMapping")) {
			CO047 co047 = new CO047(decl);
			co047.execute(tc);
		} else if(tc.getMapping().equals("CollInsPolListMapping")) {
			CO048 co048 = new CO048(decl);
			co048.execute(tc);
		} else if(tc.getMapping().equals("CollGuarantListMapping")) {
			CO049 co049 = new CO049(decl);
			co049.execute(tc);
		} else if(tc.getMapping().equals("CollLoanStockListMap")) {
			CO04A co04a = new CO04A(decl);
			co04a.execute(tc);
		} else { 
			// basic mapping
			CO041 co041 = new CO041(decl);
			co041.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO040().run(ca);
	}

}