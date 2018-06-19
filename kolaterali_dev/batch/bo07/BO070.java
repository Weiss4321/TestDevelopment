
package hr.vestigo.modules.collateral.batch.bo07;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo07.BO070;
import hr.vestigo.modules.collateral.batch.bo07.BO071;
import hr.vestigo.modules.collateral.batch.bo07.BO07Data;
import hr.vestigo.modules.collateral.batch.bo07.BO071.Iter3;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyK.YXYK1;
import hr.vestigo.modules.rba.util.DateUtils;





import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class BO070 extends Batch {
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo07/BO070.java,v 1.33 2009/05/21 09:02:06 hrakis Exp $";
	
	private BatchContext bc = null;
	
	private BO071 bo071 = null;
	
	private String outputFileName;

	private String outputFile;
	
	private String outputFileName2;

	private String outputFile2;
 	
	private static final String encoding = "Cp1250";
	
	BO07Data dataVar = new BO07Data();
	
	BO07CRMData dataVarCRM = new BO07CRMData();
	
	private String [] ulazniParametri = null; 
	
	private BigDecimal ukupnaPokrivenost = null;
	/**
	 * prihvatljivost
	 */
	private String eligibility="B1";
	
	public String executeBatch(BatchContext bc) throws Exception {
	
		this.bc = bc;
	
		bc.debug("******************** BO070.executeBatch() started *********************");
		
		this.bo071 = new BO071(bc);
		
		dataVar.setDateOfReport((Date)null);
		
//		da li je dobar broj parametra dobiven iz aplikacije, odnosno s komandne linije 
		try {
			if(checkArgs() == false){	
				bc.error("Broj parametara:" + bc.getArgs().length, bc.getArgs(), new Exception("Neispravan broj parametara."));  
				return RemoteConstants.RET_CODE_ERROR;
			}
		}catch(Exception e) 
		{
		   e.printStackTrace();
           return RemoteConstants.RET_CODE_ERROR;
		}
		
	    bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	bc.updateEveID(bo071.insertIntoEvent());
        }catch (Exception e) {
        	bc.error("Greška pri insertiranju u event tablicu. ",  e);
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();
        bc.debug("Logiranje poziva batch-a logirano u tablicu Event.");
	           
        
        /**
		 * Stavlja parametre u pozadinski objekt 
		 */
		params_to_data(ulazniParametri);
        
        try {

    		
        	setConfig(bc);
          	
        	bc.debug("Pocetak obrade podataka.");
        	
        	
        	
        	/**odabir komitenta ili svih komitenata (pravne osobe) koji ulaze u izvjesce 
        	 * i pripadajucih im plasmana, dohvat podataka o PODTIPU kolaterala
        	 * formiranje jednog retka izvjesca i insert tog retka u pomocnu tabelu
        	*/
        	createCSVStep1(bc);
        	
        	
        	
        	/**
        	 * svaki redak se iz pomocne tabele prepise u file - kod dohvata podatke 
        	 * sortira se po komitentu i po izlosenosti po partiji desc
        	*/
        	writeData(bc);
        	     	
        	// ako su ponderirane vrijednosti, stvori i analitièko izvješæe
        	if(!dataVar.getPonder().equals("P")) outputFile2 = outputFileName2 = null;
        	else writeData2(bc);
			  
        	bc.debug("Zavrseno kreiranje file-a."); 
        	YXY70.send(bc, "csv082", bc.getLogin() , outputFile, outputFile2);
     
         	
        }catch (SQLException sqle){
            sqle.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }

        //insertBatchFileLogs(outputFileName);
		bc.markFile(outputFileName);
		if(outputFileName2 != null) bc.markFile(outputFileName2);
        return RemoteConstants.RET_CODE_SUCCESSFUL;
		
	}
	
	public static void main(String[] args) {
		BatchParameters bp = new BatchParameters(new BigDecimal("1693071003"));
		bp.setArgs(args);
		new BO070().run(bp);
	}
	

	private boolean checkArgs(){
		
		String pomString = null;
		String delimiter = null;
		int brojac = 0;
		int brojac1 = 0;
		int pozicija = 0;
		int i = 0;
		
		//System.out.println("checkArgs");
		
		for(i=0;i<bc.getArgs().length;i++){
			bc.debug("bc_Arg[" + String.valueOf(i) + "]=" + bc.getArg(i));
		}
		
		
		if (bc.getArgs().length!=6 && bc.getArgs().length!=1){
			bc.debug("Neispravan broj argumenata.");
			return false;
		}
		 /** 
		  * Ako dobiva parametre s komandne linije..., inace ako iz aplikacije...
		  */
		if(bc.getArgs().length==1){
			ulazniParametri=new String[6];
			for (i=0;i<ulazniParametri.length;i++){
				ulazniParametri[i]="";
			}
			ulazniParametri[0]="14.02.2007";
			ulazniParametri[1]="8984";
			ulazniParametri[2]="P";
			ulazniParametri[3]="P";
			ulazniParametri[4]="B1";
			bc.setBankSign(bc.getArg(0));
		}else{
			ulazniParametri=new String[6];
			for(i=0;i<bc.getArgs().length;i++){
				ulazniParametri[i]=bc.getArg(i).trim();
				bc.debug("SQL_param[" + String.valueOf(i) + "]=" + ulazniParametri[i]);
			}		
			//bc.setBankSign("RB");
			bc.setBankSign(ulazniParametri[5]);
		}
		
		return true;
	} 
	
	
	public void createCSVStep1(BatchContext bc) throws SQLException, Exception{
		bc.debug("createCSVStep1.");
		String pomString=null;
		
		int i=1;
		
		//System.out.println("createCSVStep1");
		
		BO071.Iter1  iterator_pom = null;
		
	
		try {
			/*
			 *Brise pomocnu tabelu 
			 */
			bo071.deleteCRMData();
			
			/**
			 *Uzima se podatke komitenta ili svih komitenata (pravne osobe) koji ulaze u izvjesce i pripadajucih im plasmana
			 */
			iterator_pom = bo071.fetchData(dataVar);
			
			bc.debug("Punjenje vectora dataVar sa vrijednostima.");
		
			while(iterator_pom.next()){
			
				//System.out.println("Usao u iter.next createCSVStep1");
			
				dataVar.reset_object();
				dataVar.setCus_id(iterator_pom.cus_id());
				dataVar.setCol_hea_id(iterator_pom.col_hea_id());
				dataVar.setExp_coll_amount(iterator_pom.exp_coll_amount());
				dataVar.setRegister_no(iterator_pom.register_no());
				dataVar.setName(iterator_pom.name());
				dataVar.setCus_acc_no(iterator_pom.cus_acc_no());
				dataVar.setExp_balance_hrk(iterator_pom.exp_balance_hrk());
				dataVar.setCol_cat_id(iterator_pom.col_cat_id());
				dataVar.setCol_type_id(iterator_pom.col_type_id());
				//bc.debug("insertDataAboutSubtype.");
				
				/*
				 * Uzima se podatke o podtipu pojedinog kolaterala
				 */
				bo071.insertDataAboutSubtype(dataVar);
				
				/*
				 * Prebacuje podatke u pozadinski objekt koji sadrzi sve podatke koji ce ici u 
				 * pomocnu tabelu u objekt dataVarCRM tipa BO07CRMData
				 */
				fillDataVarCRM();
				
				/*
				 * Podatke iz dataVarCRM insertira u pomocnu tabelu
				 */
				bo071.insertIntoCRMTable(dataVarCRM);
						   
		}
	}catch(SQLException sqle){
		throw sqle;
	}catch(Exception e){
		throw e;
	}
}
	
	
	public void writeData(BatchContext bc) throws SQLException, Exception{
		//bc.debug("writeData.");
		String pomString=null;
		
		int i=1;
		//System.out.println("writeData");
		BO071.Iter2  pom_iterator = null;
		
		String zaglavlje = null;
		String line = null;
		
		OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(this.outputFile)));
	
		/*
    	 * dohvaca parametre kojima se puni zaglavlje file-a
    	 */
    	System.out.println("->" + dataVar.getPonder() + "," + dataVar.getPonder().equals("P"));
    	if (dataVar.getPonder().equals("P"))
    	{ 
    		zaglavlje = headerDataPonder();
    		streamWriter.write(zaglavlje);
    		zaglavlje=naziviKolonaReporta();
        	streamWriter.write(zaglavlje);
    		//zaglavlje = getPonders();
    		//streamWriter.write(zaglavlje);
    	}
    	else 
    	{
    		 zaglavlje=headerDataNotPonder();
        	streamWriter.write(zaglavlje);
        	zaglavlje=naziviKolonaReporta();
        	streamWriter.write(zaglavlje);
    	}
    	 
			
			/**
			 * Uzima podatke iz pomocne tabele 
			 */
			pom_iterator = bo071.fetchDataFromCRM();
			
			
			/**
			 * Insertira u pomocni objekt dataVarCRM 
			 */
			while(pom_iterator.next()){
				
				
			
				dataVarCRM.setDateOfReport(pom_iterator.dt());
				//Interni MB komitenta korisnika plasmana
				dataVarCRM.setRegister_no(pom_iterator.register_no());//2
				//Naziv komitenta
				dataVarCRM.setClient_name(pom_iterator.client_name());//3
				//Partija plasmana
				dataVarCRM.setInvest_party(pom_iterator.invest_party());//4
				//Izloženost
				dataVarCRM.setExposure(pom_iterator.exposure());//5
				//Gotovinski depozit
				dataVarCRM.setCash_deposit(pom_iterator.cash_deposit());//6
				//Polica osiguranja sa štednom komponentom
				dataVarCRM.setAssur_policy(pom_iterator.assur_policy());//7
				//Garancija države
				dataVarCRM.setGuaranty_state(pom_iterator.guaranty_state());
				//Garancija druge banke
				dataVarCRM.setGuaranty_other_bank(pom_iterator.guaranty_other_bank()); //9
				//Poljop.gospodarstvo,proizvodni objekt
				dataVarCRM.setReal_estate1(pom_iterator.real_estate1()); //10
				//Posl.stamb.objekt,prodajno uslužni prostor, uredske prostorije
				dataVarCRM.setReal_estate2(pom_iterator.real_estate2()); //11
				//Sklad., prod. i uredski prostori
				dataVarCRM.setReal_estate3(pom_iterator.real_estate3()); //12
				//Skladišni prostor
				dataVarCRM.setReal_estate4(pom_iterator.real_estate4()); //13
				//Stambeni objekt
				dataVarCRM.setReal_estate5(pom_iterator.real_estate5());//14
				//Hotel, pansion
				dataVarCRM.setReal_estate6(pom_iterator.real_estate6());//15
				//Hotelsko naselje
				dataVarCRM.setReal_estate7(pom_iterator.real_estate7()); //16
				//Privatni smještaj
				dataVarCRM.setReal_estate8(pom_iterator.real_estate8());//17
				//Graðevinsko zemljište
				dataVarCRM.setReal_estate9(pom_iterator.real_estate9()); //10
				//Poljoprivredno zemljište
				dataVarCRM.setReal_estate10(pom_iterator.real_estate10()); //11
				//Osobni automobili
				dataVarCRM.setPriv_cars(pom_iterator.priv_cars());//18	
				//Motocikli i ostala vozila
				dataVarCRM.setOther_veh(pom_iterator.other_veh());//19	
				//Kamioni, autobusi, pružna vozila
				dataVarCRM.setTrucks_etc(pom_iterator.trucks_etc());//20	
				//Graðevinska vozila i graðevinski strojevi
				dataVarCRM.setBuilding_veh(pom_iterator.building_veh());//21	
				//Plovila
				dataVarCRM.setVessels(pom_iterator.vessels());//22	
				//Zrakoplovi
				dataVarCRM.setAirplanes(pom_iterator.airplanes());//23	
				//Strojevi
				dataVarCRM.setMachines(pom_iterator.machines());//24
				
				/*
				 * Pomoæu objekta dataVarCRM slaze podatke u liniju pritom racunajuci ukupnu pokrivenost 
				 * koja mu na kraju treba za kolonu osigurano i neosigurano
				 */
				line = getLineOfReport();
				
	    	 	streamWriter.write(line);
	  }
			
	  streamWriter.flush();
	  streamWriter.close();  
				
}

	public void writeData2(BatchContext bc) throws SQLException, Exception
	{
		OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(this.outputFile2)));
		
		if(dataVar.getPonder().equals("P")) streamWriter.write(headerDataPonder());
		else streamWriter.write(headerDataNotPonder());

		streamWriter.write(naziviKolonaReporta2());
			
		Iter3 iter = bo071.fetchData2(dataVar, ulazniParametri[1]);
		while(iter.next()) streamWriter.write(getLineOfReport2(iter));
			
		streamWriter.flush();
		streamWriter.close();
	}
	
			
	/**
	 * puni parametre koji idu u zaglavlje CSV file-a za neponderirane...
	 * */
	private String headerDataNotPonder() throws SQLException {
		 StringBuffer buff = new StringBuffer();
	        
	      buff.append(DateUtils.getDDMMYYYY(dataVar.getDateOfReport())).append("\n");
	      buff.append("Izvjesce za CRM s neponderiranim vrijednostima.").append("\n");
	      buff.append("Prihvatljivost: ").append(ulazniParametri[4]).append("\n\n");
    	
    	 return buff.toString();
	}
	
	/**
	 * puni parametre koji idu u zaglavlje CSV file-a za neponderirane...
	 * */
	private String headerDataPonder() throws SQLException {
		 StringBuffer buff = new StringBuffer();
	        
	      buff.append(DateUtils.getDDMMYYYY(dataVar.getDateOfReport())).append("\n");
	      buff.append("Izvjesce za CRM s ponderiranim vrijednostima.").append("\n");
	      buff.append("Prihvatljivost: ").append(ulazniParametri[4]).append("\n\n");
    	
    	 return buff.toString();
	}
	
	
	/**
	 * puni parametre koji idu u zaglavlje file-a za ponderirane...
	 * */
	private String getPonders() throws Exception {
		
		BigDecimal tmp_bd = null;
	
		StringBuffer buff = new StringBuffer();
	
	    buff.append("").append(";");
	    buff.append("").append(";");
	    buff.append("").append(";");
	    buff.append("").append(";");
		/*Dohvaca se ponder za Gotovinski depozit*/
		tmp_bd = bo071.ponder_one_param("612223");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Policu osiguranja sa stednom komponentom (pretpostavlja se da je ponder 
		 * za col_cat_id = 616223, col_type_id = 54777 i col_type_id = 55777 isti)*/
		tmp_bd = bo071.ponder_two_param("616223","54777"); 
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Garancija drzave*/
		tmp_bd = bo071.ponder_two_param("615223","30777");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Garancija druge banke*/
		tmp_bd = bo071.ponder_two_param("615223","31777");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Poljop.gospodarstvo,proizvodni objekt (pretpostavlja se da je isto za 
		 * ("618223","9777","7222") i ("618223","9777","1063223"))*/
		tmp_bd = bo071.ponder_three_param("618223","9777","7222");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Posl.stamb.objekt,prodajno uslužni prostor, uredske prostorije (pretpostavlja 
		 * se da je isto za ("618223","9777","8222") i ("618223","9777","292825223"))*/
		tmp_bd = bo071.ponder_three_param("618223","9777","9222");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Sklad., prod. i uredski prostori*/
		tmp_bd = bo071.ponder_three_param("618223","9777","6524223");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Skladišni prostor*/
		tmp_bd = bo071.ponder_two_param("618223","12777");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Stambeni objekt*/
		tmp_bd = bo071.ponder_two_param("618223","8777");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Hotel, pansion (pretpostavlja se da je ponder 
		 * za col_cat_id = 618223, col_type_id = 10777 i col_sub_id = 11222 isti )*/
		tmp_bd = bo071.ponder_three_param("618223","10777","10222");//11222
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Hotelsko naselje*/
		tmp_bd = bo071.ponder_three_param("618223","10777","31931223");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Privatni smjestaj*/
		tmp_bd = bo071.ponder_three_param("618223","10777","12222");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Graðevinsko zemljište*/
		tmp_bd = bo071.ponder_three_param("618223","7777","1222");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Poljoprivredno zemljište*/
		tmp_bd = bo071.ponder_three_param("618223","7777","2222");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Osobni automobili*/
		tmp_bd = bo071.ponder_three_param("624223","14777","1333");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Motocikli i ostala vozila (pretpostavlja se da je ponder 
		 * za col_cat_id = 624223, col_type_id = 14777 i col_sub_id = 5333 isti)*/
		tmp_bd = bo071.ponder_three_param("624223","14777","2333");//5333
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Kamioni, autobusi, pruzna vozila*/
		tmp_bd = bo071.ponder_three_param("624223","14777","3333");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Gradevnska vozila i gradevinski strojevi*/
		tmp_bd = bo071.ponder_three_param("624223","14777","4333");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Plovila*/
		tmp_bd = bo071.ponder_one_param("620223");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Zrakoplovi*/
		tmp_bd = bo071.ponder_two_param("621223","60777");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append(";");
		/*Dohvaca se ponder za Strojevi*/
		tmp_bd = bo071.ponder_two_param("621223","61777");
		buff.append(tmp_bd.toBigInteger().toString()).append("%").append("\n");
	
    	return buff.toString();
    }
	
	
	/**
	 * puni parametre koji idu u zaglavlje file-a za ponderirane...
	 * */
	private String naziviKolonaReporta() throws SQLException {
		StringBuffer buff = new StringBuffer();
		buff.append("Interni MB komitenta korisnika plasmana").append(";");
		buff.append("Naziv komitenta").append(";");
		buff.append("Partija plasmana").append(";");
		buff.append("Izloženost").append(";");
		buff.append("Gotovinski depozit").append(";");
		buff.append("Polica osiguranja sa štednom komponentom").append(";");
		buff.append("Garancija države").append(";");
		buff.append("Garancija druge banke").append(";");
        buff.append("Poljop.gospodarstvo,proizvodni objekt").append(";");
        buff.append("Posl.stamb.objekt,prodajno uslužni prostor, uredske prostorije").append(";");
        buff.append("Sklad., prod. i uredski prostori").append(";");
        buff.append("Skladišni prostor").append(";");
        buff.append("Stambeni objekt").append(";");
        buff.append("Hotel, pansion").append(";");
        buff.append("Hotelsko naselje").append(";");
        buff.append("Privatni smještaj").append(";");
        buff.append("Graðevinsko zemljište").append(";");
        buff.append("Poljoprivredno zemljište").append(";");
        buff.append("Osobni automobili").append(";");
        buff.append("Motocikli i ostala vozila").append(";");
        buff.append("Kamioni, autobusi, pružna vozila").append(";");
        buff.append("Graðevinska vozila i graðevinski strojevi").append(";");
        buff.append("Plovila").append(";");
        buff.append("Zrakoplovi").append(";");
        buff.append("Strojevi").append(";");
        buff.append("Osigurano").append(";");
        buff.append("Neosigurano").append("\n");
        
        return buff.toString();
	}
	
	private String naziviKolonaReporta2()
	{
		StringBuffer buff = new StringBuffer();
		buff.append("Interni MB komitenta").append(";");
		buff.append("Naziv komitenta").append(";");
		buff.append("Partija plasmana").append(";");
		buff.append("Izloženost").append(";");
		buff.append("Osigurano").append(";");
		buff.append("Vrsta kolaterala").append(";");
		buff.append("Ponder").append(";");
		buff.append("Osigurano").append(";");
		buff.append("Neosigurano").append("\n");
		return buff.toString();
	}
	
	 
	private void params_to_data(String [] ulazniParametri){
		
    	String [] parametri=null;
    	int i=0;
    	//System.out.println("params_to_data");
    	parametri=ulazniParametri;
    	//for (i=0;i<parametri.length;i++)
    		//System.out.println("parametri[" + String.valueOf(i) + "]=" + parametri[i]);
    		
    	dataVar.setDateOfReport(parametri[0]);   	
		dataVar.setRegister_no(parametri[1]);		
		dataVar.setClientType(parametri[2]);	
		dataVar.setPonder(parametri[3]);
		
		if(parametri[4].equalsIgnoreCase("B1")){
			dataVar.setEligibility(bo071.B1);
		}else  if(parametri[4].equalsIgnoreCase("B2")){
			dataVar.setEligibility(bo071.B2);
		}else  if(parametri[4].equalsIgnoreCase("RBA")){
			dataVar.setEligibility(bo071.RBA);
		}else  if(parametri[4].equalsIgnoreCase("B2IRB")){
			dataVar.setEligibility(bo071.B2IRB);
		}	
	}
	private void setConfig(BatchContext batchContext) {
  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = new String(dateFormat.format(bc.getExecStartTime()));

        this.outputFileName = "Collateral_crm_".concat(dateString).concat(".csv");
        this.outputFile = batchContext.getOutDir() + "/" + outputFileName;
        batchContext.debug("BO070, metoda setConfig(). Variable outputFileName=" + outputFileName);
        batchContext.debug("BO070, metoda setConfig(). Variable outputDir=" + outputFile);
        
        this.outputFileName2 = "CRM_analitika_".concat(dateString).concat(".csv");
        this.outputFile2 = batchContext.getOutDir() + "/" + outputFileName2;
        batchContext.debug("BO070, metoda setConfig(). Variable outputFileName2=" + outputFileName2);
        batchContext.debug("BO070, metoda setConfig(). Variable outputDir2=" + outputFile2);
    }
	
	private void insertBatchFileLogs(String file_name) throws Exception {
		YXYK1 insertFileLog = new YXYK1(bc);
		try{
			insertFileLog.setFileProcessType_Generated();
			insertFileLog.bank_sign = bc.getBankSign();
			insertFileLog.eve_id = bo071.getEveId();
			insertFileLog.file_name = "";
			insertFileLog.file_rec_num = null;
			insertFileLog.file_id_no = null;
			insertFileLog.insertBatchFileLog();
			bc.debug("BO070/insertLogs: Inserted into batch_file_log");
		}catch (Exception e){
			bc.debug("GRESKA U INSERT INTO BATCH_FILE_LOG");
			throw e;
		}
	}	

	
	 
	
	public void fillDataVarCRM() {
    	
    	//System.out.println("!!!!!!!!!fillDataVarCRM:!!!!!!!!!!");
		
		//Interni MB komitenta korisnika plasmana
		dataVarCRM.setDateOfReport(dataVar.getDateOfReport());//1
		
//		Interni MB komitenta korisnika plasmana
		dataVarCRM.setRegister_no(dataVar.getRegister_no());//2
		
//		Naziv komitenta
		dataVarCRM.setClient_name(dataVar.getName());//3
		
//		Partija plasmana
		dataVarCRM.setInvest_party(dataVar.getCus_acc_no());//4
		
//		Izloženost
		dataVarCRM.setExposure(dataVar.getExp_balance_hrk());//5
		
		/**
		 * stavlja da nije null kako bi mogao provjeravati funkcije
		 */
		if (dataVar.getCol_cat_id() == null) dataVar.setCol_cat_id(new BigDecimal("0"));
		if (dataVar.getCol_type_id() == null) dataVar.setCol_type_id(new BigDecimal("0"));
		if (dataVar.getSubtype_coll_str() == null) dataVar.setSubtype_coll_str("");
		if (dataVar.getSubtype_coll() == null) dataVar.setSubtype_coll(new BigDecimal("0"));
		
//		Gotovinski depozit
		if (dataVar.getCol_cat_id().equals(new BigDecimal("612223")))	
			dataVarCRM.setCash_deposit(dataVar.getExp_coll_amount());//1
		else 
			dataVarCRM.setCash_deposit(new BigDecimal(0));//1
		
//		Polica osiguranja sa štednom komponentom
		if (dataVar.getCol_cat_id().equals(new BigDecimal("616223")) && dataVar.getCol_type_id().equals(new BigDecimal("54777")))
			dataVarCRM.setAssur_policy(dataVar.getExp_coll_amount());//2
		else
		if (dataVar.getCol_cat_id().equals(new BigDecimal("616223")) && dataVar.getCol_type_id().equals(new BigDecimal("55777")) && (dataVar.getSubtype_coll_str()).substring(0,5).equals("MOZOS"))
			dataVarCRM.setAssur_policy(dataVar.getExp_coll_amount());//2
		else
			dataVarCRM.setAssur_policy(new BigDecimal(0));//2
		
		
		
//		Garancija države
		if (dataVar.getCol_cat_id().equals(new BigDecimal("615223"))  && dataVar.getCol_type_id().equals(new BigDecimal("30777")))
			dataVarCRM.setGuaranty_state(dataVar.getExp_coll_amount()); //3
		else
			dataVarCRM.setGuaranty_state(new BigDecimal(0));
		
		
//		Garancija druge banke		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("615223"))  && dataVar.getCol_type_id().equals(new BigDecimal("31777")))	
			dataVarCRM.setGuaranty_other_bank(dataVar.getExp_coll_amount()); //4
		else
			dataVarCRM.setGuaranty_other_bank(new BigDecimal(0)); //4
		
		
//		Poljop.gospodarstvo,proizvodni objekt		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("9777")) && (dataVar.subtype_coll.equals(new BigDecimal("1063223")) || dataVar.subtype_coll.equals(new BigDecimal("7222"))))			
			dataVarCRM.setReal_estate1(dataVar.getExp_coll_amount()); //5
		else
			dataVarCRM.setReal_estate1(new BigDecimal(0)); //5
		
		
//		Posl.stamb.objekt,prodajno uslužni prostor, uredske prostorije		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("9777")) && (dataVar.subtype_coll.equals(new BigDecimal("292825223")) || dataVar.subtype_coll.equals(new BigDecimal("9222")) || dataVar.subtype_coll.equals(new BigDecimal("8222"))))			
			dataVarCRM.setReal_estate2(dataVar.getExp_coll_amount()); //6
		else
			dataVarCRM.setReal_estate2(new BigDecimal(0)); //6
		
		
//		Sklad., prod. i uredski prostori		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("9777")) && dataVar.getSubtype_coll().equals(new BigDecimal("6524223")))
			dataVarCRM.setReal_estate3(dataVar.getExp_coll_amount()); //7
		else
			dataVarCRM.setReal_estate3(new BigDecimal(0)); //7
		
		
//		Skladišni prostor
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("12777")))
			dataVarCRM.setReal_estate4(dataVar.getExp_coll_amount()); //8
		else
			dataVarCRM.setReal_estate4(new BigDecimal(0)); //8
		
		
