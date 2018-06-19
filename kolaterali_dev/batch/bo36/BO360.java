package hr.vestigo.modules.collateral.batch.bo36;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo36.BO361.InsurancePoliciesIterator;
import hr.vestigo.modules.collateral.common.yoyF.YOYF0;
import hr.vestigo.modules.collateral.common.yoyF.YOYFData;
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
 * Promjena statusa po policama životnog osiguranja (POK)
 * @author hrakis
 * 
 */
public class BO360 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo36/BO360.java,v 1.5 2014/03/10 13:28:22 hrakis Exp $";
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO360 pokrenut.");
		BO361 bo361 = new BO361(bc);
		
        // insertiranje eventa
		BigDecimal eve_id = bo361.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// provjera parametara
        if(!checkParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.");
		
	    // dohvat parametra odgode deaktivacije police osiguranja
        int delay = bo361.selectDeactivationDelay();
		
		// provjeriti da li u tabeli COL_PROC za ovu vrstu obrade i current date postoji slog
		BigDecimal col_pro_id = null;
		String proc_status = null;
		Vector vect = bo361.selectColProId();
		if(vect == null) return RemoteConstants.RET_CODE_ERROR;
		if(vect.get(0) != null) col_pro_id = (BigDecimal)vect.get(0);
		if(vect.get(1) != null) proc_status = (String)vect.get(1);
		bc.debug("col_pro_id = " + col_pro_id);
		bc.debug("proc_status = " + proc_status);
		
		InsurancePoliciesIterator iter = null;
		if(col_pro_id == null)  // ako obrada za današnji datum ne postoji, unesi zapis o zapoèetoj obradi i dohvati police osiguranja
		{
			col_pro_id = bo361.insertIntoColProc();
			iter = bo361.selectInsurancePolicies(0, col_pro_id, delay);
			if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		}
		else if(col_pro_id != null && proc_status.equals("0"))  // ako obrada postoji i status je '0', dohvati samo one police koje još nisu obuhvaæene obradom
		{
			iter = bo361.selectInsurancePolicies(1, col_pro_id, delay);
			if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		}
		else  // ako obrada postoji i status je '1', obrada se prekida sa RC=108 i zapisuje odgovarajuæa poruka u log
		{
			bc.error("Za current date obrada je pustena i uredno zavrsila!", new String[]{col_pro_id.toString(),proc_status});
			return RemoteConstants.RET_CODE_ERROR;
		}
		bc.debug("Dohvaceni podaci.");
		
		// stvaranje streamwritera i zapisivanje zaglavlja u izlaznu datoteku
		String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(bc.getExecStartTime());
		String shortFileName = "pok_istekli_" + dateString + ".csv";
		String fileName = bc.getOutDir() + "/" + shortFileName;
    	OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
    	bc.debug("Stvoren streamwriter.");
    	streamWriter.write(getHeader());
    	bc.debug("Zapisano zaglavlje.");
    	
    	// obrada polica osiguranja
    	int index = 1;
    	while(iter.next())
		{
			bc.debug("Pocetak obrade police osiguranja " + iter.col_ins_id());
			bc.beginTransaction();
			
            YOYFData data = new YOYFData();  // common za bilježenje povijesti promjena
            data.col_ins_id = iter.col_ins_id();
            data.use_id = new BigDecimal(1);
            data.org_uni_id = new BigDecimal(53253);
            data.eve_id = eve_id;
            YOYF0 yoyF0 = new YOYF0(bc, data);
            yoyF0.selectOldState();  // dohvati staro stanje
            
			if(!bo361.updatePolicyStatus(iter.col_ins_id())) return RemoteConstants.RET_CODE_ERROR;  // promijeni status police iz A-aktivna u I-istekla
			
            yoyF0.selectNewState();  // dohvati novo stanje
            yoyF0.insertIntoIpChgHistory();   // zapiši deaktivaciju u povijest promjena
			if(!bo361.insertIntoInDataDwhItem(col_pro_id, isEmpty(iter.ip_paid_until()), iter.col_hea_id())) return RemoteConstants.RET_CODE_ERROR;  // ubaci zapis o obraðenoj polici u tablicu IN_DATA_DWH_ITEM
			
			bc.commitTransaction();
        	streamWriter.write(getRow(iter, index++));
		}
		streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u datoteku.");
    	
        // zapiši broj obraðenih slogova u tablicu COL_PROC
        if(!bo361.updateColProc(col_pro_id)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Informacija o izvedenoj obradi spremljena u tablicu COL_PROC.");
        
        // slanje maila
        YXY70.send(bc, "csv175", bc.getLogin(), fileName);
        
		// stvaranje marker datoteke
		new File(fileName + ".marker").createNewFile();
		bc.debug("Stvoren marker.");
		
		bc.debug("Obrada zavrsena.");
		return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
	
	/**
	 * Metoda formira jedan red tablice za CSV i vraæa ga u obliku stringa.
	 * @param iter Iterator s policama osiguranja
	 * @param index Redni broj retka na listi
	 * @return red tablice u obliku stringa
	 */
	private String getRow(InsurancePoliciesIterator iter, int index) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append(index).append(";");
		buffer.append(isEmpty(iter.col_num())).append(";");
		buffer.append(isEmpty(iter.int_pol_type_name())).append(";");
		buffer.append(isEmpty(iter.ip_code())).append(";");
		buffer.append(isEmpty(iter.ip_valid_from())).append(";");
		buffer.append(isEmpty(iter.ip_valid_until())).append(";");
		buffer.append(isEmpty(iter.ip_paid_from())).append(";");
		buffer.append(isEmpty(iter.ip_paid_until())).append("\n");
		return buffer.toString();
	}
	
	/**
	 * Metoda formira zaglavlje za CSV i vraæa ga u obliku stringa.
	 * @return zaglavlje u obliku stringa
	 */
	private String getHeader() throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Redni broj").append(";");
		buffer.append("\u0160ifra kolaterala").append(";");
        buffer.append("Vrsta police").append(";");
        buffer.append("Broj police").append(";");
        buffer.append("Datum od kad vrijedi polica").append(";");
        buffer.append("Datum do kad vrijedi polica").append(";");
        buffer.append("Datum od kad je pla\u0107ena premija").append(";");
        buffer.append("Datum do kad je pla\u0107ena premija").append("\n");
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2772696003"));
        batchParameters.setArgs(args);
        new BO360().run(batchParameters);
    }
}