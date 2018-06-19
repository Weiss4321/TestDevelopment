package hr.vestigo.modules.collateral.jcics.co04;

import hr.vestigo.framework.remote.transaction.VestigoTMException;
import hr.vestigo.framework.remote.transaction.TransactionContext;
import hr.vestigo.framework.remote.transaction.ejb.EjbTransactionException;
import hr.vestigo.framework.remote.transaction.ejb.EjbProgramRunner;

public class CO040Ejb extends EjbProgramRunner {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co04/CO040Ejb.java,v 1.11 2006/05/23 07:49:45 hramkr Exp $";

	public CO040Ejb() {
	}

	public void executeProgram() throws Exception {
		DeclCO04 decl = new DeclCO04();
		setDecl(decl);

		TransactionContext tc = getTransactionContext();

		String mapping = tc.getMapping();
		if("CollSecPaperListMapping".equals(mapping)) {
			CO041 co041 = new CO041(decl);
			co041.execute(tc);
		} else if("CollMovableListMapping".equals(mapping)) {
			CO042 co042 = new CO042(decl);
			co042.execute(tc);
		} else if("CollSupplyListMapping".equals(mapping)) {
			CO043 co043 = new CO043(decl);
			co043.execute(tc);
		} else if("CollArtListMapping".equals(mapping)) {
			CO044 co044 = new CO044(decl);
			co044.execute(tc);
		} else if("CollPrecListMapping".equals(mapping)) {
			CO045 co045 = new CO045(decl);
			co045.execute(tc);
		} else if("VesselListMapping".equals(mapping)) {
			CO046 co046 = new CO046(decl);
			co046.execute(tc);
		} else if("CashDepListMapping".equals(mapping)) {
			CO047 co047 = new CO047(decl);
			co047.execute(tc);
		} else if("CollInsPolListMapping".equals(mapping)) {
			CO048 co048 = new CO048(decl);
			co048.execute(tc);
		} else if("CollGuarantListMapping".equals(mapping)) {
			CO049 co049 = new CO049(decl);
			co049.execute(tc);
		} else if("CollLoanStockListMap".equals(mapping)) {
			CO04A co04a = new CO04A(decl);
			co04a.execute(tc);
		} else {
			throw new VestigoTMException(-20, "Invalid mapping! Mapping=" + mapping, null, null);
		}
	}
}
