//created 2011.10.11
package hr.vestigo.modules.collateral.batch.bo67;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.RemoteContext;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.SendMail;
import hr.vestigo.modules.collateral.batch.bo67.BO671.IteratorData;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author hradnp
 */
public class BO670 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo67/BO670.java,v 1.2 2014/06/11 08:22:10 hrakis Exp $";
    private BO671 bo671 = null;
    
    private Date reportDate = null;                                // Datum izvještaja
    private String eligibility, ponder, registerNo, accNo; 
    private BigDecimal col_pro_id, exchangeRate;    
    private BatchContext bc = null;
   
    private OutputStreamWriter streamWriter;

    
    public String executeBatch(BatchContext bc) throws Exception
    {
        bc.debug("BO670 pokrenut.");
        this.bo671 = new BO671(bc);
        this.bc = bc;

        long pocetakIzvodenja=0;            
        pocetakIzvodenja=System.currentTimeMillis();
        
        // Ubacivanje eventa
        BigDecimal eve_id = bo671.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");

        // Dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");

        // Dohvat ID obrade izraèuna pokrivenosti
        col_pro_id = bo671.selectColProId(reportDate, ponder, eligibility);
        bc.debug("col_pro_id: " + col_pro_id);
        if(col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        
        // Stvaranje izlazne datoteke
        String dir = bc.getOutDir() + "/";
        String fileName = dir + "ColateralAlocatedValues_" + pocetakIzvodenja + ".csv";             
        streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka.");

        // Zapisivanje zaglavlja u izlazne datoteke
        streamWriter.write(getHeaderRow());
        bc.debug("Zapisano zaglavlje.");

        // Dohvat podataka u ovisnosti o opcionalnim parametrima
        IteratorData iter = null;
        if(registerNo == null && accNo == null){
            bc.debug("Komitent i partija plasmana nisu zadani.");
            iter = bo671.selectData(col_pro_id);
        }else if(registerNo != null && accNo == null){
            bc.debug("Zadan je komitent za kojeg treba dohvatiti podatke: register_no = "+registerNo+".");
            iter = bo671.selectDataByRegisterNo(col_pro_id, registerNo);
        }else if(registerNo != null && accNo != null){
            bc.debug("Zadani su i komitent i partija plasmana: register_no = "+registerNo+"; cus_acc_no = "+accNo+".");
            // Uz uvjet registerNo imamo i accNo
            iter = bo671.selectDataByAccNo(col_pro_id, registerNo, accNo);
        }else{
            bc.info("Potrebno je zadati i register_no, ako se zadaje acc_no.");
            return RemoteConstants.RET_CODE_ERROR;
        }
        if(iter == null)return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci.");

        // Dohvat srednjeg teèaja eura za dan za koji se izvršava obrada
        exchangeRate = bo671.selectExchangeRate(reportDate);
        bc.debug("Dohvacen tecaj.");

        // Zapisivanje podataka u izvješæe 
        while(iter.next()){
            streamWriter.write(getDetailsRow(iter));
        }
        bc.debug("Podaci zapisani u izvjesce.");

        // Zatvaranje izlazne datoteke
        if(streamWriter != null) streamWriter.flush();
        if(streamWriter != null) streamWriter.close();
        bc.debug("Zatvorena izlazna datoteka.");

        // Slanje maila
        YXY70.send(bc, "csvbo67", bc.getLogin(), fileName);
        bc.debug("Mail poslan.");
            
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }

    /**
     * Metoda formira jedan red izvješæa i vraæa ga u obliku stringa.
     * @param iter Iterator s podacima
     * @return formirani red
     */
    private String getDetailsRow(IteratorData iter) throws SQLException{
        
        StringBuffer buffer = new StringBuffer();       
        buffer.append(iter.register_no().trim()).append(";");  
        buffer.append(iter.name().trim()).append(";");
        buffer.append(iter.cus_acc_no().trim()).append(";");
        buffer.append(iter.col_num().trim()).append(";");
        buffer.append(iter.exp_coll_amount().toString().trim()).append(";");
       
        // Preraèunati iznos exp_coll_amount u eure
        BigDecimal exp_coll_amount_eur = toEur(iter.exp_coll_amount(), exchangeRate);
        
        buffer.append(exp_coll_amount_eur.toString().trim()).append(";");
        return buffer.append("\n").toString();
    }

    /**
     * Metoda formira zaglavlje za izvješæe i vraæa ga u obliku stringa.
     * @return formirano zaglavlje
     */
    private String getHeaderRow(){

        StringBuffer buffer = new StringBuffer();
        buffer.append("Interni MB komitenta korisnika plasmana").append(";");
        buffer.append("Naziv komitenta").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("Kolateral").append(";");
        buffer.append("Osigurano u kunama").append(";");
        buffer.append("Osigurano u eurima").append(";");
        return buffer.append("\n").toString();
    }
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Parametri se predaju u obliku "RB;report_date;eligibility;ponder;register_no;cus_acc_no;X".
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters(BatchContext bc){
        try{
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara   
            if (brojParametara == 6){
                if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                reportDate = DateUtils.parseDate(bc.getArg(1).trim());

                eligibility = bc.getArg(2).trim().toUpperCase();
                if(!eligibility.equals("B1") && !eligibility.equals("B2") && !eligibility.equals("RBA") && !eligibility.equals("B2IRB") && !eligibility.equals("ND")) throw new Exception("Dozvoljene vrijednosti za eligibility su B1,B2,RBA,B2IRB ili ND!");

                ponder = bc.getArg(3).trim().toUpperCase();
                if(!ponder.equals("P") && !ponder.equals("N")) throw new Exception("Dozvoljene vrijednosti za ponder su P ili N!");

                if (bc.getArg(4).trim().equals("") || bc.getArg(4).trim().equalsIgnoreCase("X")) registerNo = null; else registerNo = bc.getArg(4).trim();
                if (bc.getArg(5).trim().equals("") || bc.getArg(5).trim().equalsIgnoreCase("X")) accNo = null; else accNo = bc.getArg(5).trim();
            }
            else throw new Exception("Neispravan broj parametara!");
        }
        catch(Exception ex){
            bc.error("Neispravno zadani parametri!", ex);
            return false;
        }
        return true;
    }

    /**
     * Metoda preraèunava kune u eure.
     * @param kn BigDecimal iznos u kunama koji treba preraèunati u eure
     * @param exchangeRate BigDecimal  teèaj za koji se radi izraèun 
     * @return eur BigDecimal iznos u eurima
     */
    public BigDecimal toEur(BigDecimal kn, BigDecimal exchangeRate){
        BigDecimal eur= null;
        eur = kn.divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP);
        return eur;
    }

    public static void main(String[] args) {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4800981704.0"));
        batchParameters.setArgs(args);
        new BO670().run(batchParameters);
    }
}