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
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;

/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * COLL_BILL_EXCHANGE   AKCIJSKA LISTA
 */
public class CollBoe extends Handler {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollBoe.java,v 1.10 2006/08/01 08:18:29 hrasia Exp $"; 

	
	public CollBoe(ResourceAccessor ra) {
		super(ra);
	}
	  
	public void CollBoe_SE() {
		
		
		if (!ra.isLDBExists("CollBoeLDB")) {
	 		ra.createLDB("CollBoeLDB");
	 	}
		
		hr.vestigo.framework.common.TableData tdBE = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollBillExch");
        if (tdBE == null)
        	ra.createActionListSession("tblCollBillExch", false);
        tdBE = null;
        
       
		
	}//CollBoe_SE
	
	public void action(){
		if (isTableEmpty("tblCollBillExch")) {
			ra.showMessage("wrn299");
			return;
		}
			
		ra.showMessage("wrnclt0");
		
		
		//ra.showMessage("wrnclt0");
	}//action
	
	
	public void add(){
		
		ra.loadScreen("CollBoeDialog","scr_insert");
		//ra.showMessage("wrnclt0");
	}//add
	
	
	public void details(){
		if (isTableEmpty("tblCollBillExch")) {
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("CollBoeDialog","scr_detail"); 
		try {
			 ra.executeTransaction();
			 System.out.println("Izvrsio je ");			 
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 
		
		//ra.showMessage("wrnclt0");
	}//details
	
	
	
	
	
	public boolean CollBoe_txtIssueDate_FV(){
		java.sql.Date  issueDate = null;
		java.math.BigDecimal cusId = null; 
		java.math.BigDecimal collCusId = null;
		java.sql.Date  maturityDate = null;
		
		
		//TRASANT COLL_CUS_ID                                                                                                                                  
		collCusId = (java.math.BigDecimal) ra.getAttribute("CollBoeLDB", "CollBoe_COLL_CUS_ID");                                              
		issueDate = (java.sql.Date) ra.getAttribute("CollBoeLDB", "CollBoe_txtIssueDate");
		//KORISNIK PLASMANA    CUS_ID                                                                                                                                 
		cusId = (java.math.BigDecimal) ra.getAttribute("CollBoeLDB", "CollBoe_CUS_ID");                                              
		maturityDate = (java.sql.Date) ra.getAttribute("CollBoeLDB", "CollBoe_txtMaturityDate");
		
		if((maturityDate != null)  &&  (issueDate != null) ){
			if(issueDate.after(maturityDate)){
				//Datum izdavanja ne moze biti veci od datuma dospijeca
				ra.showMessage("wrnclt41");
				return false;
			}
		
		}
		
		if((cusId != null) || (collCusId != null)|| (issueDate != null)  || (maturityDate != null) ){                                                                                                         
			System.out.println("Poziv refresha akcijske liste - datum izdavanja");
			ra.refreshActionList("tblCollBillExch");	                                                                                       
		}     
		
		issueDate = null;
		cusId = null; 
		collCusId = null;
		maturityDate = null;
		
		return true;
	}//CollBoe_txtIssueDate_FV
	
	public boolean CollBoe_txtRegNoB_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		//KORISNIK PLASMANA    CUS_ID
		String ldbName = "CollBoeLDB";                                                                                                
		java.math.BigDecimal cusId = null; 
		
		
		
		java.sql.Date  issueDate = null;
		java.math.BigDecimal collCusId = null;
		java.sql.Date  maturityDate = null;
		
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("CollBoeLDB","CollBoe_txtRegNoB",null); 
			ra.setAttribute("CollBoeLDB","CollBoe_txtCodeB",null);
			ra.setAttribute("CollBoeLDB","CollBoe_txtNameB",null);                                                   
			ra.setAttribute("CollBoeLDB","CollBoe_CUS_ID",null);                                                                           
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoe_txtNameB")) {                                                      
			ra.setAttribute(ldbName, "CollBoe_txtRegNoB", "");
			ra.setAttribute(ldbName, "CollBoe_txtCodeB", "");                                                          
		} else if (ra.getCursorPosition().equals("CollBoe_txtRegNoB")) {                                              
			ra.setAttribute(ldbName, "CollBoe_txtNameB", "");  
			ra.setAttribute(ldbName, "CollBoe_txtCodeB", "");                                                        
			                                                                                                    
		} else if (ra.getCursorPosition().equals("CollBoe_txtCodeB")) {
			ra.setAttribute(ldbName, "CollBoe_txtNameB", "");  
			ra.setAttribute(ldbName, "CollBoe_txtRegNoB", "");                                                        
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "CollBoe_txtNameB") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "CollBoe_txtNameB");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "CollBoe_txtRegNoB") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "CollBoe_txtRegNoB");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "CollBoe_txtCodeB") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "CollBoe_txtCodeB");                                     
		}                                                                                                                                 
		if ((d_name.length() < 4) && (d_register_no.length() < 4) && (d_code.length() < 4)   ) {                                                                     
			ra.showMessage("wrn366");                                                                                                      
			return false;                                                                                                                  
		}                                                                                                                                
                                                                                                                                    
		                                                                                                                            
		//JE LI zvjezdica na pravom mjestu kod register_no                                                                               
		if (CharUtil.isAsteriskWrong(d_register_no)) {                                                                                   
			ra.showMessage("wrn367");                                                                                                      
			return false;                                                                                                                  
		}                                                                                                                                
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("CollBoe_txtRegNoB"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("CollBoe_txtCodeB"))  
		 	ra.setCursorPosition(2);   
		
					                                                                                                                           
		                                                                                                                                 
		if (ra.isLDBExists("CustomerAllLookUpLDB")) {                                                                                    
			ra.setAttribute("CustomerAllLookUpLDB", "cus_id", null);                                                                       
			ra.setAttribute("CustomerAllLookUpLDB", "register_no", "");                                                                    
			ra.setAttribute("CustomerAllLookUpLDB", "code", "");                                                                           
			ra.setAttribute("CustomerAllLookUpLDB", "name", "");                                                                           
			ra.setAttribute("CustomerAllLookUpLDB", "add_data_table", "");                                                                 
			ra.setAttribute("CustomerAllLookUpLDB", "cus_typ_id", null);                                                                   
			ra.setAttribute("CustomerAllLookUpLDB", "cus_sub_typ_id", null);                                                               
			ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", null);                                                                      
			ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);                                                             
		} else {                                                                                                                         
			ra.createLDB("CustomerAllLookUpLDB");                                                                                          
		}                                                                                                                                
                                                                                                                                     
		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("CollBoeLDB", "CollBoe_txtRegNoB"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("CollBoeLDB", "CollBoe_txtNameB")); 
		ra.setAttribute("CustomerAllLookUpLDB", "code", ra.getAttribute("CollBoeLDB", "CollBoe_txtCodeB")); 
		      
                                                                                                                                     
		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");                                                            
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");                                                            
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");                                                  
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");                                                                
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");                                                                
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");                                            
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");                                                    
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");                                            
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");                                                          
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");                                        
	                                                                                                                                   
		try {                                                                                                                            
			ra.callLookUp(lookUpRequest);                                                                                                  
		} catch (EmptyLookUp elu) {                                                                                                      
			ra.showMessage("err012");                                                                                                      
			return false;                                                                                                                  
		} catch (NothingSelected ns) {                                                                                                   
			return false;                                                                                                                  
		}                                                                                                                                
                                                                                                                                     
		ra.setAttribute("CollBoeLDB", "CollBoe_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));        
		ra.setAttribute("CollBoeLDB", "CollBoe_txtRegNoB", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("CollBoeLDB", "CollBoe_txtNameB", ra.getAttribute("CustomerAllLookUpLDB", "name"));        
		ra.setAttribute("CollBoeLDB", "CollBoe_txtCodeB", ra.getAttribute("CustomerAllLookUpLDB", "code"));        
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                              
		                                                                                                                                 
		//KORISNIK PLASMANA    CUS_ID                                                                                                                                 
		cusId = (java.math.BigDecimal) ra.getAttribute("CollBoeLDB", "CollBoe_CUS_ID");                                              
		
		
		issueDate = (java.sql.Date) ra.getAttribute("CollBoeLDB", "CollBoe_txtIssueDate");
		collCusId = (java.math.BigDecimal) ra.getAttribute("CollBoeLDB", "CollBoe_COLL_CUS_ID");
		
		maturityDate = (java.sql.Date) ra.getAttribute("CollBoeLDB", "CollBoe_txtMaturityDate");
		
		
		
		if((cusId != null) || (collCusId != null)|| (issueDate != null)|| (maturityDate != null)){                                                                                                         
			System.out.println("Poziv refresha akcijske liste - korisnik plasmana CUS_ID");
			ra.refreshActionList("tblCollBillExch");	                                                                                       
		}                                                                                                                                
		ldbName = null;                                                                                                                  
		cusId = null; 
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;  
		issueDate = null;
		collCusId = null;
		maturityDate = null;
		
		return true;                                                                                                                     
	}   //CollBoe_txtRegNoB_FV                                                                                                                               
	                                                                                                                                   
	
	
	
	public boolean CollBoe_txtRegNoT_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		//TRASANT MJENICE    CollBoe_COLL_CUS_ID  ( nesto kao vlasnik kolaterala )
		
		String ldbName = "CollBoeLDB";                                                                                                
		java.math.BigDecimal collCusId = null;  //TRASANT MJENICE 
		
		
		java.sql.Date  issueDate = null;
		java.math.BigDecimal cusId = null; 
		java.sql.Date  maturityDate = null;
		
		
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("CollBoeLDB","CollBoe_txtRegNoT",null); 
			ra.setAttribute("CollBoeLDB","CollBoe_txtCodeT",null);
			ra.setAttribute("CollBoeLDB","CollBoe_txtNameT",null);
			ra.setAttribute("CollBoeLDB","CollBoe_txtFName",null);
			ra.setAttribute("CollBoeLDB","CollBoe_txtSName",null);
			ra.setAttribute("CollBoeLDB","CollBoe_COLL_CUS_ID",null);   
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("CollBoe_txtNameT")) {                                                      
			ra.setAttribute(ldbName, "CollBoe_txtRegNoT", "");
			ra.setAttribute(ldbName, "CollBoe_txtCodeT", "");                                                          
		} else if (ra.getCursorPosition().equals("CollBoe_txtRegNoT")) {                                              
			ra.setAttribute(ldbName, "CollBoe_txtNameT", "");  
			ra.setAttribute(ldbName, "CollBoe_txtCodeT", "");                                                        
			                                                                                                    
		} else if (ra.getCursorPosition().equals("CollBoe_txtCodeT")) {
			ra.setAttribute(ldbName, "CollBoe_txtNameT", "");  
			ra.setAttribute(ldbName, "CollBoe_txtRegNoT", "");                                                        
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "CollBoe_txtNameT") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "CollBoe_txtNameT");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "CollBoe_txtRegNoT") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "CollBoe_txtRegNoT");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "CollBoe_txtCodeT") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "CollBoe_txtCodeT");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "CollBoe_txtFName") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "CollBoe_txtFName");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "CollBoe_txtSName") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "CollBoe_txtSName");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("CollBoe_txtRegNoT"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("CollBoe_txtCodeT"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("CollBoe_txtNameT"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("CollBoe_txtFName"))                                                      
			ra.setCursorPosition(2);   
		 
		 
		if (ra.isLDBExists("CustomerAllCitizenLookUpLDB")) {                                                                                    
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "cus_id", null);                                                                       
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "register_no", "");                                                                    
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "code", "");                                                                           
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "name", "");                                                                           
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "add_data_table", "");                                                                 
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "cus_typ_id", null);                                                                   
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "cus_sub_typ_id", null);                                                               
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "eco_sec", null);                                                                      
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "residency_cou_id", null);  
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "fname", null);
			ra.setAttribute("CustomerAllCitizenLookUpLDB", "surname", null);
		} else {                                                                                                                         
			ra.createLDB("CustomerAllCitizenLookUpLDB");                                                                                          
		}                                                                                                                                
                                                                                                                                     
		ra.setAttribute("CustomerAllCitizenLookUpLDB", "register_no", ra.getAttribute("CollBoeLDB", "CollBoe_txtRegNoT"));
		ra.setAttribute("CustomerAllCitizenLookUpLDB", "name", ra.getAttribute("CollBoeLDB", "CollBoe_txtNameT")); 
		ra.setAttribute("CustomerAllCitizenLookUpLDB", "code", ra.getAttribute("CollBoeLDB", "CollBoe_txtCodeT")); 
		ra.setAttribute("CustomerAllCitizenLookUpLDB", "fname", ra.getAttribute("CollBoeLDB", "CollBoe_txtFName")); 
		ra.setAttribute("CustomerAllCitizenLookUpLDB", "surname", ra.getAttribute("CollBoeLDB", "CollBoe_txtSName")); 
		      
                                                                                                                                     
		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllCitizenLookUp");                                                            
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "cus_id", "cus_id");                                                            
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "register_no", "register_no");                                                  
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "code", "code");                                                                
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "name", "name");                                                                
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "add_data_table", "add_data_table");                                            
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "cus_typ_id", "cus_typ_id");                                                    
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");                                            
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "eco_sec", "eco_sec");                                                          
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "residency_cou_id", "residency_cou_id"); 
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "fname", "fname");
		lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB", "surname", "surname");
		                                       
	                                                                                                                                   
		try {                                                                                                                            
			ra.callLookUp(lookUpRequest);                                                                                                  
		} catch (EmptyLookUp elu) {                                                                                                      
			ra.showMessage("err012");                                                                                                      
			return false;                                                                                                                  
		} catch (NothingSelected ns) {                                                                                                   
			return false;                                                                                                                  
		}                                                                                                                                
                                                                                                                                     
		ra.setAttribute("CollBoeLDB", "CollBoe_COLL_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllCitizenLookUpLDB", "cus_id"));        
		ra.setAttribute("CollBoeLDB", "CollBoe_txtRegNoT", ra.getAttribute("CustomerAllCitizenLookUpLDB", "register_no"));
		ra.setAttribute("CollBoeLDB", "CollBoe_txtNameT", ra.getAttribute("CustomerAllCitizenLookUpLDB", "name"));        
		ra.setAttribute("CollBoeLDB", "CollBoe_txtCodeT", ra.getAttribute("CustomerAllCitizenLookUpLDB", "code"));        
		ra.setAttribute("CollBoeLDB", "CollBoe_txtFName", ra.getAttribute("CustomerAllCitizenLookUpLDB", "fname"));        
		ra.setAttribute("CollBoeLDB", "CollBoe_txtSName", ra.getAttribute("CustomerAllCitizenLookUpLDB", "surname"));        
		                                                                                                                                 
		//                                                                                                                               
		                                                                                                                                 
		//TRASANT COLL_CUS_ID                                                                                                                                  
		collCusId = (java.math.BigDecimal) ra.getAttribute("CollBoeLDB", "CollBoe_COLL_CUS_ID");                                              
		
		
		
		issueDate = (java.sql.Date) ra.getAttribute("CollBoeLDB", "CollBoe_txtIssueDate");
		//KORISNIK PLASMANA    CUS_ID                                                                                                                                 
		cusId = (java.math.BigDecimal) ra.getAttribute("CollBoeLDB", "CollBoe_CUS_ID");                                              
		
		
		
		maturityDate = (java.sql.Date) ra.getAttribute("CollBoeLDB", "CollBoe_txtMaturityDate");
		
		
		
		if((cusId != null) || (collCusId != null)|| (issueDate != null)|| (maturityDate != null)){                                                                                                              
			System.out.println("Poziv refresha akcijske liste - TRASANT COLL_CUS_ID ");
			ra.refreshActionList("tblCollBillExch");	                                                                                       
		}                                                                                                                                
		ldbName = null;                                                                                                                  
		collCusId = null; 
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null; 
		issueDate = null;
		cusId = null; 
		
		maturityDate = null;
		
		return true;                                                                                                                     
	}//CollBoe_txtRegNoT_FV    
	
	
	
	

	public boolean CollBoe_txtMaturityDate_FV(){
		java.sql.Date  issueDate = null;
		java.math.BigDecimal cusId = null; 
		java.math.BigDecimal collCusId = null;
		java.sql.Date  maturityDate = null;
		
		//TRASANT COLL_CUS_ID                                                                                                                                  
		collCusId = (java.math.BigDecimal) ra.getAttribute("CollBoeLDB", "CollBoe_COLL_CUS_ID");                                              
		issueDate = (java.sql.Date) ra.getAttribute("CollBoeLDB", "CollBoe_txtIssueDate");
		//KORISNIK PLASMANA    CUS_ID                                                                                                                                 
		cusId = (java.math.BigDecimal) ra.getAttribute("CollBoeLDB", "CollBoe_CUS_ID");                                              
		
		maturityDate = (java.sql.Date) ra.getAttribute("CollBoeLDB", "CollBoe_txtMaturityDate");
		
		
		if((maturityDate != null)  &&  (issueDate != null) ){
			if(issueDate.after(maturityDate)){
				//Datum izdavanja ne moze biti veci od datuma dospijeca
				ra.showMessage("wrnclt41");
				return false;
			}
		
		}
		
		
		if((cusId != null) || (collCusId != null)|| (issueDate != null)|| (maturityDate != null)){                                                                                                             
			System.out.println("Poziv refresha akcijske liste - datum dospijeca");
			ra.refreshActionList("tblCollBillExch");	                                                                                       
		}     
		
		
		
		issueDate = null;
		cusId = null; 
		collCusId = null;
		maturityDate = null;
		
		return true;
	}//CollBoe_txtMaturityDate_FV
	
	
	public void loan_ben(){
		//F6
		
		if (isTableEmpty("tblCollBillExch")) {
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("LoanBeneficiary","scr_bilofexch");
		//ra.showMessage("wrnclt0");
	}
	
	
	
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
	
}
