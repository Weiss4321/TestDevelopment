/*
 * Created on 2006.10.23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CadastreMapQBE extends Handler {
    public static String cvsident = "$Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CadastreMapQBE.java,v 1.1 2006/11/08 12:37:50 hrazst Exp $";
    
    public CadastreMapQBE (ResourceAccessor ra){
    	super(ra);
    } 
    
    public boolean CadastreMapQBE_Pol_FV(String elementName, Object elementValue, Integer LookUp){
        
		BigDecimal countyTypeId = new BigDecimal("3999.0");
			
		if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("CadastreMapLDB", "CadastreMapQBE_txtPolCode", "");
		     ra.setAttribute("CadastreMapLDB", "CadastreMapQBE_txtPolName", "");
		     ra.setAttribute("CadastreMapLDB", "POL_MAP_ID", null);
		     return true;
		 }

		if (ra.getCursorPosition().equals("CadastreMapQBE_txtPolName")) {
		     ra.setAttribute("CadastreMapLDB", "CadastreMapQBE_txtPolCode", "");
		 } else if (ra.getCursorPosition().equals("CadastreMapQBE_txtPolCode")) {
		     ra.setAttribute("CadastreMapLDB", "CadastreMapQBE_txtPolName", "");
		 }	
		
		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdLookUpLDB");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", countyTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
		 lookUpRequest.addMapping("CadastreMapLDB", "POL_MAP_ID", "pol_map_id");
		 lookUpRequest.addMapping("CadastreMapLDB", "CadastreMapQBE_txtPolCode", "code");
		 lookUpRequest.addMapping("CadastreMapLDB", "CadastreMapQBE_txtPolName", "name");
		
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
    
    public boolean CadastreMapQBE_Co_FV(String ElName, Object ElValue, Integer LookUp) {
		
		// SIFRA I NAZIV SUDA: - PODACI O SUDU
		// RealEstate_REAL_EST_COURT_ID							
		// RealEstate_txtCoCode
		// RealEstate_txtCoName
		if (ElValue == null || ((String) ElValue).equals("")) {
			ra.setAttribute("CadastreMapLDB", "CadastreMapQBE_txtCoCode", "");
			ra.setAttribute("CadastreMapLDB", "CadastreMapQBE_txtCoName", "");
			ra.setAttribute("CadastreMapLDB", "CO_ID", null);
			ra.setAttribute("CadastreMapLDB", "dummyBd", null);
			ra.setAttribute("CadastreMapLDB", "dummyDate", null);
		
			return true;
		}
       
		LookUpRequest lookUpRequest = new LookUpRequest("CollCourtLookUp"); 
		lookUpRequest.addMapping("CadastreMapLDB", "CO_ID", "co_id");
		lookUpRequest.addMapping("CadastreMapLDB", "CadastreMapQBE_txtCoCode", "co_code");
		lookUpRequest.addMapping("CadastreMapLDB", "CadastreMapQBE_txtCoName", "co_name");
		lookUpRequest.addMapping("CadastreMapLDB", "dummyBd", "co_pol_map_id_cnt");
		lookUpRequest.addMapping("CadastreMapLDB", "dummyDate", "co_date_from");
		lookUpRequest.addMapping("CadastreMapLDB", "dummyDate", "co_date_until");
		
		
		try {
				ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
				ra.showMessage("err012");
				return false;
		} catch (NothingSelected ns) {
				return false;
		}
		if(ra.getCursorPosition().equals("CadastreMapQBE_txtCoCode")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("CadastreMapQBE_txtCoName")){
			ra.setCursorPosition(1);
		}	
		return true;

    }
    
    public void search(){
    	ra.performQueryByExample("tblCadastreMap");
    	ra.exitScreen();
    }
    
}
