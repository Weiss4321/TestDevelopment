package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.Vector;
 
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.LookUps;
import hr.vestigo.modules.collateral.util.LookUps.CurrencyLookUpReturnValues;
import hr.vestigo.modules.collateral.util.LookUps.SystemCodeLookUpReturnValues;
import hr.vestigo.modules.rba.util.DateUtils;
          
       
/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RealEstateDialog2 extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RealEstateDialog2.java,v 1.111 2017/11/29 09:46:23 hrazst Exp $";
	//1.26 staro
	private final BigDecimal numberZero = new BigDecimal("0.00");
	private Date todaySQLDate = null;
	
	private Date vvDate9999 = Date.valueOf("9999-12-31");

	CollateralUtil coll_util= null;	
    LookUps coll_lookups = null;
    private String MASTER_LDBNAME = "RealEstateDialogLDB";
    
    //flag koji nam govori da li smo u SE funkciji
    private boolean SEFlag=true;
    private static String[] listEstnMark = new String[]{"3","4","5","6"};
    private String old_EstnMark_value=null;
    
	public RealEstateDialog2(ResourceAccessor ra) {
		super(ra);
		coll_util = new CollateralUtil(ra);
		coll_lookups = new LookUps(ra);
	} 
	
	public void RealEstateDialog2_SE() {
	    SEFlag=true;
		postaviDatum();
		if(ra.getScreenContext().compareTo("scr_update")== 0){			
			boolean mozePromjenaValute = true;
			
			BigDecimal thirdRight = numberZero;
			BigDecimal thirdRightNom = numberZero;
			BigDecimal hfsValue = numberZero;
			BigDecimal sumLimitVal = numberZero;
			BigDecimal sumPartVal = numberZero;
			
			thirdRight = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRight");
			thirdRightNom = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtThirdRightInNom");
			hfsValue = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtHfsValue");
			sumLimitVal = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtSumLimitVal");
			sumPartVal = (BigDecimal)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtSumPartVal");
			
			if(thirdRight == null ){
				mozePromjenaValute = true;
			}else{
				if(thirdRight.compareTo(numberZero)== 0){
					//mozePromjenaValute = true;
				}else{
					mozePromjenaValute = false;
				}
			}
			if(thirdRightNom == null ){
				//mozePromjenaValute = true;
			}else{
				if(thirdRightNom.compareTo(numberZero)== 0){
					//mozePromjenaValute = true;
				}else{
					mozePromjenaValute = false;
				}
			}
			
			if(hfsValue == null ){
				//mozePromjenaValute = true;
			}else{
				if(hfsValue.compareTo(numberZero)== 0){
					//mozePromjenaValute = true;
				}else{
					mozePromjenaValute = false;
				}
			}
			
			if(sumLimitVal == null ){
				//mozePromjenaValute = true;
			}else{
				if(sumLimitVal.compareTo(numberZero)== 0){
					//mozePromjenaValute = true;
				}else{
					mozePromjenaValute = false;
				}
			}
			
			if(sumPartVal == null ){
				//mozePromjenaValute = true;
			}else{
				if(sumPartVal.compareTo(numberZero)== 0){
					//mozePromjenaValute = true;
				}else{
					mozePromjenaValute = false;
				}
			}
			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtThirdRightCurCodeChar", (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar"));  
			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRBACurCodeChar", (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar"));		
		}
	  	
		if((ra.getScreenContext().compareTo("scr_detail")== 0) || ra.getScreenContext().equalsIgnoreCase("scr_detail_deact") ||
		        (ra.getScreenContext().compareTo("scr_detail_co")== 0)){
			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtThirdRightCurCodeChar", (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar"));  
			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRBACurCodeChar", (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar")); 
		}
        
        if (ra.getScreenContext().equalsIgnoreCase("scr_update")) {            
            ra.invokeValidation("RealEstate_txtComDoc");
        }
        
        if(((String)ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtBuildProjectFinanc")).equals("N")){
            coll_util.clearField(MASTER_LDBNAME,"RealEstate_txtFutureValueAmount");
            coll_util.enableField("RealEstate_txtFutureValueAmount",2);            
        }else{
            coll_util.enableField("RealEstate_txtFutureValueAmount",0);
        }
        
        if(ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_update")){  
            coll_util.enableFields(new String[] {"RealEstate_txtB2HNB", "RealEstate_txtB2IRB"}, 0);
            coll_util.setRequired(new String[] {"Coll_txtB2HNB", "Coll_txtB2IRB"}, true);            
            
            String hnb=ra.getAttribute("RealEstateDialogLDB_B", "Reb_RealEstate_txtB2HNB");
            String irb=ra.getAttribute("RealEstateDialogLDB_B", "Reb_RealEstate_txtB2IRB");
            if(hnb==null ||hnb.equals("")) ra.setRequired("RealEstate_txtB2HNB", false);
            if(irb==null ||irb.equals("")) ra.setRequired("RealEstate_txtB2IRB", false);
        }
        
        ra.invokeValidation("RealEstate_txtAssessmentMethod1Code");
        ra.invokeValidation("RealEstate_txtAssessmentMethod2Code");
        ra.invokeValidation("RealEstate_txtEstnMark");
        //this.RealEstate_txtRegion_FV("RealEstate_txtRegionCode", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRegionCode"), 0);
        ra.setCursorPosition("RealEstate_txtEUseIdLogin");
        SEFlag=false;
	} 
	
	public boolean RealEstateDialog2_EX() {		
		String comDoc = null;
	 	Date dateRecDoc = null;
	 	String missingDoc = null;
	 	Date dateToDoc = null;
	 	 
	 	if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc") != null){
	 		comDoc = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc");
	 		if(comDoc.compareTo("D")== 0){
	 			dateRecDoc = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateRecDoc");
	 			if(dateRecDoc == null){
	 				ra.setCursorPosition("RealEstate_txtDateRecDoc");
	 				dateRecDoc = null;
	 				return false;
	 			}
		 	}
	 		if(comDoc.compareTo("N")== 0){
	 			dateToDoc = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateToDoc");
	 			missingDoc = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtMissingDoc");
	 			if(missingDoc == null){
	 				ra.setCursorPosition("RealEstate_txtMissingDoc");
	 				return false;
	 			}
	 			if(dateToDoc == null){
	 				ra.setCursorPosition("RealEstate_txtDateToDoc");
	 				return false;
	 			}
	 		}
	 	}else{
	 		ra.setCursorPosition("RealEstate_txtComDoc");
	 		return false;
	 	}
	 	
	 	comDoc = null;
	 	dateRecDoc = null;
	 	missingDoc = null;
	 	dateToDoc = null;
		return true;
		
	}
	
	public boolean RealEstate2_Estimator_FV(String elementName, Object elementValue, Integer lookUpType) {
	    String lookupLDBName="CollEstimatorLookUpLDB";
	    String name = "", est_comp_name="";
	    String register_no="", est_comp_register_no="";
	    
	    if (elementValue == null || ((String) elementValue).equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"RealEstate_txtEUseIdLogin", "RealEstate_txtEUseIdName", 
                    "RealEstate_txtTypelValuer", "RealEstate_txtEUseIdOIB", "RealEstate_REAL_EST_EUSE_ID", "RealEstate_REAL_EST_CUS_TYPE_ID",
                    "RealEstate_txtEUseIdLoginRC","RealEstate_txtEUseIdNameRC", "RealEstate_txtEUseIdRCOIB", "RealEstate_RCEstimateId","EST_TYPE_CODE",
                    "RealEstate_txtInternalValuer" });
            return true;
        }
        if (ra.getCursorPosition().equals("RealEstate_txtEUseIdLogin")) {
            coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtEUseIdName");
            est_comp_register_no = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdLogin");            
            ra.setCursorPosition(3);
        } else if(ra.getCursorPosition().equals("RealEstate_txtEUseIdName")){
            coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtEUseIdLogin");
            est_comp_name = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdName");    
            if (name.length() < 4) {
                ra.showMessage("wrn366");
                return false;
            }            
        }else{
            est_comp_register_no = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdLogin");
            est_comp_name = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdName"); 
            register_no = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdLoginRC");
            name = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdNameRC");  
        }
        //je li zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(est_comp_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }
        
        if (ra.isLDBExists(lookupLDBName)) {
            coll_util.clearFields(lookupLDBName, new String[]{ "estDate", "est_cus_id",
                    "est_register_no", "est_name", "est_tax_number", "est_comp_cus_id", "est_comp_register_no",
                    "est_comp_name", "est_comp_tax_number", "est_type","est_type_code","est_internal" });
        } else {                                                                                                                         
            ra.createLDB(lookupLDBName);                                                                                          
        }  
        //trebaju se dohvatiti oni procjenitelji koji su aktivni na dan procjene
        //ako je unesen dan procjene uzimam njega, inace uzimam trenutni dan
        Date datum=ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnDate");
        if(datum==null) datum=DateUtils.getCurrentDate();
        
        //samo ova polja su pretraziva pa njih postavljam - datum je obavezan jer se dohvacaju samo oni 
        //procjenitelji koji su bili aktivni na taj dan 
        ra.setAttribute(lookupLDBName, "estDate", datum);
        ra.setAttribute(lookupLDBName, "est_comp_register_no", est_comp_register_no);
        ra.setAttribute(lookupLDBName, "est_comp_name", est_comp_name); 
        ra.setAttribute(lookupLDBName, "est_register_no", register_no);
        ra.setAttribute(lookupLDBName, "est_name", name); 
        
        LookUpRequest lookUpRequest = new LookUpRequest("CollEstimatorLookUp"); 
        lookUpRequest.addMapping(lookupLDBName, "est_cus_id", "est_cus_id");                                                            
        lookUpRequest.addMapping(lookupLDBName, "est_register_no", "est_register_no");                                                  
        lookUpRequest.addMapping(lookupLDBName, "est_name", "est_name");                                                                
        lookUpRequest.addMapping(lookupLDBName, "est_type", "est_type"); 
        lookUpRequest.addMapping(lookupLDBName, "est_tax_number", "est_tax_number");                                                                
        lookUpRequest.addMapping(lookupLDBName, "est_comp_cus_id", "est_comp_cus_id");                                            
        lookUpRequest.addMapping(lookupLDBName, "est_comp_register_no", "est_comp_register_no");                                                    
        lookUpRequest.addMapping(lookupLDBName, "est_comp_name", "est_comp_name");                                            
        lookUpRequest.addMapping(lookupLDBName, "est_comp_tax_number", "est_comp_tax_number");
        lookUpRequest.addMapping(lookupLDBName, "est_type_code", "est_type_code");
        lookUpRequest.addMapping(lookupLDBName, "est_internal", "est_internal");

        try {                                                                                                                            
            ra.callLookUp(lookUpRequest);                                                                                                  
        } catch (EmptyLookUp elu) { 
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"RealEstate_txtEUseIdLogin", "RealEstate_txtEUseIdName",
                    "RealEstate_txtTypelValuer", "RealEstate_txtEUseIdOIB", "RealEstate_REAL_EST_EUSE_ID", "RealEstate_REAL_EST_CUS_TYPE_ID",
                    "RealEstate_txtEUseIdLoginRC","RealEstate_txtEUseIdNameRC", "RealEstate_txtEUseIdRCOIB", "RealEstate_RCEstimateId","EST_TYPE_CODE","RealEstate_txtInternalValuer" });
            ra.showMessage("infzstColl04");
            ra.setCursorPosition("RealEstate_txtEUseIdLogin");
            return false;                                                                                                                  
        } catch (NothingSelected ns) {                                                                                                   
            return false;                                                                                                                  
        }   
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_RCEstimateId", ra.getAttribute(lookupLDBName, "est_cus_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdLoginRC", ra.getAttribute(lookupLDBName, "est_register_no"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdNameRC", ra.getAttribute(lookupLDBName, "est_name"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTypelValuer", ra.getAttribute(lookupLDBName, "est_type"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdRCOIB", ra.getAttribute(lookupLDBName, "est_tax_number"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_EUSE_ID", ra.getAttribute(lookupLDBName, "est_comp_cus_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdLogin", ra.getAttribute(lookupLDBName, "est_comp_register_no"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdName", ra.getAttribute(lookupLDBName, "est_comp_name"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEUseIdOIB", ra.getAttribute(lookupLDBName, "est_comp_tax_number"));
        ra.setAttribute(MASTER_LDBNAME, "EST_TYPE_CODE", ra.getAttribute(lookupLDBName, "est_type_code"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtInternalValuer", ra.getAttribute(lookupLDBName, "est_internal"));

        return true;
    }
            
	public boolean RealEstate2_txtEstnValu_FV(){
	   //TRZISNA VRIJEDNOST  - PODACI PROCJENITELJA O VRIJEDNOSTI NEKRETNINE
       if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_TYPE_ID") == null) {
    		//Nije moguce upisati vrijednost ako nije odabrana vrsta kolaterala 
    		ra.showMessage("wrnclt48");
    		return false;
        } 	
		 		 	
        BigDecimal nominalCONV = (BigDecimal) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtNomiValu");
		BigDecimal trzisnaVrijednost = (BigDecimal) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtEstnValu");
		 		
		if(nominalCONV == null && trzisnaVrijednost != null){
		    // nije upisana vrijednost koloateral referenta		 			
		    ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNomiValu",trzisnaVrijednost);
			nominalCONV = (BigDecimal) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtNomiValu");
			
			if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2")!= null){
			    BigDecimal povrsina = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtSqrm2");
	 			if((povrsina!= null) && (nominalCONV != null) ){
	 			    if((coll_util.isAmount(povrsina)) && (nominalCONV != null) ){		 						
	 			        BigDecimal cijenaM2 = nominalCONV.divide(povrsina,2,BigDecimal.ROUND_HALF_UP);
		 				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtPricem2",cijenaM2);
		 				cijenaM2 = null;
		 			}
		 			povrsina = null;
	 			}
            }
			
			ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoValue",nominalCONV);
			ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtNepoDate",todaySQLDate);		 						
		 	ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighBValue",ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtWeighValue"));
		 	ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtWeighBDate",ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtWeighDate"));		 						
            coll_util.getPonderAndRestAmount(ra);
		}
        return true;		 		 
	}		 		 
	 		 
	public boolean RealEstate2_txtNmCurIdCodeChar_FV(String ElName, Object ElValue, Integer LookUp){
		//VALUTA - PODACI PROCJENITELJA O VRIJEDNOSTI NEKRETNINE
		if (ElValue == null || ((String) ElValue).equals("")) {                                          
			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar", "");                        
			ra.setAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_NM_CUR_ID", null); 
            ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTvCurIdCodeChar", "");  
			return true;                                                                                 
		}

		ra.setAttribute(MASTER_LDBNAME, "dummySt", "");
		
		LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");   				
		lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_REAL_EST_NM_CUR_ID", "cur_id");           
		lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "code_num");
		lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar", "code_char");
		lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}  
		 
		//POSTAVI VALUTU IZNOSA TUDIH HF NA VALUTU TRZISNE VRIJEDNOSTI
		ra.setAttribute(MASTER_LDBNAME, "RealEstate_THIRD_RIGHT_CUR_ID", (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_REAL_EST_NM_CUR_ID"));  
		ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtThirdRightCurCodeChar", (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar"));  
        		
		//POSTAVI VALUTU IZNOSA RBA HF NA VALUTU TRZISNE VRIJEDNOSTI
		ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRBACurCodeChar", (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar"));  
		// valuta na trecem ekranu, samo prikaz			
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtTvCurIdCodeChar", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNmCurIdCodeChar"));  				
		return true;  
	}//RealEstate_txtNmCurIdCodeChar_FV
	
	public boolean RealEstate2_txtNewBuildVal_FV(){
	//NOVA GRADEVINSKA VRIJEDNOST - PODACI PROCJENITELJA O VRIJEDNOSTI NEKRETNINE

	    BigDecimal newBuildVal = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtNewBuildVal");
	    BigDecimal buildingVal = (BigDecimal)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtBuildingVal");
	    Date buildValDate = (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtBuildValDate");
				 	
	 	if ( (newBuildVal != null) && (buildingVal == null) ){

	 		ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtBuildingVal", newBuildVal);
	 		if(buildValDate == null){
	 			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtBuildValDate", todaySQLDate);
	 		}
	 	}
	 	return true;
	}//RealEstate_txtNewBuildVal_FV
	
	public boolean RealEstate2_txtEstnMarkDesc_FV(){
	    return true;
	}//RealEstate2_txtEstnMarkDesc_FV
			 
	public boolean RealEstate2_txtEstnDate_FV(String elementName, Object elementValue, Integer lookUpType){						
	    //DATUM PROCJENE - PODACI PROCJENITELJA O VRIJEDNOSTI NEKRETNINE
	    // datum mora biti manji ili jednak current date
		Date doc_date = (Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnDate");
		Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
		if (doc_date == null || current_date == null) 
			return true;

		if ((current_date).before(doc_date)) {
			ra.showMessage("wrnclt117");
			return false;
		}
		ra.invokeValidation("RealEstate_txtEUseIdLogin");
		return true;			
	}	
	 	
	public boolean RealEstate2_txtComDoc_FV(String elementName, Object elementValue, Integer lookUpType) {
				
//PREDANA SVA DOKUMENTACIJA - PODACI O STANJU DOKUMENTACIJE NEKRETNINE
//RealEstate_txtComDoc
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc", null);
			
		}

		LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");		

		request.addMapping(MASTER_LDBNAME, "RealEstate_txtComDoc", "Vrijednosti");

		try {
			ra.callLookUp(request);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;  
		} catch (NothingSelected ns) {
			return false;

		}
					
		String rba_eligibility = null;				
		String sva_dokumentacija = (String) ra.getAttribute(MASTER_LDBNAME,"RealEstate_txtComDoc");
			  			
		if (sva_dokumentacija != null) {
			sva_dokumentacija = sva_dokumentacija.trim();
//	predana sva dokumentacija, obavezno upisati datum predaje dokumentacije					
//					zaprotektirati polje nedostajuca dokumentacija, datum do kada treba predati dokumentaciju					
			if (sva_dokumentacija.equalsIgnoreCase("D")) {
				ra.setContext("RealEstate_txtDateRecDoc","fld_plain");
				ra.setRequired("RealEstate_txtDateRecDoc",true);
				
				ra.setRequired("RealEstate_txtMissingDoc",false);
				ra.setRequired("RealEstate_txtDateToDoc",false);
				
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtMissingDoc","");
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtDateToDoc",null);
				
				ra.setContext("RealEstate_txtMissingDoc","fld_protected");
				ra.setContext("RealEstate_txtDateToDoc","fld_protected");
				
				ra.setCursorPosition("RealEstate_txtDateRecDoc");
				  
			} else if (sva_dokumentacija.equalsIgnoreCase("N")){
// 	nije predana sva dokumentacija, obavezno upisati nedostajucu dokumentaciju i datum do kada treba predati dokumentaciju
//					zaprotektirati polje datuma predaje dokumentacije
				
				ra.setContext("RealEstate_txtDateRecDoc","fld_protected");
				ra.setRequired("RealEstate_txtDateRecDoc",false);
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtDateRecDoc",null);						
				
				ra.setRequired("RealEstate_txtMissingDoc",true);
				ra.setRequired("RealEstate_txtDateToDoc",true);
				
				ra.setContext("RealEstate_txtMissingDoc","fld_plain");
				ra.setContext("RealEstate_txtDateToDoc","fld_plain");
				
				ra.setCursorPosition("RealEstate_txtMissingDoc");						
				
			}
		}				
		return true;
	}	//RealEstate_txtComDoc_FV
				 
				 
				 
	public boolean RealEstate2_txtMissingDoc_EF(){
	 	//NEDOSTAJUCA DOKUMENTACIJA - PODACI O STANJU DOKUMENTACIJE NEKRETNINE
	 	String comDoc = null;
	 	comDoc = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc");
	 	if(comDoc != null ){
	 		comDoc = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc");
	 		if(comDoc.trim().compareTo("D")== 0){
	 			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtMissingDoc","");
	 			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtDateToDoc",null);
		 		ra.setCursorPosition(2);
		 		return true;
		 	}else{
		 		if(comDoc.trim().compareTo("N")!= 0){
		 			ra.setCursorPosition(-1);
		 		}else{
		 			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtDateRecDoc",null);
		 		}
		 	}
	 	}else{
	 		ra.setCursorPosition(-1);
	 		return true;
	 	}
	 	comDoc = null;
	 	return true;
	 }//RealEstate_txtMissingDoc_EF
			
			
	 public boolean RealEstate2_txtMissingDoc_FV(){
	 	 //NEDOSTAJUCA DOKUMENTACIJA - PODACI O STANJU DOKUMENTACIJE NEKRETNINE

	 	String comDoc = null;
	 	String missingDoc = null;
	 	if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc") != null){
	 		comDoc = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc");
	 		if(comDoc.trim().compareTo("D") == 0){
	 			ra.setCursorPosition(2);
		 		return true;
		 	}else{
		 		if(comDoc.trim().compareTo("N")!= 0){
		 			ra.setCursorPosition(-1);
		 			return true;
		 		}
		 		if(comDoc.trim().compareTo("N")== 0){
		 			missingDoc = (String)  ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtMissingDoc");
		 			if(missingDoc == null){
		 				ra.setCursorPosition("RealEstate_txtMissingDoc");
		 				return true;
		 			}else{
		 				if(missingDoc.trim().length() == 0){
		 					ra.setCursorPosition("RealEstate_txtMissingDoc");
			 				return true;
		 				}
		 			}
		 		}
		 	}
	 	}else{
	 		ra.showMessage("wrnclt49");
	 		//Potrebno je upisati D ili N u polje predana sva dokumentacija	
	 		ra.setCursorPosition(-1);
	 		comDoc = null;
 		 	missingDoc = null;
	 		return false;
	 	}
	 	comDoc = null;
		missingDoc = null;
	 	return true;
 	
	 }//RealEstate_txtMissingDoc_FV
	 
	 public boolean RealEstate2_txtDateToDoc_FV(){
 	//RealEstate_txtComDoc	RealEstate_txtMissingDoc

	//RealEstate_txtDateToDoc		RealEstate_txtDateRecDoc
 	
//PREDATI DOKUMENTACIJU DO - PODACI O STANJU DOKUMENTACIJE NEKRETNINE
	//RealEstate_txtDateToDoc

// datum do kada treba predati dokumentaciju treba biti veci od danasnjeg 			 	

    	Date doc_date = (Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateToDoc");
    	Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
    	if (doc_date == null || current_date == null) 
    		return true;
    
    	if ((doc_date).before(current_date)) {
    		ra.showMessage("wrnclt30e");
    		return false;
    	}
    	return true;						 	
			 	
	 }//RealEstate_txtDateToDoc_FV
			 	 
	 public boolean RealEstate2_txtDateRecDoc_FV(){
//DATUM PREDAJE CJELOKUP. DOKU.: - PODACI O STANJU DOKUMENTACIJE NEKRETNINE
				//RealEstate_txtDateToDoc
//datum mora biti manji ili jednak current date 
	
		Date doc_date = (Date) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateRecDoc");
		Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
		if (doc_date == null || current_date == null) 
			return true;

		if ((current_date).before(doc_date)) {
			ra.showMessage("wrnclt112");
			return false;
		}
		return true;			 	
	 }//RealEstate_txtDateRecDoc_FV
			 
			 
			 
	  public boolean RealEstate2_txtRecLop_FV(String elementName, Object elementValue, Integer lookUpType) {
	
//UPISANO PRAVO BANKE - PODACI O STANJU UPISA PRAVA BANKE	

		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRecLop", null);
		}
		LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");		
		request.addMapping(MASTER_LDBNAME, "RealEstate_txtRecLop", "Vrijednosti");

		try {
			ra.callLookUp(request);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;  
		} catch (NothingSelected ns) {
			return false;
		}
		
		String recLop = null;
		recLop = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRecLop");
		String buildPermInd = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtBuildPermInd"); 
		String insPolInd = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtInspolInd"); 
		String stanjeDokum = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc");
		String stanjeUpisa = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRecLop");

		if((buildPermInd != null) && (insPolInd != null) && (stanjeDokum != null) && (stanjeUpisa != null)){
			
			buildPermInd = buildPermInd.trim();       
			insPolInd    = insPolInd.trim();          
			stanjeDokum  = stanjeDokum.trim();        
			stanjeUpisa  = stanjeUpisa.trim();        

			if((buildPermInd.compareTo("D")== 0) && (insPolInd.compareTo("D")== 0) && (stanjeDokum.compareTo("D")== 0) && (stanjeUpisa.compareTo("D")== 0)){
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligibility","D");
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligDesc","Kolateral je prihvatljiv");
				
			}else{
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligibility","N");
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligDesc","Kolateral je neprihvatljiv");
				
			}
		
		}
		
		if(recLop != null){
			if(recLop.trim().compareTo("D")== 0){
				 ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtDateToLop",null);
				 ra.setCursorPosition("RealEstate_txtDateRecLop");	
				 return true;
			}else{
				if(recLop.trim().compareTo("N")== 0){
					ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtDateRecLop",null);
					ra.setCursorPosition("RealEstate_txtDateToLop");
					if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateToLop") == null){
						ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtDateToLop",vvDate9999);
					}
					return true;
				}else{
					return false;
				}
			}
		}
		recLop = null;
