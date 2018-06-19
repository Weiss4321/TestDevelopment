package hr.vestigo.modules.collateral.jcics.coA0;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA00Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA0/COA00Ejb.java,v 1.3 2014/10/07 10:54:20 hrazst Exp $";

	public COA00Ejb() {
	}

	public COA00Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOA0 decl = new DeclCOA0();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("COPonderInsertMap".equals(mapping)) {
			COA01 coa01 = new COA01(decl);
			coa01.execute(tc);
		} else if("COPonderSelectMap".equals(mapping)) {
			COA02 coa02 = new COA02(decl);
			coa02.execute(tc);
		} else if("COPonderListMap".equals(mapping)) {
			COA03 coa03 = new COA03(decl);
			coa03.execute(tc);
		} else if("COPonderUpdateMap".equals(mapping)) {
			COA04 coa04 = new COA04(decl);
			coa04.execute(tc);
		} else if("WCAListMap".equals(mapping)) {
			COA05 coa05 = new COA05(decl);
			coa05.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
