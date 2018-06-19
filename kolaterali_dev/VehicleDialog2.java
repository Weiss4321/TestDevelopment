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
public class VehicleDialog2 extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/VehicleDialog2.java,v 1.3 2006/03/06 14:37:43 hrasia Exp $";
	
	
	public VehicleDialog2(ResourceAccessor ra) {
		super(ra);
	}
	public void VehicleDialog2_SE() {
	
	}
	public boolean VehicleDialog2_AmortAge_FV(){
		String vijekAmortizacije = null;
		int brojMjeseci = 0;
		int brojGodina = 0;
		
		vijekAmortizacije = (String)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtAmortAge");
		
		if(vijekAmortizacije == null){
        	return false;
        }else{
        	vijekAmortizacije.trim();
        	if(vijekAmortizacije.length() != 5){
        		ra.showMessage("wrnclt1");
                return false;
        	}
        	if(!isOnlyDigit(vijekAmortizacije)){
        		ra.showMessage("wrnclt1");
                return false;
        	}
        	brojMjeseci = java.lang.Integer.parseInt(vijekAmortizacije.substring(3));
        	if(brojMjeseci > 11){
        		ra.showMessage("wrnclt1");
                return false;
        	}
        }
		
		
		java.sql.Date datum = null;
 		if(ra.getScreenContext().compareTo("scr_change")== 0){
 			datum = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateRecDoc");
 			if(datum == null){
 				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtComDoc","N");
 			}else{
 				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtComDoc","D");
 			}
 			datum = null;
 		}
 		datum = null;
		if(ra.getScreenContext().compareTo("scr_change")== 0){
			datum = (java.sql.Date) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtDateRecLop");
			if(datum == null){
	 			ra.setAttribute("VehicleDialogLDB", "Vehicle_txtRecLop","N");
	 		}else{
	 			ra.setAttribute("VehicleDialogLDB", "Vehicle_txtRecLop","D");
	 		}
		}
		datum = null;
		
		
		 return true;
	}//VehicleDialog2_AmortAge_FV
	
	
	 public boolean VehicleDialog_DateRecDoc_FV(){
	 	
	 		
	 	
	  	return true;
	 
	 }//VehicleDialog_DateRecDoc_FV
	
	
	
	 public boolean VehicleDialog2_AmortPerCal_FV(String elementName, Object elementValue, Integer lookUpType) {
	 	
	 	java.math.BigDecimal nomiValu = null;
	 	String periodAmortizacije = null;
		String vijekAmortizacije = null;
		java.math.BigDecimal iznosAmortizacije = null;
		java.math.BigDecimal periodAmortizacijeBD = null;
		java.math.BigDecimal numberOne = new java.math.BigDecimal("1.00000000000000000000000000000000");
		int brojMjeseci = 0;
		int brojGodina = 0;
		
		nomiValu = (java.math.BigDecimal) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtNomiValu");
		vijekAmortizacije = (String)ra.getAttribute("VehicleDialogLDB", "Vehicle_txtAmortAge");
		periodAmortizacije = (String) ra.getAttribute("VehicleDialogLDB", "Vehicle_txtAmortPerCal");
		
		
	 	if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("VehicleDialogLDB", "Vehicle_txtAmortPerCal", "");
            return true;
        }

       
        ra.setAttribute("VehicleDialogLDB", "SysCodId", "clt_amort_per_cal");
        ra.setAttribute("VehicleDialogLDB", "dummySt", null);
        ra.setAttribute("VehicleDialogLDB", "AMORT_PER_CAL_ID", null);        

        LookUpRequest request = new LookUpRequest("SysCodeValueLookUp");

        request.addMapping("VehicleDialogLDB", "Vehicle_txtAmortPerCal", "sys_code_value");
        request.addMapping("VehicleDialogLDB", "dummySt", "sys_code_desc");
        request.addMapping("VehicleDialogLDB", "AMORT_PER_CAL_ID", "sys_cod_val_id");
        
       

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
        
       
        
        
        if(vijekAmortizacije == null){
        	return false;
        }else{
        	vijekAmortizacije.trim();
        	if(vijekAmortizacije.length() != 5){
        		ra.showMessage("wrnclt1");
                return false;
        	}
        	if(!isOnlyDigit(vijekAmortizacije)){
        		ra.showMessage("wrnclt1");
                return false;
        	}
        	if(vijekAmortizacije.compareTo("00000") == 0){
				iznosAmortizacije = new java.math.BigDecimal("0.00");
				ra.setAttribute("VehicleDialogLDB", "Vehicle_txtAmortValCal", iznosAmortizacije);
				return true;
			}else{
				brojMjeseci = java.lang.Integer.parseInt(vijekAmortizacije.substring(3));
	        	if(brojMjeseci > 11){
	        		ra.showMessage("wrnclt1");
	                return false;
	        	}
	        	brojGodina = java.lang.Integer.parseInt(vijekAmortizacije.substring(0,3));
				brojMjeseci = brojMjeseci + brojGodina * 12;
				periodAmortizacijeBD = new java.math.BigDecimal(brojMjeseci);
				if(nomiValu == null){
					iznosAmortizacije = new java.math.BigDecimal("0.00");
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtAmortValCal", iznosAmortizacije);
					return true;
				}else{
					iznosAmortizacije = nomiValu.divide(periodAmortizacijeBD,32,java.math.BigDecimal.ROUND_HALF_UP);
					iznosAmortizacije = iznosAmortizacije.divide(numberOne,2,java.math.BigDecimal.ROUND_HALF_UP);
					ra.setAttribute("VehicleDialogLDB", "Vehicle_txtAmortValCal", iznosAmortizacije);
					return true;
				}
			
			}
       }
       
    }//VehicleDialog2_AmortPerCal_FV
	

	
	
	
	 public boolean isOnlyDigit(String argString){
		boolean onlyDigit = true;
		int lenString = argString.length();
		for(int i=0;i<lenString;i++){
			if(!(Character.isDigit(argString.charAt(i)))){
				onlyDigit = false;
				break;
			}
		}
		return onlyDigit;
	}//isOnlyDigit
	 
	
	
}
