/*
 * Created on 2006.12.13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.modules.collateral.common.interfaces.DealCollateralCoverage;
import hr.vestigo.modules.rba.util.DecimalUtils;

/**
 * Common za izracun pokrivenosti povezane grupe plasmana pripadnim kolateralima
 * 
 * @author hraamh
 *
 */
public class YOY80 implements DealCollateralCoverage{
	
	public static String cvsident =	"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/YOY80.java,v 1.35 2012/01/18 14:50:59 hrakis Exp $";

	private RemoteContext rc;
	private YOY81 yoy81;
	private Map exchangeRate=null;
	private Date workingDate=null;
	private BigDecimal colProc=null;
	private BigDecimal use_id=null;
	private BigDecimal zero=null;
	private BigDecimal zeroAmount=null;
	
	private BigDecimal roundError=null;
	private boolean ponded=true;
	
	private String colProcStatusOK="0";
	
	protected boolean debug=true;
    
    protected boolean testcase=false; 
	
	
	/**
	 * 
	 */
	public YOY80(RemoteContext context, Date date, String exp_type_ind, BigDecimal col_proc, BigDecimal use_id,boolean debugOut) throws Exception{
		this.rc=context;
		init(date, exp_type_ind, col_proc, use_id, debugOut);
		/*this.debug=debugOut;		
		this.yoy81=new YOY81(context,debug,date);
		this.exchangeRate= new HashMap();
		this.workingDate=date;
		this.colProc=col_proc;
		this.use_id=use_id;
		this.roundError=new BigDecimal("0.05");
		this.zero=new BigDecimal(0);
		this.zeroAmount=new BigDecimal("0.00");
		*/
	}
	
	public YOY80(){

	}
    
    public void useTestcaseCalculation(){
        testcase=true;
        debug=true;
    }
	
	public void setContext(RemoteContext context) throws Exception{
		this.rc=context;
	}
	
	/**
	 * inicijaliziranje commona startnim podacima
	 * 
	 * @param date datum valute
	 * @param col_proc id obrade
	 * @param use_id user id
	 * @param debugOut ispis debug teksta
	 */
	public void init(Date date, String exp_type_ind, BigDecimal col_proc, BigDecimal use_id,boolean debugOut) throws Exception{
		this.debug=debugOut;
		this.exchangeRate= new HashMap();
		this.roundError=new BigDecimal("0.05");
		this.yoy81=new YOY81(rc,debug,date,exp_type_ind);
		this.workingDate=date;
		this.colProc=col_proc;
		this.use_id=use_id;
		this.zero=new BigDecimal(0);
		this.zeroAmount=new BigDecimal("0.00");
	}
	
	private void hardcoding(){
		this.exchangeRate.put(makeHashKey(new BigDecimal("63999")),new BigDecimal(1));
	}
	
