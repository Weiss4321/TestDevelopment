//created 2011.06.21
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
/**
 *
 * @author hramkr
 */
 

public class KolWCAReport extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/KolWCAReport.java,v 1.4 2012/04/12 12:48:17 hramkr Exp $";
    private final String ldbName = "KolWCAReportLDB";
    
    public KolWCAReport(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void KolWCAReport_SE()
    {
        if (!ra.isLDBExists(ldbName)) {
            ra.createLDB(ldbName);
        }
    } 

    /** Metoda koja narucuje batch obradu. */
    public void orderCSV()
    { 
        try
        {
            if(!allRequiredFilled()) { 

                return;
            }    
            String parametri = generateBatchParam();
//            System.out.println("parametri = " + parametri);      
            BigDecimal batchDefId = new BigDecimal("4639721704");
            if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", batchDefId);
            ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
            ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute("BatchLogDialogLDB", "fldParamValue", parametri);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");   
       }
        catch (VestigoTMException vtme)
        {
            error("KolWCAReport.orderCSV() -> VestigoTMException", vtme);
        } 
    } 

    private String isEmpty(BigDecimal bd)
    {
        if (bd == null) return " "; else return bd.toString();
    }
    
    private String isEmpty(String str)
    {
        if (str == null || str.equals("")) return " "; else return str.trim();
    }    
    
    /**
     * Metoda koja vraca formirani String sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;date;col_cat_id;col_num;exception_type
     */
    private String generateBatchParam()
    {
        String param = (String)ra.getAttribute("GDB", "bank_sign");
        Date process_date = (Date)ra.getAttribute(ldbName, "KolWCAReport_txtDate");
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute(ldbName, "col_cat_id");
        String colNum = (String) ra.getAttribute(ldbName, "KolWCAReport_txtColNum");
        String exception_type = "M";

        if (colNum.trim().length() > 5) {
            exception_type = "S";
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        
        if((process_date!=null)&& !(process_date.equals("")))
            param+=";"+isEmpty(process_date.toString());
        else
            param+=";"+isEmpty((String)ra.getAttribute(ldbName,"KolWCAReport_txtDate"));

        param+=";"+isEmpty(col_cat_id);  
        param+=";"+isEmpty(colNum);  
        param+=";"+isEmpty(exception_type);  

        return param;
    }

     
    /** Metoda koja validira datum izvjesca. */
    public boolean KolWCAReport_txtDate_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null) return true;
        Date date = (Date)ra.getAttribute(ldbName, "KolWCAReport_txtDate");
        Date current_date = (Date)ra.getAttribute("GDB", "ProcessingDate");
        
        if (current_date.before(date))
        {
            ra.showMessage("wrnclt121");
            return false;
        }
        return true;
    }
    
    
    /** Metoda koja validira kategoriju kolaterala.
     */    
    public boolean KolWCAReport_txtCategoryCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "KolWCAReport_txtCategoryCode", "");
            ra.setAttribute(ldbName, "KolWCAReport_txtCategoryName", "");
            ra.setAttribute(ldbName, "col_cat_id", null);
            ra.setContext("KolWCAReport_txtColNum", "fld_plain");
            return true;
        }

        ra.setAttribute(ldbName, "KolWCAReport_txtCategoryName", "");
        ra.setAttribute(ldbName, "col_cat_id", null);

        LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
        lookUpRequest.addMapping(ldbName, "KolWCAReport_txtCategoryName", "name");
        lookUpRequest.addMapping(ldbName, "col_cat_id", "col_cat_id");
        lookUpRequest.addMapping(ldbName, "KolWCAReport_txtCategoryCode", "code");

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
// obrisati i protektirati sifru kolaterala
        ra.setAttribute(ldbName,"KolWCAReport_txtColNum","");
        ra.setContext("KolWCAReport_txtColNum", "fld_change_protected");
        
        return true;
    }
    
    /** Metoda koja provjerava da li su popunjeni obavezni parametri
     */     
    public boolean allRequiredFilled() {
        Date report_date = (Date) ra.getAttribute(ldbName,"KolWCAReport_txtDate");
        String colNum = (String) ra.getAttribute(ldbName,"KolWCAReport_txtColNum");
        String colCategory = (String) ra.getAttribute(ldbName,"KolWCAReport_txtCategoryCode");
         
        if ((report_date == null || report_date.toString().trim().equals("")) &&
            (colNum == null || colNum.trim().equals(""))) {
            ra.showMessage("wrnclt180");
            return false;
        } 
       
        if (report_date != null && !(report_date.toString().trim().equals(""))) {
            if ((colCategory == null || colCategory.trim().equals("")) && (colNum == null || colNum.trim().equals(""))) {
                ra.showMessage("wrnclt179");
                 return false;                
            }
        }
        
        return true;
    }
    
}