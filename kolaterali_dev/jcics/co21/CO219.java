package hr.vestigo.modules.collateral.jcics.co21;

/**
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
  
import java.math.*;
import java.util.*;
import java.sql.Date;

import hr.vestigo.framework.common.CheckBoxData;
import hr.vestigo.framework.common.celltable.NamedRowData;
import hr.vestigo.framework.remote.transaction.*;
//import hr.vestigo.modules.f_payment.share.FPaymentStyle;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
 
      
public class CO219 extends JDBCScrollableRemoteTransaction {
	
	public static String cvsident =  "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co21/CO219.java,v 1.29 2017/03/24 09:55:37 hrazst Exp $";
	public DeclCO21 d = null;
	
	private ResultSet rs = null;
	private PreparedStatement prStmt = null;
	
	private Date dateFrom = Date.valueOf("1111-01-01");
	private Date dateTo = Date.valueOf("9999-12-31");
	
	public CO219 (DeclCO21 d) {
		this.d = d;
	}
            
 	public ResultSet executeScrollable(TransactionContext tc) throws Exception {
 		 		 
 		setLevel(d.kolateralqbemap.ActionListLevel); 		
        setFetchSize(tc.ACTION_LIST_FETCH_SIZE);
	 
		Connection con = tc.getConnection();
		con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		Vector values = new Vector();
		
		int position;

		//lista na kojoj se izvodi QBE		
		String collStatus = (String) d.kolateralqbemap.proc_status_QBE;
	
		//upit po uzorku imam na:
		// 0. referentskoj listi;
		//nema OJ, nema referenta, collateral_status = "0"

		//1. verifikacijskoj listi
		//nema OJ, collateral_staus = "1"
				
		//2. autorizacijskoj listi
		//svi uvjeti, collateral_status = "2"
				
		//3. arhivskoj listi
		//svi uvjeti, collateral_status = "3"
				
		//4. lista ponistenih
		//svi uvjeti, collateral_status = "4"
				
		//M. monitoring lista
		//svi uvjeti, collteral_status in (0,1,2)
				
		//Milka, 10.04.2007 - dodane jos 2 liste
		
		//I. lista neaktivnih kolaterala		
		//svi uvjeti, collateral_status = "N"
		
		//Milka, 19.11.2007 - dodana lista slobodnih kolaterala
		//svi uvjeti, collateral_status = "F"		

        //Milka, 30.05.2008 - dodano pretraživanje po svim listama
        //svi uvjeti, collateral_status in (0,1,2,3,4,N,F)	

        //Milka, 14.04.2011 - dodano pretraživanje po listi za KA		
        //svi uvjeti, collateral_status = "3", postoji nepotvrðen slog plasman-kolateral
        		
        //imam ustvari 5 razlicita slucaja obzirom na listu na kojoj se izvodi QBE
        //1. upit na referentskoj listi
        //2. upit na verifikacijskoj listi
        //3. upit na autorizacijskoj, arhivskoj i listi ponistenih i listi neaktivnih
        //4. upit na monitoring listi
        //5. upit po svim listama da se vidi na kojoj je kolateral
        //6. upit na listi za KA		
		
		String sql = "";
		String sql1 = "";
		
		String sqlColStatus = "";	
		
		String sqlOrder = "";
		String sqlCondition = "";
		String sqlCondition1 = "";
		String sqlUnion = " UNION ";
		
		String sqlVehi="";
		String sqlVehi1="";		

		String sqlDepo="";
		String sqlDepo1="";	
		
	    String sqlVrp="";
	    String sqlVrp1=""; 
	    
	    String sqlGar="";
	    String sqlGar1=""; 
	    
	    String sqlNekr="";
	    String sqlNekr1=""; 

 		System.out.println("POCEO CO219");			

////////////////////////////pocetak slaganja dinamickog upita za nekretnine
		if (collStatus.equalsIgnoreCase("N") || collStatus.equalsIgnoreCase("R") || collStatus.equalsIgnoreCase("X") ||
				collStatus.equalsIgnoreCase("S")) {
			sql = "SELECT distinct" 
				+ " ch.col_hea_id,"
				+ " ch.col_type_id,"
				+ " ch.workflow_indic,"
				+ " ch.collateral_status,"
				+ " ch.col_num,"
				+ " ch.real_est_nm_cur_id,"
				+ " cu.code_char,"
				+ " ch.real_est_nomi_valu,"
				+ " ch.basic_data_status,"
				+ " ch.mortgage_status,"
				+ " ch.cover_indic,"
				+ " ch.coll_data_status,"
				+ " ch.use_id,"
				+ " au.user_name,"
				+ " ch.col_cat_id,"
				+ " clc.screen_name,"
				+ " clc.code,"
				+ " ch.user_lock,"
				+ " ch.financial_flag,"
                + " clc.coll_deact_indic"
			+ " FROM coll_head ch"
				+ " INNER JOIN app_user au ON ch.use_id = au.use_id "
				+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id "
				+ " INNER JOIN coll_restate cres ON  ch.col_hea_id = cres.col_hea_id "
				+ " INNER JOIN currency cu ON  ch.real_est_nm_cur_id = cu.cur_id "
				+ " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id " 	
				+ " LEFT OUTER JOIN coll_hf_prior chp ON ch.col_hea_id = chp.hf_coll_head_id " 
				+ " LEFT OUTER JOIN loan_beneficiary lb ON (chp.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "  
				+ " LEFT OUTER JOIN cadastre_map pomap ON cres.real_est_cada_munc = pomap.cad_map_id " 
				+  " WHERE  ";		
	
			    tc.debug ( "1.- sql :" + sql);
							
				if (collStatus.compareTo("R")==0) {
					sqlColStatus = " ch.collateral_status in ('0','1','2')";
				} else if (collStatus.compareTo("N")==0){
					sqlColStatus = " ch.collateral_status = '3'";			
				} else if (collStatus.compareTo("X")==0){
					sqlColStatus = " ch.collateral_status = 'N'";			
				} else if (collStatus.compareTo("S")==0){
					sqlColStatus = " ch.collateral_status = 'F'";								
				} 		  
					
				sqlOrder = " ORDER BY"
                      + " col_type_id";
//				  			  + " workflow_indic desc, col_hea_id asc";		
											
				sqlCondition = "";
     
//				 organizacijska jedinica unosa 
				if(d.kolateralqbemap.org_uni_id_qbe != null){							
					sqlCondition = sqlCondition
								  + " AND"
								  + " ch.org_uni_id = ?";
				
					values.add(d.kolateralqbemap.org_uni_id_qbe);
			
				}		
				tc.debug ( "2.- sqlCondition :" + sqlCondition);		
//	 referent
				if(d.kolateralqbemap.use_id_qbe != null){							
					sqlCondition = sqlCondition
								  + " AND"
								  + " (ch.use_open_id = ? OR ch.use_id = ? OR ch.use_id_ver = ? OR ch.use_id_aut = ?)";
/*					sqlCondition = sqlCondition
					  + " AND"
					  + " ch.use_id = ?"; */				
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
			
				}				
				tc.debug ( "3.- sqlCondition :" + sqlCondition);			  
// partija kolaterala			
				if((d.kolateralqbemap.Kolateral_txtColNumQBE != null) && !d.kolateralqbemap.Kolateral_txtColNumQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtColNumQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
								   + " AND"
								   + " ch.col_num like ?";	
					}else{
						sqlCondition = sqlCondition
								   + " AND"
								   + " ch.col_num = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtColNumQBE);

				}
				tc.debug ( "4.- sqlCondition :" + sqlCondition);
//	 datum unosa kolaterala
			
				if(d.kolateralqbemap.Kolateral_txtDateFromQBE != null){										
/*					sqlCondition = sqlCondition
								  + " AND"
								  + " date(ch.opening_ts) >= ?"; */
					
					sqlCondition = sqlCondition
					  + " AND"
					  + " (date(ch.opening_ts) >= ? OR date(ch.user_lock) >= ? OR date(ch.verification_ts) >= ? OR date(ch.autorization_ts) >= ?)";				
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);

				}
				tc.debug ( "5.- sqlCondition :" + sqlCondition);
			
				if(d.kolateralqbemap.Kolateral_txtDateUntilQBE != null){										
/*					sqlCondition = sqlCondition
								  + " AND"
								  + " date(ch.opening_ts) <= ?"; */
					sqlCondition = sqlCondition
					  + " AND"
					  + " (date(ch.opening_ts) <= ? OR date(ch.user_lock) <= ? OR date(ch.verification_ts) <= ? OR date(ch.autorization_ts) <= ?)";								
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);

				}		
				tc.debug ( "6.- sqlCondition :" + sqlCondition);
			
//	 partija plasmana
			
				if((d.kolateralqbemap.Kolateral_txtAccNoQBE != null) && !d.kolateralqbemap.Kolateral_txtAccNoQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtAccNoQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
								   + " AND"
								   + " lb.acc_no LIKE ?";	
					}else{
						sqlCondition = sqlCondition
								   + " AND"
								   + " lb.acc_no = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtAccNoQBE);

				}		
				tc.debug ( "7.- sqlCondition :" + sqlCondition);
//	 broj zahtjeva		
			
				if((d.kolateralqbemap.Kolateral_txtNoRqQBE != null) && !d.kolateralqbemap.Kolateral_txtNoRqQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtNoRqQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
								   + " AND"
								   + " lb.request_no like ?";	
					}else{
						sqlCondition = sqlCondition
								   + " AND"
								   + " lb.request_no = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtNoRqQBE);

				}				
				tc.debug ( "8.- sqlCondition :" + sqlCondition);
				
// broj ugovora

                if((d.kolateralqbemap.ContractNo_txtQBE != null) && !d.kolateralqbemap.ContractNo_txtQBE.equals("")){
                    if(d.kolateralqbemap.Kolateral_txtNoRqQBE.trim().indexOf("*") != -1){
                        sqlCondition = sqlCondition
                                   + " AND"
                                   + " lb.acc_no_old like ?";   
                    }else{
                        sqlCondition = sqlCondition
                                   + " AND"
                                   + " lb.acc_no_old = ?";
                    }   
                    values.add(d.kolateralqbemap.ContractNo_txtQBE);

                }               
                tc.debug ( "8a.- sqlCondition :" + sqlCondition);
				
//	 vlasnik plasmana
			
				if(d.kolateralqbemap.cus_id_qbe != null){							
					sqlCondition = sqlCondition
								  + " AND"
								  + " lb.cus_id = ?";
				
					values.add(d.kolateralqbemap.cus_id_qbe);

				}
				tc.debug ( "9.- sqlCondition :" + sqlCondition);

	


// samo za nekretnine
				
