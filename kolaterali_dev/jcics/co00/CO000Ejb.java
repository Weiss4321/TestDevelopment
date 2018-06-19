package hr.vestigo.modules.collateral.jcics.co00;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO000Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co00/CO000Ejb.java,v 1.28 2017/12/21 14:04:08 hrazst Exp $";

	public CO000Ejb() {
	}

	public CO000Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO00 decl = new DeclCO00();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("ReDLisMap".equals(mapping)) {
			CO001 co001 = new CO001(decl);
			co001.execute(tc);
		} else if("ReDInsMap".equals(mapping)) {
			CO002 co002 = new CO002(decl);
			co002.execute(tc);
		} else if("ReDDetMap".equals(mapping)) {
			CO003 co003 = new CO003(decl);
			co003.execute(tc);
		} else if("ReDUpdMap".equals(mapping)) {
			CO004 co004 = new CO004(decl);
			co004.execute(tc);
		} else if("ReDQbeLisMap".equals(mapping)) {
			CO005 co005 = new CO005(decl);
			co005.execute(tc);
		} else if("ReDQBExMap".equals(mapping)) {
			CO006 co006 = new CO006(decl);
			co006.execute(tc);
		} else if("ReDCheExiMap".equals(mapping)) {
			CO007 co007 = new CO007(decl);
			co007.execute(tc);
		} else if("ReDFetchVal".equals(mapping)) {
			CO008 co008 = new CO008(decl);
			co008.execute(tc);
		} else if("RecoveryAmountHistoryMapping".equals(mapping)) {
			CO009 co009 = new CO009(decl);
			co009.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
