/*
 * Created on 2006.01.15
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
public class CollHfPriorList extends Handler {

	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollHfPriorList.java,v 1.4 2006/03/15 08:57:53 hrasia Exp $"; 

	
	public CollHfPriorList(ResourceAccessor ra) {
		super(ra);
	}
	
	public void CollHfPriorList_SE() {
		
		
		
		if (!ra.isLDBExists("CollHfPriorListLDB")) {
	 		ra.createLDB("CollHfPriorListLDB");
	 	}
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
			ra.setAttribute("CollHfPriorListLDB","CollHfPriorList_txtColaCusIdUseRegisterNo",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaRegisterNo") );
			ra.setAttribute("CollHfPriorListLDB","CollHfPriorList_txtColaCusIdUseOwnerName",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaOwnerName") );
			ra.setAttribute("CollHfPriorListLDB","CUS_ID",(java.math.BigDecimal)  ra.getAttribute("CollateralDialogLDB", "CUS_ID")  );
			hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollHfPriorList");
	        if (td == null)
	        	//ra.createActionListSession("tblCollHfPriorList", false);;
	        td = null;
		}
		
		
		
	}//CollHfPriorList_SE
	
	public void refresh(){
		//F5
		
		
		ra.showMessage("inf062");
	}//refresh
	public void details(){
		//F4
	}//details
	
	public void selection(){
		//F8
		
		
		
		
		
		ra.showMessage("inf062");
	}//details
	
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
	
}
