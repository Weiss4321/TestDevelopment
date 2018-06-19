//created 2015.03.04
package hr.vestigo.modules.collateral.batch.bo98;

import hr.vestigo.modules.rba.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author hrajkl
 */
public class EstimateData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo98/EstimateData.java,v 1.1 2015/03/13 11:48:09 hrajkl Exp $";

    BigDecimal coll_hea_id = null;
    Date real_est_estn_date = null;
    
    public EstimateData(BigDecimal coll_hea_id, Date real_est_estn_date) {
        this.coll_hea_id = coll_hea_id;
        this.real_est_estn_date = real_est_estn_date;
    }
    
    public boolean equals(EstimateData obj) {

        
        System.out.println(""+this.coll_hea_id+":"+this.real_est_estn_date+"  -  "+obj.coll_hea_id+":"+obj.real_est_estn_date);
        
        if(this.coll_hea_id == null || this.real_est_estn_date == null){//ovime se osigurava zapis prvog sloga dohvaæenog sql-om
            return false;
        }else if (this.coll_hea_id.compareTo(obj.coll_hea_id)==0 && DateUtils.whoIsOlder(this.real_est_estn_date, obj.real_est_estn_date)==0 ) {
            return true;
        }
        return false;
    }
}

