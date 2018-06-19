 
package hr.vestigo.modules.collateral.jcics.co40;
import hr.vestigo.modules.collateral.common.yoy6.CollListQData;
import hr.vestigo.modules.collateral.common.yoy6.YOY60;
import hr.vestigo.modules.collateral.common.yoy9.YOY90;

import java.math.BigDecimal;
import java.sql.*;

public class CollHeadNewOwner {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co40/CollHeadNewOwner.java,v 1.3 2009/11/11 08:55:46 hramlo Exp $";     

  
      public BigDecimal COL_HEA_ID = null;
      public BigDecimal COL_HEA_ID_NEW=null;
      public Timestamp Coll_txtUserLock = null;
      public String Coll_txtCode = null;
      public String STATUS = null;
      public String SPEC_STATUS = null;
      public Timestamp Coll_txtOpeningTs = null;
      public BigDecimal EVE_ID=null; 
      public Timestamp USER_LOCK = null;
     // public BigDecimal cde_cus_id=null;
    
      //COLL_HEAD

      
  /*    :(chno.SPEC_STATUS),  
      :(chno.USE_OPEN_ID),
      :(chno.USE_ID),
      :(chno.Coll_txtOpeningTs),
      :(chno.USER_LOCK),
      :(chno.EVE_ID),
      :(chno.Coll_txtEligibility),
      :(chno.Coll_txtAcumBuyValue),
      :(chno.ORIGIN_ORG_UNI_ID),
      :(chno.ORG_UNI_ID),
      :(chno.USE_ID_VER),
      :(chno.USE_ID_AUT),
      :(chno.VERIFICATION_TS),
      :(chno.AUTORIZATION_TS),
      :(chno.MORTGAGE_STATUS),
      :(chno.COLLATERAL_STATUS),
      :(chno.WORKFLOW_INDIC),
      :(chno.COVER_INDIC),
      :(chno.VER_SEND_TS),
      :(chno.BASIC_DATA_STATUS), 
      :(chno.COLL_DATA_STATUS),
      :(chno.COL_CAT_ID),
      :(chno.USE_ID_VER_SND),
      :(chno.FINANCIAL_FLAG),
      :(chno.ColRba_txtEligibility),
      :(chno.KolLow_txtEligibility),
      :(chno.Kol_txtRbaEligDsc),
      :(chno.Coll_txtNumOf),
      :(chno.Coll_txtB1Eligibility),
      :(chno.Coll_txtB2IRBEligibility)
    */
 
      public Date Coll_txtAcouBDate = null;
      public BigDecimal Coll_txtAcouBValue = null;
      public Date Coll_txtAcouDate = null;
//    public String Coll_txtAcouPerAco = null;
//    public String Coll_txtAcouPerCal = null;
      public BigDecimal Coll_txtAcouValue = null;
      public String Coll_txtAddData = null;
      public String Coll_txtAmortAge = null;
      public BigDecimal Coll_txtAmortValCal = null;
      public Date Coll_txtAvailDate = null;
      public String Coll_txtAvailPerCal = null;
      public BigDecimal Coll_txtAvailValue = null;
      public Date Coll_txtBptcDate = null;
      public BigDecimal Coll_txtBptcValu = null;
      public BigDecimal Coll_txtCollHnbPonder = null;
      public BigDecimal Coll_txtCollMvpPonder = null;
      public BigDecimal Coll_txtCollRzbPonder = null;
      public String Coll_txtCollTypeCode = null;
      public String Coll_txtComDoc = null;
      public Date Coll_txtDateFrom = null;
      public Date Coll_txtDateRecDoc = null;
      public Date Coll_txtDateToDoc = null;
      public Date Coll_txtDateToLop = null;   
      public Date Coll_txtDateRecLop = null; 
      public String Coll_txtRecLop = null; 
      public Date Coll_txtDateUnti = null;
      public Date Coll_txtDatnFrom = null;
      public Date Coll_txtDatnUnti = null;
      public Date Coll_txtEstnDate = null;
      public BigDecimal Coll_txtEstnValu = null;
      public BigDecimal Coll_txtHfsValue = null;
      public Date Coll_txtHfsValueDate = null;
      public Date Coll_txtHfsDateLastOne = null;
      public BigDecimal Coll_txtHfsValueLastOne = null;
      public Date Coll_txtLiquDate = null;
      public BigDecimal Coll_txtLiquValu = null;
      public String Coll_txtMissingDoc = null;
      public Date Coll_txtNepoDate = null;
      public String Coll_txtNepoPerCal = null;
      public BigDecimal Coll_txtNepoValue = null;
      public Date Coll_txtNomiDate = null;
      public String Coll_txtNomiDesc = null;
      public BigDecimal Coll_txtNomiValu = null;
//    public String Coll_txtRevaAutMan = null;
      public Date Coll_txtRevaBDate = null;
      public String Coll_txtRevaBDateAM = null;
      public BigDecimal Coll_txtRevaBValue = null;
      public BigDecimal Coll_txtRevaCoefMan = null;
      public Date Coll_txtRevaDate = null;
      public String Coll_txtRevaDateAM = null;
//    public Date Coll_txtRevaDateMan = null;
//    public BigDecimal Coll_txtRevaValue = null;
      public Date Coll_txtSumLimitDat = null;
      public BigDecimal Coll_txtSumLimitVal = null;
      public Date Coll_txtSumPartDat = null;
      public BigDecimal Coll_txtSumPartVal = null;
      public BigDecimal Coll_txtThirdRight = null;
      public Date Coll_txtThirdRightDate = null;
      public BigDecimal Coll_txtThirdRightInNom = null;  
      public BigDecimal COL_TYPE_ID = null;
      public BigDecimal REAL_EST_EUSE_ID = null;
      public BigDecimal REAL_EST_NM_CUR_ID = null;
      public BigDecimal CUS_ID = null;
      public BigDecimal COLL_CUS_ID = null;  
      public BigDecimal AMORT_PER_CAL_ID = null;
      public BigDecimal THIRD_RIGHT_CUR_ID = null;
      public BigDecimal COL_PLACE = null;
      public BigDecimal COL_COUNTY = null;
      public BigDecimal COL_DISTRICT = null;
      public BigDecimal COL_RESI_QUAR = null;
      public BigDecimal USE_OPEN_ID = null;
      public BigDecimal USE_ID = null;
      public String INSPOL_IND = null;
      public String Coll_txtDesc = null; 
      public java.sql.Timestamp CREATE_DATE_TIME = null;
      public Date CURR_DATE = null;   
      public String recept = null;
      public BigDecimal eve_typ_id = null;
 
