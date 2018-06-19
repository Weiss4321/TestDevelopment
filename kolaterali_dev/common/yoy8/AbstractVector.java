/*
 * Created on 2006.12.21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;
import hr.vestigo.modules.rba.util.BooleanUtils;
import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractVector implements Comparable{
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/AbstractVector.java,v 1.10 2007/05/02 14:24:26 hraamh Exp $";
	/**
	 * vektor koji nije slojevit
	 */
	public final static int SIMPLE_VECTOR=1;
	/**
	 * slojeviti vektor
	 */
	public final static int COMPLEX_VECTOR=2;
	/**
	 * vektor okvirnog sporazuma (nije slojevit)
	 */
	public final static int AGREEMENT_VECTOR=3;
	/**
	 * id vektora
	 */
	protected BigDecimal id=null;
	/**
	 * ukupni broj plasmana
	 */
	protected int count=0;
	/**
	 * vrijednost vektora
	 */
	protected BigDecimal value=null;
	/**
	 * prioritet vektora
	 */
	protected int priority=-1;
	/**
	 * datum potvrde collateral officera
	 */
	protected Date processedDate=null;
	/**
	 * vrijeme upisa u tablicu
	 */
	protected Timestamp openingTS=null;
	/**
	 * minus vrijednost ako hipoteke kolaterala prelaze njegovu vrijednost; 
	 */
	protected BigDecimal minus=null;
	/**
	 * indeks zadnjeg sloja koji je radio s pozitivnom vrijednosti kolaterala
	 */
	protected int lastLayer=-1;

	/**
	 * konstruktor
	 * 
	 * @param count sveukupni broj plasmana
	 * @param id id plasmana
	 * @param value vrijednost vektora
	 * @param priority prioritet/likvidnost vektora
	 */
	public AbstractVector(int count,BigDecimal id, BigDecimal value, int priority) {
		this.id=id;
		this.count=count;
		this.value=value;
		this.priority=priority;
	}
	/**
	 * id kolateral vektora
	 * @return id
	 */
	public BigDecimal getId(){
		return this.id;
	}
	/**
	 * vraca prioritet vektora (likvidnost vektora)
	 * @return prioritet/likvidnost vektora
	 */
	public int getPriority(){
		return priority;
	}
	/**
	 * postavlja prioritet vektora (likvidnost vektora)
	 * 
	 * @param priority prioritet/likvidnost vektora
	 */
	public void setPriority(int priority){
		this.priority=priority;
	}
	/**
	 * dohvat elementa odreðen indeksom. Ako je indeks izvan polja slojeva vraca se null
	 * 
	 * @param index indeks elementa
	 * @return vrijednost elementa
	 */
	public BigDecimal[] getElements() {
		BigDecimal[] result=new BigDecimal[this.count];
		for(int i=0;i<this.count;i++){
			result[i]=getElement(i);
		}
		return result;
	}
	/**
	 * 
	 * @param index
	 * @param marked
	 * @param include
	 * @return
	 */
	public BigDecimal getElementProportion(int index, boolean[] marked, boolean include) {
		BigDecimal sum=sum(marked,include);
		BigDecimal number=getElement(index);
		if((number!=null)&&(sum.compareTo(new BigDecimal(0))!=0)){
			return number.divide(sum,DecimalUtils.HUGE_PRECISION,BigDecimal.ROUND_HALF_EVEN);
		}else{
			return new BigDecimal(0);
		}	
	}
	/**
	 * vraca flagove svih elemenata
	 * 
	 * @return flagovi svih elemenata unutar slojeva
	 */
	public abstract boolean[] getFlags();
	
	public boolean getFlag(int index){
		return getFlags()[index];
	}
	/**
	 * dohvat elementa odreðen indeksom. Ako je indeks izvan polja slojeva vraca se null
	 * 
	 * @param index indeks elementa
	 * @return vrijednost elementa
	 */
	public abstract BigDecimal getElement(int index);
	/**
	 * vraca broj elemenata po sloju
	 * 
	 * @return
	 */
	public int getCount(){
		return this.count;
	}
	/**
	 * vraca vrijednost vektora
	 * @return vrijednost vektora
	 */
	public BigDecimal getValue() {
		return this.value;
	}
	
	
