/*
 * Created on 2007.11.26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.interfaces;

import hr.vestigo.modules.rba.interfaces.AbstractCommonInterface;

import java.util.Vector;

/**
 * 
 * Interface za dohvat instrumenata osiguranja
 * 
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CollateralInsuranceInstrument extends AbstractCommonInterface{
	
	public static String cvsident =  "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/interfaces/CollateralInsuranceInstrument.java,v 1.4 2007/12/11 12:33:43 hraamh Exp $";
	
	
	/**
	 * Dohvat svih instrumenata osiguranja. instrumenti su hr.vestigo.modules.collateral.common.data.CollateralInsuranceInstrumentData
	 * 
	 * @param cus_acc_no partija plasmana
	 * @return vektor CollateralInsuranceInstrumentData objekata
	 */
	public Vector getCollaterals(String cus_acc_no) throws Exception;

}
