package hr.vestigo.modules.collateral;

import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;


/**
 * Handler za ekran Povijest promjene statusa police osiguranja
 * @author hrakis
 */
public class InsPolStatChgHistoryList extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsPolStatChgHistoryList.java,v 1.1 2011/08/10 11:24:50 hrakis Exp $";
    
    private String ldbName = "InsPolStatChgHistoryListLDB";


    public InsPolStatChgHistoryList(ResourceAccessor ra)
    {
        super(ra);
    }

    public void InsPolStatChgHistoryList_SE()
    {
        try
        {
            if(!ra.isLDBExists(ldbName)) ra.createLDB(ldbName);
            
            if(ra.isLDBExists("InsuPolicyLDB"))
            {
                TableData tableData = (TableData)ra.getAttribute("InsuPolicyLDB", "tblInsuPolicy");
                if(tableData == null || tableData.isTableEmpty()) return;
                Vector hidden = (Vector)tableData.getSelectedRowUnique();
                ra.setAttribute(ldbName, "IP_ID", hidden.get(0));
                ra.executeTransaction();
            }
        }
        catch (VestigoTMException vtme)
        {
            if (vtme.getMessageID() != null) ra.showMessage(vtme.getMessageID());
            ra.exitScreen();
        }
    }
}