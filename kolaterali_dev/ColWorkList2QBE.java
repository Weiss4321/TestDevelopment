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



public class ColWorkList2QBE extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ColWorkList2QBE.java,v 1.6 2006/09/22 08:54:40 hrasia Exp $";
	public ColWorkList2QBE(ResourceAccessor ra) {
		super(ra);
	}
	public void ColWorkList2QBE_SE() {
		if (!(ra.isLDBExists("ColWorkList2QBE_LDB"))) {
			ra.createLDB("ColWorkList2QBE_LDB");
		}
		
		
//		String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status");
//		 ovo odredjivanje na kojoj si listi prema proc_statusu nije dobro 
//		 vrijednost proc_status se promijeni ako se npr. na arhivskoj listi vrati neki predmet
//		 u obradu, na 0 - posljedica je da se na arhivskoj listi vide ne moze pretrazivati po OJ i user-u 
//		 uvodim novi proc_status_QBE vrijednost kojeg se postavlja jednom kod ulaska na listu
//		 Milka, 08.09.2006
				  
		String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status_QBE");
		
		if(procStatus.compareTo("0")== 0){
				
			
			//REFERENTSKA LISTA
			//proc_status= 0
			
			ra.setContext("RealEstate_txtQbeOJCodeRefOpen2", "fld_protected");
			ra.setContext("RealEstate_txtQbeOJNameRefOpen2", "fld_protected");
			
			ra.setContext("RealEstate_txtQbeUseOpenIdLogin2", "fld_protected");
			ra.setContext("RealEstate_txtQbeUseOpenIdName2", "fld_protected");
			
			
			
			
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
			
			
			
			
		
		
	}//ColWorkList2QBE_SE
	 public boolean RealEstate_CarrierQbe2_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
	 	//Korisnik plasmana
        //RealEstate_lblCarrierCol
        //RealEstate_CarrierCUS_ID
		//RealEstate_txtCarrierQbeRegNo
		//RealEstate_txtCarrierQbeName
	 	
	 	
	 	
		String ldbName = "ColWorkList2QBE_LDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("ColWorkList2QBE_LDB","RealEstate_txtCarrierQbeRegNo2",null);
			ra.setAttribute("ColWorkList2QBE_LDB","RealEstate_txtCarrierQbeName2",null);
			ra.setAttribute("ColWorkList2QBE_LDB","RealEstate_CarrierCUS_ID2",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("RealEstate_txtCarrierQbeName2")) {
			ra.setAttribute(ldbName, "RealEstate_txtCarrierQbeRegNo2", "");
		} else if (ra.getCursorPosition().equals("RealEstate_txtCarrierQbeRegNo2")) {
			ra.setAttribute(ldbName, "RealEstate_txtCarrierQbeName2", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtCarrierQbeName2") != null){
			d_name = (String) ra.getAttribute(ldbName, "RealEstate_txtCarrierQbeName2");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtCarrierQbeRegNo2") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "RealEstate_txtCarrierQbeRegNo2");
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
		
		
		 if (ra.getCursorPosition().equals("RealEstate_txtCarrierQbeRegNo2")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_txtCarrierQbeRegNo2"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_txtCarrierQbeName2"));

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
		
		ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_CarrierCUS_ID2", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtCarrierQbeRegNo2", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtCarrierQbeName2", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		if(ra.getCursorPosition().equals("RealEstate_txtCarrierQbeRegNo2")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtCarrierQbeName2")){
			ra.setCursorPosition(1);
		}
		
		
		return true;

		
	}//RealEstate_CarrierQbe_FV
	 public boolean RealEstate_OwnerQbe2_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
	 			//Vlasnik kolaterala
        //RealEstate_lblOwnerCol
        //RealEstate_OwnerCUS_ID
				//RealEstate_txtRealEstateRegisterNoRE
				//RealEstate_txtRealEstateOwnerNameRE
	 	
	 	
		String ldbName = "ColWorkList2QBE_LDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("ColWorkList2QBE_LDB","RealEstate_txtOwnerQbeRegNo2",null);
			ra.setAttribute("ColWorkList2QBE_LDB","RealEstate_txtOwnerQbeName2",null);
			ra.setAttribute("ColWorkList2QBE_LDB","RealEstate_OwnerCUS_ID2",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeName2")) {
			ra.setAttribute(ldbName, "RealEstate_txtOwnerQbeRegNo2", "");
		} else if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeRegNo2")) {
			ra.setAttribute(ldbName, "RealEstate_txtOwnerQbeName2", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtOwnerQbeName2") != null){
			d_name = (String) ra.getAttribute(ldbName, "RealEstate_txtOwnerQbeName2");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtOwnerQbeRegNo2") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "RealEstate_txtOwnerQbeRegNo2");
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
		
		
		 if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeRegNo2")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_txtOwnerQbeRegNo2"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_txtOwnerQbeName2"));

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
		
		ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_OwnerCUS_ID2", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtOwnerQbeRegNo2", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtOwnerQbeName2", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		if(ra.getCursorPosition().equals("RealEstate_txtOwnerQbeRegNo2")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtOwnerQbeName2")){
			ra.setCursorPosition(1);
		}
		
		
		return true;

		
	}//RealEstate_OwnerQbe_FV
	
	 
	 
	 	//OJ referenta unosa
	 	//RealEstate_Qbe_ORG_UNI_ID2
		//RealEstate_txtQbeOJCodeRefOpen2
		//RealEstate_txtQbeOJNameRefOpen2
	 
	 public boolean RealEstate_txtQbeOJ2_FV(String elementName, Object elementValue, Integer lookUpType) {
		
	 	if (elementValue == null || ((String) elementValue).equals("")) {  
	 				ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeOJCodeRefOpen2", "");                        
					ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeOJNameRefOpen2", "");                        
					ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_Qbe_ORG_UNI_ID2", null);                               
					return true;                                                                                 
		}   
	 	
	 	
	 	if (ra.getCursorPosition().equals("RealEstate_txtQbeOJCodeRefOpen2")) {
	 		ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeOJCodeRefOpen2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeOJNameRefOpen2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_Qbe_ORG_UNI_ID2", null); 
	 		ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdLogin2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdName2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_QbeUSE_OPEN_ID2", null);      
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("RealEstate_txtQbeOJNameRefOpen2")) {
		 	ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeOJCodeRefOpen2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeOJNameRefOpen2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_Qbe_ORG_UNI_ID2", null); 
		 	ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdLogin2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdName2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_QbeUSE_OPEN_ID2", null);     
		 }	
	 	
		
	 	
	 	
		LookUpRequest lookUpRequest = new LookUpRequest("OrgUniLookUp");
		lookUpRequest.addMapping("ColWorkList2QBE_LDB", "RealEstate_txtQbeOJCodeRefOpen2", "code");
		lookUpRequest.addMapping("ColWorkList2QBE_LDB", "RealEstate_txtQbeOJNameRefOpen2", "name");
		lookUpRequest.addMapping("ColWorkList2QBE_LDB", "RealEstate_Qbe_ORG_UNI_ID2", "org_uni_id");
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		if(ra.getCursorPosition().equals("RealEstate_txtQbeOJCodeRefOpen2")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtQbeOJNameRefOpen2")){
			ra.setCursorPosition(1);
		}	
		
		return true;
	}

	 	//Referent unosa
	 	//RealEstate_QbeUSE_OPEN_ID2
	 	//RealEstate_txtQbeUseOpenIdLogin2
	 	//RealEstate_txtQbeUseOpenIdName2
	 
	 
	 
	 public boolean RealEstate_txtQbeUseOpen2_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
 	 	if (elementValue == null || ((String) elementValue).equals("")) {  
				ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdLogin2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdName2", "");                        
			ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_QbeUSE_OPEN_ID2", null);                               
			return true;                                                                                 
	 	}   
	 	
	 	if (ra.getCursorPosition().equals("RealEstate_txtQbeUseOpenIdLogin2")) {
		     ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdName2", "");
		     ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_QbeUSE_OPEN_ID2", null);      
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("RealEstate_txtQbeUseOpenIdName2")) {
		     ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdLogin2", "");
		     ra.setAttribute("ColWorkList2QBE_LDB", "RealEstate_QbeUSE_OPEN_ID2", null);      
		 }	
	 	
	 	
		 if (!ra.isLDBExists("AppUserOrgLDB")) ra.createLDB("AppUserOrgLDB");
			ra.setAttribute("AppUserOrgLDB", "org_uni_id",  ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_Qbe_ORG_UNI_ID2"));	
			ra.setAttribute("ColWorkList2QBE_LDB", "dummySt", "");
			ra.setAttribute("ColWorkList2QBE_LDB", "dummyBd", "");
	
	 	
		LookUpRequest lookUpRequest = new LookUpRequest("AppUserOrgLookUpColl");
		lookUpRequest.addMapping("ColWorkList2QBE_LDB", "RealEstate_QbeUSE_OPEN_ID2", "use_id");
		lookUpRequest.addMapping("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdLogin2", "login");
		lookUpRequest.addMapping("ColWorkList2QBE_LDB", "RealEstate_txtQbeUseOpenIdName2", "user_name");
		lookUpRequest.addMapping("ColWorkList2QBE_LDB", "dummySt", "abbreviation");
		lookUpRequest.addMapping("ColWorkList2QBE_LDB", "dummyBd", "org_uni_id");
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		if(ra.getCursorPosition().equals("RealEstate_txtQbeUseOpenIdLogin2")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("RealEstate_txtQbeUseOpenIdName2")){
			ra.setCursorPosition(1);
		}	
		return true;

	}

	 
	 
	 
	 
	 	//Datum unosa
	 	//RealEstate_txtQbeDateFrom2
	 	//RealEstate_txtQbeDateUntil2
	 
	 public boolean RealEstate_txtQbeDateFrom2_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColWorkList2QBE_LDB","RealEstate_txtQbeDateFrom2");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColWorkList2QBE_LDB","RealEstate_txtQbeDateUntil2");
	 	
	 	if(datumOd != null && datumDo != null){
	 		if(datumDo.before(datumOd)){
 				//Datum DO ne moze biti manji od datuma OD.
 		 		ra.showMessage("wrnclt30c");
 				return false;
 			}
	 	}
	 	
	 	
	 	return true; 
	 }
	 
	 public boolean RealEstate_txtQbeDateUntil2_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColWorkList2QBE_LDB","RealEstate_txtQbeDateFrom2");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColWorkList2QBE_LDB","RealEstate_txtQbeDateUntil2");
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
	 	
	 		//		 RealEstate_OwnerCUS_ID2
			//		 RealEstate_CarrierCUS_ID2
			
	 		
			
			 

	 		
	 		
	 		
	 		//	 		owner
	 		//			carrier
	 		//			
	 		//			acc_no									Partija plasmana
	 		java.math.BigDecimal ownerCusId = null;
	 		java.math.BigDecimal carrierCusId = null;
			
	 		String accNoPartija = null;
	 		String requestNo = null;
			java.math.BigDecimal originOrgUniId = null;
			java.math.BigDecimal openUseId = null;
			java.sql.Date dateFromIn = null;
			java.sql.Date dateUntilIn= null;
			String sifra = null;
			
			ownerCusId = (java.math.BigDecimal)ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_OwnerCUS_ID2");
			carrierCusId = (java.math.BigDecimal)ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_CarrierCUS_ID2");

			accNoPartija = (String)ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeAccNo2");
			requestNo = (String)ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeRequestNo2");
			
			
			originOrgUniId = (java.math.BigDecimal)ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_Qbe_ORG_UNI_ID2");
			openUseId = (java.math.BigDecimal)ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_QbeUSE_OPEN_ID2");
			dateFromIn = (java.sql.Date)ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeDateFrom2");
			
			sifra = (String)ra.getAttribute("ColWorkList2QBE_LDB", "RealEstate_txtQbeCode2");
			
			if(requestNo != null){
				requestNo = requestNo.trim();
				if(requestNo.length() == 0){
					requestNo = null;
				}
			}
			
			if(accNoPartija != null){
				accNoPartija = accNoPartija.trim();
				if(accNoPartija.length() == 0){
					accNoPartija = null;
				}
			}
			
