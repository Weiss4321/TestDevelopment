/*
 * Created on 2006.12.20
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
public class VirtualLayer extends AbstractLayer implements Layer {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/VirtualLayer.java,v 1.5 2007/08/24 07:48:48 hraamh Exp $";

	protected BigDecimal value=null; 
	
	/**
	 * @param count
	 */
	public VirtualLayer(BigDecimal value, BigDecimal id) {
		super(id);
		this.value=value;
		this.count=-1;
	}

	/**
	 * placebo funkcija. ne radi nista
	 */
	public boolean setFlags(boolean[] values) {		
		return true;
	}
	/**
	 * vraca AbstractLayer.OTHER_TYPE tip sloja
	 * 
	 * @return tip sloja
	 */
	public int getType() {
		return OTHER_LAYER;
	}

	/**
	 * vraca vrijednost hipoteke druge banke
	 * 
	 * @return vrijednost virtualnog sloja (hipoteke druge banke)
	 */
	public BigDecimal getValue() {
		return value;
	}
	/**
	 * sumira sve vrijednosti sloja. U ovom slucaju to ve vrijednost sloja
	 * 
	 * @return vrijednost virtualnog sloja (hipoteke druge banke)
	 */
	public BigDecimal sum() {
		return value;
	}
	
	public String toString(){
		return "OTHER_VAL:"+this.value;
	}

}
