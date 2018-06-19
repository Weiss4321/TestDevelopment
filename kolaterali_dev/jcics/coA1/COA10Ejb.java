package hr.vestigo.modules.collateral.jcics.coA1;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA10Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA1/COA10Ejb.java,v 1.2 2012/03/30 09:23:18 hramkr Exp $";

	public COA10Ejb() {
	}
 
	public COA10Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOA1 decl = new DeclCOA1();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("GuarIssueMapping".equals(mapping)) {
			COA11 coa11 = new COA11(decl);
			coa11.execute(tc);
		} else if("InsuCompanySelectMapp".equals(mapping)) {
			COA12 coa12 = new COA12(decl);
			coa12.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
