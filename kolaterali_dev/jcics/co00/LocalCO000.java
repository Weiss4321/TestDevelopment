package hr.vestigo.modules.collateral.jcics.co00;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO000 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co00/LocalCO000.java,v 1.12 2006/08/18 07:51:54 hrasia Exp $";

	public LocalCO000() {
	}

	public void executeProgram() throws Exception {
		DeclCO00 decl = new DeclCO00();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("ReDInsMap")) {
			CO002 co002 = new CO002(decl);
			co002.execute(tc);
		} else if(tc.getMapping().equals("ReDDetMap")) {
			CO003 co003 = new CO003(decl);
			co003.execute(tc);
		} else if(tc.getMapping().equals("ReDUpdMap")) {
			CO004 co004 = new CO004(decl);
			co004.execute(tc);
		} else if(tc.getMapping().equals("ReDQbeLisMap")) {
			CO005 co005 = new CO005(decl);
			co005.execute(tc);
		} else if(tc.getMapping().equals("ReDQBExMap")) {
			CO006 co006 = new CO006(decl);
			co006.execute(tc);
		} else if(tc.getMapping().equals("ReDCheExiMap")) {
			CO007 co007 = new CO007(decl);
			co007.execute(tc);
		} else if(tc.getMapping().equals("ReDFetchVal")) {
			CO008 co008 = new CO008(decl);
			co008.execute(tc);
		} else { 
			// basic mapping
			CO001 co001 = new CO001(decl);
			co001.execute(tc);
		}
	}

}