	/**
	 * 
	 * @param placementId
	 * @param ponded da li se vrijednost kolaterala ponderira u izracunu
	 * @param eligibility prihvatljivost; vrijednosti su u statickim poljima interface-a
	 * @throws Exception
	 */
	public int execute(BigDecimal placementId, boolean ponded, int eligibility) throws Exception{
		this.ponded=ponded;
		if (debug) System.out.println("\nYOY80->dohvat podataka o plasmanu");
		PlacementData first= this.yoy81.getPlacementDataNew(placementId);
		if(first==null){
			
			return 1;
		}else if(DecimalUtils.compareValues(first.getValue(), zero, DecimalUtils.COMPARE_PRECISION)==0){
            if(testcase){
                if (debug) System.out.println("Testcase: returning status 4- no calculation");
            }else{
                this.yoy81.insertCusaccExpColl(first.getCustomerId(),first.getId(),null,this.colProc,zeroAmount,null,zeroAmount,zeroAmount,this.workingDate,null,zeroAmount,null,first.getCurrencyId(),first.getValue(),zeroAmount,zeroAmount,zeroAmount,first.getExposureBalLcy(),first.getExpOffBalLcy(), ""+eligibility); 
            }
			return 4;
		}
		if (debug) System.out.println("\nYOY80-> podaci o plasmanu "+placementId+" dohvaceni");
		if (debug) System.out.println("\nYOY80-> dohvat domene");
		Map[] scope=getDomain(first,eligibility);
		if((scope==null)||(scope.length<3)) throw new Exception("YOY8 execute: failed to get data scope!");
		if (debug) System.out.println("\nYOY80->domena dohvacena");
		Map collaterals= scope[0];
		if (debug) System.out.println("\nYOY80->execute collaterali "+collaterals.size());
		Map placements= scope[1];
		if (debug) System.out.println("\nYOY80->execute plasmani "+placements.size());
		Map agreements=scope[2];
		if (debug) System.out.println("\nYOY80->execute okvirni sporazumi "+agreements.size());
		if(collaterals.size()==0){
			//ako se plasman ne veze niti za jedan aktivni kolateral 
			//ubacujem u tablicu cusacc_exp_coll vrijednosti s nulama
            if(testcase){
                if (debug) System.out.println("Testcase: returning status 2- no calculation");
            }else{
                BigDecimal plKnValue=DecimalUtils.scale(exchange(first.getCurrencyId(),first.getValue(),true),2);
                this.yoy81.insertCusaccExpColl(first.getCustomerId(),first.getId(),null,this.colProc,zeroAmount,null,zeroAmount,zeroAmount,this.workingDate,null,zeroAmount,null,first.getCurrencyId(),first.getValue(),plKnValue,zeroAmount,zeroAmount,first.getExposureBalLcy(),first.getExpOffBalLcy(), ""+eligibility);
            }
			return 2;
		}else if(placements.size()==0){
            if(testcase){
                if (debug) System.out.println("Testcase: returning status 3- no calculation");
            }
			return 3;
		}
		
		Collection coll_data=collaterals.values();
		Collection plac_data=placements.values();
		Collection agr_data=agreements.values();
		
		if (debug) System.out.println("\nYOY80->postoje podaci o plasmanima domene");
		BigDecimal[] placementIdsOrder= new BigDecimal[plac_data.size()]; 
		if (debug) System.out.println("\nYOY80->kreiram placement layer");
		PlacementLayer placementLayer= ModelFactory.getPlacementLayer(plac_data.size());
		
		//prebacivanje 
		Iterator iter = plac_data.iterator();
		for(int i=0; iter.hasNext(); i++) {
			PlacementData temp=(PlacementData)iter.next();
			placementIdsOrder[i]=temp.getId();
			//preracunavanje u kune
			BigDecimal knValue=exchange(temp.getCurrencyId(),temp.getValue(),true);
			placementLayer.setElement(i,knValue);
		}
		if (debug) System.out.println("\nYOY80->placement layer kreiran");
		if (debug) System.out.println("\nYOY80->placement layer:\n\n"+placementLayer);
		if (debug) System.out.println("\nYOY80->kreiram collateral vektore");
		CollateralVector[] collateralVectors=buildCollateralVectors(placementIdsOrder,coll_data,placementLayer.getCount());
		AgreementVector[] agreementVectors=null;
		if(agr_data.size()>0){
			agreementVectors=buildAgreementVectors(placementIdsOrder,agr_data,placementLayer.getCount());
		}
		//dohvat modela izracuna
		if (debug) System.out.println("\nYOY80-> kreiram model izracuna");
		PlacementCoverage coverage= ModelFactory.getCoverageModel(placementLayer,collateralVectors,agreementVectors,this.debug);
		//racunanje pokrivenosti
		if (debug) System.out.println("\nYOY80->pokrecem model "+coverage.getModelName());
		collateralVectors=coverage.run();
		//collateralVectors=coverage.getCollateralVectors();
		if (debug) System.out.println("\nYOY80-> izracun zavrsen");
		
		//nastimavanje okvirnih sporazuma
		if(agr_data.size()>0){
			ModelAgreement ma=(ModelAgreement)coverage;
			collaterals=recalculateParts(collateralVectors,collaterals,placements,agreements,placementIdsOrder,ma.getAgreementValues(),ma.getAgreementIndicators());
		}else{
			collaterals=recalculateParts(collateralVectors,collaterals,placements,null,placementIdsOrder,null,null);
		}
        if(testcase){
            if (debug) System.out.println("Testcase: executed calculation");
        }else{
    		if (debug) System.out.println("\nYOY80-> upis izracunatih podataka u bazu");
    		writeDown(collaterals,eligibility);
    		writeColProcGar(placementIdsOrder);
    		if (debug) System.out.println("\nYOY80->upis izracuna zavrsen");
        }
		return 0;
	}
	
