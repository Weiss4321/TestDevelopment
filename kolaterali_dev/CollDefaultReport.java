package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


/**   
 * Handler za ekran Kolaterali komitenata u Defaultu.
 * @author hrakis
 */ 
public class CollDefaultReport extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollDefaultReport.java,v 1.1 2015/03/30 12:17:57 hrakis Exp $";
    
    private final String ldbName = "CollDefaultReportLDB";
    
    
    public CollDefaultReport(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollDefaultReport_SE()
    {
        ra.createLDB(ldbName);
        ra.setAttribute(ldbName, "CollDefaultReport_txtDate", new Date(System.currentTimeMillis()));
    }

    public void orderReport()
    {
        try
        {
            final String batchLdb = "BatchLogDialogLDB";
            if(!ra.isLDBExists(batchLdb)) ra.createLDB(batchLdb);
            ra.setAttribute(batchLdb, "BatchDefId", new BigDecimal("74610327"));
            ra.setAttribute(batchLdb, "RepDefId", null);
            ra.setAttribute(batchLdb, "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(batchLdb, "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute(batchLdb, "fldParamValue", "RB");
            ra.setAttribute(batchLdb, "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute(batchLdb, "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");
        }
        catch (VestigoTMException vtme)
        {
            error("CollDefaultReport.orderReport()", vtme);
        }
    }
}