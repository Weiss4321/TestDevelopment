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
* Handler klasa za ekran Priprema kandidata za slanje obavijesti/ugovaranje grupne police
*
*/
public class InsPolCandidateBatch extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsPolCandidateBatch.java,v 1.3 2017/01/27 11:41:24 hrakis Exp $";
    private final String ldbName = "InsPolCandidateBatchLDB";
    
    public InsPolCandidateBatch(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void InsPolCandidateBatch_SE()
    {
        ra.createLDB(ldbName);
        
        ra.setAttribute(ldbName, "InsPolCandidateBatch_txtPolicyType", "P");
        ra.invokeValidation("InsPolCandidateBatch_txtPolicyType");
        
        ra.setAttribute(ldbName, "InsPolCandidateBatch_txtCustType", "F");
        ra.invokeValidation("InsPolCandidateBatch_txtCustType");
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
            ra.setAttribute(batchLDB, "BatchDefId", new BigDecimal("4490036704"));
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
        buffer.append((String)ra.getAttribute(ldbName, "InsPolCandidateBatch_txtPolicyType")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "InsPolCandidateBatch_txtCustType"));
        return buffer.toString();
    }

    /** Validacijska metoda za vrstu police osiguranja. */
    public boolean InsPolCandidateBatch_PolicyType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "InsPolCandidateBatch_txtPolicyType", null);
            ra.setAttribute(ldbName, "InsPolCandidateBatch_txtPolicyTypeDesc", null);
            return true;
        }
        
        final String lookupLDB = "SystemCodeValueLookUpLDB";
        if (!ra.isLDBExists(lookupLDB)) ra.createLDB(lookupLDB);
        ra.setAttribute(lookupLDB, "eng_sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_cod_id", "ip_type");
        ra.setAttribute(lookupLDB, "sys_cod_val_id", null);
        ra.setAttribute(lookupLDB, "sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_code_value", ra.getAttribute(ldbName, "InsPolCandidateBatch_txtPolicyType"));
        
        LookUpRequest request = new LookUpRequest("SystemCodeValueLookUp");
        request.addMapping(lookupLDB, "sys_cod_val_id", "sys_cod_val_id");
        request.addMapping(lookupLDB, "sys_code_value", "sys_code_value");
        request.addMapping(lookupLDB, "sys_code_desc", "sys_code_desc");
        
        if (!callLookUp(request)) return false;
        
        ra.setAttribute(ldbName, "InsPolCandidateBatch_txtPolicyType", ra.getAttribute(lookupLDB, "sys_code_value"));
        ra.setAttribute(ldbName, "InsPolCandidateBatch_txtPolicyTypeDesc", ra.getAttribute(lookupLDB, "sys_code_desc"));
        
        return true;
    }
    
    /** Validacijska metoda za vrstu komitenta. */
    public boolean InsPolCandidateBatch_CustType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String)elementValue).equals("")) return true;
        
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");       
        request.addMapping(ldbName, "InsPolCandidateBatch_txtCustType", "Vrijednosti");
        if (!callLookUp(request)) return false;
        
        String cust_type = (String)ra.getAttribute(ldbName, "InsPolCandidateBatch_txtCustType");
        if ("P".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolCandidateBatch_txtCustTypeDesc", "Pravne osobe");
        else if ("F".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolCandidateBatch_txtCustTypeDesc", "Fizi\u010Dke osobe");
        
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
}