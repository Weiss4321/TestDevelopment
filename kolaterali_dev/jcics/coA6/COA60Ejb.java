package hr.vestigo.modules.collateral.jcics.coA6;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA60Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA6/COA60Ejb.java,v 1.3 2012/08/29 06:30:21 hraaks Exp $";

	public COA60Ejb() {
	}

	public COA60Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOA6 decl = new DeclCOA6();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollCashDepExceptionSelectM".equals(mapping)) {
			COA61 coa61 = new COA61(decl);
			coa61.execute(tc);
		} else if("CollCashDepExceptionInsertM".equals(mapping)) {
			COA62 coa62 = new COA62(decl);
			coa62.execute(tc);
		} else if("CollCashDepExceptionUpdateM".equals(mapping)) {
			COA63 coa63 = new COA63(decl);
			coa63.execute(tc);
		} else if("CollCashDepExceptionDetailM".equals(mapping)) {
			COA64 coa64 = new COA64(decl);
			coa64.execute(tc);
		} else if("CollCashDepExceptionQueryM".equals(mapping)) {
			COA65 coa65 = new COA65(decl);
			coa65.execute(tc);
		} else if("CollCashDepExceptionOwnerM".equals(mapping)) {
			COA66 coa66 = new COA66(decl);
			coa66.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
