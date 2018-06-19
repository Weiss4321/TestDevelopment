package hr.vestigo.modules.collateral.jcics.coA7;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA70 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA7/COA70.java,v 1.2 2011/04/27 12:47:57 hrakis Exp $";

	public COA70() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA7 decl = new DeclCOA7();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("FrameAccExceptionDialogSelectM")) {
			COA72 coa72 = new COA72(decl);
			coa72.execute(tc);
		} else if(tc.getMapping().equals("FrameAccExceptionDialogUpdateM")) {
			COA73 coa73 = new COA73(decl);
			coa73.execute(tc);
		} else if(tc.getMapping().equals("FrameAccExceptionDialogInsertM")) {
			COA74 coa74 = new COA74(decl);
			coa74.execute(tc);
		} else { 
			// basic mapping
			COA71 coa71 = new COA71(decl);
			coa71.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COA70().run(ca);
	}

}