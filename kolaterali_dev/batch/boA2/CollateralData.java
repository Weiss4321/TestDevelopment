package hr.vestigo.modules.collateral.batch.boA2;

import java.math.BigDecimal;
import java.sql.Date;


public class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/boA2/CollateralData.java,v 1.1 2017/03/13 12:03:22 hrakis Exp $";
    
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** Šifra kolaterala. */
    public String col_num;
    
    /** Nova graðevinska vrijednost nekretnine. */
    public BigDecimal new_build_val;
    
    /** Vlasnik kolaterala. */
    public String coll_owner_name;
    
    
    /** ID grupne police osiguranja. */
    public BigDecimal group_ip_id;
    
    /** Šifra grupne police osiguranja. */
    public String group_ip_code;
    
    /** Grupna polica osiguranja plaæena od. */
    public Date group_ip_vali_from;
    
    /** Grupna polica osiguranja plaæena do. */
    public Date group_ip_vali_until;
    
    
    /** ID druge police osiguranja. */
    public BigDecimal other_ip_id; 
    
    /** Šifra druge police osiguranja. */
    public String other_ip_code; 
    
    /** Druga polica osiguranja plaæena od. */
    public Date other_ip_vali_from;
    
    /** Druga polica osiguranja plaæena do. */
    public Date other_ip_vali_until;
    
    /** Druga polica osiguranja vrijedi do. */
    public Date other_ip_date_sec_val;
    
    /** Osiguravatelj druge police osiguranja. */
    public String other_ic_name;
    
    
    /** ID korisnika plasmana. */
    public String placement_owner_register_no;
    
    /** Naziv korisnika plasmana. */
    public String placement_owner_name;
    
    /** Partija plasmana. */
    public String cus_acc_no;
    
    /** Status plasmana u modulu. */
    public String cus_acc_orig_st;
}