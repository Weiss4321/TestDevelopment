/*
 * Created on 2006.03.29
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
 */
public class PolMapDistrict extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/PolMapDistrict.java,v 1.22 2006/04/14 10:38:49 hrarmv Exp $";

    public PolMapDistrict(ResourceAccessor ra) {
        super(ra);
    }

    public void PolMapDistrict_SE() {
        if (!ra.isLDBExists("PolMapDistrictLDB")) {
            ra.createLDB("PolMapDistrictLDB");
        }
        if (ra.getAttribute("tblPolMapDistrict") == null) {
            ra.createActionListSession("tblPolMapDistrict", false);
        }
    }

    public boolean PolMapDistrict_txtPlaceCode_FV(String elementName, Object elementValue, Integer LookUp) {

        java.math.BigDecimal placeTypeId = new java.math.BigDecimal("5999.0");
        java.math.BigDecimal placeId = null;
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("PolMapDistrictLDB", "PolMapDistrict_txtPlaceCode", "");
            ra.setAttribute("PolMapDistrictLDB", "PolMapDistrict_txtPlaceName", "");
            ra.setAttribute("PolMapDistrictLDB", "POL_MAP_ID_PLACE", null);
            return true;
        }

        /*
         * brise staru vrijednost u txtPlaceName ako se upisivala vrijednost u
         * txtPlaceCode i obratno, kako ne bi doslo do pogresnog povezivanja
         * sifre i imena mjesta kod poziva LookUp-a
         */
        if (ra.getCursorPosition().equals("PolMapDistrict_txtPlaceName")) {
            ra.setAttribute("PolMapDistrictLDB", "PolMapDistrict_txtPlaceCode", "");
        } else if (ra.getCursorPosition().equals("PolMapDistrict_txtPlaceCode")) {
            ra.setAttribute("PolMapDistrictLDB", "PolMapDistrict_txtPlaceName", "");
        }	

        if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }
        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", placeTypeId);

        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
        lookUpRequest.addMapping("PolMapDistrictLDB", "POL_MAP_ID_PLACE", "pol_map_id");
        lookUpRequest.addMapping("PolMapDistrictLDB", "PolMapDistrict_txtPlaceCode", "code");
        lookUpRequest.addMapping("PolMapDistrictLDB", "PolMapDistrict_txtPlaceName", "name");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        placeId = (java.math.BigDecimal) ra.getAttribute("PolMapDistrictLDB", "POL_MAP_ID_PLACE");

        if ((placeId != null)) {
            ra.refreshActionList("tblPolMapDistrict");
        }
        placeId = null;
        return true;

    }//PolMapDistrict_txtPlaceCode_FV

    public void details() {
        if (isTableEmpty("tblPolMapDistrict")) {
            ra.showMessage("wrn299");
            return;
        }

        ra.loadScreen("PolMapDistrictDialog", "scr_detail");
    }

    public void add() {
        if (!ra.isLDBExists("PolMapDistrictDialogLDB")) {
            ra.createLDB("PolMapDistrictDialogLDB");
        }
        ra.setAttribute("PolMapDistrictDialogLDB", "PolMapDistrictDialog_txtDistrictCode", null);
        ra.setAttribute("PolMapDistrictDialogLDB", "PolMapDistrictDialog_txtDistrictName", null);
        if (ra.getAttribute("PolMapDistrictLDB", "POL_MAP_ID_PLACE") == null) {
            ra.showMessage("wrnclt37");
            return;
        } else {
            ra.loadScreen("PolMapDistrictDialog", "scr_insert");
        }

    }//add

    public boolean isTableEmpty(String tableName) {
        hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra
                .getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty

    public void action() {
        if (isTableEmpty("tblPolMapDistrict")) {
            ra.showMessage("wrn299");
            return;
        }
        
        if (!ra.isLDBExists("PolMapDistrictDialogLDB")) {
            ra.createLDB("PolMapDistrictDialogLDB");
        }
        //Dohvat id od gradske cetvrti iz akcijske liste
        java.math.BigDecimal polMapIddDistrict = null;
        TableData td = (TableData) ra.getAttribute("PolMapDistrictLDB", "tblPolMapDistrict");
		java.util.Vector hiddenVector = (java.util.Vector) td.getSelectedRowUnique();
		polMapIddDistrict = (BigDecimal) hiddenVector.elementAt(0);
			
        //setiranje dohvacenog id na LDB
        ra.setAttribute("PolMapDistrictDialogLDB", "POL_MAP_IDD_DISTRICT", polMapIddDistrict);
       
        //setiranje tipa gradske cetvrti na LDB
        java.math.BigDecimal districtTypeId = new java.math.BigDecimal("5854877003.0");
        ra.setAttribute("PolMapDistrictDialogLDB", "POL_MAP_IDD_DISTRICT", districtTypeId);
       
        
        
        ra.loadScreen("PolMapDistrictDialog", "scr_action");
    }

    public void refresh() {
        ra.refreshActionList("tblPolMapDistrict");
    }
}