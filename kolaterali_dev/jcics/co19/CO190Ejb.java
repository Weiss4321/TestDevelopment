package hr.vestigo.modules.collateral.jcics.co19;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO190Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co19/CO190Ejb.java,v 1.4 2015/01/13 08:26:43 hrajkl Exp $";

	public CO190Ejb() {
	}

	public CO190Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO19 decl = new DeclCO19();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("InsuPolicyTypeListM".equals(mapping)) {
			CO191 co191 = new CO191(decl);
			co191.execute(tc);
		} else if("InsuPolicyTypeInsertM".equals(mapping)) {
			CO192 co192 = new CO192(decl);
			co192.execute(tc);
		} else if("InsuPolicyTypeDeleteM".equals(mapping)) {
			CO193 co193 = new CO193(decl);
			co193.execute(tc);
		} else if("InsuPolicyTypeUpdateM".equals(mapping)) {
			CO194 co194 = new CO194(decl);
			co194.execute(tc);
		} else if("InsuPolicyTypeQuerryM".equals(mapping)) {
			CO195 co195 = new CO195(decl);
			co195.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
