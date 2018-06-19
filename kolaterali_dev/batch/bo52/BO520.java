package hr.vestigo.modules.collateral.batch.bo52;

import hr.vestigo.framework.remote.RemoteConstants;
import hr.vestigo.framework.remote.batch.*;
import hr.vestigo.modules.collateral.batch.bo52.BO521.FrameIterator;
import hr.vestigo.modules.collateral.batch.bo52.BO521.AccountIterator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.*;


/**
 * Izraèun pokrivenosti plasmana iz okvira
 * @author hrakis
 */
public class BO520 extends Batch
{
    public static String cvsident = "@(#) $Header: /var/cvsroot/java/src/hr/vestigo/modules/collateral/batch/bo52/BO520.java,v 1.15 2012/01/18 14:50:59 hrakis Exp $";
    
    private BatchContext bc;
    private BO521 bo521;
    
    private String ref_proc_type;       // ulazni parametar - identifikator vrste referentne obrade (obrade koja je izraèunala pokrivenost okvira)
    
    private String proc_type;           // identifikator vrste obrade
    private BigDecimal col_pro_id;      // ID obrade
    private BigDecimal ref_col_pro_id;  // ID referentne obrade (obrade koja je izraèunala pokrivenost okvira)
    private Date value_date;            // datum za koji se raèuna pokrivenost
    private String exp_type_ind;        // flag koji govori da li su u tabeli redovni podaci o izloženosti, podaci za kraj mjeseca ili nešto drugo
    private HashMap exchangeRate;       // teèajna lista
    

