package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Izvješæe prema aktivnosti plasmana
*
*/
public class CollLoanStatus extends Handler
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollLoanStatus.java,v 1.3 2017/10/16 09:40:29 hrazst Exp $";
	private final String ldbName = "CollLoanStatusLDB";
	
	public CollLoanStatus(ResourceAccessor arg0)
	{
		super(arg0);
	}
	
	public void CollLoanStatus_SE()
	{
		ra.createLDB(ldbName);
	}

	/** Metoda koja naruèuje batch obradu. */
	public void toCSVfile()
	{
        try
        {
            if(!checkParameters()) return;
        	String parametri = generateBatchParam();
        	if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("2640078003"));
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
            error("CollLoanStatus.toCSVfile() -> VestigoTMException", vtme);
        } 
	}
	
    /** Metoda koja provjerava da li su svi potrebni podaci uneseni na ekranu. */
	private boolean checkParameters()
    {
		Date date_from = (Date)ra.getAttribute(ldbName, "CollLoanStatus_txtDateFrom");
		Date date_until = (Date)ra.getAttribute(ldbName, "CollLoanStatus_txtDateUntil");
		String status = (String)ra.getAttribute(ldbName, "CollLoanStatus_txtStatusCode");
		String client_type = (String)ra.getAttribute(ldbName, "CollLoanStatus_txtClientType");
		
    	if(date_from == null || date_until == null || status == null || status.trim().length() == 0 || client_type == null || client_type.trim().length() == 0)
    	{
    		ra.showMessage("wrn152");
    		return false;
    	}
    	return true;
    }
	
	/**
	 * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
	 * Parametri se predaju u obliku RB;col_cat_id;org_uni_id;date_from;date_until;status;client_type;X
	 */
	private String generateBatchParam()
	{
		StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign")).append(";");
		buffer.append(isEmpty((BigDecimal)ra.getAttribute(ldbName, "COL_CAT_ID"))).append(";");
		buffer.append(isEmpty((BigDecimal)ra.getAttribute(ldbName, "ORG_UNI_ID"))).append(";");
		buffer.append(getDDMMYYYY(((Date)ra.getAttribute(ldbName, "CollLoanStatus_txtDateFrom")))).append(";");
		buffer.append(getDDMMYYYY(((Date)ra.getAttribute(ldbName, "CollLoanStatus_txtDateUntil")))).append(";");
		buffer.append((String) ra.getAttribute(ldbName, "CollLoanStatus_txtStatusCode")).append(";");
		buffer.append((String) ra.getAttribute(ldbName, "CollLoanStatus_txtClientType")).append(";");
        buffer.append("X");
        return buffer.toString();
    }
	
    /** Validacijska metoda za polja s podacima o organizacijskoj jedinici. */
	public boolean CollLoanStatus_Org_FV(String elementName, Object elementValue, Integer lookUpType)
	{
        // poništi sva polja (osim trenutnog) koja su vezana za organizacijsku jedinicu
    	ra.setAttribute(ldbName, "ORG_UNI_ID", null);
        if(!elementName.equals("CollLoanStatus_txtOrgCode")) ra.setAttribute(ldbName, "CollLoanStatus_txtOrgCode", "");
        if(!elementName.equals("CollLoanStatus_txtOrgName")) ra.setAttribute(ldbName, "CollLoanStatus_txtOrgName", "");
        if(elementValue == null || elementValue.equals("")) return true;
 	
		// inicijaliziraj i pozovi lookup
        LookUpRequest lu = new LookUpRequest("OrgUniLookUp");
		lu.addMapping(ldbName, "CollLoanStatus_txtOrgCode", "code");
		lu.addMapping(ldbName, "CollLoanStatus_txtOrgName", "name");
		lu.addMapping(ldbName, "ORG_UNI_ID", "org_uni_id");
		return callLookUp(lu);
	}

    /** Validacijska metoda za polja s podacima o kategoriji kolaterala. */
	public boolean CollLoanStatus_Cat_FV(String elementName, Object elementValue, Integer lookUpType)
	{
        // poništi sva polja (osim trenutnog) koja su vezana za kategoriju kolaterala
    	ra.setAttribute(ldbName, "COL_CAT_ID", null);
        if(!elementName.equals("CollLoanStatus_txtCatCode")) ra.setAttribute(ldbName, "CollLoanStatus_txtCatCode", "");
        if(!elementName.equals("CollLoanStatus_txtCatName")) ra.setAttribute(ldbName, "CollLoanStatus_txtCatName", "");
        if(elementValue == null || elementValue.equals("")) return true;
 	
		// inicijaliziraj i pozovi lookup
        LookUpRequest lu = new LookUpRequest("CollCategoryLookUp");
		lu.addMapping(ldbName, "COL_CAT_ID", "col_cat_id");
		lu.addMapping(ldbName, "CollLoanStatus_txtCatCode", "code");
		lu.addMapping(ldbName, "CollLoanStatus_txtCatName", "name");
		return callLookUp(lu);
	}
	
	/** Validacijska metoda za polje s vrstom klijenta. */
	public boolean CollLoanStatus_ClientType_FV(String elementName, Object elementValue, Integer lookUpType)
	{
		LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");		
		request.addMapping(ldbName, "CollLoanStatus_txtClientType", "Vrijednosti");
		return callLookUp(request);
	}
	
	/** Validacijska metoda za polja s podacima o statusu plasmana. */
	public boolean CollLoanStatus_Status_FV(String elementName, Object elementValue, Integer lookUpType)
	{
		LookUpRequest request = new LookUpRequest("LoanStatusDefLookUp");		
		request.addMapping(ldbName, "CollLoanStatus_txtStatusCode", "Šifra");
		request.addMapping(ldbName, "CollLoanStatus_txtStatusName", "Naziv");
		return callLookUp(request);
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
    
    private String isEmpty(BigDecimal bd)
    {
        if (bd == null) return " "; else return bd.toString();
    }
    
    
    

    
    public boolean CollLoanStatus_txtDateFrom_FV () {
        
        Date datum_od = (Date) ra.getAttribute("CollLoanStatusLDB", "CollLoanStatus_txtDateFrom");   
        Date datum_do = (Date) ra.getAttribute("CollLoanStatusLDB", "CollLoanStatus_txtDateUntil");   
        
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        if (datum_od == null || datum_do == null) 
            return true;

        if ((datum_do).before(datum_od)) {
            ra.showMessage("wrnclt175");
            return false;
        }  

        return true;
    }        
    
}