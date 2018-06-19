/*
 * Created on 2006.12.27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComplexVector extends AbstractVector implements CollateralVector{

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/ComplexVector.java,v 1.11 2010/04/26 09:28:35 hraamh Exp $";
	
	/**
	 * slojevi hipoteka/fiducija
	 */
	protected Layer[] layers=null;
	
	/**
	 * @param count
	 * @param id
	 * @param value
	 */
	public ComplexVector(int count,int priority, int layerCount, BigDecimal id, BigDecimal value) {
		super(count, id, value,priority);
		this.layers= new Layer[layerCount];
	}

	/**
	 * vraca flagove svih elemenata
	 * 
	 * @return flagovi svih elemenata unutar slojeva
	 */
	public boolean[] getFlags() {
		boolean[] result=new boolean[this.count];
		Arrays.fill(result, false);
		for(int i=0;i<layers.length;i++){
			boolean[] temp=layers[i].getFlags();
			if(temp!=null){
				for(int j=0;j<this.count;j++){
					if(temp[j]){
						result[j]=true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * incijalno postavlja sve flagove ovog vektora na false
	 *
	 */
	public void resetFlags() {
		for(int i=0;i<layers.length;i++){
			layers[i].resetFlags();
		}
	}

	/**
	 * ostatak kolateral vektora
	 * @return
	 */
	public BigDecimal getResidue() {
		BigDecimal allLayers=new BigDecimal(0);
		for(int i=0;i<layers.length;i++){
			allLayers=allLayers.add(layers[i].sum());
		}
		BigDecimal residue=this.value.subtract(allLayers);
		if(residue.signum()==-1){
			return new BigDecimal(0);
		}else{
			return residue;
		}
	}

	/**
	 * dohvat sume vrijednosti indeksiranog elementa kroz sve slojeve. Ako je indeks izvan polja slojeva vraca se null
	 * 
	 * @param index indeks elementa
	 * @return vrijednost elementa
	 */
	public BigDecimal getElement(int index) {
		BigDecimal temp=null;
		for(int i=0;i<this.layers.length;i++){
			BigDecimal val=this.layers[i].getElement(index);
			if(val!=null){
				if(temp==null){
					temp=new BigDecimal(0);
				}
				temp=temp.add(val);
			}
		}
		return temp;
	}
	/**
	 * broj slojeva
	 * @return broj slojeva
	 */
	public int getLayerCount(){
		return this.layers.length;
	}

/*	 (non-Javadoc)
	 * @see hr.vestigo.modules.collateral.common.yoy8.CollateralVector#getElements()
	 
	public BigDecimal[] getElements() {
		BigDecimal[] result=new BigDecimal[this.count];
		for(int i=0;i<this.count;i++){
			result[i]=getElement(i);
		}
		return result;
	}*/
	
	/**
	 * Rasporeduje brojeve u polju na slojeve u kompleksnom vektoru pocevsi od vrsnog sloja.
	 * Postavlja vrijednosti samo na polja s true flagom. Nakon sto prvi put postavi vrijednosti
	 * za dani indeks u visem sloju, svaki iduci put ce postaviti null na zadanu poziciju ako je flag podignut u nizem sloju.
	 * 
	 * @param values vrijednosti
	 */
	protected void setElements(BigDecimal[] values){
		for(int i=0;i<this.layers.length;i++){
			boolean[] f=this.layers[i].getFlags();
			if(f!=null){
				for(int j=0;j<this.count;j++){
					if(f[j]){
						this.layers[i].setElement(j,values[j]);
						values[j]=null;
					}
				}
			}			
		}
	}

	/**
	 * provjera da li je element aktivan (da mu je flag true)
	 * 
	 * @param index indeks elementa
	 * @return
	 */
	public boolean isActive(int index) {
		boolean[] temp=getFlags(); 
		return temp[index];
	}
	/**
	 * dodavanje sloja na indeksirani nivo (ako je indeks manji od max broja koje ovaj kolateral moze imati)
	 * @param index pozicija sloja
	 * @param layer sloj
	 */
	public void setLayer(int index, Layer layer){
		if(index<=this.layers.length){
			layers[index]=layer;
		}
	}
	/**
	 * dohvat sloja s indeksiranog sloja
	 * @param index indeks sloja
	 * @return sloj
	 */
	public Layer getLayer(int index){
		if(index<=this.layers.length){
			 return layers[index];
		}else{
			return null;
		}
	}

	/**
	 * raspodjeljuje kolateral vektor po oznacenim plasmanima
	 * 
	 * @param placements sloj plasmana
	 * @return stanje plasmana nakon raspodjele
	 */
	public BigDecimal[] operate(PlacementLayer placements) {
		BigDecimal residue= DecimalUtils.copy(this.value);
		BigDecimal zero=new BigDecimal(0);
		for(int i=0;i<this.layers.length;i++){
			//ako nema vise nicega za raspodijeliti ne radi nepotrebne korake
			if(DecimalUtils.compareValues(residue,zero,DecimalUtils.MIDDLE_PRECISION)<=0){
				Layer l= this.layers[i];
				if(l.getType()!=AbstractLayer.AGREEMENT_LAYER){
					this.lastLayer=i-1;
					break;
				}			
			}
			Layer l= this.layers[i];
			if(l.getType()==AbstractLayer.OTHER_LAYER){
				//za strane hipoteke/fiducije samo oduzima njihove vrijednosti prioritetno
				residue=residue.subtract(this.layers[i].getValue());
				if(residue.compareTo(zero)<0){
					this.minus=residue.abs();
					this.lastLayer=i;
				}
			}else if((l.getType()==AbstractLayer.RBA_LAYER)){
				//za RBA hipoteke/fiducije radi raspodjelu plasmana
				BigDecimal sub=placements.sum();
                // alikvotni izracun za hipoteku
				aliquot(placements,l,residue);
				sub=sub.subtract(placements.sum());
				residue=residue.subtract(sub);
				this.lastLayer=i;
			}else if((l.getType()==AbstractLayer.AGREEMENT_LAYER)){
				this.lastLayer=i;
			}
		}
		return null;
	}
	

	/* (non-Javadoc)
	 * @see hr.vestigo.modules.collateral.common.yoy8.CollateralVector#revalue(java.math.BigDecimal, boolean[], boolean[])
	 */
	/*public boolean revalue(BigDecimal[] amounts, boolean[] from, boolean[] to) {
		boolean result=false;
		//provjera da li ovaj kolateral uopce pokriva plasmane koji se trebaju ispremijesati
		boolean[] flagsFromAll=this.getFlags();
		boolean[] flagsToAll=this.getFlags();
		BigDecimal zero=new BigDecimal(0);
		flagsFromAll=BooleanUtils.and(flagsFromAll,from);
		flagsToAll=BooleanUtils.and(flagsToAll,to);
		//ako nema od koga uzeti ili kome dati nista ne radi
		if((!BooleanUtils.hasTrue(flagsFromAll))||(!BooleanUtils.hasTrue(flagsToAll))) return false;
		BigDecimal[] recalibrated=recalibrate(amounts,from,to);
		if (recalibrated!=null){
			//ako je izvrseno namijestanje
			amounts=recalibrated;
			result=true;
		}
		return result;
		for(int i=this.layers.length-1;i>=0;i--){
			//boolean[] flagsFrom=BooleanUtils.and(this.layers[i].getFlags(),from);
			//boolean[] flagsTo=BooleanUtils.and(this.layers[i].getFlags(),to);
			//ako nema od koga uzeti ili kome dati nista ne radi
			//if((!BooleanUtils.hasTrue(flagsFrom))||(!BooleanUtils.hasTrue(flagsTo))) return false;
			//namijestanje vrijednosti
			BigDecimal[] recalibrated=recalibrate(this.layers[i],amounts,from,to);
			if (recalibrated!=null){
				//ako je izvrseno namijestanje
				amounts=recalibrated;
				result=true;
				//ako je sav manjak zadovoljen ne iterira dalje kroz slojeve
				if(DecimalUtils.compareValues(AbstractLayer.sum(amounts,to,true),zero,DecimalUtils.LOW_PRECISION)==0){
					break;
				}
			}
		}		
		return result;
	}
*/
	/**
	 * vraca COMPLEX_VECTOR
	 */
	public int getType() {
		return COMPLEX_VECTOR;
	}
	
	
	public String toString(){
		String s="C->VALUE:"+this.value+"\n";
		for(int i=0;i<this.layers.length;i++){
			if(this.layers[i].getType()==AbstractLayer.RBA_LAYER) s+="\t\t\t";
			s+=this.layers[i].toString()+"\n";
		}
		return s;
	}
	/**
	 * inicijalizira vrijednosti elemenata na null
	 *
	 */
	public void resetValues() {
		for(int i=0;i<this.layers.length;i++){
			layers[i].resetElements(false);
		}
	}
}
