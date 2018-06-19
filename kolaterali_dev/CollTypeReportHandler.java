//created 2008.09.10
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
 * 
 * @author hrarmv Handler za ekran za unos parametara za narucivanje obrade za
 *         generiranje izvjestaja po vrstama kolaterala
 */
public class CollTypeReportHandler extends Handler {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollTypeReportHandler.java,v 1.9 2014/06/06 10:34:43 hrakis Exp $";

    public CollTypeReportHandler(ResourceAccessor ra) {
        super(ra);
    }

    public void CollTypeReport_SE() {
        if (!ra.isLDBExists("CollTypeReportLDB")) {
            ra.createLDB("CollTypeReportLDB");
        }
        return;
    }

    public void toCSVfile() throws VestigoTMException {
        
        if(!ra.isRequiredFilled()){
            return;
        }
        if (!ra.isLDBExists("CollTypeReportLDB")) {
            ra.createLDB("CollTypeReportLDB");
        }
        
        // provjera postoje li podaci za zadani datum, samo ako je datum <> current date
        Date date = (Date)ra.getAttribute("CollTypeReportLDB", "CollTypeReport_txtDate");
        Date current_date = (Date)ra.getAttribute("GDB", "ProcessingDate");        
        if (date.before(current_date)) {
            try {
                ra.setScreenContext("scr_count");
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                error("CollTypeReport.toCSVfile()", vtme);
                return;
            }
            finally {
                ra.setScreenContext("default");
            }
            Integer brojZapisa = (Integer)ra.getAttribute("CollTypeReportLDB", "BrojZapisa");
            if(brojZapisa.compareTo(new Integer(0)) <= 0)
            {
                ra.showMessage("wrnclt164");
                return;
            }
        } else {
            ra.setAttribute("CollTypeReportLDB", "IndikatorArhive", "N");
        }
        ra.setScreenContext("default");

        //Zadavanje automatske obrade
        Integer answer = (Integer) ra.showMessage("qer075");
        if (answer.intValue() == 0) {
            return;
        }

        if (!ra.isLDBExists("BatchLogDialogLDB")) {
            ra.createLDB("BatchLogDialogLDB");
        }

        try {
            //batch_id od bo31
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("2537850003"));
            ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
            ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));

            //Postavljanje ulaznih parametara batcha
            String param = (String) ra.getAttribute("GDB", "bank_sign");
            param += ";" + (Date) ra.getAttribute("CollTypeReportLDB", "CollTypeReport_txtDate");
            param += ";" + (String) ra.getAttribute("CollTypeReportLDB", "CollTypeReport_txtCustType");
            param += ";" + (BigDecimal) ra.getAttribute("CollTypeReportLDB", "COL_CAT_ID");
            param += ";" + (String) ra.getAttribute("CollTypeReportLDB", "CollTypeReport_txtListaCode");  
            param += ";" + (String) ra.getAttribute("CollTypeReportLDB", "IndikatorArhive");  
            
            ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
// poziv transakcija samo ako je datum <> current date
            ra.executeTransaction();
            ra.showMessage("inf075");
  
        } 
        catch (hr.vestigo.framework.controller.tm.VestigoTMException vtme) {
            ra.showMessage("errcomzst1");
            error("CollTypeReport:izvodenje funkcije toCSVfile", vtme);
            throw vtme;
        }
    }

    public boolean CollTypeReport_txtDate_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null) return true;
        Date date = (Date)ra.getAttribute("CollTypeReportLDB", "CollTypeReport_txtDate");
        Date current_date = (Date)ra.getAttribute("GDB", "ProcessingDate");
        Date date_until =current_date;
//        Date date_until = addDaysToDate(current_date, -1);
        Date date_from = addDaysToDate(date_until, -91);
        
/*        if (date.before(date_from) || date.after(date_until))
        {
            HashMap params = new HashMap();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            params.put("date_from", sdf.format(date_from));
            params.put("date_until", sdf.format(date_until));
            ra.showMessage("wrnclt163", params);
            return false;
        }*/ 
        return true;
    }

    public boolean CollTypeReport_txtCustType_FV(String elementName, Object ElValue, Integer lookUpType) {
        String ldbName = "CollTypeReportLDB";
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(ldbName, "CollTypeReport_txtCustType", "");
            // ra.setContext("CollReview_txtRegisterNo", "fld_plain");
            return true;
        }

        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");

        request.addMapping(ldbName, "CollTypeReport_txtCustType", "Vrijednosti");

        try {
            ra.callLookUp(request);
        }
        catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        }
        catch (NothingSelected ns) {
            return false;
        }
        return true;
    }

    public boolean CollTypeReport_txtCategory_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = "CollTypeReportLDB";
        //String coll_category;
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollTypeReport_txtCategoryCode", "");
            ra.setAttribute(ldbName, "CollTypeReport_txtCategoryName", "");
            ra.setAttribute(ldbName, "COL_CAT_ID", null);
            return true;
        }

        ra.setAttribute(ldbName, "CollTypeReport_txtCategoryName", "");
        ra.setAttribute(ldbName, "COL_CAT_ID", null);

        LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
        lookUpRequest.addMapping(ldbName, "CollTypeReport_txtCategoryName", "name");
        lookUpRequest.addMapping(ldbName, "COL_CAT_ID", "col_cat_id");
        lookUpRequest.addMapping(ldbName, "CollTypeReport_txtCategoryCode", "code");

        try {
            ra.callLookUp(lookUpRequest);
        }
        catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        }
        catch (NothingSelected ns) {
            return false;
        }
        return true;
    }

    public boolean CollTypeReport_txtListaCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = "CollTypeReportLDB";
        //String coll_category;
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollTypeReport_txtListaCode", "");
            ra.setAttribute(ldbName, "CollTypeReport_txtListaName", "");
            ra.setAttribute(ldbName, "dummy", null);
            return true;
        }

        ra.setAttribute(ldbName, "SysCodId", "kol_lista");
        ra.setAttribute(ldbName, "CollTypeReport_txtListaName", "");
        ra.setAttribute(ldbName, "dummy", null);
        
        if (!(ra.isLDBExists("SysCodeValueNewLookUpLDB"))) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "kol_lista");
        
        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
 
        request.addMapping(ldbName, "CollTypeReport_txtListaCode", "sys_code_value");
        request.addMapping(ldbName, "CollTypeReport_txtListaName", "sys_code_desc");
        request.addMapping(ldbName, "dummy", "sys_cod_val_id");

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
    
    private Date addDaysToDate(Date p_date, int num_days)
    {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(p_date);
        c1.add(Calendar.DATE, num_days);
        Date date = new Date(c1.getTimeInMillis());
        return date;
    }
}
