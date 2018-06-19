/*
 * Created on 2006.04.03
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
public class CollHeadArhInsertData {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/yoy4/CollHeadArhInsertData.java,v 1.11 2014/11/12 14:13:48 hrazst Exp $";
	 
//	 data from table coll_head
		  
//	 input
	  public java.sql.Timestamp USER_LOCK = null;
	  public BigDecimal COL_HEA_ID = null;

	  public Timestamp Coll_txtUserLock = null;
	  public String Coll_txtCode = null;
	  public String STATUS = null;
	  public String SPEC_STATUS = null;
	  public Timestamp Coll_txtOpeningTs = null;
	  public BigDecimal EVE_ID=null; 
	 
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
	  public String bank_sign = null;

	  public BigDecimal eve_typ_id = null;
	  public BigDecimal Coll_txtAcumBuyValue = null;
	  public String Coll_txtEligibility = null;
	  public BigDecimal ORIGIN_ORG_UNI_ID =null;
	  public BigDecimal ORG_UNI_ID = null;
	  public BigDecimal USE_ID_VER = null;
	  public BigDecimal USE_ID_AUT = null;
	  public Timestamp VERIFICATION_TS = null;
	  public Timestamp AUTORIZATION_TS = null;
	  public String MORTGAGE_STATUS = null;
	  public String COLLATERAL_STATUS = null;
	  public String WORKFLOW_INDIC = null;
	  public String COVER_INDIC = null;
	  public Timestamp VER_SEND_TS = null;
	  public String BASIC_DATA_STATUS = null;
	  public String COLL_DATA_STATUS = null;
	  public BigDecimal COL_CAT_ID = null; 
		
	  // dodano 19.06.2006  
	  public BigDecimal USE_ID_VER_SND = null;	  
	  public String FINANCIAL_FLAG = null;		
	  // dodano 27.10.2006
	  public String KolLow_txtEligibility = null;
	  public String ColRba_txtEligibility = null;		
		  
	  //dodano 27.12.2006
	  public String Kol_txtRbaEligDsc = null; 
		  
	  //dodano 12.12.2007
	  public Integer Coll_txtNumOf = null;

	  public String Coll_txtB1Eligibility = null;
	  public String Coll_txtB2IRBEligibility = null;	

	  public String action = null;
	  public BigDecimal COL_HEA_ID_ARH = null;

		  public BigDecimal Kol_txtPonderMVP = null;	
		  
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
		  
		  
}  
