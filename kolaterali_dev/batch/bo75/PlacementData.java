package hr.vestigo.modules.collateral.batch.bo75;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.TreeMap;


public class PlacementData
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo75/PlacementData.java,v 1.5 2017/06/29 08:41:03 hrakis Exp $";
    
    public String cus_acc_no;
}


class CollateralData
{
    private static final BigDecimal zero = new BigDecimal("0.00");
    
    /** ID kolaterala **/
    public BigDecimal col_hea_id;
    
    /** Šifra kolaterala **/
    public String col_num;
    
    /** Tržišna vrijednost kolaterala **/
    public BigDecimal real_est_nomi_valu;
    
    /** ID valute tržišne vrijednosti **/
    public BigDecimal real_est_nm_cur_id;
    
    /** Slovèana oznaka valute tržišne vrijednosti **/
    public String real_est_nm_cur_code_char;
    
    /** Tržišna vrijednost kolaterala u domaæoj valuti **/
    public BigDecimal value;
    
    /** Vezane hipoteke **/
    public TreeMap<Integer, MortgageData> mortgages = new TreeMap<Integer, MortgageData>();
    
    /** Zadnja RBA hipoteka **/
    public MortgageData lastRbaMortgage;
    
    /** Suma iznosa svih RBA hipoteka **/
    public BigDecimal totalRbaMortgagesAmount = zero;
    
    /** Suma iznosa svih hipoteka drugih banaka višeg reda od zadnje RBA hipoteke **/
    public BigDecimal totalOtherMortgagesAmount = zero;
    
    
    public MortgageData getMortgageById(BigDecimal coll_hf_prior_id)
    {
        for (MortgageData mortgage : mortgages.values())
        {
            if (mortgage.coll_hf_prior_id.equals(coll_hf_prior_id)) return mortgage;
        }
        return null;
    }
}


class MortgageData
{
    /** ID hipoteke **/
    public BigDecimal coll_hf_prior_id;
    
    /** ID veze kolateral-plasman **/
    public BigDecimal loan_ben_id;
    
    /** Prioritet hipoteke **/
    public Integer hf_priority;
    
    /** ID komitenta u èiju je korist upisana hipoteka. **/
    public BigDecimal hf_own_cus_id;
    
    /** Iznos hipoteke **/
    public BigDecimal amount_ref;
    
    /** ID valute iznosa hipoteke **/
    public BigDecimal cur_id_ref;
    
    /** Slovèana oznaka valute iznosa hipoteke **/
    public String cur_code_char;
    
    /** Iznos hipoteke u domaæoj valuti **/
    public BigDecimal amount;

    private static final BigDecimal rba_cus_id = new BigDecimal("8218251");
    
    /** Metoda vraæa da li je hipoteka RBA hipoteka **/
    public boolean isRBA()
    {
        return rba_cus_id.equals(hf_own_cus_id);
    }
    
    /** Povijesni podaci o kolateralu. */
    public CollateralData oldCollateral;
    
    /** Raspoloživa vrijednost za pokrivanje plasmana (reporting date) - vrijednost koja se trenutaèno nalazi u bazi podataka. **/
    public BigDecimal rvrd_current;
    
    /** Raspoloživa vrijednost za pokrivanje plasmana (origination date) - vrijednost koja se trenutaèno nalazi u bazi podataka. **/
    public BigDecimal rvod_current;
    
    /** Raspoloživa vrijednost za pokrivanje plasmana (reporting date) - novoizraèunata vrijednost. **/
    public BigDecimal rvrd_new;
    
    /** Raspoloživa vrijednost za pokrivanje plasmana (origination date) - novoizraèunata vrijednost. **/
    public BigDecimal rvod_new;
    
    
    /** ID plasmana **/
    public BigDecimal cus_acc_id;
    
    /** Partija plasmana **/
    public String cus_acc_no;
    
    /** Datum korištenja plasmana **/
    public Date usage_date;
    
    /** Izloženost plasmana u domaæoj valuti. */
    public BigDecimal exp_balance_hrk;
}
