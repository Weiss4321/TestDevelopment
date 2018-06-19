/*
 * Created on 2006.12.13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Podaci iz COLL_HEAD i LOAN_BENEFICIARY
 * 
 * 
 * @author hraamh
 *
 */
public class CollateralData extends AbstractData implements CollElement{
	
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/CollateralData.java,v 1.12 2008/01/24 12:56:01 hraamh Exp $";
	
	/**
	 * hipoteke vezane za ovaj kolateral
	 */
	private Map mortages=null;
	/**
	 * plasmani vezani za kolateral
	 */
	private Map placements=null;
	/**
	 * izlozenosti po plasmanu
	 */
	private Map placementExposures=null;
	/**
	 * vrsta kolaterala koja se veze direktno na plasmane
	 */
	public final static int SIMPLE=0;
	/**
	 * vrsta kolaterala koja se veze na plasmane preko hipoteka,fiducija...
	 */
	public final static int COMPLEX=1;
	/**
	 * tip kolaterala; SIMPLE ili COMPLEX
	 */
	private int type;
	/**
	 * da li je kolateral vezan preko hipoteka ili direktno na plasman
	 */
	private boolean complex=false;

	/**
	 * Ostatak kolaterala raspoloziv za daljnja pokrica
	 */
	private BigDecimal residue=null;
	/**
	 * Ponder kolaterala
	 */
	private BigDecimal ponder=null;
	/**
	 * datum potvrde kolateral officera
	 */
	protected Date processedDate=null;
	/**
	 * vremenski zapis unosa kolaterala
	 */
	protected Timestamp openingTS=null;
	/** 
	 * rba_eligibility iz coll_head
	 */
	
	
	public CollateralData(BigDecimal id){
		this.id=id;
	}
	/**
	 * konstruktor
	 * 
	 * @param id id kolaterala
	 * @param code sifra kolaterala
	 * @param currencyId id valute u kojoj se vodi kolateral
	 * @param value vrijednost kolaterala
	 * @param ponder ponder kolaterala
	 * @param type tip kolaterala /jednostavni ili slozeni
	 */
	public CollateralData(BigDecimal id,String code, BigDecimal currencyId, BigDecimal value, BigDecimal ponder, int type){
		this.id=id;
		this.code=code;
		this.currencyId=currencyId;
		this.value=value;
		this.ponder=ponder;
		if(type==COMPLEX){
			this.mortages= new HashMap();
			this.complex=true;
		}
		this.placements= new HashMap();
		this.placementExposures= new HashMap();
	}
	/**
	 * vraca vrstu podatka : COLLATERAL
	 */
	public int getDataType(){
		return COLLATERAL;
	}
	/**
	 * @return Ostatak kolaterala
	 */
	public BigDecimal getResidue() {
		return residue;
	}
	
	/**
	 * Vraca ponder
	 * @return ponder
	 */
	public BigDecimal getPonder(){
		return this.ponder;
	}

