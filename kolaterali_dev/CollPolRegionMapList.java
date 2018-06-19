//created 2015.11.20
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
public class CollPolRegionMapList extends Handler {

    private String ldbName = "CollPolRegionMapListLDB";

    public CollPolRegionMapList(ResourceAccessor ra) {
        super(ra);
        // TODO Auto-generated constructor stub
    }
    
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollPolRegionMapList.java,v 1.1 2015/12/03 08:52:32 hraziv Exp $";
    
    public void CollPolRegionMapList_SE(){
        if(!ra.isLDBExists(ldbName))
            ra.createLDB(ldbName);
        
        ra.createActionListSession("tblCollPolRegionMapList");
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

        if (isTableEmpty("tblCollPolRegionMapList")) {
            ra.showMessage("wrn299");
            return;
        }
        td = (TableData) ra.getAttribute("CollPolRegionMapListLDB", "tblCollPolRegionMapList");
        
        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal pol_region_map_id = (BigDecimal) hidden.elementAt(0);
        
        ra.setAttribute("CollPolRegionMapListLDB", "POL_REGION_MAP_ID", pol_region_map_id);     
        ra.loadScreen("CollPolRegionMapDialog","detailsContext");
        
    }
    
    public void add_new() {
        //na F8   
        //ra.showMessage("infclt1");
        ra.loadScreen("CollPolRegionMapDialog","addContext");
        
    }
    
    public void close() {
        //Digni poruku da li se želi da se zatvori         
        // pitanje da li stvarno zeli potvrditi podatke
        Integer retValue = (Integer) ra.showMessage("CollMsg01");
        if (retValue != null && retValue.intValue() == 0)
            return;

        TableData td=null;

        if (isTableEmpty("tblCollPolRegionMapList")) {
            ra.showMessage("wrn299");
            return;
        }
        td = (TableData) ra.getAttribute("CollPolRegionMapListLDB", "tblCollPolRegionMapList");
        
        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal pol_region_map_id = (BigDecimal) hidden.elementAt(0);
        
        ra.setAttribute("CollPolRegionMapListLDB", "POL_REGION_MAP_ID", pol_region_map_id);
        
        // insert podataka (ctx = scr_insert)
        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
        } catch (VestigoTMException vtme) {
            error("CollPolRegionMapList -> close(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
        
        if (((Integer) ra.getAttribute("GDB", "TransactionStatus"))
                .equals(new Integer("0"))) {

            //ra.exitScreen();
            //ra.refresh();
            ra.refreshActionList("tblCollPolRegionMapList");
        }
        
    }
    
    public void search() {
        
        ra.performQueryByExample("tblCollPolRegionMapList");
        ra.exitScreen();
      //  ra.showMessage("infclt1");
        
    }
    
    public void query_by_example()  // F5
    {
        //ra.showMessage("infclt1");
        ra.loadScreen("CollPolRegionMapQBE");
    }
}