      public BigDecimal Coll_txtAcumBuyValue = null;
      public String Coll_txtEligibility = null;
      

      public BigDecimal ORIGIN_ORG_UNI_ID = null;
      public BigDecimal ORG_UNI_ID = null;
      public String MORTGAGE_STATUS = null;
      public String COLLATERAL_STATUS = null; 
      public String WORKFLOW_INDIC = null;
      public String COVER_INDIC = null;
      public String BASIC_DATA_STATUS = null;
      public String COLL_DATA_STATUS = null;
      public BigDecimal COL_CAT_ID = null;
      

      public String save_ver_aut_flag = null; 


      public BigDecimal USE_ID_VER_SND = null;    
      public String FINANCIAL_FLAG = null;
      

      public String COLL_CATEGORY = null;
       

      public String KolLow_txtEligibility = null;
      public String ColRba_txtEligibility = null;
      

      public String Kol_txtRbaEligDsc = null; 
      

//    public BigDecimal loa_own_id = null;
      public Integer Coll_txtNumOf = null;
      

      public String Coll_txtB1Eligibility = null;
      public String Coll_txtB2IRBEligibility = null;


      public String action = null;
      public BigDecimal COL_HEA_ID_ARH = null;
        public BigDecimal USE_ID_VER = null;
        public BigDecimal USE_ID_AUT = null;
        public Timestamp VERIFICATION_TS = null;
        public Timestamp AUTORIZATION_TS = null;
        public Timestamp VER_SEND_TS = null;
        
