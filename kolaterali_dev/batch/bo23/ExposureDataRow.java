package hr.vestigo.modules.collateral.batch.bo23;

import java.math.BigDecimal;
import java.sql.Date;

public class ExposureDataRow {
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo23/ExposureDataRow.java,v 1.6 2012/04/26 13:30:29 hramkr Exp $";
	
	public long id=-1;
	/**
     * B2 ASSET CLASS VL. PLASMANA
	 */ 
    public String b2_asset_class=null;
	/**
	 * ID OMEGA KORISNIKA PLASMANA
	 */
	public String owner_code=null;
	/**
	 * NAZIV KORISNIKA PLASMANA
	 */
	public String owner_name=null;
	/**
	 * PARTIJA PLASMANA
	 */
	public String cus_acc_no=null;
	/**
	 * IZNOS PARTIJE EUR
	 */
	public BigDecimal amount_eur=null;
	
	public BigDecimal amount=null;
	/**
	 * BROJ ZAHTJEVA
	 */
	public String request_no=null;
	
	public Date hf_date_from=null;
	
	public Date hf_date_until=null;
	
	public String toString(){
		String result="";
        result+="\n b2_asset_class: "+b2_asset_class;
		result+="\n owner_code: "+owner_code;
		result+="\n owner_name: "+owner_name;
		result+="\n acc_no: "+cus_acc_no;
		result+="\n amount: "+amount;
		result+="\n eur_amount: "+amount_eur;
		result+="\n request_no: "+request_no;
		result+="\n hf_date_from: "+hf_date_from;
		result+="\n hf_date_until: "+hf_date_until;
		return result;
	}
	
	public void trim(){
		if (owner_code!=null) {
			owner_code=owner_code.trim();
		}
		if (owner_name!=null) {
			owner_name=owner_name.trim();
		}
		if (cus_acc_no!=null) {
			cus_acc_no=cus_acc_no.trim();
		}
        if (b2_asset_class!=null) {
            b2_asset_class=b2_asset_class.trim();
        }
	}
	
	public String getCVSRow(){
		StringBuffer buffer=new StringBuffer("");
        buffer.append(b2_asset_class).append(";");
		buffer.append(owner_code).append(";");
		buffer.append(owner_name).append(";");
		buffer.append(cus_acc_no).append(";");
		buffer.append(amount_eur).append(";");
		buffer.append(request_no);
		return buffer.toString();
	}
	
	public String getHfDateAppendix(){
		StringBuffer buffer=new StringBuffer("");
		buffer.append(hf_date_from).append(";");
		buffer.append(hf_date_until);
		return buffer.toString();
	}
	
	public boolean identified(long ident){
		return (id==ident);
	}

}
