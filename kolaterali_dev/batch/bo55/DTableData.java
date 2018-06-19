package hr.vestigo.modules.collateral.batch.bo55;
import hr.vestigo.modules.collateral.common.yoy6.CollListQData;
import hr.vestigo.modules.collateral.common.yoy6.YOY60;
import hr.vestigo.modules.collateral.common.yoy9.YOY90;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class DTableData {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo55/DTableData.java,v 1.9 2017/06/05 07:16:47 hrazst Exp $";     


    Date LOAD_DATE_FROM=null;
    Date LOAD_DATE_UNTIL=null;
    Date LOAD_DATE=null;
    BigDecimal REM_RE_NOMI_VALU=null;
    
    
    BigDecimal col_hea_id=null;
    BigDecimal col_type_id=null;
    String col_num=null;
    String col_desc=null; 
    BigDecimal real_est_euse_id=null; 
    BigDecimal real_est_estn_valu=null; 
    Date real_est_estn_date=null;
    Date real_est_datn_from=null;
    Date real_est_datn_unti=null; 
    String real_est_nomi_desc=null;
    BigDecimal real_est_nomi_valu=null;
    Date real_est_nomi_date=null;
    BigDecimal real_est_liqu_valu=null;
    Date real_est_liqu_date=null;
    BigDecimal real_est_bptc_valu=null;
    Date real_est_bptc_date=null;
    BigDecimal real_est_nm_cur_id=null;
    BigDecimal cus_id=null; 
    BigDecimal coll_cus_id=null;
    Date date_to_lop=null;
    Date date_rec_lop=null;
    String rec_lop=null;
    String com_doc=null;
    String missing_doc=null;
    Date date_to_doc=null;
    Date date_rec_doc=null;
    BigDecimal reva_coef=null;     
    Date reva_date=null;
    String reva_date_am=null;
    BigDecimal reva_bvalue=null;
    Date reva_bdate=null;
    String reva_bdate_am=null;
    String amort_age=null;
    BigDecimal amort_per_cal_id=null;
    BigDecimal amort_val_cal=null;
    BigDecimal nepo_value=null;
    Date nepo_date=null;     
    String nepo_per_cal=null;
    BigDecimal third_right=null;
    BigDecimal third_right_cur_id=null;
    BigDecimal third_right_nom=null;
    Date third_right_date=null;  
    BigDecimal hfs_value=null;
    Date hfs_value_date=null;
    BigDecimal hfs_value_last_one=null;
    Date hfs_date_last_one=null;
    BigDecimal weigh_value=null;
    Date weigh_date=null;
    BigDecimal weigh_bvalue=null;
    Date weigh_bdate=null;
    BigDecimal sum_limit_val=null;
    Date sum_limit_dat=null;
    BigDecimal sum_part_val=null;
    Date sum_part_dat=null;
    BigDecimal avail_value=null;
    String avail_per_cal=null;
    Date avail_date=null;
    String inspol_ind=null;
    BigDecimal col_place=null;
    BigDecimal col_county=null;
    BigDecimal col_district=null;
    BigDecimal col_resi_quar=null;
    BigDecimal coll_mvp_ponder=null;
    BigDecimal coll_hnb_ponder=null;   
    BigDecimal coll_rzb_ponder=null;       
    Date real_est_date_from=null;
    Date real_est_date_unti=null;
    String real_est_add_data=null;
    String real_est_status=null;
    String real_est_spec_stat=null;
    BigDecimal use_open_id=null;   
    BigDecimal use_id=null;        
    Timestamp opening_ts=null;    
    Timestamp user_lock=null;
    BigDecimal eve_id=null;
    String bank_sign=null;
    String eligibility=null;
    BigDecimal acum_buy_value=null;
    BigDecimal origin_org_uni_id=null;
    BigDecimal org_uni_id=null;
    BigDecimal use_id_ver=null;
    BigDecimal use_id_aut=null;
    Timestamp verification_ts=null;
    Timestamp autorization_ts=null;
    String mortgage_status=null;
    String collateral_status=null;
    String workflow_indic=null;
    String cover_indic=null;
    Timestamp ver_send_ts=null;
    String basic_data_status=null;
    String coll_data_status=null;
    BigDecimal col_cat_id=null;
    BigDecimal use_id_ver_snd=null;
    String financial_flag=null;
    BigDecimal thi_last_one=null;
    Date thi_date_last_one=null;
    Date thi_date_last_unt=null;
    String rba_eligibility=null;
    String law_eligibility=null;
    String rba_elig_dsc=null;
    String b1_eligibility=null;
    String b2_irb_elig=null;
    String free_status=null;
    Integer number=null;
    String nd_eligibility=null;
    String crm_hnb=null;
    String used_in_recovery=null;
    String kind_of_recovery=null;
    BigDecimal full_amount=null;
    BigDecimal full_amount_cur_id=null;
    BigDecimal recovery_amount=null;
    BigDecimal recovery_cur_id=null;
    String recovery_comment=null;
    Date recovery_date=null;
    BigDecimal recovery_rate=null;
    String b2_irb_insag_elig=null;
    String b2_hnb_insag_elig=null;
    BigDecimal use_id_co=null;
    Timestamp chg_nom_val_proc_ts=null;
    String real_est_nom_type=null;
    String prec_exec_est=null;
    String correct_value=null;
    String respect_deadline=null;
    String prof_to_rba=null;
    String prof_to_client=null;
    Integer col_eco_life=null;
    String real_est_estn_int=null;
    String est_type=null;
    String met_est_1=null;
    String met_est_2=null;
    String coll_risk=null;
    String non_ins_reason=null;
    String ins_cov_coll=null;
    String recovery_proc_stat=null;
    BigDecimal gctc_id=null;
    BigDecimal endorsement_type_id=null;
    BigDecimal object_type_id=null;
    BigDecimal property_type_id=null;
    String contract_type=null;
    BigDecimal realization_amount=null;
    BigDecimal realization_cur_id=null;
    Date realization_date=null;
    String realization_kind=null;
    BigDecimal realization_cost=null;
    String part_of_complex=null;
    BigDecimal realization_cost_cur_id=null;
    String for_sale=null;
    Date for_sale_date=null;
    String takeover_from_bank=null;
    //COLL_HF_PRIOR

    public BigDecimal COLL_HF_PRIOR_ID=null;
    public BigDecimal HF_TABLE_ID=null;           
    public BigDecimal HF_REF_ID=null;
    public BigDecimal HF_COLL_HEAD_ID=null;
    public BigDecimal HF_OWN_CUS_ID=null;    
    public String HF_REGISTER_NO = null;
    public String HF_OWN_CODE = null;  
    public String HF_OWN_FNAME = null;        
    public String HF_OWN_LNAME = null;
    public BigDecimal HF_HFC_ID=null;    
    public String HF_PRIORITY = null; 
    public String HF_NOTARY_AGR = null;  
    public BigDecimal HF_NOTARY_PLACE_ID=null;
    public Date HF_NOTARY_DATE = null;    
    public BigDecimal HF_NOTARY=null;   
    public String HF_NOT_FNAME = null;  
    public String HF_NOT_LNAME = null; 
    public Date HF_DATE_RECIV = null; 
    public String HF_COURT_DECIS = null; 
    public Date HF_DATE_EXTRACT = null;
    public String LAND_REGN = null;
    public String LAND_REGN_NEW = null;
    public BigDecimal JUDGE_ID=null;
    public String JUDGE_FNAME = null;
    public String JUDGE_LNAME = null;
    public BigDecimal COURT_ID=null;
    public BigDecimal HF_OFFI_LRD=null;    
    public String HF_OFFILRD_FNAME = null;  
    public String HF_OFFILRD_LNAME = null;
    public BigDecimal HF_REC_LOP_ID=null;
    public String HF_ADD_DATA = null;
    public BigDecimal HF_AMOUNT=null;   
    public BigDecimal HF_CUR_ID=null;
    public BigDecimal AMOUNT_REF=null;
    public BigDecimal CUR_ID_REF=null;    
    public BigDecimal EXC_RAT_REF=null;
    public Date EXC_RAT_REF_DATE = null;
    public BigDecimal HF_DRAW_AMO=null;  
    public BigDecimal HF_AVAIL_AMO=null; 
    public BigDecimal HF_DRAW_AMO_REF=null;
    public BigDecimal AVAIL_AMO_REF=null;
    public Date HF_AVAIL_DAT=null;
    public BigDecimal DRAW_BAMO=null;
    public BigDecimal AVAIL_BAMO=null;
    public BigDecimal DRAW_BAMO_REF=null;
    public BigDecimal AVAIL_BAMO_REF=null;
    public Date AVAIL_BDAT=null;
    public Date VAL_DATE_TURN=null;
    public Date VAL_DATE_PROC=null;
    public Date HF_DATE_HFC_FROM=null;   
    public Date HF_DATE_HFC_UNTIL=null;
    public String HF_STATUS=null;
    public String HF_SPEC_STAT=null;
    public Date HF_DATE_FROM=null;         
    public Date HF_DATE_UNTIL=null; 
    //public BigDecimal USE_OPEN_ID=null;
    //public BigDecimal USE_ID=null; 
    //public Timestamp OPENING_TS=null;
    //public Timestamp USER_LOCK=null;
    //public BigDecimal USER_LOCK=null;
    public String BANK_SIGN=null;    
    public BigDecimal HOW_COVER=null;         
    public String IS_PART_AGREEM=null;
    public BigDecimal residuecollWorthCO=null;
    public String REC_LOP=null;
    public Date DATE_TO_LOP=null;
    public Date DATE_REC_LOP=null;
    //public String STATUS=null;
    public String VEH_CON_NUM=null;
    public String AGREEMENT=null;   
    //public BigDecimal FRA_AGR_ID=null;
    public BigDecimal REG_INS=null;
    public String REG_PLACE=null;
    public String CON_NUM=null;
    public Date CON_DATE=null;
    public BigDecimal REG_COU_ID=null;
    public String SINDIC_IND=null;
    public String HBOR_LOAN=null;
    public String RBA_ARANG_SINDIC=null;
    public BigDecimal RBA_SINDIC_PART=null;
    public BigDecimal OTHER_SINDIC_PART=null;
    public BigDecimal RBA_SINDIC_AMOUNT=null;
    public BigDecimal OTHER_SINDIC_AMOUNT=null;
    public String SINDIC_DESC=null;
    public BigDecimal OTHER_SYNDICATE_CUS_ID=null;



    
    //LOAN_BENEFICIARY
    
    public BigDecimal LOAN_BEN_ID=null;
    public String REGISTER_NO=null;
    public BigDecimal CUS_ID=null;
    public String ACC_NO=null;
    public BigDecimal LA_ACC_ID=null;
    //public BigDecimal COLL_HF_PRIOR_ID=null;
    public BigDecimal COL_HEA_ID=null;
    public Integer SER_NUM=null;
    public Date DATE_FROM=null;
    public Date DATE_UNTIL=null;
    public String STATUS=null;
    public String SPEC_STATUS=null;
    public BigDecimal USE_OPEN_ID=null;
    public BigDecimal USE_ID=null;
    public Timestamp OPENING_TS=null;
    //public Timestamp USER_LOCK=null;
    public String REQUEST_NO=null;
    public String PRIORITY_NO=null;
    public String ACC_NO_OLD=null;
    public BigDecimal FRA_AGR_ID=null;
    public String APS_RQST_NO=null;
    public String INSPOL_IND=null;
    public BigDecimal IP_CUS_ID=null;
    public String KRAD_ADMIN_IND=null;
    public BigDecimal KRED_ADMIN_USE_ID=null;
    public Timestamp KRED_ADMIN_TS=null;
    public BigDecimal RVRD=null;
    public BigDecimal RVRD_CUR_ID=null;
    public BigDecimal RVOD=null;
    public BigDecimal RVOD_CUR_ID=null;

    
    //FRAME_AGREEMENT
    
    //public BigDecimal FRA_AGR_ID=null;
    //public BigDecimal CUS_ID=null;
    //public String REGISTER_NO=null;
    public String AGREEMENT_NO=null;
    public BigDecimal AMOUNT=null;
    public BigDecimal CUR_ID=null;
    public BigDecimal AMOUNT_POST=null;
    public BigDecimal AMOUNT_REST=null;
    //public Date DATE_UNTIL=null;
    public String NUM_OF_BILL=null;
    public String ADD_DATA=null;
    public String PROC_STATUS=null;
    public String HIPO_STATUS=null;
    public String LOAN_STATUS=null;
    public String WORKFLOW_INDIC=null;
    public String FINANCIAL_FLAG=null;
    //public BigDecimal USE_OPEN_ID=null;
    public BigDecimal ORG_UNI_OPEN_ID=null;
    //public Timestamp OPENING_TS=null;
    //public BigDecimal USE_ID=null;
    public BigDecimal ORG_UNI_ID=null;
    public BigDecimal USE_ID_VER_SND=null;
    public Timestamp VER_SEND_TS=null;
    public BigDecimal USE_ID_VER=null;
    public Timestamp VERIFICATION_TS=null;
    public String BOE_STOCK_INDICATOR=null;
    //public Timestamp USER_LOCK=null;
    //public String STATUS=null;
    
      
}