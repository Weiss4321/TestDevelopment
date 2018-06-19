/*
 * Created on 2007.04.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo09;

import java.math.BigDecimal;
import java.sql.Date;



/**
 * input data objekt za vozila
 * 
 * @author hraamh
 *
 */
public class InputData {
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo09/InputData.java,v 1.2 2007/05/30 13:46:10 hraamh Exp $";
	
	/**
	 * vrsta vozila
	 * vrijednosti:
	 * OSOBNAUTO – osobni automobili
	 * MOTOCIKLI – motocikli
	 * KAMBUSPRV – kamioni,autobusi,pružna vozila
	 * GRADJVOZI – graðevinska vozila i graðevinski strojevi
	 * OSTALVOZI – ostala vozila
	 */
	public String	veh_type=null;
	/** broj sasije*/
	public String	veh_vin_num=null;
	/**marka vozila*/
	public String	veh_made=null;
	/**tip vozila*/
	public String	veh_subtype=null;
	/** model vozila */
	public String	veh_model=null;
	/** boja vozila */
	public String	veh_colour=null;
	/** godina proizvodnje */
	public String	veh_made_year=null;
	/** da li je vozilo kasko osigurano; vrijednosti D i N */
	public String	veh_kasko=null;
	/** stanje vozila
	 * vrijednosti:
	 * N - novo
	 * R - rabljeno
	 */
	public String	veh_state=null;
	/** valuta nominalne vrijednosti */
	public String	veh_cur=null;
	/** nominalna vrijednost vozila */
	public BigDecimal	veh_amount=null;
	/** datum procjene vozila */
	public Date		veh_date=null;
	/** datum kada je knjizica vozila dostavljena u banku */
	public Date		lic_date=null;
	/** Omega ID vlasnika vozila */
	public String	veh_owner=null;
	/** broj partije plasmana */
	public String	acc_num=null;
	/** Omega ID vlasnika plasmana */
	public String	loan_owner=null;
						
	public InputData(){
		
	}
	
	/*public InputData(IteratorVehicleDWH input) throws Exception{
		int index=-1;
		veh_type=input.veh_type();
		veh_vin_num=input.veh_vin_num();
		*//**
		 * cvikam nebulozu u zagradi ako je ima
		 *//*
		if((veh_vin_num!=null)&&((index=veh_vin_num.indexOf("("))!=-1)){
			veh_vin_num=veh_vin_num.substring(0,index);
		}
		veh_made=input.veh_made();
		veh_subtype=input.veh_subtype();
		veh_model=input.veh_model();
		veh_colour=input.veh_colour();
		veh_made_year=input.veh_made_year();
		veh_kasko=input.veh_kasko();
		veh_state=input.veh_state();
		veh_cur=input.veh_cur();
		veh_amount=input.veh_amount();
		veh_date=input.veh_date();
		lic_date=input.lic_date();
		veh_owner=input.veh_owner();
		acc_num=input.acc_num();
		loan_owner=input.loan_owner();

		int check=check();
		if(check==1) {
			throw new Exception("veh_kasko nije D ili N");
		}else if(check==2) {
			throw new Exception("veh_state nije R ili N");
		}
	}*/
	
	public String toString(){
		String result="\n";
		result+="veh_type: "+veh_type+"\n";
		result+="veh_vin_num: "+veh_vin_num+"\n";
		result+="veh_made: "+veh_made+"\n";
		result+="veh_subtype: "+veh_subtype+"\n";
		result+="veh_model: "+veh_model+"\n";
		result+="veh_colour: "+veh_colour+"\n";
		result+="veh_made_year: "+veh_made_year+"\n";
		result+="veh_kasko: "+veh_kasko+"\n";
		result+="veh_state: "+veh_state+"\n";
		result+="veh_cur: "+veh_cur+"\n";
		result+="veh_amount: "+veh_amount+"\n";
		result+="veh_date: "+veh_date+"\n";
		result+="lic_date: "+lic_date+"\n";
		result+="veh_owner: "+veh_owner+"\n";
		result+="acc_num: "+acc_num+"\n";
		result+="loan_owner: "+loan_owner+"\n";
		
		return result;
	}
	
	/**
	 * provjera, trimanje i nuliranje ulaznih podataka
	 * 
	 * @return 0 -sve je u redu; 1- veh_kasko nije D ili N; 2- veh_state nije N ili R
	 */
	public int check(){
		veh_type=trimAndNull(veh_type);
		veh_vin_num=trimAndNull(veh_vin_num);
		veh_made=trimAndNull(veh_made);
		veh_type=trimAndNull(veh_type);
		veh_model=trimAndNull(veh_model);
		veh_colour=trimAndNull(veh_colour);
		veh_made_year=trimAndNull(veh_made_year);
		veh_kasko=trimAndNull(veh_kasko);
		if((veh_kasko!=null)&&(!veh_kasko.equalsIgnoreCase("D"))&&(!veh_kasko.equalsIgnoreCase("N"))){
			return 1;
		}
		veh_state=trimAndNull(veh_state);
		if((veh_state!=null)&&(!veh_state.equalsIgnoreCase("R"))&&(!veh_state.equalsIgnoreCase("N"))){
			return 2;
		}
		veh_cur=trimAndNull(veh_cur);		
		veh_owner=trimAndNull(veh_owner);
		acc_num=trimAndNull(acc_num);
		loan_owner=trimAndNull(loan_owner);
		return 0;
	}
	
	private String trimAndNull(String input){
		String result=input;
		if(input!=null){
			result=input.trim();
			if(result.equals("")){
				result=null;
			}
		}
		return result;
	}

}
