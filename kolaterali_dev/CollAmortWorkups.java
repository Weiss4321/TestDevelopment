/*
 * Created on 2007.05.28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollAmortWorkups extends Handler {

		    
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollAmortWorkups.java,v 1.5 2007/06/15 07:47:27 hratar Exp $";
		    
	public CollAmortWorkups(ResourceAccessor ra) {
		 super(ra);
	}
		    
	public void CollAmortWorkupsDetails_SE(){
		if (!ra.isLDBExists("CollAmortWorkupsLDB")) {
			 ra.createLDB("CollAmortWorkupsLDB");
		}	    	
		
		ra.createActionListSession("tblCollAmortWorkupsDetails");       	   	
		    	 
	}  
		    
	public void CollAmortWorkups_SE(){
		 if (!ra.isLDBExists("CollAmortWorkupsLDB")) {
			 ra.createLDB("CollAmortWorkupsLDB");
		}
		ra.createActionListSession("tblCollAmortWorkups"); 	   

		       		
	}
		    	
		    	   
	public void details() {
		    	
		TableData td=null;

		if (isTableEmpty("tblCollAmortWorkups")) {//ako se pozovu detalji nad praznom tablicom--> puca
			ra.showMessage("wrn299");
			return;
		}
		td = (TableData) ra.getAttribute("CollAmortWorkupsLDB", "tblCollAmortWorkups");
				
		Vector hidden = (Vector) td.getSelectedRowUnique();
		String col_pro_id = null;
		col_pro_id = (String) hidden.elementAt(0);
		ra.setAttribute("CollAmortWorkupsLDB", "COL_PRO_ID", col_pro_id);
		ra.loadScreen("CollAmortWorkupsDetails", "scr_details");
	} 
		      
		      
	public boolean isTableEmpty(String tableName) {
		TableData td = (TableData) ra.getAttribute(tableName);
		if (td == null)
			return true;
		if (td.getData().size() == 0)
			return true;
		
		return false;
	}   

	public void exit() {  
			 	
		ra.exitScreen();

		ra.refreshActionList("tblCollAmortWorkups");
		 	
				
		TableData td = (TableData) ra.getAttribute("CollAmortWorkupsLDB", "tblCollAmortWorkupsDetails");
			 	
		ra.setAttribute("CollAmortWorkupsLDB","tblCollAmortWorkupsDetails",null);
			 	
	}


}
