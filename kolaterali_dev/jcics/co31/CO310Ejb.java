package hr.vestigo.modules.collateral.jcics.co31;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO310Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co31/CO310Ejb.java,v 1.3 2006/11/08 12:37:31 hrazst Exp $";

	public CO310Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO31 decl = new DeclCO31();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CadastreMapM".equals(mapping)) {
			CO311 co311 = new CO311(decl);
			co311.execute(tc);
		} else if("CadastreMapDialogInsertM".equals(mapping)) {
			CO312 co312 = new CO312(decl);
			co312.execute(tc);
		} else if("CadastreMapDialogUpdateM".equals(mapping)) {
			CO313 co313 = new CO313(decl);
			co313.execute(tc);
		} else if("CadastreMapDialogDeleteM".equals(mapping)) {
			CO314 co314 = new CO314(decl);
			co314.execute(tc);
		} else if("CadastreMapQBEM".equals(mapping)) {
			CO315 co315 = new CO315(decl);
			co315.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
