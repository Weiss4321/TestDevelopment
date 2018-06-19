package hr.vestigo.modules.collateral.batch.bo95;

import java.math.BigDecimal;


public class BO95Data
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo95/BO95Data.java,v 1.3 2016/12/05 14:39:18 hrakis Exp $";
    
    public BigDecimal col_hea_id;
    public String col_num; 
    public BigDecimal col_cat_id;
    public BigDecimal col_type_id;
    public BigDecimal col_sub_id;
    public BigDecimal col_gro_id;
    public BigDecimal gctc_id;
    public BigDecimal endorsement_type_id;
    public BigDecimal object_type_id;
    public BigDecimal property_type_id;

    
    public String toString()
    {
        String ret="*";
        ret += "col_hea_id=" + col_hea_id + ", ";
        ret += "col_num=" + col_num + ", ";
        ret += "col_cat_id=" + col_cat_id + ", ";
        ret += "col_type_id=" + col_type_id + ", ";
        ret += "col_sub_id=" + col_sub_id + ", ";        
        ret += "col_gro_id=" + col_gro_id + ", ";
        ret += "gctc_id=" + gctc_id + ", ";
        ret += "endorsement_type_id=" + endorsement_type_id + ", ";
        ret += "object_type_id=" + object_type_id + ", ";
        ret += "property_type_id=" + property_type_id;
        return ret;
    }
}