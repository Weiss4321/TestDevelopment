
package hr.vestigo.modules.collateral;
import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import java.math.BigDecimal;
import java.util.Vector;

 
 
public class AgreementList extends Handler {
	public static String cvsident = "@(#) $Header: ";
	
	public AgreementList(ResourceAccessor ra) {
		super(ra);
	} 

               

	public void AgreementList_SE() {
		if (!(ra.isLDBExists("AgreementListLDB"))) {
			ra.createLDB("AgreementListLDB");
		}    

		ra.createActionListSession("tblAgreementList");	
		
		if(ra.getAttribute("GDB", "ScreenEntryParam")!=null){
			String param=(String)ra.getAttribute("GDB", "ScreenEntryParam");
			int i=param.lastIndexOf(".");
			if(i<0){
				ra.showMessage("err985");
				return;
			}
			String title=((String)ra.getAttribute("GDB", "ScreenEntryParam")).substring(++i);
//	 		System.out.println("title : "+title);
			ra.setScreenTitle(title);
			
 			int j=param.indexOf(".");
//	 		ra.setAttribute("AgreementListLDB", "phase", param.substring(0,j));
//	 		System.out.println("phase : "+ra.getAttribute("ColWorkListLDB", "phase"));
	 		
	 		param=param.substring(j+1);
	 		j=param.indexOf(".");
	 		ra.setAttribute("AgreementListLDB", "w_proc_status", param.substring(0,j));
//	 		System.out.println("proc_status : "+ra.getAttribute("ColWorkListLDB", "proc_status"));
//	 		ra.setAttribute("AgreementListLDB", "proc_status_QBE", param.substring(0,j));
 
		}		
		
	}
	
	
	public void add() {
//		 na F7		
//		ra.showMessage("inf_1800");		
		ra.loadScreen("Agreement","scr_add");
    
		   
	}	
	
	public void change() { 
// na F6		
//		ra.showMessage("inf_1800");	
		TableData td=null;

		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
		return;
		}
		td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = null;
		w_fra_agr_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);
		if (ra.getScreenContext().equalsIgnoreCase("scr_k")) 
			ra.loadScreen("Agreement","scr_change");
		else
			ra.loadScreen("Agreement","scr_change_act");
		
	}
	
	public void details() {
//		 na F4		
//		ra.showMessage("inf_1800");		
		TableData td=null;

		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
		return;
		}
		td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = null;
		w_fra_agr_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);

		ra.loadScreen("Agreement","scr_detail");
		 
	} 
	
	public void confirm () {
// na F8 - slanje na verifikaciju		
//		ra.showMessage("inf_1800");
		
//		 na F8 - slanje na verifikacju s radne liste
		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
			return;
		}
		
		TableData td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = null;
		w_fra_agr_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);
			 
//			saljem ga sa ref.liste za unos - 703223		
		ra.setAttribute("AgreementListLDB", "source_list", "703223");
//		 na listu za verifikaciju - 704223

		ra.setAttribute("AgreementListLDB", "target_list", "704223");
	
		ra.setAttribute("AgreementListLDB", "w_proc_status", "1");	
	
		ra.setAttribute("AgreementListLDB", "action_type", "SLANJE NA VERIF.");
				 
		Integer retValue = (Integer) ra.showMessage("col_qer016");

		if (retValue!=null) {
			if (retValue.intValue() == 0){ 
				return;
			}else{
				try{
					ra.executeTransaction();// AgrListSndVer - CO331.sqlj
				}catch(VestigoTMException vtme){
					ra.showMessage(vtme.getMessageID());
				}
				ra.refreshActionList("tblAgreementList");
			} 
		}
		
	}  
	
	public void stop () {
// na F2		
//		ra.showMessage("inf_1800");			
//		 na F2 - ponisti,obustavi - mijenja status sporazuma u D
//		 akcija moguca na radnoj i verifikacijskoj listi 	
				

		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
			return; 
		}
		
		Integer retValue = (Integer) ra.showMessage("col_qer017");
		if (retValue!=null && retValue.intValue() == 0) return;
		
		TableData td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = null;
		w_fra_agr_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);


//		 omoguciti akciju i sa verifikacijske liste	
		if (ra.getScreenContext().equalsIgnoreCase("scr_k")) {
//		 saljem ga sa ref.liste za unos - 703223		
			ra.setAttribute("AgreementListLDB", "source_list", "703223");
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_ver_list")) {
//		saljem ga sa verifikacijske iste  - 704223	
			ra.setAttribute("AgreementListLDB", "source_list", "704223");			
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_mon_list")) {
//		 na monitoring listi pravu source listu odredjujem u transakciji
			ra.setAttribute("AgreementListLDB", "source_list", "000000");		
		}
