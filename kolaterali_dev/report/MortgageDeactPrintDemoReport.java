//created 5.7.2012.
package hr.vestigo.modules.collateral.report;

import java.util.Vector;

import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.report.ReportHandler;

/**
 *
 * @author vu00209
 */
public class MortgageDeactPrintDemoReport implements ReportHandler {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/report/MortgageDeactPrintDemoReport.java,v 1.1 2012/07/13 13:39:44 vu00209 Exp $";
    
    
    public Vector getReportData(ResourceAccessor ra) {
        Vector data = new Vector();
        return data;
    }
}

