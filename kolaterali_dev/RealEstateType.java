/*
 * Created on 2006.05.15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
//import hr.vestigo.framework.common.TableData;


/**
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class RealEstateType extends Handler {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RealEstateType.java,v 1.8 2006/06/06 08:40:56 hraamh Exp $";
	
	public RealEstateType(ResourceAccessor ra) {
		super(ra);
	}
	 
	public void RealEstateType_SE(){ //prvo sto se poziva nad ekranom
		
			ra.createLDB("RealEstateTypeLDB");
			ra.createActionListSession("tblRealEstateType");
	
	} 
	
	public void add(){
		ra.loadScreen("RealEstateTDialog","scr_insert"); 
	}
	
	public void re_sub_type(){
	    
		if(isTableEmpty("tblRealEstateType")){//ako se pozovu detalji nad praznom tablicom--> puca
			ra.showMessage("wrn299");
			return;
		}
		System.out.println("Test");
		ra.loadScreen("ReSubType");	    
	}

	public void details(){
		
		if(isTableEmpty("tblRealEstateType")){//ako se pozovu detalji nad praznom tablicom--> puca
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("RealEstateTDialog","scr_detail");
	}
	public void action(){
		ra.loadScreen("RealEstateTDialog","scr_update");
	}
	public void refresh(){
		ra.refreshActionList("tblRealEstateType");
	}
	
	public void query_by_example(){
		ra.loadScreen("RealEstateTypeQuerry","scr_querry");
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
