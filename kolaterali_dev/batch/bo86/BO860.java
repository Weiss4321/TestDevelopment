//created 2014.03.26
package hr.vestigo.modules.collateral.batch.bo86;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo85.ExcelStyleData;
import hr.vestigo.modules.collateral.batch.bo86.BO861.ColProcIterator;
import hr.vestigo.modules.collateral.batch.bo86.BO861.Expiterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 *
 * @author hraaks
 */
public class BO860 extends Batch {

    private BatchContext bc;
    private BO861 bo861;
    private String return_code = RemoteConstants.RET_CODE_SUCCESSFUL;
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo86/BO860.java,v 1.8 2015/04/21 07:40:44 hraaks Exp $";
    public String report_code = "xls666";
    public String file_name ;
    
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheet1;
    private Date value_date;
    private String client_type;
    int row_counter = 0;
    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        String [] params = bc.getArgs();
       
        value_date = (Date) (params[1]!=null?Date.valueOf(params[1].trim()):null);
        client_type = params[2]!=null?params[2].trim():"";
        file_name = "Izvjesce_o_pokrivenosti_plasmana_"+value_date.toString()+".xls";
        if(client_type==null || value_date == null){
            bc.userLog("Neispravn broj parametara\n Obrada prima 2 parametra: 1-tip komitenta, 2- datum izvjestaja");
            return_code = RemoteConstants.RET_CODE_FATAL;
            return return_code;
        }
        this.bo861 = new BO861(bc);
        insertIntoEvent();
        ColProcIterator iter = bo861.getData(value_date);
        System.out.println("iter:"+ iter.toString());
        openReport(value_date);
        row_counter = 5; //  prva su popunjeni zaglavljem pa krecem pisati od 5 reda
        BigDecimal curr_eur =bo861.getExchangeRate(value_date, new BigDecimal(64999));
        System.out.println("curr_eur:"+ curr_eur);
        System.out.println("client_type:"+ client_type);
        
        int i = 0;
        while(iter.next()){
            Expiterator iter2 = bo861.getReportData(iter.col_pro_id(), client_type);
            while(iter2.next()){
                WriteRow(iter2, curr_eur,value_date );
                i++;
                
            }
               
        }
        bc.debug("Obradjeno ukupno:" + i + " zapisa");
        saveReport();
        sendMail();
        
        
        return return_code;
    }
    
   
    
    private void dispose()
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }
    
    private void saveReport() throws Exception
    {
        bc.startStopWatch("BO860.saveReport");
        
        String dir = bc.getOutDir() + "/";
        file_name = dir+ file_name;
        bc.info("Izlazna datoteka: " + file_name);
        
        FileOutputStream fileOut = new FileOutputStream(file_name);
        workbook.write(fileOut);
        fileOut.close();
        dispose();
        
        new File(file_name + ".marker").createNewFile();
        
        bc.stopStopWatch("BO860.saveReport");
    }
    
    private void openReport(Date value_date)
    {
        // kreiraj workbook
        this.workbook = new SXSSFWorkbook(100);
        this.styles = ExcelStyleData.createStyles(workbook);
        
        // kreiraj sheetove
        this.sheet1 = workbook.createSheet("sheet1");
        ExcelUtils.setColumnWidths(sheet1, new int[] { 90, 65, 400, 130, 110, 200, 70, 110, 110, 130, 100, 100 });
        
        Row row = sheet1.createRow(0);
        ExcelUtils.createCell(row, 0, value_date, styles.titleDateStyle);
        
        row = sheet1.createRow(1);
        String title = "Izvje\u0161\u0107e za pokrivenost plasmana"; 
        ExcelUtils.createCell(row, 0, title, styles.titleStyle);
        
        Row row1 = sheet1.createRow(4);
        row.setHeightInPoints(74);
        
        int columnIndex = 0;//
        ExcelUtils.createCell(row1, columnIndex++, "IM grupacije", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Naziv grupacije", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "IM komitenta", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Naziv komitenta", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Partija pla.", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Val.pla. ", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Izlo\u017Eenost eur", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Sifra kol.", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Tip kol.", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Val. kol.", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "NCV, eur", styles.headingStyle);
        ExcelUtils.createCell(row1, columnIndex++, "Iskor. kol., eur", styles.headingStyle);
        
        sheet1.createFreezePane(0, 5);
      
    }
   
    
    private void WriteRow(Expiterator iter, BigDecimal curr, Date value_date) throws Exception{
        
       bc.startStopWatch("BO860.write row");
        
        Row row = sheet1.createRow(row_counter);
        int columnIndex = 0;
        System.out.println("iter.exp_balance_hrk()-->"+iter.exp_balance_hrk());
        System.out.println("iter.coll_amount()-->"+iter.coll_amount());
        ExcelUtils.createCell(row, columnIndex++, iter.cus_gro_code(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.cus_gro_name(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.register_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.name(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.cus_acc_no(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.cur_exp_char(), styles.normalStyle);
        
        BigDecimal balance_eur = iter.exp_balance_hrk()!=null?iter.exp_balance_hrk().divide(curr, 2, RoundingMode.HALF_UP):new BigDecimal("0.00");
        bc.debug(iter.exp_balance_hrk()+"/"+ curr +"="+balance_eur);
        ExcelUtils.createCell(row, columnIndex++, balance_eur , styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.col_num(), styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.col_type_name(), styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.cur_col_char(), styles.normalStyle);
        // iznos srednjeg tecaja na dan izvjetaja
        BigDecimal val_mid_rate = bo861.getCurrencyMiddRate(value_date, iter.cur_col_char().trim());
        BigDecimal amount_eur = new BigDecimal("0.00");
        BigDecimal coll_amount_hrk = new BigDecimal("0.00");
        if(iter.coll_amount()!=null){
            coll_amount_hrk = val_mid_rate.multiply( iter.coll_amount()).setScale(2,RoundingMode.HALF_UP);
            amount_eur = coll_amount_hrk.divide(curr, 2, RoundingMode.HALF_UP);;
        }
       
        bc.debug("val_mid_rate:"+ val_mid_rate+"\n coll_amount_hrk:"+ coll_amount_hrk +"\n amount_eur:"+ amount_eur +"cur_eur:"+curr +"\n" );
//        if(!iter.cur_col_char().trim().equals("EUR"))
//            ExcelUtils.createCell(row, columnIndex++, amount_eur ,styles.normalStyle);
//        else
        BigDecimal ret = iter.exp_coll_amount()!=null?iter.exp_coll_amount().divide(curr, 2, RoundingMode.HALF_UP):null;
        ExcelUtils.createCell(row, columnIndex++, amount_eur ,styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, ret, styles.normalStyle);
        bc.debug("cus_gro_code:"+ iter.cus_gro_code() + " cus_gro_name:"+iter.cus_gro_name() + " cus_acc_no:"+ iter.cus_acc_no() + " coll_type_name:"+iter.col_type_name() + " coll_amount:"+ ret + " amount_eur:" + amount_eur);
        row_counter++;
        
        bc.stopStopWatch("BO860.write row");
    }
    
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO860.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6529088704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "bo86: Izvjesca o pokrivenosti plasmana");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO860.insertIntoEvent");
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
        bc.startStopWatch("BO860.sendMail");
        YXY70.send(bc, "xls666", bc.getLogin(), file_name);
        bc.stopStopWatch("BO860.sendMail");
    }

    
    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("6529087704"));
        bp.setArgs(args);
        new BO860().run(bp);
    }






   
    
}

