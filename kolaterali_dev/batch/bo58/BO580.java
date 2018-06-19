package hr.vestigo.modules.collateral.batch.bo58;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo58.BO581.KolateralIterator;
import hr.vestigo.modules.collateral.common.yoyE.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;


/**
 * Izraèun WCA vrijednosti kolaterala
 * @author hramkr
 */
public class BO580 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo58/BO580.java,v 1.3 2011/09/01 13:32:21 hramkr Exp $";
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("**** BO580 pokrenut.");
        BO581 bo581 = new BO581(bc);
        
        // ubacivanje eventa
        if(bo581.insertIntoEvent() == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
        
        // dohvat podataka o kolateralima
        KolateralIterator iter = bo581.selectCollateral();
        if(iter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("**** Dohvaceni podaci o kolateralima.");
        
        HashMap hm_ponder = new HashMap();
        BigDecimal other_mrtg_amount = new BigDecimal("0.00");
        BigDecimal wca_amount = new BigDecimal("0.00");
        Date process_date= new Date(System.currentTimeMillis());
        BigDecimal oneAmount = new BigDecimal ("1.00");
        BigDecimal wca_his_id = null;
        BigDecimal col_sub_id = null;
        String ponder_type = null;
        BigDecimal ponder_value = new BigDecimal("0.00");
        BigDecimal zeroAmount = new BigDecimal ("0.00");
        BigDecimal hundredAmount = new BigDecimal ("100.00");
        
        // Constants
        final BigDecimal estateColCatId  = new BigDecimal(618223);   
        final BigDecimal vehicleColCatId = new BigDecimal(624223); 
        final BigDecimal vesselColCatId  = new BigDecimal(620223); 
        final BigDecimal movableColCatId = new BigDecimal(621223);       
        
        // obrada dohvaæenih podataka
        while(iter.next())
        {
            bc.debug("**** COL_HEA_ID = " + iter.col_hea_id() + "   **** col_cat_id = " + iter.col_cat_id() + "**** col_typ_id = " + iter.col_typ_id() + " **** market_value = " + iter.market_value());

            other_mrtg_amount = new BigDecimal("0.00");
            wca_amount = new BigDecimal("0.00");
            col_sub_id = null;
            wca_his_id = null;

            // dohvati podvrstu ako treba
            
           if (iter.col_cat_id().compareTo(estateColCatId) == 0 || iter.col_cat_id().compareTo(vehicleColCatId) == 0 || 
               iter.col_cat_id().compareTo(vesselColCatId) == 0 || iter.col_cat_id().compareTo(movableColCatId) == 0) {
               col_sub_id = bo581.selectCollateralSubType(iter.col_hea_id(), iter.col_cat_id(), estateColCatId, vehicleColCatId, vesselColCatId, movableColCatId);  
           }
            
           bc.debug("**** dohvacen col_sub_id = " + col_sub_id + " za col_cat_id = " + iter.col_cat_id() );
                 
            // dohvati ponder
            
            hm_ponder = bo581.selectCollateralPonder(iter, col_sub_id); 
            
            ponder_type = (String) hm_ponder.get("ponder_type");
            ponder_value = (BigDecimal) hm_ponder.get("ponder_value");
            		

            bc.debug("**** dohvacen ponder (ponder_type + ponder_value)  = " + ponder_type + " - " + ponder_value );
             
            if (ponder_value == null)
                ponder_value = zeroAmount;
            // dohvati zbroj svih visih tudjih hipoteka
             
            YOYE1 YOYE1_find = new YOYE1(bc);
            other_mrtg_amount = YOYE1_find.getMortgagesSumToFirstRba(iter.col_hea_id(), iter.cur_id());  // tudje hipoteke

            bc.debug("**** suma other_mortgages = " + other_mrtg_amount + "**** ND prihvatljivost = " + iter.nd_eligibility());            
            
            // izracunaj WCA
            if (iter.market_value() == null)
                wca_amount = new BigDecimal("0.00");
            else {
                wca_amount = iter.market_value().multiply(ponder_value);
                wca_amount = wca_amount.divide(hundredAmount);
                wca_amount = wca_amount.subtract(other_mrtg_amount);
                wca_amount = wca_amount.divide(oneAmount, 2, BigDecimal.ROUND_HALF_UP);
            }

// ako je kolateral ND neprihvatljiv, WCA = 0.00            
            if (iter.nd_eligibility() == null || (iter.nd_eligibility() != null && iter.nd_eligibility().equalsIgnoreCase("N")))
                wca_amount = new BigDecimal("0.00");
            
// AKO JE WCA < 0.00 -> WCA = 0.00 
            if (wca_amount.signum() == -1 ) {
                wca_amount = new BigDecimal("0.00");
            }
            
            bc.debug("**** izracunata WCA = " + wca_amount);               
            
            // provjeri postoji li veæ zapis u tablici CASHDEP_DWH
            wca_his_id = bo581.selectCollateralCountInWcaHistory(iter.col_hea_id(), process_date);  
            bc.debug("**** provjera da li postoj zapis za WCA = " + wca_his_id);   
            
            if(wca_his_id == null)  // ako zapis ne postoji, ubaci novi
            {
                if(!bo581.insertIntoWcaHistory(iter, process_date, hm_ponder, other_mrtg_amount, wca_amount)) return RemoteConstants.RET_CODE_ERROR;
                bc.debug("**** dodan zapis za WCA za col_hea_id = " + iter.col_hea_id());                 
            }
            else // ako zapis postoji, ažuriraj postojeæi
            {
                if(!bo581.updateIntoWcaHistory(iter, process_date, hm_ponder, other_mrtg_amount, wca_his_id, wca_amount)) return RemoteConstants.RET_CODE_ERROR;
                bc.debug("**** promijenjen zapis za WCA za col_hea_id = " + iter.col_hea_id());      
            }
 /*           else  // greška
            {
                return RemoteConstants.RET_CODE_ERROR;
            } */
        }
      
        bc.debug("**** Obrada bo58 zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("4503815704"));
        batchParameters.setArgs(args);
        new BO580().run(batchParameters);
    }
}