//		 na listu ponistenih - 701223	
		ra.setAttribute("AgreementListLDB", "target_list", "707223");
		ra.setAttribute("AgreementListLDB", "w_proc_status", "D");	
		ra.setAttribute("AgreementListLDB", "action_type", "OBUSTAVI");	
				
		ra.loadScreen("AgrListQCmnt", "scr_stop");				
		
	}
	
	public void payment_history (){
// na Shift F4		
//		ra.showMessage("inf_1800");	
//		 ns Shift F4		

		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
		return;  
		}  
		TableData td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = null;
		w_fra_agr_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);
		
		ra.loadScreen("AgrListQ");		
		
	}
	
	public void AgrListQ_SE() {

/*		if (!(ra.isLDBExists("ColListQLDB"))) {
			ra.createLDB("ColListQLDB");
		}		
		ra.setAttribute("ColListQLDB", "col_hea_id", ra.getAttribute("AgreementListLDB", "w_fra_agr_id"));
*/
		ra.createActionListSession("tblAgrListQ");		
	}
	
	public void query_by_example() {
//		 na F5		
//		ra.showMessage("inf_1800");		


		ra.loadScreen("AgreementQBE", "scr_qbe");
		
		
	}
	
	public void refresh() {
//		 na Shift F5		
		ra.refreshActionList("tblAgreementList");
						
	}	
	public void back(){
//		na F7		

//   	ra.showMessage("inf_1800");
		
		
//		 provjeriti da li ima slogova na listi	
		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
			return;
		} 

// provjeriti da li su upisani org. jedinica i referent kome se vraca predmet		
//		 provjeriti da li je upisana organizacijska jedinica i user
// na arhivskoj listi, listi ponistenih, listi odbijenih
// 	
		
		if (ra.getScreenContext().equalsIgnoreCase("scr_ver_list") || ra.getScreenContext().equalsIgnoreCase("scr_auth_list")) {
			ra.setAttribute("AgreementListLDB", "use_id_asg",null);
			ra.setAttribute("AgreementListLDB", "org_uni_id_asg", null);
		} else {
			if ((ra.getAttribute("AgreementListLDB", "CollAssignment_txtUserName") == null
				|| ((String) ra.getAttribute("ColWorAgreementListLDBkListLDB","CollAssignment_txtUserName")).equals(""))
				&& (ra.getAttribute("AgreementListLDB", "CollAssignment_txtLogin") == null
				|| ((String) ra.getAttribute("AgreementListLDB","CollAssignment_txtLogin")).equals(""))) {
				ra.showMessage("wrnclt107"); 
				return;
			}
		}
		   
		
		
		Integer retValue = (Integer) ra.showMessage("col_qer018");
		if (retValue!=null && retValue.intValue() == 0) return;
		
		TableData td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = null;
		w_fra_agr_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);
		
		if (ra.getScreenContext().equalsIgnoreCase("scr_ver_list")) {  
//  vracanje s verifikacijske liste 704223
			ra.setAttribute("AgreementListLDB", "source_list", "704223");		
			ra.setAttribute("AgreementListLDB", "w_workflow_indic", "1"); 
			ra.setAttribute("AgreementListLDB", "source_status", "1");
		} 	
			//	 na radnu listu 
		ra.setAttribute("AgreementListLDB", "target_list", "703223");
		ra.setAttribute("AgreementListLDB", "w_proc_status", "0");	
		ra.setAttribute("AgreementListLDB", "action_type", "VRATI U OBRADU");	
			
		ra.loadScreen("AgrListQCmnt", "scr_back");			
		
	   	
	}
	
	public void verification_autorization(){
// na F8 - verifikacija	
//	   	ra.showMessage("inf_1800");		
	   	
//		 provjeriti da li ima slogova na listi	
		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
			return;
		} 
		
		
		TableData td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = null;
		w_fra_agr_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);	
   		
// verifikacija na verifikacijskoj listi
  			
//   saljem ga sa liste za verifikaciju - 704223	
   		ra.setAttribute("AgreementListLDB", "source_list", "704223");
		ra.setAttribute("AgreementListLDB", "source_status", "1");
