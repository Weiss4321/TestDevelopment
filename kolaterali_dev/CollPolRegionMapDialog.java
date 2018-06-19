//created 2015.11.27
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
public class CollPolRegionMapDialog extends Handler {
    
    private String ldbName = "CollPolRegionMapDialogLDB";
    
    public CollPolRegionMapDialog(ResourceAccessor ra) {
        super(ra);
        // TODO Auto-generated constructor stub
    }

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollPolRegionMapDialog.java,v 1.1 2015/12/03 08:52:32 hraziv Exp $";
    
    public void CollPolRegionMapDialog_SE(){
        
        if(!ra.isLDBExists(ldbName))
            ra.createLDB(ldbName);        
        if(ra.getScreenContext().equals("detailsContext")){
            //ra.showMessage("infclt2");
            ra.setAttribute(ldbName, "POL_REGION_MAP_ID", ra.getAttribute("CollPolRegionMapListLDB", "POL_REGION_MAP_ID"));
            try {
                //ra.showMessage("infclt1");
                ra.executeTransaction();
                //ra.showMessage("infclt2");
                
            } catch (Exception e) {
                //ra.showMessage("ramessage", e.getMessage());
                //throw new VestigoTMException(1, e.getMessage(),"errclt1", null);
                // TODO: handle exception
            }            
        } else if (ra.getScreenContext().equals("addContext"))
        {
            ra.setAttribute(ldbName, "CollPolMapRegionDialog_txtDateFrom", ra.getAttribute("GDB", "ProcessingDate"));
            ra.setAttribute(ldbName, "CollPolMapRegionDialog_txtDateUntil", "9999-12-31");
        }
    }
    
