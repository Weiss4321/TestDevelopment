package hr.vestigo.modules.collateral.jcics.coB1;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COB10Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB1/COB10Ejb.java,v 1.2 2017/11/29 09:46:19 hrazst Exp $";

	public COB10Ejb() {
	}

	public COB10Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOB1 decl = new DeclCOB1();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollEstimatorListMapping".equals(mapping)) {
			COB11 cob11 = new COB11(decl);
			cob11.execute(tc);
		} else if("CollEstimatorQBEMapping".equals(mapping)) {
			COB12 cob12 = new COB12(decl);
			cob12.execute(tc);
		} else if("CollEstimatorCloseMapping".equals(mapping)) {
			COB13 cob13 = new COB13(decl);
			cob13.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
