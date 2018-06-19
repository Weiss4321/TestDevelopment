/*
 * Created on 2006.05.06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;

import hr.vestigo.framework.util.CharUtil;
/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollBoeDialog4 extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollBoeDialog4.java,v 1.3 2006/11/23 10:43:27 hramkr Exp $"; 

	
	public CollBoeDialog4(ResourceAccessor ra) {
		super(ra);
	}
	  
	public void CollBoeDialog4_SE() {
		// ako je flag mjenica avalirana na N treba zaprotektirati cijeli ekran - u kontekstu za unos podataka 
		if(ra.getScreenContext().compareTo("scr_change")== 0 || (ra.getScreenContext().compareTo("scr_update")== 0) ){		
			String aval_flag = (String) ra.getAttribute("CollBoeDialogLDB", "Kol_txtAval");
			if (aval_flag != null && aval_flag.trim().equalsIgnoreCase("D")) {

				ra.setContext("KolBD_txtRegNoAval1","fld_plain");
				ra.setContext("KolBD_txtCodeAval1","fld_plain");
				ra.setContext("KolBD_txtNameAval1","fld_plain");
			
				ra.setContext("KolBD_txtRegNoAval2","fld_plain");
				ra.setContext("KolBD_txtCodeAval2","fld_plain");
				ra.setContext("KolBD_txtNameAval2","fld_plain");
			
				ra.setContext("KolBD_txtRegNoAval3","fld_plain");
				ra.setContext("KolBD_txtCodeAval3","fld_plain");
				ra.setContext("KolBD_txtNameAval3","fld_plain");
			
				ra.setContext("KolBD_txtRegNoAval4","fld_plain");
				ra.setContext("KolBD_txtCodeAval4","fld_plain");
				ra.setContext("KolBD_txtNameAval4","fld_plain");
			
			}  
		}  
		
	}//CollBoeDialog4_SE
	  
	 
 
	public boolean KolBD_txtRegNoAval1_FV(String elementName, Object elementValue, Integer lookUpType) {                                 

//		cus_id_aval1
//		KolBD_txtRegNoAval1
//		KolBD_txtCodeAval1
//		KolBD_txtNameAval1
//		KolBD_txtFNameAval1
//		KolBD_txtSNameAval1
//		CollBoeDialog_txtAddressAv1
//		CollBoeDialog_txtPlaceAddressAv1
//		KolBD_txtPostNrAval1
//		KolBD_txtPostNameAval1
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute(ldbName,"KolBD_txtRegNoAval1",""); 
			ra.setAttribute(ldbName,"KolBD_txtCodeAval1","");
			ra.setAttribute(ldbName,"KolBD_txtNameAval1","");
			ra.setAttribute(ldbName,"KolBD_txtFNameAval1","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval1","");
			ra.setAttribute(ldbName,"cus_id_aval1",null);  
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv1","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv1","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval1","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval1","");   
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("KolBD_txtNameAval1")) {                                                      
			ra.setAttribute(ldbName, "KolBD_txtRegNoAval1", "");
			ra.setAttribute(ldbName, "KolBD_txtCodeAval1", "");  
			ra.setAttribute(ldbName,"KolBD_txtFNameAval1","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval1","");
			ra.setAttribute(ldbName,"cus_id_aval1",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv1","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv1","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval1","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval1","");   
			
			
		} else if (ra.getCursorPosition().equals("KolBD_txtRegNoAval1")) {                                              
			ra.setAttribute(ldbName, "KolBD_txtNameAval1", "");  
			ra.setAttribute(ldbName, "KolBD_txtCodeAval1", "");  
			ra.setAttribute(ldbName,"KolBD_txtFNameAval1","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval1","");
			ra.setAttribute(ldbName,"cus_id_aval1",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv1","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv1","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval1","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval1","");  
			
			
			                                                                                                    
		} else if (ra.getCursorPosition().equals("KolBD_txtCodeAval1")) {
			ra.setAttribute(ldbName, "KolBD_txtNameAval1", "");  
			ra.setAttribute(ldbName, "KolBD_txtRegNoAval1", ""); 
			ra.setAttribute(ldbName,"KolBD_txtFNameAval1","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval1","");
			ra.setAttribute(ldbName,"cus_id_aval1",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv1","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv1","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval1","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval1","");  
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "KolBD_txtNameAval1") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "KolBD_txtNameAval1");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "KolBD_txtRegNoAval1") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "KolBD_txtRegNoAval1");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtCodeAval1") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "KolBD_txtCodeAval1");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtFNameAval1") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "KolBD_txtFNameAval1");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtSNameAval1") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "KolBD_txtSNameAval1");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("KolBD_txtRegNoAval1"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("KolBD_txtCodeAval1"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("KolBD_txtNameAval1"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("KolBD_txtFNameAval1"))                                                      
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
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute(ldbName, "KolBD_txtRegNoAval1"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute(ldbName, "KolBD_txtNameAval1")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute(ldbName, "KolBD_txtCodeAval1")); 
		      
                                                                                                                                     
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
		
		ra.setAttribute(ldbName, "CollBoeDialog_txtAddressAv1", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute(ldbName, "CollBoeDialog_txtPlaceAddressAv1", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		
		ra.setAttribute(ldbName, "KolBD_txtPostNrAval1", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute(ldbName, "KolBD_txtPostNameAval1", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		
                                                                                                                                     
		ra.setAttribute(ldbName, "cus_id_aval1",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute(ldbName, "KolBD_txtRegNoAval1", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute(ldbName, "KolBD_txtNameAval1", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute(ldbName, "KolBD_txtCodeAval1", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute(ldbName, "KolBD_txtFNameAval1", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute(ldbName, "KolBD_txtSNameAval1", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		                                                                                                                                 
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//KolBD_txtRegNoAval1_FV
	

	public boolean KolBD_txtRegNoAval2_FV(String elementName, Object elementValue, Integer lookUpType) {                                 

//		cus_id_aval2
//		KolBD_txtRegNoAval2
//		KolBD_txtCodeAval2
//		KolBD_txtNameAval2
//		KolBD_txtFNameAval2
//		KolBD_txtSNameAval2
//		CollBoeDialog_txtAddressAv2
//		CollBoeDialog_txtPlaceAddressAv2
//		KolBD_txtPostNrAval2
//		KolBD_txtPostNameAval2
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute(ldbName,"KolBD_txtRegNoAval2",""); 
			ra.setAttribute(ldbName,"KolBD_txtCodeAval2","");
			ra.setAttribute(ldbName,"KolBD_txtNameAval2","");
			ra.setAttribute(ldbName,"KolBD_txtFNameAval2","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval2","");
			ra.setAttribute(ldbName,"cus_id_aval2",null);  
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv2","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv2","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval2","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval2","");   
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("KolBD_txtNameAval2")) {                                                      
			ra.setAttribute(ldbName, "KolBD_txtRegNoAval2", "");
			ra.setAttribute(ldbName, "KolBD_txtCodeAval2", "");  
			ra.setAttribute(ldbName,"KolBD_txtFNameAval2","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval2","");
			ra.setAttribute(ldbName,"cus_id_aval2",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv2","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv2","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval2","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval2","");   
			
			
		} else if (ra.getCursorPosition().equals("KolBD_txtRegNoAval2")) {                                              
			ra.setAttribute(ldbName, "KolBD_txtNameAval2", "");  
			ra.setAttribute(ldbName, "KolBD_txtCodeAval2", "");  
			ra.setAttribute(ldbName,"KolBD_txtFNameAval2","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval2","");
			ra.setAttribute(ldbName,"cus_id_aval2",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv2","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv2","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval2","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval2","");  
			
			
			                                                                                                    
		} else if (ra.getCursorPosition().equals("KolBD_txtCodeAval2")) {
			ra.setAttribute(ldbName, "KolBD_txtNameAval2", "");  
			ra.setAttribute(ldbName, "KolBD_txtRegNoAval2", ""); 
			ra.setAttribute(ldbName,"KolBD_txtFNameAval2","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval2","");
			ra.setAttribute(ldbName,"cus_id_aval2",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv2","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv2","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval2","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval2","");  
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "KolBD_txtNameAval2") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "KolBD_txtNameAval2");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "KolBD_txtRegNoAval2") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "KolBD_txtRegNoAval2");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtCodeAval2") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "KolBD_txtCodeAval2");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtFNameAval2") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "KolBD_txtFNameAval2");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtSNameAval2") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "KolBD_txtSNameAval2");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("KolBD_txtRegNoAval2"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("KolBD_txtCodeAval2"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("KolBD_txtNameAval2"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("KolBD_txtFNameAval2"))                                                      
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
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute(ldbName, "KolBD_txtRegNoAval2"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute(ldbName, "KolBD_txtNameAval2")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute(ldbName, "KolBD_txtCodeAval2")); 
		      
                                                                                                                                     
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
		
		ra.setAttribute(ldbName, "CollBoeDialog_txtAddressAv2", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute(ldbName, "CollBoeDialog_txtPlaceAddressAv2", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		
		ra.setAttribute(ldbName, "KolBD_txtPostNrAval2", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute(ldbName, "KolBD_txtPostNameAval2", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		
                                                                                                                                     
		ra.setAttribute(ldbName, "cus_id_aval2",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute(ldbName, "KolBD_txtRegNoAval2", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute(ldbName, "KolBD_txtNameAval2", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute(ldbName, "KolBD_txtCodeAval2", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute(ldbName, "KolBD_txtFNameAval2", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute(ldbName, "KolBD_txtSNameAval2", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		                                                                                                                                 
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//KolBD_txtRegNoAval2_FV
	
	
	public boolean KolBD_txtRegNoAval3_FV(String elementName, Object elementValue, Integer lookUpType) {                                 

//		cus_id_aval3
//		KolBD_txtRegNoAval3
//		KolBD_txtCodeAval3
//		KolBD_txtNameAval3
//		KolBD_txtFNameAval3
//		KolBD_txtSNameAval3
//		CollBoeDialog_txtAddressAv3
//		CollBoeDialog_txtPlaceAddressAv3
//		KolBD_txtPostNrAval3
//		KolBD_txtPostNameAval3
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute(ldbName,"KolBD_txtRegNoAval3",""); 
			ra.setAttribute(ldbName,"KolBD_txtCodeAval3","");
			ra.setAttribute(ldbName,"KolBD_txtNameAval3","");
			ra.setAttribute(ldbName,"KolBD_txtFNameAval3","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval3","");
			ra.setAttribute(ldbName,"cus_id_aval3",null);  
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv3","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv3","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval3","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval3","");   
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("KolBD_txtNameAval3")) {                                                      
			ra.setAttribute(ldbName, "KolBD_txtRegNoAval3", "");
			ra.setAttribute(ldbName, "KolBD_txtCodeAval3", "");  
			ra.setAttribute(ldbName,"KolBD_txtFNameAval3","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval3","");
			ra.setAttribute(ldbName,"cus_id_aval3",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv3","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv3","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval3","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval3","");   
			
			
		} else if (ra.getCursorPosition().equals("KolBD_txtRegNoAval3")) {                                              
			ra.setAttribute(ldbName, "KolBD_txtNameAval3", "");  
			ra.setAttribute(ldbName, "KolBD_txtCodeAval3", "");  
			ra.setAttribute(ldbName,"KolBD_txtFNameAval3","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval3","");
			ra.setAttribute(ldbName,"cus_id_aval3",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv3","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv3","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval3","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval3","");  
			
			
			                                                                                                    
		} else if (ra.getCursorPosition().equals("KolBD_txtCodeAval3")) {
			ra.setAttribute(ldbName, "KolBD_txtNameAval3", "");  
			ra.setAttribute(ldbName, "KolBD_txtRegNoAval3", ""); 
			ra.setAttribute(ldbName,"KolBD_txtFNameAval3","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval3","");
			ra.setAttribute(ldbName,"cus_id_aval3",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv3","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv3","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval3","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval3","");  
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "KolBD_txtNameAval3") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "KolBD_txtNameAval3");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "KolBD_txtRegNoAval3") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "KolBD_txtRegNoAval3");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtCodeAval3") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "KolBD_txtCodeAval3");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtFNameAval3") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "KolBD_txtFNameAval3");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtSNameAval3") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "KolBD_txtSNameAval3");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("KolBD_txtRegNoAval3"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("KolBD_txtCodeAval3"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("KolBD_txtNameAval3"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("KolBD_txtFNameAval3"))                                                      
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
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute(ldbName, "KolBD_txtRegNoAval3"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute(ldbName, "KolBD_txtNameAval3")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute(ldbName, "KolBD_txtCodeAval3")); 
		      
                                                                                                                                     
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
		
		ra.setAttribute(ldbName, "CollBoeDialog_txtAddressAv3", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute(ldbName, "CollBoeDialog_txtPlaceAddressAv3", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		
		ra.setAttribute(ldbName, "KolBD_txtPostNrAval3", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute(ldbName, "KolBD_txtPostNameAval3", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		
                                                                                                                                     
		ra.setAttribute(ldbName, "cus_id_aval3",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute(ldbName, "KolBD_txtRegNoAval3", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute(ldbName, "KolBD_txtNameAval3", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute(ldbName, "KolBD_txtCodeAval3", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute(ldbName, "KolBD_txtFNameAval3", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute(ldbName, "KolBD_txtSNameAval3", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		                                                                                                                                 
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//KolBD_txtRegNoAval3_FV
	
	
	
	public boolean KolBD_txtRegNoAval4_FV(String elementName, Object elementValue, Integer lookUpType) {                                 

//		cus_id_aval4
//		KolBD_txtRegNoAval4
//		KolBD_txtCodeAval4
//		KolBD_txtNameAval4
//		KolBD_txtFNameAval4
//		KolBD_txtSNameAval4
//		CollBoeDialog_txtAddressAv4
//		CollBoeDialog_txtPlaceAddressAv4
//		KolBD_txtPostNrAval4
//		KolBD_txtPostNameAval4
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute(ldbName,"KolBD_txtRegNoAval4",""); 
			ra.setAttribute(ldbName,"KolBD_txtCodeAval4","");
			ra.setAttribute(ldbName,"KolBD_txtNameAval4","");
			ra.setAttribute(ldbName,"KolBD_txtFNameAval4","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval4","");
			ra.setAttribute(ldbName,"cus_id_aval4",null);  
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv4","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv4","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval4","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval4","");   
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("KolBD_txtNameAval4")) {                                                      
			ra.setAttribute(ldbName, "KolBD_txtRegNoAval4", "");
			ra.setAttribute(ldbName, "KolBD_txtCodeAval4", "");  
			ra.setAttribute(ldbName,"KolBD_txtFNameAval4","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval4","");
			ra.setAttribute(ldbName,"cus_id_aval4",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv4","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv4","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval4","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval4","");   
			
			
		} else if (ra.getCursorPosition().equals("KolBD_txtRegNoAval4")) {                                              
			ra.setAttribute(ldbName, "KolBD_txtNameAval4", "");  
			ra.setAttribute(ldbName, "KolBD_txtCodeAval4", "");  
			ra.setAttribute(ldbName,"KolBD_txtFNameAval4","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval4","");
			ra.setAttribute(ldbName,"cus_id_aval4",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv4","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv4","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval4","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval4","");  
			
			
			                                                                                                    
		} else if (ra.getCursorPosition().equals("KolBD_txtCodeAval4")) {
			ra.setAttribute(ldbName, "KolBD_txtNameAval4", "");  
			ra.setAttribute(ldbName, "KolBD_txtRegNoAval4", ""); 
			ra.setAttribute(ldbName,"KolBD_txtFNameAval4","");
			ra.setAttribute(ldbName,"KolBD_txtSNameAval4","");
			ra.setAttribute(ldbName,"cus_id_aval4",null);   
			ra.setAttribute(ldbName,"CollBoeDialog_txtAddressAv4","");
			ra.setAttribute(ldbName,"CollBoeDialog_txtPlaceAddressAv4","");
			ra.setAttribute(ldbName,"KolBD_txtPostNrAval4","");
			ra.setAttribute(ldbName,"KolBD_txtPostNameAval4","");  
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "KolBD_txtNameAval4") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "KolBD_txtNameAval4");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "KolBD_txtRegNoAval4") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "KolBD_txtRegNoAval4");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtCodeAval4") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "KolBD_txtCodeAval4");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtFNameAval4") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "KolBD_txtFNameAval4");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "KolBD_txtSNameAval4") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "KolBD_txtSNameAval4");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("KolBD_txtRegNoAval4"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("KolBD_txtCodeAval4"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("KolBD_txtNameAval4"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("KolBD_txtFNameAval4"))                                                      
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
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute(ldbName, "KolBD_txtRegNoAval4"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute(ldbName, "KolBD_txtNameAval4")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute(ldbName, "KolBD_txtCodeAval4")); 
		      
                                                                                                                                     
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
		
		ra.setAttribute(ldbName, "CollBoeDialog_txtAddressAv4", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute(ldbName, "CollBoeDialog_txtPlaceAddressAv4", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		
		ra.setAttribute(ldbName, "KolBD_txtPostNrAval4", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute(ldbName, "KolBD_txtPostNameAval4", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		
                                                                                                                                     
		ra.setAttribute(ldbName, "cus_id_aval4",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute(ldbName, "KolBD_txtRegNoAval4", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute(ldbName, "KolBD_txtNameAval4", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute(ldbName, "KolBD_txtCodeAval4", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute(ldbName, "KolBD_txtFNameAval4", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute(ldbName, "KolBD_txtSNameAval4", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		                                                                                                                                 
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//KolBD_txtRegNoAval4_FV
	
}
