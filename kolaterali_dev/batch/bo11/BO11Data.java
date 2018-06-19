/*
 * Created on 2007.05.22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo11;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO11Data {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo11/BO11Data.java,v 1.3 2008/01/03 08:52:08 hraaks Exp $";
	
	//podatci dobiveni iz aplikacije
	private String clientGroup;
	private String registerNo;
	private BigDecimal collTypeCode;
	private BigDecimal collCategoryCode;
	private Date dateOfReview;
	
	//podatci dobiveni pomocu JDBC-a i sqlj-a
	private BigDecimal col_hea_id;
	private BigDecimal col_cat_id;
	private BigDecimal col_type_id;
	private BigDecimal valuta_depozita;
	private BigDecimal valuta_plasmana;
    // amir promijenio
	private BigDecimal amount; //to je pokirvenost
	private Object subtypeData = null;
	
	/**
	 * @return Returns the clientType.
	 */
	public String getClientGroup() {
		return clientGroup;
	}
	/**
	 * @param clientType The clientType to set.
	 */
	public void setClientGroup(String clientGroup) {
		this.clientGroup = clientGroup;
	}
	/**
	 * @return Returns the coll_category.
	 */
	public BigDecimal getCollCategoryCode() {
		return collCategoryCode;
	}
	/**
	 * @param coll_category The coll_category to set.
	 */
	public void setCollCategoryCode(BigDecimal coll_category) {
		this.collCategoryCode = coll_category;
	}
	
	public void setCollCategoryCode(String collCat) {
		if (collCat!=null && !collCat.trim().equals("") && !collCat.trim().equals("null") ){
			this.collCategoryCode  = new BigDecimal(collCat);
		}
	}
	/**
	 * @return Returns the coll_type.
	 */
	public BigDecimal getCollTypeCode() {
		return collTypeCode;
	}
	/**
	 * @param coll_type The coll_type to set.
	 */
	public void setCollTypeCode(BigDecimal coll_type) {
		this.collTypeCode = coll_type;
	}
	
	public void setCollTypeCode(String collType) {
		if (collType!=null && !collType.trim().equals("") && !collType.trim().equals("null")){
			this.collTypeCode  = new BigDecimal(collType);
		}
	}
	
	/**
	 * @return Returns the register_no.
	 */
	public String getRegisterNo() {
		return registerNo;
	}
	/**
	 * @param register_no The register_no to set.
	 */
	public void setRegisterNo(String register_no) {
		this.registerNo = register_no;
	}
	
	
	
	/**
	 * @return Returns the dateOfReview.
	 */
	public Date getDateOfReview() {
		return dateOfReview;
	}
	/**
	 * @param dateOfReview The dateOfReview to set.
	 */
	public void setDateOfReview(String date) {
		if (date!=null && !date.trim().equals("")){
			this.dateOfReview  = convertStringToDate(date);
		}else{
			this.dateOfReview = Date.valueOf("1970-01-01");
		}
	}
	

	
	
	/**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    /**
	 * @return Returns the col_cat_id.
	 */
	public BigDecimal getCol_cat_id() {
		return col_cat_id;
	}
	/**
	 * @param col_cat_id The col_cat_id to set.
	 */
	public void setCol_cat_id(BigDecimal col_cat_id) {
		this.col_cat_id = col_cat_id;
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
	/**
	 * @return Returns the col_type_id.
	 */
	public BigDecimal getCol_type_id() {
		return col_type_id;
	}
	/**
	 * @param col_type_id The col_type_id to set.
	 */
	public void setCol_type_id(BigDecimal col_type_id) {
		this.col_type_id = col_type_id;
	}
	/**
	 * @return Returns the subtypeData.
	 */
	public Object getSubtypeData() {
		return subtypeData;
	}
	/**
	 * @param subtypeData The subtypeData to set.
	 */
	public void setSubtypeData(Object subtypeData) {
		this.subtypeData = subtypeData;
	}
	/**
	 * @return Returns the valuta_depozita.
	 */
	public BigDecimal getValuta_depozita() {
		return valuta_depozita;
	}
	/**
	 * @param valuta_depozita The valuta_depozita to set.
	 */
	public void setValuta_depozita(BigDecimal valuta_depozita) {
		this.valuta_depozita = valuta_depozita;
	}
	/**
	 * @return Returns the valuta_plasmana.
	 */
	public BigDecimal getValuta_plasmana() {
		return valuta_plasmana;
	}
	/**
	 * @param valuta_plasmana The valuta_plasmana to set.
	 */
	public void setValuta_plasmana(BigDecimal valuta_plasmana) {
		this.valuta_plasmana = valuta_plasmana;
	}
	
	
	public void resetFetched() {
		col_hea_id = null;
		col_cat_id = null;
		col_type_id = null;
		valuta_depozita = null;
		valuta_plasmana = null;
		amount= null; 
		subtypeData = null;
	}
	
	private Date convertStringToDate(String dataString) {
		
		String pomString = null;
		String dan = null;
		String mjesec = null;
		String godina = null;
		
		dan = dataString.substring(0,2);
		mjesec = dataString.substring(3,5);
		godina = dataString.substring(6,10);
		
		pomString = godina + '-' + mjesec + '-' + dan;
		
		return Date.valueOf(pomString);
		
	}
}
