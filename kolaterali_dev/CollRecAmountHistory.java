//created 2017.04.21
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.modules.collateral.util.CollateralUtil;

/**
 *
 * @author hrazst
 */
public class CollRecAmountHistory extends Handler {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollRecAmountHistory.java,v 1.1 2017/04/25 08:00:13 hrazst Exp $";
    CollateralUtil coll_util = null;
    
    public CollRecAmountHistory(ResourceAccessor ra) {
        super(ra);
        coll_util = new CollateralUtil(ra);
    }
    
    public void CollRecAmountHistory_SE() {
        try{
            ra.executeTransaction();
        }catch (Exception e) {
            coll_util.ShowInfoMessage("Greška pri dohvatu podataka");
        }
    }    
}

