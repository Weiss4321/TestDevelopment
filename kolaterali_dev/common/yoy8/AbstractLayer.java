/*
 * Created on 2006.12.20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Ova klasa implementira sloj jednog prioriteta.
 * 
 * @author hraamh
 *
 */
public abstract class AbstractLayer implements Layer{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/AbstractLayer.java,v 1.6 2007/03/26 13:42:30 hraamh Exp $";
	/**
	 * vrijednosti sloja
	 */
	protected BigDecimal[] values=null;
	/**
	 * flagovi sloja. procesiraju se vrijednosti kojima je flag na true
	 */
	protected boolean[] flags=null;
	/**
	 * duljina sloja (broj elemenata u sloju)
	 */
	protected int count=0;
	/**
	 * id sloja
	 */
	protected BigDecimal id=null;
	/**
	 * tip sloja :RBA sloj
	 */
	public final static int RBA_LAYER=1;
	/**
	 * tip sloja :hipoteka druge banke
	 */
	public final static int OTHER_LAYER=2;
	/**
	 * tip sloja :jednostavni sloj
	 */
	public final static int SIMPLE_LAYER=3;
	/**
	 * tip sloja :hipoteka okvirnog sporazuma
	 */
	public final static int AGREEMENT_LAYER=4;

	/**
	 * konstruktor
	 * 
	 * @param count broj elemenata u sloju
	 * @param id id sloja
	 */
	public AbstractLayer(int count, BigDecimal id) {
		this.count=count;
		this.values= new BigDecimal[count];
		resetElements(false);
		this.flags= new boolean[count];
		resetFlags();
		this.id=id;
	}
	/**
	 * konstruktor
	 * @param id id sloja
	 */
	public AbstractLayer(BigDecimal id){
		this.id=id;
	}
	

	/**
	 * sumiranje vrijednosti sloja kojima je flag postavljen na true
	 * 
	 * @return
	 */
	public BigDecimal sum() {
		BigDecimal result= new BigDecimal(0);
		for(int i=0;i<this.count;i++){
			//zbraja samo one elemente kojima je flag postavljen na true
			if((flags[i])&&(values[i]!=null)){
				result=result.add(values[i]);
			}	
		}
		return result;
	}
	
	/**
	 * sumiranje vrijednosti sloja. ako je include true sumiraju se vrijednostima ciji indeksi 
	 * u marked i flags su postavljene na true. Ako je include false sumiraju se vrijednosti kojima je
	 * flag na true a marked na flase
	 * 
	 * @param marked polje oznacenih indeksa
	 * @param include ako je true sumiraju se vrijednosti s true u merked polju; ako je false sumiraju
	 * se vrijednosti s false u merked polju
	 * @return suma vrijednosti
	 */
	public BigDecimal sum(boolean[] marked, boolean include) {
		if(marked==null) return sum();
		BigDecimal result= new BigDecimal(0);
		for(int i=0;i<this.count;i++){
			//zbraja samo one elemente kojima je flag postavljen na true
			//i koji nisu postavljeni u exclude polju
			if(values[i]!=null){
				if(include){
					if((flags[i])&&(values[i]!=null)&&(marked[i])){
						result=result.add(values[i]);
					}
				}else{
					if((flags[i])&&(values[i]!=null)&&(!marked[i])){
						result=result.add(values[i]);
					}
				}	
			}
		}
		return result;
	}
	
	/**
	 * zbraja iznose u polju oznacenih polja (ako je include=true) ili neoznacenih polja (include=false)
	 * 
	 * @param field polje decimalnih brojeva
	 * @param marked polje oznaka
	 * @param include ako je true zbraja sve koji su oznaceni sa true u polju marked; ako je false zbraja sve koji su oznaceni sa false u polju marked
	 * @return zbroj
	 */
	public static BigDecimal sum(BigDecimal[] field,boolean[] marked, boolean include) {
		if((field==null)||(marked==null)) return null;
		BigDecimal result= new BigDecimal(0);
		for(int i=0;i<field.length;i++){
			//zbraja samo one elemente kojima je flag postavljen na true
			//i koji nisu postavljeni u exclude polju
			if(field[i]!=null){
				if(include){
					if((field[i]!=null)&&(marked[i])){
						result=result.add(field[i]);
					}
				}else{
					if((field[i]!=null)&&(!marked[i])){
						result=result.add(field[i]);
					}
				}	
			}
		}
		return result;
	}

	/**
	 * dohvat elementa odreðen indeksom. Ako je indeks izvan polja sloja vraca se null
	 * 
	 * @param index indeks elementa
	 * @return vrijednost elementa
	 */
	public BigDecimal getElement(int index) {
		if((index<=count)&&(values!=null)){
			return values[index];
		}else{
			return null;
		}
	}

