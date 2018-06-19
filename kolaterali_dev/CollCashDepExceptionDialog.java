//created 2010.12.06
package hr.vestigo.modules.collateral;

import java.sql.Date;

import hr.vestigo.framework.common.TableData;
import hr.vestigo.framework.controller.handler.*;
import hr.vestigo.framework.controller.tm.VestigoTMException;

/**
 *
 * @author hramkr
 */
public class CollCashDepExceptionDialog extends Handler {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollCashDepExceptionDialog.java,v 1.2 2012/02/01 09:22:14 hramkr Exp $";
    public CollCashDepExceptionDialog(ResourceAccessor arg0)
    {
        super(arg0);
    }
    
    public void CollCashDepExceptionDialog_SE()
    {
        if (!(ra.isLDBExists("CollCashDepExceptionDialogLDB"))) 
            ra.createLDB("CollCashDepExceptionDialogLDB");
     
        if (ra.getScreenContext().equalsIgnoreCase("scr_insert")) {
            ra.setAttribute("CollCashDepExceptionDialogLDB", "CollCashDepExceptionDialog_txtDateFrom", ra.getAttribute("GDB","ProcessingDate"));
            ra.setAttribute("CollCashDepExceptionDialogLDB", "CollCashDepExceptionDialog_txtDateUntil", Date.valueOf("9999-12-31"));
            ra.setAttribute("CollCashDepExceptionDialogLDB", "CollCashDepExceptionDialog_txtStatus", "A");
            ra.setAttribute("CollCashDepExceptionDialogLDB","CollCashDepExceptionDialog_txtUseId",(java.math.BigDecimal) ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("CollCashDepExceptionDialogLDB","CollCashDepExceptionDialog_txtUserLogin",(String) ra.getAttribute("GDB", "Use_Login"));
            ra.setAttribute("CollCashDepExceptionDialogLDB","CollCashDepExceptionDialog_txtUserName",(String) ra.getAttribute("GDB", "Use_UserName"));
            
        }
    } 
    
    public void confirm()           // F4
    {
        Integer retValue = (Integer) ra.showMessage("col_qer004");
        if (retValue!=null && retValue.intValue() == 0) return;    
        
        if(!ra.isRequiredFilled()) return;
        System.out.println("ekran, ctx : "+ra.getScreenID()+"-"+ra.getScreenContext());
        try
        {
            ra.executeTransaction();
            ra.showMessage("infclt2");
            ra.exitScreen();
            ra.invokeAction("refresh");
        }
        catch (VestigoTMException vtme)
        {
            error("CollCashDepExceptionDialog -> confirm(): VestigoTMException", vtme);
            ra.showMessage(vtme.getMessageID());
        }
    }
    /**
     * Metoda kontrolira partiju depozita i dohvaca podatke o vlasniku depozita
     * @return false ako partija ne zadovoljava formalnu kontrolu
     */
    public boolean CollCashDepExceptionDialog_txtCus_Acc_No_FV() {

        String cus_acc_no = (String)ra.getAttribute("CollCashDepExceptionDialogLDB", "CollCashDepExceptionDialog_txtCus_Acc_No");
        if (cus_acc_no != null && !cus_acc_no.equals("")) {
            try {
                ra.executeTransaction();
            } catch (VestigoTMException vtme) {
                error("CollCashDepExceptionDialog_txtCus_Acc_No_FV()", vtme);
                if (vtme.getMessageID() != null)
                    ra.showMessage(vtme.getMessageID());
                return false;
            }
        }         
        return true;
    } 
    
      
    
    /**
     * Metoda kontrolira datum od kojeg je zapis vazeci i postavlja status
     * @return false ako je veci od datuma do
     * Metoda kontrolira datum do kojeg je zapis vazeci i postavlja status
     * @return false ako je manji od datuma do
     */
    public boolean CollCashDepExceptionDialog_txtDateFrom_FV(){
        Date datum_od = (Date) ra.getAttribute("CollCashDepExceptionDialogLDB", "CollCashDepExceptionDialog_txtDateFrom");  
        Date datum_do = (Date) ra.getAttribute("CollCashDepExceptionDialogLDB", "CollCashDepExceptionDialog_txtDateUntil");
        Date current_date = (Date) ra.getAttribute("GDB", "ProcessingDate");
        
        if (datum_od == null || datum_do == null) 
            return true;
 
        if ((datum_do).before(datum_od)) {
            ra.showMessage("wrnclt105");
            return false;
        }
        
        if ((current_date).before(datum_do))
            ra.setAttribute("CollCashDepExceptionDialogLDB","CollCashDepExceptionDialog_txtStatus","A");
        else
            ra.setAttribute("CollCashDepExceptionDialogLDB","CollCashDepExceptionDialog_txtStatus","N");
       
        return true;
    }
} 

