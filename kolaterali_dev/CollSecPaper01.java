package hr.vestigo.modules.collateral;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;

import hr.vestigo.framework.common.TableData;   
         
import java.math.BigDecimal;   
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.Vector;

import com.ibm.rmi.corba.IsA;

import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.CollateralCmpUtil;  
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;
import hr.vestigo.modules.rba.util.DateUtils;
                      
/**     
 * @author HRAMKR  
 *  
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollSecPaper01 extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollSecPaper01.java,v 1.113 2018/01/09 10:38:23 hrazst Exp $";
    
    CollateralUtil coll_util= null;
    CollateralCmpUtil coll_cmp_util = null;
    LookUps coll_lookups = null;

    public CollSecPaper01(ResourceAccessor ra) {
        super(ra);
        coll_util = new CollateralUtil(ra);
        coll_cmp_util = new CollateralCmpUtil(ra);
        coll_lookups = new LookUps(ra);
    }
    
    public void CollSecPaper01_SE() {
                  
    }
    
    public void CollSecPaperOF_SE() { 
        String rba_eligibility = null;
        String b2_eligibility = null;
        String b2irb_eligibility = null;
        String b1_eligibility = null;
        
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");   
        BigDecimal col_typ_id = (BigDecimal) ra.getAttribute("CollHeadLDB", "COL_TYPE_ID");               
        String low_eligibility = (String)ra.getAttribute("CollHeadLDB", "KolLow_txtEligibility");
        String upisana_hipoteka = (String) ra.getAttribute("CollHeadLDB", "Coll_txtRecLop"); 
        String crm_opinion = (String) ra.getAttribute("CollHeadLDB", "SPEC_STATUS"); 
        
        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {           
            rba_eligibility = (String) ra.getAttribute("CollHeadLDB", "ColRba_txtEligibility");  
            // za udjele u poduzecu RBA prihvatljivost se automatski postavlja uvijek na N
            if (col_cat_id.compareTo(new BigDecimal("629223")) == 0) {
                rba_eligibility = coll_util.chk_RBA_eligibility_for_all(col_cat_id, col_typ_id, low_eligibility, null, 
                        upisana_hipoteka, null, null, null, null, null);
                ra.setAttribute("CollHeadLDB", "ColRba_txtEligibility",rba_eligibility);
            }
            b2_eligibility = (String) ra.getAttribute("CollHeadLDB", "Coll_txtEligibility");                
            b2irb_eligibility = (String) ra.getAttribute("CollHeadLDB", "Coll_txtB2IRBEligibility");             
            b1_eligibility = (String) ra.getAttribute("CollHeadLDB", "Coll_txtB1Eligibility");
            //validacija rba prihvatljivosti               
            if (ra.getAttribute("CollHeadLDB", "ColRba_txtEligibility") != null) {
                ra.setAttribute("CollHeadLDB", "ColRba_txtEligDesc","");
                ra.setCursorPosition("ColRba_txtEligibility");
                ra.invokeValidation("ColRba_txtEligibility");
                ra.setCursorPosition("Coll_txtNmValCurr");  
            }       
            //pozvati samo ako nije popunjena RBA da bi se ispravno postavili nazivi
            if (rba_eligibility == null || rba_eligibility.equals("")) {     
                //validacija b2 prihvatljivosti               
                if (ra.getAttribute("CollHeadLDB", "Coll_txtEligibility") != null) {
                    ra.setAttribute("CollHeadLDB", "Coll_txtEligDesc","");
                    ra.setCursorPosition("Coll_txtEligibility");
                    ra.invokeValidation("Coll_txtEligibility");
                    ra.setCursorPosition("Coll_txtNmValCurr");  
                }
                // validacija B1 prihvatljivosti
                if (ra.getAttribute("CollHeadLDB", "Coll_txtB1Eligibility") != null) {
                    ra.setAttribute("CollHeadLDB", "Coll_txtB1EligDesc","");
                    ra.setCursorPosition("Coll_txtB1Eligibility");
                    ra.invokeValidation("Coll_txtB1Eligibility");
                    ra.setCursorPosition("Coll_txtNmValCurr");
                }               
                // validacija B2 IRB prihvatljivosti
                if (ra.getAttribute("CollHeadLDB", "Coll_txtB2IRBEligibility") != null) {
                    ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligDesc","");
                    ra.setCursorPosition("Coll_txtB2IRBEligibility");
                    ra.invokeValidation("Coll_txtB2IRBEligibility");
                    ra.setCursorPosition("Coll_txtNmValCurr");
                }  
            }  
        }   
        if (ra.getScreenContext().equalsIgnoreCase("scr_update") || ra.getScreenContext().equalsIgnoreCase("scr_change")) {
            coll_util.setVrpSecondCtx(col_cat_id);
        } else {
            coll_util.setVrpSecondCtxUdjeliUPod(col_cat_id);
        }
        coll_util.hideFields(new String[]{
                "Coll_lblSumLimitVal","Coll_txtSumLimitVal",
                "Coll_lblNepoPerCal","Coll_txtNepoPerCal",
                "Coll_lblSumLimitDat","Coll_txtSumLimitDat"
                });  
    }
    
    public void CollMovable01_SE() {
        
        
    }   
        
       
    public void CollArt01_SE() {
        
          
    }   
      
    public void CollPrec01_SE() {
        
        
    }        
 
    public void Vessel01_SE() {
        
        
    }       
      
    public void CollCashDep01_SE() {
        
         
    } 
    
    public void CollInsPol01_SE() {
        
          
    }       
    
    public void CollGuarant01_SE() {
        
         
    }    

    public void CollLoanStock01_SE() {
        
         
    }   
    
    public boolean Coll_txtComDoc_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollHeadLDB", "Coll_txtComDoc", null);
            return true;
        } 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollHeadLDB", "Coll_txtComDoc", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }  

// ako su vozila i ako je knjizica dostavljena aktiviraju se dodatna polja za unos
        String vrsta = (String) ra.getAttribute("ColWorkListLDB","code");
        if ((vrsta != null) && (vrsta.equals("VOZI"))) {
            coll_util.setVehLicenceCtx(ra);
            String doc = ((String) ra.getAttribute("CollHeadLDB", "Coll_txtComDoc")).trim();
            if ((doc != null) && (doc.equals("D"))) {
                ra.setCursorPosition("Vehi_txtVehVehLicence");
            } else if ((doc != null) && (doc.equals("N"))) {
                ra.setCursorPosition("Vehi_txtVehVehLicence");  
            }

        }
        
        return true;
    }      
            
     public boolean Coll_txtDateRecDoc_FV(){
        java.sql.Date datum = null;
        datum = (java.sql.Date) ra.getAttribute("CollHeadLDB", "Coll_txtDateRecDoc");
// za vozila ne smije biti veci od danasnjeg datuma         
            String code = (String) ra.getAttribute("ColWorkListLDB", "code");
            if (code.equalsIgnoreCase("VOZI")) {
                Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
                if (datum == null || current_date == null) 
                    return true;

                if ((current_date).before(datum)) {
                    ra.showMessage("wrnclt121");
                    return false;
                }               
            } else {
            
                if(datum == null){
                    ra.setAttribute("CollHeadLDB", "Coll_txtComDoc","N");
                }else{
                    ra.setAttribute("CollHeadLDB", "Coll_txtComDoc","D");
                }
            }

        return true;
      
     }//Coll_txtDateRecDoc_FV

  

     public boolean Coll_txtNomiValu_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null) {
            ra.setAttribute("CollHeadLDB", "Coll_txtNomiValu", null); 
            ra.setAttribute("CollHeadLDB", "Coll_txtNepoValue", null);   
            return true;                                                                                 
        }
    
        String vrsta = (String) ra.getAttribute("ColWorkListLDB","code");       
        ra.setAttribute("CollHeadLDB", "Coll_txtNepoValue", ra.getAttribute("CollHeadLDB", "Coll_txtNomiValu"));

          
// ovo punim samo za policu osiguranja, gotovinski depozit, garanciju, vrijednosne papire   
        if (vrsta.equalsIgnoreCase("OBVE") || vrsta.equalsIgnoreCase("DION") || 
            vrsta.equalsIgnoreCase("ZAPI") || vrsta.equalsIgnoreCase("UDJE") ||
            vrsta.equalsIgnoreCase("GARA") || vrsta.equalsIgnoreCase("CASH") || vrsta.equalsIgnoreCase("INSP")) {
            ra.setAttribute("CollHeadLDB", "Coll_txtEstnValu", ra.getAttribute("CollHeadLDB", "Coll_txtNomiValu"));
        }
 
        ra.setAttribute("CollHeadLDB","Coll_txtAcouBValue",ra.getAttribute("CollHeadLDB","Coll_txtAcouValue"));
        ra.setAttribute("CollHeadLDB","Coll_txtAcouBDate",ra.getAttribute("CollHeadLDB","Coll_txtAcouDate"));
        
        coll_util.getPonderAndRestAmount(ra);
        
           
        return true;
    
    }     
     
    public boolean Coll_txtNomiDate_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null) {                                          
            ra.setAttribute("CollHeadLDB", "Coll_txtNepoDate", null);                        
            ra.setAttribute("CollHeadLDB", "Coll_txtNomiDate", null);   
            return true;                                                                                 
        }
        // moze se unijeti datum u buducnosti,ali samo veci od current date +1
        Date future_date =DateUtils.addOrDeductDaysFromDate((Date) ra.getAttribute("GDB", "ProcessingDate"), 1, true);
        Date date_from = (Date) ra.getAttribute("CollHeadLDB", "Coll_txtNomiDate");       
        
        if ((future_date).before(date_from)) {
            ra.showMessage("errzstColl01");
            return false;
        }
        //ra.setAttribute("CollHeadLDB", "Coll_txtNepoDate", ra.getAttribute("CollHeadLDB", "Coll_txtNomiDate")); 
        //ra.setAttribute("CollHeadLDB", "Coll_txtAcouDate", ra.getAttribute("CollHeadLDB", "Coll_txtNomiDate"));    
            
        return true;
        
    }      

    public void coll_eventview(){
        
        String code = (String) ra.getAttribute("ColWorkListLDB", "code");
        
        if (code.equalsIgnoreCase("NEKR")) {
            if (ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID")==null) {
                ra.showMessage("wrnclt103");
                return;             
            }
        } else if (code.equalsIgnoreCase("MJEN")) {
            if (ra.getAttribute("CollBoeDialogLDB","CollBoeDialog_COL_HEA_ID")==null) {
                ra.showMessage("wrnclt103");
                return;             
            }           
        } else {
            if(ra.getAttribute("CollHeadLDB", "COL_HEA_ID")==null){
                ra.showMessage("wrnclt103");
                return;
            }
        }
 
        
        
        if (!(ra.isLDBExists("ContractLDB"))) {
            ra.createLDB("ContractLDB");
        }
     
        if (code.equalsIgnoreCase("NEKR")) 
            ra.setAttribute("ContractLDB","cus_acc_id",ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_HEA_ID"));    
        else if (code.equalsIgnoreCase("MJEN"))
            ra.setAttribute("ContractLDB","cus_acc_id",ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_HEA_ID"));                
        else
            ra.setAttribute("ContractLDB","cus_acc_id",ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));       
     
        
        ra.loadScreen("fin_reconstruction_event_list_cus_acc_id");

    }   

    public void coll_exposure () {

        String code = (String) ra.getAttribute("ColWorkListLDB", "code");
        
        if (code.equalsIgnoreCase("NEKR")) {
            if (ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID")==null) {
                ra.showMessage("wrnclt103");
                return;             
            }
        } else if (code.equalsIgnoreCase("MJEN")) {
            if (ra.getAttribute("CollBoeDialogLDB","CollBoeDialog_COL_HEA_ID")==null) {
                ra.showMessage("wrnclt103");
                return;             
            }                       
        } else {
            if(ra.getAttribute("CollHeadLDB", "COL_HEA_ID")==null){
                ra.showMessage("wrnclt103");
                return;
            }  
        }
  
       
        
        if (!(ra.isLDBExists("CusaccExpCollCallerLDB"))) {
            ra.createLDB("CusaccExpCollCallerLDB");
        }
     
        if (code.equalsIgnoreCase("NEKR")) 
            ra.setAttribute("CusaccExpCollCallerLDB","COL_HEA_ID",ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_HEA_ID")); 
        else if (code.equalsIgnoreCase("MJEN")) 
            ra.setAttribute("CusaccExpCollCallerLDB","COL_HEA_ID",ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_HEA_ID")); 
        else
            ra.setAttribute("CusaccExpCollCallerLDB","COL_HEA_ID",ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));        
     
            
        ra.loadScreen("CusaccExpColl","scr_detail");        
        
        
    }
    
    
    public boolean Coll_txtDepAcc_FV (String elementName, Object elementValue, Integer lookUpType) {
        
        BigDecimal bank = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","cde_cus_id");
        
        String ldbName = "CollSecPaperDialogLDB";
         
        //provjerava se da li je racun u CASHDEP_EXCEPTION tablici. Ako je onda se omoguæava ruèno ažuriranje "Datum  c.o." 
        try {
            ra.executeTransaction();
            Boolean isAccountException=(Boolean)ra.getAttribute("CollSecPaperDialogLDB", "IsCashDepExceptionAccount");
            System.out.println(isAccountException);
            if(!isAccountException){
                ra.setAttribute("CollHeadLDB","Coll_txtNomiDate", ra.getAttribute("CollOldLDB", "Coll_txtNomiDate_O"));
            }
        } catch (Exception e) {
            
        }        
      
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Coll_txtDepAcc",null);
            return true;
        }
        // ako je depozit kod RBA pokrenuti validaciju partije 8218251
        if (bank != null && bank.compareTo(new BigDecimal("8218251")) == 0) {
            if (coll_util.ctrlCashDepozitAccount((String) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtDepAcc"))) {
            return true;
            } else {
                ra.showMessage("wrnclt144");
                return false;
            }  
        } 
        
        return true;        
    } 


    public boolean ColRba_txtEligibility_FV(String ElName, Object ElValue, Integer LookUp)
    {
        if (ElValue == null || ElValue.equals("")) {
            coll_util.clearFields("CollHeadLDB", new String[]{"ColRba_txtEligibility", "ColRba_txtEligDesc"});
            return true;
        }

        if (ra.getCursorPosition().equals("ColRba_txtEligibility")) {
            coll_util.clearField("CollHeadLDB", "ColRba_txtEligDesc");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("ColRba_txtEligDesc")) {
            coll_util.clearField("CollHeadLDB", "ColRba_txtEligibility");
        } 
        if (ra.isLDBExists("RealEstateDialogLDB")) ra.setAttribute("RealEstateDialogLDB", "SysCodId", "clt_eligib");
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue("CollHeadLDB", "ColRba_txtEligibility", "ColRba_txtEligDesc", "clt_rba_eligib");
        if(value==null) return false;
        
        ra.setAttribute("CollHeadLDB", "ColRba_txtEligibility", value.sysCodeValue);
        ra.setAttribute("CollHeadLDB", "ColRba_txtEligDesc", value.sysCodeDesc);
        
        this.SetRbaEligibility();
        return true;
    } // ColRba_txtEligibility_FV


    public boolean ColLow_txtEligibility_FV(String ElName, Object ElValue, Integer LookUp)
    {  
        String code = (String)ra.getAttribute("ColWorkListLDB", "code");
        String ldbName = "";
        
        if (code.equalsIgnoreCase("NEKR")) {
            ldbName = "RealEstateDialogLDB";
        }
        
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(ldbName, new String[]{"ColLow_txtEligibility", "ColLow_txtEligDesc"});
            return true;        
        } 
        ra.setAttribute(ldbName, "ColLow_txtEligDesc", "");
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(ldbName, "ColLow_txtEligibility", "ColLow_txtEligDesc", "clt_eligib");
        if(value==null) return false;
        
        ra.setAttribute(ldbName, "ColLow_txtEligibility", value.sysCodeValue);
        ra.setAttribute(ldbName, "ColLow_txtEligDesc", value.sysCodeDesc);  
        return true;         
    } // ColLow_txtEligibility_FV 
    
    public boolean KolLow_txtEligibility_FV(String ElName, Object ElValue, Integer LookUp) {        
        String ldbName = "CollHeadLDB";

        if (ElValue == null || ElValue.equals("")) {
            coll_util.clearFields(ldbName, new String[]{"KolLow_txtEligibility", "KolLow_txtEligDesc"});
            return true;
        }
 
        if (ra.isLDBExists("RealEstateDialogLDB")) ra.setAttribute("RealEstateDialogLDB", "SysCodId", "clt_eligib");
        coll_util.clearField(ldbName, "KolLow_txtEligDesc");
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(ldbName, "KolLow_txtEligibility", "KolLow_txtEligDesc", "clt_eligib");
        if(value==null) return false;

        ra.setAttribute(ldbName, "KolLow_txtEligibility", value.sysCodeValue);
        ra.setAttribute(ldbName, "KolLow_txtEligDesc", value.sysCodeDesc);
        this.SetRbaEligibility();
        
        return true;
    } // KolLow_txtEligibility_FV

    private void SetRbaEligibility(){
        String code = (String)ra.getAttribute("ColWorkListLDB", "code");
        // prihvatljivost za rba
        String rba_eligibility = null;
        String low_eligibility = (String)ra.getAttribute("CollHeadLDB", "KolLow_txtEligibility");
        String upisana_hipoteka = (String)ra.getAttribute("CollHeadLDB", "Coll_txtRecLop"); 
        String first_call = (String)ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtFirstCall");
        BigDecimal col_cat_id = (BigDecimal)ra.getAttribute("ColWorkListLDB", "col_cat_id");
        BigDecimal col_typ_id = (BigDecimal)ra.getAttribute("CollHeadLDB", "COL_TYPE_ID");
        Date maturity_date = (Date)ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtCdeDepUnti");
        String cesija_naplata = (String)ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtCesijaNaplata");
  
        // Milka. 09.06.2009, za kolaterala kod kojih se rucno unosi RBA prihvatljivost ne postavlja se default
        // Milka, 22.10.2009, dodan rucni unos i za depozite
        if (code.equalsIgnoreCase("CASH") || code.equalsIgnoreCase("GARA") || 
            code.equalsIgnoreCase("CESI") || 
            code.equalsIgnoreCase("ZADU") || code.equalsIgnoreCase("VOZI") || 
            code.equalsIgnoreCase("POKR") || code.equalsIgnoreCase("PLOV") || 
            code.equalsIgnoreCase("ZALI") || code.equalsIgnoreCase("INSP") || 
            code.equalsIgnoreCase("DION") || code.equalsIgnoreCase("OBVE") || 
            code.equalsIgnoreCase("ZAPI") || code.equalsIgnoreCase("UDJE")) {              
            rba_eligibility = (String)ra.getAttribute("CollHeadLDB", "ColRba_txtEligibility");
        } else {           
            rba_eligibility = coll_util.chk_RBA_eligibility_for_all(col_cat_id, col_typ_id, low_eligibility,
                maturity_date, upisana_hipoteka, first_call, null, null, null, cesija_naplata);
        } 

        ra.setAttribute("CollHeadLDB", "ColRba_txtEligibility", rba_eligibility);
    }
    

    public boolean Kol_txtNotName_FV(String elementName, Object elementValue, Integer lookUpType){
//       validacija javnog bilježnika

        if (elementValue == null || ((String) elementValue).equals("")) {  
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtNotName", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtNotAdr", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "pub_not_id", null);   
            
            //nuliraj i mjesto
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtNotCty", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "not_cty_id", null);           
             
            return true;                                                                                 
        }                                                                                              
                  
        if (!ra.isLDBExists("PubNotLDB")) {
            ra.createLDB("PubNotLDB");
        }
        ra.setAttribute("PubNotLDB", "p_not_cty_id", ra.getAttribute("CollSecPaperDialogLDB", "not_cty_id"));
                
        LookUpRequest lookUpRequest = new LookUpRequest("PubNotLookUp");                       
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "pub_not_id", "pub_not_id");           
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Kol_txtNotName", "name");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Kol_txtNotAdr", "adress");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "not_cty_id", "pol_map_id");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Kol_txtNotCty", "city");     
        
 
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
//  Kol_txtNotName_FV
    
    public boolean Kol_txtNotCty_FV(String elementName, Object elementValue, Integer lookUpType){
// validacija mjesta u kojem je javnobiljeznicki ured
        BigDecimal placeTypeId = new BigDecimal("5999.0");

        if (elementValue == null || ((String) elementValue).equals("")) {  
            
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtNotCty", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "not_cty_id", null);
            //nuliraj i biljeznika
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtNotName", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtNotAdr", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "pub_not_id", null);      
            return true;                                                                                 
        }                                                                                              
        
        if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }
        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", placeTypeId);
        
        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");                       
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "not_cty_id", "pol_map_id");           
        lookUpRequest.addMapping("CollHeadLDB", "dummySt", "code");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Kol_txtNotCty", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
                ra.showMessage("err012");                                                                  
                return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
                return false;                                                                              
        }  
        
        
        
        return true;  
     }//Kol_txtNotCty_FV
//  
     
// za vozila
    
    public boolean Vehi_txtVehSubCode_FV (String elementName, Object elementValue, Integer lookUpType){
// ne moze upisati podgrupu ako nije odabrana grupa vozila
        
        if (elementValue == null || ((String) elementValue).equals("")) {  
            
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehSubCode", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehSubDesc", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "veh_subgr_id", null);      
            return true;                                                                                 
        }  

        if ((ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtSecTypeCode") == null) || 
                ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtSecTypeCode").equals("")){
            ra.showMessage("wrnclt119");
            return false;
        }
        
        if (!ra.isLDBExists("VehSubGroLookUpLDB")) {
            ra.createLDB("VehSubGroLookUpLDB");
        }       
        
        ra.setAttribute("VehSubGroLookUpLDB","gro_id", ra.getAttribute("CollSecPaperDialogLDB","SEC_TYP_ID"));
        ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehSubDesc", "");
        
        LookUpRequest lookUpRequest = new LookUpRequest("VehSubGroLookUp");                       
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "veh_subgr_id", "veh_sub_id");           
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Vehi_txtVehSubCode", "veh_sub_code");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Vehi_txtVehSubDesc", "veh_sub_desc");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                                
        } catch (EmptyLookUp elu) {                                                                    
            ra.showMessage("err012");   
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehSubCode", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehSubDesc", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "veh_subgr_id", null);      
            return true;                                                                              
        } catch (NothingSelected ns) {    
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehSubCode", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehSubDesc", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "veh_subgr_id", null);      
                return true;                                                                              
        }  
        
        return true;
    }
    

    public boolean Vehi_txtVehCouNumCode_FV(String ElName, Object ElValue, Integer LookUp) {
        

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehCouNumCode", ""); 
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehCouCharCode", ""); 
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehCouName", ""); 
            
            ra.setAttribute("CollSecPaperDialogLDB", "veh_cou_id_prod", null);                               
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Vehi_txtVehCouNumCode")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehCouCharCode", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Vehi_txtVehCouCharCode")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehCouNumCode", "");
        }

            
        ra.setAttribute("CollHeadLDB", "dummySt", null);
        ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehCouName", "");
        
        LookUpRequest request = new LookUpRequest("CountryLookUp");
        request.addMapping("CollSecPaperDialogLDB", "veh_cou_id_prod", "cou_id");
        request.addMapping("CollSecPaperDialogLDB", "Vehi_txtVehCouNumCode", "shortcut_num");
        request.addMapping("CollSecPaperDialogLDB", "Vehi_txtVehCouCharCode", "shortcut_char");     
        request.addMapping("CollHeadLDB", "dummySt", "cou_iso_code");           
        request.addMapping("CollSecPaperDialogLDB", "Vehi_txtVehCouName", "name");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }   
        return true;
    } // Vehi_txtVehCouNumCode_FV
    

    public boolean Vehi_txtVehKasko_FV(String ElName, Object ElValue, Integer LookUp) {
        String val = (String) ra.getAttribute("CollSecPaperDialogLDB", "Vehi_txtVehKasko");
        System.out.println("Vehi_txtVehKasko="+val);
        if(val.equals("D")){
            coll_util.enableFields(new String[] {"Coll_txtNonInsReasoneCode", "Coll_txtNonInsReasoneDesc"}, 2);
            coll_util.clearFields("CollHeadLDB", new String[]{"Coll_txtNonInsReasoneCode", "Coll_txtNonInsReasoneDesc"});
       }else{
            coll_util.enableFields(new String[]{"Coll_txtNonInsReasoneCode", "Coll_txtNonInsReasoneDesc"}, 0);                
        }
        return true;        
    }   
    
    public boolean Kol_txtVehState_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtVehState", "");   
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtVehStateDsc", ""); 
    
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Kol_txtVehState")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtVehStateDsc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Kol_txtVehStateDsc")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtVehState", "");
        } 
        
        if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
            ra.createLDB("UserCodeValueLookUpLDB");
        }               
        ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "vehicle_state");           
        
        ra.setAttribute("CollHeadLDB", "dummyBD", null);
     
        LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");

        lookUpRequest.addMapping("CollHeadLDB", "dummyBD", "use_cod_val_id");                       
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Kol_txtVehState", "use_code_value");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Kol_txtVehStateDsc", "use_code_desc");

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
    
      
// datum procjene - moze biti manji ili jednak danasnjem
    public boolean Coll_txtEstnDate_FV(String elementName, Object elementValue, Integer lookUpType){

//datum mora biti manji ili jednak current date


        Date doc_date = (Date) ra.getAttribute("CollHeadLDB", "Coll_txtEstnDate");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (doc_date == null || current_date == null) 
            return true;

        if ((current_date).before(doc_date)) {
            ra.showMessage("wrnclt117");
            return false;
        }
        return true;            
    }   
    
// datum izdavanja knjizice na revers   
    
    public boolean Vehi_txtVehDateLic_FV(String elementName, Object elementValue, Integer lookUpType){

//      datum mora biti manji ili jednak current date

        Date doc_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Vehi_txtVehDateLic");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (doc_date == null || current_date == null) 
            return true;

        if ((current_date).before(doc_date)) {
            ra.showMessage("wrnclt121");
            return false;
        }
        
// ako je upisan datum izdavanja knjizice na revers postaviti odgovarajuce kontekste
        coll_util.setVehLicenceReversCtx(ra);
        
        return true;            
    }       
    
    public boolean Vehi_txtVehDateLicTo_FV(String elementName, Object elementValue, Integer lookUpType){
// datum do kada treba vratiti knjizicu
//      datum mora biti veci od datuma izdavanja knjizice na revers
//      datum mora biti veci od current date
        
        Date doc_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Vehi_txtVehDateLic");
        Date doc_revers = (Date) ra.getAttribute("CollSecPaperDialogLDB","Vehi_txtVehDateLicTo");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        
        if (doc_date == null || doc_revers == null) 
            return true;

        if ((doc_revers).before(doc_date)) {
            ra.showMessage("wrnclt120");
            return false;
        }
        if ((doc_revers).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }       
        
        return true;            
    }       

     
// da li je knjizica vozila vracena 
    public boolean Vehi_txtVehLicReturn_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehLicReturn", null);
            return true;
        }
 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Vehi_txtVehLicReturn", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }

//       postaviti ctx za knjizicu vozila

        coll_util.setVehLicenceReversCtx(ra);   
        
        return true;
    }       


// da li je dostavljena polica obaveznog osiguranja 

    public boolean Vehi_txtVehInsurance_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehInsurance", null);
            return true;
        }
 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Vehi_txtVehInsurance", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        } 
        // ako je dostavljena treba aktivirati polja za unos osiguravatelja
        coll_util.setVehInsuranceCtx(ra);
        String doc = ((String) ra.getAttribute("CollSecPaperDialogLDB", "Vehi_txtVehInsurance")).trim();
        if ((doc != null) && (doc.equals("D"))) {
            ra.setCursorPosition("Vehi_txtInsId");
        } else if ((doc != null) && (doc.equals("N"))) {
            ra.setCursorPosition("Vehi_txtVehInsDate");
        }
        
        return true;
    }           

    
// vrsta upisa
    

    
    public boolean Vehi_txtFidTyp_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTyp", "");   
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTypDsc", ""); 
    
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Vehi_txtFidTyp")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTypDsc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Vehi_txtFidTypDsc")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTyp", "");
        } 
        
        if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
            ra.createLDB("UserCodeValueLookUpLDB");
        }               
        ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "fid_typ");             
        
        ra.setAttribute("CollHeadLDB", "dummyBD", null);
     
        LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");

        lookUpRequest.addMapping("CollHeadLDB", "dummyBD", "use_cod_val_id");                       
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Vehi_txtFidTyp", "use_code_value");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Vehi_txtFidTypDsc", "use_code_desc");

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
      
   
// za garancije
    
    
    public boolean Kol_txtGuarAmount_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount", null);                        
                                                                              
        }
// procijenjena 
        ra.setAttribute("CollHeadLDB", "Coll_txtEstnValu", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount"));
// nominalna        
        ra.setAttribute("CollHeadLDB", "Coll_txtNomiValu", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount"));
// neponderirana        
        ra.setAttribute("CollHeadLDB", "Coll_txtNepoValue", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount"));
// ponderirana = neponderirana * MVP ponder, ponder je u %

        ra.setAttribute("CollHeadLDB","Coll_txtAcouBValue",ra.getAttribute("CollHeadLDB","Coll_txtAcouValue"));
        ra.setAttribute("CollHeadLDB","Coll_txtAcouBDate",ra.getAttribute("CollHeadLDB","Coll_txtAcouDate"));
        ra.setAttribute("CollHeadLDB", "Coll_txtNomiDate",ra.getAttribute("GDB","ProcessingDate"));
        

        /**
         * hraaks 22.102013 izracuna postotka garantiranja DEFTECT 6742
         */
      BigDecimal garant = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarAmount");
      BigDecimal init_garant = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB", "txt_inicijalni_iznos_garancije");
      System.out.println("garant:"+ garant);
      System.out.println("init_garant:"+ init_garant);
      
      /**
       * u svakom lookupu od kolaterala di se nesto racuna bilo bi dobro da se sve sto 
       * se racuna prvo provijeri jel to null 
       */
      System.out.println("aaaaa");   
      if(garant==null){
          System.out.println("bbbbbb");
          return false;
      }
 
      
      if(init_garant==null){
          ra.setAttribute("CollSecPaperDialogLDB", "txt_inicijalni_iznos_garancije", garant);
          init_garant = garant;
      }
      else if(init_garant.compareTo(garant)<0){
          // inicijalni iznos garancije ne moze biti manji od garantiranog iznosa
          ra.showMessage("coll_init_garant");
          return false;
      }
      
      BigDecimal percent = garant.divide(init_garant, 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100), new MathContext(6,RoundingMode.CEILING));
      BigDecimal percent_rounded = percent.setScale(2, RoundingMode.HALF_UP);
      
      System.out.println("percent:"+ percent);
      System.out.println("percent_rounded:"+ percent_rounded);
      ra.setAttribute("CollSecPaperDialogLDB", "txt_postotak_garantranja", percent_rounded);

            
        
        return true;

    }
      

    public boolean Kol_txtGuarCur_FV(String ElName, Object ElValue, Integer LookUp) {
    

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
                    ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtGuarCur", "");                        
                    ra.setAttribute("CollSecPaperDialogLDB", "guar_cur_id", null);                               
                    return true;                                                                                 
        } 

        ra.setAttribute("CollHeadLDB", "dummySt", "");

     
        
        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");                       
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "guar_cur_id", "cur_id");           
        lookUpRequest.addMapping("CollHeadLDB", "dummySt", "code_num");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Kol_txtGuarCur", "code_char");
        lookUpRequest.addMapping("CollHeadLDB", "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
                ra.showMessage("err012");                                                                    
                return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
                return false;                                                                              
        }    
         