	/**
    *  Gradi se vektor koji sadrzi plasmana i hipoteke. po jedan za svaki kolateral. on direktno vrsi prioritetnu i 
    *  alikvotnu raspodjelu
     * 
     * @param placementIds
     * @param collaterals
     * @param placemetCount
     * @return
	 */
	private CollateralVector[] buildCollateralVectors(BigDecimal[] placementIds, Collection collaterals,  int placemetCount){
		if (debug) System.out.println("\nYOY80->buildCollateralVectors...");
		if((placementIds==null)||(collaterals==null)) return null;
		//za sad se izostavljaju agreements		
		//CollateralVector[] result=new CollateralVector[collaterals.size()];
		CollateralVector[] result=null;
		Vector resultVector=new Vector();
		boolean hasPlacements=false;
		
		Iterator iter= collaterals.iterator();
		
		for(int i=0; iter.hasNext(); i++) {
			CollateralData tempCollateral=(CollateralData)iter.next();
			if (debug) System.out.println("\nYOY80->buildCollateralVectors vrtim id: "+tempCollateral.getId());
			//preracunavanje vrijednosti kolaterala u kune
			if (debug) System.out.println("\nYOY80->buildCollateralVectors cur id: "+tempCollateral.getCurrencyId());
			BigDecimal knValue;
			if(this.ponded){
				knValue=exchange(tempCollateral.getCurrencyId(),tempCollateral.getPondedValue(),true);
			}else{
				knValue=exchange(tempCollateral.getCurrencyId(),tempCollateral.getValue(),true);
			}
			if(tempCollateral.isComplex()){
				hasPlacements=false;
				if (debug) System.out.println("\nYOY80->kreiram kompleksan vektor id: "+tempCollateral.getId());
				//kreiranje kompleksnog kolateralnog vektora- veze se na plasmane preko hipoteka
				if (debug) System.out.println("\nYOY80->sortiram hipoteke za col_id: "+tempCollateral.getId());
				MortgageData[] mortgages=tempCollateral.sortedMortgages();
				if (debug) System.out.println("\nYOY80->hipoteke sortirane");
				if(mortgages!=null){
					ComplexVector complexVector=(ComplexVector) ModelFactory.getCollateralVector(tempCollateral.getId(),knValue,tempCollateral.getPriority(), AbstractVector.COMPLEX_VECTOR,placemetCount,mortgages.length);
					complexVector.setOpeningTS(tempCollateral.getOpeningTS());
					complexVector.setProcessedDate(tempCollateral.getProcessedDate());
					Layer layer=null;
					for(int j=0;j<mortgages.length;j++){
						MortgageData tempMortgage= mortgages[j];
						if(tempMortgage.getMortgageType()==MortgageData.OTHER){
							//kreiranje virtualnog sloja hipoteke drugih banaka
							knValue=exchange(tempMortgage.getCurrencyId(),tempMortgage.getValue(),true);
							layer= ModelFactory.getLayer(mortgages[j].getId(),placemetCount,AbstractLayer.OTHER_LAYER,knValue);
						}else if(mortgages[j].getMortgageType()==MortgageData.RBA){			
							layer=ModelFactory.getLayer(tempMortgage.getId(),placemetCount,AbstractLayer.RBA_LAYER,null);
							for(int k=0;k<placementIds.length;k++){
								//oznacavanje onih plasmana koji su na RBA hipoteci
								if(tempMortgage.hasPlacement(placementIds[k])){
									layer.setFlag(k,true);
								}
							}
						}else if(mortgages[j].getMortgageType()==MortgageData.RBA_AGREEMENT){
							layer= ModelFactory.getLayer(mortgages[j].getId(),placemetCount,AbstractLayer.AGREEMENT_LAYER,null);
						}
						complexVector.setLayer(j,layer);
					}
					//result[i]=complexVector;
					resultVector.add(complexVector);
				}
			}else{
				if (debug) System.out.println("\nYOY80-> kreiram jednostavan vektor id: "+tempCollateral.getId());
				SimpleVector simpleVector= (SimpleVector)ModelFactory.getCollateralVector(tempCollateral.getId(),knValue,tempCollateral.getPriority(), AbstractVector.SIMPLE_VECTOR,placemetCount,1);
				simpleVector.setOpeningTS(tempCollateral.getOpeningTS());
				simpleVector.setProcessedDate(tempCollateral.getProcessedDate());
				for(int k=0;k<placementIds.length;k++){
					//oznacavanje onih plasmana koji su na RBA hipoteci
					if(tempCollateral.hasPlacement(placementIds[k])){
						simpleVector.setFlag(k,true);
					}
				}
				//result[i]=simpleVector;
				resultVector.add(simpleVector);
			}	
		}	
		
		result= new CollateralVector[resultVector.size()];
		for(int i=0;i<resultVector.size();i++){
			result[i]=(CollateralVector) resultVector.get(i);
		}
		
		if (debug) System.out.println("\nYOY80->kraj buildCollateralVectors");
		return result;
	}
	