//				Provjera prihvatljivosti kolaterala
		buildPermInd = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtBuildPermInd"); 
		insPolInd = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtInspolInd"); 
		stanjeDokum = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtComDoc");
		stanjeUpisa = (String) ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRecLop");

		if((buildPermInd != null) && (insPolInd != null) && (stanjeDokum != null) && (stanjeUpisa != null)){
			
			buildPermInd = buildPermInd.trim();       
			insPolInd    = insPolInd.trim();          
			stanjeDokum  = stanjeDokum.trim();        
			stanjeUpisa  = stanjeUpisa.trim();        

			if((buildPermInd.compareTo("D")== 0) && (insPolInd.compareTo("D")== 0) && (stanjeDokum.compareTo("D")== 0) && (stanjeUpisa.compareTo("D")== 0)){
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligibility","D");
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligDesc","Kolateral je prihvatljiv");
				
			}else{
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligibility","N");
				ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEligDesc","Kolateral je neprihvatljiv");
				
			}
		
		}
		return true;
	 	
	}	//RealEstate_txtRecLop_FV
	  
  	 public boolean RealEstate2_txtDateToLop_EF(){
  	 //UPISATI PRAVO BANKE DO - PODACI O STANJU UPISA PRAVA BANKE	
 	 	String recLop = null;
 	 	recLop = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRecLop"); 	
 	 	
 	 	if(recLop != null){
 	 		if(recLop.compareTo("D")== 0){
 		 		ra.setCursorPosition("RealEstate_txtDateRecLop");
 		 		return true;
 		 	}else{
	 			if(recLop.trim().compareTo("N")!= 0){
	 					ra.setCursorPosition("RealEstate_txtRecLop");
	 					return true;
	 			}
 		 	}
 	 	}else{
 	 		ra.showMessage("wrnclt50");
 	 		//Potrebno je upisati D ili N u polje Upisano pravo banke
 	 		ra.setCursorPosition("RealEstate_txtRecLop");
 	 		return false;
 	 	}
 	 	return true;
 	 	
 	 }//RealEstate_txtDateToLop_EF
  	 
 	 public boolean RealEstate2_txtDateToLop_FV(){
 	 	 //UPISATI PRAVO BANKE DO - PODACI O STANJU UPISA PRAVA BANKE	

 	 	String recLop = null;
 	 	recLop = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRecLop"); 	
 	    Date dateToLop = null;
 	 	Date dateRecLop = null;
 	 
 	 	
 	 	if(recLop != null){
 	 		
 	 		if(recLop.trim().compareTo("D")== 0){
     			
    	 		ra.setCursorPosition("RealEstate_txtDateRecLop");
    	 		return true;
 		 	}else{
	 			if(recLop.trim().compareTo("N")!= 0){
	 				ra.setCursorPosition("RealEstate_txtRecLop");
	 				recLop = null;
	 				dateToLop = null;
	 				return true;
	 			}
	 			
	 			
	 			
		 		if(recLop.trim().compareTo("N")== 0){
		 			dateToLop = (Date)  ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateToLop");
		 			if(dateToLop == null){
		 				ra.setCursorPosition("RealEstate_txtDateToLop");
		 				dateToLop = null;
		 				return true;
		 			}
		 		}
 		 	}
 	 		
 	 	}else{
 	 		ra.showMessage("wrnclt50");
 	 		//Potrebno je upisati D ili N u polje Upisano pravo banke	
 	 		ra.setCursorPosition("RealEstate_txtRecLop");
 	 		
 			dateToLop = null;
 	 		return true;
 	 	}
 	 	if(dateToLop != null){
 	 		if(dateToLop.before(todaySQLDate)){
 	 			//Datum do kada treba predati dokumentaciju mora biti veci od tekuceg
 	 			ra.showMessage("wrnclt30f");
 	 			return false;
 	 		}
 	 	}
 	 	
 		dateToLop = null;
 		dateRecLop = null;
 	 	return true;
 	 }//RealEstate_txtDateToLop_FV
 	 
 	 public boolean RealEstate2_txtDateRecLop_FV(){
	
	 //DATUM UPISA PRAVA BANKE - PODACI O STANJU UPISA PRAVA BANKE	
	 	String recLop = null;
	 	recLop = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRecLop");
	 	 	
	 	if(recLop != null){
	 		if(recLop.trim().compareTo("N")== 0){
		 		ra.setCursorPosition("RealEstate_txtDateToLop");
		 		return true;
		 	}
	 		if(recLop.trim().compareTo("D")== 0){
	 			if(ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateRecLop") == null){
		 			
		 			return true;
		 		}
		 	}else{
		 		ra.setCursorPosition("RealEstate_txtRecLop");
	 			return true;
		 	}
	 	}else{
	 		ra.showMessage("wrnclt50");
	 		//Potrebno je upisati D ili N u polje Upisano pravo banke
	 		ra.setCursorPosition("RealEstate_txtRecLop");
	 		return true;
	 	}
	 	
	 	return true;
	 }//RealEstate_txtDateRecLop_FV
			 	 
	 public boolean RealEstate2_txtDateRecLop_EF(){
		 //DATUM UPISA PRAVA BANKE - PODACI O STANJU UPISA PRAVA BANKE	

		String recLop = null;
	 	recLop = (String)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRecLop");
	 	Date dateToLopa= (Date)ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtDateToLop");
	
	 	if(recLop != null){
	 		if(recLop.trim().compareTo("N")== 0){
	 			if(dateToLopa == null){
	 				ra.setCursorPosition("RealEstate_txtDateToLop");
	 			}else{
	 				ra.setCursorPosition("RealEstate_txtRevReCounty");
	 			}
		 		return true;
		 	}else{
		 		if(recLop.trim().compareTo("D")!= 0){
		 			ra.setCursorPosition("RealEstate_txtRecLop");
		 			return true;
		 		}
		 	}
	 	}else{
	 		ra.showMessage("wrnclt50");
	 		//Potrebno je upisati D ili N u polje Upisano pravo banke	
	 		ra.setCursorPosition("RealEstate_txtRecLop");
	 		return true;
	 	}
	 	 return true;
	 	
	 }//RealEstate_txtDateRecLop_EF
				 
				 
				 
	 public boolean RealEstate2_txtRevRegCoefRe_FV(String elementName, Object elementValue, Integer lookUpType) {
	 //DATUM UPISA PRAVA BANKE - REGIONALNO_LOKACIJSKA OZNAKA NEKRETNINE	
	 //RealEstate_REVA_RE_COEF_ID
	 //RealEstate_txtRevReCode
	 //RealEstate_txtRevReName
	
        String ldbName = MASTER_LDBNAME;
        
        if (elementValue == null || ((String) elementValue).equals("")) {
        	ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRevReCode",null);
        	ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtRevReName",null);
        	ra.setAttribute(MASTER_LDBNAME,"RealEstate_REVA_RE_COEF_ID",null);
        	
        	ra.setAttribute(MASTER_LDBNAME,"RealEstate_COL_COUNTY",null);
        	ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReCounty", null);
        	ra.setAttribute(MASTER_LDBNAME,"RealEstate_COL_PLACE",null);
        	ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevRePlace", null);
        	ra.setAttribute(MASTER_LDBNAME,"RealEstate_COL_DISTRICT",null);
        	ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReDistrict", null);
        	ra.setAttribute(MASTER_LDBNAME,"RealEstate_COL_RESI_QUAR",null);
        	ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReResiQuar", null);
        	ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevDateFrom", null);		
        	ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevDateUntil", null);		
        	ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReCoef", null);		
        	
        	return true;
        }
        
        
        if (ra.getCursorPosition().equals("RealEstate_txtRevReName")) {
        	ra.setAttribute(ldbName, "RealEstate_txtRevReCode", "");
        } else if (ra.getCursorPosition().equals("RealEstate_txtRevReCode")) {
        	ra.setAttribute(ldbName, "RealEstate_txtRevReName", "");
        	//ra.setCursorPosition(2);
        }
        
        if (ra.isLDBExists("RevRegCoefReLookUpLDB")) {
        	ra.setAttribute("RevRegCoefReLookUpLDB", "rev_re_id", null);
        	ra.setAttribute("RevRegCoefReLookUpLDB", "rev_re_code", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "rev_re_name", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "county_id", null);
        	ra.setAttribute("RevRegCoefReLookUpLDB", "county_code", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "county_name", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "place_id", null);
        	ra.setAttribute("RevRegCoefReLookUpLDB", "place_code", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "place_name", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "district_id", null);
        	ra.setAttribute("RevRegCoefReLookUpLDB", "district_name", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "district_code", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "resi_quar_id", null);
        	ra.setAttribute("RevRegCoefReLookUpLDB", "resi_quar_code", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "resi_quar_name", "");
        	ra.setAttribute("RevRegCoefReLookUpLDB", "rev_date_from", null);
        	ra.setAttribute("RevRegCoefReLookUpLDB", "rev_date_unti", null);
        	ra.setAttribute("RevRegCoefReLookUpLDB", "rev_re_coef", null);
        	
        	
        } else {
        	ra.createLDB("RevRegCoefReLookUpLDB");
        }
        
        ra.setAttribute("RevRegCoefReLookUpLDB", "rev_re_code", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRevReCode"));
        ra.setAttribute("RevRegCoefReLookUpLDB", "rev_re_name", ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtRevReName"));
        
        LookUpRequest lookUpRequest = new LookUpRequest("RevRegCoefReLookUp");
        //addMapping ( LDBod lookupa, ime polja u LDB lookupa , ime iz iteratora u transakciji
        //Ova dva imena moraju biti ista
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "rev_re_id", "rev_re_id");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "rev_re_code", "rev_re_code");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "rev_re_name", "rev_re_name");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "county_id", "county_id");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "county_code", "county_code");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "county_name", "county_name");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "place_id", "place_id");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "place_code", "place_code");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "place_name", "place_name");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "district_id", "district_id");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "district_code", "district_code");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "district_name", "district_name");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "resi_quar_id", "resi_quar_id");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "resi_quar_code", "resi_quar_code");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "resi_quar_name", "resi_quar_name");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "rev_date_from", "rev_date_from");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "rev_date_unti", "rev_date_unti");
        lookUpRequest.addMapping("RevRegCoefReLookUpLDB", "rev_re_coef", "rev_re_coef");
        
        try {
        	ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
        	ra.showMessage("err012");
        			return false;
        } catch (NothingSelected ns) {
        			return false;
        }
         
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_REVA_RE_COEF_ID", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_re_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReCode", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_re_code"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReName", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_re_name"));
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_COUNTY", ra.getAttribute("RevRegCoefReLookUpLDB", "county_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReCounty", ra.getAttribute("RevRegCoefReLookUpLDB", "county_name"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReCountyCode", ra.getAttribute("RevRegCoefReLookUpLDB", "county_code"));
        
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_PLACE", ra.getAttribute("RevRegCoefReLookUpLDB", "place_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevRePlace", ra.getAttribute("RevRegCoefReLookUpLDB", "place_name"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevRePlaceCode", ra.getAttribute("RevRegCoefReLookUpLDB", "place_code"));
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_DISTRICT", ra.getAttribute("RevRegCoefReLookUpLDB", "district_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReDistrict", ra.getAttribute("RevRegCoefReLookUpLDB", "district_name"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReDistrictCode", ra.getAttribute("RevRegCoefReLookUpLDB", "district_code"));
        	
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_RESI_QUAR", ra.getAttribute("RevRegCoefReLookUpLDB", "resi_quar_id"));
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReResiQuar", ra.getAttribute("RevRegCoefReLookUpLDB", "resi_quar_name"));		
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReResiQuarCode", ra.getAttribute("RevRegCoefReLookUpLDB", "resi_quar_code"));		
        		
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevDateFrom", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_date_from"));		
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevDateUntil", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_date_unti"));		
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReCoef", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_re_coef"));		
        
        	
        return true;
	 }
				 
	 public boolean RealEstate2_txtRevReCounty_FV(String elementName, Object elementValue, Integer LookUp){
	 	//RealEstate_txtRevReCounty
	 	//RealEstate_COL_COUNTY
	 	BigDecimal countyTypeId = new BigDecimal("3999.0");
	 	
	 	ra.setAttribute(MASTER_LDBNAME, "dummySt", "");
	 	
	 	if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReCounty", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_COUNTY", null);
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevRePlace", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_PLACE", null);
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReDistrict", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_DISTRICT", null);
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReResiQuar", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_RESI_QUAR", null);
		     return true;
		 }
	 	
	 	if (ra.getCursorPosition().equals("RealEstate_txtRevReCounty")) {
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_COUNTY", null);
		} 
	 	
	 	 if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdLookUpLDB");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", countyTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_COL_COUNTY", "pol_map_id");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "code");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtRevReCounty", "name");
		
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
				 
	 public boolean RealEstate2_txtRevRePlace_FV(String elementName, Object elementValue, Integer LookUp){
	 	//RealEstate_txtRevRePlace
	 	//RealEstate_COL_PLACE
	 	BigDecimal zupanija = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_COUNTY");
	 	if(zupanija == null){
	 		ra.showMessage("wrnclt80");
	 		//Nije moguce odabrati mjesto ako nije odabrana zupanija
	 		return false;
	 	}
	 	ra.setAttribute(MASTER_LDBNAME, "dummySt", "");
	 	BigDecimal placeTypeId = new BigDecimal("5999.0");
		BigDecimal countyId = null;
			
		 if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute(MASTER_LDBNAME, "dummySt", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevRePlace", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_PLACE", null);
		    
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReDistrict", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_DISTRICT", null);
		     
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReResiQuar", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_RESI_QUAR", null);
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRegion", "");
		     return true;
		 }
		 
		 if (ra.getCursorPosition().equals("RealEstate_txtRevRePlace")) {
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_PLACE", null);
		 } 
		
		 if (!ra.isLDBExists("PolMapPlacesInCountyLookUpLDB")) {
		     ra.createLDB("PolMapPlacesInCountyLookUpLDB");
		 }
		 if (ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_COUNTY") != null) {
		 	countyId = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_COUNTY");
		 }
			
		 ra.setAttribute("PolMapPlacesInCountyLookUpLDB", "bDCountyId", countyId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PolMapPlacesInCountyLookUp");
		
		 lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_COL_PLACE", "pol_map_id");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "code");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtRevRePlace", "name"); 
		
		 try {
		     ra.callLookUp(lookUpRequest);
		 } catch (EmptyLookUp elu) {
		     ra.showMessage("err012");
		     return false;
		 } catch (NothingSelected ns) {
		     return false;
		 }
		BigDecimal pol_map_id=ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_PLACE");
		if(pol_map_id!=null){
		    ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRegion", coll_util.GetRegionFromDatabase(pol_map_id));
		}
	 	zupanija = null;
	 	return true;
	 }
				 
				 
	 public boolean RealEstate2_txtRevReDistrict_FV(String elementName, Object elementValue, Integer LookUp){
	 	//RealEstate_txtRevReDistrict
	 	//RealEstate_COL_DISTRICT
	 	
		BigDecimal mjesto = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_PLACE");
		if(mjesto == null){
	 		ra.showMessage("wrnclt81");
	 		//Nije moguce odabrati gradsku cetvrt ako nije odabrano mjesto
	 		return false;
	 	}
		
		BigDecimal districtTypeId = new BigDecimal("5854877003.0");
		BigDecimal placeId=null;
		ra.setAttribute(MASTER_LDBNAME, "dummySt", "");	
		 if (elementValue == null || ((String) elementValue).equals("")) {
		     
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReDistrict", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_DISTRICT", null);
		     
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReResiQuar", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_RESI_QUAR", null);
		    
		     
		     return true;
		 }
		 if (!ra.isLDBExists("PoliticalMapByTypIdWithParentLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdWithParentLookUpLDB");
		 }
		 if (ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_PLACE") != null) {
		 	placeId = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_PLACE");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDParentPolMapId", placeId);
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDPolMapTypId", districtTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdWithParentLookUp");
		 
		 lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_COL_DISTRICT", "pol_map_id");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "code");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtRevReDistrict", "name"); 
		
		 try {
		     ra.callLookUp(lookUpRequest);
		 } catch (EmptyLookUp elu) {
		     ra.showMessage("err012");
		     return false;
		 } catch (NothingSelected ns) {
		     return false;
		 }
		mjesto = null;
	 	return true;
	 }
	 public boolean RealEstate2_txtRevReResiQuar_FV(String elementName, Object elementValue, Integer LookUp){
	 	//RealEstate_txtRevReResiQuar
	 	//RealEstate_COL_RESI_QUAR
	 		
	 	BigDecimal gradskaCetvrt = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_DISTRICT");
	 	if(gradskaCetvrt == null){
	 		ra.showMessage("wrnclt82");
	 		//Nije moguce odabrati stambenu cetvrt ako nije odabrana gradska cetvrt
	 		return false;
	 	}
	 	
	 	BigDecimal resiquarTypeId = new BigDecimal("5854878003.0");
		BigDecimal districtId=null;
		ra.setAttribute(MASTER_LDBNAME, "dummySt", "");	
		 if (elementValue == null || ((String) elementValue).equals("")) {
		    
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRevReResiQuar", "");
		     ra.setAttribute(MASTER_LDBNAME, "RealEstate_COL_RESI_QUAR", null);
		     return true;
		 }
		
		
		 if (!ra.isLDBExists("PoliticalMapByTypIdWithParentLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdWithParentLookUpLDB");
		 }
		 if (ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_DISTRICT") != null) {
		 	districtId = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "RealEstate_COL_DISTRICT");
//					 	System.out.println(" GRADSKA CETVRT " + districtId);
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDParentPolMapId", districtId);
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDPolMapTypId", resiquarTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdWithParentLookUp");
		 //mapping se dodaje onim redoslijedom kojim se zeli naknadno citati
		 lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_COL_RESI_QUAR", "pol_map_id");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "code");
		 lookUpRequest.addMapping(MASTER_LDBNAME, "RealEstate_txtRevReResiQuar", "name"); 
		
		 try {
		     ra.callLookUp(lookUpRequest);
		 } catch (EmptyLookUp elu) {
		     ra.showMessage("err012");
		     return false;
		 } catch (NothingSelected ns) {
		     return false;
		 }
	
	 	gradskaCetvrt = null;
	 	return true;
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
	 
	public void postaviDatum(){
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		long timeT = calendar.getTime().getTime();
		todaySQLDate = new Date(timeT);
	}
	
    
	public boolean RealEstate_txtEstnMark_FV(String ElName, Object ElValue, Integer LookUp)	{	    
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME,new String[]{ "RealEstate_txtEstnMark", "RealEstate_txtEstnMarkDesc"});
            return true;        
        }
        coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtEstnMarkDesc");
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtEstnMark", "RealEstate_txtEstnMarkDesc", "kol_evaluation_typ");
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEstnMark", value.sysCodeValue); 
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtEstnMarkDesc", value.sysCodeDesc);  
        
        //Change Request 18393
        if(ra.getScreenContext().equalsIgnoreCase("scr_change") || ra.getScreenContext().equalsIgnoreCase("scr_update")){
            if(Arrays.asList(listEstnMark).contains(value.sysCodeValue)){            
                coll_util.enableFields(new String[]{"RealEstate_txtEUseIdLogin","RealEstate_txtEUseIdName",
                        "RealEstate_txtAssessmentMethod1Code", "RealEstate_txtAssessmentMethod1Desc","RealEstate_txtAssessmentMethod2Code","RealEstate_txtAssessmentMethod2Desc"}, 2);
                if(!SEFlag) coll_util.clearFields(MASTER_LDBNAME,new String[]{"RealEstate_RCEstimateId","RealEstate_REAL_EST_EUSE_ID","RealEstate_txtEUseIdLogin","RealEstate_txtEUseIdOIB",
                        "RealEstate_txtEUseIdName","RealEstate_txtTypelValuer", "RealEstate_txtEUseIdLoginRC","RealEstate_txtEUseIdRCOIB","RealEstate_txtEUseIdNameRC","RealEstate_txtAssessmentMethod1Code",
                        "RealEstate_txtAssessmentMethod1Desc","RealEstate_txtAssessmentMethod2Code","RealEstate_txtAssessmentMethod2Desc","EST_TYPE_CODE","RealEstate_txtInternalValuer"});
                coll_util.setRequired(new String[]{"RealEstate_txtEUseIdLogin","RealEstate_txtEUseIdName",
                        "RealEstate_txtAssessmentMethod1Code","RealEstate_txtAssessmentMethod1Desc","RealEstate_txtAssessmentMethod2Code","RealEstate_txtAssessmentMethod2Desc"}, false);
                
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAssessmentMethod1Code", "5");
                ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAssessmentMethod2Code", "5");
                coll_util.clearFields(MASTER_LDBNAME, new String[]{"RealEstate_txtAssessmentMethod1Desc","RealEstate_txtAssessmentMethod2Desc"});
                ra.invokeValidation("RealEstate_txtAssessmentMethod1Code");
                ra.invokeValidation("RealEstate_txtAssessmentMethod2Code");
            } else {
                coll_util.enableFields(new String[]{"RealEstate_txtEUseIdLogin","RealEstate_txtEUseIdName",
                        "RealEstate_txtAssessmentMethod1Code", "RealEstate_txtAssessmentMethod1Desc","RealEstate_txtAssessmentMethod2Code","RealEstate_txtAssessmentMethod2Desc"}, 0);
    //            if(!SEFlag) coll_util.clearFields(MASTER_LDBNAME,new String[]{"RealEstate_txtEUseIdLogin","RealEstate_txtEUseIdOIB","RealEstate_txtEUseIdName","RealEstate_txtTypelValuer",
    //                    "RealEstate_txtEUseIdLoginRC","RealEstate_txtEUseIdRCOIB","RealEstate_txtEUseIdNameRC","RealEstate_txtAssessmentMethod1Code",
    //                    "RealEstate_txtAssessmentMethod1Desc","RealEstate_txtAssessmentMethod2Code","RealEstate_txtAssessmentMethod2Desc"});
                coll_util.setRequired(new String[]{"RealEstate_txtEUseIdLogin","RealEstate_txtEUseIdName",
                        "RealEstate_txtAssessmentMethod1Code","RealEstate_txtAssessmentMethod1Desc","RealEstate_txtAssessmentMethod2Code","RealEstate_txtAssessmentMethod2Desc"}, true);
                
                if(!(old_EstnMark_value==null || "1".equals(old_EstnMark_value) || "2".equals(old_EstnMark_value))){
                    ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAssessmentMethod1Code", "2");
                    ra.setAttribute(MASTER_LDBNAME,"RealEstate_txtAssessmentMethod2Code", "3");
                    coll_util.clearFields(MASTER_LDBNAME, new String[]{"RealEstate_txtAssessmentMethod1Desc","RealEstate_txtAssessmentMethod2Desc"});
                    ra.invokeValidation("RealEstate_txtAssessmentMethod1Code");
                    ra.invokeValidation("RealEstate_txtAssessmentMethod2Code"); 
                }
            }
            old_EstnMark_value=ra.getAttribute(MASTER_LDBNAME, "RealEstate_txtEstnMark");
        }
        return true;
    }
	
	public boolean RealEstate_txtOcjenaNekretnine_FV(String ElName, Object ElValue, Integer LookUp){
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME,new String[]{ "RealEstate_txtOcjenaNekretnineCode", "RealEstate_txtOcjenaNekretnineDesc"});
            return true;        
        }
        if (ra.getCursorPosition().equals("RealEstate_txtOcjenaNekretnineCode")) {
            coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtOcjenaNekretnineDesc");
            ra.setCursorPosition(2);
        } else {
            coll_util.clearField(MASTER_LDBNAME,"RealEstate_txtOcjenaNekretnineCode");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtOcjenaNekretnineCode", "RealEstate_txtOcjenaNekretnineDesc", "coll_grade");
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtOcjenaNekretnineCode", value.sysCodeValue);
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtOcjenaNekretnineDesc", value.sysCodeDesc);  
        return true;
    }

