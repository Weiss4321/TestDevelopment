/*
 * Created on 2006.08.04
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo00;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hr.vestigo.framework.remote.batch.BatchContext;

/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDBC {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo00/JDBC.java,v 1.6 2006/09/29 08:55:13 hrazst Exp $";
	/**
	 * 
	 */
	private ResultSet rs=null;
	
	public JDBC() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultSet resultSetNekretnina(BatchContext bc, String [] sqlParametri) throws Exception{
		String [] param=null;
		String pomString=null;
		String query="";
		String sql="";
		
		try{
			bc.debug("Uzima se Connection iz BatchContext.");
			Connection con = bc.getConnection();
			bc.debug("con=" + con.toString());
			
			PreparedStatement prStmt = null;
			
			sql="SELECT	" +
					"a.col_hea_id, " +
					"a.col_desc, " +
					"c.coll_type_code, " +
					"c.coll_type_name, " +
					"d.real_es_type_desc, " +
					"e.co_code, " +
					"e.co_name, " +
					"f.code, " +
					"f.name, " +
					"b.real_est_land_part, " +
					"b.real_est_land_regn, " +
					"b.real_est_land_sub, " +
					"b.build_perm_ind, " +
					"a.inspol_ind, " +
					"a.real_est_estn_valu, " +
					"b.new_build_val, " +
					"a.com_doc, " +
					"a.missing_doc, " +
					"a.date_to_doc, " +
					"a.date_rec_doc, " +
					"a.date_to_lop, " +
					"a.date_rec_lop, " +
					"a.rec_lop, " +
					"a.real_est_nomi_valu, " +
					"a.real_est_nomi_date, " +
					"b.rb_est_nom_val, " +
					"b.rb_est_nom_dat, " +
					"b.rl_est_nom_val,	" +
					"b.rb_est_nom_dat, " +
					"b.original_val, " +
					"b.orig_val_date, " +
					"b.building_val, " +
					"b.build_val_date, " +
					"a.real_est_add_data, " +
					"a.reva_coef, " +
					"a.reva_date, " +
					"a.reva_date_am, " +
					"a.reva_bvalue, " +
					"a.reva_bdate, " +
					"a.reva_bdate_am, " +
					"a.amort_age, " +
					"a.amort_per_cal_id, " +
					"a.amort_val_cal, " +
					"b.real_est_sqrm2, " +
					"a.eligibility, " +
					"g.login, " +
					"g.user_name, " +
					"h.code, " +
					"h.name, " +
					"curr.code_char, " +
					"a.col_num " +
				"FROM 	" +
					bc.getDefaultSchema()+ "coll_head a " +
					"INNER JOIN " + bc.getDefaultSchema() + "coll_restate b ON a.col_hea_id=b.col_res_id " +
					"INNER JOIN " + bc.getDefaultSchema() + "collateral_type c ON a.col_type_id=c.coll_type_id " +
					"INNER JOIN " + bc.getDefaultSchema() + "app_user g ON a.use_open_id=g.use_id " +
					"INNER JOIN " + bc.getDefaultSchema() + "coll_category h ON a.col_cat_id=h.col_cat_id " +
					"INNER JOIN " + bc.getDefaultSchema() + "currency curr ON a.real_est_nm_cur_id=curr.cur_id " +
					"LEFT OUTER JOIN " + bc.getDefaultSchema() + "coll_owner owner ON a.col_hea_id=owner.col_hea_id " +
					"INNER JOIN " + bc.getDefaultSchema() + "real_estate_type d ON b.real_est_type=d.real_es_type_id " +
					"INNER JOIN " + bc.getDefaultSchema() + "coll_court e ON b.real_est_court_id=e.co_id " +
					"INNER JOIN " + bc.getDefaultSchema() + "cadastre_map f ON b.real_est_cada_munc=f.cad_map_id " +
					"LEFT OUTER JOIN " + bc.getDefaultSchema() + "coll_hf_prior chp ON a.col_hea_id = chp.hf_coll_head_id " +
					"LEFT OUTER JOIN " + bc.getDefaultSchema() + "loan_beneficiary lobe ON chp.coll_hf_prior_id = lobe.coll_hf_prior_id " +
				"WHERE ";
			
			bc.debug("SQL prije nego se WHERE izgenerira.");
			bc.debug(sql);
			
			//param=parametri.split(";");
			param = sqlParametri;

			if (param[0].compareToIgnoreCase("M")==0 || param[0].compareToIgnoreCase("R")==0){
				query="a.collateral_status='" + 0 +"'";
			}else{
				query="a.collateral_status='" + 3 +"'";
			}
			bc.debug("Collateral_status proso. " + param[0]);
			
			//kategorija collaterala
			if (param[1].trim().compareTo("")!=0){
				query= query + " AND h.code='" + param[1] +"'";
			}
			bc.debug("Code(kategorija kolaterala) proso. " + param[1]);
			//vrsta nekretnine
			if (param[2].trim().compareTo("")!=0){
				query= query + " AND c.coll_type_code='" + param[2] +"'";
			}
			bc.debug("Coll_type_code proso. " + param[2]);
			
			//sifra nekretnine
			if (param[3].trim().compareTo("")!=0){
				pomString=param[3].replace('*','%'); 
				if (param[3].indexOf('*')==-1){
					query= query + " AND c.coll_type_code = '" + pomString + "'";
				}else{
					query= query + " AND c.coll_type_code LIKE '" + pomString + "'";
				}
			}
			bc.debug("coll_type_code proso. "  + param[3]);
			
			//vlasnik collaterala
			if (param[4].trim().compareTo("")!=0){
				query= query + " AND owner.register_no='" + param[4] +"'";
			}
			bc.debug("Owner.register_no proso. "  + param[4]);
			
			//korisnik plasmana
			if (param[5].trim().compareTo("")!=0){
				query= query + " AND lobe.register_no='" + param[5] +"'";
			}	
			bc.debug("Lobe.register_no proso. "  + param[5]);
			
			//broj ZKU
			if (param[6].trim().compareTo("")!=0){
				pomString=param[6].replace('*','%'); 
				if (param[6].indexOf('*')==-1){
					query= query + " AND b.real_est_land_regn = '" + pomString + "'";
				}else{
					query= query + " AND b.real_est_land_regn LIKE '" + pomString + "'";
				}
			}
			bc.debug("Real_est_land_regn proso. "  + param[6]);
			
			//podulozak
			if (param[7].trim().compareTo("")!=0){
				pomString=param[7].replace('*','%'); 
				if (param[7].indexOf('*')==-1){
					query= query + " AND b.real_est_land_sub = '" + pomString + "'";
				}else{
					query= query + " AND b.real_est_land_sub LIKE '" + pomString + "'";
				}
			}
			bc.debug("Real_est_land_sub proso. "  + param[7]);
			
			//katastarska opcina
			if (param[8].trim().compareTo("")!=0){
				query= query + " AND b.real_est_cada_munc =" + param[8].trim();
			}
			bc.debug("Real_est_land_cada proso. "  + param[8]);
			
			//suvlasnicki udio
			if (param[9].trim().compareTo("")!=0){
				pomString=param[9].replace('*','%'); 
				if (param[9].indexOf('*')==-1){
					query= query + " AND b.coown = '" + pomString + "'";
				}else{
					query= query + " AND b.coown LIKE '" + pomString + "'";
				}
			}
			bc.debug("Coown proso. "  + param[9]);
			
			//partija plasmana
			if (param[10].trim().compareTo("")!=0){
				pomString=param[10].replace('*','%'); 
				if (param[10].indexOf('*')==-1){
					query= query + " AND lobe.acc_no = '" + pomString + "'";
				}else{
					query= query + " AND lobe.acc_no LIKE '" + pomString + "'";
				}
			}	
			bc.debug("Acc_no proso. "  + param[10]);
			
			//organizacijska jedinica
			if (param[11].trim().compareTo("")!=0){
				query= query + " AND a.origin_org_uni_id =" + param[11].trim();
			}
			bc.debug("Origin_org_uni_id proso. " + param[11]);
			//referent unosa
			if (param[12].trim().compareTo("")!=0){
				query= query + " AND a.use_open_id =" + param[12].trim();
			}
			bc.debug("Use_open_id proso. "  + param[12]);
			
			//datum od do (param[13]-od, param[14]-do)
			if (param[13].trim().compareTo("")!=0 && param[14].trim().compareTo("")!=0){
		        Date dateFrom = convertStringToDate(param[13].trim());
		        Date dateUntil = convertStringToDate(param[14].trim());
				query= query + " AND date(a.opening_ts) between '" + dateFrom + "'  AND '" + dateUntil + "'";
			}else{
				if (param[13].trim().compareTo("")!=0){
					Date dateFrom = convertStringToDate(param[13].trim());
					query= query + " AND date(ch.opening_ts) >= '" + dateFrom + "'";
				}
			}
			bc.debug("Datume proso. "  + param[13] + " - "  + param[14]);
			
			sql =sql + query + " ORDER BY a.col_hea_id asc ";
			
			bc.debug("SQL nakon sto se WHERE izgenerira.");
			bc.debug(sql);
			
			prStmt = con.prepareStatement(sql);
			prStmt.clearParameters();
		
			rs = prStmt.executeQuery();
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

	private Date convertStringToDate(String dataString){
		
		String pomString=null;
		String dan=null;
		String mjesec=null;
		String godina=null;
		
		dan=dataString.substring(0,2);
		mjesec=dataString.substring(3,5);
		godina=dataString.substring(6,10);
		
		pomString=godina + "-" + mjesec + "-" + dan;
		return Date.valueOf(pomString);
		
	}
}


