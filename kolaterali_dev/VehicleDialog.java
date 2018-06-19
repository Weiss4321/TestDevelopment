
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;

/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VehicleDialog extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/VehicleDialog.java,v 1.28 2006/05/20 13:51:55 hrasia Exp $";
	private final java.math.BigDecimal numberZero = new java.math.BigDecimal("0.00");

	private final java.math.BigDecimal numberOneTwo = new java.math.BigDecimal("1.00");
	private final java.math.BigDecimal numberOne        = new java.math.BigDecimal("1.0000000000000000000000000000000000000000000000000000000000000000");
	private final java.math.BigDecimal numberOneHundred = new java.math.BigDecimal("100.0000000000000000000000000000000000000000000000000000000000000000");
	
	public VehicleDialog(ResourceAccessor ra) {
		super(ra);
	}
	public void VehicleDialog_SE() {
		if (!(ra.isLDBExists("VehicleDialogLDB"))) {
			ra.createLDB("VehicleDialogLDB");
		}
		if((ra.getScreenContext().compareTo("scr_change")== 0)){
			isprazniVehicleDialogLDB();
			postavi_nekeUserDate();
		}
		if((ra.getScreenContext().compareTo("scr_detail")== 0)){
			TableData td = (TableData) ra.getAttribute("VehicleLDB", "tblVehicle");
			java.util.Vector hiddenVector = (java.util.Vector) td.getSelectedRowUnique();
			BigDecimal hidden = (BigDecimal) hiddenVector.elementAt(0);
			Vector row = td.getSelectedRowData();			
			
			BigDecimal col_veh_id = null;
			col_veh_id = hidden;
			
			
			
			if (!(ra.isLDBExists("VehicleDialogLDB"))) {
				ra.createLDB("VehicleDialogLDB");
			}
			if (!(ra.isLDBExists("VehicleDialog_B_LDB"))) {
				ra.createLDB("VehicleDialog_B_LDB");
			}	
			ra.setAttribute("VehicleDialogLDB", "Vehicle_COL_VEH_ID", col_veh_id); 
		}
		if((ra.getScreenContext().compareTo("scr_update")== 0)){
			
			TableData td = (TableData) ra.getAttribute("VehicleLDB", "tblVehicle");
			java.util.Vector hiddenVector = (java.util.Vector) td.getSelectedRowUnique();
			BigDecimal hidden = (BigDecimal) hiddenVector.elementAt(0);
			Vector row = td.getSelectedRowData();			
			
			BigDecimal col_veh_id = null;
			col_veh_id = hidden;
			
			
			
			if (!(ra.isLDBExists("VehicleDialogLDB"))) {
				ra.createLDB("VehicleDialogLDB");
			}
			if (!(ra.isLDBExists("VehicleDialog_B_LDB"))) {
				ra.createLDB("VehicleDialog_B_LDB");
			}	 
			ra.setAttribute("VehicleDialogLDB", "Vehicle_COL_VEH_ID", col_veh_id);
		}
		
	}//VehicleDialog_SE
	
	
	public void isprazniVehicleDialogLDB(){
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehVinNumber",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehVehLicence",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehColour",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehPlate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehNumDoor",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehPowerKW",null);
		ra.setAttribute("VehicleDialogLDB","VEH_ENGINE_TYPE",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtEngType",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtEngFuelType",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehKmTrav",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehEquipment",null);
		ra.setAttribute("VehicleDialogLDB","VEH_TYPE_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVetType",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVetSizeType",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVetMake",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtVehMadeDate",null);
		ra.setAttribute("VehicleDialogLDB","COL_TYPE_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollTypeCode",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollTypeName",null);
		ra.setAttribute("VehicleDialogLDB","VEH_PLACE",null);
		ra.setAttribute("VehicleDialogLDB","VEH_COUNTY",null);
		ra.setAttribute("VehicleDialogLDB","VEH_DISTRICT",null);
		ra.setAttribute("VehicleDialogLDB","VEH_RESI_QUAR",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCountyCode",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCountyName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtPlaceCode",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtPlaceName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDistrictCode",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDistrictName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtResiQuarCode",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtResiQuarName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_COLL_CUS_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtOwnerRegisterNo",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtOwnerName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCoownership",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCoownershipName",null);
		ra.setAttribute("VehicleDialogLDB","VEH_EST_ABC",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtEUseIdLogin",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtEUseIdName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDatnFrom",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtEstnValu",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_COL_VEH_ID",null);
		ra.setAttribute("VehicleDialogLDB","VEH_NM_CUR_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtNmCurIdCodeChar",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtNomiDesc",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtEstnDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtNomiValu",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtNomiDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtLiquValu",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtLiquDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtBptcValu",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtBptcDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtOriginalVal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtOrigValDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAddData",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtComDoc",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtMissingDoc",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDateToDoc",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDateRecDoc",null);
		ra.setAttribute("VehicleDialogLDB","IP_ID",null);
		ra.setAttribute("VehicleDialogLDB","IP_CUS_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtIpCode",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtIpCusIdRegisterNo",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtIpCusIdName",null);
		ra.setAttribute("VehicleDialogLDB","IP_CUR_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtIpNomVal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtIpCurIdCodeChar",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtIpValiFrom",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtIpValiUntil",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollPeriodRev",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaAutMan",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDateMan",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaCoefMan",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaValue",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDateAM",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBValue",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBDateAM",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAmortAge",null);
		ra.setAttribute("VehicleDialogLDB","AMORT_PER_CAL_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAmortPerCal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAmortValCal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRight",null);
		ra.setAttribute("VehicleDialogLDB","THIRD_RIGHT_CUR_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightCurCodeChar",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValue",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValueDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValueLastOne",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsDateLastOne",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtNepoPerCal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtNepoValue",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtNepoDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollMvpPonder",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollHnbPonder",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollRzbPonder",null);
		ra.setAttribute("VehicleDialogLDB","COLL_ACCOUNTING_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollAccounting",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollAccountingName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouPerCal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouPerAco",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouValue",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouBValue",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouBDate",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumLimitVal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumLimitDat",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumPartVal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumPartDat",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailPerCal",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailValue",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailDate",null);
		ra.setAttribute("VehicleDialogLDB","USE_OPEN_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseOpenIdLogin",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseOpenIdName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtOpeningTs",null);
		ra.setAttribute("VehicleDialogLDB","USE_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseIdLogin",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseIdName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtUserLock",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDateFrom",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDateUnti",null);
		ra.setAttribute("VehicleDialogLDB","dummyBD",null);
		ra.setAttribute("VehicleDialogLDB","dummyDate",null);
		ra.setAttribute("VehicleDialogLDB","dummySt",null);
		ra.setAttribute("VehicleDialogLDB","SysCodId",null);
		ra.setAttribute("VehicleDialogLDB","dummyInt",null);
		ra.setAttribute("VehicleDialogLDB","COOWNERSHIP_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDatnUnti",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_CUS_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCarrierRegisterNo",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCarrierName",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtInspolInd",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDateToLop",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDateRecLop",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRecLop",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollMvpPonderMin",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollMvpPonderMax",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollHnbPonderMin",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollRzbPonderMin",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollHnbPonderMax",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtCollRzbPonderMax",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_COL_HEA_ID",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtDesc",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRealEstStatus",null);
		ra.setAttribute("VehicleDialogLDB","Vehicle_txtRealEstSpecStat",null);

	
	}
	
	public void otvori_prvi_vozila() {
		if(ra.getScreenContext().compareTo("scr_change")== 0){
			ra.switchScreen("VehicleDialog", "scr_change");
		}
		if(ra.getScreenContext().compareTo("scr_detail")== 0){
			ra.switchScreen("VehicleDialog", "scr_detail");
		}
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			ra.switchScreen("VehicleDialog", "scr_update");
		}
		
	}//otvori_prvi_vozila
	public void otvori_drugi_vozila() {
		if(ra.getScreenContext().compareTo("scr_change")== 0){
			ra.switchScreen("VehicleDialog2", "scr_change");
		}
		if(ra.getScreenContext().compareTo("scr_detail")== 0){
			ra.switchScreen("VehicleDialog2", "scr_detail");
		}
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			ra.switchScreen("VehicleDialog2", "scr_update");
		}
	}//otvori_drugi_vozila
	public void otvori_treci_vozila() {
		if(ra.getScreenContext().compareTo("scr_change")== 0){
			ra.switchScreen("VehicleDialog3", "scr_change");
		}
		if(ra.getScreenContext().compareTo("scr_detail")== 0){
			ra.switchScreen("VehicleDialog3", "scr_detail");
		}
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			ra.switchScreen("VehicleDialog3", "scr_update");
		}
	}//otvori_treci_vozila
	
	
	public boolean Vehicle_txtNomiValu_FV(){
		if((ra.getScreenContext().compareTo("scr_change")== 0) ){
			
			java.math.BigDecimal nominalCONV = null;
			java.sql.Date        nominalCONVDate = null;
			java.math.BigDecimal nominalCUR_ID = null;
			java.math.BigDecimal thirdCUR_ID = null;
		
			String nominalCUR_IDChar = null;
			
			java.math.BigDecimal accouValu = null;
			java.math.BigDecimal mvpPonder = null;
			java.math.BigDecimal availValu = null;
			java.math.BigDecimal thirdRightInNom = null;
			java.math.BigDecimal hfsValue = null;
			
			if (ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiValu")!= null ){
				nominalCONV = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiValu");
			}
		
			if (ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiDate")!= null ){
				nominalCONVDate = (java.sql.Date) ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiDate");
			}
		
			if (ra.getAttribute("VehicleDialogLDB","VEH_NM_CUR_ID")!= null ){
				nominalCUR_ID = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB","VEH_NM_CUR_ID");
				nominalCUR_IDChar = (String) ra.getAttribute("VehicleDialogLDB","Vehicle_txtNmCurIdCodeChar");
			}
		
			if (ra.getAttribute("VehicleDialogLDB","THIRD_RIGHT_CUR_ID")!= null ){
				thirdCUR_ID = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB","THIRD_RIGHT_CUR_ID");
			}
		
			
			System.out.println(nominalCONVDate);
			
		//============================================================
		//Sluzbena verzija izracuna vrijednosti vrijedi za sve kolaterale
		//osim vrijednosnica kod kojih je neponderirana = zavrsnoj coll. officera
		//jer amortizacija i revalorizacija su sadrzane u cijeni na trzistu

		//	ZAVRSNA CIJENA COLLATERAL OFFICERA
		//	+/- REVALORIZACIJA
		//	- AMORTIZACIJA
		//	____________________________________
		//	CIJENA DANAS ( NEPONDERIRANA )
		//	* PONDER
		//	____________________________________
		//	PONDERIRANA VRIJEDNOST
		//	-HIPOTEKE ( SVE )
		//	____________________________________
		//	CASH ZA BANKU  ( RASPOLOZIVO)
		//	============================================================
		//	Postupak s vrijednostima

		//	1. KOD INICIJALNOG INSERTA
		//	REVA_AUT_MAN				= 	I
		//	REVA_COEF_MAN				= 	1.00
		//	REVA_VALUE					=	REAL_EST_NOMI_VALU
		//	REVA_DATE					= 	REAL_EST_NOMI_DATE
		//	REVA_DATE_AM	 			=   I
		//	REVA_BVALUE					=	REAL_EST_NOMI_VALU
		//	REVA_BDATE	   				= 	REAL_EST_NOMI_DATE
		//	REVA_BDATE_AM	 			=   I

		//	NEPO_VALUE					=	REAL_EST_NOMI_VALU
		//	NEPO_DATE					= 	REAL_EST_NOMI_DATE
		
		//	THIRD_RIGHT					= 	0
		//	if(THIRD_RIGHT_CUR_ID == null)			THIRD_RIGHT_CUR_ID	= VEH_NM_CUR_ID
		//	THIRD_RIGHT_NOM				=	0
		//	THIRD_RIGHT_DATE			= 	REAL_EST_NOMI_DATE

		//	HFS_VALUE					= 	0
		//	HFS_VALUE_DATE				= 	REAL_EST_NOMI_DATE
		//	HFS_VALUE_LAST_ONE			= 	0
		//	HFS_DATE_LAST_ONE			= 	REAL_EST_NOMI_DATE

		//	ACOU_VALUE					=   NEPO_VALUE * COLL_MVP_PONDER
		//	ACOU_DATE					=  	NEPO_DATE

		//	ACOU_BVALUE					=   NEPO_VALUE * COLL_MVP_PONDER
		//	ACOU_BDATE					=  	NEPO_DATE


		//	SUM_LIMIT_VAL				= 	0
		//	SUM_LIMIT_DAT				=	NEPO_DATE

		//	SUM_PART_VAL				= 	0
		//	SUM_PART_DAT				=	NEPO_DATE

		//	AVAIL_VALUE					=	ACOU_VALUE - THIRD_RIGHT_NOM - HFS_VALUE
		//	AVAIL_DATE					=   NEPO_DATE

			
		

				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaAutMan","I");
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDateMan",null);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaCoefMan",numberOne);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaValue",nominalCONV);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDate",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDateAM","I");
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBValue",nominalCONV);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBDate",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBDateAM","I");

				ra.setAttribute("VehicleDialogLDB","Vehicle_txtNepoValue",nominalCONV);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtNepoDate",nominalCONVDate);
		
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRight",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightDate",nominalCONVDate);
				
				if(thirdCUR_ID == null){
					thirdCUR_ID = nominalCUR_ID;
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightCurCodeChar",nominalCUR_IDChar);
					ra.setAttribute("VehicleDialogLDB","THIRD_RIGHT_CUR_ID",thirdCUR_ID);
				
				}
		
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValue",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValueDate",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValueLastOne",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsDateLastOne",nominalCONVDate);

				
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumLimitVal",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumLimitDat",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumPartVal",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumPartDat",nominalCONVDate);
				
				if(ra.getAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom") != null){
					thirdRightInNom = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom");
				}
				if(ra.getAttribute("VehicleDialogLDB","Vehicle_txtHfsValue") != null){
					hfsValue = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB","Vehicle_txtHfsValue");
				}
				
				if(ra.getAttribute("VehicleDialogLDB","Vehicle_txtCollMvpPonder") != null){
					mvpPonder = (java.math.BigDecimal)	ra.getAttribute("VehicleDialogLDB","Vehicle_txtCollMvpPonder");
					accouValu = nominalCONV.multiply(mvpPonder);
					accouValu = accouValu.divide(numberOneHundred,2,java.math.BigDecimal.ROUND_HALF_UP);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouValue",accouValu);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouDate",nominalCONVDate);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouBValue",accouValu);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouBDate",nominalCONVDate);
					availValu = accouValu.subtract(thirdRightInNom);
					availValu = availValu.subtract(hfsValue);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailValue",availValu);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailDate",nominalCONVDate);
				}
				
				if(thirdCUR_ID.compareTo(nominalCUR_ID)== 0){
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightCurCodeChar",nominalCUR_IDChar);
					
				}
				nominalCONV = null;
				nominalCONVDate = null;
				nominalCUR_ID = null;
				thirdCUR_ID = null;
			
				nominalCUR_IDChar = null;
				
				accouValu = null;
				mvpPonder = null;
				availValu = null;
				thirdRightInNom = null;
				hfsValue = null;
		
		}//scr_change

			
		return true;
	}//Vehicle_txtNomiValu_FV
	
	public boolean Vehicle_txtNomiDate_FV(){
		
		if((ra.getScreenContext().compareTo("scr_change")== 0)|| (ra.getScreenContext().compareTo("scr_update")== 0) ){
			
			java.math.BigDecimal nominalCONV = null;
			java.sql.Date        nominalCONVDate = null;
			java.math.BigDecimal nominalCUR_ID = null;
			java.math.BigDecimal thirdCUR_ID = null;
		
			String nominalCUR_IDChar = null;
			
			java.math.BigDecimal accouValu = null;
			java.math.BigDecimal mvpPonder = null;
			java.math.BigDecimal availValu = null;
			java.math.BigDecimal thirdRightInNom = null;
			java.math.BigDecimal hfsValue = null;
			
			if (ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiValu")!= null ){
				nominalCONV = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiValu");
			}
		
			if (ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiDate")!= null ){
				nominalCONVDate = (java.sql.Date) ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiDate");
			}
		
			if (ra.getAttribute("VehicleDialogLDB","VEH_NM_CUR_ID")!= null ){
				nominalCUR_ID = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB","VEH_NM_CUR_ID");
				nominalCUR_IDChar = (String) ra.getAttribute("VehicleDialogLDB","Vehicle_txtNmCurIdCodeChar");
			}
		
			if (ra.getAttribute("VehicleDialogLDB","THIRD_RIGHT_CUR_ID")!= null ){
				thirdCUR_ID = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB","THIRD_RIGHT_CUR_ID");
			}
		
			
			System.out.println(nominalCONVDate);
			
		//============================================================
		//Sluzbena verzija izracuna vrijednosti vrijedi za sve kolaterale
		//osim vrijednosnica kod kojih je neponderirana = zavrsnoj coll. officera
		//jer amortizacija i revalorizacija su sadrzane u cijeni na trzistu

		//	ZAVRSNA CIJENA COLLATERAL OFFICERA
		//	+/- REVALORIZACIJA
		//	- AMORTIZACIJA
		//	____________________________________
		//	CIJENA DANAS ( NEPONDERIRANA )
		//	* PONDER
		//	____________________________________
		//	PONDERIRANA VRIJEDNOST
		//	-HIPOTEKE ( SVE )
		//	____________________________________
		//	CASH ZA BANKU  ( RASPOLOZIVO)
		//	============================================================
		//	Postupak s vrijednostima

		//	1. KOD INICIJALNOG INSERTA
		//	REVA_AUT_MAN				= 	I
		//	REVA_COEF_MAN				= 	1.00
		//	REVA_VALUE					=	REAL_EST_NOMI_VALU
		//	REVA_DATE					= 	REAL_EST_NOMI_DATE
		//	REVA_DATE_AM	 			=   I
		//	REVA_BVALUE					=	REAL_EST_NOMI_VALU
		//	REVA_BDATE	   				= 	REAL_EST_NOMI_DATE
		//	REVA_BDATE_AM	 			=   I

		//	NEPO_VALUE					=	REAL_EST_NOMI_VALU
		//	NEPO_DATE					= 	REAL_EST_NOMI_DATE
		
		//	THIRD_RIGHT					= 	0
		//	if(THIRD_RIGHT_CUR_ID == null)			THIRD_RIGHT_CUR_ID	= VEH_NM_CUR_ID
		//	THIRD_RIGHT_NOM				=	0
		//	THIRD_RIGHT_DATE			= 	REAL_EST_NOMI_DATE

		//	HFS_VALUE					= 	0
		//	HFS_VALUE_DATE				= 	REAL_EST_NOMI_DATE
		//	HFS_VALUE_LAST_ONE			= 	0
		//	HFS_DATE_LAST_ONE			= 	REAL_EST_NOMI_DATE

		//	ACOU_VALUE					=   NEPO_VALUE * COLL_MVP_PONDER
		//	ACOU_DATE					=  	NEPO_DATE

		//	ACOU_BVALUE					=   NEPO_VALUE * COLL_MVP_PONDER
		//	ACOU_BDATE					=  	NEPO_DATE


		//	SUM_LIMIT_VAL				= 	0
		//	SUM_LIMIT_DAT				=	NEPO_DATE

		//	SUM_PART_VAL				= 	0
		//	SUM_PART_DAT				=	NEPO_DATE

		//	AVAIL_VALUE					=	ACOU_VALUE - THIRD_RIGHT_NOM - HFS_VALUE
		//	AVAIL_DATE					=   NEPO_DATE

			
		

				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaAutMan","I");
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDateMan",null);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaCoefMan",numberOne);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaValue",nominalCONV);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDate",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaDateAM","I");
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBValue",nominalCONV);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBDate",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtRevaBDateAM","I");

				ra.setAttribute("VehicleDialogLDB","Vehicle_txtNepoValue",nominalCONV);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtNepoDate",nominalCONVDate);
		
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRight",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightDate",nominalCONVDate);
				
				if(thirdCUR_ID == null){
					thirdCUR_ID = nominalCUR_ID;
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightCurCodeChar",nominalCUR_IDChar);
					ra.setAttribute("VehicleDialogLDB","THIRD_RIGHT_CUR_ID",thirdCUR_ID);
				
				}
		
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValue",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValueDate",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsValueLastOne",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtHfsDateLastOne",nominalCONVDate);

				
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumLimitVal",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumLimitDat",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumPartVal",numberZero);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtSumPartDat",nominalCONVDate);
				
				if(ra.getAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom") != null){
					thirdRightInNom = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom");
				}
				if(ra.getAttribute("VehicleDialogLDB","Vehicle_txtHfsValue") != null){
					hfsValue = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB","Vehicle_txtHfsValue");
				}
				
				if(ra.getAttribute("VehicleDialogLDB","Vehicle_txtCollMvpPonder") != null){
					mvpPonder = (java.math.BigDecimal)	ra.getAttribute("VehicleDialogLDB","Vehicle_txtCollMvpPonder");
					accouValu = nominalCONV.multiply(mvpPonder);
					accouValu = accouValu.divide(numberOneHundred,2,java.math.BigDecimal.ROUND_HALF_UP);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouValue",accouValu);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouDate",nominalCONVDate);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouBValue",accouValu);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouBDate",nominalCONVDate);
					availValu = accouValu.subtract(thirdRightInNom);
					availValu = availValu.subtract(hfsValue);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailValue",availValu);
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailDate",nominalCONVDate);
				}
				if(thirdCUR_ID.compareTo(nominalCUR_ID)== 0){
					ra.setAttribute("VehicleDialogLDB","Vehicle_txtThirdRightCurCodeChar",nominalCUR_IDChar);
					
				}
				nominalCONV = null;
				nominalCONVDate = null;
				nominalCUR_ID = null;
				thirdCUR_ID = null;
			
				nominalCUR_IDChar = null;
				
				accouValu = null;
				mvpPonder = null;
				availValu = null;
				thirdRightInNom = null;
				hfsValue = null;
		
		}//scr_change

		
		return true;
	}//Vehicle_txtNomiDate_FV
	

	
	 
	
	 public boolean Vehicle_NmCurIdCodeChar_FV(String ElName, Object ElValue, Integer LookUp){

		if (ElValue == null || ((String) ElValue).equals("")) {                                          
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtNmCurIdCodeChar", "");                        
					ra.setAttribute("VehicleDialogLDB", "VEH_NM_CUR_ID", null);                               
					return true;                                                                                 
		}
		ra.setAttribute("VehicleDialogLDB", "dummySt", "");
		LookUpRequest lookUpRequest = new LookUpRequest("FPaymCurrencyLookUp");                       
		lookUpRequest.addMapping("VehicleDialogLDB", "VEH_NM_CUR_ID", "cur_id");           
		lookUpRequest.addMapping("VehicleDialogLDB", "dummySt", "code_num");
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtNmCurIdCodeChar", "code_char");
		lookUpRequest.addMapping("VehicleDialogLDB", "dummySt", "name");
		//lookUpRequest.addMapping("VehicleDialogLDB", "dummyInt", "ord_no");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}                                                                                              
		return true;     
	
	}//Vehicle_txtNmCurIdCodeChar_FV
	
	 public boolean VehicleDialog_CollateralType_FV(String ElName, Object ElValue, Integer LookUp) {
		if (ElValue == null || ((String) ElValue).equals("")) {
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollTypeCode", "");
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollTypeName", "");
				ra.setAttribute("VehicleDialogLDB", "COL_TYPE_ID", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollPeriodRev", "");
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollMvpPonder", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollMvpPonderMin", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollMvpPonderMax", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollHnbPonder", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollHnbPonderMin", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollHnbPonderMax", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollRzbPonder", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollRzbPonderMin", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollRzbPonderMax", null);
				ra.setAttribute("VehicleDialogLDB", "dummySt", null);
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollAccounting", "");
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCollAccountingName", "");
				ra.setAttribute("VehicleDialogLDB", "dummyDate", null);
				return true;
			}


		 	if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
		 		ra.createLDB("CollateralTypeLookUpLDB");
		 	}
		 	ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", "VOZI");
		 	
			LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp"); 
			lookUpRequest.addMapping("VehicleDialogLDB", "COL_TYPE_ID", "coll_type_id");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollTypeCode", "coll_type_code");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollTypeName", "coll_type_name");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollPeriodRev", "coll_period_rev");
			
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollMvpPonder", "coll_mvp_ponder");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollMvpPonderMin", "coll_mvp_ponder_mn");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollMvpPonderMax", "coll_mvp_ponder_mx");
			
			
			
			
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollHnbPonder", "coll_hnb_ponder");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollHnbPonderMin", "coll_hnb_ponder_mn");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollHnbPonderMax", "coll_hnb_ponder_mx");
			
			
			
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollRzbPonder", "coll_rzb_ponder");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollRzbPonderMin", "coll_rzb_ponder_mn");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollRzbPonderMax", "coll_rzb_ponder_mx");
			lookUpRequest.addMapping("VehicleDialogLDB", "dummySt", "coll_category");
			lookUpRequest.addMapping("VehicleDialogLDB", "dummySt", "coll_hypo_fidu");
			lookUpRequest.addMapping("VehicleDialogLDB", "dummySt", "hypo_fidu_name");
			lookUpRequest.addMapping("VehicleDialogLDB", "dummySt", "coll_anlitika");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollAccounting", "coll_accounting");
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCollAccountingName", "accounting_name");
			//
			//		if(){
			//				Vehicle_txtCollAccountingName
			//		} else{
			//				Vehicle_txtCollAccountingName
			//		} 
			lookUpRequest.addMapping("VehicleDialogLDB", "dummyDate", "coll_date_from");
			lookUpRequest.addMapping("VehicleDialogLDB", "dummyDate", "coll_date_until");
			
			
			try {
					ra.callLookUp(lookUpRequest);
			} catch (EmptyLookUp elu) {
					ra.showMessage("err012");
					return false;
			} catch (NothingSelected ns) {
					return false;
			}
			
			ra.setAttribute("VehicleDialogLDB", "dummySt", null);
			ra.setAttribute("VehicleDialogLDB", "dummyDate", null);
			if(ra.getCursorPosition().equals("Vehicle_txtCollTypeCode")){
				ra.setCursorPosition(2);
			}
			if(ra.getCursorPosition().equals("Vehicle_txtCollTypeName")){
				ra.setCursorPosition(1);
			}
			return true;

	}//VehicleDialog_CollateralType_FV
	 
	 public boolean VehicleDialog_Coownership_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCoownership", "");
            ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCoownershipName", "");
            ra.setAttribute("VehicleDialogLDB", "COOWNERSHIP_ID", null);
            
            
            return true;
        }

       
        ra.setAttribute("VehicleDialogLDB", "SysCodId", "clt_coownership");
        ra.setAttribute("VehicleDialogLDB", "dummySt", null);
        ra.setAttribute("VehicleDialogLDB", "dummyBD", null);        

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");

        request.addMapping("VehicleDialogLDB", "Vehicle_txtCoownership", "sys_code_value");
        request.addMapping("VehicleDialogLDB", "Vehicle_txtCoownershipName", "sys_code_desc");
        request.addMapping("VehicleDialogLDB", "COOWNERSHIP_ID", "sys_cod_val_id");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }
        ra.setAttribute("VehicleDialogLDB", "dummySt", null);
        ra.setAttribute("VehicleDialogLDB", "dummyBD", null);        
        ra.setAttribute("VehicleDialogLDB", "SysCodId", "");
        if(ra.getCursorPosition().equals("Vehicle_txtCoownership")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("Vehicle_txtCoownershipName")){
			ra.setCursorPosition(1);
		}
        return true;
    }//VehicleDialog_Coownership_FV

	 
	 public boolean VehicleDialog_Owner_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
	 	
	 	System.out.println("VehicleDialog  VehicleDialog_Owner_FV    1  Vlasnik vozila  Vehicle_COLL_CUS_ID Vehicle_txtOwnerRegisterNo");
		String ldbName = "VehicleDialogLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("VehicleDialogLDB","Vehicle_txtOwnerRegisterNo",null);
			ra.setAttribute("VehicleDialogLDB","Vehicle_txtOwnerName",null);
			ra.setAttribute("VehicleDialogLDB","Vehicle_COLL_CUS_ID",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("Vehicle_txtOwnerName")) {
			ra.setAttribute(ldbName, "Vehicle_txtOwnerRegisterNo", "");
		} else if (ra.getCursorPosition().equals("Vehicle_txtOwnerRegisterNo")) {
			ra.setAttribute(ldbName, "Vehicle_txtOwnerName", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "Vehicle_txtOwnerName") != null){
			d_name = (String) ra.getAttribute(ldbName, "Vehicle_txtOwnerName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Vehicle_txtOwnerRegisterNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Vehicle_txtOwnerRegisterNo");
		}
		
		if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
			ra.showMessage("wrn366");
			return false;
		}

		
		
		
		//JE LI zvjezdica na pravom mjestu kod register_no
		if (CharUtil.isAsteriskWrong(d_register_no)) {
			ra.showMessage("wrn367");
			return false;
		}
		
		
		 if (ra.getCursorPosition().equals("Vehicle_txtOwnerRegisterNo")) 
			ra.setCursorPosition(2);
					
		
		if (ra.isLDBExists("CustomerAllLookUpLDB")) {
			ra.setAttribute("CustomerAllLookUpLDB", "cus_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "register_no", "");
			ra.setAttribute("CustomerAllLookUpLDB", "code", "");
			ra.setAttribute("CustomerAllLookUpLDB", "name", "");
			ra.setAttribute("CustomerAllLookUpLDB", "add_data_table", "");
			ra.setAttribute("CustomerAllLookUpLDB", "cus_typ_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "cus_sub_typ_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", null);
			ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
		} else {
			ra.createLDB("CustomerAllLookUpLDB");
		}

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("VehicleDialogLDB", "Vehicle_txtOwnerRegisterNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("VehicleDialogLDB", "Vehicle_txtOwnerName"));

		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");
	
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		
		ra.setAttribute("VehicleDialogLDB", "Vehicle_COLL_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("VehicleDialogLDB", "Vehicle_txtOwnerRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("VehicleDialogLDB", "Vehicle_txtOwnerName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		
		if(ra.getCursorPosition().equals("Vehicle_txtOwnerRegisterNo")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("Vehicle_txtOwnerName")){
			ra.setCursorPosition(1);
		}
		
		
		return true;

		
	}//VehicleDialog_Owner_FV
	 
	 
	 public boolean VehicleDialog_Carrier_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
	 	
	 	System.out.println("VehicleDialog  VehicleDialog_Owner_FV    1  Vlasnik vozila  Vehicle_CUS_ID Vehicle_txtCarrierRegisterNo");
		String ldbName = "VehicleDialogLDB";
		
		if (elementValue == null || ((String) elementValue).equals("")) {
			ra.setAttribute("VehicleDialogLDB","Vehicle_txtCarrierRegisterNo",null);
			ra.setAttribute("VehicleDialogLDB","Vehicle_txtCarrierName",null);
			ra.setAttribute("VehicleDialogLDB","Vehicle_CUS_ID",null);
			return true;
		}
		
		
		if (ra.getCursorPosition().equals("Vehicle_txtCarrierName")) {
			ra.setAttribute(ldbName, "Vehicle_txtCarrierRegisterNo", "");
		} else if (ra.getCursorPosition().equals("Vehicle_txtCarrierRegisterNo")) {
			ra.setAttribute(ldbName, "Vehicle_txtCarrierName", "");
			//ra.setCursorPosition(2);
		}
		
		String d_name = "";
		if (ra.getAttribute(ldbName, "Vehicle_txtCarrierName") != null){
			d_name = (String) ra.getAttribute(ldbName, "Vehicle_txtCarrierName");
		}
		
		String d_register_no = "";
		if (ra.getAttribute(ldbName, "Vehicle_txtCarrierRegisterNo") != null){
			d_register_no = (String) ra.getAttribute(ldbName, "Vehicle_txtCarrierRegisterNo");
		}
		
		if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
			ra.showMessage("wrn366");
			return false;
		}

		
		
		
		//JE LI zvjezdica na pravom mjestu kod register_no
		if (CharUtil.isAsteriskWrong(d_register_no)) {
			ra.showMessage("wrn367");
			return false;
		}
		
		
		 if (ra.getCursorPosition().equals("Vehicle_txtCarrierRegisterNo")) 
			ra.setCursorPosition(2);
					
		
		if (ra.isLDBExists("CustomerAllLookUpLDB")) {
			ra.setAttribute("CustomerAllLookUpLDB", "cus_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "register_no", "");
			ra.setAttribute("CustomerAllLookUpLDB", "code", "");
			ra.setAttribute("CustomerAllLookUpLDB", "name", "");
			ra.setAttribute("CustomerAllLookUpLDB", "add_data_table", "");
			ra.setAttribute("CustomerAllLookUpLDB", "cus_typ_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "cus_sub_typ_id", null);
			ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", null);
			ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
		} else {
			ra.createLDB("CustomerAllLookUpLDB");
		}

		ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCarrierRegisterNo"));
		ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCarrierName"));

		LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
		lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");
	
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		
		ra.setAttribute("VehicleDialogLDB", "Vehicle_CUS_ID", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
		ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCarrierRegisterNo", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
		ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCarrierName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
		if(ra.getCursorPosition().equals("Vehicle_txtCarrierRegisterNo")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("Vehicle_txtCarrierName")){
			ra.setCursorPosition(1);
		}
			
		return true;

		
	}//VehicleDialog_Carrier_FV
	 


		
	public boolean VehicleDialog_Estimator_FV(String elementName, Object elementValue, Integer lookUpType) {
			String ldbName = "VehicleDialogLDB";
			
			if (elementValue == null || ((String) elementValue).equals("")) {
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtEUseIdLogin",null);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtEUseIdName",null);
				ra.setAttribute("VehicleDialogLDB","VEH_EST_ABC",null);
				return true;
			}
			
			
			if (ra.getCursorPosition().equals("Vehicle_txtEUseIdName")) {
				ra.setAttribute(ldbName, "Vehicle_txtEUseIdLogin", "");
			} else if (ra.getCursorPosition().equals("Vehicle_txtEUseIdLogin")) {
				ra.setAttribute(ldbName, "Vehicle_txtEUseIdName", "");
				//ra.setCursorPosition(2);
			}
			
			String d_name = "";
			if (ra.getAttribute(ldbName, "Vehicle_txtEUseIdName") != null){
				d_name = (String) ra.getAttribute(ldbName, "Vehicle_txtEUseIdName");
			}
			
			String d_register_no = "";
			if (ra.getAttribute(ldbName, "Vehicle_txtEUseIdLogin") != null){
				d_register_no = (String) ra.getAttribute(ldbName, "Vehicle_txtEUseIdLogin");
			}
			
			if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
				ra.showMessage("wrn366");
				return false;
			}

			
			
			
			//JE LI zvjezdica na pravom mjestu kod register_no
			if (CharUtil.isAsteriskWrong(d_register_no)) {
				ra.showMessage("wrn367");
				return false;
			}
			
			
			 if (ra.getCursorPosition().equals("Vehicle_txtEUseIdLogin")) 
				ra.setCursorPosition(2);
						
			
			if (ra.isLDBExists("CustomerAllLookUpLDB")) {
				ra.setAttribute("CustomerAllLookUpLDB", "cus_id", null);
				ra.setAttribute("CustomerAllLookUpLDB", "register_no", "");
				ra.setAttribute("CustomerAllLookUpLDB", "code", "");
				ra.setAttribute("CustomerAllLookUpLDB", "name", "");
				ra.setAttribute("CustomerAllLookUpLDB", "add_data_table", "");
				ra.setAttribute("CustomerAllLookUpLDB", "cus_typ_id", null);
				ra.setAttribute("CustomerAllLookUpLDB", "cus_sub_typ_id", null);
				ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", null);
				ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
			} else {
				ra.createLDB("CustomerAllLookUpLDB");
			}

			ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute("VehicleDialogLDB", "Vehicle_txtEUseIdLogin"));
			ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute("VehicleDialogLDB", "Vehicle_txtEUseIdName"));

			LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
			lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");
		
			try {
				ra.callLookUp(lookUpRequest);
			} catch (EmptyLookUp elu) {
				ra.showMessage("err012");
				return false;
			} catch (NothingSelected ns) {
				return false;
			}
			
			ra.setAttribute("VehicleDialogLDB", "VEH_EST_ABC", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
			ra.setAttribute("VehicleDialogLDB", "Vehicle_txtEUseIdLogin", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
			ra.setAttribute("VehicleDialogLDB", "Vehicle_txtEUseIdName", ra.getAttribute("CustomerAllLookUpLDB", "name"));
				
			return true;
		}//VehicleDialog_Estimator_FV

	public boolean VehicleDialog_VehicleType_FV(String ElName, Object ElValue, Integer LookUp) {              
       if (ElValue == null || ((String) ElValue).equals("")) {  
					ra.setAttribute("VehicleDialogLDB", "VEH_TYPE_ID", null);     
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtVetMake", null);                                         
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtVetType", null);                        
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtVetSizeType", null);
					ra.setAttribute("VehicleDialogLDB", "dummyDate", null); 
					return true;                                                                                 
		}                                                                                              
        
			LookUpRequest lookUpRequest = new LookUpRequest("VehicleTypeLookUp");                       
			lookUpRequest.addMapping("VehicleDialogLDB", "VEH_TYPE_ID", "vet_type_id");   
			lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtVetMake", "vet_make");
		 	lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtVetType", "vet_type");
		 	lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtVetSizeType", "vet_size_type");
		 	lookUpRequest.addMapping("VehicleDialogLDB", "dummyDate", "vet_date_from");
		 	lookUpRequest.addMapping("VehicleDialogLDB", "dummyDate", "vet_date_unti");
		 	
		 	
		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}   
		 ra.setAttribute("VehicleDialogLDB", "dummyDate", null);
		 if(ra.getCursorPosition().equals("Vehicle_txtVetType")){
			ra.setCursorPosition(3);
		 }
		 if(ra.getCursorPosition().equals("Vehicle_txtVetSizeType")){
			ra.setCursorPosition(2);
		 }
		 if(ra.getCursorPosition().equals("Vehicle_txtVetMake")){
			ra.setCursorPosition(1);
		 }
		return true;                                                                                   
    
	}//VehicleDialog_VehicleType_FV     

	 
	public boolean VehicleDialog_EngineType_FV(String ElName, Object ElValue, Integer LookUp) {              
	       if (ElValue == null || ((String) ElValue).equals("")) {  
						ra.setAttribute("VehicleDialogLDB", "VEH_ENGINE_TYPE", null);     
						ra.setAttribute("VehicleDialogLDB", "Vehicle_txtEngMake", null);                                         
						ra.setAttribute("VehicleDialogLDB", "Vehicle_txtEngType", null);                        
						ra.setAttribute("VehicleDialogLDB", "Vehicle_txtEngFuelType", null); 
						ra.setAttribute("VehicleDialogLDB", "dummyDate", null); 
						
						return true;                                                                                 
			}                                                                                              
	       		ra.setAttribute("VehicleDialogLDB", "dummySt", null); 
	       
	       
				LookUpRequest lookUpRequest = new LookUpRequest("EngineTypeLookUp");                       
				lookUpRequest.addMapping("VehicleDialogLDB", "VEH_ENGINE_TYPE", "eng_type_id");   
				lookUpRequest.addMapping("VehicleDialogLDB", "dummySt", "eng_make");
			 	lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtEngType", "eng_type");
			 	lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtEngFuelType", "eng_fuel_type");
			 	lookUpRequest.addMapping("VehicleDialogLDB", "dummyDate", "eng_date_from");
			 	lookUpRequest.addMapping("VehicleDialogLDB", "dummyDate", "eng_date_unti");
			 	
			try {                                                                                          
				ra.callLookUp(lookUpRequest);                                                              
			} catch (EmptyLookUp elu) {                                                                    
					ra.showMessage("err012");                                                                  
					return false;                                                                              
			} catch (NothingSelected ns) {                                                                 
					return false;                                                                              
			} 
			
			if(ra.getCursorPosition().equals("Vehicle_txtEngType")){
				ra.setCursorPosition(2);
			}
			
			 ra.setAttribute("VehicleDialogLDB", "dummySt", null);
			 ra.setAttribute("VehicleDialogLDB", "dummyDate", null);
			return true;                                                                                   
	    
	}//VehicleDialog_EngineType_FV     
	 
	
	
	public boolean VehicleDialog_Vehicle_txtPlaceCode_FV(String ElName, Object ElValue, Integer LookUp) {              
        
		java.math.BigDecimal polMapTypId = new java.math.BigDecimal("5999.0");

		if (ElValue == null || ((String) ElValue).equals("")) {                                          
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtPlaceName", "");                        
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtPlaceCode", "");                        
					ra.setAttribute("VehicleDialogLDB", "VEH_PLACE", null);    
					
					                           
					return true;                                                                                 
		}                                                                                              
        
		if (!ra.isLDBExists("PolMapPlacesInCountyLookUpLDB")) {
	 		ra.createLDB("PolMapPlacesInCountyLookUpLDB");
	 	}
	 	
		
		java.math.BigDecimal countyId = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB","VEH_COUNTY");
		
		ra.setAttribute("PolMapPlacesInCountyLookUpLDB", "bDCountyId",countyId );
		
		LookUpRequest lookUpRequest = new LookUpRequest("PolMapPlacesInCountyLookUp");                       
		lookUpRequest.addMapping("VehicleDialogLDB", "VEH_PLACE", "pol_map_id");           
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtPlaceCode", "code");
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtPlaceName", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}
		if(ra.getCursorPosition().equals("Vehicle_txtPlaceCode")){
			ra.setCursorPosition(2);
		}
		polMapTypId =  null;
		return true;                                                                                   
    
	}//VehicleDialog_Vehicle_txtPlaceCode_FV

	
	public boolean VehicleDialog_DatnUnti_FV(){ 
		java.sql.Date pocetak = null;
		java.sql.Date kraj = null;
		if(ra.getScreenContext().compareTo("scr_change")== 0){ 
			pocetak = (java.sql.Date)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDatnFrom");
			kraj = (java.sql.Date)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDatnUnti");
			
			if(kraj.before(pocetak)){
				ra.showMessage("wrnclt31");
				pocetak = null;
				kraj = null;
				return false;
			}
		}
		pocetak = null;
		kraj = null;
		return true;
	
	}//VehicleDialog_DatnUnti_FV
	
	public boolean VehicleDialog_EstnDate_FV(){ 
		java.sql.Date pocetak = null;
		java.sql.Date kraj = null;
		java.sql.Date datumProcjene = null;
		if(ra.getScreenContext().compareTo("scr_change")== 0){ 
			
			
			pocetak = (java.sql.Date)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDatnFrom");
			kraj = (java.sql.Date)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDatnUnti");
			datumProcjene = (java.sql.Date)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtEstnDate");
			if(datumProcjene.after(kraj)){
				ra.showMessage("wrnclt30");
				pocetak = null;
				kraj = null;
				datumProcjene = null;
				return false;
			}
		}
		pocetak = null;
		kraj = null;
		datumProcjene = null;
		return true;
	
	}//VehicleDialog_EstnDate_FV
	
	
	
	
	
	public boolean VehicleDialog_Vehicle_txtDistrictCode_FV(String ElName, Object ElValue, Integer LookUp) {              
        
		java.math.BigDecimal polMapTypId = new java.math.BigDecimal("5854877003.0");

		if (ElValue == null || ((String) ElValue).equals("")) {                                          
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtDistrictName", "");                        
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtDistrictCode", "");                        
					ra.setAttribute("VehicleDialogLDB", "VEH_DISTRICT", null);    
					
					                           
					return true;                                                                                 
		}                                                                                              
         
		
		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
	 		ra.createLDB("PoliticalMapByTypIdLookUpLDB");
	 	}
	 	
		
		ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId",polMapTypId );
		
		
		
		
		LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");                       
		lookUpRequest.addMapping("VehicleDialogLDB", "VEH_DISTRICT", "pol_map_id");           
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtDistrictCode", "code");
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtDistrictName", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}
		if(ra.getCursorPosition().equals("Vehicle_txtDistrictCode")){
			ra.setCursorPosition(2);
		}
		polMapTypId =  null;
		return true;                                                                                   
    
	}//VehicleDialog_Vehicle_txtDistrictCode_FV

	public boolean VehicleDialog_Vehicle_txtResiQuarCode_FV(String ElName, Object ElValue, Integer LookUp) {              
		         
		java.math.BigDecimal polMapTypId = new java.math.BigDecimal("5854878003.0");

		if (ElValue == null || ((String) ElValue).equals("")) {                                          
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtResiQuarName", "");                        
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtResiQuarCode", "");                        
					ra.setAttribute("VehicleDialogLDB", "VEH_RESI_QUAR", null);    
					
					                           
					return true;                                                                                 
		}                                                                                              
         
		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
	 		ra.createLDB("PoliticalMapByTypIdLookUpLDB");
	 	}
	 	
		
		ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId",polMapTypId );
		
		LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");                       
		lookUpRequest.addMapping("VehicleDialogLDB", "VEH_RESI_QUAR", "pol_map_id");           
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtResiQuarCode", "code");
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtResiQuarName", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}
		if(ra.getCursorPosition().equals("Vehicle_txtResiQuarCode")){
			ra.setCursorPosition(2);
		}
		polMapTypId =  null;
		return true;                                                                                   
    
	}//VehicleDialog_Vehicle_txtResiQuarCode_FV

	public boolean VehicleDialog_Vehicle_txtCountyCode_FV(String ElName, Object ElValue, Integer LookUp) {              
        
		java.math.BigDecimal polMapTypId = new java.math.BigDecimal("3999.0");

		if (ElValue == null || ((String) ElValue).equals("")) {                                          
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCountyName", "");                        
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtCountyCode", "");                        
					ra.setAttribute("VehicleDialogLDB", "VEH_COUNTY", null);    
					
					                           
					return true;                                                                                 
		}                                                                                              
        
		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
	 		ra.createLDB("PoliticalMapByTypIdLookUpLDB");
	 	}
	 	
		ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId",polMapTypId );
		
		LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");                       
		lookUpRequest.addMapping("VehicleDialogLDB", "VEH_COUNTY", "pol_map_id");           
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCountyCode", "code");
		lookUpRequest.addMapping("VehicleDialogLDB", "Vehicle_txtCountyName", "name");

		try {                                                                                          
			ra.callLookUp(lookUpRequest);                                                              
		} catch (EmptyLookUp elu) {                                                                    
				ra.showMessage("err012");                                                                  
				return false;                                                                              
		} catch (NothingSelected ns) {                                                                 
				return false;                                                                              
		}
		polMapTypId =  null;
		if(ra.getCursorPosition().equals("Vehicle_txtCountyCode")){
			ra.setCursorPosition(2);
		}
		return true;                                                                                   
    
	}//VehicleDialog_Vehicle_txtCountyCode_FV

	 
	 public void confirm() {
	 	
		if((ra.getScreenContext().compareTo("scr_change")== 0)  || (ra.getScreenContext().compareTo("scr_update")== 0)    ){
			 
	 	
	 	java.sql.Date datumDoKadaPredatiDokum = null;
	 	java.sql.Date datumDoKadaUpisatiPravo = null;
	 	
	 	
	 	
	 	String amortizacijskiPeriod = null;
	 	java.math.BigDecimal amortPerCalId = null;
	 	String periodIzracunaAmortizacije = null;
	 	
	 	java.sql.Date datumOd = null;
	 	java.sql.Date datumDo = null;
	 	
	 	String eligibility = null;
	 	
	 	boolean imaNepopunjenih = false;
	 	
	 	
	 	datumDoKadaPredatiDokum = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateToDoc");
	 	datumDoKadaUpisatiPravo = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateToLop");
	 	
	 	
	 	
	 	amortizacijskiPeriod = (String) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtAmortAge");
	 	amortPerCalId = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB", "AMORT_PER_CAL_ID");
	 	periodIzracunaAmortizacije = (String) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtAmortPerCal");
	 	
	 	datumOd = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateFrom");
	 	datumDo = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateUnti");
	 	
	 	eligibility = (String) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtEligibility");
	 	
	 	if((datumDoKadaPredatiDokum == null) || (datumOd == null) ||(datumDo == null)){
	 		imaNepopunjenih = true; 
	 	}
	 	
	 	if((amortizacijskiPeriod == null) || (amortPerCalId == null)|| (periodIzracunaAmortizacije == null)){
	 		imaNepopunjenih = true; 
	 	}
	 	
	 	if(eligibility == null){
	 		imaNepopunjenih = true; 
	 	}
	 	
	 	if(imaNepopunjenih){
	 	 	ra.showMessage("infclt1"); 
	 		return;	
	 	}
       
	 	
	 	if (!(ra.isRequiredFilled())) {
            return;
        }
      
	 	
	 	
	 	
		if(ra.getScreenContext().compareTo("scr_update")== 0){
 			System.out.println("Pocetak promjene A vozila ");
	 		Integer del = (Integer) ra.showMessage("qer002");
			if (del != null && del.intValue() == 1) {
				System.out.println("Odabrao promjenu");
				try {
		 			ra.executeTransaction();
		 			ra.showMessage("infclt3");
		 		} catch (VestigoTMException vtme) {
		 			error("VehicleDialog -> update_A(): VestigoTMException", vtme);
		 			if (vtme.getMessageID() != null)
		 				ra.showMessage(vtme.getMessageID());
		 		}
			}
 		}//scr_update za transakciju
 		
 		if(ra.getScreenContext().compareTo("scr_change")== 0){
 			try {
 					ra.executeTransaction();
 					ra.showMessage("infclt2");
 			} catch (VestigoTMException vtme) {
 					error("VehicleDialog -> insert(): VestigoTMException", vtme);
 					if (vtme.getMessageID() != null)
 							ra.showMessage(vtme.getMessageID());
 			}
 		}//scr_change za transakciju
		
		
		
		
        
        
        
        //VLASNIK
        ra.setAttribute("VehicleLDB", "Veh_txtOwnerRegisterNo", (String) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtOwnerRegisterNo"));  
        ra.setAttribute("VehicleLDB", "Veh_txtOwnerName", (String) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtOwnerName"));  
        ra.setAttribute("VehicleLDB", "Veh_COLL_CUS_ID", (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB", "Vehicle_COLL_CUS_ID"));  
       
        //NOSITELJ-KORISNIK PLASMANA
        ra.setAttribute("VehicleLDB", "Veh_txtLoanOwnerRegisterNo", (String) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCarrierRegisterNo"));  
        ra.setAttribute("VehicleLDB", "Veh_txtLoanOwnerName", (String) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCarrierName"));  
        ra.setAttribute("VehicleLDB", "Veh_CUS_ID", (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB", "Vehicle_CUS_ID"));  
        
        
        datumDoKadaPredatiDokum = null;          
 		datumDoKadaUpisatiPravo = null;          
    	 	                                     
 	 	                                     
 		amortizacijskiPeriod = null;             
 		amortPerCalId = null;                    
 		periodIzracunaAmortizacije = null;       
    	 	                                     
 		datumOd = null;                          
 		datumDo = null;                          
 		imaNepopunjenih = false;
  
 	}//scr_change
        
        
        
        
		if (ra.getScreenID().equals("VehicleDialog")){
			ra.exitScreen();
			ra.refreshActionList("tblVehicle");
			////ra.invokeAction("refresh");
		}
		
		if (ra.getScreenID().equals("VehicleDialog2")){
			ra.exitScreen();
			ra.exitScreen();
			ra.refreshActionList("tblVehicle"); 
			////ra.invokeAction("refresh");
		}
		if (ra.getScreenID().equals("VehicleDialog3")){
			ra.exitScreen();
			ra.exitScreen();
			
			ra.refreshActionList("tblVehicle");
			////ra.invokeAction("refresh");			
		}
	
    }//confirm

	
	 public void postavi_nekeUserDate(){
		java.sql.Date todaySQLDate = null;
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		long timeT = calendar.getTime().getTime();
		todaySQLDate = new java.sql.Date(timeT);
		
		
		
	 	if(ra.getScreenContext().compareTo("scr_change")== 0){
	 		ra.setAttribute("VehicleDialogLDB", "USE_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
	 		ra.setAttribute("VehicleDialogLDB", "USE_OPEN_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
	 		ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
			ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
			ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseOpenIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
			ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseOpenIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
			java.sql.Date datumOd = null;
		 	datumOd = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateFrom");
		 	
		 	if(datumOd == null){
		 		ra.setAttribute("VehicleDialogLDB", "Vehicle_txtDateFrom", todaySQLDate); 
		 	}
		 	datumOd = null;
		 	calendar = null;
		 	todaySQLDate = null;
		 	
		 	java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
		 	java.sql.Date datumDo = null;
		 	datumDo = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateUnti");
		 	
		 	if(datumDo == null){
		 		ra.setAttribute("VehicleDialogLDB", "Vehicle_txtDateUnti", vvDatUntil); 
		 	}
		 	datumDo = null;
		 	vvDatUntil = null;
		}
	 	
	 	if(ra.getScreenContext().compareTo("scr_update")== 0){
	 		ra.setAttribute("VehicleDialogLDB", "USE_ID",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
	 		ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseIdLogin",(String) ra.getAttribute("GDB", "Use_Login"));
			ra.setAttribute("VehicleDialogLDB","Vehicle_txtUseIdName",(String) ra.getAttribute("GDB", "Use_UserName"));
			java.sql.Date datumOd = null;
		 	datumOd = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateFrom");
		 	
		 	if(datumOd == null){
		 		ra.setAttribute("VehicleDialogLDB", "Vehicle_txtDateFrom", todaySQLDate); 
		 	}
		 	datumOd = null;
		 	calendar = null;
		 	todaySQLDate = null;
		 	
		 	java.sql.Date vvDatUntil = java.sql.Date.valueOf("9999-12-31");
		 	java.sql.Date datumDo = null;
		 	datumDo = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateUnti");
		 	
		 	if(datumDo == null){
		 		ra.setAttribute("VehicleDialogLDB", "Vehicle_txtDateUnti", vvDatUntil); 
		 	}
		 	datumDo = null;
		 	vvDatUntil = null;
		}
		
	}//postavi_nekeUserDate

	 
	 
	 
	 
	 
	
	
}//VehicleDialog
