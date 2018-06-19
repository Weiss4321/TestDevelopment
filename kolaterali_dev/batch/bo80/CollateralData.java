package hr.vestigo.modules.collateral.batch.bo80;

import java.math.BigDecimal;
import java.sql.Date;


public class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo80/CollateralData.java,v 1.3 2017/05/04 10:32:00 hrakis Exp $";
    
    public BigDecimal col_hea_id;
    public String col_num;
    public BigDecimal col_cat_id;
    public BigDecimal col_typ_id;
    public String col_type_name;
    public BigDecimal coll_hf_prior_id;
    public String hf_priority;
    public BigDecimal cus_acc_id;
    public String cus_acc_no;
    public Date placement_due_date;
    public String register_no;
    public String name;
    public boolean isCorporate;
    public Date fra_agr_date_until;
    
    public String subtype;
    public BigDecimal collateral_value_eur;
    public BigDecimal placement_exposure_eur;
    public Date collateral_due_date;
}