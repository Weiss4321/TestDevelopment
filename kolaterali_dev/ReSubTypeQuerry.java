/*
 * Created on 2006.05.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReSubTypeQuerry extends Handler { 
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ReSubTypeQuerry.java,v 1.1 2006/06/14 12:27:33 hraamh Exp $";
	
	public ReSubTypeQuerry(ResourceAccessor ra) {
		super(ra);
	}

	public void search(){
		//ra.showMessage("wrnclt0");
    	ra.performQueryByExample("tblReSubType");
    	ra.exitScreen();
    }
	
}
