/*
 * Created on 2006.05.18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.modules.collateral.util.CollateralUtil; 
         
/**  
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InsuPolicy extends Handler{
    public static String cvsident = "$Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/InsuPolicy.java,v 1.36 2014/12/01 11:26:08 hrazst Exp $";
	CollateralUtil coll_util= null;	
	
    public InsuPolicy (ResourceAccessor ra){
    	super(ra);
		coll_util = new CollateralUtil(ra);
    }  
          
    public void InsuPolicy_SE(){  
   
        if (!ra.isLDBExists("InsuPolicyLDB")) {
            ra.createLDB("InsuPolicyLDB");
        }            
             
        if(ra.getScreenContext().equals("scr_realest") ||  ra.getScreenContext().equals("scr_detail")){
			String tableIdSCV = "";
			String tableIdSCD = "";  
           	if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI")) {
           		tableIdSCV = "vehicle";
           		tableIdSCD = "VOZILO";
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_txtColNum",(String)ra.getAttribute("CollHeadLDB", "Coll_txtCode"));
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_COL_HEA_ID",(java.math.BigDecimal)ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));
           	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV")) {
           		tableIdSCV = "vessel";
           		tableIdSCD = "PLOVILO";
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_txtColNum",(String)ra.getAttribute("CollHeadLDB", "Coll_txtCode"));
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_COL_HEA_ID",(java.math.BigDecimal)ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));     
           	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR")) {
           		tableIdSCV = "movable";
           		tableIdSCD = "POKRETNINA";
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_txtColNum",(String)ra.getAttribute("CollHeadLDB", "Coll_txtCode"));
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_COL_HEA_ID",(java.math.BigDecimal)ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));          		
           	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI")) {
           		tableIdSCV = "supply";
           		tableIdSCD = "ZALIHA";
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_txtColNum",(String)ra.getAttribute("CollHeadLDB", "Coll_txtCode"));
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_COL_HEA_ID",(java.math.BigDecimal)ra.getAttribute("CollHeadLDB", "COL_HEA_ID"));          		    			
           	} else {
    			tableIdSCV = "real_estate";
    			tableIdSCD = "NEKRETNINA";
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_txtColNum",(String)ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCode"));
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_COL_HEA_ID",(java.math.BigDecimal)ra.getAttribute("RealEstateDialogLDB","RealEstate_COL_HEA_ID"));
           	}
           	if(ra.getScreenContext().equals("scr_realest")){
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_txtTableSysCodeValue",tableIdSCV);
    			ra.setAttribute("InsuPolicyLDB","InsuPolicy_txtTableSysCodeDesc",tableIdSCD);
           	}
			TableData tdIP = (hr.vestigo.framework.common.TableData) ra.getAttribute("tblInsuPolicy");
	        if (tdIP == null) ra.createActionListSession("tblInsuPolicy", false);
			ra.refreshActionList("tblInsuPolicy");
        }   
    }
    
    public void details() {
    	if (isTableEmpty("tblInsuPolicy")) {
    		ra.showMessage("wrn299");
    		return;
    	}
    	String lista = (String) ra.getAttribute("ColWorkListLDB","proc_status_QBE");
    	if (lista.equalsIgnoreCase("3")) 
        	ra.loadScreen("InsuPolicyDialog", "scr_deakt_storno");
    	else 
    		ra.loadScreen("InsuPolicyDialog", "scr_detail");
    }
    
    public void action() {
    	if (isTableEmpty("tblInsuPolicy")) {
    		ra.showMessage("wrn299");
    		return;
    	}
    	ra.loadScreen("InsuPolicyDialog", "scr_action");
    }
    
    public void add() {
    	ra.loadScreen("InsuPolicyDialog", "scr_insert");
    }
    
    public void refresh() {
        ra.refreshActionList("tblInsuPolicy");
    }
    
    public void history() {
        ra.loadScreen("InsPolStatChgHistoryList");
    }
    
    public void exit(){        
        BigDecimal col_cat_id = (BigDecimal) ra.getAttribute("ColWorkListLDB", "col_cat_id");
        boolean set_eligibility = false;
        String nd_elig = (String) ra.getAttribute("InsuPolicyLDB","Kol_ND");
        
        if (nd_elig == null || nd_elig.trim().equals(""))
            set_eligibility = false;
        else
            set_eligibility = true;   
        
        if (!ra.getScreenContext().equals("scr_detail")) {
            // nisu detalji, treba preracunati pondere, ponderirani i raspolozivi iznos  
            if (ra.isLDBExists("CollHeadLDB")) {
                ra.setAttribute("CollHeadLDB","Coll_txtAcouValue",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtWeighValue"));
                ra.setAttribute("CollHeadLDB","Coll_txtAcouDate",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtWeighDate"));  
                ra.setAttribute("CollHeadLDB","Coll_txtAvailValue",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtAvailValue"));
                ra.setAttribute("CollHeadLDB","Coll_txtAvailDate",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtAvailDate"));  
                ra.setAttribute("CollHeadLDB","Coll_txtSumPartVal",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtSumPartVal"));
                ra.setAttribute("CollHeadLDB","Coll_txtSumPartDat",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtSumPartDat"));
                
                ra.setAttribute("CollHeadLDB","Coll_txtCollMvpPonderMin",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonderMin"));  
                ra.setAttribute("CollHeadLDB","Coll_txtCollMvpPonder",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonder"));
                ra.setAttribute("CollHeadLDB","Coll_txtCollMvpPonderMax",ra.getAttribute("RealEstateDialogLDB","RealEstate_txtCollMvpPonderMax"));
            }
            // puniti prihvatljivosti samo ako nisu prazni..
            // za nekretnine - punim sve           
            if (col_cat_id.compareTo(new BigDecimal("618223")) == 0) {
                if (set_eligibility) {
                    ra.setAttribute("RealEstateDialogLDB", "RealEstate_txtEligibility",ra.getAttribute("InsuPolicyLDB","Kol_B2"));
                    ra.setAttribute("RealEstateDialogLDB", "RealEstate_txtEligDesc",ra.getAttribute("InsuPolicyLDB","Kol_B2_dsc"));
                    ra.setAttribute("RealEstateDialogLDB", "Coll_txtB2IRBEligibility1",ra.getAttribute("InsuPolicyLDB","Kol_B2IRB"));
                    ra.setAttribute("RealEstateDialogLDB", "Coll_txtB2IRBEligDesc1",ra.getAttribute("InsuPolicyLDB","Kol_B2IRB_dsc")); 
                    ra.setAttribute("RealEstateDialogLDB", "Coll_txtB1Eligibility1",ra.getAttribute("InsuPolicyLDB","Kol_HNB"));
                    ra.setAttribute("RealEstateDialogLDB", "Coll_txtB1EligDesc1",ra.getAttribute("InsuPolicyLDB","Kol_HNB_dsc"));
                    ra.setAttribute("RealEstateDialogLDB", "Coll_txtNDEligibility_Re",ra.getAttribute("InsuPolicyLDB","Kol_ND"));
                    ra.setAttribute("RealEstateDialogLDB", "Coll_txtNDEligDesc_Re",ra.getAttribute("InsuPolicyLDB","Kol_ND_dsc"));                    
                }
                ra.exitScreen(); 
                try {
                    ra.invokeValidation("RealEstate_txtInspolInd");
                } catch (Exception e) {
                    //ako ne uspije  validacija to znaci da nije na ekranu(RealEstateDialog) koji ima to polje ga se proguta exception
                }
            }else{            
                if (col_cat_id.compareTo(new BigDecimal("613223")) == 0 || col_cat_id.compareTo(new BigDecimal("619223")) == 0 ||
                            col_cat_id.compareTo(new BigDecimal("622223")) == 0 || col_cat_id.compareTo(new BigDecimal("627223")) == 0 ||
                            col_cat_id.compareTo(new BigDecimal("629223")) == 0 ) {    
                    // za VRP  - punim samo ND              
                    if (set_eligibility) {   
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligibility",ra.getAttribute("InsuPolicyLDB","Kol_ND"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligDesc",ra.getAttribute("InsuPolicyLDB","Kol_ND_dsc"));                    
                    }
                } else {
                    // ostali-punim sve        
                    if (set_eligibility) {   
                        ra.setAttribute("CollHeadLDB", "Coll_txtEligibility",ra.getAttribute("InsuPolicyLDB","Kol_B2"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtEligDesc",ra.getAttribute("InsuPolicyLDB","Kol_B2_dsc"));
                        
                        ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligibility",ra.getAttribute("InsuPolicyLDB","Kol_B2IRB"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtB2IRBEligDesc",ra.getAttribute("InsuPolicyLDB","Kol_B2IRB_dsc")); 
                        
                        ra.setAttribute("CollHeadLDB", "Coll_txtB1Eligibility",ra.getAttribute("InsuPolicyLDB","Kol_HNB"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtB1EligDesc",ra.getAttribute("InsuPolicyLDB","Kol_HNB_dsc"));
                        
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligibility",ra.getAttribute("InsuPolicyLDB","Kol_ND"));
                        ra.setAttribute("CollHeadLDB", "Coll_txtNDEligDesc",ra.getAttribute("InsuPolicyLDB","Kol_ND_dsc"));                   
                    }                
                }
                ra.exitScreen();
                try {
                    ra.invokeValidation("Vehi_txtVehKasko");
                } catch (Exception e) {
                    //ako ne uspije  validacija to znaci da nije na ekranu(Dialoga) koji ima to polje ga se proguta exception
                }
            }                       
        }else{
            ra.exitScreen();
        }
    }
    
    public boolean isTableEmpty(String tableName) {
        hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }//isTableEmpty
}
