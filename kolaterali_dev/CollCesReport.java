//created 2014.04.28
package hr.vestigo.modules.collateral;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import hr.vestigo.framework.controller.handler.Handler;
import hr.vestigo.framework.controller.handler.ResourceAccessor;
import hr.vestigo.framework.controller.lookup.EmptyLookUp;
import hr.vestigo.framework.controller.lookup.LookUpRequest;
import hr.vestigo.framework.controller.lookup.NothingSelected;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.util.CharUtil;

/**
 *
 * @author hraaks
 */
public class CollCesReport extends Handler {
    
    private String ldbName = "CollCesReportLDB";
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public CollCesReport(ResourceAccessor ra) {
        super(ra);
        // TODO Auto-generated constructor stub
    }

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/CollCesReport.java,v 1.3 2014/05/22 10:55:36 hraaks Exp $";
    
    
    public void CollCesReport_SE(){
        
        if(!ra.isLDBExists(ldbName))
            ra.createLDB(ldbName);
    }
    
    public boolean CollCesReport_txtReg_no1_FV(String elementName, Object elementValue, Integer lookUpType){
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "txtReg_no1", "");
            ra.setAttribute(ldbName, "txtCustomerName1", "");
            ra.setAttribute(ldbName, "cus_id1", null);
            //ra.setAttribute("SMEScoringLDB", "SMEScoring_txtCompanyName", null);
            //clearSMECustomerData();
            
