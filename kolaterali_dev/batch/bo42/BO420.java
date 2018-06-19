package hr.vestigo.modules.collateral.batch.bo42;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo42.BO421.CollIterator;
import hr.vestigo.modules.coreapp.common.yxy7.YXY70;
import hr.vestigo.modules.rba.util.DateUtils;
import hr.vestigo.modules.rba.util.SendMail;
import hr.vestigo.modules.rba.util.excel.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;


/**
 * Izvješæe o prihvatljivim i neprihvatljivim kolateralima.
 * @author hrakis
 */
public class BO420 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo42/BO420.java,v 1.9 2009/11/25 08:26:31 hrakis Exp $";
	private BO421 bo421;
	private HashMap euroExchangeRate;
	private Date load_date;
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO420 pokrenut.");
		bo421 = new BO421(bc);
		
        // insertiranje eventa
		BigDecimal eve_id = bo421.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.");
		
		// dohvat teèajne liste
		euroExchangeRate = bo421.selectEuroExchangeRate();
		if(euroExchangeRate == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvacena tecajna lista.");
		
		// dohvat podataka o kolateralima
		CollIterator iter = bo421.selectCollaterals(load_date);
		if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvaceni podaci.");
		
		String dateString = new SimpleDateFormat("yyyyMMdd").format(load_date);
		String dir = bc.getOutDir() + "/";
		String fileName = dir + "Eligibility_" + dateString;

		// stvaranje workbooka
		Workbook workbook = new Workbook(fileName, Workbook.FileType.ExcelML, true, 100);
		workbook.addStyle(new Style("Default", Alignment.Bottom, Font.Arial10));
		workbook.addStyle(new Style("s1", Alignment.BottomCenter, Borders.All, Font.Arial10Bold, new Interior("#FFFF00"), null));
		workbook.addStyle(new Style("s2", null, null, null, null, "Fixed"));
		workbook.addStyle(new Style("s3", null, null, null, null, "0"));
		workbook.addStyle(new Style("s4", null, null, null, null, "Short Date"));
		workbook.open();
		bc.debug("Otvoren workbook.");
		
		// stvaranje sheetova
		Worksheet sheetDeposits = workbook.addWorksheet(new Worksheet("cash", dir + "cash_" + dateString));
		Worksheet sheetGuarantees = workbook.addWorksheet(new Worksheet("guarantees", dir + "guarantees_" + dateString));
		Worksheet sheetVRP = workbook.addWorksheet(new Worksheet("capital market instr", dir + "vrp_" + dateString));
		Worksheet sheetResidentialRealEstates = workbook.addWorksheet(new Worksheet("RRE", dir + "rre_" + dateString));
		Worksheet sheetCommercialRealEstates = workbook.addWorksheet(new Worksheet("CRE", dir + "cre_" + dateString));
		Worksheet sheetVehicles = workbook.addWorksheet(new Worksheet("vehicles", dir + "vehicles_" + dateString));
		Worksheet sheetLands = workbook.addWorksheet(new Worksheet("land", dir + "land_" + dateString));
		Worksheet sheetMovables = workbook.addWorksheet(new Worksheet("OPC", dir + "opc_" + dateString));
		Worksheet sheetInsurancePolicies = workbook.addWorksheet(new Worksheet("insurance policy", dir + "insPol_" + dateString));
		bc.debug("Stvoreni sheetovi.");

		// definiranje kolona
		sheetDeposits.addColumns(getColumns(new BigDecimal(612223), null));
		sheetGuarantees.addColumns(getColumns(new BigDecimal(615223), null));
		sheetVRP.addColumns(getColumns(new BigDecimal(613223), null));
		sheetResidentialRealEstates.addColumns(getColumns(new BigDecimal(618223), new BigDecimal(8777)));
		sheetCommercialRealEstates.addColumns(getColumns(new BigDecimal(618223), new BigDecimal(9777)));
		sheetVehicles.addColumns(getColumns(new BigDecimal(624223), null));
		sheetLands.addColumns(getColumns(new BigDecimal(618223), new BigDecimal(7777)));
		sheetMovables.addColumns(getColumns(new BigDecimal(621223), null));
		sheetInsurancePolicies.addColumns(getColumns(new BigDecimal(616223), null));
		bc.debug("Definirane kolone.");

		// otvaranje sheetova
		workbook.openAllWorksheets();
		bc.debug("Otvoreni sheetovi.");
		
		// formiranje zaglavlja na sheetovima
		sheetDeposits.addRow(getHeaderRow(new BigDecimal(612223), null));
		sheetGuarantees.addRow(getHeaderRow(new BigDecimal(615223), null));
		sheetVRP.addRow(getHeaderRow(new BigDecimal(613223), null));
		sheetResidentialRealEstates.addRow(getHeaderRow(new BigDecimal(618223), new BigDecimal(8777)));
		sheetCommercialRealEstates.addRow(getHeaderRow(new BigDecimal(618223), new BigDecimal(9777)));
		sheetVehicles.addRow(getHeaderRow(new BigDecimal(624223), null));
		sheetLands.addRow(getHeaderRow(new BigDecimal(618223), new BigDecimal(7777)));
		sheetMovables.addRow(getHeaderRow(new BigDecimal(621223), null));
		sheetInsurancePolicies.addRow(getHeaderRow(new BigDecimal(616223), null));
		bc.debug("Dodana zaglavlja.");
	
		// zapisivanje podataka u tablicu
		CollData coll = null;
		while(iter.next())
		{
			if(coll != null && !iter.col_hea_id().equals(coll.col_hea_id))
			{
				if(isDeposit(coll.col_cat_id)) sheetDeposits.addRow(getDetailsRow(coll));
				else if(isGuarantee(coll.col_cat_id)) sheetGuarantees.addRow(getDetailsRow(coll));
				else if(isVRP(coll.col_cat_id)) sheetVRP.addRow(getDetailsRow(coll));
				else if(isResidentialRealEstate(coll.col_cat_id, coll.col_type_id)) sheetResidentialRealEstates.addRow(getDetailsRow(coll));
				else if(isCommercialRealEstate(coll.col_cat_id, coll.col_type_id)) sheetCommercialRealEstates.addRow(getDetailsRow(coll));
				else if(isVehicle(coll.col_cat_id)) sheetVehicles.addRow(getDetailsRow(coll));
				else if(isLand(coll.col_cat_id, coll.col_type_id)) sheetLands.addRow(getDetailsRow(coll));
				else if(isMovable(coll.col_cat_id, coll.col_type_id)) sheetMovables.addRow(getDetailsRow(coll));
				else if(isInsurancePolicy(coll.col_cat_id)) sheetInsurancePolicies.addRow(getDetailsRow(coll));
				coll = null;
			}
            coll = getCollDataFromIterator(coll, iter);
		}
		
		// zapiši zadnji kolateral
		if(coll != null)
		{
			if(isDeposit(coll.col_cat_id)) sheetDeposits.addRow(getDetailsRow(coll));
			else if(isGuarantee(coll.col_cat_id)) sheetGuarantees.addRow(getDetailsRow(coll));
			else if(isVRP(coll.col_cat_id)) sheetVRP.addRow(getDetailsRow(coll));
			else if(isResidentialRealEstate(coll.col_cat_id, coll.col_type_id)) sheetResidentialRealEstates.addRow(getDetailsRow(coll));
			else if(isCommercialRealEstate(coll.col_cat_id, coll.col_type_id)) sheetCommercialRealEstates.addRow(getDetailsRow(coll));
			else if(isVehicle(coll.col_cat_id)) sheetVehicles.addRow(getDetailsRow(coll));
			else if(isLand(coll.col_cat_id, coll.col_type_id)) sheetLands.addRow(getDetailsRow(coll));
			else if(isMovable(coll.col_cat_id, coll.col_type_id)) sheetMovables.addRow(getDetailsRow(coll));
			else if(isInsurancePolicy(coll.col_cat_id)) sheetInsurancePolicies.addRow(getDetailsRow(coll));
		}
		
		bc.debug("Zapisani svi redovi.");

    	workbook.closeAllWorksheets();
    	workbook.close();
        bc.debug("Zatvoren workbook.");
        
        // slanje maila
        YXY70.send(bc, "csv209", bc.getLogin(), fileName + ".xml");
        bc.debug("Mail poslan.");

        bc.debug("Obrada zavrsena.");
		return RemoteConstants.RET_CODE_SUCCESSFUL;
    }


	/**
	 * Metoda koja stvara ili ažurira data objekt s podacima iz iteratora.
	 * @param coll Data objekt
	 * @param iter Iterator
	 * @return data objekt
	 */
	private CollData getCollDataFromIterator(CollData coll, CollIterator iter) throws Exception
	{
		if(coll == null)
		{
			coll = new CollData();
			coll.col_hea_id = iter.col_hea_id();
			coll.col_type_id = iter.col_type_id();
			coll.col_num = iter.col_num();
			coll.market_value = iter.market_value();
			coll.rec_lop = iter.rec_lop();
			coll.com_doc = iter.com_doc();
			coll.inspol_ind = iter.inspol_ind();
			coll.b2_eligibility = iter.b2_eligibility(); 
			coll.status = iter.status();
			coll.col_cat_id = iter.col_cat_id();
			coll.rba_eligibility = iter.rba_eligibility();
			coll.law_eligibility = iter.law_eligibility();
			coll.b1_eligibility = iter.b1_eligibility();
			coll.b2irb_eligibility = iter.b2irb_eligibility();
			coll.cust_id = iter.cust_id();
			coll.cust_name = iter.cust_name();
			coll.b2_asset = iter.b2_asset();
			coll.cur_id = iter.cur_id();
			coll.code_char  = iter.code_char();
			coll.org_uni_code = iter.org_uni_code();
			coll.org_uni_name = iter.org_uni_name();
			coll.col_group = getColGroup(iter);
		}
		else
		{
			String col_group = getColGroup(iter);
			if( (col_group.equals("Corporate") && !coll.col_group.equals("Corporate")) || (col_group.equals("Micro")) && coll.col_group.equals("Retail") )
			{
				coll.cust_id = iter.cust_id();
				coll.cust_name = iter.cust_name();
				coll.b2_asset = iter.b2_asset();
				coll.col_group = col_group;
			}
		}
		return coll;
	}
	
	/**
	 * Metoda koja odreðuje pripadnost kolaterala na temelju b2 asset klase.
	 * @param iter Iterator
	 */
	public String getColGroup(CollIterator iter) throws Exception
	{
		String b2_asset = iter.b2_asset().trim();
		if(b2_asset.equals("1") || b2_asset.equals("36")) return "Retail";
		else if(b2_asset.equals("20")) return "Micro";
		else return "Corporate";
	}
	
	/** Metoda koja definira izgled kolona na tablici.
	 * @param col_cat_id ID vrste kolaterala
	 * @param col_type_id ID tipa kolaterala
	 * @return vektor s definiranim kolonama tablice
	 */
	private Vector getColumns(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		boolean isDeposit = isDeposit(col_cat_id);
		boolean isGuarantee = isGuarantee(col_cat_id);
		boolean isResidentialRealEstate = isResidentialRealEstate(col_cat_id, col_type_id);
		boolean isCommercialRealEstate = isCommercialRealEstate(col_cat_id, col_type_id);
		boolean isRealEstate = isResidentialRealEstate || isCommercialRealEstate;
		boolean isVehicle = isVehicle(col_cat_id);
		boolean isLand = isLand(col_cat_id, col_type_id);
		boolean isMovable = isMovable(col_cat_id, col_type_id);
		boolean isInsurancePolicy = isInsurancePolicy(col_cat_id);
		
		Vector columns = new Vector();
		columns.add(new Column(30));
		columns.add(new Column(180));
		columns.add(new Column(30));
		if(!isGuarantee && !isInsurancePolicy) columns.add(new Column(30));
		columns.add(new Column(30));
		if(isRealEstate || isMovable) columns.add(new Column(30));
		if(isRealEstate || isLand) columns.add(new Column(30));
		if(isDeposit) columns.add(new Column(80,"s4"));
		if(isGuarantee) columns.add(new Column(30));
		if(isCommercialRealEstate) columns.add(new Column(30));
		if(isVehicle) columns.add(new Column(30));
		columns.add(new Column(30));
		columns.add(new Column(30));
		columns.add(new Column(180,"s2"));
		columns.add(new Column(30));
		columns.add(new Column(180,"s2"));
		columns.add(new Column(30));
		columns.add(new Column(70));
		columns.add(new Column(250));
		columns.add(new Column(30));
		columns.add(new Column(60));
		columns.add(new Column(30));
		columns.add(new Column(180));
		return columns;
	}
	
	/** Metoda koja formira red sa zaglavljem tablice.
	 * @param col_cat_id ID vrste kolaterala
	 * @param col_type_id ID tipa kolaterala
	 * @return formirani red tablice
	 */
	private Row getHeaderRow(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		boolean isDeposit = isDeposit(col_cat_id);
		boolean isGuarantee = isGuarantee(col_cat_id);
		boolean isResidentialRealEstate = isResidentialRealEstate(col_cat_id, col_type_id);
		boolean isCommercialRealEstate = isCommercialRealEstate(col_cat_id, col_type_id);
		boolean isRealEstate = isResidentialRealEstate || isCommercialRealEstate;
		boolean isVehicle = isVehicle(col_cat_id);
		boolean isLand = isLand(col_cat_id, col_type_id);
		boolean isMovable = isMovable(col_cat_id, col_type_id);
		boolean isInsurancePolicy = isInsurancePolicy(col_cat_id);
		
		Row row = new Row(50, "s1");
		row.addCell(new Cell("Status"));
		row.addCell(new Cell("Collateral ID"));
		row.addCell(new Cell("RBA eligibility"));
		if(!isGuarantee && !isInsurancePolicy) row.addCell(new Cell("RBA pledge is legaly effective"));
		row.addCell(new Cell("Positive opinion from Legal Division"));
		if(isRealEstate || isMovable) row.addCell(new Cell("Valid insurance policy"));
		if(isRealEstate || isLand) row.addCell(new Cell("All documentation is delivered"));
		if(isDeposit) row.addCell(new Cell("Future maturity date"));
		if(isGuarantee) row.addCell(new Cell("Guarantee is on first demand"));
		if(isCommercialRealEstate) row.addCell(new Cell("Usage licence"));
		if(isVehicle) row.addCell(new Cell("All risk insurance policy only for cars"));
		row.addCell(new Cell("B2 St. eligibility"));
		row.addCell(new Cell("B2 IRB eligibility"));
		row.addCell(new Cell("Original nominal value"));
		row.addCell(new Cell("Original currency"));
		row.addCell(new Cell("Nominal value - EUR"));
		row.addCell(new Cell("B2 asset class"));
		row.addCell(new Cell("Customer ID"));
		row.addCell(new Cell("Customer name"));
		row.addCell(new Cell("B1 eligibility"));
		row.addCell(new Cell("Group"));
		row.addCell(new Cell("Branch"));
		row.addCell(new Cell("Branch name"));
		return row;
	}
	
	/** Metoda koja formira red tablice s podacima o kolateralu.
	 * @param iter Iterator s podacima o kolateralu
	 * @return formirani red tablice
	 */
	private Row getDetailsRow(CollData coll)
	{
		boolean isDeposit = isDeposit(coll.col_cat_id);
		boolean isGuarantee = isGuarantee(coll.col_cat_id);
		boolean isResidentialRealEstate = isResidentialRealEstate(coll.col_cat_id, coll.col_type_id);
		boolean isCommercialRealEstate = isCommercialRealEstate(coll.col_cat_id, coll.col_type_id);
		boolean isRealEstate = isResidentialRealEstate || isCommercialRealEstate;
		boolean isVehicle = isVehicle(coll.col_cat_id);
		boolean isLand = isLand(coll.col_cat_id, coll.col_type_id);
		boolean isMovable = isMovable(coll.col_cat_id, coll.col_type_id);
		boolean isInsurancePolicy = isInsurancePolicy(coll.col_cat_id);
		
		Row row = new Row();
		row.addCell(new Cell(coll.status));
		row.addCell(new Cell(coll.col_num));
		row.addCell(new Cell(coll.rba_eligibility));
		if(!isGuarantee && !isInsurancePolicy) row.addCell(new Cell(coll.rec_lop));
		row.addCell(new Cell(coll.law_eligibility));
		if(isRealEstate || isMovable) row.addCell(new Cell(coll.inspol_ind));
		if(isRealEstate || isLand) row.addCell(new Cell(coll.com_doc));
		if(isDeposit) row.addCell(new Cell(bo421.selectCdeDepUnti(coll.col_hea_id)));
		if(isGuarantee) row.addCell(new Cell(bo421.selectFirstCall(coll.col_hea_id)));
		if(isCommercialRealEstate) row.addCell(new Cell(bo421.selectBuildPermInd(coll.col_hea_id)));
		if(isVehicle) row.addCell(new Cell(bo421.selectVehKasko(coll.col_hea_id)));
		row.addCell(new Cell(coll.b2_eligibility));
		row.addCell(new Cell(coll.b2irb_eligibility));
		row.addCell(new Cell(coll.market_value));
		row.addCell(new Cell(coll.code_char));
		BigDecimal rate = (BigDecimal)euroExchangeRate.get(coll.cur_id);
		BigDecimal market_value_EUR;
		if(rate == null || coll.market_value == null) market_value_EUR = new BigDecimal(0);
		else market_value_EUR = coll.market_value.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
		row.addCell(new Cell(market_value_EUR));
		row.addCell(new Cell(coll.b2_asset));
		row.addCell(new Cell(coll.cust_id));
		row.addCell(new Cell(coll.cust_name));
		row.addCell(new Cell(coll.b1_eligibility));
		row.addCell(new Cell(coll.col_group));
		row.addCell(new Cell(coll.org_uni_code));
		row.addCell(new Cell(coll.org_uni_name));
		return row;
	}


	/** Metoda koja vraæa je li kolateral depozit. */
	private boolean isDeposit(BigDecimal col_cat_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(612223)) == 0);
	}

	/** Metoda koja vraæa je li kolateral garancija. */
	private boolean isGuarantee(BigDecimal col_cat_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(615223)) == 0);
	}
	
	/** Metoda koja vraæa je li kolateral vrijednosni papir. */
	private boolean isVRP(BigDecimal col_cat_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(613223)) == 0) || (col_cat_id.compareTo(new BigDecimal(619223)) == 0) || (col_cat_id.compareTo(new BigDecimal(622223)) == 0) || (col_cat_id.compareTo(new BigDecimal(627223)) == 0) || (col_cat_id.compareTo(new BigDecimal(629223)) == 0);
	}
	
	/** Metoda koja vraæa je li kolateral stambena nekretnina. */
	private boolean isResidentialRealEstate(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(618223)) == 0) && (col_type_id.compareTo(new BigDecimal(8777)) == 0);
	}

	/** Metoda koja vraæa je li kolateral komercijalna nekretnina. */
	private boolean isCommercialRealEstate(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(618223)) == 0) && 
				( (col_type_id.compareTo(new BigDecimal(9777)) == 0) || (col_type_id.compareTo(new BigDecimal(10777)) == 0) || (col_type_id.compareTo(new BigDecimal(12777)) == 0) );
	}
	
	/** Metoda koja vraæa je li kolateral nekretnina. */
	private boolean isRealEstate(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		return isResidentialRealEstate(col_cat_id, col_type_id) || isCommercialRealEstate(col_cat_id, col_type_id); 
	}

	/** Metoda koja vraæa je li kolateral vozilo. */
	private boolean isVehicle(BigDecimal col_cat_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(624223)) == 0);
	}

	/** Metoda koja vraæa je li kolateral zemljište. */
	private boolean isLand(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(618223)) == 0) && (col_type_id.compareTo(new BigDecimal(7777)) == 0);
	}

	/** Metoda koja vraæa je li kolateral pokretnina. */
	private boolean isMovable(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(621223)) == 0) || (col_cat_id.compareTo(new BigDecimal(620223)) == 0) || (col_cat_id.compareTo(new BigDecimal(626223)) == 0);
	}

	/** Metoda koja vraæa je li kolateral polica osiguranja. */
	private boolean isInsurancePolicy(BigDecimal col_cat_id)
	{
		return (col_cat_id.compareTo(new BigDecimal(616223)) == 0);
	}


	/**
	 * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
	 * Parametri se predaju u obliku "RB;load_date;X".
	 * @return da li su dohvat i provjera uspješno završili
	 */
	private boolean getParameters(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 3)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
	     		load_date = DateUtils.parseDate(bc.getArg(1).trim());
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("25915892"));
        batchParameters.setArgs(args);
        new BO420().run(batchParameters);
    }
}