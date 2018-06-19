/*
 * Created on 2006.03.24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.yoy4;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
 
/**  
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollHeadUpdateData {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy4/CollHeadUpdateData.java,v 1.21 2017/06/01 12:03:38 hrazst Exp $";
	
	  //data from table coll_head
	  public BigDecimal COL_HEA_ID = null;
	  public Timestamp USER_LOCK = null;
	  public Timestamp USER_LOCK_IN = null;
	  
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
	  public BigDecimal USE_ID = null;
	  public String INSPOL_IND = null;
	  public String Coll_txtDesc = null;
	  public java.sql.Timestamp CREATE_DATE_TIME = null;
	  public Date CURR_DATE = null;	  
	  public BigDecimal Coll_txtAcumBuyValue = null;
	  public String Coll_txtEligibility = null;	
	  public BigDecimal ORG_UNI_ID = null;
	  public String save_ver_aut_flag = null;
	  public String BASIC_DATA_STATUS = null;
	  public String MORTGAGE_STATUS = null;
	  public String COVER_INDIC = null;
	  public String COLL_DATA_STATUS = null;
	  public BigDecimal ORIGIN_ORG_UNI_ID = null;
	  public String KolLow_txtEligibility = null;
	  public String ColRba_txtEligibility = null;
	  public String Kol_txtRbaEligDsc = null;
	  public Integer Coll_txtNumOf = null;	  
	  public String Coll_txtB1Eligibility = null;
	  public String Coll_txtB2IRBEligibility = null;
      public String SPEC_STATUS = null;  
      public String Kol_txtCRMHnb=null;

      /**Korišten u naplati D/N */
      public String used_in_recovery = null;
      public String used_in_recovery_desc = null;
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
          buffy.append("\n*********************CollHeadUpdateData*********************");
          buffy.append("\nUSER_LOCK=["+this.USER_LOCK+"]");
          buffy.append("\nUSER_LOCK_IN=["+this.USER_LOCK_IN+"]");
          buffy.append("\nCOL_TYPE_ID=["+this.COL_TYPE_ID+"]");
          buffy.append("\nCOL_CAT_ID=["+this.COL_CAT_ID+"]");
          buffy.append("\nCOL_SUB_ID=["+this.COL_SUB_ID+"]");
          buffy.append("\nColl_txtAcouBDate=["+this.Coll_txtAcouBDate+"]");
          buffy.append("\nColl_txtAcouBValue=["+this.Coll_txtAcouBValue+"]");
          buffy.append("\nColl_txtAcouDate=["+this.Coll_txtAcouDate+"]");
          buffy.append("\nColl_txtAcouValue=["+this.Coll_txtAcouValue+"]");
          buffy.append("\nColl_txtAddData=["+this.Coll_txtAddData+"]");
          buffy.append("\nColl_txtAmortAge=["+this.Coll_txtAmortAge+"]");
          buffy.append("\nColl_txtAmortValCal=["+this.Coll_txtAmortValCal+"]");
          buffy.append("\nColl_txtAvailDate=["+this.Coll_txtAvailDate+"]");
          buffy.append("\nColl_txtAvailPerCal=["+this.Coll_txtAvailPerCal+"]");
          buffy.append("\nColl_txtAvailValue=["+this.Coll_txtAvailValue+"]");
          buffy.append("\nColl_txtBptcDate=["+this.Coll_txtBptcDate+"]");
          buffy.append("\nColl_txtBptcValu=["+this.Coll_txtBptcValu+"]");
          buffy.append("\nColl_txtCollHnbPonder=["+this.Coll_txtCollHnbPonder+"]");
          buffy.append("\nColl_txtCollMvpPonder=["+this.Coll_txtCollMvpPonder+"]");
          buffy.append("\nColl_txtCollRzbPonder=["+this.Coll_txtCollRzbPonder+"]");
          buffy.append("\nColl_txtComDoc=["+this.Coll_txtComDoc+"]");
          buffy.append("\nColl_txtDateFrom=["+this.Coll_txtDateFrom+"]");
          buffy.append("\nColl_txtDateRecDoc=["+this.Coll_txtDateRecDoc+"]");
          buffy.append("\nColl_txtDateToDoc=["+this.Coll_txtDateToDoc+"]");
          buffy.append("\nColl_txtDateToLop=["+this.Coll_txtDateToLop+"]");
          buffy.append("\nColl_txtDateRecLop=["+this.Coll_txtDateRecLop+"]");
          buffy.append("\nColl_txtRecLop=["+this.Coll_txtRecLop+"]");
          buffy.append("\nColl_txtDateUnti=["+this.Coll_txtDateUnti+"]");
          buffy.append("\nColl_txtDatnFrom=["+this.Coll_txtDatnFrom+"]");
          buffy.append("\nColl_txtDatnUnti=["+this.Coll_txtDatnUnti+"]");
          buffy.append("\nColl_txtEstnDate=["+this.Coll_txtEstnDate+"]");
          buffy.append("\nColl_txtEstnValu=["+this.Coll_txtEstnValu+"]");
          buffy.append("\nColl_txtHfsValue=["+this.Coll_txtHfsValue+"]");
          buffy.append("\nColl_txtHfsValueDate=["+this.Coll_txtHfsValueDate+"]");
          buffy.append("\nColl_txtHfsDateLastOne=["+this.Coll_txtHfsDateLastOne+"]");
          buffy.append("\nColl_txtHfsValueLastOne=["+this.Coll_txtHfsValueLastOne+"]");
          buffy.append("\nColl_txtLiquDate=["+this.Coll_txtLiquDate+"]");
          buffy.append("\nColl_txtLiquValu=["+this.Coll_txtLiquValu+"]");
          buffy.append("\nColl_txtMissingDoc=["+this.Coll_txtMissingDoc+"]");
          buffy.append("\nColl_txtNepoDate=["+this.Coll_txtNepoDate+"]");
          buffy.append("\nColl_txtNepoPerCal=["+this.Coll_txtNepoPerCal+"]");
          buffy.append("\nColl_txtNepoValue=["+this.Coll_txtNepoValue+"]");
          buffy.append("\nColl_txtNomiDate=["+this.Coll_txtNomiDate+"]");
          buffy.append("\nColl_txtNomiDesc=["+this.Coll_txtNomiDesc+"]");
          buffy.append("\nColl_txtNomiValu=["+this.Coll_txtNomiValu+"]");
          buffy.append("\nColl_txtRevaBDate=["+this.Coll_txtRevaBDate+"]");
          buffy.append("\nColl_txtRevaBDateAM=["+this.Coll_txtRevaBDateAM+"]");
          buffy.append("\nColl_txtRevaBValue=["+this.Coll_txtRevaBValue+"]");
          buffy.append("\nColl_txtRevaCoefMan=["+this.Coll_txtRevaCoefMan+"]");
          buffy.append("\nColl_txtRevaDate=["+this.Coll_txtRevaDate+"]");
          buffy.append("\nColl_txtRevaDateAM=["+this.Coll_txtRevaDateAM+"]");
          buffy.append("\nColl_txtSumLimitDat=["+this.Coll_txtSumLimitDat+"]");
          buffy.append("\nColl_txtSumLimitVal=["+this.Coll_txtSumLimitVal+"]");
          buffy.append("\nColl_txtSumPartDat=["+this.Coll_txtSumPartDat+"]");
          buffy.append("\nColl_txtSumPartVal=["+this.Coll_txtSumPartVal+"]");
          buffy.append("\nColl_txtThirdRight=["+this.Coll_txtThirdRight+"]");
          buffy.append("\nColl_txtThirdRightDate=["+this.Coll_txtThirdRightDate+"]");
          buffy.append("\nColl_txtThirdRightInNom=["+this.Coll_txtThirdRightInNom+"]");
          buffy.append("\nREAL_EST_EUSE_ID=["+this.REAL_EST_EUSE_ID+"]");
          buffy.append("\nREAL_EST_NM_CUR_ID=["+this.REAL_EST_NM_CUR_ID+"]");
          buffy.append("\nCUS_ID=["+this.CUS_ID+"]");
          buffy.append("\nCOLL_CUS_ID=["+this.COLL_CUS_ID+"]");
          buffy.append("\nAMORT_PER_CAL_ID=["+this.AMORT_PER_CAL_ID+"]");
          buffy.append("\nTHIRD_RIGHT_CUR_ID=["+this.THIRD_RIGHT_CUR_ID+"]");
          buffy.append("\nCOL_PLACE=["+this.COL_PLACE+"]");
          buffy.append("\nCOL_COUNTY=["+this.COL_COUNTY+"]");
          buffy.append("\nCOL_DISTRICT=["+this.COL_DISTRICT+"]");
          buffy.append("\nCOL_RESI_QUAR=["+this.COL_RESI_QUAR+"]");
          buffy.append("\nUSE_ID=["+this.USE_ID+"]");
          buffy.append("\nINSPOL_IND=["+this.INSPOL_IND+"]");
          buffy.append("\nColl_txtDesc=["+this.Coll_txtDesc+"]");
          buffy.append("\nCREATE_DATE_TIME=["+this.CREATE_DATE_TIME+"]");
          buffy.append("\nCURR_DATE=["+this.CURR_DATE+"]");
          buffy.append("\nColl_txtAcumBuyValue=["+this.Coll_txtAcumBuyValue+"]");
          buffy.append("\nColl_txtEligibility=["+this.Coll_txtEligibility+"]");
          buffy.append("\nORG_UNI_ID=["+this.ORG_UNI_ID+"]");
          buffy.append("\nsave_ver_aut_flag=["+this.save_ver_aut_flag+"]");
          buffy.append("\nBASIC_DATA_STATUS=["+this.BASIC_DATA_STATUS+"]");
          buffy.append("\nMORTGAGE_STATUS=["+this.MORTGAGE_STATUS+"]");
          buffy.append("\nCOVER_INDIC=["+this.COVER_INDIC+"]");
          buffy.append("\nCOLL_DATA_STATUS=["+this.COLL_DATA_STATUS+"]");
          buffy.append("\nORIGIN_ORG_UNI_ID=["+this.ORIGIN_ORG_UNI_ID+"]");
          buffy.append("\nKolLow_txtEligibility=["+this.KolLow_txtEligibility+"]");
          buffy.append("\nColRba_txtEligibility=["+this.ColRba_txtEligibility+"]");
          buffy.append("\nKol_txtRbaEligDsc=["+this.Kol_txtRbaEligDsc+"]");
          buffy.append("\nColl_txtNumOf=["+this.Coll_txtNumOf+"]");
          buffy.append("\nColl_txtB1Eligibility=["+this.Coll_txtB1Eligibility+"]");
          buffy.append("\nColl_txtB2IRBEligibility=["+this.Coll_txtB2IRBEligibility+"]");
          buffy.append("\nSPEC_STATUS=["+this.SPEC_STATUS+"]");
          buffy.append("\nKol_txtCRMHnb=["+this.Kol_txtCRMHnb+"]");
          buffy.append("\nused_in_recovery=["+this.used_in_recovery+"]");
          buffy.append("\nused_in_recovery_desc=["+this.used_in_recovery_desc+"]");
          buffy.append("\nkind_of_recovery=["+this.kind_of_recovery+"]");
          buffy.append("\nkind_of_recovery_desc=["+this.kind_of_recovery_desc+"]");
          buffy.append("\nfull_amount=["+this.full_amount+"]");
          buffy.append("\nfull_amount_cur_id=["+this.full_amount_cur_id+"]");
          buffy.append("\nfull_amount_cur_codeChar=["+this.full_amount_cur_codeChar+"]");
          buffy.append("\nrecovery_amount=["+this.recovery_amount+"]");
          buffy.append("\nrecovery_cur_id=["+this.recovery_cur_id+"]");
          buffy.append("\nrecovery_cur_codeChar=["+this.recovery_cur_codeChar+"]");
          buffy.append("\nrecovery_comment=["+this.recovery_comment+"]");
          buffy.append("\nrecovery_date=["+this.recovery_date+"]");
          buffy.append("\nrecovery_rate=["+this.recovery_rate+"]");
          buffy.append("\nb2_irb_insag_elig=["+this.b2_irb_insag_elig+"]");
          buffy.append("\nb2_hnb_insag_elig=["+this.b2_hnb_insag_elig+"]");
          buffy.append("\nuse_id_co=["+this.use_id_co+"]");
          buffy.append("\nuse_id_co_code=["+this.use_id_co_code+"]");
          buffy.append("\nuse_id_co_name=["+this.use_id_co_name+"]");
          buffy.append("\nchg_nom_val_proc_ts=["+this.chg_nom_val_proc_ts+"]");
          buffy.append("\nreal_est_nom_type=["+this.real_est_nom_type+"]");
          buffy.append("\nprec_exec_est=["+this.prec_exec_est+"]");
          buffy.append("\ncorrect_value=["+this.correct_value+"]");
          buffy.append("\nrespect_deadline=["+this.respect_deadline+"]");
          buffy.append("\nprof_to_rba=["+this.prof_to_rba+"]");
          buffy.append("\nprof_to_client=["+this.prof_to_client+"]");
          buffy.append("\ncol_eco_life=["+this.col_eco_life+"]");
          buffy.append("\nreal_est_estn_int=["+this.real_est_estn_int+"]");
          buffy.append("\nest_type=["+this.est_type+"]");
          buffy.append("\nest_type_desc=["+this.est_type_desc+"]");
          buffy.append("\nmet_est_1=["+this.met_est_1+"]");
          buffy.append("\nmet_est_2=["+this.met_est_2+"]");
          buffy.append("\ncoll_risk=["+this.coll_risk+"]");
          buffy.append("\nnon_ins_reason=["+this.non_ins_reason+"]");
          buffy.append("\nnon_ins_reason_desc=["+this.non_ins_reason_desc+"]");
          buffy.append("\nins_cov_coll=["+this.ins_cov_coll+"]");
          buffy.append("\nrecovery_proc_stat=["+this.recovery_proc_stat+"]");
          buffy.append("\ngctc_id=["+this.gctc_id+"]");
          buffy.append("\ngctc_code=["+this.gctc_code+"]");
          buffy.append("\ngctc_desc=["+this.gctc_desc+"]");
          buffy.append("\nendorsement_type_id=["+this.endorsement_type_id+"]");
          buffy.append("\nendorsement_type_code=["+this.endorsement_type_code+"]");
          buffy.append("\nendorsement_type_desc=["+this.endorsement_type_desc+"]");
          buffy.append("\nobject_type_id=["+this.object_type_id+"]");
          buffy.append("\nobject_type_code=["+this.object_type_code+"]");
          buffy.append("\nobject_type_desc=["+this.object_type_desc+"]");
          buffy.append("\nproperty_type_id=["+this.property_type_id+"]");
          buffy.append("\nproperty_type_code=["+this.property_type_code+"]");
          buffy.append("\nproperty_type_desc=["+this.property_type_desc+"]");
          buffy.append("\ncontract_type=["+this.contract_type+"]");
          buffy.append("\ncontract_type_desc=["+this.contract_type_desc+"]");          
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
          buffy.append("\n**********************************************************");
          return buffy.toString();
      }      
}
