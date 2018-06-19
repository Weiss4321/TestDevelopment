
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;




/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollHfPriorDialogRba extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollHfPriorDialogRba.java,v 1.3 2006/03/15 08:57:58 hrasia Exp $"; 

	
	
	public CollHfPriorDialogRba(ResourceAccessor ra) {
		super(ra);
	}
	public void CollHfPriorDialogRba_SE() {
		if (!(ra.isLDBExists("CollHfPriorDialogRbaLDB"))) {
			ra.createLDB("CollHfPriorDialogRbaLDB");
		}
		
		//CollHfPriorDialogRba_txtHfStatusDesc
		//CollHfPriorDialogRba_txtHfSpecStatDesc

		//CollHfPriorDialogRba_txtHfAvailDatRef = CollHfPriorDialogRba_txtHfAvailDat
		//CollHfPriorDialogRba_txtHfAvailBDatRef = CollHfPriorDialogRba_txtHfAvailBDat
		
		ra.setAttribute("CollHfPriorDialogRbaLDB", "CollHfPriorDialog_txtHfAvailDatRef", (java.sql.Date) ra.getAttribute("CollHfPriorDialogRbaLDB", "CollHfPriorDialog_txtHfAvailDat"));
													
		ra.setAttribute("CollHfPriorDialogRbaLDB", "CollHfPriorDialog_txtHfAvailBDatRef", (java.sql.Date) ra.getAttribute("CollHfPriorDialogRbaLDB", "CollHfPriorDialog_txtHfAvailBDat"));  
			
	}//CollHfPriorDialogRba_SE
	
	
	
	 
	
	
	
}
