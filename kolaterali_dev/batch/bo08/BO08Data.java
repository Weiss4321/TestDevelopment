/*
 * Created on 2007.03.29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo08;
import java.sql.Date;
import java.math.BigDecimal;
/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO08Data {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo08/BO08Data.java,v 1.4 2007/05/16 08:29:27 hratar Exp $";
	
	//podaci koji se unose kroz aplikaciju
	final	private Date dateFromEval;
	final	private Date dateUntilEval;
	final	private BigDecimal category;
	final	private BigDecimal coll_type;
	final	private BigDecimal coll_subtype;
	final	private BigDecimal county;
	final	private BigDecimal location;
	final	private BigDecimal district;
	final	private Date dateOfReval;
	final	private BigDecimal revalPercentage;
	
//	podatci vezani za pojedini kolateral
	private BigDecimal real_est_nomi_valu;
	private BigDecimal iznos_revalorizacije;
	private BigDecimal real_est_nm_cur_id;
	private Date real_est_nomi_date;
	private String reva_date_am;
	private String reva_flag;
	private BigDecimal col_hea_id;
	private String accounting_indic;
	private BigDecimal col_number;//broj selektiranih kolaterala koji ulaze u izracun revalorizacije
	
	/**
	 * Konstruktor stavlja ulazne parametre u pozadinski objekt (spremaju se kao konstante)
	 */
	BO08Data(hr.vestigo.framework.remote.batch.BatchContext bc) {
		int i;
		String [] parametri=new String[11];
		
		
		for(i=0;i<bc.getArgs().length;i++){
			parametri[i]=bc.getArg(i).trim();
			bc.debug("SQL_param[" + String.valueOf(i) + "]=" + parametri[i]);
		}		


		//bc.setBankSign("RB");
		bc.setBankSign(parametri[10]);
		
    
    	for (i=0;i<parametri.length;i++)
    		System.out.println("parametri[" + String.valueOf(i) + "]=" + parametri[i]);
    	
		
    	
		
    	if (parametri[6]!=null && !parametri[6].trim().equals("")){
    		this.category  = new BigDecimal(parametri[6]);
		} else {
			this.category = null;
		}
		
    	
    	if (parametri[1]!=null && !parametri[1].trim().equals("")){
    		this.coll_type  = new BigDecimal(parametri[1]);
		} else {
			this.coll_type = null;
		}
		
    	if (parametri[2]!=null && !parametri[2].trim().equals("")){
    		this.coll_subtype  = new BigDecimal(parametri[2]);
		} else {
			this.coll_subtype = null;
		}
		
    	if (parametri[3]!=null && !parametri[3].trim().equals("")){
    		this.county  = new BigDecimal(parametri[3]);
		} else {
			this.county = null;
		}
		
    	if (parametri[4]!=null && !parametri[4].trim().equals("")){
    		this.location  = new BigDecimal(parametri[4]);
		} else {
			this.location = null;
		}

    	if (parametri[5]!=null && !parametri[5].trim().equals("")){
    		this.district  = new BigDecimal(parametri[5]);
		} else {
			this.district = null;
		}
		
    	if (parametri[0]!=null && !parametri[0].trim().equals("")){
			this.dateOfReval  = convertStringToDate(parametri[0]);
		}else{
			this.dateOfReval = Date.valueOf("1970-01-01");
		}
		
		if (parametri[7]!=null && !parametri[7].trim().equals("")){
    		this.revalPercentage  = new BigDecimal(parametri[7]);
		} else {
			this.revalPercentage = null;
		}
		
		if (parametri[8]!=null && !parametri[8].trim().equals("")){
			this.dateFromEval  = convertStringToDate(parametri[8]);
		}else{
			this.dateFromEval = Date.valueOf("1970-01-01");
		}
		
		if (parametri[9]!=null && !parametri[9].trim().equals("")){
			this.dateUntilEval  = convertStringToDate(parametri[9]);
		}else{
			this.dateUntilEval = Date.valueOf("9999-12-31");
		}
		
	}
	
	public void reset_all_coll() {
		real_est_nomi_valu = null;
		real_est_nm_cur_id= null;
		iznos_revalorizacije = null;
		real_est_nm_cur_id = null;
		real_est_nomi_date = null;
		reva_flag = "";
	}
	

	/**
	 * @return Returns the category.
	 */
	public BigDecimal getCategory() {
		return category;
	}
	/**
	 * @param category The category to set.
	 */

	
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
	 * @return Returns the col_number.
	 */
	public BigDecimal getCol_number() {
		return col_number;
	}
	/**
	 * @param col_number The col_number to set.
	 */
	public void setCol_number(BigDecimal col_number) {
		col_number = col_number;
	}
	/**
	 * @return Returns the coll_subtype.
	 */
	public BigDecimal getColl_subtype() {
		return coll_subtype;
	}
	
	
	/**
	 * @return Returns the coll_type.
	 */
	public BigDecimal getColl_type() {
		return coll_type;
	}
	
	
	/**
	 * @return Returns the county.
	 */
	public BigDecimal getCounty() {
		return county;
	}
	
	
	
	/**
	 * @return Returns the dateFromEval.
	 */
	public Date getDateFromEval() {
		return dateFromEval;
	}
	
	/**
	 * @return Returns the dateOfReval.
	 */
	public Date getDateOfReval() {
		return dateOfReval;
	}
	
	/**
	 * @return Returns the dateUntilEval.
	 */
	public Date getDateUntilEval() {
		return dateUntilEval;
	}
	
	/**
	 * @return Returns the district.
	 */
	public BigDecimal getDistrict() {
		return district;
	}
	
	
	/**
	 * @return Returns the location.
	 */
	public BigDecimal getLocation() {
		return location;
	}
	
	/**
	 * @return Returns the revalPercentage.
	 */
	public BigDecimal getRevalPercentage() {
		return revalPercentage;
	}
	
	/**
	 * @return Returns the real_est_nm_cur_id.
	 */
	public BigDecimal getReal_est_nm_cur_id() {
		return real_est_nm_cur_id;
	}
	/**
	 * @param real_est_nm_cur_id The real_est_nm_cur_id to set.
	 */
	public void setReal_est_nm_cur_id(BigDecimal real_est_nm_cur_id) {
		this.real_est_nm_cur_id = real_est_nm_cur_id;
	}
	/**
	 * @return Returns the real_est_nomi_date.
	 */
	public Date getReal_est_nomi_date() {
		return real_est_nomi_date;
	}
	/**
	 * @param real_est_nomi_date The real_est_nomi_date to set.
	 */
	public void setReal_est_nomi_date(Date real_est_nomi_date) {
		this.real_est_nomi_date = real_est_nomi_date;
	}
	/**
	 * @return Returns the reva_date_am.
	 */
	public String getReva_date_am() {
		return reva_date_am;
	}
	/**
	 * @param reva_date_am The reva_date_am to set.
	 */
	public void setReva_date_am(String reva_date_am) {
		this.reva_date_am = reva_date_am;
	}
	/**
	 * @return Returns the reva_flag.
	 */
	public String getReva_flag() {
		return reva_flag;
	}
	/**
	 * @param reva_flag The reva_flag to set.
	 */
	public void setReva_flag(String reva_flag) {
		this.reva_flag = reva_flag;
	}
	
	
	/**
	 * @return Returns the cvsident.
	 */
	public static String getCvsident() {
		return cvsident;
	}
	/**
	 * @param cvsident The cvsident to set.
	 */
	public static void setCvsident(String cvsident) {
		BO08Data.cvsident = cvsident;
	}
	
	
	/**
	 * @return Returns the real_est_nomi_valu.
	 */
	public BigDecimal getReal_est_nomi_valu() {
		return real_est_nomi_valu;
	}
	/**
	 * @param real_est_nomi_valu The real_est_nomi_valu to set.
	 */
	public void setReal_est_nomi_valu(BigDecimal real_est_nomi_valu) {
		this.real_est_nomi_valu = real_est_nomi_valu;
	}
	/**
	 * @return Returns the iznos_revalorizacije.
	 */
	public BigDecimal getIznos_revalorizacije() {
		return iznos_revalorizacije;
	}
	/**
	 * @param iznos_revalorizacije The iznos_revalorizacije to set.
	 */
	public void setIznos_revalorizacije(BigDecimal iznos_revalorizacije) {
		this.iznos_revalorizacije = iznos_revalorizacije;
	}
	
	/**
	 * @return Returns the accounting_indic.
	 */
	public String getAccounting_indic() {
		return accounting_indic;
	}
	/**
	 * @param accounting_indic The accounting_indic to set.
	 */
	public void setAccounting_indic(String accounting_indic) {
		this.accounting_indic = accounting_indic;
	}
	
	private static Date convertStringToDate(String dataString){
		
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
	
	
}
