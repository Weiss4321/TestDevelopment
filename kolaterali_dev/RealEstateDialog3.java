package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;
                 
/** 
 * @author HRASIA
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RealEstateDialog3 extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RealEstateDialog3.java,v 1.64 2017/12/21 14:04:09 hrazst Exp $";
    private Date todaySQLDate = null;

    CollateralUtil coll_util= null;
    LookUps coll_lookups = null;
    private String MASTER_LDBNAME = "RealEstateDialogLDB";
    

    public RealEstateDialog3(ResourceAccessor ra) {
        super(ra);
        coll_util = new CollateralUtil(ra);
        coll_lookups = new LookUps(ra);
    }
    
    public void RealEstateDialog3_SE() {
        postaviDatum();        
        //Milka, 22.11.2006 - napuniti polje povrsina na ekranu kol. off.
         ra.setAttribute(MASTER_LDBNAME,"RealEstate_dlblSqrm2",ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2"));           
          
        if((ra.getScreenContext().compareTo("scr_change")== 0)  || (ra.getScreenContext().compareTo("scr_update")== 0) || 
                (ra.getScreenContext().compareTo("scr_update_ref")== 0)) {
            // validacija RBA prihvatljivosti           
            if (ra.getAttribute(MASTER_LDBNAME, "ColRba_txtEligibility1") != null) {
                ra.setAttribute(MASTER_LDBNAME, "ColRba_txtEligDesc1", "");   
                ra.invokeValidation("ColRba_txtEligibility1");                  
            }           
            ra.setContext("RealEstate_txtAmortAge","fld_protected");
            ra.setContext("RealEstate_txtAmortPerCal","fld_protected");
        }
        BigDecimal trzisna = ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNomiValu");
        BigDecimal wca = ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtUsedWCAAmount");
        if(trzisna!=null && wca!=null){
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPonAvailValue",trzisna.subtract(wca));
        }else{
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPonAvailValue",trzisna);
        }
        
        //skrivanje polja po RTC-u 35417(trebalo ih je brisati, ali za svaki slucaj ostavljamo ih na ekranu ali ih skrivamo kroz kod) 
        coll_util.hideFields(new String[]{
                "RealEstate_lblSumLimitVal","RealEstate_txtSumLimitVal",
                "RealEstate_lblNepoPerCal","RealEstate_txtNepoPerCal",
                "RealEstate_lblSumLimitDat","RealEstate_txtSumLimitDat"
                });  
    }
    
    public boolean isOnlyDigit(String argString){
        boolean onlyDigit = true;
        int lenString = argString.length();
        for(int i=0;i<lenString;i++){
            if(!(Character.isDigit(argString.charAt(i)))){
                onlyDigit = false;
                break;
            }
        }
        return onlyDigit;
    }
    
    public void CalculateAmortValCal(){

        BigDecimal nomiValu = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNomiValu");
        String vijekAmortizacije = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtAmortAge");
        String periodAmortizacije = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtAmortPerCal");
        BigDecimal iznosAmortizacije = null;
        BigDecimal periodAmortizacijeBD = null;
        BigDecimal numberOne = new BigDecimal("1.00000000000000000000000000000000");
        int brojGodina = 0;
        
        if (vijekAmortizacije != null){
          if(vijekAmortizacije.trim().length()== 0){
                    vijekAmortizacije = null;
          } 
        }
        if (periodAmortizacije != null){
              if(periodAmortizacije.trim().length()== 0){
                    periodAmortizacije = null;
              } 
        }
        
        if (vijekAmortizacije != null ){
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
                    iznosAmortizacije = iznosAmortizacije.divide(numberOne,2,BigDecimal.ROUND_HALF_UP);
                    ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortValCal", iznosAmortizacije);
                    
                }
                
            }
        }
    
    }//CalculateAmortValCal
    
 
      

    public boolean RealEstate3_txtDatnFrom_FV(){ 
        // PROCJENA VRIJEDI OD-DO: - PODACI COLLATERAL OFFICERA
        // RealEstate_txtDatnFrom               
        
        Date pocetak = null;
        Date kraj = null;
        Date datumProcjene = null;
        if((ra.getScreenContext().compareTo("scr_change")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0)){ 
            pocetak = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDatnFrom");
            kraj = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDatnUnti");
            datumProcjene = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnDate");
            if((pocetak != null) && (kraj != null)){
                if(kraj.before(pocetak)){
                    ra.showMessage("wrnclt31");
                    return false;
                }
            }
            if((datumProcjene != null) && (pocetak != null)){
                if(datumProcjene.after(pocetak)){
                    ra.showMessage("wrnclt30a");
                    return false;
                }//datum procjene ne smije biti veci od pocetka vazenja procjene
            }
        }
        pocetak = null;
        kraj = null;
        return true;
        //GOT
    }//RealEstate3_txtDatnFrom_FV
    public boolean RealEstate3_txtDatnUnti_FV(){ 
        
        // PROCJENA VRIJEDI OD-DO: - PODACI COLLATERAL OFFICERA
        // RealEstate_txtDatnUnti   
        Date pocetak = null;
        Date kraj = null;
        Date datumProcjene = null;
        if((ra.getScreenContext().compareTo("scr_change")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0)){ 
            pocetak = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDatnFrom");
            kraj = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDatnUnti");
            datumProcjene = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnDate");
            if((pocetak != null) && (kraj != null)){
                if(kraj.before(pocetak)){
                    ra.showMessage("wrnclt31");
                    return false;
                }
            }
            if((datumProcjene != null) && (kraj != null)){
                if(datumProcjene.after(kraj)){
                                ra.showMessage("wrnclt30");
                                return false;
                }//datum procjene ne smije biti veci od kraja vazenja procjene
            }
        }
        pocetak = null;
        kraj = null;
        datumProcjene = null;
        return true;
        //GOT
    }//RealEstate3_txtDatnUnti_FV
   
    public boolean RealEstate3_txtRbEstNomVal(){
        // NOM. VRIJED. GRAÐEVINE C.O.: - PODACI COLLATERAL OFFICERA
        // RealEstate_txtRbEstNomVal
        // Nominalna vrijednost gradevine koju je unio collateral officer
        return true;
    }
    public boolean RealEstate3_txtRbEstNomDat(){
        // DATUM NOM.VRIJE. GR. C.O.: - PODACI COLLATERAL OFFICERA
        // RealEstate_txtRbEstNomDat
        // Datum unosa/promjene nominalne vrijednosti gradevine koju je unio collateral officer
        return true;
    }//RealEstate3_txtRbEstNomDat
    
  
    public boolean RealEstate3_txtBuildingVal_FV(){
        //RealEstate_txtBuildingVal
        
        return true;
    }//RealEstate3_txtBuildingVal_FV
    
    public boolean RealEstate3_txtBuildValDate_FV(){
        //RealEstate_txtBuildValDate
        
        return true;
    }//RealEstate3_txtBuildValDate_FV
    
    public boolean RealEstate3_Byear_FV(){
        //      GOD.IZGRAD. : - IZGRADNJA, OBNOVA, POVRSINA 
        //      RealEstate_txtByear 
        //      Godina izgradnje nekretnine ili godina prvog uredenja zemljista ili sume
        java.lang.Integer goGr = null;
        int godinaGradnje = 0;
        
        
        goGr = (Integer) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtByear");
        if(goGr != null){
                godinaGradnje = goGr.intValue();
                if( (godinaGradnje < 1500) || (godinaGradnje > 2040)){
                    ra.showMessage("wrn1005");
                    return false;
                }
        }else{
            return false;
        }
        
        
        return true;
    }//RealEstate3_Byear_FV
    
    public boolean RealEstate3_Ryear_FV(){
        //      GOD.OBNOVE: - IZGRADNJA, OBNOVA, POVRSINA 
        //      RealEstate_txtRyear 
        //      Godina zadnje obnove nekretnine ili uredenja i obnove zemljista, sume i sl.
        java.lang.Integer goGr = null;
        int godinaGradnje = 0;
        
        
        goGr = (Integer) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtByear");
        if(goGr != null){
                godinaGradnje = goGr.intValue();
                if( (godinaGradnje < 1500) || (godinaGradnje > 2040)){
                    ra.showMessage("wrn1005");
                    return false;
                }
        }else{
            return true;
        }
        
        
        java.lang.Integer goOb = null;
        int godinaObnove = 0;
        
        
        goOb = (Integer) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRyear");
        if(goOb != null){
            godinaObnove = goOb.intValue();
                if( (godinaObnove < 1500) || (godinaObnove > 2040)){
                    ra.showMessage("wrn1005");
                    return false;
                }else{
                    if(godinaObnove < godinaGradnje){
                        ra.showMessage("wrn1006");
                        return false;
                    }
                }
        }else{
            return true;
        }
        return true;
    }//RealEstate3_Ryear_FV
    
    public boolean RealEstate3_Sqrm2_FV(){
        //POVRSINA M2: - IZGRADNJA, OBNOVA, POVRSINA 
        //RealEstate_txtSqrm2
// povrsinu upisujem na 3. ekran
         ra.setAttribute(MASTER_LDBNAME,"RealEstate_dlblSqrm2",ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2"));       
        
        BigDecimal cijenaM2 = null;
        BigDecimal nominalnaCijenaCO = null;  //Nominalna cijena collateral officer-a
        BigDecimal povrsina = null;

        
        
        if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNomiValu") != null){
            nominalnaCijenaCO = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNomiValu");
            if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2") != null){
                povrsina = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2");
// Milka, 27.09.2006                
                if (!(coll_util.isAmount(povrsina)))
                    return true;
                cijenaM2 = nominalnaCijenaCO.divide(povrsina,2,BigDecimal.ROUND_HALF_UP);
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPricem2",cijenaM2);

            }
        }
         
        cijenaM2 = null;
        nominalnaCijenaCO = null;
        povrsina = null;
        return true;
     
     }//RealEstate3_Sqrm2_FV
    
    public boolean RealEstate3_txtAmortAge_FV(){
        //  Amortizacijski vijek: - REVALORIZACIJA I AMORTIZACIJA
        //  RealEstate_lblAmortAge
        //  RealEstate_txtAmortAge
        //  Amortizacijski vijek izrazen u GG Za zemljista, sumu i sl. je 00.
        
        BigDecimal collTypeIdLandF = new BigDecimal("7777.0");
        BigDecimal collTypeIdLand = null;
        if(ra.getScreenContext().compareTo("scr_change")== 0){
                String vijekAmortizacije = null;
                //int brojMjeseci = 0;
                //int brojGodina = 0;
        
                vijekAmortizacije = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtAmortAge");
                
                if( ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID")== null) {
                    //Prije upisa amortizacijskog vijeka mora se odabrati vrsta kolaterala
                    ra.showMessage("wrnclt44a");
                    return false;
                }else{
                    collTypeIdLand = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID");
                    if(collTypeIdLand.compareTo(collTypeIdLandF)== 0){
                        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortAge","0");
                        return true;
                    }
                }
            if(vijekAmortizacije == null){
                    return false;
            }else{
                vijekAmortizacije.trim();
                if(vijekAmortizacije.length() < 1){
                    ra.showMessage("wrnclt1a");
                    return false;
                }
                if(vijekAmortizacije.length() > 2){
                    ra.showMessage("wrnclt1a");
                    return false;
                }
                if(!isOnlyDigit(vijekAmortizacije)){
                    ra.showMessage("wrnclt1a");
                    return false;
                }

            }
            vijekAmortizacije = null;
        }
        return true;
    }//RealEstate3_txtAmortAge_FV
    
    public boolean RealEstate3_txtAmortPerCal_FV(String elementName, Object elementValue, Integer lookUpType) {
        //  Period izraèuna amortizacije: - REVALORIZACIJA I AMORTIZACIJA
        //  RealEstate_lblAmortPerCal
        //  RealEstate_txtAmortPerCal
        //  Amortizacijski vijek izrazen u GG Za zemljista, sumu i sl. je 00.
                
        BigDecimal nomiValu = null;
        String periodAmortizacije = null;
        String vijekAmortizacije = null;
        BigDecimal iznosAmortizacije = null;
        BigDecimal periodAmortizacijeBD = null;
        BigDecimal numberOne = new BigDecimal("1.00000000000000000000000000000000");
        int brojGodina = 0;
        
        nomiValu = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNomiValu");
        vijekAmortizacije = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtAmortAge");
        periodAmortizacije = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtAmortPerCal");
        if (elementValue == null || ((String) elementValue).equals("")) {
             ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortPerCal", "");
             return true;
         }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtAmortPerCal", null, "clt_amort_per_cal");
        if(value!=null){        
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortPerCal", value.sysCodeValue);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_AMORT_PER_CAL_ID", value.sysCodeValueId);  
        }
         if(vijekAmortizacije == null){
            return false;
         }else{
            vijekAmortizacije.trim();
            if(vijekAmortizacije.length() > 2){
                ra.showMessage("wrnclt1b");
                 return false;
            }
            if(vijekAmortizacije.length() < 1){
                ra.showMessage("wrnclt1b");
                 return false;
            }
            if(!isOnlyDigit(vijekAmortizacije)){
                ra.showMessage("wrnclt1b");
                 return false;
            }
            if(vijekAmortizacije.compareTo("0") == 0){
                iznosAmortizacije = new BigDecimal("0.00");
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortValCal", iznosAmortizacije);
                return true;
            }else{
                brojGodina = java.lang.Integer.parseInt(vijekAmortizacije);
                periodAmortizacijeBD = new BigDecimal(brojGodina);
                if(nomiValu == null){
                    iznosAmortizacije = new BigDecimal("0.00");
                    ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortValCal", iznosAmortizacije);
                    return true;
                }else{
                    iznosAmortizacije = nomiValu.divide(periodAmortizacijeBD,64,BigDecimal.ROUND_HALF_UP);
                    iznosAmortizacije = iznosAmortizacije.divide(numberOne,2,BigDecimal.ROUND_HALF_UP);
                    ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAmortValCal", iznosAmortizacije);
                    return true;
                }
            }
        }
     }//RealEstate3_txtAmortPerCal_FV
    

    public boolean RealEstate3_txtNomiValu_FV(){
        // NOMINALNA VRIJEDNOST C.O.: - PODACI COLLATERAL OFFICERA
        // RealEstate_txtNomiValu   
        postaviDatum();
        if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID") == null) {
                //Nije moguce upisati vrijednost ako nije odabrana vrsta kolaterala 
                ra.showMessage("wrnclt48");
                return false;
        }

        BigDecimal nominalCONV = (BigDecimal) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtNomiValu");
       
        if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2")!= null){
            BigDecimal povrsina = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2");
            if((povrsina!= null) && (nominalCONV != null) ){
                if ((coll_util.isAmount(povrsina)))  {

                    BigDecimal cijenaM2 = nominalCONV.divide(povrsina,2,BigDecimal.ROUND_HALF_UP);
                    ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPricem2",cijenaM2);
                    cijenaM2 = null; 
                 }
            }
                povrsina = null;
        }
        
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoValue",nominalCONV);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoDate",todaySQLDate);
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighBValue",ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtWeighValue"));
        ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighBDate",ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtWeighDate"));

        coll_util.getPonderAndRestAmount(ra);

        return true;
    } //RealEstate3_txtNomiValu_FV


    public boolean RealEstate3_txtNomiDate_FV(){
        if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID") == null) {
                //Nije moguce upisati vrijednost ako nije odabrana vrsta kolaterala 
                ra.showMessage("wrnclt48");
                return false;
        }
        // ne moze se unijeti datum u buducnosti, veci od current date
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNomiDate");
        
        if ((current_date).before(date_from)) {
            ra.showMessage("wrnclt121");
            return false;
        }
        
        return true;
    }//RealEstate3_txtNomiDate_FV

    public boolean RealEstateDialog3_txtEligibility_FV(String elementName, Object elementValue, Integer lookUpType) {
       
        if((ra.getScreenContext().compareTo("scr_change")== 0) || (ra.getScreenContext().compareTo("scr_update")== 0) ||
                    (ra.getScreenContext().compareTo("scr_update_aut")== 0)){
            if (elementValue == null || ((String) elementValue).equals("")) {
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligibility", "");
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligDesc", "");
                ra.setAttribute(MASTER_LDBNAME, "dummyBD", null);
                return true;
            }
      
            if(ra.getCursorPosition().equals("RealEstate_txtEligibility")){
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligDesc", "");
                ra.setCursorPosition(2);
            } else if(ra.getCursorPosition().equals("RealEstate_txtEligDesc")){
                ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligibility", "");
            }
          
            LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
            value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtEligibility", "RealEstate_txtEligDesc", "source_code");
            if(value==null) return false;
            
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligibility", value.sysCodeValue);
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligDesc", value.sysCodeDesc);  
         }
         return true;
    }//RealEstateDialog3_txtEligibility_FV
 
    public boolean RealEstateDialog_UseIdLogin_FV(){
        return true;
     
     }//RealEstateDialog_UseIdLogin_FV
      
    public boolean RealEstateDialog_RealEsDateFrom_FV(){
        return true;
     }//RealEstateDialog_RealEsDateFrom_FV
     
    public boolean RealEstateDialog_RealEsDateUnti_FV(){
        return true;
     }//RealEstateDialog_RealEsDateUnti_FV

    public boolean RealEstateDialog_Owner_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = MASTER_LDBNAME;
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRealEstateRegisterNo",null);
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRealEstateOwnerName",null);
            ra.setAttribute(MASTER_LDBNAME,"COLL_CUS_ID",null);
            return true;
        }
        
        
        if (ra.getCursorPosition().equals("RealEstate_txtRealEstateOwnerName")) {
            ra.setAttribute(ldbName, "RealEstate_txtRealEstateRegisterNo", "");
        } else if (ra.getCursorPosition().equals("RealEstate_txtRealEstateRegisterNo")) {
            ra.setAttribute(ldbName, "RealEstate_txtRealEstateOwnerName", "");
        }
        
        String d_name = "";
        if (ra.getAttribute(ldbName, "RealEstate_txtRealEstateOwnerName") != null){
            d_name = (String) ra.getAttribute(ldbName, "RealEstate_txtRealEstateOwnerName");
        }
        
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "RealEstate_txtRealEstateRegisterNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "RealEstate_txtRealEstateRegisterNo");
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
        
        
         if (ra.getCursorPosition().equals("RealEstate_txtRealEstateRegisterNo")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRealEstateRegisterNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRealEstateOwnerName"));

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
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_COLL_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRealEstateRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRealEstateOwnerName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
        if(ra.getCursorPosition().equals("RealEstate_txtRealEstateRegisterNo")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtRealEstateOwnerName")){
            ra.setCursorPosition(1);
        }   
        return true;

        
    }//RealEstateDialog_Owner_FV
    
    public boolean RealEstateDialog_Carrier_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = MASTER_LDBNAME;
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtCarrierRegisterNo",null);
            ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtCarrierName",null);
            ra.setAttribute(MASTER_LDBNAME,"CUS_ID",null);
            return true;
        }
        
        
        if (ra.getCursorPosition().equals("RealEstate_txtCarrierName")) {
            ra.setAttribute(ldbName, "RealEstate_txtCarrierRegisterNo", "");
        } else if (ra.getCursorPosition().equals("RealEstate_txtCarrierRegisterNo")) {
            ra.setAttribute(ldbName, "RealEstate_txtCarrierName", "");
        }
        
        String d_name = "";
        if (ra.getAttribute(ldbName, "RealEstate_txtCarrierName") != null){
            d_name = (String) ra.getAttribute(ldbName, "RealEstate_txtCarrierName");
        }
        
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "RealEstate_txtCarrierRegisterNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "RealEstate_txtCarrierRegisterNo");
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
        
        if (ra.getCursorPosition().equals("RealEstate_txtCarrierRegisterNo")) 
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

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtCarrierRegisterNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtCarrierName"));

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
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCarrierRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCarrierName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
        if(ra.getCursorPosition().equals("RealEstate_txtCarrierRegisterNo")){
            ra.setCursorPosition(2);
        }
        if(ra.getCursorPosition().equals("RealEstate_txtCarrierName")){
            ra.setCursorPosition(1);
        }   
        return true;

        
    }//RealEstateDialog_Carrier_FV
    
    
    public void postaviDatum(){
        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new Date(timeT);
    }
    
     public void setAmortization3(){
        
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
     
     }//setAmortization3
    

    public boolean Coll_txtRePurpose_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {                                          
            ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurpose", "");   
            ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurposeDsc", ""); 
            ra.setAttribute(MASTER_LDBNAME, "purpose_id", null); 
            return true;                                                                                 
        }

        if (ra.getCursorPosition().equals("Coll_txtRePurpose")) {
            ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurposeDsc", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("Coll_txtRePurposeDsc")) {
            ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurpose", "");
        } 
        
        if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
            ra.createLDB("UserCodeValueLookUpLDB");
        }  
        BigDecimal col_typ_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID");              
// razlikuju se namjene stambenih i poslovnih nekretnina
     // stambene nekretnine - col_typ_id = 8777

     // komercijalne nekretnine - sve ostalo             
// stambene - RealEstate_COL_TYPE_ID =  8777  
// zemljiste - RealEstate_COL_TYPE_ID = 7777            
// poslovne - RealEstate_COL_TYPE_ID = sve ostalo
        if (col_typ_id != null && col_typ_id.compareTo(new BigDecimal("8777")) == 0)            
            ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "restate_purpose");             
        else if (col_typ_id != null && col_typ_id.compareTo(new BigDecimal("7777")) == 0)
            ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "z_restate_purpose"); 
        else 
            ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "b_restate_purpose"); 
         
        ra.setAttribute(MASTER_LDBNAME, "Coll_txtRePurposeDsc", ""); 
     
        LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");

        lookUpRequest.addMapping(MASTER_LDBNAME, "purpose_id", "use_cod_val_id");                        
        lookUpRequest.addMapping(MASTER_LDBNAME, "Coll_txtRePurpose", "use_code_value");
        lookUpRequest.addMapping(MASTER_LDBNAME, "Coll_txtRePurposeDsc", "use_code_desc");

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
      
    public boolean Kol_txtBuildPerm_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "Kol_txtBuildPerm", null);
            return true;
        }
  
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       
 
        request.addMapping(MASTER_LDBNAME, "Kol_txtBuildPerm", "Vrijednosti");

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
          
    public boolean Kol_txtLegality_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "Kol_txtLegality", null);
            return true;
        }
  
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");       
 
        request.addMapping(MASTER_LDBNAME, "Kol_txtLegality", "Vrijednosti");

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
    
    public boolean RealEstate_txtCollOfficer_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"USE_ID_CO", "RealEstate_txtCollOfficer"});
            return true;
        }
        
        if(ra.isLDBExists("BPMUsersByGroupLDB")){
            coll_util.clearFields("BPMUsersByGroupLDB", new String[]{"use_id", "login", "name"});
        }else{
            ra.createLDB("BPMUsersByGroupLDB");
        }
        ra.setAttribute("BPMUsersByGroupLDB","bpmGroupCode", "colOfficer");
        ra.setAttribute("BPMUsersByGroupLDB","name", ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtCollOfficer"));
        
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
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtCollOfficer", ra.getAttribute("BPMUsersByGroupLDB", "name"));
        ra.setAttribute(MASTER_LDBNAME, "USE_ID_CO", ra.getAttribute("BPMUsersByGroupLDB", "use_id"));
        
        return true;
    } 
    
    public boolean RealEstate_txtTypeTV_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME,new String[]{"RealEstate_txtTypeTVCode", "RealEstate_txtTypeTV"});
            return true;        
        }   
        
        if(!ElName.equals("RealEstate_txtTypeTVCode")) coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtTypeTVCode");
        if(!ElName.equals("RealEstate_txtTypeTV")) coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtTypeTV");
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtTypeTVCode", "RealEstate_txtTypeTV", "coll_RealEstNomTyp");
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeTVCode", value.sysCodeValue);
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypeTV", value.sysCodeDesc);  
        return true;
    }
    
    public boolean RealEstate_txtEligibility1_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME,new String[]{"ColRba_txtEligibility1", "ColRba_txtEligDesc1"});
            return true;        
        }   
               
        if(ElName.equals("ColRba_txtEligDesc1")) coll_util.clearField(MASTER_LDBNAME, "ColRba_txtEligibility1"); 
        else coll_util.clearField(MASTER_LDBNAME, "ColRba_txtEligDesc1");      
        
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "ColRba_txtEligibility1", "ColRba_txtEligDesc1", "clt_rba_eligib");
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, "ColRba_txtEligibility1", value.sysCodeValue);
        ra.setAttribute(MASTER_LDBNAME, "ColRba_txtEligDesc1", value.sysCodeDesc);  

        String rba_eligibility = (String)ra.getAttribute(MASTER_LDBNAME, "ColRba_txtEligibility1");       
        String low_eligibility = (String)ra.getAttribute(MASTER_LDBNAME,"ColLow_txtEligibility");
        if (rba_eligibility.equalsIgnoreCase("D") && (low_eligibility.equalsIgnoreCase("N"))) {
            //  pitanje da li stvarno zeli promijeniti prihvatljivost na D  
            Integer retValue = (Integer)ra.showMessage("col_qer015");
            if (retValue!=null && retValue.intValue() == 0) return false;           
        }
        return true;      
    }
    
    
    
    
}
