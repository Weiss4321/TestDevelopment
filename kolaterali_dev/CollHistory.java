package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

import java.math.BigDecimal;


/**   
 * Handler za ekran Historizacija kolaterala.
 * @author hrakis
 */ 
public class CollHistory extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollHistory.java,v 1.1 2014/12/16 12:29:14 hrakis Exp $";
    
    private final String ldbName = "CollHistoryLDB";
    
    
    public CollHistory(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    /** Funkcija koja se pokreæe pri ulasku na ekran. */
    public void CollHistory_SE() throws Exception
    {
        if (!ra.isLDBExists(ldbName)) ra.createLDB(ldbName);
        
        BigDecimal col_hea_id = null;
        String col_num = null;
        
        if (ra.isLDBExists("RealEstateDialogLDB"))
        {
            col_hea_id = (BigDecimal)ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_HEA_ID");
            col_num = (String)ra.getAttribute("RealEstateDialogLDB", "RealEstate_txtCode");
        }
        
        if (col_hea_id == null && ra.isLDBExists("CollHeadLDB"))
        {
            col_hea_id = (BigDecimal)ra.getAttribute("CollHeadLDB", "COL_HEA_ID");
            col_num = (String)ra.getAttribute("CollHeadLDB", "Coll_txtCode");
        }
        
        ra.setAttribute(ldbName, "COL_HEA_ID", col_hea_id);
        ra.setAttribute(ldbName, "CollHistory_txtColNum", col_num);
        
        ra.executeTransaction();
    }

    
    public void orderReport()
    {
    }
}