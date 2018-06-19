package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.VestigoLookUpException;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.collateral.util.LookUps;

/**
 * @author HRASIA
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class CollHfPriorDialog extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollHfPriorDialog.java,v 1.96 2018/03/19 09:05:19 hraskd Exp $";

    boolean imaInsert = false;
    boolean imaUpdate = false;
    boolean fromSE=true; //flag koji nam pokazuje da li se kod izvrsava iz SE funkcije
    
    CollateralUtil coll_util = null;
    LookUps coll_lookups = null;
    private String MASTER_LDBNAME = "CollHfPriorDialogLDB";

    public CollHfPriorDialog(ResourceAccessor ra) {
        super(ra);
        coll_lookups = new LookUps(ra);
        coll_util = new CollateralUtil(ra);
    }

    public void CollHfPriorDialog_SE() {
        imaInsert = false;
        imaUpdate = false;
        fromSE=true;
        
        if (!(ra.isLDBExists(MASTER_LDBNAME))) {
            ra.createLDB(MASTER_LDBNAME);
        }
        if (!(ra.isLDBExists("CollHfPriorDialogLDB_B"))) {
            ra.createLDB("CollHfPriorDialogLDB_B");
        }
        if (!(ra.isLDBExists("CollHeadLDB"))){
            ra.createLDB("CollHeadLDB");
        }
        if (!(ra.isLDBExists("ColWorkListLDB"))){
            ra.createLDB("ColWorkListLDB");
        }     
        
        ra.setContext("CollHfPriorDialog_txtHfOwnFname", "fld_protected");
        // sakriti polja koja su za VRP
        this.hideVRPFields();
        // postavljanje konteksta za vozila za broj zakljucka
        String vrsta = (String) ra.getAttribute("ColWorkListLDB", "code");

        if (vrsta != null && (vrsta.equals("PLOV") || vrsta.equals("POKR") || vrsta.equals("ZALI"))) {
            coll_util.enableFields(new String[]{ "CollHFP_txtVehConNum", "Hf_txtRegIns" }, 0);
        } else if (vrsta != null && vrsta.equals("VOZI")) {
            coll_util.enableFields(new String[]{ "CollHFP_txtVehConNum", "Hf_txtRegIns" }, 0);
            coll_util.enableFields(new String[]{ "HfPror_lblConcNum", "HfPror_txtConcNum" }, 0);
            coll_util.enableFields(new String[]{ "HfPror_lblRegPlace", "HfPror_txtRegPlace" }, 0);
        } else if (vrsta != null && (vrsta.equals("DION") || vrsta.equals("OBVE") || vrsta.equals("UDJE") || vrsta.equals("ZAPI") ||  vrsta.equals("UDJP"))) {
            coll_util.enableFields(new String[]{ "CollHFP_txtVehConNum", "Hf_txtRegIns" }, 0);
            coll_util.enableFields(new String[]{ "HfPror_lblConcDate", "HfPror_txtConcDate" }, 0);            
            coll_util.enableFields(new String[]{ "HfPror_lblRegPlaceCou", "HfPror_lblRegPlace" }, 0);
            coll_util.enableFields(new String[]{ "HfPror_txtRegPlaceCou", "HfPror_txtRegPlaceCouName", "HfPror_txtRegPlace" }, 2);
        } else {
            coll_util.enableFields(new String[]{ "CollHFP_txtVehConNum", "Hf_txtRegIns"}, 2);
        }
        //ako se uslo sa ovim context-om onda je to context za insert
        if (ra.getScreenContext().equalsIgnoreCase("scr_changere")) {
            imaInsert = true;
        }
        //ako se uslo sa ovim context-om onda je to context za update
        if (ra.getScreenContext().equalsIgnoreCase("scr_updatere")) {
            imaUpdate = true;
        }
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail") || ra.getScreenContext().equalsIgnoreCase("scr_updatere")) {
            TableData td = (TableData) ra.getAttribute("CollHfPriorLDB", "tblCollHfPrior");
            Vector nevidljiviVector = (Vector) td.getSelectedRowUnique();
            // ID HIPOTEKE
            BigDecimal collHfPriorId = (BigDecimal) nevidljiviVector.elementAt(0);
            // ID KOLATERALA COLL_HEAD
            BigDecimal hfCollHeadId = (BigDecimal) nevidljiviVector.elementAt(1);
            // ID KORISNIKA PLASMANA IZ COLL_HEAD-a
            BigDecimal cusId = (BigDecimal) nevidljiviVector.elementAt(2);

            ra.setAttribute(MASTER_LDBNAME, "HF_TABLE_ID", (BigDecimal) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfTableSysCodeValue", (String) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeValue"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfTableSysCodeDesc", (String) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeDesc"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtColNum", (String) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_txtColNum"));
            ra.setAttribute(MASTER_LDBNAME, "HF_REF_ID", (BigDecimal) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_HF_REF_ID"));
            ra.setAttribute(MASTER_LDBNAME, "HF_COLL_HEAD_ID", (BigDecimal) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_HF_COLL_HEAD_ID"));
            ra.setAttribute(MASTER_LDBNAME, "COLL_HF_PRIOR_ID", collHfPriorId);

            String oldCtx=ra.getScreenContext();
            //postavlja se scr_detail context jer se na njemu dogada transakcija za dohvat detalja
            //a kada se izvrsi vraca se na ona context sa kojim se uslo u ekran
            ra.setScreenContext("scr_detail");
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            ra.setScreenContext(oldCtx);
        }          
        if (imaInsert) {
            // postaviti alikvotno i zaprotektirati
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtSyndicate", "N");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPrior_txtHowCowerSCV", "A");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfTableSysCodeValue", (String) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeValue"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfTableSysCodeDesc", (String) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeDesc"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtColNum", (String) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_txtColNum"));
            ra.setAttribute(MASTER_LDBNAME, "HF_REF_ID", (BigDecimal) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_HF_REF_ID"));
            ra.setAttribute(MASTER_LDBNAME, "HF_COLL_HEAD_ID", (BigDecimal) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_HF_COLL_HEAD_ID"));
            ra.setAttribute(MASTER_LDBNAME, "HF_TABLE_ID", (BigDecimal) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID"));

            String hfCurIdString = null;
            BigDecimal hfCurId = null;
            if (ra.getAttribute("CollHfPriorLDB", "HF_CUR_ID") != null) {
                hfCurId = (BigDecimal) ra.getAttribute("CollHfPriorLDB", "HF_CUR_ID");
                ra.setAttribute(MASTER_LDBNAME, "HF_CUR_ID", hfCurId);
                try {
                    hfCurIdString = coll_util.getCodeCharOfCurrency(hfCurId);
                } catch (java.sql.SQLException sqle) {
                } catch (hr.vestigo.framework.util.db.EmptyRowSet db_ers_e) { }
                if (hfCurIdString != null) {
                    hfCurIdString = hfCurIdString.trim();
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdCodeChar", hfCurIdString);
                }
            }
            // Dohvat USER_LOCK iz coll_head kako bi se azuriralo stanje u coll_head kod insert-a
            // Ulaz je col_hea_id iz coll_head HF_COLL_HEAD_ID
            // Izlaz je user_lock iz coll_head USER_LOCKCollHead
            // ChpDUserLock
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                error("Dohvat user_lock from coll_head: VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
        }
        if (imaUpdate) {
            ra.setContext("CollHfPrior_txtHowCowerSCV", "fld_protected");
            // ORIGINALNA VALUTA HIPOTEKE Ref, CUR_ID_REF AMOUNT_REF, EXC_RAT_REF EXC_RAT_REF_DATE
            // BEFORE
            // neiskoristeni bef = neiskoristeni tekuci dohvaceni
            // iskoristeni bef = iskoristeni tekuci dohvaceni
            // datum bef = neiskoristeni tekuci dohvaceni
            // AVAIL_BAMO_REF = AVAIL_AMO_REF
            // DRAW_BAMO_REF = HF_DRAW_AMO_REF
            // = HF_AVAIL_DAT
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailBAmoRef", (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailAmoRef"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDrawBAmoRef", (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDrawAmoRef"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailBDatRef", (Date) ra.getAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfAvailDatRef"));
            // CURRENT
            // neiskoristeni racuna se u transakciji
            // iskoristeni racuna se u transakciji
            // datum today HF_AVAIL_DAT
            // AVAIL_AMO_REF
            // HF_DRAW_AMO_REF
            // HF_AVAIL_DAT
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"CollHfPriorDialog_txtHfAvailAmoRef", "CollHfPriorDialog_txtHfDrawAmoRef", "CollHfPriorDialog_txtHfAvailDatRef" });
            // Iskoristeni se racuna kao suma od tri dijela (Racuna se u sqlj). Prvi je dio suma iznosa u valuti koja je ista kao i originalna valuta hipoteke
            // Drugi je dio suma iznosa u valuti kolaterala koja se po srednjem tecaju iskazuje u originalnoj valuti hipoteke
            // Treci je dio suma iznosa u valutama koje su razlicite od valute kolaterala i valute hipoteke koje se po srednjem tecaju iskazuju
            // u originalnoj valuti hipoteke
            // Neiskoristeni AVAIL_AMO_REF = AMOUNT_REF - HF_DRAW_AMO_REF Racuna
            // se u sqlj NOMINALNA VALUTA KOLATERALA HF_CUR_ID HF_AMOUNT
            // neiskoristeni bef = neiskoristeni tekuci dohvaceni
            // iskoristeni bef = iskoristeni tekuci dohvaceni
            // datum bef = neiskoristeni tekuci dohvaceni
            // AVAIL_BAMO = HF_AVAIL_AMO
            // DRAW_BAMO = HF_DRAW_AMO
            // AVAIL_BDAT = HF_AVAIL_DAT
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfAvailBAmo", (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailAmo"));
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfAvailBDat", (Date) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailDat"));
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfDrawBAmo", (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDrawAmo"));

            // CURRENT
            // neiskoristeni racuna se u transakciji
            // iskoristeni racuna se u transakciji
            // datum today HF_AVAIL_DAT
            // HF_AVAIL_AMO
            // HF_DRAW_AMO
            // HF_AVAIL_DAT
            coll_util.clearFields(MASTER_LDBNAME, new String[]{"CollHfPriorDialog_txtHfAvailAmo", "CollHfPriorDialog_txtHfDrawAmo", "CollHfPriorDialog_txtHfAvailDat" });           
        }        
        
        // Za CASH DEPOSIT POSTAVKA
        BigDecimal HfTableIdBdCashDep = new BigDecimal("1609188003.0");
        BigDecimal HfTableIdRealEstate = new BigDecimal("1602609003.0");
        BigDecimal HfTableIdVehicle = new BigDecimal("1602610003.0");
        BigDecimal HfTableIdVessel = new BigDecimal("1602611003.0");
        BigDecimal HfTableIdMovable = new BigDecimal("1602612003.0");
        BigDecimal HfTableIdSupply = new BigDecimal("1602613003.0");
        BigDecimal HfTableIdArt = new BigDecimal("1602614003.0");
        BigDecimal HfTableIdPrecious = new BigDecimal("1602615003.0");
        BigDecimal HfTableIdSecurity = new BigDecimal("1604184003.0");
        BigDecimal tablicaKoja = (BigDecimal) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID");

        String valuta = (String) ra.getAttribute("CollHeadLDB", "Coll_txtNmValCurr");
        BigDecimal valutaId = (BigDecimal) ra.getAttribute("CollHeadLDB", "REAL_EST_NM_CUR_ID");

        if (tablicaKoja != null) {
            valuta = (String) ra.getAttribute("CollHeadLDB", "Coll_txtNmValCurr");
            valutaId = (BigDecimal) ra.getAttribute("CollHeadLDB", "REAL_EST_NM_CUR_ID");
            if (tablicaKoja.compareTo(HfTableIdBdCashDep) == 0) {
                // Za CASH DEPOSIT POSTAVKA
                BigDecimal iznosDepozita = (BigDecimal) ra.getAttribute("CollHeadLDB", "Coll_txtNomiValu");
                // ORIGINALNA
                // CollHfPriorDialog_txtAmountRef,prot, CollHfPriorDialog_txtHfCurIdRefCodeChar, CUR_ID_REF
                // ovo samo za insert
                if (ra.getScreenContext().equalsIgnoreCase("scr_changere")) {
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtAmountRef", iznosDepozita);
                    ra.setAttribute(MASTER_LDBNAME, "CUR_ID_REF",valutaId);
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdRefCodeChar", valuta);
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAmount", iznosDepozita);
                    
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfHfcSysCodeValue", "P");
                    ra.invokeValidation("CollHfPriorDialog_txtHfHfcSysCodeValue");
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPrior_txtHowCowerSCV", "O");
                    ra.invokeValidation("CollHfPrior_txtHowCowerSCV");
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateHfcUntil", ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtCdeDepUnti"));
                }
                // U VALUTI KOLATERALA
                // CollHfPriorDialog_txtHfCurIdCodeChar, HF_CUR_ID
                ra.setAttribute(MASTER_LDBNAME, "HF_CUR_ID", valutaId);
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdCodeChar", valuta);

                // u korist RBA moze biti samo jedna aktivna hipoteka
                //System.out.println("1. tu sam da li postoji aktivna RBA hipoteka");
                // samo za unos novog !!!
                if (isTableOneRowRBA("tblCollHfPrior") && ra.getScreenContext().equalsIgnoreCase("scr_changere")) {
                    // System.out.println("2. tu sam postoji aktivna RBA hipoteka");
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue", "F");
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRegisterNo", "910167");
                    coll_util.enableFields(new String[]{"CollHfPriorDialog_txtHfRecLopSysCodeValue","CollHfPriorDialog_txtHfRecLopSysCodeDesc","CollHfPriorDialog_txtHfRegisterNo"}, 1);                    
                    coll_util.clearFields(MASTER_LDBNAME,new String[]{ "CollHfPriorDialog_txtHfRecLopSysCodeDesc", "CollHfPriorDialog_txtHfOwnLname", "CollHfPriorDialog_txtHfOwnCode" });
                    ra.invokeValidation("CollHfPriorDialog_txtHfRecLopSysCodeValue");
                    ra.invokeValidation("CollHfPriorDialog_txtHfRegisterNo");
                }                

                ra.setRequired("CollHfPriorDialog_txtHfDateHfcUntil", true);
                coll_util.enableField("CollHfPriorDialog_txtHfRegisterNo", 1);
                coll_util.enableFields(new String[]{"CollHfPriorDialog_txtHfOwnLname","CollHfPriorDialog_txtHfOwnFname","CollHfPriorDialog_txtHfOwnCode",
                        "CollHfPriorDialog_txtHfHfcSysCodeValue", "CollHfPriorDialog_txtHfHfcSysCodeDesc" }, 1);
                nakonIznosaOriginalne();
                // nakonValuteOriginalne();
            } else if (tablicaKoja.compareTo(HfTableIdSecurity) == 0) {
                // VRP,
                BigDecimal rbaCusId = new BigDecimal("8218251.0");
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRegisterNo", "910000");
                //brisem naziv koji je dohvacen iz transakcije, jer je zapisano krivo ime u bazi u polju hf_own_lname=RAIFFEISENBANK AUSTRIA D.D. ZAGREB.
                //Radi se validacija na polju register_no-a i podaci o owneru se popune preko lookup-a                
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnLname", "");
                ra.invokeValidation("CollHfPriorDialog_txtHfRegisterNo");
                //ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnLname", "RAIFFEISENBANK AUSTRIA D.D. ZAGREB");
                //ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnFname", null);
                //ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnCode", "00901717");
                //ra.setAttribute(MASTER_LDBNAME, "HF_OWN_CUS_ID", rbaCusId);
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue", "R");
                ra.invokeValidation("CollHfPriorDialog_txtHfRecLopSysCodeValue");
                //ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeDesc", "Pravo upisano u korist Raiffeisenbank Austria d.d. Zagreb");
                //ra.setAttribute(MASTER_LDBNAME, "HF_REC_LOP_ID",new BigDecimal("1602772003.0"));
                Date maturity_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtMaturityDate");
                // napuniti vazenje tereta do sa datumo dospijeæa VRP-a i protektirati za unos;
                if (ra.getScreenContext().equalsIgnoreCase("scr_changere") && maturity_date != null && !maturity_date.toString().equals("9999-12-31")) {
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateHfcUntil", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMaturityDate"));
                    ra.setContext("CollHfPriorDialog_txtHfDateHfcUntil","fld_change_protected");
                } else {
                    ra.setContext("CollHfPriorDialog_txtHfDateHfcUntil", "fld_plain");
                }
                ra.setRequired("CollHfPriorDialog_txtHfDateHfcUntil", true);
                // zablokirati
                coll_util.enableFields(new String[]{"CollHfPriorDialog_txtHfRegisterNo", "CollHfPriorDialog_txtHfOwnLname", "CollHfPriorDialog_txtHfOwnFname",
                         "CollHfPriorDialog_txtHfOwnCode","CollHfPriorDialog_txtHfRecLopSysCodeValue", "CollHfPriorDialog_txtHfRecLopSysCodeDesc" }, 2);
                nakonIznosaOriginalne();
            } else if (tablicaKoja.compareTo(HfTableIdVessel) == 0 || tablicaKoja.compareTo(HfTableIdVehicle) == 0
                    || tablicaKoja.compareTo(HfTableIdMovable) == 0 || tablicaKoja.compareTo(HfTableIdSupply) == 0
                    || tablicaKoja.compareTo(HfTableIdArt) == 0 || tablicaKoja.compareTo(HfTableIdPrecious) == 0) {
                // U VALUTI KOLATERALA
                // CollHfPriorDialog_txtHfAmount, CollHfPriorDialog_txtHfCurIdCodeChar, HF_CUR_ID
                ra.setAttribute(MASTER_LDBNAME, "HF_CUR_ID", valutaId);
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdCodeChar", valuta);
                nakonIznosaOriginalne();
            } else if (tablicaKoja.compareTo(HfTableIdRealEstate) == 0) {
                valuta = (String) ra.getAttribute("RealEstateDialogLDB", "RealEstate_txtNmCurIdCodeChar");
                valutaId = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_REAL_EST_NM_CUR_ID");
                // U VALUTI KOLATERALA
                // CollHfPriorDialog_txtHfAmount, CollHfPriorDialog_txtHfCurIdCodeChar, HF_CUR_ID
                ra.setAttribute(MASTER_LDBNAME, "HF_CUR_ID", valutaId);
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdCodeChar", valuta);
                nakonIznosaOriginalne();
            }
        }
        // Milka, 04.01.2007 - validirati polje upis u korist
        // ako je hipoteka u korist RBA polje vazenje terete do mora niti
        // obavezno za unos
        // ako je hipoteka u korist RBA moze biti dio okvirnog sporazuma
        // Milka, 03.03.2007 - ako je hipoteka dio okvirnog sporazuma postaviti
        // kontekste
        String sporazum = (String) ra.getAttribute(MASTER_LDBNAME, "Kol_txtFrameAgr");
        System.out.println("sporazum="+sporazum);
        if (ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue") != null) {
            String HfRecLopSCV = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue");
            HfRecLopSCV = HfRecLopSCV.trim();
            if (HfRecLopSCV.compareTo("R") == 0) {
                coll_util.setRequired(new String[]{"CollHfPriorDialog_txtHfDateHfcUntil", "Kol_txtFrameAgr"}, true);
                ra.setRequired("Agr_txtAgrNo1", false);
                ra.setContext("Kol_txtFrameAgr", "fld_plain");               

                if (sporazum != null && sporazum.equalsIgnoreCase("D")) {
                    ra.setContext("Agr_txtAgrNo1", "fld_plain");                    
                    ra.setRequired("Agr_txtAgrNo1", true);
                    coll_util.enableFields(new String[]{"CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar", "CollHfPriorDialog_txtHfDateHfcUntil"}, 1);
                } else {
                    ra.setContext("Agr_txtAgrNo1", "fld_protected");
                    ra.setRequired("Agr_txtAgrNo1", false);
                    coll_util.enableFields(new String[]{"CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar", "CollHfPriorDialog_txtHfDateHfcUntil"}, 0);
                }
            } else {
                coll_util.setRequired(new String[]{"CollHfPriorDialog_txtHfDateHfcUntil","Kol_txtFrameAgr","Agr_txtAgrNo1"}, false);
                ra.setContext("Kol_txtFrameAgr", "fld_protected");
            }
        }
        //ako je sinducirana hipoteka onda se valuta sinducirang dijela hipoteke CollHfPriorDialog_txtSyndicateCurCode popunjava iz CollHfPriorDialog_txtHfCurIdRefCodeChar 
        //jer je valuta ista samo se radi vizuale ispisuje i na jednom i drugom mjestu
        if(((String) ra.getAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtSyndicate")).equals("D")){
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtSyndicateCurCode", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdRefCodeChar"));
        }
        
        // Milka, 03.01.2007 - validirati polje da li je upisano pravo banke
        if (ra.getAttribute(MASTER_LDBNAME, "CollHFP_txtRecLop") != null && !ra.getScreenContext().equals("scr_detail")) {
            ra.invokeValidation("CollHFP_txtRecLop");
            ra.setCursorPosition("Kol_txtFrameAgr");
        }
        // Milka, 20.05.2008 - za vozila
        // provjeriti da li je upisan datum upisa prava banke i postaviti konteks
        // validiram datum upisa prava banke
        if (vrsta != null && vrsta.equals("VOZI")) {
            ra.invokeValidation("CollHFP_txtDateRecLop");
            ra.setCursorPosition("Kol_txtFrameAgr");
        }
        // Milka, 04.12.2008 - validirati polje za alikvotno
        if (ra.getAttribute(MASTER_LDBNAME, "CollHfPrior_txtHowCowerSCV") != null) {
            ra.invokeValidation("CollHfPrior_txtHowCowerSCV");
            ra.setContext("CollHfPrior_txtHowCowerSCV", "fld_change_protected");            
            ra.setContext("CollHfPrior_txtHowCowerSCV", "fld_protected");
        }
        // ZA DETALJE ZAPROTEKTIRATI 
        if (ra.getScreenContext().equalsIgnoreCase("scr_detail")) {
            coll_util.enableFields(new String[]{"CollHFP_txtVehConNum","Hf_txtRegIns","CollHFP_txtDateRecLop",                      
                "Kol_txtFrameAgr","Agr_txtAgrNo1","CollHfPriorDialog_txtAmountRef",                 
                "CollHfPriorDialog_txtHfCurIdRefCodeChar","CollHfPriorDialog_txtHfDateHfcUntil",    
                "CollHFP_txtDateToLop","HfPror_txtConcNum","HfPror_txtRegPlace","HfPror_txtConcDate"}, 2);
            coll_util.enableFields(getSyndicateFields(),2);
        }
        ra.setCursorPosition("CollHfPriorDialog_txtHfRecLopSysCodeValue");
        ra.invokeValidation("CollHfPriorDialog_txtSyndicate");
        fromSE=false;
    }

    public boolean CollHfPriorDialog_txtAmountRef_FV() {
        postavi_neke();
        boolean rez = izracunOriginalneValuteIIznose();
        if(!rez) ra.showMessage("wrnclt36");
        return rez;        
    }

    public void nakonIznosaOriginalne() {
        postavi_neke();
        izracunOriginalneValuteIIznose();
    }
    
    //Napravio metodu iz razloga sto se isti kod duplirao u hrpi metoda
    /**Ako je uspio odraditi if dio pri postavljanju atributa kada je insert ili update hipoteke vraca true inace vraca false.
     * Za detalje vraca true...
     */    
    private boolean izracunOriginalneValuteIIznose(){
        Date todaySQLDate = null;
        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new Date(timeT);

        BigDecimal originalAmount = null;
        BigDecimal numberZero = new BigDecimal("0.00");
        BigDecimal curIdRef = null;
        BigDecimal exchRateRef = null;
        BigDecimal hfCurId = null;
        BigDecimal exchRate = null;
        BigDecimal otherAmount = null;

        if (imaUpdate || imaInsert) {
            originalAmount = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtAmountRef");
            if(originalAmount != null){
                if(imaInsert){ 
                    // postavka neiskoristenog iznosa i datuma
                    // postavka iskoristenog iznosa
                    // postavka neiskoristenog iznosa prethodnog perioda i datuma
                    // postavka iskoristenog iznosa prethodnog perioda
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailAmoRef", originalAmount);
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailDatRef", todaySQLDate);
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDrawAmoRef", numberZero);
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailBAmoRef", originalAmount);
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAvailBDatRef", todaySQLDate);
                    ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDrawBAmoRef", numberZero);
                }
                if (ra.getAttribute(MASTER_LDBNAME, "HF_CUR_ID") != null) {
                    hfCurId = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "HF_CUR_ID");
                }
                if (ra.getAttribute(MASTER_LDBNAME, "CUR_ID_REF") != null) {
                    curIdRef = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "CUR_ID_REF");
                }
                try {                                                                                 
                    if (coll_util.getCurrentExchRate(todaySQLDate, curIdRef)) {                       
                        exchRateRef = coll_util.getMiddRate();                                        
                    }                                                                                 
                } catch (java.sql.SQLException sqle) {                                                
                } catch (hr.vestigo.framework.util.db.EmptyRowSet db_ers_e) { }                       
                                                                                                      
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtExcRatRef", exchRateRef);       
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtExcRatRefDate", todaySQLDate);  
                
                if (curIdRef != null && hfCurId != null) {
                    if (curIdRef.compareTo(hfCurId) == 0) {
                        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAmount", originalAmount);
                    } else {
                        // DOHVAT TECAJA ORIGINALNE VALUTE HIPOTEKE
                        try {
                            if (coll_util.getCurrentExchRate(todaySQLDate,curIdRef)) {
                                exchRateRef = coll_util.getMiddRate();
                            }
                        } catch (java.sql.SQLException sqle) {
                        } catch (hr.vestigo.framework.util.db.EmptyRowSet db_ers_e) {}
                        // DOHVAT TECAJA VALUTE KOLATERALA
                        try {
                            if (coll_util.getCurrentExchRate(todaySQLDate,hfCurId)) {
                                exchRate = coll_util.getMiddRate();
                            }
                        } catch (java.sql.SQLException sqle) {
                        } catch (hr.vestigo.framework.util.db.EmptyRowSet db_ers_e) {}
    
                        if ((exchRateRef != null) && (exchRate != null)) {
                            otherAmount = originalAmount.multiply(exchRateRef);
                            otherAmount = otherAmount.divide(exchRate, 2,BigDecimal.ROUND_HALF_UP);
                            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAmount", otherAmount);
                        }
                    }
                }
                return true;
            }else{
                coll_util.clearField(MASTER_LDBNAME, "CollHfPriorDialog_txtHfAmount");
                return false;
            }
        }else{
            return true;
        }
    }

