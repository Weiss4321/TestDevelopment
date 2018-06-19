package hr.vestigo.modules.collateral.jcics.co39;
import java.util.Iterator;  
import java.util.Vector;

import hr.vestigo.framework.common.TableData;



/**
 * Runner za base mapping getCollateralsByCollateralIDList
 *
*/   

public class CO391 extends AbstractLoxonRunnerData{
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/jcics/co39/CO391.java,v 1.2 2008/05/21 11:39:08 hraamh Exp $";
	
	public CO391(DeclCO39 decl){
		this.decl=decl;
	}
	
	public void run() throws Exception{
		TableData collateralsTD= decl.collateralCMIDList;		
		Vector list= collateralsTD.getData();
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				Vector row=(Vector)list.get(i);
				String col_num=(String)row.firstElement();
				getRunner().gatherByColNum(col_num);
			}
		}
	}
	
	protected  DeclLoxonMappingInterface getMappingDecl(){
		return decl;
	}
	
}