package hr.vestigo.modules.collateral.batch.bo30;

public class StockMarketDataRow {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo30/StockMarketDataRow.java,v 1.1 2008/09/05 11:42:14 hraamh Exp $";
	
	/** 
	 * Oznaka burze 
	 * */
	public String code=null;
	/**
	 * Naziv burze
	 */
	public String name=null;
	/**
	 * Država u kojoj je burza, troznamenkasta brojèana oznaka, npr za Hrvatsku = 191
	 */
	public String country=null;
	
	public StockMarketDataRow(){
		
	}
	
	public StockMarketDataRow(String row){
		parse(row);
	}
	
	/**
	 * parsira ulazni red uz trim podataka
	 * 
	 * @param row ulazni redak iz datoteke
	 */
	public void parse(String row){
		code=row.substring(0, 10).trim();
		name=row.substring(10, 138).trim();
		country=row.substring(138, 141).trim();
	}
	
	public String toString(){
		String result="";
		result+="\n code :|"+code+"|";
		result+="\n name :|"+name+"|";
		result+="\n country :|"+country+"|";
		return result;
	}

}
