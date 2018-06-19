/*
 * Created on 2006.05.15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.NothingSelected;


/**
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RealEstateTypeDialog extends Handler {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/RealEstateTypeDialog.java,v 1.7 2006/06/30 14:29:18 hrajkl Exp $";
	
	public RealEstateTypeDialog(ResourceAccessor ra) {
		super(ra);
	}
	
	public void RealEstateTypeDialog_SE(){
		
		ra.createLDB("RealEstateTypeDialogLDB");
		String scr_ctxt = ra.getScreenContext().trim();
		if(scr_ctxt.equalsIgnoreCase("scr_insert")){
			ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtRealEsActNoact","A");
			ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtStatus","aktivan");
			ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtUseOpenLogin",(String)ra.getAttribute("GDB","Use_Login"));
			ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtUseOpenName",(String)ra.getAttribute("GDB","Use_UserName"));
		}else if(scr_ctxt.equalsIgnoreCase("scr_update") || scr_ctxt.equalsIgnoreCase("scr_detail")){
			fillData();
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
            error("SMEScoreBoardList -> confirm(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
        ra.exitScreen();
        ra.refreshActionList("tblRealEstateType");
        //ra.invokeAction("refresh");
	}
	
	public void delete(){
		Integer del = (Integer) ra.showMessage("qzst2");
		if (del != null && del.intValue() == 1) {
			try {
				ra.executeTransaction();
				ra.showMessage("infcltzst1");
			} catch (VestigoTMException vtme) {
				error("SMEScoreBoardList -> delete(): VestigoTMException", vtme);
				if (vtme.getMessageID() != null)
					ra.showMessage(vtme.getMessageID());
			}
		}
        ra.exitScreen();
        ra.refreshActionList("tblRealEstateType");
        //ra.invokeAction("refresh");
	}//scr_delete za transakciju
	
	public void change(){
		if (!(ra.isRequiredFilled())) {//provjerava dali su popunjena polja koja moraju biti, tj koja su != NULL
            return;
        }
		
		Integer update = (Integer) ra.showMessage("qer002");//poruka o promjeni zapisa
		if (update != null && update.intValue() == 1) {
			try {
				ra.executeTransaction();
				ra.showMessage("infclt3");
			} catch (VestigoTMException vtme) {
				error("SMEScoreBoardList -> change(): VestigoTMException", vtme);
				if (vtme.getMessageID() != null)
					ra.showMessage(vtme.getMessageID());
			}
		}
        ra.exitScreen();
        ra.refreshActionList("tblRealEstateType");
        //ra.invokeAction("refresh");
	
	}//scr_update za transakciju
	
	private void fillData(){
		TableData td = (TableData) ra.getAttribute("RealEstateTypeLDB","tblRealEstateType");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector data = (Vector) td.getSelectedRowData();
		
		//data
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtCode",(String)data.elementAt(0));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtDesc",(String)data.elementAt(1));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtCollName",(String)data.elementAt(2));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtStatus",(String)data.elementAt(3));
		
		//hidden
		ra.setAttribute("RealEstateTypeDialogLDB","COLL_TYPE_ID",(BigDecimal)hidden.elementAt(0));	
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtCollCode",(String)hidden.elementAt(1));
		ra.setAttribute("RealEstateTypeDialogLDB","REAL_ES_TYPE_ID",(BigDecimal)hidden.elementAt(2));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtRealEsActNoact",(String)hidden.elementAt(3));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtUseOpenLogin",(String)hidden.elementAt(5));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtUseOpenName",(String)hidden.elementAt(6));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtUseLogin",(String)hidden.elementAt(7));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtUseName",(String)hidden.elementAt(8));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtOpeningTS",(Timestamp)hidden.elementAt(9)); 
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtUseLock",(Timestamp)hidden.elementAt(10)); 
		ra.setAttribute("RealEstateTypeDialogLDB","USER_LOCK",(Timestamp)hidden.elementAt(10));
	//ra.showMessage("wrnretd01");
		
		//potreban USER_LOCK zato sto kod prikaza na ekranu, ne prikazuje se potpuni format, pa dolazi do gubitka podataka
		ra.setAttribute("RealEstateTypeDialogLDB","USE_OPEN_ID",(BigDecimal)hidden.elementAt(11));
		ra.setAttribute("RealEstateTypeDialogLDB","USE_ID",(BigDecimal)hidden.elementAt(12));
		
		
		//old
		ra.setAttribute("RealEstateTypeDialogLDB","COLL_TYPE_ID_O",(BigDecimal)hidden.elementAt(0));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtRealEsActNoact_O",(String)hidden.elementAt(3));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtCode_O",(String)data.elementAt(0));
		ra.setAttribute("RealEstateTypeDialogLDB","RealEstateDailog_txtDesc_O",(String)data.elementAt(1));
		
	}
	
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
	
	
	public boolean RealEstateDialog_txtDateFrom_FV(String elementName, Object elementValue, Integer lookUpType) {
		//provjera postoji li unesen datumOd
		if (elementValue != null) {
			
			//findLDB();
			String ldbName = "RealEstateTypeDialogLDB";
			//provjera da li postoji datumDo
			if (ra.getAttribute(ldbName, "RealEstateDialog_txtDateUntil") != null) {
				//kontrola da li je datumDo prije datumOd, u tom slucaju je validacija false i prikazuje se poruka
				if (((java.sql.Date) ra.getAttribute(ldbName, "RealEstateDialog_txtDateUntil"))
					.before((java.sql.Date) ra.getAttribute(ldbName, "RealEstateDialog_txtDateFrom"))) {
					ra.showMessage("wrn104");
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean RealEstateDialog_txtDateUntil_FV(String elementName, Object elementValue, Integer lookUpType) {
		//provjera postoji li unesen datumDo
		if (elementValue != null) {
			//findLDB();
			String ldbName = "RealEstateTypeDialogLDB";
			//provjera da li postoji datumOd
			if (ra.getAttribute(ldbName, "RealEstateDialog_txtDateFrom") != null) {
				//kontrola da li je datumDo prije datumOd, u tom slucaju je validacija false i prikazuje se poruka
				if (((java.sql.Date) ra.getAttribute(ldbName, "RealEstateDialog_txtDateUntil"))
					.before((java.sql.Date) ra.getAttribute(ldbName, "RealEstateDialog_txtDateFrom"))) {
					ra.showMessage("wrn104");
					return false;
				}
			}
		}
		return true;
	}
	
//	provjera dali je datum od manji od trenutnog, taj se unos ne dopusta
	public boolean CheckDateFrom(){
		Date from=(Date) ra.getAttribute("RealEstateTypeDialogLDB","RealEstateDialog_txtDateFrom");
        Date currentDate= (Date) ra.getAttribute("GDB","ProcessingDate"); 
        	//if(from != null){ // puca na provjeri ako je prazno polje
        		if((from != null) && from.before(currentDate)){
        			return false;
        		}
        	//}
		return true;
	}

}

