/*
 * Created on 2006.05.03
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RevRegCoefRe extends Handler {
    public static String cvsident = "$Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RevRegCoefRe.java,v 1.5 2006/05/24 12:00:22 hrazst Exp $";
    
    public RevRegCoefRe (ResourceAccessor ra){
    	super(ra);
    }  
  
    public void RevRegCoefRe_SE(){    	
   	
            ra.createLDB("RevRegCoefReLDB");
            ra.createActionListSession("tblRevRegCoefRe");
            
    }
    
    public void details() {
    	if (isTableEmpty("tblRevRegCoefRe")) {
    		ra.showMessage("wrn299");
    		return;
    	}
    	
    	ra.loadScreen("RevRegCoefReDialog", "scr_detail");
    }
    
    public void action() {
    	if (isTableEmpty("tblRevRegCoefRe")) {
    		ra.showMessage("wrn299");
    		return;
    	}
    	
    	ra.loadScreen("RevRegCoefReDialog", "scr_action");
    }
    
    public void add() {
    	ra.loadScreen("RevRegCoefReDialog", "scr_insert");
    }
    
    public void refresh() {
        ra.refreshActionList("tblRevRegCoefRe");
    }
   
    public void query_by_example(){
	    ra.loadScreen("RevRegCoefReQuerry");
    }
    
    public boolean isTableEmpty(String tableName) {
        hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
}
