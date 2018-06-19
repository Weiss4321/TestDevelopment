/*
 * Created on 2006.12.21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CollateralVector {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/CollateralVector.java,v 1.6 2007/02/22 14:57:18 hraamh Exp $";
	/**vraca id kolaterala
	 * 
	 * @return id kolaterala
	 */
	public BigDecimal getId();
	/**
	 * vraca broj elemenata po sloju
	 * 
	 * @return
	 */
	public int getCount();
	/**
	 * vraca flagove svih elemenata
	 * 
	 * @return flagovi svih elemenata unutar slojeva
	 */
	public boolean[] getFlags();
	/**
	 * vraca flag za dani indeks
	 * 
	 * @param index indeks elementa
	 * @return flag za dani indeks
	 */
	public boolean getFlag(int index);
	/**
	 * incijalno postavlja sve flagove ovog vektora na false
	 *
	 */
	public void resetFlags();
	/**
	 * vraca tip kolateral vektora
	 * 
	 * @return tip vektora
	 */
	public int getType();
	/**
	 * ostatak kolateral vektora
	 * @return
	 */
	public BigDecimal getResidue();
	/**
	 * vraca vrijednost vektora
	 * @return vrijednost vektora
	 */
	public BigDecimal getValue();
	/**
	 * dohvat elementa odreðen indeksom. Ako je indeks izvan polja slojeva vraca se null
	 * 
	 * @param index indeks elementa
	 * @return vrijednost elementa
	 */
	public BigDecimal getElement(int index);
	/**
	 * vraca vrijednosti svih elemenata. Ako je element postavljen u vise slojeva vraca njegovu sumu
	 * @return vrijednosti svih elemenata
	 */
	public BigDecimal[] getElements();
	/**
	 * provjera da li je element aktivan (da mu je flag true)
	 * 
	 * @param index indeks elementa
	 * @return
	 */
	public boolean isActive(int index);
	
	//public int[] getActive();
	
	public boolean[] getActive();
	/**
	 * inicijalizira vrijednosti elemenata na null
	 *
	 */
	public void resetValues();
	/**
	 * funkcija namjestavanja raspodjele vrijednosti vektora
	 * 
	 * @param amounts polje vrijednosti koje je potrebno namiriti
	 * @param maxAmount maksimalni iznos fiksanja
	 * @param from oznake elemenata od kojih se moze oduzeti
	 * @param to oznake elemenata kojima se moze dati
	 * @return true ako je izvrsena raspodjela
	 */
	public boolean revalue(BigDecimal amounts[],BigDecimal maxAmount, boolean[] from, boolean[] to);
	/**
	 * raspodjeljuje kolateral vektor po oznacenim plasmanima
	 * 
	 * @param placements sloj plasmana
	 * @return stanje plasmana nakon raspodjele
	 */
	public BigDecimal[] operate(PlacementLayer placements);
	/**
	 * vraca prioritet vektora (likvidnost vektora)
	 * @return prioritet/likvidnost vektora
	 */
	public int getPriority();
	/**
	 * postavlja prioritet vektora (likvidnost vektora)
	 * 
	 * @param priority prioritet/likvidnost vektora
	 */
	public void setPriority(int priority);
	/**
	 * vraca vremenski zapis unosa kolaterala
	 * 
	 * @return vremenski zapis unosa kolaterala
	 */
	public Timestamp getOpeningTS();
	/**
	 * postavlja vremenski zapis unosa kolaterala
	 * @param openingTS vremenski zapis unosa kolaterala
	 */
	public void setOpeningTS(Timestamp openingTS);
	/**
	 * vraca datum potvrde kolateral officera
	 * 
	 * @return datum potvrde kolateral officera
	 */
	public Date getProcessedDate();
	/**
	 * postavlja datum potvrde kolateral officera
	 * 
	 * @param processedDate datum potvrde kolateral officera
	 */
	public void setProcessedDate(Date processedDate);
}
