/*
 * Created on 2007.11.26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.data;

import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Podaci o instrumentima osiguranja plasmana
 * 
 * @author hraamh
 *
 */
public class CollateralInsuranceInstrumentData {
	public static String cvsident =  "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/data/CollateralInsuranceInstrumentData.java,v 1.5 2008/12/11 12:49:05 hraamh Exp $";

	/**Datum obrade*/
	public Date procedure_date=null;
	/**Sifra organizacijske jedinice*/
	public String org_uni_code=null;
	/**Sifra proizvoda*/
	public String product_code=null;
	/**Broj partije*/
	public String cus_acc_no=null;
	/**Sifra tipa kolaterala*/
	public String col_typ_code=null;
	/**Naziv tipa kolaterala*/
	public String col_typ_name=null;
	/**Prioritet zaloga*/
	public String priority=null;
	/**Trzisna vrijednost c.o.*/
	public BigDecimal col_amount=null;
	/**Valuta trzisne vrijednosti c.o.*/
	public String col_cur_num=null;
	/**Iznos tereta*/
	public BigDecimal amount=null;
	/**Originalna valuta upisanog tereta*/
	public String amount_cur_num=null;
	/**Datum vazenja tereta od*/
	public Date hf_date_from=null;
	/**Datum vazenja tereta do*/
	public Date hf_date_until=null;
	/**Uloga u predmetu*/
	public String role=null;
    /** Partija kolaterala */
    public String col_num=null;
	
	public String toString(){
		String result="";
		result+="\nprocedure_date "+procedure_date;
		result+="\norg_uni_code "+org_uni_code;
		result+="\nproduct_code "+product_code;
		result+="\ncus_acc_no "+cus_acc_no;
        result+="\ncol_num "+col_num;
		result+="\ncol_typ_code "+col_typ_code;
		result+="\ncol_typ_name "+col_typ_name;
		result+="\npriority "+priority;
		result+="\ncol_amount "+col_amount;
		result+="\ncol_cur_num "+col_cur_num;
		result+="\namount "+amount;
		result+="\namount_cur_num "+amount_cur_num;
		result+="\nhf_date_from "+hf_date_from;
		result+="\nhf_date_until "+hf_date_until;
		result+="\nrole "+role;
		return result;
	}
	
	public String toStringLine(String delimiter){
		String result="";
		result+=delimiter+procedure_date;
		result+=delimiter+org_uni_code;
		result+=delimiter+product_code;
		result+=delimiter+cus_acc_no;
        result+=delimiter+col_num;
		result+=delimiter+col_typ_code;
		result+=delimiter+col_typ_name;
		result+=delimiter+priority;
		result+=delimiter+col_amount;
		result+=delimiter+col_cur_num;
		result+=delimiter+amount;
		result+=delimiter+amount_cur_num;
		result+=delimiter+hf_date_from;
		result+=delimiter+hf_date_until;
		result+=delimiter+role;
		return result;
	}
	
	public String toFormatedStringLine(String delimiter){
		String result="";
		result+=delimiter+DateUtils.getDDMMYYYY(procedure_date);
		result+=delimiter+StringUtils.generateStringWithBlanks(org_uni_code, 3, true);
		result+=delimiter+StringUtils.generateStringWithBlanks(product_code, 3, false);
		result+=delimiter+StringUtils.generateStringWithBlanks(cus_acc_no, 10, true);
        result+=delimiter+StringUtils.generateStringWithBlanks(col_num, 20, true);
		result+=delimiter+StringUtils.generateStringWithBlanks(col_typ_code, 4, true);
		//result+=delimiter+StringUtils.generateStringWithBlanks(col_typ_name, 30, true);
        result+=delimiter+StringUtils.generateBlankString(4);
		result+=delimiter+StringUtils.generateStringWithBlanks(priority, 2, true);
		result+=delimiter+StringUtils.generateStringWithBlanks(col_amount, 20, false);
		result+=delimiter+StringUtils.generateStringWithBlanks(col_cur_num, 3, false);
		result+=delimiter+StringUtils.generateStringWithBlanks(amount, 20, false);
		result+=delimiter+StringUtils.generateStringWithBlanks(amount_cur_num, 3, false);
		result+=delimiter+DateUtils.getDDMMYYYY(hf_date_from);
		result+=delimiter+DateUtils.getDDMMYYYY(hf_date_until);
		if(role==null){
			result+=delimiter+" ";
		}else{
			result+=delimiter+StringUtils.generateStringWithBlanks(role, 1, true);
		}
		
		return result;
	}
}
