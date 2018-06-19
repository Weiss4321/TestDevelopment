/*
 * Created on 2006.01.26
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
public class LoanComponentItem extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/LoanComponentItem.java,v 1.26 2006/03/15 08:59:39 hrasia Exp $";	
	
	//Old data of selected row
	String 							updLoanCompNoOld = null;                                                                
	String 							updAccNoOld = null;                                                                     
	String 							updLoantypeSCDOld = null;                                                               
	java.math.BigDecimal 			updBalanceOld = null;                                                             
	java.sql.Date 					updBalanceDateOld = null;                                                           
	String 							updBalanceCurIdLoanCodeCOld = null;                                                     
	java.math.BigDecimal 			updBalanceCurrentToCoverOld = null;                                               
	java.math.BigDecimal 			updBalanceRefCurrentToCoverOld = null;                                            
	java.sql.Date 					updBalanceLinkToCoverDateOld = null;                                                
	String 							updBalanceCurIdRefLoanCodeCOld = null;                                                  
	                                                                                                         
	                                                                                                         
	java.math.BigDecimal 			laAccIdOld = null;                                                                
	java.math.BigDecimal 			CUR_IDOld = null;                                                                 
	java.math.BigDecimal 			CUR_ID_REFOld = null;                                                             
	//Tecaj referente valute za komponentu plasmana ( ne cijeli plasmana)                                    
	java.math.BigDecimal 			excRatRefPartOld = null;                                                          
	//Datum tecaja eferente valute za komponentu plasmana ( uvijek today )                                   
	java.sql.Date 					excRatRefDatePartOld = null;                                                        
	java.sql.Date 					lcDateFromOld = null;                                                               
	java.sql.Date 					lcDateUntilOld = null;
	
	
	hr.vestigo.framework.common.TableData tdLoanForUpdateOld = null;                                              
	java.util.Vector selectedRowUniqueLoanUpdateOld = null;                                                     
	java.util.Vector selectedRowLoanUpdateOld = null;
	
	
	
	public LoanComponentItem(ResourceAccessor ra) {
		super(ra);
			
	}
	
	public void LoanComponentItem_SE() {
		
		System.out.println ( "USAO NA EKRAN LoanComponentItem");
		
		java.sql.Date todaySQLDate = null;
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		long timeT = calendar.getTime().getTime();
		todaySQLDate = new java.sql.Date(timeT);
		
		
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
			System.out.println ( "LoanComponentItem LoanComponentItem_SE 	scr_choose  ");
			ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceLinkToCoverDate",todaySQLDate);	
			//CollateralDialog
			
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurrentToCover",null);
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceRefCurrentToCover",null);
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceLinkToCoverDate",null);
		
		}
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			
				if (!ra.isLDBExists("LoanComponentItemLDB")) {
					ra.createLDB("LoanComponentItemLDB");
				}
			
				//Save old data of selected row
			
				if(ra.getAttribute("tblCollateralDialogLoanComp") != null){
					tdLoanForUpdateOld = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
					selectedRowUniqueLoanUpdateOld =(java.util.Vector) tdLoanForUpdateOld.getSelectedRowUnique();                  
					selectedRowLoanUpdateOld = tdLoanForUpdateOld.getSelectedRowData();    
					
					System.out.println ( "LoanComponentItem LoanComponentItem_SE 	scr_update  ");
					prepForUpdateLoanComponentItem();
					
					//POZIV TRANSAKCIJE ZA DOHVAT ZADNJEG STANJA PLASMANA PREKO ID LOAN ACCOUNT
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUS_ID",(java.math.BigDecimal)  ra.getAttribute("CollateralDialogLDB", "CUS_ID"));                              
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtColaCusIdUseRegisterNo",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaRegisterNo"));           
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtColaCusIdUseOwnerName",(String)  ra.getAttribute("CollateralDialogLDB", "CollateralDialog_txtColaOwnerName")); 
					
					//TG LoanComponentItemGetFullBalance
					try {
						 ra.executeTransaction();
					} catch (VestigoTMException vtme) {
						 if (vtme.getMessageID() != null)
							 ra.showMessage(vtme.getMessageID());
						 
					} 
					
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceDate1",(java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceDate")); 
					
				}// nije null
			
			
			
			
		}
		todaySQLDate = null;
		calendar = null;
		
		
		
	}//LoanComponentItem_SE
	
	
	public void exit() {
		
		
		if (!ra.isLDBExists("LoanComponentItemLDB")) {
	 		ra.createLDB("LoanComponentItemLDB");
	 	}
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
					
			ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalanceCurrentToCover",(java.math.BigDecimal )  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurrentToCover"));
						
			ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalanceRefCurrentToCover",(java.math.BigDecimal )  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceRefCurrentToCover"));
			
			ra.setAttribute("LoanComponentItemSLDB","LoanComponentItemS_txtBalanceLinkToCoverDate",(java.sql.Date )  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceLinkToCoverDate"));
			ra.exitScreen();
			ra.exitScreen();
		}
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			ra.exitScreen();
		}
		
		
		
	}//exit
	
		
	
	public boolean BalanceCurrentToCover_FV() {
		
		
		if((ra.getScreenContext().compareTo("scr_choose")== 0 )||(ra.getScreenContext().compareTo("scr_update")== 0 )){
			
			hr.vestigo.modules.collateral.util.CollateralUtil utilObj = new hr.vestigo.modules.collateral.util.CollateralUtil(ra);
		
		
			java.sql.Date todaySQLDate = null;
			java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
			long timeT = calendar.getTime().getTime();
			todaySQLDate = new java.sql.Date(timeT);
	
			java.math.BigDecimal oneNumber = new java.math.BigDecimal("1.0000000000000000000000000000000000000000000000000000000000000000");
			oneNumber.setScale(64);
			System.out.println("VALIDACIJA VRIJEDNOST " + ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurrentToCover") );
		
			java.math.BigDecimal balanceCurrentToCover = (java.math.BigDecimal) ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurrentToCover");
			java.math.BigDecimal balance = (java.math.BigDecimal) ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalance");
		
			java.math.BigDecimal currencyId = (java.math.BigDecimal) ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID");
		
			java.math.BigDecimal refCurrencyId = (java.math.BigDecimal) ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID_REF");
			java.math.BigDecimal exchRate = null;
		
		
			if(balanceCurrentToCover.compareTo(balance)== 1){
				System.out.println("Provjera balance < balanceCurrentToCover     " + balance + "    " + balanceCurrentToCover);
				ra.showMessage("wrnclt19");
				return false;
			}
		
		
			//Dohvat tekuceg konteksta
			String currentScreenContext = ra.getScreenContext().trim();
			
			hr.vestigo.framework.common.TableData tdLoanLCI = null;
			java.math.BigDecimal currentLoanAccId = (java.math.BigDecimal) ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_laAccId");
			java.math.BigDecimal  amoLoanCompLCI = new java.math.BigDecimal("0.00");
			java.math.BigDecimal  accountIdLoanLCI = null;
		
		
			java.math.BigDecimal  curSumTotalLoanComp = new java.math.BigDecimal("0.00");
			//curSumTotalLoanComp = curSumAllLoanComptbl + curSumTotalFromLoanComponent
			java.math.BigDecimal  curSumAllLoanComptbl = new java.math.BigDecimal("0.00");
			java.math.BigDecimal  curSumTotalFromLoanComponent = new java.math.BigDecimal("0.00");
		
			java.math.BigDecimal  curAvailableLoanComponent = new java.math.BigDecimal("0.00");
			java.math.BigDecimal  loanComponentItemBalance = new java.math.BigDecimal("0.00");
		
			//curAvailableLoanComponent = LoanComponentItem_txtBalance - (curSumAllLoanComptbl + curSumTotalFromLoanComponent)
			//curAvailableLoanComponent = LoanComponentItem_txtBalance - (curSumTotalLoanComp)

		
		
			//treba proci kroz tblCollateralDialogLoanComp i pokupiti sve s istim brojem plasmana
			tdLoanLCI = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
		
			if (tdLoanLCI != null){
								java.util.Vector vidljiviDioTabliceLoanLCI = tdLoanLCI.getData();
								java.util.Vector nevidljiviDioTabliceLoanLCI = (java.util.Vector) tdLoanLCI.getUnique();
								for(int i = 0; i < vidljiviDioTabliceLoanLCI.size(); i++){
									java.util.Vector vidljiviRedakLoanLCI = (java.util.Vector) vidljiviDioTabliceLoanLCI.elementAt(i);
									java.util.Vector nevidljiviRedakLoanLCI = (java.util.Vector) nevidljiviDioTabliceLoanLCI.elementAt(i);
						
									amoLoanCompLCI = (java.math.BigDecimal) vidljiviRedakLoanLCI.elementAt(6);
									accountIdLoanLCI = (java.math.BigDecimal) nevidljiviRedakLoanLCI.elementAt(0);
						
									if(currentLoanAccId.compareTo(accountIdLoanLCI)== 0){
										curSumAllLoanComptbl = curSumAllLoanComptbl.add(amoLoanCompLCI);
									}
								}//glavni for
			}
		
		
			//poziv transakcije za provjeru iz tablice loan_component
			// TG LoanComponentItemCheck

			try {
				ra.executeTransaction();
			} catch (VestigoTMException vtme) {
				if (vtme.getMessageID() != null)
					ra.showMessage(vtme.getMessageID());
				return true;
			} 
		
		
			curSumTotalFromLoanComponent = (java.math.BigDecimal)ra.getAttribute("LoanComponentItemLDB","curSumTotalFromLoanComponent");	
			
			//Vracanje tekuceg konteksta
			ra.setScreenContext(currentScreenContext);
		
			if(ra.getScreenContext().compareTo("scr_update")== 0 ){
				java.math.BigDecimal oldBalanceCurrentToCover = null;
				oldBalanceCurrentToCover = (java.math.BigDecimal) ra.getAttribute("LoanComponentItemLDB","LoanComponentItemOV_txtBalanceCurrentToCover");
				System.out.println("LoanComponentItem       BalanceCurrentToCover_FV  scr_update 1  oldBalanceCurrentToCover je  " + oldBalanceCurrentToCover);
				System.out.println("LoanComponentItem       BalanceCurrentToCover_FV  scr_update 2  curSumAllLoanComptbl je  " + curSumAllLoanComptbl);
								
				//Kod update treba oduzeti staru vrijednost komponente plasmana upisanu u tbl
				curSumAllLoanComptbl = curSumAllLoanComptbl.subtract(oldBalanceCurrentToCover);
				System.out.println("LoanComponentItem       BalanceCurrentToCover_FV  scr_update 3  curSumAllLoanComptbl - oldBalanceCurrentToCover je  " + curSumAllLoanComptbl);
			}
		
		
			curSumTotalLoanComp = curSumTotalFromLoanComponent.add(curSumAllLoanComptbl);
			//curAvailableLoanComponent
			//curSumTotalLoanComp
			//curSumAllLoanComptbl
			//curSumTotalFromLoanComponent

			
			
			
			ra.setAttribute("LoanComponentItemLDB","curSumAllLoanComptbl",curSumAllLoanComptbl);
			ra.setAttribute("LoanComponentItemLDB","curSumTotalLoanComp",curSumTotalLoanComp);
		
			loanComponentItemBalance = (java.math.BigDecimal)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalance");
		
			curAvailableLoanComponent = loanComponentItemBalance.subtract(curSumTotalLoanComp);
		
			ra.setAttribute("LoanComponentItemLDB","curAvailableLoanComponent",curAvailableLoanComponent);
		
			//curAvailableLoanComponent = LoanComponentItem_txtBalance - (curSumAllLoanComptbl + curSumTotalFromLoanComponent)
			//curAvailableLoanComponent = LoanComponentItem_txtBalance - (curSumTotalLoanComp)
		
		
		
		
			if(balanceCurrentToCover.compareTo(curAvailableLoanComponent)== 1){
				System.out.println("Provjera curAvailableLoanComponent < balanceCurrentToCover     " + curAvailableLoanComponent + "    " + balanceCurrentToCover);
				ra.showMessage("wrnclt22");
				return false;
			}
		
		
		
		
			//IZRACUN IZNOSA U REF VALUTI
			//
		
			try{
				if (utilObj.getCurrentExchRate(todaySQLDate,currencyId)){
					exchRate = utilObj.getMiddRate();
				
					System.out.println(" Valuta i tecaj    " + currencyId + "   " + exchRate );
				
				
					//LoanComponentItem_txtBalanceRefCurrentToCover
					java.math.BigDecimal balanceRefCurrentToCover = balanceCurrentToCover.multiply(exchRate);
					balanceRefCurrentToCover = balanceRefCurrentToCover.divide(oneNumber,2,java.math.BigDecimal.ROUND_HALF_UP);
					ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceRefCurrentToCover", balanceRefCurrentToCover); 
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceRefCurrentToCover", balanceRefCurrentToCover);
					ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalance", balance); 
					ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurrentToCover", balanceCurrentToCover);
				
					//TECAJ KOMPONENTE PLASMANA ( NE CIJELOG IZNOSA )
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefPart", exchRate);
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefDatePart", todaySQLDate);
				
					//DATUMI KOMPONENTE PLASMANA
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_lcDateFrom", todaySQLDate);
					ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_lcDateUntil", (java.sql.Date) ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_laDateUntil"));
				
					balanceRefCurrentToCover = null;
				}
			}catch(java.sql.SQLException sqle){
			
			}catch(hr.vestigo.framework.util.db.EmptyRowSet db_ers_e){
			
			}
			balance  = null;
			balanceCurrentToCover = null;
			todaySQLDate = null;
			exchRate = null;
			refCurrencyId = null;
			currencyId = null;
			return true;
		
		
		}//if konteksti
		return true;
	}//BalanceCurrentToCover_FV
	
	public void selection() {
		
		
		if (!ra.isLDBExists("LoanComponentItemLDB")) {
	 		ra.createLDB("LoanComponentItemLDB");
	 	}
		if(ra.getScreenContext().compareTo("scr_choose")== 0){
					
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalance",(java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalance"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_CUR_ID",(java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_CUR_ID"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurIdLoanCodeC",(String)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurIdLoanCodeC"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceDate",(java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceDate"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceRef",(java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceRef"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_CUR_ID_REF",(java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_CUR_ID_REF"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurIdRefLoanCodeC",(String)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurIdRefLoanCodeC"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceDate1",(java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceDate1"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtExcRatRef",(java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtExcRatRef"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtExcRatRefDate",(java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtExcRatRefDate"));
			
			System.out.println("NA EKRANU " + ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurrentToCover") );
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurrentToCover",(java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurrentToCover"));
			
			System.out.println("NA LDB " + ra.getAttribute("CollateralDialogLDB", "LoanComponentItemD_txtBalanceCurrentToCover") );
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceRefCurrentToCover",(java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceRefCurrentToCover"));
			ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceLinkToCoverDate",(java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceLinkToCoverDate"));
			
			
			System.out.println ( "IZLAZIM IZ selection LoanComponentItem");
			puniTablicu();
			ra.exitScreen();
			ra.exitScreen();
			
		}
		
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			promijeniTablicu();
			ra.exitScreen();
		}
		
		
		
	}//selection
	
	
	private void prepForUpdateLoanComponentItem(){
		
		//This method reads current row from tblCollateralDialogLoanComp
		//and set id of loan account
		hr.vestigo.framework.common.TableData tdLoanForUpdate1 = null;
		java.util.Vector selectedRowUniqueLoanUpdate1 = null;
		java.util.Vector selectedRowLoanUpdate1 = null;
		
		java.math.BigDecimal laAccId1 = null;  
		
		java.math.BigDecimal updBalanceCurrentToCover1 = null;
		java.math.BigDecimal updBalanceRefCurrentToCover1 = null;
		java.sql.Date updBalanceLinkToCoverDate1 = null;
		
		tdLoanForUpdate1 = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
		selectedRowUniqueLoanUpdate1 =(java.util.Vector) tdLoanForUpdate1.getSelectedRowUnique();
		selectedRowLoanUpdate1 = tdLoanForUpdate1.getSelectedRowData();
		
		
		resetCollateralDialogLDBLoanComponentItem();
		resetLoanComponentItemLDB();
		
		
		laAccId1 = (java.math.BigDecimal) selectedRowUniqueLoanUpdate1.elementAt(0); 
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laAccId",laAccId1);
		
		
		updBalanceCurrentToCover1 = (java.math.BigDecimal) selectedRowLoanUpdate1.elementAt(6);
		updBalanceRefCurrentToCover1 =(java.math.BigDecimal) selectedRowLoanUpdate1.elementAt(7);
		updBalanceLinkToCoverDate1 = (java.sql.Date) selectedRowLoanUpdate1.elementAt(8);
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurrentToCover",updBalanceCurrentToCover1);
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceRefCurrentToCover",updBalanceRefCurrentToCover1);
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceLinkToCoverDate",updBalanceLinkToCoverDate1);
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItemOV_txtBalanceCurrentToCover",updBalanceCurrentToCover1);
		
		
		
		
	}//prepForUpdateLoanComponentItem
	
	
	
	
	
	
	
	public void promijeniTablicu() {
		//
		//
		//Read visible data from selected row ( selected loan component) 
		updLoanCompNoOld = (String) selectedRowLoanUpdateOld.elementAt(0);
		updLoanCompNoOld = updLoanCompNoOld.trim();
		updAccNoOld = (String) selectedRowLoanUpdateOld.elementAt(1);
		updLoantypeSCDOld = (String) selectedRowLoanUpdateOld.elementAt(2);
		updBalanceOld = (java.math.BigDecimal) selectedRowLoanUpdateOld.elementAt(3);
		updBalanceDateOld = (java.sql.Date) selectedRowLoanUpdateOld.elementAt(4);
		updBalanceCurIdLoanCodeCOld = (String) selectedRowLoanUpdateOld.elementAt(5);
		updBalanceCurIdLoanCodeCOld = updBalanceCurIdLoanCodeCOld.trim();
		updBalanceCurrentToCoverOld = (java.math.BigDecimal) selectedRowLoanUpdateOld.elementAt(6);
		updBalanceRefCurrentToCoverOld =(java.math.BigDecimal) selectedRowLoanUpdateOld.elementAt(7);
		updBalanceLinkToCoverDateOld = (java.sql.Date) selectedRowLoanUpdateOld.elementAt(8);
		updBalanceCurIdRefLoanCodeCOld = (String) selectedRowLoanUpdateOld.elementAt(9);
		updBalanceCurIdRefLoanCodeCOld = updBalanceCurIdRefLoanCodeCOld.trim();
		
		//Read invisible data from selected row ( selected loan component) 
		laAccIdOld = (java.math.BigDecimal) selectedRowUniqueLoanUpdateOld.elementAt(0); 
		CUR_IDOld = (java.math.BigDecimal) selectedRowUniqueLoanUpdateOld.elementAt(1);                       
		CUR_ID_REFOld = (java.math.BigDecimal) selectedRowUniqueLoanUpdateOld.elementAt(2);     
		//Tecaj referente valute za komponentu plasmana ( ne cijeli plasmana)
		excRatRefPartOld = (java.math.BigDecimal) selectedRowUniqueLoanUpdateOld.elementAt(3); 
		//Datum tecaja referente valute za komponentu plasmana ( uvijek today )
		excRatRefDatePartOld = (java.sql.Date) selectedRowUniqueLoanUpdateOld.elementAt(4); 
		lcDateFromOld = (java.sql.Date) selectedRowUniqueLoanUpdateOld.elementAt(5);                
		lcDateUntilOld = (java.sql.Date) selectedRowUniqueLoanUpdateOld.elementAt(6);      
		
		String 							updNewLoanCompNo = null;
		String 							updNewAccNo = null;
		String 							updNewLoantypeSCD = null;
		java.math.BigDecimal 			updNewBalance = null;
		java.sql.Date 					updNewBalanceDate = null;
		String 							updNewBalanceCurIdLoanCodeC = null;
		java.math.BigDecimal 			updNewBalanceCurrentToCover = null;
		java.math.BigDecimal 			updNewBalanceRefCurrentToCover = null;
		java.sql.Date 					updNewBalanceLinkToCoverDate = null;
		String 							updNewBalanceCurIdRefLoanCodeC = null;
		
		
		java.math.BigDecimal 			newLaAccId = null;  
		java.math.BigDecimal 			newCUR_ID = null;                       
		java.math.BigDecimal 			newCUR_ID_REF = null;    
		//Tecaj referente valute za komponentu plasmana ( ne cijeli plasmana)
		java.math.BigDecimal 			newExcRatRefPart = null;
		//Datum tecaja eferente valute za komponentu plasmana ( uvijek today )
		java.sql.Date 					newExcRatRefDatePart = null;
		java.sql.Date 					newLcDateFrom = null;                  
		java.sql.Date 					newLcDateUntil = null;    
		
		
		
		

		
		updNewLoanCompNo = updLoanCompNoOld;
		updNewAccNo = updAccNoOld;
		updNewLoantypeSCD = updLoantypeSCDOld;
		
		updNewBalance = (java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalance");
		updNewBalanceDate = (java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceDate");;
		updNewBalanceCurIdLoanCodeC = (String)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurIdLoanCodeC");
		updNewBalanceCurIdLoanCodeC = updNewBalanceCurIdLoanCodeC.trim();
		updNewBalanceCurrentToCover =(java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurrentToCover");
		updNewBalanceRefCurrentToCover = (java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceRefCurrentToCover");
		updNewBalanceLinkToCoverDate = (java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceLinkToCoverDate");
		updNewBalanceCurIdRefLoanCodeC = (String)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtBalanceCurIdRefLoanCodeC");
		updNewBalanceCurIdRefLoanCodeC = updNewBalanceCurIdRefLoanCodeC.trim();
		
		
		
		newLaAccId = laAccIdOld;
		newCUR_ID = (java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_CUR_ID");                      
		newCUR_ID_REF = (java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_CUR_ID_REF"); 
		//Tecaj referente valute za komponentu plasmana ( ne cijeli plasmana)
		newExcRatRefPart = (java.math.BigDecimal)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtExcRatRefPart");
		//Datum tecaja eferente valute za komponentu plasmana ( uvijek today )
		newExcRatRefDatePart = (java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_txtExcRatRefDatePart");
		newLcDateFrom = (java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_laDateFrom");                 
		newLcDateUntil = (java.sql.Date)  ra.getAttribute("LoanComponentItemLDB", "LoanComponentItem_laDateUntil");
		
		
		
		
		
		if (!(updNewBalance.compareTo(updBalanceOld)== 0)){
			System.out.println("LoanComponentItem    promijeniTablicu     1  Razlika  updNewBalance  updBalanceOld " + updNewBalance + " " + updBalanceOld );
		} 
		if (!(updNewBalanceDate.compareTo(updBalanceDateOld) == 0)){
			System.out.println("LoanComponentItem    promijeniTablicu     2  Razlika  updNewBalanceDate  updBalanceDateOld " + updNewBalanceDate + " " + updBalanceDateOld );
		}	
		if(!(updNewBalanceCurIdLoanCodeC.compareTo(updBalanceCurIdLoanCodeCOld)== 0)){
			System.out.println("LoanComponentItem    promijeniTablicu     3  Razlika  updNewBalanceCurIdLoanCodeC  updBalanceCurIdLoanCodeCOld " + updNewBalanceCurIdLoanCodeC + " " + updBalanceCurIdLoanCodeCOld );
		}
		if(!(updNewBalanceCurIdRefLoanCodeC.compareTo(updBalanceCurIdRefLoanCodeCOld)== 0)){
			System.out.println("LoanComponentItem    promijeniTablicu     4  Razlika  updNewBalanceCurIdRefLoanCodeC  updBalanceCurIdRefLoanCodeCOld " + updNewBalanceCurIdRefLoanCodeC + " " + updBalanceCurIdRefLoanCodeCOld );
		}
			
			
			
			
		hr.vestigo.framework.common.TableData tdReplace = new hr.vestigo.framework.common.TableData();
		
		java.util.Vector rowOneV = new java.util.Vector();
		java.util.Vector rowOneNV = new java.util.Vector();
		
		rowOneV.add(updNewLoanCompNo);
		rowOneV.add(updNewAccNo);
		rowOneV.add(updNewLoantypeSCD);
		rowOneV.add(updNewBalance);
		rowOneV.add(updNewBalanceDate);
		rowOneV.add(updNewBalanceCurIdLoanCodeC);
		rowOneV.add(updNewBalanceCurrentToCover);
		rowOneV.add(updNewBalanceRefCurrentToCover);
		rowOneV.add(updNewBalanceLinkToCoverDate);
		rowOneV.add(updNewBalanceCurIdRefLoanCodeC);
		
		rowOneNV.add(newLaAccId);
		rowOneNV.add(newCUR_ID);
		rowOneNV.add(newCUR_ID_REF);
		rowOneNV.add(newExcRatRefPart);
		rowOneNV.add(newExcRatRefDatePart);
		rowOneNV.add(newLcDateFrom);
		rowOneNV.add(newLcDateFrom);
		
		updNewLoanCompNo = null;
		updNewAccNo = null;
		updNewLoantypeSCD = null;
		updNewBalance = null;
		updNewBalanceDate = null;
		updNewBalanceCurIdLoanCodeC = null;
		updNewBalanceCurrentToCover = null;
		updNewBalanceRefCurrentToCover = null;
		updNewBalanceLinkToCoverDate = null;
		updNewBalanceCurIdRefLoanCodeC = null;
		
		newLaAccId = null;  
		newCUR_ID = null;                       
		newCUR_ID_REF = null;    
		newExcRatRefPart = null;
		newExcRatRefDatePart = null;
		newLcDateFrom = null;                  
		newLcDateUntil = null;
		
		
		//treba proci kroz tblCollateralDialogLoanComp
		
		
		
		
		if (tdLoanForUpdateOld != null){
							java.util.Vector vidljiviDioTabliceLoanForUpdateOld = tdLoanForUpdateOld.getData();
							java.util.Vector nevidljiviDioTabliceLoanForUpdateOld = (java.util.Vector) tdLoanForUpdateOld.getUnique();
							for(int i = 0; i < vidljiviDioTabliceLoanForUpdateOld.size(); i++){
								java.util.Vector vidljiviRedakLoanLCI = (java.util.Vector) vidljiviDioTabliceLoanForUpdateOld.elementAt(i);
								java.util.Vector nevidljiviRedakLoanLCI = (java.util.Vector) nevidljiviDioTabliceLoanForUpdateOld.elementAt(i);
								
								
								updNewLoanCompNo = (String) vidljiviRedakLoanLCI.elementAt(0);
								updNewLoanCompNo = updNewLoanCompNo.trim();
								updNewAccNo =(String) vidljiviRedakLoanLCI.elementAt(1);
								updNewLoantypeSCD = (String) vidljiviRedakLoanLCI.elementAt(2);
								updNewBalance = (java.math.BigDecimal) vidljiviRedakLoanLCI.elementAt(3);
								updNewBalanceDate = (java.sql.Date) vidljiviRedakLoanLCI.elementAt(4);
								updNewBalanceCurIdLoanCodeC = (String) vidljiviRedakLoanLCI.elementAt(5);
								updNewBalanceCurrentToCover =(java.math.BigDecimal) vidljiviRedakLoanLCI.elementAt(6);
								updNewBalanceRefCurrentToCover = (java.math.BigDecimal) vidljiviRedakLoanLCI.elementAt(7);
								updNewBalanceLinkToCoverDate = (java.sql.Date) vidljiviRedakLoanLCI.elementAt(8);
								updNewBalanceCurIdRefLoanCodeC = (String) vidljiviRedakLoanLCI.elementAt(9);
								
								System.out.println("LoanComponentItem         promijeniTablicu  20      updNewLoanCompNo updNewAccNo                                " +  updNewLoanCompNo  + "  "  + updNewAccNo );
								System.out.println("LoanComponentItem         promijeniTablicu  21      updNewLoantypeSCD                                           " +  updNewLoantypeSCD  );
								System.out.println("LoanComponentItem         promijeniTablicu  22      updNewBalance updNewBalanceDate                             " +  updNewBalance   + "  "  + updNewBalanceDate );
								System.out.println("LoanComponentItem         promijeniTablicu  23      updNewBalanceCurIdLoanCodeC                                 " +  updNewBalanceCurIdLoanCodeC );
								System.out.println("LoanComponentItem         promijeniTablicu  24      updNewBalanceCurrentToCover updNewBalanceRefCurrentToCover  " +  updNewBalanceCurrentToCover  + "  "  + updNewBalanceRefCurrentToCover );
								System.out.println("LoanComponentItem         promijeniTablicu  25      updNewBalanceLinkToCoverDate updNewBalanceCurIdRefLoanCodeC " +  updNewBalanceLinkToCoverDate  + "  "  + updNewBalanceCurIdRefLoanCodeC );
								
								
								


								
								newLaAccId = (java.math.BigDecimal) nevidljiviRedakLoanLCI.elementAt(0);
								newCUR_ID = (java.math.BigDecimal) nevidljiviRedakLoanLCI.elementAt(1);                     
								newCUR_ID_REF = (java.math.BigDecimal) nevidljiviRedakLoanLCI.elementAt(2);  
								newExcRatRefPart = (java.math.BigDecimal) nevidljiviRedakLoanLCI.elementAt(3);
								newExcRatRefDatePart = (java.sql.Date) nevidljiviRedakLoanLCI.elementAt(4);
								newLcDateFrom = (java.sql.Date) nevidljiviRedakLoanLCI.elementAt(5);                 
								newLcDateUntil = (java.sql.Date) nevidljiviRedakLoanLCI.elementAt(6);

								
								System.out.println("LoanComponentItem         promijeniTablicu  26      newLaAccId                                                 " +  newLaAccId );
								System.out.println("LoanComponentItem         promijeniTablicu  27      newCUR_ID  newCUR_ID_REF                                   " +  newCUR_ID  + " " + newCUR_ID_REF );
								System.out.println("LoanComponentItem         promijeniTablicu  28      newCUR_ID  newCUR_ID_REF                                   " +  newCUR_ID  + " " + newCUR_ID_REF );
								System.out.println("LoanComponentItem         promijeniTablicu  29      newExcRatRefPart  newExcRatRefDatePart                     " +  newExcRatRefPart  + " " + newExcRatRefDatePart );
								System.out.println("LoanComponentItem         promijeniTablicu  29      newLcDateFrom  newLcDateUntil                              " +  newLcDateFrom  + " " + newLcDateUntil );
								
								
								
								

								if(updNewLoanCompNo.compareTo(updLoanCompNoOld) == 0){
									tdReplace.addRow(rowOneV, rowOneNV);
									System.out.println("LoanComponentItem         promijeniTablicu  40      updNewLoanCompNo updLoanCompNoOld " +  updNewLoanCompNo  + "  "  + updLoanCompNoOld );
									
								}else{
									System.out.println("LoanComponentItem         promijeniTablicu  41      updNewLoanCompNo updLoanCompNoOld " +  updNewLoanCompNo  + "  "  + updLoanCompNoOld );
									
									System.out.println("LoanComponentItem         promijeniTablicu  50      updNewLoanCompNo updNewAccNo " +  updNewLoanCompNo  + "  "  + updNewAccNo );
									System.out.println("LoanComponentItem         promijeniTablicu  51      updNewLoantypeSCD            " +  updNewLoantypeSCD  );
									System.out.println("LoanComponentItem         promijeniTablicu  52      updNewBalance updNewBalanceDate " +  updNewBalance   + "  "  + updNewBalanceDate );
									System.out.println("LoanComponentItem         promijeniTablicu  53      updNewBalanceCurIdLoanCodeC " +  updNewBalanceCurIdLoanCodeC );
									System.out.println("LoanComponentItem         promijeniTablicu  54      updNewBalanceCurrentToCover updNewBalanceRefCurrentToCover " +  updNewBalanceCurrentToCover  + "  "  + updNewBalanceRefCurrentToCover );
									System.out.println("LoanComponentItem         promijeniTablicu  55      updNewBalanceLinkToCoverDate updNewBalanceCurIdRefLoanCodeC " +  updNewBalanceLinkToCoverDate  + "  "  + updNewBalanceCurIdRefLoanCodeC );
									
									System.out.println("LoanComponentItem         promijeniTablicu  56      newLaAccId                                                 " +  newLaAccId );
									System.out.println("LoanComponentItem         promijeniTablicu  57      newCUR_ID  newCUR_ID_REF                                   " +  newCUR_ID  + " " + newCUR_ID_REF );
									System.out.println("LoanComponentItem         promijeniTablicu  58      newCUR_ID  newCUR_ID_REF                                   " +  newCUR_ID  + " " + newCUR_ID_REF );
									System.out.println("LoanComponentItem         promijeniTablicu  59      newExcRatRefPart  newExcRatRefDatePart                     " +  newExcRatRefPart  + " " + newExcRatRefDatePart );
									System.out.println("LoanComponentItem         promijeniTablicu  60      newLcDateFrom  newLcDateUntil                              " +  newLcDateFrom  + " " + newLcDateUntil );
									
									java.util.Vector rowNewOneV = new java.util.Vector();
									java.util.Vector rowNewOneNV = new java.util.Vector();
									
									rowNewOneV.add(updNewLoanCompNo);
									rowNewOneV.add(updNewAccNo);
									rowNewOneV.add(updNewLoantypeSCD);
									rowNewOneV.add(updNewBalance);
									rowNewOneV.add(updNewBalanceDate);
									rowNewOneV.add(updNewBalanceCurIdLoanCodeC);
									rowNewOneV.add(updNewBalanceCurrentToCover);
									rowNewOneV.add(updNewBalanceRefCurrentToCover);
									rowNewOneV.add(updNewBalanceLinkToCoverDate);
									rowNewOneV.add(updNewBalanceCurIdRefLoanCodeC);
									
									rowNewOneNV.add(newLaAccId);
									rowNewOneNV.add(newCUR_ID);
									rowNewOneNV.add(newCUR_ID_REF);
									rowNewOneNV.add(newExcRatRefPart);
									rowNewOneNV.add(newExcRatRefDatePart);
									rowNewOneNV.add(newLcDateFrom);
									rowNewOneNV.add(newLcDateUntil);
									tdReplace.addRow(rowNewOneV,rowNewOneNV);
									
									
									
								}
								
								
								updNewLoanCompNo = null;
								updNewAccNo = null;
								updNewLoantypeSCD = null;
								updNewBalance = null;
								updNewBalanceDate = null;
								updNewBalanceCurIdLoanCodeC = null;
								updNewBalanceCurrentToCover = null;
								updNewBalanceRefCurrentToCover = null;
								updNewBalanceLinkToCoverDate = null;
								updNewBalanceCurIdRefLoanCodeC = null;
								
								newLaAccId = null;  
								newCUR_ID = null;                       
								newCUR_ID_REF = null;    
								newExcRatRefPart = null;
								newExcRatRefDatePart = null;
								newLcDateFrom = null;                  
								newLcDateUntil = null;
								
							}//glavni for
		}
	
		try{
			tdLoanForUpdateOld.clear();
			ra.setAttribute("CollateralDialogLDB","tblCollateralDialogLoanComp",tdLoanForUpdateOld); 
			ra.setAttribute("CollateralDialogLDB","tblCollateralDialogLoanComp",tdReplace); 
			
		}catch(	VestigoTableException e){
			
		}
		
		
		
		
		
		
		//reset values
		
		resetOldRowValues();
		rowOneV = null;
		rowOneNV = null;
		tdReplace = null;
		tdLoanForUpdateOld = null;
		
		
	}//promijeniTablicu
	
	
	
	
	
	public void puniTablicu() {
		int vel = 0;
		hr.vestigo.framework.common.TableData td1 = null;
		if(ra.getAttribute("tblCollateralDialogLoanComp")==null){
			System.out.println("LoanComponentItem puniTablicu 1");
			td1=null;
			td1 = new hr.vestigo.framework.common.TableData();
		}else{
			System.out.println("LoanComponentItem puniTablicu 2");
			td1 = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
		}
		
        if (td1 != null){
        	String velString = null;
			java.util.Vector vidljiviDioTabliceZaBroj = td1.getData();
						
			for(int i = 0; i < vidljiviDioTabliceZaBroj.size(); i++){
				java.util.Vector vidljiviRedakZaBroj = (java.util.Vector) vidljiviDioTabliceZaBroj.elementAt(i);
				velString =  (String) vidljiviRedakZaBroj.elementAt(0);
				vel = Integer.parseInt(velString);
				vidljiviRedakZaBroj = null;
			}
			velString = null;
			vidljiviDioTabliceZaBroj = null;
			vel = vel + 1;
			System.out.println("LoanComponentItem puniTablicu 3   vel " + vel);
			
			
			
			java.util.Vector rowv = new java.util.Vector();
			java.util.Vector rown = new java.util.Vector();
			
			
			System.out.println("LoanComponentItem puniTablicu 4   Punjenje vektora vidljivih podataka  ");
			//LOAN_COMPONENT
			
			rowv.add(vel+"");
			rowv.add((String)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_accNo")); 
			rowv.add((String)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_loantypeSCD" ));  
			rowv.add((java.math.BigDecimal)  ra.getAttribute("CollateralDialogLDB", "LoanComponentItemD_txtBalance"));
			rowv.add((java.sql.Date)  ra.getAttribute("CollateralDialogLDB", "LoanComponentItemD_txtBalanceDate"));
			rowv.add((String)  ra.getAttribute("CollateralDialogLDB", "LoanComponentItemD_txtBalanceCurIdLoanCodeC"));
			rowv.add((java.math.BigDecimal)  ra.getAttribute("CollateralDialogLDB", "LoanComponentItemD_txtBalanceCurrentToCover"));
			rowv.add((java.math.BigDecimal)  ra.getAttribute("CollateralDialogLDB", "LoanComponentItemD_txtBalanceRefCurrentToCover"));
			rowv.add((java.sql.Date)  ra.getAttribute("CollateralDialogLDB", "LoanComponentItemD_txtBalanceLinkToCoverDate"));
			rowv.add((String)  ra.getAttribute("CollateralDialogLDB", "LoanComponentItemD_txtBalanceCurIdRefLoanCodeC"));
			
			
			rown.add((java.math.BigDecimal)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_laAccId"));
			rown.add((java.math.BigDecimal)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID"));
			rown.add((java.math.BigDecimal)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID_REF"));
			rown.add((java.math.BigDecimal)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefPart"));
			rown.add((java.sql.Date)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefDatePart"));
			rown.add((java.sql.Date)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_lcDateFrom"));
			rown.add((java.sql.Date)ra.getAttribute("LoanComponentItemLDB","LoanComponentItem_lcDateUntil"));
			
			
        	td1.addRow(rowv,rown);
        	
        	ra.setAttribute("CollateralDialogLDB","tblCollateralDialogLoanComp",td1);
        	resetLoanComponentItemLDB();
        	resetCollateralDialogLDBLoanComponentItem();
        	
        	
        }else{
        	//ra.showMessage("wrn299");
        	
			return;
        
        }
        
	
	}//puniTablicu
	
	public void resetLoanComponentItemLDB(){
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUS_ID",null);                              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtColaCusIdUseRegisterNo",null);           
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtColaCusIdUseOwnerName",null);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalance",null);                          
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID",null);                              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurIdLoanCodeC",null);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceDate",null);                      
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceRef",null);                       
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_CUR_ID_REF",null);                          
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurIdRefLoanCodeC",null);         
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceDate1",null);                     
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRef",null);                        
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefDate",null);   
		
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceCurrentToCover",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceRefCurrentToCover",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtBalanceLinkToCoverDate",null);    
		
				                                                                                                  

		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laAccId",null);        
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_accNo",null);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loanType",null);                  
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loantypeSCV" ,null);              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_loantypeSCD" ,null );             
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleLoan" ,null);               
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleloanSCV" ,null );           
	    ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_moduleloanSCD"  ,null );          
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_amount" , null );                 
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_startDate" , null );              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_payedDate" , null);               
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceB" ,  null );              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceBDdate" , null  );         
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_balanceBref" , null );            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laDateFrom" , null);              
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laDateUntil" ,  null);            
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laStatus" , null);                
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_laSpecStatus" , null );           
		 
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefPart",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_txtExcRatRefDatePart",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_lcDateFrom",null);    
		ra.setAttribute("LoanComponentItemLDB","LoanComponentItem_lcDateUntil",null);    
		ra.setAttribute("LoanComponentItemLDB","curAvailableLoanComponent",null);    
		ra.setAttribute("LoanComponentItemLDB","curSumTotalLoanComp",null);    
		ra.setAttribute("LoanComponentItemLDB","curSumAllLoanComptbl",null);    
		ra.setAttribute("LoanComponentItemLDB","curSumTotalFromLoanComponent",null);    

	}//resetLoanComponentItemLDB
	
	
	public void resetCollateralDialogLDBLoanComponentItem(){

	
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalance",null);  
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_CUR_ID",null);   
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurIdLoanCodeC",null); 
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceDate",null);                      
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceRef",null);  
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_CUR_ID_REF",null);                          
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurIdRefLoanCodeC",null); 
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceDate1",null); 
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtExcRatRef",null);                        
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtExcRatRefDate",null);   
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceCurrentToCover",null);    
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceRefCurrentToCover",null);    
		ra.setAttribute("CollateralDialogLDB","LoanComponentItemD_txtBalanceLinkToCoverDate",null);    
		
	
		
		
	}//resetCollateralDialogLDBLoanComponentItem

	public void resetOldRowValues(){
		 tdLoanForUpdateOld = null;                                              
		 selectedRowUniqueLoanUpdateOld = null;                                                     
		 selectedRowLoanUpdateOld = null;
		                                                                                                         
		 updLoanCompNoOld = null;                                                                
		 updAccNoOld = null;                                                                     
		 updLoantypeSCDOld = null;                                                               
		 updBalanceOld = null;                                                             
		 updBalanceDateOld = null;                                                           
		 updBalanceCurIdLoanCodeCOld = null;                                                     
		 updBalanceCurrentToCoverOld = null;                                               
		 updBalanceRefCurrentToCoverOld = null;                                            
		 updBalanceLinkToCoverDateOld = null;                                                
		 updBalanceCurIdRefLoanCodeCOld = null;                                                  
		                                                                                                         
		                                                                                                         
		 laAccIdOld = null;                                                                
		 CUR_IDOld = null;                                                                 
		 CUR_ID_REFOld = null;                                                             
		//Tecaj referente valute za komponentu plasmana ( ne cijeli plasmana)                                    
		 excRatRefPartOld = null;                                                          
		//Datum tecaja eferente valute za komponentu plasmana ( uvijek today )                                   
		 excRatRefDatePartOld = null;                                                        
		 lcDateFromOld = null;                                                               
		 lcDateUntilOld = null;                                                              
	    
	
	}//resetOldRowValues
	
}
