package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
 * Handler klasa za ekran Izvješæe o promjeni statusa po policama osiguranja
 * @author hrakis
 */
public class InsPolStatChgReport extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsPolStatChgReport.java,v 1.2 2017/10/13 08:41:12 hrakis Exp $";
    private final String ldbName = "InsPolStatChgReportLDB";
    
    public InsPolStatChgReport(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void InsPolStatChgReport_SE() throws Exception
    {
        ra.createLDB(ldbName);
    }
    
    /** Metoda koja naruèuje obradu. */
    public void orderCSV()
    {
        try
        {
            if(!ra.isRequiredFilled()) return;
            String parameters = generateBatchParam();
            if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("4729536704"));
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
            error("InsPolStatChgReport.orderCSV() -> VestigoTMException", vtme);
        }
    }
    
    /**
     * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;policy_type;cust_type;ic_id;org_uni_id;status_old;status_new;wrn_status_old;wrn_status_new;date_from;date_until;X
     */
    private String generateBatchParam()
    {
        StringBuffer buffer = new StringBuffer();        
        buffer.append((String)ra.getAttribute("GDB", "bank_sign")).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "InsPolStatChgReport_txtPolicyType"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "InsPolStatChgReport_txtCustType"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "IC_ID"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "ORG_UNI_ID"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "InsPolStatChgReport_txtStatusOld"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "InsPolStatChgReport_txtStatusNew"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "InsPolStatChgReport_txtWrnStatusOld"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "InsPolStatChgReport_txtWrnStatusNew"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "InsPolStatChgReport_txtDateFrom"))).append(";");
        buffer.append(isEmpty(ra.getAttribute(ldbName, "InsPolStatChgReport_txtDateUntil"))).append(";");
        buffer.append("X");
        return buffer.toString();
    }

    
    /** Validacijska metoda za vrstu komitenta. */
    public boolean InsPolStatChgReport_CustType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        ra.setAttribute(ldbName, "InsPolStatChgReport_txtCustTypeDesc", null);
        if (elementValue == null || ((String)elementValue).equals("")) return true;
        
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");
        request.addMapping(ldbName, "InsPolStatChgReport_txtCustType", "Vrijednosti");
        if(!callLookUp(request)) return false;
        
        if(ra.getAttribute(ldbName, "InsPolStatChgReport_txtCustType").equals("P")) ra.setAttribute(ldbName, "InsPolStatChgReport_txtCustTypeDesc", "Pravne osobe");
        else if(ra.getAttribute(ldbName, "InsPolStatChgReport_txtCustType").equals("F")) ra.setAttribute(ldbName, "InsPolStatChgReport_txtCustTypeDesc", "Fizièke osobe");
        
        return true;
    }
    
    /** Validacijska metoda za organizacijsku jedinicu. */
    public boolean InsPolStatChgReport_OrgUni_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        // poništi sva polja (osim trenutnog) koja su vezana za organizacijsku jedinicu
        ra.setAttribute(ldbName, "ORG_UNI_ID", null);
        if(!elementName.equals("InsPolStatChgReport_txtOrgUniCode")) ra.setAttribute(ldbName, "InsPolStatChgReport_txtOrgUniCode", "");
        if(!elementName.equals("InsPolStatChgReport_txtOrgUniName")) ra.setAttribute(ldbName, "InsPolStatChgReport_txtOrgUniName", "");
        if(elementValue == null || elementValue.equals("")) return true;
    
        // inicijaliziraj i pozovi lookup
        LookUpRequest lu = new LookUpRequest("OrgUniLookUp");
        lu.addMapping(ldbName, "InsPolStatChgReport_txtOrgUniCode", "code");
        lu.addMapping(ldbName, "InsPolStatChgReport_txtOrgUniName", "name");
        lu.addMapping(ldbName, "ORG_UNI_ID", "org_uni_id");
        return callLookUp(lu);
    }
    
    /** Validacijska metoda za osiguravatelja. */
    public boolean InsPolStatChgReport_InsuCompany_FV(String elementName, Object elementValue, Integer LookUp)
    {
        ra.setAttribute(ldbName, "IC_ID", null);  
        ra.setAttribute(ldbName, "dummyStr", null);  
        if (!ra.getCursorPosition().equals("InsPolStatChgReport_txtInsuCompanyCode")) ra.setAttribute(ldbName, "InsPolStatChgReport_txtInsuCompanyCode", null);   
        if (!ra.getCursorPosition().equals("InsPolStatChgReport_txtInsuCompanyName")) ra.setAttribute(ldbName, "InsPolStatChgReport_txtInsuCompanyName", null);
        if (elementValue == null || ((String) elementValue).equals("")) return true;
        
        LookUpRequest lu = new LookUpRequest("InsuCompanyLookup");                                     
        lu.addMapping(ldbName, "IC_ID", "ic_id");
        lu.addMapping(ldbName, "dummyStr", "ic_register_no");
        lu.addMapping(ldbName, "InsPolStatChgReport_txtInsuCompanyName", "ic_name");
        lu.addMapping(ldbName, "InsPolStatChgReport_txtInsuCompanyCode", "ic_code");

        return callLookUp(lu);
    }
    
    /** Validacijska metoda za polja koja vrijednosti dobivaju iz sistemskog šifrarnika. */
    public boolean InsPolStatChgReport_SystemCode_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, elementName, null);
            ra.setAttribute(ldbName, elementName + "Desc", null);
            return true;
        }
        
        String sys_cod_id = "";
        if (elementName.equals("InsPolStatChgReport_txtPolicyType")) sys_cod_id = "ip_type";
        else if (elementName.startsWith("InsPolStatChgReport_txtStatus")) sys_cod_id = "clt_inspolst";
        else if (elementName.startsWith("InsPolStatChgReport_txtWrnStatus")) sys_cod_id = "ip_wrn_status";
        
        final String lookupLDB = "SystemCodeValueLookUpLDB";
        if(!ra.isLDBExists(lookupLDB)) ra.createLDB(lookupLDB);
        ra.setAttribute(lookupLDB, "eng_sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_cod_id", sys_cod_id);
        ra.setAttribute(lookupLDB, "sys_cod_val_id", null);
        ra.setAttribute(lookupLDB, "sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_code_value", ra.getAttribute(ldbName, elementName));
        
        LookUpRequest request = new LookUpRequest("SystemCodeValueLookUp");
        request.addMapping(lookupLDB, "sys_cod_val_id", "sys_cod_val_id");
        request.addMapping(lookupLDB, "sys_code_value", "sys_code_value");
        request.addMapping(lookupLDB, "sys_code_desc", "sys_code_desc");
        if(!callLookUp(request)) return false;
        
        ra.setAttribute(ldbName, elementName, ra.getAttribute(lookupLDB, "sys_code_value"));
        ra.setAttribute(ldbName, elementName + "Desc", ra.getAttribute(lookupLDB, "sys_code_desc"));
        
        return true;
    }
    
    /** 
     * Metoda koja poziva zadani lookup
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
    
    private String isEmpty(Object obj)
    {
        if(obj == null || obj.equals("")) return " ";
        else if(obj instanceof Date) return new SimpleDateFormat("dd.MM.yyyy").format(obj);
        else return obj.toString();
    }
}