package hr.vestigo.modules.collateral.batch.bo90;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo90.BO901.CollSubtypeIdsIterator;
import hr.vestigo.modules.collateral.batch.bo90.BO901.CollateralSubtypeIterator;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * CES izvješæe.
 * @author hrakis
 */
public class BO900 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo90/BO900.java,v 1.3 2014/11/19 12:11:18 hrakis Exp $";
    
    private BatchContext bc;
    private BO901 bo901;
    private YRXX0 yrxx0;
    
    private Date report_date;
    private ArrayList<String> register_nos;
    
    private BigDecimal col_pro_id;
    
    private HashMap<BigDecimal, String> subtypes;
    private HashMap<BigDecimal, BigDecimal> collateralSubtypes;
    
    private ArrayList<GroupData> groups;
    
    private Vector fileNames;
    private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheet;
    private int rowIndex;
    
    private final BigDecimal zero = new BigDecimal("0.00");
    private final BigDecimal hundred = new BigDecimal("100.00");

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.yrxx0 = new YRXX0(bc);
        this.bo901 = new BO901(bc, yrxx0);
        this.fileNames = new Vector();
        
        // evidentiranje eventa
        BigDecimal eve_id = insertIntoEvent();
 
        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvati ID obrade izraèuna pokrivenosti
        col_pro_id = bo901.selectColProId(report_date);
        if (col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.info("COL_PRO_ID = " + col_pro_id);
        
        // odredi za koje grupacije se radi CES izvještaj
        this.groups = new ArrayList<GroupData>();
        for (String register_no : register_nos)
        {
            CustomerData customer = bo901.selectCustomerData(register_no);
            GroupData group = bo901.selectCustomerGroup(customer);
            if (!isGroupAlreadyInList(group)) groups.add(group);
        }
        info("CES izvjestaj se radi za:");
        for (GroupData group : groups) info("   " + group);
        
        // dohvati sve moguæe podvrste kolaterala
        bc.debug("Podvrste kolaterala:");
        this.subtypes = new HashMap<BigDecimal, String>();
        CollateralSubtypeIterator iterSubtype = bo901.selectCollateralSubtypes();
        while (iterSubtype.next())
        {
            subtypes.put(iterSubtype.col_sub_id(), iterSubtype.name());
            bc.debug("   " + iterSubtype.col_sub_id() + " " + iterSubtype.name());
        }
        iterSubtype.close();
        
        // dohvati kolaterale i njihove podvrste
        this.collateralSubtypes = new HashMap<BigDecimal, BigDecimal>();
        CollSubtypeIdsIterator iterCollSubtypeIds = bo901.selectCollSubtypeIds();
        while (iterCollSubtypeIds.next())
        {
            collateralSubtypes.put(iterCollSubtypeIds.col_hea_id(), iterCollSubtypeIds.col_sub_id());
        }
        iterCollSubtypeIds.close();
        
        // za svaku grupaciju iz izvještaja dohvati sve potrebne podatke
        for (GroupData group : groups)
        {
            // dohvati sve komitente iz grupacije
            bo901.selectGroupCustomers(group);

            // dohvati sve kolaterale povezane na plasmane komitenata grupacije, te referente komitenata
            group.showLTV = true;
            for (CustomerData customer : group.customers)
            {
                bo901.selectCustomerCollaterals(report_date, col_pro_id, customer, group);
                bo901.selectCustomerRelationshipManager(customer, group);
                if (!"20".equalsIgnoreCase(customer.basel_cus_type)) group.showLTV = false; // ako barem jedan komitent iz grupacije nije Micro klijent (B2 Asset klasa 20) - ne prikazuje se LTV vrijednost
            }
            
            // priredi potrebne podatke za svaki kolateral
            for (CollateralData collateral : group.collaterals)
            {
                // dohvati sva pokrivanja u kojima sudjeluje kolateral
                bo901.selectCollateralCoverages(report_date, col_pro_id, collateral, group);
                
                // dohvati podatke o podvrsti kolaterala
                collateral.col_sub_id = collateralSubtypes.get(collateral.col_hea_id);
                collateral.col_sub_name = subtypes.get(collateral.col_sub_id);
                
                // ako je kolateral nekretnina, dohvati dodatne podatke o nekretnini (katastarska opæina)
                if (collateral.isRealEstate()) bo901.selectRealEstateData(collateral);
                
                // izraèunaj iznose za kolateral (iznos hipoteka višeg reda, iskorištenost kolaterala, LTV)
                calculateCollateralValues(collateral);
                
                // sortiraj plasmane koje pokriva kolateral
                Collections.sort(collateral.coverages);
            }
            
            // sortiraj kolaterale
            Collections.sort(group.collaterals);
        }

        // ispiši prikupljene podatke u log
        printDataToLog();

        // kreiraj zasebni izvještaj za svaku grupaciju
        for (GroupData group : groups)
        {
            try
            {
                // otvori izvještaj
                openReport(group);
                
                // zapiši podatke u izvještaj
                writeContent(group);
                
                // spremi generirani izvještaj
                saveReport(group);
            }
            finally
            {
                dispose();
            }
        }
        
        // slanje maila
        sendMail();
        
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    
    /**
     * Metoda provjerava da li je zadana grupacija veæ na listi grupacija za koje se radi izvještaj.
     * @param targetGroup objekt s podacima o grupaciji
     * @return true ako je grupacija veæ na listi, false inaèe
     */
    private boolean isGroupAlreadyInList(GroupData targetGroup)
    {
        if (targetGroup.cus_gro_id == null) return false;  // komitent koji nije dio grupacije ima cus_gro_id = null
        
        for (GroupData group : groups)
        {
            if (targetGroup.cus_gro_id.equals(group.cus_gro_id)) return true;
        }
        return false;
    }
    
    /**
     * Metoda provjerava da li je zadani komitent dio bilo koje grupacije za koje se radi izvještaj. 
     * @param cus_id ID komitenta
     * @return true ako je komitent dio neke grupacije, false inaèe
     */
    private boolean isCustomerInAnyGroup(BigDecimal cus_id)
    {
        for (GroupData group : groups)
        {
            if (group.getCustomer(cus_id) != null) return true;
        }
        return false;
    }
    
    /**
     * Metoda izraèunava iznose za zadani kolateral: iznos hipoteka višeg reda, iskorištenost kolaterala, LTV.
     * @param collateral objekt s podacima o kolateralu
     * @param ne vraæa ništa, puni polja u objektu s izraèunatim vrijednostima.
     */
    private void calculateCollateralValues(CollateralData collateral)
    {
        bc.startStopWatch("BO900.calculateCollateralValues");
        
        // izraèunaj iznos hipoteka višeg reda
        collateral.other_mortgage_amount = zero;  
        for (CoverageData coverage : collateral.coverages)
        {
            // ako komitent nije dio nijedne grupacije za koje se radi izvještaj, priboji u hipoteke višeg reda    
            if (!isCustomerInAnyGroup(coverage.cus_id))   
            {
                if (coverage.cus_id != null)  // ako je vlasnik plasmana komitent banke, u hipoteke višeg reda zbroji iznos kojim kolateral pokriva plasman 
                {
                    collateral.other_mortgage_amount = collateral.other_mortgage_amount.add(coverage.coverage_amount_eur);
                }
                else  // ako vlasnik plasmana nije komitent banke, u hipoteke višeg reda zbroji iznos hipoteke
                {
                    collateral.other_mortgage_amount = collateral.other_mortgage_amount.add(coverage.mortgage_amount_eur);
                }
            }
        }
        
        // izraèunaj iskorištenost kolaterala
        collateral.utilization_percentage = zero;
        collateral.utilization_amount_eur = zero;
        for (CoverageData coverage : collateral.coverages)
        {
            if (coverage.placement_owner != null && coverage.coverage_amount_eur != null)  // komitent je dio trenutne grupacije
            {
                collateral.utilization_amount_eur = collateral.utilization_amount_eur.add(coverage.coverage_amount_eur);
            }
        }
        if (!collateral.value_eur.equals(zero)) collateral.utilization_percentage = collateral.utilization_amount_eur.divide(collateral.value_eur, 20, RoundingMode.HALF_UP).multiply(hundred).setScale(2, RoundingMode.HALF_UP);
        
        // izraèunaj LTV
        collateral.ltv = zero;
        BigDecimal exposure_sum = zero;
        for (CoverageData coverage : collateral.coverages)
        {
            if (coverage.placement_owner != null && coverage.exposure_balance_eur != null)  // komitent je dio trenutne grupacije
            {
                exposure_sum = exposure_sum.add(coverage.exposure_balance_eur);
            }
        }
        if (!collateral.value_eur.equals(zero)) collateral.ltv = exposure_sum.divide(collateral.value_eur, 20, RoundingMode.HALF_UP).multiply(hundred).setScale(2, RoundingMode.HALF_UP);
        
        bc.stopStopWatch("BO900.calculateCollateralValues");
    }
    
    
    /**
     * Metoda ispisuje prikupljene podatke za izvještaj u log.
     */
    private void printDataToLog() throws Exception
    {
        bc.info("Podaci za CES:");
        for (GroupData group : groups)
        {
            bc.info("Grupacija: " + group + (group.showLTV ? " -> prikazuje se LTV" : ""));
            bc.info("   Komitenti grupacije:");
            for (CustomerData customer : group.customers) bc.info("      " + customer);
            bc.info("   Referenti:");
            for (String rsm : group.relationshipManagers) bc.info("      " + rsm);
            bc.info("   Kolaterali:");
            for (CollateralData collateral : group.collaterals)
            {
                bc.info("      " + collateral);
                bc.info("      Pokrivenost:");
                for (CoverageData coverage : collateral.coverages) bc.info("         " + coverage);
            }
        }
    }
    
    
    /**
     * Metoda otvara izvještaj za zadanu grupaciju, kreira sheet i u njemu zapisuje zaglavlje.
     * @param group
     */
    private void openReport(GroupData group)
    {
        bc.startStopWatch("BO900.openReport");
        
        // kreiraj workbook
        this.workbook = new SXSSFWorkbook(100);
        this.styles = ExcelStyleData.createStyles(workbook);
        
        // kreiraj sheet
        this.sheet = workbook.createSheet();
        ExcelUtils.setColumnWidths(sheet, new int[] { 110, 30, 55, 105, 85, 30, 100, 90, 75, 100, 80, 55, 200, 150, 80, 80, 150 });
        
        // podesi podruèje za ispis za prvi sheet
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);
        sheet.getPrintSetup().setLandscape(true);
        sheet.setMargin(Sheet.TopMargin, 0.59);
        sheet.setMargin(Sheet.LeftMargin, 0.59);
        sheet.setMargin(Sheet.BottomMargin, 0.59);
        sheet.setMargin(Sheet.RightMargin, 0.59);
        sheet.setColumnBreak(11);
       
        // zapiši zaglavlje
        rowIndex = 0;
        writeHeader(group);
        
        bc.stopStopWatch("BO900.openReport");
    }
    
    
    /**
     * Metoda sprema generirani izvještaj u izlaznu datoteku.
     */
    private void saveReport(GroupData group) throws Exception
    {
        bc.startStopWatch("BO900.saveReport");
        
        // složi ime izlazne datoteke
        String dir = bc.getOutDir() + "/";
        String dateString = new SimpleDateFormat("yyyyMMdd").format(report_date);
        String groupName = removeFileNameSpecialCharacters(group.name);
        String fileName = dir + groupName + "_" + dateString + ".xlsx";
        bc.info("Izlazna datoteka = " + fileName);
        
        // spremi izvješæe u izlaznu datoteku
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        info("Zapisano " + rowIndex + " redova u izvjesce za " + group.name + ".");
        
        // napravi marker datoteku
        new File(fileName + ".marker").createNewFile();
        
        // dodaj datoteku na listu za slanje
        fileNames.add(fileName);
        
        bc.stopStopWatch("BO900.saveReport");
    }
    

    /**
     * Metoda koja u zadanom Stringu traži specijalne znakove koji se ne smiju pojaviti u imenu datoteke i zamjenjuje ih s _ 
     * @param inputString ulazni String
     * @return novi String sa zamijenjenim znakovima 
     */
    private String removeFileNameSpecialCharacters(String inputString)
    {
        // specijalni znakovi za ime datoteke  \ / : * ? " < > |
        // specijalni znakovi za regularne izraze  \ . [ ] { } ( ) * + - ? ^ $ |
        // specijalni znakovi za Java String  \ ' "
        final String specialCharactersRegex = "[\\\\/:\\*\\?\"<>\\|]";  
        return inputString.replaceAll(specialCharactersRegex, "_");
    }
    
    
    /**
     * Metoda šalje generirano izvješæe naruèitelju na mail. 
     */
    private void sendMail() throws Exception
    {
        bc.startStopWatch("BO900.sendMail");
        YXY70.send(bc, "csvbo90", bc.getLogin(), fileNames);
        bc.stopStopWatch("BO900.sendMail");
    }
    
    
    
    /**
     * Metoda zapisuje analitièke podatke u trenutni sheet.
     * @param group objekt s podacima o grupaciji
     */
    private void writeContent(GroupData group)
    {
        bc.startStopWatch("BO900.writeContent");
        
        Row row = null;
        
        BigDecimal util_sum_1 = zero;  // suma iskorištenih iznosa kolaterala 
        BigDecimal util_sum_2 = zero;  // suma iskorištenih iznosa garancija
        
        // za svaki kolateral iz grupacije
        for (CollateralData collateral : group.collaterals)
        {
            row = null;
            int firstRowIndex = rowIndex;
            Row firstRow = sheet.createRow(rowIndex++);
            
            // zapiši zajednièke æelije za kolateral
            ExcelUtils.createCell(firstRow, 0, collateral.col_num, styles.normalStyle);
            ExcelUtils.createCell(firstRow, 1, collateral.real_est_nm_cur_code, styles.normalStyle);
            ExcelUtils.createCell(firstRow, 2, collateral.real_ponder, styles.normalNumericStyle);
            ExcelUtils.createCell(firstRow, 8, collateral.other_mortgage_amount, styles.normalNumericStyle);
            ExcelUtils.createCell(firstRow, 7, collateral.value_eur, styles.normalNumericStyle);
            ExcelUtils.createCell(firstRow, 11, group.showLTV ? collateral.ltv : (BigDecimal)null, styles.normalNumericStyle);
            ExcelUtils.createCell(firstRow, 12, collateral.col_typ_name + (collateral.col_sub_name == null ? "" : "; " + collateral.col_sub_name), styles.normalWrapStyle);
            ExcelUtils.createCell(firstRow, 13, collateral.cadastre_map_name, styles.normalWrapStyle);
            ExcelUtils.createCell(firstRow, 14, collateral.real_est_land_regn, styles.normalWrapStyle);
            ExcelUtils.createCell(firstRow, 15, collateral.real_est_land_sub, styles.normalWrapStyle);
            ExcelUtils.createCell(firstRow, 16, collateral.city, styles.normalWrapStyle);
            
            // za svaki plasman vezan na kolateral
            for (CoverageData coverage : collateral.coverages)
            {
                if (coverage.placement_owner != null)  // ako je komitent iz trenutne grupacije, zapiši analitièki redak
                {
                    if (row == null) row = firstRow; else row = sheet.createRow(rowIndex++);  // prvi redak preuzmi iz zajednièkih podataka, dalje stvaraj sam
                    ExcelUtils.createCell(row, 3, coverage.placement_owner.name, styles.normalSmallStyle);
                    ExcelUtils.createCell(row, 4, coverage.cus_acc_no, styles.normalSmallStyle);
                    ExcelUtils.createCell(row, 5, coverage.exposure_balance_cur_code, styles.normalStyle);
                    ExcelUtils.createCell(row, 6, coverage.exposure_balance_eur, styles.normalNumericStyle);
                    ExcelUtils.createCell(row, 9, collateral.isGuarantee() ? (BigDecimal)null : coverage.coverage_amount_eur, styles.normalNumericStyle);
                    ExcelUtils.createCell(row, 10, collateral.isGuarantee() ? coverage.coverage_amount_eur : (BigDecimal)null, styles.normalNumericStyle);
                    if (collateral.isGuarantee()) util_sum_2 = util_sum_2.add(coverage.coverage_amount_eur); else util_sum_1 = util_sum_1.add(coverage.coverage_amount_eur);
                    
                    // potrebno definirati stil za sve ostale zajednièke æelije tako da ga merge može primijeniti na sve æelije   
                    if (row != firstRow)
                    {
                        ExcelUtils.createCell(row, 0, "", styles.normalStyle);
                        ExcelUtils.createCell(row, 1, "", styles.normalStyle);
                        ExcelUtils.createCell(row, 2, (BigDecimal)null, styles.normalNumericStyle);
                        ExcelUtils.createCell(row, 8, (BigDecimal)null, styles.normalNumericStyle);
                        ExcelUtils.createCell(row, 7, (BigDecimal)null, styles.normalNumericStyle);
                        ExcelUtils.createCell(row, 11, "", styles.normalNumericStyle);
                        ExcelUtils.createCell(row, 12, "", styles.normalWrapStyle);
                        ExcelUtils.createCell(row, 13, "", styles.normalWrapStyle);
                        ExcelUtils.createCell(row, 14, "", styles.normalWrapStyle);
                        ExcelUtils.createCell(row, 15, "", styles.normalWrapStyle);
                        ExcelUtils.createCell(row, 16, "", styles.normalWrapStyle);
                    }
                }
            }
            
            // proširi zajednièke æelije
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 7, 7));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 8, 8));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 11, 11));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 12, 12));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 13, 13));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 14, 14));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 15, 15));
            sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, rowIndex - 1, 16, 16));
        }
        
        // zapiši sumarni redak
        rowIndex++;
        row = sheet.createRow(rowIndex++);
        ExcelUtils.createCell(row, 7, "Ukupno:", styles.normalStyle);
        ExcelUtils.createCell(row, 9, util_sum_1, styles.normalNumericStyle);
        ExcelUtils.createCell(row, 10, util_sum_2, styles.normalNumericStyle);
        
        bc.stopStopWatch("BO900.writeContent");
    }
    
    
    /**
     * Metoda zapisuje zaglavlje u trenutni sheet.
     * @param group objekt s podacima o grupaciji
     */
    private void writeHeader(GroupData group)
    {
        final String delimiter = ", ";
        
        String customer_codes = "";
        String customer_names = "";
        for (CustomerData customer : group.customers)
        {
            customer_codes += customer.register_no + delimiter;
            customer_names += customer.name + " (IM " + customer.register_no + ")" + delimiter;
        }
        
        String rsm_names = "";
        for (String rsm_name : group.relationshipManagers)
        {
            rsm_names += rsm_name + delimiter;
        }
        
        if (customer_codes.length() >= delimiter.length()) customer_codes = customer_codes.substring(0, customer_codes.length() - delimiter.length());
        if (customer_names.length() >= delimiter.length()) customer_names = customer_names.substring(0, customer_names.length() - delimiter.length());
        if (rsm_names.length() >= delimiter.length()) rsm_names = rsm_names.substring(0, rsm_names.length() - delimiter.length());
        
        Row row;
        rowIndex = 0;
        
        row = sheet.createRow(rowIndex++);
        ExcelUtils.createCell(row, 0, "Datum:", styles.titleStyle);
        ExcelUtils.createCell(row, 3, report_date, styles.titleDateStyle);
        
        row = sheet.createRow(rowIndex++);
        ExcelUtils.createCell(row, 0, "IM komitenta:", styles.titleStyle);
        ExcelUtils.createCell(row, 2, customer_codes, styles.titleWrapStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 2, 11));
        
        row = sheet.createRow(rowIndex++);
        ExcelUtils.createCell(row, 0, "Naziv komitenta (IM):", styles.titleStyle);
        ExcelUtils.createCell(row, 2, customer_names, styles.titleWrapStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 2, 11));
        
        row = sheet.createRow(rowIndex++);
        ExcelUtils.createCell(row, 0, "Grupacija (\u0161ifra):", styles.titleStyle);
        ExcelUtils.createCell(row, 2, group.name + " (" + group.code + ")", styles.titleStyle);
        
        row = sheet.createRow(rowIndex++);
        ExcelUtils.createCell(row, 0, "Referent:", styles.titleStyle);
        ExcelUtils.createCell(row, 2, rsm_names, styles.titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 2, 11));

        rowIndex++;
        
        row = sheet.createRow(rowIndex++);
        row.setHeightInPoints(45);
        int columnIndex = 0;
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Val. kol.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Diskont", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vlasnik plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Partija plasmana", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Val. pla.", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Izlo\u017Eenost plasmana, EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Tr\u017Ei\u0161na vrij. kol., EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Hipoteke vi\u0161eg reda, EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Iskor. kol., EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Iskor. gar., EUR", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "LTV (%)", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Podvrsta kolaterala", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Kat. op\u0107ina", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "ZK ulo\u017Eak", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Podulo\u017Eak", styles.headingStyle);
        ExcelUtils.createCell(row, columnIndex++, "Mjesto", styles.headingStyle);
    }

    
    /**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose()
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
            bc.startStopWatch("BO900.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);
            
            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("6534455704"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "CES izvjesce");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO900.insertIntoEvent");
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
     *    <dt>report_date</dt><dd>Datum izvjestaja.</dd>
     *    <dt>register_no_1</dt><dd>Interni MB komitenta 1.</dd>
     *    <dt>register_no_2</dt><dd>Interni MB komitenta 2. (opcionalan parametar)</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke", "Datum izvjestaja", "Interni MB komitenta 1", "Interni MB komitenta 2" };

            // ispiši parametre predane obradi
            info("Parametri predani obradi:");
            for (int i = 0; i < bc.getArgs().length; i++)
            {
                String parameterName = (i < bc.getArgs().length) ? parameterNames[i] : "Nepoznati parametar";
                String parameterValue = bc.getArg(i);
                info("   " + parameterName + " = " + parameterValue);
            }

            // provjeri da li je broj parametara ispravan
            if (bc.getArgs().length < parameterNames.length - 1 || bc.getArgs().length > parameterNames.length)
            {
                error("Neispravan broj parametara - predani broj parametara je " + bc.getArgs().length + ", a obrada prima " + (parameterNames.length - 1) + " ili " + parameterNames.length + "!", null);  
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
            
            // datum izvještaja
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            report_date = new Date(dateFormat.parse(bc.getArg(1).trim()).getTime());
            
            // komitent 1
            register_nos = new ArrayList<String>();
            register_nos.add(bc.getArg(2).trim());
            
            // komitent 2
            if (bc.getArgs().length > 3 && !bc.getArg(3).trim().equalsIgnoreCase("X")) register_nos.add(bc.getArg(3).trim());
            
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
        BatchParameters bp = new BatchParameters(new BigDecimal("6534454704"));
        bp.setArgs(args);
        new BO900().run(bp);
    }
}