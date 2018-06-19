package hr.vestigo.modules.collateral.batch.bo59;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo59.BO591.Iter1;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;

import java.io.*;
import java.math.BigDecimal;


/**
 * Slobodne partije iz okvira / Višestruko dodane partije.
 * @author hrakis
 */
public class BO590 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo59/BO590.java,v 1.2 2011/04/27 12:42:23 hrakis Exp $";
    private BatchContext bc;
    private String exception_type;
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO590 pokrenut.");
        BO591 bo591 = new BO591(bc);
        this.bc = bc;
        
        // ubacivanje eventa
        BigDecimal eve_id = bo591.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
        
        // dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");
        
        // stvaranje izlazne datoteke
        String fileName = bc.getOutDir() + "/";
        fileName += exception_type.equals("F") ? "SlobodnePartijeIzOkvira.csv" : "VisestrukoDodanePartije.csv";
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName);
        
        // zapisivanje zaglavlja u izlaznu datoteku
        writeHeaderRow(streamWriter);
        bc.debug("Zapisano zaglavlje.");
        
        // dohvat podataka
        Iter1 iter = bo591.selectData(exception_type);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci.");
        
        // zapisivanje u izlaznu datoteku
        while(iter.next())
        {
            writeRow(streamWriter, iter);
        }
        iter.close();
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u izlaznu datoteku.");
        
        // slanje izlaznih datoteka na mail
        bc.startStopWatch("sendMail");
        String rpt_code = exception_type.equals("F") ? "csv237" : "csv239";
        YXY70.send(bc, rpt_code, bc.getLogin(), fileName);
        bc.stopStopWatch("sendMail");
        bc.debug("Mail poslan.");
        
        // stvaranje marker datoteke
        new File(fileName + ".marker").createNewFile();
        bc.debug("Stvoren marker.");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    /**
     * Metoda upisuje jedan red tablice u zadanu CSV datoteku.
     */
    private void writeRow(OutputStreamWriter sw, Iter1 iter) throws Exception
    {
        bc.startStopWatch("writeRow");
        StringBuffer buffer = new StringBuffer();
        buffer.append(iter.register_no()).append(";");
        buffer.append(iter.name()).append(";");
        buffer.append(iter.cus_acc_no()).append(";");
        buffer.append(iter.contract_no()).append(";");
        buffer.append(iter.status()).append(";");
        buffer.append(iter.comment()).append(";");
        buffer.append(iter.user_name()).append(";");
        buffer.append("\n");
        sw.write(buffer.toString());
        bc.stopStopWatch("writeRow");
    }
    
    /**
     * Metoda upisuje zaglavlje u zadanu CSV datoteku.
     */
    private void writeHeaderRow(OutputStreamWriter sw) throws Exception
    {
        bc.startStopWatch("writeHeaderRow");
        StringBuffer buffer = new StringBuffer();
        buffer.append("Interni MB").append(";");
        buffer.append("Naziv komitenta").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("Broj ugovora").append(";");
        buffer.append("Status").append(";");
        buffer.append("Komentar").append(";");
        buffer.append("Referent").append(";");
        buffer.append("\n");
        sw.write(buffer.toString());
        bc.stopStopWatch("writeHeaderRow");
    }
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Parametri se predaju u obliku "RB;exception_type;X".
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
            if(brojParametara == 3)
            {
                if(bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                exception_type = bc.getArg(1).trim().toUpperCase();
                if(!exception_type.equals("F") && !exception_type.equals("D")) throw new Exception("Dozvoljene vrijednosti za vrstu izuzeca su F ili D!");
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4454981964"));
        batchParameters.setArgs(args);
        new BO590().run(batchParameters);
    }
}