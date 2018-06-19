/*
 * Created on 2006.03.02
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy4;
import java.math.BigDecimal;
import java.sql.*;
/**
 * @author hramkr
 *    
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollHfPriorInsertData { 
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy4/CollHfPriorInsertData.java,v 1.4 2006/08/25 10:00:54 hramkr Exp $";	  
	public BigDecimal COLL_HF_PRIOR_ID = null;
	public BigDecimal HF_TABLE_ID = null;
	public BigDecimal HF_REF_ID  = null;
	public BigDecimal HF_COLL_HEAD_ID = null;
	
	public BigDecimal HF_OWN_CUS_ID = null;
	public String HF_REGISTER_NO = null;    // 910000
	public String HF_OWN_CODE = null;
	public String HF_OWN_LNAME = null;
	
	public BigDecimal HF_HFC_ID = null;  // vrijednost P u system_code_value
	public String HF_PRIORITY = null;	// vrijednost = 1
	public BigDecimal HF_REC_LOP_ID = null;	// vrijednost R u system_code_value
	 
	public BigDecimal HF_AMOUNT = null;	// coll_head.real_est_nomi_valu
	public BigDecimal HF_CUR_ID = null;	// coll_head.real_est_nm_cur_id

	public BigDecimal HF_DRAW_AMO = null;	// KOD 1. PUNJENJA = 0
	public BigDecimal HF_AVAIL_AMO = null;	// KOD 1. PUNJENJA = HF_AMOUNT
	public Date HF_AVAIL_DAT = null;	// current_date
	
	public BigDecimal DRAW_BAMO = null;	// KOD 1. PUNJENJA = 0
	public BigDecimal AVAIL_BAMO = null;	// KOD 1. PUNJENJA = HF_AMOUNT
	public BigDecimal DRAW_BAMO_REF = null;	// KOD 1. PUNJENJA = 0
	public BigDecimal AVAIL_BAMO_REF = null;	// KOD 1. PUNJENJA = HF_AMOUNT
	public Date AVAIL_BDAT = null;	// kod 1. punjenja null	
	
	public Date HF_DATE_HFC_FROM = null;	// current date
	public Date HF_DATE_HFC_UNTIL = null;  // 9999-12-31
	public String HF_STATUS = null;	// vrijednost A
	public String HF_SPEC_STAT = null;	// vrijednost 00
	public Date HF_DATE_FROM = null;	// current date
	public Date HF_DATE_UNTIL = null;  // 9999-12-31	
	
	public BigDecimal USE_OPEN_ID = null;
	public BigDecimal USE_ID = null;
	public Timestamp OPENING_TS = null;
    public Timestamp USER_LOCK = null;
    public BigDecimal EVE_ID = null;

    public java.sql.Timestamp CREATE_DATE_TIME = null;
    public Date CURR_DATE = null;
    
    public String table_id = null;

}
