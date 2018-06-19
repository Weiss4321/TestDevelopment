//created 2013.11.22
package hr.vestigo.modules.collateral.jcics.co40;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author hraaks
 */
public class CoHistoryData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co40/CoHistoryData.java,v 1.1 2013/11/22 11:12:48 hraaks Exp $";
    
    public BigDecimal CO_CHG_HIS_ID;
    public BigDecimal col_hea_id;
    public String CO_IND;
    public BigDecimal CO_USE_ID;
    public Timestamp CO_TS;
    public Timestamp CO_CHG_TS;
    public BigDecimal CO_CHG_USE_ID;
    public BigDecimal REAL_EST_EUSE_ID;
    public BigDecimal ESTIMATE_CUS_ID;
    public BigDecimal REAL_EST_ESTN_VALU;
    public BigDecimal REAL_EST_NM_CUR_ID;
    public BigDecimal NEW_BUILD_VAL;
    public String REAL_EST_NOMI_DESC;
    public Date REAL_EST_ESTN_DATE;
}

