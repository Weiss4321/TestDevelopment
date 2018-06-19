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
//import java.util.Map;
import java.util.Vector;
/**  
 * @author hramkr 
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColWorkListDemo extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/ColWorkListDemo.java,v 1.1 2012/07/09 12:41:37 vu00209 Exp $";
	
	public ColWorkListDemo(ResourceAccessor ra) {
		super(ra);
	} 
  
              

	public void ColWorkListDemo_SE() { 

		if (!(ra.isLDBExists("ColWorkListLDB"))) {    
			ra.createLDB("ColWorkListLDB");
		} 
		  
		
		ra.setAttribute("ColWorkListLDB", "ColWorkList_txtScreenContext", ra.getScreenContext());
//        System.out.println("ColWorkList_txtScreenContext : "+ra.getAttribute("ColWorkListLDB", "ColWorkList_txtScreenContext"));		      
		if (ra.getScreenContext().equalsIgnoreCase("scr_all_list") || ra.getScreenContext().equalsIgnoreCase("scr_arh_list") || 
			ra.getScreenContext().equalsIgnoreCase("scr_free_list") || ra.getScreenContext().equalsIgnoreCase("scr_canc_list") ||
			ra.getScreenContext().equalsIgnoreCase("scr_inact_list") || ra.getScreenContext().equalsIgnoreCase("scr_arh_nek") ||
			ra.getScreenContext().equalsIgnoreCase("scr_free_nek") ||  ra.getScreenContext().equalsIgnoreCase("scr_inac_nek")) {
			ra.createActionListSession("tblColWorkList", false);			
		} else {
			ra.createActionListSession("tblColWorkList");
		}
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
	 		ra.setAttribute("ColWorkListLDB", "phase", param.substring(0,j));
//	 		System.out.println("phase : "+ra.getAttribute("ColWorkListLDB", "phase"));
	 		
	 		param=param.substring(j+1);
	 		j=param.indexOf(".");
	 		ra.setAttribute("ColWorkListLDB", "proc_status", param.substring(0,j));
//	 		System.out.println("proc_status : "+ra.getAttribute("ColWorkListLDB", "proc_status"));
	 		ra.setAttribute("ColWorkListLDB", "proc_status_QBE", param.substring(0,j));
 
		}
		  
// ako je arhivska lista, lista ponistenih  napuniti organizacijsku jedinicu i korisnika iz GDB	
// dodana i lista odbijenih		
// Milka, 10.04.2007, dodana i lista neaktivnih		
		if (ra.getScreenContext().equalsIgnoreCase("scr_arh_list") || ra.getScreenContext().equalsIgnoreCase("scr_canc_list") ||
				ra.getScreenContext().equalsIgnoreCase("scr_discard_list") || ra.getScreenContext().equalsIgnoreCase("scr_arh_nek") ||
				ra.getScreenContext().equalsIgnoreCase("scr_inact_list") || ra.getScreenContext().equalsIgnoreCase("scr_inact_nek") ||
				ra.getScreenContext().equalsIgnoreCase("scr_free_list") || ra.getScreenContext().equalsIgnoreCase("scr_free_nek")) {
	 		ra.setAttribute("ColWorkListLDB", "CollAssignment_txtOrgCode", ra.getAttribute("GDB", "OrgUni_Code"));
			ra.setCursorPosition("CollAssignment_txtOrgCode");
			ra.invokeValidation("CollAssignment_txtOrgCode");

	 		ra.setAttribute("ColWorkListLDB","CollAssignment_txtLogin", ra.getAttribute("GDB", "Use_Login"));
			ra.setCursorPosition("CollAssignment_txtLogin");
			ra.invokeValidation("CollAssignment_txtLogin");	
			
			ra.setCursorPosition("tblColWorkList");
		}
		  
		
		ra.performQueryByExample("tblColWorkList");       
	} 
	 



	public void stop() {
// na F2 - ponisti - mijenja status collateral-a u 4
// akcija moguca na radnoj i verifikacijskoj listi 	
		
/*		if (ra.getScreenContext().equalsIgnoreCase("scr_mon_list")) {
			ra.showMessage("inf_1800");
			return;
		}*/
		
		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("dealWar009");
			return;
		}


		Integer retValue = (Integer) ra.showMessage("col_qer002");
		if (retValue!=null && retValue.intValue() == 0) return;
		
		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));
// omoguciti akciju i sa verifikacijske liste	
		if (ra.getScreenContext().equalsIgnoreCase("scr_k")) {
// saljem ga sa ref.liste za unos - 697223		
			ra.setAttribute("ColWorkListLDB", "source_list", "697223");
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_ver_list")) {
//	saljem ga sa verifikacijske iste  - 698223		
			ra.setAttribute("ColWorkListLDB", "source_list", "698223");			
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_mon_list") || ra.getScreenContext().equalsIgnoreCase("scr_mon_nek")) {
// na monitoring listi pravu source listu odredjujem u transakciji
			ra.setAttribute("ColWorkListLDB", "source_list", "000000");		
		}
// na listu ponistenih - 701223	
		ra.setAttribute("ColWorkListLDB", "target_list", "701223");
		ra.setAttribute("ColWorkListLDB", "proc_status", "4");	
		ra.setAttribute("ColWorkListLDB", "action_type", "PONISTI");	
		
		ra.loadScreen("CollListQCmnt", "scr_stop");		
	
	}  

	public void discard() {
// na verifikacijskoj i autorizacijskoj listi - odbiti collateral 		
// na F6 - odbaci - mijenja status collateral-a u 5
		
/*		if (ra.getScreenContext().equalsIgnoreCase("scr_mon_list")) {
			ra.showMessage("inf_1800");
			return;
		}*/
		   
		
		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("dealWar009");
			return;
		}
		
		Integer retValue = (Integer) ra.showMessage("col_qer003");
		if (retValue!=null && retValue.intValue() == 0) return;
		
		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));
//		 omoguciti akciju i sa autorizacijske liste	
		if (ra.getScreenContext().equalsIgnoreCase("scr_ver_list")) {
//			saljem ga sa verifikacijske iste  - 698223		
			ra.setAttribute("ColWorkListLDB", "source_list", "698223");			
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_auth_list")) {
//			saljem ga sa autorizacijske liste - 699223
			ra.setAttribute("ColWorkListLDB", "source_list", "699223");						
		} else if (ra.getScreenContext().equalsIgnoreCase("scr_mon_list") || ra.getScreenContext().equalsIgnoreCase("scr_mon_nek")) {
//			 na monitoring listi pravu source listu odredjujem u transakciji
			ra.setAttribute("ColWorkListLDB", "source_list", "000000");		
		}
