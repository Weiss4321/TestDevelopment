/*
 * Created on 2007.08.28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.util.CharUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollReview2 extends Handler {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollReview2.java,v 1.10 2007/11/12 14:05:37 hratar Exp $";
	public CollReview2(ResourceAccessor ra) {
		super(ra);
		// TODO Auto-generated constructor stub
	}
	public void CollReview2_SE(){    	
    	if(!ra.isLDBExists("CollReview2LDB")){
			ra.createLDB("CollReview2LDB");
    	}
    	ra.setAttribute("CollReview2LDB","CollReview2_txtCategoryCode","GARA");
    	ra.setAttribute("CollReview2LDB","CollReview2_txtCategoryName","Garancije");
    	ra.setContext("CollReview2_txtCategoryCode","fld_protected");
		ra.setContext("CollReview2_txtCategoryName","fld_protected");
        return;
    }	
	
	
	
	public boolean CollReview2_txtCategory_FV() {
		return true;
	}
	
	public boolean CollReview2_txtCollTypeCode_FV(String elementName, Object elementValue, Integer lookUpType){
	 	String coll_category;
	 	BigDecimal coll_type;
	 	String col_cat_id;
		String ldbName = "CollReview2LDB";
		if (elementValue == null || ((String) elementValue).equals("")) {  
			ra.setAttribute(ldbName, "CollReview2_txtCollTypeCode", "");                        
			ra.setAttribute(ldbName, "CollReview2_txtCollTypeName", "");                        
			ra.setAttribute(ldbName, "COL_TYPE_ID", null);                               
			return true;                                                                                 
	 	}   
	
	     ra.setAttribute(ldbName, "CollReview2_txtCollTypeName", "");
	     ra.setAttribute(ldbName, "COL_TYPE_ID", null);   	
	     
	     //col_cat_id = (String)ra.getAttribute(ldbName, "COL_CAT_ID");
	     coll_category =(String) ra.getAttribute(ldbName,"CollReview2_txtCategoryCode");
	    
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
			lookUpRequest.addMapping(ldbName, "CollReview2_txtCollTypeCode", "coll_type_code");
			lookUpRequest.addMapping(ldbName, "CollReview2_txtCollTypeName", "coll_type_name");
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
	
	public boolean CollReview2_txtGroupOfIssuers_FV(String elName, Object elValue, Integer lookUpType) {
		String ldbName = "CollReview2LDB";
    	if (elValue == null || ((String)elValue).equals("")) {
    		ra.setAttribute(ldbName,"CollReview2_txtGroupOfIssuers","");
    		ra.setAttribute(ldbName,"CollReview2_txtGroupOfIssuersDesc","");
    		return true;
    	}
    	
    	
    	if (!ra.isLDBExists("UserCodeValueLookUpLDB"))
    		ra.createLDB("UserCodeValueLookUpLDB");
    	
    	ra.setAttribute("UserCodeValueLookUpLDB","use_cod_id","guarantee_issuer");
    	ra.setAttribute(ldbName,"dummyBd",null);
    	ra.setAttribute(ldbName,"dummySt","");
    	
    	LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");
    	lookUpRequest.addMapping(ldbName,"dummyBd","use_cod_val_id");
    	lookUpRequest.addMapping(ldbName,"CollReview2_txtGroupOfIssuers","use_code_value");
    	lookUpRequest.addMapping(ldbName,"CollReview2_txtGroupOfIssuersDesc","use_code_desc");
    	
    	try {
    		ra.callLookUp(lookUpRequest);
    	}
    	catch(EmptyLookUp elu) {
    		ra.showMessage("err012");
    		return false;
    	}
    	catch (NothingSelected ns) {
    		return false;
    	}
    	
    	return true;
    }
	
	public void toCSVfile() throws VestigoTMException{
		String pomString = null;
     	BigDecimal pomBigDecimal = null;
     	Date pomDate = null;
     	String bankSign = null;
     	String param = null;
     	String delimiteri = null, deli = null;
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

		ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(1760206003));

		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
		
		
		//kupljenje parametra iz LDB-ja ekrana
		
	
		pomString=(String) ra.getAttribute("CollReview2LDB","COL_CAT_ID");
		if (pomString==null || pomString.equals("")==true){
			parametri[0]=" ";
		}else{
			parametri[0]=(String) ra.getAttribute("CollReview2LDB","COL_CAT_ID").toString();	
		}	
		
		pomString=(String) ra.getAttribute("CollReview2LDB","COL_TYPE_ID");
		if (pomString==null || pomString.equals("")==true){
			parametri[1]=" ";
		}else{
			parametri[1]=(String) ra.getAttribute("CollReview2LDB","COL_TYPE_ID").toString();	
		}	
		
		pomString=(String) ra.getAttribute("CollReview2LDB","CollReview2_txtGroupOfIssuers");
		if (pomString==null || pomString.equals("")==true){
			parametri[2]=" ";
		}else{ 
			parametri[2]=(String) ra.getAttribute("CollReview2LDB","CollReview2_txtGroupOfIssuers").toString();	
		}	
		
		bankSign = (String)ra.getAttribute("GDB", "bank_sign");
		
		param = parametri[0] + ";" + parametri[1] + ";" + parametri[2] + ";" + bankSign;	
		//System.out.println(param);

		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
		
		ra.executeTransaction();
		
		ra.showMessage("inf075");
		//ra.exitScreen();	
	}	
}
