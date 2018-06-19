/*
 * Created on 2006.03.31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.util;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;

//import com.ibm.cics.server.LengthErrorException;

import hr.vestigo.framework.controller.handler.ResourceAccessor;
    
/**    
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollateralCmpUtil {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/util/CollateralCmpUtil.java,v 1.29 2016/08/26 11:32:35 hrazst Exp $";
  
	private ResourceAccessor ra = null;
    
 
	public CollateralCmpUtil() {
		super();
	}
	public CollateralCmpUtil(ResourceAccessor ra) {
		this.ra = ra;
	}	
	 
	  
	public void fillData(ResourceAccessor ra) {
		
		ra.setAttribute("CollOldLDB","Coll_txtDesc_O",ra.getAttribute("CollHeadLDB","Coll_txtDesc"));
//		ra.setAttribute("CollOldLDB","Coll_txtOwnerRegisterNo_O", ra.getAttribute("CollHeadLDB","Coll_txtOwnerRegisterNo"));
//		ra.setAttribute("CollOldLDB","Coll_txtCarrierRegisterNo_O", ra.getAttribute("CollHeadLDB","Coll_txtCarrierRegisterNo"));

		ra.setAttribute("CollOldLDB","Coll_txtSecTypeCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecTypeCode")); // vrsta		
	
// blok procjenitelj
//		ra.setAttribute("CollOldLDB","Coll_txtEUseIdLogin_O", ra.getAttribute("CollHeadLDB","Coll_txtEUseIdLogin"));

//		ra.setAttribute("CollOldLDB","Coll_txtEstnCurr_O", ra.getAttribute("CollHeadLDB","Coll_txtEstnCurr"));
//		ra.setAttribute("CollOldLDB","Coll_txtEstnValu_O", ra.getAttribute("CollHeadLDB","Coll_txtEstnValu"));
//		ra.setAttribute("CollOldLDB","Coll_txtEstnDate_O", ra.getAttribute("CollHeadLDB","Coll_txtEstnDate"));
//		ra.setAttribute("CollOldLDB","Coll_txtNomiDesc_O", ra.getAttribute("CollHeadLDB","Coll_txtNomiDesc"));
		
// blok collateral officer
		ra.setAttribute("CollOldLDB","Coll_txtDatnFrom_O", ra.getAttribute("CollHeadLDB","Coll_txtDatnFrom"));
		ra.setAttribute("CollOldLDB","Coll_txtDatnUnti_O", ra.getAttribute("CollHeadLDB","Coll_txtDatnUnti"));
		
		ra.setAttribute("CollOldLDB","Coll_txtNmValCurr_O", ra.getAttribute("CollHeadLDB","Coll_txtNmValCurr"));		
		
		ra.setAttribute("CollOldLDB","Coll_txtNomiValu_O", ra.getAttribute("CollHeadLDB","Coll_txtNomiValu"));		

		ra.setAttribute("CollOldLDB","Coll_txtNomiDate_O", ra.getAttribute("CollHeadLDB","Coll_txtNomiDate"));
		ra.setAttribute("CollOldLDB","Coll_txtLiquValu_O", ra.getAttribute("CollHeadLDB","Coll_txtLiquValu"));
		ra.setAttribute("CollOldLDB","Coll_txtLiquDate_O", ra.getAttribute("CollHeadLDB","Coll_txtLiquDate"));
		ra.setAttribute("CollOldLDB","Coll_txtBptcValu_O", ra.getAttribute("CollHeadLDB","Coll_txtBptcValu"));
		ra.setAttribute("CollOldLDB","Coll_txtBptcDate_O", ra.getAttribute("CollHeadLDB","Coll_txtBptcDate"));
		ra.setAttribute("CollOldLDB","Coll_txtOriginalVal_O", ra.getAttribute("CollHeadLDB","Coll_txtOriginalVal"));
		ra.setAttribute("CollOldLDB","Coll_txtOrigValDate_O", ra.getAttribute("CollHeadLDB","Coll_txtOrigValDate"));
		ra.setAttribute("CollOldLDB","Coll_txtAddData_O", ra.getAttribute("CollHeadLDB","Coll_txtAddData"));
	
// blok prihvatljivosti 
		ra.setAttribute("CollOldLDB","Coll_txtEligibility_O", ra.getAttribute("CollHeadLDB","Coll_txtEligibility"));		
		
		 
// blok dokumentacija		
//		ra.setAttribute("CollOldLDB","Coll_txtComDoc_O", ra.getAttribute("CollHeadLDB","Coll_txtComDoc"));
//		ra.setAttribute("CollOldLDB","Coll_txtMissingDoc_O", ra.getAttribute("CollHeadLDB","Coll_txtMissingDoc"));
//		ra.setAttribute("CollOldLDB","Coll_txtDateToDoc_O", ra.getAttribute("CollHeadLDB","Coll_txtDateToDoc"));
//		ra.setAttribute("CollOldLDB","Coll_txtDateRecDoc_O", ra.getAttribute("CollHeadLDB","Coll_txtDateRecDoc"));
		
// 	blok upis prava
//		ra.setAttribute("CollOldLDB","Coll_txtDateToLop_O", ra.getAttribute("CollHeadLDB","Coll_txtDateToLop"));		

//		ra.setAttribute("CollOldLDB","Coll_txtDateRecLop_O", ra.getAttribute("CollHeadLDB","Coll_txtDateRecLop"));
//		ra.setAttribute("CollOldLDB","Coll_txtRecLop_O", ra.getAttribute("CollHeadLDB","Coll_txtRecLop"));
		
// plovila
		
		ra.setAttribute("CollOldLDB","Vehicle_txtVehPlate_O", ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehPlate"));
		ra.setAttribute("CollOldLDB","Vehicle_txtVehVehLicence_O", ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehVehLicence"));
		ra.setAttribute("CollOldLDB","Vehicle_txtVehMadeDate_O", ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehMadeDate"));
		ra.setAttribute("CollOldLDB","Vehicle_txtVehColour_O", ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehColour"));
		ra.setAttribute("CollOldLDB","Vessel_txtNumEngine_O", ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtNumEngine"));
		ra.setAttribute("CollOldLDB","Vessel_txtVehPowerKW_O", ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtVehPowerKW"));
		ra.setAttribute("CollOldLDB","Vessel_txtVehKmTrav_O", ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtVehKmTrav"));
		ra.setAttribute("CollOldLDB","Vehicle_txtVehEquipment_O", ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehEquipment"));
		ra.setAttribute("CollOldLDB","Vessel_txtHarbour_O", ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtHarbour"));
		ra.setAttribute("CollOldLDB","Vessel_txtHarbourSec_O", ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtHarbourSec"));
		
		
// police osiguranja
		ra.setAttribute("CollOldLDB","Coll_txtInsPolNumber_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtInsPolNumber"));
		ra.setAttribute("CollOldLDB","Coll_txtInsPolRegNo_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtInsPolRegNo"));
		
		
// 	zlato, dijamanti, plemenite kovine
		ra.setAttribute("CollOldLDB","Coll_txtMovUnitMes_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovUnitMes"));
		ra.setAttribute("CollOldLDB","Coll_txtMovNumOfUnit_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovNumOfUnit"));
		ra.setAttribute("CollOldLDB","Coll_txtArtYear_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtArtYear"));
		ra.setAttribute("CollOldLDB","Coll_txtMovCondition_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondition"));
		ra.setAttribute("CollOldLDB","Coll_txtMovCondDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondDate"));
		
		
// umjetnine
		ra.setAttribute("CollOldLDB","Coll_txtMovUnitMes_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovUnitMes"));
		ra.setAttribute("CollOldLDB","Coll_txtMovNumOfUnit_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovNumOfUnit"));		
		ra.setAttribute("CollOldLDB","Coll_txtArtPeriod_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtArtPeriod"));
		ra.setAttribute("CollOldLDB","Coll_txtArtYear_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtArtYear"));
		ra.setAttribute("CollOldLDB","Coll_txtArtAuthor_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtArtAuthor"));
		ra.setAttribute("CollOldLDB","Coll_txtMovCondition_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondition"));
		ra.setAttribute("CollOldLDB","Coll_txtMovCondDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondDate"));
		
//	zalihe
		ra.setAttribute("CollOldLDB","Coll_txtMovUnitMes_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovUnitMes"));		
		ra.setAttribute("CollOldLDB","Coll_txtMovNumOfUnit_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovNumOfUnit"));
		ra.setAttribute("CollOldLDB","Coll_txtMovCondition_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondition"));
		ra.setAttribute("CollOldLDB","Coll_txtMovCondDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondDate"));		
		
		
// pokretnine i oprema
		ra.setAttribute("CollOldLDB","Coll_txtMovUnitMes_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovUnitMes"));		
		ra.setAttribute("CollOldLDB","Coll_txtMovNumOfUnit_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovNumOfUnit"));
		ra.setAttribute("CollOldLDB","Coll_txtMovCondition_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondition"));
		ra.setAttribute("CollOldLDB","Coll_txtMovCondDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondDate"));				
		
		

// gotovinski depoziti		

		ra.setAttribute("CollOldLDB","Coll_txtCdeBank_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeBank"));
		ra.setAttribute("CollOldLDB","Coll_txtCdeDepFrom_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeDepFrom"));
		ra.setAttribute("CollOldLDB","Coll_txtCdeDepUnti_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeDepUnti"));

		ra.setAttribute("CollOldLDB","Coll_txtCdeAmount_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeAmount"));
		ra.setAttribute("CollOldLDB","Coll_txtCdeCurr_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeCurr"));
//		ra.setAttribute("CollOldLDB","Coll_txtCdePeriod_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdePeriod"));
		ra.setAttribute("CollOldLDB","Coll_txtCdeProlong_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeProlong"));
// loro garancije
		ra.setAttribute("CollOldLDB","Coll_txtGuarIssuer_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarIssuer"));
		ra.setAttribute("CollOldLDB","Coll_txtGuarIssCouNum_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarIssCouNum"));
		ra.setAttribute("CollOldLDB","Coll_txtGuarDatnFrom_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarDatnFrom"));
		ra.setAttribute("CollOldLDB","Coll_txtGuarDatnUnti_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarDatnUnti"));
		ra.setAttribute("CollOldLDB","Coll_txtGuarExpPeriod_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarExpPeriod")); 
		ra.setAttribute("CollOldLDB","Coll_txtGuarAddData_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarAddData"));

// zaduznice
		ra.setAttribute("CollOldLDB","Coll_txtPlaceCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPlaceCode"));
		ra.setAttribute("CollOldLDB","Coll_txtStockIssueDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtStockIssueDate"));
		ra.setAttribute("CollOldLDB","Coll_txtStockAmount_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtStockAmount"));
		ra.setAttribute("CollOldLDB","Coll_txtStockCurr_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtStockCurr"));
		ra.setAttribute("CollOldLDB","Coll_txtPayeeRegNo_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayeeRegNo")); 
		ra.setAttribute("CollOldLDB","Coll_txtPayeeCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayeeCode"));
		ra.setAttribute("CollOldLDB","Coll_txtPayeeData1_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayeeData1"));
		ra.setAttribute("CollOldLDB","Coll_txtPayeeRole_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayeeRole")); 
//		ra.setAttribute("CollOldLDB","Coll_txtPenaltyInt_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPenaltyInt"));			
//		ra.setAttribute("CollOldLDB","Coll_txtStockMatDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtStockMatDate"));	
		ra.setAttribute("CollOldLDB","Coll_txtAccNo_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtAccNo"));
		ra.setAttribute("CollOldLDB","Coll_txtReprRegNo_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtReprRegNo"));
		ra.setAttribute("CollOldLDB","Coll_txtReprRole_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtReprRole"));

// vrijednosnice
		ra.setAttribute("CollOldLDB","Coll_txtSecName_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecName"));
		ra.setAttribute("CollOldLDB","Coll_txtSecShortName_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecShortName"));
		ra.setAttribute("CollOldLDB","Coll_txtIssuerName_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIssuerName"));
		ra.setAttribute("CollOldLDB","Coll_txtGuaranteeName_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuaranteeName"));
		ra.setAttribute("CollOldLDB","Coll_txtSecPapCountryCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecPapCountryCode"));
		ra.setAttribute("CollOldLDB","Coll_txtSerialNo_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSerialNo"));
		ra.setAttribute("CollOldLDB","Coll_txtTicker_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtTicker"));
		ra.setAttribute("CollOldLDB","Coll_txtISIN_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtISIN"));
		ra.setAttribute("CollOldLDB","Coll_txtIssueAmountCur_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIssueAmountCur"));
		ra.setAttribute("CollOldLDB","Coll_txtIssueAmount_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIssueAmount"));
		ra.setAttribute("CollOldLDB","Coll_txtNominalAmount_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNominalAmount"));
		ra.setAttribute("CollOldLDB","Coll_txtNumOfSecIssued_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNumOfSecIssued"));
		ra.setAttribute("CollOldLDB","Coll_txtNumOfSec_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNumOfSec"));
		ra.setAttribute("CollOldLDB","Coll_txtRefMarketCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtRefMarketCode"));
		ra.setAttribute("CollOldLDB","Coll_txtIssueDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIssueDate"));
		ra.setAttribute("CollOldLDB","Coll_txtMaturityDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMaturityDate"));
		ra.setAttribute("CollOldLDB","Coll_txtGuarExpPer_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarExpPer"));
		ra.setAttribute("CollOldLDB","Coll_txtMarketPriceFo_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMarketPriceFo"));
		ra.setAttribute("CollOldLDB","Coll_txtMarketPriceDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMarketPriceDate"));
		ra.setAttribute("CollOldLDB","Coll_txtAccruedInterest_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtAccruedInterest"));
		ra.setAttribute("CollOldLDB","Coll_txtTMarketValueDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtTMarketValueDate"));
		ra.setAttribute("CollOldLDB","Coll_txtPayTypeCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayTypeCode"));
		ra.setAttribute("CollOldLDB","Coll_txtPayFreqCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayFreqCode"));
		ra.setAttribute("CollOldLDB","Coll_txtIntRateTypeCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIntRateTypeCode"));
		ra.setAttribute("CollOldLDB","Coll_txtIntCalcTypeCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIntCalcTypeCode"));
		ra.setAttribute("CollOldLDB","Coll_txtIntCalcMethCode_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCoupon"));
		ra.setAttribute("CollOldLDB","Coll_txtCoupon_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCoupon"));
		ra.setAttribute("CollOldLDB","Coll_txtAmortizationFactor_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtAmortizationFactor"));
		ra.setAttribute("CollOldLDB","Coll_txtReceiptDate_O", ra.getAttribute("CollSecPaperDialogLDB","Coll_txtReceiptDate"));
		
// 		
		
		 
		
	}
	
	public boolean cmpData(ResourceAccessor ra) {

		
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtDesc_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtDesc"))) 
			return true;
//		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtOwnerRegisterNo_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtOwnerRegisterNo"))) 
//			return true;
//		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtCarrierRegisterNo_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtCarrierRegisterNo")))
//			return true;	
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtSecTypeCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecTypeCode")))
			return true;
		
		
//		 blok procjenitelj
/*		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtEUseIdLogin_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtEUseIdLogin")))
			return true;
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtDatnFrom_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtDatnFrom")))
				return true;
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtDatnUnti_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtDatnUnti")))
			return true;
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtEstnCurr_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtEstnCurr")))
			return true;
		if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtEstnValu_O"), (BigDecimal)ra.getAttribute("CollHeadLDB","Coll_txtEstnValu")))
			return true;
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtEstnDate_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtEstnDate")))
				return true;
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtNomiDesc_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtNomiDesc")))
			return true;*/
		
