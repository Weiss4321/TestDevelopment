/*
 * Created on 2007.01.04
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy8;

import java.math.BigDecimal;

/**
 * Factory klasa za izradu modela racunanja pokrivenosti plasman kolateralima.
 * 
 * 
 * @author hraamh
 *
 * 
 */
public class ModelFactory {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy8/ModelFactory.java,v 1.9 2010/04/26 09:28:35 hraamh Exp $";
	
	public static Layer getLayer(BigDecimal id,int count, int type, BigDecimal value){		
		if(type==AbstractLayer.RBA_LAYER){
			return new CollateralLayer(count,id);
		}else if(type==AbstractLayer.OTHER_LAYER){
			return new VirtualLayer(value,id);
		}else if(type==AbstractLayer.AGREEMENT_LAYER){
			return new VirtualAgreementLayer(id);
		}else{
			return null;
		}
	}
	
	public static PlacementLayer getPlacementLayer(BigDecimal[] values, BigDecimal[] ids){
		if((values!=null)&&(values.length!=0)&&(ids!=null)&&(ids.length==values.length)){
			return new PlacementLayer(values,ids);
		}else{
			return null;
		}
	}
	
	public static PlacementLayer getPlacementLayer(int count){
		return new PlacementLayer(count);
	}
	
    /**
     * 
     * vraca model za kolateral. ako nema hipoteka vraca SimpleVector, ako ima hipoteka vraca ComplexVector.
     * 
     * @param id
     * @param value
     * @param priority
     * @param type
     * @param count
     * @param layerCount
     * @return
     */
	public static CollateralVector getCollateralVector(BigDecimal id, BigDecimal value,int priority, int type, int count, int layerCount){
		if(type==AbstractVector.SIMPLE_VECTOR){
			SimpleVector sv=new SimpleVector(count,priority,id,value);
			//if(flags!=null) sv.setFlags(flags);
			return sv;
		}else if(type==AbstractVector.COMPLEX_VECTOR){
			return new ComplexVector(count,priority,layerCount,id,value);
		}else{
			return null;
		}
	}
	
    /**
     * dohvat vektora za okvirne sporazume
     * 
     * @param id
     * @param value
     * @param collateralIds
     * @param count
     * @return
     */
	public static CollateralVector getAgreementVector(BigDecimal id, BigDecimal value, BigDecimal[] collateralIds,int count){
		return new AgreementVector(count,id,value,collateralIds);
	}
	
    /**
     * dohvat modela za izracun ovisno o tome da li ima vise kolaterala u izracunu, da li ima okvirnih sporazuma...
     * 
     * @param placements
     * @param collaterals
     * @param agreements
     * @param debugOut
     * @return
     */
	public static PlacementCoverage getCoverageModel(PlacementLayer placements, CollateralVector[] collaterals, CollateralVector[] agreements,boolean debugOut){
		if (agreements==null){
			if(collaterals.length==1){
				return new Model1Coll(placements,collaterals,debugOut);
			}else{
				return new Model(placements,collaterals,debugOut);
			}			
		}else{
			return new ModelAgreement(placements,collaterals,agreements,debugOut);
		}
	}
	
	

}