//		Stambeni objekt
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("8777")))
			dataVarCRM.setReal_estate5(dataVar.getExp_coll_amount());//9
		else	
			dataVarCRM.setReal_estate5(new BigDecimal(0));//9
		
		
//		Hotel, pansion
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("10777")) && (dataVar.getSubtype_coll().equals(new BigDecimal("10222")) || dataVar.getSubtype_coll().equals(new BigDecimal("11222"))))
			dataVarCRM.setReal_estate6(dataVar.getExp_coll_amount());//10
		else
			dataVarCRM.setReal_estate6(new BigDecimal(0));//10
		
		
//		Hotelsko naselje
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("10777")) &&  dataVar.getSubtype_coll().equals(new BigDecimal("31931223")))
			dataVarCRM.setReal_estate7(dataVar.getExp_coll_amount()); //11
		else
			dataVarCRM.setReal_estate7(new BigDecimal(0)); //11
		
		
//		Privatni smještaj
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("10777")) &&  dataVar.getSubtype_coll().equals(new BigDecimal("12222")))
			dataVarCRM.setReal_estate8(dataVar.getExp_coll_amount());//12	
		else
			dataVarCRM.setReal_estate8(new BigDecimal(0));//12
		
		
//		Graðevinsko zemljište
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("7777")) &&  dataVar.getSubtype_coll().equals(new BigDecimal("1222")))
			dataVarCRM.setReal_estate9(dataVar.getExp_coll_amount()); //11
		else
			dataVarCRM.setReal_estate9(new BigDecimal(0)); //11
		
		
