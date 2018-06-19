package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.util.CharUtil;

/**
 * @author Ivan Simunovic
 *
 */
public class Collateral extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/Collateral.java,v 1.8 2006/02/23 14:45:58 hrasia Exp $";
	/**
	 * Constructor for Collateral.
	 */
	public Collateral(ResourceAccessor ra) {
		super(ra);
			
	}	

	public void Collateral_SE() {
		if (!ra.isLDBExists("CollateralLDB")) {
	 		ra.createLDB("CollateralLDB");
	 	}
		
		
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblLoanRelColl");
        if (td == null)
        	ra.createActionListSession("tblLoanRelColl", false);
        td = null;
        	
              
        
	}	 
	
	
	
	
	public boolean Collateral_ColaCusId_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollateralLDB";
		java.math.BigDecimal cusId = null;
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("CollateralLDB","Collateral_txtColaCusIdUseRegisterNo",null);
			ra.setAttribute("CollateralLDB","Collateral_txtColaCusIdUseOwnerName",null);
			ra.setAttribute("CollateralLDB","CUS_ID",null);
			return true;
		}
		
		if (ra.getCursorPosition().equals("Collateral_txtColaCusIdUseOwnerName")) {
			ra.setAttribute(ldbName, "Collateral_txtColaCusIdUseRegisterNo", "");
		} else if (ra.getCursorPosition().equals("Collateral_txtColaCusIdUseRegisterNo")) {
			ra.setAttribute(ldbName, "Collateral_txtColaCusIdUseOwnerName", "");
			//ra.setCursorPosition(2);
		}
		
		
		
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "Collateral_txtColaCusIdUseOwnerName") != null){
			d_name = (String) ra.getAttribute(ldbName, "Collateral_txtColaCusIdUseOwnerName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Collateral_txtColaCusIdUseRegisterNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Collateral_txtColaCusIdUseRegisterNo");
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
		
		
		 if (ra.getCursorPosition().equals("Collateral_txtColaCusIdUseRegisterNo")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("CollateralLDB", "Collateral_txtColaCusIdUseRegisterNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("CollateralLDB", "Collateral_txtColaCusIdUseOwnerName"));

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

		ra.setAttribute("CollateralLDB", "CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("CollateralLDB", "Collateral_txtColaCusIdUseRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("CollateralLDB", "Collateral_txtColaCusIdUseOwnerName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		
		//
		
		
		cusId = (java.math.BigDecimal) ra.getAttribute("CollateralLDB", "CUS_ID");
		
		if((cusId != null)){
			//ra.refreshActionList("tblRealEstate");	
		}
		ldbName = null;
		cusId = null;
		return true;
	}
	
	
	public boolean Collateral_CollateralType_FV(String ElName, Object ElValue, Integer LookUp) {
		if (ElValue == null || ((String) ElValue).equals("")) {
				ra.setAttribute("CollateralLDB", "Collateral_txtCollTypeCode", "");
				ra.setAttribute("CollateralLDB", "Collateral_txtCollTypeName", "");
				ra.setAttribute("CollateralLDB", "COL_TYPE_ID", null);
				ra.setAttribute("CollateralLDB", "dummySt", null);   
				ra.setAttribute("CollateralLDB", "dummyDate", null); 
				ra.setAttribute("CollateralLDB", "dummyBD", null);  
				
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollPeriodRev", "");
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollMvpPonder", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollMvpPonderMin", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollMvpPonderMax", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollHnbPonder", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollHnbPonderMin", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollHnbPonderMax", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollRzbPonder", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollRzbPonderMin", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollRzbPonderMax", null);
				//ra.setAttribute("CollateralLDB", "dummySt", null);
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollAccounting", "");
				//ra.setAttribute("CollateralLDB", "RealEstate_txtCollAccountingName", "");
				//ra.setAttribute("CollateralLDB", "dummyDate", null);
				return true;
			}


		 	if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
		 		ra.createLDB("CollateralTypeLookUpLDB");
		 	}
		 	ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", "SVE");
		 	
			LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp"); 
			lookUpRequest.addMapping("CollateralLDB", "COL_TYPE_ID", "coll_type_id");
			lookUpRequest.addMapping("CollateralLDB", "Collateral_txtCollTypeCode", "coll_type_code");
			lookUpRequest.addMapping("CollateralLDB", "Collateral_txtCollTypeName", "coll_type_name");
			lookUpRequest.addMapping("CollateralLDB", "dummySt", "coll_period_rev");
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_mvp_ponder");
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_mvp_ponder_mn");
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_mvp_ponder_mx");
					
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_hnb_ponder");
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_hnb_ponder_mn");
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_hnb_ponder_mx");
			
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_rzb_ponder");
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_rzb_ponder_mn");
			lookUpRequest.addMapping("CollateralLDB", "dummyBD", "coll_rzb_ponder_mx");
						
			lookUpRequest.addMapping("CollateralLDB", "dummySt", "coll_category");
			lookUpRequest.addMapping("CollateralLDB", "dummySt", "coll_hypo_fidu");
			lookUpRequest.addMapping("CollateralLDB", "dummySt", "hypo_fidu_name");
			lookUpRequest.addMapping("CollateralLDB", "dummySt", "coll_anlitika");
			lookUpRequest.addMapping("CollateralLDB", "dummySt", "coll_accounting");
			lookUpRequest.addMapping("CollateralLDB", "dummySt", "accounting_name");
			
			lookUpRequest.addMapping("CollateralLDB", "dummyDate", "coll_date_from");
			lookUpRequest.addMapping("CollateralLDB", "dummyDate", "coll_date_until");
			
			
			try {
					ra.callLookUp(lookUpRequest);
			} catch (EmptyLookUp elu) {
					ra.showMessage("err012");
					return false;
			} catch (NothingSelected ns) {
					return false;
			}
			
			if(ra.getCursorPosition().equals("Collateral_txtCollTypeCode")){
	        	ra.setCursorPosition(2);
			}
			if(ra.getCursorPosition().equals("Collateral_txtCollTypeName")){
				ra.setCursorPosition(1);
			}
			
			return true;

	}//Collateral_CollateralType_FV

	public boolean Collateral_LoanType_FV(String elementName, Object elementValue, Integer lookUpType) {
        //Collateral_txtLoanTypeCode
        //Collateral_txtLoanTypeName
        //LOAN_TYPE
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CollateralLDB", "Collateral_txtLoanTypeCode", "");
            ra.setAttribute("CollateralLDB", "Collateral_txtLoanTypeName", "");
            ra.setAttribute("CollateralLDB", "LOAN_TYPE", null);
            
            
            return true;
        }

       
        ra.setAttribute("CollateralLDB", "SysCodId", "clt_inv_type");
        ra.setAttribute("CollateralLDB", "dummySt", null);
        ra.setAttribute("CollateralLDB", "dummyBD", null);        

       
        
        
        
        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");

        request.addMapping("CollateralLDB", "Collateral_txtLoanTypeCode", "sys_code_value");
        request.addMapping("CollateralLDB", "Collateral_txtLoanTypeName", "sys_code_desc");
        request.addMapping("CollateralLDB", "LOAN_TYPE", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }
        if(ra.getCursorPosition().equals("Collateral_txtLoanTypeCode")){
        	ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("Collateral_txtLoanTypeName")){
			ra.setCursorPosition(1);
		}
        
        
        ra.setAttribute("CollateralLDB", "dummySt", null);
        ra.setAttribute("CollateralLDB", "dummyBD", null);        
        ra.setAttribute("CollateralLDB", "SysCodId", "");
        return true;
    }//Collateral_LoanType_FV	

	public boolean Collateral_ModuleLoan_FV(String elementName, Object elementValue, Integer lookUpType) {
        //Collateral_txtModuleLoanCode
        //Collateral_txtModuleLoanName
        //MODULE_LOAN
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CollateralLDB", "Collateral_txtModuleLoanCode", "");
            ra.setAttribute("CollateralLDB", "Collateral_txtModuleLoanName", "");
            ra.setAttribute("CollateralLDB", "MODULE_LOAN", null);
            
            
            return true;
        }

       
        ra.setAttribute("CollateralLDB", "SysCodId", "clt_inv_module");
        ra.setAttribute("CollateralLDB", "dummySt", null);
        ra.setAttribute("CollateralLDB", "dummyBD", null);        

       
        
        
        
        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");

        request.addMapping("CollateralLDB", "Collateral_txtModuleLoanCode", "sys_code_value");
        request.addMapping("CollateralLDB", "Collateral_txtModuleLoanName", "sys_code_desc");
        request.addMapping("CollateralLDB", "MODULE_LOAN", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }
        if(ra.getCursorPosition().equals("Collateral_txtModuleLoanCode")){
        	ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("Collateral_txtModuleLoanName")){
			ra.setCursorPosition(1);
		}
        
        
        ra.setAttribute("CollateralLDB", "dummySt", null);
        ra.setAttribute("CollateralLDB", "dummyBD", null);        
        ra.setAttribute("CollateralLDB", "SysCodId", "");
        return true;
    }//Collateral_ModuleLoan_FV	

	
	
	public void add(){
		//Dizanje ekrana nekretnine nakon definicije akcije
		java.math.BigDecimal customerCusId = null;
		String customerRegisterNo = null;
		
		customerCusId = (java.math.BigDecimal) ra.getAttribute("CollateralLDB","CUS_ID");
		customerRegisterNo = (String)ra.getAttribute("CollateralLDB","Collateral_txtColaCusIdUseRegisterNo");
		
		if( (customerCusId == null) || (customerRegisterNo == null)){
			ra.showMessage("wrnclt9");
			return;
		}else{
			ra.loadScreen("CollateralDialog","scr_insert");
		}
		
		
	}//add
	
	
	public void refresh(){
		
       	java.math.BigDecimal collCusId = (java.math.BigDecimal) ra.getAttribute("CollateralLDB", "CUS_ID");
		System.out.println ( "CUS_ID JE "  + collCusId);
		if( !(ra.isActionListSessionActive("tblLoanRelColl"))){
			ra.createActionListSession("tblLoanRelColl", false);
		}
		
		if((collCusId != null)){
			ra.refreshActionList("tblLoanRelColl");	
		}
		
		collCusId = null;
		
		
		//ra.showMessage("wrnclt0");
	}//refresh
	public void details(){
		ra.showMessage("wrnclt0");
	}//details
	public void action(){
		ra.showMessage("wrnclt0");
	}//action
	
}
