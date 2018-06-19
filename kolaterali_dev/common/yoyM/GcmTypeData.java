package hr.vestigo.modules.collateral.common.yoyM;

import java.math.BigDecimal;

/**
 * Stavka na koju se mogu mapirati kolaterali.
 */
public class GcmTypeData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoyM/GcmTypeData.java,v 1.3 2015/04/10 06:42:40 hrakis Exp $";
    
    /** ID stavke. */
    public BigDecimal col_gcm_typ_id;
    
    /** Oznaka vrste mapiranja. */
    public String map_code;
    
    /** Redni broj stavke unutar vrste mapiranja. */
    public int ord_no;
    
    /** Oznaka stavke. */
    public String code;
    
    /** Naziv stavke. */
    public String name;
    
    /** Dodatni naziv stavke. */
    public String name_add;
    
    /** Vrijednost parametra koji je vezan na stavku. */
    public String param_value;
    
    /** Indikator o kojem se parametru radi. */
    public String param_indic;
    
    
    /**
     * Mapiranje RBA kategorije/vrste/podvrste kolaterala.
     */
    public class GcmTypeMappingData
    {
        /** ID RBA kategorije kolaterala. */
        public BigDecimal col_cat_id;
        
        /** ID RBA vrste kolaterala. */
        public BigDecimal col_typ_id;
        
        /** ID RBA podvrste kolaterala. */
        public BigDecimal col_sub_id;

        /** ID RBA grupe kolaterala. */
        public BigDecimal col_gro_id;

        /** ID stavke na koju je mapirana RBA kategorija/vrsta/podvrsta kolaterala. */
        public GcmTypeData gcmType;
    }
}
