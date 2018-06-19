//created 2014.07.22
package hr.vestigo.modules.collateral;


import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import javax.sql.RowSet;

/**
 *
 * @author hraziv
 */
public class CollEstimatorList extends Handler {
    
    private String ldbName = "CollEstimatorListLDB";

    public CollEstimatorList(ResourceAccessor ra) {
        super(ra);
        // TODO Auto-generated constructor stub
    }

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollEstimatorList.java,v 1.1 2014/09/04 12:26:53 hraziv Exp $";
    
    
    public void CollEstimatorList_SE(){
        
        if(!ra.isLDBExists(ldbName))
            ra.createLDB(ldbName);
        
        ra.createActionListSession("tblCollEstimatorList");
    }
    
    public boolean isTableEmpty(String tableName) {
        TableData td = (TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }
    
    public void details() {
        //na F4     
        
        TableData td=null;

        if (isTableEmpty("tblCollEstimatorList")) {
            ra.showMessage("wrn299");
            return;
        }
        td = (TableData) ra.getAttribute("CollEstimatorListLDB", "tblCollEstimatorList");
        
        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal est_id = (BigDecimal) hidden.elementAt(0);
        
        ra.setAttribute("CollEstimatorListLDB", "EST_ID", est_id);        
        ra.loadScreen("CollEstimatorDialog","detailsContext");
        
    }
    
    public void add_new() {
        //na F8      
        ra.loadScreen("CollEstimatorDialog","addContext");
        
    }
    
    public void search() {
        ra.performQueryByExample("tblCollEstimatorList");
        ra.exitScreen();
        
    }
    
    public void query_by_example()  // F5
    {
        ra.loadScreen("CollEstimatorQBE");
    }
    
    public void close() {
        //Digni poruku da li se želi da se zatvori
        
        
        
        // pitanje da li stvarno zeli potvrditi podatke
        Integer retValue = (Integer) ra.showMessage("CollMsg01");
        if (retValue != null && retValue.intValue() == 0)
            return;

        TableData td=null;

        if (isTableEmpty("tblCollEstimatorList")) {
            ra.showMessage("wrn299");
            return;
        }
        td = (TableData) ra.getAttribute("CollEstimatorListLDB", "tblCollEstimatorList");
        
        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal est_id = (BigDecimal) hidden.elementAt(0);
        
        ra.setAttribute("CollEstimatorListLDB", "EST_ID", est_id);
        
        // insert podataka (ctx = scr_insert)
        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
        } catch (VestigoTMException vtme) {
            error("CollEstimatorList -> close(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
        
        if (((Integer) ra.getAttribute("GDB", "TransactionStatus"))
                .equals(new Integer("0"))) {

            //ra.exitScreen();
            //ra.refresh();
            ra.refreshActionList("tblCollEstimatorList");
        }
        
    }
    
}

