package hr.vestigo.modules.collateral;


import java.math.BigDecimal;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.common.VestigoTableException;
import hr.vestigo.framework.controller.actionlist.VestigoActionListException;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;


/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollRevalWorkups extends Handler {
	    
	    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollRevalWorkups.java,v 1.9 2007/06/21 12:37:10 hratar Exp $";
	    
	    
	    
	    public CollRevalWorkups(ResourceAccessor ra) {
	        super(ra);
	    }
	    
	    
	    
	    
	    public void CollRevalWorkupsDetails_SE(){
	        if (!ra.isLDBExists("CollRevalWorkupsLDB")) {
		 		ra.createLDB("CollRevalWorkupsLDB");
		 	}	    	
	        
	        	
	        	ra.createActionListSession("tblCollRevalWorkupsDetails");       	   	
	    	
	    }  
	    
	    public void CollWorkups_SE() {
	    	if (!ra.isLDBExists("ColWorkListLDB")) {
		 		ra.createLDB("CollWorkListLDB");
		 	}	    	
	        
	        	
	        	ra.createActionListSession("tblCollWorkups");       	   	
	    	
	    }
	    
	    public void CollRevalWorkups_SE(){
	        if (!ra.isLDBExists("CollRevalWorkupsLDB")) {
		 		ra.createLDB("CollRevalWorkupsLDB");
		 	}
	     
	        
	        
	        ra.createActionListSession("tblCollRevalWorkups"); 	  
	        
	        
//	 ulazim u pregled iz detalja kolaterala
	        
/*	        if (ra.getScreenContext().compareTo("scr_details") == 0) {
	                    ra.createActionListSession("tblCollRevalWorkups");

	        } else if (ra.getScreenContext().compareTo("scr_base") == 0) {
//	 ulazim u pregled sa izbornika 
	        	ra.setScreenTitle("Pregled izvrsenih revalorizacija");
	        	ra.createActionListSession("tblCollRevalWorkups");       	
	        } else {
	           	ra.setScreenTitle("Pregled izvrsenih revalorizacija");
	        	ra.createActionListSession("tblCollRevalWorkups");       	       	
	        }
	        */
	        
	
	    }
	    	
	    	   
	    public void details() {
	    	
	    	TableData td=null;

			if (isTableEmpty("tblCollRevalWorkups")) {//ako se pozovu detalji nad praznom tablicom--> puca
				ra.showMessage("wrn299");
				return;
			}
			td = (TableData) ra.getAttribute("CollRevalWorkupsLDB", "tblCollRevalWorkups");
			
			Vector hidden = (Vector) td.getSelectedRowUnique();
			String col_pro_id = null;
			col_pro_id = (String) hidden.elementAt(0);
			ra.setAttribute("CollRevalWorkupsLDB", "COL_PRO_ID", col_pro_id);
			
			ra.loadScreen("CollRevalWorkupsDetails", "scr_details");
	   

			
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
		 
		 	if (ra.getScreenID().equals("CollRevalWorkupsDetails"))
		 	{
		 		ra.exitScreen();

		 		ra.refreshActionList("tblCollRevalWorkups");
	 	
		 		//TableData td = (TableData) ra.getAttribute("CollRevalWorkupsLDB", "tblCollRevalWorkupsDetails");
		 	
		 		ra.setAttribute("CollRevalWorkupsLDB","tblCollRevalWorkupsDetails",null);
		 	}
		 	else 
		 		if (ra.getScreenID().equals("CollWorkups")) {
		 		
		 		ra.setAttribute("ColWorkListLDB","tblCollWorkups",null);
		 		
		 		ra.setAttribute("ColWorkListLDB","CollWorkups_dynLblCollateral",null);
		 		
		 		ra.exitScreen();
		 	}
		 }

}