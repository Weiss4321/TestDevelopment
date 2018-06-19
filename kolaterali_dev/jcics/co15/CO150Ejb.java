package hr.vestigo.modules.collateral.jcics.co15;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO150Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co15/CO150Ejb.java,v 1.13 2015/01/20 12:32:28 hrazst Exp $";

	public CO150Ejb() {
	}

	public CO150Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO15 decl = new DeclCO15();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollBoeListMapping".equals(mapping)) {
			CO151 co151 = new CO151(decl);
			co151.execute(tc);
		} else if("CollBoeInsertMapping".equals(mapping)) {
			CO152 co152 = new CO152(decl);
			co152.execute(tc);
		} else if("CollBoeDetailMapping".equals(mapping)) {
			CO153 co153 = new CO153(decl);
			co153.execute(tc);
		} else if("CollBoeUpdateMapping".equals(mapping)) {
			CO154 co154 = new CO154(decl);
			co154.execute(tc);
		} else if("CollBoeQbeMapp".equals(mapping)) {
			CO155 co155 = new CO155(decl);
			co155.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
