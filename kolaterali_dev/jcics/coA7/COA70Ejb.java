package hr.vestigo.modules.collateral.jcics.coA7;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COA70Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coA7/COA70Ejb.java,v 1.2 2011/04/27 12:47:56 hrakis Exp $";

	public COA70Ejb() {
	}

	public COA70Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOA7 decl = new DeclCOA7();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("FrameAccExceptionSelectM".equals(mapping)) {
			COA71 coa71 = new COA71(decl);
			coa71.execute(tc);
		} else if("FrameAccExceptionDialogSelectM".equals(mapping)) {
			COA72 coa72 = new COA72(decl);
			coa72.execute(tc);
		} else if("FrameAccExceptionDialogUpdateM".equals(mapping)) {
			COA73 coa73 = new COA73(decl);
			coa73.execute(tc);
		} else if("FrameAccExceptionDialogInsertM".equals(mapping)) {
			COA74 coa74 = new COA74(decl);
			coa74.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
