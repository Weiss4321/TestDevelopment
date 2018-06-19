/*
 * Created on 2007.04.30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo08;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hr.vestigo.framework.remote.batch.BatchContext;

public class BO082 {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo08/BO082.java,v 1.1 2007/08/31 09:14:19 hratar Exp $";
	
	private ResultSet rs=null;
	
	public BO082() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultSet resultSetNekretnina(BatchContext bc, BO08Data p_data) throws Exception{
		String pomString=null;
		String query="";
		String sql="";

	
		
		try{
			bc.debug("Uzima se Connection iz BatchContext.");
			Connection con = bc.getConnection();
			bc.debug("con=" + con.toString());
			
			PreparedStatement prStmt = null;
			
			sql= "select " +  
					"a.COL_HEA_ID, " +
					"a.REAL_EST_NOMI_VALU, " +
					"a.REAL_EST_NM_CUR_ID, " +
					"a.REAL_EST_NOMI_DATE, " +
					"a.REVA_DATE_AM, " +
					"b.REVA_FLAG, " +
					"d.ACCOUNTING_INDIC " +
			"from " + 
			bc.getDefaultSchema() + "COLL_HEAD a, " + 
			bc.getDefaultSchema() + "COLL_RESTATE c, " +
			bc.getDefaultSchema() + "COLL_ATR b, " +
			bc.getDefaultSchema() + "COLL_CATEGORY d " +
			"where ";
			  
			bc.debug("SQL.");
			  
			query =  
			"a.col_cat_id = " + p_data.getCategory() + " and " +
			"a.COLLATERAL_STATUS = '3' " + 
			"and a.real_est_nomi_date >= '" + p_data.getDateFromEval() + "' and " +
			"a.real_est_nomi_date <= '" + p_data.getDateUntilEval() + "' " + 
			"and a.col_hea_id = c.col_hea_id " +
			"and a.col_cat_id = b.col_cat_id " +
			"and a.col_type_id = b.coll_type_id " +
			"and c.real_est_type = b.col_sub_id " +
			"and b.reva_flag = '0' " +
			"and b.status='A' and " +
			"b.date_from <= current date " +
			"and b.date_until = '9999-12-31' " +
			"and d.col_cat_id = a.col_cat_id ";
			
		 
			
			bc.debug("SQL prije nego se WHERE izgenerira.");
			bc.debug(sql);
			 
			
			if (p_data.getColl_type() != null) {
				query = query + " and a.col_type_id = " + p_data.getColl_type() + " ";
//				query = query + "and a.COL_TYPE_ID = b.COLL_TYPE_ID ";
//				query = query + " and a.col_hea_id = c.col_hea_id ";
//				query = query + "and b.col_sub_id = c.real_est_type ";
			}
			
			if (p_data.getColl_subtype() != null)
				query = query + " and c.real_est_type = " + p_data.getColl_subtype();
			
			
			if (p_data.getCounty() != null)
				query = query + " and a.COL_COUNTY = " + p_data.getCounty();
			
			
			
			
			if (p_data.getLocation() != null)
				query = query + " and a.COL_PLACE = " + p_data.getLocation();
			
			
			if (p_data.getDistrict() != null)
				query = query + " and a.COL_DISTRICT = " + p_data.getDistrict();
			
			
			sql =sql + query + " ";
			
			bc.debug("SQL nakon sto se WHERE izgenerira.");
			bc.debug(sql);
			System.out.println("SQL upit ....." + sql);					
			prStmt = con.prepareStatement(sql);
			prStmt.clearParameters();
		
			rs = prStmt.executeQuery();
			System.out.println("Velicina BO082" + rs.getFetchSize());
		}catch(SQLException sqle){
			if (sqle.getErrorCode()==100){
				bc.debug("Nije pronaden ni jedan zapis sa zadanim parametrima.");
			}else{
				throw sqle;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
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

	
}