    public boolean CollPolRegionMapDialog_txtRegionCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollPolMapRegionDialog_txtRegionCode", "");
            ra.setAttribute(ldbName, "CollPolMapRegionDialog_txtRegionName", "");
            ra.setAttribute(ldbName, "CollPolMapRegionDialog_txtRegionCode", null);
            return true;
        }

        if (ra.getCursorPosition().equals("CollPolMapRegionDialog_txtRegionCode")) {
            ra.setAttribute(ldbName, "CollPolMapRegionDialog_txtRegionName", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("CollPolMapRegionDialog_txtRegionName")) {
            ra.setAttribute(ldbName, "CollPolMapRegionDialog_txtRegionCode", "");
        }
        
        ra.setAttribute(ldbName, "SysCodId", "coll_region");
        
        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "coll_region");

        System.out.println("1");
        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        System.out.println("2");
        request.addMapping(ldbName, "CollPolMapRegionDialog_txtRegionCode", "sys_code_value");
        request.addMapping(ldbName, "CollPolMapRegionDialog_txtRegionName", "sys_code_desc");
        request.addMapping(ldbName, "REGION_ID", "sys_cod_val_id");
        System.out.println("3");
        try {
            System.out.println("4");
            ra.callLookUp(request);
            System.out.println("5");
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }
        return true;
    }
    
    public boolean CollPolRegionMapDialog_txtPolMap_FV(String elementName, Object elementValue, Integer lookUpType) {
        
        ra.setAttribute(ldbName,"CollPolRegionMapDialog_txtPolMapName","");
        if (ra.getAttribute(ldbName,"CollPolRegionMapDialog_txtPolMapCode").equals("")) {
            return true;
        }
        
        LookUpRequest request = new LookUpRequest("PoliticalMapByTypIdLookUp");
        if(!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")){
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }
        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId","5999");
        request.addMapping(ldbName,"POL_MAP_ID", "pol_map_id");
        request.addMapping(ldbName,"CollPolRegionMapDialog_txtPolMapCode", "code");
        request.addMapping(ldbName,"CollPolRegionMapDialog_txtPolMapName", "name");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012"); 
            return false;
        } catch (NothingSelected ns) {      
            return false;
        }
        return true;
    }
    
    
    public boolean CollPolRegionMapDialog_txtPolMapName_FV(String elementName, Object elementValue, Integer lookUpType) {
        ra.setAttribute(ldbName,"CollPolRegionMapDialog_txtPolMapCode","");
        if (ra.getAttribute(ldbName,"CollPolRegionMapDialog_txtPolMapName").equals("")) {
            return true;
        }
        
        LookUpRequest request = new LookUpRequest("PoliticalMapByTypIdLookUp");
        if(!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")){
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }
        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId","5999");
        request.addMapping(ldbName,"POL_MAP_ID", "pol_map_id");
        request.addMapping(ldbName,"CollPolRegionMapDialog_txtPolMapCode", "code");
        request.addMapping(ldbName,"CollPolRegionMapDialog_txtPolMapName", "name");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012"); 
            return false;
        } catch (NothingSelected ns) {      
            return false;
        }
        return true;
    }
    
    public void confirm() {
        if (!(ra.isRequiredFilled())) {
            return;
        }
        // pitanje da li stvarno zeli potvrditi podatke
        Integer retValue = (Integer) ra.showMessage("col_qer004");
        if (retValue != null && retValue.intValue() == 0)
            return;

        // insert podataka (ctx = scr_insert)        
        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
        } catch (VestigoTMException vtme) {
            error("CollEstimatorDialog -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }        
       
        
        if (((Integer) ra.getAttribute("GDB", "TransactionStatus"))
                .equals(new Integer("0"))) {

            ra.exitScreen();
            ra.refreshActionList("tblCollPolRegionMapList");
        }        
    }
    
    public boolean CollPolMapRegionDialog_txtDateFrom_FV() {
        // datum od ne moze biti manji od current date (ukinuto)i ne moze biti veci od
        // datuma do
        //ra.showMessage("infclt1");

        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute(ldbName,"CollPolMapRegionDialog_txtDateFrom");

        if (date_from == null || current_date == null)
            return true;

        /*if ((date_from).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }*/

        Date date_until = (Date) ra.getAttribute(ldbName,"CollPolMapRegionDialog_txtDateUntil");

        if (!(cmp_dte(date_from, date_until))) {
            ra.showMessage("wrncltzst4");
            return false;
        }

        if (date_from == null || date_until == null)
            return true;
        if ((date_until).before(date_from)) {
            ra.showMessage("wrnclt105");
            return false;
        }

        return true;
    }

    public boolean CollPolMapRegionDialog_txtDateUntil_FV() {
        // datum do mora biti veci ili jednak datumu od
        // datum do moze biti D-1, D i bilo koji datum u buducnosti kod promjene

        Integer num_of_days = new Integer("1");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute(ldbName,"CollPolMapRegionDialog_txtDateFrom");
        Date date_until = (Date) ra.getAttribute(ldbName,"CollPolMapRegionDialog_txtDateUntil");
        Date day_before = addOrDeductDaysFromDate(current_date,num_of_days, false);

        if ((date_until).before(date_from)) {
            ra.showMessage("wrnclt105");
            return false;
        }
        /*
         * if (!(coll_cmp_util.cmp_dte(date_from, date_until))) {
         * ra.showMessage("wrncltzst4"); return false; }
         */
        /*
         * if ((date_until).before(current_date)) { ra.showMessage("wrnclt125");
         * return false; }
         

        if ((date_until).before(day_before)) {
            ra.showMessage("wrnclt166");
            return false;
        }
        */

        return true;
    }
    
     //  usporedjivanje dva datuma
     //  ako su razlicita vraca true  
     public boolean cmp_dte(java.sql.Date dat1, java.sql.Date dat2) {
                
        if ( (dat1 == null) && (dat2 == null) ) {
            return false;
        } else if ( (dat1 != null) && (dat2 == null) ) {
            return true;
        } else if ( (dat1 == null) && (dat2 != null) ) {
            return true;
        } 
            // nisu null
                
        if ( dat1.compareTo(dat2) == 0) {
            return false;
        } else {
            return true; 
        } 
                 
     }
     /**
     *
     * Method recives date and number of days wich can be added or subtracted from the given
     * parameter date <br>
     * if last parametar of method is <b>true</b> than days will be added to given date<br>
     * if the last parematar is <b>false</b> than days will be subtracted from the given date<br>
     *
     * @param p_date
     * @param num_days
     * @param add parametar tells method to add/subtract days from date
     * @return resulting java.sql.Date
     */
    public static Date addOrDeductDaysFromDate(Date p_date,Integer num_days,boolean add){

        int num_d = add?num_days.intValue():-num_days.intValue();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(p_date);
        c1.add(Calendar.DATE,num_d);
        Date date = new Date(c1.getTimeInMillis());
        return date;
    }
}

