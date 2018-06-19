package hr.vestigo.modules.collateral;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;

/**   
 * @author hrakis
 */ 
public class CollEligDetails extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollEligDetails.java,v 1.1 2010/08/16 12:11:55 hrakis Exp $";
    private String LDBName = "CollEligDetailsLDB";
    
    public CollEligDetails(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollEligDetails_SE()
    {
        if(!ra.isLDBExists(LDBName)) ra.createLDB(LDBName);
    }
    
    public void search()
    {
        try {
            ra.executeTransaction();
        } catch (VestigoTMException vtme) {
            if (vtme.getMessageID() != null) ra.showMessage(vtme.getMessageID());
        }
    }
}
