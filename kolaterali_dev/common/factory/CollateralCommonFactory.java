/*
 * Created on 2007.11.26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.common.factory;

import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.modules.collateral.common.interfaces.CollateralInsuranceInstrument;
import hr.vestigo.modules.collateral.common.interfaces.CollateralPosting;
import hr.vestigo.modules.collateral.common.interfaces.CommonCollateralMethods;
import hr.vestigo.modules.collateral.common.interfaces.DealCollateralCoverage;
import hr.vestigo.modules.rba.interfaces.AbstractCommonInterface;


/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollateralCommonFactory {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/common/factory/CollateralCommonFactory.java,v 1.4 2008/03/05 09:09:40 hraamh Exp $";
	
	/**
	 * Instanciranje objekta za dohvat instrumenata osiguranja za plasman
	 * 
	 * @param context remote context
	 * @return
	 */
	public static CollateralInsuranceInstrument getCollateralInsuranceInstrument(RemoteContext context) throws Exception{
		Class instanceClass = Class.forName("hr.vestigo.modules.collateral.common.yoyB.YOYB0");
		CollateralInsuranceInstrument instance=(CollateralInsuranceInstrument) instanceClass.newInstance();
		instance.setContext(context);
		return instance;
	}
	
	/**
	 * Instanciranje objekta za dohvat instrumenata osiguranja za plasman. Nakon dohvata potrebno je pozvati init(...) metodu
	 * 
	 * @param context remote context
	 * @return
	 */
	public static DealCollateralCoverage getDealCollateralCoverage(RemoteContext context) throws Exception{
		Class instanceClass = Class.forName("hr.vestigo.modules.collateral.common.yoy8.YOY80");
		DealCollateralCoverage instance=(DealCollateralCoverage) instanceClass.newInstance();
		instance.setContext(context);
		return instance;
	}
	
	/**
	 * Instanciranje objekta za knjizenje i isknjizavanje kolaterala
	 * 
	 * @param context remote context
	 * @return instanca objekta
	 * @throws Exception
	 */
	public static CollateralPosting getCollateralPosting(RemoteContext context) throws Exception{
		Class instanceClass = Class.forName("hr.vestigo.modules.collateral.common.yoy9.YOY90");
		CollateralPosting instance=(CollateralPosting) instanceClass.newInstance();
		instance.setContext(context);
		return instance;
	}
	
	/**
	 * Dohvat instance objekta preko punog imena.
	 * 
	 * @param context kontekst
	 * @param instanceName puno ime instance
	 * @return interface koji je potrebno kastati na podinterface koji se direktno veze na instancu
	 * @throws Exception
	 */
	public AbstractCommonInterface getInstanceByName(RemoteContext context, String instanceName) throws Exception{
		Class instanceClass = Class.forName(instanceName);
		AbstractCommonInterface instance=(AbstractCommonInterface) instanceClass.newInstance();
		instance.setContext(context);
		return instance;
	}
	
	public static CommonCollateralMethods getCommonCollateralMethods(RemoteContext context) throws Exception{
		Class instanceClass = Class.forName("hr.vestigo.modules.collateral.common.yoyC.YOYC0");
		CommonCollateralMethods instance=(CommonCollateralMethods) instanceClass.newInstance();
		instance.setContext(context);
		return instance;
	}
}
