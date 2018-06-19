package hr.vestigo.modules.collateral.batch.bo60;

import java.math.BigDecimal;

/**
 * Parametrizacijski kriteriji za slanje obavijesti/opomena po policama osiguranja.
 * @author hrakis
 */
public class ParamWrnNotesData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo60/ParamWrnNotesData.java,v 1.1 2011/05/27 14:29:47 hrakis Exp $";
    
    /** Status slanja obavijesti/opomene. */
    public String wrn_status;
    
    /** Opis statusa slanja obavijesti/opomene. */
    public String wrn_status_desc;
    
    /** Tip obvijesti. */
    public BigDecimal cus_let_typ_id;
    
    /** Naziv tipa obavijesti. */
    public String cus_let_typ_desc;
    
    /** Minimalni broj dana koliko polica osiguranja mora biti neuredna da bi se poslala obavijest. */
    public int min_day_bfr_snd;
    
    /** Minimalni broj dana koliko mora proæi od slanja prethodne obavijesti. */
    public int min_day_aft_pre_snd;
    
    /** Status koji je prethodno morala imati polica. */
    public String pre_wrn_status;
    
    /** Tip obavijesti koja mora biti prethodno poslana. */
    public BigDecimal pre_cus_let_typ_id;

    /** Tip naknade. */
    public BigDecimal fee_cod_id;

    
    public String toString()
    {
        return "WRN_STATUS=" + wrn_status + 
             ", WRN_STATUS_DESC=" + wrn_status_desc +
             ", CUS_LET_TYP_ID=" + cus_let_typ_id + 
             ", CUS_LET_TYP_DESC=" + cus_let_typ_desc +
             ", MIN_DAY_BFR_SND=" + min_day_bfr_snd + 
             ", MIN_DAY_AFT_PRE_SND=" + min_day_aft_pre_snd + 
             ", PRE_WRN_STATUS=" + pre_wrn_status +
             ", PRE_CUS_LET_TYP_ID=" + pre_cus_let_typ_id;
    }
}