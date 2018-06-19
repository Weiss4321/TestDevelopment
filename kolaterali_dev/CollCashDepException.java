//created 2010.12.06
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;

/**
*
* @author hramkr
*/
public class CollCashDepException extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollCashDepException.java,v 1.3 2015/02/20 13:33:04 hrazst Exp $";
    
    public CollCashDepException(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    
    public boolean CollCashDepExceptionQBE_txtOwnerQBE_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = "CollCashDepExceptionLDB";
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"CollCashDepExceptionQBE_register_no",null);
            ra.setAttribute(ldbName,"CollCashDepExceptionQBE_owner_name",null);
            ra.setAttribute(ldbName,"cus_id_qbe",null);
            return true;
        }
        
        if (ra.getCursorPosition().equals("CollCashDepExceptionQBE_owner_name")) {
            ra.setAttribute(ldbName, "CollCashDepExceptionQBE_register_no", "");
        } else if (ra.getCursorPosition().equals("CollCashDepExceptionQBE_register_no")) {
            ra.setAttribute(ldbName, "CollCashDepExceptionQBE_owner_name", "");
        }
     
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollCashDepExceptionQBE_register_no") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "CollCashDepExceptionQBE_register_no");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "CollCashDepExceptionQBE_owner_name") != null){
            d_name = (String) ra.getAttribute(ldbName, "CollCashDepExceptionQBE_owner_name");
        }
        

        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }
        
        //JE LI zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }
        
           
         if (ra.getCursorPosition().equals("CollCashDepExceptionQBE_register_no")) 
            ra.setCursorPosition(2);
                    
        
         if (ra.isLDBExists("CustColAllLookUpLDB")) {//ovo ne dirati                                                                                    
             ra.setAttribute("CustColAllLookUpLDB", "cus_id", null);                                                                       
             ra.setAttribute("CustColAllLookUpLDB", "register_no", "");                                                                    
             ra.setAttribute("CustColAllLookUpLDB", "code", "");                                                                           
             ra.setAttribute("CustColAllLookUpLDB", "name", "");                                                                           
             ra.setAttribute("CustColAllLookUpLDB", "add_data_table", "");                                                                 
             ra.setAttribute("CustColAllLookUpLDB", "cus_typ_id", null);                                                                   
             ra.setAttribute("CustColAllLookUpLDB", "cus_sub_typ_id", null);                                                               
             ra.setAttribute("CustColAllLookUpLDB", "eco_sec", null);                                                                      
             ra.setAttribute("CustColAllLookUpLDB", "residency_cou_id", null);  
             ra.setAttribute("CustColAllLookUpLDB", "ModuleName", null);            
             ra.setAttribute("CustColAllLookUpLDB", "in_status", "");
             ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_id", "");   
             ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_flag", "");
             ra.setAttribute("CustColAllLookUpLDB", "tax_number", ""); 
         } else {                                                                                                                         
             ra.createLDB("CustColAllLookUpLDB"); 
         }  

        ra.setAttribute("CustColAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "CollCashDepExceptionQBE_register_no"));
        ra.setAttribute("CustColAllLookUpLDB", "name", ra.getAttribute(ldbName, "CollCashDepExceptionQBE_owner_name"));
        
//      LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp");
        LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "register_no", "register_no");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "residency_cou_id", "residency_cou_id");
    
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        
        ra.setAttribute(ldbName, "cus_id_qbe", ra.getAttribute("CustColAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "CollCashDepExceptionQBE_register_no", ra.getAttribute("CustColAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "CollCashDepExceptionQBE_owner_name", ra.getAttribute("CustColAllLookUpLDB", "name"));
            
        return true;
 
        
    }   
    
    public void CollCashDepException_SE()
    {
        if (!(ra.isLDBExists("CollCashDepExceptionLDB"))) {
            ra.createLDB("CollCashDepExceptionLDB");
        }    
        ra.createActionListSession("tblCollCashDepException", true);
    }
    
    public void details()           // F4
    {
        if (isTableEmpty("tblCollCashDepException")) {
            ra.showMessage("wrn299");
            return;
        }
        TableData td=null;
        td = (TableData) ra.getAttribute("CollCashDepExceptionLDB", "tblCollCashDepException");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal cas_exc_id = (BigDecimal) hidden.elementAt(0);
        ra.setAttribute("CollCashDepExceptionLDB", "cas_exc_id", cas_exc_id);    
        ra.loadScreen("CollCashDepExceptionDialog", "scr_detail");   
 
       try {
           ra.executeTransaction();
       } catch (VestigoTMException vtme) {
           if (vtme.getMessageID() != null)
               ra.showMessage(vtme.getMessageID());
       }
//        ra.loadScreen("CollCashDepExceptionDialog", "scr_detail");
    }
 
    public void change()           // F4
        {
            if (isTableEmpty("tblCollCashDepException")) {           
                ra.showMessage("wrn299");
            return;
        }
        TableData td=null;
        td = (TableData) ra.getAttribute("CollCashDepExceptionLDB", "tblCollCashDepException");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal cas_exc_id = (BigDecimal) hidden.elementAt(0);
        
        ra.setAttribute("CollCashDepExceptionLDB", "cas_exc_id", cas_exc_id);   
        ra.loadScreen("CollCashDepExceptionDialog", "scr_update");
       
        try {
            ra.executeTransaction();
        } catch (VestigoTMException vtme) {
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }

    }
    public void query_by_example()  // F5
    {
        ra.setScreenContext("scr_qbe");
        ra.setCursorPosition("CollCashDepExceptionQBE_txtCus_Acc_No");
    }
    
    public void refresh()           // Shift-F5 
    {
//        ra.refreshActionList("tblCollCashDepException");
        ra.createActionListSession("tblCollCashDepException", true);
    }

    public void add()               // F7
    {
        ra.loadScreen("CollCashDepExceptionDialog", "scr_insert");
    }
       
    public void search()
    {
        System.out.println("partija : "+ra.getAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtCus_Acc_No"));
        System.out.println("od : "+ra.getAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtDateFrom"));
        System.out.println("do : "+ra.getAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtDateUntil"));
        
        if(ra.getAttribute("CollCashDepExceptionLDB", "cus_id_qbe")!=null){
            ra.performQueryByExample("tblCollCashDepException");    
        }
        else if (ra.getAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtCus_Acc_No") == null || 
            ((String) ra.getAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtCus_Acc_No")).equals("") ) 
        {
            if (ra.getAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtDateFrom" ) == null ||
                ra.getAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtDateUntil" ) == null )
                ra.showMessage("wrnclt172");
            else {
                System.out.println("tu sam 1 :........ ");
                ra.performQueryByExample("tblCollCashDepException");              
            }
        } else {
            System.out.println("tu sam 2 :........ ");
            ra.performQueryByExample("tblCollCashDepException");
        }
    }
    public void exit()               // F7
    {
        if (ra.getScreenContext().equals("scr_qbe")) {
            ra.setScreenContext("defaultContext");
            ra.setAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtCus_Acc_No",null);
            ra.setAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtDateFrom",null);
            ra.setAttribute("CollCashDepExceptionLDB", "CollCashDepExceptionQBE_txtDateUntil",null);         
        } else
            ra.exitScreen();
    }
    
    public boolean isTableEmpty(String tableName)
    {
        TableData td = (TableData) ra.getAttribute(tableName);
        if (td == null) return true;
        else if (td.getData().size() == 0) return true;
        else return false; 
    }
}