//			String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status");
			String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status_QBE");			
			
			if(procStatus.compareTo("3")!= 0 && procStatus.compareTo("4")!= 0 && procStatus.compareTo("M")!= 0){
				if((sifra == null) && (ownerCusId == null) && (carrierCusId == null) &&  (accNoPartija == null) && (dateFromIn == null) ){
					ra.showMessage("wrnclt53");
					return;
				}
			}
			if(procStatus.compareTo("3")== 0 && procStatus.compareTo("4")== 0 && procStatus.compareTo("M")== 0){
				if((sifra == null) && (ownerCusId == null) && (carrierCusId == null) && (accNoPartija == null) && (dateFromIn == null) && (originOrgUniId == null) && (openUseId == null) ){
					ra.showMessage("wrnclt53");
					return;
				}
			}
			  
			
	 		//POZIV TRANSAKCIJE QBE za kolaterale
	 		try {
	 			ra.executeTransaction();
	 			
	 		} catch (VestigoTMException vtme) {
	 			error("RealEstateDialog -> insert(): VestigoTMException", vtme);
	 			if (vtme.getMessageID() != null)
	 				ra.showMessage(vtme.getMessageID());
	 		}
	 		ra.exitScreen();
	 		
	 		
	 		
	 		
	 		
	 	}//confirm
	 
	
	
	
}
