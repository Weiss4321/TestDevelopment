package hr.vestigo.modules.collateral.jcics.co05;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO050Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co05/CO050Ejb.java,v 1.22 2017/06/01 12:03:46 hrazst Exp $";

	public CO050Ejb() {
	}

	public CO050Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO05 decl = new DeclCO05();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollSecPaperInsertMapping".equals(mapping)) {
			CO051 co051 = new CO051(decl);
			co051.execute(tc);
		} else if("CollMovableInsertMapping".equals(mapping)) {
			CO052 co052 = new CO052(decl);
			co052.execute(tc);
		} else if("CollArtInsertMapping".equals(mapping)) {
			CO053 co053 = new CO053(decl);
			co053.execute(tc);
		} else if("CollPrecInsertMapping".equals(mapping)) {
			CO054 co054 = new CO054(decl);
			co054.execute(tc);
		} else if("VesselInsertMapping".equals(mapping)) {
			CO055 co055 = new CO055(decl);
			co055.execute(tc);
		} else if("CashDepInsertMapping".equals(mapping)) {
			CO056 co056 = new CO056(decl);
			co056.execute(tc);
		} else if("CollInsPolInsertMapping".equals(mapping)) {
			CO057 co057 = new CO057(decl);
			co057.execute(tc);
		} else if("CollGuarantInsertMapping".equals(mapping)) {
			CO058 co058 = new CO058(decl);
			co058.execute(tc);
		} else if("CollLoanStockInsertMap".equals(mapping)) {
			CO059 co059 = new CO059(decl);
			co059.execute(tc);
		} else if("VehInsertMapping".equals(mapping)) {
			CO05A co05a = new CO05A(decl);
			co05a.execute(tc);
		} else if("CollCesijaInsertMapping".equals(mapping)) {
			CO05B co05b = new CO05B(decl);
			co05b.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
