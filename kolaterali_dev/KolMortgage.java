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
 
 
public class KolMortgage extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/KolMortgage.java,v 1.10 2017/04/27 15:40:04 hrazst Exp $";
	private int mortage_number=0;
	
	public KolMortgage(ResourceAccessor ra) {
		super(ra);
	}                 

	public void KolMortgage_SE() {

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
	    //na F6 - potvrda deaktiviranja hipoteke
	    //ra.showMessage("inf_1800");	  	
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
		ra.setAttribute("KolMortgageLDB", "col_hf_prior_id", col_hf_prior_id);
        System.out.println("col_hf_prior_id="+col_hf_prior_id);
		//provjeravam da li se hipoteka opce moze gasiti i da li je to zadnja hipoteka 
        try{
            ra.executeTransaction();// DeactMortgage - CO348.sqlj
        }catch(VestigoTMException vtme){
            ra.showMessage(vtme.getMessageID());
            return;
        }
        
        mortage_number=((Integer) ra.getAttribute("KolMortgageLDB", "MORTAGE_NUMBER")).intValue();
        System.out.println("mortage_number"+mortage_number);
        //za kolaterale gotovinski depoziti, pokretnine, udjeli u poduzeæima, vozila, zalihe se ne salje na deaktivaciju verifikacije
        //nego se odmah deaktivira kolateral kad se zadnja hipoteka deaktivira (Change Request 16098) pa se ide na dizanje KolMortgageDeactCmnt
        //ekrana i onda se u transakciji deaktivacije hipoteke deaktivira i kolateral
        //za nekretnine i plovila se ide na deaktivaciju vrerifikacije
        String code=(String) ra.getAttribute("ColWorkListLDB", "code");
        if(mortage_number==0 && (code.equalsIgnoreCase("PLOV") || code.equalsIgnoreCase("NEKR") || code.equalsIgnoreCase("DION") || code.equalsIgnoreCase("GARA") 
                                 || code.equalsIgnoreCase("OBVE") || code.equalsIgnoreCase("UDJE") || code.equalsIgnoreCase("ZAPI"))){
            if ((Integer) ra.showMessage("qwszstColl03")==0) return;   
            ra.loadScreen("KolDeactCmnt","MortageDeactContext");            
        }else{
            if ((Integer) ra.showMessage("col_qer023")==0){
                return;
            }            
            ra.loadScreen("KolMortgageDeactCmnt");
        }
	}  
	
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
    
 
    public boolean KolMortgageDeact_txtCmnt_FV(){
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
            if(mortage_number==0){
                ra.showMessage("infzstColl09");
                //ovaj exit screen gasi ekran hipoteka, a sljedeci gasi ekran hipoteka i tako se vracamo na listu kolaterala                
                ra.exitScreen();
                ra.exitScreen();
                //ra.refreshActionList("tblColWorkList");
                ra.refreshActionList("tblColWorkListCellTable");
            }else{
                ra.showMessage("infclt3");
                ra.exitScreen();
                ra.setAttribute("KolMortgageLDB", "KolMortgageDeact_txtCmnt", "");
                ra.refreshActionList("tblKolMortgage");
            }
        }catch(VestigoTMException vtme){
            ra.showMessage(vtme.getMessageID());
        } 
        
    }
}