// ako je valuta garancije kuna obavezno je polje valutna klauzula
        coll_util.setGuarValKlaCtx(ra);
                
           
// napuniti Coll_txtEstnCurr, Coll_txtNmValCurr
        ra.setAttribute("CollHeadLDB", "Coll_txtEstnCurr", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarCur"));                        
        ra.setAttribute("CollHeadLDB", "REAL_EST_NM_CUR_ID", ra.getAttribute("CollSecPaperDialogLDB", "guar_cur_id"));   
        ra.setAttribute("CollHeadLDB", "Coll_txtNmValCurr",ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarCur"));
        
        
// napuniti dinamicku labelu Kol_dlblAvailValueCur
        
        ra.setAttribute("CollHeadLDB", "Kol_dlblAvailValueCur",ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtGuarCur"));     
        return true;     
    }   
    
    

    public boolean Kol_txtCurInd_FV(String ElName, Object ElValue, Integer LookUp) {
        String CurIndOld = (String) ra.getAttribute("CollSecPaperDialogLDB", "garancije_val_klauzula");
//        System.out.println("CurIndOld: "  + CurIndOld);       
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtCurInd", null);
            return true;
        }

        
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Kol_txtCurInd", "Vrijednosti");

        try { 
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        } 
// ako su garancije proknjizene nije dozvoljena promjena indikatora valutne klauzule 
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");  
        String finnancial_flag = (String) ra.getAttribute("CollHeadLDB", "C_FINANCIAL_FLAG");
        String CurIndNew = (String) ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtCurInd");
//        System.out.println("CurIndOld: "  + CurIndOld);         
//        System.out.println("CurIndNew: "  + CurIndNew); 
//        System.out.println("finnancial_flag: "  + finnancial_flag); 
        if (col_cat_id.compareTo(new BigDecimal("615223")) == 0) {
//            System.out.println("tu sam : " ); 
            if (finnancial_flag != null && finnancial_flag.equals("1")) {
//                System.out.println("tu sam 1: " ); 
                if(!CurIndNew.equals(CurIndOld)) {
//                    System.out.println("tu sam 2: " ); 
                    ra.showMessage("wrnclt182");
                    ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtCurInd", CurIndOld);
                    return true;
                }
            }
        } 
        return true;
    }               
     
    
    public boolean Kol_txtIntFeeInd_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtIntFeeInd", null);
            return true;
        }
 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Kol_txtIntFeeInd", "Vrijednosti");

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
    

    public boolean Kol_txtAmortInd_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtAmortInd", null);
            return true;
        }
 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Kol_txtAmortInd", "Vrijednosti");

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
    
    
    
    public boolean Kol_txtRespiro_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtRespiro", null);
            return true;
        }
 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Kol_txtRespiro", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }
    
