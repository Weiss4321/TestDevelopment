package hr.vestigo.modules.collateral.jcics.co26;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO260Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co26/CO260Ejb.java,v 1.2 2006/09/21 13:33:51 hrazst Exp $";

	public CO260Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO26 decl = new DeclCO26();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("ColControlListingSelectM".equals(mapping)) {   
			CO261 co261 = new CO261(decl);
			co261.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