//		Poljoprivredno zemljište
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223"))  && dataVar.getCol_type_id().equals(new BigDecimal("7777")) &&  dataVar.getSubtype_coll().equals(new BigDecimal("2222")))
			dataVarCRM.setReal_estate10(dataVar.getExp_coll_amount());//12	
		else
			dataVarCRM.setReal_estate10(new BigDecimal(0));//12
		
		
//		Osobni automobili
		if (dataVar.getCol_cat_id().equals(new BigDecimal("624223"))  && dataVar.getCol_type_id().equals(new BigDecimal("14777")) &&  dataVar.getSubtype_coll().equals(new BigDecimal("1333")))
			dataVarCRM.setPriv_cars(dataVar.getExp_coll_amount());//13	
		else
			dataVarCRM.setPriv_cars(new BigDecimal(0));//13
		
		
//		Motocikli i ostala vozila
		if (dataVar.getCol_cat_id().equals(new BigDecimal("624223"))  && dataVar.getCol_type_id().equals(new BigDecimal("14777")) && (dataVar.getSubtype_coll().equals(new BigDecimal("2333")) || dataVar.getSubtype_coll().equals(new BigDecimal("5333"))))
			dataVarCRM.setOther_veh(dataVar.getExp_coll_amount());//14
		else
			dataVarCRM.setOther_veh(new BigDecimal(0));//14
		
		
