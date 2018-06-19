package hr.vestigo.modules.collateral;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;


/**   
 * Slobodne partije iz okvira / Višestruko dodane partije.
 * @author hrakis
 */ 
public class FrameAccException extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/FrameAccException.java,v 1.2 2011/04/27 12:49:29 hrakis Exp $";
    private String ldbName = "FrameAccExceptionLDB";

    public FrameAccException(ResourceAccessor arg0)
    {
        super(arg0);
    }

    public void FrameAccException_SE() throws Exception
    {
        if(!ra.isLDBExists(ldbName)) ra.createLDB(ldbName);
        
        // ovisno o kontekstu postavlja vrstu izuzetka i ime ekrana
        if(ra.getScreenContext().equalsIgnoreCase("duplicateContext"))
        {
            ra.setAttribute(ldbName, "EXCEPTION_TYPE", "D");
            ra.setScreenTitle("Višestruko dodane partije");
        }
        else
        {
            ra.setAttribute(ldbName, "EXCEPTION_TYPE", "F");
            ra.setScreenTitle("Slobodne partije iz okvira");
        }
        
        ra.createActionListSession("tblFrameAccException");
    }

    public void details()
    {
        if(prepareForAction()) ra.loadScreen("FrameAccExceptionDialog", "defaultContext");
    }

    public void orderCSV()
    {
        try
        {
            if(!ra.isRequiredFilled()) return;
            if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
            
            String exception_type = (String) ra.getAttribute(ldbName, "EXCEPTION_TYPE");
            
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("4454981964"));
            ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
            ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute("BatchLogDialogLDB", "fldParamValue", "RB;" + exception_type + ";X");
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");
        }
        catch (VestigoTMException vtme)
        {
            error("FrameAccException.orderCSV()", vtme);
        }
    }

    public void add()
    {
        ra.loadScreen("FrameAccExceptionDialog", "insertContext");
    }

    public void modify()
    {
        if(prepareForAction()) ra.loadScreen("FrameAccExceptionDialog", "updateContext");
    }

    public void refresh()
    {
        ra.refreshActionList("tblFrameAccException");
    }

    private boolean prepareForAction()
    {
        TableData tableData = (TableData)ra.getAttribute(ldbName, "tblFrameAccException");
        if(tableData == null || tableData.isTableEmpty())
        {
            ra.showMessage("wrn299");
            return false;
        }
        Vector hidden = (Vector)tableData.getSelectedRowUnique();
        ra.setAttribute(ldbName, "FRA_ACC_EXC_ID", hidden.get(0));
        return true;
    }
}