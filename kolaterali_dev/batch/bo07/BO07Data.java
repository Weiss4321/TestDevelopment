/*
 * Created on 2007.02.23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo07;
import java.sql.Date;
import java.math.BigDecimal;
/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO07Data {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo07/BO07Data.java,v 1.10 2008/01/11 13:39:20 hraamh Exp $";
	
	private BigDecimal cus_id;
	public BigDecimal subtype_coll;
	public String subtype_coll_str;
	private String clientCode;
	private String clientType;
	private String ponder;
	private BigDecimal exp_coll_amount;
	private String register_no;
	private String name;
	private String cus_acc_no;
	private BigDecimal exp_balance_hrk;
	private BigDecimal col_type_id;
	private Date dateOfReport;
	private BigDecimal col_hea_id;
	private BigDecimal col_cat_id;
	private int eligibility=-1;
	/**
	 * @return Returns the ponder.
	 */
	public String getPonder() {
		return ponder;
	}
	/**
	 * @param ponder The ponder to set.
	 */
	public void setPonder(String ponder) {
		this.ponder = ponder;
	}
	/**
	 * @param subtype_coll_str The subtype_coll_str to set.
	 */
	public void setSubtype_coll_str(String subtype_coll_str) {
		this.subtype_coll_str = subtype_coll_str;
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
	 * @return Returns the cus_acc_no.
	 */
	public String getCus_acc_no() {
		return cus_acc_no;
	}
	/**
	 * @param cus_acc_no The cus_acc_no to set.
	 */
	public void setCus_acc_no(String cus_acc_no) {
		this.cus_acc_no = cus_acc_no;
	}
	/**
	 * @return Returns the exp_coll_amount.
	 */
	public BigDecimal getExp_coll_amount() {
		return exp_coll_amount;
	}
	/**
	 * @param exp_coll_amount The exp_coll_amount to set.
	 */
	public void setExp_coll_amount(BigDecimal exp_coll_amount) {
		this.exp_coll_amount = exp_coll_amount;
	}
	/**
	 * @return Returns the exposure_balance.
	 */
	public BigDecimal getExp_balance_hrk() {
		return exp_balance_hrk;
	}
	/**
	 * @param exp_balance_hrk The exp_balance_hrk to set.
	 */
	public void setExp_balance_hrk(BigDecimal exp_balance_hrk) {
		this.exp_balance_hrk = exp_balance_hrk;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the register_no.
	 */
	public String getRegister_no() {
		return register_no;
	}
	/**
	 * @param register_no The register_no to set.
	 */
	public void setRegister_no(String register_no) {
		this.register_no = register_no;
	}
	
	/**
	 * @return Returns the cus_id.
	 */
	public BigDecimal getCus_id() {
		return cus_id;
	}
	/**
	 * @param cus_id The cus_id to set.
	 */
	public void setCus_id(BigDecimal cus_id) {
		this.cus_id = cus_id;
	}
	/**
	 * @return Returns the dateOfReport.
	 */
	public Date getDateOfReport() {
		return dateOfReport;
	}
	/**
	 * @param dateOfReport The dateOfReport to set.
	 */
	public void setDateOfReport(Date dateOfReport) {
		this.dateOfReport = dateOfReport;
	}
	
	public void setDateOfReport(String date) {
		if (date!=null && !date.trim().equals("")){
			this.dateOfReport  = convertStringToDate(date);
		}else{
			this.dateOfReport = Date.valueOf("9999-12-31");
		}
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
	 * @return Returns the clientCode.
	 */
	public String getClientCode() {
		return clientCode;
	}
	/**
	 * @param clientCode The clientCode to set.
	 */
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
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
	 * @return Returns the subtype_coll.
	 */
	public BigDecimal getSubtype_coll() {
		return subtype_coll;
	}
	/**
	 * @param subtype_coll The subtype_coll to set.
	 */
	public void setSubtype_coll(BigDecimal subtype_coll) {
		this.subtype_coll = subtype_coll;
	}
	public String getSubtype_coll_str() {
		return subtype_coll_str;
	}
	/**
	 * @param subtype_coll The subtype_coll to set.
	 */
	public void setSubtype_coll(String subtype_coll_str) {
		this.subtype_coll_str = subtype_coll_str;
	}
	public void reset_object(){
		this.cus_id = null;
		this.col_hea_id = null;
		this.col_cat_id = null;
		this.subtype_coll = null;
		this.clientCode = "";
		//this.clientType = "";
		//this.ponder = "";
		this.exp_coll_amount = null;
		this.name = "";
		this.cus_acc_no = "";
		this.exp_balance_hrk = null;
		this.col_type_id = null;
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
	 * 
	 * @return prihvatljivost
	 */
	public int getEligibility() {
		return eligibility;
	}
	/**
	 * 
	 * @param eligibility prihvatljivost
	 */
	public void setEligibility(int eligibility) {
		this.eligibility = eligibility;
	}
}
