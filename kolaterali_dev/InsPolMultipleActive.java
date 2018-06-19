package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * Handler klasa za ekran Izvješæe o kolateralima s više aktivnih polica osiguranja
 * @author hrakis
 */
public class InsPolMultipleActive extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsPolMultipleActive.java,v 1.1 2017/03/13 12:04:19 hrakis Exp $";
    private final String ldbName = "InsPolMultipleActiveLDB";
    
    public InsPolMultipleActive(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void InsPolMultipleActive_SE()
    {
        ra.createLDB(ldbName);

        ra.setAttribute(ldbName, "InsPolMultipleActive_txtCustType", "F");
        ra.invokeValidation("InsPolMultipleActive_txtCustType");
    }

    /** Metoda koja naruèuje batch obradu. */
    public void order()
    {
        try
        {
            // if(!ra.isRequiredFilled()) return;
            String parameters = getBatchParameters();
            final String batchLDB = "BatchLogDialogLDB";
            if (!ra.isLDBExists(batchLDB)) ra.createLDB(batchLDB);
            ra.setAttribute(batchLDB, "BatchDefId", new BigDecimal("1221846327"));
            ra.setAttribute(batchLDB, "RepDefId", null);
            ra.setAttribute(batchLDB, "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute(batchLDB, "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute(batchLDB, "fldParamValue", parameters);
            ra.setAttribute(batchLDB, "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute(batchLDB, "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");
        }
        catch (VestigoTMException vtme)
        {
            error("InsPolMultipleActive.order() -> VestigoTMException", vtme);
        }
    }
    
    /**
     * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;policy_type;cust_type
     */
    private String getBatchParameters()
    {
        StringBuffer buffer = new StringBuffer();        
        buffer.append((String)ra.getAttribute("GDB", "bank_sign")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "InsPolMultipleActive_txtCustType"));
        return buffer.toString();
    }
    
    /** Validacijska metoda za vrstu komitenta. */
    public boolean InsPolMultipleActive_CustType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String)elementValue).equals("")) return true;
        
        LookUpRequest request = new LookUpRequest("ClientTypeDefLookUp");       
        request.addMapping(ldbName, "InsPolMultipleActive_txtCustType", "Vrijednosti");
        if (!callLookUp(request)) return false;
        
        String cust_type = (String)ra.getAttribute(ldbName, "InsPolMultipleActive_txtCustType");
        if ("P".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolMultipleActive_txtCustTypeDesc", "Pravne osobe");
        else if ("F".equalsIgnoreCase(cust_type)) ra.setAttribute(ldbName, "InsPolMultipleActive_txtCustTypeDesc", "Fizi\u010Dke osobe");
        
        return true;
    }
   
    /** 
     * Metoda koja poziva zadani lookup
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
}