    /**
     * vektor za okvirni sporazum. radi isto kao i vektor kolaterala, jedino mu vrijednost nije fiksna nego ovisi o ostatku kolaterala na koji je vezan
     * 
     * @param placementIds
     * @param agreements
     * @param placemetCount
     * @return
     */
	private AgreementVector[] buildAgreementVectors(BigDecimal[] placementIds, Collection agreements,  int placemetCount){
		if (debug) System.out.println("\nYOY80->buildCollateralVectors...");
		if((placementIds==null)||(agreements==null)) return null;
		//za sad se izostavljaju agreements		
		AgreementVector[] result=new AgreementVector[agreements.size()];
		Iterator iter= agreements.iterator();
		
		for(int i=0; iter.hasNext(); i++) {
			AgreementData tempAgreement=(AgreementData)iter.next();
			if (debug) System.out.println("\nYOY80->buildAgreementVectors vrtim id: "+tempAgreement.getId());
			//preracunavanje vrijednosti kolaterala u kune
			if (debug) System.out.println("\nYOY80->buildAgreementVectors cur id: "+tempAgreement.getCurrencyId());
			BigDecimal knValue=exchange(tempAgreement.getCurrencyId(),tempAgreement.getValue(),true);
			
			Collection colls= tempAgreement.getCollaterals().values();
			BigDecimal[] collIds=new BigDecimal[colls.size()];
			Iterator coll_iter=colls.iterator();
			int j=0;
			while(coll_iter.hasNext()){
				CollateralData tempColl=(CollateralData) coll_iter.next();
				collIds[j]=tempColl.getId();
				j++;
			}
			
			AgreementVector agreementVector= (AgreementVector)ModelFactory.getAgreementVector(tempAgreement.getId(),knValue,collIds, placemetCount);

			for(int k=0;k<placementIds.length;k++){
				//oznacavanje onih plasmana koji su na RBA hipoteci
				if(tempAgreement.containsPlacement(placementIds[k])){
					agreementVector.setFlag(k,true);
				}
			}
			result[i]=agreementVector;
		}	
		
		
		
		if (debug) System.out.println("\nYOY80->kraj buildAgreementVectors");
		return result;
	}
	
	private BigDecimal exchange(BigDecimal cur_id,BigDecimal value, boolean toKn){
		if (debug) System.out.println("\nYOY80->exchange");
		if((value==null)||(cur_id==null)) return null;
		BigDecimal result=null;		
		BigDecimal rate=(BigDecimal)this.exchangeRate.get(makeHashKey(cur_id));
		if(rate==null){
			rate=yoy81.selectMiddRate(cur_id,this.workingDate);
			this.exchangeRate.put(makeHashKey(cur_id),rate);
		}
		
		if(toKn){
			result=value.multiply(rate);
		}else{
			result=value.divide(rate,BigDecimal.ROUND_HALF_EVEN);
		}
		if (debug) System.out.println("\nYOY80->kraj exchange");
		return result;
		
	}
	
