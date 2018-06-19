package hr.vestigo.modules.collateral.batch.bo32;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.Batch;
import hr.vestigo.framework.remote.batch.BatchContext;
import hr.vestigo.framework.remote.batch.BatchParameters;
import hr.vestigo.modules.collateral.batch.bo32.BO321.ColHfIterator;
import hr.vestigo.modules.rba.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Vector;


/**
 *
 * Promjena datuma "važenje hipoteke do"
 * @author hrakis
 * 
 */
public class BO320 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo32/BO320.java,v 1.18 2017/01/18 14:09:46 hrakis Exp $";
	
	private BatchContext bc;
	private BO321 bo321;
	private String retCode;
	private BigDecimal col_pro_id;
	
	private final BigDecimal bond_col_cat_id = new BigDecimal("619223");
	private final BigDecimal depostit_col_cat_id = new BigDecimal("612223");
	private final BigDecimal guarantee_col_cat_id = new BigDecimal("615223");
	private final BigDecimal real_estate_col_cat_id = new BigDecimal("618223");
	private final Date max_date = Date.valueOf("9999-12-31");
	
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO320 pokrenut.");
		this.bc = bc;
		this.bo321 = new BO321(bc);
		this.retCode = RemoteConstants.RET_CODE_SUCCESSFUL;
		HashMap<BigDecimal, Date> dates = new HashMap<BigDecimal, Date>();
		HashMap<BigDecimal, Date> old_dates = new HashMap<BigDecimal, Date>();
		
        // ubacivanje eventa
		BigDecimal eve_id = bo321.insertIntoEvent();
		if (eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Insertiran event.");
		
		// provjera parametara
        if (!checkParameters(bc)) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri provjereni.");
		
		// dohvat iteratora
		ColHfIterator iter = getIterator();
		if (iter == null) return RemoteConstants.RET_CODE_ERROR;
		
		// za svaki dohvaæeni zapis izraèunaj datum hipoteke
	    int count = 0;
		while (iter.next())
		{
			bc.debug("Kolateral " + iter.col_num().trim() + " - hipoteka #" + iter.hf_priority() + " (COLL_HF_PRIOR_ID=" + iter.coll_hf_prior_id() + ") - vazeci datum = " + iter.datum_hipoteke_old());
			old_dates.put(iter.coll_hf_prior_id(), iter.datum_hipoteke_old());
			count++;
			
			// izraèunaj datum hipoteke
			Date datum_hipoteke = getDate(iter);
			bc.debug("...izracunati datum = " + datum_hipoteke);
			if (datum_hipoteke == null) continue;
			
			// preskoèi ako je izraèunati datum stariji od trenutno spremljenog izraèunatog datuma hipoteke
			Date spremljeni_datum_hipoteke = dates.get(iter.coll_hf_prior_id());
			if (spremljeni_datum_hipoteke != null && DateUtils.whoIsOlder(datum_hipoteke, spremljeni_datum_hipoteke) == -1) continue;
			
			// spremi izraèunati datum hipoteke
			bc.debug("...datum je najveci do sada");
			dates.put(iter.coll_hf_prior_id(), datum_hipoteke);
		}
		iter.close();
		bc.info("Ukupno obradjeno zapisa: " + count);
		bc.info("Ukupno obradjeno hipoteka: " + old_dates.size());
		
		// zapiši izraèunate datume u bazu podataka
		count = 0;
		bc.beginTransaction();
		for (BigDecimal coll_hf_prior_id : dates.keySet())
		{
		    Date datum_hipoteke = dates.get(coll_hf_prior_id);
		    Date datum_hipoteke_old = old_dates.get(coll_hf_prior_id);
            if (datum_hipoteke_old != null && DateUtils.whoIsOlder(datum_hipoteke_old, datum_hipoteke) == 0) continue;  // preskoèi ako se izraèunati datum ne razlikuje od važeæeg datuma
		    
            bc.info("COLL_HF_PRIOR_ID = " + coll_hf_prior_id + ": " + datum_hipoteke_old + " -> " + datum_hipoteke);
            if (!bo321.updateColHf(coll_hf_prior_id, datum_hipoteke)) return RemoteConstants.RET_CODE_ERROR;
            if (!bo321.updateIntoInDataDwhItem(col_pro_id, coll_hf_prior_id, datum_hipoteke_old, null)) return RemoteConstants.RET_CODE_ERROR;
		    count++;
		}
		bc.commitTransaction();
		bc.info("Ukupno azurirano hipoteka: " + count);
				
		// zapiši broj ažuriranih slogova u tablicu COL_PROC
		if (!bo321.updateColProc(col_pro_id, count)) return RemoteConstants.RET_CODE_ERROR;
		bc.setCounter(count);
		
		return retCode;
    }
	
	
	/**
	 * Metoda raèuna datum do kada važi hipoteka ovisno o tome je li hipoteka dio okvirnog sporazuma ili nije, te da li je kolateral obveznica,garancija,depozit ili nije.
	 * @param iter iterator s podacima
	 * @return izraèunati datum do kada važi hipoteka
	 */
	private Date getDate(ColHfIterator iter) throws Exception
	{
	    Date datum_hipoteke = null;
	    
        if (isDeposit(iter.col_cat_id()))  // ako je kolateral depozit
        {
            bc.debug("...kolateral je depozit.");
            Date datum_dospijeca = bo321.selectDepositMaturityDate(iter.col_hea_id());
            if (datum_dospijeca == null)
            {
                bc.warning("...Krajnji datum dospijeca depozita je NULL ili slog ne postoji za COL_HEA_ID="+iter.col_hea_id());
                retCode = RemoteConstants.RET_CODE_WARNING;
                return null;
            }
            bc.debug("...datum dospijeca depozita = " + datum_dospijeca);
            datum_hipoteke = datum_dospijeca;
        }
        else if (isBond(iter.col_cat_id()) || isGuarantee(iter.col_cat_id()))  // ako je kolateral obveznica ili garancija
        {
            bc.debug("...kolateral je obveznica ili garancija.");
            
            Date datum_dospijeca;
            if (isBond(iter.col_cat_id())) datum_dospijeca = bo321.selectBondMaturityDate(iter.col_hea_id());
            else datum_dospijeca = bo321.selectGuaranteeMaturityDate(iter.col_hea_id());
            
            bc.debug("...datum dospijeca obveznice/garancije = " + datum_dospijeca);
            
            if (datum_dospijeca != null)
            {
                Date datum_uvecani;
                if (iter.okvir_id() != null)  // ako je hipoteka dio okvirnog sporazuma
                {
                    bc.debug("...hipoteka je dio okvirnog sporazuma (FRA_AGR_ID=" + iter.okvir_id() + ")");
                    Date datum_dospijeca_sporazuma = bo321.selectFrameAgreementMaturityDate(iter.okvir_id());
                    if (datum_dospijeca_sporazuma != null)
                    {
                        bc.debug("...datum dospijeca sporazuma = " + datum_dospijeca_sporazuma);
                        datum_uvecani = datum_dospijeca_sporazuma;
                    }
                    else
                    {
                        bc.warning("...Datum dospijeca sporazuma je NULL ili slog ne postoji za FRA_AGR_ID="+iter.okvir_id());
                        retCode = RemoteConstants.RET_CODE_WARNING;
                        return null;
                    }
                }
                else  // ako hipoteka nije dio okvirnog sporazuma
                {
                    bc.debug("...hipoteka NIJE dio okvirnog sporazuma - uzima se datum dospijeca plasmana (" + iter.datum_plasmana() + ") + 10 god.");
                    datum_uvecani = DateUtils.addOrDeductYearsFromDate(iter.datum_plasmana(), 10);
                }
                
                if (DateUtils.whoIsOlder(datum_uvecani, datum_dospijeca) == -1)
                {
                    datum_hipoteke = datum_uvecani;
                }
                else
                {
                    datum_hipoteke = datum_dospijeca;
                }
            }
            else
            {
                bc.warning("...Datum dospijeca kolaterala je NULL ili slog ne postoji za COL_HEA_ID="+iter.col_hea_id());
                retCode = RemoteConstants.RET_CODE_WARNING;
                return null;
            }
        }
        else  // ako kolateral nije ni obveznica, ni depozit, ni garancija
        {
            if (iter.okvir_id() != null)  // ako je hipoteka dio okvirnog sporazuma
            {
                bc.debug("...hipoteka je dio okvirnog sporazuma (FRA_AGR_ID=" + iter.okvir_id() + ")");
                Date datum_dospijeca_sporazuma = bo321.selectFrameAgreementMaturityDate(iter.okvir_id());
                if (datum_dospijeca_sporazuma != null)
                {
                    bc.debug("...datum dospijeca sporazuma = " + datum_dospijeca_sporazuma);
                    datum_hipoteke = datum_dospijeca_sporazuma;
                }
                else
                {
                    bc.warning("...Datum dospijeca sporazuma je NULL ili slog ne postoji za FRA_AGR_ID="+iter.okvir_id());
                    retCode = RemoteConstants.RET_CODE_WARNING;
                    return null;
                }
            }
            else  // ako hipoteka nije dio okvirnog sporazuma
            {
                int yearsToAdd = 10;
                if (isRealEstate(iter.col_cat_id()) && isSSP(iter)) yearsToAdd = 20;  // ako je nekretnina i plasman je SSP -> 20 godina
                bc.debug("...hipoteka NIJE dio okvirnog sporazuma - uzima se datum dospijeca plasmana (" + iter.datum_plasmana() + ") + " + yearsToAdd + " god.");
                datum_hipoteke = DateUtils.addOrDeductYearsFromDate(iter.datum_plasmana(), yearsToAdd);
            }
        }

        // provjeri overflow
        if (datum_hipoteke.getYear() + 1900 > 9999) datum_hipoteke = max_date;
        
        return datum_hipoteke;
	}
	
	
    /** 
     * Metoda koja vraæa da li je plasman na SSP. 
     */
    private boolean isSSP(ColHfIterator iter) throws Exception
    {
        if (iter.module_code() == null || iter.cus_acc_orig_st() == null) return false;
        
        String module = iter.module_code().trim();
        String status = iter.cus_acc_orig_st().trim();
        
        return (module.equals("TRC") && status.equals("E")) ||
               (module.equals("PKR") && (status.equals("T") || status.equals("WA") || status.equals("WP") || status.equals("WU"))) ||
               (module.equals("PPZ") && (status.equals("SS") || status.equals("NM"))) ||
               (module.equals("SDR") && (status.equals("SS") || status.equals("NM"))) ||
               (module.equals("KRD") && (status.equals("SS") || status.equals("WA") || status.equals("WP") || status.equals("WU"))) ||
               (module.equals("GAR") && status.equals("SS")) ||
               (module.equals("KKR") && (status.equals("94") || status.equals("95"))) ||
               (module.equals("LOC") && status.equals("SS"));
    }
	
	
	/**
	 * Metoda dohvaæa iterator s podacima o hipotekama koje ulaze u obradu.
	 */
	private ColHfIterator getIterator()
	{
        // provjeriti da li u tabeli COL_PROC za ovu vrstu obrade i current date postoji slog
        col_pro_id = null;
        String proc_status = null;
        Vector vect = bo321.selectColProId();
        if (vect == null) return null;
        if (vect.get(0) != null) col_pro_id = (BigDecimal)vect.get(0);
        if (vect.get(1) != null) proc_status = (String)vect.get(1);
        bc.debug("col_pro_id = " + col_pro_id);
        bc.debug("proc_status = " + proc_status);
        
        if (col_pro_id == null)  // ako obrada za današnji datum ne postoji, unesi zapis o zapoèetoj obradi i dohvati hipoteke
        {
            col_pro_id = bo321.insertIntoColProc();
            if (col_pro_id == null) return null;
            return bo321.selectColHf();
        }
        else if (col_pro_id != null && proc_status.equals("0"))  // ako obrada postoji i status je '0', dohvati hipoteke
        {
            return bo321.selectColHf();
        }
        else  // ako obrada postoji i status je '1', obrada se prekida sa RC=108 i zapisuje odgovarajuæa poruka u log
        {
            bc.error("Za current date obrada je pustena i uredno zavrsila!", new String[]{col_pro_id.toString(),proc_status});
            return null;
        }
	}

	
	/** Vraæa je li kategorija kolaterala obveznica. */
	private boolean isBond(BigDecimal col_cat_id)
	{
		return (col_cat_id.compareTo(bond_col_cat_id) == 0);
	}
	/** Vraæa je li kategorija kolaterala depozit. */	
	private boolean isDeposit(BigDecimal col_cat_id)
	{
		return (col_cat_id.compareTo(depostit_col_cat_id) == 0);
	}
	/** Vraæa je li kategorija kolaterala garancija. */	
	private boolean isGuarantee(BigDecimal col_cat_id)
	{
		return (col_cat_id.compareTo(guarantee_col_cat_id) == 0);
	}
	/** Vraæa je li kategorija kolaterala nekretnina. */ 
    private boolean isRealEstate(BigDecimal col_cat_id)
    {
        return (col_cat_id.compareTo(real_estate_col_cat_id) == 0);
    }
	
	/**
	 * Metoda provjerava broj i ispravnost ulaznih parametara.
	 * @return da li je provjera prošla
	 */
	private boolean checkParameters(BatchContext bc)
	{
        try
        {
	        int brojParametara = bc.getArgs().length;
			for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i +". = " + bc.getArg(i));  // ispis parametara                   
	    	if (brojParametara == 1)
	    	{
	    		if (bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
	     		bc.setBankSign(bc.getArg(0));
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2540064003"));
        batchParameters.setArgs(args);
        new BO320().run(batchParameters);
    }
}