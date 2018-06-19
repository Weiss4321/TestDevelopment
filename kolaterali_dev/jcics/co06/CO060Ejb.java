package hr.vestigo.modules.collateral.jcics.co06;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO060Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co06/CO060Ejb.java,v 1.28 2017/12/21 14:04:03 hrazst Exp $";

	public CO060Ejb() {
	}

	public CO060Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO06 decl = new DeclCO06();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollSecPaperSelectMapping".equals(mapping)) {
			CO061 co061 = new CO061(decl);
			co061.execute(tc);
		} else if("CollMovableSelectMapping".equals(mapping)) {
			CO062 co062 = new CO062(decl);
			co062.execute(tc);
		} else if("CollArtSelectMapping".equals(mapping)) {
			CO063 co063 = new CO063(decl);
			co063.execute(tc);
		} else if("CollPrecSelectMapping".equals(mapping)) {
			CO064 co064 = new CO064(decl);
			co064.execute(tc);
		} else if("VesselSelectMapping".equals(mapping)) {
			CO065 co065 = new CO065(decl);
			co065.execute(tc);
		} else if("CachDepSelectMapping".equals(mapping)) {
			CO066 co066 = new CO066(decl);
			co066.execute(tc);
		} else if("CollInsPolSelectMapping".equals(mapping)) {
			CO067 co067 = new CO067(decl);
			co067.execute(tc);
		} else if("CollGuarantSelectMapping".equals(mapping)) {
			CO068 co068 = new CO068(decl);
			co068.execute(tc);
		} else if("CollLoanStockSelectMap".equals(mapping)) {
			CO069 co069 = new CO069(decl);
			co069.execute(tc);
		} else if("VehSelectMapping".equals(mapping)) {
			CO06A co06a = new CO06A(decl);
			co06a.execute(tc);
		} else if("CollCesijaSelectMapping".equals(mapping)) {
			CO06B co06b = new CO06B(decl);
			co06b.execute(tc);
		} else if("CashDepExceptionAccountCheckMapping".equals(mapping)) {
			CO06C co06c = new CO06C(decl);
			co06c.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
