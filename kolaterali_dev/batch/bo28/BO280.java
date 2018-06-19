package hr.vestigo.modules.collateral.batch.bo28;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo28.BO281.InsurancePoliciesIterator;
import hr.vestigo.modules.collateral.common.yoyF.YOYF0;
import hr.vestigo.modules.collateral.common.yoyF.YOYFData;
import hr.vestigo.modules.collateral.common.yoyG.YOYG0;
import hr.vestigo.modules.collateral.common.yoyG.YOYGData;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Vector;


/**
 *
 * Deaktivacija isteklih polica osiguranja i ažuriranje ovisnih prihvatljivosti kolaterala
 * @author hrakis
 * 
 */
public class BO280 extends Batch
{
	public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo28/BO280.java,v 1.11 2012/04/17 13:26:14 hrakis Exp $";
	
	private BatchContext bc;
	private BO281 bo281;
	private BigDecimal eve_id;
	
	public String executeBatch(BatchContext bc) throws Exception
	{        
		bc.debug("BO280 pokrenut.");
		this.bc = bc;
		bo281 = new BO281(bc);
		
        // insertiranje eventa
		eve_id = bo281.insertIntoEvent();
		if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
		
		// dohvat parametara
        if(!getParameters()) return RemoteConstants.RET_CODE_ERROR;
		bc.debug("Parametri dohvaceni.");
		
		// dohvat parametra odgode deaktivacije police osiguranja
		int delay = bo281.selectDeactivationDelay();
		
		// provjeri da li u tabeli COL_PROC za ovu vrstu obrade i current date postoji slog
		BigDecimal col_pro_id = null;
		String proc_status = null;
		Vector vect = bo281.selectColProId();
		if(vect == null) return RemoteConstants.RET_CODE_ERROR;
		if(vect.get(0) != null) col_pro_id = (BigDecimal)vect.get(0);
		if(vect.get(1) != null) proc_status = (String)vect.get(1);
		bc.debug("col_pro_id = " + col_pro_id);
		bc.debug("proc_status = " + proc_status);
		
		InsurancePoliciesIterator iter = null;
		if(col_pro_id == null)  // ako obrada za današnji datum ne postoji, unesi zapis o zapoèetoj obradi i dohvati police osiguranja
		{
			col_pro_id = bo281.insertIntoColProc();
			iter = bo281.selectInsurancePolicies(0, col_pro_id, delay);
			if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		}
		else if(col_pro_id != null && proc_status.equals("0"))  // ako obrada postoji i status je '0', dohvati samo one police koje još nisu obuhvaæene obradom
		{
			iter = bo281.selectInsurancePolicies(1, col_pro_id, delay);
			if(iter == null) return RemoteConstants.RET_CODE_ERROR;
		}
		else  // ako obrada postoji i status je '1', obrada se prekida sa RC=108 i zapisuje odgovarajuæa poruka u log
		{
			bc.error("Za current date obrada je pustena i uredno zavrsila!", new String[]{col_pro_id.toString(),proc_status});
			return RemoteConstants.RET_CODE_ERROR;
		}
		bc.debug("Dohvaceni podaci.");
		
		BigDecimal col_hea_id_old = null;    // varijabla za spremanje prethodnog kolaterala (na jednu policu može biti vezano više kolaterala)
		while(iter.next())
		{
		    deactivateInsurancePolicy(col_pro_id, iter);     // oznaèi policu osiguranja kao neaktivnu
			if(col_hea_id_old != null && !iter.col_hea_id().equals(col_hea_id_old))  // ako se promijenio kolateral, ažuriraj prihvatljivosti i osiguranost kolaterala
			{
				if(!updateEligibility(col_hea_id_old)) return RemoteConstants.RET_CODE_ERROR;
			}
			col_hea_id_old = iter.col_hea_id();  // spremi ID trenutnog kolaterala u varijablu
		}
		iter.close();
		if(!updateEligibility(col_hea_id_old)) return RemoteConstants.RET_CODE_ERROR;  // obradi i preostali kolateral (ako postoji)
		if(!bo281.updateColProc(col_pro_id)) return RemoteConstants.RET_CODE_ERROR;  // zapiši broj obraðenih slogova u tablicu COL_PROC

		bc.debug("Obrada zavrsena.");
		return RemoteConstants.RET_CODE_SUCCESSFUL;
    }

	/**
     * Metoda koja oznaèava policu osiguranja kao neaktivnu.
     * @param col_pro_id ID obrade
     * @param iter Iterator s policama osiguranja
     * @return da li je metoda uspješno završila
     */
    private boolean deactivateInsurancePolicy(BigDecimal col_pro_id, InsurancePoliciesIterator iter)
    {
        try
        {
            bc.startStopWatch("deactivateInsurancePolicy");
            bc.debug("IP_ID = " + iter.ip_id());
            bc.beginTransaction();
            
            YOYFData data = new YOYFData();  // common za bilježenje povijesti promjena
            data.ip_id = iter.ip_id();
            data.use_id = new BigDecimal(1);
            data.org_uni_id = new BigDecimal(53253);
            data.eve_id = eve_id;
            YOYF0 yoyF0 = new YOYF0(bc, data);
            yoyF0.selectOldState();  // dohvati staro stanje
            if(!bo281.updatePolicyStatus(iter.ip_id())) return false;  // oznaèi policu kao neaktivnu
            yoyF0.selectNewState();  // dohvati novo stanje
            yoyF0.insertIntoIpChgHistory();   // zapiši deaktivaciju u povijest promjena
            if(!bo281.insertIntoInDataDwhItem(col_pro_id, iter.ip_id(), "ip_act_noact:A->I", iter.col_hea_id())) return false;  // ubaci zapis o obraðenoj polici u tablicu IN_DATA_DWH_ITEM
            
            bc.commitTransaction();
            bc.stopStopWatch("deactivateInsurancePolicy");
        }
        catch(Exception ex)
        {
            bc.error("Greska kod deaktiviranja police osiguranja!", ex);
            return false;
        }
        return true;
    }
	
	/**
	 * Metoda koja zadanom kolateralu ažurira prihvatljivosti i osiguranost.
	 * @param col_hea_id ID kolaterala
	 * @return da li je metoda uspješno završila
	 */
	private boolean updateEligibility(BigDecimal col_hea_id)
	{
        try
        {
            if(col_hea_id == null) return true;
            bc.startStopWatch("updateEligibility");
            bc.debug("...COL_HEA_ID = " + col_hea_id);

            bc.beginTransaction();
            YOYG0 yoyG0 = new YOYG0(bc, col_hea_id);
            yoyG0.azurirajPrihvatljivosti();                                            // ažuriraj prihvatljivosti kolateralu
            YOYGData parametri = yoyG0.getParametri();
            String inspol_ind = parametri.osigurano() ? "D" : "N";
            bo281.updateInspolInd(col_hea_id, inspol_ind);                              // ažuriraj indikator osiguranosti
            if(parametri.jeLiVozilo()) bo281.updateVehKasko(col_hea_id, inspol_ind);    // ažuriraj indikator kasko osiguranja
            bc.commitTransaction();
            bc.stopStopWatch("updateEligibility");
        }
        catch(Exception ex)
        {
            bc.error("Greska kod azuriranja prihvatljivosti i osiguranosti!", ex);
            return false;
        }
	    return true;
	}

	/**
	 * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
	 * @return da li je provjera prošla
	 */
	private boolean getParameters()
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2532267003"));
        batchParameters.setArgs(args);
        new BO280().run(batchParameters);
    }
}