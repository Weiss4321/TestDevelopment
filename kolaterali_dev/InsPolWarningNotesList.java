package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Lista kandidata za slanje obavijesti / ugovaranje grupne police
*
*/
public class InsPolWarningNotesList extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsPolWarningNotesList.java,v 1.6 2017/02/07 12:34:51 hrakis Exp $";
    private final String ldbName = "InsPolWarningNotesListLDB";
    
    public InsPolWarningNotesList(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void InsPolWarningNotesList_SE() throws Exception
    {
        ra.createLDB(ldbName);
        ra.createActionListSession("tblInsPolWarningNotes", false);
        
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtPolicyType", "P");
        ra.invokeValidation("InsPolWarningNotesList_txtPolicyType");
        
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCustType", "F");
        ra.invokeValidation("InsPolWarningNotesList_txtCustType");
    }
    
    public void search()
    {
        if(!ra.isRequiredFilled()) return;
        ra.refreshActionList("tblInsPolWarningNotes");
    }
    
    public void stop()
    {
        if(!prepareForAction()) return;
        ra.setAttribute(ldbName, "CANDIDATE_STATUS", "N");
        try {
            ra.executeTransaction();
        }
        catch(VestigoTMException ex) {
            if (ex.getMessageID() != null) ra.showMessage(ex.getMessageID(), ex.getErrorMessages());
            else ra.showMessage("err411a");
        }
        ra.refreshActionList("tblInsPolWarningNotes");        
    }
    
    public void send()
    {
        if(!prepareForAction()) return;
        ra.setAttribute(ldbName, "CANDIDATE_STATUS", "A");
        try {
            ra.executeTransaction();
        }
        catch(VestigoTMException ex) {
            if (ex.getMessageID() != null) ra.showMessage(ex.getMessageID(), ex.getErrorMessages());
            else ra.showMessage("err411a");
        }
        ra.refreshActionList("tblInsPolWarningNotes");        
    }
    
    public void stop_all()
    {
        if(!ra.isRequiredFilled()) return;
        if(ra.showMessage("qer042").equals(ResourceAccessor.CONFIRM_MESSAGE_NO_BUTTON_SELECTED)) return;
        ra.setAttribute(ldbName, "CANDIDATE_STATUS", "N");
        ra.setAttribute(ldbName, "INS_WAR_NOT_ID", null);
        try {
            ra.executeTransaction();
        }
        catch(VestigoTMException ex) {
            if (ex.getMessageID() != null) ra.showMessage(ex.getMessageID(), ex.getErrorMessages());
            else ra.showMessage("err411a");
        }
        ra.refreshActionList("tblInsPolWarningNotes");
    }
    
    public void send_all()
    {
        if(!ra.isRequiredFilled()) return;
        if(ra.showMessage("qer042").equals(ResourceAccessor.CONFIRM_MESSAGE_NO_BUTTON_SELECTED)) return;
        ra.setAttribute(ldbName, "CANDIDATE_STATUS", "A");
        ra.setAttribute(ldbName, "INS_WAR_NOT_ID", null);
        try {
            ra.executeTransaction();
        }
        catch(VestigoTMException ex) {
            if (ex.getMessageID() != null) ra.showMessage(ex.getMessageID(), ex.getErrorMessages());
            else ra.showMessage("err411a");
        }
        ra.refreshActionList("tblInsPolWarningNotes");       
    }
    
    
    private boolean prepareForAction()
    {
        TableData tableData = (TableData)ra.getAttribute(ldbName, "tblInsPolWarningNotes");
        if(tableData == null || tableData.isTableEmpty())
        {
            ra.showMessage("wrn299");
            return false;
        }
        Vector hidden = (Vector)tableData.getSelectedRowUnique();
        ra.setAttribute(ldbName, "INS_WAR_NOT_ID", hidden.get(0));
        return true;
    }
    
    public void orderCSV()
    {
        try
        {
            if(!ra.isRequiredFilled()) return;
            String parameters = getBatchParameters();
            final String batchLDB = "BatchLogDialogLDB";
            if(!ra.isLDBExists(batchLDB)) ra.createLDB(batchLDB);
            ra.setAttribute(batchLDB, "BatchDefId", new BigDecimal("4668827704"));
            ra.setAttribute(batchLDB, "RepDefId", null);
            ra.setAttribute(batchLDB, "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(batchLDB, "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute(batchLDB, "fldParamValue", parameters);
            ra.setAttribute(batchLDB, "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute(batchLDB, "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");  
        }
        catch (VestigoTMException vtme)
        {
            error("InsPolWarningNotesList.order() -> VestigoTMException", vtme);
        }
    }
    
    /**
     * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;policy_type;cust_type
     */
    private String getBatchParameters()
    {
        StringBuffer buffer = new StringBuffer();        
        buffer.append((String)ra.getAttribute("GDB", "bank_sign")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "InsPolWarningNotesList_txtPolicyType")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "InsPolWarningNotesList_txtCustType"));
        return buffer.toString();
    }
    
    
    
    /** Validacijska metoda za vrstu komitenta. */
    public boolean InsPolWarningNotesList_CustType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCustTypeDesc", null);
        if (elementValue == null || ((String)elementValue).equals("")) return true;
        
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");
        request.addMapping(ldbName, "InsPolWarningNotesList_txtCustType", "Vrijednosti");
        if(!callLookUp(request)) return false;
        
        String cust_type = (String)ra.getAttribute(ldbName, "InsPolWarningNotesList_txtCustType");
        if ("P".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCustTypeDesc", "Pravne osobe");
        else if ("F".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCustTypeDesc", "Fizi\u010Dke osobe");
        
        return true;
    }
    
    /** Validacijska metoda za organizacijsku jedinicu. */
    public boolean InsPolWarningNotesList_OrgUni_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        // poništi sva polja (osim trenutnog) koja su vezana za organizacijsku jedinicu
        ra.setAttribute(ldbName, "ORG_UNI_ID", null);
        if(!elementName.equals("InsPolWarningNotesList_txtOrgUniCode")) ra.setAttribute(ldbName, "InsPolWarningNotesList_txtOrgUniCode", "");
        if(!elementName.equals("InsPolWarningNotesList_txtOrgUniName")) ra.setAttribute(ldbName, "InsPolWarningNotesList_txtOrgUniName", "");
        if(elementValue == null || elementValue.equals("")) return true;
    
        // inicijaliziraj i pozovi lookup
        LookUpRequest lu = new LookUpRequest("OrgUniLookUp");
        lu.addMapping(ldbName, "InsPolWarningNotesList_txtOrgUniCode", "code");
        lu.addMapping(ldbName, "InsPolWarningNotesList_txtOrgUniName", "name");
        lu.addMapping(ldbName, "ORG_UNI_ID", "org_uni_id");
        return callLookUp(lu);
    }
    
    /** Validacijska metoda za vrstu police osiguranja. */
    public boolean InsPolWarningNotesList_PolicyType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "InsPolWarningNotesList_txtPolicyType", null);
            ra.setAttribute(ldbName, "InsPolWarningNotesList_txtPolicyTypeDesc", null);
            return true;
        }
        
        final String lookupLDB = "SystemCodeValueLookUpLDB";
        if(!ra.isLDBExists(lookupLDB)) ra.createLDB(lookupLDB);
        ra.setAttribute(lookupLDB, "eng_sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_cod_id", "ip_type");
        ra.setAttribute(lookupLDB, "sys_cod_val_id", null);
        ra.setAttribute(lookupLDB, "sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_code_value", ra.getAttribute(ldbName, "InsPolWarningNotesList_txtPolicyType"));
        
        LookUpRequest request = new LookUpRequest("SystemCodeValueLookUp");
        request.addMapping(lookupLDB, "sys_cod_val_id", "sys_cod_val_id");
        request.addMapping(lookupLDB, "sys_code_value", "sys_code_value");
        request.addMapping(lookupLDB, "sys_code_desc", "sys_code_desc");
        
        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtPolicyType", ra.getAttribute(lookupLDB, "sys_code_value"));
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtPolicyTypeDesc", ra.getAttribute(lookupLDB, "sys_code_desc"));
        
        return true;
    }
    
    /** Validacijska metoda za osiguravatelja. */
    public boolean InsPolWarningNotesList_InsuCompany_FV(String elementName, Object elementValue, Integer LookUp)
    {
        ra.setAttribute(ldbName, "IC_ID", null);  
        ra.setAttribute(ldbName, "dummyStr", null);  
        if (!ra.getCursorPosition().equals("InsPolWarningNotesList_txtInsuCompanyCode")) ra.setAttribute(ldbName, "InsPolWarningNotesList_txtInsuCompanyCode", null);   
        if (!ra.getCursorPosition().equals("InsPolWarningNotesList_txtInsuCompanyName")) ra.setAttribute(ldbName, "InsPolWarningNotesList_txtInsuCompanyName", null);
        if (elementValue == null || ((String) elementValue).equals("")) return true;
        
        LookUpRequest lu = new LookUpRequest("InsuCompanyLookup");                                     
        lu.addMapping(ldbName, "IC_ID", "ic_id");
        lu.addMapping(ldbName, "dummyStr", "ic_register_no");
        lu.addMapping(ldbName, "InsPolWarningNotesList_txtInsuCompanyName", "ic_name");
        lu.addMapping(ldbName, "InsPolWarningNotesList_txtInsuCompanyCode", "ic_code");

        return callLookUp(lu);
    }

    /** Validacijska metoda za status obavijesti/opomene. */
    public boolean InsPolWarningNotesList_WrnStatus_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "InsPolWarningNotesList_txtWrnStatus", null);
            ra.setAttribute(ldbName, "InsPolWarningNotesList_txtWrnStatusDesc", null);
            return true;
        }
        
        final String lookupLDB = "SystemCodeValueLookUpLDB";
        if(!ra.isLDBExists(lookupLDB)) ra.createLDB(lookupLDB);
        ra.setAttribute(lookupLDB, "eng_sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_cod_id", "ip_wrn_status");
        ra.setAttribute(lookupLDB, "sys_cod_val_id", null);
        ra.setAttribute(lookupLDB, "sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_code_value", ra.getAttribute(ldbName, "InsPolWarningNotesList_txtWrnStatus"));
        
        LookUpRequest request = new LookUpRequest("SystemCodeValueLookUp");
        request.addMapping(lookupLDB, "sys_cod_val_id", "sys_cod_val_id");
        request.addMapping(lookupLDB, "sys_code_value", "sys_code_value");
        request.addMapping(lookupLDB, "sys_code_desc", "sys_code_desc");
        if(!callLookUp(request)) return false;
       
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtWrnStatus", ra.getAttribute(lookupLDB, "sys_code_value"));
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtWrnStatusDesc", ra.getAttribute(lookupLDB, "sys_code_desc"));
        
        return true;
    }
    
    
    /** Validacijska metoda za status slanja obavijesti/opomene. */
    public boolean InsPolWarningNotesList_CandidateStatus_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCandidateStatus", null);
            ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCandidateStatusDesc", null);
            return true;
        }
        
        final String lookupLDB = "SystemCodeValueLookUpLDB";
        if(!ra.isLDBExists(lookupLDB)) ra.createLDB(lookupLDB);
        ra.setAttribute(lookupLDB, "eng_sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_cod_id", "ip_candidate_stat");
        ra.setAttribute(lookupLDB, "sys_cod_val_id", null);
        ra.setAttribute(lookupLDB, "sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_code_value", ra.getAttribute(ldbName, "InsPolWarningNotesList_txtCandidateStatus"));
        
        LookUpRequest request = new LookUpRequest("SystemCodeValueLookUp");
        request.addMapping(lookupLDB, "sys_cod_val_id", "sys_cod_val_id");
        request.addMapping(lookupLDB, "sys_code_value", "sys_code_value");
        request.addMapping(lookupLDB, "sys_code_desc", "sys_code_desc");
        if(!callLookUp(request)) return false;
       
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCandidateStatus", ra.getAttribute(lookupLDB, "sys_code_value"));
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCandidateStatusDesc", ra.getAttribute(lookupLDB, "sys_code_desc"));
        
        return true;
    }
    
    
    /** Validacijska metoda za vlasnika plasmana. */
    public boolean InsPolWarningNotesList_Customer_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        ra.setAttribute(ldbName, "CUS_ID", null);
        if(!elementName.equals("InsPolWarningNotesList_txtCustRegisterNo")) ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCustRegisterNo", "");
        if(!elementName.equals("InsPolWarningNotesList_txtCustName")) ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCustName", "");
        ra.setAttribute(ldbName, "CUS_ACC_ID", null);
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCusAccNo", "");
        
        if(elementValue == null || elementValue.equals("")) return true;
        
        final String lookupLdb = "CustomerAllLookUpLDB";
        if(!ra.isLDBExists(lookupLdb))
        {
            ra.createLDB(lookupLdb);
        }
        else
        {
            ra.setAttribute(lookupLdb, "cus_id", null);
            ra.setAttribute(lookupLdb, "register_no", "");
            ra.setAttribute(lookupLdb, "code", "");
            ra.setAttribute(lookupLdb, "name", "");
            ra.setAttribute(lookupLdb, "add_data_table", "");
            ra.setAttribute(lookupLdb, "cus_typ_id", null);
            ra.setAttribute(lookupLdb, "cus_sub_typ_id", null);
            ra.setAttribute(lookupLdb, "eco_sec", "");
            ra.setAttribute(lookupLdb, "residency_cou_id", null);
        }
        
        if(elementName.equals("InsPolWarningNotesList_txtCustRegisterNo")) ra.setAttribute(lookupLdb, "register_no", ra.getAttribute(ldbName, elementName));
        else if(elementName.equals("InsPolWarningNotesList_txtCustName")) ra.setAttribute(lookupLdb, "name", ra.getAttribute(ldbName, elementName));
        
        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
        lookUpRequest.addMapping(lookupLdb, "cus_id", "cus_id");
        lookUpRequest.addMapping(lookupLdb, "register_no", "register_no");
        lookUpRequest.addMapping(lookupLdb, "code", "code");
        lookUpRequest.addMapping(lookupLdb, "name", "name");
        lookUpRequest.addMapping(lookupLdb, "add_data_table", "add_data_table");
        lookUpRequest.addMapping(lookupLdb, "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping(lookupLdb, "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping(lookupLdb, "eco_sec", "eco_sec");
        lookUpRequest.addMapping(lookupLdb, "residency_cou_id", "residency_cou_id");
        if(!callLookUp(lookUpRequest)) return false;
        
        ra.setAttribute(ldbName, "CUS_ID", ra.getAttribute(lookupLdb, "cus_id"));
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCustName", ra.getAttribute(lookupLdb, "name"));
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCustRegisterNo", ra.getAttribute(lookupLdb, "register_no"));
        return true;
    }
    
    
    /** Validacijska metoda za partiju plasmana. */
    public boolean InsPolWarningNotesList_CusAccNo_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        BigDecimal cus_id = (BigDecimal)ra.getAttribute(ldbName, "CUS_ID");

        ra.setAttribute(ldbName, "CUS_ACC_ID", null);
        if (elementValue == null || ((String)elementValue).equals("")) return true;

        final String lookupLdb = "CusaccExposureLookLDB";
        if (!ra.isLDBExists(lookupLdb)) ra.createLDB(lookupLdb);
        ra.setAttribute(lookupLdb, "cus_id", cus_id);
        ra.setAttribute(lookupLdb, "cus_acc_no", ra.getAttribute(ldbName, "InsPolWarningNotesList_txtCusAccNo")); 

        LookUpRequest lookUpRequest = new LookUpRequest("CusaccExposureLookUp");
        lookUpRequest.addMapping(lookupLdb, "cus_acc_id", "cus_acc_id");
        lookUpRequest.addMapping(lookupLdb, "cus_acc_no", "cus_acc_no");
        lookUpRequest.addMapping(lookupLdb, "contract_no", "contract_no");
        lookUpRequest.addMapping(lookupLdb, "cus_acc_status", "cus_acc_status");
        lookUpRequest.addMapping(lookupLdb, "cus_acc_orig_st", "cus_acc_orig_st");
        lookUpRequest.addMapping(lookupLdb, "frame_cus_acc_no", "frame_cus_acc_no");
        lookUpRequest.addMapping(lookupLdb, "exposure_cur_id", "exposure_cur_id");
        lookUpRequest.addMapping(lookupLdb, "code_char", "code_char");
        lookUpRequest.addMapping(lookupLdb, "exposure_balance", "exposure_balance");
        lookUpRequest.addMapping(lookupLdb, "exposure_date", "exposure_date");
        lookUpRequest.addMapping(lookupLdb, "request_no", "request_no");
        lookUpRequest.addMapping(lookupLdb, "prod_code", "prod_code");
        lookUpRequest.addMapping(lookupLdb, "name", "name");
        lookUpRequest.addMapping(lookupLdb, "module_code", "module_code");

        if(!callLookUp(lookUpRequest)) return false;

        ra.setAttribute(ldbName, "CUS_ACC_ID", ra.getAttribute(lookupLdb, "cus_acc_id"));
        ra.setAttribute(ldbName, "InsPolWarningNotesList_txtCusAccNo", ra.getAttribute(lookupLdb, "cus_acc_no"));
                   
        return true;
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
}