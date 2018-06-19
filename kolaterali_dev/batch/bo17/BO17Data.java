/*
 * Created on 2007.11.14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo17;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO17Data {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo17/BO17Data.java,v 1.1 2007/11/15 09:36:16 hratar Exp $";
	
	/**
	 * Organizacijska jedinica partije plasmana zadana preko ekrana
	*/
	private BigDecimal org_uni_id;
	
	/**
	 * Period u kojem se treba donijeti/vratiti knjižica vozila zadano preko ekrana
	 */ 
	private Date deliverDateFrom;
	private Date deliverDateUntil;
	
	/**
	 * Client type moze biti "P" ili "F" (pravne, fizicke) zadano preko ekrana
	 */
	private String clientType;

	/**
	 * Organizacijska jedinica partije plasmana dohvacena preko LOAN_BENEFICIARY tablice
	 */
	private String org_uni_name;
	private String org_uni_code;
	
	/**
	 * COLL_HEAD.col_num
	 */
	private String col_num;
	/**
	 * Customer name
	 */
	public String customer_name;
	/**
	 * CUSTOMER.address, tj. opca dopisna adresa ili ako ne postoji CUST_ADDRESS.address
	 * tj. osnovna adresa
	 */
	public String customer_address;
	/**
	 * CUSTOMER.xpostoffice ili CUST_ADDRESS.postoffice
	 */
	public String customer_postoffice;
	/**
	 * CUSTOMER.xcity ili CUST_ADDRESS.city_name
	 */
	public String customer_city;
	/**
	 * Broj partije plasmana (acc_no)
	 */
	private String party_no;
	
	public void reset_object(){
		this.org_uni_id = null;
		this.col_num = "";
		this.org_uni_code = "";
		this.org_uni_name = "";
		this.deliverDateFrom = null;
		this.deliverDateUntil = null;
		this.clientType = "";
		this.customer_name = "";
		this.customer_address = "";
		this.customer_postoffice = "";
		this.customer_city = "";
	}
	
	/**
	 * @return Returns the deliverDateFrom.
	 */
	public Date getDeliverDateFrom() {
		return deliverDateFrom;
	}
	/**
	 * @param date_from The deliverDateFrom to set.
	 */
	public void setDeliverDateFrom(String date_from) {
		if (date_from!=null && !date_from.trim().equals("")){
			this.deliverDateFrom  = convertStringToDate(date_from);
		}else{
			this.deliverDateFrom = Date.valueOf("1970-01-01");
		}
	}
	/**
	 * @return Returns the deliverDateUntil.
	 */
	public Date getDeliverDateUntil() {
		return deliverDateUntil;
	}
	/**
	 * @param date_until The deliverDateUntil to set.
	 */
	public void setDeliverDateUntil(String date_until) {
		if (date_until!=null && !date_until.trim().equals("")){
			this.deliverDateUntil = convertStringToDate(date_until);
		}else{
			this.deliverDateUntil = Date.valueOf("9999-12-31");
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
	 * @return Returns the col_num.
	 */
	public String getCol_num() {
		return col_num;
	}
	/**
	 * @param col_num The col_num to set.
	 */
	public void setCol_num(String col_num) {
		this.col_num = col_num;
	}
	
	/**
	 * @return Returns org_uni_name
	 */
	public String getOrg_uni_name() {
		return org_uni_name;
	}
	/**
	 * @param org_uni_name The org_uni_name to set.
	 */
	public void setOrg_uni_name(String org_uni_name) {
		this.org_uni_name = org_uni_name;
	}
	
	/**
	 * @return Returns org_uni_code
	 */
	public String getOrg_uni_code() {
		return org_uni_code;
	}
	/**
	 * @param org_uni_code The org_uni_code to set.
	 */
	public void setOrg_uni_code(String org_uni_code) {
		this.org_uni_code = org_uni_code;
	}
	
	/**
	 * @return Returns customer_name
	 */
	public String getCustomer_name() {
		return customer_name;
	}
	/**
	 * @param customer_name The customer_name to set.
	 */
	public void setOCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	
	/**
	 * @return Returns customer_address
	 */
	public String getCustomer_address() {
		return customer_address;
	}
	/**
	 * @param customer_address The customer_address to set.
	 */
	public void setCustomer_address(String customer_address) {
		this.customer_address = customer_address;
	}
	
	/**
	 * @return Returns customer_postoffice
	 */
	public String getCustomer_postoffice() {
		return customer_postoffice;
	}
	/**
	 * @param customer_postoffice The customer_postoffice to set.
	 */
	public void setCustomer_postoffice(String customer_postoffice) {
		this.customer_postoffice = customer_postoffice;
	}
	
	/**
	 * @return Returns customer_city
	 */
	public String getCustomer_city() {
		return customer_city;
	}
	/**
	 * @param customer_city The customer_city to set.
	 */
	public void setCustomer_city(String customer_city) {
		this.customer_city = customer_city;
	}
	
	/**
	 * @return Returns party_no
	 */
	public String getParty_no() {
		return party_no;
	}
	/**
	 * @param customer_city The customer_city to set.
	 */
	public void setParty_no(String party_no) {
		this.party_no = party_no;
	}
}
