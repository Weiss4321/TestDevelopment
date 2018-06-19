//created 2009.09.16
package hr.vestigo.modules.collateral.javatran;

/**
 *
 * @author hramkr
 * * dohvat MVP pondera za kolaterale
 */

import hr.vestigo.framework.common.VConstants;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.LookUpTransaction;
import hr.vestigo.framework.util.db.VestigoResultSet;

import java.math.BigDecimal;
import java.util.List;
     
   
public class KolSubPonderNewTran extends LookUpTransaction
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/javatran/KolSubPonderNewTran.java,v 1.2 2014/03/11 14:29:14 hrakis Exp $";
    
    public KolSubPonderNewTran(ResourceAccessor ra)
    {
        super(ra); 
    } 
      
    public VestigoResultSet getResultSet(List searchableParams, String dbname, int lookUpType) throws Exception
    {
        this.debug("U look up-u sam prije upita");
        
        BigDecimal col_cat_id = (BigDecimal)ra.getAttribute("CollateralTypeLookUpLDB", "CollateralCatId");;
        BigDecimal col_typ_id = (BigDecimal)ra.getAttribute("CollateralTypeLookUpLDB", "CollateralTypId");;
        
        String CollCategory = (String)searchableParams.get(0);
        if (CollCategory != null) {
            if (CollCategory.indexOf("%") < 0){
                CollCategory = CollCategory + "%";              
            }
        }
        searchableParams.remove(0);
        searchableParams.add(0, CollCategory);
        
        String SQLQuery = "";
        
        if (lookUpType == VConstants.PLAIN_LOOKUP)
        {
            if (col_cat_id.compareTo(new BigDecimal("618223")) == 0)   // nekretnine
            {
                SQLQuery =  
                    "SELECT DISTINCT b.real_es_type_id AS col_sub_id, " +
                    " b.real_es_type_code AS code, b.real_es_type_desc AS name," +
                    " a.min_value AS min_value, a.dfl_value AS dfl_value, a.max_value AS max_value" +
                    " FROM dfl_col_ponder a, real_estate_type b" +
                    " WHERE" +
                    " a.col_cat_id = " + col_cat_id.toString() + 
                    " AND a.col_typ_id = " + col_typ_id.toString() +  
                    " AND (a.status = 'A' OR a.status IS NULL)" + 
                    " AND (a.add_request = 'N' OR a.add_request IS NULL)" + 
                    " AND a.col_typ_id = b.coll_type_id" +
                    " AND (a.col_sub_id = b.real_es_type_id OR a.col_sub_id IS NULL)" +
                    " AND b.real_es_type_code LIKE ? AND b.real_es_type_desc LIKE ?";
            }
            else if (col_cat_id.compareTo(new BigDecimal("620223")) == 0)   // plovila
            {
                SQLQuery =  
                    "SELECT DISTINCT b.ves_typ_id AS col_sub_id, " +
                    " b.ves_code AS code, b.ves_dsc AS name," +
                    " a.min_value AS min_value, a.dfl_value AS dfl_value, a.max_value AS max_value" +
                    " FROM dfl_col_ponder a, vessel_type b" +
                    " WHERE" +
                    " a.col_cat_id = " + col_cat_id.toString()+ 
                    " AND a.col_typ_id = 15777" +
                    " AND (a.status = 'A' OR a.status IS NULL)" +
                    " AND (a.add_request = 'N' OR a.add_request IS NULL)" + 
                    " AND (a.col_sub_id = b.ves_typ_id OR a.col_sub_id IS NULL)" +
                    " AND b.ves_code LIKE ? AND b.ves_dsc LIKE ?";
            }
            else if (col_cat_id.compareTo(new BigDecimal("621223")) == 0)  // pokretnine
            {
                SQLQuery =  
                    "SELECT DISTINCT b.mov_typ_id AS col_sub_id, " +
                    " b.mov_typ_code AS code, b.mov_typ_dsc AS name," +
                    " a.min_value AS min_value, a.dfl_value AS dfl_value, a.max_value AS max_value" +
                    " FROM dfl_col_ponder a, movable_type b" +
                    " WHERE" +
                    " a.col_cat_id = " + col_cat_id.toString() +
                    " AND a.col_typ_id = " + col_typ_id.toString() +  
                    " AND (a.status = 'A' OR a.status IS NULL)"  +
                    " AND (a.add_request = 'N' OR a.add_request IS NULL)" +
                    " AND a.col_typ_id = b.col_typ_id" +
                    " AND (a.col_sub_id = b.mov_typ_id OR a.col_sub_id IS NULL)" +
                    " AND b.mov_typ_code LIKE ? AND b.mov_typ_dsc LIKE ?";
            }
            else if (col_cat_id.compareTo(new BigDecimal("624223")) == 0)   // vozila
            {
                SQLQuery =  
                    "SELECT DISTINCT b.veh_gro_id AS col_sub_id, " +
                    " b.veh_gro_code AS code, b.veh_gro_desc AS name," +
                    " a.min_value AS min_value, a.dfl_value AS dfl_value, a.max_value AS max_value" +
                    " FROM dfl_col_ponder a, vehicle_group b" +
                    " WHERE" +
                    " a.col_cat_id = " + col_cat_id.toString()+ 
                    " AND a.col_typ_id = " + col_typ_id.toString() +  
                    " AND (a.status = 'A' OR a.status IS NULL)" + 
                    " AND (a.add_request = 'N' OR a.add_request IS NULL)" +
                    " AND a.col_typ_id = b.coll_type_id" +
                    " AND (a.col_sub_id = b.veh_gro_id OR a.col_sub_id IS NULL)" +
                    " AND b.veh_gro_code LIKE ? AND b.veh_gro_desc LIKE ?";
             }
        }
        
        this.debug("SQLQuery je " + SQLQuery);
        return ra.getMetaModelDB().selectPrepare(SQLQuery, searchableParams, dbname);  
    }
}