package hr.vestigo.modules.collateral.jcics.co23;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO230Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co23/CO230Ejb.java,v 1.2 2014/12/16 12:34:46 hrakis Exp $";

	public CO230Ejb() {
	}

	public CO230Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO23 decl = new DeclCO23();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("ColListQSelect".equals(mapping)) {
			CO231 co231 = new CO231(decl);
			co231.execute(tc);
		} else if("CollHistorySelectMapping".equals(mapping)) {
			CO232 co232 = new CO232(decl);
			co232.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
