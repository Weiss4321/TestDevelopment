/*
 * Created on 2007.02.12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;
 
import java.math.BigDecimal;
import java.sql.Date;
 
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;
   
/**
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Agreement extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/Agreement.java,v 1.8 2017/03/07 08:23:41 hrazst Exp $";
	
	LookUps coll_lookups = null;
	
	public Agreement(ResourceAccessor ra) {
		super(ra);
		coll_lookups = new LookUps(ra);
	}
	
	public void Agreement_SE() {
		if (!(ra.isLDBExists("AgreementLDB"))) {
			ra.createLDB("AgreementLDB");
		}  
		//  dohvat podataka
		
		if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || 
			ra.getScreenContext().equalsIgnoreCase("scr_change") ||
			ra.getScreenContext().equalsIgnoreCase("scr_close") || 
			ra.getScreenContext().equalsIgnoreCase("scr_change_act")) {
			ra.setAttribute("AgreementLDB", "fra_agr_id", ra.getAttribute("AgreementListLDB", "w_fra_agr_id"));		
			try {
				ra.executeTransaction();
			} catch (VestigoTMException vtme) {
				if (vtme.getMessageID() != null)
					ra.showMessage(vtme.getMessageID());
			}
		}
		if (ra.getScreenContext().equalsIgnoreCase("scr_add") || ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_change_act")) {
			set_InitData();
		}

	} 	
	public void	set_InitData() {
	    // prvi unos		
		ra.setAttribute("AgreementLDB", "use_id",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("AgreementLDB", "org_uni_id",(java.math.BigDecimal) ra.getAttribute("GDB", "org_uni_id"));	
		ra.setAttribute("AgreementLDB","Coll_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
		ra.setAttribute("AgreementLDB","Coll_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));

		if (ra.getScreenContext().equalsIgnoreCase("scr_add")) {			
			ra.setAttribute("AgreementLDB", "use_open_id",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
			ra.setAttribute("AgreementLDB", "org_uni_open_id",(java.math.BigDecimal) ra.getAttribute("GDB", "org_uni_id"));
			ra.setAttribute("AgreementLDB","Coll_txtUseOpenIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
			ra.setAttribute("AgreementLDB","Coll_txtUseOpenIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
			ra.setAttribute("AgreementLDB","txtSporazumMZ","N");
		}		
	}
	
	 public void confirm() {	 
	 	
	 	if (!(ra.isRequiredFilled())) {
            return; 
        }
	 	
//		  pitanje da li stvarno zeli potvrditi podatke	 	
		Integer retValue = (Integer) ra.showMessage("col_qer004");
		if (retValue!=null && retValue.intValue() == 0) return;	 	
		
		ra.setAttribute("AgreementLDB", "user_lock_in", ra.getAttribute("AgreementLDB", "user_lock")); 	 	
		  

		 BigDecimal fra_agr_id = (BigDecimal) ra.getAttribute("AgreementLDB","fra_agr_id");
		 	
		 if (ra.getScreenContext().equalsIgnoreCase("scr_add") && fra_agr_id != null) {
		 	ra.setScreenContext("scr_change");
		 }
		 
		 if (ra.getScreenContext().equalsIgnoreCase("scr_change_act")) 
		 	ra.setAttribute("AgreementLDB", "update_flag","A");
		 else
		 	ra.setAttribute("AgreementLDB", "update_flag","B");
		 		  	 
//		  update bez obzira da li je bilo promjene na ekranu 			
			
		 if (ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_change_act")) {
//	 update podataka (ctx=scr_update || scr_update_act)	 			
		 	try {
		 		ra.executeTransaction();
		 		ra.showMessage("infclt3");
		 	} catch (VestigoTMException vtme) {
		 		error("Agreement -> update(): VestigoTMException", vtme);
		 		if (vtme.getMessageID() != null)
		 			ra.showMessage(vtme.getMessageID());
		 	}	 			
  
		 } else {
//	 insert podataka (ctx = scr_add)	 	
		 	try { 
		 		ra.executeTransaction(); 
		 		ra.showMessage("infclt2");
		 	} catch (VestigoTMException vtme) {
		 		error("Agreement -> insert(): VestigoTMException", vtme);
		 		if (vtme.getMessageID() != null)
		 			ra.showMessage(vtme.getMessageID());
		 	}
		 }	
		if (((Integer) ra.getAttribute("GDB", "TransactionStatus")).equals(new Integer("0"))) {	 		 
			ra.exitScreen();
			ra.invokeAction("refresh");
		}
	 }
	 
	 public void exit() {  
	 	ra.exitScreen();
		ra.invokeAction("refresh");
	 }
	 
	 public void agr_hypo () {
//		ra.showMessage("inf_1800");	
		ra.loadScreen("CollHfPrior","detail_agr");		
	 } 
	     
	 public void agr_loan () {
//		ra.showMessage("inf_1800");	
		ra.loadScreen("LoanBeneficiary","detail_agr");		
		
	 } 
	 
	public boolean Agr_txtRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "AgreementLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute(ldbName,"Agr_txtRegNo","");
			ra.setAttribute(ldbName,"Agr_txtCode","");
			ra.setAttribute(ldbName,"Agr_txtName","");
			ra.setAttribute(ldbName,"agr_cus_id",null);
	        ra.setAttribute(ldbName,"Agr_txtOIB",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("Agr_txtName")) {
			ra.setAttribute(ldbName, "Agr_txtRegNo", "");
		} else if (ra.getCursorPosition().equals("Agr_txtRegNo")) {
			ra.setAttribute(ldbName, "Agr_txtName", "");
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "Agr_txtName") != null){
			d_name = (String) ra.getAttribute(ldbName, "Agr_txtName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Agr_txtRegNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Agr_txtRegNo");
		}
		
		if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
			ra.showMessage("wrn366");
			return false;
		}
		
		//da li je zvjezdica na pravom mjestu kod register_no
		if (CharUtil.isAsteriskWrong(d_register_no)) {
			ra.showMessage("wrn367");
			return false;
		}
		
		
		 if (ra.getCursorPosition().equals("Agr_txtRegNo")) 
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
	                                                                                                                                       
	        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute("AgreementLDB", "Agr_txtRegNo"));
	        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute("AgreementLDB", "Agr_txtName")); 
	 
	                                                                                                                                     
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
		  
		ra.setAttribute(ldbName, "agr_cus_id", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));
		ra.setAttribute(ldbName, "Agr_txtRegNo", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));
		ra.setAttribute(ldbName, "Agr_txtCode", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "code"));
		ra.setAttribute(ldbName, "Agr_txtName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));
	    ra.setAttribute(ldbName, "Agr_txtOIB", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "oib_lk"));
			
		return true;

		
	}//Agr_txtRegNo_FV
	
	
	public boolean Agr_txtCur_FV(String ElName, Object ElValue, Integer LookUp) {
		
		String ldbName = "AgreementLDB";
		
		if (ElValue == null || ((String) ElValue).equals("")) {                                          
			ra.setAttribute(ldbName, "Agr_txtCur", "");                        
			ra.setAttribute(ldbName, "agr_cur_id", null);                               
			return true;                                                                                 
		}

		ra.setAttribute("AgreementListLDB", "dummySt", "");
		
//		LookUpRequest lookUpRequest = new LookUpRequest("FPaymCurrencyLookUp");        
		LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   		
		lookUpRequest.addMapping(ldbName, "agr_cur_id", "cur_id");           
		lookUpRequest.addMapping("AgreementListLDB", "dummySt", "code_num");
		lookUpRequest.addMapping(ldbName, "Agr_txtCur", "code_char");
		lookUpRequest.addMapping("AgreementListLDB", "dummySt", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}   
		
		return true;     
	} // Agr_txtCur_FV(
	
	
	public boolean Agr_txtDateUntil_FV(String elementName, Object elementValue, Integer lookUpType){
//		datum mora biti veci ili jednak current date
 
		Date doc_date = (Date) ra.getAttribute("AgreementLDB", "Agr_txtDateUntil");
		Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
		if (doc_date == null || current_date == null) 
			return true;

		if ((doc_date).before(current_date)) {
			ra.showMessage("wrnclt125");
			return false;
		}
		return true;					
	}
	
    public boolean Agreement_txtSporazumMZ_FV(String ElName, Object ElValue, Integer LookUp) {
        Boolean x = coll_lookups.ConfirmDN("AgreementLDB", ElName, ElValue);
        
        if("D".equals((String) ra.getAttribute("AgreementLDB", ElName))){
            ra.setRequired("Agr_txtNumOfBill", false);
        }else{
            ra.setRequired("Agr_txtNumOfBill", true);
        }
        
        return x;
    }
	
}