//RealEstate_OwnerCUS_ID - vlasnik kolaterala	
				if (d.kolateralqbemap.RealEstate_OwnerCUS_ID != null) {
					sqlCondition = sqlCondition
					  + " AND"
					  + " cown.cus_id = ?";
	
					values.add(d.kolateralqbemap.RealEstate_OwnerCUS_ID);					
				} 
				tc.debug ( "10N.- sqlCondition :" + sqlCondition);
  
//RealEstate_txtQbeRealEstLandRegn - broj ZKU	
				if((d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn != null) && !d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn.equals("")){
					if(d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
						+ " AND"
						+ " cres.real_est_land_regn like ?";	
					}else{
						sqlCondition = sqlCondition
						+ " AND"
						+ " cres.real_est_land_regn = ?";
					}	
					values.add(d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn);

				}				
				tc.debug ( "11N.- sqlCondition :" + sqlCondition);				
			
//RealEstate_txtQbeLandSub - podulozak 
				if((d.kolateralqbemap.RealEstate_txtQbeLandSub != null) && !d.kolateralqbemap.RealEstate_txtQbeLandSub.equals("")){
					if(d.kolateralqbemap.RealEstate_txtQbeLandSub.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
						+ " AND"
						+ " cres.real_est_land_sub like ?";	
					}else{
						sqlCondition = sqlCondition
						+ " AND"
						+ " cres.real_est_land_sub = ?";
					}	
					values.add(d.kolateralqbemap.RealEstate_txtQbeLandSub);

				}				
				tc.debug ( "12N.- sqlCondition :" + sqlCondition);
				
//RealEstate_txtCoown - suvlasnicki udio
				if((d.kolateralqbemap.RealEstate_txtCoown != null) && !d.kolateralqbemap.RealEstate_txtCoown.equals("")){
					if(d.kolateralqbemap.RealEstate_txtCoown.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
						+ " AND"
						+ " cres.coown like ?";	
					}else{
						sqlCondition = sqlCondition
						+ " AND"
						+ " cres.coown = ?";
					}	
					values.add(d.kolateralqbemap.RealEstate_txtCoown);

				}	
				tc.debug ( "13N.- sqlCondition :" + sqlCondition);
				
//RealEstate_QBE_CADA_MUNC - katastarska opcina				
				if (d.kolateralqbemap.RealEstate_QBE_CADA_MUNC != null) {
						sqlCondition = sqlCondition
						+ " AND"
						+ " cres.real_est_cada_munc =  ?";	
	
					values.add(d.kolateralqbemap.RealEstate_QBE_CADA_MUNC);
				}												
				tc.debug ( "14N.- sqlCondition :" + sqlCondition);
				
				sql = 	sql + sqlColStatus + sqlCondition + sqlOrder;

				tc.debug ( "10.- sql" + sql);
				tc.debug ("VALUES: " + values);	
