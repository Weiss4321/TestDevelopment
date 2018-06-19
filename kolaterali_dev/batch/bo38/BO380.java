package hr.vestigo.modules.collateral.batch.bo38;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo38.BO381.StockIterator;
import hr.vestigo.modules.rba.util.SendMail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Vector;


/**
 *
 * Uèitavanje podataka za dionice i udjele u fondovima.
 * @author hrakis
 * 
 */
public class BO380 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo38/BO380.java,v 1.4 2009/04/23 11:51:27 hrakis Exp $";
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO380 pokrenut.");
		BO381 bo381 = new BO381(bc);
		
        // insertiranje eventa
		BigDecimal eve_id = bo381.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// provjera parametara
        if(!checkParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.");
		
		// provjeriti da li u tabeli COL_PROC za ovu vrstu obrade i current date postoji slog
		BigDecimal col_pro_id = null;
		String proc_status = null;
		Vector vect = bo381.selectColProId();
		if(vect == null) return RemoteConstants.RET_CODE_ERROR;
		if(vect.get(0) != null) col_pro_id = (BigDecimal)vect.get(0);
		if(vect.get(1) != null) proc_status = (String)vect.get(1);
		bc.debug("col_pro_id = " + col_pro_id);
		bc.debug("proc_status = " + proc_status);
		
		StockIterator iter = null;
		if(col_pro_id == null)  // ako obrada za današnji datum ne postoji, unesi zapis o zapoèetoj obradi i dohvati police osiguranja
		{
			col_pro_id = bo381.insertIntoColProc();
			iter = bo381.selectStocks(0, col_pro_id);
			if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		}
		else if(col_pro_id != null && proc_status.equals("0"))  // ako obrada postoji i status je '0', dohvati samo one police koje još nisu obuhvaæene obradom
		{
			iter = bo381.selectStocks(1, col_pro_id);
			if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		}
		else  // ako obrada postoji i status je '1', obrada se prekida sa RC=108 i zapisuje odgovarajuæa poruka u log
		{
			bc.error("Za current date obrada je pustena i uredno zavrsila!", new String[]{col_pro_id.toString(),proc_status});
			return RemoteConstants.RET_CODE_ERROR;
		}
		bc.debug("Dohvaceni podaci.");
		
		// stvaranje streamwritera za preuzete i nepreuzete dionice/udjele
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
		String fileNamePreuzeti = bc.getOutDir() + "/" + "vrp_preuzeti" + dateString + ".csv";
		String fileNameNepreuzeti = bc.getOutDir() + "/" + "vrp_nepreuzeti" + dateString + ".csv";
		OutputStreamWriter streamWriterPreuzeti = new OutputStreamWriter(new FileOutputStream(new File(fileNamePreuzeti)), "Cp1250");
    	OutputStreamWriter streamWriterNepreuzeti = new OutputStreamWriter(new FileOutputStream(new File(fileNameNepreuzeti)), "Cp1250");
    	bc.debug("Stvoreni streamwriteri.");
    	
    	// zapisivanje zaglavlja u izlazne datoteke
    	streamWriterPreuzeti.write(getHeader(true));
    	streamWriterNepreuzeti.write(getHeader(false));
    	bc.debug("Zapisana zaglavlja.");
    	
    	// obrada dohvaæenih dionica/udjela
    	String retCode = RemoteConstants.RET_CODE_SUCCESSFUL;
    	while(iter.next())
		{
    		StockData stock = new StockData();
    		fillStockDataFromIterator(stock, iter);
    		bc.debug("Pocetak obrade " + stock);
    		
    		// dohvat potrebnih podataka o dionici/udjelu
    		boolean retVal = true;
    		retVal = retVal && bo381.selectAdditionalStockInfo(stock);
    		retVal = retVal && bo381.selectCollOwner(stock);
    		retVal = retVal && bo381.selectLoanOwner(stock);
    		retVal = retVal && bo381.selectCusAccExposure(stock);
    		if(!retVal)  // ako nisu uspješno dohvaæeni svi potrebni podaci, preskoèi ovaj zapis
    		{
    			streamWriterNepreuzeti.write(getRow(stock,false));
    			retCode = RemoteConstants.RET_CODE_WARNING;
    			continue;
    		}
    		
    		// punjenje tablica i knjiženje trenutnog kolaterala
    		bc.beginTransaction();
    		retVal = true;
    		retVal = retVal && bo381.insertCollHead(stock);
    		retVal = retVal && bo381.insertCollVrp(stock);
    		retVal = retVal && bo381.insertCollOwner(stock);
    		retVal = retVal && bo381.insertCollHfPrior(stock);
    		retVal = retVal && bo381.insertLoanBeneficiary(stock);
    		retVal = retVal && bo381.insertCollListQ(stock);
    		retVal = retVal && bo381.collPosting(stock);
    		retVal = retVal && bo381.insertIntoInDataDwhItem(col_pro_id, stock);
    		if(retVal)  // ako je sve uspješno obavljeno, izvrši commit
    		{
    			bc.commitTransaction();
    			streamWriterPreuzeti.write(getRow(stock,true));
    		}
    		else  // inaèe napravi rollback
    		{
    			bc.rollbackTransaction();
    			streamWriterNepreuzeti.write(getRow(stock,false));
    			retCode = RemoteConstants.RET_CODE_WARNING;
    		}
		}
		streamWriterPreuzeti.flush();
		streamWriterPreuzeti.close();
		streamWriterNepreuzeti.flush();
		streamWriterNepreuzeti.close();
        bc.debug("Podaci zapisani u datoteke.");
    	
        // zapiši broj obraðenih slogova u tablicu COL_PROC
        if(!bo381.updateColProc(col_pro_id)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Informacija o izvedenoj obradi spremljena u tablicu COL_PROC.");
        
        // dohvat mail adresa primatelja izvješæa
        String mailAddresses = bo381.selectMailAddresses();  
        if(mailAddresses == null) return RemoteConstants.RET_CODE_ERROR;

        // slanje maila
        int mailFlag = new SendMail().send(bc, "", "", mailAddresses, "", "", "Preuzete i nepreuzete dionice i udjeli", "", null, fileNamePreuzeti+"///"+fileNameNepreuzeti);
        if(mailFlag != 0)
        {
        	bc.error("Mail nije poslan!", new String[]{Integer.toString(mailFlag)});
        	return RemoteConstants.RET_CODE_ERROR;
        }
        bc.debug("Mail poslan na " + mailAddresses + " .");
        
		// stvaranje marker datoteka
		new File(fileNamePreuzeti + ".marker").createNewFile();
		new File(fileNameNepreuzeti + ".marker").createNewFile();
		bc.debug("Stvoreni markeri.");
		
		bc.debug("Obrada zavrsena.");
		return retCode;
    }
	

	/**
	 * Metoda koja puni data objekt podacima iz iteratora.
	 * @param stock Objekt s podacima o dionici
	 * @param iter Iterator
	 */
	private void fillStockDataFromIterator(StockData stock, StockIterator iter) throws Exception
	{
		stock.rbr = iter.rbr();
		stock.coll_type_code = iter.coll_type_code().trim();
		stock.isin = iter.isin();
		stock.stop_sell_ind = iter.stop_sell_ind();
		stock.stop_sell_period = iter.stop_sell_period();
		stock.currency_clause = iter.currency_clause();
		stock.rba_eligibility = iter.rba_eligibility();
		stock.num_of_sec = iter.num_of_sec();
		stock.owner_reg_no = iter.owner_reg_no();
		stock.hf_amount = iter.hf_amount();
		stock.hf_currency = iter.hf_currency();
		stock.hf_type = iter.hf_type();
		stock.hf_inst = iter.hf_inst();
		stock.hf_from = iter.hf_from();
		stock.hf_until = iter.hf_until();
		stock.hf_date_reciv = iter.hf_date_reciv();
		stock.loan_reg_no = iter.loan_reg_no();
		stock.cus_acc_no = iter.cus_acc_no();
		stock.veh_con_num = iter.veh_con_num();
		stock.cur_id_ref = iter.cur_id_ref();
		stock.mid_rate_ref = iter.mid_rate_ref();
		
		if("3DIONICE".equals(stock.coll_type_code))
		{
			stock.is_stock = true;
			stock.col_cat_id = new BigDecimal("613223");
			stock.coll_type_id = new BigDecimal("28777");
		}
		else
		{
			stock.is_stock = false;
			stock.col_cat_id = new BigDecimal("622223");
			stock.coll_type_id = new BigDecimal("29777");
		}
	}
	
	/**
	 * Metoda formira jedan red tablice za CSV i vraæa ga u obliku stringa.
	 * @param stock Objekt koji sadrži podatke o dionici
	 * @param preuzeti true ako se formira zaglavlje za preuzete dionice, false za nepreuzete
	 * @return red tablice u obliku stringa
	 */
	private String getRow(StockData stock, boolean preuzeti)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(stock.rbr).append(";");
		if(preuzeti) buffer.append(isEmpty(stock.col_num)).append(";");
		if(preuzeti) buffer.append(isEmpty(stock.isin)).append(";");
        buffer.append(isEmpty(stock.owner_reg_no)).append(";");
        if(preuzeti) buffer.append(isEmpty(stock.owner_name)).append(";");
        buffer.append(isEmpty(stock.cus_acc_no)).append(";");
        buffer.append(isEmpty(stock.loan_reg_no)).append(";");
        if(preuzeti) buffer.append(isEmpty(stock.loan_name)).append("\n");
        if(!preuzeti) buffer.append(isEmpty(stock.error)).append("\n");
		return buffer.toString();
	}
	
	/**
	 * Metoda formira zaglavlje za CSV i vraæa ga u obliku stringa.
	 * @param preuzeti true ako se formira zaglavlje za preuzete dionice, false za nepreuzete
	 * @return zaglavlje u obliku stringa
	 */
	private String getHeader(boolean preuzeti)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Redni broj").append(";");
		if(preuzeti) buffer.append("\u0160ifra kolaterala").append(";");
		if(preuzeti) buffer.append("ISIN").append(";");
        buffer.append("Interni MB vlasnika kolaterala").append(";");
        if(preuzeti) buffer.append("Naziv vlasnika kolaterala").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("Interni MB korisnika plasmana").append(";");
        if(preuzeti) buffer.append("Naziv korisnika plasmana").append("\n");
        if(!preuzeti) buffer.append("Razlog nepreuzimanja").append("\n");
		return buffer.toString();
	}
	
	private String isEmpty(String s)
	{
		if (s == null) return ""; else return s.trim();
	}

	/**
	 * Metoda provjerava broj i ispravnost ulaznih parametara.
	 * @return da li je provjera prošla
	 */
	private boolean checkParameters(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 1)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2780545003"));
        batchParameters.setArgs(args);
        new BO380().run(batchParameters);
    }
}