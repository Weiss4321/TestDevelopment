/*
 * Created on 2006.12.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MortgageData extends AbstractData implements CollElement{
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/MortgageData.java,v 1.9 2007/03/14 09:47:29 hraamh Exp $";
	
	public final static int RBA=0;
	
	public final static int OTHER=1;
	
	public final static int RBA_AGREEMENT=2;
	/**
	 * plasmani
	 */
	private Map placements=null;
	/**
	 * izlozenosti po plasmanu
	 */
	private Map placementExposures=null;
	/**
	 * tip hipoteke, rba ili drugih banaka
	 */
	private int type;
	/**
	 * id okvirnog sporazuma ako se hipoteka veze na okvirni sporazum
	 */
	private BigDecimal fraAgrId=null;
	
	/**
	 * konstruktor
	 * 
	 * @param id id hipoteke
	 * @param currencyId id valute u kojoj se vodi hipoteka
	 * @param value vrijednost hipoteke
	 * @param priority prioritet hipoteke
	 * @param type tip hipoteke/ RBA ili strana
	 */
	public MortgageData(BigDecimal id, BigDecimal currencyId, BigDecimal value,int priority,  int type){
		this.id=id;
		this.currencyId=currencyId;
		this.value=value;
		this.type=type;
		this.priority=priority;
		if((type==RBA)||(type==RBA_AGREEMENT)){
			this.placements= new HashMap();
			this.placementExposures= new HashMap();
		}
	}
	
	/**
	 * vraca tip podatka :MORTGAGE
	 */
	public int getDataType() {
		return MORTGAGE;
	}
	/**
	 * Vraca tip hipoteke (OTHER,RBA)
	 * 
	 * @return tip hipoteke
	 */
	public int getMortgageType(){
		return type;
	}

	/**
	 * Dodaje plasman na hipoteku. Ako plasman istog id postoji bazi stari zapis
	 * 
	 * @param placement plasman
	 */
	public void putPlacement(PlacementData placement){
		if ((placement!=null)&&(placements!=null))
			this.placements.put(makeHashKey(placement.getId()),placement);
	}
	/**
	 * Dohvaca plasman za dani id. Ako takav plasman ne postoji za dani id, vraca null
	 * 
	 * @param id id plasmana
	 * @return plasman
	 */
	public PlacementData getPlacement(BigDecimal id){
		PlacementData result=null;
		if (placements!=null){
			result=(PlacementData)this.placements.get(makeHashKey(id));
		}
		return result;
	}
	/**
	 * Brise plasman s hipoteke. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 * 
	 * @param id id plasmana
	 */
	public void removePlacement(BigDecimal id){
		if ((placements!=null)&&(id!=null)){
			this.placements.remove(makeHashKey(id));
		}
	}
	/**
	 * Brise sve plasmane. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 *
	 */
	public void clearPlacements(){
		if (placements!=null){
			this.placements.clear();
		}
	}
	
	/**
	 * 
	 * @param placementId id plasmana
	 * @return true ako postoji; false ako ne postoji plasman danog Id-a na ovoj hipoteci
	 */
	public boolean hasPlacement(BigDecimal placementId){
		return this.placements.containsKey(makeHashKey(placementId));
	}
	/**
	 * vraca mapu plasmana koji se vode na ovoj hipoteci
	 * @return mapa plasmana
	 */
	public Map getPlacements() {
		return placements;
	}
	/**
	 * Dodaje izlozenost po plasmanu na hipoteku/fiduciju.
	 * 
	 * @param placementId id plasmana
	 * @param exposure izlozenost
	 */
	public void putPlacementExposures(BigDecimal placementId, BigDecimal exposure){
		if (placementId!=null)
			this.placementExposures.put(makeHashKey(placementId),exposure);
	}
	/**
	 * Dohvaca izlozenost po plasmanu za dani id. Ako takav plasman ne postoji za dani id, vraca null
	 * 
	 * @param id id plasmana
	 * @return izlozenost po plasmanu
	 */
	public BigDecimal getPlacementExposure(BigDecimal id){
		return (BigDecimal)this.placementExposures.get(makeHashKey(id));
	}	
	/**
	 * Brise izlozenost po plasmanu s hipoteke/fiducije. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 * 
	 * @param id id plasmana
	 */
	public void removePlacementExposures(BigDecimal id){
		this.placementExposures.remove(makeHashKey(id));
	}
	/**
	 * Brise sve izlozenosti po plasmane. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 *
	 */
	public void clearPlacementExposures(){
		this.placementExposures.clear();
	}
	/**
	 * id okvirnog sporazuma.Ako se hipoteka ne veze na OS vraca null
	 * @return id okvirnog sporazuma
	 */
	public BigDecimal getFraAgrId() {
		return fraAgrId;
	}
	/**
	 * id okvirnog sporazuma.Ako se hipoteka ne veze na OS postaviti/ostaviti null
	 * @param id okvirnog sporazuma
	 */
	public void setFraAgrId(BigDecimal fraAgrId) {
		this.fraAgrId = fraAgrId;
	}
}
