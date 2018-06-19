/**
 * 
 */
package hr.vestigo.modules.collateral.batch.bo22;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Ulazni podaci za uèitavanje mjenica
 * 
 * @author hraamh
 *
 */
public class InputData {
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo22/InputData.java,v 1.5 2013/06/17 07:20:08 hraaks Exp $";
	
	/**
	 * Omega ID vlasnika plasmana
	 */
	public String owner=null;
	/**
	 * broj partije plasmana
	 */
	public String acc_no=null;
	/**
	 * broj komada mjenica/zaduznica (ako je više od jedne i ako taj podatak postoji)
	 */
	public Short num=null;
	/**
	 * Vrsta osiguranja - samo za mjenice
	 */
	public String type=null;
	
	/**
	 * Status partije plasmana
	 */
	public String status=null;
	
	public BigDecimal cus_id_insur = null;
	
	public String toString(){
		String result="";
		result+="\n"+owner;
		result+="\n"+acc_no;
		result+="\n"+num;
		result+="\n"+status;
		result+="\n"+type;
		return result;
	}
	
	public int check(){
		if (owner==null){
			return 1;
		}else{
			owner=owner.trim();
		}
		
		if (acc_no==null){
			return 2;
		}else{
			acc_no=acc_no.trim();
			if(acc_no.equals("")) return 2;
		}
		
		if(type==null){
			type="";
		}else{
			type=type.trim();
		}
		
		return 0;
	}
	
	public InputData(){
		
	}

}
