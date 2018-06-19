package hr.vestigo.modules.collateral.jcics.co41;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO410Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co41/CO410Ejb.java,v 1.3 2014/05/05 09:00:35 hraaks Exp $";

	public CO410Ejb() {
	}

	public CO410Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO41 decl = new DeclCO41();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("COHistoryViewSelectM".equals(mapping)) {
			CO411 co411 = new CO411(decl);
			co411.execute(tc);
		} else if("CollInvestPartiesAllQuerryDate".equals(mapping)) {
			CO412 co412 = new CO412(decl);
			co412.execute(tc);
		} else if("CollCesReportQueryDate".equals(mapping)) {
			CO413 co413 = new CO413(decl);
			co413.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
