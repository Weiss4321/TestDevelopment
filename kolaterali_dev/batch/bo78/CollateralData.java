package hr.vestigo.modules.collateral.batch.bo78;

import java.math.BigDecimal;


public class CollateralData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo78/CollateralData.java,v 1.1 2013/12/20 08:06:13 hrakis Exp $";
    
    /** ID kolaterala. */
    public BigDecimal col_hea_id;
    
    /** Šifra kolaterala. */
    public String col_num;
    
    /** Nova graðevinska vrijednost izražena u EUR. */
    public BigDecimal ngv;
    
    /** Suma osiguranih svota po svim aktivnim policama kolaterala izražena u EUR. */
    public BigDecimal insured_amount_sum;
    
    /** Omjer NGV / suma osiguranih svota. */
    public BigDecimal ratio;
    
    /** Suma izloženosti svih aktivnih plasmana povezanih na kolateral izražena u EUR. */
    public BigDecimal placement_exposure_sum;
    
    /** ID tipa komitenta vlasnika (jednog odabranog) plasmana. */
    public BigDecimal placement_owner_cus_typ_id;
    
    /** Interni MB vlasnika (jednog odabranog) plasmana. */
    public String placement_owner_register_no;
    
    /** Naziv vlasnika (jednog odabranog) plasmana. */
    public String placement_owner_name;
    
    /** Naziv referenta (jednog odabranog) plasmana. */
    public String placement_owner_rsm_name;
}