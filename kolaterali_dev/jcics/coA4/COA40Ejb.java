package hr.vestigo.modules.collateral.jcics.coA4;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA40Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA4/COA40Ejb.java,v 1.2 2014/09/18 10:37:41 hrakis Exp $";

	public COA40Ejb() {
	}

	public COA40Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOA4 decl = new DeclCOA4();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollTypeReportCountM".equals(mapping)) {
			COA41 coa41 = new COA41(decl);
			coa41.execute(tc);
		} else if("CollRevaDatesMapping".equals(mapping)) {
			COA42 coa42 = new COA42(decl);
			coa42.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
