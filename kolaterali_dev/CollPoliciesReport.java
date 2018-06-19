package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

/**
* 
* @author hrakis
* Handler klasa za ekran Izvješæe o policama
*
*/
public class CollPoliciesReport extends Handler
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollPoliciesReport.java,v 1.1 2009/05/11 13:47:16 hrakis Exp $";
	private final String ldbName = "CollPoliciesReportLDB";
	
	public CollPoliciesReport(ResourceAccessor arg0)
	{
		super(arg0);
	}
	
	public void CollPoliciesReport_SE()
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
        	if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("41567243"));
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
            error("CollPoliciesReport.orderCSV() -> VestigoTMException", vtme);
        } 
	}
	
    /** Metoda koja provjerava da li su svi potrebni podaci uneseni na ekranu. */
	private boolean checkParameters()
    {
    	return true;  // sva polja su opcionalna
    }
	
	/**
	 * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
	 * Parametri se predaju u obliku RB;org_uni_id;client_type;X
	 */
	private String generateBatchParam()
	{
		StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign")).append(";");
		buffer.append(isEmpty((BigDecimal)ra.getAttribute(ldbName, "ORG_UNI_ID"))).append(";");
		buffer.append(isEmpty((String)ra.getAttribute(ldbName, "CollPoliciesReport_txtClientType"))).append(";");
        buffer.append("X");
        return buffer.toString();
    }
	
    /** Validacijska metoda za polja s podacima o organizacijskoj jedinici. */
	public boolean CollPoliciesReport_Org_FV(String elementName, Object elementValue, Integer lookUpType)
	{
        // poništi sva polja (osim trenutnog) koja su vezana za organizacijsku jedinicu
    	ra.setAttribute(ldbName, "ORG_UNI_ID", null);
        if(!elementName.equals("CollPoliciesReport_txtOrgCode")) ra.setAttribute(ldbName, "CollPoliciesReport_txtOrgCode", "");
        if(!elementName.equals("CollPoliciesReport_txtOrgName")) ra.setAttribute(ldbName, "CollPoliciesReport_txtOrgName", "");
        if(elementValue == null || elementValue.equals("")) return true;
 	
		// inicijaliziraj i pozovi lookup
        LookUpRequest lu = new LookUpRequest("OrgUniLookUp");
		lu.addMapping(ldbName, "CollPoliciesReport_txtOrgCode", "code");
		lu.addMapping(ldbName, "CollPoliciesReport_txtOrgName", "name");
		lu.addMapping(ldbName, "ORG_UNI_ID", "org_uni_id");
		return callLookUp(lu);
	}

	/** Validacijska metoda za polje s vrstom klijenta. */
	public boolean CollPoliciesReport_ClientType_FV(String elementName, Object elementValue, Integer lookUpType)
	{
		LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");		
		request.addMapping(ldbName, "CollPoliciesReport_txtClientType", "Vrijednosti");
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
	
    private String isEmpty(BigDecimal bd)
    {
        if (bd == null) return " "; else return bd.toString();
    }
    
    private String isEmpty(String s)
    {
        if (s == null || s.equals("")) return " "; else return s.toString();
    }
}