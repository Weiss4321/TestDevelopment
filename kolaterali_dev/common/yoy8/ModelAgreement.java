/*
 * Created on 2007.01.24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import hr.vestigo.modules.rba.util.BooleanUtils;
import hr.vestigo.modules.rba.util.DecimalUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.Vector;

/**
 * 
 * Model izracuna pokrivenosti koji uzima i okvirne sporazume. Okvirni sporazum se 
 * racuna tako da se gleda kao virtualni kolateral. Obraduje ga se uvijek nakon zadnjeg kolaterala
 * koji je obuhvacen okvirnim sporazumom. Na kolateralu se obrade sve RBA hipoteke koje nisu na sporazumu
 * i pripadne hipoteke drugih banaka.Nakon sto se obrade svi kolaterali i hipoteke izvan okvirnog sporazuma
 * obraduje se okvirni sporazum
 *  
 * @author hraamh
 *
 */
public class ModelAgreement implements PlacementCoverage{
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/ModelAgreement.java,v 1.10 2007/05/25 14:06:46 hraamh Exp $";
	
	private PlacementLayer placements=null;
	//private PlacementLayer[] agreements=null; 
	private CollateralVector[] collaterals=null;
	//private BigDecimal[] residues=null;
	private BigDecimal[] placementOriginals=null;
	private boolean[] agreementIndicators=null;
	private boolean[][] agreementFieldInd=null;// novo
	private boolean[] isAgreement=null;//novo
	private BigDecimal[] agreementValues=null;
	private BigDecimal[][] agreementFieldValues=null;// novo
	private boolean debug=true;
	
	
	
	public ModelAgreement(PlacementLayer placements,CollateralVector[] collaterals, CollateralVector[] agreements,boolean debugOut){
		this.placements=placements;
		this.debug=debugOut;
		placementOriginals= new BigDecimal[this.placements.getCount()];
		for(int i=0;i<this.placements.getCount();i++){
			placementOriginals[i]= DecimalUtils.copy(this.placements.getElement(i));
		}
		collaterals=sort(collaterals);
		//agreements=sort(agreements);
		this.collaterals=insertAgreements(agreements,collaterals);
		//residues=new BigDecimal[this.collaterals.length];
		for(int i=0;i<this.agreementIndicators.length;i++){
			if(debug) System.out.print("\t"+agreementIndicators[i]);
		}
		if(debug)System.out.println();
		for(int i=0;i<this.agreementFieldInd.length;i++){
			for(int j=0;j<this.agreementFieldInd.length;j++){
				if(agreementFieldInd[i][j]){
					if(debug)System.out.print("\tOK");
				}else{
					if(debug)System.out.print("\tX");
				}
				
			}
			if(debug)System.out.println();
		}
		if(debug)System.out.println();
	}
	/**
	 * izracun pokrivenosti plasmana kroz kolateral vektore
	 * 
	 * @return kolateral vektori
	 */
	public CollateralVector[] run(){
		//int residueIndex=0;
		
		boolean stillRun=true;
		boolean firstResidue=true;
		int step=0;
		
		long l=System.currentTimeMillis();
		BigDecimal zero= new BigDecimal(0);
		
		if(debug) System.out.println("P:\t\t\t"+placements);
		operate(0);
		if(DecimalUtils.compareValues(placements.sum(), new BigDecimal(0),0)!=0){
			if(debug) System.out.println();
			if(debug) System.out.println("P: prije fiksanja\t"+placements+"\n\n\n");
			fixing();
			if(debug) System.out.println("P: poslje fiksanja\t"+placements+"\n\n\n");
		}
		return collaterals;
	}
	/**
	 * alikvotna raspodjela 
	 * @param beginIndex pocetni kolateral vektor u trenutnoj iteraciji obrade
	 */
	private void operate(int beginIndex){
		BigDecimal zero=new BigDecimal(0);
		for(int i=beginIndex;i<collaterals.length;i++){
			if(collaterals[i].getType()==AbstractVector.AGREEMENT_VECTOR){
				//izracun vrijednosti s kojom okvirni sporazum radi
				operateAgreement(i);
			}else{
				collaterals[i].operate(placements);
			}		
			if(debug) System.out.println(i+". "+collaterals[i]);
			if(debug) System.out.println("R "+i+". ->"+collaterals[i].getResidue());
			if(debug) System.out.println("-------------------------------------------------------------------------------------------------------------");
			if(debug) System.out.println("P:\t\t\t"+placements+"!");			
		}
		if(debug) System.out.print("R(K):\t\t\t");
		BigDecimal[] residues=sumResidues(-1);
		for(int j=0;j<residues.length;j++){
			if (debug) System.out.print(DecimalUtils.scale(residues[j],DecimalUtils.LOW_PRECISION)+"\t\t");
		}
		if(debug) System.out.println();
	}
	