        //Podaci za coll_hf_prior
        public BigDecimal COLL_HF_PRIOR_ID=null;
        public BigDecimal COLL_HF_PRIOR_ID_NEW=null;
        public BigDecimal HF_TABLE_ID=null;           
        public BigDecimal HF_REF_ID=null;
        public BigDecimal HF_COLL_HEAD_ID=null;
        public BigDecimal HF_OWN_CUS_ID=null;
        public String CollHfPriorDialog_txtHfRegisterNo = null;
        public String CollHfPriorDialog_txtHfOwnCode = null;  
        public String CollHfPriorDialog_txtHfOwnFname = null;        
        public String CollHfPriorDialog_txtHfOwnLname = null;
        public BigDecimal HF_HFC_ID=null;
        public String maxPriority = null; 
        public String CollHfPriorDialog_txtHfNotaryAgr = null;  
        public BigDecimal HF_NOTARY_PLACE_ID=null;
        public Date CollHfPriorDialog_txtHfNotaryDate = null;    
        public BigDecimal HF_NOTARY=null;   
        public String CollHfPriorDialog_txtHfNotFname = null;  
        public String CollHfPriorDialog_txtHfNotLname = null; 
        public Date CollHfPriorDialog_txtHfDateReciv = null; 
        public String CollHfPriorDialog_txtHfCourtDecis = null; 
        public Date CollHfPriorDialog_txtHfDateExtract = null;
        public String CollHfPriorDialog_txtLandRegn = null;
        public String CollHfPriorDialog_txtLandRegnNew = null;
        public BigDecimal CollHfPriorDialog_JUDGE_ID=null;
        public String CollHfPriorDialog_txtJudgeFname = null;
        public String CollHfPriorDialog_txtJudgeLname = null;
        public BigDecimal CollHfPriorDialog_COURT_ID=null;
        public BigDecimal HF_OFFI_LRD=null; 
        public String CollHfPriorDialog_txtHfOffildFname = null;  
        public String CollHfPriorDialog_txtHfOffildLname = null;
        public BigDecimal HF_REC_LOP_ID=null;
        public String CollHfPriorDialog_txtHfAddData = null;
        public BigDecimal CollHfPriorDialog_txtHfAmount=null;   
        public BigDecimal HF_CUR_ID=null;
        public BigDecimal CollHfPriorDialog_txtAmountRef=null;
        public BigDecimal CUR_ID_REF=null;
        
        public BigDecimal CollHfPriorDialog_txtExcRatRef=null;
        public Date CollHfPriorDialog_txtExcRatRefDate = null;
        public BigDecimal HF_DRAW_AMO=null;
        public Date CollHfPriorDialog_txtHfDateHfcFrom=null;   
        public Date CollHfPriorDialog_txtHfDateHfcUntil=null;   
        public BigDecimal Hf_Avail_Amount=null; 
        public BigDecimal Hf_Draw_Amo_Ref=null;
        public BigDecimal AvailAmoRef=null;
        public Date AvailDat=null;
        public BigDecimal Draw_Bamo=null;
        public BigDecimal AvailBAmo=null;
        public BigDecimal DrawBAmoRef=null;
        public BigDecimal HfAvailBAmoRef=null;
        public Date AvailBDat=null;
        public Date Val_Date_Turn=null;
        public Date Val_Date_Proc=null;
        public Date Hf_Date_Hfc_From=null;
        public Date Hf_Date_Hfc_Until=null;
        public String Hf_status=null;
        public String Hf_spec_stat=null;
        public Date DateFrom=null;         
        public Date DateUntil=null; 
        public BigDecimal USE_OPEN_ID_HF=null;
        public BigDecimal USE_ID_HF=null; 
        public Timestamp Opening_Ts=null;
        public Timestamp User_Lock=null;
        public BigDecimal Eve_id=null;
        public String Bank_sign=null;
        public BigDecimal CollHfPrior_HOW_COVER=null;         
        public String CollHfPrior_txtIsPartAgreem=null;
        public BigDecimal residuecollWorthCO=null;
        public String CollHFP_txtRecLop=null;
        public Date CollHFP_txtDateToLop=null;
        public Date CollHFP_txtDateRecLop=null;
        public String STATUS_HF=null;
        public String CollHFP_txtVehConNum=null;
        public String Kol_txtFrameAgr=null;
        //public BigDecimal fra_agr_id=null;
        public BigDecimal REG_INS=null;
        public String HfPror_txtRegPlace=null;
        public String HfPror_txtConcNum=null;
        public Date HfPror_txtConcDate=null;
        public BigDecimal REG_COU_ID=null;
         
        //LOAN_BENEFICIARY
        public BigDecimal loan_ben_id=null;
        public BigDecimal loan_ben_id_new=null;
        public String register_no=null;
        public BigDecimal cus_id=null;
        public String acc_no=null;
        public BigDecimal la_acc_id=null;
        public BigDecimal coll_hf_prior_id=null;
        //public BigDecimal col_hea_id=null;
        public Integer ser_num=null;
        public Date date_from=null;
        public Date date_until=null;
        public String status=null;
        public String spec_status=null;
        public BigDecimal use_open_id=null;
        public BigDecimal use_id=null;
        public Timestamp opening_ts=null;
        public Timestamp user_lock=null;
        public String request_no=null;
        public String priority_no=null;
        public String acc_no_old=null;
        public BigDecimal fra_agr_id=null;
        public String aps_rqst_no=null;
        public String inspol_ind=null;
        public BigDecimal ip_cus_id=null;
        public String krad_admin_ind=null;
        public BigDecimal kred_admin_use_id=null;
        public Timestamp kred_admin_ts=null;
        
