package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.util.CharUtil;

/**
 * @author
 *
 */  
public class CusaccExpCollQBE extends Handler {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CusaccExpCollQBE.java,v 1.14 2011/04/14 08:49:36 hramkr Exp $";

    /**
     * @param arg0
     */
    public CusaccExpCollQBE(ResourceAccessor arg0) {
        super(arg0);
    }

    public void CusaccExpCollQBE_SE() {

    }

    public void search() {

// punim dinamicku lbl. uvjetima pretrazivanja
    	String uvjet = "";
    	boolean postojiLiUpit = false;
    	if (ra.getAttribute("CusaccExpCollLDB","CUS_ID") != null){
    		uvjet = uvjet + (String) ra.getAttribute("CusaccExpCollLDB", "CusaccExpCollQBE_txtCustRegNo") + " - " +
    		(String) ra.getAttribute("CusaccExpCollLDB", "CusaccExpCollQBE_txtCustName") + "; ";
    		postojiLiUpit = true;
    	}
    	
    	if (ra.getAttribute("CusaccExpCollLDB","CUS_ACC_ID") != null) {
    		uvjet = uvjet + (String) ra.getAttribute("CusaccExpCollLDB", "CusaccExpCollQBE_txtCusAccNo") + "; "; 
    		postojiLiUpit = true;
    	}
    	
       //	if (ra.getAttribute("CusaccExpCollLDB","COL_HEA_ID") != null) {
       	//	uvjet = uvjet + (String) ra.getAttribute("CusaccExpCollLDB", "CusaccExpCollQBE_txtColNum") + "; ";    		
    	//}
    	 
        
    	ra.setAttribute("CusaccExpCollLDB","CusaccExpColl_dynLblSearchCriteria",uvjet );
        ra.exitScreen();
        
        if (postojiLiUpit == false) 
        	ra.showMessage("wrnclt53");
        else
        {
        	ra.performQueryByExample("tblCusaccExpColl");
        	ra.setAttribute("CusaccExpCollLDB","CusaccExpColl_dynLblSearchCriteria",uvjet );
        }
        
  
   

    }

    public boolean CusaccExpCollQBE_txtCustRegNo_FV(String elementName, Object elementValue, Integer LookUp) {
        String ldbName = "CusaccExpCollLDB";

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CusaccExpCollQBE_txtCustRegNo", null);
            ra.setAttribute(ldbName, "CusaccExpCollQBE_txtCustName", null);
            ra.setAttribute(ldbName, "CUS_ID", null);
            return true;
        }

