/*
 * Created on 2006.01.15
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

//LIST COLL_HF_PRIOR in favour of Rba

/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * LIST COLL_HF_PRIOR in favour of Rba
 */
public class CollHfPriorListRba extends Handler {

	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollHfPriorListRba.java,v 1.25 2006/03/15 08:57:46 hrasia Exp $";
 

	
	public CollHfPriorListRba(ResourceAccessor ra) {
		super(ra);
	}
	
	public void CollHfPriorListRba_SE() {
		
		
		
		if (!ra.isLDBExists("CollHfPriorListRbaLDB")) {
	 		ra.createLDB("CollHfPriorListRbaLDB");
	 	}
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
			
			System.out.println("LALALALALALALALALALA ");
			ra.setAttribute("CollHfPriorListRbaLDB","CollHfPriorListRba_txtColaCusIdUseRegisterNo",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaRegisterNo") );
			ra.setAttribute("CollHfPriorListRbaLDB","CollHfPriorListRba_txtColaCusIdUseOwnerName",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaOwnerName") );
			ra.setAttribute("CollHfPriorListRbaLDB","CollHfPriorListRba_CUS_ID",(java.math.BigDecimal)  ra.getAttribute("CollateralDialogLDB", "CUS_ID")  );
			
			hr.vestigo.framework.common.TableData td = null;
			if(ra.getAttribute("tblCollHfPriorListRba")==null){
				td=null;
			}else{
				td = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollHfPriorListRba");
			}
	        if (td == null)
	        	ra.createActionListSession("tblCollHfPriorListRba", false);
	                                       
											   
											   
		}
		
		
		
	}//CollHfPriorList_SE
	
	public boolean CollHfPriorListRba_CollateralType_FV(String ElName, Object ElValue, Integer LookUp) {
		if (ElValue == null || ((String) ElValue).equals("")) {
				ra.setAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_txtCollTypeCode", "");
				ra.setAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_txtCollTypeName", "");
				ra.setAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_COL_TYPE_ID", null);
				ra.setAttribute("CollHfPriorListRbaLDB", "dummySt", null);   
				ra.setAttribute("CollHfPriorListRbaLDB", "dummyDate", null); 
				ra.setAttribute("CollHfPriorListRbaLDB", "dummyBD", null);  
				
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
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "CollHfPriorListRba_COL_TYPE_ID", "coll_type_id");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "CollHfPriorListRba_txtCollTypeCode", "coll_type_code");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "CollHfPriorListRba_txtCollTypeName", "coll_type_name");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummySt", "coll_period_rev");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_mvp_ponder");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_mvp_ponder_mn");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_mvp_ponder_mx");
					
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_hnb_ponder");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_hnb_ponder_mn");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_hnb_ponder_mx");
			
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_rzb_ponder");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_rzb_ponder_mn");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyBD", "coll_rzb_ponder_mx");
						
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummySt", "coll_category");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummySt", "coll_hypo_fidu");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummySt", "hypo_fidu_name");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummySt", "coll_anlitika");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummySt", "coll_accounting");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummySt", "accounting_name");
			
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyDate", "coll_date_from");
			lookUpRequest.addMapping("CollHfPriorListRbaLDB", "dummyDate", "coll_date_until");
			
			
			try {
					ra.callLookUp(lookUpRequest);
			} catch (EmptyLookUp elu) {
					ra.showMessage("err012");
					return false;
			} catch (NothingSelected ns) {
					return false;
			}
			
			if(ra.getCursorPosition().equals("CollHfPriorListRba_txtCollTypeCode")){
	        	ra.setCursorPosition(2);
			}
			if(ra.getCursorPosition().equals("CollHfPriorListRba_txtCollTypeName")){
				ra.setCursorPosition(1);
			}
			
			return true;

	}//CollHfPriorListRba_CollateralType_FV

	
	
	
	public void refresh(){
		//F5
		
		ra.refreshActionList("tblCollHfPriorListRba");
		////ra.showMessage("wrnclt0");
	}//refresh
	
	
	
	public void details(){
//		F4
		if (isTableEmpty("tblCollHfPriorListRba")) {
			ra.showMessage("wrn299");
			return;
		}
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("CollHfPriorListRbaLDB", "tblCollHfPriorListRba");
		
		java.util.Vector nevidljiviVector = (java.util.Vector) td.getSelectedRowUnique();
		java.math.BigDecimal collHfPriorId = (java.math.BigDecimal) nevidljiviVector.get(0);
		
		
		
		java.math.BigDecimal cusId = (java.math.BigDecimal) ra.getAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_CUS_ID") ;
		String registerNo = (String)  ra.getAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_txtColaCusIdUseRegisterNo");
		String useOwnerName = (String)  ra.getAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_txtColaCusIdUseOwnerName");
		
		if (!ra.isLDBExists("CollHfPriorDialogRbaLDB")) {
	 		ra.createLDB("CollHfPriorDialogRbaLDB");
	 	}
		
		System.out.println("collHfPriorId " + collHfPriorId);
		System.out.println("cusId " + cusId);
		System.out.println("registerNo " + registerNo);
		System.out.println("useOwnerName " + useOwnerName);
		
		
		
		
		ra.setAttribute("CollHfPriorDialogRbaLDB", "COLL_HF_PRIOR_ID", collHfPriorId); 
		ra.setAttribute("CollHfPriorDialogRbaLDB", "CUS_ID",  cusId);
		ra.setAttribute("CollHfPriorDialogRbaLDB","CollHfPriorDialog_txtColaCusIdUseRegisterNo",registerNo );
		ra.setAttribute("CollHfPriorDialogRbaLDB","CollHfPriorDialog_txtColaCusIdUseOwnerName",useOwnerName );
		ra.loadScreen("CollHfPriorDialogRba","scr_detail"); 
		
		try {
			 ra.executeTransaction();
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 
		   
		collHfPriorId = null;
		cusId = null;
		registerNo = null;
		useOwnerName = null;
		////ra.showMessage("wrnclt0");
	
	
		
	}
	
	
	
	public void selection(){
		//F8
		if (isTableEmpty("tblCollHfPriorListRba")) {
			System.out.println("CollHfPriorListRba - selection 1");
			ra.showMessage("wrn299");
			return;
		}
		
		if (!ra.isLDBExists("CollComponentItemLDB")) {
			System.out.println("CollHfPriorListRba - selection 2");
	 		ra.createLDB("CollComponentItemLDB");
	 	}
		java.math.BigDecimal cusId = null;
		String registerNo = null;
		String useOwnerName = null;
		
		
		
		java.math.BigDecimal coll_hf_prior_id = null;
		String  coll_type_name = null; 
		String  col_num = null;
		java.math.BigDecimal hf_hfc_id = null;
		String  HfHfcSCD = null;
		java.math.BigDecimal hf_amount  = null;
		java.math.BigDecimal hf_cur_id  = null;
		String  hfCurIdCodeC = null;
		java.sql.Date hf_date_hfc_from = null;
		java.sql.Date hf_date_hfc_until = null;
		String hf_priority = null;
		java.math.BigDecimal hf_avail_amo = null;
		String acc_prior = null;
		
		java.math.BigDecimal coll_type_id      = null;        
		java.math.BigDecimal hf_coll_head_id   = null;        
		java.math.BigDecimal amount_ref        = null;        
		java.math.BigDecimal cur_id_ref        = null;        
		java.math.BigDecimal exc_rat_ref       = null;        
		java.sql.Date exc_rat_ref_date  = null;               
		java.math.BigDecimal hf_draw_amo       = null;        
		java.math.BigDecimal hf_draw_amo_ref   = null;        
		java.math.BigDecimal avail_amo_ref     = null;        
		java.sql.Date hf_avail_dat      = null;               
		java.math.BigDecimal draw_bamo         = null;        
		java.math.BigDecimal avail_bamo        = null;        
		java.math.BigDecimal draw_bamo_ref     = null;        
		java.math.BigDecimal avail_bamo_ref    = null;        
		java.sql.Date avail_bdat        = null;               
		java.sql.Date val_date_turn     = null;               
		java.sql.Date val_date_proc     = null;               
		String hf_status         = null;                      
		String hf_spec_stat      = null;                      
		java.sql.Date hf_date_from      = null;               
		java.sql.Date hf_date_until     = null;               
		String coll_type_code = null;                                          
		String hfCurIdRefCodeC = null;

			
	
		
		
		
		//CollComponentItemLDB
		//////////////////////////////////
		//Null all elements on CollComponentItemLDB
		resetCollComponentItemLDB();
		//Null all elements on CollateralDialogLDB refering on CollComponentItem
		resetCollateralDialogLDBCollComponentItem();
				
		
		

		
		
		//Ako je prazna tablica loan
		hr.vestigo.framework.common.TableData tdCollLoanIsEmpty = null;
		if(ra.getAttribute("tblCollateralDialogLoanComp") == null){
			System.out.println("CollHfPriorListRba - selection 3");
			ra.showMessage("wrnclt28");
			return;
		}else{
			System.out.println("CollHfPriorListRba - selection 4");
			tdCollLoanIsEmpty = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
			if(tdCollLoanIsEmpty.getData().size() == 0){
				System.out.println("CollHfPriorListRba - selection 5");
				ra.showMessage("wrnclt28");
				tdCollLoanIsEmpty = null;
				return;
			}
		}
		
		
		
		//Provjera moze li se dodati jos jedna komponenta kolaterala pocetak
		//if velicina Loan = Coll + 1
			hr.vestigo.framework.common.TableData tdLoanSelectionMore = null;
			hr.vestigo.framework.common.TableData tdCollSelectionMore = null;
			int velicinaLoan = 0;
			int velicinaColl = 0;
			tdLoanSelectionMore = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
			tdCollSelectionMore = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");
			
			
			
			if(tdLoanSelectionMore != null){
				velicinaLoan = tdLoanSelectionMore.getData().size();
				System.out.println("CollHfPriorListRba - selection 6");
			}
			if(tdCollSelectionMore != null){
				System.out.println("CollHfPriorListRba - selection 7");
				velicinaColl = tdCollSelectionMore.getData().size();
				velicinaColl = velicinaColl + 1;
			}else{
				System.out.println("CollHfPriorListRba - selection 8");
				velicinaColl = 1;
			}
			
			if(velicinaLoan == velicinaColl){
				tdLoanSelectionMore = null;
				tdCollSelectionMore = null;
				System.out.println("CollHfPriorListRba - selection 9  velicinaLoan velicinaColl  " + velicinaLoan + " " + velicinaColl );
			}else{
				tdLoanSelectionMore = null;
				tdCollSelectionMore = null;
				System.out.println("CollHfPriorListRba - selection 10  velicinaLoan velicinaColl  " + velicinaLoan + " " + velicinaColl );
				ra.showMessage("wrnclt24");
				return;
			}
			//Provjera moze li se dodati jos jedna komponenta kolaterala kraj
			
		
		
		
		//CollHfPriorListRbaLDB
		//
		//CollHfPriorListRba_CUS_ID
		//CollHfPriorListRba_txtColaCusIdUseRegisterNo
		//CollHfPriorListRba_txtColaCusIdUseOwnerName
		//CollHfPriorListRba_COL_TYPE_ID
		//CollHfPriorListRba_txtCollTypeCode
		//CollHfPriorListRba_txtCollTypeName
		//CollHfPriorListRba_COL_HEA_ID
		//CollHfPriorListRba_txtColNum
		//tblCollHfPriorListRba
		
				
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("CollHfPriorListRbaLDB", "tblCollHfPriorListRba");
		
	
		java.util.Vector vidljiviVector = (java.util.Vector) td.getSelectedRowData();
		java.util.Vector nevidljiviVector = (java.util.Vector) td.getSelectedRowUnique();
		
		//Dohvat vidljivih
		System.out.println( "PRIKAZ VIDLJIVIH ZA JEDNU HIPOTEKU - POCETAK");
		coll_type_name = (String) vidljiviVector.get(0);
		System.out.println( "TIP coll_type_name" + coll_type_name );
		
		col_num = (String) vidljiviVector.get(1);
		System.out.println( "PARTIJA  col_num" + col_num );
		
		
		HfHfcSCD =(String) vidljiviVector.get(2);
		System.out.println( "VRSTA   HfHfcSCD" + HfHfcSCD );
		
		hf_amount = (java.math.BigDecimal) vidljiviVector.get(3);
		System.out.println( "UKUPNI IZNOS hf_amount" + hf_amount );
		
		hfCurIdCodeC = (String) vidljiviVector.get(4);
		System.out.println( " VALUTA    hfCurIdCodeC" + hfCurIdCodeC );
		
		hf_date_hfc_from = (java.sql.Date)vidljiviVector.get(5);
		hf_date_hfc_until = (java.sql.Date) vidljiviVector.get(6);
		hf_priority = (String)vidljiviVector.get(7);
		
		hf_avail_amo = (java.math.BigDecimal)vidljiviVector.get(8);
		System.out.println( "hf_avail_amo" + hf_avail_amo );
		
		acc_prior = (String)vidljiviVector.get(9);
		
		
		
		
		
		
		
		System.out.println( "PRIKAZ VIDLJIVIH ZA JEDNU HIPOTEKU - KRAJ");
		System.out.println( " ");
		System.out.println( " ");
		
		
		
		
		//Dohvat nevidljivih
		coll_hf_prior_id = (java.math.BigDecimal) nevidljiviVector.get(0);
		hf_hfc_id = (java.math.BigDecimal) nevidljiviVector.get(1);
		hf_cur_id = (java.math.BigDecimal) nevidljiviVector.get(2);
		coll_type_id  = (java.math.BigDecimal) nevidljiviVector.get(3);    
		hf_coll_head_id = (java.math.BigDecimal) nevidljiviVector.get(4);  
		amount_ref  = (java.math.BigDecimal) nevidljiviVector.get(5);      
		cur_id_ref = (java.math.BigDecimal) nevidljiviVector.get(6);       
		exc_rat_ref = (java.math.BigDecimal) nevidljiviVector.get(7);      
		exc_rat_ref_date = (java.sql.Date) nevidljiviVector.get(8); 
		hf_draw_amo = (java.math.BigDecimal) nevidljiviVector.get(9);      
		hf_draw_amo_ref = (java.math.BigDecimal) nevidljiviVector.get(10);  
		avail_amo_ref= (java.math.BigDecimal) nevidljiviVector.get(11);     
		hf_avail_dat = (java.sql.Date) nevidljiviVector.get(12);       
		draw_bamo = (java.math.BigDecimal) nevidljiviVector.get(13);        
		avail_bamo= (java.math.BigDecimal) nevidljiviVector.get(14);        
		draw_bamo_ref = (java.math.BigDecimal) nevidljiviVector.get(15);    
		avail_bamo_ref = (java.math.BigDecimal) nevidljiviVector.get(16);   
		avail_bdat  = (java.sql.Date) nevidljiviVector.get(17);      
		val_date_turn = (java.sql.Date) nevidljiviVector.get(18);    
		val_date_proc = (java.sql.Date) nevidljiviVector.get(19);    
		hf_status   = (String) nevidljiviVector.get(20);      
		hf_spec_stat   = (String) nevidljiviVector.get(21);     
		hf_date_from = (java.sql.Date) nevidljiviVector.get(22);     
		hf_date_until = (java.sql.Date) nevidljiviVector.get(23);    
		coll_type_code =  (String) nevidljiviVector.get(24);   
		hfCurIdRefCodeC = (String) nevidljiviVector.get(25); 
		
		
		System.out.println( "PRIKAZ NEVIDLJIVIH ZA JEDNU HIPOTEKU - POCETAK");
		System.out.println( "coll_hf_prior_id" + coll_hf_prior_id );
		System.out.println( "hf_hfc_id" + hf_hfc_id );
		System.out.println( "hf_cur_id" + hf_cur_id );
		System.out.println( "coll_type_id" + coll_type_id );
		
		System.out.println( "hf_coll_head_id" + hf_coll_head_id );
		System.out.println( "amount_ref" + amount_ref );
		System.out.println( "cur_id_ref" + cur_id_ref );
		System.out.println( "exc_rat_ref" + exc_rat_ref );
		
		
		
		
		
		
		
		System.out.println( "hf_date_from" + hf_date_from );
		System.out.println( "hf_date_until" + hf_date_until );
		System.out.println( "coll_type_code" + coll_type_code );
		
		System.out.println( "PRIKAZ NEVIDLJIVIH ZA JEDNU HIPOTEKU - KRAJ");
		
		                                                                                    
		cusId = (java.math.BigDecimal) ra.getAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_CUS_ID") ;
		registerNo = (String)  ra.getAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_txtColaCusIdUseRegisterNo");
		useOwnerName = (String)  ra.getAttribute("CollHfPriorListRbaLDB", "CollHfPriorListRba_txtColaCusIdUseOwnerName");
		
		
		
		
		
		//POSTAVI NA CollateralDialogLDB elemente jedne hipoteke
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COL_TYPE_ID" , coll_type_id);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCollTypeCode", coll_type_code);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCollTypeName", coll_type_name);          
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COL_HEA_ID", hf_coll_head_id);               
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtColNum", col_num);                
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COMP_HF_PRIOR_ID", coll_hf_prior_id);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COMP_CUR_ID", hf_cur_id);              
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCompCurIdCodeC", hfCurIdCodeC);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_CUR_ID_REF", cur_id_ref);               
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCurIdRefCodeC", hfCurIdRefCodeC);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRef", exc_rat_ref);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRefDate", exc_rat_ref_date);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfAmount", hf_amount);               
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfHfcId", hf_hfc_id);
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfHfcSCD", HfHfcSCD);
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfPriority", hf_priority);
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtAccPrior", acc_prior);
		
		
		
		
		//POSTAVI NA CollComponentItemLDB elemenet jedne hipoteke koji se prenose na komponentu
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUS_ID",cusId);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColaCusIdUseRegisterNo",registerNo);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColaCusIdUseOwnerName",useOwnerName);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COL_TYPE_ID",coll_type_id); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCollTypeCode",coll_type_code); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCollTypeName",coll_type_name); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COL_HEA_ID",hf_coll_head_id); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColNum",col_num); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_HF_PRIOR_ID",coll_hf_prior_id);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_CUR_ID",hf_cur_id);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCurIdCodeC",hfCurIdCodeC);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUR_ID_REF",cur_id_ref);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCurIdRefCodeC",hfCurIdRefCodeC);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfAmount", hf_amount);                 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfHfcId", hf_hfc_id);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfHfcSCD", HfHfcSCD);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfPriority", hf_priority);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtAccPrior", acc_prior);
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateUntil", hf_date_hfc_until);
		    
		
		System.out.println ( "hf_date_hfc_until  " +hf_date_hfc_until);
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompDateUntil", hf_date_hfc_until);   
		
		
		                                           
		   
		System.out.println ( "ULAZIM NA EKRAN CollComponentItem");
					
		ra.loadScreen("CollComponentItem","scr_choose"); 
				
		
			             
		
		
		
		
	}//details
	
	
	
	
	public void resetCollComponentItemLDB(){
		

		ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUS_ID",null);                              
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColaCusIdUseRegisterNo",null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColaCusIdUseOwnerName",null);  
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COL_TYPE_ID",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCollTypeCode",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCollTypeName",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COL_HEA_ID",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColNum",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_ID", null);                    
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_HF_PRIOR_ID", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmo", null);              
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_CUR_ID", null);                
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCurIdCodeC", null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate", null);             
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmoRef", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUR_ID_REF", null);                 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCurIdRefCodeC", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRef", null);               
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRefDate", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovBamo", null);             
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovBdate"  , null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovBamoRef", null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateFrom", null);            
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateUntil", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompStatus", null);              
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompSpecStatus", null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_USE_OPEN_ID", null);                
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUseOpenIdLogin", null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUseOpenIdName", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtOpeningTs", null);               
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_USE_ID", null);                     
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUseIdLogin", null);              
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUseIdName", null);               
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUserLock", null);                
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtSerNoLoanComp", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfAmount", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfHfcId", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfHfcSCD", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfPriority", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtAccPrior", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate1", null);                 
		
		ra.setAttribute("CollComponentItemLDB","curSumTotalFromCollComponent", null);                 
		
		ra.setAttribute("CollComponentItemLDB","curAvailableCollComponent", null);                 
		ra.setAttribute("CollComponentItemLDB","curSumTotalCollComp", null);                 
		ra.setAttribute("CollComponentItemLDB","curSumAllCollComptbl", null);                 

	}//resetCollComponentItemLDB
	
	
	public void resetCollateralDialogLDBCollComponentItem(){
//		CollateralDialogLDB
		////////////////////////////////////////////
		//CollComponentItemD_COL_TYPE_ID
		//CollComponentItemD_txtCollTypeCode
		//CollComponentItemD_txtCollTypeName
		//CollComponentItemD_COL_HEA_ID
		//CollComponentItemD_txtColNum
		//CollComponentItemD_COMP_ID
		//CollComponentItemD_COMP_HF_PRIOR_ID
		//CollComponentItemD_txtCompCovAmo
		//CollComponentItemD_COMP_CUR_ID
		//CollComponentItemD_txtCompCurIdCodeC
		//CollComponentItemD_txtCompCovDate
		//CollComponentItemD_txtCompCovAmoRef
		//CollComponentItemD_CUR_ID_REF
		//CollComponentItemD_txtCurIdRefCodeC
		//CollComponentItemD_txtExcRatRef
		//CollComponentItemD_txtExcRatRefDate
		//CollComponentItemD_txtCompCovBamo
		//CollComponentItemD_txtCompCovBdate
		//CollComponentItemD_txtCompCovBamoRef
		//CollComponentItemD_txtCompDateFrom
		//CollComponentItemD_txtCompDateUntil
		//CollComponentItemD_txtCompStatus
		//CollComponentItemD_txtCompSpecStatus
		//CollComponentItemD_USE_OPEN_ID
		//CollComponentItemD_txtUseOpenIdLogin
		//CollComponentItemD_txtUseOpenIdName
		//CollComponentItemD_txtOpeningTs
		//CollComponentItemD_USE_ID
		//CollComponentItemD_txtUseIdLogin
		//CollComponentItemD_txtUseIdName
		//CollComponentItemD_txtUserLock
		//CollComponentItemD_txtSerNoLoanComp
		//CollComponentItemD_txtBalance
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COL_TYPE_ID" , null);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCollTypeCode", null);          
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCollTypeName", null);          
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COL_HEA_ID", null);               
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtColNum", null);                
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_COMP_ID", null);                  
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COMP_HF_PRIOR_ID", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovAmo", null);            
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COMP_CUR_ID", null);              
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCompCurIdCodeC", null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovDate", null);           
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovAmoRef", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_CUR_ID_REF", null);               
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCurIdRefCodeC", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRef", null);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRefDate", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovBamo", null);           
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovBdate"  , null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovBamoRef", null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompDateFrom", null);          
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompDateUntil", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompStatus", null);            
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompSpecStatus", null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_USE_OPEN_ID", null);              
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUseOpenIdLogin", null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUseOpenIdName", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtOpeningTs", null);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_USE_ID", null);                   
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUseIdLogin", null);            
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUseIdName", null);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUserLock", null);              
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtSerNoLoanComp", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfAmount", null);  
		
		
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfHfcId", null);                 
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfHfcSCD", null);                 
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfPriority", null);                 
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtAccPrior", null);                 
		   
		
	
	
	}//resetCollateralDialogLDBCollComponentItem
	
	
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
	
}
