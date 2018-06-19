package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
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
 * PRIKAZ POCETNOG EKRANA VOZILA I PUNJENJE TABLICE
 */
public class Vehicle extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/Vehicle.java,v 1.16 2006/05/13 09:39:57 hrasia Exp $";
	
	public Vehicle(ResourceAccessor ra) {
		super(ra);
	}
	
	public void Vehicle_SE() {
		
		if (!ra.isLDBExists("VehicleLDB")) {
			ra.createLDB("VehicleLDB");
	 	}
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblVehicle");
        if (td == null)
        	ra.createActionListSession("tblVehicle", false);;
        td = null;
        
        
        
	}//Vehicle_SE	
	
	
	public void add(){
		//Dizanje ekrana vozila nakon definicije akcije
		ra.loadScreen("VehicleDialog","scr_change");
	}
	
	public void insu_policy(){
		ra.showMessage("wrnclt0");
	}//insu_policy
	
	
	public void hypo_fid_con(){
		java.math.BigDecimal cusId = null;
		java.math.BigDecimal collCusId = null;
		//Definira se akcija gdje se poveze ime sa ekranom i upise ime ove klase
		//Dizanje ekrana hipoteke nakon definicije akcije
		
		cusId = (java.math.BigDecimal) ra.getAttribute("VehicleLDB", "Veh_CUS_ID");
		collCusId = (java.math.BigDecimal) ra.getAttribute("VehicleLDB", "Veh_COLL_CUS_ID");
		if((cusId == null) && (collCusId == null)){
			ra.showMessage("wrnclt32");
			
		}else{
			cusId = null;
			collCusId = null;
			ra.loadScreen("CollHfPrior","base_ve");
		
		}
	}//hypo_fid_con	
	
	
	public void details(){
		
				
		if (isTableEmpty("tblVehicle")) {
			ra.showMessage("wrn299");
			return;
		}
		
		ra.loadScreen("VehicleDialog","scr_detail"); 
		
		try {
			 ra.executeTransaction();
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 
		
		
	}//details	
	
	public void revval_val(){
		ra.showMessage("wrnclt0");
	}//revval_val
	

	public void owners(){
		if (isTableEmpty("tblVehicle")) {
			ra.showMessage("wrn299");
			return;
		}
		
		ra.loadScreen("CollOwners", "scr_vehicle");
		//ra.showMessage("wrnclt0");
	}//owners
	
	public void action(){
		if (isTableEmpty("tblVehicle")) {
			ra.showMessage("wrn299");
			return;
		}
		
		ra.loadScreen("VehicleDialog","scr_update"); 
		 
		try {
			 ra.executeTransaction();
		} catch (VestigoTMException vtme) {
			 if (vtme.getMessageID() != null)
				 ra.showMessage(vtme.getMessageID());
		} 
	
		
		
	}//
	public boolean Veh_txtOwnerRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
		
		System.out.println("Vehicle    Veh_txtLoanOwnerRegisterNo_FV    1  Vlasnik vozila  Veh_COLL_CUS_ID Veh_txtOwnerRegisterNo ");
		
		
		String ldbName = "VehicleLDB";
		java.math.BigDecimal collCusId = null;
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("VehicleLDB","Veh_txtOwnerRegisterNo",null);
			ra.setAttribute("VehicleLDB","Veh_txtOwnerName",null);
			ra.setAttribute("VehicleLDB","Veh_COLL_CUS_ID",null);
			return true;
		}
		
		if (ra.getCursorPosition().equals("Veh_txtOwnerName")) {
			ra.setAttribute(ldbName, "Veh_txtOwnerRegisterNo", "");
		} else if (ra.getCursorPosition().equals("Veh_txtOwnerRegisterNo")) {
			ra.setAttribute(ldbName, "Veh_txtOwnerName", "");
			//ra.setCursorPosition(2);
		}
		
		
		
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "Veh_txtOwnerName") != null){
			d_name = (String) ra.getAttribute(ldbName, "Veh_txtOwnerName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Veh_txtOwnerRegisterNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Veh_txtOwnerRegisterNo");
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
		
		
		 if (ra.getCursorPosition().equals("Veh_txtOwnerRegisterNo")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("VehicleLDB", "Veh_txtOwnerRegisterNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("VehicleLDB", "Veh_txtOwnerName"));

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

		ra.setAttribute("VehicleLDB", "Veh_COLL_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("VehicleLDB", "Veh_txtOwnerRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("VehicleLDB", "Veh_txtOwnerName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		
		//
		
		
		collCusId = (java.math.BigDecimal) ra.getAttribute("VehicleLDB", "Veh_COLL_CUS_ID");
		
		if((collCusId != null)){
			ra.refreshActionList("tblVehicle");	
		}
		ldbName = null;
		collCusId = null;
		return true;
	}//Veh_txtOwnerRegisterNo_FV
	
	public boolean Veh_txtLoanOwnerRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
		
		System.out.println("Vehicle    Veh_txtLoanOwnerRegisterNo_FV    2  Korisnik plasmana  Veh_CUS_ID Veh_txtLoanOwnerRegisterNo ");
		
		
		String ldbName = "VehicleLDB";
		java.math.BigDecimal cusId = null;			//Korisnik plasmana 
		java.math.BigDecimal collCusId = null;		//Vlasnik vozila
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("VehicleLDB","Veh_txtLoanOwnerRegisterNo",null);
			ra.setAttribute("VehicleLDB","Veh_txtLoanOwnerName",null);
			ra.setAttribute("VehicleLDB","Veh_CUS_ID",null);
			return true;
		}
		
		if (ra.getCursorPosition().equals("Veh_txtLoanOwnerName")) {
			ra.setAttribute(ldbName, "Veh_txtLoanOwnerRegisterNo", "");
		} else if (ra.getCursorPosition().equals("Veh_txtLoanOwnerRegisterNo")) {
			ra.setAttribute(ldbName, "Veh_txtLoanOwnerName", "");
			//ra.setCursorPosition(2);
		}
		
		
		
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "Veh_txtLoanOwnerName") != null){
			d_name = (String) ra.getAttribute(ldbName, "Veh_txtLoanOwnerName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Veh_txtLoanOwnerRegisterNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Veh_txtLoanOwnerRegisterNo");
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
		
		
		 if (ra.getCursorPosition().equals("Veh_txtLoanOwnerRegisterNo")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("VehicleLDB", "Veh_txtLoanOwnerRegisterNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("VehicleLDB", "Veh_txtLoanOwnerName"));

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

		ra.setAttribute("VehicleLDB", "Veh_CUS_ID",(java.math.BigDecimal) ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("VehicleLDB", "Veh_txtLoanOwnerRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("VehicleLDB", "Veh_txtLoanOwnerName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		
		//
		
		
		cusId = (java.math.BigDecimal) ra.getAttribute("VehicleLDB", "Veh_CUS_ID");
		collCusId = (java.math.BigDecimal) ra.getAttribute("VehicleLDB", "Veh_COLL_CUS_ID");
		if((cusId == null) && (collCusId == null)){
			ra.showMessage("wrnclt2");
			return false;
		}
		
		
		if((cusId != null)){
			ra.refreshActionList("tblVehicle");	
		}
		
		ldbName = null;
		cusId = null;
		collCusId = null;
		return true;
	}//Veh_txtLoanOwnerRegisterNo_FV
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
	
	
	
	
	

	

}//Vehicle
