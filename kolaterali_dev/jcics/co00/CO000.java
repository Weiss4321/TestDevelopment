package hr.vestigo.modules.collateral.jcics.co00;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO000 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co00/CO000.java,v 1.28 2017/12/21 14:04:08 hrazst Exp $";

	public CO000() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
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
		} else if(tc.getMapping().equals("RecoveryAmountHistoryMapping")) {
			CO009 co009 = new CO009(decl);
			co009.execute(tc);
		} else { 
			// basic mapping
			CO001 co001 = new CO001(decl);
			co001.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO000().run(ca);
	}

}