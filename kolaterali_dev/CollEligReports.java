package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Prihvatljivost kolaterala
*
*/
public class CollEligReports extends Handler
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollEligReports.java,v 1.2 2009/10/09 13:13:39 hrakis Exp $";
	private final String ldbName = "CollEligReportsLDB";
	
	public CollEligReports(ResourceAccessor arg0)
	{
		super(arg0);
	}
	
	public void CollEligReports_SE()
	{
		ra.createLDB(ldbName);
	}

	/** Metoda koja naruèuje batch obradu. */
	public void orderCSV()
	{
        try
        {
            if(!ra.isRequiredFilled()) return;
        	String parametri = generateBatchParam();
        	BigDecimal batchDefId = ra.getAttribute("CollEligReports_txtReport").equals("REF") ? new BigDecimal("22060612") : new BigDecimal("25915892");
        	if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
        	ra.setAttribute("BatchLogDialogLDB", "BatchDefId", batchDefId);
        	ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
            ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute("BatchLogDialogLDB", "fldParamValue", parametri);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");  
        }
        catch (VestigoTMException vtme)
        {
            error("CollEligReports.orderCSV() -> VestigoTMException", vtme);
        }
	}
	
	/**
	 * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
	 * Parametri se predaju u obliku RB;date;X
	 */
	private String generateBatchParam()
	{
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign")).append(";");
        buffer.append(sdf.format(((Date)ra.getAttribute(ldbName, "CollEligReports_txtDate")))).append(";");
        buffer.append("X");
        return buffer.toString();
    }
	
	/** Validacijska metoda za polja s podacima o vrsti izvješæa. */
	public boolean CollEligReports_Report_FV(String elementName, Object elementValue, Integer lookUpType)
	{
        if (elementValue == null || ((String)elementValue).equals(""))
        {
            ra.setAttribute(ldbName, "CollEligReports_txtReport", "");
            return true;
        }
        LookUpRequest request = new LookUpRequest("ReportTypeLookUp");		
		request.addMapping(ldbName, "CollEligReports_txtReport", "Šifra");
		request.addMapping(ldbName, "dummyStr", "Naziv");
		return callLookUp(request);
	}
    
    /** Validacijska metoda za polje s datumom izvješæa. */
    public boolean CollEligReports_Date_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null) return true;
        Date date = (Date)ra.getAttribute(ldbName, "CollEligReports_txtDate");
        Date current_date = (Date)ra.getAttribute("GDB", "ProcessingDate");
        Date date_until = addDaysToDate(current_date, -1);
        Date date_from = addDaysToDate(date_until, -91);
        
        if (date.before(date_from) || date.after(date_until))
        {
            HashMap params = new HashMap();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            params.put("date_from", sdf.format(date_from));
            params.put("date_until", sdf.format(date_until));
            ra.showMessage("wrnclt163", params);
            return false;
        }
        return true;
    }
	
	/** Metoda koja poziva zadani lookup
	 * @return da li je poziv uspješno završen
	 */
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
	
    private Date addDaysToDate(Date p_date, int num_days)
    {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(p_date);
        c1.add(Calendar.DATE, num_days);
        Date date = new Date(c1.getTimeInMillis());
        return date;
    }
}