/*
 * Created on 2006.01.26
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


/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoanAccountList extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/LoanAccountList.java,v 1.27 2006/03/15 08:59:34 hrasia Exp $";	
	public LoanAccountList(ResourceAccessor ra) {
		super(ra);
			
	}
	public void LoanAccountList_SE() {
		
		if (!ra.isLDBExists("LoanAccountListLDB")) {
	 		ra.createLDB("LoanAccountListLDB");
	 	}
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
			ra.setAttribute("LoanAccountListLDB","LoanAccountList_txtColaCusIdUseRegisterNo",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaRegisterNo") );
			ra.setAttribute("LoanAccountListLDB","LoanAccountList_txtColaCusIdUseOwnerName",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaOwnerName") );
			ra.setAttribute("LoanAccountListLDB","LoanAccountList_CUS_ID",(java.math.BigDecimal)  ra.getAttribute("CollateralDialogLDB", "CUS_ID")  );
			
			
			hr.vestigo.framework.common.TableData td = null;
			if(ra.getAttribute("tblLoanAccountList")==null){
				td=null;
			}else{
				td = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblLoanAccountList");
			}
	        if (td == null)
	        	ra.createActionListSession("tblLoanAccountList", false);
	       
	        td = null;
	        
		}
		
		
		
	}//LoanAccountList_SE
	
				   
	public boolean LoanAccountList_LoanType_FV(String elementName, Object elementValue, Integer lookUpType) {
        //Collateral_txtLoanTypeCode
        //Collateral_txtLoanTypeName
        //LOAN_TYPE
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("LoanAccountListLDB", "LoanAccountList_txtLoanTypeCode", "");
            ra.setAttribute("LoanAccountListLDB", "LoanAccountList_txtLoanTypeName", "");
            ra.setAttribute("LoanAccountListLDB", "LoanAccountList_LOAN_TYPE", null);
            
            
            return true;
        }

       
        ra.setAttribute("LoanAccountListLDB", "SysCodId", "clt_inv_type");
        ra.setAttribute("LoanAccountListLDB", "dummySt", null);
        ra.setAttribute("LoanAccountListLDB", "dummyBD", null);        

       
        
        
        
        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");

        request.addMapping("LoanAccountListLDB", "LoanAccountList_txtLoanTypeCode", "sys_code_value");
        request.addMapping("LoanAccountListLDB", "LoanAccountList_txtLoanTypeName", "sys_code_desc");
        request.addMapping("LoanAccountListLDB", "LoanAccountList_LOAN_TYPE", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }
        if(ra.getCursorPosition().equals("LoanAccountList_txtLoanTypeCode")){
        	ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("LoanAccountList_txtLoanTypeName")){
			ra.setCursorPosition(1);
		}
        
        
        ra.setAttribute("LoanAccountListLDB", "dummySt", null);
        ra.setAttribute("LoanAccountListLDB", "dummyBD", null);        
        ra.setAttribute("LoanAccountListLDB", "SysCodId", "");
        return true;
    }//LoanAccountList_LoanType_FV	

	
	public boolean LoanAccountList_ModuleLoan_FV(String elementName, Object elementValue, Integer lookUpType) {
        //LoanAccountList_txtModuleLoanCode
        //LoanAccountList_txtModuleLoanName
        //MODULE_LOAN
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("LoanAccountListLDB", "LoanAccountList_txtModuleLoanCode", "");
            ra.setAttribute("LoanAccountListLDB", "LoanAccountList_txtModuleLoanName", "");
            ra.setAttribute("LoanAccountListLDB", "LoanAccountList_MODULE_LOAN", null);
            
            
            return true;
        }

       
        ra.setAttribute("LoanAccountListLDB", "SysCodId", "clt_inv_module");
        ra.setAttribute("LoanAccountListLDB", "dummySt", null);
        ra.setAttribute("LoanAccountListLDB", "dummyBD", null);        

       
        
        
        
        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");

        request.addMapping("LoanAccountListLDB", "LoanAccountList_txtModuleLoanCode", "sys_code_value");
        request.addMapping("LoanAccountListLDB", "LoanAccountList_txtModuleLoanName", "sys_code_desc");
        request.addMapping("LoanAccountListLDB", "LoanAccountList_MODULE_LOAN", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }
        if(ra.getCursorPosition().equals("LoanAccountList_txtModuleLoanCode")){
        	ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("LoanAccountList_txtModuleLoanName")){
			ra.setCursorPosition(1);
		}
        
        
        ra.setAttribute("LoanAccountListLDB", "dummySt", null);
        ra.setAttribute("LoanAccountListLDB", "dummyBD", null);        
        ra.setAttribute("LoanAccountListLDB", "SysCodId", "");
        return true;
    }//LoanAccountList_ModuleLoan_FV

	
	public void refresh(){
		//F5
		
		ra.refreshActionList("tblLoanAccountList");
		
	}//refresh
	
	
	
	public void details(){
		//F4
		
		if (isTableEmpty("tblLoanAccountList")) {
			ra.showMessage("wrn299");
			return;
		}
		
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("LoanAccountListLDB", "tblLoanAccountList");
		
		java.util.Vector nevidljiviVector = (java.util.Vector) td.getSelectedRowUnique();
		java.math.BigDecimal laAccId = (java.math.BigDecimal) nevidljiviVector.get(0);
		java.math.BigDecimal cusId = (java.math.BigDecimal) ra.getAttribute("LoanAccountListLDB", "LoanAccountList_CUS_ID") ;
		if (!ra.isLDBExists("LoanAccountDialogLDB")) {
	 		ra.createLDB("LoanAccountDialogLDB");
	 	}
		
		System.out.println("laAccId " + laAccId);
		System.out.println("cusId " + cusId);
		
		ra.setAttribute("LoanAccountDialogLDB", "LA_ACC_ID", laAccId); 
		ra.setAttribute("LoanAccountDialogLDB", "CUS_ID",  cusId);
		
		ra.setAttribute("LoanAccountDialogLDB","LoanAccountDialog_txtColaCusIdUseRegisterNo",(String)  ra.getAttribute("LoanAccountListLDB", "LoanAccountList_txtColaCusIdUseRegisterNo") );
		ra.setAttribute("LoanAccountDialogLDB","LoanAccountDialog_txtColaCusIdUseOwnerName",(String)  ra.getAttribute("LoanAccountListLDB", "LoanAccountList_txtColaCusIdUseOwnerName") );
		
		ra.loadScreen("LoanAccountDialog","scr_detail"); 
		
		try {
			 ra.executeTransaction();
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 
		   
		laAccId = null;
		cusId = null;
		td = null;
		////ra.showMessage("wrnclt0");
	}//details
	
	
	
	
	public void selection(){
		//F8
		
		if (isTableEmpty("tblLoanAccountList")) {
			ra.showMessage("wrn299");
			return;
		}
		
		if (!ra.isLDBExists("LoanComponentItemLDB")) {
	 		ra.createLDB("LoanComponentItemLDB");
	 	}
				
		if (!ra.isLDBExists("LoanComponentItemSLDB")) {
	 		ra.createLDB("LoanComponentItemSLDB");
	 	}
		
		String accNo = null;
		String loantypeSCD = null;
		String moduleloanSCD =null;
		java.math.BigDecimal balance = null;
		java.sql.Date balanceDate = null;
		String curIdLoanCodeC = null;
		java.math.BigDecimal balanceRef = null;
		String curIdLoanRefCodeC = null;
		
		
		
		
		java.math.BigDecimal laAccId = null;
		java.math.BigDecimal loanType = null;
		String loantypeSCV = null;
		java.math.BigDecimal moduleLoan = null;
		String moduleloanSCV = null;
		java.math.BigDecimal curIdLoan = null;
		java.math.BigDecimal curIdLoanRef = null;        
		
		java.math.BigDecimal amount = null;	   
		java.math.BigDecimal excRatRef = null;
		java.sql.Date excRatRefDate = null;
		
		java.sql.Date startDate = null;
		java.sql.Date payedDate = null;
		java.math.BigDecimal balanceB = null;
		java.sql.Date balanceBDdate = null;
		java.math.BigDecimal balanceBref = null;   
		java.math.BigDecimal cusId = null;       
        	           
		java.sql.Date laDateFrom = null;
		java.sql.Date laDateUntil = null;        
		String laStatus = null;
		String laSpecStatus = null;
		
		
		//Null all elements on LoanComponentItemLDB
		resetLoanComponentItemLDB();
		
		//Null all elements on CollateralDialogLDB refering on LoanComponentItem
		resetCollateralDialogLDBLoanComponentItem();
				
		
		
		
		
		     
			//Provjera moze li se dodati jos jedna komponenta plasmana pocetak
			//if velicina Loan > Coll + 1
			hr.vestigo.framework.common.TableData tdLoanSelectionMore = null;
			hr.vestigo.framework.common.TableData tdCollSelectionMore = null;
			int velicinaLoan = 0;
			int velicinaColl = 0;
			tdLoanSelectionMore = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
			tdCollSelectionMore = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");
			
			
			
			if(tdLoanSelectionMore != null){
				velicinaLoan = tdLoanSelectionMore.getData().size();
				System.out.println("LoanAccountList selection 1 velicinaLoan " + velicinaLoan );
			}
			if(tdCollSelectionMore != null){
				velicinaColl = tdCollSelectionMore.getData().size();
				System.out.println("LoanAccountList selection 2 velicinaColl " + velicinaColl );
			}
			
			if(velicinaLoan > velicinaColl){
				System.out.println("LoanAccountList selection 4 " + velicinaLoan +  " >  "  + velicinaColl );
				tdLoanSelectionMore = null;
				tdCollSelectionMore = null;
				ra.showMessage("wrnclt27");
				return;
			}else{
				System.out.println("LoanAccountList selection 5" );
				tdLoanSelectionMore = null;
				tdCollSelectionMore = null;
				
			}
			//Provjera moze li se dodati jos jedna komponenta plasmana kraj
			
		
		
		

		
		
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("LoanAccountListLDB", "tblLoanAccountList");
		
		java.util.Vector vidljiviVector = (java.util.Vector) td.getSelectedRowData();
		java.util.Vector nevidljiviVector = (java.util.Vector) td.getSelectedRowUnique();
		
		//Dohvat vidljivih
		accNo = (String) vidljiviVector.get(0);
		loantypeSCD = (String) vidljiviVector.get(1);
		moduleloanSCD =(String) vidljiviVector.get(2);
		balance = (java.math.BigDecimal) vidljiviVector.get(3);
		balanceDate = (java.sql.Date) vidljiviVector.get(4);
		curIdLoanCodeC = (String)vidljiviVector.get(5);
		balanceRef = (java.math.BigDecimal) vidljiviVector.get(6);
		curIdLoanRefCodeC = (String)vidljiviVector.get(7);
		
		
		
		//Dohvat nevidljivih
		laAccId = (java.math.BigDecimal) nevidljiviVector.get(0);
		loanType = (java.math.BigDecimal) nevidljiviVector.get(1);
		loantypeSCV = (String) nevidljiviVector.get(2);
		moduleLoan = (java.math.BigDecimal) nevidljiviVector.get(3);
		moduleloanSCV = (String) nevidljiviVector.get(4);
		curIdLoan = (java.math.BigDecimal) nevidljiviVector.get(5);
		curIdLoanRef = (java.math.BigDecimal) nevidljiviVector.get(6);	          
		
		amount = (java.math.BigDecimal) nevidljiviVector.get(7);	   
		excRatRef = (java.math.BigDecimal) nevidljiviVector.get(8);
		excRatRefDate = (java.sql.Date) nevidljiviVector.get(9);
		
		startDate = (java.sql.Date) nevidljiviVector.get(10);
		payedDate = (java.sql.Date) nevidljiviVector.get(11);
		balanceB = (java.math.BigDecimal ) nevidljiviVector.get(12);
		balanceBDdate = (java.sql.Date) nevidljiviVector.get(13);
		balanceBref = (java.math.BigDecimal ) nevidljiviVector.get(14);         
		cusId = (java.math.BigDecimal ) nevidljiviVector.get(15);         
        	           
		laDateFrom = (java.sql.Date) nevidljiviVector.get(16);
		laDateUntil = (java.sql.Date) nevidljiviVector.get(17);          
		laStatus = (String) nevidljiviVector.get(18);
		laSpecStatus = (String) nevidljiviVector.get(19);
			             
		
			
		
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUS_ID",cusId);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_CUS_ID",cusId);
		
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtColaCusIdUseRegisterNo",(String)  ra.getAttribute("LoanAccountListLDB", "LoanAccountList_txtColaCusIdUseRegisterNo"));
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtColaCusIdUseRegisterNo",(String)  ra.getAttribute("LoanAccountListLDB", "LoanAccountList_txtColaCusIdUseRegisterNo"));
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtColaCusIdUseOwnerName",(String)  ra.getAttribute("LoanAccountListLDB", "LoanAccountList_txtColaCusIdUseOwnerName"));
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtColaCusIdUseOwnerName",(String)  ra.getAttribute("LoanAccountListLDB", "LoanAccountList_txtColaCusIdUseOwnerName"));
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalance",balance);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalance",balance);
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID",curIdLoan);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_CUR_ID",curIdLoan);
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurIdLoanCodeC",curIdLoanCodeC);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalanceCurIdLoanCodeC",curIdLoanCodeC);
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceDate",balanceDate);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalanceDate",balanceDate);
		
		
		

		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceRef",balanceRef);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalanceRef",balanceRef);
		
		
		
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID_REF",curIdLoanRef);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_CUR_ID_REF",curIdLoanRef);
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurIdRefLoanCodeC",curIdLoanRefCodeC);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalanceCurIdRefLoanCodeC",curIdLoanRefCodeC);
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceDate1",balanceDate);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalanceDate1",balanceDate);
		
		
				
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRef",excRatRef);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtExcRatRef",excRatRef);
		
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefDate",excRatRefDate);
		ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtExcRatRefDate",excRatRefDate);
		
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laAccId",laAccId);        
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_accNo",accNo);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loanType",loanType);                  
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loantypeSCV" ,loantypeSCV);              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loantypeSCD" ,loantypeSCD );             
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleLoan" ,moduleLoan);               
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleloanSCV" ,moduleloanSCV );           
	    ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleloanSCD"  ,moduleloanSCD );          
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_amount" , amount );                 
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_startDate" , startDate );              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_payedDate" , payedDate);               
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceB" ,  balanceB );              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceBDdate" , balanceBDdate  );         
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceBref" , balanceBref );            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laDateFrom" , laDateFrom);              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laDateUntil" ,  laDateUntil);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laStatus" , laStatus);                
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laSpecStatus" , laSpecStatus );           
		                                                                                    
		     
		
		
		
		System.out.println ( "ULAZIM NA EKRAN LoanComponentItem");
		ra.loadScreen("LoanComponentItem","scr_choose"); 
		System.out.println ( "IZLAZIM SA EKRANA LoanComponentItem");
			
		
		
	}//selection
	
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
	
	
	public void resetLoanComponentItemLDB(){
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUS_ID",null);                              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtColaCusIdUseRegisterNo",null);           
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtColaCusIdUseOwnerName",null);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalance",null);                          
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID",null);                              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurIdLoanCodeC",null);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceDate",null);                      
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceRef",null);                       
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID_REF",null);                          
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurIdRefLoanCodeC",null);         
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceDate1",null);                     
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRef",null);                        
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefDate",null);   
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurrentToCover",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceRefCurrentToCover",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceLinkToCoverDate",null);    
		
				                                                                                                  

		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laAccId",null);        
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_accNo",null);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loanType",null);                  
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loantypeSCV" ,null);              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loantypeSCD" ,null );             
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleLoan" ,null);               
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleloanSCV" ,null );           
	    ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleloanSCD"  ,null );          
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_amount" , null );                 
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_startDate" , null );              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_payedDate" , null);               
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceB" ,  null );              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceBDdate" , null  );         
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceBref" , null );            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laDateFrom" , null);              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laDateUntil" ,  null);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laStatus" , null);                
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laSpecStatus" , null );           
		 
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefPart",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefDatePart",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_lcDateFrom",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_lcDateUntil",null);    
		ra.setAttribute("LoanComponentItemLDB","curAvailableLoanComponent",null);    
		ra.setAttribute("LoanComponentItemLDB","curSumTotalLoanComp",null);    
		ra.setAttribute("LoanComponentItemLDB","curSumAllLoanComptbl",null);    
		ra.setAttribute("LoanComponentItemLDB","curSumTotalFromLoanComponent",null);    

	}//resetLoanComponentItemLDB
	
	
	public void resetCollateralDialogLDBLoanComponentItem(){

	
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalance",null);  
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_CUR_ID",null);   
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurIdLoanCodeC",null); 
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceDate",null);                      
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceRef",null);  
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_CUR_ID_REF",null);                          
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurIdRefLoanCodeC",null); 
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceDate1",null); 
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtExcRatRef",null);                        
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtExcRatRefDate",null);   
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurrentToCover",null);    
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceRefCurrentToCover",null);    
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceLinkToCoverDate",null);    
		
	
		
		
	}//resetCollateralDialogLDBLoanComponentItem
	
	
}
