package hr.vestigo.modules.collateral.jcics.co20;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO200 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co20/CO200.java,v 1.5 2006/06/19 10:02:13 hraamh Exp $";

	public CO200() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO20 decl = new DeclCO20();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("ReSubTypeInsertM")) {
			CO202 co202 = new CO202(decl);
			co202.execute(tc);
		} else if(tc.getMapping().equals("ReSubTypeUpdateM")) {
			CO203 co203 = new CO203(decl);
			co203.execute(tc);
		} else if(tc.getMapping().equals("ReSubTypeDeleteM")) {
			CO204 co204 = new CO204(decl);
			co204.execute(tc);
		} else if(tc.getMapping().equals("ReSubTypeQuerryM")) {
			CO205 co205 = new CO205(decl);
			co205.execute(tc);
		} else { 
			// basic mapping
			CO201 co201 = new CO201(decl);
			co201.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO200().run(ca);
	}

}