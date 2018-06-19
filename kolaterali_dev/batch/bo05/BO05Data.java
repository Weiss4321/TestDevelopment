/*
 * Created on 2007.01.22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo05;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO05Data {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo05/BO05Data.java,v 1.8 2008/04/02 10:43:09 hramkr Exp $";
	
	/**
	 * Organizacijska jedinica partije plasmana zadana preko ekrana
	*/
	private BigDecimal org_uni_id;
	/**
	 * Osiguravatelj zadan preko ekrana
	 */
	private BigDecimal ic_id;
	/**
	 * Period u kojem dospijevaju ili su istekle police osiguranja zadano preko ekrana
	 */ 
	private Date date_from;
	private  Date date_until;
	
	/**
	 * Datum do kojeg je plaæena premija
	 * */ 
	public  Date premija_do;
	/**
	 * Status police zadan preko ekrana
	 */
	private String state_code;
	/**
	 * Status police dohvacen u sqlj-u
	 */
	public String status;
	/**
	 * Vrsta police osiguranja zadano preko ekrana
	 */
	private String type_code;
	/**
	 * Client type moze biti "P" ili "F" (pravne, fizicke) zadano preko ekrana
	 */
	public String clientType;
	/**
	 * Broj zahtjeva za plasman
	 */
	public String request_no;
	/**
	 * Omega ID (register_no) korisnika plasmana
	 */
	public String register_no;
	/**
	 * Broj partije plasmana
	 */
	public String acc_no;
	/**
	 * Organizacijska jedinica partije plasmana dohvacena preko LOAN_BENEFICIARY tablice
	 */
	public String org_uni_name;
	public String org_uni_code;
	/**
	 * Id kolaterala
	 */ 
	public BigDecimal col_hea_id; 
	/**
	 * Id tablice COLL_INSPOLICY
	 */
	public BigDecimal col_ins_id; 
	/**
	 * Send status odreduje da li se salje obavijest ili opomena
	 */
	public String sendStatus;
	/**
	 * Id INSU_POLICY_TYPE tablice
	 */
	public BigDecimal ip_type_id;
	/**
	 * Osiguravatelj dohvacen preko selecta, mora biti komitent banke (id tablice INSU_COMPANY)
	 */
	public BigDecimal osig;
	/**
	 * Sifra police osiguranja
	 */
	public String ip_code;
	/**
	 * Datum osigurane svote, tablica INSURANCE_POLICY
	 */
	public Date polica_do;
	/**
	 * COLL_HEAD.col_num
	 */
	public String col_num;
	/**
	 * CURRENCY.code_char
	 */
	public String code_char;
	/**
	 * COLL_HEAD.acum_buy_value - akumulirana ili otkupna vrijednost police osiguranja
	 */
	public BigDecimal value;
	/**
	 * Customer name
	 */
	public String customer_name;
	/**
	 * CUSTOMER.address, tj. opca dopisna adresa ili ako ne postoji CUST_COMMUNICATION.com_adress
	 * tj. osnovna adresa
	 */
	public String customer_address;
	/**
	 * CUSTOMER.xpostoffice
	 */
	public String customer_postoffice;
	/**
	 * CUSTOMER.xcity
	 */
	public String customer_city;
	/**
	 * Kategorija kolaterala
	 */
	public BigDecimal col_cat_id;
	
	/**
	 * Ako je kolateral polica osiguranja vrijednost je true.
	 */
	public boolean collateral_is_ins_policy;
	public BigDecimal cus_id;

	public void reset_customer(){
		
		this.register_no = "";

		this.customer_name = "";
		this.customer_address = "";
		this.customer_postoffice = "";
		this.customer_city = "";
		
	}   
	public void reset_object(){
		this.col_ins_id = null;
		this.ip_type_id = null;
		this.osig = null;
		this.ip_code = "";
		this.premija_do = null;
		this.polica_do = null;
		this.value = null;
		this.status = "";
		this.col_hea_id = null;
		this.col_num = "";
		this.org_uni_code = "";
		this.org_uni_name = "";
		this.code_char = "";
		this.cus_id = null;
		this.request_no = "";
		this.acc_no = "";
	}
	  
	/**
	 * @return Returns the date_from.
	 */
	public Date getDate_from() {
		return date_from;
	}
	/**
	 * @param date_from The date_from to set.
	 */
	public void setDate_from(String date_from) {
		if (date_from!=null && !date_from.trim().equals("")){
			this.date_from  = convertStringToDate(date_from);
		}else{
			this.date_from = Date.valueOf("1970-01-01");
		}
	}
	/**
	 * @return Returns the date_until.
	 */
	public Date getDate_until() {
		return date_until;
	}
	/**
	 * @param date_until The date_until to set.
	 */
	public void setDate_until(String date_until) {
		if (date_until!=null && !date_until.trim().equals("")){
			this.date_until  = convertStringToDate(date_until);
		}else{
			this.date_until = Date.valueOf("9999-12-31");
		}
	}
	/**
	 * @return Returns the ic_id.
	 */
	public BigDecimal getIc_id() {
		return ic_id;
	}
	/**
	 * @param ic_id The ic_id to set.
	 */
	public void setIc_id(String ic_id) {
		if (ic_id!=null && !ic_id.trim().equals("")){
			this.ic_id  = new BigDecimal(ic_id);
		}
	}
	/**
	 * @return Returns the org_uni_id.
	 */
	public BigDecimal getOrg_uni_id() {
		return org_uni_id;
	}
	/**
	 * @param org_uni_id The org_uni_id to set.
	 */
	public void setOrg_uni_id(String org_uni_id) {
		if (org_uni_id!=null && !org_uni_id.trim().equals("")){
			this.org_uni_id  = new BigDecimal(org_uni_id);
		}
	} 
	/**
	 * @return Returns the state_code. 
	 */
	public String getState_code() {
		return state_code;
	}
	/**
	 * @param state_code The state_code to set.
	 */
	public void setState_code(String state_code) {
		if (state_code!=null && !state_code.trim().equals("")){
			this.state_code  = state_code;
		}
	}
	/**
	 * @return Returns the type_code.
	 */
	public String getType_code() {
		return type_code;
	}
	/**
	 * @param type_code The type_code to set.
	 */
	public void setType_code(String type_code) {
		if (type_code!=null && !type_code.trim().equals("")){
			this.type_code  = type_code;
		}
		
	}

	
	private Date convertStringToDate(String dataString){
		
		String pomString=null;
		String dan=null;
		String mjesec=null;
		String godina=null;
		
		dan=dataString.substring(0,2);
		mjesec=dataString.substring(3,5);
		godina=dataString.substring(6,10);
		
		pomString=godina + "-" + mjesec + "-" + dan;
		return Date.valueOf(pomString);
		
	}
	
	/**
	 * @return Returns the clientType.
	 */
	public String getClientType() {
		return clientType;
	}
	/**
	 * @param clientType The clientType to set.
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	
	/**
	 * @return Returns the col_hea_id.
	 */
	public BigDecimal getCol_hea_id() {
		return col_hea_id;
	}
	/**
	 * @param col_hea_id The col_hea_id to set.
	 */
	public void setCol_hea_id(BigDecimal col_hea_id) {
		this.col_hea_id = col_hea_id;
	}
}
