package hr.vestigo.modules.collateral;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;

public class CollCourt extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollCourt.java,v 1.6 2006/07/11 14:28:45 hraaks Exp $";

	public CollCourt(ResourceAccessor ra) {
		super(ra);
	}

	public void CollCourt_SE() {

		if (!ra.isLDBExists("CollCourtLDB")) {
			ra.createLDB("CollCourtLDB");
		}
		
		
		TableData td = (TableData) ra.getAttribute("tblCollCourt");
		if (td == null)
			ra.createActionListSession("tblCollCourt", true);
		td = null;
		

	}//CollCourt_SE

	public void refresh() {
		
		ra.refreshActionList("tblCollCourt");
		
	}

	public void add() {
		ra.loadScreen("CollCourtDialog","scr_insert");
	}
	public void action(){
		ra.loadScreen("CollCourtDialog","scr_update");
	}

	public void query_by_example() {
		ra.loadScreen("CollCourtQuerry","scr_querry");
	}

	public void details() {
		if (isTableEmpty("tblCollCourt")) {//ako se pozovu detalji nad praznom
										   // tablicom--> puca
			ra.showMessage("wrn299");
			return;
		}
		else
		{
			ra.loadScreen("CollCourtDialog","scr_detail");
		}
	}
	 
	public void search(){
	
		ra.performQueryByExample("tblCollCourt");
    	ra.exitScreen();
	}

	public boolean isTableEmpty(String tableName) {
		TableData td = (TableData) ra.getAttribute(tableName);
		if (null == td)
			return true;
		if (0 == td.getData().size())
			return true;
		return false;
	}

}