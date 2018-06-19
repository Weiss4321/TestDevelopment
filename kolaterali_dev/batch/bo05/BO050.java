package hr.vestigo.modules.collateral.batch.bo05;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.framework.remote.transaction.ConnCtx;
import hr.vestigo.modules.coreapp.common.util.CSVGenerator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyK.YXYK1;




import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Marija Tadic
 *
 *Fiter

 */
public class BO050  extends Batch {
	public static String cvsident =
		"@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo05/BO050.java,v 1.25 2009/01/21 11:27:39 hramkr Exp $";
	
	private String configFile;  
	
	private String outputFileName;

	private String outputFile;
	
	private String sendStatus = "";
	
	private BO051 bo051 = null;
	 
	private String [] ulazniParametri=null;
	
	BO05Data dataVar = new BO05Data();
	
	private BatchContext bc = null;
	
	private ConnCtx ctx = null;
	
	private static final String encoding = "Cp1250";
	
	private Date datumObrade = null; //koristi se da bi se provjerilo radi li se o opomeni ili obavijesti
	 
	private int redniBroj = 0; //broji redak u CSV file-u
	
	private Vector xml_data = null;	//vektor u koji stavljam redak za CSV file
	
	private BigDecimal CustomerId;
	
	public String executeBatch(BatchContext bc) throws Exception {
		
		xml_data = new Vector();
		
		this.bc = bc;
		
		this.ctx = bc.getContext();
	
		bc.debug("******************** BO050.executeBatch() started *********************");
		
		this.bo051 = new BO051(bc);
		
		HashMap parameters = new HashMap();
//		da li je dobar broj parametra dobiven iz aplikacije, odnosno s komandne linije 
	    if(checkArgs()==false){
			return RemoteConstants.RET_CODE_ERROR;
	    }
		
	    bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	bc.updateEveID(bo051.insertIntoEvent());
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();
        bc.debug("Logiranje poziva batch-a logirano u tablicu Event.");
	    
        //dohvat trenutnog datuma
        
        GregorianCalendar gc=new GregorianCalendar();

        datumObrade = new Date(gc.getTime().getTime());
        
        
        
        try {
        	setConfig(bc);
          	
        	bc.debug("Pokretanje generiranja data vectora za CSVGenerator.");
        	
        	createCSV(bc);
        	
            bc.debug("Pokretanje generiranja data vectora za CSVGenerator uspjesno zavrseno.");
        	
            bc.debug("Generiranja data vectora za header");
        	
            bc.debug("Generiranja data vectora za header zavrseno.");
			
			
			//kreiranje csv datoteke
        
			 //inicijalizacija
			parameters=headerData();
			
        	
        	CSVGenerator.printCSV("csv080", bc.getLogin(),xml_data, parameters, this.configFile, this.outputFile, encoding);
        	YXY70.send(bc, "csv080", bc.getLogin() , outputFile);
     
         	
        }catch (SQLException sqle){
            sqle.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }

        //insertBatchFileLogs(outputFileName);
		bc.markFile(outputFileName);
        return RemoteConstants.RET_CODE_SUCCESSFUL;
	}
	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("1686830003"));
		bp.setArgs(args);
		new BO050().run(bp);
	}
	
	private void insertBatchFileLogs(String file_name) throws Exception {
		YXYK1 insertFileLog = new YXYK1(bc);
		try{
			insertFileLog.setFileProcessType_Generated();
			insertFileLog.bank_sign = bc.getBankSign();
			insertFileLog.eve_id = bo051.getEveId();
			insertFileLog.file_name = "";
			insertFileLog.file_rec_num = null;
			insertFileLog.file_id_no = null;
			insertFileLog.insertBatchFileLog();
			bc.debug("BO050/insertLogs: Inserted into batch_file_log");
		}catch (Exception e){
			bc.debug("GRESKA U INSERT INTO BATCH_FILE_LOG");
			throw e;
		}
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
		
		if (bc.getArgs().length!=8){
			bc.debug("Neispravan broj argumenata.");
			return false;
		}
		
		//if(bc.getArgs().length==1){
		//	ulazniParametri=new String[6];
		//	for (i=0;i<ulazniParametri.length;i++){
		//		ulazniParametri[i]="";
		//	}
		//	ulazniParametri[0]="12253";
		//	ulazniParametri[1]="IMOVINA";
		//	ulazniParametri[2]="13111";
		//	ulazniParametri[3]="12.12.2006";
		//	ulazniParametri[4]="12.12.2009";
		//	ulazniParametri[5]="A";
		//	bc.setBankSign(bc.getArg(0));
		//}else{
			ulazniParametri=new String[8];
			for(i=0;i<bc.getArgs().length;i++){
				ulazniParametri[i]=bc.getArg(i).trim();
				bc.debug("SQL_param[" + String.valueOf(i) + "]=" + ulazniParametri[i]);
			}		
			//bc.setBankSign("RB");
			bc.setBankSign(ulazniParametri[7]);
		//}
		
		return true;
	}
	
	private void setConfig(BatchContext batchContext) {
        String Path = System.getProperty("user.dir") + "/" + this.getClass().getPackage().getName().replace('.', '/');
        this.configFile = Path + "/coll_notice.xml";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = new String(dateFormat.format(bc.getExecStartTime()));

        this.outputFileName = "Collateral_notice_".concat(dateString).concat(".csv");
        this.outputFile = batchContext.getOutDir() + "/" + outputFileName;
        batchContext.debug("BO050, metoda setConfig(). Variable configFile=" + this.configFile);
        batchContext.debug("BO050, metoda setConfig(). Variable path=" + Path );
        batchContext.debug("BO050, metoda setConfig(). Variable outputDir=" + outputFile);
    }
	
	public void createCSV(BatchContext bc) throws SQLException, Exception{
		
		bc.debug("Usao u createCSV");
		String pomString=null;
		
		int i=1;
		
		BO051.Iter1  iterator_pom = null;
		BO051.Iter2  iterator_pom2 = null;
		BO051.Iter3  iterator_pom3 = null;
		
		//poziv metode u kojoj ulazne paramere punimo u objekt tipa BO05Data
		params_to_data(ulazniParametri);
		
		
		try{
			/**
			 odabir svih polica i pripadajuæih im kolterala i plasman i korisnika plasmana koje ulaze u izvješæe
			*/
			iterator_pom = bo051.fetchInsPolCollateral(dataVar);
			
			while(iterator_pom.next()){
				
				bc.debug("------------------------------------------kolateral" + iterator_pom.col_num());
				
				dataVar.reset_object();
				
				dataVar.col_ins_id = iterator_pom.col_ins_id();
				dataVar.ip_type_id = iterator_pom.ip_type_id();
				dataVar.osig = iterator_pom.osig();				
				dataVar.ip_code = iterator_pom.ip_code();
				dataVar.polica_do = iterator_pom.polica_do();
				dataVar.premija_do = iterator_pom.premija_do();
				dataVar.value = iterator_pom.value();
				dataVar.status = iterator_pom.status();
				dataVar.col_hea_id = iterator_pom.col_hea_id();
				dataVar.col_cat_id = iterator_pom.col_cat_id();
				dataVar.col_num = iterator_pom.col_num();
				dataVar.org_uni_code = iterator_pom.org_uni_code();
				dataVar.org_uni_name = iterator_pom.org_uni_name();
				dataVar.code_char = iterator_pom.code_char();
				dataVar.cus_id = iterator_pom.cus_id();
				dataVar.request_no = iterator_pom.request_no();
				dataVar.acc_no = iterator_pom.acc_no();		
				
				
				/**
				 * dohvat podatka o korisniku plasmana ako je plasman vezan direktno na 
				 * kolateral, ako nije vraca prazan iterator
				 */
				iterator_pom2 = bo051.user_p_data_fetch_col(dataVar);
				
				//System.out.println("iterator_pom2:"+iterator_pom2);
				// dohvat podataka o plasmanu i korisniku plasmana
				if (iterator_pom2.next()){

/** punjenje podatka o korisniku plasmana  u dataVar ako je 
					plasman vezan direktno na  kolateral
					*/
					  
					bc.debug("plasman vezan direktno na kolateral");
//					prvi sqlj je nesto dohvatio i to ide u xml
					do{
						//System.out.println("iterator_pom2.register_no():"+iterator_pom2.register_no());
						//System.out.println("iterator_pom2.request_no(:"+iterator_pom2.request_no());
						//System.out.println("iterator_pom2.acc_no():"+iterator_pom2.acc_no());
						//System.out.println("iterator_pom2.name():"+iterator_pom2.name());
						//System.out.println("iterator_pom2.surname():"+iterator_pom2.surname());
						CustomerId = iterator_pom2.cus_id();
						dataVar.register_no = iterator_pom2.register_no();
//						dataVar.request_no = iterator_pom2.request_no();
//						dataVar.acc_no = iterator_pom2.acc_no();
						dataVar.customer_name = iterator_pom2.name();
						dataVar.customer_address = iterator_pom2.address();
						/**
						 * puni polje osnovnom adresom iz tablice CUST_COMMUNICATION ukoliko prazno, tj. ukoliko u CUSTOMER 
						 * tablici za tog komitenta ne postoji 
						 * opca dopisna adresa
						 */
						if (dataVar.customer_address==null || dataVar.customer_address.trim().equals(""))
							bo051.getBasicAddress(CustomerId,dataVar);
						dataVar.customer_postoffice = iterator_pom2.postoffice();
						dataVar.customer_city = iterator_pom2.city();
						doStuff(dataVar);	
						//pozvati metodu reset customer
						
						dataVar.reset_customer();
						
					} while (iterator_pom2.next());
					
					/**
					 * dohvat podatka o plasmanu i korisniku plasmana ako je plasman vezan preko hipoteke,
					 * ako nije vraca prazan iterator
					 */

					}
				
				
				
			}
		}catch(SQLException sqle){
			throw sqle;
		}catch(Exception e){
			throw e;
		}
	}

	/**
     * Metoda provjerava radi li se o opomeni ili obavijesti, insertira jedan redak u CSV file
     * @param dataVar BO05Data
     * @return void
     */
