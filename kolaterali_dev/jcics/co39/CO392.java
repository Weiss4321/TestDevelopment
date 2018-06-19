package hr.vestigo.modules.collateral.jcics.co39;

import java.sql.SQLException;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;

	/**
	* mapping getAccountLinkedCollateralsByCustomerIDList
	*
	*/
public class CO392 extends AbstractLoxonRunnerData{
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/CO392.java,v 1.2 2008/05/21 11:39:08 hraamh Exp $";
	
	
	public CO392(DeclCO39 decl){
		this.decl=decl;
	}


	public void run() throws Exception{
		
		TableData customers=decl.getaccountlinkedcollateralsbycustomeridlist.customerIDList;
		Vector list= customers.getData();
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				Vector row=(Vector)list.get(i);
				String register_no=(String)row.firstElement();
				getRunner().gatherByExposureUserId(register_no);
			}				
		}
		
		System.out.println(getRunner().finalResult());
		
	}
	
	protected  DeclLoxonMappingInterface getMappingDecl(){
		return (decl.getaccountlinkedcollateralsbycustomeridlist);
	}
	
}