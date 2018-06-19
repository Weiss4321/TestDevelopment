/*
 * Created on 2007.09.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.bt.bt0069;

import hr.vestigo.framework.mcserver.BTContext;
import hr.vestigo.framework.mcserver.BTransaction;

/**
 * @author hraamh
 *
 * BT transakcija za Loxon collateral - plovila
 */
public class BT0069 extends BTransaction {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/bt/bt0069/BT0069.java,v 1.1 2008/05/19 09:17:28 hraamh Exp $";

	/**
	 * @param ctx
	 */
	public BT0069(BTContext ctx) {
		super(ctx);
	}

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.mcserver.BTransaction#execute()
	 */
	public void execute() throws Exception {
		 btc.executeTransaction("LoxonBTVesselByNaturalKey");
	}

}
