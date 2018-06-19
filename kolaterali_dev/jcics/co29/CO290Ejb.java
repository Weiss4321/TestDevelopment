package hr.vestigo.modules.collateral.jcics.co29;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO290Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co29/CO290Ejb.java,v 1.2 2006/10/07 15:34:21 hramkr Exp $";

	public CO290Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO29 decl = new DeclCO29();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollHeadColNumLookUpMapp".equals(mapping)) {
			CO291 co291 = new CO291(decl);
			co291.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
