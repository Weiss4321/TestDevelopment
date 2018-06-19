package hr.vestigo.modules.collateral;
import java.math.BigDecimal;
import java.sql.Date;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralCmpUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;

/**            
 * @author HRAMKR       
 *   
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollSecPaper02 extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollSecPaper02.java,v 1.85 2018/01/09 10:38:23 hrazst Exp $";

    CollateralCmpUtil coll_cmp_util = null;	 
    CollateralUtil coll_util = null;
    LookUps coll_lookups = null;

    String CollHeadLDB = "CollHeadLDB";
    String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
    
    public CollSecPaper02(ResourceAccessor ra) {
        super(ra);
        coll_cmp_util = new CollateralCmpUtil(ra);
        coll_util = new CollateralUtil(ra);
        coll_lookups = new LookUps(ra);
    }

    public void CollLoanStockOF_SE() {
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                });  
    }   
    
    public void CollCesijaOF_SE() {

    }

    public void CollCashDepOF_SE () {

        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {	

            //			validacija rba prihvatljivosti	- dovoljno samo ovo zato sto se iz ove zove validacija ostalih			
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");				
            } 

        }
    } 

    public void CollInsPolOF_SE() {

        String low_eligibility = null;
        String upisana_hipoteka = null;  
        String kolateral_osiguran = null;

        String rba_eligibility = null;
        String b2_eligibility = null;
        String b1_eligibility = null;
        String b2irb_eligibility = null;

        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");   
        low_eligibility = (String)ra.getAttribute(CollHeadLDB, "KolLow_txtEligibility");
        upisana_hipoteka = (String) ra.getAttribute(CollHeadLDB,"Coll_txtRecLop");                
        kolateral_osiguran = (String) ra.getAttribute("ColWorkListLDB","ip_activ_and_pay"); 
        String crm_opinion = (String) ra.getAttribute(CollHeadLDB,"SPEC_STATUS");


        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) { 


            //          validacija rba        prihvatljivosti               
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");              
            }

        }                        		

    }    

    public void CollMovableOF_SE() {
        //		String knjizica = null;			
        String low_eligibility = null;
        String upisana_hipoteka = null;  
        String kolateral_osiguran = null;

        String rba_eligibility = null;
        String b2_eligibility = null;
        String b1_eligibility = null;
        String b2irb_eligibility = null;

        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");	
        low_eligibility = (String)ra.getAttribute(CollHeadLDB, "KolLow_txtEligibility");
        upisana_hipoteka = (String) ra.getAttribute(CollHeadLDB,"Coll_txtRecLop");				
        kolateral_osiguran = (String) ra.getAttribute("ColWorkListLDB","ip_activ_and_pay"); 
        String crm_opinion = (String) ra.getAttribute(CollHeadLDB,"SPEC_STATUS");

        // Milka, 13.01.2007 - RBA prihvatljivost postavlja se samo ako je prvi unos i polje nije popunjeno

        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {	
            //			validacija rba        prihvatljivosti	- dovoljno samo ovo zato sto se iz ove zove validacija svih drugih			
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");				
            }

        }		 		 		 
    } 
    public void  SupplyOF_SE() {
        //		String knjizica = null;			
        String low_eligibility = null;
        String upisana_hipoteka = null;  
        String kolateral_osiguran = null;

        String rba_eligibility = null;
        String b2_eligibility = null;
        String b1_eligibility = null;
        String b2irb_eligibility = null;

        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");	

        low_eligibility = (String)ra.getAttribute(CollHeadLDB, "KolLow_txtEligibility");
        upisana_hipoteka = (String) ra.getAttribute(CollHeadLDB,"Coll_txtRecLop");		
        kolateral_osiguran = (String) ra.getAttribute("ColWorkListLDB","ip_activ_and_pay"); 
        String crm_opinion = (String) ra.getAttribute(CollHeadLDB,"SPEC_STATUS");			

        // Milka, 13.01.2007 - RBA prihvatljivost postavlja se samo ako je prvi unos i polje nije popunjeno

        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {	

            //			validacija rba        prihvatljivosti				
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");				
            }

        }		 		 		  
    } 
    public void VesselDialogOF_SE() {
        String low_eligibility = null;
        String upisana_hipoteka = null;  
        String kolateral_osiguran = null;

        String rba_eligibility = null;
        String b2_eligibility = null;
        String b1_eligibility = null;
        String b2irb_eligibility = null;

        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");	

        low_eligibility = (String)ra.getAttribute(CollHeadLDB, "KolLow_txtEligibility");
        upisana_hipoteka = (String) ra.getAttribute(CollHeadLDB,"Coll_txtRecLop");		
        kolateral_osiguran = (String) ra.getAttribute("ColWorkListLDB","ip_activ_and_pay"); 
        String crm_opinion = (String) ra.getAttribute(CollHeadLDB,"SPEC_STATUS");

        // Milka, 13.01.2007 - RBA prihvatljivost postavlja se samo ako je prvi unos i polje nije popunjeno

        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {	
            //			validacija rba        prihvatljivosti				
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");				
            }

        }		 		 
    } 

    public void CollArtOF_SE() {

    }   

    public void CollPrecOF_SE() {

    }

    public void CollGuarantOF_SE() {

        //		 napuniti dinamicku labelu Kol_dlblAvailValueCur

        ra.setAttribute(CollHeadLDB, "Kol_dlblAvailValueCur",ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtGuarCur"));		
        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) { 

            //              validacija rba        prihvatljivosti               
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");              
            }

        }
    }

    public void  VehiDialog2_SE() {

        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {	
            // postaviti ctx za knjizicu vozila
            coll_util.setVehLicenceCtx(ra);
            coll_util.setVehLicenceReversCtx(ra);
            coll_util.setVehInsuranceCtx(ra);
            coll_util.setVehLicenceReturnCtx(ra);


        }

        if (ra.getScreenContext().equalsIgnoreCase("scr_change")) {
            // automatski punim da li je vracena knjizica vozila na N			

            ra.setAttribute(CollSecPaperDialogLDB, "Vehi_txtVehLicRetOwn", "N");				
            //			validacija 		
            ra.setCursorPosition("Vehi_txtVehLicRetOwn");
            ra.invokeValidation("Vehi_txtVehLicRetOwn");			
            //		automatski punim policu obaveznog osiguranja na D


            ra.setAttribute(CollSecPaperDialogLDB, "Vehi_txtVehInsurance", "D");	
            //		validacija 		
            ra.setCursorPosition("Vehi_txtVehInsurance");
            ra.invokeValidation("Vehi_txtVehInsurance");
            ra.setCursorPosition("Coll_txtComDoc");
        }
    }
    public void VehiDialogOF_SE () {

        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {	

            //			validacija rba        prihvatljivosti				
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");	
            }		

        }		 
    }   
    public void loan_ben() {

        BigDecimal collHeaId = (BigDecimal) ra.getAttribute(CollHeadLDB, "COL_HEA_ID");
        if(collHeaId == null){
            ra.showMessage("wrnclt103");

        }else{

            if (!(ra.isLDBExists("RealEstateDialogLDB"))) {
                ra.createLDB("RealEstateDialogLDB");
            }           
            //napuniti da li je upisano pravo banke sa ekrana
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtRecLop",ra.getAttribute(CollHeadLDB,"Coll_txtRecLop"));
            //Milka, 18.09.2009 - radi preracuna ponderirane i raspolozive vrijednsoti kod dodavanja i brisanja plasmana
            ra.setAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID",ra.getAttribute(CollHeadLDB, "COL_HEA_ID"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_REAL_EST_NM_CUR_ID",ra.getAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtNomiValu",ra.getAttribute(CollHeadLDB,"Coll_txtNomiValu"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonder",ra.getAttribute(CollHeadLDB,"Coll_txtCollMvpPonder"));

            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtWeighValue",ra.getAttribute(CollHeadLDB,"Coll_txtAcouValue"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtWeighDate",ra.getAttribute(CollHeadLDB,"Coll_txtAcouDate"));  
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtAvailValue",ra.getAttribute(CollHeadLDB,"Coll_txtAvailValue"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtAvailDate",ra.getAttribute(CollHeadLDB,"Coll_txtAvailDate"));  
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtSumPartVal",ra.getAttribute(CollHeadLDB,"Coll_txtSumPartVal"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtSumPartDat",ra.getAttribute(CollHeadLDB,"Coll_txtSumPartDat"));            

            if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut") || ra.getScreenContext().equalsIgnoreCase("scr_detail_co")) {
                ra.loadScreen("LoanBeneficiary","detail_loa");
            } else {
                ra.loadScreen("LoanBeneficiary","scr_loanstock");
            }
        }
    }       

    public void hypo_fid_con(){
        BigDecimal collHeaId = (BigDecimal) ra.getAttribute(CollHeadLDB, "COL_HEA_ID");
        if(collHeaId == null){
            ra.showMessage("wrnclt103");

        }else{   
            // za depozite I vozila 
            // i plovila 			  
            //			 ovo samo zbog upisa hipoteke 
            if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("CASH") || 
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI") || 
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("DION") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("OBVE") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJE") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZAPI") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJP")) {
                if (!(ra.isLDBExists("RealEstateDialogLDB"))) {
                    ra.createLDB("RealEstateDialogLDB");
                }		  	
                // napuniti da li je upisano pravo banke sa ekrana
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtRecLop",ra.getAttribute(CollHeadLDB,"Coll_txtRecLop"));
                // Milka, 18.09.2009 - radi preracuna ponderirane i raspolozive vrijednsoti kod dodavanja i brisanja plasmana
                ra.setAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID",ra.getAttribute(CollHeadLDB, "COL_HEA_ID"));
                ra.setAttribute("RealEstateDialogLDB","RealEstate_REAL_EST_NM_CUR_ID",ra.getAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID"));
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtNomiValu",ra.getAttribute(CollHeadLDB,"Coll_txtNomiValu"));
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonder",ra.getAttribute(CollHeadLDB,"Coll_txtCollMvpPonder"));

                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtWeighValue",ra.getAttribute(CollHeadLDB,"Coll_txtAcouValue"));
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtWeighDate",ra.getAttribute(CollHeadLDB,"Coll_txtAcouDate"));  
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtAvailValue",ra.getAttribute(CollHeadLDB,"Coll_txtAvailValue"));
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtAvailDate",ra.getAttribute(CollHeadLDB,"Coll_txtAvailDate"));  
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtSumPartVal",ra.getAttribute(CollHeadLDB,"Coll_txtSumPartVal"));
                ra.setAttribute("RealEstateDialogLDB","RealEstate_txtSumPartDat",ra.getAttribute(CollHeadLDB,"Coll_txtSumPartDat"));

            }   

            // za VOZILA i plovila
            if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("DION") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("OBVE") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJE") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZAPI") ||
                    ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJP")) {
                if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut") || 
                        ra.getScreenContext().equalsIgnoreCase("scr_detail_co") || ra.getScreenContext().equalsIgnoreCase("scr_owner_bank")) {
                    ra.loadScreen("CollHfPrior","detail_re");				
                } else {
                    ra.loadScreen("CollHfPrior","base_re");
                }					
            } else {
                // za CASH DEPOZIT-e
                if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut") || 
                        ra.getScreenContext().equalsIgnoreCase("scr_detail_co") || ra.getScreenContext().equalsIgnoreCase("scr_owner_bank")) {
                    ra.loadScreen("CollHfPrior","detail_ca");				
                } else {
                    ra.loadScreen("CollHfPrior","base_ca");
                }
            } 
        }
    } 

    public void owners(){
        BigDecimal collHeaId = (BigDecimal) ra.getAttribute(CollHeadLDB, "COL_HEA_ID");
        if(collHeaId == null){
            ra.showMessage("wrnclt103");

        }else{

            if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_aut") || ra.getScreenContext().equalsIgnoreCase("scr_detail_co")) {
                ra.loadScreen("CollOwners","detail_ca");
            } else {
                ra.loadScreen("CollOwners","scr_cashdep");
            }
        }		
    }    


    public boolean Coll_txtRecLop_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollHeadLDB, "Coll_txtRecLop", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");		

        request.addMapping(CollHeadLDB, "Coll_txtRecLop", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        } 

        return true;
    }


    public boolean Coll_txtDateRecLop_FV(){
        java.sql.Date datum = null;

        datum = (java.sql.Date) ra.getAttribute(CollHeadLDB, "Coll_txtDateRecLop");
        if(datum == null){
            ra.setAttribute(CollHeadLDB, "Coll_txtRecLop","N");
        }else{
            ra.setAttribute(CollHeadLDB, "Coll_txtRecLop","D");
        }

        return true;

    }//Coll_txtDateRecLop_FV


    public boolean Coll_txtEligibility_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollHeadLDB, new String[]{"Coll_txtEligibility", "Coll_txtEligDesc"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Coll_txtEligibility")){
            coll_util.clearField(CollHeadLDB, "Coll_txtEligDesc");
        }else if(ra.getCursorPosition().equals("Coll_txtEligDesc")){
            coll_util.clearField(CollHeadLDB, "Coll_txtEligibility");
        }
        
        ra.setAttribute(CollSecPaperDialogLDB, "SysCodId", "clt_eligib");
        if (ra.isLDBExists("RealEstateDialogLDB")) {
            ra.setAttribute("RealEstateDialogLDB", "SysCodId", "clt_eligib");           
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollHeadLDB, "Coll_txtEligibility", "Coll_txtEligDesc", "clt_eligib");
        if(value==null) return false;
        
        ra.setAttribute(CollHeadLDB, "Coll_txtEligibility", value.sysCodeValue);
        ra.setAttribute(CollHeadLDB, "Coll_txtEligDesc", value.sysCodeDesc);  
        return true;
    } // Coll_txtEligibility_FV	

    public boolean Coll_txtReprRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = CollSecPaperDialogLDB;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtReprRegNo",null);
            ra.setAttribute(ldbName,"Coll_txtReprData1",null);
            ra.setAttribute(ldbName,"repr_cus_id",null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtReprData1")) {
            ra.setAttribute(ldbName, "Coll_txtReprRegNo", "");
        } else if (ra.getCursorPosition().equals("Coll_txtReprRegNo")) {
            ra.setAttribute(ldbName, "Coll_txtReprData1", "");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtReprRegNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtReprRegNo");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "Coll_txtReprData1") != null){
            d_name = (String) ra.getAttribute(ldbName, "Coll_txtReprData1");
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


        if (ra.getCursorPosition().equals("Coll_txtReprRegNo")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtReprRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Coll_txtReprData1"));

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

        ra.setAttribute(ldbName, "repr_cus_id", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtReprRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtReprData1", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;


    }

    public boolean Coll_txtReprRole_FV(String ElName, Object ElValue, Integer LookUp) {


        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtReprRole", "");         
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtReprRoleName", "");         
            ra.setAttribute(CollSecPaperDialogLDB, "repr_func_id", null);                               
            return true;                                                                                 
        }
        if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
            ra.createLDB("UserCodeValueLookUpLDB");
        }				
        ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "repr_func_id"); 	
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtReprRoleName", ""); 	


        LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");  

        lookUpRequest.addMapping(CollSecPaperDialogLDB, "repr_func_id", "use_cod_val_id");						
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtReprRole", "use_code_value");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtReprRoleName", "use_code_desc");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }   

        return true;     
    } 	


    public boolean Coll_txtStockAmount_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((BigDecimal) ElValue).compareTo(new BigDecimal ("0.00")) == 0) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtStockAmount", null);                        

        }
        //       procijenjena 
        ra.setAttribute(CollHeadLDB, "Coll_txtEstnValu", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtStockAmount")); 
        //	 nominalna		
        ra.setAttribute(CollHeadLDB, "Coll_txtNomiValu", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtStockAmount")); 
        //neponderirana		
        ra.setAttribute(CollHeadLDB, "Coll_txtNepoValue", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtStockAmount"));
        //ponderirana = neponderirana * MVP ponder, ponder je u %
        ra.setAttribute(CollHeadLDB,"Coll_txtAcouBValue",ra.getAttribute(CollHeadLDB,"Coll_txtAcouValue"));
        ra.setAttribute(CollHeadLDB,"Coll_txtAcouBDate",ra.getAttribute(CollHeadLDB,"Coll_txtAcouDate"));

        coll_util.getPonderAndRestAmount(ra);   	

        return true;

    }

    // na gotovinskim depozitima


    public boolean Coll_txtCdeAmount_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((BigDecimal) ElValue).compareTo(new BigDecimal ("0.00")) == 0) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCdeAmount", null);                        

        }
        // procijenjena 
        ra.setAttribute(CollHeadLDB, "Coll_txtEstnValu", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeAmount"));
        // nominalna		
        ra.setAttribute(CollHeadLDB, "Coll_txtNomiValu", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeAmount"));
        // neponderirana		
        ra.setAttribute(CollHeadLDB, "Coll_txtNepoValue", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeAmount"));

        ra.setAttribute(CollHeadLDB,"Coll_txtAcouBValue",ra.getAttribute(CollHeadLDB,"Coll_txtAcouValue"));
        ra.setAttribute(CollHeadLDB,"Coll_txtAcouBDate",ra.getAttribute(CollHeadLDB,"Coll_txtAcouDate"));

        coll_util.getPonderAndRestAmount(ra);
        return true;

    }


    public boolean Coll_txtCdeCurr_FV(String ElName, Object ElValue, Integer LookUp) {


        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCdeCurr", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "cd_cur_id", null);                               
            return true;                                                                                 
        } 

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   		
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "cd_cur_id", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtCdeCurr", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                    
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }    

        // kontrola tipa i valute depozita
        String tip = ((String) ra.getAttribute(CollHeadLDB, "Coll_txtCollTypeCode"));
        String podtip = "";
        if (tip != null && !tip.equals("")) 
            podtip = tip.substring(0,4); 
        BigDecimal valuta = ((BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "cd_cur_id"));
        if (!(coll_cmp_util.ctrlCashDepVal(tip, valuta))) {
            if (podtip != null && !podtip.equals("") && podtip.equals("6CES"))
                ra.showMessage("wrnclt170");
            else 
                ra.showMessage("wrnclt100");
            return false;
        }


        // napuniti Coll_txtEstnCurr, Coll_txtNmValCurr
        ra.setAttribute(CollHeadLDB, "Coll_txtEstnCurr", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeCurr"));                        
        ra.setAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID", ra.getAttribute(CollSecPaperDialogLDB, "cd_cur_id"));   
        ra.setAttribute(CollHeadLDB, "Coll_txtNmValCurr",ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeCurr"));		
        return true;     
    } 	



    public boolean Coll_txtCdeProlong_FV(String elementName, Object elementValue, Integer lookUpType) {

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCdeProlong", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");		

        request.addMapping(CollSecPaperDialogLDB, "Coll_txtCdeProlong", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012"); 
            return false;  
        } catch (NothingSelected ns) {
            return false;

        }

        if (!(coll_cmp_util.setCashDepFinalDate(ra))) {
            ra.showMessage("wrnclt171");
            ra.setCursorPosition("Coll_txtCashDepDateUntilFinal");
        }     
        return true;
    }	 

    public boolean Coll_txtCashDepDateUntilFinal_FV(String ElName, Object ElValue, Integer LookUp) {

        if (!(coll_cmp_util.setCashDepFinalDate(ra))) {
            ra.showMessage("wrnclt171");
            return false;
        }
        return true;

    }   	

    //	Coll_txtStockIssueDate_FV	
    public boolean Coll_txtStockIssueDate_FV(String ElName, Object ElValue, Integer LookUp) {
        //		 datum izdavanja zaduznice moze biti manji ili jednak current date		

        Date issue_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtStockIssueDate");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (issue_date == null || current_date == null) 
            return true;

        if ((current_date).before(issue_date)) {
            ra.showMessage("wrnclt102");
            return false;
        }
        return true;

    } 	

    public boolean Coll_txtPayeeRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = CollSecPaperDialogLDB;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtPayeeRegNo",null);
            ra.setAttribute(ldbName,"Coll_txtPayeeCode",null);
            ra.setAttribute(ldbName,"Coll_txtPayeeData1",null);
            ra.setAttribute(ldbName,"payee_cus_id",null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtPayeeData1")) {
            ra.setAttribute(ldbName, "Coll_txtPayeeRegNo", "");
        } else if (ra.getCursorPosition().equals("Coll_txtPayeeRegNo")) {
            ra.setAttribute(ldbName, "Coll_txtPayeeData1", "");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "Coll_txtPayeeData1") != null){
            d_name = (String) ra.getAttribute(ldbName, "Coll_txtPayeeData1");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtPayeeRegNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtPayeeRegNo");
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


        if (ra.getCursorPosition().equals("Coll_txtPayeeRegNo")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtPayeeRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Coll_txtPayeeData1"));

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

        ra.setAttribute(ldbName, "payee_cus_id", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtPayeeRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtPayeeCode", ra.getAttribute("CustomerAllLookUpLDB", "code"));
        ra.setAttribute(ldbName, "Coll_txtPayeeData1", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;

    }


    public boolean Coll_txtPayeeRole_FV(String ElName, Object ElValue, Integer LookUp) {


        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayeeRole", "");         
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayeeRoleName", "");         
            ra.setAttribute(CollSecPaperDialogLDB, "role_id", null);                               
            return true;                                                                                 
        }
        if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
            ra.createLDB("UserCodeValueLookUpLDB");
        }				
        ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "role_id"); 	
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayeeRoleName", ""); 	


        LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");  

        lookUpRequest.addMapping(CollSecPaperDialogLDB, "role_id", "use_cod_val_id");						
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtPayeeRole", "use_code_value");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtPayeeRoleName", "use_code_desc");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }   

        return true;     
    } 

    // police osiguranja

    // ugovaratelj
    public boolean Coll_txtIpConRegNo_FV (String ElName, Object ElValue, Integer LookUp) {
        String ldbName = CollSecPaperDialogLDB;

        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtIpConRegNo",null);
            ra.setAttribute(ldbName,"Coll_txtIpConCode",null);
            ra.setAttribute(ldbName,"Coll_txtIpConData",null);
            ra.setAttribute(ldbName,"CON_CUS_ID",null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtIpConData")) {
            ra.setAttribute(ldbName, "Coll_txtIpConRegNo", "");
        } else if (ra.getCursorPosition().equals("Coll_txtIpConRegNo")) {
            ra.setAttribute(ldbName, "Coll_txtIpConData", "");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "Coll_txtIpConData") != null){
            d_name = (String) ra.getAttribute(ldbName, "Coll_txtIpConData");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtIpConRegNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtIpConRegNo");
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


        if (ra.getCursorPosition().equals("Coll_txtPayeeRegNo")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtIpConRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Coll_txtIpConData"));

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

        ra.setAttribute(ldbName, "CON_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtIpConRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtIpConCode", ra.getAttribute("CustomerAllLookUpLDB", "code"));
        ra.setAttribute(ldbName, "Coll_txtIpConData", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;

    }

    // osiguranik	
    public boolean Coll_txtIpInsRegNo_FV (String ElName, Object ElValue, Integer LookUp) {
        String ldbName = CollSecPaperDialogLDB;

        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtIpInsRegNo",null);
            ra.setAttribute(ldbName,"Coll_txtIpInsCode",null);
            ra.setAttribute(ldbName,"Coll_txtIpInsData",null);
            ra.setAttribute(ldbName,"INS_CUS_ID",null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtIpInsData")) {
            ra.setAttribute(ldbName, "Coll_txtIpInsRegNo", "");
        } else if (ra.getCursorPosition().equals("Coll_txtIpInsRegNo")) {
            ra.setAttribute(ldbName, "Coll_txtIpInsData", "");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "Coll_txtIpInsData") != null){
            d_name = (String) ra.getAttribute(ldbName, "Coll_txtIpInsData");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtIpInsRegNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtIpInsRegNo");
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


        if (ra.getCursorPosition().equals("Coll_txtIpInsRegNo")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtIpInsRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Coll_txtIpInsData"));

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

        ra.setAttribute(ldbName, "INS_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtIpInsRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtIpInsCode", ra.getAttribute("CustomerAllLookUpLDB", "code"));
        ra.setAttribute(ldbName, "Coll_txtIpInsData", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;

    }	 

    // datum izdavanja police
    public boolean Coll_txtIpIssueDate_FV (String ElName, Object ElValue, Integer LookUp) {
        //		 datum izdavanja zaduznice moze biti manji ili jednak current date		

        Date issue_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpIssueDate");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (issue_date == null || current_date == null) 
            return true;

        if ((current_date).before(issue_date)) {
            ra.showMessage("wrnclt104");
            return false;
        }
        return true;
    }	

    //	 period vazenja police 
    public boolean Coll_txtIpValiFrom_FV (String ElName, Object ElValue, Integer LookUp) {
        //		 datum od mora biti manji od datuma do		

        Date vali_from = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpValiFrom");
        Date vali_until = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpValiUntil");
        if (vali_from == null || vali_until == null) 
            return true;

        if ((vali_until).before(vali_from)) {
            ra.showMessage("wrnclt105");
            return false;
        } 

        // datum do kada polica vazi mora biti jednak ili veci od datuma do kada je polica placena			
        Date paid_until = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpPaidUntil");

        if (vali_until == null || paid_until == null) 
            return true;

        if ((vali_until).before(paid_until)) {
            ra.showMessage("wrnclt136");
            return false;  
        } 			

        return true;

    }		
    // period placanja premije
    public boolean Coll_txtIpPaidFrom_FV (String ElName, Object ElValue, Integer LookUp) {
        //		 datum od mora biti manji od datuma do		

        Date paid_from = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpPaidFrom");
        Date paid_until = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpPaidUntil");
        if (paid_from == null || paid_until == null) 
            return true;

        if ((paid_until).before(paid_from)) {
            ra.showMessage("wrnclt105");
            return false;
        } 
        return true;

    }	
    // validacija datuma do kada je placena premija
    public boolean Coll_txtIpPaidUntil_FV () {
        // datum do kada je placena premija mora biti veci od current date i veci od datuma od kad je placena premija


        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date from = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpPaidFrom");
        Date until = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpPaidUntil");

        if (from != null && until != null) {
            if(from.after(until) || from.equals(until)){
                ra.showMessage("wrncltzst4");
                return false;
            }
        }

        //		 datum do kada je placena premija mora biti jednak ili manji od datuma do kada polica vazi		
        Date vali_until = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpValiUntil");		
        if (vali_until != null && until != null) {
            if ((vali_until).before(until)) {
                ra.showMessage("wrnclt136");
                return false;  
            } 
        }

        if (until == null || current_date == null )
            return true;
        // postaviti status police ovisno o datumu do kad je placena premija
        if (until.before(current_date)) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpStatus","I");
        } else {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpStatus","A");        	
        }
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpStatusDsc", "");   
        //ra.setCursorPosition("Coll_txtIpStatus");
        ra.invokeValidation("Coll_txtIpStatus");		
        return true;
    }

    // status police
    public boolean Coll_txtIpStatus_FV (String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollSecPaperDialogLDB, new String[]{"Coll_txtIpStatus", "Coll_txtIpStatusDsc"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Coll_txtIpStatus")){
            coll_util.clearField(CollSecPaperDialogLDB, "Coll_txtIpStatusDsc");
        }else if(ra.getCursorPosition().equals("Coll_txtIpStatusDsc")){
            coll_util.clearField(CollSecPaperDialogLDB, "Coll_txtIpStatus");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollSecPaperDialogLDB, "Coll_txtIpStatus", "Coll_txtIpStatusDsc", "clt_inspolst");
        if(value==null) return false;
        
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpStatus", value.sysCodeValue);
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpStatusDsc", value.sysCodeDesc);  
               
        // kontrola statusa A i I u odnosu na datum do kada je placena premija
        // status moze biti A samo ako ja datum do kada je placena premija u buducnosti
        String status = (String) (ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpStatus"));
        Date until = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpPaidUntil");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");

        if (status != null && status.equalsIgnoreCase("A")) {
            //provjeriti da li je datum do kada je placena premija u buducnosti
            if (until.before(current_date)) {
                // greska, status ne moze biti A	
                ra.showMessage("wrnclt153");
                return false;
            }
        } else if (status != null && status.equalsIgnoreCase("I")) {
            // provjeriti da li je datum do kada je placena premija u proslosti
            if (!until.before(current_date)) {
                //				 greska, status ne moze biti I	
                ra.showMessage("wrnclt154");
                return false;
            }
        }
        return true;     
    }

    // napomena o plici kolateralu

    public boolean Coll_txtIpSpecStatus_FV(String elementName, Object elementValue, Integer lookUpType){       
        if(elementValue==null || elementValue.equals("")){
            coll_util.clearFields(CollSecPaperDialogLDB, new String[]{"Coll_txtIpSpecStatus", "Coll_txtIpSpecStatusDsc"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Coll_txtIpSpecStatus")){
            coll_util.clearField(CollSecPaperDialogLDB, "Coll_txtIpSpecStatusDsc");
        }else if(ra.getCursorPosition().equals("Coll_txtIpSpecStatusDsc")){
            coll_util.clearField(CollSecPaperDialogLDB, "Coll_txtIpSpecStatus");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollSecPaperDialogLDB, "Coll_txtIpSpecStatus", "Coll_txtIpSpecStatusDsc", "clt_pol_spec_st");
        if(value==null) return false;
        
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpSpecStatus", value.sysCodeValue);
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpSpecStatusDsc", value.sysCodeDesc);  
        return true;
    }		


    // valuta tereta u korist trecih osoba	

    public boolean Coll_txtThirdRightCurCodeChar_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollHeadLDB, "Coll_txtThirdRightCurCodeChar", "");                        
            ra.setAttribute(CollHeadLDB, "THIRD_RIGHT_CUR_ID", null);                               
            return true;                                                                                 
        } 

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   			
        lookUpRequest.addMapping(CollHeadLDB, "THIRD_RIGHT_CUR_ID", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollHeadLDB, "Coll_txtThirdRightCurCodeChar", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                    
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }    

        return true;     
    } 	 


    public boolean Coll_txtAcumBuyValue_FV(String ElName, Object ElValue, Integer LookUp) {

        if (ElValue == null ) {         			
            ra.setAttribute(CollHeadLDB, "Coll_txtAcumBuyValue", null);                        

        }
        // procijenjena 
        ra.setAttribute(CollHeadLDB, "Coll_txtEstnValu", ra.getAttribute(CollHeadLDB, "Coll_txtAcumBuyValue")); 
        // nominalna		
        ra.setAttribute(CollHeadLDB, "Coll_txtNomiValu", ra.getAttribute(CollHeadLDB, "Coll_txtAcumBuyValue")); 
        //neponderirana		
        ra.setAttribute(CollHeadLDB, "Coll_txtNepoValue", ra.getAttribute(CollHeadLDB, "Coll_txtAcumBuyValue"));
        //ponderirana = neponderirana * MVP ponder, ponder je u %

        ra.setAttribute(CollHeadLDB,"Coll_txtAcouBValue",ra.getAttribute(CollHeadLDB,"Coll_txtAcouValue"));
        ra.setAttribute(CollHeadLDB,"Coll_txtAcouBDate",ra.getAttribute(CollHeadLDB,"Coll_txtAcouDate"));

        coll_util.getPonderAndRestAmount(ra);	

        return true;
    }

    public boolean Coll_txtIpAmoCur_FV(String ElName, Object ElValue, Integer LookUp) {


        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpAmoCur", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "IP_AMO_CUR_ID", null);                               
            return true;                                                                                 
        } 

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   		
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "IP_AMO_CUR_ID", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtIpAmoCur", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");
        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                    
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }    

        return true;     
    } 	


    public boolean ColRba_txtEligibility1_FV(String ElName, Object ElValue, Integer LookUp)
    {	
        String ldbName = "";
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(ldbName, new String[]{"ColRba_txtEligibility1", "ColRba_txtEligDesc1"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("ColRba_txtEligibility1")){
            coll_util.clearField(ldbName, "ColRba_txtEligDesc1");
        }else if(ra.getCursorPosition().equals("ColRba_txtEligDesc1")){
            coll_util.clearField(ldbName, "ColRba_txtEligibility1");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(ldbName, "ColRba_txtEligibility1", "ColRba_txtEligDesc1", "clt_rba_eligib");
        if(value==null) return false;
        
        ra.setAttribute(ldbName, "ColRba_txtEligibility1", value.sysCodeValue);
        ra.setAttribute(ldbName, "ColRba_txtEligDesc1", value.sysCodeDesc);  

        return true;      
    } // ColRba_txtEligibility1_FV


    public boolean Ves_txMadeYear_FV (String ElName, Object ElValue, Integer LookUp){

        if (ElValue == null || ((String) ElValue).equals(""))
            return true;

        String year = (String) ra.getAttribute(CollSecPaperDialogLDB,"Ves_txMadeYear"); 

        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        String datum_string = current_date.toString();
        String godina = datum_string.substring(0,4);
        int godina_i = Integer.parseInt(godina);


        int year_i = Integer.parseInt(year);

        if (year_i > godina_i) {
            ra.showMessage("wrnclt126");
            return false;
        }

        return true;
    }

    public boolean Coll_txtB1Eligibility1_FV(String ElName, Object ElValue, Integer LookUp) {	
        String code = (String) ra.getAttribute("ColWorkListLDB", "code");
        String ldbName = "";
        if (code.equalsIgnoreCase("NEKR")) {
            ldbName = "RealEstateDialogLDB";
            ra.setAttribute(ldbName, "SysCodId", "clt_eligib");
        } else if (code.equalsIgnoreCase("MJEN")) {
            ldbName = "CollBoeDialogLDB";
            ra.setAttribute(ldbName, "SysCodId", "clt_eligib");
        }	
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(ldbName, new String[]{"Coll_txtB1Eligibility1", "Coll_txtB1EligDesc1"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Coll_txtB1Eligibility1")){
            coll_util.clearField(ldbName, "Coll_txtB1EligDesc1");
        }else if(ra.getCursorPosition().equals("Coll_txtB1EligDesc1")){
            coll_util.clearField(ldbName, "Coll_txtB1Eligibility1");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(ldbName, "Coll_txtB1Eligibility1", "Coll_txtB1EligDesc1", "clt_eligib");
        if(value==null) return false;
        
        ra.setAttribute(ldbName, "Coll_txtB1Eligibility1", value.sysCodeValue);
        ra.setAttribute(ldbName, "Coll_txtB1EligDesc1", value.sysCodeDesc);  

        return true;  
    } //Coll_txtB1Eligibility1_FV

    public boolean Coll_txtB2IRBEligibility1_FV(String ElName, Object ElValue, Integer LookUp) {	
        String code = (String) ra.getAttribute("ColWorkListLDB", "code");
        String ldbName = "";
        if (code.equalsIgnoreCase("NEKR")) {
            ldbName = "RealEstateDialogLDB";
            ra.setAttribute(ldbName, "SysCodId", "clt_eligib");
        } else if (code.equalsIgnoreCase("MJEN")) {
            ldbName = "CollBoeDialogLDB";
            ra.setAttribute(ldbName, "SysCodId", "clt_eligib");
        }
        
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(ldbName, new String[]{"Coll_txtB2IRBEligibility1", "Coll_txtB2IRBEligDesc1"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Coll_txtB2IRBEligibility1")){
            coll_util.clearField(ldbName, "Coll_txtB2IRBEligDesc1");
        }else if(ra.getCursorPosition().equals("Coll_txtB2IRBEligDesc1")){
            coll_util.clearField(ldbName, "Coll_txtB2IRBEligibility1");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(ldbName, "Coll_txtB2IRBEligibility1", "Coll_txtB2IRBEligDesc1", "clt_eligib");
        if(value==null) return false;
        
        ra.setAttribute(ldbName, "Coll_txtB2IRBEligibility1", value.sysCodeValue);
        ra.setAttribute(ldbName, "Coll_txtB2IRBEligDesc1", value.sysCodeDesc);  
        
        return true;      
    } //Coll_txtB1Eligibility1_FV

    // unos pondera koje definira referent

    public void add_ponder() {
        //		 na F7		  
        // PRIVREMENO ISKLJUCENO DOK SE NE IMPLEMENTIRA CRM IZVJESTAJ 
        //		ra.showMessage("inf_1800");
        //		return;		

        BigDecimal collHeaId = null;
        // ovisi na kojem sam ekranu u trenutku poziva akcije
        if (ra.isLDBExists(CollHeadLDB)) {		
            collHeaId = (BigDecimal) ra.getAttribute(CollHeadLDB, "COL_HEA_ID");
        } else if (ra.isLDBExists("RealEstateDialogLDB")) {
            collHeaId = (BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_HEA_ID");
        } else if (ra.isLDBExists("CollBoeDialogLDB")) {
            collHeaId = (BigDecimal) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_HEA_ID");
        }
        if(collHeaId == null){
            ra.showMessage("wrnclt103");

        }else{ 		
            if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_detail_co") 
                    || ra.getScreenContext().equalsIgnoreCase("scr_owner_bank"))
                ra.loadScreen("Kol_COPonderList","scr_detail");
            else 	
                ra.loadScreen("Kol_COPonderList","base");
        } 
    }	
    // prikaz izracuna WCA za kolateral

    public void list_wca() {
        //       na F7        
        // PRIVREMENO ISKLJUCENO DOK SE NE IMPLEMENTIRA CRM IZVJESTAJ 
        //      ra.showMessage("inf_1800");
        //      return;     
        //  ponovo ukljuceno , 19.01.2008       
        BigDecimal collHeaId = null;
        // ovisi na kojem sam ekranu u trenutku poziva akcije
        if (ra.isLDBExists(CollHeadLDB)) {        
            collHeaId = (BigDecimal) ra.getAttribute(CollHeadLDB, "COL_HEA_ID");
        } else if (ra.isLDBExists("RealEstateDialogLDB")) {
            collHeaId = (BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_HEA_ID");
        } else if (ra.isLDBExists("CollBoeDialogLDB")) {
            collHeaId = (BigDecimal) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_HEA_ID");
        }
        if(collHeaId == null){
            ra.showMessage("wrnclt103");

        }else{      
            ra.loadScreen("Kol_WCAList","scr_detail");
        }    
    }     

    //	 da li je knjizica vozila vracena vlasniku	
    public boolean Vehi_txtVehLicRetOwn_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Vehi_txtVehLicRetOwn", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");		

        request.addMapping(CollSecPaperDialogLDB, "Vehi_txtVehLicRetOwn", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }

        //		 postaviti ctx za knjizicu vozila

        coll_util.setVehLicenceReturnCtx(ra);	

        return true; 
    }			
    public boolean Vehi_txtVehLicRetDat_FV () {
        //		datum kada je knjizica vozila vracena nakon sto je kredit zatvoren
        //		datum mora biti manji ili jednak current date

        Date doc_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Vehi_txtVehLicRetDat");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (doc_date == null || current_date == null) 
            return true;

        if ((current_date).before(doc_date)) {
            ra.showMessage("wrnclt121");
            return false;
        }		
        return true;
    }  


    public boolean Vehi_txtVehLicRetWho_FV (String ElName, Object ElValue, Integer LookUp)
    {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollSecPaperDialogLDB, new String[]{"Vehi_txtVehLicRetWho", "Vehi_txtVehLicRetWhoDsc"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Vehi_txtVehLicRetWho")){
            coll_util.clearField(CollSecPaperDialogLDB, "Vehi_txtVehLicRetWhoDsc");
        }else if(ra.getCursorPosition().equals("Vehi_txtVehLicRetWhoDsc")){
            coll_util.clearField(CollSecPaperDialogLDB, "Vehi_txtVehLicRetWho");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollSecPaperDialogLDB, "Vehi_txtVehLicRetWho", "Vehi_txtVehLicRetWhoDsc", "kol_vehlicretwho");
        if(value==null) return false;
        
        ra.setAttribute(CollSecPaperDialogLDB, "Vehi_txtVehLicRetWho", value.sysCodeValue);
        ra.setAttribute(CollSecPaperDialogLDB, "Vehi_txtVehLicRetWhoDsc", value.sysCodeDesc);  
        return true;        
    }
    
    public boolean Coll_txtTypeTV_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollHeadLDB, new String[]{"Coll_txtTypeTVCode", "Coll_txtTypeTV"});
            return true;        
        }   
        
        if(!ElName.equals("Coll_txtTypeTVCode")) coll_util.clearField(CollHeadLDB, "Coll_txtTypeTVCode");
        if(!ElName.equals("Coll_txtTypeTV")) coll_util.clearField(CollHeadLDB, "Coll_txtTypeTV");
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollHeadLDB, "Coll_txtTypeTVCode", "Coll_txtTypeTV", "coll_RealEstNomTyp");
        if(value==null) return false;
        
        ra.setAttribute(CollHeadLDB, "Coll_txtTypeTVCode", value.sysCodeValue);
        ra.setAttribute(CollHeadLDB, "Coll_txtTypeTV", value.sysCodeDesc);  
        return true;
    }
    
    public boolean Coll_txtCollOfficer_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            coll_util.clearFields(CollHeadLDB, new String[]{"USE_ID_CO", "Coll_txtCollOfficer"});
            return true;
        }
        
        if(ra.isLDBExists("BPMUsersByGroupLDB")){
            coll_util.clearFields("BPMUsersByGroupLDB", new String[]{"use_id", "login", "name"});
        }else{
            ra.createLDB("BPMUsersByGroupLDB");
        }
        ra.setAttribute("BPMUsersByGroupLDB","bpmGroupCode", "colOfficer");
        ra.setAttribute("BPMUsersByGroupLDB","name", ra.getAttribute(CollHeadLDB,"Coll_txtCollOfficer"));
        
        LookUpRequest request = new LookUpRequest("BPMUsersByGroupLookUp"); 
        request.addMapping("BPMUsersByGroupLDB", "use_id", "use_id");
        request.addMapping("BPMUsersByGroupLDB", "login", "login");
        request.addMapping("BPMUsersByGroupLDB", "name", "user_name");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        
        ra.setAttribute(CollHeadLDB, "Coll_txtCollOfficer", ra.getAttribute("BPMUsersByGroupLDB", "name"));
        ra.setAttribute(CollHeadLDB, "USE_ID_CO", ra.getAttribute("BPMUsersByGroupLDB", "use_id"));
        
        return true;
    } 
    
} 