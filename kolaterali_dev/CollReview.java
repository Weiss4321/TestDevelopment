/*
 * Created on 2007.05.16
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
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollReview extends Handler {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollReview.java,v 1.7 2008/03/14 11:20:25 hramkr Exp $";
	public CollReview(ResourceAccessor ra) {
		super(ra);
		// TODO Auto-generated constructor stub
	}
	public void CollReview_SE(){    	
    	
    	if(!ra.isLDBExists("CollReviewLDB")){
			ra.createLDB("CollReviewLDB");
    	}
        return;
    }	
	
	public boolean CollReview_txtDateOfReview_FV() {
		
		Date current_date = (Date) ra.getAttribute("GDB","ProcessingDate");
		Date dateOfReview = (Date) ra.getAttribute("CollReviewLDB","CollReview_txtDateOfReview");
		
		if (dateOfReview == null || current_date == null)
			return true;
		
		if (current_date.before(dateOfReview)) 
		{	
			ra.showMessage("wrnclt121");
			return false;
		}
		return true;			
	}
	
	public boolean CollReview_txtRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollReviewLDB";
		
		if (elementValue == null || ((String)elementValue).equals("")) {
			
			ra.setAttribute(ldbName,"CollReview_txtClientName","");
			ra.setAttribute(ldbName,"CollReview_txtRegisterNo","");
			ra.setAttribute(ldbName,"CUS_ID",null);
			ra.setRequired("CollReview_txtClientGroup",true);
			ra.setContext("CollReview_txtClientGroup","fld_plain");
			return true;
		}
		
		if (ra.getCursorPosition().equals("CollReview_txtClientName")) {
            ra.setAttribute(ldbName, "CollReview_txtClientName", "");
        }else if (ra.getCursorPosition().equals("CollReview_txtRegisterNo")) {
            ra.setAttribute(ldbName, "CollReview_txtClientName", "");
        }

		
		String d_name = "";
		if (ra.getAttribute(ldbName, "CollReview_txtClientName") != null){
			d_name = (String) ra.getAttribute(ldbName, "CollReview_txtClientName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "CollReview_txtRegisterNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "CollReview_txtRegisterNo");
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
		
		if (ra.isLDBExists("CustomerAllLookUpLDB")) {
			ra.setAttribute("CustomerAllLookUpLDB", "cus_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "register_no", "");
			ra.setAttribute("CustomerAllLookUpLDB", "code", "");
			ra.setAttribute("CustomerAllLookUpLDB", "name", "");
			ra.setAttribute("CustomerAllLookUpLDB", "add_data_table", "");
			ra.setAttribute("CustomerAllLookUpLDB", "cus_typ_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "cus_sub_typ_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", "");
			ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
		} else {
			ra.createLDB("CustomerAllLookUpLDB");
		} 

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "CollReview_txtRegisterNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "CollReview_txtClientName"));

		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
		lookUpRequest.addMapping(ldbName, "CUS_ID", "cus_id");
		lookUpRequest.addMapping(ldbName, "CollReview_txtRegisterNo", "register_no");
		lookUpRequest.addMapping(ldbName, "dummySt", "code");
		lookUpRequest.addMapping(ldbName, "CollReview_txtClientName", "name");
		lookUpRequest.addMapping(ldbName, "dummySt", "add_data_table");
		lookUpRequest.addMapping(ldbName, "dummyBd", "cus_typ_id");
		lookUpRequest.addMapping(ldbName, "dummyBd", "cus_sub_typ_id");
		lookUpRequest.addMapping(ldbName, "dummySt", "eco_sec");
		lookUpRequest.addMapping(ldbName, "dummyBd", "residency_cou_id");
	
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		 
		
		if (d_register_no!=null && !d_register_no.equals("")) 
		{
			
			ra.setContext("CollReview_txtClientGroup","fld_protected");	
			ra.setRequired("CollReview_txtClientGroup",false);	
		} else {
			ra.setContext("CollReview_txtClientGroup","fld_plain");	
			ra.setRequired("CollReview_txtClientGroup",true);				
		}  
			
		return true;
 
	}
	
	public boolean CollReview_txtClientGroup_FV(String elementName, Object ElValue, Integer lookUpType) {
		String ldbName = "CollReviewLDB";
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute(ldbName, "CollReview_txtClientGroup", "");
			ra.setContext("CollReview_txtRegisterNo","fld_plain");
				return true;
		}



		LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");		

		request.addMapping(ldbName, "CollReview_txtClientGroup", "Vrijednosti");


	
		try {
			ra.callLookUp(request);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		
		String clGroup = (String)ra.getAttribute(ldbName,"CollReview_txtClientGroup");
		if (clGroup != null &&  !((String)clGroup).equals(""))
			ra.setContext("CollReview_txtRegisterNo","fld_protected");
		
		return true;
	
	}
	
	public boolean CollReview_txtCollCategoryCode_FV(String elementName, Object elementValue, Integer lookUpType) {
		String ldbName = "CollReviewLDB";
		String coll_category;
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "CollReview_txtCollCategoryCode", "");                        
			ra.setAttribute(ldbName, "CollReview_txtCollCategoryName", "");                        
			ra.setAttribute(ldbName, "COL_CAT_ID", null);   
			ra.setAttribute(ldbName, "CollReview_txtCollTypeCode", "");                        
			ra.setAttribute(ldbName, "CollReview_txtCollTypeName", "");                        
			ra.setAttribute(ldbName, "COL_TYPE_ID", null);   
			
			return true;                                                                                 
	 	}   
	
	    ra.setAttribute(ldbName, "CollReview_txtCollCategoryName", "");
	    ra.setAttribute(ldbName, "COL_CAT_ID", null);   	
	     
	     
		LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
		lookUpRequest.addMapping(ldbName, "CollReview_txtCollCategoryName", "name");
		lookUpRequest.addMapping(ldbName, "COL_CAT_ID", "col_cat_id");
		lookUpRequest.addMapping(ldbName, "CollReview_txtCollCategoryCode", "code");
			
			
	     
			
			
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012"); 
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		
// ako je promijenja kategorija treba obrisati tip kolaterala
		if (ra.isValidated("CollReview_txtCollCategoryCode")) {
			ra.setAttribute(ldbName, "CollReview_txtCollTypeCode", "");                        
			ra.setAttribute(ldbName, "CollReview_txtCollTypeName", "");                        
			ra.setAttribute(ldbName, "COL_TYPE_ID", null);   			
		}
		
		return true;
	}
	
	public boolean CollReview_txtCollTypeCode_FV(String elementName, Object elementValue, Integer lookUpType){
	 	String coll_category;
	 	BigDecimal coll_type;
	 	String col_cat_id;
		String ldbName = "CollReviewLDB";
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "CollReview_txtCollTypeCode", "");                        
			ra.setAttribute(ldbName, "CollReview_txtCollTypeName", "");                        
			ra.setAttribute(ldbName, "COL_TYPE_ID", null);                               
			return true;                                                                                 
	 	}   
	
	     ra.setAttribute(ldbName, "CollReview_txtCollTypeName", "");
	     ra.setAttribute(ldbName, "COL_TYPE_ID", null);   	
	     
	     //col_cat_id = (String)ra.getAttribute(ldbName, "COL_CAT_ID");
	     coll_category =(String) ra.getAttribute(ldbName,"CollReview_txtCollCategoryCode");
	    
	     //u LDB stavlja kategoriju po kojoj æe se podici lookUp
	     if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
	 		ra.createLDB("CollateralTypeLookUpLDB");
	 	} 
	     ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory",coll_category);
	     if (coll_category==null || coll_category.equals("")) 
	     {
	     	ra.showMessage("wrnclt96");
			return false;
	     }
			
	     ra.setAttribute(ldbName, "dummySt", "");
	     ra.setAttribute(ldbName, "dummyBd", null);
	     ra.setAttribute(ldbName, "dummyDt", null);
	     
			LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp");
			
			lookUpRequest.addMapping(ldbName, "COL_TYPE_ID", "coll_type_id");
			lookUpRequest.addMapping(ldbName, "CollReview_txtCollTypeCode", "coll_type_code");
			lookUpRequest.addMapping(ldbName, "CollReview_txtCollTypeName", "coll_type_name");
			lookUpRequest.addMapping(ldbName, "dummySt", "coll_period_rev");
			lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder");
			lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder_mn");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder_mx");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder");
			lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder_mn");
			lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder_mx");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder_mn");
		    lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder_mx");
		    lookUpRequest.addMapping(ldbName, "dummySt","coll_category");
			lookUpRequest.addMapping(ldbName, "dummySt", "coll_hypo_fidu");
			lookUpRequest.addMapping(ldbName, "dummySt", "hypo_fidu_name");
		    lookUpRequest.addMapping(ldbName, "dummySt", "coll_anlitika");
		    lookUpRequest.addMapping(ldbName, "dummySt", "coll_accounting");
		    lookUpRequest.addMapping(ldbName, "dummySt", "accounting_name");
		    lookUpRequest.addMapping(ldbName, "dummyDt", "coll_date_from");
		    lookUpRequest.addMapping(ldbName, "dummyDt", "coll_date_until");
			
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
     	
     	
     	SimpleDateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
     	
     	if(!ra.isRequiredFilled()){
			return;
		}
	
		if(!ra.isLDBExists("BatchLogDialogLDB")){
			ra.createLDB("BatchLogDialogLDB");
		}

		ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(1723307003));

		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
		
		
		//kupljenje parametra iz LDB-ja ekrana
		
		pomDate=(Date) ra.getAttribute("CollReviewLDB","CollReview_txtDateOfReview");
		if (pomDate==null || pomDate.equals("")==true){
			parametri[0]=" ";
		}else{
			parametri[0]=dateF.format((Date) ra.getAttribute("CollReviewLDB","CollReview_txtDateOfReview")).toString();	
		}
		
		
		pomString=(String) ra.getAttribute("CollReviewLDB","CollReview_txtRegisterNo");
		if (pomString==null || pomString.equals("")==true){
			parametri[1]=" ";
		}else{
			parametri[1]=(String) ra.getAttribute("CollReviewLDB","CollReview_txtRegisterNo").toString();	
		}	

				
		pomString=(String) ra.getAttribute("CollReviewLDB","CollReview_txtClientGroup");
		if (pomString==null || pomString.equals("")==true){
			parametri[2]=" ";
		}else{
			parametri[2]=(String) ra.getAttribute("CollReviewLDB","CollReview_txtClientGroup").toString();	
		}	

	 	
		
	
		if (ra.getAttribute("CollReviewLDB","COL_CAT_ID") != null){	
			pomString=(String) ra.getAttribute("CollReviewLDB","COL_CAT_ID").toString();		
			if (pomString==null || pomString.equals("")==true){
				parametri[3]=" ";
			}else{
				parametri[3]=(String) ra.getAttribute("CollReviewLDB","COL_CAT_ID").toString();	
			}	
		}
		
		if (ra.getAttribute("CollReviewLDB","COL_TYPE_ID") != null) {
			pomString=(String) ra.getAttribute("CollReviewLDB","COL_TYPE_ID").toString();
			if (pomString==null || pomString.equals("")==true){
				parametri[4]=" ";
			}else{
				parametri[4]=(String) ra.getAttribute("CollReviewLDB","COL_TYPE_ID").toString();	
			}	
		}
		 
		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
		
		param = parametri[0] + ";" + parametri[1] + ";" + parametri[2] + ";" + parametri[3] + ";" + parametri[4] + ";" + bankSign;	
		//System.out.println(param);

		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		
		ra.showMessage("inf075");
		//ra.exitScreen();	
	}	
}
