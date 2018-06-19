
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.util.CharUtil;

/**
*
* @author hraaks
*/
public class CollInvestPartiesAll extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollInvestPartiesAll.java,v 1.1 2014/04/03 07:10:59 hraaks Exp $";
    public String ldbName = "CollInvestPartiesAllLDB";  
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    
    
    public CollInvestPartiesAll(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollInvestPartiesAll_SE() 
    {
        System.out.println("CollInvestPartiesAll_SE");
        if(!ra.isLDBExists(ldbName))
            ra.createLDB(ldbName);  
        
    }
    
    
    /** Validacijska metoda za polje s vrstom klijenta. */
    public boolean CollInvestPartiesAll_ClientType_FV(String elementName, Object elementValue, Integer lookUpType)
    {    
        System.out.println(ldbName);
        
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");       
        request.addMapping(ldbName, "CollInvestPartiesAll_txtClientType", "Vrijednosti");
        if(callLookUp(request)){
            String clientType = (String)ra.getAttribute(ldbName, "CollInvestPartiesAll_txtClientType");
            if(clientType.equals("P"))
                ra.setAttribute(ldbName, "CollInvestPartiesAll_txtClientTypeDesc", "Pravne osobe");
            else if(clientType.equals("F"))
                ra.setAttribute(ldbName, "CollInvestPartiesAll_txtClientTypeDesc", "Fizièke osobe");
            return true;
        }else return false;        
    }
    
    
    public void date_check() throws Exception{
        String type = (String)ra.getAttribute(ldbName, "CollInvestPartiesAll_txtClientType");
        Date date = (Date)ra.getAttribute(ldbName, "CollInvestPartiesAll_ReportDate");
        if(type==null || date== null )
            return;
        
        
        try {
            // hr.vestigo.modules.collateral.jcics.co41.CO412.sqlj
            ra.executeTransaction();
            System.out.println("first transaction");
        } catch (Exception e) {
            // TODO: handle exception
        }
        String flag = ra.getAttribute(ldbName, "flag");
        if(flag.equalsIgnoreCase("0")){
            ra.showMessage("CollInvestPartiesAllDateMsg");
            return;
        }
        ra.invokeAction("csv");
    }
    
    
    public void csv() throws Exception{
        String type = (String)ra.getAttribute(ldbName, "CollInvestPartiesAll_txtClientType");
        Date date = (Date)ra.getAttribute(ldbName, "CollInvestPartiesAll_ReportDate");
            
        System.out.println("date:"+ date);
        System.out.println("type:"+ type);
        
        String param = "RB;"+date+";"+type+";"+"X";
        

        if(!ra.isLDBExists("BatchLogDialogLDB")){
        ra.createLDB("BatchLogDialogLDB");
        }
        ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("6529087704"));
        
        ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
        ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
        ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
        ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
        ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
        ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
        Integer answer = null;     
        
        answer = (Integer) ra.showMessage("msgIzvodiConfirm");
        if (answer != null && answer.intValue() == 0) 
                return;
            else{
                    try {
                            ra.executeTransaction();
                         } catch (Exception e) {
                                 System.out.println(e.toString());
                          }
                    }
        ra.showMessage("inf075");
        
        return;
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