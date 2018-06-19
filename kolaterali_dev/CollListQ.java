/*
 * Created on 2006.06.12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;
 
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.scramble.Scramble;
/**
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollListQ extends Handler {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollListQ.java,v 1.6 2016/12/05 09:54:32 hrazst Exp $";
	/**
	 * Constructor for PaymentListQ.
	 */
	public CollListQ(ResourceAccessor ra) {
		super(ra);
	}
	
	  
	public boolean CollListQ_txaCmnt_FV(){
		if(ra.getAttribute("CollListQ_txaCmnt")==null || ((String)ra.getAttribute("CollListQ_txaCmnt")).trim().equals("")){
			ra.showMessage("err10128");
			return false;
		}
		String cmnt=(new Scramble()).doScramble((String)ra.getAttribute("CollListQ_txaCmnt"));
		if(cmnt==null || cmnt.trim().equals("")){
			ra.showMessage("err10129");
			return false;
		}
		return true;
	}	
	

	public void stop() {
	    //akcija ponisti collateral na ekranu za upis komentara
			if(ra.getAttribute("CollListQ_txaCmnt")==null || ((String)ra.getAttribute("CollListQ_txaCmnt")).trim().equals("")){
				ra.showMessage("err10128");
				return;
			}
			try{
				ra.executeTransaction();  // scr_stop:CollWorkListStop; scr_back:CollSndBack; scr_back_all:CollSndBackAll
			} catch (VestigoTMException vtme) {
				if (vtme.getMessageID() != null)
					ra.showMessage(vtme.getMessageID());
			}
			
			ra.exitScreen();
			ra.setAttribute("ColWorkListLDB", "col_hea_id", null);
			ra.setAttribute("ColWorkListLDB", "CollListQ_txaCmnt", "");
			ra.setAttribute("ColWorkListLDB", "listSelected", new HashMap<BigDecimal, Timestamp>());
			ra.queueAction("refresh");
	}
	  
	 
	public void discard_agr() {
// ponisti okvirni ugovor
		if(ra.getAttribute("CollListQ_txaCmnt")==null || ((String)ra.getAttribute("CollListQ_txaCmnt")).trim().equals("")){
			ra.showMessage("err10128");
			return;
		}
		try{
			ra.executeTransaction();  // scr_stop:AgrListStop; scr_back:AgrSndBack; 
		} catch (VestigoTMException vtme) {
			if (vtme.getMessageID() != null)
				ra.showMessage(vtme.getMessageID());
		}
		
		ra.exitScreen();
		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", null);
		ra.setAttribute("AgreementListLDB", "CollListQ_txaCmnt", "");
		ra.queueAction("refresh");  		
	}
 
}



