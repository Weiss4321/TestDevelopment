/*
 * Created on 2006.01.16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;

/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollateralDialog extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollateralDialog.java,v 1.18 2006/03/01 10:23:32 hrasia Exp $";
	
	
	public CollateralDialog(ResourceAccessor ra) {
		super(ra);
			
	}	
	
	public void CollateralDialog_SE() {
		if (!ra.isLDBExists("CollateralDialogLDB")) {
	 		ra.createLDB("CollateralDialogLDB");
	 		              
	 	}
		
		
		
		if(ra.getScreenContext().compareTo("scr_insert")== 0){
			ra.setAttribute("CollateralDialogLDB","CollateralDialog_txtColaRegisterNo",(String)  ra.getAttribute("CollateralLDB", "Collateral_txtColaCusIdUseRegisterNo") );
			ra.setAttribute("CollateralDialogLDB","CollateralDialog_txtColaOwnerName",(String)  ra.getAttribute("CollateralLDB", "Collateral_txtColaCusIdUseOwnerName") );
			ra.setAttribute("CollateralDialogLDB","CUS_ID",(java.math.BigDecimal)  ra.getAttribute("CollateralLDB", "CUS_ID")  );
			
		}
		if(ra.getScreenContext().compareTo("scr_update")== 0){
			ra.setAttribute("CollateralDialogLDB","CollateralDialog_txtColaRegisterNo",(String)  ra.getAttribute("CollateralLDB", "Collateral_txtColaCusIdUseRegisterNo") );
			ra.setAttribute("CollateralDialogLDB","CollateralDialog_txtColaOwnerName",(String)  ra.getAttribute("CollateralLDB", "Collateral_txtColaCusIdUseOwnerName") );
			ra.setAttribute("CollateralDialogLDB","CUS_ID",(java.math.BigDecimal)  ra.getAttribute("CollateralLDB", "CUS_ID")  );
			
		}
		
		
		
			
	}	 
	
	
	
	public void coll_comp(){
		//Dodavanje komponenti kolaterala
		ra.loadScreen("CollCompChoose","scr_coll_comp");
		
	}//coll_comp
	public void loan_comp(){
		//Dodavanje plasmana
		ra.loadScreen("CollCompChoose","scr_loan_comp");
		
	}//loan_comp
	public void limit_comp(){
		//Dodavanje limita
		ra.showMessage("wrnclt0");
	}//limit_comp
	
	
	
	public void confirm(){
		
		if (isTableEmpty("tblCollateralDialogLoanComp")) {
			System.out.println(".....CollateralDialog.java tblCollateralDialogLoanComp is EMPTY ");
			ra.showMessage("wrnclt18");
			return;
		}
		if (isTableEmpty("tblCollateralDialogCollComp")) {
			System.out.println(".....CollateralDialog.java tblCollateralDialogCollComp is EMPTY ");
			ra.showMessage("wrnclt17");
			return;
		}
		
		resetCD();
		
		hr.vestigo.framework.common.TableData tdLoan = null;
		hr.vestigo.framework.common.TableData tdCola = null;
		
		
		
		tdLoan = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
		tdCola = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");
		
		int velicinaLoanComp = 0;
		int velicinaCollComp = 0;
		
		velicinaLoanComp = tdLoan.getData().size();
		velicinaCollComp = tdCola.getData().size();
		if(velicinaCollComp != velicinaLoanComp){
			ra.showMessage("wrnclt26");
			return;
			
		}
		
		
		System.out.println("OK tablice");
        if (tdLoan != null){
        	 	java.util.Vector vidljiviDioTabliceLoan = tdLoan.getData();
 				java.util.Vector nevidljiviDioTabliceLoan = (java.util.Vector) tdLoan.getUnique();
 			
 			for(int i = 0; i < vidljiviDioTabliceLoan.size(); i++){
 				java.util.Vector vidljiviRedakLoan = (java.util.Vector) vidljiviDioTabliceLoan.elementAt(i);
 				java.util.Vector nevidljiviRedakLoan = (java.util.Vector) nevidljiviDioTabliceLoan.elementAt(i);
 				
 				System.out.println(".....CollateralDialog.java                                      ");
 				System.out.println(".....CollateralDialog.java      "+ i + ". vidljivi redakLoan    ");
 				for(int j= 0; j < vidljiviRedakLoan.size();j++){
 					System.out.println(".....CollateralDialog.java      " +j + ". element je " + vidljiviRedakLoan.elementAt(j));
 				}
 				System.out.println(".....CollateralDialog.java                                         ");
 				System.out.println(".....CollateralDialog.java      " + i + ". nevidljivi redakLoan    ");
 				for(int k= 0; k < nevidljiviRedakLoan.size();k++){
 					System.out.println(".....CollateralDialog.java      " + k + ". element je " + nevidljiviRedakLoan.elementAt(k));
 				}
 				
 			}//glavni for
        	
        	
        	
        }
        if (tdCola != null){
        		java.util.Vector vidljiviDioTabliceColl = tdCola.getData();
				java.util.Vector nevidljiviDioTabliceColl = (java.util.Vector) tdCola.getUnique();
			
				for(int i = 0; i < vidljiviDioTabliceColl.size(); i++){
					java.util.Vector vidljiviRedakColl = (java.util.Vector) vidljiviDioTabliceColl.elementAt(i);
					java.util.Vector nevidljiviRedakColl = (java.util.Vector) nevidljiviDioTabliceColl.elementAt(i);
				
					System.out.println(".....CollateralDialog.java                                      ");
					System.out.println(".....CollateralDialog.java      "+ i + ". vidljivi redakColl    ");
					for(int j= 0; j < vidljiviRedakColl.size();j++){
						System.out.println(".....CollateralDialog.java      " +j + ". element je " + vidljiviRedakColl.elementAt(j));
					}
					System.out.println(".....CollateralDialog.java                                         ");
					System.out.println(".....CollateralDialog.java      " + i + ". nevidljivi redakColl    ");
					for(int k= 0; k < nevidljiviRedakColl.size();k++){
						System.out.println(".....CollateralDialog.java      " + k + ". element je " + nevidljiviRedakColl.elementAt(k));
					}
				
				}//glavni for
        }//tdCola != null
		
       

        
        
		////////////////////////////////////////////////////
        //LoanComp visible 
        String ordNoLoanComp = null;
        String currencyCodeCLoanComp = null;
        java.math.BigDecimal amoLoanComp = null;
        java.sql.Date dateLinkLoanComp = null;
                
        //Loan visible 
        String accountLoan =null;
        
        //LoanComp invisible
        java.math.BigDecimal accountIdLoan = null;
        java.math.BigDecimal currencyIdLoanComp = null;
        
        
        //CollComp visible
        String ordNoCollComp = null;
        String currencyCodeCCollComp = null;
        java.math.BigDecimal amoCollComp = null;
        
        
        //CollComp invisible
        
        
        //Sum coll_comp
        java.math.BigDecimal sumAmoCollCompZero = new java.math.BigDecimal("0.00");
        java.math.BigDecimal sumAmoCollComp = new java.math.BigDecimal("0.00");
        boolean moze = true;
        
        
        if (tdLoan != null){
    	 		java.util.Vector vidljiviDioTabliceLoan = tdLoan.getData();
				java.util.Vector nevidljiviDioTabliceLoan = (java.util.Vector) tdLoan.getUnique();
			
				for(int i = 0; (i < vidljiviDioTabliceLoan.size()) && moze ; i++){
						java.util.Vector vidljiviRedakLoan = (java.util.Vector) vidljiviDioTabliceLoan.elementAt(i);
						java.util.Vector nevidljiviRedakLoan = (java.util.Vector) nevidljiviDioTabliceLoan.elementAt(i);
				
				
							ordNoLoanComp = (String) vidljiviRedakLoan.elementAt(0);
							ordNoLoanComp = ordNoLoanComp.trim();
							accountLoan = (String) vidljiviRedakLoan.elementAt(1);
							accountLoan = accountLoan.trim();
							currencyCodeCLoanComp = (String) vidljiviRedakLoan.elementAt(5);
							currencyCodeCLoanComp = currencyCodeCLoanComp.trim();
							amoLoanComp = (java.math.BigDecimal) vidljiviRedakLoan.elementAt(6);
							dateLinkLoanComp = (java.sql.Date) vidljiviRedakLoan.elementAt(8);
				
							
							accountIdLoan = (java.math.BigDecimal) nevidljiviRedakLoan.elementAt(0);
							currencyIdLoanComp = (java.math.BigDecimal) nevidljiviRedakLoan.elementAt(1);
							
							
							
							 if (tdCola != null){
							 		java.util.Vector vidljiviDioTabliceColl = tdCola.getData();
							 		java.util.Vector nevidljiviDioTabliceColl = (java.util.Vector) tdCola.getUnique();
							 		sumAmoCollComp = sumAmoCollCompZero;
							 		
								for(int t = 0; t < vidljiviDioTabliceColl.size(); t++){
											java.util.Vector vidljiviRedakColl = (java.util.Vector) vidljiviDioTabliceColl.elementAt(t);
											java.util.Vector nevidljiviRedakColl = (java.util.Vector) nevidljiviDioTabliceColl.elementAt(t);
								
											
											ordNoCollComp = (String) vidljiviRedakColl.elementAt(0);
											ordNoCollComp = ordNoCollComp.trim();
											
											currencyCodeCCollComp = (String) vidljiviRedakColl.elementAt(5);
											currencyCodeCCollComp = currencyCodeCCollComp.trim();
											
											amoCollComp = (java.math.BigDecimal) vidljiviRedakColl.elementAt(8);
											
											if(currencyCodeCCollComp.compareTo(currencyCodeCLoanComp)== 0){
												
												if(ordNoLoanComp.compareTo(ordNoCollComp) == 0){
													sumAmoCollComp = sumAmoCollComp.add(amoCollComp);
													System.out.println(" sumAmoCollComp   " + sumAmoCollComp);
												}
												
											}else{
												System.out.println(" SREDITI AKO JE DRUG AVALUT AIAKO N EBI TREBALA");
											}
											
											
								
								}//glavni for CollComp
							 }//tdCola != null
							 if(amoLoanComp.compareTo(sumAmoCollComp) != 0){
							 	System.out.println("ordNoLoanComp amoLoanComp sumAmoCollComp " + ordNoLoanComp + " " + amoLoanComp + " " + sumAmoCollComp );
							 	ra.setAttribute("CollateralDialogLDB","ordNoLoanComp", ordNoLoanComp);
							 	ra.setAttribute("CollateralDialogLDB","amoLoanComp", amoLoanComp);
							 	ra.setAttribute("CollateralDialogLDB","sumAmoCollComp", sumAmoCollComp);
							 	System.out.println("Za {ordNoLoanComp}.komponentu plasmana postoji razlika izmedu iznosa plasmana ${amoLoanComp} i sume iznosa kolaterala ${sumAmoCollComp} kojima se pokriva. ");
							 	moze = false; 
							 }
							
							
							 
							
							
				
				}//for LoanComp
        }//tdLoan != null
	
        if(!moze){
        	ra.showMessage("wrnclt21");
			return;
        }else{
        	
        	//poziv transakcije inserta
        	try {
   			 	ra.executeTransaction();
        	} catch (VestigoTMException vtme) {
        			if (vtme.getMessageID() != null)
        						ra.showMessage(vtme.getMessageID());
        					
        	} 
   		
        }
		
		///////////////////////////////////////////////////
        ra.exitScreen();
        ra.refreshActionList("tblLoanRelColl");	
	}//confirm	
	
	
	
	public void resetCD(){
		//This method reset three elements
		
		//ordNoLoanComp
		//amoLoanComp
		//sumAmoCollComp
		
		//These elements is use to check if amount of one loan component ( amoLoanComp) which
		//order number ordNoLoanComp is equal to sum of all loan component which cover loan component
		//
		//In previous version I imagine that one loan component will be covered by more loan component
		//Now I have given up and I have next situation
		//One loan component is covered by only and only one coll component
		
		ra.setAttribute("CollateralDialogLDB","ordNoLoanComp", null);
	 	ra.setAttribute("CollateralDialogLDB","amoLoanComp",null );
	 	ra.setAttribute("CollateralDialogLDB","sumAmoCollComp", null);
		
	
	}//resetCD
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }
	
}
