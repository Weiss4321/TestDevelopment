package hr.vestigo.modules.collateral.batch.bo53;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;


public class ReportCDepData{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo53/ReportCDepData.java,v 1.1 2010/03/25 10:47:23 hramlo Exp $";
    
    String col_num =null;
    String collateral_status=null;
    String cm_cde_account=null;
    Date cm_cde_dep_until=null;
    String code_char=null;
    BigDecimal cm_cde_amount=null;
    BigDecimal dwh_cde_account=null;
    Timestamp create_ts=null;
    String register_no=null;
    String name=null;
    String b2_asset=null;
    
    
}