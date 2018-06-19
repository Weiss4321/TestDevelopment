package hr.vestigo.modules.collateral.batch.bo35;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.HashMap;
import java.util.Vector;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo35.BO351.Iter;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;

public class BO350 extends Batch {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo35/BO350.java,v 1.6 2012/02/14 09:46:39 hramkr Exp $";
    
    private String reportName= "csv190";
    
    private String outputFileName;
    private String outputFile;
    private String retcode = RemoteConstants.RET_CODE_SUCCESSFUL;
    private BatchContext context;
    
    
    public String executeBatch(BatchContext bc) throws Exception {
        context=bc;
        BO351 bo351 = new BO351(bc);
        outputFileName = "kontrola_upisnika "+ new Timestamp(System.currentTimeMillis()).toString() + ".csv";
        outputFile = bc.getOutDir() + "/" + outputFileName;
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(this.outputFile)));
        bc.debug(bc.getArg(0));
        bc.debug(bc.getArg(1));
        bc.debug(bc.getArg(2));
        Date datum_od = Date.valueOf(bc.getArg(1).trim());
        Date datum_do = Date.valueOf(bc.getArg(2).trim());
        bc.debug(datum_od.toString()+ "   "+datum_do.toString());
        int num = 0;
        BO351.Iter iterator = bo351.fetchData(datum_od, datum_do);
        streamWriter.write("Izvjesce o azuriranju podataka o fiducijama vozila za period :" + datum_od.toString() + " "+ datum_do.toString()+ "\n");
        streamWriter.write("Redni broj;Broj sasije;Partija plasmana;Sifra komitenta;Datum odobrenja;Broj zakljucka;Datum upisa\n");
        bo351.insertIntoEvent();
        while(iterator.next()){
            num++;
            
            
            streamWriter.write(""+num+";"+iterator.veh_vin_num()+";"+iterator.cus_acc_no()+";"+iterator.register_no()+";"+iterator.approval_date()+";"+iterator.veh_con_num()+";"+iterator.con_date()+";\n");
           // bc.debug(""+num+";"+iterator.veh_vin_num()+";"+iterator.cus_acc_no()+";"+iterator.register_no()+";"+iterator.approval_date()+";"+iterator.veh_con_num()+";"+iterator.con_date()+";\n");
        }
        streamWriter.flush();
        streamWriter.close();
        YXY70.send(context,reportName, bc.getLogin() , outputFile);
        return retcode;
    }
    
    
    public static void main(String[] args) {
        BatchParameters bp = new BatchParameters(new BigDecimal("2649488003"));// 
        bp.setArgs(args);
        new BO350().run(bp);
    }

}
