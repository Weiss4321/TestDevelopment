/*
 * Created on 2007.03.01
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * podaci iz FRAME_AGREEMENT
 * 
 * @author hraamh
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AgreementData extends AbstractData implements CollElement {

	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/AgreementData.java,v 1.2 2007/03/26 13:42:30 hraamh Exp $";
	
	public AgreementData(BigDecimal id, BigDecimal value, BigDecimal cur_id,String code){
		this.id=id;
		this.value=value;
		this.currencyId=cur_id;
		this.code=code;
		this.placements= new HashMap();
		this.collaterals=new HashMap();
		this.mortgages=new HashMap();
	}
	
	
	/* (non-Javadoc)
	 * @see hr.vestigo.modules.collateral.common.yoy8.AbstractData#getDataType()
	 */
	public int getDataType() {
		return AGREEMENT;
	}	
	/**
	 * plasmani vezani za okvirni sporazum
	 */
	private Map placements=null;
	/**
	 * kolaterali vezani za okvirni sporazum
	 */
	private Map collaterals=null;
	/**
	 * hipoteke na kolateralima vezane za okvirni sporazum. kljuc je id kolaterala!
	 */
	private Map mortgages=null;
	/**
	 * Dodaje plasman na okvirni sporazum. Ako plasman istog id postoji bazi stari zapis se brise
	 * 
	 * @param placement plasman
	 */
	public void putPlacement(PlacementData placement){
		if (placement!=null)
			this.placements.put(makeHashKey(placement.getId()),placement);
	}
	/**
	 * Dodaje kolateral na okvirni sporazum. Ako kolateral istog id postoji bazi stari zapis se brise
	 * 
	 * @param collateralData kolateral
	 */
	public void putCollateral(CollateralData collateralData){
		if (collaterals!=null)
			this.collaterals.put(makeHashKey(collateralData.getId()),collateralData);
	}
	/**
	 * Dodaje hipoteku na okvirni sporazum. 
	 * 
	 * @param collateralId id kolaterala
	 * @param mortgageData hipoteka
	 */
	public void putMortgage(BigDecimal collateralId, MortgageData mortgageData){
		if (mortgages!=null)
			this.mortgages.put(makeHashKey(collateralId),mortgageData);
	}
	/**
	 * Dohvaca plasman za dani id. Ako takav plasman ne postoji za dani id, vraca null
	 * 
	 * @param id id plasmana
	 * @return plasman
	 */
	public PlacementData getPlacement(BigDecimal id){
		return (PlacementData)this.placements.get(makeHashKey(id));
	}
	/**
	 * Dohvaca kolateral za dani id. Ako takav kolateral ne postoji za dani id, vraca null
	 * 
	 * @param id id kolaterala
	 * @return kolateral
	 */
	public CollateralData getCollateral(BigDecimal id){
		return (CollateralData)this.collaterals.get(makeHashKey(id));
	}
	/**
	 * dohvaca hipoteku okvirnog sporazuma za dani kolateral
	 * 
	 * @param collateralId id kolaterala
	 * @return hipoteka OS-a
	 */
	public MortgageData getMortgage(BigDecimal collateralId){
		return (MortgageData)this.mortgages.get(makeHashKey(collateralId));
	}
	/**
	 * Brise plasman s okvirnog sporazuma. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 * 
	 * @param id id plasmana
	 */
	public void removePlacement(BigDecimal id){
		this.placements.remove(makeHashKey(id));
	}
	/**
	 * Brise kolateral(i njegovu hipoteku) s okvirnog sporazuma. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 * 
	 * @param id id kolaterala
	 */
	public void removeCollaterals(BigDecimal id){
		this.collaterals.remove(makeHashKey(id));
		this.mortgages.remove(makeHashKey(id));
	}
	/**
	 * vraca mapu plasmana
	 * @return mapa plasmana
	 */
	public Map getPlacements() {
		return placements;
	}
	/**
	 * vraca mapu kolaterala
	 * @return mapa kolaterala
	 */
	public Map getCollaterals() {
		return collaterals;
	}
	/**
	 * Provjerava da li OS sadrzi plasman za dani id
	 * 
	 * @param id id plasmana
	 * @return true ako vec postoji plasman upisan na OS
	 */
	public boolean containsPlacement(BigDecimal id){
		return this.placements.containsKey(makeHashKey(id));
	}
	/**
	 * Provjerava da li OS sadrzi kolateral za dani id
	 * 
	 * @param id id kolaterala
	 * @return true ako vec postoji kolateral upisan na OS
	 */
	public boolean containsCollateral(BigDecimal id){
		return this.collaterals.containsKey(makeHashKey(id));
	}
	
	public String toString(){
		String result="OS:\tid="+this.id+"\tvalue="+this.value;
		return result;
	}

}
