/*
 * Created on 2007.08.29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo14;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO14Data {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo14/BO14Data.java,v 1.1 2007/08/29 13:53:43 hratar Exp $";
	
	//podatci dobiveni iz aplikacije
	private BigDecimal collTypeCode;
	private BigDecimal collCategoryCode;
	private String groupOfIssuers; //to je pokirvenost
	
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
		if (collCat!=null && !collCat.trim().equals("")){
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
		if (collType!=null && !collType.trim().equals("")){
			this.collTypeCode  = new BigDecimal(collType);
		}
	}
	
	/**
	 * @return Returns the amount.
	 */
	public String getGroupOfIssuers() {
		return groupOfIssuers;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setGroupOfIssuers(String str) {
		this.groupOfIssuers = str;
	}
	
}

