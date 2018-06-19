package hr.vestigo.modules.collateral.jcics.coB2;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class COB20 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/coB2/COB20.java,v 1.2 2015/12/03 08:51:11 hraziv Exp $";

	public COB20() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCOB2 decl = new DeclCOB2();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollPolMapRegionDialogDetailsMapping")) {
			COB22 cob22 = new COB22(decl);
			cob22.execute(tc);
		} else if(tc.getMapping().equals("CollPolRegionMapCloseMapping")) {
			COB23 cob23 = new COB23(decl);
			cob23.execute(tc);
		} else if(tc.getMapping().equals("CollPolRegionMapDialogInsertMapping")) {
			COB24 cob24 = new COB24(decl);
			cob24.execute(tc);
		} else if(tc.getMapping().equals("CollPolRegionMapQBEMapping")) {
			COB25 cob25 = new COB25(decl);
			cob25.execute(tc);
		} else { 
			// basic mapping
			COB21 cob21 = new COB21(decl);
			cob21.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new COB20().run(ca);
	}

}