package hr.vestigo.modules.collateral.jcics.co18;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO180Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co18/CO180Ejb.java,v 1.4 2013/12/23 09:11:38 hrajkl Exp $";

	public CO180Ejb() {
	}

	public CO180Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO18 decl = new DeclCO18();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("InsuPolicySelectM".equals(mapping)) {
			CO181 co181 = new CO181(decl);
			co181.execute(tc);
		} else if("InsuPolicyDialogInsertM".equals(mapping)) {
			CO182 co182 = new CO182(decl);
			co182.execute(tc);
		} else if("InsuPolicyDialogUpdateM".equals(mapping)) {
			CO183 co183 = new CO183(decl);
			co183.execute(tc);
		} else if("InsuPolicyDialogDeleteM".equals(mapping)) {
			CO184 co184 = new CO184(decl);
			co184.execute(tc);
		} else if("InsuPolicyStatusUpdateM".equals(mapping)) {
			CO185 co185 = new CO185(decl);
			co185.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
