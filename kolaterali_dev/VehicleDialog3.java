/*
 * Created on 2005.12.19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;

/**
 * @author HRASIA
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VehicleDialog3 extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/VehicleDialog3.java,v 1.4 2006/04/26 13:38:41 hrasia Exp $";
	private final java.math.BigDecimal numberOne = new java.math.BigDecimal("1.0000000000000000000000000000000000000000000000000000000000000000");
	private final java.math.BigDecimal numberOneHundred = new java.math.BigDecimal("100.0000000000000000000000000000000000000000000000000000000000000000");
	
	
	public VehicleDialog3(ResourceAccessor ra) {
		super(ra);
	}
	public void VehicleDialog3_SE() {
	
	}
	
	public boolean VehicleDialog3_CollMvpPonder_FV(){                                                          
		if(ra.getScreenContext().compareTo("scr_change")== 0){                                                    
		                                                                                                          
			java.math.BigDecimal mvp = null;                                                                        
			java.math.BigDecimal mvpMin = null;                                                                     
			java.math.BigDecimal mvpMax = null;
			
			java.math.BigDecimal nominalCONV = null;
			java.sql.Date        nominalCONVDate = null;
			
			java.math.BigDecimal accouValu = null;
			
			java.math.BigDecimal availValu = null;
			java.math.BigDecimal thirdRightInNom = null;
			java.math.BigDecimal hfsValue = null;
			
			
			
			
			
			
			
			if (ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollMvpPonder") != null){
				mvp = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollMvpPonder");
				
				
				if (ra.getAttribute("VehicleDialogLDB","RealEstate_txtNomiValu")!= null ){
					nominalCONV = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiValu");
				}
			
				if (ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiDate")!= null ){
					nominalCONVDate = (java.sql.Date) ra.getAttribute("VehicleDialogLDB","Vehicle_txtNomiDate");
				}
				if(ra.getAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom") != null){
					thirdRightInNom = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB","Vehicle_txtThirdRightInNom");
				}
				if(ra.getAttribute("VehicleDialogLDB","Vehicle_txtHfsValue") != null){
					hfsValue = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB","Vehicle_txtHfsValue");
				}
				
				accouValu = nominalCONV.multiply(mvp);
				accouValu = accouValu.divide(numberOneHundred,2,java.math.BigDecimal.ROUND_HALF_UP);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouValue",accouValu);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouDate",nominalCONVDate);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouBValue",accouValu);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtAcouBDate",nominalCONVDate);
				availValu = accouValu.subtract(thirdRightInNom);
				availValu = availValu.subtract(hfsValue);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailValue",availValu);
				ra.setAttribute("VehicleDialogLDB","Vehicle_txtAvailDate",nominalCONVDate);
				
				
			}else{
				ra.showMessage("wrnclt33"); 
				return false;	
			}
			mvpMin = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollMvpPonderMin");
			mvpMax = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollMvpPonderMax");
		  
			if(mvp.compareTo(mvpMin)== -1){
				//Mvp ponder je manji od mininimalno dopustenog	
				ra.showMessage("wrnclt3"); 
				return false;	
			}
			if(mvp.compareTo(mvpMax)== 1){
				//Mvp ponder je veci od maksimalno dopustenog	
				ra.showMessage("wrnclt4"); 
				return false;	
			}
			mvp = null;
			mvpMin = null;
			mvpMax = null;
			nominalCONV = null;
			nominalCONVDate = null;
			
			accouValu = null;
			
			availValu = null;
			thirdRightInNom = null;
			hfsValue = null;                                                                                   
		}                                                                                                         
		return true;                                                                                              
                                                                                                              
	}//VehicleDialog3_CollMvpPonder_FV                                                                       
                                                                                                              
	public boolean VehicleDialog3_CollHnbPonder_FV(){                                                        
		                                                                                                          
		if(ra.getScreenContext().compareTo("scr_change")== 0){                                                    
			java.math.BigDecimal hnb = null;                                                                        
			java.math.BigDecimal hnbMin = null;                                                                     
			java.math.BigDecimal hnbMax = null;                                                                     
		                                                                                                          
		                                                                                                          
			hnb = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollHnbPonder");      
			hnbMin = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollHnbPonderMin");
			hnbMax = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollHnbPonderMax");
		                                                                                                          
			if(hnb.compareTo(hnbMin)== -1){                                                                         
				//Hnb ponder je manji od mininimalno dopustenog	                                                      
				ra.showMessage("wrnclt5");                                                                            
				return false;                                                                                         
			}                                                                                                       
			if(hnb.compareTo(hnbMax)== 1){                                                                          
				//Hnb ponder je veci od maksimalno dopustenog	                                                        
				ra.showMessage("wrnclt6");                                                                            
				return false;                                                                                         
			}                                                                                                       
			hnb = null;                                                                                             
			hnbMin = null;                                                                                          
			hnbMax = null;                                                                                          
		}                                                                                                         
		return true;                                                                                              
                                                                                                              
	}//VehicleDialog3_CollHnbPonder_FV                                                                       
                                                                                                              
	public boolean VehicleDialog3_CollRzbPonder_FV(){                                                        
		if(ra.getScreenContext().compareTo("scr_change")== 0){                                                    
			java.math.BigDecimal rzb = null;                                                                        
			java.math.BigDecimal rzbMin = null;                                                                     
			java.math.BigDecimal rzbMax = null;                                                                     
			rzb = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollRzbPonder");      
			rzbMin = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollRzbPonderMin");
			rzbMax = (java.math.BigDecimal)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtCollRzbPonderMax");
		                                                                                                          
			if(rzb.compareTo(rzbMin)== -1){                                                                         
				//Rzb ponder je manji od mininimalno dopustenog	                                                      
				ra.showMessage("wrnclt7");                                                                            
				return false;                                                                                         
			}                                                                                                       
			if(rzb.compareTo(rzbMax)== 1){                                                                          
				//Rzb ponder je veci od maksimalno dopustenog                                                         
				ra.showMessage("wrnclt8");                                                                            
				return false;                                                                                         
			}                                                                                                       
			rzb = null;                                                                                             
			rzbMin = null;                                                                                          
			rzbMax = null;                                                                                          
		}                                                                                                         
		                                                                                                          
		return true;                                                                                              
                                                                                                              
	}//VehicleDialog3_CollRzbPonder_FV                                                                       
                                                                                                              
	public boolean VehicleDialog3_txtEligibility_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("VehicleDialogLDB", "Vehicle_txtEligibility", "");
            ra.setAttribute("VehicleDialogLDB", "Vehicle_txtEligDesc", "");
            ra.setAttribute("VehicleDialogLDB", "dummyBD", null);
            
            
            return true;
        }

       
        ra.setAttribute("VehicleDialogLDB", "SysCodId", "clt_eligib");
        ra.setAttribute("VehicleDialogLDB", "dummySt", null);
        ra.setAttribute("VehicleDialogLDB", "dummyBD", null);        

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");

        request.addMapping("VehicleDialogLDB", "Vehicle_txtEligibility", "sys_code_value");
        request.addMapping("VehicleDialogLDB", "Vehicle_txtEligDesc", "sys_code_desc");
        request.addMapping("VehicleDialogLDB", "dummyBD", "sys_cod_val_id");

        
        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;

        }
        ra.setAttribute("VehicleDialogLDB", "dummySt", null);
        ra.setAttribute("VehicleDialogLDB", "dummyBD", null);        
        ra.setAttribute("VehicleDialogLDB", "SysCodId", "");
        
        if(ra.getCursorPosition().equals("Vehicle_txtEligibility")){
			ra.setCursorPosition(2);
		}
		if(ra.getCursorPosition().equals("Vehicle_txtEligDesc")){
			ra.setCursorPosition(1);
		}	
        
		 return true;
    }//VehicleDialog3_txtEligibility_FV


}
