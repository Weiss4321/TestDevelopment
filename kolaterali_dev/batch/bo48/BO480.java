package hr.vestigo.modules.collateral.batch.bo48;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo48.BO481.DepositIterator;

import java.math.BigDecimal;

 
/**
 * Preuzimanje garantnih depozita iz Siriusa
 * @author hrakis
 */
public class BO480 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo48/BO480.java,v 1.3 2013/01/21 11:57:09 hrakis Exp $";
    
    public String executeBatch(BatchContext bc) throws Exception
    {        
        // inicijalizacija
        BO481 bo481 = new BO481(bc);
        
        // ubacivanje eventa
        bo481.insertIntoEvent();
        bc.debug("Ubacen event.");
        
        // dohvat podataka o depozitima
        DepositIterator iter = bo481.selectDeposits();
        bc.debug("Dohvaceni podaci o depozitima.");
        
        // obrada dohvaæenih podataka
        while (iter.next())
        {
            bc.debug("cde_account = " + iter.cde_account());
            
            // dohvati stanje glavnice depozita
            BigDecimal balance = bo481.selectDepositBalance(iter.cus_acc_id(), iter.cur_id());
            bc.debug("   balance = " + balance + " (" + iter.cur_id() + ")");
            
            // provjeri postoji li veæ zapis u tablici CASHDEP_DWH
            int count = bo481.selectDepositCountInCashdepDwh(iter.cde_account());
            
            if (count == 0)  // ako zapis ne postoji, ubaci novi
            {
                bo481.insertIntoCashdepDwh(iter, balance);
            }
            else if (count > 0)  // ako zapis postoji, ažuriraj postojeæi
            {
                bo481.updateCashdepDwh(iter, balance);
            }
        }
      
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    public static void main(String[] args)
    {
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("1049634304"));
        batchParameters.setArgs(args);
        new BO480().run(batchParameters);
    }
}