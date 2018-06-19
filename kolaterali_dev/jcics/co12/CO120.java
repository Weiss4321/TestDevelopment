package hr.vestigo.modules.collateral.jcics.co12;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO120 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co12/CO120.java,v 1.6 2006/04/13 09:49:02 hrarmv Exp $";

	public CO120() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO12 decl = new DeclCO12();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("PolMapDistrictInsertMapping")) {
			CO122 co122 = new CO122(decl);
			co122.execute(tc);
		} else if(tc.getMapping().equals("PolMapDistrictDeleteMapping")) {
			CO123 co123 = new CO123(decl);
			co123.execute(tc);
		} else if(tc.getMapping().equals("PolMapDistrictUpdateMapping")) {
			CO124 co124 = new CO124(decl);
			co124.execute(tc);
		} else if(tc.getMapping().equals("PolMapResiQuarListMapping")) {
			CO125 co125 = new CO125(decl);
			co125.execute(tc);
		} else if(tc.getMapping().equals("PolMapSelectTypeMapping")) {
			CO126 co126 = new CO126(decl);
			co126.execute(tc);
		} else if(tc.getMapping().equals("PolMapResiQuarInsertMapping")) {
			CO127 co127 = new CO127(decl);
			co127.execute(tc);
		} else if(tc.getMapping().equals("PolMapResiQuarDeleteMapping")) {
			CO128 co128 = new CO128(decl);
			co128.execute(tc);
		} else if(tc.getMapping().equals("PolMapResiQuarUpdateMapping")) {
			CO129 co129 = new CO129(decl);
			co129.execute(tc);
		} else { 
			// basic mapping
			CO121 co121 = new CO121(decl);
			co121.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO120().run(ca);
	}

}