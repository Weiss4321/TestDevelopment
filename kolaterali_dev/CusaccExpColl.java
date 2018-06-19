 
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 * @author
 *
 */ 
public class CusaccExpColl extends Handler {
    
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CusaccExpColl.java,v 1.8 2007/08/29 13:26:52 hramkr Exp $";
    
    public CusaccExpColl(ResourceAccessor ra) {
        super(ra);
    }
    
    public void CusaccExpCollFOI_SE(){
    	ra.createActionListSession("tblCusaccExpCollFOI");       	   	
    	 
    }  
    
    public void CusaccExpColl_SE(){
        if (!ra.isLDBExists("CusaccExpCollLDB")) {
	 		ra.createLDB("CusaccExpCollLDB");
	 	}
   
// ulazim u pregled iz detalja kolaterala
        
        if (ra.getScreenContext().compareTo("scr_detail") == 0) {
            if(ra.isLDBExists("CusaccExpCollCallerLDB")){
                if (ra.isAttributeExists("CusaccExpCollCallerLDB","COL_HEA_ID")){
                    ra.setAttribute("CusaccExpCollLDB", "COL_HEA_ID", ra.getAttribute("CusaccExpCollCallerLDB","COL_HEA_ID"));
                    ra.createActionListSession("tblCusaccExpColl");

 /*                   ra.createActionListSession("tblCusaccExpColl", false);

                     ra.performQueryByExample("tblCusaccExpColl");	*/	
                 }
             }        	
        } else if (ra.getScreenContext().compareTo("scr_base") == 0) {
// ulazim u pregled sa izbornika 
        	ra.setScreenTitle("Pregled pokrivenosti");
        	ra.createActionListSession("tblCusaccExpColl", false);       	
        } else {
           	ra.setScreenTitle("Pregled pokrivenosti");
        	ra.createActionListSession("tblCusaccExpColl", false);       	       	
        }
        
        
 /*       if(ra.isLDBExists("CusaccExpCollCallerLDB")){
           if (ra.isAttributeExists("CusaccExpCollCallerLDB","COL_HEA_ID")){
                ra.createActionListSession("tblCusaccExpColl", false);
                ra.setAttribute("CusaccExpCollLDB", "COL_HEA_ID", ra.getAttribute("CusaccExpCollCallerLDB","COL_HEA_ID"));
                ra.performQueryByExample("tblCusaccExpColl");		
            }
        }  
        else{
            ra.createActionListSession("tblCusaccExpColl");
        }*/
    }
    public void ColWorkListCov_SE(){
        if (!ra.isLDBExists("CusaccExpCollLDB")) {
	 		ra.createLDB("CusaccExpCollLDB");
	 	}
        
        ra.setAttribute("CusaccExpCollLDB", "COL_HEA_ID",ra.getAttribute("ColWorkListLDB", "col_hea_id"));
    	ra.createActionListSession("tblColWorkListCov");       
    } 
    	
    	   
    public void query_by_example() {
        ra.loadScreen("CusaccExpCollQBE", "scr_qbe");
    }
    public void details() {
    	
    	TableData td=null;

		if (isTableEmpty("tblCusaccExpColl")) {//ako se pozovu detalji nad praznom tablicom--> puca
			ra.showMessage("wrn299");
			return;
		}
		td = (TableData) ra.getAttribute("CusaccExpCollLDB", "tblCusaccExpColl");
		
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal cus_acc_id = null;
		cus_acc_id = (BigDecimal) hidden.elementAt(0);
		java.sql.Date value_date;
		value_date = (java.sql.Date) hidden.elementAt(1);
		BigDecimal col_pro_id = null;
		col_pro_id = (BigDecimal) hidden.elementAt(2);
		ra.setAttribute("CusaccExpCollLDB", "CUS_ACC_ID", cus_acc_id);
		ra.setAttribute("CusaccExpCollLDB", "VALUE_DATE", value_date);
		ra.setAttribute("CusaccExpCollLDB","COL_PRO_ID", col_pro_id);
    	ra.loadScreen("CusaccExpCollFOI", "src_foi");
    } 
      
      
	public boolean isTableEmpty(String tableName) {
		TableData td = (TableData) ra.getAttribute(tableName);
		if (td == null)
			return true;
		if (td.getData().size() == 0)
			return true;
		return false;
	}   
	
	
	
}
