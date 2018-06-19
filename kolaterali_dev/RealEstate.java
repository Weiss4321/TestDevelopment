package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.common.VestigoTableException;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;

/**
 * @author HRASIA
 *
 * PRIKAZ POCETNOG EKRANA NEKRETNINA I PUNJENJE TABLICE
 */
public class RealEstate extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RealEstate.java,v 1.42 2014/10/07 10:54:19 hrazst Exp $";
		
	//1.39 staro
	public RealEstate(ResourceAccessor ra) {
		super(ra);
	}
	
	public void RealEstate_SE() {		
		if (!ra.isLDBExists("RealEstateLDB")) {
	 		ra.createLDB("RealEstateLDB");
	 	}
	}//RealEstate_SE	
		
	public void add(){
		//Dizanje ekrana nekretnine nakon definicije akcije
		ra.loadScreen("RealEstateDialog","scr_change");
	}
		
	public void insu_policy(){
		if (isTableEmpty("tblRealEstate")) {
			ra.showMessage("wrn299");
			return;
		}
		
		 ra.loadScreen("InsuPolicy", "scr_realest");
		//ra.showMessage("wrnclt0");
	}//insu_policy
		
	public void hypo_fid_con(){
		BigDecimal cusId = null;
		BigDecimal collCusId = null;
		//Definira se akcija gdje se poveze ime sa ekranom i upise ime ove klase
		//Dizanje ekrana hipoteke nakon definicije akcije
		
		//cusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "CUS_ID");
		//collCusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "COLL_CUS_ID");
		//if((cusId == null) && (collCusId == null)){
		//	ra.showMessage("wrnclt2");
		//	
		//}else{
		//	cusId = null;
		//	collCusId = null;
		//	ra.loadScreen("CollHfPrior","base_re");
		//
		//}
		
		if (isTableEmpty("tblRealEstate")) {
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("CollHfPrior","base_re");		
	}//hypo_fid_con
		
	public void details(){		
		if (isTableEmpty("tblRealEstate")) {
			ra.showMessage("wrn299");
			return;
		}
			
		ra.loadScreen("RealEstateDialog","scr_detail"); 
		try {
			 ra.executeTransaction();
			 System.out.println("tekst  izmedju je : |"+ra.getAttribute("RealEstateDialogLDB","RealEstate_txtEligDesc")+"|");			 
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 
	}//details

	public void action(){
			
		if (isTableEmpty("tblRealEstate")) {
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("RealEstateDialog","scr_update"); 
		try {
			 ra.executeTransaction();
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 		
		//ra.showMessage("wrnclt0");
	}	
		
	public void revval_val(){
		BigDecimal cusId = null;
		BigDecimal collCusId = null;
		//Definira se akcija gdje se poveze ime sa ekranom i upise ime ove klase
		//Dizanje ekrana revalorizacije nakon definicije akcije
		

		cusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "CUS_ID");
		collCusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "COLL_CUS_ID");
		if((cusId == null) && (collCusId == null)){
			ra.showMessage("wrnclt2");
			
		}else{
			cusId = null;
			collCusId = null;
			ra.showMessage("wrnclt0");
			//ra.loadScreen("CollHfPrior","base_re");		
		}		
	}	
	
	public void owners(){
    	if (isTableEmpty("tblRealEstate")) {
    		ra.showMessage("wrn299");
    		return;
    	}		
    	 ra.loadScreen("CollOwners", "scr_realest");
    	//ra.showMessage("wrnclt0");
	}		
		
	public boolean RealEstate_Customer_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "RealEstateLDB";
		java.math.BigDecimal collCusId = null;
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("RealEstateLDB","RealEstate_txtRealEstateRegisterNoRE",null);
			ra.setAttribute("RealEstateLDB","RealEstate_txtRealEstateOwnerNameRE",null);
			ra.setAttribute("RealEstateLDB","COLL_CUS_ID",null);
			
			ra.setAttribute("RealEstateLDB", "RE_COL_RES_ID",null);
			ra.refreshActionList("tblRealEstate");			
			return true;
		}
		
		if (ra.getCursorPosition().equals("RealEstate_txtRealEstateOwnerNameRE")) {
			ra.setAttribute(ldbName, "RealEstate_txtRealEstateRegisterNoRE", "");
		} else if (ra.getCursorPosition().equals("RealEstate_txtRealEstateRegisterNoRE")) {
			ra.setAttribute(ldbName, "RealEstate_txtRealEstateOwnerNameRE", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtRealEstateOwnerNameRE") != null){
			d_name = (String) ra.getAttribute(ldbName, "RealEstate_txtRealEstateOwnerNameRE");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtRealEstateRegisterNoRE") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "RealEstate_txtRealEstateRegisterNoRE");
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
		
		if (ra.getCursorPosition().equals("RealEstate_txtRealEstateRegisterNoRE")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("RealEstateLDB", "RealEstate_txtRealEstateRegisterNoRE"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("RealEstateLDB", "RealEstate_txtRealEstateOwnerNameRE"));

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

		ra.setAttribute("RealEstateLDB", "COLL_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtRealEstateRegisterNoRE", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtRealEstateOwnerNameRE", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		
		ra.setAttribute("RealEstateLDB", "RE_COL_RES_ID",null);
		BigDecimal cusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "CUS_ID");
		collCusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "COLL_CUS_ID");
		BigDecimal revReId = (java.math.BigDecimal)ra.getAttribute("RealEstateLDB", "REV_RE_ID"); 
		
		ra.refreshActionList("tblRealEstate");
		
		ldbName = null;
		collCusId = null;
		cusId  = null;
		revReId = null;
		return true;
	}
		
	public boolean RealEstate_Carrier_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "RealEstateLDB";
		java.math.BigDecimal cusId = null;
		java.math.BigDecimal collCusId = null;
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("RealEstateLDB","RealEstate_txtCarrierRegisterNoRE",null);
			ra.setAttribute("RealEstateLDB","RealEstate_txtCarrierNameRE",null);
			ra.setAttribute("RealEstateLDB","CUS_ID",null);
			ra.setAttribute("RealEstateLDB", "RE_COL_RES_ID",null);
			ra.refreshActionList("tblRealEstate");
			return true;
		}
		
		if (ra.getCursorPosition().equals("RealEstate_txtCarrierNameRE")) {
			ra.setAttribute(ldbName, "RealEstate_txtCarrierRegisterNoRE", "");
		} else if (ra.getCursorPosition().equals("RealEstate_txtCarrierRegisterNoRE")) {
			ra.setAttribute(ldbName, "RealEstate_txtCarrierNameRE", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtCarrierNameRE") != null){
			d_name = (String) ra.getAttribute(ldbName, "RealEstate_txtCarrierNameRE");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "RealEstate_txtCarrierRegisterNoRE") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "RealEstate_txtCarrierRegisterNoRE");
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
		
		if (ra.getCursorPosition().equals("RealEstate_txtCarrierRegisterNoRE")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("RealEstateLDB", "RealEstate_txtCarrierRegisterNoRE"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("RealEstateLDB", "RealEstate_txtCarrierNameRE"));

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

		ra.setAttribute("RealEstateLDB", "CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtCarrierRegisterNoRE", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtCarrierNameRE", ra.getAttribute("CustomerAllLookUpLDB", "name"));
					
		ra.setAttribute("RealEstateLDB", "RE_COL_RES_ID",null);
		cusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "CUS_ID");
		collCusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "COLL_CUS_ID");
		BigDecimal revReId = (java.math.BigDecimal)ra.getAttribute("RealEstateLDB", "REV_RE_ID"); 
		
		ra.refreshActionList("tblRealEstate");		
		
		ldbName = null;
		cusId = null;
		collCusId = null;
		revReId = null;
		return true;
	}//RealEstate_Carrier_FV
		
	public boolean RealEstate_RevRegCoefRe_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "RealEstateLDB";
		java.math.BigDecimal cusId = null;
		java.math.BigDecimal collCusId = null;
		java.math.BigDecimal  revReId = null;
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("RealEstateLDB","RealEstate_txtRevReCodeRE",null);
			ra.setAttribute("RealEstateLDB","RealEstate_txtRevReNameRE",null);
			ra.setAttribute("RealEstateLDB","REV_RE_ID",null);
			ra.setAttribute("RealEstateLDB", "RealEstate_txtCountyNameRE", null);
			ra.setAttribute("RealEstateLDB", "RealEstate_txtCountyCodeRE", null);
			ra.setAttribute("RealEstateLDB", "RealEstate_txtPlaceNameRE", null);
			ra.setAttribute("RealEstateLDB", "RealEstate_txtPlaceCodeRE", null);
			ra.setAttribute("RealEstateLDB", "RealEstate_txtDistrictNameRE", null);
			ra.setAttribute("RealEstateLDB", "RealEstate_txtDistrictCodeRE", null);
			ra.setAttribute("RealEstateLDB", "RealEstate_txtResiQuarNameRE", null);		
			ra.setAttribute("RealEstateLDB", "RealEstate_txtResiQuarCodeRE", null);
			
			ra.setAttribute("RealEstateLDB", "RE_COL_RES_ID",null);
			ra.refreshActionList("tblRealEstate");
			return true;
		}
		
		if (ra.getCursorPosition().equals("RealEstate_txtRevReNameRE")) {
			ra.setAttribute(ldbName, "RealEstate_txtRevReCodeRE", "");
			
		} else if (ra.getCursorPosition().equals("RealEstate_txtRevReCodeRE")) {
			ra.setAttribute(ldbName, "RealEstate_txtRevReNameRE", "");			
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

		ra.setAttribute("RevRegCoefReLookUpLDB", "rev_re_code", ra.getAttribute("RealEstateLDB", "RealEstate_txtRevReCodeRE"));
		ra.setAttribute("RevRegCoefReLookUpLDB", "rev_re_name", ra.getAttribute("RealEstateLDB", "RealEstate_txtRevReNameRE"));
		
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
 
		ra.setAttribute("RealEstateLDB", "REV_RE_ID", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_re_id"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtRevReCodeRE", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_re_code"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtRevReNameRE", ra.getAttribute("RevRegCoefReLookUpLDB", "rev_re_name"));
		
		ra.setAttribute("RealEstateLDB", "RealEstate_txtCountyNameRE", ra.getAttribute("RevRegCoefReLookUpLDB", "county_name"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtCountyCodeRE", ra.getAttribute("RevRegCoefReLookUpLDB", "county_code"));
		
		ra.setAttribute("RealEstateLDB", "RealEstate_txtPlaceNameRE", ra.getAttribute("RevRegCoefReLookUpLDB", "place_name"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtPlaceCodeRE", ra.getAttribute("RevRegCoefReLookUpLDB", "place_code"));
			
		ra.setAttribute("RealEstateLDB", "RealEstate_txtDistrictNameRE", ra.getAttribute("RevRegCoefReLookUpLDB", "district_name"));
		ra.setAttribute("RealEstateLDB", "RealEstate_txtDistrictCodeRE", ra.getAttribute("RevRegCoefReLookUpLDB", "district_code"));
			
		ra.setAttribute("RealEstateLDB", "RealEstate_txtResiQuarNameRE", ra.getAttribute("RevRegCoefReLookUpLDB", "resi_quar_name"));		
		ra.setAttribute("RealEstateLDB", "RealEstate_txtResiQuarCodeRE", ra.getAttribute("RevRegCoefReLookUpLDB", "resi_quar_code"));
		
		ra.setAttribute("RealEstateLDB", "RE_COL_RES_ID",null);
		
		cusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "CUS_ID");
		collCusId = (java.math.BigDecimal) ra.getAttribute("RealEstateLDB", "COLL_CUS_ID");
		revReId = (java.math.BigDecimal)ra.getAttribute("RealEstateLDB", "REV_RE_ID");
		
		ra.refreshActionList("tblRealEstate");	
		
		ldbName = null;
		cusId = null;
		collCusId = null;
		revReId = null;
		return true;
	}
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty	
}