	private Map recalculateParts(CollateralVector[] collateralVectors, Map collateralDatas,Map placementDatas, Map agreementDatas,BigDecimal[] placementIds, BigDecimal[][] agreementValues, boolean[][] agreementIndicators){
		if (debug) System.out.println("\nYOY80->recalculate parts");
		if((collateralVectors==null)||(collateralDatas==null)||(placementIds==null)) return null;
		//BigDecimal zero= new BigDecimal(0);
		for(int i=0;i<collateralVectors.length;i++){
			CollateralVector vector= collateralVectors[i];
			if (debug) System.out.println("\nYOY80->recalculate parts vector "+vector.getId());
			if((vector.getType()==AbstractVector.SIMPLE_VECTOR)||(vector.getType()==AbstractVector.COMPLEX_VECTOR)){
				CollateralData data=(CollateralData)collateralDatas.get(makeHashKey(vector.getId()));
				if(data!=null){	
					data.clearPlacementExposures();
					for(int k=0;k<placementIds.length;k++){
						if (debug) System.out.println("\nYOY80->recalculate parts k="+k);
						if(vector.getType()==AbstractVector.SIMPLE_VECTOR){
							
							if(vector.getFlag(k)){
								if (debug) System.out.println("\nYOY80->recalculate parts flag je true");
								//PlacementData p_data=(PlacementData)placementDatas.get(makeHashKey(placementIds[k]));
								if(placementDatas.containsKey(makeHashKey(placementIds[k]))){
									if (debug) System.out.println("\nYOY80->recalculate parts");
									BigDecimal pla_cur_val= vector.getElement(k);
									if (debug) System.out.println("\nYOY80->recalculate parts\n K="+vector.getId()+" P="+placementIds[k]+" V="+pla_cur_val);
									if(pla_cur_val!=null){
										//pla_cur_val= DecimalUtils.scale(pla_cur_val,DecimalUtils.LOW_PRECISION);
									}else{
										pla_cur_val=new BigDecimal("0.00");
									}
									data.putPlacementExposures(placementIds[k],pla_cur_val);
									if (debug) System.out.println("\nYOY80->recalculate parts ");
								}				
							}
						} else if(vector.getType()==AbstractVector.COMPLEX_VECTOR){
							
							ComplexVector c_vectore= (ComplexVector) vector;
							for(int m=0;m<c_vectore.getLayerCount();m++){
								Layer l=c_vectore.getLayer(m);
								if(l.getType()==AbstractLayer.RBA_LAYER){
									MortgageData md=data.getMortgage(l.getId());
									if ((md!=null)&&(l.getFlag(k))&&(placementDatas.containsKey(makeHashKey(placementIds[k])))){
										BigDecimal pla_cur_val= l.getElement(k);
										if (debug)System.out.println("\nYOY80->recalculate parts\n V="+c_vectore.getId()+" M="+l.getId()+" P="+placementIds[k]+" V="+pla_cur_val);
										if(pla_cur_val!=null){
											//pla_cur_val= DecimalUtils.scale(pla_cur_val,DecimalUtils.LOW_PRECISION);
										}else{
											pla_cur_val=new BigDecimal("0.00");
										}
										md.putPlacementExposures(placementIds[k],pla_cur_val);
									}
								}
							}
						}
					}
				}				
			}else if(vector.getType()==AbstractVector.AGREEMENT_VECTOR){
				AgreementData tempAgreement=(AgreementData) agreementDatas.get(makeHashKey(vector.getId()));
				for(int k=0;k<placementIds.length;k++){
					if(vector.getFlag(k)){
						for(int agr_col=0;agr_col<agreementIndicators.length;agr_col++){
							if(agreementIndicators[i][agr_col]){
								//ako je kolateral ukljucen u sporazum
								
								MortgageData md= tempAgreement.getMortgage(collateralVectors[agr_col].getId());
								if (debug)System.out.println("\nYOY80->dohvat:\n OS:"+vector.getId()+" na kolateralu:"+collateralVectors[agr_col].getId()+" hipoteka:"+md);
								if ((md!=null)&&(placementDatas.containsKey(makeHashKey(placementIds[k])))){
									BigDecimal pla_val= vector.getElement(k);
									if (debug)System.out.println("\nYOY80->NASAO!:\n OS:"+vector.getId()+" na kolateralu:"+collateralVectors[agr_col].getId()+" hipoteka:"+md.getId());
									
									BigDecimal pla_cur_val=null;
									//provjera da li OS ima ikakvu vrijednost da se ne bi dijelilo s 0
									if(vector.getValue().compareTo(zero)!=0){
										BigDecimal ratio=agreementValues[i][agr_col].divide(vector.getValue(),BigDecimal.ROUND_HALF_EVEN);
										pla_cur_val=ratio.multiply(pla_val);
									}
									if (debug)System.out.println("\nYOY80->recalculate parts\n V="+tempAgreement.getId()+" M="+md.getId()+" P="+placementIds[k]+" V="+pla_cur_val);
									if(pla_cur_val!=null){
										//pla_cur_val= DecimalUtils.scale(pla_cur_val,DecimalUtils.LOW_PRECISION);
									}else{
										pla_cur_val=new BigDecimal("0.00");
									}
									md.putPlacementExposures(placementIds[k],pla_cur_val);
								}
							}
						}
					}
				}
			}
		}
		if (debug) System.out.println("\nYOY80->kraj recalculate parts");
		return collateralDatas;
	}
	