public void doStuff(BO05Data dataVar) throws SQLException, Exception {	
	
	try{
				//3. odrediti da li se salje obavijest ili opomena
				//System.out.println("USAO U DOSTUFF");
				//System.out.println("3. odrediti da li se salje obavijest ili opomena");
				
				if (dataVar.status.equalsIgnoreCase("A") && dataVar.premija_do.compareTo(dataVar.getDate_from())>= 0 
						&& dataVar.premija_do.compareTo(dataVar.getDate_until()) <= 0){
					
					//--------> status_slanja= 'O' (obavijest)
					
					sendStatus = "O";				
				} 
				
				if (dataVar.status.equalsIgnoreCase("I") || dataVar.premija_do.compareTo(datumObrade)<=0){
					
					//--------> status_slanja= 'P' (opomena)
					sendStatus = "P";
				}
				
				//System.out.println("obavijest ili opomena:"+sendStatus);
				
				//4. insert sloga o formiranoj obavijesti/opomeni u tabelu
				//bo051.insertIntoRecordTable(dataVar);
				
				
				//5. update statusa o slanju obavijesti/opomene 
// ovaj korak je izbaèen jer se ovaj spec status puni kroz aplikaciju				
				dataVar.sendStatus = sendStatus;
//				if(bo051.updateSendStateCol(dataVar))
//					bo051.updateSendStateInsPol(dataVar);
				
				//6.punjenje csv datoteke
				
				
				insertXMLLine(bc);
				
						

			
			
		}catch(SQLException sqle){
			throw sqle;
		}catch(Exception e){
			throw e;
		}
	}
	
	private void params_to_data(String [] ulazniParametri){
		
    	String [] parametri=null;
    	int i=0;
    	
    	parametri=ulazniParametri;
    	for (i=0;i<parametri.length;i++)
    		System.out.println("parametri[" + String.valueOf(i) + "]=" + parametri[i]);
    	
		
	
		dataVar.setOrg_uni_id(parametri[2]);
		
		dataVar.setType_code(parametri[3]);
		
		dataVar.setIc_id(parametri[4]);
	
		dataVar.setDate_from(parametri[0]);
		
		dataVar.setDate_until(parametri[1]);
		
		dataVar.setState_code(parametri[5]);
		
		dataVar.setClientType(parametri[6]);
		
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
    
    public void insertXMLLine(BatchContext bc) throws SQLException{
    	
    	//System.out.println("!!!!!!!!!insertXMLLine:!!!!!!!!!!");
		HashMap row = new HashMap();
		
		
		row.put("ser_number",String.valueOf(++redniBroj));//1 ??????????????????????????????????????????
		row.put("org_unit_code",dataVar.org_uni_code);//2
		row.put("org_unit_name",dataVar.org_uni_name);//3
		row.put("register_no",dataVar.register_no);//4
		row.put("name",dataVar.customer_name);//5
		row.put("adress",dataVar.customer_address);//7
		row.put("adress_part2",dataVar.customer_postoffice+" "+dataVar.customer_city); //8
		row.put("request_number",dataVar.request_no); //9
		row.put("party_number",dataVar.acc_no); //10
		if (dataVar.col_cat_id.equals(new BigDecimal("616223")))
		{
			row.put("coll_code_1",dataVar.col_num); //11
			row.put("coll_code_2",""); //11
		}else
		{
			row.put("coll_code_2",dataVar.col_num); //12
			row.put("coll_code_1",""); //11
		}
		row.put("policy_number",dataVar.ip_code); //13
		row.put("expiry_date",dataVar.polica_do);//14
		row.put("prem_payed_until",dataVar.premija_do);//15
		row.put("value_acc_or_surren",dataVar.value); //16
		row.put("currency",dataVar.code_char);//17	
		row.put("policy_state",dataVar.status+"\n");//18	
 
		xml_data.add(row);
		  
 
	}
    private HashMap headerData(){
    	HashMap param=new HashMap();
 	   		
    	return param;
    }
}
