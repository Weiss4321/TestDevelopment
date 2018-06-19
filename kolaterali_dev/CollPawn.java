package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;
/**
 * 
 * @author hraaks
 * Izvješæe o ažuriranju podataka o fiducijama na vozilima poziva batch
 * hr.vestigo.modules.collateral.batc.bo35
 * 
 * 
 */
public class CollPawn extends Handler {

    
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollPawn.java,v 1.6 2008/12/18 09:23:47 hraaks Exp $";
    
    public CollPawn(ResourceAccessor ra) {
        super(ra);
        // todo Auto-generated constructor stub
    }
    
    public void CollPawn_SE(){       
        
        if(!ra.isLDBExists("CollPawnLDB")){
            ra.createLDB("CollPawnLDB");
        }
    }   
    
    public boolean CollPawn_txtDateUntil_FV() {
        java.sql.Date datumOd = (java.sql.Date)ra.getAttribute("CollPawnLDB","CollPawn_txtDateFrom");
        java.sql.Date datumDo = (java.sql.Date)ra.getAttribute("CollPawnLDB","CollPawn_txtDateUntil");
        
        if(datumOd != null && datumDo != null){
            if(datumDo.before(datumOd)){
                //Datum DO ne moze biti manji od datuma OD.
                ra.showMessage("wrnclt30c");
                return false;
            }
        }
        
        
        return true; 
    }

    
  public void toCSVfile() throws VestigoTMException {
        
        if(!ra.isRequiredFilled()){
            return;
        }
        if (!ra.isLDBExists("CollPawnLDB")) {
            ra.createLDB("CollPawnLDB");
        }

        ra.setScreenContext("default");

        System.out.println("Prije narucivanja CSV-a");

        //Zadavanje automatske obrade
        Integer answer = (Integer) ra.showMessage("qer075");
        if (answer.intValue() == 0) {
            return;
        }

        if (!ra.isLDBExists("BatchLogDialogLDB")) {
            ra.createLDB("BatchLogDialogLDB");
        }

        try {
            //batch_id od bo31
            ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("2649488003"));
            ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
            ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
            ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));

            //Postavljanje ulaznih parametara batcha
            String param = (String) ra.getAttribute("GDB", "bank_sign");
            String datum_od = ra.getAttribute("CollPawnLDB", "CollPawn_txtDateFrom").toString();
            String datum_do = ra.getAttribute("CollPawnLDB", "CollPawn_txtDateUntil").toString();
            param += ";" + datum_od + ";" + datum_do;
            
            ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
            ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);

            ra.executeTransaction();
            ra.showMessage("inf075");

        }
        catch (hr.vestigo.framework.controller.tm.VestigoTMException vtme) {
            ra.showMessage("errcomzst1");
            error("CollPawnReport:izvodenje funkcije toCSVfile", vtme);
            throw vtme;
        }
    }
 
 
 
 
 

}
