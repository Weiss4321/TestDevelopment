/*
 * Created on 2006.08.03
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo00;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

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
public class BO000 extends Batch{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo00/BO000.java,v 1.7 2006/09/22 08:06:07 hrazst Exp $";
	/**
	 * Batch za ispis unesenih kolaterala. 
	 */
	
    private String configFile;

    private String outputFileName;

    private String outputFile;
	
    private String [] ulazniParametri = new String[15];
    
	private BatchContext bc = null;
	
	private BO001 bo001 = null;
	
	private JDBC jdbc=null;
	
	private Vector dataCSV=null;
	
	private static final String encoding = "Cp1250";
	
	public BO000() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String executeBatch(BatchContext batchContext) throws Exception {
        
        this.bc = batchContext;
        bc.debug("******************** BO000.executeBatch() started *********************");
        
        this.bo001 =new BO001(bc);
        this.jdbc = new JDBC();
    	ResultSet resultSet=null;
        HashMap parameters = new HashMap();
    	int i = 0;
    	
    	if(checkArgs()==false){
			return RemoteConstants.RET_CODE_ERROR;
    	}
        
    	bc.beginTransaction();
    	//logiranje poziva izvrsavanja batcha u tablicu Event
        try{
        	bo001.insertIntoEvent();
        }catch (Exception e) {
            e.printStackTrace();
            return RemoteConstants.RET_CODE_ERROR;
        }
        bc.commitTransaction();
        bc.debug("Logiranje poziva batch-a logirano u tablicu Event.");
        
        try {
        	setConfigNekretnine(bc);
        	
        	bc.debug("Pokretanje JDBC upita.");
        	resultSet = jdbc.resultSetNekretnina(bc, ulazniParametri);
        	bc.debug("Pokretanje JDBC upita uspjesno zavrseno.");
        	
        	bc.debug("Pokretanje generiranja data vectora za CSVGenerator.");
        	createCSV(resultSet);
        	bc.debug("Pokretanje generiranja data vectora za CSVGenerator uspjesno zavrseno.");
        	
        	bc.debug("Generiranja data vectora za header");
        	parameters=headerData();
        	bc.debug("Generiranja data vectora za header završeno.");
        	
        	CSVGenerator.printCSV("csv067", bc.getLogin(), dataCSV, parameters, this.configFile, this.outputFile, encoding);
        	YXY70.send(batchContext, "csv067", bc.getLogin() , outputFile);
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
	
	private void insertBatchFileLogs(String file_name) throws Exception {
		YXYK1 insertFileLog = new YXYK1(bc);
		insertFileLog.setFileProcessType_Generated();
		insertFileLog.bank_sign = bc.getBankSign();
		insertFileLog.eve_id = bo001.getEveId();
		insertFileLog.file_name = file_name;
		insertFileLog.file_rec_num = null;
		insertFileLog.file_id_no = null;
		insertFileLog.insertBatchFileLog();
		bc.debug("BO000/insertLogs: Inserted into batch_file_log");
	}
	
	private boolean checkArgs(){
		
		String pomString=null;
		String delimiter=null;
		int brojac=0;
		int brojac1=0;
		int pozicija=0;
		int i=0;

		if (bc.getArgs().length!=3){
			bc.debug("Neispravan broj argumenata.");
			return false;
		}
		delimiter=bc.getArg(0);
		pomString=bc.getArg(1);
		
		if(delimiter.length()!=1){
			bc.debug("Delimiter se sastoji od vise znakova.");
			return false;			
		}
		
		for(i=0;i<pomString.length();i++){
			brojac1=i;
			if (pomString.charAt(i)==delimiter.charAt(0)){
				ulazniParametri[pozicija]=pomString.substring(brojac,brojac1);
				System.out.println(ulazniParametri[pozicija]);
				pozicija++;
				brojac=brojac1+1;
			}
			if(i==pomString.length()-1){
				ulazniParametri[pozicija]=pomString.substring(brojac,pomString.length());
			}
		}
					
		for (i=0;i<bc.getArgs().length;i++){
			bc.debug("arg[" + String.valueOf(i) + "]=" + bc.getArg(i));
		}
		for(i=0;i<ulazniParametri.length;i++){
			bc.debug("SQL_param[" + String.valueOf(i) + "]=" + ulazniParametri[i]);
		}
		//bc.setBankSign("RB");
		bc.setBankSign(bc.getArg(2));
		
		return true;
	}
	
    private void setConfigNekretnine(BatchContext batchContext) {
        String Path = System.getProperty("user.dir") + "/" + this.getClass().getPackage().getName().replace('.', '/');
        this.configFile = Path + "/nek_struct.xml";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = new String(dateFormat.format(bc.getExecStartTime()));

        this.outputFileName = "Nekretnine_".concat(dateString).concat(".csv");
        this.outputFile = batchContext.getOutDir() + "/" + outputFileName;
        batchContext.debug("BO000, metoda setConfigNekretnine(). Variable configFile=" + this.configFile);
        batchContext.debug("BO000, metoda setConfigNekretnine(). Variable path=" + Path );
        batchContext.debug("BO000, metoda setConfigNekretnine(). Variable outputDir=" + outputFile);
    }
	
    private HashMap headerData(){
    	HashMap param=new HashMap();
    	String naslov=null;
    
    	
    	if (ulazniParametri[12].compareTo("")!=0){
    		naslov="Ispis nekretnina za referenta : " + bo001.getUseOpenName(ulazniParametri[12]);
    	}else{
    		if (ulazniParametri[0].compareTo("M")==0){
    			naslov="Ispis nekretnina na monitoring listi.";
    		}else if(ulazniParametri[0].compareTo("M")==0){
    			naslov="Ispis nekretnina po arhivskoj listi.";
    		}
    	}
    	String datum = "Za datumski period : " + ulazniParametri[13] + " - " + ulazniParametri[14];
    	String orgUnit = "za organizacijsku jedinicu : " + bo001.getOrgUnitName(ulazniParametri[11]);    		
    	
		param.put("naslov",naslov);
		param.put("datum",datum);
		param.put("orgUnit",orgUnit);
    		
    	return param;
    }
    
    
	public void createCSV(ResultSet rs) throws SQLException{
		String pomString=null;
		dataCSV = new Vector();
		int i=1;
		
		try{
			while(rs.next()){
				HashMap row=new HashMap();
				row.put("c0","Nekretnina " + String.valueOf(i));
				row.put("c1",rs.getString(51));//col_num
				row.put("c2",rs.getString(2));//col_desc
				row.put("c3",rs.getString(3));//coll_type_code
				row.put("c4",rs.getString(4));//coll_type_name
				row.put("c5",rs.getString(5));//real_es_type_desc
				row.put("c6",rs.getString(6));
				row.put("c7",rs.getString(7));
				row.put("c8",rs.getString(8));
				row.put("c9",rs.getString(9));
				row.put("c10",rs.getString(10));
				row.put("c11",rs.getString(11));
				row.put("c12",rs.getString(12));
				row.put("c13",rs.getString(13));
				row.put("c14",rs.getString(14));
				pomString = convertDecimalToString(rs.getBigDecimal(15));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c15", pomString);
				pomString = convertDecimalToString(rs.getBigDecimal(16));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c16",pomString);
				row.put("c17",rs.getString(17));
				row.put("c18",rs.getString(18));
				row.put("c19",getDDMMYYYY(rs.getDate(19)));
				row.put("c20",getDDMMYYYY(rs.getDate(20)));
				row.put("c21",getDDMMYYYY(rs.getDate(21)));
				row.put("c22",getDDMMYYYY(rs.getDate(22)));
				row.put("c23",rs.getString(23));
				pomString = convertDecimalToString(rs.getBigDecimal(24));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c24",pomString);
				row.put("c25",getDDMMYYYY(rs.getDate(25)));
				pomString = convertDecimalToString(rs.getBigDecimal(26));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c26",pomString);
				row.put("c27",getDDMMYYYY(rs.getDate(27)));	
				pomString = convertDecimalToString(rs.getBigDecimal(28));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c28",pomString);
				row.put("c29",getDDMMYYYY(rs.getDate(29)));
				pomString = convertDecimalToString(rs.getBigDecimal(30));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c30",pomString);
				row.put("c31",getDDMMYYYY(rs.getDate(31)));
				pomString = convertDecimalToString(rs.getBigDecimal(32));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c32",pomString);
				row.put("c33",getDDMMYYYY(rs.getDate(33)));
				row.put("c34",rs.getString(34));
				pomString = convertDecimalToString(rs.getBigDecimal(35));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c35",pomString);
				row.put("c36",getDDMMYYYY(rs.getDate(36)));
				row.put("c37",rs.getString(37));
				pomString = convertDecimalToString(rs.getBigDecimal(38));
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + rs.getString(50);
				}
				row.put("c38",pomString);
				row.put("c39",getDDMMYYYY(rs.getDate(39)));
				row.put("c40",rs.getString(40));
				row.put("c41",rs.getString(41));
				row.put("c42",rs.getBigDecimal(42).toString());
				row.put("c43",rs.getBigDecimal(43).toString());
				row.put("c44",rs.getBigDecimal(44).toString());
				row.put("c41",rs.getString(45));
				//ako se trazi referentska lista onda se na kraju nekretnine ne ispisuje user koji je unio nekretninu
				if (ulazniParametri[0].compareToIgnoreCase("M")==0 || ulazniParametri[0].compareToIgnoreCase("A")==0){
					row.put("c46",rs.getString(46));
					row.put("c47",rs.getString(47));
				}else{
					row.put("c46","");
					row.put("c47","");					
				}
				
				//bc.debug("Redak :" + row.toString());
				dataCSV.add(zaglavljeNekretnina());
				dataCSV.add(row);				
				i+=1;
				try{
					bc.debug("Punjenje vectora data sa vrijednostima vlasnika za nekretninu " + i + ".");
					fillVlasnike(rs.getBigDecimal(1));
				}catch (SQLException sqle){
					if (sqle.getErrorCode()!=100) throw sqle;
				}
				try{
					bc.debug("Punjenje vectora data sa vrijednostima hipoteku za nekretninu " + i + ".");
					fillHipoteka(rs.getBigDecimal(1),ulazniParametri[10]);	
				}catch (SQLException sqle){
					if (sqle.getErrorCode()!=100) throw sqle;
				}
				try{
					bc.debug("Punjenje vectora data sa vrijednostima polica za nekretninu " + i + ".");
					fillPolice(rs.getBigDecimal(1));
				}catch (SQLException sqle){
					if (sqle.getErrorCode()!=100) throw sqle;
				}
				dataCSV.add(prazanRed());
			}			
		}catch(SQLException sqle){
			
		}catch(Exception e){
			
		}
	}
	
	public void fillVlasnike(BigDecimal colHeaId) throws SQLException{
		
		BO001.iteratorVlasnici iteratorV = null;
		int i=1;
		
		try{
			bc.debug("Punjenje vectora data sa vrijednostima vlasnika za nekretninu sa col_hea_id=" + colHeaId + ".");
			iteratorV = bo001.fetchVlasnike(colHeaId);
			while(iteratorV.next()){
				HashMap row = new HashMap();
				if (i==1) dataCSV.add(zaglavljeVlasnika());				
				row.put("c0","");
				row.put("c1","Vlasnik " + String.valueOf(i));
				row.put("c2",iteratorV.code());
				row.put("c3",iteratorV.first_name());
				row.put("c4",iteratorV.surname());
				row.put("c5","'" + iteratorV.part_id().trim() + "/" + iteratorV.part_id2().trim() + "'");

				//bc.debug("Redak :" + row.toString());
				dataCSV.add(row);
				i+=1;
			}
		}catch (SQLException sqle){
			if (sqle.getErrorCode()==100){
				bc.debug("Nije pronaden niti jedan vlasnik za nekretninu sa col_hea_id=" + colHeaId + ".");
			}
			throw sqle;
		}
		bc.debug("Vlasnici za nekretninu sa col_hea_id=" + colHeaId + " uspjesno dodani.");
	}
	
	public void fillHipoteka(BigDecimal colHeaId, String parPlasmana) throws SQLException{
		
		String pomString=null;
		BO001.iteratorHipoteke iteratorH = null;
		int i=1;
		
		try{
			bc.debug("Punjenje vectora data sa vrijednostima hipoteka za nekretninu sa col_hea_id=" + colHeaId + ".");
			iteratorH = bo001.fetchHipoteke(colHeaId, parPlasmana); //arg(10) - partija plasmana
			while(iteratorH.next()){
				HashMap row = new HashMap();
				if (i==1) dataCSV.add(zaglavljeHipoteke());
				row.put("c0","");
				row.put("c1","Hipoteka " + String.valueOf(i));
				row.put("c2",iteratorH.pokrice());
				row.put("c3",iteratorH.is_part_agreem());
				row.put("c4",iteratorH.name());
				pomString = convertDecimalToString(iteratorH.amount_ref());
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + iteratorH.code_char();
				}
				row.put("c5",pomString);
				row.put("c6",iteratorH.vrstaTereta());
				row.put("c7",iteratorH.hf_priority());
				row.put("c8",getDDMMYYYY(iteratorH.hf_date_hfc_from()));
				row.put("c9",getDDMMYYYY(iteratorH.hf_date_hfc_until()));
	
				//bc.debug("Redak :" + row.toString());
				dataCSV.add(row);
				i+=1;
				try{
					fillPlasmani(iteratorH.coll_hf_prior_id(),parPlasmana);
				}catch (SQLException sqle){
					if (sqle.getErrorCode()!=100) throw sqle;
				}
			}
		}catch (SQLException sqle){
			if (sqle.getErrorCode()==100){
				bc.debug("Nije pronadena niti jedana hipoteka za nekretninu sa col_hea_id=" + colHeaId + ".");
			}
			throw sqle;
			
		}
		bc.debug("Hipoteke za nekretninu sa col_hea_id=" + colHeaId + " uspjesno dodane.");
	}
	
	public void fillPlasmani(BigDecimal collHfPriorId, String parPlasmana) throws SQLException{
		BO001.iteratorPlasmani iteratorP = null;
		int i=1;
		
		try{
			bc.debug("Punjenje vectora data sa vrijednostima plasmana za hipoteku sa coll_hf_prior_id=" + collHfPriorId + ".");
			iteratorP = bo001.fetchPlasmane(collHfPriorId, parPlasmana); //arg(10) - partija plasmana
			while(iteratorP.next()){
				HashMap row = new HashMap();
				if (i==1) dataCSV.add(zaglavljePlasmana());
				row.put("c0","");
				row.put("c1","");
				row.put("c2","Plasman " + String.valueOf(i));
				row.put("c3",iteratorP.register_no());
				row.put("c4",iteratorP.name());
				row.put("c5",iteratorP.request_no());
				row.put("c6",iteratorP.acc_no());
				row.put("c7",iteratorP.priority_no());

				//bc.debug("Redak :" + row.toString());
				dataCSV.add(row);
				i+=1;
			}
		}catch (SQLException sqle){
			if (sqle.getErrorCode()==100){
				bc.debug("Nije pronaden niti jedana plasman za hipoteku sa coll_hf_prior_id=" + collHfPriorId + ".");
			}
			throw sqle;
		}
		bc.debug("Plasmani za hipoteku sa coll_hf_prior_id=" + collHfPriorId + " uspjesno dodani.");
	}

	public void fillPolice(BigDecimal colHeaId) throws SQLException{
		
		String pomString=null;
		BO001.iteratorPolice iteratorP = null;
		int i=1;
		
		try{
			bc.debug("Punjenje vectora data sa vrijednostima polica za nekretninu sa col_hea_id=" + colHeaId + ".");
			iteratorP = bo001.fetchPolice(colHeaId);
			while(iteratorP.next()){
				HashMap row = new HashMap();
				if (i==1) dataCSV.add(zaglavljePolice());
				row.put("c0","");
				row.put("c1","Polica " + String.valueOf(i));
				row.put("c2",iteratorP.ip_code());
				row.put("c3",iteratorP.ic_name());
				row.put("c4",iteratorP.int_pol_type_name());
				pomString = convertDecimalToString(iteratorP.ip_secu_val());
				if (pomString.compareTo("")!=0){
					pomString = pomString + " " + iteratorP.code_char();
				}
				row.put("c5",pomString);
				row.put("c6",getDDMMYYYY(iteratorP.ip_vali_from()));
				row.put("c7",getDDMMYYYY(iteratorP.ip_vali_until()));

				//bc.debug("Redak :" + row.toString());
				dataCSV.add(row);
				i+=1;
			}
		}catch (SQLException sqle){
			if (sqle.getErrorCode()==100){
				bc.debug("Nije pronadena niti jedana polica za nekretninu sa col_hea_id=" + colHeaId + ".");
			}
			throw sqle;
		}
		bc.debug("Police za nekretninu sa col_hea_id=" + colHeaId + " uspjesno dodane.");
	}
	
	private HashMap zaglavljeNekretnina(){
		HashMap row=new HashMap();
		row.put("c0","NEKRETNINA");
		row.put("c1","\u0160IFRA");//col_hea_id
		row.put("c2","OPIS");//col_desc
		row.put("c3","\u0160IFRA KOLATERALA");//coll_type_code
		row.put("c4","TIP KOLATERALA");//coll_type_name
		row.put("c5","VRSTA NEKRETNINE");
		row.put("c6","\u0160IFRA SUDA");
		row.put("c7","NAZIV SUDA");
		row.put("c8","\u0160IFRA KAT. OP\u0106INE");
		row.put("c9","KATASTARSKA OP\u0106INA");
		row.put("c10","KATASTARSKA \u010CESTICA");
		row.put("c11","BROJ ZKU");
		row.put("c12","PODULO\u017DAK");
		row.put("c13","UPORABNA DOZVOLA");
		row.put("c14","NEKRETNINA OSIGURANA");
		row.put("c15","TR\u017DI\u0160NA VRIJEDNOST");
		row.put("c16","NOVA GRA\u0110. VRIJEDNOST");
		row.put("c17","PREDANA DOKUMENTACIJA");
		row.put("c18","NEDOSTAJU\u0106A DOKUMENTACIJA");
		row.put("c19","DATUM DO KADA TREBA PREDAT DOKUMENTACIJU");
		row.put("c20","DATUM PREDAJE CJELOKUPNE DOKUMENTACIJE");
		row.put("c21","DATUM DO KADA MORA BITI UPISANA FIDUCIJA ILI HIPOTEKA U KORIST BANKE");
		row.put("c22","DATUM UPISA FIDUCIJE ILI HIPOTEKE");
		row.put("c23","JE LI UPISANO PRAVO BANKE NA UMJETNINU");
		row.put("c24","NOM. VRIJ. c.o.");
		row.put("c25","DATUM NOM. VRIJ. c.o.");
		row.put("c26","NOMINALNA VRIJ. GRA\u0110. c.o.");
		row.put("c27","DATUM NOM. VRIJ. GRA\u0110. c.o.");	
		row.put("c28","NOM. VRIJ. ZRMLJI\u0160TA c.o.");
		row.put("c29","DATUM NOM. VRIJ. ZRMLJI\u0160TA c.o.");
		row.put("c30","ORIGINALNA VRIJ. c.o.");
		row.put("c31","DATUM ORIGINALNE VRIJ. c.o.");
		row.put("c32","GRA\u0110. VRIJ.");
		row.put("c33","DATUM GRA\u0110. VRIJ.");
		row.put("c34","DODATNI PODACI");
		row.put("c35","IZNOS KOEF. REVALORIZACIJE");
		row.put("c36","DATUM REVALORIZACIJE");
		row.put("c37","REVALORIZACIJA URE\u0110ENA A R I");
		row.put("c38","REVALORIZIRANA VRIJ. KOLATERALA IZ PRED. PERIODA BRV");
		row.put("c39","DATUM REVALORIZACIJA PRED. PERIODA");
		row.put("c40","REVALORIZACIJA URE\u0110ENA U PRED. PERIODU A R I");
		row.put("c41","AMORTIZACIJSKI VIJEK");
		row.put("c42","PERIOD IZRA\u010CUNA IZNOSA AMORTIZACIJE");
		row.put("c43","PERIODI\u010CNI IZNOS AMORTIZACIJE");
		row.put("c44","POVR\u0160INA m2");
		row.put("c41","PRIHVATLJIVOST");
		//ako se trazi referentska lista onda se na kraju nekretnine ne ispisuje user koji je unio nekretninu
		if (ulazniParametri[0].compareToIgnoreCase("M")==0 || ulazniParametri[0].compareToIgnoreCase("A")==0){
			row.put("c46","KORISNIK KOJI JE UNIO KOLATERAL");
			row.put("c47","IME I PREZIME KORISNIKA");
		}else{
			row.put("c46","");
			row.put("c47","");					
		}
		return row;
	}
	
	private HashMap zaglavljeVlasnika(){
		HashMap row = new HashMap();
		row.put("c0","");
		row.put("c1","VLASNIK");
		row.put("c2","MB");
		row.put("c3","IME");
		row.put("c4","PREZIME");
		row.put("c5","VLASNI\u0160TVO");
		
		return row;
	}
	
	private HashMap zaglavljeHipoteke(){
		HashMap row = new HashMap();
		row.put("c0","");
		row.put("c1","HIPOTEKA");
		row.put("c2","NA\u010CIN POKRI\u0106A PLASMANA");
		row.put("c3","JE LI DIO SPORAZUMA");
		row.put("c4","NAZIV KORISNIKA UPISANOG PLASMANA");
		row.put("c5","ORIGINALAN IZNOS TERETA");
		row.put("c6","VRSTA TERETA");
		row.put("c7","PRIORITET");
		row.put("c8","VA\u017DENJE TERETA OD");
		row.put("c9","VA\u017DENJE TERETA DO");
		
		return row;
	}

	private HashMap zaglavljePlasmana(){
		HashMap row = new HashMap();
		row.put("c0","");
		row.put("c1","");
		row.put("c2","PLASMAN");
		row.put("c3","MB KORISNIKA PLASMANA");
		row.put("c4","NAZIV KORISNIKA PLASMANA");
		row.put("c5","BROJ ZAHTJEVA");
		row.put("c6","PARTIJA PLASMANA");
		row.put("c7","PRIORITET");
		
		return row;
	}

	private HashMap zaglavljePolice(){
		HashMap row = new HashMap();
		row.put("c0","");
		row.put("c1","POLICA");
		row.put("c2","\u0160IFRA POLICE");
		row.put("c3","OSIGURAVATELJ");
		row.put("c4","VRSTA POLICE OSIGURANJA");
		row.put("c5","OSIGURANA SVOTA");
		row.put("c6","PREMIJA PLA\u010CENA OD");
		row.put("c7","PREMIJA PLA\u010CENA DO");
		
		return row;
	}
	
	private HashMap prazanRed(){
		HashMap row = new HashMap();
		
		return row;
	}
	
	
	
	/**
     * vraca String datum zapisan u formatu dd.MM.yyyy 
     * @param date
     * @return
     */
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
     * Metoda pretvara decimalan broj u strint u formatu #,##0.00
     * @param broj BigDecimal
     * @return String
     */
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
    
	public static void main(String[] args) {
        int i=0;
		BatchParameters batchParameters = new BatchParameters(new BigDecimal("1635499003.0"));
        batchParameters.setArgs(args);
        new BO000().run(batchParameters);
	}
}
