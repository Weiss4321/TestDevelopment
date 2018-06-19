package hr.vestigo.modules.collateral.jcics.co39;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO390Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/CO390Ejb.java,v 1.2 2008/05/12 11:44:01 hraamh Exp $";

	public CO390Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO39 decl = new DeclCO39();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("getCollateralsByCollateralIDList".equals(mapping)) {
			CO391 co391 = new CO391(decl);
			co391.execute(tc);
		} else if("getAccountLinkedCollateralsByCustomerIDList".equals(mapping)) {
			CO392 co392 = new CO392(decl);
			co392.execute(tc);
		} else if("getCollateralVesselByNaturalKey".equals(mapping)) {
			CO393 co393 = new CO393(decl);
			co393.execute(tc);
		} else if("getCollateralCashDepositByNaturalKey".equals(mapping)) {
			CO394 co394 = new CO394(decl);
			co394.execute(tc);
		} else if("getCollateralLifeInsuranceByNaturalKey".equals(mapping)) {
			CO395 co395 = new CO395(decl);
			co395.execute(tc);
		} else if("getCollateralRealEstateByNaturalKey".equals(mapping)) {
			CO396 co396 = new CO396(decl);
			co396.execute(tc);
		} else if("getCollateralVehicleByNaturalKey".equals(mapping)) {
			CO397 co397 = new CO397(decl);
			co397.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
