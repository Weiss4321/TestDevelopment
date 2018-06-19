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

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleVector extends AbstractVector implements CollateralVector{

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/SimpleVector.java,v 1.9 2007/05/02 14:24:26 hraamh Exp $";
	
	protected Layer layer=null; 
	
	/**
	 * @param count
	 * @param id
	 * @param value
	 */
	public SimpleVector(int count,int priority, BigDecimal id, BigDecimal value) {
		super(count, id, value,priority);
		this.layer=new CollateralLayer(count,null);
		this.lastLayer=0;
	}
	
	public SimpleVector( int priority,CollateralLayer layer, BigDecimal id, BigDecimal value) {
		super(layer.getCount(), id, value,priority);
		this.layer=layer;
		this.lastLayer=0;
	}


	/**
	 * vraca flagove svih elemenata
	 * 
	 * @return flagovi svih elemenata unutar slojeva
	 */
	public boolean[] getFlags() {
		return this.layer.getFlags();
	}

	/**
	 * incijalno postavlja sve flagove ovog vektora na false
	 *
	 */
	public void resetFlags() {
		this.layer.resetFlags();
	}

	/**
	 * ostatak kolateral vektora
	 * @return
	 */
	public BigDecimal getResidue() {
		BigDecimal residue=this.value.subtract(this.layer.getValue());
		if(residue.signum()==-1){
			return new BigDecimal(0);
		}else if(DecimalUtils.compareValues(residue.abs(),new BigDecimal(0),DecimalUtils.MIDDLE_PRECISION)<=0){
			return residue;
		}
		else{
			return residue;
		}
	}

	/**
	 * dohvat elementa odreðen indeksom. Ako je indeks izvan polja slojeva vraca se null
	 * 
	 * @param index indeks elementa
	 * @return vrijednost elementa
	 */
	public BigDecimal getElement(int index) {
		return this.layer.getElement(index);
	}

/*	 (non-Javadoc)
	 * @see hr.vestigo.modules.collateral.common.yoy8.CollateralVector#getElements()
	 
	public BigDecimal[] getElements() {
		return this.layer.getElements();
	}*/

	/**
	 * provjera da li je element aktivan (da mu je flag true)
	 * 
	 * @param index indeks elementa
	 * @return
	 */
	public boolean isActive(int index) {
		return this.layer.isActive(index);
	}
	/**
	 * raspodjeljuje kolateral vektor po oznacenim plasmanima
	 * 
	 * @param placements sloj plasmana
	 * @return stanje plasmana nakon raspodjele
	 */
	public BigDecimal[] operate(PlacementLayer placements) {
		return aliquot(placements,this.layer,this.value);
	}


	/* (non-Javadoc)
	 * @see hr.vestigo.modules.collateral.common.yoy8.CollateralVector#revalue(java.math.BigDecimal, boolean[], boolean[])
	 */
	/*public boolean revalue(BigDecimal[] amounts, boolean[] from, boolean[] to) {
		//radi "and" operaciju tako da se gledaju samo ona polja koja ovaj kolateral pokriva
		from=BooleanUtils.and(this.layer.getFlags(),from);
		to=BooleanUtils.and(this.layer.getFlags(),to);
		//ako nema od koga uzeti ili kome dati nista ne radi
		if((!BooleanUtils.hasTrue(from))||(!BooleanUtils.hasTrue(to))) return false;
		//namijestanje vrijednosti
		BigDecimal[] res=recalibrate(amounts,from,to);
		if(res==null){
			return false;
		}
		else{
			return true;
		}
	}*/
	/**
	 * postavlja vrijednosti u vektor
	 * @param vrijednosti vektora
	 */
	protected void setElements(BigDecimal[] values){
		this.layer.setElements(values);
	}

	/**
	 * vraca SIMPLE_VECTOR
	 */
	public int getType() {
		return SIMPLE_VECTOR;
	}

	/**
	 * Postavlja flag s indeksom index na zadanu vrijednost
	 * 
	 * @param flagIndex indeks flaga
	 * @param flagValue vrijednost flaga
	 */
	public void setFlag(int index, boolean value){
		this.layer.setFlag(index,value);
	}
	
	public void setFlags(boolean[] flags){
		this.layer.setFlags(flags);
	}
	
	public String toString(){
		return "S->VALUE:"+this.value+"\n"+"\t\t\t"+this.layer.toString();
	}
	/**
	 * inicijalizira vrijednosti elemenata na null
	 *
	 */
	public void resetValues() {
		this.layer.resetElements(false);
	}
	protected int getLayerCount() {
		return 1;
	}
}
