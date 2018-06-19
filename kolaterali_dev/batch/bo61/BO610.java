package hr.vestigo.modules.collateral.batch.bo61;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo61.BO611.Iter1;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
 
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


/**
 * Lista WCOV
 * @author hramkr
 */
public class BO610 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo61/BO610.java,v 1.4 2015/04/02 12:15:18 hrakis Exp $";
    private BatchContext bc;
    private Date reportDate;
    private BigDecimal colCatId;
    private String colNum;
    private String exceptionType;
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO610 pokrenut.");
        BO611 bo611 = new BO611(bc);
        this.bc = bc;
        
        // ubacivanje eventa
        BigDecimal eve_id = bo611.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
         
        // dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");
        
        // stvaranje izlazne datoteke
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        if (reportDate != null) dateString = reportDate.toString();
        String fileName = bc.getOutDir() + "/" +"ListaWCOV_" + dateString +  ".csv";
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName);
        
        // zapisivanje zaglavlja u izlaznu datoteku
        writeHeaderRow(streamWriter);
        bc.debug("Zapisano zaglavlje.");
        
        // dohvat podataka
        Iter1 iter = bo611.selectData(reportDate, colCatId, colNum, exceptionType);
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci." + iter);
         
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
        String rpt_code = "csv241";
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
        buffer.append(iter.col_num()).append(";");
        buffer.append(iter.code_char()).append(";");
        buffer.append(iter.mcv_amount()).append(";");
        buffer.append(iter.ponder_value()).append(";");
        buffer.append(iter.other_mrtg()).append(";");
        buffer.append(iter.wca_amount()).append(";");
        buffer.append(iter.nd_eligibility()).append(";");
        buffer.append(iter.process_date()).append(";");
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
        buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Valuta").append(";");
        buffer.append("Tr\u017Ei\u0161na vrijednost").append(";");
        buffer.append("Ponder").append(";");
        buffer.append("Suma tu\u0111ih hipoteka").append(";");
        buffer.append("WCOV u orig.valuti kolaterala").append(";");
        buffer.append("ND prihvatljivost").append(";");
        buffer.append("Datum izra\u010Duna").append(";");
        buffer.append("\n");
        sw.write(buffer.toString());
        bc.stopStopWatch("writeHeaderRow");
    }
    
    /**
     * Metoda dohvaca ulazne parametre, te provjerava njihov broj i ispravnost.
     * Parametri se predaju u obliku "RB;date;col_cat_id;col_num".
     * @return da li su dohvat i provjera uspjesno završili
     */
    private boolean getParameters(BatchContext bc)
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
            if(brojParametara == 5)
            {
                if(bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                if (bc.getArg(1).trim().equals("")) reportDate = null; else reportDate = Date.valueOf(bc.getArg(1).trim());
                if (bc.getArg(2).trim().equals("")) colCatId = null; else colCatId = new BigDecimal(bc.getArg(2).trim());
                if (bc.getArg(3).trim().equals("")) colNum = null; else colNum = bc.getArg(3).trim().toUpperCase();
                exceptionType = bc.getArg(4).trim();
                
                bc.debug("Parametar 1" + reportDate +". = " + reportDate);
                bc.debug("Parametar 2" + colCatId +". = " + colCatId);
                bc.debug("Parametar 3" + colNum +". = " + colNum);
 
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4639721704"));
        batchParameters.setArgs(args);
        new BO610().run(batchParameters);
    }
}