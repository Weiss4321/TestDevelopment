package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.util.CharUtil;
import java.util.GregorianCalendar;
import java.sql.Date;
import java.util.Calendar;


/**  
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
   
         
   
public class KolateralQBE extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/KolateralQBE.java,v 1.19 2016/12/20 12:15:28 hrazst Exp $";

	public KolateralQBE(ResourceAccessor ra) {
		super(ra);
	} 
	
	public void KolateralQBE_SE() {

/*		if (!(ra.isLDBExists("ColWorkListLDB"))) {
			ra.createLDB("ColWorkListLDB");
		}  */
 
// procStatus odredjuje na kojoj sam listi
// brisanje svih polja za pretrazivanje		
		deleteCondition();
		String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status_QBE");	
		
		if(procStatus.compareTo("0")== 0){
//referentska lista: nema smisla pretrazivanje po org.jed unosa i referentu-zaprotektirati
			ra.setContext("Kolateral_txtOJCodeQBE", "fld_change_protected");
			ra.setContext("Kolateral_txtOJNameQBE", "fld_change_protected");
			ra.setContext("Kolateral_txtUserQBE", "fld_change_protected");
			ra.setContext("Kolateral_txtLoginQBE", "fld_change_protected");
//punim iz GDB-a
	 		ra.setAttribute("ColWorkListLDB", "org_uni_id_qbe", null);
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJCodeQBE", ra.getAttribute("GDB", "OrgUni_Code"));
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJNameQBE", "");
	 		ra.setCursorPosition("Kolateral_txtOJCodeQBE");
	 		ra.invokeValidation("Kolateral_txtOJCodeQBE");
	 		
	 		
	 		ra.setAttribute("ColWorkListLDB", "use_id_qbe", null);
	 		ra.setAttribute("ColWorkListLDB", "Kolateral_txtLoginQBE", ra.getAttribute("GDB", "Use_Login"));
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtUserQBE", "");
	 		ra.setCursorPosition("Kolateral_txtLoginQBE");
	 		ra.invokeValidation("Kolateral_txtLoginQBE");
	 		
	 		ra.setCursorPosition("Kolateral_txtColNumQBE");
	 		
			 
		} else if (procStatus.compareTo("1")== 0) {
// verifikacijska lista: jedna za org.jed. - nema smisla pretrazivanje po org.jed.
			ra.setContext("Kolateral_txtOJCodeQBE", "fld_change_protected");
			ra.setContext("Kolateral_txtOJNameQBE", "fld_change_protected");	
//			punim iz GDB-a
	 		ra.setAttribute("ColWorkListLDB", "org_uni_id_qbe", null);
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJCodeQBE", ra.getAttribute("GDB", "OrgUni_Code"));
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJNameQBE", "");
	 		ra.setCursorPosition("Kolateral_txtOJCodeQBE");
	 		ra.invokeValidation("Kolateral_txtOJCodeQBE");
	 		ra.setCursorPosition("Kolateral_txtLoginQBE");
		} else if (procStatus.compareTo("2")== 0) {
// autorizacijska lista: jedna za Banku			
			
		} else if (procStatus.compareTo("3")== 0){
// arhivska lista
					//proc_status = 3
		} else if (procStatus.compareTo("N")== 0){
//			 arhivska lista nekretnina
					//proc_status = 3			
		} else if (procStatus.compareTo("4")== 0){
// lista ponistenih kolaterala
					
		} else if(procStatus.compareTo("M")== 0){
// monitoring lista

		} else if(procStatus.compareTo("R")== 0){
//			 monitoring lista nekretnina

		} else if (procStatus.compareTo("I") == 0) {
// lista neaktivnih kolaterala			
		} else if (procStatus.compareTo("X") == 0) {
// lista neaktivnih nekretnina			
		} else if (procStatus.compareTo("F")== 0){
// lista slobodnih kolaterala
//proc_status = 3, free_status = 0
		} else if (procStatus.compareTo("S")== 0){
// lista slobodnih nekretnina
//proc_status = 3			
		} else if (procStatus.compareTo("A") == 0){
// lista svi kolaterala			
		} else if (procStatus.compareTo("K") == 0) {
// lista za KA		    
		}
		
	}

