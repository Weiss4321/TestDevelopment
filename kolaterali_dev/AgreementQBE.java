
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;


public class AgreementQBE extends Handler {
	
	
	public static String cvsident = "";
	public AgreementQBE(ResourceAccessor ra) {
		super(ra);
	}
	public void AgreementQBE_SE() {
		
	}//AgreementQBE_SE
	
	 public boolean Agr_txtRegNoQBE_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
	 	//Korisnik ugovora
		
		String ldbName = "AgreementListLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute(ldbName,"Agr_txtRegNoQBE","");
			ra.setAttribute(ldbName,"Agr_txtCodeQBE","");
			ra.setAttribute(ldbName,"Agr_txtNameQBE","");
			ra.setAttribute(ldbName,"cus_id_qbe",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("Agr_txtNameQBE")) {
			ra.setAttribute(ldbName, "Agr_txtRegNoQBE", "");
		} else if (ra.getCursorPosition().equals("Agr_txtRegNoQBE")) {
			ra.setAttribute(ldbName, "Agr_txtNameQBE", "");
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "Agr_txtNameQBE") != null){
			d_name = (String) ra.getAttribute(ldbName, "Agr_txtNameQBE");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Agr_txtRegNoQBE") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Agr_txtRegNoQBE");
		}
		
		if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
			ra.showMessage("wrn366");
			return false;
		}
		
		//da li je zvjezdica na pravom mjestu kod register_no
		if (CharUtil.isAsteriskWrong(d_register_no)) {
			ra.showMessage("wrn367");
			return false;
		}
		
		
		 if (ra.getCursorPosition().equals("Agr_txtRegNoQBE")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "Agr_txtRegNoQBE"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "Agr_txtNameQBE"));

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
		 
		ra.setAttribute(ldbName, "cus_id_qbe", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute(ldbName, "Agr_txtRegNoQBE", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute(ldbName, "Agr_txtCodeQBE", ra.getAttribute("CustomerAllLookUpLDB", "code"));
		ra.setAttribute(ldbName, "Agr_txtNameQBE", ra.getAttribute("CustomerAllLookUpLDB", "name"));
			
		return true;


		
	}//Agr_txtRegNoQBE_FV
	 
	 
//	 akcija TRAZI na ekranu za upit po uzorku	
	 public void search() {
//	 mora biti zadan barem jedan uvjet za pretrazivanje
	 	if (checkCondition()==true){
	 		ra.performQueryByExample("tblAgreementList");
		    ra.exitScreen();	        
		}   	        
	 }  
  
	private boolean checkCondition(){
//	mora biti zadan barem jedan uvjet za pretrazivanje
//	cus_id_qbe - vlasnik ugovora-Agr_txtRegNoQBE
//	Agr_txtAgrNoQBE - broj ugovora

//	 za sve kolaterale

		if (ra.getAttribute("AgreementListLDB","cus_id_qbe") == null &&
			ra.getAttribute("AgreementListLDB","Agr_txtAgrNoQBE").equals("")) { 
			ra.showMessage("wrnclt116");
			return false;
		}

		return true;
	}    

}
