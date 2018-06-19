package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**   
 * Handler za ekran Izvještaj o omjeru NGV i osigurane svote.
 * @author hrakis
 */ 
public class CollNgvRatioReport extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollNgvRatioReport.java,v 1.1 2013/12/20 08:29:09 hrakis Exp $";
    
    public CollNgvRatioReport(ResourceAccessor arg0)
    {
        super(arg0);
    }

    public void orderReport()
    {
        try
        {
            final String batchLdb = "BatchLogDialogLDB";
            if(!ra.isLDBExists(batchLdb)) ra.createLDB(batchLdb);
            
            ra.setAttribute(batchLdb, "BatchDefId", new BigDecimal("6493776704"));
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
            error("CollNgvRatioReport.orderReport()", vtme);
        }
    }
}