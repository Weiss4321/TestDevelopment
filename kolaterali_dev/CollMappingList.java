//created 2017.02.06
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
public class CollMappingList extends Handler {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollMappingList.java,v 1.1 2017/04/14 10:13:41 hraziv Exp $";
    private String ldbName = "CollMappingListLDB";
    
    public CollMappingList(ResourceAccessor ra) {
        super(ra);
        // TODO Auto-generated constructor stub
    }
    
    public void CollMappingList_SE(){
        
        if(!ra.isLDBExists(ldbName))
            ra.createLDB(ldbName);
        
        ra.createActionListSession("tblCollMappingList");
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
        
        System.out.println("tblCollMappingList - start");

        if (isTableEmpty("tblCollMappingList")) {
            ra.showMessage("wrn299");
            return;
        }
        
        System.out.println("tblCollMappingList - end");
        
        
        td = (TableData) ra.getAttribute("CollMappingListLDB", "tblCollMappingList");
        
        System.out.println("getAttribute tblCollMappingList - end");
        
        Vector hidden = (Vector) td.getSelectedRowUnique();
        System.out.println("param 1");
        BigDecimal col_gcm_typ_id = (BigDecimal) hidden.elementAt(0);
        System.out.println("param2");
        BigDecimal col_gcm_typ_map_id = (BigDecimal) hidden.elementAt(1);
        
        ra.setAttribute("CollMappingListLDB", "COL_GCM_TYP_ID", col_gcm_typ_id);
        ra.setAttribute("CollMappingListLDB", "COL_GCM_TYP_MAP_ID", col_gcm_typ_map_id); 
        ra.loadScreen("CollMappingDialog","defaultContext");
        
    }
}

