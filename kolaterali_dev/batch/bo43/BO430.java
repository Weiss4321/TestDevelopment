package hr.vestigo.modules.collateral.batch.bo43;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo43.BO431.CollIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.SendMail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;


/**
 *
 * Izvješæe za rekonsilijaciju.
 * @author hrakis
 * 
 */
public class BO430 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo43/BO430.java,v 1.4 2012/03/02 10:10:45 hramkr Exp $";
	private Date load_date;
	private String indikator_arhive;
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO430 pokrenut.");
		BO431 bo431 = new BO431(bc);
		
        // insertiranje eventa
		BigDecimal eve_id = bo431.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.; load_date ; indikator arhive; " + load_date + "; " + indikator_arhive);
		
		// dohvat podataka
		CollIterator iter = bo431.selectCollaterals(load_date, indikator_arhive);
		if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvaceni podaci.");
		
		// stvaranje streamwritera
		String dateString = new SimpleDateFormat("yyyyMMdd").format(load_date);
		String fileName = bc.getOutDir() + "/" + "VRP_kolaterali_" + dateString + ".csv";
		OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
    	bc.debug("Stvoren streamwriter.");
    	
    	// zapisivanje zaglavlja u izlaznu datoteku
    	streamWriter.write(getHeader());
    	bc.debug("Zapisano zaglavlje.");
    	
    	// zapisivanje podataka u izlaznu datoteku
    	int index = 1;
    	while(iter.next())
		{
    		bc.debug("col_num=" + iter.col_num());
    		streamWriter.write(getRow(iter, index++));
		}
		streamWriter.flush();
		streamWriter.close();
        bc.debug("Podaci zapisani u datoteku.");
    	
        // slanje maila
        YXY70.send(bc, "csv206", bc.getLogin(), fileName);
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
	 * @param index Redni broj retka na listi
	 * @return red tablice u obliku stringa
	 */
	private String getRow(CollIterator iter, int index) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(index).append(";");
		buffer.append(isEmpty(iter.col_num())).append(";");
		buffer.append(isEmpty(iter.coll_type_name())).append(";");
		buffer.append(isEmpty(iter.isin())).append(";");
		buffer.append(isEmpty(iter.owner_id())).append(";");
		buffer.append(isEmpty(iter.owner_name())).append(";");
		buffer.append(isEmpty(iter.num_of_sec())).append(";");
		buffer.append(isEmpty(iter.nominal_amount())).append(";");
		buffer.append(isEmpty(iter.valuta())).append(";");
		buffer.append(isEmpty(iter.market_value())).append(";");
		buffer.append(isEmpty(iter.cus_acc_no())).append(";");
		buffer.append(isEmpty(iter.request_no())).append(";");
		buffer.append(isEmpty(iter.cust_id())).append(";");
		buffer.append(isEmpty(iter.cust_name())).append("\n");
		return buffer.toString();
	}
	
	/**
	 * Metoda formira zaglavlje za CSV i vraæa ga u obliku stringa.
	 * @return zaglavlje u obliku stringa
	 */
	private String getHeader()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Redni broj").append(";");
		buffer.append("\u0160ifra kolaterala").append(";");
		buffer.append("Vrsta").append(";");
		buffer.append("ISIN").append(";");
		buffer.append("ID vlasnika kolaterala").append(";");
		buffer.append("Vlasnik kolaterala").append(";");
		buffer.append("Koli\u010Dina/Ukupna nominala za obv").append(";");
		buffer.append("Nominalna vrijednost").append(";");
		buffer.append("Valuta").append(";");
		buffer.append("Ukupna vrijednost").append(";");
		buffer.append("Partija plasmana").append(";");
		buffer.append("Broj zahtjeva").append(";");
		buffer.append("ID vlasnika plasmana").append(";");
        buffer.append("Vlasnik plasmana").append("\n");
		return buffer.toString();
	}
	
	private String isEmpty(String s)
	{
		if (s == null) return ""; else return s.trim();
	}
	private String isEmpty(BigDecimal bd)
	{
		if (bd == null) return ""; else return bd.toString();
	}

	/**
	 * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
	 * Parametri se predaju u obliku "RB;load_date;X".
	 * @return da li su dohvat i provjera uspješno završili
	 */
	private boolean getParameters(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 3)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
	     		load_date = DateUtils.parseDate(bc.getArg(1));
	     		indikator_arhive = bc.getArg(2);
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("31507192"));
        batchParameters.setArgs(args);
        new BO430().run(batchParameters);
    }
}