/*
 * Created on 2007.01.24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;

/**
 * @author hraamh
 *
 */
public class AgreementVector extends SimpleVector implements CollateralVector{
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/AgreementVector.java,v 1.4 2007/03/07 13:42:30 hraamh Exp $";
	
	/**
	 * flagovi s true vrijednostima na indeksima kolaterala ukljucenih u okvirni sporazum
	 */
	private boolean[] dependency=null;
	
	private BigDecimal[] collateralIds=null;
	
	/**
	 * fiksni maksimalni iznos okvirnog sporazuma
	 */
	private BigDecimal fixedMaximum=null;

	/**
	 * 
	 * @param count
	 * @param id
	 * @param fixedValue
	 * @param allColateralsCount
	 */
	public AgreementVector(int count, BigDecimal id, BigDecimal fixedValue, BigDecimal[] collaterals) {
		super(count,-1, id, null);
		this.fixedMaximum=fixedValue;
		this.collateralIds=collaterals;
		//this.priority=priority;
		//this.layer.setFlags(flags);
	}

	/**
	 * 
	 * @param layer
	 * @param id
	 * @param fixedValue
	 * @param dependency
	 */
	public AgreementVector(CollateralLayer layer, BigDecimal id,BigDecimal fixedValue,BigDecimal[] collaterals, boolean[] dependency, int priority) {
		super(-1,layer, id, null);
		this.fixedMaximum=fixedValue;
		this.dependency=dependency;
		this.collateralIds=collaterals;
		this.priority=priority;
	}
	/**
	 * vraca tip kolateral vektora = AbstractVector.AGREEMENT_VECTOR
	 * ovo je virtualni kolateral vektor; vektor okvirnog sporazuma
	 * 
	 * @return tip vektora
	 */
	public int getType() {
		return AGREEMENT_VECTOR;
	}
	
	/**
	 * Racuna se iznos okvirnog sporazuma kojim se pokrivaju plasmani.
	 * 
	 * V(S)=min(fiksni iznos,SUM(kolaterali u sporazumu))
	 * 
	 * @param dependencySum suma ostataka kolaterala koji ulaze u sporazum
	 * @return iznos okvirnog sporazuma
	 */
	public BigDecimal setValue(BigDecimal dependencySum){
		if(dependencySum.compareTo(fixedMaximum)>=0){
			this.value=DecimalUtils.copy(fixedMaximum);
		}else{
			this.value=DecimalUtils.copy(dependencySum);
		}
		return value;
	}

	/**
	 * Dohvaca flagove koji su kolaterali ukljuceni u okvirni sporazum
	 * 
	 * @return flagovi s true vrijednostima na indeksima kolaterala ukljucenih u okvirni sporazum
	 */
	public boolean[] getDependencys() {
		return dependency;
	}
	/**
	 * postavlja koji su kolaterali ukljuceni u okvirni sporazum
	 * 
	 * @param dependency flagovi s true vrijednostima na indeksima kolaterala ukljucenih u okvirni sporazum
	 */
	public void setDependencys(boolean[] dependency) {
		this.dependency = dependency;
	}
	
	public void setDependency(int index,boolean value) {
		if((this.dependency!=null)&&(index<=this.dependency.length)){
			this.dependency[index]=value;
		}
	}
	
	public boolean getDependency(int index){
		if((this.dependency!=null)&&(index<=this.dependency.length)){
			return dependency[index];
		}else{
			return false;
		}		
	}
	
	/**
	 * Dohvaca fiksni maksimalni iznos okvirnog sporazuma
	 * 
	 * @return fiksni maksimalni iznos okvirnog sporazuma
	 */
	public BigDecimal getFixedMaximum() {
		return fixedMaximum;
	}
	/**
	 * Postavlja fiksni maksimalni iznos okvirnog sporazuma
	 * 
	 * @param fixedMaximum maksimalni iznos okvirnog sporazuma
	 */
	public void setFixedMaximum(BigDecimal fixedMaximum) {
		this.fixedMaximum = fixedMaximum;
	}
	
	public String toString(){
		return "FA->VALUE:"+this.value+"\n"+"\t\t\t"+this.layer.toString();
	}
	/**
	 * @return Returns the collateralIds.
	 */
	public BigDecimal[] getCollateralIds() {
		return collateralIds;
	}
	/**
	 * @param collateralIds The collateralIds to set.
	 */
	public void setCollateralIds(BigDecimal[] collateralIds) {
		this.collateralIds = collateralIds;
	}
	/**
	 * provjerava da li kolateral danog id-a ulazi u okvirni sporazum
	 * 
	 * @param coll_id id kolaterala
	 * @return true ako kolateral ulazi u okvirni sporazum
	 */
	public boolean involvesCollateral(BigDecimal coll_id){
		for(int i=0;i<this.collateralIds.length;i++){
			if(this.collateralIds[i].compareTo(coll_id)==0) return true;
		}
		return false;
	}
}
