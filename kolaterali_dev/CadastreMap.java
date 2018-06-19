/*
 * Created on 2006.10.16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CadastreMap extends Handler {
    public static String cvsident = "$Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CadastreMap.java,v 1.1 2006/11/08 12:37:50 hrazst Exp $";
    
    public CadastreMap (ResourceAccessor ra){
    	super(ra);
    }  

    public void CadastreMap_SE(){    	
       	ra.createLDB("CadastreMapLDB");
        ra.createActionListSession("tblCadastreMap");
                
    }
        
    public void details() {
    	if (isTableEmpty("tblCadastreMap")) {
    		ra.showMessage("wrn299");
    		return;
    	}
    	
    	ra.loadScreen("CadastreMapDialog", "scr_detail");
    }
    
    public void action() {
    	if (isTableEmpty("tblCadastreMap")) {
    		ra.showMessage("wrn299");
    		return;
    	}
    	
		TableData td = (TableData) ra.getAttribute("CadastreMapLDB", "tblCadastreMap");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        Vector row = td.getSelectedRowData();
    	
        String code=(String) row.elementAt(0);   
        if (code.charAt(0)!='X'){
        	ra.showMessage("infcltzst3");
    		return;
        }
		
    	ra.loadScreen("CadastreMapDialog", "scr_action");
    }
    
    public void add() {
    	ra.loadScreen("CadastreMapDialog", "scr_insert");
    }
    
    public void refresh() {
        ra.refreshActionList("tblCadastreMap");
    }
   
    public void query_by_example(){
	    ra.loadScreen("CadastreMapQBE");
    }
    
    public boolean isTableEmpty(String tableName) {
        TableData td = (TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty

}
