package hr.vestigo.modules.collateral.batch.bo50;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo50.BO501.Iter1;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.excel.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;


/**
 * Izvješæe o nepovezanim plasmanima.
 * @author hrakis
 */
public class BO500 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo50/BO500.java,v 1.1 2010/02/10 09:27:42 hrakis Exp $";
    private BO501 bo501;
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO500 pokrenut.");
        bo501 = new BO501(bc);
        
        // insertiranje eventa
        BigDecimal eve_id = bo501.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
        
        // dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");
        
        // dohvat podataka o plasmanima koji imaju hipoteku
        Iter1 iter1 = bo501.selectLoansWithMortgage();
        if(iter1 == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci o plasmanima s hipotekom.");
        
        // dohvat podataka o plasmanima koji nemaju hipoteku
        Iter1 iter2 = bo501.selectLoansWithoutMortgage();
        if(iter2 == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci o plasmanima bez hipoteke.");

        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        String dir = bc.getOutDir() + "/";
        String fileName = dir + "Nepovezani_plasmani_" + dateString;

        // stvaranje workbooka
        Workbook workbook = new Workbook(fileName, Workbook.FileType.ExcelML, true, 100);
        workbook.addStyle(new Style("Default", Alignment.Bottom, new Font("Arial",9)));
        workbook.addStyle(new Style("s1", Alignment.BottomCenter, Borders.All, new Font("Arial",9,true), new Interior("#FFFF00"), null));
        workbook.addStyle(new Style("s2", null, null, null, null, "Short Date"));
        workbook.open();
        bc.debug("Otvoren workbook.");
        
        // stvaranje sheetova
        Worksheet sheetWithMortgage = workbook.addWorksheet(new Worksheet("imaju hipoteku", dir + "nepovezani_plasmani_s_hipotekom__" + dateString));
        Worksheet sheetWithoutMortgage = workbook.addWorksheet(new Worksheet("nemaju hipoteku", dir + "nepovezani_plasmani_bez_hipoteke_" + dateString));
        bc.debug("Stvoreni sheetovi.");

        // definiranje kolona
        sheetWithMortgage.addColumns(getColumns());
        sheetWithoutMortgage.addColumns(getColumns());
        bc.debug("Definirane kolone.");

        // otvaranje sheetova
        workbook.openAllWorksheets();
        bc.debug("Otvoreni sheetovi.");
        
        // formiranje zaglavlja na sheetovima
        sheetWithMortgage.addRow(getHeaderRow());
        sheetWithoutMortgage.addRow(getHeaderRow());
        bc.debug("Dodana zaglavlja.");
    
        // zapisivanje podataka u sheetove
        while(iter1.next())
        {
            sheetWithMortgage.addRow(getDetailsRow(iter1));
        }
        bc.debug("Zapisani svi redovi u prvi sheet.");
        
        while(iter2.next())
        {
            sheetWithoutMortgage.addRow(getDetailsRow(iter2));
        }
        bc.debug("Zapisani svi redovi u drugi sheet.");
        
        workbook.closeAllWorksheets();
        workbook.close();
        bc.debug("Zatvoren workbook.");
        
        // slanje maila
        YXY70.send(bc, "csv222", bc.getLogin(), fileName + ".xml");
        bc.debug("Mail poslan.");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }

    /** Metoda koja definira izgled kolona.
     * @return vektor s definiranim kolonama
     */
    private Vector getColumns()
    {
        Vector columns = new Vector();
        columns.add(new Column(150));
        columns.add(new Column(80,"s2"));
        columns.add(new Column(80,"s2"));
        columns.add(new Column(40));
        columns.add(new Column(130));
        columns.add(new Column(130));
        columns.add(new Column(70));
        columns.add(new Column(200));
        columns.add(new Column(50));
        columns.add(new Column(60));
        columns.add(new Column(80));
        columns.add(new Column(80,"s2"));
        columns.add(new Column(80,"s2"));
        columns.add(new Column(80,"s2"));
        return columns;
    }
    
    /** Metoda koja formira red sa zaglavljem.
     * @return formirani red tablice
     */
    private Row getHeaderRow()
    {
        Row row = new Row(50, "s1");
        row.addCell(new Cell("Šifra kolaterala"));
        row.addCell(new Cell("Datum unosa kolaterala"));
        row.addCell(new Cell("Datum zadnje promjene"));
        row.addCell(new Cell("Red hipoteke"));
        row.addCell(new Cell("Partija plasmana"));
        row.addCell(new Cell("Broj zahtjeva"));
        row.addCell(new Cell("IM korisnika"));
        row.addCell(new Cell("Korisnik"));
        row.addCell(new Cell("B2ASSET"));
        row.addCell(new Cell("DWH status"));
        row.addCell(new Cell("Status u modulu"));
        row.addCell(new Cell("Datum izloženosti"));
        row.addCell(new Cell("Datum unosa plasmana"));
        row.addCell(new Cell("Datum promjene"));
        return row;
    }
    
    /** Metoda koja formira red tablice s podacima o plasmanu
     * @param iter Iterator s podacima o plasmanu
     * @return formirani red tablice
     */
    private Row getDetailsRow(Iter1 iter) throws Exception
    {
        Row row = new Row();
        row.addCell(new Cell(iter.sifra_kolaterala()));
        row.addCell(new Cell(iter.datum_unosa_kolaterala()));
        row.addCell(new Cell(iter.datum_promjene_kolaterala()));
        row.addCell(new Cell(iter.red_hipoteke()));
        row.addCell(new Cell(iter.partija_plasmana()));
        row.addCell(new Cell(iter.broj_zahtjeva()));
        row.addCell(new Cell(iter.im_korisnika()));
        row.addCell(new Cell(iter.korisnik()));
        row.addCell(new Cell(iter.b2asset()));
        row.addCell(new Cell(iter.dwh_status()));
        row.addCell(new Cell(iter.status_u_modulu()));
        row.addCell(new Cell(iter.datum_izlozenosti()));
        row.addCell(new Cell(iter.datum_unosa_plasmana()));
        row.addCell(new Cell(iter.datum_promjene_plasmana()));
        return row;
    }

    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Obrada prima samo jedan parametar: RB.
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
            if (brojParametara == 1)
            {
                if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2675821724"));
        batchParameters.setArgs(args);
        new BO500().run(batchParameters);
    }
}