package hr.vestigo.modules.collateral;
  
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Vector;  
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.framework.common.TableData;
            
/**      
 * @author HRASIA
 *
 * PRIKAZ POCETNOG EKRANA HIPOTEKA I PUNJENJE TABLICE
 */
public class CollHfPrior extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollHfPrior.java,v 1.71 2017/11/23 13:54:35 hrakis Exp $"; 

	CollateralUtil coll_util = null;	
	public CollHfPrior(ResourceAccessor ra) {
		super(ra);
		coll_util = new CollateralUtil(ra);
	}
	   
	public void CollHfPrior_SE() {
		ra.createLDB("CollHfPriorLDB");
		ra.createActionListSession("tblCollHfPrior", false);
		//
	  
		if( ra.getScreenContext().equalsIgnoreCase("base_re") ){
			
			BigDecimal colHeaId = null;//kljuc coll_head
			BigDecimal colSecId = null;//kljuc podtablice
			BigDecimal curIdColateral = null;//valuta kolaterala 

			BigDecimal HfTableIdBd = new BigDecimal("0.0");
			
			String colNum = null;				//Jedinstveni identifikacijski broj collateral-a

			String HfTableIdSCV = "";
			String HfTableIdSCD = "";
			
			BigDecimal fraAgrId = null;
			
// 1602611003			
          	if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI")) {
          		
          		HfTableIdBd = new BigDecimal("1602610003.0");
    			HfTableIdSCV = "vehicle";
    			HfTableIdSCD = "VOZILO";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");	
    			  
    			  
          	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV")) {
          		HfTableIdBd = new BigDecimal("1602611003.0");
    			HfTableIdSCV = "vessel";
    			HfTableIdSCD = "PLOVILO";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");	

          	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR")) {
          		HfTableIdBd = new BigDecimal("1602612003.0");
    			HfTableIdSCV = "movable";
    			HfTableIdSCD = "POKRETNINA";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");	

          	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI")) {
          		HfTableIdBd = new BigDecimal("1602613003.0");
    			HfTableIdSCV = "supply";
    			HfTableIdSCD = "ZALIHA";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");	
         	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("DION") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("OBVE") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJE") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZAPI") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJP")) {
          		HfTableIdBd = new BigDecimal("1604184003.0");
    			HfTableIdSCV = "security";
    			HfTableIdSCD = "VRIJEDNOSNICA";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");	
    			
          	} else {		
    			HfTableIdBd = new BigDecimal("1602609003.0");
          		HfTableIdSCV = "real_estate";
          		HfTableIdSCD = "NEKRETNINA";  
    			colHeaId       = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_REAL_EST_NM_CUR_ID");
    			colNum 		   = (String)ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCode");
    			
    			colSecId       = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_RES_ID");
          	}
          	

			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_REF_ID", colSecId); 
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_COLL_HEAD_ID", colHeaId); 
			ra.setAttribute("CollHfPriorLDB", "HF_CUR_ID", curIdColateral); 
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtColNum",colNum);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID",HfTableIdBd);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeValue",HfTableIdSCV);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeDesc",HfTableIdSCD);
			
			ra.setAttribute("CollHfPriorLDB", "HF_FRA_AGR_ID",fraAgrId);
