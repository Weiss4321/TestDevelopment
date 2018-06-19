/*
 * Created on 2006.05.15
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
//import hr.vestigo.framework.common.TableData;


/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ReSubType extends Handler {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ReSubType.java,v 1.3 2006/06/07 14:05:55 hraamh Exp $";
	
	public ReSubType(ResourceAccessor ra) {
		super(ra);
	}
	
	public void ReSubType_SE(){ //prvo sto se poziva nad ekranom
		
			ra.createLDB("ReSubTypeLDB");
			
			TableData td = (TableData) ra.getAttribute("RealEstateTypeLDB","tblRealEstateType");
			Vector data = td.getSelectedRowData();
			Vector hidden = (Vector)td.getSelectedRowUnique();
			
			ra.setAttribute("ReSubTypeLDB","ReSubType_txtRealEstateTypeCode",data.elementAt(0)+"");
			ra.setAttribute("ReSubTypeLDB","ReSubType_txtRealEstateTypeName",data.elementAt(1)+"");
			ra.setAttribute("ReSubTypeLDB","RE_TYPE_ID",(BigDecimal)hidden.elementAt(2));
			
			ra.createActionListSession("tblReSubType");
	
	} 
	
	public void add(){
		ra.loadScreen("ReSubTypeDialog","scr_add"); 
	}
	
	
	public void details(){
		
		if(isTableEmpty("tblReSubType")){//ako se pozovu detalji nad praznom tablicom--> puca
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("ReSubTypeDialog","scr_details");
	}
	public void action(){
		ra.loadScreen("ReSubTypeDialog","scr_action");
	}
	public void refresh(){
		ra.refreshActionList("tblReSubType");
	}
	
	public void query_by_example(){
		ra.loadScreen("ReSubTypeQuerry","scr_querry");
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
