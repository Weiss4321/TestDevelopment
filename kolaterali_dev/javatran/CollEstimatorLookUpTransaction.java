//created 2014.09.04
package hr.vestigo.modules.collateral.javatran;

import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author hrazst
 */
public class CollEstimatorLookUpTransaction extends LookUpTransaction {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/CollEstimatorLookUpTransaction.java,v 1.1 2014/10/07 10:54:20 hrazst Exp $";

    public CollEstimatorLookUpTransaction(ResourceAccessor ra) {
        super(ra);
    }

    public VestigoResultSet getResultSet(List searchableParams, String dbName, int lookUpType) throws Exception {        
           String SQL=null;

           Date estDate =(Date) ra.getAttribute("CollEstimatorLookUpLDB", "estDate");
           String reg_no = (String) ra.getAttribute("CollEstimatorLookUpLDB", "est_register_no");
           String name = (String) ra.getAttribute("CollEstimatorLookUpLDB", "est_name");    
           
           SQL = "SELECT e.est_cus_id, c.register_no as est_register_no, c.name as est_name, scv.sys_code_desc as est_type, c.tax_number as est_tax_number, " 
                   + "e.est_comp_cus_id, c1.register_no as est_comp_register_no, c1.name as est_comp_name, c1.tax_number as est_comp_tax_number, e.est_type as est_type_code "
                   + "FROM ESTIMATOR e "
                   + "INNER JOIN CUSTOMER c ON e.est_cus_id=c.cus_id "
                   + "INNER JOIN CUSTOMER c1 ON e.est_comp_cus_id=c1.cus_id "
                   + "INNER JOIN SYSTEM_CODE_VALUE scv ON (e.est_type=scv.sys_code_value AND scv.sys_cod_id='est_type') "
                   + "WHERE '" + estDate + "' BETWEEN e.date_from AND e.date_until "
                   + "AND c.register_no LIKE ? "
                   + "AND c.name LIKE ? ";
           
            return ra.getMetaModelDB().selectPrepare(SQL, searchableParams, dbName);            
        }       
    }
