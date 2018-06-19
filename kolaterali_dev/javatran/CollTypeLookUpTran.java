//created 2015.01.12
package hr.vestigo.modules.collateral.javatran;

import hr.vestigo.framework.common.VConstants;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.util.List;

/**
 *
 * @author hrajkl
 */
public class CollTypeLookUpTran extends LookUpTransaction {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/CollTypeLookUpTran.java,v 1.1 2015/01/13 08:26:43 hrajkl Exp $";

    public CollTypeLookUpTran(ResourceAccessor ra) {
        super(ra);
    }
  
    public VestigoResultSet getResultSet(List searchableParams, String dbName, int lookUpType) throws Exception {
        
        System.out.println("\n\n\n CollTypeLookUpTran \n\n\n");
        String SQL = null;
        
        if (lookUpType == VConstants.PLAIN_LOOKUP) {
            SQL = " SELECT ct.coll_type_id, ct.coll_type_code, ct.coll_type_name  FROM COLL_CATEGORY cc LEFT OUTER JOIN COLLATERAL_TYPE ct on cc.CODE = ct.SUB_CATEGORY WHERE cc.CODE LIKE ? ORDER BY ct.COLL_TYPE_CODE ASC ";
            
        }
        
        System.out.println("searchableParams:"+searchableParams);
        this.debug("SQLQuery je " + SQL);
        return ra.getMetaModelDB().selectPrepare(SQL, searchableParams, dbName);
    }

}
