/*
 * Created on 2006.08.29
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
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.framework.controller.tm.VestigoTMException;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColControlListing extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ColControlListing.java,v 1.4 2006/10/18 10:54:30 hrazst Exp $";
	/**
	 * Classa koja hendla ekran ColRealEstatePrintout
	 * @param ra
	 */
		public ColControlListing(ResourceAccessor ra) {
			super(ra);
			// TODO Auto-generated constructor stub
		}
	public void ColControlListing_SE(){    	
    	String scrContext=null;
    	
    	if(!ra.isLDBExists("ColControlListingLDB")){
			ra.createLDB("ColControlListingLDB");
    	}
    	
    	scrContext = ra.getScreenContext().trim();
    	if(scrContext.compareTo("scr_archive")==0){
    		ra.setAttribute("ColControlListingLDB","COLL_STATUS","A");
    	}else if(scrContext.compareTo("scr_monitoring")==0){
    		ra.setAttribute("ColControlListingLDB","COLL_STATUS","M");
    	}
    	ra.createActionListSession("tblColControlListing",false);
    }
	
	public void search(){
		if (checkCondition()==true){
			ra.refreshActionList("tblColControlListing");
		}
	}
	
	public void toCSVfile() throws VestigoTMException{
		String pomString =null;
     	BigDecimal pomBigDecimal=null;
     	Date pomDate=null;
     	String bankSign=null;
     	String param=null;
     	String delimiteri=null, deli=null;
     	String [] parametri = new String[7];
     	
     	boolean postoji=false;
		
		int i=0, j=0;
     	
     	
     	SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
     	
     	if(!ra.isRequiredFilled()){
			return;
		}
	
		if(!ra.isLDBExists("BatchLogDialogLDB")){
			ra.createLDB("BatchLogDialogLDB");
		}

		ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(1637835003));

		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
		
		
		//kupljenje parametra iz LDB-ja ekrana
		parametri[0]=(String) ra.getAttribute("ColControlListingLDB","COLL_STATUS");
		
		pomBigDecimal=(BigDecimal) ra.getAttribute("ColControlListingLDB","REF_ID");
		if (pomBigDecimal==null || pomBigDecimal.equals("")==true){
			parametri[1]=" ";
		}else{
			parametri[1]=(String) ra.getAttribute("ColControlListingLDB","REF_ID").toString();	
		}
		
		
		pomString=(String) ra.getAttribute("ColControlListingLDB","ColControlListing_txtAccNo");
		if (pomString==null || pomString.equals("")==true){
			parametri[2]=" ";
		}else{
			parametri[2]=(String) ra.getAttribute("ColControlListingLDB","ColControlListing_txtAccNo");	
		}
				
		pomString=(String) ra.getAttribute("ColControlListingLDB","ColControlListing_txtNoRequest");
		if (pomString==null || pomString.equals("")==true){
			parametri[3]=" ";
		}else{
			parametri[3]=(String) ra.getAttribute("ColControlListingLDB","ColControlListing_txtNoRequest");	
		}
				
		pomBigDecimal=(BigDecimal) ra.getAttribute("ColControlListingLDB","OWNER_ID");
		if (pomBigDecimal==null || pomBigDecimal.equals("")==true){
			parametri[4]=" ";
		}else{
			parametri[4]=(String) ra.getAttribute("ColControlListingLDB","OWNER_ID").toString();	
		}	

		pomDate=(Date) ra.getAttribute("ColControlListingLDB","ColControlListing_txtDateFrom");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[5]=" ";
		}else{
			parametri[5]=dateF.format((Date) ra.getAttribute("ColControlListingLDB","ColControlListing_txtDateFrom")).toString();	
		}
		
		pomDate=(Date) ra.getAttribute("ColControlListingLDB","ColControlListing_txtDateUntil");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[6]=" ";
		}else{
			parametri[6]=dateF.format((Date) ra.getAttribute("ColControlListingLDB","ColControlListing_txtDateUntil")).toString();	
		}		

		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
		
		param = parametri[0] + ";" + parametri[1] + ";" + parametri[2] + ";" + parametri[3]+ ";" + parametri[4]+ ";" + parametri[5]+ ";" 
				+ parametri[6]+ ";" + bankSign;
		
		//System.out.println(param);

		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		
		ra.showMessage("inf075");
		//ra.exitScreen();	
	}
	
	public void refresh_scr(){
		if (checkCondition()==true){
			ra.refreshActionList("tblColControlListing");
		}
	}
	
	private boolean checkCondition(){
		
		if (ra.getAttribute("ColControlListing_txtAccNo").equals("") && 
			ra.getAttribute("ColControlListing_txtNoRequest").equals("") &&
			ra.getAttribute("REF_ID")==null){
			ra.showMessage("errzst1");
    		return false;
		}
		
		
		if (ra.getAttribute("REF_ID")!=null){
			if (ra.getAttribute("ColControlListing_txtDateFrom")==null){
				ra.showMessage("errzst2");
	    		return false;				
			}
		}
		
		return true;
	}
	
	public boolean ColControlListing_Owner_FV(String elementName, Object elementValue, Integer lookUpType) {
		
		String ldbName = "ColControlListingLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("ColControlListingLDB","ColControlListing_txtOwnerCode",null);
			ra.setAttribute("ColControlListingLDB","ColControlListing_txtOwnerName",null);
			ra.setAttribute("ColControlListingLDB","OWNER_ID",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("ColControlListing_txtOwnerName")) {
			ra.setAttribute(ldbName, "ColControlListing_txtOwnerCode", "");
		} else if (ra.getCursorPosition().equals("ColControlListing_txtOwnerCode")) {
			ra.setAttribute(ldbName, "ColControlListing_txtOwnerName", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "ColControlListing_txtOwnerName") != null){
			d_name = (String) ra.getAttribute(ldbName, "ColControlListing_txtOwnerName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "ColControlListing_txtOwnerCode") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "ColControlListing_txtOwnerCode");
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
		
		
		 if (ra.getCursorPosition().equals("ColControlListing_txtOwnerCode")) 
			ra.setCursorPosition(2);
		
		 if (ra.getCursorPosition().equals("ColControlListing_txtOwnerName")) 
			ra.setCursorPosition(1);
		 
		
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
	
		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("ColControlListingLDB", "ColControlListing_txtOwnerCode"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("ColControlListingLDB", "ColControlListing_txtOwnerName"));
	
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
		
		ra.setAttribute("ColControlListingLDB", "OWNER_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("ColControlListingLDB", "ColControlListing_txtOwnerCode", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("ColControlListingLDB", "ColControlListing_txtOwnerName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		if(ra.getCursorPosition().equals("ColControlListing_txtOwnerCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("ColControlListing_txtOwnerName")){
			ra.setCursorPosition(1);
		}
	
		return true;
	}
	
	public boolean ColControlListing_Ref_FV(String elementName, Object elementValue, Integer lookUpType) {

		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute("ColControlListingLDB", "ColControlListing_txtRefCode", "");                        
			ra.setAttribute("ColControlListingLDB", "ColControlListing_txtRefName", "");                        
			ra.setAttribute("ColControlListingLDB", "REF_ID", null);                               
			return true;                                                                                 
	 	}   
	 	
	 	
	 	if (ra.getCursorPosition().equals("ColControlListing_txtRefCode")) {
		     ra.setAttribute("ColControlListingLDB", "ColControlListing_txtRefName", "");
		     ra.setAttribute("ColControlListingLDB", "REF_ID", null);      
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("ColControlListing_txtRefName")) {
		     ra.setAttribute("ColControlListingLDB", "ColControlListing_txtRefCode", "");
		     ra.setAttribute("ColControlListingLDB", "REF_ID", null);      
		 }		
		
		
		if (!ra.isLDBExists("AppUserOrgLDB")) ra.createLDB("AppUserOrgLDB");
		ra.setAttribute("AppUserOrgLDB", "org_uni_id",  null);	
		ra.setAttribute("ColControlListingLDB", "dummySt", "");
		ra.setAttribute("ColControlListingLDB", "dummyBd", "");
	
	 	
		
	 	
		LookUpRequest lookUpRequest = new LookUpRequest("AppUserOrgLookUpColl");
		lookUpRequest.addMapping("ColControlListingLDB", "REF_ID", "use_id");
		lookUpRequest.addMapping("ColControlListingLDB", "ColControlListing_txtRefCode", "login");
		lookUpRequest.addMapping("ColControlListingLDB", "ColControlListing_txtRefName", "user_name");
		lookUpRequest.addMapping("ColControlListingLDB", "dummySt", "abbreviation");
		lookUpRequest.addMapping("ColControlListingLDB", "dummyBd", "org_uni_id");
		
		
		
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		if(ra.getCursorPosition().equals("ColControlListing_txtRefCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("ColControlListing_txtRefName")){
			ra.setCursorPosition(1);
		}	
		return true;

	}
	
	public boolean ColControlListing_DateFrom_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColControlListingLDB","ColControlListing_txtDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColControlListingLDB","ColControlListing_txtDateUntil");
	 	
	 	if(datumOd != null && datumDo != null){
	 		if(datumDo.before(datumOd)){
				//Datum DO ne moze biti manji od datuma OD.
		 		ra.showMessage("wrnclt30c");
				return false;
			}
	 	}
	 	
	 	
	 	return true; 
	}
	 
	public boolean ColControlListing_DateUntil_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColControlListingLDB","ColControlListing_txtDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColControlListingLDB","ColControlListing_txtDateUntil");
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

}