// ako je resopiro = D treba aktivirati polje za unos respiro datuma
        coll_util.setGuarRespiroCtx(ra);
        
        return true;
    }           
    
        
    
    public boolean Vehi_txtInsId_FV(String elementName, Object elementValue, Integer LookUp){

        if (elementValue == null || ((String) elementValue).equals("")) {                                    
                        
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtInsId", "");   
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtInsName", "");
            ra.setAttribute("CollSecPaperDialogLDB", "veh_ins_id", null);  
            ra.setAttribute("CollHeadLDB", "dummySt", null);  
            return true;                                                                                 
        }                                                                                               
        
        if (ra.getCursorPosition().equals("Vehi_txtInsId")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtInsName", "");
            ra.setAttribute("CollSecPaperDialogLDB", "veh_ins_id", null); 
            ra.setAttribute("CollHeadLDB", "dummySt", null);
            ra.setCursorPosition(1);
             
        } else if (ra.getCursorPosition().equals("Vehi_txtInsName")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtInsId", "");
            ra.setAttribute("CollSecPaperDialogLDB", "veh_ins_id", null); 
            ra.setAttribute("CollHeadLDB", "dummySt", null);
            ra.setCursorPosition(2);
        }    
        
         
         
        LookUpRequest lu = new LookUpRequest("InsuCompanyLookup");                                     
        lu.addMapping("CollSecPaperDialogLDB", "veh_ins_id", "ic_id");  
        lu.addMapping("CollSecPaperDialogLDB", "Vehi_txtInsId", "ic_register_no");
        lu.addMapping("CollSecPaperDialogLDB", "Vehi_txtInsName", "ic_name"); 
        lu.addMapping("CollHeadLDB", "dummySt", "ic_code"); 
        
                                                                                           
        try {                                                                                            
            ra.callLookUp(lu);                                                                           
        } catch (EmptyLookUp elu) {                                                                      
            ra.showMessage("err012");                                                                    
            return false;                                                                                
        } catch (NothingSelected ns) {                                                                   
            return false;                                                                                
        } 
      
       return true;                                                                                                                  
    } 
    
      
    
