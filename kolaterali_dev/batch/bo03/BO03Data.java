package hr.vestigo.modules.collateral.batch.bo03;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 */
public class BO03Data
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo03/BO03Data.java,v 1.13 2017/10/30 12:48:24 hrakis Exp $";
    
    public BigDecimal cus_acc_id;
    public String placement_owner_code; 
    public String placement_owner_name; 
    public String module_code; 
    public String cus_acc_no; 
    public String contract_no; 
    public String cus_acc_orig_st; 
    public String exposure_code_char; 
    public BigDecimal exposure_balance; 
    public Date exposure_date;
}


/*class DuplicateData
{
    public String cus_acc_no;
    public String contract_cur;
    public String status;
    public Timestamp create_ts;
}*/

class DefaultData
{
    public BigDecimal cus_acc_exp_def_id;
    public BigDecimal cus_acc_id;
    public Date date_from;
    public Date date_until;
}