/*
 * Created on 2007.02.10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */ 
package hr.vestigo.modules.collateral;
import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.scramble.Scramble;

import java.math.BigDecimal;
import java.util.Vector;
/**
 * @author hramkr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
 
 
public class KolMortgageDemo extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/KolMortgageDemo.java,v 1.3 2013/01/04 13:03:53 vu00276 Exp $";
	
	public KolMortgageDemo(ResourceAccessor ra) { 
		super(ra);
	} 

                

	public void KolMortgageDemo_SE() {

		if (!(ra.isLDBExists("KolMortgageLDB"))) {
			ra.createLDB("KolMortgageLDB");
		}  
		BigDecimal HfTableIdBd = new BigDecimal("0.0");	
		String HfTableIdSCV = "";
		String HfTableIdSCD = "";
		
     	if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("VOZI")) {
      		HfTableIdBd = new BigDecimal("1602610003.0");
			HfTableIdSCV = "vehicle";
			HfTableIdSCD = "VOZILO";        

      	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("PLOV")) {
      		HfTableIdBd = new BigDecimal("1602611003.0");
   			HfTableIdSCV = "vessel";
			HfTableIdSCD = "PLOVILO";          

      	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("POKR")) {
      		HfTableIdBd = new BigDecimal("1602612003.0");
			HfTableIdSCV = "movable";
			HfTableIdSCD = "POKRETNINA";        


      	} else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZALI")) {
      		HfTableIdBd = new BigDecimal("1602613003.0");
			HfTableIdSCV = "supply";
			HfTableIdSCD = "ZALIHA";        
			
        } else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJE") ||
                   ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("DION") ||
                   ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("OBVE") ||
                   ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("ZAPI") ||
                   ((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("UDJP")) {
            HfTableIdBd = new BigDecimal("1604184003.0");
            HfTableIdSCV = "security";
            HfTableIdSCD = "VRIJEDNOSNICA";  
            
        } else if (((String) ra.getAttribute("ColWorkListLDB", "code")).equalsIgnoreCase("CASH")) {
            HfTableIdBd = new BigDecimal("1609188003.0");
            HfTableIdSCV = "cash_deposit";
            HfTableIdSCD = "GOTOVINSKI DEPOZIT";                  
      	} else {	
      		HfTableIdBd = new BigDecimal("1602609003.0");			
      		HfTableIdSCV = "real_estate";
      		HfTableIdSCD = "NEKRETNINA";

      	}		
		ra.setAttribute("KolMortgageLDB","HF_COL_HEA_ID",ra.getAttribute("ColWorkListLDB","col_hea_id"));
		ra.setAttribute("KolMortgageLDB", "CollHfPrior_HF_TABLE_ID",HfTableIdBd);
		ra.setAttribute("KolMortgageLDB", "CollHfPrior_txtHfTableSysCodeValue",HfTableIdSCV);
		ra.setAttribute("KolMortgageLDB", "CollHfPrior_txtHfTableSysCodeDesc",HfTableIdSCD);
		ra.setAttribute("KolMortgageLDB", "CollHfPrior_txtColNum",ra.getAttribute("ColWorkListLDB","Kolateral_txtColNumQBE"));
		
		ra.createActionListSession("tblKolMortgage");   
	} 
	  
	 public void exit() {  
	 	ra.exitScreen();
		ra.invokeAction("refresh");
	 }	  
	   
	public void details(){
		//F4
		if (isTableEmpty("tblKolMortgage")) {
			ra.showMessage("wrn299");
			return;
		}
		TableData td=null;
		td = (TableData) ra.getAttribute("KolMortgageLDB", "tblKolMortgage");
		
		Vector hidden = (Vector) td.getSelectedRowUnique();

		BigDecimal col_hf_prior_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("KolMortgageLDB", "col_hf_prior_id", col_hf_prior_id);
		
		ra.loadScreen("KolMortgageDialog","scr_detail"); 
	}//details
	
	public void KolMortgageDialog_SE () {
		if (!(ra.isLDBExists("CollHfPriorDialogLDB"))) {
			ra.createLDB("CollHfPriorDialogLDB");
		}   

// dohvat podataka

		ra.setAttribute("CollHfPriorDialogLDB", "COLL_HF_PRIOR_ID", ra.getAttribute("KolMortgageLDB", "col_hf_prior_id"));		
		try {
			ra.executeTransaction();  // CO342.sqlj 
		} catch (VestigoTMException vtme) {
			if (vtme.getMessageID() != null)
				ra.showMessage(vtme.getMessageID());
		}
		
	}
	
	public boolean isTableEmpty(String tableName) {
		TableData td = (TableData) ra.getAttribute(tableName);
		if (td == null)
			return true;

		if (td.getData().size() == 0)
			return true;

		return false;
	}  
	    
	public void deact_mortgage () {
// na F6 - potvrda deaktiviranja hipoteke
//		ra.showMessage("inf_1800");
	  	
		if (isTableEmpty("tblKolMortgage")) {
			ra.showMessage("wrn299");
			return;
		}		

        TableData td=null;
        td = (TableData) ra.getAttribute("KolMortgageLDB", "tblKolMortgage");

        Vector row=(Vector)td.getSelectedRowData();
        String hf_status = (String) row.elementAt(8);
        if (hf_status.equalsIgnoreCase("NA")) {
            ra.showMessage("wrnclt186");
            return;
        }
		
		Integer retValue = (Integer) ra.showMessage("col_qer023");

		if (retValue!=null) {
			if (retValue.intValue() == 0) 
				return;
		}

		Vector hidden = (Vector) td.getSelectedRowUnique();
		BigDecimal col_hf_prior_id = (BigDecimal) hidden.elementAt(0);

		ra.setAttribute("KolMortgageLDB", "col_hf_prior_id", col_hf_prior_id);
		
        ra.loadScreen("KolMortgageDeactCmntDemo"); 	 		
  

 
		
		
	}  
	
	public void print () {  
	 // na F5 - ispis brisovnog oèitovanja
//	       ra.showMessage("inf_1800");
	         
	         if (isTableEmpty("tblKolMortgage")) {
	             ra.showMessage("wrn299");
	             return;
	         }       

	         TableData td=null;
	         td = (TableData) ra.getAttribute("KolMortgageLDB", "tblKolMortgage");

	         Vector row=(Vector)td.getSelectedRowData();
	         String hf_status = (String) row.elementAt(8);
	         if (hf_status.equalsIgnoreCase("NA")) {
	             ra.showMessage("wrnclt186");    
	             return;
	         }
	         

	         Vector hidden = (Vector) td.getSelectedRowUnique();
	         BigDecimal col_hf_prior_id = (BigDecimal) hidden.elementAt(0);
	         debug("col_hf_prior_id "  + col_hf_prior_id); 

	         ra.setAttribute("KolMortgageLDB", "col_hf_prior_id", col_hf_prior_id);
	         
	         if (!(ra.isLDBExists("MortgageDeactPrintLDB"))) {
	             ra.createLDB("MortgageDeactPrintLDB");   
	         } 
	         
	         try{
                 ra.executeTransaction();// MortgageDeactPrintDemo - CO346.sqlj
             }catch(VestigoTMException vtme){
                 ra.showMessage(vtme.getMessageID()); 
             }
             
             ra.makeReport("rpt972");  
	                  
	   

	}
	         
	
	

/*      public void deact_mortgage () {
       // na F6 - potvrda deaktiviranja hipoteke
//             ra.showMessage("inf_1800");
               
               if (isTableEmpty("tblKolMortgage")) {
                   ra.showMessage("wrn299");
                   return;
               }       

               TableData td=null;
               td = (TableData) ra.getAttribute("KolMortgageLDB", "tblKolMortgage");

               Vector row=(Vector)td.getSelectedRowData();
               String hf_status = (String) row.elementAt(8);
               if (hf_status.equalsIgnoreCase("NA")) {
                   ra.showMessage("wrnclt186");
                   return;
               }
               
               Integer retValue = (Integer) ra.showMessage("col_qer023");

               if (retValue!=null) {
                   if (retValue.intValue() == 0) 
                       return;
               }

               Vector hidden = (Vector) td.getSelectedRowUnique();
               BigDecimal col_hf_prior_id = (BigDecimal) hidden.elementAt(0);

               ra.setAttribute("KolMortgageLDB", "col_hf_prior_id", col_hf_prior_id);          
                   
         
               try{
                   ra.executeTransaction();// DeactMortgage - CO343.sqlj
       // dodati poruku da je hipoteka deaktivirana
                   ra.showMessage("infclt3");
               }catch(VestigoTMException vtme){
                   ra.showMessage(vtme.getMessageID());
               }
               
               ra.refreshActionList("tblKolMortgage");
        
               
               
           }   */	  
	  
    public void mrtg_history(){
        //F4
        if (isTableEmpty("tblKolMortgage")) {
            ra.showMessage("wrn299");
            return;
        }
        
        TableData td = (TableData) ra.getAttribute("KolMortgageLDB","tblKolMortgage");
   
        Vector hidden = (Vector) td.getSelectedRowUnique(); //skriveni      
        Vector data = td.getSelectedRowData(); //vidljivi

        String hf_status= (String) data.elementAt(8);
        BigDecimal mrtg_id = (BigDecimal) hidden.elementAt(0);

        if (!(ra.isLDBExists("CollHfPriorLDB"))) {
            ra.createLDB("CollHfPriorLDB");
        }  
        
        ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_COLL_HEAD_ID" , ra.getAttribute("KolMortgageLDB","HF_COL_HEA_ID"));
        ra.setAttribute("CollHfPriorLDB","mrtg_id", mrtg_id);
        
        ra.setAttribute("CollHfPriorLDB", "CollHfPrior_HF_TABLE_ID",ra.getAttribute("KolMortgageLDB", "CollHfPrior_HF_TABLE_ID"));
        ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeValue",ra.getAttribute("KolMortgageLDB", "CollHfPrior_txtHfTableSysCodeValue"));
        ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtHfTableSysCodeDesc",ra.getAttribute("KolMortgageLDB", "CollHfPrior_txtHfTableSysCodeDesc"));
        ra.setAttribute("CollHfPriorLDB", "CollHfPrior_txtColNum",ra.getAttribute("KolMortgageLDB", "CollHfPrior_txtColNum"));
        
        if (hf_status.equals("NA")){
            ra.loadScreen("MortageHistDetails","defaultContext"); 
        }else {
            ra.showMessage("wrn_dist_rep16");
        }
        
    }//details
    
 
    public boolean KolMortgageDeactDemo_txtCmnt_FV(){
        if(ra.getAttribute("KolMortgageDeact_txtCmnt")==null || ((String)ra.getAttribute("KolMortgageDeact_txtCmnt")).trim().equals("")){
            ra.showMessage("err10128");
            return false;
        }
        String cmnt=(new Scramble()).doScramble((String)ra.getAttribute("KolMortgageDeact_txtCmnt"));
        if(cmnt==null || cmnt.trim().equals("")){
            ra.showMessage("err10129");
            return false;
        }
        return true;
    }       
 
    public void stop() {
// akcija deaktivacije i spremanja podataka o istoj

        if(ra.getAttribute("KolMortgageDeact_txtCmnt")==null || ((String)ra.getAttribute("KolMortgageDeact_txtCmnt")).trim().equals("")){
            ra.showMessage("err10128");
            return;
        }
        try{
            ra.executeTransaction();// DeactMortgage - CO343.sqlj
// dodati poruku da je hipoteka deaktivirana
            ra.showMessage("infclt3");
        }catch(VestigoTMException vtme){
            ra.showMessage(vtme.getMessageID());
        }
              
        ra.exitScreen();
        ra.setAttribute("KolMortgageLDB", "KolMortgageDeact_txtCmnt", "");
        ra.refreshActionList("tblKolMortgage");
 
    }
    
    /**
     * 
     */
    public void contact () {
      //akcija dizanja ekrana detalja kontakta  
        ra.loadScreen("ContactCenterDetails","ctxUpdate");     
    }
}