//			System.out.println("KLJUC PODTABLICE           colSecId        " + colSecId);
//			System.out.println("KLJUC COLL_HEAD            colHeaId        " + colHeaId);
//			System.out.println("VALUTA KOLATERALA          curIdColateral  " + curIdColateral);
			
			HfTableIdBd = null;
			HfTableIdSCV = null;
			HfTableIdSCD = null;										
			curIdColateral = null;
			if(colSecId != null){
				ra.refreshActionList("tblCollHfPrior");
			}
			colHeaId = null;
			colSecId = null;
			colNum = null;
			fraAgrId = null;
		}	
		
		if( ra.getScreenContext().equalsIgnoreCase("detail_re") ){
			BigDecimal colHeaId = null;//kljuc coll_head
			BigDecimal colSecId = null;//kljuc podtablice
			BigDecimal curIdColateral = null;//valuta kolaterala 
//			BigDecimal HfTableIdBd = new BigDecimal("1602609003.0");
			BigDecimal HfTableIdBd = new BigDecimal("0.0");			
			String colNum = null;				//Jedinstveni identifikacijski broj collateral-a
			
			String HfTableIdSCV = "";
			String HfTableIdSCD = "";
			
			BigDecimal fraAgrId = null;
			
         	if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI")) {
          		HfTableIdBd = new BigDecimal("1602610003.0");
    			HfTableIdSCV = "vehicle";
    			HfTableIdSCD = "VOZILO";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");
          	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV")) {
          		HfTableIdBd = new BigDecimal("1602611003.0");
       			HfTableIdSCV = "vessel";
    			HfTableIdSCD = "PLOVILO";          
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");
          	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR")) {
          		HfTableIdBd = new BigDecimal("1602612003.0");
    			HfTableIdSCV = "movable";
    			HfTableIdSCD = "POKRETNINA";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");	

          	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI")) {
          		HfTableIdBd = new BigDecimal("1602613003.0");
    			HfTableIdSCV = "supply";
    			HfTableIdSCD = "ZALIHA";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");	
    			
         	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("DION") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("OBVE") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJE") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZAPI") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJP")) {
          		HfTableIdBd = new BigDecimal("1604184003.0");
    			HfTableIdSCV = "security";
    			HfTableIdSCD = "VRIJEDNOSNICA";        
    			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
    			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
    			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");	
    			
          	} else {	
          		HfTableIdBd = new BigDecimal("1602609003.0");			
          		HfTableIdSCV = "real_estate";
          		HfTableIdSCD = "NEKRETNINA";
    			colHeaId       = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID");
    			curIdColateral = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_REAL_EST_NM_CUR_ID");
    			colNum 		   = (String)ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCode");
    			
    			colSecId       = (BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_RES_ID");
          	}
         	
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_REF_ID", colSecId); 
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_COLL_HEAD_ID", colHeaId); 
			ra.setAttribute("CollHfPriorLDB", "HF_CUR_ID", curIdColateral); 
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtColNum",colNum);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID",HfTableIdBd);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeValue",HfTableIdSCV);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeDesc",HfTableIdSCD);
			
			ra.setAttribute("CollHfPriorLDB", "HF_FRA_AGR_ID",fraAgrId);
//			System.out.println("KLJUC PODTABLICE           colSecId        " + colSecId);
//			System.out.println("KLJUC COLL_HEAD            colHeaId        " + colHeaId);
//			System.out.println("VALUTA KOLATERALA          curIdColateral  " + curIdColateral);
			
			HfTableIdBd = null;
			HfTableIdSCV = null;
			HfTableIdSCD = null;										
			curIdColateral = null;
			if(colSecId != null){
				ra.refreshActionList("tblCollHfPrior");
			}
			colHeaId = null; 
			colSecId = null;
			colNum = null;
			fraAgrId = null;

		}		
		
		if( ra.getScreenContext().equalsIgnoreCase("base_ca") ){
			BigDecimal colHeaId = null;//kljuc coll_head
			BigDecimal colSecId = null;//kljuc podtablice
			BigDecimal curIdColateral = null;//valuta kolaterala 
			BigDecimal HfTableIdBd = new BigDecimal("1609188003.0");
			String colNum = null;				//Jedinstveni identifikacijski broj collateral-a
			
			String HfTableIdSCV = "cash_deposit";
			String HfTableIdSCD = "GOTOVINSKI DEPOZIT";
 			
			BigDecimal fraAgrId = null;
			
			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
			
			
			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");
			
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_REF_ID", colSecId); 
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_COLL_HEAD_ID", colHeaId);  
			ra.setAttribute("CollHfPriorLDB", "HF_CUR_ID", curIdColateral);  
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtColNum",colNum);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID",HfTableIdBd);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeValue",HfTableIdSCV);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeDesc",HfTableIdSCD);
			
			ra.setAttribute("CollHfPriorLDB", "HF_FRA_AGR_ID",fraAgrId);
			