	/**
	 * daje polje ostataka kolaterala do danog indeksa a ostalo se nadopunja s 0. tako ako je indeks nekog OS-a veci od index,
	 * onda se njegove vrijednosti ne oduzimaju od kolaterala 
	 * 
	 * @param index indeks do kojeg se racunaju ostaci
	 * @return polje ostataka
	 */
	private BigDecimal[] sumResidues(int index){
		BigDecimal[] result= new BigDecimal[collaterals.length];
		if(index==-1){
			for(int i=0;i<collaterals.length;i++){		
				result[i]=collaterals[i].getResidue();
				for(int j=0;j<collaterals.length;j++){
					if(agreementFieldInd[j][i]){
						result[i]=result[i].subtract(agreementFieldValues[j][i]);
					}
				}			
			}
		}else{
			for(int i=0;i<collaterals.length;i++){
				if(i<index){
					//racunam
					result[i]=collaterals[i].getResidue();
					for(int j=0;j<index;j++){
						if(agreementFieldInd[j][i]){
							result[i]=result[i].subtract(agreementFieldValues[j][i]);
						}
					}
				}else{
					//ostavljam 0
					result[i]=new BigDecimal(0);
				}
			}
		}	
		return result;
	}
	
	private void operateAgreement(int index){
		AgreementVector av= (AgreementVector) collaterals[index];	
		BigDecimal zero=new BigDecimal(0);
		BigDecimal agreementValue=fixMaxAgreement(index);
		av.setValue(agreementValue);
		for(int j=0;j<index;j++){
			if (agreementFieldInd[index][j]){
				if (debug)System.out.println("za OS "+index+" uzimam "+agreementFieldValues[index][j]+" od kolaterala "+j);
			}
		}		
		if(DecimalUtils.compareValues(agreementValue,zero,DecimalUtils.LOW_PRECISION)>0){
			collaterals[index].operate(placements);		
		}
	}
	
