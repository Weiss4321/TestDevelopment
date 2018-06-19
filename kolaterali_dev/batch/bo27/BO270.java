package hr.vestigo.modules.collateral.batch.bo27;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo27.BO271.Iter;
import hr.vestigo.modules.rba.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * Podloga iz kolateral modula za PKR rezervacije.
 * @author hrakis
 * 
 */
public class BO270 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo27/BO270.java,v 1.4 2017/04/25 11:33:38 hrakis Exp $";
	private String inDir, outDir;
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO270 pokrenut.");
		
		String timestamp = new StringBuffer(new Timestamp(System.currentTimeMillis()).toString().substring(0,19)).deleteCharAt(16).deleteCharAt(13).deleteCharAt(10).deleteCharAt(7).deleteCharAt(4).toString();
		BO271 bo271 = new BO271(bc);
		
        // insertiranje eventa
		BigDecimal eve_id = bo271.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// provjera parametara
        if(!provjeriParametre(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri provjereni.");
		
		// pronaði datum zadnjeg RBA ponderiranog izraèuna pokrivenosti bez rezanja na iznos izloženosti
		Date value_date = bo271.selectMaxValueDate();
		if(value_date == null) return RemoteConstants.RET_CODE_ERROR;
		String value_date_string = new SimpleDateFormat("yyyyMMdd").format(value_date);
		bc.debug("Dohvacen max. datum.");
		
		// pronaði ID obrade
		BigDecimal col_pro_id = bo271.selectColProId(value_date);
		if(col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvacen ID obrade.");
		
		// dohvati datum zadnje datoteke i provjeri da li je za taj datum datoteka kreirana
		Date datum = bo271.selectDwhStatusDate();
		if(datum == null) return RemoteConstants.RET_CODE_ERROR;
		if(DateUtils.whoIsOlder(datum, value_date) == 0)
		{
			bc.error("Datoteka je vec kreirana za zadani datum!", new String[]{datum.toString()});
			return RemoteConstants.RET_CODE_ERROR;
		}
		
		// dohvati podatke
		Iter iter = bo271.selectData(col_pro_id);
		if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvaceni podaci.");
		
    	// zapisivanje podataka u izlaznu datoteku
		String fileName = outDir + "/" + "pkr_" + timestamp + ".txt";
    	OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
		StringBuffer buffer;
    	while(iter.next())
		{
    		String cus_acc_no = iter.cus_acc_no();
    		// u datoteku se ukljuèuju samo oni plasmani koji imaju vrstu posla = 50, odnosno oni èija je partija oblika XXX-50-YYYYYYY 
    		if(cus_acc_no != null && cus_acc_no.length() > 7 && cus_acc_no.substring(3, 7).equals("-50-"))
    		{
    			buffer = new StringBuffer();
    			buffer.append(value_date_string).append("#");
    			buffer.append(iter.cus_acc_no().trim()).append("#");
    			buffer.append(iter.pkr_col_typ().trim()).append("#");
    			buffer.append(iter.exp_coll_amount()).append("\n");
    			streamWriter.write(buffer.toString());
    		}
		}
        streamWriter.flush();
        streamWriter.close();
        bc.debug("Podaci zapisani u datoteku.");
        
        // evidentiraj završetak obrade u tablicu DWH_STATUS
        if(!bo271.updateDwhStatus(value_date)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Evidentiran zavrsetak obrade u tablicu DWH_STATUS.");
		
		// stvori marker datoteku
		new File(fileName + ".marker").createNewFile();
		bc.debug("Stvoren marker.");

		bc.debug("Obrada zavrsena.");
		return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
	
	/**
	 * Metoda provjerava broj i ispravnost ulaznih parametara.
	 * @return da li je provjera prošla
	 */
	private boolean provjeriParametre(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 3)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
	     		inDir = bc.getArg(1);
	     		outDir = bc.getArg(2);
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
	
    public static void main(String[] args) {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2529257003"));
        batchParameters.setArgs(args);
        new BO270().run(batchParameters);
    }
}