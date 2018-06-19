/*
 * Created on 2007.11.23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo18;
//newscreen(BatchLog,ctxView,1928628003)
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo18.BO181.VheicleIterator;
import hr.vestigo.modules.coreapp.common.util.CSVGenerator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyK.YXYK1;

/**
 * @author hraamh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO180 extends Batch {
	
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo18/BO180.java,v 1.7 2008/07/31 09:44:51 hramkr Exp $";
    private Vector dataCSV=null;
    private String configFile = System.getProperty("user.dir") + "/" + this.getClass().getPackage().getName().replace('.', '/')+"/vheicle_report.xml";
    private String outputFileName;
    private String outputFile;
    private BatchContext bc;
    private HashMap parameters= new HashMap();
    private String reportName= "csv125";
    private String retcode = RemoteConstants.RET_CODE_SUCCESSFUL;
    private BO181 bo181;

    private static final String encoding = "Cp1250";

	
	public BO180() {
		super();
	}

    private void insertBatchFileLogs(String file_name) throws Exception {
        YXYK1 insertFileLog = new YXYK1(bc);
        insertFileLog.setFileProcessType_Generated();
        insertFileLog.bank_sign = bc.getBankSign();
        insertFileLog.eve_id = bo181.getEve_id();
        insertFileLog.file_name = file_name;
        insertFileLog.file_rec_num = null;
        insertFileLog.file_id_no = null;
        insertFileLog.insertBatchFileLog();
        bc.debug("BO180/insertLogs: Inserted into batch_file_log");
    }
	
	public String executeBatch(BatchContext arg0) throws Exception {
        
		this.bc=arg0;
        bo181 = new BO181(bc);
        dataCSV = new Vector();
        String params [] = bc.getArgs();
        String datum1 = params[0].trim();
        String datum2 = params[1].trim();
        
        Date datumOd= null;
        Date datumDo= null;
        if(!"".equals(datum1) && !"".equals(datum2)){
            datumOd= Date.valueOf(datum1);
            datumDo= Date.valueOf(datum2);
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = new String(dateFormat.format(bc.getExecStartTime()));
        outputFileName = "Lista_Upisnik".concat(dateString).concat(".csv");
        
        
//        outputFileName = reportName.concat(".csv");
        outputFile = bc.getOutDir() + "/" + outputFileName;
        bc.debug("OUTFILE :"+outputFile);
        bc.debug("OUTFNAME :"+outputFile);
        bc.debug("CONFIGFILE :"+configFile);
        try {
            bc.beginTransaction();
            bo181.insertIntoEvent();
            createCSV(datumOd, datumDo);
            CSVGenerator.printCSV(reportName,bc.getLogin(), dataCSV, parameters,
                    configFile, outputFile, encoding);
            YXY70.send(bc, reportName, bc.getLogin() , outputFile);
            bc.commitTransaction();
        } catch (Exception e) {
            bc.debug("Exception "+e.toString());
            retcode = RemoteConstants.RET_CODE_ERROR;
        }
        
		return retcode;
	}
    public void createCSV(Date datumOd,Date datumDo) throws Exception{
        List lst = new ArrayList();
        if (datumOd==null || datumDo == null){
            lst = bo181.getData();
        }
        else{
            lst = bo181.getData(datumOd, datumDo);
        }
        Iterator iter = lst.iterator();
        while (iter.hasNext()) {
            VheicleData element = (VheicleData) iter.next();
            HashMap row = new HashMap();
            row.put("nr", new Integer(element.nr));
            row.put("col_num", element.col_num);
            row.put("veh_vin_num", element.veh_vin_num);
            row.put("date_to_lop", element.date_to_loop);
            row.put("acc_no", element.acc_no);
            row.put("register_no", element.register_no);
            row.put("name", element.name);
            dataCSV.add(row);
            
            
        }
    }

	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal(1928628003));// 
        bp.setArgs(args);
        new BO180().run(bp);
	}
}
