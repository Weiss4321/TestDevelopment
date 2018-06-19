/*
 * Created on 2006.12.20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollateralLayer extends AbstractLayer implements Layer {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/CollateralLayer.java,v 1.4 2007/02/14 14:40:09 hraamh Exp $";

	
	public CollateralLayer(int count,BigDecimal id){
		super(count,id);
	}
	/**postavlja polje flagova ako je istih dimenzija kao postojece polje. ako nije nista ne radi
	 * 
	 * @param values polje flagova
	 * @return true ako je istih dimenzija kao postojece polje; false u suprotnom
	 */
	public boolean setFlags(boolean[] values) {
		if(values.length==this.count){
			this.flags=values;
			return true;
		}
		return false;
	}
	/**
	 * vraca tip sloja = RBA_TYPE
	 * @return tip sloja
	 */
	public int getType() {
		return RBA_LAYER;
	}
	/**
	 * vraca vrijednost sloja. U ovom slucaju sumira sve vrijednosti elemenata sloja
	 * @return vrijednost sloja
	 */
	public BigDecimal getValue() {
		return sum();
	}
	
	public String toString(){
		String s="";
		for(int i=0;i<this.count;i++){
			if(isActive(i)){
				s+=DecimalUtils.scale(this.values[i],DecimalUtils.LOW_PRECISION)+"\t\t";
			}else{
				s+="X \t\t";
			}
		}
		return s;
	}

}
