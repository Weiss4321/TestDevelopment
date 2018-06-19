/*
 * Created on 2006.03.30
 *
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;

/**
 * @author hrarmv
 *  
 */
public class PolMapDistrictDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/PolMapDistrictDialog.java,v 1.18 2006/05/05 14:21:21 hrarmv Exp $";

    public PolMapDistrictDialog(ResourceAccessor ra) {
        super(ra);
    }

    public void PolMapDistrictDialog_SE() {
        if (!ra.isLDBExists("PolMapDistrictDialogLDB")) {
            ra.createLDB("PolMapDistrictDialogLDB");
        }

        String placeCode = (String) ra.getAttribute("PolMapDistrictLDB", "PolMapDistrict_txtPlaceCode");
        String placeName = (String) ra.getAttribute("PolMapDistrictLDB", "PolMapDistrict_txtPlaceName");
        BigDecimal placeId = (BigDecimal) ra.getAttribute("PolMapDistrictLDB", "POL_MAP_ID_PLACE");
        /*
         * provjerava da li je odabrana akcija detalji,dodaj ili promijeni pa
         * popunjava fieldove PlaceCode, PlaceName i skriveni POL_MAP_IDD_PLACE
         */
        if ((ra.getScreenContext().compareTo("scr_detail") == 0)
                || (ra.getScreenContext().compareTo("scr_insert") == 0)
                || (ra.getScreenContext().compareTo("scr_action") == 0)) {
            ra.setAttribute("PolMapDistrictDialogLDB", "PolMapDistrictDialog_txtPlaceCode", placeCode);
            ra.setAttribute("PolMapDistrictDialogLDB", "PolMapDistrictDialog_txtPlaceName", placeName);
            ra.setAttribute("PolMapDistrictDialogLDB", "POL_MAP_IDD_PLACE", placeId);
        }

        /*
         * provjerava da li je odabrana akcija detalji ili akcija promijeni pa
         * popunjava fieldove DistrictCode, DistrictName i skriveni
         * POL_MAP_IDD_DISTRICT
         */
        if ((ra.getScreenContext().compareTo("scr_detail") == 0)
                || (ra.getScreenContext().compareTo("scr_action") == 0)) {
            TableData td = (TableData) ra.getAttribute("PolMapDistrictLDB", "tblPolMapDistrict");
            Vector hiddenVector = (Vector) td.getSelectedRowUnique();
            BigDecimal hidden = (BigDecimal) hiddenVector.elementAt(0);
            Vector row = td.getSelectedRowData();
            String districtCode = (String) row.elementAt(0);
            String districtName = (String) row.elementAt(1);

            ra.setAttribute("PolMapDistrictDialogLDB", "PolMapDistrictDialog_txtDistrictCode", districtCode);
            ra.setAttribute("PolMapDistrictDialogLDB", "PolMapDistrictDialog_txtDistrictName", districtName);
            ra.setAttribute("PolMapDistrictDialogLDB", "POL_MAP_IDD_DISTRICT", hidden);
            ra.setAttribute("PolMapDistrictDialogLDB", "District_USER_LOCK", (Timestamp) hiddenVector
                    .elementAt(1));

        }

    }//PolMapDistrictDialog_SE

    /**
     * Metoda ne radi nista
     * 
     * @return
     */
    public boolean PolMapDistrictDialog_txtDistrictName_FV() {
      /*  String districtName = (String) ra.getAttribute("PolMapDistrictDialogLDB",
                "PolMapDistrictDialog_txtDistrictName");
        //String districtNameSC = null;

        if (districtName != null && !districtName.equals("")) {
            districtName = districtName.trim();
            ra.setAttribute("PolMapDistrictDialogLDB", "PolMapDistrictDialog_txtDistrictName", districtName);
            
            /*
            hr.vestigo.framework.util.scramble.Scramble scrambler = new hr.vestigo.framework.util.scramble.Scramble();
            districtNameSC = scrambler.doScramble(districtName);

            //postavlja scramblanu vrijednost u LDB
            ra.setAttribute("PolMapDistrictDialogLDB", "DISTRICT_NAME_SC", districtNameSC);
            */
            return true;
      /*  } else {
            ra.showMessage("wrnclt38");
            return false;
        }*/

    }

    /**
     * Metoda poziva transakciju za dodavanje nove gradske cetvrti
     */
    public void confirm() {

        String districtName = (String) ra.getAttribute("PolMapDistrictDialogLDB",
                "PolMapDistrictDialog_txtDistrictName");
        String districtCode = (String) ra.getAttribute("PolMapDistrictDialogLDB",
                "PolMapDistrictDialog_txtDistrictCode");

        if (districtName != null && districtCode != null) {
            if (districtName.equals("") || districtCode.equals("")) {
                ra.showMessage("wrnclt39");
            } else {

                try {
                    ra.executeTransaction();
                    ra.showMessage("infclt2");
                    ra.exitScreen();
                    ra.refreshActionList("tblPolMapDistrict");
                } catch (VestigoTMException vtme) {
                    error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                    if (vtme.getMessageID() != null)
                        ra.showMessage(vtme.getMessageID());
                }

            }
        }
    }

    /**
     * Metoda poziva transakciju za azuriranje gradske cetvrti
     */
    public void change() {
        String districtName = (String) ra.getAttribute("PolMapDistrictDialogLDB",
                "PolMapDistrictDialog_txtDistrictName");
        String districtCode = (String) ra.getAttribute("PolMapDistrictDialogLDB",
                "PolMapDistrictDialog_txtDistrictCode");

        if (districtName != null && districtCode != null) {
            if (districtName.equals("") || districtCode.equals("")) {
                ra.showMessage("wrnclt39");
            } else {

                Integer del = (Integer) ra.showMessage("qer002");
                if (del != null && del.intValue() == 1) {
                    try {
                        ra.executeTransaction();
                        ra.showMessage("infclt3");
                    } catch (VestigoTMException vtme) {
                        error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                        if (vtme.getMessageID() != null)
                            ra.showMessage(vtme.getMessageID());
                    }
                }
                ra.exitScreen();
                ra.refreshActionList("tblPolMapDistrict");

            }
        }//scr_update za transakciju
    }
    
    /**
     *  Metoda brise odabranu stambenu cetvrt iz tablice political map
     *
     */
    public void delete() {
        Integer del = (Integer) ra.showMessage("qer690");
        //provjerava da li korisnik zeli brisati gradsku cetvrt
        if (del != null && del.intValue() == 1) {
            try {
                ra.executeTransaction();
                ra.showMessage("infclt4");
            } catch (VestigoTMException vtme) {
                error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
        }
        ra.exitScreen();
        ra.refreshActionList("tblPolMapDistrict");
        //scr_delete za transakciju
    }
}