//////////////////////////////////////////////////////////////////////	kraj slaganja za nekretnine		
		}else if (collStatus.equalsIgnoreCase("O") || collStatus.equalsIgnoreCase("D")) {
////////////////////////////pocetak slaganja dinamickog upita za oslobodjene i deaktivirane kolaterale

			sql = "SELECT" 
				+ " ch.col_hea_id,"   
				+ " ch.col_type_id,"
				+ " ch.workflow_indic,"
				+ " ch.collateral_status," 
				+ " ch.col_num,"
				+ " ch.real_est_nm_cur_id,"
				+ " cu.code_char,"
				+ " ch.real_est_nomi_valu,"
				+ " ch.basic_data_status,"
				+ " ch.mortgage_status,"
				+ " ch.cover_indic,"
				+ " ch.coll_data_status,"
				+ " ch.use_id,"
				+ " au.user_name,"
				+ " ch.col_cat_id,"
				+ " clc.screen_name,"
				+ " clc.code,"
				+ " ch.user_lock,"
                + " ch.financial_flag,"
                + " clc.coll_deact_indic"
		+ " FROM coll_head ch"
				+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
				+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
                + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id " 
				+ " INNER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
				+ " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"
				+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
		+ " WHERE";

			
			sql1 = "SELECT" 
				+ " ch.col_hea_id,"
				+ " ch.col_type_id,"
				+ " ch.workflow_indic,"
				+ " ch.collateral_status," 
				+ " ch.col_num,"
				+ " ch.real_est_nm_cur_id,"
				+ " cu.code_char,"
				+ " ch.real_est_nomi_valu,"
				+ " ch.basic_data_status,"
				+ " ch.mortgage_status,"
				+ " ch.cover_indic,"
				+ " ch.coll_data_status,"
				+ " ch.use_id,"
				+ " au.user_name,"
				+ " ch.col_cat_id,"
				+ " clc.screen_name,"
				+ " clc.code,"
				+ " ch.user_lock,"
                + " ch.financial_flag,"
                + " clc.coll_deact_indic"
			+ " FROM coll_head ch"
				+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
				+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
				+ " INNER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
                + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id " 
				+ " INNER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
				+ " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"							
				+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
			+ " WHERE";			
			
			
			if (collStatus.compareTo("O")==0) {
				sqlColStatus = " ch.collateral_status = 'F'";				
			} else if (collStatus.compareTo("D")==0){
				sqlColStatus = " ch.collateral_status = 'N'";
			}

			sqlOrder = " ORDER BY"
                + " col_type_id";
//					+ " workflow_indic desc, col_hea_id asc";		
	
			sqlCondition = "";
			
			sqlCondition1 = "";

			sqlUnion = " UNION ";
			
			tc.debug ( "1od.- sql :" + sql);		

// kategorija kolaterala
			
			if(d.kolateralqbemap.col_cat_id_qbe != null){							
				sqlCondition = sqlCondition
							  + " AND"
							  + " ch.col_cat_id = ?";
			
				values.add(d.kolateralqbemap.col_cat_id_qbe);
		
			}	
			tc.debug ( "2od.- sqlCondition :" + sqlCondition);				
			
// period oslobadjanja/deaktivacije			

			if(d.kolateralqbemap.Kolateral_txtDateFromQBE != null){										
				sqlCondition = sqlCondition
				+ " AND"
				+ " date(ch.user_lock) >= ?";				
				values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
			}
			tc.debug ( "3od.- sqlCondition :" + sqlCondition);

			if(d.kolateralqbemap.Kolateral_txtDateUntilQBE != null){										
				sqlCondition = sqlCondition
				  + " AND"
				  + " date(ch.user_lock) <= ?";

				values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
			}		
			tc.debug ( "4od.- sqlCondition :" + sqlCondition);
			
// vrsta korisnika plasmana: fizièka ili pravna osoba
		
			if((d.kolateralqbemap.KolQBE_txtClientType != null) && !d.kolateralqbemap.KolQBE_txtClientType.equals("")){
				if(d.kolateralqbemap.KolQBE_txtClientType.trim().indexOf("F") != -1){
					sqlCondition = sqlCondition
							   + " AND"
							   + " cccc.basel_cus_type in ('1','36')";	
				}else{
					sqlCondition = sqlCondition
							   + " AND"
							   + " cccc.basel_cus_type not in ('1','36')";
				}	
//				values.add(d.kolateralqbemap.KolQBE_txtClientType);

			}
			tc.debug ( "5od.- sqlCondition :" + sqlCondition);		
			
//			 jos jednom isti uvjeti zbog UNION			

// kategorija kolaterala
			
			if(d.kolateralqbemap.col_cat_id_qbe != null){							
				sqlCondition1 = sqlCondition1
							  + " AND"
							  + " ch.col_cat_id = ?";
			
				values.add(d.kolateralqbemap.col_cat_id_qbe);
		
			}	
			tc.debug ( "6od.- sqlCondition1 :" + sqlCondition1);				
			
//			 period oslobadjanja/deaktivacije			

			if(d.kolateralqbemap.Kolateral_txtDateFromQBE != null){										
				sqlCondition1 = sqlCondition1
				+ " AND"
				+ " date(ch.user_lock) >= ?";				
				values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
			}
			tc.debug ( "7od.- sqlCondition1 :" + sqlCondition1);

			if(d.kolateralqbemap.Kolateral_txtDateUntilQBE != null){										
				sqlCondition1 = sqlCondition1
				  + " AND"
				  + " date(ch.user_lock) <= ?";

				values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
			}		
			tc.debug ( "8od.- sqlCondition1 :" + sqlCondition1);
			
// korisnik plasmana fizièka ili pravna osoba
		
			if((d.kolateralqbemap.KolQBE_txtClientType != null) && !d.kolateralqbemap.KolQBE_txtClientType.equals("")){
				if(d.kolateralqbemap.KolQBE_txtClientType.trim().indexOf("F") != -1){
					sqlCondition1 = sqlCondition1
							   + " AND"
							   + " cccc.basel_cus_type in ('1','36')";	
				}else{
					sqlCondition1 = sqlCondition1
							   + " AND"
							   + " cccc.basel_cus_type not in ('1','36')";
				}	
//				values.add(d.kolateralqbemap.KolQBE_txtClientType);

			}
			tc.debug ( "9od.- sqlCondition1 :" + sqlCondition1);			
			
			sql  = sql + sqlColStatus + sqlCondition;
			sql1 = sql1 + sqlColStatus + sqlCondition1 + sqlOrder;			
			sql  = sql + sqlUnion + sql1;				
	 
			tc.debug ( "10od.- sql" + sql);
//	 		System.out.println("10. - sql : "+sql);
			tc.debug ("VALUES: " + values);		
			
////////////////////////////kraj slaganja dinamickog upita za oslobodjene i deaktivirane				

		}else {
////////////////////////////pocetak slaganja dinamickog upita za sve kolaterale	
			if (collStatus.equalsIgnoreCase("A") && 
					((d.kolateralqbemap.Kolateral_txtBrojPoliceQBE != null) && !d.kolateralqbemap.Kolateral_txtBrojPoliceQBE.equals(""))) {
//				 upit za pretrazivanje preko police osiguranja	
				sql = "SELECT" 
						+ " ch.col_hea_id,"   
						+ " ch.col_type_id,"
						+ " ch.workflow_indic,"
						+ " scv.sys_code_desc as collateral_status,"
						+ " ch.col_num,"
						+ " ch.real_est_nm_cur_id,"
						+ " cu.code_char,"
						+ " ch.real_est_nomi_valu,"
						+ " ch.basic_data_status,"
						+ " ch.mortgage_status,"
						+ " ch.cover_indic,"
						+ " ch.coll_data_status,"
						+ " ch.use_id,"
						+ " au.user_name,"
						+ " ch.col_cat_id,"
						+ " clc.screen_name,"
						+ " clc.code,"
						+ " ch.user_lock,"
		                + " ch.financial_flag,"
		                + " clc.coll_deact_indic"
						+ " FROM coll_head ch"
						+ " INNER JOIN insurance_policy ip ON ch.col_hea_id = ip.col_hea_id"
						+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
						+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
						+ " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
						+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
						+ " WHERE";	
				
				    
				sqlColStatus = " ch.collateral_status in ('0','1','2','3','4','N','F','6','7') AND scv.sys_cod_id = 'kol_status_tekst'";
				
				sqlOrder = " ORDER BY"
                    + " col_type_id";               
//		  			  + " workflow_indic desc, col_hea_id asc";		
						
	   
				sqlCondition = "";
				tc.debug ( "1.- sql :" + sql);		
//	broj police

				if((d.kolateralqbemap.Kolateral_txtBrojPoliceQBE != null) && !d.kolateralqbemap.Kolateral_txtBrojPoliceQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtBrojPoliceQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
					   + " AND"
					   + " ip.ip_code like ?";	
					}else{
						sqlCondition = sqlCondition
					   + " AND"
					   + " ip.ip_code = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtBrojPoliceQBE);

				}	
				tc.debug ( "2.- sqlCondition :" + sqlCondition);	
				sql = 	sql + sqlColStatus + sqlCondition + sqlOrder;

				tc.debug ( "10.- sql" + sql);
				tc.debug ("VALUES: " + values);							
				
			}else {
				if (collStatus.equalsIgnoreCase("K")) { 
// za listu KA				    
		            sql = "SELECT" 
		                + " ch.col_hea_id,"   
		                + " ch.col_type_id,"
		                + " ch.workflow_indic,"
		                + " ch.collateral_status," 
		                + " ch.col_num,"
		                + " ch.real_est_nm_cur_id,"
		                + " cu.code_char,"
		                + " ch.real_est_nomi_valu,"
		                + " ch.basic_data_status,"
		                + " ch.mortgage_status,"
		                + " ch.cover_indic,"
		                + " ch.coll_data_status,"
		                + " ch.use_id,"
		                + " au.user_name,"
		                + " ch.col_cat_id,"
		                + " clc.screen_name,"
		                + " clc.code,"
		                + " ch.user_lock,"
		                + " ch.financial_flag,"
		                + " clc.coll_deact_indic"
		            + " FROM coll_head ch"
		                + " INNER JOIN app_user au ON ch.use_id = au.use_id"
		                + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
		                + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id " 
		                + " INNER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.kred_admin_use_id is null and lb.status = 'A') "
		                + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"
		                + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
		            + " WHERE ch.col_cat_id not in (617223,625223) and cccc.basel_cus_type not in ('1','20') and ";
		               
// za vozila
		            
                    sqlVehi = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_vehicle veh ON ch.col_hea_id = veh.col_hea_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE cccc.basel_cus_type not in ('1','20') and ";
// za nekretnine
                    
                    sqlNekr = "SELECT distinct" 
                        + " ch.col_hea_id,"
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_restate cres ON  ch.col_hea_id = cres.col_hea_id "
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id "
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id "
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                        + " WHERE cccc.basel_cus_type not in ('1','20') and ";     
                    
                    
// za depozite  

                    sqlDepo = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_cashdep dep ON ch.col_hea_id = dep.col_hea_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE cccc.basel_cus_type not in ('1','20') and ";     
            
// za VRP
                    sqlVrp = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_vrp vrp ON ch.col_hea_id = vrp.col_hea_id"
                        + " INNER JOIN coll_in2 in2vrp ON vrp.col_in2_id = in2vrp.col_in2_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE cccc.basel_cus_type not in ('1','20') and ";                  
                    
// za garancije
                    sqlGar = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_guarantee gar ON ch.col_hea_id = gar.col_hea_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE";             
		            
                    tc.debug ( "1.- sql :" + sql);
                    
		            sql1 = "SELECT" 
		                + " ch.col_hea_id,"
		                + " ch.col_type_id,"
		                + " ch.workflow_indic,"
		                + " ch.collateral_status," 
		                + " ch.col_num,"
		                + " ch.real_est_nm_cur_id,"
		                + " cu.code_char,"
		                + " ch.real_est_nomi_valu,"
		                + " ch.basic_data_status,"
		                + " ch.mortgage_status,"
		                + " ch.cover_indic,"
		                + " ch.coll_data_status,"
		                + " ch.use_id,"
		                + " au.user_name,"
		                + " ch.col_cat_id,"
		                + " clc.screen_name,"
		                + " clc.code,"
		                + " ch.user_lock,"
		                + " ch.financial_flag,"
		                + " clc.coll_deact_indic"
		            + " FROM coll_head ch"
		                + " INNER JOIN app_user au ON ch.use_id = au.use_id"
		                + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
		                + " INNER JOIN coll_hf_prior g ON (ch.col_hea_id=g.hf_coll_head_id and g.status = 'A') "
		                + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id " 
		                + " INNER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.kred_admin_use_id is null and lb.status = 'A') "
		                + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"                            
		                + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
		            + " WHERE ch.col_cat_id not in (617223,625223) and cccc.basel_cus_type not in ('1','20') and ";       
		            
		         // za vozila            
                    sqlVehi1 = "SELECT" 
                        + " ch.col_hea_id,"
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_vehicle veh ON ch.col_hea_id = veh.col_hea_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " INNER JOIN coll_hf_prior g ON (ch.col_hea_id=g.hf_coll_head_id and g.status = 'A') "
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"                            
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE cccc.basel_cus_type not in ('1','20') and  ";         

                    
// za nekretnine
                    sqlNekr1 = "SELECT distinct" 
                        + " ch.col_hea_id,"
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_restate cres ON  ch.col_hea_id = cres.col_hea_id "
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " INNER JOIN coll_hf_prior g ON (ch.col_hea_id=g.hf_coll_head_id and g.status = 'A') "
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"                            
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                        + " LEFT OUTER JOIN cadastre_map pomap ON cres.real_est_cada_munc = pomap.cad_map_id " 
                        + " WHERE cccc.basel_cus_type not in ('1','20') and  ";                
                    
// za depozite            
            
                    sqlDepo1 = "SELECT" 
                        + " ch.col_hea_id,"
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_cashdep dep ON ch.col_hea_id = dep.col_hea_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " INNER JOIN coll_hf_prior g ON (ch.col_hea_id=g.hf_coll_head_id and g.status = 'A') "
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"                            
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE cccc.basel_cus_type not in ('1','20') and  ";      
            
// za VRP                   
                    sqlVrp1 = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_vrp vrp ON ch.col_hea_id = vrp.col_hea_id"
                        + " INNER JOIN coll_in2 in2vrp ON vrp.col_in2_id = in2vrp.col_in2_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " INNER JOIN coll_hf_prior g ON (ch.col_hea_id=g.hf_coll_head_id and g.status = 'A') "
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"                            
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE cccc.basel_cus_type not in ('1','20') and  ";         
 
// za garancije
                    sqlGar1 = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_guarantee gar ON ch.col_hea_id = gar.col_hea_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " INNER JOIN coll_hf_prior g ON (ch.col_hea_id=g.hf_coll_head_id and g.status = 'A') "
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.kred_admin_use_id is null and lb.status = 'A') "
                        + " INNER JOIN customer cccc ON lb.cus_id = cccc.cus_id"                            
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE cccc.basel_cus_type not in ('1','20') and  ";                                  
                    
                    
                    tc.debug ( "1.- sql1 :" + sql1);
		            
		            sqlColStatus = " ch.collateral_status = '3'";               
		            
		            sqlOrder = " ORDER BY"
		                + " col_type_id";			    
				} else if (collStatus.equalsIgnoreCase("A")) { 
// najopcenitije pretrazivanje (lista Pretrazivanje)							
						
						sql = "SELECT" 
								+ " ch.col_hea_id,"   
								+ " ch.col_type_id,"
								+ " ch.workflow_indic,"
								+ " scv.sys_code_desc as collateral_status,"
								+ " ch.col_num,"
								+ " ch.real_est_nm_cur_id,"
								+ " cu.code_char,"
								+ " ch.real_est_nomi_valu,"
								+ " ch.basic_data_status,"
								+ " ch.mortgage_status,"
								+ " ch.cover_indic,"
								+ " ch.coll_data_status,"
								+ " ch.use_id,"
								+ " au.user_name,"
								+ " ch.col_cat_id,"
								+ " clc.screen_name,"
								+ " clc.code,"
								+ " ch.user_lock,"
								+ " ch.financial_flag,"
                                + " ch.collateral_status as lista,"
                                + " clc.coll_deact_indic"
						+ " FROM coll_head ch"
								+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
								+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
		                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
								+ " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
								+ " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
								+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
						+ " WHERE";

//		za vozila
				
						sqlVehi = "SELECT" 
							+ " ch.col_hea_id,"   
							+ " ch.col_type_id,"
							+ " ch.workflow_indic,"
							+ " scv.sys_code_desc as collateral_status,"
							+ " ch.col_num,"
							+ " ch.real_est_nm_cur_id,"
							+ " cu.code_char,"
							+ " ch.real_est_nomi_valu,"
							+ " ch.basic_data_status,"
							+ " ch.mortgage_status,"
							+ " ch.cover_indic,"
							+ " ch.coll_data_status,"
							+ " ch.use_id,"
							+ " au.user_name,"
							+ " ch.col_cat_id,"
							+ " clc.screen_name,"
							+ " clc.code,"
							+ " ch.user_lock,"
							+ " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
						+ " FROM coll_head ch"
							+ " INNER JOIN coll_vehicle veh ON ch.col_hea_id = veh.col_hea_id"
							+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
		                    + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
							+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
							+ " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
							+ " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
							+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
						+ " WHERE";

// za nekretnine 
						
		                  sqlNekr = "SELECT distinct" 
                              + " ch.col_hea_id,"   
                              + " ch.col_type_id,"
                              + " ch.workflow_indic,"
                              + " scv.sys_code_desc as collateral_status,"
                              + " ch.col_num,"
                              + " ch.real_est_nm_cur_id,"
                              + " cu.code_char,"
                              + " ch.real_est_nomi_valu,"
                              + " ch.basic_data_status,"
                              + " ch.mortgage_status,"
                              + " ch.cover_indic,"
                              + " ch.coll_data_status,"
                              + " ch.use_id,"
                              + " au.user_name,"
                              + " ch.col_cat_id,"
                              + " clc.screen_name,"
                              + " clc.code,"
                              + " ch.user_lock,"
                              + " ch.financial_flag,"
                              + " ch.collateral_status as lista,"
                              + " clc.coll_deact_indic"
		                    + " FROM coll_head ch"
		                        + " INNER JOIN coll_restate cres ON  ch.col_hea_id = cres.col_hea_id "
	                            + " INNER JOIN app_user au ON ch.use_id = au.use_id"
	                            + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
	                            + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
	                            + " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
	                            + " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
	                            + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
		                        +  " WHERE  ";                          
						
//		za depozite

						sqlDepo = "SELECT" 
							+ " ch.col_hea_id,"   
							+ " ch.col_type_id,"
							+ " ch.workflow_indic,"
							+ " scv.sys_code_desc as collateral_status,"
							+ " ch.col_num,"
							+ " ch.real_est_nm_cur_id,"
							+ " cu.code_char,"
							+ " ch.real_est_nomi_valu,"
							+ " ch.basic_data_status,"
							+ " ch.mortgage_status,"
							+ " ch.cover_indic,"
							+ " ch.coll_data_status,"
							+ " ch.use_id,"
							+ " au.user_name,"
							+ " ch.col_cat_id,"
							+ " clc.screen_name,"
							+ " clc.code,"
							+ " ch.user_lock,"
							+ " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
						+ " FROM coll_head ch"
							+ " INNER JOIN coll_cashdep dep ON ch.col_hea_id = dep.col_hea_id"
							+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
		                    + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
							+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
							+ " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
							+ " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
							+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
						+ " WHERE";
				
// za VRP
                        sqlVrp = "SELECT" 
                            + " ch.col_hea_id,"   
                            + " ch.col_type_id,"
                            + " ch.workflow_indic,"
                            + " scv.sys_code_desc as collateral_status,"
                            + " ch.col_num,"
                            + " ch.real_est_nm_cur_id,"
                            + " cu.code_char,"
                            + " ch.real_est_nomi_valu,"
                            + " ch.basic_data_status,"
                            + " ch.mortgage_status,"
                            + " ch.cover_indic,"
                            + " ch.coll_data_status,"
                            + " ch.use_id,"
                            + " au.user_name,"
                            + " ch.col_cat_id,"
                            + " clc.screen_name,"
                            + " clc.code,"
                            + " ch.user_lock,"
                            + " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
                        + " FROM coll_head ch"
                            + " INNER JOIN coll_vrp vrp ON ch.col_hea_id = vrp.col_hea_id"
                            + " INNER JOIN coll_in2 in2vrp ON vrp.col_in2_id = in2vrp.col_in2_id"
                            + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                            + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                            + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                            + " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
                            + " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
                            + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                        + " WHERE";						
						
// za garancije
                        sqlGar = "SELECT" 
                            + " ch.col_hea_id,"   
                            + " ch.col_type_id,"
                            + " ch.workflow_indic,"
                            + " scv.sys_code_desc as collateral_status,"
                            + " ch.col_num,"
                            + " ch.real_est_nm_cur_id,"
                            + " cu.code_char,"
                            + " ch.real_est_nomi_valu,"
                            + " ch.basic_data_status,"
                            + " ch.mortgage_status,"
                            + " ch.cover_indic,"
                            + " ch.coll_data_status,"
                            + " ch.use_id,"
                            + " au.user_name,"
                            + " ch.col_cat_id,"
                            + " clc.screen_name,"
                            + " clc.code,"
                            + " ch.user_lock,"
                            + " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
                        + " FROM coll_head ch"
                            + " INNER JOIN coll_guarantee gar ON ch.col_hea_id = gar.col_hea_id"
                            + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                            + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                            + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                            + " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
                            + " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
                            + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                        + " WHERE";                     						
						
						sql1 = "SELECT" 
							+ " ch.col_hea_id,"
							+ " ch.col_type_id,"
							+ " ch.workflow_indic,"
							+ " scv.sys_code_desc as collateral_status,"
							+ " ch.col_num,"
							+ " ch.real_est_nm_cur_id,"
							+ " cu.code_char,"
							+ " ch.real_est_nomi_valu,"
							+ " ch.basic_data_status,"
							+ " ch.mortgage_status,"
							+ " ch.cover_indic,"
							+ " ch.coll_data_status,"
							+ " ch.use_id,"
							+ " au.user_name,"
							+ " ch.col_cat_id,"
							+ " clc.screen_name,"
							+ " clc.code,"
							+ " ch.user_lock,"
							+ " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
						+ " FROM coll_head ch"
							+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
							+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
							+ " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
		                    + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
							+ " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
							+ " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
							+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
						+ " WHERE";

// za vozila				
						sqlVehi1 = "SELECT" 
							+ " ch.col_hea_id,"
							+ " ch.col_type_id,"
							+ " ch.workflow_indic,"
							+ " scv.sys_code_desc as collateral_status,"
							+ " ch.col_num,"
							+ " ch.real_est_nm_cur_id,"
							+ " cu.code_char,"
							+ " ch.real_est_nomi_valu,"
							+ " ch.basic_data_status,"
							+ " ch.mortgage_status,"
							+ " ch.cover_indic,"
							+ " ch.coll_data_status,"
							+ " ch.use_id,"
							+ " au.user_name,"
							+ " ch.col_cat_id,"
							+ " clc.screen_name,"
							+ " clc.code,"
							+ " ch.user_lock,"
							+ " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
						+ " FROM coll_head ch"
							+ " INNER JOIN coll_vehicle veh ON ch.col_hea_id = veh.col_hea_id"
							+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
		                    + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
							+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
							+ " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
							+ " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
							+ " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
							+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
						+ " WHERE";			

// za nekretnine
	                    sqlNekr1 = "SELECT distinct" 
                            + " ch.col_hea_id,"
                            + " ch.col_type_id,"
                            + " ch.workflow_indic,"
                            + " scv.sys_code_desc as collateral_status,"
                            + " ch.col_num,"
                            + " ch.real_est_nm_cur_id,"
                            + " cu.code_char,"
                            + " ch.real_est_nomi_valu,"
                            + " ch.basic_data_status,"
                            + " ch.mortgage_status,"
                            + " ch.cover_indic,"
                            + " ch.coll_data_status,"
                            + " ch.use_id,"
                            + " au.user_name,"
                            + " ch.col_cat_id,"
                            + " clc.screen_name,"
                            + " clc.code,"
                            + " ch.user_lock,"
                            + " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
	                    + " FROM coll_head ch"
	                        + " INNER JOIN coll_restate cres ON  ch.col_hea_id = cres.col_hea_id "
	                        + " INNER JOIN app_user au ON ch.use_id = au.use_id "
	                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "    
	                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id "
	                        + " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
	                        + " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id = g.hf_coll_head_id " 
	                        + " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "  
	                        + " LEFT OUTER JOIN currency cu ON  ch.real_est_nm_cur_id = cu.cur_id "
	                        + " LEFT OUTER JOIN cadastre_map pomap ON cres.real_est_cada_munc = pomap.cad_map_id " 
	                        +  " WHERE  ";                          
						
//		za depozite			
				
						sqlDepo1 = "SELECT" 
							+ " ch.col_hea_id,"
							+ " ch.col_type_id,"
							+ " ch.workflow_indic,"
							+ " scv.sys_code_desc as collateral_status,"
							+ " ch.col_num,"
							+ " ch.real_est_nm_cur_id,"
							+ " cu.code_char,"
							+ " ch.real_est_nomi_valu,"
							+ " ch.basic_data_status,"
							+ " ch.mortgage_status,"
							+ " ch.cover_indic,"
							+ " ch.coll_data_status,"
							+ " ch.use_id,"
							+ " au.user_name,"
							+ " ch.col_cat_id,"
							+ " clc.screen_name,"
							+ " clc.code,"
							+ " ch.user_lock,"
							+ " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
						+ " FROM coll_head ch"
							+ " INNER JOIN coll_cashdep dep ON ch.col_hea_id = dep.col_hea_id"
							+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
		                    + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
							+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
							+ " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
							+ " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
							+ " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
							+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
						+ " WHERE";						
			  	
// za VRP
                        sqlVrp1 = "SELECT" 
                            + " ch.col_hea_id,"   
                            + " ch.col_type_id,"
                            + " ch.workflow_indic,"
                            + " scv.sys_code_desc as collateral_status,"
                            + " ch.col_num,"
                            + " ch.real_est_nm_cur_id,"
                            + " cu.code_char,"
                            + " ch.real_est_nomi_valu,"
                            + " ch.basic_data_status,"
                            + " ch.mortgage_status,"
                            + " ch.cover_indic,"
                            + " ch.coll_data_status,"
                            + " ch.use_id,"
                            + " au.user_name,"
                            + " ch.col_cat_id,"
                            + " clc.screen_name,"
                            + " clc.code,"
                            + " ch.user_lock,"
                            + " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
                        + " FROM coll_head ch"
                            + " INNER JOIN coll_vrp vrp ON ch.col_hea_id = vrp.col_hea_id"
                            + " INNER JOIN coll_in2 in2vrp ON vrp.col_in2_id = in2vrp.col_in2_id"
                            + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                            + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                            + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                            + " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
                            + " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
                            + " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
                            + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                        + " WHERE";                     
						
// za garancije
                        sqlGar1 = "SELECT" 
                            + " ch.col_hea_id,"   
                            + " ch.col_type_id,"
                            + " ch.workflow_indic,"
                            + " scv.sys_code_desc as collateral_status,"
                            + " ch.col_num,"
                            + " ch.real_est_nm_cur_id,"
                            + " cu.code_char,"
                            + " ch.real_est_nomi_valu,"
                            + " ch.basic_data_status,"
                            + " ch.mortgage_status,"
                            + " ch.cover_indic,"
                            + " ch.coll_data_status,"
                            + " ch.use_id,"
                            + " au.user_name,"
                            + " ch.col_cat_id,"
                            + " clc.screen_name,"
                            + " clc.code,"
                            + " ch.user_lock,"
                            + " ch.financial_flag,"
                            + " ch.collateral_status as lista,"
                            + " clc.coll_deact_indic"
                        + " FROM coll_head ch"
                            + " INNER JOIN coll_guarantee gar ON ch.col_hea_id = gar.col_hea_id"
                            + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                            + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                            + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                            + " INNER JOIN system_code_value scv ON ch.collateral_status = scv.sys_code_value"
                            + " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
                            + " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
                            + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                        + " WHERE";                     
                        
                        
						tc.debug ( "1.- sql :" + sql);
							
						sqlColStatus = " ch.collateral_status in ('0','1','2','3','4','N','F','6','7') AND scv.sys_cod_id = 'kol_status_tekst'";
                        sqlOrder = " ORDER BY"
                            + " lista, col_type_id";
			  
				
				} else {
// sve ostale liste			
					sql = "SELECT" 
							+ " ch.col_hea_id,"   
							+ " ch.col_type_id,"
							+ " ch.workflow_indic,"
							+ " ch.collateral_status,"
							+ " ch.col_num,"
							+ " ch.real_est_nm_cur_id,"
							+ " cu.code_char,"
							+ " ch.real_est_nomi_valu,"
							+ " ch.basic_data_status,"
							+ " ch.mortgage_status,"
							+ " ch.cover_indic,"
							+ " ch.coll_data_status,"
							+ " ch.use_id,"
							+ " au.user_name,"
							+ " ch.col_cat_id,"
							+ " clc.screen_name,"
							+ " clc.code,"
							+ " ch.user_lock,"
							+ " ch.financial_flag,"
							+ " clc.coll_deact_indic"
					+ " FROM coll_head ch"
							+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
							+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
		                    + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
							+ " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
							+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
					+ " WHERE";

// za vozila
		  	
					sqlVehi = "SELECT" 
						+ " ch.col_hea_id,"   
						+ " ch.col_type_id,"
						+ " ch.workflow_indic,"
						+ " ch.collateral_status,"
						+ " ch.col_num,"
						+ " ch.real_est_nm_cur_id,"
						+ " cu.code_char,"
						+ " ch.real_est_nomi_valu,"
						+ " ch.basic_data_status,"
						+ " ch.mortgage_status,"
						+ " ch.cover_indic,"
						+ " ch.coll_data_status,"
						+ " ch.use_id,"
						+ " au.user_name,"
						+ " ch.col_cat_id,"
						+ " clc.screen_name,"
						+ " clc.code,"
						+ " ch.user_lock,"
						+ " ch.financial_flag,"
						+ " clc.coll_deact_indic"
					+ " FROM coll_head ch"
						+ " INNER JOIN coll_vehicle veh ON ch.col_hea_id = veh.col_hea_id"
						+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
						+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
						+ " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
						+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
					+ " WHERE";
// za nekretnine
					
		            sqlNekr = "SELECT distinct" 
		                + " ch.col_hea_id,"
		                + " ch.col_type_id,"
		                + " ch.workflow_indic,"
		                + " ch.collateral_status,"
		                + " ch.col_num,"
		                + " ch.real_est_nm_cur_id,"
		                + " cu.code_char,"
		                + " ch.real_est_nomi_valu,"
		                + " ch.basic_data_status,"
		                + " ch.mortgage_status,"
		                + " ch.cover_indic,"
		                + " ch.coll_data_status,"
		                + " ch.use_id,"
		                + " au.user_name,"
		                + " ch.col_cat_id,"
		                + " clc.screen_name,"
		                + " clc.code,"
		                + " ch.user_lock,"
		                + " ch.financial_flag,"
		                + " clc.coll_deact_indic"
		            + " FROM coll_head ch"
                        + " INNER JOIN coll_restate cres ON  ch.col_hea_id = cres.col_hea_id "
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
		                + " WHERE  ";  		
					
					
// za depozite

					sqlDepo = "SELECT" 
						+ " ch.col_hea_id,"   
						+ " ch.col_type_id,"
						+ " ch.workflow_indic,"
						+ " ch.collateral_status,"
						+ " ch.col_num,"
						+ " ch.real_est_nm_cur_id,"
						+ " cu.code_char,"
						+ " ch.real_est_nomi_valu,"
						+ " ch.basic_data_status,"
						+ " ch.mortgage_status,"
						+ " ch.cover_indic,"
						+ " ch.coll_data_status,"
						+ " ch.use_id,"
						+ " au.user_name,"
						+ " ch.col_cat_id,"
						+ " clc.screen_name,"
						+ " clc.code,"
						+ " ch.user_lock,"
						+ " ch.financial_flag,"
						+ " clc.coll_deact_indic"
					+ " FROM coll_head ch"
						+ " INNER JOIN coll_cashdep dep ON ch.col_hea_id = dep.col_hea_id"
						+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
						+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
						+ " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
						+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
					+ " WHERE";
			
// za VRP
                    sqlVrp = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_vrp vrp ON ch.col_hea_id = vrp.col_hea_id"
                        + " INNER JOIN coll_in2 in2vrp ON vrp.col_in2_id = in2vrp.col_in2_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE";                     
					
// za garancije
                    sqlGar = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_guarantee gar ON ch.col_hea_id = gar.col_hea_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN loan_beneficiary lb ON (ch.col_hea_id = lb.col_hea_id and lb.status = 'A') "
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE";             
                    
                    
					sql1 = "SELECT" 
								+ " ch.col_hea_id,"
								+ " ch.col_type_id,"
								+ " ch.workflow_indic,"
								+ " ch.collateral_status,"
								+ " ch.col_num,"
								+ " ch.real_est_nm_cur_id,"
								+ " cu.code_char,"
								+ " ch.real_est_nomi_valu,"
								+ " ch.basic_data_status,"
								+ " ch.mortgage_status,"
								+ " ch.cover_indic,"
								+ " ch.coll_data_status,"
								+ " ch.use_id,"
								+ " au.user_name,"
								+ " ch.col_cat_id,"
								+ " clc.screen_name,"
								+ " clc.code,"
								+ " ch.user_lock,"
								+ " ch.financial_flag,"
								+ " clc.coll_deact_indic"
					+ " FROM coll_head ch"
								+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
								+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
								+ " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
	                            + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
								+ " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
								+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
					+ " WHERE";

// za vozila			
					sqlVehi1 = "SELECT" 
						+ " ch.col_hea_id,"
						+ " ch.col_type_id,"
						+ " ch.workflow_indic,"
						+ " ch.collateral_status,"
						+ " ch.col_num,"
						+ " ch.real_est_nm_cur_id,"
						+ " cu.code_char,"
						+ " ch.real_est_nomi_valu,"
						+ " ch.basic_data_status,"
						+ " ch.mortgage_status,"
						+ " ch.cover_indic,"
						+ " ch.coll_data_status,"
						+ " ch.use_id,"
						+ " au.user_name,"
						+ " ch.col_cat_id,"
						+ " clc.screen_name,"
						+ " clc.code,"
						+ " ch.user_lock,"
						+ " ch.financial_flag,"
						+ " clc.coll_deact_indic"
					+ " FROM coll_head ch"
						+ " INNER JOIN coll_vehicle veh ON ch.col_hea_id = veh.col_hea_id"
						+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
						+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
						+ " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
						+ " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
						+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
					+ " WHERE";			

					
// za nekretnine
		            sqlNekr1 = "SELECT distinct" 
		                + " ch.col_hea_id,"
		                + " ch.col_type_id,"
		                + " ch.workflow_indic,"
		                + " ch.collateral_status,"
		                + " ch.col_num,"
		                + " ch.real_est_nm_cur_id,"
		                + " cu.code_char,"
		                + " ch.real_est_nomi_valu,"
		                + " ch.basic_data_status,"
		                + " ch.mortgage_status,"
		                + " ch.cover_indic,"
		                + " ch.coll_data_status,"
		                + " ch.use_id,"
		                + " au.user_name,"
		                + " ch.col_cat_id,"
		                + " clc.screen_name,"
		                + " clc.code,"
		                + " ch.user_lock,"
		                + " ch.financial_flag,"
		                + " clc.coll_deact_indic"
		            + " FROM coll_head ch"
                        + " INNER JOIN coll_restate cres ON  ch.col_hea_id = cres.col_hea_id "
		                + " INNER JOIN app_user au ON ch.use_id = au.use_id "
	                    + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "    
		                + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id "
                        + " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id = g.hf_coll_head_id " 
                        + " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "  
		                + " LEFT OUTER JOIN currency cu ON  ch.real_est_nm_cur_id = cu.cur_id "
		                + " LEFT OUTER JOIN cadastre_map pomap ON cres.real_est_cada_munc = pomap.cad_map_id " 
		                +  " WHERE  ";      					
					
// za depozite			  
			
					sqlDepo1 = "SELECT" 
						+ " ch.col_hea_id,"
						+ " ch.col_type_id,"
						+ " ch.workflow_indic,"
						+ " ch.collateral_status,"
						+ " ch.col_num,"
						+ " ch.real_est_nm_cur_id,"
						+ " cu.code_char,"
						+ " ch.real_est_nomi_valu,"
						+ " ch.basic_data_status,"
						+ " ch.mortgage_status,"
						+ " ch.cover_indic,"
						+ " ch.coll_data_status,"
						+ " ch.use_id,"
						+ " au.user_name,"
						+ " ch.col_cat_id,"
						+ " clc.screen_name,"
						+ " clc.code,"
						+ " ch.user_lock,"
						+ " ch.financial_flag,"
						+ " clc.coll_deact_indic"
					+ " FROM coll_head ch"
						+ " INNER JOIN coll_cashdep dep ON ch.col_hea_id = dep.col_hea_id"
						+ " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
						+ " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id"	
						+ " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
						+ " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
						+ " LEFT OUTER JOIN	currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
					+ " WHERE";						
		  	
// za VRP					
                    sqlVrp1 = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_vrp vrp ON ch.col_hea_id = vrp.col_hea_id"
                        + " INNER JOIN coll_in2 in2vrp ON vrp.col_in2_id = in2vrp.col_in2_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
                        + " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE";                  
 
// za garancije
                    sqlGar1 = "SELECT" 
                        + " ch.col_hea_id,"   
                        + " ch.col_type_id,"
                        + " ch.workflow_indic,"
                        + " ch.collateral_status,"
                        + " ch.col_num,"
                        + " ch.real_est_nm_cur_id,"
                        + " cu.code_char,"
                        + " ch.real_est_nomi_valu,"
                        + " ch.basic_data_status,"
                        + " ch.mortgage_status,"
                        + " ch.cover_indic,"
                        + " ch.coll_data_status,"
                        + " ch.use_id,"
                        + " au.user_name,"
                        + " ch.col_cat_id,"
                        + " clc.screen_name,"
                        + " clc.code,"
                        + " ch.user_lock,"
                        + " ch.financial_flag,"
                        + " clc.coll_deact_indic"
                    + " FROM coll_head ch"
                        + " INNER JOIN coll_guarantee gar ON ch.col_hea_id = gar.col_hea_id"
                        + " INNER JOIN app_user au ON ch.use_id = au.use_id"
                        + " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id "
                        + " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id" 
                        + " LEFT OUTER JOIN coll_hf_prior g ON ch.col_hea_id=g.hf_coll_head_id"
                        + " LEFT OUTER JOIN loan_beneficiary lb ON (g.coll_hf_prior_id = lb.coll_hf_prior_id and lb.status = 'A') "
                        + " LEFT OUTER JOIN currency cu ON ch.real_est_nm_cur_id = cu.cur_id"
                    + " WHERE";                                      
                    
                    
					tc.debug ( "1.- sql1 :" + sql1);
		
//			String sqlColStatus = "";			
						
					if (collStatus.compareTo("M")==0) {
						sqlColStatus = " ch.collateral_status in ('0','1','2')";
					} else if (collStatus.compareTo("0")==0){
						sqlColStatus = " ch.collateral_status = '0'";
					} else if (collStatus.compareTo("1")==0){
						sqlColStatus = " ch.collateral_status = '1'";
					} else if (collStatus.compareTo("2")==0){
						sqlColStatus = " ch.collateral_status = '2'";
					} else if (collStatus.compareTo("3")==0){
						sqlColStatus = " ch.collateral_status = '3'";			
					} else if (collStatus.compareTo("4")==0){
						sqlColStatus = " ch.collateral_status = '4'";		
					} else if (collStatus.compareTo("I")==0){
						sqlColStatus = " ch.collateral_status = 'N'";	
					} else if (collStatus.compareTo("F")==0){
						sqlColStatus = " ch.collateral_status = 'F'";						
					} else if (collStatus.compareTo("A")==0) {
						sqlColStatus = " ch.collateral_status in ('0','1','2','3','4','N','F','6','7')";				
					} else if (collStatus.compareTo("6")==0) {
                        sqlColStatus = " ch.collateral_status = '6'";                
                    } else if (collStatus.compareTo("7")==0) {
                        sqlColStatus = " ch.collateral_status = '7'";                
                    }
                    sqlOrder = " ORDER BY"
                        + " col_type_id";                    
//                          + " workflow_indic desc, col_hea_id asc";                     
				}  
//				sqlOrder = " ORDER BY"
//				  			  + " workflow_indic desc, col_hea_id asc";		
								
			
				sqlCondition = "";
				
				sqlCondition1 = "";
			
	
				sqlUnion = " UNION ";
	    
		
// organizacijska jedinica unosa 
				if(d.kolateralqbemap.org_uni_id_qbe != null){							
					sqlCondition = sqlCondition
								  + " AND"
								  + " ch.org_uni_id = ?";
				
					values.add(d.kolateralqbemap.org_uni_id_qbe);
			
				}		
				tc.debug ( "2.- sqlCondition :" + sqlCondition);		
// referent
				if(d.kolateralqbemap.use_id_qbe != null){							
	/*				sqlCondition = sqlCondition
								  + " AND"
								  + " ch.use_id = ?";*/
				
					sqlCondition = sqlCondition
					  + " AND"
					  + " (ch.use_open_id = ? OR ch.use_id = ? OR ch.use_id_ver = ? OR ch.use_id_aut = ?)";
				
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
			
				}				
				tc.debug ( "3.- sqlCondition :" + sqlCondition);
		
		  
		
// partija kolaterala
		
				if((d.kolateralqbemap.Kolateral_txtColNumQBE != null) && !d.kolateralqbemap.Kolateral_txtColNumQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtColNumQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
								   + " AND"
								   + " ch.col_num like ?";	
					}else{
						sqlCondition = sqlCondition
								   + " AND"
								   + " ch.col_num = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtColNumQBE);
	
				}
				tc.debug ( "4.- sqlCondition :" + sqlCondition);
// datum unosa kolaterala
		
				if(d.kolateralqbemap.Kolateral_txtDateFromQBE != null){										
	/*				sqlCondition = sqlCondition
								  + " AND"
								  + " date(ch.opening_ts) >= ?"; */
	
					sqlCondition = sqlCondition
					  + " AND"
					  + " (date(ch.opening_ts) >= ? OR date(ch.user_lock) >= ? OR date(ch.verification_ts) >= ? OR date(ch.autorization_ts) >= ?)";				
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);				
	  
	
				}
				tc.debug ( "5.- sqlCondition :" + sqlCondition);
			
				if(d.kolateralqbemap.Kolateral_txtDateUntilQBE != null){										
	/*				sqlCondition = sqlCondition
								  + " AND"
								  + " date(ch.opening_ts) <= ?";*/
				
					sqlCondition = sqlCondition
					  + " AND"
					  + " (date(ch.opening_ts) <= ? OR date(ch.user_lock) <= ? OR date(ch.verification_ts) <= ? OR date(ch.autorization_ts) <= ?)";								
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
	
				}		
				tc.debug ( "6.- sqlCondition :" + sqlCondition);
		