    public String executeBatch(BatchContext bc) throws Exception
    {        
        bc.debug("BO520 pokrenut.");
        this.bc = bc;
        this.bo521 = new BO521(bc);
        
        // ubacivanje eventa
        BigDecimal eve_id = bo521.insertIntoEvent();
        if(eve_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Ubacen event.");
        
        // dohvat parametara
        if(!getParameters()) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Parametri dohvaceni.");
        
        if(ref_proc_type.length() == 2 && ref_proc_type.endsWith("G")) exp_type_ind = "DGK";  // datum valute glavne knjige
        else exp_type_ind = "DVA";  // datum valute analitike
        
        // dohvat datuma za koji se vrši izraèun pokrivenosti
        value_date = bo521.selectValueDate(exp_type_ind);
        if(value_date == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("value_date = " + value_date);
        
        // dohvati ID referentne obrade
        ref_col_pro_id = bo521.selectRefColProId(value_date, ref_proc_type);
        if(ref_col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("ref_col_pro_id = " + ref_col_pro_id);
        
        // dohvati ID obrade
        proc_type = ref_proc_type + "X";
        col_pro_id = bo521.selectColProId(value_date, proc_type);
        if(col_pro_id == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("col_pro_id = " + col_pro_id);
        
        // dohvat teèajne liste
        exchangeRate = bo521.selectExchangeRate(value_date);
        if(exchangeRate == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvacena tecajna lista.");
        
        // dohvat podataka o okvirima i njihovoj pokrivenosti kolateralima
        FrameIterator frameIter = bo521.selectFrames(ref_col_pro_id, col_pro_id, value_date, exp_type_ind);
        if(frameIter == null) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Dohvaceni podaci o okvirima i njihovoj pokrivenosti kolateralima.");
        
        // kreiranje memorijske strukuture dohvaæenih podataka
        Vector<FrameData> frames = getFrames(frameIter);
        if(frames == null) return RemoteConstants.RET_CODE_ERROR;
        frameIter.close();
        bc.debug("Kreirana memorijska struktura.");
        
        // izraèun pokrivenosti
        if(!calculate(frames)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Izvrsen izracun pokrivenosti plasmana iz okvira.");

        // evidentiranje završetka obrade
        if(!bo521.updateColProc(col_pro_id)) return RemoteConstants.RET_CODE_ERROR;
        bc.debug("Evidentiran zavrsetak obrade.");
        
        bc.debug("Obrada zavrsena.");
        return RemoteConstants.RET_CODE_SUCCESSFUL;
    }
    
    /**
     * Metoda koja kreira memorijsku reprezentaciju podataka o okvirima, plasmanima iz okvira, te kolateralima koji pokrivaju okvir.
     * @param frameIter Iterator s podacima o okviru/kolateralima
     * @return Vektor s okvirima. Ako se dogodila greška, vraæa null.
     */
    private Vector<FrameData> getFrames(FrameIterator frameIter) throws Exception
    {
        Vector<FrameData> frames = new Vector<FrameData>();
        FrameData frame = null;
        while(frameIter.next())
        {
            if(frame == null || !frameIter.cus_acc_id().equals(frame.cus_acc_id))
            {
                frame = new FrameData(frameIter.cus_acc_id(), frameIter.cus_acc_no());
                frames.add(frame);
                
                // dohvati sve plasmane iz okvira i ubaci ih u okvir 
                AccountIterator accountIter = bo521.selectAccounts(frame, value_date, exp_type_ind);
                if(accountIter == null) return null;
                while(accountIter.next())
                {
                    AccountData account = new AccountData();
                    account.cus_acc_id = accountIter.cus_acc_id();
                    account.cus_acc_no = accountIter.cus_acc_no();
                    account.exposureAmount = accountIter.exposure_balance();
                    account.exposureCurrency = accountIter.exposure_cur_id();
                    account.exposureAmountHRK = accountIter.exposure_balance().multiply((BigDecimal)exchangeRate.get(account.exposureCurrency)).setScale(2, RoundingMode.HALF_UP);
                    frame.accounts.add(account);
                }
                accountIter.close();
            }
            
            // formiraj podatke o kolateralu
            CollateralData collateral = new CollateralData();
            collateral.col_hea_id = frameIter.col_hea_id();
            collateral.col_num = frameIter.col_num();
            collateral.priority = frameIter.acc_prior();
            if(collateral.priority == null) collateral.priority = new Integer(99);  // ako je tip kolaterala nepoznat, postavi mu najmanji prioritet 
            collateral.coverageAmount = frameIter.exp_fc_amount();
            collateral.coverageCurrency = frameIter.exp_cur_id();
            collateral.coverageAmountHRK = frameIter.exp_coll_amount();
            
            // spremi kolateral u pripadajuæu kategoriju unutar okvira
            CollateralCategoryData category = frame.collateralCategories.get(collateral.priority);
            if(category == null)  // ako kategorija kolaterala veæ ne postoji, stvori novu kategoriju
            {
                category = new CollateralCategoryData(collateral.priority);
                frame.collateralCategories.put(collateral.priority, category);
            }
            category.collaterals.add(collateral);
        }
        return frames;
    }
    
    /**
     * Metoda koja vrši izraèun pokrivenosti plasmana iz okvira.
     * @param framesData Skup okvira
     * @return da li je metoda uspješno završila
     */
    private boolean calculate(Vector<FrameData> framesData) throws Exception
    {
        Iterator<FrameData> frames = framesData.iterator();
        while(frames.hasNext())  // za svaki okvir
        {
            FrameData frame = frames.next();  // trenutni okvir
            bc.debug(frame.toString());
            BigDecimal totalExposureAmountHRK = frame.getTotalExposureAmountHRK();  // suma izloženosti svih plasmana iz okvira
            
            bc.beginTransaction();
            BigDecimal remainingToCover = totalExposureAmountHRK;  // iznos izloženosti koji je preostao za pokrivanje
            
            Iterator<CollateralCategoryData> categories = frame.collateralCategories.values().iterator();
            while(categories.hasNext())  // za svaku kategoriju kolaterala
            {
                CollateralCategoryData category = categories.next();  // trenutna kategorija kolaterala
                bc.debug("|  " + category);
                BigDecimal totalCoverageAmountHRK = category.getTotalCoverageAmountHRK();  // suma iznosa kojom kolaterali iz kategorije pokrivaju okvir  
                
                BigDecimal availableForCoverage = totalCoverageAmountHRK;  // iznos raspoloživ za pokrivanje
                if(availableForCoverage.compareTo(remainingToCover) > 0) availableForCoverage = remainingToCover;  // ako je iznos za pokrivanje veæi od preostalog iznosa, onda iznos za pokrivanje = preostali iznos
                
                Iterator<AccountData> accounts = frame.accounts.iterator();
                while(accounts.hasNext())  // za svaki plasman iz okvira
                {
                    AccountData account = accounts.next();  // trenutni plasman iz okvira
                    bc.debug("|  |  " + account);
                    
                    Iterator<CollateralData> collaterals = category.collaterals.iterator();
                    while(collaterals.hasNext())  // za svaki kolateral iz kategorije
                    {
                        CollateralData collateral = collaterals.next();  // trenutni kolateral iz kategorije
                        bc.debug("|  |  |  " + collateral);
                        
                        BigDecimal accountRatio = BigDecimal.ZERO;  // omjer1: izloženost trenutnog plasmana / izloženost svih plasmana iz okvira
                        BigDecimal collateralRatio = BigDecimal.ZERO;  // omjer2: iznos pokrivanja trenutnog kolaterala / iznos pokrivanja svih kolaterala iz kategorije
                        if(totalExposureAmountHRK.compareTo(BigDecimal.ZERO) > 0) accountRatio = account.exposureAmountHRK.divide(totalExposureAmountHRK, 10, RoundingMode.HALF_UP);
                        if(totalCoverageAmountHRK.compareTo(BigDecimal.ZERO) > 0) collateralRatio = collateral.coverageAmountHRK.divide(totalCoverageAmountHRK, 10, RoundingMode.HALF_UP);
                        BigDecimal amount = availableForCoverage.multiply(accountRatio).multiply(collateralRatio).setScale(2, RoundingMode.HALF_UP);  // iznos = omjer1 x omjer2 x raspoloživo za pokrivanje
                        bc.debug("|  |  |    -> " + amount + " kn");
                        
                        BigDecimal fra_acc_cov_id = bo521.insertIntoFrameAccountCov(col_pro_id, ref_col_pro_id, frame, account, collateral, amount, exchangeRate);  // zapiši pokrivenost plasmana u bazu podataka
                        if(fra_acc_cov_id == null) return false;
                        
                        if(!bo521.insertIntoInDataDwhItem(col_pro_id, account.cus_acc_id, frame.cus_acc_no, fra_acc_cov_id)) return false;  // zabilježi obraðeni zapis u tablicu IN_DATA_DWH_ITEM 
                    }
                }
                
                remainingToCover = remainingToCover.subtract(availableForCoverage);  // preostali iznos = preostali iznos - iznos za pokrivanje
                if(remainingToCover.compareTo(BigDecimal.ZERO) <= 0) break;  // ako je preostali iznos <= 0, izaði iz petlje
            }
            
            bc.commitTransaction();  // napravi commit izraèuna pokrivenosti za plasmane iz trenutnog okvira
        }
        return true;
    }

    /**
     * Metoda dohvaæa ulazne parametre, te provjerava njihov broj i ispravnost.
     * Obradi se predaje bank sign i identifikator referentne vrste obrade.
     * @return da li su dohvat i provjera uspješno završili
     */
    private boolean getParameters()
    {
        try
        {
            int brojParametara = bc.getArgs().length;
            for(int i = 0; i < brojParametara; i++) bc.debug("Parametar " + i + ". = " + bc.getArg(i));  // ispis parametara                   
            if(brojParametara == 2)
            {
                if(bc.getArg(0).compareTo("RB") != 0) throw new Exception("Prvi parametar mora biti 'RB'!");
                bc.setBankSign(bc.getArg(0));
                ref_proc_type = bc.getArg(1).trim().toUpperCase();
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
        BatchParameters batchParameters = new BatchParameters(new BigDecimal("2708554424"));
        batchParameters.setArgs(args);
        new BO520().run(batchParameters);
    }
}