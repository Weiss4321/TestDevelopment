package hr.vestigo.modules.collateral.batch.boA2;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.boA2.BOA21.IterCol;
import hr.vestigo.modules.collateral.batch.boA2.BOA21.IterPl;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Izvješæe o kolateralima s više aktivnih polica osiguranja
 * @author hrakis
 */
public class BOA20 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/boA2/BOA20.java,v 1.1 2017/03/13 12:03:22 hrakis Exp $";
    
    private BatchContext bc;
    private BOA21 boA21;
    
    private String cust_type;
    
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheet;
    private int rowIndex;
    private int columnIndex;
    private String fileName;
    
    private final String rpt_code = "csvboA2";
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        this.boA21 = new BOA21(bc);
        this.bc = bc;
        
        // evidentiranje eventa
        boA21.insertIntoEvent();

        // dohvat i provjera parametara obrade
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        try
        {
            // otvaranje izvještaja
            openReport();

            // dohvat podataka
            IterCol iter = boA21.selectCollaterals(cust_type);

            // zapisivanje podataka u izvještaj
            while (iter.next())
            {
                // kolateral
                CollateralData collData = new CollateralData();
                collData.col_hea_id = iter.col_hea_id();
                collData.col_num = iter.col_num();
                collData.new_build_val = iter.new_build_val();

                // vlasnik plasmana povezanog na prvu aktivnu RBA hipoteku
                String owner = boA21.selectRetailPlacementOwner(collData.col_hea_id);
                if (owner == null) continue;

                // aktivna grupna polica
                boA21.selectGroupPolicy(collData);
                if (collData.group_ip_id == null) continue;

                // druga aktivna polica
                boA21.selectOtherPolicy(collData);
                if (collData.other_ip_id == null) continue;
                
                // vlasnik kolaterala
                boA21.selectCollateralOwner(collData);

                // plasmani
                IterPl iterPlacement = boA21.selectPlacements(cust_type, collData);
                while (iterPlacement.next())
                {
                    collData.placement_owner_register_no = iterPlacement.register_no();
                    collData.placement_owner_name = iterPlacement.name();
                    collData.cus_acc_no = iterPlacement.cus_acc_no();
                    collData.cus_acc_orig_st = iterPlacement.cus_acc_orig_st();
                    
                    writeDetailsRow(collData);
                }
                iterPlacement.close();
            }
            iter.close();

            // spremanje izvještaja
            saveReport();
        }
        finally
        {
            dispose(workbook);
        }
        
        // slanje izvještaja na mail
        sendMail();
        
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda otvara izvještaj i kreira sheet u koji æe biti zapisani podaci. 
     */
    private void openReport()
    {
        workbook = new SXSSFWorkbook(100);
        styles = ExcelStyleData.createStyles(workbook);
        sheet = workbook.createSheet("Kolaterali s vi\u0161e aktivnih polica");
        ExcelUtils.setColumnWidths(sheet, new int[] { 120, 130, 80, 80, 130, 150, 80, 80, 80, 160, 100, 80, 170, 120, 110 }); 
        writeHeaderRow();
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u izvještaj.
     */
    private void writeHeaderRow()
    {
        rowIndex = 0;
        columnIndex = 0;
        
        Row row = sheet.createRow(rowIndex);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra Uniqa grupne police", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "GP pla\u0107ena od", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "GP pla\u0107ena do", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra druge aktivne police", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Osiguravatelj druge aktivne police", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrijedi do", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Premija pla\u0107ena od", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Premija pla\u0107ena do", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vlasnik nekretnine", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "NGV", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "ID korisnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv korisnika plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status plasmana u modulu", styles.headingStyle);
        
        rowIndex++;
        sheet.createFreezePane(0, rowIndex);
    }
    
    
    /**
     * Metoda zapisuje podatke o kandidatu u izvještaj.
     * @param collData objekt s podacima o kolateralu i policama
     */
    private void writeDetailsRow(CollateralData collData) throws Exception
    {
        columnIndex = 0;
        
        Row row = sheet.createRow(rowIndex);
        ExcelUtils.createCell(row, columnIndex++, collData.col_num.trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.group_ip_code.trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.group_ip_vali_from, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.group_ip_vali_until, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.other_ip_code.trim(), styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.other_ic_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.other_ip_date_sec_val, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.other_ip_vali_from, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.other_ip_vali_until, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.coll_owner_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.new_build_val, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.placement_owner_register_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.placement_owner_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.cus_acc_no, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, collData.cus_acc_orig_st, styles.normalStyle);

        rowIndex++;
    }
    
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku i kreira pripadajuæu marker datoteku.  
     */
    private void saveReport() throws Exception
    {
        String dir = bc.getOutDir() + "/";
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        fileName = dir + "KolateraliSViseAktivnihPolica" + dateString + ".xlsx";
        
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        new File(fileName + ".marker").createNewFile();
        
        info("Broj redova zapisanih u izvjestaj: " + rowIndex);
    }
    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose(Workbook workbook)
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }
    
    /**
     * Metoda koja šalje mail s priloženim izvještajem.
     */
    private void sendMail() throws Exception
    {
        bc.startStopWatch("BOA20.sendMail");
        
        Vector attachments = new Vector();
        attachments.add(fileName);
        YXY70.send(bc, rpt_code, bc.getLogin(), attachments);
        
        bc.stopStopWatch("BOA20.sendMail");
    }
    
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     *    <dt>cust_type</dt><dd>Vrsta komitenta. P za pravne osobe, F za fizièke osobe.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Vrsta komitenta" };

            // ispiši parametre predane obradi
            info("Parametri predani obradi:");
            for (int i = 0; i < bc.getArgs().length; i++)
            {
                String parameterName = (i < bc.getArgs().length) ? parameterNames[i] : "Nepoznati parametar";
                String parameterValue = bc.getArg(i);
                info("   " + parameterName + " = " + parameterValue);
            }

            // provjeri da li je broj parametara ispravan
            if (bc.getArgs().length != parameterNames.length)
            {
                error("Neispravan broj parametara - predani broj parametara je " + bc.getArgs().length + ", a obrada prima " + parameterNames.length + "!", null);  
                return false;
            }
            
            // provjeri oznaku banke
            String bank_sign = bc.getArg(0);
            if (!bank_sign.equals("RB"))
            {
                error("Oznaka banke mora biti RB!", null);
                return false;
            }
            bc.setBankSign("RB");
            
            // vrsta komitenta
            cust_type = bc.getArg(1).trim().toUpperCase();
            if (!cust_type.equals("F"))
            {
                error("Vrsta komitenta moze biti samo F!", null);
                return false;
            }
            
            return true;
        }
        catch (Exception ex)
        {
            error("Neispravno zadani parametri!", ex);
            return false;
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

    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("1221846327"));
        batchParameters.setArgs(args);
        new BOA20().run(batchParameters);
    }
}