//   na listu aktivnih ugovora - 706223	
  			 
   			
   		ra.setAttribute("AgreementListLDB", "target_list", "706223");
   		ra.setAttribute("AgreementListLDB", "w_proc_status", "2");	
   		ra.setAttribute("AgreementListLDB", "action_type", "VERIFICIRAJ");
   			
		Integer retValue = (Integer) ra.showMessage("col_qer019");

		if (retValue!=null) {
			if (retValue.intValue() == 0){ 
				return;
			}else{
				try{
   					ra.executeTransaction();// AgrListVer - CO334.sqlj
   				}catch(VestigoTMException vtme){
   					ra.showMessage(vtme.getMessageID());
   				}
   				ra.refreshActionList("tblAgreementList");
   			} 
   		}
	   	 
	}
	  
	public void close_agreement(){
//		 na F2 - zatvori ugovor	
//		ra.showMessage("inf_1800");		
		


		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
		return;
		}
		
		TableData td=null;
		td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = (BigDecimal) hidden.elementAt(0);
		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);
		
		Vector row=(Vector)td.getSelectedRowData();
		String w_agreement_no =(String)row.elementAt(2);
		ra.setAttribute("AgreementListLDB", "w_agreement_no", w_agreement_no);
		  
//		zatvaranje ugovora
			
//ugovor je na listi aktivnih saljem ga sa liste za verifikaciju - 706223	
		ra.setAttribute("AgreementListLDB", "source_list", "706223");
		ra.setAttribute("AgreementListLDB", "source_status", "1");
// i ostaje na listi aktivnih ali ce biti na listi neaktivnih - 706223	
		ra.setAttribute("AgreementListLDB", "target_list", "706223");
		ra.setAttribute("AgreementListLDB", "w_proc_status", "2");	
		ra.setAttribute("AgreementListLDB", "action_type", "ZATVORI UGOVOR");
			
		Integer retValue = (Integer) ra.showMessage("col_qer021");

		if (retValue!=null) {
			if (retValue.intValue() == 0){ 
				return;
			}else{
				try{
					ra.executeTransaction();// AgrListClose - CO336.sqlj
				}catch(VestigoTMException vtme){
					ra.showMessage(vtme.getMessageID());
				}
				ra.refreshActionList("tblAgreementList");
			} 
		}		
	}	

	public void connect_agreement() {
// na F6 - povezi ugovor s plasmanom
//		ra.showMessage("inf_1800");		
		
		TableData td=null;

		if (isTableEmpty("tblAgreementList")) {
			ra.showMessage("wrn299");
		return;
		}
		td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal w_fra_agr_id = null;
		w_fra_agr_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", w_fra_agr_id);

		ra.loadScreen("LoanBeneficiary","scr_agreement");		
		
	}
	 
	
	
	public void change_ref() {
// na F7		
//		 preraspodjela predmeta drugom referentu
//		 moze se preraspodijeliti predmet koji je na referentskoj listi		
//		ra.showMessage("inf_1800");
//		return;				
		
   		if (isTableEmpty("tblAgreementList")) {
   			ra.showMessage("wrn299");
   			return;
   		} 

// provjeriti da li je predmet na referentskoj listi, ako nije poruka
		TableData td = (TableData) ra.getAttribute("AgreementListLDB", "tblAgreementList");
		Vector row = (Vector) td.getSelectedRowData();
		String lista = (String) row.elementAt(1);

		
		if (!(lista.equalsIgnoreCase("0"))) {
			ra.showMessage("wrnclt108");
			return;
		}
   		  
//   	provjeriti da li su upisani org. jedinica i referent kome se vraca predmet	
   		if ((ra.getAttribute("AgreementListLDB", "AgrAssig_txtUserName") == null
   				|| ((String) ra.getAttribute("AgreementListLDB","AgrAssig_txtUserName")).equals(""))
   				&& (ra.getAttribute("AgreementListLDB", "AgrAssig_txtLogin") == null
   				|| ((String) ra.getAttribute("AgreementListLDB","AgrAssig_txtLogin")).equals(""))) {
   				ra.showMessage("wrnclt107"); 
   				return;
   		}
  
   	 	  
   		
   		Integer retValue = (Integer) ra.showMessage("col_qer020");
   		if (retValue!=null && retValue.intValue() == 0) return;	
		
		Vector hidden = (Vector) td.getSelectedRowUnique();
		ra.setAttribute("AgreementListLDB", "w_fra_agr_id", (BigDecimal) hidden.elementAt(0));  	
						 
//		vracanje s monitoring liste 000000	
//		vracam ustvari s neke radne liste 

//		saljem ga sa ref.liste za unos - 703223		
		ra.setAttribute("AgreementListLDB", "source_list", "703223");
//		ra.setAttribute("AgreementListLDB", "w_workflow_indic", "0");
		ra.setAttribute("AgreementListLDB", "source_status", "0");		
		//	 na radnu listu 
		ra.setAttribute("AgreementListLDB", "target_list", "703223");
		ra.setAttribute("AgreementListLDB", "w_proc_status", "0");	
		ra.setAttribute("AgreementListLDB", "action_type", "PRERASPODIJELI");	

		ra.loadScreen("AgrListQCmnt", "scr_back");		
	}
	
	
	public boolean isTableEmpty(String tableName) {
		TableData td = (TableData) ra.getAttribute(tableName);
		if (td == null)
			return true;

		if (td.getData().size() == 0)
			return true;

		return false;
	}	
	
	
