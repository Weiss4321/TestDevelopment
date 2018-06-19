package hr.vestigo.modules.collateral.jcics.co22;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO220Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co22/CO220Ejb.java,v 1.1 2006/06/10 15:07:40 hramkr Exp $";

	public CO220Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO22 decl = new DeclCO22();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("ColTypeListSel".equals(mapping)) {
			CO221 co221 = new CO221(decl);
			co221.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
