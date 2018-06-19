//created 2014.07.07
package hr.vestigo.modules.collateral.util;

import java.math.BigDecimal;

import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;

/**
 *
 * @author hrazst
 */
public class LookUps {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/util/LookUps.java,v 1.5 2014/12/10 14:35:09 hrazst Exp $";
    
    private ResourceAccessor ra = null;
    
    public class CurrencyLookUpReturnValues{
        public BigDecimal curId=null;
        public String codeNum=null;
        public String codeChar=null;
        public String name=null;
    }
    
    public class SystemCodeLookUpReturnValues{
        public BigDecimal sysCodeValueId=null;
        public String sysCodeValue=null;
        public String sysCodeDesc=null;
    }
        
    public LookUps( ResourceAccessor ra ){
        this.ra = ra;
    }
    
    public LookUps( ){
        
    }
    
    /**
     * @param LDB - name of LDB from where values will be taken
     * @param fieldCodeNum - numeric code of currency. It can be null.(txt field name from where to take value)
     * @param fieldCodeChar - character code of currency. It can be null. (txt field name from where to take value)
     * @param fieldName - whole currency name. It can be null. (txt field name from where to take value)
     * @param fieldCurId - cur_id. It can be null. (txt field name from where to take value)
     * @return null if empty or nothing selected else CurrencyLookUpReturnValues object
     */
    public CurrencyLookUpReturnValues CurrencyNewLookUp(String LDB, String fieldCodeNum, String fieldCodeChar, String fieldName, String fieldCurId ){ 
        CurrencyLookUpReturnValues ret = new CurrencyLookUpReturnValues();
        String LDBNAME = "CurrencyNewLookUpLDB";
        
        if( !ra.isLDBExists( LDBNAME ) ) {
            ra.createLDB( LDBNAME );
        } else {
            ra.setAttribute( LDBNAME, "CurCodeNum", null );
            ra.setAttribute( LDBNAME, "CurCodeChar", null );
            ra.setAttribute( LDBNAME, "CurName", null );
            ra.setAttribute( LDBNAME, "cur_id", null );
        }
        if(fieldCodeNum == null && fieldCodeChar==null && fieldName==null) return ret;
          
        if(fieldCurId != null) ra.setAttribute( LDBNAME, "cur_id", ra.getAttribute( LDB, fieldCurId ) );
        if(fieldCodeNum != null) ra.setAttribute( LDBNAME, "CurCodeNum", ra.getAttribute( LDB, fieldCodeNum ) );
        if(fieldCodeChar != null) ra.setAttribute( LDBNAME, "CurCodeChar", ra.getAttribute( LDB, fieldCodeChar ) );
        if(fieldName != null) ra.setAttribute( LDBNAME, "CurName", ra.getAttribute( LDB, fieldName ) );   
        
        LookUpRequest lur=new LookUpRequest("CurrencyNewLookUp");

        lur.addMapping(LDBNAME, "cur_id", "cur_id");
        lur.addMapping(LDBNAME, "CurCodeNum", "code_num");
        lur.addMapping(LDBNAME, "CurCodeChar", "code_char");
        lur.addMapping(LDBNAME, "CurName", "name");        

        try {   
            ra.callLookUp(lur);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return ret;
        } catch (NothingSelected ns) {
            return ret;
        }
        
        ret.curId = (BigDecimal) ra.getAttribute(LDBNAME, "cur_id");
        ret.codeNum = (String) ra.getAttribute(LDBNAME, "CurCodeNum");
        ret.codeChar = (String) ra.getAttribute(LDBNAME, "CurCodeChar");
        ret.name = (String) ra.getAttribute(LDBNAME, "CurName");
        
        return ret;
    }
    
    /**
     * 
     * @param LDB
     * @param TextFieldCode
     * @param TextFieldDesc
     * @param sysCodIdValue -> value of sys_cod_id in table system_code_value
     * @return null if empty or nothing selected else SystemCodeLookUpReturnValues object
     */ 
    public SystemCodeLookUpReturnValues SystemCodeValue( String LDB, String TextFieldCode, String TextFieldDesc, String sysCodIdValue){        
        String LDBNAME = "SystemCodeValueLookUpLDB";
        
        if( !ra.isLDBExists( LDBNAME ) ) {
            ra.createLDB( LDBNAME );
        }else{  
            ra.setAttribute( LDBNAME, "sys_code_value", null );
            ra.setAttribute( LDBNAME, "sys_code_desc", null );
            ra.setAttribute( LDBNAME, "eng_sys_code_desc", null );
            ra.setAttribute( LDBNAME, "sys_cod_id", null );
        }       
        
        ra.setAttribute( LDBNAME, "sys_cod_id", sysCodIdValue );
        if(TextFieldCode != null) ra.setAttribute( LDBNAME, "sys_code_value", ra.getAttribute( LDB, TextFieldCode ) );
        if(TextFieldDesc != null) ra.setAttribute( LDBNAME, "sys_code_desc", ra.getAttribute( LDB, TextFieldDesc )  );
        
        LookUpRequest lur = new LookUpRequest( "SystemCodeValueLookUp" );
        lur.addMapping( LDBNAME, "sys_cod_val_id", "sys_cod_val_id" );
        lur.addMapping( LDBNAME, "sys_code_value", "sys_code_value" );
        lur.addMapping( LDBNAME, "sys_code_desc", "sys_code_desc" );

        try {
            ra.callLookUp(lur);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return null;
        } catch (NothingSelected ns) {
            return null;
        }
        SystemCodeLookUpReturnValues ret = new SystemCodeLookUpReturnValues();        
        ret.sysCodeValue = ra.getAttribute( LDBNAME, "sys_code_value" );
        ret.sysCodeDesc = ra.getAttribute( LDBNAME, "sys_code_desc" );
        ret.sysCodeValueId = ra.getAttribute( LDBNAME, "sys_cod_val_id" );
        return ret;
    }
    
    /**Method create Confirm D/N lookup and maps it to LDB and ElName
     * If ElValue is empty or null sets it to null
     * @param LDB - LDB name to map
     * @param ElName - Element name to map
     * @param ElValue - element value
     * @return true if OK, else false
     */
    public boolean ConfirmDN(String LDB, String ElName, Object ElValue){        
        if (ElValue == null || ((String) ElValue).equals("")) {
            ra.setAttribute(LDB, ElName, null);     
            return true;
        } 
        LookUpRequest request = new LookUpRequest("ConfirmDNLookUp");
        request.addMapping(LDB, ElName, "Vrijednosti");

        try {
            ra.callLookUp(request);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        return true;
    } 
}