	private void writeColProcGar(BigDecimal[] placementIds) throws Exception{
		if(placementIds==null) throw new Exception("YOY8 writeColProcGar: null input");
		for(int i=0;i<placementIds.length;i++){
			yoy81.insertColProcGar(placementIds[i],this.colProcStatusOK,this.colProc);
		}
	}
	
	private void writeDown(Map collateralDatas,int eligibility) throws Exception{
		if (debug) System.out.println("\nYOY80->writeDown");
		if (debug) System.out.println("\nYOY80->writeDown mapsize: "+collateralDatas.size());
		if(collateralDatas==null) throw new Exception("YOY8 writeDown: null input");
		Map placementSums= new HashMap();
		Iterator coll_iter=collateralDatas.values().iterator();
		while(coll_iter.hasNext()){
			
			//iterira sve kolaterale
			CollateralData tempC=(CollateralData)coll_iter.next();
			if (debug) System.out.println("\nYOY80->writeDown obradujem coll "+tempC.getId());
			//BigDecimal coll_hf_prior_id=null;
			//ako se kolateral veze na plasman preko hipoteke
			if(tempC.isComplex()){
				//iterira sve hipoteke na kolateralu
				Map mortgages= tempC.getMortages();
				Iterator mort_iter=mortgages.values().iterator();
				while(mort_iter.hasNext()){
					MortgageData tempM=(MortgageData)mort_iter.next();
					//ako je RBA hipoteka
					if(tempM.getMortgageType()==MortgageData.RBA){
						//coll_hf_prior_id=tempM.getId();
						Iterator plac_iter= tempM.getPlacements().values().iterator();
						while(plac_iter.hasNext()){
							PlacementData tempP=(PlacementData) plac_iter.next();
							//PROMJENA ZBOG FIKSANJA LIPA
							BigDecimal knValue=DecimalUtils.scale(tempM.getPlacementExposure(tempP.getId()),2);
							BigDecimal plKnValue=DecimalUtils.scale(exchange(tempP.getCurrencyId(),tempP.getValue(),true),2);
							BigDecimal plValue=DecimalUtils.scale(tempP.getValue(),2);
							
							BigDecimal placementSum=(BigDecimal) placementSums.get(tempP.getId());
							if(placementSum==null){
								placementSum=new BigDecimal(0);
							}
							if(fullValue(placementSum,knValue,plKnValue)){
								knValue=plKnValue.subtract(placementSum);
							}
							placementSums.put(tempP.getId(),placementSum.add(knValue));
							if (debug) System.out.println("\nYOY80->writeDown vrijednost plasmana "+tempP.getId()+" je: "+knValue);
							BigDecimal collCurValue=DecimalUtils.scale(exchange(tempC.getCurrencyId(),knValue,false),2);
							BigDecimal plCurValue=DecimalUtils.scale(exchange(tempP.getCurrencyId(),knValue,false),2);
							
							BigDecimal percentage=DecimalUtils.scale(knValue.divide(plKnValue,5,BigDecimal.ROUND_HALF_UP).movePointRight(2),2);

							
							this.yoy81.insertCusaccExpColl(tempP.getCustomerId(),tempP.getId(),tempC.getId(),this.colProc,knValue,tempC.getCurrencyId(),plCurValue,collCurValue,this.workingDate,
									tempM.getId(),percentage,null,tempP.getCurrencyId(),plValue,plKnValue,tempC.getValue(),tempC.getPonder(), 
									tempP.getExposureBalLcy(),tempP.getExpOffBalLcy(),""+eligibility);
						}
					}else if(tempM.getMortgageType()==MortgageData.RBA_AGREEMENT){
						//coll_hf_prior_id=tempM.getId();
						Iterator plac_iter= tempM.getPlacements().values().iterator();
						while(plac_iter.hasNext()){
							PlacementData tempP=(PlacementData) plac_iter.next();						
							//PROMJENA ZBOG FIKSANJA LIPA
							BigDecimal knValue=DecimalUtils.scale(tempM.getPlacementExposure(tempP.getId()),2);
							BigDecimal plKnValue=DecimalUtils.scale(exchange(tempP.getCurrencyId(),tempP.getValue(),true),2);
							BigDecimal plValue=DecimalUtils.scale(tempP.getValue(),2);
							
							BigDecimal placementSum=(BigDecimal) placementSums.get(tempP.getId());
							if(placementSum==null){
								placementSum=new BigDecimal(0);
							}
							if(fullValue(placementSum,knValue,plKnValue)){
								knValue=plKnValue.subtract(placementSum);
							}
							placementSums.put(tempP.getId(),placementSum.add(knValue));
							if (debug) System.out.println("\nYOY80->writeDown vrijednost plasmana "+tempP.getId()+" je: "+knValue);
							BigDecimal collCurValue=DecimalUtils.scale(exchange(tempC.getCurrencyId(),knValue,false),2);
							BigDecimal plCurValue=DecimalUtils.scale(exchange(tempP.getCurrencyId(),knValue,false),2);
							
							BigDecimal percentage=DecimalUtils.scale(knValue.divide(plKnValue,5,BigDecimal.ROUND_HALF_UP).movePointRight(2),2);

							
							this.yoy81.insertCusaccExpColl(tempP.getCustomerId(),tempP.getId(),tempC.getId(),this.colProc,knValue,tempC.getCurrencyId(),plCurValue,collCurValue,this.workingDate,
									tempM.getId(),percentage,tempM.getFraAgrId(),tempP.getCurrencyId(),plValue,plKnValue,tempC.getValue(),tempC.getPonder(),
									tempP.getExposureBalLcy(),tempP.getExpOffBalLcy(),""+eligibility);
						}
					}
				}
			}else{
				Iterator plac_iter= tempC.getPlacements().values().iterator();
				while(plac_iter.hasNext()){
					PlacementData tempP=(PlacementData) plac_iter.next();
					//PROMJENA ZBOG FIKSANJA LIPA
					BigDecimal knValue=DecimalUtils.scale(tempC.getPlacementExposure(tempP.getId()),2);
					BigDecimal plKnValue=DecimalUtils.scale(exchange(tempP.getCurrencyId(),tempP.getValue(),true),2);
					BigDecimal plValue=DecimalUtils.scale(tempP.getValue(),2);
					
					BigDecimal placementSum=(BigDecimal) placementSums.get(tempP.getId());
					if(placementSum==null){
						placementSum=new BigDecimal(0);
					}
					if(fullValue(placementSum,knValue,plKnValue)){
						knValue=plKnValue.subtract(placementSum);
					}
					placementSums.put(tempP.getId(),placementSum.add(knValue));
					if (debug) System.out.println("\nYOY80->writeDown vrijednost plasmana "+tempP.getId()+" je: "+knValue);
					BigDecimal collCurValue=DecimalUtils.scale(exchange(tempC.getCurrencyId(),knValue,false),2);
					BigDecimal plCurValue=DecimalUtils.scale(exchange(tempP.getCurrencyId(),knValue,false),2);
					
					BigDecimal percentage=DecimalUtils.scale(knValue.divide(plKnValue,5,BigDecimal.ROUND_HALF_UP).movePointRight(2),2);

					
					this.yoy81.insertCusaccExpColl(tempP.getCustomerId(),tempP.getId(),tempC.getId(),this.colProc,knValue,tempC.getCurrencyId(),plCurValue,collCurValue,this.workingDate,
							null,percentage,null,tempP.getCurrencyId(),plValue,plKnValue,tempC.getValue(),tempC.getPonder(),
							tempP.getExposureBalLcy(),tempP.getExpOffBalLcy(),""+eligibility);
				}
			}
		}
		if (debug) System.out.println("\nYOY80->kraj writeDown");
	}
    
