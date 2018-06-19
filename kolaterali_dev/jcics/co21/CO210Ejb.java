package hr.vestigo.modules.collateral.jcics.co21;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO210Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co21/CO210Ejb.java,v 1.12 2016/12/05 09:54:29 hrazst Exp $";

	public CO210Ejb() {
	}

	public CO210Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO21 decl = new DeclCO21();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("ColWorkListSel".equals(mapping)) {
			CO211 co211 = new CO211(decl);
			co211.execute(tc);
		} else if("CollWorkListStop".equals(mapping)) {
			CO212 co212 = new CO212(decl);
			co212.execute(tc);
		} else if("CollWorkListSndVer".equals(mapping)) {
			CO213 co213 = new CO213(decl);
			co213.execute(tc);
		} else if("CollVerification".equals(mapping)) {
			CO214 co214 = new CO214(decl);
			co214.execute(tc);
		} else if("CollAuthorization".equals(mapping)) {
			CO215 co215 = new CO215(decl);
			co215.execute(tc);
		} else if("CollSndArhiva".equals(mapping)) {
			CO216 co216 = new CO216(decl);
			co216.execute(tc);
		} else if("CollSndBack".equals(mapping)) {
			CO217 co217 = new CO217(decl);
			co217.execute(tc);
		} else if("CollSndBackAll".equals(mapping)) {
			CO218 co218 = new CO218(decl);
			co218.execute(tc);
		} else if("KolateralQBEmap".equals(mapping)) {
			CO219 co219 = new CO219(decl);
			co219.execute(tc);
		} else if("CollCoChgHistMapping".equals(mapping)) {
			CO21A co21a = new CO21A(decl);
			co21a.execute(tc);
		} else if("ReEstCoChgHistM".equals(mapping)) {
			CO21B co21b = new CO21B(decl);
			co21b.execute(tc);
		} else if("KolateralReturnDeact".equals(mapping)) {
			CO21C co21c = new CO21C(decl);
			co21c.execute(tc);
		} else if("CollOwnerBankMapping".equals(mapping)) {
			CO21D co21d = new CO21D(decl);
			co21d.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