//		 na listu odbijenih - 702223	
		ra.setAttribute("ColWorkListLDB", "target_list", "702223");
		ra.setAttribute("ColWorkListLDB", "proc_status", "5");	
		ra.setAttribute("ColWorkListLDB", "action_type", "ODBIJ");	
				
		ra.loadScreen("CollListQCmnt", "scr_stop");		
			
	}  
	  
	public void details() {
// na F4		
		
		TableData td=null;

		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("wrn299");
		return;
		}
		td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal col_hea_id = null;
		col_hea_id = (BigDecimal) hidden.elementAt(0);
		String ekran="";
		ekran = (String) hidden.elementAt(5);
		String code="";
		code = (String) hidden.elementAt(6);

		BigDecimal col_cat_id = null;
		col_cat_id = (BigDecimal) hidden.elementAt(4);
		
		ra.setAttribute("ColWorkListLDB", "col_cat_id", col_cat_id);
		ra.setAttribute("ColWorkListLDB", "col_hea_id", col_hea_id);
		ra.setAttribute("ColWorkListLDB", "code", code); 
  
		if (code != null) {
		
			if (code.equalsIgnoreCase("ZALI"))
				ra.setScreenTitle("Zalihe");
			else if (code.equalsIgnoreCase("OBVE"))
				ra.setScreenTitle("Obveznice");
			else if (code.equalsIgnoreCase("DION"))
				ra.setScreenTitle("Dionice");		
			else if (code.equalsIgnoreCase("ZAPI"))
				ra.setScreenTitle("Zapisi");
			else if (code.equalsIgnoreCase("UDJE"))
				ra.setScreenTitle("Udjeli");
		}
	    String lista = (String) ra.getAttribute("ColWorkListLDB", "proc_status");
		if (ekran != null && ekran.trim().equalsIgnoreCase("X")) {
			ra.showMessage("inf_1800");
		} else {
		    
	        if (lista.equalsIgnoreCase("3") || lista.equalsIgnoreCase("K")) 
	            ra.loadScreen(ekran.trim(),"scr_detail_co");
	        else            
	            ra.loadScreen(ekran.trim(),"scr_detail");
		}
 		
	}
 
	
	public void ColListQ_SE() {

		if (!(ra.isLDBExists("ColListQLDB"))) {
			ra.createLDB("ColListQLDB");
		}		
		ra.setAttribute("ColListQLDB", "col_hea_id", ra.getAttribute("ColWorkListLDB", "col_hea_id"));

		ra.createActionListSession("tblColListQ");		
	}
	 
	public void payment_history (){  
// ns Shift F4		
		TableData td=null;		
		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("wrn299");
		return;  
		}  
		td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal col_hea_id = null;
		col_hea_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("ColWorkListLDB", "col_hea_id", col_hea_id); 
		
		ra.loadScreen("ColListQ");		
		
	} 
	
	public void query_by_example() {
// na F5		
//		ra.loadScreen("ColTypeList","query");
//		ra.showMessage("inf_1800");
//		ra.loadScreen("ColWorkListQBE");		
		
// Milka, 28.10.2006 - novi upit po uzorku, podize se ekran za qbe
// Milka, 14.11.2006 - poseban ekran za qbe za nekretnine
		if (ra.getScreenContext().equalsIgnoreCase("scr_mon_nek") || ra.getScreenContext().equalsIgnoreCase("scr_arh_nek") ||
				ra.getScreenContext().equalsIgnoreCase("scr_inact_nek") || ra.getScreenContext().equalsIgnoreCase("scr_free_nek")) {
			ra.loadScreen("KolateralQBE", "scr_nek_qbe");
		} else {
			ra.loadScreen("KolateralQBE", "scr_arh_qbe");
		}
	}    
       
	public void find_osig() {
//		 na F6 - pretrazivanje po broju police osiguranja		

//				ra.showMessage("inf_1800");

		ra.loadScreen("KolateralQBE", "scr_osig_qbe");

	}    	
	
	public void refresh() {
// na Shift F5		
		ra.refreshActionList("tblColWorkList");
	}
  
 
	public void change() { 
// na F6 sa radne liste		
		TableData td=null;

		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("wrn299");
		return;  
		}
		td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");

		Vector row=(Vector)td.getSelectedRowData();
//		 da li su potvrdjeni osnovni podaci
		String collateral_status =(String)row.elementAt(5);
		
		String posting_flag = (String)row.elementAt(9);
/*		if (collateral_status.equalsIgnoreCase("1")) {
			
			Integer retValue = (Integer) ra.showMessage("wrn473");
			if (retValue!=null && retValue.intValue() == 0) return;
		} */
		   
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal col_hea_id = null;
		col_hea_id = (BigDecimal) hidden.elementAt(0);
		String ekran="";
		ekran = (String) hidden.elementAt(5);
		String code="";
		code = (String) hidden.elementAt(6);

		BigDecimal col_cat_id = null;
		col_cat_id = (BigDecimal) hidden.elementAt(4);
		
		ra.setAttribute("ColWorkListLDB", "col_hea_id", col_hea_id);
		ra.setAttribute("ColWorkListLDB", "code", code);
		ra.setAttribute("ColWorkListLDB", "col_cat_id", col_cat_id);
		
		ra.setAttribute("ColWorkListLDB", "posting_flag", posting_flag);
		
		String ekran_ctx = ra.getScreenContext();
  
		if (code != null) {
		  
			if (code.equalsIgnoreCase("ZALI"))
				ra.setScreenTitle("Zalihe");
			else if (code.equalsIgnoreCase("OBVE"))
				ra.setScreenTitle("Obveznice");
			else if (code.equalsIgnoreCase("DION"))
				ra.setScreenTitle("Dionice");		
			else if (code.equalsIgnoreCase("ZAPI"))
				ra.setScreenTitle("Zapisi");
			else if (code.equalsIgnoreCase("UDJE"))
				ra.setScreenTitle("Udjeli");
		}
		if (ekran != null && ekran.trim().equalsIgnoreCase("X")) {
			ra.showMessage("inf_1800");
		} else {
// ako sam na referentskoj listi - ctx. za referenta	
			if (ekran_ctx.equalsIgnoreCase("scr_k")) {
// ako su osnovni podaci autorizirani referent ih vise ne moze mijenjati,samo unosi ostale podatke
				if (collateral_status.equalsIgnoreCase("3")) {
					ra.loadScreen(ekran.trim(),"scr_update_ref");					
				} else 
					ra.loadScreen(ekran.trim(),"scr_update");				
			} else  {
/*// ZA NEKRETNINE I MJENICE   
				if (code.equalsIgnoreCase("MJEN")) {  
					ra.loadScreen(ekran.trim(),"scr_update");						
				} else */
					ra.loadScreen(ekran.trim(),"scr_update_aut");
// ako sam na autorizacijskoj listi - ctx. za coll.off.
			}
			
			
		}    
	}
	
	public void add() {
// na F7		
		ra.loadScreen("ColTypeList","base");

 
	}	

	public void snd_arh () {
// na F( - slanje na arhivsku listu kod inicijalnog unosa collateral-a
// preduvjet:	status osnovnih podataka 1
//				status podataka collateral oficer-a 1
//				status hipoteke X ili 1
//				status povezanosti X ili 1	
		
		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("dealWar009");
			return;
		} 
		
		
		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector row=(Vector)td.getSelectedRowData();
		
		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));
		
