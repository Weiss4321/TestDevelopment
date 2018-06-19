/*
 * Created on 2006.06.27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 * @author hraaks
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollCourtQuerry extends Handler {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollCourtQuerry.java,v 1.4 2006/07/11 14:28:45 hraaks Exp $";
	
	public CollCourtQuerry(ResourceAccessor res) {
		super(res);
	}
	public void CollCourtQuerry_SE(){
		
		if (!ra.isLDBExists("CollCourtQuerryLDB")) {
			ra.createLDB("CollCourtQuerryLDB");
		}
		
	}
	
	

}