	/**
	 * fiksanje i namijestanje izracuna
	 *
	 */
	/*private void fixing(){
		BigDecimal zero= new BigDecimal(0);
		BigDecimal[] residues=sumResidues(-1);
		for(int out=0;out<collaterals.length;out++){
			if((DecimalUtils.compareValues(placements.sum(),zero,0)==0)&&
					(DecimalUtils.compareValues(DecimalUtils.sumField(residues),zero,DecimalUtils.LOW_PRECISION)==0)){
				return;
				//prekida se rad u petlji
			}	
			if(debug) System.out.println("PRIMARNO FIKSANJE");
			for(int in=out;in<collaterals.length;in++){
				if(DecimalUtils.compareValues(placements.sum(),zero,0)==0) return;
				if(DecimalUtils.compareValues(residues[in],zero,DecimalUtils.LOW_PRECISION)>0){
					if(collaterals[in].getType()==AbstractVector.AGREEMENT_VECTOR){
						if(debug)System.out.println("Radim s ostatkom okvirnog sporazuma "+in+" R="+residues[in]);
					}else{
						if(debug) System.out.println("Radim s ostatkom kolaterala "+in+" R="+residues[in]);
					}
					//ako nema plasmana za podmiriti				
					boolean subfix=false;
					boolean refix=false;
					boolean[] from=BooleanUtils.and(collaterals[in].getFlags(),collaterals[out].getActive());
					boolean[] to=BooleanUtils.and(placements.getActive(),collaterals[out].getFlags());
					
					
					if(BooleanUtils.hasTrue(from)&&BooleanUtils.hasTrue(to)){	
						refix=collaterals[out].revalue(placements.getElements(),residues[in],from,to);
						if(refix){
							if(collaterals[out].getType()==AbstractVector.AGREEMENT_VECTOR){
								if(debug) System.out.println("Fiksam okvirni sporazum "+out+" s coll:"+in);
							}else{
								if(debug) System.out.println("Fiksam kolateral "+out+" s coll:"+in);
							}
							if(debug) System.out.println(out+". "+collaterals[out]);
							calculatePlacements(out);
							System.out.println("P:\t\t\t"+placements);
							System.out.println("------------------------------------------------------------------------------");
							//ponovno racuna sve iduce korake
							operate(out+1);
							//stepIndex=out;
							residues=sumResidues(-1);
						}				
					}
					
					
					if(refix){
						from=BooleanUtils.and(collaterals[in].getFlags(),collaterals[out].getActive());
						to=BooleanUtils.and(placements.getActive(),collaterals[out].getFlags());
					}
					
//					prvo gledamo fiksanje dopunjavanjem sredstava okvirnog sporazuma
					//da li je 
					if((this.agreementIndicators[out])&&(BooleanUtils.hasTrue(from))){
						for(int c=out;c<=in;c++){
							if(agreementFieldInd[c][out]){
								if(subfixAgreement(out,c,from,in)){
									operate(out+1);
									//stepIndex=out;
									residues=sumResidues(-1);
								}
							}
						}	
					}
				}
			}
		}
		if(debug) System.out.println("fiksaniP:"+placements);
	}*/
	
