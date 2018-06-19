package hr.vestigo.modules.collateral.batch.bo66;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo66.BO661.Iter1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


/**
 * Eksport podataka za SOM RI rezervacije
 * @author hrakis
 */
public class BO660 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo66/BO660.java,v 1.3 2014/06/11 08:20:19 hrakis Exp $";

    private BatchContext bc;
    private BO661 bo661;
    private Date value_date;
    private BigDecimal col_pro_id;


    public String executeBatch(BatchContext bc) throws Exception
    {
        bc.debug("BO660 pokrenut.");
        this.bc = bc;
        this.bo661 = new BO661(bc);

        // ubacivanje eventa
        BigDecimal eve_id = bo661.insertIntoEvent();

        // dohvat i provjera parametara obrade
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;

        // dohvat datuma zadnjeg izraèuna pokrivenosti za ponderiranu RBA prihvatljivost
        value_date = bo661.selectMaxValueDate();
        if(value_date == null) return RemoteConstants.RET_CODE_ERROR;

        // dohvat ID-a obrade za izraèun pokrivenosti
        col_pro_id = bo661.selectColProId(value_date);
        if(col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;

        // dohvat podataka
        Iter1 iter = bo661.selectData(col_pro_id);

        // stvaranje izlazne datoteke
        String dir = bc.getOutDir() + "/";
        String dateString = new SimpleDateFormat("yyyyMMdd").format(value_date);
        String fileName = dir + "CO_" + dateString + "_RBA_pond" + ".csv";
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName);

        // zapisivanje podataka u izlaznu datoteku
        streamWriter.write(getHeaderRow());
        while(iter.next())
        {
            streamWriter.write(getDetailsRow(iter));
        }
        iter.close();
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u izlaznu datoteku.");

        // stvaranje marker datoteke
        new File(fileName + ".marker").createNewFile();
        bc.debug("Stvoren marker.");

        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }

    /**
     * Metoda formira zaglavlje izlazne datoteke i vraæa ga u obliku stringa.
     */
    private String getHeaderRow() throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Datum").append(";");
        buffer.append("ID komitenta").append(";");
        buffer.append("Naziv komitenta").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("Pripadni okvir").append(";");
        buffer.append("Ponderirani iznos kolaterala");
        return buffer.append("\n").toString();
    }

    /**
     * Metoda formira jedan red izlazne datoteke i vraæa ga u obliku stringa.
     */
    private String getDetailsRow(Iter1 iter) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(value_date).append(";");
        buffer.append(normalize(iter.register_no())).append(";");
        buffer.append(normalize(iter.name())).append(";");
        buffer.append(normalize(iter.cus_acc_no())).append(";");
        buffer.append(normalize(iter.frame_cus_acc_no())).append(";");
        
        BigDecimal osigurano = iter.ukupnaPokrivenost();
        if (osigurano.compareTo(iter.ukupnaIzlozenost()) > 0) osigurano = iter.ukupnaIzlozenost();  // osigurani dio ne smije biti veæi od izloženosti
        buffer.append(normalize(osigurano));
        
        return buffer.append("\n").toString();
    }
    
    /**
     * Metoda koja vraæa da li je plasman okvir.
     */
    public boolean isFrame(Iter1 iter) throws SQLException
    {
        return ("OKV".equals(iter.module_code()) && iter.frame_cus_acc_no() == null);
    }

    private String normalize(Object obj)
    {
        return obj == null ? "" : obj.toString().trim();
    }

    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Kao parametar se predaje samo bank_sign (RB).
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i + ". = " + bc.getArg(i));  // ispis parametara                   
            if(brojParametara == 1)
            {
                if(bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
            }
            else throw new Exception("Neispravan broj parametara!");
        }
        catch(Exception ex)
        {
            bc.error("Neispravno zadani parametri!", ex);
            return false;
        }
        bc.debug("Parametri obrade dohvaceni.");
        return true;
    }

    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4768233704"));
        batchParameters.setArgs(args);
        new BO660().run(batchParameters);
    }
}