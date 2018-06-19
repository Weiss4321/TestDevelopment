package hr.vestigo.modules.collateral.batch.bo47;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo47.BO471.CollIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.SendMail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;


/**
 * Izvješæe o osloboðenim kolateralima.
 * @author hrakis
 */
public class BO470 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo47/BO470.java,v 1.3 2011/11/28 14:52:25 hramkr Exp $";
    private BO471 bo471;
    private Date date_from, date_until;
    private String client_type;
    private BigDecimal col_cat_id;
    private BigDecimal ins_policy_col_cat_id = new BigDecimal("616223");
    private String status = "";
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO470 pokrenut.");
        bo471 = new BO471(bc);
        
        // insertiranje eventa
        BigDecimal eve_id = bo471.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
        
        // dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");
        
        // dohvat podataka
        CollIterator iter = bo471.selectCollaterals(date_from, date_until, client_type, col_cat_id);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci.");
         
        // stvaranje streamwritera
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        String fileName = bc.getOutDir() + "/" + "OslobodjeniDeaktiviraniKolaterali" + dateString + ".csv";
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvoren streamwriter.");
        
        // zapisivanje zaglavlja u izlaznu datoteku
        streamWriter.write(getHeader());
        bc.debug("Zapisano zaglavlje.");
        
        // zapisivanje podataka u izlaznu datoteku
        while(iter.next())
        {
            bc.debug("col_num=" + iter.col_num());
            if (iter.collateral_status().equalsIgnoreCase("N"))
                status = "deaktiviran";
            else 
                status = "oslobodjen";
            bc.debug("col_num=" + iter.col_num() + ".... status= "+status);
            streamWriter.write(getRow(iter));
        }
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u datoteku.");
        
        // slanje maila
        YXY70.send(bc, "csv217", bc.getLogin(), fileName);
        bc.debug("Mail poslan.");
        
        // stvaranje marker datoteke
        new File(fileName + ".marker").createNewFile();
        bc.debug("Stvoren marker.");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    /**
     * Metoda formira jedan red tablice za CSV i vraæa ga u obliku stringa.
     * @param iter Iterator s kolateralima
     * @return red tablice u obliku stringa
     */
    private String getRow(CollIterator iter) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(status).append(";"); // status kolaterala
        buffer.append(iter.col_num()).append(";");          // šifra kolaterala
        buffer.append(iter.coll_type_name()).append(";");   // vrsta kolaterala
        buffer.append(iter.acc_no()).append(";");           // partija plasmana
        buffer.append(iter.request_no()).append(";");       // broj zahtjeva
        buffer.append(iter.register_no()).append(";");      // interni MB vlasnika plasmana
        buffer.append(iter.customer_name()).append(";");    // naziv vlasnika plasmana
        buffer.append(iter.inspol_ind()).append(";");       // polica imovine

        // dohvati zadnju policu vezanu za kolateral
        Vector vector = bo471.selectInsurancePolicy(iter.col_hea_id());
        for(int a=0;a<4;a++)
        {
            if(vector != null) buffer.append(vector.get(a));
            buffer.append(";");
        }
             
        buffer.append(iter.user_lock()).append(";");        // datum prijenosa na listu slobodnih kolaterala
        
        // ako je kolateral polica osiguranja, dohvati dodatne podatke o kolateralu
        if(iter.col_cat_id().equals(ins_policy_col_cat_id))
        {
            vector = bo471.selectCollInsPolicy(iter.col_hea_id());
            for(int a=0;a<4;a++)
            {
                if(vector != null) buffer.append(vector.get(a));
                buffer.append(";");
            }
        }
        
        buffer.append("\n");
        return buffer.toString();
    }
    
    /**
     * Metoda formira zaglavlje za CSV i vraæa ga u obliku stringa.
     * @return zaglavlje u obliku stringa
     */
    private String getHeader()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Status kolaterala").append(";");
        buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Vrsta kolaterala").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("Broj zahtjeva").append(";");
        buffer.append("Interni MB vlasnika plasmana").append(";");
        buffer.append("Naziv vlasnika plasmana").append(";");
        buffer.append("Polica imovine").append(";");
        buffer.append("Broj police imovine").append(";");
        buffer.append("Status police imovine").append(";");
        buffer.append("Napomena o polici").append(";");
        buffer.append("Osiguravatelj").append(";");
        buffer.append("Datum prijenosa na listu slobodnih/deaktiviranih kolaterala").append(";");

        // stvori dodatne kolone za police ako su u izvješæe ukljuèene sve kategorije kolaterala ili ako su ukljuèene samo police osiguranja
        if(col_cat_id == null || col_cat_id.equals(ins_policy_col_cat_id))  
        {
            buffer.append("Broj police").append(";");
            buffer.append("Status police").append(";");
            buffer.append("Napomena o polici").append(";");
            buffer.append("Osiguravatelj").append(";");
        }

        buffer.append("\n");
        return buffer.toString();
    }
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Parametri se predaju u obliku "RB;date_from;date_until;client_type;col_cat_id;X".
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
            if(brojParametara == 6)
            {
                if(bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                if(bc.getArg(1).trim().equals("")) date_from = null; else date_from = DateUtils.parseDate(bc.getArg(1).trim());
                date_until = DateUtils.parseDate(bc.getArg(2).trim());
                client_type = bc.getArg(3).trim();
                if(!client_type.equals("P") && !client_type.equals("F")) client_type = null;
                if(bc.getArg(4).trim().equals("")) col_cat_id = null; else col_cat_id = new BigDecimal(bc.getArg(4).trim()); 
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("814034654"));
        batchParameters.setArgs(args);
        new BO470().run(batchParameters);
    }
}