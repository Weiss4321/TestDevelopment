/**
 * 
 */
package hr.vestigo.modules.collateral.jcics.co39;

import java.util.HashMap;
import java.util.Map;

import hr.vestigo.framework.remote.transaction.ConnCtx;
import hr.vestigo.framework.remote.transaction.RemoteTransaction;
import hr.vestigo.framework.remote.transaction.TransactionContext;

/**
 * @author hraamh
 *
 */
public abstract class AbstractLoxonRunnerData extends RemoteTransaction {
	
	public static String cvsident ="@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/AbstractLoxonRunnerData.java,v 1.4 2008/08/28 09:24:50 hraamh Exp $";
	
	
	protected DeclCO39 decl = null;
	private LoxonCommonInterfaceData runner=null;
	protected TransactionContext tc=null;
	protected ConnCtx connCtx;

	/* (non-Javadoc)
	 * @see hr.vestigo.framework.remote.transaction.RemoteTransaction#execute(hr.vestigo.framework.remote.transaction.TransactionContext)
	 */
	@Override
	public void execute(TransactionContext tc) throws Exception {
		this.tc=tc;
		connCtx=tc.getContext();
		
		/*
		 * inicijaliziranje runner objekta
		 */
		Class instanceClass = Class.forName("hr.vestigo.modules.collateral.jcics.co39.CO398");
		runner=(LoxonCommonInterfaceData) instanceClass.newInstance();
		runner.setContext(tc);
		
		run();
		resultToDecl();
	}
	
	private void resultToDecl(){
		
		Map response= new HashMap();		
		response.put("CollateralMainDetails", runner.transfortToTableData(runner.getCollateralMainDetails()));
		response.put("CollateralBillOfExchange", runner.transfortToTableData(runner.getCollateralBillOfExchange()));
		response.put("CollateralCashDeposit", runner.transfortToTableData(runner.getCollateralCashDeposit()));
		response.put("LongtermCollateralContract", runner.transfortToTableData(runner.getLongtermCollateralContract()));
		response.put("CollateralDebenture", runner.transfortToTableData(runner.getCollateralDebenture()));
		response.put("CollateralEvaluation", runner.transfortToTableData(runner.getCollateralEvaluation()));
		response.put("CollateralGuarantee", runner.transfortToTableData(runner.getCollateralGuarantee()));
		response.put("CollateralLifeInsurance", runner.transfortToTableData(runner.getCollateralLifeInsurance()));
		response.put("CollateralOwner", runner.transfortToTableData(runner.getCollateralOwner()));
		response.put("CollateralPlane", runner.transfortToTableData(runner.getCollateralPlane()));
		response.put("CollateralRealEstate", runner.transfortToTableData(runner.getCollateralRealEstate()));
		response.put("CollateralSecurity", runner.transfortToTableData(runner.getCollateralSecurity()));
		response.put("CollateralShareInCompany", runner.transfortToTableData(runner.getCollateralShareInCompany()));
		response.put("CollateralVehicles", runner.transfortToTableData(runner.getCollateralVehicles()));
		response.put("CollateralVessels", runner.transfortToTableData(runner.getCollateralVessels()));
		response.put("CollPledgeList", runner.transfortToTableData(runner.getCollPledgeList()));
		response.put("InsurancePolicy", runner.transfortToTableData(runner.getInsurancePolicy()));
		response.put("CollateralOtherColl", runner.transfortToTableData(runner.getCollateralOther()));
		
		DeclLoxonMappingInterface mapping= getMappingDecl();
		mapping.setResponseMap(response);
	}
	
	/**
	 * vraca obradni objekt za pozivanje dohvata skupa podataka
	 * 
	 * @return objekt nad kojim se poziva dohvat skupa podataka
	 */
	protected LoxonCommonInterfaceData getRunner(){
		return this.runner;
	}
	
	public abstract void run() throws Exception;
	
	protected abstract DeclLoxonMappingInterface getMappingDecl();
	
	protected String safeTrim(String input){
		String result= null;
		if(input!=null){
			result=input.trim();
		}		
		return result;
	}
	
	/**
	 * provjerava da li je ulazni string null ili blank
	 * 
	 * @param input ulazni string
	 * @return true ako je null ili blankovi
	 */
	protected boolean isEmpty(String input){
		if(input==null){
			return true;
		}else if(input.trim().equalsIgnoreCase("")){
			return true;
		}else{
			return false;
		}
	}

}
