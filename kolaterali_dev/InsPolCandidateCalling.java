package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.modules.rba.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Handler klasa za ekran Lista kandidata za pozivanje
 * @author hrakis
 */
public class InsPolCandidateCalling extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsPolCandidateCalling.java,v 1.2 2017/03/22 11:33:38 hrakis Exp $";
    private final String ldbName = "InsPolCandidateCallingLDB";
    
    public InsPolCandidateCalling(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void InsPolCandidateCalling_SE()
    {
        ra.createLDB(ldbName);
        
        ra.setAttribute(ldbName, "InsPolCandidateCalling_txtPolicyType", "P");
        ra.invokeValidation("InsPolCandidateCalling_txtPolicyType");
        
        ra.setAttribute(ldbName, "InsPolCandidateCalling_txtCustType", "F");
        ra.invokeValidation("InsPolCandidateCalling_txtCustType");
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        ra.setAttribute(ldbName, "InsPolCandidateCalling_txtMaxDate", calendar.getTime());
    }

    /** Metoda koja naruèuje batch obradu. */
    public void order()
    {
        try
        {
            if (!ra.isRequiredFilled()) return;
            String parameters = getBatchParameters();
            final String batchLDB = "BatchLogDialogLDB";
            if (!ra.isLDBExists(batchLDB)) ra.createLDB(batchLDB);
            ra.setAttribute(batchLDB, "BatchDefId", new BigDecimal("1215309327"));
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
            error("InsPolCandidateCalling.order() -> VestigoTMException", vtme);
            if (vtme.getMessageID() != null) ra.showMessage(vtme.getMessageID(), vtme.getErrorMessages());
            else ra.showMessage("err411a");
        }
    }
    
    /**
     * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;policy_type;cust_type;max_date
     */
    private String getBatchParameters()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append((String)ra.getAttribute("GDB", "bank_sign")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "InsPolCandidateCalling_txtPolicyType")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "InsPolCandidateCalling_txtCustType")).append(";");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        buffer.append(sdf.format((Date)ra.getAttribute(ldbName, "InsPolCandidateCalling_txtMaxDate")));
        return buffer.toString();
    }

    /** Validacijska metoda za vrstu police osiguranja. */
    public boolean InsPolCandidateCalling_PolicyType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "InsPolCandidateCalling_txtPolicyType", null);
            ra.setAttribute(ldbName, "InsPolCandidateCalling_txtPolicyTypeDesc", null);
            return true;
        }
        
        final String lookupLDB = "SystemCodeValueLookUpLDB";
        if (!ra.isLDBExists(lookupLDB)) ra.createLDB(lookupLDB);
        ra.setAttribute(lookupLDB, "eng_sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_cod_id", "ip_type");
        ra.setAttribute(lookupLDB, "sys_cod_val_id", null);
        ra.setAttribute(lookupLDB, "sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_code_value", ra.getAttribute(ldbName, "InsPolCandidateCalling_txtPolicyType"));
        
        LookUpRequest request = new LookUpRequest("SystemCodeValueLookUp");
        request.addMapping(lookupLDB, "sys_cod_val_id", "sys_cod_val_id");
        request.addMapping(lookupLDB, "sys_code_value", "sys_code_value");
        request.addMapping(lookupLDB, "sys_code_desc", "sys_code_desc");
        
        if (!callLookUp(request)) return false;
        
        ra.setAttribute(ldbName, "InsPolCandidateCalling_txtPolicyType", ra.getAttribute(lookupLDB, "sys_code_value"));
        ra.setAttribute(ldbName, "InsPolCandidateCalling_txtPolicyTypeDesc", ra.getAttribute(lookupLDB, "sys_code_desc"));
        
        return true;
    }
    
    /** Validacijska metoda za vrstu komitenta. */
    public boolean InsPolCandidateCalling_CustType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String)elementValue).equals("")) return true;
        
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");       
        request.addMapping(ldbName, "InsPolCandidateCalling_txtCustType", "Vrijednosti");
        if (!callLookUp(request)) return false;
        
        String cust_type = (String)ra.getAttribute(ldbName, "InsPolCandidateCalling_txtCustType");
        if ("P".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolCandidateCalling_txtCustTypeDesc", "Pravne osobe");
        else if ("F".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolCandidateCalling_txtCustTypeDesc", "Fizi\u010Dke osobe");
        
        return true;
    }
    
    /** Validacijska metoda za maksimalni datum. */
    public boolean InsPolCandidateCalling_MaxDate_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        /*if (elementValue == null) return true;
       
        Calendar calendar = Calendar.getInstance();
        Date date_from = new Date(calendar.getTimeInMillis());
        
        calendar.add(Calendar.DATE, 31);
        Date date_until = new Date(calendar.getTimeInMillis());
        
        Date date = (Date)ra.getAttribute(ldbName, "InsPolCandidateCalling_txtMaxDate");
        if (DateUtils.whoIsOlder(date, date_from) < 0 || DateUtils.whoIsOlder(date, date_until) > 0)
        {
            HashMap map = new HashMap();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            map.put("date_from", sdf.format(date_from));
            map.put("date_until", sdf.format(date_until));
            ra.showMessage("wrnclt163", map);
            return false;
        }*/
       
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