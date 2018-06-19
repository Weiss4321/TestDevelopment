/*
 * Created on 2006.05.22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;
  
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
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
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InsuPolicyDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsuPolicyDialog.java,v 1.36 2015/01/20 14:49:53 hrazst Exp $";
	CollateralUtil coll_util= null;
    public InsuPolicyDialog(ResourceAccessor ra) {
        super(ra);
		coll_util = new CollateralUtil(ra);
    }

    public void InsuPolicyDialog_SE(){
        if (!ra.isLDBExists("InsuPolicyDialogLDB")) {
	 		ra.createLDB("InsuPolicyDialogLDB");
           }    	
    	String scr_ctxt = ra.getScreenContext().trim();
    	if(scr_ctxt.equalsIgnoreCase("scr_detail") || scr_ctxt.equalsIgnoreCase("scr_action") || scr_ctxt.equalsIgnoreCase("scr_deakt_storno")){
    		InsuPolicyDialog_fill();
    	}else if (scr_ctxt.equalsIgnoreCase("scr_insert")) {
    		InsuPolicyDialog_insert();
        }
		
		ra.invokeValidation("IP_SPEC_STAT");
        ra.invokeValidation("InsuPolicyDialog_txtWrnStatusCode");
        ra.invokeValidation("InsuPolicyDialog_txtKmtStatusCode");
        ra.invokeValidation("InsuPolicyDialog_txtCIcId");
        ra.invokeValidation("InsuPolicyDialog_txtNTypeId");
        
        ra.setCursorPosition("InsuPolicyDialog_txtCode");	
    }
      
    public void InsuPolicyDialog_fill(){
		TableData td = (TableData) ra.getAttribute("InsuPolicyLDB", "tblInsuPolicy");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        Vector row = td.getSelectedRowData();
        
//        System.out.println("*** InsuPolicyDialog_fill");
        
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCode", row.elementAt(0));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCodeB", row.elementAt(0));        
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNTypeId", row.elementAt(1));   
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtSecuVal", row.elementAt(2));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtSecuValB", row.elementAt(2));        
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCurMark", row.elementAt(3));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtDateSecVal", row.elementAt(4));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtDateSecValB", row.elementAt(4));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtDateUntil", row.elementAt(5));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtDateUntilB", row.elementAt(5));        
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtAct", row.elementAt(6));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtActB", row.elementAt(6));        
        ra.setAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT", row.elementAt(7));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT_B", row.elementAt(7));
        
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtWrnStatusCode", row.elementAt(8));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtKmtStatusCode", row.elementAt(9));  
          
        ra.setAttribute("InsuPolicyDialogLDB", "IP_ID", hidden.elementAt(0));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_TYPE_ID", hidden.elementAt(1));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_TYPE_ID_B", hidden.elementAt(1));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_IC_ID", hidden.elementAt(2));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_IC_ID_B", hidden.elementAt(2));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_CONTRACTOR", hidden.elementAt(3));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_CONTRACTOR_B", hidden.elementAt(3));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_POL_HOLDER", hidden.elementAt(4));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_POL_HOLDER_B", hidden.elementAt(4));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtPlace", hidden.elementAt(5));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtPlaceB", hidden.elementAt(5));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_CUR_ID", hidden.elementAt(6));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_CUR_ID_B", hidden.elementAt(6)); 
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtDateFrom", hidden.elementAt(7));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtDateFromB", hidden.elementAt(7));        
        ra.setAttribute("InsuPolicyDialogLDB", "COL_HEA_ID", hidden.elementAt(8));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtReplace", hidden.elementAt(9));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtReplaceB", hidden.elementAt(9));

        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNContractor", hidden.elementAt(10));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNPolHolder", hidden.elementAt(11));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNIcId", hidden.elementAt(12));        
        ra.setAttribute("InsuPolicyDialogLDB", "USE_OPEN_ID", hidden.elementAt(13));       
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtOpenIdLogin", hidden.elementAt(14));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtOpenIdName", hidden.elementAt(15));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtOpeningTs", hidden.elementAt(16));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtOpeningTsNF", hidden.elementAt(16));   
        ra.setAttribute("InsuPolicyDialogLDB", "USE_ID_B", hidden.elementAt(17));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtUseIdLogin", hidden.elementAt(18));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtUseIdName", hidden.elementAt(19));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtUserLock", hidden.elementAt(20));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtUserLockNF", hidden.elementAt(20));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCTypeId", hidden.elementAt(21));               
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCIcId", hidden.elementAt(22));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCContractor", hidden.elementAt(23));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCPolHolder", hidden.elementAt(24));        
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtActDes", hidden.elementAt(25));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtSpecActDes", hidden.elementAt(26));  
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedSifra", hidden.elementAt(27));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedOpis", hidden.elementAt(28));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosOsiguraneSvote", hidden.elementAt(29));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID", hidden.elementAt(30));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosOsiguraneSvoteValuta", hidden.elementAt(31));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosIznosVinkuliranUKorist", hidden.elementAt(32));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID", hidden.elementAt(33));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValuta", hidden.elementAt(34));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtRBAVinkulacijaSifra", hidden.elementAt(35));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtRBAVinkulacijaOpis", hidden.elementAt(36));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtDodatniPodaci", hidden.elementAt(37));
        
        //dodano za NGV 6032 (polje je obavezno pa je popunjeno,  ako je = 2 onda su i iznos+valuta obavezni)
        if(ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedSifra").equals("2")){
            ra.setRequired("InsuPolicyDialog_txtIznosIznosVinkuliranUKorist", true);
            ra.setRequired("InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValuta", true);
        }
    }
      
    public void InsuPolicyDialog_insert(){
    	ra.setAttribute("InsuPolicyDialogLDB", "COL_HEA_ID", ra.getAttribute("InsuPolicyLDB","InsuPolicy_COL_HEA_ID"));
        ra.setAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT", "00");
        String UserLogin =(String) ra.getAttribute("GDB","Use_Login");
        String UserName =(String) ra.getAttribute("GDB","Use_UserName");
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtUseIdLogin", UserLogin);
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtUseIdName", UserName);
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtOpenIdLogin", UserLogin);
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtOpenIdName", UserName);
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtWrnStatusCode", "0");
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtKmtStatusCode", "0");
        
        //dodano za NGV 6032
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedSifra", "1");
        ra.invokeValidation("InsuPolicyDialog_txtVinkulacija1RedSifra");
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtRBAVinkulacijaSifra", "1");
        ra.invokeValidation("InsuPolicyDialog_txtRBAVinkulacijaSifra");
    }

      public boolean InsuPolicyDialog_txtCContractor_FV(String elementName, Object elementValue, Integer LookUp){
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtCContractor",null);
            ra.setAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtNContractor",null);
            ra.setAttribute("InsuPolicyDialogLDB","IP_CONTRACTOR",null);
            return true;
        }
        
        
        if (ra.getCursorPosition().equals("InsuPolicyDialog_txtNContractor")) {
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCContractor", "");
        } else if (ra.getCursorPosition().equals("InsuPolicyDialog_txtCContractor")) {
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNContractor", "");
        }
        
        String d_name = "";
        if (ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNContractor") != null){
            d_name = (String) ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNContractor");
        }
        
        String d_register_no = "";
        if (ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCContractor") != null){
            d_register_no = (String) ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCContractor");
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

         if (ra.getCursorPosition().equals("InsuPolicyDialog_txtCContractor")) 
            ra.setCursorPosition(2);
         
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
                                                                                                                                       
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCContractor"));
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNContractor")); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", null); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", null); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", null); 
                                                                                                                                     
        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllCitizenLookUp_22"); //ni ovo ne dirati                                                           
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
           
         
        ra.setAttribute("InsuPolicyDialogLDB", "IP_CONTRACTOR", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCContractor", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNContractor", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));
        
        if(ra.getCursorPosition().equals("InsuPolicyDialog_txtCContractor")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("InsuPolicyDialog_txtNContractor")){
            ra.setCursorPosition(1);
        }            
       
        return true;
      }

      public boolean InsuPolicyDialog_txtAct_FV(String elementName, Object elementValue, Integer lookUpType)
      {
          if ((ra.getScreenContext().compareTo("scr_insert")== 0) || (ra.getScreenContext().compareTo("scr_action")== 0))
          {
              if (elementValue == null || ((String) elementValue).equals("")) {
                  ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtAct", "");
                  ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtActDes", "");
                  ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);
                  return true;
              }

              ra.setAttribute("InsuPolicyDialogLDB", "SysCodId", "clt_inspolst");
              ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);        

              if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
              ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "clt_inspolst");

              LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
              request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtAct", "sys_code_value");
              request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtActDes", "sys_code_desc");
              request.addMapping("InsuPolicyDialogLDB", "dummyBD", "sys_cod_val_id");

              try {
                  ra.callLookUp(request);
              } catch (EmptyLookUp elu) {
                  ra.showMessage("err012");
                  return false;
              } catch (NothingSelected ns) {
                  return false;
              }

              ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);        
              ra.setAttribute("InsuPolicyDialogLDB", "SysCodId", "");

              if (ra.getCursorPosition().equals("InsuPolicyDialog_txtAct")) ra.setCursorPosition(2);
              if (ra.getCursorPosition().equals("InsuPolicyDialog_txtActDes")) ra.setCursorPosition(1);
          }
		
          return true;
    }


    public boolean InsuPolicyDialog_txtCIcId_FV(String elementName, Object elementValue, Integer LookUp){
		if (elementValue == null || ((String) elementValue).equals("")) {                                    
			coll_util.clearFields("InsuPolicyDialogLDB", new String[]{"InsuPolicyDialog_txtNIcId","InsuPolicyDialog_txtCIcId","InsuPolicyDialog_txtNTypeId",
			        "InsuPolicyDialog_txtCTypeId","InsuPolicyDialog_txtCTypeId","InsuPolicyDialog_txtIpInternalRating", "InsuPolicyDialog_txtIpExternalRating",
			        "InsuPolicyDialog_txtIpAgencyRating", "IP_IC_ID", "IP_TYPE_ID"});                                  
			return true;                                                                                 
		}                                                                                               
        
    	if (ra.getCursorPosition().equals("InsuPolicyDialog_txtNIcId")) {
    	    coll_util.clearField("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCIcId");   
    	    ra.setCursorPosition(1);		     
    	} else {
	        coll_util.clearField("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNIcId");     
            ra.setCursorPosition(2);
    	}	 
        coll_util.clearFields("InsuPolicyDialogLDB", new String[]{"IP_IC_ID", "IP_TYPE_ID"});  
		
        if (ra.isLDBExists("InsuCompanyLDB")) {//ovo ne dirati 
            coll_util.clearField("InsuCompanyLDB", "insu_company_register_no");   
            coll_util.clearField("InsuCompanyLDB", "insu_company_name");   
        } else {                                                                                                                         
            ra.createLDB("InsuCompanyLDB");                                                                                          
        }
        ra.setAttribute("InsuCompanyLDB", "insu_company_register_no", ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtCIcId"));
        ra.setAttribute("InsuCompanyLDB", "insu_company_name", ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtNIcId"));

        LookUpRequest lu = new LookUpRequest("InsuCompanyOibLookUp");  
        
        lu.addMapping("InsuCompanyLDB", "insu_company_register_no", "ic_register_no");
        lu.addMapping("InsuCompanyLDB", "insu_company_oib", "tax_number"); 
        lu.addMapping("InsuCompanyLDB", "insu_company_name", "ic_name"); 
        lu.addMapping("InsuCompanyLDB", "insu_company_id", "ic_id");  
        lu.addMapping("InsuCompanyLDB", "insu_company_code", "ic_code");
        lu.addMapping("InsuCompanyLDB", "insu_company_cust_rating", "cust_rating");
                                                                                         
        try {                                                                                            
            ra.callLookUp(lu);                                                                           
        } catch (EmptyLookUp elu) {                                                                      
            ra.showMessage("err012");                                                                    
            return false;                                                                                
        } catch (NothingSelected ns) {                                                                   
            return false;                                                                                
        } 
      
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNIcId", ra.getAttribute("InsuCompanyLDB", "insu_company_name"));   
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCIcId", ra.getAttribute("InsuCompanyLDB", "insu_company_register_no"));  
        ra.setAttribute("InsuPolicyDialogLDB", "IP_IC_ID", ra.getAttribute("InsuCompanyLDB", "insu_company_id"));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIpInternalRating", ra.getAttribute("InsuCompanyLDB", "insu_company_cust_rating"));
       return true;                                                                                                                  
    } 

    public boolean InsuPolicyDialog_txtCPolHolder_FV(String elementName, Object elementValue, Integer LookUp){
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtCPolHolder",null);
            ra.setAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtNPolHolder",null);
            ra.setAttribute("InsuPolicyDialogLDB","IP_POL_HOLDER",null);
            return true;
        }
        
        
        if (ra.getCursorPosition().equals("InsuPolicyDialog_txtNPolHolder")) {
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCPolHolder", "");
        } else if (ra.getCursorPosition().equals("InsuPolicyDialog_txtCPolHolder")) {
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNPolHolder", "");
        }
        
        String d_name = "";
        if (ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNPolHolder") != null){
            d_name = (String) ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNPolHolder");
        }
        
        String d_register_no = "";
        if (ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCPolHolder") != null){
            d_register_no = (String) ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCPolHolder");
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

         if (ra.getCursorPosition().equals("InsuPolicyDialog_txtCPolHolder")) 
            ra.setCursorPosition(2);
         
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
                                                                                                                                        
         ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCPolHolder"));
         ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNPolHolder")); 
         ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", null); 
         ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", null); 
         ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", null); 
                                                                                                                                      
         LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllCitizenLookUp_22"); //ni ovo ne dirati                                                           
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
         
        ra.setAttribute("InsuPolicyDialogLDB", "IP_POL_HOLDER", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCPolHolder", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNPolHolder", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));
        
        if(ra.getCursorPosition().equals("InsuPolicyDialog_txtCPolHolder")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("InsuPolicyDialog_txtNPolHolder")){
            ra.setCursorPosition(1);
        }            
       
        return true;
    }

    public boolean InsuPolicyDialog_txtCTypeId_FV(String elementName, Object elementValue, Integer LookUp){
    	
    	java.math.BigDecimal ipIcId = null;
    	
		if (elementValue == null || ((String) elementValue).equals("")) {                                    
                         
           ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNTypeId", "");   
           ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCTypeId", "");
           ra.setAttribute("InsuPolicyDialogLDB", "IP_TYPE_ID", null); 
           ra.setAttribute("InsuPolicyDialogLDB", "dummyStN", ""); 
           
           
           return true;                                                                                 
       }                                                                                                
	   ra.setAttribute("InsuPolicyDialogLDB", "dummyStN", "");   
	   
       if (!ra.isLDBExists("InsPolTypeLookUpLDB")) {                                                 
           ra.createLDB("InsPolTypeLookUpLDB");                                                      
       }                                                                                                
	   if (ra.getCursorPosition().equals("InsuPolicyDialog_txtNTypeId")) {
		     ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCTypeId", "");
		     ra.setAttribute("InsuPolicyDialogLDB", "IP_TYPE_ID", null); 
		     ra.setAttribute("InsuPolicyDialogLDB", "dummyStN", ""); 
		     ra.setCursorPosition(1);
		     
	   } else if (ra.getCursorPosition().equals("InsuPolicyDialog_txtCTypeId")) {
		     ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNTypeId", "");
		     ra.setAttribute("InsuPolicyDialogLDB", "IP_TYPE_ID", null); 
		     ra.setAttribute("InsuPolicyDialogLDB", "dummyStN", ""); 
		     ra.setCursorPosition(2);
	   }	
		 
       if (ra.getAttribute("InsuPolicyDialogLDB", "IP_IC_ID") != null) {
       		ipIcId = (BigDecimal) ra.getAttribute("InsuPolicyDialogLDB", "IP_IC_ID");
       }else{
       		ra.showMessage("wrncltzst3");
       		return false;
       }      
           
       ra.setAttribute("InsPolTypeLookUpLDB", "polCompanyId", ipIcId);  
       
// ovo napuniti ovisno o vrsti kolaterala za koji se unosi polica osiguranja
       
//       ra.setAttribute("InsPolTypeLookUpLDB", "group1", "IMOVINA");  
       ra.setAttribute("InsPolTypeLookUpLDB", "group4", ra.getAttribute("ColWorkListLDB", "code"));       
       
                              
       LookUpRequest lu = new LookUpRequest("InsPolTypeLookUp");                                     
       lu.addMapping("InsuPolicyDialogLDB", "IP_TYPE_ID", "int_pol_type_id");  
       lu.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCTypeId", "int_pol_type_code");
       lu.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtNTypeId", "int_pol_type_name");
       lu.addMapping("InsuPolicyDialogLDB", "dummyStN", "int_pol_sh_sign");                                                                                                 
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
    
    public boolean InsuPolicyDialog_txtCurMark_FV(String elementName, Object elementValue, Integer LookUp){

		if (elementValue == null || ((String) elementValue).equals("")) {                                          
			ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCurMark", "");                        
			ra.setAttribute("InsuPolicyDialogLDB", "IP_CUR_ID", null);                               
			return true;                                                                                 
		}

		ra.setAttribute("InsuPolicyDialogLDB", "dummyStC", "");
		ra.setAttribute("InsuPolicyDialogLDB", "dummyStN", "");
		
//		LookUpRequest lookUpRequest = new LookUpRequest("FPaymCurrencyLookUp");  
		LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   
		lookUpRequest.addMapping("InsuPolicyDialogLDB", "IP_CUR_ID", "cur_id");           
		lookUpRequest.addMapping("InsuPolicyDialogLDB", "dummyStC", "code_num");
		lookUpRequest.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtCurMark", "code_char");
		lookUpRequest.addMapping("InsuPolicyDialogLDB", "dummyStN", "name");
		//lookUpRequest.addMapping("InsuPolicyDialogLDB", "dummyInt", "ord_no");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}                                                                                              
		return true;    
    } 

       
    public boolean InsuPolicyDialog_txtDateSecVal_FV(String elementName, Object elementValue, Integer LookUp){
		Date valid_until=(Date) ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateSecVal");
        Date until=(Date) ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateUntil");   	
        if (valid_until != null && until != null) {
        	if(valid_until.before(until) ){
        		ra.showMessage("wrnclt136");
        		return false;
        	}
        }
    	return true;
    }       
    
    public boolean InsuPolicyDialog_txtDateFrom_FV(){
    	//InsuPolicyDialog_txtDateFrom
		//InsuPolicyDialog_txtDateUntil
    	
    	Date pocetak = (Date) ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateFrom");
    	
    	   	
    	if(pocetak != null){
    		
			java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        	calendar.setTime(pocetak);
        	calendar.add(java.util.Calendar.YEAR,1);
        	long timeT = calendar.getTimeInMillis(); 
        	Date kraj = new Date(timeT);
//        	System.out.println(" POCETAK JE  |" + pocetak + "|" );
			
        	ra.setAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateUntil",kraj);
//        	System.out.println(" KRAJ JE  |" + kraj + "|" );
        	
    		Date valid_until=(Date) ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateSecVal");
            Date until=(Date) ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateUntil");   	
            if (valid_until != null && until != null) {
            	if(valid_until.before(until) ){
            		ra.showMessage("wrnclt136");
            		return false;
            	}
            }        	
           
            ra.setCursorPosition("InsuPolicyDialog_txtDateUntil");
            ra.invokeValidation("InsuPolicyDialog_txtDateUntil");			
    	}
    	
// ovisno o izracunatom     	
    	
    	return true;
		
    }  
        

    public boolean InsuPolicyDialog_txtDateUntil_FV(String elementName, Object elementValue, Integer LookUp){
		Date from=(Date) ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateFrom");
        Date until=(Date) ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateUntil");   	
		Date valid_until=(Date) ra.getAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateSecVal");
		Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
		
//    	System.out.println(" from JE  |" + from);
//     	System.out.println(" until JE  |" + until);
//     	System.out.println(" valid_until JE  |" + valid_until);
//     	System.out.println(" current_date JE  |" + current_date);
       	
		
		if (from != null && until != null) {
			if(from.after(until) || from.equals(until)){
				ra.showMessage("wrncltzst4");
				return false;
			}
		}
		
        if (valid_until != null && until != null) {
    		if(valid_until.before(until) ){
    			ra.showMessage("wrnclt136");
    			return false;
    		}
        }
        
        if (until == null || current_date == null )
        	return true;
// postaviti status police ovisno o datumu do kad je placena premija
        if (until.before(current_date)) {
        	ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtAct","I");
        } else {
        	ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtAct","A");        	
        }
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtActDes", "");   
        ra.setCursorPosition("InsuPolicyDialog_txtAct");
        ra.invokeValidation("InsuPolicyDialog_txtAct");
         
    	return true;
    }
      
  
	public void confirm(){
		
        if (!(ra.isRequiredFilled())) {
 			return;
 		}
// ako nije popunjen spec_status napuniti sa default-om: 00
        String spec_stat = (String) ra.getAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT");
        
        if (spec_stat.equals("")) {
        	ra.setAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT", "00");
        }
// validirati status police
        
        ra.setCursorPosition("InsuPolicyDialog_txtDateUntil");
        ra.invokeValidation("InsuPolicyDialog_txtDateUntil");
        
       try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
        } catch (VestigoTMException vtme) {
            error("RealEstateDialog -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
            
            return;
        }
        // napuniti za vozila za kasko osiguranje
        /* String code = (String) ra.getAttribute("ColWorkListLDB", "code");
        if (code.equalsIgnoreCase("VOZI")) {
        	ra.setAttribute("CollHeadLDB","INSPOL_IND",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtInspolInd"));
           	ra.setAttribute("CollSecPaperDialogLDB","Vehi_txtVehKasko",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtInspolInd"));
        }*/
