package hr.vestigo.modules.collateral.jcics.co33;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO330Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co33/CO330Ejb.java,v 1.2 2007/03/06 14:19:06 hramkr Exp $";

	public CO330Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO33 decl = new DeclCO33();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("AgrListSndVer".equals(mapping)) {
			CO331 co331 = new CO331(decl);
			co331.execute(tc);
		} else if("AgrListStop".equals(mapping)) {
			CO332 co332 = new CO332(decl);
			co332.execute(tc);
		} else if("AgrListBack".equals(mapping)) {
			CO333 co333 = new CO333(decl);
			co333.execute(tc);
		} else if("AgrListVer".equals(mapping)) {
			CO334 co334 = new CO334(decl);
			co334.execute(tc);
		} else if("AgrListQSelect".equals(mapping)) {
			CO335 co335 = new CO335(decl);
			co335.execute(tc);
		} else if("AgrListClose".equals(mapping)) {
			CO336 co336 = new CO336(decl);
			co336.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
