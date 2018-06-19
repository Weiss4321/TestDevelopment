package hr.vestigo.modules.collateral.jcics.co21;

import com.ibm.cics.server.*;

import hr.vestigo.framework.remote.transaction.*;
import hr.vestigo.framework.remote.transaction.cics.*;

public class CO210 extends CicsProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co21/CO210.java,v 1.12 2016/12/05 09:54:29 hrazst Exp $";

	public CO210() {
	}

	public void executeProgram(CommAreaHolder ca) throws Exception {
		DeclCO21 decl = new DeclCO21();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("CollWorkListStop")) {
			CO212 co212 = new CO212(decl);
			co212.execute(tc);
		} else if(tc.getMapping().equals("CollWorkListSndVer")) {
			CO213 co213 = new CO213(decl);
			co213.execute(tc);
		} else if(tc.getMapping().equals("CollVerification")) {
			CO214 co214 = new CO214(decl);
			co214.execute(tc);
		} else if(tc.getMapping().equals("CollAuthorization")) {
			CO215 co215 = new CO215(decl);
			co215.execute(tc);
		} else if(tc.getMapping().equals("CollSndArhiva")) {
			CO216 co216 = new CO216(decl);
			co216.execute(tc);
		} else if(tc.getMapping().equals("CollSndBack")) {
			CO217 co217 = new CO217(decl);
			co217.execute(tc);
		} else if(tc.getMapping().equals("CollSndBackAll")) {
			CO218 co218 = new CO218(decl);
			co218.execute(tc);
		} else if(tc.getMapping().equals("KolateralQBEmap")) {
			CO219 co219 = new CO219(decl);
			co219.execute(tc);
		} else if(tc.getMapping().equals("CollCoChgHistMapping")) {
			CO21A co21a = new CO21A(decl);
			co21a.execute(tc);
		} else if(tc.getMapping().equals("ReEstCoChgHistM")) {
			CO21B co21b = new CO21B(decl);
			co21b.execute(tc);
		} else if(tc.getMapping().equals("KolateralReturnDeact")) {
			CO21C co21c = new CO21C(decl);
			co21c.execute(tc);
		} else if(tc.getMapping().equals("CollOwnerBankMapping")) {
			CO21D co21d = new CO21D(decl);
			co21d.execute(tc);
		} else { 
			// basic mapping
			CO211 co211 = new CO211(decl);
			co211.execute(tc);
		}
	}

	public static void main(CommAreaHolder ca) {
		new CO210().run(ca);
	}

}