package hr.vestigo.modules.collateral.jcics.co20;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO200Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co20/CO200Ejb.java,v 1.5 2006/06/19 10:02:13 hraamh Exp $";

	public CO200Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO20 decl = new DeclCO20();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("ReSubTypeSelectM".equals(mapping)) {
			CO201 co201 = new CO201(decl);
			co201.execute(tc);
		} else if("ReSubTypeInsertM".equals(mapping)) {
			CO202 co202 = new CO202(decl);
			co202.execute(tc);
		} else if("ReSubTypeUpdateM".equals(mapping)) {
			CO203 co203 = new CO203(decl);
			co203.execute(tc);
		} else if("ReSubTypeDeleteM".equals(mapping)) {
			CO204 co204 = new CO204(decl);
			co204.execute(tc);
		} else if("ReSubTypeQuerryM".equals(mapping)) {
			CO205 co205 = new CO205(decl);
			co205.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
