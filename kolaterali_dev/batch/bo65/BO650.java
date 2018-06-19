package hr.vestigo.modules.collateral.batch.bo65;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo65.BO651.Iter1;
import hr.vestigo.modules.collateral.common.yoyF.*;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;


/**
 * Izvješæe o promjeni statusa po policama osiguranja
 * @author hrakis
 */
public class BO650 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo65/BO650.java,v 1.1 2011/09/12 08:02:04 hrakis Exp $";
    
    private BatchContext bc;
    private BO651 bo651;
    private BigDecimal eve_id;
    
    private String policy_type;
    private String cust_type;
    private BigDecimal ic_id;
    private BigDecimal org_uni_id;
    private String status_old;
    private String status_new;
    private String wrn_status_old;
    private String wrn_status_new;
    private Date date_from;
    private Date date_until;
    
    private final String policy_type_coll = "K";
    private final String policy_type_inspol = "P";
    private OutputStreamWriter streamWriter;
    private int index = 0;
    
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO650 pokrenut.");
        this.bo651 = new BO651(bc);
        this.bc = bc;
        
        // ubacivanje eventa
        eve_id = bo651.insertIntoEvent();
        
        // dohvat i provjera parametara obrade
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        
        // stvaranje izlazne datoteke
        String dir = bc.getOutDir() + "/";
        String policy_type_suffix = policy_type.equals(policy_type_coll) ? "Zivota" : "Imovine";
        String fileName = dir + "PromjeneStatusaPolicaOsiguranja" + policy_type_suffix + ".csv";
        streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName);
        
        // zapisivanje zaglavlja u izlaznu datoteku
        writeHeaderRow(streamWriter);
        
        // zapisivanje podataka u izlaznu datoteku
        Iter1 iter = bo651.selectData(policy_type, cust_type, ic_id, org_uni_id, status_old, status_new, wrn_status_old, wrn_status_new, date_from, date_until);
        while(iter.next())
        {
            writeRow(streamWriter, iter);
        }
        iter.close();
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u izlaznu datoteku.");

        // slanje maila
        bc.startStopWatch("sendMail");
        YXY70.send(bc, "csv247", bc.getLogin(), fileName);
        bc.stopStopWatch("sendMail");
        bc.debug("Mail poslan.");

        // stvaranje marker datoteke
        new File(fileName + ".marker").createNewFile();
        bc.debug("Stvoren marker.");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    /**
     * Metoda upisuje zaglavlje u zadanu CSV datoteku.
     */
    private void writeHeaderRow(OutputStreamWriter sw) throws Exception
    {
        bc.startStopWatch("writeHeaderRow");
        StringBuffer buffer = new StringBuffer();
        // ispis parametara
        if(!isEmpty(cust_type)) buffer.append("Vrsta komitenta: ").append(cust_type).append("\n");
        if(!isEmpty(org_uni_id)) buffer.append("Organizacijska jedinica: ").append(org_uni_id).append("\n");
        if(!isEmpty(ic_id)) buffer.append("Osiguravatelj: ").append(ic_id).append("\n");
        if(!isEmpty(status_old)) buffer.append("Stari status police: ").append(status_old).append("\n");
        if(!isEmpty(status_new)) buffer.append("Novi status police: ").append(status_new).append("\n");
        if(!isEmpty(wrn_status_old)) buffer.append("Stari status obavijesti/opomene: ").append(wrn_status_old).append("\n");
        if(!isEmpty(wrn_status_new)) buffer.append("Novi status obavijesti/opomene: ").append(wrn_status_new).append("\n");
        if(!isEmpty(date_from)) buffer.append("Datum od: ").append(date_from).append("\n");
        if(!isEmpty(date_until)) buffer.append("Datum do: ").append(date_until).append("\n");
        // ispis zaglavlja tablice
        buffer.append("Redni broj").append(";");
        buffer.append("OJ").append(";");
        buffer.append("Interni ID").append(";");
        buffer.append("Ime i prezime").append(";");
        buffer.append("Adresa").append(";");
        buffer.append("Po\u0161ta").append(";");
        buffer.append("Mjesto").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Datum dospije\u0107a plasmana").append(";");
        buffer.append("Broj police").append(";");
        buffer.append("Datum do").append(";");
        buffer.append("Pla\u0107eno do").append(";");
        buffer.append("Status police (trenutni)").append(";");
        buffer.append("Datum zadnje promjene statusa").append(";");
        buffer.append("Status slanja obavijesti/opomene (trenutni)").append(";");
        buffer.append("Datum zadnje promjene statusa slanja obavijesti/opomene").append(";");
        buffer.append("\n");
        sw.write(buffer.toString());
        bc.stopStopWatch("writeHeaderRow");
    }
    
    /**
     * Metoda upisuje jedan red tablice u zadanu CSV datoteku.
     */
    private void writeRow(OutputStreamWriter sw, Iter1 iter) throws Exception
    {
        bc.startStopWatch("writeRow");
        StringBuffer buffer = new StringBuffer();
        buffer.append(++index).append(";");
        buffer.append(iter.org_uni_code()).append(";");
        buffer.append(iter.register_no()).append(";");
        buffer.append(iter.name()).append(";");
        buffer.append(iter.street() + " " + iter.house_no()).append(";");
        buffer.append(iter.postal_code() + " " + iter.post_office_name()).append(";");
        buffer.append(iter.city()).append(";");
        buffer.append(iter.cus_acc_no()).append(";");
        buffer.append(iter.col_num()).append(";");
        buffer.append(iter.due_date()).append(";");
        buffer.append(iter.ip_code()).append(";");
        buffer.append(iter.ip_valid_until()).append(";");
        buffer.append(iter.ip_paid_until()).append(";");
        buffer.append(iter.status()).append(";");
        buffer.append(iter.last_status_change()).append(";");
        buffer.append(iter.wrn_status()).append(";");
        buffer.append(iter.last_wrn_status_change()).append(";");
        buffer.append("\n");
        sw.write(buffer.toString());
        bc.stopStopWatch("writeRow");
    }

    private boolean isEmpty(Object obj)
    {
        return (obj == null || obj.equals(""));
    }

    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Parametri se predaju u obliku "Parametri se predaju u obliku RB;policy_type;cust_type;ic_id;org_uni_id;status_old;status_new;wrn_status_old;wrn_status_new;date_from;date_until;X".
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i + ". = " + bc.getArg(i));  // ispis parametara                   
            if(brojParametara == 12)
            {
                if(bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                policy_type = bc.getArg(1).trim().toUpperCase();
                if(!policy_type.equals(policy_type_coll) && !policy_type.equals(policy_type_inspol)) throw new Exception("Dozvoljene vrijednosti za vrstu police su " + policy_type_coll + " i " + policy_type_inspol + "!");
                if(bc.getArg(2).trim().equals("")) cust_type = null; else cust_type = bc.getArg(2).trim().toUpperCase();
                if(bc.getArg(3).trim().equals("")) ic_id = null; else ic_id = new BigDecimal(bc.getArg(3).trim());
                if(bc.getArg(4).trim().equals("")) org_uni_id = null; else org_uni_id = new BigDecimal(bc.getArg(4).trim());
                if(bc.getArg(5).trim().equals("")) status_old = null; else status_old = bc.getArg(5).trim().toUpperCase();
                if(bc.getArg(6).trim().equals("")) status_new = null; else status_new = bc.getArg(6).trim().toUpperCase();
                if(bc.getArg(7).trim().equals("")) wrn_status_old = null; else wrn_status_old = bc.getArg(7).trim().toUpperCase();
                if(bc.getArg(8).trim().equals("")) wrn_status_new = null; else wrn_status_new = bc.getArg(8).trim().toUpperCase();
                if(bc.getArg(9).trim().equals("")) date_from = null; else date_from = DateUtils.parseDate(bc.getArg(9).trim());
                if(bc.getArg(10).trim().equals("")) date_until = null; else date_until = DateUtils.parseDate(bc.getArg(10).trim());
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4729536704"));
        batchParameters.setArgs(args);
        new BO650().run(batchParameters);
    }
}