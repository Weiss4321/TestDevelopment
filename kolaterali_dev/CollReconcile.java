package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Izvješæe za rekonsilijaciju
*
*/
public class CollReconcile extends Handler
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollReconcile.java,v 1.3 2012/03/02 10:10:10 hramkr Exp $";
	private final String ldbName = "CollReconcileLDB";
	
	public CollReconcile(ResourceAccessor arg0)
	{
		super(arg0);
	}
	
	public void CollReconcile_SE()
	{
		ra.createLDB(ldbName);
	}

	/** Metoda koja naruèuje batch obradu. */
	public void orderCSV()
	{
	    if(!checkParameters()) return;
        if (!ra.isLDBExists("CollTypeReportLDB")) 
            ra.createLDB("CollTypeReportLDB");
        
// provjera da li postoje podaci za zadani datum         
        ra.setAttribute("CollTypeReportLDB", "CollTypeReport_txtDate", ra.getAttribute(ldbName, "CollReconcile_txtDate"));

        try { 
            ra.setScreenContext("scr_count");
            ra.executeTransaction();
        } catch (VestigoTMException vtme) {
            error("CollReconcile.toCSVfile()", vtme);
            return;
        }
        finally {
            ra.setScreenContext("scr_base");
        }
        Integer brojZapisa = (Integer)ra.getAttribute("CollTypeReportLDB", "BrojZapisa");
        if(brojZapisa.compareTo(new Integer(0)) <= 0)
        {
            ra.showMessage("wrnclt164");
            return;
        }
        ra.setScreenContext("scr_base");
        Integer answer = (Integer) ra.showMessage("qer075");
        if (answer.intValue() == 0) {
            return;
        }        
        try
        {
//            if(!checkParameters()) return;
        	String parametri = generateBatchParam();
        	if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
        	ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("31507192"));
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
            error("CollReconcile.orderCSV() -> VestigoTMException", vtme);
        }
	}
	
    /** Metoda koja provjerava da li su svi potrebni podaci uneseni na ekranu. */
	private boolean checkParameters()
    {
		Date date = (Date)ra.getAttribute(ldbName, "CollReconcile_txtDate");
		Date today = new Date(System.currentTimeMillis());
		
    	if(date == null)
    	{
    		ra.showMessage("wrn152");
    		return false;
    	}
	    else if(date.compareTo(today) >= 0)		    
    	{
			ra.showMessage("wrnclt164");
    		return false;
    	}
    	return true;
    }
	 
	/**
	 * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
	 * Parametri se predaju u obliku RB;date;X
	 */
	private String generateBatchParam()
	{
		StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign")).append(";");
		buffer.append(getDDMMYYYY(((Date)ra.getAttribute(ldbName, "CollReconcile_txtDate")))).append(";");
        buffer.append((String) ra.getAttribute("CollTypeReportLDB", "IndikatorArhive"));
        return buffer.toString();
    }
	

    /**
     * Metoda vraca datum zapisan u string u formatu dd.MM.yyyy
     * @param java.sql.Date
     * @return datum zapisan u string u formatu dd.MM.yyyy; ako je null vraca deset blankova
     */
    private String getDDMMYYYY(Date date)
    {
        if(date == null) return "          ";
        String date_s=date.toString();
        return date_s.substring(8, 10)+"."+date_s.substring(5, 7)+"."+date_s.substring(0, 4);
    }
    
    /**
     * @param Date1 <b>must</b> be starting date
     * @param Date2 <b>must</b> ending date
     * @return number of days between Date1 and Date2
     */
    private int actualNoDays(Date starting_date, Date end_date)
    {
		GregorianCalendar calendar1 = new GregorianCalendar();
		GregorianCalendar calendar2 = new GregorianCalendar();
		calendar1.setTime(starting_date);
		calendar2.setTime(end_date);
		int i = 0;
		while (true)
		{
		    if (calendar1.getTime().getTime() >= calendar2.getTime().getTime()) break;
			calendar1.add(Calendar.DATE, 1);
			i++;
		}
		return i;
    }
}