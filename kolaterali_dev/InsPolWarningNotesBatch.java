package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Priprema datoteke za slanje obavijesti/ugovaranje grupne police
*
*/
public class InsPolWarningNotesBatch extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsPolWarningNotesBatch.java,v 1.3 2017/02/07 12:34:51 hrakis Exp $";
    private final String ldbName = "InsPolWarningNotesBatchLDB";
    
    public InsPolWarningNotesBatch(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void InsPolWarningNotesBatch_SE()
    {
        ra.createLDB(ldbName);
        
        ra.setAttribute(ldbName, "InsPolWarningNotesBatch_txtPolicyType", "P");
        ra.invokeValidation("InsPolWarningNotesBatch_txtPolicyType");
        
        ra.setAttribute(ldbName, "InsPolWarningNotesBatch_txtCustType", "F");
        ra.invokeValidation("InsPolWarningNotesBatch_txtCustType");
    }

    
    /** Metoda koja naruèuje batch obradu. */
    public void order()
    {
        try
        {
            // if(!ra.isRequiredFilled()) return;
            String parameters = getBatchParameters();
            final String batchLDB = "BatchLogDialogLDB";
            if (!ra.isLDBExists(batchLDB)) ra.createLDB(batchLDB);
            ra.setAttribute(batchLDB, "BatchDefId", new BigDecimal("4673995704"));
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
            error("InsPolCandidateBatch.order() -> VestigoTMException", vtme);
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
        buffer.append((String)ra.getAttribute(ldbName, "InsPolWarningNotesBatch_txtPolicyType")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "InsPolWarningNotesBatch_txtCustType"));
        return buffer.toString();
    }
    

    /** Validacijska metoda za vrstu police osiguranja. */
    public boolean InsPolWarningNotesBatch_PolicyType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "InsPolWarningNotesBatch_txtPolicyType", null);
            ra.setAttribute(ldbName, "InsPolWarningNotesBatch_txtPolicyTypeDesc", null);
            return true;
        }
        
        final String lookupLDB = "SystemCodeValueLookUpLDB";
        if(!ra.isLDBExists(lookupLDB)) ra.createLDB(lookupLDB);
        ra.setAttribute(lookupLDB, "eng_sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_cod_id", "ip_type");
        ra.setAttribute(lookupLDB, "sys_cod_val_id", null);
        ra.setAttribute(lookupLDB, "sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_code_value", ra.getAttribute(ldbName, "InsPolWarningNotesBatch_txtPolicyType"));
        
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
        
        ra.setAttribute(ldbName, "InsPolWarningNotesBatch_txtPolicyType", ra.getAttribute(lookupLDB, "sys_code_value"));
        ra.setAttribute(ldbName, "InsPolWarningNotesBatch_txtPolicyTypeDesc", ra.getAttribute(lookupLDB, "sys_code_desc"));
        
        return true;
    }
    
    /** Validacijska metoda za vrstu komitenta. */
    public boolean InsPolWarningNotesBatch_CustType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");       
        request.addMapping(ldbName, "InsPolWarningNotesBatch_txtCustType", "Vrijednosti");
        if (!callLookUp(request)) return false;
        
        String cust_type = (String)ra.getAttribute(ldbName, "InsPolWarningNotesBatch_txtCustType");
        if ("P".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolWarningNotesBatch_txtCustTypeDesc", "Pravne osobe");
        else if ("F".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolWarningNotesBatch_txtCustTypeDesc", "Fizi\u010Dke osobe");
        
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