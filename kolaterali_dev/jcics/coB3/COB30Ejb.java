package hr.vestigo.modules.collateral.jcics.coB3;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COB30Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB3/COB30Ejb.java,v 1.2 2017/04/14 10:13:07 hraziv Exp $";

	public COB30Ejb() {
	}

	public COB30Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOB3 decl = new DeclCOB3();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollMappingListMapping".equals(mapping)) {
			COB31 cob31 = new COB31(decl);
			cob31.execute(tc);
		} else if("CollMappingDialogDetailsMapping".equals(mapping)) {
			COB32 cob32 = new COB32(decl);
			cob32.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
