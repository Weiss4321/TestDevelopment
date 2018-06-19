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
 * Handler klasa za ekran Ponderi - Dodaj
 *
 */
public class CollPonderDialog extends Handler
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollPonderDialog.java,v 1.1 2009/09/25 11:33:01 hrakis Exp $";
    
    private final String LDBName = "CollPonderDialogLDB";
    
    
    public CollPonderDialog(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollPonderDialog_SE()
    {
        ra.createLDB(LDBName);
        
        String context = ra.getScreenContext().trim();
        if(context.equals("scr_add"))
        {
            ra.setAttribute(LDBName, "COL_CAT_ID", null);
            ra.setAttribute(LDBName, "COL_TYP_ID", null);
            ra.setAttribute(LDBName, "COL_SUB_ID", null);
            ra.setAttribute(LDBName, "CollPonderDialog_txtAddRequest", "N");
            ra.setAttribute(LDBName, "CollPonderDialog_txtPonderType", "MVP");
            ra.setAttribute(LDBName, "CollPonderDialog_txtStatus", "A");
            ra.setAttribute(LDBName, "CollPonderDialog_txtDateFrom", new Date(System.currentTimeMillis()));
            ra.setAttribute(LDBName, "CollPonderDialog_txtDateUntil", Date.valueOf("9999-12-31"));
            ra.setAttribute(LDBName, "CollPonderDialog_txtOrgAdd", ra.getAttribute("GDB", "OrgUni_Code"));
            ra.setAttribute(LDBName, "CollPonderDialog_txtUserAdd", ra.getAttribute("GDB", "Use_UserName"));
        }
        else if(context.equals("scr_details"))
        {
            TableData td = (TableData) ra.getAttribute("CollPonderLDB","tblCollPonder");
            Vector hidden = (Vector) td.getSelectedRowUnique();
            Vector data = (Vector) td.getSelectedRowData();
            
            ra.setAttribute(LDBName, "CollPonderDialog_txtCollCategoryCode",(String)hidden.elementAt(0));
            ra.setAttribute(LDBName, "CollPonderDialog_txtCollCategoryName",(String)data.elementAt(0));
            ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeCode",(String)hidden.elementAt(1));
            ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeName",(String)data.elementAt(1));
            ra.setAttribute(LDBName, "CollPonderDialog_txtCollSubtypeCode",(String)hidden.elementAt(2));
            ra.setAttribute(LDBName, "CollPonderDialog_txtCollSubtypeName",(String)data.elementAt(2));
            ra.setAttribute(LDBName, "CollPonderDialog_txtAddRequest",(String)data.elementAt(3));
            ra.setAttribute(LDBName, "CollPonderDialog_txtPonderType",(String)data.elementAt(4));
            ra.setAttribute(LDBName, "CollPonderDialog_txtPonderMin",(BigDecimal)data.elementAt(5));
            ra.setAttribute(LDBName, "CollPonderDialog_txtPonderDefault",(BigDecimal)data.elementAt(6));
            ra.setAttribute(LDBName, "CollPonderDialog_txtPonderMax",(BigDecimal)data.elementAt(7));
            ra.setAttribute(LDBName, "CollPonderDialog_txtStatus",(String)data.elementAt(8));
            ra.setAttribute(LDBName, "CollPonderDialog_txtDateFrom",(Date)data.elementAt(9));
            ra.setAttribute(LDBName, "CollPonderDialog_txtDateUntil",(Date)data.elementAt(10));
            ra.setAttribute(LDBName, "CollPonderDialog_txtOrgAdd",(String)data.elementAt(11));
            ra.setAttribute(LDBName, "CollPonderDialog_txtUserAdd",(String)data.elementAt(12));
            ra.setAttribute(LDBName, "CollPonderDialog_txtTimeAdd",(Timestamp)data.elementAt(13));
            ra.setAttribute(LDBName, "CollPonderDialog_txtOrgChange",(String)hidden.elementAt(3));
            ra.setAttribute(LDBName, "CollPonderDialog_txtUserChange",(String)hidden.elementAt(4));
            ra.setAttribute(LDBName, "CollPonderDialog_txtTimeChange",(Timestamp)hidden.elementAt(5));
        }
    }
      
    public void confirm() throws VestigoTMException
    {
        if(!ra.isRequiredFilled()) return;
        
        try
        {
            ra.executeTransaction();
            ra.showMessage("infclt2");
            ra.exitScreen();
            ra.invokeAction("refresh");
        }
        catch (VestigoTMException vtme)
        {
            error("CollPonderDialog -> confirm(): VestigoTMException", vtme);
            ra.showMessage(vtme.getMessageID());
        }
    }
    
    public boolean CollPonderDialog_CollCategory_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollCategory();
            return true;
        }

        ra.setAttribute(LDBName, "COL_CAT_ID", null);
        ra.setAttribute(LDBName, "CollPonderDialog_txtCollCategoryName", "");

        LookUpRequest lookUpRequest = new LookUpRequest("CollCategoryLookUp");
        lookUpRequest.addMapping(LDBName, "COL_CAT_ID", "col_cat_id");
        lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollCategoryCode", "code");
        lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollCategoryName", "name");
        if(!callLookup(lookUpRequest)) return false;

        clearCollType();
        return true;
    }
    
    public boolean CollPonderDialog_CollType_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollType();
            return true;
        }

        ra.setAttribute(LDBName, "COL_TYP_ID", null);
        if (elementName.equals("CollPonderDialog_txtCollTypeCode")) ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeName", "");
        else if(elementName.equals("CollPonderDialog_txtCollTypeName")) ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeCode", "");

        String coll_category_code = (String)ra.getAttribute(LDBName, "CollPonderDialog_txtCollCategoryCode");
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
        lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollTypeCode", "coll_type_code");
        lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollTypeName", "coll_type_name");
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
    
    public boolean CollPonderDialog_CollSubtype_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        if (elementValue == null || ((String) elementValue).equals(""))
        {
            clearCollSubtype();
            return true;
        }

        ra.setAttribute(LDBName, "COL_SUB_ID", null);
        if (elementName.equals("CollPonderDialog_txtCollSubtypeCode")) ra.setAttribute(LDBName, "CollPonderDialog_txtCollSubtypeName", "");
        else if (elementName.equals("CollPonderDialog_txtCollSubtypeName")) ra.setAttribute(LDBName, "CollPonderDialog_txtCollSubtypeCode", "");
        
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
            lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollSubtypeCode", "real_es_type_code");
            lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollSubtypeName", "real_es_type_desc");
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
            lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollSubtypeCode", "mov_typ_code");
            lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollSubtypeName", "mov_typ_dsc");    
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
            lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollSubtypeCode", "ves_code");
            lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollSubtypeName", "ves_dsc");    
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
            lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollSubtypeCode", "veh_gro_code");
            lookUpRequest.addMapping(LDBName, "CollPonderDialog_txtCollSubtypeName", "veh_gro_desc");    
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
    
    public boolean CollPonderDialog_AddRequest_FV(String elementName, Object elementValue, Integer lookUpType)
    {
        LookUpRequest request = new LookUpRequest("DNLookUp");       
        request.addMapping(LDBName, "CollPonderDialog_txtAddRequest", "Odabir");
        return callLookup(request);
    }

    
    private void clearCollCategory()
    {
        ra.setAttribute(LDBName, "COL_CAT_ID", null);
        ra.setAttribute(LDBName, "CollPonderDialog_txtCollCategoryCode", "");
        ra.setAttribute(LDBName, "CollPonderDialog_txtCollCategoryName", "");
        clearCollType();
    }
    
    private void clearCollType()
    {
        ra.setAttribute(LDBName, "COL_TYP_ID", null);
        ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeCode", "");
        ra.setAttribute(LDBName, "CollPonderDialog_txtCollTypeName", "");
        clearCollSubtype();
    }
    
    private void clearCollSubtype()
    {
        ra.setAttribute(LDBName, "COL_SUB_ID", null);
        ra.setAttribute(LDBName, "CollPonderDialog_txtCollSubtypeCode", "");
        ra.setAttribute(LDBName, "CollPonderDialog_txtCollSubtypeName", "");
        
        BigDecimal col_cat_id = (BigDecimal)ra.getAttribute(LDBName, "COL_CAT_ID");
        BigDecimal col_typ_id = (BigDecimal)ra.getAttribute(LDBName, "COL_TYP_ID");
        String ctx = "fld_protected";
        if(col_cat_id != null && col_typ_id != null && ( isRealEstate(col_cat_id) || isVehicle(col_cat_id) || isVessel(col_cat_id) || isAirplane(col_cat_id, col_typ_id) || isMachine(col_cat_id, col_typ_id) ) )
        {
            ctx = "fld_plain";
        }
        ra.setContext("CollPonderDialog_txtCollSubtypeCode", ctx);
        ra.setContext("CollPonderDialog_txtCollSubtypeName", ctx);
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