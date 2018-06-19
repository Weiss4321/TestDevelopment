package hr.vestigo.modules.collateral;
import java.math.BigDecimal;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;

/**   
 * @author hramkr
 */ 
public class KolCusaccExp extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/KolCusaccExp.java,v 1.2 2012/05/03 11:07:23 hramkr Exp $";
    private String LDBName = "KolCusaccExpLDB";
    
    public KolCusaccExp(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void KolCusaccExp_SE()
    {
        if(!ra.isLDBExists(LDBName)) ra.createLDB(LDBName);
    }
    
        
    public boolean Customer_txt_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
        
        if (elementValue == null || ((String) elementValue).equals("")) {                                                                
            ra.setAttribute(LDBName,"Customer_txt",null); 
            ra.setAttribute(LDBName,"CustomerName_txt",null);
            ra.setAttribute(LDBName,"Customer_Id",null);
            return true;                                                                                                                   
        }                                                                                                                                
                                                                                                                                         
                                                                                                                                 
        String d_register_no = "";                                                                                                       
        if (ra.getAttribute(LDBName, "Customer_txt") != null){                                                   
            d_register_no = (String) ra.getAttribute(LDBName, "Customer_txt");                                     
        }                                                                                                                                
           
        
        if (d_register_no.length() < 4) {                                                                     
            ra.showMessage("wrn366");                                                                                                      
            return false;                                                                                                                  
        }   
                                                                                                                                  
        //JE LI zvjezdica na pravom mjestu kod register_no                                                                               
        if (CharUtil.isAsteriskWrong(d_register_no)) {                                                                                   
            ra.showMessage("wrn367");                                                                                                      
            return false;                                                                                                                  
        }                                                                                                                                
       
         
        if (ra.isLDBExists("CustomerAllCitizenLookUpLDB_1")) {                                                                                 
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id", null);                                                                       
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", "");                                                                    
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", "");                                                                           
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", "");                                                                           
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "add_data_table", "");                                                                 
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "cus_typ_id", null);                                                                   
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "cus_sub_typ_id", null);                                                               
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "eco_sec", null);                                                                      
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "residency_cou_id", null);  
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "status", "");
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "cocunat", "");   
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "oib_lk", "");    
        } else {                                                                                                                         
            ra.createLDB("CustomerAllCitizenLookUpLDB_1");                                                                                          
        }                                                                                                                                
                                                                                                                                       
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute(LDBName,"Customer_txt"));
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ""); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", ""); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", ""); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", ""); 
              
                                                                                                                                     
        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllCitizenLookUp_22");                                                          
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "cus_id", "cus_id");                                                            
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "register_no", "register_no");                                                  
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "code", "code");                                                                
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "name", "name");                                                                
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "add_data_table", "add_data_table");                                            
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "cus_typ_id", "cus_typ_id");                                                    
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "cus_sub_typ_id", "cus_sub_typ_id");                                            
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "eco_sec", "eco_sec");                                                          
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "residency_cou_id", "residency_cou_id"); 
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "fname", "fname");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "surname", "surname");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "status", "status");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "cocunat", "cocunat");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "oib_lk", "oib_lk");
                                                     
                                                                                                                                       
        try {                                                                                                                            
            ra.callLookUp(lookUpRequest);                                                                                                  
        } catch (EmptyLookUp elu) {                                                                                                      
            ra.showMessage("err012");                                                                                                      
            return false;                                                                                                                  
        } catch (NothingSelected ns) {                                                                                                   
            return false;                                                                                                                  
        }                                                                                                                                
  
        ra.setAttribute(LDBName, "Customer_Id",(java.math.BigDecimal) ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));        
        ra.setAttribute(LDBName, "Customer_txt", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));
        ra.setAttribute(LDBName, "CustomerName_txt", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));        
        return true;                                                                                                                     
    }                                                                                                                              

    public void get_exposure()
    {
// mora biti zadan ili komitent ili partija plasmana
        if (checkCondition()==true){
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null) ra.showMessage(vtme.getMessageID());
            }
        }
    }
     
    private boolean checkCondition(){
        if (ra.getAttribute(LDBName,"Customer_Id") == null && 
            (ra.getAttribute(LDBName,"CusAcc_txt") == null || ra.getAttribute(LDBName,"CusAcc_txt").equals(""))) {
            ra.showMessage("wrnclt116"); 
            return false;
        }    
        return true;
    }
}
  