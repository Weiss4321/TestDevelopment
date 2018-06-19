/*
 * Created on 2007.01.11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractData {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/AbstractData.java,v 1.4 2007/03/02 15:22:18 hraamh Exp $";
	/**
	 * tip podatka koji odgovara kolateralu
	 */
	public final static int COLLATERAL=0;
	/**
	 * tip podatka koji odgovara hipoteci
	 */
	public final static int MORTGAGE=1;
	/**
	 * tip podatka koji odgovara plasmanu
	 */
	public final static int PLACEMENT=2;
	/**
	 * tip podatka koji odgovara okvirnom sporazumu
	 */
	public final static int AGREEMENT=3;
	/**
	 * Id primarni kljuc
	 */
	protected BigDecimal id=null;
	/**
	 * vrijednost
	 */
	protected BigDecimal value=null;
	/**
	 * id valute
	 */
	protected BigDecimal currencyId=null;
	/**
	 * unique sifra podatka
	 */
	protected String code=null;
	/**
	 * prioritet
	 */
	protected int priority=-1;

	/**
	 * @return id valute.
	 */
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	/**
	 * @param currencyId id valute.
	 */
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	/**
	 * @return Id primarni kljuc
	 */
	public BigDecimal getId() {
		return id;
	}
	/**
	 * @param id primarni kljuc
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}
	/**
	 * @return prioritet
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority prioritet
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return vrijednost
	 */
	public BigDecimal getValue() {
		return value;
	}
	/**
	 * @param value vrijednost
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	/**
	 * @return sifra podatka
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code sifra podatka
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * Vraca tip podatka
	 * 
	 * @return tip podatka
	 */
	public abstract int getDataType();
	/**
	 * Vraca kljuc za hash tablicu
	 * 
	 * @param id objekt ciji se toString() rezultat koristi kao kljuc
	 * @return kljuc za hash tablicu
	 */
	public static Object makeHashKey(Object id){
		if(id!=null){
			return id.toString();
		}else{
			return null;
		}
	}
}
