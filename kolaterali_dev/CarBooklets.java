/*
 * Created on 2007.11.14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException; 
import hr.vestigo.framework.remote.RemoteConstants;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CarBooklets extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CarBooklets.java,v 1.8 2008/08/12 11:37:52 hramkr Exp $";
	public CarBooklets(ResourceAccessor ra) {
		super(ra);
		// TODO Auto-generated constructor stub
	}
	public void CarBooklets_SE(){    	
    	
    	if(!ra.isLDBExists("CarBookletsLDB")){
			ra.createLDB("CarBookletsLDB");
    	}
    }	
		
	public boolean CarBooklets_txtDeliverDateFrom_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateUntil");
	 	
	 	if(datumOd != null && datumDo != null){
	 		if(datumDo.before(datumOd)){
				//Datum DO ne moze biti manji od datuma OD.
		 		ra.showMessage("wrnclt30c");
				return false;
			}
	 	}
	 	
	 	
	 	return true; 
	}	
	
	public boolean CarBooklets_txtDeliverDateUntil_FV() {
		java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateUntil");
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
	
	public boolean CarBooklets_txtOrgUnit_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CarBookletsLDB";
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "CarBooklets_txtOrgUnitCode", "");                        
			ra.setAttribute(ldbName, "CarBooklets_txtOrgUnitName", "");                        
			ra.setAttribute(ldbName, "ORG_UNI_ID", null);                               
			return true;                                                                                 
	 	}   
	
	     ra.setAttribute(ldbName, "CarBooklets_txtOrgUnitName", "");
	     ra.setAttribute(ldbName, "ORG_UNI_ID", null);   	
	     
	     
			LookUpRequest lookUpRequest = new LookUpRequest("OrgUniLookUp");
			
			lookUpRequest.addMapping(ldbName, "CarBooklets_txtOrgUnitCode", "code");
			lookUpRequest.addMapping(ldbName, "CarBooklets_txtOrgUnitName", "name");
			lookUpRequest.addMapping(ldbName, "ORG_UNI_ID", "org_uni_id");
	     
		
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
	
	public boolean CarBooklets_txtClientType_FV(String ElName, Object ElValue, Integer LookUp) {
		 
		String ldbName = "CarBookletsLDB";
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute(ldbName, "CarBooklets_txtClientType", "");
			return true;
		}

		String upisano=(String) ra.getAttribute(ldbName, "CarBooklets_txtClientType");

		LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");		

		request.addMapping(ldbName, "CarBooklets_txtClientType", "Vrijednosti");

		
	
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
	

//	 da li je knjizica vozila vracena vlasniku	
	public boolean CarBooklets_txtVehLicRetOwn_FV(String ElName, Object ElValue, Integer LookUp) {
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute("CarBookletsLDB", "CarBooklets_txtVehLicRetOwn", null);
			ra.setRequired("CarBooklets_txtDeliverDateFrom",false);
			ra.setRequired("CarBooklets_txtDeliverDateUntil",false);
			return true;
		}
 
		LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");		

		request.addMapping("CarBookletsLDB", "CarBooklets_txtVehLicRetOwn", "Vrijednosti");

		try {
			ra.callLookUp(request);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;

		}
		
		String flag = (String) ra.getAttribute("CarBookletsLDB", "CarBooklets_txtVehLicRetOwn");
		
		if (flag != null && flag.equalsIgnoreCase("D")) {
// obavezno upisati datum 
			ra.setRequired("CarBooklets_txtDeliverDateFrom",true);
			ra.setRequired("CarBooklets_txtDeliverDateUntil",true);
	        ra.setContext("CarBooklets_txtDeliverDateFrom", "fld_plain");
	        ra.setContext("CarBooklets_txtDeliverDateUntil", "fld_plain");
		} else {
			ra.setRequired("CarBooklets_txtDeliverDateFrom",false);
			ra.setRequired("CarBooklets_txtDeliverDateUntil",false);	
            ra.setContext("CarBooklets_txtDeliverDateFrom", "fld_change_protected");
            ra.setContext("CarBooklets_txtDeliverDateUntil", "fld_change_protected");
		}
		
		return true;
		
	}
	public void toCSVfile() throws VestigoTMException{
		String pomString =null;
		String pomString1 =null;
     	BigDecimal pomBigDecimal=null;
     	Date pomDate=null;
     	String bankSign=null;
     	String param=null;
     	String delimiteri=null, deli=null;
     	String [] parametri = new String[8];
     	
     	boolean postoji=false;
		
		
     	SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
     	
     	if(!ra.isRequiredFilled()){
			return;
		}
	
		if(!ra.isLDBExists("BatchLogDialogLDB")){
			ra.createLDB("BatchLogDialogLDB");
		}
//		if (ra.getScreenContext().equalsIgnoreCase("base"))
			ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("1888341003"));
//		else 
//			ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("2329865003"));
		
		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
	
		
		//kupljenje parametra iz LDB-ja ekrana
		
		pomBigDecimal=(BigDecimal) ra.getAttribute("CarBookletsLDB","ORG_UNI_ID");
		if (pomBigDecimal==null || pomBigDecimal.equals("")==true){
			parametri[1]=" ";
		}else{
			parametri[1]=(String) ra.getAttribute("CarBookletsLDB","ORG_UNI_ID").toString();	
		}
				
				
		pomString=(String) ra.getAttribute("CarBookletsLDB","CarBooklets_txtClientType");
		if (pomString==null || pomString.equals("")==true){
			parametri[0]=" ";
		}else{
			parametri[0]=(String) ra.getAttribute("CarBookletsLDB","CarBooklets_txtClientType").toString();	
		}	

		pomString1=(String) ra.getAttribute("CarBookletsLDB","CarBooklets_txtVehLicRetOwn");
		if (pomString1==null || pomString1.equals("")==true){
			parametri[4]=" ";
		}else{
			parametri[4]=(String) ra.getAttribute("CarBookletsLDB","CarBooklets_txtVehLicRetOwn").toString();	
		}			

		
		pomDate=(Date) ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateFrom");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[2]=" ";
		}else{
			parametri[2]=dateF.format((Date) ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateFrom")).toString();	
		}
		
		pomDate=(Date) ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateUntil");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[3]=" ";
		}else{
			parametri[3]=dateF.format((Date) ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateUntil")).toString();	
		}		
		
		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
//		if (ra.getScreenContext().equalsIgnoreCase("base"))		
			param = parametri[0] + ";" + parametri[1] + ";" + parametri[2] + ";" + parametri[3]+ ";" + bankSign;
//		else 
//			param = parametri[0] + ";" + parametri[1] + ";" + parametri[2] + ";" + parametri[3]+ ";" + parametri[4]+ ";" + bankSign;
		//System.out.println(param);

		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		
		ra.showMessage("inf075");
		//ra.exitScreen();	
	}	
	
	public void toCSVfile2() throws VestigoTMException{
		String pomString =null;
		String pomString1 =null;
     	BigDecimal pomBigDecimal=null;
     	Date pomDate=null;
     	String bankSign=null;
     	String param=null;
     	String delimiteri=null, deli=null;
     	String [] parametri = new String[8];
     	
     	boolean postoji=false;
		
		
     	SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
     	
     	if(!ra.isRequiredFilled()){
			return;
		}
	
		if(!ra.isLDBExists("BatchLogDialogLDB")){
			ra.createLDB("BatchLogDialogLDB");
		}
//		if (ra.getScreenContext().equalsIgnoreCase("base"))
//			ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(1888341003));
//		else 
			ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("2329865003"));
		
		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
	
		
		//kupljenje parametra iz LDB-ja ekrana
		
		pomBigDecimal=(BigDecimal) ra.getAttribute("CarBookletsLDB","ORG_UNI_ID");
		if (pomBigDecimal==null || pomBigDecimal.equals("")==true){
			parametri[1]=" ";
		}else{
			parametri[1]=(String) ra.getAttribute("CarBookletsLDB","ORG_UNI_ID").toString();	
		}
				
				
		pomString=(String) ra.getAttribute("CarBookletsLDB","CarBooklets_txtClientType");
		if (pomString==null || pomString.equals("")==true){
			parametri[0]=" ";
		}else{
			parametri[0]=(String) ra.getAttribute("CarBookletsLDB","CarBooklets_txtClientType").toString();	
		}	

		pomString1=(String) ra.getAttribute("CarBookletsLDB","CarBooklets_txtVehLicRetOwn");
		if (pomString1==null || pomString1.equals("")==true){
			parametri[4]=" ";
		}else{
			parametri[4]=(String) ra.getAttribute("CarBookletsLDB","CarBooklets_txtVehLicRetOwn").toString();	
		}			

		
		pomDate=(Date) ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateFrom");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[2]=" ";
		}else{
			parametri[2]=dateF.format((Date) ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateFrom")).toString();	
		}
		
		pomDate=(Date) ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateUntil");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[3]=" ";
		}else{
			parametri[3]=dateF.format((Date) ra.getAttribute("CarBookletsLDB","CarBooklets_txtDeliverDateUntil")).toString();	
		}		
		
		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
//		if (ra.getScreenContext().equalsIgnoreCase("base"))		
//			param = parametri[0] + ";" + parametri[1] + ";" + parametri[2] + ";" + parametri[3]+ ";" + bankSign;
//		else 
			param = parametri[0] + ";" + parametri[1] + ";" + parametri[2] + ";" + parametri[3]+ ";" + parametri[4]+ ";" + bankSign;
		//System.out.println(param);

		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		
		ra.showMessage("inf075");
		//ra.exitScreen();	
	}		
}
