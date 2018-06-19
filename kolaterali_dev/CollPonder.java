package hr.vestigo.modules.collateral;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.*;

/**
 * 
 * @author hrakis 
 * Handler klasa za ekran Ponderi
 *
 */
public class CollPonder extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollPonder.java,v 1.1 2009/09/25 11:33:01 hrakis Exp $";
    
    public CollPonder(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollPonder_SE()
    {
        ra.createLDB("CollPonderLDB");
        ra.createActionListSession("tblCollPonder");
    }
    
    public void details()           // F4
    {
        if (isTableEmpty("tblCollPonder")) ra.showMessage("wrn299");
        else ra.loadScreen("CollPonderDialog", "scr_details");
    }
    
    public void query_by_example()  // F5
    {
        ra.loadScreen("CollPonderQBE", "scr_query");
    }
    
    public void refresh()           // Shift-F5 
    {
        ra.refreshActionList("tblCollPonder");
    }

    public void add()               // F7
    {
        ra.loadScreen("CollPonderDialog", "scr_add");
    }
     
    public void search()
    {
        ra.performQueryByExample("tblCollPonder");
        ra.exitScreen();
    }

    public boolean isTableEmpty(String tableName)
    {
        TableData td = (TableData) ra.getAttribute(tableName);
        if (td == null) return true;
        else if (td.getData().size() == 0) return true;
        else return false;
    }
}