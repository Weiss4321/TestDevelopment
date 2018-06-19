package hr.vestigo.modules.collateral.jcics.co32;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO320Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co32/CO320Ejb.java,v 1.3 2017/02/16 15:27:22 hrazst Exp $";

	public CO320Ejb() {
	}

	public CO320Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO32 decl = new DeclCO32();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("AgrListSel".equals(mapping)) {
			CO321 co321 = new CO321(decl);
			co321.execute(tc);
		} else if("AgrListQBE".equals(mapping)) {
			CO322 co322 = new CO322(decl);
			co322.execute(tc);
		} else if("AgrInsert".equals(mapping)) {
			CO323 co323 = new CO323(decl);
			co323.execute(tc);
		} else if("AgrUpdate".equals(mapping)) {
			CO324 co324 = new CO324(decl);
			co324.execute(tc);
		} else if("AgrSelect".equals(mapping)) {
			CO325 co325 = new CO325(decl);
			co325.execute(tc);
		} else if("AgrSelMap".equals(mapping)) {
			CO326 co326 = new CO326(decl);
			co326.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
