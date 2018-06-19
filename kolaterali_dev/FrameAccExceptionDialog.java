package hr.vestigo.modules.collateral;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Vector;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;


/**   
 * Detalji slobodne partije iz okvira / Detalji višestruko dodane partije.
 * @author hrakis
 */ 
public class FrameAccExceptionDialog extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/FrameAccExceptionDialog.java,v 1.3 2011/04/27 12:49:29 hrakis Exp $";
    private String ldbName = "FrameAccExceptionDialogLDB";

    public FrameAccExceptionDialog(ResourceAccessor arg0)
    {
        super(arg0);
    }

    public void FrameAccExceptionDialog_SE() throws Exception
    {
        if(!ra.isLDBExists(ldbName)) ra.createLDB(ldbName);
        
        ra.setAttribute(ldbName, "FRA_ACC_EXC_ID", ra.getAttribute("FrameAccExceptionLDB", "FRA_ACC_EXC_ID"));
        
        String exception_type = (String)ra.getAttribute("FrameAccExceptionLDB", "EXCEPTION_TYPE");
        ra.setAttribute(ldbName, "EXCEPTION_TYPE", exception_type);
        if("D".equals(exception_type)) ra.setScreenTitle("Detalji višestruko dodane partije");
        else ra.setScreenTitle("Detalji slobodne partije iz okvira");
        
        if(ra.getScreenContext().equals("insertContext")) ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtStatus", "A"); 
        else ra.executeTransaction();
    }
    
    public void confirm()
    {
        try
        {
            if(!ra.isRequiredFilled()) return;
            ra.executeTransaction();
            ra.showMessage("inf020");
            ra.exitScreen();
            try { ra.invokeAction("refresh"); } catch(Exception ex){}
        }
        catch (VestigoTMException vtme)
        {
            if (vtme.getMessageID() != null) ra.showMessage(vtme.getMessageID());
        }
    }
    
    public boolean FrameAccExceptionDialog_Customer_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        ra.setAttribute(ldbName, "CUS_ID", null);
        if(!elementName.equals("FrameAccExceptionDialog_txtRegisterNo")) ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtRegisterNo", "");
        if(!elementName.equals("FrameAccExceptionDialog_txtName")) ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtName", "");
        ra.setAttribute(ldbName, "CUS_ACC_ID", null);
        ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtCusAccNo", "");
        
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
        
        if(elementName.equals("FrameAccExceptionDialog_txtRegisterNo")) ra.setAttribute(lookupLdb, "register_no", ra.getAttribute(ldbName, elementName));
        else if(elementName.equals("FrameAccExceptionDialog_txtName")) ra.setAttribute(lookupLdb, "name", ra.getAttribute(ldbName, elementName));
        
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
        if(!callLookup(lookUpRequest)) return false;
        
        ra.setAttribute(ldbName, "CUS_ID", ra.getAttribute(lookupLdb, "cus_id"));
        ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtName", ra.getAttribute(lookupLdb, "name"));
        ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtRegisterNo", ra.getAttribute(lookupLdb, "register_no"));
        return true;
    }
    
    public boolean FrameAccExceptionDialog_CusAccNo_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        BigDecimal cus_id = (BigDecimal)ra.getAttribute(ldbName, "CUS_ID");
        if (cus_id == null)
        {
            ra.showMessage("wrnclt74");
            return false;
        }

        ra.setAttribute(ldbName, "CUS_ACC_ID", null);
        if (elementValue == null || ((String)elementValue).equals("")) return true;

        final String lookupLdb = "CusaccExposureLookLDB";
        if (!ra.isLDBExists(lookupLdb)) ra.createLDB(lookupLdb);
        ra.setAttribute(lookupLdb, "cus_id", cus_id);
        ra.setAttribute(lookupLdb, "cus_acc_no", ra.getAttribute(ldbName, "FrameAccExceptionDialog_txtCusAccNo")); 

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

        if(!callLookup(lookUpRequest)) return false;

        // u evidenciju slobodnih partija iz okvira ne mogu se unositi samostalne partije
        String exception_type = (String) ra.getAttribute(ldbName, "EXCEPTION_TYPE");
        String frame_cus_acc_no = (String) ra.getAttribute(lookupLdb, "frame_cus_acc_no"); 
        if ("F".equalsIgnoreCase(exception_type) && (frame_cus_acc_no == null || frame_cus_acc_no.trim().equals("")))
        {
            ra.showMessage("wrnclt176");
            return false;
        }

        ra.setAttribute(ldbName, "CUS_ACC_ID", ra.getAttribute(lookupLdb, "cus_acc_id"));
        ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtCusAccNo", ra.getAttribute(lookupLdb, "cus_acc_no"));
        ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtContractNo", ra.getAttribute(lookupLdb, "contract_no"));
                   
        return true;
    }
    
    public boolean FrameAccExceptionDialog_Status_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if(elementValue == null || elementValue.equals("")) return true;
        
        final String lookupLDB = "SystemCodeValueLookUpLDB";
        if(!ra.isLDBExists(lookupLDB)) ra.createLDB(lookupLDB);
        ra.setAttribute(lookupLDB, "eng_sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_cod_id", "penfund_status");
        ra.setAttribute(lookupLDB, "sys_cod_val_id", null);
        ra.setAttribute(lookupLDB, "sys_code_desc", "");
        ra.setAttribute(lookupLDB, "sys_code_value", ra.getAttribute(ldbName, "FrameAccExceptionDialog_txtStatus"));
        
        LookUpRequest lookUpRequest = new LookUpRequest("SystemCodeValueLookUp");
        lookUpRequest.addMapping(lookupLDB, "sys_cod_val_id", "sys_cod_val_id");
        lookUpRequest.addMapping(lookupLDB, "sys_code_value", "sys_code_value");
        lookUpRequest.addMapping(lookupLDB, "sys_code_desc", "sys_code_desc");
        if(!callLookup(lookUpRequest)) return false;
        
        ra.setAttribute(ldbName, "FrameAccExceptionDialog_txtStatus", ra.getAttribute(lookupLDB, "sys_code_value"));
        return true;
    }
    
    private boolean callLookup(LookUpRequest lookUpRequest)
    {
        try
        {
            ra.callLookUp(lookUpRequest);
            return true;
        }
        catch (EmptyLookUp elu)
        {
            ra.showMessage("err012"); 
            return false;
        }
        catch (NothingSelected ns)
        {
            return false;
        }
    }
}