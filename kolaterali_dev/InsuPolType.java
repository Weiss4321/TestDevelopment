/**
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.common.TableData;



/**
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class InsuPolType extends Handler {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsuPolType.java,v 1.3 2006/06/02 14:20:15 hrajkl Exp $";
	
	public InsuPolType(ResourceAccessor ra) {
		super(ra);
	}
	
	public void InsuPolType_SE(){ //prvo sto se poziva nad ekranom
			ra.createLDB("InsuPolicyTypeLDB");
			ra.createActionListSession("tblInsuPolicyType");
	} 
	
	public void add(){//F7 dodaj
		ra.loadScreen("InsuPolTypeDialog","scr_insert"); 
	}
	public void details(){//F4 detalji
		if(isTableEmpty("tblInsuPolicyType")){//ako se pozovu detalji nad praznom tablicom--> puca
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("InsuPolTypeDialog","scr_detail");
	}
	public void action(){//F8 promjena
		if(isTableEmpty("tblInsuPolicyType")){
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("InsuPolTypeDialog","scr_update");
	}
	public void refresh(){//F5/2 osvjezi
		ra.refreshActionList("tblInsuPolicyType");
	}
	public void query_by_example(){//F5 upit po uzorku
		ra.loadScreen("InsuPolTypeQuerry","scr_querry");
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