// da li su potvrdjeni osnovni podaci, hipoteka, povezanost, podaci coll offf. - PROVJERIT CE COMMON
/*		String collateral_status =(String)row.elementAt(5);
		if (collateral_status.equalsIgnoreCase("0")) {
			ra.showMessage("wrn478");
			return;
		} */ 
		
//		saljem ga sa ref.liste za unos - 697223		
		ra.setAttribute("ColWorkListLDB", "source_list", "697223");
// na arhivsku listu - 700223	
		ra.setAttribute("ColWorkListLDB", "target_list", "700223");
		ra.setAttribute("ColWorkListLDB", "proc_status", "3");	
		ra.setAttribute("ColWorkListLDB", "action_type", "INICIJALNI UNOS");			
		 
		 
		Integer retValue = (Integer) ra.showMessage("col_qer007");
		if (retValue!=null) {
			if (retValue.intValue() == 0){ 
				return;
			}else{
				try{
					ra.executeTransaction();// CollSndArhiva
				}catch(VestigoTMException vtme){
					ra.showMessage(vtme.getMessageID());
				}
//		 		System.out.println("nije greska : "+ ra.getAttribute("ColWorkListLDB", "poruka")); 
		 		if (((Integer) ra.getAttribute("GDB", "TransactionStatus")).equals(new Integer("0"))) {
		 			String msg = (String) ra.getAttribute("ColWorkListLDB", "poruka");
		 			if (msg != null && (!(msg.trim().equalsIgnoreCase("")))) {
		 				ra.showMessage((String) ra.getAttribute("ColWorkListLDB", "poruka"));
		 			}	
		 		} 
				ra.refreshActionList("tblColWorkList");
			} 
		}		
		
	}
	
	public void confirm() {
// na F8 - slanje na verifikacju s radne liste
// preduvjet - status osnovnih podataka je 1 
// Milka, 22.09.2006 - akcija koja salje predmet s referentske liste dalje u obradu ovisno o parametrizaciji
// predmet moze biti poslan na verifikacijsku listu, listu collateral officera na dodatni unos i autorizaciju,
// na arhivsku listu
// ovo je akcija "salji dalje"		
		
		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("dealWar009");
			return;
		} 
		
		
		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector row=(Vector)td.getSelectedRowData();
		
		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));
		
// da li su potvrdjeni osnovni podaci, hipoteka, povezanost, podaci coll offf. - PROVJERIT CE COMMON		

		 
//	saljem ga sa ref.liste za unos - 697223		
		ra.setAttribute("ColWorkListLDB", "source_list", "697223");
// na listu za verifikaciju - 698223
// ne znam na koju ce listu u ovom trenutku		
//		ra.setAttribute("ColWorkListLDB", "target_list", "698223");
		ra.setAttribute("ColWorkListLDB", "target_list", "");		
//		ra.setAttribute("ColWorkListLDB", "proc_status", "1");	
		ra.setAttribute("ColWorkListLDB", "proc_status", "");			
		ra.setAttribute("ColWorkListLDB", "action_type", "");
		
