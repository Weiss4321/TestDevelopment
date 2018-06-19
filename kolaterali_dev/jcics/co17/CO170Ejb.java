package hr.vestigo.modules.collateral.jcics.co17;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO170Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co17/CO170Ejb.java,v 1.8 2006/06/07 06:38:27 hrajkl Exp $";

	public CO170Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO17 decl = new DeclCO17();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("RealEstateTypeListM".equals(mapping)) {
			CO171 co171 = new CO171(decl);
			co171.execute(tc);
		} else if("RealEstateTypeInsertM".equals(mapping)) {
			CO172 co172 = new CO172(decl);
			co172.execute(tc);
		} else if("RealEstateTypeDeleteM".equals(mapping)) {
			CO173 co173 = new CO173(decl);
			co173.execute(tc);
		} else if("RealEstateTypeUpdateM".equals(mapping)) {
			CO174 co174 = new CO174(decl);
			co174.execute(tc);
		} else if("RealEstateTypeQuerryM".equals(mapping)) {
			CO175 co175 = new CO175(decl);
			co175.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
