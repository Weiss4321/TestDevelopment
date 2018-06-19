package hr.vestigo.modules.collateral.jcics.coB3;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COB30 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB3/COB30.java,v 1.2 2017/04/14 10:13:07 hraziv Exp $";

	public COB30() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOB3 decl = new DeclCOB3();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollMappingDialogDetailsMapping")) {
			COB32 cob32 = new COB32(decl);
			cob32.execute(tc);
		} else { 
			// basic mapping
			COB31 cob31 = new COB31(decl);
			cob31.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COB30().run(ca);
	}

}