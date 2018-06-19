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
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RealEstateTypeQuerry extends Handler { 
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RealEstateTypeQuerry.java,v 1.2 2006/05/19 14:15:35 hrajkl Exp $";
	
	public RealEstateTypeQuerry(ResourceAccessor ra) {
		super(ra);
	}

	public void search(){
		//ra.showMessage("wrnclt0");
    	ra.performQueryByExample("tblRealEstateType");
    	ra.exitScreen();
    }
	
}