// partija plasmana
		
				if((d.kolateralqbemap.Kolateral_txtAccNoQBE != null) && !d.kolateralqbemap.Kolateral_txtAccNoQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtAccNoQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
								   + " AND"
								   + " lb.acc_no LIKE ?";	
					}else{
						sqlCondition = sqlCondition
								   + " AND"
								   + " lb.acc_no = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtAccNoQBE);
	
				}		
				tc.debug ( "7.- sqlCondition :" + sqlCondition);
// broj zahtjeva		
		
				if((d.kolateralqbemap.Kolateral_txtNoRqQBE != null) && !d.kolateralqbemap.Kolateral_txtNoRqQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtNoRqQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
								   + " AND"
								   + " lb.request_no like ?";	
					}else{
						sqlCondition = sqlCondition
								   + " AND"
								   + " lb.request_no = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtNoRqQBE);
	
				}				
				tc.debug ( "8.- sqlCondition :" + sqlCondition);
				
// broj ugovora

                if((d.kolateralqbemap.ContractNo_txtQBE != null) && !d.kolateralqbemap.ContractNo_txtQBE.equals("")){
                    if(d.kolateralqbemap.Kolateral_txtNoRqQBE.trim().indexOf("*") != -1){
                        sqlCondition = sqlCondition
                                   + " AND"
                                   + " lb.acc_no_old like ?";   
                    }else{
                        sqlCondition = sqlCondition
                                   + " AND"
                                   + " lb.acc_no_old = ?";
                    }   
                    values.add(d.kolateralqbemap.ContractNo_txtQBE);

                }               
                tc.debug ( "8a.- sqlCondition :" + sqlCondition);				
