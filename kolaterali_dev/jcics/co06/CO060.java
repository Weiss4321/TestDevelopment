package hr.vestigo.modules.collateral.jcics.co06;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO060 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co06/CO060.java,v 1.26 2017/12/21 14:04:04 hrazst Exp $";

	public CO060() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO06 decl = new DeclCO06();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollMovableSelectMapping")) {
			CO062 co062 = new CO062(decl);
			co062.execute(tc);
		} else if(tc.getMapping().equals("CollArtSelectMapping")) {
			CO063 co063 = new CO063(decl);
			co063.execute(tc);
		} else if(tc.getMapping().equals("CollPrecSelectMapping")) {
			CO064 co064 = new CO064(decl);
			co064.execute(tc);
		} else if(tc.getMapping().equals("VesselSelectMapping")) {
			CO065 co065 = new CO065(decl);
			co065.execute(tc);
		} else if(tc.getMapping().equals("CachDepSelectMapping")) {
			CO066 co066 = new CO066(decl);
			co066.execute(tc);
		} else if(tc.getMapping().equals("CollInsPolSelectMapping")) {
			CO067 co067 = new CO067(decl);
			co067.execute(tc);
		} else if(tc.getMapping().equals("CollGuarantSelectMapping")) {
			CO068 co068 = new CO068(decl);
			co068.execute(tc);
		} else if(tc.getMapping().equals("CollLoanStockSelectMap")) {
			CO069 co069 = new CO069(decl);
			co069.execute(tc);
		} else if(tc.getMapping().equals("VehSelectMapping")) {
			CO06A co06a = new CO06A(decl);
			co06a.execute(tc);
		} else if(tc.getMapping().equals("CollCesijaSelectMapping")) {
			CO06B co06b = new CO06B(decl);
			co06b.execute(tc);
		} else if(tc.getMapping().equals("CashDepExceptionAccountCheckMapping")) {
			CO06C co06c = new CO06C(decl);
			co06c.execute(tc);
		} else { 
			// basic mapping
			CO061 co061 = new CO061(decl);
			co061.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO060().run(ca);
	}

}