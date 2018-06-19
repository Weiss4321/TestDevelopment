//created 2011.10.13
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.util.CharUtil;

/**
 *
 * @author hradnp
 * Handler klasa za ekran Izvješæe alociranih vrijednosti kolaterala
 */
public class CollAlocatedValuesReport extends Handler{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollAlocatedValuesReport.java,v 1.1 2011/10/21 07:07:35 hradnp Exp $";
    
    private final String ldbName = "CollAlocatedValuesReportLDB";
    
    public CollAlocatedValuesReport(ResourceAccessor ra){
        super(ra);
    }
    
    public void CollAlocatedValuesReport_SE(){
        if(!ra.isLDBExists(ldbName)){
            ra.createLDB(ldbName);
        }
    }
    
    /** Metoda koja naruèuje batch obradu. */
    public void orderCSV(){
        try{
            if(!ra.isRequiredFilled()) return;
            String parameters = generateBatchParam();
            if(!ra.isLDBExists("BatchLogDialogLDB")) ra.createLDB("BatchLogDialogLDB");
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("4800981704"));
            ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
            ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
            ra.setAttribute("BatchLogDialogLDB", "fldParamValue", parameters);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
            ra.executeTransaction();
            ra.showMessage("inf075");
        }
        catch (VestigoTMException vtme){
            error("CollFreeReport.toCSVfile() -> VestigoTMException", vtme);
        } 
    }
    
    /**
     * Metoda koja vraæa formirani String sa parametrima koji se predaju batchu.
     * Parametri se predaju u obliku RB;date;eligibility;ponder;registerNo;accNo;
     */
    private String generateBatchParam(){
        StringBuffer buffer = new StringBuffer();        
        buffer.append((String) ra.getAttribute("GDB", "bank_sign")).append(";");
        buffer.append(getDDMMYYYY(((Date)ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtDate")))).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtEligibility")).append(";");
        buffer.append((String)ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtPonder")).append(";");
        if(!isEmpty((String)ra.getAttribute(ldbName, "REGISTER_NO")))
            buffer.append((String)ra.getAttribute(ldbName, "REGISTER_NO")).append(";");
        else buffer.append("X;");
        if(!isEmpty((String)ra.getAttribute(ldbName, "ACC_NO")))
            buffer.append((String)ra.getAttribute(ldbName, "ACC_NO")).append(";");
        else buffer.append("X");
        return buffer.toString();
    }
    
    /** Validacijska metoda za polje datum za koji se radi izvještaj. */
    public boolean CollAlocatedValuesReport_txtDate_FV(String elementName, Object elementValue, Integer lookUpType){
        
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        Date dateOfReport = (Date)ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtDate");
        
        if (dateOfReport == null || current_date == null) 
            return true;

        if (current_date.before(dateOfReport)) {
            ra.showMessage("wrnclt121");
            return false;
        }
        return true;    
    }
    
    /** Validacijska metoda za polje s prihvatljivošæu. */
    public boolean CollAlocatedValuesReport_txtEligibility_FV(String elementName, Object elementValue, Integer lookUpType){

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollAlocatedValuesReport_txtEligibility", "");
            ra.setAttribute(ldbName, "dummySt", "");
            ra.setAttribute(ldbName, "SysCodId","");
            return true;
        }
        /**
         * brise staru vrijednost u txtEligibility ako se upisivala vrijednost u
         * dummyEligibilityDesc i obratno, kako ne bi doslo do pogresnog povezivanja
         * sifre i imena mjesta kod poziva LookUp-a
         */
        if (ra.getCursorPosition().equals("CollAlocatedValuesReport_txtEligibility")) {
            ra.setAttribute(ldbName, "dummySt", "");
        } else if (ra.getCursorPosition().equals("dummySt")) {
            ra.setAttribute(ldbName, "CollAlocatedValuesReport_txtEligibility", "");
        } 

        if (!ra.isLDBExists("SystemCodeValueLookUpLDB")) {
            ra.createLDB("SystemCodeValueLookUpLDB");
        }

        ra.setAttribute(ldbName, "dummyBd", null);
        ra.setAttribute(ldbName, "SysCodId", "eligibility_type");
        LookUpRequest lookUpRequest = new LookUpRequest("SysCodeValueLookUp");
        lookUpRequest.addMapping(ldbName, "CollAlocatedValuesReport_txtEligibility", "sys_code_value");
        lookUpRequest.addMapping(ldbName, "dummySt", "sys_code_desc");       
        lookUpRequest.addMapping(ldbName, "dummyBd", "sys_cod_val_id");

        return callLookUp(lookUpRequest);
    }

    /** Validacijska metoda za polje s ponderom. */
    public boolean CollAlocatedValuesReport_txtPonder_FV(String elementName, Object elementValue, Integer lookUpType){

        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CollAlocatedValuesReport_txtPonder", "");
            ra.setAttribute(ldbName, "dummySt", "");
            ra.setAttribute(ldbName, "SysCodId","");
            return true;
        }
        /**
         * brise staru vrijednost u txtPonder ako se upisivala vrijednost u
         * dummySt i obratno, kako ne bi doslo do pogresnog povezivanja
         * sifre i imena mjesta kod poziva LookUp-a
         */
        if (ra.getCursorPosition().equals("CollAlocatedValuesReport_txtPonder")) {
            ra.setAttribute(ldbName, "dummySt", "");
        } else if (ra.getCursorPosition().equals("dummySt")) {
            ra.setAttribute(ldbName, "CollAlocatedValuesReport_txtPonder", "");
        }  

        if (!ra.isLDBExists("SystemCodeValueLookUpLDB")) {
            ra.createLDB("SystemCodeValueLookUpLDB");
        }

        ra.setAttribute(ldbName, "dummyBd", null);

        ra.setAttribute(ldbName, "SysCodId", "ponder");
        LookUpRequest lookUpRequest = new LookUpRequest("SysCodeValueLookUp");
        lookUpRequest.addMapping(ldbName, "CollAlocatedValuesReport_txtPonder", "sys_code_value");
        lookUpRequest.addMapping(ldbName, "dummySt", "sys_code_desc");
        lookUpRequest.addMapping(ldbName, "dummyBd", "sys_cod_val_id");

        return callLookUp(lookUpRequest);
    }
    
    /** Validacijska metoda za polje s komitentom. */
    public boolean CollAlocatedValuesReport_txtRegisterNo_FV(String elementName, Object elementValue, Integer lookUpType){
 
        if (elementValue == null || ((String)elementValue).equals("")) {
            
            ra.setAttribute(ldbName,"CollAlocatedValuesReport_txtClientName","");
            ra.setAttribute(ldbName,"CollAlocatedValuesReport_txtRegisterNo","");
            ra.setAttribute(ldbName,"CUS_ID",null);
            return true;
        }
        
        if (ra.getCursorPosition().equals("CollAlocatedValuesReport_txtClientName")) {
            ra.setAttribute(ldbName, "CollAlocatedValuesReport_txtClientName", "");
        }else if (ra.getCursorPosition().equals("CollAlocatedValuesReport_txtRegisterNo")) {
            ra.setAttribute(ldbName, "CollAlocatedValuesReport_txtClientName", "");
        }
        
        String d_name = "";
        if (ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtClientName") != null){
            d_name = (String) ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtClientName");
        }
        
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtRegisterNo") != null){
            d_register_no = (String) ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtRegisterNo");
        }
        
        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }
        
        //da li je zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(d_register_no)) {
            ra.showMessage("wrn367");
            return false;
        }
        
        if (ra.isLDBExists("CustomerAllLookUpLDB")) {
            ra.setAttribute("CustomerAllLookUpLDB", "cus_id", null);
            ra.setAttribute("CustomerAllLookUpLDB", "register_no", "");
            ra.setAttribute("CustomerAllLookUpLDB", "code", "");
            ra.setAttribute("CustomerAllLookUpLDB", "name", "");
            ra.setAttribute("CustomerAllLookUpLDB", "add_data_table", "");
            ra.setAttribute("CustomerAllLookUpLDB", "cus_typ_id", null);
            ra.setAttribute("CustomerAllLookUpLDB", "cus_sub_typ_id", null);
            ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", "");
            ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
        } else {
            ra.createLDB("CustomerAllLookUpLDB");
        } 

        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtRegisterNo"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtClientName"));

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
        lookUpRequest.addMapping(ldbName, "CUS_ID", "cus_id");
        lookUpRequest.addMapping(ldbName, "CollAlocatedValuesReport_txtRegisterNo", "register_no");
        lookUpRequest.addMapping(ldbName, "dummySt", "code");
        lookUpRequest.addMapping(ldbName, "CollAlocatedValuesReport_txtClientName", "name");
        lookUpRequest.addMapping(ldbName, "dummySt", "add_data_table");
        lookUpRequest.addMapping(ldbName, "dummyBd", "cus_typ_id");
        lookUpRequest.addMapping(ldbName, "dummyBd", "cus_sub_typ_id");
        lookUpRequest.addMapping(ldbName, "dummySt", "eco_sec");
        lookUpRequest.addMapping(ldbName, "dummyBd", "residency_cou_id");
    
        return callLookUp(lookUpRequest);
    }
    
    /** Validacijska metoda za polje s partijom plasmana. */
    public boolean CollAlocatedValuesReport_txtAccNo_FV(String elementName, Object elementValue, Integer lookUpType){
 
        BigDecimal cusIdKorisnika = (BigDecimal)ra.getAttribute(ldbName, "CUS_ID");
        
        if (cusIdKorisnika == null) {
            ra.showMessage("wrnclt74");
            //Najprije odaberite komitenta
            return false;
        }
        
        if (elementValue == null || ((String) elementValue).equals("")) {                                                                
            ra.setAttribute(ldbName,"CollAlocatedValuesReport_txtAccNo",""); 
            ra.setAttribute(ldbName,"ACC_NO",null); 
            return true;                                                                                                                   
        }                                                                                                                                
                                                                                                                                             
        if (!(ra.isLDBExists("CusaccExposureLookLDB"))) {
            ra.createLDB("CusaccExposureLookLDB");
        }   
             
        ra.setAttribute("CusaccExposureLookLDB", "cus_id", ra.getAttribute(ldbName, "CUS_ID"));
        ra.setAttribute("CusaccExposureLookLDB", "cus_acc_no", ra.getAttribute(ldbName, "CollAlocatedValuesReport_txtAccNo")); 
        
        LookUpRequest lookUpRequest = new LookUpRequest("CusaccExposureLookUp");   
        lookUpRequest.addMapping(ldbName, "dummyBd", "cus_acc_id");         
        lookUpRequest.addMapping(ldbName, "CollAlocatedValuesReport_txtAccNo", "cus_acc_no");  
        lookUpRequest.addMapping(ldbName, "dummySt", "contract_no");
        lookUpRequest.addMapping(ldbName, "dummySt", "cus_acc_status");              
        lookUpRequest.addMapping(ldbName, "dummySt", "cus_acc_orig_st");   
        lookUpRequest.addMapping(ldbName, "dummySt", "frame_cus_acc_no");   
        lookUpRequest.addMapping(ldbName, "dummyBd", "exposure_cur_id");  
        lookUpRequest.addMapping(ldbName, "dummySt", "code_char"); 
        lookUpRequest.addMapping(ldbName, "dummyBd", "exposure_balance");                                                            
        lookUpRequest.addMapping(ldbName, "dummyD", "exposure_date");          
        lookUpRequest.addMapping(ldbName, "dummySt", "request_no");  
        lookUpRequest.addMapping(ldbName, "dummySt", "prod_code");  
        lookUpRequest.addMapping(ldbName, "dummySt", "name");          
        lookUpRequest.addMapping(ldbName, "dummySt", "module_code");                                              
       
       return callLookUp(lookUpRequest);
    }
    
    /** Metoda koja poziva zadani lookup
     * @return da li je poziv uspješno završen
     */
    private boolean callLookUp(LookUpRequest lu){
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
    
    /**
     * Metoda vraca datum zapisan u string u formatu dd.MM.yyyy
     * @param java.sql.Date
     * @return datum zapisan u string u formatu dd.MM.yyyy; ako je null vraca deset blankova
     */
    private String getDDMMYYYY(Date date){
        if(date == null) return "          ";
        String date_s=date.toString();
        return date_s.substring(8, 10)+"."+date_s.substring(5, 7)+"."+date_s.substring(0, 4);
    }

    private Boolean isEmpty(String str){
        if (str == null || str.equals("")) return true; else return false;
    }
}

