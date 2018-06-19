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
import hr.vestigo.modules.collateral.util.LookUps;

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
public class CollEstimatorDialog extends Handler {
    
    private String ldbName = "CollEstimatorDialogLDB";
    LookUps coll_lookups = null;

    public CollEstimatorDialog(ResourceAccessor ra) {
        super(ra);
        coll_lookups = new LookUps(ra);
        // TODO Auto-generated constructor stub
    }

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollEstimatorDialog.java,v 1.4 2017/11/29 09:46:23 hrazst Exp $";
    
    
    public void CollEstimatorDialog_SE(){
        
        if(!ra.isLDBExists(ldbName))
            ra.createLDB(ldbName);
        
        if(ra.getScreenContext().equals("detailsContext")){
            ra.setAttribute(ldbName, "EST_ID", ra.getAttribute("CollEstimatorListLDB", "EST_ID"));
            try {
                ra.executeTransaction();
            } catch (Exception e) {
                // TODO: handle exception
            }            
        } else if (ra.getScreenContext().equals("addContext"))
        {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtDateFrom", ra.getAttribute("GDB", "ProcessingDate"));
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtDateUntil", "9999-12-31");
        }
    }
    
    public boolean CollEstimatorDialog_txtEstComp_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstCompRegNo", "");
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstCompName", "");
            ra.setAttribute(ldbName, "EST_COMP_CUS_ID", null);
            return true;
        }
        
        if (ra.getCursorPosition().equals("CollEstimatorDialog_txtEstCompName")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstCompRegNo", "");
        } else if (ra.getCursorPosition().equals("CollEstimatorDialog_txtEstCompRegNo")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstCompName", "");
            ra.setCursorPosition(2);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstCompName") != null)
            d_name = (String) ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstCompName");
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstCompRegNo") != null)
            d_register_no = (String) ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstCompRegNo");
        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
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
        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstCompRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstCompName"));

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
        ra.setAttribute(ldbName, "EST_COMP_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstCompRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstCompName", ra.getAttribute("CustomerAllLookUpLDB", "name"));        
        boolean ret = true;
        return ret;
    
    }
    
    public boolean CollEstimatorDialog_txtEstimator_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorRegNo", "");
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorName", "");
            ra.setAttribute(ldbName, "EST_CUS_ID", null);
            return true;
        }
        
        if (ra.getCursorPosition().equals("CollEstimatorDialog_txtEstimatorName")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorRegNo", "");
        } else if (ra.getCursorPosition().equals("CollEstimatorDialog_txtEstimatorRegNo")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorName", "");
            ra.setCursorPosition(2);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstimatorName") != null)
            d_name = (String) ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstimatorName");
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstimatorRegNo") != null)
            d_register_no = (String) ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstimatorRegNo");
        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
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
        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstimatorRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "CollEstimatorDialog_txtEstimatorName"));

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

        ra.setAttribute(ldbName, "EST_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorName", ra.getAttribute("CustomerAllLookUpLDB", "name"));        

        boolean ret = true;
        return ret;
    
    }
    
    public boolean CollEstimatorDialog_txtEstimatorTypeCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorTypeCode", "");
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorTypeName", "");
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorTypeCode", null);
            return true;
        }

        if (ra.getCursorPosition().equals("CollEstimatorDialog_txtEstimatorTypeCode")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorTypeName", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("CollEstimatorDialog_txtEstimatorTypeName")) {
            ra.setAttribute(ldbName, "CollEstimatorDialog_txtEstimatorTypeCode", "");
        }
        
        ra.setAttribute(ldbName, "SysCodId", "est_type");
        
        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "est_type");

        System.out.println("1");
        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        System.out.println("2");
        request.addMapping(ldbName, "CollEstimatorDialog_txtEstimatorTypeCode", "sys_code_value");
        request.addMapping(ldbName, "CollEstimatorDialog_txtEstimatorTypeName", "sys_code_desc");
        request.addMapping(ldbName, "SysCodValueId", "sys_cod_val_id");
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
    
    public boolean CollEstimatorDialog_txtEstimatorInternal_FV(String elementName, Object elementValue, Integer lookUpType) {
        return coll_lookups.ConfirmDN(ldbName, elementName, elementValue);
    }
    
    public boolean CollEstimatorDialog_txtDateFrom_FV() {
        // datum od ne moze biti manji od current date (ukinuto)i ne moze biti veci od
        // datuma do

        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute(ldbName,"CollEstimatorDialog_txtDateFrom");

        if (date_from == null || current_date == null)
            return true;

        /*if ((date_from).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }*/

        Date date_until = (Date) ra.getAttribute(ldbName,"CollEstimatorDialog_txtDateUntil");

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

    public boolean CollEstimatorDialog_txtDateUntil_FV() {
        // datum do mora biti veci ili jednak datumu od
        // datum do moze biti D-1, D i bilo koji datum u buducnosti kod promjene

        Integer num_of_days = new Integer("1");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute(ldbName,"CollEstimatorDialog_txtDateFrom");
        Date date_until = (Date) ra.getAttribute(ldbName,"CollEstimatorDialog_txtDateUntil");
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
            ra.refreshActionList("tblCollEstimatorList");
        }
        
    }
}