// akcija TRAZI na ekranu za upit po uzorku	
    public void search() {
// mora biti zadan barem jedan uvjet za pretrazivanje
		if (checkCondition()==true){
	        ra.performQueryByExample("tblColWorkListCellTable");
	        ra.exitScreen();
	        ra.setCursorPosition("tblColWorkListCellTable");
		}   	        
    }  
    
    /** Metoda koja brise sva polja za pretrazivanje. */
	private void deleteCondition()
    {
		ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJCodeQBE", "");
		ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJNameQBE", "");
		ra.setAttribute("ColWorkListLDB", "org_uni_id_qbe", null);
		
		ra.setAttribute("ColWorkListLDB", "Kolateral_txtLoginQBE", "");
		ra.setAttribute("ColWorkListLDB", "Kolateral_txtUserQBE", "");
		ra.setAttribute("ColWorkListLDB", "use_id_qbe", null);		
		
		ra.setAttribute("ColWorkListLDB", "Kolateral_txtColNumQBE", "");
		ra.setAttribute("ColWorkListLDB", "Kolateral_txtAccNoQBE", "");
		ra.setAttribute("ColWorkListLDB", "Kolateral_txtNoRqQBE", "");
		
		ra.setAttribute("ColWorkListLDB","Kolateral_txtOwnerQBE","");
		ra.setAttribute("ColWorkListLDB","Kolateral_txtOwnNameQBE","");
		ra.setAttribute("ColWorkListLDB","cus_id_qbe",null);
		
		ra.setAttribute("ColWorkListLDB","Kolateral_txtDateFromQBE",null);
		ra.setAttribute("ColWorkListLDB","Kolateral_txtDateUntilQBE",null);
		ra.setAttribute("ColWorkListLDB","Vehi_txtVINQBE","");
		ra.setAttribute("ColWorkListLDB","Coll_txtDepAccQBE","");
		
		ra.setAttribute("ColWorkListLDB","Kolateral_txtBrojPoliceQBE","");
		
		ra.setAttribute("ColWorkListLDB","KolQBE_txtCatCode","");
		ra.setAttribute("ColWorkListLDB","KolQBE_txtCatName","");
		ra.setAttribute("ColWorkListLDB","col_cat_id_qbe",null);
		
		ra.setAttribute("ColWorkListLDB","KolQBE_txtClientType","");
		
		ra.setAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeRegNo", "");
		ra.setAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeName", "");
		ra.setAttribute("ColWorkListLDB", "RealEstate_OwnerCUS_ID",null);
		
		
		ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeRealEstLandRegn","");
		ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeLandSub","");
		ra.setAttribute("ColWorkListLDB","RealEstate_txtCoown","");
		ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncCode", "");                        
		ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncName", "");                        
		ra.setAttribute("ColWorkListLDB", "RealEstate_QBE_CADA_MUNC", null);     
		
        ra.setAttribute("ColWorkListLDB","ISIN_txtQBE","");
        ra.setAttribute("ColWorkListLDB","Izdavatelj_txtQBE","");
        ra.setAttribute("ColWorkListLDB","IzdavateljName_txtQBE","");	
        ra.setAttribute("ColWorkListLDB","issuer_cus_id_qbe",null);
        ra.setAttribute("ColWorkListLDB","ContractNo_txtQBE","");   
        ra.setAttribute("ColWorkListLDB","GuarIzdavatelj_txtQBE","");   
        ra.setAttribute("ColWorkListLDB","guar_issuer_cus_id_qbe",null);
        ra.setAttribute("ColWorkListLDB","GuarIzdavateljName_txtQBE","");   
    }
	      
    
    
    /** Metoda koja provjerava da li su svi potrebni podaci uneseni na ekranu. */  
	private boolean checkCondition(){
//		 mora biti zadan barem jedan uvjet za pretrazivanje
// org_uni_id_qbe - OJ unosa
// use_id_qbe - referent unosa
// Kolateral_txtColNumQBE - partija kolaterala
// Kolateral_txtAccNoQBE - partija plasmana
// Kolateral_txtNoRqQBE - broj zahtjeva
// cus_id_qbe - vlasnik plasmana
// Kolateral_txtDateFromQBE,Kolateral_txtDateUntilQBE - period unosa 		
		String procStatus = (String)ra.getAttribute("ColWorkListLDB","proc_status_QBE");	
// za sve kolaterale
		
		
		
		if (ra.getScreenContext().equalsIgnoreCase("scr_arh_qbe")) {
			ra.setAttribute("ColWorkListLDB","Kolateral_txtBrojPoliceQBE",null);
			if (ra.getAttribute("ColWorkListLDB","org_uni_id_qbe") == null &&
					ra.getAttribute("ColWorkListLDB","use_id_qbe") == null &&
					ra.getAttribute("ColWorkListLDB","Kolateral_txtColNumQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB","Kolateral_txtAccNoQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB","Kolateral_txtNoRqQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB","cus_id_qbe") == null &&
					ra.getAttribute("ColWorkListLDB", "RealEstate_OwnerCUS_ID") == null &&
					ra.getAttribute("ColWorkListLDB","Kolateral_txtDateFromQBE")==null &&
					ra.getAttribute("ColWorkListLDB","Kolateral_txtDateUntilQBE")==null &&
					ra.getAttribute("ColWorkListLDB","Vehi_txtVINQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB","Coll_txtDepAccQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB","ContractNo_txtQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB", "ISIN_txtQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB", "issuer_cus_id_qbe") == null &&
					ra.getAttribute("ColWorkListLDB", "guar_issuer_cus_id_qbe") == null &&
	                ra.getAttribute("RealEstate_txtQbeRealEstLandRegn").equals("") &&
	                ra.getAttribute("RealEstate_txtQbeLandSub").equals("") &&
	                ra.getAttribute("RealEstate_txtCoown").equals("") &&
	                ra.getAttribute("RealEstate_QBE_CADA_MUNC") == null 
				) {
				ra.showMessage("wrnclt116");
				return false;
			}  
// isin + izdavatelj garancije je nedozvoljena kombinacija uvjeta
			
			if (ra.getAttribute("ColWorkListLDB", "guar_issuer_cus_id_qbe") != null && !ra.getAttribute("ColWorkListLDB", "ISIN_txtQBE").equals("")){
			    ra.showMessage("wrnclt183");
			    return false;
			}  
// izdavatelj VRP + izdavatelj garancije je nedozvoljena kombinacija uvjeta			
            if (ra.getAttribute("ColWorkListLDB", "guar_issuer_cus_id_qbe") != null && ra.getAttribute("ColWorkListLDB", "issuer_cus_id_qbe") != null){
                ra.showMessage("wrnclt184");
                return false;
// podaci iz bloka NEKRETNINE + podaci iz bloka OSTALO je nedozvoljena kombinacija                
            }  
            
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_nek_qbe")){
			ra.setAttribute("ColWorkListLDB","Kolateral_txtBrojPoliceQBE",null);
// samo za nekretnine
// RealEstate_OwnerCUS_ID - vlasnik kolaterala		
// RealEstate_txtQbeRealEstLandRegn - broj ZKU
// RealEstate_txtQbeLandSub - podulozak 
// RealEstate_txtCoown - suvlasnicki udio
// RealEstate_QBE_CADA_MUNC - katastarska opcina			
			if (ra.getAttribute("ColWorkListLDB","org_uni_id_qbe") == null &&
					ra.getAttribute("ColWorkListLDB","use_id_qbe") == null &&
					ra.getAttribute("ColWorkListLDB","Kolateral_txtColNumQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB","Kolateral_txtAccNoQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB","Kolateral_txtNoRqQBE").equals("") &&
					ra.getAttribute("ColWorkListLDB","cus_id_qbe") == null &&
					ra.getAttribute("Kolateral_txtDateFromQBE")==null &&
					ra.getAttribute("Kolateral_txtDateUntilQBE")==null &&
					ra.getAttribute("RealEstate_OwnerCUS_ID") == null &&
					ra.getAttribute("RealEstate_txtQbeRealEstLandRegn").equals("") &&
					ra.getAttribute("RealEstate_txtQbeLandSub").equals("") &&
					ra.getAttribute("RealEstate_txtCoown").equals("") &&
					ra.getAttribute("RealEstate_QBE_CADA_MUNC") == null &&
	                ra.getAttribute("ColWorkListLDB","ContractNo_txtQBE").equals("")
				) {
				ra.showMessage("wrnclt116");
				return false;
			} 			 
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_osig_qbe")) {
// pretrazivanje po broju police osiguranja	
			if (ra.getAttribute("ColWorkListLDB","Kolateral_txtBrojPoliceQBE").equals("")) {
				ra.showMessage("wrnclt116");
				return false;				
			}
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_free_deact")) {
			ra.setAttribute("ColWorkListLDB","Kolateral_txtBrojPoliceQBE",null);
			if (procStatus.compareTo("F")== 0){
// pretrazivanje oslobodjenih				
				ra.setAttribute("ColWorkListLDB","proc_status_QBE","O");
			} else if (procStatus.compareTo("I")== 0) {
// pretrazivanje deaktiviranih
				ra.setAttribute("ColWorkListLDB","proc_status_QBE","D");
			}
// pretrazivanje oslobodjenih i deaktiviranih
			Date date_from = (Date)ra.getAttribute("ColWorkListLDB", "Kolateral_txtDateFromQBE");
			Date date_until = (Date)ra.getAttribute("ColWorkListLDB", "Kolateral_txtDateUntilQBE");
			
			if(date_from == null || date_until == null)
	    	{
	    		ra.showMessage("wrnclt116");
	    		return false;
	    	}

			if(actualNoDays(date_from, date_until) > 31)
			{
				ra.showMessage("wrnhrakis01");
				return false;
			}
			 
		}
		return true;
	}    

	
