/*
 * Created on 2007.01.24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

/**
 * Model izracuna pokrivenosti plasmana kolateralima.
 * 
 * @author hraamh
 *
 */
public interface PlacementCoverage {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/PlacementCoverage.java,v 1.6 2007/08/24 07:48:48 hraamh Exp $";
	/**
	 * izracun pokrivenosti plasmana kroz kolateral vektore
	 * 
	 * @return kolateral vektori
	 */
	public CollateralVector[] run();
	/**
	 * sortiranje kolaterala po likvidnosti, datumu ovjere CO te vremenu unosa 
	 * 
	 * @param vectors kolateral vektori
	 * @return kolateral vektori
	 */
	public CollateralVector[] sort(CollateralVector[] vectors);
	/**
	 * dohvat kolateral vektora
	 * @return kolateral vektori
	 */
	public CollateralVector[] getCollateralVectors();
	/**
	 * dohvat naziva modela
	 * @return naziv modela
	 */
	public String getModelName();

}
