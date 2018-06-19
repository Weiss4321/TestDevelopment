/*
 * Created on 2006.05.09
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
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;

import hr.vestigo.framework.util.CharUtil;

/**
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoanBeneficiary extends Handler {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/LoanBeneficiary.java,v 1.34 2014/10/07 10:54:19 hrazst Exp $";
	
	public LoanBeneficiary(ResourceAccessor ra) {
		super(ra);
	}
	    
	public void LoanBeneficiary_SE(){//prvo sto se poziva
		if (!(ra.isLDBExists("LBenLDB"))) {
			ra.createLDB("LBenLDB");
		}	

		if(ra.getScreenContext().compareTo("base") == 0) {
		    System.out.println("USAO OVDJEEEEEE !!!!!!!!!!!!! ZVONE");
			//Dizanje sa hipoteke
			TableData td = (TableData) ra.getAttribute("CollHfPriorLDB", "tblCollHfPrior");
			Vector hiddenVectorRow = (Vector) td.getSelectedRowUnique();
			Vector visibleVectorRow = td.getSelectedRowData();
			
			BigDecimal collHfPriorId = null;
			BigDecimal howCover = null;
			collHfPriorId = (BigDecimal) hiddenVectorRow.elementAt(0);
			howCover = (BigDecimal) hiddenVectorRow.elementAt(3);
			
			String agreementFlag = null;
			agreementFlag = (String) hiddenVectorRow.elementAt(5);
			
			BigDecimal collHeaId = null;
			collHeaId = (BigDecimal)ra.getAttribute("CollHfPriorLDB","CollHfPrior_HF_COLL_HEAD_ID");
			
			ra.setAttribute("LBenLDB","LBen_col_hea_id_check",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HEA_ID",null);
			ra.setAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID",collHfPriorId);
			ra.setAttribute("LBenLDB","LBen_HOW_COWER",howCover);
			ra.setAttribute("LBenLDB","LBen_fra_agr_id",null);			
			ra.setAttribute("LBenLDB","LBen_AgreementFlag", agreementFlag);
			
			TableData tdLB = (TableData) ra.getAttribute("tblLoanBeneficiary");
	        if (tdLB == null) ra.createActionListSession("tblLoanBeneficiary", false);
	        tdLB = null;
	        System.out.println("OVDJEEEEEE SSAAAAAAAAAAMMMMMMMMMMMMMMMMMMMM");
			ra.refreshActionList("tblLoanBeneficiary");
		}  		 
		if(ra.getScreenContext().compareTo("detail") == 0) {
		    System.out.println("USAO OVDJEEEEEE !!!!!!!!!!!!! ZVONE 1");
			//Dizanje sa hipoteke
			TableData td = (TableData) ra.getAttribute("CollHfPriorLDB", "tblCollHfPrior");
			Vector hiddenVectorRow = (Vector) td.getSelectedRowUnique();
			Vector visibleVectorRow = td.getSelectedRowData();
			java.math.BigDecimal collHfPriorId = null;
			
			BigDecimal howCover = null;
			
			collHfPriorId = (BigDecimal) hiddenVectorRow.elementAt(0);
			howCover = (BigDecimal) hiddenVectorRow.elementAt(3);

			String agreementFlag = null;
			agreementFlag = (String) hiddenVectorRow.elementAt(5);
			
			BigDecimal collHeaId = null;
			collHeaId = (BigDecimal)ra.getAttribute("CollHfPriorLDB","CollHfPrior_HF_COLL_HEAD_ID");
			ra.setAttribute("LBenLDB","LBen_col_hea_id_check",collHeaId);			
			ra.setAttribute("LBenLDB","LBen_COLL_HEA_ID",null);
			ra.setAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID",collHfPriorId);
			ra.setAttribute("LBenLDB","LBen_HOW_COWER",howCover);
			ra.setAttribute("LBenLDB","LBen_fra_agr_id",null);				
			ra.setAttribute("LBenLDB","LBen_AgreementFlag", agreementFlag);			
			TableData tdLB = (TableData) ra.getAttribute("tblLoanBeneficiary");
	        if (tdLB == null)
	        	ra.createActionListSession("tblLoanBeneficiary", false);
	        tdLB = null;
			ra.refreshActionList("tblLoanBeneficiary");
		}
		if((ra.getScreenContext().compareTo("scr_bilofexch") == 0) ){
			//Dizanje sa mjenice
			java.math.BigDecimal collHeaId = null;
			collHeaId = (java.math.BigDecimal)ra.getAttribute("CollBoeDialogLDB","CollBoeDialog_COL_HEA_ID");
			ra.setAttribute("LBenLDB","LBen_col_hea_id_check",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HEA_ID",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID",null);
			ra.setAttribute("LBenLDB","LBen_HOW_COWER",null);
			ra.setAttribute("LBenLDB","LBen_fra_agr_id",null);	
			ra.setAttribute("LBenLDB","LBen_AgreementFlag", null);
			hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblLoanBeneficiary");
	        if (tdLB == null)
	        	ra.createActionListSession("tblLoanBeneficiary", false);
	        tdLB = null;
			ra.refreshActionList("tblLoanBeneficiary");
		}
		if(ra.getScreenContext().compareTo("detail_bxc") == 0) {
		    //Dizanje sa mjenice
			BigDecimal collHeaId = null;
			collHeaId = (java.math.BigDecimal)ra.getAttribute("CollBoeDialogLDB","CollBoeDialog_COL_HEA_ID");
			ra.setAttribute("LBenLDB","LBen_col_hea_id_check",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HEA_ID",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID",null);
			ra.setAttribute("LBenLDB","LBen_HOW_COWER",null);
			ra.setAttribute("LBenLDB","LBen_fra_agr_id",null);	
			ra.setAttribute("LBenLDB","LBen_AgreementFlag", null);
			hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblLoanBeneficiary");
	        if (tdLB == null)
	        	ra.createActionListSession("tblLoanBeneficiary", false);
	        tdLB = null;
			ra.refreshActionList("tblLoanBeneficiary");
		}		
		if(ra.getScreenContext().compareTo("scr_loanstock") == 0) {
			//Dizanje sa zaduznice
			BigDecimal collHeaId = null;
			collHeaId = (java.math.BigDecimal)ra.getAttribute("CollHeadLDB","COL_HEA_ID");
			ra.setAttribute("LBenLDB","LBen_col_hea_id_check",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HEA_ID",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID",null);
			ra.setAttribute("LBenLDB","LBen_HOW_COWER",null);
			ra.setAttribute("LBenLDB","LBen_fra_agr_id",null);	
			ra.setAttribute("LBenLDB","LBen_AgreementFlag", null);
			hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblLoanBeneficiary");
	        if (tdLB == null)
	        	ra.createActionListSession("tblLoanBeneficiary", false);
	        tdLB = null;
			ra.refreshActionList("tblLoanBeneficiary");
		}
		if(ra.getScreenContext().compareTo("detail_loa") == 0) {
			//Dizanje sa zaduznice
			java.math.BigDecimal collHeaId = null;
			collHeaId = (java.math.BigDecimal)ra.getAttribute("CollHeadLDB","COL_HEA_ID");
			ra.setAttribute("LBenLDB","LBen_col_hea_id_check",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HEA_ID",collHeaId);
			ra.setAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID",null);
			ra.setAttribute("LBenLDB","LBen_HOW_COWER",null);
			ra.setAttribute("LBenLDB","LBen_fra_agr_id",null);	
			ra.setAttribute("LBenLDB","LBen_AgreementFlag", null);
			hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblLoanBeneficiary");
	        if (tdLB == null)
	        	ra.createActionListSession("tblLoanBeneficiary", false);
	        tdLB = null;
			ra.refreshActionList("tblLoanBeneficiary");
		}		
		if(ra.getScreenContext().compareTo("scr_agreement") == 0) {
			//Dizanje sa okvirnog sporazuma
			BigDecimal fra_agr_id = null;
			fra_agr_id = (BigDecimal)ra.getAttribute("AgreementListLDB","w_fra_agr_id");
			ra.setAttribute("LBenLDB","LBen_col_hea_id_check",null);
			ra.setAttribute("LBenLDB","LBen_COLL_HEA_ID",null);
			ra.setAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID",null);
			ra.setAttribute("LBenLDB","LBen_HOW_COWER",null);
			ra.setAttribute("LBenLDB","LBen_fra_agr_id",fra_agr_id);
			ra.setAttribute("LBenLDB","LBen_AgreementFlag", null);
			hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblLoanBeneficiary");
	        if (tdLB == null)
	        	ra.createActionListSession("tblLoanBeneficiary", false);
	        tdLB = null;
			ra.refreshActionList("tblLoanBeneficiary");
		}		
		
		if(ra.getScreenContext().compareTo("detail_agr") == 0) {
			//Dizanje sa okvirnog sporazuma
			BigDecimal fra_agr_id = null;
			fra_agr_id = (BigDecimal)ra.getAttribute("AgreementListLDB","w_fra_agr_id");
			ra.setAttribute("LBenLDB","LBen_col_hea_id_check",null);
			ra.setAttribute("LBenLDB","LBen_COLL_HEA_ID",null);
			ra.setAttribute("LBenLDB","LBen_COLL_HF_PRIOR_ID",null);
			ra.setAttribute("LBenLDB","LBen_HOW_COWER",null);
			ra.setAttribute("LBenLDB","LBen_fra_agr_id",fra_agr_id);
			ra.setAttribute("LBenLDB","LBen_AgreementFlag", null);
			hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblLoanBeneficiary");
	        if (tdLB == null)
	        	ra.createActionListSession("tblLoanBeneficiary", false);
	        tdLB = null;
			ra.refreshActionList("tblLoanBeneficiary");
		}
	}//LoanBeneficiary_SE
	
	 
	
	public void add(){
		if(ra.getScreenContext().compareTo("base") == 0) {
			ra.loadScreen("LoanBeneficiaryDialog","scr_insert");	
			//Plasmani se vezu za hipoteku
		}
		if(ra.getScreenContext().compareTo("scr_bilofexch") == 0) {
			ra.loadScreen("LoanBeneficiaryDialog","scr_insert");	
			//Plasmani se vezu za coll_head
		}
		if(ra.getScreenContext().compareTo("scr_loanstock") == 0) {
			ra.loadScreen("LoanBeneficiaryDialog","scr_insert");	
			//Plasmani se vezu za coll_head
		}
		if(ra.getScreenContext().compareTo("scr_agreement") == 0) {
			ra.loadScreen("LoanBeneficiaryDialog","scr_insert");	
			//Plasmani se vezu za okvirni sporazum
		}		
		//ra.showMessage("wrnclt0");//nije implementirano: poruka
	}  
	public void details(){
		if (isTableEmpty("tblLoanBeneficiary")) {
			ra.showMessage("wrn299");
			return;
		}  
// ako sam na akcijskoj listi treba otvoriti u konteksu za potvrdu kreditne administracije, paziti da nisam u okvirnom sporazumu
		
		if (!(ra.isLDBExists("ColWorkListLDB"))) {
			ra.loadScreen("LoanBeneficiaryDialog","scr_detail");	
		} else {
			String lista = (String) ra.getAttribute("ColWorkListLDB", "proc_status");
			if (lista.equalsIgnoreCase("3") || lista.equalsIgnoreCase("K")) {
				ra.loadScreen("LoanBeneficiaryDialog","scr_kred_admin");
			}else{
				ra.loadScreen("LoanBeneficiaryDialog","scr_detail");			
			}
		} 
/*		String lista = (String) ra.getAttribute("ColWorkListLDB", "proc_status");
		
		if (lista.equalsIgnoreCase("3")) {
			ra.loadScreen("LoanBeneficiaryDialog","scr_kred_admin");
		}else{
			ra.loadScreen("LoanBeneficiaryDialog","scr_detail");			
		}*/
		//ra.showMessage("wrnclt0");
	}
	public void action(){
		if (isTableEmpty("tblLoanBeneficiary")) {
			ra.showMessage("wrn299");
			return;
		}
		
		
		ra.loadScreen("LoanBeneficiaryDialog","scr_update"); 
		//try {
		//	 ra.executeTransaction();
		//} catch (VestigoTMException vtme) {
		//	 if (vtme.getMessageID() != null)
		//		 ra.showMessage(vtme.getMessageID());
		//} 
		
		//ra.showMessage("wrnclt0");
	}
	public void refresh(){
		ra.refreshActionList("tblLoanBeneficiary");
		//ra.showMessage("wrnclt0");
	}

	//FBPr200014088 Dodana akcija Sudužnici na ekran 'partija plasmana'
	public void coborrower(){
	    if (isTableEmpty("tblLoanBeneficiary")) {
            ra.showMessage("wrn299");
            return;
        }
	    
	    String ctx = ra.getScreenContext();
        
	    if(ctx.equals("base") || ctx.equals("scr_bilofexch") || ctx.equals("scr_loanstock") || ctx.equals("scr_agreement")){
	        ra.loadScreen("CoBorrowerList","ctx_change");
	    }else
	        ra.loadScreen("CoBorrowerList","ctx_view");
  	}

	
	public boolean LBen_txtRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {
		
		String ldbName = "LBenLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute(ldbName,"LBen_txtRegNo",null);
			ra.setAttribute(ldbName,"LBen_txtName",null);
			ra.setAttribute(ldbName,"LBen_CUS_ID",null);
			//ra.setAttribute(ldbName,"CUS_ID",null);//element LDB-a imenom 
			//ra.setAttribute(ldbName,"LBen_txtAccNo",null);	//LBen_txtAccNo=147223
			return true;
		}
		
		if (ra.getCursorPosition().equals("LBen_txtName")) {
			ra.setAttribute(ldbName, "LBen_txtRegNo", "");
		} else if (ra.getCursorPosition().equals("LBen_txtRegNo")) {
			ra.setAttribute(ldbName, "LBen_txtName", "");
			//ra.setCursorPosition(2);
		}
		
		
		 
		 
		String d_name = "";
		if (ra.getAttribute(ldbName, "LBen_txtName") != null){
			d_name = (String) ra.getAttribute(ldbName, "LBen_txtName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "LBen_txtRegNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "LBen_txtRegNo");
		}
		
		if ((d_name.length() < 4) && (d_register_no.length() < 4)) {	//mora upisat minimalno 4 znaka
			ra.showMessage("wrn366");
			return false;
		}

		
		
		
		//JE LI zvjezdica na pravom mjestu kod register_no
		if (CharUtil.isAsteriskWrong(d_register_no)) {
			ra.showMessage("wrn367");
			return false;
		}
		
		
		 if (ra.getCursorPosition().equals("LBen_txtRegNo")) 
			ra.setCursorPosition(2);//pomakni se za 2 textbox-a naprijed posto su ovaj i iduci popunjeni (valjda jesu)
					
		//LDB od lookUp-a koji ima ove elemente, no koristi se ono sto je potrebno u ovom slucaju 3 elementa REGISTER_NO, NAME i CUS_ID
		if (ra.isLDBExists("CustomerAllLookUpLDB")) {//ostaje mene ovo ne zanima...
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "LBen_txtRegNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "LBen_txtName"));

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

		ra.setAttribute(ldbName, "LBen_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute(ldbName, "LBen_txtRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute(ldbName, "LBen_txtName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		
		java.math.BigDecimal lbenCusId = (java.math.BigDecimal) ra.getAttribute("LBenLDB","LBen_CUS_ID");
		String accNo = (String) ra.getAttribute("LBenLDB", "LBen_txtAccNo");
		
		if((accNo != null) || (lbenCusId != null)){                                                                                                         
//			System.out.println("Poziv refresha akcijske liste - Korisnik plasmana");
			//ra.refreshActionList("tblLoanBeneficiary");	                                                                                       
		} 
		
		//
		
		ldbName = null;
		lbenCusId = null;
		accNo = null;
		return true;
	} //LBen_txtRegNo_FV
	
	
	 
	
	public boolean LBen_txtAccNo_FV(){
		String accNo = null;
		
		
		accNo = (String) ra.getAttribute("LBenLDB", "LBen_txtAccNo");
		if(accNo != null){
			if(accNo.trim().length()== 0){
				accNo = null;
			}
		}
		
		java.math.BigDecimal lbenCusId = (java.math.BigDecimal) ra.getAttribute("LBenLDB","LBen_CUS_ID");
		
		
		if((accNo != null) || (lbenCusId != null)){                                                                                                         
//			System.out.println("Poziv refresha akcijske liste - Partija plasmana");
			//ra.refreshActionList("tblLoanBeneficiary");	                                                                                       
		}     
		
		lbenCusId = null;
		accNo = null;
		
		return true;
	}//LBen_txtAccNo_FV
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
    
    public void exit() {
//      vracam se iz unosa ili promjene   
//      napuniti polja za iznos raspolozive i ponderirane
//      ako sam u DS-u - ne radi se nista         
        BigDecimal col_cat_id = null;
        if (!(ra.isLDBExists("ColWorkListLDB"))) {   
            
        } else {
            col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
            boolean set_eligibility = false;
            String nd_elig = (String) ra.getAttribute("LBenLDB","Kol_ND_lb");
            
            if (nd_elig == null || nd_elig.trim().equals(""))
                set_eligibility = false;
            else
                set_eligibility = true;           
            if(ra.getScreenContext().equalsIgnoreCase("scr_loanstock")){
                  
                if (ra.isLDBExists("CollHeadLDB") ) {
                    ra.setAttribute("CollHeadLDB","Coll_txtAcouValue",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtWeighValue"));
                    ra.setAttribute("CollHeadLDB","Coll_txtAcouDate",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtWeighDate"));  
                    ra.setAttribute("CollHeadLDB","Coll_txtAvailValue",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtAvailValue"));
                    ra.setAttribute("CollHeadLDB","Coll_txtAvailDate",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtAvailDate"));  
                    ra.setAttribute("CollHeadLDB","Coll_txtSumPartVal",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtSumPartVal"));
                    ra.setAttribute("CollHeadLDB","Coll_txtSumPartDat",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtSumPartDat"));
                     
                    ra.setAttribute("CollHeadLDB","Coll_txtCollMvpPonderMin",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonderMin"));  
                    ra.setAttribute("CollHeadLDB","Coll_txtCollMvpPonder",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonder"));
                    ra.setAttribute("CollHeadLDB","Coll_txtCollMvpPonderMax",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonderMax"));
                }             
    // puniti prihvatljivosti samo ako nisu prazni..
    
                if (col_cat_id.compareTo(new BigDecimal("613223")) == 0 || col_cat_id.compareTo(new BigDecimal("619223")) == 0 ||
                    col_cat_id.compareTo(new BigDecimal("622223")) == 0 || col_cat_id.compareTo(new BigDecimal("627223")) == 0 ||
                    col_cat_id.compareTo(new BigDecimal("629223")) == 0 ) {    
              // za VRP  - punim samo ND              
                    if (set_eligibility) {   
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligibility",ra.getAttribute("LBenLDB","Kol_ND_lb"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligDesc",ra.getAttribute("LBenLDB","Kol_ND_lb_dsc"));                    
                    }
                } else {
              // ostali-punim sve        
                    if (set_eligibility) {   
                        ra.setAttribute("CollHeadLDB", "Coll_txtEligibility",ra.getAttribute("LBenLDB","Kol_B2_lb"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtEligDesc",ra.getAttribute("LBenLDB","Kol_B2_lb_dsc"));
          
                        ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligibility",ra.getAttribute("LBenLDB","Kol_B2IRB_lb"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligDesc",ra.getAttribute("LBenLDB","Kol_B2IRB_lb_dsc")); 
          
                        ra.setAttribute("CollHeadLDB", "Coll_txtB1Eligibility",ra.getAttribute("LBenLDB","Kol_HNB_lb"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtB1EligDesc",ra.getAttribute("LBenLDB","Kol_HNB_lb_dsc"));
          
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligibility",ra.getAttribute("LBenLDB","Kol_ND_lb"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligDesc",ra.getAttribute("LBenLDB","Kol_ND_lb_dsc"));                   
                    }                
                }             
             }
        }
        
        ra.exitScreen();        
        return;
    }
      
	
}

