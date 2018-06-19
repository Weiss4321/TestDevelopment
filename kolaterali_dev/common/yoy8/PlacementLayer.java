/*
 * Created on 2006.12.28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.util.Arrays;


/**
 * 
 * Matematicki model plasmana. gradi se vektor koji sadrzi id i vrijednost plasmana.
 * uparuje se sa PlacementData objektom pri upisu u bazu
 * 
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PlacementLayer{


	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/PlacementLayer.java,v 1.6 2010/04/26 09:29:29 hraamh Exp $";

	private BigDecimal[] values=null;
	private BigDecimal[] ids=null;
	private int count=0;
	


	
	public PlacementLayer(int count){
		this.count=count;
		this.values= new BigDecimal[count];
		Arrays.fill(values,null);
		this.ids= new BigDecimal[count];
	}
	
	public PlacementLayer(BigDecimal[] values,BigDecimal[] ids){
		this.count=values.length;
		this.values= new BigDecimal[count];
		this.ids= ids;
		for(int i=0;i<count;i++){
			DecimalUtils.copy(values[i]);
		}
	}
	/**
	 * zbraja sve iznose plasmana
	 * @return zbroj plasmana
	 */
	public BigDecimal sum() {
		BigDecimal result= new BigDecimal(0);
		for(int i=0;i<this.count;i++){
			if(values[i]!=null){
				result=result.add(values[i]);
			}	
		}
		return result;
	}

	/**
	 * zbraja plasmane kojima je:
	 * <br>
	 * a)included=true i polje u marked=true
	 * <br>
	 * ili
	 * <br>
	 * b)included=false i polje u marked=false
	 * 
	 * @param marked polje oznaka
	 * @param included flag da li se gledaju oznaceni ili neoznaceni plasmani
	 * @return zbroj
	 */
	public BigDecimal sum(boolean[] marked,boolean included) {
		BigDecimal result= new BigDecimal(0);
		for(int i=0;i<this.count;i++){
			if(included){
				if((values[i]!=null)&&(marked[i])){
					result=result.add(values[i]);
				}	
			}else{
				if((values[i]!=null)&&(!marked[i])){
					result=result.add(values[i]);
				}	
			}		
		}
		return result;
	}

	/**
	 * dohvat vrijednosti indeksiranog plasmana
	 * @param index indeks plasmana
	 * @return vrijednosti indeksiranog plasmana
	 */
	public BigDecimal getElement(int index) {
		if(index<=count){
			return values[index];
		}else{
			return null;
		}
	}

	/**
	 * dohvat svih iznosa plasmana
	 * 
	 * @return iznosi plasmana
	 */
	public BigDecimal[] getElements() {
		return values;
	}
	/**
	 * postavljanje plasmana na dane vrijednosti. ako se velicina polja ne podudara ne cini nista
	 * 
	 * @param values vrijednosti plasmana
	 */
	public void setElements(BigDecimal[] values) {
		if(values.length!=this.count) return;
		this.values=values;
	}
	/**
	 * postavlja vrijednost indeksiranog plasmana na danu vrijednost
	 * 
	 * @param index indeks plasmana
	 * @param value vrijednost
	 */
	public void setElement(int index, BigDecimal value) {
		if(index<=count){
			values[index]=value;
		}	
	}
	/**
	 * dohvat broja svih plasmana
	 * @return broj svih plasmana
	 */
	public int getCount() {
		return this.count;
	}
	/**
	 * vraca omjer plasmana u odnosu na zbroj svih plasmana
	 * @param index indeks plasmana
	 * @return omjer
	 */
	public BigDecimal getElementProportion(int index) {
		BigDecimal sum=sum();
		BigDecimal number=values[index];
		if((number!=null)&&(number!=null)&&(DecimalUtils.compareValues(sum,new BigDecimal(0),DecimalUtils.MIDDLE_PRECISION)!=0)){
			return number.divide(sum,DecimalUtils.HUGE_PRECISION,BigDecimal.ROUND_HALF_EVEN);
		}else{
			return null;
		}	
	}
	/**
	 * vraca omjer plasmana u odnosu na zbroj zadanih plasmana
	 * @param index indeks plasmana
	 * @param marked polje oznacenih elemenata
	 * @param included flag da li se zbrajaju oznaceni ili neoznaceni
	 * @return omjer
	 */
	public BigDecimal getElementProportion(int index, boolean[] marked, boolean included) {
		if(marked==null) return getElementProportion(index);
		BigDecimal sum=sum(marked,included);
		BigDecimal number=values[index];
		if((number!=null)&&(number!=null)&&(DecimalUtils.compareValues(sum,new BigDecimal(0),DecimalUtils.MIDDLE_PRECISION)!=0)){
			return number.divide(sum,DecimalUtils.HUGE_PRECISION,BigDecimal.ROUND_HALF_EVEN);
		}else{
			return null;
		}
	}
	/**
	 * inicijalizira plasmane. ako je withZero=true s vrijednosti 0; ako je withZero=false s null
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
	
	public String toString(){
		String s="P->";
		for(int i=0;i<this.values.length;i++){
			s+="\t"+DecimalUtils.scale(this.values[i],DecimalUtils.LOW_PRECISION)+"\t";
		}
		return s;
	}
	/**
	 * vraca oznake svih plasmana kojima je trenutna vrijednost >0 tj. nisu pokriveni
	 * @return polje oznaka nepokrivenih plasmana
	 */
	public boolean[] getActive(){
		boolean[] result=new boolean[this.count];
		Arrays.fill(result,false);
		BigDecimal zero=new BigDecimal(0);
		for(int i=0;i<this.count;i++){
			if(DecimalUtils.compareValues(this.values[i],zero,DecimalUtils.LOW_PRECISION)>0){
				result[i]=true;
			}
		}
		
		return result;
	}
	/**
	 * klonira objekt
	 */
	public Object clone(){
		PlacementLayer result= new PlacementLayer(this.count);
		for(int i=0;i<this.count;i++){
			result.setElement(i,DecimalUtils.copy(this.getElement(i)));
		}
		return result;
	}

	public BigDecimal[] getIds() {
		return ids;
	}
	public void setIds(BigDecimal[] ids) {
		this.ids = ids;
	}
	
	public BigDecimal getId(int index) {
		if(ids.length<index) return null;
		return ids[index];
	}
	public void setId(BigDecimal id,int index) {
		if(ids.length<index) this.ids[index] = id;
	}
}
