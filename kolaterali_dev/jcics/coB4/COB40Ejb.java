package hr.vestigo.modules.collateral.jcics.coB4;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COB40Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB4/COB40Ejb.java,v 1.1 2017/02/23 08:17:15 hraaks Exp $";

	public COB40Ejb() {
	}

	public COB40Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOB4 decl = new DeclCOB4();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("GetBillFromAPSMapping".equals(mapping)) {
			COB41 cob41 = new COB41(decl);
			cob41.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
