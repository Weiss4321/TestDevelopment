package hr.vestigo.modules.collateral.jcics.coB0;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COB00Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB0/COB00Ejb.java,v 1.2 2017/11/29 09:46:18 hrazst Exp $";

	public COB00Ejb() {
	}

	public COB00Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOB0 decl = new DeclCOB0();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollEstimatorDialogInsertMapping".equals(mapping)) {
			COB01 cob01 = new COB01(decl);
			cob01.execute(tc);
		} else if("CollEstimatorDialogDetailsMapping".equals(mapping)) {
			COB02 cob02 = new COB02(decl);
			cob02.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