// vlasnik plasmana
		
				if(d.kolateralqbemap.cus_id_qbe != null){							
					sqlCondition = sqlCondition
								  + " AND"
								  + " lb.cus_id = ?";
				
					values.add(d.kolateralqbemap.cus_id_qbe);
	
				}
				tc.debug ( "9.- sqlCondition :" + sqlCondition);
				
				  
//RealEstate_OwnerCUS_ID - vlasnik kolaterala   
                if (d.kolateralqbemap.RealEstate_OwnerCUS_ID != null) {
                    sqlCondition = sqlCondition
                      + " AND"
                      + " cown.cus_id = ?";
    
                    values.add(d.kolateralqbemap.RealEstate_OwnerCUS_ID);                   
                } 
                tc.debug ( "10N.- sqlCondition :" + sqlCondition);
			
			
// samo za vozila - broj sasije
				if((d.kolateralqbemap.Vehi_txtVINQBE != null) && !d.kolateralqbemap.Vehi_txtVINQBE.equals("")){
					if(d.kolateralqbemap.Vehi_txtVINQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
								   + " AND"
								   + " veh.veh_vin_num like ?";	
					}else{
						sqlCondition = sqlCondition
								   + " AND"
								   + " veh.veh_vin_num = ?";
					}	
					values.add(d.kolateralqbemap.Vehi_txtVINQBE);
	
				}				
				tc.debug ( "9a.- sqlCondition :" + sqlCondition);			
			
