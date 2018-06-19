package hr.vestigo.modules.collateral.batch.bo39;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo39.BO391.ChangesIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;


/**
 *
 * Izvješæe o povijesti promjena na kolateralima.
 * @author hrakis
 * 
 */
public class BO390 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo39/BO390.java,v 1.4 2014/03/10 13:29:10 hrakis Exp $";
	private BigDecimal org_uni_id, use_id;
	private Date date_from, date_until;

	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO390 pokrenut.");
		BO391 bo391 = new BO391(bc);

        // insertiranje eventa
		BigDecimal eve_id = bo391.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");

		// dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.");

		// dohvat šifre i imena zadane organizacijske jedinice
		Vector vect = bo391.selectOrgUnit(org_uni_id);
		if(vect == null) return RemoteConstants.RET_CODE_ERROR;
		String org_code = (String)vect.get(0);
		String org_name = (String)vect.get(1);
		bc.debug("org_code = " + org_code);
		bc.debug("org_name = " + org_name);

		// dohvat povijesti promjena
		ChangesIterator iter = bo391.selectChanges(org_uni_id, use_id, date_from, date_until);
		if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvaceni podaci.");

		// stvaranje streamwritera
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
		String fileName = bc.getOutDir() + "/" + "povijest_promjena" + dateString + ".csv";
		OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
    	bc.debug("Stvoren streamwriter.");

    	// zapisivanje zaglavlja u izlaznu datoteku
    	streamWriter.write(getHeader(org_code,org_name));
    	bc.debug("Zapisano zaglavlje.");

    	// zapisivanje podataka u izlaznu datoteku
    	int index = 1;
    	while(iter.next())
		{
			bc.debug("login=" + iter.login() + ",income_date=" + iter.income_date() + ",col_num=" + iter.col_num());
    		streamWriter.write(getRow(iter, index++));
		}
		streamWriter.flush();
		streamWriter.close();
        bc.debug("Podaci zapisani u datoteku.");

        // slanje maila
        YXY70.send(bc, "csv185", bc.getLogin(), fileName);

		// stvaranje marker datoteke
		new File(fileName + ".marker").createNewFile();
		bc.debug("Stvoren marker.");

		bc.debug("Obrada zavrsena.");
		return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
	
	/**
	 * Metoda formira jedan red tablice za CSV i vraæa ga u obliku stringa.
	 * @param iter Iterator s promjenama
	 * @param index Redni broj retka na listi
	 * @return red tablice u obliku stringa
	 */
	private String getRow(ChangesIterator iter, int index) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(index).append(";");
		buffer.append(isEmpty(iter.login())).append(";");
		buffer.append(isEmpty(iter.user_name())).append(";");
		buffer.append(isEmpty(iter.income_date())).append(";");
		buffer.append(isEmpty(iter.coll_type_name())).append(";");
		buffer.append(isEmpty(iter.col_num())).append(";");
		buffer.append(isEmpty(iter.v_login())).append(";");
		buffer.append(isEmpty(iter.v_user_name())).append(";");
		buffer.append(isEmpty(iter.release_date())).append("\n");
		return buffer.toString();
	}
	
	/**
	 * Metoda formira zaglavlje za CSV i vraæa ga u obliku stringa.
	 * @param org_code Šifra organizacijske jedinice
	 * @param org_name Naziv organizacijske jedinice
	 * @return zaglavlje u obliku stringa
	 */
	private String getHeader(String org_code, String org_name)
	{
		StringBuffer buffer = new StringBuffer();
		// prvi red
		buffer.append("Izvje\u0161\u0107e za period: ");
		buffer.append(DateUtils.getDDMMYYYY(date_from)).append(" - ").append(DateUtils.getDDMMYYYY(date_until));
		buffer.append(" za OJ: ").append(org_code).append(" ").append(org_name).append("\n");
		// drugi red
		buffer.append("Redni broj").append(";");
		buffer.append("Login").append(";");
		buffer.append("Referent izmjene").append(";");
		buffer.append("Datum izmjene").append(";");
		buffer.append("Vrsta kolaterala").append(";");
		buffer.append("\u0160ifra kolaterala").append(";");
		buffer.append("Login").append(";");
		buffer.append("Referent verifikacije").append(";");
        buffer.append("Datum verifikacije").append("\n");
		return buffer.toString();
	}
	
	private String isEmpty(String s)
	{
		if (s == null) return ""; else return s.trim();
	}
	private String isEmpty(Date d)
	{
		if (d == null) return ""; else return DateUtils.getDDMMYYYY(d);
	}

	/**
	 * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
	 * Parametri se predaju u obliku "RB;org_uni_id;use_id;date_from;date_until;X".
	 * Parametar use_id je opcionalan (staviti blank ako nije potreban).
	 * @return da li su dohvat i provjera uspješno završili
	 */
	private boolean getParameters(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 6)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
	     		org_uni_id = new BigDecimal(bc.getArg(1));
	     		if(bc.getArg(2).trim().equals("")) use_id = null; else use_id = new BigDecimal(bc.getArg(2));
	     		date_from = DateUtils.parseDate(bc.getArg(3));
	     		date_until = DateUtils.parseDate(bc.getArg(4));
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2793903003"));
        batchParameters.setArgs(args);
        new BO390().run(batchParameters);
    }
}