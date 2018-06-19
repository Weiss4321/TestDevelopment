//created 2012.02.02
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;

/**
 * Handler za ekran CoBorrowerList
 * @author hradnp
 */
public class CoBorrowerList extends Handler{
    
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CoBorrowerList.java,v 1.2 2012/05/14 07:31:02 hradnp Exp $";
    /**
     * Handler constructor.
     * @param ra resource accessor passed to class when creating object from it.
     */
    public CoBorrowerList(ResourceAccessor ra) {
        super(ra);
    }
    
    public void  CoBorrowerList_SE(){ 
        System.out.println("Usao sam u screen entry");
        if(!ra.isLDBExists("CoBorrowerListLDB"))
            ra.createLDB("CoBorrowerListLDB");    
        
        // polja sa prethodnog ekrana
        TableData td = (TableData) ra.getAttribute("LBenLDB", "tblLoanBeneficiary");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        Vector row = td.getSelectedRowData();
        
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtLBenRegNo",(String) row.elementAt(5));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtLBenName",(String) row.elementAt(6));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtLBenAccNo",(String) row.elementAt(1));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtLBenRequestNo",(String) row.elementAt(0));
        ra.setAttribute("CoBorrowerListLDB", "CONTRACT_NO",(String) row.elementAt(4));
        ra.setAttribute("CoBorrowerListLDB", "CUS_ACC_ID",(BigDecimal) hidden.elementAt(1));
        
        ra.createActionListSession("tblCoBorrowerList",true);
    }
    
    /**
     * Handling action add
     */
    public void add() {
        java.util.Date date= new java.util.Date();
        Timestamp now = new Timestamp(date.getTime());
        
        if(!ra.isLDBExists("CoBorrowerListLDB"))
            ra.createLDB("CoBorrowerListLDB"); 
        
        //brisanje polja s podacima o sudužniku
        // FBPr200016215 - dodano polje uloga i opis uloge
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtRegNo","");
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtName","");
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtOIB","");
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtRole", "");
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtRoleDesc", "");
        
        //podaci o referentu se pune sa podacima o referentu koji je prijavljen u aplikaciju
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserOpenIdLogin", (BigDecimal) ra.getAttribute("GDB", "use_id"));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserOpenIdName", (String) ra.getAttribute("GDB", "Use_UserName"));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserLockOpenField", now);
        
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserIdLogin", (BigDecimal) ra.getAttribute("GDB", "use_id"));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserIdName", (String) ra.getAttribute("GDB", "Use_UserName"));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserLockField", now);
        
        ra.loadScreen("CoBorrower", "ctx_add");      
    }
    
    /**
     * Handling action details
     */
    public void details(){
        if (isTableEmpty("tblCoBorrowerList")) {
            ra.showMessage("wrn299");
            return;
        } 
        
        TableData td = (TableData) ra.getAttribute("CoBorrowerListLDB", "tblCoBorrowerList");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        Vector row = (Vector)td.getSelectedRowData();
        
        // podaci o sudužniku
        // FBPr200016215 - dodano polje uloga i opis uloge
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtRegNo",(String)row.elementAt(0));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtName",(String)row.elementAt(1));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtOIB",(String)row.elementAt(2));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtRole",(String) hidden.elementAt(2));
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtRoleDesc",(String) row.elementAt(3));
        
        // podaci o referentu koji je otvorio zapis
        BigDecimal userOpenLogin = (BigDecimal) hidden.elementAt(4);
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserOpenIdLogin", userOpenLogin);
        String userOpenName = (String) hidden.elementAt(5);
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserOpenIdName", userOpenName);
        Timestamp userLockOpen = (Timestamp) hidden.elementAt(6);
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserLockOpenField", userLockOpen);
        
        // podaci o referentu koji je zadnji mjenjao zapis
        BigDecimal userLogin = (BigDecimal) hidden.elementAt(7);
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserIdLogin",  userLogin);
        String userName = (String) hidden.elementAt(8);
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserIdName", userName);
        Timestamp userLock = (Timestamp) hidden.elementAt(9);
        ra.setAttribute("CoBorrowerListLDB", "CoBorrower_txtUserLockField", userLock);
        
        ra.loadScreen("CoBorrower", "ctx_details"); 
    }
    
    /**
     * Handling action change
     */
    public void change(){
        if (isTableEmpty("tblCoBorrowerList")) {
            ra.showMessage("wrn299");
            return;
        } 

        TableData td = (TableData) ra.getAttribute("CoBorrowerListLDB", "tblCoBorrowerList");
        Vector hidden = (Vector) td.getSelectedRowUnique();
        Vector row = td.getSelectedRowData();
        
        String status = (String)row.elementAt(4);
        
        Integer retValue = null;
        //Ovisno o tome u kojem je trenutno statusu zapis
        if(status.equalsIgnoreCase("A")){
            HashMap messageValues = new HashMap();
            messageValues.put("action", "deaktivirati");
            
            retValue = (Integer)ra.showMessage("qerdnp1", messageValues);             
        }else if(status.equalsIgnoreCase("N")){
            HashMap messageValues = new HashMap();
            messageValues.put("action", "aktivirati");
            
            retValue = (Integer)ra.showMessage("qerdnp1", messageValues);  
        }

        // Ako je odgovor potvrdan, pozovi transakciju
        if ((retValue!=null)&&(retValue.intValue() == 1)) {
            ra.setAttribute("CoBorrowerListLDB", "LOA_COB_ID", hidden.elementAt(0));
            ra.setAttribute("CoBorrowerListLDB", "DATE_UNTIL", hidden.elementAt(3));
            try {
                ra.executeTransaction();
                ra.showMessage("infclt3");
            } catch (VestigoTMException vtme) {
                error("CoBorrowerList -> change(): VestigoTMException", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
            }
            
            ra.refreshActionList("tblCoBorrowerList");
        }
    }
    
    /**
     * Metoda provjerava da li tablica prazna
     * @param tableName
     * @return true ako je tablica prazna, false inaèe
     */
    public boolean isTableEmpty(String tableName) {
        hr.vestigo.framework.common.TableData td = (hr.vestigo.framework.common.TableData) ra.getAttribute(tableName);
        if (td == null)
            return true;

        if (td.getData().size() == 0)
            return true;

        return false;
    }
}

