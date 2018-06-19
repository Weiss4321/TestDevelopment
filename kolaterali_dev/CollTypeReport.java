//created 2008.09.10
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 *
 * @author hrarmv
 * Handler za ekran za unos parametara za narucivanje obrade 
 * za generiranje izvjestaja po vrstama kolaterala
 */
public class CollTypeReport extends Handler {
    
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollTypeReport.java,v 1.1 2008/09/10 14:18:12 hrarmv Exp $";
    
    public CollTypeReport(ResourceAccessor ra) {
        super(ra);
    }
    
    public void CollTypeReport_SE(){        
        
       /* if(!ra.isLDBExists("CollTypeReportLDB")){
            ra.createLDB("CollTypeReportLDB");
        }
        */
        return;
    }   
}

