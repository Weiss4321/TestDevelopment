package hr.vestigo.modules.collateral.jcics.coA8;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COA80 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA8/COA80.java,v 1.4 2012/09/26 10:20:29 hrakis Exp $";

	public COA80() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOA8 decl = new DeclCOA8();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("InsPolWarningNotesListStopSendM")) {
			COA82 coa82 = new COA82(decl);
			coa82.execute(tc);
		} else if(tc.getMapping().equals("InsPolWarningNotesListWarnToNoteM")) {
			COA83 coa83 = new COA83(decl);
			coa83.execute(tc);
		} else if(tc.getMapping().equals("InsPolStatChgHistoryListSelectM")) {
			COA84 coa84 = new COA84(decl);
			coa84.execute(tc);
		} else { 
			// basic mapping
			COA81 coa81 = new COA81(decl);
			coa81.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COA80().run(ca);
	}

}