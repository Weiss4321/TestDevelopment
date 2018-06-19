package hr.vestigo.modules.collateral.batch.bo78;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo78.BO781.CollateralIterator;
import hr.vestigo.modules.collateral.batch.bo78.BO781.InsurancePolicyIterator;
import hr.vestigo.modules.collateral.batch.bo78.BO781.PlacementIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.common.yrxX.YRXX0;
import hr.vestigo.modules.rba.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Izvještaj o omjeru NGV i osigurane svote.
 * @author hrakis
 */
public class BO780 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo78/BO780.java,v 1.2 2013/12/20 08:06:13 hrakis Exp $";
    
    private BatchContext bc;
    private BO781 bo781;
    private YRXX0 yrxx0;
    
    private Date value_date;

    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheet[] = new Sheet[2];
    private int rowIndex[] = new int[2];
    
    private String fileName;
    
    private final BigDecimal eur_cur_id = new BigDecimal("64999");
    private final BigDecimal zero = new BigDecimal("0.00"); 

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo781 = new BO781(bc);
        this.yrxx0 = new YRXX0(bc);
        this.value_date = new Date(bc.getExecStartTime().getTime()); 
        
        // evidentiranje eventa
        insertIntoEvent();
 
        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat kolaterala
        CollateralIterator iter = bo781.selectCollaterals();
        
        try
        {
            // kreiraj workbook
            this.workbook = new SXSSFWorkbook(100);
            this.styles = ExcelStyleData.createStyles(workbook);
    
            // kreiraj sheetove
            createSheet(0, "PI - Private individuals");
            createSheet(1, "CO - Corporate");
            
            // zapisivanje podataka u izvještaj
            while (iter.next())
            {
                CollateralData collateral = new CollateralData();
                collateral.col_hea_id = iter.col_hea_id();
                collateral.col_num = iter.col_num().trim();
                collateral.ngv = yrxx0.exchange(iter.new_build_val(), iter.real_est_nm_cur_id(), eur_cur_id, value_date);
                
                bc.debug("KOLATERAL " + collateral.col_num);
                bc.debug("   NGV = " + collateral.ngv + " EUR");
                
                // suma osiguranih svota po svim aktivnim policama kolaterala
                bc.debug("   police:");
                collateral.insured_amount_sum = zero;
                InsurancePolicyIterator iterInsPol = bo781.selectInsurancePolicies(iter.col_hea_id());
                while (iterInsPol.next())
                {
                    BigDecimal insured_amount = yrxx0.exchange(iterInsPol.ip_secu_val(), iterInsPol.ip_cur_id(), eur_cur_id, value_date);
                    collateral.insured_amount_sum = collateral.insured_amount_sum.add(insured_amount);
                    bc.debug("      " + iterInsPol.ip_code() + ": " + insured_amount + " EUR");
                }
                iterInsPol.close();
                bc.debug("   = insured amount sum = " + collateral.insured_amount_sum + " EUR");
                
                // omjer NGV / suma osiguranih svota
                collateral.ratio = null;
                if (collateral.insured_amount_sum.compareTo(zero) > 0) collateral.ratio = collateral.ngv.divide(collateral.insured_amount_sum, 2, RoundingMode.HALF_UP);
                bc.debug("   = ratio = " + collateral.ratio);
                
                // suma izloženosti svih aktivnih plasmana povezanih na kolateral
                bc.debug("   plasmani:");
                collateral.placement_exposure_sum = zero;
                PlacementIterator iterPlacement = bo781.selectPlacements(iter.col_hea_id());
                while (iterPlacement.next())
                {
                    BigDecimal placement_exposure = yrxx0.exchange(iterPlacement.exposure_balance(), iterPlacement.exposure_cur_id(), eur_cur_id, value_date);
                    collateral.placement_exposure_sum = collateral.placement_exposure_sum.add(placement_exposure);
                    if (collateral.placement_owner_cus_typ_id == null || (isPrivateIndividual(collateral.placement_owner_cus_typ_id) && !isPrivateIndividual(iterPlacement.cus_typ_id()) ) )
                    {
                        collateral.placement_owner_register_no = iterPlacement.register_no().trim();
                        collateral.placement_owner_name = iterPlacement.name();
                        collateral.placement_owner_rsm_name = iterPlacement.referent();
                        if (collateral.placement_owner_rsm_name != null) collateral.placement_owner_rsm_name = collateral.placement_owner_rsm_name.trim();
                        collateral.placement_owner_cus_typ_id = iterPlacement.cus_typ_id();
                    }
                    bc.debug("      " + iterPlacement.cus_acc_no() + ": " + placement_exposure + " EUR, " + iterPlacement.register_no() + " " + iterPlacement.name() + " " + iterPlacement.cus_typ_id() + " " + iterPlacement.referent());
                }
                iterPlacement.close();
                bc.debug("   = placement exposure sum = " + collateral.placement_exposure_sum + " EUR");
                
                // zapiši red u izvještaj
                writeRow(collateral);
            }
    
            // spremi izvještaj
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
            fileName = bc.getOutDir() + "/" + dateFormat.format(value_date) + "_Omjer_NGV_i_osig_svote" + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
        }
        finally
        {
            dispose();
            if (iter != null) iter.close();
        }
        
        info("Zapisano " + rowIndex[0] + " redova u prvi sheet i " + rowIndex[1] + " redova u drugi sheet.");
        
        // slanje maila
        YXY70.send(bc, "csv258", bc.getLogin(), fileName);

        // kreiraj marker datoteku
        new File(fileName + ".marker").createNewFile();
      
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda koja zapisuje redak s podacima u izvještaj.
     * @param collateral objekt s podacima o kolateralu
     */
    private void writeRow(CollateralData collateral)
    {
        int sheetIndex = isPrivateIndividual(collateral.placement_owner_cus_typ_id) ? 0 : 1;
        Row row = sheet[sheetIndex].createRow(rowIndex[sheetIndex]);
        ExcelUtils.createCell(row, 0, collateral.col_num, styles.normalStyle);
        ExcelUtils.createCell(row, 1, collateral.ngv, styles.normalNumericStyle);
        ExcelUtils.createCell(row, 2, collateral.insured_amount_sum, styles.normalNumericStyle);
        ExcelUtils.createCell(row, 3, collateral.ratio, styles.normalNumericStyle);
        ExcelUtils.createCell(row, 4, collateral.placement_exposure_sum, styles.normalNumericStyle);
        ExcelUtils.createCell(row, 5, collateral.placement_owner_register_no, styles.normalStyle);
        ExcelUtils.createCell(row, 6, collateral.placement_owner_name, styles.normalStyle);
        ExcelUtils.createCell(row, 7, collateral.placement_owner_rsm_name, styles.normalStyle);
        rowIndex[sheetIndex]++;
    }
    
    
    /**
     * Metoda ispituje da li je zadani ID tipa komitenta fizièka osoba.   
     * @param cus_typ_id ID tipa komitenta
     */
    private boolean isPrivateIndividual(BigDecimal cus_typ_id)
    {
        return new BigDecimal("1999").equals(cus_typ_id) || new BigDecimal("1998").equals(cus_typ_id);
    }
    

    /**
     * Metoda kreira sheet za izvještaj.
     * @param sheetIndex redni broj sheeta u izvještaju
     * @param title naslov sheeta
     */
    private void createSheet(int sheetIndex, String title)
    {
        Sheet sheet = workbook.createSheet(title);
        this.sheet[sheetIndex] = sheet;
        ExcelUtils.setColumnWidths(sheet, new int[] { 125, 120, 135, 110, 120, 80, 300, 200 });
        Row row = sheet.createRow(0);
        row.setHeightInPoints(39);
        ExcelUtils.createCell(row, 0, "\u0160ifra kolaterala", styles.headingWrapStyle);
        ExcelUtils.createCell(row, 1, "NGV (EUR)", styles.headingWrapStyle);
        ExcelUtils.createCell(row, 2, "Suma osig. svota po svim policama (EUR)", styles.headingWrapStyle);
        ExcelUtils.createCell(row, 3, "Omjer NGV/osig. svota", styles.headingWrapStyle);
        ExcelUtils.createCell(row, 4, "Izlo\u017Eenost svih plasmana (EUR)", styles.headingWrapStyle);
        ExcelUtils.createCell(row, 5, "IM vla. pla.", styles.headingWrapStyle);
        ExcelUtils.createCell(row, 6, "Naziv vlasnika plasmana", styles.headingWrapStyle);
        ExcelUtils.createCell(row, 7, "Referent", styles.headingWrapStyle);
        sheet.createFreezePane(0, 1);
        this.rowIndex[sheetIndex] = 1;
    }
    
    /**
     * Metoda koja briše privremene datoteke koje se stvaraju za vrijeme kreiranja izvještaja.
     */
    public void dispose()
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }
    
    
    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    public BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6493777704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Izvjestaj o omjeru NGV i osigurane svote");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("insertIntoEvent");
            return eve_id;
        }
        catch (Exception ex)
        {
            bc.error("Dogodila se nepredvidjena greska pri evidentiranju izvodjenja obrade u tablicu EVENT!", ex);
            throw ex;
        }
    }
        
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke" };

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
            bc.setBankSign(bank_sign);
            
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
        BatchParameters bp = new BatchParameters(new BigDecimal("6493776704"));
        bp.setArgs(args);
        new BO780().run(bp);
    }
}