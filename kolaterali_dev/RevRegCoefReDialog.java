/*
 * Created on 2006.05.05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Vector;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class RevRegCoefReDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RevRegCoefReDialog.java,v 1.6 2006/05/25 08:24:53 hrazst Exp $";

    public RevRegCoefReDialog(ResourceAccessor ra) {
        super(ra);
    }
          
    public void RevRegCoefReDialog_SE(){
        
    	ra.createLDB("RevRegCoefReDialogLDB");
    	
    	String scr_ctxt = ra.getScreenContext().trim();
    	if(scr_ctxt.equalsIgnoreCase("scr_detail") || scr_ctxt.equalsIgnoreCase("scr_action")){
    		RevRegCoefReDialog_fill();
    	}else if (scr_ctxt.equalsIgnoreCase("scr_insert"))
    		RevRegCoefReDialog_insert();
    }
    
    public void RevRegCoefReDialog_fill(){
		TableData td = (TableData) ra.getAttribute("RevRegCoefReLDB", "tblRevRegCoefRe");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        Vector row = td.getSelectedRowData();
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_ID", hidden.elementAt(0));
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_PLACE", hidden.elementAt(1));
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_COUNTY", hidden.elementAt(2));
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_DISTRICT", hidden.elementAt(3));
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_RESI_QUAR", hidden.elementAt(4));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtAct", row.elementAt(7));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCode", row.elementAt(0));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCoef", row.elementAt(6));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtName", row.elementAt(1));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtSpecStat", hidden.elementAt(9));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtDateFrom", row.elementAt(8));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtDateUntil", row.elementAt(9));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCDistrict", hidden.elementAt(7));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNDistrict", row.elementAt(4));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCPlace", hidden.elementAt(5)); 
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNPlace", row.elementAt(2));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCCounty", hidden.elementAt(6));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNCounty", row.elementAt(3));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCQuar", hidden.elementAt(8));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNQuar", row.elementAt(5));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtUseIdLogin", hidden.elementAt(13));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtUseIdName", hidden.elementAt(14));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtUserLock", hidden.elementAt(15));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_UserLockNF", hidden.elementAt(15));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtOpenIdLogin", hidden.elementAt(10));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtOpenIdName", hidden.elementAt(11));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtOpeningTs", hidden.elementAt(12));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtOpeningTsNF", hidden.elementAt(12)); 
        
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_PLACE_B", hidden.elementAt(1));
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_COUNTY_B", hidden.elementAt(2));
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_DISTRICT_B", hidden.elementAt(3));
        ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_RESI_QUAR_B", hidden.elementAt(4));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtActB", row.elementAt(7));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCodeB", row.elementAt(0));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCoefB", row.elementAt(6));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNameB", row.elementAt(1));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtSpecStatB", hidden.elementAt(9));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtDateFromB", row.elementAt(8));
        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtDateUntilB", row.elementAt(9));
        ra.setAttribute("RevRegCoefReDialogLDB", "USE_OPEN_ID_B", hidden.elementAt(17));
        ra.setAttribute("RevRegCoefReDialogLDB", "USE_ID_B", hidden.elementAt(18));
        ra.setAttribute("RevRegCoefReDialogLDB", "EVE_ID_B", hidden.elementAt(16));
        
    }

	public void RevRegCoefReDialog_insert(){
	       	ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtAct", "A");
	        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtSpecStat", "00");
	        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCoef", new BigDecimal(1));
	        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtDateFrom",(java.sql.Date) ra.getAttribute("GDB","ProcessingDate"));
	        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtDateUntil", java.sql.Date.valueOf("9999-12-31"));      
	        String UserLogin =(String) ra.getAttribute("GDB","Use_Login");
	        String UserName =(String) ra.getAttribute("GDB","Use_UserName");
	        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtUseIdLogin", UserLogin);
	        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtUseIdName", UserName);
	        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtOpenIdLogin", UserLogin);
	        ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtOpenIdName", UserName);
	}
	
	public boolean RevRegCoefReDialog_txtCCounty_FV(String elementName, Object elementValue, Integer LookUp){
        
		java.math.BigDecimal countyTypeId = new java.math.BigDecimal("3999.0");
			
		if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCCounty", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNCounty", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_COUNTY", null);
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCPlace", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNPlace", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_PLACE", null);
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCDistrict", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNDistrict", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_DISTRICT", null);
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCQuar", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNQuar", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_RESI_QUAR", null);
		     return true;
		 }
		 /**
		  * brise staru vrijednost u txtNCounty ako se upisivala vrijednost u
		  * txtCCounty i obratno, kako ne bi doslo do pogresnog povezivanja
		  * sifre i imena mjesta kod poziva LookUp-a
		  */
		if (ra.getCursorPosition().equals("RevRegCoefReDialog_txtNCounty")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCCounty", "");
		 } else if (ra.getCursorPosition().equals("RevRegCoefReDialog_txtCCounty")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNCounty", "");
		 }	
		
		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdLookUpLDB");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", countyTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "REV_RE_COUNTY", "pol_map_id");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCCounty", "code");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNCounty", "name");
		
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

	public boolean RevRegCoefReDialog_txtCPlace_FV(String elementName, Object elementValue, Integer LookUp){
        		
		java.math.BigDecimal placeTypeId = new java.math.BigDecimal("5999.0");
		 BigDecimal countyId =null;
			
		 if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCPlace", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNPlace", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_PLACE", null);
		     return true;
		 }
		 
		   //brise staru vrijednost u txtNPlace ako se upisivala vrijednost u
		   //txtCPlace i obratno, kako ne bi doslo do pogresnog povezivanja
		   //sifre i imena mjesta kod poziva LookUp-a
		  
		 if (ra.getCursorPosition().equals("RevRegCoefReDialog_txtNPlace")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCPlace", "");
		 } else if (ra.getCursorPosition().equals("RevRegCoefReDialog_txtCPlace")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNPlace", "");
		 }	
		
		 if (!ra.isLDBExists("PolMapPlacesInCountyLookUpLDB")) {
		     ra.createLDB("PolMapPlacesInCountyLookUpLDB");
		 }
		 if (ra.getAttribute("RevRegCoefReDialogLDB", "REV_RE_COUNTY") != null) {
		 	countyId = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB", "REV_RE_COUNTY");
		 }
			
		 ra.setAttribute("PolMapPlacesInCountyLookUpLDB", "bDCountyId", countyId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PolMapPlacesInCountyLookUp");
		 //mapping se dodaje onim redoslijedom kojim se zeli naknadno citati
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "REV_RE_PLACE", "pol_map_id");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCPlace", "code");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNPlace", "name"); 
		
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
	
	public boolean RevRegCoefReDialog_txtCDistrict_FV(String elementName, Object elementValue, Integer LookUp){
        
		java.math.BigDecimal districtTypeId = new java.math.BigDecimal("5854877003.0");
		BigDecimal placeId=null;
			
		 if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCDistrict", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNDistrict", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_DISTRICT", null);
		     return true;
		 }
		 /*
		  * brise staru vrijednost u txtNDistrict ako se upisivala vrijednost u
		  * txtCDistrict i obratno, kako ne bi doslo do pogresnog povezivanja
		  * sifre i imena mjesta kod poziva LookUp-a
		  */
		 if (ra.getCursorPosition().equals("RevRegCoefReDialog_txtNDistrict")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCDistrict", "");
		 } else if (ra.getCursorPosition().equals("RevRegCoefReDialog_txtCDistrict")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNDistrict", "");
		 }	
		
		 if (!ra.isLDBExists("PoliticalMapByTypIdWithParentLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdWithParentLookUpLDB");
		 }
		 if (ra.getAttribute("RevRegCoefReDialogLDB", "REV_RE_PLACE") != null) {
		 	placeId = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB", "REV_RE_PLACE");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDParentPolMapId", placeId);
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDPolMapTypId", districtTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdWithParentLookUp");
		 //mapping se dodaje onim redoslijedom kojim se zeli naknadno citati
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "REV_RE_DISTRICT", "pol_map_id");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCDistrict", "code");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNDistrict", "name"); 
		
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

	public boolean RevRegCoefReDialog_txtCQuar_FV(String elementName, Object elementValue, Integer LookUp){
        
		java.math.BigDecimal resiquarTypeId = new java.math.BigDecimal("5854878003.0");
		BigDecimal districtId=null;
			
		 if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCQuar", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNQuar", "");
		     ra.setAttribute("RevRegCoefReDialogLDB", "REV_RE_RESI_QUAR", null);
		     return true;
		 }
		 /*
		  * brise staru vrijednost u txtNDistrict ako se upisivala vrijednost u
		  * txtCDistrict i obratno, kako ne bi doslo do pogresnog povezivanja
		  * sifre i imena mjesta kod poziva LookUp-a
		  */
		 if (ra.getCursorPosition().equals("RevRegCoefReDialog_txtNQuar")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCQuar", "");
		 } else if (ra.getCursorPosition().equals("RevRegCoefReDialog_txtCQuar")) {
		     ra.setAttribute("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNQuar", "");
		 }	
		
		 if (!ra.isLDBExists("PoliticalMapByTypIdWithParentLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdWithParentLookUpLDB");
		 }
		 if (ra.getAttribute("RevRegCoefReDialogLDB", "REV_RE_DISTRICT") != null) {
		 	districtId = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB", "REV_RE_DISTRICT");
		 	System.out.println(" GRADSKA CETVRT " + districtId);
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDParentPolMapId", districtId);
		 ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDPolMapTypId", resiquarTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdWithParentLookUp");
		 //mapping se dodaje onim redoslijedom kojim se zeli naknadno citati
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "REV_RE_RESI_QUAR", "pol_map_id");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtCQuar", "code");
		 lookUpRequest.addMapping("RevRegCoefReDialogLDB", "RevRegCoefReDialog_txtNQuar", "name"); 
		
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

	public void confirm(){
       
		String code = (String) ra.getAttribute("RevRegCoefReDialogLDB","RevRegCoefReDialog_txtCode");
        String name = (String) ra.getAttribute("RevRegCoefReDialogLDB","RevRegCoefReDialog_txtName");
        BigDecimal placeCode = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB","REV_RE_PLACE");
        BigDecimal countyCode = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB","REV_RE_COUNTY");
        BigDecimal districtCode = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB","REV_RE_DISTRICT");
        BigDecimal quarCode = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB","REV_RE_RESI_QUAR");
        Date from=(Date) ra.getAttribute("RevRegCoefReDialogLDB","RevRegCoefReDialog_txtDateFrom");
        Date until=(Date) ra.getAttribute("RevRegCoefReDialogLDB","RevRegCoefReDialog_txtDateUntil");
        
        if (!(ra.isRequiredFilled())) {
 			return;
 		}
        
        if(!provjeraDatuma(from,until)){
        	return;        
        }
        
        if (placeCode == null && countyCode == null && districtCode == null &&  quarCode == null){
        	ra.showMessage("wrncltzst2");
        	return;
        }
        
		if (code != null && name != null) {
		    if (code.equals("") || name.equals("")) {
		        ra.showMessage("wrncltzst1");
		    } else {		
		        try {
		        	BigDecimal UserId = (BigDecimal) ra.getAttribute("GDB","use_id");
		        	ra.setAttribute("RevRegCoefReDialogLDB", "USE_OPEN_ID", UserId);
		            ra.executeTransaction();
		            ra.showMessage("infclt2");
		        } catch (VestigoTMException vtme) {
		            error("RealEstateDialog -> insert(): VestigoTMException", vtme);
		            if (vtme.getMessageID() != null)
		                ra.showMessage(vtme.getMessageID());
		        }
              ra.exitScreen();
              ra.refreshActionList("tblRevRegCoefRe");
		    }
		}
	}
	
	public void change() {
		
		String code = (String) ra.getAttribute("RevRegCoefReDialogLDB","RevRegCoefReDialog_txtCode");
        String name = (String) ra.getAttribute("RevRegCoefReDialogLDB","RevRegCoefReDialog_txtName");
        BigDecimal placeCode = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB","REV_RE_PLACE");
        BigDecimal countyCode = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB","REV_RE_COUNTY");
        BigDecimal districtCode = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB","REV_RE_DISTRICT");
        BigDecimal quarCode = (BigDecimal) ra.getAttribute("RevRegCoefReDialogLDB","REV_RE_RESI_QUAR");
        Date from=(Date) ra.getAttribute("RevRegCoefReDialogLDB","RevRegCoefReDialog_txtDateFrom");
        Date until=(Date) ra.getAttribute("RevRegCoefReDialogLDB","RevRegCoefReDialog_txtDateUntil");	
        
        if (!(ra.isRequiredFilled())) {
 			return;
 		}
        
        if (placeCode == null && countyCode == null && districtCode == null &&  quarCode == null){
        	ra.showMessage("wrncltzst2");
        	return;
        }
        
        if(!provjeraDatuma(from,until)){
        	return;        
        }
        
		if (code != null && name != null) {
		    if (code.equals("") || name.equals("")) {
		        ra.showMessage("wrncltzst1");
		    } else {
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
		        ra.refreshActionList("tblRevRegCoefRe");
		
		    }
		}//scr_update za transakciju
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
        ra.refreshActionList("tblRevRegCoefRe");
        //scr_delete za transakciju
    }
    
    public boolean provjeraDatuma(Date from, Date until){
    	/*funkcija provjerava da li su daumi uneseni kak treba:
    	datum od nesmije biti veci od datuma do, niti datum od i datum do nesmje biti 
    	manji od danasnjeg datuma */
    	Date danasnji=null;
    	danasnji=(Date) ra.getAttribute("GDB","ProcessingDate");
    	
    	if(danasnji.after(from) || danasnji.after(until)){ 
    		ra.showMessage("wrncltzst5");
    		return false;
    	}else if(from.after(until) || from.equals(until)){
    		ra.showMessage("wrncltzst4");
    		return false;
    	}
    	
    	return true;
    }

}


