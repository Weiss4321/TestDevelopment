/*
 * Created on 2006.06.07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;
import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import java.math.BigDecimal;
import java.util.Vector;
/**
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColTypeList extends Handler {

	public static String cvsident =
		"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ColTypeList.java,v 1.5 2006/08/16 14:16:23 hrasia Exp $";
	/** Creates a new instance of MarketInstruments */
	public ColTypeList(ResourceAccessor ra) {
		super(ra);
	}
 
	public void ColTypeList_SE() {
		try {
			ra.createLDB("ColTypeListLDB");
			ra.executeTransaction();
		} catch (VestigoTMException vtme) {
			error("ColTypeList -> ColTypeList_SE(): VestigoTMException", vtme);
			if (vtme.getMessageID() != null)
				ra.showMessage(vtme.getMessageID());
		} 
	}

	public void selection() {
		String ind = "base";  
		String ekran=null;
		BigDecimal col_cat_id = null;
		String code=null;
		Vector hidden=null;
		Vector row = null;
		if (isTableEmpty("tblColTypeList")) {
			ra.showMessage("wrn299");
			return;
		}
		TableData td = (TableData) ra.getAttribute("ColTypeListLDB", "tblColTypeList");
		hidden = (Vector) td.getSelectedRowUnique();
		col_cat_id = (BigDecimal) hidden.elementAt(0);		
		ekran = (String) hidden.elementAt(1);
		
		row = (Vector) td.getSelectedRowData();
		code = (String) row.elementAt(0);
		
		//postavlja defaultne podatke u LDB
		ra.setAttribute("ColWorkListLDB", "col_cat_id", col_cat_id);		
		ra.setAttribute("ColWorkListLDB", "code", code);	

		if (ra.getScreenContext().equalsIgnoreCase("base")) {
			ind = "base";
		} else {
			ind = "query";
		}
		
		ra.exitScreen();
		hidden = null;
		row=null; 
		
		if (ind.equalsIgnoreCase("base")) {
			if (ekran.trim().equalsIgnoreCase("X")) {
				ra.showMessage("inf_1800");
			} else {
				ra.loadScreen(ekran.trim(),"scr_change");
			}
		} else if (ind.equalsIgnoreCase("query")) {
			if (code.equalsIgnoreCase("NEKR")) {
				ra.loadScreen("ColWorkListQBE","scr_nekr"); 
			}
			if (code.equalsIgnoreCase("MJEN")) {
				ra.loadScreen("ColWorkList2QBE","scr_mjen"); 
			}
			if (code.equalsIgnoreCase("CASH")) {
				ra.loadScreen("ColWorkList2QBE","scr_cashdep"); 
			}
			if (code.equalsIgnoreCase("ZADU")) {
				ra.loadScreen("ColWorkList2QBE","scr_zaduz"); 
			}
			
			
			
		}
		
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