            return true;
        }
        
        if (ra.getCursorPosition().equals("txtReg_no1")) {
            ra.setAttribute(ldbName, "txtCustomerName1", "");
        } else if (ra.getCursorPosition().equals("txtCustomerName1")) {
            ra.setAttribute(ldbName, "txtReg_no1", "");
            //ra.setCursorPosition(2);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "txtCustomerName1") != null)
            d_name = (String) ra.getAttribute(ldbName, "txtCustomerName1");
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "txtReg_no1") != null)
            d_register_no = (String) ra.getAttribute(ldbName, "txtReg_no1");
        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }

        //kontola da li je zvjezdica na pravom mjestu ili se baca exception s porukom, samo register_no
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
            ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", null);
            ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
        } else {
            ra.createLDB("CustomerAllLookUpLDB");
        }
        //init za poruku o pogresci
       
        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "txtReg_no1"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "txtCustomerName1"));

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        
        ra.setAttribute(ldbName, "cus_id1", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "txtReg_no1", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "txtCustomerName1", ra.getAttribute("CustomerAllLookUpLDB", "name"));
        
        
        return true;
        
    }
    
    public boolean CollCesReport_txtReg_no2_FV(String elementName, Object elementValue, Integer lookUpType){
        
        if (elementValue == null || ((String) elementValue).equals("")) {
            ra.setAttribute(ldbName, "txtReg_no2", "");
            ra.setAttribute(ldbName, "txtCustomerName2", "");
            ra.setAttribute(ldbName, "cus_id2", null);
            //ra.setAttribute("SMEScoringLDB", "SMEScoring_txtCompanyName", null);
            //clearSMECustomerData();
            
            return true;
        }
        
        if (ra.getCursorPosition().equals("txtReg_no2")) {
            ra.setAttribute(ldbName, "txtCustomerName2", "");
        } else if (ra.getCursorPosition().equals("txtCustomerName2")) {
            ra.setAttribute(ldbName, "txtReg_no2", "");
            //ra.setCursorPosition(2);
        }

        String d_name = "";
        if (ra.getAttribute(ldbName, "txtCustomerName2") != null)
            d_name = (String) ra.getAttribute(ldbName, "txtCustomerName2");
        String d_register_no = "";
        if (ra.getAttribute(ldbName, "txtReg_no2") != null)
            d_register_no = (String) ra.getAttribute(ldbName, "txtReg_no2");
        if ((d_name.length() < 4) && (d_register_no.length() < 4)) {
            ra.showMessage("wrn366");
            return false;
        }

        //kontola da li je zvjezdica na pravom mjestu ili se baca exception s porukom, samo register_no
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
            ra.setAttribute("CustomerAllLookUpLDB", "eco_sec", null);
            ra.setAttribute("CustomerAllLookUpLDB", "residency_cou_id", null);
        } else {
            ra.createLDB("CustomerAllLookUpLDB");
        }
        //init za poruku o pogresci
       
        ra.setAttribute("CustomerAllLookUpLDB", "register_no", ra.getAttribute(ldbName, "txtReg_no2"));
        ra.setAttribute("CustomerAllLookUpLDB", "name", ra.getAttribute(ldbName, "txtCustomerName2"));

        LookUpRequest lookUpRequest = new LookUpRequest("CustomerAllLookUp");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_id", "cus_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "register_no", "register_no");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "code", "code");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "name", "name");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "add_data_table", "add_data_table");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_typ_id", "cus_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "cus_sub_typ_id", "cus_sub_typ_id");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "eco_sec", "eco_sec");
        lookUpRequest.addMapping("CustomerAllLookUpLDB", "residency_cou_id", "residency_cou_id");

        try {
            ra.callLookUp(lookUpRequest);
        } catch (EmptyLookUp elu) {
            ra.showMessage("err012");
            return false;
        } catch (NothingSelected ns) {
            return false;
        }
        
        ra.setAttribute(ldbName, "cus_id2", ra.getAttribute("CustomerAllLookUpLDB", "cus_id"));
        ra.setAttribute(ldbName, "txtReg_no2", ra.getAttribute("CustomerAllLookUpLDB", "register_no"));
        ra.setAttribute(ldbName, "txtCustomerName2", ra.getAttribute("CustomerAllLookUpLDB", "name"));
        
        
        return true;
        
        
        
        
    }
    
    public void date_check() throws Exception{
        boolean reg_no1 =ra.getAttribute(ldbName, "cus_id1")!=null?
                true:false;
        boolean reg_no2 = ra.getAttribute(ldbName, "cus_id2")!=null?
                true:false;
        
        System.out.println("reg_no1:"+ reg_no1);
        System.out.println("reg_no2:"+reg_no2);
        Date date = ra.getAttribute(ldbName, "txtDate"); 
        System.out.println("date"+date);
        // 
        boolean register_check = ((!reg_no1 && !reg_no2) || !reg_no1 && reg_no2 );
        if(register_check || date==null){
                ra.showMessage("CollCesReportMsg");
                return; 
            
        }
        
        
        try {
            // hr.vestigo.modules.collateral.jcics.co41.CO412.sqlj
            ra.executeTransaction();
            System.out.println("first transaction");
        } catch (Exception e) {
            // TODO: handle exception
        }
        String flag = ra.getAttribute(ldbName, "flag");
        if(flag.equalsIgnoreCase("0")){
            ra.showMessage("CollInvestPartiesAllDateMsg");
            return;
        }
        ra.invokeAction("confirm");
    }
    
    public void confirm() throws Exception{
        
        String reg_no1 =ra.getAttribute(ldbName, "cus_id1")!=null?
                (String)ra.getAttribute(ldbName, "txtReg_no1"):"X";
        String reg_no2 = ra.getAttribute(ldbName, "cus_id2")!=null?
                (String)ra.getAttribute(ldbName, "txtReg_no2"):"X";
        Date date = ra.getAttribute(ldbName, "txtDate");   
        java.util.Date date_tmp = new java.util.Date(date.getTime());
        String date_in = sdf.format(date_tmp);
       
        String param = "RB;"+date_in+";"+reg_no1+";"+reg_no2;
        
        System.out.println("param:"+ param);
        if(!ra.isLDBExists("BatchLogDialogLDB")){
        ra.createLDB("BatchLogDialogLDB");
        }
        // bo90 batch
        ra.setAttribute("BatchLogDialogLDB", "BatchDefId", new BigDecimal("6534454704"));
        
        ra.setAttribute("BatchLogDialogLDB", "RepDefId", null);
        ra.setAttribute("BatchLogDialogLDB", "AppUseId", ra.getAttribute("GDB", "use_id"));
        ra.setAttribute("BatchLogDialogLDB", "fldNotBeforeTime", new Timestamp(System.currentTimeMillis()));
        ra.setAttribute("BatchLogDialogLDB", "fldParamValue", param);
        ra.setAttribute("BatchLogDialogLDB", "fldExecStatus", RemoteConstants.EXEC_SCHEDULED);
        ra.setAttribute("BatchLogDialogLDB", "fldExecStatusExpl", RemoteConstants.EXEC_EXPL_SCHEDULED);
        Integer answer = null;     
        
        answer = (Integer) ra.showMessage("msgIzvodiConfirm");
        if (answer != null && answer.intValue() == 0) 
                return;
            else{
                    try {
                            ra.executeTransaction();
                         } catch (Exception e) {
                                 System.out.println(e.toString());
                          }
                    }
        ra.showMessage("inf075");
        
        
        
        
    }
    
}