// za vozila

    public boolean Kol_txtVehiAmount_FV(String ElName, Object ElValue, Integer LookUp) {
                                        
        if (ElValue == null) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtVehiAmount", null);                        
 
//          return true;                                                                                 
        }  
// procijenjena 
        ra.setAttribute("CollHeadLDB", "Coll_txtEstnValu", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtVehiAmount"));
// nominalna        
        ra.setAttribute("CollHeadLDB", "Coll_txtNomiValu", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtVehiAmount"));
// neponderirana        
        ra.setAttribute("CollHeadLDB", "Coll_txtNepoValue", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtVehiAmount"));
// ponderirana = neponderirana * MVP ponder, ponder je u %
        
        coll_util.getPonderAndRestAmount(ra);
        return true;

    }
    

    public boolean Kol_txtVehiCur_FV(String ElName, Object ElValue, Integer LookUp) {
    

        if (ElValue == null || ((String) ElValue).equals("")) {                                          
                    ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtVehiCur", "");                        
                    ra.setAttribute("CollSecPaperDialogLDB", "veh_cur_id", null);                               
                    return true;                                                                                 
        } 

        ra.setAttribute("CollHeadLDB", "dummySt", "");
   
        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");                   
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "veh_cur_id", "cur_id");           
        lookUpRequest.addMapping("CollHeadLDB", "dummySt", "code_num");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Kol_txtVehiCur", "code_char");
        lookUpRequest.addMapping("CollHeadLDB", "dummySt", "name");

        try {                                                                                          
            ra.callLookUp(lookUpRequest);                                                              
        } catch (EmptyLookUp elu) {                                                                    
                ra.showMessage("err012");                                                                    
                return false;                                                                              
        } catch (NothingSelected ns) {                                                                 
                return false;                                                                              
        }    
          
  
           
// napuniti Coll_txtEstnCurr, Coll_txtNmValCurr
        ra.setAttribute("CollHeadLDB", "Coll_txtEstnCurr", ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtVehiCur"));                        
        ra.setAttribute("CollHeadLDB", "REAL_EST_NM_CUR_ID", ra.getAttribute("CollSecPaperDialogLDB", "veh_cur_id"));   
        ra.setAttribute("CollHeadLDB", "Coll_txtNmValCurr",ra.getAttribute("CollSecPaperDialogLDB", "Kol_txtVehiCur"));     

// preracunati ponderiranu i raspolozivu        
        
        return true;     
    }   
    
    public boolean Vehi_txtVehMadeYear_FV (String ElName, Object ElValue, Integer LookUp){
        
        if (ElValue == null || ((String) ElValue).equals(""))
            return true;
        
        String year = (String) ra.getAttribute("CollSecPaperDialogLDB","Vehi_txtVehMadeYear"); 
        
// ako nije upisana 4-znamenkasta godina - upozorenje
        
        if (year.trim().length() < 4) {
            ra.showMessage("wrnclt159");
            return false;
        }
        
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        String datum_string = current_date.toString();
        String godina = datum_string.substring(0,4);
        int godina_i = Integer.parseInt(godina);
        
        int godina_1900 = 1900;

        int year_i = Integer.parseInt(year);
        
        if (year_i > godina_i) {
            ra.showMessage("wrnclt126");
            return false;
        } else if (year_i < godina_1900) {
            ra.showMessage("wrnclt158");
            return false;           
        }
        
        return true;
        
    }
      
  
    
    public boolean Vehi_txtVehFPlateDate_FV(String elementName, Object elementValue, Integer lookUpType){

//      datum mora biti manji ili jednak current date


        Date doc_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Vehi_txtVehFPlateDate");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (doc_date == null || current_date == null) 
            return true;

        if ((current_date).before(doc_date)) {
            ra.showMessage("wrnclt121");
            return false;
        }
        return true;            
    }   
    
    public boolean Coll_txtDateToDoc_FV(String elementName, Object elementValue, Integer lookUpType){
//      datum mora biti veci ili jednak current date
// samo za vozila 
        String code = (String) ra.getAttribute("ColWorkListLDB", "code");
        if (code.equalsIgnoreCase("VOZI")) {
            Date doc_date = (Date) ra.getAttribute("CollHeadLDB", "Coll_txtDateToDoc");
            Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
            if (doc_date == null || current_date == null) 
                return true;

            if ((doc_date).before(current_date)) {
                ra.showMessage("wrnclt125");
                return false;
            }
            return true;                    
        }
        return true;
    }  

    

    public boolean Vehi_txtVehInsDate_FV(String elementName, Object elementValue, Integer lookUpType){
//      datum mora biti veci ili jednak current date
        Date doc_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Vehi_txtVehInsDate");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (doc_date == null || current_date == null) 
            return true;

        if ((doc_date).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }
        return true;                
    }

    
    // zalihe   
    public boolean Supp_txtKeeper_FV(String elementName, Object elementValue, Integer lookUpType){ 
        String ldbName = "CollSecPaperDialogLDB";

        if (elementValue == null || ((String) elementValue).equals("")) {
            coll_util.clearFields(ldbName, new String[] {"Supp_txtKeeper", "Supp_txtKeeperName"});
            return true;                                                                                 
        }   

        if (ra.getCursorPosition().equals("Supp_txtKeeper")) {
            coll_util.clearField(ldbName, "Supp_txtKeeperName");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Supp_txtKeeperName")) {
            coll_util.clearField(ldbName, "Supp_txtKeeper");
        } 
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(ldbName, "Supp_txtKeeper", "Supp_txtKeeperName", "clt_keeper");
        if(value==null) return false;
        
        ra.setAttribute(ldbName, "Supp_txtKeeper", value.sysCodeValue);
        ra.setAttribute(ldbName, "Supp_txtKeeperName", value.sysCodeDesc);  
        return true;         
    }
    

    public boolean Supp_txtMinValue_FV(String elementName, Object elementValue, Integer lookUpType) {

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Supp_txtMinValue", null);
            return true;
        }
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");      

        request.addMapping("CollSecPaperDialogLDB", "Supp_txtMinValue", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012"); 
            return false;  
        } catch (NothingSelected ns) {
            return false;

        }
        //ako je vrijednost D obavezan je unos minimalnog iznosa
        coll_util.setSupplyMinValueCtx(ra);        
        return true;
    }       
     
    
    public boolean Supp_txtPlace_FV (String ElName, Object ElValue, Integer LookUp) {
        
        java.math.BigDecimal placeTypeId = new java.math.BigDecimal("5999.0");

        if (ElValue == null || ((String) ElValue).equals("")) {  
            
            ra.setAttribute("CollSecPaperDialogLDB", "Supp_txtPlace", "");                        
            ra.setAttribute("CollSecPaperDialogLDB", "SUP_PLACE_ID", null);   
      
            return true;                                                                                 
        }                                                                                                 
        
        ra.setAttribute("CollHeadLDB", "dummySt", null);
        
        if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }
        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", placeTypeId);
        
        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");                       
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "SUP_PLACE_ID", "pol_map_id");           
        lookUpRequest.addMapping("CollHeadLDB", "dummySt", "code");
        lookUpRequest.addMapping("CollSecPaperDialogLDB", "Supp_txtPlace", "name");

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
    
    public boolean Coll_txtB1Eligibility_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            coll_util.clearFields("CollHeadLDB", new String[]{"Coll_txtB1Eligibility", "Coll_txtB1EligDesc"});
            return true;                                                                                 
        }
 
        if (ra.getCursorPosition().equals("Coll_txtB1Eligibility")) {
            coll_util.clearField("CollHeadLDB", "Coll_txtB1EligDesc");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtB1EligDesc")) {
            coll_util.clearField("CollHeadLDB", "Coll_txtB1Eligibility");
        }        
        if (ra.isLDBExists("RealEstateDialogLDB")) ra.setAttribute("RealEstateDialogLDB", "SysCodId", "clt_eligib");           

        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value = coll_lookups.SystemCodeValue("CollHeadLDB", "Coll_txtB1Eligibility", "Coll_txtB1EligDesc", "clt_eligib");
        if(value==null) return false;
        
        ra.setAttribute("CollHeadLDB", "Coll_txtB1Eligibility", value.sysCodeValue);
        ra.setAttribute("CollHeadLDB", "Coll_txtB1EligDesc", value.sysCodeDesc);  
        return true;
        
    } // Coll_txtB1Eligibility_FV
    
    
    public boolean Coll_txtB2IRBEligibility_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            coll_util.clearFields("CollHeadLDB", new String[]{"Coll_txtB2IRBEligibility", "Coll_txtB2IRBEligDesc"});
            return true;                                                                                 
        }
 
        if (ra.getCursorPosition().equals("Coll_txtB2IRBEligibility")) {
            coll_util.clearField("CollHeadLDB", "Coll_txtB2IRBEligDesc");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtB2IRBEligDesc")) {
            coll_util.clearField("CollHeadLDB", "Coll_txtB2IRBEligibility");
        }        
        if (ra.isLDBExists("RealEstateDialogLDB")) ra.setAttribute("RealEstateDialogLDB", "SysCodId", "clt_eligib");           

        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value = coll_lookups.SystemCodeValue("CollHeadLDB", "Coll_txtB2IRBEligibility", "Coll_txtB2IRBEligDesc", "clt_eligib");
        if(value==null) return false;
        
        ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligibility", value.sysCodeValue);
        ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligDesc", value.sysCodeDesc);  
        return true; 
    } // Coll_txtB2IRBEligibility_FV    
    
