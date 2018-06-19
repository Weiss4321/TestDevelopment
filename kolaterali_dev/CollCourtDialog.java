/*
 * Created on 2006.06.27
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
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author hraaks
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CollCourtDialog extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollCourtDialog.java,v 1.4 2006/07/11 14:28:45 hraaks Exp $";
	/**
	 * @param arg0
	 */
	public CollCourtDialog(ResourceAccessor res) {
		super(res);
		
	}
	/*
     * prva funkcija koja se poziva na dijalogom
     * String screen_ctx = ra.getScreenContext().trim();
     * cita trenutni kontekst ekrana i s obzirom na kontekst ekrana
     * popunjava polja koja ce se vidjeti na ekranu i skrivena polja koja ce se koristiti prilikom
     * azuriranja u bazu
     */
	public void CollCourtDialog_SE()
	{
		
		if (!ra.isLDBExists("CollCourtDialogLDB")) {
			ra.createLDB("CollCourtDialogLDB");
		}
		String screen_ctx = ra.getScreenContext().trim();
		if("scr_detail".equalsIgnoreCase(screen_ctx)){
			System.out.println("DETAIL");
			fillData();
		}
		else if("scr_insert".equalsIgnoreCase(screen_ctx)){
			ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtCoStatus","A");
			ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtCoStatusDesc","Aktivan");
			ra.setAttribute("CollCourtDialogLDB","RealEstateDialog_txtUseOpenLogin",(String)ra.getAttribute("GDB","Use_Login"));
			ra.setAttribute("CollCourtDialogLDB","RealEstateDialog_txtUseOpenName",(String)ra.getAttribute("GDB","Use_UserName"));
			
		}
		else if("scr_update".equalsIgnoreCase(screen_ctx)){
			System.out.println("UPDATE");
			fillData();
			prepareForArchiving();
		}
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
        //ra.refreshActionList("tblCollCourt");
        ra.invokeAction("refresh");
	}
	/**
	 * prilom inserta u bazu poziva se confirm funcija 
	 * kojase definira u PDF readeru pod granom action
	 *
	 */
	
	public void confirm(){
		if(!ra.isRequiredFilled()){
			return ;			
		}
		else
		{
			 try {
	            ra.executeTransaction();
	           
	            ra.showMessage("infclt2");
	        } catch (VestigoTMException vtme) {
	            error("SMEScoreBoardList -> insert(): VestigoTMException", vtme);
	            if (vtme.getMessageID() != null)
	                ra.showMessage(vtme.getMessageID());
	        }
	        ra.exitScreen();
	        //ra.refreshActionList("tblCollCourt");
	        ra.invokeAction("refresh");
		}
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
        //ra.refreshActionList("tblCollCourt");
        ra.invokeAction("refresh");
	}
	/**
	 * prilikom prikaza detalja i updatea napune se polja koja ce se mijenjati
	 * 
	 *
	 */
	private void fillData(){
		
		TableData td = (TableData) ra.getAttribute("CollCourtLDB","tblCollCourt");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector data = (Vector) td.getSelectedRowData();
		
		
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtCoCode",(String)data.elementAt(0));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtCoName",(String)data.elementAt(1));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtCoStatusDesc",(String)data.elementAt(2));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtCoStatus",(String)hidden.elementAt(12));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtCodeCnt",(BigDecimal)hidden.elementAt(6));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtNameCnt",(String)hidden.elementAt(7));
		ra.setAttribute("CollCourtDialogLDB","RealEstateDialog_txtUseOpenName",(String)hidden.elementAt(8));
		ra.setAttribute("CollCourtDialogLDB","RealEstateDialog_txtUseOpenLogin",(String)hidden.elementAt(9));
		ra.setAttribute("CollCourtDialogLDB","RealEstateDialog_txtUseLogin",(String)hidden.elementAt(10));
		ra.setAttribute("CollCourtDialogLDB","RealEstateDialog_txtUseName",(String)hidden.elementAt(11));		

		
		//hidden
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_CO_ID",(BigDecimal)hidden.elementAt(0));	
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_POL_MAP_ID",(BigDecimal)hidden.elementAt(1));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_USE_OPEN_ID",(BigDecimal)hidden.elementAt(2));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_USE_ID",(BigDecimal)hidden.elementAt(3));
		ra.setAttribute("CollCourtDialogLDB","RealEstateDialog_txtOpeningTS",(Timestamp)hidden.elementAt(4));
		ra.setAttribute("CollCourtDialogLDB","RealEstateDialog_txtUseLock",(Timestamp)hidden.elementAt(5));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtCodeCnt",(BigDecimal)hidden.elementAt(6));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txtNameCnt",(String)hidden.elementAt(7));

	}
	/**
	 * ovi podaci se ubacuju u arhivsku tablicu prilikom brisanja i azuriranja zapisa 
	 *
	 */
	private void prepareForArchiving(){
		TableData td = (TableData) ra.getAttribute("CollCourtLDB","tblCollCourt");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector data = (Vector) td.getSelectedRowData();
		
		
		//data
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txCoCode_a",(String)data.elementAt(0));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_txCoName_a",(String)data.elementAt(1));

		
		//hidden
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_CO_ID_a",(BigDecimal)hidden.elementAt(0));	
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_POL_MAP_ID_a",(BigDecimal)hidden.elementAt(1));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_USE_OPEN_ID_a",(BigDecimal)hidden.elementAt(2));
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_USE_ID_a",(BigDecimal)hidden.elementAt(3)); 
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_OPENING_TS_a",(Timestamp)hidden.elementAt(4)); 
		ra.setAttribute("CollCourtDialogLDB","CollCourtDialog_USER_LOCK_a",(Timestamp)hidden.elementAt(5)); 
		
	}
	
