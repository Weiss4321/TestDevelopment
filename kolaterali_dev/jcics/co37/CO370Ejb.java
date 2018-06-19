package hr.vestigo.modules.collateral.jcics.co37;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO370Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co37/CO370Ejb.java,v 1.1 2007/09/06 14:50:24 hraamh Exp $";

	public CO370Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO37 decl = new DeclCO37();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("APSCollateralDataM".equals(mapping)) {
			CO371 co371 = new CO371(decl);
			co371.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