// validacija OJ	
	public boolean Kolateral_txtOJCodeQBE_FV(String elementName, Object elementValue,Integer lookUpType) {
		
		
		if(elementValue == null || ((String)elementValue).equals("")){
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJCodeQBE", "");
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJNameQBE", "");
			ra.setAttribute("ColWorkListLDB", "org_uni_id_qbe", null);
			return true;
		}
		if (ra.getCursorPosition().equals("Kolateral_txtOJCodeQBE")) {
		     ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJNameQBE", "");
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("Kolateral_txtOJNameQBE")) {
		     ra.setAttribute("ColWorkListLDB", "Kolateral_txtOJCodeQBE", "");
		 }	
	
		LookUpRequest lookUpRequest = new LookUpRequest("OrgUniLookUp");
		lookUpRequest.addMapping("ColWorkListLDB","Kolateral_txtOJCodeQBE","code");
		lookUpRequest.addMapping("ColWorkListLDB","Kolateral_txtOJNameQBE","name");
		lookUpRequest.addMapping("ColWorkListLDB","org_uni_id_qbe","org_uni_id");
		
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
	  
// validacija referenta	
	 
	
	public boolean 	Kolateral_txtLoginQBE_FV  (String elementName, Object elementValue,Integer lookUpType) {

		if(elementValue == null || ((String)elementValue).equals("")){
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtLoginQBE", "");
			ra.setAttribute("ColWorkListLDB", "Kolateral_txtUserQBE", "");
			ra.setAttribute("ColWorkListLDB", "use_id_qbe", null);
			return true;
		}
		 
		if (ra.getCursorPosition().equals("Kolateral_txtLoginQBE")) {
		     ra.setAttribute("ColWorkListLDB", "Kolateral_txtUserQBE", "");
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("Kolateral_txtUserQBE")) {
		     ra.setAttribute("ColWorkListLDB", "Kolateral_txtLoginQBE", "");
		 }		
		
		
		if (!ra.isLDBExists("AppUserOrgLDB")) ra.createLDB("AppUserOrgLDB");
		ra.setAttribute("AppUserOrgLDB", "org_uni_id",  ra.getAttribute("ColWorkListLDB", "org_uni_id_qbe"));	
		System.out.println("ORG JED:"+ra.getAttribute("ColWorkListLDB", "org_uni_id_qbe"));			
		
		ra.setAttribute("ColWorkListLDB", "dummySt", "");
		ra.setAttribute("ColWorkListLDB", "dummyBD", null);

		LookUpRequest request = new LookUpRequest("AppUserOrgLookUp");
		request.addMapping("ColWorkListLDB", "use_id_qbe", "use_id");
		request.addMapping("ColWorkListLDB", "Kolateral_txtLoginQBE", "login");
		request.addMapping("ColWorkListLDB", "Kolateral_txtUserQBE", "user_name");
		request.addMapping("ColWorkListLDB", "dummySt", "abbreviation");
		request.addMapping("ColWorkListLDB", "dummyBD", "org_uni_id");
		
	
		
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
	
	
// validacija vlasnika plasmana
	 
	
	public boolean Kolateral_txtOwnerQBE_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "ColWorkListLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute(ldbName,"Kolateral_txtOwnerQBE",null);
			ra.setAttribute(ldbName,"Kolateral_txtOwnNameQBE",null);
			ra.setAttribute(ldbName,"cus_id_qbe",null);
			return true;
		}
		
		if (ra.getCursorPosition().equals("Kolateral_txtOwnNameQBE")) {
			ra.setAttribute(ldbName, "Kolateral_txtOwnerQBE", "");
		} else if (ra.getCursorPosition().equals("Kolateral_txtOwnerQBE")) {
			ra.setAttribute(ldbName, "Kolateral_txtOwnNameQBE", "");
		}
	 
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Kolateral_txtOwnerQBE") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Kolateral_txtOwnerQBE");
		}

		String d_name = "";
		if (ra.getAttribute(ldbName, "Kolateral_txtOwnNameQBE") != null){
			d_name = (String) ra.getAttribute(ldbName, "Kolateral_txtOwnNameQBE");
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
		
		   
		 if (ra.getCursorPosition().equals("Kolateral_txtOwnerQBE")) 
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
            ra.setAttribute("CustColAllLookUpLDB", "in_cus_typ_flag", "");
            ra.setAttribute("CustColAllLookUpLDB", "tax_number", ""); 
        } else {                                                                                                                         
            ra.createLDB("CustColAllLookUpLDB"); 
        }  

		ra.setAttribute("CustColAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Kolateral_txtOwnerQBE"));
		ra.setAttribute("CustColAllLookUpLDB", "name", ra.getAttribute(ldbName, "Kolateral_txtOwnNameQBE"));
		
