package hr.vestigo.modules.collateral.batch.bo30;

public class VRPCodeRow {

	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo30/VRPCodeRow.java,v 1.1 2008/09/05 11:42:14 hraamh Exp $";
	
	/** 
	 * Oznaka vrste vrijednosnice 
	 * */
	public String code=null;
	/**
	 * Opis
	 */
	public String description=null;
	
	public VRPCodeRow(){
		
	}
	
	public VRPCodeRow(String row){
		parse(row);
	}
	
	/**
	 * parsira ulazni red uz trim podataka
	 * 
	 * @param row ulazni redak iz datoteke
	 */
	public void parse(String row){
		code=row.substring(0, 10).trim();
		description=row.substring(10, 138).trim();
	}
	
	public String toString(){
		String result="";
		result+="\n code :|"+code+"|";
		result+="\n name :|"+description+"|";
		return result;
	}
}
