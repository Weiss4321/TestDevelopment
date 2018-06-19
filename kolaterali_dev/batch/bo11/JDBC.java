/*
 * Created on 2007.07.02
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo11;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hr.vestigo.framework.remote.batch.BatchContext;
/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDBC {
    

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo11/JDBC.java,v 1.2 2008/01/03 08:52:07 hraaks Exp $";
	
	private ResultSet rs = null;
	
	public JDBC() {
		super();
	}
	/** odabir komitenta ili svih komitenata (pravne osobe) koji ulaze u izvjesce 
	 * i pripadajucih im plasmana
	 * */
	public ResultSet resultSetClientAndInvestment(BatchContext bc, BO11Data p_data) throws Exception {
		String sql = "";
		try {
			PreparedStatement prStmt = null;
			bc.debug("Uzima se Connection iz BatchContext-a.");
			Connection con = bc.getConnection();
			bc.debug("con = " + con.toString());
			
			
			sql = "SELECT " +
					"a.cus_id, " +
					"a.col_hea_id," +
					"c.register_no, " +
					"c.name, " +
					"b.module_code," +
					"f.code, " +
					"b.cus_acc_no," +
					"c.eco_sec," +
					"b.b2asset_class," +
					"b.customer_indic," +
					"b.due_date," +
					"b.approval_date," +
					"b.currency_type, " +
					"b.contract_cur_id ," +
					"g.code_char," +
					"b.exposure_bal_lcy," +
					"b.exp_off_bal_lcy," +
					"a.exp_coll_amount,  " +
					"a.exp_balance_hrk, " +
					"d.col_cat_id, " +
					"d.col_type_id," +
					"d.REAL_EST_NM_CUR_ID " +
				" FROM  COL_PROC e," +
					"CUSACC_EXP_COLL a, " +
					"CUSTOMER c, " +
					"CUSACC_EXPOSURE b, " +
					"COLL_HEAD d," +
					"ORGANIZATION_UNIT f," +
					"CURRENCY g " +
				" WHERE  e.value_date = :(p_data.getDateOfReview()) AND " +
					"e.proc_type = 'N' AND " +
					"e.col_pro_id = a.col_pro_id AND " +
					"a.cus_id = c.cus_id AND " +
					"a.cus_acc_id = b.cus_acc_id AND " +
					"a.col_hea_id = d.col_hea_id  AND " +
					"d.collateral_status = '3' AND " +
					"b.org_uni_id = f.org_uni_id AND " +
					"b.contract_cur_id = g.cur_id";


			bc.debug("SQL prije nego se WHERE izgenerira.");
			bc.debug(sql);
			
			if (p_data.getRegisterNo()!=null && !p_data.getRegisterNo().trim().equals(""))
				sql = sql + " AND a.cus_id = " + p_data.getRegisterNo(); 
			
			if (p_data.getClientGroup().trim().equals("P"))	
				sql = sql + " AND c.cus_typ_id in (2999,2998,999) ";
			
			if (p_data.getClientGroup().trim().equals("F"))
				sql = sql + " AND c.cus_typ_id in (1998,1999) ";
				
			if (p_data.getCollCategoryCode() !=null)
				sql = sql + " AND d.col_cat_id = " + p_data.getCollCategoryCode();
			
			if (p_data.getCollTypeCode() !=null)
				sql = sql + " AND d.col_type_id = " + p_data.getCollTypeCode();
			
				
		    bc.debug("SQL nakon sto se WHERE izgenerira.");
			bc.debug(sql);
			System.out.println("SQL upit ....." + sql);		
			prStmt = con.prepareStatement(sql);
			prStmt.clearParameters();
			
			rs = prStmt.executeQuery();
			System.out.println("Velicina JDBC" + rs.getFetchSize());
			return rs;
		}
		catch(SQLException sqle) {
//			Row not found for ... or result of a query empty.
			if (sqle.getErrorCode() == 100)
				bc.debug("Nije pronaden ni jedan zapis sa zadanim parametrima.");
			else throw sqle;
				
			//mislim da je ovo u najmanju ruku nije bilo profesionalono
            //odradjeno jel mi se cini da je problem u tome sto mi se nista
            //pametno ne radi
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		System.out.println("JDBC->return null");
		return null;
	}
	
}