//    public boolean RealEstate_txtRegion_FV(String ElName, Object ElValue, Integer LookUp) {
//        if(ElValue==null || ElValue.equals("")){
//            coll_util.clearFields(MASTER_LDBNAME,new String[]{ "RealEstate_txtRegionCode", "RealEstate_txtRegion"});
//            return true;        
//        }
//        if(!ElName.equals("RealEstate_txtRegionCode")) coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtRegionCode");
//        if(!ElName.equals("RealEstate_txtRegion")) coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtRegion");
//        
//        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
//        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtRegionCode", "RealEstate_txtRegion", "coll_region");
//        if(value==null) return false;
//        
//        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRegionCode", value.sysCodeValue); 
//        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtRegion", value.sysCodeDesc);  
//        return true;
//    }

    public boolean RealEstate_txtAssessmentMethod1_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME,new String[]{ "RealEstate_txtAssessmentMethod1Code", "RealEstate_txtAssessmentMethod1Desc"});
            return true;        
        }
        if (ra.getCursorPosition().equals("RealEstate_txtAssessmentMethod1Code")) {
            coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod1Desc");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("RealEstate_txtAssessmentMethod1Desc")){
            coll_util.clearField(MASTER_LDBNAME,"RealEstate_txtAssessmentMethod1Code");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod1Code", "RealEstate_txtAssessmentMethod1Desc", "coll_Assessment");
        if(value==null) return false;

        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod1Code", value.sysCodeValue);
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod1Desc", value.sysCodeDesc);  
        return true;
    }
    
    public boolean RealEstate_txtAssessmentMethod2_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME,new String[]{ "RealEstate_txtAssessmentMethod2Code", "RealEstate_txtAssessmentMethod2Desc"});
            return true;        
        }
        if (ra.getCursorPosition().equals("RealEstate_txtAssessmentMethod2Code")) {
            coll_util.clearField(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod2Desc");
            ra.setCursorPosition(2);
        } else if(ra.getCursorPosition().equals("RealEstate_txtAssessmentMethod2Desc")){
            coll_util.clearField(MASTER_LDBNAME,"RealEstate_txtAssessmentMethod2Code");
        }
        
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod2Code", "RealEstate_txtAssessmentMethod2Desc", "coll_Assessment");
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod2Code", value.sysCodeValue);
        ra.setAttribute(MASTER_LDBNAME, "RealEstate_txtAssessmentMethod2Desc", value.sysCodeDesc);  
        return true;
    }
    
    public boolean RealEstate_Grade_FV(String ElName, Object ElValue, Integer LookUp) {
        if(ElValue==null || ElValue.equals("")){
            coll_util.clearFields(MASTER_LDBNAME, new String[]{ ElName});
            return true;        
        }
        LookUps.SystemCodeLookUpReturnValues value = coll_lookups.new SystemCodeLookUpReturnValues();
        value=coll_lookups.SystemCodeValue(MASTER_LDBNAME, ElName, null, "rating_assessment");
        if(value==null) return false;
        
        ra.setAttribute(MASTER_LDBNAME, ElName, value.sysCodeValue); 

        return true;
    }
}
