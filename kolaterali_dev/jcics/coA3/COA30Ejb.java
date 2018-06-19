package hr.vestigo.modules.collateral.jcics.coA3;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA30Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA3/COA30Ejb.java,v 1.1 2009/09/25 11:33:00 hrakis Exp $";

	public COA30Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCOA3 decl = new DeclCOA3();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollPonderSelectM".equals(mapping)) {
			COA31 coa31 = new COA31(decl);
			coa31.execute(tc);
		} else if("CollPonderInsertM".equals(mapping)) {
			COA32 coa32 = new COA32(decl);
			coa32.execute(tc);
		} else if("CollPonderQueryM".equals(mapping)) {
			COA33 coa33 = new COA33(decl);
			coa33.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
