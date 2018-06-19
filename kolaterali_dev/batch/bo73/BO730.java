package hr.vestigo.modules.collateral.batch.bo73;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo73.BO731.CollateralIterator;
import hr.vestigo.modules.rba.common.yrxJ.YRXJ0;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Properties;


/**
 * Kreiranje datoteke s kolateralima za ComDebts
 * @author hrakis
 */
public class BO730 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo73/BO730.java,v 1.7 2013/07/03 12:40:37 hrakis Exp $";

    private BatchContext bc;
    private BO731 bo731;


    public String executeBatch(BatchContext bc) throws Exception
    {
        bc.debug("BO730.executeBatch pokrenut.");
        
        // inicijalizacija potrebnih varijabli
        this.bc = bc;
        this.bo731 = new BO731(bc);
        
        // evidentiranje eventa
        bo731.insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if(!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;
        
        // dohvat kolaterala
        CollateralIterator iter = bo731.selectCollaterals();
        
        // stvaranje izlazne datoteke
        String dir = bc.getOutDir() + "/";
        String dateString = new SimpleDateFormat("yyyyMMdd").format(bc.getExecStartTime());
        String fileName = "ST_KOL_SIR_" + dateString;
        String fileNamePath = dir + fileName;
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileNamePath)), "Cp852");
        bc.debug("Stvorena izlazna datoteka " + fileNamePath);
        
        // zapisivanje u datoteku
        int count = 0;
        while(iter.next())
        {
            CollateralData data = getDataFromIterator(iter);
            bo731.selectCollSubtypeCode(data);  // dohvat podvrste kolaterala
            bo731.selectCollOwner(data);        // dohvat glavnog vlasnika kolaterala
            bo731.selectCollAddress(data);      // dohvat adrese kolaterala
            
            bc.startStopWatch("writeRow");
            String row = getDetailsRow(data);
            streamWriter.write(row);
            bc.stopStopWatch("writeRow");
            count++;
        }
        
        iter.close();
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u izlaznu datoteku.");
        bc.info("Broj zapisa: " + count);
        bc.userLog("Ukupno dohvaceno slogova: " + count);
        bc.userLog("Uspjesno obradjeno: " + count);
        bc.userLog("Neuspjesno obradjeno: " + 0);

        // stvaranje marker datoteke
        new File(fileNamePath + ".marker").createNewFile();
        bc.debug("Stvoren marker.");
        
        // slanje datoteke u drugi sustav
		bc.startStopWatch("fileTransfer");
        YRXJ0 yrxj0 = new YRXJ0(bc);
        Properties properties = new Properties();
        properties.setProperty("fileName", fileNamePath);
        yrxj0.transferFile("CD01", properties);
		bc.stopStopWatch("fileTransfer");
        bc.debug("Datoteka je poslana.");
        bc.userLog("Datoteka je poslana.");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }

    
    /**
     * Metoda kreira objekt i puni ga s podacima iz iteratora.
     * @param iter iterator s podacima
     * @return popunjeni objekt
     */
    private CollateralData getDataFromIterator(CollateralIterator iter) throws Exception
    {
        CollateralData data = new CollateralData();
        data.col_hea_id = iter.col_hea_id();
        data.col_num = iter.col_num();
        data.cus_acc_no = iter.cus_acc_no();
        data.col_cat_id = iter.col_cat_id();
        data.coll_cat_code = iter.coll_cat_code();
        data.coll_type_code = iter.coll_type_code();
        data.real_est_nomi_valu = iter.real_est_nomi_valu();
        if(data.real_est_nomi_valu == null) data.real_est_nomi_valu = new BigDecimal("0.00");
        data.real_est_nomi_cur = iter.real_est_nomi_cur();
        return data;
    }
    
    /**
     * Metoda formira jedan red izlazne datoteke i vraæa ga u obliku stringa. 
     * @param data objekt s podacima
     * @return formirani string
     */
    private String getDetailsRow(CollateralData data) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(fix(data.col_num, 18));
        buffer.append(fix(data.owner_register_no, 10));
        buffer.append(fix(data.cus_acc_no, 20));
        buffer.append(fix(data.coll_cat_code, 4));
        buffer.append(fix(data.coll_type_code, 18));
        buffer.append(fix(data.coll_subtype_code, 18));
        buffer.append(fix(data.real_est_nomi_valu, 20));
        buffer.append(fix(data.real_est_nomi_cur, 3));
        buffer.append(fix(data.street, 140));
        buffer.append(fix(data.housenr, 10));
        buffer.append(fix(data.city_code, 20));
        buffer.append(fix(data.postal_code, 5));
        return buffer.append("\n").toString();
    }
    
    /**
     * Metoda na temelju zadanog objekta formira string fiksne duljine. 
     * @param obj objekt
     * @param length duljina
     * @return formirani string
     */
    private String fix(Object obj, int length)
    {
        String str = "";
        boolean leftAlign = true;
        if(obj != null)
        {
            str = obj.toString().trim();
            if(obj instanceof BigDecimal) leftAlign = false;
        }
        return String.format("%1$" + (leftAlign ? "-" : "") + length + "." + length + "s", str);
    }
    
    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * Parametri se predaju u formatu: <code>bank_sign</code>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters()
    {
        try
        {
            if(bc.getArgs().length != 1) throw new Exception("Neispravan broj parametara!");

            bc.info("Parametri predani obradi:");
            bc.info("Oznaka banke = " + bc.getArg(0));
            
            bc.userLog("Parametri predani obradi:");
            bc.userLog("Oznaka banke = " + bc.getArg(0));

            if(!bc.getArg(0).equals("RB")) throw new Exception("Bank sign mora biti 'RB'!");
            bc.setBankSign(bc.getArg(0));
        }
        catch(Exception ex)
        {
            bc.error("Neispravno zadani parametri! Parametri se predaju u formatu 'bank_sign'!", ex);
            return false;
        }
        return true;
    }
    
    
    public static void main(String[] args)
    {
        BatchParameters bp = new BatchParameters(new BigDecimal("5851699704"));
        bp.setArgs(args);
        new BO730().run(bp);
    }
}