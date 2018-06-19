package hr.vestigo.modules.collateral.batch.boA0;

import java.math.BigDecimal;
import java.util.HashSet;


public class AnalyticData
{
    public BigDecimal col_hea_id;
    public String col_num;
    public BigDecimal col_cat_id;
    public BigDecimal real_est_nomi_valu;
    public BigDecimal real_est_nm_cur_id;
    public String real_est_nm_cur_code;
    public BigDecimal acum_buy_value;
    public String cus_acc_no;
    public String request_no;
    public String cus_acc_status;
    public String cus_acc_orig_st;
    public String placement_owner_register_no;
    public String placement_owner_name;
    public BigDecimal collateral_owner_cus_id;
    public String collateral_owner_register_no;
    public String collateral_owner_name;
    
    public BigDecimal value_hrk;
}

class SynteticData
{
    public BigDecimal collateral_owner_cus_id;
    public String collateral_owner_register_no;
    public String collateral_owner_name;
    
    public BigDecimal value_hrk;
    public HashSet<BigDecimal> collaterals = new HashSet<BigDecimal>();
}