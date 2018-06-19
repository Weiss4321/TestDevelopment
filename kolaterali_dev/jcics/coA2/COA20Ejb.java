package hr.vestigo.modules.collateral.jcics.coA2;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA20Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA2/COA20Ejb.java,v 1.4 2017/04/14 10:17:31 hraziv Exp $";

	public COA20Ejb() {
	}

	public COA20Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOA2 decl = new DeclCOA2();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("VrpIsinMapping".equals(mapping)) {
			COA21 coa21 = new COA21(decl);
			coa21.execute(tc);
		} else if("CollEstimatorLookUpMapping".equals(mapping)) {
			COA22 coa22 = new COA22(decl);
			coa22.execute(tc);
		} else if("EconomicLifeSelectMapping".equals(mapping)) {
			COA23 coa23 = new COA23(decl);
			coa23.execute(tc);
		} else if("CollGcmTypLookUpMapping".equals(mapping)) {
			COA24 coa24 = new COA24(decl);
			coa24.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
