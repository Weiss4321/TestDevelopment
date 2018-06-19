/*
 * Created on 2006.12.13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CollElement {
	
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/CollElement.java,v 1.4 2007/02/01 15:56:30 hraamh Exp $";
	/**
	 * Vraca id primarni kljuca elementa
	 * @return id 
	 */
	public BigDecimal getId();
	/**
	 * Vraca vrijednost elementa
	 * @return vrijednost elementa
	 */
	public BigDecimal getValue();
	/**Postavlja vrijednost elementa
	 * 
	 * @param value vrijednost elementa
	 */
	public void setValue(BigDecimal value);
	/**
	 * Vraca tip elementa: Collateral, Placement ili Mortgage
	 * 
	 * @return tip elementa
	 */
	public int getDataType();
	/**
	 * Vraca unique sifru elementa
	 * 
	 * @return sifra elementa
	 */
	public String getCode();
	

}
