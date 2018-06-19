/*
 * Created on 2006.09.01
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo01;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.coreapp.common.util.CSVGenerator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyK.YXYK1;


/**
 * @author hrazst
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO010 extends Batch{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo01/BO010.java,v 1.7 2006/10/17 14:19:57 hrazst Exp $";
	/**
	 * Batch za kontrolni ispis unesenih kolaterala po referentu i plasmanu.
	 */
	
	private String configFile;  

	private String outputFileName;

	private String outputFile;
	
	private BO011 bo011 = null;
	 
	private String [] ulazniParametri=null;
	
	private BatchContext bc = null;
	
	private Vector dataCSV=null;
	
	private static final String encoding = "Cp1250";
	
	public BO010() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String executeBatch(BatchContext batchContext) throws Exception {
        
        this.bc = batchContext;
        bc.debug("******************** BO010.executeBatch() started *********************");
        
        
        this.bo011 =new BO011(bc);
        
        HashMap parameters = new HashMap();

    	if(checkArgs()==false){
			return RemoteConstants.RET_CODE_ERROR;
    	}
        
    	bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	bo011.insertIntoEvent();
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();
        bc.debug("Logiranje poziva batch-a logirano u tablicu Event.");
        
        try {
        	setConfig(bc);
          	
        	bc.debug("Pokretanje generiranja data vectora za CSVGenerator.");
        	createCSV();
        	bc.debug("Pokretanje generiranja data vectora za CSVGenerator uspjesno zavrseno.");
        	
        	bc.debug("Generiranja data vectora za header");
        	parameters=headerData();
        	bc.debug("Generiranja data vectora za header završeno.");
        	
        	CSVGenerator.printCSV("csv071", bc.getLogin(), dataCSV, parameters, this.configFile, this.outputFile, encoding);
        	YXY70.send(batchContext, "csv071", bc.getLogin() , outputFile);
        	
        }catch (SQLException sqle){
            sqle.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }

        insertBatchFileLogs(outputFileName);
		bc.markFile(outputFileName);
        return RemoteConstants.RET_CODE_SUCCESSFUL;
	}
	
	private boolean checkArgs(){
		
		String pomString=null;
		String delimiter=null;
		int brojac=0;
		int brojac1=0;
		int pozicija=0;
		int i=0;
		
		for(i=0;i<bc.getArgs().length;i++){
			bc.debug("bc_Arg[" + String.valueOf(i) + "]=" + bc.getArg(i));
		}
		
		if (bc.getArgs().length!=8 && bc.getArgs().length!=1){
			bc.debug("Neispravan broj argumenata.");
			return false;
		}
		
		if(bc.getArgs().length==1){
			ulazniParametri=new String[8];
			for (i=0;i<ulazniParametri.length;i++){
				ulazniParametri[i]="";
			}
			ulazniParametri[0]="M";
			bc.setBankSign(bc.getArg(0));
		}else{
			ulazniParametri=new String[8];
			for(i=0;i<bc.getArgs().length;i++){
				ulazniParametri[i]=bc.getArg(i).trim();
				bc.debug("SQL_param[" + String.valueOf(i) + "]=" + ulazniParametri[i]);
			}		
			//bc.setBankSign("RB");
			bc.setBankSign(ulazniParametri[7]);
		}
		
		return true;
	}
	
	private void insertBatchFileLogs(String file_name) throws Exception {
		YXYK1 insertFileLog = new YXYK1(bc);
		try{
			insertFileLog.setFileProcessType_Generated();
			insertFileLog.bank_sign = bc.getBankSign();
			insertFileLog.eve_id = bo011.getEveId();
			insertFileLog.file_name = "";
			insertFileLog.file_rec_num = null;
			insertFileLog.file_id_no = null;
			insertFileLog.insertBatchFileLog();
			bc.debug("BO010/insertLogs: Inserted into batch_file_log");
		}catch (Exception e){
			bc.debug("GRESKA U INSERT INTO BATCH_FILE_LOG");
			throw e;
		}
	}
	
    private HashMap headerData(){
    	HashMap param=new HashMap();
 	   		
    	return param;
    }
	
	public void createCSV() throws SQLException, Exception{
		String pomString=null;
		dataCSV = new Vector();
		int i=1;
		
		BO011.iteratorZapisi iteratorZ = null;
		
		
		
		try{
			bc.debug("Punjenje vectora data sa vrijednostima.");
			iteratorZ= bo011.fetchZapise(ulazniParametri);
			while(iteratorZ.next()){
				HashMap row = new HashMap();
				row.put("login",iteratorZ.login());
				row.put("acc_no",iteratorZ.acc_no());
				row.put("request_no",iteratorZ.request_no());				
				row.put("register_no",iteratorZ.register_no());
				row.put("name",iteratorZ.name());
				String datum = getDDMMYYYY((Date) iteratorZ.datum());
				row.put("opening_ts", datum );
				row.put("coll_type_code",iteratorZ.coll_type_code());
				row.put("coll_type_name",iteratorZ.coll_type_name());
				row.put("col_num",iteratorZ.col_num());
				row.put("real_est_nomi_valu",convertDecimalToString(iteratorZ.real_est_nomi_valu()));
				row.put("code_char",iteratorZ.code_char());
				dataCSV.add(row);
			}			
		}catch(SQLException sqle){
			throw sqle;
		}catch(Exception e){
			throw e;
		}
	}
	
    private static String convertDecimalToString(BigDecimal broj){
    	String sbroj=null;
    	DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
    	DecimalFormat decimalFormat = new DecimalFormat();

    	if (broj==null){
    		return "";
    	}
    	
    	decimalFormatSymbols.setDecimalSeparator(',');
		decimalFormatSymbols.setGroupingSeparator('.');
		decimalFormat.applyPattern("#,##0.00");
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

    	return decimalFormat.format(broj).toString();
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
	
    private void setConfig(BatchContext batchContext) {
        String Path = System.getProperty("user.dir") + "/" + this.getClass().getPackage().getName().replace('.', '/');
        this.configFile = Path + "/kori_plas.xml";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = new String(dateFormat.format(bc.getExecStartTime()));

        this.outputFileName = "Korisnici_plasmani_".concat(dateString).concat(".csv");
        this.outputFile = batchContext.getOutDir() + "/" + outputFileName;
        batchContext.debug("BO010, metoda setConfig(). Variable configFile=" + this.configFile);
        batchContext.debug("BO010, metoda setConfig(). Variable path=" + Path );
        batchContext.debug("BO010, metoda setConfig(). Variable outputDir=" + outputFile);
    }
	
	public static void main(String[] args) {
		BatchParameters batchParameters = new BatchParameters(new BigDecimal("1637835003.0"));
        batchParameters.setArgs(args);
        new BO010().run(batchParameters);
	}
}
