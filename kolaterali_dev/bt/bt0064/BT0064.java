/*
 * Created on 2007.09.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.bt.bt0064;

import hr.vestigo.framework.mcserver.BTContext;
import hr.vestigo.framework.mcserver.BTransaction;

/**
 * @author hraamh
 *
 * BT transakcija za Loxon collaterala preko liste komitenata
 */
public class BT0064 extends BTransaction {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/bt/bt0064/BT0064.java,v 1.1 2008/05/19 09:17:29 hraamh Exp $";

	/**
	 * @param ctx
	 */
	public BT0064(BTContext ctx) {
		super(ctx);
	}

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.mcserver.BTransaction#execute()
	 */
	public void execute() throws Exception {
		 btc.executeTransaction("LoxonBTByCustomerIDList");
	}

}
