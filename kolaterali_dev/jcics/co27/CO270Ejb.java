package hr.vestigo.modules.collateral.jcics.co27;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO270Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co27/CO270Ejb.java,v 1.5 2007/06/28 07:26:51 hratar Exp $";

	public CO270Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO27 decl = new DeclCO27();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CusaccExpCollMapping".equals(mapping)) {
			CO271 co271 = new CO271(decl);
			co271.execute(tc);
		} else if("CusaccExpCollQBEMapping".equals(mapping)) {
			CO272 co272 = new CO272(decl);
			co272.execute(tc);
		} else if("CusaccExpCollQBE2Mapping".equals(mapping)) {
			CO273 co273 = new CO273(decl);
			co273.execute(tc);
		} else if("CusaccExpCollDetailsMapping".equals(mapping)) {
			CO274 co274 = new CO274(decl);
			co274.execute(tc);
		} else if("ColWorkListCovMapping".equals(mapping)) {
			CO275 co275 = new CO275(decl);
			co275.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