//		ra.setAttribute("ColWorkListLDB", "action_type", "SLANJE NA VERIF.");		
		 
		 
//		Integer retValue = (Integer) ra.showMessage("col_qer001");
		Integer retValue = (Integer) ra.showMessage("col_qer011");
		if (retValue!=null) {
			if (retValue.intValue() == 0){ 
				return;
			}else{
				try{
					ra.executeTransaction();// CollWorkListSndVer - CO213.sqlj
				}catch(VestigoTMException vtme){
					ra.showMessage(vtme.getMessageID());
				}
//		 		System.out.println("nije greska : "+ ra.getAttribute("ColWorkListLDB", "poruka")); 
		 		if (((Integer) ra.getAttribute("GDB", "TransactionStatus")).equals(new Integer("0"))) {
		 			String msg = (String) ra.getAttribute("ColWorkListLDB", "poruka");
		 			if (msg != null && (!(msg.trim().equalsIgnoreCase("")))) {
		 				ra.showMessage((String) ra.getAttribute("ColWorkListLDB", "poruka"));
		 			}	
		 		} 
				ra.refreshActionList("tblColWorkList");
			} 
		}

	
	}
	 
	 
	public void add_cmnt(){ 

	}
	
	private String getScreenName(TableData td, int i){
		String screen="";
		Vector hidden=null;
		if(td!=null) hidden=(Vector) td.getSelectedRowUnique();
		if(hidden!=null && hidden.elementAt(i)!=null) screen=((String)hidden.elementAt(i)).trim();
		return screen;
	} 



 
   
   public void back(){
// akcija vrati referentu, za sada samo s arhivske liste   	
//	ra.showMessage("inf_1800");

// na F7 - vrati - vraca predmet referentu uz upis komentara
//	 akcija moguca na svim listama osim radne i monitoring 	
// 1. vracanje s arhivske liste: vraca predmet na radnu listu referenta koji ga je zadnji obradjivao (use_id)	
// mijenja status collateral-a na 0 i sve ostale statuse na pocetne prema parametrizaciji
// workflow indic na 3, update sloga za arhivsku listu, insert sloga za radnu listu, obavezan upis razloga vracanja 
	
// 2. vracanje s liste ponistenih(obustavljenih): vraca predmet na radnu listu referenta koji ga je zadnji obradjivao (use_id)	
//	 mijenja status collateral-a na 0 i sve ostale statuse na pocetne prema parametrizaciji
//	workflow indic na 4, update sloga za listu ponistenih, insert sloga za radnu listu, obavezan upis razloga vracanja

// Milka, 07.09.2006 - vracanje s arhivske liste - nova verzija
// akcija sada vraca predmet odabranom referentu (use_id_asg) u odabranoj org. jedinici (org_uni_id_asg)
// mijenja status collateral-a na 0 i sve ostale statuse na pocetne prema parametrizaciji
// workflow indic na 3, update sloga za arhivsku listu, insert sloga za radnu listu, obavezan upis razloga vracanja
   
// Milka, 25.09.2006:
// 1. vracanje s verifikacijske i autorizacijske liste:
// 	  vraca predmet na radnu listu referenta koji ga je zadnji obradjivao (use_id) i (org_uni_id)
//  mijenja status collateral-a na 0 i sve ostale statuse na pocetne prema parametrizaciji
//	workflow indic na 1, update sloga za verifikacijsku listu, insert sloga za radnu listu, obavezan upis razloga vracanja
//	workflow indic na 2, update sloga za autorizacijsku listu, insert sloga za radnu listu, obavezan upis razloga vracanja   	
 
//	 Milka, 30.05.2008 - vracanje s liste slobodnih referentu 
//	 akcija vraca predmet odabranom referentu (use_id_asg) u odabranoj org. jedinici (org_uni_id_asg)
//	 mijenja status collateral-a na 0 i sve ostale statuse na pocetne prema parametrizaciji
//	 workflow indic na 6, update sloga za listu slobodnih, insert sloga za radnu listu, obavezan upis razloga vracanja	   
	   
// provjeriti da li ima slogova na listi	
		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("dealWar009");
			return;
		} 

// provjeriti da li su upisani org. jedinica i referent kome se vraca predmet		
//		 provjeriti da li je upisana organizacijska jedinica i user
// na arhivskoj listi, listi ponistenih, listi odbijenih
// 	
		
		if (ra.getScreenContext().equalsIgnoreCase("scr_ver_list") || ra.getScreenContext().equalsIgnoreCase("scr_auth_list")) {
			ra.setAttribute("ColWorkListLDB", "use_id_asg",null);
			ra.setAttribute("ColWorkListLDB", "org_uni_id_asg", null);
		} else {
			if ((ra.getAttribute("ColWorkListLDB", "CollAssignment_txtUserName") == null
				|| ((String) ra.getAttribute("ColWorkListLDB","CollAssignment_txtUserName")).equals(""))
				&& (ra.getAttribute("ColWorkListLDB", "CollAssignment_txtLogin") == null
				|| ((String) ra.getAttribute("ColWorkListLDB","CollAssignment_txtLogin")).equals(""))) {
				ra.showMessage("wrnclt107"); 
				return;
			}
		}
		   
 		  
		
		Integer retValue = (Integer) ra.showMessage("col_qer008");
		if (retValue!=null && retValue.intValue() == 0) return;
			
		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
	    Vector row=(Vector)td.getSelectedRowData();
		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));
	    ra.setAttribute("ColWorkListLDB", "workflow_indic", (String)row.elementAt(0));
		String workflow_indic = (String) ra.getAttribute("ColWorkListLDB", "workflow_indic");
//	 vracanje s arhivske liste	- 700223
			if (ra.getScreenContext().equalsIgnoreCase("scr_arh_list") || ra.getScreenContext().equalsIgnoreCase("scr_arh_nek")) {
//	 	 
				ra.setAttribute("ColWorkListLDB", "source_list", "700223");
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "3");
				ra.setAttribute("ColWorkListLDB", "source_status", "3");
			} else if (ra.getScreenContext().equalsIgnoreCase("scr_canc_list")) {
//	vracanje s liste ponistenih  - 701223		
				ra.setAttribute("ColWorkListLDB", "source_list", "701223");		
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "4");
				ra.setAttribute("ColWorkListLDB", "source_status", "4");
			} else if (ra.getScreenContext().equalsIgnoreCase("scr_discard_list")) {
//	vracanje s liste odbijenih  - 702223		
				ra.setAttribute("ColWorkListLDB", "source_list", "702223");		
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "5"); 
				ra.setAttribute("ColWorkListLDB", "source_status", "5");
			} else if (ra.getScreenContext().equalsIgnoreCase("scr_ver_list")) {  
//  vracanje s verifikacijske liste 698223	
			 // provjeriti da li se vraca kolatearal koji ima workflow indic = 3, njemu se workflow indikator ne mijenja     
			    if (workflow_indic.equals("3"))
		             ra.setAttribute("ColWorkListLDB", "workflow_indic", "3"); 
			    else
		             ra.setAttribute("ColWorkListLDB", "workflow_indic", "1");
			    
				ra.setAttribute("ColWorkListLDB", "source_list", "698223");		
//				ra.setAttribute("ColWorkListLDB", "workflow_indic", "1"); 
				ra.setAttribute("ColWorkListLDB", "source_status", "1");
				
			} 	else if (ra.getScreenContext().equalsIgnoreCase("scr_auth_list")) {  
//			  vracanje s autorizacijske liste 698223	
				ra.setAttribute("ColWorkListLDB", "source_list", "699223");		
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "2"); 
				ra.setAttribute("ColWorkListLDB", "source_status", "2");
			} else if (ra.getScreenContext().equalsIgnoreCase("scr_inact_list") || ra.getScreenContext().equalsIgnoreCase("scr_inact_nek")) {
// vracanje s liste neaktivnih - 709223
				ra.setAttribute("ColWorkListLDB", "source_list", "709223");
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "7");
				ra.setAttribute("ColWorkListLDB", "source_status", "N");				
			} else if (ra.getScreenContext().equalsIgnoreCase("scr_free_list") || ra.getScreenContext().equalsIgnoreCase("scr_free_nek")) {
// vracanje s liste slobodnih - 710223	
				ra.setAttribute("ColWorkListLDB", "source_list", "710223");
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "6");
				ra.setAttribute("ColWorkListLDB", "source_status", "F");
			
			}  
			//	 na radnu listu 
			ra.setAttribute("ColWorkListLDB", "target_list", "697223");
			ra.setAttribute("ColWorkListLDB", "proc_status", "0");	
			ra.setAttribute("ColWorkListLDB", "action_type", "VRATI U OBRADU");	
			
			ra.loadScreen("CollListQCmnt", "scr_back");	
	
	
 
   } 
   
   public void back_all_coll(){
// vraca sve predmete na listi odabranom referentu   
//  na F8 - vrati sve - vraca sve predmet odabranom referentu uz upis komentara
//	 akcija moguca na svim listama osim radne i monitoring 	   	
//  akcija sada vraca predmet odabranom referentu (use_id_asg) u odabranoj org. jedinici (org_uni_id_asg)
//  mijenja status collateral-a na 0 i sve ostale statuse na pocetne prema parametrizaciji
//  workflow indic na 3, update sloga za arhivsku listu, insert sloga za radnu listu, obavezan upis razloga vracanja   	
//   		ra.showMessage("inf_1800");
//   		return;				
//   	 provjeriti da li ima slogova na listi	
   		
		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("dealWar009");
			return;
		} 