//		Kamioni, autobusi, pružna vozila
		if (dataVar.getCol_cat_id().equals(new BigDecimal("624223"))  && dataVar.getCol_type_id().equals(new BigDecimal("14777")) &&  dataVar.getSubtype_coll().equals(new BigDecimal("3333")))
			dataVarCRM.setTrucks_etc(dataVar.getExp_coll_amount());//15	
		else
			dataVarCRM.setTrucks_etc(new BigDecimal(0));//15
		
		
//		Graðevinska vozila i graðevinski strojevi
		if (dataVar.getCol_cat_id().equals(new BigDecimal("624223"))  && dataVar.getCol_type_id().equals(new BigDecimal("14777")) &&  dataVar.getSubtype_coll().equals(new BigDecimal("4333")))
			dataVarCRM.setBuilding_veh(dataVar.getExp_coll_amount());//16	
		else
			dataVarCRM.setBuilding_veh(new BigDecimal(0));//16
		
		
//		Plovila
		if (dataVar.getCol_cat_id().equals(new BigDecimal("620223")))
			dataVarCRM.setVessels(dataVar.getExp_coll_amount());//17	
		else
			dataVarCRM.setVessels(new BigDecimal(0));//17
		
		
//		Zrakoplovi
		if (dataVar.getCol_cat_id().equals(new BigDecimal("621223"))  && dataVar.getCol_type_id().equals(new BigDecimal("60777")))
			dataVarCRM.setAirplanes(dataVar.getExp_coll_amount());//18	
		else
			dataVarCRM.setAirplanes(new BigDecimal(0));//18
		
		
