/*
 * Created on 2006.07.20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.jcics.co25;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import hr.vestigo.framework.remote.transaction.*;
import java.math.*;
import java.util.*;
//COLL_CASHDEP
//CASH DEPOSIT QBE SELECT	17.08.2006
/**
* CO253 class selects all data from table COLL_CASHDEP, COLL_HEAD
* into list screen and puts this data into tblColWorkList.
* 	
*/


/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CO253 extends JDBCScrollableRemoteTransaction {

	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co25/CO253.java,v 1.5 2006/09/21 10:20:46 hrasia Exp $";;
	
	public DeclCO25 decl = null;
	private ResultSet rs = null;
	int i = 0;
	public CO253 (DeclCO25 decl) {
		this.decl = decl;
	}
	
	
	public ResultSet executeScrollable(TransactionContext tc) throws Exception {
		String screen_entry_param = decl.cashdepqbemapp.ScreenEntryParam;
		String phase="";
		String proc_status="";
		Vector vec=new Vector();
		Vector valuesW = new Vector();
		int position = 0;
		
		int i=0;
		int j=screen_entry_param.indexOf(".");
		int k=0;
		tc.info("screen_entry_param : " + screen_entry_param);
		while(j>0){
			vec.add(k++, screen_entry_param.substring(i,j));
			screen_entry_param=screen_entry_param.substring(j+1);
			tc.debug("screen_entry_param : "+screen_entry_param);
			j=screen_entry_param.indexOf(".");
		}
	
		if(vec==null) throw new VestigoTMException(1, "params for screen entry function", "err985", null);
		if(vec.elementAt(0)!=null && !((String)vec.elementAt(0)).trim().equals("")) phase=(String)vec.elementAt(0);
		else throw new VestigoTMException(1, "params for screen entry function", "err985", null);
		if(vec.elementAt(1)!=null && !((String)vec.elementAt(1)).trim().equals("")) proc_status=(String)vec.elementAt(1);
		else throw new VestigoTMException(1, "params for screen entry function", "err985", null);

		tc.info("phase : "+phase);
		tc.info("proc_status : "+proc_status);

		
		setLevel(decl.cashdepqbemapp.ActionListLevel);
		setFetchSize(tc.ACTION_LIST_FETCH_SIZE);
		Connection con = tc.getConnection();
		PreparedStatement prStmt = null;
		
		
		tc.debug("............decl.cashdepqbemapp.RealEstate_txtOwnerQbeRegNo2		" + decl.cashdepqbemapp.RealEstate_txtOwnerQbeRegNo2  );             											
		tc.debug("...........decl.cashdepqbemapp.RealEstate_txtCarrierQbeRegNo2	    " + decl.cashdepqbemapp.RealEstate_txtCarrierQbeRegNo2 );    
		
		
		 tc.debug("\n  ......................USAO SAM u 253");

		String sql = " SELECT distinct " +
							" ch.col_hea_id, " +					
							" ch.col_type_id, " +         
							" ch.workflow_indic, " +      
							" ch.collateral_status, " +   
							" ch.col_num, " +             
							" ch.real_est_nm_cur_id, " +  
							" cu.code_char, " +           
							" ch.real_est_nomi_valu, " +  
							" ch.basic_data_status, " +   
							" ch.mortgage_status, " +     
							" ch.cover_indic, " +         
							" ch.coll_data_status, " +    
							" ch.use_id, " +              
							" au.user_name, " +           
							" ch.col_cat_id, " +          
							" clc.screen_name, " +        
							" clc.code,  " +
							" ch.user_lock " + 
					  " FROM coll_head ch  " +
  					  "	INNER JOIN app_user au ON ch.use_id = au.use_id " +
					  " INNER JOIN coll_category clc ON ch.col_cat_id = clc.col_cat_id " +
					  " INNER JOIN coll_cashdep cashd ON  ch.col_hea_id = cashd.col_hea_id " +
					  " INNER JOIN currency cu ON  ch.real_est_nm_cur_id = cu.cur_id " +
					  " LEFT OUTER JOIN coll_owner cown ON ch.col_hea_id = cown.col_hea_id " +	
					  " LEFT OUTER JOIN coll_hf_prior chp ON ch.col_hea_id = chp.hf_coll_head_id " +
					  " LEFT OUTER JOIN loan_beneficiary lobe ON chp.coll_hf_prior_id = lobe.coll_hf_prior_id " + 
					  " WHERE  ";
		
		
		
			String sqlOrderBy = " ORDER BY ch.workflow_indic desc, ch.col_hea_id asc ";
		
			String sqlProcStatus = " ch.collateral_status = ? ";				//proc_status	4 0 1 5 D 2 3 
		
			String sqlProcStatusM = " ch.collateral_status in ('0','1','2') ";	//proc_status	M
		
		
			if(proc_status!=null) {
				
				if (proc_status.trim().equals("M")) {
					sql = sql + " ch.collateral_status in ('0','1','2') ";
				}else{
					valuesW.add(proc_status);
					sql = sql + " ch.collateral_status = ? ";
				}
				
			}
			
			String p_owner="";
			if(decl.cashdepqbemapp.RealEstate_txtOwnerQbeRegNo2 != null && !"".equals(decl.cashdepqbemapp.RealEstate_txtOwnerQbeRegNo2)){

				p_owner = decl.cashdepqbemapp.RealEstate_txtOwnerQbeRegNo2;
				p_owner = p_owner.trim();
				valuesW.add(p_owner);	
				sql = sql + " AND cown.register_no = ? ";
			}
				
			
			String p_carrier="";
			if(decl.cashdepqbemapp.RealEstate_txtCarrierQbeRegNo2 != null && !"".equals(decl.cashdepqbemapp.RealEstate_txtCarrierQbeRegNo2)){

				p_carrier = decl.cashdepqbemapp.RealEstate_txtCarrierQbeRegNo2;
				p_carrier = p_carrier.trim();
				valuesW.add(p_carrier);
				sql = sql + " AND lobe.register_no = ? ";
				
			}
			
			
			
			
			
			
			
			
	 	 
			String accNo= "";
			if(decl.cashdepqbemapp.RealEstate_txtQbeAccNo2 != null && !"".equals(decl.cashdepqbemapp.RealEstate_txtQbeAccNo2)){
				
				
				if(decl.cashdepqbemapp.RealEstate_txtQbeAccNo2.indexOf("*") != -1){
					sql = sql + " AND lobe.acc_no like ? ";
				}else{
					sql = sql + " AND lobe.acc_no = ? ";
				}
				accNo = decl.cashdepqbemapp.RealEstate_txtQbeAccNo2;
				valuesW.add(accNo);	
				
			}
			
			String requestNo= "";
			if(decl.cashdepqbemapp.RealEstate_txtQbeRequestNo2 != null && !"".equals(decl.cashdepqbemapp.RealEstate_txtQbeRequestNo2)){
				
				
				if(decl.cashdepqbemapp.RealEstate_txtQbeRequestNo2.indexOf("*") != -1){
					sql = sql + " AND lobe.request_no like ? ";
				}else{
					sql = sql + " AND lobe.request_no = ? ";
				}
				requestNo = decl.cashdepqbemapp.RealEstate_txtQbeRequestNo2;
				valuesW.add(requestNo);	
				
			}
	 	 
			String sifra= "";
			if(decl.cashdepqbemapp.RealEstate_txtQbeCode2 != null && !"".equals(decl.cashdepqbemapp.RealEstate_txtQbeCode2)){
				
				
				if(decl.cashdepqbemapp.RealEstate_txtQbeCode2.indexOf("*") != -1){
					sql = sql + " AND ch.col_num like ? ";
				}else{
					sql = sql + " AND ch.col_num = ? ";
				}
				sifra = decl.cashdepqbemapp.RealEstate_txtQbeCode2;
				valuesW.add(sifra);	
				
			}
 
			
			if(proc_status!=null) {
			
				if ( proc_status.trim().equals("0") || proc_status.trim().equals("5")) {
					
					
					sql = sql + " AND ch.use_id = ? ";
					valuesW.add(decl.cashdepqbemapp.use_id);	
					
					sql = sql + " AND ch.org_uni_id = ? ";
					valuesW.add(decl.cashdepqbemapp.org_uni_id);	
					
					
				}
				if ( proc_status.trim().equals("1") ) {
					
					sql = sql + " AND ch.org_uni_id = ? ";
					valuesW.add(decl.cashdepqbemapp.org_uni_id);	
					
					
				}
				
				if(proc_status.trim().equals("3")){
					if(decl.cashdepqbemapp.RealEstate_Qbe_ORG_UNI_ID2 != null ){
						sql = sql + " AND ch.origin_org_uni_id = ? ";
						valuesW.add(decl.cashdepqbemapp.RealEstate_Qbe_ORG_UNI_ID2);	
					}
					if(decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2 != null ){
						sql = sql + " AND ch.use_open_id = ? ";
						valuesW.add(decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2);	
					}
				}
				if(proc_status.trim().equals("4")){
					if(decl.cashdepqbemapp.RealEstate_Qbe_ORG_UNI_ID2 != null ){
						sql = sql + " AND ch.origin_org_uni_id = ? ";
						valuesW.add(decl.cashdepqbemapp.RealEstate_Qbe_ORG_UNI_ID2);	
					}
					if(decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2 != null ){
						sql = sql + " AND ch.use_open_id = ? ";
						valuesW.add(decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2);	
					}
				}
				if(proc_status.trim().equals("M")){
					if(decl.cashdepqbemapp.RealEstate_Qbe_ORG_UNI_ID2 != null ){
						sql = sql + " AND ch.origin_org_uni_id = ? ";
						valuesW.add(decl.cashdepqbemapp.RealEstate_Qbe_ORG_UNI_ID2);	
					}
					if(decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2 != null ){
						sql = sql + " AND ch.use_open_id = ? ";
						valuesW.add(decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2);	
					}
				}
				
				
			}
			
			if(decl.cashdepqbemapp.RealEstate_txtQbeDateFrom2 != null && decl.cashdepqbemapp.RealEstate_txtQbeDateUntil2 != null ){
				
				sql = sql + " AND date(ch.opening_ts) between  ? AND ? ";
				valuesW.add(decl.cashdepqbemapp.RealEstate_txtQbeDateFrom2);	
				valuesW.add(decl.cashdepqbemapp.RealEstate_txtQbeDateUntil2);	
				
			}else{
				if(decl.cashdepqbemapp.RealEstate_txtQbeDateFrom2 != null ){
					sql = sql + " AND date(ch.opening_ts) >=  ?  ";
					valuesW.add(decl.cashdepqbemapp.RealEstate_txtQbeDateFrom2);	
				}
			
			}
			 
			 
			
			tc.info("p_owner " + p_owner);
			tc.info("p_carrier " + p_carrier);
			tc.info("decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2 " + decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2);
			tc.info("decl.cashdepqbemapp.RealEstate_Qbe_ORG_UNI_ID2 " + decl.cashdepqbemapp.RealEstate_Qbe_ORG_UNI_ID2);
			tc.info("decl.cashdepqbemapp.RealEstate_QbeDateFrom2 " + decl.cashdepqbemapp.RealEstate_txtQbeDateFrom2);
			tc.info("decl.cashdepqbemapp.RealEstate_QbeDateUntil2 " + decl.cashdepqbemapp.RealEstate_txtQbeDateUntil2);
			
			tc.debug("\n");
			tc.debug(sql);
			tc.debug("\n");
			tc.debug("decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2 " + decl.cashdepqbemapp.RealEstate_QbeUSE_OPEN_ID2);
			

			prStmt = con.prepareStatement(sql);
			prStmt.clearParameters();
			

			
			for(int i1=0;i1<valuesW.size();i1++){  			
	  			
	  			 
	  			position = i1;   
	  			if(valuesW.elementAt(i1).getClass().getName().substring(valuesW.elementAt(i1).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("String")){  				
	  				prStmt.setString(position+1, ((String)valuesW.elementAt(i1)).replace('*', '%'));
	  				//prStmtCount.setString(position+1, ((String)values.elementAt(i)).replace('*', '%'));
	  			}else if(valuesW.elementAt(i1).getClass().getName().substring(valuesW.elementAt(i1).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("BigDecimal")){  				
	  				
	  				//tc.debug(valuesW.elementAt(i1)+ "");
	  				//tc.debug(i1 + "");
	  				
	  				prStmt.setBigDecimal(position+1, (BigDecimal)valuesW.elementAt(i1));
	  				//prStmtCount.setBigDecimal(position+1, (BigDecimal)values.elementAt(i));
	  			}else if(valuesW.elementAt(i1).getClass().getName().substring(valuesW.elementAt(i1).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("Date")){  				
	  				prStmt.setDate(position+1, (java.sql.Date)valuesW.elementAt(i1));
	  				//prStmtCount.setDate(position+1, (Date)values.elementAt(i));
	  			}else if(valuesW.elementAt(i1).getClass().getName().substring(valuesW.elementAt(i1).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("Timestamp")){
	  				prStmt.setTimestamp(position+1, (Timestamp)valuesW.elementAt(i1));
	  				//prStmtCount.setTimestamp(position+1, (Timestamp)values.elementAt(i));
	  			} 
	  			
	  		}
			//tc.debug("" + prStmt);
			rs = prStmt.executeQuery();
			
//			while(rs.next()){
//				
//				tc.debug(rs.getBigDecimal(1) + "");
//			
//			}
//			
			
  										
		if(proc_status!=null) {
			if (proc_status.trim().equals("4")){
  				tc.info("tu sam .... proc_status : "+proc_status);
  				// lista ponistenih, jedna za banku
  				
				
				
  						
				// lista ponistenih po org.jed. - treba doadati ? 			
  	  		}//status 4
			if (proc_status.trim().equals("0")) {
				//referentska lista za unos, po referentu  			  
		  	 	tc.info("tu sam referentska lista.... proc_status : "+proc_status); 			
		  		tc.info("tu sam .... proc_status : "+decl.cashdepqbemapp.use_id); 			
		  		tc.info("tu sam .... proc_status : "+decl.cashdepqbemapp.org_uni_id); 			
		  				
		  			
		  							
		  	  					
		  	}//status 0
			
			if (proc_status.trim().equals("1")){
				//verifikacijska lista, po organizacijskoj jedinici  			  
	 			tc.info("tu sam .verifikacijska lista, po org.jedinici... proc_status : "+proc_status);
	 			
  	  		}//status 1
			
			if (proc_status.trim().equals("5")) {
				//referentska lista odbijenih  				
	 			tc.info("tu sam ref.lista odbijenih.... proc_status : "+proc_status);
	 			
	 			
  				
		  			 			
			}//status 5
			
			if (proc_status.trim().equals("D")) {
				proc_status = "5";
				//lista odbijenih jedna za banku 					
 	 		 	tc.info("tu sam lista odbijenih jedna za banku 	.... proc_status : "+proc_status);
 	 		 	
			}//status D
			
			
			if (proc_status.trim().equals("2")) {
				//autorizacijska lista - jedna za banku  		
	 			tc.info("tu sam ..autorizacijska lista - jedna za banku .. proc_status : "+proc_status);  			  			 
				
	 			

	 			
			}//status 2
			
			
			if (proc_status.trim().equals("3")) {
				//arhivska lista - jedna za banku  		
	 			tc.info("tu sam .arhivska lista - jedna za banku... proc_status : "+proc_status);  			  			 
					

	 			
			}//status 3
			
			
			
			if (proc_status.trim().equals("M")) {
				//monitoring lista - jedna za banku, svi collaterali koji su u radu, status not in (3,4,5)  		
	 			tc.info("tu sam .monitoring lista - jedna za banku,... proc_status : "+proc_status);  			  			 
					
	 			
			}//status M
			
		}//proc_status!=null
		return rs;
		
	}
	
	 public void close() {
	 	if(rs != null){
        	try{
	        	rs.close();
        	}catch(SQLException e){
        	}	
	 	}	
    } //close   
	 
	 public void populateRowData() throws Exception {
	 	
	 	Vector row = new Vector();	
	 	Vector unique =new Vector();	
	 	
	 	
	 	row.add(rs.getString("workflow_indic"));
	 	row.add(rs.getString("collateral_status"));
	 	row.add(rs.getString("col_num"));
	 	row.add(rs.getString("code_char"));
	 	row.add(rs.getBigDecimal("real_est_nomi_valu"));
	 	row.add(rs.getString("basic_data_status"));
	 	row.add(rs.getString("mortgage_status"));
	 	row.add(rs.getString("cover_indic"));
	 	row.add(rs.getString("coll_data_status"));
	 	row.add(rs.getString("user_name"));
	 	
	 		 	
	 	unique.add(rs.getBigDecimal("col_hea_id"));	
		unique.add(rs.getBigDecimal("col_type_id"));	
		unique.add(rs.getBigDecimal("real_est_nm_cur_id"));	
		unique.add(rs.getBigDecimal("use_id"));	
		unique.add(rs.getBigDecimal("col_cat_id"));	
		unique.add(rs.getString("screen_name"));	
		unique.add(rs.getString("code"));	
		unique.add(rs.getTimestamp("user_lock"));	  
      
       

		decl.cashdepqbemapp.tblColWorkList.addRow(row, unique);
	 	

	 } //populateRowData
	 
	 
	 
	 
}
