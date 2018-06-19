/*
 * Created on 2007.08.29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.vestigo.modules.collateral.batch.bo14;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.coreapp.common.util.CSVGenerator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author hratar
 *
 * TODO To change the template for this generated type comment go to
 * Window
 *  - Preferences - Java - Code Style - Code Templates
 */
public class BO140 extends Batch {

	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo14/BO140.java,v 1.5 2014/05/28 13:33:30 hraaks Exp $";

	BatchContext bc = null;
	
	private String [] ulazniParametri = null;
	
	private BO14Data dataVar = new BO14Data();
	
	private String configFile = null;
	
	private String outputFileName;
	
	private String outputFile;
	
	private static final String encoding = "Cp1250";
	
	private BO141 bo141 = null;

	private Vector xml_data = null;
	
	HashMap row; 
	
	public String executeBatch(BatchContext bc) throws Exception{
		
		this.bc = bc;
		
		this.bo141 = new BO141(bc);
		
		HashMap parameters = new HashMap();
		
		//da li je dobar broj parametra dobiven iz aplikacije, odnosno s komandne linije
		if (checkArgs() == false)
			return RemoteConstants.RET_CODE_ERROR;
		
		bc.beginTransaction();
//		logiranje poziva izvrsavanja batcha u tablicu Event
		try {
			bc.updateEveID(bo141.insertIntoEvent());
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
				parameters=headerData();
				
				CSVGenerator.printCSV("csv115",bc.getLogin(),xml_data,parameters,configFile,outputFile,encoding);
				
				YXY70.send(bc,"csv115", bc.getLogin() , outputFile);
				
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
		BatchParameters bp = new BatchParameters(new BigDecimal("1760206003"));
		bp.setArgs(args);
		new BO140().run(bp);
	}
	
	private boolean checkArgs() {
		int i;
		for (i = 0; i < bc.getArgs().length; i++)
			bc.debug("bc_Arg[" + String.valueOf(i) + "] = " + bc.getArg(i));
		
		if (bc.getArgs().length != 4 && bc.getArgs().length != 1)
		{	
			bc.debug("Neispravan broj argumenata.");
			return false;
		}
		
		//ako dobiva parametre s komanden linije,... inace (ako iz aplikacije)
		if (bc.getArgs().length == 1) {
			
			ulazniParametri[0] = "615223";
			ulazniParametri[1] = "30777";
			ulazniParametri[2] = "";
	
			bc.setBankSign(bc.getArg(0));
		}else {
			ulazniParametri = new String[4];
			for (i=0; i < bc.getArgs().length; i++) {
				ulazniParametri[i] = bc.getArg(i).trim();
				bc.debug("SQL_param[" + String.valueOf(i) + "] = " + ulazniParametri[i]);
			}
			bc.setBankSign(ulazniParametri[3]);
		
		}
		
		return true;
	
	}
	
	private void params_to_data(String [] ulazniParametri) {
	
		String [] parametri = null;
		
		int i = 0;
		
		parametri = ulazniParametri;
	
		
		dataVar.setCollCategoryCode(parametri[0]);
		
		dataVar.setCollTypeCode(parametri[1]);

		dataVar.setGroupOfIssuers(parametri[2]);
		
	}
	
	private void setConfig() {
		String Path = System.getProperty("user.dir") + "/" + this.getClass().getPackage().getName().replace('.','/');
		
		this.configFile = Path + "/coll_review.xml";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		
		String dateString = new String(dateFormat.format(bc.getExecStartTime()));
		
		this.outputFileName = "Collateral_review".concat(dateString).concat(".csv");
		
		this.outputFile = bc.getOutDir() + "/" + outputFileName;
		
		bc.debug("BO140, metoda setConfig() Variable configFile" + configFile);
		bc.debug("BO140, metoda setConfig() Variable path" + Path);
		bc.debug("BO140, metoda setConfig() Variable outputDir" + outputFile);
			
	}
	
	
	private void createCSV() throws SQLException, Exception {
		//u ovu varijablu se dohvacaju podaci iz JDBC-a
		ResultSet resultSet = null;
		
		row = new HashMap();
		
		BO141.Iter iterator = null;
	
		try{
			/**
			 odabir svih polica i pripadajuæih im kolterala koje ulaze u izvješæe
			*/
			iterator = bo141.fetchData(dataVar);
			
			while(iterator.next()){
				HashMap row = new HashMap();
				
				//stavlja se u redak podaci iz iteratora dohvaecni iz CLT _PODLOGA1 tablice
				
				
				row.put("h1",iterator.col_hea_id());//register_no iz tablice CUSTOMER
				row.put("h2",trzisnaVrijednostValuta(iterator.trzisna_vrijednost_valuta(),bo141.teretiDrugih(iterator.col_hea_id()),iterator.midd_rate(),BigDecimal.ROUND_HALF_UP));//name iz tablice CUSTOMER
				row.put("h3",iterator.code_char1());//module_code iz tablic CUSACC_EXPOSURE
				row.put("h4",trzisnaVrijednostKuna(iterator.trzisna_vrijednost_valuta(),bo141.teretiDrugih(iterator.col_hea_id()),iterator.midd_rate())); //code iz tablice ORGANIZATION_UNIT
				row.put("h5",iterator.reva_date());
				row.put("h6",iterator.guar_issuer());
				row.put("h7",iterator.guar_iss_regno());
				row.put("h8",iterator.eco_sec());
				row.put("h9",iterator.cou_iso_code());
				row.put("h10",iterator.guar_datn_unti());
				row.put("h11",iterator.register_no());
				row.put("h12",iterator.name());
				row.put("h13",iterator.cus_acc_no());
				row.put("h14",iterator.exposure_balance());
				row.put("h15",iterator.code_char2());
				row.put("h16",iterator.exp_balance_hrk());
			
				
				xml_data.add(row);
				
			}
		}catch(SQLException sqle){
			throw sqle;
		}catch(Exception e){
			throw e;
		}
	}
	
	
	/**
	 * 
	 * @param trzisna_vrijednost_valuta
	 * @param tereti_drugih - tereti drugih u kunama
	 * @param midd_rate - za valutu u kojoj je izrazena trzisna_vrijednosta_valuta
	 * @param rounding_mode 
	 * @return
	 */
	private BigDecimal trzisnaVrijednostValuta(BigDecimal trzisna_vrijednost_valuta,BigDecimal tereti_drugih, BigDecimal midd_rate, int rounding_mode) {
		return trzisna_vrijednost_valuta.subtract(tereti_drugih.divide(midd_rate,rounding_mode));
	
	}
	
	/**
	 * 
	 * @param trzisna_vrijednost_valuta
	 * @param tereti_drugih - tereti drugih u kunama
	 * @param midd_rate - za valutu u kojoj je izrazena trzisna_vrijednosta_valuta
	 * @return
	 */
	private BigDecimal trzisnaVrijednostKuna(BigDecimal trzisna_vrijednost_valuta,BigDecimal tereti_drugih, BigDecimal midd_rate) {
		return (trzisna_vrijednost_valuta.multiply(midd_rate)).subtract(tereti_drugih);
	
	}
	
	
	private HashMap headerData() {
		HashMap row=new HashMap();
		//row.put("datum",getDDMMYYYY(reviewDate));
    	//row.put("naslov","Izvjesce s neponderiranim vrijednostima.");
    	
    	return row;
	}
	
}
