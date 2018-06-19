//created 2009.09.16
package hr.vestigo.modules.collateral.javatran;

/**
 *
 * @author hramkr
 */
    
import hr.vestigo.framework.common.VConstants;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.math.BigDecimal;
import java.util.List;
           
/**  
 * @author HRAMKR
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * dohvat MVP pondera za kolaterale
 */
public class KolPonderNewTran extends LookUpTransaction {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/KolPonderNewTran.java,v 1.1 2009/10/01 06:49:35 hramkr Exp $";
    public KolPonderNewTran(ResourceAccessor ra) {
        super(ra); 
    } 
      
    public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType)
        throws Exception {
        
        this.debug("U look up-u sam prije upita");
         String CollCategory = (String) searchableParams.get(0);
         
        
        BigDecimal col_cat_id = null;
        col_cat_id = (BigDecimal)ra.getAttribute("CollateralTypeLookUpLDB", "CollateralCatId");    
        String SQLQuery = "";
        
        if (lookUpType == VConstants.PLAIN_LOOKUP) {
            
            if (col_cat_id.compareTo(new BigDecimal("618223")) == 0 ||
                    col_cat_id.compareTo(new BigDecimal("620223")) == 0 ||
                    col_cat_id.compareTo(new BigDecimal("621223")) == 0 ||
                    col_cat_id.compareTo(new BigDecimal("624223")) == 0) { 
                if (CollCategory.indexOf("%")<0){   
                    SQLQuery =  
                        "SELECT distinct b.coll_type_id as col_typ_id, " +
                        " b.coll_type_code as code, b.coll_type_name as name," +
                        " b.coll_mvp_ponder_mn as min_value, b.coll_mvp_ponder as dfl_value, b.coll_mvp_ponder_mx as max_value" +
                        " FROM collateral_type b WHERE"
                        + " b.col_cat_id = " + col_cat_id.toString()
                        + " AND b.coll_status = 'A' AND b.coll_spec_status = 'A'"
                        + " AND rtrim(b.coll_type_code) = ? AND b.coll_type_name like ?";                   
                } else {
                SQLQuery =  
                    "SELECT distinct b.coll_type_id as col_typ_id, " +
                    " b.coll_type_code as code, b.coll_type_name as name," +
                    " b.coll_mvp_ponder_mn as min_value, b.coll_mvp_ponder as dfl_value, b.coll_mvp_ponder_mx as max_value" +
                    " FROM collateral_type b WHERE"
                    + " b.col_cat_id = " + col_cat_id.toString()
                    + " AND b.coll_status = 'A' AND b.coll_spec_status = 'A'"
                    + " AND b.coll_type_code LIKE ? AND b.coll_type_name like ?";
                }  
                
            } else {
                if (CollCategory.indexOf("%")<0){            
                    SQLQuery =  
                        "SELECT distinct a.col_typ_id as col_typ_id, " +
                        " b.coll_type_code as code, b.coll_type_name as name," +
                        " a.min_value as min_value, a.dfl_value as dfl_value, a.max_value as max_value" +
                        " FROM collateral_type b LEFT OUTER JOIN dfl_col_ponder a ON (b.col_cat_id = a.col_cat_id and b.coll_type_id=a.col_typ_id)" +
                        " WHERE " +
                        " b.col_cat_id = " + col_cat_id.toString() +
                        " AND b.coll_status = 'A' AND b.coll_spec_status = 'A'" +
                        " AND (a.status = 'A' or a.status is null)" +
                        " AND  rtrim(b.coll_type_code) = ? AND b.coll_type_name like ?";        
                } else {
                    SQLQuery =  
                        "SELECT distinct a.col_typ_id as col_typ_id, " +
                        " b.coll_type_code as code, b.coll_type_name as name," +
                        " a.min_value as min_value, a.dfl_value as dfl_value, a.max_value as max_value" +
                        " FROM collateral_type b LEFT OUTER JOIN dfl_col_ponder a ON (b.col_cat_id = a.col_cat_id and b.coll_type_id=a.col_typ_id)" +
                        " WHERE " +
                        " b.col_cat_id = " + col_cat_id.toString() +
                        " AND b.coll_status = 'A' AND b.coll_spec_status = 'A'" +
                        " AND (a.status = 'A' or a.status is null)" +
                        " AND b.coll_type_code LIKE ? AND b.coll_type_name like ?";                    
                }
            }
        } 
  
    
        this.debug("SQLQuery je " + SQLQuery);
        return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);
        
    }   
}  

     
