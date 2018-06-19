/*
 * Created on 2006.05.15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
/**
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EngineType extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/EngineType.java,v 1.3 2006/10/16 11:50:15 hrajkl Exp $";
	
	public EngineType(ResourceAccessor ra) {
		super(ra);
	}
	
	public void EngineType_SE(){
		ra.createLDB("EngTLDB");
		ra.createActionListSession("tblEngineType");
	}
	

}
