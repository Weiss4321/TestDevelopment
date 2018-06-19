/*
 * Created on 2006.12.20
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
public interface Layer {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/Layer.java,v 1.5 2007/02/19 10:27:55 hraamh Exp $";
	/**
	 * sumiranje vrijednosti sloja kojima je flag postavljen na true
	 * 
	 * @return
	 */
	public BigDecimal sum();
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
	public BigDecimal sum(boolean[] marked, boolean include);
	/**
	 * dohvat elementa odreðen indeksom. Ako je indeks izvan polja sloja vraca se null
	 * 
	 * @param index indeks elementa
	 * @return vrijednost elementa
	 */
	public BigDecimal getElement(int index);
	/**
	 * vraca polje vrijednosti
	 * 
	 * @return polje vrijednosti
	 */
	public BigDecimal[] getElements();
	/**postavljanje vrijednosti elementa. Ako indeks izlazi iz granica sloja nista se ne radi
	 * 
	 * @param index indeks elementa
	 * @param value vrijednosti elementa
	 */
	public void setElement(int index, BigDecimal value);
	/**
	 * postavljanje vrijednosti flaga. Ako indeks izlazi iz granica sloja nista se ne radi
	 * 
	 * @param index indeks flaga
	 * @param value vrijednosti flaga
	 */
	public void setFlag(int index, boolean value);
	/**inicijalizira polje vrijednosti sloja. ako je withZero onda je inicijalna vrijednost BigDecimal(0),
	 * u suprotnom je null.
	 * 
	 * @param withZero
	 */
	public void resetElements(boolean withZero);
	/**
	 * postalja flase na sve flagove polja
	 *
	 */
	public void resetFlags();
	/**
	 * vraca broj elemenata u sloju
	 * @return
	 */
	public int getCount();
	/**
	 * provjerava da li je flag elementa true
	 * 
	 * @param index indeks elementa
	 * @return vrijednost flaga
	 */
	public boolean isActive(int index);
	
	//public int[] getActives();
	
	/**
	 * vraca proporcionalnost elementa po formuli V(index)/V(1)+...+V(index)+...+V(n)
	 * @param index indeks elementa
	 * @return proporcionalnost elementa
	 */
	public BigDecimal getElementProportion(int index);
	/**
	 * vraca proporcionalnost elementa po formuli V(index)/V(1)+...+V(index)+...+V(n)
	 * za sve elemente oznacene u marked polju (ako je include true) ili za sve elemente neoznacene u marked
	 * polju (ako je include false)
	 * 
	 * @param index indeks elementa
	 * @param marked
	 * @param include
	 * @return proporcionalnost elementa
	 */
	public BigDecimal getElementProportion(int index, boolean[] marked, boolean include);
	/**vraca polje flagova
	 * 
	 * @return polje flagova
	 */
	public boolean[] getFlags();
	/**vraca indeksirani flag
	 * 
	 * @param index indeks
	 * @return indeksirani flag; false ako indeks prelazi granice sloja
	 */
	public boolean getFlag(int index);
	/**postavlja polje flagova ako je istih dimenzija kao postojece polje. ako nije nista ne radi
	 * 
	 * @param values polje flagova
	 * @return true ako je istih dimenzija kao postojece polje; false u suprotnom
	 */
	public boolean setFlags(boolean[] values);
	/**
	 * String reprezentacija sloja
	 * @return
	 */
	public String toString();
	/**
	 * vraca tip sloja
	 * @return tip sloja
	 */
	public int getType();
	/**
	 * vraca vrijednost sloja
	 * @return vrijednost sloja
	 */
	public BigDecimal getValue();
	/**postavlja polje vrijednosti ako je istih dimenzija kao postojece polje. ako nije nista ne radi
	 * 
	 * @param elements polje vrijednosti
	 * @return true ako je istih dimenzija kao postojece polje; false u suprotnom
	 */
	public boolean setElements(BigDecimal[] elements);
	/**
	 * dohvaca id sloja
	 * 
	 * @return id sloja
	 */
	public BigDecimal getId();
}