//		 blok collateral officer
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtDatnFrom_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtDatnFrom")))
			return true;
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtDatnUnti_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtDatnUnti")))
		return true;
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtNmValCurr_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtNmValCurr")))
			return true;		
		
		if (cmp_bdc((BigDecimal) ra.getAttribute("CollOldLDB","Coll_txtNomiValu_O"), (BigDecimal)ra.getAttribute("CollHeadLDB","Coll_txtNomiValu"))) 
			return true;
		if (cmp_dte((java.sql.Date) ra.getAttribute("CollOldLDB","Coll_txtNomiDate_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtNomiDate")))
			return true;
		if (cmp_bdc((BigDecimal) ra.getAttribute("CollOldLDB","Coll_txtLiquValu_O"), (BigDecimal) ra.getAttribute("CollHeadLDB","Coll_txtLiquValu")))
			return true;
		if (cmp_dte((java.sql.Date) ra.getAttribute("CollOldLDB","Coll_txtLiquDate_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtLiquDate")))
			return true;
		if (cmp_bdc((BigDecimal) ra.getAttribute("CollOldLDB","Coll_txtBptcValu_O"), (BigDecimal) ra.getAttribute("CollHeadLDB","Coll_txtBptcValu")))
			return true;
		if (cmp_dte((java.sql.Date) ra.getAttribute("CollOldLDB","Coll_txtBptcDate_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtBptcDate")))
			return true;
		if (cmp_bdc((BigDecimal) ra.getAttribute("CollOldLDB","Coll_txtOriginalVal_O"), (BigDecimal) ra.getAttribute("CollHeadLDB","Coll_txtOriginalVal")))
			return true;
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtOrigValDate_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtOrigValDate")))
			return true;
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtAddData_O"), (String) ra.getAttribute("CollHeadLDB","Coll_txtAddData")))
			return true;

