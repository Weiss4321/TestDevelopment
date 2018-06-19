//created 2010.02.22
package hr.vestigo.modules.collateral.batch.bo52;

import java.math.BigDecimal;

/**
 * !!! NE KORISTI SE VIŠE !!!
 * @author hraamh
 */
public class AccCoverData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo52/AccCoverData.java,v 1.7 2010/09/22 08:31:05 hrakis Exp $";
    
    /*
     * Atributi iz tablic FRAME_ACCOUNT_COV
     */
    public BigDecimal fra_acc_cov_id;
    public BigDecimal ref_col_pro_id;

    /** partija okvira iz koje su dani pojedinacni plasmani */
    public BigDecimal frame_cus_acc_id;
    
    /** izloženost okvira */
    public BigDecimal frame_exposure_kn;
    
    /** partija plasmana iz okvira */
    public BigDecimal cus_acc_id;
    
    /** valuta okvira */
    public BigDecimal frame_cur_id;

    /** iznos pokrivenosti okvira u valuti okvira (iznos kojim se alikvotno pokrivaju partije iz okvira), 
     *  suma pojedinacnih pokrivenosti okvira kolateralima */
    public BigDecimal frame_cov_amount;

    /** iznos pokrivenosti okvira u kunama (iznos u kn kojim se alikvotno pokrivaju partije iz okvira) */
    public BigDecimal frm_cov_amount_kn;

    /** valuta plasmana iz okvira */
    public BigDecimal acc_cur_id;

    /** ukupna izlozenost po plasmanu u valuti plasmana */
    public BigDecimal acc_amount;

    /** ukupna izlozenost po plasmanu u kn */
    public BigDecimal acc_amount_hrk;

    /** pokrivenost plasmana u valuti plasmana */
    public BigDecimal acc_cov_amount;

    /** pokrivenost plasmana u kn */
    public BigDecimal acc_cov_amount_kn;

    /** pokrivenost plasmanu u valuti okvira */
    public BigDecimal acc_cov_amount_fr;

    /** % pokrivenosti plasmana (pokrivenost/izlozenost) * 100 */
    public BigDecimal exp_percent;

    /** veza na kolateral koji pokriva okvir */
    public BigDecimal col_hea_id;

    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("fra_acc_cov_id:" + fra_acc_cov_id).append("\n");
        sb.append("ref_col_pro_id:" + ref_col_pro_id).append("\n");
        sb.append("frame_cus_acc_id:" + frame_cus_acc_id).append("\n");
        sb.append("cus_acc_id:" + cus_acc_id).append("\n");
        sb.append("frame_cur_id:" + frame_cur_id).append("\n");
        sb.append("frame_cov_amount:" + frame_cov_amount).append("\n");
        sb.append("frm_cov_amount_kn:" + frm_cov_amount_kn).append("\n");
        sb.append("acc_cur_id:" + acc_cur_id).append("\n");
        sb.append("acc_amount:" + acc_amount).append("\n");
        sb.append("acc_amount_hrk:" + acc_amount_hrk).append("\n");
        sb.append("acc_cov_amount:" + acc_cov_amount).append("\n");
        sb.append("acc_cov_amount_kn:" + acc_cov_amount_kn).append("\n");
        sb.append("acc_cov_amount_fr:" + acc_cov_amount_fr).append("\n");
        sb.append("exp_percent:" + exp_percent.movePointRight(2) + "%").append("\n");
        sb.append("col_hea_id:" + col_hea_id).append("\n");
        return sb.toString();
    }
}

