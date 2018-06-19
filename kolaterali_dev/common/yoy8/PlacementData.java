/*
 * Created on 2006.12.13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;


/**
 * Podaci iz CUSACC_EXPOSURE. mapiranje podataka iz izracuna na pravi zapis u CUSACC_EXPOSURE
 * 
 * @author hraamh
 *
 */
public class PlacementData extends AbstractData  implements CollElement{
	
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/PlacementData.java,v 1.9 2010/04/26 09:29:29 hraamh Exp $";
	
	/**
	 * Pokrivenost plasmana
	 */
	private BigDecimal exposure=null;
	/**
	 * Id komitenta
	 */
	private BigDecimal customerId=null;
	/**
	 * Id veze plasmana i kolaterala
	 */
	private BigDecimal loanBenId=null;
	/**
	 * ukupna bilancna izlozenost po plasmanu u kunama
	 */
	private BigDecimal exposureBalLcy=null;
	/**
	 * ukupna vanbilanèna izlozenost po plasmanu u kunama
	 */
	private BigDecimal expOffBalLcy=null;
	
	public PlacementData(BigDecimal id,String code, BigDecimal currencyId, BigDecimal value, BigDecimal customerId, BigDecimal loanBenId){
		this.id=id;
		this.code=code;
		this.customerId=customerId;
		this.currencyId=currencyId;
		this.loanBenId=loanBenId;
		this.value=value;
	}
	
	public int getDataType(){
		return PLACEMENT;
	}
	/**
	 * @return Pokrivenost plasmana
	 */
	public BigDecimal getExposure() {
		return exposure;
	}
	/**
	 * @param exposure Pokrivenost plasmana
	 */
	public void setExposure(BigDecimal exposure) {
		this.exposure = exposure;
	}
	/**
	 * Dohvat id-a komitenta
	 * @return id komitenta
	 */
	public BigDecimal getCustomerId() {
		return customerId;
	}
	/**
	 * Postavljanje id-a komitenta
	 * @param customerId id komitenta
	 */
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	/**
	 * Dohvat id-a veze plasman-kolateral
	 * @return id veze plasman-kolateral
	 */
	public BigDecimal getLoanBenId() {
		return loanBenId;
	}
	/**
	 * Postavljanje id veze plasman-kolateral
	 * @param customerAccountId id veze plasman-kolateral
	 */
	public void setLoanBenId(BigDecimal loanBenId) {
		this.loanBenId = loanBenId;
	}

	/**
	 * 
	 * ukupna vanbilanèna izlozenost po plasmanu u kunama
	 * @return the expOffBalLcy
	 */
	public BigDecimal getExpOffBalLcy() {
		return expOffBalLcy;
	}

	/**
	 * 
	 * ukupna vanbilanèna izlozenost po plasmanu u kunama
	 * @param expOffBalLcy the expOffBalLcy to set
	 */
	public void setExpOffBalLcy(BigDecimal expOffBalLcy) {
		this.expOffBalLcy = expOffBalLcy;
	}

	/**
	 * 
	 * ukupna bilancna izlozenost po plasmanu u kunama
	 * @return the exposureBalLcy
	 */
	public BigDecimal getExposureBalLcy() {
		return exposureBalLcy;
	}

	/**
	 * ukupna bilancna izlozenost po plasmanu u kunama
	 * 
	 * @param exposureBalLcy the exposureBalLcy to set
	 */
	public void setExposureBalLcy(BigDecimal exposureBalLcy) {
		this.exposureBalLcy = exposureBalLcy;
	}
}