// provjeriti da li su upisani org. jedinica i referent kome se vraca predmet		
//		 provjeriti da li je upisana organizacijska jedinica i user
		if ((ra.getAttribute("ColWorkListLDB", "CollAssignment_txtUserName") == null
				|| ((String) ra.getAttribute("ColWorkListLDB","CollAssignment_txtUserName")).equals(""))
			&& (ra.getAttribute("ColWorkListLDB", "CollAssignment_txtLogin") == null
				|| ((String) ra.getAttribute("ColWorkListLDB","CollAssignment_txtLogin")).equals(""))) {
				ra.showMessage("wrnclt107"); 
			return;
		}  		
		 
		Integer retValue = (Integer) ra.showMessage("col_qer010");
		if (retValue!=null && retValue.intValue() == 0) return;
			
		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));
		
//	 vracanje s arhivske liste	- 700223
			if (ra.getScreenContext().equalsIgnoreCase("scr_arh_list") || ra.getScreenContext().equalsIgnoreCase("scr_arh_nek")) {
//	 	 
				ra.setAttribute("ColWorkListLDB", "source_list", "700223");
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "3");
				ra.setAttribute("ColWorkListLDB", "source_status", "3");
			} else if (ra.getScreenContext().equalsIgnoreCase("scr_canc_list")) {
//	vracanje s liste ponistenih  - 701223		
				ra.setAttribute("ColWorkListLDB", "source_list", "701223");		
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "4");
				ra.setAttribute("ColWorkListLDB", "source_status", "4");
			} else if (ra.getScreenContext().equalsIgnoreCase("scr_discard_list")) {
//	vracanje s liste odbijenih  - 702223		
				ra.setAttribute("ColWorkListLDB", "source_list", "702223");		
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "5"); 
				ra.setAttribute("ColWorkListLDB", "source_status", "5");
			} else if (ra.getScreenContext().equalsIgnoreCase("scr_inact_list") || ra.getScreenContext().equalsIgnoreCase("scr_inact_nek")) {
// vracanje s liste neaktivnih	- 709223	
				ra.setAttribute("ColWorkListLDB", "source_list", "709223");
				ra.setAttribute("ColWorkListLDB", "workflow_indic", "3");
				ra.setAttribute("ColWorkListLDB", "source_status", "N");				
			}
			   
			//	 na radnu listu 
			ra.setAttribute("ColWorkListLDB", "target_list", "697223");
			ra.setAttribute("ColWorkListLDB", "proc_status", "0");	
			ra.setAttribute("ColWorkListLDB", "action_type", "VRATI U OBRADU");	
			
			ra.loadScreen("CollListQCmnt", "scr_back_all");  		  	
   }  
  
   public void back_num_coll(){
//  vraca upisani broj predmeta sa liste odabranom referentu od trenutne pozicije cursora u tbl.   	
    		ra.showMessage("inf_1800");
    		return;				
//      	 provjeriti da li ima slogova na listi	
       		
    /*		if (isTableEmpty("tblColWorkList")) {
    			ra.showMessage("dealWar009");
    			return;
    		} 

//     provjeriti da li su upisani org. jedinica i referent kome se vraca predmet		
//    		 provjeriti da li je upisana organizacijska jedinica i user
    		if ((ra.getAttribute("ColWorkListLDB", "CollAssignment_txtUserName") == null
    				|| ((String) ra.getAttribute("ColWorkListLDB","CollAssignment_txtUserName")).equals(""))
    			&& (ra.getAttribute("ColWorkListLDB", "CollAssignment_txtLogin") == null
    				|| ((String) ra.getAttribute("ColWorkListLDB","CollAssignment_txtLogin")).equals(""))) {
    				ra.showMessage("wrnclt107"); 
    			return;
    		}  		
    		   
    		
    		
    		Integer retValue = (Integer) ra.showMessage("col_qer008");
    		if (retValue!=null && retValue.intValue() == 0) return;
    			
    		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
    		Vector hidden = (Vector) td.getSelectedRowUnique();
    		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));
    		
//    	 vracanje s arhivske liste	- 700223
    			if (ra.getScreenContext().equalsIgnoreCase("scr_arh_list")) {
//    	 	 
    				ra.setAttribute("ColWorkListLDB", "source_list", "700223");
    				ra.setAttribute("ColWorkListLDB", "workflow_indic", "3");
    				ra.setAttribute("ColWorkListLDB", "source_status", "3");
    			} else if (ra.getScreenContext().equalsIgnoreCase("scr_canc_list")) {
//    	vracanje s liste ponistenih  - 701223		
    				ra.setAttribute("ColWorkListLDB", "source_list", "701223");		
    				ra.setAttribute("ColWorkListLDB", "workflow_indic", "4");
    				ra.setAttribute("ColWorkListLDB", "source_status", "4");
    			} else if (ra.getScreenContext().equalsIgnoreCase("scr_discard_list")) {
//    	vracanje s liste odbijenih  - 702223		
    				ra.setAttribute("ColWorkListLDB", "source_list", "702223");		
    				ra.setAttribute("ColWorkListLDB", "workflow_indic", "5"); 
    				ra.setAttribute("ColWorkListLDB", "source_status", "5");
    			}   
    			   
    			//	 na radnu listu 
    			ra.setAttribute("ColWorkListLDB", "target_list", "697223");
    			ra.setAttribute("ColWorkListLDB", "proc_status", "0");	
    			ra.setAttribute("ColWorkListLDB", "action_type", "VRATI U OBRADU");	
    			
    			ra.loadScreen("CollListQCmnt", "scr_back");  */		  	 	  	
    }   
   
   
   public void verification_autorization(){
// akcija verificiraj na verifikacijskoj listi i autoriziraj na autorizacijskoj listi   	
   		if (isTableEmpty("tblColWorkList")) {
   			ra.showMessage("dealWar009");
   			return;
   		}  

		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));  		
   		
