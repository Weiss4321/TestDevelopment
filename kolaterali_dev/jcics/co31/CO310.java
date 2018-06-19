package hr.vestigo.modules.collateral.jcics.co31;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO310 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co31/CO310.java,v 1.3 2006/11/08 12:37:31 hrazst Exp $";

	public CO310() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO31 decl = new DeclCO31();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CadastreMapDialogInsertM")) {
			CO312 co312 = new CO312(decl);
			co312.execute(tc);
		} else if(tc.getMapping().equals("CadastreMapDialogUpdateM")) {
			CO313 co313 = new CO313(decl);
			co313.execute(tc);
		} else if(tc.getMapping().equals("CadastreMapDialogDeleteM")) {
			CO314 co314 = new CO314(decl);
			co314.execute(tc);
		} else if(tc.getMapping().equals("CadastreMapQBEM")) {
			CO315 co315 = new CO315(decl);
			co315.execute(tc);
		} else { 
			// basic mapping
			CO311 co311 = new CO311(decl);
			co311.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO310().run(ca);
	}

}