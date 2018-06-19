/*
 * Created on 2006.02.07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;




import hr.vestigo.framework.common.VestigoTableException;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

import hr.vestigo.framework.controller.tm.VestigoTMException;


/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollComponentItem extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollComponentItem.java,v 1.23 2006/03/15 08:58:15 hrasia Exp $";
	
	
	//Old data of selected row
	
	hr.vestigo.framework.common.TableData tdCollForUpdateOld = null;      
	java.util.Vector selectedRowUniqueCollUpdateOld = null;               
	java.util.Vector selectedRowCollUpdateOld = null;                     
	                                                                      
	//visible data of selected row on tblCollateralDialogCollComp
	String collCompNoOld = null;
	String colNumOld = null;
	String collTypeNameOld = null; 
	String hfHfcSCDOld = null;
	java.math.BigDecimal hfAmountOld = null;
	String CompCurIdCodeCOld = null;
	String hfPriorityOld = null;
	String accPriorOld = null;
	java.math.BigDecimal compCovAmoOld = null; 
	java.math.BigDecimal compCovAmoRefOld = null; 
	java.sql.Date compCovDateOld = null; 
	String curIdRefCodeCOld = null;
	

	

	//invisible data of selected row on tblCollateralDialogCollComp
	
	java.math.BigDecimal COL_HEA_IDOld = null;
	java.math.BigDecimal COL_TYPE_IDOld = null;
	String collTypeCodeOld = null;
	java.math.BigDecimal hfHfcIdOld = null;
	java.math.BigDecimal COMP_CUR_IDOld = null;
	java.math.BigDecimal CUR_ID_REFOld = null;
	java.math.BigDecimal COMP_HF_PRIOR_IDOld = null;
	java.math.BigDecimal excRatRefOld = null;
	java.sql.Date excRatRefDateOld = null;
	java.sql.Date compDateFromOld = null;
	java.sql.Date compDateUntilOld = null;
	
	
	public CollComponentItem(ResourceAccessor ra) {
		super(ra);
			
	}
	public void CollComponentItem_SE() {
		java.sql.Date todaySQLDate = null;
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		long timeT = calendar.getTime().getTime();
		todaySQLDate = new java.sql.Date(timeT);
		
		System.out.println ( "USAO NA EKRAN CollComponentItem");
		
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
			ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate", todaySQLDate);  
			ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovDate", todaySQLDate); 
			ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate1", todaySQLDate); 
			ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateFrom", todaySQLDate);  
			ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateUntil", (java.sql.Date) ra.getAttribute("CollateralDialogLDB","CollComponentItemD_txtCompDateUntil"));  
			  
						
		}
		
		if(ra.getScreenContext().compareTo("scr_update")== 0){

				if (!ra.isLDBExists("CollComponentItemLDB")) {
						ra.createLDB("CollComponentItemLDB");
				}
			
				//Save old data of selected row
				if(ra.getAttribute("tblCollateralDialogLoanComp") != null){
					tdCollForUpdateOld = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");
					selectedRowUniqueCollUpdateOld =(java.util.Vector) tdCollForUpdateOld.getSelectedRowUnique();                  
					selectedRowCollUpdateOld = tdCollForUpdateOld.getSelectedRowData();
					resetCollateralDialogLDBCollComponentItem();
					resetCollComponentItemLDB();                                                                                        

					
					System.out.println ( "CollComponentItem CollComponentItem_SE 	scr_update  ");
					ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUS_ID",(java.math.BigDecimal)  ra.getAttribute("CollateralDialogLDB", "CUS_ID"));                              
					ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColaCusIdUseRegisterNo",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaRegisterNo"));           
					ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColaCusIdUseOwnerName",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaOwnerName")); 
					prepForUpdateCollComponentItem();
				}
		
		}//scr_update
		
		
		todaySQLDate = null;
		calendar = null;
			
	}//CollComponentItem_SE
	
	public void exit() {
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
				ra.exitScreen();
				ra.exitScreen();
		}
		if(ra.getScreenContext().compareTo("scr_update")== 0){
				ra.exitScreen();
		
		}
		
	}//exit
	
	
	
	public boolean CollComponentItem_txtCompCovAmo_FV(){
		
		if((ra.getScreenContext().compareTo("scr_choose")== 0)  || (ra.getScreenContext().compareTo("scr_update")== 0)) {
			
			hr.vestigo.modules.collateral.util.CollateralUtil utilObj = new hr.vestigo.modules.collateral.util.CollateralUtil(ra);
		
		
						java.sql.Date todaySQLDate = null;
						java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
						long timeT = calendar.getTime().getTime();
						todaySQLDate = new java.sql.Date(timeT);
		
						java.math.BigDecimal oneNumber = new java.math.BigDecimal("1.0000000000000000000000000000000000000000000000000000000000000000");
						oneNumber.setScale(64);
		
						java.math.BigDecimal comp_cov_amo_ref = null;
						java.math.BigDecimal hf_amount = (java.math.BigDecimal) ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtHfAmount");
						java.math.BigDecimal comp_cov_amo = (java.math.BigDecimal) ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmo");	
		
						java.math.BigDecimal currencyId = (java.math.BigDecimal) ra.getAttribute("CollComponentItemLDB","CollComponentItem_COMP_CUR_ID");
						java.math.BigDecimal exchRate = null;
		
		
						System.out.println("Ukupni iznos kolaterala hf_amount     " + hf_amount );
						System.out.println("Iznos koji se koristi za pokrice comp_cov_amo     " + comp_cov_amo);
		
		
						if(comp_cov_amo.compareTo(hf_amount)== 1){
								System.out.println("Provjera hf_amount comp_cov_amo     " + hf_amount + "    " + comp_cov_amo);
								ra.showMessage("wrnclt14");
								return false;
						}
		
		
						/////////////////
		
						//Dohvat tekuceg konteksta
						String currentScreenContext = ra.getScreenContext().trim();
		
						hr.vestigo.framework.common.TableData tdColaCCI = null;
		
						//curAvailableCollComponent
						//curSumTotalCollComp
						//curSumAllCollComptbl
						//curSumTotalFromCollComponent
		
						java.math.BigDecimal currentCollCompHfPriorId = (java.math.BigDecimal) ra.getAttribute("CollComponentItemLDB","CollComponentItem_COMP_HF_PRIOR_ID"); //odabrano pravo sa liste prava RBA
						java.math.BigDecimal  amoCollCompCCI = new java.math.BigDecimal("0.00");
						java.math.BigDecimal  CollHfPriorIdCCI = null; 
		
						java.math.BigDecimal  curSumTotalCollComp = new java.math.BigDecimal("0.00");
						//curSumTotalCollComp = curSumAllCollComptbl + curSumTotalFromCollComponent
						java.math.BigDecimal  curSumAllCollComptbl = new java.math.BigDecimal("0.00");
						java.math.BigDecimal  curSumTotalFromCollComponent = new java.math.BigDecimal("0.00");
		
		
		
						java.math.BigDecimal  curAvailableCollComponent = new java.math.BigDecimal("0.00");
		
						java.math.BigDecimal  collComponentItemHfAmount = new java.math.BigDecimal("0.00");
		
						//curAvailableCollComponent = collComponentItemHfAmount - (curSumAllCollComptbl + curSumTotalFromCollComponent)
						//curAvailableCollComponent = collComponentItemHfAmount - (curSumTotalCollComp)

		
		
						//treba proci kroz tblCollateralDialogCollComp i pokupiti sve s istim brojem prava 
						tdColaCCI = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");
		
						if (tdColaCCI != null){
									java.util.Vector vidljiviDioTabliceColaCCI = tdColaCCI.getData();
									java.util.Vector nevidljiviDioTabliceColaCCI = (java.util.Vector) tdColaCCI.getUnique();
									for(int i = 0; i < vidljiviDioTabliceColaCCI.size(); i++){
											java.util.Vector vidljiviRedakColaCCI = (java.util.Vector) vidljiviDioTabliceColaCCI.elementAt(i);
											java.util.Vector nevidljiviRedakColaCCI = (java.util.Vector) nevidljiviDioTabliceColaCCI.elementAt(i);
						
											amoCollCompCCI = (java.math.BigDecimal) vidljiviRedakColaCCI.elementAt(8);
											CollHfPriorIdCCI = (java.math.BigDecimal) nevidljiviRedakColaCCI.elementAt(6);
						
											if(currentCollCompHfPriorId.compareTo(CollHfPriorIdCCI)== 0){
													curSumAllCollComptbl = curSumAllCollComptbl.add(amoCollCompCCI);
											}
									}//glavni for
						}
		
		
						//poziv transakcije za provjeru iz tablice coll_component
		

						try {
								ra.executeTransaction();
						} catch (VestigoTMException vtme) {
								if (vtme.getMessageID() != null)
									ra.showMessage(vtme.getMessageID());
								return true;
						} 
		
		
						curSumTotalFromCollComponent = (java.math.BigDecimal)ra.getAttribute("CollComponentItemLDB","curSumTotalFromCollComponent");	
			
						//Vracanje tekuceg konteksta
						ra.setScreenContext(currentScreenContext);
		
		
		
		if(ra.getScreenContext().compareTo("scr_update")== 0 ){
						java.math.BigDecimal oldCompCovAmo = null;
						oldCompCovAmo = (java.math.BigDecimal) ra.getAttribute("CollComponentItemLDB","CollComponentItemOV_txtCompCovAmo");
						System.out.println("CollComponentItem       compCovAmo_FV  scr_update 1  oldCompCovAmo je  " + oldCompCovAmo);
						System.out.println("CollComponentItem       compCovAmo_FV  scr_update 2  curSumAllCollComptbl je  " + curSumAllCollComptbl);
							
						//Kod update treba oduzeti staru vrijednost komponente kolaterala upisanu u tbl
						curSumAllCollComptbl = curSumAllCollComptbl.subtract(oldCompCovAmo);
						System.out.println("CollComponentItem       compCovAmo_FV  scr_update 3  curSumAllCollComptbl - oldCompCovAmo je  " + curSumAllCollComptbl);
		}
		
		
		
						curSumTotalCollComp = curSumTotalFromCollComponent.add(curSumAllCollComptbl);
						//curAvailableCollComponent
						//curSumTotalCollComp
						//curSumAllCollComptbl
						//curSumTotalFromCollComponent

		
						ra.setAttribute("CollComponentItemLDB","curSumAllCollComptbl",curSumAllCollComptbl);
						ra.setAttribute("CollComponentItemLDB","curSumTotalCollComp",curSumTotalCollComp);
		
						collComponentItemHfAmount = (java.math.BigDecimal)ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtHfAmount");
		
						curAvailableCollComponent = collComponentItemHfAmount.subtract(curSumTotalCollComp);
		
						ra.setAttribute("CollComponentItemLDB","curAvailableCollComponent",curAvailableCollComponent);
		
						//curAvailableCollComponent = collComponentItemHfAmount - (curSumAllCollComptbl + curSumTotalFromCollComponent)
						//curAvailableCollComponent = collComponentItemHfAmount - (curSumTotalCollComp)
		
		
		
		
						if(comp_cov_amo.compareTo(curAvailableCollComponent)== 1){
							System.out.println("Provjera curAvailableCollComponent < comp_cov_amo     " + curAvailableCollComponent + "    " + comp_cov_amo);
							ra.showMessage("wrnclt23");
							return false;
						}
		
		
		if(ra.getScreenContext().compareTo("scr_update")== 0 ){
		
		}else{
							setNumberOfComponent();
		}
		
		
		
		///////////////////////
		
		//IZRACUN IZNOSA U REF VALUTI
		//comp_cov_amo_ref = comp_cov_amo * tecaj
		
		try{
			if (utilObj.getCurrentExchRate(todaySQLDate,currencyId)){
				exchRate = utilObj.getMiddRate();
				
				System.out.println(" Valuta i tecaj    " + currencyId + "   " + exchRate );
				//CollComponentItem_txtCompCovAmo
				//CollComponentItem_txtCompCovAmoRef
				comp_cov_amo_ref = comp_cov_amo.multiply(exchRate);
				comp_cov_amo_ref = comp_cov_amo_ref.divide(oneNumber,2,java.math.BigDecimal.ROUND_HALF_UP);
				
				 
				
				
				 
				ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmoRef", comp_cov_amo_ref); 
				ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovAmoRef", comp_cov_amo_ref); 
				
				ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovAmo", comp_cov_amo);
				
				ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUR_ID_REF", (java.math.BigDecimal) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_CUR_ID_REF"));
				ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCurIdRefCodeC", (String) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtCurIdRefCodeC"));
				
				
				ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRef",exchRate ); 
				ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRefDate",todaySQLDate ); 
				
				ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRef",exchRate ); 
				ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRefDate",todaySQLDate ); 
				
				
				
				comp_cov_amo_ref = null;
				
				
			}
		}catch(java.sql.SQLException sqle){
			
		}catch(hr.vestigo.framework.util.db.EmptyRowSet db_ers_e){
			
		}
		
		
		hf_amount  = null;
		comp_cov_amo = null;
		currencyId = null;
		todaySQLDate = null;
		exchRate = null;
		
		return true;
		
		}
		return true;
	}//CollComponentItem_txtCompCovAmo_FV
	
	public void setNumberOfComponent(){
		
		
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
		
		int vel = 0;
		
		hr.vestigo.framework.common.TableData tdCollLoan = null;
		if(ra.getAttribute("tblCollateralDialogLoanComp")!= null){
			System.out.println("setNumberOfComponent BBBBBBBBBB");
			tdCollLoan = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
			String velString = null;
			java.util.Vector vidljiviDioTabliceZaBroj = tdCollLoan.getData();
			
			
			for(int i = 0; i < vidljiviDioTabliceZaBroj.size(); i++){
				java.util.Vector vidljiviRedakZaBroj = (java.util.Vector) vidljiviDioTabliceZaBroj.elementAt(i);
				velString =  (String) vidljiviRedakZaBroj.elementAt(0);
				vel = Integer.parseInt(velString);
				vidljiviRedakZaBroj = null;
			}
			velString = null;
			vidljiviDioTabliceZaBroj = null;
		}
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtSerNoLoanComp" , vel+"");
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtSerNoLoanComp" , vel+"");
	
		}
		
		
		
	}//setNumberOfComponent
	
	public boolean CollComponentItem_txtSerNoLoanComp_FV(){
		
				
		return true;
	}//CollComponentItem_txtSerNoLoanComp_FV
	
	
	public void selection() {
		if (!ra.isLDBExists("CollComponentItemLDB")) {
	 		ra.createLDB("CollComponentItemLDB");
	 	}
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
				puniTablicu();
				ra.exitScreen();
				ra.exitScreen();
		}
		
		
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			promijeniTablicu();
			ra.exitScreen();
		}
		
		
	}//selection
	
	
	public void promijeniTablicu() {
		
		                   
		                                                                      
		//visible data of selected row on tblCollateralDialogCollComp
		 collCompNoOld = (String) selectedRowCollUpdateOld.elementAt(0);
		 collCompNoOld = collCompNoOld.trim();
		 colNumOld = (String) selectedRowCollUpdateOld.elementAt(1);
		 colNumOld = colNumOld.trim();
		 collTypeNameOld = (String) selectedRowCollUpdateOld.elementAt(2);
		 hfHfcSCDOld = (String) selectedRowCollUpdateOld.elementAt(3);
		 hfAmountOld = (java.math.BigDecimal) selectedRowCollUpdateOld.elementAt(4);
		 CompCurIdCodeCOld = (String) selectedRowCollUpdateOld.elementAt(5);
		 CompCurIdCodeCOld = CompCurIdCodeCOld.trim();
		 hfPriorityOld = (String) selectedRowCollUpdateOld.elementAt(6);
		 accPriorOld = (String) selectedRowCollUpdateOld.elementAt(7);
		 compCovAmoOld = (java.math.BigDecimal) selectedRowCollUpdateOld.elementAt(8); 
		 compCovAmoRefOld = (java.math.BigDecimal) selectedRowCollUpdateOld.elementAt(9);
		 compCovDateOld = (java.sql.Date) selectedRowCollUpdateOld.elementAt(10);
		 curIdRefCodeCOld = (String) selectedRowCollUpdateOld.elementAt(11);
		 curIdRefCodeCOld = curIdRefCodeCOld.trim();

		

		//invisible data of selected row on tblCollateralDialogCollComp
		COL_HEA_IDOld = (java.math.BigDecimal) selectedRowUniqueCollUpdateOld.elementAt(0);
		COL_TYPE_IDOld = (java.math.BigDecimal) selectedRowUniqueCollUpdateOld.elementAt(1);
		collTypeCodeOld = (String) selectedRowUniqueCollUpdateOld.elementAt(2);
		hfHfcIdOld =(java.math.BigDecimal) selectedRowUniqueCollUpdateOld.elementAt(3);
		COMP_CUR_IDOld = (java.math.BigDecimal) selectedRowUniqueCollUpdateOld.elementAt(4);
		CUR_ID_REFOld = (java.math.BigDecimal) selectedRowUniqueCollUpdateOld.elementAt(5);
		COMP_HF_PRIOR_IDOld = (java.math.BigDecimal) selectedRowUniqueCollUpdateOld.elementAt(6);
		excRatRefOld = (java.math.BigDecimal) selectedRowUniqueCollUpdateOld.elementAt(7);
		excRatRefDateOld = (java.sql.Date) selectedRowUniqueCollUpdateOld.elementAt(8);
		compDateFromOld = (java.sql.Date) selectedRowUniqueCollUpdateOld.elementAt(9);
		compDateUntilOld = (java.sql.Date) selectedRowUniqueCollUpdateOld.elementAt(10);

			
		
		
		//visible data of selected row on tblCollateralDialogCollComp
		String collCompNoNew = null;
		String colNumNew = null;
		String collTypeNameNew = null; 
		String hfHfcSCDNew = null;
		java.math.BigDecimal hfAmountNew = null;
		String CompCurIdCodeCNew = null;
		String hfPriorityNew = null;
		String accPriorNew = null;
		java.math.BigDecimal compCovAmoNew = null; 
		java.math.BigDecimal compCovAmoRefNew = null; 
		java.sql.Date compCovDateNew = null; 
		String curIdRefCodeCNew = null;
		

		

		//invisible data of selected row on tblCollateralDialogCollComp
		
		java.math.BigDecimal COL_HEA_IDNew = null;
		java.math.BigDecimal COL_TYPE_IDNew = null;
		String collTypeCodeNew = null;
		java.math.BigDecimal hfHfcIdNew = null;
		java.math.BigDecimal COMP_CUR_IDNew = null;
		java.math.BigDecimal CUR_ID_REFNew = null;
		java.math.BigDecimal COMP_HF_PRIOR_IDNew = null;
		java.math.BigDecimal excRatRefNew = null;
		java.sql.Date excRatRefDateNew = null;
		java.sql.Date compDateFromNew = null;
		java.sql.Date compDateUntilNew = null;

		
		
		
		
		collCompNoNew = collCompNoOld;
		colNumNew = colNumOld;
		collTypeNameNew = collTypeNameOld;
		hfHfcSCDNew = hfHfcSCDOld;
		hfAmountNew = hfAmountOld;
		CompCurIdCodeCNew = CompCurIdCodeCOld;
		hfPriorityNew = hfPriorityOld;
		accPriorNew = accPriorOld;
		compCovAmoNew =(java.math.BigDecimal) ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmo"); 
		compCovAmoRefNew = (java.math.BigDecimal) ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmoRef"); 
		compCovDateNew = (java.sql.Date)ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate"); 
		curIdRefCodeCNew = (String) ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCurIdRefCodeC");
		
		
	
		COL_HEA_IDNew = COL_HEA_IDOld;
		COL_TYPE_IDNew = COL_TYPE_IDOld;
		collTypeCodeNew = collTypeCodeOld;
		hfHfcIdNew = hfHfcIdOld;
		COMP_CUR_IDNew = COMP_CUR_IDOld;
		CUR_ID_REFNew = CUR_ID_REFOld;
		COMP_HF_PRIOR_IDNew = COMP_HF_PRIOR_IDOld;
		excRatRefNew = (java.math.BigDecimal)ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRef");
		excRatRefDateNew = (java.sql.Date)ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRefDate");
		compDateFromNew = compDateFromOld;
		compDateUntilNew = compDateUntilOld;
		
			
			
		hr.vestigo.framework.common.TableData tdReplace = new hr.vestigo.framework.common.TableData();
		
		java.util.Vector rowOneV = new java.util.Vector();
		java.util.Vector rowOneNV = new java.util.Vector();
		
		rowOneV.add(collCompNoNew);
		rowOneV.add(colNumNew);
		rowOneV.add(collTypeNameNew);
		rowOneV.add(hfHfcSCDNew);
		rowOneV.add(hfAmountNew);
		rowOneV.add(CompCurIdCodeCNew);
		rowOneV.add(hfPriorityNew);
		rowOneV.add(accPriorNew);
		rowOneV.add(compCovAmoNew);
		rowOneV.add(compCovAmoRefNew);
		rowOneV.add(compCovDateNew);
		rowOneV.add(curIdRefCodeCNew);
		
		
		
		
		rowOneNV.add(COL_HEA_IDNew);
		rowOneNV.add(COL_TYPE_IDNew);
		rowOneNV.add(collTypeCodeNew);
		rowOneNV.add(hfHfcIdNew);
		rowOneNV.add(COMP_CUR_IDNew);
		rowOneNV.add(CUR_ID_REFNew);
		rowOneNV.add(COMP_HF_PRIOR_IDNew);
		rowOneNV.add(excRatRefNew);
		rowOneNV.add(excRatRefDateNew);
		rowOneNV.add(compDateFromNew);
		rowOneNV.add(compDateUntilNew);
		
		
		
		
		 //visible data of selected row on tblCollateralDialogCollComp
		 collCompNoNew = null;
		 colNumNew = null;
		 collTypeNameNew = null; 
		 hfHfcSCDNew = null;
		 hfAmountNew = null;
		 CompCurIdCodeCNew = null;
		 hfPriorityNew = null;
		 accPriorNew = null;
		 compCovAmoNew = null; 
		 compCovAmoRefNew = null; 
		 compCovDateNew = null; 
		 curIdRefCodeCNew = null;
		
		 //invisible data of selected row on tblCollateralDialogCollComp
		 COL_HEA_IDNew = null;
		 COL_TYPE_IDNew = null;
		 collTypeCodeNew = null;
		 hfHfcIdNew = null;
		 COMP_CUR_IDNew = null;
		 CUR_ID_REFNew = null;
		 COMP_HF_PRIOR_IDNew = null;
		 excRatRefNew = null;
		 excRatRefDateNew = null;
		 compDateFromNew = null;
		 compDateUntilNew = null;

		
		
		//treba proci kroz tblCollateralDialogLoanComp
		
		
		
		
		if (tdCollForUpdateOld != null){
							java.util.Vector vidljiviDioTabliceCollForUpdateOld = tdCollForUpdateOld.getData();
							java.util.Vector nevidljiviDioTabliceCollForUpdateOld = (java.util.Vector) tdCollForUpdateOld.getUnique();
							for(int i = 0; i < vidljiviDioTabliceCollForUpdateOld.size(); i++){
								java.util.Vector vidljiviRedakCollUP = (java.util.Vector) vidljiviDioTabliceCollForUpdateOld.elementAt(i);
								java.util.Vector nevidljiviRedakCollUP = (java.util.Vector) nevidljiviDioTabliceCollForUpdateOld.elementAt(i);
								
								
								 collCompNoNew = (String) vidljiviRedakCollUP.elementAt(0);
								 collCompNoNew = collCompNoNew.trim();
								 colNumNew =(String) vidljiviRedakCollUP.elementAt(1);
								 collTypeNameNew = (String) vidljiviRedakCollUP.elementAt(2);
								 hfHfcSCDNew = (String) vidljiviRedakCollUP.elementAt(3);
								 hfAmountNew = (java.math.BigDecimal) vidljiviRedakCollUP.elementAt(4);
								 CompCurIdCodeCNew =(String) vidljiviRedakCollUP.elementAt(5);
								 hfPriorityNew =(String) vidljiviRedakCollUP.elementAt(6);
								 accPriorNew =(String) vidljiviRedakCollUP.elementAt(7);
								 compCovAmoNew = (java.math.BigDecimal) vidljiviRedakCollUP.elementAt(8);
								 compCovAmoRefNew = (java.math.BigDecimal) vidljiviRedakCollUP.elementAt(9);
								 compCovDateNew = (java.sql.Date) vidljiviRedakCollUP.elementAt(10);
								 curIdRefCodeCNew = (String) vidljiviRedakCollUP.elementAt(11);
								 
								System.out.println("CollComponentItem         promijeniTablicu  20      collCompNoNew colNumNew                                     " +  collCompNoNew  + "  "  + colNumNew );
								System.out.println("CollComponentItem         promijeniTablicu  21      collTypeNameNew hfHfcSCDNew                                 " +  collTypeNameNew + " " + hfHfcSCDNew  );
								System.out.println("CollComponentItem         promijeniTablicu  22      hfHfcSCDNew hfAmountNew   CompCurIdCodeCNew                 " +  hfHfcSCDNew + " " + hfAmountNew + " " + CompCurIdCodeCNew );
								System.out.println("CollComponentItem         promijeniTablicu  23      hfPriorityNew  accPriorNew                                  " +  hfPriorityNew  + " " + accPriorNew );
								System.out.println("CollComponentItem         promijeniTablicu  24      compCovAmoNew compCovAmoRefNew compCovDateNew               " +  compCovAmoNew + " " + compCovAmoRefNew + " " + compCovDateNew );
								System.out.println("CollComponentItem         promijeniTablicu  25      curIdRefCodeCNew                                            " +  curIdRefCodeCNew );
								
								
								
								 COL_HEA_IDNew = (java.math.BigDecimal) nevidljiviRedakCollUP.elementAt(0);
								 COL_TYPE_IDNew = (java.math.BigDecimal) nevidljiviRedakCollUP.elementAt(1);                     
								 collTypeCodeNew = (String) nevidljiviRedakCollUP.elementAt(2);  
								 hfHfcIdNew = (java.math.BigDecimal) nevidljiviRedakCollUP.elementAt(3);
								 COMP_CUR_IDNew = (java.math.BigDecimal) nevidljiviRedakCollUP.elementAt(4);
								 CUR_ID_REFNew = (java.math.BigDecimal) nevidljiviRedakCollUP.elementAt(5);
								 COMP_HF_PRIOR_IDNew = (java.math.BigDecimal) nevidljiviRedakCollUP.elementAt(6);
								 excRatRefNew = (java.math.BigDecimal) nevidljiviRedakCollUP.elementAt(7);
								 excRatRefDateNew = (java.sql.Date) nevidljiviRedakCollUP.elementAt(8);
								 compDateFromNew = (java.sql.Date) nevidljiviRedakCollUP.elementAt(9);                 
								 compDateUntilNew = (java.sql.Date) nevidljiviRedakCollUP.elementAt(10);

								
								System.out.println("CollComponentItem         promijeniTablicu  26      COL_HEA_IDNew   COL_TYPE_IDNew  collTypeCodeNew            " +  COL_HEA_IDNew  + " " +  COL_TYPE_IDNew + " " + collTypeCodeNew );
								System.out.println("CollComponentItem         promijeniTablicu  27      hfHfcIdNew                                                 " +  hfHfcIdNew );
								System.out.println("CollComponentItem         promijeniTablicu  28      COMP_CUR_IDNew  CUR_ID_REFNew  COMP_HF_PRIOR_IDNew         " +  COMP_CUR_IDNew  + " " + CUR_ID_REFNew + " " + COMP_HF_PRIOR_IDNew );
								System.out.println("CollComponentItem         promijeniTablicu  29      excRatRefNew  excRatRefDateNew                             " +  excRatRefNew + " " + excRatRefDateNew );
								System.out.println("CollComponentItem         promijeniTablicu  29      compDateFromNew  compDateUntilNew                          " +  compDateFromNew  + " " + compDateUntilNew );
								
								
								
								

								if(collCompNoNew.compareTo(collCompNoOld) == 0){
									tdReplace.addRow(rowOneV, rowOneNV);
									System.out.println("CollComponentItem         promijeniTablicu  40      collCompNoNew collCompNoOld                           " +  collCompNoNew + " " + collCompNoOld );
									
								}else{
									System.out.println("CollComponentItem         promijeniTablicu  41      collCompNoNew colNumNew                                     " +  collCompNoNew  + "  "  + colNumNew );
									System.out.println("CollComponentItem         promijeniTablicu  42      collTypeNameNew hfHfcSCDNew                                 " +  collTypeNameNew + " " + hfHfcSCDNew  );
									System.out.println("CollComponentItem         promijeniTablicu  43      hfHfcSCDNew hfAmountNew   CompCurIdCodeCNew                 " +  hfHfcSCDNew + " " + hfAmountNew + " " + CompCurIdCodeCNew );
									System.out.println("CollComponentItem         promijeniTablicu  44      hfPriorityNew  accPriorNew                                  " +  hfPriorityNew  + " " + accPriorNew );
									System.out.println("CollComponentItem         promijeniTablicu  45      compCovAmoNew compCovAmoRefNew compCovDateNew               " +  compCovAmoNew + " " + compCovAmoRefNew + " " + compCovDateNew );
									System.out.println("CollComponentItem         promijeniTablicu  46      curIdRefCodeCNew                                            " +  curIdRefCodeCNew );
									
									
									System.out.println("CollComponentItem         promijeniTablicu  50      COL_HEA_IDNew   COL_TYPE_IDNew  collTypeCodeNew            " +  COL_HEA_IDNew  + " " +  COL_TYPE_IDNew + " " + collTypeCodeNew );
									System.out.println("CollComponentItem         promijeniTablicu  51      hfHfcIdNew                                                 " +  hfHfcIdNew );
									System.out.println("CollComponentItem         promijeniTablicu  52      COMP_CUR_IDNew  CUR_ID_REFNew  COMP_HF_PRIOR_IDNew         " +  COMP_CUR_IDNew  + " " + CUR_ID_REFNew + " " + COMP_HF_PRIOR_IDNew );
									System.out.println("CollComponentItem         promijeniTablicu  53      excRatRefNew  excRatRefDateNew                             " +  excRatRefNew + " " + excRatRefDateNew );
									System.out.println("CollComponentItem         promijeniTablicu  54      compDateFromNew  compDateUntilNew                          " +  compDateFromNew  + " " + compDateUntilNew );
									
									
									
									java.util.Vector rowNewOneV = new java.util.Vector();
									java.util.Vector rowNewOneNV = new java.util.Vector();
									
									rowNewOneV.add(collCompNoNew);
									rowNewOneV.add(colNumNew);
									rowNewOneV.add(collTypeNameNew);
									rowNewOneV.add(hfHfcSCDNew);
									rowNewOneV.add(hfAmountNew);
									rowNewOneV.add(CompCurIdCodeCNew);
									rowNewOneV.add(hfPriorityNew);
									rowNewOneV.add(accPriorNew);
									rowNewOneV.add(compCovAmoNew);
									rowNewOneV.add(compCovAmoRefNew);
									rowNewOneV.add(compCovDateNew);
									rowNewOneV.add(curIdRefCodeCNew);
									
									
									rowNewOneNV.add(COL_HEA_IDNew);
									rowNewOneNV.add(COL_TYPE_IDNew);
									rowNewOneNV.add(collTypeCodeNew);
									rowNewOneNV.add(hfHfcIdNew);
									rowNewOneNV.add(COMP_CUR_IDNew);
									rowNewOneNV.add(CUR_ID_REFNew);
									rowNewOneNV.add(COMP_HF_PRIOR_IDNew);
									rowNewOneNV.add(excRatRefNew);
									rowNewOneNV.add(excRatRefDateNew);
									rowNewOneNV.add(compDateFromNew);
									rowNewOneNV.add(compDateUntilNew);
									
									tdReplace.addRow(rowNewOneV,rowNewOneNV);
									
									
									
								}
								
								
								 //visible data of selected row on tblCollateralDialogCollComp
								 collCompNoNew = null;
								 colNumNew = null;
								 collTypeNameNew = null; 
								 hfHfcSCDNew = null;
								 hfAmountNew = null;
								 CompCurIdCodeCNew = null;
								 hfPriorityNew = null;
								 accPriorNew = null;
								 compCovAmoNew = null; 
								 compCovAmoRefNew = null; 
								 compCovDateNew = null; 
								 curIdRefCodeCNew = null;
								
								 //invisible data of selected row on tblCollateralDialogCollComp
								 COL_HEA_IDNew = null;
								 COL_TYPE_IDNew = null;
								 collTypeCodeNew = null;
								 hfHfcIdNew = null;
								 COMP_CUR_IDNew = null;
								 CUR_ID_REFNew = null;
								 COMP_HF_PRIOR_IDNew = null;
								 excRatRefNew = null;
								 excRatRefDateNew = null;
								 compDateFromNew = null;
								 compDateUntilNew = null;
								
							}//glavni for
		}
	
		try{
			tdCollForUpdateOld.clear();
			ra.setAttribute("CollateralDialogLDB","tblCollateralDialogCollComp",tdCollForUpdateOld); 
			ra.setAttribute("CollateralDialogLDB","tblCollateralDialogCollComp",tdReplace); 
			
		}catch(	VestigoTableException e){
			
		}
		
		
		
		
		
		
		//reset values
		
		resetOldRowValues();
		rowOneV = null;
		rowOneNV = null;
		tdReplace = null;
		tdCollForUpdateOld = null;
		

	}//promijeniTablicu
	
	public void resetOldRowValues(){
		tdCollForUpdateOld = null;                                              
		selectedRowUniqueCollUpdateOld = null;                                                     
		selectedRowCollUpdateOld = null;
		    
		                                                                                                         
//		visible data of selected row on tblCollateralDialogCollComp
			collCompNoOld = null;
			colNumOld = null;
			collTypeNameOld = null; 
			hfHfcSCDOld = null;
			hfAmountOld = null;
			CompCurIdCodeCOld = null;
			hfPriorityOld = null;
			accPriorOld = null;
			compCovAmoOld = null; 
			compCovAmoRefOld = null; 
			compCovDateOld = null; 
			curIdRefCodeCOld = null;
			

			

			//invisible data of selected row on tblCollateralDialogCollComp
			
			COL_HEA_IDOld = null;
			COL_TYPE_IDOld = null;
			collTypeCodeOld = null;
			hfHfcIdOld = null;
			COMP_CUR_IDOld = null;
			CUR_ID_REFOld = null;
			COMP_HF_PRIOR_IDOld = null;
			excRatRefOld = null;
			excRatRefDateOld = null;
			compDateFromOld = null;
			compDateUntilOld = null;
                                                            
	    
	
	}//resetOldRowValues
	
	public void puniTablicu() {
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
			int broj = 0;
			String brojS = null;
		
			hr.vestigo.framework.common.TableData td1 = null;
			if(ra.getAttribute("tblCollateralDialogCollComp")==null){
				System.out.println("CollComponentItem puniTablicu 1");
				td1=null;
				td1 = new hr.vestigo.framework.common.TableData();
			}else{
				System.out.println("CollComponentItem puniTablicu 2");
				td1 = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");
			}
		
		
			if (td1 != null){
        		System.out.println("CollComponentItem puniTablicu 3");
        		java.util.Vector rowv = new java.util.Vector();
        		java.util.Vector rown = new java.util.Vector();
			
        		brojS = (String) ra.getAttribute("CollateralDialogLDB","CollComponentItemD_txtSerNoLoanComp");
        		System.out.println("CollComponentItemD_txtSerNoLoanComp   selection" + brojS);
        		broj = Integer.parseInt(brojS);
			
			
			
        		//POSTAVI VIDLJIVE OD tblCollateralDialogCollComp
        		rowv.add(broj+""); 
        		rowv.add((String) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtColNum")); 
        		rowv.add((String) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtCollTypeName")); 
				rowv.add((String) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfHfcSCD")); 
				rowv.add((java.math.BigDecimal) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfAmount"));
				rowv.add((String) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtCompCurIdCodeC")); 
				rowv.add((String) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfPriority")); 
				rowv.add((String) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtAccPrior")); 
				rowv.add((java.math.BigDecimal) ra.getAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovAmo")); 
				rowv.add((java.math.BigDecimal) ra.getAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovAmoRef")); 
				rowv.add((java.sql.Date) ra.getAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovDate")); 
				rowv.add((String) ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtCurIdRefCodeC")); 
			
				//POSTAVI NEVIDLJIVE OD tblCollateralDialogCollComp
				rown.add((java.math.BigDecimal)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_COL_HEA_ID"));
				rown.add((java.math.BigDecimal)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_COL_TYPE_ID"));
				rown.add((String)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtCollTypeCode"));
				rown.add((java.math.BigDecimal)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfHfcId"));
				rown.add((java.math.BigDecimal)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_COMP_CUR_ID"));
				rown.add((java.math.BigDecimal)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_CUR_ID_REF"));
				rown.add((java.math.BigDecimal)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_COMP_HF_PRIOR_ID"));
				rown.add((java.math.BigDecimal)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRef"));
				rown.add((java.sql.Date)ra.getAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRefDate"));
			
				//comp_cov_bamo kod inserta nema
				//comp_cov_bdate kod inserta nema
				//comp_cov_bamo_ref kod inserta nema
				rown.add((java.sql.Date)ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateFrom"));
				rown.add((java.sql.Date)ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateUntil"));
			
			
			
				td1.addRow(rowv,rown);
				ra.setAttribute("CollateralDialogLDB","tblCollateralDialogCollComp",td1);
        	
        	
				java.util.Vector vidljiviDioTabliceCR = td1.getData();
				java.util.Vector nevidljiviDioTabliceCR = (java.util.Vector) td1.getUnique();
			
				System.out.println("CollComponentItem     puniTablicu 4 KONTROLNI ISPIS  SVIH KOMPONENTI KOLATERALA - POCETAK ");
				for(int i = 0; i < vidljiviDioTabliceCR.size(); i++){
				
					java.util.Vector vidljiviRedak = (java.util.Vector) vidljiviDioTabliceCR.elementAt(i);
					java.util.Vector nevidljiviRedak = (java.util.Vector) nevidljiviDioTabliceCR.elementAt(i);
				
					System.out.println("                      ");
					System.out.println(i+". vidljivi redak    ");
					for(int j= 0; j < vidljiviRedak.size();j++){
						System.out.println(j + ". element je " + vidljiviRedak.elementAt(j));
					}
					System.out.println("                      ");
					System.out.println(i+". nevidljivi redak    ");
					for(int k= 0; k < nevidljiviRedak.size();k++){
						System.out.println(k + ". element je " + nevidljiviRedak.elementAt(k));
					}
				
				}
			
				System.out.println("CollComponentItem     puniTablicu 5 KONTROLNI ISPIS  SVIH KOMPONENTI KOLATERALA - KRAJ ");
			
				vidljiviDioTabliceCR= null;
				nevidljiviDioTabliceCR = null;
			
			
				resetCollateralDialogLDBCollComponentItem();
				resetCollComponentItemLDB();
        	
			}else{
				//ra.showMessage("wrn299");
        		return;
        	}
		
		}//scr_choose
	
	}//puniTablicu
	
	
	public boolean CollComponentItem_txtCompDateUntil_FV(){
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
		java.sql.Date todaySQLDate = null;
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		long timeT = calendar.getTime().getTime();
		todaySQLDate = new java.sql.Date(timeT);
		
		
		java.sql.Date beginDate = null;
		java.sql.Date endDate = null;
		java.sql.Date endDDate = null;
		endDate = (java.sql.Date)ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateUntil");
		endDDate = (java.sql.Date)ra.getAttribute("CollateralDialogLDB","CollComponentItemD_txtCompDateUntil");
		beginDate = (java.sql.Date) ra.getAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateFrom");
		
		if(endDate == null){
			ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateUntil", (java.sql.Date) ra.getAttribute("CollateralDialogLDB","CollComponentItemD_txtCompDateUntil"));  
		}
		
		
		if(endDate.before(beginDate)){
			ra.showMessage("wrnclt20");
			todaySQLDate = null;
			return false;
		}
		
	
		todaySQLDate = null;
		
		return true;
		}else{
			return true;
		}
	}//CollComponentItem_txtCompDateUntil_FV
	
	
	private void prepForUpdateCollComponentItem(){                                                                           
          
				//This method reads current row from tblCollateralDialogCollComp                                                     
				//and set elements on LDB                                                                                       
				hr.vestigo.framework.common.TableData tdCollForUpdate2 = null;                                                       
				java.util.Vector selectedRowUniqueCollUpdate2 = null;                                                                
				java.util.Vector selectedRowCollUpdate2 = null;                                                                      
          

				//visible data of selected row on tblCollateralDialogCollComp
				String collCompNo2 = null;
				String colNum2 = null;
				String collTypeName2 = null; 
				String hfHfcSCD2 = null;
				java.math.BigDecimal hfAmount2 = null;
				String CompCurIdCodeC2 = null;
				String hfPriority2 = null;
				String accPrior2 = null;
				java.math.BigDecimal compCovAmo2 = null; 
				java.math.BigDecimal compCovAmoRef2 = null; 
				java.sql.Date compCovDate2 = null; 
				String curIdRefCodeC2 = null; 
			
				//invisible data of selected row on tblCollateralDialogCollComp
				
				java.math.BigDecimal COL_HEA_ID2 = null;
				java.math.BigDecimal COL_TYPE_ID2 = null;
				String collTypeCode2 = null;
				java.math.BigDecimal hfHfcId2 = null;
				java.math.BigDecimal COMP_CUR_ID2 = null;
				java.math.BigDecimal CUR_ID_REF2 = null;
				java.math.BigDecimal COMP_HF_PRIOR_ID2 = null;
				java.math.BigDecimal excRatRef2 = null;
				java.sql.Date excRatRefDate2 = null;
				java.sql.Date compDateFrom2 = null;
				java.sql.Date compDateUntil2 = null;
				
				tdCollForUpdate2 = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");           
				selectedRowUniqueCollUpdate2 =(java.util.Vector) tdCollForUpdate2.getSelectedRowUnique();                            
				selectedRowCollUpdate2 = tdCollForUpdate2.getSelectedRowData();                                                      
				   
				System.out.println ( "selectedRowCollUpdate2     80  " +  selectedRowCollUpdate2 );
				
				//visible
				 collCompNo2 = (String) selectedRowCollUpdate2.elementAt(0);
				 colNum2 = (String) selectedRowCollUpdate2.elementAt(1);
				 collTypeName2 = (String) selectedRowCollUpdate2.elementAt(2); 
				 hfHfcSCD2 = (String) selectedRowCollUpdate2.elementAt(3);
				 hfAmount2 = (java.math.BigDecimal) selectedRowCollUpdate2.elementAt(4);
				 CompCurIdCodeC2 = (String) selectedRowCollUpdate2.elementAt(5);
				 hfPriority2 = (String) selectedRowCollUpdate2.elementAt(6);
				 accPrior2 = (String) selectedRowCollUpdate2.elementAt(7);
				 compCovAmo2 =(java.math.BigDecimal) selectedRowCollUpdate2.elementAt(8);
				 compCovAmoRef2 = (java.math.BigDecimal) selectedRowCollUpdate2.elementAt(9);
				 compCovDate2 = (java.sql.Date) selectedRowCollUpdate2.elementAt(10);
				 curIdRefCodeC2 = (String) selectedRowCollUpdate2.elementAt(11); 
				
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtSerNoLoanComp", collCompNo2);  
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColNum",colNum2);
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCollTypeName",collTypeName2); 
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfHfcSCD", hfHfcSCD2);
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfAmount", hfAmount2);  
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCurIdCodeC", CompCurIdCodeC2); 
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfPriority", hfPriority2);                 
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtAccPrior", accPrior2);                 
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmo", compCovAmo2);
				 ra.setAttribute("CollComponentItemLDB","CollComponentItemOV_txtCompCovAmo",compCovAmo2);
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmoRef", compCovAmoRef2);
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate", compCovDate2); 
				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCurIdRefCodeC", curIdRefCodeC2); 

				 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate1", compCovDate2);                 
					
					
				//invisible
				
				 	 COL_HEA_ID2 = (java.math.BigDecimal) selectedRowUniqueCollUpdate2.elementAt(0);
					 COL_TYPE_ID2 =  (java.math.BigDecimal) selectedRowUniqueCollUpdate2.elementAt(1);
					 collTypeCode2 = (String) selectedRowUniqueCollUpdate2.elementAt(2);
					 hfHfcId2 =  (java.math.BigDecimal) selectedRowUniqueCollUpdate2.elementAt(3);
					 COMP_CUR_ID2 =  (java.math.BigDecimal) selectedRowUniqueCollUpdate2.elementAt(4);
					 CUR_ID_REF2 = (java.math.BigDecimal) selectedRowUniqueCollUpdate2.elementAt(5);
					 COMP_HF_PRIOR_ID2 = (java.math.BigDecimal) selectedRowUniqueCollUpdate2.elementAt(6);
					 excRatRef2 = (java.math.BigDecimal) selectedRowUniqueCollUpdate2.elementAt(7);
					 excRatRefDate2 =  (java.sql.Date) selectedRowUniqueCollUpdate2.elementAt(8);
					 compDateFrom2 =  (java.sql.Date) selectedRowUniqueCollUpdate2.elementAt(9);
					 compDateUntil2 = (java.sql.Date) selectedRowUniqueCollUpdate2.elementAt(10);
					 
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_COL_HEA_ID",COL_HEA_ID2); 
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_COL_TYPE_ID",COL_TYPE_ID2); 
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCollTypeCode",collTypeCode2); 
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfHfcId", hfHfcId2);                 
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_CUR_ID", COMP_CUR_ID2);                
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUR_ID_REF", CUR_ID_REF2);                 
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_HF_PRIOR_ID", COMP_HF_PRIOR_ID2);           
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRef", excRatRef2);               
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRefDate", excRatRefDate2);           
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateFrom", compDateFrom2);            
					 ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateUntil", compDateUntil2);           
										
	}//prepForUpdateCollComponentItem                                                                                      

	
	
	public void resetCollComponentItemLDB(){
		

		ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUS_ID",null);                              
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColaCusIdUseRegisterNo",null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColaCusIdUseOwnerName",null);  
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COL_TYPE_ID",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCollTypeCode",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCollTypeName",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COL_HEA_ID",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtColNum",null); 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_ID", null);                    
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_HF_PRIOR_ID", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmo", null);              
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_COMP_CUR_ID", null);                
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCurIdCodeC", null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate", null);             
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovAmoRef", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_CUR_ID_REF", null);                 
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCurIdRefCodeC", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRef", null);               
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtExcRatRefDate", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovBamo", null);             
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovBdate"  , null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovBamoRef", null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateFrom", null);            
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompDateUntil", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompStatus", null);              
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompSpecStatus", null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_USE_OPEN_ID", null);                
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUseOpenIdLogin", null);          
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUseOpenIdName", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtOpeningTs", null);               
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_USE_ID", null);                     
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUseIdLogin", null);              
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUseIdName", null);               
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtUserLock", null);                
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtSerNoLoanComp", null);           
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfAmount", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfHfcId", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfHfcSCD", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtHfPriority", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtAccPrior", null);                 
		
		ra.setAttribute("CollComponentItemLDB","CollComponentItem_txtCompCovDate1", null);                 
		
		ra.setAttribute("CollComponentItemLDB","curSumTotalFromCollComponent", null);                 
		
		ra.setAttribute("CollComponentItemLDB","curAvailableCollComponent", null);                 
		ra.setAttribute("CollComponentItemLDB","curSumTotalCollComp", null);                 
		ra.setAttribute("CollComponentItemLDB","curSumAllCollComptbl", null);                 

	}//resetCollComponentItemLDB
	
	
	public void resetCollateralDialogLDBCollComponentItem(){
//		CollateralDialogLDB
		////////////////////////////////////////////
		//CollComponentItemD_COL_TYPE_ID
		//CollComponentItemD_txtCollTypeCode
		//CollComponentItemD_txtCollTypeName
		//CollComponentItemD_COL_HEA_ID
		//CollComponentItemD_txtColNum
		//CollComponentItemD_COMP_ID
		//CollComponentItemD_COMP_HF_PRIOR_ID
		//CollComponentItemD_txtCompCovAmo
		//CollComponentItemD_COMP_CUR_ID
		//CollComponentItemD_txtCompCurIdCodeC
		//CollComponentItemD_txtCompCovDate
		//CollComponentItemD_txtCompCovAmoRef
		//CollComponentItemD_CUR_ID_REF
		//CollComponentItemD_txtCurIdRefCodeC
		//CollComponentItemD_txtExcRatRef
		//CollComponentItemD_txtExcRatRefDate
		//CollComponentItemD_txtCompCovBamo
		//CollComponentItemD_txtCompCovBdate
		//CollComponentItemD_txtCompCovBamoRef
		//CollComponentItemD_txtCompDateFrom
		//CollComponentItemD_txtCompDateUntil
		//CollComponentItemD_txtCompStatus
		//CollComponentItemD_txtCompSpecStatus
		//CollComponentItemD_USE_OPEN_ID
		//CollComponentItemD_txtUseOpenIdLogin
		//CollComponentItemD_txtUseOpenIdName
		//CollComponentItemD_txtOpeningTs
		//CollComponentItemD_USE_ID
		//CollComponentItemD_txtUseIdLogin
		//CollComponentItemD_txtUseIdName
		//CollComponentItemD_txtUserLock
		//CollComponentItemD_txtSerNoLoanComp
		//CollComponentItemD_txtBalance
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COL_TYPE_ID" , null);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCollTypeCode", null);          
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCollTypeName", null);          
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COL_HEA_ID", null);               
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtColNum", null);                
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_COMP_ID", null);                  
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COMP_HF_PRIOR_ID", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovAmo", null);            
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_COMP_CUR_ID", null);              
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCompCurIdCodeC", null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovDate", null);           
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovAmoRef", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_CUR_ID_REF", null);               
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtCurIdRefCodeC", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRef", null);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtExcRatRefDate", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovBamo", null);           
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovBdate"  , null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompCovBamoRef", null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompDateFrom", null);          
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompDateUntil", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompStatus", null);            
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtCompSpecStatus", null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_USE_OPEN_ID", null);              
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUseOpenIdLogin", null);        
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUseOpenIdName", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtOpeningTs", null);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_USE_ID", null);                   
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUseIdLogin", null);            
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUseIdName", null);             
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtUserLock", null);              
		ra.setAttribute("CollateralDialogLDB","CollComponentItemD_txtSerNoLoanComp", null);         
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfAmount", null);  
		
		
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfHfcId", null);                 
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfHfcSCD", null);                 
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtHfPriority", null);                 
		ra.setAttribute("CollateralDialogLDB","CollComponentItemCD_txtAccPrior", null);                 
		   
		
	
	
	}//resetCollateralDialogLDBCollComponentItem

	
	
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
	
}