//		LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp");
		LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_id", "cus_id");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "register_no", "register_no");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "code", "code");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "name", "name");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "add_data_table", "add_data_table");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_typ_id", "cus_typ_id");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "eco_sec", "eco_sec");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "residency_cou_id", "residency_cou_id");
	
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		
		ra.setAttribute(ldbName, "cus_id_qbe", ra.getAttribute("CustColAllLookUpLDB", "cus_id"));
		ra.setAttribute(ldbName, "Kolateral_txtOwnerQBE", ra.getAttribute("CustColAllLookUpLDB", "register_no"));
		ra.setAttribute(ldbName, "Kolateral_txtOwnNameQBE", ra.getAttribute("CustColAllLookUpLDB", "name"));			
		return true;		
	}	
	
// validacija datuma od i do

	public boolean Kolateral_txtDateFromQBE_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColWorkListLDB","Kolateral_txtDateFromQBE");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColWorkListLDB","Kolateral_txtDateUntilQBE");
	 	
	 	if(datumOd != null && datumDo != null){
	 		if(datumDo.before(datumOd)){
				//Datum DO ne moze biti manji od datuma OD.
		 		ra.showMessage("wrnclt30c");
				return false;
			}
	 	}
	 	
	 	
	 	return true; 
	}
	 
	public boolean Kolateral_txtDateUntilQBE_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColWorkListLDB","Kolateral_txtDateFromQBE");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColWorkListLDB","Kolateral_txtDateUntilQBE");
	 	if(datumOd == null && datumDo!=null){
	 	
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
	
// samo za nekretnine	
//RealEstate_CarrierQbe_FV
	public boolean RealEstate_OwnerQbe_FV(String elementName,Object elementValue, Integer lookUpType) {

		//Vlasnik kolaterala
		//RealEstate_lblOwnerCol
		//RealEstate_OwnerCUS_ID
		//RealEstate_txtRealEstateRegisterNoRE
		//RealEstate_txtRealEstateOwnerNameRE

		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeRegNo", null);
			ra.setAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeName", null);
			ra.setAttribute("ColWorkListLDB", "RealEstate_OwnerCUS_ID",null);
			return true;
		}

		if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeName")) {
			ra.setAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeRegNo", "");
		} else if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeRegNo")) {
			ra.setAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeName", "");
			//ra.setCursorPosition(2);
		}

		String d_name = "";
		if (ra.getAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeName") != null) {
			d_name = (String) ra.getAttribute("ColWorkListLDB","RealEstate_txtOwnerQbeName");
		}

		String d_register_no = "";
		if (ra.getAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeRegNo") != null) {
			d_register_no = (String) ra.getAttribute("ColWorkListLDB","RealEstate_txtOwnerQbeRegNo");
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

		if (ra.isLDBExists("CustColAllLookUpLDB")) {
			ra.setAttribute("CustColAllLookUpLDB", "cus_id", null);
			ra.setAttribute("CustColAllLookUpLDB", "register_no", "");
			ra.setAttribute("CustColAllLookUpLDB", "code", "");
			ra.setAttribute("CustColAllLookUpLDB", "name", "");
			ra.setAttribute("CustColAllLookUpLDB", "add_data_table", "");
			ra.setAttribute("CustColAllLookUpLDB", "cus_typ_id", null);
			ra.setAttribute("CustColAllLookUpLDB", "cus_sub_typ_id", null);
			ra.setAttribute("CustColAllLookUpLDB", "eco_sec", null);
			ra.setAttribute("CustColAllLookUpLDB", "residency_cou_id", null);
		} else {
			ra.createLDB("CustColAllLookUpLDB");
		}

		ra.setAttribute("CustColAllLookUpLDB", "register_no", ra.getAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeRegNo"));
		ra.setAttribute("CustColAllLookUpLDB", "name", ra.getAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeName"));

		LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_id", "cus_id");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "register_no","register_no");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "code", "code");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "name", "name");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "add_data_table","add_data_table");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_typ_id","cus_typ_id");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_sub_typ_id","cus_sub_typ_id");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "eco_sec", "eco_sec");
		lookUpRequest.addMapping("CustColAllLookUpLDB", "residency_cou_id","residency_cou_id");

		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}

		ra.setAttribute("ColWorkListLDB", "RealEstate_OwnerCUS_ID", ra.getAttribute("CustColAllLookUpLDB", "cus_id"));
		ra.setAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeRegNo", ra.getAttribute("CustColAllLookUpLDB", "register_no"));
		ra.setAttribute("ColWorkListLDB", "RealEstate_txtOwnerQbeName", ra.getAttribute("CustColAllLookUpLDB", "name"));

		if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeRegNo")) {
			ra.setCursorPosition(2);
		}
		if (ra.getCursorPosition().equals("RealEstate_txtOwnerQbeName")) {
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
			ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncCode", "");                        
			ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncName", "");                        
			ra.setAttribute("ColWorkListLDB", "RealEstate_QBE_CADA_MUNC", null);        
			setQBEfieldCtx(6); 
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
		lookUpRequest.addMapping("ColWorkListLDB", "RealEstate_QBE_CADA_MUNC", "cad_map_id");           
		lookUpRequest.addMapping("ColWorkListLDB", "RealEstate_txtCadMuncCode", "code");
		lookUpRequest.addMapping("ColWorkListLDB", "RealEstate_txtCadMuncName", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}  

		
		setQBEfieldCtx(3);
		
		return true;                                                                                   
    
	}//RealEstate_QbeCadastreMuncipality_FV     	
	
	public boolean RealEstate_txtCoown_FV(String elementName, Object elementValue, Integer lookUpType){
// SUVLASNICKI UDIO: - PODACI O SUDU I KATASTRU ZA NEKRETNINU I ADRESI NEKRETNINE
// RealEstate_txtCoown		
       if (elementValue == null || ((String) elementValue).equals("")) {
            setQBEfieldCtx(6);  
            return true;
        }    
        setQBEfieldCtx(3);	    
	return true;
	}//RealEstate_txtCoown_FV	
	public boolean RealEstate_txtQbeRealEstLandRegn_FV(String elementName, Object elementValue, Integer lookUpType){
// BROJ ZKU
       if (elementValue == null || ((String) elementValue).equals("")) {
            setQBEfieldCtx(6);  
            return true;
        }    
	    setQBEfieldCtx(3);      
	    return true;
	}
    public boolean RealEstate_txtQbeLandSub_FV(String elementName, Object elementValue, Integer lookUpType){
// PODULOZAK
        if (elementValue == null || ((String) elementValue).equals("")) {
            setQBEfieldCtx(6);  
            return true;
        }    
        setQBEfieldCtx(3);      
        return true;
    }	
    public boolean Vehi_txtVINQBE_FV(String elementName, Object elementValue, Integer lookUpType){
       if (elementValue == null || ((String) elementValue).equals("")) {
            setQBEfieldCtx(6);  
            return true;
        }          
        setQBEfieldCtx(4);       
        return true;
    }  
    public boolean Coll_txtDepAccQBE_FV(String elementName, Object elementValue, Integer lookUpType){
        if (elementValue == null || ((String) elementValue).equals("")) {
            setQBEfieldCtx(6);  
            return true;
        }       
        setQBEfieldCtx(5);    
        return true;
    }
    
    public boolean ISIN_txtQBE_FV(String elementName, Object elementValue, Integer lookUpType){
        if (elementValue == null || ((String) elementValue).equals("")) {
            setQBEfieldCtx(6);  
            return true;
        }       
        setQBEfieldCtx(2);    
        return true;
    }
    
    /** Validacijska metoda za polja s podacima o kategoriji kolaterala. */
	public boolean KolQBE_txtCatCode_FV(String elementName, Object elementValue, Integer lookUpType)
	{
        // poništi sva polja (osim trenutnog) koja su vezana za kategoriju kolaterala
    	ra.setAttribute("ColWorkListLDB", "col_cat_id_qbe", null);
        if(!elementName.equals("KolQBE_txtCatCode")) ra.setAttribute("ColWorkListLDB", "KolQBE_txtCatCode", "");
        if(!elementName.equals("KolQBE_txtCatName")) ra.setAttribute("ColWorkListLDB", "KolQBE_txtCatName", "");
        if(elementValue == null || elementValue.equals("")) return true;
 	
		// inicijaliziraj i pozovi lookup
        LookUpRequest lu = new LookUpRequest("CollCategoryLookUp");
		lu.addMapping("ColWorkListLDB", "col_cat_id_qbe", "col_cat_id");
		lu.addMapping("ColWorkListLDB", "KolQBE_txtCatCode", "code");
		lu.addMapping("ColWorkListLDB", "KolQBE_txtCatName", "name");
		return callLookUp(lu);
	}

	/** Validacijska metoda za polje s vrstom klijenta. */
	public boolean KolQBE_txtClientType_FV(String elementName, Object elementValue, Integer lookUpType)
	{
		LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");		
		request.addMapping("ColWorkListLDB", "KolQBE_txtClientType", "Vrijednosti");
		return callLookUp(request);
	}
	
	/** Metoda koja poziva zadani lookup
	 * @return da li je poziv uspješno završen
	 */
	private boolean callLookUp(LookUpRequest lu)
	{
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
	
    /**
     * @param Date1 <b>must</b> be starting date
     * @param Date2 <b>must</b> ending date
     * @return number of days between Date1 and Date2
     */
    private int actualNoDays(Date starting_date, Date end_date)
    {
		GregorianCalendar calendar1 = new GregorianCalendar();
		GregorianCalendar calendar2 = new GregorianCalendar();
		calendar1.setTime(starting_date);
		calendar2.setTime(end_date);
		int i = 0;
		while (true)
		{
		    if (calendar1.getTime().getTime() == calendar2.getTime().getTime()) break;
			calendar1.add(Calendar.DATE, 1);
			i++;
		}
		return i;
    }
    
    
  
    
    public boolean Izdavatelj_txtQBE_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = "ColWorkListLDB";
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"Izdavatelj_txtQBE",null);
            ra.setAttribute(ldbName,"IzdavateljName_txtQBE",null);
            ra.setAttribute(ldbName,"issuer_cus_id_qbe",null);
            setQBEfieldCtx(6);  
            return true;
        }
        
        if (ra.getCursorPosition().equals("IzdavateljName_txtQBE")) {
            ra.setAttribute(ldbName, "Izdavatelj_txtQBE", "");
        } else if (ra.getCursorPosition().equals("Izdavatelj_txtQBE")) {
            ra.setAttribute(ldbName, "IzdavateljName_txtQBE", "");
        }
     
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "Izdavatelj_txtQBE") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "Izdavatelj_txtQBE");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "IzdavateljName_txtQBE") != null){
            d_name = (String) ra.getAttribute(ldbName, "IzdavateljName_txtQBE");
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
        
           
         if (ra.getCursorPosition().equals("Izdavatelj_txtQBE")) 
            ra.setCursorPosition(2);
                    
        
        if (ra.isLDBExists("CustColAllLookUpLDB")) {
            ra.setAttribute("CustColAllLookUpLDB", "cus_id", null);
            ra.setAttribute("CustColAllLookUpLDB", "register_no", "");
            ra.setAttribute("CustColAllLookUpLDB", "code", "");
            ra.setAttribute("CustColAllLookUpLDB", "name", "");
            ra.setAttribute("CustColAllLookUpLDB", "add_data_table", "");
            ra.setAttribute("CustColAllLookUpLDB", "cus_typ_id", null);
            ra.setAttribute("CustColAllLookUpLDB", "cus_sub_typ_id", null);
            ra.setAttribute("CustColAllLookUpLDB", "eco_sec", null);
            ra.setAttribute("CustColAllLookUpLDB", "residency_cou_id", null);
        } else {
            ra.createLDB("CustColAllLookUpLDB");
        }

        ra.setAttribute("CustColAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Izdavatelj_txtQBE"));
        ra.setAttribute("CustColAllLookUpLDB", "name", ra.getAttribute(ldbName, "IzdavateljName_txtQBE"));
        
        LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "register_no", "register_no");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "residency_cou_id", "residency_cou_id");
    
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        
        ra.setAttribute(ldbName, "issuer_cus_id_qbe", ra.getAttribute("CustColAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "Izdavatelj_txtQBE", ra.getAttribute("CustColAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "IzdavateljName_txtQBE", ra.getAttribute("CustColAllLookUpLDB", "name"));

        setQBEfieldCtx(2);    
        return true;
 
        
    }   
    
    public boolean GuarIzdavatelj_txtQBE_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = "ColWorkListLDB";
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName,"GuarIzdavatelj_txtQBE",null);
            ra.setAttribute(ldbName,"GuarIzdavateljName_txtQBE",null);
            ra.setAttribute(ldbName,"guar_issuer_cus_id_qbe",null);
            setQBEfieldCtx(6);  
            return true;
        }
        
        if (ra.getCursorPosition().equals("GuarIzdavateljName_txtQBE")) {
            ra.setAttribute(ldbName, "GuarIzdavatelj_txtQBE", "");
        } else if (ra.getCursorPosition().equals("GuarIzdavatelj_txtQBE")) {
            ra.setAttribute(ldbName, "GuarIzdavateljName_txtQBE", "");
        }
     
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "GuarIzdavatelj_txtQBE") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "GuarIzdavatelj_txtQBE");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "GuarIzdavateljName_txtQBE") != null){
            d_name = (String) ra.getAttribute(ldbName, "GuarIzdavateljName_txtQBE");
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
        
           
         if (ra.getCursorPosition().equals("GuarIzdavatelj_txtQBE")) 
            ra.setCursorPosition(2);
                    
        
        if (ra.isLDBExists("CustColAllLookUpLDB")) {
            ra.setAttribute("CustColAllLookUpLDB", "cus_id", null);
            ra.setAttribute("CustColAllLookUpLDB", "register_no", "");
            ra.setAttribute("CustColAllLookUpLDB", "code", "");
            ra.setAttribute("CustColAllLookUpLDB", "name", "");
            ra.setAttribute("CustColAllLookUpLDB", "add_data_table", "");
            ra.setAttribute("CustColAllLookUpLDB", "cus_typ_id", null);
            ra.setAttribute("CustColAllLookUpLDB", "cus_sub_typ_id", null);
            ra.setAttribute("CustColAllLookUpLDB", "eco_sec", null);
            ra.setAttribute("CustColAllLookUpLDB", "residency_cou_id", null);
        } else {
            ra.createLDB("CustColAllLookUpLDB");
        }

        ra.setAttribute("CustColAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "GuarIzdavatelj_txtQBE"));
        ra.setAttribute("CustColAllLookUpLDB", "name", ra.getAttribute(ldbName, "GuarIzdavateljName_txtQBE"));
        
        LookUpRequest lookUpRequest = new LookUpRequest("CustColAllLookUp");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "register_no", "register_no");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustColAllLookUpLDB", "residency_cou_id", "residency_cou_id");
    
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        
        ra.setAttribute(ldbName, "guar_issuer_cus_id_qbe", ra.getAttribute("CustColAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "GuarIzdavatelj_txtQBE", ra.getAttribute("CustColAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "GuarIzdavateljName_txtQBE", ra.getAttribute("CustColAllLookUpLDB", "name"));
            
// obrisati sve iz bloka NEKRETNINE i bloka OSTALI
        setQBEfieldCtx(1);        
        
        return true;
 
        
    } 
    /**
     * metoda postavlja kontekste ovisno o dozvoljenim kombinacijama parametara
     */
    private void setQBEfieldCtx(int ind) 
    {
       if (ind == 1 ) {
// upisan izdavatelj garancije, obrisati sve u bloku NEKRETNINE i bloku OSTALI
// blok NEKRETNINE           
           ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeRealEstLandRegn","");
           ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeLandSub","");
           ra.setAttribute("ColWorkListLDB","RealEstate_txtCoown","");
           ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncCode", "");                        
           ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncName", "");                        
           ra.setAttribute("ColWorkListLDB", "RealEstate_QBE_CADA_MUNC", null);     
           ra.setContext("RealEstate_txtQbeRealEstLandRegn", "fld_protected");
           ra.setContext("RealEstate_txtQbeLandSub", "fld_protected");
           ra.setContext("RealEstate_txtCoown", "fld_protected");
           ra.setContext("RealEstate_txtCadMuncCode", "fld_protected");                     
           ra.setContext("RealEstate_txtCadMuncName", "fld_protected");
           
// blok OSTALI
           ra.setAttribute("ColWorkListLDB","ISIN_txtQBE","");
           ra.setAttribute("ColWorkListLDB","Izdavatelj_txtQBE","");
           ra.setAttribute("ColWorkListLDB","IzdavateljName_txtQBE","");   
           ra.setAttribute("ColWorkListLDB","issuer_cus_id_qbe",null);           
           ra.setAttribute("ColWorkListLDB","Vehi_txtVINQBE","");
           ra.setAttribute("ColWorkListLDB","Coll_txtDepAccQBE","");     
           ra.setContext("ISIN_txtQBE","fld_protected");
           ra.setContext("Izdavatelj_txtQBE","fld_protected");
           ra.setContext("IzdavateljName_txtQBE","fld_protected");   
           ra.setContext("Vehi_txtVINQBE","fld_protected");           
           ra.setContext("Coll_txtDepAccQBE","fld_protected");
 
       } else if (ind == 2) {
// upisan izdavatelj VRP ili ISIN
// obrisati sve u bloku NEKRETNINE i bloku OSTALI
        // blok NEKRETNINE           
           ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeRealEstLandRegn","");
           ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeLandSub","");
           ra.setAttribute("ColWorkListLDB","RealEstate_txtCoown","");
           ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncCode", "");                        
           ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncName", "");                        
           ra.setAttribute("ColWorkListLDB", "RealEstate_QBE_CADA_MUNC", null);     
           ra.setContext("RealEstate_txtQbeRealEstLandRegn", "fld_protected");
           ra.setContext("RealEstate_txtQbeLandSub", "fld_protected");
           ra.setContext("RealEstate_txtCoown", "fld_protected");
           ra.setContext("RealEstate_txtCadMuncCode", "fld_protected");                     
           ra.setContext("RealEstate_txtCadMuncName", "fld_protected");
                   
// blok OSTALI
           ra.setAttribute("ColWorkListLDB","GuarIzdavatelj_txtQBE","");   
           ra.setAttribute("ColWorkListLDB","guar_issuer_cus_id_qbe",null);
           ra.setAttribute("ColWorkListLDB","GuarIzdavateljName_txtQBE","");          
           ra.setAttribute("ColWorkListLDB","Vehi_txtVINQBE","");
           ra.setAttribute("ColWorkListLDB","Coll_txtDepAccQBE","");     
           ra.setContext("GuarIzdavatelj_txtQBE","fld_protected");
           ra.setContext("GuarIzdavateljName_txtQBE","fld_protected");
           ra.setContext("Vehi_txtVINQBE","fld_protected");   
           ra.setContext("Coll_txtDepAccQBE","fld_protected");           
            
       } else if (ind == 3) {
// upisan je barem jedan podatak u bloku NEKRETNINE, treba obrisati sve iz bloka OSTALI
           ra.setAttribute("ColWorkListLDB","GuarIzdavatelj_txtQBE","");   
           ra.setAttribute("ColWorkListLDB","guar_issuer_cus_id_qbe",null);
           ra.setAttribute("ColWorkListLDB","GuarIzdavateljName_txtQBE","");
           ra.setAttribute("ColWorkListLDB","ISIN_txtQBE","");
           ra.setAttribute("ColWorkListLDB","Izdavatelj_txtQBE","");
           ra.setAttribute("ColWorkListLDB","IzdavateljName_txtQBE","");   
           ra.setAttribute("ColWorkListLDB","issuer_cus_id_qbe",null);               
           ra.setAttribute("ColWorkListLDB","Vehi_txtVINQBE","");
           ra.setAttribute("ColWorkListLDB","Coll_txtDepAccQBE","");     
           ra.setContext("GuarIzdavatelj_txtQBE","fld_protected");
           ra.setContext("GuarIzdavateljName_txtQBE","fld_protected");
           ra.setContext("Vehi_txtVINQBE","fld_protected");   
           ra.setContext("Coll_txtDepAccQBE","fld_protected");   
           ra.setContext("ISIN_txtQBE","fld_protected");
           ra.setContext("Izdavatelj_txtQBE","fld_protected");
           ra.setContext("IzdavateljName_txtQBE","fld_protected");   
           
       } else if (ind == 4) {
// upisan je broj sasije    
// blok NEKRETNINE           
          ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeRealEstLandRegn","");
          ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeLandSub","");
          ra.setAttribute("ColWorkListLDB","RealEstate_txtCoown","");
          ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncCode", "");                        
          ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncName", "");                        
          ra.setAttribute("ColWorkListLDB", "RealEstate_QBE_CADA_MUNC", null);     
          ra.setContext("RealEstate_txtQbeRealEstLandRegn", "fld_protected");
          ra.setContext("RealEstate_txtQbeLandSub", "fld_protected");
          ra.setContext("RealEstate_txtCoown", "fld_protected");
          ra.setContext("RealEstate_txtCadMuncCode", "fld_protected");                     
          ra.setContext("RealEstate_txtCadMuncName", "fld_protected");   
// blok OSTALI     
          ra.setAttribute("ColWorkListLDB","GuarIzdavatelj_txtQBE","");   
          ra.setAttribute("ColWorkListLDB","guar_issuer_cus_id_qbe",null);
          ra.setAttribute("ColWorkListLDB","GuarIzdavateljName_txtQBE","");
          ra.setAttribute("ColWorkListLDB","ISIN_txtQBE","");
          ra.setAttribute("ColWorkListLDB","Izdavatelj_txtQBE","");
          ra.setAttribute("ColWorkListLDB","IzdavateljName_txtQBE","");   
          ra.setAttribute("ColWorkListLDB","issuer_cus_id_qbe",null);               
          ra.setAttribute("ColWorkListLDB","Coll_txtDepAccQBE","");     
          ra.setContext("GuarIzdavatelj_txtQBE","fld_protected");
          ra.setContext("GuarIzdavateljName_txtQBE","fld_protected");
          ra.setContext("Coll_txtDepAccQBE","fld_protected");   
          ra.setContext("ISIN_txtQBE","fld_protected");
          ra.setContext("Izdavatelj_txtQBE","fld_protected");
          ra.setContext("IzdavateljName_txtQBE","fld_protected");
          
       } else if (ind == 5) {
// upisan je broj depozita    
// blok NEKRETNINE           
          ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeRealEstLandRegn","");
          ra.setAttribute("ColWorkListLDB","RealEstate_txtQbeLandSub","");
          ra.setAttribute("ColWorkListLDB","RealEstate_txtCoown","");
          ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncCode", "");                        
          ra.setAttribute("ColWorkListLDB", "RealEstate_txtCadMuncName", "");                        
          ra.setAttribute("ColWorkListLDB", "RealEstate_QBE_CADA_MUNC", null);     
          ra.setContext("RealEstate_txtQbeRealEstLandRegn", "fld_protected");
          ra.setContext("RealEstate_txtQbeLandSub", "fld_protected");
          ra.setContext("RealEstate_txtCoown", "fld_protected");
          ra.setContext("RealEstate_txtCadMuncCode", "fld_protected");                     
          ra.setContext("RealEstate_txtCadMuncName", "fld_protected");   
 // blok OSTALI     
          ra.setAttribute("ColWorkListLDB","GuarIzdavatelj_txtQBE","");   
          ra.setAttribute("ColWorkListLDB","guar_issuer_cus_id_qbe",null);
          ra.setAttribute("ColWorkListLDB","GuarIzdavateljName_txtQBE","");
          ra.setAttribute("ColWorkListLDB","ISIN_txtQBE","");
          ra.setAttribute("ColWorkListLDB","Izdavatelj_txtQBE","");
          ra.setAttribute("ColWorkListLDB","IzdavateljName_txtQBE","");   
          ra.setAttribute("ColWorkListLDB","issuer_cus_id_qbe",null);               
          ra.setAttribute("ColWorkListLDB","Vehi_txtVINQBE","");
          ra.setContext("GuarIzdavatelj_txtQBE","fld_protected");
          ra.setContext("GuarIzdavateljName_txtQBE","fld_protected");
          ra.setContext("Vehi_txtVINQBE","fld_protected");   
          ra.setContext("ISIN_txtQBE","fld_protected");
          ra.setContext("Izdavatelj_txtQBE","fld_protected");
          ra.setContext("IzdavateljName_txtQBE","fld_protected");       
       } else if (ind == 6) {
// obrisano polje broj sasije
           ra.setContext("RealEstate_txtQbeRealEstLandRegn", "fld_plain");
           ra.setContext("RealEstate_txtQbeLandSub", "fld_plain");
           ra.setContext("RealEstate_txtCoown", "fld_plain");
           ra.setContext("RealEstate_txtCadMuncCode", "fld_plain");                     
           ra.setContext("RealEstate_txtCadMuncName", "fld_plain");  
           ra.setContext("GuarIzdavatelj_txtQBE","fld_plain");
           ra.setContext("GuarIzdavateljName_txtQBE","fld_plain");
           ra.setContext("Vehi_txtVINQBE","fld_plain");   
           ra.setContext("Coll_txtDepAccQBE","fld_plain");   
           ra.setContext("ISIN_txtQBE","fld_plain");
           ra.setContext("Izdavatelj_txtQBE","fld_plain");
           ra.setContext("IzdavateljName_txtQBE","fld_plain");           
       }
    }    
}  

                      
 