package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;

import java.math.BigDecimal; 
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.vestigo.modules.collateral.util.CollReqFields;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.CollateralCmpUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;
import hr.vestigo.modules.coreapp.util.CustomerUtil;
import hr.vestigo.modules.coreapp.util.SpecialAuthorityManager;
import hr.vestigo.modules.rba.util.DateUtils;

/**   
 * @author HRAMKR  
 */ 
public class CollSecPaperDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollSecPaperDialog.java,v 1.182 2017/12/21 14:04:09 hrazst Exp $";
    CollateralUtil coll_util= null;
    CollateralCmpUtil coll_cmp_util = null;
    LookUps coll_lookups = null;
    
    String CollHeadLDB = "CollHeadLDB";
    String CollSecPaperDialogLDB = "CollSecPaperDialogLDB";
    
    private String vrsta = "";

    public CollSecPaperDialog(ResourceAccessor ra) {
        super(ra);
        coll_util = new CollateralUtil(ra);
        coll_cmp_util = new CollateralCmpUtil(ra);
        coll_lookups = new LookUps(ra);
    }
    
    // cesije
    public void CollCesijaDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);        
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");
        
        BigDecimal cesija_vrsta_id = null;
        String cesija_lista = null;
        
        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")) {
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                cesija_vrsta_id = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "cesija_vrsta_id");
                cesija_lista = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaLista");
                coll_cmp_util.fillData(ra);
                coll_util.setCesijaFirstScrCtx(cesija_vrsta_id);
                coll_util.setCesijaListaScrCtx(cesija_lista);
            }
        } else {
            // unos novog sloga                   
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");    
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligibility", "D");    
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    
            // automatski punim cesija radi naplate plasmana
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaNaplata", "N");
            // automatski punim limit cedenta, regres, valuta cesije
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaLimit", "D");
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaRegres", "N");
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCdeCurr", "HRK");

            // validacija valute cesije
            ra.invokeValidation("Coll_txtCdeCurr");   

            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.invokeValidation("KolLow_txtEligibility");   
        }
        coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtContractTypeCode", coll_util.GetDefaultValueFromSystemCodeValue("coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code")));
        ra.invokeValidation("Coll_txtContractTypeCode");
        postavi_nekeUserDate();     
    }

    public void open_main_cesija() {
        ra.switchScreen("CollCesijaDialog", ra.getScreenContext());
    }     
    
    public void open_first_cesija() {
        ra.switchScreen("CollCesijaOF", ra.getScreenContext());
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                });  
    }    

    public void open_fourth_cesija() {
        ra.switchScreen("CollCesijaDialog4", ra.getScreenContext());
    }  
    
    //  vrijednosnice       
    public void CollSecPaperDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);        
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");

        // iz kategorije odrediti o kojem se VRP-u radi pa postaviti odgovarajuci kontekst ekrana
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");   
        vrsta = (String) ra.getAttribute("ColWorkListLDB","code");
        
        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")) {
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
            if(!vrsta.equals("UDJP")) ra.invokeValidation("Coll_txtISIN");
        } else {
            // automatski punim da nije VK - za udjele u poduzecima     
            // protektiram polje za klauzulu
            if (col_cat_id.compareTo(new BigDecimal("629223")) == 0) {  
                // automatski se puni valuta na HRK
                ra.setAttribute(CollSecPaperDialogLDB, "NOMINAL_CUR_ID", ra.getAttribute("GDB", "Dom_cur_id"));           
                ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur", ra.getAttribute("GDB", "DomCur_CodeChar"));
                
                ra.setAttribute(CollHeadLDB, "Coll_txtEstnCurr", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur"));                        
                ra.setAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID", ra.getAttribute(CollSecPaperDialogLDB, "NOMINAL_CUR_ID"));   
                ra.setAttribute(CollHeadLDB, "Coll_txtNmValCurr", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur"));  
                ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNomAmoCur", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur"));                

                ra.setAttribute(CollSecPaperDialogLDB, "Vrp_txtValutnaKlauzula","0");
                ra.setAttribute(CollSecPaperDialogLDB, "Vrp_txtValutnaKlauzulaDsc","");
                ra.setCursorPosition("Vrp_txtValutnaKlauzula");
                ra.invokeValidation("Vrp_txtValutnaKlauzula");
                ra.setCursorPosition("Coll_txtCollTypeCode");               
            }

            // automatski punim da nije upisano pravo banke
            ra.setAttribute(CollHeadLDB,"Coll_txtRecLop","N");

            //  Milka, 19.12.2007 automatski punim B1 prihvatljivost na N
            ra.setAttribute(CollHeadLDB, "Coll_txtB1Eligibility", "N");
            if (col_cat_id.compareTo(new BigDecimal("629223")) != 0) {
                ra.setAttribute(CollHeadLDB, "Coll_txtB1Eligibility", "D");
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligibility", "D");               
            }

            // punjenje zabrane prodaje 
            if (col_cat_id.compareTo(new BigDecimal("619223")) == 0 || col_cat_id.compareTo(new BigDecimal("627223")) == 0 ||
                    col_cat_id.compareTo(new BigDecimal("622223")) == 0 || col_cat_id.compareTo(new BigDecimal("629223")) == 0) {
                ra.setAttribute(CollSecPaperDialogLDB,"Vrp_txtStopSell",null);    
            } else {
                ra.setAttribute(CollSecPaperDialogLDB,"Vrp_txtStopSell","N"); 
            }
            
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");    
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    
            
            // automatski punim misljenje pravne sluzbe na D         
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligibility", "D");   

            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.setCursorPosition("KolLow_txtEligibility");
            ra.invokeValidation("KolLow_txtEligibility");
            ra.setCursorPosition("Coll_txtCollTypeCode");               
            // unos novog sloga     
            ra.setAttribute("ColWorkListLDB","posting_flag",null);
        }  
        //  postaviti kontekst ovisno o tip-u VRP-a
        coll_util.setVrpFirstCtx(col_cat_id);           
        String stop_sell_ind = (String) ra.getAttribute(CollSecPaperDialogLDB, "Vrp_txtStopSell");
        // osim za detalje
        if (!(ra.getScreenContext().equalsIgnoreCase("scr_detail")) && !(ra.getScreenContext().equalsIgnoreCase("scr_detail_co"))) {
            coll_util.setVrpStopSellCtx(stop_sell_ind);     
        } else {
            ra.setContext("Coll_txtIssuerName","fld_protected");
        }
        coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtContractTypeCode", coll_util.GetDefaultValueFromSystemCodeValue("coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code")));
        ra.invokeValidation("Coll_txtContractTypeCode");

        postavi_nekeUserDate();   
        
        if(vrsta.equalsIgnoreCase("UDJE") || vrsta.equalsIgnoreCase("UDJP")){
            coll_util.hideFields(new String[]{"Vrp_lblAgencyRating","Vrp_txtAgencyRating",
                                              "Vrp_lblAgencyIssuerRating","Vrp_txtAgencyIssuerRating",
                                              "Vrp_lblShortTermRating","Vrp_txtShortTermRating"});
        } 
        if(!vrsta.equalsIgnoreCase("OBVE")){
            coll_util.hideFields(new String[]{"Vrp_lblRatingIndicator","Vrp_txtRatingIndicator"});
        }  
    }  

    public void open_main_sec_paper() {
        ra.switchScreen("CollSecPaperDialog", ra.getScreenContext());   
    }

    public void open_first_sec_paper() {
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail"))
            ra.switchScreen("CollSecPaperOF", "scr_detail");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_deact"))
            ra.switchScreen("CollSecPaperOF", "scr_detail_deact");           
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update")) {
            ra.switchScreen("CollSecPaperOF","scr_update");
            // validacija b2 prihvatljivosti               
            if (ra.getAttribute(CollHeadLDB, "Coll_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "Coll_txtEligDesc","");
                ra.setCursorPosition("Coll_txtEligibility");
                ra.invokeValidation("Coll_txtEligibility");
                ra.setCursorPosition("Coll_txtNominalAmountCur");   
            }
            // validacija rba prihvatljivosti         
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Coll_txtNominalAmountCur");
            }   
            // validacija B1 prihvatljivost
            if (ra.getAttribute(CollHeadLDB, "Coll_txtB1Eligibility") != null) {
                ra.setAttribute(CollHeadLDB, "Coll_txtB1EligDesc","");
                ra.setCursorPosition("Coll_txtB1Eligibility");
                ra.invokeValidation("Coll_txtB1Eligibility");
                ra.setCursorPosition("Coll_txtNominalAmountCur");
            }               
            // validacija B2 IRB prihvatljivosti
            if (ra.getAttribute(CollHeadLDB, "Coll_txtB2IRBEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "Coll_txtB2IRBEligDesc","");
                ra.setCursorPosition("Coll_txtB2IRBEligibility");
                ra.invokeValidation("Coll_txtB2IRBEligibility");
                ra.setCursorPosition("Coll_txtNominalAmountCur");
            }                               
        }    
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_aut"))
            ra.switchScreen("CollSecPaperOF","scr_update_aut");     
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_ref"))
            ra.switchScreen("CollSecPaperOF","scr_update_ref");   
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_co"))
            ra.switchScreen("CollSecPaperOF","scr_detail_co");
        else {
            ra.switchScreen("CollSecPaperOF", "scr_change");
            // validacija b2 prihvatljivosti   
            if (ra.getAttribute(CollHeadLDB, "Coll_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "Coll_txtEligDesc","");
                ra.setCursorPosition("Coll_txtEligibility");
                ra.invokeValidation("Coll_txtEligibility");
                ra.setCursorPosition("Coll_txtNominalAmountCur");
            }
            // validacija rba prihvatljivosti         
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Coll_txtNominalAmountCur");
            }   

            // validacija B1 prihvatljivosti
            if (ra.getAttribute(CollHeadLDB, "Coll_txtB1Eligibility") != null) {
                ra.setAttribute(CollHeadLDB, "Coll_txtB1EligDesc","");
                ra.setCursorPosition("Coll_txtB1Eligibility");
                ra.invokeValidation("Coll_txtB1Eligibility");
                ra.setCursorPosition("Coll_txtNominalAmountCur");
            }               
            // validacija B2 IRB prihvatljivosti
            if (ra.getAttribute(CollHeadLDB, "Coll_txtB2IRBEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "Coll_txtB2IRBEligDesc","");
                ra.setCursorPosition("Coll_txtB2IRBEligibility");
                ra.invokeValidation("Coll_txtB2IRBEligibility");
                ra.setCursorPosition("Coll_txtNominalAmountCur");
            }                                               
        }
    }

    public void open_fourth_sec_paper() {
        ra.switchScreen("CollSecPaperDialog4", ra.getScreenContext());   
    }
    
    public void CollMovableDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);        
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");

        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")){
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
        } else {
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "N");    
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    
            // automatski punim da nije upisano pravo banke
            ra.setAttribute(CollHeadLDB,"Coll_txtRecLop","N");
            //automatski punim da li su pokretnine osigurane sa N           
            ra.setAttribute(CollSecPaperDialogLDB,"Vehi_txtVehKasko","N");
            ra.setAttribute(CollHeadLDB,"INSPOL_IND","N");

            // automatski punim misljenje pravne sluzbe na D         
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligibility", "D");   
            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.setCursorPosition("KolLow_txtEligibility");
            ra.invokeValidation("KolLow_txtEligibility");
            ra.setCursorPosition("Coll_txtCollTypeCode");               
            // unos novog sloga     
        }
        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {         
            //  postaviti kontekst ovisno o tipu pokretnine
            coll_util.setMovTypCtx(ra);         
        }       
        this.SetDefaultValuesOnLDB();
        this.ScreenEntry_FV();
        postavi_nekeUserDate();
    }   

    public void open_main_movable() {
        ra.switchScreen("CollMovableDialog", ra.getScreenContext());               
    }   

    public void open_first_movable() {
        ra.switchScreen("CollMovableOF", ra.getScreenContext());
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                }); 
    }
    public void open_fourth_movable() {
        ra.switchScreen("CollMovableDialog4", ra.getScreenContext());  
    }
    

    // zalihe
    public void SupplyDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);        
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");

        //za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")){
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
        } else {
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");   
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    
            
            // automatski punim da nije upisano pravo banke
            ra.setAttribute(CollHeadLDB,"Coll_txtRecLop","N");

            //automatski punim da li su zalihe osigurane sa N           
            ra.setAttribute(CollSecPaperDialogLDB,"Vehi_txtVehKasko","N");
            ra.setAttribute(CollHeadLDB,"INSPOL_IND","N");

            // automatski punim misljenje pravne sluzbe na D         
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligibility", "D");   
            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.setCursorPosition("KolLow_txtEligibility");
            ra.invokeValidation("KolLow_txtEligibility");
            ra.setCursorPosition("Coll_txtCollTypeCode");               
            //unos novog sloga      
        }

        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")){         
            // ako je se duznik obvezao na minimalnu vrijednost
            coll_util.setSupplyMinValueCtx(ra);
            // postaviti kontekst ovisno o odabranom tipu
            coll_util.setSupplyTypCtx(ra);
        }
        this.SetDefaultValuesOnLDB();
        this.ScreenEntry_FV();
        postavi_nekeUserDate();
    }   

    public void otvori_main_zalihe() {
        ra.switchScreen("SupplyDialog", ra.getScreenContext());            
    }   

    public void otvori_drugi_zalihe() {
        ra.switchScreen("SupplyOF", ra.getScreenContext()); 
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                });  
    }   
    
    public void otvori_fourth_zalihe() {
        ra.switchScreen("SupplyDialog4", ra.getScreenContext());  
    } 
    
    // umjetnine    
    public void CollArtDialog_SE() {
        if (!(ra.isLDBExists(CollHeadLDB))) {
            ra.createLDB(CollHeadLDB);
        }
        if (!(ra.isLDBExists(CollSecPaperDialogLDB))) {
            ra.createLDB(CollSecPaperDialogLDB);
        }

        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_detail_co") ||
                ra.getScreenContext().equalsIgnoreCase("scr_detail_deact") ||               
                ra.getScreenContext().equalsIgnoreCase("scr_update") ||
                ra.getScreenContext().equalsIgnoreCase("scr_update_aut") || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
        } else {
            // unos novog sloga 
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");    
        }
        postavi_nekeUserDate();
    }    

    public void open_main_art() {
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail"))
            ra.switchScreen("CollArtDialog","scr_detail");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_deact"))
            ra.switchScreen("CollArtDialog","scr_detail_deact");        
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update"))
            ra.switchScreen("CollArtDialog","scr_update");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_aut"))
            ra.switchScreen("CollArtDialog","scr_update_aut");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_ref"))
            ra.switchScreen("CollArtDialog","scr_update_ref");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_co"))
            ra.switchScreen("CollArtDialog","scr_detail_co");
        else
            ra.switchScreen("CollArtDialog", "scr_change");         
    }   

    public void open_first_art() {
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail"))
            ra.switchScreen("CollArtOF", "scr_detail");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_deact"))
            ra.switchScreen("CollArtOF", "scr_detail_deact");        
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update"))
            ra.switchScreen("CollArtOF","scr_update");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_aut"))
            ra.switchScreen("CollArtOF","scr_update_aut");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_ref"))
            ra.switchScreen("CollArtOF","scr_update_ref");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_co"))
            ra.switchScreen("CollArtOF", "scr_detail_co");            
        else 
            ra.switchScreen("CollArtOF", "scr_change");
    }

    public void CollPrecDialog_SE() {

        if (!(ra.isLDBExists(CollHeadLDB))) {
            ra.createLDB(CollHeadLDB);
        }
        if (!(ra.isLDBExists(CollSecPaperDialogLDB))) {
            ra.createLDB(CollSecPaperDialogLDB);
        }

        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_detail_co") ||
                ra.getScreenContext().equalsIgnoreCase("scr_detail_deact") ||  
                ra.getScreenContext().equalsIgnoreCase("scr_update") ||
                ra.getScreenContext().equalsIgnoreCase("scr_update_aut") || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")){
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
        } else {
            // unos novog sloga     
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");                           
        }
        postavi_nekeUserDate();     
    }   

    public void open_main_prec() {
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail"))
            ra.switchScreen("CollPrecDialog","scr_detail");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_deact"))
            ra.switchScreen("CollPrecDialog","scr_detail_deact");        
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update"))
            ra.switchScreen("CollPrecDialog","scr_update");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_aut"))
            ra.switchScreen("CollPrecDialog","scr_update_aut");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_ref"))
            ra.switchScreen("CollPrecDialog","scr_update_ref");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_co"))
            ra.switchScreen("CollPrecDialog","scr_detail_co");
        else
            ra.switchScreen("CollPrecDialog", "scr_change");            
    }   

    public void open_first_prec() {
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail"))
            ra.switchScreen("CollPrecOF", "scr_detail");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_deact"))
            ra.switchScreen("CollPrecOF", "scr_detail_deact");        
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update")) 
            ra.switchScreen("CollPrecOF","scr_update");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_aut"))
            ra.switchScreen("CollPrecOF","scr_update_aut");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_ref"))
            ra.switchScreen("CollPrecOF","scr_update_ref");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_co"))
            ra.switchScreen("CollPrecOF", "scr_detail_co");            
        else 
            ra.switchScreen("CollPrecOF", "scr_change");        
    }

    public void VesselDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");

        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")){
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
        } else {
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "N");   
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D"); 
            // automatski punim tip plovila
            ra.setCursorPosition("Coll_txtCollTypeCode");
            ra.setAttribute(CollHeadLDB, "Coll_txtCollTypeCode", "*");    
            ra.invokeValidation("Coll_txtCollTypeCode"); 
            ra.setCursorPosition("Coll_txtSecTypeCode");

            // automatski punim da nije upisano pravo banke
            ra.setAttribute(CollHeadLDB,"Coll_txtRecLop", "N");

            //automatski punim da li je osigurano plovilo sa N          
            ra.setAttribute(CollSecPaperDialogLDB,"Vehi_txtVehKasko","N");
            ra.setAttribute(CollHeadLDB,"INSPOL_IND","N");

            // automatski punim misljenje pravne sluzbe na D         
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligibility", "D");   
            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.setCursorPosition("KolLow_txtEligibility");
            ra.invokeValidation("KolLow_txtEligibility");
            ra.setCursorPosition("Coll_txtSecTypeCode");                    
            // unos novog sloga     
        }        
        this.SetDefaultValuesOnLDB();
        this.ScreenEntry_FV();
        postavi_nekeUserDate();
    }   

    public void open_main_vessel() {
        ra.switchScreen("VesselDialog", ra.getScreenContext());                
    }   

    public void open_first_vessel() {
       ra.switchScreen("VesselDialogOF", ra.getScreenContext()); 
       coll_util.hideFields(new String[]{
               "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
               "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
               "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
               });  
    }  
    
    public void open_fourth_vessel() {
        ra.switchScreen("VesselDialog4", ra.getScreenContext()); 
    } 
    
    //   gotovinski depoziti    
    public void CollCashDepDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");

        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")) {
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
            ra.setContext("Coll_txtCollTypeCode", "fld_change_protected");
            ra.setCursorPosition("Coll_txtCollTypeCode");
            ra.invokeValidation("Coll_txtCollTypeCode");
            ra.setCursorPosition("Coll_txtCdeAmount");          
            ra.invokeValidation("Coll_txtDepAcc");
        } else {
            // unos novog sloga
            // automatski postaviti prolongat na N
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCdeProlong", "N");
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");      
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    
            // automatski punim misljenje pravne sluzbe na D         
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligibility", "D");   
            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.setCursorPosition("KolLow_txtEligibility");
            ra.invokeValidation("KolLow_txtEligibility");
            ra.setCursorPosition("Coll_txtCollTypeCode");

        }
        coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtSuccessive","N" );
        coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtContractTypeCode", coll_util.GetDefaultValueFromSystemCodeValue("coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code")));
        ra.invokeValidation("Coll_txtContractTypeCode");
        postavi_nekeUserDate();
    }     

    public void open_main_cashdep() {
        ra.switchScreen("CollCashDepDialog", ra.getScreenContext()); 
    }   

    public void open_second_cashdep() {
        ra.switchScreen("CollCashDepOF", ra.getScreenContext());
        if (ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_update")){
            SetDateNomiValuCoContext();
        }
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                });  
    }    
    
    public void open_fourth_cashdep() {
        ra.switchScreen("CollCashDepDialog4", ra.getScreenContext());  
    } 
    
    private void SetDateNomiValuCoContext(){
        Boolean isAccountException=(Boolean) ra.getAttribute(CollSecPaperDialogLDB, "IsCashDepExceptionAccount");
        if(isAccountException!=null && isAccountException==true){
            ra.setContext("Coll_txtNomiDate", "fld_plain");
        }else{
            ra.setContext("Coll_txtNomiDate", "fld_protected");            
        } 
    }

    // police osiguranja  
    public void CollInsPolDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);        
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");
        
        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")) {
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }

            ra.invokeValidation("Coll_txtWrnStatusCode");
            ra.invokeValidation("Coll_txtKmtStatusCode");
            ra.invokeValidation("Coll_txtCollTypeCode"); 
        } else {
            // unos novog sloga 
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");    
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    

            // Milka, 25.05.2009 automatski punim napomenu o polici sa 00
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpSpecStatus", "00");
            // Milka, 01.07.2009 automatski postavljam akumuliranu vrijednost na 0.00
            ra.setAttribute(CollHeadLDB, "Coll_txtAcumBuyValue", new BigDecimal("0.00"));
            // Saša, 20.5.2010 automatski postavljam statuse na 0            
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtWrnStatusCode", "0");
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtKmtStatusCode", "0");

            // validacija
            ra.invokeValidation("Coll_txtWrnStatusCode");
            ra.invokeValidation("Coll_txtKmtStatusCode");
            ra.invokeValidation("Coll_txtIpSpecStatus");          
        }
        ra.setAttribute(CollSecPaperDialogLDB, "garancije_val_klauzula", ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCurInd"));
        coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtContractTypeCode", coll_util.GetDefaultValueFromSystemCodeValue("coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code")));        
        ra.invokeValidation("Coll_txtContractTypeCode"); 
        postavi_nekeUserDate();
    }   

    public void open_main_inspol() {
        ra.switchScreen("CollInsPolDialog", ra.getScreenContext());
    }    

    public void open_first_inspol() {
        ra.switchScreen("CollInsPolOF", ra.getScreenContext());
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                });  
    }
    
    public void open_fourth_inspol() {
        ra.switchScreen("CollInsPolDialog4", ra.getScreenContext());
    }


    // garancije
    public void CollGuarantDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);        
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");
        
        // System.out.println ( "......tu sam " + ra.getScreenContext() + ra.getAttribute("ColWorkListLDB", "col_hea_id"));       
        // za detalje i promjenu pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")) {
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
        } else {
            // unos novog sloga       
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");        
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    
            // automatski punim polje na prvi poziv sa N
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtFirstCall", "N");
        }
        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {         
            // ako je valuta garancije kuna obavezno je polje valutna klauzula
            coll_util.setGuarValKlaCtx(ra);

            // ako je respiro = D treba aktivirati polje za unos respiro datuma
            coll_util.setGuarRespiroCtx(ra);        

            // za garancije ako je garancija izdana od drzave obavezno se upisuje broj garancije
            coll_util.setGuarNumCtx(ra);            
        }
        // spremiti trenutni indikator valutne klauzule
        ra.setAttribute(CollSecPaperDialogLDB, "garancije_val_klauzula", ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCurInd"));
        ra.invokeValidation("Coll_txtGuarIssRegNo");
        ra.invokeValidation("Coll_txtContractTypeCode");
        postavi_nekeUserDate();
    }   

    public void open_main_guarant() {
        ra.switchScreen("CollGuarantDialog", ra.getScreenContext());                 
    }   

    public void open_first_guarant() {
        ra.switchScreen("CollGuarantOF", ra.getScreenContext());       
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                });  
    }
    
    public void open_fourth_guarant() {
        ra.switchScreen("CollGuarantDialog4", ra.getScreenContext());                 
    } 

    // zaduznice
    public void CollLoanStockDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);        
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");

        String rba_eligibility = null;
        //za sve context-e osim dodavanja novog pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")){
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!(ra.isLDBExists("CollOldLDB"))) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
                coll_cmp_util.setLoanStockCtx(ra);  
                // automatski punim prihvatljivosti, RBA = D, ako je prazna
                rba_eligibility = (String) ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility");    
                if (rba_eligibility == null || rba_eligibility.trim().equals("")){            
                    ra.setAttribute(CollHeadLDB, "ColRba_txtEligibility", "D");   
                }
            }
            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.setCursorPosition("KolLow_txtEligibility");
            ra.invokeValidation("KolLow_txtEligibility");  
        } else {
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "D");       
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    
            // automatski punim tip zaduznice
            ra.setCursorPosition("Coll_txtCollTypeCode");
            ra.setAttribute(CollHeadLDB, "Coll_txtCollTypeCode", "*");    
            ra.invokeValidation("Coll_txtCollTypeCode");

            // automatski punim prihvatljivosti, RBA = D
            ra.setAttribute(CollHeadLDB, "ColRba_txtEligibility", "D");           

            // automatski punim misljenje pravne sluzbe na D         
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligibility", "D");   

            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.setCursorPosition("KolLow_txtEligibility");
            ra.invokeValidation("KolLow_txtEligibility");
            ra.setCursorPosition("Coll_txtCarrierRegisterNo");
        }
        
        //:'(
        //vrijedi za sve kontekste
        //ako je tip COL_TYPE_ID = 80777 (6DSZADU - Zadužnica za DS) treba omoguciti unos Broj sporazuma, treba paziti jer se ovaj podatak i na _FV moze promjeniti
        //pa ako se odabere neki drugi tip treba protektirati polje i obrisati uneseno da se nebi dogodilo da i za te tipove unose podatak
        BigDecimal ds=(BigDecimal)ra.getAttribute(CollHeadLDB, "COL_TYPE_ID");
        if(ds==null) {
            ra.exitScreen();
            return;
        }
        if(ds!=null && ds.compareTo(BigDecimal.valueOf(80777))==0 && (ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_update")))
        {
            coll_util.enableFields(new String[] {"Coll_txtB2HNB", "Coll_txtB2IRB","Coll_txtDesc"}, 0);
            coll_util.setRequired(new String[] {"Coll_txtB2HNB", "Coll_txtB2IRB"}, true);
            if(ra.getScreenContext().equalsIgnoreCase("scr_change")){
                coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2HNB_OLD", "D");
                coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2IRB_OLD", "D");
                coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2HNB", "D");
                coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2IRB", "D");
            }else{
                String hnb = ra.getAttribute(CollHeadLDB, "Coll_txtB2HNB_OLD");
                String irb = ra.getAttribute(CollHeadLDB, "Coll_txtB2IRB_OLD");
                if(hnb==null || hnb.equals("")) ra.setRequired("Coll_txtB2HNB", false);
                if(irb==null || irb.equals("")) ra.setRequired("Coll_txtB2IRB", false);
            } 
        }else {
            coll_util.enableFields(new String[] {"Coll_txtB2HNB","Coll_txtB2IRB","Coll_txtDesc"}, 2);
            if(ds.compareTo(BigDecimal.valueOf(80777))!=0) coll_util.clearFields(CollHeadLDB,new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"});
        }

        coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtContractTypeCode", coll_util.GetDefaultValueFromSystemCodeValue("coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code")));
        ra.invokeValidation("Coll_txtContractTypeCode");   
        postavi_nekeUserDate();
    }       

    public void open_main_loanstock() {
        ra.switchScreen("CollLoanStockDialog", ra.getScreenContext());         
    }   

    public void open_second_loanstock() {
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail"))
            ra.switchScreen("CollLoanStockOF", "scr_detail");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_deact"))
            ra.switchScreen("CollLoanStockOF", "scr_detail_deact");
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update")) {
            ra.switchScreen("CollLoanStockOF","scr_update");
            // validacija rba prihvatljivosti         
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");
            }     
        }    
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_aut"))
            ra.switchScreen("CollLoanStockOF","scr_update_aut");    
        else if (ra.getScreenContext().equalsIgnoreCase("scr_update_ref"))
            ra.switchScreen("CollLoanStockOF","scr_update_ref");  
        else if (ra.getScreenContext().equalsIgnoreCase("scr_detail_co"))
            ra.switchScreen("CollLoanStockOF", "scr_detail_co");            
        else {
            ra.switchScreen("CollLoanStockOF", "scr_change");
            // validacija rba prihvatljivosti         
            if (ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility") != null) {
                ra.setAttribute(CollHeadLDB, "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Kol_txtRbaEligDsc");
            }   
        } 
    }
    
    public void open_fourth_loanstock() {
        ra.switchScreen("CollLoanStockDialog4", ra.getScreenContext());                 
    }      

    // Milka,17.11.2006 -dodana vozila
    public void VehiDialog_SE() {
        if (!ra.isLDBExists(CollHeadLDB)) ra.createLDB(CollHeadLDB);
        if (!ra.isLDBExists(CollSecPaperDialogLDB)) ra.createLDB(CollSecPaperDialogLDB);        
        //podizem ldb za zajednièke podatke(4. tab)  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");

        //za sve context-e osim dodavanja novog pokrenuti transakciju za dohvat podataka
        if (!ra.getScreenContext().equalsIgnoreCase("scr_change")){
            ra.setAttribute(CollHeadLDB, "COL_HEA_ID", ra.getAttribute("ColWorkListLDB", "col_hea_id"));      
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null) ra.showMessage(vtme.getMessageID());
            }
            if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref")) {
                if (!ra.isLDBExists("CollOldLDB")) ra.createLDB("CollOldLDB");
                coll_cmp_util.fillData(ra);
            }
        } else {
            // automatski se puni CRM misljanje na D
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", "N");      
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb", "D");    
            // automatski punim tip vozila
            ra.setAttribute(CollHeadLDB, "Coll_txtCollTypeCode", "*");    
            ra.invokeValidation("Coll_txtCollTypeCode"); 
            // automatski punim kasko osiguranje na N             
            ra.setAttribute(CollSecPaperDialogLDB,"Vehi_txtVehKasko","N");
            // automatski punim da nije upisano pravo banke
            ra.setAttribute(CollHeadLDB,"Coll_txtRecLop","N");              
            // automatski punim misljenje pravne sluzbe na D         
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligibility", "D");   
            // validacija misljenja pravne sluzbe      
            ra.setAttribute(CollHeadLDB, "KolLow_txtEligDesc","");
            ra.invokeValidation("KolLow_txtEligibility");
            ra.setCursorPosition("Coll_txtSecTypeCode");             
        }
        this.SetDefaultValuesOnLDB();
        this.ScreenEntry_FV();
        postavi_nekeUserDate();
    }        

    public void otvori_prvi_vozi() {
        ra.switchScreen("VehiDialog", ra.getScreenContext());          
    }    

    public void otvori_drugi_vozi() {
        ra.switchScreen("VehiDialog2", ra.getScreenContext());        
    }     

    public void otvori_treci_vozi() {
        ra.switchScreen("VehiDialogOF", ra.getScreenContext());
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                }); 
    } 
    
    public void otvori_cetvrti_vozi() {
        ra.switchScreen("VehiDialog4", ra.getScreenContext());
    }                           

    public void postavi_nekeUserDate(){
        if(ra.getScreenContext().compareTo("scr_change")== 0){
            ra.setAttribute(CollHeadLDB, "USE_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(CollHeadLDB, "USE_OPEN_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(CollHeadLDB,"Coll_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(CollHeadLDB,"Coll_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute(CollHeadLDB,"Coll_txtUseOpenIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(CollHeadLDB,"Coll_txtUseOpenIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute(CollHeadLDB, "ORIGIN_ORG_UNI_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "org_uni_id"));
            ra.setAttribute(CollHeadLDB, "ORG_UNI_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "org_uni_id"));   
            ra.setAttribute(CollHeadLDB,"Coll_txtNomiDate",ra.getAttribute("GDB","ProcessingDate"));
        }
        if(ra.getScreenContext().compareTo("scr_update")== 0){
            ra.setAttribute(CollHeadLDB, "USE_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(CollHeadLDB,"Coll_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(CollHeadLDB,"Coll_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute(CollHeadLDB, "ORG_UNI_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "org_uni_id"));   
        }

        java.sql.Date todaySQLDate = null;
        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new java.sql.Date(timeT);
        java.sql.Date datumOd = null;
        datumOd = (java.sql.Date) ra.getAttribute(CollHeadLDB, "Coll_txtDateFrom"); 

        if(datumOd == null){
            ra.setAttribute(CollHeadLDB, "Coll_txtDateFrom", todaySQLDate); 
        }
        datumOd = null;    
        calendar = null;
        todaySQLDate = null;
        java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
        java.sql.Date datumDo = null;
        datumDo = (java.sql.Date) ra.getAttribute(CollHeadLDB, "Coll_txtDateUnti");

        if(datumDo == null){
            ra.setAttribute(CollHeadLDB, "Coll_txtDateUnti", vvDatUntil); 
        }
        datumDo = null;
        vvDatUntil = null; 

        vrsta = (String) ra.getAttribute("ColWorkListLDB","code");

    }//postavi_nekeUserDate     
 
    // spremanje i postavljanje statusa za slanje na verifikaciju   
    public void confirm()
    {        
        // pitanje da li stvarno zeli potvrditi podatke      
        Integer retValue = (Integer) ra.showMessage("col_qer004");
        if (retValue!=null && retValue.intValue() == 0) return;                      
        
        // provjera da li su popunjena obavezna polja na 2. i 3. ekranu
        if (provjeri_nekeUserData()) {
            ra.showMessage("infclt1"); 
            return;             
        }

        if (!(ra.isRequiredFilled())) {
            return; 
        }

        // razdvojiti one za koje se RBA prihvatljivost postavlja rucno od onih za koje se postavlja automatski
        // Milka, 05.06.2009 - ako je dozvoljen rucni upis RBA prihvatljivosti ne postavlja se nikakav default  
        // Milka, 22.10.2009 - dodan rucni unos za depozite i police osiguranja        
        String code = (String) ra.getAttribute("ColWorkListLDB", "code");
        if (!(code.equalsIgnoreCase("VOZI") || code.equalsIgnoreCase("PLOV") ||
                code.equalsIgnoreCase("POKR") || code.equalsIgnoreCase("ZALI") || code.equalsIgnoreCase("DION") ||
                code.equalsIgnoreCase("OBVE") || code.equalsIgnoreCase("UDJE") || code.equalsIgnoreCase("UDJP") ||
                code.equalsIgnoreCase("ZAPI") || code.equalsIgnoreCase("GARA") || code.equalsIgnoreCase("CASH") ||
                code.equalsIgnoreCase("INSP") || code.equalsIgnoreCase("CESI") || code.equalsIgnoreCase("ZADU"))) {

            BigDecimal col_cat_id = (BigDecimal)ra.getAttribute("ColWorkListLDB", "col_cat_id");   
            BigDecimal col_typ_id = (BigDecimal)ra.getAttribute(CollHeadLDB, "COL_TYPE_ID");               
            String low_eligibility = (String)ra.getAttribute(CollHeadLDB, "KolLow_txtEligibility");
            Date maturity_date = (Date)ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeDepUnti");
            String upisana_hipoteka = (String)ra.getAttribute(CollHeadLDB, "Coll_txtRecLop"); 
            String first_call = (String)ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtFirstCall");
            String cesija_naplata = (String)ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaNaplata");
            
            String rba_eligibility = coll_util.chk_RBA_eligibility_for_all(col_cat_id, col_typ_id, low_eligibility, 
                    maturity_date, upisana_hipoteka, first_call, null, null, null, cesija_naplata);

            ra.setAttribute(CollHeadLDB, "ColRba_txtEligibility", rba_eligibility);
        }
        
        
        if (code.equalsIgnoreCase("CASH")){
            // gotovinski depoziti        
            // provjeriti upisanu partiju depozita
            BigDecimal bank = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"cde_cus_id");
            if (bank != null && bank.compareTo(new BigDecimal("8218251")) == 0) {
                if (!coll_util.ctrlCashDepozitAccount((String) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtDepAcc"))) {
                    ra.showMessage("wrnclt144");
                    ra.setCursorPosition("Coll_txtDepAcc");
                    return;
                }  
            }           
        }       

        // postaviti flag da radim i potvrdu podataka
        // save_ver_aut_flag = 1 - spremanje i potvrda
        ra.setAttribute(CollHeadLDB, "save_ver_aut_flag", "1");       
        ra.setAttribute(CollHeadLDB, "USER_LOCK_IN", ra.getAttribute(CollHeadLDB, "USER_LOCK"));        

        BigDecimal col_hea_id = (BigDecimal) ra.getAttribute(CollHeadLDB,"COL_HEA_ID");
        // System.out.println ( "......col_hea_id " + col_hea_id);         
        if (ra.getScreenContext().equalsIgnoreCase("scr_change") && col_hea_id != null) {
            ra.setScreenContext("scr_update");
        }
        // System.out.println ( "......ctx " + ra.getScreenContext());  
        // System.out.println ( "......ctx " + ra.getScreenID());

        // update bez obzira da li je bilo promjene na ekranu            
        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_owner_bank")) {
            // update podataka (ctx=scr_update)             
            try {
                ra.executeTransaction();
                ra.showMessage("infclt3");
            } catch (VestigoTMException vtme) {
                error("CollSecPaperDialog -> update(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }               
        } else {
            // insert podataka (ctx = scr_change)       
            try { 
                ra.executeTransaction(); 
                ra.showMessage("infclt2");
            } catch (VestigoTMException vtme) {
                error("CollSecPaperDialog -> insert(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }      
            // promijeniti kontekst na update
            ra.setScreenContext("scr_update");
            if (vrsta.equalsIgnoreCase("CASH") || vrsta.equalsIgnoreCase("DION") || vrsta.equalsIgnoreCase("OBVE") ||
                    vrsta.equalsIgnoreCase("UDJE") || vrsta.equalsIgnoreCase("ZAPI") || vrsta.equalsIgnoreCase("UDJP"))             
                ra.invokeAction("owners");

            if (vrsta.equalsIgnoreCase("GARA"))             
                ra.invokeAction("loan_ben");
        }    
    } //confirm

    
    public boolean provjeri_nekeUserData() {
        boolean imaNepopunjenih = false;

        // System.out.println("provjera popunjenosti:"+vrsta+imaNepopunjenih); 
        // zajednicko svima  
        // osnovni ekran        
        String tip = (String) ra.getAttribute(CollHeadLDB,"Coll_txtCollTypeCode");
        String podtip = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtSecTypeCode");

        // prvi ekran       
        BigDecimal iznos_nominalni = (BigDecimal) ra.getAttribute(CollHeadLDB,"Coll_txtNomiValu");
        String valuta_nominalna = (String) ra.getAttribute(CollHeadLDB,"Coll_txtNmValCurr");
        Date datum_nom = (Date) ra.getAttribute(CollHeadLDB,"Coll_txtNomiDate");
        
        String low_elig = (String) ra.getAttribute(CollHeadLDB,"KolLow_txtEligibility");
        String rba_eligibility = (String) ra.getAttribute(CollHeadLDB, "ColRba_txtEligibility");
        String crm = (String)ra.getAttribute(CollHeadLDB, "SPEC_STATUS");     
        String crm_hnb = (String)ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCRMHnb");     

        if((tip == null) || (tip.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;                           
        if((low_elig == null) || (low_elig.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;      
        if((crm == null) || (crm.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;          
        if((crm_hnb == null) || (crm_hnb.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;           
        if((rba_eligibility == null) || (rba_eligibility.trim()).equalsIgnoreCase("")) imaNepopunjenih = true; 

        // samo za gotovinske depozite
        String banka = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtCdeBank");
        Date datum_zatvaranja = (Date) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtCdeDepUnti");
               
        BigDecimal amount = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtCdeAmount");
        String currency = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtCdeCurr");
        Date krajnji_rok_dospijeca = (Date) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtCashDepDateUntilFinal");
        String  depozit_acc = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtDepAcc");         
        if (vrsta.equalsIgnoreCase("CASH")) {
            if((banka == null) || (banka.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;               
            if((currency == null) || (currency.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;  
            if(depozit_acc==null || (depozit_acc.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;
            if((datum_zatvaranja == null) || (amount ==null) || (krajnji_rok_dospijeca == null)) imaNepopunjenih = true;           
            if((valuta_nominalna == null) || (valuta_nominalna.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;              
            if ((iznos_nominalni == null) || (datum_nom == null)) imaNepopunjenih = true; 
        }        

        if (vrsta.equalsIgnoreCase("GARA")) {
            imaNepopunjenih=!coll_util.checkIfFieldsAreFilled((new CollReqFields()).GetGaraReqData());
        } 

        // samo za zaduznice 
        String korisnik = (String) ra.getAttribute(CollHeadLDB, "Coll_txtCarrierRegisterNo");
        Integer broj_komada =(Integer) ra.getAttribute(CollHeadLDB, "Coll_txtNumOf");

        if (vrsta.equalsIgnoreCase("ZADU")) {
            if((korisnik == null) || (korisnik.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;            
            if((broj_komada == null) || (broj_komada.toString().trim()).equalsIgnoreCase("")) imaNepopunjenih = true;                     
        } 
        
        if (vrsta.equalsIgnoreCase("INSP")) {
            imaNepopunjenih=!coll_util.checkIfFieldsAreFilled((new CollReqFields()).GetInsuPolReqData());
        } 

        BigDecimal kolicina = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtNumOfSec");
        // samo za VRP-ove - obavezna je broj komada
        if (vrsta.equalsIgnoreCase("DION") || vrsta.equalsIgnoreCase("OBVE") || vrsta.equalsIgnoreCase("UDJE") ||
                vrsta.equalsIgnoreCase("ZAPI") || vrsta.equalsIgnoreCase("UDJP")) {
            if (kolicina == null) imaNepopunjenih = true;                        
        }
        // za udjele u poduzecu     
        String p_valuta = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtNominalAmountCur");
        BigDecimal p_inos1 = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtNominalAmountTot");
        BigDecimal p_iznos2 = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"Vrp_txtUdjeliUPod");
        String issuer_vrp = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtIssuerCode");
        String valutna_kl = (String) ra.getAttribute(CollSecPaperDialogLDB,"Vrp_txtValutnaKlauzula");
        if (vrsta.equalsIgnoreCase("UDJP")) {
            if((issuer_vrp == null) || (issuer_vrp.trim()).equalsIgnoreCase("")) imaNepopunjenih = true; 
            if((valutna_kl == null) || (valutna_kl.trim()).equalsIgnoreCase("")) imaNepopunjenih = true;             
            if((p_valuta == null) || (p_valuta.trim()).equalsIgnoreCase("")) imaNepopunjenih = true; 
            if (p_inos1 == null || p_iznos2 == null) imaNepopunjenih = true; 
        }       
        // System.out.println("3.tu sam prije return...:"+vrsta+imaNepopunjenih);  
       
        if (vrsta.equalsIgnoreCase("POKR")) {
            imaNepopunjenih=!coll_util.checkIfFieldsAreFilled((new CollReqFields()).GetMovableReqData());
        }
        
        if (vrsta.equalsIgnoreCase("CESI")) {
            imaNepopunjenih=!coll_util.checkIfFieldsAreFilled((new CollReqFields()).GetCesijeReqData());
        }
                
        if (vrsta.equalsIgnoreCase("PLOV")) {
            imaNepopunjenih=!coll_util.checkIfFieldsAreFilled((new CollReqFields()).GetVesselReqData());
        }
        
        if (vrsta.equalsIgnoreCase("VOZI")) {
            imaNepopunjenih=!coll_util.checkIfFieldsAreFilled((new CollReqFields()).GetVehiReqData());
        }  
        
        if (vrsta.equalsIgnoreCase("ZALI")) {
            imaNepopunjenih=!coll_util.checkIfFieldsAreFilled((new CollReqFields()).GetSuppliesReqData());
        }
        
        return imaNepopunjenih;  
    } 

    public void exit() { 
        // refresh akcijske liste za nositelja za kojeg je prikazan kolateral
        if (ra.getScreenID().endsWith("OF") || ra.getScreenID().endsWith("2") ||
                ra.getScreenID().endsWith("3") || ra.getScreenID().endsWith("4"))
        {
            ra.exitScreen();
            ra.exitScreen();
            ra.invokeAction("refresh");
        } else {
            ra.exitScreen();
            ra.invokeAction("refresh");         
        }
    }
    
    public void CollHistory() { 
        ra.loadScreen("CollHistory");
    }    

    public boolean Coll_txtCollTypeCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollHeadLDB, "Coll_txtCollTypeCode", "");
            ra.setAttribute(CollHeadLDB, "Coll_txtCollTypeName", "");
            ra.setAttribute(CollHeadLDB, "COL_TYPE_ID", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollPeriodRev", "");
            ra.setAttribute(CollHeadLDB, "Coll_txtCollMvpPonder", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollMvpPonderMin", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollMvpPonderMax", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollHnbPonder", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollHnbPonderMin", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollHnbPonderMax", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollRzbPonder", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollRzbPonderMin", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollRzbPonderMax", null);
            ra.setAttribute(CollHeadLDB, "dummySt", null);
            ra.setAttribute(CollHeadLDB, "Coll_txtCollAccounting", "");
            ra.setAttribute(CollHeadLDB, "Coll_txtCollAccountingName", "");
            ra.setAttribute(CollHeadLDB, "dummyDate", null);
            return true;
        } 

        if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
            ra.createLDB("CollateralTypeLookUpLDB");
        } 

        ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", ra.getAttribute("ColWorkListLDB","code"));  
        ra.setAttribute("CollateralTypeLookUpLDB", "CollateralCatId", ra.getAttribute("ColWorkListLDB","col_cat_id"));   
        ra.setAttribute(CollHeadLDB, "Coll_txtCollTypeName", "");

        LookUpRequest lookUpRequest = new LookUpRequest("KolPonderNewLookUp");  
        lookUpRequest.addMapping(CollHeadLDB, "COL_TYPE_ID", "col_typ_id");
        lookUpRequest.addMapping(CollHeadLDB, "Coll_txtCollTypeCode", "code");
        lookUpRequest.addMapping(CollHeadLDB, "Coll_txtCollTypeName", "name");
        lookUpRequest.addMapping(CollHeadLDB, "Coll_txtCollMvpPonderMin", "min_value");
        lookUpRequest.addMapping(CollHeadLDB, "Coll_txtCollMvpPonder", "dfl_value");
        lookUpRequest.addMapping(CollHeadLDB, "Coll_txtCollMvpPonderMax", "max_value");            

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        // provjeriti da li postoji ponder, ako ga nema javiti poruku
        BigDecimal dfl_ponder = (BigDecimal) ra.getAttribute(CollHeadLDB, "Coll_txtCollMvpPonder");
        if (dfl_ponder == null) {
            ra.showMessage("wrnclt167");
            return false;
        }  

        vrsta = (String) ra.getAttribute("ColWorkListLDB","code");
            
        if (vrsta.equalsIgnoreCase("OBVE") || vrsta.equalsIgnoreCase("ZAPI")) {
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtISIN","");
            ra.setAttribute(CollSecPaperDialogLDB,"ISIN_ID",null);
        }
        // za depozite napuniti banku ako je odabrana vrsta depozita u RBA
        // i odraditi kontrolu tipa depozita i valute           
        if (vrsta.equalsIgnoreCase("CASH")) {
            String tip = (String) ra.getAttribute(CollHeadLDB, "Coll_txtCollTypeCode");
            BigDecimal valuta = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "cd_cur_id");
            if (!(coll_cmp_util.ctrlCashDepVal(tip, valuta))) {
                ra.showMessage("wrnclt100");
                return false; 
            }

            String bank = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtCdeRegNo");
            BigDecimal cou_id = (BigDecimal)  ra.getAttribute(CollSecPaperDialogLDB,"residency_cou_id");  
            String eco_sec = (String) ra.getAttribute(CollSecPaperDialogLDB,"eco_sec");   

            if (!(coll_cmp_util.ctrlCashDepBank(tip, bank, eco_sec, cou_id))) { 
                ra.showMessage("wrnclt101");
                return false; 
            } 
        }
        // validirati izracun 
        if (vrsta.equalsIgnoreCase("CASH")) {
            ra.setCursorPosition("Coll_txtCdeAmount");
            ra.invokeValidation("Coll_txtCdeAmount");
            ra.setCursorPosition("Coll_txtCdeAmount");              
        }
        if (vrsta.equalsIgnoreCase("ZADU")) {
            ra.setCursorPosition("Coll_txtStockAmount");
            ra.invokeValidation("Coll_txtStockAmount");
            ra.setCursorPosition("Coll_txtContractTypeCode");
            //:'(
            //ako je zaduznica COL_TYPE_ID != 80777 potrebno je onemoguciti unos podatka Broj sporazuma jer je unos moguc samo za taj tip 
            //povezano i sa _SE funkcijom ekrana zaduznica
            //kod promjene ovaj lookup nije moguce pozvati pa nije potrebno provjeravati je li context promjene
            if( ((BigDecimal)ra.getAttribute(CollHeadLDB, "COL_TYPE_ID")).compareTo(BigDecimal.valueOf(80777))==0 ){
                ra.setContext("Coll_txtDesc","fld_plain"); 
                coll_util.enableFields(new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"}, 0);
                coll_util.setRequired(new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"}, true);
                coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2HNB", "D");
                coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2IRB", "D");
            }else{
                ra.setContext("Coll_txtDesc","fld_protected"); 
                ra.setAttribute(CollHeadLDB, "Coll_txtDesc", "");
                coll_util.enableFields(new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"}, 2);
                coll_util.clearFields(CollHeadLDB,new String[] {"Coll_txtB2HNB","Coll_txtB2IRB"});
            }
        }
        if (vrsta.equalsIgnoreCase("INSP")) {
            // postaviti obavezna polja za unos ovisno o tipu   - paziti na detalje, sve protektirano mora biti     
            if (!ra.getScreenContext().equalsIgnoreCase("scr_detail") && !ra.getScreenContext().equalsIgnoreCase("scr_detail_co"))
                coll_cmp_util.setInsPolCtx(ra);             

            ra.setCursorPosition("Coll_txtAcumBuyValue");
            ra.invokeValidation("Coll_txtAcumBuyValue");
            ra.setCursorPosition("Coll_txtIpConRegNo");                 
        }
        // za garancije ako je garancija izdana od drzave obavezno se upisuje broj garancije
        if (vrsta.equalsIgnoreCase("GARA")) {
            // vrsta garancije odredjuje B1 prihvatljivost
            // dodano za b2 i n2 irb za LoC                
            String rba_eligibility = (String) ra.getAttribute(CollHeadLDB,"ColRba_txtEligibility");
            BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
            BigDecimal col_typ_id = (BigDecimal) ra.getAttribute(CollHeadLDB, "COL_TYPE_ID");
            String low_eligibility = (String)ra.getAttribute(CollHeadLDB, "KolLow_txtEligibility");
            String upisana_hipoteka = (String) ra.getAttribute(CollHeadLDB,"Coll_txtRecLop"); 
            String first_call = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtFirstCall");

            if (col_typ_id.compareTo(new BigDecimal("35777")) == 0 || col_typ_id.compareTo(new BigDecimal("73777")) == 0) {
                rba_eligibility = coll_util.chk_RBA_eligibility_for_all(col_cat_id, col_typ_id, low_eligibility, 
                        null, upisana_hipoteka, first_call, null, null, null, null); 
                ra.setAttribute(CollHeadLDB,"ColRba_txtEligibility", rba_eligibility);  
            }
            
            String tip=ra.getAttribute(CollHeadLDB, "Coll_txtCollTypeCode");
            
            if(tip.equals("4BLETOCO") || tip.equals("4SLETOCO")  || tip.equals("4UNFURPA")){
                ra.setAttribute(CollHeadLDB, "Coll_txtContractTypeCode", "9");                
            }else if(tip.equals("4BJAMSTP") || tip.equals("4BJAMSTF")){
                ra.setAttribute(CollHeadLDB, "Coll_txtContractTypeCode", "6");
            }else{
                ra.setAttribute(CollHeadLDB, "Coll_txtContractTypeCode", "5");
            }
            ra.setAttribute(CollHeadLDB, "Coll_txtContractTypeDesc", "");
            ra.invokeValidation("Coll_txtContractTypeCode");

            coll_util.setGuarNumCtx(ra);
            // validirati izracun               
            ra.invokeValidation("Kol_txtGuarAmount");
            //ra.setCursorPosition("Coll_txtGuarIssRegNo");                   
        }
        // za pokretnine, postaviti odgovarajuci kontekst ovisno o izabranom tipu
        if (vrsta.equalsIgnoreCase("POKR")) {
            coll_util.setMovTypCtx(ra);            
        }
        // za ZALIHE, postaviti odgovarajuci kontekst ovisno o izabranom tipu
        if (vrsta.equalsIgnoreCase("ZALI")) {
            coll_util.setSupplyTypCtx(ra);
        }          

        if (vrsta.equalsIgnoreCase("CESI")) {
            // vrsta utjece na prihvatljivosti  
            String tip_c = (String) ra.getAttribute(CollHeadLDB, "Coll_txtCollTypeCode");
            BigDecimal valuta_c = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "cd_cur_id");
            if (!(coll_cmp_util.ctrlCashDepVal(tip_c, valuta_c))) {
                ra.showMessage("wrnclt170");
                return false; 
            }
        } 
        
        String tip=ra.getAttribute(CollHeadLDB, "Coll_txtCollTypeCode");
        if((vrsta.equalsIgnoreCase("POKR") && !tip.equalsIgnoreCase("2AVI") && !tip.equalsIgnoreCase("2STR")) || vrsta.equalsIgnoreCase("ZALI")){
            if(!ra.isLDBExists("EconomicLifeLDB")){
                ra.createLDB("EconomicLifeLDB");
            } 
            coll_util.clearFields("EconomicLifeLDB", new String[]{"col_cat_id","col_typ_id","col_sub_id","economic_life"});
            try {
                ra.setAttribute("EconomicLifeLDB", "col_cat_id", ra.getAttribute("ColWorkListLDB", "col_cat_id"));
                ra.setAttribute("EconomicLifeLDB", "col_typ_id", ra.getAttribute(CollHeadLDB, "COL_TYPE_ID"));
                ra.setAttribute("EconomicLifeLDB", "col_sub_id", ra.getAttribute(CollSecPaperDialogLDB, "SEC_TYP_ID"));            
                ra.executeTransaction();
                ra.setAttribute("CollCommonDataLDB", "Coll_txtEconomicLife", ra.getAttribute("EconomicLifeLDB", "economic_life"));
            } catch (Exception e) {
                
            }             
        }
        return true;             

    }//Coll_txtCollTypeCode_FV

    public boolean Coll_txtOwnerRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = CollHeadLDB;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtOwnerRegisterNo",null);
            ra.setAttribute(ldbName,"Coll_txtOwnerName",null);
            ra.setAttribute(ldbName,"COLL_CUS_ID",null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtOwnerName")) {
            ra.setAttribute(ldbName, "Coll_txtOwnerRegisterNo", "");
        } else if (ra.getCursorPosition().equals("Coll_txtOwnerRegisterNo")) {
            ra.setAttribute(ldbName, "Coll_txtOwnerName", "");
            //ra.setCursorPosition(2);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "Coll_txtOwnerName") != null){
            d_name = (String) ra.getAttribute(ldbName, "Coll_txtOwnerName");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtOwnerRegisterNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtOwnerRegisterNo");
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

        if (ra.getCursorPosition().equals("Coll_txtOwnerRegisterNo")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtOwnerRegisterNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Coll_txtOwnerName"));

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

        ra.setAttribute(ldbName, "COLL_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtOwnerRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtOwnerName", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;

    }//Coll_txtOwnerRegisterNo_FV   

    public boolean Coll_txtCarrierRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String code = (String) ra.getAttribute("ColWorkListLDB", "code");
        String ldbName = "";
        if (code.equalsIgnoreCase("MJEN")) {
            ldbName = "CollBoeDialogLDB";
        } else {
            ldbName = CollHeadLDB;
        }

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtCarrierRegisterNo",null);
            ra.setAttribute(ldbName,"Coll_txtCarrierName",null);
            ra.setAttribute(ldbName,"CUS_ID",null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtCarrierName")) {
            ra.setAttribute(ldbName, "Coll_txtCarrierRegisterNo", "");
        } else if (ra.getCursorPosition().equals("Coll_txtCarrierRegisterNo")) {
            ra.setAttribute(ldbName, "Coll_txtCarrierName", "");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "Coll_txtCarrierName") != null){
            d_name = (String) ra.getAttribute(ldbName, "Coll_txtCarrierName");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtCarrierRegisterNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtCarrierRegisterNo");
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

        if (ra.getCursorPosition().equals("Coll_txtCarrierRegisterNo")) 
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

        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute(ldbName, "Coll_txtCarrierRegisterNo"));
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute(ldbName, "Coll_txtCarrierName")); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", null); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", null); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", null); 

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

        ra.setAttribute(ldbName, "CUS_ID", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtCarrierRegisterNo", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtCarrierName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));         

        return true;
    } 

    // validacija procjenitelja 
    //TODO kada se svi ekrani promjene trebali bi koristit validaciju Coll_Estimator_FV i onda se ova i Coll_txtEUsePersonId_FV validacija moze obrisati
    public boolean Coll_txtEUseIdLogin_FV(String elementName, Object elementValue, Integer lookUpType) {       
        if (elementValue == null || ((String) elementValue).equals("")) {
            coll_util.clearFields(CollHeadLDB, new String[]{"Coll_txtEUseIdLogin","Coll_txtEUseIdLoginOIB","Coll_txtEUseIdName","REAL_EST_EUSE_ID"});
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtEUseIdName")) {
            coll_util.clearFields(CollHeadLDB, new String[]{"Coll_txtEUseIdLogin"});
        } else if (ra.getCursorPosition().equals("Coll_txtEUseIdLogin")) {
            coll_util.clearFields(CollHeadLDB, new String[]{"Coll_txtEUseIdName"});
        }

        String d_name = "";
        if (ra.getAttribute(CollHeadLDB, "Coll_txtEUseIdName") != null){
            d_name = (String) ra.getAttribute(CollHeadLDB, "Coll_txtEUseIdName");
        }
        String d_register_no = "";
        if (ra.getAttribute(CollHeadLDB, "Coll_txtEUseIdLogin") != null){
            d_register_no = (String) ra.getAttribute(CollHeadLDB, "Coll_txtEUseIdLogin");
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

        if (ra.getCursorPosition().equals("Coll_txtEUseIdName")) {
            ra.setAttribute(CollHeadLDB, "Coll_txtEUseIdLogin", "");
        } else if (ra.getCursorPosition().equals("Coll_txtEUseIdLogin")) {
            ra.setAttribute(CollHeadLDB, "Coll_txtEUseIdName", "");
        }

        if (ra.getCursorPosition().equals("Coll_txtEUseIdLogin")) 
            ra.setCursorPosition(2);        

        if (ra.isLDBExists("CustColAllLookUpLDB")) {//ovo ne dirati                                                                                    
            ra.setAttribute("CustColAllLookUpLDB", "cus_id", null);                                                                       
            ra.setAttribute("CustColAllLookUpLDB", "register_no", "");                                                                    
            ra.setAttribute("CustColAllLookUpLDB", "code", "");                                                                           
            ra.setAttribute("CustColAllLookUpLDB", "name", "");                                                                           
            ra.setAttribute("CustColAllLookUpLDB", "add_data_table", "");                                                                 
            ra.setAttribute("CustColAllLookUpLDB", "cus_typ_id", null);                                                                   
            ra.setAttribute("CustColAllLookUpLDB", "cus_sub_typ_id", null);                                                               
            ra.setAttribute("CustColAllLookUpLDB", "eco_sec", null);                                                                      
            ra.setAttribute("CustColAllLookUpLDB", "residency_cou_id", null);  
            ra.setAttribute("CustColAllLookUpLDB", "ModuleName", null);            
            ra.setAttribute("CustColAllLookUpLDB", "in_status", "");
            ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_id", "");   
            ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_flag", "LEG2");
            ra.setAttribute("CustColAllLookUpLDB", "tax_number", ""); 
        } else {                                                                                                                         
            ra.createLDB("CustColAllLookUpLDB"); 
            ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_flag", "LEG2");
        }             
        ra.setAttribute("CustColAllLookUpLDB", "register_no", ra.getAttribute(CollHeadLDB, "Coll_txtEUseIdLogin"));
        ra.setAttribute("CustColAllLookUpLDB", "name", ra.getAttribute(CollHeadLDB, "Coll_txtEUseIdName")); 
        ra.setAttribute("CustColAllLookUpLDB", "code", null); 

        LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp"); //ni ovo ne dirati
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_id", "cus_id");                                                            
        lookUpRequest.addMapping("CustColAllLookUpLDB", "register_no", "register_no");                                                  
        lookUpRequest.addMapping("CustColAllLookUpLDB", "code", "code");                                                                
        lookUpRequest.addMapping("CustColAllLookUpLDB", "name", "name");                                                                
        lookUpRequest.addMapping("CustColAllLookUpLDB", "add_data_table", "add_data_table");                                            
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_typ_id", "cus_typ_id");                                                    
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");                                            
        lookUpRequest.addMapping("CustColAllLookUpLDB", "eco_sec", "eco_sec");                                                          
        lookUpRequest.addMapping("CustColAllLookUpLDB", "residency_cou_id", "residency_cou_id"); 
        lookUpRequest.addMapping("CustColAllLookUpLDB", "tax_number", "tax_number");

        try {                                                                                                                            
            ra.callLookUp(lookUpRequest);                                                                                                  
        } catch (EmptyLookUp elu) {                                                                                                      
            ra.showMessage("err012");                                                                                                      
            return false;                                                                                                                  
        } catch (NothingSelected ns) {                                                                                                   
            return false;                                                                                                                  
        }                    
        ra.setAttribute(CollHeadLDB, "REAL_EST_EUSE_ID", ra.getAttribute("CustColAllLookUpLDB", "cus_id"));
        ra.setAttribute(CollHeadLDB, "Coll_txtEUseIdLogin", ra.getAttribute("CustColAllLookUpLDB", "register_no"));
        ra.setAttribute(CollHeadLDB, "Coll_txtEUseIdName", ra.getAttribute("CustColAllLookUpLDB", "name"));
        ra.setAttribute(CollHeadLDB, "Coll_txtEUseIdLoginOIB", ra.getAttribute("CustColAllLookUpLDB", "tax_number"));  

        return true;
    }
    
    public boolean Coll_Estimator_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            coll_util.clearFields(CollSecPaperDialogLDB, new String[]{"Coll_txtEUsePersonId","Coll_txtEUsePersonName","Coll_txtEUsePersonCusId","Coll_txtEUsePersonIdOIB"});
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtEUsePersonName")) {
            coll_util.clearFields(CollSecPaperDialogLDB, new String[]{"Coll_txtEUsePersonId"});
        } else if (ra.getCursorPosition().equals("Coll_txtEUsePersonId")) {
            coll_util.clearFields(CollSecPaperDialogLDB, new String[]{"Coll_txtEUsePersonName"});
        }

        String d_name = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName") != null){
            d_name = (String) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName");
        }
        String d_register_no = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId") != null){
            d_register_no = (String) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId");
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

        if (ra.getCursorPosition().equals("Coll_txtEUsePersonId")) ra.setCursorPosition(2);        

        if (ra.isLDBExists("CustColAllLookUpLDB")) {//ovo ne dirati                                                                                    
            ra.setAttribute("CustColAllLookUpLDB", "cus_id", null);                                                                       
            ra.setAttribute("CustColAllLookUpLDB", "register_no", "");                                                                    
            ra.setAttribute("CustColAllLookUpLDB", "code", "");                                                                           
            ra.setAttribute("CustColAllLookUpLDB", "name", "");                                                                           
            ra.setAttribute("CustColAllLookUpLDB", "add_data_table", "");                                                                 
            ra.setAttribute("CustColAllLookUpLDB", "cus_typ_id", null);                                                                   
            ra.setAttribute("CustColAllLookUpLDB", "cus_sub_typ_id", null);                                                               
            ra.setAttribute("CustColAllLookUpLDB", "eco_sec", null);                                                                      
            ra.setAttribute("CustColAllLookUpLDB", "residency_cou_id", null);  
            ra.setAttribute("CustColAllLookUpLDB", "ModuleName", null);            
            ra.setAttribute("CustColAllLookUpLDB", "in_status", "");
            ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_id", "");   
            ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_flag", "CIT");
            ra.setAttribute("CustColAllLookUpLDB", "tax_number", ""); 
        } else {                                                                                                                         
            ra.createLDB("CustColAllLookUpLDB"); 
            ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_flag", "CIT");
        }             
        ra.setAttribute("CustColAllLookUpLDB", "register_no", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId"));
        ra.setAttribute("CustColAllLookUpLDB", "name", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName")); 
        ra.setAttribute("CustColAllLookUpLDB", "code", null); 

        LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp"); //ni ovo ne dirati
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_id", "cus_id");                                                            
        lookUpRequest.addMapping("CustColAllLookUpLDB", "register_no", "register_no");                                                  
        lookUpRequest.addMapping("CustColAllLookUpLDB", "code", "code");                                                                
        lookUpRequest.addMapping("CustColAllLookUpLDB", "name", "name");                                                                
        lookUpRequest.addMapping("CustColAllLookUpLDB", "add_data_table", "add_data_table");                                            
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_typ_id", "cus_typ_id");                                                    
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");                                            
        lookUpRequest.addMapping("CustColAllLookUpLDB", "eco_sec", "eco_sec");                                                          
        lookUpRequest.addMapping("CustColAllLookUpLDB", "residency_cou_id", "residency_cou_id"); 
        lookUpRequest.addMapping("CustColAllLookUpLDB", "tax_number", "tax_number");

        try {                                                                                                                            
            ra.callLookUp(lookUpRequest);                                                                                                  
        } catch (EmptyLookUp elu) {                                                                                                      
            ra.showMessage("err012");                                                                                                      
            return false;                                                                                                                  
        } catch (NothingSelected ns) {                                                                                                   
            return false;                                                                                                                  
        }                    
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonCusId", ra.getAttribute("CustColAllLookUpLDB", "cus_id"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId", ra.getAttribute("CustColAllLookUpLDB", "register_no"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName", ra.getAttribute("CustColAllLookUpLDB", "name"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonIdOIB", ra.getAttribute("CustColAllLookUpLDB", "tax_number"));

        return true;
    }

    // valuta procjene   
    public boolean Coll_txtEstnCurr_FV(String ElName, Object ElValue, Integer LookUp) {

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollHeadLDB, "Coll_txtEstnCurr", "");                        
            ra.setAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID", null);                               
            return true;                                                                                 
        }

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   
        lookUpRequest.addMapping(CollHeadLDB, "REAL_EST_NM_CUR_ID", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollHeadLDB, "Coll_txtEstnCurr", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }     
        ra.setAttribute(CollHeadLDB, "Coll_txtNmValCurr",ra.getAttribute(CollHeadLDB, "Coll_txtEstnCurr"));     
        return true;     
    } // Coll_txtEstnCurr_FV

    
    public boolean Coll_txtNmValCurr_FV (String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollHeadLDB, "Coll_txtNmValCurr", "");                        
            ra.setAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID", null);                               
            return true;                                                                                 
        }

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");                  
        lookUpRequest.addMapping(CollHeadLDB, "REAL_EST_NM_CUR_ID", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollHeadLDB, "Coll_txtNmValCurr", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }      

        ra.setAttribute(CollHeadLDB, "Coll_txtEstnCurr",ra.getAttribute(CollHeadLDB, "Coll_txtNmValCurr"));

        return true;
    } //Coll_txtNmValCurr_FV     

    // vrsta vrijednosnice,pokretnine,zaliha,umjetnine,dragocjenosti,plovila,police osiguranja  
    public boolean Coll_txtSecTypeCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "");
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "");
            ra.setAttribute(CollSecPaperDialogLDB, "SEC_TYP_ID", null);
            ra.setAttribute(CollHeadLDB, "dummySt", null);

            // kod vozila obrisati i podgrupu i pondere 
            if (vrsta.equalsIgnoreCase("VOZI")) {               
                ra.setAttribute(CollSecPaperDialogLDB, "Vehi_txtVehSubCode", "");                        
                ra.setAttribute(CollSecPaperDialogLDB, "Vehi_txtVehSubDesc", "");                        
                ra.setAttribute(CollSecPaperDialogLDB, "veh_subgr_id", null);     
            }

            return true;
        } 

        String lookup_name = "";
        BigDecimal ipIcId = null;
        if (vrsta.equalsIgnoreCase("POKR")) {
            if (ra.getAttribute(CollHeadLDB, "COL_TYPE_ID") != null) {
                ipIcId = (BigDecimal) ra.getAttribute(CollHeadLDB, "COL_TYPE_ID");
            }else{
                ra.showMessage("wrnclt129");
                return false;
            }     
            lookup_name = "KolSubPonderNewLookUp";

        } else if (vrsta.equalsIgnoreCase("ZALI")) {
            lookup_name = "SupplyTypeLookUp";           
        } 
        else if (vrsta.equalsIgnoreCase("UMJE")) {
            lookup_name = "ArtTypeLookUp";          
        } 
        else if (vrsta.equalsIgnoreCase("ZLAT")) {
            lookup_name = "PrecTypeLookUp";         
        }
        else if (vrsta.equalsIgnoreCase("PLOV")) {
            lookup_name = "KolSubPonderNewLookUp";
        }
        else if (vrsta.equalsIgnoreCase("INSP")) {
            if (!ra.isLDBExists("InsPolTypeLookUpLDB")) {
                ra.createLDB("InsPolTypeLookUpLDB");
            } 
            lookup_name = "InsPolTypeLookUp";   

            if (ra.getAttribute(CollSecPaperDialogLDB, "IP_CUS_ID") != null) {
                ipIcId = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "IP_CUS_ID");
            }else{
                ra.showMessage("wrncltzst3");
                return false;
            }       

            String tip = (String) ra.getAttribute(CollHeadLDB,"Coll_txtCollTypeCode");

            ra.setAttribute("InsPolTypeLookUpLDB", "polCompanyId", ipIcId);              
            ra.setAttribute("InsPolTypeLookUpLDB", "group4", vrsta);
            ra.setAttribute("InsPolTypeLookUpLDB", "group3", tip);    
        }
        else if (vrsta.equalsIgnoreCase("GARA")) {
            lookup_name = "SysCodeValueLookUp";
            ra.setAttribute(CollSecPaperDialogLDB, "SysCodId", "guarantee"); 
        }
        else if (vrsta.equalsIgnoreCase("ZADU")) {
            lookup_name = "UserCodeValueLookUp";
            if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
                ra.createLDB("UserCodeValueLookUpLDB");
            }               
            ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "loanstock_type");              
        } else if (vrsta.equalsIgnoreCase("VOZI")) {
            lookup_name = "KolSubPonderNewLookUp";                
        } else 
            lookup_name = "SecPapCTypeLookUp";          

        ra.setAttribute(CollHeadLDB, "dummySt", null);
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "");

        LookUpRequest lookUpRequest = new LookUpRequest(lookup_name); 
        if (vrsta.equalsIgnoreCase("POKR") || vrsta.equalsIgnoreCase("PLOV") || vrsta.equalsIgnoreCase("VOZI")) {
            if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
                ra.createLDB("CollateralTypeLookUpLDB");
            } 

            ra.setAttribute("CollateralTypeLookUpLDB", "CollateralCatId", ra.getAttribute("ColWorkListLDB","col_cat_id"));
            ra.setAttribute("CollateralTypeLookUpLDB", "CollateralTypId", ra.getAttribute(CollHeadLDB, "COL_TYPE_ID"));

            lookUpRequest.addMapping(CollSecPaperDialogLDB, "SEC_TYP_ID", "col_sub_id");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "code");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "name");  
            lookUpRequest.addMapping(CollHeadLDB, "Coll_txtCollMvpPonderMin", "min_value");
            lookUpRequest.addMapping(CollHeadLDB, "Coll_txtCollMvpPonder", "dfl_value");
            lookUpRequest.addMapping(CollHeadLDB, "Coll_txtCollMvpPonderMax", "max_value");        
        } else if (vrsta.equalsIgnoreCase("ZALI")) {
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "SEC_TYP_ID", "sup_typ_id");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "supt_type_code");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "supt_type_desc");                 
        } else if (vrsta.equalsIgnoreCase("UMJE")) {
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "SEC_TYP_ID", "wot_typ_id");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "wot_type_code");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "wot_type_desc");                  
        } else if (vrsta.equalsIgnoreCase("ZLAT")) {
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "SEC_TYP_ID", "prt_typ_id");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "prt_type_code");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "prt_type_desc");  
        } else if (vrsta.equalsIgnoreCase("INSP")) {
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "SEC_TYP_ID", "int_pol_type_id");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "int_pol_type_code");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "int_pol_type_name");  
            lookUpRequest.addMapping(CollHeadLDB, "dummySt", "int_pol_sh_sign");          
        } else if (vrsta.equalsIgnoreCase("GARA")) {
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "sys_code_value");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "sys_code_desc");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "SEC_TYP_ID", "sys_cod_val_id");
        } else if (vrsta.equalsIgnoreCase("ZADU")) {
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "SEC_TYP_ID", "use_cod_val_id");                      
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "use_code_value");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "use_code_desc");
        } else { 
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "SEC_TYP_ID", "sec_typ_id");
            lookUpRequest.addMapping(CollHeadLDB, "dummySt", "sec_type_code");
            lookUpRequest.addMapping(CollHeadLDB, "dummySt", "sec_type");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeCode", "sec_subtype");
            lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtSecTypeName", "sec_type_name");
        }
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false; 
        }

        // za police osiguranja
        // provjeriti b2 prihvatljivost police
        if (vrsta.equalsIgnoreCase("INSP")) {     
            String tip = (String) ra.getAttribute(CollHeadLDB,"Coll_txtCollTypeCode");    
            String podtip = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtSecTypeCode");            

            // za police osiguranja - B2 prihvatljiva je samo polica sa stednom komponentom - iskljuceno 12.11.2008-sve su prihvatljive                   
            if (tip != null && tip.trim().equalsIgnoreCase("7PLK")) {
                ra.setRequired("Coll_txtAcumBuyValue", true);               
            } else {
                if (podtip != null && (podtip.trim().substring(0,5)).equalsIgnoreCase("MOZOS")) {
                    ra.setRequired("Coll_txtAcumBuyValue", false);
                } else {
                    ra.setRequired("Coll_txtAcumBuyValue", false);  
                } 
            } 
        }         
        // za zaduznice: ako je bianco zaduznica - nije dozvoljen upis iznosa i valute
        // ako je obicna zaduznica - upis iznosa i valute je obavezan      
        if (vrsta.equalsIgnoreCase("ZADU")) {
            coll_cmp_util.setLoanStockCtx(ra);
        } 

        // preracunati ponderiranu i raspolozivu vrijednost        
        if (vrsta.equalsIgnoreCase("VOZI")) {
            coll_util.getPonderAndRestAmount(ra);
        } 
        //za vozila, zalihe, plovila, pokretnine 
        if(vrsta.equalsIgnoreCase("VOZI") || vrsta.equalsIgnoreCase("POKR") || vrsta.equalsIgnoreCase("ZALI") || vrsta.equalsIgnoreCase("PLOV")){
            if(!ra.isLDBExists("EconomicLifeLDB")){
                ra.createLDB("EconomicLifeLDB");
            } 
            coll_util.clearFields("EconomicLifeLDB", new String[]{"col_cat_id","col_typ_id","col_sub_id","economic_life"});
            try {
                ra.setAttribute("EconomicLifeLDB", "col_cat_id", ra.getAttribute("ColWorkListLDB", "col_cat_id"));
                ra.setAttribute("EconomicLifeLDB", "col_typ_id", ra.getAttribute(CollHeadLDB, "COL_TYPE_ID"));
                ra.setAttribute("EconomicLifeLDB", "col_sub_id", ra.getAttribute(CollSecPaperDialogLDB, "SEC_TYP_ID"));            
                ra.executeTransaction();
                ra.setAttribute("CollCommonDataLDB", "Coll_txtEconomicLife", ra.getAttribute("EconomicLifeLDB", "economic_life"));
            } catch (Exception e) {
                
            }
        }
        return true;        
    }//Coll_txtSecTypeCode_FV   

    // izdavatelj vrjednosnice  
    public boolean Coll_txtIssuerCode_FV(String elementName, Object elementValue, Integer lookUpType) {

        String ldbName = CollSecPaperDialogLDB;
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtIssuerCode","");
            ra.setAttribute(ldbName,"Coll_txtIssuerName","");
            ra.setAttribute(ldbName,"ISSUER_ID",null);

            ra.setAttribute(ldbName,"Vrp_txtRatingIssuer","");
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtIssuerCode")) {
            ra.setAttribute(ldbName, "Coll_txtIssuerName", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtIssuerName")) {
            ra.setAttribute(ldbName, "Coll_txtIssuerCode", "");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtIssuerCode") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtIssuerCode");
            d_register_no = d_register_no.trim();
        }

        if ( d_register_no.length() > 0 && d_register_no.length() < 4) {
            ra.showMessage("wrn366");
            return false;
        }

        //JE LI zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }

        String d_name = "";
        if (ra.getAttribute(ldbName,"Coll_txtIssuerName") !=null) {
            d_name = (String) ra.getAttribute(ldbName,"Coll_txtIssuerName");
            d_name = d_name.trim();
        }

        if (d_name.length() > 0 && d_name.length() < 4) {
            ra.showMessage("wrn366");
            return false;
        }

        //JE LI zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_name)) {
            ra.showMessage("wrn367");
            return false;
        }       

        if (ra.isLDBExists("GuarIssueLDB")) {
            ra.setAttribute("GuarIssueLDB", "cus_id", null);
            ra.setAttribute("GuarIssueLDB", "register_no", "");
            ra.setAttribute("GuarIssueLDB", "name", "");
            ra.setAttribute("GuarIssueLDB", "cocunat", "");
            ra.setAttribute("GuarIssueLDB", "rating", "");
            ra.setAttribute("GuarIssueLDB", "residency_cou_id", null);
            ra.setAttribute("GuarIssueLDB", "residency_cou_num", "");
            ra.setAttribute("GuarIssueLDB", "residency_cou_name", "");
            ra.setAttribute("GuarIssueLDB", "issuer_status", "");
            ra.setAttribute("GuarIssueLDB", "tax_number", "");
            ra.setAttribute("GuarIssueLDB", "rating_out", "");
            ra.setAttribute("GuarIssueLDB", "mlt_rating", "");
        } else {
            ra.createLDB("GuarIssueLDB");
        }

        ra.setAttribute("GuarIssueLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtIssuerCode"));
        ra.setAttribute("GuarIssueLDB", "name", ra.getAttribute(ldbName, "Coll_txtIssuerName"));

        LookUpRequest lookUpRequest = new LookUpRequest("GuarIssueLookUp");
        lookUpRequest.addMapping("GuarIssueLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("GuarIssueLDB", "register_no", "register_no");
        lookUpRequest.addMapping("GuarIssueLDB", "name", "name");
        lookUpRequest.addMapping("GuarIssueLDB", "rating", "rating");
        lookUpRequest.addMapping("GuarIssueLDB", "cocunat", "cocunat");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_id", "residency_cou_id");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_num", "residency_cou_num");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_name", "residency_cou_name");
        lookUpRequest.addMapping("GuarIssueLDB", "issuer_status", "issuer_status");
        lookUpRequest.addMapping("GuarIssueLDB", "tax_number", "tax_number");
        lookUpRequest.addMapping("GuarIssueLDB", "rating_out", "rating_out");
        lookUpRequest.addMapping("GuarIssueLDB", "mlt_rating", "mlt_rating");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        // provjeriti da li je komitent aktivan
        String status = (String) ra.getAttribute("GuarIssueLDB", "issuer_status");
        if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("2")) {
        } else {    
            // komitent nije aktivan            
            ra.showMessage("wrnclt145");
            return false;
        }   

        ra.setAttribute(ldbName, "ISSUER_ID", ra.getAttribute("GuarIssueLDB", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtIssuerCode", ra.getAttribute("GuarIssueLDB", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtIssuerName", ra.getAttribute("GuarIssueLDB", "name"));
        ra.setAttribute(ldbName,"Vrp_txtRatingIssuer", ra.getAttribute("GuarIssueLDB", "rating"));
        ra.setAttribute(ldbName,"Kol_txtOIB", ra.getAttribute("GuarIssueLDB", "tax_number"));

        return true;
    }//Coll_txtIssuerCode_FV

    public boolean Coll_txtGuaranteeCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = CollSecPaperDialogLDB;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtGuaranteeCode",null);
            ra.setAttribute(ldbName,"Coll_txtGuaranteeName",null);
            ra.setAttribute(ldbName,"GUARANTEE_ID",null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtGuaranteeName")) {
            ra.setAttribute(ldbName, "Coll_txtGuaranteeCode", "");
        } else if (ra.getCursorPosition().equals("Coll_txtGuaranteeCode")) {
            ra.setAttribute(ldbName, "Coll_txtGuaranteeName", "");
            //ra.setCursorPosition(2);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "Coll_txtGuaranteeName") != null){
            d_name = (String) ra.getAttribute(ldbName, "Coll_txtGuaranteeName");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtGuaranteeCode") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtGuaranteeCode");
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

        if (ra.getCursorPosition().equals("Coll_txtGuaranteeCode")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtGuaranteeCode"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Coll_txtGuaranteeName"));

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

        ra.setAttribute(ldbName, "GUARANTEE_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtGuaranteeCode", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtGuaranteeName", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;

    }//Coll_txtGuaranteeCode_FV 


    public boolean Coll_txtSecPapCountryCode_FV(String elementName, Object elementValue, Integer lookUpType) {

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtSecPapCountryCode", "");
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtSecPapCountryName", "");
            ra.setAttribute(CollSecPaperDialogLDB, "COU_ID", null);
            return true;
        }
        if (ra.getCursorPosition().equals("FPaym_txtDebitCusCountryCode")) {
            ra.setAttribute(CollSecPaperDialogLDB, "FPaym_txtDebitCusCountryName", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("FPaym_txtDebitCusCountryName")) {
            ra.setAttribute(CollSecPaperDialogLDB, "FPaym_txtDebitCusCountryCode", "");
        }

        ra.setAttribute(CollHeadLDB, "dummySt", null);
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtSecPapCountryName", "");

        LookUpRequest request = new LookUpRequest("CountryLookUp");
        request.addMapping(CollSecPaperDialogLDB, "COU_ID", "cou_id");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtSecPapCountryCode", "shortcut_num");
        request.addMapping(CollHeadLDB, "dummySt", "shortcut_char");      
        request.addMapping(CollHeadLDB, "dummySt", "cou_iso_code");           
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtSecPapCountryName", "name");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }   
        return true;
    } //Coll_txtSecPapCountryCode_FV

    public boolean Coll_txtIssueAmountCur_FV(String ElName, Object ElValue, Integer LookUp) {


        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIssueAmountCur", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "CUR_ID", null);   

            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "NOMINAL_CUR_ID", null);            

            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtTradeCur", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "TRD_CUR_ID", null);               

            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEstnCurr", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "REAL_EST_NM_CUR_ID", null);         
            return true;                                                                                 
        }

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "CUR_ID", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtIssueAmountCur", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }              

        // valuta nominalne vrijednosti jednaka je valuti izdanja vrijednosnice (NOMINAL_CUR_ID=CUR_ID)     
        ra.setAttribute(CollSecPaperDialogLDB, "NOMINAL_CUR_ID", ra.getAttribute(CollSecPaperDialogLDB,"CUR_ID"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur", ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtIssueAmountCur"));    

        // valuta trgovanja jednaka je valuti izdanja vrijednosnice (TRD_CUR_ID=CUR_ID)     
        ra.setAttribute(CollSecPaperDialogLDB, "TRD_CUR_ID", ra.getAttribute(CollSecPaperDialogLDB,"CUR_ID"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtTradeCur", ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtIssueAmountCur"));            

        //valuta PROCIJENJENE nominalne vrijednosti jednaka je valuti izdanja vrijednosnice (REAL_EST_NM_CUR_ID=CUR_ID)             
        ra.setAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID", ra.getAttribute(CollSecPaperDialogLDB,"CUR_ID"));
        ra.setAttribute(CollHeadLDB, "Coll_txtEstnCurr", ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtIssueAmountCur"));      
        return true;     
    } // Coll_txtIssueAmountCur_FV


    public boolean Coll_txtIssueAmount_FV() {
        // kontrola velicine izdanja
        if (!(ctrlIssueAmount())) {
            ra.showMessage("wrn468");
            return false;
        }
        return true; 
    }

    public boolean Coll_txtNumOfSecIssued_FV() {
        // kontrola velicine izdanja
        if (!(ctrlIssueAmount())) {
            ra.showMessage("wrn468");
            return false; 
        }
        // kontrola posjedujuceg i ukupnog broja        
        if (!(ctrlNumOfSec())) {
            ra.showMessage("wrn469");
            return false; 
        }       
        return true;        
    }

    public boolean Coll_txtNominalAmount_FV(String ElName, Object ElValue, Integer LookUp) {
        // izracunati iznos nominale u kn
        // izracunati ukupni iznos nominale = kolicina * nominala   
        // izracunati ukupni iznos nominale u kn = kolicina * nominala u kn         
        if (ElValue == null ||((BigDecimal) ElValue).compareTo(new BigDecimal ("0.00")) == 0) { 
            //          ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNumOfSec", null);
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmount", null);
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountKn", null);

            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtNominalAmountTot",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtNominalAmountTotKn", null);

            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtMarketPriceFo", null);
            return true;
        }                               

        coll_util.countVrpAmounts(ra);

        return true;
    }

    public boolean ctrlIssueAmount() {
        //  kontrola: velicina izdanja = broj komada u izdanju * nominalna vrijednost jedne vrijednosnice
        //  issueAmount = issueNum * nomAmountOne
        //  ako su upisani i izracunati iznos razliciti - poruka o gresci
        BigDecimal oneAmount;
        BigDecimal issueAmount;
        BigDecimal nomAmountOne;
        BigDecimal countedIssueAmount;
        BigDecimal issueNumDec;

        oneAmount = new BigDecimal("1.000000000000000000000000000000");
        oneAmount.setScale(29);
        issueAmount = new BigDecimal("0.000000000000000000000000000000");
        issueAmount.setScale(29);
        nomAmountOne = new BigDecimal("0.000000000000000000000000000000");
        nomAmountOne.setScale(29);
        countedIssueAmount = new BigDecimal("0.000000000000000000000000000000");
        countedIssueAmount.setScale(29);        
        issueNumDec = new BigDecimal("0.000000000000000000000000000000");
        issueNumDec.setScale(29);           

        issueAmount = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtIssueAmount");
        nomAmountOne = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtNominalAmount");
        Integer issueNum = (Integer) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtNumOfSecIssued"); 

        String issueNumstr = null;

        if (!(issueNum == null)) {
            issueNumstr = issueNum.toString();
            issueNumDec = new BigDecimal(issueNumstr); // broj komada u big decimal .....
        }       

        // racunam velicinu izdanja ako su upisana sva tri podatka      
        if (coll_util.isAmount(issueAmount) && coll_util.isAmount(nomAmountOne) && coll_util.isAmount(issueNumDec)) {

            countedIssueAmount = nomAmountOne.multiply(issueNumDec);
            countedIssueAmount=countedIssueAmount.divide(oneAmount, 2, BigDecimal.ROUND_HALF_UP);   

            // usporediti upisani i izracunati iznos            
            if (issueAmount.compareTo(countedIssueAmount) != 0){
                return false;
            }
        }       

        return true;
    }

    public boolean ctrlNumOfSec() {
        Integer issueNumber = (Integer) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNumOfSecIssued");
        Integer ownNumber = (Integer) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNumOfSec");

        if (coll_util.isNumber(ownNumber) && coll_util.isNumber(issueNumber)) {         
            if (ownNumber.compareTo(issueNumber) > 0){   // ako je posjedujuci broj > ukupni broj komada
                return false;
            }       
        }
        return true;
    }

    public boolean Coll_txtNumOfSec_FV  (String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null ||((BigDecimal) ElValue).compareTo(new BigDecimal ("0.00")) == 0) {          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNumOfSec", null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtNominalAmountTot",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtNominalAmountTotKn",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtTotalMarketValue",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtTotalMarketValueKn",null);
            return true;
        }   

        // ako su udjeli u fondu dozvoljen je upis decimala, inace nije     
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
        BigDecimal dec_number = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNumOfSec");
        if (!(col_cat_id.compareTo(new BigDecimal("622223")) == 0)) {
            if (coll_util.chek_is_there_decimal(dec_number)) {
                // poruka o gresci jer decimale nisu dozvoljene     
                ra.showMessage("wrnclt152");
                return false;
            }  
        } 
        coll_util.countVrpAmounts(ra);      
        return true;        
    }

    public boolean Coll_txtMarketPrice_FV(String ElName, Object ElValue, Integer LookUp) {
        //  izracunati iznos jedinicne trzisne u kn
        //  izracunati ukupni iznos trzisne valuti  = kolicina * trzisna    
        //  izracunati ukupni iznos trzisne u kn = kolicina * trzisna u kn          
        if (ElValue == null ||((BigDecimal) ElValue).compareTo(new BigDecimal ("0.00")) == 0) { 

            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtMarketPrice", null);
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtMarketPriceKn", null);

            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtTotalMarketValue",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtTotalMarketValueKn", null);

            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtMarketPriceFo", null);
            return true;
        }                               

        coll_util.countVrpAmounts(ra);
        return true;
    }

    public boolean Vrp_txtFond_FV(String ElName, Object ElValue, Integer LookUp) {
        //      izracunati iznos jedinicne trzisne u kn
        //      izracunati ukupni iznos trzisne valuti  = kolicina * trzisna    
        //      izracunati ukupni iznos trzisne u kn = kolicina * trzisna u kn          
        if (ElValue == null ||((BigDecimal) ElValue).compareTo(new BigDecimal ("0.00")) == 0) { 

            ra.setAttribute(CollSecPaperDialogLDB, "Vrp_txtFond", null);
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtMarketPriceKn", null);

            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtTotalMarketValue",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtTotalMarketValueKn", null);

            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtMarketPriceFo", null);
            return true;
        }                               
        coll_util.countVrpAmounts(ra);
        return true;        
    }
    
    public boolean Coll_txtTMarketValueDate_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtTMarketValueDate", null);                        
            ra.setAttribute(CollHeadLDB, "Coll_txtNomiDate", null);   
            return true;                                                                                 
        }

        ra.setAttribute(CollHeadLDB, "Coll_txtNomiDate", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtTMarketValueDate"));    
        ra.setAttribute(CollHeadLDB, "Coll_txtNepoDate", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtTMarketValueDate"));    
        ra.setAttribute(CollHeadLDB, "Coll_txtAcouDate", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtTMarketValueDate"));    

        return true;
    }   

    public boolean Coll_txtAccruedInterest_FV () {
        // racunam Coll_txtTotalMarketValue   = principal + accrued_interest  
        BigDecimal accruedInterest = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtAccruedInterest");
        BigDecimal principal= (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtPrincipal");
        BigDecimal totalMarketValue=null;

        if (coll_util.isAmount(principal) && coll_util.isAmount(accruedInterest)) {
            totalMarketValue = coll_util.setSumaTwoBigDec(principal, accruedInterest);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtTotalMarketValue",totalMarketValue);
            // sa totalMarketValue punim i real_est_nomi_value i nepo_value
            ra.setAttribute(CollHeadLDB, "Coll_txtNomiValu", totalMarketValue);
            ra.setAttribute(CollHeadLDB, "Coll_txtNepoValue", totalMarketValue);
            ra.setAttribute(CollHeadLDB, "Coll_txtEstnValu", totalMarketValue);
            // racunam acou_value i avail_value       
        }   

        // racunam acou_value 
        BigDecimal nepoValue = (BigDecimal) ra.getAttribute(CollHeadLDB, "Coll_txtNepoValue");
        BigDecimal collMvpPonder = (BigDecimal) ra.getAttribute(CollHeadLDB,"Coll_txtCollMvpPonder");
        BigDecimal acouValue = null;

        if (coll_util.isAmount(nepoValue) && coll_util.isAmount(collMvpPonder)) {
            acouValue = coll_util.setProductTwoBigDec(nepoValue,collMvpPonder);
            ra.setAttribute(CollHeadLDB,"Coll_txtAcouValue",acouValue);           
        }       
        // avail_value
        BigDecimal thirdRightNom = (BigDecimal) ra.getAttribute(CollHeadLDB,"Coll_txtThirdRightInNom");
        BigDecimal hfsValue = (BigDecimal) ra.getAttribute(CollHeadLDB,"Coll_txtHfsValue");
        BigDecimal availValue = null;

        if (coll_util.isAmount(acouValue) && coll_util.isAmount(thirdRightNom) && coll_util.isAmount(hfsValue)) {
            availValue = acouValue.subtract(thirdRightNom).subtract(hfsValue);
            ra.setAttribute(CollHeadLDB,"Coll_txtAvailValue",availValue);                 
        }               

        return true;
    }   

    public BigDecimal setMarketPrice(BigDecimal marketPriceFo, BigDecimal oneNominalAmount) {
        //    izracun trzisne cijene u %        
        BigDecimal oneHundredAmount;
        BigDecimal oneAmount;
        BigDecimal marketPriceFo1;
        BigDecimal oneNominalAmount1;
        BigDecimal marketPrice;

        oneHundredAmount = new BigDecimal("100.000000000000000000000000000000");
        oneHundredAmount.setScale(29);
        oneAmount = new BigDecimal("1.000000000000000000000000000000");
        oneAmount.setScale(29);
        marketPriceFo1 = new BigDecimal("0.000000000000000000000000000000");
        marketPriceFo1.setScale(29);
        oneNominalAmount1 = new BigDecimal("0.000000000000000000000000000000");
        oneNominalAmount1.setScale(29);
        marketPrice = new BigDecimal("0.000000000000000000000000000000");
        marketPrice.setScale(29);       

        marketPriceFo1 = marketPriceFo;
        oneNominalAmount1 = oneNominalAmount;

        // System.out.println("marketPriceFo1 : "+marketPriceFo1); 
        // System.out.println("oneNominalAmount1 : "+oneNominalAmount1);               
        marketPrice = marketPriceFo1.divide(oneNominalAmount1, 29, BigDecimal.ROUND_HALF_UP);
        // System.out.println("marketPrice : "+marketPrice);   
        marketPrice = marketPrice.multiply(oneHundredAmount);
        // System.out.println("marketPrice : "+marketPrice);   
        marketPrice = marketPrice.divide(oneAmount, 8, BigDecimal.ROUND_HALF_UP);
        // System.out.println("marketPrice : "+marketPrice);   
        return marketPrice;
    }

    public boolean Coll_txtMarketPriceDate_FV() {
        // ne smije biti veæi od current date       
        Date price_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtMarketPriceDate");
        // datum mora biti manji ili jednak current date
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (price_date == null || current_date == null) 
            return true;

        if ((current_date).before(price_date)) {
            ra.showMessage("wrnclt121");
            return false;
        }
        return true;        
    }

    public boolean Coll_txtPriceDaily_FV (String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPriceDaily", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "PRICE_DAILY_QUOTED", null); 

            return true;                                                                                 
        }

        ra.setAttribute(CollSecPaperDialogLDB, "SysCodId", "price_daily_quoted");
        ra.setAttribute(CollHeadLDB, "dummySt", null);

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtPriceDaily", "sys_code_value");
        request.addMapping(CollHeadLDB, "dummySt", "sys_code_desc");
        request.addMapping(CollSecPaperDialogLDB, "PRICE_DAILY_QUOTED", "sys_cod_val_id");

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

    public boolean Coll_txtRepoInd_FV (String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtRepoInd", null);
        }

        LookUpRequest request = new LookUpRequest("YesNoEnglish2LookUp");       
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtRepoInd", "Vrijednosti");

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

    public boolean Coll_txtPayTypeCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayTypeCode", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayTypeDesc", ""); 
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Coll_txtPayTypeCode")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayTypeDesc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtPayTypeDesc")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayTypeCode", "");
        }
        ra.setAttribute(CollSecPaperDialogLDB, "SysCodId", "pay_type");
        ra.setAttribute(CollHeadLDB, "dummySt", null);

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtPayTypeCode", "sys_code_value");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtPayTypeDesc", "sys_code_desc");
        request.addMapping(CollHeadLDB, "dummySt", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) { 
            return false;
        }                                                              
        return true;     
    } // Coll_txtPayTypeCode_FV 

    public boolean Coll_txtPayFreqCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayFreqCode", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayFreqDesc", ""); 
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Coll_txtPayFreqCode")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayFreqDesc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtPayFreqDesc")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPayFreqCode", "");
        }
        ra.setAttribute(CollSecPaperDialogLDB, "SysCodId", "pay_freq");
        ra.setAttribute(CollHeadLDB, "dummySt", null);

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtPayFreqCode", "sys_code_value");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtPayFreqDesc", "sys_code_desc");
        request.addMapping(CollHeadLDB, "dummySt", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }                                                              
        return true;     
    } // Coll_txtPayFreqCode_FV 

    public boolean Coll_txtIntRateTypeCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntRateTypeCode", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntRateTypeDesc", ""); 
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Coll_txtIntRateTypeCode")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntRateTypeDesc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtIntRateTypeDesc")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntRateTypeCode", "");
        }
        ra.setAttribute(CollSecPaperDialogLDB, "SysCodId", "int_rate_type");
        ra.setAttribute(CollHeadLDB, "dummySt", null);

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtIntRateTypeCode", "sys_code_value");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtIntRateTypeDesc", "sys_code_desc");
        request.addMapping(CollHeadLDB, "dummySt", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }                                                              
        return true;     
    } // Coll_txtIntRateTypeCode_FV     

    public boolean Coll_txtIntCalcTypeCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntCalcTypeCode", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntCalcTypeDesc", ""); 
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Coll_txtIntCalcTypeCode")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntCalcTypeDesc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtIntCalcTypeDesc")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntCalcTypeCode", "");
        }
        ra.setAttribute(CollSecPaperDialogLDB, "SysCodId", "int_type");
        ra.setAttribute(CollHeadLDB, "dummySt", null);

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtIntCalcTypeCode", "sys_code_value");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtIntCalcTypeDesc", "sys_code_desc");
        request.addMapping(CollHeadLDB, "dummySt", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }                                                              
        return true;     
    }   //Coll_txtIntCalcTypeCode_FV

    public boolean Coll_txtIntCalcMethCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntCalcMethCode", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntCalcMethDesc", ""); 
            return true;                                                                                 
        }
        if (ra.getCursorPosition().equals("Coll_txtIntCalcMethCode")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntCalcMethDesc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtIntCalcMethDesc")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIntCalcMethCode", "");
        }
        ra.setAttribute(CollSecPaperDialogLDB, "SysCodId", "int_method");
        ra.setAttribute(CollHeadLDB, "dummySt", null);

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtIntCalcMethCode", "sys_code_value");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtIntCalcMethDesc", "sys_code_desc");
        request.addMapping(CollHeadLDB, "dummySt", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }                                                               
        return true;     
    }   //Coll_txtIntCalcMethCode_FV

    public boolean Coll_txtNominalAmountCur_FV(String ElName, Object ElValue, Integer LookUp) {
        
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "NOMINAL_CUR_ID", null);   
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNomAmoCur", "");   
            return true;                                                                                 
        }

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "NOMINAL_CUR_ID", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }             

        // ako su udjeli u poduzecu, validirati currency clause     
        if (vrsta.equalsIgnoreCase("UDJP")) {
            String valutna_klauzula = (String) ra.getAttribute(CollSecPaperDialogLDB, "Vrp_txtValutnaKlauzula");  
            BigDecimal cur_id = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "NOMINAL_CUR_ID");


            if (coll_util.chkValutnaKlauzula(valutna_klauzula, cur_id)) {
                ra.setAttribute(CollSecPaperDialogLDB, "Vrp_txtValutnaKlauzula", null);
                ra.setAttribute(CollSecPaperDialogLDB,"Vrp_txtValutnaKlauzulaDsc","");    
                if (cur_id.equals(new BigDecimal("63999"))) 
                    ra.showMessage("wrnclt142");
                else 
                    ra.showMessage("wrnclt148");
                return true;
            }           
        }

        // napuniti Coll_txtEstnCurr, Coll_txtNmValCurr
        ra.setAttribute(CollHeadLDB, "Coll_txtEstnCurr", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur"));                        
        ra.setAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID", ra.getAttribute(CollSecPaperDialogLDB, "NOMINAL_CUR_ID"));   
        ra.setAttribute(CollHeadLDB, "Coll_txtNmValCurr", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur"));  
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtNomAmoCur", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtNominalAmountCur"));         

        return true;       
    } // Coll_txtNominalAmountCur_FV

    public boolean Coll_txtRefMarketCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtRefMarketCode", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtRefMarketDesc", ""); 
            ra.setAttribute(CollSecPaperDialogLDB, "MARKET_TYPE", null);                               
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Coll_txtRefMarketCode")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtRefMarketDesc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtRefMarketDesc")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtRefMarketCode", "");
        }

        ra.setAttribute(CollHeadLDB, "dummySt", null);

        LookUpRequest request = new LookUpRequest("VrpStockLookUp");
        request.addMapping(CollSecPaperDialogLDB, "MARKET_TYPE", "sto_exc_id");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtRefMarketCode", "code");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtRefMarketDesc", "name");
        request.addMapping(CollHeadLDB, "dummySt", "short_name");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }                                                              
        return true;     
    } // Coll_txtRefMarketCode_FV

    public boolean Coll_txtTradeCur_FV(String ElName, Object ElValue, Integer LookUp) {

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtTradeCur", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "TRD_CUR_ID", null);                               
            return true;                                                                                 
        }

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");          
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "TRD_CUR_ID", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtTradeCur", "code_char");
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
    } // Coll_txtTradeCur_FV

    public boolean Coll_txtIssueDate_FV(String ElName, Object ElValue, Integer LookUp) {
        // datum izdavanja mora biti manji od datuma dospijeca      
        Date issue_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIssueDate");
        Date maturity_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtMaturityDate");
        if (issue_date == null || maturity_date == null) 
            return true;

        if ((maturity_date).before(issue_date)) {
            ra.showMessage("wrn470");
            return false;
        }
        return true;
    } 

    public boolean Coll_txtMaturityDate_FV(String ElName, Object ElValue, Integer LookUp) {
        // datum dospijeca mora biti veci od datuma izdavanja       
        Date issue_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIssueDate");
        Date maturity_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtMaturityDate");
        if (issue_date == null || maturity_date == null) 
            return true;

        if ((maturity_date).before(issue_date)) {
            ra.showMessage("wrn470");
            return false;
        }
        return true;        
    }       

    public boolean Vessel_txtVehPowerKW_FV(String ElName, Object ElValue, Integer LookUp) {
        return true;
    }

    public boolean Vessel_txtVehKmTrav_FV(String ElName, Object ElValue, Integer LookUp) {
        return true;
    }   

    public boolean Vessel_txtHarbour_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Vessel_txtHarbour", "");
            ra.setAttribute(CollSecPaperDialogLDB, "HAR_OFF_ID", null);
            ra.setAttribute(CollSecPaperDialogLDB, "Vessel_txtHarbourSec", "");
            ra.setAttribute(CollSecPaperDialogLDB, "HAR_SEC_ID", null);

            return true;
        }  

        LookUpRequest lookUpRequest = new LookUpRequest("HarbourLookUp");                       
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "HAR_OFF_ID", "har_off_id");           
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Vessel_txtHarbour", "hao_office");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }                                                                                              
        return true;     

    }//Vessel_txtHarbour_FV 

    public boolean Vessel_txtHarbourSec_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Vessel_txtHarbourSec", "");
            ra.setAttribute(CollSecPaperDialogLDB, "HAR_SEC_ID", null);

            return true;
        } 
        // da bi se odabrala ispostava lucke kapetanije mora biti odabrana lucka kapetanija     
        if (ra.getAttribute(CollSecPaperDialogLDB, "HAR_OFF_ID") == null) {
            ra.setAttribute(CollSecPaperDialogLDB, "Vessel_txtHarbourSec", "");
            return true;
        } 

        LookUpRequest lookUpRequest = new LookUpRequest("HarbourSecLookUp");                       
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "HAR_SEC_ID", "har_sec_id");           
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Vessel_txtHarbourSec", "has_office");        

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }                                                                                              
        return true;     

    }//Vessel_txtHarbourSec_FV      

    // Coll_txtCdeRegNo_FV
    // Milka 21.08.2006 - umjesto na tbl. BANKE koristim lookup na CUSTOMER-a
    public boolean Coll_txtCdeRegNo_FV(String ElName, Object elementValue, Integer LookUp) {
        String ldbName = CollSecPaperDialogLDB;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtCdeRegNo",null);
            ra.setAttribute(ldbName,"Coll_txtCdeSwiftAdd",null);
            ra.setAttribute(ldbName,"Coll_txtCdeBank",null);
            ra.setAttribute(ldbName,"cde_cus_id",null);

            ra.setAttribute(ldbName,"residency_cou_id",null);   
            ra.setAttribute(ldbName,"eco_sec",null);    
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtCdeBank")) {
            ra.setAttribute(ldbName, "Coll_txtCdeRegNo", "");
        } else if (ra.getCursorPosition().equals("Coll_txtCdeRegNo")) {
            ra.setAttribute(ldbName, "Coll_txtCdeBank", "");            
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "Coll_txtCdeBank") != null){
            d_name = (String) ra.getAttribute(ldbName, "Coll_txtCdeBank");
        }           

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Coll_txtCdeRegNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Coll_txtCdeRegNo");
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

        if (ra.getCursorPosition().equals("Coll_txtCdeRegNo")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtCdeRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Coll_txtCdeBank"));

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

        // kontrola tipa i banke depozita     
        if (vrsta.equalsIgnoreCase("CASH")) {
            String tip = (String) ra.getAttribute(CollHeadLDB, "Coll_txtCollTypeCode");
            String bank = (String) ra.getAttribute("CustomerAllLookUpLDB","register_no");
            BigDecimal cou_id = (BigDecimal)  ra.getAttribute("CustomerAllLookUpLDB","residency_cou_id");   
            String eco_sec = (String) ra.getAttribute("CustomerAllLookUpLDB","eco_sec");    

            if (!(coll_cmp_util.ctrlCashDepBank(tip, bank, eco_sec, cou_id))) { 
                ra.showMessage("wrnclt101");
                return false; 
            } 
        }        

        ra.setAttribute(ldbName, "cde_cus_id", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "Coll_txtCdeRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "Coll_txtCdeBank", ra.getAttribute("CustomerAllLookUpLDB", "name"));
        ra.setAttribute(ldbName, "Coll_txtCdeSwiftAdd", ra.getAttribute("CustomerAllLookUpLDB", "code"));

        ra.setAttribute(ldbName, "residency_cou_id", ra.getAttribute("CustomerAllLookUpLDB", "residency_cou_id"));
        ra.setAttribute(ldbName, "eco_sec", ra.getAttribute("CustomerAllLookUpLDB", "eco_sec"));

        return true;
    }

    //  Coll_txtCdeSwiftAdd_FV
    public boolean Coll_txtCdeSwiftAdd_FV(String ElName, Object ElValue, Integer LookUp) {
        String ldbName = CollSecPaperDialogLDB;
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtCdeRegNo",null);
            ra.setAttribute(ldbName,"Coll_txtCdeSwiftAdd",null);
            ra.setAttribute(ldbName,"Coll_txtCdeBank",null);
            ra.setAttribute(ldbName,"cde_cus_id",null);
            return true;
        }

        if (!(ra.isLDBExists("BicRegLookUpLDB"))) {
            ra.createLDB("BicRegLookUpLDB");
        }

        ra.setAttribute("BicRegLookUpLDB", "bic_reg_id", null);
        ra.setAttribute("BicRegLookUpLDB", "institution_name1", null);
        ra.setAttribute("BicRegLookUpLDB", "institution_name2", null);
        ra.setAttribute("BicRegLookUpLDB", "city_heading", null);
        ra.setAttribute("BicRegLookUpLDB", "ph_address1", null);
        ra.setAttribute("BicRegLookUpLDB", "cou_id", null);
        ra.setAttribute("BicRegLookUpLDB", "shortcut_num", null);       
        ra.setAttribute("BicRegLookUpLDB", "name", null);

        ra.setAttribute("BicRegLookUpLDB","bic_reg_id", (String) ra.getAttribute(ldbName, "Coll_txtCdeSwiftAdd"));

        LookUpRequest lookUpRequest = new LookUpRequest("BicRegLookUp");
        lookUpRequest.addMapping("BicRegLookUpLDB", "bic_reg_id", "bic_reg_id");
        lookUpRequest.addMapping("BicRegLookUpLDB", "institution_name1", "institution_name1");
        lookUpRequest.addMapping("BicRegLookUpLDB", "institution_name2", "institution_name2");
        lookUpRequest.addMapping("BicRegLookUpLDB", "city_heading", "city_heading");
        lookUpRequest.addMapping("BicRegLookUpLDB", "ph_address1", "ph_address1");
        lookUpRequest.addMapping("BicRegLookUpLDB", "cou_id", "cou_id");
        lookUpRequest.addMapping("BicRegLookUpLDB", "shortcut_num", "shortcut_num");
        lookUpRequest.addMapping("BicRegLookUpLDB", "name", "name");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {

            return true;
        } catch (NothingSelected ns) {
            return true;
        }

        // kontrola tipa i banke depozita     
        if (vrsta.equalsIgnoreCase("CASH")) {
            String tip = (String) ra.getAttribute(CollHeadLDB, "Coll_txtCollTypeCode");
            String bank = (String) ra.getAttribute(CollSecPaperDialogLDB,"Coll_txtCdeRegNo");
            String swift = (String) ra.getAttribute("BicRegLookUpLDB","bic_reg_id");

            if (!(coll_cmp_util.ctrlCashDepBank(tip, bank, swift))) {
                ra.showMessage("wrnclt101");
                return false; 
            }
        }               

        ra.setAttribute(ldbName, "Coll_txtCdeSwiftAdd", ra.getAttribute("BicRegLookUpLDB", "bic_reg_id"));

        String bank_name = "";
        String bank_name1 = (String) ra.getAttribute("BicRegLookUpLDB", "institution_name1");
        String bank_name2 = (String) ra.getAttribute("BicRegLookUpLDB", "city_heading");
        String bank_name3 = (String) ra.getAttribute("BicRegLookUpLDB", "ph_address1");

        bank_name = bank_name1.trim() + " " + bank_name2.trim() + " " + bank_name3.trim();
        ra.setAttribute(ldbName, "Coll_txtCdeBank", bank_name);     

        return true;
    } 
    // Coll_txtCdeSwiftAdd_FV

    // Coll_txtCdeDepFrom
    public boolean Coll_txtCdeDepFrom_FV(String ElName, Object ElValue, Integer LookUp) {
        // datum otvaranja mora biti manji od datuma dospijeca        
        // datum mora biti manji ili jednak current date           
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date issue_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeDepFrom");
        Date maturity_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeDepUnti");
        if (issue_date == null || current_date == null) 
            return true;

        if ((current_date).before(issue_date)) {
            ra.showMessage("wrnclt121");
            return false;
        }       

        if (issue_date == null || maturity_date == null) 
            return true;

        if ((maturity_date).before(issue_date)) {
            ra.showMessage("wrnclt128");
            return false;
        }

        return true;
    }   

    public boolean Coll_txtCdeDepUnti_FV(String ElName, Object ElValue, Integer LookUp) {
        //  datum dospijeca mora biti veci od datuma izdavanja     
        // datum dospijeca mora biti veci ili jednak current date
        Date issue_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeDepFrom");
        Date maturity_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCdeDepUnti");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");

        // System.out.println("1......TU SAM zovem metodu  "+ maturity_date);  
        if (!(coll_cmp_util.setCashDepFinalDate(ra))) {
            ra.showMessage("wrnclt171");
            return false;
        }        

        if (maturity_date == null || current_date == null) 
            return true;

        if ((maturity_date).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }

        if (issue_date == null || maturity_date == null) 
            return true;

        if ((maturity_date).before(issue_date)) {
            ra.showMessage("wrnclt127");
            return false;
        }   

        return true;
    }   

    // Coll_txtCdeDepFrom   
    public boolean Coll_txtInsPolRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {       
        if (elementValue == null || ((String) elementValue).equals("")) {                                    
            coll_util.clearFields("CollSecPaperDialogLDB", new String[]{"Coll_txtInsPolRegNo", "Coll_txtInsPolName", "IP_CUS_ID",
                    "Coll_txtSecTypeCode","Coll_txtSecTypeName", "SEC_TYP_ID" });                                  
            return true;                                                                                 
        }          

        if (ra.getCursorPosition().equals("Coll_txtInsPolName")) {
            coll_util.clearFields("CollSecPaperDialogLDB", new String[]{ "Coll_txtInsPolRegNo", "IP_CUS_ID" });
            ra.setCursorPosition(1);
        } else if (ra.getCursorPosition().equals("Coll_txtInsPolRegNo")) {
            coll_util.clearFields("CollSecPaperDialogLDB", new String[]{ "Coll_txtInsPolName", "IP_CUS_ID" });           
            ra.setCursorPosition(2);
        }    
        coll_util.clearFields("CollSecPaperDialogLDB", new String[]{"SEC_TYP_ID","Coll_txtSecTypeCode","Coll_txtSecTypeName"});  
        
        if (ra.isLDBExists("InsuCompanyLDB")) {//ovo ne dirati 
            coll_util.clearField("InsuCompanyLDB", "insu_company_register_no");   
            coll_util.clearField("InsuCompanyLDB", "insu_company_name");   
        } else {                                                                                                                         
            ra.createLDB("InsuCompanyLDB");                                                                                         
        }
        ra.setAttribute("InsuCompanyLDB", "insu_company_register_no", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtInsPolRegNo"));
        ra.setAttribute("InsuCompanyLDB", "insu_company_name", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtInsPolName"));       
        
        LookUpRequest lu = new LookUpRequest("InsuCompanyOibLookUp");  
        
        lu.addMapping("InsuCompanyLDB", "insu_company_register_no", "ic_register_no");
        lu.addMapping("InsuCompanyLDB", "insu_company_oib", "tax_number"); 
        lu.addMapping("InsuCompanyLDB", "insu_company_name", "ic_name"); 
        lu.addMapping("InsuCompanyLDB", "insu_company_id", "ic_id");  
        lu.addMapping("InsuCompanyLDB", "insu_company_code", "ic_code");
        lu.addMapping("InsuCompanyLDB", "insu_company_cust_rating", "cust_rating");
        
        try {                                                                                            
            ra.callLookUp(lu);                                                                           
        } catch (EmptyLookUp elu) {                                                                      
            ra.showMessage("err012");                                                                    
            return false;                                                                                
        } catch (NothingSelected ns) {                                                                   
            return false;                                                                                
        } 
      
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtInsPolRegNo", ra.getAttribute("InsuCompanyLDB", "insu_company_register_no"));   
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtInsPolName", ra.getAttribute("InsuCompanyLDB", "insu_company_name"));  
        ra.setAttribute("CollSecPaperDialogLDB", "IP_CUS_ID", ra.getAttribute("InsuCompanyLDB", "insu_company_id"));
        
        ra.setAttribute("CollSecPaperDialogLDB", "CollInsuPolicyDialog_txtIpInternalRating", ra.getAttribute("InsuCompanyLDB", "insu_company_cust_rating"));
        return true;         
    }

    public boolean Coll_txtIpNomCurr_FV(String ElName, Object ElValue, Integer LookUp) {

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtIpNomCurr", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "IP_NOM_CUR_ID", null);                               
            return true;                                                                                 
        }

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");          
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "IP_NOM_CUR_ID", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtIpNomCurr", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }   
        ra.setAttribute(CollHeadLDB, "Coll_txtNmValCurr",ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtIpNomCurr"));  
        ra.setAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID", ra.getAttribute(CollSecPaperDialogLDB, "IP_NOM_CUR_ID"));        
        return true;     
    } // Coll_txtIpNomCurr_FV   

    // Coll_txtGuarDatnFrom_FV
    public boolean Coll_txtGuarDatnFrom_FV(String ElName, Object ElValue, Integer LookUp) {
        // datum otvaranja mora biti manji od datuma dospijeca
        // datum od kad vrijedi mora biti manji ili jednak current date
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date issue_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarDatnFrom");
        Date maturity_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarDatnUnti");

        if (issue_date == null || current_date == null) 
            return true;

        if ((current_date).before(issue_date)) {
            ra.showMessage("wrnclt121");
            return false;
        }           

        if (issue_date == null || maturity_date == null) 
            return true;

        if ((maturity_date).before(issue_date)) {
            ra.showMessage("wrnclt122");
            return false;
        }

        // racunam period vazenja garancije
        String period = coll_util.countGuarPeriod(issue_date, maturity_date);

        if (period.length() > 6) {
            ra.showMessage("wrnclt160");
            return false;
        }
        ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtGuarExpPeriod",period);
        return true;
    } 

    public boolean Coll_txtGuarDatnUnti_FV(String ElName, Object ElValue, Integer LookUp) {
        // datum dospijeca mora biti veci od datuma izdavanja 
        // mora biti veci od current date   
        // Milka, 10.06.2009 - mora biti manji ili jednak respiro datumu        
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date issue_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarDatnFrom");
        Date maturity_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarDatnUnti");
        Date respiro_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtRespiroDate");

        if (maturity_date == null || current_date == null) 
            return true;

        if ((maturity_date).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }

        if (issue_date == null || maturity_date == null) 
            return true;

        if ((maturity_date).before(issue_date)) {
            ra.showMessage("wrnclt122");
            return false;
        }
        if (respiro_date == null || maturity_date == null) { 
        } else {            
            if ((respiro_date).before(maturity_date)) {
                ra.showMessage("wrnclt162");
                return false;
            }        
        }
        // racunam period vazenja garancije       
        String period = coll_util.countGuarPeriod(issue_date, maturity_date);
        if (period.length() > 6) {
            ra.showMessage("wrnclt160");
            return false;
        }
        ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtGuarExpPeriod",period);          
        return true;        
    }// Coll_txtGuarDatnUnti_FV 

    // validacija respiro datuma
    public boolean Kol_txtRespiroDate_FV(String ElName, Object ElValue, Integer LookUp) {

        //Milka, 10.06.2009 - mora biti veci ili jednak datumu dopsijeca garancije        
        Date maturity_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarDatnUnti");
        Date respiro_date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtRespiroDate");

        if (respiro_date == null || maturity_date == null) 
            return true;         

        if ((respiro_date).before(maturity_date)) {
            ra.showMessage("wrnclt162");
            return false;
        }        

        return true;
    }// Coll_txtGuarDatnUnti_FV     

    public boolean Coll_txtGuarIssRegNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = CollSecPaperDialogLDB;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtGuarIssRegNo","");
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtGuarIssuer","");
            ra.setAttribute(CollSecPaperDialogLDB,"guar_issuer_id",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCocunut","");
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtRating","");
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssCouNum", "");
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssCountry", "");
            ra.setAttribute(CollSecPaperDialogLDB, "guar_cou_id", null);
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtExtRating", "");
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtOIB", "");
            ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtB2AssetCode", null);
            ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtB2AssetDesc", null);
            ra.setAttribute("CollSecPaperDialogLDB", "Coll_lblIssuerTypeCode", null);
            ra.setAttribute("CollSecPaperDialogLDB", "Coll_lblIssuerTypeDesc", null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtGuarIssuer"))
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtGuarIssRegNo", "");
        else if (ra.getCursorPosition().equals("Coll_txtGuarIssRegNo")) 
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssuer", "");

        String d_name = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssuer") != null){
            d_name = (String) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssuer");
        }

        String d_register_no = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssRegNo") != null){
            d_register_no = (String) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssRegNo");
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

        if (ra.getCursorPosition().equals("Coll_txtGuarIssRegNo")) 
            ra.setCursorPosition(2);

        if (ra.isLDBExists("GuarIssueLDB")) {
            ra.setAttribute("GuarIssueLDB", "cus_id", null);
            ra.setAttribute("GuarIssueLDB", "register_no", "");
            ra.setAttribute("GuarIssueLDB", "name", "");
            ra.setAttribute("GuarIssueLDB", "cocunat", "");
            ra.setAttribute("GuarIssueLDB", "rating", "");
            ra.setAttribute("GuarIssueLDB", "residency_cou_id", null);
            ra.setAttribute("GuarIssueLDB", "residency_cou_num", "");
            ra.setAttribute("GuarIssueLDB", "residency_cou_name", "");
            ra.setAttribute("GuarIssueLDB", "issuer_status", "");
            ra.setAttribute("GuarIssueLDB", "tax_number", "");
            ra.setAttribute("GuarIssueLDB", "rating_out", "");
            ra.setAttribute("GuarIssueLDB", "mlt_rating", "");
        } else {
            ra.createLDB("GuarIssueLDB");
        }

        ra.setAttribute("GuarIssueLDB", "register_no", ra.getAttribute(ldbName, "Coll_txtGuarIssRegNo"));
        ra.setAttribute("GuarIssueLDB", "name", ra.getAttribute(ldbName, "Coll_txtGuarIssuer"));
        ra.setAttribute("GuarIssueLDB", "retail_cust_flag", "N");
        
        // jamstvo fizicke osobe, izdavatelj moze biti i fizicka osoba
        if (((BigDecimal)ra.getAttribute(CollHeadLDB, "COL_TYPE_ID")) != null && ((BigDecimal)ra.getAttribute(CollHeadLDB, "COL_TYPE_ID")).equals(new BigDecimal("37777"))) {
            ra.setAttribute("GuarIssueLDB", "retail_cust_flag", "C");
        }

        LookUpRequest lookUpRequest = new LookUpRequest("GuarIssueLookUp");
        lookUpRequest.addMapping("GuarIssueLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("GuarIssueLDB", "register_no", "register_no");
        lookUpRequest.addMapping("GuarIssueLDB", "name", "name");
        lookUpRequest.addMapping("GuarIssueLDB", "rating", "rating");
        lookUpRequest.addMapping("GuarIssueLDB", "cocunat", "cocunat");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_id", "residency_cou_id");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_num", "residency_cou_num");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_name", "residency_cou_name");
        lookUpRequest.addMapping("GuarIssueLDB", "issuer_status", "issuer_status");
        lookUpRequest.addMapping("GuarIssueLDB", "tax_number", "tax_number");
        lookUpRequest.addMapping("GuarIssueLDB", "rating_out", "rating_out");
        lookUpRequest.addMapping("GuarIssueLDB", "mlt_rating", "mlt_rating");
        lookUpRequest.addMapping("GuarIssueLDB", "B2AssetCode","B2AssetCode"); 
        lookUpRequest.addMapping("GuarIssueLDB", "B2AssetDesc","B2AssetDesc"); 
        lookUpRequest.addMapping("GuarIssueLDB", "IssuerTypeCode","IssuerTypeCode"); 
        lookUpRequest.addMapping("GuarIssueLDB", "IssuerTypeDesc","IssuerTypeDesc"); 

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            if (((String)ra.getAttribute("GuarIssueLDB", "retail_cust_flag")).equals("C")) 
                ra.showMessage("wrnclt174");
            else    
                ra.showMessage("wrnclt161");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        // provjeriti da li je komitent aktivan
        String status = (String) ra.getAttribute("GuarIssueLDB", "issuer_status");
        if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("2")) {
        } else {    
            // komitent nije aktivan            
            ra.showMessage("wrnclt145");
            return false;
        } 
        
        ra.setAttribute(CollSecPaperDialogLDB, "guar_issuer_id", ra.getAttribute("GuarIssueLDB", "cus_id"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssRegNo", ra.getAttribute("GuarIssueLDB", "register_no"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssuer", ra.getAttribute("GuarIssueLDB", "name"));
        ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCocunut", ra.getAttribute("GuarIssueLDB", "cocunat"));
        ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtRating", ra.getAttribute("GuarIssueLDB", "rating_out"));

        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssCouNum", ra.getAttribute("GuarIssueLDB", "residency_cou_num"));
        ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtGuarIssCountry", ra.getAttribute("GuarIssueLDB", "residency_cou_name"));
        ra.setAttribute(CollSecPaperDialogLDB,"guar_cou_id", ra.getAttribute("GuarIssueLDB", "residency_cou_id"));
        ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtExtRating", ra.getAttribute("GuarIssueLDB", "mlt_rating"));
        ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtOIB", ra.getAttribute("GuarIssueLDB", "tax_number"));
        ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtB2AssetCode", ra.getAttribute("GuarIssueLDB", "B2AssetCode"));
        ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtB2AssetDesc", ra.getAttribute("GuarIssueLDB", "B2AssetDesc"));
        ra.setAttribute(CollSecPaperDialogLDB,"Coll_lblIssuerTypeCode", ra.getAttribute("GuarIssueLDB", "IssuerTypeCode"));
        ra.setAttribute(CollSecPaperDialogLDB,"Coll_lblIssuerTypeDesc", ra.getAttribute("GuarIssueLDB", "IssuerTypeDesc"));
        
        return true;
    }// Coll_txtGuarIssRegNo_FV

    // Coll_txtGuarIssCouNum_FV
    public boolean Coll_txtGuarIssCouNum_FV(String elementName, Object elementValue, Integer lookUpType) {

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssCouNum", "");
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssCountry", "");
            ra.setAttribute(CollSecPaperDialogLDB, "guar_cou_id", null);
            return true;
        }
        if (ra.getCursorPosition().equals("Coll_txtGuarIssCouNum")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssCountry", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtGuarIssCountry")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssCouNum", "");
        }

        ra.setAttribute(CollHeadLDB, "dummySt", null);
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtGuarIssCountry", "");

        LookUpRequest request = new LookUpRequest("CountryLookUp");
        request.addMapping(CollSecPaperDialogLDB, "guar_cou_id", "cou_id");
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtGuarIssCouNum", "shortcut_num");
        request.addMapping(CollHeadLDB, "dummySt", "shortcut_char");      
        request.addMapping(CollHeadLDB, "dummySt", "cou_iso_code");           
        request.addMapping(CollSecPaperDialogLDB, "Coll_txtGuarIssCountry", "name");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }   
        return true;
    } //Coll_txtGuarIssCouNum_FV    

    public boolean Coll_txtGuarLbPcRate_FV() {
        // % mora biti <= 100 
        BigDecimal hundred = new BigDecimal("100.00");
        BigDecimal percent = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtGuarLbPcRate");

        if (percent != null) {
            if (percent.compareTo(hundred) == 1 ) {
                // upisani % je veci od 100 - greska
                ra.showMessage("wrnclt40");
                return false;
            }
        } 

        return true;
    } //Coll_txtGuarLbPcRate_FV 

    // za zaduznice
    public boolean Coll_txtPlaceCode_FV(String elementName, Object elementValue, Integer LookUp){

        java.math.BigDecimal countyTypeId = new java.math.BigDecimal("5999.0");
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPlaceCode", "");
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPlaceName", "");
            ra.setAttribute(CollSecPaperDialogLDB, "isu_place_id", null);

            return true;
        }
        /**
         * brise staru vrijednost u txtNCounty ako se upisivala vrijednost u
         * txtCCounty i obratno, kako ne bi doslo do pogresnog povezivanja
         * sifre i imena mjesta kod poziva LookUp-a
         */
        if (ra.getCursorPosition().equals("Coll_txtPlaceName")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPlaceCode", "");
        } else if (ra.getCursorPosition().equals("Coll_txtPlaceCode")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtPlaceName", "");
        }  

        if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }

        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", countyTypeId);

        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "isu_place_id", "pol_map_id");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtPlaceCode", "code");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtPlaceName", "name");

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

    public boolean Coll_txtStockCurr_FV(String ElName, Object ElValue, Integer LookUp) {

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtStockCurr", "");                        
            ra.setAttribute(CollSecPaperDialogLDB, "stock_cur_id", null);                               
            return true;                                                                                 
        }

        ra.setAttribute(CollHeadLDB, "dummySt", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");          
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "stock_cur_id", "cur_id");           
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "code_num");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Coll_txtStockCurr", "code_char");
        lookUpRequest.addMapping(CollHeadLDB, "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }    
        // napuniti Coll_txtEstnCurr, Coll_txtNmValCurr
        ra.setAttribute(CollHeadLDB, "Coll_txtEstnCurr", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtStockCurr"));                        
        ra.setAttribute(CollHeadLDB, "REAL_EST_NM_CUR_ID", ra.getAttribute(CollSecPaperDialogLDB, "stock_cur_id"));   
        ra.setAttribute(CollHeadLDB, "Coll_txtNmValCurr",ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtStockCurr"));      
        return true;     
    } 

    // spremanje i postavljanje statusa za slanje na verifikaciju 
    public void confirm_coll() {       
        // provjera da li su popunjena obavezna polja na 2. i 3. ekranu
        if (provjeri_nekeUserData()) {
            ra.showMessage("infclt1"); 
            return;             
        }

        if (!(ra.isRequiredFilled())) {
            return; 
        }

        // pitanje da li stvarno zeli potvrditi podatke      
        Integer retValue = (Integer) ra.showMessage("col_qer004");
        if (retValue!=null && retValue.intValue() == 0) return;             

        //postaviti flag da radim i potvrdu podataka
        //save_ver_aut_flag = 1 - spremanje i potvrda
        ra.setAttribute(CollHeadLDB, "save_ver_aut_flag", "1");               
        ra.setAttribute(CollHeadLDB, "USER_LOCK_IN", ra.getAttribute(CollHeadLDB, "USER_LOCK"));        

        // update bez obzira da li je bilo promjene na ekranu            
        // update podataka (ctx=scr_update_aut)              
        try {
            ra.executeTransaction();
            ra.showMessage("infclt3");
        } catch (VestigoTMException vtme) {
            error("CollSecPaperDialog -> confirm_coll(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }               

    }//confirm_coll  

    //  potvrde collateral officer-a
    public void confirm_co() {       

        if (!(ra.isLDBExists("CollCoChgHistLDB"))) {
            ra.createLDB("CollCoChgHistLDB");
        }

        if((!(new SpecialAuthorityManager(ra)).checkSpecialAuthorityCodeForApplicationUser("aix", "COLL_OFF"))) {
            ra.showMessage("insufficientVerAuthority");
            return;
        }

        // pitanje da li stvarno zeli potvrditi podatke      
        Integer retValue = (Integer) ra.showMessage("col_qer004");
        if (retValue!=null && retValue.intValue() == 0) return;             

        //postavljanje atribute za insert u CO_CHG_HISTORY
        ra.setAttribute("ColWorkListLDB", "co_chg_use_id", null);  

        ra.setAttribute("ColWorkListLDB", "co_ind", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCoConfirm"));
        // potvrda CO samo ako nije co_ind= D-> ako je vec potvrden -> poruka
        // System.out.println("Ispis Coll_txtCoConfirm:"+ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCoConfirm").toString());  
        if(!ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtCoConfirm").toString().equalsIgnoreCase("D")){
            try {
                ra.executeTransaction();
                ra.showMessage("infclt3");
            } catch (VestigoTMException vtme) {
                error("CollSecPaperDialog -> confirm_coll(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }               
        }else{
            ra.showMessage("wrn_dist_rep21");
            return;
        }

        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCoConfirm",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirm"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCoConfirmUserId",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmUserId"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCoConfirmUserName",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmUserName"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtCoConfirmTime",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmTime"));

    }//confirm_coll  

    public boolean Kol_txtCRM_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollHeadLDB, "SPEC_STATUS", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");            
        request.addMapping(CollHeadLDB, "SPEC_STATUS", "Vrijednosti");

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


    public boolean Coll_txtWrnStatusCode_FV(String elementName, Object elementValue, Integer lookUpType){       
        if(elementValue==null || elementValue.equals("")){
            coll_util.clearFields(CollSecPaperDialogLDB, new String[]{"Coll_txtWrnStatusCode", "Coll_txtWrnStatusDesc"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Coll_txtWrnStatusCode")){
            coll_util.clearField(CollSecPaperDialogLDB, "Coll_txtWrnStatusDesc");
        }else if(ra.getCursorPosition().equals("Coll_txtWrnStatusDesc")){
            coll_util.clearField(CollSecPaperDialogLDB, "Coll_txtWrnStatusCode");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollSecPaperDialogLDB, "Coll_txtWrnStatusCode", "Coll_txtWrnStatusDesc", "ip_wrn_status");
        if(value==null) return false;
        
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtWrnStatusCode", value.sysCodeValue);
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtWrnStatusDesc", value.sysCodeDesc);  
        return true;
    }

    public boolean Coll_txtKmtStatusCode_FV(String elementName, Object elementValue, Integer lookUpType){
        if(elementValue==null || elementValue.equals("")){
            coll_util.clearFields(CollSecPaperDialogLDB, new String[]{"Coll_txtKmtStatusCode", "Coll_txtKmtStatusDesc"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Coll_txtKmtStatusCode")){
            coll_util.clearField(CollSecPaperDialogLDB, "Coll_txtKmtStatusDesc");
        }else if(ra.getCursorPosition().equals("Coll_txtKmtStatusDesc")){
            coll_util.clearField(CollSecPaperDialogLDB, "Coll_txtKmtStatusCode");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollSecPaperDialogLDB, "Coll_txtKmtStatusCode", "Coll_txtKmtStatusDesc", "ip_kmt_status");
        if(value==null) return false;
        
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtKmtStatusCode", value.sysCodeValue);
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtKmtStatusDesc", value.sysCodeDesc);  
        return true;
    }

    public boolean Kol_txtVrstaPotrazivanja_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanja", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanjaDsc", ""); 
            ra.setAttribute(CollSecPaperDialogLDB, "cesija_vrsta_id", null); 
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Kol_txtVrstaPotrazivanja")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanjaDsc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Kol_txtVrstaPotrazivanjaDsc")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanja", "");
        } 

        if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
            ra.createLDB("UserCodeValueLookUpLDB");
        }  
        ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "cesija_vrsta"); 
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanjaDsc", ""); 

        LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "cesija_vrsta_id", "use_cod_val_id");                        
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanja", "use_code_value");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Kol_txtVrstaPotrazivanjaDsc", "use_code_desc");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) { 
            return false; 
        }   

        BigDecimal cesija_vrsta_id = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "cesija_vrsta_id");
        // o vrsti potrazivanja ovisi kontekst nekih drugih polja na ekranu
        // System.out.println("1......TU SAM u metodi , racunam  "+cesija_vrsta_id);  
        coll_util.setCesijaFirstScrCtx(cesija_vrsta_id);

        return true;     
    }

    public boolean Kol_txtCesijaNaplata_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaNaplata", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       
        request.addMapping(CollSecPaperDialogLDB, "Kol_txtCesijaNaplata", "Vrijednosti");

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

    public boolean Kol_txtCesijaLimit_FV (String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaLimit", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       
        request.addMapping(CollSecPaperDialogLDB, "Kol_txtCesijaLimit", "Vrijednosti");

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

    public boolean Kol_txtCesijaRegres_FV (String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaRegres", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       
        request.addMapping(CollSecPaperDialogLDB, "Kol_txtCesijaRegres", "Vrijednosti");

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

    public boolean Kol_txtCesijaLista_FV (String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaLista", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       
        request.addMapping(CollSecPaperDialogLDB, "Kol_txtCesijaLista", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        String cesija_lista = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaLista");
        coll_util.setCesijaListaScrCtx(cesija_lista);

        return true;
    }
    
    // cesija - datum sklapanja, ne smije biti u buducnosti
    public boolean Kol_txtCesijaDate_FV () {
        Date datum = null;
        datum = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaDate");        
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (datum == null || current_date == null) 
            return true;

        if ((current_date).before(datum)) {
            ra.showMessage("wrnclt121");
            return false;
        }  
        return true;
    }

    // cesija - datum stanja potrazivanja, ne smije biti u buducnosti
    public boolean Kol_txtCesijaDateExp_FV () {
        Date datum = null;
        datum = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaDateExp");        
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (datum == null || current_date == null) 
            return true;

        if ((current_date).before(datum)) {
            ra.showMessage("wrnclt121");
            return false;
        }  
        return true;
    }        

    // cesija - datum dospijeca potrazivanja, mora biti veci od current
    public boolean Kol_txtCesijaMatDate_FV () {
        Date datum = null;
        datum = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaMatDate");        
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (datum == null || current_date == null) 
            return true;

        if ((datum).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }  
        return true;
    }   

    // cesija - datum kada je dostavljena lista
    public boolean Kol_txtCesijaListaDatum_FV () {
        Date datum = null;
        datum = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDatum");        
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (datum == null || current_date == null) 
            return true;

        if ((current_date).before(datum)) {
            ra.showMessage("wrnclt121");
            return false;
        }  
        Date date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDatum");
        String dinamika = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamika");
        Date next_date = null;
        // izracunati datum dostave slijedece liste
        if (date != null && (dinamika != null && (!dinamika.equals("")))) {
            int amount = Integer.parseInt(dinamika);
            next_date = coll_util.setCesijaListaNextDate(date, amount);
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaNextLista", next_date);
        }
        return true;
    }        

    public boolean Kol_txtCesijaListaDinamika_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamika", "");   
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamikaDsc", ""); 
            ra.setAttribute(CollSecPaperDialogLDB, "cesija_dinamika_id", null); 
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Kol_txtCesijaListaDinamika")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamikaDsc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Kol_txtCesijaListaDinamikaDsc")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamika", "");
        } 

        if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
            ra.createLDB("UserCodeValueLookUpLDB");
        }  
        ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "cesija_dinamika"); 

        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamikaDsc", ""); 

        LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");

        lookUpRequest.addMapping(CollSecPaperDialogLDB, "cesija_dinamika_id", "use_cod_val_id");                        
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamika", "use_code_value");
        lookUpRequest.addMapping(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamikaDsc", "use_code_desc");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");  
            return false;
        } catch (NothingSelected ns) {   
            return false; 
        }   

        Date date = (Date) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDatum");
        String dinamika = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaListaDinamika");
        Date next_date = null;
        // izracunatidatum dostave slijedece liste
        if (date != null && (dinamika != null && (!dinamika.equals("")))) {
            int amount = Integer.parseInt(dinamika);
            next_date = coll_util.setCesijaListaNextDate(date, amount);
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesijaNextLista", next_date);
        }
        return true;      
    }        

    public boolean Kol_txtCedentMb_FV(String elementName, Object elementValue, Integer lookUpType) {

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCedentMb","");
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCedentName","");
            ra.setAttribute(CollSecPaperDialogLDB,"cedent_id",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCedentOIB","");
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCedentRzbRating","");
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCedentOutRating", "");
            return true;
        }

        if (ra.getCursorPosition().equals("Kol_txtCedentName"))
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCedentMb", "");
        else if (ra.getCursorPosition().equals("Kol_txtCedentMb")) 
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCedentName", "");

        String d_name = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCedentName") != null){
            d_name = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCedentName");
        }

        String d_register_no = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCedentMb") != null){
            d_register_no = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCedentMb");
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

        if (ra.getCursorPosition().equals("Kol_txtCedentMb")) 
            ra.setCursorPosition(2);

        if (ra.isLDBExists("GuarIssueLDB")) {
            ra.setAttribute("GuarIssueLDB", "cus_id", null);
            ra.setAttribute("GuarIssueLDB", "register_no", "");
            ra.setAttribute("GuarIssueLDB", "name", "");
            ra.setAttribute("GuarIssueLDB", "cocunat", "");
            ra.setAttribute("GuarIssueLDB", "rating", "");
            ra.setAttribute("GuarIssueLDB", "residency_cou_id", null);
            ra.setAttribute("GuarIssueLDB", "residency_cou_num", "");
            ra.setAttribute("GuarIssueLDB", "residency_cou_name", "");
            ra.setAttribute("GuarIssueLDB", "issuer_status", "");
            ra.setAttribute("GuarIssueLDB", "tax_number", "");
            ra.setAttribute("GuarIssueLDB", "rating_out", "");
            ra.setAttribute("GuarIssueLDB", "retail_cust_flag", null);
            ra.setAttribute("GuarIssueLDB", "mlt_rating", "");
        } else {
            ra.createLDB("GuarIssueLDB");
        }

        ra.setAttribute("GuarIssueLDB", "register_no", ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCedentMb"));
        ra.setAttribute("GuarIssueLDB", "name", ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCedentName"));
        ra.setAttribute("GuarIssueLDB", "retail_cust_flag", "N");

        LookUpRequest lookUpRequest = new LookUpRequest("GuarIssueLookUp");
        lookUpRequest.addMapping("GuarIssueLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("GuarIssueLDB", "register_no", "register_no");
        lookUpRequest.addMapping("GuarIssueLDB", "name", "name");
        lookUpRequest.addMapping("GuarIssueLDB", "rating", "rating");
        lookUpRequest.addMapping("GuarIssueLDB", "cocunat", "cocunat");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_id", "residency_cou_id");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_num", "residency_cou_num");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_name", "residency_cou_name");
        lookUpRequest.addMapping("GuarIssueLDB", "issuer_status", "issuer_status");
        lookUpRequest.addMapping("GuarIssueLDB", "tax_number", "tax_number");
        lookUpRequest.addMapping("GuarIssueLDB", "rating_out", "rating_out");
        lookUpRequest.addMapping("GuarIssueLDB", "mlt_rating", "mlt_rating");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("wrnclt169");
            //                ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        // provjeriti da li je komitent aktivan
        String status = (String) ra.getAttribute("GuarIssueLDB", "issuer_status");
        if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("2")) {
        } else {    
            // komitent nije aktivan            
            ra.showMessage("wrnclt145");
            return false;
        }     

        ra.setAttribute(CollSecPaperDialogLDB, "cedent_id", ra.getAttribute("GuarIssueLDB", "cus_id"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCedentMb", ra.getAttribute("GuarIssueLDB", "register_no"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCedentOIB", ra.getAttribute("GuarIssueLDB", "tax_number"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCedentName", ra.getAttribute("GuarIssueLDB", "name"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCedentRzbRating", ra.getAttribute("GuarIssueLDB", "rating"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCedentOutRating", ra.getAttribute("GuarIssueLDB", "rating_out"));

        return true;
    }        

    public boolean Kol_txtCesusMb_FV(String elementName, Object elementValue, Integer lookUpType) {

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCesusMb","");
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCesusName","");
            ra.setAttribute(CollSecPaperDialogLDB,"cesus_id",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCesusOIB","");
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCesusRzbRating","");
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesusOutRating", "");
            return true;
        }

        if (ra.getCursorPosition().equals("Kol_txtCesusName"))
            ra.setAttribute(CollSecPaperDialogLDB,"Kol_txtCesusMb", "");
        else if (ra.getCursorPosition().equals("Kol_txtCesusMb")) 
            ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesusName", "");

        String d_name = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesusName") != null){
            d_name = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesusName");
        }

        String d_register_no = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesusMb") != null){
            d_register_no = (String) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesusMb");
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

        if (ra.getCursorPosition().equals("Kol_txtCesusMb")) 
            ra.setCursorPosition(2);

        if (ra.isLDBExists("GuarIssueLDB")) {
            ra.setAttribute("GuarIssueLDB", "cus_id", null);
            ra.setAttribute("GuarIssueLDB", "register_no", "");
            ra.setAttribute("GuarIssueLDB", "name", "");
            ra.setAttribute("GuarIssueLDB", "cocunat", "");
            ra.setAttribute("GuarIssueLDB", "rating", "");
            ra.setAttribute("GuarIssueLDB", "residency_cou_id", null);
            ra.setAttribute("GuarIssueLDB", "residency_cou_num", "");
            ra.setAttribute("GuarIssueLDB", "residency_cou_name", "");
            ra.setAttribute("GuarIssueLDB", "issuer_status", "");
            ra.setAttribute("GuarIssueLDB", "tax_number", "");
            ra.setAttribute("GuarIssueLDB", "rating_out", "");
            ra.setAttribute("GuarIssueLDB", "retail_cust_flag", null);
            ra.setAttribute("GuarIssueLDB", "mlt_rating", "");                

        } else {
            ra.createLDB("GuarIssueLDB");
        }

        ra.setAttribute("GuarIssueLDB", "register_no", ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesusMb"));
        ra.setAttribute("GuarIssueLDB", "name", ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtCesusName"));
        ra.setAttribute("GuarIssueLDB", "retail_cust_flag", "Y");

        LookUpRequest lookUpRequest = new LookUpRequest("GuarIssueLookUp");
        lookUpRequest.addMapping("GuarIssueLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("GuarIssueLDB", "register_no", "register_no");
        lookUpRequest.addMapping("GuarIssueLDB", "name", "name");
        lookUpRequest.addMapping("GuarIssueLDB", "rating", "rating");
        lookUpRequest.addMapping("GuarIssueLDB", "cocunat", "cocunat");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_id", "residency_cou_id");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_num", "residency_cou_num");
        lookUpRequest.addMapping("GuarIssueLDB", "residency_cou_name", "residency_cou_name");
        lookUpRequest.addMapping("GuarIssueLDB", "issuer_status", "issuer_status");
        lookUpRequest.addMapping("GuarIssueLDB", "tax_number", "tax_number");
        lookUpRequest.addMapping("GuarIssueLDB", "rating_out", "rating_out");
        lookUpRequest.addMapping("GuarIssueLDB", "mlt_rating", "mlt_rating");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("wrnclt169");
            //                ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        // provjeriti da li je komitent aktivan
        String status = (String) ra.getAttribute("GuarIssueLDB", "issuer_status");
        if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("2")) {
        } else {    
            // komitent nije aktivan            
            ra.showMessage("wrnclt145");
            return false;
        }   

        ra.setAttribute(CollSecPaperDialogLDB, "cesus_id", ra.getAttribute("GuarIssueLDB", "cus_id"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesusMb", ra.getAttribute("GuarIssueLDB", "register_no"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesusOIB", ra.getAttribute("GuarIssueLDB", "tax_number"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesusName", ra.getAttribute("GuarIssueLDB", "name"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesusRzbRating", ra.getAttribute("GuarIssueLDB", "rating"));
        ra.setAttribute(CollSecPaperDialogLDB, "Kol_txtCesusOutRating", ra.getAttribute("GuarIssueLDB", "rating_out"));

        return true;
    }               

    public boolean Coll_txtEUsePersonId_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtEUsePersonId",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtEUsePersonName",null);
            ra.setAttribute(CollSecPaperDialogLDB,"Coll_txtEUsePersonCusId",null);
            return true;
        }

        if (ra.getCursorPosition().equals("Coll_txtEUsePersonName")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId", "");
        } else if (ra.getCursorPosition().equals("Coll_txtEUsePersonId")) {
            ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName", "");
        }

        String d_name = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName") != null){
            d_name = (String) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName");
        }

        String d_register_no = "";
        if (ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId") != null){
            d_register_no = (String) ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId");
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

        if (ra.getCursorPosition().equals("Coll_txtEUsePersonId")) 
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

        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId"));
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName")); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", null); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", null); 
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", null); 

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllCitizenLookUp_22");   
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

        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonCusId", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonId", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));
        ra.setAttribute(CollSecPaperDialogLDB, "Coll_txtEUsePersonName", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));

        return true;
    }  
    
    public boolean txt_inicijalni_iznos_garancije_FV(String elementName, Object elementValue, Integer lookUpType){
        // todo 
//        System.out.println("elementName: "+ elementName);
//        System.out.println("elementValue:" + elementValue);
        if(elementValue == null){
            ra.setAttribute(CollSecPaperDialogLDB, "txt_inicijalni_iznos_garancije", new BigDecimal(0));
//            System.out.println("null");
            return false;
        }
        BigDecimal garant = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "Kol_txtGuarAmount");
        BigDecimal init_garant = (BigDecimal) ra.getAttribute(CollSecPaperDialogLDB, "txt_inicijalni_iznos_garancije");
