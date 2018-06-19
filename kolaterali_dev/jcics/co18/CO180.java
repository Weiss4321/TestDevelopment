package hr.vestigo.modules.collateral.jcics.co18;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO180 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co18/CO180.java,v 1.4 2013/12/23 09:11:38 hrajkl Exp $";

	public CO180() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO18 decl = new DeclCO18();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("InsuPolicyDialogInsertM")) {
			CO182 co182 = new CO182(decl);
			co182.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyDialogUpdateM")) {
			CO183 co183 = new CO183(decl);
			co183.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyDialogDeleteM")) {
			CO184 co184 = new CO184(decl);
			co184.execute(tc);
		} else if(tc.getMapping().equals("InsuPolicyStatusUpdateM")) {
			CO185 co185 = new CO185(decl);
			co185.execute(tc);
		} else { 
			// basic mapping
			CO181 co181 = new CO181(decl);
			co181.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO180().run(ca);
	}

}