//      napuniti za vozila podatak za kasko policu	
		if (ra.isLDBExists("CollHeadLDB")) {
			ra.setAttribute("CollHeadLDB", "INSPOL_IND", ra.getAttribute("RealEstateDialogLDB","RealEstate_txtInspolInd"));			
		}        
		if (ra.isLDBExists("CollSecPaperDialogLDB")) {
			ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehKasko", ra.getAttribute("RealEstateDialogLDB","RealEstate_txtInspolInd"));			
		}                   
      ra.exitScreen();
      ra.refreshActionList("tblInsuPolicy");

		}
	  
	public void change(){
		
        if (!(ra.isRequiredFilled())) {
 			return;
 		}
		    
//      ako nije popunjen spec_status napuniti sa default-om: 00
        String spec_stat = (String) ra.getAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT");
        
        if (spec_stat.equals("")) {
        	ra.setAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT", "00");
        }        
   
// validirati status police
        
        ra.setCursorPosition("InsuPolicyDialog_txtDateUntil");
        ra.invokeValidation("InsuPolicyDialog_txtDateUntil");    
        
       Integer update = (Integer) ra.showMessage("qer002");
       if (update != null && update.intValue() == 1) {
            try {
                ra.executeTransaction();
                ra.showMessage("infclt3");
            } catch (VestigoTMException vtme) {
                error("RevRegCoefReDialog -> update(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
                
                return;
            }
        }  
       
//     napuniti za vozila podatak za kasko policu	
		if (ra.isLDBExists("CollHeadLDB")) {
			ra.setAttribute("CollHeadLDB", "INSPOL_IND", ra.getAttribute("RealEstateDialogLDB","RealEstate_txtInspolInd"));			
		}        
		if (ra.isLDBExists("CollSecPaperDialogLDB")) {
			ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehKasko", ra.getAttribute("RealEstateDialogLDB","RealEstate_txtInspolInd"));			
		}                          
       
        ra.exitScreen();
        ra.refreshActionList("tblInsuPolicy");
	}//scr_update za transakciju
    
	public void delete(){
        Integer del = (Integer) ra.showMessage("qzst2");
        //provjerava da li korisnik zeli obrisati zapis
        if (del != null && del.intValue() == 1) {
            try {
                ra.executeTransaction();
                ra.showMessage("infcltzst1");
            } catch (VestigoTMException vtme) {
                error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
        }
//      napuniti za vozila podatak za kasko policu	
		if (ra.isLDBExists("CollHeadLDB")) {
			ra.setAttribute("CollHeadLDB", "INSPOL_IND", ra.getAttribute("RealEstateDialogLDB","RealEstate_txtInspolInd"));			
		}        
		if (ra.isLDBExists("CollSecPaperDialogLDB")) {
			ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehKasko", ra.getAttribute("RealEstateDialogLDB","RealEstate_txtInspolInd"));			
		}                 
        ra.exitScreen();
        ra.refreshActionList("tblInsuPolicy");
        //scr_delete za transakciju
	}
	
	 
    public boolean InsuPolicyDialog_txtSpecAct_FV(String elementName, Object elementValue, Integer lookUpType)
    {
    	//InsuPolicyDialog_txtAct
    	//InsuPolicyDialog_txtActDes
	 			
        if (elementValue == null || ((String) elementValue).equals("")) {
             ra.setAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT", "");
             ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtSpecActDes", "");
             ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);
             return true;
        }
        
        ra.setAttribute("InsuPolicyDialogLDB", "SysCodId", "clt_pol_spec_st");
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtSpecActDes", "");
        ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);
         
        if (!(ra.isLDBExists("SysCodeValueNewLookUpLDB"))) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "clt_pol_spec_st");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping("InsuPolicyDialogLDB", "IP_SPEC_STAT", "sys_code_value");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtSpecActDes", "sys_code_desc");
        request.addMapping("InsuPolicyDialogLDB", "dummyBD", "sys_cod_val_id");
           
        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
         
        ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);        
        ra.setAttribute("InsuPolicyDialogLDB", "SysCodId", "");
		
        return true;
    }
    
    /**
     * Validacijska funkcija za polje Status slanja obavijesti i opomena. 
     */
    public boolean InsuPolicyDialog_txtWrnStatusCode_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtWrnStatusCode", null);
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtWrnStatusDesc", null);
            return true;
        }

        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "ip_wrn_status");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtWrnStatusCode", "sys_code_value");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtWrnStatusDesc",  "sys_code_desc");
        request.addMapping("InsuPolicyDialogLDB", "dummyBD", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);        
        ra.setAttribute("InsuPolicyDialogLDB", "SysCodId", "");
        return true;
    }

    /**
     * Validacijska funkcija za polje Status poveæanja kamatne stope. 
     */
    public boolean InsuPolicyDialog_txtKmtStatusCode_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtKmtStatusCode", null);
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtKmtStatusDesc", null);
            return true;
        }

        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "ip_kmt_status");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtKmtStatusCode", "sys_code_value");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtKmtStatusDesc", "sys_code_desc");
        request.addMapping("InsuPolicyDialogLDB", "dummyBD", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);        
        ra.setAttribute("InsuPolicyDialogLDB", "SysCodId", "");

        return true;
    }
    
    public boolean InsuPolicyDialog_txtVinkulacija1RedSifra_FV(String elementName, Object elementValue, Integer lookUpType){
//        System.out.println("*** InsuPolicyDialog_txtVinkulacija1RedSifra_FV");
        
        //TODO: šifra: clt_vinkulacija_1_
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedSifra", null);
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedOpis", null);
            return true;
        }

        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedOpis", null);
        
        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) 
            ra.createLDB("SysCodeValueNewLookUpLDB");
        
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "clt_vinkulacija_1_");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedSifra", "sys_code_value");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedOpis",  "sys_code_desc");
        request.addMapping("InsuPolicyDialogLDB", "dummyBD", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);        
        ra.setAttribute("InsuPolicyDialogLDB", "SysCodId", "");
        
        if(ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtVinkulacija1RedSifra").equals("1")){
            ra.setRequired("InsuPolicyDialog_txtIznosIznosVinkuliranUKorist", false);
            ra.setRequired("InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValuta", false);
            
        }else {
            ra.setRequired("InsuPolicyDialog_txtIznosIznosVinkuliranUKorist", true);
            ra.setRequired("InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValuta", true);
        }
        
        return true;
        
    }
    
    
    public boolean InsuPolicyDialog_txtRBAVinkulacijaSifra_FV(String elementName, Object elementValue, Integer lookUpType){
//        System.out.println("*** InsuPolicyDialog_txtRBAVinkulacijaSifra_FV");
        
      //TODO: šifra: clt_rba_vinkulacij
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtRBAVinkulacijaSifra", null);
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtRBAVinkulacijaOpis", null);
            return true;
        }

        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtRBAVinkulacijaOpis", null);
        
        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) 
            ra.createLDB("SysCodeValueNewLookUpLDB");
        
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "clt_rba_vinkulacij");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtRBAVinkulacijaSifra", "sys_code_value");
        request.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtRBAVinkulacijaOpis",  "sys_code_desc");
        request.addMapping("InsuPolicyDialogLDB", "dummyBD", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute("InsuPolicyDialogLDB", "dummyBD", null);        
        ra.setAttribute("InsuPolicyDialogLDB", "SysCodId", "");
        return true;
    }
    
    public boolean InsuPolicyDialog_txtIznosOsiguraneSvoteValuta_FV(String elementName, Object elementValue, Integer lookUpType){
//        System.out.println("*** InsuPolicyDialog_txtIznosOsiguraneSvoteValuta_FV");
        
        if (elementValue == null || ((String) elementValue).equals("")) {                                          
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosOsiguraneSvoteValuta", "");                        
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID", null);                               
            return true;                                                                                 
        }

        ra.setAttribute("InsuPolicyDialogLDB", "VALUTA_NUM", "");
        ra.setAttribute("InsuPolicyDialogLDB", "VALUTA_NAME", "");
   
        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");          
        lookUpRequest.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosOsiguraneSvoteValutaID", "cur_id");           
        lookUpRequest.addMapping("InsuPolicyDialogLDB", "VALUTA_NUM", "code_num");
        lookUpRequest.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosOsiguraneSvoteValuta", "code_char");
        lookUpRequest.addMapping("InsuPolicyDialogLDB", "VALUTA_NAME", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }                                                                                              
        return true;     
        
    }
    
    
    public boolean InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValuta_FV(String elementName, Object elementValue, Integer lookUpType){
//        System.out.println("*** InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValuta_FV");
        
        if (elementValue == null || ((String) elementValue).equals("")) {                                          
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValuta", "");                        
            ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID", null);                               
            return true;                                                                                 
        }

        ra.setAttribute("InsuPolicyDialogLDB", "VALUTA_NUM", "");
        ra.setAttribute("InsuPolicyDialogLDB", "VALUTA_NAME", "");
   
        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");          
        lookUpRequest.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValutaID", "cur_id");           
        lookUpRequest.addMapping("InsuPolicyDialogLDB", "VALUTA_NUM", "code_num");
        lookUpRequest.addMapping("InsuPolicyDialogLDB", "InsuPolicyDialog_txtIznosIznosVinkuliranUKoristValuta", "code_char");
        lookUpRequest.addMapping("InsuPolicyDialogLDB", "VALUTA_NAME", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }                                                                                              
        return true;  
    }
    
    
    public boolean InsuPolicyDialog_txtSecuVal_FV(String elementName, Object elementValue, Integer lookUpType){
//        System.out.println("*** InsuPolicyDialog_txtSecuVal_FV");
        
        return true;
    }
    
    public boolean InsuPolicyDialog_txtIznosOsiguraneSvote_FV(String elementName, Object elementValue, Integer lookUpType){
//        System.out.println("*** InsuPolicyDialog_txtIznosOsiguraneSvote_FV");
        
        //kada se ovo polje promjeni treba provjeriti je li ovaj iznos manji od  InsuPolicyDialog_txtSecuVal i InsuPolicyDialog_txtIznosIznosVinkuliranUKorist 
        
        //potrebno preslikati iznos sa ovog polja na polje "Osigurana svota" (samo iznos ne i valutu)
        ra.setAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtSecuVal", elementValue);
        
        return true;
    }
    
    
    public boolean InsuPolicyDialog_txtIznosIznosVinkuliranUKorist_FV(String elementName, Object elementValue, Integer lookUpType){
//        System.out.println("*** InsuPolicyDialog_txtIznosIznosVinkuliranUKorist_FV");
        
        return true;
    }
    
    
	
	public void deact_polica(){
// akcija deaktivacije police osiguranja na listi aktivnih
// deaktivirati se moze samo polica koja je u statusu A
// akcija mijenja datum do kada vazi polica u datum deaktivacije
		String status_police = (String) ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtAct");
        if (status_police.equalsIgnoreCase("I")) {
        	ra.showMessage("wrnclt155");
 			return;
 		}
		  
       Integer update = (Integer) ra.showMessage("col_qer028");
       if (update != null && update.intValue() == 1) {
			ra.setAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateUntil", ra.getAttribute("GDB", "ProcessingDate")); 
			ra.setAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT", "DA");
			ra.setAttribute("InsuPolicyDialogLDB","proc_type_flag","D");
            try {
                ra.executeTransaction();
                ra.showMessage("infclt102");
            } catch (VestigoTMException vtme) {
                error("InsuPolicyDialog -> update(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
        }
 
        ra.exitScreen();
        ra.refreshActionList("tblInsuPolicy");
	}//
	    
	public void storno_polica(){
// akcija storniranja police		
// stornirati se moze samo polica koja je u statusu A
// akcija mijenja datum do kada vazi polica u datum deaktivacije, napomenu o polici u S
		String status_police = (String) ra.getAttribute("InsuPolicyDialogLDB", "InsuPolicyDialog_txtAct");
		if (status_police.equalsIgnoreCase("I")) {
		  	ra.showMessage("wrnclt156");
			return;
		}
				  
		Integer update = (Integer) ra.showMessage("col_qer029");
		if (update != null && update.intValue() == 1) {
			
			ra.setAttribute("InsuPolicyDialogLDB","InsuPolicyDialog_txtDateUntil", ra.getAttribute("GDB", "ProcessingDate"));
			ra.setAttribute("InsuPolicyDialogLDB","proc_type_flag","S");
			ra.setAttribute("InsuPolicyDialogLDB", "IP_SPEC_STAT", "S");
			try {
		         ra.executeTransaction();
		         ra.showMessage("infclt102");
		    } catch (VestigoTMException vtme) {
		         error("InsuPolicyDialog -> update_polica(): VestigoTMException", vtme);
		         if (vtme.getMessageID() != null)
		         ra.showMessage(vtme.getMessageID());
		   }
		}
		
		ra.exitScreen();
		ra.refreshActionList("tblInsuPolicy");
	}//

} 
