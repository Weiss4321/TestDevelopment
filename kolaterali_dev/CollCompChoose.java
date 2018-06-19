package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;


/*
 * Created on 2006.01.26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollCompChoose extends Handler{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollCompChoose.java,v 1.14 2006/03/15 08:58:20 hrasia Exp $";
	
	java.util.Vector sihgtedRow = null;
	boolean statusChoose = false;
	
	/**
	 * Constructor for Collateral.
	 */
	public CollCompChoose(ResourceAccessor ra) {
		super(ra);
			
	}	
	
	public void CollCompChoose_SE() {
		
		
		if (!ra.isLDBExists("CollCompChooseLDB")) {
	 		ra.createLDB("CollCompChooseLDB");
	 	}
        
		hr.vestigo.framework.common.TableData td_choose = new hr.vestigo.framework.common.TableData();
        java.util.Vector row_choose = new java.util.Vector();
        row_choose.add("1");
        row_choose.add("Dodaj stavku");
        td_choose.addRow(row_choose, new java.math.BigDecimal("1"));
        java.util.Vector row_choose2 = new java.util.Vector();
        row_choose2.add("2");
        row_choose2.add("Promijeni stavku");
        td_choose.addRow(row_choose2, new java.math.BigDecimal("2"));
        java.util.Vector row_choose3 = new java.util.Vector();
        row_choose3.add("3");
        row_choose3.add("Brisi stavku");
        
        td_choose.addRow(row_choose3, new java.math.BigDecimal("3"));
        java.util.Vector row_choose4 = new java.util.Vector();
        row_choose4.add("4");
        row_choose4.add("Detalji stavke");
        td_choose.addRow(row_choose4, new java.math.BigDecimal("4"));     
        //System.out.println("MARICA"+ td_choose);
        ra.setAttribute("CollCompChooseLDB", "tblCollCompChoose", td_choose);
        row_choose = null;
        row_choose2 = null;
        row_choose3 = null;
        row_choose4 = null;
        td_choose = null;
	}//CollCompChoose_SE
	
	
	
	public boolean CollCompChoose_txtChooseCode_FV(String elementName, Object elementValue, Integer lookUpType){
		if (elementValue == null && ((String) elementValue).equalsIgnoreCase("")) {
			statusChoose = false;
			return true;
		}	
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("CollCompChooseLDB", "tblCollCompChoose");
		java.util.Vector td1 = td.getData();		
		java.util.Vector row;
		String pom;

		for (int i = 0; i < td1.size(); i++) {
			
			//uzima cijeli vidljivi redak iz tabele
			row = (java.util.Vector) td1.elementAt(i);
			
			//uzima sifru iz vidljivog redka u tabeli
			pom = (String) row.elementAt(0);
			pom = pom.trim();
			
			//ispituje da li je sifra jednaka txtFieldu
			if (((String) elementValue).equalsIgnoreCase(pom)) {
				
				//u vidljivi vector stavlja vidljivi dio iz tabele
				sihgtedRow = row;
				statusChoose = true;
				return true;
			}
		}
			//ako ne pronadje u tabeli tu sifru javlja pogresku
			ra.showMessage("wrnclt11");
			return false;       
    }//CollCompChoose_txtChooseCode_FV
	
	
	
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
	
	 public void exit() {
        ra.exitScreen();
        
    }//exit
	 
	    
	public void selection() {
			 ra.setAttribute("CollateralDialogLDB","CollCompChoose_txtChooseCode",null);
		
			 String codeIn = null;
			 String scr_context = null;
			 if(!statusChoose){
		  			hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute("CollCompChooseLDB", "tblCollCompChoose");
		  			sihgtedRow = td.getSelectedRowData();
		  			codeIn = (String)sihgtedRow.elementAt(0);
		  			ra.setAttribute("CollCompChooseLDB","CollCompChoose_txtChooseCode",codeIn);
			 }
			 		codeIn = (String)sihgtedRow.elementAt(0);
			 		codeIn = codeIn.trim();
		    
		  	
		  	if(ra.getScreenContext().compareTo("scr_loan_comp")== 0){
		  			//provjera ima li podataka u tablicama
		  			if(!(codeIn.equalsIgnoreCase("1"))){
		  					if (isTableEmpty("tblCollateralDialogLoanComp")) {
		           		 		ra.showMessage("wrnclt12");
		           		 		return;
		  					}	  	    	  	    
		  			}
		  	}//scr_loan_comp
		  	if(ra.getScreenContext().compareTo("scr_coll_comp")== 0){
		  			//provjera ima li podataka u tablicama
		  			if(!(codeIn.equalsIgnoreCase("1"))){
		  					if (isTableEmpty("tblCollateralDialogCollComp")) {
		  						ra.showMessage("wrnctl13");
		  						return;
		  					}	  	    	  	    
		  			}
		  	}//scr_coll_comp
		  	
		  	ra.setAttribute("CollateralDialogLDB","CollCompChoose_txtChooseCode",codeIn);
		  	
		  	
		  	
		  	
		   if(ra.getScreenContext().compareTo("scr_loan_comp")== 0){
		   			if (codeIn.equalsIgnoreCase("1")) {
		   								//Dodavanje dijela plasmana
		   								scr_context ="scr_choose";
		   								ra.exitScreen();
		   								ra.loadScreen("LoanAccountList",scr_context);
		   			} else if (codeIn.equalsIgnoreCase("2")) {
		   								//Promjena dijela plasmana koji nije jos u bazi 
		   								scr_context ="scr_update";
		   								ra.exitScreen();
		   								ra.loadScreen("LoanComponentItem",scr_context);
		   			} else if (codeIn.equalsIgnoreCase("3")) {
		   								//Brisanje dijela plasmana koji nije jos u bazi
		   								deleteLinkedRows("CollateralDialog","tblCollateralDialogLoanComp");
		   								ra.exitScreen();
		   			}else if (codeIn.equalsIgnoreCase("4")) {
		   								//Prikaz detalja dijela plasmana
		   								scr_context ="scr_detail";
		   								ra.exitScreen();
		   								ra.loadScreen("LoanComponentItem",scr_context);
		   			}else{
		   								ra.showMessage("wrn625");
		   								return;
		   			} 
		   			
		   }//ctx scr_loan_comp
		   
		   if(ra.getScreenContext().compareTo("scr_coll_comp")== 0){
   					if (codeIn.equalsIgnoreCase("1")) {
   								//Dodavanje dijela kolaterala
   								scr_context ="scr_choose";
   								ra.exitScreen();
   								ra.loadScreen("CollHfPriorListRba",scr_context);
   					} else if (codeIn.equalsIgnoreCase("2")) {
   								//Promjena dijela kolaterala koji nije jos u bazi 
   								scr_context ="scr_update";
   								ra.exitScreen();
   								prepForUpdateCollComponentItem();
   								ra.loadScreen("CollComponentItem",scr_context);
   					} else if (codeIn.equalsIgnoreCase("3")) {
   								//Brisanje dijela kolaterala koji nije jos u bazi
   								deleteLinkedRows("CollateralDialog","tblCollateralDialogCollComp");
   								ra.exitScreen();
   					}else if (codeIn.equalsIgnoreCase("4")) {
   								//Prikaz detalja dijela plasmana
   								scr_context ="scr_detail";
   								ra.exitScreen();
   								ra.loadScreen("CollComponentItem",scr_context);
   					}else{
   								ra.showMessage("wrn625");
   								return;
   					} 
   			
		   }//ctx scr_coll_comp
		   
		   
		   
	}//selection
	
	
	
	
	
	private void prepForUpdateCollComponentItem(){
		
	}//prepForUpdateLoanComponentItem
	
	
	
	
	
	
	
	private void deleteLinkedRows(String argScreen, String argtableName){
			String ldbName = "CollateralDialogLDB";
		    String tableName = argtableName;
		    //tblCollateralDialogLoanComp
		    //tblCollateralDialogCollComp
		    
		    
		   
		    
		    if(tableName.compareTo("tblCollateralDialogLoanComp") == 0){
		    			System.out.println(" CollCompChoose deleteLinkedRows 1 " + tableName );
		    			int velLoanTable = 0;
		    			int velCollTable = 0;
		    			hr.vestigo.framework.common.TableData tdLoanDelete = null;
		    			hr.vestigo.framework.common.TableData tdCollDelete = null;
		    	
		    			if(ra.getAttribute("tblCollateralDialogLoanComp")==null){
		    				System.out.println(" CollCompChoose deleteLinkedRows 2 ");
		    			}else{
		    				System.out.println(" CollCompChoose deleteLinkedRows 3 ");
		    				tdLoanDelete = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
		    				tdCollDelete = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");
		    				if(tdLoanDelete != null){
		    					velLoanTable = tdLoanDelete.getData().size();
		    				}
		    				if(tdCollDelete != null){
		    					velCollTable = tdCollDelete.getData().size();
		    				}
		    				
						}
		    	
						    	
		    			
		    			if(velLoanTable == 0){
		    				System.out.println(" CollCompChoose deleteLinkedRows 4 Nije moguce brisati komponentu plasmana. Tablica na ekranu je prazna");
		    				ra.showMessage("wrnclt25");
							return;
						}else{
							if(velCollTable == 0){
								//CASE1 EXISTS ONLY LOAN COMPONENT
								System.out.println(" CollCompChoose deleteLinkedRows 4a ");
								Integer answer = null;
						        answer = (Integer) ra.showMessage("conclt1");
						        if (answer != null && answer.intValue() == 0){
						        	tdLoanDelete = null;
					    			tdCollDelete = null;
					    			answer = null;
						            return;
						        }else{
						        	try{
						        		System.out.println(" CollCompChoose deleteLinkedRows 5 ");
										tdLoanDelete.removeRow(tdLoanDelete.getSelectedRowUnique());
										ra.setAttribute("CollateralDialogLDB","tblCollateralDialogLoanComp",tdLoanDelete);
									}catch(hr.vestigo.framework.common.VestigoTableException ee){
										System.out.println(" CollCompChoose deleteLinkedRows 6 ");
										 answer = null;
									     tdLoanDelete = null;
							    		 tdCollDelete = null;
									}
						        }
						        
								tdLoanDelete = null;
				    			tdCollDelete = null;
				    			answer = null;
								return;
							}
							if(velCollTable > 0){
								//CASE2 EXISTS LOAN COMPONENT and COLL COMPONENT
								System.out.println(" CollCompChoose deleteLinkedRows 7");
								Integer answer = null;
						        answer = (Integer) ra.showMessage("conclt2");
						        if (answer != null && answer.intValue() == 0){
						        	tdLoanDelete = null;
					    			tdCollDelete = null;
					    			answer = null;
						            return;
						        }else{
						        	String loanCompNo = null;
						        	String collCompNo = null;
						        	java.util.Vector selectedRowLoanDelete = null;
						        	java.util.Vector selectedRowUniqueColl = null;
						        	java.util.Vector selectedRowUniqueLoan = null;
						        	boolean moze = true;
						        	try{
						        			selectedRowUniqueLoan =(java.util.Vector) tdLoanDelete.getSelectedRowUnique(); 
						        			
						        			selectedRowLoanDelete = tdLoanDelete.getSelectedRowData();
											loanCompNo = (String) selectedRowLoanDelete.elementAt(0);
											loanCompNo = loanCompNo.trim();
											System.out.println(" CollCompChoose deleteLinkedRows 8  Odabrana je komponenta plasmana za brisanje " + loanCompNo);
											System.out.println(" CollCompChoose deleteLinkedRows 9  Trazim komponentu kolaterala za brisanje ");
						        		 	
											java.util.Vector vidljiviDioTabliceCollDelete = tdCollDelete.getData();
											java.util.Vector nevidljiviDioTabliceCollDelete = (java.util.Vector) tdCollDelete.getUnique();
											
											for(int i = 0; ((i < vidljiviDioTabliceCollDelete.size()) && moze); i++){
														java.util.Vector vidljiviRedakCollDelete = (java.util.Vector) vidljiviDioTabliceCollDelete.elementAt(i);
														java.util.Vector nevidljiviRedakCollDelete = (java.util.Vector) nevidljiviDioTabliceCollDelete.elementAt(i);
														collCompNo = (String) vidljiviRedakCollDelete.elementAt(0);
														collCompNo = collCompNo.trim();
														
														if(loanCompNo.compareTo(collCompNo)== 0){
															System.out.println(" CollCompChoose deleteLinkedRows 10  Komponenta kolaterala za brisanje je  " + collCompNo);
															selectedRowUniqueColl = nevidljiviRedakCollDelete;
															moze = false;
															
														}
														vidljiviRedakCollDelete = null;
														nevidljiviRedakCollDelete = null;
											}
											System.out.println(" CollCompChoose deleteLinkedRows 11  Brise plasman  " );
											System.out.println(" CollCompChoose deleteLinkedRows 11a " + selectedRowUniqueLoan );
											tdLoanDelete.removeRow(selectedRowUniqueLoan);
											System.out.println(" CollCompChoose deleteLinkedRows 12  Obrisao plasman  " );
											
											System.out.println(" CollCompChoose deleteLinkedRows 13  Brise kolateral  " );
											System.out.println(" CollCompChoose deleteLinkedRows 13a " + selectedRowUniqueColl );
											tdCollDelete.removeRow(selectedRowUniqueColl);
											System.out.println(" CollCompChoose deleteLinkedRows 14  Obrisao kolateral  " );
										 
											
											ra.setAttribute("CollateralDialogLDB","tblCollateralDialogCollComp",tdCollDelete);
											ra.setAttribute("CollateralDialogLDB","tblCollateralDialogLoanComp",tdLoanDelete);
											
											
											
									}catch(hr.vestigo.framework.common.VestigoTableException ee){
										 System.out.println(" CollCompChoose deleteLinkedRows 15 ");
										 answer = null;
									     tdLoanDelete = null;
							    		 tdCollDelete = null;
							    		 selectedRowLoanDelete = null;
							    		 selectedRowUniqueColl = null;
							    		 selectedRowUniqueLoan = null;
									}
									selectedRowLoanDelete = null;
									selectedRowUniqueColl = null;
									selectedRowUniqueLoan = null;
									loanCompNo = null;
						        	collCompNo = null;
						        }
						        answer = null;
						        tdLoanDelete = null;
				    			tdCollDelete = null;
				    		}
		    				
		    			}//velLoanTable == 0
		    }//tblCollateralDialogLoanComp
		   
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    if(tableName.compareTo("tblCollateralDialogCollComp") == 0){
		    	
		    	System.out.println(" CollCompChoose deleteLinkedRows 17 " + tableName );
    			int velLoanTable = 0;
    			int velCollTable = 0;
    			String maxCollCompNo = null;
    			
    			hr.vestigo.framework.common.TableData tdLoanDelete = null;
    			hr.vestigo.framework.common.TableData tdCollDelete = null;
    	
    			java.util.Vector selectedRowUniqueColl = null;
	        	java.util.Vector selectedRowUniqueLoan = null;
	        	java.util.Vector selectedRowCollDelete = null;
    			
	        	String loanCompNoL = null;
	        	String collCompNoL = null;
	        	
    			if(ra.getAttribute("tblCollateralDialogCollComp")==null){
    				System.out.println(" CollCompChoose deleteLinkedRows 18 ");
    			}else{
    				System.out.println(" CollCompChoose deleteLinkedRows 19 ");
    				tdLoanDelete = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogLoanComp");
    				tdCollDelete = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollateralDialogCollComp");
    				if(tdLoanDelete != null){
    					velLoanTable = tdLoanDelete.getData().size();
    				}
    				if(tdCollDelete != null){
    					velCollTable = tdCollDelete.getData().size();
    				}
    				
				}
    			if(velCollTable == 0){
    				System.out.println(" CollCompChoose deleteLinkedRows 20 Nije moguce brisati komponentu kolaterala. Tablica na ekranu je prazna");
    				ra.showMessage("wrnclt29");
					return;
				}else{
							//GET NUMBER OF LAST COLL COMPONENT
							java.util.Vector vidljiviDioTabliceCollDelete = tdCollDelete.getData();
							for(int i = 0; i < vidljiviDioTabliceCollDelete.size(); i++){
								java.util.Vector vidljiviRedakCollDelete = (java.util.Vector) vidljiviDioTabliceCollDelete.elementAt(i);
								maxCollCompNo = (String) vidljiviRedakCollDelete.elementAt(0);
								maxCollCompNo = maxCollCompNo.trim();
								vidljiviRedakCollDelete = null;
							}
						
			        		selectedRowUniqueColl =(java.util.Vector) tdCollDelete.getSelectedRowUnique(); 
			        		//selectedRowUniqueLoan = (java.util.Vector) tdLoanDelete.getSelectedRowUnique(); 
			        		selectedRowCollDelete = tdLoanDelete.getSelectedRowData();
			        		
			        		collCompNoL = (String) selectedRowCollDelete.elementAt(0);
			        		collCompNoL = collCompNoL.trim();
							System.out.println(" CollCompChoose deleteLinkedRows 21  Odabrana je komponenta kolaterala za brisanje " + collCompNoL);
							
							
							
							
							
							if(collCompNoL.compareTo(maxCollCompNo) == 0 && (velLoanTable == 1)){
								
								
								//CASE1 DELETE LAST COLL COMPONENT
								System.out.println(" CollCompChoose deleteLinkedRows 22 ");
								Integer answer = null;
						        answer = (Integer) ra.showMessage("conclt3");
						        if (answer != null && answer.intValue() == 0){
						        	tdLoanDelete = null;
					    			tdCollDelete = null;
					    			maxCollCompNo = null;
					    			selectedRowUniqueColl = null;
					    			selectedRowUniqueLoan = null;
					    			selectedRowCollDelete = null;
					    			loanCompNoL = null;
					    			collCompNoL = null;
					    			answer = null;
						            return;
						        }else{
						        	try{
						        		System.out.println(" CollCompChoose deleteLinkedRows 23 ");
						        		tdCollDelete.removeRow(tdCollDelete.getSelectedRowUnique());
										ra.setAttribute("CollateralDialogLDB","tblCollateralDialogCollComp",tdCollDelete);
									}catch(hr.vestigo.framework.common.VestigoTableException ee){
										System.out.println(" CollCompChoose deleteLinkedRows 24 ");
										tdLoanDelete = null;
						    			tdCollDelete = null;
						    			maxCollCompNo = null;
						    			selectedRowUniqueColl = null;
						    			selectedRowUniqueLoan = null;
						    			selectedRowCollDelete = null;
						    			loanCompNoL = null;
						    			collCompNoL = null;
						    			answer = null;
									}
						        }
						        
						        tdLoanDelete = null;
				    			tdCollDelete = null;
				    			maxCollCompNo = null;
				    			selectedRowUniqueColl = null;
				    			selectedRowUniqueLoan = null;
				    			selectedRowCollDelete = null;
				    			loanCompNoL = null;
				    			collCompNoL = null;
				    			answer = null;
								return;
								
								
								
								
								
							}else{
								//CASE2 DELETE COLL COMPONENT WHICH IS NOT LAST ONE -> DELETE AND CORESPODENT  LOAN COMPONENT
									System.out.println(" CollCompChoose deleteLinkedRows 25  Odabrana je komponenta kolaterala za brisanje " + collCompNoL);
									boolean mozeL = true;
									java.util.Vector vidljiviDioTabliceLoanLDelete = null;
									java.util.Vector nevidljiviDioTabliceLoanLDelete = null;
									System.out.println(" CollCompChoose deleteLinkedRows 25a A Sada pitanje");
									Integer answer = null;
									answer = (Integer) ra.showMessage("conclt4");
									if (answer != null && answer.intValue() == 0){
												tdLoanDelete = null;
												tdCollDelete = null;
												vidljiviDioTabliceLoanLDelete = null;
												nevidljiviDioTabliceLoanLDelete = null;
												selectedRowUniqueLoan = null;
												selectedRowUniqueColl = null;
												loanCompNoL = null;
												collCompNoL = null;
												answer = null;
												return;
									}else{
										//else collateral answer begin
										
											try{
							        				System.out.println(" CollCompChoose deleteLinkedRows 26  Trazim komponentu plasmana za brisanje ");
													vidljiviDioTabliceLoanLDelete = tdLoanDelete.getData();
													nevidljiviDioTabliceLoanLDelete = (java.util.Vector) tdLoanDelete.getUnique();
													
													for(int i = 0; ((i < vidljiviDioTabliceLoanLDelete.size()) && mozeL); i++){
														java.util.Vector vidljiviRedakLoanLDelete = (java.util.Vector) vidljiviDioTabliceLoanLDelete.elementAt(i);
														java.util.Vector nevidljiviRedakLoanLDelete = (java.util.Vector) nevidljiviDioTabliceLoanLDelete.elementAt(i);
														loanCompNoL = (String) vidljiviRedakLoanLDelete.elementAt(0);
														loanCompNoL = loanCompNoL.trim();
													
														if(loanCompNoL.compareTo(collCompNoL)== 0){
															System.out.println(" CollCompChoose deleteLinkedRows 27  Komponenta plasmana za brisanje je  " + loanCompNoL);
															selectedRowUniqueLoan = nevidljiviRedakLoanLDelete;
															mozeL = false;
														
														}
														vidljiviRedakLoanLDelete = null;
														nevidljiviRedakLoanLDelete = null;
													}
													
													
													System.out.println(" CollCompChoose deleteLinkedRows 28  Brise kolateral  " );
													System.out.println(" CollCompChoose deleteLinkedRows 29 selectedRowUniqueColl " + selectedRowUniqueColl );
													tdCollDelete.removeRow(selectedRowUniqueColl);
													System.out.println(" CollCompChoose deleteLinkedRows 30  Obrisao kolateral  " );
													System.out.println(" CollCompChoose deleteLinkedRows 31  Brise povezani plasman  " );
													System.out.println(" CollCompChoose deleteLinkedRows 32 selectedRowUniqueLoan " + selectedRowUniqueLoan );
													tdLoanDelete.removeRow(selectedRowUniqueLoan);
													System.out.println(" CollCompChoose deleteLinkedRows 33 Obrisao povezani plasman  " );
													System.out.println(" CollCompChoose deleteLinkedRows 34 Postavi na ekran refreshane tablice bez obrisanog povezanog para komp.kolaterala, komp.plasmana  " );
													 
													
													ra.setAttribute("CollateralDialogLDB","tblCollateralDialogCollComp",tdCollDelete);
													ra.setAttribute("CollateralDialogLDB","tblCollateralDialogLoanComp",tdLoanDelete);
													
													
													
													
													
											}catch(hr.vestigo.framework.common.VestigoTableException ee){
													tdLoanDelete = null;
													tdCollDelete = null;
													vidljiviDioTabliceLoanLDelete = null;
													nevidljiviDioTabliceLoanLDelete = null;
													selectedRowUniqueLoan = null;
													selectedRowUniqueColl = null;
													loanCompNoL = null;
													collCompNoL = null;
													answer = null;
												
													System.out.println(" CollCompChoose deleteLinkedRows 35 Greska brisanja povezanog para komp.kolaterala, komp.plasmana ");
											 
											}
											tdLoanDelete = null;
											tdCollDelete = null;
											vidljiviDioTabliceLoanLDelete = null;
											nevidljiviDioTabliceLoanLDelete = null;
											selectedRowUniqueLoan = null;
											selectedRowUniqueColl = null;
											loanCompNoL = null;
											collCompNoL = null;
											answer = null;
											mozeL = false;
											
				        
										//else collateral answer end
									}//else answer
							//CASE2 DELETE COLL COMPONENT WHICH IS NOT LAST ONE
							}
				}//end else coll table is not empty
	    	
		    }//tblCollateralDialogCollComp
		    
		    
	}//deleteLinkedRows
	 
	
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

	

}
