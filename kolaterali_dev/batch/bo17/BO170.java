/*
 * Created on 2007.11.14
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo17;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.coreapp.common.util.CSVGenerator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyK.YXYK1;
import hr.vestigo.framework.remote.batch.Batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO170 extends Batch {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo17/BO170.java,v 1.3 2008/03/14 10:08:28 hraaks Exp $";

	BatchContext bc = null;
	
	private String [] ulazniParametri = null;
	
	private BO17Data dataVar = new BO17Data();
	
	private String configFile = null;
	
	private String outputFileName;
	
	private String outputFile;
	
	private static final String encoding = "Cp1250";
	
	private BO171 bo171 = null;

	private Vector xml_data = null;
	
	private int redniBroj = 0; //broji redak u CSV file-u
	
	public String executeBatch(BatchContext bc) throws Exception{
		
		this.bc = bc;
		
		this.bo171 = new BO171(bc);
		
		HashMap parameters = new HashMap();
		
		//da li je dobar broj parametra dobiven iz aplikacije, odnosno s komandne linije
		if (checkArgs() == false)
			return RemoteConstants.RET_CODE_ERROR;
		
		bc.beginTransaction();
//		logiranje poziva izvrsavanja batcha u tablicu Event
		try {
			bc.updateEveID(bo171.insertIntoEvent());
		}
		catch (Exception e) {
			e.printStackTrace();
			return RemoteConstants.RET_CODE_ERROR;
		}
		bc.commitTransaction();
		
		bc.debug("Logiranje poziva batch-a logirano u tablicu Event.");
		
		//stavlja paramtere u pozadinski objekt
		
		params_to_data(ulazniParametri);
		
		try {
				
				setConfig();
		
				bc.debug("Pokretane generiranja data vextora za CSV generator ");
				
				writeData(bc);
				
				YXY70.send(bc,"csv125", bc.getLogin() , outputFile);
				
		}
		catch(SQLException sqle) {
			sqle.printStackTrace();
			return RemoteConstants.RET_CODE_ERROR;
		}catch (Exception e) {
			e.printStackTrace();
			return RemoteConstants.RET_CODE_ERROR;
		}
//		insertBatchFileLogs(outputFileName);
		bc.markFile(outputFileName);
        return RemoteConstants.RET_CODE_SUCCESSFUL;
	}
	
	public static void main(String [] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("1888341003"));
		bp.setArgs(args);
		new BO170().run(bp);
	}
	
	private boolean checkArgs() {
		int i;
		for (i = 0; i < bc.getArgs().length; i++)
			bc.debug("bc_Arg[" + String.valueOf(i) + "] = " + bc.getArg(i));
		
		if (bc.getArgs().length != 5 && bc.getArgs().length != 1)
		{	
			bc.debug("Neispravan broj argumenata.");
			return false;
		}
		
		//ako dobiva parametre s komanden linije,... inace (ako iz aplikacije)
		if (bc.getArgs().length == 1) {
			
			ulazniParametri[0] = "F";
			ulazniParametri[1] = "";
			ulazniParametri[2] = "01.01.2007";
			ulazniParametri[3] = "01.01.2008";
	
			bc.setBankSign(bc.getArg(0));
		}else {
			ulazniParametri = new String[5];
			for (i=0; i < bc.getArgs().length; i++) {
				ulazniParametri[i] = bc.getArg(i).trim();
				bc.debug("SQL_param[" + String.valueOf(i) + "] = " + ulazniParametri[i]);
			}
			bc.setBankSign(ulazniParametri[4]);
		
		}
		
		return true;
	
	}
	
	private void params_to_data(String [] ulazniParametri) {
	
		String [] parametri = null;
		
		int i = 0;
		
		parametri = ulazniParametri;
	
		
		dataVar.setClientType(parametri[0]);
		
		dataVar.setOrg_uni_id(parametri[1]);

		dataVar.setDeliverDateFrom(parametri[2]);
		
		dataVar.setDeliverDateUntil(parametri[3]);
		
		
		
	}
	
	private void setConfig() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = new String(dateFormat.format(bc.getExecStartTime()));

        this.outputFileName = "Knjizice_vozila".concat(dateString).concat(".csv");
        this.outputFile = bc.getOutDir() + "/" + outputFileName;
        bc.debug("BO170, metoda setConfig(). Variable outputFileName=" + outputFileName);
        bc.debug("BO170, metoda setConfig(). Variable outputDir=" + outputFile);
	
	}
	
	
	
	
	private void insertBatchFileLogs(String file_name) throws Exception {
		YXYK1 insertFileLog = new YXYK1(bc);
		try{
			insertFileLog.setFileProcessType_Generated();
			insertFileLog.bank_sign = bc.getBankSign();
			insertFileLog.eve_id = bo171.getEveId();
			insertFileLog.file_name = "";
			insertFileLog.file_rec_num = null;
			insertFileLog.file_id_no = null;
			insertFileLog.insertBatchFileLog();
			bc.debug("BO170/insertLogs: Inserted into batch_file_log");
		}catch (Exception e){
			bc.debug("GRESKA U INSERT INTO BATCH_FILE_LOG");
			throw e;
		}
	}	
	
	/**
	 * puni parametre koji idu u zaglavlje file-a 
	 * */
	private String naziviKolonaReporta() throws SQLException {
		StringBuffer buff = new StringBuffer();
		buff.append("Redni broj").append(";");
		buff.append("Org. jedinica kod").append(";");
		buff.append("Org. jedinica naziv").append(";");
		buff.append("ID korisnika plasmana").append(";");
		buff.append("Ime i prezime korisnika plasmana").append(";");
		buff.append("Adresa").append(";");
		buff.append("Po\u0161ta i grad").append(";");
		buff.append("Partija plasmana").append(";");
        buff.append("\u0160ifra kolaterala").append("\n");
        
        return buff.toString();
	}
	
	
	private String getDDMMYYYY(Date date) {
        String dateString = "";
        if (date == null) {
            return dateString;
        }
        
        int day = date.getDate();
        int month = date.getMonth() + 1;
        int year = date.getYear() + 1900;
        String dayString;
        String monthString;

        if (day < 10) {
            dayString = "0" + day;
        }
        else {
            dayString = "" + day;
        }

        if (month < 10) {
            monthString = "0" + month;
        }
        else {
            monthString = "" + month;
        }

        dateString = dayString + "." + monthString + "." + year;

        return dateString;
    }
	 
	
	/**
	 * puni parametre koji idu u zaglavlje CSV file-a za neponderirane...
	 * */
	private String headerData() throws SQLException {
		 StringBuffer buff = new StringBuffer();
	        
		  if (dataVar.getClientType().trim().equals("F"))
		  	buff.append("Izvjesce za knjizice vozila, zadano za fizicke osobe").append("\n\n");
		  else 
		  	buff.append("Izvjesce za knjizice vozila, zadano za pravne osobe").append("\n\n");
	      buff.append("Podaci za koje se radi izvjesce:").append("\n");
	      buff.append("Vremensko razdoblje: ").append(getDDMMYYYY(dataVar.getDeliverDateFrom()));
	      buff.append("-" + getDDMMYYYY(dataVar.getDeliverDateUntil())).append("\n");
	      bo171.organizationUnit(dataVar);
	      buff.append("Org.jedinica: " + dataVar.getOrg_uni_code() + " " + dataVar.getOrg_uni_name()).append("\n\n");
	  
	     
	      
    	 return buff.toString();
	}
	
	public void writeData(BatchContext bc) throws SQLException, Exception{
		
		String pomString=null;
		
		int i=1;
		//System.out.println("writeData");
		BO171.Iter  iterator = null;
		
		String zaglavlje = null;
		
		iterator = bo171.fetchData(dataVar);
		
		
		OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(this.outputFile)));
	
		
    		zaglavlje = headerData();
    		streamWriter.write(zaglavlje);
    		zaglavlje=naziviKolonaReporta();
        	streamWriter.write(zaglavlje);
    	
			
			/**
			 * Insertira u pomocni objekt dataVarCRM 
			 */
			while(iterator.next()){
				
				StringBuffer line = new StringBuffer();
		    	
		    	//Redni broj
				line.append(String.valueOf(++redniBroj)).append(";"); 
				
//				Org.jedinica kod
				line.append(iterator.org_uni_code()).append(";");
				
//				Org.jedinica naziv
				line.append(iterator.org_uni_name()).append(";");
				
//				ID korisnika plasmana
				line.append(iterator.register_no()).append(";");
				
				
//				Ime i prezime korisnika plasmana
				line.append(iterator.name()).append(";");
				
//				Adresa, posta i grad
				if ((iterator.address()) == null || (iterator.address()).equals(""))
				{
					bo171.getBasicAddress(iterator.cus_id(),dataVar);
					line.append(dataVar.getCustomer_address()).append(";");
					line.append(dataVar.getCustomer_postoffice() + " " + dataVar.getCustomer_city()).append(";");
				}
				{
					line.append(iterator.address()).append(";");
					line.append(iterator.postoffice() + " " + iterator.city()).append(";");
				}
				
//				Partija plasmana
				line.append(iterator.party_no()).append(";");
				
//				Sifra kolaterala
				line.append(iterator.col_num()).append(";");
				line.append("\n");
				
	    	 	streamWriter.write(line.toString());
	  }
			
	  streamWriter.flush();
	  streamWriter.close();  
	  if (iterator != null) {
		try {
			iterator.close();
		} catch(SQLException ignored) {}
	}			
}
			
}
