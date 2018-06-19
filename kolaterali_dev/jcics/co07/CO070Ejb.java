package hr.vestigo.modules.collateral.jcics.co07;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO070Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co07/CO070Ejb.java,v 1.2 2006/02/21 07:45:12 hrasia Exp $";

	public CO070Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO07 decl = new DeclCO07();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollateralListMapping".equals(mapping)) {
			CO071 co071 = new CO071(decl);
			co071.execute(tc);
		} else if("CollateralDialogInsertMapping".equals(mapping)) {
			CO072 co072 = new CO072(decl);
			co072.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
