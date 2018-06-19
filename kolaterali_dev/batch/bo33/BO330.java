package hr.vestigo.modules.collateral.batch.bo33;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo33.BO331.CollIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * Batch za kreiranje CSV datoteke za ispis kolaterala prema aktivnosti plasmana.
 * @author hrakis
 * 
 */
public class BO330 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo33/BO330.java,v 1.1 2008/10/31 11:51:48 hrakis Exp $";
	private BigDecimal col_cat_id, org_uni_id;
	private Date date_from, date_until;
	private String status, client_type;
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO33 pokrenut.");
		BO331 bo331 = new BO331(bc);
		
        // insertiranje eventa
		if(bo331.insertIntoEvent() == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// dohvat parametara
        if(!dohvatiParametre(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvaceni parametri.");
		
		// dohvat podataka
		CollIterator iter = bo331.selectCollaterals(col_cat_id, org_uni_id, date_from, date_until, status, client_type);
		if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvaceni podaci.");
		
	   	// stvaranje streamwritera
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
		String fileName = bc.getOutDir() + "/" + "vrsta_kol_plasman" + dateString + ".csv";
    	OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
    	bc.debug("Stvoren streamwriter.");
		
    	// zaspisivanje zaglavlja u izlaznu datoteku
    	streamWriter.write(formirajZaglavlje());
    	bc.debug("Zapisano zaglavlje.");
    	
    	// zapisivanje podataka u izlaznu datoteku
        int rb = 0;
    	while(iter.next())
        {
        	streamWriter.write(formirajRed(++rb, iter));
        }
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u datoteku.");
        
        // slanje maila
        YXY70.send(bc, "csv166", bc.getLogin(), fileName);
        bc.debug("Mail poslan.");
        
		// stvaranje marker datoteke
		new File(fileName + ".marker").createNewFile();
		bc.debug("Stvoren marker.");

		bc.debug("Obrada zavrsena.");
		return RemoteConstants.RET_CODE_SUCCESSFUL;
    }

	
	/**
	 * Metoda formira jedan red tablice za CSV i vraæa ga u obliku stringa.
	 * @param rb Redni broj retka
	 * @param iter Iterator s kolateralima
	 * @return red tablice u obliku stringa
	 */
	private String formirajRed(int rb, CollIterator iter) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(rb).append(";");
		buffer.append(isEmpty(iter.org_code()) + "-" + isEmpty(iter.org_name())).append(";");
		buffer.append(isEmpty(iter.register_no())).append(";");
		buffer.append(isEmpty(iter.cus_name())).append(";");
		buffer.append(isEmpty(iter.address())).append(";");
		buffer.append(isEmpty(iter.postoffice()) + " " + isEmpty(iter.city_name())).append(";");
		buffer.append(isEmpty(iter.request_no())).append(";");
		buffer.append(isEmpty(iter.acc_no())).append(";");
		buffer.append(isEmpty(iter.col_num())).append(";");
		buffer.append(isEmpty(iter.real_est_nomi_valu())).append(";");
		buffer.append(isEmpty(iter.cur_code())).append(";");
		buffer.append(isEmpty(iter.coll_type_name())).append("\n");
		return buffer.toString();
	}
	
	/**
	 * Metoda formira zaglavlje za CSV i vraæa ga u obliku stringa.
	 * @return zaglavlje u obliku stringa
	 */
	private String formirajZaglavlje()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Izvje\u0161\u0107e za period: ");
		buffer.append(DateUtils.getDDMMYYYY(date_from)).append(" - ").append(DateUtils.getDDMMYYYY(date_until));
		buffer.append(" po vrstama kolaterala prema statusu plasmana").append("\n\n");

		buffer.append("Redni broj").append(";");
        buffer.append("OJ").append(";");
        buffer.append("ID korisnika plasmana").append(";");
        buffer.append("Ime i prezime korisnika plasmana").append(";");
        buffer.append("Adresa korisnika plasmana").append(";");
        buffer.append("Po\u0161ta i grad korisnika plasmana").append(";");
        buffer.append("Broj zahtjeva").append(";");
        buffer.append("Partija plasmana").append(";");
        buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Tr\u017Ei\u0161na vrijednost kolaterala").append(";");
        buffer.append("Valuta kolaterala").append(";");
        buffer.append("Vrsta kolaterala").append("\n");
		return buffer.toString();
	}
	
	/**
	 * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
	 * Parametri se predaju u obliku "RB;col_cat_id;org_uni_id;date_from;date_until;status;client_type;X".
	 * Parametri col_cat_id i org_uni_id su opcionalni (staviti blank ako nisu potrebni).
	 * Moguæe vrijednosti za status su A,C,S. Moguæe vrijednosti za client_type su P,F.
	 * @return da li su dohvat i provjera uspješno završili
	 */
	private boolean dohvatiParametre(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 8)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
	     		if(bc.getArg(1).trim().equals("")) col_cat_id = null; else col_cat_id = new BigDecimal(bc.getArg(1));
	     		if(bc.getArg(2).trim().equals("")) org_uni_id = null; else org_uni_id = new BigDecimal(bc.getArg(2));
	     		date_from = DateUtils.parseDate(bc.getArg(3));
	     		date_until = DateUtils.parseDate(bc.getArg(4));
	     		status = bc.getArg(5).trim();
	     		client_type = bc.getArg(6).trim();
	     		if(!status.equals("A") && !status.equals("C") && !status.equals("S")) throw new Exception("Moguæe vrijednosti za status plasmana su A,C,S!");
	     		if(!client_type.equals("P") && !client_type.equals("F")) throw new Exception("Moguæe vrijednosti za vrstu klijenta su P,F!");
	    	}
	    	else if (brojParametara == 1)  // batch pozvan iz shella (radi testiranja)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
	     		col_cat_id = new BigDecimal("612223");
	     		org_uni_id = new BigDecimal("9253");
	     		date_from = DateUtils.parseDate("01.01.2000");
	     		date_until = DateUtils.parseDate("01.11.2008");
	     		status = "A";
	     		client_type = "P";
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
	
	private String isEmpty(String s)
	{
		if (s == null) return ""; else return s.trim();
	}
	private String isEmpty(BigDecimal bd)
    {
        if (bd == null) return ""; else return bd.toString();
    }
	
    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2640078003"));
        batchParameters.setArgs(args);
        new BO330().run(batchParameters);
    }
}