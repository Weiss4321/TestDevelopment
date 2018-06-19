/*
 * Created on 2007.03.20
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
public class VirtualAgreementLayer extends VirtualLayer implements Layer {
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/VirtualAgreementLayer.java,v 1.1 2007/03/26 13:42:29 hraamh Exp $";

	
	/**
	 *
	 * @param id hipoteke 
	 */
	public VirtualAgreementLayer(BigDecimal id) {
		super(null, id);
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
		return "FA_LAYER:"+this.id;
	}
	/**
	 * vraca AbstractLayer.AGREEMENT_LAYER tip sloja
	 * 
	 * @return tip sloja
	 */
	public int getType() {
		return AGREEMENT_LAYER;
	}
	
	public BigDecimal sum() {
		return new BigDecimal(0);
	}

}