/*			System.out.println("KLJUC PODTABLICE           colSecId        " + colSecId);
			System.out.println("KLJUC COLL_HEAD            colHeaId        " + colHeaId);
			System.out.println("VALUTA KOLATERALA          curIdColateral  " + curIdColateral);*/
			
			HfTableIdBd = null;
			HfTableIdSCV = null;
			HfTableIdSCD = null;										
			curIdColateral = null;
			if(colSecId != null){
				ra.refreshActionList("tblCollHfPrior");
			}
			colHeaId = null;
			colSecId = null;
			colNum = null;
			fraAgrId = null;
			
		}		  
		  
		if( ra.getScreenContext().equalsIgnoreCase("detail_ca") ){
			BigDecimal colHeaId = null;//kljuc coll_head
			BigDecimal colSecId = null;//kljuc podtablice
			BigDecimal curIdColateral = null;//valuta kolaterala 
			BigDecimal HfTableIdBd = new BigDecimal("1609188003.0");
			String colNum = null;				//Jedinstveni identifikacijski broj collateral-a
			
			String HfTableIdSCV = "cash_deposit";
			String HfTableIdSCD = "GOTOVINSKI DEPOZIT";
			
			BigDecimal fraAgrId = null;
			
			colHeaId = (BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
			curIdColateral = (BigDecimal) ra.getAttribute("CollHeadLDB","REAL_EST_NM_CUR_ID");
			colNum 	 = (String)ra.getAttribute("CollHeadLDB","Coll_txtCode");
			
			
			colSecId = (BigDecimal) ra.getAttribute("CollSecPaperDialogLDB","COL_SEC_ID");
			
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_REF_ID", colSecId); 
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_COLL_HEAD_ID", colHeaId);  
			ra.setAttribute("CollHfPriorLDB", "HF_CUR_ID", curIdColateral);  
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtColNum",colNum);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID",HfTableIdBd);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeValue",HfTableIdSCV);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeDesc",HfTableIdSCD);
			
			ra.setAttribute("CollHfPriorLDB", "HF_FRA_AGR_ID",fraAgrId);
			  
//			System.out.println("KLJUC PODTABLICE           colSecId        " + colSecId);
//			System.out.println("KLJUC COLL_HEAD            colHeaId        " + colHeaId);
//			System.out.println("VALUTA KOLATERALA          curIdColateral  " + curIdColateral);
			
			HfTableIdBd = null;
			HfTableIdSCV = null;
			HfTableIdSCD = null;										
			curIdColateral = null;
			if(colSecId != null){
				ra.refreshActionList("tblCollHfPrior");
			}
			colHeaId = null;
			colSecId = null;
			colNum = null;
			fraAgrId = null;
		
		} 
		
