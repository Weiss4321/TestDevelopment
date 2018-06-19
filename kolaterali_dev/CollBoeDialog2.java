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
public class CollBoeDialog2 extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollBoeDialog2.java,v 1.9 2011/09/23 10:18:43 hramkr Exp $"; 

	
	public CollBoeDialog2(ResourceAccessor ra) {
		super(ra);
	}
	
	public void CollBoeDialog2_SE() {
	
	
	}//CollBoeDialog2_SE
	
	

	
	public boolean CollBoeDialog_txtRegNoT_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		//TRASANT MJENICE    CollBoeDialog_COLL_CUS_ID  ( nesto kao vlasnik kolaterala )
		
		
		//		TRASANT                        
		//		CollBoeDialog_txtRegNoT        
		//		CollBoeDialog_txtCodeT         
		//		CollBoeDialog_txtNameT         
		//		CollBoeDialog_txtFName         
		//		CollBoeDialog_txtSName     
		
		
		//CollBoeDialog_txtAddressT              
		//CollBoeDialog_txtPlaceAddressT         
		//                  
		//CollBoeDialog_txtPostNrT               
		//CollBoeDialog_txtPostNameT             
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		java.math.BigDecimal collCusId = null;  //TRASANT MJENICE 
		
		
		java.sql.Date  issueDate = null;
		java.math.BigDecimal cusId = null; 
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtRegNoT",""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCodeT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtNameT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFName","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSName","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_COLL_CUS_ID",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameT","");   
			
			
			
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoeDialog_txtNameT")) {                                                      
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoT", "");
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeT", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFName","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSName","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_COLL_CUS_ID",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameT","");   
			
			
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoT")) {                                              
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameT", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeT", ""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFName","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSName","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_COLL_CUS_ID",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameT","");   
			                                                                                                    
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeT")) {
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameT", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoT", ""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFName","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSName","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_COLL_CUS_ID",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrT","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameT","");   
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtNameT") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtNameT");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoT") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoT");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtCodeT") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtCodeT");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtFName") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtFName");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtSName") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtSName");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoT"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeT"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtNameT"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtFName"))                                                      
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
                                                                                                                                     
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoT"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameT")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeT")); 
		      
                                                                                                                                     
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
                                                                                                                                     
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_COLL_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoT", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameT", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeT", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtFName", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtSName", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		
		
		
		
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtAddressT", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPlaceAddressT", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNrT", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNameT", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		
		//                                                                                                                               
		                                                                                                                                 
		//TRASANT COLL_CUS_ID                                                                                                                                  
		collCusId = (java.math.BigDecimal) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COLL_CUS_ID");                                              
		
		
		
		issueDate = (java.sql.Date) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtIssueDate");
		//KORISNIK PLASMANA    CUS_ID                                                                                                                                 
		cusId = (java.math.BigDecimal) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_CUS_ID");                                              
		
		
		                                                                                                                              
		ldbName = null;                                                                                                                  
		collCusId = null; 
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//CollBoeDialog_txtRegNoT_FV                                                                                                                               


	
	
	public boolean CollBoeDialog_txtRegNoTR_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		//TRASAT  
		//CollBoeDialog_txtRegNoTR
		//CollBoeDialog_txtCodeTR
		//CollBoeDialog_txtNameTR
		//CollBoeDialog_txtFNameTR
		//CollBoeDialog_txtSNameTR
		//CollBoeDialog_TR_CUS_ID
		
		
		//CollBoeDialog_txtAddressTR              
		//CollBoeDialog_txtPlaceAddressTR         
		//                  
		//CollBoeDialog_txtPostNrTR               
		//CollBoeDialog_txtPostNameTR        
		
		String ldbName = "CollBoeDialogLDB";                                                                                                
		java.math.BigDecimal cIdTR = null;  
		
		
		
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtRegNoTR",""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCodeTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_TR_CUS_ID",null);  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameTR","");   
			
			
			
			
			
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoeDialog_txtNameTR")) {                                                      
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoTR", "");
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeTR", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_TR_CUS_ID",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameTR","");   
			
			
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoTR")) {                                              
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameTR", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtCodeTR", "");  
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_TR_CUS_ID",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameTR","");  
			
			
			                                                                                                    
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeTR")) {
			ra.setAttribute(ldbName, "CollBoeDialog_txtNameTR", "");  
			ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoTR", ""); 
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtFNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtSNameTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_TR_CUS_ID",null);   
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPlaceAddressTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNrTR","");
			ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtPostNameTR","");  
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtNameTR") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtNameTR");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoTR") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoTR");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtCodeTR") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtCodeTR");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtFNameTR") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtFNameTR");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "CollBoeDialog_txtSNameTR") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtSNameTR");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoTR"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeTR"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtNameTR"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("CollBoeDialog_txtFNameTR"))                                                      
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
		
		ra.setAttribute("CustomerAndAddressLookUpLDB", "register_no", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoTR"));
		ra.setAttribute("CustomerAndAddressLookUpLDB", "name", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameTR")); 
		ra.setAttribute("CustomerAndAddressLookUpLDB", "code", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeTR")); 
		      
                                                                                                                                     
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
		
		
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtAddressTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "address"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPlaceAddressTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "city_name"));        
		
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNrTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "postoffice"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPostNameTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "pos_off_name"));        
		
                                                                                                                                     
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_TR_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAndAddressLookUpLDB", "cus_id"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "register_no"));
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "name"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "code"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtFNameTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "fname"));        
		ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtSNameTR", ra.getAttribute("CustomerAndAddressLookUpLDB", "surname"));        
		                                                                                                                                 
	
		
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;                                                                                                              
		return true;                                                                                                                     
	}//CollBoeDialog_txtRegNoTR_FV                                                                                                                               


	
	public boolean CollBoeDialog_txtPayPlace_FV(String elementName, Object elementValue, Integer LookUp){
		
		//Mjesto placanja
		//CollBoeDialog_PAY_PLACE_ID
		//CollBoeDialog_txtPayPlaceCode
		//CollBoeDialog_txtPayPlace
	
	
		java.math.BigDecimal placeTypeId = new java.math.BigDecimal("5999.0");

	
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPayPlaceCode", "");
			ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPayPlace", "");
			ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_PAY_PLACE_ID", null);
			return true;
		}
 
  
  
		if (ra.getCursorPosition().equals("CollBoeDialog_txtPayPlace")) {
			ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPayPlaceCode", "");
		} else if (ra.getCursorPosition().equals("CollBoeDialog_txtPayPlaceCode")) {
			ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtPayPlace", "");
		}	

		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
			ra.createLDB("PoliticalMapByTypIdLookUpLDB");
		}

	
		ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", placeTypeId);

		LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
 
		lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_PAY_PLACE_ID", "pol_map_id");
		lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtPayPlaceCode", "code");
		lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtPayPlace", "name"); 

		try {
					ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
				ra.showMessage("err012");
				return false;
		} catch (NothingSelected ns) {
			return false;
		}
		
		if(ra.getCursorPosition().equals("CollBoeDialog_txtPayPlaceCode")){                                                             
			ra.setCursorPosition(2);                                                                                                           
		} 
		if(ra.getCursorPosition().equals("CollBoeDialog_txtPayPlace")){                                                             
			ra.setCursorPosition(1);                                                                                                           
		} 
		
		return true;

   
	}//CollBoeDialog_txtPayPlace_FV
	

	

	public boolean Kol_txtAval_FV(String ElName, Object ElValue, Integer LookUp) {
// da li je mjenica avalirana, ako je treba upisati avaliste
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute("CollBoeDialogLDB", "Kol_txtAval", null);
			return true;
		}
	  
			LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");		

			request.addMapping("CollBoeDialogLDB", "Kol_txtAval", "Vrijednosti");

			try {
				ra.callLookUp(request);
			} catch (EmptyLookUp elu) {
				ra.showMessage("err012");
				return false;
			} catch (NothingSelected ns) {
				return false;

			}
			return true; 
		 
	} // Kol_txtAval_FV		
	
	  
}
