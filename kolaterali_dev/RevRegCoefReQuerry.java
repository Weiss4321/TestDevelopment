/*
 * Created on 2006.05.12
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
public class RevRegCoefReQuerry extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RevRegCoefReQuerry.java,v 1.3 2006/08/25 09:44:00 hrazst Exp $";
	
    public RevRegCoefReQuerry (ResourceAccessor ra) {
        super (ra);
    }
	
    public void search(){
    	ra.performQueryByExample("tblRevRegCoefRe");
    	ra.exitScreen();
    }
    
    public boolean RevRegCoefReDialogS_txtCCounty_FV(String elementName, Object elementValue, Integer LookUp){
        
		java.math.BigDecimal countyTypeId = new java.math.BigDecimal("3999.0");
			
		if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("RevRegCoefReLDB", "RevRegCoefReDialogS_txtCCounty", "");
		     ra.setAttribute("RevRegCoefReLDB", "RevRegCoefReDialogS_txtNCounty", "");
		     ra.setAttribute("RevRegCoefReLDB", "REV_RE_COUNTY", null);
		     return true;
		 }
		 /**
		  * brise staru vrijednost u txtNCounty ako se upisivala vrijednost u
		  * txtCCounty i obratno, kako ne bi doslo do pogresnog povezivanja
		  * sifre i imena mjesta kod poziva LookUp-a
		  */
		if (ra.getCursorPosition().equals("RevRegCoefReDialogS_txtNCounty")) {
		     ra.setAttribute("RevRegCoefReLDB", "RevRegCoefReDialogS_txtCCounty", "");
		 } else if (ra.getCursorPosition().equals("RevRegCoefReDialogS_txtCCounty")) {
		     ra.setAttribute("RevRegCoefReLDB", "RevRegCoefReDialogS_txtNCounty", "");
		 }	
		
		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdLookUpLDB");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", countyTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
		 lookUpRequest.addMapping("RevRegCoefReLDB", "REV_RE_COUNTY", "pol_map_id");
		 lookUpRequest.addMapping("RevRegCoefReLDB", "RevRegCoefReDialogS_txtCCounty", "code");
		 lookUpRequest.addMapping("RevRegCoefReLDB", "RevRegCoefReDialogS_txtNCounty", "name");
		
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

	public boolean RevRegCoefReDialogS_txtCPlace_FV(String elementName, Object elementValue, Integer LookUp){
        
 		
		java.math.BigDecimal placeTypeId = new java.math.BigDecimal("5999.0");
		 BigDecimal countyId =null;
			
		 if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("RevRegCoefReLDB", "RevRegCoefReDialogS_txtCPlace", "");
		     ra.setAttribute("RevRegCoefReLDB", "RevRegCoefReDialogS_txtNPlace", "");
		     ra.setAttribute("RevRegCoefReLDB", "REV_RE_PLACE", null);
		     return true;
		 }
		 
		   //brise staru vrijednost u txtNPlace ako se upisivala vrijednost u
		   //txtCPlace i obratno, kako ne bi doslo do pogresnog povezivanja
		   //sifre i imena mjesta kod poziva LookUp-a
		  
		 if (ra.getCursorPosition().equals("RevRegCoefReDialogS_txtNPlace")) {
		     ra.setAttribute("RevRegCoefReLDB", "RevRegCoefReDialogS_txtCPlace", "");
		 } else if (ra.getCursorPosition().equals("RevRegCoefReDialogS_txtCPlace")) {
		     ra.setAttribute("RevRegCoefReLDB", "RevRegCoefReDialogS_txtNPlace", "");
		 }	
		
		 if (!ra.isLDBExists("PolMapPlacesInCountyLookUpLDB")) {
		     ra.createLDB("PolMapPlacesInCountyLookUpLDB");
		 }
		 if (ra.getAttribute("RevRegCoefReLDB", "REV_RE_COUNTY") != null) {
		 	countyId = (BigDecimal) ra.getAttribute("RevRegCoefReLDB", "REV_RE_COUNTY");
		 }
			
		 ra.setAttribute("PolMapPlacesInCountyLookUpLDB", "bDCountyId", countyId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PolMapPlacesInCountyLookUp");
		 //mapping se dodaje onim redoslijedom kojim se zeli naknadno citati
		 lookUpRequest.addMapping("RevRegCoefReLDB", "REV_RE_PLACE", "pol_map_id");
		 lookUpRequest.addMapping("RevRegCoefReLDB", "RevRegCoefReDialogS_txtCPlace", "code");
		 lookUpRequest.addMapping("RevRegCoefReLDB", "RevRegCoefReDialogS_txtNPlace", "name"); 
		
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
}
