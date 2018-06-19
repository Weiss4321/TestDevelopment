//created 2015.03.02
package hr.vestigo.modules.collateral.batch.bo98;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo98.BO981.DataIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.util.ExcelUtils;

/**
 *
 * @author hrajkl
 */
public class BO980 extends Batch {

    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo98/BO980.java,v 1.1 2015/03/13 11:48:09 hrajkl Exp $";

    private BatchContext bc;
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheet;
    private int rowIndex;
    private int columnIndex;
    
    @Override
    public String executeBatch(BatchContext bc) throws Exception {
        
        this.bc = bc;
        
        //evidentiranje eventa
        insertIntoEvent();
           
        //parametri obrade
        if(!parametriObrade()){
            return RemoteConstants.RET_CODE_ERROR;
        }
        /*
        //kontrola zadnjeg dana kvartala
        if(!zadnjiDanKvartala(bc.getExecStartTime().getTime())){
            info("Izvršavanje obrade na trenutni datum :"+bc.getExecStartTime()+" nije dozvoljeno!");
            return RemoteConstants.RET_CODE_ERROR;
        }
        */
        //inicijalizacija
        this.workbook = new SXSSFWorkbook();
        this.styles = ExcelStyleData.createStyles(workbook);
        this.sheet = this.workbook.createSheet("Izvje\u0161\u0107e F66 - procjenitelji i njihove procjene");
        ExcelUtils.setColumnWidths(this.sheet, new int[] { 150, 80, 100, 200, 200, 100, 70, 100, 70, 200, 200, 80, 80, 200, 150 });
        
        //dohvat podataka
        BO981 bo981 = new BO981(bc);
        DataIterator it = bo981.selectData();
        
        try {
            //zapis header-a
            writeHeader();
            
            EstimateData oldEstimateData = new EstimateData(null, null);
            while (it.next()) {
                if(!oldEstimateData.equals(new EstimateData(it.COL_HEA_ID(), it.DATUM_PROCJENE()))){
                    System.out.println("oldEstimateData:"+oldEstimateData.coll_hea_id+":"+oldEstimateData.real_est_estn_date);
                    writeRow(it);
                }
                oldEstimateData.coll_hea_id = it.COL_HEA_ID();
                oldEstimateData.real_est_estn_date = it.DATUM_PROCJENE();
            }
            
            //kreiranje datoteke
            String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
            String fileName = "";
            fileName = bc.getOutDir() + "/" + "F66_" + dateString + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            new File(fileName + ".marker").createNewFile();

            //slanje na mail
            Vector attachments = new Vector();
            YXY70.send(bc, "csvbo98", bc.getLogin(), fileName);
            info("Izvjestaj je kreiran i poslan na mail.");
            
        } catch (Exception e) {
            error("Dogodila se nepredvidjena greska kod kreiranja izvjestaja!", e);
            throw e;
        }finally{
            if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
        }
        
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u zadani izvještaj.
     */
    private void writeHeader() throws Exception
    {
        rowIndex = 0;
        columnIndex = 0;
        ExcelStyleData styles = this.styles;
        
        Row row = sheet.createRow(rowIndex);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tip nekretnine", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrsta nekretnine", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Mjesto", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Procjenitelj", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Za tvrtku procjenio", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tr\u017Ei\u0161na vrijednost (TV)", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta TV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Nova gra\u0111evinska vrijednost  (NGV)", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta NGV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Cijena kupoprodajnog ugovora", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Opis procjene", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum procjene", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Datum kva\u010Dice CO", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Co verificirao", styles.headingStyle);
        
        
        rowIndex++;
        
        sheet.createFreezePane(0, rowIndex);
    }
    
    private void writeRow(DataIterator iter) throws Exception
    {
        columnIndex = 0;
        ExcelStyleData styles = this.styles;
        
        info(""+iter.SIFRA_KOLATERALA()+"|"+iter.COL_HEA_ID()+"|"+iter.DATUM_PROCJENE()+"|");
        info("rowIndex:"+rowIndex);
        
        Row row = sheet.createRow(rowIndex);
        ExcelUtils.createCell(row, columnIndex++, iter.SIFRA_KOLATERALA(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.TIP_NEKRETNINE(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.VRSTA_NEKRETNINE(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.MJESTO(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.PROCJENITELJ(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.ZA_TVRTKU_PROCIJENIO(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.TV(), styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.TV_VALUTA(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.NGV(), styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.NGV_VALUTA(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.CIJENA_KUPOPRODAJNOG_UGOVORA(), styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.OPIS_PROCJENE(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.DATUM_PROCJENE(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.DATUM_KVACICE(), styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, iter.CO_VERIFICIRAO(), styles.normalStyle);
        
        rowIndex++;
    }
    

    /**
     * Metoda za kontrolu pokretanja obrade zadnjeg dana kvartala
     * @param currentTimeMillis
     * @return
     * @throws Exception
     */
    private boolean zadnjiDanKvartala(long currentTimeMillis) throws Exception
    {
        
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currentTimeMillis);
        
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        bc.debug("month:"+month+", date:"+date);//mjeseci idu od 0 a dani od 1 (0-JAN, 1-prvi dan mjeseca)
        
        if( (month == Calendar.MARCH) && (date == c.getActualMaximum(Calendar.DATE)) ){
            info("Q1");
            return true;
        }else if ( (month == Calendar.JUNE) && (date == c.getActualMaximum(Calendar.DATE)) ) {
            info("Q2");
            return true;            
        }else if ( (month == Calendar.SEPTEMBER) && (date == c.getActualMaximum(Calendar.DATE)) ) {
            info("Q3");
            return true;
        }else if ( (month == Calendar.DECEMBER) && (date == c.getActualMaximum(Calendar.DATE)) ){
            info("Q4");
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @return
     * @throws Exception
     */
    private boolean parametriObrade()throws Exception{
        
        if(bc.getArgs().length != 1){
            info("Broj parametara "+bc.getArgs().length+" nije odgovarajuæi!");
            return false;
        }
        
        if(!bc.getArg(0).equals("RB")){
            info("Oznaka banke "+bc.getArg(0)+" nije ispravna!");
            return false;
        }
        
        return true;
    }
    
    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO980.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("7709414704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Izvjesce o procjeniteljima i  njihovim procjena za nekretnine");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO980.insertIntoEvent");
            return eve_id;
        }
        catch (Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri evidentiranju pocetka izvodjenja obrade!", ex);
            throw ex;
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        
        BatchParameters bp = new BatchParameters(new BigDecimal("7709411704"));
        bp.setArgs(args);
        new BO980().run(bp);
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
    
}

