/*
 * Created on 2006.05.11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.util.Vector;
      
import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.modules.collateral.util.CollateralUtil;
     
/** 
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoanBeneficiaryDialog extends Handler {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/LoanBeneficiaryDialog.java,v 1.61 2016/07/06 11:24:04 hrakis Exp $";
	
	CollateralUtil coll_util = null;	
	
	public LoanBeneficiaryDialog(ResourceAccessor ra) {
		super(ra);
		coll_util = new CollateralUtil(ra);
	}
	
	public void LoanBeneficiaryDialog_SE(){
		
		if (!(ra.isLDBExists("LBenDialogLDB"))) {
			ra.createLDB("LBenDialogLDB");
		}	
		  
        if (!(ra.isLDBExists("RealEstateDialogLDB"))) {
            ra.createLDB("RealEstateDialogLDB");
        }   
		
		if(ra.getScreenContext().compareTo("scr_insert")== 0){
			postavi_nekeUserDate();
			ra.setAttribute("LBenDialogLDB", "LBenDialog_COLL_HF_PRIOR_ID", (java.math.BigDecimal)ra.getAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID"));	
			ra.setAttribute("LBenDialogLDB", "LBenDialog_COLL_HEA_ID", (java.math.BigDecimal)ra.getAttribute("LBenLDB","LBen_COLL_HEA_ID"));	
			ra.setAttribute("LBenDialogLDB", "LBenDialog_HOW_COWER", (java.math.BigDecimal)ra.getAttribute("LBenLDB","LBen_HOW_COWER"));

// Milka, 05.03.2007-veza na okvirni sporazum					
			ra.setAttribute("LBenDialogLDB", "LBenDialog_fra_agr_id", (java.math.BigDecimal)ra.getAttribute("LBenLDB","LBen_fra_agr_id"));
		  
		}  
		
		if((ra.getScreenContext().compareTo("scr_detail")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0) ||
				(ra.getScreenContext().compareTo("scr_kred_admin")== 0)	){
			TableData td = (TableData) ra.getAttribute("LBenLDB", "tblLoanBeneficiary");
			java.util.Vector hiddenVector = (java.util.Vector) td.getSelectedRowUnique();
			
			Vector row = (Vector) td.getSelectedRowData();

			
			BigDecimal loanBenId = (BigDecimal) hiddenVector.elementAt(0);
// za plasmane iz okvirnog sporazuma			
			BigDecimal laAccId = (BigDecimal) hiddenVector.elementAt(1);
			String laAccNo = (String) row.elementAt(1); 
			String laRequestNo = (String) row.elementAt(0);
			String laContractNo = (String) row.elementAt(4);

			ra.setAttribute("LBenDialogLDB", "LBenDialog_LOAN_BEN_ID", loanBenId);
			ra.setAttribute("LBenDialogLDB", "laAccId", laAccId);
			ra.setAttribute("LBenDialogLDB", "l_fra_agr_id", ra.getAttribute("LBenLDB","LBen_fra_agr_id"));
			 
			ra.setAttribute("LBenDialogLDB", "laAccNo", laAccNo);
			ra.setAttribute("LBenDialogLDB", "laRequestNo", laRequestNo);
			ra.setAttribute("LBenDialogLDB", "laContractNo", laContractNo);
			
			try {
				 ra.executeTransaction();
				 			 
			} catch (VestigoTMException vtme) {
				 if (vtme.getMessageID() != null)
					 ra.showMessage(vtme.getMessageID());
			}
			
			postavi_nekeUserDate();
		}
		  
		if((ra.getScreenContext().compareTo("scr_insert")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0)){
// ako su vozila obavezan je podatak o osiguranju		
			if (ra.isLDBExists("ColWorkListLDB")) {
				String code = (String) ra.getAttribute("ColWorkListLDB", "code");
				if (code != null && code.equalsIgnoreCase("VOZI")) {
					ra.setRequired("LBenDialog_Insu", true);
				} else {
					ra.setRequired("LBenDialog_Insu", false);
				}
			}
		}
		
	}//LoanBeneficiaryDialog_SE
	
	  
	public void confirm(){
		boolean imaNepopunjenih = false;
		
		if (!(ra.isRequiredFilled())) {
 			return;
 		}
		
		if(imaNepopunjenih){
 			ra.showMessage("infclt1"); 
 			return;	
 		}
		
		if(checkAccNoRequestNo()){
			if(ra.getScreenContext().compareTo("scr_insert")== 0){
				try {
 						ra.executeTransaction();
 						ra.showMessage("infclt2");
				} catch (VestigoTMException vtme) {
 						error("LoanBeneficiaryDialog -> insert(): VestigoTMException", vtme);
 						if (vtme.getMessageID() != null)
 								ra.showMessage(vtme.getMessageID());
				}
				ra.exitScreen();
				ra.refreshActionList("tblLoanBeneficiary");
			}//scr_insert
		}
		
		//ra.showMessage("wrnclt0");
		//nije implementirano
	}

	public void change(){
		boolean imaNepopunjenih = false;
		
		if (!(ra.isRequiredFilled())) {
 			return;
 		}
		
		if(imaNepopunjenih){
 			ra.showMessage("infclt1"); 
 			return;	
 		}
		
		if(checkAccNoRequestNo()){
			if(ra.getScreenContext().compareTo("scr_update")== 0){
				try {
						ra.executeTransaction();
						ra.showMessage("infclt2");
				} catch (VestigoTMException vtme) {
						error("LoanBeneficiaryDialog -> update(): VestigoTMException", vtme);
						if (vtme.getMessageID() != null)
								ra.showMessage(vtme.getMessageID());
				}
				ra.exitScreen();
				ra.refreshActionList("tblLoanBeneficiary");
			}
		}
	}
	
	
	public void delete(){
		
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			
			Integer retValue = (Integer) ra.showMessage("qer692");
	   		if (retValue!=null) {
	   			if (retValue.intValue() == 0){ 
	   				return;
	   			}else{
			
	   				try {
	   					ra.executeTransaction();
	   					ra.showMessage("infcltzst1");
	   				} catch (VestigoTMException vtme) {
	   					error("LoanBeneficiaryDialog -> delete(): VestigoTMException", vtme);
	   					if (vtme.getMessageID() != null)
							ra.showMessage(vtme.getMessageID());
	   				}
	  			}   
	   		}		   				
			ra.exitScreen();
			ra.refreshActionList("tblLoanBeneficiary");
			
			 
		}
		
		//ra.showMessage("wrnclt0");
		
		
		
		
	}
	
	
	
	
	
	
	
	public boolean LBenDialog_txtPriorityNo_FV(){
		
		java.math.BigDecimal howCover = null;
		//select * from sidev.system_code_value  where sys_cod_id like 'clt_hf_cover%'
		//		SYS_COD_VAL_ID		SYS_COD_ID		SYS_CODE_VALUE	SYS_CODE_DESC											ENG_SYS_CODE_DESC								DEFAULT_FLAG	USER_LOCK
		//		1629484003			clt_hf_cover      	A			Hipoteka pokriva alikvotno( razmjerno) vise plasmana	Mortgage covers many placements proportionally	0				2006-07-07 11:13:51.867773
		//		1629485003			clt_hf_cover      	P			Hipoteka pokriva vise plasmana po prioritetu			Mortgage covers many placements by priority		0				2006-07-07 11:14:28.553553
		//		1629486003			clt_hf_cover      	O			Hipoteka pokriva samo jedan plasman						Mortgage covers only one  placement				0				2006-07-07 11:15:25.05559

		java.math.BigDecimal PCover = new java.math.BigDecimal("1629485003.0");
		java.math.BigDecimal collHfPriorId = null;
		String priorityNo = null;
		
		collHfPriorId = (java.math.BigDecimal)ra.getAttribute("LBenDialogLDB", "LBenDialog_COLL_HF_PRIOR_ID");
		howCover = (java.math.BigDecimal)ra.getAttribute("LBenDialogLDB", "LBenDialog_HOW_COWER");
		priorityNo = (String)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtPriorityNo");
		if(collHfPriorId != null){
			//called from mortgage
			if(howCover.compareTo(PCover)== 0){
				if(priorityNo == null){
					return false;
				}
			}else{
				ra.setAttribute("LBenDialogLDB", "LBenDialog_txtPriorityNo","");
				return true;
			}
			
			
		}else{
			//called from bill of exchange or loan stock	
			ra.setAttribute("LBenDialogLDB", "LBenDialog_txtPriorityNo","");
			return true;
		}
				
		
	
		
		return true;
	}
	
	
	public boolean LBenDialog_txtRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
		
		
		String ldbName = "LBenDialogLDB";  //LDB definiran u goVADF-u                                                                                              
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtRegNo",null); 
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtCode",null);
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtName",null);
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtFName",null);
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtSurName",null);
			ra.setAttribute("LBenDialogLDB","LBenDialog_CUS_ID",null);   
	         ra.setAttribute("LBenDialogLDB","LBenDialog_txtOIB",null);   

// ako obrise komitenta obrisati i partiju plasmana 			
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtAccNo",null); 
			ra.setAttribute("LBenDialogLDB","LBenDialog_LA_ACC_ID",null); 			
			return true;                                                                                                                   
		}                                                                                                                                
		                                                                                                                                 
		if (ra.getCursorPosition().equals("LBenDialog_txtName")) {                                                      
			ra.setAttribute(ldbName, "LBenDialog_txtRegNo", "");
			ra.setAttribute(ldbName, "LBenDialog_txtCode", "");                                                          
		} else if (ra.getCursorPosition().equals("LBenDialog_txtRegNo")) {                                              
			ra.setAttribute(ldbName, "LBenDialog_txtName", "");  
			ra.setAttribute(ldbName, "LBenDialog_txtCode", "");                                                        
			                                                                                                    
		} else if (ra.getCursorPosition().equals("LBenDialog_txtCode")) {
			ra.setAttribute(ldbName, "LBenDialog_txtName", "");  
			ra.setAttribute(ldbName, "LBenDialog_txtRegNo", "");                                                        
		}                                                                                                                               
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		                                                                                                                                 
		String d_name = "";                                                                                                              
		if (ra.getAttribute(ldbName, "LBenDialog_txtName") != null){                                                    
			d_name = (String) ra.getAttribute(ldbName, "LBenDialog_txtName");                                             
		}                                                                                                                                
		                                                                                                                                 
		String d_register_no = "";                                                                                                       
		if (ra.getAttribute(ldbName, "LBenDialog_txtRegNo") != null){                                                   
			d_register_no = (String) ra.getAttribute(ldbName, "LBenDialog_txtRegNo");                                     
		}                                                                                                                                
		   
		String d_code = "";   
		if (ra.getAttribute(ldbName, "LBenDialog_txtCode") != null){                                                   
			d_code = (String) ra.getAttribute(ldbName, "LBenDialog_txtCode");                                     
		} 
		
		String d_fname = "";   
		if (ra.getAttribute(ldbName, "LBenDialog_txtFName") != null){                                                   
			d_fname = (String) ra.getAttribute(ldbName, "LBenDialog_txtFName");                                     
		} 
		
		String d_sname = "";   
		if (ra.getAttribute(ldbName, "LBenDialog_txtSurName") != null){                                                   
			d_sname = (String) ra.getAttribute(ldbName, "LBenDialog_txtSurName");                                     
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
		                                                                                                                                 
		                                                                                                                                 
		 if (ra.getCursorPosition().equals("LBenDialog_txtRegNo"))                                                      
			ra.setCursorPosition(5);                                                                                                       
		 if (ra.getCursorPosition().equals("LBenDialog_txtCode"))                                                      
			ra.setCursorPosition(4);   			                                                                                                                           
		 if (ra.getCursorPosition().equals("LBenDialog_txtName"))                                                      
			ra.setCursorPosition(3);   
		 if (ra.getCursorPosition().equals("LBenDialog_txtFName"))                                                      
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
                                                                                                                                       
		ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute("LBenDialogLDB", "LBenDialog_txtRegNo"));
		ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute("LBenDialogLDB", "LBenDialog_txtName")); 
		ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", ra.getAttribute("LBenDialogLDB", "LBenDialog_txtCode")); 
		ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", ra.getAttribute("LBenDialogLDB", "LBenDialog_txtFName")); 
		ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", ra.getAttribute("LBenDialogLDB", "LBenDialog_txtSurName")); 
		      
                                                                                                                                     
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
  
// provjeriti da li je komitent aktivan
		
		String status = (String) ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "status");
        if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("2")) {
        } else {	
// komitent nije aktivan        	
        	ra.showMessage("wrnclt145");
        	return false;
        	
        } 
		
// ako se promijenio kolitent korisnik plasmana treba obristi i partiju plasmana	
		
		BigDecimal cus_old = (BigDecimal) ra.getAttribute("LBenDialogLDB", "LBenDialog_CUS_ID");
		BigDecimal cus_new = (BigDecimal) ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id");
		
		if (cus_old != null && cus_new != null) {
				
			if (cus_old.compareTo(cus_new) == 0) {
				
			} else {
				ra.setAttribute("LBenDialogLDB","LBenDialog_txtAccNo",null); 
				ra.setAttribute("LBenDialogLDB","LBenDialog_LA_ACC_ID",null); 							
			}
			
		}
		
		
		ra.setAttribute("LBenDialogLDB", "LBenDialog_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));        
		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtRegNo", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));
		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));        
		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtCode", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "code"));        
		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtFName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "fname"));        
		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtSurName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "surname"));  
	    ra.setAttribute("LBenDialogLDB", "LBenDialog_txtOIB", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "oib_lk"));  
		                                                                                                                              
		ldbName = null;                                                                                                                  
		
		d_register_no = null;                                                                                                                            
		d_code = null;                                                                                                                            
		d_name  = null;
		ra.setCursorPosition("LBenDialog_txtApsRqstNo");
	
		return true;                                                                                                                     
	}//LBenDialog_txtRegNo_FV                                                                                                                               

	
	
	
	public void postavi_nekeUserDate(){
		java.sql.Date todaySQLDate = null;
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		long timeT = calendar.getTime().getTime();
		todaySQLDate = new java.sql.Date(timeT);
		
	 	if(ra.getScreenContext().compareTo("scr_insert")== 0){
	 		ra.setAttribute("LBenDialogLDB", "LBenDialog_USE_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
	 		ra.setAttribute("LBenDialogLDB", "LBenDialog_USE_OPEN_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
	 		ra.setAttribute("LBenDialogLDB","LBenDialog_txtUserLogin",(String) ra.getAttribute("GDB", "Use_Login"));
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtUserName",(String) ra.getAttribute("GDB", "Use_UserName"));
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtUserOpenLogin",(String) ra.getAttribute("GDB", "Use_Login"));
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtUserOpenName",(String) ra.getAttribute("GDB", "Use_UserName"));
			java.sql.Date datumOd = null;
		 	datumOd = (java.sql.Date) ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateFrom");
		 	if(datumOd == null){
		 		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtDateFrom", todaySQLDate); 
		 	}
		 	datumOd = null;
		 	calendar = null;
		 	todaySQLDate = null;
		 	
		 	java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
		 	java.sql.Date datumDo = null;
		 	datumDo = (java.sql.Date) ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateUntil");
		 	
		 	if(datumDo == null){
		 		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtDateUntil", vvDatUntil); 
		 	}
		 	datumDo = null;
		 	vvDatUntil = null;
		 	
// 17.12.2007 - preuzeti vlasnika za zaduznice i mjenice i zaprotektirati 
// 04.03.2008 - promjena, ne treba zaprotektirati preuzetog vlasnika		 	
	
			if (ra.isLDBExists("ColWorkListLDB")) {

				String code = (String) ra.getAttribute("ColWorkListLDB", "code");
				if (code.equalsIgnoreCase("MJEN")) {
					ra.setAttribute("LBenDialogLDB", "LBenDialog_txtRegNo", ra.getAttribute("CollBoeDialogLDB", "Coll_txtCarrierRegisterNo"));
//					ra.setContext("LBenDialog_txtRegNo", "fld_change_protected");
//					ra.setContext("LBenDialog_txtName", "fld_change_protected");
//					ra.setContext("LBenDialog_txtCode", "fld_change_protected");
				
					ra.setCursorPosition("LBenDialog_txtRegNo");
					ra.invokeValidation("LBenDialog_txtRegNo");
					ra.setCursorPosition("LBenDialog_txtApsRqstNo");
				} else if (code.equalsIgnoreCase("ZADU")) {
					ra.setAttribute("LBenDialogLDB", "LBenDialog_txtRegNo", ra.getAttribute("CollHeadLDB", "Coll_txtCarrierRegisterNo"));	
//					ra.setContext("LBenDialog_txtRegNo", "fld_change_protected");
//					ra.setContext("LBenDialog_txtName", "fld_change_protected");
//					ra.setContext("LBenDialog_txtCode", "fld_change_protected");
					ra.setCursorPosition("LBenDialog_txtRegNo");
					ra.invokeValidation("LBenDialog_txtRegNo");
					ra.setCursorPosition("LBenDialog_txtApsRqstNo");
				}
		 	
			} 
	 	}//scr_insert
	   
	 	if(ra.getScreenContext().compareTo("scr_update")== 0){
	 		ra.setAttribute("LBenDialogLDB", "LBenDialog_USE_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
	 		ra.setAttribute("LBenDialogLDB","LBenDialog_txtUserLogin",(String) ra.getAttribute("GDB", "Use_Login"));
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtUserName",(String) ra.getAttribute("GDB", "Use_UserName"));
			
			java.sql.Date datumOd = null;
		 	datumOd = (java.sql.Date) ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateFrom");
		 	if(datumOd == null){
		 		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtDateFrom", todaySQLDate); 
		 	}
		 	datumOd = null;
		 	calendar = null;
		 	todaySQLDate = null;
		 	
		 	java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
		 	java.sql.Date datumDo = null;
		 	datumDo = (java.sql.Date) ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateUntil");
		 	
		 	if(datumDo == null){
		 		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtDateUntil", vvDatUntil); 
		 	}
		 	datumDo = null;
		 	vvDatUntil = null;
		 	  
//		  17.12.2007 - zaprotektirati vlasnika za zaduznice i mjenice  
//		  04.03.2008 - promjena, ne treba zaprotektirati preuzetog vlasnika	
		 	
			if (ra.isLDBExists("ColWorkListLDB")) {
				String code = (String) ra.getAttribute("ColWorkListLDB", "code");
				if (code.equalsIgnoreCase("MJEN")) {
					ra.setAttribute("LBenDialogLDB", "LBenDialog_txtRegNo", ra.getAttribute("CollBoeDialogLDB", "Coll_txtCarrierRegisterNo"));
//					ra.setContext("LBenDialog_txtRegNo", "fld_change_protected");
//					ra.setContext("LBenDialog_txtName", "fld_change_protected");
//					ra.setContext("LBenDialog_txtCode", "fld_change_protected");
				
					ra.setCursorPosition("LBenDialog_txtRegNo");
					ra.invokeValidation("LBenDialog_txtRegNo");
					ra.setCursorPosition("LBenDialog_txtApsRqstNo");
				} else if (code.equalsIgnoreCase("ZADU")) {
					ra.setAttribute("LBenDialogLDB", "LBenDialog_txtRegNo", ra.getAttribute("CollHeadLDB", "Coll_txtCarrierRegisterNo"));	
//					ra.setContext("LBenDialog_txtRegNo", "fld_change_protected");
//					ra.setContext("LBenDialog_txtName", "fld_change_protected");
//					ra.setContext("LBenDialog_txtCode", "fld_change_protected");
					ra.setCursorPosition("LBenDialog_txtRegNo");
					ra.invokeValidation("LBenDialog_txtRegNo");		
					ra.setCursorPosition("LBenDialog_txtApsRqstNo");
				}		 	
			}
		 	
// ako je plasman osiguran osloboditi za upis osiguravatelja
			String ins_flag = (String) ra.getAttribute("LBenDialogLDB","LBenDialog_Insu");
			if (ins_flag != null && ins_flag.equalsIgnoreCase("D")) {
				ra.setContext("LBenDialog_txtInsPolRegNo", "fld_plain");
				ra.setRequired("LBenDialog_txtInsPolRegNo", true);
			} else {  
				ra.setContext("LBenDialog_txtInsPolRegNo", "fld_change_protected");	
				ra.setRequired("LBenDialog_txtInsPolRegNo", false);
			}
			
		}//scr_update
	
	}//postavi_nekeUserDate
	
	
	 
	
	
	public boolean LBenDialog_txtDateFrom_FV(){
		java.sql.Date pocetak = null;
		java.sql.Date kraj = null;
		if((ra.getScreenContext().compareTo("scr_insert")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0) ){ 
			pocetak = (java.sql.Date)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateFrom");
			kraj = (java.sql.Date)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateUntil");
			
			
			if((pocetak != null) && (kraj != null)){
				if(kraj.before(pocetak)){
					//Datum DO ne može biti manji od datuma OD
					ra.showMessage("wrnclt20");
					return false;
				}
			}
		}
		pocetak = null;
		kraj = null;  
		return true;
	}//LBenDialog_txtDateFrom_FV
	
	public boolean LBenDialog_txtDateUntil_FV(){
		java.sql.Date pocetak = null;
		java.sql.Date kraj = null;
		if((ra.getScreenContext().compareTo("scr_insert")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0) ){ 
			pocetak = (java.sql.Date)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateFrom");
			kraj = (java.sql.Date)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateUntil");
			
			
			if((pocetak != null) && (kraj != null)){
				if(kraj.before(pocetak)){
					//Datum DO ne može biti manji od datuma OD
					ra.showMessage("wrnclt20");
					return false;
				}
			}
		}
		pocetak = null;
		kraj = null;
		return true;
	}//LBenDialog_txtDateUntil_FV
	
	
	public boolean zaKraj(){
		//Datumi 
		java.sql.Date pocetak = null;
		java.sql.Date kraj = null;
		if((ra.getScreenContext().compareTo("scr_insert")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0) ){ 
			pocetak = (java.sql.Date)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateFrom");
			kraj = (java.sql.Date)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtDateUntil");
			
			if((pocetak == null) || (kraj == null)){
				//Nisu upisani datumi pocetka i kraja vazenja zapisa
				ra.showMessage("wrnclt20a");
				return false;
			}
			if((pocetak != null) && (kraj != null)){
				if(kraj.before(pocetak)){
					//Datum DO ne može biti manji od datuma OD
					ra.showMessage("wrnclt20");
					return false;
				}
			}
		}
		pocetak = null;
		kraj = null;
		
		
		
			
	
		//
		//
		//Ako su sve kontrole prosle onda moze potvrda, insert ili slicno
		return true;
		
		
	}//zaKraj
	
	public boolean checkAccNoRequestNo(){
		String accNo = (String)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtAccNo");
		String requestNo = 	(String)ra.getAttribute("LBenDialogLDB", "LBenDialog_txtRequestNo");
		String apsNo = (String) ra.getAttribute("LBenDialogLDB", "LBenDialog_txtApsRqstNo");
		String contractNo = (String) ra.getAttribute("LBenDialogLDB", "LBenDialog_txtContractNo");
		
		System.out.println("");
	      if((accNo == null && requestNo == null && apsNo == null && contractNo==null)||(accNo.equals("")&&requestNo.equals("")&&apsNo.equals("")&&contractNo.equals(""))){
	            //Mora biti upisano bar jedno od: partija plasmana, broj zahtjeva, broj ugovora
	            ra.showMessage("wrnclt60");
	            return false;
	      }
		
		
/*		if(accNo == null && requestNo == null && apsNo == null ){
			//Mora biti upisano bar jedno od: partija plasmana, broj zahtjeva
			ra.showMessage("wrnclt60");
			return false;
		} 
		
		if((accNo != null) && (requestNo == null || apsNo == null)){
			if(accNo.trim().length()== 0){
				//Mora biti upisano bar jedno od: partija plasmana, broj zahtjeva
				ra.showMessage("wrnclt60");
				return false;
			}
		
		}
		if((accNo == null || apsNo == null ) && (requestNo != null)){
			if(requestNo.trim().length()== 0){
				//Mora biti upisano bar jedno od: partija plasmana, broj zahtjeva
				ra.showMessage("wrnclt60");
				return false;
			}
		
		}

		if((accNo == null || requestNo == null) && (apsNo != null)){
			if(apsNo.trim().length()== 0){
				//Mora biti upisano bar jedno od: partija plasmana, broj zahtjeva
				ra.showMessage("wrnclt60");
				return false;
			}
		
		}		
		
		if((accNo != null) && (requestNo != null) && (accNo != null)){
			if(requestNo.trim().length()== 0){
				if(accNo.trim().length()== 0){
					if(apsNo.trim().length()== 0){
				//Mora biti upisano bar jedno od: partija plasmana, broj zahtjeva
						ra.showMessage("wrnclt60");
						return false;
					}
				}
			}
		   
		}*/
		return true;
	}
	  
	  
	public boolean LBenDialog_txtAccNo_FV(String elementName, Object elementValue, Integer lookUpType) throws VestigoTMException {
		
		BigDecimal cusIdKorisnika = (BigDecimal)ra.getAttribute("LBenDialogLDB", "LBenDialog_CUS_ID");
        
		if (cusIdKorisnika == null) {
			ra.showMessage("wrnclt74");
			//Najprije odaberite komitenta
			return false;
		}
		
		if (elementValue == null || ((String) elementValue).equals("")) {                                                                
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtAccNo",""); 
			ra.setAttribute("LBenDialogLDB","LBenDialog_LA_ACC_ID",null); 
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtRequestNo",""); 
			return true;                                                                                                                   
		}                                                                                                                                
			                                                                                                                                 
		if (!(ra.isLDBExists("CusaccExposureLookLDB"))) {
			ra.createLDB("CusaccExposureLookLDB");
		}	
			 
		ra.setAttribute("CusaccExposureLookLDB", "cus_id", ra.getAttribute("LBenDialogLDB", "LBenDialog_CUS_ID"));
		ra.setAttribute("CusaccExposureLookLDB", "cus_acc_no", ra.getAttribute("LBenDialogLDB", "LBenDialog_txtAccNo")); 
	                                                                                                                                     
		LookUpRequest lookUpRequest = new LookUpRequest("CusaccExposureLookUp");   
		lookUpRequest.addMapping("CusaccExposureLookLDB", "cus_acc_id", "cus_acc_id");         
		lookUpRequest.addMapping("CusaccExposureLookLDB", "cus_acc_no", "cus_acc_no");  
	    lookUpRequest.addMapping("CusaccExposureLookLDB", "contract_no", "contract_no");
		lookUpRequest.addMapping("CusaccExposureLookLDB", "cus_acc_status", "cus_acc_status");    			
		lookUpRequest.addMapping("CusaccExposureLookLDB", "cus_acc_orig_st", "cus_acc_orig_st");   
		lookUpRequest.addMapping("CusaccExposureLookLDB", "frame_cus_acc_no", "frame_cus_acc_no");   
		lookUpRequest.addMapping("CusaccExposureLookLDB", "exposure_cur_id", "exposure_cur_id");  
		lookUpRequest.addMapping("CusaccExposureLookLDB", "code_char", "code_char"); 
		lookUpRequest.addMapping("CusaccExposureLookLDB", "exposure_balance", "exposure_balance");                                                            
		lookUpRequest.addMapping("CusaccExposureLookLDB", "exposure_date", "exposure_date");          
		lookUpRequest.addMapping("CusaccExposureLookLDB", "request_no", "request_no");  
	    lookUpRequest.addMapping("CusaccExposureLookLDB", "prod_code", "prod_code");  
	    lookUpRequest.addMapping("CusaccExposureLookLDB", "name", "name");          
		lookUpRequest.addMapping("CusaccExposureLookLDB", "module_code", "module_code");

		  	                                         
		                                                                                                                                   
		try {                                                                                                                            
			ra.callLookUp(lookUpRequest);                                                                                                  
		} catch (EmptyLookUp elu) {       
			if ((((String) ra.getAttribute("CusaccExposureLookLDB", "cus_acc_no")).trim()).equalsIgnoreCase("*")) {
				ra.showMessage("err012");
				return false;
			}
// ako je upisana partija koja ne postoji ipak dozvoliti unos ako zadovoljava formalnu kontrolu 
// formalna kontrola upisane partije 
			  
			ra.setAttribute("LBenDialogLDB", "LBenDialog_LA_ACC_ID", null);     // ako upisu novi broj partije a prethodno je za stari bio id			
			
			if (coll_util.ctrlCusaccExposureAccount((String) ra.getAttribute("CusaccExposureLookLDB", "cus_acc_no"))) {

				return true;
			} else {
				Integer retValue = (Integer) ra.showMessage("col_qer014");
				if (retValue!=null) {
					if (retValue.intValue() == 0){ 
						return false;
					}else{
						return true;
					}
				}
			}  
			 
                                                                                                                    
		} catch (NothingSelected ns) {                                                                                                   
			return false;                                                                                                                  
		}                                                                                                                                

// 02.04.2010 - nije dozvoljeno povezivanje partije iz okvira direktno na kolateral
		String frame_cus_acc_no = (String) ra.getAttribute("CusaccExposureLookLDB", "frame_cus_acc_no"); 
		if (frame_cus_acc_no != null && frame_cus_acc_no.startsWith("60"))
		{
            // provjera da li plasman smije biti vezan direktno
		    ra.executeTransaction();
		    String frame_acc_exception_flag = (String)ra.getAttribute("LBenDialogLDB", "LBenDialog_frame_acc_exception_flag");
		    if("N".equals(frame_acc_exception_flag))
		    {
		        ra.showMessage("wrnclt168");
                return false;
		    }
		}
		
	  	
// 07.01.2008 - nije dozvoljen izbor partije koja ima DWH status C ili R, dozvoljen je samo status A
		String cus_acc_status = (String) ra.getAttribute("CusaccExposureLookLDB", "cus_acc_status");
		String module_code = (String) ra.getAttribute("CusaccExposureLookLDB", "module_code");
		String cus_acc_orig_st = (String) ra.getAttribute("CusaccExposureLookLDB", "cus_acc_orig_st");
		
		if (cus_acc_status !=null) {
			if (cus_acc_status.equalsIgnoreCase("R")) {
				ra.showMessage("wrnclt135");
				return false;				
			}
			if (cus_acc_status.equalsIgnoreCase("C")) {
// 28.10.2008 - dodana provjera da li je partija iz PKR u statusu N - neiskoristen kredit
// 17.02.2009 - dodana provjera da li je partija na SSP (takva se moze vezati na kolateral)				

					
				if (module_code.equalsIgnoreCase("PKR") && cus_acc_orig_st.equalsIgnoreCase("N")) {
						
				} else if (module_code.equalsIgnoreCase("PKR") && cus_acc_orig_st.equalsIgnoreCase("T")) {
						
				} else if (module_code.equalsIgnoreCase("TRC") && cus_acc_orig_st.equalsIgnoreCase("E")) {	
					
				} else if (module_code.equalsIgnoreCase("PPZ") && (cus_acc_orig_st.equalsIgnoreCase("SS") || cus_acc_orig_st.equalsIgnoreCase("NM"))) {
				    
				} else if (module_code.equalsIgnoreCase("SDR") && (cus_acc_orig_st.equalsIgnoreCase("SS") || cus_acc_orig_st.equalsIgnoreCase("NM"))) {
						
				} else if (module_code.equalsIgnoreCase("KRD") && cus_acc_orig_st.equalsIgnoreCase("SS")) {
					
				} else if (module_code.equalsIgnoreCase("GAR") && cus_acc_orig_st.equalsIgnoreCase("SS")) {	
						
				} else if (module_code.equalsIgnoreCase("KKR") && (cus_acc_orig_st.equalsIgnoreCase("94") || cus_acc_orig_st.equalsIgnoreCase("95"))) {
						
				} else if (module_code.equalsIgnoreCase("LOC") && cus_acc_orig_st.equalsIgnoreCase("SS")) {	
						
				} else {
					ra.showMessage("wrnclt135");
					return false;
				}
				
			} 
		}  
			      
		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtAccNo", ra.getAttribute("CusaccExposureLookLDB", "cus_acc_no"));
		ra.setAttribute("LBenDialogLDB", "LBenDialog_LA_ACC_ID", ra.getAttribute("CusaccExposureLookLDB", "cus_acc_id"));  
	    ra.setAttribute("LBenDialogLDB", "LBenDialog_txtContractNo", ra.getAttribute("CusaccExposureLookLDB", "contract_no"));  
// napuniti broj zahtjeva samo ako je prazan, inace NE
		String request_no = (String) ra.getAttribute("CusaccExposureLookLDB", "request_no");
		  
		if (request_no != null && (!request_no.trim().equals(""))) {
			ra.setAttribute("LBenDialogLDB", "LBenDialog_txtRequestNo", ra.getAttribute("CusaccExposureLookLDB", "request_no")); 
		} 
//		ra.setAttribute("LBenDialogLDB", "LBenDialog_txtRequestNo", ra.getAttribute("CusaccExposureLookLDB", "request_no"));     
			       
		return true;                                                                                                                     
	}
	
	  
	public boolean isOnlyDigitAndDash(String argString){  
	 	
	 		char dash = '-';
			boolean onlyDigitAndDash = true;                       	
			int lenString = argString.length();             	
			for(int i=0;i<lenString;i++){                   
				if(          !(Character.isDigit(argString.charAt(i)))  &&  !(argString.charAt(i) == dash) ){
					onlyDigitAndDash = false;                          
					break;                                      
 				}                                             
			}                                                
			return onlyDigitAndDash;                               
 	}        
	
	
	public boolean isTwoFollowingDashes(String argString){  
	 	
	 		char dash = '-';
			boolean twoDashes = false;          	
			
			int lenString = argString.length();             	
			
					
			for(int i=0;i<lenString;i++){                   
				if(i >0){
				
					if( (argString.charAt(i) == dash)   && (argString.charAt(i-1) == dash)){
//						System.out.println(argString.charAt(i));
//						System.out.println(argString.charAt(i-1));
			 			
						twoDashes = true;    
						break;
					}   
				}                                        
			}                                               
			return twoDashes;                               
 	}                       
	
	public boolean LBenDialog_Insu_FV(String ElName, Object ElValue, Integer LookUp) {
//		 da li je plasman osiguran, ako je upisati osiguravatelja
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute("LBenDialogLDB", "LBenDialog_Insu", null);
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolRegNo","");
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolName","");
			ra.setAttribute("LBenDialogLDB","LBenDialog_IP_CUS_ID",null);	
			ra.setContext("LBenDialog_txtInsPolRegNo", "fld_change_protected");		
			
			return true;
		}
			    
		LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");		

		request.addMapping("LBenDialogLDB", "LBenDialog_Insu", "Vrijednosti");

		try {
			ra.callLookUp(request);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		
// ako je u polje upisan D, treba omoguciti upis osiguravatelja
		String ins_flag = (String) ra.getAttribute("LBenDialogLDB","LBenDialog_Insu");
		if (ins_flag != null && ins_flag.equalsIgnoreCase("D")) {
			ra.setContext("LBenDialog_txtInsPolRegNo", "fld_plain");
			ra.setRequired("LBenDialog_txtInsPolRegNo", true);
			ra.setCursorPosition("LBenDialog_txtInsPolRegNo");
		} else {  
			ra.setContext("LBenDialog_txtInsPolRegNo", "fld_change_protected");	
			ra.setRequired("LBenDialog_txtInsPolRegNo", false);
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolRegNo","");
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolName","");
			ra.setAttribute("LBenDialogLDB","LBenDialog_IP_CUS_ID",null);			
		}
		
		return true; 
				 
	} // LBenDialog_Insu_FV		 
	
	
	public boolean LBenDialog_txtInsPolRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {
		if (elementValue == null || ((String) elementValue).equals("")) {                                    
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolRegNo","");
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolName","");
			ra.setAttribute("LBenDialogLDB","LBenDialog_IP_CUS_ID",null);
			ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolOIB", null);  
			return true;                                                                                 
		}                                                                                               

		if (ra.getCursorPosition().equals("LBenDialog_txtInsPolName")) {
			ra.setAttribute("LBenDialogLDB", "LBenDialog_txtInsPolRegNo", "");
			ra.setAttribute("LBenDialogLDB", "LBenDialog_IP_CUS_ID", null); 
			ra.setCursorPosition(1);
 
		} else if (ra.getCursorPosition().equals("LBenDialog_txtInsPolRegNo")) {
			ra.setAttribute("LBenDialogLDB", "LBenDialog_txtInsPolName", "");
			ra.setAttribute("LBenDialogLDB", "LBenDialog_IP_CUS_ID", null); 
			ra.setCursorPosition(2);
		}	 

        String d_register_no = "";                                                                                                       
        if (ra.getAttribute("LBenDialogLDB", "LBenDialog_txtInsPolRegNo") != null){                                                   
            d_register_no = (String) ra.getAttribute("LBenDialogLDB", "LBenDialog_txtInsPolRegNo");                                     
        }     
 
                                                                                                                                    
        //JE LI zvjezdica na pravom mjestu kod register_no                                                                               
        if (CharUtil.isAsteriskWrong(d_register_no)) {                                                                                   
            ra.showMessage("wrn367");                                                                                                      
            return false;                                                                                                                  
        }           

        if (ra.isLDBExists("InsuCompanyLDB")) {
            ra.setAttribute("InsuCompanyLDB", "insu_company_id", null);                                                                       
            ra.setAttribute("InsuCompanyLDB", "insu_company_register_no", "");                                                                    
            ra.setAttribute("InsuCompanyLDB", "insu_company_code", "");                                                                           
            ra.setAttribute("InsuCompanyLDB", "insu_company_oib", "");                                                                           
            ra.setAttribute("InsuCompanyLDB", "insu_company_name", "");                                                                 
        } else {
            ra.createLDB("InsuCompanyLDB");   
        }
                                                                                                                               
        ra.setAttribute("InsuCompanyLDB", "insu_company_register_no", ra.getAttribute("LBenDialogLDB", "LBenDialog_txtInsPolRegNo"));
                                                                                                                             
        LookUpRequest lookUpRequest = new LookUpRequest("InsuCompanyOibLookUp");                                                            
        lookUpRequest.addMapping("InsuCompanyLDB", "insu_company_register_no", "ic_register_no");                                                            
        lookUpRequest.addMapping("InsuCompanyLDB", "insu_company_oib", "tax_number");                                                  
        lookUpRequest.addMapping("InsuCompanyLDB", "insu_company_name", "ic_name");                                                                
        lookUpRequest.addMapping("InsuCompanyLDB", "insu_company_id", "ic_id");                                                                
        lookUpRequest.addMapping("InsuCompanyLDB", "insu_company_code", "ic_code");                                            
                                                                                                                    
        try {                                                                                                                            
            ra.callLookUp(lookUpRequest);                                                                                                  
        } catch (EmptyLookUp elu) {                                                                                                      
            ra.showMessage("err012");                                                                                                      
            return false;                                                                                                                  
        } catch (NothingSelected ns) {                                                                                                   
            return false;                                                                                                                  
        }                                                                                                                       

        ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolRegNo",ra.getAttribute("InsuCompanyLDB", "insu_company_register_no"));
        ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolName",ra.getAttribute("InsuCompanyLDB", "insu_company_name"));
        ra.setAttribute("LBenDialogLDB","LBenDialog_IP_CUS_ID",ra.getAttribute("InsuCompanyLDB", "insu_company_id"));
        ra.setAttribute("LBenDialogLDB","LBenDialog_txtInsPolOIB", ra.getAttribute("InsuCompanyLDB", "insu_company_oib"));  
        
		return true;                            		
	}	  
	
	
	public void confirm_loan(){
		if(ra.getScreenContext().compareTo("scr_kred_admin")== 0){
// dozvoliti akicju samo ako indikator nije postavljen na D
			String kred_admin = (String) ra.getAttribute("LBenDialogLDB", "LBenDialog_txtKredAdmin");
			
			if (kred_admin != null && !(kred_admin.equalsIgnoreCase("")) && kred_admin.equalsIgnoreCase("D")) {
				ra.showMessage("wrnclt149");
			}else {

				Integer retValue = (Integer) ra.showMessage("col_qer027");
				if (retValue!=null) {
					if (retValue.intValue() == 0){ 
						return;
					}else{
						ra.setAttribute("LBenDialogLDB", "LBenDialog_txtKredAdmin","D");
						ra.setAttribute("LBenDialogLDB", "LBenDialog_KredAdminUSE_ID", ra.getAttribute("GDB", "use_id"));
						ra.setAttribute("LBenDialogLDB", "LBenDialog_txtKredAdminUserId", ra.getAttribute("GDB", "Use_Login"));
						ra.setAttribute("LBenDialogLDB", "LBenDialog_txtKredAdminUserName", ra.getAttribute("GDB", "Use_UserName"));
						
						try {
							ra.executeTransaction();
							ra.showMessage("infclt2");
//							napuniti indikator i user-a 	  

						} catch (VestigoTMException vtme) {
							ra.setAttribute("LBenDialogLDB", "LBenDialog_txtKredAdmin", null);
							ra.setAttribute("LBenDialogLDB", "LBenDialog_KredAdminUSE_ID", null);
							ra.setAttribute("LBenDialogLDB", "LBenDialog_txtKredAdminUserId", "");
							ra.setAttribute("LBenDialogLDB", "LBenDialog_txtKredAdminUserName", "");
							
							error("LoanBeneficiaryDialog -> kredadmin(): VestigoTMException", vtme);
							if (vtme.getMessageID() != null)
								ra.showMessage(vtme.getMessageID());
						}
					}     
				}	
			}
//			ra.exitScreen();
//			ra.refreshActionList("tblLoanBeneficiary");
			 
		}
	}
}
