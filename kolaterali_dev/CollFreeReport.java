package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Izvješæe o osloboðenim kolateralima
*
*/
public class CollFreeReport extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollFreeReport.java,v 1.1 2009/11/03 12:01:02 hrakis Exp $";
    private final String ldbName = "CollFreeReportLDB";
    
    public CollFreeReport(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollFreeReport_SE()
    {
        ra.createLDB(ldbName);
    }

    /** Metoda koja naruèuje batch obradu. */
    public void orderCSV()
    {
        try
        {
            if(!ra.isRequiredFilled()) return;
            String parameters = generateBatchParam();
            if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("814034654"));
            ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
            ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute("BatchLogDialogLDB", "fldParamValue", parameters);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");
        }
        catch (VestigoTMException vtme)
        {
            error("CollFreeReport.toCSVfile() -> VestigoTMException", vtme);
        } 
    }
    
    /**
     * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;date_from;date_until;client_type;col_cat_id;X
     */
    private String generateBatchParam()
    {
        StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign")).append(";");
        buffer.append(getDDMMYYYY(((Date)ra.getAttribute(ldbName, "CollFreeReport_txtDateFrom")))).append(";");
        buffer.append(getDDMMYYYY(((Date)ra.getAttribute(ldbName, "CollFreeReport_txtDateUntil")))).append(";");
        buffer.append(isEmpty((String)ra.getAttribute(ldbName, "CollFreeReport_txtClientType"))).append(";");
        buffer.append(isEmpty((BigDecimal)ra.getAttribute(ldbName, "COL_CAT_ID"))).append(";");
        buffer.append("X");
        return buffer.toString();
    }
    

    /** Validacijska metoda za polja s podacima o periodu oslobaðanja. */
    public boolean CollFreeReport_Date_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null) return true;

        Date date_from = (Date)ra.getAttribute(ldbName, "CollFreeReport_txtDateFrom");
        Date date_until = (Date)ra.getAttribute(ldbName, "CollFreeReport_txtDateUntil");

        if (date_from != null && date_until != null && date_until.before(date_from))
        {
            ra.showMessage("wrn245");
            return false;
        }

        return true;
    }

    /** Validacijska metoda za polje s vrstom komitenta. */
    public boolean CollFreeReport_ClientType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String)elementValue).equals("")) return true;
        
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");       
        request.addMapping(ldbName, "CollFreeReport_txtClientType", "Vrijednosti");
        return callLookUp(request);
    }

    /** Validacijska metoda za polja s podacima o kategoriji kolaterala. */
    public boolean CollFreeReport_CollCategory_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        // poništi sva polja (osim trenutnog) koja su vezana za kategoriju kolaterala
        ra.setAttribute(ldbName, "COL_CAT_ID", null);
        ra.setAttribute(ldbName, "CollFreeReport_txtCollCategoryName", "");
        if(elementValue == null || elementValue.equals("")) return true;
    
        // inicijaliziraj i pozovi lookup
        LookUpRequest lu = new LookUpRequest("CollCategoryLookUp");
        lu.addMapping(ldbName, "COL_CAT_ID", "col_cat_id");
        lu.addMapping(ldbName, "CollFreeReport_txtCollCategoryCode", "code");
        lu.addMapping(ldbName, "CollFreeReport_txtCollCategoryName", "name");
        return callLookUp(lu);
    }

    
    /** Metoda koja poziva zadani lookup
     * @return da li je poziv uspješno završen
     */
    private boolean callLookUp(LookUpRequest lu)
    {
        try {
            ra.callLookUp(lu);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        return true;    
    }
    
    /**
     * Metoda vraca datum zapisan u string u formatu dd.MM.yyyy
     * @param java.sql.Date
     * @return datum zapisan u string u formatu dd.MM.yyyy; ako je null vraca deset blankova
     */
    private String getDDMMYYYY(Date date)
    {
        if(date == null) return "          ";
        String date_s=date.toString();
        return date_s.substring(8, 10)+"."+date_s.substring(5, 7)+"."+date_s.substring(0, 4);
    }
    
    private String isEmpty(BigDecimal bd)
    {
        if (bd == null) return " "; else return bd.toString();
    }
    
    private String isEmpty(String str)
    {
        if (str == null || str.equals("")) return " "; else return str;
    }
}