//		 blok prihvatljivosti 
 
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtEligibility_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtEligibility")))
			return true;	
		
//		 blok dokumentacija		
/*		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtComDoc_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtComDoc")))
			return true;
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMissingDoc_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtMissingDoc")))
			return true;
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtDateToDoc_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtDateToDoc")))
			return true;
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtDateRecDoc_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtDateRecDoc")))		
			return true; */
//	 	blok upis prava		
/*		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtDateToLop_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtDateToLop"))) 
			return true;		
		if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtDateRecLop_O"), (java.sql.Date)ra.getAttribute("CollHeadLDB","Coll_txtDateRecLop")))
			return true;
		if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtRecLop_O"), (String)ra.getAttribute("CollHeadLDB","Coll_txtRecLop")))
			return true;*/

//		String vrsta = (String) ra.getAttribute("CollSecPaperLDB","CollateralSubCategory");
		String vrsta = (String) ra.getAttribute("ColWorkListLDB","code");
		
		if (vrsta.equalsIgnoreCase("OBVE") || vrsta.equalsIgnoreCase("DION") || 
			vrsta.equalsIgnoreCase("ZAPI") || vrsta.equalsIgnoreCase("UDJE")) {
//			 vrijednosnice
			if (cmp_str((String) ra.getAttribute("CollOldLDB","Coll_txtSecName_O"), (String) ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecName")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtSecShortName_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecShortName")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtIssuerName_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIssuerName")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtGuaranteeName_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuaranteeName")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtSecPapCountryCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecPapCountryCode")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtSerialNo_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSerialNo")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtTicker_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtTicker")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtISIN_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtISIN")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtIssueAmountCur_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIssueAmountCur")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtIssueAmount_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIssueAmount")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtNominalAmount_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNominalAmount")))
				return true;
			if (cmp_int((Integer)ra.getAttribute("CollOldLDB","Coll_txtNumOfSecIssued_O"), (Integer)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNumOfSecIssued")))
				return true;
			if (cmp_int((Integer)ra.getAttribute("CollOldLDB","Coll_txtNumOfSec_O"), (Integer)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtNumOfSec")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtRefMarketCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtRefMarketCode")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtIssueDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIssueDate")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtMaturityDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMaturityDate")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtGuarExpPer_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarExpPer")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtMarketPriceFo_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMarketPriceFo")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtMarketPriceDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMarketPriceDate")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtAccruedInterest_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtAccruedInterest")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtTMarketValueDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtTMarketValueDate")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtPayTypeCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayTypeCode")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtPayFreqCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayFreqCode")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtIntRateTypeCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIntRateTypeCode")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtIntCalcTypeCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIntCalcTypeCode")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtIntCalcMethCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtIntCalcMethCode")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtCoupon_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCoupon")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtAmortizationFactor_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtAmortizationFactor")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtReceiptDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtReceiptDate")))
				return true;
		}
			
		else if (vrsta.equalsIgnoreCase("POKR")) {
//			 pokretnine i oprema
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMovUnitMes_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovUnitMes")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtMovNumOfUnit_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovNumOfUnit")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMovCondition_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondition")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtMovCondDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondDate")))
				return true;
		}
			 
		else if (vrsta.equalsIgnoreCase("ZALI")) {
//			zalihe
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMovUnitMes_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovUnitMes")))
				return true;	
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtMovNumOfUnit_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovNumOfUnit")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMovCondition_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondition")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtMovCondDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondDate")))
				return true;		
		}
			
		else if (vrsta.equalsIgnoreCase("UMJE")) {
//			 umjetnine
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMovUnitMes_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovUnitMes")))
				return true;	
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtMovNumOfUnit_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovNumOfUnit")))
				return true;	
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtArtPeriod_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtArtPeriod")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtArtYear_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtArtYear")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtArtAuthor_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtArtAuthor")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMovCondition_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondition")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtMovCondDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondDate")))
				return true;			
		}
					
		else if (vrsta.equalsIgnoreCase("ZLAT")) {
//		 	zlato, dijamanti, plemenite kovine
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMovUnitMes_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovUnitMes")))
				return true;	
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtMovNumOfUnit_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovNumOfUnit")))
				return true;	
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtArtYear_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtArtYear")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtMovCondition_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondition")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtMovCondDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtMovCondDate")))
				return true;		
			
			
		}
					
		else if (vrsta.equalsIgnoreCase("PLOV")) {
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Vehicle_txtVehPlate_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehPlate")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Vehicle_txtVehVehLicence_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehVehLicence")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Vehicle_txtVehMadeDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehMadeDate")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Vehicle_txtVehColour_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehColour")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Vessel_txtNumEngine_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtNumEngine")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Vessel_txtVehPowerKW_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtVehPowerKW")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Vessel_txtVehKmTrav_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtVehKmTrav")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Vehicle_txtVehEquipment_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Vehicle_txtVehEquipment")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Vessel_txtHarbour_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtHarbour")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Vessel_txtHarbourSec_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Vessel_txtHarbourSec")))
				return true;
		}
			
		else if (vrsta.equalsIgnoreCase("CASH")) {
//			 gotovinski depoziti		

			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtCdeBank_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeBank")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtCdeDepFrom_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeDepFrom")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtCdeDepUnti_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeDepUnti")))
				return true;
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtCdeAmount_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeAmount")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtCdeCurr_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeCurr")))
				return true;
