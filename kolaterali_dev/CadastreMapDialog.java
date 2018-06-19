/*
 * Created on 2006.10.16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CadastreMapDialog extends Handler {
    public static String cvsident = "$Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CadastreMapDialog.java,v 1.1 2006/11/08 12:37:49 hrazst Exp $";
    
    public CadastreMapDialog (ResourceAccessor ra){
    	super(ra);
    }  

    public void CadastreMapDialog_SE(){    	
    	ra.createLDB("CadastreMapDialogLDB");
    	
    	String scr_ctxt = ra.getScreenContext().trim();
    	if(scr_ctxt.equalsIgnoreCase("scr_detail") || scr_ctxt.equalsIgnoreCase("scr_action")){
    		CadastreMapDialog_fill();
    	}else if (scr_ctxt.equalsIgnoreCase("scr_insert"))
    		CadastreMapDialog_insert();        
    }

    public void CadastreMapDialog_fill(){
		TableData td = (TableData) ra.getAttribute("CadastreMapLDB", "tblCadastreMap");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        Vector row = td.getSelectedRowData();
    
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtCode", row.elementAt(0));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtName", row.elementAt(1));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtPolCode", row.elementAt(2));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtPolName", row.elementAt(3));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtCoCode", row.elementAt(4));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtCoName", row.elementAt(5));
        
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtOpenIdLogin", hidden.elementAt(10));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtOpenIdName", hidden.elementAt(11));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtOpeningTs", hidden.elementAt(8));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtUseIdLogin", hidden.elementAt(12));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtUseIdName", hidden.elementAt(13));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtUserLock", hidden.elementAt(9)); 
             
        ra.setAttribute("CadastreMapDialogLDB", "CAD_MAP_ID", hidden.elementAt(0));
        ra.setAttribute("CadastreMapDialogLDB", "CODE_CAD_REG", hidden.elementAt(1));
        ra.setAttribute("CadastreMapDialogLDB", "PARENT_CAD_MAP_ID", hidden.elementAt(2));
        ra.setAttribute("CadastreMapDialogLDB", "CAD_MAP_TYP_ID", hidden.elementAt(3));
        ra.setAttribute("CadastreMapDialogLDB", "POL_MAP_ID", hidden.elementAt(4));
        ra.setAttribute("CadastreMapDialogLDB", "CO_ID", hidden.elementAt(5));
        ra.setAttribute("CadastreMapDialogLDB", "COU_ID", hidden.elementAt(6));
        ra.setAttribute("CadastreMapDialogLDB", "USER_LOCK", hidden.elementAt(9));
        ra.setAttribute("CadastreMapDialogLDB", "USE_ID", hidden.elementAt(15));
        ra.setAttribute("CadastreMapDialogLDB", "USE_OPEN_ID", hidden.elementAt(14));
        ra.setAttribute("CadastreMapDialogLDB", "OPENING_TS", hidden.elementAt(8));
    }
    
    public void CadastreMapDialog_insert(){
    	ra.setAttribute("CadastreMapDialogLDB", "CODE_CAD_REG", "XXX");
    	ra.setAttribute("CadastreMapDialogLDB", "PARENT_CAD_MAP_ID", null);
    	ra.setAttribute("CadastreMapDialogLDB", "CAD_MAP_TYP_ID", new BigDecimal("4444"));
    	ra.setAttribute("CadastreMapDialogLDB", "COU_ID", new BigDecimal("999"));
    	
    	ra.setAttribute("CadastreMapDialogLDB","USE_OPEN_ID" ,ra.getAttribute("GDB","use_id"));
    	ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtOpenIdLogin", ra.getAttribute("GDB","Use_Login"));
        ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtOpenIdName",ra.getAttribute("GDB","Use_UserName"));
    }

    public boolean CadastreMapDialog_Pol_FV(String elementName, Object elementValue, Integer LookUp){
        
		BigDecimal countyTypeId = new BigDecimal("3999.0");
			
		if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtPolCode", "");
		     ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtPolName", "");
		     ra.setAttribute("CadastreMapDialogLDB", "POL_MAP_ID", null);
		     return true;
		 }

		if (ra.getCursorPosition().equals("CadastreMapDialog_txtPolName")) {
		     ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtPolCode", "");
		 } else if (ra.getCursorPosition().equals("CadastreMapDialog_txtPolCode")) {
		     ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtPolName", "");
		 }	
		
		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdLookUpLDB");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", countyTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
		 lookUpRequest.addMapping("CadastreMapDialogLDB", "POL_MAP_ID", "pol_map_id");
		 lookUpRequest.addMapping("CadastreMapDialogLDB", "CadastreMapDialog_txtPolCode", "code");
		 lookUpRequest.addMapping("CadastreMapDialogLDB", "CadastreMapDialog_txtPolName", "name");
		
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
    
    public boolean CadastreMapDialog_Co_FV(String ElName, Object ElValue, Integer LookUp) {
		
		// SIFRA I NAZIV SUDA: - PODACI O SUDU
		// RealEstate_REAL_EST_COURT_ID							
		// RealEstate_txtCoCode
		// RealEstate_txtCoName
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtCoCode", "");
			ra.setAttribute("CadastreMapDialogLDB", "CadastreMapDialog_txtCoName", "");
			ra.setAttribute("CadastreMapDialogLDB", "CO_ID", null);
			ra.setAttribute("CadastreMapDialogLDB", "dummyBd", null);
			ra.setAttribute("CadastreMapDialogLDB", "dummyDate", null);
		
			return true;
		}
       
		LookUpRequest lookUpRequest = new LookUpRequest("CollCourtLookUp"); 
		lookUpRequest.addMapping("CadastreMapDialogLDB", "CO_ID", "co_id");
		lookUpRequest.addMapping("CadastreMapDialogLDB", "CadastreMapDialog_txtCoCode", "co_code");
		lookUpRequest.addMapping("CadastreMapDialogLDB", "CadastreMapDialog_txtCoName", "co_name");
		lookUpRequest.addMapping("CadastreMapDialogLDB", "dummyBd", "co_pol_map_id_cnt");
		lookUpRequest.addMapping("CadastreMapDialogLDB", "dummyDate", "co_date_from");
		lookUpRequest.addMapping("CadastreMapDialogLDB", "dummyDate", "co_date_until");
		
		
		try {
				ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
				ra.showMessage("err012");
				return false;
		} catch (NothingSelected ns) {
				return false;
		}
		if(ra.getCursorPosition().equals("CadastreMapDialog_txtCoCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("CadastreMapDialog_txtCoName")){
			ra.setCursorPosition(1);
		}	
		return true;

    }
    
    public boolean CadastreMapDialog_txtCode_FV(String ElName, Object ElValue, Integer LookUp) {
    	
    	String code=(String) ElValue;
    	
    	if (code==null || code.equals("")) return true;	
    	
    	if(code.charAt(0)!='X'){
    		ra.showMessage("infcltzst2");
    		return false;
    	}
    	return true;
    }
    
    public void confirm(){
     
         if (!(ra.isRequiredFilled())) {
  			return;
  		} 
	
        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
        } catch (VestigoTMException vtme) {
            error("RealEstateDialog -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
       ra.exitScreen();
       ra.refreshActionList("tblCadastreMap");
 	}
    
	public void change(){
        
        if (!(ra.isRequiredFilled())) {
 			return;
 		}

	    Integer update = (Integer) ra.showMessage("qer002");
	    if (update != null && update.intValue() == 1) {
	        try {
	            ra.executeTransaction();
	            ra.showMessage("infclt3");
	        } catch (VestigoTMException vtme) {
	            error("RevRegCoefReDialog -> update(): VestigoTMException", vtme);
	            if (vtme.getMessageID() != null)
	                ra.showMessage(vtme.getMessageID());
	        }
	    }
	    ra.exitScreen();
	    ra.refreshActionList("tblCadastreMap");

	}
	
    public void delete() {
        Integer del = (Integer) ra.showMessage("qzst2");
        //provjerava da li korisnik zeli obrisati zapis
        if (del != null && del.intValue() == 1) {
            try {
                ra.executeTransaction();
                ra.showMessage("infcltzst1");
            } catch (VestigoTMException vtme) {
                error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
        }
        ra.exitScreen();
        ra.refreshActionList("tblCadastreMap");
        //scr_delete za transakciju
    }
   
}