	/**
	 * fiksanje i namijestanje izracuna
	 *
	 */
	private void fixing(){
		BigDecimal zero= new BigDecimal(0);
		BigDecimal[] residues=sumResidues(-1);
		for(int first=0;first<collaterals.length;first++){
			if((DecimalUtils.compareValues(placements.sum(),zero,0)==0)&&
					(DecimalUtils.compareValues(DecimalUtils.sumField(residues),zero,DecimalUtils.LOW_PRECISION)==0)){
				return;
				//prekida se rad u petlji
			}	
			if(debug) System.out.println("PRIMARNO FIKSANJE");
			for(int in=first;in<collaterals.length;in++){
				//residues=sumResidues(-1);
				if(DecimalUtils.compareValues(placements.sum(),zero,0)==0) return;
				if(DecimalUtils.compareValues(residues[in],zero,DecimalUtils.LOW_PRECISION)>0){
					if(collaterals[in].getType()==AbstractVector.AGREEMENT_VECTOR){
						if(debug) System.out.println("Radim s ostatkom okvirnog sporazuma "+in+" R="+residues[in]);
					}else{
						if(debug) System.out.println("Radim s ostatkom kolaterala "+in+" R="+residues[in]);
					}
					//ako nema plasmana za podmiriti				
					boolean subfix=false;
					boolean refix=false;
					boolean[] from=BooleanUtils.and(collaterals[in].getFlags(),collaterals[first].getActive());
					boolean[] to=BooleanUtils.and(placements.getActive(),collaterals[first].getFlags());
					
					
					if(BooleanUtils.hasTrue(from)&&BooleanUtils.hasTrue(to)&&(!agreementFieldInd[in][first])){
						BigDecimal toElementSum=AbstractLayer.sum(placements.getElements(),to,true);
						refix=collaterals[first].revalue(placements.getElements(),residues[in].min(toElementSum),from,to);
						if(refix){
							if(collaterals[first].getType()==AbstractVector.AGREEMENT_VECTOR){
								if(debug) System.out.println("Fiksam okvirni sporazum "+first+" s coll:"+in);
							}else{
								if(debug) System.out.println("Fiksam kolateral "+first+" s coll:"+in);
							}
							if(debug) System.out.println(first+". "+collaterals[first]);
							calculatePlacements(first);
							if(debug) System.out.println("P:\t\t\t"+placements);
							if(debug) System.out.println("------------------------------------------------------------------------------");
							//ponovno racuna sve iduce korake
							operate(first+1);
							//stepIndex=out;
							residues=sumResidues(-1);
						}				
					}
					
					
					if(refix){
						from=BooleanUtils.and(collaterals[in].getFlags(),collaterals[first].getActive());
						to=BooleanUtils.and(placements.getActive(),collaterals[first].getFlags());
					}
					
//					prvo gledamo fiksanje dopunjavanjem sredstava okvirnog sporazuma
					//da li je 
					if((this.agreementIndicators[first])&&(BooleanUtils.hasTrue(from))){					
						for(int c=first;c<=in;c++){
							if(agreementFieldInd[c][first]){
								if(subfixAgreement(first,c,from,in)){
									operate(first+1);
									//stepIndex=out;
									residues=sumResidues(-1);
								}
							}
						}				
					}
				}
			}
			
			if(debug) System.out.println("SEKUNDARNO FIKSANJE");
			for(int second=first;second<collaterals.length;second++){
				if(debug) System.out.println(second+". residue: "+residues[second]);
				if(DecimalUtils.compareValues(residues[second],zero,DecimalUtils.LOW_PRECISION)>0){
					boolean[] from=BooleanUtils.and(collaterals[second].getFlags(),collaterals[first].getActive());
					if(BooleanUtils.hasTrue(from)){
						for(int third=second+1;third<collaterals.length;third++){
							boolean[] to=BooleanUtils.and(placements.getActive(),collaterals[third].getFlags());
							if(BooleanUtils.hasTrue(to)){
								to=BooleanUtils.and(to,collaterals[first].getFlags());
								BigDecimal toElementSum=AbstractLayer.sum(placements.getElements(),to,true);
								toElementSum=toElementSum.add(AbstractLayer.sum(collaterals[third].getElements(),to,true));
								to=BooleanUtils.and(collaterals[first].getFlags(),collaterals[third].getFlags());
								boolean fixed=collaterals[first].revalue(placements.getElements(),residues[second].min(toElementSum),from,to);
								if(fixed){
									if(debug) System.out.println("Fiksam kolateral "+first+" ostatkom kolaterala "+second);
									if(debug) System.out.println(first+". "+collaterals[first]);
									calculatePlacements(first);
									if(debug) System.out.println("P:\t\t\t"+placements);
									if(debug) System.out.println("------------------------------------------------------------------------------");
									//ponovno racuna sve iduce korake
									operate(first+1);
									residues=sumResidues(-1);
								}
							}
						}	
					}
				}else if(collaterals[second].getType()==AbstractVector.AGREEMENT_VECTOR){
					boolean[] from=BooleanUtils.and(collaterals[first].getFlags(),collaterals[second].getActive());
					boolean[] to=BooleanUtils.and(placements.getActive(),collaterals[first].getFlags());
					AgreementVector av=(AgreementVector) collaterals[second];
					BigDecimal fixNeed=AbstractLayer.sum(placements.getElements(),to,true).add(AbstractLayer.sum(av.getElements(),to,true));
					if(BooleanUtils.hasTrue(from)&&BooleanUtils.hasTrue(to)&&(DecimalUtils.compareValues(av.getFixedMaximum(),av.getValue(),DecimalUtils.LOW_PRECISION)>0)){
						for(int third=0;third<second;third++){
							if((agreementFieldInd[second][third])&&(DecimalUtils.compareValues(residues[third],zero,DecimalUtils.LOW_PRECISION)>0)){
								BigDecimal maxVal=av.getFixedMaximum().subtract(av.getValue());
								maxVal=maxVal.min(residues[third]).min(fixNeed);
								boolean fixed=collaterals[first].revalue(placements.getElements(),maxVal,from,to);
								if(fixed){
									if(debug) System.out.println("Fiksam kolateral "+first+" ostatkom kolaterala "+second);
									if(debug) System.out.println(first+". "+collaterals[first]);
									calculatePlacements(first);
									if(debug) System.out.println("P:\t\t\t"+placements);
									if(debug) System.out.println("------------------------------------------------------------------------------");
									//ponovno racuna sve iduce korake
									operate(first+1);
									residues=sumResidues(-1);
								}
							}
						}
					}
				}
			}
			
		}
		if (debug)System.out.println("fiksaniP:"+placements);
	}
	