//			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtCdePeriod_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdePeriod")))
//				return true;			
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtCdeProlong_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtCdeProlong")))
				return true;			
			
		}
					
		else if (vrsta.equalsIgnoreCase("INSP")) {
//			 police osiguranja
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtInsPolNumber_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtInsPolNumber")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtInsPolRegNo_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtInsPolRegNo")))
				return true;
		}
					
		else if (vrsta.equalsIgnoreCase("GARA")) {
//			 loro garancije
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtGuarIssuer_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarIssuer")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtGuarIssCouNum_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarIssCouNum")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtGuarDatnFrom_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarDatnFrom")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtGuarDatnUnti_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarDatnUnti")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtGuarExpPeriod_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarExpPeriod")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtGuarAddData_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtGuarAddData")))
				return true;
		}
					
		else if (vrsta.equalsIgnoreCase("ZADU")) {
//			 zaduznice
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtPlaceCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPlaceCode")))
				return true;
			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtStockIssueDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtStockIssueDate")))
				return true;			
			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtStockAmount_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtStockAmount")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtStockCurr_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtStockCurr")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtPayeeRegNo_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayeeRegNo")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtPayeeCode_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayeeCode")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtPayeeData1_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayeeData1")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtPayeeRole_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPayeeRole")))
				return true;