// Milka, prikaz hipoteka koje su dio okvirnog sporazuma		
		if (ra.getScreenContext().equalsIgnoreCase("detail_agr")) {
			BigDecimal colHeaId = null;//kljuc coll_head
			BigDecimal colSecId = null;//kljuc podtablice
			BigDecimal curIdColateral = null;//valuta kolaterala 
			BigDecimal HfTableIdBd = new BigDecimal("1705155003.0");
			String colNum = null;				//Jedinstveni identifikacijski broj collateral-a
			
			String HfTableIdSCV = "frame_agreement";
			String HfTableIdSCD = "OKVIRNI SPORAZUM";
			
			BigDecimal fraAgrId = null;
			
			fraAgrId = (BigDecimal) ra.getAttribute("AgreementLDB","fra_agr_id");
			
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_REF_ID", colSecId); 
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_COLL_HEAD_ID", colHeaId);  
			ra.setAttribute("CollHfPriorLDB", "HF_CUR_ID", curIdColateral);  
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtColNum",colNum);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID",HfTableIdBd);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeValue",HfTableIdSCV);
			ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeDesc",HfTableIdSCD);	
			
			ra.setAttribute("CollHfPriorLDB", "HF_FRA_AGR_ID",fraAgrId);
			
			
			
			HfTableIdBd = null;
			HfTableIdSCV = null;
			HfTableIdSCD = null;										
			curIdColateral = null;
			if(fraAgrId != null){
				ra.refreshActionList("tblCollHfPrior");
			}
			colHeaId = null;
			colSecId = null;
			colNum = null;
			fraAgrId = null;
			
		}

	}//CollHfPrior_SE
	
	 
	public void add(){
// Dizanje ekrana hipoteke nakon definicije akcije
// String vrsta = (String) ra.getAttribute("ColWorkListLDB","code");
// ako su obveznice ili zapisi, 
//		 ako je obveznica dospjela ne dozvoliti upis hipoteke 				
		if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("OBVE") ||
				((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZAPI")) {	
			Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
			Date maturity_date = (Date) ra.getAttribute("CollSecPaperDialogLDB", "Coll_txtMaturityDate");
//			datum mora biti veci ili jednak current date

			if (maturity_date == null || current_date == null) {
				
			} else {

				if ((maturity_date).before(current_date)) {
					ra.showMessage("wrnclt150");
					return;
				}
			}
		}
		
//-----		
		if( ra.getScreenContext().equalsIgnoreCase("base_re") ){
				ra.loadScreen("CollHfPriorDialog","scr_changere");
		}
		             

		if( ra.getScreenContext().equalsIgnoreCase("base_ca") ){   
// 16.06.2010 - dodati provjeru na aktivnost hipoteke, moze biti upisana samo jedna aktivna hipoteka	
// 02.09.2010 - promjena - za HBOR kredite dozvoliti unos i vise od jedne hipoteke		     
            if (isTableOneRow("tblCollHfPrior")) {
//                ra.showMessage("wrnclt95");
                Integer retValue = (Integer) ra.showMessage("col_qer031");
                if (retValue!=null && retValue.intValue() == 0) return;
                else ra.loadScreen("CollHfPriorDialog","scr_changere"); 
                
            }else{
                ra.loadScreen("CollHfPriorDialog","scr_changere"); 
            }
			       
		}//base_ca		 
	}   
  
    public boolean isTableOneRow(String tableName) {
        
        if (isTableEmpty(tableName))
            return false;
        
        TableData tab_input = (TableData) ra.getAttribute(tableName);
        Vector data = tab_input.getData();
 
        for (int i = 0; i < tab_input.getUnique().size(); i++) {
            Vector row_in = (Vector) data.elementAt(i);
            String flag = "" + row_in.elementAt(0);
            if (!(flag.equalsIgnoreCase("NA")))
                return true;
        }
        
        return false;
    }         
	
	public void getRegisterNoName(){
		
	}
	
	public void details(){
		//F4
		if (isTableEmpty("tblCollHfPrior")) {
			ra.showMessage("wrn299");
			return;
		}
		ra.loadScreen("CollHfPriorDialog","scr_detail"); 
	}//details
	
	   public void mrtg_history(){
	        //F4
	        if (isTableEmpty("tblCollHfPrior")) {
	            ra.showMessage("wrn299");
	            return;
	        }
	        
	        TableData td = (TableData) ra.getAttribute("CollHfPriorLDB","tblCollHfPrior");
       
	        Vector hidden = (Vector) td.getSelectedRowUnique(); //skriveni      
	        Vector data = td.getSelectedRowData(); //vidljivi
	        
	        String hf_status= (String) data.elementAt(0);
	        BigDecimal mrtg_id = (BigDecimal) hidden.elementAt(0);
	        ra.setAttribute("CollHfPriorLDB","mrtg_id", mrtg_id);
	        
	        if (hf_status.equals("NA")){
	            ra.loadScreen("MortageHistDetails","defaultContext"); 
	        }else {
	            ra.showMessage("wrn_dist_rep16");
	        }
	        
	        
	       
	    }//details
	
	public void loan_ben(){
		//F6
		
		if (isTableEmpty("tblCollHfPrior")) {
			ra.showMessage("wrn299");
			return;
		}
		 
		TableData tdSel = (TableData) ra.getAttribute("CollHfPriorLDB", "tblCollHfPrior");
		Vector vidljiviVector = (Vector) tdSel.getSelectedRowData();
		Vector hidden = (Vector) tdSel.getSelectedRowUnique();		
		
		String interIdBen = (String) vidljiviVector.elementAt(3);
		String upisanoPravo = (String) hidden.elementAt(4);             
		String sporazum = (String) hidden.elementAt(5);
		String mortgage_prority = (String) vidljiviVector.elementAt(0);
		
//		System.out.println("UPISANO PRAVO BANKE |" + upisanoPravo + "|");
		
		if(interIdBen != null){
			interIdBen = interIdBen.trim();
		}
//		System.out.println("HIPOTEKA JE OD |" + interIdBen + "|");

		if(interIdBen.compareTo("910000") != 0){
			//Nije mooguce upisivati partije plasmana na hipoteku koja nije u korist RBA
			ra.showMessage("wrnclt62");
			return;
		
		} else {
// ako je hipoteka u korist RBA i ako je vezana na okvirni sporazum ne povezuje se ovdje s plasmanom
//			 ako je hipoteka vezana za okvirni sporazum ovdje se ne povezuje s plasmanima
    		if (sporazum != null && sporazum.equalsIgnoreCase("D")) {
// ako je prikaz detalja, dozvoliti prikaz
    			if ( ra.getScreenContext().equalsIgnoreCase("detail_re") || ra.getScreenContext().equalsIgnoreCase("detail_ca")) {
    				
    			} else {
    				ra.showMessage("wrnclt130");
//    				ra.loadScreen("LoanBeneficiary","detail");				
    				return;
    			}
    		}  
		}  
 
		
		if( ra.getScreenContext().equalsIgnoreCase("detail_re") || ra.getScreenContext().equalsIgnoreCase("detail_ca")){
			ra.loadScreen("LoanBeneficiary","detail");
		}

		if( ra.getScreenContext().equalsIgnoreCase("base_re") || ra.getScreenContext().equalsIgnoreCase("base_ca")){
//			 Milka, 25.04.2007, ako je hipoteka neaktivna ne moze se vezati s plasmanima-prikaz detalja			
			if (mortgage_prority.equalsIgnoreCase("NA")) {
				ra.loadScreen("LoanBeneficiary","detail");				
			} else {
				ra.loadScreen("LoanBeneficiary","base");
			}
		}

	}
	
	public void action(){
		if (isTableEmpty("tblCollHfPrior")) {
			ra.showMessage("wrn299");
			return;
		}
		
// Milka, 25.04.2007 - nije moguce mijenjati neaktivnu hipoteku
		TableData tdSel = (TableData) ra.getAttribute("CollHfPriorLDB", "tblCollHfPrior");
		Vector vidljiviVector = (Vector) tdSel.getSelectedRowData();
		
		String mortgage_prority = (String) vidljiviVector.elementAt(0);		
		 if (mortgage_prority.equalsIgnoreCase("NA")) {
		 	ra.showMessage("wrnclt133");
		 	return;
		 }

		if( ra.getScreenContext().equalsIgnoreCase("base_re") ){
		    ra.loadScreen("CollHfPriorDialog","scr_updatere");
		}
		if( ra.getScreenContext().equalsIgnoreCase("base_ca") ){      
			ra.loadScreen("CollHfPriorDialog","scr_updatere");        
		}//base_ca		
	
	}//action  

    public void exit() {
//      vracam se iz unosa ili promjene   
//      napuniti polja za iznos raspolozive i ponderirane
//      ako sam u DS-u - ne radi se nista         
        BigDecimal col_cat_id = null;
        if (!(ra.isLDBExists("ColWorkListLDB"))) {   
            
        } else {        
            col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
            boolean set_eligibility = false;
            String nd_elig = (String) ra.getAttribute("CollHfPriorLDB","Kol_ND");
            
            if (nd_elig == null || nd_elig.trim().equals(""))
                set_eligibility = false;
            else
                set_eligibility = true;
              
    //vracam se iz unosa ili promjene   
    
    //napuniti polja za iznos raspolozive i ponderirane
            if(ra.getScreenContext().equalsIgnoreCase("base_re") || ra.getScreenContext().equalsIgnoreCase("base_ca") ){
                if (ra.isLDBExists("CollHeadLDB")) {
                    ra.setAttribute("CollHeadLDB","Coll_txtRecLop",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtRecLop"));
                    
                    ra.setAttribute("CollHeadLDB","Coll_txtAcouValue",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtWeighValue"));
                    ra.setAttribute("CollHeadLDB","Coll_txtAcouDate",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtWeighDate"));  
                    ra.setAttribute("CollHeadLDB","Coll_txtAvailValue",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtAvailValue"));
                    ra.setAttribute("CollHeadLDB","Coll_txtAvailDate",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtAvailDate"));  
                    ra.setAttribute("CollHeadLDB","Coll_txtSumPartVal",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtSumPartVal"));
                    ra.setAttribute("CollHeadLDB","Coll_txtSumPartDat",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtSumPartDat"));
                }   
                
    // za nekretnine - punim sve           
                if (col_cat_id.compareTo(new BigDecimal("618223")) == 0) {
                    if (set_eligibility) {
                        ra.setAttribute("RealEstateDialogLDB", "RealEstate_txtEligibility",ra.getAttribute("CollHfPriorLDB","Kol_B2"));
                        ra.setAttribute("RealEstateDialogLDB", "RealEstate_txtEligDesc",ra.getAttribute("CollHfPriorLDB","Kol_B2_dsc"));
                        ra.setAttribute("RealEstateDialogLDB", "Coll_txtB2IRBEligibility1",ra.getAttribute("CollHfPriorLDB","Kol_B2IRB"));
                        ra.setAttribute("RealEstateDialogLDB", "Coll_txtB2IRBEligDesc1",ra.getAttribute("CollHfPriorLDB","Kol_B2IRB_dsc")); 
                        ra.setAttribute("RealEstateDialogLDB", "Coll_txtB1Eligibility1",ra.getAttribute("CollHfPriorLDB","Kol_HNB"));
                        ra.setAttribute("RealEstateDialogLDB", "Coll_txtB1EligDesc1",ra.getAttribute("CollHfPriorLDB","Kol_HNB_dsc"));
                        ra.setAttribute("RealEstateDialogLDB", "Coll_txtNDEligibility_Re",ra.getAttribute("CollHfPriorLDB","Kol_ND"));
                        ra.setAttribute("RealEstateDialogLDB", "Coll_txtNDEligDesc_Re",ra.getAttribute("CollHfPriorLDB","Kol_ND_dsc"));    
                        
                        if (ra.getAttribute("CollHfPriorLDB","Coll_txtHfsValue") != null)
                            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtHfsValue",ra.getAttribute("CollHfPriorLDB","Coll_txtHfsValue"));
                        
                        if (ra.getAttribute("CollHfPriorLDB","Coll_txtThirdRightInNom") != null)
                            ra.setAttribute("RealEstateDialogLDB","RealEstate_txtThirdRightInNom",ra.getAttribute("CollHfPriorLDB","Coll_txtThirdRightInNom"));                        

                        ra.setAttribute("RealEstateDialogLDB", "RealEstate_txtHfsValueLastOne",ra.getAttribute("CollHfPriorLDB","Kol_txtLastRBAMortgageAmount"));
                        ra.setAttribute("RealEstateDialogLDB", "RealEstate_txtHfsDateLastOne",ra.getAttribute("CollHfPriorLDB","Kol_txtLastRBAMortgageDate"));
                    }
       
                } else if (col_cat_id.compareTo(new BigDecimal("613223")) == 0 || col_cat_id.compareTo(new BigDecimal("619223")) == 0 ||
                            col_cat_id.compareTo(new BigDecimal("622223")) == 0 || col_cat_id.compareTo(new BigDecimal("627223")) == 0 ||
                            col_cat_id.compareTo(new BigDecimal("629223")) == 0 ) {    
    // za VRP  - punim samo ND              
                    if (set_eligibility) {   
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligibility",ra.getAttribute("CollHfPriorLDB","Kol_ND"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligDesc",ra.getAttribute("CollHfPriorLDB","Kol_ND_dsc"));                    
                    }
                } else {
    // ostali-punim sve        
                    if (set_eligibility) {   
                        ra.setAttribute("CollHeadLDB", "Coll_txtEligibility",ra.getAttribute("CollHfPriorLDB","Kol_B2"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtEligDesc",ra.getAttribute("CollHfPriorLDB","Kol_B2_dsc"));
                        
                        ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligibility",ra.getAttribute("CollHfPriorLDB","Kol_B2IRB"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligDesc",ra.getAttribute("CollHfPriorLDB","Kol_B2IRB_dsc")); 
                        
                        ra.setAttribute("CollHeadLDB", "Coll_txtB1Eligibility",ra.getAttribute("CollHfPriorLDB","Kol_HNB"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtB1EligDesc",ra.getAttribute("CollHfPriorLDB","Kol_HNB_dsc"));
                        
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligibility",ra.getAttribute("CollHfPriorLDB","Kol_ND"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligDesc",ra.getAttribute("CollHfPriorLDB","Kol_ND_dsc"));                   
                    }                
                }
      
               
            } else if (ra.getScreenContext().equalsIgnoreCase("detail_re") || ra.getScreenContext().equalsIgnoreCase("detail_agr") ||
                    ra.getScreenContext().equalsIgnoreCase("detail_ca")) {
    //vracem se iz prikaza detalja          
    //          System.out.println("TU SAM, NAPUSTAM HIPOTEKU  "+ra.getScreenID());  
                
                if (ra.getAttribute("CollHfPriorLDB","Coll_txtHfsValue") != null)
                    ra.setAttribute("CollHeadLDB","Coll_txtHfsValue",ra.getAttribute("CollHfPriorLDB","Coll_txtHfsValue"));
                
                if (ra.getAttribute("CollHfPriorLDB","Coll_txtThirdRightInNom") != null)
                    ra.setAttribute("CollHeadLDB","Coll_txtThirdRightInNom",ra.getAttribute("CollHfPriorLDB","Coll_txtThirdRightInNom"));
               
            }
        }
        ra.exitScreen();
        return;
     }           
	 
	public boolean isTableEmpty(String tableName) {
		hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }  
	
	   public void MortageHistDetails_SE() {
//	        System.out.println("Usla u MsgScreenAS01_SE())!");
//	        System.out.println("PRIJE TRANSAKCIJE!"); 
	        try {
	            ra.executeTransaction();
	        }
	        catch (VestigoTMException vtme) {
	            if (vtme.getMessageID() != null && vtme.getMessageID().equals("fw_wrn_009")) {
	                ra.showMessage("wrnclt185");
	                ra.exitScreen();
	            }
	            else if (vtme.getMessageID() != null ) ra.showMessage(vtme.getMessageID());
	        }  
	   }
}
