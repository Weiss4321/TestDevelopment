package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
//import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Izvješæe o vezi kolateral-plasman.
*
*/
public class CollLoanRelationReport extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollLoanRelationReport.java,v 1.4 2017/10/16 10:35:55 hraaks Exp $";
    private final String ldbName = "CollLoanRelationReportLDB";
    
    public CollLoanRelationReport(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollLoanRelation_SE()
    {
        ra.createLDB(ldbName);
    }

    /** Metoda koja naruèuje batch obradu. */
    public void orderCSV()
    {
        try
        {
            if(!ra.isRequiredFilled()) return;
            String parametri = generateBatchParam();
            BigDecimal batchDefId = new BigDecimal("3004161164");
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
            error("CollLoanRelationReport.orderCSV() -> VestigoTMException", vtme);
        }
    }
    
    /**
     * Metoda koja vraæa formirani string sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;client_type;X
     */
    private String generateBatchParam()
    {
        StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "CollLoanRelation_txtClientType")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "txtBatch_type")).append(";");
        buffer.append("X");
        return buffer.toString();
    }
    
    /** Validacijska metoda za polje s vrstom klijenta. */
    public boolean CollLoanRelation_ClientType_FV(String elementName, Object elementValue, Integer lookUpType)
    {        
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");       
        request.addMapping(ldbName, "CollLoanRelation_txtClientType", "Vrijednosti");
        if(callLookUp(request)){
            String clientType = (String)ra.getAttribute(ldbName, "CollLoanRelation_txtClientType");
            if(clientType.equals("P"))
                ra.setAttribute(ldbName, "CollLoanRelation_txtClientTypeDesc", "Pravne osobe");
            else if(clientType.equals("F"))
                ra.setAttribute(ldbName, "CollLoanRelation_txtClientTypeDesc", "Fizièke osobe");
            return true;
        }else return false;        
    }
    
    public boolean CollLoanRelation_BatchType_FV(String elementName, Object elementValue, Integer lookUpType)
    {        
        
        if (elementValue == null || ((String) elementValue).equals("")) {                                          
            ra.setAttribute(ldbName, "txtBatch_type_desc", "");   
            ra.setAttribute(ldbName, "txtBatch_type", ""); 
            return true;                                                                                 
        }
        
        ra.setAttribute(ldbName, "SysCodId", "coll_rell_bat_type");
        ra.setAttribute(ldbName, "dummySt", null);
        
        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
        request.addMapping(ldbName, "txtBatch_type", "sys_code_value");
        request.addMapping(ldbName, "txtBatch_type_desc", "sys_code_desc");
        request.addMapping(ldbName, "dummySt", "sys_cod_val_id");
   
        //request.addMapping(ldbName, "CollLoanRelation_txtClientType", "coll_rel_batch_type");
        if(callLookUp(request)){
//            String clientType = (String)ra.getAttribute(ldbName, "CollLoanRelation_txtClientType");
//            if(clientType.equals("P"))
//                ra.setAttribute(ldbName, "CollLoanRelation_txtClientTypeDesc", "Pravne osobe");
//            else if(clientType.equals("F"))
//                ra.setAttribute(ldbName, "CollLoanRelation_txtClientTypeDesc", "Fizièke osobe");
            return true;
        }
        else 
            return false;        

     
        
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