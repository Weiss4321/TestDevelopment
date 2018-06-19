package hr.vestigo.modules.collateral.jcics.co01;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO010Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co01/CO010Ejb.java,v 1.2 2005/12/30 11:27:05 hrasia Exp $";

	public CO010Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO01 decl = new DeclCO01();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("RevRegCoefReLookUpMapping".equals(mapping)) {
			CO011 co011 = new CO011(decl);
			co011.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
