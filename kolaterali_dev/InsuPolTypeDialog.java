/*
 * Created on 2006.05.26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author hrajkl
 *
 * TODO To change the template for this generated type comment go to
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
public class InsuPolTypeDialog extends Handler {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsuPolTypeDialog.java,v 1.5 2017/04/05 10:28:31 hraziv Exp $";
	
	public InsuPolTypeDialog(ResourceAccessor ra) {
		super(ra);
	}
	
	public void InsuPolTypeDialog_SE(){
		
		ra.createLDB("InsuPolicyTypeDialogLDB");
		String scr_ctxt = ra.getScreenContext().trim();
		if(scr_ctxt.equalsIgnoreCase("scr_insert")){
			ra.setAttribute("InsuPolicyTypeDialogLDB","STATUS","A");
//			ra.setAttribute("InsuPolicyTypeDialogLDB","INT_GROUP1","IMOVINA");
//			ra.setAttribute("InsuPolicyTypeDialogLDB","INT_GROUP2","IMOVINA2");
//			ra.setAttribute("InsuPolicyTypeDialogLDB","INT_GROUP3","IMOVINA3");
//			ra.setAttribute("InsuPolicyTypeDialogLDB","INT_GROUP4","IMOVINA4");
			ra.setAttribute("InsuPolicyTypeDialogLDB","INT_POL_SPEC_STAT","00");
			ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtDateFrom",(Date)ra.getAttribute("GDB","ProcessingDate"));
			ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtDateUntil",Date.valueOf("9999-12-31"));
			ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtUseOpenLogin",(String)ra.getAttribute("GDB","Use_Login"));//samo za prikaz
			ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtUseOpenName",(String)ra.getAttribute("GDB","Use_UserName"));//samo za prikaz
		}
		else if(scr_ctxt.equalsIgnoreCase("scr_update") || scr_ctxt.equalsIgnoreCase("scr_detail")){
			fillData();
		}
		
        //uvjet ako je za grupu 4 odabran INSP onda je grupa 3 obavezan podatak 
        if(ra.getAttribute("InsuPolicyTypeDialogLDB", "INT_GROUP4").equals("INSP")){
             ra.setContext("INT_GROUP3","fld_plain");
             ra.setRequired("INT_GROUP3", true);
        }else{
            ra.setContext("INT_GROUP3","fld_protected");
            ra.setRequired("INT_GROUP3", false);
            ra.setAttribute("InsuPolicyTypeDialogLDB", "INT_GROUP3", "");
        }
        
	}
	
	public void confirm(){
		
		/*if(!CheckDateFrom()){//provjera trenutnog datuma
			ra.showMessage("wrnretd01");
        	return;
		}*/
		
		if (!(ra.isRequiredFilled())) {//provjerava dali su popunjena polja koja moraju biti, prema oznaci na elementu Block
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
        ra.refreshActionList("tblInsuPolicyType");
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
        ra.refreshActionList("tblInsuPolicyType");
	}//scr_delete za transakciju
	
	public void change(){
		
		/*if(!CheckDateFrom()){
			ra.showMessage("wrnretd01");
        	return;
		}*/
		if (!(ra.isRequiredFilled())) {//provjerava dali su popunjena polja koja moraju biti, prema oznaci na elementu Block
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
        ra.refreshActionList("tblInsuPolicyType");
		
	}//scr_update za transakciju
	
	private void fillData(){// za update i delete se ovo poziva
		TableData td = (TableData) ra.getAttribute("InsuPolicyTypeLDB","tblInsuPolicyType");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector data = (Vector) td.getSelectedRowData();
		
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtIntPolTypeCode",(String)data.elementAt(0));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtIntPolTypeName",(String)data.elementAt(1));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtICName",(String)data.elementAt(2));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtDateFrom",(Date)data.elementAt(3));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtDateUntil",(Date)data.elementAt(4));
		
		ra.setAttribute("InsuPolicyTypeDialogLDB","INT_POL_TYPE_ID",(BigDecimal)hidden.elementAt(0));
		ra.setAttribute("InsuPolicyTypeDialogLDB","INT_POL_COMPANY_ID",(BigDecimal)hidden.elementAt(1));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtICCode",(String)hidden.elementAt(2));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtUseOpenLogin",(String)hidden.elementAt(3));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtUseOpenName",(String)hidden.elementAt(4));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtUseLogin",(String)hidden.elementAt(5));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtUseName",(String)hidden.elementAt(6));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtOpeningTS",(Timestamp)hidden.elementAt(7));
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtUseLock",(Timestamp)hidden.elementAt(8));
		ra.setAttribute("InsuPolicyTypeDialogLDB","USER_LOCK",(Timestamp)hidden.elementAt(8)); //za update i delete, dali je u meðuvremenu doslo do promjene
		ra.setAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtICRegisterNo",(String)hidden.elementAt(9));
		ra.setAttribute("InsuPolicyTypeDialogLDB","INT_GROUP1",(String)hidden.elementAt(10));
		ra.setAttribute("InsuPolicyTypeDialogLDB","INT_GROUP2",(String)hidden.elementAt(11));
		ra.setAttribute("InsuPolicyTypeDialogLDB","INT_GROUP3",(String)hidden.elementAt(12));
		ra.setAttribute("InsuPolicyTypeDialogLDB","INT_GROUP4",(String)hidden.elementAt(13));
		
	}
	
	//InsuPolTypeDialog_CompanyID_FV
	public boolean InsuPolTypeDialog_CompanyID_FV(String elementName, Object elementValue, Integer LookUp){

		if (elementValue == null || ((String) elementValue).equals("")) {                                    
			//InsuPolicyTypeDialog_txtICName && InsuPolicyTypeDialog_txtICCode
			ra.setAttribute("InsuPolicyTypeDialogLDB", "InsuPolicyTypeDialog_txtICName", "");   
			ra.setAttribute("InsuPolicyTypeDialogLDB", "InsuPolicyTypeDialog_txtICCode", "");
			ra.setAttribute("InsuPolicyTypeDialogLDB", "InsuPolicyTypeDialog_txtICRegisterNo", "");
			//register_no omega_id  ic_register_no
			ra.setAttribute("InsuPolicyTypeDialogLDB", "INT_POL_COMPANY_ID", null);                    
			return true;                                                                                 
		}                                                                                               
        
		if( ra.getCursorPosition().equals("InsuPolicyTypeDialog_txtICRegisterNo") ){
			ra.setCursorPosition(3);
		}else if( ra.getCursorPosition().equals("InsuPolicyTypeDialog_txtICCode") ){
			ra.setCursorPosition(2);
		}else if( ra.getCursorPosition().equals("InsuPolicyTypeDialog_txtICName") ){
			ra.setCursorPosition(1);
		}
		
		LookUpRequest lu = new LookUpRequest("InsuCompanyLookup");                                     
        lu.addMapping("InsuPolicyTypeDialogLDB", "INT_POL_COMPANY_ID", "ic_id");  
        lu.addMapping("InsuPolicyTypeDialogLDB", "InsuPolicyTypeDialog_txtICRegisterNo", "ic_register_no");
        lu.addMapping("InsuPolicyTypeDialogLDB", "InsuPolicyTypeDialog_txtICCode", "ic_code");
        lu.addMapping("InsuPolicyTypeDialogLDB", "InsuPolicyTypeDialog_txtICName", "ic_name"); 
        
        try {                                                                                            
            ra.callLookUp(lu);                                                                           
        } catch (EmptyLookUp elu) {                                                                      
            ra.showMessage("err012");                                                                    
            return false;                                                                                
        } catch (NothingSelected ns) {                                                                   
            return false;                                                                                
        } 
      
       return true;                                                                                                                  
    } 
	//InsuPolTypeDialog_CompanyID_FV
	
	//InsuPolicyTypeDialog_txtDateFrom_FV
	public boolean InsuPolicyTypeDialog_txtDateFrom_FV(String elementName, Object elementValue, Integer lookUpType) {
		//provjera postoji li unesen datumOd
		if (elementValue != null) {
			String ldbName = "InsuPolicyTypeDialogLDB";
			//provjera da li postoji datumDo
			if (ra.getAttribute(ldbName, "InsuPolicyTypeDialog_txtDateUntil") != null) {
				//kontrola da li je datumDo prije datumOd, u tom slucaju je validacija false i prikazuje se poruka
				if (((java.sql.Date) ra.getAttribute(ldbName, "InsuPolicyTypeDialog_txtDateUntil"))
					.before((java.sql.Date) ra.getAttribute(ldbName, "InsuPolicyTypeDialog_txtDateFrom"))) {
					ra.showMessage("wrn104");
					return false;
				}
			}
		}
		return true;
	}//InsuPolicyTypeDialog_txtDateFrom_FV
	
	//InsuPolicyTypeDialog_txtDateUntil_FV
	public boolean InsuPolicyTypeDialog_txtDateUntil_FV(String elementName, Object elementValue, Integer lookUpType) {
		//provjera postoji li unesen datumDo
		if (elementValue != null) {
			String ldbName = "InsuPolicyTypeDialogLDB";
			//provjera da li postoji datumOd
			if (ra.getAttribute(ldbName, "InsuPolicyTypeDialog_txtDateFrom") != null) {
				//kontrola da li je datumDo prije datumOd, u tom slucaju je validacija false i prikazuje se poruka
				if (((java.sql.Date) ra.getAttribute(ldbName, "InsuPolicyTypeDialog_txtDateUntil"))
					.before((java.sql.Date) ra.getAttribute(ldbName, "InsuPolicyTypeDialog_txtDateFrom"))) {
					ra.showMessage("wrn104");
					return false;
				}
			}
		}
		return true;
	}//InsuPolicyTypeDialog_txtDateUntil_FV
	
	
	//INT_GROUP4_FV
	public boolean INT_GROUP4_FV(String elementName, Object elementValue, Integer lookUpType) {
	    
	    if (elementValue == null || ((String) elementValue).equals("")) {                                    
            ra.setAttribute("InsuPolicyTypeDialogLDB", "COL_CAT_ID", null);   
            ra.setAttribute("InsuPolicyTypeDialogLDB", "COL_CAT_NAME", "");                    
            return true;                                                                                 
        }          
	    
	    
	    //ovaj lookup ne dohvaca kategorije koje imaju ekran 'X' (vidjeti primjer pozica u CollReview.java)
	    LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
	        
	    //redosljed addMapping treba odgovarati redosljedi kojim se u transakciji dohvaæaju atributi pa tako npr se u trx na 1. dohvati  col_cat_id, na 2. code....
	    lookUpRequest.addMapping("InsuPolicyTypeDialogLDB", "COL_CAT_ID", "col_cat_id");
	    lookUpRequest.addMapping("InsuPolicyTypeDialogLDB", "INT_GROUP4", "code");
	    lookUpRequest.addMapping("InsuPolicyTypeDialogLDB", "COL_CAT_NAME", "name");
	        
	        
	    try {
	        ra.callLookUp(lookUpRequest);
	    } catch (EmptyLookUp elu) {
	        ra.showMessage("err012"); 
	        return false;
	    } catch (NothingSelected ns) {
	        return false;
	    }

	    //uvjet ako je za grupu 4 odabran INSP onda je grupa 3 obavezan podatak 
	    if(ra.getAttribute("InsuPolicyTypeDialogLDB", "INT_GROUP4").equals("INSP")){
	         ra.setContext("INT_GROUP3","fld_plain");
	         ra.setRequired("INT_GROUP3", true);
	    }else{
	        ra.setContext("INT_GROUP3","fld_protected");
	        ra.setRequired("INT_GROUP3", false);
	        ra.setAttribute("InsuPolicyTypeDialogLDB", "INT_GROUP3", "");
	    }
	    
	    
       return true;  
	}
	
   //INT_GROUP3_FV
   public boolean INT_GROUP3_FV(String elementName, Object elementValue, Integer lookUpType) {
        
        if (elementValue == null || ((String) elementValue).equals("")) {                                    
            ra.setAttribute("InsuPolicyTypeDialogLDB", "COLL_TYPE_ID", null);   
            ra.setAttribute("InsuPolicyTypeDialogLDB", "COLL_TYPE_NAME", "");                    
            return true;                                                                                 
        }          
        
        LookUpRequest lookUpRequest = new LookUpRequest("CollTypeLookUp");
            
        //redosljed addMapping treba odgovarati redosljedi kojim se u transakciji dohvaæaju atributi pa tako npr se u trx na 1. dohvati  COLL_TYPE_ID, na 2. code....
        lookUpRequest.addMapping("InsuPolicyTypeDialogLDB", "COLL_TYPE_ID", "coll_type_id");
        lookUpRequest.addMapping("InsuPolicyTypeDialogLDB", "INT_GROUP3", "coll_type_code");
        lookUpRequest.addMapping("InsuPolicyTypeDialogLDB", "COLL_TYPE_NAME", "coll_type_name");
            
            
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
	
//	provjera dali je datum od manji od trenutnog, taj se unos ne dopusta
	public boolean CheckDateFrom(){
		Date from=(Date) ra.getAttribute("InsuPolicyTypeDialogLDB","InsuPolicyTypeDialog_txtDateFrom");
        Date currentDate= (Date) ra.getAttribute("GDB","ProcessingDate"); 
        	//if(from != null){ // puca na provjeri ako je prazno polje
        		if((from != null) && from.before(currentDate)){
        			return false;
        		}
        	//}
		return true;
	}

	

}

