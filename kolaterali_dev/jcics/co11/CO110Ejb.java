package hr.vestigo.modules.collateral.jcics.co11;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO110Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co11/CO110Ejb.java,v 1.15 2017/06/01 12:03:41 hrazst Exp $";

	public CO110Ejb() {
	}

	public CO110Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO11 decl = new DeclCO11();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollSecPaperUpdateMapping".equals(mapping)) {
			CO111 co111 = new CO111(decl);
			co111.execute(tc);
		} else if("CollMovableUpdateMapping".equals(mapping)) {
			CO112 co112 = new CO112(decl);
			co112.execute(tc);
		} else if("CollArtUpdateMapping".equals(mapping)) {
			CO113 co113 = new CO113(decl);
			co113.execute(tc);
		} else if("CollPrecUpdateMapping".equals(mapping)) {
			CO114 co114 = new CO114(decl);
			co114.execute(tc);
		} else if("VesselUpdateMapping".equals(mapping)) {
			CO115 co115 = new CO115(decl);
			co115.execute(tc);
		} else if("CashDepUpdateMapping".equals(mapping)) {
			CO116 co116 = new CO116(decl);
			co116.execute(tc);
		} else if("CollInsPolUpdateMapping".equals(mapping)) {
			CO117 co117 = new CO117(decl);
			co117.execute(tc);
		} else if("CollGuarantUpdateMapping".equals(mapping)) {
			CO118 co118 = new CO118(decl);
			co118.execute(tc);
		} else if("CollLoanStockUpdateMap".equals(mapping)) {
			CO119 co119 = new CO119(decl);
			co119.execute(tc);
		} else if("VehUpdateMapping".equals(mapping)) {
			CO11A co11a = new CO11A(decl);
			co11a.execute(tc);
		} else if("CollCesijaUpdateMapping".equals(mapping)) {
			CO11B co11b = new CO11B(decl);
			co11b.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
