package hr.vestigo.modules.collateral.jcics.coA8;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA80Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA8/COA80Ejb.java,v 1.4 2012/09/26 10:20:28 hrakis Exp $";

	public COA80Ejb() {
	}

	public COA80Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOA8 decl = new DeclCOA8();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("InsPolWarningNotesListSelectM".equals(mapping)) {
			COA81 coa81 = new COA81(decl);
			coa81.execute(tc);
		} else if("InsPolWarningNotesListStopSendM".equals(mapping)) {
			COA82 coa82 = new COA82(decl);
			coa82.execute(tc);
		} else if("InsPolWarningNotesListWarnToNoteM".equals(mapping)) {
			COA83 coa83 = new COA83(decl);
			coa83.execute(tc);
		} else if("InsPolStatChgHistoryListSelectM".equals(mapping)) {
			COA84 coa84 = new COA84(decl);
			coa84.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
