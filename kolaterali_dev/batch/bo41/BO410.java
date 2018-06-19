package hr.vestigo.modules.collateral.batch.bo41;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo41.BO411.CollIterator;
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
 * Prihvatljivost i plasmani.
 * @author hrakis
  */
public class BO410 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo41/BO410.java,v 1.9 2009/11/25 08:26:32 hrakis Exp $";
	private BO411 bo411;
	private HashMap euroExchangeRate;
	private Date load_date;
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO410 pokrenut.");
		bo411 = new BO411(bc);
		
        // insertiranje eventa
		BigDecimal eve_id = bo411.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// dohvat parametara
        if(!getParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.");
		
		// dohvat teèajne liste
		euroExchangeRate = bo411.selectEuroExchangeRate();
		if(euroExchangeRate == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvacena tecajna lista.");
		
		// dohvat podataka o kolateralima
		CollIterator iter = bo411.selectCollaterals(load_date);
		if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Dohvaceni podaci.");
		
		String dateString = new SimpleDateFormat("yyyyMMdd").format(load_date);
		String dir = bc.getOutDir() + "/";
		String fileName = dir + "Prihvatljivost_i_plasmani_" + dateString;

		// stvaranje workbooka
		Workbook workbook = new Workbook(fileName, Workbook.FileType.ExcelML, true, 100);
		workbook.addStyle(new Style("Default", Alignment.Bottom, Font.Arial10));
		workbook.addStyle(new Style("s1", Alignment.BottomCenter, Borders.All, Font.Arial10Bold, new Interior("#FFFF00"), null));
		workbook.addStyle(new Style("s2", null, null, null, null, "Fixed"));
		workbook.addStyle(new Style("s3", null, null, null, null, "0"));
		workbook.open();
		bc.debug("Otvoren workbook.");
		
		// stvaranje sheetova
		Worksheet sheetResidentialRealEstates = workbook.addWorksheet(new Worksheet("stambene nekretnine", dir + "stNekretnine_" + dateString));
		Worksheet sheetCommercialRealEstates = workbook.addWorksheet(new Worksheet("komercijalne nekretnine", dir + "poslNekretnine_" + dateString));
		Worksheet sheetVehicles = workbook.addWorksheet(new Worksheet("vozila", dir + "vozila_" + dateString));
		Worksheet sheetLands = workbook.addWorksheet(new Worksheet("zemlji\u0161ta", dir + "zemljista_" + dateString));
		Worksheet sheetMovables = workbook.addWorksheet(new Worksheet("pokretnine,plovila,zalihe", dir + "pokretnine_" + dateString));
		bc.debug("Stvoreni sheetovi.");

		// definiranje kolona
		sheetResidentialRealEstates.addColumns(getColumns(new BigDecimal(618223), new BigDecimal(8777)));
		sheetCommercialRealEstates.addColumns(getColumns(new BigDecimal(618223), new BigDecimal(9777)));
		sheetVehicles.addColumns(getColumns(new BigDecimal(624223), null));
		sheetLands.addColumns(getColumns(new BigDecimal(618223), new BigDecimal(7777)));
		sheetMovables.addColumns(getColumns(new BigDecimal(621223), null));
		bc.debug("Definirane kolone.");

		// otvaranje sheetova
		workbook.openAllWorksheets();
		bc.debug("Otvoreni sheetovi.");
		
		// formiranje zaglavlja na sheetovima
		sheetResidentialRealEstates.addRow(getHeaderRow(new BigDecimal(618223), new BigDecimal(8777)));
		sheetCommercialRealEstates.addRow(getHeaderRow(new BigDecimal(618223), new BigDecimal(9777)));
		sheetVehicles.addRow(getHeaderRow(new BigDecimal(624223), null));
		sheetLands.addRow(getHeaderRow(new BigDecimal(618223), new BigDecimal(7777)));
		sheetMovables.addRow(getHeaderRow(new BigDecimal(621223), null));
		bc.debug("Dodana zaglavlja.");
	
		// zapisivanje podataka u tablicu
		while(iter.next())
		{
			if(isResidentialRealEstate(iter.col_cat_id(), iter.col_type_id())) sheetResidentialRealEstates.addRow(getDetailsRow(iter));
    		else if(isCommercialRealEstate(iter.col_cat_id(), iter.col_type_id())) sheetCommercialRealEstates.addRow(getDetailsRow(iter));
    		else if(isVehicle(iter.col_cat_id(), iter.col_type_id())) sheetVehicles.addRow(getDetailsRow(iter));
    		else if(isLand(iter.col_cat_id(), iter.col_type_id())) sheetLands.addRow(getDetailsRow(iter));
    		else if(isMovable(iter.col_cat_id(), iter.col_type_id())) sheetMovables.addRow(getDetailsRow(iter));
		}
		bc.debug("Zapisani svi redovi.");
		
    	workbook.closeAllWorksheets();
    	workbook.close();
        bc.debug("Zatvoren workbook.");
        
        // slanje maila
        YXY70.send(bc, "csv208", bc.getLogin(), fileName + ".xml");
        bc.debug("Mail poslan.");
        
        bc.debug("Obrada zavrsena.");
		return RemoteConstants.RET_CODE_SUCCESSFUL;
    }


	/** Metoda koja definira izgled kolona na tablici.
	 * @param col_cat_id ID vrste kolaterala
	 * @param col_type_id ID tipa kolaterala
	 * @return vektor s definiranim kolonama tablice
	 */
	private Vector getColumns(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		boolean isRealEstate = isRealEstate(col_cat_id, col_type_id);
		boolean isVehicle = isVehicle(col_cat_id, col_type_id);
		boolean isLand = isLand(col_cat_id, col_type_id);
		boolean isMovable = isMovable(col_cat_id, col_type_id);
		
		Vector columns = new Vector();
		columns.add(new Column(30));
		columns.add(new Column(180));
		if(isRealEstate || isMovable) columns.add(new Column(30));
		if(isRealEstate || isLand) columns.add(new Column(30));
		if(isRealEstate || isLand) columns.add(new Column(30));
		if(isVehicle) columns.add(new Column(30));
		columns.add(new Column(30));
		columns.add(new Column(30));
		columns.add(new Column(30));
		columns.add(new Column(180,"s2"));
		columns.add(new Column(30));
		columns.add(new Column(180,"s2"));
		columns.add(new Column(30));
		columns.add(new Column(70));
		columns.add(new Column(250));
		columns.add(new Column(150));
		columns.add(new Column(150));
		columns.add(new Column(30));
		columns.add(new Column(30));
		columns.add(new Column(30));
		columns.add(new Column(250));
		return columns;
	}
	
	/** Metoda koja formira red sa zaglavljem tablice.
	 * @param col_cat_id ID vrste kolaterala
	 * @param col_type_id ID tipa kolaterala
	 * @return formirani red tablice
	 */
	private Row getHeaderRow(BigDecimal col_cat_id, BigDecimal col_type_id)
	{
		boolean isRealEstate = isRealEstate(col_cat_id, col_type_id);
		boolean isVehicle = isVehicle(col_cat_id, col_type_id);
		boolean isLand = isLand(col_cat_id, col_type_id);
		boolean isMovable = isMovable(col_cat_id, col_type_id);
		
		Row row = new Row(50, "s1");
		row.addCell(new Cell("Status"));
		row.addCell(new Cell("Collateral ID"));
		if(isRealEstate || isMovable) row.addCell(new Cell("Valid insurance policy"));
		if(isRealEstate || isLand) row.addCell(new Cell("All documentation is delivered"));
		if(isRealEstate || isLand) row.addCell(new Cell("Documentation missing"));
		if(isVehicle) row.addCell(new Cell("All risk insurance policy only for cars"));
		row.addCell(new Cell("RBA eligibility"));
		row.addCell(new Cell("B2 St. eligibility"));
		row.addCell(new Cell("B2 IRB eligibility"));
		row.addCell(new Cell("Original nominal value"));
		row.addCell(new Cell("Original currency"));
		row.addCell(new Cell("Nominal value - EUR"));
		row.addCell(new Cell("B2 asset class"));
		row.addCell(new Cell("Customer ID"));
		row.addCell(new Cell("Customer name"));
		row.addCell(new Cell("Deal ID"));
		row.addCell(new Cell("Deal Request No"));
		row.addCell(new Cell("Deal DWH status"));
		row.addCell(new Cell("Deal original status"));
		row.addCell(new Cell("Branch"));
		row.addCell(new Cell("Branch Name"));
		return row;
	}
	
	/** Metoda koja formira red tablice s podacima o kolateralu
	 * @param iter Iterator s podacima o kolateralu
	 * @return formirani red tablice
	 */
	private Row getDetailsRow(CollIterator iter) throws Exception
	{
		boolean isRealEstate = isRealEstate(iter.col_cat_id(), iter.col_type_id());
		boolean isVehicle = isVehicle(iter.col_cat_id(), iter.col_type_id());
		boolean isLand = isLand(iter.col_cat_id(), iter.col_type_id());
		boolean isMovable = isMovable(iter.col_cat_id(), iter.col_type_id());
		
		Row row = new Row();
		row.addCell(new Cell(iter.status()));
		row.addCell(new Cell(iter.col_num()));
		if(isRealEstate || isMovable) row.addCell(new Cell(iter.inspol_ind()));
		if(isRealEstate || isLand) row.addCell(new Cell(iter.com_doc()));
		if(isRealEstate || isLand) row.addCell(new Cell(iter.missing_doc()));
		if(isVehicle) row.addCell(new Cell(bo411.selectVehKasko(iter.col_hea_id())));
		row.addCell(new Cell(iter.rba_eligibility()));
		row.addCell(new Cell(iter.b2_eligibility()));
		row.addCell(new Cell(iter.b2irb_eligibility()));
		row.addCell(new Cell(iter.market_value()));
		row.addCell(new Cell(iter.code_char()));
		BigDecimal rate = (BigDecimal)euroExchangeRate.get(iter.cur_id());
		BigDecimal market_value_EUR = iter.market_value().multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
		row.addCell(new Cell(market_value_EUR));
		row.addCell(new Cell(iter.b2_asset()));
		row.addCell(new Cell(iter.cust_id()));
		row.addCell(new Cell(iter.cust_name()));
		row.addCell(new Cell(iter.cus_acc_no()));
		row.addCell(new Cell(iter.request_no()));
		row.addCell(new Cell(iter.acc_dwh_status()));
		row.addCell(new Cell(iter.acc_orig_status()));
		row.addCell(new Cell(iter.org_uni_code()));
		row.addCell(new Cell(iter.org_uni_name()));
		return row;
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
	private boolean isVehicle(BigDecimal col_cat_id, BigDecimal col_type_id)
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("22060612"));
        batchParameters.setArgs(args);
        new BO410().run(batchParameters);
    }
}