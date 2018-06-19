package hr.vestigo.modules.collateral.common.interfaces;

import java.math.BigDecimal;

import hr.vestigo.modules.rba.interfaces.AbstractCommonInterface;

public interface CollateralPosting extends AbstractCommonInterface{
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/interfaces/CollateralPosting.java,v 1.1 2007/12/11 12:19:21 hraamh Exp $";
	
	/**
	 * Metoda koja otvara partiju i knjizi kolateral 
	 * @param colHeaId - collateral id 
	 * @param deactivateColl - ako se collateral gasi(isknjizuje totalno) postaviti na true, inace postaviti na false 
	 */	
	public void CollPosting(BigDecimal colHeaId, boolean deactivateColl) throws Exception;
}