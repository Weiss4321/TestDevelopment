/*
 * Created on 2006.06.07
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Vector;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReSubTypeDialog extends Handler{

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ReSubTypeDialog.java,v 1.7 2006/06/14 12:27:41 hraamh Exp $";
	
	public ReSubTypeDialog(ResourceAccessor ra) {
		super(ra);
	}
	
	public void ReSubTypeDialog_SE(){

		ra.createLDB("ReSubTypeDialogLDB");
		String scr_ctxt = ra.getScreenContext().trim();
		if(scr_ctxt.equalsIgnoreCase("scr_add")){
			ra.setAttribute("ReSubTypeDialogLDB","STATUS","A");
			ra.setAttribute("ReSubTypeDialogLDB","ReSubTypeD_txtStatus","aktivan");
			ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtUseOpenLogin",(String)ra.getAttribute("GDB","Use_Login"));
			ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtUseOpenName",(String)ra.getAttribute("GDB","Use_UserName"));
		}else if(scr_ctxt.equalsIgnoreCase("scr_action") || scr_ctxt.equalsIgnoreCase("scr_details")){
			fillData();
		}
		if(scr_ctxt.equalsIgnoreCase("scr_action")){
		    
		    prepareForArchiving();
		}
	}
	
	public void confirm(){
        if (!(ra.isRequiredFilled())) {//provjerava dali su popunjena polja koja moraju biti, tj koja su != NULL
            return;
        }
        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
        } catch (VestigoTMException vtme) {
            error("SMEScoreBoardList -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
        ra.exitScreen();
        ra.refreshActionList("tblReSubType");
        //ra.invokeAction("refresh");
	}
	
	public void delete(){
		Integer del = (Integer) ra.showMessage("qzst2");
		if (del != null && del.intValue() == 1) {
			try {
				ra.executeTransaction();
				ra.showMessage("infcltzst1");
			} catch (VestigoTMException vtme) {
				error("SMEScoreBoardList -> insert(): VestigoTMException", vtme);
				if (vtme.getMessageID() != null)
					ra.showMessage(vtme.getMessageID());
			}
		}
        ra.exitScreen();
        ra.refreshActionList("tblReSubType");
        //ra.invokeAction("refresh");
	}
	
	public void change(){

        try {
            ra.executeTransaction();
            ra.showMessage("infclt3");
            
        } catch (VestigoTMException vtme) {
            error("SMEScoreBoardList -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
        ra.exitScreen();
        ra.refreshActionList("tblReSubType");
        //ra.invokeAction("refresh");
        //refresh nije jos implementirana 
	
	}
	
	private void fillData(){
		TableData td = (TableData) ra.getAttribute("ReSubTypeLDB","tblReSubType");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector data = (Vector) td.getSelectedRowData();
		

		
		//data
		ra.setAttribute("ReSubTypeDialogLDB","ReSubTypeD_txtCode",(String)data.elementAt(0));
		ra.setAttribute("ReSubTypeDialogLDB","ReSubTypeD_txtName",(String)data.elementAt(1));
		ra.setAttribute("ReSubTypeDialogLDB","ReSubTypeD_txtStatus",(String)data.elementAt(2));
		ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtUseName",(String)data.elementAt(3));
		
		//hidden
		ra.setAttribute("ReSubTypeDialogLDB","RE_SUB_TYPE_ID",(BigDecimal)hidden.elementAt(0));	
		ra.setAttribute("ReSubTypeDialogLDB","STATUS",(String)hidden.elementAt(1));
		ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtOpeningTS",(Timestamp)hidden.elementAt(3)); 
		ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtUseLock",(Timestamp)hidden.elementAt(4)); 
		ra.setAttribute("ReSubTypeDialogLDB","USER_LOCK",(Timestamp)hidden.elementAt(4));
		ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtUseOpenLogin",(String)hidden.elementAt(5));
		ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtUseOpenName",(String)hidden.elementAt(6));
		ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtUseLogin",(String)hidden.elementAt(7));
		ra.setAttribute("ReSubTypeDialogLDB","RealEstateDialog_txtUseName",(String)hidden.elementAt(8));		
		
	}
	
	//Poziva se da se napuni LBD starim vrijednostima koje ce se kod UPDATE i DELETE ubaciti u arhivsku tablicu
	private void prepareForArchiving(){
		TableData td = (TableData) ra.getAttribute("ReSubTypeLDB","tblReSubType");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector data = (Vector) td.getSelectedRowData();
	
		
		//data
		ra.setAttribute("ReSubTypeDialogLDB","RE_SUB_TYPE_CODE_a",(String)data.elementAt(0));
		ra.setAttribute("ReSubTypeDialogLDB","RE_SUB_TYPE_DESC_a",(String)data.elementAt(1));

		
		//hidden
		ra.setAttribute("ReSubTypeDialogLDB","RE_SUB_TYPE_ID_a",(BigDecimal)hidden.elementAt(0));	
		ra.setAttribute("ReSubTypeDialogLDB","RE_SUB_STATUS_a",(String)hidden.elementAt(1));
		ra.setAttribute("ReSubTypeDialogLDB","RE_SUB_SPEC_STAT_a",(String)hidden.elementAt(2));
		ra.setAttribute("ReSubTypeDialogLDB","OPENING_TS_a",(Timestamp)hidden.elementAt(3)); 
		ra.setAttribute("ReSubTypeDialogLDB","USER_LOCK_a",(Timestamp)hidden.elementAt(4)); 
		ra.setAttribute("ReSubTypeDialogLDB","USE_OPEN_ID_a",(BigDecimal)hidden.elementAt(9));
		ra.setAttribute("ReSubTypeDialogLDB","USE_ID_a",(BigDecimal)hidden.elementAt(10));
		ra.setAttribute("ReSubTypeDialogLDB","RE_TYPE_ID_a",(BigDecimal)hidden.elementAt(11));
	}
	
	
	/*
	//		RealEstateTypeDialog_CollateralType_FV
	public boolean RealEstateTypeDialog_CollateralType_FV(String ElName, Object ElValue, Integer LookUp) {
		if (ElValue == null || ((String) ElValue).equals("")) {
				ra.setAttribute("RealEstateTypeDialogLDB", "RealEstateDialog_txtCollCode", "");
				ra.setAttribute("RealEstateTypeDialogLDB", "RealEstateDialog_txtCollName", "");
				ra.setAttribute("RealEstateTypeDialogLDB", "COLL_TYPE_ID", null);
				ra.setAttribute("RealEstateTypeDialogLDB", "dummyBd", "");
				ra.setAttribute("RealEstateTypeDialogLDB", "dummySt", null);
				ra.setAttribute("RealEstateTypeDialogLDB", "dummyDate", null);
				ra.setAttribute("RealEstateTypeDialogLDB", "dummyBd", null);
				
				return true;
			}

		 	if (!ra.isLDBExists("CollateralTypeLookUpLDB")) {
		 		ra.createLDB("CollateralTypeLookUpLDB");
		 	}
		 	ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", "NEKR");
		 	
			LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp"); 
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "COLL_TYPE_ID", "coll_type_id");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "RealEstateDialog_txtCollCode", "coll_type_code");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "RealEstateDialog_txtCollName", "coll_type_name");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummySt", "coll_period_rev");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_mvp_ponder");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_mvp_ponder_mn");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_mvp_ponder_mx");
			
			
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_hnb_ponder");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_hnb_ponder_mn");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_hnb_ponder_mx");
			
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_rzb_ponder");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_rzb_ponder_mn");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyBd", "coll_rzb_ponder_mx");
			
			
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummySt", "coll_category");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummySt", "coll_hypo_fidu");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummySt", "hypo_fidu_name");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummySt", "coll_anlitika");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummySt", "coll_accounting");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummySt", "accounting_name");
			
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyDate", "coll_date_from");
			lookUpRequest.addMapping("RealEstateTypeDialogLDB", "dummyDate", "coll_date_until");
			
			try {
					ra.callLookUp(lookUpRequest);
			} catch (EmptyLookUp elu) {
					ra.showMessage("err012");
					return false;
			} catch (NothingSelected ns) {
					return false;
			}
			if(ra.getCursorPosition().equals("RealEstateDialog_txtCollCode")){
				ra.setCursorPosition(2);
			}
			if(ra.getCursorPosition().equals("RealEstateDialog_txtCollName")){
				ra.setCursorPosition(1);
			}	
			return true;
	}//RealEstateTypeDialog_CollateralType_FV
	*/
}
