package hr.vestigo.modules.collateral.jcics.co16;

import hr.vestigo.framework.remote.transaction.local.LocalProgramRunner;
import hr.vestigo.framework.remote.transaction.TransactionContext;

public class LocalCO160 extends LocalProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co16/LocalCO160.java,v 1.6 2008/11/06 09:08:28 hramkr Exp $";

	public LocalCO160() {
	}

	public void executeProgram() throws Exception {
		DeclCO16 decl = new DeclCO16();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		if(tc.getMapping().equals("LoanBeneficiaryInsertMapping")) {
			CO162 co162 = new CO162(decl);
			co162.execute(tc);
		} else if(tc.getMapping().equals("LoanBeneficiaryDetailMapping")) {
			CO163 co163 = new CO163(decl);
			co163.execute(tc);
		} else if(tc.getMapping().equals("LoanBeneficiaryUpdMapping")) {
			CO164 co164 = new CO164(decl);
			co164.execute(tc);
		} else if(tc.getMapping().equals("LoanBeneficiaryDelM")) {
			CO165 co165 = new CO165(decl);
			co165.execute(tc);
		} else if(tc.getMapping().equals("LoanBeneficiaryNewListMapp")) {
			CO166 co166 = new CO166(decl);
			co166.execute(tc);
		} else if(tc.getMapping().equals("LBenKredAdminMapping")) {
			CO167 co167 = new CO167(decl);
			co167.execute(tc);
		} else { 
			// basic mapping
			CO161 co161 = new CO161(decl);
			co161.execute(tc);
		}
	}

}