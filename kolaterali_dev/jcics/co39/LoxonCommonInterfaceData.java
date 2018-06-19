package hr.vestigo.modules.collateral.jcics.co39;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.remote.RemoteContext;

public interface LoxonCommonInterfaceData {
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/LoxonCommonInterfaceData.java,v 1.1 2008/06/10 08:34:33 hraamh Exp $";
	/**
	 * Postavljanje conteksta nakon default konstrukcije
	 * 
	 * @param context
	 * @throws SQLException
	 */
	public void setContext(RemoteContext context) throws SQLException;
	/**
	 * dohvat skupa podataka preko col_hea_id-a
	 * 
	 * @param col_hea_id
	 * @throws SQLException
	 */
	public void gatherByColHeaId(BigDecimal col_hea_id) throws SQLException;
	/**
	 * dohvat skupa podataka preko broja kolaterala
	 * 
	 * @param col_num
	 * @throws SQLException
	 */
	public void gatherByColNum(String col_num) throws SQLException;
	/**
	 * dohvat skupa podataka vezanog preko plasmana i pripadnog vlasnika plasmana (register_no)
	 * 
	 * @param register_no
	 * @throws SQLException
	 */
	public void gatherByExposureUserId(String register_no) throws SQLException;
	/**
	 * pretvorba Vectora u TableData
	 * 
	 * @param input
	 * @return
	 */
	public TableData transfortToTableData(Vector input);
	/**
	 * Ispis konacnog skupa u string
	 * 
	 * @return
	 */
	public String finalResult();
	
	public Vector getCollateralMainDetails();
	
	public Vector getCollateralBillOfExchange();
	
	public Vector getCollateralCashDeposit();
	
	public Vector getLongtermCollateralContract();
	
	public Vector getCollateralDebenture();
	
	public Vector getCollateralEvaluation();
	
	public Vector getCollateralGuarantee();
	
	public Vector getCollateralLifeInsurance();
	
	public Vector getCollateralOwner();
	
	public Vector getCollateralPlane();
	
	public Vector getCollateralRealEstate();
	
	public Vector getCollateralSecurity();
	
	public Vector getCollateralShareInCompany();
	
	public Vector getCollateralVehicles();
	
	public Vector getCollateralVessels();
	
	public Vector getCollPledgeList();
	
	public Vector getInsurancePolicy();
	
	public Vector getCollateralOther();

}