//	look up za poziv odabira zupanija
	public boolean CollCourtDialog_txtCodeCnt_FV(String elementName, Object elementValue, Integer LookUp){
        
		java.math.BigDecimal countyTypeId = new java.math.BigDecimal("3999.0");
			
		if (elementValue == null || ((String) elementValue).equals("")) {
		     ra.setAttribute("CollCourtDialogLDB", "CollCourtDialog_txtCodeCnt", "");
		     ra.setAttribute("CollCourtDialogLDB", "CollCourtDialog_txtNameCnt", "");
		     ra.setAttribute("CollCourtDialogLDB", "CollCourtDialog_POL_MAP_ID", null);
		    
		     return true;
		 }
		 /**
		  * brise staru vrijednost u txtNCounty ako se upisivala vrijednost u
		  * txtCCounty i obratno, kako ne bi doslo do pogresnog povezivanja
		  * sifre i imena mjesta kod poziva LookUp-a
		  */
		if (ra.getCursorPosition().equals("CollCourtDialog_txtNameCnt")) {
		     ra.setAttribute("CollCourtDialogLDB", "CollCourtDialog_txtCodeCnt", "");
		 } else if (ra.getCursorPosition().equals("CollCourtDialog_txtCodeCnt")) {
		     ra.setAttribute("CollCourtDialogLDB", "CollCourtDialog_txtNameCnt", "");
		 }	
		
		if (!ra.isLDBExists("PoliticalMapByTypIdLookUpLDB")) {
		     ra.createLDB("PoliticalMapByTypIdLookUpLDB");
		 }
			
		 ra.setAttribute("PoliticalMapByTypIdLookUpLDB", "bDPolMapTypId", countyTypeId);
		
		 LookUpRequest lookUpRequest = new LookUpRequest("PoliticalMapByTypIdLookUp");
		 lookUpRequest.addMapping("CollCourtDialogLDB", "CollCourtDialog_POL_MAP_ID", "pol_map_id");
		 lookUpRequest.addMapping("CollCourtDialogLDB", "CollCourtDialog_txtCodeCnt", "code");
		 lookUpRequest.addMapping("CollCourtDialogLDB", "CollCourtDialog_txtNameCnt", "name");
		
		try {
		     ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
		     ra.showMessage("err012");
		     return false;
		} catch (NothingSelected ns) {
		    return false;
		}
		return true;
	} 
	

}
