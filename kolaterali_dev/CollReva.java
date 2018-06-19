package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;


/**
* @author hrakis
* Handler klasa za ekran Revalorizacija kolaterala
*/
public class CollReva extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollReva.java,v 1.5 2017/10/13 08:41:12 hrakis Exp $";
    
    private final String ldbName = "CollRevaLDB";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final BigDecimal calculationBatchId = new BigDecimal("6546832704");
    private final BigDecimal revaluationBatchId = new BigDecimal("6546833704");
    private final BigDecimal controlReportBatchId = new BigDecimal("6563371704");

    public CollReva(ResourceAccessor arg0)
    {
        super(arg0);
        
        if (ra.getScreenContext().equalsIgnoreCase("revaContext"))
        {
            ra.setScreenTitle("Revaloriziraj - final");
        }
        else if (ra.getScreenContext().equalsIgnoreCase("controlContext"))
        {
            ra.setScreenTitle("Izvje\u0161taj o tr\u017Ei\u0161noj vrijednosti nekretnina i vozila");
        }
    }
    
    /** 
     * Ulazna funkcija.
     */
    public void CollReva_SE() throws Exception
    {
        ra.createLDB(ldbName);
        ra.executeTransaction();
    }
    
    /**
     * Akcija "Kreiraj izvještaj" na ekranu "Pomoæni izraèun revalorizacije".
     */
    public void orderCalculationReport() throws Exception
    {
        // provjeri je li napravljen izraèun revalorizacije
        Date calc_date = (Date)ra.getAttribute(ldbName, "CollReva_txtCalcDate");
        if (calc_date == null)
        {
            ra.showMessage("wrncltreva1");
            return;
        }
        
        // potvrdi zadavanje obrade
        if ((Integer)ra.showMessage("qer075") != ResourceAccessor.CONFIRM_MESSAGE_YES_BUTTON_SELECTED) return;
        
        // dohvati parametre za obradu
        String params = getBatchParamsForCalculationReport();
        
        // naruèi obradu
        orderBatch(calculationBatchId, params);
    }
    
    
    /**
     * Akcija "Napravi pomoæni izraèun" na ekranu "Pomoæni izraèun revalorizacije".
     */
    public void orderCalculation() throws Exception
    {
        // provjeri jesu li uèitani koeficijenti za revalorizaciju.
        Date coef_date = (Date)ra.getAttribute(ldbName, "CollReva_txtCoefDate");
        if (coef_date == null)
        {
            ra.showMessage("wrncltreva2");
            return;
        }
        
        // provjeri jesu li popunjena sva obvezna polja na ekranu
        if (!ra.isRequiredFilled()) return;
        
        // potvrdi akciju
        HashMap map = new HashMap();
        map.put("coef_date", dateFormat.format(coef_date));
        if(((Integer)ra.showMessage("wrncltreva3", map)) != ResourceAccessor.CONFIRM_MESSAGE_YES_BUTTON_SELECTED) return;
        
        // potvrdi zadavanje obrade
        if ((Integer)ra.showMessage("qer075") != ResourceAccessor.CONFIRM_MESSAGE_YES_BUTTON_SELECTED) return;
        
        // dohvati parametre za obradu
        String params = getBatchParamsForCalculation();
        
        // naruèi obradu
        orderBatch(calculationBatchId, params);
    }
    
    
    /**
     * Akcija "Revaloriziraj" na ekranu "Revaloriziraj - final".
     */
    public void orderRevaluation() throws Exception
    {
        // provjeri jesu li uèitani koeficijenti za revalorizaciju
        Date coef_date = (Date)ra.getAttribute(ldbName, "CollReva_txtCoefDate");
        if (coef_date == null)
        {
            ra.showMessage("wrncltreva2");
            return;
        }
        
        // provjeri je li napravljen izraèun revalorizacije
        Date calc_date = (Date)ra.getAttribute(ldbName, "CollReva_txtCalcDate");
        if (calc_date == null)
        {
            ra.showMessage("wrncltreva1");
            return;
        }
        
        // potvrdi akciju
        HashMap map = new HashMap();
        map.put("calc_date", dateFormat.format(calc_date));
        if(((Integer)ra.showMessage("wrncltreva4", map)) != ResourceAccessor.CONFIRM_MESSAGE_YES_BUTTON_SELECTED) return;
        
        // potvrdi zadavanje obrade
        if ((Integer)ra.showMessage("qer075") != ResourceAccessor.CONFIRM_MESSAGE_YES_BUTTON_SELECTED) return;
        
        // dohvati parametre za obradu
        String params = getBatchParamsForRevaluation();
        
        // naruèi obradu
        orderBatch(revaluationBatchId, params);
    }
    
    
    /**
     * Akcija "Naruèi izvještaj" na ekranu "Izvještaj o tržišnoj vrijednosti nekretnina i vozila".
     */
    public void orderControlReport() throws Exception
    {
        // potvrdi zadavanje obrade
        if ((Integer)ra.showMessage("qer075") != ResourceAccessor.CONFIRM_MESSAGE_YES_BUTTON_SELECTED) return;
        
        // dohvati parametre za obradu
        String params = getBatchParamsForControlReport();
        
        // naruèi obradu
        orderBatch(controlReportBatchId, params);
    }
    


    /**
     * Metoda koja naruèuje obradu.
     * @param batchDefId ID obrade
     * @param params parametri obrade
     */
    private void orderBatch(BigDecimal batchDefId, String params) throws Exception
    {
        try
        {
            final String batchLogLdb = "BatchLogDialogLDB";
            if (!ra.isLDBExists(batchLogLdb)) ra.createLDB(batchLogLdb);
            ra.setAttribute(batchLogLdb, "BatchDefId", batchDefId);
            ra.setAttribute(batchLogLdb, "RepDefId", null);
            ra.setAttribute(batchLogLdb, "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(batchLogLdb, "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute(batchLogLdb, "fldParamValue", params);
            ra.setAttribute(batchLogLdb, "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute(batchLogLdb, "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");  
        }
        catch (VestigoTMException vtme)
        {
            error("CollReva.orderBatch() -> VestigoTMException", vtme);
            throw vtme;
        }        
    }

    
    /**
     * Metoda koja vraæa formirani String s parametrima koji se predaju obradi za izraèun revalorizacije.
     * Parametri se predaju u obliku RB;cus_type;date_from;date_until
     */
    private String getBatchParamsForCalculation()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append((String)ra.getAttribute("GDB", "bank_sign"));
        buffer.append(";");
        buffer.append((String)ra.getAttribute(ldbName, "CollReva_txtCusTypeCode"));
        buffer.append(";");
        buffer.append(dateFormat.format((Date)ra.getAttribute(ldbName, "CollReva_txtNomiDateFrom")));
        buffer.append(";");
        buffer.append(dateFormat.format((Date)ra.getAttribute(ldbName, "CollReva_txtNomiDateUntil")));
        return buffer.toString();
    }
    
    /**
     * Metoda koja vraæa formirani String s parametrima koji se predaju za izradu izvještaja o izraèunu revalorizacije. 
     */
    private String getBatchParamsForCalculationReport()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append((String) ra.getAttribute("GDB", "bank_sign"));
        buffer.append(";");
        buffer.append("X");
        buffer.append(";");
        buffer.append("X");
        buffer.append(";");
        buffer.append("X");
        return buffer.toString();
    }
    
    /**
     * Metoda koja vraæa formirani String s parametrima koji se predaju obradi za knjiženje revalorizacije.
     * Parametri se predaju u obliku RB
     */
    private String getBatchParamsForRevaluation()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(ra.getAttribute("GDB", "bank_sign") + "#COL");
        return buffer.toString();
    }
    
    /**
     * Metoda koja vraæa formirani String s parametrima koji se predaju obradi za kreiranje kontrolnog izvještaja.
     * Parametri se predaju u obliku RB
     */
    private String getBatchParamsForControlReport()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append((String)ra.getAttribute("GDB", "bank_sign"));
        return buffer.toString();
    }
  


    /** Validacijska funkcija za vrstu komitenta. */
    public boolean CollReva_txtCusType_FV(String elName, Object elValue, Integer lookUp)
    {
        if (elValue == null || elValue.equals("")) {
            ra.setAttribute(ldbName, "CollReva_txtCusTypeCode", "");   
            ra.setAttribute(ldbName, "CollReva_txtCusTypeDesc", "");
            ra.setAttribute(ldbName, "dummyBd", null);
            return true;
        }
        
        ra.setAttribute(ldbName, "CollReva_txtCusTypeDesc", "");
        ra.setAttribute(ldbName, "dummyBd", null);

        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "coll_reva_cus_type");

        LookUpRequest request = new LookUpRequest("SysCodeValueNewLookUp");
        request.addMapping(ldbName, "CollReva_txtCusTypeCode", "sys_code_value");
        request.addMapping(ldbName, "CollReva_txtCusTypeDesc", "sys_code_desc");
        request.addMapping(ldbName, "dummyBd", "sys_cod_val_id");

        return callLookUp(request);
    }

    
    /** Validacijska funkcija za raspon datuma tržišne vrijednosti. */
    public boolean CollReva_txtNomiDate_FV()
    {
        Date dateFrom = (Date)ra.getAttribute(ldbName, "CollReva_txtNomiDateFrom");
        Date dateUntil = (Date)ra.getAttribute(ldbName, "CollReva_txtNomiDateUntil");
        if (dateFrom != null && dateUntil != null && dateFrom.compareTo(dateUntil) > 0)
        {
            ra.showMessage("wrn245");
            return false;
        }
        return true;
    }
    
   
    /** Metoda koja poziva zadani lookup
     * @return da li je poziv uspješno završen */
    private boolean callLookUp(LookUpRequest lu)
    {
        try {
            ra.callLookUp(lu);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        return true;    
    }
}