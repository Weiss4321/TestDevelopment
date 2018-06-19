//created 2014.04.03
package hr.vestigo.modules.collateral.batch.bo88;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;

import hr.vestigo.modules.collateral.batch.bo85.ExcelStyleData;
import hr.vestigo.modules.collateral.batch.bo88.BO881.Iter1;


import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.common.yrx6.B07ElementData;
import hr.vestigo.modules.rba.common.yry7.YRY70;
import hr.vestigo.modules.rba.util.ExcelUtils;
import hr.vestigo.modules.rba.util.SendMail;

/**
 *
 * @author hraaks
 */
public class BO880 extends Batch {
    
    private BatchContext bc;
    private BO881 bo881;
    private String return_code = RemoteConstants.RET_CODE_SUCCESSFUL;
    public String report_code = "xls667";
    public String file_name ;
    
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheet1;
    private Date value_date;
    int row_counter = 5;

    private YXY70 yxy70;
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo88/BO880.java,v 1.4 2014/05/27 09:17:29 hraaks Exp $";

    @Override
    public String executeBatch(BatchContext bc) throws Exception {
        this.bc = bc;
        
        bo881 = new BO881(bc);
        String ts = (""+new Timestamp(System.currentTimeMillis())).replace(":", "_");
        file_name = "Izuzeti_depoziti_"+ts+".xls";
        openReport(new Date(System.currentTimeMillis()));
        Iter1 iter = bo881.getData();
        while(iter.next()){
            WriteRow(iter);
        }
        saveReport();
        sendMail();
        dispose();
        return return_code;
    }
    
    private void openReport(Date value_date)
    {
        // kreiraj workbook
        this.workbook = new SXSSFWorkbook(100);
        this.styles = ExcelStyleData.createStyles(workbook);
        
        // kreiraj sheetove
        this.sheet1 = workbook.createSheet("sheet1");
        ExcelUtils.setColumnWidths(sheet1, new int[] { 100, 80, 400, 80, 80, 80, 200 });
        
        Row row = sheet1.createRow(0);
        ExcelUtils.createCell(row, 0, value_date, styles.titleDateStyle);
        
        row = sheet1.createRow(1);
        String title = "Izvje\u0161\u0107e o izuzetim depozitima"; 
        ExcelUtils.createCell(row, 0, title, styles.titleStyle);
        
        Row row1 = sheet1.createRow(4);
        row.setHeightInPoints(74);
        
        int columnIndex = 0;//
        ExcelUtils.createCell(row1, columnIndex++, "Partija", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "IM", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Naziv", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Status", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Datum od", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Datum do ", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Referent", styles.headingStyle);
        
        
        sheet1.createFreezePane(0, 5);
      
    }
    
    private void dispose()
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }
    
    private void saveReport() throws Exception
    {
        bc.startStopWatch("BO880.saveReport");
        
        String dir = bc.getOutDir() + "/";
        file_name = dir+file_name;
        bc.info("Izlazna datoteka: " + file_name);
        
        FileOutputStream fileOut = new FileOutputStream(file_name);
        workbook.write(fileOut);
        fileOut.close();
        dispose();
        
        new File(file_name + ".marker").createNewFile();
        
        bc.stopStopWatch("BO880.saveReport");
    }
   
    private void WriteRow(Iter1 iter ) throws Exception{
        
        bc.startStopWatch("BO880.write row");
         
         Row row = sheet1.createRow(row_counter);
         int columnIndex = 0;
       
         ExcelUtils.createCell(row, columnIndex++, iter.cde_account(), styles.normalStyle);
         ExcelUtils.createCell(row, columnIndex++, iter.interni_mb(), styles.normalStyle);
         ExcelUtils.createCell(row, columnIndex++, iter.naziv(), styles.normalStyle);
         ExcelUtils.createCell(row, columnIndex++, iter.status(), styles.normalStyle);
         ExcelUtils.createCell(row, columnIndex++, iter.date_from(), styles.normalDateStyle);
         ExcelUtils.createCell(row, columnIndex++, iter.date_until(),styles.normalDateStyle);
         ExcelUtils.createCell(row, columnIndex++, iter.user_name(), styles.normalStyle);
         row_counter++;
    }
    
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO880.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6531960704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "bo88: Izuzeti plasmani");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO880.insertIntoEvent");
            return eve_id;
        }
        catch(Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade!", ex);
            throw ex;
        }
    }
    
    private void error(String message, Exception ex) throws Exception
    {
        if (ex != null) bc.error(message, ex); else bc.error(message, new String[]{});
        bc.userLog("GRESKA: " + message);
    }
    
    private void info(String message) throws Exception
    {
        bc.info(message);
        bc.userLog(message);
    }
    
    private void sendMail() throws Exception
    {
        bc.startStopWatch("BO880.sendMail");
        int flag = 0;
        String mail_addreses=bo881.selectMailAddresses();
      
        YXY70.send(bc, mail_addreses, mail_addreses, "", "Obrada bo88: Izuzeti depoziti",  bc.getUserName(), file_name);
        //(bc, "xls667", bc.getUserName(), file_name);
        
//        SendMail mail = new SendMail();
//        flag = mail.send(bc,"", "", "", bo881.selectMailAddresses(), "", "Obrada bo88: Izuzeti depoziti", "Izuzeti depoziti" , null, file_name);
//        //YXY70.send(bc, "xls667", bc.getLogin(), file_name);
//        if(flag!=0)
//            bc.userLog("Neuspjelo slanje na:" + bo881.selectMailAddresses() + "\n\n**");
        bc.stopStopWatch("BO880.sendMail");
    }
    
    // eve_Typ_id = 
    
    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("6531959704"));
        bp.setArgs(args);
        new BO880().run(bp);
    }
}

