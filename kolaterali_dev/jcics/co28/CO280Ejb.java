package hr.vestigo.modules.collateral.jcics.co28;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO280Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co28/CO280Ejb.java,v 1.3 2011/05/20 07:22:34 hramkr Exp $";

	public CO280Ejb() {
	}

	public CO280Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO28 decl = new DeclCO28();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CusaccExposureLookUpMapp".equals(mapping)) {
			CO281 co281 = new CO281(decl);
			co281.execute(tc);
		} else if("CusaccAccountROBILookUpMap".equals(mapping)) {
			CO282 co282 = new CO282(decl);
			co282.execute(tc);
		} else if("KolCusaccExpSelectM".equals(mapping)) {
			CO283 co283 = new CO283(decl);
			co283.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
