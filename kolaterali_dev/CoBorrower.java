//created 2012.02.02
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;

/**
 * Handler za ekran CoBorrower
 * @author hradnp
 */
public class CoBorrower extends Handler{

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CoBorrower.java,v 1.3 2013/05/22 06:52:31 hrakis Exp $";
    
    /**
     * Handler constructor.
     * @param ra resource accessor passed to class when creating object from it.
     */
    public CoBorrower(ResourceAccessor ra) {
        super(ra);
    }
    
    public void  CoBorrower_SE(){ 
        if(!ra.isLDBExists("CoBorrowerListLDB")){
            ra.createLDB("CoBorrowerListLDB");
        }
        if (!ra.isLDBExists("Collateral_blUserFieldLDB")) {
            ra.createLDB("Collateral_blUserFieldLDB");
        }
    }  
    
    /** Handling action save.*/
    public void save(){

        if (!(ra.isRequiredFilled())) {
            return;
        }

        try {
            ra.executeTransaction();
            ra.showMessage("infclt2");
            ra.exitScreen();
        } catch (VestigoTMException vtme) {
            error("CoBorrower -> insert(): VestigoTMException", vtme);
            if (vtme.getMessageID() != null)
                ra.showMessage(vtme.getMessageID());
        }
        ra.refreshActionList("tblCoBorrowerList");
    }  
    
