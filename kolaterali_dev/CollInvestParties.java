package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.util.CharUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
* Handler ekrana za naruèivanje CRM izvješæa.
*/
public class CollInvestParties  extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollInvestParties.java,v 1.20 2014/10/15 13:59:07 hrakis Exp $";

    private final String ldbName = "CollInvestPartiesLDB";


    public CollInvestParties(ResourceAccessor ra)
    {
        super(ra);
    }
    
    /** Funkcija koja se pokreæe pri ulasku na ekran. */
    public void CollInvestParties_SE()
    {
        if (!ra.isLDBExists(ldbName)) ra.createLDB(ldbName);
    }


    /** Validacijska funkcija za polje Datum izvješæa. */
    public boolean CollInvestParties_txtDateOfReport_FV()
    {
        Date current_date = (Date)ra.getAttribute("GDB", "ProcessingDate");
        Date date_of_report = (Date)ra.getAttribute(ldbName, "CollInvestParties_txtDateOfReport");
        if (date_of_report == null || current_date == null) return true;

        // onemoguæi unos datuma koji je veæi od tekuæog datuma
        if (current_date.before(date_of_report))
        {
            ra.showMessage("wrnclt121");
            return false;
        }
        return true;
    }


    /** Validacijska funkcija za polje Tip komitenta. */
    public boolean CollInvestParties_txtClientType_FV(String elementName, Object elementValue, Integer lookUp)
    {
        // ako je vrijednost polja null ili prazno, isprazni polje i omoguæi unos polja Komitent
        if (elementValue == null || ((String)elementValue).equals(""))
        {
            ra.setAttribute(ldbName, "CollInvestParties_txtClientType", "");
            ra.setContext("CollInvestParties_txtRegisterNo", "fld_plain");
            return true;
        }

        // pripremi lookup za odabir tipa komitenta
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");
        request.addMapping(ldbName, "CollInvestParties_txtClientType", "Vrijednosti");
        
        // pozovi lookup za odabir tipa komitenta
        if (!callLookUp(request)) return false;

        // ako je odabrana vrijednost, onemoguæi unos u polje Komitent
        ra.setContext("CollInvestParties_txtRegisterNo", "fld_protected");

        return true;
    }


    /** Validacijska funkcija za polje Komitent. */
    public boolean CollInvestParties_txtRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        // ako je vrijednost polja null ili prazno, isprazni polje i omoguæi unos polja Tip komitenta
        if (elementValue == null || ((String)elementValue).equals(""))
        {
            ra.setAttribute(ldbName, "CollInvestParties_txtClientName","");
            ra.setAttribute(ldbName, "CollInvestParties_txtRegisterNo","");
            ra.setAttribute(ldbName, "CUS_ID", null);
            ra.setRequired("CollInvestParties_txtClientType", true);
            ra.setContext("CollInvestParties_txtClientType", "fld_plain");
            return true;
        }

        // provjeri da li je zvjezdica na pravom mjestu kod register_no
        String register_no = (String)ra.getAttribute(ldbName, "CollInvestParties_txtRegisterNo");
        if (CharUtil.isAsteriskWrong(register_no))
        {
            ra.showMessage("wrn367");
            return false;
        }

        // pripremi LDB lookupa za odabir komitenta
        final String lookUpLdbName = "CustomerAllLookUpLDB";
        if (!ra.isLDBExists(lookUpLdbName)) ra.createLDB(lookUpLdbName); 
        ra.setAttribute(lookUpLdbName, "cus_id", null);
        ra.setAttribute(lookUpLdbName, "register_no", register_no);
        ra.setAttribute(lookUpLdbName, "code", "");
        ra.setAttribute(lookUpLdbName, "name", "");
        ra.setAttribute(lookUpLdbName, "add_data_table", "");
        ra.setAttribute(lookUpLdbName, "cus_typ_id", null);
        ra.setAttribute(lookUpLdbName, "cus_sub_typ_id", null);
        ra.setAttribute(lookUpLdbName, "eco_sec", "");
        ra.setAttribute(lookUpLdbName, "residency_cou_id", null);

        // pripremi lookup za odabir komitenta
        LookUpRequest request = new LookUpRequest("CustomerAllLookUp");
        request.addMapping(lookUpLdbName, "cus_id", "cus_id");
        request.addMapping(lookUpLdbName, "register_no", "register_no");
        request.addMapping(lookUpLdbName, "code", "code");
        request.addMapping(lookUpLdbName, "name", "name");
        request.addMapping(lookUpLdbName, "add_data_table", "add_data_table");
        request.addMapping(lookUpLdbName, "cus_typ_id", "cus_typ_id");
        request.addMapping(lookUpLdbName, "cus_sub_typ_id", "cus_sub_typ_id");
        request.addMapping(lookUpLdbName, "eco_sec", "eco_sec");
        request.addMapping(lookUpLdbName, "residency_cou_id", "residency_cou_id");
        
        // pozovi lookup za odabir komitenta
        if (!callLookUp(request)) return false;
        
        // prebaci vrijednosti s lookupa
        ra.setAttribute(ldbName, "CUS_ID", ra.getAttribute(lookUpLdbName, "cus_id"));
        ra.setAttribute(ldbName, "CollInvestParties_txtRegisterNo", ra.getAttribute(lookUpLdbName, "register_no"));
        ra.setAttribute(ldbName, "CollInvestParties_txtClientName", ra.getAttribute(lookUpLdbName, "name"));

        // ako je odabrana vrijednost, onemoguæi unos u polje Vrsta komitenta
        ra.setRequired("CollInvestParties_txtClientType", false);
        ra.setContext("CollInvestParties_txtClientType", "fld_protected");

        return true;
    }


    /**
     * Metoda koja se poziva pritiskom na gumb F6 - Datum izvješæa. 
     * Metoda prikazuje lookup s popisom svih izraèuna pokrivenosti.
     * Odabirom stavke iz lookupa popunjavaju se polja Datum izvješæa, Ponder i Prihvatljivost.
     */
    public void date_of_report() throws VestigoTMException
    {
        // pripremi LDB lookupa za odabir izraèuna pokrivenosti
        final String lookUpLdbName = "CollExpDateLookUpLDB";
        if (!ra.isLDBExists(lookUpLdbName)) ra.createLDB(lookUpLdbName);

        // pripremi lookup za odabir izraèuna pokrivenosti
        LookUpRequest request = new LookUpRequest("CollExpDateLookUp");   
        request.addMapping(lookUpLdbName, "value_date", "value_date");
        request.addMapping(lookUpLdbName, "eligibility", "eligibility");
        request.addMapping(lookUpLdbName, "eligibility_desc", "eligibility_desc");
        request.addMapping(lookUpLdbName, "ponder", "ponder");
        request.addMapping(lookUpLdbName, "proc_type", "proc_type");

        // pozovi lookup za odabir izraèuna pokrivenosti
        if (!callLookUp(request)) return;

        // ako je odabrana vrijednost, prenesi vrijednosti iz lookupa u polja na ekranu. 
        ra.setAttribute(ldbName, "CollInvestParties_txtDateOfReport", ra.getAttribute(lookUpLdbName, "value_date"));
        ra.setAttribute(ldbName, "Kol_txtEligibility", ra.getAttribute(lookUpLdbName, "eligibility"));
        ra.setAttribute(ldbName, "Kol_txtEligibilityDsc", ra.getAttribute(lookUpLdbName, "eligibility_desc"));
        ra.setAttribute(ldbName, "CollInvestParties_txtPonder", ra.getAttribute(lookUpLdbName, "ponder"));
        ra.setAttribute(ldbName, "PROC_TYPE", ra.getAttribute(lookUpLdbName, "proc_type"));
    }


    /**
     * Metoda koja se poziva pritiskom na gumb F8 - Formiraj CSV.
     * Metoda izvršava transakciju koja naruèuje obradu za kreiranje CRM izvješæa.  
     */
    public void toCSVfile() throws VestigoTMException
    {
        // provjeri da li su popunjena sva potrebna polja
        if (!ra.isRequiredFilled()) return;
    
        // u ovisnosti o kontekstu ekrana odredi koja æe obrada biti naruèena i pripremi string s ulaznim parametrima obrade  
        String param = null;
        BigDecimal bat_def_id = null;
        if (ra.getScreenContext().equals("new_context"))
        {
            bat_def_id = new BigDecimal("6502762704");
            param = getParamStringForNewReport();
        }
        else
        {
            bat_def_id = new BigDecimal("120227254");
            param = getParamStringForOldReport();
        }

        // pripremi LDB za naruèivanje obrade
        final String batchLogLdbName = "BatchLogDialogLDB";
        if (!ra.isLDBExists(batchLogLdbName)) ra.createLDB(batchLogLdbName);
        ra.setAttribute(batchLogLdbName, "BatchDefId", bat_def_id);
        ra.setAttribute(batchLogLdbName, "fldParamValue", param);
        ra.setAttribute(batchLogLdbName, "RepDefId", null);
        ra.setAttribute(batchLogLdbName, "AppUseId", ra.getAttribute("GDB", "use_id"));
        ra.setAttribute(batchLogLdbName, "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
        ra.setAttribute(batchLogLdbName, "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
        ra.setAttribute(batchLogLdbName, "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
        
        // izvrši transakciju koja naruèuje obradu
        ra.executeTransaction();
        ra.showMessage("inf075");
    }


    /** Metoda vraæa string s ulaznim parametrim stare obrade za kreiranje CRM izvješæa (bo45). */
    private String getParamStringForOldReport()
    {
        String bank_sign = (String)ra.getAttribute("GDB", "bank_sign");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date_of_report = (Date)ra.getAttribute(ldbName, "CollInvestParties_txtDateOfReport");
        String date_of_report_str = dateFormat.format(date_of_report).toString();

        String register_no = (String)ra.getAttribute(ldbName, "CollInvestParties_txtRegisterNo");
        if (register_no == null || register_no.trim().equals("")) register_no = " ";

        String client_type = (String)ra.getAttribute(ldbName, "CollInvestParties_txtClientType");
        if (client_type == null || client_type.trim().equals("")) client_type = " ";

        String ponder = ra.getAttribute(ldbName, "CollInvestParties_txtPonder");
        if (ponder == null || ponder.trim().equals("")) ponder = " ";

        String eligibility = (String)ra.getAttribute(ldbName, "Kol_txtEligibility");
        if (eligibility == null || eligibility.trim().equals("")) eligibility = " ";

        return bank_sign + ";" + date_of_report_str + ";" + register_no + ";" + client_type + ";" + ponder + ";" + eligibility + ";" + "X";
    }

    /** Metoda vraæa string s ulaznim parametrim stare obrade za kreiranje CRM izvješæa (bo85). */
    private String getParamStringForNewReport()
    {
        String bank_sign = (String)ra.getAttribute("GDB", "bank_sign");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date_of_report = (Date)ra.getAttribute(ldbName, "CollInvestParties_txtDateOfReport");
        String date_of_report_str = dateFormat.format(date_of_report).toString();

        String register_no = (String)ra.getAttribute(ldbName, "CollInvestParties_txtRegisterNo");
        if (register_no == null || register_no.trim().equals("")) register_no = "X";

        String client_type = (String)ra.getAttribute(ldbName, "CollInvestParties_txtClientType");
        if (client_type == null || client_type.trim().equals("")) client_type = "X";

        String proc_type = ra.getAttribute(ldbName, "PROC_TYPE");

        return bank_sign + ";" + date_of_report_str + ";" + register_no + ";" + client_type + ";" + proc_type;
    }


    /** Metoda koja poziva zadani lookup. */
    private boolean callLookUp(LookUpRequest request)
    {
        try
        {
            ra.callLookUp(request);
            return true;
        } 
        catch (EmptyLookUp elu)
        {
            ra.showMessage("err012");
            return false;
        }
        catch (NothingSelected ns)
        {
            return false;
        }
    }
}