//		Strojevi
		if (dataVar.getCol_cat_id().equals(new BigDecimal("621223"))  && dataVar.getCol_type_id().equals(new BigDecimal("61777")))
			dataVarCRM.setMachines(dataVar.getExp_coll_amount());//19
		else
			dataVarCRM.setMachines(new BigDecimal(0));//19
			
 
	}
	
	
	
	
	/**
	 * Metoda koja stvara liniju kojom se puni file.
	 * Koristi se dataVarCRM objekt i usput se racuna ukupna pokrivenost koja treba za kolone 
	 * osigurano i neosigurano
	 * @return
	 */
	
	public String getLineOfReport() {
		
    	BigDecimal ukupnaPokrivenost = new BigDecimal(0);
    	//System.out.println("!!!!!!!!!getLineOfReport:!!!!!!!!!!");
    	StringBuffer line = new StringBuffer();
    	
    	//Interni MB komitenta korisnika plasmana
		line.append(dataVarCRM.getRegister_no()).append(";");//1 
		
		//Naziv komitenta
		line.append(dataVarCRM.getClient_name()).append(";");//2
		
		//Partija plasmana
		line.append(dataVarCRM.getInvest_party()).append(";");//3
		
		//Izloženost
		line.append(dataVarCRM.getExposure()).append(";");//4
		
		//Gotovinski depozit
		line.append(dataVarCRM.getCash_deposit()).append(";");//5
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getCash_deposit());
		
		//Polica osiguranja sa štednom komponentom
		line.append(dataVarCRM.getAssur_policy()).append(";");//6
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getAssur_policy());
		
		//Garancija države
		line.append(dataVarCRM.getGuaranty_state()).append(";");//7
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getGuaranty_state());
		
		//Garancija druge banke
		line.append(dataVarCRM.getGuaranty_other_bank()).append(";"); //8
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getGuaranty_other_bank());
		
		//Poljop.gospodarstvo,proizvodni objekt
		line.append(dataVarCRM.getReal_estate1()).append(";"); //9
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate1());
		
		//Posl.stamb.objekt,prodajno uslužni prostor, uredske prostorije
		line.append(dataVarCRM.getReal_estate2()).append(";"); //10
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate2());
		
		//Sklad., prod. i uredski prostori
		line.append(dataVarCRM.getReal_estate3()).append(";"); //13
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate3());
		
		//Skladišni prostor
		line.append(dataVarCRM.getReal_estate4()).append(";");//14
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate4());
		
		//Stambeni objekt
		line.append(dataVarCRM.getReal_estate5()).append(";");//15
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate5());
		
		//Hotel, pansion
		line.append(dataVarCRM.getReal_estate6()).append(";"); //16
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate6());
		
		//Hotelsko naselje
		line.append(dataVarCRM.getReal_estate7()).append(";");//17	
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate7());
		
		//Privatni smještaj
		line.append(dataVarCRM.getReal_estate8()).append(";");//18	
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate8());
		
		//Graðevinsko zemljište
		line.append(dataVarCRM.getReal_estate9()).append(";");//18	
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate9());
		
		//Poljoprivredno zemljište
		line.append(dataVarCRM.getReal_estate10()).append(";");//18	
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getReal_estate10());
		
		//Osobni automobili
		line.append(dataVarCRM.getPriv_cars()).append(";");//18	
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getPriv_cars());
		
		//Motocikli i ostala vozila
		line.append(dataVarCRM.getOther_veh()).append(";");//18
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getOther_veh());
		
		//Kamioni, autobusi, pružna vozila
		line.append(dataVarCRM.getTrucks_etc()).append(";");//18	
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getTrucks_etc());
		
		//Graðevinska vozila i graðevinski strojevi
		line.append(dataVarCRM.getBuilding_veh()).append(";");//18
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getBuilding_veh());
		
		//Plovila
		line.append(dataVarCRM.getVessels()).append(";");//18
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getVessels());
		
		//Zrakoplovi
		line.append(dataVarCRM.getAirplanes()).append(";");//18
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getAirplanes());
		
		//Strojevi
		line.append(dataVarCRM.getMachines()).append(";");//18
		ukupnaPokrivenost = ukupnaPokrivenost.add(dataVarCRM.getMachines());
		
		//Osigurano
		line.append(ukupnaPokrivenost).append(";");//18
		
		//Neosigurano
		line.append(dataVarCRM.getExposure().subtract(ukupnaPokrivenost)).append("\n");//18	
		
 
		return line.toString();
		  
 
	}
	
	
	private String getLineOfReport2(Iter3 iter) throws SQLException
	{
		StringBuffer buff = new StringBuffer();
		buff.append(iter.register_no()).append(";");
		buff.append(iter.name()).append(";");
		buff.append(iter.cus_acc_no()).append(";");
		buff.append(iter.exp_balance_hrk()).append(";");
		buff.append(iter.exp_coll_amount()).append(";");
		buff.append(iter.col_type_name()).append(";");
		buff.append(iter.ponder()).append(";");
		buff.append(iter.exp_coll_amount()).append(";");
		buff.append(iter.exp_balance_hrk().subtract(iter.exp_coll_amount())).append("\n");
		return buff.toString();
	}
	
}