    /**
     * Validation function of text field CoBorrower_txtRegNo
     * @param elementName
     * @param elementValue
     * @param lookUpType
     * @return If validation passes, returns true.
     */
    public boolean CoBorrower_txtRegNo_FV(String elementName, Object elementValue, Integer LookUp){
       
        String ldbName = "CoBorrowerListLDB";
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "CoBorrower_txtName","");
            ra.setAttribute(ldbName, "CoBorrower_txtOIB","");
            return true;
        }
        
        if( !ra.isLDBExists( "CustomerWithTaxNumberLookUpLDB" ) ) {
            ra.createLDB( "CustomerWithTaxNumberLookUpLDB" );
        }  

        String register_no = "";
        if (ra.getAttribute(ldbName, "CoBorrower_txtRegNo") != null){
            register_no = (String) ra.getAttribute(ldbName, "CoBorrower_txtRegNo");
        }
        
        // potrebno je unjeti bar 3 znaka uz zvjezdicu
        if ((register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }
        
        // da li je zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(register_no)) {
            ra.showMessage("wrn367");
            return false;
        }

        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "registerNo", register_no );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "code", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "name", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "taxNumber", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "cusTypId", "" );
        
        LookUpRequest lookUpRequest = new LookUpRequest("CustomerWithTaxNumberLookUp");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "cusId", "cusId");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "registerNo", "registerNo");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "taxNumber", "taxNumber");
        
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        } 
        
        // postavljanje vrijednosti dohvaæenih lookupom
        ra.setAttribute(ldbName, "CoBorrower_txtRegNo", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "registerNo"));
        ra.setAttribute(ldbName, "CoBorrower_txtName", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "name"));
        ra.setAttribute(ldbName, "CoBorrower_txtOIB", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "taxNumber"));
        ra.setAttribute(ldbName, "CUS_ID", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "cusId"));  
        
        return true;
    }
    
    /**
     * Validation function of text field CoBorrower_txtName
     * @param elementName
     * @param elementValue
     * @param lookUpType
     * @return If validation passes, returns true.
     */
    public boolean CoBorrower_txtName_FV(String elementName, Object elementValue, Integer LookUp){

        String ldbName = "CoBorrowerListLDB";
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtRegNo","");
            ra.setAttribute(ldbName, "CoBorrower_txtOIB","");
            return true;
        }
        
        if( !ra.isLDBExists( "CustomerWithTaxNumberLookUpLDB" ) ) {
            ra.createLDB( "CustomerWithTaxNumberLookUpLDB" );
        }  

        String name = "";
        if (ra.getAttribute(ldbName, "CoBorrower_txtName") != null){
            name = (String) ra.getAttribute(ldbName, "CoBorrower_txtName");
        }
        
        // potrebno je unjeti bar 3 znaka uz zvjezdicu
        if ((name.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }
        
        // da li je zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(name)) {
            ra.showMessage("wrn367");
            return false;
        }

        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "name", name );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "code", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "registerNo", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "taxNumber", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "cusTypId", "" );
        
        LookUpRequest lookUpRequest = new LookUpRequest("CustomerWithTaxNumberLookUp");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "cusId", "cusId");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "registerNo", "registerNo");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "taxNumber", "taxNumber");
        
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        } 
        
        // postavljanje vrijednosti dohvaæenih lookupom
        ra.setAttribute(ldbName, "CoBorrower_txtRegNo", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "registerNo"));
        ra.setAttribute(ldbName, "CoBorrower_txtName", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "name"));
        ra.setAttribute(ldbName, "CoBorrower_txtOIB", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "taxNumber"));
        ra.setAttribute(ldbName, "CUS_ID", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "cusId"));  
            
        return true;
    }
    
    /**
     * Validation function of text field CoBorrower_txtOIB
     * @param elementName
     * @param elementValue
     * @param lookUpType
     * @return If validation passes, returns true.
     */
    public boolean CoBorrower_txtOIB_FV(String elementName, Object elementValue, Integer LookUp){
        
        String ldbName = "CoBorrowerListLDB";
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtRegNo","");
            ra.setAttribute(ldbName, "CoBorrower_txtName","");
            return true;
        }
        
        if( !ra.isLDBExists( "CustomerWithTaxNumberLookUpLDB" ) ) {
            ra.createLDB( "CustomerWithTaxNumberLookUpLDB" );
        }  

        String taxNumber = "";
        if (ra.getAttribute(ldbName, "CoBorrower_txtOIB") != null){
            taxNumber = (String) ra.getAttribute(ldbName, "CoBorrower_txtOIB");
        }
        
        // potrebno je unjeti bar 3 znaka uz zvjezdicu
        if ((taxNumber.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }
        
        // da li je zvjezdica na pravom mjestu kod register_no
        if (CharUtil.isAsteriskWrong(taxNumber)) {
            ra.showMessage("wrn367");
            return false;
        }

        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "taxNumber", taxNumber );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "code", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "name", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "registerNo", "" );
        ra.setAttribute( "CustomerWithTaxNumberLookUpLDB", "cusTypId", "" );
        
        LookUpRequest lookUpRequest = new LookUpRequest("CustomerWithTaxNumberLookUp");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "cusId", "cusId");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "registerNo", "registerNo");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerWithTaxNumberLookUpLDB", "taxNumber", "taxNumber");
        
        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        } 
        
        // postavljanje vrijednosti dohvaæenih lookupom
        ra.setAttribute(ldbName, "CoBorrower_txtRegNo", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "registerNo"));
        ra.setAttribute(ldbName, "CoBorrower_txtName", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "name"));
        ra.setAttribute(ldbName, "CoBorrower_txtOIB", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "taxNumber"));
        ra.setAttribute(ldbName, "CUS_ID", ra.getAttribute("CustomerWithTaxNumberLookUpLDB", "cusId"));  
            
        return true;
    }
        
    /**
     * Validation function of text field CoBorrower_txtRole
     * @param elementName
     * @param elementValue
     * @param lookUpType
     * @return If validation passes, returns true.
     */
    // FBPr200016215 - dodano polje uloga i opis uloge
    public boolean CoBorrower_txtRole_FV(String elementName, Object elementValue, Integer LookUp){
        
        String ldbName = "CoBorrowerListLDB";
        
        if (elementValue == null || elementValue.equals("")) {
            ra.setAttribute(ldbName, "CoBorrower_txtRoleDesc", "");
            ra.setAttribute(ldbName, "SysCodId","");
            return true;
        }

        if (!ra.isLDBExists("SystemCodeValueLookUpLDB")) {
            ra.createLDB("SystemCodeValueLookUpLDB");
        }

        ra.setAttribute(ldbName, "dummyBd", null);
        ra.setAttribute(ldbName, "CoBorrower_txtRoleDesc", "");
        ra.setAttribute(ldbName, "SysCodId", "loa_cob_role");
        
        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "loa_cob_role");

        LookUpRequest lookUpRequest = new LookUpRequest("SysCodeValueNewLookUp");
        lookUpRequest.addMapping(ldbName, "CoBorrower_txtRole", "sys_code_value");
        lookUpRequest.addMapping(ldbName, "CoBorrower_txtRoleDesc", "sys_code_desc");
        lookUpRequest.addMapping(ldbName, "dummyBd", "sys_cod_val_id");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        return true;
    }
 
    /**
     * Validation function of text field CoBorrower_txtRoleDesc
     * @param elementName
     * @param elementValue
     * @param lookUpType
     * @return If validation passes, returns true.
     */
    // FBPr200016215 - dodano polje uloga i opis uloge
    public boolean CoBorrower_txtRoleDesc_FV(String elementName, Object elementValue, Integer LookUp){
        
        String ldbName = "CoBorrowerListLDB";
        
        if (elementValue == null || elementValue.equals("")) {
            ra.setAttribute(ldbName, "CoBorrower_txtRole", "");
            ra.setAttribute(ldbName, "SysCodId","");
            return true;
        }

        if (!ra.isLDBExists("SystemCodeValueLookUpLDB")) {
            ra.createLDB("SystemCodeValueLookUpLDB");
        }

        ra.setAttribute(ldbName, "dummyBd", null);
        ra.setAttribute(ldbName, "CoBorrower_txtRole", "");
        ra.setAttribute(ldbName, "SysCodId", "loa_cob_role");
        
        if (!ra.isLDBExists("SysCodeValueNewLookUpLDB")) ra.createLDB("SysCodeValueNewLookUpLDB");
        ra.setAttribute("SysCodeValueNewLookUpLDB", "sys_cod_id", "loa_cob_role");

        LookUpRequest lookUpRequest = new LookUpRequest("SysCodeValueNewLookUp");
        lookUpRequest.addMapping(ldbName, "CoBorrower_txtRole", "sys_code_value");
        lookUpRequest.addMapping(ldbName, "CoBorrower_txtRoleDesc", "sys_code_desc");
        lookUpRequest.addMapping(ldbName, "dummyBd", "sys_cod_val_id");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        return true;
    }
}
