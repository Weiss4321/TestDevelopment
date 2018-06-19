package hr.vestigo.modules.collateral.jcics.co34;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO340Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co34/CO340Ejb.java,v 1.5 2014/06/17 12:23:38 hrazst Exp $";

	public CO340Ejb() {
	}

	public CO340Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO34 decl = new DeclCO34();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("KolMortgageList".equals(mapping)) {
			CO341 co341 = new CO341(decl);
			co341.execute(tc);
		} else if("KolMortgageSel".equals(mapping)) {
			CO342 co342 = new CO342(decl);
			co342.execute(tc);
		} else if("KolMortgageDeact".equals(mapping)) {
			CO343 co343 = new CO343(decl);
			co343.execute(tc);
		} else if("KolateralDeact".equals(mapping)) {
			CO344 co344 = new CO344(decl);
			co344.execute(tc);
		} else if("MortageHistDetailsMapping".equals(mapping)) {
			CO345 co345 = new CO345(decl);
			co345.execute(tc);
		} else if("MortgageDeactPrintDemo".equals(mapping)) {
			CO346 co346 = new CO346(decl);
			co346.execute(tc);
		} else if("KolateralBeforeDeact".equals(mapping)) {
			CO347 co347 = new CO347(decl);
			co347.execute(tc);
		} else if("KolMortageDeactCount".equals(mapping)) {
			CO348 co348 = new CO348(decl);
			co348.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
