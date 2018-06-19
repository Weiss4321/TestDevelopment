package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.modules.collateral.util.CollReqFields;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;
import hr.vestigo.modules.coreapp.util.SpecialAuthorityManager;


/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RealEstateDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RealEstateDialog.java,v 1.185 2016/10/07 10:53:39 hrazst Exp $";

    //1.73 staro
    private final BigDecimal numberZero = new BigDecimal("0.00");
    private final BigDecimal numberOneHundred = new BigDecimal("100.0000000000000000000000000000000000000000000000000000000000000000");
    private Date todaySQLDate = null;
    CollateralUtil coll_util = null;
    LookUps coll_lookups = null;
    private String MASTER_LDBNAME = "RealEstateDialogLDB";
    
    // FBPr200017059 - dodani atributi za koje je kasnije potrebno provjeriti da li su promjenjeni (Podaci procjenitelja o vrijednosti nekretnine)
    private BigDecimal est_cus_id = null;
    private BigDecimal for_est_cus_id = null;
    private BigDecimal estn_value = null;
    private BigDecimal cur_id = null;
    private BigDecimal new_build_value = null;
    private String est_nomi_desc = null;
    private Date estn_date = null;
        
    public RealEstateDialog(ResourceAccessor ra) {
        super(ra);
        coll_lookups = new LookUps(ra);
        coll_util = new CollateralUtil(ra);
    }  

    public void RealEstateDialog_SE() {
        if (!(ra.isLDBExists(MASTER_LDBNAME))) ra.createLDB(MASTER_LDBNAME);
        // Podigni LDB za stare podatke
        if (!ra.isLDBExists("RealEstateDialogLDB_B")) ra.createLDB("RealEstateDialogLDB_B");
        //podizem ldb za zajednièke podatke  
        if(!ra.isLDBExists("CollCommonDataLDB")) ra.createLDB("CollCommonDataLDB");
        String scr_ctx = ra.getScreenContext();
        
        // Milka, 02.10.2006, promjena za ROBI      
        if(scr_ctx.equalsIgnoreCase("scr_update") || scr_ctx.equalsIgnoreCase("scr_update_aut")){
            postavi_za_update();
        }
        
        if(scr_ctx.equalsIgnoreCase("scr_detail") ||  scr_ctx.equalsIgnoreCase("scr_detail_deact") || scr_ctx.equalsIgnoreCase("scr_update") || scr_ctx.equalsIgnoreCase("scr_detail_co") || 
                scr_ctx.equalsIgnoreCase("scr_update_aut") || scr_ctx.equalsIgnoreCase("scr_update_ref") || scr_ctx.equalsIgnoreCase("scr_owner_bank")){
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_HEA_ID", (BigDecimal)ra.getAttribute("ColWorkListLDB", "col_hea_id"));
            //Pokretanje transakcije dohvata ili za detalje ili za update
            try {
                ra.executeTransaction();                 
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            } 
        } 
        
        if(scr_ctx.equalsIgnoreCase("scr_change") || scr_ctx.equalsIgnoreCase("scr_update")){
            //eligibility
            ra.setContext("RealEstate_txtEligibility","fld_change_protected");
            ra.setContext("RealEstate_txtEligDesc","fld_change_protected");
            ra.invokeValidation("RealEstate_txtInspolInd");        
            postavi_nekeUserDate();
        }
        
        if(scr_ctx.equalsIgnoreCase("scr_change")){
            postavi_za_insert();
            //ra.setCursorPosition("RealEstate_txtCollTypeCode");
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_COL_CAT_ID",(BigDecimal)ra.getAttribute("ColWorkListLDB","col_cat_id"));
        } 
        
        // Milka, 27.04.2010, postavljanje vrijednosti za CRM kod promjene podataka
        BigDecimal coll_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID"); 
        BigDecimal real_es_type_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE");
        BigDecimal coll_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id"); 
        
        //FBPr200014054 - Površina èestice
        if(scr_ctx.equalsIgnoreCase("scr_update") || scr_ctx.equalsIgnoreCase("scr_update_aut") || scr_ctx.equalsIgnoreCase("scr_update_ref")){
            // U sluèaju konteksta promjene, te ako je uneseni kolateral kuæa - omoguæava se unos površine èestice
            BigDecimal sto = new BigDecimal("8777");   // oznaka da se radi o stambenom objektu
            BigDecimal house = new BigDecimal("4222");  // oznaka da se radi o kuæi
            if(coll_typ_id.equals(sto) && real_es_type_id.equals(house)){
                ra.setContext("RealEstate_txtSqrLand", "fld_plain");
                ra.setRequired("RealEstate_txtSqrLand", true);
            }
            
            // FBPr200017059 - ulaskom na ekran pune se atributi za koje je kasnije potrebno provjeriti da li su promjenjeni
            BigDecimal nekr = new BigDecimal("618223");
            if(coll_cat_id.equals(nekr)){
                est_cus_id = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_EUSE_ID");
                for_est_cus_id = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_RCEstimateId");
                estn_value = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnValu");
                cur_id = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_NM_CUR_ID");
                new_build_value = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNewBuildVal");
                est_nomi_desc = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnMarkDesc");
                estn_date = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnDate");
                ra.setAttribute(MASTER_LDBNAME, "EstimateDataChg", "N");
                System.out.println("est_cus_id=" + est_cus_id + "\nfor_est_cus_id="+ for_est_cus_id + "\nestn_value="+ estn_value + "\nestn_value="+ estn_value
                        + "\ncur_id="+ cur_id + "\nnew_build_value="+ new_build_value + "\nest_nomi_desc="+ est_nomi_desc + "\nestn_date="+ estn_date); 
            }
        }

        // Milka, 19.08.2009 - postavljanje konteksta u detaljima ovisno o vrsti nekretnine      
        if (scr_ctx.equalsIgnoreCase("scr_detail") || scr_ctx.equalsIgnoreCase("scr_detail_deact") || scr_ctx.equalsIgnoreCase("scr_update") || scr_ctx.equalsIgnoreCase("scr_detail_co") ||
            scr_ctx.equalsIgnoreCase("scr_update_aut") || scr_ctx.equalsIgnoreCase("scr_update_ref")) {            
            coll_util.setRealEstateFirstScreenCtx(coll_typ_id, scr_ctx, real_es_type_id);            
        }   
        
        
      //:'( 10387 - prvotno se trailo da se omoguæi kod unosa ali naknadno i kod promjene
      BigDecimal sto = new BigDecimal("8777");   // oznaka da se radi o stambenom objektu
      BigDecimal house = new BigDecimal("4222");  // oznaka da se radi o kuæi
      BigDecimal stan = new BigDecimal("5222"); 
      BigDecimal col_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID"); 
      BigDecimal col_sub_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE"); 
      
      if(scr_ctx.equalsIgnoreCase("scr_update") || scr_ctx.equalsIgnoreCase("scr_change")){ // && !(col_typ_id.equals(sto) && (col_sub_typ_id.equals(house) || col_sub_typ_id.equals(stan))) ){
          //nije kuæa/stan i je kontekst promjene pa se omoguæava unos polja (mora biti kontekst promjene jer ako je unos tipovi i podtipovi kolaterala su null tj prazni)
          ra.setContext("Kol_txtCRMHnb_REstate", "fld_plain");
      }
      this.SetDefaultValuesOnLDB(); 
      this.ScreenEntry_FV();
      if(!scr_ctx.equalsIgnoreCase("scr_change")) this.SetRequiredKatAndLift();
    }

    public void otvori_prvi_nekretnine() {
        ra.switchScreen("RealEstateDialog", ra.getScreenContext());
    }
    
    public void otvori_drugi_nekretnine() {
        ra.switchScreen("RealEstateDialog2", ra.getScreenContext());       
    }
    
    public void otvori_treci_nekretnine() {
        ra.switchScreen("RealEstateDialog3", ra.getScreenContext());        
    }
    
    public void otvori_cetvrti_nekretnine() {
        ra.switchScreen("RealEstateDialog4", ra.getScreenContext());
    }
    
    public void insu_policy(){
        BigDecimal colHeaId = null;

        if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI") ||
                ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV") ||
                ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR") ||
                ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI")){
            colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB", "COL_HEA_ID");

            if (!(ra.isLDBExists(MASTER_LDBNAME))) {
                ra.createLDB(MASTER_LDBNAME);
            }                   
            
            // Milka, 18.09.2009 - radi preracuna ponderirane i raspolozive vrijednsoti kod dodavanja i brisanja plasmana
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_COL_HEA_ID",ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_REAL_EST_NM_CUR_ID",ra.getAttribute("CollHeadLDB", "REAL_EST_NM_CUR_ID"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNomiValu",ra.getAttribute("CollHeadLDB","Coll_txtNomiValu"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtCollMvpPonder",ra.getAttribute("CollHeadLDB","Coll_txtCollMvpPonder"));

            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtCollMvpPonderMin",ra.getAttribute("CollHeadLDB","Coll_txtCollMvpPonderMin"));  
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtCollMvpPonderMax",ra.getAttribute("CollHeadLDB","Coll_txtCollMvpPonderMax"));

            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighValue",ra.getAttribute("CollHeadLDB","Coll_txtAcouValue"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighDate",ra.getAttribute("CollHeadLDB","Coll_txtAcouDate"));  
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailValue",ra.getAttribute("CollHeadLDB","Coll_txtAvailValue"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailDate",ra.getAttribute("CollHeadLDB","Coll_txtAvailDate"));  
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtSumPartVal",ra.getAttribute("CollHeadLDB","Coll_txtSumPartVal"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtSumPartDat",ra.getAttribute("CollHeadLDB","Coll_txtSumPartDat"));     

            ra.setAttribute(MASTER_LDBNAME,"RealEstate_COL_CAT_ID",ra.getAttribute("CollHeadLDB","COL_CAT_ID"));  
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_COL_TYPE_ID",ra.getAttribute("CollHeadLDB","COL_TYPE_ID"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_REAL_EST_TYPE",ra.getAttribute("CollSecPaperDialogLDB","SEC_TYP_ID"));  
        } else {
            colHeaId =  (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_HEA_ID");
        }

        if(colHeaId != null){
            if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut") ||
                    ra.getScreenContext().equalsIgnoreCase("scr_detail_co") || ra.getScreenContext().equalsIgnoreCase("scr_owner_bank")) {
                ra.loadScreen("InsuPolicy", "scr_detail");
            }else{
                ra.loadScreen("InsuPolicy", "scr_realest");
            }
        }else{
            ra.showMessage("wrnclt61");
        }
    }//insu_policy

    public void hypo_fid_con(){
        BigDecimal colHeaId = null;
        colHeaId =  (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_HEA_ID");
        //Ovo se nalazi u CollSecPaper02
        if(colHeaId == null){
            ra.showMessage("wrnclt61");
        }else{
            if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut") ||
                    ra.getScreenContext().equalsIgnoreCase("scr_detail_co") || ra.getScreenContext().equalsIgnoreCase("scr_owner_bank")) {
                ra.loadScreen("CollHfPrior","detail_re");               
            } else {
                ra.loadScreen("CollHfPrior","base_re");
            }
        }
    }//hypo_fid_con

    public void owners(){
        BigDecimal colHeaId = null;
        colHeaId =  (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_HEA_ID");
        if(colHeaId == null){
            ra.showMessage("wrnclt61");
        }else{
            if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_update_aut")
                    || ra.getScreenContext().equalsIgnoreCase("scr_update_ref") || ra.getScreenContext().equalsIgnoreCase("scr_detail_co")
                    || ra.getScreenContext().equalsIgnoreCase("scr_owner_bank")) {
                ra.loadScreen("CollOwners","detail_re");
            } else {
                ra.loadScreen("CollOwners","scr_realest");
            }
        }
    }
    
    public void RealEstate_History(){
        ra.loadScreen("CollHistory");
    }

    public boolean RealEstate1_CollateralType_FV(String ElName, Object ElValue, Integer LookUp) {
        // TIP KOLATERALA: - SIFRA, VRSTA i VLASNIK 
        // RealEstate_COL_TYPE_ID                           
        // RealEstate_txtCollTypeCode
        // RealEstate_txtCollTypeName
        ra.setAttribute(MASTER_LDBNAME, "dummySt", "");
        ra.setAttribute(MASTER_LDBNAME, "dummyDate", null);

        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollTypeCode", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollTypeName", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollPeriodRev", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollMvpPonder", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollMvpPonderMin", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollMvpPonderMax", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollHnbPonder", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollHnbPonderMin", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollHnbPonderMax", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollRzbPonder", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollRzbPonderMin", null);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollRzbPonderMax", null);
            ra.setAttribute(MASTER_LDBNAME, "dummySt", "");

            ra.setAttribute(MASTER_LDBNAME, "dummyDate", null);

            //Brise tip nekretnine
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeCode", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeDesc", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE", null);

            //Brise podtip nekretnine
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtReSubTypeCode", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtReSubTypeDesc", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_RE_SUB_TYPE_ID", null);

            // Brise uporabnu dozvolu
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtBuildPermInd", null);
            ra.setContext("RealEstate_txtBuildPermInd","fld_plain");  

            // Brise namjenu
            ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurpose", "");
            ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurposeDsc", "");

            return true;
        }

        if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
            ra.createLDB("CollateralTypeLookUpLDB");
        }
        
        ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", "NEKR");
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollTypeName", "");
        ra.setAttribute("CollateralTypeLookUpLDB", "CollateralCatId", ra.getAttribute("ColWorkListLDB","col_cat_id"));

        LookUpRequest lookUpRequest = new LookUpRequest("KolPonderNewLookUp");  
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID", "col_typ_id");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCollTypeCode", "code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCollTypeName", "name");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCollMvpPonderMin", "min_value");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCollMvpPonder", "dfl_value");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCollMvpPonderMax", "max_value");            

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }    
        
        //Brise tip nekretnine
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeCode", "");
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeDesc", "");
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE", null);

        //Brise podtip nekretnine
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtReSubTypeCode", "");
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtReSubTypeDesc", "");
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_RE_SUB_TYPE_ID", null);

        // Brise namjenu
        ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurpose", "");
        ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurposeDsc", "");            

        // Brise povrsinu cestice (FBPr200014054 - Površina èestice)
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtSqrLand", null);
        ra.setContext("RealEstate_txtSqrLand","fld_protected"); 

        BigDecimal coll_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID"); 
        BigDecimal real_es_type_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE");
        
        // postaviti kontekst ovisno o vrsti nekretnine
        String scr_ctx = ra.getScreenContext();
        coll_util.setRealEstateFirstScreenCtx(coll_typ_id,scr_ctx, real_es_type_id);

        if((scr_ctx.compareTo("scr_change")== 0) || (scr_ctx.compareTo("scr_update")== 0) || (scr_ctx.compareTo("scr_update_aut")== 0)){
            setAmortization1();
            BigDecimal mvpPonder = (BigDecimal) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtCollMvpPonder");
            BigDecimal nominalCONV = (BigDecimal) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtNomiValu");

            if((mvpPonder != null) && (nominalCONV != null)){
                BigDecimal weighValue = nominalCONV.multiply(mvpPonder);
                weighValue = weighValue.divide(numberOneHundred,2,BigDecimal.ROUND_HALF_UP);

                // THIRD_RIGHT_CUR_ID        RealEstate_THIRD_RIGHT_CUR_ID RealEstate_txtThirdRightCurCodeChar

                // NEPO_VALUE                RealEstate_txtNepoValue                                                                   
                // NEPO_DATE                 RealEstate_txtNepoDate                                                                    
                // NEPO_PER_CAL              RealEstate_txtNepoPerCal          ništa se ne upisuje niti se mijenja
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoValue",nominalCONV);
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoDate",todaySQLDate);

                // WEIGH_VALUE               RealEstate_txtWeighValue                                                                      
                // WEIGH_DATE                RealEstate_txtWeighDate                                                                       
                // WEIGH_BVALUE              RealEstate_txtWeighBValue                                                                     
                // WEIGH_BDATE               RealEstate_txtWeighBDate 
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighValue",weighValue);
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighDate",todaySQLDate);
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighBValue",weighValue);
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighBDate",todaySQLDate);

                // AVAIL_VALUE               RealEstate_txtAvailValue                                                                  
                // AVAIL_PER_CAL             RealEstate_txtAvailPerCal                                                                 
                // AVAIL_DATE                RealEstate_txtAvailDate   
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailValue",weighValue);

                weighValue = null;
            }

            mvpPonder = null;
            nominalCONV = null;
        }//scr_change

        if((scr_ctx.compareTo("scr_update")== 0)){
            setAmortization1();
            BigDecimal mvpPonder = (BigDecimal) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtCollMvpPonder");
            BigDecimal nominalCONV = (BigDecimal) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtNomiValu");

            if((mvpPonder != null) && (nominalCONV != null)){
                BigDecimal weighValue = nominalCONV.multiply(mvpPonder);
                weighValue = weighValue.divide(numberOneHundred,2,BigDecimal.ROUND_HALF_UP);

                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighValue",weighValue);
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoValue",nominalCONV);

                BigDecimal availValue = numberZero;
                BigDecimal thirdRightNom = numberZero;
                BigDecimal hfsValue = numberZero;

                thirdRightNom = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRightInNom");
                hfsValue = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtHfsValue");   

                availValue = weighValue.subtract(thirdRightNom);
                availValue = weighValue.subtract(hfsValue);

                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailValue",availValue);

                weighValue = null;
                availValue = numberZero;
                thirdRightNom = numberZero;
                hfsValue = numberZero;
            }
            mvpPonder = null;
            nominalCONV = null;
        }

        if(ra.getCursorPosition().equals("RealEstate_txtCollTypeCode")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtCollTypeName")){
            ra.setCursorPosition(1);
        }   
        return true;

    }//RealEstateDialog_CollateralType_FV
    
    public boolean RealEstate1_RET_FV(String ElName, Object ElValue, Integer LookUp) {
        // VRSTA NEKRETNINE: - SIFRA, VRSTA i VLASNIK   
        // RealEstate_REAL_EST_TYPE                         
        // RealEstate_txtTypeCode
        // RealEstate_txtTypeDesc

        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeCode", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeDesc", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE", null);
            ra.setAttribute(MASTER_LDBNAME, "dummyDate", null);

            //Brise podtip nekretnine
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtReSubTypeCode", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtReSubTypeDesc", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_RE_SUB_TYPE_ID", null);

            // otvara CRM misljenje za unos
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRealEstSpecStat","D");   
            ra.setContext("RealEstate_txtRealEstSpecStat","fld_plain");                                             
            return true;
        }

        if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
            ra.createLDB("CollateralTypeLookUpLDB");
        }            

        if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID") != null) {
            ra.setAttribute("CollateralTypeLookUpLDB", "CollateralCatId", ra.getAttribute("ColWorkListLDB","col_cat_id"));
            ra.setAttribute("CollateralTypeLookUpLDB", "CollateralTypId", ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID"));                
        }else{
            //Nije moguce odabrati vrstu nekretnine dok se ne odabere vrsta kolaterala.
            ra.showMessage("wrnclt44");
            return false;
        }

        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeDesc", "");
        LookUpRequest lookUpRequest = new LookUpRequest("KolSubPonderNewLookUp"); 
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE", "col_sub_id");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtTypeCode", "code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtTypeDesc", "name");  
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCollMvpPonderMin", "min_value");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCollMvpPonder", "dfl_value");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCollMvpPonderMax", "max_value");     

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        // postaviti CRM misljenje i kontekst ovisno o vrsti i podvrsti kolaterala
        BigDecimal col_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID"); 
        BigDecimal col_sub_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE"); 
        coll_util.setRealEstateCRMopinion(col_typ_id, col_sub_typ_id);     

        // Brise povrsinu èestice (FBPr200014054 - Površina èestice)
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtSqrLand", null);
        ra.setContext("RealEstate_txtSqrLand","fld_protected"); 

        // FBPr200014054 - Površina èestice - ako je uneseni kolateral kuæa - omoguæava se unos površine èestice, te je on u tom sluèaju obavezan
        BigDecimal sto = new BigDecimal("8777");   // oznaka da se radi o stambenom objektu
        BigDecimal house = new BigDecimal("4222");  // oznaka da se radi o kuæi
        if(col_typ_id.equals(sto) && col_sub_typ_id.equals(house)){
            ra.setContext("RealEstate_txtSqrLand", "fld_plain");
            ra.setRequired("RealEstate_txtSqrLand", true);
        }

       
        // :'( Change Request 17451
        //  BigDecimal stan = new BigDecimal("5222"); 
        //  if(col_typ_id.equals(sto) && (col_sub_typ_id.equals(house)||col_sub_typ_id.equals(stan))){
        //  //za stambeni objekt i kuæa/stan nije moguæe unositi
        //  ra.setAttribute(MASTER_LDBNAME,"Kol_txtCRMHnb_REstate","N");
        //  ra.setContext("Kol_txtCRMHnb_REstate","fld_change_protected");
        //  }else 
        if(ra.getScreenContext().compareTo("scr_change")==0 || ra.getScreenContext().compareTo("scr_update")==0){
            //nije kuæa/stan i je kontekst unosa/promjene pa se omoguæava unos polja
            ra.setContext("Kol_txtCRMHnb_REstate", "fld_plain");
        }
        
        if(ra.getCursorPosition().equals("RealEstate_txtTypeCode")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtTypeDesc")){
            ra.setCursorPosition(3);
        }
        
        if(!ra.isLDBExists("EconomicLifeLDB")){
            ra.createLDB("EconomicLifeLDB");
        } 
        coll_util.clearFields("EconomicLifeLDB", new String[]{"col_cat_id","col_typ_id","col_sub_id","economic_life"});
        try {
            ra.setAttribute("EconomicLifeLDB", "col_cat_id", ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_CAT_ID"));
            ra.setAttribute("EconomicLifeLDB", "col_typ_id", ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID"));
            ra.setAttribute("EconomicLifeLDB", "col_sub_id",  ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE"));            
            ra.executeTransaction();
            ra.setAttribute("CollCommonDataLDB", "Coll_txtEconomicLife", ra.getAttribute("EconomicLifeLDB", "economic_life"));
        } catch (Exception e) {
            
        }
        this.SetRequiredKatAndLift();
        return true;

    }//RealEstateDialog_RET_FV 
    
    public boolean RealEstate1_RESUBT_FV(String ElName, Object ElValue, Integer LookUp) {
        // PODVRSTA NEKRET.: - SIFRA, VRSTA i VLASNIK   
        // RealEstate_RE_SUB_TYPE_ID                            
        // RealEstate_txtReSubTypeCode
        // RealEstate_txtReSubTypeDesc

        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtReSubTypeCode", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtReSubTypeDesc", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_RE_SUB_TYPE_ID", null);
            ra.setAttribute(MASTER_LDBNAME, "dummyDate", null);
            return true;
        }
        if (!ra.isLDBExists("RealEstateSubTypeLookUpLDB")) {                                                 
            ra.createLDB("RealEstateSubTypeLookUpLDB");                                                      
        }                                                                                                

        if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID") == null) {
            ra.showMessage("wrnclt44");
            return false;
        }

        if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE") != null) {
            ra.setAttribute("RealEstateSubTypeLookUpLDB", "RE_TYPE_ID", (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE"));  
        }else{
            //Nije moguce odabrati podvrstu nekretnine dok se ne odabere vrsta nekretnine.
            ra.showMessage("wrnclt51");
            return false;
        }

        LookUpRequest lookUpRequest = new LookUpRequest("RealEstateSubTypeLookUp"); 
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_RE_SUB_TYPE_ID", "re_sub_type_id");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtReSubTypeCode", "re_sub_type_code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtReSubTypeDesc", "re_sub_type_desc");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        if(ra.getCursorPosition().equals("RealEstate_txtReSubTypeCode")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtReSubTypeDesc")){
            ra.setCursorPosition(3);
        }
        
        return true;

    }//RealEstateDialog_RESUBT_FV 
    
    public boolean RealEstate1_CollCourt_FV(String ElName, Object ElValue, Integer LookUp) {
        // SIFRA I NAZIV SUDA: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_REAL_EST_COURT_ID                         
        // RealEstate_txtCoCode
        // RealEstate_txtCoName
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCoCode", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCoName", "");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_COURT_ID", null);
            ra.setAttribute(MASTER_LDBNAME, "dummyBD", null);
            ra.setAttribute(MASTER_LDBNAME, "dummyDate", null);

            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCadaMuncCode", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCadaMuncName", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_CADA_MUNC", null);   

            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCadaMuncCodeST", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCadaMuncNameST", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_RE_CADA_MUNC_ST", null); 

            return true;
        }

        LookUpRequest lookUpRequest = new LookUpRequest("CollCourtLookUp"); 
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_REAL_EST_COURT_ID", "co_id");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCoCode", "co_code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCoName", "co_name");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummyBD", "co_pol_map_id_cnt");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummyDate", "co_date_from");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummyDate", "co_date_until");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        if(ra.getCursorPosition().equals("RealEstate_txtCoCode")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtCoName")){
            ra.setCursorPosition(1);
        }   
        return true;

    }//RealEstateDialog_CollCourt_FV
    
    public boolean RealEstate1_CadastreMuncipality_FV(String ElName, Object ElValue, Integer LookUp) {    
        // KATASTARSKA OPCINA: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_REAL_EST_CADA_MUNC                            
        // RealEstate_txtCadaMuncCode
        // RealEstate_txtCadaMuncName

        BigDecimal realEstCourtId = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_COURT_ID");
        if(realEstCourtId == null){
            ra.showMessage("wrnclt43");
            //Za odabir katastarske opcine potrebno je najprije odabrati sud
            return false;
        }

        if (ElValue == null || ((String) ElValue).equals("")) {  
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCadaMuncCode", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCadaMuncName", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_CADA_MUNC", null);                               
            return true;                                                                                 
        }      
        
        if (!ra.isLDBExists("CadastreMuncipalityLookUpLDB")) {
            ra.createLDB("CadastreMuncipalityLookUpLDB");
        }
        ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDPolMapId", null);
        ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDCoId", (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_COURT_ID"));

        LookUpRequest lookUpRequest = new LookUpRequest("CadastreMuncipalityLookUp");                       
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_REAL_EST_CADA_MUNC", "cad_map_id");           
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCadaMuncCode", "code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCadaMuncName", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }  
        if(ra.getCursorPosition().equals("RealEstate_txtCadaMuncCode")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtCadaMuncName")){
            ra.setCursorPosition(1);
        }   
        return true;                                                                                   

    }//RealEstateDialog_CadastreMuncipality_FV     
    
    public boolean RealEstate1_CadMuncST_FV(String ElName, Object ElValue, Integer LookUp) {              

        BigDecimal realEstCourtId = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_COURT_ID");
        if(realEstCourtId == null){
            ra.showMessage("wrnclt43");
            //Za odabir katastarske opcine potrebno je najprije odabrati sud
            return false;
        }

        if (ElValue == null || ((String) ElValue).equals("")) {  
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCadaMuncCodeST", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCadaMuncNameST", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_RE_CADA_MUNC_ST", null);                               
            return true;                                                                                 
        }                                                                                              

        if (!ra.isLDBExists("CadastreMuncipalityLookUpLDB")) {
            ra.createLDB("CadastreMuncipalityLookUpLDB");
        }
        ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDPolMapId", null);
        ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDCoId", (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_COURT_ID"));

        LookUpRequest lookUpRequest = new LookUpRequest("CadastreMuncipalityLookUp");                       
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_RE_CADA_MUNC_ST", "cad_map_id");           
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCadaMuncCodeST", "code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtCadaMuncNameST", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }  
        if(ra.getCursorPosition().equals("RealEstate_txtCadaMuncCodeST")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtCadaMuncNameST")){
            ra.setCursorPosition(1);
        }   
        return true;                                                                                   

    }//RealEstateDialog_CadMuncST_FV    
    
    public boolean RealEstate1_txtRealEstLandPart_FV(){
        // KATASTARSKA CESTICA: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtRealEstLandPart                            
        return true;
    }
    public boolean RealEstate1_txtRealEstLandPartST_FV(){
        // STARA KATASTARSKA CESTICA: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtReLandPartST                           
        return true;
    }
    public boolean RealEstate1_txtRealEstLandRegn_FV(){
        // BROJ ZKU: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtRealEstLandRegn                            
        return true;
    }
    public boolean RealEstate1_txtRealEstLandSub_FV(){
        // PODULOZAK: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtRealEstLandSub                         
        return true;
    }//RealEstate1_txtRealEstLandSub_FV
    public boolean RealEstate1_txtCoown_FV(){
        // SUVLASNICKI UDIO: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtCoown                          
        return true;
    }//RealEstate_txtCoown_FV

    public boolean RealEstate1_txtBuildPermInd_FV(String elementName, Object elementValue, Integer lookUpType) {
        // UPORABNA DOZVOLA: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtBuildPermInd   
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtBuildPermInd", null);
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping(MASTER_LDBNAME, "RealEstate_txtBuildPermInd", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;  
        } catch (NothingSelected ns) {
            return false;

        }

        return true;
    }   //RealEstate1_txtBuildPermInd_FV

    public boolean RealEstate1_txtInspolInd_FV(String elementName, Object elementValue, Integer lookUpType) {
        String val = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtInspolInd");
        if(val.equals("D")){
            coll_util.enableFields(new String[] {"RealEstate_txtNonInsReasoneCode", "RealEstate_txtNonInsReasoneDesc"}, 2);
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"RealEstate_txtNonInsReasoneCode", "RealEstate_txtNonInsReasoneDesc"});
       }else{
            coll_util.enableFields(new String[]{"RealEstate_txtNonInsReasoneCode", "RealEstate_txtNonInsReasoneDesc"}, 0);                
        }
        return true;
    }
    
    public boolean RealEstate1_txtRealEstPdesc_FV(String elementName, Object elementValue, Integer lookUpType) {
        // OPIS NEKR. U ZKU: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtRealEstPdesc
        return true;
    }//RealEstate1_txtRealEstPdesc_FV
    
    public boolean RealEstate1_txtStreet_FV(String elementName, Object elementValue, Integer lookUpType) {
        // ULICA I KBR.: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtStreet 
        return true;
    }//RealEstate1_txtStreet_FV
    
    public boolean RealEstate1_txtHousenr_FV(String elementName, Object elementValue, Integer lookUpType) {
        // ULICA I KBR.: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        // RealEstate_txtHousenr
        return true;
    }//RealEstate1_txtHousenr_FV
    
    public boolean RealEstate1_txtPlacePostNr_FV(String elementName, Object elementValue, Integer lookUpType){
        //SIFRA I NAZIV MJESTA: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        //RealEstate_POL_MAP_ID_AD     
        //RealEstate_txtPlacePostCode  
        //RealEstate_txtPlacePostName  

        BigDecimal placeTypeId = new BigDecimal("5999.0");

        if (elementValue == null || ((String) elementValue).equals("")) {  

            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPlacePostCode", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPlacePostName", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_POL_MAP_ID_AD", null);   
            //nuliraj i postu
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPostNr", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPostName", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_POS_OFF_ID_AD", null);      
            return true;                                                                                 
        }                                                                                              

        if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }
        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", placeTypeId);

        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");                       
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_POL_MAP_ID_AD", "pol_map_id");           
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtPlacePostCode", "code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtPlacePostName", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }  
        if(ra.getCursorPosition().equals("RealEstate_txtPlacePostCode")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtPlacePostName")){
            ra.setCursorPosition(1);
        }   

        return true;  
    }//RealEstate1_txtPlacePostNr_FV
    
    public boolean RealEstate1_txtPostNr_FV(String elementName, Object elementValue, Integer lookUpType){
        //POSTANSKI BROJ I NAZIV POSTE: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
        //RealEstate_POS_OFF_ID_AD
        //RealEstate_txtPostNr
        //RealEstate_txtPostName

        BigDecimal polMapIdAd = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_POL_MAP_ID_AD");

        if(polMapIdAd == null){
            ra.showMessage("wrnclt46");
            //Za odabir postanskog broja i naziva poste najprije odaberite mjesto adrese.
            return false;
        }

        if (elementValue == null || ((String) elementValue).equals("")) {  
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPostNr", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPostName", "");                        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_POS_OFF_ID_AD", null);                               
            return true;                                                                                 
        }                                                                                              

        if (!ra.isLDBExists("PostOfficeLookUpLDB")) {
            ra.createLDB("PostOfficeLookUpLDB");
        }
        ra.setAttribute("PostOfficeLookUpLDB", "pol_map_id", polMapIdAd);

        LookUpRequest lookUpRequest = new LookUpRequest("PostOfficeLookUp");                       
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_POS_OFF_ID_AD", "pos_off_id");           
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtPostNr", "postal_code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtPostName", "pos_off_name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");                                                                  
            return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
            return false;                                                                              
        }  
        if(ra.getCursorPosition().equals("RealEstate_txtPostNr")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtPostName")){
            ra.setCursorPosition(1);
        }   

        return true;  
    }//RealEstate1_txtPostNr_FV

    public boolean zaKraj(){
        //Datumi procjene
        Date pocetak = null;
        Date kraj = null;
        Date datumProcjene = null;
        
        if(ra.getScreenContext().compareTo("scr_change")== 0 || ra.getScreenContext().compareTo("scr_update")== 0){
           
            pocetak = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDatnFrom");
            kraj = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDatnUnti");
            datumProcjene = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnDate");

            System.out.println(pocetak);
            System.out.println(kraj);
            
            if(datumProcjene == null){
                //Nije upisan datum procjene 
                ra.showMessage("wrnclt30b");
                return false;
            }

            if((datumProcjene != null) && (kraj != null)){
                if(datumProcjene.after(kraj)){
                    //Datum procjene ne moze biti veci od datuma vazenja procjene.
                    ra.showMessage("wrnclt30");
                    return false;
                }//datum procjene ne smije biti veci od kraja vazenja procjene
            }
            if((datumProcjene != null) && (pocetak != null)){
                if(datumProcjene.after(pocetak)){
                    ra.showMessage("wrnclt30a");
                    return false;
                }//datum procjene ne smije biti veci od pocetka vazenja procjene
            }
            if((pocetak != null) && (kraj != null)){
                if(kraj.before(pocetak)){
                    ra.showMessage("wrnclt31");
                    return false;
                }
            }

            //Povrsina i cijena jednog kvadrata
            BigDecimal cijenaM2 = null;
            BigDecimal nominalnaCijena = null;
            BigDecimal povrsina = null;
            nominalnaCijena = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNomiValu");
            povrsina = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2");

            // Milka, 27.09.2006 zakomentirano za ROBI      
            if((nominalnaCijena == null) || (povrsina == null)){
                return false; 
            }

            if(nominalnaCijena != null){
                if(povrsina != null){
                    if (coll_util.isAmount(povrsina)) {
                        cijenaM2 = nominalnaCijena.divide(povrsina,2,BigDecimal.ROUND_HALF_UP);
                        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPricem2",cijenaM2);
                    }
                }
            }

            java.sql.Date datum = null;
            datum = (java.sql.Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateRecDoc");
            if(datum == null){
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc","N");
            }else{
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc","D");
            }
            datum = null;
        }

        //Dokumentacija
        String comDoc = null;
        java.sql.Date dateRecDoc = null;
        String missingDoc = null;
        java.sql.Date dateToDoc = null;

        if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc") != null){
            comDoc = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc");
            if(comDoc.compareTo("D")== 0){
                dateRecDoc = (java.sql.Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateRecDoc");
                if(dateRecDoc == null){
                    dateRecDoc = null;
                    return false;
                }
            }
            if(comDoc.compareTo("N")== 0){
                dateToDoc = (java.sql.Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateToDoc");
                missingDoc = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtMissingDoc");
                if(missingDoc == null){
                    return false;
                }
                if(dateToDoc == null){
                    return false;
                }
            }
        }else{
            return false;
        }
        return true;
    }
    
    public void postavi_nekeUserDate(){

        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new java.sql.Date(timeT);

        if(ra.getScreenContext().compareTo("scr_change")== 0){
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_USE_ID",(BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_USE_OPEN_ID",(BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtUseOpenIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtUseOpenIdName",(String) ra.getAttribute("GDB", "Use_UserName"));

            ra.setAttribute(MASTER_LDBNAME, "RealEstate_ORIGIN_ORG_UNI_ID",(BigDecimal) ra.getAttribute("GDB", "org_uni_id"));
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_ORG_UNI_ID",(BigDecimal) ra.getAttribute("GDB", "org_uni_id"));            

            java.sql.Date datumOd = null;
            datumOd = (java.sql.Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRealEsDateFrom");

            if(datumOd == null){
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRealEsDateFrom", todaySQLDate); 
            }
            datumOd = null;
            calendar = null;  

            java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
            java.sql.Date datumDo = null;
            datumDo = (java.sql.Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRealEsDateUnti");

            if(datumDo == null){
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRealEsDateUnti", vvDatUntil); 
            }
            datumDo = null;
            vvDatUntil = null;
        }
        
        if(ra.getScreenContext().compareTo("scr_update")== 0){
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_USE_ID",(BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));

            ra.setAttribute(MASTER_LDBNAME, "RealEstate_ORG_UNI_ID",(BigDecimal) ra.getAttribute("GDB", "org_uni_id"));

            java.sql.Date datumOd = null;
            datumOd = (java.sql.Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRealEsDateFrom");

            if(datumOd == null){
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRealEsDateFrom", todaySQLDate); 
            }
            datumOd = null;
            calendar = null;

            java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
            java.sql.Date datumDo = null;
            datumDo = (java.sql.Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRealEsDateUnti");

            if(datumDo == null){
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRealEsDateUnti", vvDatUntil); 
            }
            datumDo = null;
            vvDatUntil = null;
        }

    }//postavi_nekeUserDate

    public void postavi_za_insert(){
        //      REVA_COEF                 RealEstate_txtRevaCoef     0.00  kod inserta  Ovo se samo revalorizacijom azurira. Obican update ne azurira ovaj stupac.                                                                                                                                                                                                                                                                                                                                                                                                                        
        //      REVA_DATE                 RealEstate_txtRevaDate                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
        //      REVA_DATE_AM              RealEstate_txtRevaDateAM   I kod inserta   kod update nekretnine se ne mijenja                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        //      REVA_BVALUE               RealEstate_txtRevaBValue                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
        //      REVA_BDATE                RealEstate_txtRevaBDate                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
        //      REVA_BDATE_AM             RealEstate_txtRevaBDateAM  I kod inserta   kod update nekretnine se ne mijenja                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRevaCoef",numberZero); 
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRevaDate",null);   
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRevaDateAM","I");  
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRevaBValue",numberZero);   
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRevaBDate",null);  
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRevaBDateAM","I"); 

        //      THIRD_RIGHT               RealEstate_txtThirdRight                                                                                  
        //      THIRD_RIGHT_CUR_ID        RealEstate_THIRD_RIGHT_CUR_ID RealEstate_txtThirdRightCurCodeChar                                                                         
        //      THIRD_RIGHT_NOM           RealEstate_txtThirdRightInNom                                                                                 
        //      THIRD_RIGHT_DATE          RealEstate_txtThirdRightDate   
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRight",numberZero);   
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRightInNom",numberZero);  
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRightDate",todaySQLDate);

        //      HFS_VALUE                 RealEstate_txtHfsValue                                                                        
        //      HFS_VALUE_DATE            RealEstate_txtHfsValueDate                                                                    
        //      HFS_VALUE_LAST_ONE        RealEstate_txtHfsValueLastOne                                                                 
        //      HFS_DATE_LAST_ONE         RealEstate_txtHfsDateLastOne  
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtHfsValue",numberZero);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtHfsValueDate",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtHfsValueLastOne",numberZero);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtHfsDateLastOne",null);
        //      SUM_LIMIT_VAL             RealEstate_txtSumLimitVal                                                                     
        //      SUM_LIMIT_DAT             RealEstate_txtSumLimitDat                                                                     
        //      SUM_PART_VAL              RealEstate_txtSumPartVal                                                                      
        //      SUM_PART_DAT              RealEstate_txtSumPartDat                                                                      
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtSumLimitVal",numberZero);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtSumLimitDat",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtSumPartVal",numberZero);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtSumPartDat",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNomiDate",todaySQLDate);

        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoDate",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighDate",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailDate",todaySQLDate);

        // Milka, 23.10.2006 - nekretnina osigurana-defaultno se puni sa N - mijenja se na D kada se upise polica osiguranja
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtInspolInd", "N");        

        // Milka, 27.10.2006 - upisano pravo banke-defaultno se puni sa N - mijenja se na D kada je na sve RBA hipoteke upisano pravo banke
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRecLop","N"); 
        // Milka, 31.08.2009 - CRM misljenje postavlja se na D
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRealEstSpecStat","D");  
        ra.setAttribute(MASTER_LDBNAME,"Kol_txtCRMHnb_REstate","N"); 
        // osim za slijedece vrste nekretnina
        //        KATEGORIJA        VRSTA                               PODVRSTA                        CRM 
        //        Nekretnine        2POP - Poslovni objekt(prostor)     PROIHALA - Proizvodni objekt    ne
        //        Nekretnine        2TOO - Turistièki objekt            HOTEL    - Hotel                ne
        //        Nekretnine        2TOO - Turistièki objekt            PANSION  - Pansion              ne
        //        Nekretnine        2TOO - Turistièki objekt            HOTNAS   - Hotelsko naselje     ne
        //        Nekretnine        2TOO - Turistièki objekt            2TOOBAZ  - Bazen                ne 
        // col_cat_id = 618223;
        // col_typ_id = 9777 and real_es_type_id = 7222 
        // col_typ_id = 10777  and real_es_type_id in (10222,11222,31931223,154714356223) 
        BigDecimal col_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID"); 
        BigDecimal col_sub_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID"); 
        coll_util.setRealEstateCRMopinion(col_typ_id, col_sub_typ_id);              

    }//postavi_za_insert

    public void postavi_za_update(){
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoDate",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighDate",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailDate",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNomiDate",todaySQLDate);
    }

    public void confirm() {
        String scr_ctx = (String)ra.getScreenContext();
        
        if(scr_ctx.compareTo("scr_change")== 0 || scr_ctx.compareTo("scr_update")== 0){ 
            if(ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtNomiDate") == null){
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNomiDate",todaySQLDate);
            }
            if(!coll_util.checkIfFieldsAreFilled((new CollReqFields()).GetRealEstateReqData())) return;
            
            //dodatna provjera samo za aktivni ekran
            if (!ra.isRequiredFilled()) { return; }
            
            if (!zaKraj()){
                ra.showMessage("infclt1"); 
                return; 
            }           

            ra.setAttribute(MASTER_LDBNAME,"numberRe",new Integer(0));

            ra.invokeAction("checkReEs");
            Integer numberRealEst = (Integer)ra.getAttribute(MASTER_LDBNAME,"numberRe");

            if( numberRealEst.intValue()  > 0){
                 System.out.println("Zapis s istom kombinacijom vec postoji - Broj zapisa je  numberRealEst |" + numberRealEst.intValue() + "|" );
                Integer go_forward = (Integer) ra.showMessage("qer_clt001");
                if (go_forward != null && go_forward.intValue() == 1) {

                }else{
                    return;
                }
            }            
        }
        //postaviti flag da radim i potvrdu podataka
        //save_ver_aut_flag = 1 - spremanje i potvrda
        ra.setAttribute(MASTER_LDBNAME, "save_ver_aut_flag", "1"); 
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_USER_LOCK_IN", ra.getAttribute(MASTER_LDBNAME, "RealEstate_USER_LOCK"));

        BigDecimal colHeaId = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_COL_HEA_ID");
        // System.out.println("colHeaId " + colHeaId);

        if(scr_ctx.equalsIgnoreCase("scr_change") && colHeaId != null){
            ra.setScreenContext("scr_update");
        }

        if(scr_ctx.compareTo("scr_change")== 0){
            try {
                ra.executeTransaction();
                ra.showMessage("infclt2");
            } catch (VestigoTMException vtme) {
                error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            ra.invokeAction("owners");
        }//scr_change za transakciju

        // Milka, 02.10.2006 za ROBI       
        if((scr_ctx.compareTo("scr_update")== 0) || (scr_ctx.compareTo("scr_update_aut")== 0) ||
                (scr_ctx.compareTo("scr_update_ref")== 0) || scr_ctx.compareTo("scr_owner_bank")== 0){
            // System.out.println("Pocetak promjene");
            
            // FBPr200017059 - Petra, 24.09.2012 dodana provjera za izmjenu podataka procijenitelja o vrijednosti nekretnine
            Integer del = null;
            String coConfirm = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtCoConfirm");
            if(coConfirm.compareToIgnoreCase("D")==0 && hasChanged()){
                // podaci su promijenjeni - potrebno obrisati kontrolu kolateral officera
                ra.setAttribute(MASTER_LDBNAME, "EstimateDataChg", "D");
                del = (Integer) ra.showMessage("qerdnp2"); 
            }else del = (Integer) ra.showMessage("qer002");
            
            if (del != null && del.intValue() == 1) {
                // System.out.println("Odabrao promjenu");
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
        
        // System.out.println("samo za stambene nekretnine + ");        
        // samo za stambene nekretnine    
//        BigDecimal tipKolateralaBD_new = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID");
//        BigDecimal podtipKolateralaBD_new = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_TYPE");
//        //        System.out.println("samo za stambene nekretnine:  "+ tipKolateralaBD_new);   
//        if (tipKolateralaBD_new.compareTo(new BigDecimal("8777")) == 0 && podtipKolateralaBD_new.compareTo(new BigDecimal("1156953223")) != 0 ){
//            ra.setAttribute(MASTER_LDBNAME, "Kol_txtCRMHnb_REstate",ra.getAttribute(MASTER_LDBNAME,"Kol_txtCRM_Hnb_pom"));
//        }

    }//confirm 

    /**
     * Metoda koja provjerava da li je došlo do promjene u poljima 'Podaci procjenitelja' ne ekranu s obzirom 
     * na one koji su povuèeni iz baze(coll_head i coll_restate) prilikom ulaska na ekran
     * @return true ako je promjenjen barem jedan podatak, false inaèe
     */
    public Boolean hasChanged(){
               
        System.out.println("Ušao u provjeru - hasChanged");
        
        BigDecimal est_cus_id_new = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_EUSE_ID");
        BigDecimal for_est_cus_id_new = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_RCEstimateId");
        BigDecimal estn_value_new = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnValu");
        BigDecimal cur_id_new = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_NM_CUR_ID");
        BigDecimal new_build_value_new = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNewBuildVal");
        String est_nomi_desc_new = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnMarkDesc");
        Date estn_date_new = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnDate");

        if(!isEqual(est_cus_id, est_cus_id_new)) return true;
        if(!isEqual(for_est_cus_id, for_est_cus_id_new)) return true;
        if(!isEqual(estn_value, estn_value_new)) return true;
        if(!isEqual(cur_id, cur_id_new)) return true;
        if(!isEqual(new_build_value, new_build_value_new)) return true;
        if(!isEqual(est_nomi_desc, est_nomi_desc_new)) return true;
        if(!isEqual(estn_date, estn_date_new)) return true;
       
        return false;
    }
    
    public void co_history(){
        System.out.println("Otvaram ekran COHistoryViewList");
        ra.loadScreen("COHistoryViewList");       
    }
    
    
    public void checkReEs(){
        try {
            ra.executeTransaction();

        } catch (VestigoTMException vtme) {
            error("RealEstateDialog -> check(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
        // System.out.println("Broj zapisa je   " +  ra.getAttribute(MASTER_LDBNAME,"numberRe"));
    }

    public void fetchValuesHfPrior(){

        try {
            ra.executeTransaction();

        } catch (VestigoTMException vtme) {
            error("RealEstateDialog -> check(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }

        BigDecimal  weighValueP = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtWeighValue");                                
        BigDecimal  thirdRightNomP = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRightInNom");
        BigDecimal  hfsValueP = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtHfsValue");               

        if(thirdRightNomP == null){
            thirdRightNomP = numberZero;
        }
        if(hfsValueP == null){
            thirdRightNomP = numberZero;
        }
        if((weighValueP != null) && (thirdRightNomP != null) && (hfsValueP != null) ){

            BigDecimal  availValueP   =  weighValueP.subtract(thirdRightNomP);                                                                                                                                 
            availValueP = availValueP.subtract(hfsValueP);                                                                  
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailValue",availValueP);         
        }

        java.sql.Date   hfsDateLastOne = (java.sql.Date)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtHfsDateLastOne");
        java.sql.Date   thirdRightDate = (java.sql.Date)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRightDate");

        if((hfsDateLastOne != null) && (thirdRightDate == null)){
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailDate",(java.sql.Date)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtHfsDateLastOne"));
        }
        if((hfsDateLastOne == null) && (thirdRightDate != null)){
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailDate",(java.sql.Date)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRightDate"));
        }
        if((hfsDateLastOne != null) && (thirdRightDate != null)){
            if(hfsDateLastOne.after(thirdRightDate)){
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailDate",hfsDateLastOne);
            }else{
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAvailDate",thirdRightDate);
            }
        }
    }

    //  potvrde collateral officer-a
    public void confirm_co() {       

        if (!(ra.isLDBExists("CollCoChgHistLDB"))) {
            ra.createLDB("CollCoChgHistLDB");
        }  
        
        // FBPr200017059 - provjera da li korisnik ima ovlaštenje potrebno za izvoğenje akcije
        if((!(new SpecialAuthorityManager(ra)).checkSpecialAuthorityCodeForApplicationUser("aix", "COLL_OFF"))) {
            ra.showMessage("insufficientVerAuthority");
            return;
        }
        
        // pitanje da li stvarno zeli potvrditi podatke      
        Integer retValue = (Integer) ra.showMessage("col_qer004");
        if (retValue!=null && retValue.intValue() == 0) return;             

        //postavljanje atribute za insert u CO_CHG_HISTORY
        ra.setAttribute("ColWorkListLDB", "co_chg_use_id", null);  

        ra.setAttribute("ColWorkListLDB", "co_ind", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtCoConfirm"));
        //potvrda CO samo ako nije co_ind= D-> ako je vec potvrden -> poruka
        System.out.println("Ispis Coll_txtCoConfirm:"+ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtCoConfirm").toString());  
        if(!ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtCoConfirm").toString().equalsIgnoreCase("D")){
            try {
                ra.executeTransaction();
                ra.showMessage("infclt3");
            } catch (VestigoTMException vtme) {
                error("RealEstateDialogLDB -> confirm_coll(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }               
        }else{
            ra.showMessage("wrn_dist_rep21");
            return;
        }

        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCoConfirm",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirm"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCoConfirmUserId",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmUserId"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCoConfirmUserName",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmUserName"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCoConfirmTime",ra.getAttribute("CollCoChgHistLDB", "Kol_txtCoConfirmTime"));

    }//confirm_coll  

    public void exit() {
        // Milka, 09.06.2009, ako nije popunjeno polje za RBA prihvatljivost postaviti ga          
        if (ra.getScreenID().equals("RealEstateDialog")){
            ra.exitScreen();
            ra.invokeAction("refresh");
        }else{
            ra.exitScreen();
            ra.exitScreen();
            ra.invokeAction("refresh");
        }  
    }//exit

    public void setAmortization1(){
        BigDecimal nomiValu = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNomiValu");
        BigDecimal numberOneA = new BigDecimal("1.00000000000000000000000000000000");
        BigDecimal amortPerCal12 = new BigDecimal("1600983003.0");
        BigDecimal collTypeIdLandF = new BigDecimal("7777.0");
        BigDecimal collTypeIdLand = null;
        String vijekAmortizacije = null;
        BigDecimal iznosAmortizacije = null;
        int brojGodina = 0;
        BigDecimal periodAmortizacijeBD = null;

        collTypeIdLand = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID");

        if(collTypeIdLand != null){

            if(collTypeIdLand.compareTo(collTypeIdLandF)== 0){
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortAge","0");
            }else{
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortAge","50");
            }
            vijekAmortizacije = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtAmortAge");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortPerCal","12MTH");
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_AMORT_PER_CAL_ID",amortPerCal12);

            if(vijekAmortizacije != null){
                vijekAmortizacije = vijekAmortizacije.trim();
                if(vijekAmortizacije.compareTo("0") == 0){
                    iznosAmortizacije = new BigDecimal("0.00");
                    ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortValCal", iznosAmortizacije);

                }else{ 
                    brojGodina = java.lang.Integer.parseInt(vijekAmortizacije);
                    periodAmortizacijeBD = new BigDecimal(brojGodina);
                    if(nomiValu == null){
                        iznosAmortizacije = new BigDecimal("0.00");
                        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortValCal", iznosAmortizacije);

                    }else{
                        iznosAmortizacije = nomiValu.divide(periodAmortizacijeBD,64,BigDecimal.ROUND_HALF_UP);
                        iznosAmortizacije = iznosAmortizacije.divide(numberOneA,2,BigDecimal.ROUND_HALF_UP);
                        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortValCal", iznosAmortizacije);
                    }
                }
            }
        }
    }//setAmortization1

    public boolean RealEstate_txtRealEstSpecStat_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRealEstSpecStat", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       
        request.addMapping(MASTER_LDBNAME, "RealEstate_txtRealEstSpecStat", "Vrijednosti");

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
    
    public boolean Kol_txtCRMHnb_REstate_FV(String ElName, Object ElValue, Integer LookUp) {        
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "Kol_txtCRMHnb_REstate", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       
        request.addMapping(MASTER_LDBNAME, "Kol_txtCRMHnb_REstate", "Vrijednosti");

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
    
    public boolean RealEstate_txtConstructionRight_FV(String ElName, Object ElValue, Integer LookUp) {
        boolean ret=coll_lookups.ConfirmDN(MASTER_LDBNAME, ElName, ElValue);
        if(ret) {
            String val = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtConstructionRight");
            if(val.equals("D")){
                coll_util.enableFields(new String[] {"RealEstate_txtZKConstructionRight", "RealEstate_txtConstructionRightDateTo"}, 0);
                coll_util.setRequired(new String[] {"RealEstate_txtZKConstructionRight", "RealEstate_txtConstructionRightDateTo"}, true);
                ra.setCursorPosition("RealEstate_txtZKConstructionRight");
            }else{
                coll_util.enableFields(new String[] {"RealEstate_txtZKConstructionRight", "RealEstate_txtConstructionRightDateTo"}, 2);
                coll_util.setRequired(new String[] {"RealEstate_txtZKConstructionRight", "RealEstate_txtConstructionRightDateTo"}, false);
                coll_util.clearFields(MASTER_LDBNAME, new String[] {"RealEstate_txtZKConstructionRight", "RealEstate_txtConstructionRightDateTo"});
            }
        }
        return ret;        
    }    
    
    public boolean RealEstate_YesNoLookUp_FV(String ElName, Object ElValue, Integer LookUp) {
        return coll_lookups.ConfirmDN(MASTER_LDBNAME, ElName, ElValue);
    }
    
    public boolean RealEstate_ProjectFinanc_FV(String ElName, Object ElValue, Integer LookUp) {
        boolean rez = coll_lookups.ConfirmDN(MASTER_LDBNAME, ElName, ElValue);
        
        if(rez){
            String value=ra.getAttribute(MASTER_LDBNAME, ElName);
            if(value.equals("N")) coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtFutureValueAmount");
        }
        return rez;
    }
    
    
    public boolean RealEstate_txtNonInsReasone_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME,new String[]{"RealEstate_txtNonInsReasoneCode", "RealEstate_txtNonInsReasoneDesc"});
            return true;        
        }
        
        if(ra.getCursorPosition().equals("RealEstate_txtNonInsReasoneCode")){
            coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtNonInsReasoneDesc");
        }else if(ra.getCursorPosition().equals("RealEstate_txtNonInsReasoneDesc")){
            coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtNonInsReasoneCode");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtNonInsReasoneCode", "RealEstate_txtNonInsReasoneDesc", "coll_NonInsReasone");
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtNonInsReasoneCode", value.sysCodeValue);
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtNonInsReasoneDesc", value.sysCodeDesc);  
        return true;
    }  
    
    public boolean RealEstate_Lift_FV(String ElName, Object ElValue, Integer LookUp) {
        return coll_lookups.ConfirmDN(MASTER_LDBNAME, ElName, ElValue);        
    }  
    
    public boolean RealEstate_txtFloor_FV(String ElName, Object ElValue, Integer LookUp) {
        boolean isInt=true;
        int kat=0;
        try {
            kat=Integer.parseInt((String)ElValue);
        } catch (Exception e) {
            isInt=false;
        }
        if(isInt && kat>0 && kat<100) return true;
        else if (!isInt && (ElValue.equals("") || ElValue.equals("PR") || ElValue.equals("PO") || ElValue.equals("SU") || ElValue.equals("VP"))) return true;
        else {
            ra.showMessage("qwszstColl08");
            return false;
        }
    } 
    
    public boolean RealEstate_txtTotalFloors_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")) {
            //ra.showMessage("qwszstColl09");
            return true;
        }
        int kat=((Integer) ElValue).intValue();
        if(kat>0 && kat<100) return true;
        else {
            ra.showMessage("qwszstColl09");
            return false;
        }
    }     
    
    public boolean RealEstate_txtContractType_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME, new String[]{ "RealEstate_txtContractTypeCode", "RealEstate_txtContractTypeDesc"});
            return true;        
        }
        if (ra.getCursorPosition().equals("RealEstate_txtContractTypeCode")) {
            coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtContractTypeDesc");
            ra.setCursorPosition(2);
        } else if(ra.getCursorPosition().equals("RealEstate_txtContractTypeDesc")){
            coll_util.clearField(MASTER_LDBNAME,"RealEstate_txtContractTypeCode");
        }
        String sys_code_id="coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code");
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtContractTypeCode", "RealEstate_txtContractTypeDesc", sys_code_id);
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtContractTypeCode", value.sysCodeValue);
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtContractTypeDesc", value.sysCodeDesc);  
        return true;
    }
    
    /**
     * Metoda usporeğuje dva broja
     * @param first BigDecimal
     * @param second BigDecimal
     * @return true  - ako su jednaki
     *         false  - ako su razlièiti
     */
    public boolean isEqual(BigDecimal first, BigDecimal second) {

        if(first == null){
            if(second == null) return true;
            else return false;
        }else{
            if(second == null) return false;
            else if (first.compareTo(second) != 0) return false;
            return true;
        }
    }
    
    /**
     * Metoda usporeğuje dva datuma
     * @param first Date
     * @param second Date
     * @return true   - ako su jednaki
     *         false  - ako su razlièiti
     */
    public boolean isEqual(Date first, Date second) {

        if(first == null){
            if(second == null) return true;
            else return false;
        }else{
            if(second == null) return false;
            else if (first.compareTo(second) != 0) return false;
            return true;
        }
    }
    
    /**
     * Metoda usporeğuje dva stringa
     * @param first String
     * @param second String
     * @return true   - ako su jednaki
     *         false  - ako su razlièiti
     */
    public boolean isEqual(String first, String second) {

        first = isEmpty(first);
        second = isEmpty(second);
        
        if(first.equals("") && second.equals("")) return true;
        
        return first.equalsIgnoreCase(second);
    }
    
    /**
     * Metoda koja provjerava da li je parametar null
     * @param s String za koji se radi provjera
     * @return Prazan string ako je proslijeğeni parametar null, odnosno trim-ani string
     */
    private String isEmpty(String s){
        if (s == null) return ""; else return s.trim();
    }
    
    private void SetDefaultValuesOnLDB(){
        //POSTAVLJAM U POMOCNE VARIJABLE NA LDB-U VRIJEDNOSTI DA SE ZAPAMTE KOJE SU BILE NA ULAZU NA EKRAN
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtFloorOLD", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtFloor"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTotalFloorsOLD", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtTotalFloors"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtLiftOLD", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtLift"));
        //ako je samo kontekst scr_change to znaci da se radi unos novoga i ova polja se postavljaju na D
        if(ra.getScreenContext().equalsIgnoreCase("scr_change")){  
            //postavljam stare vrijednosti ovih polja na neku vrijednost za insert novog kolaterala zbog provjere koja ce se raditi pri snimanju
            //jer kada su stare vrijednosti popunjene onda su polja RealEstate_txtB2HNB,Reb_RealEstate_txtB2IRB obavezna pri snimanju, a kod novog kolaterala ova polja
            //su obavezna. Obaveznost se postavlja i mice u handleru RealEstateDialog2 u screen entry funkciji
            coll_util.SetDefaultValue("RealEstateDialogLDB_B", "Reb_RealEstate_txtB2HNB", "D");
            coll_util.SetDefaultValue("RealEstateDialogLDB_B", "Reb_RealEstate_txtB2IRB", "D");
            coll_util.SetDefaultValue(MASTER_LDBNAME, "RealEstate_txtB2HNB", "D");
            coll_util.SetDefaultValue(MASTER_LDBNAME, "RealEstate_txtB2IRB", "D");
            coll_util.SetDefaultValue(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod1Code", "2");
            coll_util.SetDefaultValue(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod2Code", "3");
            coll_util.SetDefaultValue(MASTER_LDBNAME, "RealEstate_txtConstructionRight", "N");       
            coll_util.SetDefaultValue(MASTER_LDBNAME, "RealEstate_txtBuildProjectFinanc", "N");
            coll_util.SetDefaultValue(MASTER_LDBNAME, "RealEstate_txtContractTypeCode", coll_util.GetDefaultValueFromSystemCodeValue("coll_cnt_typ_" + ra.getAttribute("ColWorkListLDB", "code")));
        }
   
        BigDecimal rationNGVInsu=ra.getAttribute("CollCommonDataLDB", "Coll_txtRationNGVInsurance");        
        if(rationNGVInsu!=null && rationNGVInsu.compareTo(new BigDecimal("1"))<=0){
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtInsTotalCoverCode", "D");
        }else{
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtInsTotalCoverCode", "N");
        }
    }  
    
    private void ScreenEntry_FV(){
        ra.invokeValidation("RealEstate_txtNonInsReasoneCode");
        ra.invokeValidation("RealEstate_txtRiskCode"); 
        ra.invokeValidation("RealEstate_txtContractTypeCode");
    }
    
    private void SetRequiredKatAndLift(){
        BigDecimal tip=ra.getAttribute(MASTER_LDBNAME,"RealEstate_COL_TYPE_ID");
        BigDecimal podtip=ra.getAttribute(MASTER_LDBNAME,"RealEstate_REAL_EST_TYPE"); 
        String context=ra.getScreenContext();
        String lift=(String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtLift");
        String kat=(String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtFloor");
        Integer katova=(Integer)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtTotalFloors");         
        boolean checkLift = (lift!=null && !"".equals(lift)); 
        boolean checkKat = (kat!=null && !"".equals(kat)); 
        boolean checkKatova = (katova!=null); 
        
        //ako je insert novog kolaterala onda je obavezno polje lift
        //ako je update postojeceg i lift je vec popunjen onda se postavlja na obavezno, inace nije obavezno na update-u  
        if(context.equals("scr_change") || checkLift){
            if(tip.equals(new BigDecimal("7777")) || podtip.equals(new BigDecimal("4222"))){
                ra.setRequired("RealEstate_txtLift", false);
            }else{
                ra.setRequired("RealEstate_txtLift", true);
            }
        }
        if(podtip.equals(new BigDecimal("5222"))){
            if(context.equals("scr_change") || checkKat) coll_util.setRequired(new String[]{"RealEstate_txtFloor"}, true);
            if(context.equals("scr_change") || checkKatova) coll_util.setRequired(new String[]{"RealEstate_txtTotalFloors"}, true);
        }else if (podtip.equals(new BigDecimal("1156953223"))){
            if(context.equals("scr_change") || checkKatova) coll_util.setRequired(new String[]{"RealEstate_txtTotalFloors"}, true);
            coll_util.setRequired(new String[]{"RealEstate_txtFloor"}, false);
        }else{
            coll_util.setRequired(new String[]{"RealEstate_txtFloor","RealEstate_txtTotalFloors"}, false);
        }
    }     
}
