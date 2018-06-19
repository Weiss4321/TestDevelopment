//created 2017.02.07
package hr.vestigo.modules.collateral;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.util.CharUtil;
import hr.vestigo.modules.collateral.util.CollateralUtil;
import hr.vestigo.modules.rba.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import javax.sql.RowSet;
/**
 *
 * @author hraziv
 */
public class CollMappingDialog extends Handler  {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollMappingDialog.java,v 1.1 2017/04/14 10:13:41 hraziv Exp $";
    
    private String ldbName = "CollMappingDialogLDB";
    CollateralUtil coll_util= null;
    
    
    public CollMappingDialog(ResourceAccessor ra) {
        super(ra);
        // TODO Auto-generated constructor stub
        coll_util = new CollateralUtil(ra);
    }
    
    public void CollMappingDialog_SE(){
        
        if(!ra.isLDBExists(ldbName))
            ra.createLDB(ldbName);
        
        System.out.println("screen context:  " + ra.getScreenContext());
        
        if(ra.getScreenContext().equals("defaultContext")){
            
            System.out.println("COL_GCM_TYP_ID " + ra.getAttribute("CollMappingListLDB", "COL_GCM_TYP_ID"));
            ra.setAttribute(ldbName, "COL_GCM_TYP_ID", ra.getAttribute("CollMappingListLDB", "COL_GCM_TYP_ID"));
            ra.setAttribute(ldbName, "COL_GCM_TYP_MAP_ID", ra.getAttribute("CollMappingListLDB", "COL_GCM_TYP_MAP_ID"));
            try {
                ra.executeTransaction();
            } catch (Exception e) {
                // TODO: handle exception
            }            
        }
    }
    
    public boolean CollMappingDialog_txtCollateralCategoryCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollCategory();
            return true;
        }

        ra.setAttribute(ldbName, "COL_CAT_ID", null);
        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryName", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
        lookUpRequest.addMapping(ldbName, "COL_CAT_ID", "col_cat_id");
        lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtCollateralCategoryCode", "code");
        lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtCollateralCategoryName", "name");
        if(!callLookup(lookUpRequest)) return false;

        clearCollType();
        return true;
        
