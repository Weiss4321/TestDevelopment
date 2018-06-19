package hr.vestigo.modules.collateral.jcics.co24;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO240Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co24/CO240Ejb.java,v 1.4 2006/07/12 09:02:23 hraaks Exp $";

	public CO240Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO24 decl = new DeclCO24();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollCourtSelectM".equals(mapping)) {
			CO241 co241 = new CO241(decl);
			co241.execute(tc);
		} else if("CollCourtQuerryM".equals(mapping)) {
			CO242 co242 = new CO242(decl);
			co242.execute(tc);
		} else if("CollCourtInsertM".equals(mapping)) {
			CO243 co243 = new CO243(decl);
			co243.execute(tc);
		} else if("CollCourtUpdateM".equals(mapping)) {
			CO244 co244 = new CO244(decl);
			co244.execute(tc);
		} else if("CollCourtDeleteM".equals(mapping)) {
			CO245 co245 = new CO245(decl);
			co245.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
