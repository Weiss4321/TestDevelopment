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
public class CollHeadInsertData {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy4/CollHeadInsertData.java,v 1.21 2017/06/01 12:03:38 hrazst Exp $";

	// output	  
	public BigDecimal COL_HEA_ID = null;
	public Timestamp Coll_txtUserLock = null;
	public String Coll_txtCode = null;
	public String STATUS = null;
	public String SPEC_STATUS = null;
	public Timestamp Coll_txtOpeningTs = null;
	public BigDecimal EVE_ID=null; 
	public Timestamp USER_LOCK = null;

	// input    
    /** ID vrste kolaterala  - potrebno popuniti zbog odreðivanja GCTC polja */
    public BigDecimal COL_TYPE_ID = null; 
    /**ID kategorije kolaterala  - potrebno popuniti zbog odreðivanja GCTC polja */
    public BigDecimal COL_CAT_ID = null;
    /**ID podvrste kolaterala  - potrebno popuniti zbog odreðivanja GCTC polja*/
    public BigDecimal COL_SUB_ID = null; 
    /**ID podvrste kolaterala  - potrebno popuniti zbog odreðivanja GCTC polja*/
    public BigDecimal COL_GRO_ID = null; 
	
	public Date Coll_txtAcouBDate = null;
	public BigDecimal Coll_txtAcouBValue = null;
	public Date Coll_txtAcouDate = null;
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
	public Date Coll_txtRevaBDate = null;
	public String Coll_txtRevaBDateAM = null;
	public BigDecimal Coll_txtRevaBValue = null;
	public BigDecimal Coll_txtRevaCoefMan = null;
	public Date Coll_txtRevaDate = null;
	public String Coll_txtRevaDateAM = null;
	public Date Coll_txtSumLimitDat = null;
	public BigDecimal Coll_txtSumLimitVal = null;
	public Date Coll_txtSumPartDat = null;
	public BigDecimal Coll_txtSumPartVal = null;
	public BigDecimal Coll_txtThirdRight = null;
	public Date Coll_txtThirdRightDate = null;
	public BigDecimal Coll_txtThirdRightInNom = null; 
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
	public String save_ver_aut_flag = null; 

	public BigDecimal USE_ID_VER_SND = null;      
	public String FINANCIAL_FLAG = null;

	public String COLL_CATEGORY = null;

	public String KolLow_txtEligibility = null;
	public String ColRba_txtEligibility = null;

	public String Kol_txtRbaEligDsc = null; 

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

	public String Kol_txtCRMHnb=null;

	/**Korišten u naplati D/N */
	public String used_in_recovery = null;
	/**Vrsta naplate */
	public String kind_of_recovery = null;
	public String kind_of_recovery_desc = null;
	/**Podatak o kupoprodajnoj cijenu */
	public BigDecimal full_amount = null;
	/**Valuta kupoprodajne cijene */
	public BigDecimal full_amount_cur_id = null;
	public String full_amount_cur_codeChar = null;
	/**Dio iznosa Full Amount iz kojeg Banka naplaæuje dugovanje. */
	public BigDecimal recovery_amount = null;
	/**Valuta recovery_amount*/
	public BigDecimal recovery_cur_id = null;
	public String recovery_cur_codeChar = null;
	/**komentar naplate */
	public String recovery_comment = null;
	/**datum prodaje */
	public Date recovery_date = null;
	/**Postotak naplate*/
	public BigDecimal recovery_rate = null;
	/**B2 IRB prihv. sporazuma o osiguranju D/N*/
	public String b2_irb_insag_elig = null;
	/**B2 HNB prihv. sporazuma o osiguranju D/N*/
	public String b2_hnb_insag_elig = null;
	/**Coll. officer koji je prekontrolirao vrijednost kolaterala*/
	public BigDecimal use_id_co = null;
	public String use_id_co_code = null;
	public String use_id_co_name = null;
	/**Vrijeme promjene tržišne vrijednosti kolaterala (garancija,depozit;vrp , polica osig. za mikro kl.) obradom */
	public Timestamp chg_nom_val_proc_ts = null;
	/**Vrsta tržišne vrijednosti*/
	public String real_est_nom_type = null;
	public String real_est_nom_type_desc = null;
	/**Toènost provedbe procjene*/
	public String prec_exec_est = null;
	/**Korektnost vrijednosti*/
	public String correct_value = null;
	/**Poštivanje rokova*/
	public String respect_deadline = null;
	/**Profesionalnost*/
	public String prof_to_rba = null;
	/**Ocjena procjene*/
	public String prof_to_client = null;
	/**Ekonomski vijek kolaterala*/
	public Integer col_eco_life = null;
	/**Interni procjenitelj*/
	public String real_est_estn_int = null;
	/**Tip procjenitelja*/
	public String est_type = null;
	public String est_type_desc = null;
	/**Metoda procjene 1*/
	public String met_est_1 = null;
	/**Metoda procjene 2*/
	public String met_est_2 = null;
	/**Pristup JPP*/
	public String coll_risk = null;
	/**Razlog neosiguranja*/
	public String non_ins_reason = null;
	public String non_ins_reason_desc = null;
	/**Osiguranje pokriva kolateral u potpunosti*/
	public String ins_cov_coll = null;
	/**Oznaèava da li je kolateral u procesu prodaje. */
	public String recovery_proc_stat = null;
	/**GCTC kolaterala*/
	public BigDecimal gctc_id = null;
	public String gctc_code = null;
	public String gctc_desc = null;
	/**Endorsement Type - GCTC*/
	public BigDecimal endorsement_type_id = null;
	public String endorsement_type_code = null;
	public String endorsement_type_desc = null;
	/**Object Type - GCTC*/
	public BigDecimal object_type_id = null;
	public String object_type_code = null;
	public String object_type_desc = null;
	/**Property Type - GCTC*/
	public BigDecimal property_type_id = null;
	public String property_type_code = null;
	public String property_type_desc = null;
	/**Vrsta ugovora*/
	public String contract_type = null;
	public String contract_type_desc = null; 
	