// verifikacija na verifikacijskoj listi
   		if (ra.getScreenContext().equalsIgnoreCase("scr_ver_list")) {
   			
// saljem ga sa verifikacijske na autorizacijsku ili arhivsku listu
   			
//   saljem ga sa liste za verifikaciju - 698223	
   			ra.setAttribute("ColWorkListLDB", "source_list", "698223");
//   na listu za autorizaciju - 699223	
// ne znam na koju listu ce otici   			
/*   			ra.setAttribute("ColWorkListLDB", "target_list", "699223");
   			ra.setAttribute("ColWorkListLDB", "proc_status", "2");	
   			ra.setAttribute("ColWorkListLDB", "action_type", "VERIFICIRAJ");	*/		   			
   			
   			ra.setAttribute("ColWorkListLDB", "target_list", "");
   			ra.setAttribute("ColWorkListLDB", "proc_status", "");	
   			ra.setAttribute("ColWorkListLDB", "action_type", "");
   			
   			
   			Integer retValue = (Integer) ra.showMessage("col_qer005");
   			if (retValue!=null) {
   				if (retValue.intValue() == 0){ 
   					return;
   				}else{
   					try{
   						ra.executeTransaction();// CollVerification
   					}catch(VestigoTMException vtme){
   						ra.showMessage(vtme.getMessageID());
   					}
//   			 		System.out.println("nije greska : "+ ra.getAttribute("ColWorkListLDB", "poruka")); 
   			 		if (((Integer) ra.getAttribute("GDB", "TransactionStatus")).equals(new Integer("0"))) {
   			 			String msg = (String) ra.getAttribute("ColWorkListLDB", "poruka");
   			 			if (msg != null && (!(msg.trim().equalsIgnoreCase("")))) {
   			 				ra.showMessage((String) ra.getAttribute("ColWorkListLDB", "poruka"));
   			 			}	
   			 		} 
   					ra.refreshActionList("tblColWorkList");
   				} 
   			}
   			
   			
   		} else if (ra.getScreenContext().equalsIgnoreCase("scr_auth_list")) {
// autorizacija na autorizacijskoj listi   	
//   saljem ga sa autorizacijske na arhivsku listu ili radnu listu
   			
//   saljem ga sa liste za autorizaciju - 699223	
   			ra.setAttribute("ColWorkListLDB", "source_list", "699223");
//   na arhivsku listu - 700223	
// ne znam na koju listu ce otici   			
   			ra.setAttribute("ColWorkListLDB", "target_list", "700223");
   			ra.setAttribute("ColWorkListLDB", "proc_status", "3");	
   			ra.setAttribute("ColWorkListLDB", "action_type", "AUTORIZIRAJ");		

    			  
  			Integer retValue = (Integer) ra.showMessage("col_qer006");
  			if (retValue!=null) {
  				if (retValue.intValue() == 0){ 
  					return;
  				}else{
  					try{
  						ra.executeTransaction();// CollAuthorization
  					}catch(VestigoTMException vtme){
  						ra.showMessage(vtme.getMessageID());
  					}
//  			 		System.out.println("nije greska : "+ ra.getAttribute("ColWorkListLDB", "poruka")); 
  			 		if (((Integer) ra.getAttribute("GDB", "TransactionStatus")).equals(new Integer("0"))) {
  			 			String msg = (String) ra.getAttribute("ColWorkListLDB", "poruka");
  			 			if (msg != null && (!(msg.trim().equalsIgnoreCase("")))) {
  			 				ra.showMessage((String) ra.getAttribute("ColWorkListLDB", "poruka"));
  			 			}	
  			 		} 
  					ra.refreshActionList("tblColWorkList");
  				} 
  			}
  		  			
   		}
 
   }
  
   
   public void deact_mortgage () {
//   		ra.showMessage("inf_1800");
   		TableData td=null;
  
   		if (isTableEmpty("tblColWorkList")) {
   			ra.showMessage("wrn299");
   			return;
   		}
   		td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
	
   		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector row=(Vector)td.getSelectedRowData();
   		
   		BigDecimal col_hea_id = (BigDecimal) hidden.elementAt(0);
		String code = (String) hidden.elementAt(6);   		
   		String mortgage = (String) row.elementAt(6);
   		String col_num = (String) row.elementAt(2);
   		
   		if (mortgage.equalsIgnoreCase("X")) {
   			ra.showMessage("wrnclt132");
   			return;
   		}
   		 
   		ra.setAttribute("ColWorkListLDB", "col_hea_id", col_hea_id);
		ra.setAttribute("ColWorkListLDB", "code", code);    		
		ra.setAttribute("ColWorkListLDB", "Kolateral_txtColNumQBE", col_num);
		
		ra.loadScreen("KolMortgageDemo","scr_deact_mortgage"); 
	  
   } 
   
   public void print () {
//     ra.showMessage("inf_1800");
   TableData td=null;

   if (isTableEmpty("tblColWorkList")) {
       ra.showMessage("wrn299");
       return;
   }
   td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");

   Vector hidden = (Vector) td.getSelectedRowUnique();
   Vector row=(Vector)td.getSelectedRowData();
   
   BigDecimal col_hea_id = (BigDecimal) hidden.elementAt(0);
   String code = (String) hidden.elementAt(6);         
   String mortgage = (String) row.elementAt(6);
   String col_num = (String) row.elementAt(2);
   
   if (mortgage.equalsIgnoreCase("X")) {
       ra.showMessage("wrnclt132");
       return;
   }
    
   ra.setAttribute("ColWorkListLDB", "col_hea_id", col_hea_id);
   ra.setAttribute("ColWorkListLDB", "code", code);            
   ra.setAttribute("ColWorkListLDB", "Kolateral_txtColNumQBE", col_num);
   
   ra.loadScreen("KolMortgageDemo","scr_print"); 
   
} 

   
	public boolean isTableEmpty(String tableName) {
		TableData td = (TableData) ra.getAttribute(tableName);
		if (td == null)
			return true;

		if (td.getData().size() == 0)
			return true;

		return false;
	}
	
  
	