	/**
	 * sortiranje kolaterala po likvidnosti, datumu ovjere CO te vremenu unosa 
	 * 
	 * @param vectors kolateral vektori
	 * @return kolateral vektori
	 */
	public CollateralVector[] sort(CollateralVector[] vectors) {		
		for(int i=0;i<vectors.length;i++)
			for(int j=i+1;j<vectors.length;j++){
				if(vectors[i].getPriority()>vectors[j].getPriority()){
					CollateralVector temp=vectors[i];
					vectors[i]=vectors[j];
					vectors[j]=temp;
					//ako su istog prioriteta
				}else if(vectors[i].getPriority()==vectors[j].getPriority()){
					//Date d1=Date.valueOf(vectors[i].getProcessedDate().toString());
					//Date d2=Date.valueOf(vectors[j].getProcessedDate().toString());
					Date d1=vectors[i].getProcessedDate();
					Date d2=vectors[j].getProcessedDate();
					//ali razlicitog datuma procjene CO
					if((d1!=null)&&(d2!=null)&&(d1.before(d2))){
						CollateralVector temp=vectors[i];
						vectors[i]=vectors[j];
						vectors[j]=temp;
						//istog datuma procjene ali upisani razliciti datum
					}else if((d1!=null)&&(d2!=null)&&(d1.equals(d2))
							&&(vectors[i].getOpeningTS().before(vectors[j].getOpeningTS()))){
						CollateralVector temp=vectors[i];
						vectors[i]=vectors[j];
						vectors[j]=temp;
					}
					
				}
			}
		return vectors;
	}
	/**
	 * ubacuje OS tik iza zadnjeg kolaterala koji je ukljucen u OS
	 * 
	 * @param agreements vektori Os-a
	 * @param collaterals kolateral vektori
	 */
	protected CollateralVector[] insertAgreements(CollateralVector[] agreements,CollateralVector[] collaterals){
		//CollateralVector[] result= new CollateralVector[collaterals.length+agreements.length];
		Vector v= new Vector();
		for(int i=0;i<collaterals.length;i++){
			v.add(i,collaterals[i]);			
		}
		boolean toBreak=false;
		for(int i=agreements.length-1;i>=0;i--){
			toBreak=false;
			for(int j=v.size()-1;j>=0;j--){
				//ako je vektor tipa kolateral a ne sporazum
				CollateralVector collateral=(CollateralVector) v.get(j);
				if(collateral.getType()!=AbstractVector.AGREEMENT_VECTOR){
					AgreementVector temp= (AgreementVector) agreements[i];
					BigDecimal[] ids= temp.getCollateralIds();
					for (int k=0;k<ids.length;k++){
						if(ids[k].compareTo(collateral.getId())==0){
							toBreak=true;
							if(j+1>=v.size()){
								v.add(temp);
							}else{
								v.add(j+1,temp);
							}
							break;
						}
					}	
				}
				if(toBreak) break;
			}
		}
		CollateralVector[] result= new CollateralVector[v.size()];
		this.agreementIndicators=new boolean[v.size()];
		this.isAgreement=new boolean[v.size()];
		this.agreementFieldInd=new boolean[v.size()][v.size()];
		this.agreementValues=new BigDecimal[v.size()];
		this.agreementFieldValues=new BigDecimal[v.size()][v.size()];
		
		for(int i=0;i<v.size();i++){
			result[i]=(CollateralVector)v.get(i);
			if(result[i].getType()==AbstractVector.AGREEMENT_VECTOR){
				this.isAgreement[i]=true;
				AgreementVector agreement=(AgreementVector) result[i];
				boolean[] dependencies=new boolean[v.size()];
				for(int j=0;j<i;j++){
					if(result[j].getType()!=AbstractVector.AGREEMENT_VECTOR){
						//postavlja ovisnost dependency na true ako je sporazum result[i] na kolateralu result[j]
						dependencies[j]=agreement.involvesCollateral(result[j].getId());
						if(dependencies[j]){
							agreementIndicators[j]=true;
							agreementFieldInd[i][j]=true;
						}
					}
				}
				agreement.setDependencys(dependencies);
			}
		}
		
		return result;
	}
	/**
	 * zamjena kolateral vektora
	 * @param first
	 * @param second
	 */
	protected void swap(CollateralVector first, CollateralVector second){
		CollateralVector temp=first;
		first=second;
		second=temp;
	}
	/**
	 * dohvat kolateral vektora
	 * @return kolateral vektori
	 */
	public CollateralVector[] getCollateralVectors() {
		return this.collaterals;
	}
	