/*	
	 (non-Javadoc)
	 * @see hr.vestigo.modules.collateral.common.yoy8.CollateralVector#getActive()
	 
	public int[] getActive() {
		boolean[] b= getFlags();
		int[] temp=null;
		if(count!=0){			
			int counter=0;
			for(int i=0;i<this.count;i++){
				if(b[i]){
					counter++;
				}
			}
			if (counter!=0){
				temp=new int[counter];
				counter=0;
				for(int i=0;i<this.count;i++){
					if(b[i]){
						temp[counter++]=i;
					}
				}
			}			
		}
		return temp;
	}*/
	
	
	/**
	 * vraca boolean polje s true vrijednosti na indeksima plasmana ukljucenih u vektor kojima je i vrijednost veca od 0
	 * 
	 * @return polje aktivnih plasmana
	 */
	public boolean[] getActive() {
		if(this.count==0) return null;
		boolean[] flags=getFlags();
		boolean[] result= new boolean[this.count];
		BigDecimal zero= new BigDecimal(0);
		Arrays.fill(result,false);
		for(int i=0;i<this.count;i++){
			if((flags[i])&&(getElement(i)!=null)&&(DecimalUtils.compareValues(getElement(i),zero,DecimalUtils.LOW_PRECISION)>0)){
				result[i]=true;
			}
		}
		return result;
	}
	/**
	 * zbraja iznose u polju oznacenih polja (ako je include=true) ili neoznacenih polja (include=false)
	 * 
	 * @param elements polje oznaka
	 * @param include ako je true zbraja sve koji su oznaceni sa true u polju marked; ako je false zbraja sve koji su oznaceni sa false u polju marked
	 * @return zbroj
	 */
	protected BigDecimal sum(boolean[] elements,boolean include){
		BigDecimal[] e= getElements();
		BigDecimal result=new BigDecimal(0);
		for(int i=0;i<this.count;i++){
			//System.out.print("e[i]:"+e[i]+"\t");
			if(include){
				if(elements[i]){
					if(e[i]!=null){
						result=result.add(e[i]);
					}
				}
			}else{
				if(!elements[i]){
					if(e[i]!=null){
						result=result.add(e[i]);
					}
				}
			}
		}
		System.out.println();
		return result;
	}
	/**
	 * allikvotna raspodjela vrijednosti kolateral vektora na plasmane unutar slojeva
	 * 
	 * @param placements sloj plasmana
	 * @param layer sloj hipoteke
	 * @param value vrijednost kolaterala
	 * @return stanje plasmana
	 */
	protected BigDecimal[] aliquot(PlacementLayer placements, Layer layer, BigDecimal value) {
		if(placements==null) return null;
		BigDecimal zero= new BigDecimal(0);
		layer.resetElements(false);
		boolean[] flags=layer.getFlags();
		//zbrajam samo one koji su ukljuceni u raspodjelu za ovaj kolateral
		BigDecimal s=placements.sum(flags,true);
		//ako ima dovoljno kolaterala za potpuno pokrice plasmana
		if(DecimalUtils.compareValues(value,s,DecimalUtils.MIDDLE_PRECISION)>=0){
			for(int i=0;i<this.count;i++){
				if(flags[i]){
					//postavljam pune iznose plasmana u elemente pokrivenosti
					
					layer.setElement(i,placements.getElement(i));
					placements.setElement(i,new BigDecimal(0));
					
				}
			}
			//	ako nema dovoljno kolaterala za potpuno pokrice plasmana
		}else{
			BigDecimal[] replacements= new BigDecimal[layer.getCount()];
			for(int i=0;i<this.count;i++){
				if(flags[i]){
					//postavljam alikvotno izracunate iznose plasmana u elemente pokrivenosti
					BigDecimal temp=placements.getElementProportion(i,flags,true);
					//alikvotno pridjeljena vrijednost
					BigDecimal val=temp.multiply(value);
					// decimalna kriza!
					layer.setElement(i,DecimalUtils.scale(val,DecimalUtils.HIGH_PRECISION));
					//placements.setElement(i,placements.getElement(i).subtract(val));
					replacements[i]=placements.getElement(i).subtract(val);
				}else{
					replacements[i]=placements.getElement(i);
				}
			}
			placements.setElements(replacements);
		}
		return placements.getElements();
	}
	/**
	 * raspodjela pokrivenosti plasmana.
	 * 
	 * @param amounts iznosi nepokrivenih plasmana
	 * @param maxAmount maksimalan iznos kojim se moze vrsiti namijestanje
	 * @param from polja plasmana od kojih se moze oduzeti vrijednost
	 * @param to polja plasmana kojima se dodaje vrijednost.ako je to=null onda se vrsi samo oduzimanje sredstava koje se koristi radi podunjenja okvirnog sporazuma
	 * @return true ako je izvrsena raspodjela
	 */
	public boolean revalue(BigDecimal[] amounts,BigDecimal maxAmount, boolean[] from, boolean[] to) {
		boolean result=false;
		//ne vrsi namijestanje ako kolateral ulazi u - s stranom hipotekom
		if(this.lastLayer!=(getLayerCount()-1)) return false;
		//provjera da li ovaj kolateral uopce pokriva plasmane koji se trebaju ispremijesati
		boolean[] flagsFromAll=this.getFlags();
		boolean[] flagsToAll=this.getFlags();
		BigDecimal zero=new BigDecimal(0);
		if(to!=null){
			flagsFromAll=BooleanUtils.and(flagsFromAll,from);
			flagsToAll=BooleanUtils.and(flagsToAll,to);
			//ako nema od koga uzeti ili kome dati nista ne radi
			if((!BooleanUtils.hasTrue(flagsFromAll))||(!BooleanUtils.hasTrue(flagsToAll))) return false;
			BigDecimal[] recalibrated=recalibrate(amounts,maxAmount,from,to);
			if (recalibrated!=null){
				//ako je izvrseno namijestanje
				amounts=recalibrated;
				result=true;
			}
		}else{
			flagsFromAll=BooleanUtils.and(flagsFromAll,from);
			if(!BooleanUtils.hasTrue(flagsFromAll)) return false;
			BigDecimal[] recalibrated=subcalibrate(amounts,maxAmount,from);
			if (recalibrated!=null){
				//ako je izvrseno namijestanje
				amounts=recalibrated;
				result=true;
			}
		}	
		return result;
	}
	/**
	 * Koristi se samo kod kolaterala na koje se veze hipoteka okvirnog sporazuma.
	 * vrsi se namijestanje tako da se samo oduzima od plasmana. Ovime se postize da se dobiva
	 * dodatni ostatak koji se moze iskoristiti u pridjeljenom okvirnom sporazumu.
	 * 
	 * @param amounts iznosi nepokrivenih plasmana
	 * @param maxAmount maksimalan iznos kojim se moze oduzeti
	 * @param from polja plasmana od kojih se moze oduzeti vrijednost
	 * @return
	 */
	protected BigDecimal[] subcalibrate(BigDecimal[] amounts,BigDecimal maxAmount, boolean[] from){
		BigDecimal zero= new BigDecimal(0);
		BigDecimal fromVal=sum(from,true);
		BigDecimal[] temp=new BigDecimal[this.count];
		Arrays.fill(temp,null);
		BigDecimal sumValue=sum(from,true);
		boolean fullValue=true;
		if(sumValue.compareTo(maxAmount)>0){
			sumValue=maxAmount;
			fullValue=false;
		}
		if(DecimalUtils.compareValues(sumValue,zero,DecimalUtils.MIDDLE_PRECISION)==0){
			return null;
		}
		for(int i=0;i<this.count;i++){
			if(from[i]){
				BigDecimal value=getElement(i);
				if((fullValue)&&(value!=null)){					
					amounts[i].add(value);
					temp[i]=null;
				}else if(value!=null){
					BigDecimal mValue=sumValue.multiply(getElementProportion(i,from,true));
					amounts[i].add(mValue);
					temp[i]=value.subtract(mValue);					
				}
			}else{
				temp[i]=getElement(i);
			}
				
		}
		setElements(temp);
		return amounts;
	}
	
	/**
	 * namjestavanje raspodjele 
	 * 
	 * @param amounts vrijednosti koje je potrebno namiriti
	 * @param maxAmount maksimalan iznos kojim se moze vrsiti namijestanje
	 * @param from oznake elemenata kojima se moze oduzeti
	 * @param to oznake elemenata kojima se treba dati
	 * @return true ako je odradeno namijestanje
	 */
	protected BigDecimal[] recalibrate(BigDecimal[] amounts,BigDecimal maxAmount, boolean[] from, boolean[] to) {
		BigDecimal[] temp;
		BigDecimal addValue;
		BigDecimal zero= new BigDecimal(0);
		BigDecimal fromVal=sum(from,true);
		
		//BigDecimal amountSum=DecimalUtils.sumField(amounts);
		//BigDecimal amountSum=AbstractLayer.sum(amounts,to,true);
		BigDecimal[] newAmounts=null;
		BigDecimal[] constAmounts=new BigDecimal[amounts.length];
		/*if(maxAmount.compareTo(amountSum)<0){
			newAmounts= new BigDecimal[amounts.length];
			for(int i=0;i<amounts.length;i++){
				if(to[i]){
					BigDecimal val=AbstractLayer.getProportion(amounts,i,to,true);
					val=val.multiply(maxAmount);
					newAmounts[i]=val;
					constAmounts[i]=val;
				}			
			}		
			amountSum=maxAmount;
			amounts=newAmounts;
		}else{
			for(int i=0;i<amounts.length;i++){
				constAmounts[i]=amounts[i];		
			}	
		}*/
		newAmounts= new BigDecimal[amounts.length];
		for(int i=0;i<amounts.length;i++){
			if(to[i]){
				BigDecimal val=AbstractLayer.getProportion(amounts,i,to,true);
				val=val.multiply(maxAmount);
				newAmounts[i]=val;
				constAmounts[i]=val;
			}			
		}		
		amounts=newAmounts;
		/*if(DecimalUtils.compareValues(amountSum,zero,DecimalUtils.MIDDLE_PRECISION)==0){
			return null;
		}else */if(DecimalUtils.compareValues(fromVal,zero,DecimalUtils.MIDDLE_PRECISION)==0){
			return null;
		}else if(DecimalUtils.compareValues(fromVal,maxAmount,DecimalUtils.MIDDLE_PRECISION)>=0){
			//ako je zbroj zamjenjivih plasmana veci od zbroja potreba
			temp= new BigDecimal[from.length];
			for(int i=0;i<from.length;i++){
				if(from[i]){
					//alikvotno procijenjujem koliko cu oduzeti od zamjenjivih plasmana
					BigDecimal val=getElementProportion(i,from,true);
					val=val.multiply(maxAmount);
					//oduzimam alikvotnu vrijednost od zamjenjivog plasmana
					temp[i]=getElement(i).subtract(val);
				}else if(to[i]){
					//dodajem punu potrebnu vrijednost plasmanu
					addValue=getElement(i);
					if(addValue==null){
						addValue=new BigDecimal(0);
					}
					temp[i]=addValue.add(amounts[i]);
					amounts[i]=new BigDecimal(0);
					//amounts[i]=null;
				}else{
					temp[i]=getElement(i);
				}
			}	
			
		}else{
//			ako je zbroj zamjenjivih plasmana manji od zbroja potreba
			//onda ne mogu pokriti sve potrebe pa ih alikvotno pokrivam;kome vise treba vise i dobiva
			temp= new BigDecimal[from.length];
			for(int i=0;i<from.length;i++){
				if(from[i]){
					//oduzimam alikvotnu vrijednost od zamjenjivog plasmana
					temp[i]=new BigDecimal(0);
					//temp[i]=null;
				}else if(to[i]){
//					alikvotno procijenjujem kako cu raspodijeliti potrebe
					BigDecimal val=AbstractLayer.getProportion(constAmounts,i,to,true);
					val=val.multiply(fromVal);
					//dodajem punu potrebnu vrijednost plasmanu
					addValue=getElement(i);
					if(addValue==null){
						addValue=val;
					}else{
						temp[i]=addValue.add(val);
					}	
					amounts[i]=amounts[i].subtract(val);
				}else{
					temp[i]=getElement(i);
				}
			}	
		}
		setElements(temp);
		return amounts;
	}
	
	protected abstract void setElements(BigDecimal[] values);
	
	/**
	 * usporedba kolateral vektora po prioritetu. Ako je o veceg prioriteta od this (tj. o ima manji broj), jednakog , ili manjeg
	 * vraca -1, 0 ili 1
	 * 
	 * @return -1,0,1 ako je this manjeg, jednakog ili veceg prioriteta od o
	 */
	public int compareTo(Object o) {
		CollateralVector cv= (CollateralVector) o;
		if(this.getPriority()>cv.getPriority()){
			return 1;
		}else if(this.getPriority()==cv.getPriority()){
			return 0;
		}else{
			return -1;
		}
	}
	
	/**
	 * @return Returns the openingTS.
	 */
	public Timestamp getOpeningTS() {
		return openingTS;
	}
	/**
	 * @param openingTS The openingTS to set.
	 */
	public void setOpeningTS(Timestamp openingTS) {
		this.openingTS = openingTS;
	}
	/**
	 * @return Returns the processedDate.
	 */
	public Date getProcessedDate() {
		return processedDate;
	}
	/**
	 * @param processedDate The processedDate to set.
	 */
	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}
	protected abstract int getLayerCount();
}
