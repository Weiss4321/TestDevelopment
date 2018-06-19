/*
 * Created on 2006.04.24
 */
package hr.vestigo.modules.collateral;
 
 
import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Vector;

/**
 * @author hrarmv
 * 
 */
public class CollOwners extends Handler {
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollOwners.java,v 1.25 2015/05/28 08:26:53 hrazst Exp $";
    public CollOwners(ResourceAccessor ra) {
		super(ra);
	}
     
    
    public void CollOwners_SE(){
        if (!ra.isLDBExists("CollOwnersLDB")) {
	 		ra.createLDB("CollOwnersLDB");
	 	}
          
        
        if(ra.getScreenContext().compareTo("scr_realest") == 0) {
        	
			String tableIdSCV = "real_estate";
			String tableIdSCD = "NEKRETNINA";
			
			String colNum = (String) ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCode");
			java.math.BigDecimal colHeaId =(java.math.BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID");
			
			ra.setAttribute("CollOwnersLDB","CollOwners_txtTableSysCodeValue",tableIdSCV);
			ra.setAttribute("CollOwnersLDB","CollOwners_txtTableSysCodeDesc",tableIdSCD);
			ra.setAttribute("CollOwnersLDB","CollOwners_txtColNum",colNum);
	        ra.setAttribute("CollOwnersLDB","CollOwners_COL_HEA_ID",colHeaId);
	        
	        colNum = null;
	        
	        hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollOwners");
	        if (tdLB == null)
	        	ra.createActionListSession("tblCollOwners", false);
	        tdLB = null;
			ra.refreshActionList("tblCollOwners");
	        
	        
        }//REAL_ESTATE
        
        if(ra.getScreenContext().compareTo("detail_re") == 0) {
        	
			String tableIdSCV = "real_estate";
			String tableIdSCD = "NEKRETNINA";
			
			String colNum = (String) ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCode");
			java.math.BigDecimal colHeaId =(java.math.BigDecimal) ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID");
			
			ra.setAttribute("CollOwnersLDB","CollOwners_txtTableSysCodeValue",tableIdSCV);
			ra.setAttribute("CollOwnersLDB","CollOwners_txtTableSysCodeDesc",tableIdSCD);
			ra.setAttribute("CollOwnersLDB","CollOwners_txtColNum",colNum);
	        ra.setAttribute("CollOwnersLDB","CollOwners_COL_HEA_ID",colHeaId);
	        
	        colNum = null;
	        hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollOwners");
	        if (tdLB == null)
	        	ra.createActionListSession("tblCollOwners", false);
	        tdLB = null;
			ra.refreshActionList("tblCollOwners");
	        
	        
        }//REAL_ESTATE
        
        
        
        if(ra.getScreenContext().compareTo("scr_cashdep") == 0) {
        	
        	String tableIdSCV = "";
  			String tableIdSCD = "";
  			
           	if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI")) {
          		tableIdSCV = "vehicle";
          		tableIdSCD = "VOZILO";        
          	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV")) { 
         		tableIdSCV = "vessel";
          		tableIdSCD = "PLOVILO";       
         	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR")) { 
         		tableIdSCV = "movable";
          		tableIdSCD = "POKRETNINA";       
         	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI")) { 
         		tableIdSCV = "supply";
          		tableIdSCD = "ZALIHA";  
         	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("DION") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("OBVE") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJE") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZAPI") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJP")) {
        		tableIdSCV = "security";
          		tableIdSCD = "VRIJEDNOSNICA";   
            } else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("CESI")) { 
                tableIdSCV = "cesija";
                tableIdSCD = "CESIJA";            		
          	} else {
          		tableIdSCV = "cash_deposit";
          		tableIdSCD = "GOTOVINSKI DEPOZIT";
          	}  			
  			
   			   
  			
  			java.math.BigDecimal tableId = new java.math.BigDecimal("1609188003.0");
  			
  			
			String colNum = (String) ra.getAttribute("CollHeadLDB","Coll_txtCode");
			java.math.BigDecimal colHeaId =(java.math.BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
			
			ra.setAttribute("CollOwnersLDB","CollOwners_txtTableSysCodeValue",tableIdSCV);
			ra.setAttribute("CollOwnersLDB","CollOwners_txtTableSysCodeDesc",tableIdSCD);
			ra.setAttribute("CollOwnersLDB","CollOwners_txtColNum",colNum);
	        ra.setAttribute("CollOwnersLDB","CollOwners_COL_HEA_ID",colHeaId);
	        
	        hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollOwners");
	        if (tdLB == null)
	        	ra.createActionListSession("tblCollOwners", false);
	        tdLB = null;
			ra.refreshActionList("tblCollOwners");
	        
	    }//CASH DEPOSIT
         