        //COLL_VEHICLE
        
        public BigDecimal col_veh_id=null;
        //col_hea_id
        public BigDecimal veh_group_id=null; 
        public BigDecimal veh_subgr_id=null;
        public String veh_made=null;
        public String veh_type=null;
        public String veh_model=null;
        public String veh_colour=null;
        public String veh_chassis=null;
        public String veh_base_purpose=null;
        public String veh_producer=null;
        public BigDecimal veh_cou_id_prod=null;
        public String veh_made_year=null;
        public Date veh_first_reg=null;
        public BigDecimal veh_sit_no=null;
        public BigDecimal veh_sta_no=null;
        public BigDecimal veh_lyi_no=null;
        public BigDecimal veh_allow_load=null;
        public BigDecimal veh_empty_mass=null;
        public BigDecimal veh_max_allo_mass=null;
        public BigDecimal veh_max_velocity=null;
        public BigDecimal veh_all_loa_fr=null;
        public BigDecimal veh_all_loa_mi=null;
        public BigDecimal veh_all_loa_ba=null;
        public BigDecimal veh_axis_no=null;
        public BigDecimal veh_pivot_axis_no=null;
        public String veh_engine_type=null;
        public BigDecimal veh_power_kw=null;
        public BigDecimal veh_r_min=null;
        public String veh_ccm=null;
        public BigDecimal veh_length=null;
        public BigDecimal veh_width=null;
        public BigDecimal veh_height=null;
        public BigDecimal veh_volume=null;
        public BigDecimal veh_no_wheel=null;
        public String veh_tracks=null;
        public String veh_tyres_fr=null;
        public String veh_tyres_mi=null;
        public String veh_tyres_ba=null;
        public String veh_brake=null;
        public String veh_hook=null;
        public String veh_windlass=null;
        public BigDecimal veh_num_door=null;
        public BigDecimal veh_km_trav=null;
        public String veh_equipment=null;
        public String veh_licence=null;
        public String veh_veh_licence=null;
        public String veh_plate =null;
        public String veh_f_plate=null;
        public Date veh_f_plate_dat=null;
        public Date lic_date_to=null;
        public Date veh_date_lic=null;
        public Date veh_date_lic_to=null;
        public String veh_lic_return=null;
        public String veh_lic_reamark=null;
        public String veh_insurance=null;   
        public String veh_con_num=null;
        public String fid_typ=null;
        public String veh_state=null;
        public String veh_kasko=null;
        public Date veh_insdate=null;
        public BigDecimal veh_ins_id=null;
        public String veh_vin_num=null;
        public String lic_ret_own=null;
        public Date lic_ret_own_dat=null;
        public String lic_ret_who=null;
        public String lic_ret_own_rem=null;
        
        //COLL_RESTATE
        
        public BigDecimal col_res_id=null;  
        //public BigDecimal col_hea_id=null;
        public BigDecimal real_est_type=null;
        public BigDecimal rb_est_nom_val=null;
        public Date rb_est_nom_dat=null;
        public BigDecimal rl_est_nom_val=null;
        public Date rl_est_nom_dat=null;
        public BigDecimal real_est_sqrm2=null;
        public BigDecimal real_est_pricm2=null;
        public BigDecimal real_est_court_id=null;
        public BigDecimal real_est_cada_munc=null;
        public String real_est_land_regn=null;  
        public String real_est_land_part=null;
        public String real_est_pdesc=null;
        public Integer byear=null;
        public Integer ryear=null;
        public String street=null;
        public String housenr=null;
        public BigDecimal reva_re_coef_id=null;
        public BigDecimal original_val=null;
        public Date orig_val_date=null;
        public BigDecimal building_val=null;
        public Date build_val_date=null;
        public BigDecimal pol_map_id_ad=null;
        public BigDecimal pos_off_id_ad=null;
        public String coown=null;                         
        public String real_est_land_sub=null; 
        public BigDecimal re_cada_munc_st=null;       
        public String re_land_part_st=null;
        public String build_perm_ind=null;
        public BigDecimal new_build_val=null;
        public BigDecimal re_sub_type_id=null;
        public BigDecimal purpose=null;
        public String ok_own=null;
        public String build_perm=null;
        public String legality=null;
        
        //COLL_MOVABLE
        
        public BigDecimal col_mov_id=null; 
        public BigDecimal mov_typ_id=null; 
        public String mov_model=null; 
        public String mov_ser_num=null; 
        public String mov_made_year=null; 
        public String mov_reg_sign=null;
        public String mov_hr_reg=null;
        public String mov_dsc=null;
        public String mov_address=null;
        public BigDecimal mov_place_id=null;
        public String mov_place=null;
        public Date mov_ins_date=null;
        
