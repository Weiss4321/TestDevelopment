package hr.vestigo.modules.collateral.jcics.co14;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO140Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co14/CO140Ejb.java,v 1.3 2006/05/16 12:32:06 hrazst Exp $";

	public CO140Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO14 decl = new DeclCO14();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("RevRegCoefReSelectM".equals(mapping)) {
			CO141 co141 = new CO141(decl);
			co141.execute(tc);
		} else if("RevRegCoefReDialogInsertM".equals(mapping)) {
			CO142 co142 = new CO142(decl);
			co142.execute(tc);
		} else if("RevRegCoefReDialogUpdateM".equals(mapping)) {
			CO143 co143 = new CO143(decl);
			co143.execute(tc);
		} else if("RevRegCoefReDialogDeleteM".equals(mapping)) {
			CO144 co144 = new CO144(decl);
			co144.execute(tc);
		} else if("RevRegCoefReQuerryM".equals(mapping)) {
			CO145 co145 = new CO145(decl);
			co145.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
