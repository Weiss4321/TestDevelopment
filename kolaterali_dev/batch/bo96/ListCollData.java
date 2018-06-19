//created 2015.02.09
package hr.vestigo.modules.collateral.batch.bo96;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
/**
 *
 * @author Hraziv
 */
public class ListCollData {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo96/ListCollData.java,v 1.5 2018/01/23 12:14:01 hraskd Exp $";
    
    /**
     * cus_id vlasnika plasmana
     */
    public BigDecimal cusId;
    public BigDecimal colHeaId;
    /**
     * register no vlasnika plasmana
     */
    public String registerNo;
    /**
     * naziv vlasnika plasmana
     */
    public String customerName;
    /**
     * cocunat vlasnika plasmana
     */
    public String cocunat;
    /**
     * sifra kolaterala - col_num
     */
    public String collateralObjectId;
    /**
     * Collateral code(GCTC)
     */
    public String collataeralCode;
    /**
     * Endorsement type(GCTC)
     */
    public String endorsementType;
    /**
     * Property type(GCTC)
     */
    public String propertyType;
    /**
     * Objekt type(GCTC)
     */
    public String objectType;
    /**
     * real_est_nomi_valu
     */
    public BigDecimal iznosNCV;
    /**
     * real_est_nomi_date
     */
    public Date datumNCV;
    /**
     * new_build_val
     */
    public BigDecimal iznosNGV;
    /**
     * real_est_estn_date 
     */
    public Date datumNGV;    
    public BigDecimal iznosWCV;
    public Date datumWCV;
    public String takeoverFromBank;
    public BigDecimal iznosWCOV;
    public Date datumWCOV;
    public BigDecimal recoveryAmount;
    public String recoveryCurrency;
    public String kindOfRecovery;
    public Date recoveryDate;
    public String recovery_comment;
    public BigDecimal costForCollateralRealization;
    public BigDecimal realizationAmount;
    public String realizationCurrency;
    /**
     * 618223 -> nekretnine
     */
    public BigDecimal collCatId;
    public Date realizationDate;
    
    /** 
     * Konstruktor klase.
     */
    public ListCollData()
    {
        
    }
}