	/**
	 * @return indikatori OS-a.
	 */
	public boolean[][] getAgreementIndicators() {
		return agreementFieldInd;
	}
	/**
	 * @return vrijednosti kolaterala koji se koriste u okvirnim sporazumima.
	 */
	public BigDecimal[][] getAgreementValues() {
		return agreementFieldValues;
	}
	/**
	 * @return ime modela
	 */
	public String getModelName() {		
		return this.getClass().getName();
	}
	/**
	 * nastimava vrijednost OS-a i njegovu podjelu po kolateralima
	 * @param index indeks okvirnog sporazuma
	 * @return vrijednost okvirnog sporazuma
	 */
	private BigDecimal fixMaxAgreement(int index){
		BigDecimal zero= new BigDecimal(0);
		BigDecimal[] residues= sumResidues(index);
		BigDecimal maxAgreementValue=((AgreementVector)collaterals[index]).getFixedMaximum();
		BigDecimal value=maxAgreementValue;
		value=value.min(placements.sum(collaterals[index].getFlags(),true));
		for(int collIndex=0;collIndex<index;collIndex++){
			if((!isAgreement[collIndex])&&(agreementFieldInd[index][collIndex])){
				//ako je obican kolateral i ulazi u ovaj okvirni sporazum
				if(DecimalUtils.compareValues(residues[collIndex],zero,DecimalUtils.LOW_PRECISION)>0){
					if(residues[collIndex].compareTo(value)>=0){
						//residues[j]=residues[j].subtract(amount);
						this.agreementFieldValues[index][collIndex]=value;
						value=new BigDecimal(0);
					}else{
						value=value.subtract(residues[collIndex]);
						this.agreementFieldValues[index][collIndex]=residues[collIndex];
						residues[collIndex]=new BigDecimal(0);
					}	
				}else{
					this.agreementFieldValues[index][collIndex]=zero;
					BigDecimal max=value;
					for(int agrIndex=0;agrIndex<index;agrIndex++){
						if(agreementFieldInd[agrIndex][collIndex]){
							//ako je ukljucen u neki drugi okvirni sporazum
							boolean[] from=this.agreementFieldInd[agrIndex];
							from=BooleanUtils.exclude(from,this.agreementFieldInd[index]);
							//BigDecimal tempResidues=AbstractLayer.sum(residues,from,true);
							for(int f=0;f<index;f++){
								if(agreementFieldValues[agrIndex][collIndex].compareTo(zero)==0) break;
								if(max.compareTo(zero)==0) break;
								if((from[f])&&(DecimalUtils.compareValues(residues[f],zero,DecimalUtils.LOW_PRECISION)>0)){
									BigDecimal tmp=max.min(residues[f]);
									if(agreementFieldValues[agrIndex][collIndex].compareTo(tmp)>0){
										//ima vise udijela drugog OS-a nego sto nam treba
										max=max.subtract(tmp);
										residues[f]=residues[f].subtract(tmp);
										agreementFieldValues[agrIndex][f]=agreementFieldValues[agrIndex][f].add(tmp);
										agreementFieldValues[agrIndex][collIndex]=agreementFieldValues[agrIndex][collIndex].subtract(tmp);
										agreementFieldValues[index][collIndex]=agreementFieldValues[index][collIndex].add(tmp);
									}else{
										max=max.subtract(agreementFieldValues[agrIndex][collIndex]);
										residues[f]=residues[f].subtract(agreementFieldValues[agrIndex][collIndex]);
										agreementFieldValues[agrIndex][f]=agreementFieldValues[agrIndex][f].add(agreementFieldValues[agrIndex][collIndex]);
										agreementFieldValues[agrIndex][collIndex]=zero;
										agreementFieldValues[index][collIndex]=agreementFieldValues[index][collIndex].add(agreementFieldValues[agrIndex][collIndex]);
									}
								}
							}
						}
					}
					
				}
			}
		}
		return DecimalUtils.sumField(agreementFieldValues[index]);
	}
	/**
	 * umanjuje potrosnju kolaterala po pripadnim plasmanima u cilju povecanja vrijednosti okvirnog sporazuma
	 * 
	 * @param collIndex indeks kolaterala
	 * @param agrIndex indeks okvirnog sporazuma
	 * @param from oznake plasmana cija se vrijednost na kolateralu treba umanjiti
	 * @param fromIndex
	 * @return true ako je izvrsena preraspodjela
	 */
	private boolean subfixAgreement(int collIndex,int agrIndex, boolean[] from,int fromIndex){
		AgreementVector agreement= (AgreementVector) this.collaterals[agrIndex];
		boolean subfix=false;
		BigDecimal[] residues=sumResidues(-1);
		//ako okvirni sporazum sadrzi plasman kojeg treba pokriti
		if(BooleanUtils.hasTrue(BooleanUtils.and(agreement.getFlags(),placements.getActive()))&&DecimalUtils.compareValues(agreement.getFixedMaximum(),agreement.getValue(),DecimalUtils.LOW_PRECISION)>0){
			BigDecimal maxAmount = agreement.getFixedMaximum().subtract(agreement.getValue());
			if(maxAmount.compareTo(residues[fromIndex])>0){
				maxAmount=residues[fromIndex];
			}
			subfix=collaterals[collIndex].revalue(placements.getElements(),maxAmount,from,null);
			if(subfix){
				if(debug) System.out.println("Fiksam oduzimanjem od kolaterala "+collIndex+" iznos:"+maxAmount);
				if(debug) System.out.println(collIndex+". "+collaterals[collIndex]);
				if(debug) System.out.println("R:"+collaterals[collIndex].getResidue());
				
				calculatePlacements(collIndex);
				if(debug) System.out.println("P:\t\t\t"+placements);
				if(debug) System.out.println("------------------------------------------------------------------------------");
			}
			
		}
		
		return subfix;
	}
	/**
	 * racuna nepokrivenost plasmana s ozbirom na dani indeks. tj umanjuje vrijednosti plasmana za iznose koji su 
	 * podijeljeni na kolateralima <= od index
	 * 
	 * @param index
	 */
	private void calculatePlacements(int index){
		BigDecimal[] addedValues=new BigDecimal[placements.getCount()];
		Arrays.fill(addedValues,new BigDecimal(0));
		for(int i=0;i<=index;i++){			
			for(int j=0;j<placements.getCount();j++){
				BigDecimal tmp=collaterals[i].getElement(j);
				if(tmp==null){
					tmp=addedValues[j];
				}else{
					addedValues[j]=addedValues[j].add(tmp);
				}
			}							
		}
		placements.setElements(DecimalUtils.subFields(placementOriginals,addedValues));
	}
	
}
