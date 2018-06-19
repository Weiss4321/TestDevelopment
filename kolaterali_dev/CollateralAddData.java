package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;



/**
 * @author Ivan Simunovic
 *
 */



public class CollateralAddData extends Handler{
	
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollateralAddData.java,v 1.1 2005/02/23 09:46:06 hrasia Exp $";
	
	/**
	 * Constructor for CollateralAddData.
	 */
	public CollateralAddData(ResourceAccessor ra) {
		super(ra);
			
	}	

	public void CollateralAddData_SE() {
		
		
			
	}	 

	
	public void open_CollateralAddData_scr () {
		ra.switchScreen("CollateralAddData", "base");			
			
	}	

}
