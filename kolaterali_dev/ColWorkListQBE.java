/*
 * Created on 2006.07.16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColWorkListQBE extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ColWorkListQBE.java,v 1.14 2006/09/22 08:54:45 hrasia Exp $";;
	public ColWorkListQBE(ResourceAccessor ra) {
		super(ra);
	}
	public void ColWorkListQBE_SE() {
		if (!(ra.isLDBExists("ColWorkListQBE_LDB"))) {
			ra.createLDB("ColWorkListQBE_LDB");
		}
		
		

//		String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status");

// ovo odredjivanje na kojoj si listi prema proc_statusu nije dobro 
// vrijednost proc_status se promijeni ako se npr. na arhivskoj listi vrati neki predmet
// u obradu, na 0 - posljedica je da se na arhivskoj listi vide ne moze pretrazivati po OJ i user-u 
// uvodim novi proc_status_QBE vrijednost kojeg se postavlja jednom kod ulaska na listu
// Milka, 08.09.2006
		  
		String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status_QBE");
		
		if(procStatus.compareTo("0")== 0){
	
			
			//REFERENTSKA LISTA  
			//proc_status= 0
			
			ra.setContext("RealEstate_txtQbeOJCodeRefOpen", "fld_protected");
			ra.setContext("RealEstate_txtQbeOJNameRefOpen", "fld_protected");
			
			ra.setContext("RealEstate_txtQbeUseOpenIdLogin", "fld_protected");
			ra.setContext("RealEstate_txtQbeUseOpenIdName", "fld_protected");
			
			
			
			
		}
		
		if(procStatus.compareTo("3")== 0){
			
		
			//ARHIVSKA LISTA
			//proc_status = 3
		}
		
		
		if(procStatus.compareTo("4")== 0){
			
		
			//LISTA PONISTENIH OBUSTAVLJENIH
			//4
		}	
		if(procStatus.compareTo("M")== 0){
			
		
			//MONITORING LISTA
			//M
		}	
			
			
			
			
		
		
	}//ColWorkListQBE_SE
	 public boolean RealEstate_CarrierQbe_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
	 	//Korisnik plasmana
        //RealEstate_lblCarrierCol
        //RealEstate_CarrierCUS_ID
		//RealEstate_txtCarrierQbeRegNo
		//RealEstate_txtCarrierQbeName
	 	
	 	
	 	
		String ldbName = "ColWorkListQBE_LDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("ColWorkListQBE_LDB","RealEstate_txtCarrierQbeRegNo",null);
			ra.setAttribute("ColWorkListQBE_LDB","RealEstate_txtCarrierQbeName",null);
			ra.setAttribute("ColWorkListQBE_LDB","RealEstate_CarrierCUS_ID",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("RealEstate_txtCarrierQbeName")) {
			ra.setAttribute(ldbName, "RealEstate_txtCarrierQbeRegNo", "");
		} else if (ra.getCursorPosition().equals("RealEstate_txtCarrierQbeRegNo")) {
			ra.setAttribute(ldbName, "RealEstate_txtCarrierQbeName", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtCarrierQbeName") != null){
			d_name = (String) ra.getAttribute(ldbName, "RealEstate_txtCarrierQbeName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtCarrierQbeRegNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "RealEstate_txtCarrierQbeRegNo");
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
		
		
		 if (ra.getCursorPosition().equals("RealEstate_txtCarrierQbeRegNo")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtCarrierQbeRegNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtCarrierQbeName"));

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
		
		ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_CarrierCUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtCarrierQbeRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtCarrierQbeName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		if(ra.getCursorPosition().equals("RealEstate_txtCarrierQbeRegNo")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtCarrierQbeName")){
			ra.setCursorPosition(1);
		}
		
		
		return true;

		
	}//RealEstate_CarrierQbe_FV
	 public boolean RealEstate_OwnerQbe_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
	 			//Vlasnik kolaterala
        //RealEstate_lblOwnerCol
        //RealEstate_OwnerCUS_ID
				//RealEstate_txtRealEstateRegisterNoRE
				//RealEstate_txtRealEstateOwnerNameRE
	 	
	 	
		String ldbName = "ColWorkListQBE_LDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("ColWorkListQBE_LDB","RealEstate_txtOwnerQbeRegNo",null);
			ra.setAttribute("ColWorkListQBE_LDB","RealEstate_txtOwnerQbeName",null);
			ra.setAttribute("ColWorkListQBE_LDB","RealEstate_OwnerCUS_ID",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeName")) {
			ra.setAttribute(ldbName, "RealEstate_txtOwnerQbeRegNo", "");
		} else if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeRegNo")) {
			ra.setAttribute(ldbName, "RealEstate_txtOwnerQbeName", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtOwnerQbeName") != null){
			d_name = (String) ra.getAttribute(ldbName, "RealEstate_txtOwnerQbeName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtOwnerQbeRegNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "RealEstate_txtOwnerQbeRegNo");
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
		
		
		 if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeRegNo")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtOwnerQbeRegNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtOwnerQbeName"));

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
		
		ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_OwnerCUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtOwnerQbeRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtOwnerQbeName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		if(ra.getCursorPosition().equals("RealEstate_txtOwnerQbeRegNo")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtOwnerQbeName")){
			ra.setCursorPosition(1);
		}
		
		
		return true;

		
	}//RealEstate_OwnerQbe_FV
	
	 public boolean RealEstate_QbeCadastreMuncipality_FV(String ElName, Object ElValue, Integer LookUp) {              

		// KATASTARSKA OPCINA:
		// RealEstate_QBE_CADA_MUNC							
		// RealEstate_txtCadMuncCode
		// RealEstate_txtCadMuncName
		
		
		if (ElValue == null || ((String) ElValue).equals("")) {  
			
			
			
					ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtCadMuncCode", "");                        
					ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtCadMuncName", "");                        
					ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_QBE_CADA_MUNC", null);                               
					return true;                                                                                 
		}                                                                                              
         
		
		if (!ra.isLDBExists("CadastreMuncipalityLookUpLDB")) {
	 		ra.createLDB("CadastreMuncipalityLookUpLDB");
	 	}
	 	//ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDPolMapId", null);
		//ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDCoId", (java.math.BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_REAL_EST_COURT_ID"));
		ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDPolMapId", null);
		ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDCoId", null);
		
		
		
		
		
		LookUpRequest lookUpRequest = new LookUpRequest("CadastreMuncipalityLookUp");                       
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_QBE_CADA_MUNC", "cad_map_id");           
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_txtCadMuncCode", "code");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_txtCadMuncName", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}  
		if(ra.getCursorPosition().equals("RealEstate_txtCadMuncCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtCadMuncName")){
			ra.setCursorPosition(1);
		}	
		return true;                                                                                   
    
	}//RealEstate_QbeCadastreMuncipality_FV     

	 
	 	//OJ referenta unosa
	 	//RealEstate_Qbe_ORG_UNI_ID
		//RealEstate_txtQbeOJCodeRefOpen
		//RealEstate_txtQbeOJNameRefOpen
	 
	 public boolean RealEstate_txtQbeOJ_FV(String elementName, Object elementValue, Integer lookUpType) {
		
	 	if (elementValue == null || ((String) elementValue).equals("")) {  
	 				ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeOJCodeRefOpen", "");                        
					ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeOJNameRefOpen", "");                        
					ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_Qbe_ORG_UNI_ID", null);   
					ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdLogin", "");                        
					ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdName", "");                        
					ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_QbeUSE_OPEN_ID", null);
					return true;                                                                                 
		}   
	 	
	 	
	 	if (ra.getCursorPosition().equals("RealEstate_txtQbeOJCodeRefOpen")) {
	 		ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeOJCodeRefOpen", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeOJNameRefOpen", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_Qbe_ORG_UNI_ID", null);  
			
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdLogin", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdName", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_QbeUSE_OPEN_ID", null);      
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("RealEstate_txtQbeOJNameRefOpen")) {
		 	ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeOJCodeRefOpen", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeOJNameRefOpen", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_Qbe_ORG_UNI_ID", null);  
		 	
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdLogin", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdName", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_QbeUSE_OPEN_ID", null);     
		 }		
		
	 	
	 	
		LookUpRequest lookUpRequest = new LookUpRequest("OrgUniLookUp");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_txtQbeOJCodeRefOpen", "code");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_txtQbeOJNameRefOpen", "name");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_Qbe_ORG_UNI_ID", "org_uni_id");
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		if(ra.getCursorPosition().equals("RealEstate_txtQbeOJCodeRefOpen")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtQbeOJNameRefOpen")){
			ra.setCursorPosition(1);
		}	
		
		return true;
	}

	 	//Referent unosa
	 	//RealEstate_QbeUSE_OPEN_ID
	 	//RealEstate_txtQbeUseOpenIdLogin
	 	//RealEstate_txtQbeUseOpenIdName
	 
	 
	 
	 public boolean RealEstate_txtQbeUseOpen_FV(String elementName, Object elementValue, Integer lookUpType) {
		
		
	 	if (elementValue == null || ((String) elementValue).equals("")) {  
				ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdLogin", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdName", "");                        
			ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_QbeUSE_OPEN_ID", null);                               
			return true;                                                                                 
	 	}   
	 	
	 	
	 	if (ra.getCursorPosition().equals("RealEstate_txtQbeUseOpenIdLogin")) {
		     ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdName", "");
		     ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_QbeUSE_OPEN_ID", null);      
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("RealEstate_txtQbeUseOpenIdName")) {
		     ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdLogin", "");
		     ra.setAttribute("ColWorkListQBE_LDB", "RealEstate_QbeUSE_OPEN_ID", null);      
		 }		
		
		
		if (!ra.isLDBExists("AppUserOrgLDB")) ra.createLDB("AppUserOrgLDB");
		ra.setAttribute("AppUserOrgLDB", "org_uni_id",  ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_Qbe_ORG_UNI_ID"));	
		ra.setAttribute("ColWorkListQBE_LDB", "dummySt", "");
		ra.setAttribute("ColWorkListQBE_LDB", "dummyBd", "");

	 	
		
	 	
		LookUpRequest lookUpRequest = new LookUpRequest("AppUserOrgLookUpColl");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_QbeUSE_OPEN_ID", "use_id");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdLogin", "login");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "RealEstate_txtQbeUseOpenIdName", "user_name");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "dummySt", "abbreviation");
		lookUpRequest.addMapping("ColWorkListQBE_LDB", "dummyBd", "org_uni_id");
		
		
		
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		if(ra.getCursorPosition().equals("RealEstate_txtQbeUseOpenIdLogin")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtQbeUseOpenIdName")){
			ra.setCursorPosition(1);
		}	
		return true;

	}

	 
	 
	 
	 
	 	//Datum unosa
	 	//RealEstate_QbeDateFrom
	 	//RealEstate_QbeDateUntil
	 //RealEstate_txtQbeDateFrom    
	 //RealEstate_txtQbeDateUntil   

	 
	 public boolean RealEstate_txtQbeDateFrom_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColWorkListQBE_LDB","RealEstate_txtQbeDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColWorkListQBE_LDB","RealEstate_txtQbeDateUntil");
	 	
	 	if(datumOd != null && datumDo != null){
	 		if(datumDo.before(datumOd)){
 				//Datum DO ne moze biti manji od datuma OD.
 		 		ra.showMessage("wrnclt30c");
 				return false;
 			}
	 	}
	 	
	 	
	 	return true; 
	 }
	 
	 public boolean RealEstate_txtQbeDateUntil_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColWorkListQBE_LDB","RealEstate_txtQbeDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColWorkListQBE_LDB","RealEstate_txtQbeDateUntil");
	 	if(datumOd == null){
	 	
	 		//Najprije upisite datum OD
	 		ra.showMessage("wrnclt30d");
	 		return false;
	 	}else{
	 		if(datumDo != null){
	 			if(datumDo.before(datumOd)){
	 				//Datum DO ne moze biti manji od datuma OD.
	 		 		ra.showMessage("wrnclt30c");
	 				return false;
	 			}
	 		}
	 	
	 	}
	 	
	 	
	 	return true; 
	 }
	 
	 public void confirm() {
	 	if(ra.getScreenContext().compareTo("scr_nekr")== 0 ){
	 		//		 RealEstate_OwnerCUS_ID
			//		 RealEstate_CarrierCUS_ID
			//		 RealEstate_txtQbeRealEstLandRegn
			//		 RealEstate_txtQbeLandSub
	 		//		 RealEstate_QBE_CADA_MUNC
			//		 RealEstate_txtCoown
	 		
			
			 

	 		
	 		
	 		
	 		//	 		owner
	 		//			carrier
	 		//			REAL_EST_LAND_REGN	char(25)	no		Broj zemljisno-knjiznog uloska
	 		//			REAL_EST_LAND_SUB	char(25)	yes		Broj zemljišno-knjižnog poduloška
	 		//			REAL_EST_CADA_MUNC	decimal(16,0)	no	F(CADASTRE_MAP)	Katastarska opcina   cadastre muncipality
	 		//			REAL_EST_LAND_PART	char(25)	no		Katastarska cestica
	 		//			COOWN	varchar(25)	yes					Suvlasnicki dio
	 		//			
	 		//			acc_no									Partija plasmana
	 		java.math.BigDecimal ownerCusId = null;
	 		java.math.BigDecimal carrierCusId = null;
			String landRegnZKU = null;
			String landSubPU = null;
			java.math.BigDecimal cadaMuncQbe = null;
			String coownSuvlDio = null;
			String accNoPartija = null;
			String requestNo = null;
			java.math.BigDecimal originOrgUniId = null;
			java.math.BigDecimal openUseId = null;
			java.sql.Date dateFromIn = null;
			java.sql.Date dateUntilIn= null;
			String sifra = null;
			
			ownerCusId = (java.math.BigDecimal)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_OwnerCUS_ID");
			carrierCusId = (java.math.BigDecimal)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_CarrierCUS_ID");
			landRegnZKU = (String)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeRealEstLandRegn");
			landSubPU = (String)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeLandSub");
			cadaMuncQbe = (java.math.BigDecimal)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_QBE_CADA_MUNC");
			coownSuvlDio = (String)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtCoown");
			accNoPartija = (String)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeAccNo");
			requestNo = (String)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeRequestNo");
			
			
			originOrgUniId = (java.math.BigDecimal)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_Qbe_ORG_UNI_ID");
			openUseId = (java.math.BigDecimal)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_QbeUSE_OPEN_ID");
			dateFromIn = (java.sql.Date)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeDateFrom");
			
			sifra = (String)ra.getAttribute("ColWorkListQBE_LDB", "RealEstate_txtQbeCode");
			
			if(requestNo != null){
				requestNo = requestNo.trim();
				if(requestNo.length() == 0){
					requestNo = null;
				}
			}
			
			
			if(landRegnZKU != null){
				landRegnZKU = landRegnZKU.trim();
				if(landRegnZKU.length() == 0){
					landRegnZKU = null;
				}
			}
			if(landSubPU != null){
				landSubPU = landSubPU.trim();
				if(landSubPU.length() == 0){
					landSubPU = null;
				}
			}
			if(coownSuvlDio != null){
				coownSuvlDio = coownSuvlDio.trim();
				if(coownSuvlDio.length() == 0){
					coownSuvlDio = null;
				}
			}
			if(accNoPartija != null){
				accNoPartija = accNoPartija.trim();
				if(accNoPartija.length() == 0){
					accNoPartija = null;
				}
			}    
//			String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status");
// Milka 08.09.2006			
			String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status_QBE");			
			
			if(procStatus.compareTo("3")!= 0 && procStatus.compareTo("4")!= 0 && procStatus.compareTo("M")!= 0){
				if((sifra == null) && (ownerCusId == null) && (carrierCusId == null) && (landRegnZKU == null) && (landSubPU == null) && (cadaMuncQbe == null) && (coownSuvlDio == null) && (accNoPartija == null) && (dateFromIn == null) ){
					ra.showMessage("wrnclt53");
					return;
				}
			}
			if(procStatus.compareTo("3")== 0 && procStatus.compareTo("4")== 0 && procStatus.compareTo("M")== 0){
				if((sifra == null) && (ownerCusId == null) && (carrierCusId == null) && (landRegnZKU == null) && (landSubPU == null) && (cadaMuncQbe == null) && (coownSuvlDio == null) && (accNoPartija == null) && (dateFromIn == null) && (originOrgUniId == null) && (openUseId == null) ){
					ra.showMessage("wrnclt53");
					return;
				}
			}
			
			
	 		//POZIV TRANSAKCIJE QBE z anekretninu
	 		try {
	 			ra.executeTransaction();
	 			
	 		} catch (VestigoTMException vtme) {
	 			error("RealEstateDialog -> insert(): VestigoTMException", vtme);
	 			if (vtme.getMessageID() != null)
	 				ra.showMessage(vtme.getMessageID());
	 		}
	 		ra.exitScreen();
	 		
	 		
	 		
	 		
	 		
	 	}
	 }

	
	
	
}
