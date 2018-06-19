package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.lookup.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;

/**
 * 
 * @author hrakis 
 * Handler klasa za ekran Ponderi - Upit po uzorku
 *
 */
public class CollPonderQBE extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollPonderQBE.java,v 1.1 2009/09/25 11:33:01 hrakis Exp $";
    
    private final String LDBName = "CollPonderLDB";
    
    
    public CollPonderQBE(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollPonderQBE_SE()
    {
        ra.setAttribute(LDBName, "CollPonderQBE_txtPonderType", "MVP");
    }
    
    public void search()
    {
        ra.performQueryByExample("tblCollPonder");
        ra.exitScreen();
    }
      
    public boolean CollPonderQBE_CollCategory_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollCategory();
            return true;
        }

        ra.setAttribute(LDBName, "COL_CAT_ID", null);
        ra.setAttribute(LDBName, "CollPonderQBE_txtCollCategoryName", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
        lookUpRequest.addMapping(LDBName, "COL_CAT_ID", "col_cat_id");
        lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollCategoryCode", "code");
        lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollCategoryName", "name");
        if(!callLookup(lookUpRequest)) return false;

        clearCollType();
        return true;
    }
    
    public boolean CollPonderQBE_CollType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollType();
            return true;
        }

        ra.setAttribute(LDBName, "COL_TYP_ID", null);
        if (elementName.equals("CollPonderQBE_txtCollTypeCode")) ra.setAttribute(LDBName, "CollPonderQBE_txtCollTypeName", "");
        else if(elementName.equals("CollPonderQBE_txtCollTypeName")) ra.setAttribute(LDBName, "CollPonderQBE_txtCollTypeCode", "");

        String coll_category_code = (String)ra.getAttribute(LDBName, "CollPonderQBE_txtCollCategoryCode");
        if (coll_category_code == null || coll_category_code.equals("")) 
        {
            ra.showMessage("wrnclt96");
            return false;
        }

        if (!ra.isLDBExists("CollateralTypeLookUpLDB")) ra.createLDB("CollateralTypeLookUpLDB");
        ra.setAttribute("CollateralTypeLookUpLDB", "CollateralSubCategory", coll_category_code);

        ra.setAttribute(LDBName, "dummyStr", "");
        ra.setAttribute(LDBName, "dummyBd", null);
        ra.setAttribute(LDBName, "dummyDt", null);

        LookUpRequest lookUpRequest = new LookUpRequest("CollateralTypeLookUp");
        lookUpRequest.addMapping(LDBName, "COL_TYP_ID", "coll_type_id");
        lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollTypeCode", "coll_type_code");
        lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollTypeName", "coll_type_name");
        lookUpRequest.addMapping(LDBName, "dummyStr", "coll_period_rev");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_mvp_ponder");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_mvp_ponder_mn");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_mvp_ponder_mx");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_hnb_ponder");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_hnb_ponder_mn");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_hnb_ponder_mx");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_rzb_ponder");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_rzb_ponder_mn");
        lookUpRequest.addMapping(LDBName, "dummyBd", "coll_rzb_ponder_mx");
        lookUpRequest.addMapping(LDBName, "dummyStr","coll_category");
        lookUpRequest.addMapping(LDBName, "dummyStr", "coll_hypo_fidu");
        lookUpRequest.addMapping(LDBName, "dummyStr", "hypo_fidu_name");
        lookUpRequest.addMapping(LDBName, "dummyStr", "coll_anlitika");
        lookUpRequest.addMapping(LDBName, "dummyStr", "coll_accounting");
        lookUpRequest.addMapping(LDBName, "dummyStr", "accounting_name");
        lookUpRequest.addMapping(LDBName, "dummyDt", "coll_date_from");
        lookUpRequest.addMapping(LDBName, "dummyDt", "coll_date_until");
        if(!callLookup(lookUpRequest)) return false;
        
        clearCollSubtype();
        return true;
    }
    
    public boolean CollPonderQBE_CollSubtype_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollSubtype();
            return true;
        }

        ra.setAttribute(LDBName, "COL_SUB_ID", null);
        if (elementName.equals("CollPonderQBE_txtCollSubtypeCode")) ra.setAttribute(LDBName, "CollPonderQBE_txtCollSubtypeName", "");
        else if (elementName.equals("CollPonderQBE_txtCollSubtypeName")) ra.setAttribute(LDBName, "CollPonderQBE_txtCollSubtypeCode", "");
        
        ra.setAttribute(LDBName, "dummyStr", "");
        ra.setAttribute(LDBName, "dummyBd", null);
        ra.setAttribute(LDBName, "dummyDt", null);
        
        BigDecimal col_cat_id = (BigDecimal)ra.getAttribute(LDBName, "COL_CAT_ID");
        BigDecimal col_typ_id = (BigDecimal)ra.getAttribute(LDBName, "COL_TYP_ID");
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
            lookUpRequest.addMapping(LDBName, "COL_SUB_ID", "real_es_type_id");
            lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollSubtypeCode", "real_es_type_code");
            lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollSubtypeName", "real_es_type_desc");
            lookUpRequest.addMapping(LDBName, "dummyStr", "coll_period_rev");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_mvp_ponder");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_mvp_ponder_mn");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_mvp_ponder_mx");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_hnb_ponder");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_hnb_ponder_mn");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_hnb_ponder_mx");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_rzb_ponder");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_rzb_ponder_mn");
            lookUpRequest.addMapping(LDBName, "dummyBd", "coll_rzb_ponder_mx");
            lookUpRequest.addMapping(LDBName, "dummyDt", "real_es_date_from");
            lookUpRequest.addMapping(LDBName, "dummyDt", "real_es_date_unti");
            return callLookup(lookUpRequest);
        }
        else if (isMovable(col_cat_id))
        {
            if (!ra.isLDBExists("MovTypLDB")) ra.createLDB("MovTypLDB");
            ra.setAttribute("MovTypLDB", "mov_col_typ_id", col_typ_id);      
            
            LookUpRequest lookUpRequest = new LookUpRequest("MovableTypeLookUp");
            lookUpRequest.addMapping(LDBName, "COL_SUB_ID", "mov_typ_id");
            lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollSubtypeCode", "mov_typ_code");
            lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollSubtypeName", "mov_typ_dsc");    
            lookUpRequest.addMapping(LDBName, "dummyStr", "val_per_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_max");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_max");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_max");
            return callLookup(lookUpRequest);
        }
        else if (isVessel(col_cat_id))
        {
            LookUpRequest lookUpRequest = new LookUpRequest("VesselTypeLookUp");
            lookUpRequest.addMapping(LDBName, "COL_SUB_ID", "ves_typ_id");
            lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollSubtypeCode", "ves_code");
            lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollSubtypeName", "ves_dsc");    
            lookUpRequest.addMapping(LDBName, "dummyStr", "val_per_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_max");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_max");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_max");
            return callLookup(lookUpRequest);
        }
        else if (isVehicle(col_cat_id))
        {
            LookUpRequest lookUpRequest = new LookUpRequest("VehTypeLookUp");
            lookUpRequest.addMapping(LDBName, "COL_SUB_ID", "veh_gro_id");
            lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollSubtypeCode", "veh_gro_code");
            lookUpRequest.addMapping(LDBName, "CollPonderQBE_txtCollSubtypeName", "veh_gro_desc");    
            lookUpRequest.addMapping(LDBName, "dummyStr", "val_per_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "mvp_max");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "hnb_max");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_dfl");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_min");
            lookUpRequest.addMapping(LDBName, "dummyBd", "rzb_max");
            return callLookup(lookUpRequest);
        }
        else
        {
            return false;
        }
    }
    
    public boolean CollPonderQBE_AddRequest_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        LookUpRequest request = new LookUpRequest("DNLookUp");       
        request.addMapping(LDBName, "CollPonderQBE_txtAddRequest", "Odabir");
        return callLookup(request);
    }
    
    public boolean CollPonderQBE_Status_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            ra.setAttribute(LDBName, "CollPonderQBE_txtStatus", null);
            return true;
        }
        
        LookUpRequest request = new LookUpRequest("PonderStatusLookUp");
        request.addMapping(LDBName, "CollPonderQBE_txtStatus", "Oznaka");
        request.addMapping(LDBName, "dummyStr", "Naziv");
        return callLookup(request);
    }

    
    private void clearCollCategory()
    {
        ra.setAttribute(LDBName, "COL_CAT_ID", null);
        ra.setAttribute(LDBName, "CollPonderQBE_txtCollCategoryCode", "");
        ra.setAttribute(LDBName, "CollPonderQBE_txtCollCategoryName", "");
        clearCollType();
    }
    
    private void clearCollType()
    {
        ra.setAttribute(LDBName, "COL_TYP_ID", null);
        ra.setAttribute(LDBName, "CollPonderQBE_txtCollTypeCode", "");
        ra.setAttribute(LDBName, "CollPonderQBE_txtCollTypeName", "");
        clearCollSubtype();
    }
    
    private void clearCollSubtype()
    {
        ra.setAttribute(LDBName, "COL_SUB_ID", null);
        ra.setAttribute(LDBName, "CollPonderQBE_txtCollSubtypeCode", "");
        ra.setAttribute(LDBName, "CollPonderQBE_txtCollSubtypeName", "");
        
        BigDecimal col_cat_id = (BigDecimal)ra.getAttribute(LDBName, "COL_CAT_ID");
        BigDecimal col_typ_id = (BigDecimal)ra.getAttribute(LDBName, "COL_TYP_ID");
        String ctx = "fld_protected";
        if(col_cat_id != null && col_typ_id != null && ( isRealEstate(col_cat_id) || isVehicle(col_cat_id) || isVessel(col_cat_id) || isAirplane(col_cat_id, col_typ_id) || isMachine(col_cat_id, col_typ_id) ) )
        {
            ctx = "fld_plain";
        }
        ra.setContext("CollPonderQBE_txtCollSubtypeCode", ctx);
        ra.setContext("CollPonderQBE_txtCollSubtypeName", ctx);
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
    
    private boolean isAirplane(BigDecimal col_cat_id, BigDecimal col_typ_id)
    {
        return isMovable(col_cat_id) && col_typ_id.equals(new BigDecimal("60777"));
    }
    
    private boolean isMachine(BigDecimal col_cat_id, BigDecimal col_typ_id)
    {
        return isMovable(col_cat_id) && col_typ_id.equals(new BigDecimal("61777"));
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