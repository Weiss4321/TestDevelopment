package hr.vestigo.modules.collateral.batch.bo44;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo44.BO441.CollateralPolicyIterator;
import hr.vestigo.modules.collateral.batch.bo44.BO441.PolicyIterator;
import hr.vestigo.modules.collateral.batch.bo44.BO441.PolicyIteratorShort;
import hr.vestigo.modules.collateral.batch.bo87.DefPonderData;
import hr.vestigo.modules.collateral.batch.bo87.ExcelStyleData;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.ExcelUtils;
import hr.vestigo.modules.rba.util.excel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Izvješæe o policama.
 * @author hrakis
 */
public class BO440 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo44/BO440.java,v 1.17 2016/09/02 10:40:44 hrakis Exp $";
	private BO441 bo441;
	private BigDecimal org_uni_id;
	private String client_type;
	/** Da li je skraæena verzija obrade
	 * 0 - normalna verzija obrade
	 * 1 - skraæena verzija obrade
	 */
	private int batchType = 0;
	private Date value_date;
	
	private Workbook workbook;
    private ExcelStyleData styles;
    private Sheet sheetPolice;
    private int rowIndexPolice = 0;
    

	
	public String executeBatch(BatchContext bc) throws Exception
	{
		bc.debug("BO440 pokrenut.");
		bo441 = new BO441(bc);
		
        // insertiranje eventa
		BigDecimal eve_id = bo441.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.");
		
		if (batchType == 0)
		{
		    return NormalVersion(bc);
		}
		else
		{
		    return ShortVersion(bc);
		}
		
    }
	
	private String ShortVersion(BatchContext bc) throws Exception
    {
	    // dohvat podataka o policama
        PolicyIteratorShort iter = bo441.selectPoliciesShort(org_uni_id, client_type);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci o policama.");        
        String nazivDatoteke = "";

        if (client_type.equals("P"))
        {
            nazivDatoteke = "Police_Co_RBA_";
        }
        else if(client_type.equals("F"))
        {
            nazivDatoteke = "Police_Ret_RBA_";
        }
        
        String dateString = new SimpleDateFormat("yyyyMMdd").format(bc.getExecStartTime());
        String dir = bc.getOutDir() + "/";
        String fileName = dir + nazivDatoteke + dateString + ".xlsx";

        openReport();        
        writeHeaders();
        
        // zapisivanje podataka u prvi sheet
        String col_num = null;
        BigDecimal ip_id = null;
        //Za provjeru da li je veæ zapisan
        CollData tempData = null;
        rowIndexPolice = 1;
        while(iter.next())
        {
            if(!iter.dwh_status().equals("C") || isSSP(iter.module_code(), iter.status_u_modulu()))  // uzmi samo aktivne plasmane i one koji su na SSP
            {   
                if(!iter.col_num().equals(col_num) && !iter.ip_id().equals(ip_id))  // ako su se promijenili i col_num i ip_id, znaèi da smo na novom kolateralu
                {
                    col_num = iter.col_num();
                    ip_id = iter.ip_id();
                }
                if(iter.col_num().equals(col_num) && iter.ip_id().equals(ip_id))  // u prvi sheet dodaj samo zadnju važeæu policu kolaterala 
                {
                    CollData data = new CollData();
                    fillDataFromIterator(data, iter);
                    if(!ISEqualCollData(data, tempData))
                    {
                        bo441.selectAppUser(data);
                        writePoliceRow(data);
                        tempData = data;
                    }
                }
            }
        }
        bc.debug("Zapisani svi redovi u prvi sheet.");  
        //Spremi datoteku
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        dispose();        
        new File(fileName + ".marker").createNewFile();
        // slanje maila
        bc.startStopWatch("BO440.sendMail");
        YXY70.send(bc, "csv186" + client_type, bc.getLogin(), fileName);
        bc.stopStopWatch("BO440.sendMail");
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
	
	/**
     * Metoda usporeðuje dva CollData objekta.
     */
	private boolean ISEqualCollData(CollData data1, CollData data2)
	{
	    if(data1 != null && data2 != null)
	    {
	        if(data1.col_num != null && data2.col_num != null)
	        {
	            if(!data1.col_num.equals(data2.col_num))
	                return false;
	        }
	        if(data1.ip_code != null && data2.ip_code != null)
            {
                if(!data1.ip_code.equals(data2.ip_code))
                    return false;
            }
	        if(data1.ic_name != null && data2.ic_name != null)
            {
                if(!data1.ic_name.equals(data2.ic_name))
                    return false;
            }
	        if(data1.valid_until != null && data2.valid_until != null)
            {
                if(!data1.valid_until.equals(data2.valid_until))
                    return false;
            }
	        if(data1.paid_from != null && data2.paid_from != null)
            {
                if(!data1.paid_from.equals(data2.paid_from))
                    return false;
            }
	        if(data1.paid_until != null && data2.paid_until != null)
            {
                if(!data1.paid_until.equals(data2.paid_until))
                    return false;
            }
	        if(data1.osiguranik != null && data2.osiguranik != null)
            {
                if(!data1.osiguranik.equals(data2.osiguranik))
                    return false;
            }
	        if(data1.valuta != null && data2.valuta != null)
            {
                if(!data1.valuta.equals(data2.valuta))
                    return false;
            }
	        
	        if(data1.osigurana_svota != null && data2.osigurana_svota != null)
            {
                if(!data1.osigurana_svota.equals(data2.osigurana_svota))
                    return false;
            }
	        if(data1.status_police != null && data2.status_police != null)
            {
                if(!data1.status_police.equals(data2.status_police))
                    return false;
            }
	        if(data1.ip_spec_stat != null && data2.ip_spec_stat != null)
            {
                if(!data1.ip_spec_stat.equals(data2.ip_spec_stat))
                    return false;
            }
	        if(data1.spec_stat != null && data2.spec_stat != null)
            {
                if(!data1.spec_stat.equals(data2.spec_stat))
                    return false;
            }
	        
	        return true;
	        
	    }
	    else if (data1 != null && data2 == null)
	    {
	        return false;
	    }
	    else if (data1 == null && data2 != null)
        {
            return false;
        }
	    return true;
	}
	
	/**
     * Metoda èisti privremeno korištene resurse za generiranje izvještaja.
     */
    private void dispose()
    {
        if (workbook instanceof SXSSFWorkbook) ((SXSSFWorkbook)workbook).dispose();
    }
	
	/**
     * Metoda otvara izvještaj i kreira sheetove u koje æe biti zapisani podaci (Skraæeni izvještaj). 
     */
    private void openReport()
    {
        // kreiraj workbook
        this.workbook = new SXSSFWorkbook(100);
        this.styles = ExcelStyleData.createStyles(workbook);
        
        // kreiraj sheet
        this.sheetPolice = workbook.createSheet("Police");
        ExcelUtils.setColumnWidths(sheetPolice, new int[] { 125, 193, 300, 80, 80, 80, 85, 300, 47, 97, 45, 45, 106, 85, 200, 65, 200 });
    }
    
        
    /**
     * Metoda zapisuje zaglavlje u Police sheet. (Skraæeni izvještaj)
     */
    private void writeHeaders()
    {
        writeBonusHeader();
    }
    
    /**
     * Metoda zapisuje zaglavlje u Police sheet. (Skraæeni izvještaj)
     */
    private void writeBonusHeader()
    {
        Row row = sheetPolice.createRow(0);
        row.setHeightInPoints(50);
        
        int columnIndex = 0;
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra kolaterala", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra police", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Osiguravatelj", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Vrijedi do", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Pla\u0107ena od", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Pla\u0107ena do", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "ID osiguranika", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Osiguranik", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Valuta", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Osigurana svota", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status police", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Status napomene", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Napomena o polici", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Login referenta", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Ime referenta", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "\u0160ifra OJ referenta", styles.headingWrapStyle);
        ExcelUtils.createCell(row, columnIndex++, "Naziv OJ referenta", styles.headingWrapStyle);
        
        sheetPolice.createFreezePane(0, 1);
    }
    
    /**
     * Metoda puni data objekt s podacima iz iteratora. (Skraæeni izvještaj)
     * @param data data objekt
     * @param iter iterator s podacima
     */
    private void fillDataFromIterator(CollData data, PolicyIteratorShort iter) throws Exception
    {
        //bc.debug("col_num: " + iter.col_num());
        data.col_num = iter.col_num();                      // Šifra kolaterala
        data.ip_code = iter.ip_code();                      // Šifra police
        data.ic_name = iter.ic_name();                      // Osiguravatelj
        data.valid_until = iter.valid_until();              // Vrijedi do
        data.paid_from = iter.paid_from();                  // Plaæena od
        data.paid_until = iter.paid_until();                // Plaæena do
        data.id_osiguranika = iter.id_osiguranika();        // ID osiguranika
        data.osiguranik = iter.osiguranik();                // Osiguranik
        data.valuta = iter.valuta();                        // Valuta
        data.osigurana_svota = iter.osigurana_svota();      // Osigurana svota
        data.status_police = iter.status_police();          // Status police
        data.ip_spec_stat = iter.ip_spec_stat();            // Status napomene
        data.spec_stat = iter.spec_stat();                  // Napomena o polici
        data.use_id = iter.use_id();                        // Referent
    }
    
    /**
     * Metoda zapisuje redak s podacima u police sheet. (Skraæeni izvještaj)
     * @param data objekt s podacima
     */
    private void writePoliceRow(CollData data)
    {
        
        Row row = sheetPolice.createRow(rowIndexPolice);
        int columnIndex = 0;
        
        ExcelUtils.createCell(row, columnIndex++, data.col_num, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.ip_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.ic_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.valid_until, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.paid_from, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.paid_until, styles.normalDateStyle);
        ExcelUtils.createCell(row, columnIndex++, data.id_osiguranika, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.osiguranik, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.valuta, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.osigurana_svota, styles.normalNumericStyle);
        ExcelUtils.createCell(row, columnIndex++, data.status_police, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.ip_spec_stat, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.spec_stat, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.referent_login, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.referent_name, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.referent_org_code, styles.normalStyle);
        ExcelUtils.createCell(row, columnIndex++, data.referent_org_name, styles.normalStyle);
        
        rowIndexPolice++;
    }
		
	
	private String NormalVersion(BatchContext bc) throws Exception
	{
	    // dohvat podataka o policama
        PolicyIterator iter = bo441.selectPolicies(org_uni_id, client_type);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci o policama.");
        
        // dohvat podataka o policama koje su kolaterali
        CollateralPolicyIterator iter2 = bo441.selectCollateralPolicies();
        if(iter2 == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci o policama koje su kolaterali.");
        
        // dohvat podataka o policama koje su NEAKTIVNI kolaterali
        CollateralPolicyIterator iter3 = bo441.selectInactiveCollateralPolicies();
        if(iter3 == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci o policama koje su NEAKTIVNI kolaterali.");
        
        String dateString = new SimpleDateFormat("yyyyMMdd").format(bc.getExecStartTime());
        String dir = bc.getOutDir() + "/";
        String fileName = dir + "Police_za_back_office_" + dateString;

        // stvaranje workbooka
        hr.vestigo.modules.rba.util.excel.Workbook workbook = new hr.vestigo.modules.rba.util.excel.Workbook(fileName, hr.vestigo.modules.rba.util.excel.Workbook.FileType.ExcelML, true, 100);
        workbook.addStyle(new Style("Default", Alignment.Bottom, new Font("Arial", 9)));
        workbook.addStyle(new Style("s1", Alignment.BottomLeft, Borders.All, new Font("Arial", 9, true), new Interior("#FFFF00"), null));
        workbook.addStyle(new Style("s2", null, null, null, null, "Standard"));
        workbook.addStyle(new Style("s3", null, null, null, null, "0"));
        workbook.addStyle(new Style("s4", null, null, null, null, "Short Date"));
        workbook.open();
        bc.debug("Otvoren workbook.");
        
        // stvaranje sheetova
        Worksheet sheetPolicyLast = workbook.addWorksheet(new Worksheet("Police imovine - zadnja", dir + "Police_zadnja_" + dateString));
        Worksheet sheetPolicyAll = workbook.addWorksheet(new Worksheet("Police imovine - sve", dir + "Police_sve_" + dateString));
        Worksheet sheetPolicyColl = workbook.addWorksheet(new Worksheet("Police kao kolateral", dir + "Police_kolateral_" + dateString));
        Worksheet sheetInactiveColl = workbook.addWorksheet(new Worksheet("Lista neaktivnih kolaterala", dir + "Lista_neaktivnih_kolaterala_" + dateString));    
        sheetPolicyLast.setDefaultRowHeight(12);
        sheetPolicyAll.setDefaultRowHeight(12);
        sheetPolicyColl.setDefaultRowHeight(12);
        sheetInactiveColl.setDefaultRowHeight(12);    
        bc.debug("Stvoreni sheetovi.");

        // definiranje kolona
        sheetPolicyLast.addColumns(getColumns(1));
        sheetPolicyAll.addColumns(getColumns(2));
        sheetPolicyColl.addColumns(getColumns(3));
        sheetInactiveColl.addColumns(getColumns(3));
        bc.debug("Definirane kolone.");

        // otvaranje sheetova
        workbook.openAllWorksheets();
        bc.debug("Otvoreni sheetovi.");
        
        // formiranje zaglavlja na sheetovima
        sheetPolicyLast.addRow(getHeaderRow(1));
        sheetPolicyAll.addRow(getHeaderRow(2));
        sheetPolicyColl.addRow(getHeaderRow(3));
        sheetInactiveColl.addRow(getHeaderRow(3));
        bc.debug("Dodana zaglavlja.");
    
        // zapisivanje podataka u prva dva sheeta
        String col_num = null;
        BigDecimal ip_id = null;
        while(iter.next())
        {
            if(!iter.dwh_status().equals("C") || isSSP(iter.module_code(), iter.status_u_modulu()))  // uzmi samo aktivne plasmane i one koji su na SSP
            {
                hr.vestigo.modules.rba.util.excel.Row row = getDetailsRow(iter);
                sheetPolicyAll.addRow(row);  // u drugi sheet dodaj sve aktivne kolaterale i sve njihove police
                if(!iter.col_num().equals(col_num) && !iter.ip_id().equals(ip_id))  // ako su se promijenili i col_num i ip_id, znaèi da smo na novom kolateralu
                {
                    col_num = iter.col_num();
                    ip_id = iter.ip_id();
                }
                if(iter.col_num().equals(col_num) && iter.ip_id().equals(ip_id))  // u prvi sheet dodaj samo zadnju važeæu policu kolaterala 
                {
                    sheetPolicyLast.addRow(row);
                }
            }
        }
        bc.debug("Zapisani svi redovi u prva dva sheeta.");
        
        //zapisivanje podataka u treæi sheet
        CollData coll = null;
        while(iter2.next())
        {
            if(coll != null && coll.col_group != null && !iter2.col_num().equals(coll.col_num))
            {
                sheetPolicyColl.addRow(getDetailsRow(coll));
                coll = null;
            }
            coll = getCollDataFromIterator(coll, iter2);
        }
        // zapiši zadnji kolateral u treæi sheet
        if(coll != null && coll.col_group != null) sheetPolicyColl.addRow(getDetailsRow(coll));
        bc.debug("Zapisani svi redovi u treci sheet.");
        
        // FBPr200013234 - dopuna, dodan 4. sheet u kojem se nalaze neaktivni kolaterali
        // zapisivanje podataka u èetvrti sheet
        CollData coll1 = null;
        while(iter3.next())
        {
            if(coll1 != null && coll1.col_group != null && !iter3.col_num().equals(coll1.col_num))
            {
                sheetInactiveColl.addRow(getDetailsRow(coll1));
                coll1 = null;
            }
            coll1 = getCollDataFromIterator(coll1, iter3);
        }
        // zapiši zadnji kolateral u èetvrti sheet
        if(coll1 != null && coll1.col_group != null) sheetInactiveColl.addRow(getDetailsRow(coll1));
        bc.debug("Zapisani svi redovi u èetvrti sheet.");
   
        workbook.closeAllWorksheets();
        workbook.close();
        bc.debug("Zatvoren workbook.");

        // slanje maila
        YXY70.send(bc, "csv186", bc.getLogin(), fileName + ".xml");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
	}
    
    
    /**
     * Metoda koja stvara ili ažurira data objekt s podacima iz iteratora.
     * @param coll Data objekt
     * @param iter Iterator
     * @return data objekt
     */
    private CollData getCollDataFromIterator(CollData coll, CollateralPolicyIterator iter) throws Exception
    {
        if(coll == null)
        {
            coll = new CollData();
            coll.ip_id = iter.ip_id();
            coll.b2asset_class = iter.b2asset_class();
            coll.oj_code = iter.oj_code();
            coll.oj_name = iter.oj_name();
            coll.col_num = iter.col_num();
            coll.collateral_status = iter.collateral_status(); 
            coll.ip_code = iter.ip_code();
            coll.ic_name = iter.ic_name(); 
            coll.osig_reg_no = iter.osig_reg_no();
            coll.osiguranik = iter.osiguranik();
            coll.valid_until = iter.valid_until();
            coll.paid_from = iter.paid_from();
            coll.paid_until = iter.paid_until();
            coll.valuta = iter.valuta();
            coll.osigurana_svota = iter.osigurana_svota();
            coll.akum_amount = iter.akum_amount();
            coll.status_police = iter.status_police();
            coll.ip_spec_stat = iter.ip_spec_stat();
            coll.spec_stat = iter.spec_stat();
            coll.id_ugovaratelja = iter.id_ugovaratelja();
            coll.ugovaratelj = iter.ugovaratelj();
            coll.id_korisnika = iter.id_korisnika();
            coll.korisnik = iter.korisnik();
            coll.plasman = iter.plasman();
            coll.dwh_status = iter.dwh_status(); 
            coll.status_u_modulu = iter.status_u_modulu();
            coll.col_group = getColGroup(iter);
        }
        else
        {
            String col_group = getColGroup(iter);
            if(col_group != null)
            {
                if( coll.col_group == null || (col_group.equals("Corporate") && !coll.col_group.equals("Corporate")) || (col_group.equals("Micro")) && coll.col_group.equals("Retail") )
                {
                    coll.id_korisnika = iter.id_korisnika();
                    coll.korisnik = iter.korisnik();
                    coll.plasman = iter.plasman();
                    coll.dwh_status = iter.dwh_status();
                    coll.status_u_modulu = iter.status_u_modulu();
                    coll.b2asset_class = iter.b2asset_class();
                    coll.col_group = col_group;
                }
            }
        }
        return coll;
    }
    
    /**
     * Metoda koja odreðuje pripadnost kolaterala na temelju b2 asset klase.
     * @param iter Iterator
     * @return "Retail","Micro" ili "Corporate". Null ako plasman ne ulazi u odreðivanje pripadnosti kolaterala.
     */
    public String getColGroup(CollateralPolicyIterator iter) throws Exception
    {
        if(!iter.dwh_status().equals("C") || isSSP(iter.module_code(), iter.status_u_modulu())) 
        {
            String b2_asset = iter.b2asset_class().trim();
            if(b2_asset.equals("1") || b2_asset.equals("36")) return "Retail";
            else if(b2_asset.equals("20")) return "Micro";
            else return "Corporate";
        }
        return null;
    }
    
    /**
     * Metoda koja vraæa da li je plasman na SSP.
     * @param modul naziv modula (module_code iz tablice cusacc_exposure)
     * @param status status u aplikaciji iz koje se uzima plasman (cus_acc_orig_st iz tablice cusacc_exposure)
     */
    public boolean isSSP(String modul, String status) throws Exception
    {
        modul = modul.trim();
        status = status.trim();
        return (modul.equals("TRC") && status.equals("E"))  ||
               (modul.equals("PKR") && status.equals("T"))  ||
               (modul.equals("PPZ") && (status.equals("SS") || status.equals("NM"))) ||
               (modul.equals("SDR") && (status.equals("SS") || status.equals("NM"))) ||
               (modul.equals("KRD") && status.equals("SS")) ||
               (modul.equals("GAR") && status.equals("SS")) ||
               (modul.equals("KKR") && (status.equals("94") || status.equals("95"))) ||
               (modul.equals("LOC") && status.equals("SS"));
    }

	/** 
     * Metoda koja definira izgled kolona na tablici.
	 * @param sheetNumber redni broj sheeta
     * @return vektor s definiranim kolonama tablice
	 */
	private Vector getColumns(int sheetNumber)
	{
		Vector columns = new Vector();
		columns.add(new Column(30));		// B2asset class
		columns.add(new Column(37));		// OJ plasmana
		columns.add(new Column(150));		// OJ
		columns.add(new Column(95));		// Šifra kolaterala
		columns.add(new Column(35));		// Status kolaterala
		columns.add(new Column(95));		// Šifra police
		columns.add(new Column(135));		// Osiguravatelj
		columns.add(new Column(55, "s4"));	// Vrijedi do
		columns.add(new Column(55, "s4"));	// Plaæena od
		columns.add(new Column(55, "s4"));	// Plaæena do
        if(sheetNumber == 3) columns.add(new Column(60));   // IM osiguranika
		columns.add(new Column(160));		// Osiguranik
		columns.add(new Column(35));		// Valuta
		columns.add(new Column(65, "s2"));	// Osigurana svota
        if(sheetNumber == 3) columns.add(new Column(65, "s2")); // Akumulirana svota
		columns.add(new Column(30));		// Status police
		columns.add(new Column(30));		// Status napomene
		columns.add(new Column(95));		// Napomena o polici
		columns.add(new Column(60));		// Interni MB ugovaratelja
		columns.add(new Column(160));		// Ugovaratelj
		columns.add(new Column(60));		// Interni MB korisnika plasmana
		columns.add(new Column(160));		// Korisnik plasmana
		columns.add(new Column(75));		// Partija plasmana
		columns.add(new Column(40));		// DWH status plasmana
		columns.add(new Column(50));		// Status plasmana u modulu
		return columns;
	}
	
	/** 
     * Metoda koja definira izgled kolona na tablici za skraæeni izvještaj.
     * @param sheetNumber redni broj sheeta
     * @return vektor s definiranim kolonama tablice
     */
	private Vector getColumnsShort(int sheetNumber)
    {
        Vector columns = new Vector();
        columns.add(new Column(95));        // Šifra kolaterala
        columns.add(new Column(95));        // Šifra police
        columns.add(new Column(135));       // Osiguravatelj
        columns.add(new Column(55, "s4"));  // Vrijedi do
        columns.add(new Column(55, "s4"));  // Plaæena od
        columns.add(new Column(55, "s4"));  // Plaæena do
        columns.add(new Column(160));       // Osiguranik
        columns.add(new Column(35));        // Valuta
        columns.add(new Column(65, "s2"));  // Osigurana svota
        columns.add(new Column(30));        // Status police
        columns.add(new Column(30));        // Status napomene
        columns.add(new Column(95));        // Napomena o polici
        return columns;
    }
	
	/** 
     * Metoda koja formira red sa zaglavljem tablice.
     * @param sheetNumber redni broj sheeta
	 * @return formirani red tablice
	 */
	private hr.vestigo.modules.rba.util.excel.Row getHeaderRow(int sheetNumber)
	{
	    hr.vestigo.modules.rba.util.excel.Row row = new hr.vestigo.modules.rba.util.excel.Row(50, "s1");
		row.addCell(new Cell("B2asset class"));
		row.addCell(new Cell("OJ plasmana"));
		row.addCell(new Cell("OJ"));
		row.addCell(new Cell("\u0160ifra kolaterala"));
		row.addCell(new Cell("Status kolaterala"));
		row.addCell(new Cell("\u0160ifra police"));
		row.addCell(new Cell("Osiguravatelj"));
		row.addCell(new Cell("Vrijedi do"));
		row.addCell(new Cell("Pla\u0107ena od"));
		row.addCell(new Cell("Pla\u0107ena do"));
        if(sheetNumber == 3) row.addCell(new Cell("IM osiguranika"));
		row.addCell(new Cell("Osiguranik"));
		row.addCell(new Cell("Valuta"));
		row.addCell(new Cell("Osigurana svota"));
        if(sheetNumber == 3) row.addCell(new Cell("Akumulirana svota"));
		row.addCell(new Cell("Status police"));
		row.addCell(new Cell("Status napomene"));
		row.addCell(new Cell("Napomena o polici"));
		row.addCell(new Cell("Interni MB ugovaratelja"));
		row.addCell(new Cell("Ugovaratelj"));
		row.addCell(new Cell("Interni MB korisnika plasmana"));
		row.addCell(new Cell("Korisnik plasmana"));
		row.addCell(new Cell("Partija plasmana"));
		row.addCell(new Cell("DWH status plasmana"));
		row.addCell(new Cell("Status plasmana u modulu"));
		return row;
	}
	
	/** 
     * Metoda koja formira red sa zaglavljem tablice za skraæenu verziju.
     * @param sheetNumber redni broj sheeta
     * @return formirani red tablice
     */
    private hr.vestigo.modules.rba.util.excel.Row getHeaderRowShort(int sheetNumber)
    {   
        hr.vestigo.modules.rba.util.excel.Row row = new hr.vestigo.modules.rba.util.excel.Row(50, "s1");
        row.addCell(new Cell("\u0160ifra kolaterala"));
        row.addCell(new Cell("\u0160ifra police"));
        row.addCell(new Cell("Osiguravatelj"));
        row.addCell(new Cell("Vrijedi do"));
        row.addCell(new Cell("Pla\u0107ena od"));
        row.addCell(new Cell("Pla\u0107ena do"));
        row.addCell(new Cell("Osiguranik"));
        row.addCell(new Cell("Valuta"));
        row.addCell(new Cell("Osigurana svota"));
        row.addCell(new Cell("Status police"));
        row.addCell(new Cell("Status napomene"));
        row.addCell(new Cell("Napomena o polici"));
        return row;
    }
	
	/**
     * Metoda koja formira red tablice s podacima o polici (za prva dva sheeta)
	 * @param iter Iterator s podacima o polici
	 * @return formirani red tablice
	 */
	private hr.vestigo.modules.rba.util.excel.Row getDetailsRow(PolicyIterator iter) throws Exception
	{
	    hr.vestigo.modules.rba.util.excel.Row row = new hr.vestigo.modules.rba.util.excel.Row();
		row.addCell(new Cell(iter.b2asset_class()));                      // B2asset class
		row.addCell(new Cell(iter.oj_code()));                            // OJ plasmana
		row.addCell(new Cell(replaceXmlCharacters(iter.oj_name())));      // OJ
		row.addCell(new Cell(iter.col_num()));                            // Šifra kolaterala
		row.addCell(new Cell(iter.collateral_status()));                  // Status kolaterala
		row.addCell(new Cell(iter.ip_code()));                            // Šifra police
		row.addCell(new Cell(replaceXmlCharacters(iter.ic_name())));      // Osiguravatelj
		row.addCell(new Cell(checkDate(iter.valid_until())));             // Vrijedi do
		row.addCell(new Cell(checkDate(iter.paid_from())));               // Plaæena od
		row.addCell(new Cell(checkDate(iter.paid_until())));              // Plaæena do
		row.addCell(new Cell(replaceXmlCharacters(iter.osiguranik())));   // Osiguranik
		row.addCell(new Cell(iter.valuta()));                             // Valuta
		row.addCell(new Cell(iter.osigurana_svota()));                    // Osigurana svota
		row.addCell(new Cell(iter.status_police()));                      // Status police
		row.addCell(new Cell(iter.ip_spec_stat()));                       // Status napomene
		row.addCell(new Cell(iter.spec_stat()));                          // Napomena o polici
		row.addCell(new Cell(iter.id_ugovaratelja()));		              // Interni MB ugovaratelja
		row.addCell(new Cell(replaceXmlCharacters(iter.ugovaratelj())));  // Ugovaratelj
		row.addCell(new Cell(iter.id_korisnika()));			              // Interni MB korisnika plasmana
		row.addCell(new Cell(replaceXmlCharacters(iter.korisnik())));     // Korisnik plasmana
		row.addCell(new Cell(iter.plasman()));                            // Partija plasmana
		row.addCell(new Cell(iter.dwh_status()));                         // DWH status plasmana
		row.addCell(new Cell(iter.status_u_modulu()));                    // Status plasmana u modulu
		return row;
	}
	
	
	/**
     * Metoda koja formira red tablice s podacima o polici (za prva dva sheeta) za skraæenu verziju
     * @param iter Iterator s podacima o polici
     * @return formirani red tablice
     */
    private hr.vestigo.modules.rba.util.excel.Row getDetailsRowShort(PolicyIterator iter) throws Exception
    {
        hr.vestigo.modules.rba.util.excel.Row row = new hr.vestigo.modules.rba.util.excel.Row();
        
        row.addCell(new Cell(iter.col_num()));                            // Šifra kolaterala
        row.addCell(new Cell(iter.ip_code()));                            // Šifra police
        row.addCell(new Cell(replaceXmlCharacters(iter.ic_name())));      // Osiguravatelj
        row.addCell(new Cell(checkDate(iter.valid_until())));             // Vrijedi do
        row.addCell(new Cell(checkDate(iter.paid_from())));               // Plaæena od
        row.addCell(new Cell(checkDate(iter.paid_until())));              // Plaæena do
        row.addCell(new Cell(replaceXmlCharacters(iter.osiguranik())));   // Osiguranik
        row.addCell(new Cell(iter.valuta()));                             // Valuta
        row.addCell(new Cell(iter.osigurana_svota()));                    // Osigurana svota
        row.addCell(new Cell(iter.status_police()));                      // Status police
        row.addCell(new Cell(iter.ip_spec_stat()));                       // Status napomene
        row.addCell(new Cell(iter.spec_stat()));                          // Napomena o polici
        
        return row;
    }
    
    /**
     * Metoda koja formira red tablice s podacima o polici koja je kolateral (za treæi sheet)
     * @param iter Iterator s podacima o polici koja je kolateral
     * @return formirani red tablice
     */
    private hr.vestigo.modules.rba.util.excel.Row getDetailsRow(CollData coll) throws Exception
    {
        hr.vestigo.modules.rba.util.excel.Row row = new hr.vestigo.modules.rba.util.excel.Row();
        row.addCell(new Cell(coll.b2asset_class));                      // B2asset class
        row.addCell(new Cell(coll.oj_code));                            // OJ plasmana
        row.addCell(new Cell(replaceXmlCharacters(coll.oj_name)));      // OJ
        row.addCell(new Cell(coll.col_num));                            // Šifra kolaterala
        row.addCell(new Cell(coll.collateral_status));                  // Status kolaterala
        row.addCell(new Cell(coll.ip_code));                            // Šifra police
        row.addCell(new Cell(replaceXmlCharacters(coll.ic_name)));      // Osiguravatelj
        row.addCell(new Cell(checkDate(coll.valid_until)));             // Vrijedi do
        row.addCell(new Cell(checkDate(coll.paid_from)));               // Plaæena od
        row.addCell(new Cell(checkDate(coll.paid_until)));              // Plaæena do
        row.addCell(new Cell(coll.osig_reg_no));                        // IM osiguranika#
        row.addCell(new Cell(replaceXmlCharacters(coll.osiguranik)));   // Osiguranik
        row.addCell(new Cell(coll.valuta));                             // Valuta
        row.addCell(new Cell(coll.osigurana_svota));                    // Osigurana svota
        row.addCell(new Cell(coll.akum_amount));                        // Akumulirana svota#
        row.addCell(new Cell(coll.status_police));                      // Status police
        row.addCell(new Cell(coll.ip_spec_stat));                       // Status napomene
        row.addCell(new Cell(coll.spec_stat));                          // Napomena o polici
        row.addCell(new Cell(coll.id_ugovaratelja));                    // Interni MB ugovaratelja
        row.addCell(new Cell(replaceXmlCharacters(coll.ugovaratelj)));  // Ugovaratelj
        row.addCell(new Cell(coll.id_korisnika));                       // Interni MB korisnika plasmana
        row.addCell(new Cell(replaceXmlCharacters(coll.korisnik)));     // Korisnik plasmana
        row.addCell(new Cell(coll.plasman));                            // Partija plasmana
        row.addCell(new Cell(coll.dwh_status));                         // DWH status plasmana
        row.addCell(new Cell(coll.status_u_modulu));                    // Status plasmana u modulu
        return row;
    }
    
    /**
     * Metoda koja u stringu mijenja znakove koji se ne smiju pojaviti u XML-u
     * @param s String koji se mijenja
     * @return String sa zamijenjenim znakovima
     */
    private String replaceXmlCharacters(String s)
    {
        if(s == null) return s;
        s = s.replace("\"", "&quot;");
        s = s.replace("&", "&amp;");
        s = s.replace("'", "&apos;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        return s;
    }
    
    /**
     * Metoda koja provjerava da li je zadani datum manji od 1.1.1900.
     * @param d Datum koji se provjerava
     * @return Vraæa zadani datum ako je provjera uspješno prošla ili null ako nije
     */
    private Date checkDate(Date d)
    {
        if(d.getYear() < 0) return null; else return d;
    }

	/**
	 * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
	 * Parametri se predaju u obliku "RB;org_uni_id;client_type;X".
	 * @return da li su dohvat i provjera uspješno završili
	 */
	private boolean getParameters(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 4)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
                if (bc.getArg(1).trim().equals("") || bc.getArg(1).trim().equals("X")) org_uni_id = null; else org_uni_id = new BigDecimal(bc.getArg(1).trim());
	     		if (bc.getArg(2).trim().equals("") || bc.getArg(2).trim().equals("X")) client_type = null; else client_type = bc.getArg(2).trim().toUpperCase();
	     		if (bc.getArg(3).trim().equals("") || bc.getArg(3).trim().equals("X")) batchType = 0; 
	     		else 
	     		{
	     		  batchType =Integer.parseInt(bc.getArg(3).trim().toUpperCase());
	     		  if (client_type == null)
	     		  {
	     		     throw new Exception("Tip klijenta je obavezan za skraceni izvjestaj! (P/F)");
	     		  }
	     		}
	    	}
	    	else if (brojParametara == 1)  
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
	     		org_uni_id = null;
	     		client_type = "F";
	    	}
	    	
	    	else throw new Exception("Neispravan broj parametara!");
		}
		catch(Exception ex)
		{
            bc.error("Neispravno zadani parametri!", ex);
            return false;
		}
		return true;
	}

    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("41567243"));
        batchParameters.setArgs(args);
        new BO440().run(batchParameters);
    }
}