// validacija user-a i organizacijske jedinice kod preraspodjele predmeta	

	
	public boolean CollAssignment_txtOrgUnit_FV(String elementName, Object elementValue,Integer lookUpType) {
		
		
		if(elementValue == null || ((String)elementValue).equals("")){
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtOrgCode", "");
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtOrgUnit", "");
			ra.setAttribute("ColWorkListLDB", "org_uni_id_asg", null);
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtUserName", "");
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtLogin", "");
			ra.setAttribute("ColWorkListLDB", "use_id_asg", null);
			return true;
		}
		if (ra.getCursorPosition().equals("CollAssignment_txtOrgCode")) {
		     ra.setAttribute("ColWorkListLDB", "CollAssignment_txtOrgUnit", "");
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("CollAssignment_txtOrgUnit")) {
		     ra.setAttribute("ColWorkListLDB", "CollAssignment_txtOrgCode", "");
		 }	
	
		LookUpRequest lookUpRequest = new LookUpRequest("OrgUniLookUp");
		lookUpRequest.addMapping("ColWorkListLDB","CollAssignment_txtOrgCode","code");
		lookUpRequest.addMapping("ColWorkListLDB","CollAssignment_txtOrgUnit","name");
		lookUpRequest.addMapping("ColWorkListLDB","org_uni_id_asg","org_uni_id");
		
		try {
			ra.callLookUp(lookUpRequest);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtOrgCode", "");
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtOrgUnit", "");
			ra.setAttribute("ColWorkListLDB", "org_uni_id_asg", null);
			ra.setCursorPosition("CollAssignment_txtOrgCode");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		if(elementName.equals("CollAssignment_txtOrgCode") || 
				elementName.equals("CollAssignment_txtOrgUnit")){
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtUserName", "");
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtLogin", "");
			ra.setAttribute("ColWorkListLDB", "use_id_asg", null);			
		} 	
		ra.setCursorPosition("CollAssignment_txtLogin");
		 
		return true;
	}	 
	 
	 
	 
	public boolean CollAssignment_txtLogin_FV  (String elementName, Object elementValue,Integer lookUpType) {

		if(elementValue == null || ((String)elementValue).equals("")){
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtUserName", "");
			ra.setAttribute("ColWorkListLDB", "CollAssignment_txtLogin", "");
			ra.setAttribute("ColWorkListLDB", "use_id_asg", null);
			return true;
		}
		 
		if ((ra.getAttribute("ColWorkListLDB", "CollAssignment_txtOrgCode") == null) || 
				ra.getAttribute("ColWorkListLDB", "CollAssignment_txtOrgCode").equals("")){
			ra.showMessage("inf216");
			return false;
		}

		if (ra.getCursorPosition().equals("CollAssignment_txtLogin")) {
		     ra.setAttribute("ColWorkListLDB", "CollAssignment_txtUserName", "");
		     ra.setCursorPosition(2);
		 } else if (ra.getCursorPosition().equals("CollAssignment_txtUserName")) {
		     ra.setAttribute("ColWorkListLDB", "CollAssignment_txtLogin", "");
		 }		
		
		
		if (!ra.isLDBExists("AppUserOrgLDB")) ra.createLDB("AppUserOrgLDB");
		ra.setAttribute("AppUserOrgLDB", "org_uni_id",  ra.getAttribute("ColWorkListLDB", "org_uni_id_asg"));	
		
// 		System.out.println("org_uni_id : "+ra.getAttribute("AppUserOrgLDB", "org_uni_id"));
//		System.out.println("user : "+ra.getAttribute("ColWorkListLDB", "CollAssignment_txtLogin"));	
//		System.out.println("name : "+ra.getAttribute("ColWorkListLDB", "CollAssignment_txtUserName"));	
 		
		ra.setAttribute("ColWorkListLDB", "dummySt", "");
		ra.setAttribute("ColWorkListLDB", "dummyBD", null);
 
		LookUpRequest request = new LookUpRequest("AppUserOrgLookUp");
		request.addMapping("ColWorkListLDB", "use_id_asg", "use_id");
		request.addMapping("ColWorkListLDB", "CollAssignment_txtLogin", "login");
		request.addMapping("ColWorkListLDB", "CollAssignment_txtUserName", "user_name");
		request.addMapping("ColWorkListLDB", "dummySt", "abbreviation");
		request.addMapping("ColWorkListLDB", "dummyBD", "org_uni_id");
		try {  
			ra.callLookUp(request);
		} catch (EmptyLookUp elu) {
			ra.showMessage("err012");
			return false;
		} catch (NothingSelected ns) {
			return false;
		}
		return true;
        
		
	} // CollAssignment_txtLogin_FV	
	
	public void change_ref() {
// preraspodjela predmeta drugom referentu
// moze se preraspodijeliti predmet koji je na referentskoj listi		
//		ra.showMessage("inf_1800");
//		return;				
		
   		if (isTableEmpty("tblColWorkList")) {
   			ra.showMessage("dealWar009");
   			return;
   		} 

// provjeriti da li je predmet na referentskoj listi, ako nije poruka
		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector row = (Vector) td.getSelectedRowData();
		String lista = (String) row.elementAt(1);

		
		if (!(lista.equalsIgnoreCase("0"))) {
			ra.showMessage("wrnclt108");
			return;
		}
   		  
//   	provjeriti da li su upisani org. jedinica i referent kome se vraca predmet	
   		if ((ra.getAttribute("ColWorkListLDB", "CollAssignment_txtUserName") == null
   				|| ((String) ra.getAttribute("ColWorkListLDB","CollAssignment_txtUserName")).equals(""))
   				&& (ra.getAttribute("ColWorkListLDB", "CollAssignment_txtLogin") == null
   				|| ((String) ra.getAttribute("ColWorkListLDB","CollAssignment_txtLogin")).equals(""))) {
   				ra.showMessage("wrnclt107"); 
   				return;
   		}
  
   		  
   		
   		Integer retValue = (Integer) ra.showMessage("col_qer012");
   		if (retValue!=null && retValue.intValue() == 0) return;	
		
//		TableData td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
		Vector hidden = (Vector) td.getSelectedRowUnique();
		ra.setAttribute("ColWorkListLDB", "col_hea_id", (BigDecimal) hidden.elementAt(0));  	
						 
//		vracanje s monitoring liste 000000	
//		vracam ustvari s neke radne liste

		ra.setAttribute("ColWorkListLDB", "source_list", "697223");
		ra.setAttribute("ColWorkListLDB", "workflow_indic", "0");
		ra.setAttribute("ColWorkListLDB", "source_status", "0");		
		//	 na radnu listu 
		ra.setAttribute("ColWorkListLDB", "target_list", "697223");
		ra.setAttribute("ColWorkListLDB", "proc_status", "0");	
		ra.setAttribute("ColWorkListLDB", "action_type", "PRERASPODIJELI");	
		
		ra.loadScreen("CollListQCmnt", "scr_back");	

		
	}
	public void coll_exposure() {
//		 na F2		
				
		TableData td=null;

		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("wrn299");
			return;
		}
		td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
				
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal col_hea_id = null;
		col_hea_id = (BigDecimal) hidden.elementAt(0);
		
		

		ra.setAttribute("ColWorkListLDB", "col_hea_id", col_hea_id);
		
		ra.loadScreen("ColWorkListCov","scr_col");
	}		  
	
	public void reva_amort_view() {
		
		TableData td=null;

		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("wrn299");
			return;
		}
		td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");
				
		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal col_hea_id = null;
		col_hea_id = (BigDecimal) hidden.elementAt(0);
		
		

		ra.setAttribute("ColWorkListLDB", "col_hea_id",col_hea_id);
		
		ra.loadScreen("CollWorkups","scr_base");
		
		
	}
	
	public void deact_kolateral() {
//		ra.showMessage("inf_1800");
// deaktivacija kolaterala, dozvoljena samo za kolaterale na kojima nema hipoteke		
		TableData td=null;

		if (isTableEmpty("tblColWorkList")) {
			ra.showMessage("wrn299");
			return;
		} 
		td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");

		Vector hidden = (Vector) td.getSelectedRowUnique();
		Vector row=(Vector)td.getSelectedRowData();
		
		BigDecimal col_hea_id = (BigDecimal) hidden.elementAt(0);
		String mortgage = (String) row.elementAt(6);
 
		
		if (!mortgage.equalsIgnoreCase("X")) {
			ra.showMessage("wrnclt134");
			return;
		}
		 
		ra.setAttribute("ColWorkListLDB", "col_hea_id", col_hea_id);
	
		Integer retValue = (Integer) ra.showMessage("col_qer024");

		if (retValue!=null) {
			if (retValue.intValue() == 0) 
				return;
		}
			
		try{
			ra.executeTransaction();// KolateralDeact - CO344.sqlj
		}catch(VestigoTMException vtme){
			ra.showMessage(vtme.getMessageID());
		}
		
		ra.refreshActionList("tblColWorkList");
  
	} 
	/**
	 * Metoda koja otvara ekran za pretrazivanje liste slobodnih i deaktiviranih kolaterala
	 */	
	public void srch_free_deact() {
//		 na F7		
		ra.loadScreen("KolateralQBE", "scr_free_deact");

	}      
	 
	public void deact_new_owner() {
//	    na F8      
//	    ra.showMessage("inf_1800");
//	    return;             
	    TableData td=null;

	    if (isTableEmpty("tblColWorkList")) {
	        ra.showMessage("wrn299");
	        return;
	    }
	    td = (TableData) ra.getAttribute("ColWorkListLDB", "tblColWorkList");

	    Vector hidden = (Vector) td.getSelectedRowUnique();
	    BigDecimal col_hea_id = null;
	    col_hea_id = (BigDecimal) hidden.elementAt(0);
	    String ekran="";
	    ekran = (String) hidden.elementAt(5);
	    String code="";
	    code = (String) hidden.elementAt(6);

	    BigDecimal col_cat_id = null;
	    col_cat_id = (BigDecimal) hidden.elementAt(4);
//        System.out.println("col_cat_id : "+col_cat_id);

	    if((col_cat_id.compareTo(new BigDecimal("611223"))!=0)&& (col_cat_id.compareTo(new BigDecimal("617223"))!=0)&&
	       (col_cat_id.compareTo(new BigDecimal("623223"))!=0)&&(col_cat_id.compareTo(new BigDecimal("628223"))!=0)&&
	       (col_cat_id.compareTo(new BigDecimal("614223"))!=0)&&(col_cat_id.compareTo(new BigDecimal("625223"))!=0)
	       ){
 
	        ra.setAttribute("ColWorkListLDB", "col_cat_id", col_cat_id);
	        ra.setAttribute("ColWorkListLDB", "col_hea_id", col_hea_id);
	        ra.setAttribute("ColWorkListLDB", "code", code); 

	        if (code != null) {

	            if (code.equalsIgnoreCase("ZALI"))
	                ra.setScreenTitle("Zalihe");
	            else if (code.equalsIgnoreCase("OBVE"))
	                ra.setScreenTitle("Obveznice");
	            else if (code.equalsIgnoreCase("DION"))
	                ra.setScreenTitle("Dionice");       
	            else if (code.equalsIgnoreCase("ZAPI"))
	                ra.setScreenTitle("Zapisi");
	            else if (code.equalsIgnoreCase("UDJE"))
	                ra.setScreenTitle("Udjeli");
	        }
	        if (ekran != null && ekran.trim().equalsIgnoreCase("X")) {
	            ra.showMessage("inf_1800");
	        } else {
	            ra.loadScreen(ekran.trim(),"scr_detail_deact");
	        }
	    }else{
         // poruka kolateral se ne knjizi-potrebno je samo promjeniti vlasnika
            ra.showMessage("infKolDeact02");
        }
        
        ra.refreshActionList("tblColWorkList");
        
	}

}  

