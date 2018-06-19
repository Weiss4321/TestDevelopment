/*
 * Created on 2006.05.06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;
   
import java.math.BigDecimal;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
 
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;
/** 
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollBoeDialog3 extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollBoeDialog3.java,v 1.12 2016/10/10 07:48:12 hrazst Exp $"; 

	LookUps coll_lookups = null;
	CollateralUtil coll_util = null;
	
	public CollBoeDialog3(ResourceAccessor ra) {
		super(ra);
		coll_util = new CollateralUtil(ra);
		coll_lookups = new LookUps(ra);
	}
	
	public void CollBoeDialog3_SE() {  
		
		if((ra.getScreenContext().compareTo("scr_change")== 0)  || (ra.getScreenContext().compareTo("scr_update")== 0)        ){
			//eligibility
			ra.setContext("CollBoeDialog_txtEligibility","fld_change_protected");
			ra.setContext("CollBoeDialog_txtEligDesc","fld_change_protected");
		
// validacija rba prihvatljivosti	
			
			if (ra.getAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibility") != null) {
				ra.setAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibilityDsc","");
				ra.setCursorPosition("ColBoeRba_txtEligibility");
				ra.invokeValidation("ColBoeRba_txtEligibility");
				ra.setCursorPosition("CollBoeDialog_txtRegNoInd1");	
			}				
		}
	
	}//CollBoeDialog3_SE
	
	  
	public boolean CollBoeDialog3_txtEligibility_FV(String elementName, Object elementValue, Integer lookUpType) {
	       
		if((ra.getScreenContext().compareTo("scr_change")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0)){			
			if (elementValue == null || ((String) elementValue).equals("")) {
                ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtEligibility", "");
                ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtEligDesc", "");
                ra.setAttribute("CollBoeDialogLDB", "dummyBD", null);
                return true;
            }
            coll_util.clearField("CollBoeDialogLDB", "CollBoeDialog_txtEligDesc");
    
            String sys_code_id="clt_eligib";
            LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
            value=coll_lookups.SystemCodeValue("CollBoeDialogLDB", "CollBoeDialog_txtEligibility", "CollBoeDialog_txtEligDesc", sys_code_id);
            if(value==null) return false;
            
            ra.setAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibility", value.sysCodeValue);
            ra.setAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibilityDsc", value.sysCodeDesc);  
		}
        return true;  
	}//CollBoeDialog2_txtEligibility_FV

	public boolean CollBoeDialog_txtRegNoInd1_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		//Indosant 1 na trecem ekranu	
		//CollBoeDialog_txtRegNoInd1
		//CollBoeDialog_txtCodeInd1
		//CollBoeDialog_txtNameInd1
		//CollBoeDialog_txtFNameInd1
		//CollBoeDialog_txtSNameInd1
		//CollBoeDialog_CUS_IDInd1
		
		
		//CollBoeDialog_txtAddressInd1           
		//CollBoeDialog_txtPlaceAddressInd1         
		//                  
		//CollBoeDialog_txtPostNrInd1               
		//CollBoeDialog_txtPostNameInd1        
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtRegNoInd1",""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCodeInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd1",null);  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd1","");   
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoeDialog_txtNameInd1")) {                                                      
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoInd1", "");
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeInd1", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd1",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd1","");   
			
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoInd1")) {                                              
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameInd1", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeInd1", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd1",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd1","");  
			                                                                                                    
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeInd1")) {
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameInd1", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoInd1", ""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd1",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd1","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd1","");  
		}                                                                                                                               
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtNameInd1") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtNameInd1");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoInd1") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoInd1");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtCodeInd1") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtCodeInd1");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtFNameInd1") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtFNameInd1");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtSNameInd1") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtSNameInd1");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoInd1"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeInd1"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtNameInd1"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtFNameInd1"))                                                      
			ra.setCursorPosition(2);   
		 
		 
		if (ra.isLDBExists("CustomerAndAddressLookUpLDB")) {                                                                                    
			ra.setAttribute("CustomerAndAddressLookUpLDB", "cus_id", null);                                                                       
			ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", "");                                                                    
			ra.setAttribute("CustomerAndAddressLookUpLDB", "code", "");                                                                           
			ra.setAttribute("CustomerAndAddressLookUpLDB", "name", "");                                                                           
			ra.setAttribute("CustomerAndAddressLookUpLDB", "address", "");   
			ra.setAttribute("CustomerAndAddressLookUpLDB", "postoffice", "");                                                                   
			ra.setAttribute("CustomerAndAddressLookUpLDB", "pos_off_name", "");                                                               
			ra.setAttribute("CustomerAndAddressLookUpLDB", "city_name", "");                                                                      
			ra.setAttribute("CustomerAndAddressLookUpLDB", "fname", null);
			ra.setAttribute("CustomerAndAddressLookUpLDB", "surname", null);
			   
			
		} else {                                                                                                                         
			ra.createLDB("CustomerAndAddressLookUpLDB");                                                                                          
		} 
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoInd1"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameInd1")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeInd1")); 
		      
                                                                                                                                     
		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAndAddressLookUp");                                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "cus_id", "cus_id");                                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "register_no", "register_no");                                                  
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "code", "code");                                                                
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "name", "name");                                                                
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "address", "address");                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "postoffice", "postoffice");                                                    
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "pos_off_name", "pos_off_name");                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "city_name", "city_name");                                                          
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "fname", "fname");
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "surname", "surname");
		
	                                
	                                                                                                                                   
		try {                                                                                                                            
			ra.callLookUp(lookUpRequest);                                                                                                  
		} catch (EmptyLookUp elu) {                                                                                                      
			ra.showMessage("err012");                                                                                                      
			return false;                                                                                                                  
		} catch (NothingSelected ns) {                                                                                                   
			return false;                                                                                                                  
		}  
		
		
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtAddressInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPlaceAddressInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNrInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNameInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		
                                                                                                                                     
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_CUS_IDInd1",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtFNameInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtSNameInd1", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		                                                                                                                                 
	
		
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//CollBoeDialog_txtRegNoInd1_FV                                                                                                                        
	public boolean CollBoeDialog_txtRegNoInd2_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		//Indosant 2 na trecem ekranu	
		//CollBoeDialog_txtRegNoInd2
		//CollBoeDialog_txtCodeInd2
		//CollBoeDialog_txtNameInd2
		//CollBoeDialog_txtFNameInd2
		//CollBoeDialog_txtSNameInd2
		//CollBoeDialog_CUS_IDInd2
		
		
		//CollBoeDialog_txtAddressInd2           
		//CollBoeDialog_txtPlaceAddressInd2         
		//                  
		//CollBoeDialog_txtPostNrInd2               
		//CollBoeDialog_txtPostNameInd2        
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtRegNoInd2",""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCodeInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd2",null);  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd2","");   
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoeDialog_txtNameInd2")) {                                                      
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoInd2", "");
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeInd2", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd2",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd2","");   
			
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoInd2")) {                                              
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameInd2", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeInd2", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd2",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd2","");  
			                                                                                                    
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeInd2")) {
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameInd2", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoInd2", ""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd2",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd2","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd2","");  
		}                                                                                                                               
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtNameInd2") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtNameInd2");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoInd2") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoInd2");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtCodeInd2") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtCodeInd2");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtFNameInd2") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtFNameInd2");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtSNameInd2") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtSNameInd2");                                     
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
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoInd2"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeInd2"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtNameInd2"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtFNameInd2"))                                                      
			ra.setCursorPosition(2);   
		 
		 
		if (ra.isLDBExists("CustomerAndAddressLookUpLDB")) {                                                                                    
			ra.setAttribute("CustomerAndAddressLookUpLDB", "cus_id", null);                                                                       
			ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", "");                                                                    
			ra.setAttribute("CustomerAndAddressLookUpLDB", "code", "");                                                                           
			ra.setAttribute("CustomerAndAddressLookUpLDB", "name", "");                                                                           
			ra.setAttribute("CustomerAndAddressLookUpLDB", "address", "");   
			ra.setAttribute("CustomerAndAddressLookUpLDB", "postoffice", "");                                                                   
			ra.setAttribute("CustomerAndAddressLookUpLDB", "pos_off_name", "");                                                               
			ra.setAttribute("CustomerAndAddressLookUpLDB", "city_name", "");                                                                      
			ra.setAttribute("CustomerAndAddressLookUpLDB", "fname", null);
			ra.setAttribute("CustomerAndAddressLookUpLDB", "surname", null);
			   
			
		} else {                                                                                                                         
			ra.createLDB("CustomerAndAddressLookUpLDB");                                                                                          
		} 
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoInd2"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameInd2")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeInd2")); 
		      
                                                                                                                                     
		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAndAddressLookUp");                                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "cus_id", "cus_id");                                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "register_no", "register_no");                                                  
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "code", "code");                                                                
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "name", "name");                                                                
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "address", "address");                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "postoffice", "postoffice");                                                    
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "pos_off_name", "pos_off_name");                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "city_name", "city_name");                                                          
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "fname", "fname");
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "surname", "surname");
		
	                                
	                                                                                                                                   
		try {                                                                                                                            
			ra.callLookUp(lookUpRequest);                                                                                                  
		} catch (EmptyLookUp elu) {                                                                                                      
			ra.showMessage("err012");                                                                                                      
			return false;                                                                                                                  
		} catch (NothingSelected ns) {                                                                                                   
			return false;                                                                                                                  
		}  
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtAddressInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPlaceAddressInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNrInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNameInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		
                                                                                                                                     
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_CUS_IDInd2",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtFNameInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtSNameInd2", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//CollBoeDialog_txtRegNoInd2_FV                                                                                                                        
	public boolean CollBoeDialog_txtRegNoInd3_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		//Indosant 3 na trecem ekranu	
		//CollBoeDialog_txtRegNoInd3
		//CollBoeDialog_txtCodeInd3
		//CollBoeDialog_txtNameInd3
		//CollBoeDialog_txtFNameInd3
		//CollBoeDialog_txtSNameInd3
		//CollBoeDialog_CUS_IDInd3
		
		
		//CollBoeDialog_txtAddressInd3           
		//CollBoeDialog_txtPlaceAddressInd3         
		//                  
		//CollBoeDialog_txtPostNrInd3               
		//CollBoeDialog_txtPostNameInd3        
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtRegNoInd3",""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCodeInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd3",null);  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd3","");   
			
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoeDialog_txtNameInd3")) {                                                      
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoInd3", "");
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeInd3", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd3",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd3","");   
			
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoInd3")) {                                              
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameInd3", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeInd3", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd3",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd3","");  
			                                                                                                    
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeInd3")) {
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameInd3", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoInd3", ""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd3",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd3","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd3","");  
		}                                                                                                                               
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtNameInd3") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtNameInd3");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoInd3") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoInd3");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtCodeInd3") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtCodeInd3");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtFNameInd3") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtFNameInd3");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtSNameInd3") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtSNameInd3");                                     
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
		if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoInd3"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeInd3"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		if (ra.getCursorPosition().equals("CollBoeDialog_txtNameInd3"))                                                      
			ra.setCursorPosition(3);   
		if (ra.getCursorPosition().equals("CollBoeDialog_txtFNameInd3"))                                                      
			ra.setCursorPosition(2);   
		 
		if (ra.isLDBExists("CustomerAndAddressLookUpLDB")) {                                                                                    
			ra.setAttribute("CustomerAndAddressLookUpLDB", "cus_id", null);                                                                       
			ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", "");                                                                    
			ra.setAttribute("CustomerAndAddressLookUpLDB", "code", "");                                                                           
			ra.setAttribute("CustomerAndAddressLookUpLDB", "name", "");                                                                           
			ra.setAttribute("CustomerAndAddressLookUpLDB", "address", "");   
			ra.setAttribute("CustomerAndAddressLookUpLDB", "postoffice", "");                                                                   
			ra.setAttribute("CustomerAndAddressLookUpLDB", "pos_off_name", "");                                                               
			ra.setAttribute("CustomerAndAddressLookUpLDB", "city_name", "");                                                                      
			ra.setAttribute("CustomerAndAddressLookUpLDB", "fname", null);
			ra.setAttribute("CustomerAndAddressLookUpLDB", "surname", null);
			
		} else {                                                                                                                         
			ra.createLDB("CustomerAndAddressLookUpLDB");                                                                                          
		} 
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoInd3"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameInd3")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeInd3")); 
                                                                                                                                     
		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAndAddressLookUp");                                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "cus_id", "cus_id");                                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "register_no", "register_no");                                                  
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "code", "code");                                                                
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "name", "name");                                                                
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "address", "address");                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "postoffice", "postoffice");                                                    
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "pos_off_name", "pos_off_name");                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "city_name", "city_name");                                                          
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "fname", "fname");
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "surname", "surname");
	                                                                                                                                   
		try {                                                                                                                            
			ra.callLookUp(lookUpRequest);                                                                                                  
		} catch (EmptyLookUp elu) {                                                                                                      
			ra.showMessage("err012");                                                                                                      
			return false;                                                                                                                  
		} catch (NothingSelected ns) {                                                                                                   
			return false;                                                                                                                  
		}  
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtAddressInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPlaceAddressInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
	
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNrInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNameInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
                                                                                                                                   
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_CUS_IDInd3",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtFNameInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtSNameInd3", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//CollBoeDialog_txtRegNoInd3_FV                                                                                                                        
	public boolean CollBoeDialog_txtRegNoInd4_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		//Indosant 4 na trecem ekranu	
		//CollBoeDialog_txtRegNoInd4
		//CollBoeDialog_txtCodeInd4
		//CollBoeDialog_txtNameInd4
		//CollBoeDialog_txtFNameInd4
		//CollBoeDialog_txtSNameInd4
		//CollBoeDialog_CUS_IDInd4
		
		
		//CollBoeDialog_txtAddressInd4           
		//CollBoeDialog_txtPlaceAddressInd4         
		//                  
		//CollBoeDialog_txtPostNrInd4               
		//CollBoeDialog_txtPostNameInd4        
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtRegNoInd4",""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCodeInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd4",null);  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd4","");   
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoeDialog_txtNameInd4")) {                                                      
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoInd4", "");
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeInd4", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd4",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd4","");   
			
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoInd4")) {                                              
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameInd4", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeInd4", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd4",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd4","");  
			                                                                                                    
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeInd4")) {
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameInd4", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoInd4", ""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_IDInd4",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrInd4","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameInd4","");  
		}                                                                                                                               
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtNameInd4") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtNameInd4");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoInd4") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoInd4");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtCodeInd4") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtCodeInd4");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtFNameInd4") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtFNameInd4");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtSNameInd4") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtSNameInd4");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoInd4"))                                                      
		    ra.setCursorPosition(5);                                                                                                       
		if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeInd4"))                                                      
		    ra.setCursorPosition(4);   			                                                                                                                           
		if (ra.getCursorPosition().equals("CollBoeDialog_txtNameInd4"))                                                      
		    ra.setCursorPosition(3);   
        if (ra.getCursorPosition().equals("CollBoeDialog_txtFNameInd4"))                                                      
            ra.setCursorPosition(2);   
		 
		if (ra.isLDBExists("CustomerAndAddressLookUpLDB")) {                                                                                    
			ra.setAttribute("CustomerAndAddressLookUpLDB", "cus_id", null);                                                                       
			ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", "");                                                                    
			ra.setAttribute("CustomerAndAddressLookUpLDB", "code", "");                                                                           
			ra.setAttribute("CustomerAndAddressLookUpLDB", "name", "");                                                                           
			ra.setAttribute("CustomerAndAddressLookUpLDB", "address", "");   
			ra.setAttribute("CustomerAndAddressLookUpLDB", "postoffice", "");                                                                   
			ra.setAttribute("CustomerAndAddressLookUpLDB", "pos_off_name", "");                                                               
			ra.setAttribute("CustomerAndAddressLookUpLDB", "city_name", "");                                                                      
			ra.setAttribute("CustomerAndAddressLookUpLDB", "fname", null);
			ra.setAttribute("CustomerAndAddressLookUpLDB", "surname", null);
		} else {                                                                                                                         
			ra.createLDB("CustomerAndAddressLookUpLDB");                                                                                          
		} 
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoInd4"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameInd4")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeInd4")); 
		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAndAddressLookUp");                                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "cus_id", "cus_id");                                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "register_no", "register_no");                                                  
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "code", "code");                                                                
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "name", "name");                                                                
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "address", "address");                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "postoffice", "postoffice");                                                    
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "pos_off_name", "pos_off_name");                                            
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "city_name", "city_name");                                                          
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "fname", "fname");
		lookUpRequest.addMapping("CustomerAndAddressLookUpLDB", "surname", "surname");
	                                                                                                                                   
		try {                                                                                                                            
			ra.callLookUp(lookUpRequest);                                                                                                  
		} catch (EmptyLookUp elu) {                                                                                                      
			ra.showMessage("err012");                                                                                                      
			return false;                                                                                                                  
		} catch (NothingSelected ns) {                                                                                                   
			return false;                                                                                                                  
		}  
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtAddressInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPlaceAddressInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNrInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNameInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_CUS_IDInd4",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtFNameInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtSNameInd4", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		ldbName = null;                                                                                                                  
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//CollBoeDialog_txtRegNoInd4_FV                                                                                                                        

    
    public boolean ColBoeRba_txtEligibility_FV(String ElName, Object ElValue, Integer LookUp) {  
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibility", "");   
            ra.setAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibilityDsc", "");
            return true;                                                                                 
        }  
        
        coll_util.clearField("CollBoeDialogLDB", "ColBoeRba_txtEligibilityDsc");

        String sys_code_id="clt_rba_eligib";
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue("CollBoeDialogLDB", "ColBoeRba_txtEligibility", "ColBoeRba_txtEligibilityDsc", sys_code_id);
        if(value==null) return false;
        
        ra.setAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibility", value.sysCodeValue);
        ra.setAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibilityDsc", value.sysCodeDesc);  
        return true;        
    }
}