        if (ra.getCursorPosition().equals("CusaccExpCollQBE_txtCustName")) {
            ra.setAttribute(ldbName, "CusaccExpCollQBE_txtCustRegNo", "");
        }
        else if (ra.getCursorPosition().equals("CusaccExpCollQBE_txtCustRegNo")) {
            ra.setAttribute(ldbName, "CusaccExpCollQBE_txtCustName", "");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CusaccExpCollQBE_txtCustRegNo") != null) {
            d_register_no = (String) ra.getAttribute(ldbName, "CusaccExpCollQBE_txtCustRegNo");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "CusaccExpCollQBE_txtCustName") != null) {
            d_name = (String) ra.getAttribute(ldbName, "CusaccExpCollQBE_txtCustName");
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
        }
        else {
            ra.createLDB("CustomerAllLookUpLDB");
        }

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "CusaccExpCollQBE_txtCustRegNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "CusaccExpCollQBE_txtCustName"));

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
        }
        catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        }
        catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute(ldbName, "CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "CusaccExpCollQBE_txtCustRegNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "CusaccExpCollQBE_txtCustName", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;
    }

    public boolean CusaccExpCollQBE_txtCusAccNo_FV(String elementName, Object elementValue, Integer LookUp) {
        String ldbName = "CusaccExpCollLDB";
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CusaccExpCollQBE_txtCusAccNo", null);
            ra.setAttribute(ldbName, "CUS_ACC_ID", null);
            return true;
        }

        if (!(ra.isLDBExists("CusaccExposureLookLDB"))) {
            ra.createLDB("CusaccExposureLookLDB");
        }
 
        ra.setAttribute("CusaccExposureLookLDB", "cus_id", null);

        if (ra.isLDBExists("CusaccExposureLookLDB")) {
            ra.setAttribute("CusaccExposureLookLDB", "cus_id", null);
            ra.setAttribute("CusaccExposureLookLDB", "cus_acc_no", "");
        }
   
        ra.setAttribute("CusaccExposureLookLDB", "cus_id", ra.getAttribute(ldbName, "CUS_ID"));
        ra.setAttribute("CusaccExposureLookLDB", "cus_acc_no", ra.getAttribute(ldbName, "CusaccExpCollQBE_txtCusAccNo"));
        
		LookUpRequest lookUpRequest = new LookUpRequest("CusaccExposureLookUp");   			
        lookUpRequest.addMapping("CusaccExposureLookLDB", "cus_acc_id", "cus_acc_id");         
        lookUpRequest.addMapping("CusaccExposureLookLDB", "cus_acc_no", "cus_acc_no");  
        lookUpRequest.addMapping("CusaccExposureLookLDB", "contract_no", "contract_no");
        lookUpRequest.addMapping("CusaccExposureLookLDB", "cus_acc_status", "cus_acc_status");              
        lookUpRequest.addMapping("CusaccExposureLookLDB", "cus_acc_orig_st", "cus_acc_orig_st");   
        lookUpRequest.addMapping("CusaccExposureLookLDB", "frame_cus_acc_no", "frame_cus_acc_no");   
        lookUpRequest.addMapping("CusaccExposureLookLDB", "exposure_cur_id", "exposure_cur_id");  
        lookUpRequest.addMapping("CusaccExposureLookLDB", "code_char", "code_char"); 
        lookUpRequest.addMapping("CusaccExposureLookLDB", "exposure_balance", "exposure_balance");                                                            
        lookUpRequest.addMapping("CusaccExposureLookLDB", "exposure_date", "exposure_date");          
        lookUpRequest.addMapping("CusaccExposureLookLDB", "request_no", "request_no");  
        lookUpRequest.addMapping("CusaccExposureLookLDB", "prod_code", "prod_code");  
        lookUpRequest.addMapping("CusaccExposureLookLDB", "name", "name");          
        lookUpRequest.addMapping("CusaccExposureLookLDB", "module_code", "module_code");  
  
        try {
            ra.callLookUp(lookUpRequest);
        }
        catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        }
        catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute(ldbName, "CusaccExpCollQBE_txtCusAccNo", ra.getAttribute("CusaccExposureLookLDB", "cus_acc_no"));
        ra.setAttribute(ldbName, "CUS_ACC_ID", ra.getAttribute("CusaccExposureLookLDB", "cus_acc_id"));

        return true;
    }

    public boolean CusaccExpCollQBE_txtColNum_FV(String elementName, Object elementValue, Integer LookUp) {
        String ldbName = "CusaccExpCollLDB";
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CusaccExpCollQBE_txtColNum", null);
            ra.setAttribute(ldbName, "COL_HEA_ID", null);
            return true;  
        }

        if (!(ra.isLDBExists("CollHeadColNumLookUpLDB"))) {
            ra.createLDB("CollHeadColNumLookUpLDB");
        }
        else {
            ra.setAttribute("CollHeadColNumLookUpLDB", "col_num", "");
            ra.setAttribute("CollHeadColNumLookUpLDB", "acc_no", null);
            ra.setAttribute("CollHeadColNumLookUpLDB", "cus_id", null);
        }

        ra.setAttribute("CollHeadColNumLookUpLDB", "col_num", ra.getAttribute(ldbName, "CusaccExpCollQBE_txtColNum"));
        ra.setAttribute("CollHeadColNumLookUpLDB", "acc_no", ra.getAttribute(ldbName, "CusaccExpCollQBE_txtCusAccNo"));
        ra.setAttribute("CollHeadColNumLookUpLDB", "cus_id", ra.getAttribute(ldbName, "CUS_ID"));

        LookUpRequest lookUpRequest = new LookUpRequest("CollHeadColNumLookUp");
        lookUpRequest.addMapping("CollHeadColNumLookUpLDB", "col_hea_id", "col_hea_id");
        lookUpRequest.addMapping("CollHeadColNumLookUpLDB", "col_num", "col_num");

        try {
            ra.callLookUp(lookUpRequest);
        }  
        catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        }
        catch (NothingSelected ns) {
            return false; 
        }

        ra.setAttribute(ldbName, "COL_HEA_ID", ra.getAttribute("CollHeadColNumLookUpLDB", "col_hea_id"));
        ra.setAttribute(ldbName, "CusaccExpCollQBE_txtColNum", ra.getAttribute("CollHeadColNumLookUpLDB", "col_num"));

        return true;
    } 

}

