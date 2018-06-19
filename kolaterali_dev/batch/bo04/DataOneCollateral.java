/*
 * Created on 2006.12.13
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo04;

import java.math.BigDecimal;

/**
 * @author hrazst
 *
 * Objekt za podatke koji trebaju za obradu jednog collaterala u funkciji processOneRecord iz BO040
 */
public class DataOneCollateral {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo04/DataOneCollateral.java,v 1.3 2007/05/10 13:50:19 hrazst Exp $";
	
	/**
	 * Collateral id
	 */	
	public BigDecimal colHeaId=null;
	
	/**
	 * Jedinstveni identifikacijski broj collaterala
	 */
	public String colNum=null;
	
	/**
	 * Nominalna vrijednost collaterala kuju je unio collateral officer
	 */	
	public BigDecimal realEstNomiValu=null;
	
	/**
	 * Id valute vrijednosti collaterala
	 */	
	public BigDecimal realEstNmCurId=null;
	
	/**
	 * Id referenta koji je unio collateral
	 */	
	public BigDecimal useOpenId=null;  
	
	/**
	 * Id organizacijske jedinice referenta
	 */	
	public BigDecimal originOrgUniId=null;
	
	/**
	 * Id tipa collaterala
	 */	
	public BigDecimal colCatId=null;
	
	public DataOneCollateral() {
		super();
	}
}