//	 validacija user-a i organizacijske jedinice kod preraspodjele predmeta	

	
	public boolean AgrAssig_txtOrgUnit_FV(String elementName, Object elementValue,Integer lookUpType) {
		
		
		if(elementValue == null || ((String)elementValue).equals("")){
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtOrgCode", "");
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtOrgUnit", "");
			ra.setAttribute("AgreementListLDB", "org_uni_id_asg", null);
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtUserName", "");
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtLogin", "");
			ra.setAttribute("AgreementListLDB", "use_id_asg", null);
			return true;
		}
		if (ra.getCursorPosition().equals("CollAssignment_txtOrgCode")) {
		     ra.setAttribute("AgreementListLDB", "AgrAssig_txtOrgUnit", "");
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("AgrAssig_txtOrgUnit")) {
		     ra.setAttribute("AgreementListLDB", "AgrAssig_txtOrgCode", "");
		 }	
	
		LookUpRequest lookUpRequest = new LookUpRequest("OrgUniLookUp");
		lookUpRequest.addMapping("AgreementListLDB","AgrAssig_txtOrgCode","code");
		lookUpRequest.addMapping("AgreementListLDB","AgrAssig_txtOrgUnit","name");
		lookUpRequest.addMapping("AgreementListLDB","org_uni_id_asg","org_uni_id");
		
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtOrgCode", "");
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtOrgUnit", "");
			ra.setAttribute("AgreementListLDB", "org_uni_id_asg", null);
			ra.setCursorPosition("AgrAssig_txtOrgCode");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		if(elementName.equals("AgrAssig_txtOrgCode") || 
				elementName.equals("AgrAssig_txtOrgUnit")){
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtUserName", "");
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtLogin", "");
			ra.setAttribute("AgreementListLDB", "use_id_asg", null);			
		} 	
		ra.setCursorPosition("AgrAssig_txtLogin");
		 
		return true;
	}	 
	 
	 
	 
	public boolean AgrAssig_txtLogin_FV  (String elementName, Object elementValue,Integer lookUpType) {

		if(elementValue == null || ((String)elementValue).equals("")){
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtUserName", "");
			ra.setAttribute("AgreementListLDB", "AgrAssig_txtLogin", "");
			ra.setAttribute("AgreementListLDB", "use_id_asg", null);
			return true;
		}
		 
		if ((ra.getAttribute("AgreementListLDB", "AgrAssig_txtOrgCode") == null) || 
				ra.getAttribute("AgreementListLDB", "AgrAssig_txtOrgCode").equals("")){
			ra.showMessage("inf216");
			return false;
		}

		if (ra.getCursorPosition().equals("AgrAssig_txtLogin")) {
		     ra.setAttribute("AgreementListLDB", "AgrAssig_txtUserName", "");
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("AgrAssig_txtUserName")) {
		     ra.setAttribute("AgreementListLDB", "AgrAssig_txtLogin", "");
		 }		
		
		
		if (!ra.isLDBExists("AppUserOrgLDB")) ra.createLDB("AppUserOrgLDB");
		ra.setAttribute("AppUserOrgLDB", "org_uni_id",  ra.getAttribute("AgreementListLDB", "org_uni_id_asg"));	
		
		
		ra.setAttribute("AgreementListLDB", "dummySt", "");
		ra.setAttribute("AgreementListLDB", "dummyBD", null);
 
		LookUpRequest request = new LookUpRequest("AppUserOrgLookUp");
		request.addMapping("AgreementListLDB", "use_id_asg", "use_id");
		request.addMapping("AgreementListLDB", "AgrAssig_txtLogin", "login");
		request.addMapping("AgreementListLDB", "AgrAssig_txtUserName", "user_name");
		request.addMapping("AgreementListLDB", "dummySt", "abbreviation");
		request.addMapping("AgreementListLDB", "dummyBD", "org_uni_id");
		try {  
			ra.callLookUp(request);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		return true;
        
		 
	} // AgrAssig_txtLogin_FV
	
	
}