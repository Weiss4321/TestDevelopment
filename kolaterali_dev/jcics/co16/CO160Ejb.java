package hr.vestigo.modules.collateral.jcics.co16;

import javax.ejb.SessionContext;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO160Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co16/CO160Ejb.java,v 1.9 2014/12/15 08:39:25 hrazst Exp $";

	public CO160Ejb() {
	}

	public CO160Ejb(SessionContext sessionContext) {
		super (sessionContext);
	}

	public void executeProgram() throws Exception {
		DeclCO16 decl = new DeclCO16();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("LoanBeneficiaryListMapping".equals(mapping)) {
			CO161 co161 = new CO161(decl);
			co161.execute(tc);
		} else if("LoanBeneficiaryInsertMapping".equals(mapping)) {
			CO162 co162 = new CO162(decl);
			co162.execute(tc);
		} else if("LoanBeneficiaryDetailMapping".equals(mapping)) {
			CO163 co163 = new CO163(decl);
			co163.execute(tc);
		} else if("LoanBeneficiaryUpdMapping".equals(mapping)) {
			CO164 co164 = new CO164(decl);
			co164.execute(tc);
		} else if("LoanBeneficiaryDelM".equals(mapping)) {
			CO165 co165 = new CO165(decl);
			co165.execute(tc);
		} else if("LoanBeneficiaryNewListMapp".equals(mapping)) {
			CO166 co166 = new CO166(decl);
			co166.execute(tc);
		} else if("LBenKredAdminMapping".equals(mapping)) {
			CO167 co167 = new CO167(decl);
			co167.execute(tc);
		} else if("LBenFrameAccExceptionCheckM".equals(mapping)) {
			CO168 co168 = new CO168(decl);
			co168.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