//			if (cmp_bdc((BigDecimal)ra.getAttribute("CollOldLDB","Coll_txtPenaltyInt_O"), (BigDecimal)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtPenaltyInt")))
//				return true;			
//			if (cmp_dte((java.sql.Date)ra.getAttribute("CollOldLDB","Coll_txtStockMatDate_O"), (java.sql.Date)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtStockMatDate")))
//				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtAccNo_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtAccNo")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtReprRegNo_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtReprRegNo")))
				return true;
			if (cmp_str((String)ra.getAttribute("CollOldLDB","Coll_txtReprRole_O"), (String)ra.getAttribute("CollSecPaperDialogLDB","Coll_txtReprRole")))
				return true;
		
		}	
		
		return false; 
	}	  
	  
	
//	 usporedjivanje dva stringa
//   ako su razlicita vraca true	 
	 public boolean cmp_str(String str1, String str2) {
	            
	 	if ( (str1 == null) && (str2 == null) ) {
	 		return false;
		} else if ( (str1 != null) && (str2 == null) ) {
			return true;
		} else if ( (str1 == null) && (str2 != null) ) {
			return true;
		} 
			// nisu null
	            
		if ( str1.equals(str2) ) {
			return false;
		} else {
			return true;
		}
	             
	 }	
  
//	 usporedjivanje dva decimala 
//   ako su razlicita vraca true	 
	 public boolean cmp_bdc(BigDecimal num1, BigDecimal num2) {
	            
	 	if ( (num1 == null) && (num2 == null) ) {
	 		return false;
		} else if ( (num1 != null) && (num2 == null) ) {
			return true;
		} else if ( (num1 == null) && (num2 != null) ) {
			return true;
		} 
			// nisu null
	            
		if ( num1.compareTo(num2) == 0 ) {
			return false;
		} else {
			return true;
		}
	             
	 }	

//	 usporedjivanje dva integera
//  ako su razlicita vraca true	 
	 public boolean cmp_int( Integer num1, Integer num2) {
	            
	 	if ( (num1 == null) && (num2 == null) ) {
	 		return false;
		} else if ( (num1 != null) && (num2 == null) ) {
			return true;
		} else if ( (num1 == null) && (num2 != null) ) {
			return true;
		} 
			// nisu null
	            
		if ( num1.compareTo(num2) == 0 ) {
			return false;
		} else {
			return true;
		}
	             
	 }		 