    private void testcaseOutput(){
        
        
    }
	
	private Map[] getDomain(CollElement input, int eligibility) throws Exception{
		if (debug) System.out.println("\nYOY80->getDomain");
		if(input==null) return null;
		HashMap collaterals= new HashMap();
		HashMap placements= new HashMap();
		HashMap temporary= new HashMap();
		yoy81.clearAgreements();
		if(input.getDataType()==AbstractData.COLLATERAL){
			collaterals.put(makeHashKey(input.getId()),input);
			temporary.put(makeHashKey(input.getId()),input);
		}else if(input.getDataType()==AbstractData.PLACEMENT){
			HashMap pl=new HashMap();
			placements.put(makeHashKey(input.getId()),input);
			temporary.put(makeHashKey(input.getId()),input);
		}else{
			throw new Exception("YOY8 getDomain: input element is not COLLATERAL or PLACEMENT type!");
		}
		
		//popunio temp mapu pocetnim elementom i dopunjavam domenu sve dok
		//nema dodatnih elemenata
		while(!temporary.isEmpty()){
			if (debug) System.out.println("\nYOY80->getDomain- punim temporary");
			Collection fromTemporary= temporary.values();
			temporary= new HashMap();
			if (debug) System.out.println("\nYOY80->getDomain- broj iteracija: "+fromTemporary.size());
			Iterator it=fromTemporary.iterator();
			//provjera za svaki novi element domene
			while(it.hasNext()) {
				CollElement element = (CollElement) it.next();
				//da li je skup testiranja tipa COLLATERAL ili PLACEMENT
				if(element.getDataType()==AbstractData.COLLATERAL){
					if (debug) System.out.println("\nYOY80->getDomain- punim plasmane");
					//za kolaterale dohvaca pripadne plasmane
					Vector p=yoy81.selectPlacements((CollateralData)element);
					temporary.putAll(checkNewMembers(p,placements));
					
				}else if(element.getDataType()==AbstractData.PLACEMENT){
					if (debug) System.out.println("\nYOY80->getDomain- punim kolaterale");
					//za plasman dohvaca pripadne kolaterale
					Vector c=yoy81.selectCollaterals((PlacementData)element,eligibility,this.ponded);
					temporary.putAll(checkNewMembers(c,collaterals));
				}
				
		    }			
		}
		Map[] result= new Map[3];
		result[0]=collaterals;
		result[1]=placements;
		result[2]=this.yoy81.getAgreements();
		if (debug) System.out.println("\nYOY80->kraj getDomain");
		return result;

	}
	/**
	 * radi kljuc za hash tablicu
	 * 
	 * @param id id elementa (BigDecimal)
	 * @return kljuc
	 */
	private static Object makeHashKey(Object id){
		if(id!=null){
			return id.toString();
		}else{
			return null;
		}
	}
	