//        if (elementValue == null || ((String) elementValue).equals("")) {
//            ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode", "");
//            ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryName", "");
//            ra.setAttribute(ldbName, "col_cat_id", null);
//            return true;
//        }
//        
//        if (ra.getCursorPosition().equals("CollMappingDialog_txtCollateralCategoryName")) {
//            ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode", "");
//        } else if (ra.getCursorPosition().equals("CollMappingDialog_txtCollateralCategoryCode")) {
//            ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryName", "");
//            ra.setCursorPosition(2);
//        }
//
//        String d_name = "";
//        if (ra.getAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryName") != null)
//            d_name = (String) ra.getAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryName");
//        String d_register_no = "";
//        if (ra.getAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode") != null)
//            d_register_no = (String) ra.getAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode");
//        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
//            ra.showMessage("wrn366");
//            return false;
//        }
//
//        //kontola da li je zvjezdica na pravom mjestu ili se baca exception s porukom, samo register_no
//        if (CharUtil.isAsteriskWrong(d_register_no)) {
//            ra.showMessage("wrn367");
//            return false;
//        }
//        
//        ///
//        if (ra.isLDBExists("CollCategoryLookUp")) {
//            ra.setAttribute("CollCategoryLookUp", "col_cat_id", null);
//            ra.setAttribute("CollCategoryLookUp", "code", "");
//            ra.setAttribute("CollCategoryLookUp", "name", "");
//        } else {
//            ra.createLDB("CollCategoryLookUp");
//        }
//        //init za poruku o pogresci
//        //ra.setAttribute("SMEScoringLDB","SME_MSG","");
//        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode"));
//        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryName"));
//
//        LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
//        lookUpRequest.addMapping("CollCategoryLookUp", "col_cat_id", "col_cat_id");
//        lookUpRequest.addMapping("CollCategoryLookUp", "Šifra", "code");
//        lookUpRequest.addMapping("CollCategoryLookUp", "Naziv", "name");
//        
//
//        try {
//            ra.callLookUp(lookUpRequest);
//        } catch (EmptyLookUp elu) {
//            ra.showMessage("err012");
//            return false;
//        } catch (NothingSelected ns) {
//            return false;
//        }
//        //clearSMECustomerData();
//        ra.setAttribute(ldbName, "col_cat_id", ra.getAttribute("CollCategoryLookUp", "col_cat_id"));
//        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode", ra.getAttribute("CollCategoryLookUp", "code"));
//        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryName", ra.getAttribute("CollCategoryLookUp", "name"));        
//        boolean ret = true;
//        return ret;
    
    }
    
    public boolean CollMappingDialog_txtCollateralCategoryName_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollCategory();
            return true;
        }
        
        System.out.println("CollMappingDialog_txtCollateralCategoryName_FV");

        ra.setAttribute(ldbName, "COL_CAT_ID", null);
        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
        lookUpRequest.addMapping(ldbName, "COL_CAT_ID", "col_cat_id");
        lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtCollateralCategoryCode", "code");
        lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtCollateralCategoryName", "name");
        if(!callLookup(lookUpRequest)) return false;

        clearCollType();
        return true;
        
    
    }
    
    /**
     * @param elementName
     * @param elementValue
     * @param lookUpType
     * @return
     */
    public boolean CollMappingDialog_txtMapCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        
        String lookupLDBName="CollGcmTypLookUpLDB";
        String map_code = "";
        String name = "";
        String code="";
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            coll_util.clearFields(ldbName, new String[]{"CollMappingDialog_txtMapCode", "CollMappingDialog_txtCode", "CollMappingDialog_txtName"});
            return true;
        }
        
        if (ra.getCursorPosition().equals("CollMappingDialog_txtMapCode")) {
            coll_util.clearField(ldbName, "CollMappingDialog_txtCode");
            coll_util.clearField(ldbName, "CollMappingDialog_txtName");
            map_code = (String) ra.getAttribute(ldbName, "CollMappingDialog_txtMapCode");            
            ra.setCursorPosition(4);
        } else if (ra.getCursorPosition().equals("CollMappingDialog_txtCode")){
            coll_util.clearField(ldbName, "CollMappingDialog_txtMapCode");
            coll_util.clearField(ldbName, "CollMappingDialog_txtName");
            code = (String) ra.getAttribute(ldbName, "CollMappingDialog_txtCode");
            ra.setCursorPosition(4);
        } else if (ra.getCursorPosition().equals("CollMappingDialog_txtName")){
            coll_util.clearField(ldbName, "CollMappingDialog_txtCode");
            coll_util.clearField(ldbName, "CollMappingDialog_txtMapCode");
            name = (String) ra.getAttribute(ldbName, "CollMappingDialog_txtName");
            ra.setCursorPosition(4);
        }
        
        if (ra.isLDBExists(lookupLDBName)) {
            coll_util.clearFields(lookupLDBName, new String[]{ "col_gcm_typ_id", "map_code", "code", "name", "ord_no", "name_add", "param_value", 
                    "param_indic", "currentDate" });
        } else {                                                                                                                         
            ra.createLDB(lookupLDBName);                                                                                          
        }  
        //uzimam trenutni dan
        Date datum=DateUtils.getCurrentDate();
        
        //samo ova polja su pretraziva pa njih postavljam - datum je obavezan jer se dohvacaju samo one
        //vrsta mapiranja koji su bili aktivni na taj dan (današnji dan)
        ra.setAttribute(lookupLDBName, "currentDate", datum);
        ra.setAttribute(lookupLDBName, "map_code", map_code);
        ra.setAttribute(lookupLDBName, "code", code); 
        ra.setAttribute(lookupLDBName, "name", name);
                                                                                                                                     
        LookUpRequest lookUpRequest = new LookUpRequest("CollGcmTypLookUp"); 
        lookUpRequest.addMapping(lookupLDBName, "col_gcm_typ_id", "col_gcm_typ_id");                                                            
        lookUpRequest.addMapping(lookupLDBName, "map_code", "map_code");                                                  
        lookUpRequest.addMapping(lookupLDBName, "code", "code");                                                                
        lookUpRequest.addMapping(lookupLDBName, "name", "name"); 
        /*lookUpRequest.addMapping(lookupLDBName, "ord_no", "ord_no");                                                                
        lookUpRequest.addMapping(lookupLDBName, "name_add", "name_add");                                            
        lookUpRequest.addMapping(lookupLDBName, "param_value", "param_value");                                                    
        lookUpRequest.addMapping(lookupLDBName, "param_indic", "param_indic");
        lookUpRequest.addMapping(lookupLDBName, "currentDate", "currentDate");*/

        try {                                                                                                                            
            ra.callLookUp(lookUpRequest);   
            ra.setCursorPosition("CollMappingDialog_txtCollateralCategoryCode");
        } catch (EmptyLookUp elu) { 
            coll_util.clearFields(ldbName, new String[]{"CollMappingDialog_txtMapCode" });
            //ra.showMessage("infzstColl04");
            //ra.setCursorPosition("RealEstate_txtEUseIdLogin");
            return false;                                                                                                                  
        } catch (NothingSelected ns) {                                                                                                   
            return false;                                                                                                                  
        }   
        
        ra.setAttribute(ldbName, "CollMappingDialog_txtMapCode", ra.getAttribute(lookupLDBName, "map_code"));
        ra.setAttribute(ldbName, "CollMappingDialog_txtCode", ra.getAttribute(lookupLDBName, "code"));
        ra.setAttribute(ldbName, "CollMappingDialog_txtName", ra.getAttribute(lookupLDBName, "name"));
        ra.setAttribute(ldbName, "COL_GCM_TYP_ID", ra.getAttribute(lookupLDBName, "col_gcm_typ_id"));

        return true;
        
    
    }
    
    
    public boolean CollMappingDialog_txtCollateralTypeCode_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollType();
            return true;
        }
        
        System.out.println("CollMappingDialog_txtCollateralTypeCode_FV");

        ra.setAttribute(ldbName, "COL_TYP_ID", null);
        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralTypeName", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp");
        
        
        //if (elementName.equals("CollPonderDialog_txtCollTypeCode")) ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeName", "");
        //else if(elementName.equals("CollPonderDialog_txtCollTypeName")) ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeCode", "");

        String coll_category_code = (String)ra.getAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode");
        if (coll_category_code == null || coll_category_code.equals("")) 
        {
            ra.showMessage("wrnclt96");
            return false;
        }
        
        if (!ra.isLDBExists("CollateralTypeLookUpLDB")) ra.createLDB("CollateralTypeLookUpLDB");
        ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", coll_category_code);
        
        lookUpRequest.addMapping(ldbName, "COL_TYP_ID", "coll_type_id");
        lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtCollateralTypeCode", "coll_type_code");
        lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtCollateralTypeName", "coll_type_name");
        
        if(!callLookup(lookUpRequest)) return false;

        clearCollSubtype();
        return true;
        
    
    }
    
    public boolean CollMappingDialog_txtCollateralTypeName_FV(String elementName, Object elementValue, Integer lookUpType) {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollType();
            return true;
        }
        
        System.out.println("CollMappingDialog_txtCollateralTypeName_FV");

        ra.setAttribute(ldbName, "COL_TYP_ID", null);
        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralTypeCode", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp");
        
        
        //if (elementName.equals("CollPonderDialog_txtCollTypeCode")) ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeName", "");
        //else if(elementName.equals("CollPonderDialog_txtCollTypeName")) ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeCode", "");

        String coll_category_code = (String)ra.getAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode");
        if (coll_category_code == null || coll_category_code.equals("")) 
        {
            ra.showMessage("wrnclt96");
            return false;
        }
        
        if (!ra.isLDBExists("CollateralTypeLookUpLDB")) ra.createLDB("CollateralTypeLookUpLDB");
        ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", coll_category_code);
        
        lookUpRequest.addMapping(ldbName, "COL_TYP_ID", "coll_type_id");
        lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtCollateralTypeCode", "coll_type_code");
        lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtCollateralTypeName", "coll_type_name");
        
        if(!callLookup(lookUpRequest)) return false;

        clearCollSubtype();
        return true;            
    }
    
    public boolean CollMappingDialog_CollSubtype_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollSubtype();
            return true;
        }

        ra.setAttribute(ldbName, "COL_SUB_ID", null);
        if (elementName.equals("CollMappingDialog_ColSubCode")) ra.setAttribute(ldbName, "CollMappingDialog_ColSubName", "");
        else if (elementName.equals("CollMappingDialog_ColSubName")) ra.setAttribute(ldbName, "CollMappingDialog_ColSubCode", "");
        
        /*ra.setAttribute(ldbName, "dummyStr", "");
        ra.setAttribute(ldbName, "dummyBd", null);
        ra.setAttribute(ldbName, "dummyDt", null);*/
        
        BigDecimal col_cat_id = (BigDecimal)ra.getAttribute(ldbName, "COL_CAT_ID");
        BigDecimal col_typ_id = (BigDecimal)ra.getAttribute(ldbName, "COL_TYP_ID");
        if(col_cat_id == null || col_typ_id == null)
        {
            ra.showMessage("wrnclt131");
            return false;
        }

        if (isRealEstate(col_cat_id))
        {
            if (!ra.isLDBExists("RealEstateTypeLookUpLDB")) ra.createLDB("RealEstateTypeLookUpLDB");                                                      
            ra.setAttribute("RealEstateTypeLookUpLDB", "collateralTypeId", col_typ_id);  
            
            LookUpRequest lookUpRequest = new LookUpRequest("RealEstateTypeLookUp");
            lookUpRequest.addMapping(ldbName, "COL_SUB_ID", "real_es_type_id");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubCode", "real_es_type_code");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubName", "real_es_type_desc");
            /*lookUpRequest.addMapping(ldbName, "dummyStr", "coll_period_rev");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder_mn");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_mvp_ponder_mx");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder_mn");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_hnb_ponder_mx");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder_mn");
            lookUpRequest.addMapping(ldbName, "dummyBd", "coll_rzb_ponder_mx");
            lookUpRequest.addMapping(ldbName, "dummyDt", "real_es_date_from");
            lookUpRequest.addMapping(ldbName, "dummyDt", "real_es_date_unti");*/
            return callLookup(lookUpRequest);
        }
        else if (isMovable(col_cat_id))
        {
            if (!ra.isLDBExists("MovTypLDB")) ra.createLDB("MovTypLDB");
            ra.setAttribute("MovTypLDB", "mov_col_typ_id", col_typ_id);      
            
            LookUpRequest lookUpRequest = new LookUpRequest("MovableTypeLookUp");
            lookUpRequest.addMapping(ldbName, "COL_SUB_ID", "mov_typ_id");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubCode", "mov_typ_code");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubName", "mov_typ_dsc");    
            /*lookUpRequest.addMapping(ldbName, "dummyStr", "val_per_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_max");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_max");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_max");*/
            return callLookup(lookUpRequest);
        }
        else if (isVessel(col_cat_id))
        {
            LookUpRequest lookUpRequest = new LookUpRequest("VesselTypeLookUp");
            lookUpRequest.addMapping(ldbName, "COL_SUB_ID", "ves_typ_id");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubCode", "ves_code");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubName", "ves_dsc");    
            /*lookUpRequest.addMapping(ldbName, "dummyStr", "val_per_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_max");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_max");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_max");*/
            return callLookup(lookUpRequest);
        }
        else if (isVehicle(col_cat_id))
        {
            LookUpRequest lookUpRequest = new LookUpRequest("VehTypeLookUp");
            lookUpRequest.addMapping(ldbName, "COL_SUB_ID", "veh_gro_id");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubCode", "veh_gro_code");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubName", "veh_gro_desc");    
            /*lookUpRequest.addMapping(ldbName, "dummyStr", "val_per_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "mvp_max");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "hnb_max");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_dfl");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_min");
            lookUpRequest.addMapping(ldbName, "dummyBd", "rzb_max");*/
            return callLookup(lookUpRequest);
        }
        else
        {
            return false;
        }
    }
    
    public boolean CollMappingDialog_txtColGro_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        System.out.println("CollMappingDialog_txtColGro_FV");
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            //clearCollSubtype();
            return true;
        }

        System.out.println("CollMappingDialog_txtColGro_FV 1");
        //ra.setAttribute(ldbName, "COL_SUB_ID", null);
        if (elementName.equals("CollMappingDialog_txtColGroCode")) ra.setAttribute(ldbName, "CollMappingDialog_txtColGroName", "");
        else if (elementName.equals("CollMappingDialog_txtColGroName")) ra.setAttribute(ldbName, "CollMappingDialog_txtColGroCode", "");
                
        
        BigDecimal col_cat_id = (BigDecimal)ra.getAttribute(ldbName, "COL_CAT_ID");
        BigDecimal col_typ_id = (BigDecimal)ra.getAttribute(ldbName, "COL_SUB_ID");
        
        System.out.println("CollMappingDialog_txtColGro_FV 3" + col_typ_id);
        
        
        if(col_cat_id == null || col_typ_id == null)
        {
            System.out.println("CollMappingDialog_txtColGro_FV 2");
            ra.showMessage("wrnclt131");
            return false;
        }

        if (isRealEstate(col_cat_id))
        {
            System.out.println("CollMappingDialog_txtColGro_FV 3");
            System.out.println("CollMappingDialog_txtColGro_FV 3" + col_typ_id);
            if (!ra.isLDBExists("RealEstateSubTypeLookUpLDB")) ra.createLDB("RealEstateSubTypeLookUpLDB");                                                      
            ra.setAttribute("RealEstateSubTypeLookUpLDB", "RE_TYPE_ID", col_typ_id);  
            
            LookUpRequest lookUpRequest = new LookUpRequest("RealEstateSubTypeLookUp");
            lookUpRequest.addMapping(ldbName, "COL_GRO_ID", "re_sub_type_id");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtColGroCode", "re_sub_type_code");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_txtColGroName", "re_sub_type_desc");
            
            return callLookup(lookUpRequest);
        }
        /*else if (isMovable(col_cat_id))
        {
            if (!ra.isLDBExists("MovTypLDB")) ra.createLDB("MovTypLDB");
            ra.setAttribute("MovTypLDB", "mov_col_typ_id", col_typ_id);      
            
            LookUpRequest lookUpRequest = new LookUpRequest("MovableTypeLookUp");
            lookUpRequest.addMapping(ldbName, "COL_SUB_ID", "mov_typ_id");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubCode", "mov_typ_code");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubName", "mov_typ_dsc");    
            
            return callLookup(lookUpRequest);
        }
        else if (isVessel(col_cat_id))
        {
            LookUpRequest lookUpRequest = new LookUpRequest("VesselTypeLookUp");
            lookUpRequest.addMapping(ldbName, "COL_SUB_ID", "ves_typ_id");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubCode", "ves_code");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubName", "ves_dsc");    
            
            return callLookup(lookUpRequest);
        }
        else if (isVehicle(col_cat_id))
        {
            LookUpRequest lookUpRequest = new LookUpRequest("VehTypeLookUp");
            lookUpRequest.addMapping(ldbName, "COL_SUB_ID", "veh_gro_id");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubCode", "veh_gro_code");
            lookUpRequest.addMapping(ldbName, "CollMappingDialog_ColSubName", "veh_gro_desc");    
            
            return callLookup(lookUpRequest);
        }
        else
        {
            return false;
        }*/
        return false;
    }
    
    
    
    private void clearCollCategory()
    {
        ra.setAttribute(ldbName, "COL_CAT_ID", null);
        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryCode", "");
        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralCategoryName", "");
        clearCollType();
    }
    
    private void clearCollType()
    {
        ra.setAttribute(ldbName, "COL_TYP_ID", null);
        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralTypeCode", "");
        ra.setAttribute(ldbName, "CollMappingDialog_txtCollateralTypeName", "");
        clearCollSubtype();
    }
    
    private void clearCollSubtype()
    {
        ra.setAttribute(ldbName, "COL_SUB_ID", null);
        ra.setAttribute(ldbName, "CollMappingDialog_ColSubCode", "");
        ra.setAttribute(ldbName, "CollMappingDialog_ColSubName", "");
        
        BigDecimal col_cat_id = (BigDecimal)ra.getAttribute(ldbName, "COL_CAT_ID");
        BigDecimal col_typ_id = (BigDecimal)ra.getAttribute(ldbName, "COL_TYP_ID");
        String ctx = "fld_protected";
        if(col_cat_id != null && col_typ_id != null && ( isRealEstate(col_cat_id) || isVehicle(col_cat_id) || isVessel(col_cat_id) 
                //|| 
                //isAirplane(col_cat_id, col_typ_id) || isMachine(col_cat_id, col_typ_id) 
                ) )
        {
            ctx = "fld_plain";
        }
        ra.setContext("CollMappingDialog_ColSubCode", ctx);
        ra.setContext("CollMappingDialog_ColSubName", ctx);
    }
    
    private boolean isRealEstate(BigDecimal col_cat_id)
    {
        return col_cat_id.equals(new BigDecimal("618223"));
    }
    
    private boolean isVehicle(BigDecimal col_cat_id)
    {
        return col_cat_id.equals(new BigDecimal("624223"));
    }
    
    private boolean isVessel(BigDecimal col_cat_id)
    {
        return col_cat_id.equals(new BigDecimal("620223"));
    }
    
    private boolean isMovable(BigDecimal col_cat_id)
    {
        return col_cat_id.equals(new BigDecimal("621223"));
    }
    
    private boolean callLookup(LookUpRequest lookUpRequest)
    {
        try
        {
            ra.callLookUp(lookUpRequest);
            return true;
        }
        catch (EmptyLookUp elu)
        {
            ra.showMessage("err012"); 
            return false;
        }
        catch (NothingSelected ns)
        {
            return false;
        }
    }
}