        //COLL_SUPPLY
        
        public BigDecimal col_sup_id=null; 
        public String sup_dsc=null;
        public String sup_keeper=null; 
        public String sup_loc=null; 
        public String sup_address=null; 
        public BigDecimal sup_place_id=null;
        public String sup_place=null;
        public Date sup_ins_date=null;
        public String sup_min_ind=null;
        public BigDecimal sup_min_amount=null;
        
        //COLL_VESSEL
        
        public BigDecimal col_ves_id=null; 
        public BigDecimal ves_typ_id=null;
        public BigDecimal har_off_id=null; 
        public String ves_name=null;
        public String ves_made_year=null;
        public String ves_num_engine=null;
        public String ves_sign=null;
        public BigDecimal ves_brutto=null;
        public BigDecimal ves_netto=null;
        public String ves_number=null;
        public String ves_licence=null;
        public String ves_colour=null;                           
        public BigDecimal ves_power_kw=null;
        public String ves_equipment=null;
        public BigDecimal har_sec_id=null;
        public String ves_dsc=null;
        public Date ves_ins_date=null;
        
        
        //COLL_VRP
        
        public BigDecimal col_vrp_id=null;         
        public BigDecimal col_in2_id=null;
        public BigDecimal nom_cur_id=null;
        public BigDecimal one_nom_amo=null;
        public BigDecimal one_nom_amo_kn=null; 
        public BigDecimal one_mar_amo=null; 
        public BigDecimal one_mar_amo_kn=null; 
        public BigDecimal one_mar_amo_per=null;
        public BigDecimal num_of_sec=null;
        public BigDecimal nominal_amount=null; 
        public BigDecimal market_amount=null; 
        public BigDecimal nominal_amount_kn=null; 
        public BigDecimal market_amount_kn=null;
        public String stop_sell_ind=null;
        public BigDecimal stop_sell_period=null;
        public BigDecimal sto_mar_id=null;
        public Date price_date=null;
        public String currency_clause=null;
        public String iss_cus_id=null;
        public String tem_kap_per=null;

        
        //INSURANCE_POLICY
        
        public BigDecimal IP_ID=null;
        public BigDecimal IP_ID_NEW=null;
        public String IP_CODE=null;
        public BigDecimal IP_TYPE_ID=null;
        public BigDecimal IP_IC_ID=null;
        public BigDecimal IP_CONTRACTOR=null;
        public BigDecimal IP_POL_HOLDER=null;
        public String IP_PLACE=null;
        public BigDecimal IP_SECU_VAL=null;
        public BigDecimal IP_CUR_ID=null;
        public Date IP_DATE_SEC_VAL=null;
        public String IP_REPLACE=null;
        public Date IP_VALI_FROM=null;
        public Date IP_VALI_UNTIL=null;
        public String IP_ACT_NOACT=null;
        public String IP_SPEC_STAT=null;        
        public Timestamp OPENING_TS=null;
        
        
        //COLL_INSPOLICY
        
        public BigDecimal col_ins_id = null; 
        //col_hea_id
        public BigDecimal ip_type_id = null; 
        public BigDecimal con_cus_id = null;
        public String con_reg_no = null;
        public String con_code = null;
        public String con_data = null;
        public BigDecimal ins_cus_id = null;
        public String ins_reg_no = null;
        public String ins_code = null;
        public String ins_data = null;
        public String ip_code = null;  
        //public BigDecimal ip_cus_id = null;
        public Date ip_issue_date =null;
        public Date ip_valid_from =null;
        public Date ip_valid_until =null;
        public BigDecimal ip_nom_cur_id = null;
        public BigDecimal ip_nom_value = null;
        public BigDecimal ip_amount = null;
        public Date ip_paid_from =null;
        public Date ip_paid_until =null;
        public String ip_status = null;
        public BigDecimal ip_amo_cur_id = null;
        public String ip_spec_stat = null;
        
        //COLL_CASHDEP
        
        public BigDecimal col_cas_id = null; 
        //public BigDecimalcol_hea_id = null; 
        public BigDecimal cde_typ_id = null; 
        public BigDecimal cde_cus_id = null;  
        public String cde_reg_no = null;
        public String cde_swift_add = null;
        public String cde_bank = null; 
        public Date cde_dep_from= null;
        public Date cde_dep_unti = null;
        public BigDecimal cde_amount = null;
        public BigDecimal cde_cur_id = null;
        public BigDecimal cde_period = null;
        public String cde_prolong = null;
        public String cde_account= null;
        
       
    
    
    
    

}