//	 usporedjivanje dva datuma
//  ako su razlicita vraca true	 
	 public boolean cmp_dte(java.sql.Date dat1, java.sql.Date dat2) {
	            
	 	if ( (dat1 == null) && (dat2 == null) ) {
	 		return false;
		} else if ( (dat1 != null) && (dat2 == null) ) {
			return true;
		} else if ( (dat1 == null) && (dat2 != null) ) {
			return true;
		} 
			// nisu null
	            
		if ( dat1.compareTo(dat2) == 0) {
			return false;
		} else {
			return true; 
		} 
	             
	 }	 
	 
	 public void setLoanStockCtx (ResourceAccessor ra) {
		String tip = ((String) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtSecTypeCode")).trim();
		if ((tip != null) && (!(tip.equals("")))) {
			if (tip.equals("1")) {  // obicna zaduznica-iznos i valuta obavezni
				ra.setContext("Coll_txtStockAmount", "fld_plain");
				ra.setContext("Coll_txtStockCurr", "fld_plain");
				ra.setRequired("Coll_txtStockAmount", false);	
				ra.setRequired("Coll_txtStockCurr", false);	
//				ra.setRequired("Coll_txtStockAmount", true);	
//				ra.setRequired("Coll_txtStockCurr", true);	
				
			} else if (tip.equals("2")) { // bianco zaduznica - iznos i valuta se ne upisuju 
				ra.setContext("Coll_txtStockAmount", "fld_protected");
				ra.setContext("Coll_txtStockCurr", "fld_protected");
				ra.setRequired("Coll_txtStockAmount", false);	
				ra.setRequired("Coll_txtStockCurr", false);		
// obrisati iznos i valutu
				ra.setAttribute("CollSecPaperDialogLDB","Coll_txtStockAmount",null);
				ra.setAttribute("CollSecPaperDialogLDB","Coll_txtStockCurr",null);
			} else {
				ra.setContext("Coll_txtStockAmount", "fld_plain");
				ra.setContext("Coll_txtStockCurr", "fld_plain");
				ra.setRequired("Coll_txtStockAmount", false);	
				ra.setRequired("Coll_txtStockCurr", false);							
			}
		}	 	
	 	return;
	 }

	 /**
	  * Kontrola tipa depozita i upisane valute. 
	  * @return vraæa false ako tip i valuta nisu usklaðeni
	  */
	 public boolean ctrlCashDepVal (String tip, BigDecimal valuta) 
	 {
		if (tip == null || valuta == null) return true; // nije upisan tip ili valuta 
		
		tip = tip.trim();
		if (tip.equals("") || tip.length() < 3) return true;
		
		String podtip = tip.substring(0, 4); 
		BigDecimal kn = new BigDecimal("63999");
		
		if (podtip.equals("1KNC"))  // depoziti s valutnom klauzulom - ne smije biti kn 
		{   
			return !valuta.equals(kn);			
		}
		else if (podtip.equals("1KNK") || podtip.equals("1KNR") || podtip.equals("6CES"))  // kunski depoziti - smije biti samo kn
		{
			return valuta.equals(kn);
		} 
		else if (podtip.equals("1FCD") || podtip.equals("1FCF") || podtip.equals("1FCR"))  // depoziti u stranoj valuti - ne smije biti kn
		{ 
			return !valuta.equals(kn);
		}
		else if (tip.equals("12NETKNN"))  // depoziti za netiranje u kunama - smije biti samo kn
		{
		    return valuta.equals(kn);
		}
		else if (tip.equals("12NETVAL"))  // depoziti za netiranje u valuti - ne smije biti kn 
		{
		    return !valuta.equals(kn);
		}
	 	
		return true;
	 }
	     
	 
	 public boolean ctrlCashDepBank (String tip, String bank, String swift) {
//	  kontrola tipa depozita i upisane banke
//	  vraca false ako tip i banka nisu uskladjeni	 	
	 	 	
 		if (tip == null)  
 			return true; // nije upisan tip
 		
	 	tip = tip.trim();
	 	 
	 	if (tip.equals("")) 
	 		return true;
	 		
//	 	System.out.println("TIP: "+tip);
	 	
	 	if (tip.equalsIgnoreCase("1KNKNRBA") || tip.equalsIgnoreCase("1KNCCRBA") || tip.equalsIgnoreCase("1FCRBA")) {
// depozit kod RBA, banka nije upisana, popuniti banku s podacima 
	 		if (bank == null || bank.trim().equals("")) {
	 			ra.setAttribute("CollSecPaperDialogLDB","Coll_txtCdeRegNo","910000");
	 			ra.setCursorPosition("Coll_txtCdeRegNo");
	 			ra.invokeValidation("Coll_txtCdeRegNo");
	 			ra.setCursorPosition("Coll_txtCdeAmount");	 		
	 			return true;
	 		} else {
	 			if (!(bank.equals("910000"))) 
	 				return false;
	 		}
	 	} else if (tip.equalsIgnoreCase("1FCDOMBA")) {
// depozit kod domace banke, kontrola preko zemlje iz swift koda (5. i 6. pozicija = HR)
// ne smije biti RBA
	 		if (bank != null && (!(bank.trim().equals("")))) {
	 			if (bank.equals("910000")) 
	 				return false;	 			
	 		} 
	 		
	 		if (swift == null || swift.trim().equals(""))
	 			return true;
	 		
	 		if (!((swift.trim().substring(4,6)).equalsIgnoreCase("HR"))) {
	 			return false;
	 		}
	 		
	 		
	 		
	 		
	 	} else if (tip.equalsIgnoreCase("1FCFORBA")) {
//	 	 depozit kod ino banke, kontrola preko zemlje iz swift koda (5. i 6. pozicija != HR)
	 		if (swift == null || swift.trim().equals(""))
	 			return true;
	 		
	 		if ((swift.trim().substring(4,6)).equalsIgnoreCase("HR")) {
	 			return false;
	 		}	 		 
	 	}
	 	
	 	return true;
	 } 
	 
	
	/**
	 * Kontrola tipa depozita i upisane banke.
	 * @return vraæa false ako tip i banka nisu usklaðeni
	 */
	public boolean ctrlCashDepBank (String tip, String bank, String eco_sec, BigDecimal cou_id)
	{
	    if (tip == null || tip.trim().equals("")) return true;  // nije upisan tip
	    tip = tip.trim();
				
		ra.setRequired("Coll_txtDepAcc", false);
		
		if (tip.equalsIgnoreCase("1KNKNRBA") || tip.equalsIgnoreCase("1KNCCRBA") || tip.equalsIgnoreCase("1FCRBA") || tip.equalsIgnoreCase("12NETKNN") || tip.equalsIgnoreCase("12NETVAL") )  // depozit kod RBA i depozit za netiranje
		{
		    ra.setRequired("Coll_txtDepAcc", true);  // partija depozita je obavezan podatak
		
			if (bank == null || bank.trim().equals(""))  // ako banka nije upisana, popuniti s RBA
			{
				ra.setAttribute("CollSecPaperDialogLDB","Coll_txtCdeRegNo", "910000");
				ra.setCursorPosition("Coll_txtCdeRegNo");
				ra.invokeValidation("Coll_txtCdeRegNo");
				ra.setCursorPosition("Coll_txtCdeAmount");
				return true;
			}
			else  // ako je banka upisana, provjeriti je li RBA
			{
				return bank.equals("910000");
			}
		}
		else if (tip.equalsIgnoreCase("1KNRBAG") || tip.equalsIgnoreCase("1KNCRBAG") || tip.equalsIgnoreCase("1FCRBAG"))  // depozit unutar RBA grupe
		{
			if (bank == null || bank.trim().equals(""))  // ako banka nije upisana, popuniti s RBA stambenom štedionicom
			{
				ra.setAttribute("CollSecPaperDialogLDB","Coll_txtCdeRegNo","86833");
				ra.setCursorPosition("Coll_txtCdeRegNo");
				ra.invokeValidation("Coll_txtCdeRegNo");
				ra.setCursorPosition("Coll_txtCdeAmount");
				return true;
			}
		} 
		else if (tip.equalsIgnoreCase("1FCDOMBA"))  // depozit kod domaæe banke ili štedionice
		{
			if("910000".equals(bank) || "86833".equals(bank)) return false;  // ne smije biti RBA i RBA stambena štedionica
			
			if (eco_sec == null || eco_sec.trim().equals("")) return true;
			
			return eco_sec.equals("10") || eco_sec.equals("11") || eco_sec.equals("12") || eco_sec.equals("13");
		}
		else if (tip.equalsIgnoreCase("1FCFORBA"))  // depozit kod ino banke ili štedionice
		{
			if (eco_sec == null || eco_sec.trim().equals("")) return true;
			
			return eco_sec.equals("92");
		}
		else if (tip.equalsIgnoreCase("1FCRZB"))  // depozit unutar RZB grupe - pretpostavka kod ino banke
		{
			if (eco_sec == null || eco_sec.trim().equals("")) return true;
			
			return eco_sec.equals("92");
		}
			
		return true;
	}
	
	
	 public void setInsPolCtx (ResourceAccessor ra) {
		String tip = ((String) ra.getAttribute("CollHeadLDB", "Coll_txtCollTypeCode")).trim();
//		String vrsta = (String) ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecTypeCode");		
		if ((tip != null) && (!(tip.equals("")))) {
			if (tip.equals("7PLK")) {  // zo - lombardni kredit - upisuje se akumulirana vrijednost i da li je potpisana izjava
				ra.setRequired("Coll_txtAcumBuyValue", true);	
				ra.setRequired("Coll_txtRecLop", true);	
	            // po Defect 25510 akumulirana vrijednost treba biti prazna po default-u za 7PLK
				if(ra.getScreenContext().equals("scr_change")){
				    ra.setAttribute("CollHeadLDB", "Coll_txtAcumBuyValue", null);
				}
// za potrebe inicijalnog unosa punim polje potpisana izjava sa D i zaprotektiram - izbacila 20.09.2006
//				ra.setAttribute("CollHeadLDB","Coll_txtRecLop","D");
//				ra.setAttribute("CollHeadLDB","Coll_txtRecLop","");
				ra.setContext("Coll_txtRecLop", "fld_plain");
				
			} else if (tip.equals("7POK")) { // ZO - ostali plasmani - ne upisuje se akumulirana vrijednost i da li je potpisana izjava 
// Milka, 22.05.2007 - ako je polica sa stednom komponentom obavezan je upis akumulirane vrijednosti
// Milka, 28.03.2008 - promjena, akumulirana vrijednost nije obavezna ni ako je polica sa stednom komponentom				
				ra.setRequired("Coll_txtAcumBuyValue", false);	
				
/*				if (vrsta != null && !(vrsta.trim().equals(""))) {
					vrsta = vrsta.trim();
					if (vrsta.length() > 4) {
						vrsta = vrsta.substring(0,5);
					} else {
						vrsta = vrsta.substring(0,vrsta.length()+1);
				 	}
					if (vrsta.equalsIgnoreCase("MOZOS")) {
						ra.setRequired("Coll_txtAcumBuyValue", true);							
					}
				}  */
			 	
				ra.setRequired("Coll_txtRecLop", false);		
//				ra.setAttribute("CollHeadLDB","Coll_txtRecLop","");
				ra.setContext("Coll_txtRecLop", "fld_plain");
			} else {
				ra.setRequired("Coll_txtAcumBuyValue", false);	
				ra.setRequired("Coll_txtRecLop", false);	
//				ra.setAttribute("CollHeadLDB","Coll_txtRecLop","");
				ra.setContext("Coll_txtRecLop", "fld_plain");
			}
		}  	 	 
	 	return;
	 }
	 
	 public boolean chkInsPolType (ResourceAccessor ra) {
		String tip = (String) ra.getAttribute("CollHeadLDB", "Coll_txtCollTypeCode");
		String vrsta = (String) ra.getAttribute("CollSecPaperDialogLDB","Coll_txtSecTypeCode");
		
		if (vrsta == null || vrsta.trim().equals(""))
			return true;
		
		if (tip == null || tip.trim().equals(""))
			return true;

		tip = tip.trim();
		
		vrsta = vrsta.trim();
		
//		System.out.println("TIP: "+tip);
//		System.out.println("VRSTA: "+vrsta+ " " + vrsta.length());
		
		if (vrsta.length() > 4) {
			vrsta = vrsta.substring(0,5);
		} else {
			vrsta = vrsta.substring(0,vrsta.length()+1);
	 	} 
		 
//		System.out.println("VRSTA: "+vrsta);		  
		
		if (tip.equals("7PLK")) {
// dozvoljene vrste su : MO - polica zivotnog osiguranja sa stednom komponentom drugih osiguravatelja - MOZOS	
			if (!(vrsta.equalsIgnoreCase("MOZOS")))
					return false;
		} else if (tip.equals("7POK")) {
// dozvoljene vrste su : MO - polica zivotnog osiguranja sa stednom komponentom drugih osiguravatelja - MOZOS	
//						MODOS - Uniqa polica zivotnog osiguranja sa stednom komponentom - MODOS
//						ROPOS - Uniqa polica riziko osiguranja - ROPOS
//						SN - polica riziko osiguranja ostalih osiguravatelja - SNRIZ	
			if ((vrsta.equalsIgnoreCase("MOZOS")) || (vrsta.equalsIgnoreCase("MODOS")) || 
				(vrsta.equalsIgnoreCase("ROPOS")) || (vrsta.equalsIgnoreCase("SNRIZ")))
				return true;
			else 
				return false;
			
		} else {
			return true;
		}
			
		
		  
	 	return true;
	 }
	 /**
     *
     * Method recives date and number of days wich can be added or subtracted from the given
     * parameter date <br>
     * if last parametar of method is <b>true</b> than days will be added to given date<br>
     * if the last parematar is <b>false</b> than days will be subtracted from the given date<br>
     *
     * @param p_date
     * @param num_days
     * @param add parametar tells method to add/subtract days from date
     * @return resulting java.sql.Date
     */
    public static Date addOrDeductDaysFromDate(Date p_date,Integer num_days,boolean add){

        int num_d = add?num_days.intValue():-num_days.intValue();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(p_date);
        c1.add(Calendar.DATE,num_d);
        Date date = new Date(c1.getTimeInMillis());
        return date;
    }	
    /**
    *
    * Method sets final maturity date of cash deposit<br>
    * if prlongat equels N, then final maturity date will be set to maturity date<br>
    * @param prolongat
    * @param m_date
    * @return final_maturity_date java.sql.Date
    */    
    public boolean setCashDepFinalDate (ResourceAccessor ra) {
        String prolongat = (String) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtCdeProlong");
        Date m_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtCdeDepUnti");
        Date f_m_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtCashDepDateUntilFinal");
        System.out.println("2......TU SAM u metodi  "+ prolongat + m_date + f_m_date);          
        if (prolongat != null && prolongat.equals("N") && m_date != null) {
            ra.setAttribute("CollSecPaperDialogLDB", "Coll_txtCashDepDateUntilFinal", m_date);
            System.out.println("3......TU SAM u metodi  "+ m_date + ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtCashDepDateUntilFinal"));              
        } else if (prolongat != null && prolongat.equals("D") && m_date != null && f_m_date != null) {
// kontrola datuma dospijeca i krajnjeg datuma dospijeca
            System.out.println("4......TU SAM u metodi  "+ m_date + f_m_date);            
            if (f_m_date.before(m_date)) {
                return false;
            } 
        }
        return true;
     }    
      
}  
