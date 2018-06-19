//created 2010.06.15
package hr.vestigo.modules.collateral;


import java.math.BigDecimal;
import java.sql.Timestamp;

import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
/**
 *
 * @author hramlo
 * Handler za ekran narucivanja Liste partiija depozita i DWH povezanosti
 */
public class CollDepDWH extends Handler{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollDepDWH.java,v 1.2 2010/07/06 09:22:04 hramlo Exp $";
    
    public CollDepDWH(ResourceAccessor ra){
        super(ra);
    }
    
    
    public void CollDepDWH_SE() {
        
     
    }
    
    /** Metoda koja naruèuje batch obradu. */
    public void orderCSV(){
        try{
            
            //System.out.println("unutar  orderCSV u HAndleru sam!");       
            String parametri = generateBatchParam();
            if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("3019525084"));
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
    
    
    /**
     * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;date;X
     */
    private String generateBatchParam()
    {
        StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign"));
        return buffer.toString();
    }
    
    
    
    
    
}

