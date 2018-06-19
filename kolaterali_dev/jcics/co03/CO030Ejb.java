package hr.vestigo.modules.collateral.jcics.co03;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO030Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co03/CO030Ejb.java,v 1.14 2014/10/07 10:54:20 hrazst Exp $";

	public CO030Ejb() {
	}

	public CO030Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO03 decl = new DeclCO03();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("ChpLisAllMap".equals(mapping)) {
			CO031 co031 = new CO031(decl);
			co031.execute(tc);
		} else if("ChpDInsMap".equals(mapping)) {
			CO032 co032 = new CO032(decl);
			co032.execute(tc);
		} else if("ChpDPlaUseMap".equals(mapping)) {
			CO033 co033 = new CO033(decl);
			co033.execute(tc);
		} else if("ChpDDetMap".equals(mapping)) {
			CO034 co034 = new CO034(decl);
			co034.execute(tc);
		} else if("ChpDUpdMap".equals(mapping)) {
			CO035 co035 = new CO035(decl);
			co035.execute(tc);
		} else if("ChpDUserLockM".equals(mapping)) {
			CO036 co036 = new CO036(decl);
			co036.execute(tc);
		} else if("ChpLoanMap".equals(mapping)) {
			CO037 co037 = new CO037(decl);
			co037.execute(tc);
		} else if("MrtgDelMap".equals(mapping)) {
			CO038 co038 = new CO038(decl);
			co038.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