//	samo za depozite - broj partije depozita
				if((d.kolateralqbemap.Coll_txtDepAccQBE != null) && !d.kolateralqbemap.Coll_txtDepAccQBE.equals("")){
					if(d.kolateralqbemap.Coll_txtDepAccQBE.trim().indexOf("*") != -1){
						sqlCondition = sqlCondition
								   + " AND"
								   + " dep.cde_account like ?";	
					}else{
						sqlCondition = sqlCondition
								   + " AND"
								   + " dep.cde_account = ?";
					}	
					values.add(d.kolateralqbemap.Coll_txtDepAccQBE);
	
				}				
				tc.debug ( "9b.- sqlCondition :" + sqlCondition);						

//samo za VRP - ISIN
	            if((d.kolateralqbemap.ISIN_txtQBE != null) && !d.kolateralqbemap.ISIN_txtQBE.equals("")){
	                if(d.kolateralqbemap.ISIN_txtQBE.trim().indexOf("*") != -1){
	                    sqlCondition = sqlCondition
	                               + " AND"
	                               + " in2vrp.isin like ?"; 
	                }else{
	                    sqlCondition = sqlCondition
	                               + " AND"
	                               + " in2vrp.isin = ?";
	                }   
	                values.add(d.kolateralqbemap.ISIN_txtQBE);

	            }               
	            tc.debug ( "9c.- sqlCondition :" + sqlCondition);     

//samo za VRP - izdavatelj
	            if(d.kolateralqbemap.issuer_cus_id_qbe != null){                           
	                sqlCondition = sqlCondition
	                              + " AND"
	                              + " vrp.iss_cus_id = ?";
	            
	                values.add(d.kolateralqbemap.issuer_cus_id_qbe);

	            }
	            tc.debug ( "9d.- sqlCondition :" + sqlCondition);	            

//samo za garancije - izdavatelj
	            if(d.kolateralqbemap.guar_issuer_cus_id_qbe != null){                           
	                sqlCondition = sqlCondition
	                              + " AND"
	                              + " gar.guar_issuer_id = ?";
	            
	                values.add(d.kolateralqbemap.guar_issuer_cus_id_qbe);

	            }
	            tc.debug ( "9e.- sqlCondition :" + sqlCondition);
	            
// samo za nekretnine	            
//RealEstate_txtQbeRealEstLandRegn - broj ZKU   
                if((d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn != null) && !d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn.equals("")){
                    if(d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn.trim().indexOf("*") != -1){
                        sqlCondition = sqlCondition
                        + " AND"
                        + " cres.real_est_land_regn like ?";    
                    }else{
                        sqlCondition = sqlCondition
                        + " AND"
                        + " cres.real_est_land_regn = ?";
                    }   
                    values.add(d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn);

                }               
                tc.debug ( "11N.- sqlCondition :" + sqlCondition);              
            
//RealEstate_txtQbeLandSub - podulozak 
                if((d.kolateralqbemap.RealEstate_txtQbeLandSub != null) && !d.kolateralqbemap.RealEstate_txtQbeLandSub.equals("")){
                    if(d.kolateralqbemap.RealEstate_txtQbeLandSub.trim().indexOf("*") != -1){
                        sqlCondition = sqlCondition
                        + " AND"
                        + " cres.real_est_land_sub like ?"; 
                    }else{
                        sqlCondition = sqlCondition
                        + " AND"
                        + " cres.real_est_land_sub = ?";
                    }   
                    values.add(d.kolateralqbemap.RealEstate_txtQbeLandSub);

                }               
                tc.debug ( "12N.- sqlCondition :" + sqlCondition);
                