	/**
	 * Vraca ponderiranu vrijednost kolaterala
	 * @return ponderirana vrijednost
	 */
	public BigDecimal getPondedValue(){
		if((this.value==null)||(this.ponder==null)) return null;
		return value.multiply(ponder).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * @param residue Ostatak kolaterala
	 */
	public void setResidue(BigDecimal residue) {
		this.residue = residue;
	}
	
	/**
	 * Dodaje plasman na kolateral. Ako plasman istog id postoji bazi stari zapis
	 * 
	 * @param placement plasman
	 */
	public void putPlacement(PlacementData placement){
		if (placement!=null)
			this.placements.put(makeHashKey(placement.getId()),placement);
	}
	
	/**
	 * Dodaje hipoteku na kolateral. Ako hipoteka istog id postoji gazi stari zapis
	 * 
	 * @param mortage hipoteka
	 */
	public void putMortgage(MortgageData mortgage){
		if ((mortgage!=null)&&(this.mortages!=null))
			this.mortages.put(makeHashKey(mortgage.getId()),mortgage);
	}
	/**
	 * Dodaje izlozenost po plasmanu na kolateral.
	 * 
	 * @param placementId id plasmana
	 * @param exposure izlozenost
	 */
	public void putPlacementExposures(BigDecimal placementId, BigDecimal exposure){
		if (placementId!=null)
			this.placementExposures.put(makeHashKey(placementId),exposure);
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
	 * Dohvaca hipoteku za id. Ako takva hipoteka ne postoji za dani id, vraca null
	 * 
	 * @param id id hipoteke
	 * @return hipoteka
	 */
	public MortgageData getMortgage(BigDecimal id){
		MortgageData result=null;
		if (mortages!=null){
			result=(MortgageData)this.mortages.get(makeHashKey(id));
		}
		return result;
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
	 * Brise plasman s kolaterala. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 * 
	 * @param id id plasmana
	 */
	public void removePlacement(BigDecimal id){
		this.placements.remove(makeHashKey(id));
	}
	/**
	 * Brise hipoteku s kolaterala. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 * 
	 * @param id id hipoteke
	 */
	public void removeMortgage(BigDecimal id){
		if (mortages!=null){
			this.mortages.remove(makeHashKey(id));
		}
	}
	/**
	 * Brise izlozenost po plasmanu s kolaterala. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 * 
	 * @param id id plasmana
	 */
	public void removePlacementExposures(BigDecimal id){
		this.placementExposures.remove(makeHashKey(id));
	}
	/**
	 * Brise sve plasmane. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 *
	 */
	public void clearPlacements(){
		this.placements.clear();
	}
	/**
	 * brise sve hipoteke. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 *
	 */
	public void clearMortgages(){
		if(this.mortages!=null){
			this.mortages.clear();
		}
	}
	/**
	 * Brise sve izlozenosti po plasmane. PAZI, to ne znaci da je zapis brisan u bazi, samo na modelu izracuna
	 *
	 */
	public void clearPlacementExposures(){
		this.placementExposures.clear();
	}

	/**
	 * Vraca sortirani niz hipoteka.
	 * 
	 * @return sortiranji niz hipoteka
	 */
	public MortgageData[] sortedMortgages(){
		MortgageData[] result=null;
		if((mortages!=null)&&(!mortages.isEmpty())){
			result= new MortgageData[mortages.size()];
			Iterator iter= mortages.values().iterator();
			for(int i=0; iter.hasNext(); i++) {
				result[i]=(MortgageData)iter.next();
			}
			
			for(int i=0; i<result.length; i++) {
				for(int j=i; j<result.length; j++) {
					if(result[i].getPriority()>result[j].getPriority()){
						MortgageData temp=result[i];
						result[i]=result[j];
						result[j]=temp;
					}
				}
			}
		}
		return result;
	}
	
	/**Da li je ovo kompleksni kolateral;tj. da li se veze na 
	 * plasmane preko hipoteka
	 * 
	 * @return true ako se veze na plasmane preko hipoteka
	 */
	public boolean isComplex(){
		return complex;
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
	 * vraca mapu hipoteka
	 * @return mapa hipoteka
	 */
	public Map getMortages() {
		return mortages;
	}
	/**
	 * vraca mapu plasmana
	 * @return mapa plasmana
	 */
	public Map getPlacements() {
		return placements;
	}
	/**
	 * vraca vremenski zapis unosa kolaterala
	 * 
	 * @return vremenski zapis unosa kolaterala
	 */
	public Timestamp getOpeningTS() {
		return openingTS;
	}
	/**
	 * postavlja vremenski zapis unosa kolaterala
	 * @param openingTS vremenski zapis unosa kolaterala
	 */
	public void setOpeningTS(Timestamp openingTS) {
		this.openingTS = openingTS;
	}
	/**
	 * vraca datum potvrde kolateral officera
	 * 
	 * @return datum potvrde kolateral officera
	 */
	public Date getProcessedDate() {
		return processedDate;
	}
	/**
	 * postavlja datum potvrde kolateral officera
	 * 
	 * @param processedDate datum potvrde kolateral officera
	 */
	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}
	
	public String toString(){
		String result="";
		result+="\tid:"+id+"\n";
		result+="\tvalue:"+value+"\n";
		result+="\tpriority:"+this.priority+"\n";
		result+="\ttype:"+this.type+"\n";
		result+="\tcurrencyId:"+this.currencyId+"\n";
		result+="\topeningTS:"+this.openingTS+"\n";
		result+="\tponder:"+this.ponder+"\n";
		result+="\tprocessedDate:"+this.processedDate+"\n";
		return result;
	}
}
