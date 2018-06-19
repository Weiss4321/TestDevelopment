/*
 * Created on 2006.03.31
 *
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

/**
 * @author hrarmv
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PolMapResiQuar extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/PolMapResiQuar.java,v 1.13 2006/04/14 10:38:49 hrarmv Exp $";

    public PolMapResiQuar(ResourceAccessor ra) {
        super(ra);
    }

    /*
     * screen entry metoda - kreira se LDB kod ulaza na ekran
     */
    public void PolMapResiQuar_SE() {
        if (!ra.isLDBExists("PolMapResiQuarLDB")) {
            ra.createLDB("PolMapResiQuarLDB");
        }
        
        if (ra.getAttribute("tblPolMapResiQuar") == null){
            ra.createActionListSession("tblPolMapResiQuar", false);
        }
    }

    public boolean PolMapResiQuar_txtPlaceCode_FV(String elementName, Object elementValue, Integer lookUp) {

        BigDecimal placeId = new BigDecimal("5999.0");
        System.out.println("txtPlaceCode_FV: elementValue" + (String) elementValue );
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtPlaceCode", "");
            ra.setAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtPlaceName", "");
            ra.setAttribute("PolMapResiQuarLDB", "POL_MAP_ID_PLACE", null);
            return true;
        }

        /*
         * brise staru vrijednost u txtPlaceName ako se upisivala vrijednost
         * u txtPlaceCode i obratno, kako ne bi doslo do pogresnog povezivanja 
         * sifre i imena mjesta kod poziva LookUp-a
         */
		if (ra.getCursorPosition().equals("PolMapResiQuar_txtPlaceName")) {
			ra.setAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtPlaceCode", "");
		} else if (ra.getCursorPosition().equals("PolMapResiQuar_txtPlaceCode")) {
			ra.setAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtPlaceName", "");
		}
		
        if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }
        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", placeId);

        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
        lookUpRequest.addMapping("PolMapResiQuarLDB", "POL_MAP_ID_PLACE", "pol_map_id");
        lookUpRequest.addMapping("PolMapResiQuarLDB", "PolMapResiQuar_txtPlaceCode", "code");
        lookUpRequest.addMapping("PolMapResiQuarLDB", "PolMapResiQuar_txtPlaceName", "name");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        if ((ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_PLACE") != null )&& (ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_DISTRICT") != null )) {
            ra.refreshActionList("tblPolMapResiQuar");
        }
      
        return true;

    }//PolMapDistrict_txtPlaceCode_FV

    public boolean PolMapResiQuar_txtDistrictCode_FV(String elementName, Object elementValue, Integer lookUp) {

        BigDecimal districtTypeId = new BigDecimal("5854877003.0");
        BigDecimal placeId = null;
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtDistrictCode", "");
            ra.setAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtDistrictName", "");
            ra.setAttribute("PolMapResiQuarLDB", "POL_MAP_ID_DISTRICT", null);
            return true;
        }
        /*
         * brise staru vrijednost u txtPlaceName ako se upisivala vrijednost
         * u txtPlaceCode i obratno, kako ne bi doslo do pogresnog povezivanja 
         * sifre i imena mjesta kod poziva LookUp-a
         */
		if (ra.getCursorPosition().equals("PolMapResiQuar_txtDistrictName")) {
			ra.setAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtDistrictCode", "");
		} else if (ra.getCursorPosition().equals("PolMapResiQuar_txtDistrictCode")) {
			ra.setAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtDistrictName", "");
		}
		
        if (!ra.isLDBExists("PoliticalMapByTypIdWithParentLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdWithParentLookUpLDB");
        }
        if (ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_PLACE") != null) {
            placeId = (BigDecimal) ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_PLACE");
        }

        ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDParentPolMapId", placeId);
        ra.setAttribute("PoliticalMapByTypIdWithParentLookUpLDB", "bDPolMapTypId", districtTypeId);

        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdWithParentLookUp");
        //mapping se dodaje onim redoslijedom kojim se zeli naknadno citati
        lookUpRequest.addMapping("PolMapResiQuarLDB", "POL_MAP_ID_DISTRICT", "pol_map_id");
        lookUpRequest.addMapping("PolMapResiQuarLDB", "PolMapResiQuar_txtDistrictCode", "code");
        lookUpRequest.addMapping("PolMapResiQuarLDB", "PolMapResiQuar_txtDistrictName", "name");        

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        //placeId = (java.math.BigDecimal) ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_PLACE");

        if ((ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_PLACE") != null )&& (ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_DISTRICT") != null )) {
            ra.refreshActionList("tblPolMapResiQuar");
        }
       
        return true;

    }
    
    public void details(){
		if (isTableEmpty("tblPolMapResiQuar")) {
			ra.showMessage("wrn299");
			return;
		}
		
		ra.loadScreen("PolMapResiQuarDialog","scr_detail"); 
	}
    
    public void add() {
        if (!ra.isLDBExists("PolMapResiQuarDialogLDB")) {
            ra.createLDB("PolMapResiQuarDialogLDB");
        }
        ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtResiQuarCode", null);
        ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtResiQuarName", null);
        
        if ((ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_PLACE") == null)
                || ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_DISTRICT") == null) {
            	ra.showMessage("wrnclt370");
            return;
        } else {
            ra.loadScreen("PolMapResiQuarDialog", "scr_insert");
        }
        
        
        
    }//add
    
    
    public void action() {
        if (isTableEmpty("tblPolMapResiQuar")) {
            ra.showMessage("wrn299");
            return;
        }
        if (!ra.isLDBExists("PolMapResiQuarDialogLDB")) {
            ra.createLDB("PolMapResiQuarDialogLDB");
        }
        //Dohvat id od stambene cetvrti iz akcijske liste
        java.math.BigDecimal polMapIddResiQuar = null;
        TableData td = (TableData) ra.getAttribute("PolMapResiQuarLDB", "tblPolMapResiQuar");
		Vector hiddenVector = (Vector) td.getSelectedRowUnique();
		polMapIddResiQuar = (BigDecimal) hiddenVector.elementAt(0);
			
        //setiranje dohvacenog id na LDB
        ra.setAttribute("PolMapResiQuarDialogLDB", "POL_MAP_IDD_RESIQ", polMapIddResiQuar);
       
        //setiranje tipa stambene cetvrti na LDB
        java.math.BigDecimal resiQuarTypeId = new java.math.BigDecimal("5854878003.0");
        ra.setAttribute("PolMapResiQuarDialogLDB", "POL_MAP_TYP_ID_ResiQuar", resiQuarTypeId);
        ra.loadScreen("PolMapResiQuarDialog", "scr_action");
    }
    public void refresh() {
        ra.refreshActionList("tblPolMapResiQuar");
    }

    
    public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty

}