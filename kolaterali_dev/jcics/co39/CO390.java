package hr.vestigo.modules.collateral.jcics.co39;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO390 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/CO390.java,v 1.2 2008/05/12 11:44:01 hraamh Exp $";

	public CO390() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO39 decl = new DeclCO39();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("getAccountLinkedCollateralsByCustomerIDList")) {
			CO392 co392 = new CO392(decl);
			co392.execute(tc);
		} else if(tc.getMapping().equals("getCollateralVesselByNaturalKey")) {
			CO393 co393 = new CO393(decl);
			co393.execute(tc);
		} else if(tc.getMapping().equals("getCollateralCashDepositByNaturalKey")) {
			CO394 co394 = new CO394(decl);
			co394.execute(tc);
		} else if(tc.getMapping().equals("getCollateralLifeInsuranceByNaturalKey")) {
			CO395 co395 = new CO395(decl);
			co395.execute(tc);
		} else if(tc.getMapping().equals("getCollateralRealEstateByNaturalKey")) {
			CO396 co396 = new CO396(decl);
			co396.execute(tc);
		} else if(tc.getMapping().equals("getCollateralVehicleByNaturalKey")) {
			CO397 co397 = new CO397(decl);
			co397.execute(tc);
		} else { 
			// basic mapping
			CO391 co391 = new CO391(decl);
			co391.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO390().run(ca);
	}

}