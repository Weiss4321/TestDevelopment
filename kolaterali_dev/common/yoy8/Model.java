/*
 * Created on 2007.01.05
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;

import hr.vestigo.modules.rba.util.BooleanUtils;
import hr.vestigo.modules.rba.util.DecimalUtils;

/**
 * Model izracuna pokrivenosti gdje nema okvirnih sporazuma
 * 
 * @author hraamh
 *
 * 
 */
public class Model implements PlacementCoverage{
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/Model.java,v 1.12 2010/04/26 09:28:35 hraamh Exp $";
	
	private PlacementLayer placements=null;
	private CollateralVector[] collaterals=null;
	private BigDecimal[] residues=null;
	private BigDecimal[] placementOriginals=null;
	private boolean debug=true;
	private int stepIndex=0;
	

	
	public Model(PlacementLayer placements,CollateralVector[] collaterals,boolean debugOut){
		this.placements=placements;
		this.debug=debugOut;
		placementOriginals= new BigDecimal[this.placements.getCount()];
		for(int i=0;i<this.placements.getCount();i++){
			placementOriginals[i]= DecimalUtils.copy(this.placements.getElement(i));
		}
		this.collaterals=collaterals;
		sort(this.collaterals);
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
        /*
         * ovo je polje sa ostacima kolaterala nakon izracuna. koristi se u izracunu da bi se nastimala raspodjela
         * po kolateralu i sto vise pokrili plasmani
         */
		residues=new BigDecimal[collaterals.length];
		if(debug) System.out.println("P:\t\t\t"+placements);
		operate(0);
		if(DecimalUtils.compareValues(placements.sum(), new BigDecimal(0),0)!=0){
			if(debug) System.out.println();
			if(debug) System.out.println("P: prije fiksanja\t"+placements+"\n\n\n");
			fixing();
			if(debug) System.out.println("P: poslje fiksanja\t"+placements+"\n\n\n");
		}
		if (debug)System.out.print("R(K):\t\t\t");
		for(int j=0;j<residues.length;j++){
			if (debug) System.out.print(DecimalUtils.scale(residues[j],DecimalUtils.LOW_PRECISION)+"\t\t");
		}
		return collaterals;
	}
	/**
	 * alikvotna raspodjela. racuna se za svaki collateral vektor po predanom indeksu. 
	 * @param beginIndex pocetni kolateral vektor u trenutnoj iteraciji obrade
	 */
	private void operate(int beginIndex){
		for(int i=beginIndex;i<collaterals.length;i++){
			collaterals[i].operate(placements);
			if(debug) System.out.println(i+". "+collaterals[i]);
			if(debug) System.out.println("-------------------------------------------------------------------------------------------------------------");
			if(debug) System.out.println("P:\t\t\t"+placements+"!");
			residues[i]=collaterals[i].getResidue();
		}
		if(debug) System.out.print("R(K):\t\t\t");
		for(int j=0;j<residues.length;j++){
			if (debug)System.out.print(DecimalUtils.scale(residues[j],DecimalUtils.LOW_PRECISION)+"\t\t");
		}
		if(debug) System.out.println();
	}
	/**
	 * fiksanje i namijestanje vrijednosti u izracunu
	 *@deprecated
	 */
	private void fixing1(){
		BigDecimal zero= new BigDecimal(0);
		for(int out=0;out<collaterals.length;out++){
			if((DecimalUtils.compareValues(placements.sum(),zero,0)==0)&&
					(DecimalUtils.compareValues(DecimalUtils.sumField(residues),zero,DecimalUtils.LOW_PRECISION)==0)){
				return;
				//prekida se rad u petlji
			}
			if(DecimalUtils.compareValues(residues[out],zero,DecimalUtils.LOW_PRECISION)>0){
				if(debug) System.out.println("Fiksam kolateralom "+out);
				for(int in=stepIndex;in<out;in++){
					//ako nema plasmana za podmiriti
					if(DecimalUtils.compareValues(placements.sum(),zero,0)==0) return;
					boolean[] from=BooleanUtils.and(collaterals[out].getFlags(),collaterals[in].getActive());
					boolean[] to=BooleanUtils.and(placements.getActive(),collaterals[in].getFlags());
					if(BooleanUtils.hasTrue(from)&&BooleanUtils.hasTrue(to)){					
						boolean fixed=collaterals[in].revalue(placements.getElements(),residues[out],from,to);
						if(fixed){
							if(debug) System.out.println("Fiksam kolateral "+in+"?: ");
							if(debug) System.out.println(in+". "+collaterals[in]);
							//placements.resetElements(true);
							BigDecimal[] addedValues=new BigDecimal[placements.getCount()];
							Arrays.fill(addedValues,new BigDecimal(0));
							for(int o=0;o<collaterals.length;o++){
								if(o<=in){
									for(int j=0;j<placements.getCount();j++){
										BigDecimal tmp=collaterals[o].getElement(j);
										if(tmp==null){
											tmp=addedValues[j];
										}else{
											addedValues[j]=addedValues[j].add(tmp);
										}
									}
								}				
							}
							placements.setElements(DecimalUtils.subFields(placementOriginals,addedValues));
							if(debug) System.out.println("P:\t\t\t"+placements);
							if(debug) System.out.println("------------------------------------------------------------------------------");
							//ponovno racuna sve iduce korake
							operate(in+1);
							stepIndex=in;
						}				
					}
				}
			}
		}
		if(debug) System.out.println("fiksaniP:"+placements);
	}
	/**
	 * fiksanje i namijestanje izracuna
	 *
	 */
	private void fixing(){
		BigDecimal zero= new BigDecimal(0);
		for(int first=0;first<collaterals.length;first++){
			//ako nema viska nema niti namjestanja
			if((DecimalUtils.compareValues(placements.sum(),zero,0)==0)&&
					(DecimalUtils.compareValues(DecimalUtils.sumField(residues),zero,DecimalUtils.LOW_PRECISION)==0)){
				return;
				//prekida se rad u petlji
			}
			boolean[] to=BooleanUtils.and(placements.getActive(),collaterals[first].getFlags());
			if(debug) System.out.println("PRIMARNO FIKSANJE");
			if(BooleanUtils.hasTrue(to)){
				for(int second=first;second<collaterals.length;second++){
					if(DecimalUtils.compareValues(residues[second],zero,DecimalUtils.LOW_PRECISION)>0){
						boolean[] from=BooleanUtils.and(collaterals[second].getFlags(),collaterals[first].getActive());
						if(BooleanUtils.hasTrue(from)){
							BigDecimal toElementSum=AbstractLayer.sum(placements.getElements(),to,true);
							boolean fixed=collaterals[first].revalue(placements.getElements(),residues[second].min(toElementSum),from,to);
							if(fixed){
								if(debug) System.out.println("Fiksam kolateral "+first+" ostatkom kolaterala "+second);
								if(debug) System.out.println(first+". "+collaterals[first]);
								calculatePlacements(first);
								if(debug) System.out.println("P:\t\t\t"+placements);
								if(debug) System.out.println("------------------------------------------------------------------------------");
								//ponovno racuna sve iduce korake
								operate(first+1);
							}
						}
					}
				}
			}
			if(debug) System.out.println("SEKUNDARNO FIKSANJE");
			for(int second=first;second<collaterals.length;second++){
				if(DecimalUtils.compareValues(residues[second],zero,DecimalUtils.LOW_PRECISION)>0){
					boolean[] from=BooleanUtils.and(collaterals[second].getFlags(),collaterals[first].getActive());
					if(BooleanUtils.hasTrue(from)){
						for(int third=second+1;third<collaterals.length;third++){
							to=BooleanUtils.and(placements.getActive(),collaterals[third].getFlags());
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
								}
							}
						}	
					}
				}
			}
			
			
		}
		if(debug) System.out.println("fiksaniP:"+placements);
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
	
	/**
	 * dohvat kolateral vektora
	 * @return kolateral vektori
	 */
	public CollateralVector[] getCollateralVectors() {
		return this.collaterals;
	}
	/* (non-Javadoc)
	 * @see hr.vestigo.modules.collateral.common.yoy8.PlacementCoverage#getModelName()
	 */
	public String getModelName() {		
		return this.getClass().getName();
		
		
	}

}
