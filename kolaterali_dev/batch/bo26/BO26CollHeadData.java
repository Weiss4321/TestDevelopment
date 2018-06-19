/*
 * hrazst Created on 2007.06.28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo26;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO26CollHeadData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo26/BO26CollHeadData.java,v 1.1 2008/08/14 13:07:12 hrazst Exp $";   
    
    /**
     * Podklasa u koju se dohvate podaci iz coll_head tablice koji koriste u obradi 
     * @author hrazst
     *
     */
    public class CollHeadDataToProcess{
    
        public BigDecimal col_hea_id=null;
        public BigDecimal col_typ_id=null;
        public BigDecimal real_est_nomi_value=null;
        public Date real_est_nomi_date=null;
        public BigDecimal real_est_nm_cur_id=null;
        public String cur_code=null;
        public String collateral_status=null;
        public String reva_date_am=null;
    
        public String toString(){
            StringBuffer ret=new StringBuffer();
            
            ret.append("\ncol_hea_id=[").append(col_hea_id).append("]\n");
            ret.append("col_typ_id=[").append(col_typ_id).append("]\n");
            ret.append("real_est_nomi_value=[").append(real_est_nomi_value).append("]\n");
            ret.append("real_est_nomi_date=[").append(real_est_nomi_date).append("]\n");
            ret.append("real_est_nm_cur_id=[").append(real_est_nm_cur_id).append("]\n");
            ret.append("cur_code=[").append(cur_code).append("]\n");
            ret.append("collateral_status=[").append(collateral_status).append("]\n");
            ret.append("reva_date_am=[").append(reva_date_am).append("]\n");
            
            return ret.toString();        
        }
    }
    
    /**
     * Podklasa u koju se pohranjuju rezultati obrade(revalorizacije) i koji se onda update-aju u tablicu coll_head
     * @author hrazst
     *
     */
    public class CollHeadDataProcessed{
        public BigDecimal real_est_nomi_value=null;
        public Date real_est_nomi_date=null;
        public BigDecimal reva_coef = null;
        public Date reva_date = null;
        public String reva_date_am = null;
        public BigDecimal reva_bvalue = null;
        public Date reva_bdate = null;
        public String reva_bdate_am = null;
        
        public String toString(){
            StringBuffer ret=new StringBuffer();
            
            ret.append("\nreal_est_nomi_value=[").append(real_est_nomi_value).append("]\n");
            ret.append("real_est_nomi_date=[").append(real_est_nomi_date).append("]\n");
            ret.append("reva_coef=[").append(reva_coef).append("]\n");
            ret.append("reva_date=[").append(reva_date).append("]\n");
            ret.append("reva_date_am=[").append(reva_date_am).append("]\n");
            ret.append("reva_bvalue=[").append(reva_bvalue).append("]\n");
            ret.append("reva_bdate=[").append(reva_bdate).append("]\n");
            ret.append("reva_bdate_am=[").append(reva_bdate_am).append("]\n");
            
            return ret.toString();        
        }
    }
    
    
    public class ColTurnoverData{
        
        public BigDecimal col_pro_id=null;
        public String col_num=null;
        public BigDecimal col_hea_id=null;
        public BigDecimal coll_type_id=null;
        public BigDecimal col_sub_id=null;
        public BigDecimal amount=null;
        public BigDecimal amount_proc=null;
        public String amort_age=null;
        public BigDecimal proc_perc=null;
        public Date date_from=null;
        public Date date_until=null;
        public String proc_period=null;
        public String proc_status=null;
        
        public String toString(){
            StringBuffer ret=new StringBuffer();
            
            ret.append("\ncol_pro_id=[").append(col_pro_id).append("]\n");
            ret.append("col_num=[").append(col_num).append("]\n");
            ret.append("col_hea_id=[").append(col_hea_id).append("]\n");
            ret.append("coll_type_id=[").append(coll_type_id).append("]\n");
            ret.append("col_sub_id=[").append(col_sub_id).append("]\n");
            ret.append("amount=[").append(amount).append("]\n");
            ret.append("amount_proc=[").append(amount_proc).append("]\n");
            ret.append("amort_age=[").append(amort_age).append("]\n");
            ret.append("proc_perc=[").append(proc_perc).append("]\n");
            ret.append("date_from=[").append(date_from).append("]\n");
            ret.append("date_until=[").append(date_until).append("]\n");
            ret.append("proc_period=[").append(proc_period).append("]\n");
            ret.append("proc_status=[").append(proc_status).append("]\n");           
            
            return ret.toString();        
        }
        
        
    }
    
    
    
    
    
    
}
