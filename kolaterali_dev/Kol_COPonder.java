package hr.vestigo.modules.collateral;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import hr.vestigo.modules.collateral.util.CollateralCmpUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.rba.util.DateUtils;

public class Kol_COPonder extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/Kol_COPonder.java,v 1.8 2014/10/07 10:54:19 hrazst Exp $";
    CollateralCmpUtil coll_cmp_util = null;
    CollateralUtil collUtil=null;
    
    public Kol_COPonder(ResourceAccessor ra) {
        super(ra);
        coll_cmp_util = new CollateralCmpUtil(ra);
        collUtil = new CollateralUtil(ra);
    }

    public void Kol_COPonderList_SE() {
        if (!(ra.isLDBExists("Kol_COPonderLDB"))) {
            ra.createLDB("Kol_COPonderLDB");
        }

        if (ra.isLDBExists("CollHeadLDB")) {
            ra.setAttribute("Kol_COPonderLDB", "Mvp_txtOldValue", ra.getAttribute("CollHeadLDB", "Kol_txtPonderMVP"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_hea_id", ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_typ_id", ra.getAttribute("CollHeadLDB", "COL_TYPE_ID"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_sub_typ_id", ra.getAttribute("CollSecPaperDialogLDB", "SEC_TYP_ID"));
        } else if (ra.isLDBExists("RealEstateDialogLDB")) {
            ra.setAttribute("Kol_COPonderLDB", "Mvp_txtOldValue", ra.getAttribute("RealEstateDialogLDB", "Kol_txtPonderMVP"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_hea_id", ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_HEA_ID"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_typ_id", ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_TYPE_ID"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_sub_typ_id", ra.getAttribute("RealEstateDialogLDB", "RealEstate_REAL_EST_TYPE"));
        } else if (ra.isLDBExists("CollBoeDialogLDB")) {
            ra.setAttribute("Kol_COPonderLDB", "Mvp_txtOldValue", ra.getAttribute("CollBoeDialogLDB", "Kol_txtPonderMVP"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_hea_id", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_HEA_ID"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_typ_id", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_TYPE_ID"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_sub_typ_id", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_BOE_TYPE_ID"));
        }
        ra.setAttribute("Kol_COPonderLDB", "Mvp_col_cat_id", ra.getAttribute("ColWorkListLDB", "col_cat_id"));
        //ra.createActionListSession("tblMvpPonder"); 
        try {
            ra.executeTransaction();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void Kol_WCAList_SE() {
        if (!(ra.isLDBExists("Kol_COPonderLDB"))) {
            ra.createLDB("Kol_COPonderLDB");
        }

        if (ra.isLDBExists("CollHeadLDB")) {
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_hea_id", ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));
        } else if (ra.isLDBExists("RealEstateDialogLDB")) {
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_hea_id", ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_HEA_ID"));
        } else if (ra.isLDBExists("CollBoeDialogLDB")) {
            ra.setAttribute("Kol_COPonderLDB", "Mvp_col_hea_id", ra.getAttribute("CollBoeDialogLDB", "CollBoeDialog_COL_HEA_ID"));
        }
        ra.createActionListSession("tblWCAList");
    }

    public boolean isTableEmpty(String tableName) {
        TableData td = (TableData) ra.getAttribute(tableName);
        if (td == null) return true;
        if (td.getData().size() == 0) return true;
        return false;
    }

    public void details() {

        TableData td = null;

        if (isTableEmpty("tblMvpPonder")) {
            ra.showMessage("wrn299");
            return;
        }
        td = (TableData) ra.getAttribute("Kol_COPonderLDB", "tblMvpPonder");

        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal col_pon_id = null;
        col_pon_id = (BigDecimal) hidden.elementAt(0);

        ra.setAttribute("Kol_COPonderLDB", "Mvp_col_pon_id", col_pon_id);
        ra.loadScreen("Kol_COPonder", "scr_detail");

    }

    public void add() {
        ra.setAttribute("Kol_COPonderLDB", "Mvp_col_pon_id", null);
        ra.setAttribute("Kol_COPonderLDB", "PONDER_TYPE", "MVP");
        ra.loadScreen("Kol_COPonder", "scr_insert");
    }
    
    public void add_ces_ponder() {
        ra.setAttribute("Kol_COPonderLDB", "Mvp_col_pon_id", null);
        ra.setAttribute("Kol_COPonderLDB", "PONDER_TYPE", "CES");
        ra.loadScreen("Kol_CESPonder", "scr_insert");
    }

    public void change() {
        TableData td = null;

        if (isTableEmpty("tblMvpPonder")) {
            ra.showMessage("wrn299");
            return;
        }
        td = (TableData) ra.getAttribute("Kol_COPonderLDB", "tblMvpPonder");

        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal col_pon_id = (BigDecimal) hidden.elementAt(0);
        
        Vector row = (Vector) td.getSelectedRowData();
        String status = (String) row.elementAt(0);
        Date datum_od = (Date) row.elementAt(1);

        // moze se mijenjati samo aktivan slog
        if (status.equalsIgnoreCase("N")) {
            ra.showMessage("wrnclt139");
            return;
        }
        ra.setAttribute("Kol_COPonderLDB", "Mvp_col_pon_id", col_pon_id);
        ra.setAttribute("Kol_COPonderLDB", "Mvp_status", status);
        ra.loadScreen("Kol_COPonder", "scr_change");
    }
    
    public void close_ces_ponder() {
        TableData td = null;

        if (isTableEmpty("tblCESPonder")) {
            ra.showMessage("wrn299");
            return;
        }
        td = (TableData) ra.getAttribute("Kol_COPonderLDB", "tblCESPonder");

        Vector hidden = (Vector) td.getSelectedRowUnique();
        BigDecimal col_pon_id = (BigDecimal) hidden.elementAt(0);

        Vector row = (Vector) td.getSelectedRowData();
        String status = (String) row.elementAt(0);
        Date datum_od = (Date) row.elementAt(1);

        // moze se mijenjati samo aktivan slog
        if (status.equalsIgnoreCase("N")) {
            ra.showMessage("wrnclt139");
            return;
        }
        ra.setAttribute("Kol_COPonderLDB", "Mvp_col_pon_id", col_pon_id);
        ra.setAttribute("Kol_COPonderLDB", "Mvp_status", status);
        ra.loadScreen("Kol_CESPonder", "scr_change");
    }
    

    public void Kol_COPonder_SE() {
        
        // pozvati transakciju za dohvat starog sloga ako postoji
        try {
            ra.executeTransaction();
        } catch (VestigoTMException vtme) {
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }

        String ponder_type=(String)ra.getAttribute("Kol_COPonderLDB", "PONDER_TYPE");
        if(ponder_type.equals("CES")){
            ra.setScreenTitle("Prijedlog ponder");
        }        
        
        if (ra.getScreenContext().equalsIgnoreCase("scr_insert")) {
            ra.setAttribute("Kol_COPonderLDB", "Mvp_org_uni_id", ra.getAttribute("GDB", "org_uni_id"));
            ra.setAttribute("Kol_COPonderLDB", "Mvp_use_id", ra.getAttribute("GDB", "use_id"));

            GregorianCalendar calendar = new GregorianCalendar();
            long timeT = calendar.getTime().getTime();
            Date todaySQLDate = new Date(timeT);

            ra.setAttribute("Kol_COPonderLDB", "Mvp_txtFrom", todaySQLDate);
            Date vvDatUntil = Date.valueOf("9999-12-31");
            ra.setAttribute("Kol_COPonderLDB", "Mvp_txtUntil", vvDatUntil);
        } else if (ra.getScreenContext().equalsIgnoreCase("scr_change")) {
            // ako je datum od jednak current, moze se mijenjati iznos pondera i datum do
            // ako je datum od manji od current, moze se mijenjati samo datum do
            Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
            Date date_from = (Date) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtFrom");

            // Stavi date until na juèerašnji datum
            // if (!(coll_cmp_util.cmp_dte(date_from, current_date))) {
            // ra.setContext("Mvp_txtNewValue", "fld_plain");
            // ra.setContext("Mvp_txtUntil", "fld_plain");
            // }
            // if ((date_from).before(current_date)) {
            // ra.setContext("Mvp_txtUntil", "fld_plain");
            // ra.setContext("Mvp_txtNewValue", "fld_change_protected");
            // }
            Date date_before = DateUtils.addOrDeductDaysFromDate(current_date, 1, false);
            
            ra.setAttribute("Kol_COPonderLDB", "Mvp_txtUntil", date_before);
            ra.setContext("Mvp_txtUntil", "fld_change_protected");
            ra.setContext("Mvp_txtNewValue", "fld_change_protected");
        }
    }

    public void confirm() {
        if (!(ra.isRequiredFilled())) {
            return;
        }
        // pitanje da li stvarno zeli potvrditi podatke
        Integer retValue = (Integer) ra.showMessage("col_qer004");
        if (retValue != null && retValue.intValue() == 0) return;

        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
        } catch (VestigoTMException vtme) {
            error("KolCOPonder -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
        // ako novi ponder vrijedi od danas, zamijeniti vrijednost na ekranu

        if (ra.isLDBExists("CollHeadLDB")) {
            ra.setAttribute("CollHeadLDB", "Kol_txtPonderMVP", ra.getAttribute("Kol_COPonderLDB", "Mvp_txtNewValue"));
        } else if (ra.isLDBExists("RealEstateDialogLDB")) {
            ra.setAttribute("RealEstateDialogLDB", "Kol_txtPonderMVP", ra.getAttribute("Kol_COPonderLDB", "Mvp_txtNewValue"));
        } else if (ra.isLDBExists("CollBoeDialogLDB")) {
            ra.setAttribute("CollBoeDialogLDB", "Kol_txtPonderMVP", ra.getAttribute("Kol_COPonderLDB", "Mvp_txtNewValue"));
        }

        if (((Integer) ra.getAttribute("GDB", "TransactionStatus")).equals(new Integer("0"))) {
            ra.exitScreen();
        }
    }

    public boolean Mvp_txtFrom_FV() {
        // datum od ne moze biti manji od current date i ne moze biti veci od
        // datuma do

        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtFrom");
        if (date_from == null || current_date == null) return true;
        if ((date_from).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }

        Date date_until = (Date) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtUntil");

        if (!(coll_cmp_util.cmp_dte(date_from, date_until))) {
            ra.showMessage("wrncltzst4");
            return false;
        }

        if (date_from == null || date_until == null) return true;
        if ((date_until).before(date_from)) {
            ra.showMessage("wrnclt105");
            return false;
        }

        return true;
    }

    public boolean Mvp_txtUntil_FV() {
        // datum do mora biti veci ili jednak datumu od
        // datum do moze biti D-1, D i bilo koji datum u buducnosti kod promjene

        Integer num_of_days = new Integer("1");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date date_from = (Date) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtFrom");
        Date date_until = (Date) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtUntil");
        Date day_before = coll_cmp_util.addOrDeductDaysFromDate(current_date, num_of_days, false);

        if ((date_until).before(date_from)) {
            ra.showMessage("wrnclt105");
            return false;
        }
        /*
         * if (!(coll_cmp_util.cmp_dte(date_from, date_until))) {
         * ra.showMessage("wrncltzst4"); return false; }
         */
        /*
         * if ((date_until).before(current_date)) { ra.showMessage("wrnclt125");
         * return false; }
         */

        if ((date_until).before(day_before)) {
            ra.showMessage("wrnclt166");
            return false;
        }

        return true;
    }

    public boolean Mvp_txtNewValue_FV() {
        // nova vrijednost pondera mora biti razlicita od stare i ne moze biti jednaka trenutno defaultnoj
        BigDecimal old_value = (BigDecimal) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtOldValue");
        BigDecimal new_value = (BigDecimal) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtNewValue");
        BigDecimal dfl_value = (BigDecimal) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtDflValue");
        BigDecimal min_value = (BigDecimal) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtMinValue");
        BigDecimal max_value = (BigDecimal) ra.getAttribute("Kol_COPonderLDB", "Mvp_txtMaxValue");

        if (!(coll_cmp_util.cmp_bdc(old_value, new_value))) {
            ra.showMessage("wrnclt138");
            return false;
        }
        // nova vrijednost mora biti unutar dozvoljenih granica
        if (new_value.compareTo(min_value) < 0) {
            ra.showMessage("wrnclt5");
            return false;
        }
        if (new_value.compareTo(max_value) > 0) {
            ra.showMessage("wrnclt6");
            return false;
        }
        if (!(coll_cmp_util.cmp_bdc(dfl_value, new_value))) {
            ra.showMessage("wrnclt141");
            return false;
        }
        return true;
    }
}
