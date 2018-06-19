package hr.vestigo.modules.collateral.jcics.co25;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO250Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co25/CO250Ejb.java,v 1.2 2006/08/17 10:38:36 hrasia Exp $";

	public CO250Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO25 decl = new DeclCO25();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollateralQbeMapp".equals(mapping)) {
			CO251 co251 = new CO251(decl);
			co251.execute(tc);
		} else if("LoanStockQbeMapp".equals(mapping)) {
			CO252 co252 = new CO252(decl);
			co252.execute(tc);
		} else if("CashDepQbeMapp".equals(mapping)) {
			CO253 co253 = new CO253(decl);
			co253.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
