package hr.vestigo.modules.collateral.jcics.co40;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO400Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co40/CO400Ejb.java,v 1.5 2012/03/06 12:39:05 hramlo Exp $";

	public CO400Ejb() {
	}

	public CO400Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO40 decl = new DeclCO40();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("VehDeactOwnerMapping".equals(mapping)) {
			CO401 co401 = new CO401(decl);
			co401.execute(tc);
		} else if("RealEstDeactOwnerMapping".equals(mapping)) {
			CO402 co402 = new CO402(decl);
			co402.execute(tc);
		} else if("MovDeactOwnerMapping".equals(mapping)) {
			CO403 co403 = new CO403(decl);
			co403.execute(tc);
		} else if("VesDeactOwnerMapping".equals(mapping)) {
			CO404 co404 = new CO404(decl);
			co404.execute(tc);
		} else if("VrpDeactOwnerMapping".equals(mapping)) {
			CO405 co405 = new CO405(decl);
			co405.execute(tc);
		} else if("InsPolDeactOwnerMapping".equals(mapping)) {
			CO406 co406 = new CO406(decl);
			co406.execute(tc);
		} else if("CashDepDeactOwnerMapping".equals(mapping)) {
			CO407 co407 = new CO407(decl);
			co407.execute(tc);
		} else if("GuarantDeactOwnerMapping".equals(mapping)) {
			CO408 co408 = new CO408(decl);
			co408.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
