/*
 * Created on 2006.08.17
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
import hr.vestigo.framework.util.CharUtil;


/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColRealEstatePrintout extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ColRealEstatePrintout.java,v 1.3 2006/10/24 09:01:41 hrazst Exp $";
	/**
	 * Classa koja hendla ekran ColRealEstatePrintout
	 * @param ra
	 */
	
	
	public ColRealEstatePrintout(ResourceAccessor ra){
    	super(ra);
    }  

    public void ColRealEstatePrintout_SE(){    	
    	String scrContext=null;
    	
    	if(!ra.isLDBExists("ColRealEstatePrintoutLDB")){
			ra.createLDB("ColRealEstatePrintoutLDB");
    	}
    	
    	scrContext = ra.getScreenContext().trim();
    	if (scrContext.compareTo("scr_RealEstateRef")==0){
    		ra.setAttribute("ColRealEstatePrintoutLDB","COLL_STATUS","R");
    		ra.setAttribute("ColRealEstatePrintoutLDB","USE_ID",ra.getAttribute("GDB","use_id"));
    		ra.setAttribute("ColRealEstatePrintoutLDB","OU_ID",ra.getAttribute("GDB","org_uni_id"));
    	}else if(scrContext.compareTo("scr_RealEstateArc")==0){
    		ra.setAttribute("ColRealEstatePrintoutLDB","COLL_STATUS","A");
    	}else if(scrContext.compareTo("scr_RealEstateMon")==0){
    		ra.setAttribute("ColRealEstatePrintoutLDB","COLL_STATUS","M");
    	}
    	ra.setAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCatCode","NEKR");
    	ra.setAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCatName","Nekretnine");
    	ra.setAttribute("ColRealEstatePrintoutLDB","COL_CAT_ID",null);
    	
    	ra.setContext("ColRealEstatePrintout_txtCatCode", "fld_protected");
    	ra.setContext("ColRealEstatePrintout_txtCatName", "fld_protected");
    }
    
     public void order_batch() throws VestigoTMException{
     	String pomString =null;
     	BigDecimal pomBigDecimal=null;
     	Date pomDate=null;
     	String bankSign=null;
     	String param=null;
     	String delimiteri=null, deli=null;
     	String [] parametri = new String[15];
     	
     	boolean postoji=false;
		
		int i=0, j=0;
     	
     	
     	SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
     	
     	if(!ra.isRequiredFilled()){
			return;
		}
	
		if(!ra.isLDBExists("BatchLogDialogLDB")){
			ra.createLDB("BatchLogDialogLDB");
		}

		ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(1635499003));

		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
		
		
		//kupljenje parametra iz LDB-ja ekrana
		parametri[0]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","COLL_STATUS");
		
		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCatCode");
		if (pomString==null){
			parametri[1]="";
		}else{
			parametri[1]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCatCode");	
		}
		
		
		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtTypeCode");
		if (pomString==null){
			parametri[2]="";
		}else{
			parametri[2]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtTypeCode");	
		}
		
		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCode");
		if (pomString==null){
			parametri[3]="";
		}else{
			parametri[3]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCode");	
		}
		
		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtOwnerCode");
		if (pomString==null){
			parametri[4]="";
		}else{
			parametri[4]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtOwnerCode");	
		}	
		
		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtBenCode");
		if (pomString==null){
			parametri[5]="";
		}else{
			parametri[5]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtBenCode");	
		}			

		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtLandRegn");
		if (pomString==null){
			parametri[6]="";
		}else{
			parametri[6]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtLandRegn");	
		}

		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtLandSub");
		if (pomString==null){
			parametri[7]="";
		}else{
			parametri[7]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtLandSub");	
		}		

		pomBigDecimal=(BigDecimal) ra.getAttribute("ColRealEstatePrintoutLDB","CADA_ID");
		if (pomBigDecimal==null){
			parametri[8]="";
		}else{
			parametri[8]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","CADA_ID").toString();	
		}
		
		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCoown");
		if (pomString==null){
			parametri[9]="";
		}else{
			parametri[9]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCoown");	
		}	
		
		pomString=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtAccNo");
		if (pomString==null){
			parametri[10]="";
		}else{
			parametri[10]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtAccNo");	
		}	
		
		pomBigDecimal=(BigDecimal) ra.getAttribute("ColRealEstatePrintoutLDB","OU_ID");
		if (pomBigDecimal==null){
			parametri[11]="";
		}else{
			parametri[11]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","OU_ID").toString();	
		}

		pomBigDecimal=(BigDecimal) ra.getAttribute("ColRealEstatePrintoutLDB","USE_ID");
		if (pomBigDecimal==null){
			parametri[12]="";
		}else{
			parametri[12]=(String) ra.getAttribute("ColRealEstatePrintoutLDB","USE_ID").toString();	
		}

		pomDate=(Date) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtDateFrom");
		if (pomDate==null){
			parametri[13]="";
		}else{
			parametri[13]=dateF.format((Date) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtDateFrom")).toString();	
		}
		
		pomDate=(Date) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtDateUntil");
		if (pomDate==null){
			parametri[14]="";
		}else{
			parametri[14]=dateF.format((Date) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtDateUntil")).toString();	
		}		

		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
		
		//jedan od ovih znakova je delimiter(program sam odredi koji i posalje ga kao prvi parametar parametar)
		delimiteri="#:?%&'=_";

		for(i=0;i<delimiteri.length();i++){
			postoji=false;
			for(j=0;j<parametri.length;j++){
				if(parametri[j].indexOf(delimiteri.charAt(i))!=-1){
					postoji=true;
					break;
				}
			}
			if (postoji==false) break;
		}
		deli=delimiteri.substring(i,i+1);
	
		pomString="";
		for(j=0;j<parametri.length;j++){
			if (j!=parametri.length-1){
				pomString=pomString + parametri[j] + deli;
			}else{
				pomString=pomString + parametri[j];
			}
			
		}
		param=deli + ";" + pomString + ";" + bankSign;

		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		
		ra.showMessage("inf075");
		ra.exitScreen();
     }
   	
     
     public boolean ColRealEstatePrintout_CollCat_FV(){
     
     	return true;
     }
     
     public boolean ColRealEstatePrintout_CollType_FV(String ElName, Object ElValue, Integer LookUp){
		// TIP KOLATERALA: - SIFRA, VRSTA i VLASNIK	
		// RealEstate_COL_TYPE_ID							
		// RealEstate_txtCollTypeCode
		// RealEstate_txtCollTypeName
		ra.setAttribute("ColRealEstatePrintoutLDB", "dummySt", "");
		ra.setAttribute("ColRealEstatePrintoutLDB", "dummyDate", null);
		
		if (ElValue == null || ((String) ElValue).equals("")) {
				ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtTypeCode", "");
				ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtTypeName", "");
				ra.setAttribute("ColRealEstatePrintoutLDB", "COLL_TYPE_ID", null);
				ra.setAttribute("ColRealEstatePrintoutLDB", "dummySt", "");
				
				ra.setAttribute("ColRealEstatePrintoutLDB", "dummyDate", null);
	
				return true;
			}


		 	if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
		 		ra.createLDB("CollateralTypeLookUpLDB");
		 	}
		 	String colCat =(String) ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtCatCode");
		 	
		 	ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", colCat);
		 	
			LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp"); 
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "COLL_TYPE_ID", "coll_type_id");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtTypeCode", "coll_type_code");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtTypeName", "coll_type_name");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_period_rev");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_mvp_ponder");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_mvp_ponder_mn");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_mvp_ponder_mx");
			
			
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_hnb_ponder");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_hnb_ponder_mn");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_hnb_ponder_mx");
			
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_rzb_ponder");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_rzb_ponder_mn");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_rzb_ponder_mx");
			
			
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_category");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_hypo_fidu");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "hypo_fidu_name");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_anlitika");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "coll_accounting");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "accounting_name");
			//
			//		if(){
			//				RealEstate_txtCollAccountingName
			//		} else{
			//				RealEstate_txtCollAccountingName
			//		} 
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummyDate", "coll_date_from");
			lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummyDate", "coll_date_until");
			
			
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
     
	 public boolean ColRealEstatePrintout_Owner_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
		String ldbName = "ColRealEstatePrintoutLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtOwnerCode",null);
			ra.setAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtOwnerName",null);
			ra.setAttribute("ColRealEstatePrintoutLDB","OWNER_ID",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("ColRealEstatePrintout_txtOwnerName")) {
			ra.setAttribute(ldbName, "ColRealEstatePrintout_txtOwnerCode", "");
		} else if (ra.getCursorPosition().equals("ColRealEstatePrintout_txtOwnerCode")) {
			ra.setAttribute(ldbName, "ColRealEstatePrintout_txtOwnerName", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "ColRealEstatePrintout_txtOwnerName") != null){
			d_name = (String) ra.getAttribute(ldbName, "ColRealEstatePrintout_txtOwnerName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "ColRealEstatePrintout_txtOwnerCode") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "ColRealEstatePrintout_txtOwnerCode");
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
		
		
		 if (ra.getCursorPosition().equals("ColRealEstatePrintout_txtOwnerCode")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtOwnerCode"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtOwnerName"));

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
		
		ra.setAttribute("ColRealEstatePrintoutLDB", "OWNER_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtOwnerCode", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtOwnerName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtOwnerCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtOwnerName")){
			ra.setCursorPosition(1);
		}
		
		
		return true;

		
	}//RealEstate_CarrierQbe_FV
 
	 public boolean ColRealEstatePrintout_Ben_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
		String ldbName = "ColRealEstatePrintoutLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtBenCode",null);
			ra.setAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtBenName",null);
			ra.setAttribute("ColRealEstatePrintoutLDB","BEN_ID",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("ColRealEstatePrintout_txtBenName")) {
			ra.setAttribute(ldbName, "ColRealEstatePrintout_txtBenCode", "");
		} else if (ra.getCursorPosition().equals("ColRealEstatePrintout_txtBenCode")) {
			ra.setAttribute(ldbName, "ColRealEstatePrintout_txtBenName", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "ColRealEstatePrintout_txtBenName") != null){
			d_name = (String) ra.getAttribute(ldbName, "ColRealEstatePrintout_txtBenName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "ColRealEstatePrintout_txtBenCode") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "ColRealEstatePrintout_txtBenCode");
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
				
		 if (ra.getCursorPosition().equals("ColRealEstatePrintout_txtBenCode")) 
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

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtBenCode"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtBenName"));

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
		
		ra.setAttribute("ColRealEstatePrintoutLDB", "BEN_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtBenCode", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtBenName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtBenCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtBenName")){
			ra.setCursorPosition(1);
		}
	
		return true;
	
	}
  
	 public boolean ColRealEstatePrintout_Cada_FV(String ElName, Object ElValue, Integer LookUp) {              

		// KATASTARSKA OPCINA:
		// RealEstate_QBE_CADA_MUNC							
		// RealEstate_txtCadMuncCode
		// RealEstate_txtCadMuncName
		
		
		if (ElValue == null || ((String) ElValue).equals("")) {  
			ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtCadaCode", "");                        
			ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtCadaName", "");                        
			ra.setAttribute("ColRealEstatePrintoutLDB", "CADA_ID", null);                               
			return true;                                                                                 
		}                                                                                              
        
		
		if (!ra.isLDBExists("CadastreMuncipalityLookUpLDB")) {
	 		ra.createLDB("CadastreMuncipalityLookUpLDB");
	 	}
	 	//ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDPolMapId", null);
		//ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDCoId", (java.math.BigDecimal) ra.getAttribute("ColRealEstatePrintoutLDB", "RealEstate_REAL_EST_COURT_ID"));
		ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDPolMapId", null);
		ra.setAttribute("CadastreMuncipalityLookUpLDB", "bDCoId", null);

		LookUpRequest lookUpRequest = new LookUpRequest("CadastreMuncipalityLookUp");                       
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "CADA_ID", "cad_map_id");           
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtCadaCode", "code");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtCadaName", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}  
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtCadaCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtCadaName")){
			ra.setCursorPosition(1);
		}	
		return true;                                                                                   
   
	}//RealEstate_QbeCadastreMuncipality_FV     
	 
	 public boolean ColRealEstatePrintout_OJ_FV(String elementName, Object elementValue, Integer lookUpType) {
		
	 	if (elementValue == null || ((String) elementValue).equals("")) {  
	 				ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtOJCode", "");                        
					ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtOJName", "");                        
					ra.setAttribute("ColRealEstatePrintoutLDB", "OU_ID", null);                               
					return true;                                                                                 
		}   
	 	
	 	
		LookUpRequest lookUpRequest = new LookUpRequest("OrgUniLookUp");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtOJCode", "code");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtOJName", "name");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "OU_ID", "org_uni_id");
		
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtRefCode", "");                        
		ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtRefName", "");                        
		ra.setAttribute("ColRealEstatePrintoutLDB", "USE_ID", null);  
		
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtOJCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtOJName")){
			ra.setCursorPosition(1);
		}	
		
		return true;
	}

	 public boolean ColRealEstatePrintout_Ref_FV(String elementName, Object elementValue, Integer lookUpType) {
		
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtRefCode", "");                        
			ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtRefName", "");                        
			ra.setAttribute("ColRealEstatePrintoutLDB", "USE_ID", null);                               
			return true;                                                                                 
	 	}   
	 	
	 	
	 	if (ra.getCursorPosition().equals("ColRealEstatePrintout_txtRefCode")) {
		     ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtRefName", "");
		     ra.setAttribute("ColRealEstatePrintoutLDB", "USE_ID", null);      
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("ColRealEstatePrintout_txtRefName")) {
		     ra.setAttribute("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtRefCode", "");
		     ra.setAttribute("ColRealEstatePrintoutLDB", "USE_ID", null);      
		 }		
		
		
		if (!ra.isLDBExists("AppUserOrgLDB")) ra.createLDB("AppUserOrgLDB");
		ra.setAttribute("AppUserOrgLDB", "org_uni_id", ra.getAttribute("ColRealEstatePrintoutLDB","OU_ID"));	
		ra.setAttribute("ColRealEstatePrintoutLDB", "dummySt", "");
		ra.setAttribute("ColRealEstatePrintoutLDB", "dummyBd", "");
	 	
		LookUpRequest lookUpRequest = new LookUpRequest("AppUserOrgLookUpColl");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "USE_ID", "use_id");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtRefCode", "login");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "ColRealEstatePrintout_txtRefName", "user_name");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummySt", "abbreviation");
		lookUpRequest.addMapping("ColRealEstatePrintoutLDB", "dummyBd", "org_uni_id");

		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtRefCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("ColRealEstatePrintout_txtRefName")){
			ra.setCursorPosition(1);
		}	
		return true;

	}

	 public boolean ColRealEstatePrintout_DateFrom_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtDateUntil");
	 	
	 	if(datumOd != null && datumDo != null){
	 		if(datumDo.before(datumOd)){
				//Datum DO ne moze biti manji od datuma OD.
		 		ra.showMessage("wrnclt30c");
				return false;
			}
	 	}
	 	
	 	
	 	return true; 
	 }
	 
	 public boolean ColRealEstatePrintout_DateUntil_FV(){
	 	java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtDateFrom");
	 	java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("ColRealEstatePrintoutLDB","ColRealEstatePrintout_txtDateUntil");
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
    
    
    
    
    
    
}
