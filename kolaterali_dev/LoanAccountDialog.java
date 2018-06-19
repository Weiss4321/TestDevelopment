/*
 * Created on 2006.01.29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoanAccountDialog extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/LoanAccountDialog.java,v 1.2 2006/01/29 10:09:01 hrasia Exp $";	
	public LoanAccountDialog(ResourceAccessor ra) {
		super(ra);
			
	}
	public void LoanAccountDialog_SE() {
		
		if(ra.getScreenContext().compareTo("scr_detail")== 0){
			
			ra.setAttribute("LoanAccountDialogLDB","LoanAccountDialog_txtBalanceDate1", (java.sql.Date)  ra.getAttribute("LoanAccountDialogLDB", "LoanAccountDialog_txtBalanceDate")  );
			ra.setAttribute("LoanAccountDialogLDB","LoanAccountDialog_txtBalanceBDate1", (java.sql.Date)  ra.getAttribute("LoanAccountDialogLDB", "LoanAccountDialog_txtBalanceBDate")  );
		}
	}//LoanAccountDialog_SE

}