//    public void nakonValuteOriginalne() {
//        izracunOriginalneValuteIIznose();
//    }

    public boolean CollHfPriorDialog_txtHfCurIdRefCodeChar_FV(String ElName,Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdRefCodeChar", "");
            ra.setAttribute(MASTER_LDBNAME, "CUR_ID_REF", null);
            return true;
        }

        ra.setAttribute(MASTER_LDBNAME, "dummySt", "");
        LookUpRequest lookUpRequest = new LookUpRequest("CollCurrencyLookUp");
        lookUpRequest.addMapping(MASTER_LDBNAME, "CUR_ID_REF", "cur_id");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "code_num");
        lookUpRequest.addMapping(MASTER_LDBNAME,"CollHfPriorDialog_txtHfCurIdRefCodeChar", "code_char");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "name");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        boolean rez = izracunOriginalneValuteIIznose();
        if(!rez) ra.showMessage("wrnclt36");
        return rez; 
    }

    public boolean CollHfPriorDialog_HfAmount_FV() {
        // IZNOS HIPOTEKE U VALUTI KOLATERALA
        // HF_AMOUNT decimal(17,2) no Iznos upisane hipoteke, fiducije za danu
        // nekretninu, plovilo, ...u valuti nominalne vrijednosti kolaterala

        // NE MOZE SE UPISATI VEC SE IZRACUNAVA NA TEMELJU IZNOSA U ORIGINALNOJ
        // VALUTI

        return true;
    }// CollHfPriorDialog_HfAmount_FV

    public boolean CollHfPriorDialog_txtHfCurIdCodeChar_FV(String ElName,
            Object ElValue, Integer LookUp) {
        // NOMINALNA VALUTA KOLATERALA
        // HF_CUR_ID decimal(16,0) no F(CURRENCY) Nominalna valuta kolaterala
        // Postavlja se u CollHfPrior_SE u CollHfPrior.java
        return true;

    }// CollHfPriorDialog_txtHfCurIdCodeChar_FV

    public boolean CollHfPrior_txtHowCowerSCV_FV(String elementName,
            Object elementValue, Integer lookUpType) {
        // Nacin pokrica plasmana:CollHfPrior_lblHowCower
        // CollHfPrior_HOW_COWER
        // CollHfPrior_txtHowCowerSCV
        // CollHfPrior_txtHowCowerSCD

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPrior_HOW_COWER",
                    null);
            ra.setAttribute(MASTER_LDBNAME,
                    "CollHfPrior_txtHowCowerSCV", "");
            ra.setAttribute(MASTER_LDBNAME,
                    "CollHfPrior_txtHowCowerSCD", "");
            return true;
        }

        ra.setAttribute(MASTER_LDBNAME, "SysCodId", "clt_hf_cover");
        ra.setAttribute(MASTER_LDBNAME, "CollHfPrior_txtHowCowerSCD",
                "");

        if (!(ra.isLDBExists("SysCodeValueNewLookUpLDB")))
            ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id",
                "clt_hf_cover");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping(MASTER_LDBNAME,
                "CollHfPrior_txtHowCowerSCV", "sys_code_value");
        request.addMapping(MASTER_LDBNAME,
                "CollHfPrior_txtHowCowerSCD", "sys_code_desc");
        request.addMapping(MASTER_LDBNAME, "CollHfPrior_HOW_COWER",
                "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        return true;
    } // CollHfPrior_txtHowCowerSCV_FV

    public boolean CollHfPrior_txtIsPartAgreem_FV(String elementName,Object elementValue, Integer lookUpType) {
        // Je li dio sporazuma - CollHfPrior_txtIsPartAgreem
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME,
                    "CollHfPrior_txtIsPartAgreem", null);
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");

        request.addMapping(MASTER_LDBNAME, "CollHfPrior_txtIsPartAgreem", "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }

        return true;
    } // CollHfPrior_txtIsPartAgreem_FV

    public void confirm() {
        imaInsert = false;       
        if (!(ra.isRequiredFilled())) {
            return;
        }

        try {
            ra.executeTransaction();
        } catch (VestigoTMException vtme) {
            error("CollHfPriorDialog -> UPDATE(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }

        if ((ra.getScreenContext().equalsIgnoreCase("scr_changere"))) {
            imaInsert = true;

        }
        // forsirati povezivanje hipoteke s plasmanom samo ako je upisano pravo
        // banke
        String upisanoPravo = "";
        String sporazum = (String) ra.getAttribute(MASTER_LDBNAME,"Kol_txtFrameAgr");

        // da li je upisano pravo banke - iskljuceno privremeno, 09.12.2006,
        // forsira se povezivanje s plasmanom ako je hipoteka u korist RBA bez
        // da je jos upisano pravo banke
        ra.exitScreen();
        ra.refreshActionList("tblCollHfPrior");

        // Milka, 26.04.2007 - izbaceno forsiranje povezivanja s plasmanom zbog
        // deaktiviranih hipoteka
        if (imaInsert) {
            imaInsert = false;
            TableData tdin = (TableData) ra.getAttribute("CollHfPriorLDB", "tblCollHfPrior");
            Vector vidljiviVector = null;
            Vector hidden = null;
            // koliko redaka ima tabela
            int broj_redaka = tdin.getData().size();
            // pozicioniram se na zadnji redak
            // pronaci zadnju aktivnu hipoteku-ona ciji prioritet je najvisi a razlicit od NA
            for(int k = (broj_redaka - 1); k >= 0; k--){
                tdin.setSelectedRow(k);
                vidljiviVector = (Vector) tdin.getSelectedRowData();
                String mortgage_status = (String) vidljiviVector.elementAt(0);
                // System.out.println("K.....STATUS HIPOTEKE: " + k + "...."+ mortgage_status + "|");
                if (!mortgage_status.equalsIgnoreCase("NA")) {
                    break;
                }
            }
            hidden = (Vector) tdin.getSelectedRowUnique();
            upisanoPravo = (String) hidden.elementAt(4);
            String interIdBen = (String) vidljiviVector.elementAt(3);
            // System.out.println("interIdBen |" + interIdBen + "|");
            // System.out.println("UPISANO PRAVO BANKE |" + upisanoPravo + "|");
            if (interIdBen != null) {
                interIdBen = interIdBen.trim();
            }
            // System.out.println("HIPOTEKA JE OD |" + interIdBen + "|");
            // if((interIdBen.compareTo("910000") == 0) && (upisanoPravo != null && (upisanoPravo.compareTo("D") ==0))){
            if (interIdBen.compareTo("910000") == 0) {
                // ako je hipoteka vezana za okvirni sporazum ovdje se ne povezuje s plasmanima
                if (sporazum != null && sporazum.equalsIgnoreCase("D")) {
                    ra.showMessage("wrnclt130");
                } else {
                    ra.invokeAction("loan_ben");
                }
            }
        }
    }// confirm

    public void delete() {
        // pitanje da li zelite obrisati hipoteku
        // ra.showMessage("inf_1800");
        Integer retValue = (Integer) ra.showMessage("col_qer022");
        if (retValue != null) {
            if (retValue.intValue() == 0) {
                return;
            } else {
                try {
                    ra.executeTransaction();// MortgageDelete
                    ra.showMessage("infcltzst1");
                } catch (VestigoTMException vtme) {
                    error("CollHfPriorDialog -> delete(): VestigoTMException", vtme);
                    if (vtme.getMessageID() != null) ra.showMessage(vtme.getMessageID());
                }
            }
        }
        ra.exitScreen();
        ra.refreshActionList("tblCollHfPrior");
    }

    public boolean CollHfPriorDialog_CollCourt_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtCoCode", "");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtCoName", "");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_COURT_ID", null);
            ra.setAttribute(MASTER_LDBNAME, "dummyBD", null);
            ra.setAttribute(MASTER_LDBNAME, "dummyDate", null);
            return true;
        }

        LookUpRequest lookUpRequest = new LookUpRequest("CollCourtLookUp");
        lookUpRequest.addMapping(MASTER_LDBNAME, "CollHfPriorDialog_COURT_ID", "co_id");
        lookUpRequest.addMapping(MASTER_LDBNAME, "CollHfPriorDialog_txtCoCode", "co_code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "CollHfPriorDialog_txtCoName", "co_name");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummyBD", "co_pol_map_id_cnt");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummyDate", "co_date_from");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummyDate", "co_date_until");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtCoCode")) {
            ra.setCursorPosition(2);
        }
        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtCoName")) {
            ra.setCursorPosition(1);
        }
        ra.setAttribute(MASTER_LDBNAME, "dummyBD", null);
        ra.setAttribute(MASTER_LDBNAME, "dummyDate", null);
        return true;
    }// CollHfPriorDialog_CollCourt_FV

    public boolean CollHfPriorDialog_HfRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = MASTER_LDBNAME;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRegisterNo", "");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnCode", "");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnLname", "");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOIB", "");
            ra.setAttribute(MASTER_LDBNAME, "HF_OWN_CUS_ID", null);

            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue", "");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeDesc", "");
            ra.setAttribute(MASTER_LDBNAME, "HF_REC_LOP_ID", null);
            return true;
        }

        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfOwnLname")) {
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfRegisterNo", "");
        } else if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfRegisterNo")) {
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfOwnLname", "");
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfOwnLname") != null) {
            d_name = (String) ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfOwnLname");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfRegisterNo") != null) {
            d_register_no = (String) ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfRegisterNo");
        }

        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }

        // JE LI zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }

        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfRegisterNo"))
            ra.setCursorPosition(4);
        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfOwnLname"))
            ra.setCursorPosition(1);

        if (ra.isLDBExists("CustomerAllCitizenLookUpLDB_1")) {// ovo ne dirati
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", "");
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "code", "");
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", "");
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "add_data_table", "");
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "cus_typ_id", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "cus_sub_typ_id", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "eco_sec", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "residency_cou_id", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "fname", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "surname", null);
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "status", "");
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "cocunat", "");
            ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "oib_lk", "");
        } else {
            ra.createLDB("CustomerAllCitizenLookUpLDB_1");
        }

        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "register_no", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRegisterNo"));
        ra.setAttribute("CustomerAllCitizenLookUpLDB_1", "name", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnLname"));

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllCitizenLookUp_22"); // ni ovo ne dirati
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "register_no", "register_no");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "code", "code");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "name", "name");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "residency_cou_id", "residency_cou_id");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "fname", "fname");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "surname", "surname");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "status", "status");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "cocunat", "cocunat");
        lookUpRequest.addMapping("CustomerAllCitizenLookUpLDB_1", "oib_lk", "oib_lk");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute(MASTER_LDBNAME, "HF_OWN_CUS_ID", (BigDecimal) ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "cus_id"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRegisterNo", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "register_no"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnLname", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "name"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnCode", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "code"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOIB", ra.getAttribute("CustomerAllCitizenLookUpLDB_1", "oib_lk"));

        BigDecimal brojKomitenta = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "HF_OWN_CUS_ID");
        BigDecimal rbaCusId = new BigDecimal("8218251.0");
        BigDecimal pravoZaRba = new BigDecimal("1602772003.0");
        BigDecimal pravoZaDruge = new BigDecimal("1602773003.0");

        if(!fromSE){
            String u_korist = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue");
    
            if (brojKomitenta != null) {
                if (brojKomitenta.compareTo(rbaCusId) == 0) {
                    if (u_korist == null || !u_korist.trim().equalsIgnoreCase("R")) {
                        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue", "");
                        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeDesc", "");
                        ra.setAttribute(MASTER_LDBNAME, "HF_REC_LOP_ID", null);
                        ra.setAttribute("CollHFP_txtRecLop", "");
                    }
                } else {
                    if (u_korist != null && u_korist.trim().equalsIgnoreCase("R")) {
                        ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfRecLopSysCodeValue", "");
                        ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfRecLopSysCodeDesc", "");
                        ra.setAttribute(MASTER_LDBNAME, "HF_REC_LOP_ID",null);
                    }
                }
            }
        }
        ldbName = null;
        d_register_no = null;
        d_name = null;
        return true;
    }// CollHfPriorDialog_HfRegisterNo_FV

    public boolean CollHfPriorDialog_HfOwnLname_FV() {
        String lName = null;
        lName = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOwnLname");
        if (lName == null || lName.trim().equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnLname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnFname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnCode", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_OWN_CUS_ID", null);
        }
        return true;
    }// CollHfPriorDialog_HfOwnLname_FV

    public boolean CollHfPriorDialog_HfOwnFname_FV() {
        String fName = null;

        fName = (String) ra.getAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnFname");
        if (fName == null || fName.trim().equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnLname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnFname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnCode", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_OWN_CUS_ID", null);
        }
        return true;
    }// CollHfPriorDialog_HfOwnFname_FV

    public boolean CollHfPriorDialog_HfOwnCode_FV() {
        String ownCode = null;

        ownCode = (String) ra.getAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnCode");
        if (ownCode == null || ownCode.trim().equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnLname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnFname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOwnCode", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_OWN_CUS_ID", null);
        }
        return true;
    }// CollHfPriorDialog_HfOwnCode_FV

    public boolean CollHfPriorDialog_HfOffildRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = MASTER_LDBNAME;
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOffildRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOffildLname", null);
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOffildFname", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_OFFI_LRD", null);
            return true;
        }
        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfOffildLname")) {
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfOffildFname", "");
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfOffildRegisterNo","");
        } else if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfOffildRegisterNo")) {
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfOffildLname", "");
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfOffildFname", "");
            ra.setCursorPosition(2);
        }
        
        String d_name = "";
        if (ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfOffildLname") != null) {
            d_name = (String) ra.getAttribute(ldbName,"CollHfPriorDialog_txtHfOffildLname");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfOffildRegisterNo") != null) {
            d_register_no = (String) ra.getAttribute(ldbName,"CollHfPriorDialog_txtHfOffildRegisterNo");
        }

        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }

        // JE LI zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }

        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfOffildRegisterNo")) ra.setCursorPosition(3);
        
        if (ra.isLDBExists("CustomerAllLookUpLDB")) {
            coll_util.clearFields("CustomerAllLookUpLDB", new String[] {"cus_id", "register_no", "code", "name",               
                    "add_data_table", "cus_typ_id",  "cus_sub_typ_id", "eco_sec", "residency_cou_id" });
        } else {
            ra.createLDB("CustomerAllLookUpLDB");
        }

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOffildRegisterNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOffildLname"));

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no","register_no");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute(MASTER_LDBNAME, "HF_OFFI_LRD", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildLname", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;
    }// CollHfPriorDialog_HfOffildRegisterNo_FV

    public boolean CollHfPriorDialog_JudgeRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = MASTER_LDBNAME;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtJudgeRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtJudgeFname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtJudgeLname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_JUDGE_ID", null);
            return true;
        }

        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtJudgeFname")) {
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtJudgeLname", "");
            ra.setAttribute(ldbName,"CollHfPriorDialog_txtJudgeRegisterNo", "");
        } else if (ra.getCursorPosition().equals("CollHfPriorDialog_txtJudgeRegisterNo")) {
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtJudgeFname", "");
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtJudgeLname", "");
            ra.setCursorPosition(2);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "CollHfPriorDialog_txtJudgeFname") != null) {
            d_name = (String) ra.getAttribute(ldbName, "CollHfPriorDialog_txtJudgeFname");
        }

        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollHfPriorDialog_txtJudgeRegisterNo") != null) {
            d_register_no = (String) ra.getAttribute(ldbName,"CollHfPriorDialog_txtJudgeRegisterNo");
        }

        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }

        // JE LI zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }
        if (ra.getCursorPosition().equals(
                "CollHfPriorDialog_txtJudgeRegisterNo"))
            ra.setCursorPosition(3);

        if (ra.isLDBExists("CustomerAllLookUpLDB")) {
            coll_util.clearFields("CustomerAllLookUpLDB", new String[] {"cus_id", "register_no", "code", "name",               
                    "add_data_table", "cus_typ_id",  "cus_sub_typ_id", "eco_sec", "residency_cou_id" });
        } else {
            ra.createLDB("CustomerAllLookUpLDB");
        }

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtJudgeRegisterNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtJudgeFname"));

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no","register_no");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table","add_data_table");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id","cus_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id","cus_sub_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id","residency_cou_id");
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_JUDGE_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtJudgeRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtJudgeLname", ra.getAttribute("CustomerAllLookUpLDB", "name"));
        return true;
    }// CollHfPriorDialog_JudgeRegisterNo_FV

    public boolean CollHfPriorDialog_HfOffildLname_FV() {
        String lName = null;

        lName = (String) ra.getAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildLname");
        if (lName == null || lName.trim().equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildLname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildFname", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_OFFI_LRD", null);
        }
        return true;
    }// CollHfPriorDialog_HfOffildLname_FV

    public boolean CollHfPriorDialog_HfOffildFname_FV() {
        String fName = null;

        fName = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfOffildFname");
        if (fName == null || fName.trim().equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildLname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfOffildFname", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_OFFI_LRD", null);
        }
        return true;
    }// CollHfPriorDialog_HfOffildFname_FV

    public boolean CollHfPriorDialog_HfNotaryRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType) {
        String ldbName = MASTER_LDBNAME;

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfNotaryRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfNotLname", null);
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfNotFname", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_NOTARY", null);
            return true;
        }
        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfNotLname")) {
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfNotFname", "");
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfNotaryRegisterNo", "");
        } else if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfNotaryRegisterNo")) {
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfNotLname", "");
            ra.setAttribute(ldbName, "CollHfPriorDialog_txtHfNotFname", "");
            // ra.setCursorPosition(2);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfNotLname") != null) {
            d_name = (String) ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfNotLname");
        }
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfNotaryRegisterNo") != null) {
            d_register_no = (String) ra.getAttribute(ldbName, "CollHfPriorDialog_txtHfNotaryRegisterNo");
        }

        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }

        // JE LI zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }

        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtHfNotaryRegisterNo"))
            ra.setCursorPosition(3);

        if (ra.isLDBExists("CustomerAllLookUpLDB")) {
            coll_util.clearFields("CustomerAllLookUpLDB", new String[] {"cus_id", "register_no", "code", "name",               
                    "add_data_table", "cus_typ_id",  "cus_sub_typ_id", "eco_sec", "residency_cou_id" });
        } else {
            ra.createLDB("CustomerAllLookUpLDB");
        }

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfNotaryRegisterNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfNotLname"));

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id","cus_sub_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute(MASTER_LDBNAME, "HF_NOTARY", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotaryRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotLname", ra.getAttribute("CustomerAllLookUpLDB", "name"));

        return true;
    }// CollHfPriorDialog_HfNotaryRegisterNo_FV

    public boolean CollHfPriorDialog_HfNotLname_FV() {
        String lName = null;

        lName = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfNotLname");
        if (lName == null || lName.trim().equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotaryRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotLname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotFname", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_NOTARY", null);
        }
        return true;
    }// CollHfPriorDialog_HfNotLname_FV

    public boolean CollHfPriorDialog_HfNotFname_FV() {
        String fName = null;

        fName = (String) ra.getAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotFname");
        if (fName == null || fName.trim().equals("")) {
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotaryRegisterNo", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotLname", null);
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotFname", null);
            ra.setAttribute(MASTER_LDBNAME, "HF_NOTARY", null);
        }
        return true;
    }// CollHfPriorDialog_HfNotFname_FV

    public boolean CollHfPriorDialog_HfRecLop_FV(String elementName, Object elementValue, Integer lookUpType) {
        String HfRecLopSCV = null;
        BigDecimal rbaCusId = new BigDecimal("8218251.0");
        if (elementValue == null || elementValue.equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[] {"CollHfPriorDialog_txtHfRecLopSysCodeValue","CollHfPriorDialog_txtHfRecLopSysCodeDesc",
                    "HF_REC_LOP_ID", "CollHfPriorDialog_txtHfRegisterNo", "CollHfPriorDialog_txtHfOwnCode","CollHfPriorDialog_txtHfOIB",
                    "CollHfPriorDialog_txtHfOwnLname", "HF_OWN_CUS_ID" });
            return true;
        }

        ra.setAttribute(MASTER_LDBNAME, "SysCodId", "clt_hfreclop");
        coll_util.clearField(MASTER_LDBNAME,"CollHfPriorDialog_txtHfRecLopSysCodeDesc");

        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB"))
            ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "clt_hfreclop");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue", "sys_code_value");
        request.addMapping(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeDesc", "sys_code_desc");
        request.addMapping(MASTER_LDBNAME, "HF_REC_LOP_ID", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        // CollHfPriorDialog_txtHfDateHfcUntil - obavezno za unos ako je RBA
        // hipoteka
        String pravo = (String) ra.getAttribute(MASTER_LDBNAME, "CollHFP_txtRecLop");
        String vrsta = (String) ra.getAttribute("ColWorkListLDB", "code");

        // ocistimo polja korisnika tereta
        coll_util.clearFields(MASTER_LDBNAME, new String[] {
                "CollHfPriorDialog_txtHfRegisterNo", "CollHfPriorDialog_txtHfOwnCode", "CollHfPriorDialog_txtHfOIB",
                "CollHfPriorDialog_txtHfOwnLname","CollHfPriorDialog_txtHfOwnFname" });

        HfRecLopSCV = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue");
        if (HfRecLopSCV != null) {
            HfRecLopSCV = HfRecLopSCV.trim();
        }
        
        if (HfRecLopSCV.compareTo("R") == 0) {
            // hipoteka je u korist RBA: popuniti korisnika tereta sa RBA, datum
            // do kada treba upisati hipoteku mora biti obavezno
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfRegisterNo", "910000");
            if(!fromSE) coll_util.clearField(MASTER_LDBNAME, "CollHFP_txtRecLop");
            ra.invokeValidation("CollHfPriorDialog_txtHfRegisterNo");
            ra.setCursorPosition("CollHfPriorDialog_txtAmountRef");
            ra.setRequired("CollHfPriorDialog_txtHfDateHfcUntil", true);
            // ako su vozila i ako je upisano pravo banke D napuniti i
            // ako su vozila napuniti i vrstu upisa
            // ako je upisano pravo u korist rba
            if ((vrsta != null) && (vrsta.equals("VOZI") || vrsta.equals("PLOV") || vrsta.equals("POKR") || vrsta.equals("ZALI"))) {
                if ((pravo != null) && pravo.compareTo("D") == 0) {
                    ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTyp", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfHfcSysCodeValue"));
                } else {
                    ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTyp", null);
                }
            }
            // ako je hipoteka u korist RBA osloboditi za upis podatke za
            // okvirni sporazum
            coll_util.enableField("Kol_txtFrameAgr", 0);
            if(!fromSE) ra.setAttribute(MASTER_LDBNAME, "Kol_txtFrameAgr", "N");
            ra.setRequired("Kol_txtFrameAgr", true);
            ra.setCursorPosition("Kol_txtFrameAgr");
            coll_util.enableField("CollHfPriorDialog_txtSyndicate", 0);
        } else {
            // za cash depozit napuniti korisnika hipoteke s HBOR
            BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
            if (col_cat_id != null && col_cat_id.compareTo(new BigDecimal("612223")) == 0) {
                ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfRegisterNo", "910167");
                ra.invokeValidation("CollHfPriorDialog_txtHfRegisterNo");
            }
            coll_util.enableField("Kol_txtFrameAgr", 2);
            coll_util.clearField(MASTER_LDBNAME, "Kol_txtFrameAgr");
            ra.setRequired("Kol_txtFrameAgr", false);
            // hipoteka nije u korist RBA - polje o upisu prava banke treba popuniti automatski sa D
            // datum upisa ne treba biti obavezno polje
            ra.setAttribute(MASTER_LDBNAME, "CollHFP_txtRecLop", "D");
            ra.setRequired("CollHfPriorDialog_txtHfDateHfcUntil", false);
            // validirati CollHFP_txtRecLop
            ra.invokeValidation("CollHFP_txtRecLop");
            ra.setCursorPosition("CollHfPriorDialog_txtHfRegisterNo");
            coll_util.clearFields(MASTER_LDBNAME, getSyndicateFields());
            coll_util.enableFields(getSyndicateFields(), 2);
        }
        if(!fromSE) ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtSyndicate", "N");

        ra.setAttribute(MASTER_LDBNAME, "dummySt", null);
        ra.setAttribute(MASTER_LDBNAME, "dummyBD", null);
        ra.setAttribute(MASTER_LDBNAME, "SysCodId", "");

        return true;
    } // CollHfPriorDialog_HfRecLop_FV

    public boolean CollHfPriorDialog_HfHfc_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || elementValue.equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfHfcSysCodeValue", "");
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfHfcSysCodeDesc", "");
            ra.setAttribute(MASTER_LDBNAME, "HF_HFC_ID", null);
            return true;
        }

        String vrsta = (String) ra.getAttribute("ColWorkListLDB", "code");
        if (vrsta != null && vrsta.equals("NEKR")) { // za nekretnine
            ra.setAttribute(MASTER_LDBNAME, "SysCodId", "clt_hf_reaest");
        } else if (vrsta != null && (vrsta.equals("VOZI") || vrsta.equals("PLOV") || vrsta.equals("POKR") || vrsta.equals("ZALI"))) { 
            // za vozila
            ra.setAttribute(MASTER_LDBNAME, "SysCodId", "clt_hf_vehicle");
        } else { 
            // za ostale kolaterale
            ra.setAttribute(MASTER_LDBNAME, "SysCodId", "clt_hf_other");
        }

        ra.setAttribute(MASTER_LDBNAME, "dummySt", null);
        ra.setAttribute(MASTER_LDBNAME, "dummyBD", null);
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfHfcSysCodeDesc", "");

        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", ra.getAttribute(MASTER_LDBNAME, "SysCodId"));

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping(MASTER_LDBNAME, "CollHfPriorDialog_txtHfHfcSysCodeValue", "sys_code_value");
        request.addMapping(MASTER_LDBNAME, "CollHfPriorDialog_txtHfHfcSysCodeDesc", "sys_code_desc");
        request.addMapping(MASTER_LDBNAME, "HF_HFC_ID", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute(MASTER_LDBNAME, "dummySt", null);
        ra.setAttribute(MASTER_LDBNAME, "dummyBD", null);
        ra.setAttribute(MASTER_LDBNAME, "SysCodId", "");

        String pravo = (String) ra.getAttribute(MASTER_LDBNAME, "CollHFP_txtRecLop");
        String HfRecLopSCV = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue");

        // ako su vozila napuniti i vrstu upisa
        // ako je upisano pravo u korist rba
        if (vrsta != null && (vrsta.equals("VOZI") || vrsta.equals("PLOV") || vrsta.equals("POKR") || vrsta.equals("ZALI"))) {
            if (HfRecLopSCV != null && HfRecLopSCV.equalsIgnoreCase("R") && pravo != null && pravo.equalsIgnoreCase("D")) {
                ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTyp", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfHfcSysCodeValue"));
            } else {
                ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTyp", null);
            }
        }
        return true;
    } // CollHfPriorDialog_HfHfc_FV

    public boolean CollHfPriorDialog_NotaryPlace_FV(String ElName, Object ElValue, Integer LookUp) {
        BigDecimal polMapTypId = new BigDecimal("5999.0");

        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "dummySt", "");
            ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfNotaryPlace", "");
            ra.setAttribute(MASTER_LDBNAME, "HF_NOTARY_PLACE_ID", null);
            return true;
        }
        if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
            ra.createLDB("PoliticalMapByTypIdLookUpLDB");
        }
        ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", polMapTypId);

        LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
        lookUpRequest.addMapping(MASTER_LDBNAME, "HF_NOTARY_PLACE_ID","pol_map_id");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt", "code");
        lookUpRequest.addMapping(MASTER_LDBNAME, "CollHfPriorDialog_txtHfNotaryPlace", "name");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        polMapTypId = null;
        return true;
    }// RealEstateDialog_NotaryPlace_FV

    public void postavi_neke() {
        if (imaInsert) {
            ra.setAttribute(MASTER_LDBNAME, "USE_ID", (BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(MASTER_LDBNAME, "USE_OPEN_ID", (BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtUseIdLogin", (String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtUseIdName", (String) ra.getAttribute("GDB", "Use_UserName"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtUseOpenIdLogin", (String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtUseOpenIdName", (String) ra.getAttribute("GDB", "Use_UserName"));
        }

        if (imaUpdate) {
            ra.setAttribute(MASTER_LDBNAME, "USE_ID", (BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtUseIdLogin", (String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtUseIdName", (String) ra.getAttribute("GDB", "Use_UserName"));
        }

        Date todaySQLDate = null;
        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        long timeT = calendar.getTime().getTime();
        todaySQLDate = new Date(timeT);

        Date datumOd = (Date) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateFrom");
        if (datumOd == null) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateFrom", todaySQLDate);
        }
        Date vvDatUntil = Date.valueOf("9999-12-31");
        Date datumDo = null;
        datumDo = (Date) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateUntil");
        if (datumDo == null) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateUntil", vvDatUntil);
        }
        datumDo = null;
        vvDatUntil = null;
    }

    // da li je upisana hipoteka
    public boolean CollHFP_txtRecLop_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHFP_txtRecLop", null);
            ra.setRequired("CollHfPriorDialog_txtHfCourtDecis", false);
            ra.setContext("CollHfPriorDialog_txtHfCourtDecis", "fld_plain");
            return true;
        }

        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");
        request.addMapping(MASTER_LDBNAME, "CollHFP_txtRecLop", "Vrijednosti");
        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        String pravo = (String) ra.getAttribute(MASTER_LDBNAME, "CollHFP_txtRecLop");
        String HfRecLopSCV = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue");
        String vrsta = (String) ra.getAttribute("ColWorkListLDB", "code");

        if (pravo != null) {
            pravo = pravo.trim();
            // upisana hipoteka, obavezno upisati datum upisa hipoteke ako je hipoteka u korist RBA
            // zaprotektirati polje do kada treba upisati pravo i nije obavezno
            if (pravo.equalsIgnoreCase("D")) {
                ra.setContext("CollHFP_txtDateRecLop", "fld_plain");
                if ((HfRecLopSCV != null) && HfRecLopSCV.compareTo("R") == 0) {
                    ra.setRequired("CollHFP_txtDateRecLop", true); // ako je u korist RBA
                } else {
                    ra.setRequired("CollHFP_txtDateRecLop", false); // nije u korist RBA
                }
                ra.setRequired("CollHFP_txtDateToLop", false);
                ra.setContext("CollHFP_txtDateToLop", "fld_protected");
                ra.setCursorPosition("CollHFP_txtDateRecLop");
                // ako su vozila napuniti i vrstu upisa ako je upisano pravo u korist rba
                if (vrsta != null  && (vrsta.equals("VOZI") || vrsta.equals("PLOV") || vrsta.equals("POKR") || vrsta.equals("ZALI"))) {
                    if ((HfRecLopSCV != null) && HfRecLopSCV.compareTo("R") == 0) {
                        ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTyp", ra.getAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfHfcSysCodeValue"));
                    }
                }
            } else if (pravo.equalsIgnoreCase("N")) {
                // nije upisano pravo, obavezno upisati datum do kada treba upisati pravo ako je hipoteka u korist RBA
                // zaprotektirati polje datuma upisa hipoteke i nije obavezno
                ra.setContext("CollHFP_txtDateRecLop", "fld_protected");
                ra.setRequired("CollHFP_txtDateRecLop", false);
                if ((HfRecLopSCV != null) && HfRecLopSCV.compareTo("R") == 0) {
                    ra.setRequired("CollHFP_txtDateToLop", true); // ako je u korist RBA
                } else {
                    ra.setRequired("CollHFP_txtDateToLop", false); // nije u korist RBA
                }
                ra.setContext("CollHFP_txtDateToLop", "fld_plain");
                ra.setCursorPosition("CollHFP_txtDateToLop");
                // ako su vozila napuniti i vrstu upisa
                if ((vrsta != null) && (vrsta.equals("VOZI") || vrsta.equals("PLOV") || vrsta.equals("PLOV") || vrsta.equals("PLOV"))) {
                    ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtFidTyp",null);
                }
                // ako su vozila, nisu obavezna neka polja
                if (vrsta != null && vrsta.equals("VOZI")) {
                    coll_util.setRequired(new String[] {"CollHFP_txtVehConNum", "Hf_txtRegIns", "HfPror_txtConcNum", "HfPror_txtRegPlace"}, false);
                }
            }
            // uvjet za nekretnine col_cat_id=618223
            BigDecimal col_cat_id = new BigDecimal(ra.getAttribute("ColWorkListLDB", "col_cat_id").toString());
            if (col_cat_id.compareTo(new BigDecimal(618223)) == 0) {
                System.out.println("Usla u blok upita za nekretnine");
                if (pravo.equalsIgnoreCase("D")) {
                    ra.setRequired("CollHfPriorDialog_txtHfCourtDecis", true);
                    ra.setContext("CollHfPriorDialog_txtHfCourtDecis", "fld_plain");
                } else if (pravo.equalsIgnoreCase("N")) {
                    ra.setRequired("CollHfPriorDialog_txtHfCourtDecis", false);
                    ra.setContext("CollHfPriorDialog_txtHfCourtDecis", "fld_protected");
                }
            } else {
                System.out.println("Usla u blok else");
                ra.setRequired("CollHfPriorDialog_txtHfCourtDecis", false);
            }
        }
        return true;
    } // RealEstate_txtComDoc_FV

    public boolean CollHFP_txtDateToLop_FV(String elementName, Object elementValue, Integer lookUpType) {
        // do kad treba biti upisana hipoteka - datum mora biti veci od current date
        Date doc_date = (Date) ra.getAttribute(MASTER_LDBNAME, "CollHFP_txtDateToLop");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (doc_date == null || current_date == null)
            return true;

        if ((doc_date).before(current_date)) {
            ra.showMessage("wrnclt113");
            return false;
        }
        return true;
    }

    public boolean CollHFP_txtDateRecLop_FV(String elementName, Object elementValue, Integer lookUpType) {
        // datum upisa hipoteke - datum mora biti <= current date
        // dodana kontrola na datum do kada vrijedi hipoteka, ovaj datum mora biti manji
        Date doc_date = (Date) ra.getAttribute(MASTER_LDBNAME,"CollHFP_txtDateRecLop");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date mortgage_valid_until = (Date) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateHfcUntil");
        
        if (doc_date == null || current_date == null) return true;        
        if ((current_date).before(doc_date)) {
            ra.showMessage("wrnclt114");
            return false;
        }
        
        if (doc_date == null || mortgage_valid_until == null) return true;
        if ((mortgage_valid_until).before(doc_date)) {
            ra.showMessage("wrnclt146");
            return false;
        }

        // punim i stari datum upisa koji se sada ne vidi na ekranu
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateReciv", ra.getAttribute(MASTER_LDBNAME, "CollHFP_txtDateRecLop"));

        // ako su vozila, postavljam kontekst za obavezna polja:broj zakljucka,
        // broj uloska i upisnicko mjesto
        String vrsta = (String) ra.getAttribute("ColWorkListLDB", "code");
        if (vrsta != null && vrsta.equals("VOZI")) {
            coll_util.setRequired(new String[] {"CollHFP_txtVehConNum", "Hf_txtRegIns", "HfPror_txtConcNum", "HfPror_txtRegPlace"}, true);
        }
        return true;
    }

    public boolean CollHfPriorDialog_txtHfDateHfcUntil_FV(String elementName, Object elementValue, Integer lookUpType) {
        // datum do kada vrijedi upisani teret
        // mora biti > current date
        // mora biti veci od datuma upisa prava

        Date doc_date = (Date) ra.getAttribute(MASTER_LDBNAME, "CollHFP_txtDateRecLop");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date mortgage_valid_until = (Date) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfDateHfcUntil");
        Date maturity_date = null;

        BigDecimal HfTableIdSecurity = new BigDecimal("1604184003.0");
        BigDecimal tablicaKoja = (BigDecimal) ra.getAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID");

        if (mortgage_valid_until == null || current_date == null) return true;
        if ((mortgage_valid_until).before(current_date)) {
            ra.showMessage("wrnclt125");
            return false;
        }

        // za VRP, provjeriti da nije veci od datuma dospijeca VRP-a
        if (tablicaKoja.compareTo(HfTableIdSecurity) == 0) {
            maturity_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtMaturityDate");
            
            if (mortgage_valid_until == null || maturity_date == null) return true;
            if ((maturity_date).before(mortgage_valid_until)) {
                ra.showMessage("wrnclt147");
                return false;
            }
        }

        if (doc_date == null || mortgage_valid_until == null) return true;
        if ((mortgage_valid_until).before(doc_date)) {
            ra.showMessage("wrnclt146");
            return false;
        }
        return true;
    }

    public boolean CollHFP_txtVehConNum_FV(String elementName, Object elementValue, Integer lookUpType) {
        // punim i stari broj zakljucka za tbl. coll_vehicle
        ra.setAttribute("CollSecPaperDialogLDB", "Vehi_txtVehConNum", ra.getAttribute(MASTER_LDBNAME, "CollHFP_txtVehConNum"));
        return true;
    }

    public boolean Hf_txtRegIns_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "Hf_txtRegIns", "");
            ra.setAttribute(MASTER_LDBNAME, "REG_INS", null);
            return true;
        }
        String vrsta = (String) ra.getAttribute("ColWorkListLDB", "code");
        if (!(ra.isLDBExists("UserCodeValueLookUpLDB"))) {
            ra.createLDB("UserCodeValueLookUpLDB");
        }
        ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id", "kol_reg_ins");
        ra.setAttribute(MASTER_LDBNAME, "dummyBD", null);
        ra.setAttribute(MASTER_LDBNAME, "dummySt", "");

        // postavljam use_cod_id ovisno o tipu
        if (vrsta != null) {
            if (vrsta.equals("DION")) {
                ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id","reg_ins_dio");
            } else if (vrsta.equals("OBVE") || vrsta.equals("ZAPI")) {
                ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id","reg_ins_obv");
            } else if (vrsta.equals("UDJE")) {
                ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id","reg_ins_udj");
            } else if (vrsta.equals("UDJP")) {
                ra.setAttribute("UserCodeValueLookUpLDB", "use_cod_id","reg_ins_udj_pod");
            }
        }

        LookUpRequest lookUpRequest = new LookUpRequest("UserCodeValueLookUp");
        lookUpRequest.addMapping(MASTER_LDBNAME, "REG_INS","use_cod_val_id");
        lookUpRequest.addMapping(MASTER_LDBNAME, "Hf_txtRegIns","use_code_value");
        lookUpRequest.addMapping(MASTER_LDBNAME, "dummySt","use_code_desc");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        // ako je odabrano DRUGO i ako su vrp-ovi - otvoriti za upis polja
        // drzava upisa i upisnicko mjesto
        String reg_ins = (String) ra.getAttribute(MASTER_LDBNAME,"Hf_txtRegIns");
        if (vrsta != null && (vrsta.equals("OBVE") || vrsta.equals("ZAPI") || vrsta.equals("UDJE") || vrsta.equals("UDJP"))) {
            if (reg_ins != null && reg_ins.equalsIgnoreCase("DRUGO")) {
                ra.setContext("HfPror_txtRegPlaceCou", "fld_plain");
                ra.setContext("HfPror_txtRegPlace", "fld_plain");
            } else {
                ra.setAttribute(MASTER_LDBNAME, "HfPror_txtRegPlaceCou", "");
                ra.setAttribute(MASTER_LDBNAME, "HfPror_txtRegPlaceCouName", "");
                ra.setAttribute(MASTER_LDBNAME, "REG_COU_ID", null);
                ra.setContext("HfPror_txtRegPlaceCou", "fld_protected");
                ra.setContext("HfPror_txtRegPlace", "fld_protected");
            }
        }
        return true;
    }

    public boolean Kol_txtFrameAgr_FV(String ElName, Object ElValue, Integer LookUp) {
        boolean ret = coll_lookups.ConfirmDN(MASTER_LDBNAME, ElName, ElValue);
        if (!ret) return ret;

        String agr = (String) ra.getAttribute(MASTER_LDBNAME, "Kol_txtFrameAgr");
        BigDecimal hf_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "COLL_HF_PRIOR_ID");
        // provjera hipoteka, okvirni sporazum, plasmani ako je promjena sa N na D provjera da li su na tu hipoteku vezani
        // plasmani, ako jesu ne smije se dozvoliti upis D ako je promjena sa D na N provjera da li je ostala jos koja hipoteka
        // vezana za okvirni sporazum, ako nije ne smije se dozvoliti promjena
        if (hf_id != null) {
            try {
                ra.executeTransaction();// LoanHfControl, CO037.sqlj            
            } catch (hr.vestigo.framework.remote.transaction.VestigoTMException vtme) {
                System.out.println("TEST");
                ra.showMessage(vtme.getMessageID());
                return false;
            }catch (Exception e) {
                // TODO: handle exception
            }
        }
        // ako je dio sporazuma treba upisati broj ugovora
        if (agr != null && agr.equalsIgnoreCase("D")) {
            coll_util.enableFields(getSyndicateFields(), 2);
            coll_util.clearFields(MASTER_LDBNAME, getSyndicateFields());
            coll_util.enableField("Agr_txtAgrNo1", 0);
            ra.setRequired("Agr_txtAgrNo1", true);
            ra.setCursorPosition("Agr_txtAgrNo1");
            coll_util.clearFields(MASTER_LDBNAME, new String[] { "CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar",
                    "CollHfPriorDialog_txtExcRatRef", "CollHfPriorDialog_txtExcRatRefDate" });
            coll_util.enableFields(new String[] {"CollHfPriorDialog_txtAmountRef","CollHfPriorDialog_txtHfCurIdRefCodeChar", "CollHfPriorDialog_txtHfDateHfcUntil" }, 2);
        } else {
            // obrisati broj ugovora samo ako je prvi unos
            // Milka, 29.03.2007, promjena - obrisati uvijek ako je N
            coll_util.clearFields(MASTER_LDBNAME, new String[] { "fra_agr_id", "Agr_txtAgrNo1" });
            coll_util.enableField("CollHfPriorDialog_txtSyndicate", 0);
            coll_util.enableField("Agr_txtAgrNo1", 2);
            coll_util.clearFields(MASTER_LDBNAME, new String[] { "CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar",
                    "CollHfPriorDialog_txtExcRatRef", "CollHfPriorDialog_txtExcRatRefDate" });
            coll_util.enableFields(new String[] {"CollHfPriorDialog_txtAmountRef","CollHfPriorDialog_txtHfCurIdRefCodeChar", "CollHfPriorDialog_txtHfDateHfcUntil" }, 0);
            ra.setRequired("Agr_txtAgrNo1", false);
            ra.setCursorPosition("CollHfPriorDialog_txtAmountRef");
        }
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtSyndicate", "N");
        return true;
    }

    public boolean Agr_txtAgrNo1_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "Agr_txtAgrNo1", "");
            ra.setAttribute(MASTER_LDBNAME, "fra_agr_id", null);
            return true;
        }

        String d_frame_num = "";
        if (ra.getAttribute(MASTER_LDBNAME, "Agr_txtAgrNo1") != null) {
            d_frame_num = (String) ra.getAttribute(MASTER_LDBNAME, "Agr_txtAgrNo1");
        }

        // da li je zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_frame_num)) {
            ra.showMessage("wrn367");
            return false;
        }

        if (ra.isLDBExists("AgrSelMapLDB")) {
            coll_util.clearFields("AgrSelMapLDB", new String[]{"l_fra_agr_id", "frame_num", "l_register_no", "l_name", "l_amount", "l_currency",
                    "l_cur_id", "l_status", "l_date"});
        } else {
            ra.createLDB("AgrSelMapLDB");
        }

        ra.setAttribute("AgrSelMapLDB", "frame_num", ra.getAttribute(MASTER_LDBNAME, "Agr_txtAgrNo1"));

        LookUpRequest lookUpRequest = new LookUpRequest("AgrSelLookUp");
        lookUpRequest.addMapping("AgrSelMapLDB", "l_fra_agr_id", "fra_agr_id");
        lookUpRequest.addMapping("AgrSelMapLDB", "frame_num", "frame_num");
        lookUpRequest.addMapping("AgrSelMapLDB", "l_register_no", "register_no");
        lookUpRequest.addMapping("AgrSelMapLDB", "l_name", "name");
        lookUpRequest.addMapping("AgrSelMapLDB", "l_amount", "amount");
        lookUpRequest.addMapping("AgrSelMapLDB", "l_currency", "code_char");
        lookUpRequest.addMapping("AgrSelMapLDB", "l_cur_id", "cur_id");
        lookUpRequest.addMapping("AgrSelMapLDB", "l_status", "status");
        lookUpRequest.addMapping("AgrSelMapLDB", "l_date", "maturity_date");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }catch (VestigoLookUpException e) {
            ra.showMessage("wrn090");
            return false;
        }        
        ra.setAttribute(MASTER_LDBNAME, "fra_agr_id", ra.getAttribute("AgrSelMapLDB", "l_fra_agr_id"));
        ra.setAttribute(MASTER_LDBNAME, "Agr_txtAgrNo1", ra.getAttribute("AgrSelMapLDB", "frame_num"));

        // punim iznos ugovora u iznos hipoteke, valutu i datum dospijeca
        // ugovora u do kad vazi teret i protektiram za unos ta polja
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtAmountRef", ra.getAttribute("AgrSelMapLDB", "l_amount"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdRefCodeChar", ra.getAttribute("AgrSelMapLDB", "l_currency"));
        ra.setAttribute(MASTER_LDBNAME, "CUR_ID_REF", ra.getAttribute("AgrSelMapLDB", "l_cur_id"));
        ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtHfDateHfcUntil", ra.getAttribute("AgrSelMapLDB", "l_date"));

        coll_util.enableFields(new String[] {"CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar", "CollHfPriorDialog_txtHfDateHfcUntil"}, 1);
        // pozvati validaciju valute i iznosa
        ra.invokeValidation("CollHfPriorDialog_txtAmountRef");
        ra.invokeValidation("CollHfPriorDialog_txtHfCurIdRefCodeChar");
        
        ra.setCursorPosition("CollHfPriorDialog_txtHfHfcSysCodeValue");
        return true;
    }

    public boolean HfPror_txtConcDate_FV() {
        // ne moze biti u buducnosti
        Date conc_date = (Date) ra.getAttribute(MASTER_LDBNAME, "HfPror_txtConcDate");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (conc_date != null && current_date != null) {
            if (current_date.before(conc_date)) {
                ra.showMessage("wrnclt121");
                return false;
            }
        }
        return true;
    }

    public boolean HfPror_txtRegPlaceCou_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "HfPror_txtRegPlaceCou", "");
            ra.setAttribute(MASTER_LDBNAME, "HfPror_txtRegPlaceCouName", "");
            ra.setAttribute(MASTER_LDBNAME, "REG_COU_ID", null);
            return true;
        }
        if (ra.getCursorPosition().equals("HfPror_txtRegPlaceCou")) {
            ra.setAttribute(MASTER_LDBNAME, "HfPror_txtRegPlaceCouName", "");
            ra.setCursorPosition(2);
        } else if (ra.getCursorPosition().equals("HfPror_txtRegPlaceCouName")) {
            ra.setAttribute(MASTER_LDBNAME, "HfPror_txtRegPlaceCou", "");
        }

        ra.setAttribute(MASTER_LDBNAME, "dummySt", null);
        ra.setAttribute(MASTER_LDBNAME, "HfPror_txtRegPlaceCouName", "");

        LookUpRequest request = new LookUpRequest("CountryLookUp");
        request.addMapping(MASTER_LDBNAME, "REG_COU_ID", "cou_id");
        request.addMapping(MASTER_LDBNAME, "HfPror_txtRegPlaceCou", "shortcut_num");
        request.addMapping(MASTER_LDBNAME, "dummySt", "shortcut_char");
        request.addMapping(MASTER_LDBNAME, "dummySt", "cou_iso_code");
        request.addMapping(MASTER_LDBNAME, "HfPror_txtRegPlaceCouName", "name");
        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        // ne moze se odabrati hrvatska
        BigDecimal cou_id = (BigDecimal) ra.getAttribute(MASTER_LDBNAME, "REG_COU_ID");
        if (cou_id != null && cou_id.compareTo(new BigDecimal("999")) == 0) {
            ra.showMessage("wrnclt143");
            return false;
        }
        return true;
    } // HfPror_txtRegPlaceCou_FV

    public boolean HfPror_txtCourtCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        return true;
    }

    public boolean CollHfPriorDialog_txtSyndicate_FV(String ElName, Object ElValue, Integer LookUp) {
        boolean ret = coll_lookups.ConfirmDN(MASTER_LDBNAME, ElName, ElValue);
        String value = (String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtSyndicate");

        //ako nije pozvana metoda iz SE funkcije onda se pozvala validacija na polju...
        //ako je iz SE onda se provjerava da li je D i ako je onda se radi enabled na polja...
        if(!fromSE) {
            if (value.equals("D")) {
                coll_util.enableFields(getSyndicateFields(), 0);
                coll_util.setRequired(getSyndicateFields(), true);
                ra.setRequired("CollHfPriorDialog_txtSyndicatedComment", false);
                coll_util.enableFields(new String[] { "CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar",
                        "CollHfPriorDialog_txtsinducExcRatRef", "CollHfPriorDialog_txtExcRatRefDate" }, 2);
                coll_util.clearFields(MASTER_LDBNAME, new String[] { "CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar","CUR_ID_REF"});
                ra.setCursorPosition("CollHfPriorDialog_txtHBORCredit");
            } else {
                // ako je prazno ili N onda se  protektiraju i obrisu sva
                // polja, a onda se odprotektira
                // samo polje sindikata i postavi na N jer samo ono se moze
                // mijenjati ako je N
                coll_util.enableFields(getSyndicateFields(), 2);
                coll_util.clearFields(MASTER_LDBNAME, getSyndicateFields());
                coll_util.clearFields(MASTER_LDBNAME, new String[] { "CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar","CUR_ID_REF"});
                coll_util.enableField("CollHfPriorDialog_txtSyndicate", 0);
                ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtSyndicate", "N");
                coll_util.enableFields(new String[] { "CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar"}, 0);
                coll_util.setRequired(new String[] { "CollHfPriorDialog_txtAmountRef", "CollHfPriorDialog_txtHfCurIdRefCodeChar"}, true);            
            }   
        }else{
            if(ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfRecLopSysCodeValue").equals("R")){
                if (value.equals("D") && !ra.getScreenContext().equalsIgnoreCase("scr_detail")) {
                    coll_util.enableFields(getSyndicateFields(), 0);
                    coll_util.setRequired(getSyndicateFields(), true);
                    ra.setRequired("CollHfPriorDialog_txtSyndicatedComment", false);
                }else if(value.equals("N") && !ra.getScreenContext().equalsIgnoreCase("scr_detail")){
                    coll_util.enableField("CollHfPriorDialog_txtSyndicate", 0);
                }
            }
        }
        izracunOriginalneValuteIIznose();
        return ret;
    }

    public boolean CollHfPriorDialog_txtYESNO_FV(String ElName, Object ElValue, Integer LookUp) {
        return coll_lookups.ConfirmDN(MASTER_LDBNAME, ElName, ElValue);
    }
    
    public boolean CollHfPriorDialog_txtAmountSyndicate_FV() {
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtAmountRef", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtRBASyndicateAmountValue"));
        boolean rez = izracunOriginalneValuteIIznose();
        if(!rez) ra.showMessage("wrnclt36");
        return rez;        
    }

    public boolean CollHfPriorDialog_txtSyndicateCurCode_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue == null || ElValue.equals("")) {
            ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtSyndicateCurCode", null);
            ra.setAttribute(MASTER_LDBNAME, "CUR_ID_REF", null);
            return true;
        }

        LookUps.CurrencyLookUpReturnValues value = coll_lookups.new CurrencyLookUpReturnValues();
        value = coll_lookups.CurrencyNewLookUp(MASTER_LDBNAME, null, "CollHfPriorDialog_txtSyndicateCurCode", null, "CUR_ID_REF");

        ra.setAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtSyndicateCurCode", value.codeChar);
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtHfCurIdRefCodeChar", value.codeChar);
        ra.setAttribute(MASTER_LDBNAME, "CUR_ID_REF", value.curId);
        izracunOriginalneValuteIIznose();
        return true; 
    }

    public boolean CollHfPriorDialog_txtSyndicatePart_FV(String ElName, Object ElValue, Integer LookUp) {
        if (ElValue != null && ((BigDecimal) ElValue).compareTo(new BigDecimal("100")) == 1) {
            ra.showMessage("infzstColl03");
            return false;
        }
        return true;
    }

    public boolean CollHfPriorDialog_txtOtherSyndicate_FV(String ElName, Object ElValue, Integer LookUp) {
        String lookUpLDBName = "BankLegalEntLookUpLDB";
        if (ElValue == null || ((String) ElValue).equals("")) {
            coll_util.clearFields(MASTER_LDBNAME, new String[] { "CollHfPriorDialog_txtOtherSyndicateMB", "CollHfPriorDialog_txtOtherSyndicateName",
                    "OTHER_SYNDICATE_CUS_ID" });
            return true;
        }
        if (ra.getCursorPosition().equals("CollHfPriorDialog_txtOtherSyndicateMB")) {
            coll_util.clearField(MASTER_LDBNAME,"CollHfPriorDialog_txtOtherSyndicateName");
        } else if (ra.getCursorPosition().equals("CollHfPriorDialog_txtOtherSyndicateName")) {
            coll_util.clearField(MASTER_LDBNAME,"CollHfPriorDialog_txtOtherSyndicateMB");
        }
        if (((String) ra.getAttribute(MASTER_LDBNAME,"CollHfPriorDialog_txtOtherSyndicateMB")).length() < 3
                && ((String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtOtherSyndicateName")).length() < 3) {
            ra.showMessage("wrn366");
            return false;
        }
        // da li je zvjezdica na pravom mjestu kod register_no-a
        if (CharUtil.isAsteriskWrong((String) ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtOtherSyndicateMB"))) {
            ra.showMessage("wrn367");
            return false;
        }
        if (!ra.isLDBExists(lookUpLDBName)) {
            ra.createLDB(lookUpLDBName);
        }
        ra.setAttribute(lookUpLDBName, "cus_id", null);
        ra.setAttribute(lookUpLDBName, "register_no", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtOtherSyndicateMB"));
        ra.setAttribute(lookUpLDBName, "name", ra.getAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtOtherSyndicateName"));

        LookUpRequest lu = new LookUpRequest("BankLegalEntLookUp");

        lu.addMapping(lookUpLDBName, "cus_id", "cus_id");
        lu.addMapping(lookUpLDBName, "register_no", "register_no");
        lu.addMapping(lookUpLDBName, "name", "name");

        try {
            System.out.println("Pozivam lookUp");
            ra.callLookUp(lu);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }

        ra.setAttribute(MASTER_LDBNAME, "OTHER_SYNDICATE_CUS_ID", ra.getAttribute(lookUpLDBName, "cus_id"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtOtherSyndicateName", ra.getAttribute(lookUpLDBName, "name"));
        ra.setAttribute(MASTER_LDBNAME, "CollHfPriorDialog_txtOtherSyndicateMB", ra.getAttribute( lookUpLDBName, "register_no"));        
        return true;
    }

    private String[] getSyndicateFields() { 
        String[] s = new String[] { "CollHfPriorDialog_txtSyndicate",
                "CollHfPriorDialog_txtHBORCredit", "CollHfPriorDialog_txtRBAArranger",
                "CollHfPriorDialog_txtAmountSyndicate", "CollHfPriorDialog_txtSyndicateCurCode",
                "CollHfPriorDialog_txtRBASyndicatePartValue", "CollHfPriorDialog_txtOtherSyndicatePartValue",
                "CollHfPriorDialog_txtRBASyndicateAmountValue", "CollHfPriorDialog_txtOtherSyndicateAmountValue",
                "CollHfPriorDialog_txtSyndicatedComment", "CollHfPriorDialog_txtOtherSyndicateMB",
                "CollHfPriorDialog_txtOtherSyndicateName" };      
        return s;
    }
    
    private void hideVRPFields(){
        coll_util.hideFields(new String[]{
           "HfPror_lblConcDate", "HfPror_txtConcDate", "HfPror_lblConcNum",
           "HfPror_txtConcNum", "HfPror_lblRegPlaceCou", "HfPror_txtRegPlaceCou", 
           "HfPror_txtRegPlaceCouName", "HfPror_lblRegPlace",  "HfPror_txtRegPlace", 
           "HfPror_lblCourt", "HfPror_txtCourtCode", "HfPror_txtCourtName"  
        });
    }

    public boolean isSecondActivMrtg(String tableName) {
        if (isTableEmpty(tableName)) return false;
        TableData tab_input = (TableData) ra.getAttribute(tableName);
        Vector data = tab_input.getData();

        for (int i = 0; i < tab_input.getUnique().size(); i++) {
            Vector row_in = (Vector) data.elementAt(i);
            String flag = "" + row_in.elementAt(8);
            if ((flag.equalsIgnoreCase("01")))
                return true;
        }
        return false;
    }

    public boolean isTableEmpty(String tableName) {
        TableData td = (TableData) ra.getAttribute(tableName);
        if (td == null) return true;
        if (td.getData().size() == 0) return true;
        return false;
    }// isTableEmpty

    /** 
     * Method cheks is there RBA active morgages
     * @return true
     */
    public boolean isTableOneRowRBA(String tableName) {
        if (isTableEmpty(tableName)) return false;
        TableData tab_input = (TableData) ra.getAttribute(tableName);
        Vector data = tab_input.getData();
        for (int i = 0; i < tab_input.getUnique().size(); i++) {
            Vector row_in = (Vector) data.elementAt(i);
            String flag = "" + row_in.elementAt(8);
            String user = "" + row_in.elementAt(1);
            user = user.trim();
            // System.out.println("1. vrtim hipoteke " + "red: " + flag + "user: " + user);
            if (user.equalsIgnoreCase("910000")
                    && (!flag.equalsIgnoreCase("NA"))) {
                // System.out.println("2. vrtim hipoteke " + "red: " + flag + "user: " + user);
                return true;
            }
        }
        return false;
    }
}
