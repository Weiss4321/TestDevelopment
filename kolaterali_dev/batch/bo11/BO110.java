/*
 * Created on 2007.05.16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo11;

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
import java.util.HashMap;
import java.util.Vector;
import java.sql.ResultSet;
/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BO110 extends Batch {
	
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo11/BO110.java,v 1.11 2014/05/27 14:22:02 hraaks Exp $";
	
	BatchContext bc = null;
	
	private String [] ulazniParametri = null;
	
	private BO11Data dataVar = new BO11Data();
	
	private String configFile = null;
	
	private String outputFileName;
	
	private String outputFile;
	
	private static final String encoding = "Cp1250";
	
	private hr.vestigo.modules.collateral.batch.bo11.BO112 bo112 = null;
	
	private BO111 bo111 = null;

	private Vector xml_data = null;
	
	HashMap row;
	public String executeBatch(BatchContext bc) throws Exception{
		
	
		this.bc = bc;
		
		this.bo111 = new BO111(bc);
        // dodano new jel se nije prije nigdej instancirao vektor
        xml_data = new Vector();
		
		HashMap parameters = new HashMap();
		
		//da li je dobar broj parametra dobiven iz aplikacije, odnosno s komandne linije
		if (checkArgs() == false)
			return RemoteConstants.RET_CODE_ERROR;
		
		bc.beginTransaction();
//		logiranje poziva izvrsavanja batcha u tablicu Event
		try {
			bc.updateEveID(bo111.insertIntoEvent());
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
				
				createCSV();
				
				//postavlja parametre zaglavlja izvjesca
				parameters=headerData(dataVar.getDateOfReview());
				
				CSVGenerator.printCSV("csv101",bc.getLogin(),xml_data,parameters,configFile,outputFile,encoding);
				
				YXY70.send(bc,"csv101", bc.getLogin() , outputFile);
				
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
		BatchParameters bp = new BatchParameters(new BigDecimal("1723307003"));
		bp.setArgs(args);
		new BO110().run(bp);
	}
	
	private boolean checkArgs() {
		int i;
		for (i = 0; i < bc.getArgs().length; i++)
			bc.debug("bc_Arg[" + String.valueOf(i) + "] = " + bc.getArg(i));
		
		if (bc.getArgs().length != 6 && bc.getArgs().length != 1)
		{	
			bc.debug("Neispravan broj argumenata.");
			return false;
		}
		
		//ako dobiva parametre s komanden linije,... inace (ako iz aplikacije)
		if (bc.getArgs().length == 1) {
			ulazniParametri=new String[6];
			ulazniParametri[0] = "02.07.2007";
			ulazniParametri[1] = "";
			ulazniParametri[2] = "P";
			ulazniParametri[3] = "";
			ulazniParametri[4] = "";
			bc.setBankSign(bc.getArg(0));
		}else {
			ulazniParametri = new String[6];
			for (i=0; i < bc.getArgs().length; i++) {
				ulazniParametri[i] = bc.getArg(i).trim();
				bc.debug("SQL_param[" + String.valueOf(i) + "] = " + ulazniParametri[i]);
			}
			bc.setBankSign(ulazniParametri[5]);
		
		}
		
		return true;
	
	}
	
	private void params_to_data(String [] ulazniParametri) {
	
		String [] parametri = null;
		
		int i = 0;
		
		parametri = ulazniParametri;
		
		
		dataVar.setDateOfReview(parametri[0]);
		
		dataVar.setRegisterNo(parametri[1]);
		
		dataVar.setClientGroup(parametri[2]);
		
		dataVar.setCollCategoryCode(parametri[3]);
		
		dataVar.setCollTypeCode(parametri[4]);
		
		
	}
	
	private void setConfig() {
		String Path = System.getProperty("user.dir") + "/" + this.getClass().getPackage().getName().replace('.','/');
		
		this.configFile = Path + "/coll_review.xml";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		
		String dateString = new String(dateFormat.format(bc.getExecStartTime()));
		
		this.outputFileName = "Collateral_review".concat(dateString).concat(".csv");
		
		this.outputFile = bc.getOutDir() + "/" + outputFileName;
		
		bc.debug("BO110, metoda setConfig() Variable configFile" + configFile);
		bc.debug("BO110, metoda setConfig() Variable path" + Path);
		bc.debug("BO110, metoda setConfig() Variable outputDir" + outputFile);
			
	}
	
	
	private void createCSV() throws SQLException, Exception {
		//u ovu varijablu se dohvacaju podaci iz BO112-a
		ResultSet resultSet = null;
		
		row = new HashMap();
		
		BO111.Iter iterator = null;
		
		this.bo112 = new hr.vestigo.modules.collateral.batch.bo11.BO112();
		try {
			bc.debug("Pokretanje BO112 upita.");
			resultSet = bo112.resultSetClientAndInvestment(bc,dataVar);
			bc.debug("Pokretanje BO112 upita uspjesno zavrseno.");
            int i =0;
            
            String old_cus_acc_no=null;
			while (resultSet.next())
			{
			    
                
                bc.debug("\n\n\nnext"+ i++);
				//brise podatke pozadinskog objekta koji se dohvacaju
				dataVar.resetFetched();
				
				row.clear();
				row.put("PROC_DATE",dataVar.getDateOfReview());
                bc.debug("PROC_DATE"+dataVar.getDateOfReview());
				
                row.put("REPORT_IND",new String("1"));
				
                row.put("REGISTER_NO",resultSet.getString(3));//register_no iz tablice CUSTOMER
				
                row.put("NAME",resultSet.getString(4));//name iz tablice CUSTOMER
				
                row.put("MODULE_CODE",resultSet.getString(5));//module_code iz tablic CUSACC_EXPOSURE
				
                row.put("DOM_OJ",resultSet.getString(6)); //code iz tablice ORGANIZATION_UNIT
				
                row.put("CUS_ACC_NO",resultSet.getString(7));
				
                row.put("ECO_SEC",resultSet.getString(8));
				
                row.put("B2ASSET_CLASS",resultSet.getString(9));
				
                row.put("CUSTOMER_INDIC",resultSet.getString(10));
                // promijenio amir jel je bilo getString a trebalo je biti get Date
				row.put("DUE_DATE",resultSet.getDate(11));
				
                row.put("APPROVAL_DATE",resultSet.getDate(12));
                // do tud sam promijenio
				row.put("CURRENCY_TYPE",resultSet.getString(13));
//				row.put("contract_cur_id",resultSet.getString(14));
				row.put("CODE_CHAR",resultSet.getString(15));
                //todo amir promijenio bilo je getString a treba biti getBigDecimal
				row.put("EXPOSURE_BALANCE",resultSet.getBigDecimal(19));
				row.put("EXP_BAL_BIL",resultSet.getBigDecimal(16));
				row.put("EXP_BAL_OUTBIL",resultSet.getBigDecimal(17));
                // do tud sam promijenio
				
				dataVar.setCol_hea_id(new BigDecimal(resultSet.getString(2)));
				dataVar.setCol_cat_id(new BigDecimal(resultSet.getString(20)));
				dataVar.setCol_type_id(new BigDecimal(resultSet.getString(21)));
				dataVar.setAmount(resultSet.getBigDecimal(18));
				// dohvat podataka o PODTIPU kolaterala
				dataVar.setSubtypeData(bo111.fetchDataAboutSubtype(dataVar));
				
				//ispunjava kolone izvjesca koja imaju veze sa pokrivenosti
				organizeRowExposureData();
				
				dataVar.setValuta_depozita(new BigDecimal(resultSet.getString(22)));//REAL_EST_NM_CUR_ID iz tablic COLL_HEAD
				dataVar.setValuta_plasmana(new BigDecimal(resultSet.getString(14)));//contract_cur_id iz tadl. CUSACC_EXPOSURE
				
				organizeRowCashDepData();
				
				organizeRowGuarantyData(bo111.fetchCustomerEco_sec(dataVar.getCol_hea_id()));
				
				//obrisati sve slogove iz pomocne tabele CLT_PODLOGA 1 od prethodnog izvjesca
				
				
				
               //todo
                if(old_cus_acc_no!= null && !old_cus_acc_no.equals(resultSet.getString(7).trim())){
                    
                    iterator = bo111.fetchDataFromCLT_Podloga1();
                    
                    bo111.eraseDataInCLT_PODLOGA1Table();
                
                
                    insertXMLLine(iterator);
                }
                
                bo111.insertDataIntoCLT_PODLOGA1Table(row,dataVar);
                
                old_cus_acc_no=resultSet.getString(7).trim();
                
                //row.clear();
                    
			}
		}catch(SQLException sqle){
			throw sqle;
		}catch(Exception e){
			throw e;
		}
	
	}
	
	private HashMap headerData(Date reviewDate) {
		HashMap row=new HashMap();
		row.put("datum",getDDMMYYYY(reviewDate));
    	row.put("naslov","Izvjesce Podloga1 s neponderiranim vrijednostima.");
    	
    	return row;
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
	 * Ureduje podatke za izvjesce koji su vezani za pokrivenost
	 * */
	public void organizeRowExposureData() {
			
    	System.out.println("organizeRowData!!!!!!");
	
    	BigDecimal ukupnaPokrivenost = new BigDecimal(0);
		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("612223")))	
			{
				row.put("AMOUNT_CASH",dataVar.getAmount());
				ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
			}
		else 
			row.put("AMOUNT_CASH",new BigDecimal(0));
		
		
		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("616223")) && dataVar.getCol_type_id().equals(new BigDecimal("54777")))
		{
			row.put("AMOUNT_POL",dataVar.getAmount());
			ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
		}
		else
		if (dataVar.getCol_cat_id().equals(new BigDecimal("616223")) && dataVar.getCol_type_id().equals(new BigDecimal("55777")) && ((String)dataVar.getSubtypeData()).substring(0,5).equals("MOZOS"))
		{
			row.put("AMOUNT_POL",dataVar.getAmount());
			ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
		}
		else
			row.put("AMOUNT_POL",new BigDecimal(0));
		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("615223")))
		{
				if (dataVar.getCol_type_id().equals(new BigDecimal("30777")))
				{
					row.put("AMOUNT_GAR_S",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_GAR_S",new BigDecimal(0));
				
				if (dataVar.getCol_type_id().equals(new BigDecimal("31777")))	
				{
					row.put("AMOUNT_GAR_B",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_GAR_B",new BigDecimal(0)); 
		}
		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("618223")))
		{		
			if (dataVar.getCol_type_id().equals(new BigDecimal("8777")))			
			{
				row.put("AMOUNT_NEK",dataVar.getAmount());
				ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
			}
			else
				row.put("AMOUNT_NEK",new BigDecimal(0)); 
		
		
			if (dataVar.getCol_type_id().equals(new BigDecimal("7777")))
			{
				if(dataVar.getSubtypeData().toString().equals("1222"))			
				{
					row.put("AMOUNT_GZEM",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_GZEM",new BigDecimal(0)); 
		
		
				if (dataVar.getSubtypeData().toString().equals("2222") || dataVar.getSubtypeData().toString().equals("3222"))
				{
					row.put("AMOUNT_PZEM",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_PZEM",new BigDecimal(0)); 
		
		
			}
			    
			if (dataVar.getCol_type_id().equals(new BigDecimal("9777")))
			{
                // amir promijenio jel je bio string a sad je bigdecimal pretvoren u string
                    bc.debug("dataVar.getSubtypeData() :"+dataVar.getSubtypeData());
					if (dataVar.getSubtypeData().toString().equals("7222") || dataVar.getSubtypeData().toString().equals("459223"))
					{
						row.put("AMOUNT_PHALA",dataVar.getAmount());
						ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
					}
					else
						row.put("AMOUNT_PHALA",new BigDecimal(0)); 
		
		//todo tako je bilo if (((String)dataVar.getSubtypeData()).equals(new BigDecimal("8222")) || ((String)dataVar.getSubtypeData()).equals(new BigDecimal("9222")))
					if (dataVar.getSubtypeData().toString().equals("8222") || dataVar.getSubtypeData().toString().equals("9222"))
					{
						row.put("AMOUNT_URED",dataVar.getAmount());
						ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
					}
					else	
						row.put("AMOUNT_URED",new BigDecimal(0));
		
			}
			
			if (dataVar.getCol_type_id().equals(new BigDecimal("10777")))
			{
				if (dataVar.getSubtypeData().toString().equals("10222") || dataVar.getSubtypeData().toString().equals("11222"))
				{
					row.put("AMOUNT_HOTEL",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_HOTEL",new BigDecimal(0));//15
		
		
				if (dataVar.getSubtypeData().toString().equals("12222"))
				{
					row.put("AMOUNT_PRIV",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_PRIV",new BigDecimal(0)); 
			}
		
		
			if (dataVar.getCol_type_id().equals(new BigDecimal("12777")))
				
                if (dataVar.getSubtypeData().toString().equals("6222"))
				{
					row.put("AMOUNT_SK_PR",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_SK_PR",new BigDecimal(0));	

		}
		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("624223")))
		{
			if (dataVar.getCol_type_id().equals(new BigDecimal("14777")))
			{
				if (dataVar.getSubtypeData().toString().equals("1333"))
				{
					row.put("AMOUNT_AUTO",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}	
				else
					row.put("AMOUNT_AUTO",new BigDecimal(0));	
		
		
		
				if (dataVar.getSubtypeData().toString().equals("2333") || dataVar.getSubtypeData().toString().equals("5333"))
				{
					row.put("AMOUNT_MOTO",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_MOTO",new BigDecimal(0));
		
		
		
				if (dataVar.getSubtypeData().toString().equals("3333"))
				{
					row.put("AMOUNT_KAM",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_KAM",new BigDecimal(0));
		
		
		
				if (dataVar.getSubtypeData().toString().equals("4333"))
				{
					row.put("AMOUNT_GRV",dataVar.getAmount());
					ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
				}
				else
					row.put("AMOUNT_GRV",new BigDecimal(0));
		
			}
		}
		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("620223")))
		{
			row.put("AMOUNT_PLOV",dataVar.getAmount());
			ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
		}	
		else
			row.put("AMOUNT_PLOV",new BigDecimal(0));
		
		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("621223")))
		{
			if (dataVar.getCol_type_id().equals(new BigDecimal("60777")))
			{
				row.put("AMOUNT_ZRAK",dataVar.getAmount());
				ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
			}
			else
				row.put("AMOUNT_ZRAK",new BigDecimal(0));
		
		
			if (dataVar.getCol_type_id().equals(new BigDecimal("61777")))
			{
				row.put("AMOUNT_STRO",dataVar.getAmount());
                // todo kod svih sam promijenio ovo
				//ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(dataVar.getAmount()));
                // u ovo
                ukupnaPokrivenost = ukupnaPokrivenost.add(dataVar.getAmount());
			}
			else
				row.put("AMOUNT_STRO",new BigDecimal(0));	
		
		}
		
		
 
	} 
	
	
	
	/**
	 * Ureduje podatke za izvjesce koji su vezani za depozit
	 * */
	public void organizeRowCashDepData() {
		
		if (dataVar.getCol_cat_id().equals(new BigDecimal("612223")))
			if (dataVar.getCol_type_id().equals("1777") || dataVar.getCol_type_id().equals("2777") || dataVar.getCol_type_id().equals("3777"))
			{	if (dataVar.getValuta_depozita().equals(dataVar.getValuta_plasmana()))
					row.put("CASH_DEP_1",dataVar.getAmount());
				else
					row.put("CASH_DEP_2",dataVar.getAmount());
			}
			else
			{
				if (dataVar.getValuta_depozita().equals(dataVar.getValuta_plasmana()))
					row.put("CASH_DEP_3",dataVar.getAmount());
				else
					row.put("CASH_DEP_4",dataVar.getAmount());
			}	
		
	}
	
	
	/**
	 * Ureduje podatke za izvjesce koji su vezani za garancije
	 * @param eco_sec - String
	 * */
	public void organizeRowGuarantyData(String eco_sec) {
		if (dataVar.getCol_cat_id().equals(new BigDecimal("615223")))
		{
			if (eco_sec.equals("20"))
				row.put("GAR_1",dataVar.getAmount());
			else if (eco_sec.equals("21"))
				row.put("GAR_2",dataVar.getAmount());
			else if (eco_sec.equals("22"))
				row.put("GAR_3",dataVar.getAmount());
			else if (eco_sec.equals("11"))
				row.put("GAR_4",dataVar.getAmount());
			else if (eco_sec.equals("92"))
				row.put("GAR_5",dataVar.getAmount());
			else
				row.put("GAR_6",dataVar.getAmount());
		}
	}
	
	
	public void insertXMLLine(BO111.Iter iterator) throws SQLException{
		BigDecimal ukupnaPokrivenost = new BigDecimal(0);
    	//System.out.println("!!!!!!!!!insertXMLLine:!!!!!!!!!!");
        if(iterator.next()){
    		HashMap redak = new HashMap();
    		
    		//stavlja se u redak podaci iz iteratora dohvaecni iz CLT _PODLOGA1 tablice
    		
    		
            redak.put("c1",iterator.register_no());//register_no iz tablice CUSTOMER
            redak.put("c2",iterator.name());//name iz tablice CUSTOMER
            redak.put("c3",iterator.module_code());//module_code iz tablic CUSACC_EXPOSURE
            redak.put("c4",iterator.dom_oj()); //code iz tablice ORGANIZATION_UNIT
            redak.put("c5",iterator.cus_acc_no());
            redak.put("c6",iterator.eco_sec());
            redak.put("c7",iterator.b2asset_class());
            redak.put("c8",iterator.customer_indic());
            redak.put("c9",iterator.due_date());
            redak.put("c10",iterator.approval_date());
            redak.put("c11",iterator.currency_type());
            redak.put("c12",iterator.code_char());
            redak.put("c13",iterator.exposure_balance());
            redak.put("c14",iterator.exp_bal_bil());
            redak.put("c15",iterator.exp_bal_outbil());
    		
            redak.put("a1",iterator.cash());
            if(iterator.cash()!=null)
                ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.cash());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
            
            redak.put("a2",iterator.pol());
            if(iterator.pol()!= null)
                ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.pol());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		//ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.pol());
    		
            redak.put("a3",iterator.gar_s());
            if(iterator.gar_s()!=null)
                ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.gar_s());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a4",iterator.gar_b());
    		if(iterator.gar_b()!= null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.gar_b());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a5",iterator.nek());
            if(iterator.nek()!= null)
                ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.nek());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
            
            redak.put("a6",iterator.gzem());
    		if(iterator.gzem()!= null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.gzem());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
            
            redak.put("a7",iterator.pzem());
    		if(iterator.pzem()!= null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.pzem());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a8",iterator.phala());
            if(iterator.phala()!=null)
                ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.phala());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
            
            redak.put("a9",iterator.ured());
    		if(iterator.ured()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.ured());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
            
            redak.put("a10",iterator.hotel());
    		if(iterator.hotel()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.hotel());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a11",iterator.priv());
    		if(iterator.priv()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.priv());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a12",iterator.sk_pr());
    		if(iterator.sk_pr()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.sk_pr());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a13",iterator.auto());
    		if(iterator.auto()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.auto());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a14",iterator.moto());
    		if(iterator.moto()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.moto());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a15",iterator.kam());
    		if(iterator.kam()!= null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.kam());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a16",iterator.grv());
    		if(iterator.grv()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.grv());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a17",iterator.plov());
    		if(iterator.plov()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.plov());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
    		
            redak.put("a18",iterator.zrak());
    		if(iterator.zrak()!= null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.zrak());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
            
            redak.put("a19",iterator.stro());
    		if(iterator.stro()!=null)
    		    ukupnaPokrivenost = ukupnaPokrivenost.add(iterator.stro());
            else
                ukupnaPokrivenost = ukupnaPokrivenost.add(new BigDecimal(0));
            
            redak.put("osigurano",ukupnaPokrivenost);
            redak.put("neosigurano",(iterator.exposure_balance()).subtract(ukupnaPokrivenost));
            redak.put("d1",iterator.cash_dep_1());
            redak.put("d2",iterator.cash_dep_2());
            redak.put("d3",iterator.cash_dep_3());
            redak.put("d4",iterator.cash_dep_4());
            redak.put("g1",iterator.gar_1());
            redak.put("g2",iterator.gar_2());
            redak.put("g3",iterator.gar_3());
            redak.put("g4",iterator.gar_4());
            redak.put("g5",iterator.gar_5());
            redak.put("g6",iterator.gar_6());
    
    		xml_data.add(redak);
        }
		  
	}
	
	
}