    /**Iznos realizacije*/
    public BigDecimal realization_amount=null;
    /**Valuta realizacije*/
    public BigDecimal realization_cur_id=null;
    /**Datum realizacije*/
    public Date realization_date=null;
    /**Vrsta realizacije*/
    public String realization_kind=null;
    /**Troškovi realizacije*/
    public BigDecimal realization_cost=null;
    /**Oznaka da li je kolateral dio kompleksa*/
    public String part_of_complex=null;
    /**Troškovi realizacije valuta*/
    public BigDecimal realization_cost_cur_id=null;
    /**Kolateral na prodaji*/
    public String for_sale=null;
    /**Datum stavljanja kolaterala na prodaju*/
    public Date for_sale_date=null;
    /**Preuzeto od banke*/
    public String takeover_from_bank=null;  
      
    public String toString(){
        StringBuffer buffy=new StringBuffer();          
        buffy.append("\nCOL_HEA_ID=["+COL_HEA_ID+"]");
        buffy.append("\nColl_txtUserLock=["+Coll_txtUserLock+"]");
        buffy.append("\nColl_txtCode=["+Coll_txtCode+"]");
        buffy.append("\nSTATUS=["+STATUS+"]");
        buffy.append("\nSPEC_STATUS=["+SPEC_STATUS+"]");
        buffy.append("\nColl_txtOpeningTs=["+Coll_txtOpeningTs+"]");
        buffy.append("\nEVE_ID=["+EVE_ID+"]");
        buffy.append("\nUSER_LOCK=["+USER_LOCK+"]");
        buffy.append("\nColl_txtAcouBDate=["+Coll_txtAcouBDate+"]");
        buffy.append("\nColl_txtAcouBValue=["+Coll_txtAcouBValue+"]");
        buffy.append("\nColl_txtAcouDate=["+Coll_txtAcouDate+"]");
        buffy.append("\nColl_txtAcouValue=["+Coll_txtAcouValue+"]");
        buffy.append("\nColl_txtAddData=["+Coll_txtAddData+"]");
        buffy.append("\nColl_txtAmortAge=["+Coll_txtAmortAge+"]");
        buffy.append("\nColl_txtAmortValCal=["+Coll_txtAmortValCal+"]");
        buffy.append("\nColl_txtAvailDate=["+Coll_txtAvailDate+"]");
        buffy.append("\nColl_txtAvailPerCal=["+Coll_txtAvailPerCal+"]");
        buffy.append("\nColl_txtAvailValue=["+Coll_txtAvailValue+"]");
        buffy.append("\nColl_txtBptcDate=["+Coll_txtBptcDate+"]");
        buffy.append("\nColl_txtBptcValu=["+Coll_txtBptcValu+"]");
        buffy.append("\nColl_txtCollHnbPonder=["+Coll_txtCollHnbPonder+"]");
        buffy.append("\nColl_txtCollMvpPonder=["+Coll_txtCollMvpPonder+"]");
        buffy.append("\nColl_txtCollRzbPonder=["+Coll_txtCollRzbPonder+"]");
        buffy.append("\nColl_txtCollTypeCode=["+Coll_txtCollTypeCode+"]");
        buffy.append("\nColl_txtComDoc=["+Coll_txtComDoc+"]");
        buffy.append("\nColl_txtDateFrom=["+Coll_txtDateFrom+"]");
        buffy.append("\nColl_txtDateRecDoc=["+Coll_txtDateRecDoc+"]");
        buffy.append("\nColl_txtDateToDoc=["+Coll_txtDateToDoc+"]");
        buffy.append("\nColl_txtDateToLop=["+Coll_txtDateToLop+"]");
        buffy.append("\nColl_txtDateRecLop=["+Coll_txtDateRecLop+"]");
        buffy.append("\nColl_txtRecLop=["+Coll_txtRecLop+"]");
        buffy.append("\nColl_txtDateUnti=["+Coll_txtDateUnti+"]");
        buffy.append("\nColl_txtDatnFrom=["+Coll_txtDatnFrom+"]");
        buffy.append("\nColl_txtDatnUnti=["+Coll_txtDatnUnti+"]");
        buffy.append("\nColl_txtEstnDate=["+Coll_txtEstnDate+"]");
        buffy.append("\nColl_txtEstnValu=["+Coll_txtEstnValu+"]");
        buffy.append("\nColl_txtHfsValue=["+Coll_txtHfsValue+"]");
        buffy.append("\nColl_txtHfsValueDate=["+Coll_txtHfsValueDate+"]");
        buffy.append("\nColl_txtHfsDateLastOne=["+Coll_txtHfsDateLastOne+"]");
        buffy.append("\nColl_txtHfsValueLastOne=["+Coll_txtHfsValueLastOne+"]");
        buffy.append("\nColl_txtLiquDate=["+Coll_txtLiquDate+"]");
        buffy.append("\nColl_txtLiquValu=["+Coll_txtLiquValu+"]");
        buffy.append("\nColl_txtMissingDoc=["+Coll_txtMissingDoc+"]");
        buffy.append("\nColl_txtNepoDate=["+Coll_txtNepoDate+"]");
        buffy.append("\nColl_txtNepoPerCal=["+Coll_txtNepoPerCal+"]");
        buffy.append("\nColl_txtNepoValue=["+Coll_txtNepoValue+"]");
        buffy.append("\nColl_txtNomiDate=["+Coll_txtNomiDate+"]");
        buffy.append("\nColl_txtNomiDesc=["+Coll_txtNomiDesc+"]");
        buffy.append("\nColl_txtNomiValu=["+Coll_txtNomiValu+"]");
        buffy.append("\nColl_txtRevaBDate=["+Coll_txtRevaBDate+"]");
        buffy.append("\nColl_txtRevaBDateAM=["+Coll_txtRevaBDateAM+"]");
        buffy.append("\nColl_txtRevaBValue=["+Coll_txtRevaBValue+"]");
        buffy.append("\nColl_txtRevaCoefMan=["+Coll_txtRevaCoefMan+"]");
        buffy.append("\nColl_txtRevaDate=["+Coll_txtRevaDate+"]");
        buffy.append("\nColl_txtRevaDateAM=["+Coll_txtRevaDateAM+"]");
        buffy.append("\nColl_txtSumLimitDat=["+Coll_txtSumLimitDat+"]");
        buffy.append("\nColl_txtSumLimitVal=["+Coll_txtSumLimitVal+"]");
        buffy.append("\nColl_txtSumPartDat=["+Coll_txtSumPartDat+"]");
        buffy.append("\nColl_txtSumPartVal=["+Coll_txtSumPartVal+"]");
        buffy.append("\nColl_txtThirdRight=["+Coll_txtThirdRight+"]");
        buffy.append("\nColl_txtThirdRightDate=["+Coll_txtThirdRightDate+"]");
        buffy.append("\nColl_txtThirdRightInNom=["+Coll_txtThirdRightInNom+"]");
        buffy.append("\nCOL_TYPE_ID=["+COL_TYPE_ID+"]");
        buffy.append("\nREAL_EST_EUSE_ID=["+REAL_EST_EUSE_ID+"]");
        buffy.append("\nREAL_EST_NM_CUR_ID=["+REAL_EST_NM_CUR_ID+"]");
        buffy.append("\nCUS_ID=["+CUS_ID+"]");
        buffy.append("\nCOLL_CUS_ID=["+COLL_CUS_ID+"]");
        buffy.append("\nAMORT_PER_CAL_ID=["+AMORT_PER_CAL_ID+"]");
        buffy.append("\nTHIRD_RIGHT_CUR_ID=["+THIRD_RIGHT_CUR_ID+"]");
        buffy.append("\nCOL_PLACE=["+COL_PLACE+"]");
        buffy.append("\nCOL_COUNTY=["+COL_COUNTY+"]");
        buffy.append("\nCOL_DISTRICT=["+COL_DISTRICT+"]");
        buffy.append("\nCOL_RESI_QUAR=["+COL_RESI_QUAR+"]");
        buffy.append("\nUSE_OPEN_ID=["+USE_OPEN_ID+"]");
        buffy.append("\nUSE_ID=["+USE_ID+"]");
        buffy.append("\nINSPOL_IND=["+INSPOL_IND+"]");
        buffy.append("\nColl_txtDesc=["+Coll_txtDesc+"]");
        buffy.append("\nCREATE_DATE_TIME=["+CREATE_DATE_TIME+"]");
        buffy.append("\nCURR_DATE=["+CURR_DATE+"]");
        buffy.append("\nrecept=["+recept+"]");
        buffy.append("\neve_typ_id=["+eve_typ_id+"]");
        buffy.append("\nColl_txtAcumBuyValue=["+Coll_txtAcumBuyValue+"]");
        buffy.append("\nColl_txtEligibility=["+Coll_txtEligibility+"]");
        buffy.append("\nORIGIN_ORG_UNI_ID=["+ORIGIN_ORG_UNI_ID+"]");
        buffy.append("\nORG_UNI_ID=["+ORG_UNI_ID+"]");
        buffy.append("\nMORTGAGE_STATUS=["+MORTGAGE_STATUS+"]");
        buffy.append("\nCOLLATERAL_STATUS=["+COLLATERAL_STATUS+"]");
        buffy.append("\nWORKFLOW_INDIC=["+WORKFLOW_INDIC+"]");
        buffy.append("\nCOVER_INDIC=["+COVER_INDIC+"]");
        buffy.append("\nBASIC_DATA_STATUS=["+BASIC_DATA_STATUS+"]");
        buffy.append("\nCOLL_DATA_STATUS=["+COLL_DATA_STATUS+"]");
        buffy.append("\nCOL_CAT_ID=["+COL_CAT_ID+"]");
        buffy.append("\nCOL_SUB_ID=["+COL_SUB_ID+"]");        
        buffy.append("\nsave_ver_aut_flag=["+save_ver_aut_flag+"]");
        buffy.append("\nUSE_ID_VER_SND=["+USE_ID_VER_SND+"]");
        buffy.append("\nFINANCIAL_FLAG=["+FINANCIAL_FLAG+"]");
        buffy.append("\nCOLL_CATEGORY=["+COLL_CATEGORY+"]");
        buffy.append("\nKolLow_txtEligibility=["+KolLow_txtEligibility+"]");
        buffy.append("\nColRba_txtEligibility=["+ColRba_txtEligibility+"]");
        buffy.append("\nKol_txtRbaEligDsc=["+Kol_txtRbaEligDsc+"]");
        buffy.append("\nColl_txtNumOf=["+Coll_txtNumOf+"]");
        buffy.append("\nColl_txtB1Eligibility=["+Coll_txtB1Eligibility+"]");
        buffy.append("\nColl_txtB2IRBEligibility=["+Coll_txtB2IRBEligibility+"]");
        buffy.append("\naction=["+action+"]");
        buffy.append("\nCOL_HEA_ID_ARH=["+COL_HEA_ID_ARH+"]");
        buffy.append("\nUSE_ID_VER=["+USE_ID_VER+"]");
        buffy.append("\nUSE_ID_AUT=["+USE_ID_AUT+"]");
        buffy.append("\nVERIFICATION_TS=["+VERIFICATION_TS+"]");
        buffy.append("\nAUTORIZATION_TS=["+AUTORIZATION_TS+"]");
        buffy.append("\nVER_SEND_TS=["+VER_SEND_TS+"]");
        buffy.append("\nKol_txtCRMHnb=["+Kol_txtCRMHnb+"]");
        buffy.append("\nused_in_recovery=["+used_in_recovery+"]");
        buffy.append("\nkind_of_recovery=["+kind_of_recovery+"]");
        buffy.append("\nkind_of_recovery_desc=["+kind_of_recovery_desc+"]");
        buffy.append("\nfull_amount=["+full_amount+"]");
        buffy.append("\nfull_amount_cur_id=["+full_amount_cur_id+"]");
        buffy.append("\nfull_amount_cur_codeChar=["+full_amount_cur_codeChar+"]");
        buffy.append("\nrecovery_amount=["+recovery_amount+"]");
        buffy.append("\nrecovery_cur_id=["+recovery_cur_id+"]");
        buffy.append("\nrecovery_cur_codeChar=["+recovery_cur_codeChar+"]");
        buffy.append("\nrecovery_comment=["+recovery_comment+"]");
        buffy.append("\nrecovery_date=["+recovery_date+"]");
        buffy.append("\nrecovery_rate=["+recovery_rate+"]");
        buffy.append("\nb2_irb_insag_elig=["+b2_irb_insag_elig+"]");
        buffy.append("\nb2_hnb_insag_elig=["+b2_hnb_insag_elig+"]");
        buffy.append("\nuse_id_co=["+use_id_co+"]");
        buffy.append("\nuse_id_co_code=["+use_id_co_code+"]");
        buffy.append("\nuse_id_co_name=["+use_id_co_name+"]");
        buffy.append("\nchg_nom_val_proc_ts=["+chg_nom_val_proc_ts+"]");
        buffy.append("\nreal_est_nom_type=["+real_est_nom_type+"]");
        buffy.append("\nreal_est_nom_type_desc=["+real_est_nom_type_desc+"]");
        buffy.append("\nprec_exec_est=["+prec_exec_est+"]");
        buffy.append("\ncorrect_value=["+correct_value+"]");
        buffy.append("\nrespect_deadline=["+respect_deadline+"]");
        buffy.append("\nprof_to_rba=["+prof_to_rba+"]");
        buffy.append("\nprof_to_client=["+prof_to_client+"]");
        buffy.append("\ncol_eco_life=["+col_eco_life+"]");
        buffy.append("\nreal_est_estn_int=["+real_est_estn_int+"]");
        buffy.append("\nest_type=["+est_type+"]");
        buffy.append("\nest_type_desc=["+est_type_desc+"]");
        buffy.append("\nmet_est_1=["+met_est_1+"]");
        buffy.append("\nmet_est_2=["+met_est_2+"]");
        buffy.append("\ncoll_risk=["+coll_risk+"]");
        buffy.append("\nnon_ins_reason=["+non_ins_reason+"]");
        buffy.append("\nnon_ins_reason_desc=["+non_ins_reason_desc+"]");
        buffy.append("\nins_cov_coll=["+ins_cov_coll+"]");
        buffy.append("\nrecovery_proc_stat=["+recovery_proc_stat+"]");
        buffy.append("\ngctc_id=["+gctc_id+"]");
        buffy.append("\ngctc_code=["+gctc_code+"]");
        buffy.append("\ngctc_desc=["+gctc_desc+"]");
        buffy.append("\nendorsement_type_id=["+endorsement_type_id+"]");
        buffy.append("\nendorsement_type_code=["+endorsement_type_code+"]");
        buffy.append("\nendorsement_type_desc=["+endorsement_type_desc+"]");
        buffy.append("\nobject_type_id=["+object_type_id+"]");
        buffy.append("\nobject_type_code=["+object_type_code+"]");
        buffy.append("\nobject_type_desc=["+object_type_desc+"]");
        buffy.append("\nproperty_type_id=["+property_type_id+"]");
        buffy.append("\nproperty_type_code=["+property_type_code+"]");
        buffy.append("\nproperty_type_desc=["+property_type_desc+"]");
        buffy.append("\ncontract_type=["+contract_type+"]");
        buffy.append("\ncontract_type_desc=["+contract_type_desc+"]");
        
        buffy.append("\nrealization_amount=["+realization_amount+"]");
        buffy.append("\nrealization_cur_id=["+realization_cur_id+"]");
        buffy.append("\nrealization_date=["+realization_date+"]");
        buffy.append("\nrealization_kind=["+realization_kind+"]");
        buffy.append("\nrealization_cost=["+realization_cost+"]");
        buffy.append("\npart_of_complex=["+part_of_complex+"]");
        buffy.append("\nrealization_cost_cur_id=["+realization_cost_cur_id+"]");
        buffy.append("\nfor_sale=["+for_sale+"]");
        buffy.append("\nfor_sale_date=["+for_sale_date+"]");
        buffy.append("\ntakeover_from_bank=["+takeover_from_bank+"]");
        
        return buffy.toString();
    }
} 
