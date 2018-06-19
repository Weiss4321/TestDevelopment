package hr.vestigo.modules.collateral.jcics.co39;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Vector;

public class MainCollateralData {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/MainCollateralData.java,v 1.5 2010/07/02 11:33:55 vu00201 Exp $";
	
	private final String TRUE_S="Y";
	private final String FALSE_S="N";
	
	public BigDecimal col_hea_id=null;
	
	public String collateralCMID=null;
	public String collateralGroupCode=null;
	public String collateralGroupName=null;
	public String collateralTypeCode=null;
	public String collateralTypeName=null;
	public BigDecimal nominalValue=null;
	public String currency=null;
	public String eligibilityIndicator=null;
	public String localEligInd=null;
	public BigDecimal priorClaimsAmount=null;
	public String legalOpinionYN=null;
	public BigDecimal discount=null;
	public String insuranceYN=null;
	
	public String mortgage_status =null;
	public BigDecimal col_cat_id=null;
	public BigDecimal col_typ_id=null;
	
	
	public BigDecimal evaluationAmountExternal=null;
	public BigDecimal evaluationAmountInternal=null;
	public Date evaluationDateExternal=null; 
	public Date evaluationDateInternal=null; 
	public String evaluationAppraiserIDExternal=null; 
	public String evaluationAppraiserIDInternal=null; 
	public String evaluationAppraiserNameExternal=null; 
	public String evaluationAppraiserNameInternal=null; 
	public String evaluationRevaluationYN=null; 
	public String evaluationCommentExternal=null; 
	public String evaluationCommentInternal =null;

	
	public void prepare(){
		collateralCMID=checkAndCut(collateralCMID, 20);
		collateralGroupCode=checkAndCut(collateralGroupCode, 4);
		collateralGroupName=checkAndCut(collateralGroupName, 40);
		collateralTypeCode=checkAndCut(collateralTypeCode, 10);
		collateralTypeName=checkAndCut(collateralTypeName, 40);
		currency=checkAndCut(currency, 3);	
		evaluationAppraiserIDExternal=checkAndCut(evaluationAppraiserIDExternal, 10);
		evaluationAppraiserIDInternal=checkAndCut(evaluationAppraiserIDInternal, 10);
		evaluationAppraiserNameExternal=checkAndCut(evaluationAppraiserNameExternal, 128);
		evaluationAppraiserNameInternal=checkAndCut(evaluationAppraiserNameInternal, 128);
		evaluationCommentExternal=checkAndCut(evaluationCommentExternal, 64);
		evaluationCommentInternal=checkAndCut(evaluationCommentInternal, 64);
	}
	
	public Vector toMainVector(){
		//prepare();
		Vector result=new Vector();
		//result.add(col_hea_id);
		result.add(collateralCMID);
		result.add(collateralGroupCode);
		result.add(collateralGroupName);
		result.add(collateralTypeCode);
		result.add(collateralTypeName);
		result.add(nominalValue);
		result.add(currency);
        result.add(eligibilityIndicator);
        result.add(localEligInd);
		result.add(priorClaimsAmount);
		if("D".equals(legalOpinionYN)){
			result.add(TRUE_S);
		}else{
			result.add(FALSE_S);
		}
		result.add(discount);
		if("D".equals(insuranceYN)){
			result.add(TRUE_S);
		}else{
			result.add(FALSE_S);
		}
		return result;
	}
	
	public Vector toEvaluationVectors(){
		Vector result=new Vector();
		Vector tmp=new Vector();
		
		tmp.add(collateralCMID);
		tmp.add("E");
		tmp.add(evaluationAmountExternal);
		tmp.add(currency);
		tmp.add(evaluationDateExternal);
		tmp.add(evaluationAppraiserIDExternal);
		tmp.add(evaluationAppraiserNameExternal);
		tmp.add("");
		tmp.add(evaluationCommentExternal);
		
		result.add(tmp);
		
		tmp= new Vector();
		
		tmp.add(collateralCMID);
		tmp.add("I");
		tmp.add(evaluationAmountInternal);
		tmp.add(currency);
		tmp.add(evaluationDateInternal);
		tmp.add(evaluationAppraiserIDInternal);
		tmp.add(evaluationAppraiserNameInternal);
		tmp.add(evaluationRevaluationYN);
		tmp.add(evaluationCommentInternal);
		
		result.add(tmp);
		
		return result;
	}
	
	/**
	* Provjerava da li je ulazni string dozvoljene duljine. ako prelazi dozvoljenu duljinu reze ostatak.
	* 
	* @param input ulazni string
	* @param max_size maksimalna duljina string
	* @return trimani ulazni string 
	*/
	private String checkAndCut(String input, int max_size){
		String result=null;
		if(input!=null){
			result=input.trim();
			if(result.length()>max_size){
				result=result.substring(0, max_size);
			}
		}
		return result;
	}
}
