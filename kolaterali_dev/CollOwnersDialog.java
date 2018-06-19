/*
 * Created on 2006.04.25
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
  
  
/**
 * @author hrarmv
 *  
 */ 
public class CollOwnersDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollOwnersDialog.java,v 1.42 2017/07/19 08:04:20 hrazst Exp $";
    CollateralUtil coll_util= null; 
    public CollOwnersDialog(ResourceAccessor ra) {
        super(ra);
        coll_util = new CollateralUtil(ra);
    }

    public void CollOwnersDialog_SE() {
        if (!ra.isLDBExists("CollOwnersDialogLDB")) {
            ra.createLDB("CollOwnersDialogLDB");
        }
        if (!ra.isLDBExists("Collateral_blValidDatesLDB")) {
            ra.createLDB("Collateral_blValidDatesLDB");
        }
        if (!ra.isLDBExists("Collateral_blUserFieldLDB")) {
            ra.createLDB("Collateral_blUserFieldLDB");
        }
        setUserData();
        if ((ra.getScreenContext().compareTo("scr_insert") == 0)
                || (ra.getScreenContext().compareTo("scr_action") == 0)) {
            String ldbName = "CollOwnersDialogLDB";
            
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo", "");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_CUS_ID", null);
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName", "");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtFirstName", "");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtSurname", "");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode", "");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_PART_ID", null);
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtUseCodeValue", "");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtUseCodeDesc", "");
 
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNum", "");  
// za cesije postaviti u vlasnika cesus-a            
            setOwnerForCes();
        }
          
        String vrsta = (String) ra.getAttribute("ColWorkListLDB","code");            
        
        if ((ra.getScreenContext().compareTo("scr_insert") == 0) || (ra.getScreenContext().compareTo("scr_action") == 0)) {
// samo za cash depozite - moze biti samo jedan vlasnik   
// 23.10.2008, dodani dionice,obveznice,udjeli,zapisi, udjeli u poduzecu - i oni mogu imati samo jednog vlasnika    
            if (vrsta.equalsIgnoreCase("CASH") || vrsta.equalsIgnoreCase("DION") || 
                    vrsta.equalsIgnoreCase("OBVE") || vrsta.equalsIgnoreCase("UDJE") ||
                    vrsta.equalsIgnoreCase("UDJP") || vrsta.equalsIgnoreCase("ZAPI") ||  vrsta.equalsIgnoreCase("CESI")) {          
                ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtUseCodeValue", "1");
                ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtUseCodeDesc",  "1");
                ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNum", "1");    
// zaprotektirati ta polja
                 
                ra.setContext("CollOwnersDialog_txtUseCodeValue", "fld_protected");
                ra.setContext("CollOwnersDialog_txtUseCodeDesc", "fld_protected");
                ra.setContext("Kol_txtOwnNum", "fld_protected");
                
            } else {
                ra.setContext("CollOwnersDialog_txtUseCodeValue", "fld_plain");
                ra.setContext("CollOwnersDialog_txtUseCodeDesc", "fld_plain");
                ra.setContext("Kol_txtOwnNum", "fld_plain");            
            } 
        }
          
        if(ra.getScreenContext().compareTo("scr_action") == 0 || ra.getScreenContext().compareTo("scr_activate") == 0) {
            TableData tda = (TableData) ra.getAttribute("CollOwnersLDB", "tblCollOwners");
            Vector hiddenVector = (Vector) tda.getSelectedRowUnique();
            BigDecimal coll_own_id = (BigDecimal) hiddenVector.elementAt(0);
            //setiranje dohvacenog id na LDB
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_COLL_OWN_ID", coll_own_id);
        }
        
        if (ra.getScreenContext().compareTo("scr_detail") == 0 || ra.getScreenContext().compareTo("scr_action") == 0 ||
                ra.getScreenContext().compareTo("scr_activate") == 0 ) {

            TableData td = (TableData) ra.getAttribute("CollOwnersLDB", "tblCollOwners");
            Vector hiddenVector = (Vector) td.getSelectedRowUnique();

              
           BigDecimal cusomerCusId = (java.math.BigDecimal)hiddenVector.elementAt(1);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_CUS_ID", cusomerCusId);
           
/*           String firstName = (String) hiddenVector.elementAt(2);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtFirstName", firstName);
           
           String surname = (String) hiddenVector.elementAt(3);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtSurname", surname);*/
           
           
           String code = (String) hiddenVector.elementAt(2);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode", code);
           
           String userOpenLogin = (String) hiddenVector.elementAt(3);
           String userOpenName = (String) hiddenVector.elementAt(4);
           ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserOpenIdLogin", userOpenLogin);
           ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserOpenIdName", userOpenName);
          
           String userLogin = (String) hiddenVector.elementAt(5);
           String userName = (String) hiddenVector.elementAt(6);
           ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserIdLogin", userLogin);
           ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserIdName", userName);
           
           
           Timestamp userLockOpen = ( java.sql.Timestamp) hiddenVector.elementAt(7);
           ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserLockOpenField", userLockOpen);
           
           
           Timestamp userLock = ( java.sql.Timestamp) hiddenVector.elementAt(8);
           ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserLockField", userLock);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_USER_LOCK_NF", userLock);

           
           
           Date dateFrom = (Date) hiddenVector.elementAt(9);
           Date dateUntil = (Date) hiddenVector.elementAt(10);
           ra.setAttribute("Collateral_blValidDatesLDB", "Collateral_txtDateFrom", dateFrom);
           ra.setAttribute("Collateral_blValidDatesLDB", "Collateral_txtDateUntil", dateUntil);

           String brojnik = (String) hiddenVector.elementAt(11);
           String nazivnik = (String) hiddenVector.elementAt(12);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtUseCodeValue", brojnik);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtUseCodeDesc", nazivnik);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_PART_ID", brojnik);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_PART_ID2", nazivnik);
           
           BigDecimal broj_vlasnika = (BigDecimal) hiddenVector.elementAt(13);
           ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNum", broj_vlasnika); 
           
           String broj_nekretnina_dsc = (String) hiddenVector.elementAt(14);
           ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstateDsc", broj_nekretnina_dsc); 
 
           BigDecimal cus_typ_id  = (BigDecimal) hiddenVector.elementAt(15);
           ra.setAttribute("CollOwnersDialogLDB", "cus_typ_id", cus_typ_id);
           
           BigDecimal statement_owner_cus_id  = (BigDecimal) hiddenVector.elementAt(16);
           ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementUseId", statement_owner_cus_id);
           String statement_owner_register_no = (String) hiddenVector.elementAt(17);
           ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo", statement_owner_register_no);
           String statement_owner_name  = (String) hiddenVector.elementAt(18);
           ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementName", statement_owner_name); 
           String statement_owner_oib  = (String) hiddenVector.elementAt(19);
           ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementOIB", statement_owner_oib); 
           
           //VIDLJIVI
           Vector row = td.getSelectedRowData();
           String registerNo = (String) row.elementAt(0);
           String oib = (String) row.elementAt(1);
           String customerName = (String) row.elementAt(2);
           String partId = (String) row.elementAt(3);
           String broj_nekretnina_u_vlasnistvu = (String) row.elementAt(5);
           String main_owner = (String) row.elementAt(6);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo", registerNo);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtOIB", oib);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName", customerName);
           ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_PART_ID", partId);
           ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstate", broj_nekretnina_u_vlasnistvu);  
           ra.setAttribute("CollOwnersDialogLDB", "Kol_txtMainOwner", main_owner);
            
        }
  
        BigDecimal coll_typ_id = null;
        BigDecimal cus_typ_id_1 = (BigDecimal) ra.getAttribute("CollOwnersDialogLDB", "cus_typ_id"); 
