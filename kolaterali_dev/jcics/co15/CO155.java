/*
 * Created on 2006.07.20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.jcics.co15;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import hr.vestigo.framework.remote.transaction.*;
import java.math.*;
import java.util.*;
//COLL_BILL_EXCH
//MJENICA QBE SELECT 16.08.2006
/**
* CO155 class selects all data from table COLL_BILL_EXCH, COLL_HEAD
* into list screen and puts this data into tblColWorkList.
* 	
*/  


/**
 * @author hrasia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CO155 extends JDBCScrollableRemoteTransaction {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co15/CO155.java,v 1.7 2007/10/03 08:09:56 hramkr Exp $";
	
	public DeclCO15 decl = null;
	private ResultSet rs = null;
	private PreparedStatement prStmt = null;
	int i = 0;
	public CO155 (DeclCO15 decl) {
		this.decl = decl;
	}
	
	
	public ResultSet executeScrollable(TransactionContext tc) throws Exception {
		String screen_entry_param = decl.collboeqbemapp.ScreenEntryParam;
		String phase="";
		String proc_status="";
		Vector vec=new Vector();
		Vector valuesW = new Vector();
		int position = 0;
		
		int i=0;
		int j=screen_entry_param.indexOf(".");
		int k=0;
		tc.info("screen_entry_param : "+screen_entry_param);
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

		
		setLevel(decl.collboeqbemapp.ActionListLevel);
		setFetchSize(tc.ACTION_LIST_FETCH_SIZE);
		Connection con = tc.getConnection();
		con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);	
//		PreparedStatement prStmt = null;
		
		
		
		 tc.info("decl.collboeqbemapp.RealEstate_txtOwnerQbeRegNo2		" + decl.collboeqbemapp.RealEstate_txtOwnerQbeRegNo2  );             											
		 tc.info("decl.collboeqbemapp.RealEstate_txtCarrierQbeRegNo2	    " + decl.collboeqbemapp.RealEstate_txtCarrierQbeRegNo2 );    
		 
		
		

		String sql = " SELECT distinct" +
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
					  " INNER JOIN coll_bill_exch  cboe ON  ch.col_hea_id = cboe.col_hea_id " +
					  " LEFT OUTER JOIN currency cu ON  ch.real_est_nm_cur_id = cu.cur_id " +
					  " INNER JOIN coll_boe_debtor cbdebu ON  cboe.boe_id = cbdebu.boe_id " +
					  " LEFT OUTER JOIN loan_beneficiary lobe ON ch.col_hea_id = lobe.col_hea_id " + 
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
			if(decl.collboeqbemapp.RealEstate_txtOwnerQbeRegNo2 != null && !"".equals(decl.collboeqbemapp.RealEstate_txtOwnerQbeRegNo2)){

				p_owner = decl.collboeqbemapp.RealEstate_txtOwnerQbeRegNo2;
				p_owner = p_owner.trim();
				valuesW.add(p_owner);	
				sql = sql + " AND cbdebu.register_no = ?  AND cbdebu.role_boe_id = 1620130003 ";
			}
				
			
			String p_carrier="";
			if(decl.collboeqbemapp.RealEstate_txtCarrierQbeRegNo2 != null && !"".equals(decl.collboeqbemapp.RealEstate_txtCarrierQbeRegNo2)){

				p_carrier = decl.collboeqbemapp.RealEstate_txtCarrierQbeRegNo2;
				p_carrier = p_carrier.trim();
				valuesW.add(p_carrier);
				sql = sql + " AND lobe.register_no = ? ";
				
			}
			
			
	 	 
			String accNo= "";
			if(decl.collboeqbemapp.RealEstate_txtQbeAccNo2 != null && !"".equals(decl.collboeqbemapp.RealEstate_txtQbeAccNo2)){
				
				
				if(decl.collboeqbemapp.RealEstate_txtQbeAccNo2.indexOf("*") != -1){
					sql = sql + " AND lobe.acc_no like ? ";
				}else{
					sql = sql + " AND lobe.acc_no = ? ";
				}
				accNo = decl.collboeqbemapp.RealEstate_txtQbeAccNo2;
				valuesW.add(accNo);	
				
			}
			
			String requestNo= "";
			if(decl.collboeqbemapp.RealEstate_txtQbeRequestNo2 != null && !"".equals(decl.collboeqbemapp.RealEstate_txtQbeRequestNo2)){
				
				
				if(decl.collboeqbemapp.RealEstate_txtQbeRequestNo2.indexOf("*") != -1){
					sql = sql + " AND lobe.request_no like ? ";
				}else{
					sql = sql + " AND lobe.request_no = ? ";
				}
				requestNo = decl.collboeqbemapp.RealEstate_txtQbeRequestNo2;
				valuesW.add(requestNo);	
				
			}
	 	 
			String sifra= "";
			if(decl.collboeqbemapp.RealEstate_txtQbeCode2 != null && !"".equals(decl.collboeqbemapp.RealEstate_txtQbeCode2)){
				
				
				if(decl.collboeqbemapp.RealEstate_txtQbeCode2.indexOf("*") != -1){
					sql = sql + " AND ch.col_num like ? ";
				}else{
					sql = sql + " AND ch.col_num = ? ";
				}
				sifra = decl.collboeqbemapp.RealEstate_txtQbeCode2;
				valuesW.add(sifra);	
				
			}
 
			
			if(proc_status!=null) {
			
				if ( proc_status.trim().equals("0") || proc_status.trim().equals("5")) {
					
					
					sql = sql + " AND ch.use_id = ? ";
					valuesW.add(decl.collboeqbemapp.use_id);	
					
					sql = sql + " AND ch.org_uni_id = ? ";
					valuesW.add(decl.collboeqbemapp.org_uni_id);	
					
					
				}
				if ( proc_status.trim().equals("1") ) {
					
					sql = sql + " AND ch.org_uni_id = ? ";
					valuesW.add(decl.collboeqbemapp.org_uni_id);	
					
					
				}
				
				if(proc_status.trim().equals("3")){
					if(decl.collboeqbemapp.RealEstate_Qbe_ORG_UNI_ID2 != null ){
						sql = sql + " AND ch.origin_org_uni_id = ? ";
						valuesW.add(decl.collboeqbemapp.RealEstate_Qbe_ORG_UNI_ID2);	
					}
					if(decl.collboeqbemapp.RealEstate_QbeUSE_OPEN_ID2 != null ){
						sql = sql + " AND ch.use_open_id = ? ";
						valuesW.add(decl.collboeqbemapp.RealEstate_QbeUSE_OPEN_ID2);	
					}
				}
				if(proc_status.trim().equals("4")){
					if(decl.collboeqbemapp.RealEstate_Qbe_ORG_UNI_ID2 != null ){
						sql = sql + " AND ch.origin_org_uni_id = ? ";
						valuesW.add(decl.collboeqbemapp.RealEstate_Qbe_ORG_UNI_ID2);	
					}
					if(decl.collboeqbemapp.RealEstate_QbeUSE_OPEN_ID2 != null ){
						sql = sql + " AND ch.use_open_id = ? ";
						valuesW.add(decl.collboeqbemapp.RealEstate_QbeUSE_OPEN_ID2);	
					}
				}
				if(proc_status.trim().equals("M")){
					if(decl.collboeqbemapp.RealEstate_Qbe_ORG_UNI_ID2 != null ){
						sql = sql + " AND ch.origin_org_uni_id = ? ";
						valuesW.add(decl.collboeqbemapp.RealEstate_Qbe_ORG_UNI_ID2);	
					}
					if(decl.collboeqbemapp.RealEstate_QbeUSE_OPEN_ID2 != null ){
						sql = sql + " AND ch.use_open_id = ? ";
						valuesW.add(decl.collboeqbemapp.RealEstate_QbeUSE_OPEN_ID2);	
					}
				}
				
				
			}
			
			if(decl.collboeqbemapp.RealEstate_txtQbeDateFrom2 != null && decl.collboeqbemapp.RealEstate_txtQbeDateUntil2 != null ){
				
				sql = sql + " AND date(ch.opening_ts) between  ? AND ? ";
				valuesW.add(decl.collboeqbemapp.RealEstate_txtQbeDateFrom2);	
				valuesW.add(decl.collboeqbemapp.RealEstate_txtQbeDateUntil2);	
				
			}else{
				if(decl.collboeqbemapp.RealEstate_txtQbeDateFrom2 != null ){
					sql = sql + " AND date(ch.opening_ts) >=  ?  ";
					valuesW.add(decl.collboeqbemapp.RealEstate_txtQbeDateFrom2);	
				}
			
			}
			 
			 
	
			
			
			
			tc.info("p_owner " + p_owner);
			tc.info("p_carrier " + p_carrier);
			
			
			tc.info("decl.collboeqbemapp.RealEstate_QbeUSE_OPEN_ID2 " + decl.collboeqbemapp.RealEstate_QbeUSE_OPEN_ID2);
			tc.info("decl.collboeqbemapp.RealEstate_Qbe_ORG_UNI_ID2 " + decl.collboeqbemapp.RealEstate_Qbe_ORG_UNI_ID2);
			tc.info("decl.collboeqbemapp.RealEstate_QbeDateFrom2 " + decl.collboeqbemapp.RealEstate_txtQbeDateFrom2);
			tc.info("decl.collboeqbemapp.RealEstate_QbeDateUntil2 " + decl.collboeqbemapp.RealEstate_txtQbeDateUntil2);
			tc.info("\n ");
			tc.debug(sql);
			tc.info("\n ");

			prStmt = con.prepareStatement(sql);
			prStmt.clearParameters();
			
			

			
			for(int i1=0;i1<valuesW.size();i1++){  			
	  			
	  			 
	  			position = i1;   
	  			if(valuesW.elementAt(i1).getClass().getName().substring(valuesW.elementAt(i1).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("String")){  				
	  				prStmt.setString(position+1, ((String)valuesW.elementAt(i1)).replace('*', '%'));
	  				//prStmtCount.setString(position+1, ((String)values.elementAt(i)).replace('*', '%'));
	  			}else if(valuesW.elementAt(i1).getClass().getName().substring(valuesW.elementAt(i1).getClass().getName().trim().lastIndexOf(".")+1).trim().equals("BigDecimal")){  				
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
			
			rs = prStmt.executeQuery();
			
			
			
			
			
			
			
			
  										
		if(proc_status!=null) {
			if (proc_status.trim().equals("4")){
  				tc.info("tu sam .... proc_status : "+proc_status);
  				// lista ponistenih, jedna za banku
  				
				
				
  						
				// lista ponistenih po org.jed. - treba doadati ? 			
  	  		}//status 4
			if (proc_status.trim().equals("0")) {
				//referentska lista za unos, po referentu  			  
		  	 	tc.info("tu sam referentska lista.... proc_status : "+proc_status); 			
		  		tc.info("tu sam .... proc_status : "+decl.collboeqbemapp.use_id); 			
		  		tc.info("tu sam .... proc_status : "+decl.collboeqbemapp.org_uni_id); 			
		  				
		  			
		  							
		  	  					
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
	 	
		if(prStmt != null){
        	try{
        		prStmt.close();
        	}catch(SQLException e){
        	}	
	 	}	
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
      
       

		decl.collboeqbemapp.tblColWorkList.addRow(row, unique);
	 	

	 } //populateRowData
	 
	 
	 
	 
}
