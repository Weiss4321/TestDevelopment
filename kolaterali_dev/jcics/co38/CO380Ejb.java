package hr.vestigo.modules.collateral.jcics.co38;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO380Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co38/CO380Ejb.java,v 1.1 2007/10/12 08:35:15 hratar Exp $";

	public CO380Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO38 decl = new DeclCO38();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollExpDateLookUpMap".equals(mapping)) {
			CO381 co381 = new CO381(decl);
			co381.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
