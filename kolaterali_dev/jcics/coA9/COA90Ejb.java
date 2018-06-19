package hr.vestigo.modules.collateral.jcics.coA9;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA90Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA9/COA90Ejb.java,v 1.3 2012/05/14 07:38:12 hradnp Exp $";

	public COA90Ejb() {
	}

	public COA90Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOA9 decl = new DeclCOA9();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CoBorrowerSelectM".equals(mapping)) {
			COA91 coa91 = new COA91(decl);
			coa91.execute(tc);
		} else if("CoBorrowerInsertM".equals(mapping)) {
			COA92 coa92 = new COA92(decl);
			coa92.execute(tc);
		} else if("CoBorrowerUpdateM".equals(mapping)) {
			COA93 coa93 = new COA93(decl);
			coa93.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
