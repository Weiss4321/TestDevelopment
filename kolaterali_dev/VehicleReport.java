/*
 * Created on 2007.11.26
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
public class VehicleReport extends Handler {
	
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/VehicleReport.java,v 1.1 2007/11/26 09:38:54 hratar Exp $";
	
	public VehicleReport(ResourceAccessor ra) {
		super(ra);
	}
	
	public void CollInvestParties_SE() {
    	
    	if(!ra.isLDBExists("VehicleReportLDB")){
			ra.createLDB("VehicleReportLDB");
    	}
	}
	
	public boolean VehicleReport_txtDateFrom_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("VehicleReportLDB","VehicleReport_txtRightOfBankDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("VehicleReportLDB","VehicleReport_txtRightOfBankDateUntil");
	 	
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
		java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("VehicleReportLDB","VehicleReport_txtRightOfBankDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("VehicleReportLDB","VehicleReport_txtRightOfBankDateUntil");
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

	
	public void toCSVfile() throws VestigoTMException{
		String pomString =null;
     	BigDecimal pomBigDecimal=null;
     	Date pomDate=null;
     	String bankSign=null;
     	String param=null;
     	String delimiteri=null, deli=null;
     	String [] parametri = new String[3];
     	
     	boolean postoji=false;
		
		int i=0, j=0;
     	
     	System.out.println("--> VehicleReport toCSVfile() metoda.");
		
     	SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
     	
 
	
		if(!ra.isLDBExists("BatchLogDialogLDB")){
			ra.createLDB("BatchLogDialogLDB");
		}

		ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(1888341003));

		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
	
		
		pomDate=(Date) ra.getAttribute("VehicleReportLDB","NoticeByPolicy_txtDateFrom");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[0]=" ";
		}else{
			parametri[0]=dateF.format((Date) ra.getAttribute("VehicleReportLDB","NoticeByPolicy_txtDateFrom")).toString();	
		}
		
		pomDate=(Date) ra.getAttribute("VehicleReportLDB","NoticeByPolicy_txtDateUntil");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[1]=" ";
		}else{
			parametri[1]=dateF.format((Date) ra.getAttribute("VehicleReportLDB","NoticeByPolicy_txtDateUntil")).toString();	
		}		

		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
		
		param = parametri[0] + ";" + parametri[1] + ";" + bankSign;	
		//System.out.println(param);

		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		
		ra.showMessage("inf075");
		//ra.exitScreen();	
	}	
}
