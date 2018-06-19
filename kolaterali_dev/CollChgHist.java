package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Izvješæe o povijesti promjena
*
*/
public class CollChgHist extends Handler
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollChgHist.java,v 1.1 2009/03/10 13:29:00 hrakis Exp $";
	private final String ldbName = "CollChgHistLDB";
	
	public CollChgHist(ResourceAccessor arg0)
	{
		super(arg0);
	}
	
	public void CollChgHist_SE()
	{
		ra.createLDB(ldbName);
	}

	/** Metoda koja naruèuje batch obradu. */
	public void orderCSV()
	{
        try
        {
            if(!checkParameters()) return;
        	String parametri = generateBatchParam();
        	final String batchLDB = "BatchLogDialogLDB";
        	if(!ra.isLDBExists(batchLDB)) ra.createLDB(batchLDB);
            ra.setAttribute(batchLDB, "BatchDefId", new BigDecimal("2793903003"));
            ra.setAttribute(batchLDB, "RepDefId", null);
            ra.setAttribute(batchLDB, "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(batchLDB, "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute(batchLDB, "fldParamValue", parametri);
            ra.setAttribute(batchLDB, "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute(batchLDB, "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");
        }
        catch (VestigoTMException vtme)
        {
            error("CollChgHist.orderCSV() -> VestigoTMException", vtme);
        } 
	}
	
    /** Metoda koja provjerava da li su svi potrebni podaci uneseni na ekranu. */
	private boolean checkParameters()
    {
		Date date_from = (Date)ra.getAttribute(ldbName, "CollChgHist_txtDateFrom");
		Date date_until = (Date)ra.getAttribute(ldbName, "CollChgHist_txtDateUntil");
		BigDecimal org_uni_id = (BigDecimal)ra.getAttribute(ldbName, "ORG_UNI_ID");
		
		if(date_from == null || date_until == null || org_uni_id == null)
    	{
    		ra.showMessage("wrn181");
    		return false;
    	}
		else if(date_from.compareTo(date_until) > 0)
    	{
    		ra.showMessage("wrn245");
    		return false;
    	}
		else if(actualNoDays(date_from, date_until) > 31)
		{
			ra.showMessage("wrnhrakis01");
    		return false;
		}
    	return true;
    }
	
	/**
	 * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
	 * Parametri se predaju u obliku RB;org_uni_id;use_id;date_from;date_until;X
	 */
	private String generateBatchParam()
	{
		StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign")).append(";");
		buffer.append(isEmpty((BigDecimal)ra.getAttribute(ldbName, "ORG_UNI_ID"))).append(";");
		buffer.append(isEmpty((BigDecimal)ra.getAttribute(ldbName, "USE_ID"))).append(";");
		buffer.append(getDDMMYYYY(((Date)ra.getAttribute(ldbName, "CollChgHist_txtDateFrom")))).append(";");
		buffer.append(getDDMMYYYY(((Date)ra.getAttribute(ldbName, "CollChgHist_txtDateUntil")))).append(";");
        buffer.append("X");
        return buffer.toString();
    }
	
    /** Validacijska metoda za polja s podacima o organizacijskoj jedinici. */
	public boolean CollChgHist_Org_FV(String elementName, Object elementValue, Integer lookUpType)
	{
        // poništi sva polja (osim trenutnog) koja su vezana za organizacijsku jedinicu i referenta
    	ra.setAttribute(ldbName, "ORG_UNI_ID", null);
        if(!elementName.equals("CollChgHist_txtOrgCode")) ra.setAttribute(ldbName, "CollChgHist_txtOrgCode", "");
        if(!elementName.equals("CollChgHist_txtOrgName")) ra.setAttribute(ldbName, "CollChgHist_txtOrgName", "");
        ra.setAttribute(ldbName, "USE_ID", null);
        ra.setAttribute(ldbName, "CollChgHist_txtUserLogin", "");
        ra.setAttribute(ldbName, "CollChgHist_txtUserName", "");
        if(elementValue == null || elementValue.equals("")) return true;
 	
		// inicijaliziraj i pozovi lookup
        LookUpRequest lu = new LookUpRequest("OrgUniLookUp");
		lu.addMapping(ldbName, "CollChgHist_txtOrgCode", "code");
		lu.addMapping(ldbName, "CollChgHist_txtOrgName", "name");
		lu.addMapping(ldbName, "ORG_UNI_ID", "org_uni_id");
		return callLookUp(lu);
	}

    /** Validacijska metoda za polja s podacima o referentu. */
	public boolean CollChgHist_User_FV(String elementName, Object elementValue, Integer lookUpType)
	{
		// poništi sva polja (osim trenutnog) koja su vezana za referenta
		ra.setAttribute(ldbName, "USE_ID", null);
        if(!elementName.equals("CollChgHist_txtUserLogin")) ra.setAttribute(ldbName, "CollChgHist_txtUserLogin", "");
        if(!elementName.equals("CollChgHist_txtUserName")) ra.setAttribute(ldbName, "CollChgHist_txtUserName", "");
		if(elementValue == null || elementValue.equals("")) return true;
			 
		// provjeri je li odabrana organizacijska jedinica
		if (ra.getAttribute(ldbName, "ORG_UNI_ID") == null)
		{
			ra.showMessage("inf216");
			return false;
		}
			
		// postavi organizacijsku jedinicu na LDB lookupa
		if (!ra.isLDBExists("AppUserOrgLDB")) ra.createLDB("AppUserOrgLDB");
		ra.setAttribute("AppUserOrgLDB", "org_uni_id", ra.getAttribute(ldbName, "ORG_UNI_ID"));	
		ra.setAttribute(ldbName, "dummyStr", "");
		ra.setAttribute(ldbName, "dummyBd", null);
 
		// inicijaliziraj i pozovi lookup
		LookUpRequest lu = new LookUpRequest("AppUserOrgLookUp");
		lu.addMapping(ldbName, "USE_ID", "use_id");
		lu.addMapping(ldbName, "CollChgHist_txtUserLogin", "login");
		lu.addMapping(ldbName, "CollChgHist_txtUserName", "user_name");
		lu.addMapping(ldbName, "dummyStr", "abbreviation");
		lu.addMapping(ldbName, "dummyBd", "org_uni_id");
		return callLookUp(lu);
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
		    if (calendar1.getTime().getTime() == calendar2.getTime().getTime()) break;
			calendar1.add(Calendar.DATE, 1);
			i++;
		}
		return i;
    }
    
    private String isEmpty(BigDecimal bd)
    {
        if (bd == null) return " "; else return bd.toString();
    }
}