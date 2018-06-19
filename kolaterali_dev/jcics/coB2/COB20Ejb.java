package hr.vestigo.modules.collateral.jcics.coB2;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class COB20Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB2/COB20Ejb.java,v 1.2 2015/12/03 08:51:11 hraziv Exp $";

	public COB20Ejb() {
	}

	public COB20Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCOB2 decl = new DeclCOB2();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollPolRegionMapListMapping".equals(mapping)) {
			COB21 cob21 = new COB21(decl);
			cob21.execute(tc);
		} else if("CollPolMapRegionDialogDetailsMapping".equals(mapping)) {
			COB22 cob22 = new COB22(decl);
			cob22.execute(tc);
		} else if("CollPolRegionMapCloseMapping".equals(mapping)) {
			COB23 cob23 = new COB23(decl);
			cob23.execute(tc);
		} else if("CollPolRegionMapDialogInsertMapping".equals(mapping)) {
			COB24 cob24 = new COB24(decl);
			cob24.execute(tc);
		} else if("CollPolRegionMapQBEMapping".equals(mapping)) {
			COB25 cob25 = new COB25(decl);
			cob25.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
