/*
 * Created on 2006.05.30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;


public class InsuPolTypeQuerry extends Handler { 
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsuPolTypeQuerry.java,v 1.2 2006/06/02 14:20:15 hrajkl Exp $";
	
	public InsuPolTypeQuerry(ResourceAccessor ra) {
		super(ra);
	}

	public void search(){
		//ra.showMessage("wrnclt0");
    	ra.performQueryByExample("tblInsuPolicyType");
    	ra.exitScreen();
    }
	
}
