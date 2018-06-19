/*
 * Created on 2007.01.30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo06;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author hrazvh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO06Data {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo06/BO06Data.java,v 1.4 2007/05/16 08:28:29 hratar Exp $";
	
	public BigDecimal col_pro_id = null;
	public BigDecimal col_tur_id = null;
	public BigDecimal coll_type_id = null;
	public BigDecimal use_id = null;
	public BigDecimal org_uni_id = null;
	public BigDecimal coll_id = null;
	public BigDecimal col_hea_id = null;
	public BigDecimal real_est_nomi_valu = null;
	public BigDecimal real_est_nm_cur_id = null;
	public BigDecimal col_number = null;
	public BigDecimal amort_pst = null;
	public BigDecimal amort_age_decimal = null;
	public BigDecimal col_turnover_amount = null;
	public BigDecimal col_turnover_amount_proc = null;
	public BigDecimal proc_perc = null;
	public Date proces_date = null;
	public Date value_date = null;
	public Date real_est_nomi_dat = null;
	//public Date val_per_min = null;
	public String val_per_min = null;
	public String amort_flag = "";
	public String amort_age = "";
	public String proc_way = "";
	public String proc_type = "";
	public String proc_status = "";
	public String amort_per = "";
	public String proc_period = "";
	public String accounting_indic = "";
}
