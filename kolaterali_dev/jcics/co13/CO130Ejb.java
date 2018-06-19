package hr.vestigo.modules.collateral.jcics.co13;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO130Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co13/CO130Ejb.java,v 1.7 2010/07/16 13:37:06 hramlo Exp $";

	public CO130Ejb() {
	}

	public CO130Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO13 decl = new DeclCO13();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollOwnersMapping".equals(mapping)) {
			CO131 co131 = new CO131(decl);
			co131.execute(tc);
		} else if("CollOwnersInsertMapping".equals(mapping)) {
			CO132 co132 = new CO132(decl);
			co132.execute(tc);
		} else if("CollOwnersDeleteMapping".equals(mapping)) {
			CO133 co133 = new CO133(decl);
			co133.execute(tc);
		} else if("CollOwnersUpdateMapping".equals(mapping)) {
			CO134 co134 = new CO134(decl);
			co134.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