	private HashMap checkNewMembers(Vector newMembers, HashMap oldMembers) throws CollDataException{
		if (debug) System.out.println("\nYOY80->checkNewMembers");
		if ((newMembers!=null)&&(oldMembers!=null)){	
			HashMap result= new HashMap();
			if (debug) System.out.println("\nYOY80->checkNewMembers oldMembers: "+oldMembers.size());
			if (debug) System.out.println("\nYOY80->checkNewMembers newMembers: "+newMembers.size());
			for(int i=0; i<newMembers.size();i++){
				CollElement element=(CollElement)newMembers.get(i);
				//ako novi element nije vec postojao u domeni/tj. mapi
				if(!oldMembers.containsKey(makeHashKey(element.getId()))){
					result.put(makeHashKey(element.getId()),element);
					oldMembers.put(makeHashKey(element.getId()),element);
					if (debug) System.out.println("\nYOY80->checkNewMembers dodan "+element.getId());
				}
			}
			if (debug) System.out.println("\nYOY80->checkNewMembers new count: "+result.size());
			if (debug) System.out.println("\nYOY80->kraj checkNewMembers");
			return result;
		}else{
			throw new CollDataException("One of required input data is not defined (is NULL)!");
		}
	}
	
	private boolean fullValue(BigDecimal sumValue,BigDecimal cookie,BigDecimal placementValue){
		if(sumValue==null){
			sumValue=new BigDecimal(0);
		}
		if(cookie==null){
			cookie=new BigDecimal(0);
		}
		BigDecimal sum=sumValue.add(cookie);
		if(sum.compareTo(placementValue)>0){
			return true;
		}else if(DecimalUtils.compareValues(placementValue.subtract(sum),this.roundError,2)<=0){
			return true;
		}else{
			return false;
		}	
	}
	
}
