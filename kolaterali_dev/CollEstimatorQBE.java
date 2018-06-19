//created 2014.08.26
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
public class CollEstimatorQBE extends Handler {
    
    private String ldbName = "CollEstimatorListLDB";

    public CollEstimatorQBE(ResourceAccessor ra) {
        super(ra);
        // TODO Auto-generated constructor stub
    }

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollEstimatorQBE.java,v 1.1 2014/09/04 12:26:53 hraziv Exp $";
    
    public boolean CollEstimatorQBE_txtEstimatorRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorRegNo", "");
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorCode", "");
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorName", "");
            ra.setAttribute(ldbName, "EST_CUS_ID", null);
            //ra.setAttribute("SMEScoringLDB", "SMEScoring_txtCompanyName", null);
            //clearSMECustomerData();
            
            return true;
        }
        
        if (ra.getCursorPosition().equals("CollEstimatorQBE_txtEstimatorRegNo")) {
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorCode", "");
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorName", "");
        } else if (ra.getCursorPosition().equals("CollEstimatorQBE_txtEstimatorCode")) {
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorRegNo", "");
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorName", "");
            ra.setCursorPosition(2);
        }
        else if (ra.getCursorPosition().equals("CollEstimatorQBE_txtEstimatorName")) {
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorRegNo", "");
            ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorCode", "");
            ra.setCursorPosition(3);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorName") != null)
            d_name = (String) ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorName");
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorRegNo") != null)
            d_register_no = (String) ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorRegNo");
        
        String d_code = "";
        if (ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorCode") != null)
            d_code = (String) ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorCode");
        
        if ((d_name.length() < 4) && (d_register_no.length() < 4) && (d_code.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }
        

        //kontola da li je zvjezdica na pravom mjestu ili se baca exception s porukom, samo register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }
        
        ///
        if (ra.isLDBExists("CustomerAllLookUpLDB")) {
            ra.setAttribute("CustomerAllLookUpLDB", "cus_id", null);
            ra.setAttribute("CustomerAllLookUpLDB", "register_no", "");
            ra.setAttribute("CustomerAllLookUpLDB", "code", "");
            ra.setAttribute("CustomerAllLookUpLDB", "name", "");
            ra.setAttribute("CustomerAllLookUpLDB", "add_data_table", "");
            ra.setAttribute("CustomerAllLookUpLDB", "cus_typ_id", null);
            ra.setAttribute("CustomerAllLookUpLDB", "cus_sub_typ_id", null);
            ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", null);
            ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
        } else {
            ra.createLDB("CustomerAllLookUpLDB");
        }
        //init za poruku o pogresci
        //ra.setAttribute("SMEScoringLDB","SME_MSG","");
        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorName"));
        ra.setAttribute("CustomerAllLookUpLDB", "code", ra.getAttribute(ldbName, "CollEstimatorQBE_txtEstimatorCode"));

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        //clearSMECustomerData();
        ra.setAttribute(ldbName, "EST_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorName", ra.getAttribute("CustomerAllLookUpLDB", "name"));        
        ra.setAttribute(ldbName, "CollEstimatorQBE_txtEstimatorCode", ra.getAttribute("CustomerAllLookUpLDB", "code"));

        boolean ret = true;
        //System.out.println("dsfafafasfsadfs");
        
        return ret;
    
    }
    
    public void search() {
        ra.performQueryByExample("tblCollEstimatorList");
        ra.exitScreen();
        
    }
    
    public boolean CollEstimatorQBE_txtEstimatorDateFrom_FV() {
        // datum od ne moze biti manji od current date i ne moze biti veci od
        // datuma do

        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute(ldbName,"CollEstimatorQBE_txtEstimatorDateFrom");

        if (date_from == null || current_date == null)
            return true;

        Date date_until = (Date) ra.getAttribute(ldbName,"CollEstimatorQBE_txtEstimatorDateUntil");

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
    
    public boolean CollEstimatorQBE_txtEstimatorDateUntil_FV() {
        // datum do mora biti veci ili jednak datumu od
        // datum do moze biti D-1, D i bilo koji datum u buducnosti kod promjene

        Integer num_of_days = new Integer("1");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute(ldbName,"CollEstimatorQBE_txtEstimatorDateFrom");
        Date date_until = (Date) ra.getAttribute(ldbName,"CollEstimatorQBE_txtEstimatorDateUntil");
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
         */

        if ((date_until).before(day_before)) {
            ra.showMessage("wrnclt166");
            return false;
        }

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