//RealEstate_txtCoown - suvlasnicki udio
                if((d.kolateralqbemap.RealEstate_txtCoown != null) && !d.kolateralqbemap.RealEstate_txtCoown.equals("")){
                    if(d.kolateralqbemap.RealEstate_txtCoown.trim().indexOf("*") != -1){
                        sqlCondition = sqlCondition
                        + " AND"
                        + " cres.coown like ?"; 
                    }else{
                        sqlCondition = sqlCondition
                        + " AND"
                        + " cres.coown = ?";
                    }   
                    values.add(d.kolateralqbemap.RealEstate_txtCoown);

                }   
                tc.debug ( "13N.- sqlCondition :" + sqlCondition);
                
//RealEstate_QBE_CADA_MUNC - katastarska opcina             
                if (d.kolateralqbemap.RealEstate_QBE_CADA_MUNC != null) {
                        sqlCondition = sqlCondition
                        + " AND"
                        + " cres.real_est_cada_munc =  ?";  
    
                    values.add(d.kolateralqbemap.RealEstate_QBE_CADA_MUNC);
                }                                               
                tc.debug ( "14N.- sqlCondition :" + sqlCondition);	            
	            
	            
//			 jos jednom isti uvjeti zbog UNION
			
//			 organizacijska jedinica unosa 
				if(d.kolateralqbemap.org_uni_id_qbe != null){							
					sqlCondition1 = sqlCondition1
									  + " AND"
									  + " ch.org_uni_id = ?";
					
					values.add(d.kolateralqbemap.org_uni_id_qbe);
				
				}		
				tc.debug ( "2.- sqlCondition1 :" + sqlCondition1);		
//	 referent
				if(d.kolateralqbemap.use_id_qbe != null){							
	/*				sqlCondition1 = sqlCondition1
									  + " AND"
									  + " ch.use_id = ?"; */
					
					sqlCondition1 = sqlCondition1
					  + " AND"
					  + " (ch.use_open_id = ? OR ch.use_id = ? OR ch.use_id_ver = ? OR ch.use_id_aut = ?)";
				
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
					values.add(d.kolateralqbemap.use_id_qbe);
				
				}				 
				tc.debug ( "3.- sqlCondition1 :" + sqlCondition1);
				
			
			
//	 partija kolaterala
			
				if((d.kolateralqbemap.Kolateral_txtColNumQBE != null) && !d.kolateralqbemap.Kolateral_txtColNumQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtColNumQBE.trim().indexOf("*") != -1){
						sqlCondition1 = sqlCondition1
									   + " AND"
									   + " ch.col_num like ?";	
					}else{
						sqlCondition1 = sqlCondition1
									   + " AND"
									   + " ch.col_num = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtColNumQBE);
	
				}
				tc.debug ( "4.- sqlCondition1 :" + sqlCondition1);
//	 datum unosa kolaterala
			
				if(d.kolateralqbemap.Kolateral_txtDateFromQBE != null){										
	/*				sqlCondition1 = sqlCondition1
									  + " AND"
									  + " date(ch.opening_ts) >= ?"; */
	
					sqlCondition1 = sqlCondition1
					  + " AND"  
					  + " (date(ch.opening_ts) >= ? OR date(ch.user_lock) >= ? OR date(ch.verification_ts) >= ? OR date(ch.autorization_ts) >= ?)";				
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateFromQBE);				
	
	
				}   
				tc.debug ( "5.- sqlCondition1 :" + sqlCondition1);
				
				if(d.kolateralqbemap.Kolateral_txtDateUntilQBE != null){										
	/*				sqlCondition1 = sqlCondition1
									  + " AND"
									  + " date(ch.opening_ts) <= ?";*/
					
					sqlCondition1= sqlCondition1
					  + " AND"
					  + " (date(ch.opening_ts) <= ? OR date(ch.user_lock) <= ? OR date(ch.verification_ts) <= ? OR date(ch.autorization_ts) <= ?)";								
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
					values.add(d.kolateralqbemap.Kolateral_txtDateUntilQBE);
				}		
				tc.debug ( "6.- sqlCondition1 :" + sqlCondition1);
			
//	 partija plasmana
			
				if((d.kolateralqbemap.Kolateral_txtAccNoQBE != null) && !d.kolateralqbemap.Kolateral_txtAccNoQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtAccNoQBE.trim().indexOf("*") != -1){
						sqlCondition1 = sqlCondition1
									   + " AND"
									   + " lb.acc_no LIKE ?";	
					}else{
						sqlCondition1 = sqlCondition1
									   + " AND"
									   + " lb.acc_no = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtAccNoQBE);
	
				}		
				tc.debug ( "7.- sqlCondition1 :" + sqlCondition1);
//	 broj zahtjeva		
			
				if((d.kolateralqbemap.Kolateral_txtNoRqQBE != null) && !d.kolateralqbemap.Kolateral_txtNoRqQBE.equals("")){
					if(d.kolateralqbemap.Kolateral_txtNoRqQBE.trim().indexOf("*") != -1){
						sqlCondition1 = sqlCondition1
									   + " AND"
									   + " lb.request_no like ?";	
					}else{
						sqlCondition1 = sqlCondition1
									   + " AND"
									   + " lb.request_no = ?";
					}	
					values.add(d.kolateralqbemap.Kolateral_txtNoRqQBE);
	
				}				
				tc.debug ( "8.- sqlCondition1 :" + sqlCondition1);
				
// broj ugovora

                if((d.kolateralqbemap.ContractNo_txtQBE != null) && !d.kolateralqbemap.ContractNo_txtQBE.equals("")){
                    if(d.kolateralqbemap.Kolateral_txtNoRqQBE.trim().indexOf("*") != -1){
                        sqlCondition1 = sqlCondition1
                                   + " AND"
                                   + " lb.acc_no_old like ?";   
                    }else{
                        sqlCondition1 = sqlCondition1
                                   + " AND"
                                   + " lb.acc_no_old = ?";
                    }   
                    values.add(d.kolateralqbemap.ContractNo_txtQBE);

                }               
                tc.debug ( "8a.- sqlCondition1 :" + sqlCondition1);				
//	 vlasnik plasmana
			 
				if(d.kolateralqbemap.cus_id_qbe != null){							
					sqlCondition1 = sqlCondition1
									  + " AND"
									  + " lb.cus_id = ?";
					
					values.add(d.kolateralqbemap.cus_id_qbe);
	
				}
				tc.debug ( "9.- sqlCondition1 :" + sqlCondition1);					
 
//			 samo za vozila - broj sasije
				if((d.kolateralqbemap.Vehi_txtVINQBE != null) && !d.kolateralqbemap.Vehi_txtVINQBE.equals("")){
					if(d.kolateralqbemap.Vehi_txtVINQBE.trim().indexOf("*") != -1){
						sqlCondition1 = sqlCondition1
								   + " AND"
								   + " veh.veh_vin_num like ?";	
					}else{
						sqlCondition1 = sqlCondition1
								   + " AND"
								   + " veh.veh_vin_num = ?";
					}	
					values.add(d.kolateralqbemap.Vehi_txtVINQBE);
	
				}				
				tc.debug ( "9a.- sqlCondition :" + sqlCondition);		
				
//RealEstate_OwnerCUS_ID - vlasnik kolaterala   
                if (d.kolateralqbemap.RealEstate_OwnerCUS_ID != null) {
                    sqlCondition1 = sqlCondition1
                      + " AND"
                      + " cown.cus_id = ?";
    
                    values.add(d.kolateralqbemap.RealEstate_OwnerCUS_ID);                   
                } 
                tc.debug ( "10N.- sqlCondition :" + sqlCondition1);
			
//	samo za depozite - broj partije
				if((d.kolateralqbemap.Coll_txtDepAccQBE != null) && !d.kolateralqbemap.Coll_txtDepAccQBE.equals("")){
					if(d.kolateralqbemap.Coll_txtDepAccQBE.trim().indexOf("*") != -1){
						sqlCondition1 = sqlCondition1
								   + " AND"
								   + " dep.cde_account like ?";	
					}else{
						sqlCondition1 = sqlCondition1
								   + " AND"
								   + " dep.cde_account = ?";
					}	
					values.add(d.kolateralqbemap.Coll_txtDepAccQBE);
	
				}				
				tc.debug ( "9b.- sqlCondition :" + sqlCondition1);					
			  
//samo za VRP - ISIN
	            if((d.kolateralqbemap.ISIN_txtQBE != null) && !d.kolateralqbemap.ISIN_txtQBE.equals("")){
	                if(d.kolateralqbemap.ISIN_txtQBE.trim().indexOf("*") != -1){
	                    sqlCondition1 = sqlCondition1
	                               + " AND"
	                               + " in2vrp.isin like ?"; 
	                }else{
	                    sqlCondition1 = sqlCondition1
	                               + " AND"
	                               + " in2vrp.isin = ?";
	                }   
	                values.add(d.kolateralqbemap.ISIN_txtQBE);

	            }               
	            tc.debug ( "9c.- sqlCondition1 :" + sqlCondition1);     

//samo za VRP - izdavatelj
	            if(d.kolateralqbemap.issuer_cus_id_qbe != null){                           
	                sqlCondition1 = sqlCondition1
	                              + " AND"
	                              + " vrp.iss_cus_id = ?";
	            
	                values.add(d.kolateralqbemap.issuer_cus_id_qbe);
  
	            }
	            tc.debug ( "9d.- sqlCondition1 :" + sqlCondition1);	            

//samo za garancije - izdavatelj
	            if(d.kolateralqbemap.guar_issuer_cus_id_qbe != null){                           
	                sqlCondition1 = sqlCondition1
	                              + " AND"
	                              + " gar.guar_issuer_id = ?";
	            
	                values.add(d.kolateralqbemap.guar_issuer_cus_id_qbe);

	            }
	            tc.debug ( "9e.- sqlCondition1 :" + sqlCondition1);     	            

// samo za nekretnine	            
//RealEstate_txtQbeRealEstLandRegn - broj ZKU   
                if(d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn != null && !d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn.equals("")){
                    if(d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn.trim().indexOf("*") != -1){
                        sqlCondition1 = sqlCondition1
                        + " AND"
                        + " cres.real_est_land_regn like ?";    
                    }else{
                        sqlCondition1 = sqlCondition1
                        + " AND"
                        + " cres.real_est_land_regn = ?";
                    }   
                    values.add(d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn);

                }               
                tc.debug ( "11N.- sqlCondition1 :" + sqlCondition1);              
            