// samo za stambene nekretnine se unosi broj nekretnina u vlasnistvu
        if (ra.getScreenContext().compareTo("scr_insert") == 0 || ra.getScreenContext().compareTo("scr_action") == 0)  {            
// postaviti kontekst za unos boja nekretnina u vlasnistvu i indikatora glavnog vlasnika
            if (ra.isLDBExists("RealEstateDialogLDB")) {
                coll_typ_id = (BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_TYPE_ID"); 
            }
            coll_util.setNumberOfOwnersCtx(coll_typ_id, cus_typ_id_1);
        }
    }   

    public boolean Kol_txtStatementRegNo_FV (String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementUseId", null);
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo", null);
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementName", null);
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementOIB", null);
            return true;
        }
          
        if (ra.getCursorPosition().equals("Kol_txtStatementName")) {                                                      
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo", "");
            ra.setAttribute("CollOwnersDialogLDB","Kol_txtStatementUseId",null);  
        } else if (ra.getCursorPosition().equals("Kol_txtStatementRegNo")) {                                              
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementName", "");  
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementUseId", "");  
         }
        
        String d_register_no = "";
        if (ra.getAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo") != null ) {
            d_register_no = (String) ra.getAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo");
        }
 
        String d_name = "";                                                                                                              
        if (ra.getAttribute("CollOwnersDialogLDB", "Kol_txtStatementName") != null){                                                    
            d_name = (String) ra.getAttribute("CollOwnersDialogLDB", "Kol_txtStatementName");                                             
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
        
         
        if (ra.isLDBExists("CustomerAllCitizenLookUpLDB_1")) {//ovo ne dirati                                                                                    
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
                                                                                                                                       
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo"));
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute("CollOwnersDialogLDB", "Kol_txtStatementName")); 
 
                                                                                                                                     
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
    
        ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementUseId",(java.math.BigDecimal) ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));        
        ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));                       
        ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));                             
        ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementOIB", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "oib_lk"));
        return true;
    }
     
    public boolean CollOwnersDialog_InterniMB_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo", null);
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_CUS_ID", null);
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName", null);
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtFirstName", null);
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtSurname", null);
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode", null);
            
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstate", "");   
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstateDsc","");
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementUseId", null);
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementRegNo", null);
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtStatementName", null);
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtOIB", null);
            
            return true;
        }
          
        if (ra.getCursorPosition().equals("CollOwnersDialog_txtCustomerName")) {                                                      
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo", "");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode", "");  
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtFirstName","");
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtSurname","");
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_CUS_ID",null);  
        } else if (ra.getCursorPosition().equals("CollOwnersDialog_txtRegisterNo")) {                                              
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName", "");  
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode", "");  
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtFirstName","");
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtSurname","");
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_CUS_ID",null);   
        } else if (ra.getCursorPosition().equals("CollOwnersDialog_txtCode")) {
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName", "");  
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo", ""); 
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtFirstName","");
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtSurname","");
            ra.setAttribute("CollOwnersDialogLDB","CollOwnersDialog_CUS_ID",null);   
        }                     
        String d_register_no = "";
        if (ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo") != null ) {
            d_register_no = (String) ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo");
        }
        String d_code = "";   
        if (ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode") != null){                                                   
                    d_code = (String) ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode");                                     
        } 
        String d_name = "";                                                                                                              
        if (ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName") != null){                                                    
            d_name = (String) ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName");                                             
        } 
        String d_fname = "";   
        if (ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtFirstName") != null){                                                   
            d_fname = (String) ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtFirstName");                                     
        } 
        
        String d_sname = "";   
        if (ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtSurname") != null){                                                   
            d_sname = (String) ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtSurname");                                     
        } 
        
        if ((d_name.length() < 4) && (d_register_no.length() < 4) && (d_code.length() < 4)  && (d_sname.length() < 4) ) {                                                                     
            ra.showMessage("wrn366");                                                                                                      
            return false;                                                                                                                  
        }   
          
        //JE LI zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }
        
        if (ra.getCursorPosition().equals("CollOwnersDialog_txtRegisterNo"))                                                      
            ra.setCursorPosition(5);                                                                                                       
        if (ra.getCursorPosition().equals("CollOwnersDialog_txtCode"))                                                      
            ra.setCursorPosition(4);                                                                                                                                       
        if (ra.getCursorPosition().equals("CollOwnersDialog_txtCustomerName"))                                                      
            ra.setCursorPosition(3);              

                  
        
        
        if (ra.isLDBExists("CustomerAllCitizenLookUpLDB_1")) {//ovo ne dirati                                                                                    
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
                                                                                                                                       
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo"));
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName")); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode")); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtFirstName")); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", ra.getAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtSurname")); 
                                                                                                                                     
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

        //provjeriti da li je komitent aktivan

        String status = (String) ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "status");
        BigDecimal cus_sub_typ_id=ra.getAttribute("CustomerAllCitizenLookUpLDB_1","cus_sub_typ_id");
        if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("2")) {
        } else {    
            //komitent nije aktivan         
            ra.showMessage("wrnclt145");
            return false;    
        } 
        if((new BigDecimal("31250")).compareTo(cus_sub_typ_id)==0){
            coll_util.ShowInfoMessage("Komitent vrste komitenta VK28 ne može biti vlasnik kolaterala.");
            return false;
        }
    
        ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));        
        ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));                       
        ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCustomerName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));                               
        ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtCode", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "code"));                               
        ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtFirstName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "fname"));                             
        ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtSurname", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "surname"));   
        ra.setAttribute("CollOwnersDialogLDB", "cus_typ_id", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_typ_id"));  
        ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtOIB", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "oib_lk"));  
         
// postaviti kontekst za unos boja nekretnina u vlasnistvu
        BigDecimal cus_typ_id = (BigDecimal) ra.getAttribute("CollOwnersDialogLDB", "cus_typ_id");
        BigDecimal coll_typ_id = null;
        if (ra.isLDBExists("RealEstateDialogLDB")) {
            coll_typ_id = (BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_TYPE_ID"); 
        }
        coll_util.setNumberOfOwnersCtx(coll_typ_id, cus_typ_id);
        
        ra.setCursorPosition("CollOwnersDialog_txtUseCodeValue");
        return true;
    }
   
    public boolean CollOwnersDialog_txtUseCodeValue_FV(){

        String useCodeValue = (String)ra.getAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtUseCodeValue");
        int useCodeValueInt = 0;
        int useCodeDescInt = 0;
        
        if(useCodeValue != null){
            useCodeValue = useCodeValue.trim();
            if(useCodeValue.length()== 0) {
                //Mora se upisati broj dijelova vlasnistva ( brojnik)
                ra.showMessage("wrnclt54");
                return false;
            }
            
            useCodeValue = rejectLeadingZeros(useCodeValue);
            if(useCodeValue.compareTo("0") == 0){
                //Brojnik ne moze biti 0
                ra.showMessage("wrnclt55");
                return false;
            }
            useCodeValueInt = Integer.parseInt(useCodeValue); 
        }else{
            //Mora se upisati broj dijelova vlasnistva ( brojnik)
            ra.showMessage("wrnclt54");
            return false;
        }
        
        String useCodeDesc = (String)ra.getAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtUseCodeDesc"); 
        if(useCodeDesc != null){
            useCodeDesc = useCodeDesc.trim();
            if(useCodeDesc.length() > 0){
                useCodeDescInt = Integer.parseInt(useCodeDesc); 
                if(useCodeValueInt > useCodeDescInt){
                    //Brojnik ne moze biti veci od nazivnika
                    ra.showMessage("wrnclt56");
                    return false;
                }
            }
        }
        return true;
    }//CollOwnersDialog_txtUseCodeValue_FV
    
    
    public boolean CollOwnersDialog_txtUseCodeDesc_FV(){
//      CollOwnersDialog_txtUseCodeDesc je nazivnik dijela vlasnistva
        String useCodeValue = (String)ra.getAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtUseCodeValue");
        String useCodeDesc = (String)ra.getAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtUseCodeDesc"); 
        int useCodeValueInt = 0;
        int useCodeDescInt = 0;
        
        if(useCodeValue != null){
            useCodeValue = useCodeValue.trim();
            if(useCodeValue.length()== 0) {
                //Mora se upisati broj dijelova vlasnistva ( brojnik)
                ra.showMessage("wrnclt54");
                ra.setCursorPosition("CollOwnersDialog_txtUseCodeValue");
                return false;
            }else{
        
                useCodeValue = rejectLeadingZeros(useCodeValue);
                if(useCodeValue.compareTo("0") == 0){
                    //Brojnik ne moze biti 0
                    ra.showMessage("wrnclt55");
                    ra.setCursorPosition("CollOwnersDialog_txtUseCodeValue");
                    return false;
                }else{
                    useCodeValueInt = Integer.parseInt(useCodeValue); 
                }
            }
        }else{
            //Mora se upisati broj dijelova vlasnistva ( brojnik)
            ra.showMessage("wrnclt54");
            ra.setCursorPosition("CollOwnersDialog_txtUseCodeValue");
            return false;
        }
       
        if(useCodeDesc != null){
            useCodeDesc = useCodeDesc.trim();
            if(useCodeDesc.length()== 0) {
                //Mora se upisati udio vlasnistva ( nazivnik)
                ra.showMessage("wrnclt57");
                return false;
            }
           
            useCodeDesc = rejectLeadingZeros(useCodeDesc);
            if(useCodeDesc.compareTo("0") == 0){
                //Nazivnik dijela vlasnistva ne moze biti 0
                ra.showMessage("wrnclt58");
                return false;
            }
            
            useCodeDescInt = Integer.parseInt(useCodeDesc); 
            
        }else{
            //Ne moze se upisati dio vlasnistva bez nazivnika
            ra.showMessage("wrnclt59");
            ra.setCursorPosition("CollOwnersDialog_txtUseCodeDesc");
            return false;
        }
        
        if(useCodeValueInt > useCodeDescInt){
            //Brojnik ne moze biti veci od nazivnika
            ra.showMessage("wrnclt56");
            return false;
        }
         
        useCodeValue = (String)ra.getAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtUseCodeValue");
        useCodeDesc = (String)ra.getAttribute("CollOwnersDialogLDB","CollOwnersDialog_txtUseCodeDesc"); 
        useCodeValue = useCodeValue.trim();
        useCodeDesc =useCodeDesc.trim() ; 
        
        useCodeValue = rejectLeadingZeros(useCodeValue);
        useCodeDesc  = rejectLeadingZeros(useCodeDesc) ; 
        
        String partId = useCodeValue + "/" + useCodeDesc;
        ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_PART_ID", partId);
//      System.out.println("AAAAAAAAAAAAAAA"   + partId);
        return true;
    }
    
    public String rejectLeadingZeros(String argString){
        String retString = "";
        int lenString = argString.length();
        int pos = 0;
        for(int i=0;i<lenString;i++){
            if((argString.charAt(i) != '0')){
                pos = i;
                break;
            }
    
        }   
        
        retString = argString.substring(pos,lenString);
        return retString;
    }//rejectLeadingZeros
   
    
    /**
     * Postavlja ID osobe koja unosi podatke o vlasniku, te date_from i
     * date_until
     */
    public void setUserData() {
        java.sql.Date todaySQLDate = null;
        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new java.sql.Date(timeT);
       
        if (ra.getScreenContext().compareTo("scr_insert") == 0) {
            
            ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserIdLogin", (String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserIdName", (String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserOpenIdLogin", (String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserOpenIdName", (String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_USE_ID", (BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_USE_OPEN_ID", (BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_DATE_FROM", todaySQLDate);
            Date dateUntil = Date.valueOf("9999-12-31");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_DATE_UNTIL", dateUntil);
        }

        if (ra.getScreenContext().compareTo("scr_action") == 0 || ra.getScreenContext().compareTo("scr_activate") == 0) {
            ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserIdLogin", (String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute("Collateral_blUserFieldLDB", "Collateral_txtUserIdName", (String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_USE_ID", (BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_DATE_FROM", todaySQLDate);
            Date dateUntil = Date.valueOf("9999-12-31");
            ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_DATE_UNTIL", dateUntil);
        }
    }

    /**
     * Metoda poziva transakciju za dodavanje vlasnika/suvlasnika
     */
    public void confirm() {
        if (!(ra.isRequiredFilled())) {
            return;
        }
        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
            ra.exitScreen();
            ra.refreshActionList("tblCollOwners");
        }
        catch (VestigoTMException vtme) {
            error("CollOwnersDialog -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
    }//confirm
  
   
    public void change() {
        if (!(ra.isRequiredFilled())) {
            return;
        }
        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
            ra.exitScreen();
            ra.refreshActionList("tblCollOwners");
        }
        catch (VestigoTMException vtme) {
            error("CollOwnersDialog -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }   
    }//change  
  
    public void act_owner() {
        Integer del = (Integer) ra.showMessage("col_qer026");
        //provjerava da li korisnik zeli aktivirati odabrani zapis
        if (del != null && del.intValue() == 1) {
            try {
                ra.executeTransaction();
                ra.showMessage("infclt101");
            }
            catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
        }  
        ra.exitScreen();
        ra.refreshActionList("tblCollOwners");
    }
       
    public void delete() {
        Integer del = (Integer) ra.showMessage("col_qer025");
        //provjerava da li korisnik zeli brisati odabrani zapis
        if (del != null && del.intValue() == 1) {
            try {
                ra.executeTransaction();
                ra.showMessage("infclt100");
            }
            catch (VestigoTMException vtme) {
                error("CollOwnersDialog -> delete(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
        }
        ra.exitScreen();
        ra.refreshActionList("tblCollOwners");
        //scr_delete za transakciju
    }//delete
    
         
    public boolean Kol_txtOwnNumOfEstate_FV (String ElName, Object ElValue, Integer LookUp)
    {
        // dozvoljene vrijednosti su N, i brojke
        // num_of_real_estate
        if (ElValue == null || ElValue.equals("")) {                                          
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstate", "");
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstateDsc", "");
            return true;
        }

        ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstateDsc", ""); 
        ra.setAttribute("RealEstateDialogLDB", "SysCodId", "num_of_real_estate");
        ra.setAttribute("RealEstateDialogLDB", "dummySt", null);

        if (!(ra.isLDBExists("SysCodeValueNewLookUpLDB"))) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "num_of_real_estate");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstate", "sys_code_value");
        request.addMapping("CollOwnersDialogLDB", "Kol_txtOwnNumOfEstateDsc", "sys_code_desc");
        request.addMapping("RealEstateDialogLDB", "dummySt", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) { 
            return false;
        }
        return true;
    }

    
    public boolean Kol_txtMainOwner_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollOwnersDialogLDB", "Kol_txtMainOwner", null);
            return true;
        }
//        System.out.println("AAAAAAAAAAAAAAA prije new LookUpRequest"  );
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");   
//        System.out.println("AAAAAAAAAAAAAAA poslije new LookUpRequest"  );
 
        request.addMapping("CollOwnersDialogLDB", "Kol_txtMainOwner", "Vrijednosti");
//        System.out.println("AAAAAAAAAAAAAAA postavljena lookup vrijednost"  );

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

// izbrojati koliko ima aktivnih vlasnika, samo za nekretnine, samo kod unosa novog
// ako je upisano N, provjeriti da li je upisani vlasnik jedini aktivni
        String main_owner = (String) ra.getAttribute("CollOwnersDialogLDB", "Kol_txtMainOwner");
        BigDecimal col_typ_id = null;
        if (ra.isLDBExists("RealEstateDialogLDB")) {
            col_typ_id = (BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_TYPE_ID"); 
        }     
        BigDecimal cus_typ_id = (BigDecimal) ra.getAttribute("CollOwnersDialogLDB", "cus_typ_id");  
/*        if (main_owner != null && main_owner.equalsIgnoreCase("N")) {
//            if (ra.isLDBExists("RealEstateDialogLDB")) {
//                col_typ_id = (BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_TYPE_ID"); 
//            }        
            if (col_typ_id != null && col_typ_id.compareTo(new BigDecimal("8777")) == 0) {
 
                if(!(isActiveMainOwner("tblCollOwners"))) { 
                    ra.showMessage("errclt12");
                    return false;
                }
            }
        }  else if (main_owner != null && main_owner.equalsIgnoreCase("D")) {
// ako je upisano D, provjeriti da li vec ima jedan aktivni vlasnik koji nije isti kao ovaj 
            if (col_typ_id != null && col_typ_id.compareTo(new BigDecimal("8777")) == 0) {
                
                if(isActiveMainOwner("tblCollOwners")) { 
                    ra.showMessage("errclt11");
                    return false;
                }
            }            
        } */
        return true;
    }  
           
    public void setOwnerForCes(){
        if(ra.getScreenContext().compareTo("scr_insert")== 0){
// 23.08.2010 - preuzeti cesus-a kao vlasnika za cesije
// 04.03.2008 - promjena, ne treba zaprotektirati preuzetog vlasnika            
            if (ra.isLDBExists("ColWorkListLDB")) {
                String code = (String) ra.getAttribute("ColWorkListLDB", "code");
                if (code.equalsIgnoreCase("CESI")) {
                    ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtCesusMb"));   
                    ra.setCursorPosition("CollOwnersDialog_txtRegisterNo");
                    ra.invokeValidation("CollOwnersDialog_txtRegisterNo");
                    ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtUseCodeValue", "1");                    
                    ra.setCursorPosition("CollOwnersDialog_txtUseCodeValue");
                    ra.invokeValidation("CollOwnersDialog_txtUseCodeValue");
                    ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtUseCodeDesc", "1");  
                    ra.setCursorPosition("CollOwnersDialog_txtUseCodeDesc");
                    ra.invokeValidation("CollOwnersDialog_txtUseCodeDesc");
                    ra.setAttribute("CollOwnersDialogLDB", "Kol_txtOwnNum", "1");        
                    ra.setAttribute("CollOwnersDialogLDB", "Kol_txtMainOwner", "D");
                    ra.setContext("CollOwnersDialog_txtRegisterNo", "fld_change_protected");
                    ra.setContext("CollOwnersDialog_txtCustomerName", "fld_change_protected");
                    ra.setContext("CollOwnersDialog_txtCode", "fld_change_protected");
                    ra.setContext("CollOwnersDialog_txtUseCodeValue", "fld_change_protected");
                    ra.setContext("CollOwnersDialog_txtUseCodeDesc", "fld_change_protected");
                    ra.setContext("Kol_txtOwnNum", "fld_change_protected");
                    ra.setContext("Kol_txtMainOwner", "fld_change_protected");
                }
            } 
        }//scr_insert
       
        if(ra.getScreenContext().compareTo("scr_action")== 0){
//        17.12.2007 - zaprotektirati vlasnika za zaduznice i mjenice  
//        04.03.2008 - promjena, ne treba zaprotektirati preuzetog vlasnika 
            if (ra.isLDBExists("ColWorkListLDB")) {
                String code = (String) ra.getAttribute("ColWorkListLDB", "code");
                if (code.equalsIgnoreCase("CESI")) {
                    ra.setAttribute("CollOwnersDialogLDB", "CollOwnersDialog_txtRegisterNo", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtCesusMb"));   
                    ra.setCursorPosition("CollOwnersDialog_txtRegisterNo");
                    ra.invokeValidation("CollOwnersDialog_txtRegisterNo");
                    ra.setContext("CollOwnersDialog_txtRegisterNo", "fld_change_protected");
                    ra.setContext("CollOwnersDialog_txtCustomerName", "fld_change_protected");
                    ra.setContext("CollOwnersDialog_txtCode", "fld_change_protected");
                    ra.setContext("CollOwnersDialog_txtUseCodeValue", "fld_change_protected");
                    ra.setContext("CollOwnersDialog_txtUseCodeDesc", "fld_change_protected");
                    ra.setContext("Kol_txtOwnNum", "fld_change_protected");
                    ra.setContext("Kol_txtMainOwner", "fld_change_protected");
                }           
            } 
        }//scr_action
    }//postavi_nekeUserDate    
    
    public boolean isActiveMainOwner(String tableName) {
        TableData tab_input = (TableData) ra.getAttribute(tableName);
        Vector data = tab_input.getData();
        for (int i = 0; i < tab_input.getUnique().size(); i++) {
            Vector row_in = (Vector) data.elementAt(i);
            String flag_activ = "" + row_in.elementAt(3);
            String flag_main = "" + row_in.elementAt(5);
            if (flag_activ.equalsIgnoreCase("A") && flag_main.equalsIgnoreCase("D")) 
                return true;
        }
        return false;
    }
}