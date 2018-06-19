package hr.vestigo.modules.collateral.batch.bo30;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class CollIn2Data {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo30/CollIn2Data.java,v 1.3 2014/07/09 12:07:42 hraziv Exp $";
	
	public BigDecimal col_in2_id=null;
	public BigDecimal col_cat_id=null;
	public BigDecimal col_typ_id=null;
	public BigDecimal iss_cus_id=null;
	public String ticker=null;
	public String isin=null;
	public BigDecimal nom_cur_id=null;
	public BigDecimal nom_amount=null;
	public BigDecimal sto_mar_id=null;
	public Date issue_date=null;
	public Date maturity_date=null;
	public BigDecimal int_rate=null;
	public String stock_ind=null;
	public BigDecimal use_id=null;
	public Timestamp user_lock=null;
	public String currency_clause=null;
	public String daily_price=null;    
    public String seniority_indic=null;
    
    public String toString(){
        StringBuffer result=new StringBuffer("");
        result.append("\n").append("col_in2_id:").append(col_in2_id);
        result.append("\n").append("col_cat_id:").append(col_cat_id);
        result.append("\n").append("col_typ_id:").append(col_typ_id);
        result.append("\n").append("iss_cus_id:").append(iss_cus_id);
        result.append("\n").append("ticker:").append(ticker);
        result.append("\n").append("isin:").append(isin);
        result.append("\n").append("nom_cur_id:").append(nom_cur_id);
        result.append("\n").append("nom_amount:").append(nom_amount);
        result.append("\n").append("sto_mar_id:").append(sto_mar_id);
        result.append("\n").append("issue_date:").append(issue_date);
        result.append("\n").append("maturity_date:").append(maturity_date);
        result.append("\n").append("int_rate:").append(int_rate);
        result.append("\n").append("stock_ind:").append(stock_ind);
        result.append("\n").append("use_id:").append(use_id);
        result.append("\n").append("user_lock:").append(user_lock);
        result.append("\n").append("currency_clause:").append(currency_clause);
        result.append("\n").append("daily_price:").append(daily_price);
        result.append("\n").append("seniority_indic:").append(seniority_indic);
        return result.toString();
    }



}
