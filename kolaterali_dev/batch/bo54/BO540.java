package hr.vestigo.modules.collateral.batch.bo54;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo54.BO541.*;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Vector;
  

/**
 * Izvješæe o vezi kolateral plasman
 * @author hrakis     
 */
public class BO540 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo54/BO540.java,v 1.9 2014/03/14 10:14:53 hraskd Exp $";
    private BatchContext bc;
    // FBPr200010498 - ubaèen parametar cus_type: P-pravne osobe, F-fizièke osobe
    private String cus_type=null;
    // RTC12219 - ubaèen parametar rep_type: 0-prošireni, 1-skraæeni izvještaj
    private String rep_type=null;
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO540 pokrenut.");
        BO541 bo541 = new BO541(bc);
        this.bc = bc;
        
        // ubacivanje eventa
        BigDecimal eve_id = bo541.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
        
        // dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");
        
        // stvaranje izlaznih datoteka
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        String fileNameCore = bc.getOutDir() + "/" + "kolaterali_plasmani_" + dateString + "_<suffix>" + ".csv";
        String fileName1 = fileNameCore.replaceFirst("<suffix>", "imajuHipoteku");
        String fileName2 = fileNameCore.replaceFirst("<suffix>", "nemajuHipoteku");
        String fileName3 = fileNameCore.replaceFirst("<suffix>", "mjeniceZaduznice");
        OutputStreamWriter streamWriter1 = new OutputStreamWriter(new FileOutputStream(new File(fileName1)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName1);
        OutputStreamWriter streamWriter2 = new OutputStreamWriter(new FileOutputStream(new File(fileName2)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName2);
        OutputStreamWriter streamWriter3 = new OutputStreamWriter(new FileOutputStream(new File(fileName3)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName3);
        
        // zapisivanje zaglavlja u izlazne datoteke
        writeHeaderRow(streamWriter1, 1);
        writeHeaderRow(streamWriter2, 2);
        writeHeaderRow(streamWriter3, 3);
        bc.debug("Zapisana zaglavlja.");
        
        // dohvat kolaterala koji imaju hipoteku i zapisivanje u izlaznu datoteku
        Iter1 iter = bo541.selectCollateralsWithMortgage(cus_type,rep_type);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni kolaterali koji imaju hipoteku.");
        while(iter.next())
        {
            writeRow(streamWriter1, 1, iter);
        }
        iter.close();
        streamWriter1.flush();
        streamWriter1.close();
        bc.debug("Podaci zapisani u prvu datoteku.");
        
        // dohvat kolaterala koji nemaju hipoteku i zapisivanje u izlazne datoteke
        iter = bo541.selectCollateralsWithoutMortgage(cus_type,rep_type);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni kolaterali koji nemaju hipoteku.");
        
        final BigDecimal mjenice_id = new BigDecimal(617223);
        final BigDecimal zaduznice_id = new BigDecimal(625223);
        while(iter.next())
        {
            if(iter.col_cat_id().equals(mjenice_id) || iter.col_cat_id().equals(zaduznice_id))
            {
                writeRow(streamWriter3, 3, iter);  // mjenice i zadužnice idu u treæu datoteku
            }
            else
            {
                writeRow(streamWriter2, 2, iter);  // ostali kolaterali idu u drugu datoteku
            }
        }
        iter.close();
        streamWriter2.flush();
        streamWriter2.close();
        streamWriter3.flush();
        streamWriter3.close();
        bc.debug("Podaci zapisani u drugu i trecu datoteku.");
        
        // slanje izlaznih datoteka na mail
        bc.startStopWatch("sendMail");
        Vector attachments = new Vector();
        attachments.add(fileName1);
        attachments.add(fileName2);
        attachments.add(fileName3);
        YXY70.send(bc, "csv225", bc.getLogin(), attachments);
        bc.stopStopWatch("sendMail");
        
        // stvaranje marker datoteka
        new File(fileName1 + ".marker").createNewFile();
        new File(fileName2 + ".marker").createNewFile();
        new File(fileName3 + ".marker").createNewFile();
        bc.debug("Stvoreni markeri.");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    /**
     * Metoda upisuje jedan red tablice u zadanu CSV datoteku.
     */
    private void writeRow(OutputStreamWriter sw, int fileNumber, Iter1 iter) throws Exception
    {
        bc.startStopWatch("writeRow");
        StringBuffer buffer = new StringBuffer();
        buffer.append(norm(iter.sifra_kolaterala())).append(";"); 
        buffer.append(norm(iter.status_kolaterala())).append(";");
        buffer.append(norm(iter.datum_unosa_kolaterala())).append(";");
        buffer.append(norm(iter.datum_promjene_kolaterala())).append(";");
        if(fileNumber == 1) buffer.append(norm(iter.red_hipoteke())).append(";");
        buffer.append(norm(iter.partija_plasmana())).append(";");
        buffer.append(norm(iter.dwh_partija_plasmana())).append(";");
        buffer.append(norm(iter.broj_zahtjeva())).append(";");
        buffer.append(norm(iter.dwh_broj_zahtjeva())).append(";");
        buffer.append(norm(iter.broj_ugovora())).append(";");
        buffer.append(norm(iter.dwh_broj_ugovora())).append(";");
        buffer.append(norm(iter.im_korisnika())).append(";");
        buffer.append(norm(iter.korisnik())).append(";");
        buffer.append(norm(iter.b2asset())).append(";");
        buffer.append(norm(iter.dwh_status())).append(";");
        buffer.append(norm(iter.status_u_modulu())).append(";");
        buffer.append(norm(iter.datum_unosa_plasmana())).append(";");
        buffer.append(norm(iter.datum_promjene_plasmana())).append(";");
        buffer.append(norm(iter.izlozenost())).append(";");
        buffer.append(norm(iter.valuta_izlozenosti())).append(";");
        buffer.append(norm(iter.datum_izlozenosti())).append(";");
        buffer.append("\n");
        sw.write(buffer.toString());
        bc.stopStopWatch("writeRow");
    }
    
    /**
     * Metoda upisuje zaglavlje u zadanu CSV datoteku.
     */
    private void writeHeaderRow(OutputStreamWriter sw, int fileNumber) throws Exception
    {
        bc.startStopWatch("writeHeaderRow");
        StringBuffer buffer = new StringBuffer();
        buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Status kolaterala").append(";");
        buffer.append("Datum unosa kolaterala").append(";");
        buffer.append("Datum zadnje promjene").append(";");
        if(fileNumber == 1) buffer.append("Red hipoteke").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("DWH Partija plasmana").append(";");
        buffer.append("Broj zahtjeva").append(";");
        buffer.append("DWH Broj zahtjeva").append(";");
        buffer.append("Broj ugovora").append(";");
        buffer.append("DWH Broj ugovora").append(";");
        buffer.append("IM korisnika").append(";");
        buffer.append("Korisnik").append(";");
        buffer.append("B2ASSET").append(";");
        buffer.append("DWH status").append(";");
        buffer.append("Status u modulu").append(";");
        buffer.append("Datum unosa plasmana").append(";");
        buffer.append("Datum promjene").append(";");
        buffer.append("Izlo\u017Eenost").append(";");
        buffer.append("Valuta").append(";");
        buffer.append("Datum izlo\u017Eenosti").append(";");
        buffer.append("\n");
        sw.write(buffer.toString());
        bc.stopStopWatch("writeHeaderRow");
    }
    
    private String norm(Object obj)
    {
        if (obj == null) return ""; 
        else return obj.toString().trim();
    }
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
            if(brojParametara == 4)
            {
                if(bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                
                if(!bc.getArg(1).equalsIgnoreCase("P") && !bc.getArg(1).equalsIgnoreCase("F")) throw new Exception("Drugi parametar mora biti 'F' ili 'P'!");
                cus_type=bc.getArg(1);
                if(!bc.getArg(2).equalsIgnoreCase("0") && !bc.getArg(2).equalsIgnoreCase("1")) throw new Exception("Treæi parametar mora biti '0' ili '1'!");
                rep_type=bc.getArg(2);         
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("3004161164"));
        batchParameters.setArgs(args);
        new BO540().run(batchParameters);
    }
}