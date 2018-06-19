/*
 * Created on 2006.10.04
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy7;





/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollHeadCusAccNoData {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy7/CollHeadCusAccNoData.java,v 1.5 2011/05/30 11:39:54 hramkr Exp $";
	
	 public java.math.BigDecimal col_hea_id = null;			//Id kolaterala
	 
	 public java.math.BigDecimal use_open_id = null;		//Referent koji je otvorio kolateral
	 
	 public java.math.BigDecimal origin_org_uni_id = null;	//OJ otvaranja kolaterala
	 //Ove gore trebam dobiti
	
	 //Ove dolje ja punim u commonu
	 public java.sql.Date todaySQLDate = null;				//Tekuci datum
	 
	 public java.sql.Timestamp  todaySQLTimestamp= null;
	 
	 public java.math.BigDecimal eve_typ_id = null;
	 
	 public java.math.BigDecimal  cus_id = null;
	 
	 public String cus_acc_no = null;
	 
	 public java.math.BigDecimal ban_pro_typ_id = null;
	 
	 public String ban_pro_typ_name = null;
	 
	 public java.math.BigDecimal ban_rel_typ_id = null;
	 
	 public java.math.BigDecimal eve_id = null;
	 
	 public String currency_type = null;
	 
	 public String currency_indicator = null;
	 
	 public String blockable_cusacc = null;
	 
	 public java.math.BigDecimal real_est_nm_cur_id = null;
	 
	 public String cus_sub_acc_indic = null;
	 
	 public java.math.BigDecimal pro_cat_id = null;  //Product_category
	 
	 public java.math.BigDecimal col_cat_id = null;  //Kategorija kolaterala
	 
	 public java.math.BigDecimal col_typ_id = null;  //vrsta kolaterala
}
