package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.tm.VestigoTMException;
import hr.vestigo.framework.remote.RemoteConstants;

public class CollUnosParametara extends Handler {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollUnosParametara.java,v 1.4 2008/09/29 08:04:19 hramkr Exp $";
    public CollUnosParametara(ResourceAccessor arg0) {
        super(arg0);
        
    }
    
    public void CollUnosParametara_SE(){
        if(!ra.isLDBExists("CollUnosParametaraLDB")){
            ra.createLDB("CollUnosParametaraLDB");
        }
       
    }
    
    public boolean CollUnosParametara_txtDatumDo_FV(String elementName, Object elementValue, Integer lookUpType){
        Date datumOd = (java.sql.Date)ra.getAttribute("CollUnosParametaraLDB","CollUnosParametara_txtDatumOd");
        Date datumDo = (java.sql.Date)ra.getAttribute("CollUnosParametaraLDB","CollUnosParametara_txtDatumDo");
        
        if(datumOd != null && datumDo != null){
            if(datumDo.before(datumOd)){
                //Datum DO ne moze biti manji od datuma OD.
                ra.showMessage("wrnclt30c");
                return false;
            }
        }
       
        return true; 
    }
    private boolean CheckParams(){
    	
    	Date datumOd = (java.sql.Date)ra.getAttribute("CollUnosParametaraLDB","CollUnosParametara_txtDatumOd");
        Date datumDo = (java.sql.Date)ra.getAttribute("CollUnosParametaraLDB","CollUnosParametara_txtDatumDo");
//        System.out.println("Datum OD :"+datumOd );  
//        System.out.println("Datum Do :"+datumDo );

        
       // if(!"null".equals(datumOd) && !"null".equals(datumDo)){
        if(datumOd != null && datumDo != null){
            if(datumDo.before(datumOd)){
                //Datum DO ne moze biti manji od datuma OD.
                ra.showMessage("wrnclt30c");
                return false;
            }
        }
        //if(("null".equals(datumOd) && !"null".equals(datumDo)) || ("null".equals(datumDo) && !"null".equals(datumOd))){
        if((datumOd==null && datumDo !=null) || (datumOd!=null && datumDo==null)){
        ra.showMessage("err181");
        	return false;
        }
        
        return true; 
    	
    }
    public void toCSVfile() throws VestigoTMException{
    	if(CheckParams()){
    		String param = generateBatchParam();  
    	    if(!ra.isLDBExists("BatchLogDialogLDB")){
    			ra.createLDB("BatchLogDialogLDB");
    		}
    	    ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal(1928628003));

    		ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
    		ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
    		ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
    		ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
    		ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
    		ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
    		try {
    		    ra.executeTransaction();
            } catch (Exception e) {
            	System.out.println(e.toString());
            }
            ra.showMessage("inf075");
            
    	}
    	
    }
    
    private String generateBatchParam() {
    StringBuffer buffer = new StringBuffer();
    Date datumOd = (java.sql.Date)ra.getAttribute("CollUnosParametaraLDB","CollUnosParametara_txtDatumOd");
    Date datumDo = (java.sql.Date)ra.getAttribute("CollUnosParametaraLDB","CollUnosParametara_txtDatumDo");
    if(datumOd!=null && datumDo != null){
    	buffer.append(datumOd).append(";").append(datumDo).append(";");
    }
    return buffer.toString();
    }

}