        if(ra.getScreenContext().compareTo("detail_ca") == 0) {
        	
  /*      	String tableIdSCV = "cash_deposit";
  			String tableIdSCD = "GOTOVINSKI DEPOZIT";*/
  			
  	       	String tableIdSCV = "";
  			String tableIdSCD = "";
  			
           	if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI")) {
          		tableIdSCV = "vehicle";
          		tableIdSCD = "VOZILO";
         	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV")) { 
         		tableIdSCV = "vessel";
          		tableIdSCD = "PLOVILO";  
         	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR")) { 
         		tableIdSCV = "movable";
          		tableIdSCD = "POKRETNINA";       
         	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI")) { 
         		tableIdSCV = "supply";
          		tableIdSCD = "ZALIHA";  
        	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("DION") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("OBVE") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJE") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZAPI") ||
         			((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJP")) 
        	{
        		tableIdSCV = "security";
          		tableIdSCD = "VRIJEDNOSNICA";   
            } else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("CESI")) { 
                tableIdSCV = "cesija";
                tableIdSCD = "CESIJA";                  		
          	} else {
          		tableIdSCV = "cash_deposit";
          		tableIdSCD = "GOTOVINSKI DEPOZIT";
          	}  			  			
  			
  			
  			
  			java.math.BigDecimal tableId = new java.math.BigDecimal("1609188003.0");
  			
  			
			String colNum = (String) ra.getAttribute("CollHeadLDB","Coll_txtCode");
			java.math.BigDecimal colHeaId =(java.math.BigDecimal) ra.getAttribute("CollHeadLDB","COL_HEA_ID");
			
			ra.setAttribute("CollOwnersLDB","CollOwners_txtTableSysCodeValue",tableIdSCV);
			ra.setAttribute("CollOwnersLDB","CollOwners_txtTableSysCodeDesc",tableIdSCD);
			ra.setAttribute("CollOwnersLDB","CollOwners_txtColNum",colNum);
	        ra.setAttribute("CollOwnersLDB","CollOwners_COL_HEA_ID",colHeaId);
	        hr.vestigo.framework.common.TableData tdLB = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblCollOwners");
	        if (tdLB == null)
	        	ra.createActionListSession("tblCollOwners", false);
	        tdLB = null;
			ra.refreshActionList("tblCollOwners");
	        
	        
	    }//CASH DEPOSIT
        
         
    }//CollOwners_SE  
       
    public void details(){
        if (isTableEmpty("tblCollOwners")) {
            ra.showMessage("wrn299");
            return;
        }
        ra.loadScreen("CollOwnersDialog", "scr_detail");
        
    }
    
    public void refresh() {
        ra.refreshActionList("tblCollOwners");
    }
    
    public void add() {
// za cash depozite moze se upisati samo jedan vlasnik
// 23.10.2009 - dionice, obveznice, udjeli, zapisi, udjeli u poduzecu mogu imati samo 1 vlasnika
    	
		String vrsta = (String) ra.getAttribute("ColWorkListLDB","code");    	
		if (vrsta.equalsIgnoreCase("CASH") || vrsta.equalsIgnoreCase("DION") || vrsta.equalsIgnoreCase("OBVE") || vrsta.equalsIgnoreCase("UDJE") ||
				vrsta.equalsIgnoreCase("UDJP") ||vrsta.equalsIgnoreCase("ZAPI") || vrsta.equalsIgnoreCase("CESI")) {
			if (isTableOneRow("tblCollOwners")) {
				ra.showMessage("wrnclt123");
				return;
			}
		}
    
        ra.loadScreen("CollOwnersDialog", "scr_insert");
    }//add

    
       
    
    public void action() {
        if (isTableEmpty("tblCollOwners")) {
            ra.showMessage("wrn299");
            return;
        }
// ako je vlasnik neaktivan otvoriti ekran u kojem je moguca samo akcija aktivacije
        
        TableData td = (TableData) ra.getAttribute("CollOwnersLDB", "tblCollOwners");       
        Vector row = td.getSelectedRowData();
        String flag = (String) row.elementAt(4);
        if (flag.equalsIgnoreCase("A")){ 
        	ra.loadScreen("CollOwnersDialog", "scr_action");
        } else {
        	ra.loadScreen("CollOwnersDialog", "scr_activate");
        }
    } 

    public void exit(){
// za VRP ne prepisivati prihvatljivosti
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
        BigDecimal col_typ_id = null;
        BigDecimal podtipKolateralaBD_new = null;
        boolean set_eligibility = false;
        String nd_elig = (String) ra.getAttribute("CollOwnersLDB","Kol_ND");
        
        if (nd_elig == null || nd_elig.trim().equals(""))
            set_eligibility = false;
        else
            set_eligibility = true;   
        
        if (ra.getScreenContext().compareTo("scr_detail") == 0) {
            
        } else { 
// nisu detalji
 // puniti prihvatljivosti samo ako nisu prazni..
 // za nekretnine - punim sve           
             if (col_cat_id.compareTo(new BigDecimal("618223")) == 0) {
                 col_typ_id = (BigDecimal) ra.getAttribute("RealEstateDialogLDB", "RealEstate_COL_TYPE_ID");
                 podtipKolateralaBD_new = (BigDecimal)ra.getAttribute("RealEstateDialogLDB", "RealEstate_REAL_EST_TYPE");
                 if (set_eligibility) {
                     ra.setAttribute("RealEstateDialogLDB", "RealEstate_txtEligibility",ra.getAttribute("CollOwnersLDB","Kol_B2"));
                     ra.setAttribute("RealEstateDialogLDB", "RealEstate_txtEligDesc",ra.getAttribute("CollOwnersLDB","Kol_B2_dsc"));
                     ra.setAttribute("RealEstateDialogLDB", "Coll_txtB2IRBEligibility1",ra.getAttribute("CollOwnersLDB","Kol_B2IRB"));
                     ra.setAttribute("RealEstateDialogLDB", "Coll_txtB2IRBEligDesc1",ra.getAttribute("CollOwnersLDB","Kol_B2IRB_dsc")); 
                     ra.setAttribute("RealEstateDialogLDB", "Coll_txtB1Eligibility1",ra.getAttribute("CollOwnersLDB","Kol_HNB"));
                     ra.setAttribute("RealEstateDialogLDB", "Coll_txtB1EligDesc1",ra.getAttribute("CollOwnersLDB","Kol_HNB_dsc"));
                     ra.setAttribute("RealEstateDialogLDB", "Coll_txtNDEligibility_Re",ra.getAttribute("CollOwnersLDB","Kol_ND"));
                     ra.setAttribute("RealEstateDialogLDB", "Coll_txtNDEligDesc_Re",ra.getAttribute("CollOwnersLDB","Kol_ND_dsc"));   
// samo za stambene nekretnine koje nisu stambena zgrada
                     if ((col_typ_id != null && col_typ_id.compareTo(new BigDecimal("8777")) == 0) && 
                         (podtipKolateralaBD_new != null &&  podtipKolateralaBD_new.compareTo(new BigDecimal("1156953223")) != 0))
                         ra.setAttribute("RealEstateDialogLDB", "Kol_txtCRMHnb_REstate",ra.getAttribute("CollOwnersLDB","Kol_CRMHnb"));
                     
                 }
      
             } else if (col_cat_id.compareTo(new BigDecimal("613223")) == 0 || col_cat_id.compareTo(new BigDecimal("619223")) == 0 ||
                         col_cat_id.compareTo(new BigDecimal("622223")) == 0 || col_cat_id.compareTo(new BigDecimal("627223")) == 0 ||
                         col_cat_id.compareTo(new BigDecimal("629223")) == 0 ) {    
 // za VRP  - punim samo ND              
                 if (set_eligibility) {   
                     ra.setAttribute("CollHeadLDB", "Coll_txtNDEligibility",ra.getAttribute("CollOwnersLDB","Kol_ND"));
                     ra.setAttribute("CollHeadLDB", "Coll_txtNDEligDesc",ra.getAttribute("CollOwnersLDB","Kol_ND_dsc"));                    
                 }
             } else {
 // ostali-punim sve         
                 if (set_eligibility) {   
                     ra.setAttribute("CollHeadLDB", "Coll_txtEligibility",ra.getAttribute("CollOwnersLDB","Kol_B2"));
                     ra.setAttribute("CollHeadLDB", "Coll_txtEligDesc",ra.getAttribute("CollOwnersLDB","Kol_B2_dsc"));
                     
                     ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligibility",ra.getAttribute("CollOwnersLDB","Kol_B2IRB"));
                     ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligDesc",ra.getAttribute("CollOwnersLDB","Kol_B2IRB_dsc")); 
                     
                     ra.setAttribute("CollHeadLDB", "Coll_txtB1Eligibility",ra.getAttribute("CollOwnersLDB","Kol_HNB"));
                     ra.setAttribute("CollHeadLDB", "Coll_txtB1EligDesc",ra.getAttribute("CollOwnersLDB","Kol_HNB_dsc"));
                     
                     ra.setAttribute("CollHeadLDB", "Coll_txtNDEligibility",ra.getAttribute("CollOwnersLDB","Kol_ND"));
                     ra.setAttribute("CollHeadLDB", "Coll_txtNDEligDesc",ra.getAttribute("CollOwnersLDB","Kol_ND_dsc"));                   
                 }                
             }            
        }   
           
        ra.exitScreen();
    }
           
   
    
    
    public boolean isTableEmpty(String tableName) {
        TableData td = (TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty

    public boolean isTableOneRow(String tableName) {
    	
		if (isTableEmpty(tableName))
			return false;
		
        TableData tab_input = (TableData) ra.getAttribute(tableName);
        Vector data = tab_input.getData();
       
 
        for (int i = 0; i < tab_input.getUnique().size(); i++) {
        	Vector row_in = (Vector) data.elementAt(i);
        	String flag = "" + row_in.elementAt(3);
        	if (flag.equalsIgnoreCase("A"))
        		return true;
        }
        
        return false;
  
    }//
    
}


