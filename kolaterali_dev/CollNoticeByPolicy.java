/*
 * Created on 2007.01.17
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



public class CollNoticeByPolicy  extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollNoticeByPolicy.java,v 1.14 2007/04/12 14:03:19 hratar Exp $";
	public CollNoticeByPolicy(ResourceAccessor ra) {
		super(ra);
		// TODO Auto-generated constructor stub
	}
	public void CollNoticeByPolicy_SE(){    	
    	String scrContext=null;
    	
    	if(!ra.isLDBExists("CollNoticeByPolicyLDB")){
			ra.createLDB("CollNoticeByPolicyLDB");
    	}

    	//ra.createActionListSession("tblColControlListing",false);
    }	
		
	public boolean NoticeByPolicy_txtDateFrom_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtDateUntil");
	 	
	 	if(datumOd != null && datumDo != null){
	 		if(datumDo.before(datumOd)){
				//Datum DO ne moze biti manji od datuma OD.
		 		ra.showMessage("wrnclt30c");
				return false;
			}
	 	}
	 	
	 	
	 	return true; 
	}	
	
	public boolean NoticeByPolicy_txtDateUntil_FV() {
		java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtDateUntil");
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
	
	public boolean NoticeByPolicy_txtEnsurer_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollNoticeByPolicyLDB";
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "NoticeByPolicy_txtEnsurerCode", "");                        
			ra.setAttribute(ldbName, "NoticeByPolicy_txtEnsurerName", "");                        
			ra.setAttribute(ldbName, "IC_ID", null);  
			return true;                                                                                 
	 	}   
		ra.setAttribute(ldbName, "dummy", "");
		ra.setAttribute(ldbName, "NoticeByPolicy_txtEnsurerName", "");
	    ra.setAttribute(ldbName, "IC_ID", null);   	
	     
	    LookUpRequest lookUpRequest = new LookUpRequest("InsuCompanyLookup");
	    
	    lookUpRequest.addMapping(ldbName, "IC_ID", "ic_id");
	    lookUpRequest.addMapping(ldbName, "dummy", "ic_register_no");
	    lookUpRequest.addMapping(ldbName, "NoticeByPolicy_txtEnsurerName", "ic_name");
	    lookUpRequest.addMapping(ldbName, "NoticeByPolicy_txtEnsurerCode", "ic_code");
		

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
	public boolean NoticeByPolicy_txtPolicyType_FV(String elementName, Object elementValue, Integer lookUpType) {
		
		
		String ldbName = "CollNoticeByPolicyLDB";

		if (elementValue == null || ((String) elementValue).equals("")) {                                          
			ra.setAttribute(ldbName, "NoticeByPolicy_txtPolicyTypeName", "");   
			ra.setAttribute(ldbName, "NoticeByPolicy_txtPolicyTypeCode", ""); 
        
			return true;                                                                                 
		}

		ra.setAttribute(ldbName, "NoticeByPolicy_txtPolicyTypeName", "");
		ra.setAttribute(ldbName, "SysCodId", "ins_pol_typ");
		ra.setAttribute(ldbName, "dummy", null);
		 
		LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
 
		request.addMapping(ldbName, "NoticeByPolicy_txtPolicyTypeCode", "sys_code_value");
		request.addMapping(ldbName, "NoticeByPolicy_txtPolicyTypeName", "sys_code_desc");
		request.addMapping(ldbName, "dummy", "sys_cod_val_id");

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
	public boolean NoticeByPolicy_txtPolicyState_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollNoticeByPolicyLDB";

		if (elementValue == null || ((String) elementValue).equals("")) {                                          
			ra.setAttribute(ldbName, "NoticeByPolicy_txtPolicyStateName", "");   
			ra.setAttribute(ldbName, "NoticeByPolicy_txtPolicyStateCode", ""); 
        
			return true;                                                                                 
		}

		ra.setAttribute(ldbName, "NoticeByPolicy_txtPolicyStateName", "");
		ra.setAttribute(ldbName, "SysCodId", "clt_inspolst");
		ra.setAttribute(ldbName, "dummy", null);
		 
		LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");
 
		request.addMapping(ldbName, "NoticeByPolicy_txtPolicyStateCode", "sys_code_value");
		request.addMapping(ldbName, "NoticeByPolicy_txtPolicyStateName", "sys_code_desc");
		request.addMapping(ldbName, "dummy", "sys_cod_val_id");

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
	
	public boolean NoticeByPolicy_txtOrgUnit_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollNoticeByPolicyLDB";
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "NoticeByPolicy_txtOrgUnitCode", "");                        
			ra.setAttribute(ldbName, "NoticeByPolicy_txtOrgUnitName", "");                        
			ra.setAttribute(ldbName, "ORG_UNI_ID", null);                               
			return true;                                                                                 
	 	}   
	
	     ra.setAttribute(ldbName, "NoticeByPolicy_txtOrgUnitName", "");
	     ra.setAttribute(ldbName, "ORG_UNI_ID", null);   	
	     
	     
			LookUpRequest lookUpRequest = new LookUpRequest("OrgUniLookUp");
			
			lookUpRequest.addMapping(ldbName, "NoticeByPolicy_txtOrgUnitCode", "code");
			lookUpRequest.addMapping(ldbName, "NoticeByPolicy_txtOrgUnitName", "name");
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
	
	public boolean NoticeByPolicy_txtClientType_FV(String ElName, Object ElValue, Integer LookUp) {
		 
		String ldbName = "CollNoticeByPolicyLDB";
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute(ldbName, "NoticeByPolicy_txtClientType", "");
			return true;
		}

		String upisano=(String) ra.getAttribute(ldbName, "NoticeByPolicy_txtClientType");

		LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");		

		request.addMapping(ldbName, "NoticeByPolicy_txtClientType", "Vrijednosti");

		
	
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
	public void search(){
		
	}
	public void refresh_scr(){

	}
	public void toCSVfile() throws VestigoTMException{
		String pomString =null;
     	BigDecimal pomBigDecimal=null;
     	Date pomDate=null;
     	String bankSign=null;
     	String param=null;
     	String delimiteri=null, deli=null;
     	String [] parametri = new String[8];
     	
     	boolean postoji=false;
		
		int i=0, j=0;
     	
     	System.out.println("---> nova verzija");
		
     	SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
     	
     	if(!ra.isRequiredFilled()){
			return;
		}
	
		if(!ra.isLDBExists("BatchLogDialogLDB")){
			ra.createLDB("BatchLogDialogLDB");
		}

		ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(1686830003));

		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
	
		
		//kupljenje parametra iz LDB-ja ekrana
		
		pomBigDecimal=(BigDecimal) ra.getAttribute("CollNoticeByPolicyLDB","ORG_UNI_ID");
		if (pomBigDecimal==null || pomBigDecimal.equals("")==true){
			parametri[2]=" ";
		}else{
			parametri[2]=(String) ra.getAttribute("CollNoticeByPolicyLDB","ORG_UNI_ID").toString();	
		}
				
				
		pomString=(String) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtPolicyTypeCode");
		if (pomString==null || pomString.equals("")==true){
			parametri[3]=" ";
		}else{
			parametri[3]=(String) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtPolicyTypeCode").toString();	
		}	

		
		pomBigDecimal=(BigDecimal) ra.getAttribute("CollNoticeByPolicyLDB","IC_ID");
		if (pomBigDecimal==null || pomBigDecimal.equals("")==true){
			parametri[4]=" ";
		}else{
			parametri[4]=(String) ra.getAttribute("CollNoticeByPolicyLDB","IC_ID").toString();	
		}	

		
		pomDate=(Date) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtDateFrom");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[0]=" ";
		}else{
			parametri[0]=dateF.format((Date) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtDateFrom")).toString();	
		}
		
		pomDate=(Date) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtDateUntil");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[1]=" ";
		}else{
			parametri[1]=dateF.format((Date) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtDateUntil")).toString();	
		}		

		pomString=(String) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtPolicyStateCode");
		if (pomString==null || pomString.equals("")==true){
			parametri[5]=" ";
		}else{
			parametri[5]=(String) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtPolicyStateCode").toString();	
		}	
		
		pomString=(String) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtClientType");
		if (pomString==null || pomString.equals("")==true){
			parametri[6]=" ";
		}else{
			parametri[6]=(String) ra.getAttribute("CollNoticeByPolicyLDB","NoticeByPolicy_txtClientType").toString();	
		}
		
		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
		
		param = parametri[0] + ";" + parametri[1] + ";" + parametri[2] + ";" + parametri[3]+ ";" + parametri[4] + ";" + parametri[5] + ";" + parametri[6]+ ";" + bankSign;	
		//System.out.println(param);

		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		
		ra.showMessage("inf075");
		//ra.exitScreen();	
	}	
}
