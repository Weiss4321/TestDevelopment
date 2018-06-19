package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**   
 * Handler za ekran Izvješæe o dospijeæu kolaterala i plasmana.
 * @author hrakis
 */ 
public class CollDueDatesReport extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollDueDatesReport.java,v 1.1 2014/03/27 13:39:09 hrakis Exp $";
    
    private final String ldbName = "CollDueDatesReportLDB";
    
    
    public CollDueDatesReport(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollDueDatesReport_SE()
    {
        ra.createLDB(ldbName);
    }

    public void orderReport()
    {
        try
        {
            if(!ra.isRequiredFilled()) return;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date report_date = (Date)ra.getAttribute(ldbName, "CollDueDatesReport_txtReportDate");
            String report_date_str = dateFormat.format(report_date).toString();
            String bank_sign = (String)ra.getAttribute("GDB", "bank_sign");
            String parameters = bank_sign + ";" + report_date_str;
            
            final String batchLdb = "BatchLogDialogLDB";
            if(!ra.isLDBExists(batchLdb)) ra.createLDB(batchLdb);
            ra.setAttribute(batchLdb, "BatchDefId", new BigDecimal("6526066704"));
            ra.setAttribute(batchLdb, "RepDefId", null);
            ra.setAttribute(batchLdb, "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(batchLdb, "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute(batchLdb, "fldParamValue", parameters);
            ra.setAttribute(batchLdb, "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute(batchLdb, "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");
        }
        catch (VestigoTMException vtme)
        {
            error("CollDueDatesReport.orderReport()", vtme);
        }
    }
}