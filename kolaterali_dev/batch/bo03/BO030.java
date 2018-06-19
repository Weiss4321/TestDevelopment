package hr.vestigo.modules.collateral.batch.bo03;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo03.BO031.IteratorDefaultData;
import hr.vestigo.modules.collateral.batch.bo03.BO031.IteratorPlacement;
import hr.vestigo.modules.collateral.batch.bo03.BO031.IteratorPlacementDefault;
import hr.vestigo.modules.collateral.batch.bo03.BO031.IteratorPlacementDuplicates;
import hr.vestigo.modules.collateral.batch.bo03.BO031.IteratorPlacementWithData;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.coreapp.common.yxyB.YXYB0;
import hr.vestigo.modules.coreapp.common.yxyD.YXYD0;
import hr.vestigo.modules.rba.util.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


/**
 * Batch za prihvat plasmana iz DWH-a.
 * Obrada prebacuje podatake iz tablice CUSACC_EXP_DWH u tablice CUSACC_EXPOSURE i CUSACC_EXPOSURE_NEW.
 */
public class BO030 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo03/BO030.java,v 1.39 2017/12/06 14:51:11 hrakis Exp $";

	private final String rpt_code = "csv173";
	private final String rpt_code2 = "csvbo03";

	private BatchContext bc;
	private BO031 bo031;

	private String returnCode;
	private Date exposureDate;
	private boolean isTestMode;


	public String executeBatch(BatchContext batchContext) throws Exception
	{
		// inicijalizacija potrebnih varijabli
	    bc = batchContext;
		bo031 = new BO031(bc);
		returnCode = RemoteConstants.RET_CODE_SUCCESSFUL;
		exposureDate = null;
		isTestMode = false;
		
        // evidentiranje eventa
        insertIntoEvent();

        // dohvat i provjera parametara predanih obradi
        if (!loadBatchParameters()) return RemoteConstants.RET_CODE_ERROR;

        // provjera preduvjeta za pokretanje obrade
		if (!checkPreconditions()) return returnCode;
		info("Datum za koji se preuzimaju podaci: " + exposureDate);

		// dohvati ukupni broj isporuèenih plasmana za datum valute analitike
		int count_dva = bo031.selectNumberOfInputRecords("DVA");
		info("Broj isporucenih zapisa za datum valute analitike: " + count_dva);

		if (count_dva > 0)
		{
		    bc.info("Preuzimaju se podaci za datum valute analitike.");
		    
		    // eliminiranje duplikata
		    // int duplicates_count = eliminateDuplicates("DVA");
		    // info("Broj duplih zapisa: " + duplicates_count);

            // dohvati plasmane s izloženošæu 0
		    HashMap<BigDecimal, BO03Data> placementsWithZeroExposureOld = loadPlacementsWithZeroExposure();
		    info("Broj aktivnih plasmana s izlozenoscu 0 (prije preuzimanja podataka): " + placementsWithZeroExposureOld.size());

            // dohvati plasmane koji su ušli u status WU
		    HashMap<BigDecimal, BO03Data> placementsEnteredWUStatus = loadPlacementsEnteredWuStatus();
		    info("Broj plasmana koji su usli u status WU: " + placementsEnteredWUStatus.size());

            // dohvati plasmane koji su prešli iz statusa W u status A
            HashMap<BigDecimal, BO03Data> placementsExitedWStatus = loadPlacementsExitedWStatus();
            info("Broj plasmana koji su izasli iz statusa W: " + placementsExitedWStatus.size());

            // dohvati evidenciju promjene default statusa po partiji plasmana
            HashMap<BigDecimal, ArrayList<DefaultData>> defaultData = loadDefaultData();
            info("Broj razlicitih plasmana u evidenciji promjene statusa defaulta: " + defaultData.size());

            // *** krediti
            
            // èišæenje temp tablice
            bo031.truncateCusaccExpDwh_temp();  
            
            // punjenje temp tablice 
            bo031.insertIntoTableCusaccExpDwh_temp("DVA", true);
            
    		// prebacivanje podataka u CUSACC_EXPOSURE
    		bo031.mergeDwhTableIntoCusaccExposure(true);
    		
    		// *** ostali
    		
            // èišæenje temp tablice
            bo031.truncateCusaccExpDwh_temp();  
            
            // punjenje temp tablice 
            bo031.insertIntoTableCusaccExpDwh_temp("DVA", false);
            
            // prebacivanje podataka u CUSACC_EXPOSURE
            bo031.mergeDwhTableIntoCusaccExposure(false);
            
            // ***
    		
            // punjenje/pražnjenje polja "Kolateral na prodaju" i "Datum stavljanja kolaterala na prodaju"
    		updateForSaleData(placementsEnteredWUStatus, placementsExitedWStatus);
            
            // ažuriranje evidencije defaulta
            updateDefaultData(defaultData);
    		
    	    // dohvati plasmane s izloženošæu 0
            HashMap<BigDecimal, BO03Data> placementsWithZeroExposureNew = loadPlacementsWithZeroExposure();
            info("Broj aktivnih plasmana s izlozenoscu 0 (poslije preuzimanja podataka): " + placementsWithZeroExposureNew.size());
            
            // provjera postotka broja plasmana s izloženošæu 0
            if (placementsWithZeroExposureOld.size() > 0)
            {
                float changePercentage = (placementsWithZeroExposureNew.size() - placementsWithZeroExposureOld.size()) / (float)placementsWithZeroExposureOld.size() * 100f;
                info("Promjena broja plasmana koji imaju izlozenost 0: " + changePercentage + "%");
        		
        		// dohvat postotka dozvoljene promjene broja plasmana koji imaju izloženost 0
                float allowedChangePercentage = bo031.selectAllowedZeroExposureChangePercentage();
                info("Postotak dozvoljene promjene broja plasmana koji imaju izlozenost 0: " + allowedChangePercentage + "%");
                
                if (changePercentage > allowedChangePercentage)
                {
                    info("Promjena broja plasmana je veca od dozvoljene - salje se obavijest.");
                    createAndSendReport(placementsWithZeroExposureOld, placementsWithZeroExposureNew, allowedChangePercentage);
                }
            }
		}
		
	    // dohvati ukupni broj isporuèenih plasmana za datum glavne knjige
        int count_dgk = bo031.selectNumberOfInputRecords("DGK");
        info("Broj isporucenih zapisa za datum glavne knjige: " + count_dgk);
		
        if (count_dgk > 0)
        {            
            bc.info("Preuzimaju se podaci za datum glavne knjige.");
            
            // eliminiranje duplikata
            // int duplicates_count = eliminateDuplicates("DGK");
            // info("Broj duplih zapisa: " + duplicates_count);
            
            // *** krediti
            
            // èišæenje temp tablice
            bo031.truncateCusaccExpDwh_temp();
            
            // punjenje temp tablice 
            bo031.insertIntoTableCusaccExpDwh_temp("DGK", true);
            
            // prebacivanje podataka u CUSACC_EXPOSURE_NEW
            bo031.mergeDwhTableIntoCusaccExposureNew(true);
            
            // *** ostali
            
            // èišæenje temp tablice
            bo031.truncateCusaccExpDwh_temp();
            
            // punjenje temp tablice 
            bo031.insertIntoTableCusaccExpDwh_temp("DGK", false);
            
            // prebacivanje podataka u CUSACC_EXPOSURE_NEW
            bo031.mergeDwhTableIntoCusaccExposureNew(false);
        }
        
        // provjera da li je uopæe bilo podataka
        if (count_dva == 0 && count_dgk == 0)
        {
            error("Nije dohvacen ni jedan zapis iz tablice cusacc_exp_dwh za obradu bo03.", null);
            return RemoteConstants.RET_CODE_ERROR;
        }
		
		// brisanje podataka iz DWH tablice
		if (!isTestMode) bo031.truncateCusaccExpDwh();
		
        // èišæenje temp tablice kada obrada završi
        bo031.truncateCusaccExpDwh_temp();
		
        // signaliziranje kraja obrade
		bo031.updateDwhStatus(exposureDate, "0", "bo03");
		bc.setCounter(count_dva + count_dgk);
			
		return returnCode;
	}
	
	
	/**
	 * Metoda provjerava da li su zadovoljeni preduvjeti vezani za isporuku podataka iz DWH.
	 * @return true ako su preduvjeti zadovoljeni, false ako nisu
	 */
	private boolean checkPreconditions() throws Exception
	{
        Vector vector = bo031.selectInputDataStatusAndDate("bo03");
        if (vector == null)
        {
            error("Nije dohvacen ni jedan zapis iz tablice dwh_status za obradu bo03.", null);
            returnCode = RemoteConstants.RET_CODE_ERROR;
            return false;
        }
        
        String exposureStatus = (String)vector.get(0);      
        this.exposureDate = (Date)vector.get(1);
             
        if (!exposureStatus.equals("1"))
        {
            String msg = "U kolateral modulu zadnji podaci o plasmanima i izlo\u017Eenosti preneseni su iz DWH za datum " + DateUtils.getDDMMYYYY(exposureDate) + ".\n";
            YXY70.send(bc, rpt_code, bc.getLogin(), new Vector(), msg);
            warning("Nema novih podataka za obraditi. Zadnji podaci su za datum=" + exposureDate + ".\n");
            returnCode = RemoteConstants.RET_CODE_WARNING;  // TODO: kad se prebaci na master batch, ovdje bi trebao biti RET_CODE_ERROR
            return false;
        }
        
        return true;
	}
	
	
	/**
	 * Metoda koja eliminira duplikate iz DWH tablice.
	 * @return broj duplih zapisa
	 */
	/*private int eliminateDuplicates(String exp_type_ind) throws Exception
	{
	    IteratorPlacementDuplicates iter = bo031.selectDuplicates(exp_type_ind);
	    HashMap<String, DuplicateData> map = new HashMap<String, DuplicateData>();
	    
	    int counter = 0;
	    while (iter.next())
	    {
	        if ("191".equals(iter.contract_cur()) || !map.containsKey(iter.cus_acc_no()))
	        {
	            DuplicateData data = new DuplicateData();
	            data.cus_acc_no = iter.cus_acc_no();
	            data.contract_cur = iter.contract_cur();
	            data.status = iter.status();
	            data.create_ts = iter.create_ts();
	            map.put(data.cus_acc_no, data);
	        }
	        counter++;
	    }
	    
	    bc.beginTransaction();
	    for (DuplicateData data : map.values())
	    {
	        bo031.updateDuplicates(data, exp_type_ind);
	    }
	    bc.commitTransaction();
	    
	    return counter - map.size();
	}*/
	
	
	/**
     * Metoda dohvaæa podatke o aktivnim plasmanima koji imaju izloženost 0.
     * @return HashMap
     */
    private HashMap<BigDecimal, BO03Data> loadPlacementsWithZeroExposure() throws Exception
    {
        bc.startStopWatch("BO030.loadPlacementsWithZeroExposure");
        
        HashMap<BigDecimal, BO03Data> map = new HashMap<BigDecimal, BO03Data>();
        
        IteratorPlacementWithData iter = bo031.selectPlacementsWithZeroExposure();
        while (iter.next())
        {
            BO03Data data = new BO03Data();
            data.cus_acc_id = iter.cus_acc_id(); 
            data.placement_owner_code = iter.code(); 
            data.placement_owner_name = iter.name(); 
            data.module_code = iter.module_code(); 
            data.cus_acc_no = iter.cus_acc_no(); 
            data.contract_no = iter.contract_no(); 
            data.cus_acc_orig_st = iter.cus_acc_orig_st(); 
            data.exposure_code_char = iter.code_char(); 
            data.exposure_balance = iter.exposure_balance(); 
            data.exposure_date = iter.exposure_date();
            map.put(data.cus_acc_id, data);
        }
        iter.close();
        
        bc.stopStopWatch("BO030.loadPlacementsWithZeroExposure");
        
        return map;
    }
	
	
	/**
	 * Metoda dohvaæa podatke o plasmanima koji su ušli u status WU.
	 * @return HashMap
	 */
	private HashMap<BigDecimal, BO03Data> loadPlacementsEnteredWuStatus() throws Exception
	{
	    bc.startStopWatch("BO030.loadPlacementsEnteredWuStatus");
	    
	    HashMap<BigDecimal, BO03Data> map = new HashMap<BigDecimal, BO03Data>();
	    
	    IteratorPlacement iter = bo031.selectPlacementsEnteredWuStatus();
	    while (iter.next())
	    {
	        BO03Data data = new BO03Data();
	        data.cus_acc_id = iter.cus_acc_id();
	        data.cus_acc_no = iter.cus_acc_no();
	        data.exposure_date = iter.exposure_date();
	        map.put(data.cus_acc_id, data);
	    }
	    iter.close();
	    
	    bc.stopStopWatch("BO030.loadPlacementsEnteredWuStatus");
	    
	    return map;
	}
	
	
	/**
     * Metoda dohvaæa podatke o plasmanima koji su izašli iz statusa W i ušli u status A.
     * @return HashMap
     */
    private HashMap<BigDecimal, BO03Data> loadPlacementsExitedWStatus() throws Exception
    {
        bc.startStopWatch("BO030.loadPlacementsExitedWStatus");
        
        HashMap<BigDecimal, BO03Data> map = new HashMap<BigDecimal, BO03Data>();
        
        IteratorPlacement iter = bo031.selectPlacementsExitedWStatus();
        while (iter.next())
        {
            BO03Data data = new BO03Data();
            data.cus_acc_id = iter.cus_acc_id();
            data.cus_acc_no = iter.cus_acc_no();
            data.exposure_date = iter.exposure_date();
            map.put(data.cus_acc_id, data);
        }
        iter.close();
        
        bc.stopStopWatch("BO030.loadPlacementsExitedWStatus");
        
        return map;
    }
    
    
    /**
     * Metoda dohvaæa evidenciju promjene default statusa po partiji plasmana.
     */
    private HashMap<BigDecimal, ArrayList<DefaultData>> loadDefaultData() throws Exception
    {
        bc.startStopWatch("BO030.loadDefaultData");
        
        HashMap<BigDecimal, ArrayList<DefaultData>> map = new HashMap<BigDecimal, ArrayList<DefaultData>>();
        
        IteratorDefaultData iter = bo031.selectDefaultData();
        while (iter.next())
        {
            DefaultData data = new DefaultData();
            data.cus_acc_exp_def_id = iter.cus_acc_exp_def_id();
            data.cus_acc_id = iter.cus_acc_id();
            data.date_from = iter.date_from();
            data.date_until = iter.date_until();
            
            ArrayList<DefaultData> list = map.get(data.cus_acc_id);
            if (list == null)
            {
                list = new ArrayList<DefaultData>();
                map.put(data.cus_acc_id, list);
            }
            list.add(data);
        }
        iter.close();
        
        bc.stopStopWatch("BO030.loadDefaultData");
        
        return map;
    }
    
    
    /**
     * Metoda puni/prazni polja "Kolateral na prodaju" i "Datum stavljanja kolaterala na prodaju"
     * @param placementsEnteredWUStatus plasmani koji su ušli u status WU
     * @param placementsExitedWStatus plasmani koji su izašli iz statusa W i ušli u status A
     * @throws Exception
     */
    private void updateForSaleData(HashMap<BigDecimal, BO03Data> placementsEnteredWUStatus, HashMap<BigDecimal, BO03Data> placementsExitedWStatus) throws Exception
    {
        bc.beginTransaction();
        
        for (BO03Data bo03Data : placementsEnteredWUStatus.values()) bo031.fillForSaleData(bo03Data);
        
        for (BO03Data bo03Data : placementsExitedWStatus.values()) bo031.clearForSaleData(bo03Data);
        
        bc.commitTransaction();
    }

    
    /**
     * Metoda ažurira evidenciju promjene default statusa po partiji plasmana.
     * @param defaultData evidencija
     */    
    private void updateDefaultData(HashMap<BigDecimal, ArrayList<DefaultData>> defaultData) throws Exception
    {
        bc.startStopWatch("BO030.updateDefaultData");
        
        bc.beginTransaction();
        
        int counterInsert = 0;
        int counterUpdate = 0;
        
        IteratorPlacementDefault iter = bo031.selectPlacementsWithDefaultData();
        while (iter.next())
        {
            if ("Y".equalsIgnoreCase(iter.default_status()))
            {
                if (DateUtils.whoIsOlder(iter.default_date(), iter.exposure_date()) == 1) continue;  // ne smije biti default_date > exposure_date
                
                // ako ne postoji za trenutni plasman zapis u evidenciji defaulta kojem je exposure_date unutar [date_from, date_until], insertiraj novi zapis u evidenciju
                boolean exists = false;
                ArrayList<DefaultData> list = defaultData.get(iter.cus_acc_id());
                if (list != null)
                {
                    for (DefaultData data : list)
                    {
                        if (DateUtils.whoIsOlder(iter.exposure_date(), data.date_from) != -1 && DateUtils.whoIsOlder(iter.exposure_date(), data.date_until) != 1)
                        {
                            exists = true;
                            break;
                        }
                    }
                }
                
                if (!exists)
                {
                    bo031.insertCusaccExpDef(iter.cus_id(), iter.cus_acc_id(), iter.default_status(), iter.default_date(), DateUtils.createDateFromString("31.12.9999"));
                    counterInsert++;
                }
            }
            else if ("N".equalsIgnoreCase(iter.default_status()))
            {
                // naði sve zapise za trenutni plasman u evidenciji defaulta kojima je exposure_date unutar [date_from, date_until] i ažuriraj im date_until na exposure_date - 1 day
                ArrayList<DefaultData> list = defaultData.get(iter.cus_acc_id());
                if (list != null)
                {
                    for (DefaultData data : list)
                    {
                        if (DateUtils.whoIsOlder(iter.exposure_date(), data.date_from) != -1 && DateUtils.whoIsOlder(iter.exposure_date(), data.date_until) != 1)
                        {
                            bo031.updateCusaccExpDef(data.cus_acc_exp_def_id, DateUtils.addOrDeductDaysFromDate(iter.exposure_date(), 1, false));
                            counterUpdate++;
                        }
                    }
                }
            }
        }
        iter.close();
        
        bc.commitTransaction();
        
        info("Broj ubacenih zapisa u evidenciji promjene statusa defaulta po partiji plasmana: " + counterInsert);
        info("Broj azuriranih zapisa u evidenciji promjene statusa defaulta po partiji plasmana: " + counterUpdate);
        
        bc.stopStopWatch("BO030.updateDefaultData");
    }
    
    
    /**
     * Metoda stvara i šalje na mail izvještaj s plasmanima koji imaju izloženost 0, a nisu imali prije.
     * @param placementsWithZeroExposureOld kolekcija plasmana koji su prije imali izloženost 0
     * @param placementsWithZeroExposureNew kolekcija plasmana koji imaju izloženost 0
     * @param allowedPercentage dozvoljeni postotak promjene broja plasmana koji imaju izloženost 0
     */
    private void createAndSendReport(HashMap<BigDecimal, BO03Data> placementsWithZeroExposureOld, HashMap<BigDecimal, BO03Data> placementsWithZeroExposureNew, float allowedPercentage) throws Exception
    {
        // stvaranje izlazne datoteke
        String dir = bc.getOutDir() + "/";
        String fileName = dir + "ExposureZeroControl_" + bc.getExecStartTime().getTime() + ".csv";             
        OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(new File(fileName)), "Cp1250");
        bc.debug("Stvorena datoteka.");

        // zapisivanje zaglavlja u izlaznu datoteku
        streamWriter.write(getHeaderRow());
        
        // zapiši sve plasmane koji imaju izloženost 0, a nisu imali prije 
        for (BigDecimal cus_acc_id : placementsWithZeroExposureNew.keySet())
        {
            if (!placementsWithZeroExposureOld.containsKey(cus_acc_id))
            {
                streamWriter.write(getDetailsRow(placementsWithZeroExposureNew.get(cus_acc_id)));                
            }
        }
        bc.debug("Zapisani podaci u datoteku.");

        // zatvaranje izlazne datoteke
        streamWriter.flush();
        streamWriter.close();

        // slanje maila
        String msg = "Od ukupno " + placementsWithZeroExposureNew.size() + " partija s izlo\u017Eeno\u0161\u0107u 0 na dana\u0161nji dan, " + placementsWithZeroExposureOld.size() + " ih je i prethodni dan imalo izlo\u017Eenost 0, \u0160to je vi\u0161e od dozvoljenih " + allowedPercentage + "%.\n";
        Vector attachment = new Vector();
        attachment.add(fileName);
        YXY70.send(bc, rpt_code2, bc.getLogin(),attachment, msg);
        info("Poslan mail s izvjescem o plasmanima koji imaju izlozenost 0.");
    }

	
	/**
     * Metoda formira jedan red izvješæa i vraæa ga u obliku stringa.
     * @param data Objekt s podacima
     * @return formirani red
     */
    private String getDetailsRow(BO03Data data) throws SQLException
    {
        StringBuffer buffer = new StringBuffer();       
        buffer.append(norm(data.placement_owner_code)).append(";");  
        buffer.append(norm(data.placement_owner_name)).append(";");
        buffer.append(norm(data.module_code)).append(";");
        buffer.append(norm(data.cus_acc_no)).append(";");
        buffer.append(norm(data.contract_no)).append(";");
        buffer.append(norm(data.cus_acc_orig_st)).append(";");
        buffer.append(norm(data.exposure_code_char)).append(";");
        buffer.append(norm(data.exposure_balance)).append(";");
        buffer.append(norm(data.exposure_date)).append(";");
        return buffer.append("\n").toString();
    }
    
    /**
     * Metoda formira zaglavlje za izvješæe i vraæa ga u obliku stringa.
     * @return formirano zaglavlje
     */
    private String getHeaderRow()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\u0160ifra komitenta").append(";");
        buffer.append("Naziv komitenta").append(";");
        buffer.append("Naziv modula").append(";");
        buffer.append("Partija ra\u010Duna").append(";");
        buffer.append("Broj ugovora").append(";");
        buffer.append("DWH status").append(";");
        buffer.append("\u0160ifra valute").append(";");
        buffer.append("Izlo\u017Eenost").append(";");
        buffer.append("Datum stanja izlo\u017Eenosti").append(";");
        return buffer.append("\n").toString();
    }
	

    /**
     * Metoda evidentira dogaðaj izvoðenja obrade u tablicu EVENT.
     * @return EVE_ID novostvorenog zapisa u tablici EVENT.
     */
    private BigDecimal insertIntoEvent() throws Exception
    {
        try
        {
            bc.startStopWatch("BO030.insertIntoEvent");
            bc.beginTransaction();

            BigDecimal eve_id = new YXYD0(bc).getNewId();
            bc.debug("EVE_ID = " + eve_id);

            HashMap event = new HashMap();
            event.put("eve_id", eve_id);
            event.put("eve_typ_id", new BigDecimal("1676496003"));
            event.put("event_date", new Date(System.currentTimeMillis()));
            event.put("cmnt", "Batch za prihvat plasmana iz DWH-a");
            event.put("use_id", bc.getUserID());
            event.put("bank_sign", bc.getBankSign());

            new YXYB0(bc).insertEvent(event);
            bc.updateEveID(eve_id);

            bc.commitTransaction();
            bc.debug("Evidentirano izvodjenje obrade u tablicu EVENT.");
            bc.stopStopWatch("BO030.insertIntoEvent");
            return eve_id;
        }
        catch (Exception ex)
        {
            error("Dogodila se nepredvidjena greska pri evidentiranju pocetka izvodjenja obrade!", ex);
            throw ex;
        }
    }	


    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.<br/>
     * <dl>
     *    <dt>bank_sign</dt><dd>Oznaka banke. Obvezno predati RB.</dd>
     * </dl>
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean loadBatchParameters() throws Exception
    {
        try
        {
            String[] parameterNames = { "Oznaka banke" };

            // ispiši parametre predane obradi
            info("Parametri predani obradi:");
            for (int i = 0; i < bc.getArgs().length; i++)
            {
                String parameterName = (i < bc.getArgs().length) ? parameterNames[i] : "Nepoznati parametar";
                String parameterValue = bc.getArg(i);
                info("   " + parameterName + " = " + parameterValue);
            }

            // provjeri da li je broj parametara ispravan
            if (bc.getArgs().length != parameterNames.length)
            {
                error("Neispravan broj parametara - predani broj parametara je " + bc.getArgs().length + ", a obrada prima " + parameterNames.length + "!", null);  
                return false;
            }
            
            // provjeri oznaku banke
            String bank_sign = bc.getArg(0);
            if (bank_sign.endsWith("#TEST"))
            {
               isTestMode = true;
               bank_sign = bank_sign.replaceAll("#TEST", "");
               info("**************** TESTNI MOD ****************");
            }
            if (!bank_sign.equals("RB"))
            {
                error("Oznaka banke mora biti RB!", null);
                return false;
            }
            bc.setBankSign("RB");
            
            return true;
        }
        catch (Exception ex)
        {
            error("Neispravno zadani parametri!", ex);
            return false;
        }
    }
    
    
    private String norm(Object value)
    {
        if (value == null) return "";
        else return value.toString().trim();
    }
   
    private void error(String message, Exception ex) throws Exception
    {
        if (ex != null) bc.error(message, ex); else bc.error(message, new String[]{});
        bc.userLog("GRESKA: " + message);
    }
    
    private void warning(String message) throws Exception
    {
        bc.warning(message);
        bc.userLog("UPOZORENJE: " + message);
    }    

    private void info(String message) throws Exception
    {
        bc.info(message);
        bc.userLog(message);
    }

    
	public static void main(String[] args)
	{
		BatchParameters batchParameters = new BatchParameters(new BigDecimal("1676484003"));
        batchParameters.setArgs(args);
        new BO030().run(batchParameters);
	}
}