// ----------------------------
     
    public boolean Coll_txtISIN_FV(String elementName, Object elementValue, Integer lookUpType) {
        //dohvat podataka o VRP-u preko ISIN-a
        String ldbName = "CollSecPaperDialogLDB";
                    
        if (elementValue == null || ((String) elementValue).equals("")) {
            coll_util.clearFields(ldbName, new String[]{
                    "Coll_txtISIN","ISIN_ID","Coll_txtIssuerCode","Coll_txtIssuerName","ISSUER_ID",
                    "Vrp_txtRatingIssuer","Kol_txtOIB","Coll_txtDailyPublishPrice","Coll_txtSeniorityIndicator",
                    "Vrp_txtRatingLong","Vrp_txtRatingShort","rat_sco_id_long","rat_sco_id_short",         
                    "Coll_txtRefMarketCode","Coll_txtRefMarketDesc","MARKET_TYPE","Vrp_txtRefMarketInx",                                       
                    "Vrp_txtStopSell","Vrp_txtPeriodStopSell","Vrp_txtValutnaKlauzula","Coll_txtNominalAmountCur",                         
                    "NOMINAL_CUR_ID", "Coll_txtNomAmoCur","Coll_txtNominalAmount","Coll_txtNominalAmountKn",
                    "Coll_txtMarketPrice","Coll_txtMarketPriceKn","Coll_txtMarketPriceDate","Coll_txtAccruedInterest",   
                    "Coll_txtMaturityDate","Vrp_txtFond","Coll_txtNominalAmountTot","Coll_txtNominalAmountTotKn",
                    "Coll_txtTotalMarketValue","Coll_txtTotalMarketValueKn","Coll_txtMarketPriceFo",
                    "Coll_txtB2AssetCode","Coll_txtB2AssetDesc","Coll_lblIssuerTypeCode","Coll_lblIssuerTypeDesc",
                    "Vrp_txtRatingLong_MD","Vrp_txtRatingShort_MD","Vrp_txtRatingVP_SP","Vrp_txtRatingVP_MD",
                    "Vrp_txtRatingIndicator","Vrp_txtAgencyRating","Vrp_txtAgencyIssuerRating"
                });
            
            ra.setAttribute("CollHeadLDB", "REAL_EST_NM_CUR_ID",null);                    
            return true;
        }        
        String d_register_no = "";
        if (ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtISIN") != null){
            d_register_no = (String) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtISIN");
        }
           
        if (ra.isLDBExists("VrpIsinLDB")) {
            coll_util.clearFields("VrpIsinLDB", new String[]{
                    "col_cat_id","col_typ_id","col_in2_id","isin",
                    "cus_id","register_no","name","rating","rating_long",
                    "rating_short","sto_mar_id","market_code","market_dsc",
                    "stock_ind","nom_cur_id","nom_cur_code","nom_amount",
                    "price","price_date","int_rate","maturity_date",
                    "currency_clause","rat_typ_id_long_vrp","rat_typ_id_short_vrp",
                    "col_typ_id_code","oib_vrp"
            });            
        } else {
            ra.createLDB("VrpIsinLDB");
        }
   
        ra.setAttribute("VrpIsinLDB", "isin", ra.getAttribute(ldbName, "Coll_txtISIN"));
        ra.setAttribute("VrpIsinLDB", "col_cat_id", ra.getAttribute("ColWorkListLDB", "col_cat_id"));
        ra.setAttribute("VrpIsinLDB", "col_typ_id", ra.getAttribute("CollHeadLDB", "COL_TYPE_ID"));     
        
        LookUpRequest lookUpRequest = new LookUpRequest("VrpIsinLookUp");
        lookUpRequest.addMapping("VrpIsinLDB", "col_in2_id", "col_in2_id");
        lookUpRequest.addMapping("VrpIsinLDB", "isin", "isin");
        lookUpRequest.addMapping("VrpIsinLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("VrpIsinLDB", "register_no", "register_no");
        lookUpRequest.addMapping("VrpIsinLDB", "name", "name");
        lookUpRequest.addMapping("VrpIsinLDB", "maturity_date", "maturity_date");                
        lookUpRequest.addMapping("VrpIsinLDB", "rating", "rating");
        lookUpRequest.addMapping("VrpIsinLDB", "rating_long", "rating_long");
        lookUpRequest.addMapping("VrpIsinLDB", "rating_short", "rating_short");
        lookUpRequest.addMapping("VrpIsinLDB", "sto_mar_id", "sto_mar_id");
        lookUpRequest.addMapping("VrpIsinLDB", "market_dsc", "market_dsc");
        lookUpRequest.addMapping("VrpIsinLDB", "market_code", "market_code");       
        lookUpRequest.addMapping("VrpIsinLDB", "stock_ind", "stock_ind");
        lookUpRequest.addMapping("VrpIsinLDB", "nom_cur_id", "nom_cur_id");
        lookUpRequest.addMapping("VrpIsinLDB", "nom_cur_code", "nom_cur_code");
        lookUpRequest.addMapping("VrpIsinLDB", "nom_amount", "nom_amount");
        lookUpRequest.addMapping("VrpIsinLDB", "price", "price");
        lookUpRequest.addMapping("VrpIsinLDB", "price_date", "price_date");
        lookUpRequest.addMapping("VrpIsinLDB", "int_rate", "int_rate");
        lookUpRequest.addMapping("VrpIsinLDB", "currency_clause","currency_clause");
        lookUpRequest.addMapping("VrpIsinLDB", "rat_typ_id_long_vrp", "rat_typ_id_long_vrp");
        lookUpRequest.addMapping("VrpIsinLDB", "rat_typ_id_short_vrp","rat_typ_id_short_vrp");
        lookUpRequest.addMapping("VrpIsinLDB", "rat_sco_id_long_vrp", "rat_sco_id_long_vrp");
        lookUpRequest.addMapping("VrpIsinLDB", "rat_sco_id_short_vrp","rat_sco_id_short_vrp");        
        lookUpRequest.addMapping("VrpIsinLDB", "col_typ_id","col_typ_id");
        lookUpRequest.addMapping("VrpIsinLDB", "col_typ_id_code","col_typ_id_code");
        lookUpRequest.addMapping("VrpIsinLDB", "oib_vrp","oib_vrp");
        lookUpRequest.addMapping("VrpIsinLDB", "daily_price","daily_price");
        lookUpRequest.addMapping("VrpIsinLDB", "seniority_indic","seniority_indic");
        lookUpRequest.addMapping("VrpIsinLDB", "B2AssetCode","B2AssetCode"); 
        lookUpRequest.addMapping("VrpIsinLDB", "B2AssetDesc","B2AssetDesc"); 
        lookUpRequest.addMapping("VrpIsinLDB", "IssuerTypeCode","IssuerTypeCode");
        lookUpRequest.addMapping("VrpIsinLDB", "IssuerTypeDesc","IssuerTypeDesc"); 
        lookUpRequest.addMapping("VrpIsinLDB", "rat_typ_id_long_vrp_MD","rat_typ_id_long_vrp_MD"); 
        lookUpRequest.addMapping("VrpIsinLDB", "rat_typ_id_short_vrp_MD","rat_typ_id_short_vrp_MD");
        lookUpRequest.addMapping("VrpIsinLDB", "rat_sco_id_long_vrp_MD","rat_sco_id_long_vrp_MD");
        lookUpRequest.addMapping("VrpIsinLDB", "rat_sco_id_short_vrp_MD","rat_sco_id_short_vrp_MD");
        lookUpRequest.addMapping("VrpIsinLDB", "rating_long_MD","rating_long_MD");
        lookUpRequest.addMapping("VrpIsinLDB", "rating_short_MD","rating_short_MD");
        lookUpRequest.addMapping("VrpIsinLDB", "rating_VP_SP","rating_VP_SP");
        lookUpRequest.addMapping("VrpIsinLDB", "rating_VP_MD","rating_VP_MD");
        
        
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");  
            ra.setCursorPosition("Coll_txtISIN");
            return false;
        } catch (NothingSelected ns) {
            ra.setCursorPosition("Coll_txtISIN");
            return false;
        }

        //ako je istekao maturity date, ne dozvoliti odabir, takav VRP ne moze biti kolateral
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date maturity_date = (Date) ra.getAttribute("VrpIsinLDB", "maturity_date");
        //datum mora biti veci ili jednak current date

        if (maturity_date != null && current_date != null) { 
            if ((maturity_date).before(current_date)) {
                ra.showMessage("wrnclt150");
                ra.setAttribute(ldbName,"Coll_txtISIN", "");
                ra.setCursorPosition("Coll_txtISIN");
                return false;
            }
        }                
        
        ra.setAttribute("CollHeadLDB", "Coll_txtCollTypeCode", ra.getAttribute("VrpIsinLDB", "col_typ_id_code"));
        String vrsta = (String) ra.getAttribute("ColWorkListLDB","code");   
        BigDecimal col_typ_id_x = (BigDecimal)ra.getAttribute("VrpIsinLDB", "col_typ_id");
        
        //validirati vrstu obveznice    
        if (vrsta.equalsIgnoreCase("OBVE") && col_typ_id_x != null) {
            ra.setAttribute("CollHeadLDB", "Coll_txtCollTypeName", "");   
            ra.setCursorPosition("Coll_txtCollTypeCode");
            ra.invokeValidation("Coll_txtCollTypeCode");    
        }
        String posting_flag = (String) ra.getAttribute("ColWorkListLDB","posting_flag");
        //ako je VRP proknjizen, nije dozvoljena promjena VRP-a koja mijenja i valutu VRP-a
        
        if (posting_flag != null && posting_flag.equalsIgnoreCase("1")){
            //proknjizen, provjeriti staru i novu valutu 
            BigDecimal valuta_old = (BigDecimal) ra.getAttribute("CollHeadLDB", "REAL_EST_NM_CUR_ID");
            BigDecimal valuta_new = (BigDecimal) ra.getAttribute("VrpIsinLDB", "nom_cur_id");            
            if (coll_cmp_util.cmp_bdc(valuta_old, valuta_new)) {
                //valute su razlicite
                ra.showMessage("wrnclt151");
                return false;
            }                  
        }
        BigDecimal marketAmountOne = null;
        
        ra.setAttribute("CollSecPaperDialogLDB","ISIN_ID",ra.getAttribute("VrpIsinLDB", "col_in2_id"));
        ra.setAttribute("CollSecPaperDialogLDB","Coll_txtISIN",ra.getAttribute("VrpIsinLDB", "isin"));
        ra.setAttribute("CollSecPaperDialogLDB","Coll_txtIssuerCode",ra.getAttribute("VrpIsinLDB", "register_no"));
        ra.setAttribute("CollSecPaperDialogLDB","Coll_txtIssuerName",ra.getAttribute("VrpIsinLDB", "name"));
        ra.setAttribute("CollSecPaperDialogLDB","ISSUER_ID",ra.getAttribute("VrpIsinLDB", "cus_id"));
        ra.setAttribute("CollSecPaperDialogLDB","Kol_txtOIB",ra.getAttribute("VrpIsinLDB", "oib_vrp"));
        ra.setAttribute("CollSecPaperDialogLDB","Coll_txtDailyPublishPrice",ra.getAttribute("VrpIsinLDB", "daily_price"));
        ra.setAttribute("CollSecPaperDialogLDB","Coll_txtSeniorityIndicator",ra.getAttribute("VrpIsinLDB", "seniority_indic"));        
        ra.setAttribute("CollSecPaperDialogLDB","Coll_txtB2AssetCode", ra.getAttribute("VrpIsinLDB", "B2AssetCode"));
        ra.setAttribute("CollSecPaperDialogLDB","Coll_txtB2AssetDesc", ra.getAttribute("VrpIsinLDB", "B2AssetDesc"));
        ra.setAttribute("CollSecPaperDialogLDB","Coll_lblIssuerTypeCode", ra.getAttribute("VrpIsinLDB", "IssuerTypeCode"));
        ra.setAttribute("CollSecPaperDialogLDB","Coll_lblIssuerTypeDesc", ra.getAttribute("VrpIsinLDB", "IssuerTypeDesc"));        
        //rating vrp-a
        ra.setAttribute("CollSecPaperDialogLDB","Vrp_txtRatingIssuer", ra.getAttribute("VrpIsinLDB", "rating"));                            
        ra.setAttribute("CollSecPaperDialogLDB","Vrp_txtRatingLong", ra.getAttribute("VrpIsinLDB", "rating_long"));
        ra.setAttribute("CollSecPaperDialogLDB","Vrp_txtRatingShort", ra.getAttribute("VrpIsinLDB", "rating_short"));        
        ra.setAttribute("CollSecPaperDialogLDB","rat_typ_id_long", ra.getAttribute("VrpIsinLDB", "rat_typ_id_long_vrp"));
        ra.setAttribute("CollSecPaperDialogLDB","rat_typ_id_short", ra.getAttribute("VrpIsinLDB", "rat_typ_id_short_vrp"));          
        ra.setAttribute("CollSecPaperDialogLDB","rat_sco_id_long", ra.getAttribute("VrpIsinLDB", "rat_sco_id_long_vrp"));
        ra.setAttribute("CollSecPaperDialogLDB","rat_sco_id_short", ra.getAttribute("VrpIsinLDB", "rat_sco_id_short_vrp"));
        //burza          
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtRefMarketCode", ra.getAttribute("VrpIsinLDB", "market_code"));   
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtRefMarketDesc", ra.getAttribute("VrpIsinLDB", "market_dsc")); 
        ra.setAttribute("CollSecPaperDialogLDB", "MARKET_TYPE", ra.getAttribute("VrpIsinLDB", "sto_mar_id"));              
        ra.setAttribute("CollSecPaperDialogLDB","Vrp_txtRefMarketInx",ra.getAttribute("VrpIsinLDB", "stock_ind"));
        //podaci na drugom ekranu, valuta nominale, nominalna vrijednost...          
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtNominalAmountCur", ra.getAttribute("VrpIsinLDB", "nom_cur_code"));                        
        ra.setAttribute("CollSecPaperDialogLDB", "NOMINAL_CUR_ID", ra.getAttribute("VrpIsinLDB", "nom_cur_id"));               
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtNominalAmount",ra.getAttribute("VrpIsinLDB", "nom_amount"));
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtMarketPrice",ra.getAttribute("VrpIsinLDB", "price"));
        
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingLong_MD",ra.getAttribute("VrpIsinLDB", "rating_long_MD"));
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingShort_MD",ra.getAttribute("VrpIsinLDB", "rating_short_MD"));
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingVP_SP",ra.getAttribute("VrpIsinLDB", "rating_VP_SP"));
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingVP_MD",ra.getAttribute("VrpIsinLDB", "rating_VP_MD"));
        
        //za obveznice treba cijenu podijeliti sa 100
        if (vrsta.equalsIgnoreCase("OBVE")) {
            marketAmountOne = (BigDecimal) ra.getAttribute("VrpIsinLDB", "price");
            marketAmountOne = coll_util.setDivideTwoBigDec_obveznice(marketAmountOne, 2);
            ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtMarketPrice",marketAmountOne);
        }  
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtMarketPriceDate",ra.getAttribute("VrpIsinLDB", "price_date"));    
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtAccruedInterest",ra.getAttribute("VrpIsinLDB", "int_rate"));  
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtMaturityDate",ra.getAttribute("VrpIsinLDB", "maturity_date")); 
        ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtNomAmoCur", ra.getAttribute("VrpIsinLDB", "nom_cur_code")); 
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzula", ra.getAttribute("VrpIsinLDB", "currency_clause"));
        //pregaziti ostali podaci
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtStopSell", "");   
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtPeriodStopSell", ""); 
        
        //ako je udjel u fondu napuniti  
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
        if (col_cat_id.compareTo(new BigDecimal("622223")) == 0) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtFond",ra.getAttribute("VrpIsinLDB", "price"));     
        }
         
        //napuniti Coll_txtEstnCurr, Coll_txtNmValCurr                                
        ra.setAttribute("CollHeadLDB", "REAL_EST_NM_CUR_ID", ra.getAttribute("CollSecPaperDialogLDB", "NOMINAL_CUR_ID"));   
    
        //preracunati iznose
        coll_util.countVrpAmounts(ra);
        //kontrola valutne klauzule
        
        if (ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzula") != null) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzulaDsc","");
            ra.setCursorPosition("Vrp_txtValutnaKlauzula");
            ra.invokeValidation("Vrp_txtValutnaKlauzula");
            //poslati cursor ovisno o col_cat_id
            if (col_cat_id.compareTo(new BigDecimal("622223")) == 0 || col_cat_id.compareTo(new BigDecimal("629223")) == 0)  
                ra.setCursorPosition("Vrp_txtStopSell");    
            else 
                ra.setCursorPosition("Vrp_txtRatingLong"); // dionice, obveznice i zapisi                    
        }
        this.setCollSecRatingFields();
        return true;
    }//Coll_txtISIN_FV
    
    
    public boolean Vrp_txtRatingLong_FV(String elementName, Object elementValue, Integer lookUpType){
        String ldbName = "CollSecPaperDialogLDB";
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Vrp_txtRatingLong","");
            ra.setAttribute(ldbName,"rat_sco_id_long",null);
            return true;
        }  
        
        if (ra.isLDBExists("VrpIsinLDB")) {
            ra.setAttribute("VrpIsinLDB", "col_cat_id", null);
            ra.setAttribute("VrpIsinLDB", "col_typ_id", null);
            ra.setAttribute("VrpIsinLDB", "name", "");
            ra.setAttribute("VrpIsinLDB", "rating", "");
            ra.setAttribute("VrpIsinLDB", "market_dsc", "");
            ra.setAttribute("VrpIsinLDB", "price_date", null);
            ra.setAttribute("VrpIsinLDB", "maturity_date", null);
        } else {
            ra.createLDB("VrpIsinLDB");
        }

        ra.setAttribute("VrpIsinLDB", "col_typ_id", new BigDecimal("660835251"));           
        LookUpRequest lookUpRequest = new LookUpRequest("VrpRatingLookUp");
        lookUpRequest.addMapping("VrpIsinLDB", "col_typ_id", "rat_typ_id");
        lookUpRequest.addMapping("VrpIsinLDB", "name", "rat_typ_desc");
        lookUpRequest.addMapping("VrpIsinLDB", "col_cat_id", "rat_sco_id");
        lookUpRequest.addMapping("VrpIsinLDB", "rating", "score");
        lookUpRequest.addMapping("VrpIsinLDB", "market_dsc", "score_desc");
        lookUpRequest.addMapping("VrpIsinLDB", "price_date", "date_from");
        lookUpRequest.addMapping("VrpIsinLDB", "maturity_date", "date_until");
    
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012"); 
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
              
        ra.setAttribute(ldbName,"rat_typ_id_long",ra.getAttribute("VrpIsinLDB", "col_typ_id"));
        ra.setAttribute(ldbName,"rat_sco_id_long",ra.getAttribute("VrpIsinLDB", "col_cat_id"));
        ra.setAttribute(ldbName,"Vrp_txtRatingLong",ra.getAttribute("VrpIsinLDB", "rating"));
            
        return true;
    }
    
    public boolean Vrp_txtRatingShort_FV(String elementName, Object elementValue, Integer lookUpType){
        String ldbName = "CollSecPaperDialogLDB";

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Vrp_txtRatingShort","");
            ra.setAttribute(ldbName,"rat_sco_id_short",null);
            return true;
        }
        
        if (ra.isLDBExists("VrpIsinLDB")) {
            ra.setAttribute("VrpIsinLDB", "col_cat_id", null);
            ra.setAttribute("VrpIsinLDB", "col_typ_id", null);
            ra.setAttribute("VrpIsinLDB", "name", "");
            ra.setAttribute("VrpIsinLDB", "rating", "");
            ra.setAttribute("VrpIsinLDB", "market_dsc", "");
            ra.setAttribute("VrpIsinLDB", "price_date", null);
            ra.setAttribute("VrpIsinLDB", "maturity_date", null);
        } else {
            ra.createLDB("VrpIsinLDB");
        } 
  
        ra.setAttribute("VrpIsinLDB", "col_typ_id", new BigDecimal("660836251"));           
        LookUpRequest lookUpRequest = new LookUpRequest("VrpRatingLookUp");
        lookUpRequest.addMapping("VrpIsinLDB", "col_typ_id", "rat_typ_id");
        lookUpRequest.addMapping("VrpIsinLDB", "name", "rat_typ_desc");
        lookUpRequest.addMapping("VrpIsinLDB", "col_cat_id", "rat_sco_id");
        lookUpRequest.addMapping("VrpIsinLDB", "rating", "score");
        lookUpRequest.addMapping("VrpIsinLDB", "market_dsc", "score_desc");
        lookUpRequest.addMapping("VrpIsinLDB", "price_date", "date_from");
        lookUpRequest.addMapping("VrpIsinLDB", "maturity_date", "date_until");
    
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012"); 
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
              
        ra.setAttribute(ldbName,"rat_typ_id_short",ra.getAttribute("VrpIsinLDB", "col_typ_id"));
        ra.setAttribute(ldbName,"rat_sco_id_short",ra.getAttribute("VrpIsinLDB", "col_cat_id"));
        ra.setAttribute(ldbName,"Vrp_txtRatingShort",ra.getAttribute("VrpIsinLDB", "rating"));
            
        return true;
    }
     
    public boolean Vrp_txtRefMarketInx_FV(String elementName, Object elementValue, Integer lookUpType){
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtRefMarketInx", null);
            return true;
        } 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Vrp_txtRefMarketInx", "Vrijednosti");

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
      
    
    public boolean Vrp_txtStopSell_FV(String elementName, Object elementValue, Integer lookUpType) {

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtStopSell", null);
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Vrp_txtStopSell", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012"); 
            return false;  
        } catch (NothingSelected ns) {
            return false;

        }
          