//RealEstate_txtQbeLandSub - podulozak 
                if((d.kolateralqbemap.RealEstate_txtQbeLandSub != null) && !d.kolateralqbemap.RealEstate_txtQbeLandSub.equals("")){
                    if(d.kolateralqbemap.RealEstate_txtQbeLandSub.trim().indexOf("*") != -1){
                        sqlCondition1 = sqlCondition1
                        + " AND"
                        + " cres.real_est_land_sub like ?"; 
                    }else{
                        sqlCondition1 = sqlCondition1
                        + " AND"
                        + " cres.real_est_land_sub = ?";
                    }   
                    values.add(d.kolateralqbemap.RealEstate_txtQbeLandSub);

                }               
                tc.debug ( "12N.- sqlCondition1:" + sqlCondition1);
                
//RealEstate_txtCoown - suvlasnicki udio
                if((d.kolateralqbemap.RealEstate_txtCoown != null) && !d.kolateralqbemap.RealEstate_txtCoown.equals("")){
                    if(d.kolateralqbemap.RealEstate_txtCoown.trim().indexOf("*") != -1){
                        sqlCondition1 = sqlCondition1
                        + " AND"
                        + " cres.coown like ?"; 
                    }else{
                        sqlCondition1 = sqlCondition1
                        + " AND"
                        + " cres.coown = ?";
                    }   
                    values.add(d.kolateralqbemap.RealEstate_txtCoown);

                }   
                tc.debug ( "13N.- sqlCondition1 :" + sqlCondition1);
                
//RealEstate_QBE_CADA_MUNC - katastarska opcina             
                if (d.kolateralqbemap.RealEstate_QBE_CADA_MUNC != null) {
                        sqlCondition1 = sqlCondition1
                        + " AND"
                        + " cres.real_est_cada_munc =  ?";  
    
                    values.add(d.kolateralqbemap.RealEstate_QBE_CADA_MUNC);
                }                                               
                tc.debug ( "14N.- sqlCondition1 :" + sqlCondition1);	            
	            
	            
	            
/*			sql = 	sql + sqlColStatus + sqlCondition;
			sql1 = sql1 + sqlColStatus + sqlCondition1 + sqlOrder;			
			sql = sql + sqlUnion + sql1;*/
				if((d.kolateralqbemap.Vehi_txtVINQBE != null) && !d.kolateralqbemap.Vehi_txtVINQBE.equals("")){
					sqlVehi  = sqlVehi  + sqlColStatus + sqlCondition;
					sqlVehi1 = sqlVehi1 + sqlColStatus + sqlCondition1 + sqlOrder;	 
					sql = sqlVehi + sqlUnion + sqlVehi1;
				} else if((d.kolateralqbemap.Coll_txtDepAccQBE != null) && !d.kolateralqbemap.Coll_txtDepAccQBE.equals("")){
					sqlDepo  = sqlDepo  + sqlColStatus + sqlCondition;
					sqlDepo1 = sqlDepo1 + sqlColStatus + sqlCondition1 + sqlOrder;	 
					sql = sqlDepo + sqlUnion + sqlDepo1;
		        } else if ((d.kolateralqbemap.ISIN_txtQBE != null && !d.kolateralqbemap.ISIN_txtQBE.equals("")) || d.kolateralqbemap.issuer_cus_id_qbe != null) {
	                sqlVrp  = sqlVrp  + sqlColStatus + sqlCondition;
	                sqlVrp1 = sqlVrp1 + sqlColStatus + sqlCondition1 + sqlOrder;   
	                sql = sqlVrp + sqlUnion + sqlVrp1;   
	            } else if (d.kolateralqbemap.guar_issuer_cus_id_qbe != null) {
	                sqlGar  = sqlGar  + sqlColStatus + sqlCondition;
	                sqlGar1 = sqlGar1 + sqlColStatus + sqlCondition1 + sqlOrder;   
	                sql = sqlGar + sqlUnion + sqlGar1;     
	            } else if ((d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn != null && !d.kolateralqbemap.RealEstate_txtQbeRealEstLandRegn.equals("")) ||
	                       (d.kolateralqbemap.RealEstate_txtQbeLandSub != null && !d.kolateralqbemap.RealEstate_txtQbeLandSub.equals("")) ||
	                       (d.kolateralqbemap.RealEstate_txtCoown != null && !d.kolateralqbemap.RealEstate_txtCoown.equals("")) ||
	                       d.kolateralqbemap.RealEstate_QBE_CADA_MUNC != null) {
                    sqlNekr  = sqlNekr  + sqlColStatus + sqlCondition;
                    sqlNekr1 = sqlNekr1 + sqlColStatus + sqlCondition1 + sqlOrder;   
                    sql = sqlNekr + sqlUnion + sqlNekr1;     	                
		        } else {
					sql = 	sql + sqlColStatus + sqlCondition;
					sql1 = sql1 + sqlColStatus + sqlCondition1 + sqlOrder;			
					sql = sql + sqlUnion + sql1;				
				}
			   
		
				tc.debug ( "10.- sql" + sql);
		 		System.out.println("10. - sql : "+sql);
				tc.debug ("VALUES: " + values);
			}
		}   
//////////////////////////////////////////////////////// kraj slaganja dinamickog upita za sve kolaterale 
		      
  		prStmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
  		
		prStmt.clearParameters();
  
		tc.debug (" PREPARE STATEMENT : " );  
		
  		for(int i=0;i<values.size();i++){  			
  
  			 
  			position = i;   
  			if(values.elementAt(i).getClass().getName().substring(values.elementAt(i).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("String")){  				
  				prStmt.setString(position+1, ((String)values.elementAt(i)).replace('*', '%'));
   			}else if(values.elementAt(i).getClass().getName().substring(values.elementAt(i).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("BigDecimal")){  				
  				prStmt.setBigDecimal(position+1, (BigDecimal)values.elementAt(i));
   			}else if(values.elementAt(i).getClass().getName().substring(values.elementAt(i).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("Date")){  				
  				prStmt.setDate(position+1, (Date)values.elementAt(i));
   			}else if(values.elementAt(i).getClass().getName().substring(values.elementAt(i).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("Timestamp")){
  				prStmt.setTimestamp(position+1, (Timestamp)values.elementAt(i));
   			} 
  			
  		} 
		tc.debug (" PREPARE STATEMENT 22222222: " );  		
		rs = prStmt.executeQuery();
		
		int rowCount = size();
        int pageSize = getFetchSize();
        int pageCount = BigDecimal.valueOf((double)rowCount/pageSize).setScale(0, BigDecimal.ROUND_UP).intValue();

        d.kolateralqbemap.tblColWorkListCellTable.getPagingInfo().setCurrentPage(level);
        d.kolateralqbemap.tblColWorkListCellTable.getPagingInfo().setPageCount(pageCount);
        d.kolateralqbemap.tblColWorkListCellTable.getPagingInfo().setPageSize(pageSize);
        d.kolateralqbemap.tblColWorkListCellTable.getPagingInfo().setRowCount(rowCount);
		
		return rs;
 	}
 	
    @Override
    public int size() throws SQLException {
        int currentRow = rs.getRow();
        rs.last();
        int rowCount = rs.getRow();
        rs.absolute(currentRow);
        return rowCount;
    }    
 	
    public void close() {
        if(prStmt != null) {
            try {
            	prStmt.close();
            } catch (SQLException e) {}
        }
        
        if(rs != null){
        	try{
	        	rs.close();
        	}catch(SQLException e){
        	}	
        }	
    }         
     
    public void populateRowData() throws Exception {
					
		Vector row = null;
		Vector unique = null;
		NamedRowData nrd = new NamedRowData();
		
		row = new Vector();
		unique = new Vector();

		nrd.put("col_index", d.kolateralqbemap.tblColWorkListCellTable.rowCount());   
        nrd.put("col_col_hea_id", rs.getBigDecimal("col_hea_id"));
        CheckBoxData cd= new CheckBoxData(d.kolateralqbemap.listSelected.containsKey(rs.getBigDecimal("col_hea_id")));
        nrd.put("col_checkbox", cd);
		
		row.add(rs.getString("workflow_indic"));
		nrd.put("col_wi", rs.getString("workflow_indic"));
		
		row.add(rs.getString("collateral_status"));
		nrd.put("col_lst", rs.getString("collateral_status"));
		row.add(rs.getString("col_num"));
		nrd.put("col_broj", rs.getString("col_num"));
		row.add(rs.getString("code_char"));
		nrd.put("col_val", rs.getString("code_char"));		
		row.add(rs.getBigDecimal("real_est_nomi_valu"));
		nrd.put("col_iznos", rs.getBigDecimal("real_est_nomi_valu"));		
		row.add(rs.getString("basic_data_status")); 
		nrd.put("col_podaci", rs.getString("basic_data_status"));
		row.add(rs.getString("mortgage_status"));
		nrd.put("col_hipoteka", rs.getString("mortgage_status"));
		row.add(rs.getString("cover_indic"));
		nrd.put("col_plasman", rs.getString("cover_indic"));
		row.add(rs.getString("coll_data_status")); 
		nrd.put("col_officer", rs.getString("coll_data_status"));
		row.add(rs.getString("financial_flag"));
		nrd.put("col_knjizen", rs.getString("financial_flag"));
		row.add(rs.getString("user_name"));
		nrd.put("col_referent", rs.getString("user_name"));	
		
		unique.add(rs.getBigDecimal("col_hea_id"));
		unique.add(rs.getBigDecimal("col_type_id"));
		unique.add(rs.getBigDecimal("real_est_nm_cur_id"));
		unique.add(rs.getBigDecimal("use_id"));
		unique.add(rs.getBigDecimal("col_cat_id"));
		unique.add(rs.getString("screen_name"));
		unique.add(rs.getString("code"));
		unique.add(rs.getTimestamp("user_lock"));
		unique.add(rs.getString("coll_deact_indic"));
	
		String status=d.kolateralqbemap.proc_status_QBE;
 
        if(( status.equalsIgnoreCase("0") || status.equalsIgnoreCase("1") || status.equalsIgnoreCase("2") 
                || status.equalsIgnoreCase("M") || status.equalsIgnoreCase("R") ) && rs.getString("workflow_indic").equals("3")) { 
            d.kolateralqbemap.tblColWorkList.addRow(row, unique, "tableRowHighlight2");
            nrd.setRowStyle("tableRowHighlight2");
        } else {
            d.kolateralqbemap.tblColWorkList.addRow(row, unique);            
        } 
        d.kolateralqbemap.tblColWorkListCellTable.addNamedRow(nrd);			
	}									
}