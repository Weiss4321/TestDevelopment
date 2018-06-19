/*
 * Created on 2007.02.08
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;


/**
 * Model izracuna kada je u pitanju samo jedan kolateral
 * 
 * @author hraamh
 */
public class Model1Coll implements PlacementCoverage {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/Model1Coll.java,v 1.7 2007/03/26 13:42:29 hraamh Exp $";
	
	private PlacementLayer placements=null;
	private CollateralVector[] collaterals=null;
	
	
	private boolean debug=true;
	
	public Model1Coll(PlacementLayer placements,CollateralVector[] collaterals,boolean debugOut){
		this.placements=placements;	
		this.debug=debugOut;
		this.collaterals=collaterals;
		if (debug) System.out.println("\t\t\tModel 1");
	}
	/**
	 * izracun pokrivenosti plasmana kroz kolateral vektore
	 * 
	 * @return kolateral vektori
	 */
	public CollateralVector[] run() {
		if (debug) System.out.println("P:\t\t\t"+placements);
		collaterals[0].operate(placements);
		if (debug) System.out.println(collaterals[0]);
		if (debug) System.out.println("-------------------------------------------------------------------------------------------------------------");
		if (debug) System.out.println("P:\t\t\t"+placements+"!");
		if (debug) System.out.print("R(K):\t\t\t"+collaterals[0].getResidue());
		return collaterals;
	}
	
	/**
	 * ne radi nista
	 */
	public CollateralVector[] sort(CollateralVector[] vectors) {
		return vectors;
	}
	
	/**
	 * vraca kolateral vektore koji su rezultat proracun u run() metodi
	 * 
	 * @return collateral vektori
	 */
	public CollateralVector[] getCollateralVectors() {
		return this.collaterals;
	}
	
	public String getModelName() {		
		return this.getClass().getName();
	}

}