// ako je upisano D - obavezno treba upisati period zabrane trgovanja u broju dana
        
        String stop_sell_ind = (String) ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtStopSell");
        
        coll_util.setVrpStopSellCtx(stop_sell_ind);
          
        return true;
    }    
        
    public boolean Vrp_txtValutnaKlauzula_FV(String ElName, Object ElValue, Integer lookUpType) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields("CollSecPaperDialogLDB", new String[]{"Vrp_txtValutnaKlauzula", "Vrp_txtValutnaKlauzulaDsc"});
            return true;        
        }

        coll_util.clearField("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzulaDsc");
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzula", "Vrp_txtValutnaKlauzulaDsc", "currency_clause");
        if(value==null) return false;
        
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzula", value.sysCodeValue);
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzulaDsc", value.sysCodeDesc);  

        //kontrola, ako je valutna klauzula = D ne moze biti upisana valuta kn        
        String valutna_klauzula = (String) ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzula");  
        BigDecimal cur_id = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB", "NOMINAL_CUR_ID");        
        
        if (coll_util.chkValutnaKlauzula(valutna_klauzula, cur_id)) {
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtValutnaKlauzula", null);
            ra.setAttribute("CollSecPaperDialogLDB","Vrp_txtValutnaKlauzulaDsc","");    
            if (cur_id.equals(new BigDecimal("63999"))){ 
                ra.showMessage("wrnclt142");
            }else{ 
                ra.showMessage("wrnclt148");
            }
        }
        return true;
    }    
    
    public boolean Coll_txtNominalAmountTot_FV(String ElName, Object ElValue, Integer LookUp) {                        
        // ovo se zapisuje u coll_head, to je trenutni market value kolaterala
        ra.setAttribute("CollHeadLDB", "Coll_txtNomiValu", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNominalAmountTot"));
        //neponderirana         
        ra.setAttribute("CollHeadLDB", "Coll_txtNepoValue",ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNominalAmountTot"));
        return true;
    }
        
    public boolean Kol_txtFirstCall_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Kol_txtFirstCall", null);
            return true;
        } 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       

        request.addMapping("CollSecPaperDialogLDB", "Kol_txtFirstCall", "Vrijednosti");

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
    
    
    public boolean Coll_txtPosAnalystOpinion_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtPosAnalystOpinion", null);
            return true;
        }  
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp"); 
        request.addMapping("CollSecPaperDialogLDB", "Coll_txtPosAnalystOpinion", "Vrijednosti");
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
    
    public void deact_kolateral_owner (){
      try{
          ra.executeTransaction();
      }catch(VestigoTMException vtme){
          ra.showMessage(vtme.getMessageID());
      }
      
      if (((Integer) ra.getAttribute("GDB", "TransactionStatus")).equals(new Integer("0"))) {
          ra.showMessage("infKolDeact01");
        if (ra.getScreenID().equals("CollMovableOF") || ra.getScreenID().equals("CollCashDepOF") ||
                ra.getScreenID().equals("CollSecPaperOF") || ra.getScreenID().equals("CollGuarantOF") ||
                ra.getScreenID().equals("CollInsPolOF") || ra.getScreenID().equals("VesselDialogOF") ||
                ra.getScreenID().equals("SupplyOF") || ra.getScreenID().equals("CollLoanStockOF") ||
                ra.getScreenID().equals("VehiDialog2") || ra.getScreenID().equals("VehiDialogOF") ||
                ra.getScreenID().equals("CollCesijaOF")) {
                ra.exitScreen();
                ra.exitScreen();
                ra.invokeAction("refresh");
            } else {
                ra.exitScreen();
                ra.invokeAction("refresh");         
            }
      }      
    }
    
    private void setCollSecRatingFields(){
        String vp_SP=ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingVP_SP");
        String vp_MD=ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingVP_MD");
        String rat_LSP=ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingLong");
        String rat_SSP=ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingShort");
        String rat_LMD=ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingLong_MD");
        String rat_SMD=ra.getAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingShort_MD");
        
        ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtShortTermRating","N");
        if(coll_util.isEmpty(vp_SP) && coll_util.isEmpty(vp_MD)){
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingIndicator","N");
        }else{
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtRatingIndicator","D");
        }     
        if(!coll_util.isEmpty(vp_SP)){
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtAgencyRating","S&P");
        }else if (!coll_util.isEmpty(vp_MD)){
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtAgencyRating","Moody's");
        }else{
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtAgencyRating","");
        }
        
        if(!coll_util.isEmpty(rat_LSP) || !coll_util.isEmpty(rat_SSP)){
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtAgencyIssuerRating","S&P");
        }else if (!coll_util.isEmpty(rat_LMD) || !coll_util.isEmpty(rat_SMD)){
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtAgencyIssuerRating","Moody's");
        }else{
            ra.setAttribute("CollSecPaperDialogLDB", "Vrp_txtAgencyIssuerRating","");
        }
    }
    
    

}  
 
 