//        System.out.println("init_garant:"+ init_garant);
//        System.out.println("garant:"+ garant);
        
//        System.out.println("prije");
        if(garant==null && init_garant == null){
//            System.out.println("nulllllll");
            return false;
        }
            
        
        if(init_garant.compareTo(garant)<0){
            // inicijalni iznos garancije ne moze biti manji od garantiranog iznosa
            ra.showMessage("coll_init_garant");
            return false;
        }
        BigDecimal percent = garant.divide(init_garant, 6 , BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100), new MathContext(6,RoundingMode.CEILING));
        BigDecimal percent_rounded = percent.setScale(2, RoundingMode.HALF_UP);
        
//        System.out.println("percent:"+ percent);
//        System.out.println("percent_rounded:"+ percent_rounded);
        
        
        ra.setAttribute(CollSecPaperDialogLDB, "txt_postotak_garantranja", percent_rounded);
        
        return true;
    }

    /**
     *  :'(
     * _FV podrska ideji da se iskoristi postojeæe polje opisa za unos podatka Broj sporazuma
     * _FV je definirana u CollSecPaperDialog.xml u kojem se isto tako nalazi sve i svasta 
     * _FV bi se trebala pozivati samo u sluèaju UNOSA zadužnice tipa COLL_TYPE_ID=80777
     * @param elementName
     * @param elementValue
     * @param lookUpType
     * @return
     */
    public boolean Coll_txtDesc_FV(String elementName, Object elementValue, Integer lookUpType){        
        String brojSporazuma = elementValue.toString();
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            return true;
        }
        
        //ovo se koristi i na mjenicama(CollBoeDialog) a nastalo je C/P
        boolean validacija = true;
        if(brojSporazuma.trim().length()!=11){
            validacija = false;
            //System.out.println("Duljina podatka Broj sporazuma nije odgovarajuca!");
        }else if ((new CustomerUtil(ra)).getOrgUniDesc(brojSporazuma.substring(2, 5))==null) {
            validacija = false;
            //System.out.println("Neispravna OJ!");
        }else if ( !(brojSporazuma.substring(5, 7).equals("80") || brojSporazuma.substring(5, 7).equals("81") || brojSporazuma.substring(5, 7).equals("82")) ) {
            validacija = false;
            //System.out.println("Sifra vrste sporazuma nije ispravna!");
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
    
    public boolean Coll_YesNoLookUp_FV(String ElName, Object ElValue, Integer LookUp) {
        return coll_lookups.ConfirmDN(CollHeadLDB, ElName, ElValue);
    }   
    
    public boolean Coll_txtNonInsReasone_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollHeadLDB, new String[]{"Coll_txtNonInsReasoneCode", "Coll_txtNonInsReasoneDesc"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("Coll_txtNonInsReasoneCode")){
            coll_util.clearField(CollHeadLDB, "Coll_txtNonInsReasoneDesc");
        }else if(ra.getCursorPosition().equals("Coll_txtNonInsReasoneDesc")){
            coll_util.clearField(CollHeadLDB, "Coll_txtNonInsReasoneCode");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollHeadLDB, "Coll_txtNonInsReasoneCode", "Coll_txtNonInsReasoneDesc", "coll_NonInsReasone");
        if(value==null) return false;
        
        ra.setAttribute(CollHeadLDB, "Coll_txtNonInsReasoneCode", value.sysCodeValue);
        ra.setAttribute(CollHeadLDB, "Coll_txtNonInsReasoneDesc", value.sysCodeDesc);  
        return true;
    }
    
    public boolean Coll_txtAssessmentMethod1_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollHeadLDB,new String[]{ "Coll_txtAssessmentMethod1Code", "Coll_txtAssessmentMethod1Desc"});
            return true;        
        }
        if (ra.getCursorPosition().equals("Coll_txtAssessmentMethod1Code")) {
            coll_util.clearField(CollHeadLDB, "Coll_txtAssessmentMethod1Desc");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtAssessmentMethod1Desc")){
            coll_util.clearField(CollHeadLDB,"Coll_txtAssessmentMethod1Code");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollHeadLDB, "Coll_txtAssessmentMethod1Code", "Coll_txtAssessmentMethod1Desc", "coll_Assessment");
        if(value==null) return false;

        ra.setAttribute(CollHeadLDB, "Coll_txtAssessmentMethod1Code", value.sysCodeValue);
        ra.setAttribute(CollHeadLDB, "Coll_txtAssessmentMethod1Desc", value.sysCodeDesc);  
        return true;
    }
    
    public boolean Coll_txtAssessmentMethod2_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollHeadLDB,new String[]{ "Coll_txtAssessmentMethod2Code", "Coll_txtAssessmentMethod2Desc"});
            return true;        
        }
        if (ra.getCursorPosition().equals("Coll_txtAssessmentMethod2Code")) {
            coll_util.clearField(CollHeadLDB, "Coll_txtAssessmentMethod2Desc");
            ra.setCursorPosition(2);
        } else if(ra.getCursorPosition().equals("Coll_txtAssessmentMethod2Desc")){
            coll_util.clearField(CollHeadLDB,"Coll_txtAssessmentMethod2Code");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollHeadLDB, "Coll_txtAssessmentMethod2Code", "Coll_txtAssessmentMethod2Desc", "coll_Assessment");
        if(value==null) return false;
        
        ra.setAttribute(CollHeadLDB, "Coll_txtAssessmentMethod2Code", value.sysCodeValue);
        ra.setAttribute(CollHeadLDB, "Coll_txtAssessmentMethod2Desc", value.sysCodeDesc);  
        return true;
    }
    
    public boolean Coll_Grade_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollHeadLDB,new String[]{ ElName});
            return true;        
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollHeadLDB, ElName, null, "rating_assessment");
        if(value==null) return false;
        
        ra.setAttribute(CollHeadLDB, ElName, value.sysCodeValue); 

        return true;
    } 
    
    public boolean Coll_txtContractType(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollHeadLDB, new String[]{ "Coll_txtContractTypeCode", "Coll_txtContractTypeDesc"});
            return true;        
        }
        if (ra.getCursorPosition().equals("Coll_txtContractTypeCode")) {
            coll_util.clearField(CollHeadLDB, "Coll_txtContractTypeDesc");
            ra.setCursorPosition(2);
        } else if(ra.getCursorPosition().equals("Coll_txtContractTypeDesc")){
            coll_util.clearField(CollHeadLDB,"Coll_txtContractTypeCode");
        }
        String sys_code_id="coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code");
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(CollHeadLDB, "Coll_txtContractTypeCode", "Coll_txtContractTypeDesc", sys_code_id);
        if(value==null) return false;
        
        ra.setAttribute(CollHeadLDB, "Coll_txtContractTypeCode", value.sysCodeValue);
        ra.setAttribute(CollHeadLDB, "Coll_txtContractTypeDesc", value.sysCodeDesc);  
        return true;
    }
    
    public boolean Coll_txtAmountHeightPlacement_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(CollHeadLDB, new String[]{ "Coll_txtAmountHeightPlacement", "Coll_txtPercentageByGuarantor"});
            coll_util.enableField("Coll_txtPercentageByGuarantor", 2);
            return true;        
        }
        boolean rez=coll_lookups.ConfirmDN(CollHeadLDB, ElName, ElValue);
        if(rez){
            String value=(String) ra.getAttribute(CollHeadLDB,"Coll_txtAmountHeightPlacement");
            if(value!=null && value.equals("D")){
                coll_util.enableField("Coll_txtPercentageByGuarantor", 0);
            }else{
                coll_util.enableField("Coll_txtPercentageByGuarantor", 2);
                coll_util.clearField(CollHeadLDB, "Coll_txtPercentageByGuarantor");
            }
        }
        return rez;
    }
    
    private void SetDefaultValuesOnLDB(){
       
        //ako je samo kontekst scr_change to znaci da se radi unos novoga i ova polja se postavljaju na D
        if(ra.getScreenContext().equalsIgnoreCase("scr_change")){  
            //postavljam stare vrijednosti ovih polja na neku vrijednost za insert novog kolaterala zbog provjere koja ce se raditi pri snimanju
            //jer kada su stare vrijednosti popunjene onda su polja RealEstate_txtB2HNB,Reb_RealEstate_txtB2IRB obavezna pri snimanju, a kod novog kolaterala ova polja
            //su obavezna.
            coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2HNB_OLD", "D");
            coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2IRB_OLD", "D");
            coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2HNB", "D");
            coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtB2IRB", "D");
            coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtAssessmentMethod1Code", "5");
            coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtAssessmentMethod2Code", "5");
        }else{
            String hnb=ra.getAttribute(CollHeadLDB, "Coll_txtB2HNB_OLD");
            String irb=ra.getAttribute(CollHeadLDB, "Coll_txtB2IRB_OLD");
            if(hnb==null ||hnb.equals("")) ra.setRequired("Coll_txtB2HNB", false);
            if(irb==null ||irb.equals("")) ra.setRequired("Coll_txtB2IRB", false);
        } 
        
        coll_util.SetDefaultValue(CollHeadLDB, "Coll_txtContractTypeCode", coll_util.GetDefaultValueFromSystemCodeValue("coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code")));
        
        //Ne racuna se za vozila, pokretnine, plovila i zalihe... ako se bude trebalo racunat ovo odkomentirat
        //BigDecimal rationNGVInsu=ra.getAttribute("CollCommonDataLDB", "Coll_txtRationNGVInsurance");        
        //if(rationNGVInsu!=null && rationNGVInsu.compareTo(new BigDecimal("1"))<=0){
        //    ra.setAttribute(CollHeadLDB, "Coll_txtInsTotalCoverCode", "D");
        //}else{
        //    ra.setAttribute(CollHeadLDB, "Coll_txtInsTotalCoverCode", "N");
        //}
    } 
    
    private void ScreenEntry_FV(){
        ra.invokeValidation("Coll_txtAssessmentMethod1Code");
        ra.invokeValidation("Coll_txtAssessmentMethod2Code");
        ra.invokeValidation("Coll_txtNonInsReasoneCode");
        ra.invokeValidation("Coll_txtContractTypeCode");
        if(ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_update")){
            ra.invokeValidation("Vehi_txtVehKasko");
        }
    }
}  
