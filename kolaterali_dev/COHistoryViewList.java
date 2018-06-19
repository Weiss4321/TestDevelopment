//created 2012.09.27
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

/**
 * Handler za ekran COHistoryViewList
 * @author hradnp
 */
public class COHistoryViewList extends Handler{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/COHistoryViewList.java,v 1.1 2012/10/10 10:07:29 hradnp Exp $";
    /**
     * Handler constructor.
     * @param ra resource accessor passed to class when creating object from it.
     */
    public COHistoryViewList(ResourceAccessor ra) {
        super(ra);
    }
    
    public void  COHistoryViewList_SE(){ 
        System.out.println("Usao sam u screen entry");
        if(!ra.isLDBExists("COHistoryViewListLDB"))
            ra.createLDB("COHistoryViewListLDB");    
        
        ra.createActionListSession("tblCOHistoryViewList",true);
    }
}
