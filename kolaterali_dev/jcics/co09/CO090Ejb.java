package hr.vestigo.modules.collateral.jcics.co09;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO090Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co09/CO090Ejb.java,v 1.7 2006/02/05 07:40:58 hrasia Exp $";

	public CO090Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO09 decl = new DeclCO09();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollHfPriorListRbaMapping".equals(mapping)) {
			CO091 co091 = new CO091(decl);
			co091.execute(tc);
		} else if("CollHfPriorDialogRbaMapping".equals(mapping)) {
			CO092 co092 = new CO092(decl);
			co092.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
