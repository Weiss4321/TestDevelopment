/*
 * hrazst Created on 2007.06.28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo29;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO29Data {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo29/BO29Data.java,v 1.1 2008/09/10 11:56:26 hrazst Exp $";   
    
    /**
     * Podklasa u koju se dohvate podaci iz coll_head tablice koji koriste u obradi 
     * @author hrazst
     *
     */
    public class CollHfPriorData{
        BigDecimal reg_ins=null;
        String veh_con_num=null;
        String con_num=null;
        String reg_place=null;
        String rec_lop=null;
        Date con_date=null;
        Date date_rec_lop=null;
        Date hf_date_reciv=null;
        BigDecimal hf_hfc_id=null;
        Date hf_date_hfc_from=null;
        
        public String toString(){
            StringBuffer ret=new StringBuffer();
            
            ret.append("\\nreg_ins=[").append(reg_ins).append("]\n");
            ret.append("veh_con_num=[").append(veh_con_num).append("]\n");
            ret.append("con_num=[").append(con_num).append("]\n");
            ret.append("reg_place=[").append(reg_place).append("]\n");
            ret.append("rec_lop=[").append(rec_lop).append("]\n");
            ret.append("con_date=[").append(con_date).append("]\n");
            ret.append("date_rec_lop=[").append(date_rec_lop).append("]\n");
            ret.append("hf_date_reciv=[").append(hf_date_reciv).append("]\n");
            ret.append("hf_hfc_id=[").append(hf_hfc_id).append("]\n");
            ret.append("hf_date_hfc_from=[").append(hf_date_hfc_from).append("]\n");
            
            return ret.toString();        
        }
    }
  
    public class InDataDwnItem{
        
        public BigDecimal col_pro_id=null;
        public BigDecimal input_id=null;
        public String input_code=null;
        public String status=null;
        public BigDecimal output_id=null;
        
        public String toString(){
            StringBuffer ret=new StringBuffer();
            
            ret.append("\ncol_pro_id=[").append(col_pro_id).append("]\n");
            ret.append("input_id=[").append(input_id).append("]\n");
            ret.append("input_code=[").append(input_code).append("]\n");
            ret.append("status=[").append(status).append("]\n");
            ret.append("output_id=[").append(output_id).append("]\n");     
            
            return ret.toString();        
        }
        
        
    }
    
    
    
    
    
    
}
