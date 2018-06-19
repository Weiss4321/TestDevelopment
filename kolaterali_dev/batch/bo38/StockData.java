package hr.vestigo.modules.collateral.batch.bo38;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Klasa koja sadrži podatke o dionici/udjelu.
 * @author hrakis
 */
public class StockData
{
	// podaci o dionici/udjelu iz ulazne datoteke
	public Integer rbr;
	public String coll_type_code;
	public String isin;
	public String stop_sell_ind;
	public BigDecimal stop_sell_period;
	public String currency_clause;
	public String rba_eligibility;
	public BigDecimal num_of_sec;
	public String owner_reg_no;
	public BigDecimal hf_amount;
	public String hf_currency;
	public String hf_type;
	public String hf_inst;
	public Date hf_from;
	public Date hf_until;
	public Date hf_date_reciv;
	public String loan_reg_no;
	public String cus_acc_no;
	public String veh_con_num;
	public BigDecimal cur_id_ref;
	public BigDecimal mid_rate_ref;
	
	// podaci o tipu kolaterala dobiveni iz atributa coll_type_code
	public boolean is_stock;
	public BigDecimal col_cat_id;
	public BigDecimal coll_type_id;
	
	// dodatni podaci o dionici/udjelu iz tablice COLL_IN2 i COLL_IN2_PRICE
	public BigDecimal col_in2_id; 
	public BigDecimal nom_cur_id;
	public BigDecimal one_nom_amo;
	public BigDecimal sto_mar_id;
	public BigDecimal price;
	public Date price_date;
	public BigDecimal mid_rate;

	// dodatni podaci o vlasniku kolaterala iz tablice CUSTOMER
	public BigDecimal owner_cus_id;
	public String owner_code;
	public String owner_name;

	// dodatni podaci o vlasniku plasmana iz tablice CUSTOMER
	public BigDecimal loan_cus_id;
	public String loan_name;

	// dodatni podaci o plasmanu iz tablice CUSACC_EXPOSURE
	public BigDecimal cus_acc_id;
	public String request_no;
	
	// primarni kljuèevi koji æe biti generirani prilikom punjenja tablica... 
	public BigDecimal col_hea_id = null;		// COLL_HEAD
	public BigDecimal col_vrp_id = null;		// COLL_VRP
	public BigDecimal coll_own_id = null;		// COLL_OWNER
	public BigDecimal coll_hf_prior_id = null;	// COLL_HF_PRIOR
	public BigDecimal loan_ben_id = null;		// LOAN_BENEFICIARY
	public BigDecimal col_lis_q_id = null;		// COLL_LIST_Q
	
	// generirani broj kolaterala
	public String col_num;
	
	// opis greške koja je uzrokovala da se dionice ne preuzme uspješno
	public String error;

	public String toString()
	{
		return "RBR="+rbr+" ISIN="+isin;
	}
}