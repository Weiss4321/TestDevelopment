/*
 * Created on 2006.04.03
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
 */
public class PolMapResiQuarDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/PolMapResiQuarDialog.java,v 1.7 2006/05/12 06:59:35 hrarmv Exp $";

    public PolMapResiQuarDialog(ResourceAccessor ra) {
        super(ra);
    }

    public void PolMapResiQuarDialog_SE() {
        if (!ra.isLDBExists("PolMapResiQuarDialogLDB")) {
            ra.createLDB("PolMapResiQuarDialogLDB");
        }

        String placeCode = (String) ra.getAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtPlaceCode");
        String placeName = (String) ra.getAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtPlaceName");
        BigDecimal POL_MAP_ID_PLACE = (BigDecimal) ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_PLACE");
        String districtCode = (String) ra.getAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtDistrictCode");
        String districName = (String) ra.getAttribute("PolMapResiQuarLDB", "PolMapResiQuar_txtDistrictName");
        BigDecimal POL_MAP_ID_DISTRICT = (BigDecimal) ra.getAttribute("PolMapResiQuarLDB", "POL_MAP_ID_DISTRICT");
        
        /* provjerava da li je odabrana akcija detalji,dodaj ili promijeni pa
         * popunjava fieldove PlaceCode, PlaceName i skriveni POL_MAP_IDD_PLACE,
         * te fieldove DistrictCode, DistrictName i skriveni
         * POL_MAP_IDD_DISTRICT, ResiQuarCode, ResiQuarName i skriveni
         * POL_MAP_IDD_RESI_QUAR
         */
        if ((ra.getScreenContext().compareTo("scr_detail") == 0)
                || (ra.getScreenContext().compareTo("scr_action") == 0)){
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtPlaceCode", placeCode);
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtPlaceName", placeName);
            ra.setAttribute("PolMapResiQuarDialogLDB", "POL_MAP_IDD_PLACE", POL_MAP_ID_PLACE);
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtDistrictCode", districtCode);
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtDistrictName", districName);
            ra.setAttribute("PolMapResiQuarDialogLDB", "POL_MAP_IDD_DISTRICT", POL_MAP_ID_DISTRICT);
            
            TableData tableData = (TableData) ra.getAttribute("PolMapResiQuarLDB", "tblPolMapResiQuar");
            //RowUnique sadrzi skrivene podatke iz tablice
            Vector hiddenVector = (Vector) tableData.getSelectedRowUnique();
            BigDecimal hidden = (BigDecimal) hiddenVector.elementAt(0);
            //RowData sadrzi vidljive podatke iz tablice
            Vector row = tableData.getSelectedRowData();
            String resiQuarCode = (String) row.elementAt(0);
            String resiQuarName = (String) row.elementAt(1);

            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtResiQuarCode", resiQuarCode);
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtResiQuarName", resiQuarName);
            ra.setAttribute("PolMapResiQuarDialogLDB", "POL_MAP_IDD_RESIQ", hidden);
            ra.setAttribute("PolMapResiQuarDialogLDB", "ResiQuar_USER_LOCK", (Timestamp) hiddenVector.elementAt(1));
        }
        
        /* provjerava da li je odabrana akcija dodaj pa
         * popunjava fieldove PlaceCode, PlaceName i skriveni POL_MAP_IDD_PLACE,
         * te fieldove DistrictCode, DistrictName i skriveni
         * POL_MAP_IDD_DISTRICT,
         */
        if ((ra.getScreenContext().compareTo("scr_insert") == 0)) {
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtPlaceCode", placeCode);
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtPlaceName", placeName);
            ra.setAttribute("PolMapResiQuarDialogLDB", "POL_MAP_IDD_PLACE", POL_MAP_ID_PLACE);
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtDistrictCode", districtCode);
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtDistrictName", districName);
            ra.setAttribute("PolMapResiQuarDialogLDB", "POL_MAP_IDD_DISTRICT", POL_MAP_ID_DISTRICT);
        }
    }
    
    /**
     * Metoda ne radi nista
     * @return
     */
    public boolean PolMapResiQuarDialog_FV() {
       /* String resiQuarName = (String) ra.getAttribute("PolMapResiQuarDialogLDB",
                "PolMapResiQuarDialog_txtResiQuarName");
        //String districtNameSC = null;

        if (resiQuarName != null && !resiQuarName.equals("")) {
            resiQuarName = resiQuarName.trim();
            ra.setAttribute("PolMapResiQuarDialogLDB", "PolMapResiQuarDialog_txtResiQuarName", resiQuarName);
            /*
            hr.vestigo.framework.util.scramble.Scramble scrambler = new hr.vestigo.framework.util.scramble.Scramble();
            districtNameSC = scrambler.doScramble(districtName);

            //postavlja scramblanu vrijednost u LDB
            ra.setAttribute("PolMapResiQuarDialogLDB", "RESIQUAR_NAME_SC", districtNameSC);
            */
            return true;
        /*} else {
            ra.showMessage("wrnclt38");
            return false;
        }*/

    }
    

    /**
     * Metoda poziva transakciju za dodavanje nove stambene cetvrti
     */
    public void confirm() {

        String resiQuarName = (String) ra.getAttribute("PolMapResiQuarDialogLDB",
                "PolMapResiQuarDialog_txtResiQuarName");
        String resiQuarCode = (String) ra.getAttribute("PolMapResiQuarDialogLDB",
                "PolMapResiQuarDialog_txtResiQuarCode");

        if (resiQuarName != null && resiQuarCode != null) {
            if (resiQuarName.equals("") || resiQuarCode.equals("")) {
                ra.showMessage("wrnclt39");
            } else {

                try {
                    ra.executeTransaction();
                    ra.showMessage("infclt2");
                    ra.exitScreen();
                    ra.refreshActionList("tblPolMapResiQuar");
                } catch (VestigoTMException vtme) {
                    error("RealEstateDialog -> insert(): VestigoTMException", vtme);
                    if (vtme.getMessageID() != null)
                        ra.showMessage(vtme.getMessageID());
                }

            }
        }
    }

    /**
     * Metoda poziva transakciju za azuriranje stambene cetvrti
     */
    public void change() {
        String resiQuarName = (String) ra.getAttribute("PolMapResiQuarDialogLDB",
                "PolMapResiQuarDialog_txtResiQuarName");
        String resiQuarCode = (String) ra.getAttribute("PolMapResiQuarDialogLDB",
                "PolMapResiQuarDialog_txtResiQuarCode");

        if (resiQuarName != null && resiQuarCode != null) {
            if (resiQuarName.equals("") || resiQuarCode.equals("")) {
                ra.showMessage("wrnclt39");
            } else {

                Integer del = (Integer) ra.showMessage("qer002");
                if (del != null && del.intValue() == 1) {
                    try {
                        ra.executeTransaction();
                        ra.showMessage("infclt3");
                    } catch (VestigoTMException vtme) {
                        error("PolMapResiQuarDialog -> insert(): VestigoTMException", vtme);
                        if (vtme.getMessageID() != null)
                            ra.showMessage(vtme.getMessageID());
                    }
                }
                ra.exitScreen();
                ra.refreshActionList("tblPolMapResiQuar");

            }
        }//scr_delete za transakciju
    }
    
   /**
    *  Metoda brise odabranu stambenu cetvrt iz tablice political map
    *
    */
    public void delete() {
       Integer del = (Integer) ra.showMessage("qer691");
        //provjerava da li korisnik zeli brisati gradsku cetvrt
        if (del != null && del.intValue() == 1) {
           try {
                ra.executeTransaction();
                ra.showMessage("infclt7");
            } catch (VestigoTMException vtme) {
                error("PolMapResiQuarDialog -> insert(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
           }
        }
        ra.exitScreen();
        ra.refreshActionList("tblPolMapResiQuar");
       //scr_delete za transakciju
    }


}