	/**postavljanje vrijednosti elementa. Ako indeks izlazi iz granica sloja nista se ne radi
	 * 
	 * @param index indeks elementa
	 * @param value vrijednosti elementa
	 */
	public void setElement(int index, BigDecimal value) {
		if(index<=count){
			values[index]=value;
		}		
	}
	/**
	 * vraca broj elemenata u sloju
	 * @return
	 */
	public int getCount() {
		return this.count;
	}
	/**
	 * vraca proporcionalnost elementa po formuli V(index)/V(1)+...+V(index)+...+V(n)
	 * @param index indeks elementa
	 * @return proporcionalnost elementa
	 */
	public BigDecimal getElementProportion(int index) {
		BigDecimal sum=sum();
		BigDecimal number=values[index];
		if((number!=null)&&(sum.compareTo(new BigDecimal(0))!=0)){
			return number.divide(sum,DecimalUtils.HUGE_PRECISION,BigDecimal.ROUND_HALF_EVEN);
		}else{
			return new BigDecimal(0);
		}	
	}
	/**
	 * vraca proporcionalnost elementa po formuli V(index)/V(1)+...+V(index)+...+V(n)
	 * za sve elemente oznacene u marked polju (ako je include true) ili za sve elemente neoznacene u marked
	 * polju (ako je include false)
	 * 
	 * @param index indeks elementa
	 * @param marked oznacavajuce polje
	 * @param include ako je true gledaju se oznacenmi elementi; ako je false gledaju se neoznaceni elementi
	 * @return proporcionalnost elementa
	 */
	public BigDecimal getElementProportion(int index, boolean[] marked, boolean include) {
		if(marked==null) return getElementProportion(index);
		BigDecimal sum=sum(marked,include);
		BigDecimal number=values[index];
		if((number!=null)&&(sum.compareTo(new BigDecimal(0))!=0)){
			return number.divide(sum,DecimalUtils.HUGE_PRECISION,BigDecimal.ROUND_HALF_EVEN);
		}else{
			return new BigDecimal(0);
		}	
	}
	/**
	 * vraca proporcionalnost elementa po formuli V(index)/V(1)+...+V(index)+...+V(n)
	 * za sve elemente polja field oznacene u marked polju (ako je include true) ili za sve elemente neoznacene u marked
	 * polju (ako je include false)
	 * 
	 * @param field polje elemenata
	 * @param index indeks elementa
	 * @param marked oznacavajuce polje
	 * @param include ako je true gledaju se oznacenmi elementi; ako je false gledaju se neoznaceni elementi
	 * @return proporcionalnost elementa
	 */
	public static BigDecimal getProportion(BigDecimal[] field,int index, boolean[] marked, boolean include) {
		if((field==null)||(marked==null)) return null;
		BigDecimal sum=AbstractLayer.sum(field,marked,include);
		BigDecimal number=field[index];
		if((number!=null)&&(sum.compareTo(new BigDecimal(0))!=0)){
			return number.divide(sum,DecimalUtils.HUGE_PRECISION,BigDecimal.ROUND_HALF_EVEN);
		}else{
			return new BigDecimal(0);
		}	
	}
	/**
	 * vraca indeksirani flag
	 * 
	 * @param index indeks
	 * @return indeksirani flag; false ako indeks prelazi granice sloja
	 */
	public boolean getFlag(int index) {
		if(index<=count){
			return flags[index];
		}else{
			return false;
		}
	}
	/**
	 * postavlja polje flagova ako je istih dimenzija kao postojece polje. ako nije nista ne radi
	 * 
	 * @param values polje flagova
	 * @return true ako je istih dimenzija kao postojece polje; false u suprotnom
	 */
	public void setFlag(int index, boolean value) {
		if(index<=count){
			flags[index]=value;
		}	
	}
	/**
	 * inicijalizira polje vrijednosti sloja. ako je withZero onda je inicijalna vrijednost BigDecimal(0),
	 * u suprotnom je null.
	 * 
	 * @param withZero
	 */
	public void resetElements(boolean withZero) {
		if(withZero){
			Arrays.fill(values,new BigDecimal(0));
		}else{
			Arrays.fill(values,null);
		}
		
	}
	/**
	 * postalja flase na sve flagove polja
	 *
	 */
	public void resetFlags() {
		Arrays.fill(this.flags, false);
	}
	/**
	 * provjerava da li je flag elementa true
	 * 
	 * @param index indeks elementa
	 * @return vrijednost flaga
	 */
	public boolean isActive(int index) {
		if(index<=count){
			return flags[index];
		}else{
			return false;
		}
	}
	/**vraca polje flagova
	 * 
	 * @return polje flagova
	 */
	public boolean[] getFlags(){
		return this.flags;
	}
	/**
	 * vraca polje vrijednosti
	 * 
	 * @return polje vrijednosti
	 */
	public BigDecimal[] getElements(){
		return this.values;
	}
	/**postavlja polje vrijednosti ako je istih dimenzija kao postojece polje. ako nije nista ne radi
	 * 
	 * @param elements polje vrijednosti
	 * @return true ako je istih dimenzija kao postojece polje; false u suprotnom
	 */
	public boolean setElements(BigDecimal[] elements){
		if((elements!=null)&&(elements.length==this.count)){
			this.values=elements;
			return true;
		}else{
			return false;
		}
		
	}
	/**
	 * dohvaca id sloja
	 * 
	 * @return id sloja
	 */
	public BigDecimal getId(){
		return this.id;
	}
}
