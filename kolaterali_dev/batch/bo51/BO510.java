package hr.vestigo.modules.collateral.batch.bo51;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo51.BO511.*;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * HNB rezervacije.
 * @author hrakis
 */
public class BO510 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo51/BO510.java,v 1.4 2010/03/24 13:16:22 hrakis Exp $";
    private BatchContext bc;
    private BO511 bo511;
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO510 pokrenut.");
        bo511 = new BO511(bc);
        this.bc = bc;
        
        // ubacivanje eventa
        BigDecimal eve_id = bo511.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
        
        // dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");

        // postavi indikator obrade na sve važeæe slogove
        if(!bo511.markBatchStart()) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Postavljeni indikatori obrade.");
        
        // dohvat podataka o plasmanima i njihovoj osiguranosti kolateralima
        Iter1 mainIter = bo511.selectLoans();
        if(mainIter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci.");

        // punjenje mape s dohvaæenim podacima
        TreeMap loans = new TreeMap();
        while(mainIter.next())
        {
            loans.put(mainIter.cus_acc_no(), new LoanData(mainIter.cus_acc_no(), mainIter.exposure_date(), mainIter.module_code(), mainIter.flag()));
        }
        mainIter.close();
        bc.debug("Mapa napunjena.");
        
        // naði sve plasmane koji su okvir i dodaj u novu listu plasmane iz okvira
        Vector frameLoans = new Vector();
        for(Iterator i = loans.values().iterator(); i.hasNext();)
        {
            LoanData loan = (LoanData)i.next();
            if(!isFrame(loan)) continue;  // ako plasman nije okvir, preskoèi ga

            Iter2 frameIter = bo511.selectFrameLoans(loan.cus_acc_no);  // dohvati sve plasmane iz okvira
            if(frameIter == null) return RemoteConstants.RET_CODE_ERROR;
            while(frameIter.next())
            {
                if(loans.containsKey(frameIter.cus_acc_no()))  // ako plasman iz okvira veæ postoji u mapi, osvježi postojeæi
                {
                    LoanData frameLoan = (LoanData)loans.get(frameIter.cus_acc_no());
                    if(loan.flag.equals("1") && !frameLoan.flag.equals("1")) frameLoan.flag = "1";  // ako okvir ima flag osiguranosti 1, a plasman iz okvira 0, dodijeli 1 plasmanu iz okvira
                }
                else  // ako plasman ne postoji, dodaj ga u novu listu
                {
                    frameLoans.add(new LoanData(frameIter.cus_acc_no(), frameIter.exposure_date(), null, loan.flag));
                }
            }
            frameIter.close();
        }
        
        // spoji mapu s listom plasmana iz okvira
        for(Iterator i = frameLoans.iterator(); i.hasNext();)
        {
            LoanData frameLoan = (LoanData)i.next();
            if(!loans.containsKey(frameLoan.cus_acc_no)) loans.put(frameLoan.cus_acc_no, frameLoan);
        }

        // stvaranje izlazne datoteke
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
        String fileName = bc.getOutDir() + "/" + "osigurane_i_neosigurane_partije_" + dateString + ".csv";
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena izlazna datoteka " + fileName);

        // zapisivanje zaglavlja u izlaznu datoteku
        streamWriter.write(getHeader());
        bc.debug("Zapisano zaglavlje.");
        
        // zapisivanje podataka u izlaznu datoteku i evidentiranje osiguranosti u tablicu CUSACC_IND_COLL
        for(Iterator i = loans.values().iterator(); i.hasNext();)
        {
            LoanData loan = (LoanData)i.next();
            bc.debug("cus_acc_no=" + loan.cus_acc_no);
            streamWriter.write(getRow(loan));  // zapiši u izlaznu datoteku
            processLoan(loan);  // evidentiraj osiguranost u tablicu CUSACC_IND_COLL
        }
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u datoteku.");
        
        // deaktiviraj neobraðene slogove
        if(!bo511.markBatchEnd()) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Neobraðeni slogovi deaktivirani.");

        // slanje maila
        bc.startStopWatch("sendMail");
        YXY70.send(bc, "csv223", bc.getLogin(), fileName);
        bc.stopStopWatch("sendMail");
        bc.debug("Mail poslan.");

        // stvaranje marker datoteke
        new File(fileName + ".marker").createNewFile();
        bc.debug("Stvoren marker.");

        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    /**
     * Metoda vraæa je li partija okvir.
     * @param loan Data objekt s podacima o plasmanu
     */
    private boolean isFrame(LoanData loan)
    {
        return loan.cus_acc_no.trim().length() == 10 && loan.cus_acc_no.startsWith("60") && loan.module_code.trim().equals("OKV"); 
    }
    
    /**
     * Metoda formira jedan red tablice za CSV i vraæa ga u obliku stringa.
     * @param loan Data objekt s podacima o plasmanu
     * @return red tablice u obliku stringa
     */
    private String getRow(LoanData loan) throws Exception
    {
        bc.startStopWatch("getRow");
        StringBuffer buffer = new StringBuffer();
        buffer.append(loan.cus_acc_no).append(";"); 
        buffer.append(loan.flag).append(";");
        buffer.append(loan.exposure_date).append(";");
        buffer.append("\n");
        bc.stopStopWatch("getRow");
        return buffer.toString();
    }

    /**
     * Metoda formira zaglavlje za CSV i vraæa ga u obliku stringa.
     * @return zaglavlje u obliku stringa
     */
    private String getHeader()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Partija plasmana").append(";");
        buffer.append("Flag").append(";");
        buffer.append("Datum stanja").append(";");
        buffer.append("\n");
        return buffer.toString();
    }
    
    /**
     * Metoda koja ažurira tablicu CUSACC_IND_COLL s podacima o osiguranosti plasmana
     * @param loan Data objekt s podacima o plasmanu
     */
    private void processLoan(LoanData loan) throws Exception
    {
        // dohvati dodatne podatke o partiji
        Vector vect = bo511.selectCustomerAccount(loan.cus_acc_no);
        if(vect == null)
        {
            bc.debug("Partija ne postoji u CUSTOMER_ACCOUNT!");
            return;
        }
        BigDecimal cus_acc_id = (BigDecimal)vect.get(0);
        String external_flag = (String)vect.get(1);
        
        // ako partija nije iz Siriusa, nema potrebe za ažuriranjem
        if(!external_flag.equals("0"))
        {
            bc.debug("Partija nije iz Siriusa!");
            return;
        }

        bc.beginTransaction();

        // dohvati podatke o važeæem indikatoru osiguranosti partije
        vect = bo511.selectCusaccIndColl(cus_acc_id);
        if(vect == null)  // ako zapis ne postoji, ubaci ga
        {
            bo511.insertIntoCusaccIndColl(cus_acc_id, loan.cus_acc_no, loan.exposure_date, loan.flag);
        }
        else  // ako zapis postoji, provjeri da li se indikator promijenio
        {
            BigDecimal cus_ind_col = (BigDecimal)vect.get(0);
            String coll_ind = (String)vect.get(1);
            if(coll_ind.equals(loan.flag))  // ako se indikator nije promijenio, ažuriraj samo indikator obrade
            {
                bo511.updateProcInd(cus_ind_col);
            }
            else  // ako se indikator promijenio, zatvori važeæi slog i dodaj novi
            {
                bo511.updateCusaccIndColl(cus_ind_col, loan.exposure_date);
                bo511.insertIntoCusaccIndColl(cus_acc_id, loan.cus_acc_no, loan.exposure_date, loan.flag);
            }
        }
        
        bc.commitTransaction();
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
        return true;
    }

    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2681741924"));
        batchParameters.setArgs(args);
        new BO510().run(batchParameters);
    }
}