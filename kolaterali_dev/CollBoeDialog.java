/*
 * Created on 2006.05.06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;
import hr.vestigo.modules.coreapp.util.CustomerUtil;
import hr.vestigo.modules.coreapp.util.SpecialAuthorityManager;

/** 
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollBoeDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollBoeDialog.java,v 1.75 2015/05/19 12:15:56 hrazst Exp $"; 
    CollateralUtil coll_util= null;
    LookUps coll_lookups = null;
    
    private java.sql.Date todaySQLDate = null;
    public CollBoeDialog(ResourceAccessor ra) {
        super(ra);
        coll_util = new CollateralUtil(ra);
        coll_lookups= new LookUps(ra);
    } 

    public void CollBoeDialog_SE() {
        if (!ra.isLDBExists("CollBoeDialogLDB")) ra.createLDB("CollBoeDialogLDB");
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");        

        if(ra.getScreenContext().compareTo("scr_change")== 0) {
            // Milka, 31.08.2009 - CRM misljenje postavlja se na D
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_SpecStatus","D");     
            ra.setAttribute("CollBoeDialogLDB","Kol_txtCRMHnbBoe","D"); 

            // Milka, 26.03.2008, automatski punim tip mjenice sa 	6MVLTRS - Bianco mjenica - vlastita trasatna - 43777
            // Zvonimir, Trazila ankica da se pojavi sifarnik
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCollTypeCode", "*");
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCollTypeName", "");
            if(!ra.invokeValidation("CollBoeDialog_txtCollTypeCode")){
                ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCollTypeCode", "");	
            }

            //automatski punim misljenje pravne sluzbe na D		  
            ra.setAttribute("CollBoeDialogLDB", "ColBoeLow_txtEligibility", "D");	
            //validacija misljenja pravne sluzbe		
            ra.setAttribute("CollBoeDialogLDB", "ColBoeLow_txtEligibilityDesc","");
            ra.invokeValidation("ColBoeLow_txtEligibility");	
        }

        if(ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_update")){
            //POSTAVI INICIJALNO KOD UNOSA REMITENTA RBA
            //Id remitenta ( RAIFFEISENBANK AUSTRIA D.D. ZAGREB )
            BigDecimal payeeCusId = new BigDecimal("8218251.0");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_P_CUS_ID",payeeCusId);
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoP","910000");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeP","00901717");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNamePayee","RAIFFEISENBANK AUSTRIA D.D. ZAGREB");
            ra.setContext("CollBoeDialog_txtRegNoP","fld_protected");
            ra.setContext("CollBoeDialog_txtCodeP","fld_protected");
            ra.setContext("CollBoeDialog_txtNamePayee","fld_protected");
            postavi_nekeUserDate();
        }
        
        if(!ra.getScreenContext().equalsIgnoreCase("scr_change")){
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_HEA_ID", (java.math.BigDecimal)ra.getAttribute("ColWorkListLDB", "col_hea_id"));
            //Podigni LDB za stare podatke
            if (!ra.isLDBExists("CollBoeDialogLDB_B")) {
                ra.createLDB("CollBoeDialogLDB_B");
            }
            try {
                ra.executeTransaction();	 
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }   
        }
        if(ra.getScreenContext().compareTo("scr_update")== 0) {
            // validacija misljenja pravne sluzbe      
            ra.setAttribute("CollBoeDialogLDB", "ColBoeLow_txtEligibilityDesc","");
            ra.invokeValidation("ColBoeLow_txtEligibility");   
        }  
        
        BigDecimal ds=(BigDecimal)ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_TYPE_ID");
        //dodana kontrola i na _SE da se podatak "Broj sporazuma" ovisno o tipu mjenice može unijeti/promjeniti ili ne
        if(ds!=null && ds.compareTo(BigDecimal.valueOf(81777))==0 && (ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_update"))){
            coll_util.enableFields(new String[] {"Coll_txtB2HNB", "Coll_txtB2IRB","CollBoeDialog_txtColDesc"}, 0);
            coll_util.setRequired(new String[] {"Coll_txtB2HNB", "Coll_txtB2IRB"}, true);
            if(ra.getScreenContext().equalsIgnoreCase("scr_change")){
                coll_util.SetDefaultValue("CollBoeDialogLDB", "Coll_txtB2HNB_OLD", "D");
                coll_util.SetDefaultValue("CollBoeDialogLDB", "Coll_txtB2IRB_OLD", "D");
                coll_util.SetDefaultValue("CollBoeDialogLDB", "Coll_txtB2HNB", "D");
                coll_util.SetDefaultValue("CollBoeDialogLDB", "Coll_txtB2IRB", "D");
            }else{
                String hnb = ra.getAttribute("CollBoeDialogLDB", "Coll_txtB2HNB_OLD");
                String irb = ra.getAttribute("CollBoeDialogLDB", "Coll_txtB2IRB_OLD");
                if(hnb==null || hnb.equals("")) ra.setRequired("Coll_txtB2HNB", false);
                if(irb==null || irb.equals("")) ra.setRequired("Coll_txtB2IRB", false);
            }       
        }else{
            coll_util.enableFields(new String[] {"Coll_txtB2HNB","Coll_txtB2IRB","CollBoeDialog_txtColDesc"}, 2);
            if(ds!=null && ds.compareTo(BigDecimal.valueOf(81777))!=0) coll_util.clearFields("CollBoeDialogLDB",new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"});
        }        
        coll_util.SetDefaultValue("CollBoeDialogLDB", "Coll_txtContractTypeCode", coll_util.GetDefaultValueFromSystemCodeValue("coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code")));
        ra.invokeValidation("Coll_txtContractTypeCode");        
    }//CollBoeDialog_SE
    
    public void otvori_prvi_mjenica() {

        if(ra.getScreenContext().compareTo("scr_change")== 0){
            ra.switchScreen("CollBoeDialog", "scr_change");
        }  

        if(ra.getScreenContext().compareTo("scr_detail")== 0){
            ra.switchScreen("CollBoeDialog", "scr_detail");
        }
        if(ra.getScreenContext().compareTo("scr_detail_co")== 0){
            ra.switchScreen("CollBoeDialog", "scr_detail_co");
        }
        if(ra.getScreenContext().compareTo("scr_update")== 0){
            ra.switchScreen("CollBoeDialog", "scr_update");
        }

        if(ra.getScreenContext().compareTo("scr_update_aut")== 0){
            ra.switchScreen("CollBoeDialog", "scr_update_aut");
        }
        if(ra.getScreenContext().compareTo("scr_update_ref")== 0){
            ra.switchScreen("CollBoeDialog", "scr_update_ref");
        }
    }

    public void otvori_drugi_mjenica() {
        if(ra.getScreenContext().compareTo("scr_change")== 0){
            ra.switchScreen("CollBoeDialog2", "scr_change");
        }
        if(ra.getScreenContext().compareTo("scr_detail")== 0){
            ra.switchScreen("CollBoeDialog2", "scr_detail");
        }
        if(ra.getScreenContext().compareTo("scr_detail_co")== 0){
            ra.switchScreen("CollBoeDialog2", "scr_detail_co");
        }
        if(ra.getScreenContext().compareTo("scr_update")== 0){
            ra.switchScreen("CollBoeDialog2", "scr_update");
        }
        if(ra.getScreenContext().compareTo("scr_update_aut")== 0){
            ra.switchScreen("CollBoeDialog2", "scr_update_aut");
        }
        if(ra.getScreenContext().compareTo("scr_update_ref")== 0){
            ra.switchScreen("CollBoeDialog2", "scr_update_ref");
        }
    }  

    public void otvori_treci_mjenica() {
        if(ra.getScreenContext().compareTo("scr_change")== 0){
            ra.switchScreen("CollBoeDialog3", "scr_change");
        }
        if(ra.getScreenContext().compareTo("scr_detail")== 0){
            ra.switchScreen("CollBoeDialog3", "scr_detail");
        }
        if(ra.getScreenContext().compareTo("scr_detail_co")== 0){
            ra.switchScreen("CollBoeDialog3", "scr_detail_co");
        }
        if(ra.getScreenContext().compareTo("scr_update")== 0){
            ra.switchScreen("CollBoeDialog3", "scr_update");
        }
        if(ra.getScreenContext().compareTo("scr_update_aut")== 0){
            ra.switchScreen("CollBoeDialog3", "scr_update_aut");
        }
        if(ra.getScreenContext().compareTo("scr_update_ref")== 0){
            ra.switchScreen("CollBoeDialog3", "scr_update_ref");
        }
    }

    public void otvori_4_mjenica() {
        if(ra.getScreenContext().compareTo("scr_change")== 0){
            ra.switchScreen("CollBoeDialog4", "scr_change");
        }
        if(ra.getScreenContext().compareTo("scr_detail")== 0){
            ra.switchScreen("CollBoeDialog4", "scr_detail");
        }
        if(ra.getScreenContext().compareTo("scr_detail_co")== 0){
            ra.switchScreen("CollBoeDialog4", "scr_detail_co");
        }
        if(ra.getScreenContext().compareTo("scr_update")== 0){
            ra.switchScreen("CollBoeDialog4", "scr_update");
        }
        if(ra.getScreenContext().compareTo("scr_update_aut")== 0){
            ra.switchScreen("CollBoeDialog4", "scr_update_aut");
        }
        if(ra.getScreenContext().compareTo("scr_update_ref")== 0){
            ra.switchScreen("CollBoeDialog4", "scr_update_ref");
        }		
    }

    public void confirm(){
        //SPREMANJE I POTVRDA PODATAKA
        // dodati validaciju prihvatljivosti

        boolean imaNepopunjenih = false;

        if (!(ra.isRequiredFilled())) {
            return;
        }
        String crm = null;
        BigDecimal colTypeId = null;
        colTypeId = (BigDecimal) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_TYPE_ID");
        crm = (String) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_SpecStatus");
        if((crm == null) ){
            imaNepopunjenih = true; 
        }else{
            if(crm.trim().length() == 0) imaNepopunjenih = true; 
        }
        if (colTypeId == null)imaNepopunjenih = true; 
        //Provjera popunjenosti na 2 ekranu 
        if(imaNepopunjenih){
            ra.showMessage("infclt1"); 
            return;	
        } 

        ra.setAttribute("CollBoeDialogLDB", "save_ver_aut_flag", "1"); 	 
        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_USER_LOCK_IN", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_USER_LOCK")); 
        BigDecimal colHeaId = (BigDecimal)ra.getAttribute("CollBoeDialogLDB","CollBoeDialog_COL_HEA_ID");
        if(ra.getScreenContext().equalsIgnoreCase("scr_change") && colHeaId != null){
            ra.setScreenContext("scr_update");
        }

        if(ra.getScreenContext().compareTo("scr_change")== 0){
            try {
                ra.executeTransaction();
                ra.showMessage("infclt2");
            } catch (VestigoTMException vtme) {
                error("CollBoeDialog -> insert(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
        }//scr_change za transakciju

        if(ra.getScreenContext().compareTo("scr_update")== 0 || ra.getScreenContext().compareTo("scr_update_aut")== 0 ||
                ra.getScreenContext().compareTo("scr_update_ref")== 0){
            Integer del = (Integer) ra.showMessage("qer002");
            if (del != null && del.intValue() == 1) {
                try {
                    ra.executeTransaction();
                    ra.showMessage("infclt3");
                } catch (VestigoTMException vtme) {
                    error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                    if (vtme.getMessageID() != null)
                        ra.showMessage(vtme.getMessageID());
                }
            }
        }//scr_update za transakciju
        colTypeId = null;

    }//confirm


    public void exit() {
        System.out.println("USAOOOOOOOOOOOOOOOO!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (ra.getScreenID().equals("CollBoeDialog")){
            ra.exitScreen();
            ra.invokeAction("refresh");
        }

        if (ra.getScreenID().equals("CollBoeDialog2")){
            ra.exitScreen();
            ra.exitScreen();
            ra.invokeAction("refresh");

        }
        if (ra.getScreenID().equals("CollBoeDialog3")){
            ra.exitScreen();
            ra.exitScreen();
            ra.invokeAction("refresh");
        }

        if (ra.getScreenID().equals("CollBoeDialog4")){
            ra.exitScreen();
            ra.exitScreen();
            ra.invokeAction("refresh");
        }
    }//exit

    public void loan_ben(){

        java.math.BigDecimal colHeaId = (java.math.BigDecimal)ra.getAttribute("CollBoeDialogLDB","CollBoeDialog_COL_HEA_ID");
        if(colHeaId == null){
            //Nije moguce upisati partiju i korisnika plasmana prije upisa podataka o mjenici sa F8.
            ra.showMessage("wrnclt110");
            return;
        }

        if((ra.getScreenContext().compareTo("scr_change")== 0)  || (ra.getScreenContext().compareTo("scr_update")== 0)){
            // podici LDB i napuniti
            if (!(ra.isLDBExists("RealEstateDialogLDB"))) {
                ra.createLDB("RealEstateDialogLDB");
            }
            // Milka, 18.09.2009 - radi preracuna ponderirane i raspolozive vrijednsoti kod dodavanja i brisanja plasmana
            ra.setAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID",ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_HEA_ID"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_REAL_EST_NM_CUR_ID",ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_CUR_ID"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtNomiValu",ra.getAttribute("CollBoeDialogLDB","CollBoeDialog_txtAmount"));
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonder", null);

            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtWeighValue",null);
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtWeighDate",null);  
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtAvailValue",null);
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtAvailDate",null);  
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtSumPartVal",null);
            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtSumPartDat",null);     

            ra.setAttribute("RealEstateDialogLDB","Coll_txtB1Eligibility1",ra.getAttribute("CollBoeDialogLDB","Coll_txtB1Eligibility1"));
            ra.setAttribute("RealEstateDialogLDB","Coll_txtB1EligDesc1",ra.getAttribute("CollBoeDialogLDB","Coll_txtB1EligDesc1"));
            ra.setAttribute("RealEstateDialogLDB","Coll_txtB2IRBEligibility1",ra.getAttribute("CollBoeDialogLDB","Coll_txtB2IRBEligibility1"));
            ra.setAttribute("RealEstateDialogLDB","Coll_txtB2IRBEligDesc1",ra.getAttribute("CollBoeDialogLDB","Coll_txtB2IRBEligDesc1"));

            ra.loadScreen("LoanBeneficiary","scr_bilofexch");
        }

        if(ra.getScreenContext().compareTo("scr_detail")== 0 || ra.getScreenContext().compareTo("scr_update_aut")== 0 || ra.getScreenContext().compareTo("scr_detail_co")== 0){
            ra.loadScreen("LoanBeneficiary","detail_bxc");
        }
    }	

    //  potvrde collateral officer-a
    public void confirm_co() {       
        if (!(ra.isLDBExists("CollCoChgHistLDB"))) {
            ra.createLDB("CollCoChgHistLDB");
        }

        // FBPr200017059 - provjera da li korisnik ima ovlaštenje potrebno za izvoðenje akcije
        if((!(new SpecialAuthorityManager(ra)).checkSpecialAuthorityCodeForApplicationUser("aix", "COLL_OFF"))) {
            ra.showMessage("insufficientVerAuthority");
            return;
        }

        // pitanje da li stvarno zeli potvrditi podatke      
        Integer retValue = (Integer) ra.showMessage("col_qer004");
        if (retValue!=null && retValue.intValue() == 0) return;             

        //postavljanje atribute za insert u CO_CHG_HISTORY
        ra.setAttribute("ColWorkListLDB", "co_chg_use_id", null);  

        ra.setAttribute("ColWorkListLDB", "co_ind", ra.getAttribute("CollBoeDialogLDB", "CollBoe_txtCoConfirm"));
        //potvrda CO samo ako nije co_ind= D-> ako je vec potvrden -> poruka
//        System.out.println("Ispis Coll_txtCoConfirm:"+ra.getAttribute("CollBoeDialogLDB", "CollBoe_txtCoConfirm").toString());  
        if(!ra.getAttribute("CollBoeDialogLDB", "CollBoe_txtCoConfirm").toString().equalsIgnoreCase("D")){
            try {
                ra.executeTransaction();
                ra.showMessage("infclt3");
            } catch (VestigoTMException vtme) {
                error("CollBoeDialogLDB -> confirm_coll(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }               
        }else{
            ra.showMessage("wrn_dist_rep21");
            return;
        }

        ra.setAttribute("CollBoeDialogLDB", "CollBoe_txtCoConfirm",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirm"));
        ra.setAttribute("CollBoeDialogLDB", "CollBoe_txtCoConfirmUserId",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmUserId"));
        ra.setAttribute("CollBoeDialogLDB", "CollBoe_txtCoConfirmUserName",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmUserName"));
        ra.setAttribute("CollBoeDialogLDB", "CollBoe_txtCoConfirmTime",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmTime"));

    }//confirm_coll  

    public void save_coll() {
        //SAMO SPREMANJE PODATAKA
        boolean imaNepopunjenih = false;


        //Obavezni podaci drugog ekrana
        //CollBoeDialog_txtEligDesc					prihvatljivost kolaterala	Opis 
        //CollBoeDialog_txtEligibility				prihvatljivost kolaterala	DA NE
        //CollBoeDialog_txtNumberBoe				broj komada

        //CollBoeDialog_COL_TYPE_ID				//Tip kolaterala
        //CollBoeDialog_BOE_TYPE_ID				//Tip mjenice
        //CollBoeDialog_ISU_PLACE_ID			//Mjesto izdavanja
        //CollBoeDialog_txtIssueDate

        if((ra.getScreenContext().compareTo("scr_change")== 0)  || (ra.getScreenContext().compareTo("scr_update")== 0)    ){

            //Najprije provjera jesu li popunjeni svi podaci 
            String eligibility = null;
            Integer numberBoe = null;
            java.math.BigDecimal colTypeId = null;
            java.math.BigDecimal boeTypeId = null;
            java.math.BigDecimal isuPlaceId = null;
            java.sql.Date issueDate = null;

            eligibility = (String) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtEligibility");
            numberBoe = (Integer) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNumberBoe");
            colTypeId = (java.math.BigDecimal) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_TYPE_ID");
            boeTypeId = (java.math.BigDecimal) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_BOE_TYPE_ID");
            isuPlaceId = (java.math.BigDecimal) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_ISU_PLACE_ID");
            issueDate = (java.sql.Date) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtIssueDate");

            if((eligibility == null) ){
                imaNepopunjenih = true; 
            }else{
                if(eligibility.trim().length() == 0) imaNepopunjenih = true; 
            }
            if(numberBoe == null){
                imaNepopunjenih = true; 
            }

            if (colTypeId == null)imaNepopunjenih = true; 
            if (boeTypeId == null)imaNepopunjenih = true; 
            if (isuPlaceId == null)imaNepopunjenih = true; 
            if (issueDate == null)imaNepopunjenih = true; 

            //Provjera popunjenosti na 2 ekranu 
            if(imaNepopunjenih){
                ra.showMessage("infclt1"); 
                return;	
            }
            //Provjera popunjenosti 
            if (!(ra.isRequiredFilled())) {
                return;
            }
            BigDecimal col_hea_id = (BigDecimal) ra.getAttribute("CollBoeDialogLDB","CollBoeDialog_COL_HEA_ID");
            if (ra.getScreenContext().equalsIgnoreCase("scr_change") && col_hea_id != null) {
                ra.setScreenContext("scr_update");
            }
            //postaviti flag da radim samo spremanje podataka
            //save_ver_aut_flag = 0 - samo spremanje podataka

            ra.setAttribute("CollBoeDialogLDB", "save_ver_aut_flag", "0"); 	 

            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_USER_LOCK_IN", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_USER_LOCK")); 
            if(ra.getScreenContext().compareTo("scr_change")== 0){
                try {
                    ra.executeTransaction();
                    ra.showMessage("infclt2");
                } catch (VestigoTMException vtme) {
                    error("CollBoeDialog -> insert(): VestigoTMException", vtme);
                    if (vtme.getMessageID() != null)
                        ra.showMessage(vtme.getMessageID());
                }
            }//scr_change za transakciju

            if(ra.getScreenContext().compareTo("scr_update")== 0){
                Integer del = (Integer) ra.showMessage("qer002");
                if (del != null && del.intValue() == 1) {
                    try {
                        ra.executeTransaction();
                        ra.showMessage("infclt3");
                    } catch (VestigoTMException vtme) {
                        error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                        if (vtme.getMessageID() != null)
                            ra.showMessage(vtme.getMessageID());
                    }
                }
            }//scr_update za transakciju
            eligibility = null;
            numberBoe = null;

            colTypeId = null;
            boeTypeId = null;
            isuPlaceId = null;
            issueDate = null;		
            if (ra.getScreenID().equals("CollBoeDialog")){
                ra.exitScreen();
                ra.invokeAction("refresh");
            }		
            if (ra.getScreenID().equals("CollBoeDialog2")){
                ra.exitScreen();
                ra.exitScreen();
                ra.invokeAction("refresh");
            }			

        }//if scr_change || scr_update
    }//save_coll


    public boolean CollBoeDialog_txtIssueDate_FV(){
        java.sql.Date issueDate = (java.sql.Date) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtIssueDate");

        if(issueDate != null){
            if (issueDate.after(todaySQLDate) ){
                //Datum izdavanja mjenice moze biti danasnji datum ili datum u proslosti
                return false;
            }
        }
        return true;
    }


    public boolean CollBoeDialog_txtRegNoB_FV(String elementName, Object elementValue, Integer lookUpType) {                                 
        //KORISNIK PLASMANA    CUS_ID
        String ldbName = "CollBoeDialogLDB";       

        if (elementValue == null || ((String) elementValue).equals("")) {                                                                
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtRegNoB",null); 
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCodeB",null);
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtNameB",null);                                                   
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_CUS_ID",null);                                                                           
            return true;                                                                                                                   
        }                                                                                                                                

        if (ra.getCursorPosition().equals("CollBoeDialog_txtNameB")) {                                                      
            ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoB", "");
            ra.setAttribute(ldbName, "CollBoeDialog_txtCodeB", "");                                                          
        } else if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoB")) {                                              
            ra.setAttribute(ldbName, "CollBoeDialog_txtNameB", "");  
            ra.setAttribute(ldbName, "CollBoeDialog_txtCodeB", "");                                                        

        } else if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeB")) {
            ra.setAttribute(ldbName, "CollBoeDialog_txtNameB", "");  
            ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoB", "");                                                        
        }                                                                                                                               

        String d_name = "";                                                                                                              
        if (ra.getAttribute(ldbName, "CollBoeDialog_txtNameB") != null){                                                    
            d_name = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtNameB");                                             
        }                                                                                                                                

        String d_register_no = "";                                                                                                       
        if (ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoB") != null){                                                   
            d_register_no = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoB");                                     
        }                                                                                                                                

        String d_code = "";   
        if (ra.getAttribute(ldbName, "CollBoeDialog_txtCodeB") != null){                                                   
            d_code = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtCodeB");                                     
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

        if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoB"))                                                      
            ra.setCursorPosition(3);   
        if (ra.getCursorPosition().equals("CollBoeDialog_txtCodeB"))  
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoB"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameB")); 
        ra.setAttribute("CustomerAllLookUpLDB", "code", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeB")); 


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

        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));        
        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoB", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNameB", ra.getAttribute("CustomerAllLookUpLDB", "name"));        
        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeB", ra.getAttribute("CustomerAllLookUpLDB", "code"));        

        //KORISNIK PLASMANA                       
        ldbName = null; 
        d_register_no = null;                                                                                                                            
        d_code = null;                                                                                                                            
        d_name  = null;                                                                                                              
        return true;                                                                                                                     
    }   //CollBoeDialog_txtRegNoB_FV                                                                                                                               

    public boolean CollBoeDialog_txtCollTypeCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCollTypeCode", "");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCollTypeName", "");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_TYPE_ID", null);
            ra.setAttribute("CollBoeDialogLDB", "dummyBD", null);

            ra.setAttribute("CollBoeDialogLDB", "dummySt", null);

            ra.setAttribute("CollBoeDialogLDB", "dummyDate", null);
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtBoeTypeUCD", "");   
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtBoeTypeUCV", "");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_BOE_TYPE_ID", null);
            return true;
        }

        if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
            ra.createLDB("CollateralTypeLookUpLDB");
        }
        ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", "MJEN");
        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCollTypeName", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollAtrLookUp"); 
        lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_COL_TYPE_ID", "coll_type_id");
        lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtCollTypeCode", "coll_type_code");
        lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtCollTypeName", "coll_type_name");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummySt", "val_per_min");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "mvp_dfl");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "mvp_min");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "mvp_max");

        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "hnb_dfl");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "hnb_min");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "hnb_max");

        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "rzb_dfl");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "rzb_min");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyBD", "rzb_max");

        lookUpRequest.addMapping("CollBoeDialogLDB", "dummySt", "col_prior");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummySt", "mortgage_flag");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummySt", "posting_flag");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyDate", "date_from");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummyDate", "date_until");			

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;  
        }
        if(ra.getCursorPosition().equals("CollBoeDialog_txtCollTypeCode")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("CollBoeDialog_txtCollTypeName")){
            ra.setCursorPosition(1);
        }	

        ra.setAttribute("CollBoeDialogLDB", "dummyBD", null);
        ra.setAttribute("CollBoeDialogLDB", "dummySt", null);
        ra.setAttribute("CollBoeDialogLDB", "dummyDate", null);
        
        //:'(
        //ako je mjenica COL_TYPE_ID != 81777 potrebno je onemoguciti unos podatka Broj sporazuma jer je unos moguc samo za taj tip
        //povezano i sa _SE funkcijom ekrana zaduznica
        //kod promjene je ovu _FV moguce pozivati pa je potrebna provjera context-a  tako da je podatak moguce mjenjati samo ako je context unosa i tip je 81777
        if( ((BigDecimal)ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_TYPE_ID")).compareTo(BigDecimal.valueOf(81777))==0 ){
            coll_util.enableFields(new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"}, 0);
            coll_util.setRequired(new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"}, true);
            coll_util.SetDefaultValue("CollBoeDialogLDB", "Coll_txtB2HNB", "D");
            coll_util.SetDefaultValue("CollBoeDialogLDB", "Coll_txtB2IRB", "D");
            ra.setContext("CollBoeDialog_txtColDesc","fld_plain"); 
        }else{
            coll_util.enableFields(new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"}, 2);
            coll_util.clearFields("CollBoeDialogLDB",new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"});
            ra.setContext("CollBoeDialog_txtColDesc","fld_protected"); 
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtColDesc", "");
        }

        return true;

    }//CollBoeDialog_txtCollTypeCode_FV

    public boolean CollBoeDialog_txtRegNoP_FV(String elementName, Object elementValue, Integer lookUpType) {                                  

        //REMITENT                          
        //CollBoeDialog_txtRegNoP           
        //CollBoeDialog_txtCodeP            
        //CollBoeDialog_txtNamePayee        
        //CollBoeDialog_P_CUS_ID            

        String ldbName = "CollBoeDialogLDB";                                                                                              

        if (elementValue == null || ((String) elementValue).equals("")) {                                                                    
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtRegNoP",null);                                                  
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtCodeP",null);  
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtNamePayee",null);  

            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_P_CUS_ID",null);                                                                         
            return true;                                                                                                                       
        }                                                                                                                                    

        if (ra.getCursorPosition().equals("CollBoeDialog_txtNamePayee")) {                                                            
            ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoP", ""); 
            ra.setAttribute(ldbName, "CollBoeDialog_txtCodeP", ""); 
        } else if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoP")) {                                                    
            ra.setAttribute(ldbName, "CollBoeDialog_txtNamePayee", ""); 
            ra.setAttribute(ldbName, "CollBoeDialog_txtCodeP", ""); 
            //ra.setCursorPosition(2);                                                                                                         
        }else if(ra.getCursorPosition().equals("CollBoeDialog_txtCodeP")){
            ra.setAttribute(ldbName, "CollBoeDialog_txtRegNoP", ""); 
            ra.setAttribute(ldbName, "CollBoeDialog_txtNamePayee", ""); 
        }                                                                                                                                    

        String d_name = "";                                                                                                                  
        if (ra.getAttribute(ldbName, "CollBoeDialog_txtNamePayee") != null){                                                          
            d_name = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtNamePayee");                                                   
        }                                                                                                                                    

        String d_register_no = "";                                                                                                           
        if (ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoP") != null){                                                         
            d_register_no = (String) ra.getAttribute(ldbName, "CollBoeDialog_txtRegNoP");                                           
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

        if (ra.getCursorPosition().equals("CollBoeDialog_txtRegNoP"))                                                            
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoP"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNamePayee"));        
        ra.setAttribute("CustomerAllLookUpLDB", "code", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeP"));        

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

        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_P_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));                            
        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtRegNoP", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtNamePayee", ra.getAttribute("CustomerAllLookUpLDB", "name"));        
        ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCodeP", ra.getAttribute("CustomerAllLookUpLDB", "code"));        

        if(ra.getCursorPosition().equals("CollBoeDialog_txtRegNoP")){                                                             
            ra.setCursorPosition(3);                                                                                                           
        }  
        if(ra.getCursorPosition().equals("CollBoeDialog_txtCodeP")){                                                              
            ra.setCursorPosition(2);                                                                                                           
        }
        if(ra.getCursorPosition().equals("CollBoeDialog_txtNamePayee")){                                                              
            ra.setCursorPosition(1);                                                                                                           
        }	                                                                                                                                   
        return true;                                                                                                                         

    }//CollBoeDialog_txtRegNoP_FV     


    public boolean CollBoeDialog_txtAmount_FV(){

        return true;
    }

    public boolean CollBoeDialog_txtCurIdCodeChar_FV(String ElName, Object ElValue, Integer LookUp){

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtCurIdCodeChar", "");                        
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_CUR_ID", null);                               
            return true;                                                                                 
        }

        ra.setAttribute("CollBoeDialogLDB", "dummySt", "");
        ra.setAttribute("CollBoeDialogLDB", "dummyInt", null);
   
        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   		
        lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_CUR_ID", "cur_id");           
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummySt", "code_num");
        lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtCurIdCodeChar", "code_char");
        lookUpRequest.addMapping("CollBoeDialogLDB", "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }                                                                                              
        return true;     

    }//CollBoeDialog_txtCurIdCodeChar_FV


    public boolean CollBoeDialog_txtIsuPlaCode_FV(String elementName, Object elementValue, Integer LookUp){

        //Mjesto izdavanja
        //CollBoeDialog_ISU_PLACE_ID
        //CollBoeDialog_txtIsuPlaCode
        //CollBoeDialog_txtIssuePlace

        java.math.BigDecimal placeTypeId = new java.math.BigDecimal("5999.0");

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtIsuPlaCode", "");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtIssuePlace", "");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_ISU_PLACE_ID", null);
            return true;
        }

        if (ra.getCursorPosition().equals("CollBoeDialog_txtIssuePlace")) {
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtIsuPlaCode", "");
        } else if (ra.getCursorPosition().equals("CollBoeDialog_txtIsuPlaCode")) {
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtIssuePlace", "");
        }	

        if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }

        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", placeTypeId);

        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");

        lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_ISU_PLACE_ID", "pol_map_id");
        lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtIsuPlaCode", "code");
        lookUpRequest.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtIssuePlace", "name"); 

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        if(ra.getCursorPosition().equals("CollBoeDialog_txtIsuPlaCode")){                                                             
            ra.setCursorPosition(2);                                                                                                           
        } 
        if(ra.getCursorPosition().equals("CollBoeDialog_txtIssuePlace")){                                                             
            ra.setCursorPosition(1);                                                                                                           
        } 
        return true;

    }//CollBoeDialog_txtIsuPlaCode_FV

    public void postavi_nekeUserDate(){

        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new java.sql.Date(timeT);
        
        if(ra.getScreenContext().compareTo("scr_change")== 0){
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_USE_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_USE_OPEN_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtUseOpenIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtUseOpenIdName",(String) ra.getAttribute("GDB", "Use_UserName"));

            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_ORIGIN_ORG_UNI_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "org_uni_id"));
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_ORG_UNI_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "org_uni_id"));			

            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_CAT_ID",(java.math.BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id"));			


            java.sql.Date datumOd = null;
            datumOd = (java.sql.Date) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_FROM");

            if(datumOd == null){
                ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_FROM", todaySQLDate); 
            }
            datumOd = null;
            calendar = null;


            java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
            java.sql.Date datumDo = null;
            datumDo = (java.sql.Date) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_UNTIL");

            if(datumDo == null){
                ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_UNTIL", vvDatUntil); 
            }
            datumDo = null;
            vvDatUntil = null;
        }
        if(ra.getScreenContext().compareTo("scr_update")== 0){
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_USE_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute("CollBoeDialogLDB","CollBoeDialog_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_ORG_UNI_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "org_uni_id"));			
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_CAT_ID",(java.math.BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id"));			
            java.sql.Date datumOd = null;
            datumOd = (java.sql.Date) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_FROM");

            if(datumOd == null){
                ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_FROM", todaySQLDate); 
            }
            datumOd = null;
            calendar = null;
            java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
            java.sql.Date datumDo = null;
            datumDo = (java.sql.Date) ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_UNTIL");

            if(datumDo == null){
                ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_UNTIL", vvDatUntil); 
            }
            datumDo = null;
            vvDatUntil = null;
        }
        if(ra.getScreenContext().compareTo("scr_detail")== 0 || ra.getScreenContext().compareTo("scr_detail_co")== 0){
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_CAT_ID",(java.math.BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id"));			
        }

    }//postavi_nekeUserDate

    public boolean CollBoeDialog_txtBoeType_FV(String elementName, Object elementValue, Integer lookUpType){

        //TIP ( VRSTA ) MJENICE
        //CollBoeDialog_BOE_TYPE_ID                                                                                                   
        //CollBoeDialog_txtBoeTypeUCV
        //CollBoeDialog_txtBoeTypeUCD

        java.math.BigDecimal tipKolaterala = (java.math.BigDecimal)ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_TYPE_ID");

        if(tipKolaterala == null){
            ra.showMessage("wrnclt47");
            //Za odabir vrste mjenice potrebno je najprije odabrati vrstu kolaterala
            return false;
        }
        if (elementValue == null || ((String) elementValue).equals("")) {                                    

            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtBoeTypeUCD", "");   
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_txtBoeTypeUCV", "");
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_BOE_TYPE_ID", null);                                
            return true;                                                                                 
        }                                                                                                
        if (!ra.isLDBExists("UserCodeValueLookUpLDB")) {                                                 
            ra.createLDB("UserCodeValueLookUpLDB");                                                      
        }                                                                                                

        ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "clt_boe_type");                        
        LookUpRequest lu = new LookUpRequest("UserCodeValueLookUp");                                     
        lu.addMapping("CollBoeDialogLDB", "CollBoeDialog_BOE_TYPE_ID", "use_cod_val_id");  
        lu.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtBoeTypeUCV", "use_code_value");
        lu.addMapping("CollBoeDialogLDB", "CollBoeDialog_txtBoeTypeUCD", "use_code_desc");                 

        try {                                                                                            
            ra.callLookUp(lu);                                                                           
        } catch (EmptyLookUp elu) {                                                                      
            ra.showMessage("err012");                                                                    
            return false;                                                                                
        } catch (NothingSelected ns) {                                                                   
            return false;                                                                                
        } 

        if(ra.getCursorPosition().equals("CollBoeDialog_txtBoeTypeUCV")){                                                             
            ra.setCursorPosition(2);                                                                                                           
        } 
        if(ra.getCursorPosition().equals("CollBoeDialog_txtBoeTypeUCD")){                                                             
            ra.setCursorPosition(1);                                                                                                           
        } 
        return true;                                                                                     

    }//CollBoeDialog_txtBoeType_FV                                                                     


    public boolean CollBoeDialog_txtDATE_FROM_FV(){ 
        java.sql.Date pocetak = null;
        java.sql.Date kraj = null;

        if((ra.getScreenContext().compareTo("scr_change")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0)){ 

            pocetak = (java.sql.Date)ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_FROM");
            kraj = (java.sql.Date)ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_UNTIL");

            if((pocetak != null) && (kraj != null)){
                if(kraj.before(pocetak)){
                    ra.showMessage("wrnclt20");
                    //Datum DO ne moze biti manji od datuma OD
                    return false;
                }
            }
        }
        pocetak = null;
        kraj = null;
        return true;
    }//CollBoeDialog_txtDATE_FROM_FV


    public boolean CollBoeDialog_txtDATE_UNTIL_FV(){ 
        java.sql.Date pocetak = null;
        java.sql.Date kraj = null;

        if((ra.getScreenContext().compareTo("scr_change")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0)){ 


            pocetak = (java.sql.Date)ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_FROM");
            kraj = (java.sql.Date)ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_txtDATE_UNTIL");

            if((pocetak != null) && (kraj != null)){
                if(kraj.before(pocetak)){
                    ra.showMessage("wrnclt20");
                    //Datum DO ne moze biti manji od datuma OD
                    return false;
                }
            }

        }
        pocetak = null;
        kraj = null;
        return true;
    }//CollBoeDialog_txtDATE_UNTIL_FV

    public boolean Coll_txtNumOf_FV(){
        String ldb_name = "";
        if (ra.isLDBExists("CollHeadLDB")) {
            ldb_name = "CollHeadLDB";
        } else if (ra.isLDBExists("CollBoeDialogLDB")) {
            ldb_name = 	"CollBoeDialogLDB";		
        }
        int broj = 0;
        Integer broj_1 = null;
        if (ra.getAttribute(ldb_name,"Coll_txtNumOf") != null) {
            broj_1 = (Integer) ra.getAttribute(ldb_name, "Coll_txtNumOf");
            broj = broj_1.intValue();
            if (broj == 0) {
                ra.showMessage("wrnclt140");
                return false;
            }
        }	
        return true;
    } 

    public boolean CollBoeDialog_SpecStatus_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollBoeDialogLDB", "CollBoeDialog_SpecStatus", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollBoeDialogLDB", "CollBoeDialog_SpecStatus", "Vrijednosti");

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

    public boolean ColBoeLow_txtEligibility_FV(String ElName, Object ElValue, Integer LookUp) {

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute("CollBoeDialogLDB", "ColBoeLow_txtEligibility", "");   
            ra.setAttribute("CollBoeDialogLDB", "ColBoeLow_txtEligibilityDesc", ""); 
            return true;                                                                                 
        }   

        if (ra.isLDBExists("RealEstateDialogLDB")) {
            ra.setAttribute("RealEstateDialogLDB", "dummySt", null);
            ra.setAttribute("RealEstateDialogLDB", "SysCodId", "clt_eligib");
        }          

        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB"))ra.createLDB("SysCodeValueNewLookUpLDB");      
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "clt_eligib");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping("CollBoeDialogLDB", "ColBoeLow_txtEligibility", "sys_code_value");
        request.addMapping("CollBoeDialogLDB", "ColBoeLow_txtEligibilityDesc", "sys_code_desc");
        request.addMapping("CollBoeDialogLDB", "dummyBD", "sys_cod_val_id");

        try { 
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) { 
            return false;

        }         

        String rba_eligibility = null;
        String low_eligibility = null;
        BigDecimal col_cat_id = null;   

        col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");  
        low_eligibility = (String)ra.getAttribute("CollBoeDialogLDB", "ColBoeLow_txtEligibility");
        rba_eligibility = coll_util.chk_RBA_eligibility_for_all(col_cat_id, null, low_eligibility,
                null, null, null, null, null, null, null);

        ra.setAttribute("CollBoeDialogLDB", "ColBoeRba_txtEligibility",rba_eligibility);

        return true;      
    } 
    
    
    /**
     *  :'(
     * _FV podrska ideji da se iskoristi postojeæe polje opisa za unos podatka Broj sporazuma
     * _FV je definirana u CollBoeDialog.xml 
     * _FV bi se trebala pozivati samo u sluèaju UNOSA mjenice tipa COLL_TYPE_ID=81777
     * @param elementName
     * @param elementValue
     * @param lookUpType
     * @return
     */
    public boolean CollBoeDialog_txtColDesc_FV(String elementName, Object elementValue, Integer lookUpType){
        
        String brojSporazuma = elementValue.toString();
//        System.out.println(ra.getScreenID()+" CollBoeDialog_txtColDesc_FV("+brojSporazuma+")\n\n");
        
        
        //ovo se koristi i na zaduznicama(CollSecPaperDialog) a nastalo je C/P
        boolean validacija = true;
        if(brojSporazuma.trim().length()!=11){
            validacija = false;
//            System.out.println("Duljina podatka Broj sporazuma nije odgovarajuca!");
        }else if ((new CustomerUtil(ra)).getOrgUniDesc(brojSporazuma.substring(2, 5))==null) {
            validacija = false;
//            System.out.println("Neispravna OJ!");
        }else if ( !(brojSporazuma.substring(5, 7).equals("80") || brojSporazuma.substring(5, 7).equals("81") || brojSporazuma.substring(5, 7).equals("82")) ) {
            validacija = false;
//            System.out.println("Sifra vrste sporazuma nije ispravna!");
        }else {
            Pattern pattern = Pattern.compile("[0-9]{4}");
            Matcher matcher = pattern.matcher(brojSporazuma.substring(7, 11));
            if(!matcher.matches()){
                validacija = false;
            }
            
            pattern = Pattern.compile("[0-9]{2}");//prve 2 znamenke oznacavaju godinu ali bilo koju tako da je provjera da moraju biti 2 broja
            matcher = pattern.matcher(brojSporazuma.substring(2, 4));
            if(!matcher.matches()){
                validacija = false;
            }
        }  
        if(!validacija){
            Integer answer = (Integer) ra.showMessage("qer101");
            if (answer != null && answer.intValue() == 0)
                return false;
        }
        
        return true;
    }
    
    
    public boolean CollBoeDialog_YesNoLookUp_FV(String ElName, Object ElValue, Integer LookUp) {
        return coll_lookups.ConfirmDN("CollBoeDialogLDB", ElName, ElValue);
    }      
    
    public boolean CollBoeDialog_txtContractType(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields("CollBoeDialogLDB", new String[]{ "Coll_txtContractTypeCode", "Coll_txtContractTypeDesc"});
            return true;        
        }
        if (ra.getCursorPosition().equals("Coll_txtContractTypeCode")) {
            coll_util.clearField("CollBoeDialogLDB", "Coll_txtContractTypeDesc");
            ra.setCursorPosition(2);
        } else if(ra.getCursorPosition().equals("Coll_txtContractTypeDesc")){
            coll_util.clearField("CollBoeDialogLDB","Coll_txtContractTypeCode");
        }
        String sys_code_id="coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code");
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue("CollBoeDialogLDB", "Coll_txtContractTypeCode", "Coll_txtContractTypeDesc", sys_code_id);
        if(value==null) return false;
        
        ra.setAttribute("CollBoeDialogLDB", "Coll_txtContractTypeCode", value.sysCodeValue);
        ra.setAttribute("CollBoeDialogLDB", "Coll_txtContractTypeDesc", value.sysCodeDesc);  
        return true;
    }
    
}  
