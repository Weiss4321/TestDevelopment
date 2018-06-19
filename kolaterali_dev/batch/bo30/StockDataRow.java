package hr.vestigo.modules.collateral.batch.bo30;

import hr.vestigo.modules.rba.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;

public class StockDataRow {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo30/StockDataRow.java,v 1.9 2015/04/20 11:43:03 hraziv Exp $";
	
	/**
	 * Jedinstvena identifikacijska oznaka VP-a
	 */
	public String isin=null;
	/**
	 * Oznaka VP-a na tržištu
	 */
	public String ticker=null;
	/**
	 * Vrsta vrijednosnog papira
	 */
	public String vp_typ=null;
	/**
	 * Izdavatelj VP-a, interni MB izdavatelja VP-a u ZBK
	 */
	public String issuer=null;
	/**
	 * Valuta u kojoj je izražena nominalna vrijednost vrijednosnog papira
	 */
	public String nom_cur=null;
	/**
	 * Jedinièna nominalna vrijednost VP-a
	 */
	public BigDecimal nom_amount=null;
	/**
	 * Referentno tržište na kojem se trguje tim VP-om
	 */
	public String stock_exchange=null;
	/**
	 * Datum izdavanja VP-a
	 */
	public Date issue_date=null;
	/**
	 * Datum dospijeæa VP-a
	 */
	public Date maturity_date=null;
	/**
	 * Kamatna stopa, nema za sve VP
	 */
	public BigDecimal int_rate=null;
	/**
	 * Trenutna cijena na tržištu
	 */
	public BigDecimal price=null;
	/**
	 * Datum od kada vrijedi cijena
	 */
	public Date price_date=null;
    
    public String main_index_indicator=null;
    
    public String long_rating =null;
    
    public String short_rating=null;
    
    public String daily_price=null;
    
    public String seniority_indic=null;
	
	private int tokenIndex=0;
	
	/*
     * CQ 16964
     */
    //SSB - SP Stable (Outlook)
    public String sp_rating=null;
    //MFS - moody_financial_strength_rating
    public String mfs_rating=null;
    //MLT - moody_long_term_rating
    public String mlt_rating=null;
    //MST - moody_short_term_rating
    public String mst_rating=null;
	
	public StockDataRow(){
		
	}
	
	public StockDataRow(String row){
		parse(row);
	}
	
	/**
	 * parsira ulazni red uz trim podataka
	 * 
	 * @param row ulazni redak iz datoteke
	 */
	public void parse(String row){
		tokenIndex=0;
		isin=cut(row,20);
		ticker=cut(row,20);
		vp_typ=cut(row,10);
		issuer=cut(row,10);
		nom_cur=cut(row,3);
		nom_amount=new BigDecimal(cut(row,17));
		stock_exchange=cut(row,10);
		issue_date=DateUtils.convertOmegaDate(cut(row,8));
		String maturity_date_s=cut(row,8);
		if(!maturity_date_s.equals("")){
			maturity_date=DateUtils.convertOmegaDate(maturity_date_s);
		}
		String init_rate_s=(cut(row,17));
		if(!init_rate_s.equals("")){
			int_rate=new BigDecimal(init_rate_s);
		}
		
		String price_s=(cut(row,17));
		if(!price_s.equals("")){
			price=new BigDecimal(price_s);
		}
		price_date=DateUtils.convertOmegaDate(cut(row,8));
        main_index_indicator=(cut(row,1));
        //sp_rating se mapira u    SPIL                            S&P Long Term Rating - Issue Level
        sp_rating=(cut(row,10));
        short_rating=(cut(row,10));
        long_rating=(cut(row,10));
        //Moody's VP rating - mapira se u rat_typ_id = 91982780351       MNW                           MOODY'S NO WATCH
        mfs_rating=(cut(row,10));
        mlt_rating=(cut(row,10));
        mst_rating=(cut(row,10));
        
        String daily_price_tmp=(cut(row,2));
        if(daily_price_tmp.toUpperCase().equals("DA")){
            daily_price="D";
        }
        else {
            daily_price="N";
        }
        
        String seniority_indic_tmp=(cut(row,15));
        if(seniority_indic_tmp.toUpperCase().equals("SENIOR")){
            seniority_indic="D";
        }
        else
        {
            seniority_indic="N";
        }
	}
	
	public String toString(){
		String result="";
		result+="\n isin:"+isin;
		result+="\n ticker:"+ticker;
		result+="\n vp_typ:"+vp_typ;
		result+="\n issuer:"+issuer;
		result+="\n nom_cur:"+nom_cur;
		result+="\n nom_amount:"+nom_amount;
		result+="\n stock_exchange:"+stock_exchange;
		result+="\n issue_date:"+issue_date;
		result+="\n maturity_date:"+maturity_date;
		result+="\n int_rate:"+int_rate;
		result+="\n price:"+price;
		result+="\n price_date:"+price_date;
        result+="\n main_index_indicator:"+main_index_indicator;
        result+="\n long_rating:"+long_rating;
        result+="\n short_rating:"+short_rating;
        result+="\n sp_stable_rating:"+sp_rating;
        result+="\n mody_short_rating:"+mst_rating;
        result+="\n mody_long_rating:"+mlt_rating;
        result+="\n moody_financial_strength_rating:"+mfs_rating;
        result+="\n daily_price:"+daily_price;
        result+="\n seniority_indic:"+short_rating;
		return result;
	}
	
	private String cut(String input, int tokenLength){
		String result= input.substring(tokenIndex, tokenIndex+tokenLength).trim();
		tokenIndex+=tokenLength;
		return result;
	}

}
